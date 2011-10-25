package perseus.controllers.document;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.TokenList;
import perseus.util.DisplayPreferences;

public class XmlChunkController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		DisplayPreferences prefs = new DisplayPreferences(request, response);

		Query query = new Query(request.getParameter("doc"));

		Chunk chunk = query.getChunk();
		Metadata metadata = chunk.getMetadata();

		TokenList tokens = TokenList.getTokensFromXML(chunk);

		Renderer renderer = new Renderer(metadata.getLanguage());
		renderer.addLanguageTokenFilters(prefs);
		
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("tokens", tokens);
		myModel.put("renderer", renderer);
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin","*");
		
		return new ModelAndView("xmlchunk", myModel);
	}

}
