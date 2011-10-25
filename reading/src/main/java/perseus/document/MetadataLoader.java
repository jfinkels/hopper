package perseus.document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.language.LanguageCode;
import perseus.util.Config;
import perseus.util.StringUtil;

public class MetadataLoader {

	private static Logger logger = Logger.getLogger(MetadataLoader.class);

	private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	private DocumentTaskManager taskManager = DocumentTaskManager.getManager();

	private boolean ignoreCache;

	private String CATALOG_METADATA_TASK = "catalog-metadata";
	private String DOCUMENT_METADATA_TASK = "document-metadata";

	private static final Map<String,String> sortExceptions =
		new HashMap<String,String>();

	static {
		sortExceptions.put("afer", "terence");
		sortExceptions.put("pliny the elder", "pliny maj");
		sortExceptions.put("seneca", "seneca min");
		sortExceptions.put("seneca the elder", "seneca maj");
		sortExceptions.put("honoratus", "servius");
		sortExceptions.put("maro", "vergil");
		sortExceptions.put("naso", "ovid");
		sortExceptions.put("pollio", "vitruvius");
		sortExceptions.put("rhodius", "apollonius");
		sortExceptions.put("siculus", "diodorus");
		sortExceptions.put("tranquillus", "suetonius");
		sortExceptions.put("pliny the younger", "pliny min");
		sortExceptions.put("laertius", "diogenes");
		sortExceptions.put("alexandrinus", "heron");
	}

	public MetadataLoader() {
		this(false);
	}

	public MetadataLoader(boolean ic) {
		ignoreCache = ic;
	}

	public void clear() throws SQLException {
		MetadataDAO md = new SQLMetadataDAO();
		md.clear();
	}

	public void save(MetadataStore store) throws SQLException {
		MetadataDAO md = new SQLMetadataDAO();
		md.save(store);
	}

	public MetadataStore load(String catalogFilename) throws
	MetadataExtractionException {

		MetadataStore catalogMetadata = loadCatalog(catalogFilename);
		MetadataStore documentMetadata = new MetadataStore();

		for (Iterator it = catalogMetadata.queryIterator(); it.hasNext(); ) {
			Query query = (Query) it.next();
			Metadata existingDocData = catalogMetadata.get(query);

			File textFile = existingDocData.getSourceFile();

			if (textFile == null) continue;

			try {
				MetadataStore fileData = loadFileMetadata(query, textFile);

				if (!existingDocData.has(Metadata.DATE_MODIFIED_KEY)) {
					String lastModified = getLastModifiedDate(existingDocData);
					if (lastModified != null) {
						existingDocData.addField(Metadata.DATE_MODIFIED_KEY,
								lastModified);
					}
				}
				documentMetadata.putAll(fileData);
			} catch (MetadataExtractionException e) {
				logger.warn("Problem getting metadata from file "
						+ textFile + ": " + e);
				e.printStackTrace();
			}
		}

		MetadataStore mergedData = catalogMetadata.merge(documentMetadata);
		for (Iterator it = mergedData.metadataIterator(); it.hasNext(); ) {
			Metadata metadata = (Metadata) it.next();
			Query query = metadata.getQuery();

			if (!query.isAbstract()) {
				String likelyType = guessType(query);
				if (likelyType != null) {
					metadata.addField(Metadata.TYPE_KEY, likelyType);
				}

				// If this query a text with no language specified, assume
				// English (but if it's a subdocument, assume that the main
				// document has language info and don't worry about it)
				if ("text".equals(metadata.get(Metadata.TYPE_KEY)) &&
						!metadata.has(Metadata.LANGUAGE_KEY) &&
						query.isJustDocumentID()) {

					metadata.addField(Metadata.LANGUAGE_KEY,
							LanguageCode.ENGLISH);
				}
			}
			addSortField(metadata);
		}

		return mergedData;
	}

