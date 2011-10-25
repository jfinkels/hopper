package perseus.document;

import static perseus.document.Metadata.SUBDOC_REF_KEY;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.util.Config;
import perseus.util.HibernateUtil;

/**
 * This class does nothing by itself, but provides a framework for
 * applications that loop over many documents within a corpus.  The class
 * defines several callback methods at numerous levels.
 *
 * The behavior of individual instances of subclasses can be further customized
 * via the getOption() and setOption() methods. At present, the sole option
 * used by the CorpusProcessor class itself is CHUNK_TYPE, which can be one of
 * two options: DEFAULT_CHUNK (process the default chunk for each
 * document--this is set by default) or INNERMOST_CHUNK (process the smallest
 * possible chunk for each document).
 *
 * processAnything() is the only method you'll really need to call when
 * invoking this class from somewhere else; it should handle every possible
 * type of input.
 */

public class CorpusProcessor implements Runnable {
    
    // This stores any configuration options that apply to this instance of the
    // CorpusProcessor.
    private Map<String,String> options = new HashMap<String,String>();
    private Logger logger = Logger.getLogger(getClass());
    private Query query;

    public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public CorpusProcessor() {
		this(null);
    }
    
    public CorpusProcessor(Query query) {
    	setOption(CHUNK_TYPE, DEFAULT_TYPE);
    	setOption(SUBDOC_METHOD, SEPARATE_DOCS);
    	this.query = query;
    }
    
    /*
     * The CHUNK_TYPE option affects the chunk scheme used by the this
     * CorpusProcessor. The DEFAULT_TYPE setting (the default!) will
     * use the default chunk scheme; the INNERMOST_TYPE setting will use
     * whatever chunk scheme provides the finest granularity. For example,
     * if we're processing Thucydides, the former setting will chunk by
     * book:chapter (the default), the latter by book:chapter:section.
     * 
     * Using INNERMOST_TYPE only really useful if you need to deal with
     * existing data that has been marked as such, as in the case of some files
     * from the old Perl hopper (.ref, .dat, etc.).
     */
    public static final String CHUNK_TYPE = "_chunk-type_";
    public static final String DEFAULT_TYPE = "_default-type_";
    public static final String INNERMOST_TYPE = "_innermost-type_";
    
    /*
     * The SUBDOC_METHOD option controls how we handle subdocuments.
     * If it's set to SEPARATE_DOCS, we call processDocument(Chunk) for
     * each subdocument; if it's set to ONE_DOC, we call
     * processDocument(Chunk) once for the whole document and then
     * processChunk() on each individual chunk, as we would with a normal
     * document.
     */
    public static final String SUBDOC_METHOD = "_subdoc-method_";
    public static final String SEPARATE_DOCS = "_sep-docs_";
    public static final String ONE_DOC = "_one-doc_";
    
    public static void main (String args[]) {
	CorpusProcessor processor = new CorpusProcessor();
	processor.processCorpus();
    }
    
    /** 
     Loop through all the documents in a corpus. This method
     fires processDocument calls for every document in the corpus.
     For documents with subdocuments (like the Bible or Cicero),
     processDocument is called for every subdocument, but NOT
     for the main document, unless the SUBDOC_METHOD option has been
     set to ONE_DOC.
     */
    public void processCorpus() {
	List<String> documentIDs = Document.getTexts();
	for (String documentID : documentIDs) {
	    Query documentQuery = new Query(documentID);
	    handleDocumentQuery(documentQuery);
	}
    }

	public void run() {
		logger.info("Starting "+query.getDocumentID());
		handleDocumentQuery(query);
		logger.info("Ending "+query.getDocumentID());
	}
    
    /**
     An alternate starting point. Takes the same actions as processCorpus()
     but for a single document ID.
     */
    public void processDocument(String documentID) {
	Query documentQuery = new Query(documentID);
	handleDocumentQuery(documentQuery);
    }
    
