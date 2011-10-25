/**
 * @GPL@
 * Last Modified: @TIME@
 *
 * Interface that all ImgDAOs must support
 * This DAO is based upon the sample code found in Sun's article on the DataAccessObject pattern.  http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html
 * References to patterns are from "Patterns of Enterprise Appliation Architecture", Martin Fowler.
 */
package perseus.artarch.image.dao;

import java.util.List;

import perseus.artarch.image.Img;

public interface ImgDAO {

    // Returns -1 if the insert failed, returns the id of the Img otherwise
    public int insertImg(Img image);
    public boolean deleteImg(Img image);
    public boolean deleteAllImgs();
    public List findImg(Img image);
    public List findImg(Img image, int offset, int maxResults);
    public Img getImg(String archiveNumber);
    public boolean updateImg(Img image);
    public int getTotalHits(Img image);
    public String getCaption(String archiveNumber);
}

