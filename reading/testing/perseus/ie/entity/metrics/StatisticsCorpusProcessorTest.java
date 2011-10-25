package testing.perseus.ie.entity.metrics;
/**
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */

import java.util.Map;
import junit.framework.TestCase;
import perseus.ie.entity.metrics.StatisticsCorpusProcessor;

public class StatisticsCorpusProcessorTest extends TestCase {

    public StatisticsCorpusProcessorTest (String name) {
	super(name);
    }

    public void testProcessDocument() {
	String aesch_ag = "Perseus:text:1999.01.0003";
	StatisticsCorpusProcessor scp = new StatisticsCorpusProcessor();
	scp.processDocument(aesch_ag);
	Map documentLemmaScores = scp.getDocumentLemmaScores();
	assertEquals(4, documentLemmaScores.keySet().size());
    }

}
