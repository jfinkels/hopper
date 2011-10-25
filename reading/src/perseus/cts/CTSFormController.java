/*
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package perseus.cts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

public class CTSFormController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	
	Map myModel = new HashMap();
	return new ModelAndView("cts_form",  "model", myModel);
    }

}
