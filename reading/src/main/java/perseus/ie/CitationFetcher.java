/**
 * Class for retrieving Citations to and from a given query, shielding the
 * caller from the lower-level data-access objects. Returns matching
 * citations as objects or as an XML string.
 */
package perseus.ie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import perseus.document.Query;
import perseus.ie.dao.CitationDAO;
import perseus.ie.dao.HibernateCitationDAO;

public class CitationFetcher {

	/** The query for which we want citations. */
	private Query query;

	/** The Data Access Object that does the actual fetching. */
	private CitationDAO dao = new HibernateCitationDAO();

	/** All the citations originating in other documents and leading to this
	 * fetcher's Query. */
	private List<Citation> incomingCitations;

	/** All the citations leading to other documents and originating in this
	 * fetcher's Query. */
	private List<Citation> outgoingCitations;

	private static Comparator citationComparator = new Comparator() {
		public int compare(Object a, Object b) {
			Citation c1 = (Citation) a;
			Citation c2 = (Citation) b;

			int sourceResult = c1.getSource().compareTo(c2.getSource());
			if (sourceResult == 0) {
				return c1.getResolvedDestination().compareTo(
						c2.getResolvedDestination());
			}
			return sourceResult;
		}
	};

	/**
	 * Private constructor--use the factory method instead.
	 *
	 * @param query the query to search for
	 */
	private CitationFetcher(Query query) {
		this.query = query;
	}

	/**
	 * Returns a CitationFetcher to find citations from the given query. (A
	 * factory method because eventually, say, we may want to do something
	 * involving caching or something else nefarious.)
	 *
	 * @param query the query to search for
	 */
	public static CitationFetcher forQuery(Query query) {
		return new CitationFetcher(query);
	}

	/**
	 * Returns all citations <em>to</em> this query.
	 */
	public List<Citation> getIncomingCitations() {
		if (incomingCitations == null) {
			incomingCitations = dao.getIncomingCitations(query, null, -1, -1);
			Collections.sort(incomingCitations, citationComparator);
		}
		return incomingCitations;
	}

	/**
	 * Returns all citations <em>from</em> this query.
	 */
	public List<Citation> getOutgoingCitations() {
		if (outgoingCitations == null) {
			outgoingCitations = dao.getOutgoingCitations(query, null, -1, -1);
			Collections.sort(outgoingCitations, citationComparator);
		}
		return outgoingCitations;
	}

	/**
	 * Returns the total number of citations <em>to</em> this chunk.
	 */
	public int incomingCount() {
		getIncomingCitations();
		return incomingCitations.size();
	}

	/**
	 * Returns the total number of citations <em>from</em> this chunk.
	 */
	public int outgoingCount() {
		getOutgoingCitations();
		return outgoingCitations.size();
	}

	/**
	 * Returns the total number of citations to or from this chunk.
	 */
	public int totalCount() {
		return incomingCount() + outgoingCount();
	}

	/**
	 * Returns a list of the citations located as an XML string.
	 */
	public String toXML() {
		StringBuffer output = new StringBuffer();
		output.append("<citations>\n")
		.append("<incoming>\n")
		.append(listToXML(getIncomingCitations()))
		.append("</incoming>\n")
		.append("<outgoing>\n")
		.append(listToXML(getOutgoingCitations()))
		.append("</outgoing>\n")
		.append("</citations>");
		return output.toString();
	}

	private String listToXML(List<Citation> cits) {
		StringBuffer output = new StringBuffer();
		for (Citation cit : cits) {
			output.append(cit.toXML());
		}
		return output.toString();
	}
}
