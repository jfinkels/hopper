package perseus.document;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import perseus.language.Language;
import perseus.util.Config;
import perseus.util.StringUtil;

/**
 * Stores various data about a given document--author, title, date modified,
 * and much more. Generally associated with a {@link Chunk} or {@link Query}
 * class.
 *
 * This class's values correspond to entries in the <kbd>metadata</kbd> table,
 * which are essentially key-value pairs (though not <em>quite</em> so simple).
 * Attributes are accessible via the #get() method, which takes as an argument
 * one of the string constants below and returns a string (possibly
 * concatenating return values), or through the #getList() method, which returns
 * a list of matching attributes. As of much more recently, there also exist
 * convenience methods (#getTitle(), #getAuthor(), et al.) that were generally
 * written as needed, when the calls to #get() were felt too cumbersome.
 *
 * The <kbd>metadata</kbd> table is essentially a copy of the old
 * <kbd>ptext</kbd> table with some small changes, and as such, contains a
 * number of fields. In particular, there are <em>two</em> fields to represent
 * values:
 *
 * <dl>
 *  <dt>document_id</dt>
 *  <dd>the document this entry belongs to</dd>
 *  <dt>subquery</dt>
 *  <dd>the subquery of the document (may be null)</dd>
 *  <dt>key_name</dt>
 *  <dd>the entry key, like "dc:Title"</dd>
 *  <dt>value</dt>
 *  <dd>the entry value, like "De Bello Gallico"</dd>
 *  <dt>value_id</dt>
 *  <dd>another possible value, usually corresponding to a document ID (e.g.,
 *  for the key "dc:isCommentaryOn", this contains the ID of the source
 *  text)</dd>
 *  <dt>language</dt>
 *  <dd>the language of the text stored in <kbd>value</kbd>, which may be
 *  different from the document language</dd>
 *  <dt>position</dt>
 *  <dd>this entry's sequence in the document metadata, when a given document has
 *  multiple entries for the same key</dd>
 * </dl>
 *
 * As with the Query and Chunk classes, this whole structure really needs to be
 * rethought, and hopefully rewritten into something more natural and
 * object-oriented.
 */

public class Metadata implements Comparable<Metadata> {
    
    public static final String TITLE_KEY = "dc:Title";
    public static final String ALTERNATIVE_TITLE_KEY = "dc:Title:Alternative";
    public static final String CREATOR_KEY = "dc:Creator";
    public static final String CONTRIBUTOR_KEY = "dc:Contributor";
    public static final String TYPE_KEY = "dc:Type";
    public static final String RIGHTS_KEY = "dc:Rights";
    public static final String DATE_AVAILABLE_KEY = "dc:Date:Available";
    public static final String DATE_MODIFIED_KEY = "dc:Date:Modified";
    public static final String DATE_COPYRIGHTED_KEY = "dc:Date:Copyrighted";
    public static final String DATE_CREATED_KEY = "dc:Date:Created";
    public static final String DATE_ISSUED_KEY = "dc:Date:Issued";
    public static final String PUBLISHER_KEY = "dc:Publisher";
    public static final String FORMAT_KEY = "dc:Format";
    public static final String COVERAGE_KEY = "dc:Coverage";
    public static final String SPATIAL_COVERAGE_KEY = "dc:Coverage:Spatial";
    public static final String RELATION_KEY = "dc:Relation";
    
    public static final String IDENTIFIER_KEY = "dc:Identifier";
    public static final String EXTERNAL_URL_KEY = "dc:Identifier:URL";
    public static final String CTS_KEY = "dc:Identifier:cts:urn";
    
    public static final String LANGUAGE_KEY = "dc:Language";
    public static final String SOURCE_KEY = "dc:Source";
    public static final String SUBJECT_KEY = "dc:Subject";
    public static final String DESCRIPTION_KEY = "dc:Description";
    
    public static final String ABO_KEY = "dc:Relation:IsVersionOf";
    public static final String COMMENTARY_KEY = "dc:Relation:IsCommentaryOn";
    public static final String CORPUS_KEY = "dc:Relation:IsPartOf";
    public static final String LEXICON_KEY = "dc:Relation:IsLexiconTo";
    public static final String INTRODUCTION_KEY = "dc:Relation:IsIntroductionTo";
    
