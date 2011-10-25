package perseus.search.nu;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import perseus.document.Query;
import perseus.document.dao.SQLMetadataDAO;
import perseus.util.Range;

public class MetadataSearcher implements Searcher {
	public SearchResults<? extends SearchResult> search(String term, Range<Integer> range) {
		Map<String,Set<Query>> matches = new SQLMetadataDAO().getDocumentsWithTerm(term,
				range.getStart(), range.getEnd()-range.getStart());

		return new MetadataSearchResults(matches);
	}

	public class MetadataSearchResult extends SearchResult<Query> {
		public MetadataSearchResult(Query query) {
			super();

			setContent(query);
			setTitle(query.getMetadata().getTitle());
			setIdentifier(query.toString());
		}
	}

	public class MetadataSearchResults extends SearchResults<MetadataSearchResult> {
		private Map<String,Set<Query>> matchesByField =
			new HashMap<String,Set<Query>>();

		public MetadataSearchResults(Map<String,Set<Query>> matches) {
			matchesByField = matches;

			for (String matchingTerm : matches.keySet()) {
				Set<Query> matchingDocs = matches.get(matchingTerm);
				for (Query doc : matchingDocs) {
					add(new MetadataSearchResult(doc));
				}
			}
		}

		public Set<String> getMatchingFields() {
			return matchesByField.keySet();
		}

		public Set<Query> getMatchesByField(String field) {
			return matchesByField.get(field);
		}
	}
}
