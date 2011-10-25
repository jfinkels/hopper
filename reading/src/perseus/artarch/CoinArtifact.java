/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@

CoinArtifact utilizes the Decorator/Wrapper pattern around the AtomicArtifact
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

public class CoinArtifact extends AbstractXsltView implements AtomicArtifact {

	private AtomicArtifact aao = new SimpleAtomicArtifact();
	private static Map<String, String> propertyDisplayNames = new HashMap<String, String>();
	private static Map<String, String> propertyNames = new HashMap<String, String>();

	//'coin' attributes
	public String actualWeight;
	public String commentary;
	public String denomination;
	public String dieAxis;
	public String issuingAuthority;
	public String obverseLegend;
	public String obverseType;
	public String reverseLegend;
	public String reverseType;

	static {
		String[] propertyNameArray = {"actualWeight", "commentary", "denomination", "dieAxis",
				"issuingAuthority", "obverseLegend", "obverseType",
				"reverseLegend", "reverseType"};
		String[] propertyDisplayNameArray = {"Actual Weight", "Commentary", "Denomination",
				"Die Axis", "Issuing Authority", "Obverse Legend",
				"Obverse Type", "Reverse Legend", "Reverse Type"};

		SimpleAtomicArtifact saa = new SimpleAtomicArtifact();
		propertyDisplayNames.putAll(saa.getPropertyDisplayNames());
		propertyNames.putAll(saa.getPropertyNames());

		for (int i=0; i < propertyNameArray.length; i++) {
			propertyDisplayNames.put(propertyNameArray[i], propertyDisplayNameArray[i]);
			propertyNames.put(propertyDisplayNameArray[i], propertyNameArray[i]);
		}
	}

	public CoinArtifact() {
		//	SimpleAtomicArtifact aao = new SimpleAtomicArtifact();
		//	this.aao = aao;
	}

	public CoinArtifact(AtomicArtifact aao) {
		this.aao = aao;
	}

	// methods for the Entity implementation
	public Integer getId() {
		return aao.getId();
	}

	public void setId(Integer id) {
		aao.setId(id);
	}

	public String getDisplayName() {
		return aao.getDisplayName();
	}

	public void setDisplayName(String dispName) {
		aao.setDisplayName(dispName);
	}

	public String getAuthorityName() {
		return aao.getAuthorityName();
	}

	public void setAuthorityName(String authName) {
		aao.setAuthorityName(authName);
	}

	public String getSortableString() {
		return aao.getSortableString();
	}

	public void setSortableString(String ss) {
		aao.setSortableString(ss);
	}

	public Long getMinOccurrenceCount() {
		return aao.getMinOccurrenceCount();
	}

	public void setMinOccurrenceCount(Long oc) {
		aao.setMinOccurrenceCount(oc);
	}

	public Long getMaxOccurrenceCount() {
		return aao.getMaxOccurrenceCount();
	}

	public void setMaxOccurrenceCount(Long oc) {
		aao.setMaxOccurrenceCount(oc);
	}

	public boolean equals(Object o) {
		return aao.equals(o);
	}

	/*public int compareTo(Object o) {
    	return aao.compareTo(o);
    }*/

    public void willBeRegistered(EntityManager manager) {
    	aao.willBeRegistered(manager);
    }

    public void willBeUnregistered(EntityManager manager) {
    	aao.willBeUnregistered(manager);
    }    

    // getter and setter methods for 'object' attributes
    public String getName() {
    	return aao.getName();
    }

    public void setName(String name) {
    	aao.setName(name);
    }

    public String getType() {
    	return aao.getType();
    }

    public void setType(String type) {
    	aao.setType(type);
    }

    public String getLocation() {
    	return aao.getLocation();
    }

    public void setLocation(String location) {
    	aao.setLocation(location);
    }

    public String getSummary() {
    	return aao.getSummary();
    }

    public void setSummary(String summary) {
    	aao.setSummary(summary);
    }

    public String getPerseusVersion() {
    	return aao.getPerseusVersion();
    }

    public void setPerseusVersion(String perseusVersion) {
    	aao.setPerseusVersion(perseusVersion);
    }

