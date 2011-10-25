package perseus.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

/**
 * Class that allows for a connection to an SQL database
*/
public class SQLHandler {

    private Connection connection = null;
    private Vector statements = null;
    private Vector callableStatements = null;
    private Vector resultSets = null;

    private static BasicDataSource offLineDataSource = null;

    private static final String DEFAULT_DATABASE_IDENTIFIER = 
	"jdbc/PerseusDB";
    private static final String WRITABLE_DATABASE_IDENTIFIER =
	"jdbc/PerseusWritableDB";
    
    private static Logger logger = Logger.getLogger(SQLHandler.class);

    /**
     * Class constructor
     *
     * @param con An instance of the {@link java.sql.Connection} Interface
    */
    public SQLHandler(Connection con) {
        if (con != null) {
	      connection = con;
        } else {
	    logger.error("Instantiated SQLHandler w/ a null connection!");
	}
    }

    /**
     * Static method to get writable connection objects.<p> Like
     * getConnection(), this method looks first for JNDI connection pools (as
     * in servlet environment) then falls back to creating plain connections.
     * Note that if the current JVM is running outside of a servlet environment
     * and can't find a context, it will default to using the database
     * specified by the DBURL property--i.e., the normal (non-writable)
     * database, which should be what we want for offline testing.
     *
     * @return a {@link java.sql.Connection}
     */
    public static Connection getWritableConnection() {
	return doGetConnection(WRITABLE_DATABASE_IDENTIFIER);
    }

    /**
     * Static method to get connection objects.<p>
     * Looks first for JNDI connection pools (as in servlet environment)
     * then falls back to creating plain connections.
     *
     * @return a {@link java.sql.Connection}
     */
    public static Connection getConnection() {
	return doGetConnection(DEFAULT_DATABASE_IDENTIFIER);
    }

    /**
     * Returns the actual DataSource representing the database, for callers
     * (e.g., QueryRunner) that handle connections themselves.
     */
    public static DataSource getDataSource() {
	return getDataSource(DEFAULT_DATABASE_IDENTIFIER);
    }

    /**
     * Returns the actual DataSource representing the writable database, for
     * callers (e.g., QueryRunner) that handle connections themselves.
     */
    public static DataSource getWritableDataSource() {
	return getDataSource(WRITABLE_DATABASE_IDENTIFIER);
    }

    private static DataSource getDataSource(String dbIdentifier) {
	try {
	    if (offLineDataSource != null) {
		return offLineDataSource;
	    } else {
		Context initialContext = new InitialContext();
		Context envContext =
		    (Context) initialContext.lookup("java:comp/env");
		return (DataSource) envContext.lookup(dbIdentifier);
	    }
	} catch (NoInitialContextException nice) {
	    // There was no initial context. This is a cheesy way of 
	    // figuring out if we are running in a servlet engine or
	    // in a standalone application.
	    
	    logger.info("Using offline data source");

	    String driver = Config.getProperty("hopper.database.driver");
	    logger.info("driver = " + driver + "\n");
	    String dbURL = Config.getProperty("hopper.database.url");
	    logger.info("dbURL = " + dbURL + "\n");
	    String login = Config.getProperty("hopper.database.username");
	    logger.info("login = " + login + "\n");
	    String passwd = Config.getProperty("hopper.database.password");
	    logger.info("password = " + passwd + "\n");

	    offLineDataSource = new BasicDataSource();
	    offLineDataSource.setDriverClassName(driver);
	    offLineDataSource.setUsername(login);
	    offLineDataSource.setPassword(passwd);
	    offLineDataSource.setUrl(dbURL);

	    return offLineDataSource;
	} catch (NamingException ne) {
	    logger.error("Problem getting data source:" + ne);
	    return null;
	}
    }

    private static Connection doGetConnection(String dbIdentifier) {
	try {
	    DataSource dataSource = getDataSource(dbIdentifier);
	    return dataSource.getConnection();
	} catch (SQLException e) {
            logger.fatal("Problem getting connection: " + e);
            throw new Error(e);
	}
    }

