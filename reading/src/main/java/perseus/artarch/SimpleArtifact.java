/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
*/
package perseus.artarch;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;
import org.w3c.dom.Node;

import perseus.document.Renderer;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityManager;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.freq.EntityTuple;
import perseus.ie.freq.Frequency;
import perseus.voting.Vote;

/**
 * SimpleArtifact is a concrete implementation of Artifact and corresponds with ARTIFACT in
 *  ArtifactType
 */
public class SimpleArtifact extends AbstractXsltView implements Artifact {
	
	private static Logger logger = Logger.getLogger(SimpleArtifact.class);

    private String authorityName;
    private String displayName;
    private String sortableString;
    private Integer id;
    private Long maxOccurrenceCount;
    private Long minOccurrenceCount;
    private Set frequencies = new HashSet();
    private Set occurrences = new HashSet();
    private Set tuples = new HashSet();

    //HashMaps for displayNames
    private static Map<String, String> propertyDisplayNames = new HashMap<String, String>();
    private static Map<String, String> propertyNames = new HashMap<String, String>();

    //'object' attributes
    public String name;
    public String type;
    public String location;
    public String summary;
    public String perseusVersion;

    //'documented' attributes
    public String enteredBy;
    public String sourcesUsed;
    public String otherBibliography;
    public String documentaryReferences;


    static {
	String[] propertyNameArray = {"name", "type", "location", "summary", "perseusVersion",
	"enteredBy", "sourcesUsed", "otherBibliography", "documentaryReferences"};
	String[] propertyDisplayNameArray = {"Name", "Artifact Type", "Location", "Summary",
	"Perseus Version", "Entered By", "Sources Used", "Other Bibliography", "Documentary References"};
	
	for (int i=0; i < propertyNameArray.length; i++) {
	    propertyDisplayNames.put(propertyNameArray[i], propertyDisplayNameArray[i]);
	    propertyNames.put(propertyDisplayNameArray[i], propertyNameArray[i]);
	}
    }

    public SimpleArtifact() {}

    // getter and setter methods for 'entity' attributes
    public String getAuthorityName() {
    	return authorityName;
    }

    public void setAuthorityName(String an) {
    	authorityName = an;
    }

    public String getDisplayName() {
    	return displayName;
    }

    public void setDisplayName(String dn) {
    	displayName = dn;
    }

    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public String getSortableString() {
    	return authorityName;
    }

    public void setSortableString(String ss) {
    	sortableString = ss;
    }

    public boolean equals(Object o) {
	if (!(o instanceof Entity)) {
	    return false;
	}

	Entity e = (Entity) o;

	return getAuthorityName().equals(e.getAuthorityName());
    }

    public int compareTo(Entity o) {
	if (!(o instanceof Entity)) {
	    throw new IllegalArgumentException();
	}

	return getSortableString().compareTo(o.getSortableString());
    }

    public Set getOccurrences() { return occurrences; }
    public void setOccurrences(Set occs) { occurrences = occs; }
    public void addOccurrence(EntityOccurrence occ) {
    	occurrences.add(occ);
    	occ.setEntity(this);
    }
    public void clearOccurrences() { occurrences.clear(); }

    public Set getFrequencies() { return frequencies; }
    public void setFrequencies(Set<Frequency> freqs) { frequencies = freqs; }
    public void addFrequency(Frequency freq) {
    	frequencies.add(freq);
    	freq.setEntity(this);
    }
    public void clearFrequencies() { frequencies.clear(); }

    public Set getTuples() { return tuples; }
    public void setTuples(Set tups) { tuples = tups; }
    public void addTuple(EntityTuple tuple) {
    	tuples.add(tuple);
    	tuple.setEntity(this);
    }
    public void clearTuples() { tuples.clear(); }

    // By default, we really don't care what's going to happen to us.
    public void willBeRegistered(EntityManager manager) {}
    public void willBeUnregistered(EntityManager manager) {}

    public Long getMaxOccurrenceCount() { return maxOccurrenceCount; }
    public void setMaxOccurrenceCount(Long to) { maxOccurrenceCount = to; }

    public Long getMinOccurrenceCount() { return minOccurrenceCount; }
    public void setMinOccurrenceCount(Long to) { minOccurrenceCount = to; }

    // getter and setter methods for 'object' attributes

