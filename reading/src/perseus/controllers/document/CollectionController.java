package perseus.controllers.document;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import perseus.document.Corpus;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.WordCount;
import perseus.document.Corpus.CollectionData;
import perseus.document.dao.HibernateWordCountDAO;
import perseus.util.Timekeeper;

public class CollectionController implements Controller {
    private static Logger logger = Logger.getLogger(CollectionController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();

		String collectionID = request.getParameter("collection");
		if (collectionID == null) {
			response.sendRedirect("collections");
			return null;
		}
		// bit of a hack to redirect users if they try to view the DDBDP collection, since
		// we no longer host it
		if (collectionID.contains("DDBDP") || collectionID.contains("ddbdp")) {
			return new ModelAndView(new RedirectView("/hopper/help/ddbdp.jsp"));
		}
		collectionID = collectionID.replace("Collection", "collection");
		Corpus corpus = new Corpus(collectionID);
		Metadata metadata = corpus.getMetadata();
		keeper.record("Got collection id "+collectionID+" and metadata for this collection");
		
		// Word Counts
		List<WordCount> counts = new HibernateWordCountDAO().getByDocument(collectionID);
		NumberFormat commaFormatter = NumberFormat.getInstance();
		long maxWords = 0L;
		long otherTotal = 0L;
		Map<String, String> langCounts = null;
		Map<String, Integer> langBars = null;
		if (counts != null) {
			maxWords = WordCount.getLargest(counts).getWordCount();
			langCounts = new TreeMap<String, String>();
			langBars = new TreeMap<String, Integer>();
			for (WordCount count : counts) {
				if (count.getLang() == null) {
	                  otherTotal += count.getWordCount();
	                  continue;
	              }
				long words = count.getWordCount();
				int barLength = (int) (((float) words / maxWords) * 100);
				langBars.put(count.getLang().getName(), barLength);
				String wordDisplay = commaFormatter.format(words)+" word"+(words > 1 ? "s" : "");
				langCounts.put(count.getLang().getName(), wordDisplay);
			}
			if (otherTotal > 0) {
				int barLength = (int) (((float) otherTotal / maxWords) * 100);
				langBars.put("Other", barLength);
				String wordDisplay = commaFormatter.format(otherTotal)+" word"+(otherTotal > 1 ? "s" : "");
				langCounts.put("Other", wordDisplay);
			}
		}
		keeper.record("Calculated word counts for collection");
		
		List<List<Metadata>> allDocuments;
		Map<Metadata, List<Query>> hasSubDocs;
		CollectionData cd;
		
		if (collectionID.equals("Perseus:collection:Greco-Roman")) {
			cd = Corpus.classicsCollection;
		} else {
			cd = Corpus.loadCollection(collectionID);
		}
		allDocuments = cd.getAllDocuments();
		hasSubDocs = cd.getHasSubDocs();
		keeper.record("Created list of list of documents in collection grouped by author");
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("title", metadata.getTitle());
		myModel.put("language", metadata.getLanguage());
		myModel.put("documentID", metadata.getDocumentID());
		myModel.put("langCounts", langCounts);
		myModel.put("langBars", langBars);
		myModel.put("allDocuments", allDocuments);
		myModel.put("hasSubDocs", hasSubDocs);

		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("collection", "model", myModel);
	}
}
