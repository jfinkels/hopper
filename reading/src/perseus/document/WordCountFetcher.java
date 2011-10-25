package perseus.document;

import perseus.util.SQLHandler;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

/*
 * This used to be used for fetching word counts from the 'wcount' table.
 *
 * @deprecated use the WordCountDAO instead.
 */

public class WordCountFetcher {

    private static Logger logger = Logger.getLogger(WordCountFetcher.class);
    static Map documentCounts;

    String documentID = null;
    Map wordCountsByLanguage = null;
    int total = 0;
    int max = 0;

    static {
	init();
    }

    private static void init() {
	documentCounts = new HashMap();

        ResultSet rs = null;
        Connection con = null;
        SQLHandler sqlHandler = null;

        try {
            con = SQLHandler.getConnection();

            String sql = "select * from wcount";

            sqlHandler = new SQLHandler(con);
            rs = sqlHandler.executeQuery(sql);

            while (rs.next()) {
                String documentID = rs.getString("id");
                String languageCode = rs.getString("lang");
		int words = rs.getInt("words");
		WordCountFetcher documentWordCount =
		    getDocumentWordCount(documentID);
		documentWordCount.setCount(languageCode, words);
            }

        } catch (SQLException e) {
	    logger.fatal("Problem initializing word counts", e);
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

    }

    public static WordCountFetcher getDocumentWordCount(String documentID) {   
	if (! documentCounts.containsKey(documentID)) {
	    documentCounts.put(documentID, new WordCountFetcher());
	}
	
	return (WordCountFetcher) documentCounts.get(documentID);
    }

    public WordCountFetcher () {
	wordCountsByLanguage = new HashMap();
    }

    private void setCount (String languageCode, int words) {
	wordCountsByLanguage.put(languageCode, new Integer(words));
	total += words;
	if (words > max) {
	    max = words;
	}
    }

    /** Return the total words in all languages in this document or collection */
    public int getTotal() {
	return total;
    }

    /** Return the number of words in the language with the largest word count.
	This is useful for formatting histograms */
    public int getMax() {
	return max;
    }

    /** Get the word count for a particular language */
    public int getCount(String languageCode) {
	if (! wordCountsByLanguage.containsKey(languageCode)) {
	    return 0;
	}
	
	Integer count = (Integer) wordCountsByLanguage.get(languageCode);
	return count.intValue();
    }

    public Set getLanguages() {
	return wordCountsByLanguage.keySet();
    }
    
    /**
     * This is designed to be used with the Map returned by 
     * getCollectionWordCounts(String[]). Call .values() on the
     * map to get a Collection of WordCountFetcher objects
     */
    public static int getTotalWords(Collection documentCounts) {
	int grandTotal = 0;

	Iterator iterator = documentCounts.iterator();
	while (iterator.hasNext()) {
	    WordCountFetcher wordCount = 
		(WordCountFetcher) iterator.next();
	    grandTotal += wordCount.getTotal();
	}

	return grandTotal;
    }

    /**
     * This is similar to getTotalWords, but it returns the count of the 
     * single largest collection.
     */
    public static int getMaxWords(Collection documentCounts) {
	int max = 0;

	Iterator iterator = documentCounts.iterator();
	while (iterator.hasNext()) {
	    WordCountFetcher wordCount = 
		(WordCountFetcher) iterator.next();
	    if (wordCount.getTotal() > max) {
		max = wordCount.getTotal();
	    }
	}

	return max;
    }
    
    /**
     * Pass an array of strings representing document IDs.
     * You get a Map of documentID strings -> WordCountFetcher objects
     */
    public static Map getCollectionWordCounts(String[] collections) {
	Map results = new HashMap();
	
	for (int i=0;i<collections.length;i++) {
	    results.put(collections[i], getDocumentWordCount(collections[i]));
	}

	return results;
    }

}
