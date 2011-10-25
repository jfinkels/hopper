/**
 * 
 */
package perseus.controllers.morph;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.MorphLinksTokenFilter;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.StyleTransformer;
import perseus.document.TokenList;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;

/**
 * @author rsingh04
 *
 */
public class LoadQueryController implements Controller {
	private static Logger logger = Logger.getLogger(LoadQueryController.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String document = request.getParameter("doc");
		String sourceDocument = request.getParameter("source");
		DisplayPreferences prefs = new DisplayPreferences(request, response);

		Timekeeper keeper = new Timekeeper();
		keeper.start();
		
		Query originalQuery = new Query(document, prefs, null);

		keeper.record("Got original query: " + originalQuery);
		Chunk chunk = originalQuery.getChunk();
		
		keeper.record("Got chunk");

		Query query = chunk.getQuery();

		keeper.record("Got actual query");

		// Reload metadata, which may have changed during chunk lookup
		Metadata metadata = query.getMetadata();

		Renderer renderer = new Renderer(chunk.getEffectiveLanguage());
		renderer.addLanguageTokenFilters(prefs);
		MorphLinksTokenFilter morphFilter = new MorphLinksTokenFilter(chunk);

		String documentIndex = request.getParameter("num");
		if (documentIndex != null) {
		    try {
			morphFilter.setDocumentsProcessed(Integer.parseInt(documentIndex));
		    } catch (NumberFormatException nfe) {
			// do nothing
		    }
		}
		renderer.addTokenFilter(morphFilter);
		
		// Make sure we allow for sense-voting
		String docLanguage = query.getMetadata().get(Metadata.LANGUAGE_KEY);

		Map<String, String> styleParameters = new HashMap<String, String>();
		styleParameters.put("lexquery", query.toString());
		if (!query.isJustDocumentID()) {
		    styleParameters.put("subquery", query.getQuery());
		}
		styleParameters.put("lang", chunk.getEffectiveLanguage());

		if (sourceDocument != null) {
		    Query sourceQuery = new Query(sourceDocument);
		    styleParameters.put("voting_lang", docLanguage);
		    styleParameters.put("source_id", sourceQuery.getDocumentID());
		    styleParameters.put("allow_voting", "true");
		    if (request.getParameter("form") != null) {
			styleParameters.put("form", request.getParameter("form"));
		    }
		    if (request.getParameter("which") != null) {
			styleParameters.put("which", request.getParameter("which"));
		    }
		    if (sourceQuery.getMetadata().get(Metadata.ABO_KEY) != null) {
			styleParameters.put("sourcework", sourceQuery
				.getMetadata().get(Metadata.ABO_KEY));
			styleParameters.put("sourcesub", sourceQuery.getNonABOValues());
		    }
		}
		String styledText = StyleTransformer.transform(chunk.getText(), chunk.getMetadata(), styleParameters);

		keeper.record("Styled text");

		TokenList tokens = TokenList.getTokens(styledText, metadata.getLanguage());

		keeper.record("Tokenized text");

		String output = renderer.render(tokens);
		
		keeper.record("Rendered text");

		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("output", output);
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("loadquery", myModel);
	}

}
