package perseus.document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import perseus.language.Language;
import perseus.util.DisplayPreferences;
import edu.unc.epidoc.transcoder.TransCoder;

public class TextEncodingConverter {
	private static Logger logger = Logger.getLogger(TextEncodingConverter.class);

	String originalFilePath;
	String newFilePath;
	Language language;
	String inputFormat;
	String outputFormat;

	public TextEncodingConverter(String inputFile, String outputFile, Language language, String inputFormat, String outputFormat) {
		originalFilePath = inputFile;
		newFilePath = outputFile;
		this.language = language;
		this.inputFormat = inputFormat;
		this.outputFormat = outputFormat;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options()
		.addOption("i", "inputFormat", true,
		"Format text currently is in [BetaCode, Buckwalter]")
		.addOption("o", "outputFormat", true,
		"Format to turn text into [Unicode, PerseusBetaCode, Buckwalter]")
		.addOption("l", "language", true,
		"Language of the given input file")
		.addOption("h", "help", false, "print help message");;

		String helpString = "TextEncodingConverter <Input File> <Output File> <Input Format> <Output Format> <Language>";
		HelpFormatter helpFormatter = new HelpFormatter();
		String[] workingArgs = args;

		try {
			CommandLineParser parser = new PosixParser();
			CommandLine cl = parser.parse(options, args);

			String inputFormat = cl.getOptionValue('i');
			if (inputFormat == null) {
				inputFormat = cl.getOptionValue("inputFormat");
			}
			if (inputFormat == null) {
				inputFormat = "BetaCode";
			}

			String outputFormat = cl.getOptionValue('o');
			if (outputFormat == null) {
				outputFormat = cl.getOptionValue("outputFormat");
			}
			if (outputFormat == null || outputFormat.equalsIgnoreCase("unicode")) {
				outputFormat = "UnicodeC";
			}
			
			String language = cl.getOptionValue('l');
			if (language == null) {
				language = cl.getOptionValue("language");
			}
			if (language == null) {
				language = "greek";
			}
			language = language.toLowerCase();

			if (cl.hasOption("help") || cl.hasOption('h')) {
				helpFormatter.printHelp(helpString, options);
				System.exit(0);
			}
			workingArgs = cl.getArgs();

			Language lang = Language.forCode(language);
			TextEncodingConverter tec = new TextEncodingConverter(workingArgs[0], workingArgs[1], lang, inputFormat, outputFormat);
			if (tec.outputFormat.contains("BetaCode")) {
				tec.toBetaCode();
			} else {
				tec.toUnicode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void toBetaCode() throws Exception {
		File originalFile = new File(originalFilePath);
		TransCoder tc = new TransCoder(inputFormat, outputFormat);
		String result = tc.getString(originalFile);
		//logger.info(result);
		FileWriter newFileWriter = new FileWriter(newFilePath);
		BufferedWriter out = new BufferedWriter(newFileWriter);
		out.write(result);
		out.close();		
	}

	private void toUnicode() throws Exception {
		String xmlText = null;
		File file = new File(originalFilePath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line  = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		xmlText = stringBuilder.toString();
	
		Renderer renderer = new Renderer(language);
		DisplayPreferences prefs = new DisplayPreferences();
		if (language == Language.GREEK) {
			prefs.put(DisplayPreferences.GREEK_INPUT_KEY, inputFormat);
			prefs.put(DisplayPreferences.GREEK_DISPLAY_KEY, outputFormat);
		} else {
			prefs.put(DisplayPreferences.ARABIC_DISPLAY_KEY, outputFormat);
		}
		renderer.addLanguageTokenFilters(prefs);
		String renderedText = renderer.renderText(xmlText);

		File outFile = new File(newFilePath);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));

		out.write(renderedText);
		out.close();
	}
}
