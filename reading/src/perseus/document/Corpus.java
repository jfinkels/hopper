package perseus.document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.language.LanguageCode;
import perseus.util.SQLHandler;
import perseus.util.StringUtil;

/**
 * Class that encapsulates a Query and adds various methods for functionality
 * with collections and corpora.
 *
 * @see Query
 * @see Metadata
 */
public class Corpus {
    
    private Query query;
    
    private static Logger logger = Logger.getLogger(Corpus.class);
    
    /**
     * A class to represent the type of corpus, as defined in our metadata:
     *
     * <ul>
     *  <li>work - a single work</li>
     *  <li>author - all works by a given author</li>
     *  <li>conglomerate - a more general collection, like "Greek Prose" or
     *  "Greek Texts"</li>
     * </ul>
     *
     * This exists only because some callers ask for corpora by type; there is
     * no difference in behavior between the different types.
     */
    public enum Type {
	WORK("work"),
	AUTHOR("author"),
	CONGLOMERATE("conglomerate");
	
	private String type;
	
	private Type(String t) {
	    type = t;
	}

	public String getType() { return type; }
    }
    
    private static Map<String,String> xmlCache = new HashMap<String,String>();
    
    public static CollectionData classicsCollection;
	
	static {
		classicsCollection = loadCollection("Perseus:collection:Greco-Roman");
	}
	
	public static CollectionData loadCollection(String collectionID) {
		Corpus corpus = new Corpus(collectionID);		
		List<Metadata> documents = new ArrayList<Metadata>();

		// Get metadata for each document in collection
		Iterator<Query> idIterator = corpus.getCorpusDocuments().iterator();
		while (idIterator.hasNext()) {
		    Metadata documentMetadata = ((Query) idIterator.next()).getMetadata();
		    // We shouldn't have any non-text documents in this collection, but just make sure
		    if (documentMetadata.has(Metadata.TYPE_KEY) &&
			        ! documentMetadata.getType().equals("text")) {
			        continue;
			    }
		    // Ignore documents with status = 3
		    if (documentMetadata.has(Metadata.STATUS_KEY) &&
			        documentMetadata.get(Metadata.STATUS_KEY).equals("3")) {
			        continue;
			    }
		    
		    // Some Civil War documents don't seem to have titles, or link to
		    // actual texts. ignore them--don't show the user a document named "null"
		    // that produces an error when clicked.
		    if (documentMetadata.has(Metadata.TITLE_KEY) &&
			    !documentMetadata.getTitle().equals("null")) {
			documents.add(documentMetadata);
		    }
		}

		// Sort the metadata objects in their natural order
		Collections.sort(documents);
		
		List<List<Metadata>> allDocuments  = new ArrayList<List<Metadata>>();
		Map<Metadata, List<Query>> hasSubDocs = new HashMap<Metadata, List<Query>>();

		List<Metadata> similarEntries = new ArrayList<Metadata>();
		
		for (Metadata docMetadata : documents) {
		    hasSubDocs.put(docMetadata, checkHasSubDocs(docMetadata));
		    String author = docMetadata.getCreator();

		    // If there is a set of entries with the same author:
		    if (similarEntries.size() > 0) {
		    	// Grab the 'current' past entry (if there is one)
		    	Metadata previousEntry = (Metadata) similarEntries.get(0);

		    	// If the previous author is the same as the current, add it to the list
		    	if (previousEntry.getCreator() != null && previousEntry.getCreator().equals(author)) {
		    		similarEntries.add(docMetadata);
		    	}

		    	// Otherwise, add this list to big list,
		    	// create new list, and add documentMetadata as the only item
		    	else {
		    		allDocuments.add(similarEntries);
		    		similarEntries = new ArrayList<Metadata>();
		    		similarEntries.add(docMetadata);
		    	}
		    } else {
				similarEntries.add(docMetadata);
			}
		}
		// Add any extras
		if (similarEntries.size() > 0) {
			allDocuments.add(similarEntries);
		}
				
		return new CollectionData(allDocuments, hasSubDocs);
	}
	
