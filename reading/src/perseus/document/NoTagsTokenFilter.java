package perseus.document;

import perseus.util.*;

public class NoTagsTokenFilter extends TokenFilter {

    public void process(Token token) {
	if (token.getType() == Token.Type.TAG) {
	    if(token.getOriginalText().equals("<p>")){
		token.setDisplayText("<p>");
	    }else if(token.getOriginalText().equals("</p>")){
		token.setDisplayText("</p>");
	    }else{
		token.setDisplayText("");
	    }
        }
    }
}
