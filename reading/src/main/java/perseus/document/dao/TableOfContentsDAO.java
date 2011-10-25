package perseus.document.dao;

import java.util.List;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.TableOfContents;
import perseus.util.DAO;

/**
 * Methods for retrieving and saving TableOfContents objects and Chunks
 * within the context of a specific TOC.
 */
public interface TableOfContentsDAO extends DAO<TableOfContents> {
    public void save(TableOfContents toc);
    public void delete(TableOfContents toc);
    public void cleanup();
    public TableOfContents getById(int id);

    /**
     * Returns all tables of contents for the given document.
     */
    public List<TableOfContents> getByDocument(String documentID);

    /**
     * Returns the TOC for the document (or perhaps subdocument) represented by
     * the given chunk.
     */
    public TableOfContents get(Chunk chunk);
    public TableOfContents get(Chunk chunk, String scheme);

    /**
     * Returns a TOC guaranteed to contain `chunk`.
     */
    public TableOfContents getTOCWithChunk(Chunk chunk);
    public TableOfContents getTOCWithChunk(Chunk chunk, String scheme);

    /**
     * Returns the TOC for the document (or perhaps subdocument) represented by
     * the given query.
     */
    public TableOfContents getTOCForQuery(Query initialQuery);

    public void deleteByDocument(String documentID);

    /**
     * Returns the chunk in `toc` whose subquery matches the one `targetQuery`,
     * if such a chunk can be found.
     */
    public Chunk getChunkByQuery(TableOfContents toc, Query targetQuery)
    	throws InvalidQueryException;
}
