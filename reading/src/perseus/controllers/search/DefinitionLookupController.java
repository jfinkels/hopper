package perseus.controllers.search;

import static perseus.search.LexiconSearcher.SearchMethod.END;
import static perseus.search.LexiconSearcher.SearchMethod.START;
import static perseus.search.LexiconSearcher.SearchMethod.SUBSTRING;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.display.Pager;
import perseus.display.URLBuilder;
import perseus.document.MorphLinksTokenFilter;
import perseus.document.Renderer;
import perseus.language.Language;
import perseus.language.LanguageCode;
import perseus.search.LexiconSearcher;
import perseus.search.nu.SearchResults;
import perseus.util.DisplayPreferences;
import perseus.util.Range;
import perseus.util.Timekeeper;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

public class DefinitionLookupController implements Controller {
	private static Logger logger = Logger.getLogger(DefinitionLookupController.class);

	private static final int HITS_PER_PAGE = 50;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();

		DisplayPreferences prefs = new DisplayPreferences(request, response);
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel prefsStatics = (TemplateHashModel) staticModels.get("perseus.util.DisplayPreferences");

		String term = request.getParameter("lookup");
		if (term == null) {
			term = request.getParameter("q");
		}
		keeper.record("Got term: "+term);
		
		String languageCode = request.getParameter("lang");
		if (languageCode == null) {
			languageCode = request.getParameter("target");
		}
		if (languageCode == null) {
			languageCode = LanguageCode.GREEK;
		} else {
			languageCode = languageCode.toLowerCase();
		}

		String searchType = request.getParameter("type");
		if (searchType == null) {
			searchType = "begin";
		}
		keeper.record("Got search type: "+searchType);
		// TODO: sorting options; different kinds of matching, if Lucene can deal
		// (index by reversing strings?)

		int startingPage = 1;
		if (request.getParameter("page") != null) {
			try {
				startingPage = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException nfe) {
				// okay, whatever.
			}
		}

		Map<String,Object> model = new HashMap<String,Object>();
		Language language = Language.forCode(languageCode);
		keeper.record("Got language: "+language.toString());
		if (term != null) {
			LexiconSearcher searcher = new LexiconSearcher();
			searcher.setTargetLanguage(language);
			if (searchType.equals("begin"))  {
				searcher.setSearchMethod(START);
			} else if (searchType.equals("end")) {
				searcher.setSearchMethod(END);
			} else if (searchType.equals("substring")) {
				searcher.setSearchMethod(SUBSTRING);
			}

			SearchResults results = searcher.search(term,
					Range.range((startingPage-1) * HITS_PER_PAGE, startingPage * HITS_PER_PAGE));

			model.put("pager", new Pager(
					results.getTotalHitCount(), HITS_PER_PAGE, startingPage));
			model.put("results", results);
			keeper.record("Got search results");
		}

		model.put("searchType", searchType);
		model.put("term", term);
		model.put("prefs", prefs);
		model.put("prefsStatics", prefsStatics);
		model.put("url", request.getRequestURI()+"?"+request.getQueryString());
		model.put("languages", Language.getDefinitionLookupLanguages());
		model.put("language", language);

		Renderer renderer = new Renderer(languageCode);
		renderer.addLanguageTokenFilters(prefs);
		renderer.addTokenFilter(new MorphLinksTokenFilter(languageCode));
		model.put("renderer", renderer);

		URLBuilder builder = URLBuilder.fromRequest(request,
				new String[] {"lookup", "q", "lang", "target", "type" });
		model.put("builder", builder);

		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMinimumFractionDigits(0);
		formatter.setMaximumFractionDigits(2);
		model.put("formatter", formatter);
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("definitionlookup", model);
	}

}
