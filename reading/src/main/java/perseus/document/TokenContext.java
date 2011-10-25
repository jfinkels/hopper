package perseus.document;

/**
 * This class provides some simple information to determine the location of a
 * Token within a document (or a collection). This information consists of the
 * Query that fetched the list to which the token belongs, and the [one-based]
 * occurrence of the token's form in that query's TokenList (thus, the first
 * occurrence of any form would have occurrence = 1 and the third would have
 * occurrence = 3).
 *
 * Note that the query field is not actually used anywhere as of this writing,
 * but it should be perfectly usable.
 */

public class TokenContext {

    Query query;
    int occurrence;

    public TokenContext(Query q, int occ) {
	query = q;
	occurrence = occ;
    }

    public Query getQuery() {
	return query;
    }

    public int getOccurrence() {
	return occurrence;
    }
}
