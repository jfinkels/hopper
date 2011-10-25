/**
 * Loads a lexicon (one with its senses properly expanded) into our SQL
 * database, creating an entry for each sense of each word. Skips entries
 * whose senses don't have valid id attributes, guessing they couldn't be
 * handled by the sense resolver.
 *
 * @author Adrian Packel
 */

package perseus.voting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import perseus.util.Config;
import perseus.util.SQLHandler;

public class SenseLoader {

	private static final Logger logger = Logger.getLogger(SenseLoader.class);

	String lexiconPath;
	String lexiconID;

	String currentKey;
	static PreparedStatement ps;
	static Connection con;

	static {
		con = SQLHandler.getConnection();

		try {
			ps = con.prepareStatement(
			"INSERT INTO senses VALUES (?, ?, ?, ?, ?, ?, ?)");
		} catch (SQLException sqle) {
			logger.fatal("Problem preparing statement!");
			System.exit(1);
		}
	}

	/**
	 * Creates a new SenseLoader for a given lexicon.
	 *
	 * @param lexiconID the Perseus ID of our lexicon
	 * @param lexiconPath the path of the desired XMLified lexicon
	 */
	public SenseLoader(String lexiconID, String lexiconPath) {
		this.lexiconID = lexiconID;
		this.lexiconPath = lexiconPath;
	}

	/**
	 * Clear out any existing entries for this lexicon.
	 */
	public void clearExisting() {
		try {
			Statement statement = con.createStatement();

			statement.execute("DELETE FROM senses WHERE document_id = '"
					+ lexiconID + "'");
		} catch (Exception e) {
			logger.error("Problem clearing out previous entries");
		}
	}

