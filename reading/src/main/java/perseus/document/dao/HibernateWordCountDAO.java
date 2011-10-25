package perseus.document.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import perseus.document.Corpus;
import perseus.document.WordCount;
import perseus.language.Language;
import perseus.util.HibernateDAO;
import perseus.util.HibernateUtil;
import perseus.util.StringUtil;

/**
 * Class with methods for saving and retrieving {@link WordCount} objects.
 *
 * @see WordCount
 */
public class HibernateWordCountDAO extends HibernateDAO<WordCount> implements WordCountDAO {

    public List<WordCount> getByDocument(String documentID) {
	return HibernateUtil.getSession().createQuery(
		"from WordCount where documentID = :docID")
		.setParameter("docID", documentID)
		.list();
    }

    public List<WordCount> getByLanguage(Language language) {
	return HibernateUtil.getSession().createQuery(
	"from WordCount where lang = :language")
	.setParameter("language", language)
	.list();
    }

    public long getTotalCount(String documentID) {
	return (Long) HibernateUtil.getSession().createQuery(
	"select sum(wordCount) from WordCount where documentID = :docID")
	.setParameter("docID", documentID)
	.uniqueResult();
    }

    public long getTotalCount(Language language) {
	return (Long) HibernateUtil.getSession().createQuery(
	"select sum(wordCount) from WordCount where lang = :language")
	.setParameter("language", language)
	.uniqueResult();
    }

    public long getCount(String documentID, Language language) {
	return (Long) HibernateUtil.getSession().createQuery(
	"select wordCount from WordCount where documentID = :docID and " +
	"lang = :language")
	.setParameter("docID", documentID)
	.setParameter("language", language)
	.uniqueResult();
    }

    public void deleteByDocument(String documentID) {
	HibernateUtil.getSession().createQuery(
		"delete from WordCount where documentID = :docID")
		.setParameter("docID", documentID).executeUpdate();
    }

    public long getMaxWords(String[] collections) {
	long maxWords = 0;
	long dbCount = 0;
	List wordCountList = new ArrayList();
	
	for (String collection : collections) {
	    dbCount = 0;
	    wordCountList = HibernateUtil.getSession().createQuery(
              "select wordCount from WordCount where documentID = :docID")
              .setParameter("docID", collection).list();

	    if (wordCountList.size() == 0)
		continue;

	    Iterator i = wordCountList.iterator();
	    while(i.hasNext()) {
		dbCount += (Long) i.next();
	    }
	    
	    if (dbCount > maxWords) {
		maxWords = dbCount;
	    }
	}
	return maxWords;
    }

    public long getMaxWordsForDocument(String documentID) {
	List<WordCount> documents = getByDocument(documentID);

	Iterator documentIterator = documents.iterator();

	long max = 0;
	while (documentIterator.hasNext()) {
	    WordCount wc = (WordCount) documentIterator.next();

	    if (wc.getWordCount() > max)
		max = wc.getWordCount();
	}

	return max;
    }

    public List<Language> getLanguagesForDocument(String documentID) {
	List<WordCount> wordCounts =  HibernateUtil.getSession().createQuery(
	       "from WordCount where documentID = :docID")
	       .setParameter("docID", documentID)
	       .list();

	List<Language> languages = new ArrayList();
	Iterator wc = wordCounts.iterator();
	while (wc.hasNext()) {
	    languages.add(((WordCount) wc.next()).getLang());
	}

	return languages;
    }

    /**
     * Saves aggregated WordCounts for <kbd>corpus</kbd> (aggregated by
     * document).
     */
    public void aggregateCorpus(Corpus corpus) {
	deleteByDocument(corpus.getID());
	
	String query =
		"insert into WordCount (documentID, lang, wordCount) " +
		"select '" + corpus.getID() + "', wc.lang, sum(wc.wordCount) " +
		"from WordCount wc where " + "wc.documentID in ('" +
		StringUtil.join(corpus.getDocumentIDs(), "','")
		+ "') group by wc.lang";
	getSession().createQuery(query).executeUpdate();
    }

}
