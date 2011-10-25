package perseus.controllers.document;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import perseus.document.Chunk;
import perseus.document.ChunkSchemes;
import perseus.document.InvalidQueryException;
import perseus.document.Metadata;
import perseus.document.MetadataCache;
import perseus.document.MorphLinksTokenFilter;
import perseus.document.NoTagsTokenFilter;
import perseus.document.PhraseSearchResultsTokenFilter;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.Resource;
import perseus.document.SearchResultsTokenFilter;
import perseus.document.StyleTransformer;
import perseus.document.TableOfContents;
import perseus.document.TokenList;
import perseus.document.Resource.ResourceType;
import perseus.ie.CitationFetcher;
import perseus.ie.entity.HibernateEntityManager;
import perseus.language.Language;
import perseus.sharing.Partner;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

public class TextController implements Controller {
    private static Logger logger = Logger.getLogger(TextController.class);
    
    private static final Pattern LEXICON_ENTRY_PATTERN = Pattern.compile("entry=#(\\d+)");

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper(request.getRequestURI() + ": " + request.getHeader("User-Agent"));
		keeper.start();
		Map<String, Object> myModel = new HashMap<String, Object>();
		
		DisplayPreferences prefs = new DisplayPreferences(request, response);
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel prefsStatics = (TemplateHashModel) staticModels.get("perseus.util.DisplayPreferences");
		
		myModel.put("prefs", prefs);
		myModel.put("prefsStatics", prefsStatics);
		myModel.put("url", request.getRequestURI()+"?"+request.getQueryString());

		// This is the primary parameter that specifies which document to load
		String document = request.getParameter("doc");
		if (document != null) {
			//Carry over from P3, remove 'toc' from the parameter
			int index = document.indexOf("toc");
			if (index != -1) {
				document = document.substring(0,index);
			}
			Matcher matcher = LEXICON_ENTRY_PATTERN.matcher(document);
			if (matcher.find()) {
				String entryValue = matcher.group(1);
				int entryNum = Integer.valueOf(entryValue);
				entryNum--;
				document = document.replaceFirst("entry=#\\d+", "id=n"+String.valueOf(entryNum));
			}

		    document = URLDecoder.decode(document, "utf-8");
		}
		
		// bit of a hack to redirect users if they try to view a DDBDP page, since
		// we no longer host those texts
		if (document.contains("Perseus:text:1999.05")) {
			return new ModelAndView(new RedirectView("/hopper/help/ddbdp.jsp"));
		}

		// Document context: This is used in the "go to" box at the top of the
		//   text page. For internal queries, we want users to be able to jump
		//   to different parts of the current document using standard abbreviations

		Query context = null;
		if (request.getParameter("fromdoc") != null) {
		    context = new Query(URLDecoder.decode(request.getParameter("fromdoc"), "utf-8"));
		}

		// When browsing through a lexicon, we want to let the user browse through
		// entry groups without changing the current entry. Therefore we keep a
		// separate, parallel table of contents point as well as the regular point
		// in the document that is actually displayed

		String tocLocation = request.getParameter("toc");

		keeper.record("Starting " + document);

		// GET INITIAL QUERY

		// This step resolves work references (ABOs) and "jump box" queries within
		// documents. For work references with multiple expressions and no further
		// hints as to which one is desired, apply the language preference 
		// (translation/original).

		Query originalQuery = new Query(document, prefs, context);

		keeper.record("Resolved possible query abstraction");

		// SEARCH RESULTS HANDLING

		// If we're accessing the text from a search results page, highlight the
		// words the user searched for. If the user searched for a phrase,
		// highlight any occurrences of the whole phrase.

		String[] highlightedWords = null;
		Set<String> searchTerms = null;
		String highlighted = request.getParameter("highlight");
		String searchPhrase = null;

		if (highlighted != null) {
		    if (highlighted.startsWith("\"") && highlighted.endsWith("\"")) {
			searchPhrase = highlighted;
		    } else {
			highlightedWords = highlighted.split(",");
			searchTerms = new HashSet<String>();

			for (int i = 0; i < highlightedWords.length; i++) {
			    searchTerms.add(highlightedWords[i]);
			}
		    }
		}

		keeper.record("Applied search filtering");

		// NON-DEFAULT CHUNK SCHEME PROCESSING

