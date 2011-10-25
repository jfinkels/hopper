package perseus.ie.entity;

import java.util.Set;

import org.jdom.Element;

import perseus.ie.freq.Frequency;
import perseus.voting.Vote;

/**
 * The base interface for entities, named or unnamed. Implementing this
 * interface, with a corresponding Fetcher class, allows instances of this
 * class to be extracted and presented in various interesting ways to tue
 * caller.
 */
public interface Entity extends Comparable<Entity> {

    public Integer getId();
    public void setId(Integer id);

    /**
     * Returns the name of this entity in a format suitable for display.
     *
     * @return the entity's name in a displayable format
     */
    public String getDisplayName();

    /**
     * Sets this entity's display name.
     *
     * @param dispName the desired display name for the entity
     */
    public void setDisplayName(String dispName);

    /**
     * Returns a unique ID corresponding to this entity, which can be used to
     * access the entity at a later time.
     *
     * @return an authoritative ID for this entity
     */
    public String getAuthorityName();

    /**
     * Sets this entity's ID.
     *
     * @param authName the new ID for the entity
     */
    public void setAuthorityName(String authName);

    /**
     * Returns a String representation of this entity that can be used to sort
     * the entity against others of its kind.
     *
     * @return a sortable String representing this entity
     */
    public String getSortableString();

    public void setSortableString(String ss);

    /**
     * Returns the number of unambiguous occurrences of this entity over the
     * whole collection.
     * @return the number of times this entity appears for certain
     */
    public Long getMinOccurrenceCount();
    public void setMinOccurrenceCount(Long oc);
    
    /**
     * Returns the number of possible occurrences of this entity over the
     * whole collection (not all of them may actually be this entity).
     * 
     * @return the number of times this entity possibly appears
     */
    public Long getMaxOccurrenceCount();
    public void setMaxOccurrenceCount(Long oc);
    
    /**
     * Returns the number of documents this entity appears in.
     * @return the number of documents containing this entity
     */
    public Integer getDocumentCount();
    public void setDocumentCount(Integer dc);
    
    /**
     * Returns this entity's inverse document frequency. Used to help
     * determine TFIDF and avoid repeatedly calculating the IDF.
     *
     * @return this document's IDF
     */
    public Double getInverseDocumentFrequency();
    public void setInverseDocumentFrequency(Double idf);
    
    /**
     * Returns any frequencies pertaining to this entity.
     * @return any relevant Frequency objects
     */
    public Set<Frequency> getFrequencies();
    public void setFrequencies(Set<Frequency> frequencies);

    /**
     * Returns any votes pertaining to this entity.
     * @return any relevant Vote objects
     */
    public Set<Vote> getVotes();
    public void setVotes(Set<Vote> votes);
    
    public boolean equals(Object o);
    public int compareTo(Entity e);

    // These methods are provided so that an entity can perform any necessary
    // preprocessing before being registered or deleted. A DateRange, for
    // example, would need to register both its Dates, or link them up with any
    // matching pre-existing entities.
    public void willBeRegistered(EntityManager manager);
    public void willBeUnregistered(EntityManager manager);

    /**
     * Returns an XML representation of this entity.
     */
    public String toXML();
    
    public Element toXMLElement();
}