    /**
     * A third starting point. Processes every document within a given
     * collection, like "Perseus:collection:Greco-Roman" or
     * "Perseus:collection:cwar".
     */
    public void processCollection(String collectionID) {
	
	List<Query> corpusDocuments = new Corpus(collectionID).getCorpusDocuments();
	for (Query docQuery : corpusDocuments) {	
	    // skip subdocuments, which will get taken care of when we
	    // process the parent documents
	    if (!docQuery.isJustDocumentID()) continue;
	    
	    handleDocumentQuery(docQuery);
	}
    }
    
    /**
     * Yet another starting point. Processes every text mentioned in the given
     * catalog (like "classics.xml").
     */
    public void processCatalog(String catalogFilename) {
	File catalogFile = new File(catalogFilename);
	if (!catalogFile.isAbsolute()) {
	    catalogFile = new File(Config.getCatalogPath(), catalogFilename);
	}
	
	try {
	    MetadataStore store =
		MetadataLoader.loadUncachedCatalog(catalogFile);
	    for (Iterator it = store.queryIterator(); it.hasNext(); ) {
		Query query = (Query) it.next();
		if (!query.isJustDocumentID()) continue;
		
		// get the query from the database--don't use the catalog copy,
		// which will lack some fields
		Metadata metadata = MetadataCache.get(query);
		if (metadata.has(Metadata.TYPE_KEY) &&
			metadata.get(Metadata.TYPE_KEY).equals("text")) {
		    
		    // don't process collections within the file, which could
		    // lead to problems for files that define both collections
		    // and their constituent documents--only process texts
		    processDocument(query.getDocumentID());
		}
	    }
	} catch (MetadataExtractionException mee) {
	    mee.printStackTrace();
	    return;
	}
    }
    
    /**
     * A general-purpose method to process documentIDs, collections, catalogs,
     * or files on disk. It does its best to figure out what, specifically, is
     * supposed to be processed, and passes its target off to the appropriate
     * function.
     *
     * It knows how to process ids of the following sorts:
     * <ul>
     * <li>Normal document IDs (e.g., Perseus:text:1999.01.0001)</li>
     * <li>Collection IDs (e.g., Perseus:collection:Greco-Roman,
     *	Perseus:corpus:perseus,author,Aeschylus) - will process all documents
     *	in the collection</li>
     * <li>Catalogs (e.g., classics.xml, /sgml/texts/DDBDP/collection.xml) -
     *	will process all document IDs with RDF entries in the catalog</li>
     * <li>XML source files, either absolute or relative to the texts path
     *  (e.g., /sgml/texts/Plato/plat.rep_gk.xml, Aeschines/aeschin_gk.xml) -
     *  will process whatever document ID has the given argument as its
     *  source file</li>
     * </ul>
     */
    public void processAnything(String id) {
	// Is it a document ID?
	Query query = new Query(id);
	Metadata metadata = query.getMetadata();
	
	// (if it's processable, it'll have a type)
	if (metadata.has(Metadata.TYPE_KEY)) {
	    String type = metadata.get(Metadata.TYPE_KEY);
	    if (type.equals("text")) {
		handleDocumentQuery(query);
		return;
	    } else if (type.equals("collection") || type.equals("corpus")) {
		processCollection(id);
		return;
	    }
	}
	
	// (although some corpora lack types but have corpus type keys; check
	// for one before writing this off as a non-document)
	if (metadata.has(Metadata.CORPUS_TYPE_KEY)) {
	    processCollection(id);
	    return;
	}
	
	// Okay, is it a catalog we recognize?
	String[] knownCatalogs = Config.getCatalogFiles();
	for (int i = 0; i < knownCatalogs.length; i++) {
	    if (id.equals(knownCatalogs[i])) {
		processCatalog(id);
		return;
	    }
	}
	
	// Okay, is it an XML source text for which we've loaded metadata?
	// (Don't worry about those for which we haven't.)
	try {
	    MetadataDAO dao = new SQLMetadataDAO();
	    List<Query> matchingDocuments =
		dao.getDocuments(Metadata.SOURCE_TEXT_PATH_KEY,
			id, null, true);
	    
	    if (matchingDocuments.isEmpty()) {
	    	// if no matches, see if it was an absolute path.
	    	// (remove the source-text path plus, possibly, a slash)
	    	String sourcePath = Config.getSourceTextsPath();
	    	if (id.startsWith(sourcePath)) {
	    		String relativeId = id.replaceAll(
	    				sourcePath + File.separatorChar + "?", "");
	    		matchingDocuments = dao.getDocuments(
	    				Metadata.SOURCE_TEXT_PATH_KEY,
	    				relativeId, null, true);
	    	}
	    }
	    else {
	    	for (Query match : matchingDocuments) {
	    		processDocument(match.getDocumentID());
	    	}
	    	return;
	    }
	} catch (Exception e) {
	    // *shrug*
	    e.printStackTrace();
	}
	
	// Otherwise, give up!
	throw new UnsupportedOperationException("Not sure how to process '"
		+ id + "'");
    }

