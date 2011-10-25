package perseus.eval.morph;

import java.sql.*;
import java.util.*;
import java.util.regex.*;

import org.apache.log4j.Logger;

import perseus.document.Query;
import perseus.morph.Parse;
import perseus.util.SQLHandler;

public class UserVotesEvaluator extends ParseEvaluator {

	private static Logger logger = Logger.getLogger(UserVotesEvaluator.class);

	private int languageID;
	private Query query;
	private String form;

	public static final Pattern LEMMA_PATTERN =
		Pattern.compile("^(\\D+)(\\d+)$");

	public UserVotesEvaluator(int langID) {
		this(langID, null, null);
	}

	public UserVotesEvaluator(int langID, Query q) {
		this(langID, q, null);
	}

	public UserVotesEvaluator(int langID, Query q, String f) {
		super();
		languageID = langID;
		query = q;
		form = f;
	}

	protected Map<Parse, Number> evaluate(Collection<Parse> parses) {
		Map<Parse,Number> output = new HashMap<Parse,Number>();

		for (Parse parse : parses) {
			String currentLemma = parse.getLemma().getDisplayForm();
			String morphCode = parse.getCode();

			int featureFrequency =
				retrieveVoteCount(form, currentLemma, morphCode);

			output.put(parse, new Integer(featureFrequency));
		}

		return output;
	}

	public int retrieveVoteCount(String form, String currentLemma,
			String currentCode) {

		Connection con = null;
		Statement statement = null;
		try {
			con = SQLHandler.getWritableConnection();
			statement = con.createStatement();
		} catch (SQLException sqle) {
			logger.error("Problem getting database connection: " + sqle);
		}

		Matcher matcher = LEMMA_PATTERN.matcher(currentLemma);
		int sequenceNumber = 1;
		if (matcher.matches()) {
			currentLemma = matcher.group(1);
			sequenceNumber = Integer.parseInt(matcher.group(2));
		}

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) AS total FROM morph_votes WHERE ");
		if (query != null) {
			String documentID = query.getDocumentID();
			sql.append("document_id = '").append(documentID).append("' AND ");
		}
		sql.append("lang_id = ").append(languageID);
		if (form != null) {
			sql.append(" AND form = '").append(form).append("'");
		}
		sql.append(" AND lemma = '").append(currentLemma)
		.append("' AND sequence_number = ").append(sequenceNumber)
		.append(" AND morph_code = '").append(currentCode).append("'");

		int count = 0;
		try {
			ResultSet rs = statement.executeQuery(sql.toString());
			while (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException sqle) {
			logger.warn("Problem getting user votes: " + sqle);
		}

		try {
			statement.close();
			con.close();
		} catch (SQLException sqle) {
			logger.error("Problem closing connection: " + sqle);
		}

		return count;
	}

	public String getDescription() {
		return "User-voting evaluator";
	}

	public String getLongDescription() {
		return "Scores parses based on the number of votes each one has " +
		"received from users. Weighted more heavily as more users vote " +
		"for a given word in a text.";
	}
}
