/*
 * @GPL@
 * Last Modified: @TIME@
 * 
 * HibernateImgDAO implementation of the ImgDAO interface.
 * This class can contain all Hibernate specific code and SQL
 * statements.  The client is thus shielded from knowing these
 * implementation details.
 *
 * This DAO is based upon the sample code found in Sun's article on the DataAccessObject pattern.  http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html
 */
package perseus.artarch.image.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Example;
import org.hibernate.exception.SQLGrammarException;

import perseus.artarch.image.Img;
import perseus.util.HibernateDAO;

public class HibernateImgDAO extends HibernateDAO<Img> implements ImgDAO {
	
	private static Logger logger = Logger.getLogger(HibernateImgDAO.class);

    public int insertImg(Img image) {
	int result = -1;
	if (image.getArchiveNumber() == null) {
	    throw new IllegalArgumentException("No archive number for " + image);
	}
	try {
	    getSession().save(image);
	} catch (Exception e) {
	    logger.error("Error inserting image: " + e);
	    logger.error(image);
	    getSession().flush();
	}
	Integer id = image.getId();
	if  (id == null) {
	    result = -1;
	} else {
	    result = id.intValue();
	}
	return result;
    }

    /**
     * Delete a persisted Image
     */
    public boolean deleteImg(Img image) {
	boolean result = true;
	if (image.getArchiveNumber() == null) {
	    logger.warn("deleteImg: NULL archiveNumber");
	    result = false;
	}
	try {
	    getSession().delete(image);
	} catch (Exception e) {
	    logger.error("Error deleting iamge: " + e);
	    getSession().flush();
	}
	return result;
    }

    public List findImg(Img image) {
	Example example = Example.create(image).ignoreCase();
	List results = null;
	try {
	    results = getSession().createCriteria(image.getClass())
		.add(example).list();
	} catch (SQLGrammarException sge) {
	    logger.error("Error findng image: " + sge);
	    getSession().flush();
	} catch (Exception e) {
	    logger.error("Error finding image: " + e);
	    getSession().flush();
	}
	return results;
    }


    public List findImg(Img image, int offset, int maxResults) {
	List results = null;
	try {
	    results = getSession().createCriteria(image.getClass())
		.setFirstResult(offset).setMaxResults(maxResults).list();
	} catch (Exception e) {
	    logger.error("Error finding image: " + e);
	    getSession().flush();
	}
	return results;
    }

    public int getTotalHits(Img image) {
	Example example = Example.create(image).ignoreCase();
	List results = null;
	try {
	    results = getSession().createCriteria(image.getClass())
		.add(example).list();
	} catch (Exception e) {
	    logger.error("Error getting total hits: " + e);
	    getSession().flush();
	}
	return results.size();
    }
    
    public Img getImg(String archiveNumber) {
    	List<Img> imgs = getSession().createQuery("from Img i where i.archiveNumber = :archiveNumber")
    		.setString("archiveNumber", archiveNumber).list();
    	if (imgs.size() != 0) {
    		return imgs.get(0);
    	}
    	return null;
    }

    public boolean updateImg(Img image) {
	boolean result = true;
	if (image.getArchiveNumber() == null) {
	    result = false;
	    throw new IllegalArgumentException("No archive number for " + image);
	}
	try {
	    getSession().update(image);
	} catch (Exception e) {
	    logger.error("Error updating image: " + e);
	    getSession().flush();
	}
	return result;
    }

	public String getCaption(String archiveNumber) {
		List captions = getSession().createQuery("select caption from Img i where i.archiveNumber = :archiveNumber")
			.setString("archiveNumber", archiveNumber).list();
		String caption = null;
		if (captions.size() != 0) {
			caption = (String) captions.get(0);
		}
		return caption;
	}

	public boolean deleteAllImgs() {
		int result = 0;
		try {
		getSession().createQuery("delete from Img").executeUpdate();
		} catch(Exception e) {
			logger.error("Error deleting imgs" +e);
		}
		if (result > 0) 
			return true;
		return false;
	}  
}
