package perseus.ie.freq;

import java.util.Comparator;

import perseus.ie.entity.Entity;

/**
 * Interface for frequencies that deal with entities (as opposed to, say,
 * forms).
 */
public interface EntityBased {
    public Entity getEntity();
    public void setEntity(Entity e);
    
    public static final Comparator<? extends EntityBased> ENTITY_COMPARATOR =
	new Comparator<EntityBased>() {
	public int compare(EntityBased et1, EntityBased et2) {
	    return et1.getEntity().compareTo(et2.getEntity());
	}
    };
}
