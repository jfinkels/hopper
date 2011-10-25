/**
 * The base class for classes that perform any sort of searching.
 */

package perseus.search.nu;

import perseus.util.Range;

public interface Searcher {
    public SearchResults search(String query, Range<Integer> range);
}
