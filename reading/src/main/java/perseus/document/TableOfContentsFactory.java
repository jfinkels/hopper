package perseus.document;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.language.LanguageCode;
import perseus.util.Config;

/**
 * Contains some methods used in creating TableOfContents instances for
 * our documents. Used by the ByteOffsetParser after we have finished
 * chunking a text.
 */

public class TableOfContentsFactory {
    private static final int MAX_ENTRY_COUNT = 3000;
    private static Logger logger =
	Logger.getLogger(TableOfContentsFactory.class);

    public static TableOfContents newTOC(
	    Query query, String scheme, List<Chunk> chunks) {

	BufferedWriter writer = null;
	try {
	    String xml = generateXML(query, chunks, scheme);
	    String cachePath = Config.getProperty(Config.CACHE_PATH);
	    File cacheDir = new File(cachePath);
	    if (!cacheDir.exists()) cacheDir.mkdirs();

	    File tocCache = new File(cachePath, getFilename(query, scheme));

	    writer = new BufferedWriter(new FileWriter(tocCache));
	    writer.write(xml);
	} catch (IOException ioe) {
	    logger.error("Problem writing TOC XML", ioe);
	} finally {
	    try {
		if (writer != null) { writer.close(); }
	    } catch (IOException ioe) {
		logger.error("Problem closing TOC file", ioe);
	    }
	}
	
	TableOfContents toc = null;
	
	List<String> types = ChunkSchemes.typesForScheme(scheme);
	if (types.get(types.size()-1).equals("entry") &&
		types.get(0).equals("alphabetic letter")) {
		
		String workingScheme = "";
		
		if (types.contains("root")) {
			workingScheme = "alphabetic letter:root:entry";
		} else {
	    	workingScheme = "alphabetic letter:entry group:entry";
	    }
	    
	    // Keep the scheme that people expect to see, but add entry
	    // groups to the TOC in place of actual entries.
	    toc = new ContextTableOfContents(query, scheme, workingScheme);
	} else {
	    toc = new DocumentTableOfContents(query, scheme);
	}
	for (Chunk c : chunks) {
	    toc.addChunk(c);
	}

	return toc;
   }
    
    private static String getFilename(Query query, String scheme) {
	return String.format("%s.%s.xml", 
		query.toString().replaceAll("/", "_"), scheme);
    }
    
    public static String getXML(TableOfContents toc) throws IOException {
	File cache = new File(
		Config.getProperty(Config.CACHE_PATH),
		getFilename(toc.getQuery(), toc.getScheme()));
	if (!cache.exists()) return null;
	
	InputStream is = new BufferedInputStream(new FileInputStream(cache));
	byte[] tocData = new byte[is.available()];
	is.read(tocData);
	is.close();
	
	return new String(tocData);
    }

    // Some yucky code to deal with subdocuments.
    private static List<Chunk> getSubtextChunks(Chunk initialChunk, ChunkDAO dao,
	    List<String> types) {
	
	Chunk topChunk = Chunk.getInitialChunk(
		new Query(initialChunk.getDocumentID()));
	List<Chunk> subtexts = new ArrayList<Chunk>(topChunk.getSubTexts());
	Collections.sort(subtexts);
	
	Set<Chunk> chunks = new HashSet<Chunk>();

	for (Chunk subtext : subtexts) {
	    if (subtext.contains(initialChunk) ||
		    subtext.equals(initialChunk)) {
		List<Chunk> containedChunks =
		    dao.getContainedChunks(subtext, types);

		// this will add the parent chunk, too...
		chunks.addAll(containedChunks);
	    } else {
		chunks.add(subtext);
	    }
	}

	return new ArrayList<Chunk>(chunks);
    }
    
