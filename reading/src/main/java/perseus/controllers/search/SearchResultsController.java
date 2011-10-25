/**
 * Handles search requests. May forward the viewer to a text page or an A&A
 * page if the search term resembles a term/query.
 * 
 * Valid parameters:
 * 
 * <dl>
 * <dt>q</dt><dd>the term to search for (only used on pages other than advancedsearch)</dd>
 * <dt>language</dt><dd>the target language (only applies to text searches)</dd>
 * <dt>page</dt><dd>the page number; defaults to 1</dd>
 * <dt>all_words</dt><dd>a space-separated list of words, all of which must appear in results</dd>
 * <dt>all_words_expand</dt><dd>whether to expand the contents of all_words to all possible forms</dd>
 * <dt>phrase</dt><dd>a phrase that must appear in the results</dd>
 * <dt>any_words</dt><dd>a space-separated list of words, all of which must appear in results</dd>
 * <dt>any_words_expand</dt><dd>whether to expand any_words</dd>
 * <dt>exclude_words</dt><dd>a space-separated list of words, all of which must be absent from results</dd>
 * <dt>exclude_words_expand</dt><dd>whether to expand exclude_words</dd>
 * <dt>documents</dt><dd>documents to restrict the search to</dd>
 * <dt>aggregate</dt><dd>how to display the results</dd>
 * </dl>
 * 
 * 
 */

package perseus.controllers.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import perseus.artarch.image.SimpleImg;
import perseus.display.URLBuilder;
import perseus.document.Corpus;
import perseus.document.Metadata;
import perseus.document.MorphLinksTokenFilter;
import perseus.document.NoTagsTokenFilter;
import perseus.document.PhraseSearchResultsTokenFilter;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.SearchResultsTokenFilter;
import perseus.document.TokenList;
import perseus.language.Language;
import perseus.language.LanguageCode;
import perseus.morph.Lemmatizer;
import perseus.search.LexiconSearcher;
import perseus.search.nu.ArtifactSearcher;
import perseus.search.nu.CorpusSearcher;
import perseus.search.nu.ImgSearcher;
import perseus.search.nu.LemmaSearchResult;
import perseus.search.nu.LemmaSearcher;
import perseus.search.nu.MetadataSearcher;
import perseus.search.nu.SearchResult;
import perseus.search.nu.SearchResults;
import perseus.search.nu.ArtifactSearcher.ArtifactSearchResults;
import perseus.search.nu.CorpusSearcher.CorpusSearchResult;
import perseus.util.Config;
import perseus.util.DisplayPreferences;
import perseus.util.HibernateUtil;
import perseus.util.Range;
import perseus.util.Timekeeper;
import perseus.search.nu.MetadataSearcher.MetadataSearchResults;

public class SearchResultsController implements Controller {

	// Maximum results per page
    private static final int PAGE_SIZE = 10;

    private static Logger logger = Logger.getLogger(SearchResultsController.class);

