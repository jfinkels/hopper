package perseus.document.dao;

import java.util.List;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;

import perseus.util.DAO;

/**
 * An interface containing a variety of methods for accessing chunks.
 */

public interface ChunkDAO {
    public void save(Chunk chunk);
    public void delete(Chunk chunk);
    public void updateChunk(Chunk chunk);
    public void cleanup();
    
    /**
     * Deletes all chunks from the given document.
     */
    public void deleteByDocument(String documentID);
    
    public void beginTransaction();
    public void endTransaction();
	
    /**
     * Returns the chunk with the corresponding Hibernate id.
     */
    public Chunk getById(int id);

    /**
     * Returns the chunk found by looking up each element of `query`.
     *
     * @throws InvalidQueryException if no chunk was found for the query
     */
    public Chunk getByQuery(Query query) throws InvalidQueryException;
    
    /**
     * Returns the chunk with the specified "display query".
     */
    public Chunk getByDisplayQuery(String documentID, String subquery);

    /**
     * Returns the chunk with the specified ID attribute.
     */
    public Chunk getByChunkID(String documentID, String chunkID);
    
    /**
     * Returns the chunk in `documentID` found by successively looking up the
     * first member of each type of `scheme`.
     */
    public Chunk getFirstChunkForScheme(String documentID, String scheme);

    /**
     * Returns the chunk in `documentID` found by successively looking up the
     * first member of each type of `scheme`, going no further than `type`.
     */
    public Chunk getFirstChunkForScheme(String documentID, String scheme,
	    String type);
    
    /**
     * Returns all `chunk`'s child chunks of type `type`.
     */
    public List<Chunk> getContainedChunks(Chunk chunk, String type);
    public List<Chunk> getContainedChunks(Chunk chunk, List<String> types);
    public List<Chunk> getContainedChunks(Chunk chunk,
	    List<String> types, boolean includeOverlaps);
    
    /**
     * Returns all chunks belonging to the given document.
     */
    public List<Chunk> getAllChunks(String documentID);
    public List<Chunk> getAllChunks(String documentID, String type);
    public List<Chunk> getAllChunks(String documentID, List<String> types);
    
    /**
     * Returns the number of chunks present in the given document.
     */
    public int getChunkCount(String documentID);
    public int getChunkCount(String documentID, String type);
    public int getChunkCount(String documentID, List<String> types);
    
    /**
     * Returns the first chunk in `documentID` of type `type` and value `value`.
     */
    public Chunk getFirstChunk(String documentID, String type, String value);
    
    /**
     * Returns the first chunk contained within `chunk`.
     */
    public Chunk getFirstContainedChunk(Chunk chunk);

    /**
     * Returns the first chunk contained within `chunk` with type `type` and value
     * `value` (either of which may be set to null to indicate no requirement).
     */
    public Chunk getFirstContainedChunk(Chunk chunk, String type, String value);
    
    /**
     * Returns the chunk immediately preceding `chunk` (that is, the previous
     * chunk of the same type).
     */
    public Chunk getPreviousChunk(Chunk chunk);
    /**
     * Returns the chunk immediately following `chunk` (that is, the next chunk
     * of the same type).
     */
    public Chunk getNextChunk(Chunk chunk);
    
    /**
     * Returns the child of `parent` whose position in the document equals
     * `position`.
     */
    public Chunk getByPosition(Chunk parent, String type, int position);

    /**
     * Returns the child of `parent` whose absolute position in the document
     * equals `position`.
     */
    public Chunk getByAbsolutePosition(Chunk parent, int position);
    
    /**
     * Returns the chunk of type `type` that contains `child`.
     */
    public Chunk getContainingChunk(Chunk child, String type);
}
