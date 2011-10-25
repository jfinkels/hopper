/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
 */
package perseus.artarch.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;

import perseus.artarch.Artifact;
import perseus.artarch.ArtifactKeyword;
import perseus.artarch.ArtifactType;
import perseus.artarch.BuildingArtifact;
import perseus.artarch.CoinArtifact;
import perseus.artarch.GemArtifact;
import perseus.artarch.SculptureArtifact;
import perseus.artarch.SiteArtifact;
import perseus.artarch.VaseArtifact;
import perseus.util.Config;
import perseus.util.HibernateDAO;

/**
 * The HibernateArtifactDAO retrieves information about an Artifact from a Hibernate data source
 *  containing Artifact data.
 */
public class HibernateArtifactDAO extends HibernateDAO<Artifact> implements ArtifactDAO {

	private static Logger logger = Logger.getLogger(HibernateArtifactDAO.class);

	/**
	 * Save an Artifact to the underlying data source.
	 * @param artifact an Artifact with the desired changes authorityName != null.
	 * @return true if the update is successful, false otherwise
	 */  
	public int insertArtifact(Artifact artifact) {
		int result = -1;
		if (artifact.getAuthorityName() == null) {
			throw new IllegalArgumentException("No auth name for " + artifact);
		}
		try {
			getSession().save(artifact);
		} catch (org.hibernate.exception.SQLGrammarException sqe) {
			logger.error(sqe);
			logger.error(sqe.getSQL());
			logger.error(sqe.getSQLException());
		} catch (Exception e) {
			getSession().flush();
		}
		Integer id = artifact.getId();
		if (id == null) {
			result = -1;
		} else {
			result = id.intValue();
		}
		return result;
	}

	/**
	 * Retrieve persisted Artifacts using artifact as a Query Object(316)
	 * @param artifact the Query Object Artifact
	 * @return a List of all artifacts similar to artifact.
	 */
	public List findArtifact(Artifact artifact) {
		Example example = Example.create(artifact).ignoreCase();
		List results = new ArrayList();
		try {
			results = getSession().createCriteria(artifact.getClass())
			.add(example).list();
		} catch (SQLGrammarException sge) {
			logger.error("Error with SQL grammar " + sge);
			logger.error(sge.getSQLException());
			getSession().flush();	   
		} catch (Exception e) {
			logger.error("findArtifact error: " + e);
			getSession().flush();
		}	
		return results;
	}

	/** 
	 * Retrieve persisted Artifacts using artifact as a Query Object(316)
	 * @param artifact the Query Object Artifact
	 * @param offset - where to start grabbing Artifacts from the list of matches
	 * @param maxResults - how many Artifacts to grab
	 */
	public List findArtifact(Artifact artifact, int offset, int maxResults) {
		Example example = Example.create(artifact).ignoreCase();
		List results = new ArrayList();
		try {
			results = getSession().createCriteria(artifact.getClass())
			.add(example).setFirstResult(offset).setMaxResults(maxResults).list();
		} catch (Exception e) {
			logger.error("findArtifact: " + e);
			getSession().flush();
		}	
		return results;
	}

	/** 
	 * Retrieve persisted Artifacts using artifact as a Query Object(316)
	 * @param artifactType 
	 * @param offset - where to start grabbing Artifacts from the list of matches
	 * @param maxResults - how many Artifacts to grab
	 */
	public List findArtifact(ArtifactType artifactType, String field, String value, int offset, int maxResults) {
		List results = getSession().createCriteria(artifactType.getArtifactClass())
		.add(Restrictions.eq(field, value))
		.setFirstResult(offset).setMaxResults(maxResults)
		.addOrder(Order.asc("name")).list();

		return results;
	}

	/**
	 * Save a persisted object to the underlying data source.
	 * @param artifact a persisted Artifact that has 0 or more modifications to save
	 * @return true if the update is successful, false otherwise
	 */
	public boolean updateArtifact(Artifact artifact) {
		boolean result = true;
		if (artifact.getAuthorityName() == null) {
			result = false;
			throw new IllegalArgumentException("No auth name for " + artifact);
		}
		try {
			getSession().update(artifact);
		} catch (Exception e) {
			logger.error("updateArtifact: " + e);
			getSession().flush();
		}
		return result;
	}
	
