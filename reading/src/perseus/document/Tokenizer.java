package perseus.document;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import perseus.language.Language;
import perseus.language.LanguageAdapter;
import perseus.util.ObjectCounter;
import perseus.util.StringUtil;

/**
 * Converts a string into a list of tokens, each of which represents a word, a non-word
 * (space/punctuation), or something else (a tag, perhaps). In general, it is
 * faster to use a {@link TokenizingParser}, which wraps a SAX parser, to parse
 * an XML document.
 */
public class Tokenizer {

	private static Pattern tagPattern = Pattern.compile("^(<[^>]+>)");
	private static Pattern entityPattern = Pattern.compile("^([&_][a-zA-Z0-9]+\\;)");
	private static Pattern delimiterPattern = Pattern.compile("^([\\s\\.\\:,'\"]+)");
	private static Pattern textPattern = Pattern.compile("^([^\\s\\.\\:,<&\"]+)");
	private static Pattern linkStartPattern = Pattern.compile("<a .*href=.*>");

	// This pattern should be used in place of textPattern when we know that
	// we're tokenizing plain text, which may include stray characters like
	// less-thans (which have perhaps been created by an XML parser) and
	// ampersands but definitely won't include tags.
	private static Pattern taglessTextPattern =
		Pattern.compile("^([^\\s\\.\\:,\"]+)");

	private Stack<Language> languageStack = new Stack<Language>();

	private StringBuffer input = null;
	private String currentToken = null;

	boolean noTags = false;

	/**
	 * Returns a special Tokenizer that presupposes it is scanning text with
	 * no tags (for example, a chunk that has been run through striptext.xsl).
	 * Since it does not need to check for tags, it runs somewhat faster than
	 * a normal Tokenizer. Of note: this method returns <strong>only word
	 * tokens</strong>, no non-word tokens. It is expected to be used in
	 * classes like the CorpusProcessor, whose subclasses (at least the ones
	 * that override processToken()) don't care about non-words.
	 * 
	 * @param text the text to tokenize, without tags of any sort
	 * @param language the language of the text
	 * @return a TokenList with only word tokens
	 */
	public static Tokenizer taglessTokenizer(
			final String text, Language language) {

		return new Tokenizer(text, language) {
			private ObjectCounter<String> formCounts =
				new ObjectCounter<String>();

			public TokenList getTokens() {
				TokenList tokens = new TokenList();

				Pattern wordPattern =
					getCurrentLanguage().getAdapter().getWordPattern();
				Pattern nonWordPattern =
					getCurrentLanguage().getAdapter().getNonWordPattern();

				Scanner scanner = new Scanner(text.toString());
				int lastPosition = 0;

				// Whitespace will always be a non-word. Split the string up by
				// whitespace, then search out words and non-words within each
				// of the split words.
				while (scanner.hasNext()) {
					String nextToken = scanner.next();
					Matcher wordMatcher = wordPattern.matcher(nextToken);
					Matcher nonWordMatcher = nonWordPattern.matcher(nextToken);
					Matcher entityMatcher = entityPattern.matcher(nextToken);

					while (true) {
						if (entityMatcher.lookingAt()) {
							tokens.add(new Token(getCurrentLanguage(),
									entityMatcher.group(), Token.Type.NON_WORD));
							if (entityMatcher.end() == nextToken.length()) break;
							wordMatcher.region(entityMatcher.end(), nextToken.length());
							nonWordMatcher.region(entityMatcher.end(), nextToken.length());
						}

						if (nonWordMatcher.lookingAt()) {
							tokens.add(new Token(getCurrentLanguage(),
									nonWordMatcher.group(), Token.Type.NON_WORD));
							if (nonWordMatcher.end() == nextToken.length()) break;
							wordMatcher.region(nonWordMatcher.end(), nextToken.length());
							entityMatcher.region(nonWordMatcher.end(), nextToken.length());
						}

						if (wordMatcher.lookingAt()) {
							tokens.add(new Token(getCurrentLanguage(),
									wordMatcher.group(), Token.Type.WORD));
							if (wordMatcher.end() == nextToken.length()) break;
							nonWordMatcher.region(wordMatcher.end(), nextToken.length());
							entityMatcher.region(wordMatcher.end(), nextToken.length());
						}
					}

					// The scanner swallows spaces, so add one if we have elements
					// remaining.
					if (scanner.hasNext()) {
						tokens.add(new Token(getCurrentLanguage(),
								" ", Token.Type.NON_WORD));				
					}
				}

				return tokens;
			}

			public ObjectCounter<String> getFormCounts() {
				return formCounts;
			}

			public void useFormCounts(ObjectCounter<String> counts) {
				formCounts = new ObjectCounter<String>(counts);
			}
		};
	}



