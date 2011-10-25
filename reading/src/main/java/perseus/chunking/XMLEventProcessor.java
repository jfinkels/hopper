package perseus.chunking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;
import org.xml.sax.Attributes;

import perseus.document.Chunk;
import perseus.document.ChunkSchemes;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.dao.HibernateChunkDAO;
import perseus.language.LanguageCode;
import perseus.util.StringUtil;

/**
 * Base class for processing XML texts. Should be subclassed as necessary
 * to tailor processing to specific XML DOCTYPEs. In general, this only gets
 * called from ByteOffsetProcessor.
 */
public abstract class XMLEventProcessor {

	/** The ID of the document being processed */
	String documentID = null;

	/** A hash of the all chunks currently open, by type */
	Map<String,Stack<Chunk>> openChunks = new HashMap<String,Stack<Chunk>>();

	/** A count of each type of chunk seen so far */
	Map<String,Integer> typeCounts = new HashMap<String,Integer>();
	int elementCount = 0;

	/** The current context of tags */
	Stack<String> tagContext = new Stack<String>();

	HibernateChunkDAO dao = new HibernateChunkDAO();

	/** The last chunk we processed */
	Chunk mostRecentChunk = null;
	StringBuffer currentHead = null;

	boolean outputSuppressed = false;
	boolean customHeadForChunk = false;
	boolean createdCurrentHead = false;

	private Document documentTree = new Document();
	private Parent currentElement = null;

	/** Keeps track of all the chunks that represent a given XML element */
	private Map<Parent,List<Chunk>> chunksForElements = new HashMap<Parent,List<Chunk>>();

	private String headLanguage = null;

	private static Logger logger = Logger.getLogger(XMLEventProcessor.class);
	
	private Semaphore chunkSem;

	public XMLEventProcessor (String documentID, Semaphore chunkSem) {
		this.documentID = documentID;
		//chunkSem should never be null, but check just in case
		if (chunkSem == null) {
			this.chunkSem = new Semaphore(1);
		} else {
			this.chunkSem = chunkSem;
		}

		try {
			this.chunkSem.acquire();
		} catch (InterruptedException e) {
			logger.trace("Thread interrupted");
		}
		dao.beginTransaction();
		dao.deleteByDocument(documentID);
		dao.endTransaction();
		this.chunkSem.release();
		
		Metadata metadata = new Query(documentID).getMetadata();

		headLanguage = metadata.get(Metadata.LANGUAGE_KEY);

	}

	/**
	 * Closes any chunks that have not already been closed. Called when we are
	 * finished processing the document.
	 *
	 * @param finalOffset the total number of bytes in the outputted file
	 */
	public void cleanup(int finalOffset) {
		// Close chunks that are still open
		for (String type : openChunks.keySet()) {
			Stack<Chunk> chunksToClose = openChunks.get(type);

			while (!chunksToClose.isEmpty()) {
				logger.debug("Popping chunk for type " + type);
				// and make sure to process them as end chunks
				processEndChunkEvent(type, finalOffset);
			}
		}
	}

	/**
	 * Records the start of a new chunk.
	 *
	 * @param type the type of chunk, usually from a "type" or "unit" attribute
	 * @param value value of the chunk, usually from an "n" attribute
	 * @param offset the current byte offset
	 */
	public void processStartChunkEvent(String type, String value, int offset) {
		processStartChunkEvent(type, value, null, offset);
	}

