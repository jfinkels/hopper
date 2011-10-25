/**
 * 
 */
package perseus.controllers.image;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.artarch.Artifact;
import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.artarch.image.ImageName;
import perseus.artarch.image.Img;
import perseus.artarch.image.SimpleImg;
import perseus.artarch.image.dao.HibernateImageNameDAO;
import perseus.artarch.image.dao.HibernateImgDAO;
import perseus.artarch.image.dao.ImageNameDAO;
import perseus.artarch.image.dao.ImgDAO;
import perseus.util.Timekeeper;

/**
 * @author rsingh04
 *
 */
public class ImageController implements Controller {
	private static Logger logger = Logger.getLogger(ImageController.class);

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Timekeeper keeper = new Timekeeper();
		keeper.start();

		String archiveNumber = request.getParameter("img");
		if (archiveNumber == null || archiveNumber.equals("")) {
			archiveNumber = request.getParameter("lookup");
		}
		if (!archiveNumber.startsWith("Perseus:image:")) {
			archiveNumber = "Perseus:image:" + archiveNumber;
		}

		ImgDAO iDAO = new HibernateImgDAO();
		Img img = iDAO.getImg(archiveNumber);
		
		keeper.record("Searched for image using archive number "+archiveNumber);

		String headerText = "";
		String caption = "";
		String artifactLink = "";

		if (img != null) {
			((SimpleImg) img).setURLs();
			((SimpleImg) img).setIsRestricted(request);
			keeper.record("Set image URLS and restrictions");

			ImageNameDAO inDAO = new HibernateImageNameDAO();
			ImageName imageName = inDAO.getImageName(archiveNumber);
			//header text
			if (imageName != null) {
				headerText = "Image of "+imageName.getPrimaryName();
			} else {
				headerText = "Image from <i>"+img.getCaption()+"</i>";
			}
			keeper.record("Set header text");
			
			//caption
			String sourceID = img.getSourceId();
			if (sourceID != null && !sourceID.equals("")) {
				caption = "Image from <a href=\"/hopper/text?doc="+sourceID+"\"><i>"+img.getCaption()+"</i></a>";
			} else {
				caption = img.getCaption();
			}
			keeper.record("Set caption");
			
			// find out if there is an artifact associated with this image and create link
			ArtifactDAO aDAO = new HibernateArtifactDAO();
			Artifact artifact = aDAO.findArtifact(archiveNumber);
			if (artifact != null) {
				artifactLink = "<a href=\"/hopper/artifact?name="+artifact.getName()+
				"&object="+artifact.getType()+"\">"+imageName.getPrimaryName()+"</a>";
				keeper.record("Created artifact link");
			} else  if (imageName != null) {
				artifactLink = headerText;
			}
		}

		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("img", img);
		myModel.put("headerText", headerText);
		myModel.put("caption", caption);
		myModel.put("artifactLink", artifactLink);
		
		keeper.stop();
		logger.info(keeper.getResults());

		return new ModelAndView("image", "model", myModel);
	}

}
