package perseus.document;

import gpl.pierrick.brihaye.aramorph.AraMorph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import perseus.language.ArabicAdapter;
import perseus.language.Language;
import perseus.util.DisplayPreferences;

/**
 * A TokenFilter that converts Arabic text between encodings. Ignores Tokens 
 * that were not marked as Arabic by the tokenizer.
 */

public class ArabicTranscoderTokenFilter extends TokenFilter {

	private Logger logger = Logger.getLogger(getClass());
	private DisplayPreferences prefs;

	// change arabic below to ar to work
	private static Pattern filterPattern = Pattern.compile(
	"(lang|class)=\"[^\"]*ar[^\"]*\"");


	public ArabicTranscoderTokenFilter() {}

	public ArabicTranscoderTokenFilter (DisplayPreferences prefs) {
		this.prefs=prefs;
	}

	public void process(Token token) {
		if (token.getType() == Token.Type.WORD &&
				token.getLanguage().equals(Language.ARABIC)) {

			try {

				if (prefs.get(DisplayPreferences.ARABIC_DISPLAY_KEY).equals("Buckwalter")) {

					String prearabized=token.getDisplayText();

					// convert salmone to buckwalter
					prearabized=ArabicAdapter.salmone2Buckwalter(prearabized);
					// convert from XML friendly to original buckwalter (as HTML entities)
					prearabized=prearabized.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
					prearabized=prearabized.replaceAll("W", "&amp;").replaceAll("I", "&lt;").replaceAll("O", "&gt;");

					token.setDisplayText(prearabized);	

				} else if (prefs.get(DisplayPreferences.ARABIC_DISPLAY_KEY).equals("UnicodeC")) {

					String prearabized=token.getDisplayText();

					// convert salmone to buckwalter
					prearabized=ArabicAdapter.salmone2Buckwalter(prearabized);
					// convert from XML friendly to original buckwalter in order to tranliterate back into Unicode arabic correctly
					prearabized=prearabized.replaceAll("I", "<").replaceAll("O", ">").replaceAll("W", "&");	

					token.setDisplayText(AraMorph.arabizeWord(prearabized));
				}

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
				defaultLanguage.equals(Language.ARABIC.getCode())) return true;

		Matcher matcher = filterPattern.matcher(text);
		return matcher.find();
	}
}
