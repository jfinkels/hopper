package perseus.morph;

import perseus.document.Query;
/**
 * An interface that allows one to associate statistics with a particular
 * entity.
*/

interface Statistical{


    public void loadFrequencies(Query query);

    /**
     * Get the minimum frequency for this entity in the Query returned by
     * getQuery().
     *
     * @return the minimum frequency
     */
    public int getMinimumFrequency();

    /**
     * Get the maximum frequency for this entity in the Query returned by
     * getQuery().
     *
     * @return the maximum frequency
     */
    public int getMaximumFrequency();

    /**
     * Get the weighted frequency for this entity in the Query returned by 
     * getQuery().
     *
     * @return the weighted frequency
     */
    public double getWeightedFrequency();

    /**
     * Get the keyword score for this entity in the Query returned by
     * getQuery().
     * 
     * @return the keyword score
     */
    public double getKeywordScore();

}

