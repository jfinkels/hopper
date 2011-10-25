/**
 * @GPL@
 * Last Modified: @TIME@
 *
 * DAOType is a typesafe enum pattern as discussed by:
 *  Block, Joshua in "Effective Java" on page 105.
 **/
package perseus.util;

public class DAOType {
    private final String name;
    private final String displayName;
    
    private DAOType(String name, String displayName) {
	this.name = name;
	this.displayName = displayName;
    }
    
    public String getDisplayName() { return this.displayName;}
    public String toString() { return displayName; }

    public static final DAOType HIBERNATE = new DAOType("Hibernate", "Local Database");
    public static final DAOType ANSOAI = new DAOType("AnsOai", "ANS");
    public static final DAOType PERSEUS_SERVICE = new DAOType("PersService", "Perseus XML Service");
}
