package perseus.morph;
/**
 * @MPL@
 * Last Modified: @TIME@
 *
 * Author: @gweave01@
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.jdom.Content;
import org.jdom.Element;
import org.jdom.filter.ContentFilter;
import org.jdom.filter.Filter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import perseus.document.Chunk;
import perseus.document.Query;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateChunkDAO.ChunkRow;
import perseus.ie.entity.Entity;
import perseus.ie.entity.EntityManager;
import perseus.ie.freq.Frequency;
import perseus.language.Language;
import perseus.voting.Vote;

/**
 * Implementation of a simple lemma
 */
public class Lemma implements Entity {

	/* Entity specific properties */
	private String authorityName;
	private String displayName;
	private Integer id;
	private String sortableString;
	private Long maxOccurrenceCount = new Long(0);
	private Long minOccurrenceCount = new Long(0);
	private Integer documentCount = new Integer(0);
	private Double inverseDocumentFrequency = new Double(0.0);
	private Set<ChunkRow> lexiconChunks = new HashSet<ChunkRow>();
	private Set<Parse> parses = new HashSet<Parse>();

	/* Lemma specific properties */
	private String headword;
	private String bareHeadword;
	private Integer sequenceNumber;
	private Language language;
	private String shortDefinition;
	
	/*
	 * Used for converting word to bare words (i.e., forms without
	 * diacritics), which can then be used as another option for searching.
	 * Used by LexiconEntryLoader, ParseLoader, MorphController
	 */
	public static final Pattern BARE_WORD_PATTERN = Pattern.compile("[()\\\\/*=|+']");
	
    /**
     * Attempts to extract a short definition for the current lemma. This
     * usually consists of the first TR or GLOSS element in the chunk--or, if
     * there are no such elements, the first HI element that doesn't contain
     * noise.
     *
     * @param chunkElement the element containing the lexicon entry
     * @return a short definition for the entry
     */
    public static String extractDefinition(org.jdom.Document chunkElement) {
    	// First, look for actual semantic elements
    	Iterator descendantIt = chunkElement.getDescendants(DEFINITION_FILTER);

    	// If we didn't find any (we won't for the Lewis and Short),
    	// check for HI tags.
    	if (!descendantIt.hasNext()) {
    		descendantIt = chunkElement.getDescendants(HIGHLIGHT_FILTER);
    	}

    	if (descendantIt.hasNext()) {
    		Content definitionElement = (Content) descendantIt.next();
    		return definitionElement.getValue();
    	}

    	return null;
    }
	
	public static final Filter DEFINITION_FILTER =
        new ContentFilter(ContentFilter.ELEMENT) {
            private Set<String> elementNames = new HashSet<String>() {{
                add("tr");
                add("gloss");
            }};
	    
            public boolean matches(Object obj) {
                if (!super.matches(obj)) return false;
		
                Element element = (Element) obj;
                return (elementNames.contains(element.getName().toLowerCase()));
            }
        };
    
    // The Lewis and Short does not mark semantic distinctions for italics;
    // both definitions and grammatical abbreviations like the ones below are
    // marked as HI tags. Accept such tags for short definitions, but try to
    // filter out as much of the noise as we can.
    public static final Filter HIGHLIGHT_FILTER = 
        new ContentFilter(ContentFilter.ELEMENT) {
            private Set<String> noisyValues = new HashSet<String>(){{
                add("init.");
                add("fin.");
                add("perf.");
                add("v. inch. n.");
                add("a.");
                add("adj. dim.");
                add("P. a. fin.");
                add("v. dep. n.");
                add("gen. plur.");
                add("Act.");
                add("gen.");
                add("v. dep. a.");
                add("inf.");
                add("v. freq. n.");
                add("masc.");
                add("Pass.");
                add("conj.");
                add("gen. sing.");
                add("v. freq.");
                add("advv.");
                add("Neutr.");
                add("sup.");
                add("v. inch.");
                add("nom. sing.");
                add("N. cr.");
                add("P. a. fin");
                add("dep.");
                add("fut.");
                add("perf. subj.");
                add("v. impers.");
                add("fem.");
            }};
	    
            public boolean matches(Object obj) {
                if (!super.matches(obj)) return false;
		
                Element element = (Element) obj;
                return (element.getName().equalsIgnoreCase("hi") &&
                        !noisyValues.contains(element.getValue()));
            }
        };

