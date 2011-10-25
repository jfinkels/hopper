package perseus.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * A class that controls display preferences
 */
public class DisplayPreferences {
	private static Logger logger = Logger.getLogger(DisplayPreferences.class);

	public static final String DISPLAY_PREFS_COOKIE = "disp.prefs";

	/** User's preferred method for inputting Greek */
	public static final String GREEK_INPUT_KEY = "greek.input";

	/** Transcoder output configuration */
	public static final String GREEK_DISPLAY_KEY = "greek.display";

	/** Transcoder output configuration */
	public static final String ARABIC_DISPLAY_KEY = "arabic.display";

	/** How named entities and encyclopedia links are sorted */
	public static final String IE_SORT_KEY = "ie_sort";

	/** What language you get if you request a document without specifying
     version */
	public static final String LANGUAGE_KEY = "language";

	/** Whether to load translations and commentaries automatically */
	public static final String AUTOLOAD_KEY = "autoload";

	/** Whether to show the navigational bar by default */
	public static final String NAVBAR_KEY = "navbar.display";

	/** The last chunk scheme selected (for any document, not just the one
	 *  being viewed; if it doesn't apply to a given document it'll be ignored) */
	public static final String DEFAULT_CHUNK_SCHEME = "default.scheme";

	/** The last default type selected */
	public static final String DEFAULT_CHUNK_TYPE = "default.type";

	/** All known preferences */
	public static String prefNames[] = { GREEK_DISPLAY_KEY, GREEK_INPUT_KEY, ARABIC_DISPLAY_KEY,
		LANGUAGE_KEY, AUTOLOAD_KEY, IE_SORT_KEY, NAVBAR_KEY,
		DEFAULT_CHUNK_SCHEME, DEFAULT_CHUNK_TYPE};

	private static Map<String,List<String>> knownValues =
		new HashMap<String,List<String>>();
	private static Map<String,List<String>> displayValues =
		new HashMap<String,List<String>>();

	static {
		/**
		 * TODO put all these in a bundle, for purposes of i18n!
		 */
		addToPrefs(GREEK_DISPLAY_KEY, knownValues, new String[] { 
				"UnicodeC",
				"UnicodeD",
				"PerseusBetaCode",
				"SPIonic",
				"SGreek",
				"GreekKeys",
				"GreekXLit"
		});

		addToPrefs(GREEK_DISPLAY_KEY, displayValues, new String[] {
				"Unicode (precombined)",
				"Unicode (combining diacriticals)",
				"Beta Code",
				"SPIonic",
				"SGreek",
				"GreekKeys",
				"Latin transliteration"
		});

		addToPrefs(ARABIC_DISPLAY_KEY, knownValues, new String[] {
				"UnicodeC",
				"Buckwalter"
		});

		addToPrefs(ARABIC_DISPLAY_KEY, displayValues, new String[] {
				"Unicode",
				"Buckwalter transliteration"
		});

		addToPrefs(LANGUAGE_KEY, knownValues, new String[] {
				"original", "trans"
		});

		addToPrefs(LANGUAGE_KEY, displayValues, new String[] {
				"Original Language", "Translation"
		});

		addToPrefs(NAVBAR_KEY, knownValues, new String[] {
				"show", "hide"
		});

		addToPrefs(NAVBAR_KEY, displayValues, new String[] {
				"Show by default",
				"Hide by default"
		});

		addToPrefs(IE_SORT_KEY, knownValues, new String[] {
				"display", "token", "freq"
		});

		addToPrefs(IE_SORT_KEY, displayValues, new String[] {
				"alphabetically/chronologically",
				"as they appear on the page",
				"by frequency"
		});
	}

	private Map<String,String> preferences = new HashMap<String,String>();

	private HttpServletRequest request;
	private HttpServletResponse response;

	/** 
	 * Class Constructor.  Specifically, an empty constructor for non-servlet functions      
	 */
	public DisplayPreferences() {
	}

	private static void addToPrefs(String key,
			Map<String, List<String>> targetMap, String[] values) {
		targetMap.put(key, Arrays.asList(values));
	}

	/**
	 * Class Constructor for servlet functions.
	 *
	 * @param request
	 * @param response
	 */
	public DisplayPreferences(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;

		this.preferences = parseCookie(getCookie(DISPLAY_PREFS_COOKIE));
	}
	
	public void updatePreferences() {
		boolean changed = false;

		for (int i=0;i<prefNames.length;i++) {
			if (request.getParameter(prefNames[i]) != null &&
					! request.getParameter(prefNames[i]).equals(preferences.get(prefNames[i]))) {
				preferences.put(prefNames[i],
						request.getParameter(prefNames[i]));
				changed = true;
			}
		}

		if (changed) {
			writeCookie();
		}
	}

	public String getGreekDisplay() {
		return get(GREEK_DISPLAY_KEY);
	}

	public String getArabicDisplay() {
		return get(ARABIC_DISPLAY_KEY);
	}

	public String getGreekInput() {
		return get(GREEK_INPUT_KEY);
	}

	public String getLanguage() {
		return get(LANGUAGE_KEY);
	}

