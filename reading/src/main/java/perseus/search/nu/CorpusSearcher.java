package perseus.search.nu;

import static org.apache.lucene.search.BooleanClause.Occur.MUST;
import static org.apache.lucene.search.BooleanClause.Occur.MUST_NOT;
import static org.apache.lucene.search.BooleanClause.Occur.SHOULD;
import static perseus.search.nu.CorpusIndexer.SearchField.COLLECTION;
import static perseus.search.nu.CorpusIndexer.SearchField.DOCUMENT_ID;
import static perseus.search.nu.CorpusIndexer.SearchField.HEAD;
import static perseus.search.nu.CorpusIndexer.SearchField.LEMMATIZED;
import static perseus.search.nu.CorpusIndexer.SearchField.OFFSET;
import static perseus.search.nu.CorpusIndexer.SearchField.QUERY;
import static perseus.search.nu.CorpusIndexer.SearchField.TEXT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import perseus.document.Chunk;
import perseus.document.Corpus;
import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.Resource;
import perseus.language.Language;
import perseus.language.LanguageCode;
import perseus.morph.Lemma;
import perseus.morph.Lemmatizer;
import perseus.search.SearchException;
import perseus.search.nu.CorpusIndexer.SearchField;
import perseus.util.ObjectCounter;
import perseus.util.Range;

public class CorpusSearcher implements Searcher {

	// disable this until we find a more efficient way of doing it...
	//private static final int MAX_HITS_FOR_COUNTS = 0;

	private int hitsPerPage = 10;
	private int resultsPerDocument = 1;

	private Language language = Language.forCode(LanguageCode.ENGLISH);
	private Set<Query> targetDocuments = new HashSet<Query>();

	/** All of these words must appear in the result */
	private List<String> requiredWords = new ArrayList<String>();
	private boolean expandRequiredWords = false;

	/** This phrase must appear in the result */
	private String requiredPhrase = null;

	/** At least one of these words must appear in the result */
	private List<String> allowedWords = new ArrayList<String>();
	private boolean expandAllowedWords = false;

	/** None of these words may appear in the result */
	private List<String> excludedWords = new ArrayList<String>();
	private boolean expandExcludedWords = false;

	private boolean sortByRelevance = false;

	private static Logger logger = Logger.getLogger(CorpusSearcher.class);

	private static Map<Language,String[]> documentIDCache =
		new HashMap<Language,String[]>();

		/*private Filter wrapperFilter = new CachingWrapperFilter(
	new Filter() {
	    // dummy filter to let all searches through
	    private BitSet bits;

	    public BitSet bits(IndexReader reader) {
		if (bits == null) {
		    bits = new BitSet(reader.numDocs());
		    bits.flip(0, bits.size());
		}

		return bits;
	    }
	}
    );*/


		public BooleanQuery constructQuery() {
			BooleanQuery rootQuery = new BooleanQuery();

			if (!requiredWords.isEmpty()) {
				rootQuery.add(buildRequiredWordsQuery(), MUST);
			}
			if (requiredPhrase != null) {
				try {
					rootQuery.add(buildRequiredPhraseQuery(), MUST);
				} catch (ParseException pe) {
					logger.error("Unable to construct phrase query", pe);
				}
			}
			if (!allowedWords.isEmpty()) {
				rootQuery.add(buildAllowedWordsQuery(), MUST);
			}
			if (!excludedWords.isEmpty()) {
				rootQuery.add(buildExcludedWordsQuery(), MUST_NOT);
			}

			return rootQuery;
		}

		private BooleanQuery buildRequiredWordsQuery() {
			if (expandRequiredWords && language.getHasMorphData()) {
				return buildExpandedQuery(MUST, SHOULD, requiredWords);
			} else {
				return buildBooleanQuery(TEXT, requiredWords, MUST);
			}
		}

