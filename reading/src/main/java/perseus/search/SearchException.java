package perseus.search;

public class SearchException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SearchException(Exception e) {
		super(e);
	}
}
