package perseus.morph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import perseus.language.Language;

import com.megginson.sax.XMLWriter;

/**
 * Sadly, we lost the perl script that converts Morpheus output to XML as it is used
 * by the hopper, so this is an attempt to recreate it using the hopper's classes.
 * For the most part, we can handle Greek and Latin mostly the same, except the codes 
 * for adjectives in Greek and Latin are different.
 * @see Parse
 * @see MorphCode
 * @see Lemma
 * 
 * Based on Gabe Weaver's morphology_api, but uses hopper classes
 * @author rsingh04
 *
 */
public class CruncherToXML {

	private static Logger logger = Logger.getLogger(CruncherToXML.class);

	private BufferedReader buffReader;
	private String outputFileName;
	private static List<String> adjectiveCodes = new ArrayList<String>();;
	private Map<String, String[]> morphCodeFeatureMap = MorphCode.getFeatureMap();
	String newLine = System.getProperty("line.separator");
	Language language = Language.GREEK;
	
	static {
		//there are duplicates in these arrays, but it's not a big deal. can add new arrays
		//if we need to handle new languages
		//Greek
		String[] greekAdj = {"os_on", "os_h_on", "ws_wn_long", "verb_adj1", "verb_adj2", "ws_wn", "eos_eh_eon", "art_adj", "oos_oh_oon", 
				"oos_oon", "hs_es", "is_idos_adj", "is_itos_adj", "as_asa_an", "wn_on_comp", "wn_on", "us_u", "us_eia_u", "as_aina_an", 
				"hn_eina_en", "eis_essa", "heis_hessa", "oeis_oessa", "n_nos_adj", "ous_ontos", "wn_ousa_on", "irreg_adj1", "irreg_adj3", 
				"pron_adj3", "pron_adj1"};
		//Latin
		String[] latinAdj = {"us_a_um", "0_a_um", "er_ra_rum", "er_era_erum", "ius_ia_ium", "is_e", "er_ris_re", "ans_adj", "ens_adj", 
				"us_ius_adj", "0_ius_adj", "ior_ius_comp", "or_us_comp", "ax_adj", "0_adj3", "peLs_pedis_adj", "ox_adj", "ix_adj",
				"s_tis_adj", "ex_icis_adj", "s_dis_adj", "irreg_adj3", "irreg_adj1", "irreg_adj2", "pron_adj1", "pron_adj3"};
		
		adjectiveCodes.addAll(Arrays.asList(greekAdj));
		adjectiveCodes.addAll(Arrays.asList(latinAdj));
	}

