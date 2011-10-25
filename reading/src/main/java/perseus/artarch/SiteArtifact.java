/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@

SiteArtifact utilizes the Decorator/Wrapper pattern around the AtomicArtifact
 Refer to:  Bloch, Joshua.  "Effective Java"  Chapter 4, Item 14.
*/
package perseus.artarch;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.DOMOutputter;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;
import org.w3c.dom.Node;

import perseus.document.Renderer;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityManager;
import perseus.ie.freq.Frequency;
import perseus.voting.Vote;

public class SiteArtifact extends AbstractXsltView implements Artifact {

    private Artifact ao = new SimpleArtifact();
    private static Map<String, String> propertyDisplayNames = new HashMap<String, String>();
    private static Map<String, String> propertyNames = new HashMap<String, String>();

    //'site' attributes
    public String extent;
    public String humanName;
    public String region;
    public String siteType;
    public String description;
    public String exploration;
    public String periods;
    public String physical;

    static {
	String[] propertyNameArray = {"extent", "humanName", "region", "siteType",
				      "description", "exploration", "periods", "physical"};
	String[] propertyDisplayNameArray = {"Extent", "Human Name", "Region", "Site Type",
					     "Description", "Exploration", "Periods", "Physical"};
	SimpleArtifact sa = new SimpleArtifact();
	propertyDisplayNames.putAll(sa.getPropertyDisplayNames());
	propertyNames.putAll(sa.getPropertyNames());

	for (int i=0; i < propertyNameArray.length; i++) {
	    propertyDisplayNames.put(propertyNameArray[i], propertyDisplayNameArray[i]);
	    propertyNames.put(propertyDisplayNameArray[i], propertyNameArray[i]);
	}

    }

    public SiteArtifact() {
    	//	this.ao = new SimpleArtifact();
    }

    public SiteArtifact(Artifact ao) {
    	this.ao = ao;
    }

    // methods for the Entity implementation
    public Integer getId() {
    	return ao.getId();
    }

    public void setId(Integer id) {
    	ao.setId(id);
    }

    public String getDisplayName() {
    	return ao.getDisplayName();
    }

    public void setDisplayName(String dispName) {
    	ao.setDisplayName(dispName);
    }

    public String getAuthorityName() {
    	return ao.getAuthorityName();
    }

    public void setAuthorityName(String authName) {
    	ao.setAuthorityName(authName);
    }

    public String getSortableString() {
    	return ao.getSortableString();
    }

    public void setSortableString(String ss) {
    	ao.setSortableString(ss);
    }

    public Long getMinOccurrenceCount() {
    	return ao.getMinOccurrenceCount();
    }

    public void setMinOccurrenceCount(Long oc) {
    	ao.setMinOccurrenceCount(oc);
    }

    public Long getMaxOccurrenceCount() {
    	return ao.getMaxOccurrenceCount();
    }

    public void setMaxOccurrenceCount(Long oc) {
    	ao.setMaxOccurrenceCount(oc);
    }

    public boolean equals(Object o) {
    	return ao.equals(o);
    }

    /*public int compareTo(Object o) {
    	return ao.compareTo(o);
    }*/

    public void willBeRegistered(EntityManager manager) {
    	this.ao.willBeRegistered(manager);
    }

    public void willBeUnregistered(EntityManager manager) {
    	this.ao.willBeUnregistered(manager);
    }

    // getter and setter methods for 'object' attributes
    public String getName() {
    	return ao.getName();
    }

    public void setName(String name) {
    	ao.setName(name);
    }

    public String getType() {
    	return ao.getType();
    }

    public void setType(String type) {
    	ao.setType(type);
    }

    public String getLocation() {
    	return ao.getLocation();
    }

    public void setLocation(String location) {
    	ao.setLocation(location);
    }

    public String getSummary() {
    	return ao.getSummary();
    }

    public void setSummary(String summary) {
    	ao.setSummary(summary);
    }

    public String getPerseusVersion() {
    	return ao.getPerseusVersion();
    }

    public void setPerseusVersion(String perseusVersion) {
    	ao.setPerseusVersion(perseusVersion);
    }

    // getter and setter methods for 'documented' attributes
    public String getEnteredBy() {
    	return ao.getEnteredBy();
    }

    public void setEnteredBy(String enteredBy) {
    	ao.setEnteredBy(enteredBy);
    }

    public String getSourcesUsed() {
    	return ao.getSourcesUsed();
    }

    public void setSourcesUsed(String sourcesUsed) {
    	ao.setSourcesUsed(sourcesUsed);
    }

