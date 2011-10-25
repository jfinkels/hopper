package perseus.voting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import perseus.language.LanguageCode;
import perseus.morph.Parse;
import perseus.morph.dao.HibernateParseDAO;
import perseus.morph.dao.ParseDAO;
import perseus.util.SQLHandler;
import perseus.util.StringUtil;

public class VoteManager {

    private static Logger logger = Logger.getLogger(VoteManager.class);

    public static final Pattern ID_PATTERN =
	Pattern.compile("n(\\d+)\\.(\\d+)");

    public static final Pattern LEMMA_PATTERN =
	Pattern.compile("^(\\D+)(\\d+)$");

    public static boolean recordSenseVote(FormInstance form, String lexQuery,
	    String vote, String ipAddress) {

	Connection con = null;
	SQLHandler sqlHandler = null;

	try {

	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);

	    int lemmaStart = lexQuery.indexOf("entry=");
	    String lexiconID = lexQuery.substring(0, lemmaStart-1);
	    String lemma = lexQuery.substring(lemmaStart);
	    int entryID;
	    int senseID;

	    Matcher matcher = ID_PATTERN.matcher(vote);
	    if (matcher.matches()) {
		entryID = Integer.parseInt(matcher.group(1));
		senseID = Integer.parseInt(matcher.group(2));
	    } else {
		throw new IllegalArgumentException("Bad ID: " + vote);
	    }

	    String[] parameters = {lexiconID, lemma, form.getDocument(),
		StringUtil.sqlEscape(form.getSubquery()),
		StringUtil.sqlEscape(form.getForm()),
		Integer.toString(form.getOccurrence()),
		Integer.toString(entryID), Integer.toString(senseID)};

	    // We want to give SQL an unquoted NULL instead of a quoted "null"
	    String insertSenseStmt = "REPLACE INTO sense_votes VALUES ('"
		+ StringUtil.join(parameters, "', '") + "', null, '"
		+ ipAddress + "')";

	    /*insertSenseStmt.setString(1, lexiconID);
	      insertSenseStmt.setString(2, lemma);
	      insertSenseStmt.setString(3, form.getDocument());
	      insertSenseStmt.setString(4, form.getSubquery());
	      insertSenseStmt.setString(5, form.getForm());
	      insertSenseStmt.setInt(6, form.getOccurrence());
	      insertSenseStmt.setInt(7, entryID);
	      insertSenseStmt.setInt(8, senseID);
	    // Our timestamp will be set automatically
	    insertSenseStmt.setString(9, ipAddress);
	    */

	    sqlHandler.executeUpdate(insertSenseStmt);

	} catch (Exception e) {
	    logger.fatal("Error storing sense vote: " + e);
	    return false;
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

	return true;
    }

    public static boolean recordMorphVote(FormInstance instance, String lemma,
	    String morphCode, String ipAddress) {

	Connection con = null;
	SQLHandler sqlHandler = null;

	try {
	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);
	    Matcher matcher = LEMMA_PATTERN.matcher(lemma);

	    int sequenceNumber = 1;
	    if (matcher.matches()) {
		lemma = matcher.group(1);
		sequenceNumber = Integer.parseInt(matcher.group(2));
	    }

	    String[] parameters = {instance.getDocument(),
		instance.getSubquery(), StringUtil.sqlEscape(instance.getForm()),
		Integer.toString(instance.getOccurrence()),
		Integer.toString(LanguageCode.getLanguageID(instance.getLanguageCode())), 
		lemma,
		Integer.toString(sequenceNumber), morphCode};

	    // We want to give SQL an unquoted NULL instead of a quoted "null"
	    String insertMorphStmt = "REPLACE INTO morph_votes VALUES ('"
		+ StringUtil.join(parameters, "', '") + "', "
		+ "NULL, '" + ipAddress + "')";

	    sqlHandler.executeUpdate(insertMorphStmt);

	} catch (Exception e) {
	    logger.fatal("Error storing morph vote: " + e);
	    return false;
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

	return true;
    }

