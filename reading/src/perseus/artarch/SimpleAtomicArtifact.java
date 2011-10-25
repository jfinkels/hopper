/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@

SimpleAtomicArtifact utilizes the Decorator/Wrapper pattern around the Artifact
  Refer to:  Bloch, Joshua.  "Effective Java"  Chapter 4, Item 14.
*/
package perseus.artarch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;
import org.w3c.dom.Node;

import perseus.document.Renderer;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityManager;
import perseus.ie.freq.Frequency;
import perseus.voting.Vote;

public class SimpleAtomicArtifact extends AbstractXsltView implements AtomicArtifact {
	
	private static Logger logger = Logger.getLogger(SimpleAtomicArtifact.class);
	
    private Artifact ao = new SimpleArtifact();
    private static Map<String, String> propertyDisplayNames = new HashMap<String, String>();
    private static Map<String, String> propertyNames = new HashMap<String, String>();

    //'atomic' attributes
    public String accessionNumber;
    public String dimensions;
    public String region;
    public String startDate;
    public String startMod;
    public String endDate;
    public String endMod;
    public String unitaryDate;
    public String unitaryMod;
    public String dateForSort;
    public String period;
    public String periodForSort;
    public String culture;
    public String context;
    public String contextMod;
    public String findspot;
    public String findspotMod;
    public String collection;
    public String dateDescription;
    public String collectionHistory;
    public String donor;
    public String condition;
    public String conditionDescription;
    public String comparanda;
    public String material;
    public String materialDescription;
    public String otherNotes;

    static {
	String[] propertyNameArray = {"accessionNumber", "dimensions", "region", "startDate",
				    "startMod", "endDate", "endMod", "unitaryDate", "unitaryMod",
				    "dateForSort", "period", "periodForSort", "culture", "context",
				    "contextMod", "findspot", "findspotMod", "collection", 
				    "dateDescription", "collectionHistory", "donor", "condition",
				    "conditionDescription", "comparanda", "material", 
				    "materialDescription", "otherNotes"};
	String[] propertyDisplayNameArray = {"Accession Number", "Dimensions", "Region", 
					     "Start Date", "Start Mod", "End Date", "End Mod",
					     "Unitary Date", "Unitary Mod", "Date For Sort",
					     "Period", "Period For Sort", "Culture", "Context",
					     "Context Mod", "Findspot", "Findspot Mod",
					     "Collection", "Date Description", "Collection History",
					     "Donor", "Condition", "Condition Description",
					     "Comparanda", "Material", "Material Description", 
					     "Other Notes"};
	SimpleArtifact sa = new SimpleArtifact();
	propertyDisplayNames.putAll(sa.getPropertyDisplayNames());
	propertyNames.putAll(sa.getPropertyNames());
	
	for (int i=0; i < propertyNameArray.length; i++) {
	    propertyDisplayNames.put(propertyNameArray[i], propertyDisplayNameArray[i]);
	    propertyNames.put(propertyDisplayNameArray[i], propertyNameArray[i]);
	}
    }

    public SimpleAtomicArtifact() {
    	//this.ao = new SimpleArtifact();
    }

    public SimpleAtomicArtifact(Artifact ao) {
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
    	ao.willBeRegistered(manager);
    }

