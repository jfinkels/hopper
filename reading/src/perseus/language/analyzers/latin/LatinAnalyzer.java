package perseus.language.analyzers.latin;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import perseus.language.analyzers.DeaccentFilter;

/** Filters {@link StandardTokenizer} with {@link LatinFilter}, {@link
 * LowerCaseFilter} and {@link StopFilter}. */
public class LatinAnalyzer extends Analyzer {
    private Set stopSet;
    private boolean expand = false;
    
    /** An array containing some common English words that are usually not
        useful for searching. */
    public static final String[] STOP_WORDS = {
        "ab", "ac", "ad", "adhic", "aliqui", "aliquis", "an",
        "ante", "apud", "at", "atque", "aut", "autem", "cum",
        "cur", "de", "deinde", "dum", "ego", "enim", "ergo",
        "es", "est", "et", "etiam", "etsi", "ex", "fio", "haud",
        "hic", "iam", "idem", "igitur", "ille", "in", "infra",
        "inter", "interim", "ipse", "is", "ita", "magis", "modo",
        "mox", "nam", "ne", "nec", "necque", "neque", "nisi",
        "non", "nos", "o", "ob", "per", "possum", "post", "pro",
        "quae", "quam", "quare", "qui", "quia", "quicumque",
        "quidem", "quilibet", "quis", "quisnam", "quisquam",
        "quisque", "quisquis", "quo", "quoniam", "sed", "si",
        "sic", "sive", "sub", "sui", "sum", "super", "suus",
        "tam", "tamen", "trans", "tu", "tum", "ubi", "uel", "uero"
    };

    /** Builds an analyzer. */
    public LatinAnalyzer(boolean expand) {
	stopSet = StopFilter.makeStopSet(STOP_WORDS);
	this.expand = expand;
    }

    /** Constructs a {@link StandardTokenizer} filtered by a {@link
        LatinFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}. */
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(reader);
	result = new DeaccentFilter(result);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stopSet);
	/*
	if (expand) {
	    result = new LatinFilter(result);
	}
	*/
        return result;
    }
}