	public MetadataStore loadCatalog(String catalogFilename) throws
	MetadataExtractionException {

		File catalogFile = new File(catalogFilename);
		if (!catalogFile.isAbsolute()) {
			catalogFile = new File(Config.getCatalogPath(), catalogFilename);
		}

		// Keep track of the timestamp using the absolute path to the catalog
		// file, not whatever form of it the user happened to enter.
		String catalogPath = catalogFile.getAbsolutePath();

		Date lastLoaded = null;
		try {
			lastLoaded = taskManager.getTimestamp(
					catalogPath, CATALOG_METADATA_TASK);
		} catch (SQLException se) {
			logger.warn("Problem getting timestamp: " + se);
		}

		Date catalogModified = new Date(catalogFile.lastModified());
		File cachedFile = getCachedCatalogFile(catalogFilename);

		logger.info(catalogFilename);

		MetadataStore catalogMetadata = new MetadataStore();
		boolean loadedFromCache = false;

		// Try loading from the cache, if we're allowed to...
		if (lastLoaded != null
				&& lastLoaded.after(catalogModified)
				&& !ignoreCache()) {
			logger.info(" [cached]");

			try {
				catalogMetadata = MetadataStoreLoader.fromXML(cachedFile);
				// If we made it here, we win.
				loadedFromCache = true;
			} catch (MetadataExtractionException mee) {
				logger.warn("Problem loading from cache: " + mee);
			}
		}

		// If we decided not to load from the cache, or we ran into a problem
		// loading, load the metadata normally and try to cache it.
		if (!loadedFromCache) {
			logger.info(" [loading]");
			catalogMetadata = loadUncachedCatalog(catalogFile);

			try {
				cache(catalogMetadata, cachedFile, catalogPath,
						CATALOG_METADATA_TASK);
			} catch (Exception e) {
				logger.warn("Problem caching catalog metadata: " + e);
			}
		}

		return catalogMetadata;
	}

	public static MetadataStore loadUncachedCatalog(File catalogFile)
	throws MetadataExtractionException {

		String styledMetadata = StyleTransformer.transform(catalogFile,
		"build/document/catalog.xsl");
		return MetadataStoreLoader.fromXML(styledMetadata);
	}

	/*
    private boolean shouldLoadCachedStore(File sourceFile, File cachedFile) {
	return (!ignoreCache &&
		(cachedFile.exists() &&
			sourceFile.lastModified() < cachedFile.lastModified()));
    }
	 */

	private File getCachedCatalogFile(String catalogFilename) {
		return new File(Config.getFilePath(),
				catalogFilename.replaceAll("/", "_")
				.replaceAll("\\.xml", ".metadata.xml"));
	}

	private void cache(MetadataStore store, File targetFile,
			String id, String taskName) {
		PrintWriter writer = null;

		if (targetFile.getParentFile() != null) {
			targetFile.getParentFile().mkdirs();
		}

		// Cache the file...
		try {
			writer = new PrintWriter(new BufferedWriter(
					new FileWriter(targetFile)));
			writer.write(store.toXML());
		} catch (IOException ioe) {
			logger.warn("Problem writing store to cache at "
					+ targetFile + ": " + ioe);
		} finally {
			writer.close();
		}

		// and record that we've processed it.
		if (taskName != null) {
			try {
				taskManager.set(id, taskName);
			} catch (SQLException se) {
				logger.warn("Problem setting timestamp: " + se);
			}
		}
	}

	public MetadataStore loadFileMetadata(Query query, File sourceFile)
	throws MetadataExtractionException {
		Date lastLoaded = null;
		try {
			lastLoaded = taskManager.getTimestamp(
					query.getDocumentID(), DOCUMENT_METADATA_TASK);
		} catch (SQLException se) {
			logger.warn("Problem getting timestamp: " + se);
		}

		Date fileModified = new Date(sourceFile.lastModified());

		// a bit hackish, but should suffice for now.
		// this SHOULD be okay, since subdocuments will be contained inside
		// the MetadataStore for the root document, and so there'll be no
		// clobbering.
		File cachedFile = new File(Chunk.getFilename(query.toString())
				.replaceAll("\\.xml",
				".metadata.xml"));

		MetadataStore documentMetadata = new MetadataStore();
		boolean loadedFromCache = false;

		if (lastLoaded != null
				&& lastLoaded.after(fileModified)
				&& !ignoreCache
				&& cachedFile.exists()) {

			try {
				documentMetadata = MetadataStoreLoader.fromXML(cachedFile);
				logger.debug(
						String.format("Loaded %s -> %s [cached]",
								query, sourceFile));

				loadedFromCache = true;
			} catch (MetadataExtractionException mee) {
				logger.warn("Problem loading from cache: " + mee);
			}
		}

		if (!loadedFromCache) {
			logger.info(String.format("Loaded %s -> %s", query, sourceFile));

			DocumentAdapter adapter = guessAdapter(sourceFile);
			documentMetadata = 
				adapter.getMetadataExtractor().extract(query, sourceFile);

			try {
				cache(documentMetadata, cachedFile, query.getDocumentID(),
						DOCUMENT_METADATA_TASK);
			} catch (Exception e) {
				logger.warn("Problem caching document metadata: " + e);
			}
		}
		return documentMetadata;
	}