    public String getName() {
    	return name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public String getType() {
    	return type;
    }

    public void setType(String type) {
    	this.type = type;
    }

    public String getLocation() {
    	return location;
    }

    public void setLocation(String location) {
    	this.location = location;
    }

    public String getSummary() {
    	return summary;
    }

    public void setSummary(String summary) {
    	this.summary = summary;
    }

    public String getPerseusVersion() {
    	return perseusVersion;
    }

    public void setPerseusVersion(String perseusVersion) {
    	this.perseusVersion = perseusVersion;
    }

    // getter and setter methods for 'documented' attributes
    public String getEnteredBy() {
    	return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
    	this.enteredBy = enteredBy;
    }

    public String getSourcesUsed() {
    	return sourcesUsed;
    }

    public void setSourcesUsed(String sourcesUsed) {
    	this.sourcesUsed = sourcesUsed;
    }

    public String getOtherBibliography() {
    	return otherBibliography;
    }

    public void setOtherBibliography(String otherBibliography) {
    	this.otherBibliography = otherBibliography;
    }

    public String getDocumentaryReferences() {
    	return documentaryReferences;
    }

    public void setDocumentaryReferences(String documentaryReferences) {
    	this.documentaryReferences = documentaryReferences;
    }

    public String toHTTPRequest() {
	StringBuffer request = new StringBuffer();
	request.append("&name=" + this.name)
	    .append("&type=" + this.type)
	    .append("&location=" + this.location)
	    .append("&summary=" + this.summary)
	    .append("&perseusVersion=" + this.perseusVersion)
	    .append("&enteredBy=" + this.enteredBy)
	    .append("&sourcesUsed=" + this.sourcesUsed)
	    .append("&otherBibliography=" + this.otherBibliography)
	    .append("&documentaryReferences=" + this.documentaryReferences);
	return request.toString().replaceAll("null", "");
    }

    public String toString() {
	StringBuffer result = new StringBuffer();
	String newLine = System.getProperty("line.separator");

	result.append( this.getClass().getName() );
	result.append( " Object {" );
	result.append(newLine);

	//determine fields declared in this class including superclasses
	Method[] methods = this.getClass().getMethods();
	
	//print field names paired with their values
	for (int i=0; i < methods.length;  i++) {
	    Method method = methods[i];
	    if (method.getName().startsWith("get")) {
		result.append("  ");
		try {
		    result.append( method.getName() );
		    result.append(": ");
		    //requires access to private field:
		    result.append(method.invoke(this, (java.lang.Object[]) null));
		} catch (InvocationTargetException ite) {
		   logger.error(ite);
		} catch (Exception e) {
		    logger.error(e);
		}
		result.append(newLine);
	    }//then we have a getter
	}
	result.append("}");
	return result.toString();	
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
    		HttpServletResponse res) 
    throws Exception {
    	Node result = null;

    	Artifact ao = (Artifact)((Map)model.get("model")).get("artifact");
    	String type = ao.getType();
    	if ( (type != null)  && (!type.equals("")) ) {
    		if (type.equals(ArtifactType.BUILDING.getDisplayName())) {
    			result = ((BuildingArtifact)ao).toXML(model, rootName, req, res);
    		} else if (type.equals(ArtifactType.COIN.getDisplayName())) {
    			result = ((CoinArtifact)ao).toXML(model, rootName, req, res);
    		} else if (type.equals(ArtifactType.GEM.getDisplayName())) {
    			result = ((GemArtifact)ao).toXML(model, rootName, req, res);
    		} else if (type.equals(ArtifactType.SCULPTURE.getDisplayName())) {
    			result = ((SculptureArtifact)ao).toXML(model, rootName, req, res);
    		} else if (type.equals(ArtifactType.SITE.getDisplayName())) {
    			result = ((SiteArtifact)ao).toXML(model, rootName, req, res);
    		} else if (type.equals(ArtifactType.VASE.getDisplayName())) {
    			result = ((VaseArtifact)ao).toXML(model, rootName, req, res);
    		} 	
    	} 
    	return new DOMSource(result);
    }

    /*
     * Purpose:  Given a property name, return the display name for
     *  that property.
     * Precondition:  property must be a valid field in SimpleArtifact
     * Postcondition: the propertyDisplayName is returned if a valid
     *  field is recovered, else return null.
     */  
    public Map<String, String> getPropertyDisplayNames() {
	return propertyDisplayNames;
    }

    public Map<String, String> getPropertyNames() {
	return propertyNames;
    }
  
    public String getPropertyDisplayName(String property) {
	return (String)propertyDisplayNames.get(property);
    }

    public String getPropertyName(String propertyDisplayName) {
	return (String)propertyNames.get(propertyDisplayName);
    }
    
    public Node toXML(Map model, String rootName, HttpServletRequest req, 
		      HttpServletResponse res) throws Exception {
    	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }

	public Integer getDocumentCount() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getInverseDocumentFrequency() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Vote> getVotes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDocumentCount(Integer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setInverseDocumentFrequency(Double arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setVotes(Set<Vote> arg0) {
		// TODO Auto-generated method stub
		
	}

	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> getTableProperties(Renderer renderer) {
		return null;		
	}

	public Map<String, String> getParagraphProperties(Renderer renderer) {
		return null;
	}
	
	public String getSummaryDisplay() {
		if (summary != null && !summary.equals("")) {
			return summary;
		} else {
			return "";
		}
	}
	
	public String getSourcesUsedDisplay() {
		if (sourcesUsed != null && !sourcesUsed.equals("")) {
			return sourcesUsed;
		} else {
			return "";
		}
	}
	
	public String getOtherBibliographyDisplay() {
		if (otherBibliography != null && !otherBibliography.equals("")) {
			return otherBibliography;
		} else {
			return "";
		}
	}
	
	public String createArtifactBrowserURL(String artifactType, String field, String value) {
		String URL = "";
		try {
			URL = "<a href=\"/hopper/artifactBrowser?object=" + artifactType 
			+ "&field=" + field + "&value=" + URLEncoder.encode(value, "UTF-8") + "\" >" + value + "</a>";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return URL;
	}

	public Element toXMLElement() {
		// TODO Auto-generated method stub
		return null;
	}
}
