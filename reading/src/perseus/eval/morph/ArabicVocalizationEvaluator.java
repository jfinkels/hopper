package perseus.eval.morph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import perseus.morph.Parse;

/**
 * ParseEvaluator subclass that gives to priority to parses with vocalizations
 * that match the original
 *
 */

public class ArabicVocalizationEvaluator extends ParseEvaluator {
	private static Logger logger = Logger.getLogger(ArabicVocalizationEvaluator.class);

	private String word;

	public ArabicVocalizationEvaluator(String word) {
		this.word=word;
	}

	protected Map<Parse, Number> evaluate(Collection<Parse> parses) {

		Map<Parse,Number> output = new HashMap<Parse,Number>();

		for (Parse parse : parses) {
			String form=parse.getExpandedForm();

			double editDistance = getEditDistance(form, word);

			output.put(parse, editDistance);
		}

		return output;
	}

	public double getEditDistance(String parse, String original) {
		int parse_len=parse.length()+1;
		int original_len=original.length()+1;

		int ins_cost=1;
		int del_cost=1;
		int sub_cost=2;

		int[][] matrix=new int[parse_len][original_len];

		for (int i=0; i<parse_len; i++) {
			for (int j=0; j<original_len; j++) {	
				matrix[i][j]=0;
			}
		}

		for (int i=1; i<parse_len; i++) {
			for (int j=1; j<original_len; j++) {

				int ins=matrix[i-1][j] + ins_cost;
				int del=matrix[i][j-1] + del_cost;
				int sub=(parse.charAt(i-1) == original.charAt(j-1) ? matrix[i-1][j-1] : matrix[i-1][j-1] + sub_cost);

				int low=(ins < del ? ins : del);
				int lowest=(low < sub ? low : sub);

				matrix[i][j]=lowest;
			}
		}
		logger.debug(parse + "\t" + original);
		for (int i=1; i<parse_len; i++) {
			for (int j=1; j<original_len; j++) {
				logger.debug(matrix[i][j] + "\t");
			}
			logger.debug("");
		}

		double edit_distance = matrix[parse_len-1][original_len-1] == 0 ? 0.0001 : ((double) matrix[parse_len-1][original_len-1]);
		return 1/edit_distance;
	}

	public String getDescription() {
		return "Arabic vocalization evaluator";
	}

	public String getLongDescription() {
		return "Scores parses based on how close the known vocalization is to the parsed one.";
	}
}
