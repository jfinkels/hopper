package perseus.visualizations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import perseus.language.Language;
import perseus.util.Config;
import perseus.visualizations.dao.HibernateWordDocumentCountDAO;
import perseus.visualizations.dao.HibernateYearWordCountDAO;

public class TimeplotFilesCreator {
	private static final Logger logger = Logger.getLogger(TimeplotFilesCreator.class);
	
	HibernateWordDocumentCountDAO wDAO = new HibernateWordDocumentCountDAO();
	HibernateYearWordCountDAO yDAO = new HibernateYearWordCountDAO();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TimeplotFilesCreator tfc = new TimeplotFilesCreator();
		tfc.createFiles();
	}

	private void createFiles() {
		List<Language> languages = wDAO.getDistinctLanguages();
		for (Language lang : languages) {
			allWords(lang);
			individualWords(lang);
		}
	}

	/**
	 * For every word, creates a file that contains the different
	 * document counts per year
	 * @param lang
	 */
	private void individualWords(Language lang) {
		List<String> distinctWords = wDAO.getWordsByLanguage(lang);
		for (String word : distinctWords) {
			String filePath = Config.getStaticPath()+"data/"+lang.getName().toLowerCase()+"/";
			new File(filePath).mkdirs();
			String fileName = word+".txt";
			StringBuffer output = new StringBuffer();
			output.append("# ").append(word).append("\n");
			List<WordDocumentCount> counts = wDAO.getByWordAndLanguage(word, lang);
			for (WordDocumentCount count : counts) {
				String year = formatYear(count.getYear());
				output.append(year).append(",").append(count.getDocumentCount()).append("\n");
			}
			writeToFile(filePath, fileName, output);			
		}
	}

	/**
	 * Creates a file with year word counts per language
	 * @param lang
	 */
	private void allWords(Language lang) {
		String filePath = Config.getStaticPath()+"data/"+lang.getName().toLowerCase()+"/";
		new File(filePath).mkdirs();
		StringBuffer output = new StringBuffer();
		output.append("# ").append(lang.getName().toLowerCase()).append("\n");
		String fileName = lang.getName().toLowerCase()+"All.txt";
		List<YearWordCount> results = yDAO.getByLanguage(lang);
		for (YearWordCount ywc : results) {
			String year = formatYear(ywc.getYear());
			output.append(year).append(",").append(ywc.getWordCount()).append("\n");
		}
		writeToFile(filePath, fileName, output);
	}
	
	private String formatYear(int origYear) {
		String year = String.valueOf(origYear);
		if (origYear < 0) {
			year = year.substring(1);
			while (year.length() < 4) {
				year = "0" + year;
			}
			year = "-"+year;
		} else {
			while (year.length() < 4) {
				year = "0" + year;
			}
		}
		return year;
	}
	
	private void writeToFile(String filePath, String fileName, StringBuffer output) {
		try {
			File file = new File(filePath, fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(output.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
