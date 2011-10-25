package perseus.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;

/**
 * CTS Helper Class 
 * Utilities for handling a CTS urn in input 
 * (Note this is not the actual CTS API implementation, which is in  
 *  the perseus.cts package, but a class dealing with CTS when they're supplied
 *  where we're expecting a legacy document id or abo) 
 * @author balmas
 *
 */
public class CTS {

    private static Logger logger = Logger.getLogger(CTS.class);
    
    /**
     * List all documents with the supplied CTS urn
     */
    public static List<Query> getDocuments(String a_urn) {
	List<Query> documents = new ArrayList<Query>();
	
	try {
	    MetadataDAO dao = new SQLMetadataDAO();
	    
	    documents = dao.getDocuments(Metadata.CTS_KEY, a_urn, null);
	} catch (MetadataAccessException se) {
	    logger.warn("Couldn't get documents for " + a_urn + ": ", se);
	}
	
	return documents;
    }
    
    /**
     * Convert a CTS urn to a document identifier
     * (currently only a doc id, later to include abos)
     * @param a_urn
     * @return String traditional document identifier
     */
    public static String toDocument(String a_urn)
    {
    	String converted = a_urn;
    	String[] parts = a_urn.trim().split(":");

		if (parts.length >= 4)
		{
			String[] editionParts = {parts[0], parts[1], parts[2], parts[3]};
			String editionUrn = StringUtils.join(editionParts,":");				    
	    	List<Query> documents = CTS.getDocuments(editionUrn);	    	
	    	Iterator<Query> iter = documents.iterator();
	    	while (iter.hasNext())
	    	{
	    		Query q = iter.next();
	    		
	    		String docId = q.getDocumentID();
	    		converted = docId;
	    		// TODO 
	    		// if the document has an Abo, try to get to the right edition
	    		// given the language
	    		// List<String> abos = new ArrayList<String>();
	    		// String foundAbo = q.getMetadata().getAbo();
	    		// if (foundAbo != null)
	    		// {
	    		//	abos.add(foundAbo);
	    		//}
	    		//else 
	    		//{
	    		//	abos.add(docId);
	    		//}
	    	}			
			// just take the first one found for now
			//if (abos.size() > 0)
			//{				
			//	abo = abos.get(0);
			//	if (parts.length > 4)
			//	{
			//		String cite = parts[5].replace(".", ":");
			//		// add the document subquery, replacing '.' with ':'
			//		abo = abo + cite;
			//	}										
			//}
			//else
			//{
			//	logger.warn("No Abo for CTS URN: " + a_urn);
			//}
		}
		else 
		{
			logger.warn("Invalid CTS URN: " + a_urn);
		}
    	
    	return converted;
    }
    
    /**
     * Returns a list of valid CTS URNs
     * TODO this probably doesn't work yet
     */
    public static List<Query> getURNs() {
	List<Query> documents = new ArrayList<Query>();
	
	MetadataDAO dao = new SQLMetadataDAO();
	try {
	    List<String> ctsURNs = dao.getDistinctValues(Metadata.CTS_KEY);
	    
	    for (String urn : ctsURNs) {
	    	// TODO need to add logic to Query to support cts urn scheme
	    	documents.add(new Query(urn));
	    }
	} catch (MetadataAccessException e) {
	    logger.error("Problem retrieving all CTS urns", e);
	}
	
	return documents;
    }
}
