package perseus.chunking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import perseus.document.ABO;
import perseus.document.Abbreviation;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.ie.entity.CivilWarDateParser;
import perseus.ie.entity.Date;
import perseus.ie.entity.DateParser;
import perseus.ie.entity.DateRange;
import perseus.ie.entity.Person;
import perseus.util.ObjectCounter;

public class TEIP4Processor extends XMLEventProcessor {

	Map<String,Stack<String>> qNameTypes = null;
	Map<String,List<String>> subChunkMap = null;

	ObjectCounter typeCounts = null;
	private static Logger logger = Logger.getLogger(TEIP4Processor.class);

	public static final DateParser dateParser = new CivilWarDateParser();

	static final Pattern ABO_MATCH_PATTERN =
		Pattern.compile("(Perseus:abo:\\w+,\\d+,\\d+)");
	
	static final Pattern SEC_MATCH_PATTERN =
		Pattern.compile("(Perseus:abo:sec,\\d+)");
	
	static final Pattern PAP_MATCH_PATTERN =
		Pattern.compile("(Perseus:abo:pap,[\\w \\.]*)");

	static final Pattern PID_MATCH_PATTERN = 
		Pattern.compile("(Perseus:text:\\d+\\.\\d+\\.\\d+)");

	public TEIP4Processor (String documentID, Semaphore chunkSem) {
		super(documentID, chunkSem);

		qNameTypes = new HashMap<String,Stack<String>>();
		typeCounts = new ObjectCounter();

		subChunkMap = new HashMap<String,List<String>>();

		Query documentQuery = new Query(documentID);
		Metadata documentMetadata = documentQuery.getMetadata();

		if (documentMetadata.has(Metadata.SUBDOC_REF_KEY)) {
			List<String> subtextQueries =
				documentMetadata.getList(Metadata.SUBDOC_REF_KEY);

			for (String ref : subtextQueries) {
				Query subtextQuery = new Query(documentID, ref);
				registerSchemes(subtextQuery.getMetadata());
			}
		}

		registerSchemes(documentMetadata);
	}

	public void registerSchemes(Metadata metadata) {

		for (String scheme : metadata.getChunkSchemes().getSchemes()) {
			if (scheme == null) { continue; }

			String[] tokens = scheme.split("[:\\*]+");
			if (tokens.length == 0) { continue; }

			String previousToken = null;
			for (int i=0;i<tokens.length;i++) {
				String token = tokens[i];
				if (previousToken != null) {
					registerSubChunkType(previousToken, token);
				}
				previousToken = token;
			}
		}
	}



	public boolean canTakeHeader(String chunkType) {
		if (chunkType.equalsIgnoreCase("page")) {
			return false;
		}
		return true;
	}

