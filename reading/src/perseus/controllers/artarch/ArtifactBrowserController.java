package perseus.controllers.artarch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.artarch.Artifact;
import perseus.artarch.ArtifactOccurrence;
import perseus.artarch.ArtifactType;
import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.util.Timekeeper;

public class ArtifactBrowserController implements Controller {
	
	private static Logger logger = Logger.getLogger(ArtifactBrowserController.class);

	public static final int PAGE_SIZE = 10;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Timekeeper keeper = new Timekeeper();
		keeper.start();
		
		ArtifactDAO aDAO = new HibernateArtifactDAO();
		Map<String, Object> myModel = new HashMap<String, Object>();
		
		String object = request.getParameter("object");
		if (object != null) {
			object = object.substring(0,1).toUpperCase()+object.substring(1);
		}
		String field = request.getParameter("field");
		if (field != null) {
			field = fixField(field, object);
		}
		String keyclass = request.getParameter("keyclass");
		String keyword = request.getParameter("keyword");
		String value = request.getParameter("value");
		
		String pageString = request.getParameter("page");
		if (pageString == null || pageString.equals("")) {
			pageString = "0";
		}
		int page = Integer.valueOf(pageString);
		myModel.put("pageSize", PAGE_SIZE);
		myModel.put("page", page);
		myModel.put("object", object);
		myModel.put("field", field);
		myModel.put("keyclass", keyclass);
		myModel.put("keyword", keyword);
		myModel.put("value", value);
		
		keeper.record("Got various parameters");
		
		List<Artifact> artifacts = new ArrayList<Artifact>();
		int artifactCount = 0;
		
		//have nothing, first page of browser
		if (object == null || object.equals("")) {
			keeper.record("Choice (1), user has not selected artifact type");
			List<ArtifactType> artifactTypes = ArtifactType.getArtifactTypes();
			myModel.put("artifactTypes", artifactTypes);

			//get random artifacts only on first page of browser
			List<Artifact> randomArtifacts = aDAO.getRandomArtifacts();
			keeper.record("Got random artifacts");
			Map<Artifact, ArtifactOccurrence> randomArtifactImages = new HashMap<Artifact, ArtifactOccurrence>();
			for (Artifact a : randomArtifacts) {
				List<ArtifactOccurrence> artOccs = ArtifactOccurrence.getArtifactOccurrences(a, 1, request);
				ArtifactOccurrence artOcc = null;
				if (!artOccs.isEmpty()) {
					artOcc = artOccs.get(0);
				}
				randomArtifactImages.put(a, artOcc);
			}
			keeper.record("Got images for random artifacts");
			myModel.put("randomArtifacts", randomArtifacts);
			myModel.put("randArtifactImages", randomArtifactImages);
		} else {
			//have artifact type
			ArtifactType artType = ArtifactType.getArtifactType(object);
			if (field == null || field.equals("")) {
				keeper.record("Choice (2), user has selected artifact type but not property");
				List<String> propertyDisplayNames = getAllowedPropertyDisplayNames(artType);
				myModel.put("propertyDisplayNames", propertyDisplayNames);
				keeper.record("Got property display names");
			} else {
				//have artifact type and field, now need to diverge depending on whether or not user chose Keyword
				String propertyName = getPropertyName(artType, field);
				if (field.equalsIgnoreCase("Keyword")) {
					//user hasn't selected keyclass
					if (keyclass == null || keyclass.equals("")) {
						keeper.record("Choice (3a), user has selected Keyword property but not the keyclass");
						Map<String, Integer> keyclassesCounts = aDAO.getDistinctKeyclasses(artType.getDisplayName());
						myModel.put("keyclasses", keyclassesCounts);
						keeper.record("Got keyclass and their keyword counts");
					} else {
						//user hasn't selected keyword
						if (keyword == null || keyword.equals("")) {
							keeper.record("Choice (3b), user has selected Keyword property and keyclass but not keyword");
							Map<String, Integer> keywordsCounts = aDAO.getDistinctKeywords(artType.getDisplayName(), keyclass);
							myModel.put("keywords", keywordsCounts);
							keeper.record("Got keywords and their artifact counts");
						} else {
							keeper.record("Choice (3c), user has selected Keyword property, keyclass and keyword");
							//show artifacts given artifact type, keyclass and keyword
							artifacts = aDAO.getArtifactsByKeyword(artType.getDisplayName(), keyclass, keyword, page, PAGE_SIZE);
							artifactCount = aDAO.getCountByKeyword(artType.getDisplayName(), keyclass, keyword);
							keeper.record("Got artifacts");
						}
					}
				} else {
					if (value == null || value.equals("")) {
						keeper.record("Choice (3i), user has selected object and non-Keyword property, but not value");
						//List<String> fieldValues = aDAO.findDistinctFieldValues(artType.getDisplayName(), field);
						Map<String, Integer> fieldValues = aDAO.findDistinctFieldValuesCounts(artType.getDisplayName(), propertyName);
						myModel.put("fieldValues", fieldValues);
						keeper.record("Got values for property and their artifact counts");
					} else {
						keeper.record("Choice (3ii), user has selected object, non-Keyword property and value");
						//show artifacts given artifact type, field and value
						artifacts = aDAO.findArtifact(artType, propertyName, value, page, PAGE_SIZE);
						artifactCount = aDAO.getArtifactCount(artType, propertyName, value);
						keeper.record("Got artifacts");
					}
				}
			}
		}
		myModel.put("artifacts", artifacts);
		myModel.put("artifactCount", artifactCount);
		
