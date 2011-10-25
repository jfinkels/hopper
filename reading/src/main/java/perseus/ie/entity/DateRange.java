package perseus.ie.entity;

import java.util.List;

import org.apache.log4j.Logger;

public class DateRange extends AbstractEntity {
	private static final Logger logger = Logger.getLogger(DateRange.class);

	private Date startDate;
	private Date endDate;

	public DateRange() {}

	public DateRange(Date start, Date end) {
		startDate = start;
		endDate = end;
	}

	public Date getStartDate() { return startDate; }
	public Date getEndDate() { return endDate; }

	public void setStartDate(Date sd) { startDate = sd; }
	public void setEndDate(Date ed) { endDate = ed; }

	// We'll let Hibernate or whomever assign DateRanges an alternate
	// authority name or display name if they really want to, but if no one
	// has, we'll figure them out from our component dates.

	public String getAuthorityName() {
		if (authorityName == null) {
			authorityName = startDate.getAuthorityName() + "/"
			+ endDate.getAuthorityName();
		}

		return authorityName;
	}

	public String getDisplayName() {
		if (displayName == null) {
			displayName = startDate.getDisplayName() + " - "
			+ endDate.getDisplayName();
		}

		return displayName;
	}

	public void willBeRegistered(EntityManager manager) {
		logger.debug("About to register start date");
		List<? extends Entity> matchingStarts =
			manager.getEntitiesByAuthName(startDate.getAuthorityName());
		if (matchingStarts.isEmpty()) {
			logger.debug("Didn't find; registering");
			manager.registerEntity(startDate);
		} else {
			startDate = (Date) matchingStarts.get(0);
			logger.debug("Found: id = " + startDate.getId());
		}

		logger.debug("About to register end date");
		List<? extends Entity> matchingEnds =
			manager.getEntitiesByAuthName(endDate.getAuthorityName());
		if (matchingEnds.isEmpty()) {
			logger.debug("Didn't find; registering");
			manager.registerEntity(endDate);
		} else {
			endDate = (Date) matchingEnds.get(0);
			logger.debug("Found: id = " + endDate.getId());
		}
	}

	public void willBeUnregistered(EntityManager manager) {
		manager.unregisterEntity(startDate);
		manager.unregisterEntity(endDate);
	}

	public String toXML() {
		StringBuffer output = new StringBuffer();

		output.append("<dateRange start=\"")
		.append(startDate.getAuthorityName())
		.append("\" end=\"").append(endDate.getAuthorityName())
		.append("\">\n")
		.append(startDate.toXML())
		.append(endDate.toXML())
		.append("</dateRange>");

		return output.toString();
	}

	protected String toXMLHelper() {
		return toXML();
	}
}
