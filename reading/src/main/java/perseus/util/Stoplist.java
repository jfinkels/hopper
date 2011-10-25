package perseus.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

public class Stoplist extends TreeSet<String> {

	static Map<String,Stoplist> lists = new HashMap<String,Stoplist>();

	private static Logger logger = Logger.getLogger(Stoplist.class);

	/**
	 * Return a Stoplist for the given language
	 * 
	 * @param languageCode the language to retrieve the Stoplist for
	 * @return Stoplist
	 */
	public static Stoplist getStoplist(String languageCode) {
		return getList(languageCode + ".stop");
	}

	/**
	 * Return a common list for the given language
	 *
	 * @param languageCode the language to get the Stoplist for
	 * @return Stoplist
	 */
	public static Stoplist getCommonList(String languageCode) {
		return getList(languageCode + ".common");
	}

	/**
	 * Return a prior frequency stop list
	 *
	 * @param languageCode the language to get the Stoplist for
	 * @return Stoplist
	 */
	public static Stoplist getPriorFrequencyStoplist(String languageCode) {
		return getList(languageCode + ".priorfreq");
	}

	/**
	 * Retrieve any list with the specified filename
	 *
	 * @param filename the filename that has the desired list
	 * @return Stoplist
	 */
	public static Stoplist getList(String filename) {
		if (lists.containsKey(filename)) {
			return (Stoplist) lists.get(filename);
		}

		// Otherwise, read each line into a new Stoplist.
		Stoplist list = new Stoplist();

		try {
			File stoplistFile = new File(Config.getStoplistPath(),
					filename);

			// Return an empty set if there is no stop list for this
			// language.
			if (! stoplistFile.exists()) {
				logger.warn("no list: " + filename);
				return list;
			}

			BufferedReader in = new BufferedReader(new FileReader(stoplistFile));
			String word = null;
			while ((word = in.readLine()) != null) {
				list.add(word);
			}

			lists.put(filename, list);
		} catch (IOException e) {
			logger.fatal("problem loading word list: " + filename, e);
		}

		return list;
	}
}
