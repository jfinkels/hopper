package perseus.search;

import static perseus.search.LexiconIndexer.SearchField.HEAD;
import static perseus.search.LexiconIndexer.SearchField.LANGUAGE;
import static perseus.search.LexiconIndexer.SearchField.LEMMA;
import static perseus.search.LexiconIndexer.SearchField.LENGTH;
import static perseus.search.LexiconIndexer.SearchField.LEXICON;
import static perseus.search.LexiconIndexer.SearchField.LEXICON_QUERY;
import static perseus.search.LexiconIndexer.SearchField.REVERSED_TEXT;
import static perseus.search.LexiconIndexer.SearchField.SHORT_DEFINITION;
import static perseus.search.LexiconIndexer.SearchField.SORT;
import static perseus.search.LexiconIndexer.SearchField.SORTABLE_HEAD;
import static perseus.search.LexiconIndexer.SearchField.TEXT;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.hibernate.ScrollableResults;

import perseus.document.Chunk;
import perseus.document.DOMOffsetAdder;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.dao.HibernateChunkDAO;
import perseus.language.Language;
import perseus.language.LanguageAdapter;
import perseus.language.LanguageCode;
import perseus.language.analyzers.DeaccentingAnalyzer;
import perseus.morph.Lemma;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.util.Config;
import perseus.util.HibernateUtil;
import perseus.util.StringUtil;

/**
 * This class is responsible for indexing the data used in English-to-Greek
 * (and English-to-Latin, and so on) lookups. For each lemma in a given
 * language it indexes a headword, a short meaning, frequency information,
 * and the relevant text of each lexicon entry.
 */
public class LexiconIndexer {

	private Logger logger = Logger.getLogger(getClass());
	private Language language = Language.forCode(LanguageCode.GREEK);
	private LanguageAdapter adapter;

	private IndexWriter writer;

	private int lemmaCount;

	private int documentCount;
	private static final int DOCUMENT_LIMIT = 200;
        
	public enum SearchField {
		LEXICON("lexicon"),
		LEXICON_QUERY("lex_query"),
		TEXT("text"),
		REVERSED_TEXT("reversed"), // for end/substring searching
		LANGUAGE("lang"),
		LEMMA("lemma"),
		HEAD("head"),
		SORTABLE_HEAD("head_sortable"),
		SHORT_DEFINITION("short_def"),
		SORT("byNumber"),
		LENGTH("length");

		private String field;

		private SearchField(String field) {
			this.field = field;
		}

		public String getField() { return field; }
	}

	/**
	 * Create a new indexer.
	 *
	 * @param lang the language to index
	 */
	public LexiconIndexer(Language lang) {
		language = lang;
		adapter = lang.getAdapter();
		lemmaCount = 0;	
	}

	public static String getIndexPath(String languageCode) {
		String languagePath = languageCode + ".lexicon";
		File indexPath = new File(Config.getSearchIndexPath(), languagePath);
		if (!indexPath.exists()) {
			boolean success = indexPath.mkdirs();
			if (!success) {
				Logger.getLogger(LexiconIndexer.class).fatal(
						"Unable to create index directory "
						+ languagePath + "!");
				System.exit(1);
			}
		}

		return indexPath.getPath();
	}

	private void initWriter() throws IOException {
		writer =
			new IndexWriter(getIndexPath(), new DeaccentingAnalyzer(), true);
	}

	private String getIndexPath() {
		return getIndexPath(language.getCode());
	}

	/**
	 * Index all lemmas in this indexer's language.
	 */
	public void index() {
		String indexPath = getIndexPath();
		Analyzer analyzer = LanguageAdapter.getLanguageAdapter(
				LanguageCode.ENGLISH).getAnalyzer();

		try {
			initWriter();
			documentCount = 0;

			HibernateLemmaDAO dao = new HibernateLemmaDAO();
			ScrollableResults results = dao.getScrollableLemmas(language.getCode());
			while (results.next()) {
				Lemma lemma = (Lemma) results.get(0);
				indexLemma(lemma);

				documentCount++;
				if (documentCount % DOCUMENT_LIMIT == 0) {
					// To keep from running out of memory, 
					// flush the indexer every so often.
					writer.close();
					writer = new IndexWriter(indexPath, analyzer, false);

					logger.info(String.format("[%6d] %s",
							documentCount, lemma.getDisplayForm()));
				}

				HibernateUtil.getSession().evict(lemma);
			}

			cleanup();
		} catch (IOException ioe) {
			logger.fatal("Couldn't open index writer", ioe);
		}
	}

	public void cleanup() throws IOException {
		if (writer == null) initWriter();

		logger.info("Optimizing");
		writer.optimize();
		logger.info("Closing");
		writer.close();
		writer = null;
	}

