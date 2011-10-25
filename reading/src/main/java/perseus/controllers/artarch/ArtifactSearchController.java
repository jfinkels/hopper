/**
 * 
 */
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
import org.springframework.web.servlet.view.RedirectView;

import perseus.artarch.Artifact;
import perseus.artarch.ArtifactOccurrence;
import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.artarch.image.SimpleImg;
import perseus.search.nu.ArtifactSearcher;
import perseus.search.nu.ImgSearcher;
import perseus.search.nu.SearchResult;
import perseus.search.nu.SearchResults;
import perseus.search.nu.ArtifactSearcher.ArtifactSearchResult;
import perseus.search.nu.ArtifactSearcher.ArtifactSearchResults;
import perseus.search.nu.ImgSearcher.ImgSearchResults;
import perseus.util.Config;
import perseus.util.Range;
import perseus.util.Timekeeper;

/**
 * @author rsingh04
 *
 */
public class ArtifactSearchController implements Controller {
	private static Logger logger = Logger.getLogger(ArtifactSearchController.class);
	
	public static final int PAGE_SIZE = 10;  

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Timekeeper keeper = new Timekeeper();
		keeper.start();

		keeper.record("Beginning search result processing");
		
		String queryParam = request.getParameter("q");
		keeper.record("Have search term(s): "+queryParam);		
		
		//first check to see if user entered an artifact name
		ArtifactDAO aDAO = new HibernateArtifactDAO();
		Artifact artifact = aDAO.findArtifactByName(queryParam);
		logger.info("artifact is "+artifact);
		if (artifact != null) {
			logger.info("artifact is not null");
			Map<String, String> model = new HashMap<String, String>();
			model.put("name", artifact.getName());
			model.put("object", artifact.getType());
			logger.info("model is "+model);
			return new ModelAndView(new RedirectView("artifact"), model);
		}
		
		String artifactPage = request.getParameter("artifactPage");
		if (artifactPage == null || artifactPage.equals("")) {
			artifactPage = "0";
		}
		int artifactStart= Integer.parseInt(artifactPage);
		
		String imagePage = request.getParameter("imagePage");
		if (imagePage == null || imagePage.equals("")) {
			imagePage = "0";
		}
		int imageStart = Integer.parseInt(imagePage);
		keeper.record("determined pagination information");

		String searchArtifact = request.getParameter("artifact");
		
		String searchImage = request.getParameter("image");
		
		boolean searchArt = false;
		boolean searchImg = false;
		
		List<String> artifactTypes = new ArrayList<String>();
		Map<String, Boolean> artifactTypesRequested = new HashMap<String, Boolean>();
		Map<Artifact, List<ArtifactOccurrence>> artifactResultOccurrences = null;
		ArtifactSearchResults artifactResults = null;
		Range<Integer> range = Range.range(artifactStart, PAGE_SIZE+artifactStart);
		
		if (searchArtifact != null && !searchArtifact.equals("")) {
			keeper.record("Searching artifacts");
			searchArt = true;
			
			if (request.getParameterValues("artifactType") != null) {
				artifactTypes = Arrays.asList(request.getParameterValues("artifactType"));
			}
			for (String artifactType : Config.artifactTypes) {
				if (artifactTypes.contains(artifactType) || artifactTypes.isEmpty()) {
					artifactTypesRequested.put(artifactType, true);
				} else {
					artifactTypesRequested.put(artifactType, false);
				}
			}
			
			ArtifactSearcher as = new ArtifactSearcher(artifactTypes);
			artifactResults = (ArtifactSearchResults) as.search(queryParam, range);
			
			keeper.record("got artifact hits: " + artifactResults.getTotalHitCount() + " results");
		
			artifactResultOccurrences = new HashMap<Artifact, List<ArtifactOccurrence>>();
			for (ArtifactSearchResult sr : artifactResults.getHits()) {
				Artifact art = (Artifact) sr.getContent();
				List<ArtifactOccurrence> artOccurrences = ArtifactOccurrence.getArtifactOccurrences(art, -1, request);
				artifactResultOccurrences.put(art, artOccurrences);
			}
			keeper.record("got first occurrence for each artifact result");			
		}
		
		List<String> archiveNumbers = null;
		SearchResults imgResults = null;
		
		if (searchImage != null && !searchImage.equals("")) {
			searchImg = true;
			keeper.record("Searching image captions");
			
			ImgSearcher imgSearcher = new ImgSearcher();
			imgResults = imgSearcher.search(queryParam, Range.range(imageStart, PAGE_SIZE+imageStart));

			for (Object result : imgResults.getHits()) {
				SimpleImg img = (SimpleImg) ((SearchResult) result).getContent();
				img.setIsRestricted(request);
			}
			archiveNumbers = ((ImgSearchResults) imgResults).getArchiveNumbers();
			keeper.record("got image hits: "+ imgResults.getTotalHitCount() + " results");
		}
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("query", queryParam);

		myModel.put("artifactResults", artifactResults);
		myModel.put("artifactResultOccs", artifactResultOccurrences);
		myModel.put("artifactPage", artifactStart);
		myModel.put("searchArt", searchArt);
		myModel.put("artifactTypes", artifactTypesRequested);
		
		myModel.put("imagePage", imageStart);
		myModel.put("imgResults", imgResults);
		myModel.put("archiveNumbers", archiveNumbers);
		myModel.put("searchImg", searchImg);

		myModel.put("pageSize", PAGE_SIZE);
		
		keeper.stop();
		logger.info(keeper.getResults());
		
		return new ModelAndView("artifactSearch", "model", myModel);
	}

}
