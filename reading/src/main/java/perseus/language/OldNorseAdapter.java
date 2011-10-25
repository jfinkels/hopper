package perseus.language;

import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


/**
 * The LanguageAdapter for Old Norse
 */
public class OldNorseAdapter extends DefaultLanguageAdapter {
    
    private Pattern nonWordPattern;
    private Pattern wordPattern;
    
    /**
     * Class constructor
    */
    public OldNorseAdapter () {
	nonWordPattern = Pattern.compile("^([^a-zA-Z\\u00e1\\u00e9\\u00ed\\u00f3\\u00fa\\u00fd\\u00c1\\u00c9\\u00cd\\u00d3\\u00da\\u00dd\\u00f6\\u00d6\\u0153\\u0152\\u00f0\\u00d0\\u00e6\\u00c6\\u00de\\u00fe]+)");
	wordPattern = Pattern.compile("^([a-zA-Z\\u00e1\\u00e9\\u00ed\\u00f3\\u00fa\\u00fd\\u00c1\\u00c9\\u00cd\\u00d3\\u00da\\u00dd\\u00f6\\u00d6\\u0153\\u0152\\u00f0\\u00d0\\u00e6\\u00c6\\u00de\\u00fe\\[\\]]+)");
    }

    public Analyzer getAnalyzer() {
	return new StandardAnalyzer();
    }

    /**
     * Retrieve the regexp for the non-word pattern in Old Norse
     *
     * @return the pattern for non words
     */
    public Pattern getNonWordPattern() {
	return nonWordPattern;
    }

    /**
     * Retrieve the regexp for the word pattern in Old Norse
     *
     * @return the pattern for words
     */
    public Pattern getWordPattern() {
	return wordPattern;
    }
}