    public String getOtherBibliography() {
    	return ao.getOtherBibliography();
    }

    public void setOtherBibliography(String otherBibliography) {
    	ao.setOtherBibliography(otherBibliography);
    }

    public String getDocumentaryReferences() {
    	return ao.getDocumentaryReferences();
    }

    public void setDocumentaryReferences(String documentaryReferences) {
    	ao.setDocumentaryReferences(documentaryReferences);
    }

    // Site specific attributes
    public String getExtent() {
    	return extent;
    }

    public void setExtent(String extent) {
    	this.extent = extent;
    }

    public String getHumanName() {
    	return humanName;
    }

    public void setHumanName(String humanName) {
    	this.humanName = humanName;
    }

    public String getRegion() {
    	return region;
    }

    public void setRegion(String region) {
    	this.region = region;
    }

    public String getSiteType() {
    	return siteType;
    }

    public void setSiteType(String siteType) {
    	this.siteType = siteType;
    }

    public String getDescription() {
    	return description;
    }

    public void setDescription(String description) {
    	this.description = description;
    }

    public String getExploration() {
    	return exploration;
    }

    public void setExploration(String exploration) {
    	this.exploration = exploration;
    }

    public String getPeriods() {
    	return periods;
    }

    public void setPeriods(String periods) {
    	this.periods = periods;
    }

    public String getPhysical() {
    	return physical;
    }

    public void setPhysical(String physical) {
    	this.physical = physical;
    }

    public String toString() {
	StringBuffer result = new StringBuffer();
	String newLine = System.getProperty("line.separator");

	result.append( this.getClass().getName() );
	result.append( " Object {" );
	result.append(newLine);
	result.append("}");
	return result.toString();	
    }

