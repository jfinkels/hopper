package perseus.document;

public abstract class TokenFilter {
    
    public void filter(TokenList tokens) {
	for (Token token : tokens) {
	    process(token);
	}
	
	cleanup();
    }
    
    public abstract void process(Token token);
    
    /** empty callback method that allows subclasses to close resources
     as necessary */
    public void cleanup() {
	
    }
    
    /**
     * Returns true if this filter will act upon the given text in
     * any way, false if not. This is used with the renderText() method in
     * Renderer to avoid tokenizing strings if it's not necessary to.
     */
    public boolean willFilter(String text, String defaultLanguage) {
	return true;
    }
}
