package perseus.document;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.util.StringUtil;

public class Commentary implements Comparable {
    
    private static Logger logger = Logger.getLogger(Commentary.class);
    private Query commentaryQuery;
    private Query textQuery;
    
    private List<String> commentaryScheme; 
    private List<String> textScheme;
    
    public Commentary(Query cq, Query tq, String scheme) {
	commentaryQuery = cq;
	textQuery = tq;
	
	String[] schemeTokens = scheme.split("=");
	commentaryScheme = Arrays.asList(schemeTokens[0].split(":"));
	textScheme = Arrays.asList(schemeTokens[1].split(":"));
    }
    
    public Query getCommentaryQuery() {
	return commentaryQuery;
    }
    
    public Query getTextQuery() {
	return textQuery;
    }
    
    public List<String> getCommentaryScheme() {
	return commentaryScheme;
    }
    
    public List<String> getTextScheme() {
	return textScheme;
    }
    
    public String toString() {
	return String.format("%s [%s] -> %s [%s]",
	    commentaryQuery, StringUtil.join(commentaryScheme, ":"),
	    textQuery, StringUtil.join(textScheme, ":"));
    }
    
    public Query transformTextQuery(Query query) {
	Query resultQuery = getCommentaryQuery();
	
	int schemeIdx = 0;
	for (String textSchemePart : getTextScheme()) {
	    if (schemeIdx >= commentaryScheme.size()) break;
	    
	    String commSchemePart = commentaryScheme.get(schemeIdx);
	    
	    // Some commentary schemes are defined by line, others by card;
	    // twiddle them if we need to.
	    if (textSchemePart.equals("card") &&
		    query.getValueForType("card") == null) {
		textSchemePart = "line";
	    } else if (textSchemePart.equals("line") &&
		    query.getValueForType("line") == null) {
		textSchemePart = "card";
	    }

	    String value = query.getValueForType(textSchemePart);
	    if (value == null) break;

	    resultQuery = resultQuery.appendSubquery(commSchemePart, value);
	    schemeIdx++;
	}

	return resultQuery;
    }

    public static Set<Commentary> getCommentariesFor(Query textQuery) {
	if (textQuery.getMetadata().getAbo() == null) {
	    return Collections.emptySet();
	}
	
	return retrieveCommentaries(textQuery.getMetadata().get(Metadata.ABO_KEY));
    }

    public static Set<Commentary> getAllCommentaries() {
	return retrieveCommentaries(null);
    }
    
    private static Set<Commentary> retrieveCommentaries(String abo) {
	Set<Commentary> commentaries = new HashSet<Commentary>();
	
	try {
	    MetadataDAO dao = new SQLMetadataDAO();
	    
	    List<Query> commQueries = dao.getDocuments(
		    Metadata.COMMENTARY_KEY, null, abo);
	    
	    for (Query commQuery : commQueries) {
		Metadata metadata = commQuery.getMetadata();
		
		List<Metadata.NodeValue> commsForQuery = 
		    metadata.getNodeValues(Metadata.COMMENTARY_KEY);
		for (Metadata.NodeValue nv : commsForQuery) {
		    String scheme = nv.getValue();
		    String targetText = nv.getValueID();
		    
		    Commentary commentary = new Commentary(
			    commQuery, new Query(targetText), scheme);
		    commentaries.add(commentary);
		}
	    }
	} catch (MetadataAccessException me) {
	    logger.warn("Problem extracting citations: " + me);
	}
	
	return commentaries;
    }
    
    public boolean equals(Object o) {
	if (!(o instanceof Commentary)) {
	    return false;
	}
	
	Commentary c = (Commentary) o;
	
	return (commentaryQuery.equals(c.commentaryQuery) &&
		textQuery.equals(c.textQuery) &&
		commentaryScheme.equals(c.commentaryScheme) &&
		textScheme.equals(c.textScheme));
    }
    
    public int compareTo(Object o) {
	if (!(o instanceof Commentary)) {
	    throw new IllegalArgumentException("Cannot compare a Commentary"
		    + " to a non-Commentary");
	}
	
	Commentary c = (Commentary) o;
	
	int commQueryResult = commentaryQuery.compareTo(c.commentaryQuery);
	if (commQueryResult != 0) return commQueryResult;
	
	int textQueryResult = textQuery.compareTo(c.textQuery);
	if (textQueryResult != 0) return textQueryResult;
	
	int commSchemeResult =
	    StringUtil.join(commentaryScheme, ":").compareTo(
		    StringUtil.join(c.commentaryScheme, ":"));
	if (commSchemeResult != 0) return commSchemeResult;
	
	return (StringUtil.join(textScheme, ":").compareTo(
		StringUtil.join(c.textScheme, ":")));
    }
    
    public int hashCode() {
	int result = 17;
	
	result += 37 * commentaryQuery.hashCode();
	result += 37 * textQuery.hashCode();
	
	for (String schemePart : commentaryScheme) {
	    result += 37 * schemePart.hashCode();
	}
	for (String schemePart : textScheme) {
	    result += 37 * schemePart.hashCode();
	}
	
	return result;
    }
}
