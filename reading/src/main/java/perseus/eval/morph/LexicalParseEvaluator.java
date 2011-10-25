package perseus.eval.morph;

import java.util.*;

import org.apache.log4j.Logger;

import perseus.morph.Lemma;
import perseus.morph.Parse;

/**
 * Base class for parse evaluators that only distinguish between different
 * lemmas--lemma-frequency analyzers, for example.
 */
public abstract class LexicalParseEvaluator extends ParseEvaluator {

    private static Logger logger = Logger.getLogger(LexicalParseEvaluator.class);

    private Set<Lemma> lemmas;

    protected LexicalParseEvaluator() {
	super();
    }

    public Map<Parse,Number> evaluateParses(
	    Map<Lemma,List<Parse>> parses) {
	
	if (parses == null || parses.size() == 1) {
	    return new HashMap<Parse,Number>();
	}

	lemmas = parses.keySet();
	return super.evaluateParses(parses);
    }

    protected final Map<Parse, Number> evaluate(Collection<Parse> parses) {
	Map<Lemma,Number> lemmaScores = evaluateLemmas(lemmas);
	Map<Parse,Number> parseScores = new HashMap<Parse,Number>();
	
	for (Parse parse : parses) {
	    Lemma lemma = parse.getLemma();

	    if (lemmas.contains(lemma)) {
		Number lemmaScore = lemmaScores.get(lemma);
		parseScores.put(parse, lemmaScore);
	    } else {
		logger.warn("No lemma found for parse " + parse);
	    }
	}

	return parseScores;
    }

    protected abstract Map<Lemma,Number> evaluateLemmas(Set<Lemma> lemmas);
}
