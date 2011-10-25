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
import perseus.document.Chunk;
import perseus.document.ChunkSchemes;
import perseus.document.GreekTranscoderTokenFilter;
import perseus.document.NoTagsTokenFilter;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.EntityTuple;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;

public class NamedEntityQueryController implements Controller {
	private static Logger logger = Logger.getLogger(NamedEntityQueryController.class);
	private static final int HITS_PER_PAGE = 10;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();
		keeper.record("Beginning query browsing");

		Map<String,Object> myModel = new HashMap<String,Object>();

		DisplayPreferences prefs = new DisplayPreferences(request, response);

		String objectType = request.getParameter("type");
		String queryString = request.getParameter("query");
		String sortMethod = request.getParameter("sort");
		String firstHitText = request.getParameter("start");
		String sortOrder = request.getParameter("order");

		if (sortMethod == null) {
			sortMethod = HibernateEntityManager.FIRST_POSITION;
		}
		myModel.put("sortMethod", sortMethod);
		myModel.put("queryString", queryString);
		
		if (sortOrder == null) {
			sortOrder = HibernateEntityManager.ASCENDING;
		}
		myModel.put("sortOrder", sortOrder);

		if (queryString == null) {
			logger.info("redirecting to search");
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

		URLBuilder urlBuilder = new URLBuilder("nebrowser");
		urlBuilder.setParameter("query", queryString)
		.setParameter("sort", sortMethod)
		.setParameter("order", sortOrder);
		if (objectType != null) {
			urlBuilder.setParameter("type", objectType);
		}
		
		myModel.put("urlBuilder", urlBuilder);

		int resultCount = 0;

		HibernateEntityManager manager = new HibernateEntityManager();

		manager.setMaxResults(HITS_PER_PAGE);
		manager.setFirstResult((firstHit-1)*HITS_PER_PAGE);

		List<EntityTuple> tuples = null;
		List<EntityDocumentFrequency> frequencies = null;
		Query query = new Query(queryString);
		myModel.put("query", query);
		int tupleCount = 0;

		// BLEH
		Class entityClass = null;

		if (objectType != null) {
			try {
				entityClass = Class.forName("perseus.ie.entity." +
						Character.toUpperCase(objectType.charAt(0)) +
						objectType.substring(1));
			} catch (Exception e) {
				// Fall back to searching for all entities
			}
		}

		if (entityClass == null) {
			entityClass = Class.forName("perseus.ie.entity.Entity");
		}

		manager.setSortMethod(sortMethod);
		manager.setSortOrder(sortOrder);
		tuples = manager.getTuples(entityClass, query);
		keeper.record("Got tuples");
		
		ChunkDAO cDAO = new HibernateChunkDAO();
		
		for (EntityTuple et : tuples) {
			List<String> schemes = et.getChunk().getMetadata().getChunkSchemes().getSchemes();
			for (String scheme : schemes) {
				if (scheme.contains(et.getChunk().getType())) {
					Chunk targetChunk = et.getChunk();
					Chunk parentChunk = null;
					Query parentQuery = targetChunk.getQuery();
					List<String> types = ChunkSchemes.typesForScheme(scheme);

					while (true) {
						String targetType = null;
						int targetIndex = types.indexOf(targetChunk.getType());
						if (targetIndex <= 0) {
							break;
						}
						targetType = types.get(targetIndex-1);
						parentChunk = cDAO.getContainingChunk(targetChunk, targetType);
						parentQuery = parentChunk.getQuery().appendSubquery(targetChunk.getQuery().getQuery());
						targetChunk = parentChunk;
					}

					et.setChunk(parentQuery.getChunk());
					break;
				}
			}
		}
		keeper.record("Updated chunks for entity tuples");
		myModel.put("tuples", tuples);

		tupleCount = manager.getTupleCount(entityClass, query);
		keeper.record("Got tuple count");
		resultCount = manager.getOccurrenceCount(null, query, false);
		
		myModel.put("tupleCount", tupleCount);
		myModel.put("resultCount", resultCount);
		keeper.record("Got occurrence count");

		manager.setFirstResult(0);
		manager.setMaxResults(10);
		manager.setSortOrder(HibernateEntityManager.DESCENDING);
		manager.setSortMethod(HibernateEntityManager.FREQUENCY);
		frequencies = manager.getFrequencies(query.getDocumentID(), entityClass);

		myModel.put("frequencies", frequencies);
		keeper.record("Got frequencies");

		Renderer headRenderer = new Renderer();
		headRenderer.addTokenFilter(new GreekTranscoderTokenFilter(prefs));
		headRenderer.addTokenFilter(new NoTagsTokenFilter());

		Renderer textRenderer = new Renderer();
		textRenderer.addTokenFilter(new GreekTranscoderTokenFilter(prefs));

		String[] sortMethods =	{HibernateEntityManager.AUTHORITY_NAME,
				HibernateEntityManager.FIRST_POSITION};
		String[] sortTitles = {"By entity", "By position"};
		String[] sortDescriptions = {"Chronological order for dates, alphabetical order for places and people.",
		"As the entities appear in the document."};
		myModel.put("sortMethods", sortMethods);
		myModel.put("sortTitles", sortTitles);
		myModel.put("sortDescriptions", sortDescriptions);
		
		Renderer renderer = new Renderer();
		renderer.addLanguageTokenFilters(prefs);
		myModel.put("renderer", renderer);
		myModel.put("pager", new Pager((int)tupleCount, HITS_PER_PAGE, firstHit));
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("nequery", myModel);
	}

}
