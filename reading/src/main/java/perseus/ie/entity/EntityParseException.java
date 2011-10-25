package perseus.ie.entity;

/**
 * Class representing a problem encountered when attempting to create an
 * entity from a string of text. An example might be a badly-formatted
 * date.
 */
public class EntityParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** The class we wanted to extract */
    private Class<? extends Entity> attemptedClass;
    /** The problematic text */
    private String displayName;
    
    /**
     * Creates a new exception.
     *
     * @param ac the class we were trying to create
     * @param dn the display text we were trying to parse
     */
    public EntityParseException(Class<? extends Entity> ac, String dn) {
	super();
	attemptedClass = ac;
	displayName = dn;
    }
    
    public String toString() {
	return String.format("invalid %s: [ %s ]",
		attemptedClass.getSimpleName(),
		displayName);
    }
}
