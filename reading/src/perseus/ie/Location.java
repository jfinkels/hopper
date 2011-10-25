package perseus.ie;

import perseus.document.Query;

/**
 * Struct-like class representing a particular location within a document.
 * Contains both a query (which may or may not be only a document ID) and
 * a position/byte offset.
 */
public class Location implements Comparable<Location> {

    Query query;
    int position;

    public Location() {}

    public Location(Query q, int p) {
	query = q;
	position = p;
    }

    public Query getQuery() {
	return query;
    }

    public void setQuery(Query q) {
	query = q;
    }

    public int getPosition() {
	return position;
    }

    public void setPosition(int p) {
	position = p;
    }

    public String toString() {
	return query + " [pos " + position + "]";
    }

    public boolean equals(Object o) {
	if (!(o instanceof Location)) {
	    return false;
	}

	Location l = (Location) o;

	if (!query.equals(l.query)) {
	    return false;
	}

	return (position == l.position);
    }

    public int compareTo(Location l) {
	int queryResult = query.compareTo(l.query);
	if (queryResult != 0) {
	    return queryResult;
	}

	return (position - l.position);
    }
}
