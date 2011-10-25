package perseus.morph;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import perseus.document.Chunk;
import perseus.document.CorpusProcessor;
import perseus.document.Document;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.Token;
import perseus.language.LanguageCode;
import perseus.util.SQLHandler;
import perseus.util.Stoplist;

public class MorphCodeAggregator extends CorpusProcessor {

	private static Logger logger = Logger.getLogger(MorphCodeAggregator.class);

	private static final boolean USE_PRIOR = true;

	String currentLanguage;
	String previousLanguage;
	int tokens;

	Map<String, Map<String, Double>> morphCounts;
	Map<Bigram, Double> priorCounts;
	Map<String, Map<Lemma, List<Parse>>> cachedParses;

	List<Parse> priorParses; // stores most recent unstopped list of parses
	Chunk currentChunk;

	Stoplist stoplist;

    private Semaphore morphSem, priorSem;

	/**
	 * Class Constructor
	 */
    public MorphCodeAggregator(Semaphore morphSem, Semaphore priorSem) {
		super();
		currentLanguage = "";
		previousLanguage = "";

		morphCounts = new HashMap<String, Map<String, Double>>();
		priorCounts = new HashMap<Bigram, Double>();
		cachedParses = new HashMap<String, Map<Lemma, List<Parse>>>();

		stoplist = null;

		this.morphSem = morphSem;
		this.priorSem = priorSem;
	}

	public MorphCodeAggregator() {
	    this(new Semaphore(1), new Semaphore(1));
	}

	/**
	 * True if the following documentChunk should be processed
	 */
	public boolean shouldProcessDocument(Chunk documentChunk) {
		return documentChunk.getMetadata().getLanguage().getHasMorphData();
	}


	public void startDocument(Chunk documentChunk) {
		super.startDocument(documentChunk);

		Metadata metadata = documentChunk.getMetadata();

		currentLanguage = metadata.get(Metadata.LANGUAGE_KEY);
		if (!currentLanguage.equals("previousLanguage")) {
			stoplist = Stoplist.getPriorFrequencyStoplist(currentLanguage);
		}

		logger.info("Starting document " + documentChunk);
	}

	public void endDocument(Chunk documentChunk) {
		tokens = 0;

		try {
		    morphSem.acquire();
		} catch (InterruptedException e) {
		    logger.trace("Thread interrupted");
		}
		writeMorphCounts();
		morphSem.release();
		morphCounts.clear();

		if (USE_PRIOR) {
		    try {
			priorSem.acquire();
		    } catch (InterruptedException e) {
			logger.trace("Thread interrupted");
		    }
			writePriorCounts();
			priorSem.release();
			priorCounts.clear();
		}

		cachedParses.clear();
		previousLanguage = currentLanguage;
		priorParses.clear();
	}

	public void processChunk(Chunk documentChunk) {
		currentChunk = documentChunk;

		super.processChunk(documentChunk);
	}

	public void processToken(Token token) {
		// Skip punctuation, spacing and the like
		if (token.getType() != Token.Type.WORD) {
			return;
		}

		tokens++;
		if ((tokens % 500) == 0) {
			logger.info(tokens + " tokens processed");
		}

		String word = token.getOriginalText();
		String languageCode = token.getLanguageCode();

		Map<Lemma, List<Parse>> parses = null;
		if (!cachedParses.containsKey(word + languageCode)) {
			parses = Parse.getParses(word, languageCode);
			cachedParses.put(word + languageCode, parses);
		}
		else {
			parses = (Map<Lemma, List<Parse>>) cachedParses.get(word+languageCode);
		}
		List<Parse> parseList = new ArrayList<Parse>();

		Iterator<List<Parse>> parseIt = parses.values().iterator();
		while (parseIt.hasNext()) {
			List<Parse> subparses = (List<Parse>) parseIt.next();
			parseList.addAll(subparses);
		}

		double parseFrequency = 1.0 / parseList.size();

		Iterator<Parse> listIt = parseList.iterator();
		while (listIt.hasNext()) {
			Parse parse = (Parse) listIt.next();

			updateMorphCounts(word, parse, parseFrequency);
		}

		if (hasLemmasInStoplist(parses)) {
			logger.info("STOPLISTED: " + parses + " " + word);
		} else {
			priorParses = parseList;
		}
	}

	private boolean hasLemmasInStoplist(Map<Lemma, List<Parse>> parses) {
		if (stoplist == null) {
			return false;
		}

		if (parses.isEmpty()) {
			return false;
		}

		for (Iterator<Lemma> it = parses.keySet().iterator(); it.hasNext(); ) {
			String lemma = it.next().toString();
			lemma = lemma.replaceAll("\\d+$", "");

			if (!stoplist.contains(lemma)) {
				return false;
			}
		}

		return true;
	}

