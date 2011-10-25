/**
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package perseus.morph.dao;

import static org.hibernate.criterion.MatchMode.ANYWHERE;
import static org.hibernate.criterion.MatchMode.END;
import static org.hibernate.criterion.MatchMode.EXACT;
import static org.hibernate.criterion.MatchMode.START;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;

import perseus.language.Language;
import perseus.language.LanguageAdapter;
import perseus.morph.Lemma;
import perseus.util.HibernateDAO;
import perseus.util.HibernateUtil;

/**
 * The HibernateLemmaDAO retrieves all Lemmas in the 'morphology' data store accessed via 
 *  Hibernate
 */
public class HibernateLemmaDAO extends HibernateDAO<Lemma> implements LemmaDAO {
	private static Logger logger = Logger.getLogger(HibernateLemmaDAO.class);

	public HibernateLemmaDAO() {}
	private Session session;

	private static Pattern LEMMA_SPLITTER_PATTERN =
		Pattern.compile("^(\\D+)(\\d+)$");

	/**
	 * Indicate the start of a unit of work(184)
	 */
	public void beginTransaction() {
		this.session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();
	}

	/**
	 * Indicate the end of a unit of work(184)
	 */
	public void endTransaction() {
		HibernateUtil.commitTransaction();
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
		//HibernateUtil.closeSession();
	}

	/**
	 * Save a Lemma to the underlying data source.
	 * @param lemma a Lemma with the desired changes
	 * @return true if the update is successful, false otherwise
	 */
	public int insertLemma(Lemma lemma) {
		int result = -1;
		if (lemma.getHeadword() == null) {
			throw new IllegalArgumentException("No headword for " + lemma);
		}
		try {
			this.session.save(lemma);
		} catch (org.hibernate.exception.SQLGrammarException sge) {
			logger.error(sge);
			logger.error(sge.getSQL());
			logger.error(sge.getSQLException());
		} catch (Exception e) {
			logger.error("Error: HibernateLemmaDAO.insertLemma: " + e);
			this.session.flush();
		}
		Integer id = lemma.getId();
		if (id == null) {
			result = -1;
		} else {
			result = id.intValue();
		}
		return result;
	}

	/**
	 * Retrieve persisted Lemmas using Lemma as a Query Object(316)
	 * @param lemma the Query Object Lemma
	 * @return a List of all Lemmas similar to lemma
	 */
	public List findLemma(Lemma lemma) {
		Example example;

		if (lemma.getLanguage().getAdapter().matchCase()) {
			example = Example.create(lemma);
		} else {
			example = Example.create(lemma).ignoreCase();
		}

		List results = new ArrayList();
		try {
			results = this.session.createCriteria(lemma.getClass())
			.add(example).list();
		} catch (SQLGrammarException sge) {
			logger.error("findLemma: " + sge);
			logger.error(sge.getSQLException());
			this.session.flush();
		} catch (Exception e) {
			logger.error("findLemma: " + e);
			this.session.flush();
		}
		return results;
	}

	public Lemma getLemmaById(int id) {
		return (Lemma) session.load(Lemma.class, new Integer(id));
	}

	/**
	 * Retrieve persisted Lemmas using Lemma as a Query Object(316)
	 * @param lemma the Query Object Lemma
	 * @param offset where to start grabbing Lemmas from the list of matches
	 * @param maxResults how many Lemmas to grab
	 * @return a List of all Lemmas similar to lemma
	 */
	public List findLemma(Lemma lemma, int offset, int maxResults) {
		Example example;

		if (lemma.getLanguage().getAdapter().matchCase()) {
			example = Example.create(lemma);
		} else {
			example = Example.create(lemma).ignoreCase();
		}

		List results = new ArrayList();
		try {
			results = this.session.createCriteria(lemma.getClass())
			.add(example).setFirstResult(offset).setMaxResults(maxResults).list();
		} catch (Exception e) {
			logger.error("findArtifact: " + e);
			this.session.flush();
		}
		return results;
	}

	/**
	 * Retrieve persisted Lemmas using Lemma as a Query Object(316)
	 * @param lemma the Query Object Lemma
	 * @param offset where to start grabbing Lemmas from the list of matches
	 * @param maxResults how many Lemmas to grab
	 * @param sortKey the fieldName by which to sort the list of matches
	 * @param ascDesc sort the list ascending ('asc') or descending ('desc')
	 * @return a List of all Lemmas similar to lemma
	 */
	public List findLemma(Lemma lemma, int offset, int maxResults, String sortKey, String ascDesc) {
		Example example;

		if (lemma.getLanguage().getAdapter().matchCase()) {
			example = Example.create(lemma);
		} else {
			example = Example.create(lemma).ignoreCase();
		}

		List results = new ArrayList();
		try {
			if (ascDesc.equals("asc")) {
				results = this.session.createCriteria(lemma.getClass())
				.add(example).setFirstResult(offset).setMaxResults(maxResults)
				.addOrder(Order.asc(sortKey)).list();
			} else if (ascDesc.equals("desc")) {
				results = this.session.createCriteria(lemma.getClass())
				.add(example).setFirstResult(offset).setMaxResults(maxResults)
				.addOrder(Order.desc(sortKey)).list();
			} else {
				logger.error("Error, invalid value for ascDesc: " + ascDesc + ". Valid values are 'asc' or 'desc'.");
			}
		} catch (Exception e) {
			logger.error("findLemma: " + e);
			this.session.flush();
		}
		return results;
	}

