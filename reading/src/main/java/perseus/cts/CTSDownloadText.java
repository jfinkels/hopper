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
 * CTSDownloadText
 */
public class CTSDownloadText extends AbstractXsltView {

    private Map requestMap = new HashMap();

    public CTSDownloadText() {}

    public CTSDownloadText(Map requestMap) {
	this.requestMap = requestMap;
    }

    public String getContentType() {
	return "text/xml; charset=UTF-8";
    }

    public Element toXML() {
	String edition = (String)requestMap.get("edition");
	
	String[] archNums = edition.split("\\_");
	String collection = archNums[0] + "." + archNums[1];
	edition = archNums[0] + "." + archNums[1] + "." + archNums[2];

	Element downloadText = new Element("DownloadText");
	Element requestElet = new Element("request");
	Iterator requestMapIter = requestMap.keySet().iterator();
	while (requestMapIter.hasNext()) {
	    String param = (String)requestMapIter.next();
	    String value = (String)requestMap.get(param);
	    Element paramElet = new Element(param);
	    paramElet.addContent(value);
	    requestElet.addContent(paramElet);
	}
	downloadText.addContent(requestElet);
	/*Use this xml path if pushing a release to a different machine*/
	//String xmlPath = "/usr/local/perseus/texts/" + collection + "/" + edition + ".xml";

	/*Use this xml path if NOT pushing a release to a different machine*/
	String xmlPath = "/sgml/xml/texts/" + collection + "/" + edition + ".xml";

	File teiFile = new File(xmlPath);
	
	try {
	    SAXBuilder builder = new SAXBuilder();
	    Document teiDoc = builder.build(teiFile);
	    Element teiRoot = teiDoc.detachRootElement();
	    downloadText.addContent(teiRoot);
	} catch (JDOMException e) {
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    e.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}

	return downloadText;
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req, 
				      HttpServletResponse res) throws Exception {
	
	org.jdom.Document doc = new org.jdom.Document();
	CTSDownloadText dt  = (CTSDownloadText)((Map)model.get("model")).get("dt");
	Node dtXML = new DOMOutputter().output(new org.jdom.Document(dt.toXML()));
	return new DOMSource(dtXML);
    }

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res)  
	throws Exception {
	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }

}
