/**
 * 
 */
package perseus.controllers.artarch;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.artarch.Artifact;
import perseus.artarch.ArtifactOccurrence;
import perseus.artarch.BuildingArtifact;
import perseus.artarch.CoinArtifact;
import perseus.artarch.GemArtifact;
import perseus.artarch.SculptureArtifact;
import perseus.artarch.SiteArtifact;
import perseus.artarch.VaseArtifact;
import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.document.PhraseSearchResultsTokenFilter;
import perseus.document.Renderer;
import perseus.document.SearchResultsTokenFilter;
import perseus.util.Timekeeper;

/**
 * @author rsingh04
 * Based upon work by gweave01 (see ArtifactBrowserController.java)
 * 
 * Controller for dynamically displaying data for an individual Artifact 
 * (rather than using the Perseus catalog texts in the hopper).
 */
public class ArtifactViewerController implements Controller {
	private static Logger logger = Logger.getLogger(ArtifactViewerController.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Timekeeper keeper = new Timekeeper();
		keeper.start();
		
		keeper.record("Beginning artifact processing");

		String artifactName = request.getParameter("name");
		String artifactType = request.getParameter("object");
		
		ArtifactDAO aDAO = new HibernateArtifactDAO();
		Artifact artifact = null;
		if (artifactType.equalsIgnoreCase("Building")) {
			artifact = (BuildingArtifact) aDAO.findArtifact(artifactName, artifactType);
		} else if (artifactType.equalsIgnoreCase("Coin")) {
			artifact = (CoinArtifact) aDAO.findArtifact(artifactName, artifactType);
		} else if (artifactType.equalsIgnoreCase("Gem")) {
			artifact = (GemArtifact) aDAO.findArtifact(artifactName, artifactType);
		} else if (artifactType.equalsIgnoreCase("Sculpture")) {
			artifact = (SculptureArtifact) aDAO.findArtifact(artifactName, artifactType);
		} else if (artifactType.equalsIgnoreCase("Site")) {
			artifact = (SiteArtifact) aDAO.findArtifact(artifactName, artifactType);
		} else if (artifactType.equalsIgnoreCase("Vase")) {
			artifact = (VaseArtifact) aDAO.findArtifact(artifactName, artifactType);
		}
		
		if (artifact == null) {
			return new ModelAndView("artifact", "model", new HashMap<String, Object>());
		}
		keeper.record("Got artifact: "+artifact.getName());
		
		// If we're accessing the text from a search results page, highlight the
		// words the user searched for. If the user searched for a phrase,
		// highlight any occurrences of the whole phrase.

		String[] highlightedWords = null;
		Set<String> searchTerms = null;
		String highlighted = request.getParameter("highlight");
		String searchPhrase = null;

		if (highlighted != null) {
		    if (highlighted.startsWith("\"") && highlighted.endsWith("\"")) {
		        searchPhrase = highlighted;
		    } else {
		        highlightedWords = highlighted.split(",");
		        searchTerms = new HashSet<String>();

		        for (int i = 0; i < highlightedWords.length; i++) {
		        	logger.info("search term is "+highlightedWords[i]);
		            searchTerms.add(highlightedWords[i]);
		        }
		    }
		}
		
		keeper.record("Got search term(s)");
		
		Renderer renderer = new Renderer();
		if (searchTerms != null) {
			renderer.addTokenFilter(new SearchResultsTokenFilter(searchTerms, true));
		}
		if (searchPhrase != null) {
			renderer.addTokenFilter(new PhraseSearchResultsTokenFilter(searchPhrase, true));
		}
		
		//DisplayPreferences prefs = new DisplayPreferences(request, response);
		//renderer.addLanguageTokenFilters(prefs);
		
		Map<String, String> tableProperties = artifact.getTableProperties(renderer);
		keeper.record("got table properties");
		
		Map<String, String> paragraphProperties = artifact.getParagraphProperties(renderer);
		keeper.record("got paragraph properties");

		List<ArtifactOccurrence> artOccs = ArtifactOccurrence.getArtifactOccurrences(artifact, -1, request);
		keeper.record("got artifact occurrences");
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("artifact", artifact);
		myModel.put("tableProperties", tableProperties);
		myModel.put("paragraphProperties", paragraphProperties);
		myModel.put("artOccs", artOccs);
		
		keeper.stop();
		logger.info(keeper.getResults());
		
		return new ModelAndView("artifact", "model", myModel);
	}

}
