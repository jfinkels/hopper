/**
 * A simple class to encapsulate some attributes used in paging through search
 * results (or another sort of ordered results). Meant to be passed as a
 * parameter to a view (like a JSP or FreeMarker page).
 */

package perseus.display;

public class Pager {
    /** The total number of hits to display */
    private int totalHits = 100;
    
    /** The number of hits per page */
    private int pageSize = 10;
    
    /** The page the user's currently looking at */
    private int currentPage = 1;
    
    /** The number of pages to display on either side of the current page */
    private int pageRadius = 5;
    
    public Pager(int hits) {
	totalHits = hits;
    }
    
    public Pager(int hits, int size) {
	this(hits);
	pageSize = size;
    }
    
    public Pager(int hits, int size, int page) {
	this(hits, size);
	currentPage = page;
    }
    
    public Pager(int hits, int size, int page, int thresh) {
	this(hits, size, page);
	pageRadius = thresh;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageRadius() {
        return pageRadius;
    }
    public void setPageRadius(int pageThreshold) {
        this.pageRadius = pageThreshold;
    }
    public int getTotalHits() {
        return totalHits;
    }
    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }
}
