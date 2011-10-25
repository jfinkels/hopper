package perseus.util;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

/**
 * A class designed to be used for batch inserts/updates via Hibernate.
 * It uses a StatelessSession for its operations, with the result that it
 * keeps memory usage to a minimum (since it does no caching).
 * 
 * @see HibernateUtil
 */

public class BatchInserter {
    
    private StatelessSession session;
    private Transaction tx = null;
    
    public BatchInserter() {
	session = HibernateUtil.getStatelessSession();
    }
    
    public StatelessSession getSession() {
	return session;
    }
    
    public void beginTransaction() {
	if (tx != null && tx.isActive()) {
	    throw new IllegalStateException("Transaction already active");
	}
	tx = session.beginTransaction();
    }
    
    public void endTransaction() {
	if (tx == null || !tx.isActive()) {
	    throw new IllegalStateException("No transaction active");
	}
	
	tx.commit();
    }
    
    public void closeSession() {
	session.close();
    }
    
    public void insert(Object obj) {
	session.insert(obj);
    }
    
    public void update(Object obj) {
	session.update(obj);
    }
    
    public void refresh(Object obj) {
	session.refresh(obj);
    }
    
    public void delete(Object obj) {
	session.delete(obj);
    }
}
