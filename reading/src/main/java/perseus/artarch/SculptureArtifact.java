/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@

SculptureArtifact utilizes the Decorator/Wrapper pattern around the AtomicArtifact
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

public class SculptureArtifact extends AbstractXsltView implements AtomicArtifact {
    
    private AtomicArtifact aao = new SimpleAtomicArtifact();
    private static Map<String, String> propertyDisplayNames = new HashMap<String, String>();
    private static Map<String, String> propertyNames = new HashMap<String, String>();

    public String associatedBuilding;
    public String category;
    public String objectFunction;
    public String graffiti;
    public String inscription;
    public String inscriptionBibliography;
    public String original;
    public String originalOrCopy;
    public String placement;
    public String primaryCitation;
    public String scale;
    public String scaleForSort;
    public String sculptor;
    public String sculptorMod;
    public String style;
    public String formStyleDescription;
    public String subjectDescription;
    public String technique;
    public String techniqueDescription;
    public String title;
    public String sculptureType;
    public String inGroup;
    public String inWhole;

    static {
	String[] propertyNameArray = {"associatedBuilding", "category", "objectFunction", "graffiti", "inscription",
				      "inscriptionBibliography", "original", "originalOrCopy",
				      "placement", "primaryCitation", "scale", "scaleForSort",
				      "sculptor", "sculptorMod", "style", "formStyleDescription",
				      "subjectDescription", "technique", "techniqueDescription",
				      "title", "sculptureType", "inGroup", "inWhole"};
	String[] propertyDisplayNameArray = {"Associated Building", "Category", "Object Function", "Graffiti", "Inscription",
					     "Inscription Bibliography", "Original", "Original Or Copy",
					     "Placement", "Primary Citation", "Scale", "Scale For Sort",
					     "Sculptor", "Sculptor Mod", "Style", "Form Style Description",
					     "Subject Description", "Technique", "Technique Description",
					     "Title", "Sculpture Type", "In Group", "In Whole"};

	SimpleAtomicArtifact saa = new SimpleAtomicArtifact();
	propertyDisplayNames.putAll(saa.getPropertyDisplayNames());
	propertyNames.putAll(saa.getPropertyNames());

	for (int i=0; i < propertyNameArray.length; i++) {
	    propertyDisplayNames.put(propertyNameArray[i], propertyDisplayNameArray[i]);
	    propertyNames.put(propertyDisplayNameArray[i], propertyNameArray[i]);
	}
    }

    public SculptureArtifact() {
    	//	AtomicArtifact aao = new SimpleAtomicArtifact();
    	//	this.aao = aao;
    }

