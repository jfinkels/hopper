package perseus.document.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import perseus.document.Chunk;
import perseus.document.ChunkSchemes;
import perseus.document.Metadata;
import perseus.document.Query.QueryElement;
import perseus.ie.freq.Frequency;
import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.util.HibernateDAO;
import perseus.util.HibernateUtil;
import perseus.util.ResultsIterator;
import perseus.util.StringUtil;

public class HibernateChunkDAO extends HibernateDAO<HibernateChunkDAO.ChunkRow> implements ChunkDAO {
    private static class ChunkIterator extends ResultsIterator<Chunk> {
	private Criteria criteria;
	
	public ChunkIterator(Criteria crit) {
	    super(2000);
	    criteria = crit;
	}
	
	protected Collection<Chunk> getResults(int firstHit, int maxHits) {
	    criteria.setFirstResult(firstHit).setMaxResults(maxHits);
	    
	    List<ChunkRow> results = criteria.list();
	    
	    List<Chunk> chunks = new ArrayList<Chunk>(results.size());
	    for (ChunkRow result : results) {
		chunks.add(result.toChunk());
		HibernateUtil.getSession().evict(result);
	    }
	    return chunks;
	}
    }

    private static final Comparator<Chunk> valueComparator =
	new Comparator<Chunk>() {
	
	public int compare(Chunk c1, Chunk c2) {
	    try {
		int value1 = Integer.parseInt(c1.getValue());
		int value2 = Integer.parseInt(c2.getValue());
		
		return value1 - value2;
	    } catch (NumberFormatException nfe) {
		return c1.getValue().compareTo(c2.getValue());
	    }
	}
    };
    
    private static final int SAVE_BEFORE_FLUSHING = 20;
    private int chunksSaved = 0;
    
    public void save(Chunk chunk) {
		super.save(chunkToRow(chunk));
//		getSession().save(chunkToRow(chunk));
/*		registerSave();*/
    }
    
    private void registerSave() {
	chunksSaved++;
	if (chunksSaved >= SAVE_BEFORE_FLUSHING) {
/*	    endTransaction();*/
	    getSession().flush();
	    getSession().clear();
/*	    HibernateUtil.closeSession();
	    beginTransaction();
*/	    
		chunksSaved = 0;
	}
    }
    
    public void delete(Chunk chunk) {
	super.delete(chunkToRow(chunk));
	//getSession().delete(chunkToRow(chunk));
    }

    public static ChunkRow chunkToRow(Chunk chunk) {
	if (chunk instanceof RowBackedChunk) {
	    return ((RowBackedChunk) chunk).row;
	}

	if (chunk.getId() != null) {
	    ChunkRow row = (ChunkRow) getSession().load(ChunkRow.class, chunk.getId());
	    row.setFrequencies(chunk.getFrequencies());
	    return row;
	}
	
	ChunkRow row = new ChunkRow();
	row.setId(chunk.getId());
	row.setElementName(chunk.getElementName());
	
	row.setDocumentID(chunk.getDocumentID());
	row.setType(chunk.getType());
	row.setValue(chunk.getValue());
	row.setPosition(chunk.getPosition());
	row.setAbsolutePosition(chunk.getAbsolutePosition());
	row.setChunkID(chunk.getChunkID());
	row.setOpenTags(chunk.getOpenTags());
	row.setCloseTags(chunk.getCloseTags());
	row.setStartOffset(chunk.getStartOffset());
	row.setEndOffset(chunk.getEndOffset());
	row.setLemma(chunk.getLemma());
	row.setFrequencies(chunk.getFrequencies());
/*  row.setSenses(chunk.getSenses());*/

	row.setDisplayQuery(chunk.getDisplayQuery());
	row.setHead(chunk.getHead());
	row.setHeadLanguage(chunk.getHeadLanguage());
	row.setHasCustomHead(chunk.getHasCustomHead());

	return row;
   }

    public void updateChunk(Chunk chunk) {
	ChunkRow row = chunkToRow(chunk);
	// ACK need merge instead of update because Hibernate keeps throwing
	// NonUniqueObjectExceptions
	HibernateUtil.getSession().merge(row);
	//update(row);
    }
    
    public void cleanup() {
	HibernateUtil.closeSession();
    }
    
