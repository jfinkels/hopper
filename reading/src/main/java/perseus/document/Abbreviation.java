package perseus.document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import perseus.util.Config;
import perseus.util.StringUtil;

public class Abbreviation implements Comparable<Abbreviation> {
	private static Map<List<String>,Abbreviation> abbreviations;
	private static Logger logger;

	private static final Comparator<Abbreviation> displayFormComparator =
		new Comparator<Abbreviation>() {

		public int compare(Abbreviation a1, Abbreviation a2) {
			return a1.getDisplayForm().compareTo(a2.getDisplayForm());
		}
	};

	static {
		logger = Logger.getLogger(Abbreviation.class);
		abbreviations = loadAbbreviations();
	}

	private static Map<List<String>,Abbreviation> loadAbbreviations() {
		Map<List<String>,Abbreviation> abbrevs =
			new HashMap<List<String>,Abbreviation>();

		for (URL url : Config.getAbbreviationFiles()) {
			try {
				for (Abbreviation abbrev : fromURL(url)) {
					abbrevs.put(abbrev.getTokens(), abbrev);
				}
			} catch (IOException e) {
				logger.error("Unable to read abbreviations from " + url, e);
			}
		}

		return abbrevs;
	}

	private static List<Abbreviation> fromURL(URL url) throws IOException {
		List<Abbreviation> abbrevs = new ArrayList<Abbreviation>();

		BufferedReader reader =
			new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] abbrevParts = line.split("\t");

			List<String> tokens = Arrays.asList(
					abbrevParts[0].replaceAll("[.]", "").split("[|]"));

			String displayForm = abbrevParts[1];
			String abo = abbrevParts[2];
			if (!abo.startsWith("Perseus:")) abo = "Perseus:" + abo;

			abbrevs.add(new Abbreviation(tokens, displayForm, new Query(abo)));
		}

		return abbrevs;
	}

	private List<String> tokens;
	private String displayForm;
	private Query abo;

	private Abbreviation(List<String> tokens, String dispForm, Query abo) {
		this.tokens = tokens;
		displayForm = dispForm;
		this.abo = abo;
	}

	public static Collection<Abbreviation> getAbbreviations() {
		return abbreviations.values();
	}

	public static Abbreviation find(String citation) {
		List<String> runningCitation = normalize(citation);

		while (!runningCitation.isEmpty()) {
			logger.trace("Trying " + runningCitation);

			if (abbreviations.containsKey(runningCitation)) {
				logger.debug("Found a match: " + runningCitation);
				return abbreviations.get(runningCitation);
			}
			runningCitation.remove(runningCitation.size()-1);
		}

		logger.warn("Unrecognized abbreviation: " + citation);
		return null;
	}

	private static List<String> normalize(String citation) {
		return new ArrayList<String>(
				Arrays.asList(citation.toLowerCase().split("[ .]+")));
	}

	public static List<Abbreviation> findPossible(String citation) {
		List<String> tokens = normalize(citation);
		List<Abbreviation> matches = new ArrayList<Abbreviation>();

		for (List<String> candidate : abbreviations.keySet()) {
			if (testPossible(tokens, candidate)) {
				matches.add(abbreviations.get(candidate));
			}
		}

		return matches;
	}

	private static boolean testPossible(
			List<String> query, List<String> candidate) {

		// If the string we're looking for is *longer* than the current
		// candidate, it's definitely not a possibility.
		if (query.size() > candidate.size()) return false;

		// Now check whether the individual tokens match, *except* for the
		// last one, which we'll handle separately.
		int lastToken = query.size()-1;
		for (int i = 0; i < lastToken; i++) {
			if (!candidate.get(i).equals(query.get(i))) {
				return false;
			}
		}

		// The last token in the string we're looking for can be either an
		// exact or a partial match; for "Aesc", find both "Aesch." and
		// "Aeschin." 
		if (!candidate.get(lastToken).startsWith(query.get(lastToken))) {
			return false;
		}

		return true;
	}

	public Query getABO() {
		return abo;
	}
	public void setABO(Query abo) {
		this.abo = abo;
	}

	public String getDisplayForm() {
		return displayForm;
	}
	public void setDisplayForm(String displayForm) {
		this.displayForm = displayForm;
	}

	public List<String> getTokens() {
		return tokens;
	}
	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}

	public String toString() {
		return String.format("%s -> %s", displayForm, abo);
	}

	public int compareTo(Abbreviation a) {
		return getDisplayForm().compareTo(a.getDisplayForm());
	}

	public static Collection<Abbreviation> getAll() {
		Set<Abbreviation> abbrevs =
			new TreeSet<Abbreviation>(displayFormComparator);
		abbrevs.addAll(abbreviations.values());
		return abbrevs;
	}

	public List<String> getNonABOTokens(String citation) {
		List<String> normalized = normalize(citation);
		for (String token : getTokens()) {
			normalized.remove(token);
		}
		return normalized;
	}

	public static Abbreviation forABO(Query aboQuery) {
		for (Abbreviation abbrev : getAbbreviations()) {
			if (abbrev.getABO().equals(aboQuery)) return abbrev;
		}

		return null;
	}
}