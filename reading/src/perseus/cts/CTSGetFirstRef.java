/*
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package perseus.cts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.DOMOutputter;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;
import org.w3c.dom.Node;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;

/*
 * CTSGetFirstRef
 */
public class CTSGetFirstRef extends AbstractXsltView {
	private static final Logger logger = Logger.getLogger(CTSGetFirstRef.class);

	private Map requestMap = new HashMap();

	public CTSGetFirstRef() {}

	public CTSGetFirstRef(Map requestMap) {
		this.requestMap = requestMap;
	}

	public String getContentType() {
		return "text/xml; charset=UTF-8";
	}

	public Element toXML() {
	/*
    String config = (String)requestMap.get("config");
	String textgroup = (String)requestMap.get("textgroup");
	String work = (String)requestMap.get("work");
	String collection = (String)requestMap.get("collection");
	String ref = (String)requestMap.get("ref");
	String level = (String)requestMap.get("level");
	*/
		String edition = (String)requestMap.get("edition");


		Element getFirstRef = new Element("GetFirstRef");

		// The request element
		Element requestElet = new Element("request");
		Iterator requestMapIter = requestMap.keySet().iterator();
		while (requestMapIter.hasNext()) {
			String param = (String)requestMapIter.next();
			String value = (String)requestMap.get(param);
			Element paramElet = new Element(param);
			paramElet.addContent(value);
			requestElet.addContent(paramElet);
		}
		getFirstRef.addContent(requestElet);
		edition = edition.replaceAll("_", ".");
		Query contextQuery = new Query("Perseus:text:" + edition);
		try {
			Chunk chunk = contextQuery.getChunk();
			Query targetQuery = chunk.getQuery();	
			String displayCitation = targetQuery.getDisplaySubqueryCitation();
			Element firstref = new Element("firstref");
			firstref.addContent(displayCitation);
			getFirstRef.addContent(firstref);
		} catch (InvalidQueryException iqe) {
			logger.error(iqe);
		}

		return getFirstRef;
	}

	protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		CTSGetFirstRef gfr = (CTSGetFirstRef)((Map)model.get("model")).get("gfr");
		Node gfrXML = new DOMOutputter().output(new org.jdom.Document(gfr.toXML()));
		return new DOMSource(gfrXML);
	}

	public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res)
	throws Exception {
		return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
	}


}
