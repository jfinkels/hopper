package perseus.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateTableOfContentsDAO;
import perseus.document.dao.TableOfContentsDAO;
import perseus.document.dao.HibernateChunkDAO.ChunkRow;

/**
 * A hierarchy of chunks for a document and chunk scheme that effectively
 * represents a "table of contents" for the document in terms of the given
 * chunk scheme. Provides methods to access its contained chunks by type, to
 * access the root chunk (which, incidentally, does not usually correspond to
 * an actual chunk in the document; it only serves to group the highest-level
 * chunks), to return the next or previous chunk for a specified chunk, and
 * to output itself as text (see the <kbd>toHierarchy()</kbd> method) or as
 * XML.
 *
 * This class handles normal tables of contents and TOCs for texts with
 * subdocuments; a subclass, the ContextTableOfContents, handles entry-based
 * works.
 *
 * Note that the functionality of the old HierarchyBuilder class has been moved
 * here. Among other things,
 * the Chunk and TableOfContents classes were rewritten to use Hibernate for
 * storage; the logic code was moved into DAO classes, which can be extended as
 * necessary. Due to various quirks and issues with the Chunk and Query classes
 * (which are entrenched so deeply in the code that rewriting them would
 * require a prohibitive amount of time and the alteration of a great many
 * classes that use them), however, the new code still has some issues,
 * especially with some texts that contain subdocuments.
 *
 * @see ContextTableOfContents
 * @see Chunk
 * @see Query
 */

public abstract class TableOfContents {

	/**
	 * Represents a single entry in a TableOfContents. Consists of a reference
	 * to the parent TOC, a chunk representing the entry, and a subquery
	 * ("book=1:chapter=14") representing the chunk's location.
	 */
	public static class ChunkNode {
		private TableOfContents parent;
		private ChunkRow chunk;
		private String subquery;

		public ChunkNode() {}
		public ChunkNode(Chunk c, TableOfContents toc) {
			setChunk(HibernateChunkDAO.chunkToRow(c));
			setParent(toc);

			// Handle queries in Cicero letters, like
			// <div2 type="letter" n="coll=A:book=1:letter=1">, which are
			// supposed to link against an (imaginary?) authority list
			if (c.getValue().indexOf(":") != -1) {
				setSubquery(c.getValue());
			} else {
				setSubquery(c.getQuery().getQuery());
			}
		}

		public ChunkRow getChunk() {
			return chunk;
		}
		public void setChunk(ChunkRow chunk) {
			this.chunk = chunk;
		}
		public TableOfContents getParent() {
			return parent;
		}
		public void setParent(TableOfContents parent) {
			this.parent = parent;
		}
		public String getSubquery() {
			return subquery;
		}
		public void setSubquery(String subquery) {
			this.subquery = subquery;
		}
	}

	private Logger logger = Logger.getLogger(this.getClass());

	/** The id for this TOC as it it store in the database; used by Hibernate.
	 * */
	private Integer id;

	/** The document this TOC represents. */
	private Query query;

	/** The chunk scheme used--something like "book:chapter:section". */
	private String scheme;

	private String xml;

	/** Maps between chunks and their queries for this TOC. Don't expect this
	 *  to be filled at any given time--it's mostly here for retrieving chunks
	 *  from the database. */
	private Set<ChunkNode> chunks = new HashSet<ChunkNode>();

	public static TableOfContents forChunk(Chunk chunk) {
		return forChunk(chunk, null);
	}

	/**
	 * Returns a TableOfContents for the given Chunk with the document's
	 * default scheme.
	 *
	 * @param chunk the chunk 
	 * @return the TableOfContents for the given chunk
	 * @throws InvalidQueryException if the Query representing the Chunk
	 * can't be resolved using getInitialChunk()
	 */
	public static TableOfContents forChunk(Chunk chunk, String scheme) {
		HibernateTableOfContentsDAO tocDAO = new HibernateTableOfContentsDAO();

		// Special case for context TOCs, which don't store every chunk in the
		// database
		if (chunk.getType().equals("entry")) {
			List<TableOfContents> matches = tocDAO.getByDocument(chunk.getDocumentID());
			if (matches.isEmpty()) return null;
			for (int i = 0; i < matches.size(); i++) {
				if (matches.get(i).getScheme().startsWith("alphabetic letter:entry")) {
					return matches.get(i);
				}
			}
			return matches.get(0);
		}
		return tocDAO.getTOCWithChunk(chunk, scheme);
	}

