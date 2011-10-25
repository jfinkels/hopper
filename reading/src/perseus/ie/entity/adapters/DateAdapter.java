package perseus.ie.entity.adapters;

import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import perseus.ie.entity.Date;
import perseus.ie.entity.HibernateEntityManager;

/**
 * EntityAdapter for looking up dates.
 */
public class DateAdapter extends AbstractEntityAdapter {
	private static final Logger logger = Logger.getLogger(DateAdapter.class);

	private static final String[] dateFeatures =
	{"year", "month", "day", "hour", "minute", "second"};

	public Criteria getMatchingEntityCriteria(String keyword, Map<String, String> options,
			String sortMethod, String sortOrder, Session session) {

		if (options.containsKey("endyear")
				|| options.containsKey("endmonth")
				|| options.containsKey("endday")) {
			Date startDate = new Date();
			if (options.containsKey("year")) {
				int year = Integer.parseInt(options.get("year"));
				if (options.containsKey("era")) {
					if (options.get("era").equalsIgnoreCase("BC")) {
						year = -year;
					}
				}
				startDate.setYear(year);
			}
			if (options.containsKey("month")) {
				startDate.setMonth(Integer.parseInt(options.get("month")));
			}
			if (options.containsKey("day")) {
				startDate.setDay(Integer.parseInt(options.get("day")));
			}
			if (!startDate.hasMonth() && startDate.hasYear()) startDate.setMonth(1);
			if (!startDate.hasDay()) startDate.setDay(1);

			Date endDate = new Date();
			if (options.containsKey("endyear")) {
				int endyear = Integer.parseInt(options.get("endyear"));
				if (options.containsKey("endera")) {
					if (options.get("endera").equalsIgnoreCase("BC")) {
						endyear = -endyear;
					}
				}
				endDate.setYear(endyear);
			}
			if (options.containsKey("endmonth")) {
				endDate.setMonth(Integer.parseInt(options.get("endmonth")));
			}
			if (options.containsKey("endday")) {
				endDate.setDay(Integer.parseInt(options.get("endday")));
			}
			if (!endDate.hasMonth() && endDate.hasYear()) { endDate.setMonth(12); }
			if (!endDate.hasDay()) { endDate.setDay(31); }

			return getRangeCriteria(startDate, endDate, session);
		}

		Criteria criteria = session.createCriteria(Date.class);
		for (String feature : dateFeatures) {
			if (options.containsKey(feature)) {
				try {
					Number featureValue = Integer.valueOf(options.get(feature));
					if (feature.equals("year")) {
						if (options.containsKey("era")) {
							if (options.get("era").equalsIgnoreCase("BC")) {
								featureValue = -Integer.valueOf(options.get(feature));							
							}
						}
					}
					criteria.add(Restrictions.eq(feature, featureValue));
				} catch (NumberFormatException nfe) {
					logger.error("Problem parsing feature " + feature + ": " + options.get(feature));
				}
			}
		}

		if (options.containsKey("secondFraction")) {
			try {
				Number featureValue = Double.valueOf(options.get("secondFraction"));
				criteria.add(Restrictions.eq("secondFraction", featureValue));
			} catch (NumberFormatException nfe) {
				logger.error("Problem parsing second fraction: " +
						options.get("secondFraction"));
			}
		}

		if (options.containsKey("searcher")) {
			if (keyword == null) {
				throw new IllegalArgumentException(
				"Attempted to search with a null keyword");
			}

			Junction junction = Restrictions.disjunction()
			.add(Restrictions.eq("displayName", keyword))
			.add(Restrictions.eq("authorityName", keyword));
			try {
				// If this is a numerical keyword, check some more fields
				Number keywordValue = Integer.valueOf(keyword);
				junction.add(Restrictions.eq("year", keywordValue));
				junction.add(Restrictions.eq("month", keywordValue));
				junction.add(Restrictions.eq("day", keywordValue));
			} catch (NumberFormatException nfe) {
				// okay, don't bother
			}

			criteria.add(junction);
		}

		boolean sortAscending = false;
		String sortField = "authorityName";
		if (sortOrder == null || sortOrder.equals(HibernateEntityManager.ASCENDING)) {
			sortAscending = true;
		}
		if (sortMethod == null || sortMethod.equals(HibernateEntityManager.FREQUENCY)) {
			sortField = "maxOccurrenceCount";
		} else if (sortMethod.equals(HibernateEntityManager.SORTABLE_STRING)) {
			sortField = "sortableString";
		}
		criteria.addOrder((sortAscending ? Order.asc(sortField) : Order.desc(sortField)));

		return criteria;
	}

