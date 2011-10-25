package perseus.morph;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import perseus.language.Language;

/**
 * MorphCode provides a mechanism to map between the morphological
 * codes in the database and the representation of those codes
 * within the Java classes as a Map.
 */
public class MorphCode {
	private static Logger logger = Logger.getLogger(MorphCode.class);

    
    private static Map<Language,MorphCodeGenerator> generatorClasses =
	new HashMap<Language,MorphCodeGenerator>();
    static {
	generatorClasses.put(
		Language.GREEK, new GreekCodeGenerator());
	generatorClasses.put(
		Language.OLD_NORSE, new OldNorseCodeGenerator());
	generatorClasses.put(
		Language.OLD_ENGLISH, new OldEnglishCodeGenerator());
	generatorClasses.put(
		Language.LATIN, new LatinCodeGenerator());
	generatorClasses.put(
		Language.ARABIC, new ArabicCodeGenerator());
    }
    
    // This maps each feature-type to an array of feature Strings.
    private static Map<String,String[]> featureMap =
	new HashMap<String,String[]>();
	
	// This maps each feature string to its type.
	private static Map<String,String> reverseFeatureMap =
	    new HashMap<String,String>();
	
	public static final String PART_OF_SPEECH = "pos";
	public static final String NOUN = "noun";
	public static final String VERB = "verb";
	public static final String PARTICIPLE = "part";
	public static final String ADJECTIVE = "adj";
	public static final String ADVERB = "adv";
	public static final String ADVERBIAL = "adverbial";
	public static final String ARTICLE = "article";
	public static final String PARTICLE = "partic";
	public static final String CONJUNCTION = "conj";
	public static final String PREPOSITION = "prep";
	public static final String PRONOUN = "pron";
	public static final String NUMERAL = "numeral";
	public static final String INTERJECTION = "interj";
	public static final String EXCLAMATION = "exclam";
	public static final String PUNCTUATION = "punc";
	public static final String IRREGULAR = "irreg";
	public static final String FUNCTIONAL = "func";
	static {
	    registerFeatures(PART_OF_SPEECH, new String[] {
		    NOUN, VERB, PARTICIPLE, ADJECTIVE, ADVERB, ADVERBIAL, ARTICLE,
		    PARTICLE, CONJUNCTION, PREPOSITION, PRONOUN, NUMERAL,
		    INTERJECTION, EXCLAMATION, PUNCTUATION, IRREGULAR, FUNCTIONAL
	    });
	}
	
	public static final String PERSON = "person";
	public static final String FIRST_PERSON = "1st";
	public static final String SECOND_PERSON = "2nd";
	public static final String THIRD_PERSON = "3rd";
	static {
	    registerFeatures(PERSON, new String[] {
		    FIRST_PERSON, SECOND_PERSON, THIRD_PERSON
	    });
	}
	
	public static final String NUMBER = "number";
	public static final String SINGULAR = "sg";
	public static final String PLURAL = "pl";
	public static final String DUAL = "dual";
	static {
	    registerFeatures(NUMBER, new String[] {
		    SINGULAR, PLURAL, DUAL
	    });
	}
	
	public static final String TENSE = "tense";
	public static final String PRESENT = "pres";
	public static final String IMPERFECT = "imperf";
	public static final String PERFECT = "perf";
	public static final String PLUPERFECT = "plup";
	public static final String FUTURE_PERFECT = "futperf";
	public static final String FUTURE = "fut";
	public static final String AORIST = "aor";
	public static final String PAST_ABSOLUTE = "pastabs";
	static {
	    registerFeatures(TENSE, new String[] {
		    PRESENT, IMPERFECT, PERFECT, PLUPERFECT, FUTURE_PERFECT,
		    FUTURE, AORIST, PAST_ABSOLUTE
	    });
	}
	
	public static final String MOOD = "mood";
	public static final String INDICATIVE = "ind";
	public static final String SUBJUNCTIVE = "subj";
	public static final String OPTATIVE = "opt";
	public static final String INFINITIVE = "inf";
	public static final String IMPERATIVE = "imperat";
	public static final String GERUNDIVE = "gerundive";
	public static final String SUPINE = "supine";
	public static final String GERUND = "gerund";
	static {
	    registerFeatures(MOOD, new String[] {
		    INDICATIVE, SUBJUNCTIVE, OPTATIVE, INFINITIVE,
		    IMPERATIVE, GERUNDIVE, SUPINE, GERUND
	    });
	}
	
	public static final String VOICE = "voice";
	public static final String ACTIVE = "act";
	public static final String PASSIVE = "pass";
	public static final String DEPONENT = "dep";
	public static final String MIDDLE = "mid";
	public static final String MEDIO_PASSIVE = "mp";
	static {
	    registerFeatures(VOICE, new String[] {
		    ACTIVE, PASSIVE, DEPONENT, MIDDLE, MEDIO_PASSIVE
	    });
	}
	
	public static final String GENDER = "gender";
	public static final String MASCULINE = "masc";
	public static final String FEMININE = "fem";
	public static final String NEUTER = "neut";
	static {
	    registerFeatures(GENDER, new String[] {
		    MASCULINE, FEMININE, NEUTER
	    });
	}
	
	public static final String CASE = "case";
	public static final String NOMINATIVE = "nom";
	public static final String GENITIVE = "gen";
	public static final String DATIVE = "dat";
	public static final String ACCUSATIVE = "acc";
	public static final String ABLATIVE = "abl";
	public static final String VOCATIVE = "voc";
	public static final String LOCATIVE = "loc";
	public static final String INSTRUMENTAL = "ins";
	static {
	    registerFeatures(CASE, new String[] {
		    NOMINATIVE, GENITIVE, DATIVE, ACCUSATIVE, ABLATIVE,
		    VOCATIVE, LOCATIVE, INSTRUMENTAL
	    });
	}
	
