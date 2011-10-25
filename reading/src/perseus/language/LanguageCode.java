package perseus.language;

import java.util.HashSet;
import java.util.Set;


/**
 * This class was once responsible for handling all language-related matters
 * within the hopper. Said responsibilities have since been given to the
 * Language class, and this class now serves ony as a wrapper.
*/
public class LanguageCode {

    public static final String ENGLISH = Language.ENGLISH.getCode();
    public static final String GREEK = Language.GREEK.getCode();
    public static final String LATIN = Language.LATIN.getCode();
    public static final String OLD_NORSE = Language.OLD_NORSE.getCode();
    public static final String OLD_ENGLISH = Language.OLD_ENGLISH.getCode();

    /**
     * Retrieve the display name for the given language code.   
     * 
     * @param code one of the static constants of this class, GREEK, LATIN, ENGLISH, etc
     * @return the display name for the language code
    */
    public static String getDisplayName(String code) {
        return Language.forCode(code).getName();
    }

    /**
     * Returns a Set of Strings representing codes for all the languages
     * currently recognized by the system.
     */
    public static Set<String> getKnownLanguages() {
        Set<Language> languages = Language.getAll();
        Set<String> langCodes = new HashSet<String>();
        for (Language language : languages) {
            langCodes.add(language.getCanonicalAbbreviation());
        }
        return langCodes;
    }

    /**
     * Returns an XML representation of the currently known languages in the
     * system, of the following form. (The first abbreviation listed is the
     * canonical one.)
     * <languages>
     *	<language name="Greek" abbrev="greek" morph="yes">
     *	<language name="English" abbrev="en">
     *	(and so on)
     * </languages>
     */
    public static String toXML() {
	StringBuffer output = new StringBuffer();
	output.append("<languages>\n");
	for (String code : getKnownLanguages()) {
	    output.append("<language name=\"")
		.append(getDisplayName(code)).append("\"");
	    output.append(" abbrev=\"").append(code).append("\"");
	    if (hasMorphData(code)) {
		output.append(" morph=\"yes\"");
	    }
	    output.append("/>\n");
	}

	output.append("</languages>");
	return output.toString();
    }

    /** 
     * Get a language ID for the given language code.  The database 
     * contains a numeric id for each language. 
     *
     * @param languageCode 
     * @return the corresponding language id
    */
    public static int getLanguageID(String languageCode) {
	return Language.forCode(languageCode).getId();
    }

    /** 
     * Given a numeric ID, return a language Code for the corresponding
     * language. Note that getLanguageCode(getLanguageID(x)) does NOT
     * necessarily yield x, because getLanguageCode returns the canonical
     * abbreviation for most languages, for compatibility reasons (see
     * above).
     *
     * @param languageID the numeric code for which to retrieve the language
     * @return the string constant language code for the numeric language id
    */
    public static String getLanguageCode(int languageID) {
        return Language.forId(languageID).getAbbreviation();
    }

    /**
     * A way to see if the given language has morphological data
     * by using a language id rather than a language code
     *
     * @param languageID a number that maps to a language code
     * @return true or false depending on if morph data is present
    */
    public static boolean hasMorphData(int languageID) {
	return Language.forId(languageID).getHasMorphData();
    }
  
    /**
     * Return true if the given language has morphological data
     *
     * @param languageCode one of the static constants in this class
     * @return true if there is morph data. false if there is no morph 
     * data
    */
    public static boolean hasMorphData(String languageCode) {
        return Language.forCode(languageCode).getHasMorphData();
    }

    public static boolean isKnownLanguage(String languageCode) {
        return (Language.forCode(languageCode) != Language.UNKNOWN);
    }

    /**
     * Returns the "default" abbreviation for the language represented
     * by the given abbreviation. For instance, getCanonicalAbbreviation("eng")
     * returns "en".
     */
    public static String getCanonicalAbbreviation(String otherAbbrev) {
        return Language.forCode(otherAbbrev).getCanonicalAbbreviation();
    }
}
