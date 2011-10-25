package perseus.search.nu;

import static perseus.search.nu.CorpusIndexer.SearchField.COLLECTION;
import static perseus.search.nu.CorpusIndexer.SearchField.DOCUMENT_ID;
import static perseus.search.nu.CorpusIndexer.SearchField.HEAD;
import static perseus.search.nu.CorpusIndexer.SearchField.LEMMATIZED;
import static perseus.search.nu.CorpusIndexer.SearchField.OFFSET;
import static perseus.search.nu.CorpusIndexer.SearchField.QUERY;
import static perseus.search.nu.CorpusIndexer.SearchField.TEXT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexModifier;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;

import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.NoTagsTokenFilter;
import perseus.document.Query;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.document.Renderer;
import perseus.document.Token;
import perseus.document.TokenList;
import perseus.language.Language;
import perseus.language.LanguageAdapter;
import perseus.morph.Lemmatizer;
import perseus.util.Config;
import perseus.util.StringUtil;

public class CorpusIndexer extends RecentlyModifiedCorpusProcessor {
	
    private static Logger logger = Logger.getLogger(CorpusIndexer.class);
    private static final Integer CHUNK_LIMIT = 300;

    private boolean lemmatizeText = true;
    private Semaphore luceneSem;
    private Map<Language, List<Document>> indexedChunks = new HashMap<Language, List<Document>>();

    public CorpusIndexer(Semaphore luceneSem) {
    	super();
    	this.luceneSem = luceneSem;
    }

    public CorpusIndexer() {
    	this(new Semaphore(1));
    }
    
    public String getTaskName() {
		return "build-indexes";
	}
    
    public enum SearchField {
	DOCUMENT_ID("doc_id"),
	COLLECTION("collections"),
	OFFSET("offset"),
	HEAD("head"),
	TEXT("text"),
	LEMMATIZED("lemma_text"),
	QUERY("query");

	private String name;

	private SearchField(String name) { this.name = name; }
	public String getName() { return name; }
    }

    private List<Language> getIndexedLanguages() {
	List<Language> indexedLanguages = new ArrayList<Language>();

	File searchDirectory = new File(Config.getSearchIndexPath());

	for (Language language : Language.getAll()) {
	    File potentialIndex = new File(searchDirectory, language.getCode());
	    if (potentialIndex.isDirectory()) {
		indexedLanguages.add(language);
	    }
	}

	return indexedLanguages;
    }

    public static String getIndexPath(Language language) {
	return new File(Config.getSearchIndexPath(), language.getCode()).getPath();
    }

    private MyIndexModifier getIndexer(Language language)
	throws IOException {

	MyIndexModifier modifier;
	String indexPath = getIndexPath(language);

	    File indexFile = new File(indexPath);
	    boolean needToCreate = false;
	    if (!indexFile.exists()) {
		boolean gotDirectory = indexFile.mkdirs();
		if (!gotDirectory) {
		    throw new IOException("Problem creating " + indexFile);
		}

		needToCreate = true;
	    }

	    Analyzer analyzer = LanguageAdapter.getLanguageAdapter(
				language).getAnalyzer();

	    modifier = new MyIndexModifier(
				indexFile.getPath(), analyzer, needToCreate);
			
	    logger.debug(String.format("%s -> %s",
				       language,
				       analyzer.getClass().getSimpleName()));

	return modifier;
    }

    private MyIndexModifier getIndexerForDocument(String documentID)
	throws IOException {

	Query query = new Query(documentID);
	return getIndexer(query.getMetadata().getLanguage());
    }

    public boolean shouldProcessDocument(Chunk documentChunk) {
	Metadata metadata = documentChunk.getMetadata();

	return (!metadata.getLanguage().equals(Language.UNKNOWN) &&
		super.shouldProcessDocument(documentChunk));
    }

    public void startDocument(Chunk documentChunk) {
	try {
	    logger.info("Deleting existing entries for "
			+ documentChunk.getQuery());
	    delete(documentChunk.getQuery().toString());
	} catch (IOException ioe) {
	    logger.warn("Problem deleting search index for " +
			documentChunk.getQuery(), ioe);
	}
	logger.info("Indexing document " + documentChunk.getQuery());
    }

