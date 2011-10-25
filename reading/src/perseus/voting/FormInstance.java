package perseus.voting;

import java.lang.Integer;

import perseus.document.Query;

public class FormInstance {

    Query query;
    String form;
    int occurrence;
    String languageCode; // this turns out not to be necessary in practice

    public FormInstance() {}

    public FormInstance(Query query, String form, int which) {
	this.query = query;

	this.form = form;
	this.occurrence = which;
    }

    /**
     * Creates a new FormInstance. Old constructor; should not be used.
     *
     * @deprecated
     */
    public FormInstance(String doc, String subq, String form, String which) {
	this(new Query(doc, subq), form, Integer.parseInt(which));
    }

    public Query getQuery() {
	return query;
    }

    public String getDocument() {
        return query.getDocumentID();
    }

    public String getSubquery() {
        return query.getQuery();
    }

    public String getForm() {
        return form;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public String getLanguageCode() {
	return languageCode;
    }

    public void setQuery(Query q) {
	query = q;
    }

    public void setDocument(String dcmnt) {
	query = new Query(dcmnt, query.getQuery());
    }

    public void setSubquery(String sbqry) {
	query = new Query(query.getDocumentID(), sbqry);
    }

    public void setForm(String frm) {
        form = frm;
    }

    public void setOccurrence(int ccrrnc) {
        occurrence = ccrrnc;
    }

    public void setLanguageCode(String langCode) {
	languageCode = langCode;
    }

    public String toString() {
	return "[" + query + "] " + form + "(" + occurrence + ")";
    }
}
