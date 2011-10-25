package perseus.controllers.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.morph.Parse;
import perseus.util.DisplayPreferences;

public class XmlMorphController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String word = request.getParameter("lookup");
		String languageCode = request.getParameter("lang");

		DisplayPreferences prefs = new DisplayPreferences(request, response);

		List<Parse> lemmaParses = Parse.getFlatParses(word, languageCode);

		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("lemmaParses", lemmaParses);
		myModel.put("prefs", prefs);
		
		response.setContentType("text/xml;charset=UTF-8"); 
		response.setHeader("Access-Control-Allow-Origin","*");
		
		return new ModelAndView("xmlmorph", myModel);
	}

}
