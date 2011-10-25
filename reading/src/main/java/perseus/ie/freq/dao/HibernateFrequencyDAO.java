package perseus.ie.freq.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Restrictions;

import perseus.document.Chunk;
import perseus.document.dao.HibernateChunkDAO;
import perseus.ie.entity.AbstractEntity;
import perseus.ie.entity.Entity;
import perseus.ie.freq.ChunkFrequency;
import perseus.ie.freq.DocumentFrequency;
import perseus.ie.freq.EntityBased;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.EntityTuple;
import perseus.ie.freq.FormBased;
import perseus.ie.freq.Frequency;
import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.morph.dao.LemmaDAO;
import perseus.util.HibernateDAO;
import perseus.util.StringUtil;

/**
 * Hibernate-based implementation of FrequencyDAO. This is intended to be
 * instantiated with, as a parameter, a class that specifies what kind of
 * Frequencies this DAO instance will deal with.
 */
public class HibernateFrequencyDAO extends HibernateDAO<Frequency> implements FrequencyDAO {
	private static final Logger logger = Logger.getLogger(HibernateFrequencyDAO.class);

	private Class<? extends Frequency> targetClass;

	public HibernateFrequencyDAO(Class<? extends Frequency> target) {
		targetClass = target;
	}

	public void deleteByChunk(
			Chunk chunk, List<Class<? extends Entity>> entityClasses) {
		getSession().createQuery(
				String.format("delete from %s where chunkRow.id = :chunkID " +
						"and class in %s",
						className(), getClassNames(entityClasses)))
						.setParameter("chunkID", chunk.getId()).executeUpdate();	
	}

	public void deleteByDocument(String documentID,
			List<Class<? extends Entity>> entityClasses) {

		Query query = getSession().createQuery(
				String.format("delete from %s z where z.documentID = :docID" +
						" and z.entity.id in (select id from Entity e where e.class in %s)",
						className(), getClassNames(entityClasses)))
						.setParameter("docID", documentID);

		query.executeUpdate();
	}
	
	/*public void deleteByDocument(String documentID) {
		Query query = getSession().createQuery(
				String.format("delete from %s where documentID = :docID", className()))
						.setParameter("docID", documentID);

		query.executeUpdate();
	}*/
	
	public void deleteByChunkIDs(List<Integer> chunkIDs) {
		getSession().createQuery(
				String.format("delete from %s where chunkRow.id in (:chunkIDs)", className()))
						.setParameterList("chunkIDs", chunkIDs).executeUpdate();	
	}

	private String getClassNames(List<Class<? extends Entity>> classes) {
		StringBuilder builder = new StringBuilder();

		builder.append("(");
		for (Class<? extends Entity> cl : classes) {
			builder.append("'").append(cl.getSimpleName()).append("',");
		}
		builder.deleteCharAt(builder.length()-1);
		builder.append(")");

		return builder.toString();
	}

	private List<String> namesAsList(List<Class<? extends Entity>> classes) {
		List<String> names = new ArrayList<String>();
		for (Class<? extends Entity> cl : classes) {
			names.add(cl.getSimpleName());
		}
		return names;
	}

	private String className() {
		return targetClass.getSimpleName();
	}

	private String getIDField() {
		return isChunkBased() ? "chunkRow.documentID" : "documentID";
	}

	/**
	 * Aggregates frequencies for all records of the target class, regardless
	 * of entity type.
	 */
	public void aggregateAllDocuments(boolean deleteExisting) {
		if (FormBased.class.isAssignableFrom(targetClass)) {
			throw new IllegalArgumentException(
					"aggregateAllDocuments may only be called with " +
			"targetClass set to EntityDocumentFrequency or EntityTuple");
		}

		Class classToUse = targetClass;
		// 2011-05-18 BALMAS -- THIS IS BROKEN, DocumentID is not set for EntityTuple
		// and it's not really clear to me what this was intended to do
		if (targetClass == EntityDocumentFrequency.class) {
			classToUse = EntityTuple.class;
		}

		// First, delete any existing aggregated rows
		if (deleteExisting) {
			int rowsDeleted = getSession().createQuery(String.format(
					"delete from %s z where z.documentID is null",
					classToUse.getSimpleName()))
					.executeUpdate();
		}

		Query query = getSession().createQuery(String.format(
				"select z.entity, sum(z.maxFrequency), sum(z.minFrequency), " +
				"sum(z.weightedFrequency) from %s z " +
				"where z.documentID is not null",
				classToUse.getSimpleName()));

		int entityCount = 0;
		ScrollableResults results = query.scroll();
		while (results.next()) {
			Entity entity = (Entity) results.get(0);
			long totalMax = (Long) results.get(1);
			long totalMin = (Long) results.get(2);
			double totalWeighted = (Double) results.get(3);

			EntityDocumentFrequency docFreq = new EntityDocumentFrequency();
			docFreq.setEntity(entity);
			docFreq.setMaxFrequency((int) totalMax);
			docFreq.setMinFrequency((int) totalMin);
			docFreq.setWeightedFrequency(totalWeighted);
			entityCount++;
			if (entityCount % 500 == 0) {
				logger.info(entityCount);
			}
			save(docFreq);	    
		}
	}

