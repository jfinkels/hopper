package perseus.morph;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import static perseus.morph.MorphCode.*;

/**
 * Class that maps between the morphological code in the database
 * and the representation of the morphological code in the 
 * Java classes
*/
public class ArabicCodeGenerator implements MorphCodeGenerator {

    private static Logger logger = Logger.getLogger(ArabicCodeGenerator.class);
  
public static final String FUNCTIONAL = "func";
public static final String CONJ = "conj+";
public static final String EMPHATIC_PARTICLE = "emphatic+";
public static final String PREFIX = "prefix";
public static final String PREP = "prep+";
public static final String RESULT_CLAUSE_PARTICLE = "resultative+";
public static final String INTERROGATIVE_PARTICLE = "interrogative+";
public static final String NEG_PARTICLE = "neg+";

public static final String OBJ="object";

public static final String O1P="+obj(1st/plur)";
public static final String O1S="+obj(1st/sing)";
public static final String O2D="+obj(2nd/dual)";
public static final String O2FP="+obj(2nd/fem/plur)";
public static final String O2FS="+obj(2nd/fem/sing)";
public static final String O2MP="+obj(2nd/masc/plur)";
public static final String O2MS="+obj(2nd/masc/sing)";
public static final String O3D="+obj(3rd/dual)";
public static final String O3FP="+obj(3rd/fem/plur)";
public static final String O3FS="+obj(3rd/fem/sing)";
public static final String O3MP="+obj(3rd/masc/plur)";
public static final String O3MS="+obj(3rd/masc/sing)";


public static final String DEFINITE = "definite";
public static final String INDEFINITE = "indefinite";

public static final String POSSESSIVE = "possessive";
public static final String POSS_1P = "poss(1st/plur)";
public static final String POSS_1S = "poss(1st/sing)";
public static final String POSS_2D = "poss(2nd/dual)";
public static final String POSS_2FP = "poss(2nd/fem/plur)";
public static final String POSS_2FS = "poss(2nd/fem/sing)";
public static final String POSS_2MP = "poss(2nd/masc/plur)"; 
public static final String POSS_2MS = "poss(2nd/masc/sing)";
public static final String POSS_3D = "poss(3rd/dual)";
public static final String POSS_3FP = "poss(3rd/fem/plur)"; 
public static final String POSS_3FS = "poss(3rd/fem/sing)";
public static final String POSS_3MP = "poss(3rd/masc/plur)";
public static final String POSS_3MS = "poss(3rd/masc/sing)";


