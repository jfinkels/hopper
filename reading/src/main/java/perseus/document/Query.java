package perseus.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;

import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateTableOfContentsDAO;
import perseus.document.dao.TableOfContentsDAO;
import perseus.language.Language;
import perseus.language.LanguageCode;
import perseus.util.DisplayPreferences;
import perseus.util.StringUtil;

/**
 * Represents an identifier for a particular resource or part of a resource.
 * Generally used for identifying specific sections of a text, but can also
 * refer to a collection, a corpus, an image, or an entire text or subdocument.
 * A Query comprises two main parts: a document ID (e.g.,
 * "Perseus:text:1999.01.0001" or "Perseus:collection:Greco-Roman") and, for
 * texts, a subquery ("speech=2:section=100") denoting a specific part of the
 * document referred to in the ID.
 *
 * Along with the {@link Chunk} and {@link Metadata} classes, this is probably
 * the most frequently used class in the system, and sadly suffers from the
 * same problems afflicting them. In particular, the Query class is trying to
 * represent too many different things and needs to know an unhealthy amount
 * about itself to execute a lot of its methods (see, for example, the "is just
 * document ID" tests and the hard-coded loop values in many of the functions).
 * The code has generally been left untouched because it mostly works and
 * because rewriting a class so fundamental could result in all sorts of
 * errors, but it needs very badly to be refactored and generally rethought in
 * a future release/rewrite.
 *
 * The most important methods in this class are, in no particular order,
 * getChunk(), getDocumentID(), getQuery(), getMetadata(), and toString().
 * Most of the other methods are used only once, or a couple times, by various
 * other classes.
 */
public class Query implements Comparable<Query> {

	public static final Pattern NAME_VALUE_SPLITTER =
		Pattern.compile("(.+?)=(.+)");

	private static Logger logger = Logger.getLogger(Query.class);

	private String documentID = null;

	private List<QueryElement> queryElements = new ArrayList<QueryElement>();

	// need metadata to work with chunk schemes
	private Metadata metadata = null;

	private String targetScheme = null;
	private String targetType = null;

	/** Empty constructor for Hibernate */
	public Query () {}

	/**
	 * Creates a new Query by duplicating an existing Query.
	 */
	public Query (Query q) {
		this(q.getDocumentID(), q.getQuery());
	}

	/**
	 * Creates a new Query with a document ID and optionally a subquery.
	 *
	 * @param doc the document ID which should be of the form
	 * "system:type:value", as in "Perseus:collection:Greco-Roman"
	 * @param q the subquery, which may be null
	 */
	public Query (String doc, String q) {

		// hack to convert a CTS urn to a document ID
		if (doc.startsWith("urn:cts"))
		{
			doc = CTS.toDocument(doc);
		}
		
		// 1. Parse the document ID into its constituent elements
		String[] documentTokens = doc.split(":");

		if (documentTokens.length == 3) {
			queryElements.add(new QueryElement(QueryElement.SYSTEM_ID,
					documentTokens[0], true));
			queryElements.add(new QueryElement(QueryElement.OBJECT_TYPE,
					documentTokens[1], true));
			queryElements.add(new QueryElement(QueryElement.DOCUMENT_ID,
					documentTokens[2], true));
		}
		else {
			// Invalid document ID
		}

		// 2. Parse the query, if any, into its constituent parts
		if (q != null) {
			// Make sure we don't have a subquery consisting only of whitespace
			// (this could happen if we had bad data in a table somewhere)
			q = q.trim();
			if (q.length() > 0) {
				String[] queryTokens = q.split(":");

				for (int i=0;i < queryTokens.length;i++) {
					queryElements.add(new QueryElement(queryTokens[i]));
				}
			}
		}

		documentID = doc;
	}

	/**
	 * Creates a query from `s`, which should represent the address of an
	 * existing resource.
	 *
	 * @param s the string representing the target object, like
	 * "Perseus:text:1999.01.0003:line=1"
	 */
	public Query (String s) {
		this(s, null, null);
	}

	/**
	 * Creates a query from `s`, using `prefs` (if `s` represents an ABO)
	 * to determine which version to fetch and using `context` (if `s` is
	 * a citation like "1.3") to determine the source of the work.
	 *
	 * The logic inside this method really needs to be moved somewhere else,
	 * probably into a controller class. The only time this method is called
	 * is in connection with the text page, after all.
	 *
	 * @param s a query string, which can be one of several formats:
	 * <ul>
	 *  <li>A text: Perseus:text:1999.01.0002:speech=1:chapter=3</li>
	 *  <li>An ABO: Perseus:abo:tlg,002,0003:12:24</li>
	 *  <li>A full citation: Verg. A. 12.235</li>
	 *  <li>A citation within a text: 12.235</li>
	 * </ul>
	 * @param prefs a {@link DisplayPreferences} instance representing the
	 * settings for the current user
	 * @param context a query representing the "context" for citations within
	 * texts (in the fourth format above, this would represent the document
	 * within which the citation occurred)
	 */
	public Query (String s, DisplayPreferences prefs, Query context) {
		if (s == null) {
			return;
		}
					
		// hack to convert a CTS urn to a document ID
		if (s.startsWith("urn:cts"))
		{
			s = CTS.toDocument(s);
		}
		String[] tokens = s.trim().split(":");

		
		// Somewhat fake way to determine whether a query is an ID
		if (tokens.length >= 3) {
		
			queryElements.add(new QueryElement(QueryElement.SYSTEM_ID,
					tokens[0], true));
			queryElements.add(new QueryElement(QueryElement.OBJECT_TYPE,
					tokens[1], true));
			queryElements.add(new QueryElement(QueryElement.DOCUMENT_ID,
					tokens[2], true));

			documentID = tokens[0] + ":" + tokens[1] + ":" + tokens[2];

			// Process the remaining query elements
			for (int i=3;i < tokens.length;i++) {
				queryElements.add(new QueryElement(tokens[i]));
			}
		}
		else {
			// Did the user enter an abbreviation (like Verg. A. 1.1)?
			boolean couldResolve = resolveAbbreviation(s, prefs, context);
			if (!couldResolve && context != null) {
				// If that didn't work, and we have a context, the user's
				// input may represent a subquery within the current
				// context. This can happen if the user enters something in the
				// jump box for works with no canonical abbreviations.
				//
				// Start with the document ID...
				this.documentID = context.getDocumentID();

				// Add the subelements we can find in the context query.
				for (QueryElement qe : context.getQueryElements()) {
					addQueryElement(qe);
				}

				// Then break up the user input into query elements and append
				// them. This will give us element values but not types, which
				// should be okay, because we'll figure out the types below.
				String tokenStrings[] = s.trim().split("[\\s\\.]+");
				for (int i = 0; i < tokenStrings.length; i++) {
					addQueryElement(new QueryElement(tokenStrings[i].trim()));
				}
			} else if (!couldResolve) {
				logger.warn("Unable to resolve abbreviation: " + s);
			}
		}

		// there are several circumstances where we will only have query values,
		// not query types. That's ok for ABOs, though.
		if (hasUntypedElements() && ! isAbstract()) {
			// If we've gone through the abbreviation parser,
			// we already have metadata, which we need for citation schemes.
			if (metadata == null) {
				metadata = getMetadata();
			}
			setElementTypes();
		}
	}