	private void updateMorphCounts(String word, Parse parse,
			double parseFrequency) {

		Map<String, String> features = MorphCode.getFeatures(
				parse.getMorphCode(), parse.getLanguageCode());
		String languageCode = parse.getLanguageCode();

		// Add every subset of the parse to the total count.
		Set<Map<String, String>> submaps = getAllSubmaps(features);
		// Add an empty set, to keep track of the total number of words
		// (of any form at all)
		submaps.add(new HashMap<String, String>());

		Iterator<Map<String, String>> subIt = submaps.iterator();
		while (subIt.hasNext()) {
			Map<String, String> featureMap = (Map<String, String>) subIt.next();

			Map<String, Double> languageCounts;
			if (morphCounts.containsKey(languageCode)) {
				languageCounts = (Map<String, Double>) morphCounts.get(languageCode);
			} else {
				languageCounts = new HashMap<String, Double>();
				morphCounts.put(languageCode, languageCounts);
			}

			double subFreqTotal = parseFrequency;
			String morphCode = MorphCode.getCode(featureMap, languageCode);
			if (languageCounts.containsKey(morphCode)) {
				subFreqTotal += ((Double) languageCounts.get(morphCode))
				.doubleValue();
			}
			languageCounts.put(morphCode, new Double(subFreqTotal));
		}

		if (USE_PRIOR) {
			addPriorCounts(parse, parseFrequency, features, currentChunk);
		}
	}

	private void addPriorCounts(Parse parse, double parseFrequency,
			Map<String, String> featureMap, Chunk currentChunk) {

		if (priorParses == null) {
			return;
		}

		for (int i = 0, n = priorParses.size(); i < n; i++) {
			Parse previousParse = (Parse) priorParses.get(i);

			Map<String,String> previousFeatures = MorphCode.getFeatures(
					previousParse.getCode(),
					previousParse.getLemma().getLanguageCode());

			Bigram bigram = new Bigram(parse.getLemma().getDisplayForm(),
					featureMap,
					previousParse.getLemma().getDisplayForm(),
					previousFeatures);

			double frequencyTotal = parseFrequency;
			if (priorCounts.containsKey(bigram)) {
				frequencyTotal +=
					((Double) priorCounts.get(bigram)).doubleValue();
			}
			priorCounts.put(bigram, new Double(frequencyTotal));
		}
	}

	private Set<Map<String, String>> getAllSubmaps(Map<String, String> features) {

		Set<Map<String, String>> output = new HashSet<Map<String, String>>();

		output.add(features);

		Iterator<String> it = features.keySet().iterator();
		while (it.hasNext()) {
			String feature = (String) it.next();

			Map<String, String> workingMap = new HashMap<String, String>(features);
			workingMap.remove(feature);

			if (workingMap.size() > 0 && !output.contains(workingMap)) {
				output.addAll(getAllSubmaps(workingMap));
			}
		}

		return output;
	}

