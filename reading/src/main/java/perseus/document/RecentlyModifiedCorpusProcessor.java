package perseus.document;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A subclass of CorpusProcessor that uses the DocumentTaskManager class to
 * record successful processing of documents. When its shouldProcessDocument()
 * method is called, it checks the document's last-modified date against
 * the date that whatever task we specified was last completed, and skips the
 * document if it hasn't changed since then. An example is the CorpusChunkifier
 * class, which only chunkifies a document if its XML source file has changed
 * since it was last chunked; another is the CitationExtractor class, which
 * only runs if the document has been chunkified more recently than it has been
 * citation-extracted.
 *
 * To create your own subclass, you only need to specify under what conditions
 * your CorpusProcessor should run; you can do so by overriding either the
 * `getRequiredTasks()` method, which returns an array of strings referring to
 * names of tasks that this task depends on, or the `getDependencies()` method,
 * which returns a list of Class objects that this Class depends on to run.
 * You can override the `getTaskName()` method to set the task name that your
 * CorpusProcessor returns; by default it returns the name of the class.
 *
 * If you override `endDocument()` in a subclass, make sure to include a call
 * to `super.endDocument()`, so that the completion of the task is properly
 * recorded. Likewise, if you override `shouldProcessDocument()`, make sure
 * to include a call to `super.shouldProcessDocument()` as a necessary condition
 * for processing; this class's implementation of `shouldProcessDocument()`
 * takes care of testing the given task against its dependent tasks.
 */

public class RecentlyModifiedCorpusProcessor extends CorpusProcessor {
    protected boolean ignoreModificationDate;
    protected Logger logger = Logger.getLogger(getClass());
    private boolean documentSucceeded = true;

    public boolean shouldProcessDocument(Chunk documentChunk) {
	Date lastProcessed = null;

	String documentID = documentChunk.getDocumentID();
	String taskName = getTaskName();

	try {
	    lastProcessed = DocumentTaskManager.getManager()
		.getTimestamp(documentID, taskName);
	} catch (Exception e) {
	    logger.warn(String.format("Couldn't get timestamp for document %s, task %s",
		    documentID, taskName), e);
	}

	Date referenceDate = getReferenceDate(documentChunk);
	boolean willProcess =
	    lastProcessed == null ||
	    referenceDate == null ||
	    referenceDate.after(lastProcessed) ||
	    ignoreModificationDate();

	return willProcess;
    }
    
    public void skippedDocument(Chunk documentChunk) {
	logger.info(String.format("%s: skipping %s",
		getClass().getSimpleName(),
		documentChunk.getDocumentID()));
    }
    
    public void startDocument(Chunk documentChunk) {
	// Assume this document is successful until we're told otherwise
	documentSucceeded = true;
	
	super.startDocument(documentChunk);
    }
    
    public boolean documentSucceeded() { return documentSucceeded; }

    public void endDocument(Chunk documentChunk) {
	// Make sure to record that we completed the given task.
	if (documentSucceeded) {
	    try {
		DocumentTaskManager.getManager().set(
			documentChunk.getQuery().getDocumentID(), getTaskName());
	    } catch (SQLException e) {
		logger.warn("Problem setting timestamp", e);
	    }
	} else {
	    logger.error("Document did not succeed!: " + documentChunk.getDocumentID());
	}
    }

    /**
     * The name of the task performed by this processor. Something like
     * "chunkify" or "extract-entities"; will be the class name by default.
     */
    public String getTaskName() {
	return getClass().getName();
    }

    /**
     * Whether we should ignore the last-modified date and chunk anyway. By
     * default, no, but subclasses can change this.
     */
    public boolean ignoreModificationDate() {
	return ignoreModificationDate;
    }

    public void setIgnoreModificationDate(boolean imd) {
	ignoreModificationDate = imd;
    }

    /**
     * Returns the date to be compared against the date of the last-performed
     * task. By default, returns the modification date of the XML source file.
     * Alternatively, if getRequiredTasks() returns a nonempty array, we
     * return the most recent of the timestamps given by the tasks it
     * specifies. This allows us to create a clunky sort of "dependencies"
     * between tasks, so that "extract-entities" can be dependent on
     * "chunkify", say, and only run if "chunkify" has been run more
     * recently than "extract-entities".
     */
    public Date getReferenceDate(Chunk documentChunk) {
	Set<String> requirements = new HashSet<String>();
	for (String task : getRequiredTasks(documentChunk)) {
	    requirements.add(task);
	}
	
	for (Class dependency : getDependencies()) {
	    requirements.add(dependency.getName());
	}
	if (!requirements.isEmpty()) {
	    try {
		return getMostRecentDate(
			documentChunk.getQuery().getDocumentID(),
			requirements);
	    } catch (SQLException se) {
		logger.warn("Problem getting timestamps:", se);
		return new Date();
	    }
	}

	Metadata metadata = documentChunk.getMetadata();
	if (metadata.getSourceFile() == null) {
	    return null;
	}
	return new Date(metadata.getSourceFile().lastModified());
    }

    /**
     * Returns a list of tasks, any one of which must have been completed
     * sooner than this task was last completed for the document to be
     * considered "recently modified" and hence runnable.
     *
     * @param documentChunk a Chunk representing the document to be processed,
     * which may have some bearing on the return result
     * @return an array of names of tasks required by this task
     */
    public String[] getRequiredTasks(Chunk documentChunk) {
	return new String[0];
    }
    
    /**
     * Returns a list of RecentlyModifiedCorpusProcessor subclasses that this
     * one depends on. This can be used to establish a clunky system of
     * dependencies. Essentially, it is a slightly more typesafe version of
     * `getRequiredTasks()`.
     */
    protected Class[] getDependencies() {
	return new Class[0];
    }

    private Date getMostRecentDate(String documentID, Set<String> taskNames)
	throws SQLException {

	// Default to the epoch--anything we have ought to be more recent than
        // the epoch...
	Date mostRecent = new Date(0);
	for (String task : taskNames) {
	    Date taskCompleted = DocumentTaskManager.getManager()
		.getTimestamp(documentID, task);

	    if (taskCompleted != null && taskCompleted.after(mostRecent)) {
		mostRecent = taskCompleted;
	    }
	}

	return mostRecent;
    }
    
    @Override
    public void reportException(Chunk c, Exception ex) {
	super.reportException(c, ex);
    }
}
