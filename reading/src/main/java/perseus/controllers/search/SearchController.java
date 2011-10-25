/**
 * 
 */
package perseus.controllers.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Corpus;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.language.Language;
import perseus.util.Config;
import perseus.util.Timekeeper;

/**
 * @author rsingh04
 *
 */
public class SearchController implements Controller {
    private static Logger logger = Logger.getLogger(SearchController.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Timekeeper keeper = new Timekeeper();
		keeper.start();
		
		Metadata metadata = null;

		if (request.getParameter("doc") != null) {
		    Query query = new Query(request.getParameter("doc"));
		    metadata = query.getMetadata();
		}
		keeper.record("Checked if searching in context");
				
		Set<Language> searchableLanguages = Language.getSearchableLanguages();
		Set<Language> definitionLookupLanguages = Language.getDefinitionLookupLanguages();
		Set<Language> resolveFormLanguages = Language.getResolveFormLanguages();
		keeper.record("Got different language sets");
		
		List<Corpus> corpusList = new ArrayList<Corpus>();
		if (metadata == null) {
			String[] allCollections = Config.getPrimaryCollections();
			for (String coll : allCollections) { 
				Corpus corpus = new Corpus(coll);
				corpusList.add(corpus);
			}
			keeper.record("Got primary collections list");
		}

		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("metadata", metadata);
		myModel.put("corpusList", corpusList);
		myModel.put("searchLangs", searchableLanguages);
		myModel.put("defLookupLangs", definitionLookupLanguages);
		myModel.put("resolveFormLangs", resolveFormLanguages);
		
		keeper.stop();
		logger.info(keeper.getResults());
		
		return new ModelAndView("search", "model", myModel);
	}

}