		String userScheme = prefs.getDefaultChunkScheme();
		String userType = prefs.getDefaultChunkType();
		if (userScheme != null && 
		    originalQuery.getMetadata().getChunkSchemes().getSchemes().indexOf(userScheme) != -1) {
		    originalQuery.setTargetScheme(userScheme);

		    if (userType != null && userScheme.indexOf(userType) != -1) {
			originalQuery.setTargetType(userType);
		    }
		}

		// Before we try to load a chunk, find the metadata and see if
		// this document is actually at an affiliated library.

		Metadata metadata = originalQuery.getMetadata();
		if (metadata != null) {
		    if (metadata.has(Metadata.PARTNER_ID_KEY)) {
		        Partner partner = Partner.getPartner(metadata.get(Metadata.PARTNER_ID_KEY));
		        response.sendRedirect(partner.getTextURL(originalQuery));
		        return null;
		    }
		    if (metadata.has(Metadata.EXTERNAL_URL_KEY)) {
		        response.sendRedirect(metadata.get(Metadata.EXTERNAL_URL_KEY));
		        return null;
		    }

		    if ("collection".equals(metadata.getType())) {
		        response.sendRedirect("collection?collection=" + originalQuery);
		        return null;
		    }
		}

		keeper.record("Examined metadata");

		Chunk chunk = null;
		int MAX_CHUNK_SIZE = 250000;
		boolean truncateChunk = false;
		String force = request.getParameter("force");
		try {
			chunk = originalQuery.getChunk();
		    int chunkSize = chunk.getSize();
		    //Truncate the text of chunk greater than a reasonable size, unless forced to load
		    //something bigger
		    if (chunkSize > MAX_CHUNK_SIZE && force == null) {
		       truncateChunk = true;
		    }
		} catch (InvalidQueryException iqe) {
		    // Make a final effort to recover in one special case: if the user has
		    // entered a word into a jump box while viewing a lexicon, it may be an
		    // inflected form rather than a lemma. Parse it and find the query for the
		    // actual entry.
		    String lookupWord = originalQuery.getLastElementValue();
		    String language = originalQuery.getMetadata().get(Metadata.SUBJECT_LANGUAGE_KEY);
		} catch (NullPointerException ne) {
			// redirects from P3 may not map to anything in P4, causing a null pointer exception, send them to the home page
			if (request.getParameter("redirect") != null && request.getParameter("redirect").equalsIgnoreCase("true")) {
				return new ModelAndView(new RedirectView("/hopper?redirect=true"));
			}
		}
		myModel.put("truncateChunk", truncateChunk);
		myModel.put("force", force);
		
		if (chunk == null) {
			response.sendRedirect("invalidquery.jsp?doc="+originalQuery.toString());
		    return null;
		}

		Query query = chunk.getQuery();
		
		// Reload metadata, which may have changed during chunk lookup
		metadata = MetadataCache.get(query);
		myModel.put("query", query);
		myModel.put("metadata", metadata);

		keeper.record("Loaded chunk and associated metadata");
		
		//-----------------HEADER---------------------------//

		// Style the header
		String head = "<TEI.2><text><body>" + chunk.getContainingHeads() + "</body></text></TEI.2>";
		Map<String,String> headLangParam = new HashMap<String,String>();
		headLangParam.put("lang", chunk.getHeadLanguage());
		
		String styledHead = StyleTransformer.transform(head, metadata, headLangParam);
		TokenList headTokens = TokenList.getTokens(styledHead, metadata.getLanguage());
		Renderer headRenderer = new Renderer();
		headRenderer.addLanguageTokenFilters(prefs);
		headRenderer.addTokenFilter(new NoTagsTokenFilter());

		head = headRenderer.render(headTokens);
		myModel.put("head", head);
		keeper.record("Rendered head");
		
		//-------------------TOC-------------------------//
		
		Query tocQuery = (tocLocation != null) ? new Query(tocLocation) : query;
		Chunk tocChunk = tocQuery.getChunk();

		keeper.record("Got TOC chunk: " + tocChunk);
		TableOfContents toc = TableOfContents.forChunk(tocChunk, tocChunk.getQuery().getTargetScheme());

		keeper.record("Got TOC: " + toc);

		Renderer tocRenderer = new Renderer(chunk.getEffectiveLanguage());
		tocRenderer.addLanguageTokenFilters(prefs);

		// The side-TOC can use this later
		String tocXML = toc.getXML(tocChunk);
		String renderedTocXML = tocRenderer.renderText(tocXML);
		keeper.record("Tokenized/rendered TOC XML");