	/** This keeps track of the times we've seen a particular form. */
	private ObjectCounter<String> formCounts = new ObjectCounter<String>();

	/**
	 * Creates a new Tokenizer that will assume it's processing text that
	 * might include tags as well as character data.
	 */
	public Tokenizer (String text, Language language) {
		// Set the default language for this chunk
		pushLanguage(language);

		// Convert the text to a StringBuffer so we can chop off parts of it
		// as we work through.
		input = StringUtil.replaceNumericEntities(text);

		// Prepare to keep track of words we've seen so far
	}

	/**
	 * Returns the TokenList created by operating on the Tokenizer's `text`
	 * field.
	 */
	public TokenList getTokens() {
		// This is the ultimate destination of the tokens
		TokenList tokens = new TokenList();

		// This tokenizer works in two stages:
		//  1. Divide the text based on obvious, non-word delimiters like
		//     tags and periods.
		//  2. Pass off the remaining text blocks to language-specific
		//     tokenizers. This allows us to parse things like beta-code Greek.
		while (input.length() > 0) {

			// If noTags is set to true, a couple of these patterns are
			// pointless to check; also, we need to handle tagless text a
			// little differently from text with tags.

			if (!noTags && matches(tagPattern)) {
				// if matches() has returned true, it has also split off
				// the matching text segment from input and put it into
				// the currentToken.

				// some tags signal a change of language

				Matcher linkStartMatcher =
					linkStartPattern.matcher(currentToken.toLowerCase());
				if (!noTags && linkStartMatcher.matches()) {
					tokens.add(new Token(getCurrentLanguage(),
							currentToken,
							Token.Type.LINK_START));
				}
				else if (currentToken.equalsIgnoreCase("</a>")) {
					tokens.add(new Token(getCurrentLanguage(),
							currentToken,
							Token.Type.LINK_END));
				}
				else {
					tokens.add(new Token(getCurrentLanguage(),
							currentToken,
							Token.Type.TAG));
				}

				currentToken = null;
			}
			else if (matches(entityPattern)) {
				tokens.add(new Token(getCurrentLanguage(),
						currentToken,
						Token.Type.ENTITY));
				currentToken = null;
			}
			else if (matches(delimiterPattern)) {
				tokens.add(new Token(getCurrentLanguage(),
						currentToken,
						Token.Type.NON_WORD));
				currentToken = null;
			}
			else if ((noTags && matches(taglessTextPattern))
					|| matches(textPattern)) {

				LanguageAdapter adapter = getCurrentLanguage().getAdapter();

				while (hasCurrentToken()) {
					Matcher entityMatcher =
						entityPattern.matcher(currentToken);

					Matcher nonWordMatcher =
						adapter.getNonWordPattern().matcher(currentToken);

					if (entityMatcher.lookingAt()) {
						String token = entityMatcher.group();

						tokens.add(new Token(getCurrentLanguage(),
								token,
								Token.Type.ENTITY));

						currentToken = currentToken.substring(entityMatcher.end());
					}
					else if (nonWordMatcher.lookingAt()) {
						String token = nonWordMatcher.group();

						tokens.add(new Token(getCurrentLanguage(),
								token,
								Token.Type.NON_WORD));

						currentToken = currentToken.substring(nonWordMatcher.end());
					}
					else {
						Matcher wordMatcher =
							adapter.getWordPattern().matcher(currentToken);

						if (wordMatcher.lookingAt()) {
							String tokenText = wordMatcher.group();

							// Update context information.
							int timesSeen = formCounts.count(tokenText);
							TokenContext context =
								new TokenContext(null, timesSeen);

							Token token = new Token(getCurrentLanguage(),
									tokenText, Token.Type.WORD, context);
							tokens.add(token);

							currentToken = currentToken.substring(wordMatcher.end());

						}
						else {
							// What would get to this point?
							tokens.add(new Token(getCurrentLanguage(),
									currentToken,
									Token.Type.NON_WORD));
							currentToken = null;
						}
					}
				}
			}
		}

		return tokens;
	}

