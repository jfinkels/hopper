package perseus.document;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import perseus.language.Language;

/**
 * Represents some segment of a chunk, possibly a word, a non-word
 * (punctuation/space), a tag, or something else.
 *
 * Each token has several fields:
 * <ul>
 *  <li>`language` - the language of the token, as determined during tokenization</li>
 *  <li>`originalText` - the token's original text, without any modifications</li>
 *  <li>`displayText` - the token's text upon being run through a Renderer</li>
 *  <li>`type` - the type of the token (one of several enumerated values)</li>
 *  <li>`context` - any relevant contextual information (this field may or may not be set)</li>
 * </ul>
 */

public class Token implements Serializable {
    private static final long serialVersionUID = 1L;

    private Language language;

    private String originalText;
    private String displayText;
    
    private Type type;

    private TokenContext context;
    
    public enum Type {
	WORD,
	NON_WORD,
	TAG,
	ENTITY,
	LINK_START,
	LINK_END;
    }

    /** Error flag returned by the find token helper methods */
    public static final int NOT_FOUND = -1;
    
    // This is used for extracting element names from tags
    public static final Pattern ELEMENT_NAME_PATTERN =
	Pattern.compile("</?(\\w+)");

    public Token (Language lc, String text, Type t) {
	this(lc, text, t, null);
    }

    public Token (Language lc, String text, Type t, TokenContext c) {
	setLanguage(lc);

	originalText = text;
	displayText = text;

	type = t;

	context = c;
    }

    public String getLanguageCode() {
	return language.getCode();
    }
    
    public Language getLanguage() {
	return language;
    }
    
    public void setLanguage(Language l) {
	language = l;
    }
    
    public void setLanguageCode(String code) {
	language = Language.forCode(code);
    }

    public String getOriginalText() {
	return originalText;
    }

    public String getDisplayText() {
	return displayText;
    }

    public void setDisplayText(String newText) {
	displayText = newText;
    }

    public TokenContext getContext() {
	return context;
    }

    public boolean hasContext() {
	return (context != null);
    }

    public void setContext(TokenContext ctxt) {
	context = ctxt;
    }

    public Type getType() {
	return type;
    }

    public static int findPreviousToken(TokenList tokens, int startingPosition,
					Type type) {
	for (int pos=startingPosition - 1; pos >= 0; pos--) {
	    Token token = tokens.get(pos);
	    if (token.getType() == type) {
		return pos;
	    }
	}

	return NOT_FOUND;
    }

    public static int findNextToken(TokenList tokens, int startingPosition,
				    Type type) {
	for (int pos=startingPosition + 1; pos < tokens.size(); pos++) {
	    Token token = tokens.get(pos);
	    if (token.getType() == type) {
		return pos;
	    }
	}

	return NOT_FOUND;
    }

    /**
     * Returns true if this token represents an end tag.  End tags, like start
     * tags, are still of type TAG_TYPE, as we don't want to break prior
     * functionality.
     */
    public boolean isEndTag() {
	return (type == Type.TAG && originalText.startsWith("</"));
    }

    /**
     * Returns true if this token represents an empty XML tag.  End tags, like
     * start tags, are still of type TAG_TYPE, as we don't want to break prior
     * functionality.
     */
    public boolean isEmptyTag() {
	return (type == Type.TAG && originalText.endsWith("/>"));
    }

    /**
     * Returns the name of the element represented by this Token, or null if
     * the token does not represent an element.
     */
    public String getElementName() {
	if (type != Type.TAG) {
	    return originalText;
	}

	Matcher matcher = ELEMENT_NAME_PATTERN.matcher(originalText);

	if (matcher.find()) {
	    return matcher.group(1);
	}

	return null;
    }

    public String toString() {
	return String.format("%s %s [%s] -> [%s]",
		type.name(), language.getCode(), originalText, displayText);
    }
}
