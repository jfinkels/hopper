package perseus.document;

import java.io.File;

import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import perseus.chunking.XMLRenderer;

public class TEIP4MetadataExtractor implements MetadataExtractor {

	private static CatalogManager catalogManager = new CatalogManager();
	private static CatalogResolver resolver = null;
	static {
		// Turn off all those entity-resolving messages
		catalogManager.setVerbosity(0);
		resolver = new CatalogResolver(catalogManager);
	}

	public MetadataStore extract(Query query, String xmlText) 
	throws MetadataExtractionException {

		return MetadataStoreLoader.fromXML(xmlText);
	}

	public MetadataStore extract(Query query, File file)
	throws MetadataExtractionException {

		InputSource source = new InputSource(file.getPath());

		String xml = extractHeader(query, source);
		MetadataStore store = extract(query, xml);

		store = store.merge(
				MetadataStoreLoader.fromXML(
						getCastMetadata(query, source))).merge(
								MetadataStoreLoader.fromXML(
										getWitnessMetadata(query, source)));

		return store;
	}

	private String extractHeader(Query query, InputSource source)
	throws MetadataExtractionException {
		return doExtraction(query, source, "teiHeader",
		"build/document/tei2p4.xsl");
	}

	private String getWitnessMetadata(Query query, InputSource source)
	throws MetadataExtractionException {
		return doExtraction(query, source, "witList",
		"build/document/witlist.xsl");
	}

	private String getCastMetadata(Query query, InputSource source)
	throws MetadataExtractionException {
		return doExtraction(query, source, "castList",
		"build/document/castlist.xsl");
	}

	private String doExtraction(Query query, InputSource source,
			String targetTag, String stylesheet)
	throws MetadataExtractionException {

		try {
			XMLReader parser = XMLReaderFactory.createXMLReader();

			ElementCapturingHandler handler =
				new ElementCapturingHandler(targetTag, "body");
			parser.setContentHandler(handler);
			parser.setEntityResolver(resolver);

			String header = null;

			try {
				parser.parse(source);
			} catch (FinishedParsingException fpe) {
				// Exceptions indicating we've finished parsing are okay...

				// ...especially if we found the tag we were looking for!
				if (fpe.foundTargetTag()) {
					header = handler.getCapturedText();
					String metadataText = StyleTransformer.transform(
							header, stylesheet, query.toString());
					return metadataText;
				}
				// (but if we didn't, don't panic, since not all documents
				// have witnesses or speakers)
			}

			return null;

			// use query.toString(), not query.getDocumentID(), because we
			// want the subdocument part of the query too, if there is one
		} catch (Exception e) {
			throw new MetadataExtractionException(e);
		}
	}

	/**
	 * This class captures the first occurrence of the specified tag
	 * in the document to be parsed. After it finishes reading the tag,
	 * it throws a FinishedParsingException (a descendant of a SAXException)
	 * to avoid further processing.
	 *
	 * Optionally, you can specify a second argument--a "bailout tag"--
	 * that, when reached, will cause the parser to stop immediately.
	 * This can be useful in the case of large documents, when the
	 * target element may or may not be present.
	 */
	private class ElementCapturingHandler extends DefaultHandler {

		private String tagToCapture;
		private String bailoutTag;

		private StringBuffer captureBuffer;
		private XMLRenderer renderer = new XMLRenderer();

		private boolean capturingText = false;

		public ElementCapturingHandler(String ttc) {
			captureBuffer = new StringBuffer(500);
			tagToCapture = ttc;
		}

		public ElementCapturingHandler(String ttc, String bt) {
			this(ttc);
			bailoutTag = bt;
		}

		public void startElement(String namespaceURI,
				String sName,
				String qName,
				Attributes attributes) throws SAXException {

			if (qName.equals(bailoutTag)) {
				// throw an exception, and mark that we didn't find the
				// tag we were looking for!
				throw new FinishedParsingException(false);
			}

			if (qName.equals(tagToCapture)) {
				capturingText = true;
			}

			if (capturingText) {
				captureBuffer.append(renderer.renderStartElement(
						namespaceURI, sName, qName, attributes));
			}
		}

		public void endElement(String namespaceURI,
				String sName,
				String qName) throws SAXException {
			if (capturingText) {
				captureBuffer.append(renderer.renderEndElement(
						namespaceURI, sName, qName));
			}

			if (qName.equals(tagToCapture)) {
				capturingText = false;

				// This is a horrible way of interrupting parsing, but SAX
				// doesn't provide us with any clean way
				throw new FinishedParsingException();
			}
		}

		public void characters(char[] chars, int startIndex, int length) {
			if (capturingText) {
				captureBuffer.append(renderer.renderCharacters(
						chars, startIndex, length));
			}
		}

		public String getCapturedText() {
			return captureBuffer.toString();
		}

		public boolean capturingText() {
			return capturingText;
		}
	}

	class FinishedParsingException extends SAXException {
		private static final long serialVersionUID = 1L;
		public boolean foundTargetTag = true;

		public FinishedParsingException() {}
		public FinishedParsingException(boolean ftt) {
			this();
			foundTargetTag = ftt;
		}

		public boolean foundTargetTag() { return foundTargetTag; }
	}
}
