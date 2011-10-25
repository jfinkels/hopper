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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import perseus.artarch.dao.ArtifactDAO;
import perseus.artarch.dao.HibernateArtifactDAO;
import perseus.ie.entity.Entity;

/**
 * ArtifactLoader is a class designed to load data from Perseus' XML data files and put it  
 * into the Artifact table of artarch.
 */
public class ArtifactLoader {

	private static Logger logger = Logger.getLogger(ArtifactLoader.class);
	
	private String artifactType;
	private String dataPath;

	/**
	 * @param artifactType is a valid displayName for any ArtifactType
	 * @param dataPath a path to the XML file for the given artifactType
	 */
	public ArtifactLoader(String artifactType, String dataPath) {
		artifactType = Character.toUpperCase(artifactType.charAt(0)) + artifactType.substring(1);

		this.artifactType = artifactType;
		this.dataPath = dataPath;
	}

	/**
	 * Takes 2 arguments
	 * args[0] is artifactType, a valid displayName for any ArtifactType
	 * args[1] is dataPath to the XML file for the given artifactType
	 */
	public static void main (String[] args){
		if (args.length < 2) {
			logger.error("ArtifactLoader Usage: ArtifactLoader [artifactType] [dataPath]");
			System.exit(0);
		}
		ArtifactLoader aol = new ArtifactLoader(args[0], args[1]);
		aol.loadObjects();	
	}

