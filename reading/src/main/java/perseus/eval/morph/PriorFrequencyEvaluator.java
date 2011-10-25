package perseus.eval.morph;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import perseus.language.LanguageCode;
import perseus.morph.Lemma;
import perseus.morph.Parse;
import perseus.util.SQLHandler;

public class PriorFrequencyEvaluator extends ParseEvaluator {

	private static Logger logger = Logger.getLogger(PriorFrequencyEvaluator.class);

//	private static double maxFrequency = 1.0;

//	private String documentID = null;
	private int languageID = -1;

	String priorForm = null;
	Map<Lemma,List<Parse>> priorParses = null;

	private static final Pattern TRIVIAL_MORPHCODE_PATTERN =
		Pattern.compile("^\\w-+$");

	public PriorFrequencyEvaluator(int languageID) {
		this(languageID, null);
	}

	public PriorFrequencyEvaluator(int languageID, String documentID) {
		this(languageID, documentID, null);
	}

	public PriorFrequencyEvaluator(int languageID, String documentID,
			String priorForm) {

		super();
		this.languageID = languageID;
//		this.documentID = documentID;
		if (priorForm != null) {
			setPriorForm(priorForm);
		}

		setProperty("evaluatePrecedingAuto", "true");
		//setProperty("evaluatePrecedingMorphCode", "true");
		//setProperty("evaluatePrecedingLemma", "true");

		setProperty("evaluateCurrentMorphCode", "true");
		setProperty("evaluateCurrentLemma", "true");
	}

	public void setPriorForm(String pf) {
		priorForm = pf;
		String language = LanguageCode.getLanguageCode(languageID);
		priorParses = Parse.getParses(pf, language);
	}

	public String getPriorForm() {
		return priorForm;
	}

	protected Map<Parse, Number> evaluate(Collection<Parse> parses) {

		Map<Parse,Number> output = new HashMap<Parse,Number>();

		for (Parse parse : parses) {
			Lemma currentLemma = parse.getLemma();
			String morphCode = parse.getCode();

			double featureFrequency =
				retrieveFrequencySum(currentLemma.getDisplayForm(), morphCode);

			output.put(parse, featureFrequency);
		}

		return output;
	}

	public double retrieveFrequencySum(String currentLemma,
			String currentCode) {

		Connection con = SQLHandler.getConnection();
		Statement statement = null;
		try {
			statement = con.createStatement();
		} catch (SQLException sqle) {
		}

		Boolean evaluatePrecedingLemma;
		Boolean evaluatePrecedingMorphCode;
		if (hasProperty("evaluatePrecedingAuto")) {
			boolean useMorphCode = hasNonTrivialParse(priorParses);

			evaluatePrecedingLemma = 
				new Boolean(!useMorphCode);
			evaluatePrecedingMorphCode =
				new Boolean(useMorphCode);
		} else {
			evaluatePrecedingLemma =
				new Boolean(getProperty("evaluatePrecedingLemma"));
			evaluatePrecedingMorphCode =
				new Boolean(getProperty("evaluatePrecedingMorphCode"));
		}

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(count) AS total FROM prior_frequencies WHERE ");

		if (priorParses != null && !priorParses.isEmpty()) {
			sql.append("(");
			for (Lemma lemma : priorParses.keySet()) {
				sql.append("(");

				if (evaluatePrecedingLemma.equals(Boolean.TRUE)) {
					sql.append("prior_lemma = '").append(lemma.getDisplayForm()).append("' ");
				}
				if (evaluatePrecedingMorphCode.equals(Boolean.TRUE)) {
					if (evaluatePrecedingLemma.equals(Boolean.TRUE)) {
						sql.append("AND ");
					}
					sql.append("prior_morph_code IN ('");

					List<Parse> priorParsesForLemma = priorParses.get(lemma);

					for (Parse parse : priorParsesForLemma) {
						String parseCode = parse.getCode();

						sql.append(parseCode).append("'");
						sql.append(",'");
					}
					sql.delete(sql.length()-",'".length(), sql.length());
					sql.append(")"); // close IN statement
				}

				sql.append(")"); // close prior_lemma/morph_code statement
				sql.append(" OR ");
			}
			sql.delete(sql.length()-" OR ".length(), sql.length());
			sql.append(")");

			sql.append(" AND ");
		}

		if (hasProperty("evaluateCurrentLemma")) {
			sql.append("lemma = '").append(currentLemma).append("' AND ");
		}

		if (hasProperty("evaluateCurrentMorphCode")) {
			sql.append(" morph_code")
			.append(" = '").append(currentCode).append("'")
			.append(" AND language_id = ").append(languageID);
		}

		double totalFrequency = 0.0;

		try {
			ResultSet rs = statement.executeQuery(sql.toString());
			while (rs.next()) {
				totalFrequency += rs.getDouble("total");
			}
		} catch (SQLException sqle) {
			logger.warn("Problem getting prior frequencies: " + sqle);
		}

		try {
			statement.close();
			con.close();
		} catch (SQLException sqle) {
			logger.error("Problem closing database connection: " + sqle);
		}

		return totalFrequency;
	}

	/**
	 * Checks to see whether the given set of parses contains at least one
	 * parse that's "nontrivial", where a "trivial" parse means an
	 * indeclinable form. If we find only trivial parses, we want to search
	 * based on preceding lemma rather than preceding morph code.
	 */
	private boolean hasNonTrivialParse(Map<Lemma,List<Parse>> parses) {
		List<Parse> flatParses = flattenParses(parses);

		for (Parse parse : flatParses) {
			String morphCode = parse.getCode();
			Matcher matcher = TRIVIAL_MORPHCODE_PATTERN.matcher(morphCode);

			if (!matcher.matches()) {
				return true;
			}
		}

		return false;
	}

	public String getDescription() {
		return "Prior-form frequency evaluator";
	}

	public String getLongDescription() {
		return "Evaluates forms based on the preceding word in the text; " +
		"finds the most likely parse among this word's possible " +
		"morphological features and the preceding word's possible " +
		"features based on the frequency of each possible pair.";
	}
}
