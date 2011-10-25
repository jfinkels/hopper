package perseus.morph;

import java.util.*;
import perseus.document.Query;

/**
 * Anything compared must implement Statistical
 */
public class StatisticalComparator implements Comparator {
    
    public static final String MIN_FREQ = "min_freq";
    public static final String MAX_FREQ = "max_freq";
    public static final String WEIGHTED_FREQ = "weighted_freq";
    public static final String KEYWORD_SCORE = "keyword_score";
    private Query query = null;

    String sortKey = KEYWORD_SCORE;

    private StatisticalComparator() {

    }

    private StatisticalComparator(String sk, Query query){
	if(sk != null){
	    sortKey = sk;
	}
	this.query = query;
    }

    /**
     * Get the sort key value
     */
    public String getSortKey(){
	return sortKey;
    }

    /**
     * Get a Comparator that uses the appropriate sort field
     *
     * @param field Corresponds to one of the static constant sort fields
     * @param return a Statistical comparator
     */
    public static StatisticalComparator getComparator(String sk, Query query){
	return new StatisticalComparator(sk, query);
    }

    /**
     * Precondition:  The statQuery string must be specified in the Statistical entity
     *
     * @param o1 The first Statistical to compare
     * @param o2 The second Statistical to compare
     * @return -1, 0, 1 if o1 is less than, equal to , or greater than o2
     */
    public int compare(Object o1, Object o2){
	if(! (o1 instanceof Statistical) ||
	   ! (o2 instanceof Statistical)){
	    throw new ClassCastException("Unable to compare object");
	}

	Statistical entity1 = (Statistical) o1;
	Statistical entity2 = (Statistical) o2;

	if(sortKey.equals(this.MIN_FREQ)){
	    return entity1.getMinimumFrequency() - entity2.getMinimumFrequency();
	}else if (sortKey.equals(this.MAX_FREQ)){
	    return entity1.getMaximumFrequency() - entity2.getMaximumFrequency();
	}else if (sortKey.equals(this.WEIGHTED_FREQ)){
	    return Double.compare(entity1.getWeightedFrequency(), entity2.getWeightedFrequency());
	}else{
	    return Double.compare(entity1.getKeywordScore(), entity2.getKeywordScore());
	}	
    }


}
