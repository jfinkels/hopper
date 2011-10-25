package perseus.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Provides access to various settings in this installation of the hopper.
 * Values can be set in one of three ways. In descending order of priority,
 * these are:
 * 
 * <ol>
 * <li>As system properties</li>
 * <li><s>As Tomcat environment variables</s></li>
 * <li>As values in the <kbd>hopper.properties</kbd> file, which can be
 * located anywhere in the classpath (in our current setup, it's part of
 * <kbd>hopper.jar</kbd>)</li>
 * </ol>
 * 
 * The class provides several methods to retrieve the most commonly-used properties,
 * along with two general-purpose methods (<kbd>getProperty()</kbd> and
 * <kbd>getStringArray()</kbd>). Also, the names of all known properties are provided
 * as strings, so that they can be imported statically if desired.
 */
public class Config {
    
    private static Logger logger = Logger.getLogger(Config.class);
    private static Configuration hopperConfig = reload();
    
    public static Configuration reload() {
	return loadConfig();
    }
    
    private Config() {}
    
    private static Configuration loadConfig() {
	try {
	    return new PropertiesConfiguration("hopper.properties");
	} catch (ConfigurationException ce) {
	    // Okay, we can't read the configuration at all;
	    // there's probably not much point in continuing, but this could
	    // be an acceptable situation if, for instance, we're running
	    // with all our parameters as system properties.
	    logger.warn("Unable to read configuration file!", ce);
	    return new PropertiesConfiguration();
	}
    }
    
    /** The path to the processed XML text files */
    public static final String DOCUMENT_FILE_PATH = "hopper.document.file.path";
    
    /** The path to the Lucene search indices */
    public static final String SEARCH_INDEX_PATH = "hopper.search.index.path";
    
    /** The path to our XSL stylesheets */
    public static final String STYLESHEET_PATH = "hopper.stylesheet.path";
    
    /** The system ID for this installation--not currently used */
    public static final String SYSTEM_ID = "hopper.system.id";
    
    /** The path to our stoplist files for various languages */
    public static final String STOPLIST_PATH = "hopper.stopList.path";
    
    /** The path to our Dublin Core catalogs */
    public static final String CATALOG_PATH = "hopper.catalog.path";
    
    /** The path to our source texts */
    public static final String SOURCE_TEXTS_PATH = "hopper.source.texts.path";
    
    /** One or more catalog files to load by default */
    public static final String CATALOG_FILES = "hopper.catalog.files";
    
    /** One or more collections to display on the main page */
    public static final String PRIMARY_COLLECTIONS = "hopper.primary.collections";
    
    /** The path to cruncher (the morpheus executable), if it's installed */
    public static final String CRUNCHER_PATH = "perseus.cruncher.path";
    
    /** The path to morpheus' morphlib/ directory, if present */
    public static final String MORPHLIB_PATH = "perseus.morphlib.path";
    
    /** One or more names of .abbrev files */
    public static final String ABBREVIATION_PATH = "hopper.abbrev.path";
    
    /** The path to things we're caching (not currently used) */
    public static final String CACHE_PATH = "hopper.cache.path";
    
    /** The directory in the cache containing our timelines (not used) */
    public static final String CACHE_TIMELINE_DIRECTORY = "hopper.cache.timeline.dir";
    
    /** The location of artifact, image and TGN data */
    public static final String DATA_PATH = "hopper.data.path";
    
    public static final String STATIC_PATH = "hopper.static.files.path";
    
    public static final String DATABASE_DRIVER = "hopper.database.driver";
    public static final String DATABASE_URL = "hopper.database.url";
    public static final String DATABASE_USERNAME = "hopper.database.username";
    public static final String DATABASE_PASSWORD = "hopper.database.password";
    
    public static final String RELAXNG_PATH = "hopper.rng.path";
    public static final String DTD_PATH = "perseus.dtd.path";
    
    public static final String[] artifactTypes = {"Building", "Coin", "Gem", "Sculpture", "Site", "Vase"};
    
    /**
     * Retrieve the file path.  This corresponds to hopper.document.file.path in the 
     * script.config file
     *
     * @return the document file path for the hopper
     */
    public static String getFilePath() {
        return getProperty(DOCUMENT_FILE_PATH);
    }

    /**
     * Retrieve the search index path.  This corresponds to hopper.search.index.path in the 
     * script.config file
     *
     * @return the search index path for the hopper
     */
    public static String getSearchIndexPath () {
        return getProperty(SEARCH_INDEX_PATH);
    }
    /**
     * Retrieve the stylesheet path.  This corresponds to hopper.stylesheet.path in the 
     * script.config file
     *
     * @return the stylesheet path for the hopper
     */
    public static String getStylesheetPath () {
	return getProperty(STYLESHEET_PATH);
    }

    /**
     * Retrieve the system id.  This corresponds to hopper.system.id in the 
     * script.config file
     *
     * @return the system id
     */
    public static String getSystemID () {
	return getProperty(SYSTEM_ID);
    }
    
