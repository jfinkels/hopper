package perseus.artarch;

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

import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.util.Config;

public class ArtifactKeywordLoader {
	
	private static Logger logger = Logger.getLogger(ArtifactKeywordLoader.class);

	private String filename = "AAKeywords.xml";
	ArtifactDAO aDAO = new HibernateArtifactDAO();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ArtifactKeywordLoader().loadKeywords();
	}

	private void loadKeywords() {
		logger.info("Deleting keywords from database...");
		aDAO.deleteKeywords();
		
		String dataPath = Config.getDataPath() + "/" + filename;
		
		File file = new File(dataPath);
		
		try {
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(file);
			Element root = doc.getRootElement();
			List rows = root.getChildren();
			Iterator i = rows.iterator();
			int count = 0;
			while (i.hasNext()) {
				writeArtifactKeywords((Element)i.next());
				count++;
				if (count % 1000 == 0) {
					logger.info("saved "+count+" keywords");
				}
			}
		} catch (IOException e) {
			logger.error("Failed to read file: " + file.getAbsolutePath() + ". " + e);
		} catch (JDOMException e) {
			logger.error("Error building XML file "+e);
		}

	}

	private void writeArtifactKeywords(Element artKeyword) {
		String artifactName = StringEscapeUtils.escapeXml(artKeyword.getChildText("artifactName"));
		String artifactType = artKeyword.getChildText("artifactType");
		String keyclass = artKeyword.getChildText("keyclass");
		String keyword = artKeyword.getChildText("keyword");

		Artifact art = aDAO.findArtifact(artifactName, artifactType);

		if (art != null) {
			ArtifactKeyword ak = new ArtifactKeyword(art, artifactType, keyclass, keyword);
			aDAO.saveArtifactKeyword(ak);
		}
	}

}
