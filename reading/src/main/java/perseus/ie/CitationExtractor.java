package perseus.ie;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.transform.JDOMSource;
import org.xml.sax.InputSource;

import perseus.document.Chunk;
import perseus.document.CorpusProcessor;
import perseus.document.DOMOffsetAdder;
import perseus.document.InvalidQueryException;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.document.StyleTransformer;
import perseus.ie.dao.CitationDAO;
import perseus.ie.dao.HibernateCitationDAO;

/**
 * Subclass of CorpusProcessor that extracts all citations in a given text.
 * This class relies on an XSL stylesheet, `citations.xsl`, to do the actual
 * work of identifying the citations for each chunk.
 */

@SuppressWarnings("serial")
public class CitationExtractor extends RecentlyModifiedCorpusProcessor {
	private static final Logger logger = Logger.getLogger(CitationExtractor.class);

    /** Are we in a lexicon or a normal text? */
    private boolean inLexicon;

    /** Are we in a text-specific lexicon, like Autenrieth or Slater? */
    private boolean inSpecificLexicon;
    private CitationDAO citDAO = new HibernateCitationDAO();
    private Semaphore citSem;

    // Keep caches of queries we've looked up/normalized. We have to look up a
    // *lot* of queries, which means a lot of database hits, which can cause
    // the program to run incredibly slowly.
    private static Map<String,Query> normalizedQueryCache = Collections.synchronizedMap(new LinkedHashMap<String,Query>() {
	protected boolean removeEldestEntry(Map.Entry eldest) {
	    return (size() > 15000);
	}
    });
    private static Map<String,List<Query>> expandedQueryCache = Collections.synchronizedMap(new LinkedHashMap<String,List<Query>>() {
	protected boolean removeEldestEntry(Map.Entry eldest) {
	    return (size() > 15000);
	}
    });

    private int chunksProcessed = 0;
    private int citationsProcessed = 0;

    private int expandedCacheSeeks = 0;
    private int expandedCacheHits = 0;

    public CitationExtractor() {
    	this(new Semaphore(1));
    }
    
    public CitationExtractor(Semaphore citSem) {
	setOption(CorpusProcessor.SUBDOC_METHOD, CorpusProcessor.ONE_DOC);
		this.citSem = citSem;
    }
    
    public String getTaskName() {
		return "extract-citations";
	}

    public void startDocument(Chunk documentChunk) {
	Metadata metadata = documentChunk.getMetadata();

	inLexicon = false;
	inSpecificLexicon = false;

	if (metadata.has(Metadata.FORM_KEY)
		&& metadata.get(Metadata.FORM_KEY).equals("dictionary")) {

	    inLexicon = true;

	    List<String> textsCovered = metadata.getList(Metadata.LEXICON_KEY);
	    if (textsCovered != null && !textsCovered.isEmpty()) {
		inSpecificLexicon = true;
	    }
	}
	citationsProcessed = 0;
	chunksProcessed = 0;

	expandedCacheSeeks = 0;
	expandedCacheHits = 0;

	logger.info("Clearing existing citations for document...");
	try {
		citSem.acquire();
	} catch (InterruptedException e) {
		logger.trace("Thread interrupted");
	}
	((HibernateCitationDAO) citDAO).deleteByDocumentID(documentChunk.getDocumentID());
	citSem.release();
    }

    public void endDocument(Chunk documentChunk) {
	super.endDocument(documentChunk);

	logger.info("Finished document: " + documentChunk.getQuery());
	logger.info(String.format("Found %d citations in %d chunks",
                    citationsProcessed, chunksProcessed));

        logger.info(String.format("Expanded query cache: %d, with %d hits",
                    expandedCacheSeeks, expandedCacheHits));
    }

