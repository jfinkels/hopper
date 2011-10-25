package perseus.visualizations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.apache.commons.lang.time.DateFormatUtils;

import perseus.document.Chunk;
import perseus.document.DOMOffsetAdder;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityOccurrence;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.entity.Place;
import perseus.ie.Location;
import perseus.ie.entity.EntityExtractor.Adapter;
import perseus.util.Config;

/**
 * Creates a RDF file serializing place entities in 
 * the Perseus collection as OAC Annotations, where the
 * annotation target is the uri to retrieve the source text (in XML)
 * from Perseus, and the annotation body is supplied as inline XML (marker tags)
 * 
 * @author Bridget Almas
 *
 */
public class PlacesOACCreator extends RecentlyModifiedCorpusProcessor {

	private static final Logger logger = Logger.getLogger(PlacesOACCreator.class);
	
	private static final String baseUrl = "http://www.perseus.tufts.edu/hopper/xmlchunk?doc=";
	
	private Map<String,List<Location>> textPlaceOccurrences;
	private Map<String,List<Location>> collectionPlaceOccurrences;
	private Map<String,List<Location>> allPlaceOccurrences;
	private Date textModificationDate = null;
	
	// texts are sometimes duplicated in different collections, don't process them more than once
	private Set<String> textsProcessed;
	
    static HibernateEntityManager manager = new HibernateEntityManager();
    private static int maxNumPlaces = -1;
    
    public PlacesOACCreator() {
    	textPlaceOccurrences = new HashMap<String,List<Location>>();
    	collectionPlaceOccurrences = new HashMap<String,List<Location>>();
    	allPlaceOccurrences = new HashMap<String,List<Location>>();
    	textsProcessed = new HashSet<String>();
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlacesOACCreator pxc = new PlacesOACCreator();
		pxc.setIgnoreModificationDate(true);
		if (args.length >0)
		{
			pxc.create(args[0]);
		}
		else
		{
			pxc.create();
		}
		
	}
	
	private void create(String a_doc) {
		logger.info("Processing place occurrences for " + a_doc);
		processDocument(a_doc);
		allPlacesToRDF(a_doc);
	}
	

	private void create() {
		String[] collections = Config.getPrimaryCollections();
		for (String collection : collections) {
			logger.info("Processing collection: "+collection);
			clearCollectionMap();
			processCollection(collection);
            allPlacesToRDF(collection);
		}
		logger.info("Processing all place occurrences");
		allPlacesToRDF(null);
	}
	
	

	private void clearCollectionMap() {
		collectionPlaceOccurrences.clear();
	}
	
