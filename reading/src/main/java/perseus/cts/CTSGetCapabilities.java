/*
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package perseus.cts;

import java.io.File;
import java.io.IOException;
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
 * CTSGetCapabilities 
 */
public class CTSGetCapabilities extends AbstractXsltView {

    public CTSGetCapabilities() {}

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
	Element result = new Element("GetCapabilities");
	try {
	    SAXBuilder builder = new SAXBuilder();
	    Document capsDoc = builder.build(getCapsFile);
	    result = capsDoc.detachRootElement();
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
        CTSGetCapabilities gc = (CTSGetCapabilities)((Map)model.get("model")).get("gc");
        Node gcXML = new DOMOutputter().output(new org.jdom.Document(gc.toXML()));
        return new DOMSource(gcXML);
    }

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }
    
}
