package perseus.eval.morph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import perseus.morph.Lemma;
import perseus.morph.Parse;

public class ParseSelector {

    private List<ParseEvaluator> parseEvaluators =
	new ArrayList<ParseEvaluator>();

    public ParseSelector() {
    }

    public ParseVotingResults evaluate(Map<Lemma,List<Parse>> parses) {

	ParseVotingResults votingResult = new ParseVotingResults();

	int evaluatorsUsed = 0;

	for (ParseEvaluator evaluator : parseEvaluators) {
	    Map<Parse, Number> results = evaluator.evaluateParses(parses);

	    evaluatorsUsed++;
	    for (Parse parse : results.keySet()) {
		votingResult.recordScore(parse, evaluator,
					results.get(parse).doubleValue());
	    }
	}

	return votingResult;
    }

    public void addEvaluator(ParseEvaluator eval) {
	parseEvaluators.add(eval);
    }

    public class ParseVotingResults {

	Map<Parse,Double> totalScores =
	    new HashMap<Parse,Double>();
	Map<ParseEvaluator,Map<Parse,Double>> individualScores =
	    new HashMap<ParseEvaluator,Map<Parse,Double>>();
	Set<ParseEvaluator> evaluators = new HashSet<ParseEvaluator>();
	Set<Parse> parses = new HashSet<Parse>();

	Parse winner;

	public static final double NO_SCORE = -1.0;

	private boolean totalNeedsUpdating;

	public ParseVotingResults() {
	    totalNeedsUpdating = false;
	}

	public Iterator<ParseEvaluator> evaluatorIterator() {
	    return evaluators.iterator();
	}

	public Iterator<Parse> parseIterator() {
	    return parses.iterator();
	}

	public int evaluatorCount() {
	    return evaluators.size();
	}

	public int parseCount() {
	    return parses.size();
	}

	public void recordScore(Parse parse, ParseEvaluator evaluator,
		double score) {

	    if (!evaluators.contains(evaluator)) {
		evaluators.add(evaluator);
	    }

	    if (!parses.contains(parse)) {
		parses.add(parse);
	    }

	    Map<Parse,Double> evaluatorScores;
	    if (individualScores.containsKey(evaluator)) {
		evaluatorScores = individualScores.get(evaluator);
	    } else {
		evaluatorScores = new HashMap<Parse,Double>();
		individualScores.put(evaluator, evaluatorScores);
	    }

	    evaluatorScores.put(parse, score);

	    totalNeedsUpdating = true;
	}

	public boolean containsParse(Parse parse) {
	    return parses.contains(parse);
	}

	public boolean containsEvaluator(ParseEvaluator evaluator) {
	    return evaluators.contains(evaluator);
	}

	public boolean containsScore(Parse parse, ParseEvaluator evaluator) {
	    return (getScore(parse, evaluator) != NO_SCORE);
	}

	public double getScore(Parse parse, ParseEvaluator evaluator) {

	    if (!individualScores.containsKey(evaluator)) {
		return NO_SCORE;
	    }

	    Map<Parse,Double> evaluatorScores =
		individualScores.get(evaluator);
	    if (!evaluatorScores.containsKey(parse)) {
		return NO_SCORE;
	    }

	    return evaluatorScores.get(parse);
	}

	// This is a hack that will hopefully go away (but probably won't).  We
	// want to weight user votes more heavily than the other, automated
	// processes, so the voting evaluator' confidence for a given parse
	// will be equal to the number of user-votes the parse actually
	// received. For every other evaluator, the confidence will just be
	// equal to 1.
	public double getConfidence(Parse parse, ParseEvaluator evaluator) {
	    if (evaluator instanceof UserVotesEvaluator &&
		getScore(parse, evaluator) > 0) {

		return getScore(parse, evaluator);
	    }

	    return 1.0;
	}

	public double getTotalScore(Parse parse) {
	    if (totalNeedsUpdating) {
		updateTotal();
	    }

	    return ((Double) totalScores.get(parse)).doubleValue();
	}

	public Parse getWinner() {
	    if (totalNeedsUpdating) {
		updateTotal();
	    }

	    return winner;
	}

	private void updateTotal() {
	    totalScores.clear();
	    double highestScore = -1.0;
	    double totalDistance = 0.0;
	    
	    for (Parse currentParse : parses) {		
	    	double runningTotal = 0.0;
	    	double votesReceived = 0.0;

	    	for (ParseEvaluator evaluator : evaluators) {
	    		if (containsScore(currentParse, evaluator)) {
	    			//			double confidence =
	    			//			    getConfidence(currentParse, evaluator);
	    			runningTotal += getScore(currentParse, evaluator);
	    			votesReceived++;
	    		}
	    	}

	    	for (ParseEvaluator evaluator : evaluators) {
	    		if (containsScore(currentParse, evaluator)) {
	    			//			double confidence =
	    			//			    getConfidence(currentParse, evaluator);
	    			runningTotal += getScore(currentParse, evaluator);
	    			votesReceived++;
	    		}
	    	}

	    	double averageScore = runningTotal / votesReceived;
	    	//totalDistance += (averageScore * averageScore);
	    	totalDistance += averageScore;

	    	if (averageScore > highestScore) {
	    		highestScore = averageScore;
	    		winner = currentParse;
	    	}
	    	totalScores.put(currentParse, new Double(averageScore));
	    }

	    //totalDistance = Math.sqrt(totalDistance);

	    //basically there were no votes, so we have no scores, so don't divide by 0!!
	    if (totalDistance > 0.0) {
	    	for (Parse currentParse : totalScores.keySet()) {
	    		double score = totalScores.get(currentParse);

	    		totalScores.put(currentParse, score/totalDistance);
	    	}
	    }
	}

	public Set<ParseEvaluator> getEvaluators() {
		return evaluators;
	}

	public void setEvaluators(Set<ParseEvaluator> evaluators) {
		this.evaluators = evaluators;
	}

	public Set<Parse> getParses() {
		return parses;
	}

	public void setParses(Set<Parse> parses) {
		this.parses = parses;
	}
    }

}