    public void endDocument(Chunk documentChunk) {
    	try {
    		for (Language lang : indexedChunks.keySet()) {
    			try {
    				luceneSem.acquire();
    			} catch 	(InterruptedException e) {
    				logger.trace("Thread interrupted");
    			}
    			int count = 0;
    			MyIndexModifier indexer = getIndexer(lang);
    			List<Document> chunks = indexedChunks.get(lang);
    			for (Document doc : chunks) {
    				indexer.addDocument(doc);
    				count++;
    				
    				if (count > CHUNK_LIMIT) {
    					indexer.flush();
    					count = 0;
    				}
    			}
    			indexer.close();
    			luceneSem.release();
    		}
    	} catch (IOException ie) {
    		logger.warn("Problem adding document to index", ie);
    	}
    indexedChunks.clear();
	super.endDocument(documentChunk);
	logger.info("Finished " + documentChunk.getQuery());
    }

    public void processChunk(Chunk documentChunk) {
	logger.debug("Starting chunk " + documentChunk);
	Document luceneDoc = new Document();

	Query query = documentChunk.getQuery();
	Language language = Language.forCode(documentChunk.getEffectiveLanguage());

	Renderer textRenderer = new Renderer(language, new NoTagsTokenFilter());
	String bodyText = textRenderer.renderText(documentChunk.getText());
	String headText = textRenderer.renderText(documentChunk.getHead());

	documentChunk.loadContainingHeads();
	luceneDoc.add(new Field(QUERY.getName(), query.toString(),
				Field.Store.YES, Field.Index.UN_TOKENIZED));
	luceneDoc.add(new Field(DOCUMENT_ID.getName(),
				query.getInnermostDocumentID(),
				Field.Store.YES, Field.Index.UN_TOKENIZED));
	luceneDoc.add(new Field(HEAD.getName(), headText,
				Field.Store.YES, Field.Index.NO));
	luceneDoc.add(new Field(OFFSET.getName(),
				Integer.toString(documentChunk.getStartOffset()),
				Field.Store.NO,
				Field.Index.UN_TOKENIZED
				));

	Field bodyField = new Field(TEXT.getName(), bodyText,
				    Field.Store.NO, Field.Index.TOKENIZED);
	luceneDoc.add(bodyField);
	if (language.getHasMorphData() && lemmatizeText) {
	    String lemmatizedText = lemmatizeText(bodyText, language);
	    if (!lemmatizedText.equals("")) {
	    	luceneDoc.add(new Field(LEMMATIZED.getName(),
				    lemmatizedText,
				    Field.Store.NO, Field.Index.TOKENIZED));
	    }
	}

	List<String> documentIDs = new ArrayList<String>();
	documentIDs.add(query.getDocumentID());
	documentIDs.add(query.getInnermostDocumentID());
	documentIDs.addAll(query.getMetadata().getList(Metadata.CORPUS_KEY));
	documentIDs.addAll(query.getMetadata().getList(Metadata.ABO_KEY));
	for (String docID : documentIDs) {
	    luceneDoc.add(new Field(COLLECTION.getName(), docID,
				    Field.Store.YES, Field.Index.UN_TOKENIZED));
	}

		List<Document> chunksList = indexedChunks.get(language);
		if (chunksList == null) {
			chunksList = new ArrayList<Document>();
		}
		chunksList.add(luceneDoc);
		indexedChunks.put(language, chunksList);
    }

    public boolean delete(String documentID) throws IOException {
    	try {
			luceneSem.acquire();
		} catch (InterruptedException e) {
			logger.trace("Thread interrupted");
		}
		MyIndexModifier indexer = getIndexerForDocument(documentID);
		boolean deleted = indexer.deleteDocuments(getDocTerm(documentID)) > 0;
		indexer.close();
		luceneSem.release();
		return deleted;
    }

    public void optimize(Language language) throws IOException {
	MyIndexModifier indexer = getIndexer(language);
	indexer.optimize();
	indexer.close();
    }

    public void optimize() throws IOException {
	for (Language language : getIndexedLanguages()) {
	    logger.info("Optimizing index for " + language.getName());
	    optimize(language);
	}
    }

