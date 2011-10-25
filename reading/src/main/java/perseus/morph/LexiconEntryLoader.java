/*
 * Extracts all the entries from a given lexicon. If lemmas corresponding to
 * the entry headwords do not exist, we create them and set their attributes,
 * including short definitions, based on the given chunk. Otherwise, we simply
 * add the given chunk as one of the lemma's lexicon entries.
 *
 * The only prerequisite for this loader is that the target lexicon have been
 * chunkified.
 *
 * @see Lemma
 * @see CorpusChunkifier
 */

package perseus.morph;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import perseus.document.Chunk;
import perseus.document.DOMOffsetAdder;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;
import perseus.language.Language;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.morph.dao.LemmaDAO;
import perseus.util.HibernateUtil;

/**
 * Responsible for loading lexicon entries from a source XML file.
 */

public class LexiconEntryLoader extends RecentlyModifiedCorpusProcessor {
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    private static final String ELEMENTARY_LEWIS = "Perseus:text:1999.04.0060";
    
    private static final Pattern HEADWORD_PATTERN = Pattern.compile(
								    "(.*?)#?(\\d+)$");    
    
    private LemmaDAO dao = new HibernateLemmaDAO();
    private ChunkDAO chunkDAO = new HibernateChunkDAO();
    
    private Language language;
    private int entryCount = 0;
    
    @Override
    public String getTaskName() {
	return "load-lexicon-entries";
    }
    
    public static void main(String args[]) {
        LexiconEntryLoader loader = new LexiconEntryLoader();
        
    	Options options = new Options()
	    .addOption("f", "force", false, "force loading of all lexicon entries");
    	
    	CommandLineParser parser = new PosixParser();
    	CommandLine cl;
    	String[] workingArgs = args;

    	try{
    	    cl = parser.parse(options, args);
    	    if (cl.hasOption("force") || cl.hasOption('f')) {
    		loader.logger.info("Forcing loading of all lexicon entries");
    		loader.setIgnoreModificationDate(true);
    	    }
    	    workingArgs = cl.getArgs();
    	} catch (ParseException e) {
    	    loader.logger.error("Unable to parse command-line arguments", e);
    	    System.exit(1);
    	}
    	
        if (workingArgs.length > 0) {
            for (String arg : workingArgs) {
                loader.processAnything(arg);
            }
        } else {
            loader.processCorpus();
        }
    }
    
    public boolean shouldProcessDocument(Chunk documentChunk) {
    	if (documentChunk.getMetadata().getDefaultChunk() != null) {
    		return super.shouldProcessDocument(documentChunk) && 
    		documentChunk.getMetadata().hasSubjectLanguage() &&
    		documentChunk.getMetadata().getDefaultChunk().equalsIgnoreCase("entry");
    	} else {
    		return false;
    	}
    }

    public void startDocument(Chunk documentChunk) {
        language = documentChunk.getMetadata().getSubjectLanguage();
        dao.beginTransaction();
        entryCount = 0;
    }
    
    public void endDocument(Chunk documentChunk) {
        dao.endTransaction();
        logger.info("Ending document "+documentChunk.getDocumentID());
        super.endDocument(documentChunk);
    }
    
    public void processChunk(Chunk documentChunk) {
        if (!documentChunk.getType().equals("entry")) return;
	
        // Long marks (_) and syllable breaks (^) are in the
        // Latin lexicon, but not in the morphology output, 
        // so, regretfully, we need to remove them.
        String entryName = documentChunk.getValue().replaceAll("[_^]", "");
        org.jdom.Document chunkElement = null;
	
	
        try {
            chunkElement = DOMOffsetAdder.domFromChunk(documentChunk);
        } catch (Exception e) {
            logger.error("Unable to read chunk; no short def. available", e);
        }
	
        Matcher headwordMatcher = HEADWORD_PATTERN.matcher(entryName);
	
        String headword = entryName;
        int sequenceNumber = 1;
        if (headwordMatcher.matches()) {
            headword = headwordMatcher.group(1);
            sequenceNumber = Integer.parseInt(headwordMatcher.group(2));
        }
        String bareHeadword = Lemma.BARE_WORD_PATTERN.matcher(headword).replaceAll("");
	
        List<Lemma> matches = dao.getMatchingLemmas(
						    headword, sequenceNumber, language.getCode(), false);

        String shortDef = null;
        if (chunkElement != null) {
        	shortDef = Lemma.extractDefinition(chunkElement);
        }
        Lemma lemma;        
        if (matches.isEmpty()) {  
            lemma = new Lemma();
            lemma.setHeadword(headword);
            lemma.setBareHeadword(bareHeadword);
            lemma.setSequenceNumber(sequenceNumber);
            lemma.setLanguage(language);
	    
            lemma.setAuthorityName(
				   String.format("%s%d(%s)", headword, sequenceNumber, language));
            lemma.setDisplayName(headword);
            lemma.setSortableString(
				    String.format("%s%s", headword, sequenceNumber));
            lemma.setShortDefinition(shortDef);				
	    
            dao.save(lemma);
            matches.add(lemma);
        } else {
            lemma = matches.get(0);
            /*
             * Elem. Lewis has better short definitions than Lewis & Short.
             * Write over any short definitions added from Lewis & Short that 
             * are also in Elem. Lewis.
             * Update with the latest short def if the existing def is null
	     */
	    if (documentChunk.getDocumentID().equals(ELEMENTARY_LEWIS) || lemma.getShortDefinition() == null) {
		if (shortDef != null) {
        		lemma.setShortDefinition(shortDef);
        		dao.update(lemma);
        		HibernateUtil.getSession().flush();
		}
	    }
        }
	
        documentChunk.setLemma(lemma);
        HibernateUtil.getSession().refresh(lemma);
        chunkDAO.updateChunk(documentChunk);
	
        //lemma.addLexiconChunk(documentChunk);		
        //dao.update(lemma);
        entryCount++;
        if (entryCount % 20 == 0) {
	    HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        }
        if (entryCount % 5000 == 0) {
            logger.info(String.format("[%6d] %s -> chunk %d",
				   entryCount, lemma.getHeadword(), documentChunk.getId()));
        }
    }
}
