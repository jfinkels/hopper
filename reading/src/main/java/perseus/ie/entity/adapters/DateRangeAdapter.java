package perseus.ie.entity.adapters;

import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import perseus.ie.entity.DateRange;
import perseus.ie.entity.HibernateEntityManager;

public class DateRangeAdapter extends AbstractEntityAdapter {
	private static final Logger logger = Logger.getLogger(DateRangeAdapter.class);

	private static final String[] DATE_FEATURES =
	{"year", "month", "day", "hour", "minute", "second", "secondFraction"};

	public Criteria getMatchingEntityCriteria(String keyword, Map<String, String> options,
			String sortMethod, String sortOrder, Session session) {
		Criteria criteria = session.createCriteria(DateRange.class)
		.createAlias("startDate", "start")
		.createAlias("endDate", "end");
		addRestrictions(criteria, "start", "start", options);
		addRestrictions(criteria, "end", "end", options);

		boolean sortAscending = false;
		String sortField = "authorityName";
		if (sortOrder == null ||
				sortOrder.equals(HibernateEntityManager.ASCENDING)) {
			sortAscending = true;
		}
		if (sortMethod == null ||
				sortMethod.equals(HibernateEntityManager.FREQUENCY)) {
			sortField = "maxOccurrenceCount";
		} else if (sortMethod.equals(HibernateEntityManager.SORTABLE_STRING)) {
			sortField = "sortableString";
		}
		criteria.addOrder((sortAscending ?
				Order.asc(sortField) : Order.desc(sortField)));

		return criteria;
	}

	public boolean supportsMatchingEntityCriteria() { return true; }

	private void addRestrictions(Criteria criteria, String paramPrefix,
			String propertyPrefix, Map options) {
		for (int i = 0; i < DATE_FEATURES.length; i++) {
			String paramName = paramPrefix + "." + DATE_FEATURES[i];

			if (options.containsKey(paramName)) {
				logger.debug("Adding restrictions.eq: " +
						paramName + " = " + options.get(paramName));
				criteria.add(Restrictions.eq(paramName, options.get(paramName)));
			}
		}
	}
}
