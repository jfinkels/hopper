package perseus.document;

import perseus.chunking.XMLRenderer;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A subclass of SAXDefaultHandler that keeps count of the number of bytes
 * read. Counting can be enabled/disabled by calls to `startCountingBytes()`
 * and `stopCountingBytes()`; the current count is available via
 * `getByteOffset()`. This class is used by the {@link DOMOffsetAdder} to
 * keep track of byte offsets for named entities and citations.
 *
 * @see DOMOffsetAdder
 */

public class OffsetReportingHandler extends DefaultHandler {
	private static Logger logger = Logger.getLogger(OffsetReportingHandler.class);

	private String encoding;
	private XMLRenderer renderer;

	private String previousText = null;

	private String openTagText = null;
	private String openTagName = null;

	private boolean countingBytes = true;
	private int bytesRead = 0;

	public OffsetReportingHandler(String enc) {
		encoding = enc;
		renderer = new XMLRenderer();
	}

	public OffsetReportingHandler(String enc, XMLRenderer rend) {
		encoding = enc;
		renderer = rend;
	}

	public void startElement(String namespaceURI,
			String localName,
			String qName,
			Attributes attributes) throws SAXException {
		// if we're waiting on an open tag, print it (now that we know it's
		// not an empty tag)
		flushOpenTag();

		// then print the text from the previous element
		flushPreviousText();

		// now register this as a new possibly-empty tag
		setOpenTag(namespaceURI, localName, qName, attributes);
	}

	public void endElement(String namespaceURI,
			String localName,
			String qName) throws SAXException {

		if (openTagText != null) {
			handleOpenTag(qName);
		} else {
			String elementText = renderer.renderEndElement(
					namespaceURI, localName, qName);
			recordText(elementText);
		}
	}

	public void endDocument() {
		flushPreviousText();
	}

	public void characters(char[] chars, int startIndex, int length) 
	throws SAXException {

		flushOpenTag();
		String charText = renderer.renderCharacters(chars, startIndex, length);
		recordText(charText);
	}

	public void ignorableWhitespace(char[] chars, int startIndex, int length)
	throws SAXException {

		flushOpenTag();
		String spaceText = renderer.renderIgnorableWhitespace(
				chars, startIndex, length);
		recordText(spaceText);
	}

	private void recordText(String text) {
		if (previousText != null && countingBytes) {
			incrementOffset(previousText);
		}
		previousText = text;
	}

	private void incrementOffset(String text) {
		try {
			byte[] bytes = text.getBytes(encoding);
			bytesRead += bytes.length;
		} catch (UnsupportedEncodingException uee) {
			logger.warn("Problem incrementing offset: " + text);
		}
	}

	private void handleOpenTag(String endQName) throws SAXException {
		if (openTagText == null) return;

		if (endQName.equals(openTagName)) {
			recordText(renderer.emptyElementFromText(openTagText));
			clearOpenTag();
		} else {
			// This should never happen!
			throw new SAXException("Ending tag " + endQName +
					" doesn't match start tag " + openTagName);
			//flushOpenTag();
		}
	}

	private void flushOpenTag() {
		if (openTagText == null) return;

		String prevTagText = renderer.startElementFromText(openTagText);

		clearOpenTag();
		recordText(prevTagText);
	}

	private void flushPreviousText() {
		// May be slightly hackish, but... eh.
		recordText("");
	}

	public void setOpenTag(String namespaceURI,
			String localName,
			String qName,
			Attributes attributes) {

		openTagText = renderer.renderElementText(namespaceURI, localName,
				qName, attributes);
		openTagName = qName;
	}

	public void clearOpenTag() {
		openTagText = null;
		openTagName = null;
	}

	public int getByteOffset() {
		return bytesRead;
	}

	public void reset() {
		bytesRead = 0;
		previousText = null;
		clearOpenTag();
	}

	public String getEncoding() {
		return encoding;
	}

	public void stopCountingBytes() {
		countingBytes = false;
	}

	public void startCountingBytes() {
		countingBytes = true;
	}

	public boolean isCountingBytes() {
		return countingBytes;
	}

	private class OpenTag {
		String namespaceURI;
		String localName;
		String qName;
		Attributes attributes;

		public OpenTag(String nsuri, String lName, String qName,
				Attributes attrs) {
			namespaceURI = nsuri;
			localName = lName;
			this.qName = qName;
			attributes = attrs;
		}

		public String getNamespaceURI() {
			return namespaceURI;
		}

		public Attributes getAttributes() {
			return attributes;
		}

		public String getLocalName() {
			return localName;
		}

		public String getQName() {
			return qName;
		}
	}
}
