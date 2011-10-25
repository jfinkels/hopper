/**
 * @GPL@
 * Last Modified: @TIME@
 *
 * This should eventually replace Images.java
 * Defines an interface which consists of member variables found in the
 *  'images' postgres table
 **/
package perseus.artarch.image;

public interface Img {

    public Integer getId();
    public void setId(Integer id);
   
    // getter and setter methods for 'image' attributes
    public String getArchiveNumber();
    public void setArchiveNumber(String archiveNumber);

    public String getCaption();
    public void setCaption(String caption);

    public String getCredits();
    public void setCredits(String credits);

    public String getDate();
    public void setDate(String date);

    public String getSeries();
    public void setSeries(String series);

    public String getSeq();
    public void setSeq(String seq);

    public String getEnteredBy();
    public void setEnteredBy(String enteredBy);
    
    public String getLon();
    public void setLon(String lon);

    public String getLat();
    public void setLat(String lat);

    public int getStatus();
    public void setStatus(int status);

    public String getCanonical();
    public void setCanonical(String canonical);

    public String getSourceId();
    public void setSourceId(String sourceId);

    public String getFormat();
    public void setFormat(String format);

    public String getQtName();
    public void setQtName(String qtName);

    public String getQtMovieName();
    public void setQtMovieName(String qtMovieName);

    public String getQtController();
    public void setQtController(String qtController);

    public String getQtAutoplay();
    public void setQtAutoplay(String qtAutoplay);

    public String getVrBcolor();
    public void setVrBcolor(String vrBcolor);

    public String getPlaybackWidth();
    public void setPlaybackWidth(String playbackWidth);

    public String getPlaybackHeight();
    public void setPlaybackHeight(String playbackHeight);
   
}
