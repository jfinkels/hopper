package perseus.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  ChunkSchemes describe how a document is divided.    
 */
public class ChunkSchemes {

	private HashSet<String> chunkTypes;
	private List<String> schemes;
	private String defaultScheme = null;

	private static String[] defaultChunkTypes = { "chapter", "entry", "letter",
		"section", "poem", "scene",
	"document" };

	private static Pattern LINE_SCHEME = Pattern.compile(":line");

	// Some documents have no specified default, so keep track of
	// the last seen chunk type.
	private String mostRecentType;

	private static Pattern defaultPattern = Pattern.compile("\\*");

	/**
	 * Class Constructor
	 */
	public ChunkSchemes () {
		chunkTypes = new HashSet<String>();
		schemes = new ArrayList<String>();
		mostRecentType = "";
	}

	/**
	 * Returns the number of chunking schemes for this instance of the ChunkSchemes object
	 */
	public int getSchemeCount() {
		return schemes.size();
	}

	public void addScheme(String scheme) {

		// Drop repeats:
		if (schemes.contains(scheme)) {
			return;
		}

		// Special Case:
		//  The "card" chunk is the default chunk for long poems
		//  (ie epic), but it is never listed as a citable section.
		//  Therefore, we need to add it here. The rule is:

		//    For all documents with the "book:line" scheme, add an 
		//    additional "book:card" scheme.

		//  Documents with the "book:line" chunk always have only that
		//  scheme, as far as I can tell. This is implemented 
		//  as a recursive call to this function. It's always with a
		//  different string, though, so I don't think it should cause
		//  an infinite loop.

		if (scheme.equals("line")) {
			addScheme("card");
		}
		else if (scheme.equals("commline")) {
			addScheme("card");
		}
		else {
			Matcher lineMatcher = LINE_SCHEME.matcher(scheme);
			if (lineMatcher.find()) {
				String cardScheme = lineMatcher.replaceAll(":card");
				addScheme(cardScheme);
			}

			/*
	     lineMatcher = COMMLINE_SCHEME.matcher(scheme);
	     if (lineMatcher.find()) {
	     String cardScheme = lineMatcher.replaceAll(":card");
	     addScheme(cardScheme);
	     }
			 */
		}

		// Drop +'s
		scheme = scheme.replaceAll("[+]", "");

		StringTokenizer tokenizer = new StringTokenizer(scheme, ":");

		String token = "";

		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();

			if (token.endsWith("*")) {
				token = token.replaceAll("[*]", "");		
			}

			mostRecentType = token; 	    
			chunkTypes.add(token);
		}

