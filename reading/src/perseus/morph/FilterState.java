package perseus.morph;

import java.util.regex.*;

/**
 * This class represents the current state of the morphology linking
 * filter. It is designed to be passed from the main linking routine
 * to one or more language-specific tokenizers.
 */
public class FilterState {
    public StringBuffer output;
    public StringBuffer input;

    public String currentToken;

    /**
     * Class constructor.
     */
    public FilterState (String s) {
	input = new StringBuffer(s);
	output = new StringBuffer(input.length());

	currentToken = null;
    }

    /**
     * This method returns true if there is more text to process
     *
     * @return true if more text to process
     */
    public boolean hasMore() {
	return input.length() > 0;
    }

    /**
     * This method returns true if the current token contains
     * text.
     *
     * @return true if the current token contains text
     */
    public boolean hasCurrentToken() {
	if (currentToken == null) {
	    return false;
	}
	if (currentToken.length() == 0) {
	    return false;
	}
	return true;
    }

    /** 
     * This method tells the FilterState to add the current
     * token to the output. It is useful for tokens that
     * are ignored, such as tags and entitites.
     * The current token is cleared.
     */
    public void proceed() {
	output.append(currentToken);
	currentToken = null;
    }

    /**
     * This method is similar to proceed(), but it passes
     * a substring of the current token rather than the whole
     * thing. The second argument is saved as the current token.
     *
     * @param s the token substring
     * @param remainder the rest of the token string not in s
     */
    public void proceed(String s, String remainder) {
	output.append(s);
	currentToken = remainder;
    }

    /**
     * This method drops the current token.
     */
    public void ignore() {
	currentToken = null;
    }

    /**
     * A version of ignore that only drops a substring of
     * the current token. The calling program must pass in
     * the remaining segment of the current token.
     *
     * @param remainder the string to be set as the entire token
     */
    public void ignore(String remainder) {
	currentToken = remainder;
    }

    /**
     * This method tells the FilterState to add the current token,
     * surrounded by the specified text. This is usually used
     * for adding a hypertext link, but it could be anything.
     * The current token is cleared.
     * 
     * @param leftText left context for the token 
     * @param rightText right context for the token
     */
    public void link(String leftText, String rightText) {
	output.append(leftText)
	    .append(currentToken)
	    .append(rightText);
	currentToken = null;
    }

    /**
     * Link a subset of the current token, setting the remainder
     * as the current token.
     * 
     * @param leftText the text that appears to the left of the token
     *   e.g. "<a href=blah.html>"
     * @param linkText the token itself, e.g. "blah"
     * @param rightText the text after the token, e.g. "</a>"
     * @param remainder whatever is left of the current token
     */
    public void link(String leftText, String linkText,
		     String rightText, String remainder) {
	output.append(leftText)
	    .append(linkText)
	    .append(rightText);
	currentToken = remainder;
    }


    /**
     * Retrieve the current token
     *
     * @return currentToken
     */
    public String getCurrentToken() {
	return currentToken;
    }

    /**
     * True if the input attribute matches the pattern
     *
     * @param pattern the pattern to compare the input to
     * @return true if the input matches the pattern
     */
    public boolean matches(Pattern pattern) {
	Matcher matcher = pattern.matcher(input);

	if (matcher.lookingAt()) {
	    setCurrentToken(matcher.group(), matcher.end());

	    return true;
	}
	return false;
    }

    /**
     * True if the current token matches the pattern
     *
     * @param pattern the pattern to compare the token to
     * @return true if the current token matches the pattern
    */
    public boolean matchesCurrentToken(Pattern pattern) {
	Matcher matcher = pattern.matcher(currentToken);

	if (matcher.lookingAt()) {
	    setCurrentToken(matcher.group(), matcher.end());

	    return true;
	}
	return false;
    }

	

    /**
     * This method is called by the tokenizer to advance the
     * current token. It also strips off the new token from 
     * the input buffer.
     * If the previous token has not been ignored, linked, or passed
     * to output, it is written to output and cleared. This
     * means that to drop a token, you must explicitly call ignore().
     *
     * @param s the value to set the current token to
     * @param end
     */
    public void setCurrentToken(String s, int end) {
	// Make sure the current token is flushed to output
	if (currentToken != null) {
	    proceed();
	}
	currentToken = s;

	input.delete(0, end);
    }

    /**
     * Serialize the output attribute of FilterState as a String
     *
     * @return the value of output
    */
    public String toString() {
	return output.toString();
    }
    
}
