package testing.perseus.ie.entity.metrics;
/**
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import perseus.document.Chunk;
import perseus.document.Query;
import perseus.ie.Location;
import perseus.ie.entity.Entity;
import perseus.ie.entity.metrics.Score;
import perseus.ie.entity.metrics.ScoredEntity;
import perseus.ie.entity.metrics.ScoredEntityDAO;
import perseus.ie.entity.metrics.SQLScoredEntityDAO;
import perseus.morph.HibernateLemmaDAO;
import perseus.morph.Lemma;
import perseus.morph.LemmaDAO;

public class SQLScoredEntityDAOTest extends TestCase {

    private ScoredEntityDAO seDAO;

    public SQLScoredEntityDAOTest (String name) {
	super(name);
    }

    {
	seDAO = (ScoredEntityDAO)new SQLScoredEntityDAO();
    }

    /*
    public void testCreateMax() {
	boolean testPassed = true;
	ScoredEntity se = getUnpersistedMaxExample();
	try {
	    seDAO.create(se);
	} catch (SQLException e) {
	    testPassed = false;
	}
	assertTrue(testPassed);
    }
    */

    /*
    public void testCreateTfIdf() {
	boolean testPassed = true;
	ScoredEntity se = getUnpersistedTfIdfExample();
	try {
	    seDAO.create(se);
	} catch (SQLException e) {
	    testPassed = false;
	}
	assertTrue(testPassed);	
    }
    */
    /*
    public void testRetrieveMax() {
	boolean testPassed = true;
	ScoredEntity se = getUnpersistedMaxExample();	
	List sList = new ArrayList();
	System.out.println("Pre-Retrieved: " + se);
	try {
	    sList = seDAO.retrieve(se);
	} catch (SQLException e) {
	    testPassed = false;
	}
	se = (ScoredEntity)sList.get(0);
	System.out.println("Retrieved: " + se);
	assertTrue(testPassed);
	assertEquals(1, sList.size());
	}*/

    public void testRetrieveTfIdf() {
	boolean testPassed = true;
	ScoredEntity se = getCard1Query();	
	List sList = new ArrayList();
	System.out.println("Query: " + se);
	try {
	    sList = seDAO.retrieve(se, 0, 200, "tf_idf", "ASC");
	} catch (SQLException e) {
	    testPassed = false;
	}
	assertTrue(testPassed);
	assertEquals(190, sList.size());
    }
    /*
    public void testUpdateMax() {
	boolean testPassed = true;
	ScoredEntity se = getUnpersistedMaxExample();
	Score score = new Score();
	score.setTfIdfScore(new Double(3.14159));
	se.setScore(score);
	Location location = se.getLocation();
	//	System.out.println("Before update: " + location);
	try {
	    seDAO.update(se);
	} catch (SQLException e) {
	    System.out.println(e);
	    testPassed = false;
	}
	assertTrue(testPassed);
    }
    */
    /*
    public void testUpdateTfIdf() {
	boolean testPassed = true;
	ScoredEntity se = getUnpersistedTfIdfExample();
	Score score = new Score();
	score.setTfIdfScore(new Double(3.33));
	se.setScore(score);
	Location location = se.getLocation();
		System.out.println("Before update: " + location);
	try {
	    seDAO.update(se);
	} catch (SQLException e) {
	    System.out.println(e);
	    testPassed = false;
	}
	assertTrue(testPassed);
    }

    public void testDeleteMax() {
	boolean testPassed = true;
	ScoredEntity se = getUnpersistedMaxExample();
	Score score = new Score();
	score.setTfIdfScore(new Double(3.33));
	se.setScore(score);
	try {
	    List results = seDAO.retrieve(se);
	    se = (ScoredEntity)results.get(0);
	    seDAO.delete(se);	
	} catch (SQLException e) {
	    System.out.println(e);
	    testPassed = false;
	}
	assertTrue(testPassed);
    }

    public void testDeleteTfIdf() {
	boolean testPassed = true;
	ScoredEntity se = getUnpersistedTfIdfExample();
	Score score = new Score();
	score.setTfIdfScore(new Double(3.33));
	se.setScore(score);
	try {
	    List results = seDAO.retrieve(se);
	    se = (ScoredEntity)results.get(0);
	    seDAO.delete(se);	
	} catch (SQLException e) {
	    System.out.println(e);
	    testPassed = false;
	}
	assertTrue(testPassed);
	}


    public void testClear() {
	boolean testPassed = true;
	try {
	    seDAO.clear();
	} catch (SQLException e) {
	    testPassed = false;
	}
	assertTrue(testPassed);
	}*/
    /**
    public void testGetDistinctScoredEntitiesCount() {
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	int count = 0;

	long start = System.currentTimeMillis();	
	try {
	    count = seDAO.getDistinctScoredEntitiesCount(aesch_ag_1);
	} catch (SQLException e) {
	    System.err.println(e);
	}
	long end = System.currentTimeMillis();
	
		System.out.println("testGetDistinctScoredEntitiesCount: [ " + (end - start) + " ]");
	assertEquals(186, count);
    }

    public void testGetNumChunksContaining() {
	Entity lampas = (Entity)new Lemma();
	int id = 323844;
	lampas.setId(new Integer(id));
	 Contain lampas
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Query aesch_ag_83 = new Query("Perseus:text:1999.01.0003:card=83");
	Query aesch_ag_281 = new Query("Perseus:text:1999.01.0003:card=281");
	 Do not contain lampas
	Query aesch_ag_1617 = new Query("Perseus:text:1999.01.0003:card=1617");
	
	List docChunks = new ArrayList();
	docChunks.add(new Chunk(aesch_ag_1));
	docChunks.add(new Chunk(aesch_ag_83));
	docChunks.add(new Chunk(aesch_ag_281));
	docChunks.add(new Chunk(aesch_ag_1617));
	
	int num_chunks_containing = 0;
	long start = System.currentTimeMillis();
	num_chunks_containing = seDAO.getNumChunksContaining(lampas, docChunks);
	long end = System.currentTimeMillis();
	//	System.out.println("testGetNumChunksContaining [ " + (end - start) + "ms ]");
	assertTrue(num_chunks_containing == 3);
    }

    public void testChunkContains() {
	Entity lampas = (Entity)new Lemma();
	int id = 323844;
	lampas.setId(new Integer(304630));
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Query aesch_ag_1617 = new Query("Perseus:text:1999.01.0003:card=1617");
	boolean aesch_ag_1_contains = false;
	boolean aesch_ag_1617_contains = true;
	try {
	    aesch_ag_1_contains = seDAO.chunkContains(lampas, new Chunk(aesch_ag_1));
	    aesch_ag_1617_contains = seDAO.chunkContains(lampas, new Chunk(aesch_ag_1617));
	} catch (SQLException e) {
	    System.err.println(e);
	}
	assertTrue(aesch_ag_1_contains);
	assertFalse(aesch_ag_1617_contains);		
    }
    */

    public static ScoredEntity getPersistedMaxExample() {
	ScoredEntity se = getUnpersistedMaxExample();
	ScoredEntityDAO seDAO = (ScoredEntityDAO)new SQLScoredEntityDAO();
	List seList = new ArrayList();
	try {
	    seList = seDAO.retrieve(se);
	} catch (Exception e) {
	    System.err.println(e); 
	}
	se = (ScoredEntity)seList.get(0);
	return se;
    }

    public static ScoredEntity getUnpersistedMaxExample() {
	ScoredEntity se = new ScoredEntity();

	Lemma lemma = new Lemma();
	lemma.setHeadword("fu/lac");
	lemma.setLanguageID(new Integer(2));
	LemmaDAO lDAO = (LemmaDAO) new HibernateLemmaDAO();
	lDAO.beginTransaction();
	List lList = lDAO.findLemma(lemma);
	lDAO.endTransaction();
	lemma = (Lemma)lList.get(0);

	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Location location = new Location();
	location.setQuery(aesch_ag_1);

	Score score = new Score();
	score.setMinFrequencyScore(new Integer(3));
	score.setMaxFrequencyScore(new Integer(11));
	
	se.setEntity(lemma);
	se.setLocation(location);
	se.setScore(score);
	return se;
    }
    
    public static ScoredEntity getCard1Query() {
	ScoredEntity se = new ScoredEntity();
	Lemma lemma = new Lemma();
	
	Query aesch_ag_1 = new Query("Perseus:text:1999.01.0003:card=1");
	Query aesch_ag = new Query("Perseus:text:1999.01.0003");
	Location location = new Location();
	location.setQuery(aesch_ag_1);

	//	Score score = new Score();
	//	score.setMinFrequencyScore(new Integer(3));
	//	score.setMaxFrequencyScore(new Integer(11));
	//	score.setTfIdfScore(new Double(9.21));

	se.setEntity(lemma);
	se.setLocation(location);
	se.setCollectionQuery(aesch_ag);
	return se;
    }

}