	/**
	 * Creates a new, empty TableOfContents. This is for Hibernate.
	 */
	public TableOfContents () {
	}

	/**
	 * Creates a new TableOfContents using the given Chunk and the default
	 * scheme for the given document. Unless you actually plan on creating
	 * a new TableOfContents that doesn't exist in the database and then
	 * saving it, you should retrieve it using <kbd>forChunk()</kbd>
	 * instead of calling this method.
	 *
	 * @param chunk the chunk to use as the current chunk
	 * @return a new TableOfContents
	 */
	public TableOfContents(Query query) {
		this(query, query.getMetadata().getChunkSchemes().getDefaultScheme());
	}

	/**
	 * Creates a new TableOfContents using the given Chunk and scheme. Unless
	 * you actually plan on creating a new TableOfContents that doesn't exist
	 * in the database and then saving it, you should retrieve it using
	 * <kbd>forChunk()</kbd> instead.
	 * 
	 * @param chunk the chunk to use as the current chunk
	 * @param scheme the scheme to use
	 * @return a new TableOfContents with the given chunk and scheme
	 */
	public TableOfContents(Query query, String s) {
		setScheme(s);
		setQuery(query);
	}

	public Integer getId() { return id; }
	public void setId(Integer i) { id = i; }

	public Query getQuery() { return query; }
	public void setQuery(Query q) { query = q; }

	public String getScheme() { return scheme; }
	public void setScheme(String s) { scheme = s; }

	/**
	 * Returns an Iterator spanning all chunks in this TOC.
	 *
	 * @return an iterator over every chunk, regardless of type
	 */
	public Iterator<Chunk> getAllChunks() {
		return getChunks(new ArrayList<String>());
	}

	/**
	 * Returns an Iterator spanning all chunks in this TOC of type `type`.
	 *
	 * @param type the target chunk type
	 * @return an iterator over every chunk of type `type`
	 */
	public Iterator<Chunk> getChunks(String type) {
		List<String> types = new ArrayList<String>();
		types.add(type);

		return getChunks(types);
	}

	/**
	 * Returns an Iterator spanning all chunks of a type included in `types`.
	 *
	 * @param types a list of target chunk types
	 * @return an iterator over every chunk with a type contained in `types`
	 */
	public Iterator<Chunk> getChunks(List<String> types) {
		return new HibernateTableOfContentsDAO().getAllChunks(this, types);
	}

	/**
	 * Returns an XML representation of this TOC.
	 */
	public String getXML() {
		if (xml == null) {
			try {
				xml = TableOfContentsFactory.getXML(this);
			} catch (IOException e) {
				// BLAH
				e.printStackTrace();
			}
		}

		return xml;
	}

	// Convenience alias
	public String toXML() { return getXML(); }

	/**
	 * Returns an XML representation of this TOC, making sure to include
	 * `chunkWithFocus`. For all subclasses except ContextTableOfContents,
	 * this is identical to the parameterless method.
	 *
	 * @param chunkWithFocus the chunk currently selected/focused on
	 * @return an XML representation of this TOC, including `chunkWithFocus`
	 */
	public String getXML(Chunk chunkWithFocus) {
		return getXML();
	}

	/**
	 * Returns the chunk from this TOC that matches the given query.
	 *
	 * @param targetQuery the query to search for
	 * @return a matching Chunk
	 * @throws InvalidQueryException 
	 */
	public Chunk getByQuery(Query targetQuery)
	throws InvalidQueryException {
		if (targetQuery.isLineBased() ||
				targetQuery.getLastElementType().equals("commline")) {
			// we can't use the fast method for line-based queries, because
			// they might not show up at all.
			return new HibernateChunkDAO().getByQuery(targetQuery);
		}

		TableOfContentsDAO tocDAO = new HibernateTableOfContentsDAO();

		try {
			return tocDAO.getChunkByQuery(this, targetQuery);
		} catch (InvalidQueryException iqe) {
			// Maybe this query uses a scheme meant for a different TOC within
			// this document? This happens with some Cicero texts
			// (speech:section vs. speech:chapter:section).
			List<TableOfContents> otherTOCs =
				tocDAO.getByDocument(getQuery().getDocumentID());
			otherTOCs.remove(this);

			for (TableOfContents other : otherTOCs) {
				try {
					return tocDAO.getChunkByQuery(other, targetQuery);
				} catch (InvalidQueryException iqe2) {
					// try again
				}
			}
		}

		// really give up
		throw new InvalidQueryException(targetQuery);
	}

