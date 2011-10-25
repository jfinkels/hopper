/**
 * 
 */
package perseus.ie.entity;

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

import perseus.util.Config;

/**
 * @author rsingh04
 * Replaces the atlas-convert ant task, so we don't have to rely on the atlas table.  
 * Reads place data from XML files and stores them in the db.
 *
 */
public class PlaceLoader {
	
	private static Logger logger = Logger.getLogger(PlaceLoader.class);
	
	static final Pattern TGN_MATCH_PATTERN = Pattern.compile("TGN\\d+\\.xml");
	HibernateEntityManager manager = new HibernateEntityManager();
	
	int updateCount = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlaceLoader pl = new PlaceLoader();
		pl.loadPlaces();
	}

	private void loadPlaces() {
		logger.info("deleting exisiting places...");
		manager.deleteAllPlaces();

		manager.beginWrite();
		File dataPath = new File(Config.getDataPath());
		File[] dataFiles = dataPath.listFiles();
		Matcher matcher;
		for (File f : dataFiles) {
			matcher = TGN_MATCH_PATTERN.matcher(f.getName());
			if (matcher.find()) {
				createAndWritePlace(f);
			}
		}
		manager.endWrite();
	}

	private void createAndWritePlace(File f) {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(f);
			Element root = doc.getRootElement();
			List rows = root.getChildren();
			Iterator i = rows.iterator();
			while (i.hasNext()) {
				Place place = createPlace((Element)i.next());
				manager.registerEntity(place);
				updateCount++;
				if (updateCount % 20 == 0) {
					manager.clearCache();
				}
				if (updateCount % 5000 == 0) {
					logger.info(updateCount + " places written");
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Place createPlace(Element place) {
		Place p = new Place();
		String name = place.getChildText("name");
		String cname = place.getChildText("cname");
		String state = place.getChildText("state");
		String nation = place.getChildText("nation");
		p.setAuthorityName(place.getChildText("id"));
		String displayName = cname != null ? cname : name;
		if (state != null) {
			displayName = displayName + ", " + state;
		}
		if (nation != null) {
			displayName = displayName + ", " + nation;
		}
		p.setDisplayName(displayName);
		p.setLongitude(Double.parseDouble(place.getChildText("lon")));
		p.setLatitude(Double.parseDouble(place.getChildText("lat")));
		p.setSiteName(cname != null ? cname : name);
		p.setState(state);
		p.setNation(nation);
		p.setSortableString(p.getDisplayName());
		
		return p;
	}

}