    public static final String SORT_KEY = "perseus:Sort";
    public static final String CITATION_SCHEME_KEY = "perseus:Citation";
    public static final String TEMPLATE_KEY = "perseus:Layout:Template";
    public static final String STYLESHEET_KEY = "perseus:Layout:Stylesheet";
    public static final String FUNDER_KEY = "perseus:Funder";
    public static final String METHOD_KEY = "perseus:Method";
    public static final String SPEAKER_KEY = "perseus:Speaker";
    public static final String STATUS_KEY = "perseus:Status";
    public static final String EXTENT_KEY = "perseus:Extent";
    public static final String PARTNER_ID_KEY = "perseus:Partner";
    public static final String ARITY_KEY = "perseus:Arity";
    public static final String IMAGE_TYPE_KEY = "perseus:ImType";
    public static final String CORPUS_TYPE_KEY = "perseus:CorpusType";
    public static final String FORM_KEY = "perseus:Form";
    public static final String SOURCE_TEXT_PATH_KEY = "perseus:SourceText";
    public static final String SEGMENTATION_KEY = "perseus:Segmentation";

    public static final String CORRECTION_LEVEL_KEY =
	"perseus:CorrectionLevel";
    
    public static final String SOURCE_FIGURES_KEY = "perseus:SourceFigures";
    public static final String SOURCE_PAGES_KEY = "perseus:SourcePages";
    
    public static final String SUMMARY_KEY = "perseus:IsSummaryOf";
    public static final String SUMMARY_COMMENTARY_KEY =
	"perseus:isCommentaryOnSummary";
    
    public static final String SUBJECT_LANGUAGE_KEY = "dc:Subject:Language";
    
    public static final String WITNESS_KEY = "perseus:Witness";
    public static final String FIGURE_KEY = "dcterms:hasPart";
    
    public static final String DOCUMENT_ID_KEY = "internal:DocumentID";

    /** This constant is for a subdocument to define which subdocument it is */
    public static final String SUBDOC_QUERY_KEY = "internal:SubDocQuery";
    
    /** This constant is for a main document to keep track of what subdocuments
     it contains */
    public static final String SUBDOC_REF_KEY = "internal:SubDocRef";
    
    public static final String DATE_KEY = "dc:Date";
        
    Map<String,Set<NodeValue>> fields = new HashMap<String,Set<NodeValue>>();

    ChunkSchemes chunkSchemes = new ChunkSchemes();
    private Logger logger = Logger.getLogger(this.getClass());
    
    private static final String[] COMPARAND_KEYS = new String[] {
        SORT_KEY, CREATOR_KEY, CONTRIBUTOR_KEY, TITLE_KEY
    };

    /** This empty constructor is temporary storage for name value pairs that
     happen to be metadata, but may not be in the database as such */
    public Metadata () {
    }

    public Metadata (String initialQuery) {
	this(new Query(initialQuery));
    }
    
    public Metadata (Query initialQuery) {
	this(initialQuery.getDocumentID(), initialQuery.getQuery());
    }

    /** Version for use by the metadata cache */
    public Metadata (String documentID, String subquery) {
	addField(DOCUMENT_ID_KEY, documentID);
	
	if (subquery != null) {
	    addField(SUBDOC_QUERY_KEY, subquery);
	}
    }

    /**
     * Returns all attributes keys contained in this Metadata object.
     * The values can then be retrieved by calling the #get() method
     * for each key.
     */
    public Set<String> getKeys() {
	return fields.keySet();
    }
    
    /**
     * Returns all values for a given key as a list of `NodeValue` objects.
     * Each such object contains three fields: `value`, `valueID`
     * and `language`, each representing a corresponding field in the database
     * table.
     *
     * @param key the key to match
     * @return a ordered list of NodeValue objects containing the key's values
     */
    public List<NodeValue> getNodeValues(String key) {
	Set<NodeValue> values = fields.get(key);
	return new ArrayList<NodeValue>(values);
    }
    