	private void writePriorCounts() {

		Connection con = null;

		try {
			con = SQLHandler.getConnection();

			logger.info("Writing prior frequencies: "
					+ priorCounts.size());
			PreparedStatement insertStatement = con.prepareStatement(
					"INSERT INTO prior_frequencies VALUES ("
					+ "?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
					+ "count=count+?");

			for (Iterator<Bigram> it = priorCounts.keySet().iterator();
			it.hasNext(); ) {

				Bigram bigram = (Bigram) it.next();

				int languageID = LanguageCode.getLanguageID(currentLanguage);

				insertStatement.setInt(1, languageID);

				String previousCode = MorphCode.getCode(
						bigram.getPreviousFeatures(), currentLanguage);
				insertStatement.setString(2, previousCode);

				String currentCode = MorphCode.getCode(
						bigram.getCurrentFeatures(), currentLanguage);

				insertStatement.setString(3, currentCode);

				Double frequency = (Double) priorCounts.get(bigram);
				insertStatement.setDouble(4, frequency.doubleValue());

				// for the ON DUPLICATE KEY clause
				insertStatement.setDouble(5, frequency.doubleValue());

				insertStatement.executeUpdate();
			}
		} catch (SQLException sqle) {
			logger.fatal("Problem updating database: " + sqle);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException sqle) {
				logger.fatal("Problem releasing connection" + sqle);
			}
		}
	}

	public void writeMorphCounts() {
		Connection con = null;

		logger.info("Writing morph counts: " + morphCounts.size());
		try {
			con = SQLHandler.getConnection();

			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO morph_frequencies VALUES ("
					+ "?, ?, ?) ON DUPLICATE KEY UPDATE "
					+ "count=count+?");

			for (Iterator<String> it = morphCounts.keySet().iterator();
			it.hasNext(); ) {

				String languageCode = (String) it.next();
				Map<String, Double> languageCounts = (Map<String, Double>) morphCounts.get(languageCode);

				logger.info("Writing counts for language code: "
						+ languageCounts.size());
				pstmt.setInt(1, LanguageCode.getLanguageID(languageCode));

				for (Iterator<String> langIt = languageCounts.keySet().iterator();
				langIt.hasNext(); ) {
					String morphCode = (String) langIt.next();

					pstmt.setString(2, morphCode);

					Double frequency = (Double) languageCounts.get(morphCode);
					pstmt.setDouble(3, frequency.doubleValue());

					// for the ON DUPLICATE KEY clause
					pstmt.setDouble(4, frequency.doubleValue());

					pstmt.executeUpdate();
				}
			}
		} catch (SQLException sqle) {
			logger.fatal("Problem updating database: " + sqle);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException sqle) {
				logger.fatal("Problem releasing connection" + sqle);
			}
		}
	}

	private void clearTables() {
		logger.info("Clearing tables...");
		Connection con = null;
		Statement initStmt = null;

		try {
			con = SQLHandler.getConnection();
			initStmt = con.createStatement();
		} catch (SQLException sqle) {
			logger.fatal("Problem connecting to database: " + sqle);
		}

		try {
			initStmt.executeUpdate("TRUNCATE TABLE morph_frequencies");
		} catch (SQLException sqle) {
			logger.fatal("Problem initializing morph_frequencies: "
					+ sqle);
		}

		try {
			initStmt.executeUpdate("TRUNCATE TABLE prior_frequencies");
		} catch (SQLException sqle) {
			logger.fatal("Problem initializing prior_frequencies: "
					+ sqle);
		}

		try {
			initStmt.close();
			con.close();
		} catch (SQLException sqle) {
			logger.fatal("Problem closing database connection: " + sqle);
		}
	}

	public static void main(String args[]) {
		MorphCodeAggregator ag = new MorphCodeAggregator();

		Options options = new Options()
		.addOption(OptionBuilder.withLongOpt("threads")
				.withDescription("set number of threads to use")
				.hasArg()
				.withArgName("NUMBER")
				.create());

		CommandLineParser parser = new PosixParser();
		//use the minimum # of cpu's available to this machine, but allow user to change #
		int THREADS = Runtime.getRuntime().availableProcessors();

		try {
		    CommandLine cl = parser.parse(options, args);
		    
		    if (cl.hasOption("threads")) {
			String option = cl.getOptionValue("threads");
			if (option != null && !option.equals("")) {
			    THREADS = Integer.parseInt(option);
			}
		    }
		} catch (ParseException e) {
		    e.printStackTrace();
		}

		ag.clearTables();

		ExecutorService exec = Executors.newFixedThreadPool(THREADS);
		List<String> documentIDs = Document.getTexts();
		Collections.shuffle(documentIDs);
		Semaphore morphSem = new Semaphore(1);
		Semaphore priorSem = new Semaphore(1);
		for (String documentID : documentIDs) {
		    Query documentQuery = new Query(documentID);
		    MorphCodeAggregator loader = new MorphCodeAggregator(morphSem, priorSem);
		    loader.setQuery(documentQuery);
		    exec.execute(loader);
		}
		exec.shutdown();
	}


	private class Bigram {
		String currentLemma;
		Map<String, String> currentFeatures;

		String previousLemma;
		Map<String, String> previousFeatures;

		public Bigram(String cl, Map<String, String> cf, String pl, Map<String, String> pf) {
			currentLemma = cl;
			currentFeatures = cf;

			previousLemma = pl;
			previousFeatures = pf;
		}

		public String getCurrentLemma() {
			return currentLemma;
		}

		public String getPreviousLemma() {
			return previousLemma;
		}

		public Map<String, String> getCurrentFeatures() {
			return currentFeatures;
		}

		public Map<String, String> getPreviousFeatures() {
			return previousFeatures;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Bigram)) {
				return false;
			}
			Bigram b = (Bigram) o;

			return (currentLemma.equals(b.currentLemma)
					&& previousLemma.equals(b.previousLemma)
					&& currentFeatures.equals(b.currentFeatures)
					&& previousFeatures.equals(b.previousFeatures));
		}

		public int hashCode() {
			int result = 37;

			result += 17 * currentLemma.hashCode();
			result += 17 * previousLemma.hashCode();
			result += 17 * currentFeatures.hashCode();
			result += 17 * previousFeatures.hashCode();

			return result;
		}

		public String toString() {
			return previousLemma + "[" + previousFeatures + "] -> "
			+ currentLemma + "[" + currentFeatures + "]";
		}
	}
}
