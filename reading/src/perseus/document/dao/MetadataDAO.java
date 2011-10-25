/**
 * The goal of this class, as of any reasonable DAO, is to remove all the
 * ugly database-related code from the Metadata class. This class contains
 * methods for loading, saving and clearing the metadata table.
 */
package perseus.document.dao;

import java.util.List;

import perseus.document.Metadata;
import perseus.document.MetadataAccessException;
import perseus.document.MetadataStore;
import perseus.document.Query;

public interface MetadataDAO {
    public void clear() throws MetadataAccessException;
    public Metadata getMetadata(Query query) throws MetadataAccessException;

    public void save(Metadata metadata) throws MetadataAccessException;
    public void delete(Metadata metadata) throws MetadataAccessException;

    public void save(MetadataStore store) throws MetadataAccessException;

    public List<Query> getDocuments(String keyName, String value, String valueID) throws MetadataAccessException;
    public List<Query> getDocuments(String keyName, String value, String valueID, boolean ignoreSubdocs) throws MetadataAccessException;
    public List<Query> getDocuments(String keyName, List<String> values, boolean ignoreSubdocs) throws MetadataAccessException;
    public List<String> getDistinctValues(String keyName)
	throws MetadataAccessException;
    public List<String> getDistinctValueIDs(String keyName)
	throws MetadataAccessException;
}
