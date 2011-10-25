package perseus.ie.entity;
/**
 * @GPL@
 * Last Modified: @TIME@
 *
 * EntityType is a typesafe enum pattern as discussed by:
 *  Block, Joshua in "Effective Java" on page 105.
 **/

import javax.servlet.http.HttpServletRequest;

public class EntityType {
    private final String name;
    
    private EntityType(String name) {this.name = name;}
    
    public String toString() { return name; }
    
    public String getDisplayName() { return name; }

    public static final EntityType LEMMA = new EntityType("Lemma");
    public static final EntityType PERSON = new EntityType("Person");
    public static final EntityType PLACE = new EntityType("Place");
    public static final EntityType DATE = new EntityType("Date");

    public static EntityType getEntityType(HttpServletRequest request) {
	String entity_type_str = request.getParameter("entityType");
	if ( (entity_type_str == null) || (entity_type_str.equals("")) ) {
	    return EntityType.LEMMA;
	} else if (entity_type_str.equals(EntityType.LEMMA.getDisplayName())) {
	    return EntityType.LEMMA;
	} else {
	    return EntityType.LEMMA;
	}
    }

}
