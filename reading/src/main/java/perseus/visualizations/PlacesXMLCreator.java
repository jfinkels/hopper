package perseus.visualizations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import perseus.document.Chunk;
import perseus.document.DOMOffsetAdder;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.ie.entity.Entity;
import perseus.ie.entity.HibernateEntityManager;
import perseus.ie.entity.Place;
import perseus.ie.entity.EntityExtractor.Adapter;
import perseus.util.Config;

/**
 * Creates the XML files, used by the Google Maps API,
 * for each collection and text that has places tagged.
 * Extracts places using perseus.ie.EntityExtractor.
 * 
 * @author rsingh04
 *
 */
public class PlacesXMLCreator extends RecentlyModifiedCorpusProcessor {

	private static final Logger logger = Logger.getLogger(PlacesXMLCreator.class);
	
	private Map<String,Integer> textPlaceOccurrences;
	private Map<String,Integer> collectionPlaceOccurrences;
	private Map<String,Integer> allPlaceOccurrences;
	
	// texts are sometimes duplicated in different collections, don't process them more than once
	private Set<String> textsProcessed;
	
    static HibernateEntityManager manager = new HibernateEntityManager();
    private final static int maxNumPlaces = 200;
    
    public PlacesXMLCreator() {
    	textPlaceOccurrences = new HashMap<String,Integer>();
    	collectionPlaceOccurrences = new HashMap<String,Integer>();
    	allPlaceOccurrences = new HashMap<String,Integer>();
    	textsProcessed = new HashSet<String>();
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlacesXMLCreator pxc = new PlacesXMLCreator();
		pxc.setIgnoreModificationDate(true);
	
		pxc.create();
	}

	private void create() {
		String[] collections = Config.getPrimaryCollections();
		for (String collection : collections) {
			logger.info("Processing collection: "+collection);
			clearCollectionMap();
			processCollection(collection);
            topPlacesToXML(collection);
            mergeOccurrences();
		}
		logger.info("Processing all place occurrences");
		topPlacesToXML(null);
	}
	
	private void mergeOccurrences() {
		for (String p : collectionPlaceOccurrences.keySet()) {
			int count = collectionPlaceOccurrences.get(p);
			if (allPlaceOccurrences.containsKey(p)) {
				count += allPlaceOccurrences.get(p); 
			}
			allPlaceOccurrences.put(p, count);
		}
	}

	private void clearCollectionMap() {
		collectionPlaceOccurrences.clear();
	}

	private void topPlacesToXML(String pid) {		
		List<Entry<String,Integer>> placeList;
		if (pid == null) {
			placeList = new ArrayList<Entry<String,Integer>>(allPlaceOccurrences.entrySet());
		} else if (pid.contains("collection")) {
			placeList = new ArrayList<Entry<String,Integer>>(collectionPlaceOccurrences.entrySet());
		} else {
			placeList = new ArrayList<Entry<String,Integer>>(textPlaceOccurrences.entrySet());
		}
		
		Collections.sort(placeList, new Comparator<Entry<String,Integer>>(){
			public int compare(Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
				return (entry1.getValue().equals(entry2.getValue()) ? 0 : (entry1.getValue() > entry2.getValue() ? -1 : 1));
			}
		});
		
		if (placeList.isEmpty()) {
			return;
		}
		StringBuffer output = new StringBuffer();
		output.append("<markers>\n");
		for (int i = 0; i < placeList.size() && i < maxNumPlaces; i++) {
			Entry<String,Integer> placeEntry = placeList.get(i);
			Place p = (Place) manager.getEntityByAuthName(placeEntry.getKey());
			output.append(createMarker(p));
		}
		output.append("</markers>");
		
		String filePath = Config.getStaticPath()+"xml/";
		String fileName = "";
		if (pid != null) {
			fileName = pid + ".";
		}
		fileName += "places.xml";
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
            if (e != null && (((Place) e).getLatitude() != 0 && ((Place) e).getLongitude() != 0)) {
            	int collectionCount = 1;
            	int textCount = 1;
            	if (collectionPlaceOccurrences.containsKey(e.getAuthorityName())) {
            		collectionCount += collectionPlaceOccurrences.get(e.getAuthorityName());
            	}
            	if (textPlaceOccurrences.containsKey(e.getAuthorityName())) {
            		textCount += textPlaceOccurrences.get(e.getAuthorityName());
            	}
            	collectionPlaceOccurrences.put(e.getAuthorityName(), collectionCount);
            	textPlaceOccurrences.put(e.getAuthorityName(), textCount);
            } 
        }   
	}
	
	public void startDocument(Chunk documentChunk) {
		String query = documentChunk.getQuery().toString();
		logger.info("Processing "+query);
		textsProcessed.add(query);
		textPlaceOccurrences.clear();
	}
	
	public boolean shouldProcessDocument(Chunk documentChunk) {
		return (super.shouldProcessDocument(documentChunk) && !textsProcessed.contains(documentChunk.getQuery().toString()));
	}
	
	public void endDocument(Chunk documentChunk) {
		super.endDocument(documentChunk);
		topPlacesToXML(documentChunk.getDocumentID());
	}

	private static StringBuffer createMarker(Place p) {
		StringBuffer marker = new StringBuffer();
		
		marker.append("<marker ").append("lat=\"").append(p.getLatitude()).append("\" ")
		.append("lon=\"").append(p.getLongitude()).append("\" ")
		.append("site=\"").append(p.getDisplayName()).append("\"/>\n");
		
		return marker;
	}

}