	/**
	 * Returns the chunk from this TOC matching `targetChunk`, or throws an
	 * {@link InvalidQueryException} if the chunk cannot be found.
	 *
	 * @param targetChunk the chunk to search for
	 * @return a matching Chunk
	 * @throws InvalidQueryException 
	 */
	public Chunk getByChunk(Chunk targetChunk)
	throws InvalidQueryException {
		try {
			return getByQuery(targetChunk.getQuery());
		} catch (InvalidQueryException e) {
			logger.warn("Bad query in chunk: " + targetChunk, e);
			return null;
		}
	}

	public Chunk getByID(String chunkID) {
		return new HibernateChunkDAO()
		.getByChunkID(getQuery().getDocumentID(), chunkID);
	}

	/**
	 * Returns the first chunk in this TableOfContents, as given by the
	 * default type in the TOC's scheme
	 * 
	 * @return the first default chunk
	 */
	public Chunk getFirstChunk() {
		String defaultType = ChunkSchemes.defaultTypeForScheme(getScheme());
		return getFirstChunk(defaultType);	
	}

	/**
	 * Returns the first chunk in this TableOfContents of the given type.
	 *
	 * @param type the type to search for, like "section"
	 * @return the first matching chunk, or null if none could be found
	 */
	public Chunk getFirstChunk(String type) {
		return new HibernateTableOfContentsDAO()
		.getFirstChunkOfType(this, type);
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
		return new HibernateTableOfContentsDAO()
		.getFirstChunkOfType(this, type, null);
	}

	/**
	 * Returns the most immediate chunk containing the target chunk. This is
	 * most useful for matching up Chunks that aren't actually in this
	 * TableOfContents (which is why we can't simply look at the chunk's
	 * parent) with ones that are.
	 *
	 * @param targetChunk the Chunk to search for
	 * @return the chunk's parent, or null if we can't find the chunk
	 */
	public Chunk findChunkContaining(Chunk targetChunk) {
		List<String> types = ChunkSchemes.typesForScheme(getScheme());
		String targetType = null;
		int targetIndex = types.indexOf(targetChunk.getType());
		if (targetIndex <= 0) {
			targetType = types.get(0);
		} else {
			targetType = types.get(targetIndex-1);
		}

		return new HibernateChunkDAO().getContainingChunk(
				targetChunk, targetType);
	}

	public String toString() {
		return query + " " + scheme;
	}

	public boolean equals(Object o) {
		if (!(o instanceof TableOfContents)) return false;

		TableOfContents toc = (TableOfContents) o;

		return (getClass().equals(toc.getClass()) &&
				getQuery().equals(toc.getQuery()) &&
				getScheme().equals(toc.getScheme()));
	}

	/**
	 * Returns all {@link ChunkNode}s that this TOC comprises. This is for
	 * Hibernate, which otherwise loads the ChunkNodes extra-lazily.
	 */
	public Set<ChunkNode> getChunks() {
		return chunks;
	}

	/**
	 * Returns all {@link ChunkNode}s that this TOC comprises. This is for
	 * Hibernate, which otherwise loads the ChunkNodes extra-lazily.
	 */
	public void setChunks(Set<ChunkNode> chunks) {
		this.chunks = chunks;
	}

	/**
	 * Adds a Chunk to this TOC, creating a ChunkNode for it.
	 */
	public void addChunk(Chunk c) {
		chunks.add(new ChunkNode(c, this));
	}

	/**
	 * Returns the chunk immediately preceding `chunk` in this TOC.
	 */
	public Chunk getPreviousChunk(Chunk chunk) {
		return new HibernateTableOfContentsDAO().getPreviousChunk(this, chunk);
	}

	/**
	 * Returns the chunk immediately following `chunk` in this TOC.
	 */
	public Chunk getNextChunk(Chunk chunk) {
		return new HibernateTableOfContentsDAO().getNextChunk(this, chunk);
	}    
}