	public Lemma() {}

	private Set<Vote> votes;

	public Set<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Set<Vote> votes) {
		this.votes = votes;
	}

	private Set<Frequency> frequencies = new HashSet<Frequency>();

	public Set<Frequency> getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(Set<Frequency> frequencies) {
		this.frequencies = frequencies;
	}

	/**
	 * Class constructor
	 * If you want a persisted object, it is better to use the Lemma DAO
	 */
	public Lemma (int lemmaID, String lemma, int sequenceNumber,
			String languageCode, String shortDefinition) {
		this.id = new Integer(lemmaID);
		this.headword = lemma;
		this.sequenceNumber = new Integer(sequenceNumber);
		this.language = Language.forCode(languageCode);
		this.shortDefinition = shortDefinition;
		this.authorityName = lemma + sequenceNumber + "(" + languageCode + ")";
		this.displayName = this.authorityName;
	}

	public Lemma (String authorityName, String lemma, int sequenceNumber,
			String languageCode, String shortDefinition) {
		this.headword = lemma;
		this.sequenceNumber = new Integer(sequenceNumber);
		this.language = Language.forCode(languageCode);
		this.shortDefinition = shortDefinition;
		this.authorityName = authorityName  + "(" + languageCode + ")";
		this.displayName = this.authorityName;
	}

	public String getLemma() {
		return getHeadword();
	}

	public void setLemma(String l) {
		setHeadword(l);
	}

	public Set<ChunkRow> getLexiconChunks() {
		return lexiconChunks;
	}

	public void setLexiconChunks(Set<ChunkRow> lexiconChunks) {
		this.lexiconChunks = lexiconChunks;
	}

	public void addLexiconChunk(Chunk chunk) {
		lexiconChunks.add(HibernateChunkDAO.chunkToRow(chunk));
	}
	public Set<Query> getLexiconQueries() {
		Set<Query> queries = new TreeSet<Query>();
		for (ChunkRow chunk : lexiconChunks) {
			Query query = new Query(chunk.getDocumentID())
			.appendSubquery(chunk.getType(), chunk.getValue());
			queries.add(query);
		}

		return queries;
	}


	public Query getLexiconQuery() {
		for (Query query : getLexiconQueries()) {
			return query;
		}
		return null;
	}

	/**
	 * Returns a lexicon query for this lemma associated with a specific
	 * document. Useful for finding occurrences of a particular lemma in a
	 * given lexicon.
	 *
	 * @param documentID the ID of the target lexicon
	 * @return a Query yielding the desired lexicon entry, or null if no
	 *	query could be found
	 */
	public Query getLexiconQuery(String documentID) {
		// This ought to be reworked to use a Map eventually, but for the
		// moment searching through a List won't slow things down *that* much.
		for (Query query : getLexiconQueries()) {
			if (documentID.equals(query.getDocumentID())) {
				return query;
			}
		}

		return null;
	}

	/**
	 * Retrieve all lexicon entries for the given lemma. An alias for
	 * getLexiconChunks() that convers the result into a more familiar
	 * type.
	 *
	 * @return a list of lexicon entries
	 */
	public Set<Chunk> getLexiconEntries() {
		Set<Chunk> chunks = new HashSet<Chunk>();
		for (ChunkRow row : lexiconChunks) {
			chunks.add(row.toChunk());
		}

		return chunks;
	}

	/**
	 * Retrieve the chunk for the lexicon entry for this
	 * lemma instance
	 *
	 * @return Chunk
	 */
	public Chunk getLexiconEntry() {
		for (ChunkRow row : lexiconChunks) {
			return row.toChunk();
		}
		return null;
	}
	/* Accessor Methods for Entity */

	public String getAuthorityName() {
		if (authorityName == null) {
			int sequenceNumber = (getSequenceNumber() != null) ?
					getSequenceNumber() : 1;

					authorityName = String.format("%s%d(%s)",
							getHeadword(), sequenceNumber, getLanguage().getCode());
		}
		return authorityName;
	}

	public void setAuthorityName(String an) {
		authorityName = an;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String dn) {
		displayName = dn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getLemmaID() {
		return this.id.intValue();
	}

	public String getSortableString() {
		return sortableString;
	}

	public void setSortableString(String ss) {
		sortableString = ss;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Entity)) {
			return false;
		}

		Entity e = (Entity) o;

		return getAuthorityName().equals(e.getAuthorityName());
	}

	public int hashCode() {
		return getAuthorityName().hashCode();
	}

	public int compareTo(Entity e) {
		return getSortableString().compareTo(e.getSortableString());
	}

	// By default, we really don't care what's going to happen to us.
	public void willBeRegistered(EntityManager manager) {}
	public void willBeUnregistered(EntityManager manager) {}

	public Long getMaxOccurrenceCount() { return maxOccurrenceCount; }
	public void setMaxOccurrenceCount(Long to) { maxOccurrenceCount = to; }

	public Long getMinOccurrenceCount() { return minOccurrenceCount; }
	public void setMinOccurrenceCount(Long to) { minOccurrenceCount = to; }

	// Accessor Methods for Lemma
	public String getHeadword() {
		return this.headword;
	}

	public void setHeadword(String headword) {
		this.headword = headword;
	}

	public String getBareHeadword() {
		return bareHeadword;
	}

	public void setBareHeadword(String bareHeadword) {
		this.bareHeadword = bareHeadword;
	}

	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getDisplayForm() {
		return getHeadword() +
		(getSequenceNumber() > 1 ? getSequenceNumber() : "");
	}

	public void setSequenceNumber(int sn) {
		sequenceNumber = new Integer(sn);
	}

	public Integer getLanguageID() {
		return language.getId();
	}

	public void setLanguageID (Integer languageID) {
		this.language = Language.forId(languageID);
	}

	public String getLanguageCode() {
		return language.getCode();
	}

	public void setLanguageCode(String languageCode) {
		this.language = Language.forCode(languageCode);
	}

	public String getShortDefinition() {
		return this.shortDefinition;
	}

	public void setShortDefinition(String shortDefinition) {
		this.shortDefinition = shortDefinition;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		final String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName() + " Object {");
		result.append(newLine);

		result.append("  Id: ");
		result.append(this.id);
		result.append(newLine);

		result.append("  authorityName: ");
		result.append(this.authorityName);
		result.append(newLine);

		result.append("  headword: ");
		result.append(this.headword);
		result.append(newLine);

		result.append("  sequenceNumber: ");
		result.append(this.sequenceNumber);
		result.append(newLine);

		result.append("  language: ");
		result.append(this.language);
		result.append(newLine);

		result.append("  shortDefinition: ");
		result.append(this.shortDefinition);
		result.append(newLine);

		result.append("}");
		return result.toString();
	}
	
	public Element toXMLElement() {
		Element lemma = new Element("lemma");
		lemma.setAttribute("lang", language.getCode());
		
		lemma.addContent(new Element("headword").addContent(headword));
		
		Element shortDef = new Element("shortDefinition");
		if (shortDefinition != null) {
			shortDef.addContent(shortDefinition);
		} else {
			shortDef.addContent("[unavailable]");
		}
		lemma.addContent(shortDef);
		
		Set<Query> lexQueries = getLexiconQueries();
		if (!lexQueries.isEmpty()) {
			Element lexiconQueries = new Element("lexiconQueries");
			for (Query query : lexQueries) {
				Element queryElem = new Element("query");
				queryElem.setAttribute("ref", query.toString());
				queryElem.setAttribute("name", query.getMetadata().getAlternativeTitle());
				lexiconQueries.addContent(queryElem);
			}
			lemma.addContent(lexiconQueries);
		}

		return lemma;
	}

	public String toXML() {
		return new XMLOutputter(Format.getPrettyFormat()).outputString(toXMLElement());
	}

	public Integer getDocumentCount() {
		return documentCount;
	}

	public void setDocumentCount(Integer documentCount) {
		this.documentCount = documentCount;
	}

	public Double getInverseDocumentFrequency() {
		return inverseDocumentFrequency;
	}

	public void setInverseDocumentFrequency(Double inverseDocumentFrequency) {
		this.inverseDocumentFrequency = inverseDocumentFrequency;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Set<Parse> getParses() {
		return parses;
	}

	public void setParses(Set<Parse> parses) {
		this.parses = parses;
	}
}
