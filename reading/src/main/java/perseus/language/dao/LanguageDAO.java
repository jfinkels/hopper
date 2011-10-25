package perseus.language.dao;

import java.util.Set;

import perseus.language.Language;

public interface LanguageDAO {
    public void save(Language language);
    public void delete(Language language);
    public void clear();
    public void update(Language language);
    public void saveOrUpdate(Language language);
    
    public Language getById(int id);
    public Language getByAbbreviation(String abbrev);
    
    public Set<Language> getAllLanguages();
}