		private BooleanQuery buildExpandedQuery(
				BooleanClause.Occur outerOccur, 
				BooleanClause.Occur innerOccur, 
				List<String> words) {

			BooleanQuery mainQuery = new BooleanQuery();
			for (String word : words) {
				BooleanQuery lemmaQuery = new BooleanQuery();

				if (language.getHasMorphData()) {
					List<Lemma> lemmas = Lemmatizer.getLemmas(word, language.getCode());
					for (Lemma lemma : lemmas) {
						lemmaQuery.add(new TermQuery(new Term(
								LEMMATIZED.getName(),
								lemma.getDisplayForm())),
								innerOccur);
					}
				} else {
					lemmaQuery.add(
							new TermQuery(new Term(TEXT.getName(), word)),
							innerOccur);
				}

				mainQuery.add(lemmaQuery, outerOccur);
			}

			return mainQuery;
		}

		private org.apache.lucene.search.Query buildRequiredPhraseQuery()
		throws ParseException {
			String query = String.format("\"%s\"", requiredPhrase);
			return new QueryParser(TEXT.getName(),
					language.getAdapter().getAnalyzer()).parse(query);
		}

		private BooleanQuery buildAllowedWordsQuery() {
			if (expandAllowedWords && language.getHasMorphData()) {
				return buildExpandedQuery(SHOULD, SHOULD, allowedWords);
			} else {
				return buildBooleanQuery(TEXT, allowedWords, SHOULD);
			}
		}

		private BooleanQuery buildExcludedWordsQuery() {
			if (expandExcludedWords && language.getHasMorphData()) {
				return buildExpandedQuery(MUST_NOT, SHOULD, excludedWords);
			} else {
				return buildBooleanQuery(TEXT, excludedWords, SHOULD);
			}
		}

		private BooleanQuery buildTargetDocumentsQuery() {
			BooleanQuery query = new BooleanQuery();
			for (Query document : targetDocuments) {
				query.add(new TermQuery(new Term(
						COLLECTION.getName(), document.toString())), SHOULD);
			}

			return query;
		}

		private BooleanQuery buildTargetCollectionsQuery() {
			BooleanQuery query = new BooleanQuery();
			for (Corpus corpus : targetCollections) {
				query.add(new TermQuery(new Term(
						COLLECTION.getName(), corpus.getID())), SHOULD);
			}

			return query;
		}

		private BooleanQuery buildBooleanQuery(SearchField field, Collection<String> items,
				BooleanClause.Occur occur) {

			BooleanQuery query = new BooleanQuery();
			QueryParser parser = new QueryParser(
					field.getName(), language.getAdapter().getAnalyzer());
			for (String item : items) {
				try {
					query.add(parser.parse("\"" + item.toString() + "\""), occur);
					//	    query.add(new TermQuery(new Term(field.getName(), item.toString())), occur);
				} catch (ParseException pe) {
					logger.error("Couldn't parse search query", pe);
				}
			}

			return query;
		}

		public SearchResults search(Range<Integer> range) {
			return search(null, range);
		}

