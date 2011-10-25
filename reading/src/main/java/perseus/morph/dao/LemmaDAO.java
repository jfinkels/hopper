/**
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package perseus.morph.dao;

import java.util.List;

import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.util.DAO;

/**
 * The LemmaDAO retrieves information about a Lemma from a data source.  Each data source that
 * provides Lemmas must implement LemmaDAO
 * <p>
 * This DAO is based upon the sample code found in Sun's article on the DataAccessObject pattern.  
 *   http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html
 * References to patterns are from "Patterns of Enterprise Appliation Architecture", Martin Fowler.
 */
public interface LemmaDAO extends DAO<Lemma> {

    /**
     * Indicate the start of a unit of work(184)
     */
    public void beginTransaction();

    /**
     * Indicate the end of a unit of work(184)
     */
    public void endTransaction();

    /**
     * Save a Lemma to the underlying data source.
     * @param lemma a Lemma with the desired changes
     * @return true if the update is successful, false otherwise
     */
    public int insertLemma(Lemma lemma);

    /**
     * Retrieve persisted Lemmas using Lemma as a Query Object(316)
     * @param lemma the Query Object Lemma
     * @return a List of all Lemmas similar to lemma
     */
    public List findLemma(Lemma lemma);

    /**
     * Retrieve persisted Lemmas using Lemma as a Query Object(316)
     * @param lemma the Query Object Lemma
     * @param offset where to start grabbing Lemmas from the list of matches
     * @param maxResults how many Lemmas to grab
     * @return a List of all Lemmas similar to lemma
     */
    public List findLemma(Lemma lemma, int offset, int maxResults);

    /**
     * Retrieve persisted Lemmas using Lemma as a Query Object(316)
     * @param lemma the Query Object Lemma
     * @param offset where to start grabbing Lemmas from the list of matches
     * @param maxResults how many Lemmas to grab
     * @param sortKey the fieldName by which to sort the list of matches
     * @param ascDesc sort the list ascending ('asc') or descending ('desc')
     * @return a List of all Lemmas similar to lemma
     */
    public List findLemma(Lemma lemma, int offset, int maxResults, String sortKey, String ascDesc);

    /**
     * Save a persisted object to the underlying data source
     * @param lemma a persisted Lemma that has 0 or more modifications to be saved
     * @return true if the update is successful, false otherwise
     */
    public boolean updateLemma(Lemma lemma);

    /**
     * Remove a Lemma from the underlying data source.
     * @param lemma a persisted Lemma
     * @return true if the delete was a success, false otherwise
     */
    public boolean deleteLemma(Lemma lemma);

    /**
     * Get distinct values for the fieldName of a Lemma from the underlying data source
     * @param fieldName a valid fieldName for any class with the Lemma interface
     * @return a list of Strings that are values of the fieldName
     
    public List findDistinctFieldValues(String fieldName);
    */
    /**
     * Get the number of distinct values for the fieldName of a Lemma from the underlying data source
     * @param fieldName A valid fieldName for the lemma
     * @return the number of distint values for the given fieldName

    public int getDistinctFieldValuesHits(String fieldName);
     */
    /**
     * Get the number of Lemmas in the underlying data source similar to lemma
     * @param lemma the underlying Query Object(316)
     * @return the number of Lemmas similar to lemma
     */
    public int getTotalHits(Lemma lemma);

    public Lemma getLemmaById(int id);

    public List<Lemma> getMatchingLemmas(String lookupForm, int sequenceNumber, String languageCode, boolean ignoreAccents);
    public List<Lemma> getLemmasStartingWith(String lookupForm, String languageCode, boolean ignoreAccents);
    public List<Lemma> getLemmasEndingWith(String lookupForm, String languageCode, boolean ignoreAccents);
    public List<Lemma> getLemmasWithSubstring(String lookupForm, String languageCode, boolean ignoreAccents);

    /**
     * getMatchingLemma
     *
     * @param lookupForm the form to search form
     * @param languageCode the target language
     * @return a matching lemma, or null if none can be found
     */
    public Lemma getMatchingLemma(String lookupForm, String languageCode, boolean ignoreAccents);

    /**
     * Returns all lemmas in the specified language.
     */
    public List<Lemma> getAllLemmas(Language language);
}