	public Criteria getRangeCriteria(Date start, Date end, Session session) {
		Criteria criteria = session.createCriteria(Date.class);

		Junction mainJunction = null;

		if (start.hasYear() && end.hasYear()) {
			int startYear = start.getYear();
			int startMonth = start.getMonth();
			int startDay = start.getDay();

			int endYear = end.getYear();
			int endMonth = end.getMonth();
			int endDay = end.getDay();

			if (startYear == endYear) {
				mainJunction = Restrictions.conjunction()
				.add(Restrictions.eq("year", new Integer(startYear)));
				if (start.hasMonth() && end.hasMonth()) {
					if (startMonth == endMonth) {
						mainJunction.add(Restrictions.eq("month", new Integer(startMonth)));
						if (start.hasDay() && end.hasDay()) {
							if (startDay == endDay) {
								mainJunction.add(Restrictions.eq("day",	new Integer(startDay)));
							} else {
								mainJunction.add(Restrictions.between("day", new Integer(startDay), new Integer(endDay)));
							}
						}
					} else {
						mainJunction.add(getMonthCriterion(startMonth, endMonth, startDay, endDay));
					}
				}

			} else {
				mainJunction = Restrictions.disjunction();

				Junction startConjunction = Restrictions.conjunction()
				.add(Restrictions.eq("year", new Integer(startYear)))
				.add(getMonthCriterion(startMonth, -1, startDay, -1));
				Criterion midCriterion = Restrictions.between("year", new Integer(startYear+1), new Integer(endYear-1));
				Junction endConjunction = Restrictions.conjunction()
				.add(Restrictions.eq("year", new Integer(endYear)))
				.add(getMonthCriterion(-1, endMonth, -1, endDay));

				mainJunction.add(startConjunction)
				.add(midCriterion)
				.add(endConjunction);
			}
		} else if (start.hasMonth() && end.hasMonth()) {
			int startMonth = start.getMonth();
			int startDay = start.getDay();
			int endMonth = end.getMonth();
			int endDay = end.getDay();

			mainJunction = Restrictions.conjunction()
			.add(getMonthCriterion(startMonth, endMonth, startDay, endDay));
		} else {
			int startDay = start.getDay();
			int endDay = end.getDay();
			mainJunction = Restrictions.conjunction()
			.add(Restrictions.between("day", new Integer(startDay),
					new Integer(endDay)));
		}

		criteria.add(mainJunction);

		criteria.addOrder(Order.asc("year"))
		.addOrder(Order.asc("month"))
		.addOrder(Order.asc("day"));
		return criteria;
	}

	private Criterion getMonthCriterion(int startMonth, int endMonth,
			int startDay, int endDay) {
		Junction monthDisjunction = Restrictions.disjunction();

		if (startMonth == endMonth) {
			monthDisjunction.add(Restrictions.conjunction()
					.add(Restrictions.between("day", new Integer(startDay), new Integer(endDay)))
					.add(Restrictions.eq("month", new Integer(startMonth))));
		} else {
			if (startMonth == -1) {
				monthDisjunction
				.add(Restrictions.lt("month", new Integer(endMonth)))
				.add(Restrictions.conjunction()
						.add(Restrictions.eq("month", new Integer(endMonth)))
						.add(Restrictions.le("day", new Integer(endDay))));
			} else if (endMonth == -1) {
				monthDisjunction
				.add(Restrictions.gt("month", new Integer(startMonth)))
				.add(Restrictions.conjunction()
						.add(Restrictions.eq("month", new Integer(startMonth)))
						.add(Restrictions.ge("day", new Integer(startDay))));
			} else {
				Junction startConjunction = Restrictions.conjunction()
				.add(Restrictions.eq("month", new Integer(startMonth)))
				.add(Restrictions.ge("day", new Integer(startDay)));
				Criterion midCriterion =
					Restrictions.between("month", new Integer(startMonth+1), new Integer(endMonth-1));
				Junction endConjunction = Restrictions.conjunction()
				.add(Restrictions.eq("month", new Integer(endMonth)))
				.add(Restrictions.le("day", new Integer(endDay)));

				monthDisjunction.add(startConjunction)
				.add(midCriterion)
				.add(endConjunction);
			}

			/*
	    Junction startConjunction = Restrictions.conjunction()
		.add(Restrictions.ge("day", startDay))
		.add(Restrictions.eq("month", startMonth));
	    Criterion midCriterion =
		Restrictions.between("month", startMonth, endMonth);
	    Junction endConjunction = Restrictions.conjunction()
		.add(Restrictions.le("day", endDay))
		.add(Restrictions.eq("month", endMonth));

	    monthDisjunction.add(startConjunction)
		.add(midCriterion)
		.add(endConjunction);
			 */
		}

		return monthDisjunction;
	}

	public boolean supportsMatchingEntityCriteria() { return true; }
}