    public void processChunk(Chunk documentChunk) {
	Query currentQuery = documentChunk.getQuery();

	Map<String,String> styleParameters = new HashMap<String,String>();
	styleParameters.put("current_query", currentQuery.toString());

	boolean inCommentary = false;
	if (currentQuery.getMetadata().has(Metadata.COMMENTARY_KEY)) {
	    inCommentary = true;
	}

	String defaultLinkType = Citation.REF_DEFAULT;
	if (chunkInIndex(documentChunk)) {
	    defaultLinkType = Citation.REF_INDEX;
	} else if (inSpecificLexicon) {
	    defaultLinkType = Citation.REF_SPECIFIC_LEXICON;
	} else if (inLexicon) {
	    defaultLinkType = Citation.REF_LEXICON;
	} else if (inCommentary) {
		defaultLinkType = Citation.REF_COMMENTARY;
	}
	styleParameters.put("default_link_type", defaultLinkType);

        Document document = null;
        try {
            document = DOMOffsetAdder.domFromChunk(documentChunk);
        } catch (Exception e) {
            logger.error("Unable to add offsets to document", e);
            throw new IllegalArgumentException(e);
        }

        String citationXML = StyleTransformer.transform(
                new JDOMSource(document),
                "build/ie/citations.xsl",
                currentQuery.getDocumentID(),
                styleParameters);

	String sourceHeader = documentChunk.getHasCustomHead() ?
		documentChunk.getHead() :
		currentQuery.getDisplayCitation();

	List<Citation> citations = parseCitations(citationXML);
	if (!citations.isEmpty()) {
		logger.debug(String.format("%s: [%d]", currentQuery, citations.size()));
	}

        for (Citation citation : citations) {
	    citation.setSourceHeader(sourceHeader);

	    List<Citation> expandedCits = expandCitation(citation);
            for (Citation expandedCit : expandedCits) {
		recordCitation(expandedCit);
	    }
	}

	chunksProcessed++;
	citationsProcessed += citations.size();
    }

    private List<Citation> parseCitations(String xml) {
	List<Citation> output = new ArrayList<Citation>();

	try {
	    Document citationTree = new SAXBuilder().build(new InputSource(
			new StringReader(xml)));
            Iterator elements =
                citationTree.getDescendants(new ElementFilter("citation"));
            while (elements.hasNext()) {
                Element element = (Element) elements.next();

		Citation parsedCit = parseCitation(element);
		if (parsedCit != null) output.add(parsedCit);
            }
	} catch (IOException ioe) {
            logger.error("Bad document", ioe);
	} catch (JDOMException jde) {
            logger.error("Bad document", jde);
	}

	return output;
    }

    private Citation parseCitation(Element element) {
	Citation cit = new Citation();

        if (element.getChild("source") != null) {
            cit.setSource(new Query(element.getChildText("source")));
        }
        if (element.getChild("destination") != null) {
            cit.setDestination(new Query(element.getChildText("destination")));
        }
        if (element.getChild("linkType") != null) {
            cit.setLinkType(element.getChildText("linkType"));
        }

	// the destination document ID will be null if we found an
	// abbreviation we couldn't resolve
	if (cit.getDestination() == null ||
		cit.getDestination().getDocumentID() == null) {
	    logger.debug("bad destination: skipping " + cit);
	    return null;
	}
	return cit;
    }

    private void recordCitation(Citation citation) {
	logger.debug("citation is "+citation);
		try {
			citSem.acquire();
		} catch (InterruptedException e1) {
			logger.trace("Thread interrupted");
		}
		citDAO.beginTransaction();
	    citDAO.insert(citation);
	    citDAO.endTransaction();
	    citSem.release();
    }

    private List<Citation> expandCitation(Citation citation) {
	List<Query> destinationCits = doExpand(citation.getDestination().asABO());

	List<Citation> expandedCits = new ArrayList<Citation>();

	Query originalDestination = citation.getDestination().asABO();

        for (Query destination : destinationCits) {
	    assert !destination.isAbstract();

	    Citation expandedCit = new Citation(
		    citation.getSource(), originalDestination,
		    citation.getLinkType());
	    expandedCit.setResolvedDestination(destination);
	    expandedCit.setSourceHeader(citation.getSourceHeader());
	    expandedCits.add(expandedCit);
	}

	return expandedCits;
    }

