package perseus.document;

import static perseus.document.Metadata.CITATION_SCHEME_KEY;
import static perseus.document.Metadata.SUBDOC_REF_KEY;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import perseus.document.dao.SQLMetadataDAO;

public class MetadataCache {
    
    private MetadataCache() {}
    
    private static MetadataStore cache = MetadataStore.synchronizedStore();
    
    static Logger cacheLogger = Logger.getLogger(MetadataCache.class);
    
    public static Metadata get(Query targetQuery) {
	Query rootQuery = new Query(targetQuery.getDocumentID());
	
	if (!cache.containsKey(rootQuery)) {
	    loadMetadata(targetQuery);
	}
	
	Metadata result;
	if (cache.containsKey(targetQuery)) {
	    result = cache.get(targetQuery);
	} else {
	    result = findSubdocData(rootQuery, targetQuery);
	    
	    // if no subdoc query matches, just return the document data.
	    if (result == null) {
		result = cache.get(rootQuery);
	    }
	}
	if (result == null) {
	    // oh well, we couldn't find anything; return a blank set of
	    // metadata
	    return new Metadata();
	}
	
	return result;
    }
    
    private static Metadata findSubdocData(Query parent, Query target) {
	if (!cache.containsKey(parent)) {
	    cacheLogger.warn("Metadata cache parent-doc miss: " + parent);
	    return null;
	}
	
	List<String> subdocs = new ArrayList<String>(
		cache.get(parent).getList(SUBDOC_REF_KEY));

	if (subdocs == null) return null;
	
	while (target != null && !target.equals(parent)) {
	    for (String possibleSubdoc : subdocs) {
		if (possibleSubdoc.equals(target.getQuery())) {
		    return cache.get(new Query(
			    parent.getDocumentID(), possibleSubdoc));
		}
	    }
	    
	    target = target.getContainingQuery();
	}
	
	return null;
    }
    
    private static void loadMetadata(Query query) {
	try {
	    MetadataStore documentStore = new SQLMetadataDAO()
	    	.getDocumentMetadata(query.getDocumentID());
	    
	    // Now fix up subdocuments, if there are any.
	    Metadata parentMetadata =
		documentStore.get(new Query(query.getDocumentID()));
	    
	    for (Iterator<Query> it = documentStore.queryIterator(); it.hasNext(); ) {
		Query childQuery = it.next();
		if (childQuery.isJustDocumentID()) continue;
		
		Metadata childMetadata = documentStore.get(childQuery);
		
		// include properties from parent, like language, etc., that
		// we don't have ...but do NOT merge citations, which can vary
		// between texts/subtexts, if the subdocument already has some
		// defined
		List<String> childSchemes =
		    childMetadata.getList(CITATION_SCHEME_KEY);

		childMetadata = childMetadata.merge(parentMetadata);
				
		// A slight kludge to re-add child citation schemes
		if (!childSchemes.isEmpty()) {
		    childMetadata.remove(CITATION_SCHEME_KEY);
		    childMetadata.addField(CITATION_SCHEME_KEY, childSchemes);

		    // ...and in some cases, the *parent* may not have any
		    // citation schemes defined, as often happens with
		    // texts with subdocuments. If it doesn't have any, find
		    // its first child and add its schemes to the parent.
		    if (!parentMetadata.has(CITATION_SCHEME_KEY)) {
			parentMetadata.addField(CITATION_SCHEME_KEY, childSchemes);
		    }
		}

		// Mark the document as a parent of the subdocument
		String fieldToAdd = Metadata.SUBDOC_REF_KEY;
		parentMetadata.addField(fieldToAdd, childQuery.getQuery());
		
		setSubDocumentTitles(childMetadata);
		documentStore.put(childQuery, childMetadata);
	    }
	    
	    cache.putAll(documentStore);
	} catch (MetadataAccessException me) {
	    cacheLogger.warn("Problem getting metadata from cache: " + me);
	}
    }
    
    /** Remove all metadata objects. Metadata will subsequently be
     reloaded from the database.
     */
    public static void clear() {
	cache.clear();
    }
    
    /** Subdocuments currently do not have their own titles in the 
     metadata table. They do have references to ABOs, which tend to 
     have titles. Grab those where we can and overwrite any title
     carried over from the main document.
     */
    private static void setSubDocumentTitles(Metadata child) {
	// Register this subdocument as part of the document
	String subdocABO = child.get(Metadata.ABO_KEY);
	
	if (subdocABO != null) {
	    Metadata aboMetadata = MetadataCache.get(new Query(subdocABO));
	    
	    if (aboMetadata != null && aboMetadata.has(Metadata.TITLE_KEY)) {
		// clear the title key from the parent
		child.remove(Metadata.TITLE_KEY);
		
		for (String title : aboMetadata.getList(Metadata.TITLE_KEY)) {
		    child.addField(Metadata.TITLE_KEY, title);
		}
	    }
	}
    }
}
