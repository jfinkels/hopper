package perseus.voting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import perseus.util.SQLHandler;

public class Sense implements Comparable {
	private static Logger logger = Logger.getLogger(Sense.class);

	String lemma;
	String lexiconID;
	String sense;
	int entryID;
	int senseID;
	int level;
	String shortDefinition;

	public static final Pattern LEMMA_PATTERN =
		Pattern.compile("^(\\D+)(\\d+)$");

	public Sense(String lem, String lexID, String sense, int entryID,
			int senseID, int level, String shortDef) {
		lemma = lem;
		lexiconID = lexID;
		this.sense = sense;
		this.entryID = entryID;
		this.senseID = senseID;
		shortDefinition = shortDef;
		this.level = level;
	}

	// ACCESSOR METHODS
	public String getLemma() {
		return lemma;
	}

	public String getLexiconID() {
		return lexiconID;
	}

	public String getSense() {
		return sense;
	}

	public String getShortDefinition() {
		return shortDefinition;
	}

	public int getEntryID() {
		return entryID;
	}

	public int getSenseID() {
		return senseID;
	}

	public int getLevel() {
		return level;
	}

	// MODIFIER METHODS
	public void setLemma(String lmm) {
		lemma = lmm;
	}

	public void setLexiconID(String lxcnID) {
		lexiconID = lxcnID;
	}

	public void setSense(String sns) {
		sense = sns;
	}

	public void setShortDefinition(String shrtDfntn) {
		shortDefinition = shrtDfntn;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setEntryID(int entID) {
		entryID = entID;
	}

	public void setSenseID(int senID) {
		senseID = senID;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append(lemma);
		buf.append(" (");
		buf.append(sense);
		buf.append(", n");
		buf.append(entryID);
		buf.append(".");
		buf.append(senseID);
		buf.append(") [");
		buf.append(lexiconID);
		buf.append(" ]: ");
		buf.append(shortDefinition);

		return buf.toString();
	}

	public String senseInfo() {
		StringBuffer buf = new StringBuffer();

		buf.append(sense);
		buf.append(" (");
		buf.append(entryID);
		buf.append(".");
		buf.append(senseID);
		buf.append(")");
		if (shortDefinition != null && !shortDefinition.equals("")) {
			buf.append(": ");
			buf.append(shortDefinition);
		}

		return buf.toString();
	}

	public boolean equals(Object o) {
		if (o instanceof Sense) {
			Sense s = (Sense) o;
			if (lemma.equals(s.lemma) 
					&& lexiconID.equals(s.getLexiconID())
					&& sense.equals(s.getSense())
					&& (entryID == s.getEntryID())
					&& (senseID == s.getSenseID())) {
				return true;
			}
		}
		return false;
	}

	// Comparing senses in this way only really makes sense for ordering
	// specific senses within an entry or entries within a lexicon, so we'll
	// only check senseID and entryID
	public int compareTo(Object o) {
		if (o instanceof Sense) {
			Sense s = (Sense) o;

			if (entryID == s.getEntryID()) {
				if (senseID > s.getSenseID()) return 1;
				if (senseID < s.getSenseID()) return -1;
			}

			if (entryID > s.getEntryID()) return 1;
			if (entryID < s.getEntryID()) return -1;

			return 0;
		} else {
			throw new ClassCastException();
		}
	}

	/**
	 * Returns all the senses for a given lexicon entry.
	 *
	 * @param lexiconID the ID of the target lexicon
	 * @param lemma the lemma for which to return senses
	 *
	 * @return a List of Sense objects corresponding to the dictionary entry's
	 *	senses
	 */
	public static List getSenses(String lexiconID, String lemma) {

		Connection con = null;
		SQLHandler sqlHandler = null;

		String lemmaText;
		int sequenceNumber = 1;

		List output = new ArrayList();

		Matcher lemmaMatcher = LEMMA_PATTERN.matcher(lemma);
		if (lemmaMatcher.matches()) {
			lemmaText = lemmaMatcher.group(1);
			sequenceNumber = Integer.parseInt(lemmaMatcher.group(2));
		} else {
			lemmaText = lemma;
		}

		try {
			con = SQLHandler.getConnection();
			sqlHandler = new SQLHandler(con);

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT entry_id, sense_id, sense, level, ")
			.append("short_definition FROM senses WHERE document_id = '")
			.append(lexiconID).append("' AND lemma = '")
			.append("entry=").append(lemma).append("'")
			.append(" ORDER BY sense_id ASC");

			ResultSet rs = sqlHandler.executeQuery(sql.toString());

			while (rs.next()) {
				int entryID = rs.getInt("entry_id");
				int senseID = rs.getInt("sense_id");

				String senseText = rs.getString("sense");
				int level = rs.getInt("level");
				String shortDefinition = rs.getString("short_definition");

				Sense sense = new Sense(lemma, lexiconID, senseText, entryID,
						senseID, level, shortDefinition);
				output.add(sense);
			}
		} catch (Exception e) {
			logger.error("Problem getting vote counts");
			e.printStackTrace();
		} finally {
			try {
				if (sqlHandler != null) {
					sqlHandler.releaseAll();
				}
			} catch (SQLWarning w) {
				logger.fatal("Problem releasing resources", w);
			}
			try {
				if (con != null) con.close();
			} catch (SQLException s) {
				logger.fatal("Problem releasing connection", s);
			}
		}
		return output;
	}
}
