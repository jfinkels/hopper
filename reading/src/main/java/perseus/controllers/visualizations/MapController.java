package perseus.controllers.visualizations;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Metadata;
import perseus.document.Query;
import perseus.util.Timekeeper;

public class MapController implements Controller {
    private static Logger logger = Logger.getLogger(MapController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();
				
		String documentID = request.getParameter("doc");
		String mapID = "all";
		boolean loadTextMap = false;
		
		Metadata metadata = null;
		if (documentID != null && !documentID.equals("")) {
			metadata = new Query(documentID).getMetadata();
			loadTextMap = true;
			mapID = documentID;
		}
		
		keeper.record("Got document id, if there is one");
		
		String creator = null, title = null, language = null;
		if (metadata != null) {
			creator = metadata.getCreator();
			title = metadata.getTitle();
			language = metadata.getLanguage().getName();
			if (language.equals("UNKNOWN")) {
				language = "";
			}
		}
		
		keeper.record("Got metadata information for document");
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("mapID", mapID);
		myModel.put("loadTextMap", loadTextMap);
		myModel.put("creator", creator);
		myModel.put("title", title);
		myModel.put("language", language);
		
		keeper.stop();
		logger.info(keeper.getResults());
		
		return new ModelAndView("map", "model", myModel);
	}

}