	public void aggregateDocument(
			String documentID,
			List<Class<? extends Entity>> classes) {
		Query query = getSession().createQuery(String.format(
				"select z.entity, sum(z.maxFrequency), " +
				"sum(z.minFrequency), sum(z.weightedFrequency)" +
				"from %s z where z.chunkRow.documentID = :docID " +
				"and z.entity.class in %s group by z.entity",
				className(), getClassNames(classes)))
				.setParameter("docID", documentID);

		ScrollableResults results = query.scroll();
		while (results.next()) {
			Entity entity = (Entity) results.get(0);
			long totalMax = (Long) results.get(1);
			long totalMin = (Long) results.get(2);
			double totalWeighted = (Double) results.get(3);

			EntityDocumentFrequency docFreq = new EntityDocumentFrequency();
			docFreq.setEntity(entity);
			docFreq.setDocumentID(documentID);
			docFreq.setMaxFrequency((int) totalMax);
			docFreq.setMinFrequency((int) totalMin);
			docFreq.setWeightedFrequency(totalWeighted);
			save(docFreq);
		}
		results.close();
	}

	public List<? extends Frequency> getByDocumentID(
			Entity entity,
			String documentID) {

		Criteria crit = criteria();
		addEntity(crit, entity);
		addDocumentID(crit, documentID);

		return crit.list();
	}

	private void addEntity(Criteria crit, Entity entity) {
		if (entity != null) {
			crit.add(Restrictions.eq("entity", entity));
		}
	}

	public List<? extends Frequency> getByDocumentID(
			List<Class<? extends Entity>> entityClasses,
			String documentID) {
		Criteria crit = criteria();
		addClasses(crit, entityClasses);
		addDocumentID(crit, documentID);

		return crit.list();
	}

	public List<? extends Frequency> getByChunk(
			Entity entity,
			Chunk chunk) {
		Criteria crit = criteria();
		addEntity(crit, entity);
		addChunk(crit, chunk);

		return crit.list();
	}

	public List<? extends Frequency> getByChunk(Chunk chunk,
			List<Class<? extends Entity>> entityClasses) {
		Criteria crit = criteria();
		addClasses(crit, entityClasses);
		addChunk(crit, chunk);

		return crit.list();
	}

	private void addChunk(Criteria crit, Chunk chunk) {
		if (chunk != null) {
			crit.add(Restrictions.eq("chunkRow",
					HibernateChunkDAO.chunkToRow(chunk)));
		}
	}

	private void addClasses(
			Criteria crit,
			List<Class<? extends Entity>> classes) {

		if (classes != null && !classes.isEmpty()) {
			crit.createCriteria("entity")
			.add(Restrictions.in("class", namesAsList(classes)));
		}
	}

	private void addDocumentID(Criteria crit, String documentID) {
		if (documentID != null) {
			if (isChunkBased()) {
				crit.createCriteria("chunk")
				.add(Restrictions.eq("documentID", documentID));
			} else {
				crit.add(Restrictions.eq("documentID", documentID));
			}
		}
	}

	private String resolve(Class clazz) {
		return clazz.getSimpleName();
	}

	private boolean isChunkBased() {
		return ChunkFrequency.class.isAssignableFrom(targetClass);
	}

	private Criteria criteria() {
		return getSession().createCriteria(targetClass);
	}

