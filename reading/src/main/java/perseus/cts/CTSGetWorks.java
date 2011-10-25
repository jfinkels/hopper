/*
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package perseus.cts;

import java.io.IOException;
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

/*
 * CTSGetWorks
 */
public class CTSGetWorks extends AbstractXsltView {

    private Map requestMap = new HashMap();

    public CTSGetWorks() {}

    public CTSGetWorks(Map requestMap) {
	this.requestMap = requestMap;
    }

    public String getContentType() {
	return "text/xml; charset=UTF-8";
    }

    public Element toXML() {
	Element getWorks = new Element("GetWorks");
	
	Element requestElet = new Element("request");
	Iterator requestMapIter = requestMap.keySet().iterator();
	while (requestMapIter.hasNext()) {
	    String param = (String)requestMapIter.next();
	    String value = (String)requestMap.get(param);
	    Element paramElet = new Element(param);
	    paramElet.addContent(value);
	    requestElet.addContent(paramElet);
	}
	getWorks.addContent(requestElet);

	CTSGetCapabilities cgc = new CTSGetCapabilities();
	Element getCaps = cgc.toXML();
	getWorks.addContent(getCaps);
	
	return getWorks;
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
				 HttpServletResponse res) throws Exception {
	org.jdom.Document doc = new org.jdom.Document();
	CTSGetWorks gw = (CTSGetWorks)((Map)model.get("model")).get("gw");
	Node gwXML = new DOMOutputter().output(new org.jdom.Document(gw.toXML()));
	return new DOMSource(gwXML);
    }

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }

}