	public void pushLanguage(Language language) {
		languageStack.push(language);
	}

	public void popLanguage() {
		languageStack.pop();
	}

	public Language getCurrentLanguage() {
		return languageStack.peek();
	}
	public void useFormCounts(ObjectCounter<String> counts) {
		for (String item : counts.objects()) {
			formCounts.put(item, counts.get(item).get());
		}
		//formCounts = new ObjectCounter<String>(counts);
	}

	public ObjectCounter<String> getFormCounts() {
		return formCounts;
	}

	public void clearFormCounts() {
		formCounts.clear();
	}

	/**
	 * This method returns true if the current token contains
	 * text.
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

	public boolean matches(Pattern pattern) {
		Matcher matcher = pattern.matcher(input);

		if (matcher.lookingAt()) {
			setCurrentToken(matcher.group(), matcher.end());

			return true;
		}
		return false;
	}

	public String getCurrentToken() {
		return currentToken;
	}

	/**
	 * This method is called by the tokenizer to advance the
	 * current token. It also strips off the new token from 
	 * the input buffer.
	 * If the previous token has not been ignored, linked, or passed
	 * to output, it is written to output and cleared. This
	 * means that to drop a token, you must explicitly call ignore().
	 */
	public void setCurrentToken(String s, int end) {
		// Make sure the current token is flushed to output
		currentToken = s;

		input.delete(0, end);
	}

	/**
	 * Returns the text with any unclosed tags closed and any unopened tags
	 * open. This is useful if we're working with a snippet that may not be
	 * well-formed.
	 */
	public String getPaddedText() {
		TokenList tokens = getTokens();
		LinkedList<String> needStartTags = new LinkedList<String>();
		LinkedList<String> needEndTags = new LinkedList<String>();

		for (Token token : tokens) {
			if (token.getType() == Token.Type.TAG && !token.isEmptyTag()) {
				String tokenText = token.getElementName();

				if (token.isEndTag()) {
					// An end tag could be an orphan tag, but it could also
					// close off a start tag we've already seen
					if (!needEndTags.isEmpty()) {
						String lastOpenTag = (String) needEndTags.getLast();
						if (lastOpenTag != null && lastOpenTag.equals(tokenText)) {
							needEndTags.removeLast();
						} else {
							needStartTags.addLast(tokenText);
						}
					} else {
						needStartTags.addLast(tokenText);
					}
				} else {
					needEndTags.addLast(tokenText);
				}
			}
		}

		// Now create the output string; add the start tokens
		StringBuffer output = new StringBuffer();
		output.append("<snippet>");
		while (!needStartTags.isEmpty()) {
			String startTag = (String) needStartTags.removeLast();

			output.append("<" + startTag + ">");
		}

		// Add the actual tokens
		for (int i = 0; i < tokens.size(); i++) {
			Token token = (Token) tokens.get(i);
			output.append(token.getDisplayText());
		}

		// ...and add the end tokens
		while (!needEndTags.isEmpty()) {
			String endTag = (String) needEndTags.removeLast();

			output.append("</" + endTag + ">");
		}

		output.append("</snippet>");

		return output.toString();
	}
}
