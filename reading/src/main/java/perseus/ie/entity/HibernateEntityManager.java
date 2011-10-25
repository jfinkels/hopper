package perseus.ie.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import perseus.document.Chunk;
import perseus.document.Corpus;
import perseus.document.InvalidQueryException;
import perseus.document.dao.HibernateChunkDAO;
import perseus.ie.entity.adapters.DateAdapter;
import perseus.ie.entity.adapters.DateRangeAdapter;
import perseus.ie.entity.adapters.EntityAdapter;
import perseus.ie.entity.adapters.PersonAdapter;
import perseus.ie.entity.adapters.PlaceAdapter;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.EntityTuple;
import perseus.util.HibernateUtil;
import perseus.util.StringUtil;

/**
 * Hibernate-based implementation of EntityManger.
 */
public class HibernateEntityManager implements EntityManager {

	private Session session;
	private Map<Class,EntityAdapter> adapters;

	// Only execute this many inserts before flushing the cache;
	// 20 is the value given in the documentation
	private static final int MAX_CACHE_SIZE = 20;
	private int addsSinceLastFlush;

	private Session getSession() {
		return HibernateUtil.getSession();
	}

	/** The maximum number of results this object's retrieval methods should
	 * fetch */
	private int maxResults = -1;

	/** The point at which this object's retrieval methods should begin */
	private int firstResult = -1;
	private String sortMethod = SORTABLE_STRING;
	private String sortOrder = ASCENDING;

	// Possible sort method values.
	public static final String DISPLAY_NAME = "displayName";
	public static final String AUTHORITY_NAME = "authorityName";
	public static final String SORTABLE_STRING = "sortableString";
	public static final String FIRST_POSITION = "firstPosition";
	public static final String COUNT = "count";
	public static final String CLASS_NAME = "entityClass";
	public static final String FREQUENCY = "frequency";
	public static final String DOCUMENT = "document";

	// Possible sort order values.
	public static final String ASCENDING = "asc";
	public static final String DESCENDING = "desc";

	/** These comparators are meant to be used with the maps from
	 *  the getAllEntityCounts() methods
	 */
	private class EntityCountComparator implements Comparator<Map> {
		private String comparatorKey;

		public EntityCountComparator(String key) {
			comparatorKey = key;
		}

		public int compare(Map m1, Map m2) {
			Comparable c = (Comparable) m1.get(comparatorKey);
			int result = c.compareTo(m2.get(comparatorKey));
			// If the comparison method we're using says that both elements
			// are equal, sort them by their sortable strings (otherwise we
			// can't put them both in a Set at once).
			if (result == 0) {
				String authName = (String) m1.get(SORTABLE_STRING);
				return authName.compareTo((String) m2.get(SORTABLE_STRING));
			}
			return result;
		}
	};

	public HibernateEntityManager() {
		super();

		adapters = new HashMap<Class,EntityAdapter>();
		adapters.put(Place.class, new PlaceAdapter());
		adapters.put(Person.class, new PersonAdapter());
		adapters.put(Date.class, new DateAdapter());
		adapters.put(DateRange.class, new DateRangeAdapter());
		//adapters.put(Lemma.class, new LemmaAdapter());
	}

	/**
	 * Signals the beginning of a transaction.
	 */
	public void beginWrite() {
		session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();
	}

	/**
	 * Signals the end of a transaction.
	 */
	public void endWrite() {
		HibernateUtil.commitTransaction();
		HibernateUtil.getSession().clear();
	}

	public void clearCache() {
		endWrite();
		beginWrite();
	}

	private void updateCache() {
		addsSinceLastFlush++;
		if (addsSinceLastFlush >= MAX_CACHE_SIZE) {
			clearCache();
			addsSinceLastFlush = 0;
		}
	}

	/**
	 * Returns the number of entities matching the given keyword.
	 *
	 * @param keyword the keyword to search for
	 * @param entityClass the Entity subclass to search for
	 * @param parameters any search/subclass-specific parameters
	 */
	public int getMatchingEntityCount(String keyword, Class entityClass, Map parameters) {
		EntityAdapter adapter = getAdapter(entityClass);
		return adapter.getMatchingEntityCount(keyword, parameters, getSession());
	}

