package perseus.language;

import gpl.pierrick.brihaye.aramorph.AraMorph;

import java.util.regex.Pattern;


/**
 * The language adapter for Arabic.
 * 
 * @see LanguageAdapter
 */
public class ArabicAdapter extends DefaultLanguageAdapter {
    
    private static final Pattern nonWordPattern =
	Pattern.compile("([^'|OWIAbptvjHx d*rzs$SDTZEg_fqklmnhwYyFNKaui~o`{}PJVG^=])");
    private static final Pattern wordPattern =
	Pattern.compile("(['|OWIAbptvjHx d*rzs$SDTZEg_fqklmnhwYyFNKaui~o`{}PJVG^=]+)");

    
    /**
     * Class constructor.
    */
    public ArabicAdapter () {
    }

    public Pattern getNonWordPattern() {
	return nonWordPattern;
    }

    public Pattern getWordPattern() {
	return wordPattern;
    }
 
    /**
     * Returns a normalized form of the given word that can be used to look up
     * the word in a database. In this case, we convert Salmone-style tranliteration
     * to Buckwalter and convert non-XML-friendly characters.
     *
     * @param s the string to be normalized
     * @return the string in its lookup-form
     */
     
    public String getLookupForm(String s) {
    
		s=ArabicAdapter.salmone2Buckwalter(s);
		s=ArabicAdapter.toXMLFriendly(s.replaceAll("~", ""));
		return AraMorph.romanizeWord(s);
    
    }
    
    /**
     * The Salmone dictionary is transcribed with in a different scheme than
     * Buckwalter; convert.
     *
     */
    
    public static String salmone2Buckwalter(String s) {
	
		return s.replaceAll("A\\^", ">")
		.replaceAll("y\\^", "}")
		.replaceAll("w\\^", "&")
		.replaceAll("A=", "|")
		.replaceAll("A_", "<i");
	
	}
	
	/**
	 * Buckwalter transliteration uses &, < and > characters; replace these
	 *
	 */
	 
	public static String toXMLFriendly(String s) {
	
		return s.replaceAll("&", "W").replaceAll("<", "I").replaceAll(">", "O");
	}

	
	public static String devowelize(String s) {
		
		return s.replaceAll("[~FNKauio]", "");
		
	}
	
    /**
     * Arabic words are ALL case sensitive
     *
     */
    public boolean matchCase() {
    	return true;
    }


}
