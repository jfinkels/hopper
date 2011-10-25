/**
 * @GPL@
 * Last Modified: @TIME@
 *
 * EntityRegistry captures the functionality used to navigate a collection of a given Perseus Entity
 * Refer to:  http://devwiki.perseus.tufts.edu/wiki/Documentation:PerseusServices
 * Entity Registry is an interface for a generic Transfer Object Assembler.
 * Refer to: http://java.sun.com/blueprints/corej2eepatterns/Patterns/TransferObjectAssembler.html
 **/
package perseus.ie.entity.service;

import java.util.HashMap;
import java.util.List;
import perseus.ie.entity.Entity;
import perseus.util.DAOType;

public interface EntityRegistry {

    //Used for pagination
    public int getOffset();
    public void setOffset(int offset);
    public int getMaxResults();
    public void setMaxResults(int maxResults);

    //Used to determine the data source
    public DAOType getDaoType();
    public void setDaoType(DAOType daoType);

    //Used to query the database
    public Entity getQueryObject();
    public void setQueryObject(Entity entity);

    //Used to refine the query
    public List getEntities();
    public void setEntities(List entities);
    public void appendEntity(Entity entity);
    public void initEntities();
    public void convertEntities();

    //Think of these as Metadata fields for the given entity.  CRUD
    public void initExpressionTypes();
    public List getExpressionTypes();
    public void setExpressionTypes(List expressionTypes);
    public void clearExpressionTypes();

    //Think of these as the values the Metadata fields can take on.
    public void initExpressionValues();
    public HashMap getExpressionValues();
    public void setExpressionValues(HashMap expressionValues);    
    public void clearExpressionValues();
    
    public HashMap getEntityOccurrences();
    public void initEntityOccurrences();
}

