package perseus.morph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;

import perseus.language.Language;
import perseus.morph.dao.HibernateLemmaDAO;
import perseus.morph.dao.HibernateParseDAO;
import perseus.morph.dao.LemmaDAO;
import perseus.morph.dao.ParseDAO;
import perseus.util.BatchInserter;

/**
 * Adds parses to our database by loading them from an XML file. The file is
 * expected to be of the form:
 * <pre>
 * &lt;analyses&gt;
 *   &lt;analysis&gt;
 *     &lt;form&gt;facis&lt;/form&gt;
 *     &lt;lemma&gt;facio&lt;/lemma&gt;
 *     &lt;pos&gt;verb&lt;/pos&gt;
 *     &lt;tense&gt;pres&lt;/tense&gt;
 *     &lt;voice&gt;act&lt;/voice&gt;
 *     ... more features ...
 *   &lt;/analysis&gt;
 *   ... more analyses ...
 * &lt;/analyses&gt;
 * </pre>
 * 
 * Each analysis expects a "form" element and a "lemma" element; other
 * possible elements are as given in the appropriate {@link MorphCodeGenerator}
 * for each given language. The value of every such element should be one of
 * the accepted values found in the {@link MorphCodeGenerator} constants,
 * although this is not enforced. The value of the "form" element may be
 * anything; the value of the "lemma" element should correspond to the headword
 * of a known lemma (and may include a sequence number). Also, there may be an
 * "orth" element that includes long marks (^ and _).
 *
 * The XML files used by this program, named {@code greek.morph.xml},
 * {@code latin.morph.xml} and so on, were originally generated from a Perl
 * script ("cruncher2xml.pl") that parsed Morpheus output files.
 *
 * @see Lemma
 * @see Parse
 * @see MorphCodeGenerator
 */

public final class ParseLoader {

	private static Logger logger = Logger.getLogger(ParseLoader.class);

	private static LemmaDAO lemmaDAO = new HibernateLemmaDAO();
	private static ParseDAO parseDAO = new HibernateParseDAO();	
	private static BatchInserter inserter = new BatchInserter();

	private static void doLoad(
			String filename,
			Language language,
			boolean deleteExisting) throws IOException, SAXException {

		if (deleteExisting) {
			logger.info("Deleting existing parses for " + language);
			parseDAO.beginTransaction();
			parseDAO.deleteByLanguage(language);
			parseDAO.endTransaction();
		}

		inserter.beginTransaction();
		load(filename, language);
		inserter.endTransaction();
	}

	public static void load(String filename, Language language)
	throws IOException, SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader();
		InputSource source = new InputSource(filename);

