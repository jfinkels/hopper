package perseus.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.jdom.Element;

import perseus.language.dao.HibernateLanguageDAO;
import perseus.language.dao.LanguageDAO;
import perseus.util.Stoplist;

/**
 * A replacement for the LanguageCode class--provides a concrete
 * representation of a language. This includes a display name, one or more
 * abbreviations, a boolean indicating whether the language has morphological
 * data, and an adapter (to provide language-specific implementations). The
 * languages themselves are loaded by the LanguageLoader class, which uses
 * properties in a languages.properties file. (We store the languages in our
 * database, instead of merely reading them from the properties file every time
 * we run something, so that other objects can use them as a foreign key if
 * they want).
 * 
 * The old implementation of languages used arbitrarily-specified numeric
 * language IDs; in this implementation we keep the old IDs (that is, we
 * specify them in the properties file and tell Hibernate that we are
 * assigning them manually).
 *
 * @see LanguageCode
 * @see LanguageLoader
 * @see LanguageDAO
 */
public class Language implements Comparable<Language> {

	private static Set<Language> allLanguages;
	private static Set<Language> searchableLanguages;
	private static Set<Language> defintionLookupLanguages;
	private static Set<Language> resolveFormLanguages;
	private static Map<String,Language> languagesByCode;

	static {
		allLanguages = Collections.unmodifiableSet(
				new HibernateLanguageDAO().getAllLanguages());

		languagesByCode = new HashMap<String,Language>();
		for (Language language : allLanguages) {
			for (String abbreviation : language.getAbbreviations()) {
				languagesByCode.put(abbreviation, language);
			}
		}
		languagesByCode = Collections.unmodifiableMap(languagesByCode);
	}

	/** An instance representing the result of looking up an unknown
	 *  abbreviation.
	 */
	public static final Language UNKNOWN = new Language("?", "UNKNOWN");

	// Some constants representing commonly-used languages. Note that these
	// will all return UNKNOWN if you haven't actually loaded the languages.
	public static final Language ENGLISH = forCode("en");
	public static final Language GREEK = forCode("greek");
	public static final Language LATIN = forCode("la");
	public static final Language OLD_NORSE = forCode("non");
	public static final Language OLD_ENGLISH = forCode("oe");
	public static final Language ARABIC = forCode("ar");
	public static final Language GERMAN = forCode("de");
	public static final Language FRENCH = forCode("fr");
	public static final Language ITALIAN = forCode("it");

	/** Whether the language has usable morphological data. */
	private boolean hasMorphData = false;

	/** This language's abbreviations; the first listed is the default.
	 *  Usually these correspond to abbreviations used in XML documents
	 *  or database tables--English, for example, might have "en" or "eng".
	 */
	private List<String> abbreviations = new ArrayList<String>();

	/** The display name for this language (like "English"). */
	private String name;

	/** The class representing this language's adapter, to be specified in
	 * the .properties file.
	 */
	private Class adapterClass;

	/** This language's database ID. */
	private Integer id;

	private Logger logger = Logger.getLogger(getClass());

	public Language() {}

	public Language(String abbrev, String name) {
		this.name = name;
		if (abbrev != null) {
			abbreviations.add(abbrev);
		}
	}

	public List<String> getAbbreviations() {
		return abbreviations;
	}

	public void setAbbreviations(List<String> abbreviations) {
		this.abbreviations = abbreviations;
	}

	public void addAbbreviations(List<String> abbrevs) {
		// We want to preserve ordering, but also make sure each abbreviation
		// occurs only once in our list.
		for (String abbrev : abbrevs) {
			if (abbreviations.indexOf(abbrev) == -1) {
				abbreviations.add(abbrev);
			}
		}
	}

	public String getAbbreviation() {
		return getCanonicalAbbreviation();
	}

	/**
	 * Returns this language's "canonical" abbreviation, defined as the first
	 * element in our list of abbreviations.
	 * 
	 * @return the canonical abbreviation for this language
	 */
	public String getCanonicalAbbreviation() {
		return (getAbbreviations().isEmpty() ? null : abbreviations.get(0));
	}

	public boolean getHasMorphData() {
		return hasMorphData;
	}