	// Attempts to guess what sort of adapter we'll need by looking at the
	// specified file's DOCTYPE declaration.
	public DocumentAdapter guessAdapter(File sourceFile) {
		String rootElement = "TEI.2";

		try {
			BufferedReader reader =
				new BufferedReader(new FileReader(sourceFile));
			String nextLine;

			while ((nextLine = reader.readLine()) != null) {
				if (nextLine.startsWith("<!DOCTYPE")) break;
			}

			if (nextLine == null) {
				// Okay, something went wrong; just return a normal TEI adapter.
				return new TEIP4Adapter();
			}

			// The line should be of the form <!DOCTYPE <identifier> PUBLIC ...;
			// look at the second token.
			String[] doctypeTokens = nextLine.split("\\s+");
			rootElement = doctypeTokens[1];

			reader.close();
		} catch (IOException ioe) {
			logger.info(String.format("Couldn't guess doctype of %s: %s",
					sourceFile, ioe));
		}

		if (rootElement.equalsIgnoreCase("TEI.2")) {
			return new TEIP4Adapter();
		} else if (rootElement.equalsIgnoreCase("ETS")) {
			return new EEBOAdapter();
		}

		// *shrug*
		return new TEIP4Adapter();
	}

	// Adds one more unofficial metadata field: the XML text's
	// last-modified date
	private String getLastModifiedDate(Metadata metadata) {
		String textsDirectory = Config.getSourceTextsPath();
		String filePath = metadata.get(Metadata.SOURCE_TEXT_PATH_KEY);
		if (filePath == null) return null;

		File sourceFile = new File(textsDirectory, filePath);
		if (sourceFile.exists()) {
			return dateFormatter.format(new Date(sourceFile.lastModified()));
		} else {
			logger.warn("Couldn't find source text at " + filePath);
			return null;
		}
	}

	private String guessType(Query query) {
		String objectType = query.getObjectType();
		if (objectType.equals("text") ||
				objectType.equals("collection") ||
				objectType.equals("image")) {
			return objectType;
		}

		return null;
	}

	/**
	 * Constructs a sort key for the given Metadata. Much of this is
	 * hackish and could probably be better served using author-name
	 * authorities and non-sort tags on titles.
	 *
	 * (This is a straight port of the Perl code sorting code. Improvements,
	 * or an entirely new algorithm, would be most welcome.)
	 */
	private void addSortField(Metadata metadata) {
		StringBuffer keyBuffer = new StringBuffer();

		//1. Don't make a sort key for images
		if (metadata.has(Metadata.TYPE_KEY) &&
				metadata.get(Metadata.TYPE_KEY).equalsIgnoreCase("image")) {

			return;
		}

		// (1.5. Don't make a sort key for subdocuments)
		if (metadata.has(Metadata.SUBDOC_QUERY_KEY)) return;

		//2. Don't make a sort key if there already is one
		if (metadata.has(Metadata.SORT_KEY)) return;

		//3. Find the first dc:creator element. Find the last element of the
		//name and put it first.

		if (metadata.has(Metadata.CREATOR_KEY)) {
			String firstCreator =
				(String) (metadata.getList(Metadata.CREATOR_KEY).get(0));

			keyBuffer.append(resolveSortCreator(firstCreator));
		}

		//5. Lowercase everything (we do this later)

		//6. Merge all dc:title elements
		//7. Remove articles (A|An|The|Le|Les|La|Die|Il)
		if (metadata.has(Metadata.TITLE_KEY)) {
			String joinedTitles =
				StringUtil.join(metadata.getList(Metadata.TITLE_KEY), " ");

			if (keyBuffer.length() > 0) keyBuffer.append(" ");
			keyBuffer.append(joinedTitles.replaceAll(
					"\\b(A|An|The|Le|Les|La|Die|Il)\\b\\s*", ""));
		}
		//8. Lowercase everything
		//9. Delete anything that is not an ascii letter, number or a space
		//(this gets the algorithm into trouble, especially in the Norse texts)
		// ... our version of MySQL should be able to handle Unicode...
		// so we'll ignore this step for now.

		String sortField = keyBuffer.toString().toLowerCase();
		metadata.addField(Metadata.SORT_KEY, sortField);
	}

