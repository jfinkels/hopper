package perseus.ie;

import java.util.*;
import java.util.regex.*;
import java.sql.*;

import org.apache.log4j.Logger;

import perseus.util.*;

public class ReferenceEntry {

    private static Logger logger = Logger.getLogger(ReferenceEntry.class);

    /** local references are to documents that are part of this digital library */
    public static final int LOCAL_REFERENCE = 1;
    /** remote references are to documents that are on other sites */
    public static final int REMOTE_REFERENCE = 2;

    int entryID;
    ReferenceList referenceList;
    // Language code?
    String entryReference;
    String displayName;
    
    public ReferenceEntry (int entryID) {
	this.entryID = entryID;

	ResultSet rs = null;
        Connection con = null;
        SQLHandler sqlHandler = null;

        try {
            con = SQLHandler.getConnection();

            String sql = "SELECT * FROM reference_entry WHERE entry_id = " + entryID;

            sqlHandler = new SQLHandler(con);
            rs = sqlHandler.executeQuery(sql);

            if (rs.next()) {
		referenceList = ReferenceList.get(rs.getInt("list_id"));
                entryReference = rs.getString("entry_ref");
		displayName = rs.getString("display_name");
            }
	    
	} catch (SQLException e) {
	    logger.fatal("Problem retrieving reference entry info for id [" + entryID + "]", e);
	} finally {
	    try {
		if (sqlHandler != null) {
		    sqlHandler.releaseAll();
		}
	    } catch (SQLWarning w) {
		logger.fatal("Problem releasing resources", w);
	    }
	    try {
		if (con != null) con.close();
	    } catch (SQLException s) {
		logger.fatal("Problem releasing connection", s);
	    }
	}
    }

    public ReferenceList getReferenceList() {
	return referenceList;
    }

    public int getEntryType () {
	if (entryReference.startsWith("http:")) {
	    return REMOTE_REFERENCE;
	}
	else {
	    return LOCAL_REFERENCE;
	}
    }

    /** Returns the identifier (local query or remote URL) of the reference
	entry */
    public String getEntryReference() {
	return entryReference;
    }
    
    public int getEntryID () {
	return entryID;
    }
}

