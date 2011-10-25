package perseus.document;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;

/**
 * This class generates a set of close tags that make arbitrary
 * xml chunks well-formed.
 */

public class XMLChunkHandler extends DefaultHandler {
    Stack openTags;
    
    public XMLChunkHandler () {
	openTags = new Stack();
    }
    
    public void startElement(String namespaceURI,
			     String sName,
			     String qName,
			     Attributes attrs) throws SAXException {
	openTags.push(qName);
    }
    
    public void endElement(String namespaceURI,
			   String sName,
			   String qName) throws SAXException {
	if (qName.equals(openTags.peek())) {
	    openTags.pop();
	}
    }
    
    public String getCloseTags() {
	StringBuffer tags = new StringBuffer();
	
	while (! openTags.empty()) {
	    tags.append("</")
		.append(openTags.pop())
		.append(">");
	}
	
	return tags.toString();
    }
}
