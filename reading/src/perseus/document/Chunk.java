/**
 * Represents a "chunk" of a file, including starting and ending offsets, a
 * header, and the actual text of the chunk. A chunk can contain other chunks,
 * methods for accessing which are provided.
 * 
 * This class, along with the Query and Metadata classes, constitutes the core
 * of the hopper.  Unfortunately, it currently has a couple nontrivial issues:
 * 
 * <ul>
 * <li>A Chunk often has values set for its start and end offsets, but not
 * always; the only reliable way to check is to see whether they equal -1.
 * If the offsets are not set to actual values, it's impossible to load the
 * chunk's text, and the chunk becomes little more than a wrapper around a
 * Query. There are a great many methods of accessing chunks, and it is not
 * necessarily obvious which methods yield which sorts of chunks.</li>
 * <li>The Chunk and Query classes are tied alarmingly closely to one another.
 * A chunk, ideally, would be entirely ignorant of whatever query was used to
 * retrieve it, since the same chunk could correspond to different queries
 * (depending on which chunk scheme is being used), and rely on an associated
 * TableOfContents class to convey its location in the document. Unfortunately, a chunk is
 * expected to be able to return the query that spawned it.</li>
 * <li>A Chunk is used in many places to represent an entire document (see the
 * {@link CorpusProcessor} class). The text of the document is not itself
 * used, but a Chunk is used as the type anyway). There should be a distinct
 * Document class for such occasions (well, there <em>is</em> a Document class,
 * but its functionality is wholly unrelated).</li>
 * </ul>
 * 
 * Throughout the latter half of 2006, the internals of the class (along with
 * those of the {@link TableOfContents} class) were modified to use Hibernate
 * and gradually reworked, but no attempt was made at addressing the above
 * issues, mainly because the Chunk and Query classes are entrenched deeply
 * enough in the system that modifying them would be a decidedly punishing
 * endeavor.
 * 
 * There are several methods that callers may be interested in:
 * <ul>
 * <li><code>getInitialChunk(Query query)</code> -- returns a Chunk that wraps
 * the document or subdocument referred to by <code>query</code>, ignoring
 * any part of the query that isn't part of the [sub]document ID.</li>
 * <li><code>getTextlessChunk()</code> and <code>getChunk()</code> in the
 * {@link Query} class -- these query the database for the chunk associated
 * with the given Query; they throw an InvalidQueryException if they cannot
 * find an appropriate chunk.</li>
 * <li><code>getText()</code> -- retrieves the text for this chunk (if it has
 * its start and end offsets set!)</li>
 * <li><code>style()</code> -- styles the chunk's text using the given
 * stylesheet.</li>
 * <li><code>getQuery()</code> -- returns the Query associated with this
 * chunk.</li>
 * <li><code>getMetadata()</code> -- returns the metadata associated with
 * the chunk (equivalent to <code>getQuery().getMetadata()</code>).</li>
 * <li><code>getHead()</code> -- gets the chunk's header, which will be
 * the contents of the first &lt;head&gt; tag in the source text, if there
 * is one, or else something like "chapter 5"</li>
 * <li><code>getTokens()</code> -- returns this chunk's contents as a
 * {@link TokenList}</li>
 * </ul>
 * 
 * @see Document
 * @see Query
 * @see TableOfContents
 * @see Metadata
 * @see TokenList
 */
package perseus.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.ie.freq.Frequency;
import perseus.language.LanguageCode;
import perseus.morph.Lemma;
import perseus.util.Config;

public class Chunk implements Comparable<Chunk> {
    
    private static final Pattern TYPE_VALUE_PATTERN =
	Pattern.compile("([^=]*)=([^:]*):?");
    
    // This comparator is used in getSubTexts() to sort the subtexts by
    // offset
    public static final Comparator<Chunk> offsetComparator =
	new Comparator<Chunk>() {
	public int compare(Chunk c1, Chunk c2) {
	    return c1.getStartOffset() - c2.getStartOffset();
	}
    };

    private static final Pattern EFFECTIVE_LANGUAGE_PATTERN =
	Pattern.compile("lang=\"(\\w*)\"");

    private static Logger logger = Logger.getLogger(Chunk.class);
    
    /** The internal ID for the chunk, used by Hibernate */
    private Integer idNumber = null;
    
    /** The text of the chunk --normally just returned by combining
     * openTags, closeTags and innerText, but we cache a copy here
     * if it's modified (by, e.g., the style() method) */
    private String cachedText = null;
    
    /** The chunk's metadata */
    private Metadata metadata = null;
    
    /** The query referred to by this chunk */
    private Query query = null;
    
    /** The next chunk in the hierarchy this chunk belongs to */
    private Chunk next = null;
    
    /** The last chunk in the hierarchy this chunk belongs to */
    private Chunk previous = null;
    
    /** This chunk's parent in the hierarchy */
    private Chunk parent = null;
    
    /** This chunk's starting byte offset in its source file */
    private int startOffset = -1;
    
    /** This chunk's ending byte offset in its source file */
    private int endOffset = -1;
    
    /** The header of the chunk */
    private String head = null;

    /** XML texts can have multiple head elements
     * in a chunk, use the first head element
     * for the TOC
     */
    private boolean hasHead = false;
    
    /** The language of the header
     * (by default, {@link LanguageCode.ENGLISH}) */
    private String headLanguage = null;
    