	/**
	 * Returns the number of entities matching the given keyword.
	 *
	 * @param keyword the keyword to search for
	 * @param entityClass the Entity subclass to search for
	 */
	public List<? extends Entity> getMatchingEntities(String keyword, Class entityClass) {
		return getMatchingEntities(keyword, entityClass,
				Collections.EMPTY_MAP);
	}

	/**
	 * Returns any entities matching the given keyword/parameters, subject
	 * to the values of firstResult and maxResults.
	 *
	 * @param keyword the keyword to search for
	 * @param entityClass the Entity subclass to search for
	 * @param parameters any search/subclass-specific parameters
	 */
	public List<Entity> getMatchingEntities(String keyword, Class entityClass,
			Map parameters) {

		List<Entity> output = null;
		EntityAdapter adapter = getAdapter(entityClass);
		if (adapter == null) {
			throw new IllegalArgumentException("Couldn't find an appropriate "
					+ "method for adapter for class " + entityClass.getName());
		} else if (adapter.supportsMatchingEntityQuery()) {
			Query entityQuery = adapter.getMatchingEntityQuery(
					keyword, parameters, sortMethod, sortOrder, getSession());
			applyConfigurationRestrictions(entityQuery);

			entityQuery.setCacheable(true);
			output = entityQuery.list();

		} else if (adapter.supportsMatchingEntityCriteria()) {
			Criteria entityCriteria = adapter.getMatchingEntityCriteria(
					keyword, parameters, sortMethod, sortOrder, getSession());
			applyConfigurationRestrictions(entityCriteria);

			entityCriteria.setCacheable(true);
			output = entityCriteria.list();
		}

		if (output != null) {
			return output;
		} else {
			return new ArrayList<Entity>();
		}
	}

	/**
	 * Saves the given entity.
	 */
	public void registerEntity(Entity entity) {
		if (entity.getAuthorityName() == null) {
			throw new IllegalArgumentException("No auth name for " + entity);
		}
		entity.willBeRegistered(this);
		session.save(entity);
	}

	/**
	 * Updates the given entity.
	 */
	public void updateEntity(Entity entity) {
		if (entity.getAuthorityName() == null) {
			throw new IllegalArgumentException("No auth name for " + entity);
		}
		session.update(entity);
	}

	public Entity getEntityById(int id) {
		Session session = HibernateUtil.getSession();
		Entity entity = (Entity) session.load(Entity.class, new Integer(id));

		return entity;
	}

	/**
	 * Returns all entities found via a query by example on `exemplar`.
	 */
	public List<? extends Entity> getEntitiesByExample(Entity exemplar) {
		Example example = Example.create(exemplar);
		List results = getSession().createCriteria(exemplar.getClass())
		.add(example).list();

		return results;
	}

	/**
	 * Returns all entities matching `authName`.
	 */
	public List<? extends Entity> getEntitiesByAuthName(String authName) {
		Session session = HibernateUtil.getSession();
		Query query = session.createQuery(
		"from Entity where authorityName = :authName")
		.setParameter("authName", authName);

		return query.list();
	}

	/**
	 * Returns the first entity matching `authName`.
	 */
	public Entity getEntityByAuthName(String authName) {
		Session session = HibernateUtil.getSession();
		Query query = session.createQuery(
		"from Entity where authorityName = :authName")
		.setParameter("authName", authName);

		return (Entity) query.uniqueResult();
	}

	/**
	 * Removes the given entity.
	 */
	public void unregisterEntity(Entity entity) {
		entity.willBeUnregistered(this);
		Session session = HibernateUtil.getSession();
		List<? extends Entity> matches = getEntitiesByAuthName(entity.getAuthorityName());
		if (!matches.isEmpty()) {
			session.delete(entity);
		}
	}

	private EntityAdapter getAdapter(Class targetClass) {
		if (adapters.containsKey(targetClass)) {
			return (EntityAdapter) adapters.get(targetClass);
		}

		throw new IllegalArgumentException("No adapter for class "
				+ targetClass.getName());
	}

	/**
	 * Saves the given EntityOccurrence.
	 */
	public void addOccurrence(EntityOccurrence occurrence) {
		if (occurrence.getEntity().getId() == null) {
			// If we don't throw an exception, Hibernate will...
			throw new IllegalArgumentException("Don't call addOccurrence w/ "
					+ "a detached/transient entity");
		}
		add(occurrence);
	}