	private static List<Query> checkHasSubDocs (Metadata metadata) {
		boolean hasSubDocs = false;
		List<Query> subdocQueries = new ArrayList<Query>();

		if (metadata.has(Metadata.SUBDOC_REF_KEY)) {
			List<Chunk> subdocs = new Chunk(metadata.getQuery()).getSubTexts();
			if (subdocs.size() <= 1) {
				hasSubDocs = false;
			} else {
				for (Chunk subdoc : subdocs) {
					Query subquery = subdoc.getQuery();
					subdocQueries.add(subquery);
				}
				if (subdocQueries.size() == 1) {
					hasSubDocs = false;
				} else {
					hasSubDocs = true;
				}
			}
		}
		
		if (hasSubDocs) {
			return subdocQueries;
		} else {
			return null;
		}
	}
        
    public Corpus (String id) {
	this(new Query(id));
    }
    
    public Corpus (Query query) {
	this.query = query;
    }

    /**
     * Returns a list of Query objects representing all documents that belong
     * to this Corpus.
     */
    public List<Query> getCorpusDocuments() {
	try {			
	    MetadataDAO dao = new SQLMetadataDAO();
	    List<Query> queries =
		dao.getDocuments(Metadata.CORPUS_KEY, null, query.getDocumentID(), true);
	    
	    Collections.sort(queries);
	    return queries;
	} catch (MetadataAccessException se) {
	    logger.warn("Problem getting corpus documents", se);
	    return new ArrayList<Query>();
	}
    }
    
    // Like getCorpusDocuments(), but returns subdocuments instead of outer
    // documents where possible.
    public List<Query> getCorpusWorks() {
	try {			
	    MetadataDAO dao = new SQLMetadataDAO();
	    List<Query> queries =
		dao.getDocuments(Metadata.CORPUS_KEY, null, query.getDocumentID(), false);
	    
	    Collections.sort(queries);
	    List<Query> subdocQueries = new ArrayList<Query>();
	    for (Query documentQuery : queries) {
		if (!documentQuery.getMetadata().hasSubdocs()) {
		    subdocQueries.add(documentQuery);
		}
	    }
	    return subdocQueries;
	} catch (MetadataAccessException se) {
	    logger.warn("Problem getting corpus documents", se);
	    return new ArrayList<Query>();
	}
    }
    
    /** This method returns the ids of all corpora of a given type.
     Valid values are:
     <ul>
     <li>work (Aristotle's Poetics)
     <li>author (Aristotle)
     <li>conglomerate (Greek Philosophy)
     </ul>
     */
    public static List<Corpus> getCorporaByType(Type type) {
	List<Corpus> corpora = new ArrayList<Corpus>();
	try {
	    MetadataDAO dao = new SQLMetadataDAO();
	    
	    List<Query> queries = dao.getDocuments(
		    Metadata.CORPUS_TYPE_KEY, type.getType(), null, true);
	    
	    for (Query query : queries) {
		corpora.add(new Corpus(query));
	    }
	    
	} catch (MetadataAccessException se) {
	    logger.warn("Problem getting corpora for type " + type, se);
	}
	return corpora;
    }
    
    /**
     * Returns all corpora whose type matches any element of <kbd>types</kbd>.
     */
    public static List<Corpus> getCorporaByTypes(EnumSet<Type> types) {
	List<Corpus> corpora = new ArrayList<Corpus>();
	for (Type type : types) {
	    corpora.addAll(getCorporaByType(type));
	}
	
	return corpora;
    }
    
    /** A collection isn't REALLY a corpus, but they're handled in much the
     same way.
     */
    public static List<Corpus> getCollections() {
	try {
	    MetadataDAO dao = new SQLMetadataDAO();
	    
	    List<Query> queries = dao.getDocuments(
		    Metadata.TYPE_KEY, "collection", null, true);
	    
	    List<Corpus> corpora = new ArrayList<Corpus>();
	    for (Query query : queries) {
		corpora.add(new Corpus(query));
	    }
	    
	    return corpora;
	} catch (MetadataAccessException se) {
	    logger.warn("Problem getting collections", se);
	    return new ArrayList<Corpus>();
	}
    }
    
