package perseus.document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import perseus.util.Config;

/**
 * A wrapper for XSL transformations. Includes several different transform()
 * methods designed to operate on strings and DOM/JDOM documents and take
 * various parameters.
 *
 * By default, we assume that stylesheets are located in the directory
 * specified by the `hopper.stylesheet.path` property.
 */

public class StyleTransformer {

	public static final String DEFAULT_STYLESHEET = "tei.xsl";
	private static Logger logger = Logger.getLogger(StyleTransformer.class);

	/**
	 * Stores existing compiled stylesheets.
	 */
	private static HashMap<File,Templates> translets =
		new HashMap<File,Templates>();

	/**
	 * Stores the modification dates for stylesheets. If we find that a
	 * stylesheet has been modified since we've loaded it, we reload it.
	 */
	private static HashMap<File,Long> modificationDates =
		new HashMap<File,Long>();

	static {
		String key = "javax.xml.transform.TransformerFactory";
		String value = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
		Properties props = System.getProperties();
		if (!props.containsKey(key)) {
			props.put(key, value);
			System.setProperties(props);
		}
	}

	private static Templates loadStylesheet(File xsltFile) throws
	TransformerConfigurationException {

		Templates translet = null;

		// Instantiate the TransformerFactory, and use it with a StreamSource
		// XSL stylesheet to create a translet as a Templates object.
		TransformerFactory tFactory = TransformerFactory.newInstance();
		translet = tFactory.newTemplates(new StreamSource(xsltFile));

		return translet;
	}

	private static File getFile(String stylesheetName) {
		File xsltPath = new File(stylesheetName);
		if (!xsltPath.isAbsolute()) {
			File stylesheetPath = new File(Config.getStylesheetPath());
			xsltPath = new File(stylesheetPath, xsltPath.getPath());
		}
		return xsltPath;
	}

