package perseus.language;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import perseus.language.dao.HibernateLanguageDAO;
import perseus.util.Stringifier;

/**
 * Loads language settings from a properties file.
 */
public class LanguageLoader {
	private static Logger logger = Logger.getLogger(LanguageLoader.class);

	public void load(String path) throws ConfigurationException {
		Configuration config = new PropertiesConfiguration(new File(path));

		Language language = null;
		List<Language> languages = new ArrayList<Language>();

		HibernateLanguageDAO dao = new HibernateLanguageDAO();
		dao.clear();
		dao.beginTransaction();

		Iterator it = config.getKeys();
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] tokens = key.split("\\.");
			if (tokens.length == 3) {
				// okay, this is a language and its display name
				String abbreviation = tokens[2];

				int id = config.getInteger(key + ".id", -1);
				if (id != -1) {
					language = dao.getById(id);
					if (language == null) {
						language = new Language(abbreviation, config.getString(key));			
					}
				}

				languages.add(language);

				language.addAbbreviations(new ArrayList<String>(config.getList(key + ".abbrev",
						Collections.emptyList())));

				language.setHasMorphData(config.getBoolean(key + ".morph_data", false));

				language.setId(config.getInteger(key + ".id", -1));
				if (config.getString(key + ".adapter") != null) {
					language.setAdapterClassName(
							config.getString(key + ".adapter"));
				}
				if (language.getId() == -1) {
					throw new IllegalArgumentException("Bad ID for language: " +
							language.getName());
				}
				logger.info(Stringifier.toString(language));
			}
		}

		for (Language l : languages) {
			dao.saveOrUpdate(l);
		}
		dao.endTransaction();
	}

	public static void main(String[] args) {

		if (args.length == 0) {
			logger.error("Usage: LanguageLoader <language file>+");
			System.exit(1);
		}

		LanguageLoader loader = new LanguageLoader();

		for (String arg : args) {
			try {
				loader.load(arg);
			} catch (ConfigurationException ce) {
				logger.fatal("Problem loading languages!", ce);
				System.exit(1);
			}
		}
	}
}
