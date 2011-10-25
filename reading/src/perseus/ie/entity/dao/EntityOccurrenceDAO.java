/**
@GPL@
Last Modified: @TIME@
*/
package perseus.ie.entity.dao;

import java.util.List;

import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
/**
 * Interface that all EntityOccurrenceDAOs must support.  
  This DAO is based upon the sample code found in Sun's article on the DataAccessObject pattern.  http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html
  * References to patterns are from "Patterns of Enterprise Appliation Architecture", Martin Fowler.
 */
public interface EntityOccurrenceDAO {

    // Returns -1 if the insert failed, returns the id of the entityOccurrence otherwise
    public int insertEntityOccurrence(EntityOccurrence entityOccurrence);
    public boolean deleteEntityOccurrence(EntityOccurrence entityOccurrence);
    public boolean deleteEntityOccurrence(Entity entity);
    public List<EntityOccurrence> findEntityOccurrence(Entity entity, perseus.document.Query query);
    /*
     *   @param offset - which set of EntityOccurrences to grab
     *   @param eoCount - how many EntityOccurrences to grab
     */
    public List<EntityOccurrence> findEntityOccurrence(Entity entity, perseus.document.Query query, int offset, int maxResults);
    // Return true if the update successful
    public boolean updateEntityOccurrence(EntityOccurrence entityOccurrence);
}
