/**
 * Generic exception to indicate that something went wrong at some point while
 * we were trying to extract metadata.
 */

package perseus.document;

import java.io.File;

public class MetadataExtractionException extends Exception {
    private static final long serialVersionUID = 1L;

    public MetadataExtractionException(File f) {
	super(f.getAbsolutePath());
    }

    public MetadataExtractionException(String s) {
	super(s);
    }

    public MetadataExtractionException(String message, File f) {
	super(message + ": " + f.getAbsolutePath());
    }

    public MetadataExtractionException(Throwable t) {
	super(t);
    }
}