    /**
     * Returns a File representing this object's (un-chunkified) *XML* source
     * file, like "/sgml/texts/Cicero/cic.att_lat.xml", or null if the object
     * doesn't have a source file (as in the case of collections and ABOs).
     * Note that this returns an *XML* source file; if the metadata references
     * an SGML file, we assume that the file has been processed at some point
     * with osx and attempt to guess the path of the resulting XML file. Per
     * the configuration of the old hopper, we assume that Fred/my_text.sgml
     * has an XML file located at Fred/xml/my_text.xml. If we can't find the
     * XML text, return null. If you want the SGML path, you can always check
     * the SOURCE_TEXT_PATH_KEY attribute.
     */
    public File getSourceFile() {
	if (!has(SOURCE_TEXT_PATH_KEY)) return null;
	
	File sourceFile = null;

	File specifiedPath = new File(get(SOURCE_TEXT_PATH_KEY));
	if (!specifiedPath.isAbsolute()) {
	    File baseDirectory = new File(Config.getSourceTextsPath());
	    sourceFile = new File(baseDirectory, specifiedPath.getPath());
	} else {
	    sourceFile = specifiedPath;
	}
	
	// Kludge for SGML files--hope that they've been passed through
	// something like osx, and so have an XML version available.
	if (sourceFile.getAbsolutePath().endsWith(".sgml")) {
	    String xmlFile = sourceFile.getName().replaceAll("\\.sgml$", ".xml");
	    File xmlDirectory = new File(sourceFile.getParent(), "xml");
	    sourceFile = new File(xmlDirectory, xmlFile);
	}
	
	if (!sourceFile.exists()) {
	    logger.error("Unable to locate source file " + sourceFile
		    + " for document " + getDocumentID());
	    return null;
	}
	
	return sourceFile;
    }
    
    /**
     * Returns true if the full text document may be offered for download.
     */
    public boolean isDownloadable() {
    	//our way of preventing certain texts from being downloaded even if
    	//they may be in the public domain
    	if ("nodownload".equals(get(RIGHTS_KEY))) return false;
    	
    	if (has(STATUS_KEY)) {
    		String status = get(STATUS_KEY);
    		if (status.equals("1") || status.equals("3")) return false;
    	}
    	if (!has(DATE_COPYRIGHTED_KEY)) return true;

    	int date;
    	try {
    		date = Integer.parseInt(get(DATE_COPYRIGHTED_KEY));
    	} catch (NumberFormatException nfe) {
    		return false;
    	}

    	//all texts published in 1922 or earlier are in public domain
    	if (date <= 1922) {
    		return true;
    	}

    	//some texts published after 1922 don't have copyright and are downloadable
    	if (date > 1922 && "download".equals(get(RIGHTS_KEY))) {
    		return true;
    	}

    	//at this point, don't let it be downloaded
    	return false;
    }
    
    public boolean hasCreationOrIssueDate() {
    	if (this.hasSubdocs()) {
    		
    	}
    	return has(DATE_CREATED_KEY) || has(DATE_ISSUED_KEY);
    }
    
    public String getCreationOrIssueDate() {
    	if (has(DATE_CREATED_KEY)) {
    		return getDateCreated();
    	}
    	if (has(DATE_ISSUED_KEY)) {
    		return getDateIssued();
    	}
    	return null;
    }
    
    /**
     * Returns the stylesheet that should be used by default for transforming
     * this chunk. For TEI documents, it will probably be "tei.xsl"; for other
     * DTDs it should be something that can specifically handle the DTD. We
     * determine the stylesheet to use by (a) checking if this Metadata object
     * has anything defined in STYLESHEET_KEY and, if not, (b) checking whether
     * its parent collection has one defined, and returning the first one we
     * come upon.
     */
    public String getDefaultStylesheet() {
	if (has(STYLESHEET_KEY)) {
	    return get(STYLESHEET_KEY);
	} else if (has(CORPUS_KEY)) {
	    for (String corpus : getList(CORPUS_KEY)) {
		Query collection = new Query(corpus);
		
		Metadata collMetadata = collection.getMetadata();
		if (collMetadata.has(Metadata.STYLESHEET_KEY)) {
		    return collMetadata.get(Metadata.STYLESHEET_KEY);
		}
	    }
	}
	
	return StyleTransformer.DEFAULT_STYLESHEET;
    }
    
