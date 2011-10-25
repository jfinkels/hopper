package perseus.visualizations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

import perseus.document.Chunk;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.document.Token;
import perseus.language.Language;
import perseus.util.HibernateUtil;
import perseus.visualizations.dao.HibernateWordDocumentCountDAO;
import perseus.visualizations.dao.HibernateYearWordCountDAO;

public class DistinctWordCountPerYearLoader extends RecentlyModifiedCorpusProcessor {

	private static Logger logger = Logger.getLogger(DistinctWordCountPerYearLoader.class);

	Language docLanguage = null;
	int docYear = 9999;
	Set<String> uniqueWords;
	Map<Language, Map<String, Integer>> uniqueWordsByLang;
	Map<Integer, Map<Language,Map<String, Integer>>> uniqueWordsByYear;
	HibernateWordDocumentCountDAO wDAO = new HibernateWordDocumentCountDAO();
	HibernateYearWordCountDAO yDAO = new HibernateYearWordCountDAO();

	private static Pattern YEAR_PATTERN = Pattern.compile("(\\d*)\\s*(((A\\.?D\\.?)|(C\\.?E\\.?))|((B\\.?C\\.?)|(B\\.?C\\.?E\\.?)))$", 
			Pattern.CASE_INSENSITIVE);
	
    private final Pattern BARE_WORD_PATTERN = Pattern.compile("[()\\\\/*=|+']");
	
	private static final int YEAR_RANGE = 20;

	public DistinctWordCountPerYearLoader() {
		uniqueWordsByYear = new HashMap<Integer, Map<Language,Map<String, Integer>>>();
	}

