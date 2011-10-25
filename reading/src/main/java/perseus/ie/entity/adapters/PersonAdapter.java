package perseus.ie.entity.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import perseus.ie.entity.HibernateEntityManager;
import perseus.util.StringUtil;

/**
 * Adapter for searching for Person entities.
 */
public class PersonAdapter extends AbstractEntityAdapter {

	public int getMatchingEntityCount(String keyword, Map<String, String> options,
			Session session) {
		String conditions = getConditions(keyword, options);

		String queryString = String.format("select count(distinct p.id) from Person p join p.names pn where %s",
				conditions);

		Query query = session.createQuery(queryString);

		setParameters(keyword, options, conditions, query);
		
		return ((Number) query.uniqueResult()).intValue();
	}

	public Query getMatchingEntityQuery(String keyword, Map<String, String> options,
			String sortMethod, String sortOrder, Session session) {
		// OPTIONS:
		//  first = ["", yes]
		//  last = ["", yes]
		//  full = ["", yes]
		String conditions = getConditions(keyword, options);

		String orderBy;
		if (sortMethod.equals(HibernateEntityManager.AUTHORITY_NAME)) {
			orderBy = "authorityName";
		} else if (sortMethod.equals(HibernateEntityManager.SORTABLE_STRING)) {
			orderBy = "sortableString";
		} else { //if (sortMethod.equals(HibernateEntityManager.FREQUENCY)) {
			orderBy = "maxOccurrenceCount";
		}

		String queryString =
			String.format("select distinct p from Person p join p.names pn where %s order by p.%s %s",
					conditions, orderBy, sortOrder);

		Query query = session.createQuery(queryString);

		setParameters(keyword, options, conditions, query);

		return query;
	}

	/**
	 * @param keyword
	 * @param options
	 * @param conditions
	 * @param query
	 */
	private void setParameters(String keyword, Map<String, String> options,
			String conditions, Query query) {
		if (options.containsKey("first")) {
			query.setParameter("first", keyword);
		}
		if (options.containsKey("last")) {
			query.setParameter("last", keyword);
		}
		if (options.containsKey("full")) {
			query.setParameter("displayName", '%' + keyword + '%');
		}

		// This is for the EntitySearcher class
		if (options.containsKey("searcher")) {
			query.setParameter("name", keyword);
		}

		if (options.isEmpty()) {
			query.setParameter("displayName", '%' + keyword + '%');
		}
	}

	public boolean supportsMatchingEntityQuery() { return true; }

	private String getConditions(String keyword, Map<String,String> options) {
		List<String> conditions = new ArrayList<String>();

		String firstName = keyword;
		if (options.containsKey("first")) {
			firstName = options.get("first");
			conditions.add("(pn.nameType = 'forename' and " +
					((firstName.length() > 1) ? "pn.name" : "pn.initial")
					+ " = :first)");
		}
		
		String lastName = keyword;
		if (options.containsKey("last")) {
			lastName = options.get("last");
			conditions.add("(pn.nameType = 'surname' and " +
					((lastName.length() > 1) ? "pn.name" : "pn.initial")
					+ " = :last)");
		}
		if (options.containsKey("full")) {
			conditions.add("p.displayName like :displayName");
		}

		if (options.containsKey("searcher")) {
			conditions.add("pn.name = :name");
		}

		if (conditions.isEmpty()) {
			conditions.add("p.displayName like :displayName");
		}

		return StringUtil.join(conditions, " or ");
	}
}
