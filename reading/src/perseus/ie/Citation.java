/**
 * A Citation represents a link between a source query and a destination
 * query, along with some other relevant information. A given citation can
 * be one of several types (as indicated by the <code>linkType</code>
 * variable):
 *
 * <dl>
 *  <dt>Commentary reference</dt>
 *  <dd>A reference in a commentary to a passage somewhere else.</dd>
 *  <dt>Cross-reference in a note</dt> <dd>A reference in a note (usually a
 *  footnote) in one document to a passage elsewhere.</dd>
 *  <dt>Cross-reference in a general dictionary</dt>
 *  <dd>A mention in a general dictionary of a passage elsewhere.</dd>
 *  <dt>Cross-reference in a specific lexicon</dt>
 *  <dd>A mention in a text-specific dictionary (like Slater or Autenrieth) of a passage elsewhere.</dd>
 *  <dt>Cross-reference in an index</dt>
 *  <dd>A reference in an index to a passage elsewhere.</dd>
 *  <dt>Cross-reference</dt>
 *  <dd>A reference that doesn't fall into any of the above categories.</dd>
 * </dl>
 */
package perseus.ie;

import java.lang.StringBuffer;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.Metadata;

import perseus.util.StringUtil;

public class Citation {

    private static Logger logger = Logger.getLogger(Citation.class);

    /** Code for a reference in a CIT tag. */
    public static final String REF_CITATION = "cit";

    /** Code for a commentary reference. */
    public static final String REF_COMMENTARY = "commentary";

    /** Code for a generic cross-reference. */
    public static final String REF_DEFAULT = "text";

    /** Code for a cross-reference in a note. */
    public static final String REF_NOTES = "note";

    /** Code for a cross-reference in a general lexicon. */
    public static final String REF_LEXICON = "lexicon";

    /** Code for a cross-reference in an index. */
    public static final String REF_INDEX = "index";

    /** Code for a cross-reference in a text-specific lexicon. */
    public static final String REF_SPECIFIC_LEXICON = "specific_lexicon";

    /** Keep track of all the link-types we know. */
    private static Set<String> knownLinkTypes = new HashSet<String>();
    static {
	knownLinkTypes.add(REF_CITATION);
	knownLinkTypes.add(REF_COMMENTARY);
	knownLinkTypes.add(REF_DEFAULT);
	knownLinkTypes.add(REF_NOTES);
	knownLinkTypes.add(REF_LEXICON);
	knownLinkTypes.add(REF_INDEX);
	knownLinkTypes.add(REF_SPECIFIC_LEXICON);
    }

    /** The query that spawned the citation. */
    private Query source = null;

    /** The destination of the citation. */
    private Query destination = null;

    /** The destination, aligned with a chunk in one of our documents.
     * For example, if the destination in the text is an ABO, this query
     * would represent a chunk in one of our versions of the ABO.
     */
    private Query resolvedDestination = null;
    
    /** The type of citation. */
    private String linkType = REF_DEFAULT;

    /** The header of the section in the source file where we found the
     * citation. May be something dull like "Chapter 4", or may be
     * something informative like "THIRD DECLENSION NOUNS". Used in
     * formatting the citation. (We don't keep track of the destination
     * header because it's slightly more of a hassle to retrieve for each
     * citation when we're extracting them, and the source headers are
     * usually more interesting anyway, since they tend to be grammars
     * and lexica and the like.)
     */
    private String sourceHeader = null;

    /**
     * Empty constructor.
     */
    public Citation() {
    }

    /**
     * Constructor. Other attributes can be set via setter methods.
     *
     * @param src the query where the citation originated
     * @param dest the target of the citation
     * @param ltype the link type
     */
    public Citation(Query src, Query dest, String ltype) {
	source = src;
	destination = dest;
	linkType = ltype;
    }

    public Query getSource() { return source; }
    public Query getDestination() { return destination; }
    public Query getResolvedDestination() {
	if (resolvedDestination == null) {
	    try {
		resolvedDestination =
		    destination.getChunk().getQuery();
	    } catch (InvalidQueryException iqe) {
		logger.warn("Unable to resolve destination in citation " + this);
	    }
	}
	return resolvedDestination;
    }

    public String getLinkType() { return linkType; }
    public String getSourceHeader() { return sourceHeader; }

    public void setSource(Query src) { source = src; }
    public void setDestination(Query dest) { destination = dest; }
    public void setResolvedDestination(Query rDest) {
	resolvedDestination = rDest;
    }

    public void setLinkType(String lnkTyp) {
	if (!knownLinkTypes.contains(lnkTyp)) {
	    throw new IllegalArgumentException(
		    "Unrecognized link type: " + lnkTyp);
	}
	linkType = lnkTyp;
    }
    public void setSourceHeader(String sh) { sourceHeader = sh; }

    public String toString() {
	StringBuffer buf = new StringBuffer();

	buf.append(source).append(" -> ").append(destination);
	if (resolvedDestination != null) {
	    buf.append(" [").append(resolvedDestination).append("]");
	}
	buf.append(" {").append(linkType).append("}");

	return buf.toString();
    }

    /**
     * Returns an XML representation of this Citation, along with some
     * additional information related to the source and destination
     * queries (title, creator, etc and so on).
     */
    public String toXML() {
	StringBuffer xml = new StringBuffer();

	xml.append("<citation>");

	xml.append("<source>");
	xml.append(queryToXML(source));
	if (sourceHeader != null) {
	    xml.append("<header>")
		.append(sourceHeader)
		.append("</header>");
	}
	xml.append("</source>\n");

	xml.append("<destination>");
	xml.append(queryToXML(destination));
	xml.append("<header>")
	    .append(destination.getDisplaySubqueryCitation())
	    .append("</header>");
	xml.append("</destination>\n");

	xml.append("<resolvedDestination>")
	    .append(queryToXML(resolvedDestination))
	    .append("</resolvedDestination>\n");

	xml.append("<linkType>");
	xml.append(linkType);
	xml.append("</linkType>\n");

	xml.append("</citation>\n");

	return xml.toString();
    }

    /**
     * Helper method for toXML().
     */
    private String queryToXML(Query query) {

	StringBuffer xml = new StringBuffer();
	Metadata metadata = query.getMetadata();

	if (metadata.has(Metadata.ALTERNATIVE_TITLE_KEY)) {
	    xml.append("<title>");
	    xml.append(metadata.get(Metadata.ALTERNATIVE_TITLE_KEY));
	    xml.append("</title>");
	} else {
	    xml.append("<title>");
	    xml.append(metadata.get(Metadata.TITLE_KEY));
	    xml.append("</title>");

	    if (metadata.has(Metadata.CREATOR_KEY)) {
		xml.append("<creator>");
		xml.append(metadata.get(Metadata.CREATOR_KEY));
		xml.append("</creator>");
	    }
	}

	xml.append("<query>");
	xml.append(query);
	xml.append("</query>");

	return xml.toString();
    }
}