	/**
	 * Saves the given frequency.
	 */
	public void addFrequency(EntityDocumentFrequency freq) {
		add(freq);
	}

	/**
	 * Saves the given EntityTuple.
	 */
	public void addTuple(perseus.ie.freq.EntityTuple tup) {
		add(tup);
	}

	private void add(Object obj) {
		HibernateUtil.getSession().save(obj);
		updateCache();
	}

	public void cleanup() {
		HibernateUtil.closeSession();
	}

	/**
	 * Returns all matching EntityOccurrences.
	 *
	 * @param entity the entity to find (may be null)
	 * @param query the query to search in (may be null)
	 */
	public List<EntityOccurrence> getOccurrences(Entity entity,
			perseus.document.Query query) {
		List results = null;
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(EntityOccurrence.class);

		if (entity != null) {
			criteria.createCriteria("entity")
			.add(Restrictions.eq("id", entity.getId()));
		}

		if (query != null) {
			applyQueryRestrictions(criteria, query);
		}
		applyConfigurationRestrictions(criteria);	    
		criteria.addOrder(getSortOrder("location.position"));
		return criteria.list();
	}

	/**
	 * Returns a map containing names and occurrence counts for all entities
	 * within the chunk corresponding to `docQuery`.
	 * 
	 * An example output instance is
	 * <pre>
	 * {"Place" =&gt; [
	 *   {displayName =&gt; "Springfield",
	 *    authorityName =&gt; "tgn,abcdefg",
	 *    sortableString =&gt; "il,springfield",
	 *    count =&gt; 14,
	 *    firstPosition =&gt; 10000,
	 *    entityClass =&gt; "Place"},
	 *
	 *   {displayName =&gt; "Shelbyville",
	 *    ... and so on ...
	 *   }
	 * ]}
	 * </pre>
	 */
	public Map<String,List<Map<String,Object>>> getAllEntityCounts(
			perseus.document.Query docQuery) {

		if (docQuery == null) {
			throw new IllegalArgumentException("Need a valid query!");
		}

		Query query = getSession().createQuery(
				"select new map(et.entity.displayName as displayName, et.entity.authorityName as authorityName, et.entity.sortableString as sortableString, " +
				"et.count as count, et.firstPosition as firstPosition, et.entity.class as entityClass) " +
				"from EntityTuple et where " +
				"et.query.documentID = :documentID " +
				(!docQuery.isJustDocumentID() ?
						"and et.query.query = :subquery " : "group by et.entity "))
						.setParameter("documentID", docQuery.getDocumentID());

		if (!docQuery.isJustDocumentID()) {
			query.setParameter("subquery", docQuery.getQuery());
		}
		query.setCacheable(true);

		return mapFromTuples(query.list());
	}

	public Map<String,List<Map<String,Object>>> getAllEntityCounts(String documentID,
			long startOffset, long endOffset) {

		if (documentID == null) {
			throw new IllegalArgumentException("Need a document ID!");
		}

		Session session = HibernateUtil.getSession();
		Query query = session.createQuery(
				"select new map(eo.entity.displayName as displayName, eo.entity.authorityName as authorityName, eo.entity.sortableString as sortableString, " +
				"count(eo) as count, min(eo.location.position) as firstPosition, " +
				"eo.entity.class as entityClass) " +
				"from EntityOccurrence eo where " +
				"eo.location.query.documentID = :documentID and " +
		"eo.location.position between :start and :end group by eo.entity");

		query.setParameter("documentID", documentID);
		query.setLong("start", startOffset);
		query.setLong("end", endOffset);
		query.setCacheable(true);

		Map<String,List<Map<String,Object>>> output = mapFromTuples(query.list());

		return output;
	}

