package perseus.ie.freq.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.ScrollableResults;

import perseus.document.Chunk;
import perseus.ie.entity.Entity;
import perseus.ie.freq.EntityBased;
import perseus.ie.freq.Frequency;
import perseus.language.Language;
import perseus.util.DAO;

/**
 * DAO class with methods for retrieving frequencies.
 */

public interface FrequencyDAO extends DAO<Frequency> {
    /**
     * Deletes all the frequencies within the given document.
     *
     * @param documentID the target document
     * @param entityClasses delete entities of any of these classes
     */
    public void deleteByDocument(
	    String documentID, List<Class<? extends Entity>> entityClasses);

    /**
     * Deletes all the frequencies within the given chunk.
     *
     * @param chunk the target chunk
     * @param entityClasses delete entities of any of these classes
     */
    public void deleteByChunk(
	    Chunk chunk, List<Class<? extends Entity>> entityClasses);
    
    /**
     * Deletes all the frequencies within the given chunk.
     *
     * @param chunk the target chunk
     * @param entityClasses delete entities of any of these classes
     */
    public List getByChunk(Chunk chunk, List<Class<? extends Entity>> classes);

    /**
     * Aggregates frequencies in all documents.
     */
    public void aggregateAllDocuments(boolean deleteExisting);

    /**
     * Returns a ScrollableResults object representing the lemmas and their
     * counts for all documents in `documentIDs`.
     */
    public ScrollableResults getLemmaCounts(Set<String> documentIDs);

    /**
     * Returns a ScrollableResults object representing the forms of language
     * `language` and their counts for all documents in `documentIDs`.
     */
    public ScrollableResults getFormCounts(Language language, Set<String> documentIDs);

    /**
     * Returns the number of frequencies in `documentID` with entities
     * whose class is contained in `entityClasses`.
     */
    public long getCount(String documentID, List<Class<? extends Entity>> entityClasses);
    
    /**
     * Returns all entity-based frequencies for `entity`.
     */
    public List<? extends EntityBased> getByEntity(Entity entity);

    /**
     * Returns the number of frequencies for `entity`.
     */
    public long getResultCount(Entity entity);

	/**
	 * getVocabularyList
	 * 
	 * @param IDs chunkIDs or documentIDs to restrict the search to
	 * @param cutoffFrequency return only as many results as needed to push the summed weighted
	 *        frequency past this percent of the document's total weighted frequency
	 * @param usingChunks so we know if we're searching chunks or documents
	 * @return a list of Frequency objects for the given documents or chunks
	 */
    public List<Frequency> getVocabularyList(List<String> IDs, double cutoffFrequency, boolean usingChunks);

    /**
     * Returns the number of words that only occur once within the given
     * documents.
     */
    public long getSingleOccurrencesCount(List<String> documentIDs);

    public long getUniqueWordCount(List<String> documentIDs);
        
    public int getTotalEntitiesForChunk(Chunk chunk);
    
    public void updateFrequencyWithTfIdf();
}
