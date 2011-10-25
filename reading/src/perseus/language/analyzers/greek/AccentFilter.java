package perseus.language.analyzers.greek;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import perseus.language.GreekAdapter;
import perseus.language.LanguageAdapter;

public class AccentFilter extends TokenFilter {
    
    private static final LanguageAdapter adapter = new GreekAdapter();

    public AccentFilter(TokenStream in) {
	super(in);
    }
    
    public Token next() throws java.io.IOException {
        Token t = input.next();
        
        if (t == null) return null;
        
        String oldText = t.termText();
        String newText = adapter.getLookupForm(oldText);

        /*
	boolean changed = false;

	String text = t.termText();
	Matcher m = graveAccentPattern.matcher(text);
	if (m.find()) {
	    text = m.replaceAll("/");
	    changed = true;
	}

	m = secondaryAccentPattern.matcher(text);
	if (m.find()) {
	    text = m.replaceFirst(m.group());
	    changed = true;
	}
	*/

	if (!oldText.equals(newText)) {
	    t = new Token(newText,
			  t.startOffset(),
			  t.endOffset(),
			  t.type());
	}
	
        return t;
    }
}
