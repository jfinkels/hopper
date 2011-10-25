package perseus.ie.freq;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.ie.entity.AbstractEntity;
import perseus.ie.entity.Entity;
import perseus.ie.freq.dao.HibernateFrequencyDAO;
import perseus.language.Language;
import perseus.util.HibernateUtil;

/**
 * Loads some statistics, including IDF and document count, for entities
 * and/or lemmas. Run this after you've run the frequency-loading classes.
 */
public class DocumentCountLoader {
    
    private static Logger logger = Logger.getLogger(DocumentCountLoader.class);

    public void process() {
	logger.info("Processing lemmas");
	processLemmas();
	logger.info("Processing named entities");
	processNamedEntities();
    }

    private void processLemmas() {
    	for (Language language : Language.getAll()) {
    		processLemmas(language);
    	}
    	HibernateFrequencyDAO freqDAO = new HibernateFrequencyDAO(Frequency.class);
    	freqDAO.beginTransaction();
    	logger.info("Calculating TF/IDF/saving");
    	freqDAO.updateFrequencyWithTfIdf();
    	freqDAO.endTransaction();
    }
   
    public void processLemmas(Language language) {
	if (!language.getHasMorphData()) return;
	
	logger.info("Processing language: " + language.getName());
	MetadataDAO dao = new SQLMetadataDAO();
	
	List<Query> texts = dao.getDocuments(Metadata.LANGUAGE_KEY, language.getAbbreviations(), true);
	
	int totalDocuments = texts.size();
	logger.info("Counted " + totalDocuments + " documents");
	
	HibernateFrequencyDAO freqDAO =
	    new HibernateFrequencyDAO(EntityDocumentFrequency.class);
	freqDAO.beginTransaction();
	
	logger.info("Fetching lemmas for "+language.getName());
	
	ScrollableResults results =
	    freqDAO.getScrollableLemmasWithCounts(language);

	logger.info("Calculating IDF/saving");
	
	processResults(results, totalDocuments);
	
	freqDAO.endTransaction();
    }

    private void processResults(ScrollableResults results, int totalDocuments) {
	double log2 = Math.log(2);
	
	int updateCount = 0;
	while (results.next()) {
	    Entity entity = (Entity) results.get(0);
	    int documentCount = ((Long) results.get(1)).intValue();
	    double idf = 
		Math.log((double) totalDocuments / documentCount) / log2;
	    
	    long maxFrequency = (Long) results.get(2);
	    long minFrequency = (Long) results.get(3);
	    
	    entity.setDocumentCount(documentCount);
	    entity.setInverseDocumentFrequency(idf);
	    entity.setMaxOccurrenceCount(maxFrequency);
	    entity.setMinOccurrenceCount(minFrequency);
	    
	    HibernateUtil.getSession().update(entity);
	    updateCount++;
	    if (updateCount % 20 == 0) {
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
	    }
	    
	    if (updateCount % 1000 == 0) {
		logger.info(updateCount + " entities updated");
	    }
	}
	results.close();
    }
    
    public void processNamedEntities() {
	HibernateFrequencyDAO dao =
	    new HibernateFrequencyDAO(EntityDocumentFrequency.class);
	
	dao.beginTransaction();
	int totalDocuments = (int) dao.getTotalDocumentCount();
	logger.info("Counted " + totalDocuments + " documents w/ entities");
	
	ScrollableResults results =
	    dao.getScrollableEntitiesWithCounts(AbstractEntity.class);
	logger.info("Processing entities...");
	
	processResults(results, totalDocuments);
	dao.endTransaction();
	logger.info("Done");
    }
    
    public static void main(String[] args) {
	Options options = new Options()
	.addOption("l", "lemmas", false, "only calculate IDF/document count for lemmas")
	.addOption("e", "entities", false, "only calculate IDF/document count for named entities")
	.addOption("h", "help", false, "print this message");

	CommandLineParser parser = new PosixParser();
	CommandLine cl = null;
	try {
	    cl = parser.parse(options, args);
	} catch (ParseException e) {
	    logger.error("Bad command-line options", e);
	    System.exit(1);
	}
	
	boolean processLemmas = false;
	boolean processNamedEntities = false;
	if (cl.hasOption('l') || cl.hasOption("lemmas")) {
	    processLemmas = true;
	}
	if (cl.hasOption('e') || cl.hasOption("entities")) {
	    processNamedEntities = true;
	}
	
	if (cl.hasOption("help") || cl.hasOption('h')) {
	    new HelpFormatter().printHelp(
		    DocumentCountLoader.class.getName(), options);
	    System.exit(0);
	}
	
	if (!processLemmas && !processNamedEntities) {
	    processLemmas = true;
	    processNamedEntities = true;
	}
	
        DocumentCountLoader loader = new DocumentCountLoader();
	if (processLemmas) loader.processLemmas();
	if (processNamedEntities) loader.processNamedEntities();
    }
}
