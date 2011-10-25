package perseus.artarch;

public class ArtifactKeyword {
	//Hibernate id
	private int id;
	private Artifact artifact;
	private String artifactType;
	private String keyclass;
	private String keyword;
	
	public ArtifactKeyword() {}
	
	public ArtifactKeyword(Artifact artifact, String artifactType, String keyclass, String keyword) {
		this.artifact = artifact;
		this.artifactType = artifactType;
		this.keyclass = keyclass;
		this.keyword = keyword;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Artifact getArtifact() {
		return artifact;
	}
	
	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}
	
	public String getArtifactType() {
		return artifactType;
	}
	
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}
	
	public String getKeyclass() {
		return keyclass;
	}
	
	public void setKeyclass(String keyclass) {
		this.keyclass = keyclass;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String toString() {
		return artifact.getDisplayName()+" ("+artifactType+"): "+keyclass+" - "+keyword;
	}
}
