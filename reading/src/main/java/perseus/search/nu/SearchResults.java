package perseus.search.nu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import perseus.util.Range;

public class SearchResults<T extends SearchResult> implements Iterable<T> {
    int totalHitCount = 0;

    List<T> hits = new ArrayList<T>();
    Range<Integer> range = Range.NONE;

    private String identifier;
    private String type;
    
    private Searcher searcher;

    public Searcher getSearcher() {
        return searcher;
    }

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }

    public SearchResults() {}

    public SearchResults(int thc) {
	this(thc, Range.NONE);
    }

    public SearchResults(int thc, Range<Integer> r) {
	totalHitCount = thc;
	range = r;
    }

    public T get(int idx) {
	return hits.get(idx);
    }
    
    public List<T> getHits() {
	return hits;
    }

    public Range<Integer> getRange() {
	return range;
    }

    public int getTotalHitCount() {
	// Cover for ourselves if we forgot to set the total hit count
	if (totalHitCount == 0 && !hits.isEmpty()) {
	    return getReturnedHitCount();
	}
	
	return totalHitCount;
    }

    public String getIdentifier() {
	return identifier;
    }
    
    public String getType() {
	return type;
    }

    public void setTotalHitCount(int count) {
	totalHitCount = count;
    }

    public void setRange(Range<Integer> r) {
	range = r;
    }

    public int getReturnedHitCount() {
	return hits.size();
    }

    public void setIdentifier(String i) {
	identifier = i;
    }

    public void setType(String t) {
	type = t;
    }

    public void add(T hit) {
	hits.add(hit);
    }

    public void addAll(List<T> list) {
	hits.addAll(list);
    }

    public Iterator<T> iterator() {
	return hits.iterator();
    }
}
