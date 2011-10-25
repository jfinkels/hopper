package perseus.document;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import perseus.language.LanguageCode;
import perseus.util.DisplayPreferences;
import edu.unc.epidoc.transcoder.TransCoder;

public class GreekFilter {
	private static Logger logger = Logger.getLogger(GreekFilter.class);

	private static Pattern tagPattern;
	private static Pattern entityPattern;

	static {
		tagPattern = Pattern.compile("((?:<[^>]+>)|(?:&.*?;))");
		entityPattern = Pattern.compile("([&_].*?;)");
	}

	private DisplayPreferences prefs;

	private Stack<String> languageStack;

	public StringBuffer output;

	public GreekFilter (DisplayPreferences prefs) {
		this.prefs = prefs;

		languageStack = new Stack<String>();

		// Set English as default language
		languageStack.push(LanguageCode.ENGLISH);
	}	

	public GreekFilter (String languageCode, DisplayPreferences prefs) {
		this.prefs = prefs;

		languageStack = new Stack<String>();

		// Set English as default language
		languageStack.push(languageCode);

	}

	public void pushLanguage(String languageCode) {
		languageStack.push(languageCode);
	}

	public void popLanguage() {
		languageStack.pop();
	}

	private List<String> tokenize(String text) {
		List<String> results = new ArrayList<String>();

		Matcher m = tagPattern.matcher(text);

		// Special case: text contains no tags
		if (! m.find()) {
			results.add(text);
			return results;
		}
		m.reset();

		int start = 0;
		while (m.find()) {
			if (m.start() > start) {
				results.add (text.substring(start, m.start()));
			}
			results.add (m.group());

			start = m.end();
		}

		return results;
	}

	public String getCurrentLanguage() {
		return (String) languageStack.peek();
	}

	private boolean inGreek() {
		return languageStack.peek().equals(LanguageCode.GREEK);
	}

	public String filter(String in) {
		List<String> tokens = tokenize(in);
		String token;

		String targetEncoding = "UnicodeC";
		if (prefs != null) {
			targetEncoding = prefs.get(DisplayPreferences.GREEK_DISPLAY_KEY);
		}

		TransCoder tc = null;
		try {
			tc = new TransCoder("BetaCode", targetEncoding);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		output = new StringBuffer(in.length());


		for (int i=0;i<tokens.size();i++) {
			token = (String) tokens.get(i);

			if (token.startsWith("<")) {
				if (token.equalsIgnoreCase("<g>")) {
					// outputting this rather than <g> makes us XTML compliant
					output.append("<span class=\"greek\">");

					pushLanguage(LanguageCode.GREEK);
				}
				else if (token.equalsIgnoreCase("</g>")) {
					// see above
					output.append("</span>");

					popLanguage();
				} else {
					output.append(token);
				}
			}
			else if (token.startsWith("&")) {
				output.append(token);
			}
			else if (inGreek()) {
				try {
					output.append(tc.getString(token));
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage());
				}
			}
			else {
				output.append(token);
			}
		}

		//output.append("hellenized in " + (System.currentTimeMillis() - startTime) + " milliseconds");

		return output.toString();

	}

}
