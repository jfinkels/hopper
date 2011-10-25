/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
 */
package perseus.artarch;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import perseus.util.Config;

/**
 * ArtifactImgIndexer is a class used to index Artifacts of a given ArtifactType using Lucene
 */
public class ArtifactImgIndexer {
	
	private static Logger logger = Logger.getLogger(ArtifactImgIndexer.class);

	static final Pattern IMAGE_MATCH_PATTERN = Pattern.compile("((ArtifactImg)|(Images)).+\\.xml");

	private IndexWriter indexWriter;
	
	/**
	 * Requires 0 arguments
	 */
	public static void main(String[] args) {
		if (args.length  != 0) {
			logger.error("ArtifactImgIndexer Usage: ArtifactImgIndexer");
			System.exit(0);
		}
		ArtifactImgIndexer ai = new ArtifactImgIndexer();
		ai.indexArtifactImgs();
		ai.optimize();
	}

	private void optimize() {
		logger.info("optimizing indexes...");
		try {
			indexWriter.optimize();
			indexWriter.close();		
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArtifactImgIndexer() {
		try {
			this.indexWriter = new IndexWriter(Config.getSearchIndexPath() + "image", new SimpleAnalyzer(), true);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
		}
	}

	public void indexArtifactImgs() {
		File dataPath = new File(Config.getDataPath());
		File[] dataFiles = dataPath.listFiles();
		Matcher matcher;
		for (File f : dataFiles) {
			matcher = IMAGE_MATCH_PATTERN.matcher(f.getName());
			if (matcher.find()) {
				logger.info("Indexing "+f.getAbsolutePath());
				try {
					SAXBuilder sb = new SAXBuilder();
					org.jdom.Document doc = sb.build(f);
					Element root = doc.getRootElement();
					List rows = root.getChildren();
					Iterator i = rows.iterator();
					while (i.hasNext()) {
						createLuceneDoc((Element) i.next());
					}
				} catch(IOException e) {
					logger.error("Failed to read file: " + f.getAbsolutePath() + ". " + e);
				} catch (JDOMException e) {
					logger.error("Error building XML file "+e);
				}			
			}
		}
	}

	private void createLuceneDoc(Element image) {
		Document luceneDoc = new Document();
		luceneDoc.add(new Field("archiveNumber", image.getChildText("archive_number"), Store.YES, Index.TOKENIZED));		
		luceneDoc.add(new Field("caption", image.getChildText("caption"), Store.YES, Index.TOKENIZED));
		
		try {
			indexWriter.addDocument(luceneDoc);
		} catch (IOException ioe) {
			logger.error(ioe);
		}	
	}
}
