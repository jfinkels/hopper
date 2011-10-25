package perseus.voting.dao;

import java.util.List;
import java.util.Map;

import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.util.DAO;
import perseus.voting.Vote;

public interface VoteDAO extends DAO<Vote> {
    public List<Vote> getByOccurrence(EntityOccurrence occurrence);
    public List<Vote> getBySelection(Entity selection);
    public Map<Entity,Long> getCounts(EntityOccurrence occurrence);
    
    public void deleteByDocument(String documentID);
    public void deleteByOccurrence(EntityOccurrence occurrence);
    public void deleteBySelection(Entity selection);
}
