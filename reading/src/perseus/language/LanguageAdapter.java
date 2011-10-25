package perseus.language;

import java.util.Comparator;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;

import perseus.language.analyzers.DeaccentingAnalyzer;

/**
 * An adapter class that allows per-language functionality. Less useful than
 * it was when there was no Language class.
 */
public abstract class LanguageAdapter implements Comparator<String> {
    /**
     * Returns a Pattern representing all characters that do *not* constitute
     * a word.
     */
    public abstract Pattern getNonWordPattern();

    /**
     * Returns a Pattern representing a word in the given language, suitable
     * for scanning.
     */
    public abstract Pattern getWordPattern();

    /**
     * Returns an appropriate Lucene search analyzer for the this language.
     * @return an analyzer to be used in indexing documents in this language
     */
    public Analyzer getAnalyzer() {
	return new DeaccentingAnalyzer();
    }
    
    /**
     * Returns a normalized form of `s` that can be used for lookup in a
     * search index or database.
     *
     * @param s get the lookup form for this string
     * @return a version of `s` that can be used for lookup
     */
    public String getLookupForm(String s) {
	return s;
    }

    /**
     * Returns the adapter for `langCode`, or the default adapter if none
     * exists.
     *
     * @param langCode a language code found in {@link perseus.language.LanguageCode}
     * @return a LanguageAdapter for the specified language
    */
    public static LanguageAdapter getLanguageAdapter(String langCode) {
	return Language.forCode(langCode).getAdapter();
    }
    
    /**
     * Compares two strings in the given language.
     */
    public int compare(String form1, String form2) {
	return form1.compareTo(form2);
    }

    public boolean equals(Object o) {
	return o.getClass().equals(this.getClass());
    }
    
    /**
     * Returns a capitalized version of `s`. The default implementation should
     * be suitable for most languages with normal encoding schemes (that is
     * to say, not Greek).
     */
    public String capitalize(String s) {
	return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    
    /**
     * Returns an uncapitalized version of `s`. The default implementation
     * should be suitable for most languages with normal encoding schemes (that
     * is to say, not Greek).
     */
    public String uncapitalize(String s) {
	return Character.toLowerCase(s.charAt(0)) + s.substring(1);	
    }
    
    /**
     * Returns an upper-case version of `s`. The default implementation
     * should be suitable for most languages with normal encoding schemes (that
     * is to say, not Greek).
     */
    public String toUpperCase(String s) {
	return s.toUpperCase();
    }
    
    /**
     * Returns a lower-case version of `s`. The default implementation
     * should be suitable for most languages with normal encoding schemes (that
     * is to say, not Greek).
     */
    public String toLowerCase(String s) {
	return s.toLowerCase();
    }
    
    public static LanguageAdapter getLanguageAdapter(Language language) {
	return language.getAdapter();
    }
    
    /**
     * Returns whether or not words in this language are case sensitive
     *
     */
    public boolean matchCase() {
    	return false;
    }
}
