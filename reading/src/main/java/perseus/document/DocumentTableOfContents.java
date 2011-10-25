package perseus.document;

import java.util.List;

import org.apache.log4j.Logger;

import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateTableOfContentsDAO;
import perseus.document.dao.TableOfContentsDAO;

/**
 * A {@link TableOfContents} implementation that once had distinct
 * functionality from its parent class but now exists only to differentiate
 * document-based TOCs from their context- and view-based counterparts.
 */

public class DocumentTableOfContents extends TableOfContents {
    
    public DocumentTableOfContents() {}
    
    public DocumentTableOfContents(Query query) {
	super(query);
    }

    public DocumentTableOfContents(Query query, String s) {
	super(query, s);
    }
}
