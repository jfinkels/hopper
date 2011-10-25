/**
 * 
 */
package perseus.artarch.image.dao;

import java.util.List;

import perseus.artarch.image.ImageName;

/**
 * @author rsingh04
 *
 */
public interface ImageNameDAO {
	
	public List getImagesByName(String name);
	public int insertImageName(ImageName in);
	public ImageName getImageName(String archiveNumber);
	public boolean deleteImageNames();

}
