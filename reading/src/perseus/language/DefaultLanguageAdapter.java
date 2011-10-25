package perseus.language;

import java.util.regex.Pattern;

import perseus.util.StringUtil;

/**
 * A language adapter containing some settings that should work for most
 * reasonable languages.
 */

public class DefaultLanguageAdapter extends LanguageAdapter {
    
    private static Pattern WORD_PATTERN =
	Pattern.compile("([\\p{Alpha}\\[\\]]+)");
    private static Pattern NON_WORD_PATTERN =
	Pattern.compile("([^\\p{Alpha}]+)");
    
    public Pattern getNonWordPattern() {
	return NON_WORD_PATTERN;
    }

    public Pattern getWordPattern() {
	return WORD_PATTERN;
    }

    public String getLookupForm(String word) {
	return StringUtil.deaccent(word);
    }
}