    // getter and setter methods for 'documented' attributes
    public String getEnteredBy() {
    	return aao.getEnteredBy();
    }

    public void setEnteredBy(String enteredBy) {
    	aao.setEnteredBy(enteredBy);
    }

    public String getSourcesUsed() {
    	return aao.getSourcesUsed();
    }

    public void setSourcesUsed(String sourcesUsed) {
    	aao.setSourcesUsed(sourcesUsed);
    }

    public String getOtherBibliography() {
    	return aao.getOtherBibliography();
    }

    public void setOtherBibliography(String otherBibliography) {
    	aao.setOtherBibliography(otherBibliography);
    }

    public String getDocumentaryReferences() {
    	return aao.getDocumentaryReferences();
    }

    public void setDocumentaryReferences(String documentaryReferences) {
    	aao.setDocumentaryReferences(documentaryReferences);
    }

    //getter and setter methods for 'atomic' attributes
    public String getAccessionNumber() {
    	return aao.getAccessionNumber();
    }

    public void setAccessionNumber(String accessionNumber) {
    	aao.setAccessionNumber(accessionNumber);
    }

    public String getDimensions() {
    	return aao.getDimensions();
    }

    public void setDimensions(String dimensions) {
    	aao.setDimensions(dimensions);
    }

    public String getRegion() {
    	return aao.getRegion();
    }

    public void setRegion(String region) {
    	aao.setRegion(region);
    }
    public String getStartDate() {
    	return aao.getStartDate();
    }

    public void setStartDate(String startDate) {
    	aao.setStartDate(startDate);
    }

    public String getStartMod() {
    	return aao.getStartMod();
    }

    public void setStartMod(String startMod) {
    	aao.setStartMod(startMod);
    }

    public String getEndDate() {
    	return aao.getEndDate();
    }

    public void setEndDate(String endDate) {
    	aao.setEndDate(endDate);
    }

    public String getEndMod() {
    	return aao.getEndMod();
    }

    public void setEndMod(String endMod) {
    	aao.setEndMod(endMod);
    }

    public String getUnitaryDate() {
    	return aao.getUnitaryDate();
    }

    public void setUnitaryDate(String unitaryDate) {
    	aao.setUnitaryDate(unitaryDate);
    }

    public String getUnitaryMod() {
    	return aao.getUnitaryMod();
    }

    public void setUnitaryMod(String unitaryMod) {
    	aao.setUnitaryMod(unitaryMod);
    }

    public String getDateForSort() {
    	return aao.getDateForSort();
    }

    public void setDateForSort(String dateForSort) {
    	aao.setDateForSort(dateForSort);
    }

    public String getPeriod(){
    	return aao.getPeriod();
    }

    public void setPeriod(String period) {
    	aao.setPeriod(period);
    }

    public String getPeriodForSort() {
    	return aao.getPeriodForSort();
    }

    public void setPeriodForSort(String periodForSort) {
    	aao.setPeriodForSort(periodForSort);
    }

    public String getCulture() {
    	return aao.getCulture();
    }

    public void setCulture(String culture) {
    	aao.setCulture(culture);
    }

    public String getContext() {
    	return aao.getContext();
    }

    public void setContext(String context) {
    	aao.setContext(context);
    }

    public String getContextMod() {
    	return aao.getContextMod();
    }

    public void setContextMod(String contextMod) {
    	aao.setContextMod(contextMod);
    }

    public String getFindspot() {
    	return aao.getFindspot();
    }

    public void setFindspot(String findspot) {
    	aao.setFindspot(findspot);
    }

    public String getFindspotMod() {
    	return aao.getFindspotMod();
    }

    public void setFindspotMod(String findspotMod) {
    	aao.setFindspotMod(findspotMod);
    }

    public String getCollection() {
    	return aao.getCollection();
    }

    public void setCollection(String collection) {
    	aao.setCollection(collection);
    }

    public String getDateDescription() {
    	return aao.getDateDescription();
    }

    public void setDateDescription(String dateDescription) {
    	aao.setDateDescription(dateDescription);
    }