	/**
	 * Returns a Templates object representing the compiled version of
	 * `stylesheetName`.
	 */
	public static Templates getTranslet(String stylesheetName) {
		File xsltFile = getFile(stylesheetName).getAbsoluteFile();
		if (!xsltFile.exists()) { 
			throw new IllegalArgumentException(
					new FileNotFoundException(stylesheetName));
		}

		long lastModified = xsltFile.lastModified();

		if (translets.containsKey(xsltFile) &&
				lastModified == modificationDates.get(xsltFile)) {
			return translets.get(xsltFile);
		}
		else {
			Templates translet;
			try {
				translet = loadStylesheet(xsltFile);

				logger.debug("Reloaded stylesheet " + xsltFile);
				modificationDates.put(xsltFile, lastModified);
				translets.put(xsltFile, translet);
				return translet;
			} catch (TransformerConfigurationException e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	/**
	 * Forces all stylesheets to reload.
	 */
	public static void clear() {
		translets.clear();
		modificationDates.clear();
	}

	/**
	 * Transforms `input` by running it through `stylesheetName`.
	 *
	 * @param input a string representing the text to transform
	 * @param stylesheetName the stylesheet to use ("tei.xsl")
	 * @return the transformed text
	 */
	public static String transform(String input, String stylesheetName) {
		Map<String,String> map = Collections.emptyMap();
		return transform(input, stylesheetName, "", map);
	}

	/**
	 * Transforms `input` by running it through `stylesheetName` and passing
	 * `documentID` as a parameter representing the document being transformed.
	 *
	 * @param input a string representing the text to transform
	 * @param stylesheetName the stylesheet to use (e.g., "tei.xsl")
	 * @param documentID the ID of the document containing the text
	 * @return the transformed text
	 */
	public static String transform(String input, String stylesheetName,
			String documentID) {
		Map<String,String> map = Collections.emptyMap();
		return transform(input, stylesheetName, documentID, map);
	}

	/**
	 * Transforms `input` by running it through the default stylesheet for
	 * the document given by `metadata`. `metadata` is used to set parameters
	 * for language code and document ID.
	 *
	 * @param input the text to transform
	 * @param metadata the metadata representing `input`'s source documentg
	 * @return the transformed text
	 */
	public static String transform(String input, Metadata metadata) {
		Map<String,String> map = Collections.emptyMap();
		return transform(input, metadata, map);
	}

	/**
	 * Transforms `input` by running it through the default stylesheet for the
	 * document given by `metadata`, passing the values from `parameters` to
	 * the stylesheet. `metadata` is used to set parameters for language code
	 * and document ID.
	 *
	 * @param input the text to transform
	 * @param metadata the metadata representing `input`'s source documentg
	 * @param parameters the stylesheet parameters to set, and their values
	 * @return the transformed text
	 */
	public static String transform(String input, Metadata metadata,
			Map<String,String> parameters) {
		Map<String,String> workingParameters = (parameters != null ?
				new HashMap<String,String>(parameters) :
					new HashMap<String,String>());

		if (!workingParameters.containsKey("lang")) {
			workingParameters.put("lang", metadata.get(Metadata.LANGUAGE_KEY));
		}
		return transform(input, metadata.getDefaultStylesheet(),
				metadata.getDocumentID(), workingParameters);
	}

	/**
	 * Transforms `input` by running it through `stylesheetName` for the
	 * document `documentID`, passing the values from `parameters` to the
	 * stylesheet.
	 *
	 * @param input the text to transform
	 * @param metadata the metadata representing `input`'s source documentg
	 * @param parameters the stylesheet parameters to set, and their values
	 * @return the transformed text
	 */
	public static String transform(String input, String stylesheetName,
			String documentID,
			Map<String,String> parameters) {
		return doTransform(new StreamSource(new StringReader(input)),
				stylesheetName, documentID, parameters);
	}

	/**
	 * Transforms `document` by running it through `stylesheetName`.
	 *
	 * @param document a DOM document representing the text to transform
	 * @param stylesheetName the stylesheet to use (e.g., "tei.xsl")
	 * @return the transformed text
	 */
	public static String transform(Document document, String stylesheetName) {
		return transform(document, stylesheetName, "", null);
	}

	public static String transform(Document document, Metadata metadata) {
		return transform(document, metadata, null);
	}

	public static String transform(Document document, String stylesheetName,
			String documentID) {
		return transform(document, stylesheetName, documentID, null);
	}

	public static String transform(Document document, Metadata metadata,
			Map<String,String> parameters) {
		Map<String,String> workingParameters = (parameters != null ?
				new HashMap<String,String>(parameters) :
					new HashMap<String,String>());

		if (!workingParameters.containsKey("lang")) {
			workingParameters.put("lang", metadata.get(Metadata.LANGUAGE_KEY));
		}

		return transform(document, metadata.getDefaultStylesheet(),
				metadata.getDocumentID(), workingParameters);
	}

	public static String transform(Document document, String stylesheetName,
			String documentID,
			Map<String,String> parameters) {
		return doTransform(new DOMSource(document), stylesheetName,
				documentID, parameters);
	}

	/**
	 * Returns the content of `file` transformed by `stylesheetName`.
	 *
	 * @param file an XML file
	 * @param stylesheetName the stylesheet to use
	 * @return the text of `file`, transformed
	 */
	public static String transform(File file, String stylesheetName) {
		return doTransform(new StreamSource(file), stylesheetName,
				null, null);
	}

	/**
	 * Returns the content of `is` transformed by `stylesheetName`.
	 *
	 * @param is an InputStream containing XML text
	 * @param stylesheetName the stylesheet to use
	 * @return the content of `is`, transformed
	 */
	public static String transform(InputStream is, String stylesheetName) {
		return doTransform(new StreamSource(is), stylesheetName, null, null);
	}

	/**
	 * Returns the content of `is` transformed by `stylesheetName`, passing
	 * the entries in `parameters` to the stylesheet.
	 *
	 * @param is an InputStream containing XML text
	 * @param stylesheetName the stylesheet to use
	 * @param parameters the parameters to pass to the stylesheet
	 * @return the content of `is`, transformed
	 */
	public static String transform(InputStream is, String stylesheetName,
			Map<String,String> parameters) {
		return doTransform(new StreamSource(is), stylesheetName, null,
				parameters);
	}

	/**
	 * Returns the content of `source`, which is assumed to be from document
	 * `documentID`, transformed by `stylesheetName`, passing the entries in
	 * `parameters` to the stylesheet.
	 *
	 * @param source a Source representing the text to style
	 * @param stylesheetName the stylesheet to use
	 * @param documentID the document containing the given text
	 * @param parameters the parameters to pass to the stylesheet
	 * @return the content of `is`, transformed
	 */
	public static String transform(Source source, String stylesheetName,
			String documentID, Map<String,String> parameters) {
		return doTransform(source, stylesheetName, documentID, parameters);
	}

	private static String doTransform(Source source, String stylesheetName,
			String documentID,
			Map<String,String> parameters) {

		if (stylesheetName == null) {
			// set default stylesheet
			stylesheetName = DEFAULT_STYLESHEET;
		}

		if (stylesheetName.equals("archdb.spec")) {
			stylesheetName = "archdb.xsl";
		}
		else if (stylesheetName.equals("marlowe.spec")) {
			stylesheetName = "marlowe.xsl";
		}
		else if (stylesheetName.equals("papy.spec")) {
			stylesheetName = "papyrus.xsl";
		}
		else if (stylesheetName.endsWith("spec")) {
			// override CoST specs
			stylesheetName = DEFAULT_STYLESHEET;
		}

		Templates translet = getTranslet(stylesheetName);

		// Xalan has issues with StringWriter and UTF-8: bug #2047
		// Use a ByteArray instead and convert it to a String afterward
		//StringWriter strWriter = new StringWriter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			Transformer transformer = translet.newTransformer();
			if (documentID != null) {
				transformer.setParameter("document_id", documentID);
			}

			if (parameters != null) {
				for (String paramKey : parameters.keySet()) {
					String paramValue = parameters.get(paramKey);

					if (paramValue != null) {
						transformer.setParameter(paramKey, paramValue);
					}
				}
			}

			//transformer.setOutputProperty("encoding", "utf-8");
			transformer.transform(source, new StreamResult(outputStream));


		} catch (TransformerConfigurationException tce) {
			logger.fatal("Problem configuring stylesheet", tce);
		} catch (TransformerException te) {
			logger.fatal("Problem transforming " + documentID, te);
		}

		try {
			String result = outputStream.toString("utf-8");

			return result;
		} catch (UnsupportedEncodingException uee) {
			logger.fatal("Problem encoding output", uee);
			return null;
		}

		//return strWriter.toString();
	}
}