    public void addField(String key, String value) {
	addField(key, value, null);
    }
    
    public void addField(String key, String value, String valueID) {
	addField(key, new NodeValue(value, valueID));
    }
    
    public void addField(String key, String val, String valID, Language lang) {
	addField(key, new NodeValue(val, valID, lang));
    }
    
    private void addField(String key, NodeValue nodeValue) {
	if (key.equals(CITATION_SCHEME_KEY)) {
	    addChunkScheme(nodeValue.getValue());
	}
	
	Set<NodeValue> fieldValues;
	if (fields.containsKey(key)) {
	    fieldValues = fields.get(key);
	} else {
	    fieldValues = new ListOrderedSet();
	    fields.put(key, fieldValues);
	}
	fieldValues.add(nodeValue);
    }
    
    public void addField(String key, List<String> values) {
	Set<NodeValue> set = null;
	
	if (fields.containsKey(key)) {
	    set = fields.get(key);
	}
	else {
	    set = new ListOrderedSet();
	    fields.put(key, set);
	}
	
	for (String value : values) {
	    if (key.equals(CITATION_SCHEME_KEY)) {
		addChunkScheme(value);
	    }
	    set.add(new NodeValue(value, null));
	}
	
    }
    
    public Object remove(String key) {
	if (CITATION_SCHEME_KEY.equals(key)) {
	    chunkSchemes.clearSchemes();
	}
	return fields.remove(key);
    }
    
    public String getDocumentID() {
	return get(DOCUMENT_ID_KEY);
    }
    
    /**
     * Returns all values for <kbd>key</kbd>, concatenated with a space.
     *
     * @param key the metadata key
     * @return a string containing all values for `key`
     */ 
    public String get(String key) {
	if (key == null) {
	    return null;
	}
	
	if (fields.containsKey(key)) {
	    Set<NodeValue> values = fields.get(key);
	    if (values.size() == 0) {
		return null;
	    } else {
		return StringUtil.join(values, ", ");
	    }
	}
	
	return null;
    }
    
    /**
     * Returns true if this Metadata object contains any values for `key`.
     */
    public boolean has(String key) {
	if (key == null) { return false; }
	
	return fields.containsKey(key);
    }

    /**
     * This returns a List of String values rather than NodeValues
     * (for back-compatibility).
     */
    public List<String> getList(String key) {
	List<String> output = new ArrayList<String>();
	
	if (fields.containsKey(key)) {
	    Set<NodeValue> values = fields.get(key);
	    for (NodeValue value : values) {
		if (value.hasValue()) {
		    output.add(value.getValue());
		}
		if (value.hasValueID()) {
		    output.add(value.getValueID());
		}
	    }
	    
	    return output;
	}
	
	return Collections.emptyList();
    }
    
    /**
     * Returns a new Metadata object containing both this Metadata object's
     * values and `other`'s.
     *
     * @param other the Metadata object to merge with this one
     * @return a new object containing data from this object and `other`
     */
    public Metadata merge(Metadata other) {
	Metadata mergedData = new Metadata();
	
	// First copy everything from this instance
	for (String key : fields.keySet()) {
	    Set<NodeValue> nodeValues = fields.get(key);
	    for (NodeValue nodeValue : nodeValues) {
		mergedData.addField(key, nodeValue);
	    }
	}
	
	// ...and now copy from the other guy
	for (String key : other.fields.keySet()) {
	    // (but don't touch any internal keys, which we definitely don't
	    //  want to copy)
	    if (key.startsWith("internal:")) {
		continue;
	    }
	    
	    Set<NodeValue> nodeValues = other.fields.get(key);
	    for (NodeValue nodeValue : nodeValues) {
		mergedData.addField(key, nodeValue);
	    }
	}
	
	return mergedData;
    }
    
    /**
     * Returns true if this object and `other` share at least one
     * value in the field denoted by `key`. One use is checking whether
     * two documents are in the same collection.
     *
     * @param other the Metadata object to compare with this one
     * @param key the field to check
     * @return true if the objects overlap
     */
    public boolean overlaps(Metadata other, String key) {
	
	if ((!has(key)) || (!other.has(key))) {
	    return false;
	}
	
	Set<NodeValue> myValues = fields.get(key);
	Set<NodeValue> otherValues = other.fields.get(key);
	
	Iterator valuesIterator = myValues.iterator();
	while (valuesIterator.hasNext()) {
	    if (otherValues.contains(valuesIterator.next())) {
		return true;
	    }
	}
	
	return false;
    }
    
