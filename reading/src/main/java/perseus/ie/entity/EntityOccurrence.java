package perseus.ie.entity;

import perseus.ie.Location;
import perseus.voting.Vote;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Represents a single occurrence of an entity in a document.
 */

public class EntityOccurrence implements Comparable<EntityOccurrence> {

    private Integer id = new Integer(-1);

    /** The entity in question */
    private Entity entity;

    /** Where the entity occurred */
    private Location location;

    /** Any votes appertaining to this occurrence */
    private Set<Vote> votes = new HashSet<Vote>(0);

    /** The text representing the occurrence
     * (e.g., "Robert Lee" or "R.E.&nbsp;Lee") */
    private String displayText;

    /** Any distinguishing parameters for this occurence (not used) */
    private Map parameters;

    /** Comparator for sorting by document ID/offset */
    public static final Comparator<EntityOccurrence> LOCATION_COMPARATOR =
	new Comparator<EntityOccurrence>() {

	public int compare(EntityOccurrence eo1, EntityOccurrence eo2) {
	    return eo1.getLocation().compareTo(eo2.getLocation());
	}
    };

    /** Comparator for sorting by entity */
    public static final Comparator<EntityOccurrence> ENTITY_COMPARATOR =
	new Comparator<EntityOccurrence>() {

	public int compare(EntityOccurrence eo1, EntityOccurrence eo2) {
	    return eo1.getEntity().compareTo(eo2.getEntity());
	}
    };

    // Empty constructor for hibernate
    public EntityOccurrence() {}

    public EntityOccurrence(Entity ent, Location loc, String dispText) {
	this(ent, loc, dispText, new HashMap());
    }

    public EntityOccurrence(Entity ent, Location loc, String dispText,
	    Map params) {
	entity = ent;
	location = loc;
	displayText = dispText;
	parameters = params;
    }
    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Entity getEntity() {
	return entity;
    }

    public void setEntity(Entity e) {
	entity = e;
    }

    public Location getLocation() {
	return location;
    }

    public void setLocation(Location loc) {
	location = loc;
    }

    public String getDisplayText() {
	return displayText;
    }

    public void setDisplayText(String dt) {
	displayText = dt;
    }

    public Map getParameters() {
	return parameters;
    }

    public void setParameters(Map params) {
	parameters = params;
    }

    public String toString() {
	return String.format("%s @ [%s/%d]",
		entity.getAuthorityName(), location.getQuery().getDocumentID(),
		location.getPosition());
    }
    
    public int compareTo(EntityOccurrence other) {
	return getLocation().compareTo(other.getLocation());
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }
}