    /**
     * A helper method for our processing methods.
     */
    private void handleDocumentQuery(Query documentQuery) {
	Chunk initialChunk = Chunk.getInitialChunk(documentQuery);
	Metadata metadata = initialChunk.getMetadata();
	
	if (!shouldProcessDocument(initialChunk)) {
	    skippedDocument(initialChunk);
	    return;
	}
	
	String subdocMethod = getOption(SUBDOC_METHOD);

	boolean hasSubdocs = false;
	List<Chunk> subdocs = new ArrayList<Chunk>();
	
	if (metadata.has(SUBDOC_REF_KEY)) {
	    hasSubdocs = true;
	    subdocs = initialChunk.getSubTexts();
	}
	
	// We need to fire the start(), end() and process() methods
	// either only once for the whole document or for every 
	// subdocument.
	if (!hasSubdocs || subdocMethod.equals(ONE_DOC)) {
	    startDocument(initialChunk);
	}
	
	if (hasSubdocs) {
	    for (Chunk chunk : subdocs) {				
		if (subdocMethod.equals(SEPARATE_DOCS)) {
		    startDocument(chunk);
		}
		
		processDocument(chunk);
		
		if (subdocMethod.equals(SEPARATE_DOCS)) {
		    endDocument(chunk);
		}
	    }
	}
	else {
	    processDocument(initialChunk);
	}
	
	if (!hasSubdocs || subdocMethod.equals(ONE_DOC)) {
	    endDocument(initialChunk);
	}

	// (is this a good idea? it hopefully prevents chunks from huge
	// documents from causing OutOfMemoryErrors...)
	logger.debug("Clearing session...");
	HibernateUtil.getSession().clear();
	logger.debug("Evicting chunks from SessionFactory...");
	HibernateUtil.getSession().getSessionFactory().evict(
	    HibernateChunkDAO.ChunkRow.class);
	logger.debug("Continuing...");
    }

    /**
     * Called when a document has been skipped, usually because
     * shouldProcessDocument() returned false.
     * 
     * @param documentChunk a chunk representing the document being skipped
     */
    public void skippedDocument(Chunk documentChunk) {
    }