	public void updateChunkFrequencies(
			List<ChunkFrequency> frequencies,
			Chunk chunk,
			List<Class<? extends Entity>> entityClasses) {

		List<? extends Frequency> savedFrequencies =
			getByChunk(chunk, entityClasses);

		mergeFrequencyLists(frequencies, savedFrequencies);
	}

	private void mergeFrequencyLists(List<? extends Frequency> frequencies, List<? extends Frequency> savedFrequencies) {
		for (Frequency newFrequency : frequencies) {
			Frequency matchingFrequency = null;
			for (Frequency oldFrequency : savedFrequencies) {
				if (newFrequency.getForm().equals(oldFrequency.getForm())) {
					matchingFrequency = oldFrequency;
					break;
				}
			}

			if (matchingFrequency != null) {
				newFrequency.setId(matchingFrequency.getId());
				getSession().merge(newFrequency);
				savedFrequencies.remove(matchingFrequency);
			} else {
				getSession().save(newFrequency);
			}
		}

		for (Frequency remainingFrequency : savedFrequencies) {
			getSession().delete(remainingFrequency);
		}

		getSession().flush();
		getSession().clear();
	}

	public void updateDocumentFrequencies(
			List<DocumentFrequency> frequencies,
			String documentID,
			List<Class<? extends Entity>> entityClasses) {

		List<? extends Frequency> savedFrequencies =
			getByDocumentID(entityClasses, documentID);

		mergeFrequencyLists(frequencies, savedFrequencies);
	}

	/**
	 * Returns a list of which each entry consists of two elements: an entity,
	 * and the number of distinct documents in which that entity appears.
	 * We use this to calculate inverse document frequencies for our entities.
	 * (Note that, unlike most methods in this class, this one ignores the
	 * targetClass field; it fetches EntityTuples regardless.)
	 * 
	 * @param documentIDs the documents to look for (used to restrict the search to, e.g., a particular language)
	 * @return a list of the form (entity, documentCount)
	 */
	public ScrollableResults getLemmaCounts(Set<String> documentIDs) {
		return getSession().createQuery(
				"select et.entity, count(distinct chunk.documentID) " +
				"from EntityTuple et inner join et.chunkRow chunk where chunk.documentID in ('" +
				StringUtil.join(documentIDs, "','") + "') group by et.entity ")
				.scroll();
	}

	/**
	 * Returns a list of which each entry consists of two elements: a form
	 * (as a string) and the number of distinct documents in which that form
	 * appears. We use this to calculate inverse document frequencies for our
	 * form. (Note that, unlike most methods in this class, this one ignores
	 * the targetClass field; it fetches EntityTuples regardless.)
	 * 
	 * @param language the language the forms should be in
	 * @param documentIDs the documents to look for (used to restrict the search to, e.g., a particular language)
	 * @return a list of the form (form, documentCount)
	 */
	public ScrollableResults getFormCounts(
			Language language, Set<String> documentIDs) {

		return getSession().createQuery(
				"select ff.form, count(distinct chunk.documentID) " +
				"from FormFrequency ff inner join ff.chunkRow chunk where chunk.documentID in " +
				StringUtil.join(documentIDs, "','") +
		"') and ff.language = :language group by ff.form")
		.setParameter("language", language).scroll();
	}

	public long getCount(
			String documentID,
			List<Class<? extends Entity>> classes) {
		return (Long) getSession().createQuery(String.format(
				"select count(*) from %s where %s = :docID and entity.class in %s",
				className(), getIDField(), getClassNames(classes)))
				.setParameter("docID", documentID)
				.uniqueResult();
	}

	public ScrollableResults getScrollableLemmasWithCounts(Language language) {
		return getSession().createQuery(
				"select f.entity, count(distinct f.documentID), " +
				"sum(f.maxFrequency), sum(f.minFrequency) " +
				"from EntityDocumentFrequency f where " +
				"f.documentID is not null and " +
				"f.entity.class = 'Lemma' and f.entity.language = :lang " +
		"group by f.entity")
		.setParameter("lang", language)
		.scroll(ScrollMode.SCROLL_SENSITIVE);
	}

	/**
	 * Returns the total number of documents indexed. Useful for finding
	 * TF/IDF.
	 */
	public long getTotalDocumentCount() {
		return (Long) getSession().createQuery(
				"select count(distinct f.documentID) from EntityDocumentFrequency f " +
		"where f.documentID is not null").uniqueResult();
	}

