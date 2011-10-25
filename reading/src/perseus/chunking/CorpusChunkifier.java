package perseus.chunking;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
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
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.util.Config;

public class CorpusChunkifier extends RecentlyModifiedCorpusProcessor {

	private static Logger logger = Logger.getLogger(CorpusChunkifier.class);

	private boolean ignoreModificationDate = false;
	
	Semaphore tocSem, chunkSem, freqSem;

	public String getTaskName() {
		return "chunkify";
	}

	public Date getReferenceDate(Chunk documentChunk) {
		Metadata docMetadata = documentChunk.getQuery().getMetadata();
		File sourceFile = docMetadata.getSourceFile();
		if (sourceFile != null && sourceFile.exists()) {
			return new Date(sourceFile.lastModified());
		}

		// If we couldn't actually get the file's modification date, return
		// *now* as the date, which will pretty much force it to be processed.
		return new Date();
	}

	public boolean ignoreModificationDate() {
		return ignoreModificationDate;
	}

	public void setIgnoreModificationDate(boolean imd) {
		ignoreModificationDate = imd;
	}

	public void endDocument(Chunk documentChunk) {
		super.endDocument(documentChunk);
		//System.err.println("Ended "+documentChunk.getDocumentID());
		//keeper.record("Processed document " + documentChunk.getQuery());
	}

	public boolean shouldProcessDocument(Chunk documentChunk) {
		// Check the timestamp, but only if we haven't explicitly been told
		// to ignore modification dates.
		if (!ignoreModificationDate) {
			if (!super.shouldProcessDocument(documentChunk)) return false;
		}

		// If we can't get a valid XML file, don't do any processing!
		// We can't handle SGML files in Java--skip them if we find them
		// and they don't have corresponding XML versions.
		File sourceFile = documentChunk.getMetadata().getSourceFile();

		if (sourceFile == null) {
			logger.info("Skipping " + documentChunk.getQuery() +
			" which has no XML source file");
			return false;
		} else if (!sourceFile.exists()) {
			logger.info("Skipping " + documentChunk.getQuery()
					+ "with nonexistent source " + sourceFile);
			return false;
		}

		return true;
	}

	public CorpusChunkifier() {
		this(new Semaphore(1), new Semaphore(1), new Semaphore(1));
	}
	
	public CorpusChunkifier(Semaphore tocSem, Semaphore chunkSem, Semaphore freqSem) {
		super();
		setOption(SUBDOC_METHOD, ONE_DOC);
		this.tocSem = tocSem;
		this.chunkSem = chunkSem;
		this.freqSem = freqSem;
	}

	// Do the actual processing in the call to startDocument(), not the one
	// to processDocument(). Because of the way the CorpusProcessor is
	// structured, it will call processDocument() not on the whole document
	// but on each *subtext*, which is not what we want (because the routine
	// in the Chunk class that it calls, getSubTexts(), assumes that the
	// texts have already been chunkified--which is a bad thing to assume.
	// On the other hand, startDocument() only ever gets called once,
	// regardless of subtexts, so it's safe to use.
	public void startDocument(Chunk documentChunk) {
		super.startDocument(documentChunk);
		//System.err.println(documentChunk.getDocumentID());
		String documentID = documentChunk.getQuery().getDocumentID();

		try {
			// We don't need to check whether the given file exists--if it
			// didn't, we wouldn't have gotten past shouldProcessDocument().
			File sourceFile = documentChunk.getMetadata().getSourceFile();
			if (!sourceFile.isAbsolute()) {
				sourceFile = new File(Config.getSourceTextsPath(),
						sourceFile.getPath());
			}

			ByteOffsetParser.parse(documentID, sourceFile.getAbsolutePath(), tocSem, chunkSem, freqSem);
		} catch (IOException ioe) {
			logger.warn("Problem chunkifying document " + documentChunk, ioe);
		} catch (NullPointerException npe) {
			// this probably means we couldn't find a value for the key in
			// the metadata.
			logger.warn("No source file specified for " + documentChunk + "!");
		}
	}

	// Does nothing. See the comments preceding startDocument().
	public void processDocument(Chunk documentChunk) {
	}

	public static void main(String[] args) {

		CorpusChunkifier cc = new CorpusChunkifier();
		cc.setOption(CorpusProcessor.SUBDOC_METHOD, CorpusProcessor.ONE_DOC);

		Options options = new Options()
		.addOption("f", "force", false, "force chunking of unmodified documents")
		.addOption("h", "help", false, "print this message")
		.addOption(OptionBuilder.withLongOpt("threads")
				.withDescription("set number of threads to use")
				.hasArg()
				.withArgName("NUMBER")
				.create());

		CommandLineParser parser = new PosixParser();
		CommandLine cl;

		HelpFormatter helpFormatter = new HelpFormatter();
		String helpString = cc.getClass().getName() + " [processable]+";
		String[] workingArgs = args;
		
		boolean hasForce = false;
		//use the minimum # of cpu's available to this machine, but allow user to change #
		int THREADS = Runtime.getRuntime().availableProcessors();
		
		try {
			cl = parser.parse(options, args);
			if (cl.hasOption("force") || cl.hasOption('f')) {
				logger.info("Forcing chunking of all documents");
				cc.setIgnoreModificationDate(true);
				hasForce = true;
			} else if (cl.hasOption("help") || cl.hasOption('h')) {
				helpFormatter.printHelp(helpString, options);
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
			Semaphore tocSem = new Semaphore(1);
			Semaphore chunkSem = new Semaphore(1);
			Semaphore freqSem = new Semaphore(1);
			for (String documentID : documentIDs) {
				Query documentQuery = new Query(documentID);
				CorpusChunkifier loader = new CorpusChunkifier(tocSem, chunkSem, freqSem);
				loader.setIgnoreModificationDate(hasForce);
				loader.setQuery(documentQuery);
				exec.execute(loader);
			}
			exec.shutdown();
		} else {
			for (String arg : workingArgs) {
				try {
					logger.info("Processing " + arg);
					cc.processAnything(arg);
				} catch (UnsupportedOperationException uoe) {
					logger.error(uoe);
					helpFormatter.printHelp(helpString, options);
					System.exit(1);
				}
			}
		}
	}
}