	public void setHasMorphData(boolean hasMorphData) {
		this.hasMorphData = hasMorphData;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * A convenience method--its name is the same as a method in the
	 * LanguageCode class.
	 */
	public String getCode() {
		return getCanonicalAbbreviation();
	}

	public String toString() {
		return getName();
	}

	public static boolean isKnownCode(String code) {
		return forCode(code) != UNKNOWN;
	}

	/**
	 * Returns the language matching the given abbreviation. If none can be
	 * found, returns UNKNOWN.
	 * 
	 * @param code a language code, like "la" or "en"
	 * @return the language matching the code, or UNKNOWN if none matched
	 */
	public static Language forCode(String code) {
		if (languagesByCode.containsKey(code)) {
			return languagesByCode.get(code);
		}

		return UNKNOWN;

		/*
           Language match = new HibernateLanguageDAO().getByAbbreviation(code);
           if (match == null) match = UNKNOWN;

           languagesByCode.put(code, match);
           long end = System.nanoTime();
           return match;
		 */
	}

	/**
	 * Returns the language matching the given Hibernate ID.
	 * 
	 * @param id the numeric ID to search for
	 * @return a matching language, otherwise UNKNOWN
	 */
	public static Language forId(int id) {
		Language match = new HibernateLanguageDAO().getById(id);
		return (match != null) ? match : UNKNOWN;
	}

	/**
	 * Returns all known languages.
	 * @return a set of all known languages, sorted by ID
	 */
	public static Set<Language> getAll() {
		return new TreeSet<Language>(allLanguages);
	}

	/**
	 * Return a set of all languages in the system marked as having
	 * morphological data.
	 *
	 * @return all loaded languages with morphological data
	 */
	public static Set<Language> getMorphLanguages() {
		Set<Language> morphLanguages = new TreeSet<Language>();
		for (Language language : getAll()) {
			if (language.getHasMorphData()) morphLanguages.add(language);
		}
		return morphLanguages;
	}

	/**
	 * Return the languages that are searchable in the general collection
	 * @return set of languages that can be searched
	 */
	public static Set<Language> getSearchableLanguages() {
		searchableLanguages = getAll();
		searchableLanguages.remove(Language.ARABIC);
		searchableLanguages.remove(Language.FRENCH);
		searchableLanguages.remove(Language.ITALIAN);
		return searchableLanguages;
	}

	/**
	 * Return languages that can be used in the definition lookup tool
	 * @return set of languages
	 */
	public static Set<Language> getDefinitionLookupLanguages() {
		defintionLookupLanguages = getAll();
		defintionLookupLanguages.remove(Language.ENGLISH);
		defintionLookupLanguages.remove(Language.FRENCH);
		defintionLookupLanguages.remove(Language.GERMAN);
		defintionLookupLanguages.remove(Language.ITALIAN);
		defintionLookupLanguages.remove(Language.OLD_ENGLISH);
		return defintionLookupLanguages;
	}

	/**
	 * Return languages that can be used in the resolve form tool
	 * @return set of languages
	 */
	public static Set<Language> getResolveFormLanguages() {
		resolveFormLanguages = getAll();
		resolveFormLanguages.remove(Language.FRENCH);
		resolveFormLanguages.remove(Language.GERMAN);
		resolveFormLanguages.remove(Language.ITALIAN);
		resolveFormLanguages.remove(Language.OLD_ENGLISH);
		return resolveFormLanguages;
	}

	public Element toXML() {
		return new Element("language")
		.setAttribute("name", getName())
		.setAttribute("abbrev", getCanonicalAbbreviation());
	}

	public boolean hasStoplist() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL listURL = loader.getResource(getStoplistPath());
		return (listURL != null);
	}

	private String getStoplistPath() {
		return new File("stoplists", getCode() + ".stop").getPath();
	}

	public Stoplist getStoplist() {
		Stoplist stoplist = new Stoplist();

		BufferedReader reader = null;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream stream = loader.getResourceAsStream(getStoplistPath());
			if (stream == null) return stoplist;

			reader = new BufferedReader(new InputStreamReader(stream));

			String nextLine;
			while ((nextLine = reader.readLine()) != null) {
				stoplist.add(nextLine);
			}
		} catch (IOException ioe) {
			logger.error("Problem reading stoplist file", ioe);
		} finally {
			try {
				if (reader != null) { reader.close(); }
			} catch (IOException ioe) {
				logger.warn("Unable to close stoplist file", ioe);
			}
		}

		return stoplist;
	}

	public int compareTo(Language o) {
		return getId().compareTo(o.getId());
	}

	public LanguageAdapter getAdapter() {
		if (adapterClass != null) {
			try {
				return (LanguageAdapter) adapterClass.newInstance();
			} catch (InstantiationException e) {
				logger.warn("Couldn't instantiate adapter " +
						adapterClass.getName() +
						". Returning default adapter instead",
						e);
			} catch (IllegalAccessException e) {
				logger.warn("Problem accessing " + adapterClass.getName() +
						". Returning default adapter instead", e);
			}
		}

		return new DefaultLanguageAdapter();
	}

	public String getAdapterClassName() {
		return (adapterClass != null) ? adapterClass.getName() : null;
	}

	public void setAdapterClassName(String acn) {
		if (acn == null) return;
		try {
			adapterClass = Class.forName(acn);
		} catch (ClassNotFoundException e) {
			logger .error("Unable to find adapter for " + getName(), e);
		}
	}

	public int hashCode() {
		int result = 17;

		result += 37 * getName().hashCode();
		return result;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Language)) return false;

		return getName().equals(((Language) o).getName());
	}
}