    public ModelAndView handleRequest(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Timekeeper keeper = new Timekeeper();
	keeper.start();

	keeper.record("Beginning search result processing");
	// First, see if we've been passed a citation
	// TODO make this into a filter of some sort!
	String queryParam = request.getParameter("q");
	if (queryParam != null) {
	    Query citationQuery = new Query(queryParam);
	    if (citationQuery.getDocumentID() != null) {
	    	String url = "text";
	    	if (request.getParameter("redirect") != null && request.getParameter("redirect").equalsIgnoreCase("true")) {
	    		url = "text?redirect=true";
	    	}
		return new ModelAndView(new RedirectView(url),
			"doc", citationQuery.toString());
	    }
	}
	keeper.record("Handled possible redirection");

	String languageCode = request.getParameter("language");
	if (languageCode == null || languageCode.equals("")) languageCode = request.getParameter("target");
	if (languageCode == null || languageCode.equals("")) languageCode = request.getParameter("la");
	// carry over from P3, which uses 'lang' as a parameter
	if (languageCode == null || languageCode.equals("")) languageCode = request.getParameter("lang");
	// carry over from P3, which sometimes capitalizes the language - probably should be done in the Language class
	if (languageCode != null) {
		languageCode = languageCode.toLowerCase();
		//for some reason the value in the language param has extra junk, just a quick check to see if we can make it better
		int index = languageCode.indexOf(',');
		if (index != -1) {
			languageCode = languageCode.substring(0, index);
		}
	}
	Language language = Language.forCode(languageCode);
	if (language == Language.UNKNOWN) {
	    language = Language.forCode(LanguageCode.ENGLISH);
	}

	int page = 1;
	String pageParam = request.getParameter("page");
	if (pageParam != null) {
	    try {
		page = Integer.parseInt(pageParam);
		if (page <= 0) page = 1;
	    } catch (NumberFormatException nfe) {
	    }
	}
	keeper.record("Got language/page: "+language.toString()+ "/"+page);

	DisplayPreferences prefs = new DisplayPreferences(request, response);
	keeper.record("Created display prefs");
	
	// If the user entered multiple allowed words, use the first one to
	// search other categories.
	if (queryParam == null || queryParam.equals("")) { 
	    queryParam = request.getParameter("all_words");
	}
	if (queryParam == null || queryParam.equals("")) {
	    queryParam = request.getParameter("any_words");
	}
	keeper.record("Searching for: "+queryParam);
	
	String inContent = request.getParameter("inContent");
	
	SearchResults metadataSearchResults = null;
	List<Metadata> metadataResults = new ArrayList<Metadata>();
	if (queryParam != null) {
	    MetadataSearcher ms = new MetadataSearcher();
	    metadataSearchResults = ms.search(queryParam, Range.range(0, -1));
	    keeper.record("Got metadata results: "+metadataSearchResults.getTotalHitCount());
	    
	    for (String field : ((MetadataSearchResults) metadataSearchResults).getMatchingFields()) {
	    	for (Query match : ((MetadataSearchResults) metadataSearchResults).getMatchesByField(field)) {
	    		metadataResults.add(match.getMetadata());
	    	}
	    }
	    Collections.sort(metadataResults);
	}
	
	ModelMap myModel = new ModelMap();

	if ((language == Language.ENGLISH || language == Language.LATIN) && metadataResults.size() > 0 && inContent == null) {
		myModel.addObject("metadataSearch", true);
		myModel.addObject("language", language);
	} else {
		myModel.addObject("metadataSearch", false);
		
		CorpusSearcher.CorpusSearchResults corpusResults =
			getCorpusResults(request, prefs, language, page, keeper);
		keeper.record("Got corpus results: "+corpusResults.getTotalHitCount());
		// now create a hash mapping document IDs to queries, since FreeMarker
		// doesn't allow non-string keys...
		Map<String,Query> idsToQueries = new HashMap<String,Query>();
		for (String documentID : corpusResults.getDocumentFrequencies().keySet()) {
			idsToQueries.put(documentID, new Query(documentID));
		}
		
		/*
		 * Search Results Processing Tasks:
		 *
		 * 1.  Create a List of similar results
		 * 2.  Create a List of all the lists
		 * 3.  Iterate through the results and populate the lists
		 * 4.  Add the superlist to the myModel hash
		 * 5.  Iterate through and print out the superlist in freemarker
		 */									

		List<CorpusSearchResult> sameDocIDResults = new ArrayList<CorpusSearchResult>();
		List<List<CorpusSearchResult>> allResults = new ArrayList<List<CorpusSearchResult>>();
		 	
		// Get metadata for each document in collection
		Iterator<CorpusSearchResult> idIterator = corpusResults.getHits().iterator();

		while (idIterator.hasNext()) {
			CorpusSearchResult searchResult = ((CorpusSearchResult)idIterator.next());
			Query currentQuery = searchResult.getQuery();
			if (sameDocIDResults.size() > 0) {
				   	   
				Query previousQuery = sameDocIDResults.get(0).getQuery();
				  
				if (previousQuery.getInnermostDocumentID().equals(currentQuery.getInnermostDocumentID())) {
				 	sameDocIDResults.add(searchResult);
				}
				else {
					allResults.add(sameDocIDResults);
					sameDocIDResults = new ArrayList<CorpusSearchResult>();
					sameDocIDResults.add(searchResult);    
				}
			} else {
				sameDocIDResults.add(searchResult);
			}
		}

		allResults.add(sameDocIDResults);
		/* End Search Result Processing */
		
		myModel.addObject("corpusResults", corpusResults);
		myModel.addObject("idsToQueries", idsToQueries);

		// Find the first ten results for any of these, regardless
		Range<Integer> widgetRange = Range.range(0, 10);

		SearchResults entityResults = null;
		/*if (queryParam != null && language == Language.ENGLISH) {
	    EntitySearcher es = new EntitySearcher();
	    entityResults = es.search(queryParam, widgetRange);
	    keeper.record("Got entity results: "+entityResults.getTotalHitCount());
	}*/

		SearchResults lemmaResults = null;
		if (language.getHasMorphData()) {
			LemmaSearcher ls = new LemmaSearcher();

			List<Language> languages = new ArrayList<Language>();
			languages.add(language);
			ls.setTargetLanguages(languages);
			lemmaResults = ls.search(queryParam, widgetRange);
			keeper.record("Got lemma results: "+lemmaResults.getTotalHitCount());

			for (Object result : lemmaResults) {
				LemmaSearchResult lsr =
					(LemmaSearchResult) result;

				Renderer formRenderer =
					new Renderer(lsr.getContent().getLanguageCode());
				formRenderer.addLanguageTokenFilters(prefs);
				lsr.setTitle(formRenderer.render(lsr.getTitle()));
			}
			keeper.record("Rendered lemma results");
		}

		SearchResults lexiconResults = null;
		if (language.getCode().equals(LanguageCode.ENGLISH)) {
			try {
				LexiconSearcher ls = new LexiconSearcher();
				lexiconResults = ls.search(queryParam, widgetRange);
				keeper.record("Got lexicon results: "+ lexiconResults.getTotalHitCount());

				for (Object result : lexiconResults) {
					LemmaSearchResult lemmaSR =
						(LemmaSearchResult) result;
					Renderer formRenderer =
						new Renderer(lemmaSR.getContent().getLanguageCode());
					formRenderer.addLanguageTokenFilters(prefs);
					lemmaSR.setTitle(formRenderer.render(lemmaSR.getTitle()));
				}
				keeper.record("Rendered lexicon results");
			} catch (Exception e) {
				logger.warn("Problem searching lexica", e);
			}
		}

		ArtifactSearchResults artifactResults = null;
		if (queryParam != null) {
			ArtifactSearcher as = new ArtifactSearcher();
			artifactResults = (ArtifactSearchResults) as.search(queryParam, widgetRange);
			keeper.record("Got artifact results: "+artifactResults.getTotalHitCount());
		}

		SearchResults imgResults = null;
		if (queryParam != null) {
			ImgSearcher is = new ImgSearcher();
			imgResults = is.search(queryParam, Range.range(0, 2));
			for (Object result : imgResults.getHits()) {
				SimpleImg img = (SimpleImg) ((SearchResult) result).getContent();
				img.setIsRestricted(request);
			}
			keeper.record("Got image results: "+imgResults.getTotalHitCount());
		}
		
		if (entityResults != null) {
		    myModel.addObject("entityResults", entityResults);
		}

		if (lemmaResults != null) {
		    myModel.addObject("lemmaResults", lemmaResults);
		}
		
		if (lexiconResults != null) {
		    myModel.addObject("lexiconResults", lexiconResults);
		}
		
		if (artifactResults != null) {
			myModel.addObject("artifactResults", artifactResults);
		}
		
		if (imgResults != null) {
			myModel.addObject("imgResults", imgResults);
		}
		
		if (allResults != null) {
			myModel.addObject("allResults", allResults);
		}
	}

	myModel.addObject("languages", Language.getSearchableLanguages());
	myModel.addObject("page", new Integer(page));
	myModel.addObject("pagesize", PAGE_SIZE);

	myModel.addObject("metadataResults", metadataResults);

	URLBuilder builder = URLBuilder.fromRequest(request, null);
	myModel.addObject("searchTerm", queryParam);
	myModel.addObject("builder", builder);
	myModel.addObject("prefs", prefs);

	ModelAndView mv = new ModelAndView();
	mv.addAllObjects(myModel);

	keeper.record("Added objects to model");

	response.setCharacterEncoding("utf-8");

	HibernateUtil.closeSession();

	keeper.record("Closed session");
	keeper.stop();
	logger.info(keeper.getResults());

	return mv;
    }