		//-------------------NAVBAR-------------------------//
		
		Map<String, String> navbarParameters = new HashMap<String, String>();
		navbarParameters.put("smoothWidths", "true");
		if (chunk.getType() != null) {
			navbarParameters.put("selectedType", chunk.getType());
		}

		navbarParameters.put("document_id", query.getDocumentID());
		navbarParameters.put("subquery", query.getQuery());
		navbarParameters.put("startOffset",
		    Integer.toString(tocChunk.getStartOffset()));
		navbarParameters.put("endOffset",
		    Integer.toString(tocChunk.getEndOffset()));

		String styledNavbar = StyleTransformer.transform(
			renderedTocXML, "navbar.xsl", "", navbarParameters);
		keeper.record("Styled TOC XML for navbar");
		myModel.put("navbar", styledNavbar);
		
		//-------------------COLLECTIONS-------------------------//

		List<String> collectionList = metadata.getList(Metadata.CORPUS_KEY);
		List<Query> collections = new ArrayList<Query>();
		if (collectionList != null) {
			for (String coll : collectionList) {
				collections.add(new Query(coll));
			}
		}
		myModel.put("collections", collections);
		keeper.record("Got associated collections");
		
		//------------------CHUNK SCHEMES--------------------------//

		boolean displayChunkSchemes = true;
		List<String> displayableSchemes = metadata.getChunkSchemes().getDisplayableSchemes();
		Map<String, List<String>> chunkSchemes = new TreeMap<String, List<String>>();
		Map<String, String> displayNames = new HashMap<String, String>();
		if (displayableSchemes.size() == 1) {
			String scheme = (String) displayableSchemes.get(0);

			if (scheme.indexOf(":") == -1) {
				displayChunkSchemes = false;
			}
		}

		for (String dispScheme : displayableSchemes) {
			List<String> types = ChunkSchemes.getChunks(dispScheme);
			chunkSchemes.put(dispScheme, types);
			for (String type : types) {
				displayNames.put(type, ChunkSchemes.getDisplayName(type));
			}
		}
		myModel.put("displayChunkSchemes", displayChunkSchemes);
		myModel.put("chunkSchemes", chunkSchemes);
		myModel.put("displayNames", displayNames);
		keeper.record("Got chunk schemes");
		
		//-------------------SIDE TOC-------------------------//
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("subquery", query.getQuery());
		parameters.put("tocSubquery", tocQuery.getQuery());
		parameters.put("scheme", query.getTargetScheme());
		parameters.put("startOffset", Integer.toString(tocChunk.getStartOffset()));
		parameters.put("endOffset", Integer.toString(tocChunk.getEndOffset()));

		String styledToc = StyleTransformer.transform(renderedTocXML, "sidetoc.xsl",
			query.getDocumentID(), parameters);

		myModel.put("styledToc", styledToc);
		keeper.record("Fetched and transformed side table of contents");

		//------------------PREVIOUS/NEXT CHUNKS--------------------------//
		
		Chunk matchingChunk = toc.getByQuery(query);
		Chunk previousChunk = toc.getPreviousChunk(matchingChunk);
		Chunk nextChunk = toc.getNextChunk(matchingChunk);
		
		myModel.put("matchingChunk", matchingChunk);
		myModel.put("previousChunk", previousChunk);
		myModel.put("nextChunk", nextChunk);
		
		keeper.record("Got matching, previous and next chunks");
		
		//-------------------CHUNK TEXT-------------------------//
		
		MorphLinksTokenFilter morphFilter = new MorphLinksTokenFilter(chunk);

		String chunkLanguage = chunk.getEffectiveLanguage();
		Language lang = Language.forCode(chunkLanguage);
		myModel.put("lang", lang);
		
		Renderer renderer = new Renderer(chunkLanguage);
		renderer.addLanguageTokenFilters(prefs);
		renderer.addTokenFilter(morphFilter);

		// Add the appropriate type of search-results filter if we need to
		if (searchTerms != null) {
		    renderer.addTokenFilter(new SearchResultsTokenFilter(searchTerms, true));
		} else if (searchPhrase != null) {
		    renderer.addTokenFilter(new PhraseSearchResultsTokenFilter(searchPhrase, true));
		}

		keeper.record("Added filters to renderer");
		