	public Attributes preprocessAttributes(String namespaceURI,
			String localName,
			String qName,
			Attributes attributes) {

		AttributesImpl attImpl = new AttributesImpl(attributes);

		// Remove a couple useless attributes
		int teiFormIndex = attImpl.getIndex("TEIform");
		if (teiFormIndex != -1) attImpl.removeAttribute(teiFormIndex);

		int partIndex = attImpl.getIndex("part");
		if (partIndex != -1) attImpl.removeAttribute(partIndex);

		// If we're looking at a bibl tag that points to something, test whether
		// we have the destination document on our system; if we do, add an
		// appropriate attribute
		if (qName.equalsIgnoreCase("bibl")) {
			String destination = attributes.getValue("n");
			String validLink = attributes.getValue("valid");
			boolean aboMatch = false;

			if (destination != null && validLink == null) {
				boolean isValidLink = false;

				// Chop off the subquery if we can
				// It could be an ABO, like Perseus:abo:tlg,0001,001...
				Matcher matcher = ABO_MATCH_PATTERN.matcher(destination);
				if (matcher.find()) {
					aboMatch = true;
					destination = matcher.group(1);
				} else {
					// Or a secondary source like Perseus:abo:sec,00005...
					matcher = SEC_MATCH_PATTERN.matcher(destination);
					if (matcher.find()) {
						aboMatch = true;
						destination = matcher.group(1);
					} else {
						// Or a papyri abo like Perseus:abo:pap,BGU...
						matcher = PAP_MATCH_PATTERN.matcher(destination);
						if (matcher.find()) {
							aboMatch = true;
							destination = matcher.group(1);
						}
					}
				}
				
				if (aboMatch) {
					// Do we have any documents that match this ABO?
					if (!ABO.getDocuments(destination).isEmpty()) {
						isValidLink = true;
					}
				} 
				// Perseus document id's are valid too
				else if ((matcher = PID_MATCH_PATTERN.matcher(destination)).find()) {
					destination = matcher.group(1);
					isValidLink = true;
				}
				else {
					// or it could be an abbreviation, like "Aeschin. 1 3"
					Abbreviation matchingAbbrev = Abbreviation.find(destination);
					if (matchingAbbrev != null ) {
						isValidLink = true;
						//don't create links to valid abbreviations that we don't currently haves
						Metadata abbrevMetadata = matchingAbbrev.getABO().getMetadata();
						if (!abbrevMetadata.has(Metadata.TITLE_KEY)) {
							isValidLink = false;
						}
					}
				}

				if (isValidLink) {
					attImpl.addAttribute(namespaceURI, "valid", "valid",
							"CDATA", "yes");
				}
			}
		} else if (qName.equalsIgnoreCase("placeName") &&
				attImpl.getValue("key") != null) {
			// The following few cases are all intended to convert the
			// authority names of our entities into a coherent format, as
			// opposed to the existing attributes (like the persName's "reg"
			// attribute, which has other extraneous information that we
			// want to preserve but can't match against). We use the "authname"
			// attribute we create here to highlight entities with a given
			// authority name when displaying texts.
			attImpl.addAttribute(namespaceURI, "authname", "authname",
					"CDATA", attImpl.getValue("key"));
		} else if (qName.equalsIgnoreCase("persName") &&
				attImpl.getValue("reg") != null) {
			String[] regTokens = attImpl.getValue("reg").split(":");
			String[] nameTokens = regTokens[1].split(",");

			Person person = new Person();
			person.addName(Person.SURNAME, nameTokens[0]);
			for (int i = 1; i < nameTokens.length; i++) {
				if (!nameTokens[i].equals("nomatch")) {
					person.addName(Person.FORENAME, nameTokens[i]);
				}
			}

			attImpl.addAttribute(namespaceURI, "authname", "authname",
					"CDATA", person.getAuthorityName());
		} else if (qName.equalsIgnoreCase("date") &&
				attImpl.getValue("value") != null) {
			String dateText = attImpl.getValue("value");
			try {
				Date parsedDate = dateParser.parse(dateText);

				attImpl.addAttribute(namespaceURI, "authname", "authname",
						"CDATA", parsedDate.getAuthorityName());
			} catch (Exception e) {
				logger.warn("Couldn't parse date " +
						attImpl.getValue("value"), e);
			}
		} else if (qName.equalsIgnoreCase("dateRange") &&
				attImpl.getValue("from") != null &&
				attImpl.getValue("to") != null) {

			String fromText = attImpl.getValue("from");
			String toText = attImpl.getValue("to");
			try {
				DateRange parsedRange = new DateRange(
						dateParser.parse(fromText), dateParser.parse(toText));
				attImpl.addAttribute(namespaceURI, "authname", "authname",
						"CDATA", parsedRange.getAuthorityName());
			} catch (Exception e) {
				logger.warn("Couldn't parse date range " +
						attImpl.getValue("from") + " / " +
						attImpl.getValue("to"),  e);
			}
		} else if (qName.equalsIgnoreCase("dateStruct") &&
				attImpl.getValue("value") != null) {
			try {
				Date parsedDate = dateParser.parse(attImpl.getValue("value"));
				attImpl.addAttribute(namespaceURI, "authname", "authname",
						"CDATA", parsedDate.getAuthorityName());
			} catch (Exception e) {
				logger.warn("Couldn't parse dateStruct " +
						attImpl.getValue("value"));
			}
		}

		return attImpl;
	}