    public void delete(String documentID, String type, String value) {	
	List<String> conditions = new ArrayList<String>();
	Map<String,String> parameters = new HashMap<String,String>();
	
	if (documentID != null) {
	    conditions.add("c.documentID = :documentID");
	    parameters.put("documentID", documentID);
	}
	if (type != null) {
	    conditions.add("c.type = :type");
	    parameters.put("type", type);
	}
	if (value != null) {
	    conditions.add("c.value = :value");
	    parameters.put("value", value);
	}
	
	String queryString = "delete from HibernateChunkDAO$ChunkRow c " +
	(conditions.isEmpty() ?
		"" : " where " + StringUtil.join(conditions, " and "));

	Query query = getSession().createQuery(queryString);

	for (String term : parameters.keySet()) {
	    String paramValue = parameters.get(term);
	    query.setParameter(term, paramValue);
	}

	query.executeUpdate();
    }
    
    public void deleteByDocument(String documentID) {
	delete(documentID, null, null);
    }
    
    public Chunk getById(int id) {
	Object result = getSession().load(ChunkRow.class, new Integer(id));
	return (result != null) ? ((ChunkRow) result).toChunk() : null;
    }
    
    public Chunk getByQuery(perseus.document.Query query) {
	perseus.document.Query finalQuery = new perseus.document.Query(query);
	List<QueryElement> queryElements = finalQuery.getQueryElements();
	
	Chunk targetChunk = new Chunk();
	targetChunk.setDocumentID(query.getDocumentID());
	
	for (int i = 3, n = queryElements.size(); i < n; i++) {
	    // sigh...
	    QueryElement element = queryElements.get(i);
	    String type = element.getType();
	    String value = element.getValue();
	    
	    if (type.equalsIgnoreCase("line")) {
		// A little hand-waving to make card breaks mandatory
		type = "card";
	    }
	    
	    if (type.equalsIgnoreCase("card")) {
		targetChunk =
		    getPrecedingContainedChunk(targetChunk, type, value);
		if (targetChunk != null) {
		    // Make sure the chunk's query knows that we jiggled
		    // the line value
		    element.setValue(targetChunk.getValue());
		}
	    } else if (value.startsWith("abs=")) {
		int absolutePosition =
		    Integer.parseInt(value.replaceAll("abs=",""));
		targetChunk = getByAbsolutePosition(targetChunk, absolutePosition); 
	    } else if (value.startsWith("pos=")) {
		int position = Integer.parseInt(value.replaceAll("pos=",""));
		targetChunk = getByPosition(targetChunk, type, position);
	    } else {
		targetChunk = getFirstContainedChunk(targetChunk, type, value);
	    }
	    if (targetChunk == null) break;
	}
	
	return targetChunk;
    }
    
    public Chunk getPrecedingContainedChunk(
	    Chunk chunk, String type, String value) {
	
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	addChunkConstraints(crit, chunk);
	
	// This is slightly trickier than it looks; since the "value" field
	// is a string, not an int, we can't just sort.
	//
	// Load all the chunks contained in the chunk that we have, then
	// sort them and figure out where the chunk we want would go; this
	// will tell us which chunk to return.
	
	Chunk dummyChunk = new Chunk();
	dummyChunk.setType(type);
	dummyChunk.setValue(value);
	
	List<Chunk> containedChunks = getContainedChunks(chunk, type);
	
	if (containedChunks.isEmpty()) return null;
	
	Collections.sort(containedChunks, valueComparator);
	int chunkPosition =
	    Collections.binarySearch(containedChunks, dummyChunk, valueComparator);
	// binarySearch() returns:
	//  index of matching element, if one was found
	//  otherwise, -(insertion point) + 1.
	//
	//  We want the element just before the insertion point, which would
	//  be the element preceding this chunk if it were in the array.
	
	
	int targetIndex = (chunkPosition > -1) ?
		chunkPosition : -chunkPosition - 2;
	
	Chunk targetChunk = null;
	if (targetIndex >= 0) {
	    targetChunk = containedChunks.get(targetIndex);
	} else {
	    targetChunk = containedChunks.get(0);
	}
	
	targetChunk.setQuery(chunk.getQuery().appendSubquery(type, value));
	return targetChunk;
    }
    
    public Chunk getByDisplayQuery(String documentID, String subquery) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	crit.add(Restrictions.eq("documentID", documentID));
	crit.add(Restrictions.eq("displayQuery", subquery));
	