    /**
     * Builds all possible tables of contents for the document represented by
     * `query`. Builds one TOC for each scheme it finds in the query's
     * metadata.
     *
     * @param query a query representing the document to build TOCs for
     * @return an array of TOCs
     */
    public static List<TableOfContents> build(Query query) {
	Chunk chunk = Chunk.getInitialChunk(query);
	if (chunk == null) {
	    logger.warn("No chunk for query: " + query);
	    return new ArrayList<TableOfContents>();
	}
	
	
	List<String> schemes = query.getMetadata().getChunkSchemes().getSchemes();
	
	List<TableOfContents> tocs = new ArrayList<TableOfContents>();
	
	ChunkDAO chunkDAO = new HibernateChunkDAO();
	int entryCount =
	    chunkDAO.getChunkCount(query.getDocumentID(), "entry");
	
	for (String scheme : schemes) {
	    List<String> types = ChunkSchemes.typesForScheme(scheme);
	    
	    // Don't bother building TOCs for "line" types.
	    if (types.get(types.size()-1).equals("line")) continue;
	    
	    // Ignore "id" schemes, too--we handle "id=" queries specially.
	    if (types.size() == 1 && types.get(0).equals("id")) continue;
	    
	    // For lexica, especially lexica as large as the LSJ, we do *not*
	    // want to store  a table of contents consisting only of entries, 
	    // because it would be huge. But we may want TOCs for texts with far
	    // fewer entries. Try to figure something out based on the work
	    // (this is decidedly not foolproof): if there are lots of entries,
	    // don't create a TOC for them.
	    if (types.get(0).equals("entry") && entryCount > MAX_ENTRY_COUNT) {
		logger.info("Skipping entries-only scheme ("
			+ entryCount + " entries)");
		continue;
	    }
	    
	    logger.info("Building with scheme " + scheme);
	    tocs.add(buildTOC(query, scheme));
	}
	
	return tocs;
    }
    
    public static TableOfContents buildTOC(Query query, String scheme) {
	Chunk chunk = Chunk.getInitialChunk(query);
	ChunkDAO chunkDAO = new HibernateChunkDAO();
	
	List<String> types = ChunkSchemes.typesForScheme(scheme);
	logger.info(String.format("%s: -> %s", query, types));
	
	// If we see something beginning with "alphabetic letter", we have
	// a context-based TableOfContents. Do some fiddling with the types
	// and the scheme...
	List<String> typesToFetch;
	if (types.get(0).equals("alphabetic letter") &&
		(types.get(1).equals("entry") || types.get(types.size()-1).equals("entry"))) {
	    typesToFetch = new ArrayList<String>();
	    typesToFetch.add("alphabetic letter");
	    if (types.get(1).equals("root")) {
	    typesToFetch.add("root");
	    }
	} else {
	    typesToFetch = types;
	}
	
	List<Chunk> chunks;
	if (chunk.getMetadata().has(Metadata.SUBDOC_QUERY_KEY)) {
	    chunks = getSubtextChunks(chunk, chunkDAO, typesToFetch);
	} else {
	    chunks = chunkDAO.getAllChunks(
		    query.getDocumentID(), typesToFetch);
	}
	logger.info("Got " + chunks.size() + " chunks");
	
	TableOfContents toc = newTOC(query, scheme, chunks);

	return toc;
    }
    
    /**
     * Returns an XML representation of a TOC created from the given chunks,
     * with the query URLs encoded. The intent is to create cached TOC XML
     * files.
     *
     * @param scheme 
     * @param root the chunk holding the other chunks
     */
    public static String generateXML(
	    Query rootQuery, List<Chunk> chunks, String scheme) {
	Element xmlTree = encodeURLs(assembleTree(rootQuery, chunks, scheme));
	
	return new XMLOutputter(Format.getPrettyFormat())
		.outputString(xmlTree);
    }
    
