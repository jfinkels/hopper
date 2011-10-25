/**
 * Represents a date, which, unlike the Date class in the Java standard
 * library, makes all of its components optional. Thus, an instance of this
 * class can have a value set for a year but not a month or day ("1856"),
 * or, for example, a month and day set but not a year ("December 4").
 * This allows us to represent all the values of the &lt;date&gt; and
 * &lt;dateStruct&gt; TEI tags.
 */

package perseus.ie.entity;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class Date extends AbstractEntity {

    private static String[] monthNames;
    private static String[] eraNames;
    private static String[] amPmStrings;

    private int year = 0;
    private int month = 0;
    private int day = 0;

    // the value "0" means something for hours, minutes and seconds, so use a
    // different default value.
    private int hour = -1;
    private int minute = -1;
    private int second = -1;
    private double secondFraction = 0.0;

    static {
	DateFormatSymbols symbols = new DateFormatSymbols();
	monthNames = symbols.getMonths();
	eraNames = symbols.getEras();
	amPmStrings = symbols.getAmPmStrings();
    }

    public Date() {}

    public Date(int year) {
        this(year, 0, 0);
    }

    public Date(int year, int month, int day) throws NumberFormatException {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public Date(int year, int month, int day, int hour, int minute,
            int second, double secondFraction) {
	setYear(year);
	setMonth(month);
	setDay(day);
	setHour(hour);
	setMinute(minute);
	setSecond(second);
	setSecondFraction(secondFraction);
    }

    public int hashCode() {
        int result = 17;

        result = 37*result + getYear();
        if (hasMonth()) result = 37*result + getMonth();
        if (hasDay()) result = 37*result + getDay();

        return result;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getSecond() { return second; }
    public double getSecondFraction() { return secondFraction; }

    public void setYear(int y) { year = y; clearNames(); }
    public void setMonth(int m) {
	if (m < 0 || m > monthNames.length) {
	    reportError("month", m);
	}

	month = m; clearNames();
    }

    public void setDay(int d) {
	// TODO be less lazy about this; check max day by month
	if (d < 0 || d > 31) {
	    reportError("day", d);
	}
	day = d; clearNames();
    }
    public void setHour(int h) {
	if (h < -1 || h > 24) {
	    reportError("hour", h);
	}
	hour = h; clearNames();
    }
    public void setMinute(int m) {
	if (m < -1 || m > 60) {
	    reportError("minute", m);
	}
	minute = m; clearNames();
    }
    public void setSecond(int s) {
	if (s < -1 || s > 60) {
	    reportError("second", s);
	}
	second = s; clearNames();
    }
    public void setSecondFraction(double sf) {
	if (sf < 0.0 || sf > 1.0) {
	    reportError("second fraction", sf);
	}
	secondFraction = sf; clearNames();
    }

    private void reportError(String field, Number quantity) {
	throw new IllegalArgumentException(
	    "Illegal " + field + " for date: " + quantity);
    }

    public boolean hasYear() { return (year != 0); }
    public boolean hasMonth() { return (month != 0); }
    public boolean hasDay() { return (day != 0); }
    public boolean hasHour() { return (hour != -1); }
    public boolean hasMinute() { return (minute != -1); }
    public boolean hasSecond() { return (second != -1); }
    public boolean hasSecondFraction() { return (secondFraction != 0); }

    protected String toXMLHelper() {
        StringBuffer output = new StringBuffer();
        if (hasYear()) appendToXML(output, "year", year);
        if (hasMonth()) appendToXML(output, "month", month);
        if (hasDay()) appendToXML(output, "day", day);
        if (hasHour()) appendToXML(output, "hour", hour);
        if (hasMinute()) appendToXML(output, "minute", minute);
        if (hasSecond()) appendToXML(output, "second", second);
        if (hasSecondFraction()) {
            output.append("<secondFraction>")
                .append(secondFraction).append("</secondFraction>\n");
        }

        return output.toString();
    }

    private void appendToXML(StringBuffer sb, String name, int value) {
        sb.append("<").append(name).append(">").append(value)
            .append("</").append(name).append(">");
    }

    public String getAuthorityName() {
        if (authorityName != null) return authorityName;

        StringBuffer output = new StringBuffer();

        if (hasYear() || hasMonth() || hasDay()) {
            if (hasYear()) {
                /*
                int year = getYear();
                output.append((year >= 0 ? "+" : "-"));
                output.append(padValue((year >= 0 ? year : -year), 4));
                */
                output.append(getYear());
            } else {
                output.append("-");
            }

            if (hasMonth()) {
                output.append("-");
                output.append(padValue(getMonth(), 2));
            } else if (hasDay()) {
                output.append("-");
            }

            if (hasDay()) {
                output.append("-");
                output.append(padValue(getDay(), 2));
            }
        }

        if (hasHour()) {
            if (output.length() > 0) {
                // Only append a "T" if we have an actual date so far
                output.append("T");
            }
            output.append(padValue(getHour(), 2));
            if (hasMinute()) {
                output.append(":");
                output.append(padValue(getMinute(), 2));
                if (hasSecond()) {
                    output.append(":");
                    output.append(padValue(getSecond(), 2));
                }
            }
            if (hasSecondFraction()) {
                output.append(",");
                output.append(getSecondFraction());
            }
        }

        authorityName = output.toString();
        return authorityName;
    }

    public String getDisplayName() {
        if (displayName != null) return displayName;

        StringBuffer output = new StringBuffer();

        if (hasMonth()) {
            output.append(monthNames[getMonth()-1]);
        }
        if (hasDay()) {
            if (hasMonth()) output.append(" ");
            int day = getDay();
            output.append(getDay());

            if (day % 10 == 1 && day != 11) {
                output.append("st");
            } else if (day % 10 == 2 && day != 12) {
                output.append("nd");
            } else if (day % 10 == 3 && day != 13) {
                output.append("rd");
            } else {
                output.append("th");
            }
        }

        if (hasYear()) {
            if (hasMonth() || hasDay()) {
                output.append(", ");
            }

            int year = getYear();
            if (year < 0) {
                output.append(-year).append(" ");
                output.append(eraNames[0]);
            } else {
                output.append(year).append(" ");
                output.append(eraNames[1]);
            }
        }

        if (hasHour()) {
            output.append(", ");
            int hour = getHour();
            boolean isPM = (hour > 12);
            output.append(isPM ? (hour-12) : hour);

            if (hasMinute()) {
                output.append(":").append(padValue(getMinute(), 2));
            }
            if (hasSecond()) {
                output.append(":").append(padValue(getSecond(), 2));
            }

            output.append(" ")
                .append(amPmStrings[isPM ? Calendar.PM : Calendar.AM]);
            if (hasSecondFraction()) {
                output.append(" (").append(getSecondFraction())
                    .append(")");
            }
        }

        displayName = output.toString();
        return displayName;
    }

    public String toXML() {
        StringBuffer output = new StringBuffer();

        output.append("<date value=\"").append(getAuthorityName())
            .append("\">");
        if (hasYear()) {
            output.append("<year>").append(getYear()).append("</year>");
        }
        if (hasMonth()) {
            output.append("<month>").append(getMonth()).append("</month>");
        }
        if (hasDay()) {
            output.append("<day>").append(getDay()).append("</day>");
        }
        if (hasHour()) {
            output.append("<hour>").append(getHour()).append("</hour>");
        }
        if (hasMinute()) {
            output.append("<minute>").append(getMinute()).append("</minute>");
        }
        if (hasSecond()) {
            output.append("<second>").append(getSecond()).append("</second>");
        }
        if (hasSecondFraction()) {
            output.append("<secondFraction>").append(getSecondFraction())
                .append("</secondFraction>");
        }

        output.append("</date>");

        return output.toString();
    }

    private void clearNames() {
        displayName = null;
        authorityName = null;
    }

    private String padValue(int value, int minDigits) {
        // This strategy is largely taken from
        // http://mindprod.com/jgloss/conversion.html.
        String valueString = Integer.toString(value);
        if (valueString.length() >= minDigits) return valueString;

        return "0000000000000".substring(0, minDigits-valueString.length())
            + valueString;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Date)) {
            return false;
        }

        Date ds = (Date) o;

        return (getYear() == ds.getYear() &&
                getMonth() == ds.getMonth() &&
                getDay() == ds.getDay() &&
                getHour() == ds.getHour() &&
                getMinute() == ds.getMinute() &&
                getSecond() == ds.getSecond() &&
                getSecondFraction() == ds.getSecondFraction());
    }

    public int compareTo(Entity e) {
        if (!(e instanceof Date)) {
            throw new IllegalArgumentException();
        }

        Date ds = (Date) e;

        int yearValue = getYear() - ds.getYear();
        if (yearValue != 0) return yearValue;

        int monthValue = getMonth() - ds.getMonth();
        if (monthValue != 0) return monthValue;

        int dayValue = getDay() - ds.getDay();
        if (dayValue != 0) return dayValue;

        int hourValue = getHour() - ds.getHour();
        if (hourValue != 0) return hourValue;

        int minuteValue = getMinute() - ds.getMinute();
        if (minuteValue != 0) return minuteValue;

        int secondValue = getSecond() - ds.getSecond();
        if (secondValue != 0) return secondValue;

        return (int) (getSecondFraction() - ds.getSecondFraction());
    }

    public String getSortableString() {
        return getAuthorityName();
    }
    
    public String toString() {
	return getDisplayName();
    }
}
