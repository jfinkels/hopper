/**
 * @GPL@
 * Last Modified: @TIME@
 *
 **/
package perseus.artarch.image;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.DOMOutputter;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;
import org.w3c.dom.Node;


public class SimpleImg extends AbstractXsltView implements Img {
	
	private static Logger logger = Logger.getLogger(SimpleImg.class);

    private Integer id = new Integer(-1);
    
    //'image' attributes
    private String archiveNumber;
    private String caption;
    private String credits;
    private String date;
    private String series;
    private String seq;
    private String enteredBy;
    private String lon;
    private String lat;
    private int status;
    private String canonical;
    private String sourceId;
    private String format;
    private String qtName;
    private String qtMovieName;
    private String qtController;
    private String qtAutoplay;
    private String vrBcolor;
    private String playbackWidth;
    private String playbackHeight;
    
    private String thumbURL;
    private String fullURL;
    private boolean isRestricted;

	public SimpleImg() {}

    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public String getArchiveNumber() {
    	return archiveNumber;
    }

    public void setArchiveNumber(String archiveNumber) {
    	this.archiveNumber = archiveNumber;
    }

    public String getCaption() {
    	return caption;
    }

    public void setCaption(String caption) {
    	this.caption = caption;
    }

    public String getCredits() {
    	return credits;
    }

    public void setCredits(String credits) {
    	this.credits = credits;
    }

    public String getDate() {
    	return date;
    }

    public void setDate(String date) {
    	this.date = date;
    }

    public String getSeries() {
    	return series;
    }

    public void setSeries(String series) {
    	this.series = series;
    }

    public String getSeq() {
    	return seq;
    }

    public void setSeq(String seq) {
    	this.seq = seq;
    }

    public String getEnteredBy() {
    	return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
    	this.enteredBy = enteredBy;
    }

    public String getLon() {
    	return lon;
    }

    public void setLon(String lon) {
    	this.lon = lon;
    }

    public String getLat() {
    	return lat;
    }

    public void setLat(String lat) {
    	this.lat = lat;
    }

    public int getStatus() {
    	return status;
    }

    public void setStatus(int status) {
    	this.status = status;
    }

    public String getCanonical() {
    	return canonical;
    }

    public void setCanonical(String canonical) {
    	this.canonical = canonical;
    }

    public String getSourceId() {
    	return sourceId;
    }

    public void setSourceId(String sourceId) {
    	this.sourceId = sourceId;
    }

    public String getFormat() {
    	return format;
    }

    public void setFormat(String format) {
    	this.format = format;
    }

    public String getQtName() {
    	return qtName;
    }

    public void setQtName(String qtName) {
    	this.qtName = qtName;
    }

    public String getQtMovieName() {
    	return qtMovieName;
    }

    public void setQtMovieName(String qtMovieName) {
    	this.qtMovieName = qtMovieName;
    }

    public String getQtController() {
    	return qtController;
    }

    public void setQtController(String qtController) {
    	this.qtController = qtController;
    }

    public String getQtAutoplay() {
    	return qtAutoplay;
    }

    public void setQtAutoplay(String qtAutoplay) {
    	this.qtAutoplay = qtAutoplay;
    }

    public String getVrBcolor() {
    	return vrBcolor;
    }

    public void setVrBcolor(String vrBcolor) {
    	this.vrBcolor = vrBcolor;
    }

    public String getPlaybackWidth() {
    	return playbackWidth;
    }

    public void setPlaybackWidth(String playbackWidth) {
    	this.playbackWidth = playbackWidth;
    }

    public String getPlaybackHeight() {
    	return playbackHeight;
    }

    public void setPlaybackHeight(String playbackHeight) {
    	this.playbackHeight = playbackHeight;
    }
    
    public String getThumbURL() {
		return thumbURL;
	}

	public void setThumbURL() {
		thumbURL = "http://images.perseus.tufts.edu/images/thumbs/" + getArchiveNumLocation();
	}

	public String getFullURL() {
		return fullURL;
	}

	public void setFullURL() {
		fullURL = "http://images.perseus.tufts.edu/images/" + getArchiveNumLocation();
		
		if (status != 1) {
			//then get URL from restricted directory
			fullURL = "http://images.perseus.tufts.edu/restricted/" + getArchiveNumLocation();
		}
	}
	
	private String getArchiveNumLocation() {
		String[] archiveNums = archiveNumber.split(":");
		String archiveID = archiveNums[2];
		String[] archiveSplit = archiveID.split("\\.");
		String imageDirectory = archiveSplit[0] + "." + archiveSplit[1] + ".";
		
		// need to deal with archive numbers with letters like Perseus:image:1990.33.0001a
		String archive = archiveSplit[2].replaceAll("[a-zA-Z]", "");
		int num = Integer.parseInt(archive);
		int dirNum = (num / 500) + 1;
		imageDirectory += Integer.toString(dirNum);
		imageDirectory += "/"+archiveID;
		
		return imageDirectory;
	}
	
	public boolean getIsRestricted() {
		return isRestricted;
	}

	public void setRestricted(boolean isRestricted) {
		this.isRestricted = isRestricted;
	}
	
