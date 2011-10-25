/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
 */
package perseus.artarch;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Every class which implements Artifact is an ArtifactType and should be registered as a static final variable in this class. 
 * Based loosly upon: http://www.javaworld.com/javaworld/jw-10-2002/jw-1025-opensourceprofile.html
 * 
 * ArtifactType is a typesafe enum pattern as discussed by:
 * Block, Joshua in "Effective Java" on page 105.
 */
public class ArtifactType implements Comparable{
	private final String displayName;
	private final String htmlAttribute;
	private final Class clazz;
	private final String teiCatalogArchiveNumber;

	private ArtifactType(Class clazz, String displayName, 
			String htmlAttribute, String teiCatalogArchiveNumber) {
		this.clazz = clazz;
		this.displayName = displayName;
		this.htmlAttribute = htmlAttribute;	
		this.teiCatalogArchiveNumber = teiCatalogArchiveNumber;
	}

	public String getDisplayName() {return displayName;}

	public Class getArtifactClass() {return clazz;}

	public String getTeiCatalogArchiveNumber() {return teiCatalogArchiveNumber;}

	public String toString() { return displayName; }

	public static final ArtifactType ARTIFACT = new ArtifactType(SimpleArtifact.class, "Art Object", "artObject", "");
	public static final ArtifactType ATOMIC = new ArtifactType(SimpleAtomicArtifact.class,  "Atomic", "atomic", "");
	public static final ArtifactType BUILDING = new ArtifactType(BuildingArtifact.class, "Building", "building", "1999.04.0039");
	public static final ArtifactType COIN = new ArtifactType(CoinArtifact.class, "Coin", "coin", "1999.04.0040");
	public static final ArtifactType GEM = new ArtifactType(GemArtifact.class, "Gem", "gem", "1999.04.0048");
	public static final ArtifactType SCULPTURE = new ArtifactType(SculptureArtifact.class, "Sculpture", "sculpture", "1999.04.0041");
	public static final ArtifactType SITE = new ArtifactType(SiteArtifact.class, "Site", "site", "1999.04.0042");
	public static final ArtifactType VASE = new ArtifactType(VaseArtifact.class, "Vase", "vase", "1999.04.0043");    

	public static ArtifactType getArtifactType(String displayName) {
		if ((displayName != null) && (!displayName.equals("")) ) {
			if(displayName.equals(ARTIFACT.getDisplayName())) {
				return ARTIFACT;
			} else if(displayName.equals(ATOMIC.getDisplayName())) {
				return ATOMIC;
			} else if(displayName.equals(BUILDING.getDisplayName())) {
				return BUILDING;
			} else if(displayName.equals(COIN.getDisplayName())) {
				return COIN;
			} else if(displayName.equals(GEM.getDisplayName())) {
				return GEM;
			} else if(displayName.equals(SCULPTURE.getDisplayName())) {
				return SCULPTURE;
			} else if(displayName.equals(SITE.getDisplayName())) {
				return SITE;
			} else if(displayName.equals(VASE.getDisplayName())) {
				return VASE;
			}
		} 
		return ARTIFACT;
	}

	public static ArtifactType getArtifactType(Object obj) {
		return getArtifactType(obj.getClass());
	}

	public static ArtifactType getArtifactType(Class clazz) {
		if (clazz == SimpleArtifact.class) {
			return ARTIFACT;
		} else if (clazz == SimpleAtomicArtifact.class) {
			return ATOMIC;
		} else if (clazz == BuildingArtifact.class) {
			return BUILDING;
		} else if (clazz == CoinArtifact.class) {
			return COIN;
		} else if (clazz == GemArtifact.class) {
			return GEM;
		} else if (clazz == SiteArtifact.class) {
			return SITE;
		} else if (clazz == SculptureArtifact.class) {
			return SCULPTURE;
		} else if (clazz == VaseArtifact.class) {
			return VASE;
		}
		return ARTIFACT;
	}

	public static ArtifactType getArtifactType(HttpServletRequest request, String paramName) {
		String displayName = request.getParameter(paramName);
		return getArtifactType(displayName);
	}

	public static String getDisplayName(Class clazz) {
		if (clazz.equals(SimpleArtifact.class)) {
			return ARTIFACT.getDisplayName();
		} else if(clazz.equals(SimpleAtomicArtifact.class)) {
			return ATOMIC.getDisplayName();
		} else if(clazz.equals(BuildingArtifact.class)) {
			return BUILDING.getDisplayName();
		} else if(clazz.equals(CoinArtifact.class)) {
			return COIN.getDisplayName();
		} else if(clazz.equals(GemArtifact.class)) {
			return GEM.getDisplayName();
		} else if(clazz.equals(SculptureArtifact.class)) {
			return SCULPTURE.getDisplayName();
		} else if(clazz.equals(SiteArtifact.class)) {
			return SITE.getDisplayName();
		} else if (clazz.equals(VaseArtifact.class)) {
			return VASE.getDisplayName();
		}
		return "";
	}

	public static List<ArtifactType> getArtifactTypes() {
		List<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		artifactTypes.add(ArtifactType.BUILDING);
		artifactTypes.add(ArtifactType.COIN);
		artifactTypes.add(ArtifactType.GEM);
		artifactTypes.add(ArtifactType.SCULPTURE);
		artifactTypes.add(ArtifactType.SITE);
		artifactTypes.add(ArtifactType.VASE);
		return artifactTypes;
	}

	/* Comparable */
	public int compareTo(Object o) {
		if (!(o instanceof ArtifactType)) {
			throw new IllegalArgumentException();
		} 
		ArtifactType aotype = (ArtifactType)o;
		return this.displayName.compareTo(aotype.getDisplayName());
	}

}