	private Map<String,List<Map<String,Object>>> mapFromTuples(
			List<Map<String,Object>> results) {

		// *blink*
		Map<String,List<Map<String,Object>>> output =
			new HashMap<String,List<Map<String,Object>>>();

		for (Map<String,Object> tupleMap : results) {
			String className = (String) tupleMap.get("entityClass");

			List<Map<String,Object>> tuples;
			if (!output.containsKey(className)) {
				tuples = new ArrayList<Map<String,Object>>();
				output.put(className, tuples);
			} else {
				tuples = output.get(className);
			}
			tuples.add(tupleMap);
		}

		Comparator<Map> comparator = new EntityCountComparator(sortMethod);
		for (List tuples : output.values()) {
			Collections.sort(tuples, comparator);
			// If we need to sort in descending order, reverse the results we
			// got from our previous comparator.
			if (sortOrder.equals(DESCENDING)) {
				Collections.reverse(tuples);
			}
		}

		return output;
	}

	/**
	 * Returns all EntityTuples containing `entity` and/or in `docQuery`.
	 */
	public List<EntityTuple> getTuples(Entity entity,
			perseus.document.Query docQuery) {

		String sortField = "et.firstPosition";
		if (sortMethod.equals(AUTHORITY_NAME)) {
			sortField = "et.entity.authorityName";
		} else if (sortMethod.equals(SORTABLE_STRING)) {
			sortField = "et.entity.sortableString";
		} else if (sortMethod.equals(DISPLAY_NAME)) {
			sortField = "et.entity.displayName";
		}

		Query query = getSession().createQuery("from EntityTuple et " +
				"left join fetch et.entity as ent where " +
				"ent = :entity and " +
				(docQuery != null ? 
						(docQuery.isJustDocumentID() ?
								"et.chunkRow.documentID = :documentID" :
						"et.chunkRow = :chunk ") :
				"1 = 1") +
				" order by " + sortField + " " + sortOrder)
				.setParameter("entity", entity);

		if (docQuery != null) {
			if (docQuery.isJustDocumentID()) {
				query.setParameter("documentID", docQuery.getDocumentID());
			} else {
				query.setParameter("chunk", HibernateChunkDAO.chunkToRow(
						resolveQuery(docQuery)));		
			}
		}

		applyConfigurationRestrictions(query);

		return query.list();
	}

	/**
	 * Returns all tuples for `entity`.
	 */
	public List<EntityTuple> getTuples(Entity entity) {
		String sortField = DOCUMENT;
		if (sortMethod.equals(COUNT)) {
			sortField = "et.count";
		} else if (sortMethod.equals(FIRST_POSITION)) {
			sortField = "et.firstPosition";
		}
		Query query = getSession().createQuery("from EntityTuple et " +
				"left join fetch et.entity as ent where " +
				"ent = :entity order by " +
				(sortField.equals(DOCUMENT) ?
						("et.query.documentID " + sortOrder +
								", et.firstPosition " + sortOrder) :
									sortField + " " + sortOrder))
									.setParameter("entity", entity);

		applyConfigurationRestrictions(query);

		List<EntityTuple> results = query.list();
		return results;
	}

	/**
	 * Returns all tuples for entities of class `entityClass` in query
	 * `docQuery`.
	 */
	public List<EntityTuple> getTuples(Class entityClass,
			perseus.document.Query docQuery) {

		String sortField = "et.firstPosition";
		if (sortMethod.equals(AUTHORITY_NAME)) {
			sortField = "et.entity.authorityName";
		} else if (sortMethod.equals(SORTABLE_STRING)) {
			sortField = "et.entity.sortableString";
		} else if (sortMethod.equals(DISPLAY_NAME)) {
			sortField = "et.entity.displayName";
		}

		StringBuffer queryBuffer = new StringBuffer();

		queryBuffer.append("from EntityTuple et left join fetch et.entity ");
		if (docQuery != null) {
			if (docQuery.isJustDocumentID()) {
				queryBuffer.append("where et.chunkRow.documentID = :documentID ");
			} else {
				queryBuffer.append("where et.chunkRow = :chunk ");
			}
		}
		if (entityClass != Entity.class) {
			queryBuffer.append("and et.entity.class = :class ");
		}

		queryBuffer.append("order by ").append(sortField).append(" ")
		.append(sortOrder);
		Query query = getSession().createQuery(queryBuffer.toString());

		if (docQuery != null) {
			if (docQuery.isJustDocumentID()) {
				query.setParameter("documentID", docQuery.getDocumentID());
			} else {
				query.setParameter("chunk", HibernateChunkDAO.chunkToRow(
						resolveQuery(docQuery)));
			}
		}

		if (entityClass != Entity.class) {
			query.setParameter("class", getDiscriminatorValue(entityClass));
		}

		applyConfigurationRestrictions(query);

		return query.list();
	}

