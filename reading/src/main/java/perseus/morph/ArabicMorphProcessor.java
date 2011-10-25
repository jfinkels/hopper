package perseus.morph;

import gpl.pierrick.brihaye.aramorph.AraMorph;
import gpl.pierrick.brihaye.aramorph.Solution;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import perseus.document.Chunk;
import perseus.document.RecentlyModifiedCorpusProcessor;
import perseus.document.Token;
import perseus.language.ArabicAdapter;
import perseus.language.Language;

/**
 * Generates a file of morphological analyses for all
 * words contained in the texts specified on the command line.
 *
 */
public class ArabicMorphProcessor extends RecentlyModifiedCorpusProcessor {
	private static Logger logger = Logger.getLogger(ArabicMorphProcessor.class);

	private TreeSet<String> forms;
	private AraMorph aramorph;
	private String outputFile;

	public ArabicMorphProcessor() {
		super();
		setOption(SUBDOC_METHOD, ONE_DOC);
		forms=new TreeSet<String>();
		aramorph=new AraMorph();
	}

	public void setOutputFile(String s) {
		outputFile=s;
	}

	public String getOutputFile() {
		return outputFile;
	}

	/**
	 * Check if the word is arabic and add it to the set of unique words to be processed
	 * at the end
	 *
	 */
	public void processToken(Token token) {
		if (token.getType() != Token.Type.WORD || !token.getLanguage().equals(Language.ARABIC)) return;

		String form=token.getDisplayText();
		form=ArabicAdapter.salmone2Buckwalter(form);
		form=ArabicAdapter.toXMLFriendly(form);

		forms.add(AraMorph.romanizeWord(form));
	}

	public void printForms() {

		try {
			PrintStream ps = new PrintStream(new FileOutputStream(getOutputFile()), true);

			ArabicCruncher ac = new ArabicCruncher();

			ps.println("<analyses>");

			for (String s : forms) {
				String transliterated=AraMorph.romanizeWord(s).replaceAll("I", "<").replaceAll("O", ">").replaceAll("W", "&");

				aramorph.analyzeToken(transliterated, true);
				HashSet<Solution> solutions=aramorph.getWordSolutions(transliterated);

				for (Solution solution : solutions) {
					TreeMap<String, String> features=new TreeMap(ArabicCruncher.getFeatures(solution));
					String xml =
						"\t<orth>" + ArabicAdapter.toXMLFriendly(solution.getWordVocalization()) + "</orth>\n" +
						"\t<lemma>" + ArabicAdapter.toXMLFriendly(solution.getLemma().replaceAll("_", "")) + "</lemma>\n";

					for (String feature : features.keySet()) {
						xml += "\t<" + feature + ">" + features.get(feature) + "</" + feature + ">\n";
					}

					ps.println("<analysis>\n\t<form>" + ArabicAdapter.toXMLFriendly(transliterated) + "</form>\n" + xml + "</analysis>");
				}

			}

			ps.println("</analyses>");
		} catch (FileNotFoundException e) {
			logger.error("Can't write to output file : " + getOutputFile());
		} catch (Exception e) {
			logger.error("Error with file " + e);
		}
	}

	public boolean shouldProcessDocument(Chunk documentChunk) {
		return (super.shouldProcessDocument(documentChunk) && documentChunk.getMetadata().getLanguage().equals(Language.ARABIC));
	}

	public static void main(String[] args) {
		ArabicMorphProcessor loader = new ArabicMorphProcessor();

		String[] effectiveArgs = args;
		Options options = new Options()
		.addOption("f", "force", false, 
		"force loading, even if file unchanged");

		CommandLineParser parser = new PosixParser();

		try {
			CommandLine cl = parser.parse(options, args);

			if (cl.hasOption("force") || cl.hasOption("f")) {
				loader.setIgnoreModificationDate(true);
			}
			effectiveArgs = cl.getArgs();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (effectiveArgs.length > 0) {
			loader.setOutputFile(args[0]);

			for (int i=1; i<args.length; i++) {
				loader.processAnything(args[i]);
			}
		} else {
			loader.processCorpus();
		}
		loader.printForms();
	}
}
