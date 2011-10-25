package perseus.util;

import java.util.*;
import java.util.regex.*;

/**
 * This class contains useful methods for working with various language
 * encodings
 */
public class StringUtil {

    public static final Pattern CAPITAL_A =
	Pattern.compile("[\\u00c0\\u00c1\\u00c2\\u00c3\\u00c4\\u0100\\u0101]");
    public static final Pattern CAPITAL_E = 
	Pattern.compile("[\\u00c8\\u00c9\\u00ca\\u00cb\\u0112\\u0114]");
    public static final Pattern CAPITAL_I =
	Pattern.compile("[\\u00cc\\u00cd\\u00ce\\u00cf\\u012a\\u012c]");
    public static final Pattern CAPITAL_O =
	Pattern.compile("[\\u00d2\\u00d3\\u00d4\\u00d5\\u00d6\\u014c\\u014e]");
    public static final Pattern CAPITAL_U =
	Pattern.compile("[\\u00d9\\u00da\\u00db\\u00dc\\u016a\\u016c]");

    public static final Pattern CAPITAL_AELIG = 
	Pattern.compile("[\\u00c6\\u01e2]");
    public static final Pattern CAPITAL_OELIG = 
	Pattern.compile("\\u0152");

    public static final Pattern LOWERCASE_A = 
	Pattern.compile("[\\u00e0\\u00e1\\u00e2\\u00e3\\u00e4\\u0101\\u0103]");
    public static final Pattern LOWERCASE_E =
	Pattern.compile("[\\u00e8\\u00e9\\u00ea\\u00eb\\u0113\\u0115]");
    public static final Pattern LOWERCASE_I =
	Pattern.compile("[\\u00ec\\u00ed\\u00ee\\u00ef\\u012b\\u012d]");
    public static final Pattern LOWERCASE_O =
	Pattern.compile("[\\u00f2\\u00f3\\u00f4\\u00f5\\u00f6\\u014d\\u014f]");
    public static final Pattern LOWERCASE_U =
	Pattern.compile("[\\u00f9\\u00fa\\u00fb\\u00fc\\u016b\\u016d]");

    public static final Pattern LOWERCASE_AELIG =
	Pattern.compile("[\\u00e6\\u01e3]");
    public static final Pattern LOWERCASE_OELIG =
	Pattern.compile("\\u0153");

    // Patterns for Capitalization
    public static final Pattern MC_NAME_PATTERN = 
	Pattern.compile("^mc(.)");
    public static final Pattern MAC_NAME_PATTERN = 
	Pattern.compile("^mac(.)");
    public static final Pattern O_NAME_PATTERN = 
	Pattern.compile("^o'(.)");
    public static final Pattern DEFAULT_NAME_PATTERN = 
	Pattern.compile("^(.)");

    /** This pattern matches numeric entities */
    public static final Pattern entityPattern = Pattern.compile("&#(\\d+);");

    
    /** 
     * Return the String specified, joined to the end of the Collection, serialized
     * as a String
     * @param c the collection to join the string to
     * @param s The string to join the collection to
     * @return the collection with s at its end, serialized as a String
    */
    public static String join (Collection c, String s) {
	Object tokens[] = c.toArray();
	return join(tokens, s);
    }

    public static String joinNonnulls(Collection c, String s) {
	Object tokens[] = c.toArray();
	return join(tokens, s);
    }

    /**
     * Append the given string to the end of the tokens list
     *
     * @param tokens A list of tokens
     * @param s The string to add to the token list
     * @return the tokens list with, s at its end, serialized as a String
    */
    public static String join(Object[] tokens, String s) {
	StringBuffer output = new StringBuffer();
	for (int i=0;i<tokens.length;i++) {
	    output.append(tokens[i]);
	    if (i < tokens.length - 1) {
		output.append(s);
	    }
	}
	return output.toString();
    }

    public static String joinNonnulls(Object[] tokens, String s) {
	List<Object> nonnulls = new ArrayList<Object>();
	for (int i = 0; i < tokens.length; i++) {
	    if (tokens[i] != null) nonnulls.add(tokens[i]);
	}
	return join(nonnulls, s);
    }

    /**
     * Use this method for escaping strings so that
     * they can be used in a SQL statement.
     *
     * @param s the string to be escaped
     * @return the escaped string
    */
    public static String sqlEscape(String s) {
	if (s == null) { return null; }

	s = s.replaceAll("'", "''");
	s = s.replaceAll("\\\\", "\\\\\\\\");

	return s;
    }

