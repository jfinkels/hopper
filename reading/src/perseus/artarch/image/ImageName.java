/**
 * 
 */
package perseus.artarch.image;

/**
 * @author rsingh04
 *
 */
public class ImageName {
	
	/**
	 * Hibernate-based id
	 */
	private Integer id;
	/**
	 * Image archive number
	 */
	private String archiveNumber;
	/**
	 * Image's primary name
	 */
	private String primaryName;
	/**
	 * Image's secondary name
	 */
	private String secondaryName;
	/**
	 * Image's tertiary name
	 */
	private String tertiaryName;
	/**
	 * Image's schema - does not appear to be used, currently
	 */
	private String schema;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getArchiveNumber() {
		return archiveNumber;
	}
	
	public void setArchiveNumber(String an) {
		archiveNumber = an;
	}
	
	public String getPrimaryName() {
		return primaryName;
	}
	
	public void setPrimaryName(String pn) {
		primaryName = pn;
	}
	
	public String getSecondaryName() {
		return secondaryName;
	}
	
	public void setSecondaryName(String sn) {
		secondaryName = sn;
	}
	
	public String getTertiaryName() {
		return tertiaryName;
	}
	
	public void setTertiaryName(String tn) {
		tertiaryName = tn;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String s) {
		schema = s;
	}
}
