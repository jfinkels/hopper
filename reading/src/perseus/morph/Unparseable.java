package perseus.morph;

import perseus.document.*;
import perseus.language.LanguageCode;
import perseus.util.*;
import java.util.*;

/**
 * This class contains methods for handling word tokens that 
 * do not parse back to lemmas
*/
public class Unparseable {
    
    /**
     * Class constructor
    */
    public Unparseable(){

    }

    /**
     * Get all words which do not parse in a given chunk
     *
     * @param chunk the chunk
     * @return a list of words in the chunk that are unparseable
     */
    public static TokenList getUnparseable(Chunk chunk){

	String languageCode = chunk.getMetadata().get(Metadata.LANGUAGE_KEY);
	TokenList textTokens = chunk.getTokens();
	return getUnparseable(textTokens, languageCode);	

    }

    /**
     * Get all words which do not parse in a given chunk
     *
     * @param textTokens {@link perseus.document.TokenList}
     * @param languageCode {@link perseus.language.LanguageCode}
     * @return a list of words in the chunk that are unparseable
     */
    public static TokenList getUnparseable(TokenList textTokens, String languageCode){
	Iterator tokenIterator = textTokens.iterator();
	TokenList unparseable = new TokenList();
	while(tokenIterator.hasNext()){
	    Token token = (Token) tokenIterator.next();
	    if (token.getType() == Token.Type.WORD && 
		LanguageCode.hasMorphData(token.getLanguageCode())){

		String word = token.getOriginalText();
		Map lemmaParses = Parse.getParses(word, token.getLanguageCode());

		if (lemmaParses == null || lemmaParses.keySet().size() == 0){
		    //try lowercase
		    String lowercaseWord = word.toLowerCase();
		    lemmaParses = Parse.getParses(lowercaseWord, languageCode);
		    if(lemmaParses == null || lemmaParses.keySet().size() == 0){
			//definately nothing
			unparseable.add(token);
		    }
		}		
	    }
	}
	return unparseable;
    }
}
