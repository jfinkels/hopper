package perseus.document;

import java.io.File;

public interface MetadataExtractor {

    public MetadataStore extract(Query query, String xmlText)
	throws MetadataExtractionException;
    public MetadataStore extract(Query query, File file)
	throws MetadataExtractionException;
}
