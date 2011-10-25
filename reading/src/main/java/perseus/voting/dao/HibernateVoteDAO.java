package perseus.voting.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.util.HibernateDAO;
import perseus.voting.Vote;

public class HibernateVoteDAO extends HibernateDAO<Vote> implements VoteDAO {
    public void deleteByDocument(String documentID) {
        getSession().createQuery(
                "delete from Vote where occurrence_id in " +
                "(select id from EntityOccurrence where document_id = :docID)")
                .setParameter("docID", documentID)
                .executeUpdate();
    }

    public void deleteByOccurrence(EntityOccurrence occurrence) {
        getSession().createQuery(
                "delete from Vote where occurrence_id = :occID")
                .setParameter("occID", occurrence.getId())
                .executeUpdate();
    }

    public void deleteBySelection(Entity selection) {
        getSession().createQuery(
        "delete from Vote where entity_id = :entID")
        .setParameter("entID", selection.getId())
        .executeUpdate();
    }

    public List<Vote> getByOccurrence(EntityOccurrence occurrence) {
        return getSession().createQuery(
                "from Vote vote where occurrence = :occ")
                .setParameter("occ", occurrence).list();
    }

    public List<Vote> getBySelection(Entity selection) {
        return getSession().createQuery(
                "from Vote vote where selection = :sel")
                .setParameter("sel", selection).list();
    }

    public Map<Entity,Long> getCounts(EntityOccurrence occurrence) {
        List results = getSession().createQuery(
                "select v.selection, count(*) " +
                "from Vote v where v.occurrence = :occ group by v.selection")
                .setParameter("occ", occurrence).list();
        
        Map<Entity,Long> counts = new HashMap<Entity,Long>();

        for (Object result : results) {
            Object[] resultArr = (Object[]) result;
            Entity entity = (resultArr[0] != null ?
        	    (Entity) resultArr[0] :
        		Vote.NONE_OF_THE_ABOVE);
            long count = (Long) resultArr[1];
            
            counts.put(entity, count);
        }
        
        return counts;
    }
}
