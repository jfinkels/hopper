/**
 * This servlet serves up entire XML texts for download. It takes a "doc"
 * parameter indicating the document to use (if the parameter is a subquery, it
 * just uses the document ID). If there's a problem opening the text, or we've
 * been given an invalid document ID (or the ID of a document for which
 * downloading is forbidden), we output an error message.
 */
package perseus.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.util.Timekeeper;

public class TextDownloadServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(TextDownloadServlet.class);

	public static final String NOT_FOUND_MSG =
		"Sorry, the document you requested could not be found.";

	public static final String FORBIDDEN_MSG =
		"Sorry, this text is not available for download.";

	public static final String INTERNAL_SERVER_ERROR_MSG =
		"Sorry, there was a problem loading the text you requested.";


	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

		Timekeeper keeper = new Timekeeper();
		keeper.start();
		keeper.record("Beginning text D/L request");

		String documentParam = request.getParameter("doc");
		boolean available = true;


		PrintWriter writer = null;

		Query query = new Query(documentParam);
		Metadata documentMetadata = query.getMetadata();
		if (documentMetadata.has(Metadata.STATUS_KEY) &&
				(documentMetadata.get(Metadata.STATUS_KEY).equals("1") ||
						documentMetadata.get(Metadata.STATUS_KEY).equals("3"))) {

			reportError(response, HttpServletResponse.SC_FORBIDDEN,
					FORBIDDEN_MSG);
			return;
		}

		File chunkFile = new File(Chunk.getFilename(documentParam));

		BufferedReader reader = null;

		try {
			// Don't simply use the Document.getDocumentText() command, as it
			// might involve loading a huge amount of text into memory at once,
			// as in the case of some of the larger lexica and Civil War files.
			reader = new BufferedReader(new FileReader(chunkFile));
			String nextLine;
			keeper.record("Opened file");

			String filename = documentMetadata.getQuery().getDocumentID()
			.replaceAll(":", "_");

			// Don't set these until we're reasonably sure that we can read
			// an actual document, since they can't be modified once we've
			// called getWriter() or getOutputStream().
			response.setContentType("text/xml;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin","*");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ filename + ".xml");

			writer = response.getWriter();
			writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

			while ((nextLine = reader.readLine()) != null) {
				writer.println(nextLine);
			}
			keeper.record("Finished printing file");
		} catch (FileNotFoundException fnfe) {
			reportError(response, HttpServletResponse.SC_NOT_FOUND,
					NOT_FOUND_MSG);
			return;
		} catch (IOException ioe) {
			reportError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					INTERNAL_SERVER_ERROR_MSG);
			return;
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException ioe) {
				logger.error("Problem closing reader: " + ioe);
			}
		}
		keeper.record("Closed file");

		writer.flush();
		keeper.record("Flushed stream");
		keeper.stop();
		logger.info(keeper.getResults());
	}

	private void reportError(HttpServletResponse response, int status, 
			String message) {
		response.setStatus(status);
		try {
			PrintWriter writer = response.getWriter();
			writer.println(message);
			writer.flush();
		} catch (IOException ioe) {
			logger.error("Problem reporting error to user! " + ioe);
		}
		return;
	}
}
