package perseus.morph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import perseus.language.GreekAdapter;
import perseus.language.LanguageCode;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.util.Config;

/**
 * Contains some methods for working with cruncher/morpheus.
 *
 */

public class Cruncher {
	private static Logger logger = Logger.getLogger(Cruncher.class);

	static Pattern lemmaPattern = Pattern.compile("<NL>\\w+ (?:\\S+,)?(\\S+)\\s");

	boolean ignoringAccents = false;

	public String getOutput(String form, String languageCode) {
		String cruncherPath = Config.getCruncherPath();
		String environment = "MORPHLIB=" + Config.getMorphlibPath();

		String[] command;

		if (languageCode.equals(LanguageCode.LATIN)) {
			command = new String[] {cruncherPath, "-LS"};
		}
		else {
			// Assume Greek
			if (ignoringAccents) {
				command = new String[] {cruncherPath, "-S", "-n"};
			}
			else {
				command = new String[] {cruncherPath, "-S"};
			}
		}

		StringBuffer output = new StringBuffer();

		try {
			Runtime runtime = Runtime.getRuntime();
			Process cruncherProcess = 
				runtime.exec(command, new String[] {environment});

			PrintWriter out =
				new PrintWriter(new BufferedWriter(new OutputStreamWriter(cruncherProcess.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(cruncherProcess.getInputStream()));

			out.println(form);
			out.close();

			String line;
			while ((line = in.readLine()) != null) {
				output.append(line);
				output.append("\n");
			}

			cruncherProcess.destroy();
		} catch (SecurityException se) {
			logger.error(se);
		} catch (IOException ioe) {
			logger.error(ioe);
		}

		return output.toString();
	}

	public Set<String> getFormLemmaIDs(String form, String languageCode) {
		Set<String> lemmas = new HashSet<String>();

		if (languageCode.equals(LanguageCode.OLD_NORSE)) {
			return lemmas;
		}

		String rawOutput = getOutput(form, languageCode);
		Matcher matcher = lemmaPattern.matcher(rawOutput);
		while (matcher.find()) {
			String lemmaID = matcher.group(1);
			lemmas.add(lemmaID);
		}
		return lemmas;
	}

	public Set<Lemma> getFormLemmas(String form, String languageCode) {
		Set<Lemma> lemmas = new HashSet<Lemma>();

		if (languageCode.equals(LanguageCode.OLD_NORSE)) {
			return lemmas;
		}

		String rawOutput = getOutput(form, languageCode);

		Matcher matcher = lemmaPattern.matcher(rawOutput);
		while (matcher.find()) {
			String lemmaID = matcher.group(1);
			List<Lemma> matches =
				new HibernateLemmaDAO().getMatchingLemmas(lemmaID, languageCode, false);
			if (!matches.isEmpty()) {
				lemmas.addAll(matches);
			}
		}

		return lemmas;
	}

	/** Get all cruncher lemmas of a given form. For Greek, this method searches
	first for accented forms. If it doesn't find anything, it searches ignoring 
	accents.
	 */
	public Set getLemmas(String form, String languageCode) {
		Set lemmas = null;

		if (languageCode.equals(LanguageCode.GREEK)) {
			// Do some jiggering with accents.
			// If it has an accent, first try an accented search,
			// then try ignoring accents (ie, if an accent is wrong)
			if (GreekAdapter.hasAccents(form)) {
				lemmas = getFormLemmas(form, languageCode);

				if (lemmas.size() == 0) {
					// That didn't work: try without accent.
					setIgnoreAccents(true);
					lemmas = getFormLemmas(GreekAdapter.removeAccents(form),
							languageCode);
				}
			}
			else {
				setIgnoreAccents(true);
				lemmas = getFormLemmas(form, languageCode);
			}
		}
		else {
			lemmas = getFormLemmas(form, languageCode);
		}

		return lemmas;
	}

	public void setIgnoreAccents(boolean b) {
		ignoringAccents = b;
	}
}
