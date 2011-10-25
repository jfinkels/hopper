package perseus.controllers.document;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.MorphLinksTokenFilter;
import perseus.document.Renderer;
import perseus.document.WordCount;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.dao.HibernateFrequencyDAO;
import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.morph.dao.HibernateParseDAO;
import perseus.morph.dao.ParseDAO;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

@SuppressWarnings("serial")
public class WordFreqController implements Controller {
    private static Logger logger = Logger.getLogger(WordFreqController.class);

	private static Map<String,CountComparator> comparators =
		new HashMap<String,CountComparator>() {{
			put("total", CountComparator.TOTAL);
			put("max", CountComparator.MAXIMUM);
			put("max10k", CountComparator.MAX_PER_10K);
			put("min", CountComparator.MINIMUM);
			put("min10k", CountComparator.MIN_PER_10K);
			put("name", CountComparator.NAME);	    
		}
	};

	public enum CountComparator implements Comparator {
		TOTAL {
			protected int doCompare(
				EntityDocumentFrequency f1, WordCount w1,
				EntityDocumentFrequency f2, WordCount w2) {
				
				return (int) (w1.getWordCount() - w2.getWordCount());
			}
		},
		MAXIMUM {
			protected int doCompare(
				EntityDocumentFrequency f1, WordCount w1,
				EntityDocumentFrequency f2, WordCount w2) {
				
				return (int) (f1.getMaxFrequency() - f2.getMaxFrequency());
			}
		},
		MAX_PER_10K {
			protected int doCompare(
				EntityDocumentFrequency f1, WordCount w1,
				EntityDocumentFrequency f2, WordCount w2) {

				return (int) (f1.getMaxPer10K(w1.getWordCount()) -
					f2.getMaxPer10K(w2.getWordCount()));
			}
		},
		MINIMUM {
			protected int doCompare(
				EntityDocumentFrequency f1, WordCount w1,
				EntityDocumentFrequency f2, WordCount w2) {
					
				return (int) (f1.getMaxFrequency() - f2.getMaxFrequency());
			}
		},
		MIN_PER_10K {
			protected int doCompare(
				EntityDocumentFrequency f1, WordCount w1,
				EntityDocumentFrequency f2, WordCount w2) {
				
				return (int) (f1.getMinPer10K(w1.getWordCount()) -
					f2.getMinPer10K(w2.getWordCount()));
			}
		},
		NAME {
			protected int doCompare(
				EntityDocumentFrequency f1, WordCount w1,
				EntityDocumentFrequency f2, WordCount w2) {
				
				return w1.getQuery().getMetadata().getTitle().compareTo(
						w2.getQuery().getMetadata().getTitle());
			}			
		};
		
		public int compare(Object a, Object b) {
			EntityDocumentFrequency f1 =
				(EntityDocumentFrequency) ((Object []) a)[0];
			WordCount w1 = (WordCount) ((Object []) a)[1];
			EntityDocumentFrequency f2 =
				(EntityDocumentFrequency) ((Object []) b)[0];
			WordCount w2 = (WordCount) ((Object []) b)[1];
			
			return doCompare(f1, w1, f2, w2);
		}
		
		protected abstract int doCompare(
			EntityDocumentFrequency f1, WordCount w1,
			EntityDocumentFrequency f2, WordCount w2);
	}

	public ModelAndView handleRequest(HttpServletRequest request,
	HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();
		Map<String, Object> myModel = new HashMap<String, Object>();

		String word = request.getParameter("lookup");
		if (word == null) {
			word = request.getParameter("entry");
		}
		myModel.put("word", word);

		String languageCode = request.getParameter("lang");
		if (languageCode != null) {
			languageCode = languageCode.toLowerCase();
		}
		Language language = Language.forCode(languageCode);
		if (language == Language.UNKNOWN) {
			language = Language.GREEK;
			languageCode = language.getCanonicalAbbreviation();
		}
		// Show only languages with morphological data
		Set<Language> languages = Language.getMorphLanguages();
		
		myModel.put("language", language);
		myModel.put("languages", languages);
		
		DisplayPreferences prefs = new DisplayPreferences(request, response);
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel prefsStatics = (TemplateHashModel) staticModels.get("perseus.util.DisplayPreferences");
		myModel.put("prefs", prefs);
		myModel.put("prefsStatics", prefsStatics);
		myModel.put("url", request.getRequestURI()+"?"+request.getQueryString());
		keeper.record("Got word, language and display preferences");
		
		if (word == null || word.equals("")) {
			keeper.stop();
			logger.info(keeper.getResults());

			return new ModelAndView("wordfreq", myModel);
		}

		String sortMethod = request.getParameter("sort");
		
		Renderer renderer = new Renderer(languageCode);
		renderer.addLanguageTokenFilters(prefs);
		renderer.addTokenFilter(new MorphLinksTokenFilter(languageCode));

		HibernateLemmaDAO dao = new HibernateLemmaDAO();
		Lemma lemma = dao.getMatchingLemma(word, languageCode, false);
		keeper.record("Got matching lemma");
		
		if (lemma == null) {
			// see if the word we've been given is actually a form; if it is,
			// find the first matching lemma
			ParseDAO parseDAO = new HibernateParseDAO();
			
			List<Lemma> matches = parseDAO.getLemmasByForm(word, language);
			if (!matches.isEmpty()) { 
				lemma = matches.get(0);
				myModel.put("originalForm", word);
				myModel.put("displayOriginalForm", renderer.render(word));
			}
			keeper.record("Got parse because lemma is null");
		}
		
		if (lemma != null) {			
			HibernateFrequencyDAO freqDAO =
				new HibernateFrequencyDAO(EntityDocumentFrequency.class);

			List<Object[]> counts = freqDAO.getDocumentFrequenciesWithCounts(lemma);
			CountComparator comparator = comparators.containsKey(sortMethod) ?
				comparators.get(sortMethod) : CountComparator.TOTAL;

			Collections.sort(counts, comparator);
			if (comparator != CountComparator.NAME) Collections.reverse(counts);

			myModel.put("frequencyCounts", counts);
			myModel.put("displayWord", renderer.render(lemma.getHeadword()));
			myModel.put("lemma", lemma);	
			keeper.record("Got frequency counts");
		}
		
		boolean isStopWord = false;
		if (language.getStoplist().contains(word)) {
			isStopWord = true;
		}
		keeper.record("Checked if word is stop word");
		
		myModel.put("sortMethod", sortMethod);
		myModel.put("isStopWord", isStopWord);
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("wordfreq", myModel);
	}
}
