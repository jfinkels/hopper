/**
 *
 * A subclass of the TableOfContents class, meant specifically for working
 * with lexica (or anything with "entry" chunks). To avoid excessive large
 * TOCs and/or OutOfMemoryExceptions, it does not load the entire hierarchy;
 * instead, it loads everything but the smallest-level chunks (in this case,
 * "entry" chunks), which it loads on demand.
 *
 * Currently, we only need this particular functionality for entry-based
 * documents, but the class could be extended to support arbitary types
 * without too much trouble. You'd need to add a field to represent the TOC's
 * default chunk type (and substitute it for "entry" in the code), and also add
 * a corresponding property in the Hibernate schema.
 */

package perseus.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;

public class ContextTableOfContents extends TableOfContents {

	Chunk contextChunk = null;

	private Set<Chunk> chunks = new TreeSet<Chunk>();

	private HibernateChunkDAO dao = new HibernateChunkDAO();

	/** The scheme we're actually using. For lexica, this might be
	 * "alphabetic letter:entry group:entry".
	 */
	private String workingScheme;

	private Logger logger = Logger.getLogger(this.getClass());

	public ContextTableOfContents() {
	}

	public ContextTableOfContents(Query query) {
		this(query, query.getMetadata().getChunkSchemes().getDefaultScheme());
	}

	public ContextTableOfContents(Query query, String s) {
		super(query, s);
	}

	public ContextTableOfContents(Query q, String s, String ws) {
		this(q, s);
		workingScheme = ws;
	}

	private void loadTopChunks() {
		List<String> schemeTypes = ChunkSchemes.typesForScheme(getWorkingScheme());
		String topType = schemeTypes.get(0);

		Iterator<Chunk> it = getChunks(topType);
		while (it.hasNext()) {
			chunks.add(it.next());
		}
	}

	public String getXML(Chunk chunkWithFocus) {
		// try to load all the chunks we need, triggering the loading of
		// subchunks if this isn't the default type
		try {
			getByQuery(chunkWithFocus.getQuery());
		} catch (InvalidQueryException iqe) {
			logger.warn("Bad query for context TOC", iqe);
		}

		return toXML();
	}

	public String toXML() {
		return TableOfContentsFactory.generateXML(
				getQuery(),
				new ArrayList<Chunk>(chunks), getWorkingScheme());	
	}

	public Chunk findChunkContaining(Chunk targetChunk) {
		List<String> types = ChunkSchemes.typesForScheme(getWorkingScheme());
		String targetType = targetChunk.getType();
		int targetIdx = types.indexOf(targetType);

		if (chunks.isEmpty()) loadTopChunks();

		Chunk parent = null;
		for (int i = 0; i < targetIdx; i++) {
			parent = dao.getContainingChunk(targetChunk, types.get(i));
			chunks.add(parent);

			chunks.addAll(dao.getContainedChunks(parent, types.get(i+1)));
		}

		return parent;
	}

	public Chunk getFirstChunk() {
		return getFirstChunk(ChunkSchemes.defaultTypeForScheme(getScheme()));
	}

	/**
	 * Returns the first chunk in this TableOfContents of the given type.
	 *
	 * @param type the type to search for, like "section"
	 * @return the first matching chunk, or null if none could be found
	 */
	public Chunk getFirstChunk(String type) {
		return getFirstChunk(type, null);
	}

	/**
	 * Returns the first chunk in this TableOfContents of the given type and
	 * value.
	 *
	 * @param type the type to search for, like "section"
	 * @param type the value it should be, like "1" or "intro"
	 * @return the first matching chunk, or null if none could be found
	 */
	public Chunk getFirstChunk(String type, String value) {
		Chunk firstChunk = dao.getFirstChunk(
				getQuery().getDocumentID(), type, value);
		findChunkContaining(firstChunk);

		return firstChunk;
	}

	public Chunk getByQuery(Query targetQuery) throws InvalidQueryException {
		Chunk chunk = targetQuery.getChunk();
		findChunkContaining(chunk);

		List<String> types = ChunkSchemes.typesForScheme(getWorkingScheme());
		if (types.indexOf(chunk.getType()) == -1) {
			throw new InvalidQueryException(targetQuery);
		}

		String targetType = types.get(types.size()-1);

		// If we've been asked to fetch something (like an "alphabetic letter"
		// or "entry group") that isn't last in the scheme, keep finding
		// child chunks until we hit the last type ("entry").
		ChunkDAO dao = new HibernateChunkDAO();
		while (!chunk.getType().equalsIgnoreCase(targetType) && chunk != null) {
			String nextType = types.get(types.indexOf(chunk.getType())+1);
			chunk = dao.getFirstContainedChunk(chunk, nextType, null);
			logger.debug("got CHUNK: " + chunk);
		}

		if (chunk != null) {
			findChunkContaining(chunk);
		}

		return chunk;
	}

	public Chunk getByChunk(Chunk targetChunk) {
		findChunkContaining(targetChunk);
		return targetChunk;
	}

	public Chunk getPreviousChunk(Chunk chunk) {
		Chunk previousChunk = dao.getPreviousChunk(chunk);
		if (previousChunk == null) return null;

		previousChunk.setQuery(new Query(chunk.getDocumentID()).
				appendSubquery(previousChunk.getType(), previousChunk.getValue()));
		findChunkContaining(previousChunk);
		return previousChunk;
	}

	public Chunk getNextChunk(Chunk chunk) {
		Chunk nextChunk = dao.getNextChunk(chunk);
		if (nextChunk == null) return null;

		nextChunk.setQuery(new Query(chunk.getDocumentID()).
				appendSubquery(nextChunk.getType(), nextChunk.getValue()));
		findChunkContaining(nextChunk);
		return nextChunk;
	}

	public Chunk getByID(String chunkID) {
		Chunk chunk = dao.getByChunkID(getQuery().getDocumentID(), chunkID);
		findChunkContaining(chunk);

		return chunk;
	}

	public String getWorkingScheme() {
		return workingScheme;
	}

	public void setWorkingScheme(String workingScheme) {
		this.workingScheme = workingScheme;
	}

	@Override
	public Iterator<Chunk> getChunks(List<String> types) {
		return new HibernateChunkDAO().getChunkIterator(
				getQuery().getDocumentID(), types);
	}
}