	/**
	 * Records the start of a new chunk.
	 *
	 * @param type the type of chunk, usually from a "type" or "unit" attribute
	 * @param value value of the chunk, from an "n" attribute
	 * @param id the unique document-wide id of the chunk, from an "id"
	 * attribute, if one exists
	 * @param offset the current byte offset
	 */
	public void processStartChunkEvent(String type, String value, String id,
			int offset) {

		Chunk chunk = new Chunk();
		chunk.setDocumentID(documentID);
		chunk.setOpenTags(getOpenTagContext());
		// (we set the type below)
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");

		chunk.setValue(value);
		chunk.setChunkID(id);
		chunk.setStartOffset(offset);
		chunk.setHasCustomHead(false);
		if (value.indexOf(":") != -1 && id != null) {
			chunk.setDisplayQuery(value);
		}
		chunk.setHead(ChunkSchemes.getDisplayName(type) + " " + value);
		chunk.setHeadLanguage(LanguageCode.ENGLISH);

		if (canTakeHeader(type)) {
			mostRecentChunk = chunk;
		}

		// There is some variation in chunk type capitalization:
		//  you sometimes get "Book" for "book", "Chapter" for "chapter", 
		//  and so forth. We want to preserve that in the table, but make
		//  sure that we process chunk events correctly.
		String normalizedType = type.toLowerCase();

		Stack<Chunk> chunks = null;
		if (openChunks.containsKey(normalizedType)) {
			chunks = openChunks.get(normalizedType);
		}
		else {
			chunks = new Stack<Chunk>();
			openChunks.put(normalizedType, chunks);
		}
		chunk.setType(normalizedType);
		chunks.push(chunk);

		if (typeCounts.containsKey(normalizedType)) {
			typeCounts.put(normalizedType, typeCounts.get(normalizedType)+1);
		} else {
			typeCounts.put(normalizedType, 1);
		}
		elementCount++;

		chunk.setPosition(typeCounts.get(normalizedType));
		chunk.setAbsolutePosition(elementCount);
	}

	/**
	 * Records the end of a chunk.
	 *
	 * @param type the type of the chunk
	 * @param offset the current byte offset
	 */
	public void processEndChunkEvent(String type, int offset) {
		Chunk chunk = popOpenChunk(type);

		if (chunk != null) {
			chunk.setCloseTags(getCloseTagContext());
			chunk.setEndOffset(offset);

			try {
				chunkSem.acquire();
			} catch (InterruptedException e) {
				logger.trace("Thread interrupted");
			}
			dao.save(chunk);
			chunkSem.release();
		}
	}

	/**
	 * Records the start of a tag that may or may not correspond to a chunk.
	 * Subclasses may call processStartChunkEvent() or one of the other
	 * process-start methods from this method.
	 *
	 * @param qName the tag's qualified name
	 * @param attrs the tag's SAX attributes
	 * @param tagText the text of the tag
	 * @param offset the current byte offset
	 */
	public void processStartTagEvent(String qName, Attributes attrs,
			String tagText, int offset) {
		tagContext.push(tagText);
	}

	/**
	 * Records the end of a tag that may or may not correspond to a chunk.
	 * Subclasses may call processEndChunkEvent() or one of the other
	 * process-end methods from this method.
	 *
	 * @param qName the tag's qualified name
	 * @param offset the current byte offset
	 */
	public void processEndTagEvent(String qName, int offset) {

		if (tagContext.size() > 0) {
			tagContext.pop();
		}
		else {
			logger.warn("context stack EMPTY!!");
		}
	}

	/**
	 * Suppresses further output, until a call to stopSuppressingOutput(). Can
	 * be used for ignoring certain undesirable elements.
	 */
	public void startSuppressingOutput() {
		outputSuppressed = true;
	}

	/**
	 * Stops suppressing output.
	 */
	public void stopSuppressingOutput() {
		outputSuppressed = false;
	}

	/**
	 * Records the start of a header tag.
	 *
	 * @param language the language of the tag, usually from a "lang" attribute
	 */
	public void processStartHeaderEvent(String language) {
		currentHead = new StringBuffer();
		if (language != null) {
			headLanguage = language;	    
		}
	}

	/**
	 * Returns a version of `attributes`, possibly with changes applied.
	 */
	public Attributes preprocessAttributes(String namespaceURI,
			String sName,
			String qName,
			Attributes attributes) {
		return attributes;
	}

