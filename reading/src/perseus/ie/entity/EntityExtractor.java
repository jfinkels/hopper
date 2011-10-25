package perseus.ie.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.output.XMLOutputter;

import perseus.document.Chunk;
import perseus.document.DOMOffsetAdder;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.ie.Location;
import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.EntityTuple;
import perseus.ie.freq.dao.FrequencyDAO;
import perseus.ie.freq.dao.HibernateFrequencyDAO;
import perseus.voting.dao.HibernateVoteDAO;

/**
 * Class responsible for extracting tagged entities from documents. Currently
 * looks for places, people and dates; you can have it find additional
 * entities in the future (for example, organization names) by adding another
 * instance of the Adapter subclass.  Each instance of this subclass specifies
 * the elements it will process (using a JDOM {@link ElementFilter}, as
 * returned by <code>getFilter()</code>) to find appropriate elements, and the
 * <code>extractEntity()</code> method to actually create them).
 *
 * This replaces the old Jython scripts (in the scripts/ie directory) that
 * did the same thing, except that there were separate scripts for each type of
 * entity.
 */
@SuppressWarnings("serial")
public class EntityExtractor extends RecentlyModifiedCorpusProcessor {
    private static final Logger logger =
	Logger.getLogger(EntityExtractor.class);

    static XMLOutputter outputter = new XMLOutputter();

    static DateParser parser = new CivilWarDateParser();
    static HibernateEntityManager manager = new HibernateEntityManager();
    static Chunk currentChunk;
    
    private boolean deleteExisting = true;
    private HashMap<String,Boolean> seenDocuments = new HashMap<String,Boolean>();
    
    public String getTaskName() {
		return "extract-entities";
	}
    
    private static Map<String,Entity> entityCache =
	new LinkedHashMap<String,Entity>() {
	    protected boolean removeEldestEntry(Map.Entry eldest) {
		return (size() > 20000);
	    }
	};
    
    private static Map<String,EntityDocumentFrequency> runningFrequencies =
	new HashMap<String,EntityDocumentFrequency>();

    public enum Adapter {
        PLACE(Place.class) {
            Set<String> unknownPlaces = new HashSet<String>();
	    
            @Override
		public Filter getFilter() {
                return new ElementFilter("placeName") {
			public boolean matches(Object obj) {
			    if (super.matches(obj)) return true;
			    if (obj instanceof Element) {
				Element elt = (Element) obj;
				
				return (elt.getName().equals("name") &&
					"place".equals(
						       elt.getAttributeValue("type")));
			    }
			    return false;
			}
		    };
            }
	    
            @Override
		public Entity extractEntity(Element element) {
                // Don't create any new entities for places; look them up
                // against existing records, which should have been created
                // from the atlas table and the "atlas-convert" Ant task.
		
                // First see if, at some point in the past, we checked for
                // this text and couldn't find it.
                if (unknownPlaces.contains(element.getText())) return null;
		
                // Some keys appear to actually be multiple keys concatenated
                // together wth semicolons; grab the first element
                String key = element.getAttributeValue("key");
                if (key != null) {
                    Entity place = manager.getEntityByAuthName(key.split(";")[0]);
                    if (place != null) return place;
                }
		
                String displayName = element.getAttributeValue("n");
                String reg = element.getAttributeValue("reg");
                String childText =
                    outputter.outputString(element.getChildren());
		
                // If a lookup by the key didn't return a result, try matching
                // against each of the attributes in turn, and finally the 
                // text.
                List<? extends Entity> entities;
                if (displayName != null) {
                    entities = manager.getMatchingEntities(
							   displayName, Place.class);
                    if (!entities.isEmpty()) {
                        return entities.get(0);
                    }
                }
		
                if (reg != null) {
                    entities = manager.getMatchingEntities(reg, Place.class);
                    if (!entities.isEmpty()) {
                        return entities.get(0);
                    }
                }
		
                if (childText != null) {
                    entities = manager.getMatchingEntities(
							   childText, Place.class);
                    if (!entities.isEmpty()) {
                        return entities.get(0);
                    }
                }
		
                logger.warn("No match for place: " + element.getText());
                unknownPlaces.add(element.getText());
                return null;
            }
	    
            public boolean isAmbiguous(EntityOccurrence occurrence) {
                // A place is probably unambiguous if it contains a comma or
                // a left parenthesis (like "Cambridge, MA")--otherwise, it's
                // ambiguous.
                String text = occurrence.getDisplayText();
                return (text.indexOf("(") == -1 && text.indexOf(",") == -1);
            }
        },
	
