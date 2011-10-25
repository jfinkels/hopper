/**
 * @deprecated The ByteOffsetParser class now does everything that this class
 * used to do--there's no longer any need to use it.
 */
package perseus.chunking;

import java.util.Iterator;

import org.apache.log4j.Logger;

import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;

public class ChunkLinker {
	private static Logger logger = Logger.getLogger(ChunkLinker.class);

	ChunkDAO dao = new HibernateChunkDAO();

	public ChunkLinker(String documentID) {

		Query documentQuery = new Query(documentID);
		Chunk initialChunk = Chunk.getInitialChunk(documentQuery);

		if (initialChunk.getMetadata().has(Metadata.SUBDOC_REF_KEY)) {
			Iterator chunkIterator = initialChunk.getSubTexts().iterator();
			while (chunkIterator.hasNext()) {
				processChunk((Chunk) chunkIterator.next());
			}
		}

		processChunk(initialChunk);
	}

	public void processChunk(Chunk chunk) {
		logger.info("linking chunks for " + chunk.getQuery());

		/*
	while (chunkSchemes.hasNext()) {
	    String scheme = (String) chunkSchemes.next();
	    scheme = scheme.replaceAll("[*]", "");
	    TableOfContents toc = TableOfContents.assemble(chunk, scheme);

	    TableOfContentsDAO dao = new HibernateTableOfContentsDAO();
	    dao.save(toc);
	    dao.cleanup();
	}
		 */
	}
}