	/**
	 * Returns all document frequencies for `entity`.
	 */
	public List<EntityDocumentFrequency> getFrequencies(Entity entity) {
		String sortField = "maxFrequency";
		if (sortMethod.equals(DOCUMENT)) {
			sortField = "documentID";
		}
		Query query = getSession().createQuery("from EntityDocumentFrequency where " + 
				"entity = :entity and documentID is not null " +
				"order by " + sortField + " " + sortOrder)
				.setParameter("entity", entity);

		applyConfigurationRestrictions(query);
		query.setCacheable(true);

		return query.list();
	}

	/**
	 * Returns the document frequency for `entity` in `documentID`, if one
	 * exists.
	 */
	public EntityDocumentFrequency getFrequency(Entity entity, String documentID) {
		Query query = getSession().createQuery("from EntityDocumentFrequency " +
		"where entity = :entity and documentID = :docID")
		.setParameter("entity", entity)
		.setParameter("docID", documentID);

		query.setCacheable(true);
		List results = query.list();
		if (results.size() == 0) {
			return null;
		}
		else {
			return (EntityDocumentFrequency) results.get(0);
		}
	}

	/**
	 * Returns all frequencies for entities of type `entityClass` in
	 * `documentID`.
	 */
	public List<EntityDocumentFrequency> getFrequencies(String documentID,
			Class entityClass) {
		String sortField = "ef.maxFrequency";
		if (sortMethod.equals(AUTHORITY_NAME)) {
			sortField = "ent.authorityName";
		} else if (sortMethod.equals(SORTABLE_STRING)) {
			sortField = "ent.sortableString";
		} else if (sortMethod.equals(DISPLAY_NAME)) {
			sortField = "ent.displayName";
		}

		StringBuffer queryBuffer = new StringBuffer()
		.append("from EntityDocumentFrequency ef ")
		.append("left join fetch ef.entity as ent ")
		.append("where ef.documentID = :documentID ");
		if (entityClass != Entity.class) {
			queryBuffer.append("and ent.class = :class ");
		}
		queryBuffer.append("order by ").append(sortField)
		.append(" ").append(sortOrder);

		Query query = getSession().createQuery(queryBuffer.toString())
		.setParameter("documentID", documentID);
		if (entityClass != Entity.class) {
			query.setParameter("class", getDiscriminatorValue(entityClass));
		}

		applyConfigurationRestrictions(query);
		query.setCacheable(true);

		return query.list();
	}

	/**
	 * Returns the number of tuples for entities of type `entityClass`
	 * in `docQuery`.
	 */
	public int getTupleCount(Class entityClass,
			perseus.document.Query docQuery) {

		StringBuffer queryBuffer = new StringBuffer();
		List<String> conditions = new ArrayList<String>();
		queryBuffer.append("select count(et) from EntityTuple et where ");

		if (entityClass != Entity.class) {
			conditions.add("et.entity.class = :entityClass");
		}

		if (docQuery != null) {
			if (docQuery.isJustDocumentID()) {
				conditions.add("et.chunkRow.documentID = :documentID");
			} else {
				conditions.add("et.chunkRow = :chunk");
			}
		}
		queryBuffer.append(StringUtil.join(conditions, " and "));

		Query query = getSession().createQuery(queryBuffer.toString());
		if (entityClass != Entity.class) {
			query.setParameter("entityClass",
					getDiscriminatorValue(entityClass));
		}
		if (docQuery != null) {
			if (docQuery.isJustDocumentID()) {
				query.setParameter("documentID", docQuery.getDocumentID());
			} else {
				query.setParameter("chunk",
						HibernateChunkDAO.chunkToRow(resolveQuery(docQuery)));
			}
		}

		query.setCacheable(true);

		return ((Number) query.uniqueResult()).intValue();
	}