    public String toString() {
	StringBuilder output = new StringBuilder();
	
	for (String key : fields.keySet()) {
	    Set nodeValueSet = fields.get(key);
	    List nodeValueList = new ArrayList();
	    nodeValueList.addAll(nodeValueSet);
	    NodeValue firstNodeValue = (NodeValue)nodeValueList.get(0);

	    output.append(key)
		.append(" -> ")
		.append(fields.get(key))
		.append("\n");
	}
	
	return output.toString();
    }
    
    public ChunkSchemes getChunkSchemes() {
	return chunkSchemes;
    }
    
    /**
     * Returns the default chunk type for this document, as given by its
     * {@link ChunkSchemes} object.
     */
    public String getDefaultChunk() {
	return chunkSchemes.getDefaultChunk();
    }
    
    public void initChunkSchemes() {
	chunkSchemes = new ChunkSchemes();
	List<String> citationSchemes = getList(Metadata.CITATION_SCHEME_KEY);
	
	for (String scheme : citationSchemes) {
	    addChunkScheme(scheme);
	}
	
	// If there are no chunk schemes available for the document proper and
	// this document has subdocuments, use the schemes for the first
	// subdoc. This can happen if the source text has refsDecl elements
	// defined for subdocuments but not for the main document.
	if (chunkSchemes.getSchemeCount() == 0 && has(SUBDOC_REF_KEY)) {
	    String firstSubdoc = getList(SUBDOC_REF_KEY).get(0);
	    Query subdocQuery = new Query(getDocumentID(), firstSubdoc);
	    Metadata subdocMetadata = subdocQuery.getMetadata();
	    
	    List<String> subdocSchemes = new ArrayList<String>(
		    subdocMetadata.getChunkSchemes().getSchemes());
	    
	    for (String scheme : subdocSchemes) {
		addChunkScheme(scheme);
	    }
	}
    }
    
    public void addChunkScheme(String scheme) {
	chunkSchemes.addScheme(scheme);
    }
    
    public Query getQuery() {
	return new Query(getDocumentID(), get(SUBDOC_QUERY_KEY));
    }
    
    public String getComparand() {
        for (String comparand : COMPARAND_KEYS) {
	    String sortValue = get(comparand);
            if (sortValue != null) {
                return sortValue;
            }
        }
	
        return "zzzzzzz";
    }
    
    public int compareTo(Metadata m) {
	return getComparand().compareTo(m.getComparand());
    }
    
    /**
     * Returns true if the value of every field in this object is equal to the
     * value of the corresponding field in `m`. The purpose of this method is
     * to allow us to create metadata templates that can then be used as
     * queries against a set of metadata objects. For example, you might create
     * a metadata object with LANGUAGE set to "en", and then use it to filter
     * out only documents in English.
     */
    public boolean matches(Metadata m) {
	if (m == null) { 
	    return false;
	}
	
	for (String key : fields.keySet()) {
	    String myValue = get(key);
	    
	    if (! m.has(key)) {
		return false;
	    }
	    
	    // Loop through all values for this metadata field, and if none
	    // of the values matches our value, this metadata doesn't match.
	    
	    List<String> otherValues = m.getList(key);
	    boolean foundMatch = false;
	    for (String otherValue : otherValues) {
		if (myValue.equals(otherValue)) {
		    foundMatch = true;
		    break;
		}
	    }
	    if (! foundMatch) {
		return false;
	    }
	}
	
	// We've looped through all our metadata fields without NOT finding a
	// match so we must have a match.
	return true;
    }
    
    public String toXML() {
	return toXML(null);
    }
    
    public org.jdom.Document toXMLDocument() {
	return toXMLDocument(null);
    }
    
    public org.jdom.Document toXMLDocument(String schema) {
	Element root = new Element("document")
            .setAttribute("id", getQuery().toString());
	
	root.addContent(dataAsElements());
	
	return new org.jdom.Document(root);
    }
    
