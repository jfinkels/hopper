package perseus.language.analyzers;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


/**
 * DeaccentingAnalyzer allows search terms 
 */
public class DeaccentingAnalyzer extends StandardAnalyzer {

	/**
	 * Class Constructor
	 */
	public DeaccentingAnalyzer() {
		super();
	}

	/**
	 * Constructs a StandardTokenizer filtered by a DeaccentFilter.
	 *
	 * @param fieldName 
	 * @param reader
	 * @return TokenStream filtered by the DeaccentFilter
	 */
	public final TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream result = super.tokenStream(fieldName, reader);
		result = new DeaccentFilter(result);
		return result;
	}
}