    private Term getDocTerm(String documentID) {
	return new Term(DOCUMENT_ID.getName(), documentID);
    }

    private String lemmatizeText(String text, Language language) {
	Set<String> forms = new HashSet<String>();

	TokenList tokens = TokenList.getTokens(text, language)
	    .getTokensOfType(Token.Type.WORD);
	for (Token token : tokens) {
	    String tokenText = token.getOriginalText();
	    forms.add(tokenText);
	}
	
	if (!forms.isEmpty()) {
		return StringUtil.join(Lemmatizer.getAllForms(forms, language), " ");	
	}
	
	return "";
    }

    /**
     * A subclass of Lucene's {@link IndexModifier} class with a few extra
     * useful methods. (Lucene's documentation encourages us to subclass
     * the IndexModifier if it doesn't have methods from the Reader or
     * Writer classes that we want.)
     */
    class MyIndexModifier extends IndexModifier {
	public MyIndexModifier(String file, Analyzer an, boolean create)
	    throws IOException {

	    super(file, an, create);
	}

	public int docFreq(Term term) throws IOException {
	    createIndexReader();
	    return indexReader.docFreq(term);
	}

	public Directory directory() throws IOException {
	    createIndexReader();
	    return indexReader.directory();
	}

	public TermEnum terms() throws IOException {
	    createIndexReader();
	    return indexReader.terms();
	}
    }

    public static void main(String[] args) {
	CorpusIndexer indexer = new CorpusIndexer();

	String[] effectiveArgs = args;
	Options options = new Options()
	    .addOption("L", "no-lemmatize", false,
		       "don't store lemmatized text")
	    .addOption("f", "force", false,
		       "force loading, even if file unchanged")
	    .addOption("o", "optimize", false,
		       "optimize existing indexes (do this before using them on a server)")
	    .addOption("h", "help", false, "print this message")
	    .addOption(OptionBuilder.withLongOpt("threads")
				.withDescription("set number of threads to use")
				.hasArg()
				.withArgName("NUMBER")
				.create());
	
	CommandLineParser parser = new PosixParser();
	boolean hasForce = false;
	boolean lemmatizeText = true;
	//use the minimum # of cpu's available to this machine, but allow user to change #
	int THREADS = Runtime.getRuntime().availableProcessors();

	try {
	    CommandLine cl = parser.parse(options, args);

	    if (cl.hasOption("help") || cl.hasOption('h')) {
		new HelpFormatter().printHelp("CorpusIndexer", options);
		System.exit(1);
	    }

	    if (cl.hasOption("force") || cl.hasOption('f')) {
	    	hasForce = true;
		indexer.setIgnoreModificationDate(true);
	    }
	    if (cl.hasOption("no-lemmatize") || cl.hasOption('L')) {
	    	lemmatizeText = false;
		indexer.setLemmatizeText(false);
	    }
	    if (cl.hasOption("optimize") || cl.hasOption('o')) {
		indexer.optimize();
		System.exit(0);
	    }
	    if (cl.hasOption("threads")) {
	    	String option = cl.getOptionValue("threads");
	    	if (option != null && !option.equals("")) {
	    		THREADS = Integer.parseInt(option);
	    	}
	    }
	    effectiveArgs = cl.getArgs();
	} catch (ParseException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	if (effectiveArgs.length > 0) {
	    for (String arg : effectiveArgs) {
		indexer.processAnything(arg);
	    }
	} else {
		ExecutorService exec = Executors.newFixedThreadPool(THREADS);
		List<String> documentIDs = perseus.document.Document.getTexts();
		Collections.shuffle(documentIDs);
		Semaphore luceneSem = new Semaphore(1);
		for (String documentID : documentIDs) {
			Query documentQuery = new Query(documentID);
			CorpusIndexer loader = new CorpusIndexer(luceneSem);
			loader.setIgnoreModificationDate(hasForce);
			loader.setLemmatizeText(lemmatizeText);
			loader.setQuery(documentQuery);
			exec.execute(loader);
		}
		exec.shutdown();
	}
    }

    public boolean getLemmatizeText() {
	return lemmatizeText;
    }

    public void setLemmatizeText(boolean lemmatizeText) {
	this.lemmatizeText = lemmatizeText;
    }
}
