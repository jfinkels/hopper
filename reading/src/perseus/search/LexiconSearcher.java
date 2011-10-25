package perseus.search;

import static org.apache.lucene.search.BooleanClause.Occur.SHOULD;
import static perseus.search.LexiconIndexer.SearchField.LANGUAGE;
import static perseus.search.LexiconIndexer.SearchField.LEMMA;
import static perseus.search.LexiconIndexer.SearchField.LEXICON_QUERY;
import static perseus.search.LexiconIndexer.SearchField.REVERSED_TEXT;
import static perseus.search.LexiconIndexer.SearchField.TEXT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import perseus.language.Language;
import perseus.language.LanguageCode;
import perseus.morph.Lemma;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.search.nu.LemmaSearchResult;
import perseus.search.nu.SearchResults;
import perseus.search.nu.SearchTypes;
import perseus.util.Range;

public class LexiconSearcher {

	public static enum SearchMethod {
		START(TEXT, true),
		END(REVERSED_TEXT, true),
		EXACT(TEXT, false),
		SUBSTRING {
			public Query getQuery(String keyword) {
				BooleanQuery query = new BooleanQuery();
				query.add(new WildcardQuery(
						new Term(TEXT.getField(), keyword + "*")), SHOULD);
				query.add(new WildcardQuery(
						new Term(REVERSED_TEXT.getField(),
								StringUtils.reverse(keyword) + "*")), SHOULD);

				return query;
			}
		};

		private SearchMethod() {}

		private SearchMethod(LexiconIndexer.SearchField field, boolean wild) {
			targetField = field;
			useWildcard = wild;
		}

		private LexiconIndexer.SearchField targetField;
		private boolean useWildcard = false;

		public Query getQuery(String keyword) {
			String searchTerm = keyword;
			if (targetField == REVERSED_TEXT) {
				searchTerm = StringUtils.reverse(searchTerm);
			}
			if (useWildcard) searchTerm = searchTerm + "*";

			Term term = new Term(targetField.getField(), searchTerm);
			return useWildcard ?
					new WildcardQuery(term) :
						new TermQuery(term);
		}
	}

	public static enum SortByField {
		RELEVANCE("rel"),
		LENGTH("length"),
		FREQUENCY("freq"),
		HEADWORD("alpha");

		private SortByField(String field) {
			this.field = field;
		}

		private String field;

		public String getField() { return field; }
	}

	private Language targetLanguage = Language.forCode(LanguageCode.GREEK);
	private boolean searchAsPrefix = false;
	private List<Query> targetLexica = new ArrayList<Query>();

	private Logger logger = Logger.getLogger(getClass());

	private SearchMethod searchMethod = SearchMethod.EXACT;

	public LexiconSearcher() {}

	public SearchResults search(String term, Range<Integer> range) {
		try {
			Hits hits = getHits(term);
			return resultsFromHits(hits, range);
		} catch (IOException ioe) {
			logger.warn("Problem searching definitions", ioe);
			throw new SearchException(ioe);
		}
	}

	private Hits getHits(String keyword) throws IOException {

		IndexSearcher searcher = new IndexSearcher(
				LexiconIndexer.getIndexPath(targetLanguage.getCode()));
		BooleanQuery mainQuery = new BooleanQuery();
		mainQuery.add(
				new TermQuery(
						new Term(LANGUAGE.getField(), targetLanguage.getCode())),
						BooleanClause.Occur.MUST);

		if (!targetLexica.isEmpty()) {
			BooleanQuery docQuery = new BooleanQuery();
			for (Query query : targetLexica) {
				docQuery.add(
						new TermQuery(
								new Term(LEXICON_QUERY.getField(),
										query.toString())),
										BooleanClause.Occur.SHOULD);
			}
			mainQuery.add(docQuery, BooleanClause.Occur.MUST);
		}

		Query keywordQuery = searchMethod.getQuery(keyword);
//		Query keywordQuery = (searchAsPrefix ?
//		new WildcardQuery(new Term(TEXT.getField(), keyword + "*")) :
//		new TermQuery(new Term(TEXT.getField(), keyword)));

		mainQuery.add(keywordQuery, BooleanClause.Occur.MUST);

//		Sort sort = new Sort(new SortField(
//		SortByFieldRELEVANCE.getField(), SortField.INT, true));

		return searcher.search(mainQuery,
				new Sort(LexiconIndexer.SearchField.SORTABLE_HEAD.getField()));
	}

	private SearchResults resultsFromHits(Hits hits, Range<Integer> range) 
	throws IOException {

		SearchResults<LemmaSearchResult> results =
			new SearchResults<LemmaSearchResult>();
		results.setTotalHitCount(hits.length());
		results.setType(SearchTypes.LEXICON_TYPE);

		HibernateLemmaDAO dao = new HibernateLemmaDAO();

		Set<Lemma> allMatches = new TreeSet<Lemma>();
		for (int idx = range.getStart(); idx < range.getEnd(); idx++) {
			if (idx >= hits.length()) break;

			Document doc = hits.doc(idx);
			String identifier = doc.get(LEMMA.getField());
			String language = doc.get(LANGUAGE.getField());

			for (Lemma lemma : dao.getMatchingLemmas(identifier, language, false)) {
				// make sure chunks are loaded from session
				// actually, do this in the DAO call above
				// lemma.getLexiconChunks();
				allMatches.add(lemma);
			}
		}

		for (Lemma lemma : allMatches) {
			results.add(new LemmaSearchResult(lemma));
		}

		return results;
	}

	public boolean isSearchAsPrefix() {
		return searchAsPrefix;
	}

	public void setSearchAsPrefix(boolean searchAsPrefix) {
		this.searchAsPrefix = searchAsPrefix;
	}

	public Language getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(Language targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public List<Query> getTargetLexica() {
		return targetLexica;
	}

	public void setTargetLexica(List<Query> targetLexica) {
		this.targetLexica = targetLexica;
	}

	public SearchMethod getSearchMethod() {
		return searchMethod;
	}

	public void setSearchMethod(SearchMethod searchMethod) {
		this.searchMethod = searchMethod;
	}
}
