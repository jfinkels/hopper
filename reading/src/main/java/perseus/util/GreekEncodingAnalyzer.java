package perseus.util;

import java.lang.Character.UnicodeBlock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.unc.epidoc.transcoder.TransCoder;

public class GreekEncodingAnalyzer {
	private static Logger logger = Logger.getLogger(GreekEncodingAnalyzer.class);

	/*    private static final Pattern UNICODE_PATTERN =
	Pattern.compile("[\\p{Punct}\\p{Digit}\\p{Space}\\u0370-\\u03FF\\u1F00-\\u1FFE]+");
	 */
	private static final Pattern BETA_CODE_PATTERN =
		Pattern.compile("[\\\\/\\()=]");

	private static final Pattern BETA_CODE_CHARS_PATTERN =
		Pattern.compile("[qywf]|c(?!h)", Pattern.CASE_INSENSITIVE);

	/**
	 * Tries to determine the encoding of the given Greek text. Returns a
	 * String corresponding to one of the encodings accepted by the TransCoder
	 * class.
	 * 
	 * We only support Unicode or BetaCode
	 * 
	 * @param text the Greek text to analyze
	 * @return the text's encoding ("BetaCode", "Unicode", etc.)
	 */
	public static String guessEncoding(String text) {

		for (int i = 0, n = text.length(); i < n; i++) {
			char ch = text.charAt(i);
			UnicodeBlock containingBlock = UnicodeBlock.of(ch);

			if (containingBlock == UnicodeBlock.GREEK
					|| containingBlock == UnicodeBlock.GREEK_EXTENDED) {
				return "Unicode";
			}
		}
		
		return "BetaCode";

		/*Ignore the rest since we only support Unicode or BetaCode
		
		// Okay, no Unicode Greek characters...

		Matcher betacodeMatcher = BETA_CODE_PATTERN.matcher(text);
		if (betacodeMatcher.find()) {
			return "BetaCode";
		}

		// Now run some checks for some characters, like "q", that appear in
		// beta code but not in transliteration.
		if (BETA_CODE_CHARS_PATTERN.matcher(text).find()) {
			return "BetaCode";
		}

		// an "h" as anything but the first letter indicates beta code
		if (text.indexOf("h") > 0 || text.indexOf("H") > 0) {
			return "BetaCode";
		}

		// We'll only get here if the beta code doesn't have any accents or
		// breathing-marks. In this case, it's likely that the user has entered
		// an accent-less word (like "toi" or "te"), and so it shouldn't matter
		// whether it gets classified as BetaCode or GreekXLit.
		// However, we can't use GreekXLit because the class for parsing it doesn't exist,
		// so for now, default to BetaCode until we can handle GreekXLit
		return "BetaCode";
		
		*/
	}

	public static String transcode(String text, String outputEncoding) {
		String inputEncoding = guessEncoding(text);
		try {
			return new TransCoder(inputEncoding, outputEncoding).getString(text);
		} catch (Exception e) {
			logger.error("Problem transcoding:");
			e.printStackTrace();
			return "";
		}
	}
}
