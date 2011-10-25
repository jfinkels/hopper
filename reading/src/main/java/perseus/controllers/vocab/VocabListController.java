/**
 * Handles display of vocabulary lists over entire documents.
 */
package perseus.controllers.vocab;

import static perseus.ie.freq.Frequency.FrequencyComparator.WEIGHTED_FREQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Chunk;
import perseus.document.ChunkSchemes;
import perseus.document.Metadata;
import perseus.document.MorphLinksTokenFilter;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateWordCountDAO;
import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.document.dao.WordCountDAO;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.Frequency;
import perseus.ie.freq.Frequency.FrequencyComparator;
import perseus.ie.freq.dao.FrequencyDAO;
import perseus.ie.freq.dao.HibernateFrequencyDAO;
import perseus.language.Language;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

public final class VocabListController implements Controller {
	private static Logger logger = Logger.getLogger(VocabListController.class);

	private static final double DEFAULT_CUTOFF = 0.5;
	
	private static List<Query> greekTexts;
	private static List<Query> latinTexts;
	
	//cache these lists because they don't change and what's the point of requesting them over and over?
	static {
		MetadataDAO mDAO = new SQLMetadataDAO();
		greekTexts = mDAO.getDocuments(Metadata.LANGUAGE_KEY, Language.GREEK.getAbbreviations(), true);
		latinTexts = mDAO.getDocuments(Metadata.LANGUAGE_KEY, Language.LATIN.getAbbreviations(), true);
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();
		Map<String, Object> myModel = new HashMap<String, Object>();

		String languageCode = request.getParameter("lang");
		if (languageCode == null) languageCode = Language.GREEK.getCode();

		// alpha, max, min, weighted, keyterm
		String sortMethod = request.getParameter("sort");
		if (sortMethod == null) sortMethod = "weighted_freq";

		// 100, 90, 80, ..., 10
		String percentFilter = request.getParameter("filt");

		boolean usingCustomFilter = false;
		if (percentFilter != null && percentFilter.equals("custom")) {
			percentFilter = request.getParameter("filt_custom");
			usingCustomFilter = true;
		}
		
		if (percentFilter == null) percentFilter = "50";
		double percentCutoff = DEFAULT_CUTOFF;

		if (percentFilter != null) {
			try {
				percentCutoff = Double.parseDouble(percentFilter)/100.0;
			} catch (NumberFormatException nfe) {
				//just go back to the default
				percentCutoff = DEFAULT_CUTOFF;
				percentFilter = "50";
			}			
		}
		
		//Figure out if we're requesting vocab for a subquery (ie using chunks)
		//and how the user is viewing results (text vs vocablist vs xmlvocab pages)
		boolean usingChunks = false;
		String chunksVocab = request.getParameter("usingChunks");
		if (chunksVocab != null && !chunksVocab.equals("")) {
			usingChunks = true;
		}
		//for vocablist page
		boolean fullPage = false;
		String fullPageString = request.getParameter("fullPage");
		if (fullPageString != null && !fullPageString.equals("")) {
			fullPage = true;
		}
		//for xmlvocab page
		boolean returnXML = false;
		String outputString = request.getParameter("output");
		if (outputString != null && outputString.equals("xml")) {
			returnXML = true;
		}
		myModel.put("usingChunks", usingChunks);

		String[] workIDs = request.getParameterValues("works");

		Language language = Language.forCode(languageCode);
		Query documentQuery = null;
		//change language to this document's language if using chunks, 
		//just to make sure we're using the correct language
		if (usingChunks) {
			documentQuery = new Query(workIDs[0]);
			language = documentQuery.getMetadata().getLanguage();
		}
		keeper.record("Got language, sort, filter, and works");

		DisplayPreferences prefs = new DisplayPreferences(request, response);
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel prefsStatics = (TemplateHashModel) staticModels.get("perseus.util.DisplayPreferences");
		
		myModel.put("language", language);
		myModel.put("languageList", Language.getMorphLanguages());
		myModel.put("prefs", prefs);
		myModel.put("prefsStatics", prefsStatics);
		myModel.put("url", request.getRequestURI()+"?"+request.getQueryString());
		myModel.put("sortMethod", sortMethod);
		myModel.put("percentFilter", percentFilter);
		myModel.put("usingCustomFilter", usingCustomFilter);		

		
		MetadataDAO mDAO = new SQLMetadataDAO();
		List<Query> texts;
		if (language.equals(Language.GREEK)) {
			texts = greekTexts;
		} else if (language.equals(Language.LATIN)) {
			texts = latinTexts;
		} else {
			texts = mDAO.getDocuments(Metadata.LANGUAGE_KEY, language.getAbbreviations(), true);
		}
		Collections.sort(texts, new Comparator<Query>() {
			public int compare(Query o1, Query o2) {
				return o1.getMetadata().compareTo(o2.getMetadata());
			}
		});
		myModel.put("texts", texts);
		keeper.record("Got texts for language "+language.getName());

		//user is viewing main vocablist page and has not requested any works
		if (workIDs == null || workIDs.length == 0) {
			keeper.stop();
			logger.info(keeper.getResults());
			return new ModelAndView("vocablist", myModel);
		}

		//determine sorter
		FrequencyComparator sorter = null;
		for (FrequencyComparator candidate : FrequencyComparator.values()) {
			if (candidate.name().equalsIgnoreCase(sortMethod)) {
				sorter = candidate;
				break;
			}
		}
		if (sorter == null) sorter = WEIGHTED_FREQ;
		keeper.record("Set sorter");

		FrequencyDAO dao = new HibernateFrequencyDAO(EntityDocumentFrequency.class);
		dao.beginTransaction();
		
		List<String> IDs = new ArrayList<String>();
		double cutoffFrequency = 0.0;
		
		//get list of chunk ids if using chunks, otherwise list of document ids
		if (usingChunks) {
			myModel.put("documentQuery", documentQuery);
			Chunk chunk = documentQuery.getChunk();

			int entityCount = dao.getTotalEntitiesForChunk(chunk);

			//if this chunk doesn't have any results, then hopefully it's contained chunks do
			//if not, then it's probably too small (like sections in book:chapter:section), and 
			//we'll tell the user to view a broader chunk
			if (entityCount == 0) {
				ChunkSchemes cs = chunk.getMetadata().getChunkSchemes();
				String type = cs.getNextTypeForChunk(chunk);
				if (type != null) {
					ChunkDAO cDAO = new HibernateChunkDAO();
					List<Chunk> chunks = cDAO.getContainedChunks(chunk, type);

					if (chunks.size() > 0) {
						for (Chunk c : chunks) {
							IDs.add(c.getId().toString());
						}
					}
				}
			} else {
				IDs.add(chunk.getId().toString());
			}
			keeper.record("Created list of chunk ids");
		} else {
			List<Query> workQueries = new ArrayList<Query>();
			long totalCount = 0;
			
			//if user requested document ids, then we also calculate some 
			//word counts in order to display useful information on the vocablist page
			WordCountDAO wcDAO = new HibernateWordCountDAO();
			for (String docID : workIDs) {
				//clean up document ID since we could be handling a redirect from P3
				docID = docID.replaceAll(",", ":");
				docID = new Query(docID).getDocumentID();
				
				totalCount += wcDAO.getCount(docID, language);
				// Add queries, in addition to document IDs, so we can get at metadata.
				IDs.add(docID);
				workQueries.add(new Query(docID));
			}
			keeper.record("Got total word count and created list of work queries and document ids");
			
			long uniqueCount = dao.getUniqueWordCount(IDs);
			long onlyOnceCount = dao.getSingleOccurrencesCount(IDs);
			keeper.record("Calculated unique word count, single occurrences count and vocab density");
		
			myModel.put("queryList", workQueries);
			myModel.put("totalCount", totalCount);
			myModel.put("uniqueCount", uniqueCount);
			myModel.put("onceCount", onlyOnceCount);
		}

		//now actually get frequencies if we don't have an empty ID list
		List<Frequency> frequencies = new ArrayList<Frequency>();
		if (!IDs.isEmpty()) {
			cutoffFrequency = ((HibernateFrequencyDAO) dao).getTotalWeightedFrequency(IDs, usingChunks)
				* percentCutoff;
			keeper.record("Got cutoff frequency");

			frequencies = ((HibernateFrequencyDAO) dao).getVocabularyList(IDs, cutoffFrequency, usingChunks);
			keeper.record("Got database results");
		}
		dao.endTransaction();

		Collections.sort(frequencies, sorter);
		keeper.record("Got sorted frequencies");
		
		myModel.put("frequencies", frequencies);

		Renderer renderer = new Renderer(language);
		renderer.addLanguageTokenFilters(prefs);
		renderer.addTokenFilter(new MorphLinksTokenFilter(languageCode));

		myModel.put("workIDs", IDs);

		myModel.put("renderer", renderer);

		keeper.stop();
		logger.info(keeper.getResults());

		String viewName = "vocablist";
		if (returnXML) {
			viewName = "xmlvocab";
			response.setContentType("text/xml;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin","*");
		} else if (usingChunks && !fullPage) {
			viewName = "vocabwgt";
		}
		return new ModelAndView(viewName, myModel);
	}
}
