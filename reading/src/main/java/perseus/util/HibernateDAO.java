package perseus.util;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;

/**
 * Subclass of DAO containing Hibernate-based implementations of all its
 * methods. HibernateZZZDAO classes can extend this class and implement
 * the ZZZDAO class to get lots of methods for free.
 */
public class HibernateDAO<T> implements DAO<T> {
    private static int batchSize = 20;
    static {
	try {
	    batchSize = Integer.parseInt(
				HibernateUtil.getProperty(Environment.STATEMENT_BATCH_SIZE));
	} catch (NumberFormatException nfe) {
	    // ok, whatever
	}
    }
		
    private int updateCount = 0;
	
    public void setBatchSize(int newBatchSize) {
	batchSize = newBatchSize;
    }

    public int getBatchSize() {
	return batchSize;
    }
	
    public int getUpdateCount() {
	return updateCount;
    }
	
    protected static Session getSession() {
	return HibernateUtil.getSession();
    }

    public void beginTransaction() {
	HibernateUtil.beginTransaction();
	updateCount = 0;
    }

    public void endTransaction() {
	HibernateUtil.commitTransaction();
	HibernateUtil.getSession().clear();
    }
    
    public void closeSession() {
	HibernateUtil.closeSession();
    }
    
    public void save(T obj) {
	getSession().save(obj);
	registerUpdate();
    }

    public void update(T obj) {
	getSession().update(obj);
	registerUpdate();
    }
    
    public void saveOrUpdate(T obj) {
	getSession().saveOrUpdate(obj);
	registerUpdate();
    }

    public void delete(T obj) {
	getSession().delete(obj);
    }

    private void registerUpdate() {
	updateCount++;
		
	if (updateCount % batchSize == 0) {
	    HibernateUtil.getSession().flush();
	    HibernateUtil.getSession().clear();
	}
    }
	
    private Criteria createExampleCriteria(T exemplar) {
	Example example = Example.create(exemplar);
	return getSession().createCriteria(exemplar.getClass()).add(example);
    }
	
    public List<T> findByExample(T exemplar) {
	return createExampleCriteria(exemplar).list();
    }
	
    public long countByExample(T exemplar) {
	return (Long) createExampleCriteria(exemplar)
	    .setProjection(Projections.rowCount())
	    .uniqueResult();
    }
}