	/**
	 * Returns a list of tuples of the form (Entity, long, long, long),
	 * an entity, the number of documents it appears in, its max frequency
	 * and its min frequency.
	 */
	public ScrollableResults getScrollableEntitiesWithCounts(Class<AbstractEntity> name) {
		return getSession().createQuery(String.format(
				"select f.entity, count(distinct f.documentID), " +
				"sum(f.maxFrequency), sum(f.minFrequency) " +
				"from EntityDocumentFrequency f where " +
				"f.documentID is not null and " +
				"f.entity.class in ('Person','Place','Date','DateRange') group by f.entity", name.getName()))
				.scroll(ScrollMode.SCROLL_SENSITIVE);
	}

	public int getTotalEntitiesForChunk(Chunk chunk) {
		long count = (Long) getSession().createQuery("select count(*) from EntityTuple e " +
		"where e.chunkRow = :chunk")
		.setParameter("chunk", HibernateChunkDAO.chunkToRow(chunk))
		.uniqueResult();

		return (int) count;
	}

	/**
	 * Returns all frequencies, of the class specified by
	 * <code>targetClass</code>, for the given entity.
	 *
	 * @param entity the entity to search for
	 * @return all matching frequencies for this DAO's class
	 */
	public List<? extends EntityBased> getByEntity(Entity entity) {
		return getSession().createQuery(String.format(
				"from %s where entity = :entity",
				className()))
				.setParameter("entity", entity)
				.list();
	}

	/**
	 * Returns the number of results--either chunks or documents, depending
	 * on the value of <code>targetClass</code>--in which the given entity
	 * appears.
	 *
	 * @param entity the entity to search for
	 * @return the number of chunks/documents in which this entity appears
	 */
	public long getResultCount(Entity entity) {
		return (Long) getSession().createQuery(String.format(
				"select count(*) from %s where entity = :entity and " +
				(isChunkBased() ?
						"chunk is not null" :
				"documentID is not null"), className())).uniqueResult();
	}
	
	/**
	 * Find the total weighted frequency between all documents requested for Vocab List
	 * @param documentIDs doc IDs to restrict search
	 * @param usingChunks so we know if we're searching chunks or documents
	 * @return total weighted frequency for all lemmas
	 */
	public double getTotalWeightedFrequency(List<String> IDs, boolean usingChunks) {
		String query = "select sum(weighted_freq) from hib_frequencies where ";
		
		if (usingChunks) {
			query += "chunk_id";
		} else {
			query += "document_id";
		}
		query += " in (:IDs)";
		
		return (Double) getSession().createSQLQuery(query).setParameterList("IDs", IDs).uniqueResult();
	}

	public List<Frequency> getVocabularyList(List<String> IDs, double cutoffFrequency, boolean usingChunks) {  
		/* Fetch results, starting with the entities that appear most
		 * often among all the documents. Stop fetching results when
		 * the total weighted frequency we've seen so far is as high as we need.
		 * 
		 * Using Hibernate's createQuery() was too slow because of all the joins on the different
		 * entity tables, so we use createSQLQuery() instead and get the Lemma object from the entity_id
		 * when we're processing the results.  It's much, much faster this way.
		 * 
		 */
		String query = "select entity_id, sum(max_freq) as max_sum, sum(min_freq) as min_sum, " +
		"sum(weighted_freq) as weighted_sum, sum(tf_idf) as tfidf_sum " +
		"from hib_frequencies where ";
		
		if (usingChunks) {
			query += "chunk_id";
		} else {
			query += "document_id";
		}
		
		query += " in (:IDs) group by entity_id order by weighted_sum desc";
		
		ScrollableResults results = getSession().createSQLQuery(query)
		.addScalar("entity_id", Hibernate.INTEGER)
		.addScalar("max_sum", Hibernate.LONG)
		.addScalar("min_sum", Hibernate.LONG)
		.addScalar("weighted_sum", Hibernate.DOUBLE)
		.addScalar("tfidf_sum", Hibernate.DOUBLE)
		.setParameterList("IDs", IDs).scroll();
		
		List<Frequency> frequencies = new ArrayList<Frequency>();

		double runningFrequency = 0.0;
		int count = 0;
		LemmaDAO lDAO = new HibernateLemmaDAO();
		lDAO.beginTransaction();
		while (results.next() && runningFrequency < cutoffFrequency) {
			int entityID = (Integer) results.get(0);
			Lemma lemma = lDAO.getLemmaById(entityID);
			long maxFrequency = (Long) results.get(1);
			long minFrequency = (Long) results.get(2);
			double weightedFrequency = (Double) results.get(3);
			double tfidf = (Double) results.get(4);

			Frequency frequency = new EntityDocumentFrequency(lemma, null);
			frequency.setMaxFrequency((int) maxFrequency);
			frequency.setMinFrequency((int) minFrequency);
			frequency.setWeightedFrequency(weightedFrequency);
			frequency.setTfidf(tfidf);
			frequencies.add(frequency);
			
			count++;
			if (count % getBatchSize() == 0) {
				getSession().flush();
				getSession().clear();
			}

			runningFrequency += weightedFrequency;
		}
		lDAO.endTransaction();
		results.close();
		return frequencies;
	}