    public String getCollectionHistory() {
    	return aao.getCollectionHistory();
    }

    public void setCollectionHistory(String collectionHistory) {
    	aao.setCollectionHistory(collectionHistory);
    }

    public String getDonor() {
    	return aao.getDonor();
    }

    public void setDonor(String donor) {
    	aao.setDonor(donor);
    }

    public String getCondition() {
    	return aao.getCondition();
    }

    public void setCondition(String condition) {
    	aao.setCondition(condition);
    }

    public String getConditionDescription() {
    	return aao.getConditionDescription();
    }

    public void setConditionDescription(String conditionDescription) {
    	aao.setConditionDescription(conditionDescription);
    }

    public String getComparanda() {
    	return aao.getComparanda();
    }

    public void setComparanda(String comparanda) {
    	aao.setComparanda(comparanda);
    }

    public String getMaterial() {
    	return aao.getMaterial();
    }

    public void setMaterial(String material) {
    	aao.setMaterial(material);
    }

    public String getMaterialDescription() {
    	return aao.getMaterialDescription();
    }

    public void setMaterialDescription(String materialDescription) {
    	aao.setMaterialDescription(materialDescription);
    }

    public String getOtherNotes() {
    	return aao.getOtherNotes();
    }

    public void setOtherNotes(String otherNotes) {
    	aao.setOtherNotes(otherNotes);
    }

    // Coin specific attributes

    public String getActualWeight() {
    	return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
    	this.actualWeight = actualWeight;
    }

    public String getCommentary() {
    	return commentary;
    }

    public void setCommentary(String commentary) {
    	this.commentary = commentary;
    }

    public String getDenomination() {
    	return denomination;
    }

    public void setDenomination(String denomination) {
    	this.denomination = denomination;
    }

    public String getDieAxis() {
    	return dieAxis;
    }

    public void setDieAxis(String dieAxis) {
    	this.dieAxis = dieAxis;
    }

    public String getIssuingAuthority() {
    	return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
    	this.issuingAuthority = issuingAuthority;
    }

    public String getObverseLegend() {
    	return obverseLegend;
    }

    public void setObverseLegend(String obverseLegend) {
    	this.obverseLegend = obverseLegend;
    }

    public String getObverseType() {
    	return obverseType;
    }

    public void setObverseType(String obverseType) {
    	this.obverseType = obverseType;
    }

    public String getReverseLegend() {
    	return reverseLegend;
    }

    public void setReverseLegend(String reverseLegend) {
    	this.reverseLegend = reverseLegend;
    }

    public String getReverseType() {
    	return reverseType;
    }

    public void setReverseType(String reverseType) {
    	this.reverseType = reverseType;
    }


