package perseus.document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import perseus.language.LanguageAdapter;
import perseus.util.StringUtil;

/**
 * TokenFilter for highlighting one or more search results in a TokenList
 * and, optionally, deleting all tokens sufficiently far from any results.
 */

public class SearchResultsTokenFilter extends TokenFilter {
    
    private Set<String> searchTerms = new HashSet<String>();
    private List<Token> matchingTokens = new ArrayList<Token>();
    private Set<String> matchingWords = new HashSet<String>();
    private boolean keepFullContext = false;
    private boolean lookedUpMatches = false;

    int outputPosition = 0;

    public static final int CONTEXT_TOKENS = 20;
	public static final int MATCH_HIGHLIGHT_LIMIT = 8;
	
	private int totalMatchCount = 0;

    /**
     * Creates a SearchResultsTokenFilter that will match on any of the
     * strings in `terms` and delete all tokens not in close range of
     * matches.
     *
     * @param terms one or more terms to match on
     */
    public SearchResultsTokenFilter (Set<String> terms) {
	if (terms != null) {
	    for (String searchTerm : terms) {
		this.searchTerms.add(searchTerm.toLowerCase());
	    }
	}
    }

    /**
     * Creates a SearchResultsTokenFilter that will match on any of the
     * strings in `terms` and, if `keepCtxt` is false, delete all tokens not
     * in close range of matches.
     *
     * @param terms one or more terms to match on
     * @param keepCtxt whether to leave the entire TokenList intact
     */
    public SearchResultsTokenFilter(Set<String> searchTerms, boolean keepCtxt) {
	this(searchTerms);
	keepFullContext = keepCtxt;
    }

    /**
     * Find all our matching tokens, caching both the actual tokens and the
     * normalized word for each token.
     */
    public void findMatchingTokens(TokenList tokens) {

	matchingTokens = new ArrayList<Token>();
	matchingWords = new HashSet<String>();

	for (Token token : tokens) {
	    if (token.getType() == Token.Type.WORD ||
		    token.getType() == Token.Type.NON_WORD) {
		String matchableWord = token.getOriginalText().toLowerCase();
		
		matchableWord = LanguageAdapter.getLanguageAdapter(
			token.getLanguageCode()).getLookupForm(matchableWord);
		
		if (searchTerms.contains(matchableWord)) {
		    matchingTokens.add(token);
		    matchingWords.add(matchableWord);
		}
	    }
	}

	lookedUpMatches = true;
    }

    /** 
	This filter needs to look at more than one token at a time, so
	it overrides the filter method.
    */
    public void filter(TokenList tokens) {
	outputPosition = 0;
	totalMatchCount = 0;

	// If we've already cached our matches, we only need to look at
	// a few specific tokens
	if (lookedUpMatches) {

	    int position = 0;
	    for (Token token : tokens) {
		if (matchingTokens.contains(token)) {
		    filterToken(tokens, token, position);
		}
		
		position++;
	    }

	    lookedUpMatches = false;
	    
	} else {
	    // Otherwise, loop through all the tokens as normal

	    matchingTokens = new ArrayList<Token>();
	    matchingWords = new HashSet<String>();
	
	    for (int position=0;position < tokens.size();position++) {
		Token token = (Token) tokens.get(position);
		if (token.getType() == Token.Type.WORD) {
		    

		    String matchableWord = token.getOriginalText().toLowerCase();
		    
		    matchableWord = LanguageAdapter.getLanguageAdapter(
			    token.getLanguageCode()).getLookupForm(matchableWord);

		    if (searchTerms.contains(matchableWord)) {
			totalMatchCount++;
			matchingTokens.add(token);
			matchingWords.add(matchableWord);
			
			if (totalMatchCount < MATCH_HIGHLIGHT_LIMIT) {
			filterToken(tokens, token, position);
		    }
		}
		}
	    }
	
	}

	// clear out any additional tokens
	if (! keepFullContext) {
	    clearTo(tokens, tokens.size());
	}
    }

    private void filterToken(TokenList tokens, Token token, int position) {

	// Handle context
	if (!keepFullContext) {
	    clearTo(tokens, position - CONTEXT_TOKENS);
	    advanceTo(tokens, position + CONTEXT_TOKENS);
	}

	// Highlight the search term
	StringBuffer newToken = new StringBuffer();
	newToken.append("<span class=\"search_result\">")
	    .append(token.getDisplayText())
	    .append("</span>");

	token.setDisplayText(newToken.toString());
    }

    protected void clearTo(TokenList tokens, int end) {
	while (outputPosition < end && outputPosition < tokens.size()) {
            Token token = (Token) tokens.get(outputPosition);
	    token.setDisplayText("");
	    outputPosition++;
	}
    }

    protected void advanceTo(TokenList tokens, int end) {
	// Add elipsis if we have just started ended a cleared section
	if (outputPosition > 0) {
	    Token previousToken = tokens.get(outputPosition - 1);
	    if ("".equals(previousToken.getDisplayText())) {
		previousToken.setDisplayText("... ");
	    }
	}
	
	while (outputPosition < end && outputPosition < tokens.size()) {
            Token token = tokens.get(outputPosition);
	    if (token.getType() == Token.Type.TAG) {
		token.setDisplayText(" ");
	    }
	    outputPosition++;
	}
    }

    public void process(Token token) {
	// Just here to fulfill the TokenFilter API
    }

    /**
     * Returns all Tokens that matched any of the target terms.
     */
    public List<Token> getMatchingTokens() {
	return matchingTokens;
    }

    public void setMatchingTokens(List<Token> tokens) {
	matchingTokens = tokens;
    }

    /**
     * Returns the original text, joined with a comma, of every token that matched.
     * This is used in passing a "highlight" parameter from the search results
     * page to the text page.
     */
    public String matchingTokensAsString() {
	return StringUtil.join(matchingWords, ",");
    }

    /**
     * Returns the total number of matches found in this token list.
     */
    public int getTotalMatchCount() {
        return totalMatchCount;
    }

    /**
     * Returns the number of matches that were actually highlighted. May be
     * equal to or less than the total number of matches.
     */
    public int getHighlightedMatchCount() {
        return Math.min(totalMatchCount, MATCH_HIGHLIGHT_LIMIT);
    }
}
