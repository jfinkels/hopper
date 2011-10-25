package perseus.util;

import java.util.Comparator;

import org.apache.commons.collections.Bag;

public class BagCountComparator implements Comparator {

    private Bag bag;
    private int sortOrder;

    private static final int ASCENDING = 0;
    private static final int DESCENDING = 1;

    public BagCountComparator(Bag b) {
	this(b, DESCENDING);
    }

    public BagCountComparator(Bag b, int order) {
	bag = b;
	sortOrder = order;
    }

    public int compare(Object o1, Object o2) {
	if (sortOrder == ASCENDING) {
	    return bag.getCount(o1) - bag.getCount(o2);
	} else {
	    return bag.getCount(o2) - bag.getCount(o1);
	}
    }

    public boolean equals(Object o) {
	if (!(o instanceof BagCountComparator)) {
	    return false;
	}

	BagCountComparator bcc = (BagCountComparator) o;

	return (bag.equals(bcc.bag));
    }
}
