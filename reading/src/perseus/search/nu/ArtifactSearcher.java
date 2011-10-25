package perseus.search.nu;

import static org.apache.lucene.search.BooleanClause.Occur.MUST;
import static org.apache.lucene.search.BooleanClause.Occur.SHOULD;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;

import perseus.artarch.Artifact;
import perseus.artarch.ArtifactIndexer;
import perseus.artarch.BuildingArtifact;
import perseus.artarch.CoinArtifact;
import perseus.artarch.GemArtifact;
import perseus.artarch.SculptureArtifact;
import perseus.artarch.SiteArtifact;
import perseus.artarch.VaseArtifact;
import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.util.Config;
import perseus.util.Range;

public class ArtifactSearcher implements Searcher {
	private static Logger logger = Logger.getLogger(ArtifactSearcher.class);

	private List<String> artifactTypes = null;

	public ArtifactSearcher() {
		setArtifactTypes(null);
	}
	
	public ArtifactSearcher(List<String> artifactTypes) {
		setArtifactTypes(artifactTypes);
	}

	public List<String> getArtifactTypes() {
		return artifactTypes;
	}

	public void setArtifactTypes(List<String> artifactTypes) {
		if (artifactTypes == null || artifactTypes.size() == 0) {
			this.artifactTypes = Arrays.asList(Config.artifactTypes);
		} else {
			this.artifactTypes = artifactTypes;
		}
	}

	public SearchResults<ArtifactSearchResult> search(String query, Range<Integer> range) {
		ArtifactSearchResults results = new ArtifactSearchResults();
		String indexPath = Config.getSearchIndexPath() + "artifact";
		QueryWrapperFilter filter = new QueryWrapperFilter(filterByArtifactType(this.artifactTypes));
		int end = range.getEnd();
		try {
			IndexSearcher searcher = new IndexSearcher(indexPath);
			QueryParser queryParser = new QueryParser(ArtifactIndexer.CONTENTS, new SimpleAnalyzer());
			BooleanQuery mainQuery = new BooleanQuery();
			mainQuery.add(queryParser.parse(query), MUST);

			// Get counts for each artifact type
			for (String artType : Config.artifactTypes) {
				QueryWrapperFilter fil = new QueryWrapperFilter(filterByArtifactType(Arrays.asList(artType)));
				results.setArtifactCount(artType, searcher.search(mainQuery, fil).length());
			}
			Hits hits = searcher.search(mainQuery, filter, new Sort(new String[] {ArtifactIndexer.TYPE, ArtifactIndexer.NAME}));

			int totalHits = hits.length();
			results.setTotalHitCount(totalHits);

			if (end > totalHits) {
				end = totalHits;
			}

			for (int i = range.getStart(); i < end; i++) {
				Document artifactDoc = hits.doc(i);
				Artifact artifact = getArtifact(artifactDoc);
				ArtifactSearchResult result = new ArtifactSearchResult(artifact);
				results.add(result);
			}
		} catch (IOException ioe) {
			logger.error(ioe);
		} catch (ParseException e) {
			logger.error(e);
		}
		return results;
	}

	private Query filterByArtifactType(List<String> artifactTypes) {
		BooleanQuery query = new BooleanQuery();
		for (String artifactType : artifactTypes) {
			query.add(new TermQuery(new Term(ArtifactIndexer.TYPE, artifactType)), SHOULD);
		}
		return query;
	}

	private Artifact getArtifact(Document doc) {
		ArtifactDAO aDAO = new HibernateArtifactDAO();
		Artifact result = null;
		if (doc.get(ArtifactIndexer.TYPE).equals("Building")) {
			result = (BuildingArtifact) aDAO.findArtifact(doc.get(ArtifactIndexer.NAME), doc.get(ArtifactIndexer.TYPE));
		} else if (doc.get(ArtifactIndexer.TYPE).equals("Coin")) {
			result = (CoinArtifact) aDAO.findArtifact(doc.get(ArtifactIndexer.NAME), doc.get(ArtifactIndexer.TYPE));
		} else if (doc.get(ArtifactIndexer.TYPE).equals("Gem")) {
			result = (GemArtifact) aDAO.findArtifact(doc.get(ArtifactIndexer.NAME), doc.get(ArtifactIndexer.TYPE));
		} else if (doc.get(ArtifactIndexer.TYPE).equals("Sculpture")) {
			result = (SculptureArtifact) aDAO.findArtifact(doc.get(ArtifactIndexer.NAME), doc.get(ArtifactIndexer.TYPE));
		} else if (doc.get(ArtifactIndexer.TYPE).equals("Site")) {
			result = (SiteArtifact) aDAO.findArtifact(doc.get(ArtifactIndexer.NAME), doc.get(ArtifactIndexer.TYPE));
		} else if (doc.get(ArtifactIndexer.TYPE).equals("Vase")) {
			result = (VaseArtifact) aDAO.findArtifact(doc.get(ArtifactIndexer.NAME), doc.get(ArtifactIndexer.TYPE));
		}
		return result;
	}

	public class ArtifactSearchResults extends SearchResults<ArtifactSearchResult> {
		Map<String, Integer> artifactCounts = new TreeMap<String, Integer>();

		public Map<String, Integer> getArtifactCounts() {
			return artifactCounts;
		}

		public void setArtifactCounts(Map<String, Integer> artifactCounts) {
			this.artifactCounts = artifactCounts;
		}

		public void setArtifactCount(String artifactType, int count) {
			artifactCounts.put(artifactType, count);
		}
	}

	public class ArtifactSearchResult extends SearchResult<Artifact> {		
		public ArtifactSearchResult(Artifact a) {
			setTitle(a.getDisplayName());
			setIdentifier(a.getAuthorityName());
			setContent(a);
		}		
	}

}
