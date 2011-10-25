package perseus.controllers.ie.entity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.entity.HibernateEntityManager;
import perseus.util.HibernateUtil;
import perseus.voting.Vote;
import perseus.voting.dao.HibernateVoteDAO;
import perseus.voting.dao.VoteDAO;

public class RecordEntityVoteController implements Controller {

    private HibernateEntityManager manager = new HibernateEntityManager();

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String authParam = request.getParameter("auth");
	String numParam = request.getParameter("n");
	String queryParam = request.getParameter("doc");

        String selectionParam = request.getParameter("selection");
        String occIDParam = request.getParameter("occurrence_id");
        
        String baseURL = String.format(
        	"redirect:entityvote?auth=%s&n=%s&doc=%s",
		    authParam, numParam, queryParam);

        if (selectionParam != null && occIDParam != null) {
            int occID = Integer.parseInt(occIDParam);
            
            Entity matchingEntity;
            if (selectionParam.equals(Vote.NONE_OF_THE_ABOVE)) {
        	matchingEntity = Vote.NONE_OF_THE_ABOVE;
            } else {
        	matchingEntity = manager.getEntityByAuthName(selectionParam);
            }
            
            EntityOccurrence matchingOccurrence = (EntityOccurrence)
                HibernateUtil.getById(EntityOccurrence.class, occID);

            Vote<Entity> vote = new Vote<Entity>(matchingOccurrence, matchingEntity);
            VoteDAO voteDAO = new HibernateVoteDAO();
            voteDAO.beginTransaction();
            voteDAO.save(vote);
            voteDAO.endTransaction();
            
            return new ModelAndView(
        	    baseURL + String.format("&voted=%s", selectionParam));
        } else {
            return new ModelAndView(baseURL);
        }
    }
}
