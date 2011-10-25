/**
 * 
 */
package perseus.search.nu;

import java.util.Set;

import perseus.document.Query;
import perseus.morph.Lemma;

public class LemmaSearchResult extends SearchResult<Lemma> {
    public LemmaSearchResult(Lemma lemma) {
        setTitle(lemma.getHeadword());
        setIdentifier(lemma.getDisplayForm());
        setContent(lemma);
	setLexiconQueries(lemma.getLexiconQueries());
    }

    private Set<Query> lexiconQueries;

    public Set<Query> getLexiconQueries() { return lexiconQueries; }
    public void setLexiconQueries(Set<Query> queries) {
	lexiconQueries = queries;
    }
}
