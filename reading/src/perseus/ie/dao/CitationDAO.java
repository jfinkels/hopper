package perseus.ie.dao;

import java.util.List;

import perseus.document.Query;
import perseus.ie.Citation;

public interface CitationDAO {
    public void beginTransaction();
    public void endTransaction();
    
    public void insert(Citation cit);
    /*
     public boolean delete(Citation cit);
     public boolean update(Citation cit);
     */
    public List<Citation> getCitations(Query source, Query destination,
	    String linkType, int firstResult, int maxResults);
    
    public List<Citation> getOutgoingCitations(Query source, String linkType,
	    int firstResult, int maxResults);
    public List<Citation> getIncomingCitations(Query destination, String linkType,
	    int firstResult, int maxResults);
    public Citation getCitationById(int id);
    public int clear(Query source, Query destination, String linkType);
    
    public void cleanup();
}