	private boolean resolveAbbreviation(String originalQuery,
			DisplayPreferences prefs, Query context) {
		Abbreviation match = Abbreviation.find(originalQuery);
		if (match == null) {
			return false;
		}
		Query abo = match.getABO();
		List<Query> documents = ABO.getDocuments(abo.toString());

		if (documents.isEmpty()) {
			return false;
		}

		// Determine correct version of the ABO.

		Query documentQuery = null;
		Metadata documentMetadata = null;

		// First pass: If there is a context query, does the document ID
		//   match any of the ABO options?

		if (context != null) {
			for (Query currentQuery : documents) {
				if (currentQuery.getDocumentID().equals(context.getDocumentID())) {
					documentQuery = currentQuery;
					documentMetadata = currentQuery.getMetadata();
					break;
				}
			}
		}

		// Second pass: If there is no context query, or if the context
		//   query did not match any of the versions of the abstract work
		//   (ie documentQuery is still null), look for the version of the
		//   work that matches the user's language preferences.

		if (documentQuery == null) {
			String languagePreference = "original";
			if (prefs != null) {
				languagePreference =
					prefs.get(DisplayPreferences.LANGUAGE_KEY);
			}

			for (Query candidate : documents) {
				documentMetadata = candidate.getMetadata();

				String documentLanguage = documentMetadata.get(Metadata.LANGUAGE_KEY);

				if (languagePreference.equals("original")) {
					// We don't have information on the "original" language
					// of an ABO, so we have to fake it. A document is
					// categorized as such if it's not in English

					if (!LanguageCode.ENGLISH.equals(documentLanguage)) {
						documentQuery = candidate;
						break;
					}
				}
				else if (languagePreference.equals("trans") && 
						LanguageCode.ENGLISH.equals(documentLanguage)) {
					// Likewise, fake translations; any English document
					// counts as a translation for the time being.

					documentQuery = candidate;
					break;
				}
				else if (languagePreference.equals(documentLanguage)) {
					documentQuery = candidate;
					break;
				}
			}
		}
		
		// if we haven't yet found any matches (in the case of
		// English corpora like Shakespeare), then just take the english version
		if (documentQuery == null) {
			for (Query candidate : documents) {
				if (Language.ENGLISH.equals(candidate.getMetadata().getLanguage())) {
					documentQuery = candidate;
					break;
				}
			}
		}
		
		// if we still haven't found any matches, just take the first one so we're not 
		// producing an error
		if (documentQuery == null) {
			documentQuery = documents.get(0);
		}

		// Accept this version of the abstract work
		this.documentID = documentQuery.getDocumentID();

		// Create new QueryElements based on this version's elements (we should
		// actually create new ones, because simply pointing to the existing
		// elements might get us into trouble later)
		for (QueryElement element : documentQuery.getQueryElements()) {
			addQueryElement(new QueryElement(element.getType(),
					element.getValue(), element.isInABO()));
		}

		// and mark those elements as part of the ABO (for constructing 
		// canonical citations later)
		for (QueryElement queryElement : queryElements) {
			queryElement.setInABO(true);
		}

		// Add the remaining tokens as new QueryElements.
		// The abbreviation resolver removes tokens as they are
		// matched, so only the tokens that specify the chunk should
		// remain. If the query was "shak. 1h4 1.1.99", we will be left
		// with (1 1 99).
		for (String token : match.getNonABOTokens(originalQuery)) {
			queryElements.add(new QueryElement(null, token, false));
		}

		// Now try to find a scheme that works with the tokens we just added.
		// This will apply the scheme type automatically if it finds one that
		// works.
		ChunkSchemes documentSchemes =
			documentQuery.getMetadata().getChunkSchemes();
		getFirstValidScheme(documentSchemes);

		return true;
	}

