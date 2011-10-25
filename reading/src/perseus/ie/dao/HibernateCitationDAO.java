package perseus.ie.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateChunkDAO.ChunkRow;
import perseus.ie.Citation;
import perseus.util.HibernateDAO;
import perseus.util.HibernateUtil;
import perseus.util.StringUtil;

public class HibernateCitationDAO extends HibernateDAO<HibernateCitationDAO.CitationInternal>
implements CitationDAO {

    private Session session = HibernateUtil.getSession();

    private static final int ADD_BEFORE_FLUSHING = 5000;

    private int addedSinceFlush = 0;

    private static Logger logger = Logger.getLogger(HibernateCitationDAO.class);

    public HibernateCitationDAO() {
	beginTransaction();
    }

    public void beginTransaction() {
	super.beginTransaction();
	addedSinceFlush = 0;
    }

    public void cleanup() {
	endTransaction();
	HibernateUtil.closeSession();
    }

    public void insert(Citation citation) {
    try {
		super.save(CitationInternal.fromCitation(citation));
	} catch (InvalidQueryException e) {
		logger.warn("Could not insert citation: "+citation.toString());
	} catch (NullPointerException ne) {
	    //eh, ignore, just don't want CorpusProcessor to catch
	}
	registerAdd();
    }

    private void registerAdd() {
	addedSinceFlush++;
	if (addedSinceFlush > ADD_BEFORE_FLUSHING) {
	    logger.info("Added " + ADD_BEFORE_FLUSHING
		+ " entities; restarting transaction");

	    // It appears to be necessary to actually commit the session if we
	    // want Hibernate to drop its references to the entities it's
	    // stored so far. Otherwise, when we're working with humongous
	    // documents like the LSJ, we run out of memory.
	    endTransaction();
	    beginTransaction();
	}
    }
    
    public void deleteByDocumentID(String documentID) {
    	session.createQuery("delete from HibernateCitationDAO$CitationInternal where sourceDocumentID = :sourceID")
    	.setParameter("sourceID", documentID).executeUpdate();
    }

    public int clear(Query source, Query destination, String linkType) {
	StringBuffer queryBuffer = new StringBuffer();
	queryBuffer.append("delete from HibernateCitationDAO$CitationInternal");

	List<String> conditions = new ArrayList<String>();
	if (source != null) {
	    conditions.add("sourceDocumentID = :sourceID");
	    if (!source.isJustDocumentID()) {
		conditions.add("sourceSubquery = :sourcesub");
	    }
	}
	if (destination != null) {
	    conditions.add("resolvedDestinationDocumentID = :destinationID");
	    if (!destination.isJustDocumentID()) {
		conditions.add("resolvedDestinationSubquery = :destinationsub");
	    }
	}

	if (linkType != null) {
	    conditions.add("linkTypeCode = :linkTypeCode");
	}

	if (!conditions.isEmpty()) {
	    queryBuffer.append(" where ")
		.append(StringUtil.join(conditions, " and "));
	}

	org.hibernate.Query query = session.createQuery(queryBuffer.toString());
	if (source != null) {
	    query.setString("sourceID", source.getDocumentID());
	    if (!source.isJustDocumentID()) {
		query.setString("sourcesub", source.getQuery());
	    }
	}
	if (destination != null) {
	    query.setString("destinationID",
		    destination.getDocumentID());
	    if (!destination.isJustDocumentID()) {
		query.setString("destinationsub",
			destination.getQuery());
	    }
	}
	if (linkType != null) {
	    query.setParameter("linkTypeCode",
			    CitationInternal.getCodeForType(linkType));
	}
	return query.executeUpdate();
    }

    public Citation getCitationById(int id) {
	CitationInternal internalCit = (CitationInternal)
	    session.load(CitationInternal.class, new Integer(id));

	return internalCit.toCitation();
    }

    public List<Citation> getCitations(Query source, Query destination,
	    String linkType, int firstResult, int maxResults) {
	Criteria criteria = session.createCriteria(CitationInternal.class);
	if (source != null) {
	    criteria.add(Restrictions.eq("sourceDocumentID",
				    source.getDocumentID()));
	    if (!source.isJustDocumentID()) {
		criteria.add(Restrictions.eq("sourceSubquery",
				    source.getQuery()));
	    }
	}
	if (destination != null) {
	    criteria.add(Restrictions.eq("resolvedDestinationDocumentID",
				    destination.getDocumentID()));
	    if (!destination.isJustDocumentID()) {
		criteria.add(Restrictions.eq("resolvedDestinationSubquery",
				    destination.getQuery()));
	    }
	}
	if (linkType != null) {
	    criteria.add(Restrictions.eq("linkTypeCode", CitationInternal.getCodeForType(linkType)));
	}

	HibernateUtil.setResultRestrictions(criteria, firstResult, maxResults);

	return processResults(criteria.list());
    }

    public List<Citation> getOutgoingCitations(Query source, String linkType,
	    int firstResult, int maxResults) {
	return getCitations(source, null, linkType, firstResult, maxResults);
    }

    public List<Citation> getIncomingCitations(Query destination, String linkType,
	    int firstResult, int maxResults) {
	return getCitations(null, destination, linkType, firstResult,
		maxResults);
    }

    /**
     * Helper method to convert a list from our internal Hibernate
     * citations to actual Citation objects and eliminate duplicates
     * (that is, Citation objects with the same abstract destination but
     * different resolved destinations)..
     */
    private List<Citation> processResults(
	    List<CitationInternal> internalResults) {
	Map<Integer,Citation> destinationsFound =
		new HashMap<Integer,Citation>();

	for (CitationInternal intl : internalResults) {
	    // Record only one citation per original (= ABO'ed) destination;
	    // this prevents, e.g., multiple links to the mh=nis in the first
	    // line of the Odyssey from showing up if we're looking at the
	    // word's LSJ entry.
	    int hashCode = intl.uniqueDestinationHashCode();
	    if (!destinationsFound.containsKey(hashCode)) {
		destinationsFound.put(hashCode, intl.toCitation());
	    }
	}

	return new ArrayList<Citation>(destinationsFound.values());
    }

    /**
     * This is an internal, Hibernate-based representation of the Citation
     * class.  Notably, it splits up the Queries into separate document_id and
     * subquery fields that are more amenable to being stored in the database.
     * Also, it stores the link-type as an integer, not a String, to save a
     * little storage space.
     * 
     * This class is mostly useful because it shields the Citation class
     * from needing to know how it's stored in a given database. Hooray!
     */
    public static class CitationInternal {

	private String sourceDocumentID;
	private String sourceSubquery;
	private String destinationDocumentID;
	private String destinationSubquery;
	private String resolvedDestinationDocumentID;
	private String resolvedDestinationSubquery;
	
	private Integer linkTypeCode;
	private String sourceHeader;

	private Integer id;

	public CitationInternal() {}

	/**
	 * Factory method to create internal representations of  existing
	 * Citations.
	 */
	public static CitationInternal fromCitation(Citation c) throws InvalidQueryException, NullPointerException {
	    CitationInternal ci = new CitationInternal();

	    Query sourceQuery = c.getSource();
	    ci.setSourceDocumentID(sourceQuery.getDocumentID());
	    if (!sourceQuery.isJustDocumentID()) {
		ci.setSourceSubquery(sourceQuery.getQuery());
	    }

	    Query destinationQuery = c.getDestination();
	    ci.setDestinationDocumentID(destinationQuery.getDocumentID());
	    if (!destinationQuery.isJustDocumentID()) {
			ci.setDestinationSubquery(destinationQuery.getQuery());
	    }

	    Query resolvedDestinationQuery = c.getResolvedDestination();

	    //check for bad cits, which will throw an InvalidQueryException
	    //this adds time, but we need to make sure we aren't adding invalid queries,
	    //which would otherwise display on the website and it alerts us to problems in texts
	    resolvedDestinationQuery.getChunk();
	    
	    ci.setResolvedDestinationDocumentID(
		    resolvedDestinationQuery.getDocumentID());
	    if (!resolvedDestinationQuery.isJustDocumentID()) {
			ci.setResolvedDestinationSubquery(resolvedDestinationQuery.getQuery());
	    }

	    ci.setLinkTypeCode((Integer) typesToCodes.get(c.getLinkType()));
	    ci.setSourceHeader(c.getSourceHeader());

	    logger.debug(c + " -> " + ci);

	    return ci;
	}

	/**
	 * Converts the given internal citation to a Citation, ready to
	 * be processed by the outside world.
	 */
	public Citation toCitation() {
	    Citation cit = new Citation();
	    cit.setSource(new Query(sourceDocumentID, sourceSubquery));
	    cit.setDestination(new Query(destinationDocumentID, destinationSubquery));
	    cit.setResolvedDestination(new Query(resolvedDestinationDocumentID, resolvedDestinationSubquery));
	    cit.setLinkType((String) codesToTypes.get(linkTypeCode));
	    cit.setSourceHeader(sourceHeader);

	    logger.debug(this + " -> " + cit);

	    return cit;
	}

	public static final String[] citTypes =
	    {Citation.REF_COMMENTARY,
		Citation.REF_CITATION,
		Citation.REF_DEFAULT,
		Citation.REF_NOTES,
		Citation.REF_LEXICON,
		Citation.REF_INDEX,
		Citation.REF_SPECIFIC_LEXICON
	    };

	/**
	 * These type codes correspond to the codes used by the Perl hopper
	 * (except for REF_CiTATION, which is new).  There's no reason they
	 * have to, though, if we want to change them at some point.
	 */
	private static final Integer[] citTypeCodes =
	    {new Integer(10),
		new Integer(15),
		new Integer(20),
		new Integer(30),
		new Integer(35),
		new Integer(40),
		new Integer(37)
	    };

	private static Map<String,Integer> typesToCodes;
	private static Map<Integer,String> codesToTypes;

	static {
	    typesToCodes = new HashMap<String,Integer>();
	    codesToTypes = new HashMap<Integer,String>();

	    for (int i = 0; i < citTypes.length; i++) {
		typesToCodes.put(citTypes[i], citTypeCodes[i]);
		codesToTypes.put(citTypeCodes[i], citTypes[i]);
	    }
	}

	public static Integer getCodeForType(String linkType) {
	    return (Integer) typesToCodes.get(linkType);
	}

	public String getSourceDocumentID() {
	    return sourceDocumentID;
	}

	public String getSourceSubquery() {
	    return sourceSubquery;
	}

	public String getResolvedDestinationDocumentID() {
	    return resolvedDestinationDocumentID;
	}

	public String getResolvedDestinationSubquery() {
	    return resolvedDestinationSubquery;
	}

	public String getDestinationDocumentID() {
	    return destinationDocumentID;
	}

	public String getDestinationSubquery() {
	    return destinationSubquery;
	}

	public Integer getLinkTypeCode() {
	    return linkTypeCode;
	}

	public String getSourceHeader() { return sourceHeader; }
	public Integer getId() { return id; }

	public void setSourceDocumentID(String srcID) {
	    sourceDocumentID = srcID;
	}

	public void setSourceSubquery(String srcCit) {
	    sourceSubquery = srcCit;
	}

	public void setResolvedDestinationDocumentID(String destID) {
	    resolvedDestinationDocumentID = destID;
	}

	public void setResolvedDestinationSubquery(String destCit) {
	    resolvedDestinationSubquery = destCit;
	}

	public void setDestinationDocumentID(String destID) {
	    destinationDocumentID = destID;
	}

	public void setDestinationSubquery(String destCit) {
	    destinationSubquery = destCit;
	}

	public void setLinkTypeCode(Integer code) {
	    linkTypeCode = code;
	}

	public void setSourceHeader(String sh) { sourceHeader = sh; }

	public void setId(Integer id) { this.id = id; }

	public String toString() {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(printQuery("src", sourceDocumentID, sourceSubquery))
		.append("\n")
		.append(printQuery("dest", destinationDocumentID, destinationSubquery))
		.append("\n")
		.append(printQuery("res-dest",
			resolvedDestinationDocumentID,
			resolvedDestinationSubquery))
		.append("\n")
		.append("[src] ").append(sourceHeader).append("\n")
		.append("[ltype] ").append(linkTypeCode).append("\n");

	    return buffer.toString();
	}

	private String printQuery(String title,
		String documentID, String subquery) {
	    if (subquery == null) {
		return "[" + title + "] " + documentID;
	    }
	    return "[" + title + "] " + documentID + ":" + subquery;
	}

	/**
	 * This is like a hashCode() function, but it specifically ignores the
	 * resolved-destination, and so can be used to effectively reverse the
	 * call to expandABOQuery() we made when we extracted the citations.
	 */
	public int uniqueDestinationHashCode() {
	    int result = 17;

	    result = 37*result + sourceDocumentID.hashCode();
	    if (sourceSubquery != null) {
		result = 37*result + sourceSubquery.hashCode();
	    }
	    result = 37*result + destinationDocumentID.hashCode();
	    if (destinationSubquery != null) {
		result = 37*result + destinationSubquery.hashCode();
	    }
	    if (sourceHeader != null) {
		result = 37*result + sourceHeader.hashCode();
	    }
	    result = 37*result + linkTypeCode.hashCode();

	    return result;
	}
    }
}
