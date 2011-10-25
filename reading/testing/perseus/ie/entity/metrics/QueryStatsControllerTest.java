/*
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
package testing.perseus.ie.entity.metrics;

import edu.unc.epidoc.transcoder.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mock.web.MockHttpServletRequest;
import perseus.document.GreekTranscoderTokenFilter;
import perseus.document.Chunk;
import perseus.document.Query;
import perseus.document.Renderer;
import perseus.document.Token;
import perseus.ie.entity.metrics.ScoredEntity;
import perseus.ie.entity.metrics.QueryStatsBrowser;
import perseus.ie.entity.metrics.QueryStatsController;
import perseus.util.DisplayPreferences;
import perseus.util.LanguageCode;

public class QueryStatsControllerTest extends TestCase {
    
    private ApplicationContext ac;

    public void setUp() throws IOException {
	ac = new FileSystemXmlApplicationContext("testing/WEB-INF/hopper-servlet.xml");
    }

    public void testTranscoder() {
	try {
	    TransCoder tc = new TransCoder("BetaCode", "BetaCode");
	    String god = "qeou\\s";
	    String display = tc.getString(god);
	    tc = new TransCoder("BetaCode", "UnicodeC");
	    display = tc.getString(display);
	    System.out.println(god + ": " + display);
	    
	} catch (Exception e) {
	    System.out.println(e);
	}
	assertFalse(2 == 2);
		 
    }
    
    public void testHandleRequest() throws ServletException, IOException {
	QueryStatsController qsc = (QueryStatsController)ac.getBean("queryStatsController");
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.addParameter("ascending", "false");
	request.addParameter("entityType", "Lemma");
	request.addParameter("property", "tf_idf");
	request.addParameter("pageSize", "30");
	request.addParameter("page", "0");
	request.addParameter("doc", "Perseus:text:1999.01.0003:card=1");
	request.addParameter("collection", "Perseus:text:1999.01.0003");
	ModelAndView mav = qsc.handleRequest((HttpServletRequest)request, (HttpServletResponse)null);
	Map m = mav.getModel();
	Map model = (Map)m.get("model");
	Query documentQuery = (Query)model.get("document_query");
	String documentCit = (String)model.get("document_cit");
	
	Integer page_count = (Integer)model.get("page_count");
	Integer entity_count = (Integer)model.get("entity_count");
	
	assertEquals(documentQuery.toString(), "Perseus:text:1999.01.0003:card=1");
	System.out.println("Document cit: " + documentCit);
	//	assertEquals(documentCit.toString(), "Aesch. Ag. 1");
	System.out.println("Page count: " + page_count);
	//	assertEquals(7, page_count.intValue());
	System.out.println("Entity count: " + entity_count);
	//assertEquals(186, entity_count.intValue());
    }

    /*    public void testGreekTranscoderTokenFilter() {

	Renderer renderer = new Renderer("greek");
	Chunk aesch_ag_1 = new Chunk(new Query("Perseus:text:1999.01.0003:card=1"));
	GreekTranscoderTokenFilter gttf = new GreekTranscoderTokenFilter("UnicodeC");	
	renderer.addTokenFilter(gttf);
	renderer.renderChunk(aesch_ag_1);
	Token token = new Token(LanguageCode.GREEK, "A)/NDRA", Token.WORD_TYPE);
	System.out.println(token);
	gttf.process(token);
	System.out.println(token);
	assertFalse(2 == 2);
    }

    public void testTranscoderAPIExample() {
	String source = "A)/NDRA MOI E)/NNEPE, MOU=SA";
	try {
	    TransCoder tc = new TransCoder("BetaCode", "UnicodeC");
	    String result = tc.getString(source);
	    System.out.println("Test Transcoder:");
	    System.out.println(result);	   
	} catch (Exception e) {
	    System.out.println(e);
	}
	assertFalse(2==2);
    }

    public void testTranscodeScoredEntities() {
	String aesch_ag_1 = "Perseus:text:1999.01.0003:card=1";
	String aesch_ag = "Perseus:text:1999.01.0003";
	String aesch = "Perseus:corpus:perseus,author,Aeschylus";

	QueryStatsController qsc = (QueryStatsController)ac.getBean("queryStatsController");
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.addParameter("ascending", "false");
	request.addParameter("entityType", "Lemma");
	request.addParameter("property", "tf_idf");
	request.addParameter("ignoreCase", "true");
	request.addParameter("pageSize", "30");
	request.addParameter("page", "0");
	request.addParameter("doc", aesch_ag_1);
	request.addParameter("collection", aesch_ag);
	QueryStatsBrowser qsb = QueryStatsController.bindQueryStatsBrowser(request);
	qsb.initScoredEntitiesList();
	List scoredEntitiesList = qsb.getScoredEntitiesList();

	DisplayPreferences prefs = new DisplayPreferences(request, null);
	List transcodedLemmas = QueryStatsController.transcodeScoredEntities(prefs, scoredEntitiesList);
	Iterator iterator = transcodedLemmas.iterator();
	System.out.println("testTranscodeScoredEntities()");
	while (iterator.hasNext()) {
	    String displayLemma = ((ScoredEntity)iterator.next()).getDisplayName();
	    System.out.println(displayLemma);
	}
	assertFalse(2 == 2);
    }
    */
    /*
    public void testBindQueryStatsBrowser() {
	String aesch_ag_1 = "Perseus:text:1999.01.0003:card=1";
	String aesch = "Perseus:corpus:perseus,author,Aeschylus";

	QueryStatsController qsc = (QueryStatsController)ac.getBean("queryStatsController");
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.addParameter("ascending", "false");
	request.addParameter("entityType", "Lemma");
	request.addParameter("property", "tf_idf");
	request.addParameter("ignoreCase", "true");
	request.addParameter("pageSize", "30");
	request.addParameter("page", "0");
	request.addParameter("doc", aesch_ag_1);
	request.addParameter("collection", aesch);
	QueryStatsBrowser qsb = QueryStatsController.bindQueryStatsBrowser(request);
	qsb.initScoredEntitiesList();
	List scoredEntities = qsb.getScoredEntitiesList();
	printScoredEntitiesList(scoredEntities);
	System.out.println(qsb.toString());
	assertTrue(2 == 3);
    }
    */
    public static void printScoredEntitiesList(List scoredEntities) {
	Iterator iterator = scoredEntities.iterator();
	while (iterator.hasNext()) {
	    ScoredEntity se = (ScoredEntity)iterator.next();
	    System.out.println(se.getEntity().getAuthorityName() + ": " +
			       se.getScore().getTfIdfScore());
	}

    }
}