	private Chunk resolveQuery(perseus.document.Query docQuery) {
		if (docQuery == null) return null;
		try {
			return docQuery.getChunk();
		} catch (InvalidQueryException iqe) {
			throw new IllegalArgumentException(iqe);
		}
	}

	/**
	 * Returns the number of tuples for `entity` in `docQuery`.
	 */
	public int getTupleCount(Entity entity, perseus.document.Query docQuery) {
		StringBuffer queryBuffer = new StringBuffer();
		List<String> conditions = new ArrayList<String>();
		queryBuffer.append("select count(et) from EntityTuple et where ");

		if (entity != null) conditions.add("et.entity = :entity");
		if (docQuery != null) {
			if (docQuery.isJustDocumentID()) {
				conditions.add("et.chunkRow.documentID = :documentID");
			} else {
				conditions.add("et.chunkRow = :chunk");
			}
		}
		queryBuffer.append(StringUtil.join(conditions, " and "));

		Query query = getSession().createQuery(queryBuffer.toString());
		if (entity != null) query.setParameter("entity", entity);
		if (docQuery != null) {
			if (docQuery.isJustDocumentID()) {
				query.setParameter("documentID", docQuery.getDocumentID());
			} else {
				query.setParameter("chunk", HibernateChunkDAO.chunkToRow(
						resolveQuery(docQuery)));
			}
		}

		query.setCacheable(true);

		return ((Number) query.uniqueResult()).intValue();
	}

	/**
	 * Returns the number of unique entities in `documentID`.
	 */
	public int getEntityCount(String documentID) {
		StringBuffer queryBuffer = new StringBuffer();

		queryBuffer.append("select count(ef) ")
		.append("from EntityDocumentFrequency ef where ef.documentID = :docID");

		Query query = getSession().createQuery(queryBuffer.toString())
		.setParameter("docID", documentID);
		return ((Number) query.uniqueResult()).intValue();
	}

