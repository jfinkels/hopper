package perseus.chunking;

import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.dao.ChunkDAO;
import perseus.document.dao.HibernateChunkDAO;

public class EntryGroup {
	private static Logger logger = Logger.getLogger(EntryGroup.class);

	public static final int GROUP_SIZE = 50;

	private ChunkDAO dao = null;
	
	private Semaphore chunkSem;

	public EntryGroup(Semaphore chunkSem) {
		this.chunkSem = chunkSem;
		dao = new HibernateChunkDAO();
	}

	public void addEntryGroups(String documentID) {
		List<Chunk> letterChunks = dao.getAllChunks(documentID, "alphabetic letter");

		if (letterChunks.size() > 0) {
			for (Chunk chunk : letterChunks) {
				addEntryGroups(chunk);
			}
		} else {
			Query documentQuery = new Query(documentID);
			Chunk documentChunk = Chunk.getInitialChunk(documentQuery);
			addEntryGroups(documentChunk);
		}
	}

	private void addEntryGroups(Chunk containingChunk) {
		List<Chunk> entryChunks = dao.getContainedChunks(containingChunk, "entry");

		int currentStart = 0;
		int currentEnd = 0;

		int entryGroupCount = 1;

		String documentID = containingChunk.getQuery().getDocumentID();
		String subjectLanguage =
			containingChunk.getMetadata().get(Metadata.SUBJECT_LANGUAGE_KEY);

		while (entryChunks.size() > currentStart) {
			currentEnd = currentStart + GROUP_SIZE - 1;
			if (currentEnd >= entryChunks.size()) {
				currentEnd = entryChunks.size() - 1;
			}

			Chunk startChunk = entryChunks.get(currentStart);
			Chunk endChunk = entryChunks.get(currentEnd);

			String head;
			if (startChunk.getHead().equals(endChunk.getHead())) {
				head = startChunk.getHead();
			}
			else {
				head = startChunk.getHead() + " - " + endChunk.getHead();
			}

			Chunk entryGroup = new Chunk();
			entryGroup.setDocumentID(documentID);
			entryGroup.setType("entry group");
			entryGroup.setValue(String.valueOf(entryGroupCount));
			entryGroup.setStartOffset(startChunk.getStartOffset());
			entryGroup.setEndOffset(endChunk.getEndOffset());
			entryGroup.setHead(head);
			entryGroup.setHasCustomHead(true);
			entryGroup.setHeadLanguage(subjectLanguage);

			entryGroupCount++;
			currentStart += GROUP_SIZE;

			try {
				chunkSem.acquire();
			} catch (InterruptedException e) {
				logger.trace("Thread interrupted");
			}
			dao.save(entryGroup);
			chunkSem.release();
		}
	}

}