    /** This chunk's text as a series of tokens */
    private TokenList tokens = null;
    
    /** All the tags in the source text that we need to prepend to the chunk's
     * text to make it valid XML
     */
    private String openTags = null;
    
    /** All the tags in the source text that we need to append to the chunk's
     * text to make it valid XML
     */
    private String closeTags = null;
    
    /** The name of the element representing this chunk in the source text,
     * like "milestone" or "div1"
     */
    private String elementName = null;
    
    /** The type of the element, like "chapter" or "section" */
    private String type = null;
    
    /** The value of the element, usually represented by the "n" attribute */
    private String value = null;
    
    /** The "id" attribute of the element, if there is one */
    private String chunkID = null;
    
    /** An attribute used for certain chunks whose "n" attributes are values
     * representing entire subqueries, as in some of the Cicero letters.
     */
    private String displayQuery = null;
    
    /** The count of previous occurrences of elements with the same name in
     * the source file, plus one. Used to resolve ambiguity when there are two
     * chunks with the same type and value.
     */
    private int position = -1;
    
    /** The count of previous occurrences of any chunkable elements in the
     * source file, plus one.
     */
    private int absolutePosition = -1;
    
    /** Does this chunk have an interesting header, or is it an
     * automatically-generated one like "chapter 4"?
     */
    private boolean hasCustomHead = false;
    
    /**
     * Used to create chunk hierarchies, in cooperation with the
     * {@link TableOfContents} class.
     */
    private Set<Chunk> containedChunks = new TreeSet<Chunk>();
    
    private List<Chunk> subtextChunks = null;

    /** Holds this chunk's actual text, unenclosed by the opening and closing
     * tags that getText() returns it with.
     */
    private String innerText = null;
    
    /**
     * The lemma this chunk refers to, if it's a lexicon entry
     */
    private Lemma lemma = null;
    
    /**
     * Any frequencies concerning this chunk
     */
    private Set<Frequency> frequencies = null;
    
    public Set<Frequency> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(Set<Frequency> frequencies) {
        this.frequencies = frequencies;
    }

    public Chunk() {}
    
    /**
     * Creates a new Chunk from the given Query, with no offset information
     * specified or text loaded.
     * 
     * @param q the referring query
     */
    public Chunk(Query q) {
	query = q;
	setDocumentID(q.getDocumentID());
	setType(q.getLastElementType());
	setValue(q.getLastElementValue());
	
	metadata = q.getMetadata();
    }
    
    public Chunk(String type, String value, int start, int end) {
	setType(type);
	setValue(value);
	setStartOffset(start);
	setEndOffset(end);
    }

    /**
     * Returns a chunk corresponding to the document (or subdocument)
     * represented by the query, stripping off any subquery information. If
     * <kbd>q</kbd> references part of a subdocument, the resulting chunk will
     * have <kbd>startOffset</kbd> and <kbd>endOffset</kbd> set; if <kbd>q</kbd>
     * references a whole document, the resulting chunk's offsets will be set to -1.
     * 
     * @param q the referring query 
     * @return a chunk representing the query
     */
    public static Chunk getInitialChunk(Query q) {
	if (q.isJustDocumentID()) {
	    return new Chunk(q);
	} else if (q.isSubDocument()) {
	    return Chunk.getInitialSubdocChunk(q);
	} else if (q.getMetadata().has(Metadata.SUBDOC_QUERY_KEY)) {
	    Query subdocQuery = new Query(q.getInnermostDocumentID());
	    return getInitialSubdocChunk(subdocQuery);
	} else {
	    Query documentIDQuery = new Query(q.getDocumentID());
	    return new Chunk(documentIDQuery);
	}
    }
    
    /**
     * Returns a chunk that can be used to call
     * {@link TableOfContents.forChunk()}. Almost identical to
     * <code>getTOCChunk()</code>, but handles texts with subdocuments
     * differently.
     * 
     * This is called internally by the {@link TableOfContents} class; other
     * callers should never need to use it.
     * 
     * @param q a query
     * @return a Chunk that can be used to call {@link TableOfContents.forChunk()}
     */
    public static Chunk getTOCChunk(Query q) {
	Chunk chunk = getInitialChunk(q);
	
	// If we've been given a query like "Perseus:text:1999.01.0001",
	// for a document with subtexts, don't return the TOC for the
	// document itself; return the TOC for the first of its subdocuments.
	if (q.isJustDocumentID() &&
		q.getMetadata().has(Metadata.SUBDOC_REF_KEY)) {
	    List<Chunk> subtexts = chunk.getSubTexts();
	    if (!subtexts.isEmpty()) {
		return subtexts.get(0);
	    } else {
		logger.warn("Expected subtexts for " + q  + " but found none");
	    }
	}
	
	return chunk;
    }
    
