/**
 * This class tries to guess the encoding--Latin transliteration, beta code,
 * Unicode, or perhaps something else--of any Greek text passed to it.
 * It's intended mostly for dealing with user input consisting of one or two
 * words at a time--it may get confused if it has to deal with larger
 * phrasal units or sentences.
 */

package perseus.document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.unc.epidoc.transcoder.TransCoder;

public class GreekInputHandler {
	private static Logger logger = Logger.getLogger(GreekInputHandler.class);

	private static final Pattern UNICODE_PATTERN =
		Pattern.compile("\\p{InGreek}|\\p{InGreekExtended}");

	private static final Pattern BETA_CODE_PATTERN =
		Pattern.compile("[()\\/=*]");

	/**
	 * Attempts to guess the encoding of the supplied text.
	 *
	 * @param text the text of uncertain encoding
	 * @return (hopefully) the name of the encoding, like "BetaCode" or
	 *		"Unicode"
	 */
	public static String guessEncoding(String text) {
		// First pass: are there any Unicode characters in the string?
		Matcher unicodeMatcher = UNICODE_PATTERN.matcher(text);
		if (unicodeMatcher.find()) {
			// NB: the transcoder wants "Unicode" as an input encoding, but
			// "UnicodeC" or "UnicodeD" as an output encoding.
			return "Unicode";
		}

		// Second pass: are there any beta-code characters? This isn't a very
		// clever pattern, but it should work, unless the user has decided to
		// be ornery and, say, input parentheses that actually represent
		// parentheses (instead of breathing marks).
		Matcher betaCodeMatcher = BETA_CODE_PATTERN.matcher(text);
		if (betaCodeMatcher.find()) {
			return "BetaCode";
		}

		return "GreekXLit";
	}

	/**
	 * Attempts to guess the encoding of the given text and transcode it to
	 * the desired encoding.
	 *
	 * @param text the string whose encoding we want to guess
	 * @param targetEncoding the encoding in which to return the string
	 * @return the string in the target encoding
	 */
	public static String transcode(String text, String targetEncoding) {
		String inputEncoding = guessEncoding(text);
		try {
			TransCoder transcoder =
				new TransCoder(inputEncoding, targetEncoding);

			return transcoder.getString(text);
		} catch (Exception e) {
			logger.error("Transcoder threw an exception: " + e);
			return null;
		}
	}
}
