/**
 * Document adapter for EEBO (Early English Books Online) files.
 * These are based on the TEI Lite DTD, but use a great many tags of their
 * own devising.
 */

package perseus.document;

import perseus.chunking.XMLEventProcessor;
import perseus.chunking.TEIP4Processor;

import java.io.File;

public class EEBOAdapter extends DocumentAdapter {
    public XMLEventProcessor getEventProcessor(String documentID) {
	// Ehh. Let's see how this works for now.
	return new TEIP4Processor(documentID, null);
    }

    public MetadataExtractor getMetadataExtractor() {
	// EEBO files, at least the ones we've been given, don't store any
	// metadata information in the documents themselves. Return an
	// empty store.
	return new MetadataExtractor() {
	    public MetadataStore extract(Query query, String xmlText)
		throws MetadataExtractionException {

		return new MetadataStore();
	    }
	    public MetadataStore extract(Query query, File file)
		throws MetadataExtractionException {

		return new MetadataStore();
	    }
	};
    }
}
