package perseus.ie.freq;

import java.util.Comparator;

import perseus.language.Language;

/**
 * Interface for frequencies dealing with a particular form--not used at this
 * moment.
 */

public interface FormBased {
    public String getForm();
    public Language getLanguage();
    
    public void setForm(String f);
    public void setLanguage(Language l);
    
    public static final Comparator<? extends FormBased> FORM_COMPARATOR =
	new Comparator<FormBased>() {
	
	public int compare(FormBased et1, FormBased et2) {
	    int formResult = et1.getForm().compareTo(et2.getForm());
	    if (formResult != 0) return formResult;

	    return et1.getLanguage().compareTo(et2.getLanguage());
	}
    };
    
    public static final Comparator<? extends FormBased> LANGUAGE_COMPARATOR =
	new Comparator<FormBased>() {
	
	public int compare(FormBased et1, FormBased et2) {
	    int langResult = et1.getLanguage().compareTo(et2.getLanguage());
	    if (langResult != 0) return langResult;
	    
	    return et1.getForm().compareTo(et2.getForm());
	}
    };
}
