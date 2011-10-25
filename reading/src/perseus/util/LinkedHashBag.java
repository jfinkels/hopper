package perseus.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.set.ListOrderedSet;

/**
 * An implementation of the Jakarta Commons Bag class that preserves
 * the order in which elements were added.
 */

public class LinkedHashBag extends HashBag {

    public List elementList = new ArrayList();

    public LinkedHashBag() {
	super();
    }

    public LinkedHashBag(Collection coll) {
	super(coll);
    }

    public boolean add(Object object) {
	return super.add(object);
    }

    public boolean add(Object object, int nCopies) {
	for (int copiesAdded = 0; copiesAdded < nCopies; copiesAdded++) {
	    elementList.add(object);
	}
	return super.add(object, nCopies);
    }

    public boolean addAll(Collection coll) {
	return super.addAll(coll);
    }

    /**
     * Returns an iterator over the elements of this LinkedHashBag in the
     * order in which the elements were added.
     */
    public Iterator iterator() {
	return elementList.iterator();
    }

    public boolean remove(Object object) {
	return super.remove(object);
    }

    public boolean remove(Object object, int nCopies) {
	int copiesRemoved = 0;
	while (copiesRemoved < nCopies) {
	    elementList.remove(object);
	    copiesRemoved++;
	}
	return super.remove(object, nCopies);
    }

    public boolean removeAll(Collection coll) {
	return super.removeAll(coll);
    }

    public boolean retainAll(Collection coll) {
	return super.retainAll(coll);
    }

    public Set uniqueSet() {
	ListOrderedSet los = new ListOrderedSet();
	los.addAll(elementList);
	return los;
    }

    public String toString() {
	return super.toString();
    }
}
