/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
 */
package perseus.artarch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.artarch.image.dao.HibernateImageNameDAO;
import perseus.artarch.image.dao.HibernateImgDAO;
import perseus.artarch.image.dao.ImageNameDAO;
import perseus.artarch.image.dao.ImgDAO;
import perseus.document.Query;
import perseus.ie.Location;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.entity.dao.EntityOccurrenceDAO;
import perseus.ie.entity.dao.HibernateEntityOccurrenceDAO;

/**
 * ArtifactOccurrenceLoader is a class designed to load data from an XML file
 * and the artifact table of artarch and put it into the ArtifactOccurrence table of artarch
 * BugFix Note: You may need to tweak perseus.document.Query.setQuery to get reflection working
 */
public class ArtifactOccurrenceLoader {

	private static Logger logger = Logger.getLogger(ArtifactOccurrenceLoader.class);
	
	private String artifactType;
	private ArtifactDAO aoDAO;
	private EntityOccurrenceDAO eoDAO;

	/**
	 * @param artifactType is a valid displayName for any ArtifactType
	 */
	public ArtifactOccurrenceLoader(String artifactType) {
		this.artifactType = artifactType;
	}

	/**
	 * Takes 1 argument
	 * args[0] is artifactType, a valid displayName for any ArtifactType
	 * args[0] can be all, which will loop through and load all ArtifactTypes
	 */
	public static void main (String[] args) {
		if (args.length != 1) {
			logger.error("ArtifactOccurrenceLoader Usage: ArtifactOccurrenceLoader [artifactType]");
			System.exit(0);
		}
		ArtifactOccurrenceLoader aool = new ArtifactOccurrenceLoader(args[0]);
		aool.loadObjects();
	}

	/**
	 * Load EntityOccurrences into the database.  Occurrences are loaded for all artifacts of
	 * this.artifactType
	 */
	private void loadObjects() {
		aoDAO = new HibernateArtifactDAO();
		eoDAO = new HibernateEntityOccurrenceDAO();

		List<Artifact> aoList = new ArrayList<Artifact>();
		Artifact ao = getArtifact();
		ao.setType(artifactType);

		//Retrieve all objects of the specified ao type
		aoList = aoDAO.findArtifact(ao);

		//Loop through the objects and construct an occurrence
		Iterator<Artifact> aoIter = aoList.iterator();
		while (aoIter.hasNext()) {
			//Grab all locations, for each one create an EntityOccurrence
			Artifact object = (Artifact)aoIter.next();
			eoDAO.deleteEntityOccurrence((Entity) object);
			HashMap<Location, String> locations = getLocations(object);
			Iterator<Location> locIter = locations.keySet().iterator();
			while (locIter.hasNext()) {
				Location location = (Location)locIter.next();
				String displayText = (String)locations.get(location);
				EntityOccurrence eo = new EntityOccurrence((Entity)object,location,displayText);
				int result = eoDAO.insertEntityOccurrence(eo);
				if (result < 0) {
					logger.error("Could not insert into database: " + eo);
				}
			}
		}
	}

	/**
	 * Load all locations for the Artifact, requires the sor database and
	 *  ImageNames table
	 */
	private HashMap<Location, String> getLocations(Artifact ao) {
		HashMap<Location, String> results = new HashMap<Location, String>();
		String aoName = ao.getName();
		if ((aoName == null) || (aoName.equals(""))) {
			logger.warn("Could not load artifact occurrences because artifact doesn't have a name: " + ao);
		} else {
			ImageNameDAO inDAO = new HibernateImageNameDAO();
			List<String> archiveNumbers = inDAO.getImagesByName(aoName);
			if ((archiveNumbers == null) || (archiveNumbers.size() == 0)) {
				logger.info("Could not load artifact occurrences because there are no archive numbers for artifact: " + ao.getName());
			} else {
				Iterator<String> i = archiveNumbers.iterator();
				while (i.hasNext()) {
					String archiveNumber = (String)i.next();
					ImgDAO iDAO = new HibernateImgDAO();
					String caption = iDAO.getCaption(archiveNumber);
					Query query = new Query(archiveNumber);
					int position = 0;
					Location location = new Location(query,position);
					results.put(location,caption);
				}
			}
		}
		return results;
	}

	/*
	 * Purpose:  Retrieve an artifact of the apropos type depending upon artifactType
	 * Precondition: this.artifactType is set to one of the display names of ArtifactType
	 * Postcondition: The artifact of the specified type is returned, if an unknown type
	 *  then return null
	 */
	private Artifact getArtifact() {
		Artifact artifact = null;
		if (this.artifactType.equals("Building")) {
			artifact = new BuildingArtifact();
		} else if(this.artifactType.equals("Gem")) {
			artifact = new GemArtifact();
		} else if(this.artifactType.equals("Coin")) {
			artifact = new CoinArtifact();
		} else if(this.artifactType.equals("Sculpture")) {
			artifact = new SculptureArtifact();
		} else if(this.artifactType.equals("Site")) {
			artifact = new SiteArtifact();
		} else if(this.artifactType.equals("Vase")) {
			artifact = new VaseArtifact();
		}
		return artifact;
	}
}
