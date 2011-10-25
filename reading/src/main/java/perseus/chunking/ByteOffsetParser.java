package perseus.chunking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import perseus.document.Chunk;
import perseus.document.ChunkSchemes;
import perseus.document.Metadata;
import perseus.document.MetadataCache;
import perseus.document.Query;
import perseus.document.TableOfContents;
import perseus.document.TableOfContentsFactory;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateTableOfContentsDAO;
import perseus.document.dao.TableOfContentsDAO;
import perseus.ie.freq.EntityTuple;
import perseus.ie.freq.dao.HibernateFrequencyDAO;

/**
 * Responsible for chunking XML source texts and outputting versions of the
 * texts with all entities expanded, recording starting and ending byte
 * offsets for the document's sections along the way. This class handles
 * reading and writing the XML and outputting byte offsets; it delegates
 * processing of individual chunks to an {@link XMLEventProcessor} class,
 * which will vary depending on the document format. After the document has
 * been fully processed and divided into chunks, we create the tables of
 * contents for the document, as specified by the document's chunk schemes.
 * After this, the document is ready to be viewed or to have other corpus-wide
 * operations peformed on it.
 *
 * The only prerequisite for chunkifying a document is that its metadata
 * be loaded into the database, as performed by the MetadataLoader class.
 *
 * This class can be called directly, but it's probably easier to use the
 * CorpusChunkifier class, which extends the CorpusProcessor class and can
 * more easily chunkify large numbers of documents.
 *
 * @see XMLEventProcessor
 * @see TableOfContentsFactory
 * @see CorpusChunkifier
 */

public class ByteOffsetParser {

	/** Used to look up XML entities */
	public static final CatalogResolver resolver = new CatalogResolver();

	private Metadata currentMetadata;

	/** An availability statement that we inject into the headers of files
	 * that are to be made available for download. */
	private static String availabilityStatement;
	private static final String AVAILABILITY_FILENAME = "rights.fragment.xml";

	// Does the given document already have an availability tag? If it does,
	// we shouldn't add one.
	private boolean hasAvailabilityStatement = false;

	private static Logger logger = Logger.getLogger(ByteOffsetParser.class);
	
	private Semaphore tocSem, chunkSem, freqSem;
	private String documentID, infile, outfile;

	/**
	 * Creates a ByteOffsetParser that parses the specified document.
	 *
	 * @param documentID the ID of the target document
	 * @param infile the path to the original source text
	 * @param outfile the destination of the processed text
	 */
	public ByteOffsetParser (String documentID, String infile, String outfile, Semaphore tocSem, Semaphore chunkSem, Semaphore freqSem) {
		//now we can lock when modifying the db
		this.tocSem = tocSem;
		this.chunkSem = chunkSem;
		this.freqSem = freqSem;

		this.documentID = documentID;
		this.infile = infile;
		this.outfile = outfile;

		this.currentMetadata = new Query(documentID).getMetadata();
	}