	/**
	 * Returns the first chunk scheme that can be matched against this Query's
	 * subquery, or null if no valid scheme is found.
	 * 
	 * @param schemes the chunk schemes for this document
	 * @return a chunk scheme ("book:chapter:section") that makes sense for
	 * the given query, or null if no scheme works
	 */
	public String getFirstValidScheme(ChunkSchemes schemes) {
		getMetadata();
		ChunkSchemes chunkSchemes = metadata.getChunkSchemes();

		// Try default chunk first
		String defaultScheme = chunkSchemes.getDefaultScheme();
		if (matchesSchemeType(defaultScheme)) {
			return defaultScheme;
		}

		// Otherwise, loop through chunk schemes
		for (String scheme : chunkSchemes.getSchemes()) {
			if (scheme == defaultScheme) { continue; }
			if (matchesSchemeType(scheme)) { return scheme; }
		}

		// Oh well, we lose. Later on, we'll apply the default type and see
		// what happens.
		clearSchemeTypes();
		return null;
	}

	/**
	 * Tests whether this query's values work with `scheme`.  Some scheme types
	 * got a given work may be of the same length but accept different values,
	 * like book:section and book:page in Plato's Republic.  Thus, to see
	 * whether a query works with a given scheme type, we must consider not
	 * only the length of the query/scheme but also whether a valid chunk
	 * exists for the given scheme with the given values.
	 */
	private boolean matchesSchemeType(String scheme) {
		if (scheme == null) {
			return false;
		}

		String[] chunks = scheme.split("\\:");
		if (chunks.length != queryTokenCount()) {
			return false;
		}

		try {
			applySchemeTypes(chunks);
			getTextlessChunk();
			return true;
		} catch (InvalidQueryException iqe) {
			return false;
		}
	}

	public Metadata getMetadata() {
		if (metadata == null) {
			metadata = MetadataCache.get(this);
		}

		return metadata;
	}

	/**
	 * Returns a new version of this query, but in the specified
	 * language. This is useful for dealing with requests involving
	 * an ABO rather than a text in a specific language.
	 *
	 * FIXME this is broken and should be fixed. fortunately, I don't know if
	 * we ever use it.
	 */
	public Query inLanguage(String language) {
		if (!isAbstract()) {
			return this;
		}

		List<Query> documents = ABO.getDocuments(getMetadata().get(Metadata.ABO_KEY));
		// It shouldn't matter which of these we grab
		if (documents.size() > 0) {
			for (Query query : documents) {
				String docLanguage =
					query.getMetadata().get(Metadata.LANGUAGE_KEY);

				if (docLanguage.equals(language)) {
					String documentID = query.getDocumentID();
					Query newQuery = new Query(documentID, getQuery());

					newQuery.getMetadata();
					newQuery.setElementTypes();

					return newQuery;
				}
			}
		}

		return null;
	}

	/**
	 * Returns a list of all known actual texts corresponding to this query
	 * (which should be of the form "Perseus:abo:..."), with their subqueries
	 * set to match this query's subquery.
	 *
	 * @return a list of queries representing actual texts
	 */
	public List<Query> expandABOQuery() {

		List<Query> aboList = new ArrayList<Query>();

		if (!isAbstract()) {
			aboList.add(this);
			return aboList;
		}

		List<Query> documents = ABO.getDocuments(documentID);

		for (Query documentQuery : documents) {
			Query workingQuery = new Query(documentQuery);

			for (int i = 3; i < queryElements.size(); i++) {
				QueryElement element = (QueryElement) queryElements.get(i);
				if (element.isInABO()) continue;

				workingQuery = workingQuery.appendSubquery(
						element.getType(), element.getValue());
			}
			try {
				workingQuery.getMetadata();
				workingQuery.setElementTypes();
			} catch (Exception e) {
				e.printStackTrace();
			}

			aboList.add(workingQuery);
		}

		return aboList;
	}

	/**
	 * Returns the chunk referred to by this query.
	 *
	 * @return a chunk representing this query
	 * @throws InvalidQueryException if this query does not correspond to a
	 * valid section of a text
	 */
	public Chunk getChunk() throws InvalidQueryException {
		return getChunk(-1);
	}

	/**
	 * Returns the chunk referred to by this query, or the first subchunk
	 * of at most `maxChunkSize` bytes. First calls `getChunk()` as
	 * normal; if the resulting chunk is larger than `maxChunkSize`, fetches
	 * the chunk's first contained chunk, and if <em>that</em> chunk is too
	 * large, fetches <em>its</em> first contained chunk, and so on.
	 *
	 * @return a chunk representing this query or a subquery
	 * @throws InvalidQueryException if this query does not correspond to a
	 * valid section of a text
	 */
	public Chunk getChunk(int maxChunkSize) throws InvalidQueryException {

		Chunk chunk = getTextlessChunk();
		if (maxChunkSize != -1) {
			while (chunk.getEndOffset()-chunk.getStartOffset() > maxChunkSize) {
				logger.debug(chunk.getQuery()
						+ " exceeds specified size " + maxChunkSize
						+ "! Fetching contained chunk.");
				Chunk subchunk = chunk.getFirstContainedChunk(null);
				if (subchunk != null) {
					chunk = subchunk;
				} else {
					break;
				}
			}
			logger.debug("Using chunk " + chunk.getQuery());
		}

		return chunk;
	}

