package perseus.search.nu;

public class SearchResult<T> implements Comparable<SearchResult> {
    String title;
    String identifier;
    T content;
    float relevance = 0.0f;

    public SearchResult() {}

    public SearchResult(String tl, String id, T desc) {
	this(tl, id, desc, 0.0f);
    }

    public SearchResult(String tl, String id, T desc, float rel) {
	title = tl;
	identifier = id;
	content = desc;
	relevance = rel;
    }

    public String getTitle() {
	return title;
    }

    public T getContent() {
	return content;
    }

    public String getIdentifier() {
	return identifier;
    }

    public float getRelevance() {
	return relevance;
    }

    public void setTitle(String t) {
	title = t;
    }

    public void setContent(T c) {
	content = c;
    }

    public void setIdentifier(String i) {
	identifier = i;
    }

    public void setRelevance(float r) {
	relevance = r;
    }

    public String toString() {
	return String.format("%s: %s", getIdentifier(), getTitle());
    }
    
    public boolean equals(Object o) {
	if (!(o instanceof SearchResult)) return false;
	
	SearchResult sr = (SearchResult) o;
	
	return (getIdentifier().equals(sr.getIdentifier()));
    }
    
    public int compareTo(SearchResult sr) {
	return getIdentifier().compareTo(sr.getIdentifier());
    }
}
