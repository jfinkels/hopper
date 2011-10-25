package perseus.visualizations;

import perseus.language.Language;

public class YearWordCount {
	/** This instance's Hibernate-based ID */
    private Integer id;

    /** The year being counted */
    private int year;

    /** The language */
    private Language lang;

    /** The actual count */
    private long wordCount;

    public YearWordCount() {
	}
    
	public YearWordCount(int year, Language lang, long wordCount) {
		this.year = year;
		this.lang = lang;
		this.wordCount = wordCount;
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

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public long getWordCount() {
		return wordCount;
	}

	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}
    
    
}