	/**
	 * Remove all artifacts for a particular artifact type
	 * @param type
	 * @return true if delete was a success, otherwise false
	 */
	public boolean deleteAllArtifacts(String type) {
		int numDeleted = 0;

		try {
			String queryString = "delete from perseus.artarch."+type+"Artifact";
			numDeleted = getSession().createQuery(queryString).executeUpdate();
		} catch (HibernateException e) {
			logger.error("Error deleting artifacts of type "+ type+": "+e);
		}

		if (numDeleted > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Remove an Artifact from the underlying data source.
	 * @param artifact a persisted Artifact
	 * @return true if the delete was a success, false otherwise. 
	 */
	public boolean deleteArtifact(Artifact artifact) {
		boolean result = true;
		if (artifact.getAuthorityName() == null) {
			logger.error("deleteArtifact: NULL authname");
			result = false;
		}
		try {
			getSession().delete(artifact);
		} catch (Exception e) {
			logger.error("deleteArtifact: " + e);
			getSession().flush();
		}
		return result;
	}

	/**
	 * Get distinct values for the fieldName of an Artifact from the underlying data source
	 * @param artifact Any class that implements Artifact
	 * @param fieldName A valid fieldName for the artifact 
	 * @return a list of Strings that are values of the fieldName
	 */
	public List<String> findDistinctFieldValues(String artifactType, String fieldName) {
		fieldName = fieldName.toLowerCase();
		
		String query = "select distinct " + fieldName +" from ";
		if (artifactType.equalsIgnoreCase("Building")) {
			query = query + "BuildingArtifact";
		} 
		else if (artifactType.equalsIgnoreCase("Coin")) {
			query = query + "CoinArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Gem")) {
			query = query + "GemArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Sculpture")) {
			query = query + "SculptureArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Site")) {
			query = query + "SiteArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Vase")) {
			query = query + "VaseArtifact";
		}
		else {
			query = query + "Artifact";
		}
		
		query = query + " order by "+fieldName;
		
		return getSession().createQuery(query).list();
	}
	
	/**
	 * Get distinct values for the fieldName of an Artifact from the underlying data source
	 * @param artifact Any class that implements Artifact
	 * @param fieldName A valid fieldName for the artifact 
	 * @return a list of Strings that are values of the fieldName
	 */
	public Map<String, Integer> findDistinctFieldValuesCounts(String artifactType, String fieldName) {
		Map<String, Integer> fieldsCounts = new LinkedHashMap<String, Integer>();
		
		String query = "select distinct " + fieldName +", count(*) as count from ";
		if (artifactType.equalsIgnoreCase("Building")) {
			query = query + "BuildingArtifact";
		} 
		else if (artifactType.equalsIgnoreCase("Coin")) {
			query = query + "CoinArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Gem")) {
			query = query + "GemArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Sculpture")) {
			query = query + "SculptureArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Site")) {
			query = query + "SiteArtifact";
		}
		else if (artifactType.equalsIgnoreCase("Vase")) {
			query = query + "VaseArtifact";
		}
		else {
			query = query + "Artifact";
		}
		
		query = query + " group by "+fieldName+" order by "+fieldName;
		
		ScrollableResults results = getSession().createQuery(query).scroll();
		
		while (results.next()) {
			String fieldValue = results.getString(0);
			long count = (Long) results.get(1);
			fieldsCounts.put(fieldValue, (int) count);
		}
		
		return fieldsCounts;
	}

	/**
	 * Get the number of distinct values for the fieldName of an Artifact from the underlying data source
	 * @param artifact Any class that implements Artifact
	 * @param fieldName A valid fieldName for the artifact
	 * @return the number of distinct values for the given artifact and fieldname 
	 */
	public int getDistinctFieldValuesHits(Artifact artifact, String fieldName) {
		int results = 0;
		String artifactType;
		if (artifact instanceof BuildingArtifact) {
			artifactType = "BuildingArtifact";
		} 
		else if (artifact instanceof CoinArtifact) {
			artifactType = "CoinArtifact";
		}
		else if (artifact instanceof GemArtifact) {
			artifactType = "GemArtifact";
		}
		else if (artifact instanceof SculptureArtifact) {
			artifactType = "SculptureArtifact";
		}
		else if (artifact instanceof SiteArtifact) {
			artifactType = "SiteArtifact";
		}
		else if (artifact instanceof VaseArtifact) {
			artifactType = "VaseArtifact";
		}
		else {
			artifactType ="Artifact";
		}
		String query = "select count(*) from "+ artifactType+ " artifact " +
		"where artifact." + fieldName + " is not null" +
		" and artifact." + fieldName + "!=''";
		try {
			results = ((Number)getSession().createQuery(query)
					.uniqueResult()).intValue();
		} catch (Exception e) {
			logger.error("Exception: " + e);
			getSession().flush();
		}
		return results;
	}

	/**
	 * Get the number of Artifacts in the underlying data source similar to artifact
	 * @param artifact The underlying Query Object(316)
	 * @return the number of Artifacts similar to artifact
	 */
	public int getTotalHits(Artifact artifact) {
		Example example = Example.create(artifact).ignoreCase();
		List results = new ArrayList();
		try {
			results = getSession().createCriteria(artifact.getClass())
			.add(example).list();
		} catch (Exception e) {
			logger.error("findArtifact: " + e);
			getSession().flush();
		}
		return results.size();	
	}
	
	public List<Artifact> getRandomArtifacts() {
		String[] artifactTypes = Config.artifactTypes;
		List<Artifact> randomArtifacts = new ArrayList<Artifact>();
		for (String artifactType : artifactTypes) {
			//MySQL's order by rand() is really slow, so we'll choose a random id ourselves
			List<Integer> ids = getSession().createQuery("select id from Artifact where type= :at")
				.setParameter("at", artifactType).list();
			Collections.shuffle(ids);
			Artifact a = (Artifact) getSession().createQuery("from Artifact a where a.id = :id")
				.setParameter("id", ids.get(0)).setMaxResults(1).uniqueResult();
			randomArtifacts.add(a);
		}
		return randomArtifacts;
	}

	public Artifact findArtifact(String name, String type) {
		Artifact a = (Artifact) getSession().createQuery("from Artifact a where a.name = :name " +
				"and a.type = :type").setParameter("name", name).setParameter("type", type).setMaxResults(1)
				.uniqueResult();
		return a;
	}
	
	public Artifact findArtifactByName(String name) {
		List<Artifact> artifacts = getSession().createQuery("from Artifact a where a.name = :name")
		.setParameter("name", name).list();
		logger.info("artifacts is "+artifacts);
		if (artifacts.size() == 1) {
			logger.info("artifact size is 1");
			return artifacts.get(0);
		}
		logger.info("returning null because there was no found artifact");
		return null;
	}
	
	public Artifact findArtifact(String archiveNumber) {
		List results = getSession().createQuery("select entity from EntityOccurrence eo where " +
				"eo.location.query.documentID = :archiveNumber").setParameter("archiveNumber", archiveNumber).list();
		
		Artifact a = null;
		if (results.size() > 0) {
			a = (Artifact) results.get(0);
		}
		return a;
	}

	public void deleteKeywords() {
		getSession().createSQLQuery("delete from hib_artifact_keywords").executeUpdate();		
	}

	public Map<String, Integer> getDistinctKeyclasses(String artifactType) {
		Map<String, Integer> keyclassesCounts = new LinkedHashMap<String, Integer>();
		ScrollableResults results = getSession().createQuery("select keyclass, count(distinct keyword) as count " +
				"from ArtifactKeyword " +
				"where artifactType = :artType group by keyclass")
		.setParameter("artType", artifactType).scroll();
		
		while (results.next()) {
			String keyclass = (String) results.get(0);
			long count = (Long) results.get(1);
			keyclassesCounts.put(keyclass, (int) count);
		}
		
		return keyclassesCounts;
	}

	public Map<String, Integer> getDistinctKeywords(String artifactType, String keyclass) {
		Map<String, Integer> keywordsCounts = new LinkedHashMap<String, Integer>();
		ScrollableResults results = getSession().createQuery("select keyword, count(keyword) as count " +
				"from ArtifactKeyword " +
		"where artifactType = :artType and keyclass = :keyclass group by keyword")
		.setParameter("artType", artifactType)
		.setParameter("keyclass", keyclass).scroll();
		
		while (results.next()) {
			String keyword = (String) results.get(0);
			long count = (Long) results.get(1);
			keywordsCounts.put(keyword, (int) count);
		}

		return keywordsCounts;
	}

	public int getArtifactCount(ArtifactType artType, String field, String value) {
		return (Integer) getSession().createCriteria(artType.getArtifactClass())
		.setProjection(Projections.rowCount())
		.add(Restrictions.eq(field, value))
		.uniqueResult();
	}

	public void saveArtifactKeyword(ArtifactKeyword ak) {
		getSession().save(ak);
	}

	public List<Artifact> getArtifactsByKeyword(String artType,
			String keyclass, String keyword, int offset, int maxResults) {
		List<ArtifactKeyword> results = getSession().createQuery("from ArtifactKeyword " +
				"where artifactType = :artType and keyclass = :keyclass and keyword = :keyword")
				.setParameter("artType", artType)
				.setParameter("keyclass", keyclass)
				.setParameter("keyword", keyword)
				.setFirstResult(offset).setMaxResults(maxResults).list();
		
		List<Artifact> artifacts = new ArrayList<Artifact>();
		//I was getting errors trying to just select artifact from ArtifactKeyword, so we loop through
		//each ArtifactKeyword object and get the Artifact and add to list.  not ideal, but it's ok.
		for (ArtifactKeyword ak : results) {
			Artifact art = ak.getArtifact();
			artifacts.add(art);
		}
		
		return artifacts;
	}
	
	public int getCountByKeyword(String artType, String keyclass, String keyword) {
		long count = (Long) getSession().createQuery("select count(artifact) from ArtifactKeyword " +
				"where artifactType = :artType and keyclass = :keyclass and keyword = :keyword")
				.setParameter("artType", artType)
				.setParameter("keyclass", keyclass)
				.setParameter("keyword", keyword)
				.uniqueResult();
		return (int) count;
	}
}