	public void parseDocument() {
		try {
			// Don't assume the directory for the target file already exists!
			// If it doesn't, try to create it.
			File targetDirectory = new File(outfile).getParentFile();
			if (!targetDirectory.exists()) {
				boolean gotDirectory = targetDirectory.mkdirs();
				if (!gotDirectory) {
					logger.fatal("Unable to create directory " +
							targetDirectory + "; quitting...");

					// We can't write to a file--get out of here
					throw new Exception("Could not write to file " + outfile);
				}
			}

			deleteChunksFromTables();

			TableOfContentsDAO tocDAO = new HibernateTableOfContentsDAO();

			deleteTocs(tocDAO);

			PrintWriter out = 
				new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF-8")));

			XMLEventProcessor eventProcessor = new TEIP4Processor(documentID, this.chunkSem);

			EchoHandler handler = new EchoHandler(out, eventProcessor);

			XMLReader parser = XMLReaderFactory.createXMLReader();
			// DEAR SIR:
			// if you ever get around to using the transcoder, beware:
			// using a filter may cause the ampersands, which we're
			// diligently spitting out as "&amp;", to get swallowed up again
			// and appear as "&" in the XML files.
			//XMLFilter transcoderFilter =
			//new XMLTranscoderFilter(parser,
			//currentMetadata.get(Metadata.LANGUAGE_KEY),
			//null);

			parser.setContentHandler(handler);
			parser.setEntityResolver(resolver);

			parser.parse(infile);

			new EntryGroup(this.chunkSem).addEntryGroups(documentID);			

			eventProcessor.cleanup(handler.getCurrentByte());
			out.close();

			buildAndSaveTocs(tocDAO);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void buildAndSaveTocs(TableOfContentsDAO tocDAO)
			throws InterruptedException {
		logger.debug("Building tables of contents");

		List<TableOfContents> tocs = TableOfContentsFactory.build(new Query(documentID));
		tocSem.acquire();
		tocDAO.beginTransaction();
		for (TableOfContents toc : tocs) {
			logger.debug(toc);
			tocDAO.save(toc);
		}
		tocDAO.endTransaction();
		tocSem.release();

		for (String subdoc : currentMetadata.getList(Metadata.SUBDOC_REF_KEY)) {
			logger.debug("Building TOC for subdoc: " + subdoc);
			Query subdocQuery = new Query(documentID, subdoc);

			logger.debug(subdocQuery);
			List<TableOfContents> subdocTOCs = TableOfContentsFactory.build(subdocQuery);

			tocSem.acquire();
			tocDAO.beginTransaction();
			for (TableOfContents subtoc : subdocTOCs) {
				tocDAO.save(subtoc);
			}
			tocDAO.endTransaction();
			tocSem.release();
		}
	}

	private void deleteTocs(TableOfContentsDAO tocDAO)
			throws InterruptedException {
		logger.debug("Clearing tocs...");
		tocSem.acquire();
		tocDAO.beginTransaction();
		tocDAO.deleteByDocument(documentID);
		tocDAO.endTransaction();
		tocSem.release();
	}

	private void deleteChunksFromTables() {
		//delete all associated frequencies since the chunk ids for this document will no longer be valid
		if (currentMetadata.getLanguage().getHasMorphData()) {
			List<Integer> chunkIDs = new HibernateChunkDAO().getAllChunkIDs(documentID);
			logger.debug("size of chunksIDs is "+chunkIDs.size());
			
			if (!chunkIDs.isEmpty()) {
				logger.debug("Clearing chunks from other tables...");
				//just delete freqs that don't have a null chunk id, we'll delete
				//freqs based on document id in WordFrequencyLoader
				try {
					freqSem.acquire();
				} catch (InterruptedException e) {
					logger.trace("Thread interrupted");
				}
				//handle really large texts in smaller sublists so we don't get a stack overflow
				List<Integer> subChunkIDs;
				HibernateFrequencyDAO fDAO = new HibernateFrequencyDAO(EntityTuple.class);
				for (int i = 0; i < chunkIDs.size(); i+=5000) {
					subChunkIDs = chunkIDs.subList(i, Math.min(chunkIDs.size(), i+5000));
					fDAO.deleteByChunkIDs(subChunkIDs);
				}
				freqSem.release();
			}
			
		}
	}

	/**
	 * Parses the given text, determining the location of the output file from
	 * the supplied document ID.
	 *
	 * @param id the target document ID
	 * @param sourcePath the path to the source text
	 */
	public static void parse(String id, String sourcePath, Semaphore tocSem, Semaphore chunkSem, Semaphore freqSem)
	throws IOException {
		File destination = new File(Chunk.getFilename(id));

		File tempTarget = null;
		try {
			File directory = new File("/sgml/reading");
			//use id so that different threads never create the same temp file name
			//which could cause problems with multiple threads
			tempTarget = File.createTempFile(id, "xml", directory);
		} catch (IOException e) {
			logger.fatal("Could not create chunk temp file", e);
			System.exit(0);
		}

		if (sourcePath == null) {
			Metadata meta = MetadataCache.get(new Query(id));
			sourcePath = meta.getSourceFile().getAbsolutePath();
		}

		ByteOffsetParser bop = new ByteOffsetParser(id, sourcePath, tempTarget.toString(), tocSem, chunkSem, freqSem);
		bop.parseDocument();

		if (!tempTarget.renameTo(destination)) {
			logger.error("Did not create "+destination);
		}
	}

	private static List<Chunk> getChunksForScheme(List<Chunk> chunkList, String scheme) {
		List<Chunk> schemeChunks = new ArrayList<Chunk>();

		List<String> types = ChunkSchemes.typesForScheme(scheme);
		for (Chunk chunk : chunkList) {
			for (String type : types) {
				if (type.equalsIgnoreCase(chunk.getType())) {
					schemeChunks.add(chunk);
					break;
				}
			}
		}

		return schemeChunks;
	}

	/**
	 * A SAX handler class that merely echoes the input it receives and keeps
	 * a count of the current byte offset. The SAX classes provide no inherent
	 * way to figure out the current byte offset, but we can imitate it by
	 * rendering the XML ourselves and counting the number of bytes we've
	 * outputted.
	 */
	public class EchoHandler extends DefaultHandler {
		PrintWriter out = null;

		String openTagQName = null;
		String openTagText = null;

		int currentByte = 0;

		XMLRenderer renderer;

		Stack<String> contextTags;

		XMLEventProcessor chunkProcessor = null;

		public EchoHandler (PrintWriter out, XMLEventProcessor proc) {
			this.out = out;
			contextTags = new Stack<String>();
			chunkProcessor = proc;
			renderer = new XMLRenderer();
		}

		public int getCurrentByte() {
			return currentByte;
		}

		private void emit (String s) throws SAXException {
			chunkProcessor.processEmit(s);

			try {
				byte[] bytes = s.getBytes("UTF-8");
				currentByte += bytes.length;
			} catch (UnsupportedEncodingException uee) {
				// unlikely
			}

			try {
				out.write(s);
				out.flush();
			} catch (Exception ioe) {
				throw new SAXException("IOException", ioe);
			}
		}

		/** This method allows us to hold open tag events in reserve until
	 we're sure they aren't part of an empty tag */
		private void handleOpenTag() throws SAXException {
			if (openTagQName == null) {
				// No pending open tag
				return;
			}

			emit(renderer.startElementFromText(openTagText));
			openTagQName = null;
			openTagText = null;
		}

		public void startElement(String namespaceURI,
				String localName,
				String qName,
				Attributes attributes) throws SAXException {
			handleOpenTag();

			// Make sure not to add our availability statement if this
			// document already has one
			if (qName.equals("availability")) {
				hasAvailabilityStatement = true;
				logger.debug("Found existing availability statement");
			}

			attributes = chunkProcessor.preprocessAttributes(
					namespaceURI, localName, qName, attributes);

			StringBuffer tag = new StringBuffer();

			tag.append(renderer.renderElementText(
					namespaceURI, localName, qName, attributes));

			openTagQName = qName;
			openTagText = tag.toString();

			chunkProcessor.processStartTagEvent(qName, attributes,
					openTagText, currentByte);

			// eThis may be a bodyless tag (e.g. <pb />),
			// so don't emit it yet.

			contextTags.push(openTagText);
		}

		public void endElement(String namespaceURI,
				String localName,
				String qName) throws SAXException {

			contextTags.pop();

			if (qName.equals("publicationStmt") && !hasAvailabilityStatement) {
				// Add appropriate availability information to the chunked XML
				// file--similar to what shows up on the text page, but for
				// people who download the whole text. It'd be nicer to do this
				// in TEI-specific code, but the XMLEventProcessors don't have
				// any access to the actual file being written.
				String rights = currentMetadata.get(Metadata.RIGHTS_KEY);
				String status = currentMetadata.get(Metadata.STATUS_KEY);
				if ((rights == null || !rights.equals("nodownload")) &&
						(status == null || !status.equals("3"))) {
					logger.debug("Adding availability statement...");
					addAvailabilityStatement();
					hasAvailabilityStatement = true;
				}
			}

			if (openTagQName != null &&
					openTagQName.equals(qName)) {
				// Empty tag -- print it as <tag type="empty" />
				emit(renderer.emptyElementFromText(openTagText));
				// reset open tag
				openTagQName = null;
				openTagText = null;
			}
			else {
				// clean up any open tag that isn't coordinate with this one,
				// which isn't really possible...
				handleOpenTag();

				// Construct the tag and emit it.

				emit(renderer.endElementFromText(qName));
			}

			chunkProcessor.processEndTagEvent(qName, currentByte);

		}

		public void characters(char[] chars, int startIndex, int length) 
		throws SAXException {
			handleOpenTag();
			emit(renderer.renderCharacters(chars, startIndex, length));
		}

		public void ignorableWhitespace(char[] chars, int startIndex, int length) 
		throws SAXException {
			handleOpenTag();

			emit(renderer.renderIgnorableWhitespace(
					chars, startIndex, length));
		}

		public void processingInstruction(String target, String data) {
			logger.info("processing INST: " + target + ", " + data);
		}

		private void addAvailabilityStatement() {
			// If you *do* change the wording of this statement, be sure to
			// change the wording in text.jsp accordingly.

			if (availabilityStatement == null) {
				try {
					FileInputStream stream =
						new FileInputStream(AVAILABILITY_FILENAME);
					int bytesToRead = stream.available();
					byte[] bytes = new byte[bytesToRead];
					stream.read(bytes);
					availabilityStatement = new String(bytes);
					stream.close();
				} catch (IOException ioe) {
					logger.warn("Couldn't read availability statement", ioe);
				}
			}

			String stmtWithFunder = availabilityStatement;
			String funder = currentMetadata.get(Metadata.FUNDER_KEY);
			if (funder != null) {
				stmtWithFunder = availabilityStatement.replaceAll(
						"\\$FUNDER\\$", ", with funding from " + funder);
			} else {
				stmtWithFunder =
					availabilityStatement.replaceAll("\\$FUNDER\\$", "");
			}

			try {
				logger.debug("Adding funder information...");
				emit(stmtWithFunder);
			} catch (SAXException se) {
				logger.warn("Couldn't emit funder information", se);
			}
		}
	}

}
