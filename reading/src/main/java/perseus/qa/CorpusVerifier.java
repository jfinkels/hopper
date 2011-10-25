package perseus.qa;

import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import perseus.document.Chunk;
import perseus.document.CorpusProcessor;
import perseus.document.InvalidQueryException;
import perseus.document.Metadata;
import perseus.document.StyleTransformer;
import perseus.util.StringUtil;

import static perseus.qa.VerificationResult.ResultStatus.*;

public class CorpusVerifier extends CorpusProcessor {

	LinkedList<VerificationResult> results =
		new LinkedList<VerificationResult>();
	private static Logger cvLogger = Logger.getLogger(CorpusVerifier.class);

	String targetCorpus = null;
	boolean foundChunks = false;

	VerificationResult currentDocument;

	VerificationResult currentChunkResult;

	public CorpusVerifier() {
		this(null);
	}

	public CorpusVerifier(String corpus) {
		targetCorpus = corpus;
	}

	public static void main(String[] args) {

		if (args.length == 1 && args[0].equals("--help")) {
			cvLogger.error("Usage: CorpusVerifier [target collection]");
			cvLogger.error("\t(omit [target collection] to process everything");
			System.exit(1);
		} else {
			CorpusVerifier verifier;

			if (args.length > 0) {
				// Spaces in our corpus mean that it'll be interpreted as multiple
				// arguments, so put the arguments back together if we need to
				verifier = new CorpusVerifier(StringUtil.join(args, " "));
			} else {
				verifier = new CorpusVerifier();
			}

			verifier.processCorpus();
			//verifier.printResults();

			String xml = new XMLOutputter().outputString(verifier.toXML());
			String output = StyleTransformer.transform(xml, "docstatus.xsl");

			cvLogger.info(output);
		}
	}

	/** 
	Called at the beginning of every processable document.
	 */
	public void startDocument(Chunk documentChunk) {
		super.startDocument(documentChunk);

		currentDocument = new VerificationResult(documentChunk);
		results.add(currentDocument);

		currentChunkResult = currentDocument;

		cvLogger.info("Now processing document: " + documentChunk.getQuery());

		foundChunks = false;
	}

	/** 
	Called at the end of every processable document.
	 */
	public void endDocument(Chunk documentChunk) {
		super.endDocument(documentChunk);

		if (foundChunks == false) {
			cvLogger.warn("No chunks found for " + documentChunk.getQuery());

			// Create an exception, but don't throw it; simply report it in a
			// civilized manner and move on.
			InvalidQueryException iqe =
				new InvalidQueryException("No chunks found for "
						+ documentChunk.getQuery());

			reportException(documentChunk, iqe);
		}

		if (currentDocument.getStatus() != SUCCESS) {
			cvLogger.info("Finished with document: "
					+ documentChunk.getQuery()
					+ "\nStatus: " + currentDocument.getStatus());
		}
	}

	/** 
	Called at the beginning of every default chunk of
	every processable document.
	 */
	public void startChunk(Chunk chunk) {
		super.startChunk(chunk);
		cvLogger.debug("Now processing chunk " + chunk.getQuery());

		currentChunkResult = new VerificationResult(chunk);

		foundChunks = true;
	}

	/** 
	Called at the end of every default chunk of
	every processable document.
	 */
	public void endChunk(Chunk chunk) {
		super.endChunk(chunk);

		if (currentChunkResult.getStatus() != SUCCESS) {
			cvLogger.info("Finished with document: " + chunk.getQuery()
					+ "\nStatus: " + currentChunkResult.getStatus());
		}

		// Don't keep the result around if we succeed; this is especially
		// useful in the case of documents with lots and lots of chunks, most
		// of which are probably okay, like the LSJ.
		if (currentChunkResult.getStatus() != SUCCESS) {
			currentDocument.addChild(currentChunkResult);
		}
	}

	public boolean shouldProcessDocument(Chunk documentChunk) {
		Metadata metadata = documentChunk.getMetadata();

		String documentID = metadata.getDocumentID();

		// Skip these for now. They will bring disaster upon us.
		if (documentID.equals("Perseus:text:1999.04.0057")
				|| documentID.equals("Perseus:text:1999.04.0059")) {
			return false;
		}

		if (targetCorpus == null) return true;

		List corpora = metadata.getList(Metadata.CORPUS_KEY);

		if (corpora == null) return false;

		return (corpora.contains(targetCorpus));
	}

	/**
	 * Overrides the parent's method because we don't need to bother looking at
	 * every single token.
	 */
	public void processChunk(Chunk chunk) {
		startChunk(chunk);

		Exception exc = null;

		boolean success = false;

		// StyleTransformer doesn't throw back exceptions, so we'll
		// create them ourselves if we need to.
		//
		// TODO: create an unsafeTransform method or something of the sort
		// instead of doing this here
		try {
			String styledText =
				StyleTransformer.transform(chunk.getText(), "tei.xsl");

			if (styledText != null && styledText.length() > 0) {
				success = true;
			}
		} catch (Exception e) {
			exc = e;
		} finally {
			if (!success) {
				if (exc == null) {
					exc = new TransformerException(
							"Failed to style text of " + chunk.getQuery());
				}
				reportException(chunk, exc);
			}
		}

		endChunk(chunk);
	}

	public void reportException(Chunk chunk, Exception e) {
		cvLogger.error(chunk, e);
		currentChunkResult.reportError(e);
	}

	public void printResults(PrintStream out) {
		if (out == null) {
			out = System.out;
		}

		for (VerificationResult result : results) {
			out.println(result.toString());
		}
		out.println("------------------------");
	}

	public Element toXML() {
		Element root = new Element("corpusVerification")
		.setAttribute("timestamp", new Date().toString());
		if (targetCorpus != null) {
			root.setAttribute("corpus", targetCorpus);
		}

		for (VerificationResult result : results) {
			root.addContent(result.toXML());
		}

		return root;
	}
}