    /**
     * Returns all known ABO IDs.
     */
    public static List<String> getABOs() {
	List<String> output = new ArrayList<String>();
	
	ResultSet rs = null;
	Connection con = null;
	SQLHandler sqlHandler = null;
	
	try {
	    con = SQLHandler.getConnection();
	    
	    String sql = "select distinct document_id from metadata where document_id like '%:abo:%'";
	    
	    sqlHandler = new SQLHandler(con);
	    rs = sqlHandler.executeQuery(sql);
	    
	    while (rs.next()) {
		output.add (rs.getString("document_id"));
	    }
	    
	} catch (SQLException e) {
	    logger.error("Problem retrieving all ABOs", e);
	} finally {
	    try {
		if (sqlHandler != null) {
		    sqlHandler.releaseAll();
		}
	    } catch (SQLWarning w) {
		logger.error("Problem releasing resources", w);
	    }
	    try {
		if (con != null) con.close();
	    } catch (SQLException s) {
		logger.error("Problem releasing connection", s);
	    }
	}
	
	return output;
	
    }
    
    /**
     * This returns an array of two Maps (String pointing to a list of Works).
     * The first Map consists of creators of primary documents, the second of
     * secondary documents.
     *
     * This was once used for displaying works by author, and editions by work,
     * but it currently serves no purpose.
     */
    public Map[] getCreators() {
	
	Map<String,Set<Work>> primaryCreators =
	    new TreeMap<String,Set<Work>>();
	Map<String,Set<Work>> secondaryCreators =
	    new TreeMap<String,Set<Work>>();
	Map<String,List<Query>> documentsByCreator = getDocumentsByCreator();
	
	for (String creator : documentsByCreator.keySet()) {
	    // Grab all the documents at once
	    List<Query> documents = documentsByCreator.get(creator);
	    
	    // Split them up into primary/secondary texts based on the
	    // Arity metadata field
	    List<Query> primaryTexts = new ArrayList<Query>();
	    List<Query> secondaryTexts = new ArrayList<Query>();
	    
	    for (Query query : documents) {
		String arity = query.getMetadata().getArity();
		
		if (arity != null && arity.equalsIgnoreCase("Secondary")) {
		    secondaryTexts.add(query);
		} else {
		    primaryTexts.add(query);
		}
	    }
	    
	    if (!primaryTexts.isEmpty()) {
		Set<Work> primaryWorks = getWorksForDocuments(primaryTexts);
		if (primaryWorks.isEmpty()) {
		    primaryWorks = worksFromTexts(primaryTexts);
		}
		
		primaryCreators.put(creator, primaryWorks);
	    }
	    
	    if (!secondaryTexts.isEmpty()) {
		Set<Work> secondaryWorks = worksFromTexts(secondaryTexts);
		
		secondaryCreators.put(creator, secondaryWorks);
	    }
	    
	}
	return new Map[] {primaryCreators, secondaryCreators};
    }
    
