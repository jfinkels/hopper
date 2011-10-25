package perseus.document;

/** This exception is thrown by the chunk loader when it receives a 
    query for an invalid or non-existent chunk.
*/

public class InvalidQueryException extends Exception {

    public InvalidQueryException (Query query) {
	super(query.toString());
    }
    
    public InvalidQueryException (String message) {
	super(message);
    }

}
