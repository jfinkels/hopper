package perseus.document;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;

public class ABO {

    private static Logger logger = Logger.getLogger(ABO.class);
    
    /**
     * Convenience method for when we don't care about getting documents
     * in a particular language.
     */
    public static List<Query> getDocuments(String abo) {
	List<Query> documents = new ArrayList<Query>();
	
	try {
	    MetadataDAO dao = new SQLMetadataDAO();
	    
	    documents = dao.getDocuments(Metadata.ABO_KEY, null, abo);
	} catch (MetadataAccessException se) {
	    logger.warn("Couldn't get documents for " + abo + ": ", se);
	}
	
	return documents;
    }
    
    /**
     * Returns a list of valid ABOs
     */
    public static List<Query> getABOs() {
	List<Query> documents = new ArrayList<Query>();
	
	MetadataDAO dao = new SQLMetadataDAO();
	try {
	    List<String> aboIDs = dao.getDistinctValueIDs(Metadata.ABO_KEY);
	    
	    for (String aboID : aboIDs) {
		documents.add(new Query(aboID));
	    }
	} catch (MetadataAccessException e) {
	    logger.error("Problem retrieving all ABO ids", e);
	}
	
	return documents;
    }
}
