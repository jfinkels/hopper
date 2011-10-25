package perseus.language.analyzers.greek;

import java.io.Reader;

import org.apache.lucene.analysis.CharTokenizer;

/** This class tokenizes Greek text encoded using the Perseus subset of BetaCode:
    letters: a-z, A-Z
    breathings: )(
    accents: /\=
    capitals: *
    iota subscript: |
    apostrophe: '
    diaeresis: +
    
*/

public class BetaCodeTokenizer extends CharTokenizer {
    /** Construct a new LetterTokenizer. */
    public BetaCodeTokenizer(Reader in) {
	super(in);
    }

    /** Collects only characters which satisfy
     * {@link Character#isLetter(char)}.*/
    protected boolean isTokenChar(char c) {
	if (c >= 'a' && c <= 'z') { return true; }
	if (c >= 'A' && c <= 'Z') { return true; }
	if (c >= '0' && c <= '9') { return true; }
	if (c == '_') { return true; }
	if (c == ')') { return true; }
	if (c == '(') { return true; }
	if (c == '=') { return true; }
	if (c == '*') { return true; }
	if (c == '|') { return true; }
	if (c == '\'') { return true; }
	if (c == '+') { return true; }
	if (c == '^') { return true; }
	if (c == '/') { return true; }
	if (c == '\\') { return true; }
	return false;
    }
}