    /**
     * Retrieve the cache path.  This corresponds to hopper.cache.path in the 
     * script.config file
     *
     * @return the cache path
     */
    public static String getCachePath () {
	return getProperty(CACHE_PATH);
    }
    
    /**
     * Retrieve the timeline image cache directory.  This corresponds to hopper.cache.timeline.dir in the 
     * script.config file
     *
     * @return the timeline image cache directory
     */
    public static String getTimelineImageCacheDirectory() {
	return getProperty(CACHE_TIMELINE_DIRECTORY);
    }
    
    /**
     * Retrieve the stoplist path id.  This corresponds to hopper.stoplist.path in the 
     * script.config file
     *
     * @return the stoplist path
     */
    public static String getStoplistPath () {
	return getProperty(STOPLIST_PATH);
    }

    /**
     * Retrieve the path to our XML catalogs (classics.xml, cwar.xml, etc.).
     * This corresponds to the property hopper.catalog.path.
     *
     * @return the path to the XML catalogs
     */
    public static String getCatalogPath() {
	return getProperty(CATALOG_PATH);
    }

    /**
     * Retrieve the path to our source SGML and XML texts.
     * This corresponds to the property hopper.source.texts.path.
     *
     * @return the path to the source texts
     */
    public static String getSourceTextsPath() {
	return getProperty(SOURCE_TEXTS_PATH);
    }

    /**
     * Returns the filenames of all the catalogs that this Hopper installation
     * is set to use by default.
     *
     * @return an array of catalog filenames
     */
    public static String[] getCatalogFiles() {
	 return getStringArray(CATALOG_FILES);
    }

    /**
     * Returns the collections that this hopper installation has defined as
     * "primary" collections. This is used chiefly to specify the
     * collections that should appear on the main collections page, as
     * distinct from subcollections and corpora.
     */
     public static String[] getPrimaryCollections() {
	 return getStringArray(PRIMARY_COLLECTIONS);
     }

     public static String getCruncherPath() {
	 return getProperty(CRUNCHER_PATH);
     }

     public static String getMorphlibPath() {
	 return getProperty(MORPHLIB_PATH);
     }
     
     public static String getDataPath() {
    	 return getProperty(DATA_PATH);
     }
     
     public static String getStaticPath() {
    	 return getProperty(STATIC_PATH);
     }
     
     /**
      * Returns all specified values for the given property, in the form of an
      * array of strings. 
      * 
      * @param key the property to search for
      * @return an array of strings, which may be empty
      */
     public static String[] getStringArray(String key) {
//	 String value = getPropertyFromContext(key);
//	 if (value != null) { return splitString(value); }
	 
	 if (hopperConfig.containsKey(key)) {
	     return hopperConfig.getStringArray(key);
	 }
	 
	 String value = System.getProperty(key);
	 if (value != null) { return splitString(value); }
	 
	 return new String[0];
     }
     
     private static String[] splitString(String value) {
	 return value.split(",\\s*");
     }

     /*
     private static String getPropertyFromContext(String key) {
	 String value = null;
	 
	 try {
	     // Get the environment context
	     Context ctx =
		 new InitialContext();
	     Context env =
		 (Context) ctx.lookup("java:comp/env");

	     // Find the property
	     value = (String) env.lookup(key);
	 } catch (NamingException ne) {
	     // If Tomcat can't find it, it's really not that big a deal;
	     // suppress the error and hope that we can look it up somewhere
	     // else.
	     
	     //logger.severe(String.format(
		     //"Problem finding environment variable %s: %s", key, ne));
	 }

	 return value;
     }
     */
     
    /**
     * Returns the value of the property specified by <kbd>key</kbd> as a
     * String.
     * 
     * @param key The name of the property to retrieve
     * @return the value of that property
     */
    public static String getProperty(String key) {
	String value = System.getProperty(key);
	if (value != null) return value;

//	value = getPropertyFromContext(key);
//	if (value != null) return value;
	
	value = hopperConfig.getString(key);
	if (value != null) return value;

	return null;
    }
    
    /**
     * Sets a configuration property. Note that not all parts of the system
     * will necessarily make use of the new property value; they may ignore
     * it, for instance, if they already acquired the value at startup. This
     * is most useful for testing purposes; e.g., you can store files in a
     * directory meant for testing instead of for production.
     * 
     * @param key the key to modify
     * @param value the new value for the key
     */
    public void setProperty(String key, String value) {
	System.setProperty(key, value);
    }

    /**
     * Returns a list of URLs pointing to all known files containing
     * abbreviations. Typically, these will be found in a JAR or somewhere in
     * the classpath.
     * 
     * @return a list of URLs pointing to files containing abbreviations
     */
    public static List<URL> getAbbreviationFiles() {
	String[] paths = getStringArray(ABBREVIATION_PATH);
	
	List<URL> output = new ArrayList<URL>();
	for (String path : paths) {
	    output.add(Thread.currentThread().getContextClassLoader()
		.getResource("abbreviations" + File.separator + path));
	}

	
	return output;
    }
}
