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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.TableOfContents;
import perseus.util.DisplayPreferences;

public class CTSController implements Controller {
	private static final Logger logger = Logger.getLogger(CTSController.class);

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {

		String requestType = request.getParameter("request");
		Map<String, AbstractXsltView> myModel = new HashMap<String, AbstractXsltView>();
		String view = "cts";

		if (requestType.equals("GetCapabilities")) {
			CTSGetCapabilities gc = new CTSGetCapabilities();
			myModel.put("gc", gc);
			view = "get_capabilities";
		} else if (requestType.equals("GetWorks")) {
			String config = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);

			CTSGetWorks gw = new CTSGetWorks(requestMap);
			myModel.put("gw", gw);
			view = "get_works";
		} else if (requestType.equals("GetTEIHeader")) {
			String config = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);

			CTSGetTEIHeader gth = new CTSGetTEIHeader(requestMap);
			myModel.put("gth", gth);
			view = "get_tei_header";
		} else if (requestType.equals("GetValidReff")) {
			String config = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");
			String ref = request.getParameter("ref");
			String level = request.getParameter("level");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);
			requestMap.put("ref", ref);
			requestMap.put("level", level);

			edition = edition.replaceAll("_",".");
			Query contextQuery = new Query("Perseus:text:" + edition);
			String displayCitation = contextQuery.getDisplayCitation();
			String canonicalReference = displayCitation + ref;

			Query query = new Query(canonicalReference, new DisplayPreferences(), contextQuery);

			TableOfContents toc = null;
			try {
				toc = TableOfContents.forChunk(query.getChunk());
			} catch (InvalidQueryException iqe) {
				logger.error(iqe);
			}
			CTSGetValidReff gvr = new CTSGetValidReff(toc, requestMap);
			myModel.put("gvr", gvr);
			view = "get_valid_reff";
		} else if (requestType.equals("GetPassage")) {
			String config  = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");
			String ref = request.getParameter("ref");
			String level = request.getParameter("level");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);
			requestMap.put("ref", ref);
			requestMap.put("level", level);

			if ( (edition == null) || (edition.equals("")) ) {
				//		edition = getEdition(textgroup, work);
			}

			edition = edition.replaceAll("_",".");
			Query contextQuery = new Query("Perseus:text:" + edition);
			String displayCitation = contextQuery.getDisplayCitation();
			String canonicalReference = displayCitation + ref;

			Query query = new Query(canonicalReference, new DisplayPreferences(), contextQuery);
			Chunk chunk = null;
			try {
				chunk = query.getChunk();
			} catch (InvalidQueryException iqe) {
				logger.error(iqe);
			}

			CTSGetPassage gp = new CTSGetPassage(chunk, requestMap);
			myModel.put("gp", gp);

			view = "get_passage";
		} else if (requestType.equals("DownloadText")) {
			String config = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);

			CTSDownloadText dt = new CTSDownloadText(requestMap);
			myModel.put("dt", dt);
			view = "download_text";	    
		} else if (requestType.equals("GetCitationScheme")) {
			String config = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);

			CTSGetCitationScheme gcs = new CTSGetCitationScheme(requestMap);
			myModel.put("gcs", gcs);
			view = "get_citation_scheme";	    	    
		} else if (requestType.equals("GetPrevNext")) {
			String config  = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");
			String ref = request.getParameter("ref");
			String level = request.getParameter("level");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);
			requestMap.put("ref", ref);
			requestMap.put("level", level);

			edition = edition.replaceAll("_",".");
			Query contextQuery = new Query("Perseus:text:" + edition);
			String displayCitation = contextQuery.getDisplayCitation();
			String canonicalReference = displayCitation + ref;

			Query targetQuery = new Query(canonicalReference, new DisplayPreferences(), contextQuery);
			try {
				Chunk chunk = contextQuery.getChunk();
				TableOfContents toc = TableOfContents.forChunk(chunk);
				chunk = toc.getByQuery(targetQuery);
				CTSGetPrevNext gpn = new CTSGetPrevNext(chunk, requestMap);
				myModel.put("gpn", gpn);
				view = "get_prev_next";
			} catch (InvalidQueryException iqe) {
				logger.error(iqe);
			}

		} else if (requestType.equals("GetFirstRef")) {
			String config  = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");
			String ref = request.getParameter("ref");
			String level = request.getParameter("level");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);
			requestMap.put("ref", ref);
			requestMap.put("level", level);

			CTSGetFirstRef gfr = new CTSGetFirstRef(requestMap);
			myModel.put("gfr", gfr);
			view = "get_first_ref";
		} else if (requestType.equals("GetPassagePlus")) {
			String config  = request.getParameter("config");
			String textgroup = request.getParameter("textgroup");
			String work = request.getParameter("work");
			String collection = request.getParameter("collection");
			String edition = request.getParameter("edition");
			String ref = request.getParameter("ref");
			String level = request.getParameter("level");

			if ( (edition == null) || (edition.equals("")) ) {
				edition = getEdition(textgroup, work);
			}

			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("config", config);
			requestMap.put("request", requestType);
			requestMap.put("textgroup", textgroup);
			requestMap.put("work", work);
			requestMap.put("collection", collection);
			requestMap.put("edition", edition);
			requestMap.put("ref", ref);
			requestMap.put("level", level);

			CTSGetPassagePlus gpp = new CTSGetPassagePlus(requestMap);
			myModel.put("gpp", gpp);
			view = "get_passage_plus";
		}
		return new ModelAndView(view,  "model", myModel);
	} 

	public static String getEdition(String textgroup, String work) {
		CTSGetCapabilities gc = new CTSGetCapabilities();

		Element result = gc.toXML();
		Filter tgFilter = new ElementFilter("textgroup");
		Iterator tgIter = result.getDescendants(tgFilter);
		while (tgIter.hasNext()) {
			Element tg = (Element)tgIter.next();
			String tgProjid = tg.getAttributeValue("projid");
			if (textgroup.equals(tgProjid)) {
				Filter wFilter = new ElementFilter("work");
				Iterator wIter = tg.getDescendants(wFilter);
				while (wIter.hasNext()) {
					Element w = (Element)wIter.next();
					String wProjid = w.getAttributeValue("projid");
					if (work.equals(wProjid)) {
						Filter editionFilter = new ElementFilter("editioncomments");
						Iterator editionIter = w.getDescendants(editionFilter);
						while (editionIter.hasNext()) {
							Element edition = (Element)editionIter.next();
							String editionProjid = edition.getAttributeValue("projid");
							if ( (editionProjid != null) && (!editionProjid.equals("")) ) {
								return editionProjid;
							}
						}
						Filter transFilter = new ElementFilter("translationcomments");
						Iterator transIter = w.getDescendants(transFilter);
						while (transIter.hasNext()) {
							Element trans = (Element)transIter.next();
							String transProjid = trans.getAttributeValue("projid");
							if ( (transProjid != null) && (!transProjid.equals("")) ) {
								return transProjid;
							}
						}
					}
				}
			}
		}
		return "";
	}
}

