package perseus.document;

import perseus.language.Language;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents a count of words in a given language and document (which may be a
 * corpus or a collection).
 */

public class WordCount implements Comparable<WordCount> {
    /** This instance's Hibernate-based ID */
    private Integer id;

    /** The ID of the document being counted */
    private String documentID;

    /** The language */
    private Language lang;

    /** The actual count */
    private long wordCount;

    public static Comparator<WordCount> COUNT_COMPARATOR =
	new Comparator<WordCount>() {

	public int compare(WordCount a, WordCount b) {
	    return (int) (a.getWordCount() - b.getWordCount());
	}
    };
    
    public WordCount() {}

    public WordCount(Language l, String id, long c) {
	lang = l;
	documentID = id;
	wordCount = c;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language l) {
        lang = l;
    }

    public long getWordCount() {
        return wordCount;
    }

    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
    }
    
    public boolean equals(Object o) {
	if (!getClass().isAssignableFrom(o.getClass())) return false;
	
	WordCount w = (WordCount) o;
	return getDocumentID().equals(w.getDocumentID()) &&
		getLang().equals(w.getLang());
    }
    
    public int hashCode() {
	int result = 17;
	
	result += 37 * getDocumentID().hashCode();
	result += 37 * getLang().hashCode();
	
	return result;
    }

    public int compareTo(WordCount wc) {
	int docResult = getDocumentID().compareTo(wc.getDocumentID());
	if (docResult != 0) return docResult;
	
	return getLang().compareTo(wc.getLang());
    }

    /**
     * Returns a Query representing this WordCount's document. A convenience
     * method.
     *
     * @return a Query representing this WordCount's document
     */
    public Query getQuery() {
	return new Query(documentID);
    }	
    
    public String toString() {
	return String.format("WordCount[%s,%s: %d]",
		getDocumentID(), getLang(), getWordCount());
    }

    private static SortedSet<WordCount> getSortedCounts(
	Collection<WordCount> c) {
	SortedSet<WordCount> sortedCounts =
	    new TreeSet<WordCount>(COUNT_COMPARATOR);
	sortedCounts.addAll(c);
	return sortedCounts;
    }

    public static WordCount getLargest(Collection<WordCount> counts) {
	return getSortedCounts(counts).last();
    }

    public static WordCount getSmallest(Collection<WordCount> counts) {
	return getSortedCounts(counts).first();
    }
}