    public String toXML(String schema) {
	// do something with stylesheets based on schema
	return new XMLOutputter().outputString(
		toXMLDocument(schema).detachRootElement());
    }
    
    private List<Element> dataAsElements() {
	// By default, this returns a list of elements of the form
	//
	// <datum key="dcterms:IsPartOf" valueid="Perseus:collection:" />
	// <datum key="dc:Relation:IsPartOf"
	//     valueid="Perseus:corpus:perseus,Greek Prose" />
	// <datum key="dc:Language">greek</datum>
	//
	// (We do this mostly so we won't have to deal with the
	// double namespaces in some of the elements (dc:Date:Modified),
	// which no sensible XML parser will be willing to deal with.
	List<Element> data = new ArrayList<Element>();
	
	for (String key : fields.keySet()) {
	    Set<NodeValue> values = fields.get(key);
	    
	    // Don't print out tags that aren't actually part of any
	    // scheme
	    if (key.startsWith("internal:")) continue;
	    
	    for (NodeValue nodeValue : values) {
		String value = nodeValue.getValue();
		String valueID = nodeValue.getValueID();
		Language language = nodeValue.getLanguage();
		
		Element datum = new Element("datum").setAttribute("key", key);
		if (valueID != null) {
		    datum.setAttribute("valueid", valueID);
		}
		if (language != null) {
		    datum.setAttribute("lang", language.getCode());
		}
		if (value != null) {
		    datum.addContent(value);
		}
		
		data.add(datum);
	    }
	}
	
	return data;
    }
    
    /** Convenient helper methods. Add more if you feel like it. */
    public String getTitle() { return get(TITLE_KEY); }
    public String getCreator() { return get(CREATOR_KEY); };
    public String getContributor() { return get(CONTRIBUTOR_KEY); }
    public String getDescription() { return get(DESCRIPTION_KEY); }
    public String getAbo() { return get(ABO_KEY); }
    public Language getLanguage() { return Language.forCode(get(LANGUAGE_KEY)); }
    public String getType() { return get(TYPE_KEY); }
    public Language getSubjectLanguage() { return Language.forCode(get(SUBJECT_LANGUAGE_KEY)); }
    public String getFunder() { return get(FUNDER_KEY); }
    public String getAlternativeTitle() { return get(ALTERNATIVE_TITLE_KEY); }
    public String getArity() { return get(ARITY_KEY); }
    public String getCorpusType() { return get(CORPUS_TYPE_KEY); }
    public String getDateCreated() { return get(DATE_CREATED_KEY); }
    public String getDateIssued() { return get(DATE_ISSUED_KEY); }
    public String getCorrectionLevel() { return get(CORRECTION_LEVEL_KEY); }
    public String getMethod() { return get(METHOD_KEY); }
    public String getSegmentation() { return get(SEGMENTATION_KEY); }
    public List<String> getSourceList() { return getList(SOURCE_KEY); }
    
    public String getISBN() {
	if (has(SOURCE_KEY)) {
	    String source = get(SOURCE_KEY);
	    String isbn = null;
	    String[] sourceSplit = source.split("isbn,");
	    if (sourceSplit.length > 1) {
		isbn = sourceSplit[1];
		sourceSplit = isbn.split(",");
		if (sourceSplit.length > 0) {
		    isbn = sourceSplit[0];
		}
	    }
	    return isbn;
	}
	else return null;
    }

    public boolean hasSubdocs() { return has(SUBDOC_REF_KEY); }

    public boolean hasSubjectLanguage() { return has(SUBJECT_LANGUAGE_KEY); }

