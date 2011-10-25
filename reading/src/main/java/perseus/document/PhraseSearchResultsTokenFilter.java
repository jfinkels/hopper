package perseus.document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PhraseSearchResultsTokenFilter extends TokenFilter {

    public List<String> phraseTokens = new ArrayList<String>();

    int outputPosition = 0;
    boolean keepContext;
    
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-z]");

    public static final int CONTEXT_TOKENS = 20;
    public static final String EMPTY_STRING = "";

    public PhraseSearchResultsTokenFilter(String phrase) {
	this(phrase, false);
    }

    public PhraseSearchResultsTokenFilter(String phrase, boolean keepCtxt) {

	keepContext = keepCtxt;

	if (phrase.startsWith("\"")) {
	    phrase = phrase.substring(1);
	}
	if (phrase.endsWith("\"")) {
	    phrase = phrase.substring(0, phrase.length()-1);
	}

	phrase = phrase.replaceAll("\\.","");
	String[] phraseWords = phrase.split("\\s");

	if (phrase != null) {
	    for (int i = 0; i < phraseWords.length; i++) {
		String token = phraseWords[i];
		phraseTokens.add(token.toLowerCase());
	    }
	}
    }

    public void filter(TokenList tokens) {

	outputPosition = 0;
	int position = 0;

	while (position < tokens.size()) {

	    // Without this initial check, the filter would occasionally
	    // start highlighting one token too early for unclear reasons
	    String thisToken = phraseTokens.get(0);
	    Token targetToken = tokens.get(position);
	    String target = targetToken.getOriginalText();

	    if (thisToken.equalsIgnoreCase(target)) {

		int endpoint = findPhraseEndpoint(tokens, position);

		if (endpoint != -1) {

		    Token startToken = tokens.get(position);

		    startToken.setDisplayText("<span class=\"phrase_result\">" +
			    startToken.getDisplayText());

		    if (!keepContext) {
			clearTo(tokens, position - CONTEXT_TOKENS);
			advanceTo(tokens, endpoint + CONTEXT_TOKENS);
		    }

		    Token endToken = tokens.get(endpoint);
		    endToken.setDisplayText(endToken.getDisplayText() +
			    "</span>");
		    position = endpoint;
		}
	    }
	    position++;
	}
		
	if (!keepContext) {
	    clearTo(tokens, tokens.size());
	}
    }

    private int findPhraseEndpoint(TokenList tokens, int position) {

	// A token is not necessarily a word--it could be space or a quote,
	// for example--so we can't give up when a word fails to match against
	// a token until we've checked that both tokens are actual words.

	int tokensMatched = 0;

	for (int i = position; i < tokens.size(); i++) {
	    
	    if (tokens.get(i).getType() != Token.Type.WORD) continue;

	    String thisToken = phraseTokens.get(tokensMatched);
	    String target = tokens.get(i).getOriginalText();

	    if (thisToken.equalsIgnoreCase(target)) {
		tokensMatched++;

		if (tokensMatched == phraseTokens.size()) {
		    return i;
		}
	    } else {
		return -1;
	    }
	}

	return -1;
    }

    protected void clearTo(TokenList tokens, int end) {
	while (outputPosition < end && outputPosition < tokens.size()) {
            Token token = tokens.get(outputPosition);
	    token.setDisplayText("");
	    outputPosition++;
	}
    }

    protected void advanceTo(TokenList tokens, int end) {
	// Add elipsis if we have just started ended a cleared section
	if (outputPosition > 0) {
	    Token previousToken = tokens.get(outputPosition - 1);
	    if (EMPTY_STRING.equals(previousToken.getDisplayText())) {
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
}