		ParseHandler handler = new ParseHandler(language);
		parser.setContentHandler(handler);
		logger.info("Parsing " + filename + "...");
		parser.parse(source);
		logger.info("Done");
	}

	/**
	 * Handles the actual parsing. Stores features in a {@link HashMap}
	 * until it hits the end of a given analysis, at which point it uses
	 * the accumulated features to create the {@code Parse}. Also, uses a
	 * cache to map known headwords to lemmas (or to indicate if a headword
	 * doesn't match anything).
	 */
	private static class ParseHandler extends DefaultHandler2 {
		private Map<String,String> features = new HashMap<String,String>();
		private int analysisCount = 0;
		private Language language;

		private String currentFeature;
		private StringBuilder currentValue = new StringBuilder();

		private int parseCount = 0;
		private Map<String,List<Lemma>> matchCache = 
			new LinkedHashMap<String,List<Lemma>>() {

			protected boolean removeEldestEntry(Map.Entry eldest) {
				return (size() > 10000);
			}
		};

		public ParseHandler(Language language) {
			this.language = language;
		}

		public void startElement(
				String namespaceURI,
				String localName,
				String qName,
				Attributes attributes) {

			if (qName.equals("analysis")) {
				// this tag marks the beginning of a parse
				features.clear();
				analysisCount++;
			} else if (qName.equals("analyses")) {
				// do nothing; this tag encloses everything else
			} else {
				// if it's not either of those two, we have either a form, a
				// lemma or a feature (gender, number, case...); store them all
				// in a hash for now until we get to the end of the current
				// analysis.
				currentFeature = qName.toLowerCase();
				currentValue = new StringBuilder(10);
			}
		}

		public void endElement(
				String namespaceURI,
				String localName,
				String qName) {

			// if this is just another feature, add it to the hash
			if (!qName.equalsIgnoreCase("analysis") &&
					!qName.equalsIgnoreCase("analyses")) {

				features.put(currentFeature, currentValue.toString());
				return;
			}

			// if we've hit this tag, we're done
			if (qName.equalsIgnoreCase("analyses")) return;

			// if we're here, we're dealing with the end of a given analysis;
			// first, freak out/return if we're missing a form or lemma
			if (!(features.containsKey("form") &&
					features.containsKey("lemma"))) {
				logger.warn("Parse lacks form or lemma: " + features);
				return;
			}

			// otherwise, create the parse and save it
			List<Parse> parses = createParses();

			for (Parse parse : parses) {
				if (!parseDAO.exists(parse)) {
					inserter.insert(parse);
					parseCount++;
					if (parseCount % 2500 == 0) {
						logger.info(String.format("[%9d] %s %s",
								parseCount, parse.getForm(), parse));
					}
				}

			}
		}

		private List<Parse> createParses() {

			// be sensitive to case when converting, and use adapter's toLowerCase

			String form = (language.getAdapter().matchCase() ? features.remove("form") : language.getAdapter().toLowerCase(features.remove("form")));
			String lemmaText = features.remove("lemma");
			lemmaText = lemmaText.replaceAll("#", "");
			Pattern hyphenPattern = Pattern.compile("^.+-(.+)$");
			Matcher hyphenMatcher = hyphenPattern.matcher(lemmaText);
			if (hyphenMatcher.matches()) {
				lemmaText = hyphenMatcher.group(1);
			}

			Pattern lemmaPattern = Pattern.compile("^(\\D+)(\\d+)$");
			Matcher lemmaMatcher = lemmaPattern.matcher(lemmaText);

			String headword;
			//get all lemmas if there isn't a seq # in the parses file
			int sequenceNumber=-1;

			if (lemmaMatcher.matches()) {
				headword = lemmaMatcher.group(1);
				sequenceNumber = Integer.parseInt(lemmaMatcher.group(2));
			} else {
				headword = lemmaText;
			}			

			String formWithQuantity = features.remove("orth");
			// some parses may lack "orth" tags
			if (formWithQuantity == null) formWithQuantity = form;

			String bareForm = Lemma.BARE_WORD_PATTERN.matcher(form).replaceAll("");

			List<Lemma> matchingLemmas;
			if (matchCache.containsKey(headword + sequenceNumber)) {
				matchingLemmas = matchCache.get(headword + sequenceNumber);
			} else {
				matchingLemmas =
					lemmaDAO.getMatchingLemmas(headword, sequenceNumber, language.getCode(), false);

				// this will put empty lists too, which is fine; it effectively
				// marks this lemma as a miss
				matchCache.put(headword + sequenceNumber, matchingLemmas);
			}

			List<Parse> parses = new ArrayList<Parse>();
			for (Lemma lemma : matchingLemmas) {
				Parse parse = new Parse();
				parse.setForm(form);
				parse.setExpandedForm(formWithQuantity);
				parse.setBareForm(bareForm);

				for (String feature : features.keySet()) {
					parse.setFeature(feature, features.get(feature));
				}

				parse.setLemma(lemma);
				parses.add(parse);
			}
			return parses;
		}

		public void characters(char[] chars, int startIndex, int length) {
			// the only actual characters in these files are inside
			// form/lemma/orth/<feature> tags, so we don't need to worry about
			// anything besides the simple case: add it to the buffer for the
			// given feature.
			currentValue.append(chars, startIndex, length);
		}
	}

	public static void main(String[] args) {
		Options options = new Options()
		.addOption("n", "no-delete", false, "don't delete existing parses")
		.addOption("h", "help", false, "print this message");


		HelpFormatter helpFormatter = new HelpFormatter();

		String helpString = "ParseLoader <XML file>";
		String[] workingArgs = args;
		boolean deleteExisting = true;

		try {
			CommandLineParser parser = new PosixParser();
			CommandLine cl = parser.parse(options, args);

			if (cl.hasOption("no-delete") || cl.hasOption('n')) {
				deleteExisting = false;
			} else if (cl.hasOption("help") || cl.hasOption('h')) {
				helpFormatter.printHelp(helpString, options);
				System.exit(0);
			}
			workingArgs = cl.getArgs();
		} catch (ParseException e) {
			logger.fatal("Unable to parse command-line arguments", e);
			System.exit(1);
		}

		if (workingArgs.length < 1 || workingArgs.length > 2) {
			logger.error("Usage: ParseLoader <XML file>");
			System.exit(1);
		}

		String filename = workingArgs[0];
		// try to guess the language from the filename if it wasn't specified;
		// it will probably be something like "greek.morph.xml"
		
		String[] fileSplit = filename.split("/");
		int splitLength = fileSplit.length;
		
		String languageCode = fileSplit[splitLength-1].split("\\.")[0];

				Language language = Language.forCode(languageCode);
				if (language == Language.UNKNOWN) {
					logger.fatal("Unknown language code: " + languageCode);
					System.exit(1);
				} else {
					logger.info("Using language: " + language);
				}

				try {
					doLoad(filename, language, deleteExisting);
				} catch (Exception e) {
					logger.fatal("Problem loading parse file", e);
					System.exit(1);
				}
	}
}