    private static Element encodeURLs(Element tree) {
	Iterator it = tree.getDescendants(new ElementFilter("chunk"));
	while (it.hasNext()) {
	    Element element = (Element) it.next();
	    if (element.getAttribute("ref") != null) {
		String unencoded = element.getAttributeValue("ref");
		try {
		    element.setAttribute("ref", URLEncoder.encode(unencoded, "utf-8"));
		} catch (UnsupportedEncodingException uee) {
		    logger.warn("Unable to encode query", uee);
		}
	    }
	}
	
	return tree;
    }
    
    /**
     * This is where the actual assembling of the TOC happens. Given the chunks
     * that we've already loaded, we group them by type; starting with the
     * smallest type, we attempt to match each child chunk to a chunk of the
     * next smallest type that contains it. When we've done this for all the
     * chunks, we'll have formed a hierarchy, which becomes our TOC.
     */
    private static Element assembleTree(Query rootQuery,
	    List<Chunk> chunkList, String scheme) {
	/*Find the query that should be at the very top of the hierarchy,
	  which will be the document ID */
	String rootRef = rootQuery.getDocumentID();
	Element rootElement = new Element("contents")
		.setAttribute("ref", rootRef)
		.setAttribute("lang", LanguageCode.ENGLISH);
	
	List<String> types = ChunkSchemes.typesForScheme(scheme);
	Map<String,SortedSet<Chunk>> chunksByType =
	    new HashMap<String,SortedSet<Chunk>>();
		
	// Group them by type (and sort them in the process)...
	for (String type : types) {
	    SortedSet<Chunk> chunks = new TreeSet<Chunk>();
	    chunksByType.put(type, chunks);
	    for (Chunk chunk : chunkList) {
		if (chunk.getType().equals(type)) {
		    chunks.add(chunk);
		}
	    }
	}
	
	/*Then, for each chunk below the top level, find that chunk's parent.
	  To do so, move through the list of *parents*, and for each parent
	  find all its children. If there are children left over when we've
	  looked through all the parents, then we have orphans, which means
	  something is probably wrong with the hierarchy.*/
	for (int typeIdx = types.size()-1; typeIdx > 0; typeIdx--) {
	    SortedSet<Chunk> chunks = chunksByType.get(types.get(typeIdx));
	    SortedSet<Chunk> parents = chunksByType.get(types.get(typeIdx-1));
	    for (Chunk parent : parents) {
		while (!chunks.isEmpty() && 
			parent.getEndOffset() >= chunks.first().getStartOffset()) {
		    Chunk child = chunks.first();
		    parent.addContainedChunk(child);
		    chunks.remove(child);
		}
	    }
	    
	    if (!chunks.isEmpty()) {
		logger.warn(chunks.size() + " " + types.get(typeIdx)
			+ " chunks lack parents");
	    }
	}
	
	// Now create a root chunk to act as the parent for all our top-level
	// chunks.
	Chunk rootChunk = Chunk.getInitialChunk(new Query(rootQuery.getDocumentID()));

	// And add the top-level chunks to it, giving us a single hierarchy.
	// (We only really need to do this to fool line-based chunks into
	// thinking they have a parent, so they give us nice headers.)
	rootChunk.addContainedChunks(chunksByType.get(types.get(0)));

	// Now actually output the hierarchy.
	for (Chunk chunk : chunksByType.get(types.get(0))) {
	    /*
	     * 1999.02.0022 (Shuckburgh) is a difficult text. It has values that
	     * are like text=A:book=1:letter=1. The hopper doesn't like it when you
	     * add the type and value so that it is: letter=text=A:book=1:letter=1.
	     * So we don't cascade down through the queries and reset them (why, in fact,
	     * do we do this in the first place?).  There are also changes in Chunk.java
	     * buildQuery() and refreshQuery() to handle this particular text
	     */

	    if (chunk.getDocumentID().equals("Perseus:text:1999.02.0022")) {
		chunk.resetQuery(false);
	    }
	    else {
		chunk.resetQuery(true);
	    }
	    rootElement.addContent(chunk.toXML());
	}
	
	return rootElement;
    }    
}
