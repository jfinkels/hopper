/*
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package perseus.cts;

import java.io.File;
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
 * CTSGetCitationScheme 
 */
public class CTSGetCitationScheme extends AbstractXsltView {

    private Map requestMap = new HashMap();

    public CTSGetCitationScheme() {}

    public CTSGetCitationScheme(Map requestMap) {
	this.requestMap = requestMap;
    }

    public String getContentType() {
	return "text/xml; charset=UTF-8";
    }

    public Element toXML() {
	/* Need to feed this in via properties */
	/* Use this path name if pushing a release to a different machine */
	//String getCapsFilePath = "/usr/local/perseus/docroot/xml/GetCapabilities.xml";  

	/* Use this path name if NOT pushing a release to a different machine */
	String getCapsFilePath = "/sgml/reading/static/xml/GetCapabilities.xml";

	File getCapsFile = new File(getCapsFilePath);
	Element result = new Element("GetCitationScheme");
	try {
	    SAXBuilder builder = new SAXBuilder();

	    Iterator requestMapIter = requestMap.keySet().iterator();
	    Element requestElet = new Element("request");
	    while (requestMapIter.hasNext()) {
		String param = (String)requestMapIter.next();
		String value = (String)requestMap.get(param);
		Element paramElet = new Element(param);
		paramElet.addContent(value);
		requestElet.addContent(paramElet);
	    }
	    result.addContent(requestElet);

	    Document capsDoc = builder.build(getCapsFile);
	    Element citWrapper = capsDoc.detachRootElement();
	    result.addContent(citWrapper);
	} catch (JDOMException e) {
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    e.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
	return result;
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req, 
				      HttpServletResponse res) throws Exception {
	org.jdom.Document doc = new org.jdom.Document();
        CTSGetCitationScheme gcs = (CTSGetCitationScheme)((Map)model.get("model")).get("gcs");
        Node gcsXML = new DOMOutputter().output(new org.jdom.Document(gcs.toXML()));
        return new DOMSource(gcsXML);
    }

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }
    
}