    private List<Query> doExpand(Query unexpandedQuery) {
    	String cacheKey = unexpandedQuery.toString();

    	expandedCacheSeeks++;
    	List<Query> queryList = expandedQueryCache.get(cacheKey);
    	if (queryList == null) {
    		try {
    			List<Query> queries = unexpandedQuery.expandABOQuery();
    			List<Query> normalizedQueries = new ArrayList<Query>(queries.size());

    			for (Query query : queries) {
    				normalizedQueries.add(normalizeQuery(query));
    			}
    			expandedQueryCache.put(cacheKey, normalizedQueries);
    			return normalizedQueries;
    		} catch (Exception e) {
    			logger.trace("Problem expanding query " + unexpandedQuery, e);
    			return Collections.singletonList(unexpandedQuery);
    		}
    	} else {
    		logger.trace("Expanded-query cache hit: " + cacheKey);
    		expandedCacheHits++;
    		return queryList;
    	}
    }

    private Query normalizeQuery(Query query) {
	String cacheKey = query.toString();
	
	Query cacheQuery = normalizedQueryCache.get(cacheKey);
	if (cacheQuery == null) {
		try {
			Chunk chunk = query.getChunk();
			Query normalizedQuery = chunk.getQuery();
			cacheQuery = normalizedQueryCache.put(cacheKey, normalizedQuery);
			return normalizedQuery;
		} catch (InvalidQueryException e) {
			logger.debug("Problem normalizing query " + query, e);
		    return query;
		}
	} else {
		return cacheQuery;
	}
    }

    private boolean chunkInIndex(Chunk documentChunk) {
	// This is a horrendously clunky way of finding out whether we're
	// inside an index. It's helpful to know whether a given citation comes
	// from an index or not; unfortunately, such indices are usually not
	// part of a document's chunk schemes. Make a guess based on the
	// chunk's open-tags.
	String openTags = documentChunk.getOpenTags();
	return (openTags.indexOf("type=\"index\"") != -1 ||
		openTags.indexOf("n=\"index\"") != -1);
    }

    public static void main(String[] args) {
	CitationExtractor ce = new CitationExtractor();

	String[] effectiveArgs = args;
	Options options = new Options()
	.addOption("f", "force", false, 
	"force loading, even if file unchanged")
	.addOption(OptionBuilder.withLongOpt("threads")
			.withDescription("set number of threads to use")
			.hasArg()
			.withArgName("NUMBER")
			.create());

	CommandLineParser parser = new PosixParser();
	boolean hasForce = false;
	//use the minimum # of cpu's available to this machine, but allow user to change #
	int THREADS = Runtime.getRuntime().availableProcessors();
	
	try {
	    CommandLine cl = parser.parse(options, args);
	    
	    if (cl.hasOption("force") || cl.hasOption("f")) {
	    	hasForce = true;
		ce.setIgnoreModificationDate(true);
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
        }
	
	if (effectiveArgs.length > 0) {
	    for (String arg : effectiveArgs) {
		ce.processAnything(arg);
	    }
	} else {
		ExecutorService exec = Executors.newFixedThreadPool(THREADS);
		List<String> documentIDs = perseus.document.Document.getTexts();
		Collections.shuffle(documentIDs);
		Semaphore citSem = new Semaphore(1);
		for (String documentID : documentIDs) {
			Query documentQuery = new Query(documentID);
			CitationExtractor loader = new CitationExtractor(citSem);
			loader.setIgnoreModificationDate(hasForce);
			loader.setQuery(documentQuery);
			exec.execute(loader);
		}
		exec.shutdown();
	}
    }
}
