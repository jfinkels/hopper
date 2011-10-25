package perseus.controllers.document;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.StyleTransformer;

public class PublicationsController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String sort = request.getParameter("sort");
		// we accept "author" or "date"
		if (sort == null) {
			sort = "author";
		}
		
		boolean dateSort = false;
		if (sort.equals("date")) {
			dateSort = true;
		}

		Map<String, String> styleParameters = new HashMap<String, String>();
		styleParameters.put("sort_by", sort);

		InputStream xmlStream = request.getSession().getServletContext()
		.getResourceAsStream("/WEB-INF/classes/MODSBibliography.xml");
		String output = StyleTransformer.transform(xmlStream, "mods2html.xsl", styleParameters);
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("dateSort", dateSort);
		myModel.put("output", output);

		return new ModelAndView("index/about/publications", "model", myModel);
	}

}
