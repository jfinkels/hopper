package perseus.eval.morph;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import perseus.language.Language;
import perseus.morph.MorphCode;
import perseus.morph.Parse;
import perseus.util.SQLHandler;

/**
 * ParseEvaluator subclass that gives to priority to parses with forms that
 * appear more often in the collection.
 */
public class FormFrequencyEvaluator extends ParseEvaluator {

	private static Logger logger = Logger.getLogger(FormFrequencyEvaluator.class);

	// TODO add support for looking only at the current document freqs
	private String documentID = null;
	private int languageID = -1;

	/**
	 * Creates a new FormFrequencyEvaluator using the given language.
	 *
	 * @param languageID an ID representing the current language
	 */
	public FormFrequencyEvaluator(int languageID) {
		this(languageID, null);
	}

	/**
	 * Creates a new FormFrequencyEvaluator using the given language and
	 * considering only frequencies in the given document (TODO!).
	 *
	 * @param languageID an ID representing the current language
	 * @param documentID the document to consider (not yet implemented)
	 */
	public FormFrequencyEvaluator(int languageID, String documentID) {
		super();
		this.languageID = languageID;
		this.documentID = documentID; // this isn't used yet
	}

	protected Map<Parse, Number> evaluate(Collection<Parse> parses) {

		Map<Parse,Number> output = new HashMap<Parse,Number>();

		for (Parse parse : parses) {
			Map<String,String> features = MorphCode.getFeatures(
					parse.getCode(), Language.forCode(parse.getLemma().getLanguageCode()));

			Map<String,String> exclusiveFeatures =
				findSpecialFeatures(parses, parse, features);

			// If we have enough parses that we can't find any features unique
			// to a given parse, just search for the parse's whole feature set.
			if (exclusiveFeatures.isEmpty()) {
				exclusiveFeatures = features;
			}

			double featureFrequency = retrieveFrequency(exclusiveFeatures);

			output.put(parse, featureFrequency);
		}

		return output;
	}

	private double retrieveFrequency(Map<String,String> features) {
		double frequency = 0.0;

		Connection con = SQLHandler.getConnection();
		Statement statement = null;
		try {
			statement = con.createStatement();
		} catch (SQLException sqle) {
		}

		String morphCode = MorphCode.getCode(
				features, Language.forId(languageID));

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count FROM morph_frequencies WHERE morph_code")
		.append(" = '").append(morphCode).append("' AND language_id = ")
		.append(languageID);

		try {
			ResultSet rs = statement.executeQuery(sql.toString());
			while (rs.next()) {
				frequency = rs.getDouble("count");
			}
		} catch (SQLException sqle) {
			logger.error("Problem accessing database: " + sqle);
		}

		try {
			statement.close();
			con.close();
		} catch (SQLException sqle) {
			logger.error("Problem closing database connection: " + sqle);
		}

		return frequency;
	}

	/**
	 * Finds all the features in a given feature-set that are not shared by
	 * every other feature-set. Supposing, for example, that we have the
	 * following parses:
	 * 
	 * 1st sg imperf ind act
	 * 3rd pl imperf ind act
	 * 3rd sg imperf ind act
	 *
	 * Each parse's special features will be "1st sg", "3rd pl" and "3rd sg"
	 * respectively.
	 */
	private Map<String,String> findSpecialFeatures(Collection<Parse> parses,
			Parse currentParse, Map<String,String> features) {

		Map<String,String> specialFeatures = new HashMap<String,String>();

		for (String currentFeature : features.keySet()) {
			String currentValue = features.get(currentFeature);

			// Start out by assuming that the current parse has this feature
			// in common with every other parse.
			boolean isCommonFeature = true;

			for (Parse parse : parses) {
				if (parse == currentParse) {
					continue;
				}

				// If a given parse either doesn't have this feature or has
				// a different value for the feature, it's no longer a common
				// feature.
				if (!(parse.hasFeature(currentFeature) &&
						parse.getFeature(currentFeature).equals(currentValue))) {

					isCommonFeature = false;
					break;
				}
			}
			if (!isCommonFeature) {
				specialFeatures.put(currentFeature, currentValue);
			}
		}

		return specialFeatures;
	}

	public String getDescription() {
		return "Form frequency evaluator";
	}

	public String getLongDescription() {
		return "Scores parses based on how often their morphological features "
		+ "(first-person, indicative, plural, and so on) occur among all "
		+ "the words in the Perseus corpus.";
	}
}