	public boolean docHasPlaces(String documentID) {
		String query = "select f.entity from EntityDocumentFrequency f where f.documentID = :docID and f.entity.class='Place' " +
		"group by f.entity order by sum(f.maxFrequency) desc";
		List<Place> places = getSession().createQuery(query).setParameter("docID", documentID).setMaxResults(1).list();
		if (places.size() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the number of occurrences of `entity` in `docQuery`.
	 */
	public int getOccurrenceCount(Entity entity,
			perseus.document.Query docQuery,
			boolean unique) {
		StringBuffer queryBuffer = new StringBuffer();

		List<String> groupBy = new ArrayList<String>();

		queryBuffer.append("select count(eo) ");
		queryBuffer.append("from EntityOccurrence eo where ");
		if (entity != null) {
			queryBuffer.append("eo.entity = :entity ");
			groupBy.add("eo.entity");
			if (docQuery != null) queryBuffer.append("and ");
		}
		if (docQuery != null) {
			queryBuffer.append("eo.location.query.documentID = :documentID ");
			groupBy.add("eo.location.query.documentID");
			if (!docQuery.isJustDocumentID()) {
				queryBuffer.append("and eo.location.query.query = :subquery ");
				groupBy.add("eo.location.query.query");
			}
		}

		if (unique && !groupBy.isEmpty()) {
			queryBuffer.append("group by ")
			.append(StringUtil.join(groupBy, ", "));
		}

		Query query = getSession().createQuery(queryBuffer.toString());
		if (entity != null) query.setParameter("entity", entity);
		if (docQuery != null) {
			query.setParameter("documentID", docQuery.getDocumentID());
			if (!docQuery.isJustDocumentID()) {
				query.setParameter("subquery", docQuery.getQuery());
			}
		}
		query.setCacheable(true);

		return ((Number) query.uniqueResult()).intValue();
	}

	/**
	 * Calulates entity frequencies for `corpus`, assuming document frequencies
	 * have already been loaded.
	 */
	public void aggregateCorpusFrequencies(Corpus corpus) {
		HibernateUtil.getSession().createQuery(
				"insert into EntityDocumentFrequency " +
				"(entity, documentID, maxFrequency, minFrequency, weightedFrequency, keywordScore) " +
				"select ef.entity, '" + corpus + "', " +
				"sum(maxFrequency), sum(minFrequency), sum(weightedFrequency), 0.0 " +
				"from EntityDocumentFrequency ef where ef.documentID in ('" +
				StringUtil.join(corpus.getDocumentIDs(), "','") + "') group by ef.entity")
				.executeUpdate();
	}    

	public void clearReferences(String documentID, Class cla) {
		clearOccurrences(documentID, cla);
		clearFrequencies(documentID, cla);
		clearTuples(documentID, cla);
	}

	public int getMaxResults() {
		return maxResults;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public String getSortMethod() {
		return sortMethod;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setMaxResults(int max) {
		maxResults = max;
	}

	public void setFirstResult(int first) {
		firstResult = first;
	}

	public void setSortMethod(String method) {
		sortMethod = method;
	}

	public void setSortOrder(String order) {
		if (order.equals(ASCENDING) || order.equals(DESCENDING)) {
			sortOrder = order;
		}
	}

	public void clearOccurrences(String documentID, Class cla) {
		doClear(EntityOccurrence.class, documentID, cla);
	}

	public void clearFrequencies(String documentID, Class cla) {
		doClear(EntityDocumentFrequency.class, documentID, cla);
	}

	public void clearTuples(String documentID, Class cla) {
		doClear(EntityTuple.class, documentID, cla);
	}

	private void doClear(Class tableClass, String docID, Class entityClass) {
		String tableName = tableClass.getName();

		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("delete ").append(tableName)
		.append(" ")
		.append("where entity_id in (select id from ")
		.append(entityClass.getName())
		.append(") ");
		if (docID != null) {
			// This is very ugly--maybe these classes should all just use
			// Locations to represent documents/queries?
			String documentIDField = "document_id";

			queryBuffer.append(" and ").append(documentIDField)
			.append(" = :documentID");
		}

		Query query = getSession().createQuery(queryBuffer.toString())
		.setString("documentID", docID);

		query.executeUpdate();
	}

	public void deleteAllPlaces() {
		HibernateUtil.getSession().createSQLQuery("delete from hib_places").executeUpdate();
		HibernateUtil.getSession().createSQLQuery("delete from hib_entities where entity_type='Place'").executeUpdate();
	}

	private void applyQueryRestrictions(Criteria criteria,
			perseus.document.Query query) {
		if (query != null) {
			criteria.add(
					Restrictions.eq("documentID", query.getDocumentID()));
			if (!query.isJustDocumentID()) {
				criteria.add(
						Restrictions.eq("subquery", query.getQuery()));
			}
		}
	}

	private void applyConfigurationRestrictions(Query query) {
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}

		if (firstResult > 0) {
			query.setFirstResult(firstResult);
		}
	}

	private void applyConfigurationRestrictions(Criteria criteria) {
		if (maxResults != -1) {
			criteria.setMaxResults(maxResults);
		}

		if (firstResult != -1) {
			criteria.setFirstResult(firstResult);
		}
	}

	public static String getOppositeSortOrder(String order) {
		if (order.equals(ASCENDING)) return DESCENDING;
		if (order.equals(DESCENDING)) return ASCENDING;

		return null;
	}

	// ...Hibernate, what the HELL? The "class" field is supposed to be smart
	// and resolve the class name to the discriminator value automatically.
	// Instead, it's exceedingly dumb.
	private String getDiscriminatorValue(Class entityClass) {
		return entityClass.getSimpleName();
	}

	private Order getSortOrder(String field) {
		return (sortOrder.equals(ASCENDING) ?
				Order.asc(field) : Order.desc(field));
	}

	public EntityOccurrence getOccurrenceByEntity(
			Chunk chunk, Entity entity, int which) {
		if (which < 0) which = 1;

		Query query = getSession().createQuery(
				"from EntityOccurrence eo where " +
				"eo.location.query.documentID = :docID "  +
				"and eo.location.position between :start and :end " +
		"and eo.entity = :entity order by eo.location.position asc")
		.setParameter("docID", chunk.getDocumentID())
		.setParameter("start", chunk.getStartOffset())
		.setParameter("end", chunk.getEndOffset())
		.setParameter("entity", entity)
		.setMaxResults(1)
		.setFirstResult(which-1);

		List<EntityOccurrence> matches = query.list();

		return (matches.isEmpty() ? null : matches.get(0));
	}
}
