package perseus.ie.entity.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import perseus.ie.entity.HibernateEntityManager;
import perseus.util.StringUtil;

public class PlaceAdapter extends AbstractEntityAdapter {

	private static final String[] validFields = 
	{"siteName", "state", "nation", "displayName"};

	public int getMatchingEntityCount(String keyword, Map<String, String> options,
			Session session) {
		String queryString = "select count(*) " + getQueryString(keyword, options);

		Query query = session.createQuery(queryString);
		query.setParameter("name", keyword);

		return ((Number) query.uniqueResult()).intValue();
	}

	public Query getMatchingEntityQuery(String keyword, Map<String, String> options,
			String sortMethod, String sortOrder, Session session) {

		if (sortOrder == null) {
			sortOrder = HibernateEntityManager.ASCENDING;
		}
		String orderString = null;
		if (sortMethod.equals(HibernateEntityManager.FREQUENCY)) {
			orderString = "order by e.maxOccurrenceCount " + sortOrder;
		} else if (sortMethod.equals(HibernateEntityManager.SORTABLE_STRING)) {
			orderString = "order by e.sortableString " + sortOrder;
		} else {
			orderString = "order by e.authorityName " + sortOrder;
		}

		Query query = session.createQuery(getQueryString(keyword, options)
				+ " " + orderString);
		query.setParameter("name", keyword);

		return query;
	}

	private String getQueryString(String keyword, Map<String, String> options) {
		StringBuffer queryBuffer = new StringBuffer();

		queryBuffer.append("from Place as e")
		.append(" where ");

		List<String> conditions = new ArrayList<String>();

		for (int i = 0; i < validFields.length; i++) {
			if (options.containsKey(validFields[i])) {
				conditions.add("e." + validFields[i] + " = :name");
			}
		}

		// Default to searching the place-name
		if (conditions.isEmpty()) {
			conditions.add("e.siteName = :name");
		}

		queryBuffer.append(StringUtil.join(conditions, " and "));
		return queryBuffer.toString();
	}

	public boolean supportsMatchingEntityQuery() { return true; }

	/*
    public Criteria getMatchingEntityCriteria(String keyword, Map options,
	    Session session) {

	Criteria criteria = session.createCriteria(Place.class);
	if (options.containsKey("any")) {
	    criteria.add(Restrictions.disjunction()
		    .add(Restrictions.eq("state", keyword))
		    .add(Restrictions.eq("nation", keyword))
		    .add(Restrictions.eq("siteName", keyword))
		    );
	} else if (options.containsKey("state") || options.containsKey("nation")
		    || options.containsKey("siteName")) {
	    String[] validOptions = {"state", "nation", "siteName"};

	    for (int i = 0; i < validOptions.length; i++) {
		if (options.containsKey(validOptions[i])) {
		    String optionValue = options.get(validOptions[i]).toString();
		    criteria.add(Restrictions.eq(validOptions[i], optionValue));
		}
	    }
	} else {
	    criteria.add(Restrictions.eq("siteName", keyword));
	}

	return criteria;
    }

    public boolean supportsMatchingEntityCriteria() { return true; }
	 */
}