    public String toString() {
    	StringBuffer result = new StringBuffer();
    	String newLine = System.getProperty("line.separator");

    	result.append( this.getClass().getName() );
    	result.append( " Object {" );
    	result.append(newLine);

    	//determine fields declared in this class including superclasses
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

    	// On to the Atomic portion of the schema!
    	Element atomicArtifact = new Element("AtomicArtifact", artarch);

    	Element accessionNumber = new Element("accessionNumber", artarch);
    	accessionNumber.setText(getAccessionNumber());
    	atomicArtifact.addContent(accessionNumber);

    	Element dimensions = new Element("dimensions", artarch);
    	dimensions.setText(getDimensions());
    	atomicArtifact.addContent(dimensions);

    	Element region = new Element("region", artarch);
    	region.setText(getRegion());
    	atomicArtifact.addContent(region);

    	Element startDate = new Element("startDate", artarch);
    	startDate.setText(getStartDate());
    	atomicArtifact.addContent(startDate);

    	Element startMod = new Element("startMod", artarch);
    	startMod.setText(getStartMod());
    	atomicArtifact.addContent(startMod);

    	Element endDate = new Element("endDate", artarch);
    	endDate.setText(getEndDate());
    	atomicArtifact.addContent(endDate);

    	Element endMod = new Element("endMod", artarch);
    	endMod.setText(getEndMod());
    	atomicArtifact.addContent(endMod);

    	Element unitaryDate = new Element("unitaryDate", artarch);
    	unitaryDate.setText(getUnitaryDate());
    	atomicArtifact.addContent(unitaryDate);

    	Element unitaryMod = new Element("unitaryMod", artarch);
    	unitaryMod.setText(getUnitaryMod());
    	atomicArtifact.addContent(unitaryMod);

    	Element dateForSort = new Element("dateForSort", artarch);
    	dateForSort.setText(getDateForSort());
    	atomicArtifact.addContent(dateForSort);

    	Element period = new Element("period", artarch);
    	period.setText(getPeriod());
    	atomicArtifact.addContent(period);

    	Element periodForSort = new Element("periodForSort", artarch);
    	periodForSort.setText(getPeriodForSort());
    	atomicArtifact.addContent(periodForSort);

    	Element culture = new Element("culture", artarch);
    	culture.setText(getCulture());
    	atomicArtifact.addContent(culture);

    	Element context = new Element("context", artarch);
    	context.setText(getContext());
    	atomicArtifact.addContent(context);

    	Element contextMod = new Element("contextMod", artarch);
    	contextMod.setText(getContextMod());
    	atomicArtifact.addContent(contextMod);

    	Element findspot = new Element("findspot", artarch);
    	findspot.setText(getFindspot());
    	atomicArtifact.addContent(findspot);

    	Element findspotMod = new Element("findspotMod", artarch);
    	findspotMod.setText(getFindspotMod());
    	atomicArtifact.addContent(findspotMod);

    	Element collection = new Element("collection", artarch);
    	collection.setText(getCollection());
    	atomicArtifact.addContent(collection);

    	Element dateDescription = new Element("dateDescription", artarch);
    	dateDescription.setText(getDateDescription());
    	atomicArtifact.addContent(dateDescription);

    	Element collectionHistory = new Element("collectionHistory", artarch);
    	collectionHistory.setText(getCollectionHistory());
    	atomicArtifact.addContent(collectionHistory);

    	Element donor = new Element("donor", artarch);
    	donor.setText(getDonor());
    	atomicArtifact.addContent(donor);

    	Element condition = new Element("condition", artarch);
    	condition.setText(getCondition());
    	atomicArtifact.addContent(condition);

    	Element conditionDescription = new Element("conditionDescription", artarch);
    	conditionDescription.setText(getConditionDescription());
    	atomicArtifact.addContent(conditionDescription);

    	Element comparanda = new Element("comparanda", artarch);
    	comparanda.setText(getComparanda());
    	atomicArtifact.addContent(comparanda);

    	Element material = new Element("material", artarch);
    	material.setText(getMaterial());
    	atomicArtifact.addContent(material);

    	Element materialDescription = new Element("materialDescription", artarch);
    	material.setText(getMaterialDescription());
    	atomicArtifact.addContent(materialDescription);

    	Element otherNotes = new Element("otherNotes", artarch);
    	otherNotes.setText(getOtherNotes());
    	atomicArtifact.addContent(otherNotes);

    	// Now for the CoinArtifact Portion of the Schema!
    	Element coinArtifact = new Element("CoinArtifact", artarch);	

    	Element actualWeight = new Element("actualWeight", artarch);
    	actualWeight.setText(getActualWeight());
    	coinArtifact.addContent(actualWeight);

    	Element commentary = new Element("commentary", artarch);
    	commentary.setText(getCommentary());
    	coinArtifact.addContent(commentary);

    	Element denomination = new Element("denomination", artarch);
    	denomination.setText(getDenomination());
    	coinArtifact.addContent(denomination);

    	Element dieAxis = new Element("dieAxis", artarch);
    	dieAxis.setText(getDieAxis());
    	coinArtifact.addContent(dieAxis);

    	Element issuingAuthority = new Element("issuingAuthority", artarch);
    	issuingAuthority.setText(getIssuingAuthority());
    	coinArtifact.addContent(issuingAuthority);

    	Element obverseLegend = new Element("obverseLegend", artarch);
    	obverseLegend.setText(getObverseLegend());
    	coinArtifact.addContent(obverseLegend);

    	Element obverseType = new Element("obverseType", artarch);
    	obverseType.setText(getObverseType());
    	coinArtifact.addContent(obverseType);

    	Element reverseLegend = new Element("reverseLegend", artarch);
    	reverseLegend.setText(getReverseLegend());
    	coinArtifact.addContent(reverseLegend);

    	Element reverseType = new Element("reverseType", artarch);
    	reverseType.setText(getReverseType());
    	coinArtifact.addContent(reverseType);

    	artifact.addContent(coinArtifact);
    	artifact.addContent(atomicArtifact); 
    	return artifact;
    }
    
    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
    		HttpServletResponse res) throws Exception {
    	CoinArtifact cao = (CoinArtifact)((Map)model.get("model")).get("artifact");
    	Node caoXML = new DOMOutputter().output(new org.jdom.Document(cao.createXML()));
    	return new DOMSource(caoXML);
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
	
	private String getDenominationDisplay() {
		if (denomination != null && !denomination.equals("")) {
			return ((SimpleAtomicArtifact) aao).createArtifactBrowserURL("Coin", "Denomination", denomination);
		} else {
			return "";
		}
	}
	
	private String getIssuingAuthorityDisplay() {
		if (issuingAuthority != null && !issuingAuthority.equals("")) {
			return ((SimpleAtomicArtifact) aao).createArtifactBrowserURL("Coin", "Issuing Authority", issuingAuthority);
		} else {
			return "";
		}
	}
	
	private String getActualWeightDisplay() {
		if (actualWeight != null && !actualWeight.equals("")) {
			return actualWeight;
		} else {
			return "";
		}
	}
	
	private String getDieAxisDisplay() {
		if (dieAxis != null && !dieAxis.equals("")) {
			return dieAxis;
		} else {
			return "";
		}
	}
	
	private String getObverseTypeDisplay() {
		if (obverseType != null && !obverseType.equals("")) {
			return obverseType;
		} else {
			return "";
		}
	}
	
	private String getReverseTypeDisplay() {
		if (reverseType != null && !reverseType.equals("")) {
			return reverseType;
		} else {
			return "";
		}
	}
	
	private String getCommentaryDisplay() {
		if (commentary != null && !commentary.equals("")) {
			return commentary;
		} else {
			return "";
		}
	}

	public Map<String, String> getTableProperties(Renderer renderer) {
		Map<String, String> properties = new LinkedHashMap<String, String>();

		// Collection
		String collectionDisplay = ((SimpleAtomicArtifact) aao).getCollectionDisplay("Coin");
		if (!collectionDisplay.equals("")) {
			properties.put("Collection", collectionDisplay);
		}
		
		// Summary
		String summaryDisplay = ((SimpleAtomicArtifact) aao).getSummaryDisplay();
		if (!summaryDisplay.equals("")) {
			properties.put("Summary", summaryDisplay);
		}
		
		// Context (Mint for coin)
		String contextDisplay = ((SimpleAtomicArtifact) aao).getContextDisplay("Coin");
		if (!contextDisplay.equals("")) {
			properties.put("Context", contextDisplay);
		}
		
		// Material
		String materialDisplay = ((SimpleAtomicArtifact) aao).getMaterialDisplay("Coin");
		if (!materialDisplay.equals("")) {
			properties.put("Material", materialDisplay);
		}
		
		// Denomination (coin)
		String denomDisplay = getDenominationDisplay();
		if (!denomDisplay.equals("")) {
			properties.put("Denomination", denomDisplay);
		}
		
		// Issuing Authority (coin)
		String issueDisplay = getIssuingAuthorityDisplay();
		if (!issueDisplay.equals("")) {
			properties.put("Issuing Authority", issueDisplay);
		}
		
		// Actual Weight (coin)
		String weightDisplay = getActualWeightDisplay();
		if (!weightDisplay.equals("")) {
			properties.put("Actual Weight", weightDisplay);
		}
		
		// Die Axis (coin)
		String dieAxisDisplay = getDieAxisDisplay();
		if (!dieAxisDisplay.equals("")) {
			properties.put("Die Axis", dieAxisDisplay);
		}
		
		// Findspot
		String findspotDisplay = ((SimpleAtomicArtifact) aao).getFindspotDisplay();
		if (!findspotDisplay.equals("")) {
			properties.put("Findspot", findspotDisplay);
		}
		
		// Date
		String dateDisplay = ((SimpleAtomicArtifact) aao).getDateDisplay();
		if (!dateDisplay.equals("")) {
			properties.put("Date", dateDisplay);
		}
		
		// Dimensions
		String dimensionsDisplay = ((SimpleAtomicArtifact) aao).getDimensionsDisplay();
		if (!dimensionsDisplay.equals("")) {
			properties.put("Dimensions", dimensionsDisplay);
		}
		
		// Region
		String regionDisplay = ((SimpleAtomicArtifact) aao).getRegionDisplay("Coin");
		if (!regionDisplay.equals("")) {
			properties.put("Region", regionDisplay);
		}
		
		// Period
		String periodDisplay = ((SimpleAtomicArtifact) aao).getPeriodDisplay("Coin");
		if (!periodDisplay.equals("")) {
			properties.put("Period", periodDisplay);
		}
		
		// Culture
		String cultureDisplay = ((SimpleAtomicArtifact) aao).getCultureDisplay();
		if (!cultureDisplay.equals("")) {
			properties.put("Culture", cultureDisplay);
		}
		
		return properties;		
	}

	public Map<String, String> getParagraphProperties(Renderer renderer) {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		
		// Obverse Type (coin)
		String obverseDisplay = getObverseTypeDisplay();
		if (!obverseDisplay.equals("")) {
			properties.put("Obverse Type", obverseDisplay);
		}
		
		// Reverse Type (coin)
		String reverseDisplay = getReverseTypeDisplay();
		if (!reverseDisplay.equals("")) {
			properties.put("Obverse Type", reverseDisplay);
		}
		
		// Date Description
		String dateDescriptionDisplay = ((SimpleAtomicArtifact) aao).getDateDescription();
		if (!dateDescriptionDisplay.equals("")) {
			properties.put("Date Description", dateDescriptionDisplay);
		}
		
		// Condition
		String condition = ((SimpleAtomicArtifact) aao).getConditionDisplay();
		if (!condition.equals("")) {
			properties.put("Condition", condition);
		}
		
		// Condition Description
		String conditionDescriptionDisplay = ((SimpleAtomicArtifact) aao).getConditionDescriptionDisplay();
		if (!conditionDescriptionDisplay.equals("")) {
			properties.put("Condition Description", conditionDescriptionDisplay);
		}
		
		// Material description
		String materialDescriptionDisplay = ((SimpleAtomicArtifact) aao).getMaterialDescriptionDisplay();
		if (!materialDescriptionDisplay.equals("")) {
			properties.put("Material Description", materialDescriptionDisplay);
		}
		
		// Collection History
		String collHistoryDisplay = ((SimpleAtomicArtifact) aao).getCollectionHistoryDisplay();
		if (!collHistoryDisplay.equals("")) {
			properties.put("Collection History", collHistoryDisplay);
		}
		
		// Donor
		String donorDisplay = ((SimpleAtomicArtifact) aao).getDonorDisplay();
		if (!donorDisplay.equals("")) {
			properties.put("Donor", donorDisplay);
		}
		
		// Commentary (coin)
		String commDisplay = getCommentaryDisplay();
		if (!commDisplay.equals("")) {
			properties.put("Commentary", commDisplay);
		}
		
		// Other notes
		String otherNotesDisplay = ((SimpleAtomicArtifact) aao).getOtherNotesDisplay();
		if (!otherNotesDisplay.equals("")) {
			properties.put("Other Notes", otherNotesDisplay);
		}
		
		// Sources used
		String sourcesUsedDisplay = ((SimpleAtomicArtifact) aao).getSourcesUsedDisplay();
		if (!sourcesUsedDisplay.equals("")) {
			properties.put("Sources Used", sourcesUsedDisplay);
		}
		
		// Other bibliography
		String otherBibDisplay = ((SimpleAtomicArtifact) aao).getOtherBibliographyDisplay();
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
