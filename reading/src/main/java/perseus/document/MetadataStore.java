package perseus.document;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Essentially a glorified Map that maps Queries to their associated Metadata
 * objects.  It provides type-safe methods to retrieve the metadata and the
 * ability to merge MetadataStores with other MetadataStores.  It serves as the
 * back-end for the MetadataCache class, to which end it provides a method to
 * create a synchronized store.
 */

public class MetadataStore implements Iterable<Map.Entry<Query,Metadata>> {
    
    private Map<Query,Metadata> store = new HashMap<Query,Metadata>();
    
    public MetadataStore() {}
    
    private MetadataStore(MetadataStore ms) {
	store = new TreeMap<Query,Metadata>(ms.store);
    }
    
    /** Special version for use with the metadata cache */
    public static MetadataStore synchronizedStore() {
	MetadataStore synchedStore = new MetadataStore();
	synchedStore.store =
	    Collections.synchronizedMap(new LinkedHashMap<Query,Metadata>(){
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Map.Entry eldest) {
		    return (size() > 1500);
		}
	    });
	return synchedStore;
    }
    
    /**
     * Adds the given query/metadata pair to the store.
     * 
     * @param query a document query
     * @param metadata a metadata object
     */
    public void put(Query query, Metadata metadata) {
	store.put(query, metadata);
    }
    
    /**
     * Removes the metadata in the store referenced by `query` and returns it.
     * If no such metadata exists in the store, returns null.
     *
     * @param query a document query
     * @return the metadata referenced by query, or null if there is none
     */
    public Object remove(Query query) {
	return store.remove(query);
    }
    
    /**
     * Returns the total number of Metadata objects in this store.
     */
    public int size() {
	return store.size();
    }
    
    /**
     * Returns true if this store is empty.
     */
    public boolean isEmpty() {
	return store.isEmpty();
    }
    
    /**
     * Clears this MetadataStore.
     */
    public void clear() {
	store.clear();
    }
    
    /**
     * Merges `newData` with any metadata for `query` already present in the
     * store; if there is none present, adds `newData`.
     */
    public void putOrMerge(Query query, Metadata newData) {
	if (containsKey(query)) {
	    Metadata existingData = store.get(query);
	    store.put(query, existingData.merge(newData));
	} else {
	    put(query, newData);
	}
    }
    
    /**
     * Adds all the entries from `otherStore` to this store, merging if this
     * store already contains matching entries.
     */
    public void putAll(MetadataStore otherStore) {
	for (Map.Entry<Query,Metadata> me : otherStore) {
	    Query query = me.getKey();
	    Metadata mergend = me.getValue();
	    
	    putOrMerge(query, mergend);
	}
    }
    
    /**
     * Returns true if this MetadataStore contains metadata for `query`.
     */
    public boolean containsKey(Query query) {
	return store.containsKey(query);
    }
    
    /**
     * Returns the metadata associated with `query`, or null if there is none.
     */
    public Metadata get(Query query) {
	if (containsKey(query)) {
	    return store.get(query);
	}
	return null;
    }
    
    /**
     * Returns an iterator over all the queries in this store.
     */
    public Iterator<Query> queryIterator() {
	return store.keySet().iterator();
    }
    
    /**
     * Returns an iterator over all the metadata objects in this store.
     */
    public Iterator<Metadata> metadataIterator() {
	return store.values().iterator();
    }
    
    /**
     * Returns an iterator over all the query/metadata pairs in this store.
     */
    public Iterator<Map.Entry<Query,Metadata>> iterator() {
	return store.entrySet().iterator();
    }
    
    /**
     * Returns a MetadataStore containing entries from this MetadataStore
     * merged with those in `other`.
     */
    public MetadataStore merge(MetadataStore other) {
	MetadataStore mergedStore = new MetadataStore(this);
	
	for (Map.Entry<Query,Metadata> me : other) {
	    Query query = me.getKey();
	    Metadata mergend = me.getValue();
	    
	    mergedStore.putOrMerge(query, mergend);
	}
	
	return mergedStore;
    }
    
    public String toString() {
	StringBuffer output = new StringBuffer();
	
	output.append("[\n");
	for (Query query : store.keySet()) {
	    Metadata metadata = store.get(query);
	    
	    output.append(query).append(":\n").append(metadata).append("\n");
	}
	output.append("]\n");
	
	return output.toString();
    }
    
    public String toXML() {
	StringBuffer output = new StringBuffer();
	
	output.append("<metadata>\n");
	for (Metadata metadata : store.values()) {
	    output.append(metadata.toXML());
	}
	output.append("</metadata>");
	
	return output.toString();
    }
}
