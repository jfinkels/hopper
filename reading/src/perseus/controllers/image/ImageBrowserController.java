package perseus.controllers.image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.artarch.image.SimpleImg;
import perseus.artarch.image.dao.HibernateImgDAO;
import perseus.artarch.image.dao.ImgDAO;
import perseus.util.Timekeeper;

public class ImageBrowserController implements Controller {
	
	private static Logger logger = Logger.getLogger(ImageBrowserController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.info(request.getRemoteAddr() + " " + request.getRemoteHost());
		logger.info(request.getHeader("X-Forwarded-For"));
		
		Timekeeper keeper = new Timekeeper();
		keeper.start();
		
		keeper.record("Beginning image browser processing");
		
		String[] archiveNumbers = request.getParameterValues("archiveNumber");
		String firstHitText = request.getParameter("start");
		String name = request.getParameter("name");
		String searchTerm = request.getParameter("q");
		
		keeper.record("got parameters");
		
		int firstHit = 0;
		int hitsPerPage = 20;
		int startPage = 1;
		
		if (firstHitText != null) {
		    try {
		    	startPage = Integer.parseInt(firstHitText);
		        firstHit = (startPage - 1) * hitsPerPage;
		    } catch (NumberFormatException nfe) {
		        // ignore invalid start offsets
		    }
		}
		
		List<String> archNumbers = null;
		String title = "";
		int hitCount = 0;

		if (archiveNumbers != null) {
			if (archiveNumbers.length > 0) {
				archNumbers = Arrays.asList(archiveNumbers);
		    }
		    hitCount = archNumbers.size();
		    
			title = hitCount + " Image Occurrences of ";
			if (searchTerm != null && !searchTerm.equals("")) {
				title += "\"" + searchTerm +"\"";
			}
			else {
				title += "Artifact ";
			}
		    if (name != null) { 
		      title += name;
		    }
		}
		
		keeper.record("set hitCount and title");
				
		int numPages = (int) Math.ceil( (double) (hitCount-1) / hitsPerPage);
		
		ImgDAO iDAO = new HibernateImgDAO();
		List<SimpleImg> images = new ArrayList<SimpleImg>();
		for (int i = firstHit; i < firstHit + hitsPerPage && i < archNumbers.size(); i++) {
			SimpleImg img = (SimpleImg) iDAO.getImg(archNumbers.get(i));
			img.setURLs();
			img.setIsRestricted(request);
			images.add(img);
		}
		keeper.record("got images for this page");
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("title", title);
		myModel.put("hitCount", hitCount);
		myModel.put("hitsPerPage", hitsPerPage);
		myModel.put("numPages", numPages);
		myModel.put("archNumbers", archNumbers);
		myModel.put("startPage", startPage);
		myModel.put("images", images);
		myModel.put("searchTerm", searchTerm);
		myModel.put("name", name);
		
		keeper.stop();
		logger.info(keeper.getResults());
		
		return new ModelAndView("imbrowser", "model", myModel);
	}

}
