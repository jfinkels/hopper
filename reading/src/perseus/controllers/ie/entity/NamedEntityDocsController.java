package perseus.controllers.ie.entity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.display.Pager;
import perseus.display.URLBuilder;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.ie.entity.Entity;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.util.Timekeeper;

public class NamedEntityDocsController implements Controller {
	private static Logger logger = Logger.getLogger(NamedEntityDocsController.class);

	private static final int HITS_PER_PAGE = 50;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();

		Map<String,Object> myModel = new HashMap<String,Object>();

		String entityID = request.getParameter("id");
		String sortMethod = request.getParameter("sort");
		String sortOrder = request.getParameter("order");
		String firstHitText = request.getParameter("start");
		String documentID = request.getParameter("query");

		myModel.put("entityID", entityID);

		if (sortMethod == null) {
			sortMethod = HibernateEntityManager.FREQUENCY;
		}

		if (sortOrder == null) {
			sortOrder = HibernateEntityManager.DESCENDING;
		}

		keeper.record("Got parameters");

		int firstHit = 1;
		if (firstHitText != null) {
			try {
				firstHit = Integer.parseInt(firstHitText);
			} catch (NumberFormatException nfe) {
				// okay, whatever
			}
		}

		HibernateEntityManager manager = new HibernateEntityManager();

		manager.setSortMethod(sortMethod);
		manager.setSortOrder(sortOrder);

		List<EntityDocumentFrequency> frequencies;
		Entity entity = null;
		String title;
		int hitCount;

		// The number of entities appearing in a given document will typically be
		// much higher than the number of documents in which case a single entity
		// appears: in the former case, page through the entities; in the latter case,
		// fetch the documents all at once.
		if (entityID != null) {
			entity = manager.getEntityByAuthName(entityID);
			frequencies = manager.getFrequencies(entity);
			title = "\"" + entity.getDisplayName() + "\"";
			hitCount = frequencies.size();
			int endIndex = Math.min(hitCount, firstHit * HITS_PER_PAGE);
			frequencies = frequencies.subList((firstHit - 1) * HITS_PER_PAGE, endIndex);
		} else if (documentID != null) {
			manager.setMaxResults(HITS_PER_PAGE);
			manager.setFirstResult((firstHit-1)*HITS_PER_PAGE);
			frequencies = manager.getFrequencies(documentID, Entity.class);
			title = new Query(documentID).getMetadata().get(Metadata.TITLE_KEY);
			hitCount = manager.getEntityCount(documentID);
		} else {
			logger.info("redirecting to nebrowser");
			RequestDispatcher rd = request.getRequestDispatcher("nebrowser");
			rd.forward(request, response);
			return null;
		}

		keeper.record("Got entity and frequencies");

		List<Metadata> frequencyMetadata = new ArrayList<Metadata>(); 
		for (EntityDocumentFrequency f : frequencies) {
			frequencyMetadata.add(new Query(f.getDocumentID()).getMetadata());
		}

		myModel.put("title", title);
		myModel.put("hitCount", hitCount);
		myModel.put("entity", entity);
		myModel.put("frequencies", frequencies);
		myModel.put("frequencyMetadata", frequencyMetadata);

		URLBuilder urlBuilder = new URLBuilder("nebrowser")
		.setParameter("mode", "browse")
		.setParameter("sort", sortMethod)
		.setParameter("order", sortOrder)
		.setParameter("query", documentID);

		if (entityID != null) {
			urlBuilder.setParameter("id", URLEncoder.encode(entityID, "utf-8"));
		}

		myModel.put("urlBuilder", urlBuilder);
		keeper.record("Created url builder");

		String documentLink;
		String documentText;

		URLBuilder builder = new URLBuilder("nebrowser")
		.setParameter("mode", "browse")
		.setParameter("sort", sortMethod)
		.setParameter("order", sortOrder);

		if (entityID != null) {
			builder.setParameter("id", URLEncoder.encode(entityID, "utf-8"));
		}

		if (documentID != null) {
			builder.setParameter("query", documentID);
		}

		String oppositeSort = HibernateEntityManager.getOppositeSortOrder(sortOrder);
		String sortField = (entityID != null) ?	HibernateEntityManager.DOCUMENT : HibernateEntityManager.SORTABLE_STRING;
		if (sortMethod.equals(HibernateEntityManager.DOCUMENT)) {
			documentLink = builder.withParameter("order", oppositeSort).toString();
			documentText = "Document&nbsp;" + (sortOrder.equals(HibernateEntityManager.ASCENDING) ? "&#8679;" : "&#8681;");
		} else if (sortMethod.equals(HibernateEntityManager.SORTABLE_STRING)) {
			documentLink = builder.withParameter("order", oppositeSort).toString();
			documentText = "Entity&nbsp;" + (sortOrder.equals(HibernateEntityManager.ASCENDING) ? "&#8679;" : "&#8681;");
		} else {
			documentLink = builder.withParameter("sort", sortField).toString();
			documentText = (entityID != null) ? "Document" : "Entity";
		}

		myModel.put("documentLink", documentLink);
		myModel.put("documentText", documentText);

		String hitsLink;
		String maxHitsText;
		String minHitsText;
		if (sortMethod.equals(HibernateEntityManager.FREQUENCY)) {
			hitsLink = builder.withParameter("order", HibernateEntityManager.getOppositeSortOrder(sortOrder)).toString();
			maxHitsText = "Max. Freq.&nbsp;" + (sortOrder.equals(HibernateEntityManager.ASCENDING) ? "&#8679;" : "&#8681;");
			minHitsText = "Min. Freq.&nbsp;" + (sortOrder.equals(HibernateEntityManager.ASCENDING) ? "&#8679;" : "&#8681;");
		} else {
			hitsLink = builder.withParameter("sort", HibernateEntityManager.FREQUENCY).toString();
			maxHitsText = "Max. Freq.";
			minHitsText = "Min. Freq.";
		}
		
		keeper.record("Created different links");

		myModel.put("hitsLink", hitsLink);
		myModel.put("maxHitsText", maxHitsText);
		myModel.put("minHitsText", minHitsText);

		myModel.put("pager", new Pager((int)hitCount, HITS_PER_PAGE, firstHit));

		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("nedocs", myModel);
	}

}