	public CruncherToXML(String inputFileName, String outputFileName) {
		FileReader inputFileRead;
		try {
			inputFileRead = new FileReader(inputFileName);
			BufferedReader br = new BufferedReader(inputFileRead);
			this.buffReader = br;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.outputFileName = outputFileName;
	}

	/**
	 * Give it the file name that has Morpheus output and the file name for the XML
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) {
		Options options = new Options()
		.addOption("l", "latin", false, "process Latin Morpheus output")
		.addOption("h", "help", false, "print this message");

		HelpFormatter helpFormatter = new HelpFormatter();

		String helpString = "Cruncher2XML <Input File> <Output File> [language]";
		String[] workingArgs = args;
		boolean isLatin = false;

		try {
			CommandLineParser parser = new PosixParser();
			CommandLine cl = parser.parse(options, args);

			if (cl.hasOption("latin") || cl.hasOption('l')) {
				isLatin = true;
			}
			else if (cl.hasOption("help") || cl.hasOption('h')) {
				helpFormatter.printHelp(helpString, options);
				System.exit(0);
			}
			workingArgs = cl.getArgs();
		} catch (ParseException e) {
			logger.fatal("Unable to parse command-line arguments", e);
			System.exit(1);
		}

		CruncherToXML cx =  new CruncherToXML(workingArgs[0], workingArgs[1]);
		if (isLatin) {
			cx.setLanguage(Language.LATIN);
		}
		try {
			cx.parseMorpheusOutput();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * Write list of parses to a file in XML format
	 * @throws SAXException
	 * @throws IOException
	 */
	private void writeToFile(XMLWriter w, List<Parse> parses) throws SAXException, IOException {
		Collections.sort(parses, new Comparator<Parse>() {
			public int compare(Parse o1, Parse o2) {
				String parse1 = o1.getForm()+" "+o1.getLemma().getHeadword()+" "+o1.toString();
				String parse2 = o2.getForm()+" "+o2.getLemma().getHeadword()+" "+o2.toString();
				return parse1.compareTo(parse2);
			}
		});

		for (Parse p : parses) {
			w.characters(" ");
			w.startElement("analysis");
			w.characters(newLine);
			w.characters("  ");
			w.dataElement("form", p.getForm());
			w.dataElement("lemma", p.getLemma().getHeadword());
			w.characters(newLine);			
			w.characters("  ");
			
			for (String featureName : p.getFeatures().keySet()) {
				w.dataElement(featureName, p.getFeatures().get(featureName));
			}
			w.characters(newLine);
			w.characters(" ");

			w.endElement("analysis");
			w.characters(newLine);
		}
	}

	/**
	 * Creates Parses from Morpheus output
	 * @throws IOException
	 */
	private void parseMorpheusOutput() throws SAXException, IOException {
		XMLWriter w = new XMLWriter(new FileWriter(outputFileName));

		w.startDocument();
		w.startElement("analyses");
		w.characters(newLine);
		
		String analysisData = buffReader.readLine();
		while (analysisData != null) {
			analysisData = analysisData + "\n" + buffReader.readLine();
			List<Parse> parses = createParses(analysisData);
			writeToFile(w, parses);
			analysisData = buffReader.readLine();
		}
		
		w.endElement("analyses");
		w.endDocument();
	}

	/**
	 * Creates Parse objects from Morpheus output
	 * @param wordAnalysis the word plus it's analysis from Morpheus
	 * @return list of Parses
	 */
	private List<Parse> createParses(String wordAnalysis) {
		//make sure there are no duplicates
		Set<Parse> parseList = new HashSet<Parse>();
		logger.debug("Word and analysis is: " + wordAnalysis);

		String[] formSplit = wordAnalysis.split("\\n");
		String form = formSplit[0];
		logger.debug("form is "+form);
		wordAnalysis = formSplit[1];

		String[] NLs = wordAnalysis.split("<NL>");
		for (String NL : NLs) {
			if (NL.equals("")) {
				continue;
			}
			NL = NL.replaceAll("</NL>","");	

			String[] morphCodes = NL.split("(\\s)+");
			Parse currentParse = initializeParse(form, morphCodes[1]);

			String lastMorphCode = morphCodes[morphCodes.length-1];
			String partOfSpeechAbbrev = morphCodes[0];

			if (lastMorphCode.equals("adverb")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.ADVERB);
			} 
			else if (lastMorphCode.equals("article")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.ARTICLE);
			} 
			else if (lastMorphCode.equals("particle")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.PARTICLE);
			} 
			else if (lastMorphCode.equals("conj")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.CONJUNCTION);
			} 
			else if (lastMorphCode.equals("prep")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.PREPOSITION);
			} 
			else if (lastMorphCode.equals("pron1") || lastMorphCode.equals("pron2") || lastMorphCode.equals("pron3") 
					|| lastMorphCode.equals("relative")	|| lastMorphCode.equals("demonstr") || lastMorphCode.equals("indef") 
					|| lastMorphCode.equals("interrog")
			) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.PRONOUN);
			} 
			else if (lastMorphCode.equals("numeral")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.NUMERAL);
			} 
			else if (lastMorphCode.equals("exclam")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.EXCLAMATION);
			} 
			else if (lastMorphCode.equals("alphabetic") || (lastMorphCode.equals("indecl") && language.equals(Language.GREEK))) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.IRREGULAR);
			} 
			else if (morphCodes[2].equals("adverbial")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.ADVERBIAL);
			} 
			else if (partOfSpeechAbbrev.equals("V")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.VERB);
			} 
			else if (partOfSpeechAbbrev.equals("P")) {
				currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.PARTICIPLE);
			}
			//partOfSpeechAbbrev is 'N'
			else {
				/*
				 * need to do more to differentiate nouns and adjectives based on codes in the 
				 * morpheus output. since there are so many, it's cleaner to make it a separate 
				 * list and check if the list contains the code
				 */
				if (adjectiveCodes.contains(lastMorphCode)) {
					currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.ADJECTIVE);
				} else {
					//at this point, classify it as a noun since we've tried everything else
					logger.debug("found a noun");
					currentParse.setFeature(MorphCode.PART_OF_SPEECH, MorphCode.NOUN);
				}
			}

			List<Parse> groupedParses = new ArrayList<Parse>();
			groupedParses.add(currentParse);
			for (int i=2; i < morphCodes.length-1; i++) {
				String code = morphCodes[i];
				//handle situations where code values are lumped together with /, ie masc/fem
				if (code.indexOf("/") != -1) {
					String[] codeComponents = code.split("/");
					
					/*
					 * can't remove and add Parses to the same list, so we
					 * create a new list for the new Parses and overwrite
					 * groupedParses with new list after we're done
					 */
					List<Parse> newParses = new ArrayList<Parse>();
					for (Parse existingParse : groupedParses) {
						for (int j = 0; j < codeComponents.length; j++) {
							Parse dupParse = new Parse(existingParse);
							dupParse = setFeature(codeComponents[j], dupParse);
							newParses.add(dupParse);
						}
					}
					groupedParses = newParses;
				} else {
					for (Parse groupParse : groupedParses) {
						groupParse = setFeature(code, groupParse);
					}
				}
			}
			parseList.addAll(groupedParses);
		}
		return new ArrayList<Parse>(parseList);
	}
	
	/**
	 * Set a feature for a Parse
	 * @param code
	 * @param p
	 * @return Parse object with feature set
	 */
	private Parse setFeature(String code, Parse p) {
		boolean setFeature = false;
		for (String featureType : morphCodeFeatureMap.keySet()) {
			if (ArrayUtils.contains(morphCodeFeatureMap.get(featureType),code)) {
				//handle dialects differently
				if (featureType.equals(MorphCode.DIALECT)) {
					String oldDialect = p.getDialects();
					String newDialect;
					//append new dialect code to end of existing string
					if (oldDialect != null) {
						newDialect = oldDialect + " " + code;
					} else {
						newDialect = code;
					}
					p.setDialects(newDialect);
					setFeature = true;
					break;
				} else {
					p.setFeature(featureType, code);
					setFeature = true;
					break;
				}
			}
		}
		if (!setFeature) {
			//if the code didn't fit in any of the other features, add it to "miscellaneous features"
			String oldMisc = p.getMiscellaneousFeatures();
			String newMisc;
			if (oldMisc != null) {
				newMisc = oldMisc + " " + code;
			} else {
				newMisc = code;
			}
			p.setMiscellaneousFeatures(newMisc);
		}
		return p;
	}

	/**
	 * Initializes parse with form and Lemma (with headword set)
	 * @param form
	 * @param lemmaString
	 * @return Parse object
	 */
	private Parse initializeParse(String form, String lemmaString) {
		Parse p = new Parse();
		p.setForm(form);

		String[] lemmaParts = lemmaString.split(",");

		String headword = lemmaParts[0];
		if (lemmaParts.length == 2) {
			headword = lemmaParts[1];
		}

		Lemma lemma = new Lemma();
		//set language and auth name so we can take advantage of
		//the equals() methods in Parse and Lemma
		lemma.setLanguage(language);
		lemma.setHeadword(headword);
		lemma.setAuthorityName(headword);

		p.setLemma(lemma);
		return p;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

}