		Map<String, String> textStyleParams = new HashMap<String, String>();
		if (request.getParameter("highlightauth") != null) {
		    textStyleParams.put("highlight_authname", request.getParameter("highlightauth"));
		}

		chunk.style(textStyleParams);

		keeper.record("Text: Applied XSL transform to text");
		
		String renderedTokens = renderer.renderChunk(chunk);
		int fullTextLength = renderedTokens.length();
		int shortTextLength = fullTextLength;
		int percentOfText = 100;

		if (truncateChunk && force == null) {
			String shorterInnerText = chunk.truncateText();
			renderedTokens = renderer.renderText(shorterInnerText);
			shortTextLength = renderedTokens.length();   
			percentOfText = (int)( ((float)shortTextLength / fullTextLength) * 100);
		}
		
		keeper.record("Text: Tokenized/rendered main text (length: "+ renderedTokens.length() + ")");
		// right justify arabic 
		boolean rightJustify = false;
		if (lang == Language.ARABIC && prefs.get(DisplayPreferences.ARABIC_DISPLAY_KEY).equals("UnicodeC")) {
			rightJustify = true;
		}
		
		myModel.put("rightJustify", rightJustify);
		myModel.put("percentOfText", percentOfText);
		myModel.put("renderedTokens", renderedTokens);
		
		
		//if user is looking at a lexicon, add a Dictionary Entry Lookup box
		boolean isLexicon = false;
		if (chunk.getLemma() != null) {
			isLexicon = true;
			myModel.put("lookupLangs", Language.getResolveFormLanguages());
			myModel.put("lexLang", chunk.getLemma().getLanguage());
		}
		myModel.put("isLexicon", isLexicon);

		//------------------RESOURCES--------------------------//

	    // start at one because we've already added main text to documents array
		int resourcesDisplayed = 1;
		Map<ResourceType,Set<Chunk>> resourcesByType = Resource.getResources(chunk);
		Map<String, Set<Chunk>> resources = new TreeMap<String, Set<Chunk>>();
		Map<String, String> formattedResourceChunks = new HashMap<String, String>();
		for (ResourceType type : resourcesByType.keySet()) {
			resources.put(type.toString(), resourcesByType.get(type));
			for (Chunk resource : resourcesByType.get(type)) {
				formattedResourceChunks.put(resource.toString(), type.format(resource));
				resourcesDisplayed++;
			}
		}

		myModel.put("resources", resources);
		myModel.put("formattedResourceChunks", formattedResourceChunks);
		keeper.record("Got resources for this chunk");

		//-------------------ENTITIES-------------------------//

		String sortMethod = prefs.get(DisplayPreferences.IE_SORT_KEY);
		String sortOrder = HibernateEntityManager.ASCENDING;
		if (sortMethod == null || sortMethod.equals("freq")) {
			sortMethod = HibernateEntityManager.COUNT;
			sortOrder = HibernateEntityManager.DESCENDING;
		} else if (sortMethod.equals("token")) {
			sortMethod = HibernateEntityManager.FIRST_POSITION;
		} else if (sortMethod.equals("display")) {
			sortMethod = HibernateEntityManager.SORTABLE_STRING;
		}

		keeper.record("About to begin entities");
		HibernateEntityManager manager = new HibernateEntityManager();
		manager.setSortMethod(sortMethod);
		manager.setSortOrder(sortOrder);
		keeper.record("Initialized entity manager");

		Map<String, List<Map<String,Object>>> entityCounts = manager.getAllEntityCounts(query.getDocumentID(), chunk.getStartOffset(), chunk.getEndOffset());
		boolean docHasPlaces = manager.docHasPlaces(query.getDocumentID());

		myModel.put("entityCounts", entityCounts);
		myModel.put("docHasPlaces", docHasPlaces);
		keeper.record("Fetched all entities");

		//-------------------CITATIONS-------------------------//
		
		CitationFetcher cf = CitationFetcher.forQuery(query);
		int citationCount = cf.totalCount();
		String styledCits =	StyleTransformer.transform(cf.toXML(), "citations.xsl");
		Renderer citRenderer = new Renderer(Language.ENGLISH);
		citRenderer.addLanguageTokenFilters(prefs);
		String citationText = citRenderer.renderText(styledCits);
		
		myModel.put("citationCount", citationCount);
		myModel.put("citationText", citationText);
		keeper.record("Rendered citations");

		//--------------------------------------------//
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("text", myModel);
	}

}
