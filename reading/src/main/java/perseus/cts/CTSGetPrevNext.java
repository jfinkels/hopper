/*
 * @MPL@
 * Last Modified: @TIME@
 * 
 * Author: @gweave01@
 */
package perseus.cts;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;
import org.w3c.dom.Node;
import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.Query;

/*
 * CTSGetPrevNext is a wrapper class for Chunk
 */
public class CTSGetPrevNext extends AbstractXsltView {

    private Chunk chunk;
    private Map requestMap = new HashMap();

    public CTSGetPrevNext(){}

    public CTSGetPrevNext(Chunk chunk, Map requestMap) {
	this.chunk = chunk;
	this.requestMap = requestMap;
    }

    public String getContentType() {
	return "text/xml; charset=UTF-8";
    }

    public Element toXML() {
	Element getPrevNext = new Element("GetPrevNext");

	Element requestElet = new Element("request");
	Iterator requestMapIter = requestMap.keySet().iterator();
	while (requestMapIter.hasNext()) {
	    String param = (String)requestMapIter.next();
	    String value = (String)requestMap.get(param);
	    Element paramElet = new Element(param);
	    paramElet.addContent(value);
	    requestElet.addContent(paramElet);
	}
	getPrevNext.addContent(requestElet);
	
	Chunk previous = chunk.getPrevious();
	Chunk next = chunk.getNext();
	
	Element prevnext = new Element("prevnext");
	Element prev = new Element("prev");
	if (previous != null) {
	    Element ref = new Element("ref");
	    Query previousQuery = previous.getQuery();
	    //	    Metadata metadata = previousQuery.getMetadata();
	    String displayCitation = previousQuery.getDisplaySubqueryCitation();
	    ref.addContent(displayCitation);
	    prev.addContent(ref);
	}
	prevnext.addContent(prev);
	Element nextElet = new Element("next");
	if (next != null) {
	    Element ref = new Element("ref");
	    Query nextQuery = next.getQuery();
	    //	    Metadata metadata = nextQuery.getMetadata();
	    String displayCitation = nextQuery.getDisplaySubqueryCitation();
	    ref.addContent(displayCitation);
	    nextElet.addContent(ref);

	}
	prevnext.addContent(nextElet);
	getPrevNext.addContent(prevnext);
	
	return getPrevNext;
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
				 HttpServletResponse res) throws Exception {
	org.jdom.Document doc = new org.jdom.Document();
	CTSGetPrevNext gpn = (CTSGetPrevNext)((Map)model.get("model")).get("gpn");
	Node gpnXML = new DOMOutputter().output(new org.jdom.Document(gpn.toXML()));
	return new DOMSource(gpnXML);
    }

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res)
	throws Exception {
	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }
    
}