	/**
	 * Returns the number of words that occur only once in the given documents
	 * combined. Note that occurrence here is measured using maximum frequency;
	 * as such, this may not reflect the *actual* number of words that occur.
	 * 
	 * @param documentIDs the document IDs to restrict this search to
	 * @return the number of words that only occur once in total over these documents
	 */
	public long getSingleOccurrencesCount(List<String> documentIDs) {
		getSession().flush();
		getSession().clear();
		ScrollableResults results = getSession().createQuery(String.format(
				"select count(*) from %s where " +
				"documentID in (:docIDs) group by entity " +
				"having sum(maxFrequency) = 1",
				className()))
				.setParameterList("docIDs", documentIDs)
				.scroll();

		// This is decidedly hackish. We'd like to do a sum(*) instead of a
		// count(*) above, but MySQL doesn't like it. Instead of returning a
		// the total count, we return a bunch of rows of "1"s and find out
		// how many rows we returned.
		results.last();
		long rowNumber = results.getRowNumber();
		results.close();
		return rowNumber;
	}

	/**
	 * getUniqueEntityCount
	 *
	 * @param documentIDs the document IDs to restrict this search to
	 * @return the number of entities that appear at some point in these documents
	 */
	public long getUniqueWordCount(List<String> documentIDs) {
		return (Long) getSession().createQuery(String.format(
				"select count(distinct entity) from %s " +
				"where documentID in (:docIDs)",
				className()))
				.setParameterList("docIDs", documentIDs)
				.uniqueResult();
	}

	public void updateFrequencyWithTfIdf() {
		getSession().createSQLQuery("UPDATE hib_frequencies f, hib_entities e SET f.tf_idf=(e.idf*f.tf) WHERE " +
		"e.id=f.entity_id").executeUpdate();

	}

	/**
	 * Returns all <code>EntityDocumentFrequency</code> objects for the given
	 * entity, along with the corresponding WordCount object for the entity's
	 * document and language.
	 *
	 * @param entity the entity to search for
	 * @return a list of arrays of the form [EntityDocumentFrequency, WordCount]
	 */
	public List<Object[]> getDocumentFrequenciesWithCounts(Entity entity) {
		return getSession().createQuery(
				"from EntityDocumentFrequency f, WordCount w where " +
				"f.documentID = w.documentID and " +
		"f.entity = :entity and w.lang = :language")
		.setParameter("entity", entity)
		.setParameter("language", (entity instanceof Lemma) ?
				((Lemma) entity).getLanguage() :
					Language.ENGLISH)
					.list();
	}

	/**
	 * Returns all <code>EntityDocumentFrequency</code> objects for the given
	 * entity and document id, along with the corresponding WordCount object for the 
	 * document id and entity's language.
	 * 
	 * @param entity  the entity to search for
	 * @param documentID the document id for the text we are searching for
	 * @return a list of arrays of the form [EntityDocumentFrequency, WordCount]
	 */
	public List<Object[]> getDocumentFrequenciesWithCounts(Entity entity, String documentID) {
		return getSession().createQuery(
				"from EntityDocumentFrequency f, WordCount w where " +
				"f.documentID = :documentID and w.documentID = :documentID and " +
		"f.entity = :entity and w.lang = :language")
		.setParameter("entity", entity)
		.setParameter("language", (entity instanceof Lemma) ?
				((Lemma) entity).getLanguage() :
					Language.ENGLISH)
					.setParameter("documentID", documentID)
					.list();
	}
}
