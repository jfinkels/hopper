package perseus.morph.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import perseus.language.Language;
import perseus.language.LanguageAdapter;
import perseus.morph.Lemma;
import perseus.morph.Parse;
import perseus.util.HibernateDAO;

/**
 * Hibernate-based DAO for managing Parse objects.
 */
public class HibernateParseDAO extends HibernateDAO<Parse> implements ParseDAO {
	
    public void deleteByLanguage(Language language) {
	StringBuilder hql = new StringBuilder()
	    .append("delete Parse where lemma_id in ")
	    .append("(select id from Lemma where language = :lang)");

	Query query = getSession().createQuery(hql.toString())
	    .setParameter("lang", language);

	query.executeUpdate();
    }

    public List<Parse> getByForm(String form, String languageCode) {
	return getByForm(form, Language.forCode(languageCode));
    }
    
    public List<Parse> getByForm(String form, Language language) {
	return getByForm(form, language, false, false);
    }

    public List<Parse> getByForm(String form, String languageCode,
		boolean ignoreAccents, boolean matchCase) {
	return getByForm(form, Language.forCode(languageCode),
		ignoreAccents, matchCase);
    }
    
    public List<Parse> getByForm(String form, Language language,
	boolean ignoreAccents, boolean matchCase) {
	
	// force matchCase if the language requires it.
	if (language.getAdapter().matchCase()) {
		matchCase=true;
	}
	Criteria criteria = getSession().createCriteria(Parse.class);

	if (form != null) {
	    criteria.add(Restrictions.eq(
		ignoreAccents ? "bareForm" : "form" , form));
	}

	if (language != null) {
	    criteria.createCriteria("lemma")
		.add(Restrictions.eq("language", language));
	}
	
	List<Parse> results = criteria.list();
	if (matchCase) {
	    LanguageAdapter adapter = language.getAdapter();
	    List<Parse> matchingParses = new ArrayList<Parse>();
	    for (Parse parse : results) {
		if (adapter.compare(parse.getForm(), form) == 0) {
		    matchingParses.add(parse);
		}
	    }
	    return matchingParses;
	}

	return results;
    }

    public boolean exists(Parse parse) {
    	return exists(parse, false);
    }
   
	public boolean exists(Parse parse, boolean matchCase) {

	// force matchCase if the language requires it.
	if (Language.forCode(parse.getLanguageCode()).getAdapter().matchCase()) {
		matchCase=true;
	}
	
		Criteria criteria = getSession().createCriteria(Parse.class);
	
		if (parse.getForm() != null) {
			criteria.add(Restrictions.eq("form" , parse.getForm()));
		}
		
		if (parse.getMorphCode() != null) {
			criteria.add(Restrictions.eq("morphCode", parse.getMorphCode()));
		}
	
		if (parse.getLemma() != null) {
			criteria.createCriteria("lemma")
			.add(Restrictions.eq("id", parse.getLemma().getId()));
		}
		
		List<Parse> results = criteria.list();
		
		if (results.size() > 0) {
			if (matchCase) {
				LanguageAdapter adapter = Language.forCode(parse.getLanguageCode()).getAdapter();
				for (Parse result : results) {
					if (adapter.compare(result.getForm(), parse.getForm()) == 0) {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		
		return false;
    }

    public List<Parse> getByLemma(Lemma lemma) {
	if (lemma.getId() == null) {
	    throw new IllegalArgumentException("lemma has no ID!");
	}

	Criteria criteria = getSession().createCriteria(Parse.class)
	    .add(Restrictions.eq("lemma", lemma));

	return criteria.list();
    }

    public List<Lemma> getLemmasByForm(String lookupForm, Language language) {
	Query query = getSession().createQuery(
		"select p.lemma from Parse p where " +
		"p.form = :form and p.lemma.language = :language")
	    .setParameter("form", lookupForm)
	    .setParameter("language", language)
	    .setCacheable(true);
	
	// it's unlikely, but not impossible, that we could return the same
	// lemma twice (it could happen in the case of two parses with the same
	// form but differing expanded forms); don't return duplicates
	Set<Lemma> lemmaSet = new HashSet<Lemma>(query.list());
	return new ArrayList<Lemma>(lemmaSet);
    }

    public Set<String> getAllForms(Collection<String> forms, Language language) {

	Set<String> output = new HashSet<String>();
	Query query = getSession().createQuery(
		"select l.headword, l.sequenceNumber from Parse p " +
		"join p.lemma l where l.language = :language and " +
		" p.form in (:forms)")
	    .setParameter("language", language)
	    .setParameterList("forms", forms);

	List results = query.list();
	for (Object result : results) {
	    Object[] row = (Object[]) result;
	    String lemmaForm = (String) row[0];
	    int sequenceNumber = (Integer) row[1];
	    output.add(lemmaForm + (sequenceNumber > 1 ? sequenceNumber : ""));
	}

	return output;
    }

    public Set<Lemma> getAllLemmas(Collection<String> forms, Language language) {

	Set<Lemma> output = new HashSet<Lemma>();
	Query query = getSession().createQuery(
		"select p.lemma from Parse p " +
		"where l.language = :language and " +
		"p.form in (:forms)")
	    .setParameter("language", language)
	    .setParameterList("forms", forms);

	List results = query.list();
	for (Object result : results) {
	    Object[] row = (Object[]) result;
	    output.add((Lemma) row[0]);
	}

	return output;
    }
}