	/**
	 * Records the end of a header tag.
	 */
	public void processEndHeaderEvent() {
		// Watch out for empty HEAD tags!  This is a horrendous hack, but
		// there isn't any reasonable way to check whether we've read actual
		// text or just an empty tag.
		if (currentHead != null && (currentHead.length() == 0 ||
				currentHead.toString().equals("<head />"))) {

			return;
		}
		if (currentHead == null) { return; }

		// if chunk already has a customized head, don't replace it (i.e., prefer the first <head> or 
		// <orth> tag to the last one.
		if ((!outputSuppressed) && (mostRecentChunk != null) && !mostRecentChunk.getHasCustomHead()) {

			if (!mostRecentChunk.getHasHead()) {
				String taglessHead =
					currentHead.toString().replaceAll("</?head.*?>", "");
				mostRecentChunk.setHead(
						StringUtil.insertNumericEntities(taglessHead));
				mostRecentChunk.setHasHead(true);
			}
			if (headLanguage != null) {
				mostRecentChunk.setHeadLanguage(headLanguage);
			}
			mostRecentChunk.setHasCustomHead(true);
		}
		currentHead = null;
	}

	public void processEmit(String s) {
		if (currentHead != null) {
			currentHead.append(s);
		}
	}

	/**
	 * Returns a string representing all the currently open XML tags, starting
	 * with the one highest in the hierarchy. Output will be something like
	 * like "&lt;TEI.2&gt;&lt;body&gt;&lt;page n="1"&gt;".
	 */
	public String getOpenTagContext() {
		StringBuffer output = new StringBuffer();
		for (String tag : tagContext) {
			output.append("<")
			.append(tag)
			.append(">");
		}

		return output.toString();
	}

	/**
	 * Returns a string representing the close tags for all the currently
	 * open tags, starting with the one lowest in the hierarchy.
	 */
	public String getCloseTagContext() {
		StringBuffer output = new StringBuffer();
		Iterator tags = tagContext.iterator();
		while (tags.hasNext()) {
			String tag = (String) tags.next();
			String tagTokens[] = tag.split("\\s+");
			output.insert(0, "</" + tagTokens[0] + ">");
		}

		return output.toString();
	}

	/**
	 * Returns a most-recently opened chunk of type `type`, or null if no
	 * such chunk has been opened.
	 */
	public Chunk getOpenChunk (String type) {
		String normalizedType = type.toLowerCase();

		if (openChunks.containsKey(normalizedType)) {
			Stack<Chunk> chunks = openChunks.get(normalizedType);
			if (chunks.size() > 0) {
				return chunks.peek();
			}
		}

		return null;
	}

	/**
	 * Returns an open chunk of type `type` from the stack and removes it;
	 * returns null if there is no such chunk.
	 */
	public Chunk popOpenChunk (String type) {
		String normalizedType = type.toLowerCase();

		if (openChunks.containsKey(normalizedType)) {
			Stack<Chunk> chunks = openChunks.get(normalizedType);
			if (chunks.size() > 0) {
				return chunks.pop();
			}
		}

		return null;
	}

	/**
	 * Returns a JDOM document tree representing what has been chunked so far.
	 * Note that this will return null if we are not building a document tree.
	 */
	public Document getDocumentTree() {
		return documentTree;
	}

	/**
	 * Returns all the chunks corresponding to the JDOM element `element`.
	 */
	public List<Chunk> chunksForElement(Element element) {
		if (chunksForElements.containsKey(element)) {
			return chunksForElements.get(element);
		}

		return new ArrayList<Chunk>();
	}

	/**
	 * Returns all the chunks that correspond to any element in a list of JDOM
	 * nodes.
	 */
	public List<Chunk> chunksFromNodes(List nodes) {
		List<Chunk> chunks = new ArrayList<Chunk>();

		for (Object node : nodes) {
			if (!(node instanceof Element)) continue;

			Element element = (Element) node;
			chunks.addAll(chunksForElement(element));
			chunks.addAll(chunksFromNodes(element.getChildren()));
		}

		return chunks;
	}

	/**
	 * Returns true if a chunk of type `chunkType` can have a custom header,
	 * as determined by the processor.
	 * In general, chunks of type `section` and `chapter` and the like
	 * can take headers, while chunks like `page` cannot.
	 */
	public abstract boolean canTakeHeader(String chunkType);
}
