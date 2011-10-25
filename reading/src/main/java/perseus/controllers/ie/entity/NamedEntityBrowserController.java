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
import perseus.document.Chunk;
import perseus.document.ChunkSchemes;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.ie.entity.Entity;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.EntityTuple;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;

public class NamedEntityBrowserController implements Controller {
	private static Logger logger = Logger.getLogger(NamedEntityBrowserController.class);
    
	private static final int HITS_PER_PAGE = 10;
    
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Timekeeper keeper = new Timekeeper();
		keeper.start();
		
		Map<String,Object> myModel = new HashMap<String,Object>();

		String queryString = request.getParameter("query");
		String id = request.getParameter("id");
		String sortMethod = request.getParameter("sort");
		String firstHitText = request.getParameter("start");
		String sortOrder = request.getParameter("order");

		String queryMode = request.getParameter("mode");
		if (queryMode != null && queryMode.equals("browse")) {
			logger.info("redirecting to nedocs");
			RequestDispatcher rd = request.getRequestDispatcher("nedocs");
			rd.forward(request, response);
			return null;
		}
		
		if (id == null && queryString == null) {
			logger.info("redirecting to nekeyword");
			RequestDispatcher rd = request.getRequestDispatcher("nekeyword");
			rd.forward(request, response);
			return null;
		}
		
		if (queryString != null && id == null) {
			logger.info("redirecting to nequery");
			RequestDispatcher rd = request.getRequestDispatcher("nequery");
			rd.forward(request, response);
			return null;
		}
		
		myModel.put("id", id);
		myModel.put("sortMethod", sortMethod);

		DisplayPreferences prefs = new DisplayPreferences(request, response);

		int firstHit = 1;
		if (firstHitText != null) {
			try {
				firstHit = Integer.parseInt(firstHitText);
			} catch (NumberFormatException nfe) {
				// okay, whatever
			}
		}

		if (sortOrder == null) {
			sortOrder = HibernateEntityManager.ASCENDING;
		}
		myModel.put("sortOrder", sortOrder);

		URLBuilder urlBuilder = new URLBuilder("nebrowser");
		urlBuilder.setParameter("sort", sortMethod)
		.setParameter("order", sortOrder);
		if (queryString != null) {
			urlBuilder.setParameter("query", queryString);
		}
		urlBuilder.setParameter("start", 0);
		if (id != null) {
			urlBuilder.setParameter("id", URLEncoder.encode(id, "utf-8"));
		}
		myModel.put("urlBuilder", urlBuilder);

		// The number of document sections in which we found results
		int sectionCount = 0;

		// The total number of results
		long resultCount = 0;

		keeper.record("Set various parameters");

		HibernateEntityManager manager = new HibernateEntityManager();

		manager.setMaxResults(HITS_PER_PAGE);
		manager.setFirstResult((firstHit-1)*HITS_PER_PAGE);

		keeper.record("Set fetcher parameters");

		List<EntityTuple> tuples = null;
		List<EntityDocumentFrequency> frequencies = null;
		Entity entity = null;

		Query restrictingQuery = (queryString != null) ? new Query(queryString) : null;
		myModel.put("restrictingQuery", restrictingQuery);
		if (id != null) {
			// If we're searching for a date, we may have already created our entity;
			// otherwise, created it with the id we've been given
			if (entity == null) {
				entity = manager.getEntityByAuthName(id);
			}

			if (entity != null) {
				keeper.record("Looked up entity: " + entity.getClass());

				manager.setSortMethod(HibernateEntityManager.FIRST_POSITION);
				manager.setSortOrder(sortOrder);
				if (restrictingQuery != null) {
					tuples = manager.getTuples(entity, restrictingQuery);
				} else {
					tuples = manager.getTuples(entity);
				}
				keeper.record("Got snippets");
				sectionCount = manager.getTupleCount(entity, restrictingQuery);

				if (restrictingQuery == null) {
					resultCount = entity.getMaxOccurrenceCount();
				} else {
					resultCount = (long) manager.getOccurrenceCount(entity, restrictingQuery, false);
				}

				keeper.record("Got section/document occurrence counts");

				manager.setSortMethod(HibernateEntityManager.COUNT);
				manager.setSortOrder(HibernateEntityManager.DESCENDING);
				frequencies = manager.getFrequencies(entity);
				keeper.record("Got frequencies");
			}
		} else {
			logger.info("redirecting to search");
			RequestDispatcher rd = request.getRequestDispatcher("search");
			rd.forward(request, response);
			//response.sendRedirect("search");
			return null;
		}
		
		List<Metadata> frequencyMetadata = new ArrayList<Metadata>(); 
		for (EntityDocumentFrequency f : frequencies) {
			frequencyMetadata.add(new Query(f.getDocumentID()).getMetadata());
		}

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

		myModel.put("entity", entity);
		myModel.put("frequencies", frequencies);
		myModel.put("frequencyMetadata", frequencyMetadata);
		myModel.put("sectionCount", sectionCount);
		myModel.put("resultCount", resultCount);
		myModel.put("tuples", tuples);
		myModel.put("pager", new Pager((int)sectionCount, HITS_PER_PAGE, firstHit));

		Renderer renderer = new Renderer();
		renderer.addLanguageTokenFilters(prefs);
		myModel.put("renderer", renderer);

		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("nebrowser", myModel);
	}

}
