package perseus.document;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import perseus.language.Language;
import perseus.util.StringUtil;

/**
 * Contains some helper methods for loading MetadataStore objects. This class
 * exists chiefly to avoid polluting the MetadataStore class with helper
 * methods.
 */

public class MetadataStoreLoader {
    
    private static Logger logger = Logger.getLogger(MetadataStoreLoader.class);

    private static DocumentBuilder builder;
    static {
	initDomBuilder();
    }

    private static void initDomBuilder() {
	try {
	    builder = DocumentBuilderFactory.newInstance().
			newDocumentBuilder();
	} catch (Exception e) {
	    logger.warn("Problem creating DocumentBuilder: " + e);
	}
    }

    /**
     * Extracts a MetadataStore from `metadataFile`, which should be an XML
     * file that has been passed through `catalog.xsl`.
     *
     * Current output format of build/document/catalog.xsl:
     * <metadata>
     *  <document id="Perseus:text:blah">
     *   <dc:Title>The Iliad</dc:Title>
     *   <dc:Creator>Homer</dc:Creator>
     *   <dc:Funder valueid="zzyxwl">The Zzyxwl Foundation</dc:Funder>
     *  </document>
     *  <document id="Perseus:abo:blah2">
     *   ....
     *  </document>
     * </metadata>
     */
    public static MetadataStore fromXML(File metadataFile)
	throws MetadataExtractionException {

	try {
	    Document documentTree = builder.parse(
			    metadataFile.getAbsolutePath());
	    return processDocument(documentTree);
	} catch (IOException ioe) {
	    throw new MetadataExtractionException(ioe);
	} catch (SAXException se) {
	    throw new MetadataExtractionException(se);
	}
    }

    /**
     * Extracts a MetadataStore from `xmlMetadata`, which should be a String
     * containing the output from an XML catalog file passed through
     * `catalog.xsl`.
     */
    public static MetadataStore fromXML(String xmlMetadata)
	throws MetadataExtractionException {
	
	if (xmlMetadata == null) return new MetadataStore();

	try {
	    Document documentTree = builder.parse(
		    new InputSource(new StringReader(xmlMetadata)));
	    return processDocument(documentTree);
	} catch (IOException ioe) {
	    throw new MetadataExtractionException(ioe);
	} catch (SAXException se) {
	    throw new MetadataExtractionException(se);
	}
    }

    private static MetadataStore processDocument(Document documentTree) 
	throws IOException, SAXException {

	MetadataStore store = new MetadataStore();

	NodeList children = documentTree.getElementsByTagName("document");
	for (int i = 0, n = children.getLength(); i < n; i++) {
	    Element documentNode = (Element) children.item(i);

	    Metadata metadata = processNode(documentNode);
	    store.putOrMerge(
		    new Query(documentNode.getAttribute("id")), metadata);
	}
	return store;
    }

    private static Metadata processNode(Element documentElement) {
	String query = documentElement.getAttribute("id");
	Metadata metadata = new Metadata(query);

	NodeList metadataElts = documentElement.getChildNodes();
	for (int i = 0, n = metadataElts.getLength(); i < n; i++) {
	    Node node = metadataElts.item(i);
	    if (node.getNodeType() != Node.ELEMENT_NODE) continue;

	    Element datumElement = (Element) node;

	    String field = datumElement.getNodeName();
	    // Metadata can be of the form:
	    // <dc:Title>Life and Times of Tiger Brown</dc:Title>
	    // <datum key="dc:Title">Life and Times of Tiger Brown</datum>
	    if (field.equals("datum")) {
		field = datumElement.getAttribute("key");
	    }

	    // the metadata table uses this wacky "double namespace"
	    // format for some entries, with two colons, that sensible XML
	    // implementations don't allow; convert back from the sensible to
	    // the wacky
	    field = field.replaceAll("_", ":");

	    // also normalize case; go from, e.g., "dc:title" to "dc:Title"
	    // (that is, capitalize all parts of the name except the first)
	    // (sigh...)
	    String[] fieldTokens = field.split(":");
	    for (int tok = 1; tok < fieldTokens.length; tok++) {
		fieldTokens[tok] =
		    Character.toUpperCase(fieldTokens[tok].charAt(0))
			+ fieldTokens[tok].substring(1);
	    }
	    field = StringUtil.join(fieldTokens, ":");

	    // NB: the DOM classes return empty strings instead of null.
	    String value = datumElement.getTextContent().trim();
	    value = StringEscapeUtils.escapeXml(value);
	    
	    // the Title elements have some extraneous information we
	    // don't want to show (like "Machine readable text", e.g.);
	    // run the element text some regexps to get rid of what we don't
	    // want. It'd be much nicer to do this in the stylesheet, but XSLT
	    // 1.0 doesn't support regexps.
	    if (field.equals(Metadata.TITLE_KEY)) {
		value = cleanTitle(value);

		// If a particular title element consisted *only* of text that
		// we wiped out, silently continue--don't report the existence
		// of an empty field, which would just confuse people
		if (value.equals("")) continue;
	    } else {
		if (value.equals("")) value = null;
	    }
	    
	    // Some publishing dates dates have extra gunk at the end of them,
	    // like "1934 (no copyright specified)"--we just want the year
	    if (field.equals(Metadata.DATE_COPYRIGHTED_KEY) && value != null) {
		value = value.replaceFirst("^(\\d*).*", "$1");
		if (value.equals("")) continue;
	    }

	    String valueID = datumElement.hasAttribute("valueid") ?
		datumElement.getAttribute("valueid") : null;

	    String language = datumElement.hasAttribute("lang") ?
		datumElement.getAttribute("lang") : null;

	    if (value == null && valueID == null) {
		logger.debug("-- Ignoring empty field " + field);
		continue;
	    }

	    String schema = datumElement.getAttribute("schema");
	    metadata.addField(schema.equals("") ? field : schema,
				value, valueID, Language.forCode(language));
	}

	return metadata;
    }

    // Patterns matching some junk we want to get rid of
    private static final Pattern MACHINE_TEXT_PATTERN = Pattern.compile(
	    "[ .]*machine\\s+readable\\s+text[ .]*$",
	    Pattern.CASE_INSENSITIVE);
    private static final Pattern MACHINE_TRANS_PATTERN = Pattern.compile(
	    "[ .:]*a\\s+machine[ \\-]+readable\\s+transcription[ .]*$",
	    Pattern.CASE_INSENSITIVE);
    private static final Pattern ELEC_ED_PATTERN = Pattern.compile(
	    ":\\s*electronic\\s+edition[ .]*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LANGUAGE_PATTERN = Pattern.compile(
	    "\\s+\\((?:Greek|English|Latin)\\)$", Pattern.CASE_INSENSITIVE);
    private static Pattern[] badPatterns = new Pattern[] {
	MACHINE_TEXT_PATTERN, MACHINE_TRANS_PATTERN, ELEC_ED_PATTERN,
	    LANGUAGE_PATTERN};

    private static String cleanTitle(String title) {
	for (int i = 0; i < badPatterns.length; i++) {
	    Matcher matcher = badPatterns[i].matcher(title);
	    title = matcher.replaceAll("");
	}
	return title;
    }
}
