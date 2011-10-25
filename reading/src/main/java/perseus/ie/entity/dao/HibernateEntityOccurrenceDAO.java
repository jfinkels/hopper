/*
 * @GPL@
 * Last Modified: @TIME@
 *
 * HibernateEntityOccurrenceDAO implementation of the
 * EntityOccurrenceDAO interface.  This class can contain all
 * Hibernate specific code and SQL statements.
 * The client is thus shielded from knowing 
 * these implementation details.
 *
 * This DAO is based upon the sample code found in Sun's article on the DataAccessObject pattern.  http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html
 */
package perseus.ie.entity.dao;

import java.util.List;

import org.apache.log4j.Logger;

import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.entity.HibernateEntityManager;
import perseus.util.HibernateDAO;

public class HibernateEntityOccurrenceDAO extends HibernateDAO<EntityOccurrence> implements EntityOccurrenceDAO {
	private static final Logger logger = Logger.getLogger(HibernateEntityOccurrenceDAO.class);

	private HibernateEntityManager hem = new HibernateEntityManager();

	public HibernateEntityOccurrenceDAO() {}

	public int insertEntityOccurrence(EntityOccurrence entityOccurrence) {
		int result = -1;
		try {
			hem.addOccurrence(entityOccurrence);
		} catch (Exception e) {
			logger.error("insertEntityOccurrence: " + e);
			logger.error(entityOccurrence);
			hem.clearCache();
		}
		Integer id = entityOccurrence.getId();
		if (id == null) {
			result = -1;
		} else {
			result = id.intValue();
		}
		return result;
	}

	public boolean deleteEntityOccurrence(Entity entity) {
		int result = getSession().createQuery("delete from EntityOccurrence where entity = :e").setParameter("e", entity).executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}


	public List<EntityOccurrence> findEntityOccurrence(Entity entity, perseus.document.Query query) {
		return hem.getOccurrences(entity, query);
	}

	public List<EntityOccurrence> findEntityOccurrence(Entity entity,
			perseus.document.Query query, int offset, int maxResults) {
		hem.setFirstResult(offset);
		hem.setMaxResults(maxResults);
		return hem.getOccurrences(entity, query);
	}

	public boolean updateEntityOccurrence(EntityOccurrence entityOccurrence) {
		return false;
	}

	public boolean deleteEntityOccurrence(EntityOccurrence entityOccurrence) {
		// TODO Auto-generated method stub
		return false;
	}
}