	Object result = crit.uniqueResult();
	return (result != null) ? ((ChunkRow) result).toChunk() : null;
    }
    
    public Chunk getByChunkID(String documentID, String chunkID) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	crit.add(Restrictions.eq("documentID", documentID));
	crit.add(Restrictions.eq("chunkID", chunkID));
	
	Object result = crit.uniqueResult();
	return (result != null) ? ((ChunkRow) result).toChunk() : null;
    }
    
    public Chunk getFirstChunkForScheme(String documentID, String scheme) {
	return getFirstChunkForScheme(documentID, scheme, null);
    }
    
    public Chunk getFirstChunkForScheme(String documentID, String scheme,
	    String targetType) {
	if (targetType == null) {
	    targetType = ChunkSchemes.defaultTypeForScheme(scheme);
	}
	List<String> types = ChunkSchemes.typesForScheme(scheme);
	
	// The document ID we've been passed may in fact represent a subdoc;
	// as such, it may have some additional elements (e.g., "speech=1").
	// If it does, make sure we find the chunks matching the given values.
	perseus.document.Query workingQuery =
	    new perseus.document.Query(documentID);
	Chunk targetChunk = Chunk.getInitialChunk(workingQuery);
	
	Chunk lastFound = null;
	for (String type : types) {
	    // If the document ID we were passed actually included something
	    // for a subdocument, make sure to search for the subdoc, not
	    // just the first chunk we see.
	    
	    // (this will equal null if the query has no value)
	    String existingValue = workingQuery.getValueForType(type);
	    targetChunk = getFirstContainedChunk(
		    targetChunk, type, existingValue);
	    
	    if (targetChunk == null) break;
	    
	    if (!workingQuery.containsType(targetChunk.getType())) {
		workingQuery = workingQuery.appendSubquery(
			targetChunk.getType(), targetChunk.getValue());
	    }
	    
	    lastFound = targetChunk;
	    
	    if (type.equals(targetType)) {
		break;
	    }
	}
	
	if (lastFound != null) {
	    lastFound.setQuery(workingQuery);
	}
	
	return lastFound;
    }
    
    public List<Chunk> getContainedChunks(Chunk chunk, String type) {
	List<String> types = new ArrayList<String>();
	types.add(type);
	return getContainedChunks(chunk, types);
    }
    
    public List<Chunk> getContainedChunks(Chunk chunk, List<String> types) {
	return getContainedChunks(chunk, types, false);
    }
    
    @SuppressWarnings("unchecked")
    public List<Chunk> getContainedChunks(Chunk chunk, List<String> types,
	    boolean includeOverlaps) {
	
	Criteria crit = getContainedChunksCriteria(chunk, types, includeOverlaps);
	
	List<ChunkRow> results = crit.list();
	return rowsToChunks(results);
    }

    private Criteria getContainedChunksCriteria(Chunk chunk, List<String> types, boolean includeOverlaps) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	if (types != null && !types.isEmpty()) {
	    crit.add(Restrictions.in("type", types));
	}
	
	crit.add(Restrictions.eq("documentID",
		chunk.getQuery().getDocumentID()));
	
	Integer startOffset = new Integer(chunk.getStartOffset());
	Integer endOffset = new Integer(chunk.getEndOffset());
	if (includeOverlaps) {
	    addDocumentIDConstraint(crit, chunk);
	    
	    // If we're allowing overlapping chunks, we have three cases:
	    // 1. chunks contained within the parent, as usual
	    // 2. chunks that start before the end of the parent, but end
	    // after the parent
	    // 3. chunks that end after the start of the parent, but start
	    // before the parent
	    crit.add(Restrictions.disjunction()
		    .add(
			Restrictions.conjunction()
			.add(Restrictions.ge("startOffset", startOffset))
			.add(Restrictions.le("endOffset", endOffset)))
		    .add(
			Restrictions.conjunction()
			.add(Restrictions.lt("startOffset", endOffset))
			.add(Restrictions.ge("endOffset", endOffset)))
		    .add(
			Restrictions.conjunction()
			.add(Restrictions.gt("endOffset", startOffset))
			.add(Restrictions.le("startOffset", startOffset))));
	} else {
	    addChunkConstraints(crit, chunk);
	}
	return crit;
    }

    public Iterator<Chunk> getContainedChunksIterator(
	    Chunk chunk, String type) {
	List<String> types = new ArrayList<String>();
	types.add(type);
	return getContainedChunksIterator(chunk, types);
    }
    
    public Iterator<Chunk> getContainedChunksIterator(
	    Chunk chunk, List<String> types) {
	return getContainedChunksIterator(chunk, types, false);
    }
    
    public Iterator<Chunk> getContainedChunksIterator(
	    Chunk chunk, List<String> types, boolean includeOverlaps) {

	Criteria crit = getContainedChunksCriteria(chunk, types, includeOverlaps);
//	ScrollableResults results = crit.scroll(ScrollMode.FORWARD_ONLY);
	return new ChunkIterator(crit);
    }
    
    public List<Integer> getAllChunkIDs(String documentID) {
    	List<Integer> chunkIDs = getSession().createQuery("select c.id from HibernateChunkDAO$ChunkRow c where " +
    			"documentID = :docID").setParameter("docID", documentID).list();
    	
    	return chunkIDs;
    }
   
    public List<Chunk> getAllChunks(String documentID) {
	return getAllChunks(documentID, new ArrayList<String>());
    }
    
    public List<Chunk> getAllChunks(String documentID, String type) {
	List<String> types = new ArrayList<String>();
	types.add(type);
	return getAllChunks(documentID, types);
    }
    
    @SuppressWarnings("unchecked")
    public List<Chunk> getAllChunks(String documentID, List<String> types) {
	Criteria crit = getAllChunksCriteria(documentID, types);

	List<ChunkRow> results = crit.list();
	return rowsToChunks(results);
    }

    private List<Chunk> rowsToChunks(List<ChunkRow> results) {
	List<Chunk> chunks = new ArrayList<Chunk>();
	for (ChunkRow result : results) {
	    chunks.add(result.toChunk());
	}
	
	return chunks;
    }

    private Criteria getAllChunksCriteria(
	String documentID, List<String> types) {

	Criteria crit = getSession().createCriteria(ChunkRow.class);
	
	if (documentID != null) {
	    crit.add(Restrictions.eq("documentID", documentID));
	}
	if (types != null && !types.isEmpty()) {
	    crit.add(Restrictions.in("type", types));
	}
	crit.addOrder(Order.asc("startOffset"))
	.addOrder(Order.desc("endOffset"));
	
	return crit;
    }

    public Iterator<Chunk> getChunkIterator(
	String documentID, String type) {
    
	List<String> types = new ArrayList<String>();
	types.add(type);

	return getChunkIterator(documentID, types);
    }

    public Iterator<Chunk> getChunkIterator(
	String documentID, List<String> types) {

	Criteria crit = getAllChunksCriteria(documentID, types);
	return new ChunkIterator(crit);
    }
    
    public Chunk getFirstChunk(String documentID, String type, String value) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	
	if (documentID != null) {
	    crit.add(Restrictions.eq("documentID", documentID));
	}
	if (type != null) {
	    crit.add(Restrictions.eq("type", type));
	}
	if (value != null) {
	    crit.add(Restrictions.eq("value", value));
	}
	crit.addOrder(Order.asc("startOffset"))
	.addOrder(Order.desc("endOffset"));
	
	crit.setMaxResults(1);
	
	List<ChunkRow> results = crit.list();
	if (results.isEmpty()) return null;

	Chunk chunk = results.get(0).toChunk();
	chunk.setQuery(new perseus.document.Query(documentID)
			.appendSubquery(type, value));
	return chunk;
    }
    
    public Chunk getFirstContainedChunk(Chunk chunk) {
	return getFirstContainedChunk(chunk, null, null);
    }
    
    public Chunk getFirstContainedChunk(Chunk chunk, String type, String value) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	
	if (type != null) {
	    crit.add(Restrictions.eq("type", type));
	} else {
	    crit.add(Restrictions.ne("type", chunk.getType()));
	}
	
	if (value != null) {
	    crit.add(Restrictions.eq("value", value));
	}
	addChunkConstraints(crit, chunk);
	
	crit.addOrder(Order.asc("startOffset"));
	
	// For arabic texts, make sure to match chunk on case.
	boolean matchCase=Language.forCode(chunk.getQuery().getMetadata().get(Metadata.LANGUAGE_KEY)).getAdapter().matchCase();

	// For lexica, the language may be english while the subject (and chunking standard) may be arabic
	if (Language.forCode(chunk.getMetadata().get(Metadata.SUBJECT_LANGUAGE_KEY)) == Language.ARABIC) {
		matchCase=true;
	}
	
	if (matchCase) {
		List<ChunkRow> results = crit.list();
		if (results.isEmpty()) {
			return null;
		}
		for (ChunkRow chunkrow : results) {
			Chunk thischunk=chunkrow.toChunk();
			perseus.document.Query query=thischunk.getQuery();
			if (query.containsType(type) && query.getValueForType(type).equals(value)) {
				return thischunk;
			}
		}
	}
	
	//Since we just want the first result in the list, don't ever get the entire list
	//(which can run out of memory for very large texts) but just the first result
	List<ChunkRow> firstResult = crit.setMaxResults(1).list();
	if (firstResult.isEmpty()) {
		return null;
	}
	ChunkRow result = (ChunkRow) firstResult.get(0);
	Chunk foundChunk = result.toChunk();
	
	perseus.document.Query parentQuery = chunk.getQuery();
	perseus.document.Query childQuery = parentQuery;

	if (!childQuery.containsType(foundChunk.getType())) {
	    childQuery = childQuery.appendSubquery(
		    foundChunk.getType(), foundChunk.getValue());
	}
	foundChunk.setQuery(childQuery);
	return foundChunk;
    }
    
    /**
     * This method does NOT set a query on the resulting chunk, since it's
     * impossible to know whether the "previous" chunk shares the same parent
     * chunk as its successor.
     */
    public Chunk getPreviousChunk(Chunk chunk) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	addDocumentIDConstraint(crit, chunk);
	
	crit.add(Restrictions.lt("startOffset",
		new Integer(chunk.getStartOffset())))
		.add(Restrictions.eq("type", chunk.getType()))
		.setMaxResults(1)
		.addOrder(Property.forName("startOffset").desc());
	
	List results = crit.list();
	if (results.isEmpty()) return null;
	
	Chunk result = ((ChunkRow) results.get(0)).toChunk();
	return result;
    }
    
    /**
     * This method does NOT set a query on the resulting chunk, since it's
     * impossible to know whether the "next" chunk shares the same parent
     * chunk as its predecessor.
     */
   public Chunk getNextChunk(Chunk chunk) {
	List results = getSession().createQuery(
	    "from HibernateChunkDAO$ChunkRow where startOffset > :startOffset and "
	    + "type = :type and documentID = :documentID "
	    + "order by startOffset asc")
	    .setInteger("startOffset", chunk.getStartOffset())
	    .setString("documentID", chunk.getDocumentID())
	    .setString("type", chunk.getType())
	    .setMaxResults(1)
	    .list();

	if (results.isEmpty()) return null;
	
	Chunk result = ((ChunkRow) results.get(0)).toChunk();
	return result;
    }
    
    private void addChunkConstraints(Criteria crit, Chunk chunk) {
	if (chunk != null) {
	    addDocumentIDConstraint(crit, chunk);
	    
	    Integer startOffset = new Integer(chunk.getStartOffset());
	    Integer endOffset = new Integer(chunk.getEndOffset());
	    
	    // Some chunks, if they've been specified using milestones,
	    // don't overlap neatly with their parent chunks, like
	    // chapters/sections in some documents. Thus, we specify only
	    // that a chunk must start somewhere within this chunk--we say
	    // nothing about where it must end.
	    if (chunk.getStartOffset() != -1 && chunk.getEndOffset() != -1) {
		crit.add(Restrictions.ge("startOffset", startOffset))
		.add(Restrictions.le("startOffset", endOffset));
	    }
	}
    }
    
    private void addDocumentIDConstraint(Criteria crit, Chunk chunk) {
	if (chunk != null) {
	    crit.add(Restrictions.eq("documentID",
		    chunk.getQuery().getDocumentID()));
	}
    }
    
    public Chunk getByPosition(Chunk parent, String type, int position) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	
	addChunkConstraints(crit, parent);
	crit.add(Restrictions.eq("type", type))
	.add(Restrictions.eq("position", position));
	
	Object result = crit.uniqueResult();
	if (result == null) return null;
	
	Chunk chunk = ((ChunkRow) result).toChunk();
	chunk.setQuery(parent.getQuery().appendSubquery(
		chunk.getType(), "pos=" + chunk.getPosition()));
	return chunk;
    }
    
    public Chunk getByAbsolutePosition(Chunk parent, int position) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	
	addChunkConstraints(crit, parent);
	crit.add(Restrictions.eq("absolutePosition", position));
	
	Object result = crit.uniqueResult();
	if (result == null) return null;
	
	Chunk chunk = ((ChunkRow) result).toChunk();
	chunk.setQuery(parent.getQuery().appendSubquery(
		chunk.getType(), "abs=" + chunk.getAbsolutePosition()));
	return chunk;
    }
    
    public Chunk getContainingChunk(Chunk child, String type) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	addDocumentIDConstraint(crit, child);
	
	crit.add(Restrictions.le("startOffset", child.getStartOffset()))
		.add(Restrictions.ge("endOffset", child.getEndOffset()))
		.add(Restrictions.eq("type", type))
		.addOrder(Property.forName("startOffset").desc());
	
	crit.setMaxResults(1);
	List results = crit.list();
	
	if (results.isEmpty()) return null;

	Chunk chunk = ((ChunkRow) results.get(0)).toChunk();
	chunk.setQuery(child.getQuery().getContainingQuery());
	return chunk;
    }

    public Chunk getByExample(Chunk exemplar) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	crit.add(Restrictions.eq("documentID", exemplar.getDocumentID()))
		.add(Restrictions.eq("type", exemplar.getType()))
		.add(Restrictions.eq("position", exemplar.getPosition()));
	
	crit.setMaxResults(1);
	List results = crit.list();
	
	return (results.isEmpty() ? null :
	    ((ChunkRow) results.get(0)).toChunk());
    }

    public int getChunkCount(String documentID) {
	return getChunkCount(documentID, new ArrayList<String>());
    }

    public int getChunkCount(String documentID, String type) {
	List<String> types = new ArrayList<String>();
	types.add(type);
	return getChunkCount(documentID, types);
    }

    public int getChunkCount(String documentID, List<String> types) {
	Criteria crit = getSession().createCriteria(ChunkRow.class);
	
	crit.add(Restrictions.eq("documentID", documentID));
	if (!types.isEmpty()) {
	    crit.add(Restrictions.in("type", types));
	}
	crit.setProjection(Projections.rowCount());
	
	return (Integer) crit.uniqueResult();
    }

	private static class RowBackedChunk extends Chunk {
		ChunkRow row;
		
		public RowBackedChunk(ChunkRow row) {
			this.row = row;
		}
		
		public Integer getId() { return row.getId(); }
		public String getDocumentID() { return row.getDocumentID(); }
		public String getElementName() { return row.getElementName(); }
		public String getType() { return row.getType(); }
		public String getValue() { return row.getValue(); }
		public int getPosition() { return row.getPosition(); }
		public int getAbsolutePosition() { return row.getAbsolutePosition(); }
		public String getChunkID() { return row.getChunkID(); }
		public String getOpenTags() { return row.getOpenTags(); }
		public String getCloseTags() { return row.getCloseTags(); }
		public int getStartOffset() { return row.getStartOffset(); }
		public int getEndOffset() { return row.getEndOffset(); }

		public String getDisplayQuery() { return row.getDisplayQuery(); }
		public String getHead() { return row.getHead(); }
		public String getHeadLanguage() { return row.getHeadLanguage(); }
		public boolean getHasCustomHead() { return row.isHasCustomHead(); }
		public Lemma getLemma() { return row.getLemma(); }
		public Set<Frequency> getFrequencies() { return row.getFrequencies(); }
/*      public List<Sense> getSenses() { return row.getSenses(); }*/
		
		
		public void setId(Integer id) { row.setId(id); }
		public void setDocumentID(String did) { row.setDocumentID(did); }
		public void setElementName(String en) { row.setElementName(en); }
		public void setType(String t) { row.setType(t); }
		public void setValue(String v) { row.setValue(v); }
		public void setPosition(int p) { row.setPosition(p); }
		public void setAbsolutePosition(int ap) { row.setAbsolutePosition(ap); }
		public void setChunkID(String ci) { row.setChunkID(ci); }
		public void setOpenTags(String ot) { row.setOpenTags(ot); }
		public void setCloseTags(String ct) { row.setCloseTags(ct); }
		public void setStartOffset(int so) { row.setStartOffset(so); }
		public void setEndOffset(int eo) { row.setEndOffset(eo); }

		public void setDisplayQuery(String dq) { row.setDisplayQuery(dq); }
		public void setHead(String h) { row.setHead(h); }
		public void setHeadLanguage(String hl) { row.setHeadLanguage(hl); }
		public void setHasCustomHead(boolean hch) { row.setHasCustomHead(hch); }
		public void setLemma(Lemma l) { row.setLemma(l); }
		public void setFrequencies(Set<Frequency> f) { row.setFrequencies(f); }
/*      public void setSenses(List<Sense> s) { row.setSenses(s); }*/
	}
    
    /**
     * Internal implementation class, to prevent Hibernate's cache/persistence
     * from being affected by modifications to mutable Chunk attributes (like
     * calls to setQuery()).
     */
    public static class ChunkRow {
	private Integer id;
	private String documentID;
	private String elementName;
	private String type;
	private String value;
	private int position;
	private int absolutePosition;
	private String chunkID;
	private String openTags;
	private String closeTags;
	private int startOffset;
	private int endOffset;
	
	private String displayQuery;
	private String head;
	private String headLanguage;
	private boolean hasCustomHead;
	private Lemma lemma;
	private Set<Frequency> frequencies;
/*  private List<Sense> senses;*/
	
	public Set<Frequency> getFrequencies() {
	    return frequencies;
	}

	public void setFrequencies(Set<Frequency> frequencies) {
	    this.frequencies = frequencies;
	}

	public ChunkRow() {}
	
	public ChunkRow(String docID) {
	    documentID = docID;
	}
	
	public Chunk toChunk() {
		RowBackedChunk chunk = new RowBackedChunk(this);
		return chunk;
	}
	
	public int getAbsolutePosition() {
	    return absolutePosition;
	}

	public void setAbsolutePosition(int absolutePosition) {
	    this.absolutePosition = absolutePosition;
	}

	public String getChunkID() {
	    return chunkID;
	}

	public void setChunkID(String chunkID) {
	    this.chunkID = chunkID;
	}

	public String getCloseTags() {
	    return closeTags;
	}

	public void setCloseTags(String closeTags) {
	    this.closeTags = closeTags;
	}

	public String getDisplayQuery() {
	    return displayQuery;
	}

	public void setDisplayQuery(String displayQuery) {
	    this.displayQuery = displayQuery;
	}

	public String getDocumentID() {
	    return documentID;
	}

	public void setDocumentID(String documentID) {
	    this.documentID = documentID;
	}

	public int getEndOffset() {
	    return endOffset;
	}

	public void setEndOffset(int endOffset) {
	    this.endOffset = endOffset;
	}

	public boolean isHasCustomHead() {
	    return hasCustomHead;
	}

	public void setHasCustomHead(boolean hasCustomHead) {
	    this.hasCustomHead = hasCustomHead;
	}

	public String getHead() {
	    return head;
	}

	public void setHead(String head) {
	    this.head = head;
	}

	public String getHeadLanguage() {
	    return headLanguage;
	}

	public void setHeadLanguage(String headLanguage) {
	    this.headLanguage = headLanguage;
	}

	public Integer getId() {
	    return id;
	}

	public void setId(Integer id) {
	    this.id = id;
	}

	public String getOpenTags() {
	    return openTags;
	}

	public void setOpenTags(String openTags) {
	    this.openTags = openTags;
	}

	public int getPosition() {
	    return position;
	}

	public void setPosition(int position) {
	    this.position = position;
	}

	public int getStartOffset() {
	    return startOffset;
	}

	public void setStartOffset(int startOffset) {
	    this.startOffset = startOffset;
	}

	public String getType() {
	    return type;
	}

	public void setType(String type) {
	    this.type = type;
	}

	public String getValue() {
	    return value;
	}

	public void setValue(String value) {
	    this.value = value;
	}

	public String getElementName() {
	    return elementName;
	}

	public void setElementName(String elementName) {
	    this.elementName = elementName;
	}

	public Lemma getLemma() {
	    return lemma;
	}

	public void setLemma(Lemma lemma) {
	    this.lemma = lemma;
	}
/*  
    public void setSenses(List<Sense> newSenses) {
        senses = newSenses;
    }

    public List<Sense> getSenses() {
        return senses;
    }
*/	
    }
}
