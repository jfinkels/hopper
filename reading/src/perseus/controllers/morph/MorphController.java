package perseus.controllers.morph;

import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Query;
import perseus.document.Renderer;
import perseus.eval.morph.ArabicVocalizationEvaluator;
import perseus.eval.morph.FormFrequencyEvaluator;
import perseus.eval.morph.ParseEvaluator;
import perseus.eval.morph.ParseSelector;
import perseus.eval.morph.PriorFrequencyEvaluator;
import perseus.eval.morph.UserVotesEvaluator;
import perseus.eval.morph.WordFrequencyEvaluator;
import perseus.eval.morph.ParseSelector.ParseVotingResults;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.dao.HibernateFrequencyDAO;
import perseus.language.Language;
import perseus.language.LanguageAdapter;
import perseus.morph.Lemma;
import perseus.morph.Parse;
import perseus.util.DisplayPreferences;
import perseus.util.GreekEncodingAnalyzer;
import perseus.util.Timekeeper;
import perseus.voting.FormInstance;
import perseus.voting.VoteManager;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

public class MorphController implements Controller {
	private static Logger logger = Logger.getLogger(MorphController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper tk = new Timekeeper(request.getParameter("l") + ": " + request.getHeader("User-Agent"));
		tk.start();
		tk.record("Starting analysis");

		request.setCharacterEncoding("UTF-8");
		DisplayPreferences prefs = new DisplayPreferences(request, response);
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel prefsStatics = (TemplateHashModel) staticModels.get("perseus.util.DisplayPreferences");

		tk.record("Got preferences");
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("prefs", prefs);
		myModel.put("prefsStatics", prefsStatics);
		myModel.put("url", request.getRequestURI()+"?"+request.getQueryString());
		myModel.put("languages", Language.getDefinitionLookupLanguages());
		
		String word = request.getParameter("l");
		if (word == null || word.equals("")) {
			word = request.getParameter("lookup");
		}

		String languageCode = request.getParameter("la");
		if (languageCode == null || languageCode.equals("")) {
			languageCode = request.getParameter("lang");
		}
		Language language = Language.GREEK;
		if (languageCode != null && !languageCode.equals("")) {
			languageCode = languageCode.toLowerCase();
			language = Language.forCode(languageCode);
		}
		myModel.put("language", language);
		if (word == null || word.equals("")) {
			return new ModelAndView("morph", myModel);
		}
		
		//don't need to check that it isn't null or empty because we
		//wouldn't have gotten to this point otherwise
		//don't need this because user is only entering BetaCode
		word = URLDecoder.decode(word, "utf-8");
		word = new String(word.getBytes("8859_1"),"UTF-8");
		if (language.equals(Language.GREEK)) {
			word = GreekEncodingAnalyzer.transcode(word, "PerseusBetaCode");
		}

		LanguageAdapter adapter = LanguageAdapter.getLanguageAdapter(language);
		word = adapter.getLookupForm(word);

		tk.record("Got word: "+word);

		Renderer parseRenderer = new Renderer(language);
		parseRenderer.addLanguageTokenFilters(prefs);

		Query query = null;
		String documentID = request.getParameter("d");

		if (documentID != null) {
			query = new Query(documentID);
		}

		tk.record("Got query");

		String which = request.getParameter("i");
		FormInstance formInstance = null;
		Map<Parse, Integer> morphVotes = null;
		ParseSelector selector = null;
		ParseEvaluator priorFreqEvaluator = null;

		if (which != null) { // has the user looked up something in a context?
			formInstance = new FormInstance(query, word, Integer.parseInt(which));
			formInstance.setLanguageCode(language.getCode());
			morphVotes = VoteManager.getMorphVoteCounts(formInstance);

			selector = new ParseSelector();
			int languageID = language.getId();
			String priorForm = request.getParameter("prior");
			if (priorForm != null) {
				priorFreqEvaluator = new PriorFrequencyEvaluator(languageID, null, priorForm);
				priorFreqEvaluator.removeProperty("evaluatePrecedingLemma");
				priorFreqEvaluator.removeProperty("evaluateCurrentLemma");
				selector.addEvaluator(priorFreqEvaluator);
			}

			selector.addEvaluator(new UserVotesEvaluator(languageID, query, word));
			selector.addEvaluator(new FormFrequencyEvaluator(languageID));
			selector.addEvaluator(new WordFrequencyEvaluator(query.getDocumentID()));

			if (language.equals(Language.ARABIC)) {
				selector.addEvaluator(new ArabicVocalizationEvaluator(request.getParameter("l")));
			}
		}		
		tk.record("Checked if user looked something up in a context and added evaluators");

		// change word to lower case unless the language doesn't accept it
		String tempWord = adapter.matchCase() ? word : adapter.toLowerCase(word);
		//attempt to find word given the word exactly as typed
		Map<Lemma,List<Parse>> lemmaParses = Parse.getParses(tempWord, language.getCode(), false, false);

		// If we didn't find anything, check again, stripping and ignoring accents.
		tempWord = Lemma.BARE_WORD_PATTERN.matcher(word).replaceAll("");
		if (lemmaParses.isEmpty()) {
			lemmaParses = Parse.getParses(tempWord, language.getCode(), true, false);
		}
		tk.record("Got lemmas and parses");
		
		boolean hasMultipleParses = false;

		// Now check to see whether we received multiple parses.
		if (lemmaParses.size() > 1) {
			hasMultipleParses = true;
		} else {
			for (List<Parse> parsesFound : lemmaParses.values()) {
				if (parsesFound.size() > 1) {
					hasMultipleParses = true;
				}
			}
		}

		ParseVotingResults votingResults = null;
		Parse winningParse = null;
		boolean doVoting = false;
		NumberFormat voteFormat = null;

		if (query != null && hasMultipleParses) {
			votingResults = selector.evaluate(lemmaParses);
			winningParse = votingResults.getWinner();
			doVoting = true;

			voteFormat = NumberFormat.getPercentInstance();
			voteFormat.setMaximumFractionDigits(1);
		}

		tk.record("Got voting information");
		
		Map<String, Object[]> lemmaStats = null;
		
		if (query != null) {
			lemmaStats = new HashMap<String, Object[]>();
			
			HibernateFrequencyDAO freqDAO =	new HibernateFrequencyDAO(EntityDocumentFrequency.class);
			for (Lemma lemma : lemmaParses.keySet()) {
				List<Object[]> counts = freqDAO.getDocumentFrequenciesWithCounts(lemma, query.getDocumentID());
				// there should only be one result in this list
				if (!counts.isEmpty()) {
					lemmaStats.put(lemma.getAuthorityName(), counts.get(0));
				}
			}
		}
		
		tk.record("Got frequency statistics");
		
		Set<Query> allLexQueries = new HashSet<Query>();
		for (Lemma l : lemmaParses.keySet()) {
			allLexQueries.addAll(l.getLexiconQueries());
		}
		Map<String, Boolean> doSenseVoting = new HashMap<String, Boolean>();
		for (Query lexQuery : allLexQueries) {
			int numSenses = VoteManager.getNumSenses(lexQuery.getDocumentID(), lexQuery.getQuery());
			
			if (which != null && numSenses > 1) {
				doSenseVoting.put(lexQuery.toString(), true);
			}
			else {
				doSenseVoting.put(lexQuery.toString(), false);
			}
		}
		
		tk.record("Got sense information");

		// Freemarker doesn't do maps the same way java does, keys need to be strings for freemarker to work correctly
		// this is a pain, but must be done
		Set<Lemma> lemmas = lemmaParses.keySet();
		Map<String, List<Parse>> lemmaParsesByAuthName = new HashMap<String, List<Parse>>();
		Map<String, Integer> morphVotesByParse = new HashMap<String, Integer>();
		Map<String, String> votingResultsByParse = new HashMap<String, String>();		
		
		for (Lemma l : lemmaParses.keySet()) {
			lemmaParsesByAuthName.put(l.getAuthorityName(), lemmaParses.get(l));
			for (Parse p : lemmaParses.get(l)) {
				String key = p.toString()+p.getLemma().getAuthorityName();
				if (doVoting) {
					votingResultsByParse.put(key, voteFormat.format(votingResults.getTotalScore(p)));
					morphVotesByParse.put(key, morphVotes.get(p));	
				}
			}
		}
		Map<String, String> votingScoresByParse = new HashMap<String, String>();
		if (doVoting) {
			for (Parse parse : votingResults.getParses()) {
				for (ParseEvaluator evaluator : votingResults.getEvaluators()) {
					String key = parse.toString()+parse.getLemma().getAuthorityName()+evaluator.getDescription();
					double score = votingResults.getScore(parse, evaluator);
					votingScoresByParse.put(key, voteFormat.format(score));
				}
			}
		}

		myModel.put("word", word);
		myModel.put("parseRenderer", parseRenderer);
		myModel.put("lemmas", lemmas);
		myModel.put("lemmaParsesByAuthName", lemmaParsesByAuthName);
		myModel.put("query", query);
		myModel.put("which", which);
		myModel.put("doVoting", doVoting);
		myModel.put("winningParse", winningParse);
		myModel.put("votingResultsByParse", votingResultsByParse);
		myModel.put("morphVotesByParse", morphVotesByParse);
		myModel.put("hasMultipleParses", hasMultipleParses);
		myModel.put("lemmaStats", lemmaStats);
		myModel.put("allLexQueries", allLexQueries);
		myModel.put("doSenseVoting", doSenseVoting);
		myModel.put("votingResults", votingResults);
		myModel.put("votingScoresByParse", votingScoresByParse);
		
		tk.stop();
		logger.info(tk.getResults());

		return new ModelAndView("morph", myModel);
	}

}
