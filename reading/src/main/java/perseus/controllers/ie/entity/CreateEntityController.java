package perseus.controllers.ie.entity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.ie.entity.Date;
import perseus.ie.entity.DateRange;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.entity.Person;
import perseus.ie.entity.Place;
import perseus.util.HibernateUtil;
import perseus.voting.Vote;
import perseus.voting.dao.HibernateVoteDAO;

public class CreateEntityController implements Controller {

	private static final Map<String,Class<? extends Entity>> typeClasses =
		new HashMap<String,Class<? extends Entity>>() {{
			put("Place", Place.class);
			put("Date", Date.class);
			put("Person", Person.class);
			put("DateRange", DateRange.class);
		}
	};

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String authParam = request.getParameter("auth");
		String numParam = request.getParameter("n");
		String queryParam = request.getParameter("doc");

		String occIDParam = request.getParameter("occurrence_id");
		String entityName = request.getParameter("name");
		String type = request.getParameter("type");

		String baseURL = String.format(
				"redirect:entityvote?auth=%s&n=%s&doc=%s",
				authParam, numParam, queryParam);

		HibernateEntityManager manager = new HibernateEntityManager();
		manager.beginWrite();

		Entity entity = null;
		if (type.equalsIgnoreCase("place")) {
			entity = Place.parseDisplayName(entityName);
		} else if (type.equalsIgnoreCase("person")) {
			entity = Person.parseDisplayName(entityName);
		}

		if (entity != null) {
			manager.registerEntity(entity);

			if (occIDParam != null) {
				int occID = Integer.parseInt(occIDParam);

				Vote<Entity> vote = new Vote<Entity>();
				EntityOccurrence occurrence = (EntityOccurrence)
				HibernateUtil.getById(EntityOccurrence.class, occID);

				vote.setOccurrence(occurrence);

				vote.setSelection(entity);

				new HibernateVoteDAO().save(vote);
			}
			manager.endWrite();

			return new ModelAndView(baseURL + "&voted=" + entity.getAuthorityName());
		} else {
			return new ModelAndView(baseURL);
		}
	}

}
