package perseus.document;

import java.util.*;

public class TokenAnnotation implements Comparable {
    int startToken = 0;
    int endToken = 0;

    boolean isSuppressed = false;

    String type;
    String value;

    public static final String TERM_TYPE = "term";
    public static final String LINK_TYPE = "link";

    public TokenAnnotation (int s, int e, String v) {
	startToken = s;
	endToken = e;
	type = TERM_TYPE;
	value = v;
    }

    public void suppress() {
	isSuppressed = true;
    }

    public int getStartToken() {
	return startToken;
    }

    public int getEndToken() {
	return endToken;
    }

    public String getType() {
	return type;
    }

    public String getValue() {
	return value;
    }

    public void apply(TokenList tokens, String preText, String postText) {
	if (isSuppressed) {
	    return;
	}

	Token token = (Token) tokens.get(startToken);
	token.setDisplayText(preText + token.getDisplayText());

	token = (Token) tokens.get(endToken);
	token.setDisplayText(token.getDisplayText() + postText);
    }

    public int compareTo (Object o) throws ClassCastException {
	if (! (o instanceof TokenAnnotation)) {
	    throw new ClassCastException("Unable to compare object to this TokenAnnotation");
	}

	TokenAnnotation other = (TokenAnnotation) o;

	if (startToken != other.startToken) {
	    return startToken - other.startToken;
	}
	else if (endToken != other.endToken) {
	    // This rule makes it so that if two terms start
	    // at the same position, the longer one is sorted first.
	    return other.endToken - endToken;
	}

	return 0;
    }
}