	private String resolveSortCreator(String creator) {
		String normalizedCreator = creator.toLowerCase();

		/*
		 * Some names are like R. J. Cholmeley, M.A.  We want to sort
		 * on Cholmeley, not M.A., so split on the comma
		 */
		String[] createCommaSplit = normalizedCreator.split(",");
		normalizedCreator = createCommaSplit[0];

		String[] creatorTokens = normalizedCreator.split("\\s+");

		//4. Check for a small number of "exceptions":

		// Strategy: we consider the name of the first listed creator.  Look at
		// the final part of the name; if it is listed as one of the
		// "exceptions" (see the Map above), find and return ONLY what the
		// exception resolves to. 
		// Next, check to see if the full normalized name and if it is listed as an 
		// exception, find and return what that resolved to.
		// Otherwise, return the listed creator, but
		// with the last element preceding the others (e.g., M. Tullius Cicero
		// becomes "cicero m tullius").

		String lastToken = creatorTokens[creatorTokens.length-1];

		/*
		 * Some texts have the common name in parentheses after the formal
		 * name, we want to sort on the common name.
		 */
		if (lastToken.startsWith("(") && lastToken.endsWith(")")) {
			lastToken = lastToken.substring(1, lastToken.length()-1);
		}

		if (sortExceptions.containsKey(lastToken)) {
			return (String) sortExceptions.get(lastToken);
		} else if (sortExceptions.containsKey(normalizedCreator)) {
			return (String) sortExceptions.get(normalizedCreator);
		} else {
			StringBuffer creatorBuffer = new StringBuffer();
			creatorBuffer.append(lastToken);
			for (int i = 0; i < creatorTokens.length-1; i++) {
				creatorBuffer.append(" ").append(creatorTokens[i]);
			}

			return creatorBuffer.toString();
		}
	}

	public boolean ignoreCache() { return ignoreCache; }
	public void setIgnoreCache(boolean ic) { ignoreCache = ic; }

	public static void main(String[] args) throws MetadataExtractionException, ParseException, SQLException {

		Options options = new Options()
		.addOption("c", "clear", false, "clear the database")
		.addOption("r", "reload", false, "clear/reload all catalog files")
		.addOption("h", "help", false, "print this message")
		.addOption("i", "ignore-cache", false, "ignore cache when reloading");

		CommandLineParser parser = new PosixParser();
		CommandLine cl = parser.parse(options, args);

		MetadataLoader loader = new MetadataLoader();
		if (cl.hasOption("ignore-cache") || cl.hasOption('i')) {
			loader.setIgnoreCache(true);
		}

		if (cl.hasOption("clear") || cl.hasOption('c')) {
			logger.info("Clearing existing metadata");
			loader.clear();
		} else if (cl.hasOption("reload") || cl.hasOption('r')) {
			logger.info("Clearing existing metadata");
			loader.clear();

			for (String catalogFile : Config.getCatalogFiles()) {
				logger.info("Loading metadata: " + catalogFile);
				loader.save(loader.load(catalogFile));
				logger.info("Saving");
			}
		} else {
			new HelpFormatter().printHelp("MetadataLoader", options);
		}
	}
}