    // TODO this should be replaced by a stylesheet!
    public String toRDF() {
	StringBuffer output = new StringBuffer();
	
	output.append("<rdf:Description xmlns=\"http://www.perseus.org/meta/perseus.rdfs#\" ")
	.append("xmlns:dc=\"http://purl.org/dc/elements/1.1/\" ")
	.append("xmlns:dcterms=\"http://purl.org/dc/terms/\" ")
	.append("xmlns:dctype=\"http://purl.org/dc/dcmitype/\" ")
	.append("xmlns:perseus=\"http://www.perseus.org/meta/perseus.rdfs#\" ")
	.append("xmlns:persq=\"http://www.perseus.org/meta/persq.rdfs#\" ")
	.append("xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" ")
	.append("xmlns:tufts=\"http://www.tufts.edu/\" ")
	.append("rdf:about=\"")
	.append(get(DOCUMENT_ID_KEY));
	if (has(SUBDOC_QUERY_KEY)) {
	    output.append(":" + get(SUBDOC_QUERY_KEY));
	}
	output.append("\">\n");
	
	output.append(getXMLData());
	
	output.append("</rdf:Description>");
	
	return output.toString();
    }
    
    // TODO this should be replaced by a stylesheet, too!
    public String toOAI() {
	StringBuffer output = new StringBuffer();
	
	output.append("<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dcterms=\"http://purl.org/dc/terms/\">\n");
	
	output.append(getXMLData());
	
	output.append("</oai_dc:dc>");
	
	return output.toString();
    }
    
    private String getXMLData() {
	StringBuffer output = new StringBuffer();
	
	for (String key : fields.keySet()) {
	    Set<NodeValue> values = fields.get(key);
	    
	    // Don't print out tags that aren't actually part of any
	    // scheme
	    if (key.startsWith("internal:")) {
		continue;
	    }
	    
	    for (NodeValue nodeValue : values) {
		String value = StringUtil.xmlEscape(nodeValue.getValue());
		String valueID = StringUtil.xmlEscape(nodeValue.getValueID());
		String language = StringUtil.xmlEscape(
			nodeValue.getLanguage().getCode());
		
		output.append("<").append(key);
		if (valueID != null) {
		    output.append(" valueid=\"").append(valueID).append("\"");
		}
		if (language != null) {
		    output.append(" lang=\"").append(language).append("\"");
		}
		if (value != null) {
		    output.append(">").append(value)
		    .append("</").append(key).append(">");
		} else {
		    output.append("/>");
		}
		
		output.append("\n");
	    }
	}
	
	return output.toString();
    }
    
    /**
     * Internal helper class to represent values from individual rows of the
     * `metadata` table. The #get() and #getList() methods return either the
     * value or the valueID field, depending on the key, but are not always
     * perfect in choosing what to return. The #getNodeValues() method returns
     * the `NodeValue` classes used to store the values, allowing easy access
     * to all fields.
     */
    public class NodeValue {
	String value;
	String valueID;
	Language language;
	
	public NodeValue(String val, String valID) {
	    value = val;
	    valueID = valID;
	}
	
	public NodeValue(String val, String valID, Language lang) {
	    this(val, valID);
	    language = lang;
	}
	
	public String getValue() { return value; }
	public boolean hasValue() { return (value != null); }
	
	public String getValueID() { return valueID; }
	public boolean hasValueID() { return (valueID != null); }
	
	public Language getLanguage() { return language; }
	public boolean hasLanguage() { return (language != null); }
	
	public String toString() {
	    if (hasValue()) {
		if (hasValueID()) {
		    return value + ", " + valueID;
		} else {
		    return value;
		}
	    }
	    return valueID;
	}
	
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof NodeValue)) return false;
	    NodeValue nv = (NodeValue) o;
	    
	    if (!hasValue()) {
		if (nv.hasValue()) { return false; }
	    } else {
		if (!getValue().equals(nv.getValue())) {
		    return false;
		}
	    }
	    
	    if (!hasValueID()) {
		if (nv.hasValueID()) { return false; }
	    } else {
		if (!getValueID().equals(nv.getValueID())) {
		    return false;
		}
	    }
	    
	    if (!hasLanguage()) {
		if (nv.hasLanguage()) { return false; }
	    } else {
		if (!getLanguage().equals(nv.getLanguage())) {
		    return false;
		}
	    }
	    
	    return true;
	}
	
	public int hashCode() {
	    int result = 17;
	    
	    if (hasValue()) result = 37*result + getValue().hashCode();
	    if (hasValueID()) result = 37*result + getValueID().hashCode();
	    if (hasLanguage()) result = 37*result + getLanguage().hashCode();
	    
	    return result;
	}
    }
}
