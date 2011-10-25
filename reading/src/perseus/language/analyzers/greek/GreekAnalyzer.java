package perseus.language.analyzers.greek;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


public class GreekAnalyzer extends Analyzer {
    private Set stopSet;

    /** An array containing some common Greek words that are usually not
	useful for searching. */
    public static final String[] STOP_WORDS = {
	"mh/", "e(autou=", "a)/n", "a)ll'", "a)lla/", "a)/llos", "a)po/", 
	"a)/ra", "au)to/s", "d'", "de/", "dh/", "dia/", "dai/", "dai/s", 
	"e)/ti", "e)gw/", "e)k", "e)mo/s", "e)n", "e)pi/", "ei)", "ei)mi/",
	"ei)/mi", "ei)s", "ga/r", "ge", "ga^", "h(", "h)/", "kai/", "kata/",
	"me/n", "meta/", "mh/", "o(", "o(/de", "o(/s", "o(/stis", "o(/ti",
	"ou(/tws", "ou(=tos", "ou)/te", "ou)=n", "ou)dei/s", "oi(", "ou)",
	"ou)de/", "ou)k", "peri/", "pro/s", "su/", "su/n", "ta/", "te", "th/n",
	"th=s", "th=|", "ti", "ti/", "tis", "ti/s", "to/", "toi/", "toiou=tos",
	"to/n", "tou/s", "tou=", "tw=n", "tw=|", "u(mo/s", "u(pe/r", "u(po/",
	"w(s", "w)=", "w(/ste", "e)a/n", "para/", "so/s"
    };

    /** Builds an analyzer. */
    public GreekAnalyzer() {
	stopSet = StopFilter.makeStopSet(STOP_WORDS);
    }

    /** Constructs a {@link StandardTokenizer} filtered by a {@link
	StandardFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}. */
    public TokenStream tokenStream(String fieldName, Reader reader) {
	TokenStream result = new BetaCodeTokenizer(reader);
	//result = new StandardFilter(result);
	result = new AccentFilter(result);
	result = new StopFilter(result, stopSet);
	return result;
    }
}
