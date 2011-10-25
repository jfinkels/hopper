package perseus.visualizations.dao;

import java.util.List;

import perseus.language.Language;
import perseus.util.HibernateDAO;
import perseus.visualizations.YearWordCount;

public class HibernateYearWordCountDAO extends HibernateDAO<YearWordCount> implements YearWordCountDAO {

	public void clear() {
		getSession().createQuery("delete from YearWordCount").executeUpdate();
	}

	public List<YearWordCount> getByLanguage(Language lang) {
		return getSession().createQuery("from YearWordCount where lang = :lang order by year").setParameter("lang", lang).list();
	}

}