	public void processStartTagEvent(String qName, Attributes attrs,
			String tagText, int offset) {

		if (qName.equalsIgnoreCase("div") ||
				qName.equalsIgnoreCase("div0") ||
				qName.equalsIgnoreCase("div1") ||
				qName.equalsIgnoreCase("div2") ||
				qName.equalsIgnoreCase("div3") ||
				qName.equalsIgnoreCase("div4")) {

			String type = attrs.getValue("type");
			if (type == null) {
				logger.warn("missing type for element " + qName);
				type = qName;
			} else {
				String edition = attrs.getValue("ed");
				if (edition != null && !edition.equalsIgnoreCase("p")) {
					type = edition + " " + type;
				}
			}

			setQNameType(qName, type);

			String attributeValue = null;
			if (type.equalsIgnoreCase("entry")) {
				attributeValue = attrs.getValue("id");

				// attr "n" also shows up for some entries, so try it too
				if (attributeValue == null) {
					attributeValue = attrs.getValue("n");
				}
			}
			else {
				attributeValue = attrs.getValue("n");
			}

			String id = attrs.getValue("id");

			// figure out the chunk query value
			String value = calculateValue(attributeValue, type);

			processStartChunkEvent(type, value, id, offset);
		}
		else if (qName.equalsIgnoreCase("titlePage")) {
			String type = "titlePage";
			// We expect title pages will not have any n or id attribute...
			String value = calculateValue(null, type);

			processStartChunkEvent(type, value, null, offset);
		}
		else if (qName.equalsIgnoreCase("text")) {

			// We don't want to process the <text> element that wraps
			// a single text in a monograph. These will always have 
			// no type, and no n attribute.
			if (attrs.getValue("n") != null) {

				String type = "text";
				String value = calculateValue(attrs.getValue("n"), type);
				String id = attrs.getValue("id");

				processStartChunkEvent(type, value, id, offset);
			}
		}
		else if (qName.equalsIgnoreCase("entry") ||
				qName.equalsIgnoreCase("entryFree")) {
			String type = "entry";
			String value = calculateValue(attrs.getValue("key"), type);
			String id = attrs.getValue("id");

			// For entries, an entry-key should be a perfectly acceptable
			// substitute for an actual id
			if (id == null) {
				id = value;
			}

			processStartChunkEvent(type, value, id, offset);
		}
		else if (qName.equalsIgnoreCase("l")) {
			String type = "line";
			String value = calculateValue(attrs.getValue("n"), type);
			String id = attrs.getValue("id");

			// Open this chunk
			processStartChunkEvent(type, value, id, offset);
		}
		else if (qName.equalsIgnoreCase("milestone")) {
			String type = attrs.getValue("unit");
			String edition = attrs.getValue("ed");

			if (edition != null && !edition.equalsIgnoreCase("p")) {
				type = edition + " " + type;
			}
			String value = calculateValue(attrs.getValue("n"), type);
			String id = attrs.getValue("id");

			// Close any hanging elements of this type
			processEndChunkEvent(type, offset);

			// Open this chunk
			processStartChunkEvent(type, value, id, offset);
		}
		else if (qName.equalsIgnoreCase("pb")) {
			String type = "page";
			String edition = attrs.getValue("ed");
			if (edition != null && !edition.equalsIgnoreCase("p")) {
				type = edition + " " + type;
			}

			String value = calculateValue(attrs.getValue("n"), type);
			String id = attrs.getValue("id");

			// Close any hanging elements of this type
			processEndChunkEvent(type, offset);

			// Open this chunk
			processStartChunkEvent(type, value, id, offset);
		}
		else if (qName.equalsIgnoreCase("lb")) {
			String type = "line";
			String edition = attrs.getValue("ed");
			if (edition != null && !edition.equalsIgnoreCase("p")) {
				type = edition + " " + type;
			}

			String value = calculateValue(attrs.getValue("n"), type);
			String id = attrs.getValue("id");

			// Close any hanging elements of this type
			processEndChunkEvent(type, offset);

			// Open this chunk
			processStartChunkEvent(type, value, id, offset);
		}
		else if (qName.equalsIgnoreCase("head") ||
				qName.equalsIgnoreCase("orth") ||
				qName.equalsIgnoreCase("opener")) {
			processStartHeaderEvent(attrs.getValue("lang"));
		}
		else if (qName.equalsIgnoreCase("table")) {
			startSuppressingOutput();
		}
		else if (qName.equalsIgnoreCase("figure")) {
			startSuppressingOutput();
		}

		super.processStartTagEvent(qName, attrs, tagText, offset);

	}

