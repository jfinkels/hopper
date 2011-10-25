package testing.perseus.ie.entity.metrics;
/**
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */

//import perseus.ie.entity.metrics.MaximumFrequencyScorer;
import perseus.ie.entity.metrics.ScoredEntity;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jdom.output.DOMOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import perseus.document.Query;
import perseus.ie.Location;
import perseus.ie.entity.Entity;
//import perseus.ie.entity.metrics.TfIdfScorer;

public class ScoredEntityTest extends TestCase {

    public ScoredEntityTest (String name) {
	super(name);
    }
    /*
    public void testSetScoreLocation_I() {
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Location aesch_ag_1_location = new Location(aesch_ag_1, 0);
	Map scoredEntities = MaximumFrequencyScorer.getScoredEntities(aesch_ag_1);
	long start = System.currentTimeMillis();
	scoredEntities = ScoredEntity.setScoreLocation(scoredEntities, aesch_ag_1_location);
	long end = System.currentTimeMillis();
	System.out.println("testSetScoreLocation_I [ " + (end - start) + "ms ]");
	Iterator iterator = scoredEntities.keySet().iterator();
	while (iterator.hasNext()) {
	    Entity entity = (Entity)iterator.next();
	    ScoredEntity scoredEntity = (ScoredEntity)scoredEntities.get(entity);
	    Location location = scoredEntity.getLocation();
	    Query query = location.getQuery();
	    assertEquals(aesch_ag_1, query);
	}
    }

    public void testSetScoreLocation_II() {
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Query aesch_ag = new Query("Perseus:text:1999.01.0003");
	Location aesch_ag_1_location = new Location(aesch_ag_1, 0);
	Map scoredEntities = TfIdfScorer.getScoredEntities(aesch_ag_1, aesch_ag);
	long start = System.currentTimeMillis();
	scoredEntities = ScoredEntity.setScoreLocation(scoredEntities, aesch_ag_1_location, aesch_ag);
	long end = System.currentTimeMillis();
	System.out.println("testSetScoreLocation_II [ " + (end - start) + "ms ]");
	Iterator iterator = scoredEntities.keySet().iterator();
	while (iterator.hasNext()) {
	    Entity entity = (Entity)iterator.next();
	    ScoredEntity scoredEntity = (ScoredEntity)scoredEntities.get(entity);
	    Location location = scoredEntity.getLocation();
	    Query query = location.getQuery();
	    Query collection_query = scoredEntity.getCollectionQuery();
	    assertEquals(aesch_ag_1, query);
	    assertEquals(aesch_ag, collection_query);
	}
    }

    public void testToXML() {
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Location aesch_ag_1_location = new Location(aesch_ag_1, 0);
	Map scoredEntities = MaximumFrequencyScorer.getScoredEntities(aesch_ag_1);
	Iterator iterator = scoredEntities.keySet().iterator();	
	ScoredEntity se = (ScoredEntity)scoredEntities.get(iterator.next());
	String rootNodeName = "";
	try {
	    Node seXML = new DOMOutputter().output(new org.jdom.Document(se.toXML()));
	    rootNodeName = seXML.getFirstChild().getNodeName();
	    OutputFormat format = new OutputFormat();
	    XMLSerializer output = new XMLSerializer(System.out, format);
	    output.serialize((Document)seXML);
	    System.out.println("");
	} catch (IOException ioe) {
	    System.err.println(ioe);
	} catch (Exception e) {
	    System.err.println(e);
	}
	assertEquals("ScoredEntity", rootNodeName);
    }
    */
}