		public SearchResults search(String term, Range<Integer> range) {
			try {
				IndexSearcher searcher = new IndexSearcher(
						CorpusIndexer.getIndexPath(language));

				if (!documentIDCache.containsKey(language)) {
					IndexReader reader = IndexReader.open(
							CorpusIndexer.getIndexPath(language));
					documentIDCache.put(language,
							FieldCache.DEFAULT.getStrings(
									reader, DOCUMENT_ID.getName()));
					reader.close();
				}
				BooleanQuery mainQuery = constructQuery();

				QueryWrapperFilter filter = null;
				if (!targetDocuments.isEmpty()) {
					filter = new QueryWrapperFilter(buildTargetDocumentsQuery());
				} else if (!targetCollections.isEmpty()) {
					filter = new QueryWrapperFilter(buildTargetCollectionsQuery());
				}

				CorpusSearchResults results = new CorpusSearchResults();
				results.setSearcher(this);
				results.setTargetDocuments(getTargetDocuments());

				TopDocs docs = searcher.search(mainQuery, filter, 99999, 
						new Sort(new String[] { DOCUMENT_ID.getName(), OFFSET.getName() }));
				//maybe create a new field and sort by Metadata sort key and offset so we group
				//authors/texts together??  Would need to be updated in CorpusIndexer as well
				results.setTotalHitCount(docs.totalHits);
				ScoreDoc[] hits = docs.scoreDocs;

				/*
				 * If we have target documents, just get the results from those
				 * documents
				 */
				 Set<Query> targetDocQueries = results.getTargetDocuments();

				Set<String> targetDocs = new HashSet<String>();
				for (Query q : targetDocQueries) {
					targetDocs.add(q.toString());
				}

				List<String> documentIDs = new ArrayList<String>();
				ObjectCounter<String> docIDFrequencies = new ObjectCounter<String>();
				String[] docIDs = documentIDCache.get(language);
				for (int i = 0; i < hits.length; i++) {
					String docId = docIDs[hits[i].doc];
					updatedDocIdFrequencies(docIDFrequencies, docId, documentIDs);
				}
				results.setDocumentFrequencies(docIDFrequencies);

				Collections.sort(documentIDs);

				int startDoc = range.getStart();
				int endDoc = 0;
				int maxResults = Math.min(range.getEnd(), docIDFrequencies.size());
				for (int i = 0; i < maxResults; i++) {
					if (i == startDoc) {
						startDoc = endDoc;
					}
					endDoc += docIDFrequencies.getCount(documentIDs.get(i));
				}

				for (int i = startDoc; i < endDoc; i++) {
					Document luceneDoc = searcher.doc(hits[i].doc);

					CorpusSearchResult result = new CorpusSearchResult(luceneDoc);
					result.setRelevance(hits[i].score);
					results.add(result);
				}

				searcher.close();
				return results;

			} catch (IOException ioe) {
				throw new SearchException(ioe);
			}
		}

		private void updatedDocIdFrequencies(ObjectCounter<String> docIDFrequencies, String docId, List<String> documentIDs) {
			logger.debug("counting docId "+docId);
			docIDFrequencies.count(docId);
			if (!documentIDs.contains(docId)) {
				documentIDs.add(docId);
			}
		}

		public class CorpusSearchResults extends SearchResults<CorpusSearchResult> {

			private ObjectCounter<String> documentFrequencies =
				new ObjectCounter<String>();

			private Set<Query> targetDocuments = new HashSet<Query>();

			public void setTargetDocuments(Set<Query> newTargetDocuments) {
				targetDocuments = newTargetDocuments;
			}

			public Set<Query> getTargetDocuments() {
				return targetDocuments;
			}

			public ObjectCounter<String> getDocumentFrequencies() {
				return documentFrequencies;
			}
			public void setDocumentFrequencies(
					ObjectCounter<String> documentFrequencies) {

				this.documentFrequencies = documentFrequencies;
			}
		}

		public class CorpusSearchResult extends SearchResult<String> {
			private Query query = null;
			private Set<Chunk> translations = new HashSet<Chunk>();
			private String renderedText = null;
			private String matchingTokenString = null;
			private int totalMatchCount = 0;
			private int highlightedMatchCount = 0;

			public String getMatchingTokenString() {
				return matchingTokenString;
			}
			public void setMatchingTokenString(String matchingTokenString) {
				this.matchingTokenString = matchingTokenString;
			}
			public Query getQuery() {
				return query;
			}
			public void setQuery(Query query) {
				this.query = query;
			}
			public Set<Chunk> getTranslations() {
				return translations;
			}
			public void setTranslations(Set<Chunk> translations) {
				this.translations = translations;
			}

			public CorpusSearchResult(Document doc) {	    
				setIdentifier(doc.get(QUERY.getName()));

				Query query = new Query(getIdentifier());
				setTitle(query.getDisplayQuery());
				if (getTitle() == null) {
					setTitle(doc.get(HEAD.getName()));
				}

				try {
					String chunkText = query.getChunk().getText();
					setContent(chunkText);
				} catch (InvalidQueryException e) {
					setContent("Sorry, the text for this result is not available.");
				}

				setQuery(query);
				try {
					Set<Chunk> translations = Resource.getResources(
							query.getChunk(),
							Resource.ResourceType.TRANSLATION);
					setTranslations(translations);
				} catch (InvalidQueryException iqe) {
					logger.warn("Problem getting chunk for query", iqe);
				}
			}

			public String getRenderedText() {
				return renderedText;
			}
			public void setRenderedText(String renderedText) {
				this.renderedText = renderedText;
			}

