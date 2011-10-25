package perseus.morph.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.morph.Parse;
import perseus.util.DAO;

/**
 * Data Access Object for manipulating Parse instances.
 */

public interface ParseDAO extends DAO<Parse> {
    public void beginTransaction();
    public void endTransaction();

    public void save(Parse parse);
    public void delete(Parse parse);

    /**
     * Deletes all Parses whose lemmas are in the given language.
     */
    public void deleteByLanguage(Language language);

    /**
     * Returns all parses matching `form` with lemmas in `languageCode`.
     */
    public List<Parse> getByForm(String form, String languageCode);

    /**
     * Returns all parses matching `form` with lemmas in `languageCode`,
     * possibly ignoring accents or matching case.
     */
    public List<Parse> getByForm(String form, String languageCode,
	    boolean ignoreAccents, boolean matchCase);
    
    /**
     * Returns all parses matching `form` with lemmas in `language`.
     */
    public List<Parse> getByForm(String form, Language language);

    /**
     * Returns all parses matching `form` with lemmas in `language`,
     * possibly ignoring accents or matching case.
     */
    public List<Parse> getByForm(String form, Language language,
	    boolean ignoreAccents, boolean matchCase);

    /**
     * Returns all parses for the given lemma.
     */
    public List<Parse> getByLemma(Lemma lemma);

    /**
     * Returns all lemmas in `language` that have a parse matching
     * `lookupForm`.
     */
    public List<Lemma> getLemmasByForm(String lookupForm, Language language);

    /**
     * Lookups all the forms in `forms` at once and returns the headwords of
     * all matching lemmas.
     */
    public Set<String> getAllForms(Collection<String> forms, Language language);

    /**
     * Lookups all the forms in `forms` at once and returns all matching
     * lemmas.
     */
    public Set<Lemma> getAllLemmas(Collection<String> forms, Language language);
    
    /**
     * Checks to see if the parameter parse matches any in the data source for `form`,
     * `morph_code` and `lemma_id`
     */
    public boolean exists(Parse parse);
}