    /** 
     This method is called for every document. It first queries
     the shouldProcessDocument() method. If that returns false, 
     it immediately returns. Otherwise it calls startDocument(),
     loops through all default chunks in the document, firing
     processChunk() calls as it goes, and finally calls 
     endDocument().
     */
    public void processDocument(Chunk documentChunk) {
	
	Metadata documentMetadata = documentChunk.getMetadata();
	
	String defaultScheme = documentMetadata.getChunkSchemes().getDefaultScheme();
	
	// the scheme would be null if this chunk was the wrapper for a 
	// set of sub-documents.
	if (defaultScheme == null) { return; }
	
	// We may want to access a document's chunks in one of two ways:
	// (a) access every default chunk, or (b) access every "innermost"
	// (i.e., smallest) chunk. (a) is the default, but check whether the
	// user has set (b).
//	String chunkTypeOption = getOption(CHUNK_TYPE);
//	String defaultChunkType;
//	
//	if (chunkTypeOption != null && chunkTypeOption.equals(INNERMOST_TYPE)) {
//	    List<String> schemes = ChunkSchemes.typesForScheme(defaultScheme);
//	    defaultChunkType = schemes.get(schemes.size()-1);
//	} else {
//	    defaultChunkType = ChunkSchemes.defaultTypeForScheme(defaultScheme);
//	}

	// Entry-based works are often exceptionally large, and retrieving all
	// the chunks at once can cause us to run out of memory, as the LSJ
	// tends to. If we need to deal with an entry-based work, use a
	// ChunkFactory instead, and iterate through the chunks.
	
	List<String> possibleSchemes =
	    documentMetadata.getChunkSchemes().schemesWithDefaultChunk();
	if (possibleSchemes.isEmpty()) {
	    possibleSchemes.add(defaultScheme);
	}
	
	try {
	    for (String scheme : possibleSchemes) {
		try {
		    ChunkFactory factory = ChunkFactory.forDocument(
			    documentChunk.getQuery(), scheme);
		    for (Chunk chunk : factory) {
			processChunk(chunk);
		    }
		    break;

		} catch (NoSuchElementException nsee) {
		    continue;
		}
	    }
	} catch (Exception e) {
	    reportException(documentChunk, e);
/*	} finally {
	    if (factory != null) factory.cleanup();
*/	}
    }
    
    /**
     This method is called for every default chunk in every processed document.
     It starts by firing a startChunk() call, then tokenizes the text,
     then loops through all TEXT tokens, firing processToken() calls.
     Finally it calls endChunk().
     */
    public void processChunk(Chunk chunk) {
	
	startChunk(chunk);
	
	// For each word in the chunk, find lemmas and record them.
	
	try {
	    /*
	    String chunkText =
		StyleTransformer.transform(chunk.getText(), "striptext.xsl");
	    
	    TokenList tokens = TokenList.getTextTokens(
		    chunkText, chunk.getMetadata().getLanguage());
	    */
	    TokenList tokens = TokenList.getTokensFromXML(chunk);

	    for (Token token : tokens) {
		processToken(token);
	    }
	    
	} catch (Exception e) {
	    reportException(chunk, e);
	}
	
	endChunk(chunk);
    }
    
    /** 
     Called at the beginning of every processable document.
     */
    public void startDocument(Chunk documentChunk) {

    }
    
    /** 
     Called at the end of every processable document.
     */
    public void endDocument(Chunk documentChunk) {
    }
    
    /** 
     Called at the beginning of every default chunk of
     every processable document.
     */
    public void startChunk(Chunk chunk) {
	
    }
    
    /** 
     Called at the end of every default chunk of
     every processable document.
     */
    public void endChunk(Chunk chunk) {
	
    }
    
    /**
     Called for every token in processable chunks
     */
    public void processToken(Token token) {
	
    }
    
    /**
     This method allows subclasses to define which documents
     should be processed. Examples might include checking whether
     a document is in a particular collection, whether it is in English,
     whether it is a primary document, etc.
     */
    public boolean shouldProcessDocument(Chunk documentChunk) {
	return true;
    }
    
    /**
     All exceptions thrown during the chunk iteration process produce 
     calls to this method. There are try-catch blocks at 
     */
    public void reportException(Chunk chunk, Exception e) {
	logger.warn(chunk + ": ", e);
	e.printStackTrace();
    }
    
    /**
     * Sets the specified option to the specified value. The value can then be
     * retrieved with getOption(). This allows the instantiator to further
     * specify the behavior of individual instances of CorpusProcessor
     * subclasses.
     */
    public void setOption(String key, String value) {
	options.put(key, value);
    }
    
    /**
     * Retrieves the value associated with the specified key, or return null if
     * no such value has been set.
     */
    public String getOption(String key) {
	if (options.containsKey(key)) {
	    return options.get(key);
	}
	
	return null;
    }
}
