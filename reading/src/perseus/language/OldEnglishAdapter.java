package perseus.language;

import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


/**
 * The LanguageAdapter for Old English
*/
public class OldEnglishAdapter extends DefaultLanguageAdapter {
    
    private static Pattern nonWordPattern;
    private static Pattern wordPattern;

    static {
	nonWordPattern = Pattern.compile("^([^a-zA-Z\\u0100\\u0101\\u0112\\u0113\\u012a\\u012b\\u014c\\u014d\\u016a\\u016b\\u0232\\u0233\\u01e2\\u01e3\\u00f0\\u00d0\\u00e6\\u00c6\\u00de\\u00fe]+)");
	wordPattern = Pattern.compile("^([a-zA-Z\\u0100\\u0101\\u0112\\u0113\\u012a\\u012b\\u014c\\u014d\\u016a\\u016b\\u0232\\u0233\\u01e2\\u01e3\\u00f0\\u00d0\\u00e6\\u00c6\\u00de\\u00fe\\[\\]]+)");
    }
    
    public Analyzer getAnalyzer() {
	return new StandardAnalyzer();
    }

    /**
     * Retrieve the regexp for non-word pattern in Old English
     *
     * @return the pattern for non words
    */
    public Pattern getNonWordPattern() {
	return nonWordPattern;
    }

    /** Retrieve the regexp for the word pattern in Old English
     * 
     * @return the pattern for words
     */
    public Pattern getWordPattern() {
	return wordPattern;
    }
}
