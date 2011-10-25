package perseus.ie.freq;

import java.text.DecimalFormat;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import perseus.ie.entity.Entity;

/**
 * Frequency subclass measuring occurrences of an entity in a particular
 * document.
 */

public class EntityDocumentFrequency extends DocumentFrequency implements
EntityBased {

	public EntityDocumentFrequency() {}

	public EntityDocumentFrequency(Entity entity, String documentID) {
		super();
		setEntity(entity);
		setDocumentID(documentID);
	}

	private Entity entity;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity e) {
		entity = e;
	}

	public String getForm() {
		return entity.getAuthorityName();
	}
	
	public String toXML() {
		Element frequency = new Element("frequency");

		frequency.addContent(entity.toXMLElement());
		
		frequency.addContent(new Element("maxFrequency").addContent(String.valueOf(getMaxFrequency())));
		frequency.addContent(new Element("minFrequency").addContent(String.valueOf(getMinFrequency())));
		frequency.addContent(new Element("weightedFrequency").addContent(new DecimalFormat("0.##").format(getWeightedFrequency())));
		frequency.addContent(new Element("keyTermScore").addContent(new DecimalFormat("0.####").format(getTfidf())));
		
		return new XMLOutputter(Format.getPrettyFormat()).outputString(frequency);
	}
}
