/**
 * Generic exception that indicates some sort of issue in fetching Metadata.
 */
package perseus.document;

public class MetadataAccessException extends RuntimeException {
    public MetadataAccessException(Throwable t) {
	super(t);
    }

    public MetadataAccessException(Query q) {
	super(q.toString());
    }
}