    /**
     * Returns the chunk that corresponds to the last element of a given query,
     * with all the useful information from the chunk_test table loaded. We
     * *cannot* simply get a subdocument by instantiating a new Chunk as we do
     * for documents and text chunks--if we do so, the subdocument chunk will
     * lack some necessary information, like its start offset and end offset,
     * because we won't have retrieved the information from the database (and
     * hence, if we try to get the subquery's "containing chunk," we won't get
     * what we want).
     *
     * The method is so named because it's intended to be used with queries
     * that represent subdocuments, though it should work with other queries as
     * well.
     * 
     * @param q a query representing a subdocument
     * @return a chunk for the query, with offset information loaded
     */
    public static Chunk getInitialSubdocChunk(Query q) {
	
	String queryString = q.getQuery();

	Matcher matcher = TYPE_VALUE_PATTERN.matcher(queryString);
	
	String type = null;
	String value = null;
	
	String defaultType = q.getMetadata().getChunkSchemes().getDefaultChunk();

	Query documentIDQuery = new Query(q.getDocumentID());
	
	Chunk parentChunk = new Chunk(documentIDQuery);
	Chunk childChunk = null;
	
	while (matcher.find() && parentChunk != null) {
	    type = matcher.group(1);
	    value = matcher.group(2);

	    childChunk = parentChunk.getContainedChunk(type, value);
	    if (childChunk == null) {
		logger.warn("NULL chunk in getInitialSubdocChunk: "
			+ " type " + type + ", value " + value);
	    }
	    parentChunk = childChunk;
	    
	    if (type.equals(defaultType)) {
		break;
	    }
	    
	}
	
	if (childChunk != null) {
	    childChunk.setQuery(q);
	}
	
	return childChunk;
    }
    
    /**
     * Returns the text at the given points in the given document. Callers, in
     * general, will want to call <kbd>getText()</kbd> on a specific chunk
     * instead, which returns the chunk's text with the opening and closing
     * tags circumpended.
     * 
     * @param documentID the document ID representing the file to access
     * @param startOffset the starting offset
     * @param endOffset the ending offset
     * @return the text at the given points from the given document
     */
    public static String getText(String documentID, int startOffset,
	    int endOffset) {
	String output = null;
	
	//Charset cs = Charset.forName("ISO-8859-1");
	Charset cs = Charset.forName("UTF-8");
	CharsetDecoder decoder = cs.newDecoder();
	FileInputStream stream = null;
	
	// open the xml file and prepare our stream
	File xmlFile = new File(getFilename(documentID));
	try {
	    stream = new FileInputStream(xmlFile);
	    
	    // set up our character buffers
	    FileChannel channel = stream.getChannel();
	    MappedByteBuffer byteBuf =
		channel.map(FileChannel.MapMode.READ_ONLY, startOffset,
			(endOffset - startOffset));
	    CharBuffer cBuf = decoder.decode(byteBuf);
	    
	    output = cBuf.toString();
	    
	    // close our FileInputStream
	    stream.close();
	    
	} catch (FileNotFoundException fnf) {
	    logger.error("Couldn't find file with chunk text", fnf);
	} catch (CharacterCodingException cce) {
	    logger.error("character coding wrong in getText()", cce);
	} catch (IOException ioe) {
	    logger.error("Problem retrieving chunk text", ioe);
	} 	
	
	return output;
    }
    
    /**
     * Returns the absolute path to the file represented by the given chunk.
     * 
     * @param chunk a chunk from the document whose path we want
     * @return the absolute path to the file represented by the chunk
     */
    public static String getFilename(Chunk chunk) {
	String documentID = chunk.getQuery().getDocumentID();
	return getFilename(documentID);
    }
    
    /**
     * Returns the absolute path to the file represented by the given
     * document ID.
     * 
     * @param documentID the ID of the document whose path we want
     * @return the absolute path
     */
    public static String getFilename(String documentID) {
	String filePath = Config.getFilePath();
	
	return new File(filePath, getPathToText(documentID))
	.getAbsolutePath();
    }
    
    /**
     * Returns the path to the file represented by this document ID *within*
     * the texts directory--1999.01/1999.01.0001.xml, for example. Use
     * getFilename() to get an absolute path.
     * 
     * @param documentID the ID of the document whose path we want
     * @return a relative path to the document
     */
    public static String getPathToText(String documentID) {
	String[] documentIDFields = documentID.split(":");
	
	if (documentIDFields.length < 3) {
	    throw new IllegalArgumentException("Bad documentID: " + documentID);
	}
	
	documentID = documentIDFields[2];
	
	String[] numericIDFields = documentID.split("\\.");
	
	if (numericIDFields.length < 3) {
	    throw new IllegalArgumentException("Bad documentID: " + documentID);
	}
	
	StringBuffer filename = new StringBuffer();
	
	// For texts from other providers, like the Stoa, use a separate
	// namespace in the texts directory: Stoa/text/2001.01.0001.xml
	if (!documentIDFields[0].equals("Perseus")) {
	    filename.append(documentIDFields[0]);
	    filename.append(File.separator);
	    filename.append(documentIDFields[1]);
	} else {
	    // Otherwise, use the normal directory: 1999.01/1999.01.0001.xml
	    filename.append(numericIDFields[0]);
	    filename.append(".");
	    filename.append(numericIDFields[1]);
	}
	
	filename.append(File.separator);
	filename.append(numericIDFields[0]);
	filename.append(".");
	filename.append(numericIDFields[1]);
	filename.append(".");
	filename.append(numericIDFields[2]);
	filename.append(".xml");
	
	return filename.toString();
    }
    
    /**
     * Returns this chunk's text as a {@link TokenList}.
     * 
     * @return our text as a list of tokens
     */
    public TokenList getTokens() {
	if (tokens == null) {
	    tokens = TokenList.getTokensFromXML(this);
	}
	return tokens;
    }
    