    /**
     * Return the morphological code, as it appears in the database
     * given a map of features.
     *
     * @param features a map of features
     * @return a String representing the morph code as it
     * appears in the database
    */
    public String getCode(Map<String, String> features) {
	StringBuffer code = new StringBuffer();

	// part of speech
	if (! features.containsKey(POSITIVE)) {
	    code.append("-");
	}
	else {
	    String value = features.get(POSITIVE);
	    if (value.equals(NOUN)) {
		code.append("n");
	    }
	    else if (value.equals(VERB)) {
		code.append("v");
	    }
	    else if (value.equals(PARTICIPLE)) {
		code.append("t");
	    }
	    else if (value.equals(ADJECTIVE)) {
		code.append("a");
	    }
	    else if (value.equals(ADVERB)) {
		code.append("d");
	    }
	    else if (value.equals(ADVERBIAL)) {
		code.append("d");
	    }
	    else if (value.equals(CONJUNCTION)) {
		code.append("c");
	    }
	    else if (value.equals(PARTICLE)) {
		code.append("g");
	    }
	    else if (value.equals(PREPOSITION)) {
		code.append("r");
	    }
	    else if (value.equals(PRONOUN)) {
		code.append("p");
	    }
	    else if (value.equals(NUMERAL)) {
		code.append("m");
	    }
	    else if (value.equals(INTERJECTION)) {
		code.append("i");
	    }
	    else if (value.equals(EXCLAMATION)) {
		code.append("e");
	    }
	    else if (value.equals(FUNCTIONAL)) {
		code.append("f");
	    }
	    else {
		logger.warn("Unrecognized pos code: " + value);
		code.append("!");
	    }
	}

	// person
	if (! features.containsKey(PERSON)) {
	    code.append("-");
	}
	else {
	    String value = features.get(PERSON);
	    if (value.equals(FIRST_PERSON)) {
		code.append("1");
	    }
	    else if (value.equals(SECOND_PERSON)) {
		code.append("2");
	    }
	    else if (value.equals(THIRD_PERSON)) {
		code.append("3");
	    }
	    else {
		logger.warn("Unrecognized person code: " + value);
		code.append("!");
	    }
	}

	// number
	if (! features.containsKey(NUMBER)) {
	    code.append("-");
	}
	else {
	    String value = features.get(NUMBER);
	    if (value.equals(SINGULAR)) {
		code.append("s");
	    }
	    else if (value.equals(PLURAL)) {
		code.append("p");
	    }
	    else if (value.equals(DUAL)) {
		code.append("d");
	    }
	    else {
		logger.warn("Unrecognized number code: " + value);
		code.append("!");
	    }
	}

	// tense
	if (! features.containsKey(TENSE)) {
	    code.append("-");
	}
	else {
	    String value = features.get(TENSE);
	    if (value.equals(PRESENT)) {
		code.append("p");
	    }
	    else if (value.equals(IMPERFECT)) {
		code.append("i");
	    }
	    else if (value.equals(PERFECT)) {
		code.append("r");
	    }
	    else if (value.equals(PLUPERFECT)) {
		code.append("l");
	    }
	    else if (value.equals(FUTURE_PERFECT)) {
		code.append("t");
	    }
	    else if (value.equals(FUTURE)) {
		code.append("f");
	    }
	    else {
		logger.warn("Unrecognized tense code: " + value);
		code.append("!");
	    }
	}

	// mood
	if (! features.containsKey(MOOD)) {
	    code.append("-");
	}
	else {
	    String value = features.get(MOOD);
	    if (value.equals(INDICATIVE)) {
		code.append("i");
	    }
	    else if (value.equals(SUBJUNCTIVE)) {
		code.append("s");
	    }
	    else if (value.equals(INFINITIVE)) {
		code.append("n");
	    }
	    else if (value.equals(IMPERATIVE)) {
		code.append("m");
	    }
	    else if (value.equals(PARTICIPLE)) {
		code.append("p");
	    }
	    else if (value.equals(GERUNDIVE)) {
		code.append("g");
	    }
	    else if (value.equals(SUPINE)) {
		code.append("u");
	    }
	    else {
		logger.warn("Unrecognized mood code: " + value);
		code.append("!");
	    }
	}

	// voice
	if (! features.containsKey(VOICE)) {
	    code.append("-");
	}
	else {
	    String value = features.get(VOICE);
	    if (value.equals(ACTIVE)) {
		code.append("a");
	    }
	    else if (value.equals(PASSIVE)) {
		code.append("p");
	    }
	    else if (value.equals(DEPONENT)) {
		code.append("d");
	    }
	    else if (value.equals(MEDIO_PASSIVE)) {
		code.append("e");
	    }
	    else {
		logger.warn("Unrecognized voice code: " + value);
		code.append("!");
	    }
	}

	// gender
	if (! features.containsKey(GENDER)) {
	    code.append("-");
	}
	else {
	    String value = features.get(GENDER);
	    if (value.equals(MASCULINE)) {
		code.append("m");
	    }
	    else if (value.equals(FEMININE)) {
		code.append("f");
	    }
	    else if (value.equals(NEUTER)) {
		code.append("n");
	    }
	    else {
		logger.warn("Unrecognized gender code: " + value);
		code.append("!");
	    }
	}

	// case
	if (! features.containsKey(CASE)) {
	    code.append("-");
	}
	else {
	    String value = features.get(CASE);
	    if (value.equals(NOMINATIVE)) {
		code.append("n");
	    }
	    else if (value.equals(GENITIVE)) {
		code.append("g");
	    }
	    else if (value.equals(DATIVE)) {
		code.append("d");
	    }
	    else if (value.equals(ACCUSATIVE)) {
		code.append("a");
	    }
	    else if (value.equals(ABLATIVE)) {
		code.append("b");
	    }
	    else if (value.equals(VOCATIVE)) {
		code.append("v");
	    }
	    else if (value.equals(LOCATIVE)) {
		code.append("l");
	    }
	    else if (value.equals(INSTRUMENTAL)) {
		code.append("i");
	    }
	    else {
		logger.warn("Unrecognized case code: " + value);
		code.append("!");
	    }
	}

	// degree
	if (! features.containsKey(DEGREE)) {
	    code.append("-");
	}
	else {
	    String value = features.get(DEGREE);
	    if (value.equals(COMPARATIVE)) {
		code.append("c");
	    }
	    else if (value.equals(SUPERLATIVE)) {
		code.append("s");
	    }
	    else {
		logger.warn("Unrecognized degree code: " + value);
		code.append("!");
	    }
	}
	
	// possessive
	if (! features.containsKey(POSSESSIVE)) {
	    code.append("-");
	}
	else {
	    String value = features.get(POSSESSIVE);
	    if (value.equals(POSSESSIVE)) {
		code.append("p");
	    }
	    else if (value.equals(POSS_1P)) {
		code.append("a");
	    }
	    else if (value.equals(POSS_1S)) {
		code.append("b");
	    }
	    else if (value.equals(POSS_2D)) {
		code.append("c");
	    }
	    else if (value.equals(POSS_2FP)) {
		code.append("d");
	    }
	    else if (value.equals(POSS_2FS)) {
		code.append("e");
	    }
	    else if (value.equals(POSS_2MP)) {
		code.append("f");
	    }
	    else if (value.equals(POSS_2MS)) {
		code.append("g");
	    }
	    else if (value.equals(POSS_3D)) {
		code.append("h");
	    }
	    else if (value.equals(POSS_3FP)) {
		code.append("i");
	    }
	    else if (value.equals(POSS_3FS)) {
		code.append("j");
	    }
	    else if (value.equals(POSS_3MP)) {
		code.append("k");
	    }
	    else if (value.equals(POSS_3MS)) {
		code.append("m");
	    }
	    
	    else {
		logger.warn("Unrecognized possessive code: " + value);
		code.append("!");
	    }
	}
	

	// definite
	if (! features.containsKey(DEFINITE)) {
	    code.append("-");
	}
	else {
	    String value = features.get(DEFINITE);
	    if (value.equals(DEFINITE)) {
		code.append("d");
	    }
	    else if (value.equals(INDEFINITE)) {
		code.append("i");
	    }
	    else {
		logger.warn("Unrecognized definite code: " + value);
		code.append("!");
	    }
	}

	
	// object person/number/gender

	if (! features.containsKey(OBJ)) {
	    code.append("-");
	}
	else {
	    String value = features.get(OBJ);
	    if (value.equals(O1P)) {
		code.append("a");
	    }
	    else if (value.equals(O1S)) {
		code.append("b");
	    }
	    else if (value.equals(O2D)) {
		code.append("c");
	    }
	    else if (value.equals(O2FP)) {
		code.append("d");
	    }
	    else if (value.equals(O2FS)) {
		code.append("e");
	    }
	    else if (value.equals(O2MP)) {
		code.append("f");
	    }
	    else if (value.equals(O2MS)) {
		code.append("g");
	    }
	    else if (value.equals(O3D)) {
		code.append("h");
	    }
	    else if (value.equals(O3FP)) {
		code.append("i");
	    }
	    else if (value.equals(O3FS)) {
		code.append("j");
	    }
	    else if (value.equals(O3MP)) {
		code.append("k");
	    }
	    else if (value.equals(O3MS)) {
		code.append("l");
	    }

	    else {
		logger.warn("Unrecognized object person code: " + value);
		code.append("!");
	    }
	}

	// prefix
	if (! features.containsKey(PREFIX)) {
	    code.append("-");
	}
	else {
	    String value = features.get(PREFIX);
	    if (value.equals(CONJ)) {
		code.append("c");
	    }
	    else if (value.equals(EMPHATIC_PARTICLE)) {
		code.append("e");
	    }
	    else if (value.equals(PREP)) {
		code.append("p");
	    }
	    else if (value.equals(RESULT_CLAUSE_PARTICLE)) {
		code.append("r");
	    }
	    else if (value.equals(INTERROGATIVE_PARTICLE)) {
		code.append("i");
	    }
	    else if (value.equals(NEG_PARTICLE)) {
		code.append("n");
	    }
	    else {
		logger.warn("Unrecognized prefix code: " + value);
		code.append("!");
	    }
	}
	
	
	return code.toString();
    }
    
    
    /**
     * Given a string that represents a morphological code in the database
     * return a map representation of the morphological code.
     *
     * @param code the morph code
     * @return a map of features
     */
    public Map<String, String> getFeatures(String code) {
    
    if (code == null) {
    	return new HashMap<String, String>();
    }
    
	char[] chars = code.toCharArray();
	Map<String,String> features = new HashMap<String,String>();

	char pos = chars[0];
	if (pos == 'n') {
	    features.put(PART_OF_SPEECH, NOUN);
	}
	else if (pos == 'v') {
	    features.put(PART_OF_SPEECH, VERB);
	}
	else if (pos == 't') {
	    features.put(PART_OF_SPEECH, PARTICIPLE);
	}
	else if (pos == 'a') {
	    features.put(PART_OF_SPEECH, ADJECTIVE);
	}
	else if (pos == 'm') {
	    features.put(PART_OF_SPEECH, PARTICLE);
	}
	else if (pos == 'd') {
	    features.put(PART_OF_SPEECH, ADVERB);
	}
	else if (pos == 'c') {
	    features.put(PART_OF_SPEECH, CONJUNCTION);
	}
	else if (pos == 'r') {
	    features.put(PART_OF_SPEECH, PREPOSITION);
	}
	else if (pos == 'p') {
	    features.put(PART_OF_SPEECH, PRONOUN);
	}
	else if (pos == 'm') {
	    features.put(PART_OF_SPEECH, NUMERAL);
	}
	else if (pos == 'i') {
	    features.put(PART_OF_SPEECH, INTERJECTION);
	}
	else if (pos == 'e') {
	    features.put(PART_OF_SPEECH, EXCLAMATION);
	} else if (pos == 'f') {
	    features.put(PART_OF_SPEECH, FUNCTIONAL);
	}
	
	char person = chars[1];
	if (person == '1') {
	    features.put(PERSON, FIRST_PERSON);
	}
	else if (person == '2') {
	    features.put(PERSON, SECOND_PERSON);
	}
	else if (person == '3') {
	    features.put(PERSON, THIRD_PERSON);
	}
	
	char number = chars[2];
	if (number == 's') {
	    features.put(NUMBER, SINGULAR);
	}
	else if (number == 'p') {
	    features.put(NUMBER, PLURAL);
	}
	else if (number == 'd') {
	    features.put(NUMBER, DUAL);
	}
	
	char tense = chars[3];
	if (tense == 'p') {
	    features.put(TENSE, PRESENT);
	}
	else if (tense == 'i') {
	    features.put(TENSE, IMPERFECT);
	}
	else if (tense == 'r') {
	    features.put(TENSE, PERFECT);
	}
	else if (tense == 'l') {
	    features.put(TENSE, PLUPERFECT);
	}
	else if (tense == 't') {
	    features.put(TENSE, FUTURE_PERFECT);
	}
	else if (tense == 'f') {
	    features.put(TENSE, FUTURE);
	}
	else if (tense == 'a') {
	    features.put(TENSE, AORIST);
	}
	
	char mood = chars[4];
	if (mood == 'i') {
	    features.put(MOOD, INDICATIVE);
	}
	else if (mood == 's') {
	    features.put(MOOD, SUBJUNCTIVE);
	}
	else if (mood == 'n') {
	    features.put(MOOD, INFINITIVE);
	}
	else if (mood == 'm') {
	    features.put(MOOD, IMPERATIVE);
	}
	else if (mood == 'g') {
	    features.put(MOOD, GERUNDIVE);
	}
	else if (mood == 'u') {
	    features.put(MOOD, SUPINE);
	}
	else if (mood == 'p') {
	    features.put(MOOD, PARTICIPLE);
	}
	
	char voice = chars[5];
	if (voice == 'a') {
	    features.put(VOICE, ACTIVE);
	}
	else if (voice == 'p') {
	    features.put(VOICE, PASSIVE);
	}
	else if (voice == 'd') {
	    features.put(VOICE, DEPONENT);
	}
	else if (voice == 'e') {
	    features.put(VOICE, MEDIO_PASSIVE);
	}
	
	char gender = chars[6];
	if (gender == 'm') {
	    features.put(GENDER, MASCULINE);
	}
	else if (gender == 'f') {
	    features.put(GENDER, FEMININE);
	}
	else if (gender == 'n') {
	    features.put(GENDER, NEUTER);
	}
	
	// CASE is a reserved word
	char caseLetter = chars[7];
	if (caseLetter == 'n') {
	    features.put(CASE, NOMINATIVE);
	}
	else if (caseLetter == 'g') {
	    features.put(CASE, GENITIVE);
	}
	else if (caseLetter == 'd') {
	    features.put(CASE, DATIVE);
	}
	else if (caseLetter == 'a') {
	    features.put(CASE, ACCUSATIVE);
	}
	else if (caseLetter == 'b') {
	    features.put(CASE, ABLATIVE);
	}
	else if (caseLetter == 'v') {
	    features.put(CASE, VOCATIVE);
	}
	else if (caseLetter == 'i') {
	    features.put(CASE, INSTRUMENTAL);
	}
	else if (caseLetter == 'l') {
	    features.put(CASE, LOCATIVE);
	}
	
	char degree = chars[8];
	if (degree == 'p') {
	    features.put(DEGREE, POSITIVE);
	}
	else if (degree == 'c') {
	    features.put(DEGREE, COMPARATIVE);
	}
	else if (degree == 's') {
	    features.put(DEGREE, SUPERLATIVE);
	}

	
	char possessive = chars[9];
	if (possessive == 'p') {
	    features.put(POSSESSIVE, POSSESSIVE);
	}
	else if (possessive == 'a') {
	    features.put(POSSESSIVE, POSS_1P);
	}
	else if (possessive == 'b') {
	    features.put(POSSESSIVE, POSS_1S);
	}
	else if (possessive == 'c') {
	    features.put(POSSESSIVE, POSS_2D);
	}
	else if (possessive == 'd') {
	    features.put(POSSESSIVE, POSS_2FP);
	}
	else if (possessive == 'e') {
	    features.put(POSSESSIVE, POSS_2FS);
	}
	else if (possessive == 'f') {
	    features.put(POSSESSIVE, POSS_2MP);
	}
	else if (possessive == 'g') {
	    features.put(POSSESSIVE, POSS_2MS);
	}
	else if (possessive == 'h') {
	    features.put(POSSESSIVE, POSS_3D);
	}
	else if (possessive == 'i') {
	    features.put(POSSESSIVE, POSS_3FP);
	}
	else if (possessive == 'j') {
	    features.put(POSSESSIVE, POSS_3FS);
	}
	else if (possessive == 'k') {
	    features.put(POSSESSIVE, POSS_3MP);
	}
	else if (possessive == 'k') {
	    features.put(POSSESSIVE, POSS_3MS);
	}
	
	
	char definite = chars[10];
	if (definite == 'd') {
	    features.put(DEFINITE, DEFINITE);
	}
	else if (definite == 'i') {
	    features.put(DEFINITE, INDEFINITE);
	}

	char obj = chars[11];
	
	char o_person = chars[11];
	if (o_person == 'a') {
	    features.put(OBJ, O1P);
	}
	else if (o_person == 'b') {
	    features.put(OBJ, O1S);
	}
	else if (o_person == 'c') {
	    features.put(OBJ, O2D);
	}
	else if (o_person == 'd') {
	    features.put(OBJ, O2FP);
	}
	else if (o_person == 'e') {
	    features.put(OBJ, O2FS);
	}
	else if (o_person == 'f') {
	    features.put(OBJ, O2MP);
	}
	else if (o_person == 'g') {
	    features.put(OBJ, O2MS);
	}
	else if (o_person == 'h') {
	    features.put(OBJ, O3D);
	}
	else if (o_person == 'i') {
	    features.put(OBJ, O3FP);
	}
	else if (o_person == 'j') {
	    features.put(OBJ, O3FS);
	}
	else if (o_person == 'k') {
	    features.put(OBJ, O3MP);
	}
	else if (o_person == 'l') {
	    features.put(OBJ, O3MS);
	}

	char prefix = chars[12];
	if (prefix == 'c') {
	    features.put(PREFIX, CONJ);
	}
	else if (prefix == 'e') {
	    features.put(PREFIX, EMPHATIC_PARTICLE);
	}
	else if (prefix == 'p') {
	    features.put(PREFIX, PREP);
	}
	else if (prefix == 'r') {
	    features.put(PREFIX, RESULT_CLAUSE_PARTICLE);
	}


	
	
	return features;
    }
    
}
