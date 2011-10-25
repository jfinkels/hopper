package perseus.chunking;

import org.xml.sax.Attributes;

public class XMLRenderer {
    
    public static final String LEFT_PARENTHESIS = "\uff08";
    public static final String RIGHT_PARENTHESIS = "\uff09";
    
    public String renderStartElement(String namespaceURI, String localName,
	    String qName, Attributes attributes) {
	
	StringBuffer tag = new StringBuffer();
	
	tag.append("<").append(renderElementText(namespaceURI, localName,
		qName, attributes));
	tag.append(">");
	
	return tag.toString();
    }
    
    public String startElementFromText(String tagText) {
	StringBuffer tag = new StringBuffer();
	
	tag.append("<").append(tagText).append(">");
	return tag.toString();
    }
    
    public String emptyElementFromText(String tagText) {
	StringBuffer tag = new StringBuffer();
	
	tag.append("<").append(tagText).append(" />");
	return tag.toString();
    }
    
    public String endElementFromText(String tagText) {
	StringBuffer tag = new StringBuffer();
	
	tag.append("</").append(tagText).append(">");
	return tag.toString();
    }
    
    public String renderEmptyElement(String namespaceURI, String localName,
	    String qName, Attributes attributes) {
	
	StringBuffer tag = new StringBuffer();
	
	tag.append("<").append(qName).append(" ");
	tag.append(renderAttributes(attributes));
	tag.append(" />");
	
	return tag.toString();
    }
    
    public String renderElementText(String namespaceURI,
	    String localName, String qName, Attributes attributes) {
	
	StringBuffer tag = new StringBuffer();
	
	tag.append(qName);
	if (attributes.getLength() > 0) {
	    tag.append(" ").append(renderAttributes(attributes));
	}
	
	return tag.toString();
    }
    
    private String processAttribute(String s) {
	s = s.replaceAll("&", "&amp;");
	s = s.replaceAll("<", "&lt;");
	s = s.replaceAll(">", "&gt;");
	s = s.replaceAll("\"", "&quot;");
	
	return s;
    }
    
    public String renderEndElement(String namespaceURI, String localName,
	    String qName) {
	
	return "</" + qName + ">";
    }
    
    public String renderCharacters(String text) {
	char[] chars = text.toCharArray();
	return renderCharacters(chars, 0, chars.length);
    }
    
    public String renderCharacters(char[] chars, int startIndex, int length) {
	if (length == 1) {
	    if (chars[startIndex] == 160) {
		return "&#160;";
	    }
	    else if (chars[startIndex] == 60) {
		return "&lt;";
	    }
	    else if (chars[startIndex] == 62) {
		return "&gt;";
	    }
	    else if (chars[startIndex] == 38) {
		return "&amp;";
	    }
	    else if (chars[startIndex] == '(') {
		// Transform left parentheses to a near-equivalent Unicode
		// version that the beta-code parser won't catch
		return LEFT_PARENTHESIS;
	    }
	    else if (chars[startIndex] == ')') {
		// Do the same for &rpar;
		return RIGHT_PARENTHESIS;
	    } else {
		String s = new String(chars, startIndex, length);
		return s;
	    }
	}
	return new String(chars, startIndex, length);
    }
    
    public String renderIgnorableWhitespace(char[] chars,
	    int startIndex, int length) {
	return new String(chars, startIndex, length);
    }
    
    public String renderAttributes(Attributes attributes) {
	StringBuffer buffer = new StringBuffer();
	if (attributes != null) {
	    for (int i = 0, n = attributes.getLength(); i < n; i++) {
		buffer.append(attributes.getQName(i) + "=\""
			+ processAttribute(attributes.getValue(i)) + "\"");
		if (i < n-1) buffer.append(" ");
	    }
	}
	
	return buffer.toString();
    }
    
    /*
     public void ignoreAttribute(String attrName) {
     ignoredAttributes.add(attrName);
     }
     
     public void unignoreAttribute(String attrName) {
     if (ignoredAttributes.contains(attrName)) {
     ignoredAttributes.add(attrName);
     }
     }
     
     public boolean isIgnoringAttribute(String attrName) {
     return ignoredAttributes.contains(attrName);
     }
     
     public Set getIgnoredAttributes() {
     return ignoredAttributes;
     }
     */
}
