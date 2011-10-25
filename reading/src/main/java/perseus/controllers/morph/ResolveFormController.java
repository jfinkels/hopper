package perseus.controllers.morph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import perseus.display.Pager;
import perseus.display.URLBuilder;
import perseus.document.MorphLinksTokenFilter;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.morph.dao.LemmaDAO;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

public class ResolveFormController implements Controller {
    private static Logger logger = Logger.getLogger(ResolveFormController.class);

	private static final int HITS_PER_PAGE = 50;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();

		String lookupForm = request.getParameter("lookup");
		if (lookupForm == null || lookupForm.equals("")) {
			lookupForm = request.getParameter("entry");
		}
		keeper.record("Got lookup word: "+lookupForm);

		String matchMode = request.getParameter("type");
		if (matchMode == null) matchMode = "exact";
		//include begin for redirects from P3
		if (matchMode.equalsIgnoreCase("begin")) {
			matchMode = "start";
		}
		keeper.record("Got match mode: "+matchMode);

		String languageCode = request.getParameter("lang");
		Language language = Language.GREEK;
		if (languageCode != null) {
			languageCode = languageCode.toLowerCase();
			language = Language.forCode(languageCode);
		}
		keeper.record("Got language: "+language.toString());
		
		List<Lemma> results = null;
		if (lookupForm != null) {
			LemmaDAO dao = new HibernateLemmaDAO();

			//first assume user didn't enter accents and look for results ignoring accents.
			//if there is nothing, then try again using accents.  if still no results, then we 
			//definitely don't have anything
			if (matchMode.equals("start")) {
				results = dao.getLemmasStartingWith(lookupForm, language.getCanonicalAbbreviation(), true);
				if (results.isEmpty()) {
					results = dao.getLemmasStartingWith(lookupForm, language.getCanonicalAbbreviation(), false);
				}
			} else if (matchMode.equals("end")) {
				results = dao.getLemmasEndingWith(lookupForm, language.getCanonicalAbbreviation(), true);
				if (results.isEmpty()) {
					results = dao.getLemmasEndingWith(lookupForm, language.getCanonicalAbbreviation(), false);
				}
			} else if (matchMode.equals("substring")) {
				results = dao.getLemmasWithSubstring(lookupForm, language.getCanonicalAbbreviation(), true);
				if (results.isEmpty()) {
					results = dao.getLemmasWithSubstring(lookupForm, language.getCanonicalAbbreviation(), false);
				}
			} else {
				results = dao.getMatchingLemmas(lookupForm, -1, language.getCanonicalAbbreviation(), true);
				if (results.isEmpty()) {
					results = dao.getMatchingLemmas(lookupForm, -1, language.getCanonicalAbbreviation(), false);
				}
			}
		}
		
		DisplayPreferences prefs = new DisplayPreferences(request, response);
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel prefsStatics = (TemplateHashModel) staticModels.get("perseus.util.DisplayPreferences");

		Map<String,Object> model = new HashMap<String,Object>();

		if (results != null) {
			keeper.record("Got lemma results: "+results.size());
			int totalResults = results.size();
			model.put("totalResults", totalResults);

			//redirect to TextController if there is one Lemma result
			//and one dictionary entry for that Lemma
			if (totalResults == 1) {
				Lemma result = results.get(0);
				if (result.getLexiconQueries().size() == 1) {
					Query query = ((SortedSet<Query>) result.getLexiconQueries()).first();
					return new ModelAndView(new RedirectView("text"),
							"doc", query.toString());
				}
			}
			keeper.record("Handled any redirection to text");
			
			Renderer renderer = new Renderer(language);
			renderer.addLanguageTokenFilters(prefs);
			renderer.addTokenFilter(new MorphLinksTokenFilter(languageCode));
			model.put("renderer", renderer);

			int startingPage = 1;
			if (request.getParameter("page") != null) {
				try {
					startingPage = Integer.parseInt(request.getParameter("page"));
				} catch (NumberFormatException nfe) {
					// okay, whatever.
				}
			}

			model.put("pager", new Pager(totalResults, HITS_PER_PAGE, startingPage));

			URLBuilder builder = URLBuilder.fromRequest(request,
					new String[] {"lookup", "type", "lang"});

			model.put("builder", builder);

			if (totalResults > 0) {
				int endIndex = Math.min(totalResults, startingPage * HITS_PER_PAGE);
				results = results.subList((startingPage - 1) * HITS_PER_PAGE, endIndex);
			}
			model.put("results", results);
			keeper.record("Handled pagination");
		}

		model.put("lookupForm", lookupForm);
		model.put("languages", Language.getResolveFormLanguages());
		model.put("language", language);
		model.put("prefs", prefs);
		model.put("prefsStatics", prefsStatics);
		model.put("url", request.getRequestURI()+"?"+request.getQueryString());
		model.put("matchMode", matchMode);
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("resolveform", model);
	}

}
