package perseus.document.dao;

import java.util.List;

import perseus.document.WordCount;
import perseus.language.Language;

/**
 * Contains a variety of methods for saving and loading WordCount objects.
 */

public interface WordCountDAO {
    /**
     * Returns all WordCounts for <kbd>documentID</kbd>.
     */
    public List<WordCount> getByDocument(String documentID);

    /**
     * Returns all WordCounts in <kbd>language</kbd>.
     */
    public List<WordCount> getByLanguage(Language language);

    /**
     * Returns the number of words for <kbd>documentID</kbd> in
     * <kbd>language</kbd>.
     */
    public long getCount(String documentID, Language language);

    /**
     * Returns the total number of words in the given language.
     */
    public long getTotalCount(String documentID);

    /**
     * Returns the total number of words in the given document.
     */
    public long getTotalCount(Language language);

    /**
     * Deletes all <kbd>WordCount</kbd> objects of the given language.
     */
    public void deleteByDocument(String documentID);

    /*
     * Returns the count of the largest collection
     */
    public long getMaxWords(String[] collections);

    /*
     * Returns the word count of the largest language
     * for a given document id
     */
    public long getMaxWordsForDocument(String documentID);

    /* 
     * Returns a list of languages for a given document id
     */
    public List<Language> getLanguagesForDocument(String documentID);
}
