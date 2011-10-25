/**
 * This class is provided as a way to keep track of how recently a given
 * document has completed a given task--like being chunkified, being indexed
 * for search, being scanned for entities, and so on. Document-processors can
 * compare timestmps on various tasks to determine whether a document is
 * up-to-date (for instance, if we see that document X was last scanned for
 * citations *after* it was chunkified, we don't need to run CitationExtractor
 * again, since the XML file hasn't changed).
 *
 * The methods here are responsible for saving, retrieving and deleting
 * document-processing-related records. We use a SQL table as the back-end;
 * it didn't seem worth writing an interface and a conforming implementation
 * for, since it's extremely doubtful that outside users will know or care at
 * all about the innards of this particular functionality, and moreover it's
 * unlikely that we'll decide we want to store this somewhere else.
 */

package perseus.document;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import perseus.util.SQLHandler;
import perseus.util.StringUtil;

public class DocumentTaskManager {

	private static DocumentTaskManager manager = new DocumentTaskManager();

	private static QueryRunner getQueryRunner() {
		return new QueryRunner(SQLHandler.getDataSource());
	}

	private static ResultSetHandler defaultHandler =
		new ScalarHandler("last_run");

	private DocumentTaskManager() {}

	private static String TABLE_NAME = "document_tasks";

	/**
	 * Returns a DocumentTaskManager, ready to add new actions or check
	 * for existing ones. As of now, this uses the Singleton design
	 * pattern--all calls return the same manager instance.
	 */ 
	public static DocumentTaskManager getManager() {
		return manager;
	}

	/**
	 * Records a timestamp for the given identifier and task. Defaults to
	 * the current time.
	 */
	public void set(String id, String task) throws SQLException {
		set(id, task, new Date());
	}

	/**
	 * Records a timestamp for the given identifier and task.
	 */
	public void set(String id, String task, Date date)
	throws SQLException {

		Timestamp timestamp = new Timestamp(date.getTime());
		Connection con = SQLHandler.getConnection();
		QueryRunner runner = new QueryRunner();
		runner.update(con,
				"REPLACE INTO " + TABLE_NAME + " VALUES (?, ?, ?)",
				new Object[] {id, task, timestamp});
		DbUtils.close(con);
	}

	/**
	 * Removes the timestamp for the given identifier and task. Alternatively,
	 * you can set id to null to delete everything of the given task, or
	 * set task to null to delete everything with the given id. Or set both
	 * parameters to null to remove everything.
	 */
	public void delete(String id, String task) throws SQLException {
		List<String> conditions = new ArrayList<String>();
		List<String> parameters = new ArrayList<String>();

		if (id != null) {
			conditions.add("document_id = ?");
			parameters.add(id);
		}

		if (task != null) {
			conditions.add("task = ?");
			parameters.add(task);
		}

		String queryString = "DELETE FROM " + TABLE_NAME +
		(conditions.isEmpty() ? "" :
			(" WHERE " + StringUtil.join(conditions, " AND ")));

		getQueryRunner().update(queryString, parameters.toArray());
	}

	public Date getTimestamp(String id, String task) throws SQLException {
		if (id == null || task == null) {
			throw new IllegalArgumentException(
					"getTimestamp() requires a non-null id and task!");
		}

		// defaultHandler being a ScalarHandler, this will return the
		// Timestamp we want. (If there is no such timestamp, it'll return
		// null.)
		Object resultStamp = (Date) getQueryRunner().query(
				"SELECT last_run FROM " + TABLE_NAME +
				" WHERE document_id = ? AND task = ?",
				new Object[] {id, task}, defaultHandler);

		if (resultStamp != null) {
			// It's true that Timestamp is a subclass of Date, but the two
			// classes have some implementation differences--see the Javadoc
			// for Timestamp. In particular, checking whether a Date is equal
			// to a Timestamp will always return false, which could confuse
			// callers trying to compare these dates with dates from elsewhere.
			// Return a normal date to avoid any weirdness.
			return new Date(((Timestamp) resultStamp).getTime());
		}

		return null;
	}

	public boolean hasTimestamp(String id, String task) throws SQLException {
		return (getTimestamp(id, task) != null);
	}
}
