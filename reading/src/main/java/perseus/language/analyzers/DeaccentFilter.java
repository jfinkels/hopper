package perseus.language.analyzers;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import perseus.util.StringUtil;

/**
 * The DeaccentFilter removes all accents from tokens in the TokenStream
 */
public class DeaccentFilter extends TokenFilter {

	/**
	 * Class Constructor
	 */
	public DeaccentFilter(TokenStream in) {
		super(in);
	}

	/**
	 * Retrieves the next token in the TokenStream
	 * 
	 * @throws java.io.IOException
	 * @return the next token in the TokenStream
	 */
	public Token next() throws java.io.IOException {
		Token t = input.next();

		if (t == null)
			return null;

		String s = t.termText();
		s = StringUtil.deaccent(s);
		Token lemmaToken =
			new Token(s, t.startOffset(), t.endOffset(), t.type());
		return lemmaToken;
	}
}
