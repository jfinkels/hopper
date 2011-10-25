package perseus.eval.morph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import perseus.ie.freq.EntityDocumentFrequency;
import perseus.ie.freq.dao.HibernateFrequencyDAO;
import perseus.morph.Lemma;

/**
 * Scores parses based on their lemmas' frequencies.
 *
 */

public class WordFrequencyEvaluator extends LexicalParseEvaluator {

	private String documentID;

	public WordFrequencyEvaluator(String docID) {
		documentID = docID;
	}

	public Map<Lemma,Number> evaluateLemmas(Set<Lemma> lemmas) {
		HibernateFrequencyDAO freqDAO =	new HibernateFrequencyDAO(EntityDocumentFrequency.class);
		Map<Lemma,Number> lemmaScores = new HashMap<Lemma,Number>();

		for (Lemma lemma : lemmas) {
			List<Object[]> counts = freqDAO.getDocumentFrequenciesWithCounts(lemma, documentID);
			if (!counts.isEmpty()) {
				Object[] count = counts.get(0);
				EntityDocumentFrequency f = (EntityDocumentFrequency) count[0];
				double weightedFrequency = f.getWeightedFrequency();
				lemmaScores.put(lemma, new Double(weightedFrequency));
			} else {
				lemmaScores.put(lemma, new Double(0.0));
			}
		}

		return lemmaScores;
	}

	public String getDescription() {
		return "Word-frequency evaluator";
	}

	public String getLongDescription() {
		return "Scores parses based on how often the dictionary word appears "
		+ "in the Perseus corpus. Only used when a given form could be "
		+ "from more than one possible word.";
	}
}
