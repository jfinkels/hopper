/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
*/

package perseus.artarch.dao;

import java.util.List;
import java.util.Map;

import perseus.artarch.Artifact;
import perseus.artarch.ArtifactKeyword;
import perseus.artarch.ArtifactType;

/**
 * The ArtifactDAO retrieves information about an Artifact from an data source.  Each data source
 *  that provides Artifacts must implement ArtifactDAO
 * <p>
 * This DAO is based upon the sample code found in Sun's article on the DataAccessObject pattern.  
 *   http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html
 * References to patterns are from "Patterns of Enterprise Appliation Architecture", Martin Fowler.
*/
public interface ArtifactDAO {

    /**
     * Save an Artifact to the underlying data source.
     * @param artifact an Artifact with the desired changes authorityName != null.
     * @return true if the update is successful, false otherwise
    */  
    public int insertArtifact(Artifact artifact);
    
    /**
     * Retrieve persisted Artifacts using artifact as a Query Object(316)
     * @param artifact the Query Object Artifact
     * @return a List of all artifacts similar to artifact.
     */
    public List findArtifact(Artifact artifact);

    /** 
     * Retrieve persisted Artifacts using artifact as a Query Object(316)
     * @param artifact the Query Object Artifact
     * @param offset - where to start grabbing Artifacts from the list of matches
     * @param maxResults - how many Artifacts to grab
     */
    public List findArtifact(Artifact artifact, int offset, int maxResults);

    /** 
     * Retrieve persisted Artifacts using artifact as a Query Object(316)
     * @param artifact the Query Object Artifact
     * @param offset - where to start grabbing Artifacts from the list of matches
     * @param maxResults - how many Artifacts to grab
     */
    public List findArtifact(ArtifactType artifactType, String field, String value, int offset, int maxResults);
    
    /**
     * Retrieve the artifact based on name and type
     * @param name
     * @param type
     * @return the Artifact
     */
    public Artifact findArtifact(String name, String type);

    /**
     * Save a persisted object to the underlying data source.
     * @param artifact a persisted Artifact that has 0 or more modifications to save
     * @return true if the update is successful, false otherwise
    */
    public boolean updateArtifact(Artifact artifact);

    /**
     * Remove all artifacts of a specific type
     * @param type
     * @return true if it deleted artifacts, otherwise false
     */
    public boolean deleteAllArtifacts(String type);
    
    /**
     * Remove an Artifact from the underlying data source.
     * @param artifact a persisted Artifact
     * @return true if the delete was a success, false otherwise. 
     */
    public boolean deleteArtifact(Artifact artifact);

    /**
     * Get distinct values for the fieldName of an Artifact from the underlying data source
     * @param artifact Any class that implements Artifact
     * @param fieldName A valid fieldName for the artifact 
     * @return a list of Strings that are values of the fieldName
     */
    public List<String> findDistinctFieldValues(String artifactType, String fieldName);

    /**
     * Get the number of distinct values for the fieldName of an Artifact from the underlying data source
     * @param artifact Any class that implements Artifact
     * @param fieldName A valid fieldName for the artifact
     * @return the number of distinct values for the given artifact and fieldname 
     */
    public int getDistinctFieldValuesHits(Artifact artifact, String fieldName);

    /** 
     * Get the number of Artifacts in the underlying data source similar to artifact
     * @param artifact The underlying Query Object(316)
     * @return the number of Artifacts similar to artifact
     */
    public int getTotalHits(Artifact artifact);

    /**
     * Get a list of one randomly chosen artifact from each artifact type
     * @return the list of randomly chosen artifacts
     */
	public List<Artifact> getRandomArtifacts();
	
	/**
	 * Find artifact based on a given image archive number (uses entity occurrences table)
	 * @param archiveNumber
	 * @return artifact associated with that image
	 */
	public Artifact findArtifact(String archiveNumber);

	/**
	 * Delete existing keywords
	 */
	public void deleteKeywords();

	/**
	 * Get map of distinct keyclasses for a particular artifact type with keyword counts
	 * @param artifactType
	 * @return Map of of keyclasses and their keyword counts
	 */
	public Map<String, Integer> getDistinctKeyclasses(String artifactType);

	/**
	 * Get list of keywords for given artifact type and keyclass
	 * @param artifactType
	 * @param keyclass
	 * @return distinct list of keywords
	 */
	public Map<String, Integer> getDistinctKeywords(String artifactType, String keyclass);

	/**
	 * Get total number of artifacts given artifact type, field and value
	 * @param artType
	 * @param field
	 * @param value
	 * @return count
	 */
	public int getArtifactCount(ArtifactType artType, String field, String value);
	
	/**
	 * Get distinct field values and number of artifacts for that value
	 * @param artifactType
	 * @param fieldName
	 * @return Map of field value and it's count
	 */
	public Map<String, Integer> findDistinctFieldValuesCounts(String artifactType, String fieldName);

	/**
	 * Save ArtifactKeyword object
	 * @param ak
	 */
	public void saveArtifactKeyword(ArtifactKeyword ak);

	/**
	 * Find all artifacts by artifact type, keyclass and keyword.  Also uses pagination.
	 * @param artType
	 * @param keyclass
	 * @param keyword
	 * @param page
	 * @param pageSize
	 * @return List of artifacts given the parameters
	 */
	public List<Artifact> getArtifactsByKeyword(String artType,
			String keyclass, String keyword, int page, int pageSize);

	/**
	 * Get total count of artifacts given artifact type, keyclass and keyword.
	 * @param artType
	 * @param keyclass
	 * @param keyword
	 * @return count of total artifacts
	 */
	public int getCountByKeyword(String artType, String keyclass, String keyword);
	
	/**
	 * Search for artifact by name.
	 * @param name
	 * @return artifact object if there is only one by that name, otherwise null.
	 */
	public Artifact findArtifactByName(String name);

}
