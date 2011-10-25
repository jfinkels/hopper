package perseus.ie.entity.adapters;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

public interface EntityAdapter {
    public Query getMatchingEntityQuery(String keywords,
	    Map<String, String> options,
	    String sortMethod, String sortOrder, Session session);
    public abstract Criteria getMatchingEntityCriteria(String keyword,
	    Map<String, String> options, String sortMethod, String sortOrder,
	    Session session);

    public boolean supportsMatchingEntityQuery();
    public boolean supportsMatchingEntityCriteria();

    public int getMatchingEntityCount(
	    String keyword, Map<String, String> options, Session session);
}