    /** set the tokens of this chunk to a tokenized version of a string.
     This is useful if we want to tokenize the styled text rather than 
     */
    public void setTokens(String text) {
	tokens = TokenList.getTokens(text, getMetadata().getLanguage());
    }
    
    public void setTokens(TokenList tokens) {
	this.tokens = tokens;
    }
    
    /**
     * Returns the Metadata object owned by this chunk's Query.
     */
    public Metadata getMetadata() {
	if (metadata == null) {
	    metadata = getQuery().getMetadata();
	}
	
	return metadata;
    }
    
    public Integer getId() { return idNumber; }
    public void setId(Integer i) { idNumber = i; }
    
    public String getDocumentID() {
	if (query == null) return null;
	return query.getDocumentID();
    }
    public void setDocumentID(String id) {
	query = new Query(id);
    }
    
    public Query getQuery() {
	// Fallback for chunks that don't have queries, or don't have them
	// properly set
	if (query == null ||
		(getStartOffset() != -1 && query.isJustDocumentID())) {
	    query = buildQuery();
	}
	
	return query;
    }
    
    public void setQuery(Query q) {
	query = q;
	for (Chunk child : getContainedChunks()) {
	    child.refreshQuery();
	}
    }
    
    public Chunk getNext() { return next; }
    public void setNext(Chunk c) { next = c; }
    
    public Chunk getPrevious() { return previous; }
    public void setPrevious(Chunk c) { previous = c; }
    
    public Chunk getParent() { return parent; }
    public void setParent(Chunk c) { parent = c; }
    
    public String getHead() { return head; }
    public void setHead(String h) { head = h; }

    public boolean getHasHead() { return hasHead; }
    public void setHasHead(boolean b) { hasHead = b; }

    public String getHeadLanguage() { return headLanguage; }
    public void setHeadLanguage(String hl) { headLanguage = hl; }
    
    public String getElementName() { return elementName; }
    public void setElementName(String en) { elementName = en; }
    
    public String getType() { return type; }
    public void setType(String h) { type = h; }
    
    public String getValue() { return value; }
    public void setValue(String h) { value = h; }
    
    public String getChunkID() { return chunkID; }
    public void setChunkID(String id) { chunkID = id; }
    
    public String getDisplayQuery() { return displayQuery; }
    public void setDisplayQuery(String dq) { displayQuery = dq; }
    public boolean hasDisplayQuery() { return (displayQuery != null); }
    
    public int getPosition() { return position; }
    public void setPosition(int pos) { position = pos; }

    public int getAbsolutePosition() { return absolutePosition; }
    public void setAbsolutePosition(int ap) { absolutePosition = ap; }
    
    public boolean getHasCustomHead() { return hasCustomHead; }
    public void setHasCustomHead(boolean hch) { hasCustomHead = hch; }
    
    public int getStartOffset() { return startOffset; }
    public void setStartOffset(int s) { startOffset = s; }
    
    public int getEndOffset() { return endOffset; }
    public void setEndOffset(int e) { endOffset = e; }
    
    public String getOpenTags() { return openTags; }
    public void setOpenTags(String s) { openTags = s; }
    
    public String getCloseTags() { return closeTags; }
    public void setCloseTags(String s) { closeTags = s; }
    
    /**
     * Returns the text of this chunk, loading it if necessary.
     * 
     * @return the text of the chunk
     */
    public String getText() {
	// Check for cached text, like from the style() method
	if (cachedText != null) {
	    return cachedText;
	}
	
	if (innerText == null) {
	    loadText();
	}

	return getOpenTags() + innerText + getCloseTags();
    }

    /**
     * Returns a truncated form of the cached text, adding
     * any </div> tags so it will still output nicely
     *
     * @return a truncated version of cached text
     */
    public String truncateText() {
	int maxChunkSize = 25000;
	String shortCachedText;
	if (cachedText != null && cachedText.length() > maxChunkSize) {
	    shortCachedText = cachedText.substring(0, maxChunkSize);
	    
	    Pattern divOpenPattern = Pattern.compile("<div");
	    Pattern divClosePattern = Pattern.compile("</div>");

	    String reverseShortCachedText = new StringBuffer(shortCachedText).reverse().toString();
	    String[] reverseSplit = reverseShortCachedText.split(".*<", 2);

	    if (reverseSplit.length == 2) {
		reverseShortCachedText = reverseSplit[1];
		shortCachedText = new StringBuffer(reverseShortCachedText).reverse().toString();
	    }

	    if (shortCachedText.indexOf("<p><table") >= 0 
		&& shortCachedText.indexOf("</table></p>") == -1) {
		shortCachedText = shortCachedText.concat("</table></p>");
	    }

	    Matcher divOpenMatcher = divOpenPattern.matcher(shortCachedText);
	    Matcher divCloseMatcher = divClosePattern.matcher(shortCachedText);

	    int divCount = 0;
	    while (divOpenMatcher.find()) divCount++;
	    while (divCloseMatcher.find()) divCount--;

	    for (int i = 1; i <= divCount; i++) {
		shortCachedText = shortCachedText.concat("</div>");
	    }
	    return shortCachedText;
	}
	else return getText();
    }