	public static final String DEGREE = "degree";
	public static final String POSITIVE = "pos";
	public static final String COMPARATIVE = "comp";
	public static final String SUPERLATIVE = "superl";
	static {
	    registerFeatures(DEGREE, new String[] {
		    POSITIVE, COMPARATIVE, SUPERLATIVE
	    });
	}
	
	public static final String DIALECT = "dialect";
	// uhh...I have no idea what should go here. What does Cruncher output?
	public static final String AEOLIC = "aeolic";
	public static final String ATTIC = "attic";
	public static final String DORIC = "doric";
	public static final String EPIC = "epic";
	public static final String HOMERIC = "homeric";
	public static final String IONIC = "ionic";
	public static final String PARADIGM_FORM = "parad_form";
	public static final String PROSE = "prose";
	public static final String POETIC = "poetic";
	static {
	    registerFeatures(DIALECT, new String[] {
		    AEOLIC, ATTIC, DORIC, EPIC, HOMERIC, IONIC,
		    PARADIGM_FORM, PROSE, POETIC
	    });
	}
	
	public static final String UNSPECIFIED = "UNSPECIFIED";
	
	// Catch-all for morphological quirks that don't fit in another category
	public static final String OTHER = "feature";
	
	private static void registerFeatures(String type, String[] features) {
	    if (featureMap.containsKey(type)) {
	    	logger.debug("Already registered morph-type " + type + "!");
	    }
	    for (int i = 0; i < features.length; i++) {
		reverseFeatureMap.put(features[i], type);
	    }
	    
	    featureMap.put(type, features);
	}
	
		/**
		 * Arabic-specific features
		 *
		 */
		
	public static final String PREFIX = "prefix";
	
	public static final String CONJ = "conj+";
	public static final String PREP = "prep+";
	public static final String EMPHATIC_PARTICLE = "emphatic+";
	public static final String RESULT_CLAUSE_PARTICLE = "resultative+";
	public static final String INTERROGATIVE_PARTICLE = "interrogative+";
	public static final String NEG_PARTICLE = "neg+";

	static {
		registerFeatures(PREFIX, new String[] {
			CONJ, PREP, EMPHATIC_PARTICLE, RESULT_CLAUSE_PARTICLE, INTERROGATIVE_PARTICLE, NEG_PARTICLE
		});
	}

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
	
	static {
		registerFeatures(OBJ, new String[] {
			O1P, O1S, O2D, O2FP, O2FS, O2MP, O2MS, O3D, O3FP, O3FS, O3MP, O3MS
		});
	}
	
	public static final String DEFINITE = "definite";
	public static final String INDEFINITE = "indefinite";
	static {
		registerFeatures(DEFINITE, new String[] {
			DEFINITE, INDEFINITE
		});
	}

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
		
	static {
			registerFeatures(POSSESSIVE, new String[] {
				POSS_1P, POSS_1S, POSS_2D, POSS_2FP, POSS_2FS, POSS_2MP, POSS_2MS, POSS_3D, POSS_3FP, POSS_3FS, POSS_3MP, POSS_3MS
			});
		}
		
	/**
	 * Returns true if this is we recognize the given feature. We'll
	 * recognize it iff it's been registered by a call to registerFeatures().
	 */
	public static boolean isKnownFeature(String feature) {
	    return reverseFeatureMap.containsKey(feature);
	}
	
	/**
	 * Returns the type of this feature, as registered by registerFeatures(),
	 * or OTHER if we don't recognize it.
	 * E.g., getTypeForFeature(NOUN) would return PART_OF_SPEECH.
	 */
	public static String getTypeForFeature(String feature) {
	    if (reverseFeatureMap.containsKey(feature)) {
		return reverseFeatureMap.get(feature);
	    }
	    
	    return OTHER;
	}
	
	/**
	 * Returns all known features denoted by the given type. E.g.,
	 * getFeaturesForType(DEGREE) would return [POSITIVE, COMPARATIVE,
	 * SUPERLATIVE].
	 */
	public static String[] getFeaturesForType(String type) {
	    if (featureMap.containsKey(type)) {
		return featureMap.get(type);
	    }
	    
	    return new String[0];
	}
	
	public static Map<String,String[]> getFeatureMap() {
		return featureMap;
	}
	
	/**
	 * Translate the Map representation of the morphological code into 
	 * a string
	 *
	 * @param features
	 * @param languageCode
	 * @return string
	 */
	public static String getCode(Map<String,String> features, Language language) {
	    MorphCodeGenerator codeGenerator = getCodeGenerator(language);
	    return codeGenerator.getCode(features);
	}

	public static String getCode(Map<String,String> features, String languageCode) {
	    return getCode(features, Language.forCode(languageCode));
	}
	
	/**
	 * Translate the string representation of the morphological code into
	 * a map
	 *
	 * @param code the morphological code as a string
	 * @param languageCode the language of the morph code
	 */
	public static Map<String,String> getFeatures(String code, Language language) {
	    MorphCodeGenerator codeGenerator = getCodeGenerator(language);
	    return codeGenerator.getFeatures(code);
	}
	
	public static Map<String,String> getFeatures(String code, String languageCode) {
	    return getFeatures(code, Language.forCode(languageCode));
	}
	
	/**
	 * Return a MorphCodeGenerator depending upon the language
	 *
	 * @param a language
	 * @return a MorphCodeGenerator for the given language, or null if none exists
	 */
	public static MorphCodeGenerator getCodeGenerator(Language language) {
	    if (!generatorClasses.containsKey(language)) {
		return null;
	    }

	    return generatorClasses.get(language);
	}
}