	/**
	 * Read our senses from the file we specified earlier.
	 */
	public void load() {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();

			SAXParser parser = factory.newSAXParser();
			SenseLoaderHandler senseHandler = new SenseLoaderHandler();

			logger.info("Parsing file " + lexiconPath);
			parser.parse(lexiconPath, senseHandler);
		} catch (SAXParseException spex) {
			logger.error("Problem while parsing entry " + currentKey
					+ ":" + spex.getLineNumber() + ":"
					+ spex.getColumnNumber() + ": " + spex.toString());
		} catch (Exception ex) {
			logger.error("Problem: " + ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Insert our senses into our SQL table.
	 */
	public void insertSense(String lexicon, String lexQuery,
			String id, String n, String level, String shortDef) {

		try {
			int entryID;
			int senseID;
			// Split our id up into its entry and sense numbers
			Matcher matcher = VoteManager.ID_PATTERN.matcher(id);
			if (matcher.matches()) {
				entryID = Integer.parseInt(matcher.group(1));
				senseID = Integer.parseInt(matcher.group(2));
			} else {
				logger.warn("Error getting IDs for " + id);
				entryID = -1;
				senseID = -1;
			}

			// 100 is the length of the short_definition field at the moment;
			// this probably shouldn't be hardcoded
			if (shortDef.length() > 100) {
				// this may leave some tags open, but it doesn't really matter
				shortDef = shortDef.substring(0, 97) + "...";
			}

			ps.setInt(1, entryID);
			ps.setInt(2, senseID);

			ps.setString(3, lexicon);
			ps.setString(4, lexQuery);
			ps.setString(5, n);
			if (level != null) {
				ps.setInt(6, Integer.parseInt(level));
			} else {
				ps.setInt(6, -1);
			}
			ps.setString(7, shortDef);

			ps.executeUpdate();
		} catch (SQLException sqle) {
			logger.error("Problem writing to database: " + sqle);
			sqle.printStackTrace();
		}
	}

	public static void main(String[] args) {

		if (args.length < 1) {
			logger.error("Usage: java SenseLoader <lexiconID> [lex file]");
			System.exit(1);
		}

		String lexiconID = args[0];
		String lexiconPath;

		// If the user entered a custom lexicon path, use it--otherwise grab
		// the filename from the Perseus ID
		if (args.length == 1) {
			String[] lexParts = lexiconID.split(":");
			String lexiconCode = lexParts[2];
			String[] idParts = lexiconCode.split("\\.");
			String numericID = idParts[0] + "." + idParts[1];

			lexiconPath = Config.getFilePath() + numericID + "/"
			+ lexParts[2] + ".xml";
		} else {
			lexiconPath = args[1];
		}

		SenseLoader loader = new SenseLoader(lexiconID, lexiconPath);
		loader.clearExisting();
		loader.load();
	}

	public class SenseLoaderHandler extends DefaultHandler {

		boolean inEntry;
		boolean inSense;
		boolean inTrans;
		boolean inOrth;

		StringBuffer meaningBuffer;
		String currentSense;
		String senseID;
		String n;
		String expandedN;
		String lexiconQuery;
		String level;
		boolean needDefinition;
		boolean needLemma;

		String meaningTag;
		boolean openTag = false;

		public SenseLoaderHandler() {
			inEntry = false;
			inSense = false;
			inTrans = false;
			inOrth = false;

			meaningBuffer = null;
			currentKey = null;
			currentSense = null;
			senseID = null;
			n = null;
			needDefinition = false;
			needLemma = false;

			if (lexiconID.equals("Perseus:text:1999.04.0057")
					|| lexiconID.equals("Perseus:text:1999.04.0058")
					|| lexiconID.equals("Perseus:text:1999.04.0072")
					|| lexiconID.equals("Perseus:text:1999.04.0073")) {
				meaningTag = "g";
			} else if (lexiconID.equals("Perseus:text:1999.04.0059")
					|| lexiconID.equals("Perseus:text:1999.04.0060")) {
				meaningTag = "l";
			} else {
				meaningTag = "i"; // default to plain old italics
			}
		}

		public void startElement(String namespaceURI,
				String sName,
				String qName,
				Attributes attributes) throws SAXException {

			if (qName.equalsIgnoreCase("entry")
					|| qName.equalsIgnoreCase("entryfree")) {

				inEntry = true;

				currentKey = attributes.getValue("key");

			} else if (qName.equalsIgnoreCase("sense")) {
				inSense = true;

				level = attributes.getValue("level");
				n = attributes.getValue("n");
				senseID = attributes.getValue("id");

				String expandedN = n; //expand(n);
			} else if ((qName.equalsIgnoreCase("tr")
					|| qName.equalsIgnoreCase("gloss")
					|| qName.equalsIgnoreCase("hi")) && inSense) {

				inTrans = true;
				if (meaningBuffer == null) {
					meaningBuffer = new StringBuffer();
				}
				meaningBuffer.append("<" + meaningTag + ">");
				openTag = true;
			}
		}

		public void endElement(String namespaceURI,
				String sName,
				String qName) throws SAXException {

			if (qName.equalsIgnoreCase("entry")
					|| qName.equalsIgnoreCase("entryfree")) {

				inEntry = false;

			} else if (qName.equalsIgnoreCase("sense")) {
				inSense = false;

				if (meaningBuffer == null) {
					meaningBuffer = new StringBuffer("[no specified meaning]");
				}
				insertSense(lexiconID, "entry="+currentKey, senseID, n, level, meaningBuffer.toString());
				meaningBuffer = null;
			} else if ((qName.equalsIgnoreCase("tr")
					|| qName.equalsIgnoreCase("gloss")
					|| qName.equalsIgnoreCase("hi")) && inSense) {
				inTrans = false;

				if (meaningBuffer != null) {
					meaningBuffer.append("</" + meaningTag + ">");
					openTag = false;
				}
			}
		}

		public void characters(char[] ch, int start, int length) {

			if (inSense) {
				if (meaningBuffer == null) {
					meaningBuffer = new StringBuffer();
				}
				meaningBuffer.append(ch, start, length);
			}
		}
	}
}
