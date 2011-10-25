package perseus.document;

import java.util.ArrayList;
import java.util.List;

import perseus.language.Language;
import perseus.util.DisplayPreferences;

/**
 * Outputs a {@link TokenList} as a String, optionally applying one or more
 * TokenFilters to the individual tokens. The TokenFilters are applied in the
 * order that they were added to the TokenList.
 */

public class Renderer {
    
    List<TokenFilter> filtersList;

    public Renderer(Language language, TokenFilter... tokenFilters) {
	filtersList = new ArrayList<TokenFilter>();
	setDefaultLanguage(language);
	for (TokenFilter filter : tokenFilters) {
	    addTokenFilter(filter);
	}
    }

    // Knowing the default language allows us to render the entire chunk in
    // that language (except for parts explicitly specified as being part of
    // another language), which is useful for displaying Greek with
    // non-Unicode fonts.
    Language defaultLanguage;
    
    /**
     * Creates a new Renderer with English as the default language.
     */
    public Renderer() {
	this(Language.ENGLISH);
    }
    
    /**
     * Creates a new Renderer with `languageCode` representing the default
     * language.
     */
    public Renderer(String languageCode) {
	this(Language.forCode(languageCode));
    }
    
    /**
     * Creates a new Renderer with `defLang` as the default language.
     */
    public Renderer(Language defLang) {
	filtersList = new ArrayList<TokenFilter>();
	defaultLanguage = defLang;
    }
    
    /**
     * Adds `filter` to the list of filters this Renderer will run on any
     * string or TokenList passed to it.
     */
    public void addTokenFilter(TokenFilter filter) {
	filtersList.add(filter);
    }
    
    /**
     * Adds all language filters
     * @param prefs
     */
    public void addLanguageTokenFilters(DisplayPreferences prefs) {
    	filtersList.add(new GreekTranscoderTokenFilter(prefs));
    	filtersList.add(new ArabicTranscoderTokenFilter(prefs));
    }
    
    /**
     * Renders the text of `chunk`, using the language given by the chunk's
     * `getEffectiveLanguage()` method.
     */
    public synchronized String renderChunk(Chunk chunk) {
	Language chunkLanguage =
	    Language.forCode(chunk.getEffectiveLanguage());
	String result;
	if (!chunkLanguage.equals(defaultLanguage)) {
	    Language oldLanguage = defaultLanguage;
	    defaultLanguage = chunkLanguage;
	    result = renderText(chunk.getText());
	    defaultLanguage = oldLanguage;
	} else {
	    result = renderText(chunk.getText());	    
	}
	return result;
    }
    
    /**
     * Renders `text`, after first converting it into a TokenList.
     */
    public String renderText(String text) {
	
	List<TokenFilter> actingFilters = new ArrayList<TokenFilter>();
	for (TokenFilter filter : filtersList) {
	    if (filter.willFilter(text, defaultLanguage.getCode())) {
		actingFilters.add(filter);
	    }
	}
	
	if (actingFilters.isEmpty()) {
	    return text;
	}
	
	TokenList tokens = TokenList.getTokens(text, defaultLanguage);
	
	for (TokenFilter filter : actingFilters) {
	    filter.filter(tokens);
	}
	
	return tokensToString(tokens);
    }
    
    /**
     * Renders a single string, which is treated as a token consisting of a
     * word.
     *
     * @param str the string to render
     * @return the rendered string
     */
    public String render(String str) {
	Token token = new Token(defaultLanguage, str, Token.Type.WORD);
	return render(token);
    }
    
    /**
     * Renders a single token.
     *
     * @param token the token to render
     * @return the token's display text, filtered and rendered
     */
    public String render(Token token) {
	TokenList tokenList = new TokenList();
	tokenList.add(token);
	return render(tokenList);
    }
    
    /**
     * Renders the given list of tokens.
     *
     * @param tokens the list of tokens to render
     * @return the tokens, with matching display text
     */
    public String render(TokenList tokens) {
	for (TokenFilter filter : filtersList) {
	    filter.filter(tokens);
	}
	
	return tokensToString(tokens);
    }
    
    /**
     * Returns `tokens` as a string. Essentially does nothing but join the
     * tokens' display text.
     */
    public String tokensToString(TokenList tokens) {
	StringBuilder output = new StringBuilder();
	
	for (Token token : tokens) {
	    output.append(token.getDisplayText());
	}
	
	return output.toString();
    }
    
    public Language getDefaultLanguage() {
	return defaultLanguage;
    }
    
    public void setDefaultLanguage(Language defLang) {
	defaultLanguage = defLang;
    }
}
