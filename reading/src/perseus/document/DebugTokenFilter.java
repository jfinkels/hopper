package perseus.document;

import perseus.util.*;

public class DebugTokenFilter extends TokenFilter {

    public void process(Token token) {
	StringBuffer output = new StringBuffer();
	
	output.append(token.getType())
	    .append(" ")
	    .append(token.getLanguageCode())
	    .append(" ")
	    .append(token.getOriginalText())
	    .append("\n");
	    
	token.setDisplayText(output.toString());
    }
}
