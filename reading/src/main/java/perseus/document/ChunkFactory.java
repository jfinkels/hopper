package perseus.document;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import perseus.document.dao.HibernateTableOfContentsDAO;

/**
 This class uses a Hibernate ScrollableResults to rapidly cycle 
 through chunks in a document. It is designed to
 allow us to step through massive documents like LSJ without
 running out of memory.
 */

public class ChunkFactory implements Iterable<Chunk> {
    
    String scheme;
    private Query documentQuery;
    
    private ChunkFactory() {}

    private static Logger logger = Logger.getLogger(ChunkFactory.class);
    
    public static ChunkFactory forDocument(Query query, String scheme) {
	ChunkFactory factory = new ChunkFactory();
	factory.scheme = scheme;
	factory.documentQuery = query;
	
	return factory;
    }
    
    /**
     * Returns an iterator that spans the "List" of all chunks following and
     * preceding startingChunk with the given element type. This is useful if
     * you want to iterate through all chunks of type "entry" in a given
     * lexicon, for example. (The underlying structure isn't *really* a List,
     * but it functions as one to the user.
     *
     * The following code fragment returns a ListIterator over all entries in
     * the LSJ (this works because getTextlessChunk() actually returns the first
     * subchunk of the lexicon):
     *
     * <code>
     * ChunkFactory f = new ChunkFactory();
     * Chunk c = new Query("Perseus:text:1999.04.0057").getTextlessChunk();
     * Iterator it = f.iterator(c);
     * </code>
     *
     * @param startingChunk a Chunk that represents the first chunk in the list
     * of chunks to be retrieved (e.g., the first entry in a lexicon, or the
     * first section of a speech)
     * @return a ListIterator that can move through all such chunks
     */    
    public Iterator<Chunk> iterator() {
	HibernateTableOfContentsDAO tocDAO = new HibernateTableOfContentsDAO();
	String possibleType = documentQuery.getMetadata().getDefaultChunk();
	String tocType = possibleType;
	if (possibleType.equals("entry")) {
	    // a TOC may not exist for "entry" types if there are lots of
	    // entries, and if one does, there will be no chunks to match from,
	    // so just grab anything
	    tocType = null;
	}
	
	TableOfContents toc = tocDAO.getTOCForQuery(documentQuery, tocType);
	if (toc != null) {
	    return toc.getChunks(possibleType);
	}

	// if that didn't work, try again with the default chunk for the
	// default scheme, which can sometimes be different from the
	// "default chunk" as returned by the metadata method
	tocType = ChunkSchemes.defaultTypeForScheme(
	    documentQuery.getMetadata().getChunkSchemes().getDefaultScheme());
	toc = tocDAO.getTOCForQuery(documentQuery, tocType);
	if (toc != null) {
	    return toc.getChunks(tocType);
	}

	logger.error("No TOC found for " + documentQuery +
			", type " + tocType);
	return new ArrayList<Chunk>().iterator();
    }
    
}
