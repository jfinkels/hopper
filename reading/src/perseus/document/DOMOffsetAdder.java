package perseus.document;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;

import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;

import org.jdom.input.SAXBuilder;

import org.xml.sax.SAXException;

import org.xml.sax.helpers.AttributesImpl;

/**
 * A utility class that attempts to calculate byte offsets for all elements in
 * a given JDOM tree. Since, in general, documents can have multiple chunk
 * schemes, it's necessary for many classes, like {@link
 * perseus.ie.entity.EntityOccurrence} and {@link perseus.ie.Citation}, to
 * include the exact point of occurrence in the document, as opposed to being
 * tied to a particular query. Since Java provides no support for calculating
 * byte offsets, we have to do it ourselves.
 */
public class DOMOffsetAdder {

    /**
     * The offset at which this chunk begins. Usually acquired by a call to
     * Chunk#getStartOffset().
     */
    private long startingOffset = 0;

    private OffsetReportingHandler handler =
	new OffsetReportingHandler("utf-8");

    public DOMOffsetAdder() {}

    public DOMOffsetAdder(long startOff) {
	startingOffset = startOff;
    }

    private static final Logger logger = Logger.getLogger(DOMOffsetAdder.class);

    public static final String START_OFFSET = "perseus:start_offset";
    public static final String END_OFFSET = "perseus:end_offset";

    /**
     * Traverses the given document and adds byte offsets to every element as
     * attributes. Note that, by adding additional attributes, we're
     * effectively making the byte offsets invalid for this particular
     * copy of the text, though they'll still apply to the original
     * text, which hasn't been touched.
     */
    public void processDocument(Document document) {
	processContent(document.getRootElement());
	handler.endDocument();
    }

    /**
     * Helper method for processing the actual content of the document.
     * Basically, our calculation strategy consists of writing out the XML as a
     * string in the desired encoding,  converting it to bytes, and recording
     * the length of the array.
     */
    private void processContent(Content content) {
        if (content instanceof Element) {
	    processElement((Element) content);
	} else if (content instanceof Text) {
	    processText((Text) content);
	}
    }

    private void processElement(Element element) {
	AttributesImpl saxAttrs = new AttributesImpl();

        for (Object attrObj : element.getAttributes()) {
            Attribute attr = (Attribute) attrObj;

            saxAttrs.addAttribute(attr.getNamespaceURI(),
                    attr.getName(), attr.getQualifiedName(),
                    "CDATA", attr.getValue());
        }

        String localName = element.getName();
        String qName = element.getQualifiedName();
        String namespace = element.getNamespaceURI();

	try {
	    handler.startElement(namespace, localName, qName, saxAttrs);
	} catch (SAXException se) {
	    logger.error("Bad start element", se);
	}

	if (element.getAttribute("start_offset") == null) {
	    element.setAttribute("start_offset",
		    Long.toString(calculateByteOffset()));
	}

        for (Object child : element.getContent()) {
            processContent((Content) child);
        }

	try {
	    handler.endElement(namespace, localName, qName);
	} catch (SAXException se) {
	    logger.error("Bad end element", se);
	}

	if (element.getAttribute("end_offset") == null) {
	    element.setAttribute("end_offset",
		    Long.toString(calculateByteOffset()));
	}
    }

    private void processText(Text text) {
	String characters = text.getText();
	try {
	    handler.characters(characters.toCharArray(),
				0, characters.length());
	} catch (SAXException se) {
	    logger.error("Bad text", se);
	}
    }

    private long calculateByteOffset() {
	return handler.getByteOffset() + startingOffset;
    }

    /**
     * Sets the offset at which this particular chunk begins. This should be
     * used when the chunk is part of a larger XML document.
     */
    public void setStartingOffset(long newOffset) {
	startingOffset = newOffset;
    }

    public long getStartingOffset() {
	return startingOffset;
    }

    /**
     * Convenience method to parse a chunk and return a JDOM Document
     * representing its text, with byte offsets added as attributes
     * ("start_offset" and "end_offset", respectively) to each element.
     *
     * @param chunk the chunk to parse
     * @return a Document representing the chunk, with byte offsets added
     */
    public static Document domFromChunk(Chunk chunk)
        throws IOException, JDOMException {

	String chunkText = chunk.getText();
	String openTags = chunk.getOpenTags();

	int startingOffset = chunk.getStartOffset() - openTags.length();

	DOMOffsetAdder adder = new DOMOffsetAdder(startingOffset);
	Document document = null;

        StringReader reader = new StringReader(chunkText);
        document = new SAXBuilder().build(reader);

	adder.processDocument(document);

	return document;
    }
}
