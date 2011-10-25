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
import perseus.document.TableOfContents;

/*
 * CTSGetValidReff is a wrapper class for TableOfContents
 */
public class CTSGetValidReff extends AbstractXsltView {

    private TableOfContents toc;
    private Map requestMap = new HashMap();

    public CTSGetValidReff(){}

    public String getContentType() {
	return "text/xml; charset=UTF-8";
    }

    public CTSGetValidReff(TableOfContents toc, Map requestMap) {
	this.toc = toc;
	this.requestMap = requestMap;
    }

    public Element toXML() {

	Element contents = new Element("contents");
	
	Element requestElet = new Element("request");
	Iterator requestMapIter = requestMap.keySet().iterator();
	while (requestMapIter.hasNext()) {
	    String param = (String)requestMapIter.next();
	    String value = (String)requestMap.get(param);
	    Element paramElet = new Element(param);
	    paramElet.addContent(value);
	    requestElet.addContent(paramElet);
	}
	contents.addContent(requestElet);

	StringReader hierarchyReader = new StringReader(toc.toXML());
	
	try {
	    SAXBuilder builder = new SAXBuilder();
	    Document hierarchyDoc = 
		builder.build(hierarchyReader);
	    Element hierarchyRoot = hierarchyDoc.detachRootElement();
	    contents.addContent(hierarchyRoot);
	} catch(JDOMException e) {
	    e.printStackTrace();
	} catch(NullPointerException e) {
	    e.printStackTrace();
	} catch(IOException ioe) {
	    ioe.printStackTrace();
	}
	
	return contents;
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
				 HttpServletResponse res) throws Exception {
	org.jdom.Document doc = new org.jdom.Document();
	CTSGetValidReff gvr = (CTSGetValidReff)((Map)model.get("model")).get("gvr");
	Node gvrXML = new DOMOutputter().output(new org.jdom.Document(gvr.toXML()));
	return new DOMSource(gvrXML);
    }

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res)
	throws Exception {
	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }

}