	/**
	 * Save a persisted object to the underlying data source
	 * @param lemma a persisted Lemma that has 0 or more modifications to be saved
	 * @return true if the update is successful, false otherwise
	 */
	public boolean updateLemma(Lemma lemma) {
		boolean result = true;
		// added check for getId() to ensure no false positives due to case

		if (lemma.getHeadword() == null || lemma.getId() == null) {
			result = false;
			throw new IllegalArgumentException("No headword or ID for " + lemma);
		}
		try {
			this.session.update(lemma);
		} catch (Exception e) {
			logger.error("updateLemma: " + e);
			this.session.flush();
		}
		return result;
	}

	/**
	 * Remove a Lemma from the underlying data source.
	 * @param lemma a persisted Lemma
	 * @return true if the delete was a success, false otherwise
	 */
	public boolean deleteLemma(Lemma lemma) {
		boolean result = true;

		// added check for getId() to ensure no false positives due to case

		if (lemma.getHeadword() == null || lemma.getId() == null) {
			logger.error("deletLemma: NULL headword or ID");
			result = false;
		}
		try {
			this.session.delete(lemma);
		} catch (Exception e) {
			logger.error("deleteLemma: " + e);
			this.session.flush();
		}
		return result;
	}

	public Lemma getMatchingLemma(String lookupForm, String languageCode, boolean ignoreAccents) {
		List<Lemma> results = getMatchingLemmas(lookupForm, languageCode, ignoreAccents);
		return (results.isEmpty() ? null : results.get(0));
	}

	public List<Lemma> getMatchingLemmas(String headword,
			String languageCode, boolean ignoreAccents) {

		String actualHeadword = headword;
		int sequenceNumber = -1;

		Matcher matcher = LEMMA_SPLITTER_PATTERN.matcher(headword);
		if (matcher.matches()) {
			actualHeadword = matcher.group(1);
			sequenceNumber = Integer.parseInt(matcher.group(2));
		}

		return getMatchingLemmas(actualHeadword, sequenceNumber, languageCode, ignoreAccents);
	}

	public List<Lemma> getMatchingLemmas(String headword, int sequenceNumber,
			String languageCode, boolean ignoreAccents) {
		return getMatchingLemmas(headword, sequenceNumber, languageCode, EXACT, ignoreAccents);
	}

	public List<Lemma> getLemmasStartingWith(String headword, String languageCode, boolean ignoreAccents) {
		return getMatchingLemmas(headword, -1, languageCode, START, ignoreAccents);
	}

	public List<Lemma> getLemmasEndingWith(String headword, String languageCode, boolean ignoreAccents) {
		return getMatchingLemmas(headword, -1, languageCode, END, ignoreAccents);
	}

	public List<Lemma> getLemmasWithSubstring(String headword, String languageCode, boolean ignoreAccents) {
		return getMatchingLemmas(headword, -1, languageCode, ANYWHERE, ignoreAccents);
	}

	private List<Lemma> getMatchingLemmas(String headword, int sequenceNumber,
			String languageCode, MatchMode matchMode, boolean ignoreAccents) {
		boolean matchCase=false;

		if (Language.forCode(languageCode).getAdapter().matchCase()) {
			matchCase=true;
		}		
		return getMatchingLemmas(headword, sequenceNumber, languageCode, matchMode, matchCase, ignoreAccents);
	}

	private List<Lemma> getMatchingLemmas(String headword, int sequenceNumber,
			String languageCode, MatchMode matchMode, boolean matchCase, boolean ignoreAccents) {

		Criteria crit = getSession().createCriteria(Lemma.class);

		if (headword != null) {
			crit.add((matchMode == EXACT) ?
					Restrictions.eq((ignoreAccents)?"bareHeadword":"headword", headword) :
						Restrictions.like((ignoreAccents)?"bareHeadword":"headword", headword, matchMode));
			crit.addOrder(Order.asc((ignoreAccents)?"bareHeadword":"headword"));
		}

		if (languageCode != null) {
			crit.add(Restrictions.eq("language",
					Language.forCode(languageCode)));
		}

		if (sequenceNumber != -1) {
			crit.add(Restrictions.eq("sequenceNumber", sequenceNumber));
		}

		List<Lemma> results = crit.list();

		if (matchCase && matchMode == EXACT) {
			LanguageAdapter adapter = Language.forCode(languageCode).getAdapter();
			List<Lemma> matchingLemmas = new ArrayList<Lemma>();
			for (Lemma lemma : results) {
				if (adapter.compare(lemma.getHeadword(), headword) == 0) {
					matchingLemmas.add(lemma);
				}
			}
			return matchingLemmas;
		}

		return results;    
	}

	public ScrollableResults getScrollableLemmas(String languageCode) {
		Criteria crit = getSession().createCriteria(Lemma.class);
		crit.add(Restrictions.eq("language",
				Language.forCode(languageCode)));

		return crit.scroll(ScrollMode.FORWARD_ONLY);
	}

	/**
	 * Get the number of Lemmas in the underlying data source similar to lemma
	 * @param lemma the underlying Query Object(316)
	 * @return the number of Lemmas similar to lemma
	 */
	public int getTotalHits(Lemma lemma) {
		Example example;

		if (lemma.getLanguage().getAdapter().matchCase()) {
			example = Example.create(lemma);
		} else {
			example = Example.create(lemma).ignoreCase();
		}	
		List results = new ArrayList();
		try {
			results = this.session.createCriteria(lemma.getClass())
			.add(example).list();
		} catch (Exception e) {
			logger.error("getTotalHits: " + e);
			this.session.flush();
		}
		return results.size();
	}

	/**
	 * Returns all lemmas in the specified language.
	 */
	public List<Lemma> getAllLemmas(Language language) {
		return getSession().createQuery("from Lemma l "
				+ "where l.language = :lang")
				.setParameter("lang", language)
				.list();
	}
}
