package perseus.controllers.search;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.language.Language;
import perseus.util.DisplayPreferences;

public class StopWordsController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String,Object> myModel = new HashMap<String,Object>();
		myModel.put("languages", Language.getAll());
		myModel.put("prefs", new DisplayPreferences(request, response));

		return new ModelAndView("stopwords", myModel);
	}

}