	public String getAutoload() {
		return get(AUTOLOAD_KEY);
	}

	public String getNavbar() {
		return get(NAVBAR_KEY);
	}

	public String getDefaultChunkScheme() {
		return get(DEFAULT_CHUNK_SCHEME);
	}

	public String getDefaultChunkType() {
		return get(DEFAULT_CHUNK_TYPE);
	}
	
	public List<String> getGreekKnownValues() {
		return knownValues.get(GREEK_DISPLAY_KEY);
	}
	
	public List<String> getGreekDisplayValues() {
		return displayValues.get(GREEK_DISPLAY_KEY);
	}
	
	public List<String> getArabicKnownValues() {
		return knownValues.get(ARABIC_DISPLAY_KEY);
	}
	
	public List<String> getArabicDisplayValues() {
		return displayValues.get(ARABIC_DISPLAY_KEY);
	}
	
	public List<String> getLanguageKnownValues() {
		return knownValues.get(LANGUAGE_KEY);
	}
	
	public List<String> getLanguageDisplayValues() {
		return displayValues.get(LANGUAGE_KEY);
	}
	
	public List<String> getNavbarKnownValues() {
		return knownValues.get(NAVBAR_KEY);
	}
	
	public List<String> getNavbarDisplayValues() {
		return displayValues.get(NAVBAR_KEY);
	}

	/**
	 * Returns the key specified, a convenience method
	 *
	 * @param key A static constant associated with this class
	 * @return the desired encoding
	 */
	public String get(String key) {
		if (preferences.containsKey(key)) {
			return (String) preferences.get(key);
		}

		// Defaults
		if (key.equals(GREEK_DISPLAY_KEY)) {
			return "UnicodeC";
		}
		if (key.equals(GREEK_INPUT_KEY)) {
			return "BetaCode";
		}
		if (key.equals(ARABIC_DISPLAY_KEY)) {
			return "UnicodeC";
		}
		if (key.equals(LANGUAGE_KEY)) {
			return "trans";
		}
		if (key.equals(AUTOLOAD_KEY)) {
			return "orig";
		}
		if (key.equals(NAVBAR_KEY)) {
			return "show";
		}

		return null;
	}

	public void put(String key, String value) {
		preferences.put(key, value);
	}

	/****************************************************************
	 * Returns the value of the cookie associated with name given 
	 * as an argument.
	 *
	 * @param name	the name of the cookie 
	 * @return value of the cookie specified
	 ***************************************************************/
	private String getCookie(String name) {
		Cookie[] cookieJar = request.getCookies();

		if (cookieJar != null) {
			for (int i=0; i<cookieJar.length; i++) {
				if ((cookieJar[i].getName()).equals(name)) {
					return cookieJar[i].getValue();
				}
			}
		}

		return null;
	}

	/**
	 * Convert the current settings in the DisplayPreferences class to a cookie
	 */
	private void writeCookie() {
		StringBuffer cookie = new StringBuffer("");

		boolean firstValue = true;
		Iterator<String> keys = preferences.keySet().iterator();
		while (keys.hasNext()) {
			if (! firstValue) {
				cookie.append("|");
			} else {
				firstValue = false;
			}

			String key = (String) keys.next();
			cookie.append(key);
			cookie.append("=");
			cookie.append(preferences.get(key));
		}

		Cookie cook = new Cookie(DISPLAY_PREFS_COOKIE, cookie.toString());
		cook.setVersion(1);
		response.addCookie(cook);
	}

	/**
	 * Parses a cookie into display preferences
	 *
	 * @param cookie The cookie to convert into display preferences
	 * @return HashMap of display preferences
	 */
	private HashMap<String,String> parseCookie (String cookie) {
		HashMap<String,String> cookiePrefs = new HashMap<String,String>();

		if (cookie == null) {
			return cookiePrefs;
		}

		String[] tokens = cookie.split("\\|");
		for (int i=0;i<tokens.length;i++) {
			String nameValuePair[] = tokens[i].split("=");

			try {
				cookiePrefs.put(nameValuePair[0], nameValuePair[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("array index out of bounds: " + cookie);
			}
		}

		return cookiePrefs;
	}

	/** Find out whether we can support XMLHttpRequest; assume for now that it
	 *  only works with Mozilla and IE 5+ (but not IE/Mac)
	 *
	 *  @deprecated Using the BrowserDetector class instead is suggested.
	 */
	public boolean supportsXMLHTTPRequest() {
		String userAgent = request.getHeader("User-Agent");

		if (userAgent.startsWith("Mozilla/5.0") ||
				(userAgent.indexOf("MSIE") != -1
						&& userAgent.indexOf("Mac_PowerPC") == -1)) {
			return true;
		}

		return false;
	}

	public static List<String> possibleValues(String prefName) {
		if (knownValues.containsKey(prefName)) {
			return knownValues.get(prefName);
		}
		return new ArrayList<String>();
	}

	public static List<String> displayValues(String prefName) {
		if (displayValues.containsKey(prefName)) {
			return displayValues.get(prefName);
		}
		return new ArrayList<String>();
	}

}
