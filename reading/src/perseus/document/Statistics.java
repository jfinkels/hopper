package perseus.document;

import perseus.util.*;
import perseus.morph.*;
import java.util.*;

public class Statistics {

    public static Set getUniqueWords(List tokens) {
	Set uniqueWords = new HashSet();

	Iterator tokenIterator = tokens.iterator();
	while (tokenIterator.hasNext()) {
	    Token token = (Token) tokenIterator.next();
	    if (token.getType() == Token.Type.WORD) {
		uniqueWords.add(token.getOriginalText());
	    }
	}

	return uniqueWords;
    }

    public static ObjectCounter getTokenFrequencies(List tokens) {
	ObjectCounter counts = new ObjectCounter();
	
	Iterator tokenIterator = tokens.iterator();
	while (tokenIterator.hasNext()) {
	    Token token = (Token) tokenIterator.next();
	    if (token.getType() == Token.Type.WORD) {
		counts.count(token.getOriginalText());
	    }
	}

	return counts;
    }

}