	/** 
	 * Equivalent to `getChunk()`. A long time ago, this method returned a
	 * copy of the chunk without the text loaded, whereas getChunk()
	 * automatically loaded the text. Currently, the text is only loaded
	 * when the user explicitly asks for it, usually by calling
	 * `getText()`.
	 *
	 * @deprecated use getChunk() instead.
	 */
	public Chunk getTextlessChunk() throws InvalidQueryException {
		// if the chunk is abstract, it really should be run through
		// expandABOQuery; but we can at least return the first
		// matching expanded chunk
		if (isAbstract()) {
			List expandedQueries = expandABOQuery();
			for (int i = 0; i < expandedQueries.size(); i++) {
				try {
					Query workingQuery = (Query) expandedQueries.get(i);

					return workingQuery.getTextlessChunk();

				} catch (InvalidQueryException iqe) {
					// okay, try the next one
				}
			}

			// if we haven't found anything yet, give up
			throw new InvalidQueryException(this);
		}

		ChunkDAO dao = new HibernateChunkDAO();
		Chunk chunk = null;

		String defaultScheme = targetScheme;

		if (defaultScheme == null) {
			defaultScheme = getMetadata().getChunkSchemes().getDefaultScheme();
		}

		// If the document itself doesn't have a default scheme, see if it
		// has any subdocuments with schemes.
		if (defaultScheme == null) {
			if (isJustDocumentID()) {
				// if it has subdocuments, see if any of them have schemes
				logger.info("no default scheme: " + this);
				defaultScheme = getFirstSubdocScheme();
			} else {
				// if it *is* a subdocument, look at its containing queries
				// until we find one with a scheme.
				Query parentQuery = this;
				while (!parentQuery.isJustDocumentID()) {
					parentQuery = parentQuery.getContainingQuery();
					defaultScheme = parentQuery.getMetadata()
					.getChunkSchemes().getDefaultScheme();
				}
			}
		}

		TableOfContentsDAO tocDAO = new HibernateTableOfContentsDAO();

		if (isJustDocumentID()) {
			// Case 1: if the query is nothing more than a document ID, like
			// Perseus:text:1999.01.0001, get the first chunk we find for its
			// default chunk-type (as indicated by its metadata).

			chunk = dao.getFirstChunkForScheme(documentID, defaultScheme, targetType);
		} else if (isSubDocument()) {
			// See if this has any subchunks
			chunk = dao.getFirstChunkForScheme(getInnermostDocumentID(),
					defaultScheme, targetType);

			// If not, it may be a subtext with no child
			// chunks (this happens with Callimachus)--just grab the chunk
			// itself
			if (chunk == null) {
				chunk = tocDAO.getChunkByQuery(null, this);		
			}
		} else if (getLastElementType().equals("id")) {
			// Case 3: if the query includes an "id" type, like,
			// "Perseus:text:1999.01.0001:id=fred", look it up using the
			// chunkID field instead.
			// if id=fred points to page=3, then we don't need to do anything special
			// but if id=fred actually points to book=1:chapter=3:section=2, 
			// we need to fix it: getByChunkID will only give us section=2, so we
			// need to get all of the previous elements
			
			chunk = dao.getByChunkID(getDocumentID(), getLastElementValue());
			
			//we need to find correct scheme, because the default scheme may not be the correct one
			List<String> chunkSchemes = getMetadata().getChunkSchemes().getSchemes();
			List<String> schemeList = null;
			int index = -1;
			for (String scheme : chunkSchemes) {
				schemeList = ChunkSchemes.typesForScheme(scheme);
				if (schemeList.contains(chunk.getType())) {
					index = schemeList.indexOf(chunk.getType()) - 1;
					break;
				}
			}
			
			// chunk type was not the first item, otherwise the previous
			// dao call worked fine
			if (index >= -1) {
				Query targetQuery = chunk.getQuery();
				//go backwards through the schemes starting with the first one before the type of the chunk
				for (int i = index; i >= 0; i--) {
					//getContainingChunk does some weird stuff, so don't change chunk's query until we're done
					Query parentQuery = dao.getContainingChunk(chunk, schemeList.get(i))
					.getQuery().appendSubquery(targetQuery.getQuery());				
					targetQuery = parentQuery;
				}
				chunk.setQuery(targetQuery);
			}
		} else {
			// Try to look it up in one of our TOCs
			try {
				chunk = tocDAO.getChunkByQuery(null, this);

				// Otherwise, do a standard search, narrowing down the query
				// element by element (this can happen with line-based queries)
			} catch (InvalidQueryException iqe) {
				logger.debug("Table-of-contents search failed; trying line-based", iqe);
				chunk = dao.getByQuery(this);
			}

			// force loading entries (instead of alphabetic letters/entry groups)
			while (chunk != null &&
					("alphabetic letter".equals(chunk.getType()) || 
							"entry group".equals(chunk.getType()))) {
				String targetType = chunk.getType().equals("entry group")
				? "entry" : "entry group";
				chunk = dao.getFirstContainedChunk(chunk, targetType, null);
			}

			if (chunk == null) {
				// And if *that* didn't work, search by the display-query.
				chunk = dao.getByDisplayQuery(getDocumentID(), getQuery());
			}
		}

		if (chunk != null) {
			return chunk;
		}

		throw new InvalidQueryException(this);
	}

	public void setElementTypes () {
		// At this point our query elements probably (?) contain 
		// a few elements that we don't have a type for (like 1, 1, 99 in
		// the previous example). Now find a chunk scheme that is compatible
		// and set the types. So, for the scheme act:scene:line, we get
		// act=1:scene=1:line=99.

		if (isWholeText()) return;

		ChunkSchemes chunkSchemes = metadata.getChunkSchemes();

		String validScheme = getFirstValidScheme(chunkSchemes);
		if (validScheme == null) {
			String defaultScheme = chunkSchemes.getDefaultScheme();
			if (defaultScheme != null) {
				String chunks[] = defaultScheme.split("\\:");
				applySchemeTypes(chunks);
			}
		}
	}

