package perseus.document;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
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

import perseus.document.dao.HibernateWordCountDAO;
import perseus.language.Language;
import perseus.util.ObjectCounter;

/**
 * Counts the number of words for each document in the collection, breaking
 * down the total count by language. The counts are then written to the
 * database as {@link WordCount} objects.
 * 
 * When counts for all individual documents have been loaded, we create
 * counts for each collection/corpus, by aggregating the results for
 * each document within a given collection.
 * 
 * @see WordCount
 * @see HibernateWordCountDAO
 */
public class WordCountLoader extends RecentlyModifiedCorpusProcessor {

	private static Logger logger = Logger.getLogger(WordCountLoader.class);
	private ObjectCounter<Language> counter = new ObjectCounter<Language>();
	private HibernateWordCountDAO wcDAO = new HibernateWordCountDAO();;
	private Semaphore wordSem;

	public void processCorpus() {
		super.processCorpus();
		aggregateCounts();
	}

	@Override
	public String getTaskName() {
		return "load-wordcounts";
	}

	public static void main (String args[]) {
		WordCountLoader count = new WordCountLoader();

		Options options = new Options()
		.addOption("f", "force", false, "force loading of all word counts")
		.addOption("a", "aggregate", false, "aggregate counts")
		.addOption(OptionBuilder.withLongOpt("threads")
				.withDescription("set number of threads to use")
				.hasArg()
				.withArgName("NUMBER")
				.create());
		
		CommandLineParser parser = new PosixParser();
		CommandLine cl;
		String[] workingArgs = args;
		boolean hasForce = false;
		//use the minimum # of cpu's available to this machine, but allow user to change #
		int THREADS = Runtime.getRuntime().availableProcessors();
		
		try{
			cl = parser.parse(options, args);
			if (cl.hasOption("force") || cl.hasOption('f')) {
				hasForce = true;
				logger.info("Forcing loading of all word counts");
				count.setIgnoreModificationDate(true);
			}
			if (cl.hasOption("aggregate") || cl.hasOption('a')) {
				count.aggregateCounts();
				System.exit(0);
			}
			if (cl.hasOption("threads")) {
				String option = cl.getOptionValue("threads");
				if (option != null && !option.equals("")) {
					THREADS = Integer.parseInt(option);
				}
			}
			workingArgs = cl.getArgs();
		} catch (ParseException e) {
			logger.error("Unable to parse command-line arguments", e);
			System.exit(1);
		}

		if (workingArgs.length == 0) {
			ExecutorService exec = Executors.newFixedThreadPool(THREADS);
			List<String> documentIDs = Document.getTexts();
			Collections.shuffle(documentIDs);
			Semaphore wordSem = new Semaphore(1);
			for (String documentID : documentIDs) {
				Query documentQuery = new Query(documentID);
				WordCountLoader loader = new WordCountLoader(wordSem);
				loader.setIgnoreModificationDate(hasForce);
				loader.setQuery(documentQuery);
				exec.execute(loader);
			}
			exec.shutdown();
		} else {
			for (String arg : workingArgs) {
				try {
					logger.info("Processing " + arg);
					count.processAnything(arg);
				} catch (UnsupportedOperationException uoe) {
					logger.error(uoe);
					System.exit(1);
				}
			}
		}
	}


	public WordCountLoader(Semaphore wordSem) {
		super();
		setOption(CorpusProcessor.SUBDOC_METHOD, CorpusProcessor.ONE_DOC);
		if (wordSem == null) {
			this.wordSem = new Semaphore(1);
		} else {
			this.wordSem = wordSem;
		}
	}

	public WordCountLoader() {
		this(null);
	}

	public void processToken(Token token) {
		if (token.getType() == Token.Type.WORD) {
			Language language = token.getLanguage();
			counter.count(language);
		}
	}

	public void endDocument(Chunk documentChunk) {
		try {
			wordSem.acquire();
		} catch (InterruptedException ie) {
			logger.trace("Thread interrupted");
		}
		String documentID = documentChunk.getDocumentID();
		wcDAO.beginTransaction();

		wcDAO.deleteByDocument(documentID);

		for (Language language : counter.objects()) {
			// Hibernate doesn't like the "UNKNOWN" language, so save it as null
			Language savedLanguage = (language != Language.UNKNOWN ? language : null);
			WordCount count = new WordCount(savedLanguage, documentID, (long) counter.getCount(language));
			logger.info(count);
			wcDAO.save(count);
		}

		wcDAO.endTransaction();
		super.endDocument(documentChunk);
		wordSem.release();
		counter.clear();
	}

	public void aggregateCounts() {
		EnumSet<Corpus.Type> targetTypes =
			EnumSet.of(Corpus.Type.CONGLOMERATE, Corpus.Type.AUTHOR, Corpus.Type.WORK);
		List<Corpus> corpora = Corpus.getCorporaByTypes(targetTypes);
		corpora.addAll(Corpus.getCollections());

		wcDAO.beginTransaction();
		for (Corpus corpus : corpora) {
			logger.info("Aggregating for " + corpus.getID());
			wcDAO.aggregateCorpus(corpus);
		}
		wcDAO.endTransaction();
	}
}
