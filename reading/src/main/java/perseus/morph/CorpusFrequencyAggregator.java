package perseus.morph;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import perseus.document.Corpus;
import perseus.ie.entity.HibernateEntityManager;

/**
 * Aggregates word frequencies for all known corpora and collections. Should
 * after the FrequencyLoader.
 */

public class CorpusFrequencyAggregator {
    
    private HibernateEntityManager manager;
    private Logger logger = Logger.getLogger(getClass());

    public void aggregate() {
	List<Corpus> corpora = new ArrayList<Corpus>();
	corpora.addAll(Corpus.getConglomerates());
	corpora.addAll(Corpus.getAuthorCorpora());
	
	manager = new HibernateEntityManager();
	manager.beginWrite();
	for (Corpus corpus : corpora) {
	    aggregate(corpus);
	}
	manager.endWrite();
	
	logger.info("Done aggregating");
    }
    
    public void aggregate(Corpus corpus) {
	logger.info("Aggregating " + corpus);
	manager.aggregateCorpusFrequencies(corpus);
    }

    public static void main(String[] args) {
	CorpusFrequencyAggregator aggro = new CorpusFrequencyAggregator();
	aggro.aggregate();
    }
}
