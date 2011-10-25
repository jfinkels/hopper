package perseus.controllers.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Abbreviation;

public class AbbrevHelpController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest arg0,
	    HttpServletResponse arg1) throws Exception {
	
	return new ModelAndView("abbrevhelp", "abbrevs", Abbreviation.getAll());
    }

}
