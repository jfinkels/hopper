package perseus.ie.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISODateParser extends AbstractDateParser {

    private static Pattern YEAR_PATTERN =
	Pattern.compile("(\\d*)\\s*(A\\.?D\\.?|B\\.?C\\.?)$",
		Pattern.CASE_INSENSITIVE);

    public Date parse(String dateString) {
	Date ds = new Date();

	// An ISO string will represent date and time as (say)
	// 1994-11-05T08:15:30
	String[] tokens = dateString.split("T");
	String datePart = tokens[0];

	parseISODateTokens(datePart, ds);

	if (tokens.length > 1) {
	    String timePart = tokens[1];
	    parseISOTimeTokens(timePart, ds);
	}
	return ds;
    }

    private void parseISODateTokens(String dateText, Date date) {

	String[] tokens = dateText.split("-");

	if (tokens.length == 1) {
	    // just a year
	    date.setYear(parseYear(tokens[0]));
	} else if (tokens.length == 2) {
	    if (tokens[0].length() == 0) {
		// something like "-81"--i.e., 1981
		date.setYear(1900 + Integer.parseInt(tokens[1]));
	    } else {
		// something like "1841-12" - a year and a month
		date.setYear(parseYear(tokens[0]));
		date.setMonth(Integer.parseInt(tokens[1]));
	    }
	} else if (tokens.length == 3) {
	    if (tokens[0].length() == 0) {
		if (tokens[1].length() == 0) {
		    // "--12" - just a month
		    date.setMonth(Integer.parseInt(tokens[2]));
		} else {
		    // "-81-12" - 1900+year and a month
		    date.setYear(1900 + Integer.parseInt(tokens[1]));
		    date.setMonth(Integer.parseInt(tokens[2]));
		}
	    } else {
		// "1841-12-5" - year, month, day
		date.setYear(parseYear(tokens[0]));
		date.setMonth(Integer.parseInt(tokens[1]));
		date.setDay(Integer.parseInt(tokens[2]));
	    }
	} else {
	    if (tokens[0].length() == 0) {
		if (tokens[1].length() == 0) {
		    if (tokens[2].length() == 0) {
			// "---12" - just a day
			date.setDay(Integer.parseInt(tokens[3]));
		    } else {
			// "--4-5" - month and day
			date.setMonth(Integer.parseInt(tokens[2]));
			date.setDay(Integer.parseInt(tokens[3]));
		    }
		} else {
		    // "-85-1-12" - 1900+year, month, day
		    date.setYear(1900 + Integer.parseInt(tokens[1]));
		    date.setMonth(Integer.parseInt(tokens[2]));
		    date.setDay(Integer.parseInt(tokens[3]));
		}
	    }
	}
    }

    private void parseISOTimeTokens(String timeText, Date time) {
	String[] timeTokens = timeText.split(":");

	time.setHour(Integer.parseInt(timeTokens[0]));
	time.setMinute(Integer.parseInt(timeTokens[1]));

	if (timeTokens.length > 2) {
	    String[] secondTokens = timeTokens[2].split(",");

	    time.setSecond(Integer.parseInt(secondTokens[0]));
	    if (secondTokens.length > 1) {
		time.setSecondFraction(
			Double.parseDouble("0." + secondTokens[1]));
	    }
	}
    }

    // This method handles years that might possibly be followed by "BC" or 
    // "AD", and sets the sign of the number to match the era.
    private int parseYear(String yearText) {
	Matcher yearMatcher = YEAR_PATTERN.matcher(yearText);
	if (yearMatcher.matches()) {
	    int year = Integer.parseInt(yearMatcher.group(1));
	    String era = yearMatcher.group(2).replaceAll("[.]", "");

	    if (era.equalsIgnoreCase("BC")) {
		return -year;
	    } else {
		return year;
	    }
	} else {
	    return Integer.parseInt(yearText);
	}
    }

    public DateRange parseRange(String rangeText) {
	String[] dateTokens = rangeText.split("/");
	return new DateRange(parse(dateTokens[0]), parse(dateTokens[1]));
    }
}
