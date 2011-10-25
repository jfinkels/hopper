package perseus.controllers.document;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.dao.HibernateWordCountDAO;
import perseus.document.dao.WordCountDAO;
import perseus.util.Config;

public class IndexCollectionsController implements Controller {
    private static Logger logger = Logger.getLogger(IndexCollectionsController.class);

	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		Map<String, Object> myModel = new HashMap<String, Object>();

		String[] collections = Config.getPrimaryCollections();
		WordCountDAO wcDAO = new HibernateWordCountDAO();

		long maxWords = wcDAO.getMaxWords(collections);

		NumberFormat commaFormatter = NumberFormat.getInstance();
		Map<String, String> collCounts = new TreeMap<String, String>();
		Map<String, Integer> collBars = new TreeMap<String, Integer>();
		List<Metadata> collMetadata = new ArrayList<Metadata>();
		
		for (String collection : collections) {
			try {
				Metadata metadata = new Query(collection).getMetadata();
				String displayName = metadata.getAlternativeTitle();

				if (displayName == null) {
					continue;
				} 
				collMetadata.add(metadata);

				long words = wcDAO.getTotalCount(collection);
				int barLength = (int) (((double) words / maxWords) * 100);
				collCounts.put(displayName, commaFormatter.format(words));
				collBars.put(displayName, barLength);
			} catch (Exception e) {
				// Something went wrong; skip this collection!
				logger.warn("Couldn't access coll "+collection, e);
			}
		}
		
		myModel.put("collCounts", collCounts);
		myModel.put("collBars", collBars);
		myModel.put("collMetadata", collMetadata);

		return new ModelAndView("index/collections", "model", myModel);
	}

}
