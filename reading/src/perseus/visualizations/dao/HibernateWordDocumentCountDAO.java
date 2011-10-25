package perseus.visualizations.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

import perseus.language.Language;
import perseus.util.HibernateDAO;
import perseus.visualizations.WordDocumentCount;

public class HibernateWordDocumentCountDAO extends HibernateDAO<WordDocumentCount> implements WordDocumentCountDAO {

	public WordDocumentCount getByWordAndYear(String word, int year) {
		WordDocumentCount wdc = (WordDocumentCount) getSession()
		.createQuery("from WordDocumentCount where word = :word and year = :year")
			.setParameter("word", word).setParameter("year", year).uniqueResult();
		
		return wdc;		
	}

	public void clear() {
		getSession().createQuery("delete from WordDocumentCount").executeUpdate();
	}

	public List<Language> getDistinctLanguages() {
		return getSession().createQuery("select distinct lang from WordDocumentCount").list();
	}

	public ScrollableResults getYearCounts(Language l) {
		 return getSession().createQuery("select year, count(word) from WordDocumentCount where lang = :lang group by year")
		.setParameter("lang", l).scroll(ScrollMode.SCROLL_SENSITIVE);
		 
	}

	public List<String> getByYearAndLanguage(int year, Language lang) {
		List<String> results = getSession().createQuery("select word from WordDocumentCount where year = :year " +
				"and lang = :lang")
		.setParameter("year", year).setParameter("lang", lang).list();
		if (results != null) {
			return results;
		} else {
			return new ArrayList<String>();
		}
	}

	public List<String> getWordsByLanguage(Language lang) {
		return getSession().createQuery("select distinct word from WordDocumentCount where lang = :lang")
		.setParameter("lang", lang).list();
	}

	public List<WordDocumentCount> getByWordAndLanguage(String word, Language lang) {
		return getSession().createQuery("from WordDocumentCount where word = :word and lang = :lang order by year")
		.setParameter("word", word).setParameter("lang", lang).list();
	}

}
