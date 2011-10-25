/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
 */
package perseus.artarch;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.artarch.image.Img;
import perseus.artarch.image.SimpleImg;
import perseus.artarch.image.dao.HibernateImgDAO;
import perseus.artarch.image.dao.ImgDAO;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.entity.dao.EntityOccurrenceDAO;
import perseus.ie.entity.dao.HibernateEntityOccurrenceDAO;

/**
 * Associates an EntityOccurrence with an Img object.
 */
public class ArtifactOccurrence {

	private static Logger logger = Logger.getLogger(ArtifactOccurrence.class);
	
	private ImgDAO imgDAO;
	private EntityOccurrence entityOccurrence;
	private Img image;

	public ArtifactOccurrence(){};

	public ArtifactOccurrence(EntityOccurrence eo) {
		imgDAO = new HibernateImgDAO();
		setEntityOccurrence(eo);	
		String archiveNumber = getArchiveNumber(eo);
		Img image = new SimpleImg();
		image = imgDAO.getImg(archiveNumber);
		setImage(image);
	}

	public static String getArchiveNumber(EntityOccurrence eo) {
		String archiveNumber = "";
		archiveNumber = eo.getLocation().getQuery().getDocumentID();
		return archiveNumber;
	}

	public EntityOccurrence getEntityOccurrence() {
		return this.entityOccurrence;
	}

	public void setEntityOccurrence(EntityOccurrence entityOccurrence) {
		this.entityOccurrence = entityOccurrence;
	}

	public Img getImage() {
		return this.image;
	}

	public void setImage(Img image) {
		this.image = image;
	}

	/**
	 * Get a list of ArtifactOccurrences
	 * @param artifact Must be persisted, not meant to be used as a query object though
	 * @return a count of total occurrences for this artifact
	 */
	public static List getTotalEntityOccurrences(Artifact artifact) {
		List entityOccurrences = new ArrayList();
		if (artifact.getId() == null){
			ArtifactDAO aoDAO = new HibernateArtifactDAO();
			List aoList = aoDAO.findArtifact(artifact,0,1);
			logger.warn("Reloading Artifacts: BAD");
			artifact = (Artifact)aoList.get(0);
		}
		EntityOccurrenceDAO eoDAO = new HibernateEntityOccurrenceDAO();
		entityOccurrences = eoDAO.findEntityOccurrence(artifact, null);
		return entityOccurrences;
	}

	/**
	 * Get a list of ArtifactOccurrences
	 * @param artifact Must be persisted, not meant to be used as a query object though
	 * @param occurrenceBound the number of occurrences to load, if occurrenceBound <= 0, then 
	 * retrieve all occurrences, if occurrenceBound > numOccurrences, then retrieve numOccurrences
	 * @return a List of ArtifactOccurrences
	 */
	public static List<ArtifactOccurrence> getArtifactOccurrences(Artifact artifact, int occurrenceBound, HttpServletRequest request) {
		List entityOccurrences = new ArrayList();
		List<ArtifactOccurrence> result = new ArrayList<ArtifactOccurrence>();
		if (artifact.getId() == null){
			ArtifactDAO aoDAO = new HibernateArtifactDAO();
			List aoList = aoDAO.findArtifact(artifact,0,1);
			logger.warn("Reloading Artifacts: BAD");
			artifact = (Artifact)aoList.get(0);
		}
		EntityOccurrenceDAO eoDAO = new HibernateEntityOccurrenceDAO();
		entityOccurrences = eoDAO.findEntityOccurrence(artifact, null);

		if (occurrenceBound <= 0) {
			occurrenceBound = entityOccurrences.size();
		}

		if (occurrenceBound > entityOccurrences.size()) {
			occurrenceBound = entityOccurrences.size();
		}

		for (int i=0; i < occurrenceBound; i++) {
			EntityOccurrence eo = (EntityOccurrence)entityOccurrences.get(i);
			ArtifactOccurrence ao = new ArtifactOccurrence(eo);
			((SimpleImg)ao.getImage()).setURLs();
			((SimpleImg)ao.getImage()).setIsRestricted(request);
			result.add(ao);
		}
		return result;
	}



	public String toString() {
		StringBuffer result = new StringBuffer();
		String newLine = System.getProperty("line.separator");

		result.append( this.getClass().getName() );
		result.append( " Object { ");
		result.append(newLine);

		result.append(" entityOccurrence: ")
		.append(entityOccurrence)
		.append(newLine);

		result.append("}");
		return result.toString();
	}
}    