    /*
     * Serialize this Artifact as XML
     */
    public Element createXML() throws Exception {
		Namespace artarch = Namespace.getNamespace("http://www.perseus.tufts.edu/artarch");
	Element artifact = new Element("Artifact", artarch);

	Element name = new Element("name", artarch);
	name.setText(getName());
	artifact.addContent(name);

	Element type = new Element("type", artarch);
	type.setText(getType());
	artifact.addContent(type);

	Element location = new Element("location", artarch);
	location.setText(getLocation());
	artifact.addContent(location);
	
	Element summary = new Element("summary", artarch);
	summary.setText(getSummary());
	artifact.addContent(summary);

	Element perseusVersion = new Element("perseusVersion", artarch);
	perseusVersion.setText(getPerseusVersion());
	artifact.addContent(perseusVersion);

	Element enteredBy = new Element("enteredBy", artarch);
	enteredBy.setText(getEnteredBy());
	artifact.addContent(enteredBy);

	Element sourcesUsed = new Element("sourcesUsed", artarch);
	sourcesUsed.setText(getSourcesUsed());
	artifact.addContent(sourcesUsed);

	Element otherBibliography = new Element("otherBibliography", artarch);
	otherBibliography.setText(getOtherBibliography());
	artifact.addContent(otherBibliography);

	Element documentaryReferences = new Element("documentaryReferences", artarch);
	documentaryReferences.setText(getDocumentaryReferences());
	artifact.addContent(documentaryReferences);

	Element authorityName = new Element("authorityName", artarch);
	authorityName.setText(getAuthorityName());
	artifact.addContent(authorityName);
	
	Element displayName = new Element("displayName", artarch);
	displayName.setText(getDisplayName());
	artifact.addContent(displayName);

	Element sortableString = new Element("sortableString", artarch);
	sortableString.setText(getSortableString());
	artifact.addContent(sortableString);

	Element id = new Element("id", artarch);
	if (getId() != null) {
	    id.setText(getId().toString());
	}
	artifact.addContent(id);

	Element maxOccurrenceCount = new Element("maxOccurrenceCount", artarch);
	maxOccurrenceCount.setText(getMaxOccurrenceCount().toString());
	artifact.addContent(maxOccurrenceCount);

	Element minOccurrenceCount = new Element("minOccurrenceCount", artarch);
	minOccurrenceCount.setText(getMinOccurrenceCount().toString());
	artifact.addContent(minOccurrenceCount);

	Element frequencies = new Element("frequencies", artarch);
	artifact.addContent(frequencies);

	Element occurrences = new Element("occurrences", artarch);
	artifact.addContent(occurrences);
	
	Element tuples = new Element("tuples", artarch);
	artifact.addContent(tuples);
	
	Element siteArtifact = new Element("SiteArtifact", artarch);
	
	Element extent = new Element("extent", artarch);
	extent.setText(getExtent());
	siteArtifact.addContent(extent);

	Element humanName = new Element("humanName", artarch);
	humanName.setText(getHumanName());
	siteArtifact.addContent(humanName);

	Element region = new Element("region", artarch);
	region.setText(getRegion());
	siteArtifact.addContent(region);

	Element siteType = new Element("siteType", artarch);
	siteType.setText(getSiteType());
	siteArtifact.addContent(siteType);

	Element description = new Element("description", artarch);
	description.setText(getDescription());
	siteArtifact.addContent(description);

	Element exploration = new Element("exploration", artarch);
	exploration.setText(getExploration());
	siteArtifact.addContent(exploration);

	Element periods = new Element("periods", artarch);
	periods.setText(getPeriods());
	siteArtifact.addContent(periods);

	Element physical = new Element("physical", artarch);
	physical.setText(getPhysical());
	siteArtifact.addContent(physical);

	artifact.addContent(siteArtifact);
	return artifact;
    }
    
    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
    		HttpServletResponse res) throws Exception {
    	SiteArtifact siao = (SiteArtifact)((Map)model.get("model")).get("artifact");
    	Node siaoXML = new DOMOutputter().output(new org.jdom.Document(siao.createXML()));
    	return new DOMSource(siaoXML);
    }

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

	public int compareTo(Entity arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Integer getDocumentCount() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Frequency> getFrequencies() {
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

	public void setFrequencies(Set<Frequency> arg0) {
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
	
	private String getSiteTypeDisplay() {
		if (siteType != null && !siteType.equals("")) {
			return ((SimpleArtifact) ao).createArtifactBrowserURL("Site", "Site Type", siteType);
		} else {
			return "";
		}
	}
	
	private String getRegionDisplay() {
		if (region != null && !region.equals("")) {
			return ((SimpleArtifact) ao).createArtifactBrowserURL("Site", "Region", region);
		} else {
			return "";
		}
	}
	
	private String getPeriodsDisplay() {
		if (periods != null && !periods.equals("")) {
			return periods;
		} else {
			return "";
		}
	}
	
	private String getPhysicalDisplay() {
		if (physical != null && !physical.equals("")) {
			return physical;
		} else {
			return "";
		}
	}
	
	private String getDescriptionDisplay() {
		if (description != null && !description.equals("")) {
			return description;
		} else {
			return "";
		}
	}
	
	private String getExplorationDisplay() {
		if (exploration != null && !exploration.equals("")) {
			return exploration;
		} else {
			return "";
		}
	}

	public Map<String, String> getTableProperties(Renderer renderer) {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		
		// Summary
		String summaryDisplay = ((SimpleArtifact) ao).getSummaryDisplay();
		if (!summaryDisplay.equals("")) {
			properties.put("Summary", summaryDisplay);
		}
		
		// Site Type (site)
		String typeDisplay = getSiteTypeDisplay();
		if (!typeDisplay.equals("")) {
			properties.put("Type", typeDisplay);
		}
		
		// Region (site)
		String regionDisplay = getRegionDisplay();
		if (!regionDisplay.equals("")) {
			properties.put("Region", regionDisplay);
		}
		
		return properties;		
	}

	public Map<String, String> getParagraphProperties(Renderer renderer) {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		
		// Periods (site)
		String periodsDisplay = getPeriodsDisplay();
		if (!periodsDisplay.equals("")) {
			properties.put("Periods", periodsDisplay);
		}
		
		// Physical (site)
		String physicalDisplay = getPhysicalDisplay();
		if (!physicalDisplay.equals("")) {
			properties.put("Physical", physicalDisplay);
		}
		
		// Description (site)
		String descriptionDisplay = getDescriptionDisplay();
		if (!descriptionDisplay.equals("")) {
			properties.put("Description", descriptionDisplay);
		}
		
		// Exploration (site)
		String explorationDisplay = getExplorationDisplay();
		if (!explorationDisplay.equals("")) {
			properties.put("Exploration", explorationDisplay);
		}
		
		// Sources used
		String sourcesUsedDisplay = ((SimpleArtifact) ao).getSourcesUsedDisplay();
		if (!sourcesUsedDisplay.equals("")) {
			properties.put("Sources Used", sourcesUsedDisplay);
		}
		
		// Other bibliography
		String otherBibDisplay = ((SimpleArtifact) ao).getOtherBibliographyDisplay();
		if (!otherBibDisplay.equals("")) {
			properties.put("Other Bibliography", otherBibDisplay);
		}
		return properties;
	}

	public Element toXMLElement() {
		// TODO Auto-generated method stub
		return null;
	}

}