        PERSON(Person.class) {
            Pattern personPattern = Pattern.compile(".*?:([^:]*)(?::.*)?");
	    
            @Override
		public Filter getFilter() {
                return new ElementFilter("persName");
            }
	    
            @Override
		public Entity extractEntity(Element element) {
                String reg = element.getAttributeValue("reg");
                if (reg == null) {
                    logger.warn(
				String.format("Empty reg attribute for %s [%s]",
					      element.getName(), element.getValue()));
                    return null;
                }
		
                Matcher matcher = personPattern.matcher(reg);
                if (!matcher.matches()) return null;
		
                String name = matcher.group(1);
                List<String> nameTokens =
                    new ArrayList<String>(Arrays.asList(name.split(",")));
		
                // The current format:
                // surname,forename1,forename2,...
                // (sometimes things like "nomatch" appear and should be
                // ignored)
                Person person = new Person();
                person.addName(Person.SURNAME, nameTokens.remove(0));
                for (String currentName : nameTokens) {
                    String trimmedName = currentName.trim();
                    if (!trimmedName.equals("") &&
			!trimmedName.equals("nomatch")) {
                        person.addName(Person.FORENAME, trimmedName);
                    }
                }
		
                return person;
            }
	    
            public boolean isAmbiguous(EntityOccurrence occurrence) {
                // Assume that a person is "ambiguous" if the name as it
                // appeared in the text contains *fewer* tokens than the
                // display name of the entity we looked up (e.g., if the text
                // contains "Washington" and the occurrence was matched to
        	// "George Washington", it's probably ambiguous). This isn't
        	// a very good metric, but it'll do for now.
                Person person = (Person) occurrence.getEntity();
                int nameCount = person.getNames().size();
		
                String displayText = occurrence.getDisplayText();
                return (nameCount >= displayText.split("\\s+").length);
            }
        },
	
        DATE(Date.class) {
            @Override
		public Filter getFilter() {
                return new Filter() {
			public boolean matches(Object obj) {
			    if (obj instanceof Element) {
				String name = ((Element) obj).getName();
				return (name.equalsIgnoreCase("date") ||
					name.equalsIgnoreCase("dateStruct"));
			    }
			    return false;
			}
		    };
            }
	    
            public Entity extractEntity(Element element) {
                if (element.getName().equals("date")) {
                    return processDate(element);
                } else {
                    return processDateStruct(element);
                }
            }
	    
            private Entity processDate(Element element) {
                String dateText = element.getAttributeValue("value");
                if (dateText == null) {
                    dateText = outputter.outputString(element.getChildren());
                }
		
                if (dateText == null || dateText.equals("")) {
                    logger.warn("Bad date: " + outputter.outputString(element));
                    return null;
                }
		
                try {
                    return parser.parse(dateText);
                } catch (NumberFormatException nfe) {
                    logger.warn("Bad date value: " + dateText);
                }

                return null;
            }

            private Entity processDateStruct(Element element) {
                String value = element.getAttributeValue("value");
                if (value == null) return null;

                try {
                    return parser.parse(value);
                } catch (NumberFormatException nfe) {
                    logger.warn("Bad dateStruct value: " + value);
                }

                Date date = new Date();
                for (Object content : element.getChildren()) {
                    if (!(content instanceof Element)) continue;
                    Element child = (Element) content;
                    String childName = child.getName();
		    
                    // UGH
                    String childValue = outputter.outputString(
							       child.getContent());
                    
                    if (childValue == null || childValue.equals("")) break;
                    if (childName.equals("year")) {
                        try {
                            date.setYear(Integer.parseInt(childValue));
                        } catch (NumberFormatException nfe) {
                            logger.warn("Bad value for year: " + childValue);
                        }
                    } else if (childName.equals("month")) {
                        try {
                            date.setMonth(Integer.parseInt(childValue));
                        } catch (NumberFormatException nfe) {
                            logger.warn("Bad value for month: " + childValue);
                        }
                    } else if (childName.equals("day")) {
                        try {
                            date.setDay(Integer.parseInt(childValue));
                        } catch (NumberFormatException nfe) {
                            logger.warn("Bad value for day: " + childValue);
                        }
                    }
                }

                return date;
            }
        },

        DATE_RANGE(DateRange.class) {
            @Override
            public Filter getFilter() {
                return new ElementFilter("dateRange");
            }

            @Override
            public Entity extractEntity(Element element) {
                String start = element.getAttributeValue("from");
                String end = element.getAttributeValue("to");

                try {
                    return new DateRange(
                            parser.parse(start), parser.parse(end));
                } catch (Exception e) {
                    logger.warn(String.format("Bad dateRange values: %s, %s",
                                start, end));
                }

                return null;
            }
        };

