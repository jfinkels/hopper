package perseus.language.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;

import perseus.language.Language;
import perseus.util.HibernateDAO;

public class HibernateLanguageDAO extends HibernateDAO<Language> implements LanguageDAO {

    public Language getByAbbreviation(String abbrev) {
	Query query = getSession().createQuery(
		"from Language where :abbrev in elements(abbreviations)")
		.setMaxResults(1)
		.setCacheable(true)
		.setString("abbrev", abbrev);

	List<Language> results = query.list();
	return results.isEmpty() ? null : results.get(0);
    }

    public Language getById(int id) {
	try {
	    return (Language) getSession().get(
		    Language.class, new Integer(id));
	} catch (ObjectNotFoundException onfe) {
	    return null;
	}
    }
    
    public Set<Language> getAllLanguages() {
	return new HashSet<Language>(
		getSession().createCriteria(Language.class).list());
    }
    
    public void clear() {
	getSession().createQuery("delete from Language").executeUpdate();
    }
}
