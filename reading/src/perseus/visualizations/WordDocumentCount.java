package perseus.visualizations;

import perseus.language.Language;

public class WordDocumentCount {
	/** This instance's Hibernate-based ID */
    private Integer id;

    /** The year being counted */
    private int year;

    /** The word */
    private String word;

    /** The actual count */
    private long documentCount;
    
    /** Language of the word */
    private Language lang;
    
    public WordDocumentCount() {
    	
    }

	public WordDocumentCount(String word, int year, Language lang,
			long documentCount) {
		
		this.word = word;
		this.year = year;
		this.lang = lang;
		this.documentCount = documentCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getDocumentCount() {
		return documentCount;
	}

	public void setDocumentCount(long documentCount) {
		this.documentCount = documentCount;
	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}
	
	
}
