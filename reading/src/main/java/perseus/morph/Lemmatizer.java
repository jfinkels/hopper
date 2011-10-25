package perseus.morph;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import perseus.language.Language;
import perseus.language.LanguageAdapter;
import perseus.morph.dao.HibernateParseDAO;
import perseus.morph.dao.ParseDAO;
import perseus.util.HibernateUtil;
import perseus.util.SQLHandler;
import perseus.util.StringUtil;

/**
 * Contains various helper methods to retrieve lemmas, or forms of lemmas,
 * for a given parse.
*/
public class Lemmatizer {
	
	private static final Logger logger = Logger.getLogger(Lemmatizer.class);

    private static ParseDAO parseDAO = new HibernateParseDAO();
    
    /**
     * Class constructor.
     */
    public Lemmatizer() {
    }

    /**
     * Does nothing now; here for back-compatibility.
     * 
     * @deprecated
     */
    public void cleanup() {
    }

    public static List<Lemma> getLemmas(String word, String languageCode) {
	return getLemmas(word, Language.forCode(languageCode));
    }
    
    /**
     * Returns a list of lemmas that the given form could belong to.
     * 
     * @param word the word
     * @param languageCode the language
     * @return a list of Lemma objects 
    */
    public static List<Lemma> getLemmas(String word, Language language) {
	String lookupForm = language.getAdapter().getLookupForm(word);
	return parseDAO.getLemmasByForm(lookupForm, language); 
    }

    public static Set<String> getAllForms(String word, String languageCode) {
	return getAllForms(word, Language.forCode(languageCode));
    }

    /**
     * Like getLemmas(), but returns all *forms* of lemmas that the given word
	 * could belong to instead of actual Lemma objects.
     *
     * @param word the word to grab forms for
     * @param languageCode the language of the word
     * @return a set of forms
    */
    public static Set<String> getAllForms(String word, Language language) {
	List<Lemma> lemmas = getLemmas(word, language);
	Set<String> forms = new HashSet<String>();
	
	for (Lemma lemma : lemmas) {
	    List<Parse> parses = parseDAO.getByLemma(lemma);
	    for (Parse parse : parses) {
		forms.add(parse.getForm());
		HibernateUtil.getSession().evict(parse);
	    }
	}
	
	return getAllForms(lemmas);
    }

	public static Set<String> getAllForms(Set<String> forms, Language language) {
		return parseDAO.getAllForms(forms, language);
	}

    /** 
     * This is a version of getLemmas that does not use a prepared statement.
     * Not currently used. 
     * 
     * @param word the word to get the lemmas for
     * @param languageCode the language the word is in
     * @return a list of lemmas
     * 
     * @deprecated
     */
    public static List getLemmasWithCruncher(String word, String languageCode) {
	long startTime = System.currentTimeMillis();

	LanguageAdapter languageAdapter =
	    LanguageAdapter.getLanguageAdapter(languageCode);

	word = languageAdapter.getLookupForm(word);

	List<Lemma> lemmas = new ArrayList<Lemma>();

	ResultSet rs = null;
        Connection con = null;
        SQLHandler sqlHandler = null;

        try {
            con = SQLHandler.getConnection();

	    // First try to lemmatize the search term
	    String sql = "SELECT l.* FROM word_parse wp, " +
		"language_abbrev la, lemma l " + 
		"WHERE wp.form = '" + StringUtil.sqlEscape(word) + "' " +
		"AND la.abbrev='" + StringUtil.sqlEscape(languageCode) + "' " +
		"AND la.lang_id = wp.lang_id AND wp.lemma_id = l.lemma_id";

            logger.debug(sql);

            sqlHandler = new SQLHandler(con);
            rs = sqlHandler.executeQuery(sql);

	    while (rs.next()) {
		// There is a bug such that unparsed forms get stuck into
		// word_parse with a 0 lemma id. Don't add them.

		int lemmaID = rs.getInt("lemma_id");
		if (lemmaID == 0) {
		    continue;
		}

		String lemma = rs.getString("lemma");
		int sequenceNumber = rs.getInt("sequence_number");
		String shortDefinition = rs.getString("short_def");
		Lemma l = new Lemma(lemmaID, lemma, sequenceNumber,
				    languageCode, shortDefinition);

		lemmas.add(l);
	    }
	    
	    // For infrequent forms, the search term may be the dictionary
	    // form, which may not be attested. If we have no lemmas, try
	    // searching the lemma table directly.

	    if (lemmas.size() == 0) {
		sql = "SELECT l.* FROM lemma l, " +
		    "language_abbrev la " + 
		    "WHERE l.lemma = '" + StringUtil.sqlEscape(word) + "' " +
		    "AND la.abbrev='" + StringUtil.sqlEscape(languageCode) + "' " +
		    "AND la.lang_id = l.lang_id";

		rs = sqlHandler.executeQuery(sql);

		while (rs.next()) {
		    int lemmaID = rs.getInt("lemma_id");
		    String lemma = rs.getString("lemma");
		    int sequenceNumber = rs.getInt("sequence_number");
		    String shortDefinition = rs.getString("short_def");
		    Lemma l = new Lemma(lemmaID, lemma, sequenceNumber,
					languageCode, shortDefinition);
		    
		    lemmas.add(l);
		}
	    }

	    // Resort to a cruncher call.

	    /*
	    if (lemmas.size() == 0) {
		Cruncher cruncherWrapper = new Cruncher();

		lemmas.addAll(cruncherWrapper.getLemmas(word, languageCode));
	    }
	    */

        } catch (SQLException e) {
        	logger.error("Problem retrieving all forms for [" + word + "]", e);
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
	
	return lemmas;
    }


    /**
     * Get all forms for all of the listed lemmas
     *
     * @param lemmas a list of Lemma objects
     * @return a set of all forms for the lemmas
     */
    public static Set<String> getAllForms(List<Lemma> lemmas) {

	Set<String> forms = new HashSet<String>();
	ParseDAO parseDAO = new HibernateParseDAO();

	for (Lemma lemma : lemmas) {
	    List<Parse> parses = parseDAO.getByLemma(lemma);
	    for (Parse parse : parses) {
		forms.add(parse.getForm());
	    }
	}

	return forms;
    }
}
