/**
 * 
 */
package perseus.artarch.image.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import perseus.artarch.image.ImageName;
import perseus.util.HibernateDAO;

/**
 * @author rsingh04
 *
 */
public class HibernateImageNameDAO extends HibernateDAO<ImageName> implements ImageNameDAO {
	
	private static Logger logger = Logger.getLogger(HibernateImageNameDAO.class);

	public List getImagesByName(String name) {
		List results = new ArrayList();
		try {
			results = getSession().createQuery("select distinct i.archiveNumber from ImageName i where i.primaryName = :name or " +
					"i.secondaryName = :name or i.tertiaryName = :name").setString("name", name).list();
		} catch (HibernateException e) {
			logger.error("Error getting result list");
			e.printStackTrace();
		}
		return results;
	}

	public int insertImageName(ImageName in) {
		try {
			getSession().save(in);
		} catch (HibernateException e) {
			logger.error("Problem inserting ImageName");
			e.printStackTrace();
		}
		return 0;
	}

    public ImageName getImageName(String archiveNumber) {
    	List<ImageName> imageNames = getSession().createQuery("from ImageName i where i.archiveNumber = :archiveNumber")
    		.setString("archiveNumber", archiveNumber).list();
    	if (imageNames.size() != 0) {
    		return imageNames.get(0);
    	}
    	return null;
    }

	public boolean deleteImageNames() {
		
		int deleted = getSession().createQuery("delete from ImageName").executeUpdate();
		if (deleted > 0) {
			return true;
		}
		return false;
	}
}
