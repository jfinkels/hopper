package perseus.ie.entity;

/**
 * An interface to allow parsing of dates in various formats. Contains methods
 * for parsing dates and date-ranges and methods for setting various parameters
 * (which can then be handled by the parsing routines). The interface was
 * created largely because having multiple parsing routines within the Date
 * class was getting extremely clunky.
 */

public interface DateParser {

    public Date parse(String dateText);
    public DateRange parseRange(String rangeText);

    public void setParameter(String key, Object value);
    public Object getParameter(String key);
    public boolean hasParameter(String key);
}
