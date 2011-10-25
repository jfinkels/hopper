package perseus.ie;

import java.util.*;

public class IEComparator implements Comparator<IEComparable> {

    public static final String ORIGINAL_TEXT_ORDER = "original";
    public static final String DISPLAY_TEXT_ORDER = "display";
    public static final String TOKEN_ORDER = "token";
    public static final String FREQUENCY_ORDER = "freq";

    /** default sort order is the order in which terms appear in the
	document */
    String field = TOKEN_ORDER;

    public IEComparator () {

    }
    
    public IEComparator (String f) {
	if (f != null) {
	    field = f;
	}
    }

    public int compare(IEComparable term1, IEComparable term2) {
	if (field.equals(TOKEN_ORDER)) {
	    if (term1.getPosition() - term2.getPosition() != 0) {
		return term1.getPosition() - term2.getPosition();
	    }
	    return term1.getSecondarySortableString().compareTo(term2.getSecondarySortableString());
	}
	else if (field.equals(ORIGINAL_TEXT_ORDER)) {
	    return term1.getSortableString().compareTo(term2.getSortableString());
	}
	else if (field.equals(DISPLAY_TEXT_ORDER)) {
	    return term1.getSortableString().compareTo(term2.getSortableString());
	}
	else if (field.equals(FREQUENCY_ORDER)) {
	    // Assume descending order is more useful than ascending order
	    int freqComparison = term2.getCount() - term1.getCount();
	    if (freqComparison != 0) {
		return freqComparison;
	    }

	    // Sort items that appear the same number of times by name.
	    return term1.getSortableString().compareTo(term2.getSortableString());
	}
	else {
	    // Default to document order
	    
	    if (term1.getPosition() - term2.getPosition() != 0) {
		return term1.getPosition() - term2.getPosition();
	    }
	    return term1.getSecondarySortableString().compareTo(term2.getSecondarySortableString());

	}
    }
}