    public void willBeUnregistered(EntityManager manager) {
    	ao.willBeUnregistered(manager);
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

    //getter and setter methods for 'atomic' attributes
    public String getAccessionNumber() {
    	return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
    	this.accessionNumber = accessionNumber;
    }

    public String getDimensions() {
    	return dimensions;
    }

    public void setDimensions(String dimensions) {
    	this.dimensions = dimensions;
    }

    public String getRegion() {
    	return region;
    }

    public void setRegion(String region) {
    	this.region = region;
    }
    public String getStartDate() {
    	return startDate;
    }

    public void setStartDate(String startDate) {
    	this.startDate = startDate;
    }

    public String getStartMod() {
    	return startMod;
    }

    public void setStartMod(String startMod) {
    	this.startMod = startMod;
    }

    public String getEndDate() {
    	return endDate;
    }

    public void setEndDate(String endDate) {
    	this.endDate = endDate;
    }

    public String getEndMod() {
    	return endMod;
    }

    public void setEndMod(String endMod) {
    	this.endMod = endMod;
    }

    public String getUnitaryDate() {
    	return unitaryDate;
    }

    public void setUnitaryDate(String unitaryDate) {
    	this.unitaryDate = unitaryDate;
    }

    public String getUnitaryMod() {
    	return unitaryMod;
    }

    public void setUnitaryMod(String unitaryMod) {
    	this.unitaryMod = unitaryMod;
    }

    public String getDateForSort() {
    	return dateForSort;
    }

    public void setDateForSort(String dateForSort) {
    	this.dateForSort = dateForSort;
    }

    public String getPeriod(){
    	return period;
    }

    public void setPeriod(String period) {
    	this.period = period;
    }

    public String getPeriodForSort() {
    	return periodForSort;
    }

    public void setPeriodForSort(String periodForSort) {
    	this.periodForSort = periodForSort;
    }

    public String getCulture() {
    	return culture;
    }

    public void setCulture(String culture) {
    	this.culture = culture;
    }

    public String getContext() {
    	return context;
    }

    public void setContext(String context) {
    	this.context = context;
    }

    public String getContextMod() {
    	return contextMod;
    }

    public void setContextMod(String contextMod) {
    	this.contextMod = contextMod;
    }

    public String getFindspot() {
    	return findspot;
    }

    public void setFindspot(String findspot) {
    	this.findspot = findspot;
    }

    public String getFindspotMod() {
    	return findspotMod;
    }

    public void setFindspotMod(String findspotMod) {
    	this.findspotMod = findspotMod;
    }

    public String getCollection() {
    	return collection;
    }

    public void setCollection(String collection) {
    	this.collection = collection;
    }

    public String getDateDescription() {
    	return dateDescription;
    }

    public void setDateDescription(String dateDescription) {
    	this.dateDescription = dateDescription;
    }

    public String getCollectionHistory() {
    	return collectionHistory;
    }

    public void setCollectionHistory(String collectionHistory) {
    	this.collectionHistory = collectionHistory;
    }

    public String getDonor() {
    	return donor;
    }

    public void setDonor(String donor) {
    	this.donor = donor;
    }

    public String getCondition() {
    	return condition;
    }

    public void setCondition(String condition) {
    	this.condition = condition;
    }

    public String getConditionDescription() {
    	return conditionDescription;
    }

    public void setConditionDescription(String conditionDescription) {
    	this.conditionDescription = conditionDescription;
    }

    public String getComparanda() {
    	return comparanda;
    }

    public void setComparanda(String comparanda) {
    	this.comparanda = comparanda;
    }

    public String getMaterial() {
    	return material;
    }

    public void setMaterial(String material) {
    	this.material = material;
    }

    public String getMaterialDescription() {
    	return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
    	this.materialDescription = materialDescription;
    }

    public String getOtherNotes() {
    	return otherNotes;
    }

    public void setOtherNotes(String otherNotes) {
    	this.otherNotes = otherNotes;
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

    /*
     *Purpose:  Deal with internal entities, at the Data Level
     *This method is a hack and a half and is as ugly as sin...temporary hopefully
    */
    private String convertEntities(String text){
	if(text == null || text.equals("")){
	 
	}else{
	    text = text.replaceAll("&ocirc;","&#x00F4;");
	    text = text.replaceAll("&eacute;","&#x00e9;");
	    text = text.replaceAll("&agrave;","&#x00E0;");
	    text = text.replaceAll("&ouml;","&#x00F6;");
	    text = text.replaceAll("&egrave;","&#x00E8;");
	    text = text.replaceAll("&auml;","&#x00E4;");
	    text = text.replaceAll("&uuml;","&#x00FC;");
	    text = text.replaceAll("&ccedil;","&#x00E7;");
	    text = text.replaceAll("&mdash;","&#x2014;");
	    text = text.replaceAll("&oslash;","&#x00F8;");
	    text = text.replaceAll("&epig-rough;", "[epig-rough]");
	    text = text.replaceAll("&koppa;", "&#x3de;");
	    text = text.replaceAll("&euml;", "&#x00EB;");
	    text = text.replaceAll("&ecirc;","&#x00EA;");
	    text = text.replaceAll("&Eacute;","&#x00C9;");
	    text = text.replaceAll("&aacute;","&#x00E1;");
	    text = text.replaceAll("&inodot;","&#x0131;");
	    text = text.replaceAll("&ecirc;","&#x00EA;");
	    text = text.replaceAll("&ccedil;","&#x00E7;");
	    text = text.replaceAll("&Ouml;", "&#x00D6;");
	    text = text.replaceAll("&iuml;", "&#x00EF;");
	    text = text.replaceAll("&oacute;","&#x00F3;");
	    text = text.replaceAll("&iacute;","&#x00ED;");
	    text = text.replaceAll("&acirc;","&#x00E2;");
	    //Change this to replaceAll("&(?!#)", "&amp;");
	    text = text.replaceAll("&", "&amp;");
	}

	return text;

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
	return null;
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

	public Map<String, String> getTableProperties(Renderer renderer) {
		return null;
		// TODO Auto-generated method stub	
	}
	
	public String createArtifactBrowserURL(String artifactType, String field, String value) {
		return ((SimpleArtifact) ao).createArtifactBrowserURL(artifactType, field, value);
	}
	
	public String getCollectionDisplay(String artifactType) {
		if (collection != null && !collection.equals("")) {
			String collectionDisplay = createArtifactBrowserURL(artifactType, "Collection", collection);
			return collectionDisplay;
		} else {
			return "";
		}
	}
	
	public String getSummaryDisplay() {
		return ((SimpleArtifact) ao).getSummaryDisplay();
	}
	
	public String getContextDisplay(String artifactType) {
		String contextDisplay = "";
		if (contextMod != null && !contextMod.equals("")) {
			contextDisplay = contextMod;
		}
		if (context != null && !context.equals("")) { 
			String contextURL = createArtifactBrowserURL(artifactType, "Context", context);
			if (contextDisplay.equals("")) {
				contextDisplay = contextURL;
			} else {
				contextDisplay = contextDisplay + " " + contextURL;
			}
		}
		return contextDisplay;
	}
	
	public String getFindspotDisplay() {
		String findspotDisplay = "";
		if (findspotMod != null && !findspotMod.equals("")) {
			findspotDisplay = getFindspotMod();
		}
		if (findspot != null && !findspot.equals("")) {
			if (findspotDisplay.equals("")) {
				findspotDisplay = findspot;
			} else {
				findspotDisplay = findspotDisplay + " " + findspot;
			}
		}
		return findspotDisplay;
	}
	
	public String getDateDisplay() {
		String startDateDisplay = "";
		if (startMod != null && !startMod.equals("")) {
			startDateDisplay = getStartMod();
		}
		if (startDate != null && !startDate.equals("")) {
			String year; 
			if (Integer.parseInt(startDate) < 0) {
				year = startDate.substring(1) + " BC";
			} else {
				year = startDate + " AD";
			}
			if (startDateDisplay.equals("")) {
				startDateDisplay = year;
			} else {
				startDateDisplay = startDateDisplay + " " + year;
			}
		}
		String endDateDisplay = "";
		if (endMod != null && !endMod.equals("")) {
			endDateDisplay = getEndMod();
		}
		if (endDate != null && !endDate.equals("")) {
			String year; 
			if (Integer.parseInt(endDate) < 0) {
				year = endDate.substring(1) + " BC";
			} else {
				year = endDate + " AD";
			}
			if (endDateDisplay.equals("")) {
				endDateDisplay = year;
			} else {
				endDateDisplay = endDateDisplay + " " + year;
			}
		}
		String date = "";
		if (!startDateDisplay.equals("") && endDateDisplay.equals("")) {
			date = startDateDisplay;
		} else if (!startDateDisplay.equals("") && !endDateDisplay.equals("")) {
			date = startDateDisplay + " - " + endDateDisplay;
		} else if (startDateDisplay.equals("") && !endDateDisplay.equals("")) {
			date = endDateDisplay;
		} else {
			//Use unitary date
			String unitaryDateDisplay = "";
			if (unitaryMod != null && !unitaryMod.equals("")) {
				unitaryDateDisplay = getUnitaryMod();
			}
			if (unitaryDate != null && !unitaryDate.equals("")) {
				String year;
				if (Integer.parseInt(unitaryDate) < 0) {
					year = unitaryDate.substring(1) + " BC";
				} else {
					year = unitaryDate + " AD";
				}
				if (unitaryDateDisplay.equals("")) {
					date = year;
				} else {
					date = unitaryDateDisplay + " " + year;
				}
			}
		}
		return date;
	}
	
	public String getDimensionsDisplay() {
		if (dimensions != null && !dimensions.equals("")) {
			return dimensions;
		} else {
			return "";
		}
	}
	
	public String getMaterialDisplay(String artifactType) {
		if (material != null && !material.equals("")) {
			String materialURL = createArtifactBrowserURL(artifactType, "Material", material);
			return materialURL;
		} else {
			return "";
		}
	}
	
	public String getRegionDisplay(String artifactType) {
		if (region != null && !region.equals("")) {
			String regionURL = createArtifactBrowserURL(artifactType, "Region", region);
			return regionURL;
		} else {
			return "";
		}
	}
	
	public String getPeriodDisplay(String artifactType) {
		if (period != null && !period.equals("")) {
			String periodURL = createArtifactBrowserURL(artifactType, "Period", period);
			return periodURL;
		} else {
			return "";
		}
	}
	
	public String getCultureDisplay() {
		if (culture != null && !culture.equals("")) {
			return culture;
		} else {
			return "";
		}
	}
	
	public String getDateDescriptionDisplay() {
		if (dateDescription != null && !dateDescription.equals("")) {
			return dateDescription;
		} else {
			return "";
		}
	}
	
	public String getConditionDisplay() {
		if (condition != null && !condition.equals("")) {
			return condition;
		} else {
			return "";
		}
	}
	
	public String getConditionDescriptionDisplay() {
		if (conditionDescription != null && !conditionDescription.equals("")) {
			return conditionDescription;
		} else {
			return "";
		}
	}
	
	public String getMaterialDescriptionDisplay() {
		if (materialDescription != null && !materialDescription.equals("")) {
			return materialDescription;
		} else {
			return "";
		}
	}
	
	public String getCollectionHistoryDisplay() {
		if (collectionHistory != null && !collectionHistory.equals("")) {
			return collectionHistory;
		} else {
			return "";
		}
	}
	
	public String getDonorDisplay() {
		if (donor != null && !donor.equals("")) {
			return donor;
		} else {
			return "";
		}
	}
	
	public String getOtherNotesDisplay() {
		if (otherNotes != null && !otherNotes.equals("")) {
			return otherNotes;
		} else {
			return "";
		}
	}
	
	public String getSourcesUsedDisplay() {
		return ((SimpleArtifact) ao).getSourcesUsedDisplay();
	}
	
	public String getOtherBibliographyDisplay() {
		return ((SimpleArtifact) ao).getOtherBibliographyDisplay();
	}

	public Map<String, String> getParagraphProperties(Renderer renderer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Element toXMLElement() {
		// TODO Auto-generated method stub
		return null;
	}
}

