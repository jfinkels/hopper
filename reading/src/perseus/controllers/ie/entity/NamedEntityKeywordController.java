package perseus.controllers.ie.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.display.Pager;
import perseus.display.URLBuilder;
import perseus.ie.entity.Entity;
import perseus.ie.entity.HibernateEntityManager;
import perseus.util.Timekeeper;

public class NamedEntityKeywordController implements Controller {
	private static Logger logger = Logger.getLogger(NamedEntityKeywordController.class);

	int HITS_PER_PAGE = 20;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();

		Map<String,Object> myModel = new HashMap<String,Object>();

		String objectType = request.getParameter("type");
		String keyword = request.getParameter("keyword");
		String sortMethod = request.getParameter("sort");
		String sortOrder = request.getParameter("order");
		String firstHitText = request.getParameter("start");

		myModel.put("objectType", objectType);
		myModel.put("keyword", keyword);

		if (sortMethod == null) {
			sortMethod = HibernateEntityManager.FREQUENCY;
		}

		// Default sort should be ascending for sorting by entity, descending for
		// sorting by frequency
		if (sortOrder == null) {
			sortOrder = (sortMethod.equals(HibernateEntityManager.FREQUENCY) ?
					HibernateEntityManager.DESCENDING :
						HibernateEntityManager.ASCENDING);
		}

		if (objectType == null) {
			response.sendRedirect("search");
			return null;
		}

		int firstHit = 1;
		if (firstHitText != null) {
			try {
				firstHit = Integer.parseInt(firstHitText);
			} catch (NumberFormatException nfe) {
				// okay, whatever
			}
		}

		Map<String, String> managerParameters = new HashMap<String, String>();

		String placeType = request.getParameter("placetype");
		if (placeType != null) {
			if (placeType.equals("nation")) {
				managerParameters.put("nation", "yes");
			} else if (placeType.equals("state")) {
				managerParameters.put("state", "yes");
			} else if (placeType.equals("siteName")) {
				managerParameters.put("siteName", "yes");
			}
		}

		String[] paramsToAdd = {"siteName", "nation", "state", "placetype",
				"year", "month", "day", "era", "endyear", "endmonth", "endday", "endera",
				"first", "last", "full"};

		URLBuilder urlBuilder = new URLBuilder("nebrowser")
		.setParameter("type", objectType)
		.setParameter("keyword", keyword)
		.setParameter("sort", sortMethod)
		.setParameter("order", sortOrder);

		for (int i = 0; i < paramsToAdd.length; i++) {
			String paramValue = request.getParameter(paramsToAdd[i]);
			if (paramValue != null && paramValue.length() > 0) {
				managerParameters.put(paramsToAdd[i], paramValue);
				urlBuilder.setParameter(paramsToAdd[i], paramValue);
			}
		}
		
		keeper.record("Created url builder and set manager parameters");

		HibernateEntityManager manager = new HibernateEntityManager();
		Class entityClass;
		if (objectType == null) {
			response.sendRedirect("search");
			return null;
		} else if (objectType.equals("person")) {
			entityClass = Class.forName("perseus.ie.entity.Person");
		} else if (objectType.equals("place")) {
			entityClass = Class.forName("perseus.ie.entity.Place");
		} else if (objectType.equals("date")) {
			entityClass = Class.forName("perseus.ie.entity.Date");
		} else if (objectType.equals("lemma")) {
			entityClass = Class.forName("perseus.morph.Lemma");
		} else if (objectType.equals("daterange")) {
			entityClass = Class.forName("perseus.ie.entity.DateRange");
		} else {
			response.sendRedirect("search");
			return null;
		}

		manager.setMaxResults(HITS_PER_PAGE);
		manager.setFirstResult((firstHit-1)*HITS_PER_PAGE);
		manager.setSortMethod(sortMethod);
		manager.setSortOrder(sortOrder);

		List<? extends Entity> results = manager.getMatchingEntities(keyword, entityClass, managerParameters);
		myModel.put("results", results);
		int hitCount = manager.getMatchingEntityCount(keyword, entityClass,	managerParameters);
		keeper.record("Got entity results");
		
		myModel.put("hitCount", hitCount);

		String entityLink;
		String entityText;

		String oppositeSort = HibernateEntityManager.getOppositeSortOrder(sortOrder);

		if (sortMethod.equals(HibernateEntityManager.AUTHORITY_NAME)) {
			entityLink = urlBuilder.withParameter("order", oppositeSort).toString();
			entityText = "Entity&nbsp;" + (sortOrder.equals(HibernateEntityManager.ASCENDING) ? "&#8679;" : "&#8681;");
		} else {
			entityLink = urlBuilder.withParameter("sort", HibernateEntityManager.AUTHORITY_NAME).toString();
			entityText = "Entity";
		}

		String hitsLink;
		String maxHitsText;
		String minHitsText;
		if (sortMethod.equals(HibernateEntityManager.FREQUENCY)) {
			hitsLink = urlBuilder.withParameter("order", HibernateEntityManager.getOppositeSortOrder(sortOrder)).toString();
			maxHitsText = "Max. Freq.&nbsp;" + (sortOrder.equals(HibernateEntityManager.ASCENDING) ? "&#8679;" : "&#8681;");
			minHitsText = "Min. Freq.&nbsp;" + (sortOrder.equals(HibernateEntityManager.ASCENDING) ? "&#8679;" : "&#8681;");
		} else {
			hitsLink = urlBuilder.withParameter("sort", HibernateEntityManager.FREQUENCY).toString();
			maxHitsText = "Max. Freq.";
			minHitsText = "Min. Freq.";
		}
		
		myModel.put("entityLink", entityLink);
		myModel.put("entityText", entityText);
		myModel.put("hitsLink", hitsLink);
		myModel.put("maxHitsText", maxHitsText);
		myModel.put("minHitsText", minHitsText);
		keeper.record("Created link text");
		
		myModel.put("urlBuilder", urlBuilder);
		myModel.put("pager", new Pager((int)hitCount, HITS_PER_PAGE, firstHit));

		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("nekeyword", myModel);
	}

}
