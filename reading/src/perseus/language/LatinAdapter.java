package perseus.language;

import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;

import perseus.language.analyzers.latin.LatinAnalyzer;
import perseus.util.StringUtil;

/**
 * The LanguageAdapter for Latin.
 * 
 * @see LanguageAdapter
*/
public class LatinAdapter extends DefaultLanguageAdapter {
    
    private static final Pattern nonWordPattern =
	Pattern.compile("([^a-zA-Z\\u008c\\u009c\\u00c0\\u00c1\\u00c2\\u00c3\\u00c4\\u00c6\\u00c8\\u00c9\u00ca\\u00cb\\u00cc\\u00cd\\u00ce\\u00cf\\u00d2\\u00d3\\u00d4\\u00d5\\u00d6\\u00d9\\u00da\\u00db\\u00dc\\u00e0\\u00e1\\u00e2\\u00e3\\u00e4\\u00e6\\u00e8\\u00e9\\u00ea\\u00eb\\u00ec\\u00ed\\u00ee\\u00ef\\u00f2\\u00f3\\u00f4\\u00f5\\u00f6\\u00f9\\u00fa\\u00fb\\u00fc\\u0100\\u0101\\u0102\\u0103\\u0112\\u0113\\u0114\\u0115\\u012a\\u012b\\u012c\\u012d\\u014c\\u014d\\u014e\\u014f\\u016a\\u016b\\u016c\\u016d]+)");
    private static final Pattern wordPattern =
	Pattern.compile("([a-zA-Z\\u008c\\u009c\\u00c0\\u00c1\\u00c2\\u00c3\\u00c4\\u00c6\\u00c8\\u00c9\u00ca\\u00cb\\u00cc\\u00cd\\u00ce\\u00cf\\u00d2\\u00d3\\u00d4\\u00d5\\u00d6\\u00d9\\u00da\\u00db\\u00dc\\u00e0\\u00e1\\u00e2\\u00e3\\u00e4\\u00e6\\u00e8\\u00e9\\u00ea\\u00eb\\u00ec\\u00ed\\u00ee\\u00ef\\u00f2\\u00f3\\u00f4\\u00f5\\u00f6\\u00f9\\u00fa\\u00fb\\u00fc\\u0100\\u0101\\u0102\\u0103\\u0112\\u0113\\u0114\\u0115\\u012a\\u012b\\u012c\\u012d\\u014c\\u014d\\u014e\\u014f\\u016a\\u016b\\u016c\\u016d]+)");

    public LatinAdapter () {
    }
    
    public Analyzer getAnalyzer() {
	return new LatinAnalyzer(false);
    }

    /**
     * Retrieve the regexp for non-word pattern in Latin
     *
     * @return the pattern for non words
     */
    public Pattern getNonWordPattern() {
	return nonWordPattern;
    }

    /**
     * Retrieve the regexp for the word pattern in Latin
     *
     * @return the pattern for words
     */
    public Pattern getWordPattern() {
	return wordPattern;
    }
    
    /**
     * Retrieves the lookup form for the given string. In the case of Latin,
     * this is equivalent to deaccenting the word--removing accents and length
     * marks, and splitting up ligatures into their constituent letters.
     *
     * @param s the word to be looked up
     * @return a the form in which the word should be looked up
     */
    public String getLookupForm(String s) {
	return StringUtil.deaccent(s);
    }
}
