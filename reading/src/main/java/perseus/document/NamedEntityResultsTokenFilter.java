package perseus.document;

import java.util.*;
import java.util.regex.*;
import perseus.util.*;

public class NamedEntityResultsTokenFilter extends TokenFilter {
    
    private Set namedEntityTerms;

    int outputPosition = 0;

    Pattern SPAN_PATTERN = Pattern.compile("^<span ");

    public static final int CONTEXT_TOKENS = 20;
    public static final String EMPTY_STRING = "";

    public NamedEntityResultsTokenFilter () {
    }

    public NamedEntityResultsTokenFilter (Set namedEntityTerms) {
	this.namedEntityTerms = new HashSet();
	if (namedEntityTerms != null) {
	    Iterator inputIterator = namedEntityTerms.iterator();
	    while (inputIterator.hasNext()) {
		String namedEntityTerm = (String) inputIterator.next();
		this.namedEntityTerms.add(namedEntityTerm.toLowerCase());
	    }
	}
    }

    /** 
	This filter needs to look at more than one token at a time, so
	it overrides the filter method.
    */
    public void filter(List tokens) {
	outputPosition = 0;

	for (int position=0;position < tokens.size();position++) {
            Token token = (Token) tokens.get(position);
	    if (token.getType() == Token.Type.TAG &&
		token.getOriginalText().startsWith("<span ")) {

		//namedEntityTerms.contains(StringUtil.deaccent(token.getOriginalText().toLowerCase()))) {

		// Handle context
		clearTo(tokens, position - CONTEXT_TOKENS);
		advanceTo(tokens, position + CONTEXT_TOKENS);

		token.setDisplayText(token.getOriginalText());
	    }
        }

	// clear out any additional tokens
	clearTo(tokens, tokens.size());
    }

    private void clearTo(List tokens, int end) {
	while (outputPosition < end && outputPosition < tokens.size()) {
            Token token = (Token) tokens.get(outputPosition);
	    token.setDisplayText("");
	    outputPosition++;
	}
    }

    private void advanceTo(List tokens, int end) {
	// Add elipsis if we have just started ended a cleared section
	if (outputPosition > 0) {
	    Token previousToken = (Token) tokens.get(outputPosition - 1);
	    if (EMPTY_STRING.equals(previousToken.getDisplayText())) {
		previousToken.setDisplayText("... ");
	    }
	}
	
	while (outputPosition < end && outputPosition < tokens.size()) {
            Token token = (Token) tokens.get(outputPosition);
	    if (token.getType() == Token.Type.TAG &&
		! token.getOriginalText().equals("</span>")) {
		token.setDisplayText(" ");
	    }
	    outputPosition++;
	}
    }

    public void process(Token token) {
	// Just here to fulfill the TokenFilter API
    }
}
