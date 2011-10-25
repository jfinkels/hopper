package perseus.ie.entity;

import java.util.List;
import java.util.Map;

import perseus.document.Chunk;
import perseus.ie.freq.EntityDocumentFrequency;

/**
 * DAO-like class meant for dealing with entities. Most classes use the
 * HibernateEntityManager class directly.
 */
public interface EntityManager {

    public List<? extends Entity> getMatchingEntities(
    		String keyword, Class cl, Map parameters);
    public List<EntityOccurrence> getOccurrences(Entity entity,
	    perseus.document.Query query);

    public EntityDocumentFrequency getFrequency(Entity entity, String documentID);
    public List<EntityDocumentFrequency> getFrequencies(Entity entity);

    public Entity getEntityById(int id);
    public List<? extends Entity> getEntitiesByAuthName(String id);
    public List<? extends Entity> getEntitiesByExample(Entity exemplar);

    public void registerEntity(Entity entity);
    public void unregisterEntity(Entity entity);

    public void addOccurrence(EntityOccurrence occurrence);
    public void addFrequency(EntityDocumentFrequency freq);
    public void addTuple(perseus.ie.freq.EntityTuple tuple);

    public void beginWrite();
    public void endWrite();

    public void cleanup();
    public EntityOccurrence getOccurrenceByEntity(Chunk chunk, Entity entity, int which);
}