        public Map<Element,EntityOccurrence> process(Document document) {
            Map<Element,EntityOccurrence> occurrences =
                new HashMap<Element,EntityOccurrence>();

            Iterator it = document.getDescendants(getFilter());
            while (it.hasNext()) {
                Element element = (Element) it.next();

                Entity entity = extractEntity(element);
                if (entity == null) continue;

                Entity existingEntity =
                    manager.getEntityByAuthName(entity.getAuthorityName());
                if (existingEntity == null) {
                    manager.registerEntity(entity);
                    existingEntity = entity;
                }
                entityCache.put(entity.getAuthorityName(), existingEntity);

                EntityOccurrence occurrence =
                    createOccurrence(existingEntity, element);
                occurrences.put(element, occurrence);
            }

            return occurrences;
        }

        private EntityOccurrence createOccurrence(
                Entity entity, Element element) {

            int byteOffset = Integer.parseInt(
                    element.getAttributeValue("start_offset"));
//            
//            StringBuilder displayText = new StringBuilder();
//            for (Object child : element.getContent()) {
//                if (child instanceof Text) {
//                    displayText.append(((Text) child).getValue());
//                }
//            }

            Location location =
                new Location(currentChunk.getQuery(), byteOffset);

            return new EntityOccurrence(
                    entity, location, element.getValue());
        }

        public abstract Filter getFilter();
        public abstract Entity extractEntity(Element element);
        public boolean isAmbiguous(EntityOccurrence occurrence) {
            return false;
        }
        
        private Class<? extends Entity> entityClass;
        private Class<? extends Entity> getEntityClass() {
            return entityClass;
        }

        private Adapter(Class<? extends Entity> ec) {
            entityClass = ec;
        }
    }

    @Override
    public void startDocument(Chunk documentChunk) {
        String documentID = documentChunk.getDocumentID();
   		runningFrequencies.clear();
                
        manager.beginWrite();
        
        //FrequencyDAO tupleDAO = new HibernateFrequencyDAO(EntityTuple.class);
        if (deleteExisting) {
        	// && tupleDAO.getCount(documentID, getClasses()) > 0L) {
            //tupleDAO.deleteByDocument(documentID, getClasses());            
        	
        	// 2011-05-18 BALMAS -- these should maybe be a delete by query
        	// but hib_frequencies and hib_votes don't include the subquery....
        	// so check to see if we've already processed the document or not first
        	if (!(seenDocuments.containsKey(documentID))) {
        		logger.info("Deleting occurrences, frequencies and votes for " + documentID);
        		new HibernateFrequencyDAO(EntityDocumentFrequency.class)
        			.deleteByDocument(documentID, getClasses());
            	new HibernateVoteDAO().deleteByDocument(documentID);
            	manager.clearOccurrences(documentID, AbstractEntity.class);
            	logger.info("Done deleting");
        	}                    	    
        }
        seenDocuments.put(documentChunk.getDocumentID(), true);
//        manager.clearFrequencies(documentID, AbstractEntity.class);
//        manager.clearTuples(documentID, AbstractEntity.class);
    }

    private List<Class<? extends Entity>> getClasses() {
	List<Class<? extends Entity>> classes =
	    new ArrayList<Class<? extends Entity>>();
	for (Adapter adapter : Adapter.values()) {
	    classes.add(adapter.getEntityClass());
	}
	
	return classes;
    }

    @Override
    public void endDocument(Chunk documentChunk) {
        super.endDocument(documentChunk);

        if (documentSucceeded()) {

            logger.info("Aggregating frequencies");
            for (EntityDocumentFrequency freq : runningFrequencies.values()) {
	        	manager.addFrequency(freq);
            }
	//            new HibernateFrequencyDAO(EntityTuple.class)
	//            .aggregateDocument(
	//        	    documentChunk.getDocumentID(),
	//        	    getClasses());
	            logger.info("Done aggregating");
        }

        manager.endWrite();    	

    }

    @Override
    public void processChunk(Chunk documentChunk) {
	currentChunk = documentChunk;
        Document document = null;
        try {
            document = DOMOffsetAdder.domFromChunk(documentChunk);
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        } catch (JDOMException jde) {
            throw new IllegalArgumentException(jde);
        }

                        
        for (Adapter adapter : Adapter.values()) {
            Map<Element,EntityOccurrence> results = adapter.process(document);
            for (EntityOccurrence occurrence : results.values()) {
                manager.addOccurrence(occurrence);
            }

            Collection<EntityTuple> tuples = createTuples(
                    results, documentChunk, document, adapter);
            for (EntityTuple tuple : tuples) {
                manager.addTuple(tuple);
                
                updateDocumentFrequencies(documentChunk, tuple);
            }
        }
    }

