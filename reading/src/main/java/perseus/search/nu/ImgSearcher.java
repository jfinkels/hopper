package perseus.search.nu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import perseus.artarch.image.Img;
import perseus.artarch.image.SimpleImg;
import perseus.artarch.image.dao.HibernateImgDAO;
import perseus.artarch.image.dao.ImgDAO;
import perseus.util.Config;
import perseus.util.Range;

public class ImgSearcher implements Searcher {
	private static Logger logger = Logger.getLogger(ImgSearcher.class);

	public SearchResults<ImgSearchResult> search(String query, Range<Integer> range) {
		ImgSearchResults results = new ImgSearchResults();
		String indexPath =  Config.getSearchIndexPath() + "image";
		int end = range.getEnd();

		try {
			org.apache.lucene.search.Searcher searcher = new IndexSearcher(indexPath);
			QueryParser queryParser = new QueryParser("caption", new SimpleAnalyzer());
			Query q = queryParser.parse(query);
			Hits hits = searcher.search(q);
			
			int totalHits = hits.length();
			results.setTotalHitCount(totalHits);
			
			if (end > totalHits) {
				end = totalHits;
			} else {
				for (int i = 0; i < totalHits; i++) {
					Document imgDoc = hits.doc(i);
					results.addArchiveNumber(imgDoc.get("archiveNumber"));
				}
			}
			
			for (int i = 0; i < end; i++) {
				Document imgDoc = hits.doc(i);
				SimpleImg image = getImg(imgDoc);
				ImgSearchResult result = new ImgSearchResult(image);
				results.add(result);
			}
		} catch (IOException ioe) {
			logger.error(ioe);
		} catch (ParseException e) {
			logger.error(e);
		}
		return results;
	}
	
	private SimpleImg getImg(Document doc) {
		ImgDAO iDAO = new HibernateImgDAO();
		SimpleImg image = (SimpleImg) iDAO.getImg(doc.get("archiveNumber"));
		image.setURLs();
		return image;
	}
	
	public class ImgSearchResults extends SearchResults<ImgSearchResult> {
		List<String> archiveNumbers = new ArrayList<String>();

		public List<String> getArchiveNumbers() {
			return archiveNumbers;
		}

		public void setArchiveNumbers(List<String> archiveNumbers) {
			this.archiveNumbers = archiveNumbers;
		}
		
		public void addArchiveNumber(String archiveNum) {
			archiveNumbers.add(archiveNum);
		}
	}
	
	private class ImgSearchResult extends SearchResult<Img> {
		public ImgSearchResult(Img i) {
			setTitle(i.getArchiveNumber());
			setIdentifier(i.getArchiveNumber());
			setContent(i);
		}
	}

}