    private CorpusSearcher.CorpusSearchResults getCorpusResults(
	    HttpServletRequest request, DisplayPreferences prefs,
	    Language language, int page, Timekeeper keeper) {

	CorpusSearcher searcher = new CorpusSearcher();

	searcher.setLanguage(language);

	if (request.getParameter("all_words") != null) {
	    searcher.setRequiredWords(getWordList(request, "all_words"));
	} else if (request.getParameter("q") != null) {
	    if (request.getParameter("expand") != null &&
		    request.getParameter("expand").equals("lemma")) {
		// Special case for links from word-frequency pages
		String lemma = request.getParameter("q");

		searcher.setAllowedWords(new ArrayList<String>(
			    Lemmatizer.getAllForms(lemma, request.getParameter("target"))));
	    } else {
		searcher.setRequiredWords(getWordList(request, "q"));
	    }
	}
	if (request.getParameter("all_words_expand") != null ||
		request.getParameter("expand") != null) {
	    searcher.setExpandRequiredWords(true);
	}

	String phrase = request.getParameter("phrase");
	if (phrase != null && phrase.trim().length() > 0) {
	    searcher.setRequiredPhrase(phrase);
	}

	if (searcher.getAllowedWords().isEmpty()) {
	    searcher.setAllowedWords(getWordList(request, "any_words"));
	    if (request.getParameter("any_words_expand") != null) {
		searcher.setExpandAllowedWords(true);
	    }
	}

	searcher.setExcludedWords(getWordList(request, "exclude_words"));
	if (request.getParameter("exclude_words_expand") != null) {
	    searcher.setExpandExcludedWords(true);
	}
	keeper.record("Set words options");

	if (request.getParameter("sort") != null &&
		request.getParameter("sort").equals("rel")) {
	    searcher.setSortByRelevance(true);
	}

	// P3 can have multiple values for 'doc' parameter
	Set<Query> targetDocs = new HashSet<Query>();
	String[] documentParams = request.getParameterValues("documents");
	if (documentParams == null || documentParams.length == 0) {
		documentParams = request.getParameterValues("doc");
	}
	if (documentParams != null && !documentParams[0].equals("")) {
		if (request.getParameter("searchAll") == null || request.getParameter("searchAll").equals("")) {
			for (String doc : documentParams) {
				//P3 has extra junk in the doc param that we can't use, try to get rid of it
				int index = doc.indexOf(';');
				if (index != -1) {
					doc = doc.substring(0,index);
				}
				Query query = new Query(doc);
				targetDocs.add(new Query(doc));
			}
		}
	}
	searcher.setTargetDocuments(targetDocs);

	Set<Corpus> targetCollections = new HashSet<Corpus>();
	String[] corpusParams = request.getParameterValues("collections");
	// carry over from P3, which uses collection as a parameter
	if (corpusParams == null || corpusParams.length == 0) {
		corpusParams = request.getParameterValues("collection");
	}
	if (corpusParams != null && !corpusParams[0].equals("")) {
	    for (String corpusID : corpusParams) {
		targetCollections.add(new Corpus(corpusID));
	    }
	}
	if (targetCollections.isEmpty()) {
	    String[] matchingCollections = Config.getPrimaryCollections();
	    for (String matchingCollection : matchingCollections) {
	    	targetCollections.add(new Corpus(matchingCollection));
	    }
	}
	searcher.setTargetCollections(targetCollections);
	keeper.record("Set doc and collections options");

	Range<Integer> range =
	    Range.range((page-1) * PAGE_SIZE, page * PAGE_SIZE);
	CorpusSearcher.CorpusSearchResults results =
	    (CorpusSearcher.CorpusSearchResults) searcher.search(null, range);
	keeper.record("Got initial search results");

    Renderer renderer = new Renderer(language.getCode());
    renderer.addTokenFilter(new NoTagsTokenFilter());
	renderer.addLanguageTokenFilters(prefs);
    
    if (language.getHasMorphData()) {
	renderer.addTokenFilter(
		new MorphLinksTokenFilter(language.getCode()));
    }

    Set<String> matchingTerms = searcher.getPositiveTerms();
    String matchString = "";
    // If there are matching non-phrase terms, the phrase-filter
    // should keep its context; otherwise, it shouldn't.
    if (searcher.getRequiredPhrase() != null) {
	renderer.addTokenFilter(new PhraseSearchResultsTokenFilter(
		    searcher.getRequiredPhrase(),
		    !matchingTerms.isEmpty()));

	matchString = searcher.getRequiredPhrase() + ",";
    }

    SearchResultsTokenFilter searchFilter = null;
    if (!matchingTerms.isEmpty()) {
	searchFilter = new SearchResultsTokenFilter(matchingTerms);
	renderer.addTokenFilter(searchFilter);
    }
	keeper.record("Set renderer options");
	
	// Now we need to render the results...
	for (CorpusSearcher.CorpusSearchResult result : results) {
	    String resultText = result.getContent();

	    TokenList tokens = TokenList.getTokens(resultText, language);
	    result.setRenderedText(renderer.render(tokens));

	    try {
		if (searchFilter != null) {
		    matchString = searchFilter.matchingTokensAsString();
		    result.setTotalMatchCount(searchFilter.getTotalMatchCount());
		    result.setHighlightedMatchCount(searchFilter.getHighlightedMatchCount());
		}
		result.setMatchingTokenString(URLEncoder.encode(matchString,"utf-8"));
	    } catch (UnsupportedEncodingException e) {
		result.setMatchingTokenString("");
	    }

	}
	keeper.record("Rendered results");

	return results;
    }

    private List<String> getWordList(
	    HttpServletRequest req, String paramName) {
	String value = req.getParameter(paramName);
	if (value != null && value.trim().length() > 0)  {
	    // users may have tried to enter commas...destroy them
	    String[] tokens = value.trim().replaceAll(",", "").split("\\s+");
	    return Arrays.asList(tokens);
	}

	return Collections.emptyList();
    }

}
