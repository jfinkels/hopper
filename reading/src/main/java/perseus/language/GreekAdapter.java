package perseus.language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;

import perseus.language.analyzers.greek.GreekAnalyzer;

/**
 * The language adapter for Greek.
 * 
 * @see LanguageAdapter
 */
public class GreekAdapter extends DefaultLanguageAdapter {

	private static final Pattern nonWordPattern =
		Pattern.compile("([^a-z\\*\\'\\/\\\\\\=\\(\\)\\|\\+\\^\\[\\]\\?]+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern wordPattern =
		Pattern.compile("([\\*a-z\\'\\/\\\\\\=\\(\\)\\|\\+\\^\\[\\]\\?_-]+)",
				Pattern.CASE_INSENSITIVE);

	private static final Pattern accentPattern = Pattern.compile("[/=\\\\]");
	private static final Pattern graveAccentPattern = Pattern.compile("\\\\");
	private static final Pattern secondaryAccentPattern =
		Pattern.compile("([/=].*)/");

	/**
	 * Class constructor.
	 */
	public GreekAdapter () {
	}

	public Analyzer getAnalyzer() {
		return new GreekAnalyzer();
	}

	public Pattern getNonWordPattern() {
		return nonWordPattern;
	}

	public Pattern getWordPattern() {
		return wordPattern;
	}

	public Pattern getAccentPattern() {
		return accentPattern;
	}

	public Pattern getGraveAccentPattern() {
		return graveAccentPattern;
	}

	public Pattern getSecondaryAccentPattern() {
		return secondaryAccentPattern;
	}

	/**
	 * Returns a normalized form of the given word that can be used to look up
	 * the word in a database. In this case, we convert grave accents to acutes
	 * and remove secondary accents (i.e., accents caused by following
	 * enclitics).
	 *
	 * @param s the string to be normalized
	 * @return the string in its lookup-form
	 */
	public String getLookupForm(String s) {
		// remove extra accents, change graves to acutes

		Matcher graveAccentMatcher = graveAccentPattern.matcher(s);
		s = graveAccentMatcher.replaceAll("/");

		Matcher secondaryAccentMatcher = secondaryAccentPattern.matcher(s);
		if (secondaryAccentMatcher.find()) {
			s = secondaryAccentMatcher.replaceFirst(secondaryAccentMatcher.group(1));
		}

		// Remove philological marks (eg [k]ai)
		s = s.replaceAll("[\\[\\]]", "");

		return s;
	}

	/**
	 * Returns true if the word has one or more accents.
	 *
	 * @param word the word in question
	 * @return true if the word has accents
	 */
	public static boolean hasAccents(String word) {
		Matcher m = accentPattern.matcher(word);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a copy of the word with accents removed.
	 * 
	 * @param word the word to remove accents from
	 * @return the word without accents
	 */
	public static String removeAccents(String word) {
		Matcher m = accentPattern.matcher(word);
		return m.replaceAll("");
	}

	// For beta code, a capital letter is signified by an asterisk;
	// whether a given letter is upper- or lower-case is irrelevant
	public int compare(String form1, String form2) {
		return form1.compareToIgnoreCase(form2);
	}

	@Override
	public String capitalize(String s) {
		return (s.indexOf("*") != -1 ? s :
			s.replaceFirst("^(\\w)(\\W*)", "*$2$1"));
	}

	@Override
	public String uncapitalize(String s) {
		return s.replaceFirst("^\\*(\\W*)(\\w)", "$2$1");
	}

	@Override
	public String toUpperCase(String s) {
		StringBuilder output = new StringBuilder();

		// First call capitalize(), which may twiddle accents around
		String capitalized = capitalize(s);

		boolean seenAsterisk = false;
		// Now convert the rest of the string to upper-case, skipping any
		// letters with asterisks preceding them
		for (int i = 0, n = capitalized.length(); i < n; i++) {
			if (Character.isLetter(capitalized.charAt(i))) {
				if (!seenAsterisk) {
					output.append("*");
				} else {
					seenAsterisk = false;
				}
			} else if (capitalized.charAt(i) == '*') {
				seenAsterisk = true;
			}
			output.append(capitalized.charAt(i));
		}

		return output.toString();
	}

	@Override
	public String toLowerCase(String s) {
		return uncapitalize(s).replaceAll("\\*", "");
	}
}
