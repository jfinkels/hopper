package perseus.controllers.ie.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.StyleTransformer;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.entity.Person;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;
import perseus.voting.dao.HibernateVoteDAO;
import perseus.voting.dao.VoteDAO;

public class EntityVoteController implements Controller {
	private static Logger logger = Logger.getLogger(EntityVoteController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Timekeeper keeper = new Timekeeper();
		keeper.start();
		
		String authParam = request.getParameter("auth");
		String numParam = request.getParameter("n");
		String queryParam = request.getParameter("doc");
		String votedParam = request.getParameter("voted");

		HibernateEntityManager manager = new HibernateEntityManager();

		int whichOccurrence = 1;

		Query query = new Query(queryParam);

		try {
			whichOccurrence = Integer.parseInt(numParam);
		} catch (NumberFormatException nfe) {
		}

		Chunk chunk = null;
		try {
			chunk = query.getChunk();
		} catch (InvalidQueryException iqe) {
			throw new IllegalArgumentException("we lose");
		}

		Entity matchingEntity = manager.getEntityByAuthName(authParam);
		final EntityOccurrence matchingOccurrence = manager.getOccurrenceByEntity(
				chunk, matchingEntity, whichOccurrence);

		keeper.record("Found entity "+matchingEntity.toString()+" for auth "+ authParam);

		keeper.record("Found occurrence: " + matchingOccurrence);

		Class<? extends Entity> entityClass = matchingEntity.getClass();

		String keyword = matchingOccurrence.getDisplayText();
		Map<String,String> searchParams = getParams(keyword, entityClass);
		keeper.record("Got search params: " + searchParams);

		List<Entity> candidates =
			manager.getMatchingEntities(keyword, entityClass, searchParams);
		keeper.record("Possible matches for " + keyword + ": " + candidates.size());

		// freemarker, wtf?
		VoteDAO voteDAO = new HibernateVoteDAO();
		final Map<Entity,Long> voteCounts = voteDAO.getCounts(matchingOccurrence);
		for (Entity votedEntity : voteCounts.keySet()) {
			if (candidates.indexOf(votedEntity) == -1) {
				candidates.add(votedEntity);
			}
		}

		// sort, making sure the automatically-classified entity is first,
		// followed by the the entities with the most votes;
		Collections.sort(candidates, new Comparator<Entity>() {
			public int compare(Entity e1, Entity e2) {
				long v1 = (voteCounts.containsKey(e1) ? voteCounts.get(e1) : 0L);
				long v2 = (voteCounts.containsKey(e2) ? voteCounts.get(e2) : 0L);
				if (v2 - v1 != 0) return (int) (v2 - v1);

				if (e1 == matchingOccurrence.getEntity()) return -1;
				if (e2 == matchingOccurrence.getEntity()) return 1;

				return (int) (e2.getMaxOccurrenceCount() - e1.getMaxOccurrenceCount());
			}
		});

		List<Long> countList = new ArrayList<Long>();

		List<EntityDocumentFrequency> frequencies =
			new ArrayList<EntityDocumentFrequency>();
		for (Entity candidate : candidates) {
			frequencies.add(
					manager.getFrequency(candidate, query.getDocumentID()));
			if (voteCounts.containsKey(candidate)) {
				countList.add(voteCounts.get(candidate));
			} else {
				countList.add(0L);
			}
		}

		keeper.record("Got counts: " + voteCounts);

		Map<String,Object> modelMap = new HashMap<String,Object>();

		String chunkText = StyleTransformer.transform(chunk.getText(), "striptext.xsl");
		/*
		Renderer renderer = new Renderer(chunk.getEffectiveLanguage());
		renderer.addTokenFilter(new NoTagsTokenFilter());
		String taglessText = renderer.render(chunk.getText());
		 */

		Matcher matcher = Pattern.compile(
				String.format("%s", matchingOccurrence.getDisplayText()))
				.matcher(chunkText);
		int matchCount = 0;
		int startPosition = 0;
		int endPosition = 0;
		int startMatch = 0;
		int endMatch = 0;

		while (matcher.find()) {
			matchCount++;
			if (matchCount == whichOccurrence) {
				startMatch = matcher.start();
				endMatch = matcher.end();
				break;
			}
		}

		startPosition = startMatch - 200;
		if (startPosition < 0) startPosition = 0;

		endPosition = endMatch + 200;
		if (endPosition > chunkText.length()) {
			endPosition = chunkText.length();
		}

		//String matchingText = chunkText.substring(startPosition, endPosition);

		String displayText = String.format(
				"...%s <span class=\"match\">%s</span> %s...",
				chunkText.substring(startPosition, startMatch),
				matchingOccurrence.getDisplayText(),
				chunkText.substring(endMatch, endPosition));

		modelMap.put("header", query.getDisplayQuery());
		modelMap.put("which", whichOccurrence);
		modelMap.put("prefs", new DisplayPreferences(request, response));
		modelMap.put("entities", candidates);
		modelMap.put("occurrence", matchingOccurrence);
		modelMap.put("frequencies", frequencies);
		modelMap.put("voteCounts", countList);
		modelMap.put("metadata", query.getMetadata());
		modelMap.put("chunk_text", displayText);
		modelMap.put("authParam", authParam);
		modelMap.put("numParam", numParam);
		modelMap.put("queryParam", queryParam);
		modelMap.put("votedParam", votedParam);
		modelMap.put("type", matchingEntity.getClass().getSimpleName());
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("entityvote", modelMap);
	}

	private Map<String,String> getParams(String keyword, Class<? extends Entity> entityClass) {
		if (entityClass == Person.class) {
			String[] tokens = keyword.split("\\s+");
			if (tokens.length == 0) return Collections.emptyMap();
			if (tokens.length == 1) {
				return Collections.singletonMap("last", keyword);
			}

			if (tokens.length == 2 &&
					(tokens[0].equals("Mr.") || tokens[0].equals("Sir"))) {
				return Collections.singletonMap("last", tokens[1]);
			}
		}

		return Collections.emptyMap();
	}
}