	/**
	 * Indexes the specified lemma and its associated lexicon chunks.
	 *
	 * @param lemma the lemma to index
	 */
	public void indexLemma(Lemma lemma) throws IOException {
		if (writer == null) initWriter();
		logger.trace("Indexing " + lemma.getDisplayForm());

		Document luceneDoc = new Document();

		luceneDoc.add(new Field(LANGUAGE.getField(), language.getCode(),
				Field.Store.YES, Field.Index.UN_TOKENIZED));
		luceneDoc.add(new Field(LEMMA.getField(), lemma.getDisplayForm(),
				Field.Store.YES, Field.Index.NO));

		addLexiconFields(luceneDoc, lemma);
		//addFrequencyFields(luceneDoc, lemma);
		addHeaderFields(luceneDoc, lemma);

		try {
			writer.addDocument(luceneDoc);
		} catch (IOException ioe) {
			logger.error("Problem writing document " + luceneDoc, ioe);
		}

	}

	public void deleteLemma(Lemma lemma) {
		try {
			cleanup();
			IndexReader reader = IndexReader.open(getIndexPath());
			reader.deleteDocuments(
					new Term(LEMMA.getField(), lemma.getDisplayForm()));
		} catch (IOException ioe) {
			logger.error("Problem deleting lemma " + lemma.getDisplayForm(), ioe);
			ioe.printStackTrace();
		}
	}

	private void addLexiconFields(Document document, Lemma lemma) {
		Set<Chunk> lexiconChunks = lemma.getLexiconEntries();

		int longestLength = 0;
		for (Chunk lexiconChunk : lexiconChunks) {
			org.jdom.Document chunkElement = null;
	        try {
	            chunkElement = DOMOffsetAdder.domFromChunk(lexiconChunk);
	        } catch (Exception e) {
	            logger.error("Unable to read chunk; no short def. available", e);
	        }
	        String chunkText = "";
	        if (chunkElement != null) {
	        	chunkText = Lemma.extractDefinition(chunkElement);
	        	if (chunkText == null) {
	        		chunkText = "";
	        	}
	        }
			String lexiconName =
				lexiconChunk.getMetadata().get(Metadata.ALTERNATIVE_TITLE_KEY);
			Query lexiconQuery = lexiconChunk.getQuery();

			document.add(new Field(LEXICON.getField(), lexiconName,
					Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field(LEXICON_QUERY.getField(),
					lexiconQuery.toString(),
					Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field(TEXT.getField(), chunkText,
					Field.Store.NO, Field.Index.TOKENIZED));
			String reversedText = StringUtils.reverse(chunkText);
			document.add(new Field(REVERSED_TEXT.getField(), reversedText,
					Field.Store.NO, Field.Index.TOKENIZED));

			if (chunkText.length() > longestLength) {
				longestLength = chunkText.length();
			}
			HibernateUtil.getSession().evict(HibernateChunkDAO.chunkToRow(lexiconChunk));
		}
		if (longestLength > 0) {
			document.add(new Field(LENGTH.getField(),
					Integer.toString(longestLength),
					Field.Store.YES, Field.Index.UN_TOKENIZED));
			logger.debug(lemma + " -> " + longestLength);
		}
	}

	private void addHeaderFields(Document document, Lemma lemma) {
		// Lucene doesn't seem to handle entities well (sometimes, after
		// reading the last entity in a token, it cuts the rest of the token
		// off), so replace them with their UTF-8 equivalents.
		String headText =
			StringUtil.replaceNumericEntities(lemma.getLemma()).toString();
		document.add(new Field(HEAD.getField(), headText,
				Field.Store.YES, Field.Index.UN_TOKENIZED));

		// Macrons will confound our attempts to sort by dictionary headword,
		// thanks to their place in the Unicode character set, so index
		// a sortable version of the head text with them removed.
		String sortableHeadText = adapter.toLowerCase(
				StringUtil.dropMacrons(headText));

		document.add(new Field(SORTABLE_HEAD.getField(), sortableHeadText,
				Field.Store.NO, Field.Index.UN_TOKENIZED));

		String shortDefinition = "";
		if (lemma != null) {
			shortDefinition = StringUtil.noNull(lemma.getShortDefinition());
		}

		document.add(new Field(SHORT_DEFINITION.getField(), shortDefinition,
				Field.Store.YES, Field.Index.NO));

		document.add(new Field(SORT.getField(), Integer.toString(lemmaCount),
				Field.Store.NO, Field.Index.UN_TOKENIZED));
		lemmaCount++;
	}
	
	public static void main(String[] args) {
		if (args.length == 1) {
			String langCode = args[0];
			LexiconIndexer indexer = new LexiconIndexer(Language.forCode(langCode));
			indexer.index();
		}
		else {
			Set<Language> languages = Language.getAll();
			for (Language language : languages) {
				LexiconIndexer indexer = new LexiconIndexer(language);
				indexer.index();      
			}
		}
	}
}
