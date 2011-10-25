/**
 * Thrown by a TableOfContents to indicate that it could not find a chunk
 * it was asked to search for.
 */

package perseus.document;

public class ChunkNotFoundException extends RuntimeException {

    private TableOfContents tableOfContents;
    private String searchString;

    public ChunkNotFoundException(TableOfContents toc, Query badQuery) {
	this(toc, badQuery.toString());
    }
    
    public ChunkNotFoundException(TableOfContents toc, String ss) {
	tableOfContents = toc;
	searchString = ss;
    }
    
    public String toString() {
	return super.toString() + ": " + tableOfContents.toString() +
		" -> " + searchString;
    }

}