	/** This method sets the "type" value on any untyped query values
     based on the types in the chunk scheme.
	 */
	private void applySchemeTypes(String[] scheme) {
		// The chunk scheme may not have the same number of chunks as the query.
		// A good example is Livy. The Latin is marked to book:chapter:section,
		// but the English translation is only marked to book:chapter.
		// Thus, if we start with the Latin chunk 2.4.3, we will be overspecifying
		// the English translation -- that's bad because there may not be an 
		// entry in the chunktable for book=2:chapter=4:section=3. In this case,
		// drop the extra query element.

		List<QueryElement> subelements = subqueryElements();
		while (subelements.size() > scheme.length) {
			subelements.remove(subelements.size()-1);
		}

		// If it goes the other way, we will be underspecifying, but there should
		// be a chunktable entry for book=2:chapter=4, even if there's also one
		// for book=2:chapter=4:section=1

		int schemeIdx = 0;
		for (QueryElement element : subqueryElements()) {
			// Get chunk type -- we've already made sure that there are enough
			// chunk types
			element.setType(scheme[schemeIdx].replaceAll("[\\*\\+]", ""));
			schemeIdx++;
		}
	}

	/**
	 * This method clears the "type" value on any query values that belong to
	 * a subquery.
	 */
	private void clearSchemeTypes() {
		for (QueryElement element : subqueryElements()) {
			element.setType(null);
		}
	}

	/**
	 * Returns all the elements of this query that are *not* part of the
	 * document ID.
	 */
	private List<QueryElement> subqueryElements() {
		int startIndex = 3;
		return queryElements.subList(startIndex, queryElements.size());
	}

	private void setSubdocElements(String subdocQuery) {
		if (queryElements.size() <= 3) {
			// If we have no subdoc elements, we can't mark them
			return;
		}

		String[] tokens = subdocQuery.split(":");
		for (int i=0;i<tokens.length;i++) {
			Matcher matcher = NAME_VALUE_SPLITTER.matcher(tokens[i]);
			if (matcher.matches()) {
				String name = matcher.group(1);

				QueryElement element = queryElements.get(i + 3);
				if (name.equals(element.getType())) {
					element.setInABO(true);
				}
			}
			else {
				logger.warn("Couldn't parse subdoc query " + subdocQuery);
			}
		}
	}

	/**
	 * Returns a copy of this query, minus the most specific query element.
	 * So, Perseus:text:1999.01.0001:speech=1:chapter=2 will be converted to
	 * Perseus:text:1999.01.0001:speech=1.  If the query is already just a
	 * document id, this returns null.
	 */
	public Query getContainingQuery() {
		if (queryElements.size() <= 3) {
			return null;
		}

		if (queryElements.size() == 4) {
			return new Query(documentID, (String)null);
		}

		// Construct a query string as we normally do, but leave off
		// the last element
		StringBuilder query = new StringBuilder();

		for (int i=3;i < queryElements.size() - 1 ;i++) {
			if (i > 3) { query.append(":"); }
			query.append(queryElements.get(i));
		}

		return new Query(documentID, query.toString());
	}

	/**
	 * Identical to the `appendSubquery` method with the same parameters.
	 *
	 * @deprecated use appendSubquery(String, String) instead.
	 */
	public Query getSubQuery(String type, String value) {
		if (isJustDocumentID()) {
			return new Query(documentID, type + "=" + value);
		}
		else {
			return new Query(documentID,
					getQuery() + ":" + type + "=" + value);
		}
	}

	/**
	 * Returns the ID of the document represented by this query. The document
	 * ID is a cached compilation of the first three query elements.
	 */
	public String getDocumentID() {
		// This is a cached compilation of the first three query elements
		if (documentID == null) {
			if (queryElements.size() != 0) {
				documentID = StringUtil.join(queryElements.subList(0, 3), ":");
			}
		}
		return documentID;
	}

	/**
	 * Sets this query's document ID. This method is mostly here to make
	 * Hibernate happy (it needs a modifier method for the document ID).
	 */
	public void setDocumentID(String newID) {
		String[] tokens = newID.split(":");

		documentID = newID;
		if (queryElements.size() >= 3) {
			queryElements.set(0, new QueryElement(QueryElement.SYSTEM_ID,
					tokens[0], true));
			queryElements.set(1, new QueryElement(QueryElement.OBJECT_TYPE,
					tokens[1], true));
			queryElements.set(2, new QueryElement(QueryElement.DOCUMENT_ID,
					tokens[2], true));
		} else {
			queryElements.add(new QueryElement(QueryElement.SYSTEM_ID,
					tokens[0], true));
			queryElements.add(new QueryElement(QueryElement.OBJECT_TYPE,
					tokens[1], true));
			queryElements.add(new QueryElement(QueryElement.DOCUMENT_ID,
					tokens[2], true));
		}

	}

	/**
	 * Sets this query's subquery.
	 */
	public void setQuery(String newQuery) {
		if (newQuery == null) {
			while (queryElements.size() > 3) {
				queryElements.remove(queryElements.size()-1);
			}
			return;
		}
		newQuery = newQuery.trim();
		if (newQuery.length() > 0) {
			String[] queryTokens = newQuery.split(":");

			for (int i=0;i < queryTokens.length;i++) {
				if (queryElements.size() > i+4) {
					queryElements.set(i+3, new QueryElement(queryTokens[i]));
				} else {
					queryElements.add(i+3, new QueryElement(queryTokens[i]));
				}
			}

			int tokenCount = queryTokenCount();
			while (queryTokens.length < tokenCount) {
				queryElements.remove(queryTokens.length);
			}
		}
	}

	/**
	 * Returns the "innermost" document ID of the query--in other words, returns
	 * the subdocument ID ("Perseus:text:1999.01.0001:speech=1") if the query
	 * represents a subdocument and the regular ID
	 * ("Perseus:text:1999.01.0135") otherwise.
	 *
	 * Currently, the only use for this method involves selecting a new
	 * chunking type while viewing a subdocument. If the user is viewing
	 * a subtext and chooses to view the text chunked in a different way, we
	 * want to reload the current subdocument, *not* the current document.
	 */
	public String getInnermostDocumentID() {

		if (metadata == null) { metadata = this.getMetadata(); }

		String queryKey = metadata.get(Metadata.SUBDOC_QUERY_KEY);

		if (queryKey != null) {
			Query subquery = new Query(documentID, queryKey);
			return subquery.toString();
		} else {
			return documentID;
		}
	}

