package perseus.document;

import perseus.chunking.TEIP4Processor;
import perseus.chunking.XMLEventProcessor;

public class TEIP4Adapter extends DocumentAdapter {

    public XMLEventProcessor getEventProcessor(String documentID) {
	return new TEIP4Processor(documentID, null);
    }

    public MetadataExtractor getMetadataExtractor() {
	return new TEIP4MetadataExtractor();
    }
}