    public void setText(String tx) { cachedText = tx; }

    
    /**
     * Returns the size of this chunk's text. Equivalent to calling
     * <code>getEndOffset() - getStartOffset()</code>.
     * 
     * @return the size of this chunk's text
     */
    public int getSize() {
	if (getEndOffset() != -1 && getStartOffset() != -1) {
	    return getEndOffset() - getStartOffset();
	}
	
	return -1;
    }
    
    /**
     * Returns a list of subdocuments for the document this chunk represents
     * (the list may be empty).
     * 
     * @return a list of subdocuments as chunks
     */
    public List<Chunk> getSubTexts() {
	
	if (subtextChunks != null) {
	    return subtextChunks;
	}
	
	// Does the given document have any subtexts defined?
	List<String> subtextQueries = getMetadata().getList(Metadata.SUBDOC_REF_KEY);
	
	if (subtextQueries == null || subtextQueries.isEmpty()) {
	    subtextChunks = Collections.emptyList();
	    return subtextChunks;
	}
	
	subtextChunks = new ArrayList<Chunk>();
	
	// If so, step through them and grab the chunk that corresponds to
	// each subtext query.
	for (String ref : subtextQueries) {
	    Query query = new Query(getQuery().getDocumentID(), ref);
	    
	    Chunk currentChunk = getInitialSubdocChunk(query);
	    
	    // If getInitialSubdocChunk returned null, something went wrong;
	    // just forget about this subtext for now
	    if (currentChunk == null) {
		logger.warn("Error accessing subtext " + query
			+ "; skipping");
	    } else {
		subtextChunks.add(currentChunk);
	    }
	}
	
	// Sort our chunks, so that the caller sees the subdocuments in a
	// predictable order.
	Collections.sort(subtextChunks, offsetComparator);

	return subtextChunks;
    }
    
