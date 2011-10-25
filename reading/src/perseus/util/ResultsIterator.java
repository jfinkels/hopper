package perseus.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * An iterator that wraps the results of a Hibernate query. Subclasses
 * need only implement the getResults() method.
 */
public abstract class ResultsIterator<T> implements Iterator<T> {
    private int firstResult = 0;
    
    private static final int DEFAULT_RESULTS_PER_PAGE = 200;
    
    private int resultsPerPage;
    private Queue<T> results;
    private boolean initialized = false;
    
    public ResultsIterator() {
	this(DEFAULT_RESULTS_PER_PAGE);
    }
    
    public ResultsIterator(int rpp) {
	resultsPerPage = rpp;
	results = new ArrayBlockingQueue<T>(resultsPerPage);
    }
    
    public boolean hasNext() {
	init();
	return results.peek() != null;
    }
    
    public T next() {
	init();
	T result = results.poll();
	if (result == null) throw new NoSuchElementException();
	
	if (results.isEmpty()) {
	    getMoreResults();
	}
	
	return result;
    }
    
    // This delays the call to getMoreResults() until after any subclass
    // constructors have finished
    private void init() {
	if (!initialized) {
	    getMoreResults();
	    initialized = true;
	}
    }
    
    private void getMoreResults() {
	for (T result : getResults(firstResult, resultsPerPage)) {
	    results.add(result);
	}
	firstResult += resultsPerPage;
    }
    
    protected abstract Collection<T> getResults(int firstResult, int maxResults);

    public void remove() {
	throw new UnsupportedOperationException();
    }
}