	public void setIsRestricted(HttpServletRequest request) {
		if (status != 1) {
			isRestricted = true;
		}
		
		//check IP to only allow Tufts IPs access to restricted images
		String requestIP = request.getRemoteAddr();
		if (requestIP == null) {
		    requestIP = request.getHeader("X-Forwarded-For");
		}
		if (requestIP != null) {
		    if (requestIP.startsWith("130.64")) {
			isRestricted = false;
		    }		
		}
		// if IP is still null, ignore for now
	}

	public void setURLs() {
		setThumbURL();
		setFullURL();
	}

    public List<Method> getGetterMethods() {
        List<Method> result = new ArrayList<Method>();
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                try {
                    result.add(method);
                }  catch (Exception e) {
                    logger.warn("Problem adding getter methods to list "+e);
                }
            }//then we have a getter
        }
        return result;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);

        //determine fields declared in this class including superclasses
        List<Method> methods = getGetterMethods();
        for (int i=0; i < methods.size();  i++) {
            Method method = (Method)methods.get(i);
            result.append("  ");
            try {
                result.append( method.getName() );
                result.append(": ");

		//casting to java.lang.Object[] necessary to supress a warning
                //requires access to private field:
                result.append(method.invoke(this, (java.lang.Object[]) null));
            } catch (InvocationTargetException ite) {
                logger.warn(ite);
            } catch (Exception e) {
               logger.warn(e);
            }
            result.append(newLine);
        }
        result.append("}");
        return result.toString();
    }

    /*
     * Serialize this SimpleImg as XML
     */
    public Element toXML() throws Exception {

    	Namespace artarch = Namespace.getNamespace("http://www.perseus.tufts.edu/artarch");
    	Element artifactImg = new Element("ArtifactImage", artarch);

    	Element archiveNumber = new Element("archiveNumber", artarch);
    	archiveNumber.setText(getArchiveNumber());
    	artifactImg.addContent(archiveNumber);

    	Element caption = new Element("caption", artarch);
    	caption.setText(getCaption());
    	artifactImg.addContent(caption);

    	Element credits = new Element("credits", artarch);
    	credits.setText(getCredits());
    	artifactImg.addContent(credits);

    	Element date = new Element("date", artarch);
    	date.setText(getDate());
    	artifactImg.addContent(date);

    	Element series = new Element("series", artarch);
    	series.setText(getSeries());
    	artifactImg.addContent(series);

    	Element seq = new Element("seq", artarch);
    	seq.setText(getSeq());
    	artifactImg.addContent(seq);

    	Element enteredBy = new Element("enteredBy", artarch);
    	enteredBy.setText(getEnteredBy());
    	artifactImg.addContent(enteredBy);

    	Element lon = new Element("lon", artarch);
    	lon.setText(getLon());
    	artifactImg.addContent(lon);

    	Element lat = new Element("lat", artarch);
    	lat.setText(getLat());
    	artifactImg.addContent(lat);

    	Element status = new Element("status", artarch);
    	status.setText(Integer.toString(getStatus()));
    	artifactImg.addContent(status);

    	Element canonical = new Element("canonical", artarch);
    	canonical.setText(getCanonical());
    	artifactImg.addContent(canonical);

    	Element sourceId = new Element("sourceId", artarch);
    	sourceId.setText(getSourceId());
    	artifactImg.addContent(sourceId);

    	Element format = new Element("format", artarch);
    	format.setText(getFormat());
    	artifactImg.addContent(format);

    	Element qtName = new Element("qtName", artarch);
    	qtName.setText(getQtName());
    	artifactImg.addContent(qtName);

    	Element qtMovieName = new Element("qtMovieName", artarch);
    	qtMovieName.setText(getQtMovieName());
    	artifactImg.addContent(qtMovieName);

    	Element qtController = new Element("qtController", artarch);
    	qtController.setText(getQtController());
    	artifactImg.addContent(qtController);

    	Element qtAutoplay = new Element("qtAutoplay", artarch);
    	qtAutoplay.setText(getQtAutoplay());
    	artifactImg.addContent(qtAutoplay);

    	Element vrBcolor = new Element("vrBcolor", artarch);
    	vrBcolor.setText(getVrBcolor());
    	artifactImg.addContent(vrBcolor);

    	Element playbackWidth = new Element("playbackWidth", artarch);
    	playbackWidth.setText(getPlaybackWidth());
    	artifactImg.addContent(playbackWidth);

    	Element playbackHeight = new Element("playbackHeight", artarch);
    	playbackHeight.setText(getPlaybackHeight());
    	artifactImg.addContent(playbackHeight);

    	return artifactImg;
    }

    protected Source createXsltSource(Map model, String rootName, HttpServletRequest req,
    		HttpServletResponse res) throws Exception {
    	SimpleImg si = (SimpleImg)((Map)model.get("model")).get("artifactImg");
    	Node siXML = new DOMOutputter().output(new org.jdom.Document(si.toXML()));
    	return new DOMSource(siXML);
    }
    
    public Node toXML(Map model, String rootName, HttpServletRequest req,
    		HttpServletResponse res) throws Exception {
    	return ((DOMSource) createXsltSource(model, rootName, req, res)).getNode();
    }

}