    /**
     * Returns true if this chunk represents a whole document that also
     * contains subdocuments.
     */
    public boolean hasSubTexts() {
	if (subtextChunks == null) {
	    getSubTexts();
	}
	
	return !subtextChunks.isEmpty();
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public static Chunk getChunkByDisplayQuery(String documentID,
	    String displayQuery) {
	ChunkDAO dao = new HibernateChunkDAO();
	Chunk chunk = dao.getByDisplayQuery(documentID, displayQuery);
	dao.cleanup();
	
	return chunk;
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public static Chunk getChunkByID(String documentID, String chunkID) {
	ChunkDAO dao = new HibernateChunkDAO();
	Chunk chunk = dao.getByChunkID(documentID, chunkID);
	dao.cleanup();
	
	return chunk;
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public List<Chunk> getAllChunks(String type) {
	List<String> types = new ArrayList<String>();
	types.add(type);
	return getAllChunks(types);
    }
    
    /**
     * Returns the language that this chunk should probably be rendered in.
     * Document metadata isn't always trustworthy here, since a given document
     * could have multiple languages. Checks the chunk's open tags for any
     * pertinent attributes; if it can't find any, returns the document
     * language.
     * 
     * This isn't anything close to foolproof, but it works well enough with
     * the setup we have. It's only really necessary for displaying a
     * chunk's language to the user; as far as displaying the actual text is
     * concerned, the tokenizing routines need no help handling chunks with
     * multiple languages.
     * 
     * @return the code for the language in which to render the chunk
     */
    public String getEffectiveLanguage() {
	loadText();
	
	// 1. Check the first tag of the actual text, which may have a
	// "lang" attribute.
	String firstTag = innerText.substring(0, innerText.indexOf(">"));
	Matcher tagMatcher = EFFECTIVE_LANGUAGE_PATTERN.matcher(firstTag);
	if (tagMatcher.find()) {
	    return tagMatcher.group(1);
	}
	
	// 2. If not, check the open tags, using the innermost tag with an
	// appropriate attribute.
	if (getOpenTags() != null) {
	    String openTagLanguage = null;
	    Matcher matcher = EFFECTIVE_LANGUAGE_PATTERN.matcher(getOpenTags());
	    while (matcher.find()) {
		openTagLanguage = matcher.group(1);
	    }
	    if (openTagLanguage != null) return openTagLanguage;
	}
	
	// 3. If that didn't work, just return the document language.
	return getMetadata().get(Metadata.LANGUAGE_KEY);
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public List<Chunk> getAllChunks(List<String> types) {
	return getAllChunks(types, false);
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public List<Chunk> getAllChunks(String type, boolean includeOverlapping) {
	List<String> types = new ArrayList<String>();
	types.add(type);
	return getAllChunks(types, includeOverlapping);
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public List<Chunk> getAllChunks(List<String> types, boolean includeOverlapping) {
	
	ChunkDAO dao = new HibernateChunkDAO();
	List<Chunk> results =
	    dao.getContainedChunks(this, types, includeOverlapping);
	return results;
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public Chunk getContainedChunk(String type, String value) {
	ChunkDAO dao = new HibernateChunkDAO();
	Chunk chunk = dao.getFirstContainedChunk(this, type, value);
	
	return chunk;
    }
    
    /**
     * An alias to the like-named <code>ChunkDAO</code> method. Only here for
     * back-compatibility.
     */
    public Chunk getFirstContainedChunk(String type) {
	return getContainedChunk(type, null);
    }
    
    
    /**
     * Returns all chunks contained within this chunk (they must be loaded
     * manually with <kbd>addContainedChunk()</kbd>).
     * @return a set of chunks
     */
    public Set<Chunk> getContainedChunks() {
	return containedChunks;
    }
    
    /**
     * Registers the given chunks as children of this chunk.
     * @param chunks the chunks to add
     */
    public void addContainedChunks(Collection<Chunk> chunks) {
	for (Chunk c : chunks) {
	    addContainedChunk(c);
	}
    }
    
    /**
     * Registers <code>c</code> as children of this chunk. Used when building
     * TableOfContents objects for a document.
     *
     * @param c the chunk to add
     */
    public void addContainedChunk(Chunk c) {
	c.setParent(this);
	
	// It's possible, however unlikely, that we could have multiple chunks
	// with the same type and value in the same part of the hierarchy,
	// depending on the markup of the source document. If we do, update
	// this chunk's query to use its absolute position.
	// (Leave the value property alone--otherwise any open Hibernate
	// sessions will attempt to write it back to the database on closing!)
	
//	for (Chunk child : containedChunks) {
//	    if (child.getType().equals(c.getType()) &&
//		    child.getValue().equals(c.getValue()) &&
//		    child.getStartOffset() != c.getStartOffset() &&
//		    child.getEndOffset() != c.getEndOffset()) {
//		
//		Query existingQuery = new Query(getQuery());
//		c.setQuery(existingQuery.appendSubquery(
//			c.getType(), "pos=" + c.getPosition()));
//		break;
//	    }
//	}
	
	containedChunks.add(c);
	
//	if (!c.hasDisplayQuery()) {
//	    c.refreshQuery();
//	}
    }
    
    /**
     * Removes any chunks contained in this chunk (and sets their parent to
     * null).
     */
    public void clearContainedChunks() {
	for (Chunk child : containedChunks) {
	    child.setParent(null);
	}
	
	containedChunks.clear();
    }
    
    /**
     * Returns true if this chunk contains any children.
     */
    public boolean hasContainedChunks() {
	return !containedChunks.isEmpty();
    }
    
    /**
     * Loads the text for this chunk, if it isn't already loaded (or even if
     * it is). Called automatically if <code>getText()</code> is called and
     * the text has not yet been loaded.
     */
    public void loadText() {
	if (innerText != null) return;
	
	innerText = Chunk.getText(
		getQuery().getDocumentID(),
		getStartOffset(),
		getEndOffset());
    }    
    
    /** Individual chunk heads can be very uninformative, like "chapter 4".
     This method looks at a query and combines the heads of all containing
     chunks, to create something like "book 7, chapter 4". */
    public void loadContainingHeads() {
	Query containingQuery = query.getContainingQuery();
	try {
	    while (! containingQuery.isJustDocumentID()) {
		Chunk containingChunk = containingQuery.getChunk();
		head = containingChunk.getHead() + " " + head;
		
		containingQuery = containingQuery.getContainingQuery();
	    }
	} catch (InvalidQueryException iqe) {
	    logger.warn("Invalid query in containing heads", iqe);
	}
    }
    
    /**
     * loadContainingHeads() doesn't seem to be working correctly,
     * this does the same thing except returns the string, rather than
     * setting this chunk's head 
     * Individual chunk heads can be very uninformative, like "chapter 4".
     * This method looks at a query and combines the heads of all containing
     * chunks, to create something like "book 7, chapter 4".
     */
    public String getContainingHeads() {
    	String head = getHead();
    	Query containingQuery = query.getContainingQuery();
    	try {
    		while (!containingQuery.isJustDocumentID() && !containingQuery.isSubDocument()) {
    			Chunk containingChunk = containingQuery.getChunk();
    			head = containingChunk.getHead() + ", " + head;
    			containingQuery = containingQuery.getContainingQuery();
    		}
    	} catch (InvalidQueryException iqe) {
    		logger.warn("Invalid query in containing heads", iqe);
    	}
    	return head;
    }

    /**
     * Transforms the chunk text using the default stylesheet for the
     * chunk (probably tei.xsl).
     */
    public void style() {
	Map<String,String> map = Collections.emptyMap();
	style(map);
    }
    
    /**
     * Transforms the chunk text using the given stylesheet.
     * 
     * @param stylesheet a path to a stylesheet (which, if not absolute,
     * assumes it's located in the stylesheet directory specified in the
     * configuration)
     */
    public void style(String stylesheet) {
	Map<String,String> map = Collections.emptyMap();
	style(stylesheet, map);
    }
    
    /**
     * Transforms the chunk text using the default stylesheet and the given
     * parameters.
     * 
     * @param parameters the parameters to pass to the transformer
     */
    public void style(Map<String,String> parameters) {
	style(null, parameters);
    }
    
    /**
     * Transforms the chunk text using the specified stylesheet, with the
     * given parameters.
     *
     * @param stylesheetName the name of the stylesheet to use
     * @param parameters parameters to be passed to the transformer
     */
    public void style(String stylesheetName, Map<String,String> parameters) {
	if (stylesheetName == null) {
	    stylesheetName = getMetadata().getDefaultStylesheet();
	}
	
	if (innerText == null) {
	    loadText();
	}

	String language = getEffectiveLanguage();
	if (language == null) {
	    language = LanguageCode.ENGLISH;
	}

	Map<String,String> workingParams = new HashMap<String,String>(parameters);
	if (!workingParams.containsKey("lang")) {
	    workingParams.put("lang", language);
	}
	if (!workingParams.containsKey("query")) {
	    workingParams.put("query", getQuery().toString());
	}
	
	cachedText = StyleTransformer.transform(getText(), stylesheetName, 
		getQuery().getDocumentID(), workingParams);
    }
    
    public boolean equals (Object o) {
	if (o instanceof Chunk) {
	    Chunk c = (Chunk) o;
	    return
		this.getDocumentID().equals(c.getDocumentID()) &&
		this.getType().equals(c.getType()) &&
		this.getStartOffset() == c.getStartOffset() &&
		this.getEndOffset() == c.getEndOffset();
	}
	return false;
    }
    
    public int hashCode() {
	int result = 17;
	
	result = 37*result + getDocumentID().hashCode();
	result = 37*result + getType().hashCode();
	result = 37*result + getStartOffset();
	result = 37*result + getEndOffset();
	
	return result;
    }
    
    /**
     * Compares this chunk's document ID and offset with <code>c</code>'s.
     * 
     * @param c the chunk to compare this one with
     * @return -1 if this chunk comes before <code>c</code>; 0 if they're
     * equal; 1 if <code>c</code> comes first
     */
    public int compareTo (Chunk c) {
	if (this.getDocumentID().compareTo(c.getDocumentID()) != 0) {
	    return this.getDocumentID().compareTo(c.getDocumentID());
	}

	if (this.getStartOffset() - c.getStartOffset() != 0) {
	    return this.getStartOffset() - c.getStartOffset();
	}
	
	// If two chunks start at the exact same point, the *larger* one should
	// come first. This is what the hierarchy-building methods expect.
	if (c.getEndOffset() - this.getEndOffset() != 0) {
	    return c.getEndOffset() - this.getEndOffset();
	}

	return 0;
    }
    
    /**
     * Returns true if this chunk could contain the target chunk, as
     * determined by the two chunks' offsets.
     * 
     * @param c the potential child chunk
     * @return true if this chunk could contain c, otherwise false
     */
    public boolean contains(Chunk c) {
	return this.getStartOffset() <= c.getStartOffset() &&
	this.getEndOffset() >= c.getEndOffset() &&
	this.getSize() != c.getSize();
	// (identical chunks don't contain one another)
    }
    
    /** Helper method used with contained chunks. */
    private void refreshQuery() {
	
	String workingValue = getValue();
	// If we've renamed the query to avoid a collision, make sure to
	// retain the query value
	if (!getQuery().getLastElementType().equals("document_id") &&
		!getQuery().getLastElementValue().equals(getValue())) {
	    workingValue = getQuery().getLastElementValue();
	}
	
	/*
	 * 1999.02.0022 (Shuckburgh) is a difficult text. It has values that
	 * are like text=A:book=1:letter=1. The hopper doesn't like it when you
	 * add the type and value so that it is: letter=text=A:book=1:letter=1.
	 * So we set the query so it's Perseus:text:1999.02.0022:text=A:book=1:letter=1
	 * which actually work.  Unfortunately this needs to happen multiple times
	 * in this class (see buildQuery) and also needs a tweak in TableOfContentsFactory
	 * assembleTree()
	 */
	if (parent != null) {
	    if (parent.getDocumentID().equals("Perseus:text:1999.02.0022")) {
		if (workingValue.startsWith("text=")) {
		    Query q = new Query(parent.getDocumentID()+":"+workingValue);
		    setQuery(q);
		}
	    }
	    else {
		setQuery(parent.getQuery().appendSubquery(getType(), workingValue));
	    }
	}
    }
    
    public String toString() {
	return String.format("[CHUNK %s %s %s pos=%d abs=%d %d-%d]",
                getQuery().toString(),
                getType(),
                getValue(),
                getPosition(),
                getAbsolutePosition(),
                getStartOffset(),
                getEndOffset());
    }

    public Element toXML() {
	Element topElement = new Element("chunk")
        .setAttribute("pos", Integer.toString(getPosition()))
	    .setAttribute("start",
		Integer.toString(getStartOffset()))
	    .setAttribute("end",
		Integer.toString(getEndOffset()))
	    .setAttribute("type",
		getType())
	    .setAttribute("n", getValue())
	    .addContent(renderHead());

	String type = getType();
	if (type.equals("alphabetic letter") || 
		type.equals("entry group") ||
		getMetadata().has(Metadata.SUBDOC_QUERY_KEY)) {
	    topElement.setAttribute("isParent", "1");
	}
	
	if (getChunkID() != null) {
	    topElement.setAttribute("id", getChunkID());
	}
	
	if (getDisplayQuery() != null) {
	    topElement.setAttribute("display", getDisplayQuery());
	}
	
	if (getQuery() != null) {
	    topElement.setAttribute("ref", getQuery().toString());
	}
	
	for (Chunk child : getContainedChunks()) {
	    topElement.addContent(child.toXML());
	}
	
	return topElement;
    }

    public void resetQuery(boolean cascade) {
	setQuery(buildQuery());
	if (cascade) {
	    resetChildQueries(true);
	}
    }

    /**
     * Sets all the queries of this chunk's children, based on this chunk's
     * query. Also checks for collisions (i.e., the presence of two chunks with
     * the same type and value), and differentiates the queries if necessary.
     */
    public void resetChildQueries(boolean cascade) {
	if (!getContainedChunks().isEmpty()) {
	    logger.debug(this + ": RESETTING CHILD QUERIES");
	}
	Query myQuery = getQuery();
	
	Set<String> valuesSeen = new HashSet<String>();
	for (Chunk child : getContainedChunks()) {
	    Query childQuery = myQuery.appendSubquery(child.getType(), child.getValue());

	    if (valuesSeen.contains(child.getValue())) {
		childQuery = myQuery.appendSubquery(
		    child.getType(),
		    "pos=" + String.valueOf(child.getPosition()));
		logger.debug("Collision: " + child.getValue());
	    }

	    valuesSeen.add(child.getValue());
	    child.setQuery(childQuery);
	    if (cascade) {
		child.resetChildQueries(cascade);
	    }

	}
	if (!getContainedChunks().isEmpty()) {
	    logger.debug(this + " DONE reset cqueries");
	}
    }

    private Query buildQuery() {
	String workingValue = getValue();
	if (getParent() == null) {
	    return new Query(getDocumentID())
	    	.appendSubquery(getType(), getValue());
	}
	
	for (Chunk collisionCandidate : getParent().getContainedChunks()) {
	    if (this.equals(collisionCandidate)) break;
	    
	    if (collisionCandidate.getType().equals(getType()) &&
		    collisionCandidate.getValue().equals(getValue())) {
		workingValue = "pos=" + getPosition();
		break;
	    }
	}
	Query parent = getParent().getQuery();

	/*
	 * 1999.02.0022 (Shuckburgh) is a difficult text. It has values that
	 * are like text=A:book=1:letter=1. The hopper doesn't like it when you
	 * add the type and value so that it is: letter=text=A:book=1:letter=1.
	 * So we set the query so it's Perseus:text:1999.02.0022:text=A:book=1:letter=1
	 * which actually work.  Unfortunately this class is not well written and this fix 
	 * needs to happen multiple times in this class (see refreshQuery) and also needs 
	 * a tweak in TableOfContentsFactory assembleTree()
	 */
	if (parent.getDocumentID().equals("Perseus:text:1999.02.0022")) {
	    if (workingValue.startsWith("text=")) {
		return new Query(parent.getDocumentID()+":"+workingValue);
	    }
	}
	return parent.appendSubquery(getType(), workingValue);
    }
    
    private Element renderHead() {
	Element headElement = new Element("head");
	
	String displayType = ChunkSchemes.getDisplayName(getType());
	String defaultHead = displayType.equals("line") ?
		getLineHead() :
		ChunkSchemes.getDisplayName(getType()) + " " + getValue();

	if (getHasCustomHead()) {
	    if (getHeadLanguage() != null) {
		headElement.setAttribute("lang", getHeadLanguage());
	    } else {
		headElement.setAttribute("lang",
			getMetadata().getLanguage().getCode());
	    }
	    
	    try {
		String headText = "<head>" + getHead() + "</head>";
		if (getHead().length() > 255) {
			logger.debug("length of head for chunk "+this.toString()+" is greater than 255");
		}
		headElement.addContent(new SAXBuilder().build(
			new StringReader(headText)).detachRootElement());
	    } catch (Exception e) {
		logger.warn("Problem rendering chunk header for TOC", e);
		headElement.addContent(defaultHead);
	    }
	} else {
	    headElement.addContent(defaultHead);
	}
	
	return headElement;
    }

    /**
     * Returns a pretty header for line-based chunks, like "lines 1-8" or
     * "lines 225ff."
     */
    private String getLineHead() {
	String myValue = getValue();

	// Some chunks have values of, e.g., "4-6"; return them as they are
	if (myValue.indexOf("-") != -1) return "lines " + myValue;
	if (getParent() == null) return "line " + myValue;
	
	// Otherwise, find this chunk's siblings; if it has none following it,
	// it probably represents the last card of a book, so return something
	// line "200ff." If it does have some following it, return something of
	// the form "lines A-B", where A is this chunk's value and B is the next
	// chunk's value minus one.
	
	Set<Chunk> siblings = getParent().getContainedChunks();
	Iterator<Chunk> it = siblings.iterator();
	
	// First we need to traverse the list until we find ourself.
	while (it.hasNext()) { if (it.next().equals(this)) break; }

	if (!it.hasNext()) {
	    return "lines " + myValue + "ff.";
	} else {
	    Chunk nextSibling = it.next();
	    String nextLineNumber = nextSibling.getValue().replaceAll("-.*", "");
	    try {
		if (!myValue.equals(nextLineNumber)) {
		    int nextValue = Integer.parseInt(nextLineNumber);
		    return "lines " + myValue + "-" + (nextValue-1);
		}
	    } catch (NumberFormatException nfe) {
		// *shrug*
	    }
	    return "line " + myValue;
	}
    }

    public Lemma getLemma() {
        return lemma;
    }

    public void setLemma(Lemma lemma) {
        this.lemma = lemma;
    }

	
/*	public void setSenses(List<Sense> newSenses) {
	    senses = newSenses;
	}

	public List<Sense> getSenses() {
	    return senses;
	}
*/
}
