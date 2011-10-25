package perseus.controllers.morph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Query;
import perseus.document.Renderer;
import perseus.morph.Parse;
import perseus.util.DisplayPreferences;
import perseus.util.Timekeeper;
import perseus.voting.FormInstance;
import perseus.voting.Sense;
import perseus.voting.VoteManager;

public class SubmitVoteController implements Controller {
	private static Logger logger = Logger.getLogger(SubmitVoteController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();

		boolean badRequest = false;
		boolean recordedVote = false;

		String voteType = request.getParameter("type");

		String document = request.getParameter("doc");
		String subquery = request.getParameter("subquery");
		
		String form = request.getParameter("form");
		String which = request.getParameter("which");
		String languageCode = request.getParameter("lang");
		String identity = request.getParameter("identity");
		String lemma = null;
		String morphCode = null;
		String senseID = null;
		String lexQuery = null;

		boolean hasMorph = false;
		boolean hasSense = false;

		if (document == null || form == null || which == null) {
			response.sendRedirect("error.jsp");
			return null;
		}

		Query query = new Query(document, subquery);
		keeper.record("Got query "+query.toString());

		document = query.getDocumentID();
		subquery = query.getQuery();

		// If the user has just opened a new document, what he's reading may not yet
		// have a particular subquery associated with it; the sense_votes wants
		// non-null subqueries to help it keep track of unique votes, so add one.
		if (subquery == null) {
			subquery = "";
		}

		if (voteType.equals("morph")) {
			lemma = request.getParameter("lemma");
			morphCode = request.getParameter("code");
			hasMorph = true;
		} else if (voteType.equals("sense")) {
			senseID = request.getParameter("sense_id");
			lexQuery = request.getParameter("lexquery");
			hasSense = true;
		} else {
			badRequest = true;
		}
		keeper.record("Determined what type of vote to process: "+voteType);

		DisplayPreferences prefs = new DisplayPreferences(request, response);
		Renderer renderer = new Renderer(languageCode);
		renderer.addLanguageTokenFilters(prefs);

		FormInstance formInst = new FormInstance(query, form, Integer.parseInt(which));
		formInst.setLanguageCode(languageCode);

		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("prefs", prefs);
		myModel.put("recordedVote", recordedVote);
		myModel.put("badRequest", badRequest);
		myModel.put("query", query);
		myModel.put("which", which);
		myModel.put("renderer", renderer);
		myModel.put("form", form);
		
		recordedVote = false;
		if (hasSense) {
			if (senseID != null) {
				recordedVote = VoteManager.recordSenseVote(
						formInst, lexQuery, senseID, identity);
				keeper.record("Recorded sense vote");
			}
		} else if (hasMorph) {
			if (morphCode != null) {
				recordedVote = VoteManager.recordMorphVote(
						formInst, lemma, morphCode, identity);
				keeper.record("Recorded morph vote");
			}
		}

		Map<String, Integer> senseCounts = new HashMap<String, Integer>();
		Map<String, Integer> morphCounts = new HashMap<String, Integer>();
		int totalVotes = 0;
		Set<Parse> parses = null;
		Set<Sense> senses = null;
		boolean hasCounts = false;
		
		if (hasSense) {
			Map<Sense, Integer> senseVoteCounts = VoteManager.getSenseVoteCounts(lexQuery, formInst);
			if (!senseVoteCounts.isEmpty()) {
				hasCounts = true;
			}
			for (int i : senseVoteCounts.values()) {
				totalVotes += i;
			}
			senses = senseVoteCounts.keySet();
			for (Sense s : senses) {
				String key = s.getLexiconID()+s.getLemma()+s.getSense();
				senseCounts.put(key, senseVoteCounts.get(s));
			}
			myModel.put("senses", senses);
			myModel.put("senseCounts", senseCounts);
			keeper.record("Got senses and voting info");
		} else if (hasMorph) {
			Map<Parse, Integer> morphVoteCounts = VoteManager.getMorphVoteCounts(formInst);
			if (!morphVoteCounts.isEmpty()) {
				hasCounts = true;
			}
			for (int i : morphVoteCounts.values()) {
				totalVotes += i;
			}
			parses = morphVoteCounts.keySet();
			for (Parse p : parses) {
				String key = p.toString()+p.getLemma().getAuthorityName();
				morphCounts.put(key, morphVoteCounts.get(p));
			}
			myModel.put("parses", parses);
			myModel.put("morphCounts", morphCounts);
			keeper.record("Got parses and voting info");
		}
		
		myModel.put("totalVotes", totalVotes);
		myModel.put("hasCounts", hasCounts);
		myModel.put("hasMorph", hasMorph);
		myModel.put("hasSense", hasSense);

		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("submitvote", myModel);
	}
}
