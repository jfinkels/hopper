package perseus.search.nu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import perseus.language.Language;
import perseus.morph.Lemma;
import perseus.morph.Lemmatizer;
import perseus.util.Range;

public class LemmaSearcher implements Searcher {

    private List<Language> targetLanguages = new ArrayList<Language>();

    public SearchResults search(String keyword, Range<Integer> range) {
	Set<Lemma> matchingLemmas = new HashSet<Lemma>();
	for (Language language : targetLanguages) {
	    if (!language.getHasMorphData()) continue;
	    for (String token : keyword.split("\\s+")) {
		matchingLemmas.addAll(Lemmatizer.getLemmas(token, language.getCode()));
	    }
	}

	SearchResults<LemmaSearchResult> results =
	    new SearchResults<LemmaSearchResult>();

	results.setIdentifier(keyword);
	for (Lemma lemma : matchingLemmas) {
	    // load chunks now, not after session has closed
	    lemma.getLexiconChunks();
	    results.add(new LemmaSearchResult(lemma));
	}

	return results;
    }

    public List<Language> getTargetLanguages() {
	return targetLanguages;
    }

    public void setTargetLanguages(List<Language> targetLanguages) {
	this.targetLanguages = targetLanguages;
    }
    
    public void setTargetLanguage(Language targetLanguage) {
	targetLanguages = new ArrayList<Language>();
	targetLanguages.add(targetLanguage);
    }
}
