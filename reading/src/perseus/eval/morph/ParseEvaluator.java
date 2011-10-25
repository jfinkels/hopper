package perseus.eval.morph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import perseus.morph.Lemma;
import perseus.morph.Parse;

public abstract class ParseEvaluator {

    Map<String,String> properties = new HashMap<String,String>();

    protected ParseEvaluator() {
    }
    
    /**
     * This method should return a Map, with Parses as keys and Doubles as
     * values (corresponding to the score each Parse received, between 0 and
     * 1).
     */
    protected abstract Map<Parse, Number> evaluate(Collection<Parse> parses);

    /**
     * This returns a Map with Parses as keys and Doubles (between 0 and 1)
     * as values.
     */
    public Map<Parse,Number> evaluateParses(Map<Lemma,List<Parse>> parses) {
	List<Parse> flattenedParses = flattenParses(parses);
	Map<Parse,Number> parseScores = evaluate(flattenedParses);

	// HACK! Give user votes higher priority--don't normalize their scores.
	if (!(this instanceof UserVotesEvaluator)) {
	    parseScores = normalizeScores(parseScores);
	}

	return parseScores;
    }

    protected Map<Parse,Number> normalizeScores(Map<Parse,Number> scores) {

	Map<Parse,Number> normalizedScores = new HashMap<Parse,Number>();

	//double denominator = calculateEuclideanDistance(scores);
	double denominator = calculateSum(scores);
	if (denominator == 0.0) {
	    for (Parse parse : scores.keySet()) {
		normalizedScores.put(parse, 0.0);
	    }

	    return normalizedScores;
	}
	
	for (Parse parse : scores.keySet()) {
	    if (scores.containsKey(parse)) {
		double parseScore = scores.get(parse).doubleValue();

		double normalizedScore = parseScore / denominator;

		normalizedScores.put(parse, normalizedScore);
	    } else {
		normalizedScores.put(parse, 0.0);
	    }
	}

	return normalizedScores;
    }

    private double calculateSum(Map<Parse,Number> scores) {

	double total = 0.0;

	for (Number score : scores.values()) {
	    total += score.doubleValue();
	}

	return total;
    }

    protected List<Parse> flattenParses(Map<Lemma,List<Parse>> parses) {

	List<Parse> flattenedParses = new ArrayList<Parse>();

	for (List<Parse> lemmaParses : parses.values()) {
	    flattenedParses.addAll(lemmaParses);
	}

	return flattenedParses;
    }

    public String getProperty(String key) {
	if (properties.containsKey(key)) {
	    return properties.get(key);
	}
	return null;
    }
    
    public void setProperty(String key, String value) {
	properties.put(key, value);
    }

    public boolean hasProperty(String key) {
	return properties.containsKey(key);
    }

    public void removeProperty(String key) {
	properties.remove(key);
    }

    public abstract String getDescription();

    public abstract String getLongDescription();
}
