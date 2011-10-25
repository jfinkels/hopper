package perseus.document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;

/** Document contains several methods relating to entire XML documents.
 The bulk of the work of the text processing
 system is done by the Chunk class. */
public class Document {

	private static Logger logger = Logger.getLogger(Document.class);

	String documentID;
	Metadata metadata;

	public Document (Query q) {
		documentID = q.getDocumentID();
		metadata = MetadataCache.get(q);
	}

	public Iterator<Chunk> getChunks() {
		return getChunks(metadata.getChunkSchemes().getDefaultScheme(),
				metadata.getDefaultChunk());
	}

	public Iterator<Chunk> getChunks(String chunkScheme, String targetType) {
		Chunk documentChunk = Chunk.getInitialChunk(new Query(documentID));
		TableOfContents toc =
			TableOfContents.forChunk(documentChunk, chunkScheme);

		return toc.getChunks(targetType);
	}

	/**
	 * Returns the entire text of the document. Useful when we just want the
	 * raw text and don't need to work with the document hierarchy.
	 */
	public String getDocumentText() {
		StringBuffer outputBuffer = new StringBuffer();

		File xmlFile = new File(Chunk.getFilename(documentID));

		Charset cs = Charset.forName("UTF-8");
		CharsetDecoder decoder = cs.newDecoder();
		FileInputStream stream = null;

		try {
			stream = new FileInputStream(xmlFile);

			FileChannel channel = stream.getChannel();
			int fileSize = (int) channel.size();

			MappedByteBuffer byteBuf = channel.map(
					FileChannel.MapMode.READ_ONLY, 0, fileSize);

			CharBuffer charBuf = decoder.decode(byteBuf);
			outputBuffer.append(charBuf.toString());

			stream.close();

		} catch (FileNotFoundException fnf) {
			logger.fatal("file not found [" + xmlFile + "]", fnf);
		} catch (CharacterCodingException cce) {
			logger.fatal("character coding wrong in getText()", cce);
		} catch (IOException ioe) {
			logger.error("Problem retrieving chunk text", ioe);
		} 	

		return outputBuffer.toString();
	}

	public static List<String> getTexts() {
		return getDocumentsByMetadata(Metadata.TYPE_KEY, "text");
	}

	public static List<String> getDocumentsByMetadata(String key, String value) {
		MetadataDAO dao = new SQLMetadataDAO();
		List<String> docList = new ArrayList<String>();

		try {
			List<Query> matches = dao.getDocuments(key, value, null, true);

			// Callers expect to be returned a list of document IDs (as
			// strings), not an array of Queries, unfortunately.
			for (Query match : matches) {
				docList.add(match.getDocumentID());
			}
		} catch (MetadataAccessException me) {
			logger.warn("Problem getting documents by metadata: " + me);
		}

		Collections.sort(docList);
		return docList;
	}

	public static long getFileSize(Query query) {
		File xmlFile = new File(Chunk.getFilename(query.getDocumentID()));

		return xmlFile.length();
	}

	public void writeChunkFiles(String path) {
		try {
			File targetDir = new File(path, documentID);

			if (! targetDir.exists()) {
				targetDir.mkdir();
			}

			GreekFilter greekFilter =
				new GreekFilter(metadata.get(Metadata.LANGUAGE_KEY), null);

			Iterator<Chunk> chunkQueries = getChunks();
			while (chunkQueries.hasNext()) {
				Chunk chunk = chunkQueries.next();
				Query query = chunk.getQuery();
				File targetFile = new File(targetDir, query.getQuery().replaceAll(":", "_") + ".xml");
				PrintWriter out = 
					new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")));
				out.print(greekFilter.filter(chunk.getText()));
				out.close();
			}

		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}	
	}

	public void compileChunkFiles(String path) {
		try {
			File targetDir = new File(path, documentID);
			File targetFile = new File(path, documentID + ".xml");

			PrintWriter out = 
				new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")));

			boolean firstChunk = true;

			Iterator<Chunk> chunkQueries = getChunks();
			while (chunkQueries.hasNext()) {
				Chunk chunk = chunkQueries.next();
				Query query = chunk.getQuery();
				// Include the text before the first chunk (ie TEI Header)
				if (firstChunk) {
					int startOffset = chunk.getStartOffset();
					out.print(Chunk.getText(documentID, 0, startOffset));
					firstChunk = false;
				}

				File sourceFile = new File(targetDir, query.getQuery().replaceAll(":", "_") + ".xml");

				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
				StringBuffer chunkText = new StringBuffer();

				String line;
				while ((line = in.readLine()) != null) {
					// Ignore XML header
					if (! line.startsWith("<?xml")) {
						// De-GATEify chunk text

						line = line.replaceAll(" gate:gateId=\"[0-9]+\"", "");
						line = line.replaceAll(" gate:annotMaxId=\"[0-9]+\"", "");
						line = line.replaceAll("  xmlns:gate=\"http://www.gate.ac.uk\"", "");

						chunkText.append(line).append("\n");
					}
				}
				in.close();


				// Remove the added start and end tags
				int start = chunk.getOpenTags().length();
				int end = chunkText.length() - chunk.getCloseTags().length() - 1;

				out.print(chunkText.substring(start, end));
			}

			out.close();

		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}	
	}

	public Metadata getMetadata() {
		return metadata;
	}

}