    /**
     * Returns a Map of Strings (creators) to lists of Queries (documents).
     */
    private Map<String,List<Query>> getDocumentsByCreator() {
	
	Map<String,List<Query>> creators = new HashMap<String,List<Query>>(); // String (creator) -> List (docs)
	SQLHandler sqlHandler = null;
	Connection con = null;
	Map<String,String> documentsProcessed = new HashMap<String,String>();
	
	try {
	    con = SQLHandler.getConnection();
	    
	    String sql = "select m1.docuemnt_id, m1.subquery, m1.value from "
		+ "metadata m1 inner join metadata m2 using "
		+ "(document_id, subquery) where "
		+ "m2.key_name = 'dc:Relation:IsPartOf' and "
		+ "m2.value_id = '" + StringUtil.sqlEscape(query.getDocumentID()) + "' "
		+ "and m1.key_name = 'dc:Creator'";
	    /*
	     String sql = "select document_id, subquery, value from metadata where document_id in (select"
	     + " document_id from metadata where typekey = 'Relation' and valueid = '"
	     + corpusID + "') and typekey = 'Creator'";
	     */
	    
	    sqlHandler = new SQLHandler(con);
	    ResultSet rs = sqlHandler.executeQuery(sql);
	    
	    while (rs.next()) {
		String documentID = rs.getString("id");
		String subdoc = rs.getString("subdoc");
		
		Query docQuery = new Query(documentID, subdoc);
		
		String creator = rs.getString("value");
		
		// Some documents have multiple creators, like the LSJ. If we
		// see the same document multiple times, combine the names of
		// the creators and remove the document from the previous
		// creator's list.
		if (documentsProcessed.containsKey(documentID)) {
		    
		    String oldCreator = documentsProcessed.get(documentID);
		    List<Query> oldDocuments = creators.get(oldCreator);
		    
		    // If the document to be moved was the previous creator's
		    // only work we've seen thus far, we'll need to remove
		    // the old creator from our collection.
		    oldDocuments.remove(docQuery);
		    if (oldDocuments.isEmpty()) {
			creators.remove(oldCreator);
		    }
		    
		    creator = oldCreator + ", " + creator;
		}
		documentsProcessed.put(documentID, creator);
		
		List<Query> creatorDocuments;
		if (creators.containsKey(creator)) {
		    creatorDocuments = creators.get(creator);
		} else {
		    creatorDocuments = new ArrayList<Query>();
		    creators.put(creator, creatorDocuments);
		}
		
		creatorDocuments.add(docQuery);
	    }
	    
	} catch (SQLException e) {
	    logger.error("Problem retrieving all ABOs", e);
	} finally {
	    try {
		if (sqlHandler != null) {
		    sqlHandler.releaseAll();
		}
	    } catch (SQLWarning w) {
		logger.error("Problem releasing resources", w);
	    }
	    try {
		if (con != null) con.close();
	    } catch (SQLException s) {
		logger.error("Problem releasing connection", s);
	    }
	}
	
	return creators;
    }
    
    /**
     * Returns a Set of Works for a given group of documents.
     */
    private Set<Work> getWorksForDocuments(List documents) {
	
	Map<Query,Work> works = new TreeMap<Query,Work>(); // Query (abo) -> Work
	
	Connection con = null;
	SQLHandler sqlHandler = null;
	
	try {
	    con = SQLHandler.getConnection();
	    
	    // Grab primary texts
	    String sql = "select id, subdoc, valueid from metadata where id in ('"
		+ StringUtil.join(documents, "','") + "') and typekey = "
		+ "'Relation' and subkey = 'IsVersionOf'";
	    
	    sqlHandler = new SQLHandler(con);
	    ResultSet rs = sqlHandler.executeQuery(sql);
	    
	    while (rs.next()) {
		String documentID = rs.getString("id");
		String subdoc = rs.getString("subdoc");
		
		String abo = rs.getString("valueid");
		Query aboQuery = new Query(abo);
		
		Work currentWork;
		if (works.containsKey(aboQuery)) {
		    currentWork = (Work) works.get(aboQuery);
		} else {
		    currentWork = new Work(aboQuery);
		    works.put(aboQuery, currentWork);
		}
		
		currentWork.addText(new Query(documentID, subdoc));
	    }
	    
	    // ...then grab commentaries
	    sql = "select id, subdoc, valueid from metadata where valueid in ('"
		+ StringUtil.join(works.keySet(), "','") + "') and typekey = "
		+ "'Relation' and subkey = 'IsCommentaryOn'";
	    
	    rs = sqlHandler.executeQuery(sql);
	    
	    while (rs.next()) {
		String commentaryID = rs.getString("id");
		String subdoc = rs.getString("subdoc");
		
		String abo = rs.getString("valueid");
		Query aboQuery = new Query(abo);
		
		Work currentWork;
		if (works.containsKey(aboQuery)) {
		    currentWork = (Work) works.get(aboQuery);
		} else {
		    currentWork = new Work(aboQuery);
		    works.put(aboQuery, currentWork);
		}
		
		currentWork.addCommentary(new Query(commentaryID, subdoc));
	    }
	} catch (SQLException e) {
	    logger.error("Problem retrieving works", e);
	} finally {
	    try {
		if (sqlHandler != null) {
		    sqlHandler.releaseAll();
		}
	    } catch (SQLWarning w) {
		logger.error("Problem releasing resources", w);
	    }
	    try {
		if (con != null) con.close();
	    } catch (SQLException s) {
		logger.error("Problem releasing connection", s);
	    }
	}
	
	return new HashSet<Work>(works.values());
    }
    
