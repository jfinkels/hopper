package perseus.controllers.document;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.TableOfContents;

public class XmlTocController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Query query = new Query(request.getParameter("doc"));

		Metadata metadata = query.getMetadata();

		String chunkScheme = request.getParameter("scheme");
		if (chunkScheme == null) {
		    chunkScheme = metadata.getChunkSchemes().getDefaultScheme();
		}

		TableOfContents toc = TableOfContents.forChunk(query.getChunk(), chunkScheme);
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("toc", toc);

		response.setContentType("text/xml;charset=UTF-8"); 
		response.setHeader("Access-Control-Allow-Origin","*");

		return new ModelAndView("xmltoc", myModel);
	}

}
