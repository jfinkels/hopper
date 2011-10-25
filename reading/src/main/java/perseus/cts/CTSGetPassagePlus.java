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
import perseus.document.TableOfContents;
import perseus.util.DisplayPreferences;

/*
 * CTSGetPassagePlus is a wrapper class for Chunk
 */
public class CTSGetPassagePlus extends AbstractXsltView {
	private static final Logger logger = Logger.getLogger(CTSGetPassagePlus.class);

	private Map requestMap = new HashMap();

	public CTSGetPassagePlus(){}

	public CTSGetPassagePlus(Map requestMap) {
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
		String level = (String)requestMap.get("level");
		 */
		String edition = (String)requestMap.get("edition");
		String ref = (String)requestMap.get("ref");

		Element getPassagePlus = new Element("GetPassagePlus");

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
		getPassagePlus.addContent(requestElet);

		// The TEIFrag
		edition = edition.replaceAll("_", ".");
		Query contextQuery = new Query("Perseus:text:" + edition);
		String displayCitation = contextQuery.getDisplayCitation();
		String canonicalReference = displayCitation + ref;
		Query targetQuery = new Query(canonicalReference, new DisplayPreferences(), contextQuery);

		try {
			Chunk chunk = contextQuery.getChunk();
			Chunk targetChunk = targetQuery.getChunk();
			CTSGetPassage gp = new CTSGetPassage(targetChunk, requestMap);	    
			getPassagePlus.addContent(gp.toXML());

			TableOfContents toc = TableOfContents.forChunk(chunk);
			chunk = toc.getByQuery(targetQuery);
			CTSGetPrevNext gpn = new CTSGetPrevNext(chunk, requestMap);
			getPassagePlus.addContent(gpn.toXML());

			CTSGetCitationScheme cgcs = new CTSGetCitationScheme(requestMap);
			getPassagePlus.addContent(cgcs.toXML());

		} catch (InvalidQueryException iqe) {
			logger.error(iqe);
		}

		return getPassagePlus;
	}

	protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		CTSGetPassagePlus gpp = (CTSGetPassagePlus)((Map)model.get("model")).get("gpp");
		Node gppXML = new DOMOutputter().output(new org.jdom.Document(gpp.toXML()));
		return new DOMSource(gppXML);
	}

	public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res)
	throws Exception {
		return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
	}

}