    /**
     * Renders the corpus as XML, aggregated by author and then by work.
     * This is relatively complex, because we wamt tp group works by ABO
     * but also make sure that we include works that don't have ABOs.
     */
    public String toXML() {
	
	String xml;
	String corpusID = query.getDocumentID();
	
	if (xmlCache.containsKey(corpusID)) {
	    logger.debug("Found cached copy! " + corpusID);
	    
	    xml = xmlCache.get(corpusID);
	} else {
	    StringBuffer xmlBuffer = new StringBuffer();
	    
	    xmlBuffer.append("<corpus>\n");
	    xmlBuffer.append("<name>");
	    xmlBuffer.append(query.getMetadata().getTitle());
	    xmlBuffer.append("</name>\n");
	    
	    // This will be an Array of Maps<String, Set<Work>>
	    //
	    // Returning the creators as an array is clunky, but at this point
	    // it's not worth the overhead to create a new class to hold
	    // primary and secondary texts.
	    Map[] creators = getCreators();
	    Map primaryCreators = creators[0];
	    Map secondaryCreators = creators[1];
	    
	    xmlBuffer.append("<primaryTexts>");
	    
	    for (Iterator it = primaryCreators.keySet().iterator();
	    it.hasNext(); ) {
		String creator = (String) it.next();
		Set<Work> works = (Set) primaryCreators.get(creator);
		
		printWorks(creator, works, xmlBuffer);
	    }
	    xmlBuffer.append("</primaryTexts>\n");
	    
	    xmlBuffer.append("<secondaryTexts>");
	    for (Iterator it = secondaryCreators.keySet().iterator();
	    it.hasNext(); ) {
		String creator = (String) it.next();
		Set<Work> works = (Set) secondaryCreators.get(creator);
		
		printWorks(creator, works, xmlBuffer);
	    }
	    xmlBuffer.append("</secondaryTexts>\n");
	    xmlBuffer.append("</corpus>\n");
	    
	    xml = xmlBuffer.toString();
	    xmlCache.put(corpusID, xml);
	    
	    logger.debug("Cached newly-created copy of " + corpusID);
	}
	
	return xml;
    }
    
    private void printWorks(String creator, Set<Work> works, StringBuffer output) {
	output.append("<creator>\n");
	output.append("<name>");
	output.append(creator);
	output.append("</name>\n");
	output.append("<works>\n");
	
	for (Work work : works) {
	    output.append(work.toXML());
	}
	
	output.append("</works>\n");
	output.append("</creator>\n");
    }
    
    public String getID() {
	return query.getDocumentID();
    }
    
    public Query getQuery() {
	return query;
    }
    
    public Metadata getMetadata() {
	return query.getMetadata();
    }
    
    /**
     * Convenience method for creating Works from a collection of Queries
     * representing texts that lack ABOs. We fake it by using each Query's
     * document ID as the ABO ID and create a new Work for each one.
     */
    private Set<Work> worksFromTexts(Collection<Query> documents) {
	
	Set<Work> works = new HashSet<Work>();
	
	for (Query query : documents) {
	    Work work = new Work(query);
	    work.addText(query);
	    works.add(work);
	}
	
	return works;
    }
    
    public static class CollectionData {
		List<List<Metadata>> allDocuments;
		Map<Metadata, List<Query>> hasSubDocs;
		
		public List<List<Metadata>> getAllDocuments() {
			return allDocuments;
		}

		public void setAllDocuments(List<List<Metadata>> allDocuments) {
			this.allDocuments = allDocuments;
		}

		public Map<Metadata, List<Query>> getHasSubDocs() {
			return hasSubDocs;
		}

