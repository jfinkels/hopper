/*
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package testing.perseus.ie.entity.metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jdom.output.DOMOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import perseus.document.Query;
import perseus.ie.entity.EntityType;
import perseus.ie.entity.metrics.ScoredEntity;
import perseus.ie.entity.metrics.QueryStatsBrowser;


public class QueryStatsBrowserTest extends TestCase {
    
    public QueryStatsBrowserTest (String name) {
	super(name);
    }

    public void testSomething() {
	assertTrue(2 == 2);
    }

    public void testInitScoredEntitiesList() {
	boolean ascending = false;
	EntityType entityType = EntityType.LEMMA;
	String propertyName = "tf_idf";
	Map pagedListHolderParams = new HashMap();
	pagedListHolderParams.put("page", new Integer(0));
	pagedListHolderParams.put("pageSize", new Integer(30));
	
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Query aesch_ag = new Query("Perseus:text:1999.01.0003");
	
	//	Query plin_nat_preface_1 = new Query("Perseus:text:1999.02.0138:book=preface:chapter=1");
	//Query plin = new Query("Perseus:text:1999.02.0138");

	QueryStatsBrowser qsb = new QueryStatsBrowser(ascending, entityType,
						      propertyName, pagedListHolderParams,
						      aesch_ag_1, aesch_ag);
	qsb.initScoredEntitiesList();
	List seList = new ArrayList();
	seList = qsb.getScoredEntitiesList();
	Iterator iterator = seList.iterator();
	System.out.println("===========================================");
	int i=0;
	while (iterator.hasNext()) {
	    ScoredEntity match_se = (ScoredEntity)iterator.next();
	    System.out.println("(" + i + ") " + match_se.getEntity().getAuthorityName() + ": " + match_se.getScore().getTfIdfScore());
	    i++;
	}
	System.out.println("===========================================");
	assertTrue(seList.size() == 30);
    }

    public void testInitEntityCount() {
	boolean ascending = false;
	EntityType entityType = EntityType.LEMMA;
	String propertyName = "tf_idf";
	Map pagedListHolderParams = new HashMap();
	pagedListHolderParams.put("page", new Integer(0));
	pagedListHolderParams.put("pageSize", new Integer(30));

	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Query aesch_ag = new Query("Perseus:text:1999.01.0003");

	QueryStatsBrowser qsb = new QueryStatsBrowser(ascending, entityType,
						      propertyName, pagedListHolderParams,
						      aesch_ag_1, aesch_ag);
	qsb.initEntityCount();
	int entity_count = qsb.getEntityCount();
	assertEquals(190, entity_count);
    }

    public void testToXML() {
	boolean ascending = false;
	EntityType entityType = EntityType.LEMMA;
	String propertyName = "tf_idf";
	Map pagedListHolderParams = new HashMap();
	pagedListHolderParams.put("page", new Integer(0));
	pagedListHolderParams.put("pageSize", new Integer(30));

	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Query aesch_ag = new Query("Perseus:text:1999.01.0003");

	QueryStatsBrowser qsb = new QueryStatsBrowser(ascending, entityType,
						      propertyName, pagedListHolderParams,
						      aesch_ag_1, aesch_ag);
	qsb.initEntityCount();
	String rootNodeName = "";
	try {
	    Node qsbXML = new DOMOutputter().output(new org.jdom.Document(qsb.toXML()));
	    rootNodeName = qsbXML.getFirstChild().getNodeName();
	    OutputFormat format = new OutputFormat();
	    XMLSerializer output = new XMLSerializer(System.out, format);
	    output.serialize((Document)qsbXML);
	    System.out.println("");
	} catch (IOException ioe) {
	    System.err.println(ioe);
	} catch (Exception e) {
	    System.err.println(e);
	}
	assertEquals("QueryStats", rootNodeName);
    }

}
