package perseus.controllers.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import perseus.util.DisplayPreferences;

/**
 * For now, there isn't truly a view associated with this controller.  This class merely
 * updates the user's display preferences and then redirects them back to whatever URL they were
 * viewing.
 * This is done so we can avoid having parameters about display prefs in the URL
 * @author rsingh04
 *
 */
public class DisplayPreferencesController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DisplayPreferences prefs = new DisplayPreferences(request, response);
		prefs.updatePreferences();
		String url = request.getParameter("url");
		//this should never be the case, but just in case
		if (url == null || url.equals("")) {
			url = "/hopper";
		}

		return new ModelAndView(new RedirectView(url));
	}

}