		//scheme = scheme.replaceAll("[\\*]", "");
		schemes.add(scheme);
	}

	public void clearSchemes() {
		schemes.clear();
		chunkTypes.clear();
	}

	public HashSet<String> getValidChunks() {
		return chunkTypes;
	}

	public List<String> getSchemes() {
		return schemes;
	}

	/**
	 * Like getSchemes(), but filters out line-based chunk types.
	 */
	public List<String> getDisplayableSchemes() {
		List<String> dispSchemes = new ArrayList<String>();

		for (String scheme : schemes) {
			if (scheme.equals("line") || scheme.endsWith(":line")) {
				continue;
			}

			dispSchemes.add(scheme);
		}

		return dispSchemes;
	}

	public String getDefaultChunk() {
		// Run through the default chunk types in order.
		// If any of them are valid, return that one.
		for (String defaultCandidate : defaultChunkTypes) {
			if (chunkTypes.contains(defaultCandidate)) {
				return defaultCandidate;
			}
		}

		// None of the default chunk types was present.

		// Special case: Long poems with "card" breaks.
		// card is never explicitly specified as a chunk type.
		if (mostRecentType != null &&
				mostRecentType.equals("line")) {
			return "card";
		}

		// As a last resort, return the what would otherwise be the default.
		return defaultTypeForScheme(getDefaultScheme());
	}


	public String getDefaultScheme() {
		if (defaultScheme != null) {
			return defaultScheme;
		}

		String scheme = null;
		for (String s : schemes) {
			Matcher matcher = defaultPattern.matcher(s);
			if (matcher.find()) {
				scheme = s;
				break;
			}
		}

		if (schemes.size() == 0) {
			return null;
		}

		if (scheme == null) {
			scheme = (String) schemes.get(0);
		}

		if (scheme.equals("line")) {
			return "card";
		}

		Pattern linePattern = Pattern.compile(":line");
		Matcher lineMatcher = linePattern.matcher(scheme);
		if (lineMatcher.find()) {
			scheme = lineMatcher.replaceAll(":card");
		}	

		return scheme;
	}

	public void setDefaultScheme(String scheme) {
		defaultScheme = scheme;
	}

	public String getLargestChunk() {
		String scheme = getDefaultScheme();
		List<String> chunks = getChunks(scheme);
		return (String) chunks.get(0);
	}

	public static List<String> getChunks (String scheme) {
		List<String> chunks = new ArrayList<String>();

		StringTokenizer tokenizer = new StringTokenizer(scheme, ":");

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			token = token.replaceAll("[*]", "");
			chunks.add(token);
		}

		return chunks;
	}

	public static List<String> getNonTrivialChunks(String scheme) {
		List<String> chunks = getChunks(scheme);
		List<String> output = new ArrayList<String>();

		for (int i=0;i<chunks.size();i++) {
			String type = chunks.get(i);

			// Don't bother constructing next-prev links for lines
			// or verses
			if (type.equals("line") ||
					type.equals("verse")) {
				continue;
			}

			// Also don't bother with sections that are within chapters or
			// letters--unless the sections are themselves the parent of
			// another type, as in some of the Civil War documents
			if (type.equals("section")) {
				if (i > 0 && (chunks.get(i-1).equals("chapter"))) {
					// "section" within "chapter" is usually very small, 
					// such as within Tacitus. The one case where it is important
					// is in the grammatical overviews.
					if (chunks.size() > i+1 && chunks.get(i+1).equals("form")) {
						// Special case for Latin grammatical overview
					}
					else if (chunks.size() > i+1 &&
							chunks.get(i+1).equals("subsection")) {
						// Special case for sections that may not be trivially small
					}
					else {
						continue;
					}
				}
				else if (i > 0 && chunks.get(i-1).equals("letter")) {
					// Also ignore sections in letters
					continue;
				}
			}

			output.add(type);
		}

		return output;
	}

	/**
	 * Return the display name for the given chunk type
	 * 
	 * @param type specifies the chunk type
	 * @return the display name
	 */
	public static String getDisplayName(String type) {
		type = type.replaceAll("[*]", "");

		if (type.equals("regyr")) {
			return "regnal year";
		}
		else if (type.equals("ad")) {
			return "year";
		}
		else if (type.equals("smythp")) {
			return "chapter";
		}
		else if (type.equals("alphabetic letter")) {
			return "first letter";
		}
		else if (type.equals("card")) {
			return "line";
		}
		else if (type.equals("commline")) {
			return "line";
		} else {
			return type;
		}
	}

	public boolean containsType(String chunkType) {
		return chunkTypes.contains(chunkType);
	}

	public List<String> schemesWithDefaultChunk() {
		String defaultChunk = getDefaultChunk();
		List<String> matchingSchemes = new ArrayList<String>();

		for (String scheme : getSchemes()) {
			if (scheme.indexOf(defaultChunk) != -1) {
				matchingSchemes.add(scheme);
			}
		}

		return matchingSchemes;
	}

	public static List<String> typesForScheme(String scheme) {
		if (scheme == null) return null;

		List<String> types =
			Arrays.asList(scheme.replaceAll("[*+]", "").split(":"));
		return types;
	}

	public static String defaultTypeForScheme(String scheme) {
		if (scheme == null) return null;

		String[] types = scheme.split(":");
		for (int i = 0; i < types.length; i++) {
			if (types[i].indexOf("*") != -1) {
				return types[i].replaceAll("[*+]", "");
			}
		}

		// If there was no default type specified, just use the last type we
		// saw.
		return types[types.length-1].replaceAll("[+]", "");
	}

	public String getNextTypeForChunk(Chunk chunk) {
		for (String scheme : schemes) {
			if (scheme.contains(chunk.getType())) {
				List<String> types = ChunkSchemes.typesForScheme(scheme);
				int indexOfChunkType = types.indexOf(chunk.getType());
				if (indexOfChunkType == types.size()-1) {
					return null;
				} else {
					return types.get(types.indexOf(chunk.getType())+1);
				}
			}
		}
		return null;
	}
}