    private void updateDocumentFrequencies(Chunk chunk, EntityTuple tuple) {
	Entity entity = tuple.getEntity();

	if (!runningFrequencies.containsKey(entity.getAuthorityName())) {
	    runningFrequencies.put(
		    entity.getAuthorityName(), 
		    new EntityDocumentFrequency(entity, chunk.getDocumentID()));
	}
	
	runningFrequencies.get(entity.getAuthorityName()).add(tuple);
    }

    private Collection<EntityTuple> createTuples(
            Map<Element, EntityOccurrence> occurrences,
            Chunk chunk,
            Document document,
            Adapter adapter) {

        Map<Entity,EntityTuple> tuples = new HashMap<Entity,EntityTuple>();
        Map<Entity,List<Element>> entityElements =
            new HashMap<Entity,List<Element>>();

        for (Element element : occurrences.keySet()) {
            EntityOccurrence occurrence = occurrences.get(element);
            Entity entity = occurrence.getEntity();

            if (!entityElements.containsKey(entity)) {
                entityElements.put(entity, new ArrayList<Element>());
            }
            entityElements.get(entity).add(element);

            if (!tuples.containsKey(entity)) {
                tuples.put(entity, new EntityTuple(entity, chunk));
            }

            EntityTuple tuple = tuples.get(entity);
            tuple.count(adapter.isAmbiguous(occurrence) ? 2 : 1);

            if (occurrence.getLocation().getPosition() < 
                    tuple.getFirstPosition()) {
                tuple.setFirstPosition(
                        occurrence.getLocation().getPosition());
            }
        }

	if (!tuples.isEmpty()) {
	    logger.info(String.format("%s: %d unique %s entities",
			chunk.getQuery(),
                        tuples.size(),
                        adapter.getEntityClass().getSimpleName()));
	}

        SnippetCreator snipper = new SnippetCreator(document);
        for (Entity entity : entityElements.keySet()) {
            List<Element> matchingElements = entityElements.get(entity);

            EntityTuple tuple = tuples.get(entity);
            tuple.setSnippet(snipper.createSnippet(matchingElements));
        }

        return tuples.values();
    }

    public static void main(String[] args) {
        EntityExtractor xtr = new EntityExtractor();
	Options options = new Options()
	.addOption("n", "no-delete", false, "don't clear existing records for each document")
	.addOption("f", "force", false, "load regardless of last modification date")
	.addOption("a", "aggregate", false, "aggregate corpus stats (use this after you've loaded all individual documents)")
	.addOption("h", "help", false, "print this message");

	CommandLineParser parser = new PosixParser();
	CommandLine cl = null;
	try {
	    cl = parser.parse(options, args);
	} catch (ParseException e) {
	    logger.error("Bad command-line options", e);
	    System.exit(1);
	}
	
	if (cl.hasOption('n') || cl.hasOption("no-delete")) {
	    xtr.setDeleteExisting(false);
	}
	if (cl.hasOption("force") || cl.hasOption('f')) {
	    xtr.setIgnoreModificationDate(true);
	}
	
	if (cl.hasOption("aggregate") || cl.hasOption('a')) {
	    xtr.aggregateCorpusFrequencies();
	    System.exit(0);
	}
	
	if (cl.hasOption("help") || cl.hasOption('h')) {
	    new HelpFormatter().printHelp(
		    EntityExtractor.class.getName(), options);
	    System.exit(0);
	}
	
	String[] remainingArgs = cl.getArgs();
        if (remainingArgs.length > 0) {
            for (String arg : remainingArgs) {
                xtr.processAnything(arg);
            }
        } else {
            xtr.processCorpus();
        }
    }

    private void setDeleteExisting(boolean b) {
	deleteExisting = b;
    }

    public boolean getDeleteExisting() {
        return deleteExisting;
    }

    private void aggregateCorpusFrequencies() {
    	// 2011-05-18 BALMAS -> this is broken because
    	// HibernateFrequenceDAO converts the class to EntityTuple
    	// see comment in that file
	logger.info("Aggregating...");
	FrequencyDAO fDAO = new HibernateFrequencyDAO(EntityDocumentFrequency.class);
	fDAO.beginTransaction();
		fDAO.aggregateAllDocuments(deleteExisting);
		fDAO.endTransaction();
    }

}