    /**
     * Use this method for preparing a string to be inserted into
     * an XML document.  This is accomplished by
     * converting all ampersands and less than substrings to
     * the appropriate entity.
     *
     * @param s the string to escape
     * @return the escaped string, ready to be included
     * in an XML document
    */
    public static String xmlEscape(String s) {
	if (s == null) { return null; }

        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        return s;
    }

    /**
     * Determine how similar two strings are to each other.
     *
     * @param a the first string
     * @param b the second string to compare 
     * @return a floating point number indicating how similar the strings are to each other
    */
    public static float similarityScore(String a, String b) {
	char[] rightLetters = a.toCharArray();
	char[] downLetters = b.toCharArray();

	float chart[][] = new float[downLetters.length + 1][rightLetters.length + 1];
	chart[0][0] = 0;
	
	int i;
	int j;
	
	float VARIATION = 0.05f;

	// Initialize the first row
	for (j = 1; j <= rightLetters.length; j++) {
	    chart[0][j] = chart[0][j-1] - VARIATION;
	}

	for (i = 1; i <= downLetters.length; i++) {
	    chart[i][0] = chart[i-1][0] - VARIATION;
	    for (j = 1; j <= rightLetters.length; j++) {
		// Calcluate three values, one for each adjacent
		// square that has been filled in.
		float vertical = chart[i-1][j] - VARIATION;
		float diagonal = chart[i-1][j-1];
		if (downLetters[i-1] == rightLetters[j-1]) {
		    // The diagonal score will be higher if 
		    // the two letters are the same.
		    diagonal += 1;
		}
		float horizontal = chart[i][j-1] - VARIATION;

		// Choose the highest of the three
		if (vertical > diagonal &&
		    vertical > horizontal) {
		    chart[i][j] = vertical;
		}
		else if (horizontal > diagonal &&
		       horizontal > vertical) {
		    chart[i][j] = horizontal;
		}
		else {
		    chart[i][j] = diagonal;
		}   

	    }
	}

	return chart[downLetters.length][rightLetters.length];
    }

    /** 
     * Returns the length of the longest common substring of the
     * two strings, starting at the beginning 
     *
     * @param a The first string
     * @param b The second string
     * @return the length of the longest common substring of the two strings
     */
    public static int initialSimilarity(String a, String b) {
	char[] left = a.toCharArray();
	char[] right = b.toCharArray();

	int i = 0;
	for (; i < left.length && i < right.length; i++) {
	    if (left[i] != right[i]) {
		return i;
	    }
	}
	return i;
    }

    /**
     * Take the accents out of the given string using the static final constants
     * of this class
     *
     * @param s The String to deaccent
     * @return the String s without accents
     */
    public static String deaccent(String s) {
	if (s == null) { return null; }

	s = applyPattern(CAPITAL_A, s, "A");
	s = applyPattern(CAPITAL_E, s, "E");
	s = applyPattern(CAPITAL_I, s, "I");
	s = applyPattern(CAPITAL_O, s, "O");
	s = applyPattern(CAPITAL_U, s, "U");

	s = applyPattern(CAPITAL_AELIG, s, "AE");
	s = applyPattern(CAPITAL_OELIG, s, "OE");
	
	s = applyPattern(LOWERCASE_A, s, "a");
	s = applyPattern(LOWERCASE_E, s, "e");
	s = applyPattern(LOWERCASE_I, s, "i");
	s = applyPattern(LOWERCASE_O, s, "o");
	s = applyPattern(LOWERCASE_U, s, "u");

	s = applyPattern(LOWERCASE_AELIG, s, "ae");
	s = applyPattern(LOWERCASE_OELIG, s, "oe");

	return s;
    }

    /**
     * Replace occurances of the Pattern p in String s with the replacement String.
     * 
     * @param p The pattern to match
     * @param s The string to look in
     * @param replacement The string to replace s with
     * @return The string s with all occurrances of p replaced by the replacement string
    */
    public static String applyPattern(Pattern p, String s, String replacement) {
	Matcher m = p.matcher(s);
	if (m.find()) {
	    s = m.replaceAll(replacement);
	}
	return s;
    }

