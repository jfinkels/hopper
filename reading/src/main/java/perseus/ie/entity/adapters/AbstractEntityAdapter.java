package perseus.ie.entity.adapters;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public abstract class AbstractEntityAdapter implements EntityAdapter {
    public Query getMatchingEntityQuery(String keyword, Map<String, String> options,
	    String sortMethod, String sortOrder, Session session) {
	throw new UnsupportedOperationException("This adapter doesn't "
		+ "support queries");
    }
    public Criteria getMatchingEntityCriteria(String keyword,
	    Map<String, String> options, String sortMethod, String sortOrder, Session session) {
	throw new UnsupportedOperationException("This adapter doesn't "
		+ "support criteria");
    }

    public boolean supportsMatchingEntityQuery() { return false; }
    public boolean supportsMatchingEntityCriteria() { return false; }

    public int getMatchingEntityCount(String keyword, Map<String, String> options,
	    Session session) {

	// If you're planning on using Criteria and not Query, you're set.
	// Otherwise, you'll need to override this in a subclass.
	if (supportsMatchingEntityCriteria()) {
	    Criteria criteria = getMatchingEntityCriteria(keyword, options,
		    null, null, session);
	    criteria.setProjection(Projections.rowCount());

	    return ((Number) criteria.uniqueResult()).intValue();
	} else {
	    return 0;
	}
    }
}
