package perseus.ie;

import java.util.*;
import java.util.regex.*;
import java.sql.*;

import org.apache.log4j.Logger;

import perseus.util.*;

public class ReferenceList {

    private static Logger logger = Logger.getLogger(ReferenceList.class);

    String shortName;
    String fullName;
    int listID;
    
    static HashMap referenceLists;
    
    static {
	referenceLists = new HashMap();
    }

    public static ReferenceList get(int listID) {
	Integer key = new Integer(listID);
	if (referenceLists.containsKey(key)) {
	    return (ReferenceList) referenceLists.get(key);
	}
	else {
	    ReferenceList list = new ReferenceList(listID);
	    referenceLists.put(key, list);
	    return list;
	}
    }

    private ReferenceList (int listID) {
	this.listID = listID;

	ResultSet rs = null;
        Connection con = null;
        SQLHandler sqlHandler = null;

        try {
            con = SQLHandler.getConnection();

            String sql = "SELECT * FROM reference_list WHERE list_id = " + listID;

            sqlHandler = new SQLHandler(con);
            rs = sqlHandler.executeQuery(sql);

            if (rs.next()) {
                shortName = rs.getString("short_name");
                fullName = rs.getString("full_name");
            }
	    
	} catch (SQLException e) {
	    logger.fatal("Problem retrieving reference list info for id [" + listID + "]", e);
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

    public String getShortName() {
	return shortName;
    }
    
    public String getFullName() {
	return fullName;
    }

    public int getListID() {
	return listID;
    }
}

