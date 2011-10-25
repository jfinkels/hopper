package perseus.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * A specialized {@link HashMap} intended to be used for counting sets of
 * objects. As a subclass of {@link Map}, it supports all the usual
 * map-related methods, but it is better used by invoking its own methods,
 * described below.
 */

public class ObjectCounter<K> extends HashMap<K,ObjectCounter.Count> {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an empty ObjectCounter.
     */
    public ObjectCounter() {
        super();
    }

    /**
     * Creates an ObjectCounter populated with values from the supplied
     * counter. (The new counter is populated using new objects, so additions
     * to one ObjectCounter will not modify values in the other.)
     * 
     * @param counter an existing ObjectCounter
     */
    public ObjectCounter(ObjectCounter<K> counter) {
        super();

        // Do a deep copy, not a shallow copy!
        for (K item : counter.objects()) {
            put(item, counter.get(item).get());
        }
    }

    /**
     * Increments the given object by 1. If the object has not already been
     * encountered by this counter, it is given the value 1.
     * @param s the object to count
     * @return the new value of s
     */
    public int count(K s) {
        return count(s, 1);
    }

    /**
     * Increments the given object by the given amount, or assigns the object
     * this amount if it has not already been seen. 
     * @param s the object to count
     * @param amount the amount by which to increment
     * @return the new value of s
     */
    public int count(K s, int amount) {
        getOrCreate(s).add(amount);
        return get(s).get();
    }

    /**
     * Increments the value by one; a convenience method for {@link count()}.
     * @param s the object to increment
     * @return the new value of s
     */
    public int increment(K s) {
        return count(s);
    }

    /**
     * Decrements the object by one.
     * @param s
     * @return the new value of s
     */
    public int decrement(K s) {
        getOrCreate(s).decrement();
        return get(s).get();
    }

    private ObjectCounter.Count getOrCreate(K s) {
        if (!containsKey(s)) put(s, 0);
        return get(s);
    }

    /**
     * An alias for @{link containsKey()}.
     */
    public boolean contains(K s) {
        return containsKey(s);
    }

    /**
     * Gets the count for the specified object, as an integer.
     * 
     * @param s the object in question
     * @return the count for the object
     */
    public int getCount(K s) {
        return get(s).get();
    }

    /**
     * Sets the count for the given object to zero. If the object has not been
     * counted before, it is added (with a count of zero).
     * @param s the object to reset
     */
    public void reset(K s) {
        getOrCreate(s).reset();
    }

    /**
     * Returns all objects being counted; an alias for @{keySet()}.
     * @return all counted objects
     */
    public Set<K> objects() {
        return keySet();
    }

    public void put(K key, int count) {
        super.put(key, new Count(count));
    }

    public void put(K key, Count count) {
        // Again, don't reuse Count objects
        super.put(key, new Count(count.get()));
    }

    /**
     * Returns all counted objects, sorted by ascending count.
     * @return all counted objects, smallest first
     */
    public Set<K> getAscendingObjects() {
        // Create a new sorted set with this class as its comparator
        TreeSet<K> sortedKeys =
            new TreeSet<K>(new CountComparator(CountComparator.ASCENDING));
        sortedKeys.addAll(keySet());
        return sortedKeys;
    }

    /**
     * Returns all counted objects, sorted by descending count.
     * @return all counted objects, largest first
     */
    public Set getDescendingObjects() {
        // Create a new sorted set with this class as its comparator
        TreeSet<K> sortedKeys =
            new TreeSet<K>(new CountComparator(CountComparator.DESCENDING));
        sortedKeys.addAll(keySet());
        return sortedKeys;
    }

    /** This inner class represents the frequency of a particular object */
    public class Count {
        int frequency = 0;

        public Count(int startingCount) {
            frequency = startingCount;
        }

        public void increment() {
            frequency++;
        }

        public void decrement() {
            frequency--;
        }

        public void add(int a) {
            frequency += a;
        }

        public void subtract(int a) {
            frequency -= a;
        }

        public void reset() {
            frequency = 0;
        }

        public void set(int i) {
            frequency = i;
        }

        public int get() {
            return frequency;
        }
    }

    private class CountComparator implements Comparator<K> {
        public static final int ASCENDING = 1;
        public static final int DESCENDING = -1;
        int order = 1;

        public CountComparator(int order) {
            this.order = order;
        }

        public int compare (K o1, K o2) {
            int left = getCount(o1);
            int right = getCount(o2);

            int difference = right - left;
            if (difference == 0) {
                if (o1 instanceof String &&
                        o2 instanceof String) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;

                    difference = s1.compareTo(s2);
                }
            }

            return difference * order;
        }
    }
}
