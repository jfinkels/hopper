/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
 */
package perseus.artarch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.util.Config;

/**
 * ArtifactIndexer is a class used to index Artifacts of a given ArtifactType using Lucene
 */
public class ArtifactIndexer {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String CONTENTS = "contents";
	
	private static Logger logger = Logger.getLogger(ArtifactIndexer.class);
	ArtifactDAO aoDAO = new HibernateArtifactDAO();

	private IndexWriter indexWriter;
	private String artifactType;
	String newLine = System.getProperty("line.separator");

	public static void main(String[] args) {
		ArtifactIndexer ai = new ArtifactIndexer();
		for (String artifactType : Config.artifactTypes) {
			ai.setArtifactType(artifactType);
			ai.indexArtifacts();
		}
		ai.optimize();
	}

	/**
	 * @param artifactType, any valid displayName of ArtifactType
	 */
	public ArtifactIndexer() {
		try {
			this.indexWriter = new IndexWriter(Config.getSearchIndexPath() + "artifact", new SimpleAnalyzer(), true);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
			ioe.printStackTrace();
		}
	}

	public String getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	public void indexArtifacts() {
		try {
			logger.info("indexing "+artifactType+"...");
			Artifact queryArtifact = getQueryArtifact();
			List results = aoDAO.findArtifact(queryArtifact);
			Iterator artifactIter = results.iterator();
			while (artifactIter.hasNext()) {
				Artifact artifact = (Artifact)artifactIter.next();
				createLuceneDoc(artifact);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	private void optimize() {
		try {
			logger.info("optimizing indexes...");
			indexWriter.optimize();
			indexWriter.close();		
		} catch (CorruptIndexException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	private void createLuceneDoc(Artifact artifact) {
		if (artifact instanceof BuildingArtifact) {
			createLuceneDoc((BuildingArtifact)artifact);
		} 
		else if (artifact instanceof CoinArtifact) {
			createLuceneDoc((CoinArtifact)artifact);
		} 
		else if (artifact instanceof GemArtifact) {
			createLuceneDoc((GemArtifact)artifact);
		} 
		else if (artifact instanceof SculptureArtifact) {
			createLuceneDoc((SculptureArtifact)artifact);
		} 
		else if (artifact instanceof SiteArtifact) {
			createLuceneDoc((SiteArtifact)artifact);
		} 
		else if (artifact instanceof VaseArtifact) {
			createLuceneDoc((VaseArtifact)artifact);
		}
		else {
			logger.error("Unknown artifact type");
		}
	}

	private Map<String,String> initArtifactDoc(Map<String,String> luceneFieldValue, Artifact artifact) {
		luceneFieldValue.put(ID, artifact.getId().toString());
		luceneFieldValue.put(NAME, artifact.getName());
		
		String contents = artifact.getName();
		contents += newLine + artifact.getSummary();
		luceneFieldValue.put(CONTENTS, contents);

		return luceneFieldValue;
	}

	private Map<String,String> initAtomicArtifactDoc(Map<String,String> luceneFieldValue, AtomicArtifact artifact) {
		String contents = luceneFieldValue.get(CONTENTS);
		
		contents += newLine + artifact.getRegion();
		contents += newLine + artifact.getPeriod();
		contents += newLine + artifact.getCulture();
		contents += newLine + artifact.getContext();
		contents += newLine + artifact.getFindspot();
		contents += newLine + artifact.getCollection();
		contents += newLine + artifact.getDateDescription();
		contents += newLine + artifact.getCollectionHistory();
		contents += newLine + artifact.getDonor();
		contents += newLine + artifact.getCondition();
		contents += newLine + artifact.getConditionDescription();
		contents += newLine + artifact.getMaterial();
		contents += newLine + artifact.getMaterialDescription();
		contents += newLine + artifact.getOtherNotes();
		
		luceneFieldValue.put(CONTENTS, contents);
		
		return luceneFieldValue;
	}

	private void createLuceneDoc(BuildingArtifact artifact) {
		Map<String,String> luceneFieldValue = new HashMap<String,String>();
		luceneFieldValue = initArtifactDoc(luceneFieldValue, (Artifact)artifact);
		luceneFieldValue = initAtomicArtifactDoc(luceneFieldValue, (AtomicArtifact)artifact);
		
		String contents = luceneFieldValue.get(CONTENTS);
		contents += newLine + artifact.getArchitecturalOrder();
		contents += newLine + artifact.getArchitect();
		contents += newLine + artifact.getArchitectEvidence();
		contents += newLine + artifact.getBuildingType();
		contents += newLine + artifact.getHistory();
		contents += newLine + artifact.getPlan();
		
		luceneFieldValue.put(CONTENTS, contents);

		Document luceneDoc = createDocument(luceneFieldValue, artifact);
		
		try {
			indexWriter.addDocument(luceneDoc);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
		}
	}

	private void createLuceneDoc(CoinArtifact artifact) {
		Map<String,String> luceneFieldValue = new HashMap<String,String>();
		luceneFieldValue = initArtifactDoc(luceneFieldValue, (Artifact)artifact);
		luceneFieldValue = initAtomicArtifactDoc(luceneFieldValue, (AtomicArtifact)artifact);
		
		String contents = luceneFieldValue.get(CONTENTS);
		contents += newLine + artifact.getCommentary();
		contents += newLine + artifact.getDenomination();
		contents += newLine + artifact.getIssuingAuthority();
		contents += newLine + artifact.getObverseType();
		contents += newLine + artifact.getReverseType();
		
		luceneFieldValue.put(CONTENTS, contents);
		
		Document luceneDoc = createDocument(luceneFieldValue, artifact);

		try {
			indexWriter.addDocument(luceneDoc);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
		}	
	}

	private void createLuceneDoc(GemArtifact artifact) {
		Map<String,String> luceneFieldValue = new HashMap<String,String>();
		luceneFieldValue = initArtifactDoc(luceneFieldValue, (Artifact)artifact);
		luceneFieldValue = initAtomicArtifactDoc(luceneFieldValue, (AtomicArtifact)artifact);
		
		String contents = luceneFieldValue.get(CONTENTS);
		contents += newLine + artifact.getStyle();
		contents += newLine + artifact.getSculptureType();
		
		luceneFieldValue.put(CONTENTS, contents);
		
		Document luceneDoc = createDocument(luceneFieldValue, artifact);
		
		try {
			this.indexWriter.addDocument(luceneDoc);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
		}
	}

	private void createLuceneDoc(SculptureArtifact artifact) {
		Map<String,String> luceneFieldValue = new HashMap<String,String>();
		luceneFieldValue = initArtifactDoc(luceneFieldValue, (Artifact)artifact);
		luceneFieldValue = initAtomicArtifactDoc(luceneFieldValue, (AtomicArtifact)artifact);
		
		String contents = luceneFieldValue.get(CONTENTS);
		contents += newLine + artifact.getAssociatedBuilding();
		contents += newLine + artifact.getCategory();
		contents += newLine + artifact.getObjectFunction();
		contents += newLine + artifact.getGraffiti();
		contents += newLine + artifact.getInscription();
		contents += newLine + artifact.getOriginal();
		contents += newLine + artifact.getPlacement();
		contents += newLine + artifact.getScale();
		contents += newLine + artifact.getSculptor();
		contents += newLine + artifact.getStyle();
		contents += newLine + artifact.getFormStyleDescription();
		contents += newLine + artifact.getSubjectDescription();
		contents += newLine + artifact.getTechnique();
		contents += newLine + artifact.getTechniqueDescription();
		contents += newLine + artifact.getTitle();
		contents += newLine + artifact.getSculptureType();
		contents += newLine + artifact.getInGroup();
		contents += newLine + artifact.getInWhole();
		
		luceneFieldValue.put(CONTENTS, contents);
		
		Document luceneDoc = createDocument(luceneFieldValue, artifact);
		
		try {
			this.indexWriter.addDocument(luceneDoc);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
		}       
	}

	private void createLuceneDoc(SiteArtifact artifact) {
		Map<String,String> luceneFieldValue = new HashMap<String,String>();
		luceneFieldValue = initArtifactDoc(luceneFieldValue, (Artifact)artifact);
		
		String contents = luceneFieldValue.get(CONTENTS);
		contents += newLine + artifact.getRegion();
		contents += newLine + artifact.getSiteType();
		contents += newLine + artifact.getDescription();
		contents += newLine + artifact.getExploration();
		contents += newLine + artifact.getPeriods();
		contents += newLine + artifact.getPhysical();
		
		luceneFieldValue.put(CONTENTS, contents);
		
		Document luceneDoc = createDocument(luceneFieldValue, artifact);
		
		try {
			this.indexWriter.addDocument(luceneDoc);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
		}       
	}

	private void createLuceneDoc(VaseArtifact artifact) {
		Map<String,String> luceneFieldValue = new HashMap<String,String>();
		luceneFieldValue = initArtifactDoc(luceneFieldValue, (Artifact)artifact);
		luceneFieldValue = initAtomicArtifactDoc(luceneFieldValue, (AtomicArtifact)artifact);
		
		String contents = luceneFieldValue.get(CONTENTS);
		contents += newLine + artifact.getCeramicPhase();
		contents += newLine + artifact.getDecorationDescription();
		contents += newLine + artifact.getEssayText();
		contents += newLine + artifact.getGraffiti();
		contents += newLine + artifact.getInscriptions();
		contents += newLine + artifact.getPainter();
		contents += newLine + artifact.getAttributedBy();
		contents += newLine + artifact.getPotter();
		contents += newLine + artifact.getRelief();
		contents += newLine + artifact.getShape();
		contents += newLine + artifact.getShapeDescription();
		contents += newLine + artifact.getWare();

		luceneFieldValue.put(CONTENTS, contents);
		
		Document luceneDoc = createDocument(luceneFieldValue, artifact);

		try {
			this.indexWriter.addDocument(luceneDoc);
		} catch (java.io.IOException ioe) {
			logger.error(ioe);
		}       
	}
	
	private Document createDocument(Map<String, String> luceneFieldValue, Artifact artifact) {
		Document luceneDoc = new Document();

		for (String key : luceneFieldValue.keySet()) {
			luceneDoc.add(new Field(key, luceneFieldValue.get(key), Store.YES, Index.TOKENIZED));
		}

		luceneDoc.add(new Field(TYPE, artifact.getType(), Store.YES, Index.UN_TOKENIZED));

		return luceneDoc;
	}

	private Artifact getQueryArtifact() {
		Artifact artifact = null;
		if (artifactType.equals("Building")) {
			artifact = new BuildingArtifact();
		} else if (artifactType.equals("Coin")) {
			artifact = new CoinArtifact();
		} else if (artifactType.equals("Gem")) {
			artifact = new GemArtifact();
		} else if (artifactType.equals("Sculpture")) {
			artifact = new SculptureArtifact();
		} else if (artifactType.equals("Site")) {
			artifact = new SiteArtifact();
		} else if (artifactType.equals("Vase")) {
			artifact = new VaseArtifact();
		}
		return artifact;
	}

}