	public void processEndTagEvent(String qName, int offset) {
		super.processEndTagEvent(qName, offset);

		if (qName.equalsIgnoreCase("div") ||
				qName.equalsIgnoreCase("div0") ||
				qName.equalsIgnoreCase("div1") ||
				qName.equalsIgnoreCase("div2") ||
				qName.equalsIgnoreCase("div3") ||
				qName.equalsIgnoreCase("div4")) {

			processEndChunkEvent(getQNameType(qName), offset);
		}
		else if (qName.equalsIgnoreCase("titlePage")) {
			processEndChunkEvent("titlePage", offset);
		}
		else if (qName.equalsIgnoreCase("text")) {
			processEndChunkEvent("text", offset);	    
		}
		else if (qName.equalsIgnoreCase("entry") ||
				qName.equalsIgnoreCase("entryFree")) {
			processEndChunkEvent("entry", offset);
		}
		else if (qName.equalsIgnoreCase("l")) {
			processEndChunkEvent("line", offset);
		}
		else if (qName.equalsIgnoreCase("head")) {
			processEndHeaderEvent();
		}
		else if (qName.equalsIgnoreCase("orth")) {
			processEndHeaderEvent();
		}
		else if (qName.equalsIgnoreCase("opener")) {
			processEndHeaderEvent();
		}
		else if (qName.equalsIgnoreCase("table")) {
			stopSuppressingOutput();
		}
		else if (qName.equalsIgnoreCase("figure")) {
			stopSuppressingOutput();
		}
	}

	public void processEndChunkEvent(String type, int offset) {
		// Handle implicitly closed milestone tags:
		//  if the scheme is book:chapter, the end of a book implies the
		//  end of a chapter.

		List subChunkTypes = getSubChunkTypes(type);
		if (subChunkTypes != null) {
			Iterator subChunkIterator = subChunkTypes.iterator();
			while (subChunkIterator.hasNext()) {
				String subChunkType = (String) subChunkIterator.next();
				// reset the counter, but *not* for entries; since entry keys
				// are also used as ids, having two chunks with "entry=1"
				// will mean that only the first one will ever be accessible
				// by, e.g., ChunkFactories or top-level queries; thus, keep
				// on counting

				if (!subChunkType.equals("entry")) {
					typeCounts.reset(subChunkType);
				}
				// recursively propagate the end chunk event
				processEndChunkEvent(subChunkType, offset);
			}
		}

		super.processEndChunkEvent(type, offset);
	}

	private void setQNameType(String qName, String type) {
		Stack<String> types = null;
		if (qNameTypes.containsKey(qName)) {
			types = (Stack<String>) qNameTypes.get(qName);
		}
		else {
			types = new Stack<String>();
			qNameTypes.put(qName, types);
		}
		types.push(type);
	}

	private String getQNameType(String qName) {
		if (qNameTypes.containsKey(qName)) {
			Stack types = (Stack) qNameTypes.get(qName);
			if (types.size() > 0) {
				return (String) types.pop();
			}
		}
		return "UNKNOWN"; // Will this ever happen?
	}

	private String calculateValue(String declaredValue, String type) {
		typeCounts.count(type);

		if (declaredValue != null && 
				! declaredValue.equals("")) {
			return declaredValue;
		}

		// There is no declared value, so return the order of this chunk
		return "" + typeCounts.getCount(type);
	}

	private void registerSubChunkType (String parent, String child) {
		logger.debug(parent + " -> " + child);

		List<String> subChunks = null;
		if (subChunkMap.containsKey(parent)) {
			subChunks = subChunkMap.get(parent);
		}
		else {
			subChunks = new ArrayList<String>();
			subChunkMap.put(parent, subChunks);
		}

		subChunks.add(child);
	}

	private List<String> getSubChunkTypes (String parent) {
		if (parent != null) {
			parent = parent.toLowerCase();
			if (subChunkMap.containsKey(parent)) {
				return subChunkMap.get(parent);
			}
		}
		return null;
	}

}
