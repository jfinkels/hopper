package perseus.ie.freq;

import perseus.ie.entity.Entity;
import perseus.language.Language;

public class FormDocumentFrequency extends DocumentFrequency implements
	FormBased {
    
    private String form;
    private Language language;

    public String getForm() {
	return form;
    }

    public Language getLanguage() {
	return language;
    }

    public void setForm(String f) {
	form = f;
    }

    public void setLanguage(Language l) {
	language = l;
    }

	@Override
	public Entity getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEntity(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
