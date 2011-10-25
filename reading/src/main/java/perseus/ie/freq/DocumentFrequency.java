package perseus.ie.freq;

/**
 * Subclass of Frequency for document-based frequencies.
 */
public abstract class DocumentFrequency extends Frequency {
    private String documentID;

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

	public double getMaxPer10K(long totalWords) {
		return ((double) getMaxFrequency() / totalWords) * 10000.0;
	}	

	public double getMinPer10K(long totalWords) {
		return ((double) getMinFrequency() / totalWords) * 10000.0;
	}
}
