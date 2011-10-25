package perseus.display.token;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import perseus.document.Token;
import perseus.document.TokenFilter;
import perseus.ie.entity.EntityOccurrence;

public class NamedEntityVotingTokenFilter extends TokenFilter {
    
    Set<EntityOccurrence> occurrences =
	new TreeSet<EntityOccurrence>(EntityOccurrence.LOCATION_COMPARATOR);
    
    public NamedEntityVotingTokenFilter(Collection<EntityOccurrence> occs) {
	occurrences.addAll(occs);
    }
    
    @Override
    public void process(Token token) {
	// just here to fulfill the API
	if (token.getType() != Token.Type.WORD) return;
	if (occurrences.isEmpty()) return;
    }

}
