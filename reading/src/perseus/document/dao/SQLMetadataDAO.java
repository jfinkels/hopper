/**
 * Implements methods for saving and retrieving Metadata objects to/from
 * our MySQL database using JDBC. Wraps SQLExceptions in a
 * MetadataAccessException class, which is non-checked and therefore does
 * not have to be caught.
 */
package perseus.document.dao;

import static perseus.document.Metadata.CONTRIBUTOR_KEY;
import static perseus.document.Metadata.CREATOR_KEY;
import static perseus.document.Metadata.TITLE_KEY;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import perseus.document.Metadata;
import perseus.document.MetadataAccessException;
import perseus.document.MetadataStore;
import perseus.document.Query;
import perseus.language.Language;
import perseus.util.SQLHandler;
import perseus.util.StringUtil;

public class SQLMetadataDAO implements MetadataDAO {

    private static Logger logger = Logger.getLogger(SQLMetadataDAO.class);
    public static final String TABLE_NAME = "metadata";

    private static QueryRunner getQueryRunner() {
	return new QueryRunner(SQLHandler.getDataSource());
    }

    public void clear() throws MetadataAccessException {
	try {
	    QueryRunner runner = getQueryRunner();
	    runner.update("TRUNCATE TABLE " + TABLE_NAME);
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }

    public void reload(Metadata m) throws MetadataAccessException {
	delete(m);
	save(m);
    }

    public MetadataStore getDocumentMetadata(String documentID)
	throws MetadataAccessException {

	try {
	    MetadataHandler metadataHandler = new MetadataHandler();
	    Object result = getQueryRunner().query(
		    "SELECT * FROM " + TABLE_NAME + " WHERE document_id = ? " +
		    " ORDER BY subquery, key_name, position ASC",
		    documentID, metadataHandler);

	    return (MetadataStore) result;
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }

    public List<Query> getDocuments(String keyName, String value,
		String valueID) throws MetadataAccessException {
	return getDocuments(keyName, value, valueID, false);
    }

    public List<Query> getDocuments(String keyName, String value,
		String valueID, boolean ignoreSubdocs)
	    throws MetadataAccessException {

	try {
	    List<String> parameters = new ArrayList<String>();
	    List<String> queryStrings = new ArrayList<String>();

	    parameters.add(keyName);
	    queryStrings.add("key_name = ?");

	    if (value != null) {
		parameters.add(value);
		queryStrings.add("value = ?");
	    }
	    if (valueID != null) {
		parameters.add(valueID);
		queryStrings.add("value_id = ?");
	    }

	    String queryString = "SELECT DISTINCT document_id, subquery FROM "
		+ TABLE_NAME + " WHERE "
		+ StringUtil.join(queryStrings, " AND ");

	    Object results = getQueryRunner().query(queryString,
		    parameters.toArray(),
		    new QueryHandler(ignoreSubdocs));
	    Set<Query> resultSet = (Set<Query>) results;

	    return new ArrayList<Query>(resultSet);
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }
    
    public List<Query> getDocuments(String keyName, List<String> values, boolean ignoreSubdocs)
    	    throws MetadataAccessException {

    	try {
    		List<String> parameters = new ArrayList<String>();
    	    List<String> queryStrings = new ArrayList<String>();

    	    parameters.add(keyName);
    	    queryStrings.add("key_name = ?");

    	    if (values != null) {
    		queryStrings.add("value in ('" + StringUtils.join(values.toArray(), "','") + "')");
    	    }
    	    queryStrings.add("document_id like 'Perseus:text:%'");

    	    String queryString = "SELECT DISTINCT document_id, subquery FROM "
    		+ TABLE_NAME + " WHERE "
    		+ StringUtil.join(queryStrings, " AND ");

    	    Object results = getQueryRunner().query(queryString,
    		    parameters.toArray(),
    		    new QueryHandler(ignoreSubdocs));
    	    Set<Query> resultSet = (Set<Query>) results;

    	    return new ArrayList<Query>(resultSet);
    	} catch (SQLException sqle) {
    	    throw new MetadataAccessException(sqle);
    	}
        }

    public Metadata getMetadata(Query query) throws MetadataAccessException {
	try {
	    QueryRunner runner = getQueryRunner();

	    MetadataHandler metadataHandler = new MetadataHandler();

	    String queryPrefix = "SELECT * FROM " + TABLE_NAME +
		" WHERE document_id = ? AND ";
	    String querySuffix = " ORDER BY key_name ASC, position ASC";

	    Object result;
	    if (query.isJustDocumentID()) {
		result = runner.query(
			queryPrefix + " subquery IS NULL " + querySuffix,
			query.getDocumentID(),
			metadataHandler);
	    } else {
		result = runner.query(
			queryPrefix + " subquery = ? " + querySuffix,
			new Object[] {query.getDocumentID(), query.getQuery()},
			metadataHandler);
	    }

	    // this should be a MetadataStore with only one item.
	    MetadataStore store = (MetadataStore) result;
	    return store.get(query);
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }

    public void delete(Metadata metadata) throws MetadataAccessException {
	try {
	    QueryRunner runner = getQueryRunner();
	    Query targetQuery = metadata.getQuery();

	    if (targetQuery.isJustDocumentID()) {
		runner.update("DELETE FROM " + TABLE_NAME + " WHERE "
			+ "document_id = ? AND subquery IS NULL",
			targetQuery.getDocumentID());
	    } else {
		runner.update("DELETE FROM " + TABLE_NAME + " WHERE "
			+ "document_id = ? AND subquery = ?",
			new Object[] {
			targetQuery.getDocumentID(),
			targetQuery.getQuery()});
	    }
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }

    public void save(Metadata metadata) throws MetadataAccessException {
	try {
	    QueryRunner runner = getQueryRunner();

	    List<Object[]> paramList = new ArrayList<Object[]>();

	    String documentID = metadata.getQuery().getDocumentID();
	    String subquery = metadata.getQuery().getQuery();

	    for (String key : metadata.getKeys()) {
		// skip the internal fields
		if (key.startsWith("internal:")) {
		    continue;
		}

		List<Metadata.NodeValue> nodeValues = metadata.getNodeValues(key);
		int index = 0;
		for (Metadata.NodeValue nv : nodeValues) {
		    String value = nv.getValue();
		    String valueID = nv.getValueID();
		    String language = null;
		    if (nv.getLanguage() != null) {
			language = nv.getLanguage().getCode();
		    }

		    paramList.add(new Object[] { documentID, subquery,
			    key, value, valueID, language, index++});
		}
	    }

	    Object[][] params = new Object[paramList.size()][7];
	    for (int i = 0; i < params.length; i++) {
		params[i] = (Object []) paramList.get(i);
	    }

	    runner.batch("INSERT INTO " + TABLE_NAME
		    + " VALUES (?, ?, ?, ?, ?, ?, ?)", params);
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }

    public List<String> getDistinctValues(String keyName)
	throws MetadataAccessException {

	return doGetDistinct(keyName, "value");
    }

    public List<String> getDistinctValueIDs(String keyName)
	throws MetadataAccessException {

	return doGetDistinct(keyName, "value_id");
    }

    private List<String> doGetDistinct(String keyName, String targetField) 
	    throws MetadataAccessException {
	try {
	    QueryRunner runner = getQueryRunner();

	    Object result = runner.query("SELECT DISTINCT " + targetField +
		    " FROM " + TABLE_NAME + " WHERE key_name = ?" +
		    " ORDER BY position ASC",
		    keyName, new ArrayListHandler());

	    String[] resultList = (String[]) result;
	    return Arrays.asList(resultList);
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }

    /**
     * Convenience method for saving an entire MetadataStore at once.
     */
    public void save(MetadataStore store) throws MetadataAccessException {
	for (Iterator<Metadata> it = store.metadataIterator(); it.hasNext(); ) {
	    Metadata metadata = it.next();
	    save(metadata);
	}
    }

    private class QueryHandler implements ResultSetHandler {
	private boolean ignoreSubdocuments;

	public QueryHandler() { this(false); }
	public QueryHandler(boolean is) { ignoreSubdocuments = is; }

	public boolean ignoreSubdocuments() { return ignoreSubdocuments; }
	public void setIgnoreSubdocuments(boolean is) {
	    ignoreSubdocuments = is;
	}

	public Object handle(ResultSet rs) throws SQLException {
	    Set<Query> queries = new HashSet<Query>();

	    while (rs.next()) {
		String documentID = rs.getString("document_id");
		String subquery = rs.getString("subquery");

		Query query;
		if (subquery != null && !ignoreSubdocuments) {
		    query = new Query(documentID, subquery);
		} else {
		    query = new Query(documentID);
		}

		queries.add(query);
	    }

	    return queries;
	}
    }

    private class MetadataHandler implements ResultSetHandler {
	public Object handle(ResultSet rs) throws SQLException {
	    MetadataStore store = new MetadataStore();

	    while (rs.next()) {
		String documentID = rs.getString("document_id");
		String subquery = rs.getString("subquery");
		String key = rs.getString("key_name");

		String valTemp = rs.getString("value");
		String value = null;
		if (valTemp != null) {
		    try {
		    	byte[] bytes = valTemp.getBytes("utf-8");
		    	value = new String(bytes);
		    } catch (UnsupportedEncodingException uee) {
			logger.warn("Bad encoding: " + uee);
			value = valTemp;
		    }
		}

		/*Blob valBlob = rs.getBlob("value");
		String value = null;
		if (valBlob != null) {
		    byte[] bytes = valBlob.getBytes(1L, (int) valBlob.length());
		    try {
			value = new String(bytes, "utf-8");
		    } catch (UnsupportedEncodingException uee) {
			logger.warn("Bad encoding:" + uee);
			value = new String(bytes);
		    }
		    }*/
		

		String valueID = rs.getString("value_id");
		Language language = Language.forCode(rs.getString("language"));

		Query currentQuery;
		if (subquery != null) {
		    currentQuery = new Query(documentID, subquery);
		} else {
		    currentQuery = new Query(documentID);
		}

		Metadata metadata;
		if (store.containsKey(currentQuery)) {
		    metadata = store.get(currentQuery);
		} else {
		    metadata = new Metadata(currentQuery);
		    store.put(currentQuery, metadata);
		}
		metadata.addField(key, value, valueID, language);
	    }
	    
	    return store;
	}
    }
    
    private class TermHandler implements ResultSetHandler {
    	public Object handle(ResultSet rs) throws SQLException {
    		Map<String,Set<Query>> matches =
    			new HashMap<String,Set<Query>>();

    		Set<String> queryDocIds = new TreeSet<String>();
    		while (rs.next()) {
    			String documentID = rs.getString("document_id");
    			String subquery = rs.getString("subquery");
    			String key = rs.getString("key_name");

    			Query currentQuery;
    			if (subquery != null) {
    				currentQuery = new Query(documentID, subquery);
    			} else {
    				currentQuery = new Query(documentID);
    			}

    			Set<Query> matchesForKey = matches.get(key);
    			if (matchesForKey == null) {
    				matchesForKey = new TreeSet<Query>();
    				matches.put(key, matchesForKey);
    			}
    			if (!queryDocIds.contains(currentQuery.getDocumentID())) {
    				queryDocIds.add(currentQuery.getDocumentID());
    				matchesForKey.add(currentQuery);
    			}
    		}

    		return matches;
    	}
    }

    public Map<String,Set<Query>> getDocumentsWithTerm(
	    String term, int firstHit, int maxHits) {
	try {
	    QueryRunner runner = getQueryRunner();
	    
	    String limitString = "";
	    if (maxHits != -1 && firstHit > 0) {
		limitString = String.format("LIMIT %d, %d", firstHit, maxHits);
	    } else if (maxHits != -1) {
		limitString = String.format("LIMIT %d", maxHits);
	    }
	    
//	    String searchParam = String.format("%%%s%%", term);
	    String[] keyFields = new String[] {
		    TITLE_KEY, CREATOR_KEY, CONTRIBUTOR_KEY
	    };

	    Object result = runner.query("SELECT distinct document_id, subquery, key_name " +
		    " FROM " + TABLE_NAME + " WHERE key_name IN ('" +
		    StringUtils.join(keyFields, "','") + "') AND MATCH(value) AGAINST (?) " +
		    		"and document_id like 'Perseus:text%'" +
		    limitString,
		    term, new TermHandler());

	    return (Map<String,Set<Query>>) result;
	} catch (SQLException sqle) {
	    throw new MetadataAccessException(sqle);
	}
    }
}
