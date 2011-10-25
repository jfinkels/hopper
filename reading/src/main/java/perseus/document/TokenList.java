package perseus.document;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import perseus.language.Language;

public class TokenList implements Iterable<Token>, Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(TokenList.class);

	Set<TokenAnnotation> annotations = new TreeSet<TokenAnnotation>();

	private List<Token> tokens = new ArrayList<Token>();

	public TokenList () {
	}

	private TokenList(List<Token> tokens) {
		this.tokens.addAll(tokens);
	}

	public static TokenList getTextTokens(String text, Language language) {
		Tokenizer tokenizer = Tokenizer.taglessTokenizer(text, language);
		return tokenizer.getTokens();
	}

	/**
	 * Returns the text of the given chunk as a TokenList. The contents of the
	 * chunk should be XML.
	 *
	 * @param chunk the Chunk whose text is to be tokenized
	 * @return the tokenized text, or null if the text couldn't be tokenized
	 */
	public static TokenList getTokensFromXML(Chunk chunk) {
		String chunkText = chunk.getText();
		if (chunkText == null) {
			throw new IllegalStateException();
		}

		TokenizingParser parser = new TokenizingParser(chunk.getText(),
				chunk.getMetadata().getLanguage());
		try {
			return parser.getOutputFromXML();
		} catch (SAXException se) {
			logger .warn("Problem parsing chunk text as XML", se);
			return null;
		}
	}

	/**
	 * Returns the given text as a TokenList.
	 *
	 * @param text the text to tokenize
	 * @param defaultLanguage the language of the text
	 * @return the text as a TokenList
	 */
	public static TokenList getTokens(String text, Language defaultLanguage) {
		TokenizingParser parser = new TokenizingParser(text, defaultLanguage);
		return parser.getOutput();
	}

	/**
	 * Returns the contents of the given URL as a TokenList.
	 *
	 * @param url the URL containing the text to be tokenized
	 * @param defaultLanguage the language of the URL's contents
	 * @return the contents of the URL as a TokenList
	 *
	 * @throws IOException if connecting to/reading from the URL fails
	 */
	public static TokenList getTokens(URL url, Language defaultLanguage) throws IOException {
		InputStream stream = url.openStream();
		TokenizingParser parser = new TokenizingParser(stream, defaultLanguage);
		stream.close();

		return parser.getOutput();
	}

	public void addAnnotation(TokenAnnotation note) {
		annotations.add(note);
	}

	public void removeOverlappingAnnotations () {
		int currentStart = 0;
		int currentEnd = 0;

		for (TokenAnnotation note : annotations) {
			// Are we inside the current annotation?
			if (note.getStartToken() <= currentEnd) {
				note.suppress();
			}
			else {
				currentStart = note.getStartToken();
				currentEnd = note.getEndToken();
			}
		}
	}

	public Set<TokenAnnotation> getAnnotations() {
		return annotations;
	}

	public Token get(int i) {
		return tokens.get(i);
	}

	public int indexOf(Token token) {
		return tokens.indexOf(token);
	}

	public int size() {
		return tokens.size();
	}

	public void add(Token token) {
		tokens.add(token);
	}

	public void addAll(TokenList tokens) {
		this.tokens.addAll(tokens.tokens);
	}

	public void addAll(int index, TokenList tokens) {
		this.tokens.addAll(index, tokens.tokens);
	}

	public void remove(int index) {
		tokens.remove(index);
	}

	public List<Token> getTokens() {
		return tokens;
	}

	/**
	 * Returns a new TokenList containing all Tokens in this list of the
	 * specified type.
	 *
	 * @param tokenType the type of Token, which should be one of the constants
	 * in the Token class, or the bitwise and of several
	 * @return a new TokenList containing tokens of the specified type
	 */
	public TokenList getTokensOfType(Token.Type type) {
		return getTokensOfType(EnumSet.of(type));
	}

	public TokenList getTokensOfType(EnumSet<Token.Type> types) {
		List<Token> matches = new ArrayList<Token>();

		for (Token token : tokens) {
			if (types.contains(token.getType())) {
				matches.add(token);
			}
		}

		return new TokenList(matches);
	}

	public Iterator<Token> iterator() {
		return tokens.iterator();
	}

	public static TokenList getTokens(Query query) {
		TokenList chunkSequence = new TokenList();
		try {
			Chunk chunk = query.getChunk();
			String text = chunk.getText();
			Language language = chunk.getMetadata().getLanguage();
			chunkSequence = TokenList.getTokens(text, language);
		} catch (Exception e) {
			logger.error("EntitiesTokenTest: " + e);
		}
		return chunkSequence;
	}

}
