package perseus.util;

import java.util.List;

/**
 * An interface for DAO classes that specifies some commonly-used methods.
 */
public interface DAO<T> {
    public void beginTransaction();
    public void endTransaction();
    public void closeSession();

    public void save(T obj);
    public void update(T obj);
    public void saveOrUpdate(T obj);

    public void delete(T obj);

	public List<T> findByExample(T example);
	public long countByExample(T exemplar);
}
