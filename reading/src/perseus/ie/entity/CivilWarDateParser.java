/**
 * Date parser for non-ISO dates, so named because most such dates are found in
 * the Civil War XML documents. The format is similar to ISO, but slightly
 * different (and prone to collisions between some months and years).
 */
package perseus.ie.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CivilWarDateParser extends AbstractDateParser {

    private static Pattern YEAR_PATTERN =
	Pattern.compile("(\\d*)\\s*(A\\.?D\\.?|B\\.?C\\.?)$",
		Pattern.CASE_INSENSITIVE);

    public Date parse(String dateString) {
	int year = 0;
	int month = 0;
	int day = 0;

	String[] dateTokens = dateString.trim().split("-");
	boolean isBC = false;

	int tokenIndex = 0;

	Date date = new Date();

	// Egad. Lots of special cases:
	if (dateTokens.length == 1) {
	    // This is probably a simple year, but it could have AD or BC
	    // following it. Parse the year so we can be sure.
	    year = parseYear(dateTokens[0]);
	} else if (dateTokens.length == 2) {
	    if (dateTokens[0].length() == 0) {
		// The given date is a month ("-5"). Or maybe a B.C. year.
		// It's sort of hard to tell, all things considered.
		// We'll make it a year. Unless it's between 1 and 12.
		int token = Integer.parseInt(dateTokens[1]);

		if (token > 12) {
		    year = -token;
		} else {
		    month = token;
		}
	    } else if (dateTokens[1].length() > 0 &&
		       Integer.parseInt(dateTokens[1]) > 12) {
		// This is supposed to parse dates like "07-30",
		// which really ought to be "--07-30"...
		month = Integer.parseInt(dateTokens[0]);
		day = Integer.parseInt(dateTokens[1]);
	    } else {
		year = Integer.parseInt(dateTokens[0]);
		month = Integer.parseInt(dateTokens[1]);
	    }
	} else if (dateTokens.length == 3) {
	    // Several possibilities here, like:
	    //  1234-5-6 (full date)
	    //  1234-5- (year and month)
	    //  1234-- (year)
	    //  --5 (day)
	    //  -234-5 (BC year and month)
	    //  -10-15 (month and day)

	    if (dateTokens[0].length() == 0 &&
		    dateTokens[1].length() == 0 &&
		    dateTokens[2].length() > 0) {
		day = Integer.parseInt(dateTokens[2]);
	    } else if (dateTokens[0].length() == 0) {
		int token = Integer.parseInt(dateTokens[1]);
		if (token > 12) {
		    year = -token;
		    if (dateTokens[2].length() > 0) {
			month = Integer.parseInt(dateTokens[2]);
		    }
		} else {
		    month = token;
		    if (dateTokens[2].length() > 0) {
			day = Integer.parseInt(dateTokens[2]);
		    }
		}
	    } else {
		if (dateTokens[0].length() > 0) {
		    year = Integer.parseInt(dateTokens[0]);
		}
		if (dateTokens[1].length() > 0) {
		    month = Integer.parseInt(dateTokens[1]);
		}
		if (dateTokens[2].length() > 0) {
		    day = Integer.parseInt(dateTokens[2]);
		}
	    }
	} else if (dateTokens.length == 4) {
	    // Four tokens. Of the form:
	    //  -1234-5-6 (full date, BC)
	    year = Integer.parseInt("-" + dateTokens[1]);
	    if (dateTokens[2].length() > 0) {
		month = Integer.parseInt(dateTokens[2]);
	    }
	    if (dateTokens[3].length() > 0) {
		day = Integer.parseInt(dateTokens[3]);
	    }
	} else {
	    throw new NumberFormatException("Unable to parse date "
		    + dateString);
	}

	// *One* of these should have been set; if not, we can't deal with
	// our input
	if (year == 0 && month == 0 && day == 0) {
	    throw new NumberFormatException("Unable to parse date "
		    + dateString);
	}

	return new Date(year, month, day);
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