			public void setTotalMatchCount(int newTotalMatchCount) {
				totalMatchCount = newTotalMatchCount;
			}

			public int getTotalMatchCount() {
				return totalMatchCount;
			}

			public void setHighlightedMatchCount(int newHighlightedMatchCount) {
				highlightedMatchCount = newHighlightedMatchCount;
			}

			public int getHighlightedMatchCount() {
				return highlightedMatchCount;
			}
		}

		public int getHitsPerPage() {
			return hitsPerPage;
		}

		public void setHitsPerPage(int hitsPerPage) {
			this.hitsPerPage = hitsPerPage;
		}

		public Language getLanguage() {
			return language;
		}

		public void setLanguage(Language language) {
			this.language = language;
		}

		public int getResultsPerDocument() {
			return resultsPerDocument;
		}

		public void setResultsPerDocument(int resultsPerDocument) {
			this.resultsPerDocument = resultsPerDocument;
		}

		public Set<Query> getTargetDocuments() {
			return targetDocuments;
		}

		public void setTargetDocuments(Set<Query> targetDocuments) {
			this.targetDocuments = targetDocuments;
		}

		public List<String> getAllowedWords() {
			return allowedWords;
		}

		public void setAllowedWords(List<String> allowedWords) {
			this.allowedWords = allowedWords;
		}

		public List<String> getExcludedWords() {
			return excludedWords;
		}

		public void setExcludedWords(List<String> excludedWords) {
			this.excludedWords = excludedWords;
		}

		/*private List<String> getLemmas(List<String> words) {
	if (!language.getHasMorphData()) return Collections.emptyList();

	List<String> lemmatizedWords = new ArrayList<String>();

	for (String word : words) {
	    List<Lemma> lemmas = Lemmatizer.getLemmas(word, language.getCode());
	    for (Lemma lemma : lemmas) {
		lemmatizedWords.add(lemma.getDisplayForm());
	    }
	}

	return lemmatizedWords;
    }*/

		public boolean getExpandAllowedWords() {
			return expandAllowedWords;
		}

		public void setExpandAllowedWords(boolean expandAllowedWords) {
			this.expandAllowedWords = expandAllowedWords;
		}

		public boolean getExpandExcludedWords() {
			return expandExcludedWords;
		}

		public void setExpandExcludedWords(boolean expandExcludedWords) {
			this.expandExcludedWords = expandExcludedWords;
		}

		public boolean getExpandRequiredWords() {
			return expandRequiredWords;
		}

		public void setExpandRequiredWords(boolean expandRequiredWords) {
			this.expandRequiredWords = expandRequiredWords;
		}

		public String getRequiredPhrase() {
			return requiredPhrase;
		}

		public void setRequiredPhrase(String requiredPhrase) {
			this.requiredPhrase = requiredPhrase;
		}

		public List<String> getRequiredWords() {
			return requiredWords;
		}

		public void setRequiredWords(List<String> requiredWords) {
			this.requiredWords = requiredWords;
		}

		public Set<String> getPositiveTerms() {
			Set<String> terms = new HashSet<String>();
			terms.addAll(expandRequiredWords ?
					getExpandedForms(requiredWords) : requiredWords);
			terms.addAll(expandAllowedWords ?
					getExpandedForms(allowedWords) : allowedWords);

			return terms;
		}

		private Set<String> getExpandedForms(List<String> words) {
			if (!language.getHasMorphData()) return new HashSet<String>(words);
			Set<String> expandedForms = new HashSet<String>();
			for (String word : requiredWords) {
				expandedForms.addAll(Lemmatizer.getAllForms(word, language.getCode()));
			}
			return expandedForms;
		}

		public boolean getSortByRelevance() {
			return sortByRelevance;
		}

		public void setSortByRelevance(boolean sortByRelevance) {
			this.sortByRelevance = sortByRelevance;
		}

		private Set<Corpus> targetCollections;

		public void setTargetCollections(Set<Corpus> newTargetCollections) {
			targetCollections = newTargetCollections;
		}

		public Set<Corpus> getTargetCollections() {
			return targetCollections;
		}

}
