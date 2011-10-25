package perseus.ie.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import perseus.ie.freq.Frequency;
import perseus.voting.Vote;

/**
 * Convenience class for Entity subclasses; implements getter and setter
 * methods for the entity's authority name and display name.
 */
public abstract class AbstractEntity implements Entity, Serializable {

    protected String authorityName;
    protected String displayName;
    protected String sortableString;
    protected Integer id;
    protected Long maxOccurrenceCount = new Long(0);
    protected Long minOccurrenceCount = new Long(0);
    protected Integer documentCount = new Integer(0);
    protected Double inverseDocumentFrequency = new Double(0.0);
    
    private Set<Vote> votes = new HashSet<Vote>();
    
    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    private Set<Frequency> frequencies = new HashSet<Frequency>();

    public Set<Frequency> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(Set<Frequency> frequencies) {
        this.frequencies = frequencies;
    }

    public AbstractEntity() {}

    public AbstractEntity(String an, String dn) {
	authorityName = an;
	displayName = dn;
    }

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

    public int compareTo(Entity e) {
	return getSortableString().compareTo(e.getSortableString());
    }

    // By default, we really don't care what's going to happen to us.
    public void willBeRegistered(EntityManager manager) {}
    public void willBeUnregistered(EntityManager manager) {}

    public Long getMaxOccurrenceCount() { return maxOccurrenceCount; }
    public void setMaxOccurrenceCount(Long to) { maxOccurrenceCount = to; }

    public Long getMinOccurrenceCount() { return minOccurrenceCount; }
    public void setMinOccurrenceCount(Long to) { minOccurrenceCount = to; }

    protected abstract String toXMLHelper();

    public String toXML() {
    	return new XMLOutputter(Format.getPrettyFormat()).outputString(toXMLElement());
    }
    
    public Element toXMLElement() {
    	Element entity = new Element("entity");
    	entity.setAttribute("id", authorityName);
    	entity.addContent(new Element("displayName").addContent(displayName));
    	entity.addContent(toXMLHelper());
    	
    	return entity;
    }

    public Integer getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public Double getInverseDocumentFrequency() {
        return inverseDocumentFrequency;
    }

    public void setInverseDocumentFrequency(Double inverseDocumentFrequency) {
        this.inverseDocumentFrequency = inverseDocumentFrequency;
    }
}