    /**
     * Return the string s, just with no macrons
     *
     * @param s the String drop macrons from
     * @return the string s with all macrons removed
    */
    public static String dropMacrons(String s) {
	if (s == null) return null;

	char[] chars = s.toCharArray();
	StringBuffer output = new StringBuffer();
	for (int i=0;i<chars.length;i++) {
	    char ch = chars[i];
	    if (ch == '\u0101') {
		output.append('a');
	    }
	    else if (ch == '\u0100') {
                output.append('A');
            }
	    else if (ch == '\u0113') {
                output.append('e');
            }
	    else if (ch == '\u0112') {
                output.append('E');
            }
	    else if (ch == '\u012a') {
                output.append('I');
            }
	    else if (ch == '\u012b') {
                output.append('i');
            }
	    else if (ch == '\u014c') {
                output.append('O');
            }
	    else if (ch == '\u014d') {
                output.append('o');
            }
	    else if (ch == '\u016b') {
                output.append('u');
            }
	    else if (ch == '\u016a') {
                output.append('U');
            }
	    else {
		output.append(ch);
	    }

	}
	    
	return output.toString();
    }
    
    /** This method returns the string it is passed, unless
     * that string is null, in which case it returns an
     * empty string. This prevents stringbuffers from appending
     * the word "null" when they are passed null strings 
     * 
     * @param s the string passed in
     * @return the string s, unless null and then return ""
    */
    public static String noNull(String s) {
	if (s != null) {
	    return s;
	}
	return "";
    }

    /** 
     * This method properly capitalizes names, including McKinley and
     * O'Malley 
     *
     * @param s The name to be capitalized
     * @return the name s, with proper capitalization
    */
    public static String capitalizeName(String s) {
	if (s == null) { return null; }

	StringBuffer output = new StringBuffer();

	Matcher m = MC_NAME_PATTERN.matcher(s);
	if (m.find()) {
	    m.appendReplacement(output, "Mc" + m.group(1).toUpperCase());
	    m.appendTail(output);
	    return output.toString();
	}
	
	m = MAC_NAME_PATTERN.matcher(s);
	if (m.find()) {
	    m.appendReplacement(output, "Mac" + m.group(1).toUpperCase());
	    m.appendTail(output);
	    return output.toString();
	}
	
	m = O_NAME_PATTERN.matcher(s);
	if (m.find()) {
	    m.appendReplacement(output, "O'" + m.group(1).toUpperCase());
	    m.appendTail(output);
	    return output.toString();
	}
	
	m = DEFAULT_NAME_PATTERN.matcher(s);
	if (m.find()) {
	    m.appendReplacement(output, m.group(1).toUpperCase());
	    m.appendTail(output);
	    return output.toString();
	}
	
	// One of those should have matched, but just in case...
	return s;
    }

    /** 
     * This method turns all numeric entities (&#1e7;) into their unicode
     * equivalents. This is useful if poorly behaved XSL transformers eat
     * all your nice UTF-8 characters.
     *
     * @param input the string containing numeric entities to convert
     * @return a StringBuffer with all numeric entities replaced
    */
    public static StringBuffer replaceNumericEntities(String input) {
        StringBuffer output = new StringBuffer();

        Matcher m = entityPattern.matcher(input);
        while (m.find()) {
            m.appendReplacement(output, makeChar(m.group()));
        }
        m.appendTail(output);

        return output;
    }

    /**
     * This is a helper method for removeNumericEntities. It
     * returns the character equivalent of a numeric entity, such 
     * as &#1e7;
     *
     * @param entity The entity to return the char equiv for
     * @return the character equivalent
     */
    public static String makeChar(String entity) {
        char charValue = (char) Integer.parseInt(entity.substring(2,entity.length()-1));
        return (new Character(charValue)).toString();
    }

    /**
     * A method to convert unicode entities into numeric entities
     *
     * @param input the input string with UTF-8 characters
     * @return String
    */
    public static String insertNumericEntities(String input) {
	if (input == null) { return null; }

	char[] characters = input.toCharArray();
	StringBuffer output = new StringBuffer();
	for (int i=0;i<characters.length;i++) {
	    char c = characters[i];
	    int numericValue = (int) c;
	    if (numericValue > 127) {
		output.append("&#" + numericValue + ";");
	    }
	    else {
		output.append(c);
	    }
	}

	return output.toString();
    }

    /**
     * Returns the simple "name" of the given class. For instance,
     * getClassName(Query.class) will return "Query", as opposed to the
     * Class.getName() function, which would return "perseus.document.Query".
     */
    public static String getClassName(Class cl) {
	String[] nameTokens = cl.getName().split("\\.");
	String shortName = nameTokens[nameTokens.length-1];

	// Check for inner classes, like 'Chunk$OpenChunk"
	if (shortName.indexOf("$") != -1) {
	    return shortName.split("\\$")[1];
	}
	return shortName;
    }
}
