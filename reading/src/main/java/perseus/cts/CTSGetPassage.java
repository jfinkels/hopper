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

/*
 * CTSGetPassage is a wrapper class for Chunk
 */
public class CTSGetPassage extends AbstractXsltView {

    private Chunk chunk;
    private Map requestMap = new HashMap();

    public CTSGetPassage(){}

    public CTSGetPassage(Chunk chunk, Map requestMap) {
	this.chunk = chunk;
	this.requestMap = requestMap;
    }

    public String getContentType() {
	return "text/xml; charset=UTF-8";
    }

    public Element toXML() {
	Element getPassage = new Element("GetPassage");

	Element requestElet = new Element("request");
	Iterator requestMapIter = requestMap.keySet().iterator();
	while (requestMapIter.hasNext()) {
	    String param = (String)requestMapIter.next();
	    String value = (String)requestMap.get(param);
	    Element paramElet = new Element(param);
	    paramElet.addContent(value);
	    requestElet.addContent(paramElet);
	}
	getPassage.addContent(requestElet);
	

	StringReader chunkReader = new StringReader(chunk.getText());
	
	try {
	    SAXBuilder builder = new SAXBuilder();
	    Document chunkDoc = 
		builder.build(chunkReader);
	    Element chunkRoot = chunkDoc.detachRootElement();
	    getPassage.addContent(chunkRoot);
	} catch(JDOMException e) {
	    e.printStackTrace();
	} catch(NullPointerException e) {
	    e.printStackTrace();
	} catch(IOException ioe) {
	    ioe.printStackTrace();
	}
	
	return getPassage;
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
				 HttpServletResponse res) throws Exception {
	org.jdom.Document doc = new org.jdom.Document();
	CTSGetPassage gp = (CTSGetPassage)((Map)model.get("model")).get("gp");
	Node gpXML = new DOMOutputter().output(new org.jdom.Document(gp.toXML()));
	return new DOMSource(gpXML);
    }

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res)
	throws Exception {
	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }


}