    public SculptureArtifact(AtomicArtifact aao) {
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

    //Sculpture specific attributes
    public String getAssociatedBuilding() {
    	return associatedBuilding;
    }

    public void setAssociatedBuilding(String associatedBuilding) {
    	this.associatedBuilding = associatedBuilding;
    }

    public String getCategory() {
    	return category;
    }

    public void setCategory(String category) {
    	this.category = category;
    }

    public String getObjectFunction() {
    	return objectFunction;
    }

    public void setObjectFunction(String objectFunction) {
    	this.objectFunction = objectFunction;
    }

    public String getGraffiti() {
    	return graffiti;
    }

    public void setGraffiti(String graffiti) {
    	this.graffiti = graffiti;
    }

    public String getInscription() {
    	return inscription;
    }

    public void setInscription(String inscription) {
    	this.inscription = inscription;
    }

    public String getInscriptionBibliography() {
    	return inscriptionBibliography;
    }

    public void setInscriptionBibliography(String inscriptionBibliography) {
    	this.inscriptionBibliography = inscriptionBibliography;
    }

    public String getOriginal() {
    	return original;
    }

    public void setOriginal(String original) {
    	this.original = original;
    }

    public String getOriginalOrCopy() {
    	return originalOrCopy;
    }

    public void setOriginalOrCopy(String originalOrCopy) {
    	this.originalOrCopy = originalOrCopy;
    }

    public String getPlacement() {
    	return placement;
    }

    public void setPlacement(String placement) {
    	this.placement = placement;
    }

    public String getPrimaryCitation() {
    	return primaryCitation;
    }

    public void setPrimaryCitation(String primaryCitation) {
    	this.primaryCitation = primaryCitation;
    }

    public String getScale() {
    	return scale;
    }

    public void setScale(String scale) {
    	this.scale = scale;
    }

    public String getScaleForSort() {
    	return scaleForSort;
    }

    public void setScaleForSort(String scaleForSort) {
    	this.scaleForSort = scaleForSort;
    }

    public String getSculptor() {
    	return sculptor;
    }

    public void setSculptor(String sculptor) {
    	this.sculptor = sculptor;
    }

    public String getSculptorMod() {
    	return sculptorMod;
    }

    public void setSculptorMod(String sculptorMod) {
    	this.sculptorMod = sculptorMod;
    }

    public String getStyle() {
    	return style;
    }

    public void setStyle(String style) {
    	this.style = style;
    }

    public String getFormStyleDescription() {
    	return formStyleDescription;
    }

    public void setFormStyleDescription(String formStyleDescription) {
    	this.formStyleDescription = formStyleDescription;
    }

    public String getSubjectDescription() {
    	return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
    	this.subjectDescription = subjectDescription;
    }

    public String getTechnique() {
    	return technique;
    }

    public void setTechnique(String technique) {
    	this.technique = technique;
    }

    public String getTechniqueDescription() {
    	return techniqueDescription;
    }

    public void setTechniqueDescription(String techniqueDescription) {
    	this.techniqueDescription = techniqueDescription;
    }

    public String getTitle() {
    	return title;
    }

    public void setTitle(String title) {
    	this.title = title;
    }

    public String getSculptureType() {
    	return sculptureType;
    }

    public void setSculptureType(String sculptureType) {
    	this.sculptureType = sculptureType;
    }

    public String getInGroup() {
    	return inGroup;
    }

    public void setInGroup(String inGroup) {
    	this.inGroup = inGroup;
    }

    public String getInWhole() {
    	return inWhole;
    }

    public void setInWhole(String inWhole) {
    	this.inWhole = inWhole;
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

	// Now for the SculptureArtifact portion!
	Element sculptureArtifact = new Element("SculptureArtifact", artarch);

	Element associatedBuilding = new Element("associatedBuilding", artarch);
	associatedBuilding.setText(getAssociatedBuilding());
	sculptureArtifact.addContent(associatedBuilding);
	
	Element category = new Element("category", artarch);
	category.setText(getCategory());
	sculptureArtifact.addContent(category);

	Element objectFunction = new Element("objectFunction", artarch);
	objectFunction.setText(getObjectFunction());
	sculptureArtifact.addContent(objectFunction);

	Element graffiti = new Element("graffiti", artarch);
	graffiti.setText(getGraffiti());
	sculptureArtifact.addContent(graffiti);

	Element inscription = new Element("inscription", artarch);
	inscription.setText(getInscription());
	sculptureArtifact.addContent(inscription);

	Element inscriptionBibliography = new Element("inscriptionBibliography", artarch);
	inscriptionBibliography.setText(getInscriptionBibliography());
	sculptureArtifact.addContent(inscriptionBibliography);

	Element original = new Element("original", artarch);
	original.setText(getOriginal());
	sculptureArtifact.addContent(original);

	Element originalOrCopy = new Element("originalOrCopy", artarch);
	originalOrCopy.setText(getOriginalOrCopy());
	sculptureArtifact.addContent(originalOrCopy);
	
	Element placement = new Element("placement", artarch);
	placement.setText(getPlacement());
	sculptureArtifact.addContent(placement);

	Element primaryCitation = new Element("primaryCitation", artarch);
	primaryCitation.setText(getPrimaryCitation());
	sculptureArtifact.addContent(primaryCitation);

	Element scale = new Element("scale", artarch);
	scale.setText(getScale());
	sculptureArtifact.addContent(scale);

	Element scaleForSort = new Element("scaleForSort", artarch);
	scaleForSort.setText(getScaleForSort());
	sculptureArtifact.addContent(scaleForSort);

	Element sculptor = new Element("sculptor", artarch);
	sculptor.setText(getSculptor());
	sculptureArtifact.addContent(sculptor);

	Element sculptorMod = new Element("sculptorMod", artarch);
	sculptorMod.setText(getSculptorMod());
	sculptureArtifact.addContent(sculptorMod);

	Element style = new Element("style", artarch);
	style.setText(getStyle());
	sculptureArtifact.addContent(style);

	Element formStyleDescription = new Element("formStyleDescription", artarch);
	formStyleDescription.setText(getFormStyleDescription());
	sculptureArtifact.addContent(formStyleDescription);
	
	Element subjectDescription = new Element("subjectDescription", artarch);
	subjectDescription.setText(getSubjectDescription());
	sculptureArtifact.addContent(subjectDescription);

	Element technique = new Element("technique", artarch);
	technique.setText(getTechnique());
	sculptureArtifact.addContent(technique);

	Element techniqueDescription = new Element("techniqueDescription", artarch);
	techniqueDescription.setText(getTechniqueDescription());
	sculptureArtifact.addContent(techniqueDescription);

	Element title = new Element("title", artarch);
	title.setText(getTitle());
	sculptureArtifact.addContent(title);

	Element sculptureType = new Element("sculptureType", artarch);
	sculptureType.setText(getSculptureType());
	sculptureArtifact.addContent(sculptureType);

	Element inGroup = new Element("inGroup", artarch);
	inGroup.setText(getInGroup());
	sculptureArtifact.addContent(inGroup);

	Element inWhole = new Element("inWhole", artarch);
	inWhole.setText(getInWhole());
	sculptureArtifact.addContent(inWhole);

	artifact.addContent(sculptureArtifact);
	artifact.addContent(atomicArtifact);
	return artifact;	
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
    		HttpServletResponse res) throws Exception {
    	SculptureArtifact scao = (SculptureArtifact)((Map)model.get("model")).get("artifact");
    	Node scaoXML = new DOMOutputter().output(new org.jdom.Document(scao.createXML()));
    	return new DOMSource(scaoXML);
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
	
	private String getTitleDisplay() {
		if (title != null && !title.equals("")) {
			return title;
		} else {
			return "";
		}
	}
	
	private String getObjectFunctionDisplay() {
		if (objectFunction != null && !objectFunction.equals("")) {
			return ((SimpleAtomicArtifact) aao).createArtifactBrowserURL("Sculpture", "Object Function", objectFunction);
		} else {
			return "";
		}
	}
	
	private String getSculptorDisplay() {
		String sculptorDisplay = "";
		if (sculptorMod != null && !sculptorMod.equals("")) {
			sculptorDisplay = sculptorMod;
		}
		if (sculptor != null && !sculptor.equals("")) {
			String sculptorURL = ((SimpleAtomicArtifact) aao).createArtifactBrowserURL("Sculpture", "Sculptor", sculptor);
			if (sculptorDisplay.equals("")) {
				sculptorDisplay = sculptorURL;
			} else {
				sculptorDisplay = sculptorDisplay + " " + sculptorURL;
			}
		}
		
		return sculptorDisplay;
	}
	
	private String getSculptureTypeDisplay() {
		if (sculptureType != null && !sculptureType.equals("")) {
			return sculptureType;
		} else {
			return "";
		}
	}
	
	private String getCategoryDisplay() {
		if (category != null && !category.equals("")) {
			return ((SimpleAtomicArtifact) aao).createArtifactBrowserURL("Sculpture", "Category", category);
		} else {
			return "";
		}
	}
	
	private String getPlacementDisplay() {
		if (placement != null && !placement.equals("")) {
			return placement;
		} else {
			return "";
		}
	}
	
	private String getStyleDisplay() {
		if (style != null && !style.equals("")) {
			return style;
		} else {
			return "";
		}
	}
	
	private String getTechniqueDisplay() {
		if (technique != null && !technique.equals("")) {
			return technique;
		} else {
			return "";
		}
	}
	
	private String getOriginalOrCopyDisplay() {
		if (originalOrCopy != null && !originalOrCopy.equals("")) {
			return originalOrCopy;
		} else {
			return "";
		}
	}
	
	private String getOriginalDisplay() {
		if (original != null && !original.equals("")) {
			return original;
		} else {
			return "";
		}
	}
	
	private String getScaleDisplay() {
		if (scale != null && !scale.equals("")) {
			return ((SimpleAtomicArtifact) aao).createArtifactBrowserURL("Sculpture", "Scale", scale);
		} else {
			return "";
		}
	}
	
	private String getInGroupDisplay() {
		if (inGroup != null && !inGroup.equals("")) {
			return inGroup;
		} else {
			return "";
		}
	}
	
	private String getInWholeDisplay() {
		if (inWhole != null & !inWhole.equals("")) {
			return inWhole;
		} else {
			return "";
		}
	}
	
	private String getPrimaryCitationDisplay() {
		if (primaryCitation != null && !primaryCitation.equals("")) {
			return primaryCitation;
		} else {
			return "";
		}
	}
	
	private String getSubjectDescriptionDisplay() {
		if (subjectDescription != null && !subjectDescription.equals("")) {
			return subjectDescription;
		} else {
			return "";
		}
	}
	
	private String getFormStyleDescriptionDisplay() {
		if (formStyleDescription != null && !formStyleDescription.equals("")) {
			return formStyleDescription;
		} else {
			return "";
		}
	}
	
	private String getGraffitiDisplay() {
		if (graffiti != null && !graffiti.equals("")) {
			return graffiti;
		} else {
			return "";
		}
	}
	
	private String getTechniqueDescriptionDisplay() {
		if (techniqueDescription != null && !techniqueDescription.equals("")) {
			return techniqueDescription;
		} else {
			return "";
		}
	}
	
	private String getInscriptionDisplay() {
		if (inscription != null && !inscription.equals("")) {
			return inscription;
		} else {
			return "";
		}
	}
	
	private String getInscriptionBibliographyDisplay() {
		if (inscriptionBibliography != null && !inscriptionBibliography.equals("")) {
			return inscriptionBibliography;
		} else {
			return "";
		}
	}
	
	private String getAssociatedBuildingDisplay() {
		if (associatedBuilding != null && !associatedBuilding.equals("")) {
			return ((SimpleAtomicArtifact) aao).createArtifactBrowserURL("Sculpture", "Associated Building", associatedBuilding);
		} else {
			return "";
		}
	}
	
	public Map<String, String> getTableProperties(Renderer renderer) {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		
		// Collection
		String collectionDisplay = ((SimpleAtomicArtifact) aao).getCollectionDisplay("Sculpture");
		if (!collectionDisplay.equals("")) {
			properties.put("Collection", collectionDisplay);
		}
		
		// Title (sculpture)
		String titleDisplay = getTitleDisplay();
		if (!titleDisplay.equals("")) {
			properties.put("Title", titleDisplay);
		}
		
		// Context
		String contextDisplay = ((SimpleAtomicArtifact) aao).getContextDisplay("Sculpture");
		if (!contextDisplay.equals("")) {
			properties.put("Context", contextDisplay);
		}
		
		// Findspot
		String findspotDisplay = ((SimpleAtomicArtifact) aao).getFindspotDisplay();
		if (!findspotDisplay.equals("")) {
			properties.put("Findspot", findspotDisplay);
		}
		
		// Summary
		String summaryDisplay = ((SimpleAtomicArtifact) aao).getSummaryDisplay();
		if (!summaryDisplay.equals("")) {
			properties.put("Summary", summaryDisplay);
		}
		
		// Object function (sculpture)
		String objectFunctionDisplay = getObjectFunctionDisplay();
		if (!objectFunctionDisplay.equals("")) {
			properties.put("Object Function", objectFunctionDisplay);
		}
		
		// Sculptor (sculpture)
		String sculptorDisplay = getSculptorDisplay();
		if (!sculptorDisplay.equals("")) {
			properties.put("Sculptor", sculptorDisplay);
		}
		
		// Material
		String materialDisplay = ((SimpleAtomicArtifact) aao).getMaterialDisplay("Sculpture");
		if (!materialDisplay.equals("")) {
			properties.put("Material", materialDisplay);
		}
		
		// Sculpture type (sculpture)
		String sculptureTypeDisplay = getSculptureTypeDisplay();
		if (!sculptureTypeDisplay.equals("")) {
			properties.put("Sculpture Type", sculptureTypeDisplay);
		}
		
		// Category (sculpture)
		String categoryDisplay = getCategoryDisplay();
		if (!categoryDisplay.equals("")) {
			properties.put("Category", categoryDisplay);
		}
		
		// Placement (sculpture)
		String placementDisplay = getPlacementDisplay();
		if (!placementDisplay.equals("")) {
			properties.put("Placement", placementDisplay);
		}
		
		// Style (sculpture)
		String styleDisplay = getStyleDisplay();
		if (!styleDisplay.equals("")) {
			properties.put("Style", styleDisplay);
		}
		
		// Technique (sculpture)
		String techniqueDisplay = getTechniqueDisplay();
		if (!techniqueDisplay.equals("")) {
			properties.put("Technique", techniqueDisplay);
		}
		
		// Original or copy (sculpture)
		String originalOrCopyDisplay = getOriginalOrCopyDisplay();
		if (!originalOrCopyDisplay.equals("")) {
			properties.put("Original or Copy", originalOrCopyDisplay);
		}
		
		// Original (sculpture)
		String originalDisplay = getOriginalDisplay();
		if (!originalDisplay.equals("")) {
			properties.put("Original", originalDisplay);
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
		
		// Scale (sculpture)
		String scaleDescription = getScaleDisplay();
		if (!scaleDescription.equals("")) {
			properties.put("Scale", scaleDescription);
		}
		
		// Region
		String regionDisplay = ((SimpleAtomicArtifact) aao).getRegionDisplay("Sculpture");
		if (!regionDisplay.equals("")) {
			properties.put("Region", regionDisplay);
		}
		
		// Period
		String periodDisplay = ((SimpleAtomicArtifact) aao).getPeriodDisplay("Sculpture");
		if (!periodDisplay.equals("")) {
			properties.put("Period", periodDisplay);
		}
		
		// Culture
		String cultureDisplay = ((SimpleAtomicArtifact) aao).getCultureDisplay();
		if (!cultureDisplay.equals("")) {
			properties.put("Culture", cultureDisplay);
		}
		
		// In group (sculpture)
		String inGroupDisplay = getInGroupDisplay();
		if (!inGroupDisplay.equals("")) {
			properties.put("In Group", inGroupDisplay);
		}
		
		// In whole (sculpture)
		String inWholeDisplay = getInWholeDisplay();
		if (!inWholeDisplay.equals("")) {
			properties.put("In Whole", inWholeDisplay);
		}
		
		// Primary citation (sculpture)
		String primaryCitationDisplay = getPrimaryCitationDisplay();
		if (!primaryCitationDisplay.equals("")) {
			properties.put("Primary Citation", primaryCitationDisplay);
		}

		return properties;		
	}

	public Map<String, String> getParagraphProperties(Renderer renderer) {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		
		// Subject Description (sculpture)
		String subjectDisplay = getSubjectDescriptionDisplay();
		if (!subjectDisplay.equals("")) {
			properties.put("Subject Description", subjectDisplay);
		}
		
		// Form Style Description (sculpture)
		String formStyleDisplay = getFormStyleDescriptionDisplay();
		if (!formStyleDisplay.equals("")) {
			properties.put("Form & Style", formStyleDisplay);
		}
		
		// Graffiti (sculpture)
		String graffitiDisplay = getGraffitiDisplay();
		if (!graffitiDisplay.equals("")) {
			properties.put("Graffiti", graffitiDisplay);
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
		
		// Technique description (sculpture)
		String techDescripDisplay = getTechniqueDescriptionDisplay();
		if (!techDescripDisplay.equals("")) {
			properties.put("Technique Description", techDescripDisplay);
		}
		
		// Inscription (sculpture)
		String inscriptionDisplay = getInscriptionDisplay();
		if (!inscriptionDisplay.equals("")) {
			properties.put("Inscription", inscriptionDisplay);
		}
		
		// Inscription bibliography (sculpture)
		String inscriptionBibDisplay = getInscriptionBibliographyDisplay();
		if (!inscriptionBibDisplay.equals("")) {
			properties.put("Inscription Bibliography", inscriptionBibDisplay);
		}
		
		// Associated building (sculpture)
		String assocBuildingDisplay = getAssociatedBuildingDisplay();
		if (!assocBuildingDisplay.equals("")) {
			properties.put("Associated Building", assocBuildingDisplay);
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
