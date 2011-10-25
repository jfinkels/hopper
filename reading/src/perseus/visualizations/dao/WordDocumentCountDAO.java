package perseus.visualizations.dao;

import java.util.List;

import org.hibernate.ScrollableResults;

import perseus.language.Language;
import perseus.visualizations.WordDocumentCount;

public interface WordDocumentCountDAO {

	public WordDocumentCount getByWordAndYear(String word, int year);
	
	public void clear();
	
	public List<Language> getDistinctLanguages();
	
	public ScrollableResults getYearCounts(Language l);
	
	public List<String> getByYearAndLanguage(int year, Language lang);
}