		public void setHasSubDocs(Map<Metadata, List<Query>> hasSubDocs) {
			this.hasSubDocs = hasSubDocs;
		}

		public CollectionData(List<List<Metadata>> allDocuments,
				Map<Metadata, List<Query>> hasSubDocs) {
			this.allDocuments = allDocuments;
			this.hasSubDocs = hasSubDocs;
		}
    }
    
    private class Work {
	
	Query aboQuery;
	
	Metadata metadata;
	
	Set<Query> texts = new TreeSet<Query>();
	Set<Query> commentaries = new TreeSet<Query>();
	
	public Work(Query query) {
	    aboQuery = query;
	    metadata = MetadataCache.get(aboQuery);
	}
	
	public void addText(Query text) {
	    texts.add(text);
	}
	
	public void addTexts(List<Query> texts) {
	    texts.addAll(texts);
	}
	
	public void addCommentary(Query comm) {
	    commentaries.add(comm);
	}
	
	public void addCommentaries(List comms) {
	    commentaries.addAll(comms);
	}
	
	public Query getQuery() {
	    return aboQuery;
	}
	
	public Metadata getMetadata() {
	    return metadata;
	}
	
	public Set<Query> getTexts() {
	    return texts;
	}
	
	public Set<Query> getCommentaries() {
	    return commentaries;
	}
	
	public String toXML() {
	    StringBuffer buffer = new StringBuffer();
	    
	    buffer.append("<work>\n");
	    
	    buffer.append("<id>");
	    buffer.append(aboQuery);
	    buffer.append("</id>");
	    
	    String title = "Works";
	    if (metadata != null) {
		title = metadata.get(Metadata.TITLE_KEY);
	    }
	    buffer.append("<title>");
	    buffer.append(title);
	    buffer.append("</title>\n");
	    
	    if (!texts.isEmpty()) {
		buffer.append("<texts>\n");
		
		for (Query query : texts) {
		    buffer.append("<text>");
		    printTextData(query, buffer);
		    buffer.append("</text>\n");
		}
		buffer.append("</texts>\n");
	    }
	    
	    if (!commentaries.isEmpty()) {
		buffer.append("<commentaries>\n");
		
		for (Query query : commentaries) {
		    buffer.append("<commentary>");
		    printTextData(query, buffer);
		    buffer.append("</commentary>\n");
		}
		buffer.append("</commentaries>\n");
	    }
	    
	    buffer.append("</work>\n");
	    
	    return buffer.toString();
	}
	
	private void printTextData(Query query, StringBuffer output) {
	    
	    Metadata metadata = query.getMetadata();
	    
	    output.append("<title>");
	    output.append(metadata.get(Metadata.TITLE_KEY));
	    output.append("</title>\n");
	    
	    String editor = metadata.get(Metadata.CONTRIBUTOR_KEY);
	    if (editor != null) {
		output.append("<editor>");
		output.append(editor);
		output.append("</editor>\n");
	    }
	    
	    if (metadata.has(Metadata.CREATOR_KEY)) {
		output.append("<author>");
		output.append(metadata.get(Metadata.CREATOR_KEY));
		output.append("</author>\n");
	    }
	    
	    output.append("<language>");
	    output.append(LanguageCode.getDisplayName(
		    metadata.get(Metadata.LANGUAGE_KEY)));
	    output.append("</language>\n");
	    
	    output.append("<documentID>");
	    output.append(query);
	    output.append("</documentID>\n");
	}
	
    }

    public Set<String> getDocumentIDs() {
	Set<String> docIDs = new HashSet<String>();
	for (Query document : getCorpusDocuments()) {
	    docIDs.add(document.getDocumentID());
	}
	
	return docIDs;
    }

    /**
     * Returns all known conglomerates.
     */
    public static List<Corpus> getConglomerates() {
	return getCorporaByType(Type.CONGLOMERATE);
    }
    
    /**
     * Returns all known corpora that correspond to a specific author.
     */
    public static List<Corpus> getAuthorCorpora() {
	return getCorporaByType(Type.AUTHOR);
    }
    
    public String toString() {
	return getID();
    }
}
