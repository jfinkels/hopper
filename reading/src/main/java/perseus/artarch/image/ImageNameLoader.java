/**
 * 
 */
package perseus.artarch.image;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import perseus.artarch.image.dao.HibernateImageNameDAO;
import perseus.artarch.image.dao.ImageNameDAO;
import perseus.util.HibernateUtil;

/**
 * @author rsingh04
 *
 */
public class ImageNameLoader {
	
	private static Logger logger = Logger.getLogger(ImageNameLoader.class);
	private String dataPath;
	ImageNameDAO inDAO = new HibernateImageNameDAO();
	private int count;
	
	public ImageNameLoader(String dataPath) {
		this.dataPath = dataPath;
		count = 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			logger.error("ImageNameLoader Usage:  ImageNameLoader [dataPath(s)]");
			System.exit(0);
		}
		
		ImageNameDAO inDAO = new HibernateImageNameDAO();
		inDAO.deleteImageNames();
		
		for (String arg : args) {
			ImageNameLoader inl = new ImageNameLoader(arg);
			inl.loadImageNames();
		}

	}

	public void loadImageNames() {
		try {
			SAXBuilder sb = new SAXBuilder();
			File XMLdata = new File(dataPath);
			Document doc = sb.build(XMLdata);
			Element root = doc.getRootElement();
			List rows = root.getChildren();
			Iterator i = rows.iterator();
			while (i.hasNext()) {
				createAndWriteImageName((Element)i.next());
			}		
		} catch (JDOMException e) {
			logger.error("Problem building document "+e);
			e.printStackTrace(); 
		} catch (IOException e) {
			logger.error("Problem building document "+e);
			e.printStackTrace();
		}
	}

	private void createAndWriteImageName(Element imageName) {
		ImageName in = new ImageName();
		in.setArchiveNumber(imageName.getChildText("archive_number"));
		in.setPrimaryName(StringEscapeUtils.escapeXml(imageName.getChildText("name")));
		in.setSecondaryName(StringEscapeUtils.escapeXml(imageName.getChildText("secondary_name")));
		in.setTertiaryName(StringEscapeUtils.escapeXml(imageName.getChildText("tertiary_name")));
		in.setSchema(imageName.getChildText("image_schema"));
		
		inDAO.insertImageName(in);
		count++;
		if (count % 20 == 0) {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}
	}

}