		Map<Artifact, List<ArtifactOccurrence>> artifactOccurrences = new HashMap<Artifact, List<ArtifactOccurrence>>();
		if (artifacts.size() > 0) {
			for (Artifact art : artifacts) {
				List<ArtifactOccurrence> artOccurrences = ArtifactOccurrence.getArtifactOccurrences(art, -1, request);
				artifactOccurrences.put(art, artOccurrences);
			}
			keeper.record("Got artifact occurrences");
		}
		myModel.put("artifactOccs", artifactOccurrences);
		
		keeper.stop();
		logger.info(keeper.getResults());
		
		return new ModelAndView("artifactBrowser", "model", myModel);
	}
	
	/*
	 * Well, P3 changes some fields, so we have to here too so redirects will work properly.
	 */
    private String fixField(String field, String object) {
    	field = field.replaceAll("_", " "); //like Issuing_Authority
    	if (field.equalsIgnoreCase("Function")) {
    		field = "Object Function";
    	}
    	if (field.equalsIgnoreCase("Gem Type")) {
    		field = "Sculpture Type";
    	}
    	if (field.equalsIgnoreCase("Mint") || field.equalsIgnoreCase("Site")) {
    		field = "Context";
    	}
    	if (field.equalsIgnoreCase("Type") || field.equalsIgnoreCase("Class")) {
    		field = object+" Type";
    	}
    	//we don't have Period for Site anymore, so set it to null so user will see all fields for Sites
    	if (object.equalsIgnoreCase("Site") && (field.equalsIgnoreCase("Period") || field.equalsIgnoreCase("Periods"))) {
    		field = null;
    	}
    		
    	return field;
	}

	/**
     * Retrieve the fields which may be seen and queried upon in the context of a UI
     * @param artifactType 
     * @return a List of Strings
     */
	private List<String> getAllowedPropertyDisplayNames(ArtifactType artifactType) {
		List<String> allowFields = new ArrayList<String>();
		if (artifactType == ArtifactType.BUILDING) {
			String[] fieldNames = {"Architect", "Building Type", "Context", "Period", "Region"};
			allowFields = Arrays.asList(fieldNames);
		} 
		else if(artifactType == ArtifactType.COIN) {
			String[] fieldNames = {"Collection", "Context", "Denomination", "Issuing Authority", "Material", "Period", "Region", "Keyword"};
			allowFields = Arrays.asList(fieldNames);
		} 
		else if(artifactType == ArtifactType.GEM) {
			String[] fieldNames = {"Collection", "Material", "Sculpture Type", "Style"};
			allowFields = Arrays.asList(fieldNames);
		} 
		else if(artifactType == ArtifactType.SCULPTURE) {
			String[] fieldNames = {"Associated Building", "Category", "Collection", "Context", "Material", "Object Function", "Period", "Region", "Scale", "Sculptor", "Sculpture Type", "Keyword"};
			allowFields = Arrays.asList(fieldNames);
		} 
		else if(artifactType == ArtifactType.SITE) {
			String[] fieldNames = {"Region", "Site Type"};
			allowFields = Arrays.asList(fieldNames);
		} 
		else if(artifactType == ArtifactType.VASE) {
			String[] fieldNames = {"Collection", "Context", "Painter", "Period", "Potter", "Region", "Shape", "Ware", "Keyword"};
			allowFields = Arrays.asList(fieldNames);
		}
		return allowFields;
	}
	
	private String getPropertyName(ArtifactType artifactType, String propertyDisplayName) {
		String propertyName = "";
		try {
			Artifact artifact = (Artifact)artifactType.getArtifactClass().newInstance();
			propertyName = artifact.getPropertyName(propertyDisplayName);
		} catch (Exception e) {
			logger.error("Could not retrieve artifactType " + e);
		}
		return propertyName;
	}

}