	/*
	 * 1. If this chunk has a creation or issue date, check the year.  if it isn't the "null" value (9999), 
	 * then return true, otherwise false
	 * 2. If this chunk has subdocs, return true, and do the date check (above) when you process each subdoc
	 * 3. Otherwise return false
	 * (non-Javadoc)
	 * @see perseus.document.RecentlyModifiedCorpusProcessor#shouldProcessDocument(perseus.document.Chunk)
	 */
	public boolean shouldProcessDocument(Chunk documentChunk) {
		if (documentChunk.getMetadata().hasCreationOrIssueDate()) {
			docYear = getDate(documentChunk.getMetadata().getCreationOrIssueDate());
			if (docYear == 9999) {
				return false;
			} else {
				return true;
			}
		} else if (documentChunk.getMetadata().hasSubdocs()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Convert BCE/BC and CE/AD dates to int
	 * Ranges that are <= YEAR_RANGE years are included (take the middle year)
	 * @param creationOrIssueDate
	 * @return
	 */
	private int getDate(String creationOrIssueDate) {
		//handle small ranges of dates
		if (creationOrIssueDate.contains("-")) {
			String[] dates = creationOrIssueDate.split("-");
			int startDate = processDate(dates[0]);
			int endDate = processDate(dates[1]);
			if (endDate - startDate <= YEAR_RANGE) {
				return (startDate + ((endDate - startDate) / 2));
			} else {
				return 9999;
			}
		} else {
			return processDate(creationOrIssueDate);
		}
	}
	
	private int processDate(String date) {
		Matcher yearMatcher = YEAR_PATTERN.matcher(date);
		if (yearMatcher.matches()) {
			int year = Integer.parseInt(yearMatcher.group(1));
			String era = yearMatcher.group(2).replaceAll("[.]", "");

			if (era.equalsIgnoreCase("BC") || era.equalsIgnoreCase("BCE")) {
				return -year;
			} else {
				return year;
			}
		} else {
			try {
				return Integer.parseInt(date);
			} catch (NumberFormatException e) {
				return 9999;
			}
		}
	}

	public void startDocument(Chunk documentChunk) {
		logger.info("processing "+documentChunk.toString());
		logger.info("doc year is "+docYear);
		docLanguage = documentChunk.getMetadata().getLanguage();
		
		uniqueWords = new HashSet<String>();
	}

	public void endDocument(Chunk documentChunk) {
		Map<String, Integer> wordCounts = new HashMap<String, Integer>();
		
		if (uniqueWordsByYear.containsKey(docYear)) {
			uniqueWordsByLang = uniqueWordsByYear.get(docYear);
			if (uniqueWordsByLang.containsKey(docLanguage)) {
				wordCounts = uniqueWordsByLang.get(docLanguage);
				for (String word : uniqueWords) {
					if (wordCounts.containsKey(word)) {
						wordCounts.put(word, wordCounts.get(word)+1);
					} else {
						wordCounts.put(word, 1);
					}
				}
			} else {
				wordCounts = new HashMap<String, Integer>();
				wordCounts = addAll();
			}
		} else {
			uniqueWordsByLang = new HashMap<Language, Map<String, Integer>>();
			wordCounts = new HashMap<String, Integer>();
			wordCounts = addAll();
		}
		
		uniqueWordsByLang.put(docLanguage, wordCounts);
		uniqueWordsByYear.put(docYear, uniqueWordsByLang);

		docLanguage = null;
		super.endDocument(documentChunk);
	}
	
	private Map<String, Integer> addAll() {
		Map<String, Integer> wordCounts = new HashMap<String, Integer>();
		for (String word : uniqueWords) {
			wordCounts.put(word, 1);
		}
		return wordCounts;
	}

	public void processToken(Token token) {
		if (token.getType() == Token.Type.WORD) {
			if (token.getLanguage() == docLanguage) {
				// case doesn't matter for english, latin and greek, so it's ok
				// to set the word to lower case.  if/when arabic texts are included, 
				// this will need to be changed
				String word = token.getOriginalText().toLowerCase();
				//remove accents
				word = BARE_WORD_PATTERN.matcher(word).replaceAll("");
				uniqueWords.add(word);
			}
		}
	}

	/**
	 * 
	 */
	private void writeToDatabase() {
		logger.info("Writing to database...");
		int updateCount = 0;
		for (int year : uniqueWordsByYear.keySet()) {
			Map<Language, Map<String, Integer>> wordsByLang = uniqueWordsByYear.get(year);
			logger.info("number of langs for year " + year + " is "+wordsByLang.size());

			for (Language lang : wordsByLang.keySet()) {
				logger.info("number of words for year "+ year + " and lang "+ lang.getName()+" is "+wordsByLang.get(lang).size());
				Map<String, Integer> words = wordsByLang.get(lang);

				for (String word : words.keySet()) {
					WordDocumentCount wdc = new WordDocumentCount(word, year, lang, words.get(word));
					wDAO.save(wdc);
					updateCount++;
					if (updateCount % 20 == 0) {
						HibernateUtil.getSession().flush();
						HibernateUtil.getSession().clear();
					}

					if (updateCount % 1000 == 0) {
						logger.info(updateCount + " words saved");
					}
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DistinctWordCountPerYearLoader dwcl = new DistinctWordCountPerYearLoader();

		Options options = new Options()
		.addOption("f", "force", false, "force loading of all word counts");

		CommandLineParser parser = new PosixParser();
		CommandLine cl;
		String[] workingArgs = args;

		try{
			cl = parser.parse(options, args);
			if (cl.hasOption("force") || cl.hasOption('f')) {
				dwcl.setIgnoreModificationDate(true);
			}
			workingArgs = cl.getArgs();
		} catch (ParseException e) {
			logger.error("Unable to parse command-line arguments "+ e);
			System.exit(1);
		}

		dwcl.clearTables();
		if (workingArgs.length > 0) {
			for (String arg : workingArgs) {
				dwcl.processAnything(arg);
			}
		} else {
			dwcl.processCorpus();
		}
		dwcl.writeToDatabase();
		dwcl.aggregate();
	}

	private void clearTables() {
		logger.info("clearing tables...");
		wDAO.clear();
		yDAO.clear();		
	}

	/**
	 * Aggregate total number of words per year per language
	 */
	private void aggregate() {
		logger.info("aggregating...");
		List<Language> languages = wDAO.getDistinctLanguages();
		logger.info("size of languages is "+languages.size());
		for (Language lang : languages) {
			ScrollableResults results = wDAO.getYearCounts(lang);
			while (results.next()) {
				int year = (Integer) results.get(0);
				long count = (Long) results.get(1);
				YearWordCount ywc = new YearWordCount(year, lang, count);
				yDAO.save(ywc);
			}
		}
	}

}
