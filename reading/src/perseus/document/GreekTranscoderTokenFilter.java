package perseus.document;

import org.apache.log4j.Logger;
import java.io.*;

import edu.unc.epidoc.transcoder.*;

import perseus.language.Language;
import perseus.util.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A TokenFilter that converts Greek text between encodings, using Epidoc's
 * transcoder library. Ignores Tokens that were not marked as Greek by the
 * tokenizer.
 */

public class GreekTranscoderTokenFilter extends TokenFilter {
    
    private Logger logger = Logger.getLogger(getClass());

    private static Pattern filterPattern = Pattern.compile(
	    "(lang|class)=\"[^\"]*greek[^\"]*\"");
    private TransCoder tc = null;
    
    public GreekTranscoderTokenFilter (DisplayPreferences prefs) {
        try {
            tc = new TransCoder(prefs.get(DisplayPreferences.GREEK_INPUT_KEY),
                                prefs.get(DisplayPreferences.GREEK_DISPLAY_KEY));
        } catch (Exception e) {
            logger.error("Problem creating transcoder", e);
        }
    }

    /** this version of the constructor is designed for offline processing with 
	default Greek encodings. */
    public GreekTranscoderTokenFilter (String target) {
	this("BetaCode", target);
    }

    public GreekTranscoderTokenFilter (String encoding, String target) {
        try {
            tc = new TransCoder(encoding, target);
        } catch (Exception e) {
	    logger.error("Problem creating transcoder: ", e);
        }
    }

    public void process(Token token) {
	if (token.getType() == Token.Type.WORD &&
		token.getLanguage().equals(Language.GREEK)) {
			
	    try {
		// the xmlEscape is necessary so characters like less-thans
		// and ampersands (which SPIonic outputs regularly) don't get
		// interpreted wrongly by XML parsers
		token.setDisplayText(StringUtil.xmlEscape(
			    tc.getString(token.getDisplayText())));
	    } catch (UnsupportedEncodingException e) {
		logger.error("Problem transcoding:", e);
	    } catch (Exception e) {
		logger.error("Non-encoding-related problem transcoding: ", e);
	    }

	}
	else if (token.getType() == Token.Type.ENTITY) {
	    if (token.getDisplayText().startsWith("_lpar;")) {
		token.setDisplayText("(");
	    }
	    else if (token.getDisplayText().startsWith("_rpar;")) {
		token.setDisplayText(")");
	    }
	}
    }

    public boolean willFilter(String text, String defaultLanguage) {
	if (defaultLanguage != null &&
	    defaultLanguage.equals(Language.GREEK.getCode())) return true;

	Matcher matcher = filterPattern.matcher(text);
	return matcher.find();
    }
}
