/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
*/
package perseus.artarch;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;

import perseus.document.Renderer;
import perseus.ie.entity.Entity;

/**
 * An Artifact encapsulates metadata pertaining to a physical Art and Archaeology object.    
 */
public interface Artifact extends Entity, Serializable {                                                

    public String getDocumentaryReferences();
    public void setDocumentaryReferences(String documentaryReferences);

    public String getName();
    public void setName(String name);

    public String getType();
    public void setType(String type);

    public String getLocation();
    public void setLocation(String location);

    public String getSummary();
    public void setSummary(String summary);

    public String getPerseusVersion();
    public void setPerseusVersion(String perseusVersion);
    
    public String getEnteredBy();
    public void setEnteredBy(String enteredBy);

    public String getSourcesUsed();
    public void setSourcesUsed(String sourcesUsed);
    
    public String getOtherBibliography();
    public void setOtherBibliography(String otherBibliography);

    public Node toXML(Map model, String rootName, HttpServletRequest req, HttpServletResponse res) 
	throws Exception;

    public Map<String,String> getPropertyDisplayNames();
    public Map<String,String> getPropertyNames();

    public String getPropertyDisplayName(String property);
    public String getPropertyName(String propertyDisplayName);
    
    public Map<String, String> getTableProperties(Renderer renderer);
    public Map<String, String> getParagraphProperties(Renderer renderer);
}
