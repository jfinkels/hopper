package perseus.ie.freq;

import java.util.Comparator;

import perseus.ie.entity.Entity;

/**
 * Abstract class representing frequency information. Subclasses constrain
 * this class to particular objects (entities or forms) within particular
 * scopes (document or chunk).
 */

public abstract class Frequency implements Comparable<Frequency> {
	
    public enum FrequencyComparator implements Comparator<Frequency> {
        MAX_FREQ(true) {
            protected int doCompare(Frequency et1, Frequency et2) {
                int c1 = et1.getMaxFrequency();
                int c2 = et2.getMaxFrequency();
                return (c1 == c2) ? 0 : (c1 < c2 ? -1 : 1);
            }			
        },
            MIN_FREQ(true) {
                protected int doCompare(Frequency et1, Frequency et2) {
                    int c1 = et1.getMinFrequency();
                    int c2 = et2.getMinFrequency();
                    return (c1 == c2) ? 0 : (c1 < c2 ? -1 : 1);
                }				
            },
            WEIGHTED_FREQ(true) {
                protected int doCompare(Frequency et1, Frequency et2) {
                    double c1 = et1.getWeightedFrequency();
                    double c2 = et2.getWeightedFrequency();
                    return (c1 == c2) ? 0 : (c1 < c2 ? -1 : 1);
                }

            },
            KEYWORD_SCORE(true) {
                protected int doCompare(Frequency et1, Frequency et2) {
                    double c1 = et1.getTfidf();
                    double c2 = et2.getTfidf();
                    return (c1 == c2) ? 0 : (c1 < c2 ? -1 : 1);
                }			
            },
            FORM(false) {
                protected int doCompare(Frequency et1, Frequency et2) {
                    return (et1.getForm().compareTo(et2.getForm()));
                }

            },
            POSITION(false) {
                protected int doCompare(Frequency et1, Frequency et2) {
                    int fp1 = et1.getFirstPosition();
                    int fp2 = et2.getFirstPosition();
                    return (fp1 == fp2) ? 0 : (fp1 < fp2 ? -1 : 1);
                }			
            };

        private boolean reverseSorting = true;

        private FrequencyComparator(boolean rs) {
            reverseSorting = rs;
        }

        public int compare(Frequency et1, Frequency et2) {
            int result = doCompare(et1, et2);
            return reverseSorting ? -result : result;
        }

        protected abstract int doCompare(Frequency et1, Frequency et2);
    }

    /** Hibernate-based ID */
    private Integer id;

    private int minFrequency;
    private int maxFrequency;
    private double weightedFrequency;
    private double keywordScore;
    private int firstPosition;
    private double termFreq;
    private double tfidf;
    
    public double getTfidf() {
		return tfidf;
	}
	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}
	public double getTermFreq() {
		return termFreq;
	}
	public void setTermFreq(double termFreq) {
		this.termFreq = termFreq;
	}
	public int getFirstPosition() {
        return firstPosition;
    }
    public void setFirstPosition(int firstPosition) {
        this.firstPosition = firstPosition;
    }
    public double getKeywordScore() {
        return keywordScore;
    }
    public void setKeywordScore(double keywordScore) {
        this.keywordScore = keywordScore;
    }
    public int getMaxFrequency() {
        return maxFrequency;
    }
    public void setMaxFrequency(int maxFrequency) {
        this.maxFrequency = maxFrequency;
    }
    public int getMinFrequency() {
        return minFrequency;
    }
    public void setMinFrequency(int minFrequency) {
        this.minFrequency = minFrequency;
    }
    public double getWeightedFrequency() {
        return weightedFrequency;
    }
    public void setWeightedFrequency(double weightedFrequency) {
        this.weightedFrequency = weightedFrequency;
    }
    
    public int compareTo(Frequency f) {
	int maxResult = getMaxFrequency() - f.getMaxFrequency();
	if (maxResult != 0) return maxResult;
	
	return getMinFrequency() - f.getMinFrequency();
    }

    public void count() {
	count(1);
    }
    public void count(int increment) {
	maxFrequency += increment;
	if (increment == 1) minFrequency += increment;
	weightedFrequency += (double) 1 / increment;
    }
    
    public void add(Frequency addend) {
	maxFrequency += addend.getMaxFrequency();
	minFrequency += addend.getMinFrequency();
	weightedFrequency += addend.getWeightedFrequency();
    }
    
    public abstract String getForm();
    public abstract String getDocumentID();
    public abstract Entity getEntity();
    public abstract void setEntity(Entity e);
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public String toString() {
	return String.format("%s -> [%d/%d/%.2f]",
		getForm(), getMaxFrequency(), getMinFrequency(),
		getWeightedFrequency());
    }
    
    public abstract String toXML();
}