    /**
     * This method returns all resources used by the SQLHandler.
     * Here's some sample code:
     *
     * <blockquote><pre>
     * SQLHandler handler = null;
     * Connection con = null;
     * ResultSet rs = null;
     * try {
     *    con = getDBConnection();
     *    handler = new SQLHandler(con);
     *    rs = handler.executeQuery("select * from users");
     *    // do something with the ResultSet...
     * } catch (SQLException e) {
     *   // do something with the SQLException....
     * } finally {
     *    try {
     *       if (handler != null) handler.releaseAll();
     *    } catch (SQLWarning w) {
     *       // releaseAll() had problems....
     *    }
     *
     *    try {
     *       if (con != null) con.close();
     *    } catch (SQLException s) {
     *       // Connection could not be closed!
     *    }
     * }
     *</pre></blockquote>
     *
     * @exception SQLWarning thrown if any errors occurred while cleaning up
     */
    public void releaseAll() throws SQLWarning {
        boolean hadSQLException = false;
        int failures = 0;
        StringBuffer failureMessages = new StringBuffer("");

        if (statements != null) {
            for (int i = 0; i < statements.size(); i++) {
                try {
                    Statement tmp = (Statement)statements.elementAt(i);
                    tmp.close();
                } catch (SQLException s) {
                    hadSQLException = true;
                    failures++;
                    failureMessages.append(s.getMessage()).append("\n");
                } catch (ArrayIndexOutOfBoundsException a) {
                    // won't happen....
                }
            }

            statements.removeAllElements();
            statements = null;
        }

        if (callableStatements != null) {
            for (int i = 0; i < callableStatements.size(); i++) {
                try {
                    CallableStatement tmp = (CallableStatement)callableStatements.elementAt(i);
                    tmp.close();
                } catch (SQLException s) {
                    hadSQLException = true;
                    failures++;
                    failureMessages.append(s.getMessage()).append("\n");
                } catch (ArrayIndexOutOfBoundsException a) {
                    // won't happen....
                }
            }

            callableStatements.removeAllElements();
            callableStatements = null;
        }

        if (resultSets != null) {
            for (int i = 0; i < resultSets.size(); i++) {
                try {
                    ResultSet tmp = (ResultSet)resultSets.elementAt(i);
                    tmp.close();
                } catch (SQLException s) {
                    hadSQLException = true;
                    failures++;
                    failureMessages.append(s.getMessage()).append("\n");
                } catch (ArrayIndexOutOfBoundsException a) {
                    // won't happen....
                }
            }

            resultSets.removeAllElements();
            resultSets = null;
        }

        if (hadSQLException) {
            throw new SQLWarning(failures +
                                 " close() calls failed. Reasons:\n" +
                                 failureMessages.toString());
        }
    }


    /**
     * Executes a simple query and returns the
     * result of the query in a ResultSet object.
     * This method will NOT perform inserts, updates,
     * or deletes.
     *
     * An example of how to use executeQuery():
     *
     * <blockquote><pre>
     * DBAccessor dba = null;
     * Connection con = null;
     * ResultSet rs = null;
     * try {
     *    con = getDBConnection();
     *    dba = new DBAccessor(con);
     *    rs = dba.executeQuery("select * from tradeout.tblagagency");
     *    // do something with the ResultSet...
     * } catch (SQLException e) {
     *    // do something with the SQLException...
     * } finally {
     *    try {
     *       if (dba != null) dba.cleanUpResources();
     *    } catch (SQLWarning w) {
     *       // cleanUpResources() had problems....
     *    }
     *
     *    try {
     *       if (con != null) con.close();
     *    } catch (SQLException s) {
     *       // Connection could not be closed!
     *    }
     * }
     *</pre></blockquote>
     *
     * @param query A String containing the SQL query to perform.
     * @return a ResultSet object
     * @exception SQLException
     */
    public ResultSet executeQuery(String query) throws SQLException {
        // create a new Statement
        Statement queryStatement = connection.createStatement();
        // add it to the cache
        cacheStatement(queryStatement);

        // create a new ResultSet
        ResultSet rs = queryStatement.executeQuery(query);
        // add it to the cache
        cacheResultSet(rs);
        
        return rs;
    }


    /**
     * Executes an INSERT, UPDATE, or DELETE
     * SQL statement.
     *
     * An example of how to use executeUpdate():
     *
     * <blockquote><pre>
     * SQLHandler handler = null;
     * Connection con = null;
     * int numDeleted = 0;
     *
     * try {
     *    con = getDBConnection();
     *    handler = new SQLHandler(con);
     *    numDeleted = handler.executeUpdate("delete from users");
     * } catch (SQLException e) {
     *    // do something with the SQLException...
     * } finally {
     *    try {
     *       if (dba != null) dba.releaseAll();
     *    } catch (SQLWarning w) {
     *       // releaseAll() had problems....
     *    }
     *
     *    try {
     *       if (con != null) con.close();
     *    } catch (SQLException s) {
     *       // Connection could not be closed!
     *    }
     * }
     *</pre></blockquote>
     *
     * @param update the SQL statement to perform.
     * @return the row count result of the SQL statement,
     * or 0 for statements which return nothing.
     * @exception SQLException
     */
    public int executeUpdate(String update) throws SQLException {
        // create a new Statement
        Statement updateStatement = connection.createStatement();

        // add it to the cache
        cacheStatement(updateStatement);

        int result = updateStatement.executeUpdate(update);

        return result;
    }
    
    protected void finalize() throws Throwable {
        releaseAll();
    }

    private void cacheStatement(Statement s) {
        if (statements == null)
            statements = new Vector();

        statements.addElement(s);
    }

    private void cacheCallableStatement(CallableStatement c) {
        if (callableStatements == null)
            callableStatements = new Vector();
        
        callableStatements.addElement(c);
    }

    private void cacheResultSet(ResultSet r) {
        if (resultSets == null)
            resultSets = new Vector();

        resultSets.addElement(r);
    }
    
}