    /**
     * A lemma vote is like a morph-vote, but it records only the lemma and
     * leaves the morph_code field as null.
     * 
     * @deprecated lemma votes are not used
     */
    public static boolean recordLemmaVote(FormInstance instance, String lemma,
	    String ipAddress) {

	Connection con = null;
	SQLHandler sqlHandler = null;

	try {
	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);
	    Matcher matcher = LEMMA_PATTERN.matcher(lemma);

	    int sequenceNumber = 1;
	    if (matcher.matches()) {
		lemma = matcher.group(1);
		sequenceNumber = Integer.parseInt(matcher.group(2));
	    }

	    StringBuffer sql = new StringBuffer();

	    sql.append("REPLACE INTO morph_votes VALUES ('")
		.append(instance.getDocument()).append("','")
		.append(instance.getSubquery()).append("','")
		.append(StringUtil.sqlEscape(instance.getForm())).append("',")
		.append(instance.getOccurrence()).append(",")
		.append(LanguageCode.getLanguageID(instance.getLanguageCode()))
		    .append(",'")
		.append(lemma).append("',")
		.append(sequenceNumber).append(",")
		.append("NULL,") // morphCode would go here
		.append("NULL,'") // timestamp goes here (defaults to current time)
		.append(ipAddress)
		.append("')");

	    logger.info(sql.toString());

	    sqlHandler.executeUpdate(sql.toString());
	} catch (Exception e) {
	    logger.fatal("Error storing morph vote: " + e);
	    return false;
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

	return true;
    }

    public static int getTotalLemmaVotes(String lexiconID, String lemma, String identity) {
	Connection con = null;
	SQLHandler sqlHandler = null;

	int voteCount = 0;

	try {
	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);

	    if (!lemma.startsWith("entry=")) {
		lemma = "entry=" + lemma;
	    }

	    logger.debug("lexiconID: " + lexiconID);
	    logger.debug("lemma: " + lemma);

	    String getSenseCountStmt = "SELECT COUNT(*) AS total FROM sense_votes"
		+ " WHERE lexicon_id = '" + lexiconID + "' AND lemma = '" + StringUtil.sqlEscape(lemma)
		+ "' AND identity = '" + identity + "'";

	    ResultSet rs = sqlHandler.executeQuery(getSenseCountStmt);

	    while (rs.next()) {
		voteCount = rs.getInt("total");
	    }
	} catch (Exception e) {
	    logger.fatal("Problem getting vote counts");
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
	return voteCount;
    }

    public static Map<Sense, Integer> getSenseVoteCounts(
	    String lexQuery, FormInstance instance) {

	Map<Sense, Integer> voteCounts = new LinkedHashMap<Sense, Integer>();

	Connection con = null;
	SQLHandler sqlHandler = null;

	try {
	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);
	    int lemmaStart = lexQuery.indexOf("entry=");
	    String lexiconID = lexQuery.substring(0, lemmaStart-1);
	    String lemma = lexQuery.substring(lemmaStart);

	    logger.debug("lexiconID: " + lexiconID);
	    logger.debug("lemma: " + lemma);

	    String getSenseCountsStmt = "SELECT s.sense_id, s.sense, s.level,"
		+ " SUM(s.sense_id=sv.sense_id) AS num_votes, s.short_definition"
		+ " FROM senses AS s LEFT JOIN sense_votes AS sv ON"
		+ " s.document_id = sv.lexicon_id AND s.entry_id = sv.entry_id"
		+ " WHERE sv.lexicon_id = '" + lexiconID + "' AND sv.lemma = '"
		+ lemma + "' AND sv.document_id = '" + instance.getDocument()
		+ "' AND sv.subquery = '" + instance.getSubquery() + "' AND"
		+ " sv.form = '" + StringUtil.sqlEscape(instance.getForm()) + "' AND sv.occurrence = '"
		+ instance.getOccurrence() + "' GROUP BY s.sense_id ASC";

	    logger.info("Executing " + getSenseCountsStmt);

	    ResultSet rs = sqlHandler.executeQuery(getSenseCountsStmt);

	    while (rs.next()) {
		int senseID = rs.getInt(1);
		String sense = rs.getString(2);
		int level = rs.getInt(3);
		int numVotes = rs.getInt(4);
		String shortDef = rs.getString(5);

		// We don't care about the entry ID, so just set it to -1
		Sense thisSense = new Sense(lemma, lexiconID, sense, -1,
			senseID, level, shortDef);
		voteCounts.put(thisSense, new Integer(numVotes));
	    }
	} catch (Exception e) {
	    logger.fatal("Problem getting vote counts");
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
	return voteCounts;
    }