	/**
	 * Returns the specific location of the query within a document. The result
	 * will be in the form "book=3:chapter=4". Essentially returns a string
	 * representation of all query elements that are not part of the document
	 * ID. If the query is just a document ID, returns null.
	 *
	 * The name "getQuery" is rather confusing, since the method doesn't
	 * return an actual Query but rather a subquery.
	 */
	public String getQuery() {
		// Is the query just a document ID?
		if (isJustDocumentID()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		for (int i=3;i < queryElements.size();i++) {
			if (i > 3) { query.append(":"); }
			query.append(queryElements.get(i));
		}
		return query.toString();
	}

	/**
	 * Returns all elements of the query in the usual form
	 * EXCEPT those parts that participate in the ABO. So, the query
	 * Perseus:text:1999.02.0010:text=Catil.:speech=1:section=2 will return
	 * speech=1:section=2.
	 */
	public String getNonABOQuery() {
		// Make sure that query elements in the ABO are marked as such...

		if (metadata == null) { metadata = getMetadata(); }
		if (metadata.get(Metadata.SUBDOC_QUERY_KEY) != null) {
			setSubdocElements(metadata.get(Metadata.SUBDOC_QUERY_KEY));
		}

		StringBuilder output = new StringBuilder();

		boolean firstElement = true;
		for (int i=3;i < queryElements.size();i++) {
			QueryElement element = queryElements.get(i);
			if (! element.isInABO()) {
				if (firstElement) { 
					firstElement = false;
				}
				else {
					output.append(":");
				}
				output.append(element.getType())
				.append("=")
				.append(element.getValue());
			}
		}
		
		return output.toString();
	}

	/**
	 * Returns the *values* of all elements of this query that aren't part of
	 * an ABO. So, the query
	 * Perseus:text:1999.02.0010:text=Catil.:speech=1:section=2 will return
	 * 1:2. Useful for comparing subqueries of an ABO query and a query
	 * pointing to an actual text.
	 */
	public String getNonABOValues() {
		if (metadata == null) { metadata = getMetadata(); }
		if (metadata.get(Metadata.SUBDOC_QUERY_KEY) != null) {
			setSubdocElements(metadata.get(Metadata.SUBDOC_QUERY_KEY));
		}

		List<String> values = new ArrayList<String>();
		for (int i = 3; i < queryElements.size(); i++) {
			QueryElement element = queryElements.get(i);
			if (! element.isInABO()) {
				values.add(element.getValue());
			}
		}

		if (values.isEmpty()) {
			return "";
		}
		return StringUtil.join(values, ":");
	}

	/**
	 * Returns this query as an ABO. For example, the query
	 * Perseus:text:1999.02.0010:text=Catil.:speech=1:section=2 will return
	 * a new query pointing to Perseus:abo:phi,0474,013:2.
	 */   
	public Query asABO() {
		if (isAbstract()) return this;
		String aboID = getMetadata().get(Metadata.ABO_KEY);
		if (aboID == null) return this;

		return new Query(aboID, getNonABOValues());
	}     

	/**
	 * Returns a representation of this query's subquery in a human-readable
	 * format. One example would be "book 1, chapter 5".
	 * 
	 * @return a representation of this query in the form "type value", "type
	 * value", ...
	 */
	public String getDisplayQuery() {
		// Is the query just a document ID?
		if (subqueryElements().isEmpty()) {
			return null;
		}

		List<String> tokens = new ArrayList<String>();
		for (QueryElement element : subqueryElements()) {
			tokens.add(
					((element.getType() != null) ? element.getType() + " " : "") +
					element.getValue());
		}

		return StringUtil.join(tokens, ", ");
	}

	/**
	 * Returns a representation of this query (the entire query, not just the
	 * subquery) as a citation. Essentially combines the abbreviation for the
	 * ABO, if one exists, with the subquery elements separated by periods. One
	 * example would be "Verg. A. 1.2".
	 *
	 * @return a representation of this query as a citation
	 */
	public String getDisplayCitation() {
		if (metadata == null) { metadata = this.getMetadata(); }
		if (metadata.get(Metadata.SUBDOC_QUERY_KEY) != null) {
			setSubdocElements(metadata.get(Metadata.SUBDOC_QUERY_KEY));
		}

		StringBuilder citation = new StringBuilder();

		if (metadata.has(Metadata.ABO_KEY)) {
			Query abo = new Query(metadata.get(Metadata.ABO_KEY));
			Abbreviation abbreviation = Abbreviation.forABO(abo);

			if (abbreviation != null) {
				citation.append(abbreviation.getDisplayForm()).append(" ");
			} else {
				logger.warn("No abbreviation found for ABO [" + abo + "]");
			}
		}

		if (!subqueryElements().isEmpty()) {
			citation.append(getDisplaySubqueryCitation());
		}

		return citation.toString();
	}

	/**
	 * Returns a representation of this query's subquery as a citation.
	 * One example would be "1.12" for "book=1:chapter=12". Ignores the
	 * document/subdocument ID.
	 *
	 * @return a representation of this query's subquery as a citation
	 */
	public String getDisplaySubqueryCitation() {
		StringBuilder citation = new StringBuilder();

		for (QueryElement element : subqueryElements()) {
			if (! element.isInABO()) {
				citation.append(element.getValue()).append(".");
			}
		}

		if (citation.length() > 0) citation.deleteCharAt(citation.length()-1);
		return citation.toString();
	}

	public boolean equals (Object o) {
		if (! (o instanceof Query)) {
			return false;
		}

		return toString().equals(o.toString());
	}

	public int compareTo(Query query) {
		return toString().compareTo(query.toString());
	}

	public int hashCode() {
		int result = 17;

		result = 37*result + toString().hashCode();

		return result;
	}

	public String toString() {
		if (isJustDocumentID()) {
			return documentID;
		}
		return documentID + ":" + getQuery();
	}

	public List<QueryElement> getQueryElements() {
		return queryElements;
	}

	/**
	 * Removes the query element at index `element`.
	 *
	 * @deprecated this method never seems to have actually been used, and
	 * uses a bizarre indexing scheme
	 */
	public void removeQueryElement(int element) {
		queryElements.remove(element + 2);
	}

	/**
	 * Appends a query element to an existing list of them.  Intended for use
	 * with resolving queries in ABO form into the more familiar form.
	 */
	public void addQueryElement(QueryElement elt) {
		queryElements.add(elt);
	}

	/**
	 * Adds a new query element. Useful for callers who don't have access
	 * to the QueryElement class, and so can't use the method that takes a
	 * QueryElement.
	 */
	public void addQueryElement(String type, String value, boolean inABO) {
		addQueryElement(new QueryElement(type, value, inABO));
	}

	/**
	 * Returns a new query representing `subquery` appended to this query.
	 * Useful if you have a Query that may or may not be a subdocument, and you
	 * want to add something to the query without clobbering the
	 * subdocument-part of the query.
	 *
	 * @param subquery a string representing one or more subquery elements, like
	 * "chapter=1:section=4"
	 * @return a new Query with `subquery` appended to the current query
	 */
	public Query appendSubquery(String subquery) {
		Query newQuery = new Query(this);
		if (subquery.length() > 0) {
			String[] tokens = subquery.split(":");
			for (int i = 0; i < tokens.length; i++) {
				newQuery.addQueryElement(new QueryElement(tokens[i]));
			}
		}
		return newQuery;
	}

	/**
	 * Returns a new query representing `type` and `value` appended to this
	 * query. Useful for programatically constructing Queries.
	 *
	 * @param type the type to add
	 * @param value the value to add
	 * @return a new Query with an additional QueryElement of the form
	 * "type=value" appended to the current query
	 */
	public Query appendSubquery(String type, String value) {
		Query newQuery = new Query(this);
		newQuery.addQueryElement(new QueryElement(type, value, false));

		return newQuery;
	}

	/**
	 * Returns the type associated with the first non-document-ID part of the
	 * query. If the query is "Perseus:text:1999.04.0059:entry=abalieno", this
	 * method will return "entry".
	 * 
	 * This is initially intended to help the efficient loading of lexicon
	 * entries, which are usually not hierarchical, and can therefore be
	 * described using only a document id, a type, and a value.
	 */
	public String getTopQueryType() {
		if (queryElements.size() > 3) {
			QueryElement top = queryElements.get(3);
			return top.getType();
		}
		return null;
	}

	/**
	 * Returns the value associated with the first non-document-ID part of the
	 * query. If the query is "Perseus:text:1999.04.0059:entry=abalieno", this
	 * method will return "abalieno".
	 * 
	 * This is initially intended to help the efficient loading of lexicon
	 * entries.
	 */
	public String getTopQueryValue() {
		if (queryElements.size() > 3) {
			QueryElement top = queryElements.get(3);
			return top.getValue();
		}
		return null;
	}

	/**
	 * Returns true if this query contains an element of type `type`.
	 *
	 * It's useful to know that when finding nested chunks -- if you are
	 * in speech=1, and the chunk scheme is speech:section, we want to
	 * know not to look for speech elements.
	 *
	 * @param type the type to search for, like "book"
	 * @return true if this query contains an element of type `type`.
	 */
	public boolean containsType(String type) {
		for (int i=3;i < queryElements.size();i++) {
			QueryElement element = queryElements.get(i);
			if (element.getType() != null &&
					element.getType().equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the value associated with `type` within this query, or null if
	 * this query has no element of type `type`.
	 */
	public String getValueForType(String type) {
		for (int i=3;i < queryElements.size();i++) {
			QueryElement element = queryElements.get(i);
			if (element.getType() != null &&
					element.getType().equalsIgnoreCase(type)) {
				return element.getValue();
			}
		}
		return null;
	}

	/**
	 * Returns true if this query contains no elements besides the three that
	 * make up its document ID. Note that this will return <em>false</em>
	 * for subdocuments, which may not be what you want.
	 */
	public boolean isJustDocumentID() {
		return queryElements.size() == 3;
	}

	/**
	 * Returns true if this query represents a subdocument (a whole
	 * subdocument, not a particular chunk of the subdocument)--that is, if all
	 * its elements are part of an ABO.
	 */
	public boolean isSubDocument() {

		if (isJustDocumentID()) {
			return false;
		}

		if (metadata == null) { metadata = getMetadata(); }
		if (metadata.get(Metadata.SUBDOC_QUERY_KEY) != null) {
			setSubdocElements(metadata.get(Metadata.SUBDOC_QUERY_KEY));
		}

		List queryElements = getQueryElements();
		for (int i = 0; i < queryElements.size(); i++) {

			QueryElement element = (QueryElement) queryElements.get(i);

			if (!element.isInABO()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns true if this query represents an entire text--that is, if it's
	 * just a document ID or a subdocument.
	 */
	public boolean isWholeText() {
		return (isJustDocumentID() || isSubDocument());
	}

	/**
	 * Returns true if this query contains any elements with type "line" or
	 * "card".
	 */
	public boolean isLineBased() {
		for (QueryElement element : subqueryElements()) {
			if ("line".equals(element.getType()) ||
					"card".equals(element.getType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the number of elements this query contains, not including the
	 * document ID.
	 */
	public int queryTokenCount() {
		return queryElements.size() - 3;
	}

	/**
	 * Returns the value of the smallest element within the query. Thus, for
	 * "Perseus:text:1999.01.0001:speech=3:section=148", returns "148".
	 */
	public String getLastElementValue() {
		QueryElement element = queryElements.get(queryElements.size() - 1);
		return element.getValue();
	}

	/**
	 * Returns the value of the smallest element within the query. Thus, for
	 * "Perseus:text:1999.01.0001:speech=3:section=148", returns "section".
	 */
	public String getLastElementType() {
		QueryElement element = queryElements.get(queryElements.size() - 1);
		return element.getType();
	}

	/**
	 * Returns true if this query represents an ABO.
	 */
	public boolean isAbstract() {
		if (queryElements.size() < 2) {
			return false;
		}

		QueryElement objectType = queryElements.get(1);
		return objectType.getValue().equals("abo");
	}

	/**
	 * Returns true if any of this query's elements have a value but not a
	 * type. This happens for some ABO queries, like
	 * "Perseus:abo:tlg,001,0001:1".
	 */
	public boolean hasUntypedElements() {
		for (int i=0;i<queryElements.size();i++) {
			QueryElement element = queryElements.get(i);
			if (element.getType() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the default chunk scheme for the first subdocument of this
	 * query, which should represent the ID of a document with subdocuments.
	 */
	public String getFirstSubdocScheme() {
		// We'll put all the subtext chunks into a sorting set.
		Set<Chunk> subtextChunks = new TreeSet<Chunk>();

		subtextChunks.addAll(new Chunk(this).getSubTexts());

		for (Chunk c : subtextChunks) {
			String scheme = c.getMetadata()
			.getChunkSchemes().getDefaultScheme();

			if (scheme != null) return scheme;
		}

		return null;

	}

	/*
    public boolean documentIDResolves() {
	return (!(isAbstract() && ABO.getDocuments(documentID).isEmpty()));
    }
	 */

	/**
	 * Returns the first element of the document ID, which will be a string
	 * representing the system on which this document lives, like "Perseus" or
	 * "Stoa".
	 */
	public String getSystem() {
		return getValueForElement(0);
	}

	/**
	 * Returns the second element of the document ID, which will be the type of
	 * this object, like "text", "image" or "abo".
	 */
	public String getObjectType() {
		return getValueForElement(1);
	}

	/**
	 * Returns the third element of the document ID, which will be a value like
	 * "1999.01.0001".
	 */
	public String getShortID() {
		return getValueForElement(2);
	}

	/**
	 * Internal helper method that doesn't attempt to use three-based
	 * query element indexing.
	 */
	private String getValueForElement(int index) {
		return ((QueryElement) queryElements.get(index)).getValue();
	}

	/**
	 * Returns a representation of this query as XML.
	 */
	public String toXML() {
		StringBuilder output = new StringBuilder();

		output.append("<query id=\"").append(toString()).append("\">\n");

		if (queryElements.size() >= 3) {
			output.append("<documentID>\n")
			.append("<system>").append(getSystem()).append("</system>\n")
			.append("<type>").append(getObjectType()).append("</type>\n")
			.append("<id>").append(getShortID()).append("</id>\n")
			.append("</documentID>\n");

			output.append("<subquery>\n");
			for (int i=3, n = queryElements.size(); i < n; i++) {
				QueryElement element = (QueryElement) queryElements.get(i);
				output.append("<element type=\"").append(element.getType())
				.append("\" value=\"").append(element.getValue())
				.append("\" />\n");
			}
			output.append("</subquery>\n");
		}

		output.append("</query>");

		return output.toString();
	}

	/**
	 * Helper class representing a segment of this query. Consists of a type
	 * ("speech", "book", "line", etc.), a value (3, "A", etc.) and a boolean
	 * denoting whether this element is part of an ABO. The last is consulted by
	 * several methods in the Query class to determine whether a given element
	 * should be acted on by various methods.
	 */
	public class QueryElement {
		String type;
		String value;
		boolean inABO;

		public static final String SYSTEM_ID = "system";
		public static final String OBJECT_TYPE = "object_type";
		public static final String DOCUMENT_ID = "document_id";

		public QueryElement (String t, String v, boolean a) {
			type = t;
			value = v;
			inABO = a;
		}

		public QueryElement (String s) {
			Matcher matcher = NAME_VALUE_SPLITTER.matcher(s);
			if (matcher.matches()) {
				type = matcher.group(1);
				value = matcher.group(2);

				inABO = false; // until we know otherwise
			}
			else {
				type = null;
				value = s;
				inABO = false; // until we know otherwise
			}

		}

		public void setInABO(boolean b) {
			inABO = b;
		}

		public void setType(String t) {
			type = t;
		}

		public String getType() {
			if (type == null) {
				return null;
			}

			// A little handwaving to make card breaks mandatory
			if (type.equals("line")) {
				return "card";
			}

			return type;
		}

		public void setValue(String v) {
			value = v;
		}

		public String getValue() {
			return value;
		}

		public boolean isInABO() {
			return inABO;
		}

		public String toString() {
			if (type == null) {
				return value;
			}
			else if (type.equals(SYSTEM_ID) ||
					type.equals(OBJECT_TYPE) ||
					type.equals(DOCUMENT_ID)) {
				return value;
			}
			else {
				return type + "=" + value;
			}
		}
	}

	public String getTargetScheme() {
		return targetScheme;
	}

	public void setTargetScheme(String targetScheme) {
		this.targetScheme = targetScheme;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
		
}