	private void allPlacesToRDF(String pid) {
		List<Entry<String,List<Location>>> placeList;
		if (pid == null) {
			placeList = new ArrayList<Entry<String,List<Location>>>(allPlaceOccurrences.entrySet());
		} else if (pid.contains("collection")) {
			placeList = new ArrayList<Entry<String,List<Location>>>(collectionPlaceOccurrences.entrySet());
		} else {
			placeList = new ArrayList<Entry<String,List<Location>>>(textPlaceOccurrences.entrySet());
		}				
		
		if (placeList.isEmpty()) {
			return;
		}
		StringBuffer output = new StringBuffer();		
		output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		output.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
		output.append("  xmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
		output.append("  xmlns:dcterms=\"http://purl.org/dc/terms/\"");
		output.append("  xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"");
		output.append("  xmlns:oac=\"http://www.openannotation.org/ns/\"");
		output.append("  xmlns:cnt=\"http://www.w3.org/2008/content#\">\n");
		int max = placeList.size();
		if (maxNumPlaces > 0)
		{
			max = maxNumPlaces; 
		}
		
		for (int i = 0; i < placeList.size() && i < max; i++) {
			Entry<String,List<Location>> placeEntry = placeList.get(i);
			Place p = (Place) manager.getEntityByAuthName(placeEntry.getKey());
			output.append(createAnnotations(p,placeEntry.getValue()));
			output.append("  <oac:Body rdf:about=\"org.perseus:entity:" + p.getId() + "\">\n");
			output.append("    <rdf:type rdf:resource=\"cnt:ContentAsXML\"/>\n");
			output.append("    <cnt:rest rdf:parseType=\"Resource\">\n");
			output.append("    " + createMarker(p) + "\n");
			output.append("    </cnt:rest>\n");
			output.append("    <cnt:characterEncoding>utf-8</cnt:characterEncoding>\n");
			output.append("  </oac:Body>\n");										
		}
		output.append("</rdf:RDF>");	
		String filePath = Config.getStaticPath()+"xml/";
		String fileName = "";
		if (pid != null) {
			fileName = pid + ".";
		}
		fileName += "allplaces.rdf";
		try {
			File file = new File(filePath, fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(output.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	public void processChunk(Chunk documentChunk) {
		Document document = null;
        try {
            document = DOMOffsetAdder.domFromChunk(documentChunk);
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        } catch (JDOMException jde) {
            throw new IllegalArgumentException(jde);
        }
        
        Iterator it = document.getDescendants(Adapter.PLACE.getFilter());
        while (it.hasNext()) {
            Element element = (Element) it.next();
            Entity e = Adapter.PLACE.extractEntity(element);
            if (e != null) {
        		Location loc = null;
        		try {
	            	EntityOccurrence occur = manager.getOccurrenceByEntity(documentChunk,e,1	);
	            	loc = occur.getLocation();
	            	if (! collectionPlaceOccurrences.containsKey(e.getAuthorityName())) {
	            		collectionPlaceOccurrences.put(e.getAuthorityName(), new ArrayList<Location>());
	            	}
	            	if (! textPlaceOccurrences.containsKey(e.getAuthorityName())) {
	            		textPlaceOccurrences.put(e.getAuthorityName(), new ArrayList<Location>());
	            	}	            	
	            	collectionPlaceOccurrences.get(e.getAuthorityName()).add(loc);             	
	            	textPlaceOccurrences.get(e.getAuthorityName()).add(loc);
	            	logger.info("Extracted entity occurrence for " + e.getId() + " in " + documentChunk);
        		} catch (Exception a_e) {
        			logger.error("Error extracting entity occurrence for " + e.getId() + " in " + documentChunk);
        			a_e.printStackTrace();
        		}
            } 
        }   
	}
	
	public void startDocument(Chunk documentChunk) {
		String query = documentChunk.getQuery().toString();
		logger.info("Processing "+query);
		textsProcessed.add(query);
		textPlaceOccurrences.clear();
		textModificationDate = getReferenceDate(documentChunk);
		
	}
	
	public boolean shouldProcessDocument(Chunk documentChunk) {
		return (super.shouldProcessDocument(documentChunk) && !textsProcessed.contains(documentChunk.getQuery().toString()));
	}
	
	public void endDocument(Chunk documentChunk) {
		super.endDocument(documentChunk);
		allPlacesToRDF(documentChunk.getDocumentID());
	}

	private static StringBuffer createMarker(Place p) {
		StringBuffer marker = new StringBuffer();
		
		marker.append("<marker ").append("lat=\"").append(p.getLatitude()).append("\" ")
		.append("lon=\"").append(p.getLongitude()).append("\" ")
		.append("site=\"").append(p.getDisplayName()).append("\"/>\n");
		
		return marker;
	}
	
	private StringBuffer createAnnotations(Place p, List<Location> locations) {
		StringBuffer annotations = new StringBuffer();
		for (int i=0; i<locations.size(); i++)
		{		
			Location loc  = locations.get(i);			
			String annotId = "org.perseus:entityoccurrence:" + p.getId() + ":" + (i+1) + ":" + loc.getQuery(); 
			annotations.append("  <oac:Annotation rdf:about=\"" + annotId + "\">\n");
			annotations.append("    <oac:hasBody rdf:resource=\"org.perseus:entity:" + p.getId() + "\"/>\n");
			annotations.append("    <oac:hasTarget>\n");
			// TODO this should really use the CTS URN and CTS api to get down to the exact citation
			annotations.append("      <rdf:Description rdf:about=\"" + baseUrl + loc.getQuery() + "\"/>\n");			
			annotations.append("    </oac:hasTarget>\n");
			annotations.append("    <dc:title xml:lang=\"en\">" + p.getDisplayName() + "</dc:title>\n");
			annotations.append("    <dcterms:creator>Perseus Digital Library</dcterms:creator>\n");
			annotations.append("    <dcterms:created>" + DateFormatUtils.ISO_DATE_FORMAT.format(textModificationDate) + "</dcterms:created>\n");
			annotations.append("  </oac:Annotation>\n");			
		}
		
		return annotations;
	}

}