    public static Map<Parse, Integer> getMorphVoteCounts(FormInstance instance) {

	Map<Parse, Integer> voteCounts = new HashMap<Parse, Integer>();
	String languageCode = null;

	Connection con = null;
	SQLHandler sqlHandler = null;

	try {
	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);
	    languageCode = instance.getLanguageCode();

	    // FIXME! This is unwieldy. We should probably configure
	    // morph_votes to use numeric lemma IDs instead of the lemmas
	    // themselves.
	    String getMorphCountsStmt =
		"SELECT mv.lemma, mv.sequence_number, mv.morph_code, COUNT(*) FROM"
		+ " morph_votes mv WHERE mv.lang_id = '"
		+ LanguageCode.getLanguageID(languageCode) + "' AND"
		+ " mv.morph_code IS NOT NULL AND"
		+ " document_id = '" + instance.getDocument() + "' AND"
		+ " subquery = '" + instance.getSubquery() + "' AND mv.form = '"
		+ StringUtil.sqlEscape(instance.getForm())
		+ "' AND occurrence = " + instance.getOccurrence()
		+ " GROUP BY mv.lemma, mv.sequence_number, mv.morph_code";

	    ResultSet rs = sqlHandler.executeQuery(getMorphCountsStmt);

	    ParseDAO parseDAO = new HibernateParseDAO();
	    List<Parse> parses =
		parseDAO.getByForm(instance.getForm(), languageCode);

	    while (rs.next()) {
		String lemma = rs.getString(1);
		int sequenceNumber = rs.getInt(2);
		String morphCode = rs.getString(3);
		int numVotes = rs.getInt(4);

		// Out of all the parses we know of for this form, find one
		// that corresponds with the fields we just found in the
		// database.

		for (Parse parse : parses) {
		    if (parse.getMorphCode().equals(morphCode) &&
			parse.getLemma().getHeadword().equals(lemma) &&
			parse.getLemma().getSequenceNumber() == sequenceNumber) {
			voteCounts.put(parse, numVotes);
			break;
		    }
		}
	    }

	} catch (Exception e) {
	    logger.fatal("Problem getting vote counts");
	    logger.info("languageCode was " + languageCode);
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
	return voteCounts;
    }

    /**
     * @deprecated lemma votes not used
     * @param instance
     * @return
     */
    public static Map<Parse, Integer> getLemmaVoteCounts(FormInstance instance) {

	Map<Parse, Integer> voteCounts = new LinkedHashMap<Parse, Integer>();
	String languageCode = null;

	Connection con = null;
	SQLHandler sqlHandler = null;

	try {
	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);

	    languageCode = instance.getLanguageCode();

	    String getMorphCountsStmt =
		"SELECT lemma, sequence_number, COUNT(*) FROM"
		+ " morph_votes WHERE morph_code IS NULL AND lang_id = '"
		+ LanguageCode.getLanguageID(languageCode) + "' AND"
		+ " document_id = '" + instance.getDocument() + "' AND"
		+ " subquery = '" + instance.getSubquery() + "' AND form = '"
		+ StringUtil.sqlEscape(instance.getForm())
		+ "' AND occurrence = "
		+ instance.getOccurrence()
		+ " GROUP BY lemma, sequence_number";

	    ResultSet rs = sqlHandler.executeQuery(getMorphCountsStmt);
	    ParseDAO parseDAO = new HibernateParseDAO();

	    while (rs.next()) {
		//String lemma = rs.getString(1);
		//int sequenceNumber = rs.getInt(2);
		int numVotes = rs.getInt(3);

		List<Parse> parses =
		    parseDAO.getByForm(instance.getForm(), languageCode);

		/*
		Parse parse = new Parse(languageCode, instance.getForm());
		parse.setLemma(lemma + sequenceNumber);
		*/

		if (!parses.isEmpty()) {
		    voteCounts.put(parses.get(0), numVotes);
		}
	    }

	} catch (Exception e) {
	    logger.fatal("Problem getting vote counts");
	    logger.info("languageCode was " + languageCode);
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
	return voteCounts;
    }

    /**
     * Returns the number of senses for the given entry in the given lexicon.
     * At present this is mainly used for the "help us make this tool more
     * useful" blurb above the dictionary definition; if the entry has only
     * one sense, or the senses table isn't working, we don't display the
     * message.
     *
     * @return the number of senses that exist for the given entry, or -1 if
     *	an error occurred
     */
    public static int getNumSenses(String lexiconID, String entry) {

	int numSenses = -1;

	Connection con = null;
	SQLHandler sqlHandler = null;

	try {
	    con = SQLHandler.getWritableConnection();
	    sqlHandler = new SQLHandler(con);

	    String getNumSensesStmt = "SELECT COUNT(*) FROM senses WHERE"
		+ " document_id = '" + lexiconID + "' AND lemma = '" +
		entry + "'";

	    ResultSet rs = sqlHandler.executeQuery(getNumSensesStmt);

	    while (rs.next()) {
		numSenses = rs.getInt(1);
	    }
	} catch (SQLException sqle) {
	    logger.warn("Problem getting number of senses" + " for entry " + entry + ": " + sqle);
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
	return numSenses;
    }
}
