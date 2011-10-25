/**
 * Contains a bunch of constants used to represent the options we can use for
 * searching. An excellent candidate for static imports if/when we end up
 * moving to Java 1.5.
 *
 * Comments are given with each option regarding their suggested use, but they
 * don't need to be followed to the letter; the intent of this package is to
 * establish some common names for parameters, not to dictate their behavior.
 * Mostly because I've mixed up my parameters one time too many, and it's
 * much easier to hard-code their names than to pass around strings and hope
 * they stay consistent.
 *
 * This isn't meant to include fields specific to a given search method, like
 * our Lucene indices; it's meant to encompass all possible options we can
 * handle.
 */

package perseus.search.nu;

public class SearchOptions {
    /** Whether to aggregate the results (how, exactly, depends on the type
     * of search) */
    public static final String AGGREGATE = "aggregate";

    /** Which classes to search (this can be used to restrict a named-entity
     * search to a particular type of entity) */
    public static final String CLASSES = "classes";

    /** A set of documents to restrict this search to */
    public static final String DOCUMENTS = "docs";

    /** A map of documents to their frequencies */
    public static final String DOCUMENT_FREQUENCIES = "doc-freqs";

    /** Whether to expand the given search somehow
     * (into all a lemma's forms, e.g.) */
    public static final String EXPAND = "expand";

    /** The language to search in */
    public static final String LANGUAGE = "language";

    /** How to sort the results. */
    public static final String SORT_METHOD = "sort";

    /** The order in which to sort. */
    public static final String SORT_ORDER = "order";

    /** The page of results to display. */
    public static final String PAGE = "page";

    private SearchOptions() {}
}
