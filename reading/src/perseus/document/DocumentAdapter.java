package perseus.document;

import perseus.chunking.XMLEventProcessor;

public abstract class DocumentAdapter {

    public abstract XMLEventProcessor getEventProcessor(String documentID);
    public abstract MetadataExtractor getMetadataExtractor();
}
