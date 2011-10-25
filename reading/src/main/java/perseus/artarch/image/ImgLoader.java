/**
 * @GPL@
 * Last Modified: @TIME@
 *
 * ImgLoader is a class designed to load data from XML files and put it
 *  into the ArtifactImg table of archdb.
 **/
package perseus.artarch.image;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import perseus.artarch.image.dao.HibernateImgDAO;
import perseus.artarch.image.dao.ImgDAO;
import perseus.util.Config;
import perseus.util.HibernateUtil;

public class ImgLoader {

	private static Logger logger = Logger.getLogger(ImgLoader.class);
	ImgDAO iDAO = new HibernateImgDAO();
	int count = 0;
	
	static final Pattern IMAGE_MATCH_PATTERN = Pattern.compile("((ArtifactImg)|(Images)).+\\.xml");

	public static void main (String[] args) {
		if (args.length != 0) {
			logger.info("ImgLoader Usage: ImgLoader");
			System.exit(0);
		}
		
		ImgLoader il = new ImgLoader();
		il.loadImages();
	}

	private void loadImages() {
		logger.info("deleting existing images...");
		iDAO.deleteAllImgs();
		
		File dataPath = new File(Config.getDataPath());
		File[] dataFiles = dataPath.listFiles();
		Matcher matcher;
		for (File f : dataFiles) {
			matcher = IMAGE_MATCH_PATTERN.matcher(f.getName());
			if (matcher.find()) {
				logger.info("Loading "+f.getAbsolutePath());
				try {
					SAXBuilder sb = new SAXBuilder();
					Document doc = sb.build(f);
					Element root = doc.getRootElement();
					List rows = root.getChildren();
					Iterator i = rows.iterator();
					while (i.hasNext()) {
						createAndWriteImg((Element)i.next());
					}
				} catch (IOException e) {
					logger.error("Failed to read file: " + f.getAbsolutePath() + ". " + e);
				} catch (JDOMException e) {
					logger.error("Error building XML file "+e);
				}
				
				try {
					//because we're getting a 403 error for one of the .ent files in the DTD
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}

	private void createAndWriteImg(Element imgData) {
		Img img = new SimpleImg();
		img.setArchiveNumber(imgData.getChildText("archive_number"));
		img.setCaption(imgData.getChildText("caption"));
		img.setCredits(imgData.getChildText("credits"));
		img.setDate(imgData.getChildText("date"));
		img.setSeries(imgData.getChildText("series"));
		img.setSeq(imgData.getChildText("seq"));
		img.setEnteredBy(imgData.getChildText("entered_by"));
		img.setLon(imgData.getChildText("lon"));
		img.setLat(imgData.getChildText("lat"));
		img.setStatus(Integer.parseInt(imgData.getChildText("status")));
		img.setCanonical(imgData.getChildText("canonical"));
		img.setSourceId(imgData.getChildText("source_id"));
		img.setFormat(imgData.getChildText("format"));
		img.setQtName(imgData.getChildText("qt_name"));
		img.setQtMovieName(imgData.getChildText("qt_movieName"));
		img.setQtController(imgData.getChildText("qt_controller"));
		img.setQtAutoplay(imgData.getChildText("qt_autoplay"));
		img.setVrBcolor(imgData.getChildText("vr_bcolor"));
		img.setPlaybackWidth(imgData.getChildText("playback_width"));
		img.setPlaybackHeight(imgData.getChildText("playback_height"));
		
		int result = iDAO.insertImg(img);
		if (result < 0) {
			logger.error("Could not insert into database: " + img);
		}
		count++;
		if (count % 20 == 0) {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}
	}
}