	/**
	 * Read the XML file using JDOM, instantiate an Artifact of apropos type, and write to the database
	 */
	private void loadObjects() {
		// delete all existing instances from db
		ArtifactDAO aDao = new HibernateArtifactDAO();
		aDao.deleteAllArtifacts(artifactType);
		
		try {
			SAXBuilder sb = new SAXBuilder();
			File XMLdata = new File(dataPath);
			Document doc = sb.build(XMLdata);
			Element root = doc.getRootElement();
			List rows = root.getChildren();
			Iterator i = rows.iterator();
			while (i.hasNext()) {
				createAndWriteArtifact((Element)i.next());
			}
		} catch (IOException e) {
			logger.error("Failed to read file: " + dataPath + ". " + e);
		} catch (JDOMException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * Create an Artifact instance from the artifactData and write to the data source.
	 * @param artifactData one artifact element from the XML file, one Artifact entry in the database
	 */  
	private void createAndWriteArtifact(Element artifactData) {
		Artifact sao = new SimpleArtifact();
		sao = (SimpleArtifact)initAbstractEntity(sao, artifactData);
		sao = (SimpleArtifact)initArtifact(sao, artifactData);
		// Check perseus version - don't continue if the version < 0 or >= 5
		// (artifact isn't finished)
		if (sao.getPerseusVersion().equals("")  || 
				Float.parseFloat(sao.getPerseusVersion()) < 0 || Float.parseFloat(sao.getPerseusVersion()) >= 5) {
			return;
		}
		if (this.artifactType.equals("Site")) {
			if (artifactData.getChildren().size() == 17) {
				initAndWriteSiteArtifact(sao,artifactData);
			} else {
				logger.warn("Wrong number of fields: " + artifactData.getChildText("name"));
			}
		} else {
			AtomicArtifact saao = new SimpleAtomicArtifact(sao);	
			saao = initAtomicArtifact(saao, artifactData);

			if (this.artifactType.equals("Building")) {
				if (artifactData.getChildren().size() == 43) {
					initAndWriteBuildingArtifact(saao, artifactData);
				} else {
					logger.warn("Wrong number of fields: " + artifactData.getChildText("name"));
				}
			} else if (this.artifactType.equals("Coin")) {
				if (artifactData.getChildren().size() == 45) {
					initAndWriteCoinArtifact(saao, artifactData);
				} else {
					logger.warn("Wrong number of fields: " + artifactData.getChildText("name"));
				}
			} else if (this.artifactType.equals("Gem")) {
				if (artifactData.getChildren().size() == 58) {
					initAndWriteGemArtifact(saao, artifactData);
				} else {
					logger.warn("Wrong number of fields: " + artifactData.getChildText("name"));
				}
			} else if (this.artifactType.equals("Sculpture")) {
				if (artifactData.getChildren().size() == 59) {
					initAndWriteSculptureArtifact(saao,artifactData);
				} else {
					logger.warn("Wrong number of fields: " + artifactData.getChildText("name"));
				}
			} else if (this.artifactType.equals("Vase")) {
				if (artifactData.getChildren().size() == 53) {
					initAndWriteVaseArtifact(saao,artifactData);
				} else {
					logger.warn("Wrong number of fields: " + artifactData.getChildText("name"));
				}
			} else {
				logger.warn("Invalid artifact type, please use:  Building, Coin, Gem, Sculpture, Site, or Vase");
			}
		} // Else we have an Artifact Type based on Atomic
	}

	private void initAndWriteBuildingArtifact(AtomicArtifact aao, Element buildingArtifactData) {
		BuildingArtifact bao = new BuildingArtifact(aao);
		bao = initBuildingArtifact(bao, buildingArtifactData);
		if (bao != null) {
			writeArtifact(bao);
		} else {
			logger.error("Failed to create building artifact");
		}
	}

	private void initAndWriteCoinArtifact(AtomicArtifact aao, Element coinArtifactData) {
		CoinArtifact cao = new CoinArtifact(aao);
		cao = initCoinArtifact(cao, coinArtifactData);
		if (cao != null) {
			writeArtifact(cao);
		} else {
			logger.error("Failed to create coin artifact");
		}
	}

	private void initAndWriteGemArtifact(AtomicArtifact aao, Element gemArtifactData) {
		GemArtifact gao = new GemArtifact(aao);
		gao = initGemArtifact(gao, gemArtifactData);
		if (gao != null) {
			writeArtifact(gao);
		} else {
			logger.error("Failed to create gem artifact");
		}
	}

	private void initAndWriteSculptureArtifact(AtomicArtifact aao, Element sculptureArtifactData) {
		SculptureArtifact scao = new SculptureArtifact(aao);
		scao = initSculptureArtifact(scao, sculptureArtifactData);
		if (scao != null) {
			writeArtifact(scao);
		} else {
			logger.error("Failed to create sculpture artifact");
		}
	}

	private void initAndWriteSiteArtifact(Artifact ao, Element siteArtifactData) {
		SiteArtifact siao = new SiteArtifact(ao);
		siao = initSiteArtifact(siao, siteArtifactData);
		if (siao != null) {
			writeArtifact(siao);
		} else {
			logger.error("Failed to create site artifact");
		}
	}

	private void initAndWriteVaseArtifact(AtomicArtifact aao, Element vaseArtifactData) {
		VaseArtifact vao = new VaseArtifact(aao);
		vao = initVaseArtifact(vao, vaseArtifactData);
		if (vao != null) {
			writeArtifact(vao);
		} else {
			logger.error("Failed to create vase artifact");
		}
	}

	private void writeArtifact(Artifact ao) {
		ArtifactDAO aoDAO = new HibernateArtifactDAO();
		// The unit of work for this.
		int result = aoDAO.insertArtifact(ao);
		if (result < 0) {
			logger.warn("Could not insert into database: " + ao);
		}
	}

	private Entity initAbstractEntity(Entity e, Element artifactData) {
		//Initialize the AbstractEntity Fields		
		e.setAuthorityName(artifactData.getChildText("name") + artifactData.getChildText("perseus_version"));
		e.setDisplayName(artifactData.getChildText("name"));
		e.setSortableString(artifactData.getChildText("name"));
		return e;
	}

	private Artifact initArtifact(Artifact ao, Element artifactData) {
		//Initialize the AbstractArtifact Fields

		ao.setName(StringEscapeUtils.escapeXml(artifactData.getChildText("name")));
		ao.setType(artifactData.getChildText("type"));
		ao.setLocation(artifactData.getChildText("location"));
		ao.setSummary(artifactData.getChildText("summary"));
		ao.setPerseusVersion(artifactData.getChildText("perseus_version"));

		ao.setEnteredBy(artifactData.getChildText("entered_by"));
		ao.setSourcesUsed(artifactData.getChildText("sources_used"));
		ao.setOtherBibliography(artifactData.getChildText("other_bibliography"));
		ao.setDocumentaryReferences(artifactData.getChildText("documentary_references"));
		return ao;
	}

	private AtomicArtifact initAtomicArtifact(AtomicArtifact aao, Element artifactData) {
		aao.setAccessionNumber(artifactData.getChildText("accession_number"));
		aao.setDimensions(artifactData.getChildText("dimensions"));
		aao.setRegion(StringEscapeUtils.escapeXml(artifactData.getChildText("region")));	
		aao.setStartDate(artifactData.getChildText("start_date"));
		aao.setStartMod(artifactData.getChildText("start_mod"));
		aao.setEndDate(artifactData.getChildText("end_date"));
		aao.setEndMod(artifactData.getChildText("end_mod"));
		aao.setUnitaryDate(artifactData.getChildText("unitary_date"));
		aao.setUnitaryMod(artifactData.getChildText("unitary_mod"));
		aao.setDateForSort(artifactData.getChildText("date_for_sort"));
		aao.setPeriod(StringEscapeUtils.escapeXml(artifactData.getChildText("period")));
		aao.setPeriodForSort(artifactData.getChildText("period_for_sort"));
		aao.setCulture(artifactData.getChildText("culture"));
		aao.setContext(StringEscapeUtils.escapeXml(artifactData.getChildText("context")));
		aao.setContextMod(artifactData.getChildText("context_mod"));
		aao.setFindspot(artifactData.getChildText("findspot"));
		aao.setFindspotMod(artifactData.getChildText("findspot_mod"));
		aao.setCollection(StringEscapeUtils.escapeXml(artifactData.getChildText("collection")));
		aao.setDateDescription(artifactData.getChildText("date_description"));
		aao.setCollectionHistory(artifactData.getChildText("collection_history"));
		aao.setDonor(artifactData.getChildText("donor"));
		aao.setCondition(artifactData.getChildText("condit"));
		aao.setConditionDescription(artifactData.getChildText("condition_description"));
		aao.setComparanda(artifactData.getChildText("comparanda"));
		aao.setMaterial(StringEscapeUtils.escapeXml(artifactData.getChildText("material")));
		aao.setMaterialDescription(artifactData.getChildText("material_description"));
		aao.setOtherNotes(artifactData.getChildText("other_notes"));
		return aao;
	}

	private CoinArtifact initCoinArtifact(CoinArtifact cao, Element coinArtifactData) {
		cao.setActualWeight(coinArtifactData.getChildText("actual_weight"));
		cao.setCommentary(coinArtifactData.getChildText("commentary"));
		cao.setDenomination(StringEscapeUtils.escapeXml(coinArtifactData.getChildText("denomination")));
		cao.setDieAxis(coinArtifactData.getChildText("die_axis"));
		cao.setIssuingAuthority(StringEscapeUtils.escapeXml(coinArtifactData.getChildText("issuing_authority")));
		cao.setObverseLegend(coinArtifactData.getChildText("obverse_legend"));
		cao.setObverseType(coinArtifactData.getChildText("obverse_type"));
		cao.setReverseLegend(coinArtifactData.getChildText("reverse_legend"));
		cao.setReverseType(coinArtifactData.getChildText("reverse_type"));
		return cao;
	}

	private VaseArtifact initVaseArtifact(VaseArtifact vao, Element vaseArtifactData) {
		vao.setCeramicPhase(vaseArtifactData.getChildText("ceramic_phase"));
		vao.setDecorationDescription(vaseArtifactData.getChildText("decoration_description"));
		vao.setEssayNumber(vaseArtifactData.getChildText("essay_number"));
		vao.setEssayText(vaseArtifactData.getChildText("essay_text"));
		vao.setGraffiti(vaseArtifactData.getChildText("graffiti"));
		vao.setInscriptions(vaseArtifactData.getChildText("inscriptions"));
		vao.setPainter(StringEscapeUtils.escapeXml(vaseArtifactData.getChildText("painter")));
		vao.setPainterMod(vaseArtifactData.getChildText("painter_mod"));
		vao.setAttributedBy(vaseArtifactData.getChildText("attributed_by"));
		vao.setPotter(StringEscapeUtils.escapeXml(vaseArtifactData.getChildText("potter")));
		vao.setPotterMod(vaseArtifactData.getChildText("potter_mod"));
		vao.setPrimaryCitation(vaseArtifactData.getChildText("primary_citation"));
		vao.setBeazleyNumber(vaseArtifactData.getChildText("beazley_number"));
		vao.setRelief(vaseArtifactData.getChildText("relief"));
		vao.setShape(StringEscapeUtils.escapeXml(vaseArtifactData.getChildText("shape")));
		vao.setShapeDescription(vaseArtifactData.getChildText("shape_description"));
		vao.setWare(StringEscapeUtils.escapeXml(vaseArtifactData.getChildText("ware")));
		return vao;
	}

	private SculptureArtifact initSculptureArtifact(SculptureArtifact sao, Element sculptureArtifactData) {
		sao.setAssociatedBuilding(StringEscapeUtils.escapeXml(sculptureArtifactData.getChildText("assoc_building")));
		sao.setCategory(StringEscapeUtils.escapeXml(sculptureArtifactData.getChildText("category")));
		sao.setObjectFunction(StringEscapeUtils.escapeXml(sculptureArtifactData.getChildText("object_function")));
		sao.setGraffiti(sculptureArtifactData.getChildText("graffiti"));
		sao.setInscription(sculptureArtifactData.getChildText("inscription"));
		sao.setInscriptionBibliography(sculptureArtifactData.getChildText("inscription_bibliography"));
		sao.setOriginal(sculptureArtifactData.getChildText("original"));
		sao.setOriginalOrCopy(sculptureArtifactData.getChildText("original_or_copy"));
		sao.setPlacement(sculptureArtifactData.getChildText("placement"));
		sao.setPrimaryCitation(sculptureArtifactData.getChildText("primary_citation"));
		sao.setScale(StringEscapeUtils.escapeXml(sculptureArtifactData.getChildText("scale")));
		sao.setScaleForSort(sculptureArtifactData.getChildText("scale_for_sort"));
		sao.setSculptor(StringEscapeUtils.escapeXml(sculptureArtifactData.getChildText("sculptor")));
		sao.setSculptorMod(sculptureArtifactData.getChildText("sculptor_mod"));
		sao.setStyle(sculptureArtifactData.getChildText("style"));
		sao.setFormStyleDescription(sculptureArtifactData.getChildText("form_style_description"));
		sao.setSubjectDescription(sculptureArtifactData.getChildText("subject_description"));
		sao.setTechnique(sculptureArtifactData.getChildText("technique"));
		sao.setTechniqueDescription(sculptureArtifactData.getChildText("technique_description"));
		sao.setTitle(sculptureArtifactData.getChildText("title"));
		sao.setSculptureType(sculptureArtifactData.getChildText("sculpture_type"));
		sao.setInGroup(sculptureArtifactData.getChildText("in_group"));
		sao.setInWhole(sculptureArtifactData.getChildText("in_whole"));
		return sao;
	}

	private GemArtifact initGemArtifact(GemArtifact gao, Element gemArtifactData) {
		gao.setCategory(gemArtifactData.getChildText("category"));
		gao.setObjectFunction(gemArtifactData.getChildText("object_function"));
		gao.setGraffiti(gemArtifactData.getChildText("graffiti"));
		gao.setInscription(gemArtifactData.getChildText("inscription"));
		gao.setInscriptionBibliography(gemArtifactData.getChildText("inscription_bibliography"));
		gao.setOriginal(gemArtifactData.getChildText("original"));
		gao.setOriginalOrCopy(gemArtifactData.getChildText("original_or_copy"));
		gao.setPlacement(gemArtifactData.getChildText("placement"));
		gao.setPrimaryCitation(gemArtifactData.getChildText("primary_citation"));
		gao.setScale(gemArtifactData.getChildText("scale"));
		gao.setScaleForSort(gemArtifactData.getChildText("scale_for_sort"));
		gao.setSculptor(gemArtifactData.getChildText("sculptor"));
		gao.setSculptorMod(gemArtifactData.getChildText("sculptor_mod"));
		gao.setStyle(StringEscapeUtils.escapeXml(gemArtifactData.getChildText("style")));
		gao.setFormStyleDescription(gemArtifactData.getChildText("form_style_description"));
		gao.setSubjectDescription(gemArtifactData.getChildText("subject_description"));
		gao.setTechnique(gemArtifactData.getChildText("technique"));
		gao.setTechniqueDescription(gemArtifactData.getChildText("technique_description"));
		gao.setTitle(gemArtifactData.getChildText("title"));
		gao.setSculptureType(StringEscapeUtils.escapeXml(gemArtifactData.getChildText("sculpture_type")));
		gao.setInGroup(gemArtifactData.getChildText("in_group"));
		gao.setInWhole(gemArtifactData.getChildText("in_whole"));
		return gao;
	}

	private BuildingArtifact initBuildingArtifact(BuildingArtifact bao, Element buildingArtifactData) {
		bao.setArchitecturalOrder(buildingArtifactData.getChildText("architectural_order"));
		bao.setArchitect(StringEscapeUtils.escapeXml(buildingArtifactData.getChildText("architect")));
		bao.setArchitectEvidence(buildingArtifactData.getChildText("architect_evidence"));
		bao.setBuildingType(StringEscapeUtils.escapeXml(buildingArtifactData.getChildText("building_type")));
		bao.setHistory(buildingArtifactData.getChildText("history"));
		bao.setPlan(buildingArtifactData.getChildText("plan"));
		bao.setSeeAlso(buildingArtifactData.getChildText("see_also"));
		return bao;
	}

	private SiteArtifact initSiteArtifact(SiteArtifact sao, Element siteArtifactData) {
		sao.setExtent(siteArtifactData.getChildText("extent"));
		sao.setHumanName(siteArtifactData.getChildText("human_name"));
		sao.setRegion(StringEscapeUtils.escapeXml(siteArtifactData.getChildText("region")));
		sao.setSiteType(StringEscapeUtils.escapeXml(siteArtifactData.getChildText("site_type")));
		sao.setDescription(siteArtifactData.getChildText("description"));
		sao.setExploration(siteArtifactData.getChildText("exploration"));
		sao.setPeriods(siteArtifactData.getChildText("periods"));
		sao.setPhysical(siteArtifactData.getChildText("physical"));
		return sao;
	}
}
