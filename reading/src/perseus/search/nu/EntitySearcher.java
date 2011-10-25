/**
 * Searcher adapter for named entities.
 */

package perseus.search.nu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import perseus.ie.entity.Date;
import perseus.ie.entity.Entity;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.entity.Person;
import perseus.ie.entity.Place;
import perseus.util.Range;

public class EntitySearcher implements Searcher {

    /** Which classes of entities should we look for? Default to some sensible
     * ones, but don't include Lemma, since it has its own searcher. */
    Set<Class<? extends Entity>> classes =
			new HashSet<Class<? extends Entity>>() {{
			    add(Person.class);
			    add(Place.class);
			    add(Date.class);
			}};
    
    private Map<String,SearchResults> resultCache =
	new LinkedHashMap<String,SearchResults>() {
	    
	    protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > 100;
	    }
	};
    

    public SearchResults search(String term, Range<Integer> range) {
	HibernateEntityManager manager = new HibernateEntityManager();
	if (range.hasStart()) {
	    manager.setFirstResult(range.getStart());
	}
	if (range.hasEnd()) {
	    manager.setMaxResults(range.getEnd() - range.getStart());
	}
	
	if (resultCache.containsKey(term)) return resultCache.get(term);
	
	EntitySearchResults results = new EntitySearchResults();
	int totalCount = 0;
	Map<Class<? extends Entity>,Integer> frequencies =
	    new HashMap<Class<? extends Entity>,Integer>();
	
	Map<String,Object> entityOptions = new HashMap<String,Object>();
	
	// This tells the EntityAdapters that we're accessing them through the
	// search page (as distinct from the named-entity browser), so that
	// they can behave differently if they need to.
	entityOptions.put("searcher", Boolean.TRUE);
	
	for (Class<? extends Entity> clazz : classes) {
	    int entityCount = manager
		.getMatchingEntityCount(term, clazz, entityOptions);
	    frequencies.put(clazz, new Integer(entityCount));
	    totalCount += entityCount;
	    
	    List<? extends Entity> entities =
		manager.getMatchingEntities(term, clazz, entityOptions);
	    
	    for (Entity entity : entities) {
		if (entity.getMaxOccurrenceCount() != null) {
		    EntitySearchResult result = new EntitySearchResult(entity);
		    results.add(result);
		}
	    }
	}
	results.setDocumentFrequencies(frequencies);
	//	results.setParameter(SearchOptions.DOCUMENT_FREQUENCIES, frequencies);
	results.setTotalHitCount(totalCount);
	
	resultCache.put(term, results);
	
	return results;
    }
    
    private class EntitySearchResults extends SearchResults<EntitySearchResult> {
	private Map<Class<? extends Entity>,Integer> documentFrequencies =
				    new HashMap<Class<? extends Entity>,Integer>();
	
	public Map<Class<? extends Entity>, Integer> getDocumentFrequencies() {
	    return documentFrequencies;
	}
	
	public void setDocumentFrequencies(Map<Class<? extends Entity>, 
					   Integer> documentFrequencies) {
	    this.documentFrequencies = documentFrequencies;
	}
    }
    
    private class EntitySearchResult extends SearchResult<Entity> {
	private int frequency;
	
	public EntitySearchResult(Entity e) {
	    setTitle(e.getDisplayName());
	    setIdentifier(e.getAuthorityName());
	    setContent(e);
	    setFrequency(e.getMaxOccurrenceCount().intValue());
	}
	
	public int getFrequency() {
	    return frequency;
	}
	
	public void setFrequency(int freq) {
	    frequency = freq;
	}
    }
}
