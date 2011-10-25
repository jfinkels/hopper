package perseus.ie.freq;

import perseus.document.Chunk;
import perseus.ie.entity.Entity;

/**
 * Frequency subclasses measuring the occurrences of an entity in a particular
 * chunk. Replaces the old EntityTuple class in `perseus.ie.entity`.
 */

public class EntityTuple extends ChunkFrequency implements EntityBased {
    
    private Entity entity;
    private String snippet;

    public EntityTuple() {}

    public EntityTuple(Entity e) {
	super();
	entity = e;
    }

    public EntityTuple(Entity e, Chunk chunk) {
	this(e);
	setChunk(chunk);
    }

    public Entity getEntity() {
	return entity;
    }

    public void setEntity(Entity e) {
	entity = e;
    }
    
    public String getSnippet() {
        return snippet;
    }
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
    
    public String getForm() {
	return entity.getAuthorityName();
    }

    @Override
    public String getDocumentID() {
	return getChunk().getDocumentID();
    }

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
