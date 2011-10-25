package perseus.sharing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import perseus.document.Query;
import perseus.util.SQLHandler;

public class Partner {

	private static Logger logger = Logger.getLogger(Partner.class);

	String partnerID;
	String name;
	String description;
	String textURL;
	String oaiProviderURL;

	static HashMap cache = null;
	static {
		cache = new HashMap();
	}

	private Partner (String pid, String n, String d, String url, String oai) {
		partnerID = pid;
		name = n;
		description = d;
		textURL = url;
		oaiProviderURL = oai;
	}

	public String getPartnerID() {
		return partnerID;
	}

	public String getName() {
		return name;
	}

	public String getTextURL(Query q) {
		return textURL + q.toString();
	}

	public String getOAIProviderURL() {
		return oaiProviderURL;
	}

	public static Partner getPartner(String partnerID) {
		if (cache.containsKey(partnerID)) {
			return (Partner) cache.get(partnerID);
		}

		Partner partner = null;

		ResultSet rs = null;
		Connection con = null;
		SQLHandler sqlHandler = null;

		try {
			con = SQLHandler.getConnection();

			String sql = "SELECT * FROM partners WHERE partner_id = '" +
			partnerID + "'";

			sqlHandler = new SQLHandler(con);
			rs = sqlHandler.executeQuery(sql);

			if (rs.next()) {
				String name = rs.getString("partner_name");
				String description = rs.getString("partner_desc");
				String textURL = rs.getString("text_url");
				String oaiURL = rs.getString("oai_provider_url");

				partner = new Partner(partnerID, name, description, textURL, oaiURL);
			}

		} catch (SQLException e) {
			logger.fatal("Problem retrieving partner info for [" + partnerID + "]", e);
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

		cache.put(partnerID, partner);

		return partner;
	}

	public static List getPartners() {

		List output = new ArrayList();
		Partner partner = null;

		ResultSet rs = null;
		Connection con = null;
		SQLHandler sqlHandler = null;

		try {
			con = SQLHandler.getConnection();

			String sql = "SELECT * FROM partners";

			sqlHandler = new SQLHandler(con);
			rs = sqlHandler.executeQuery(sql);

			while (rs.next()) {
				String partnerID = rs.getString("partner_id");
				String name = rs.getString("partner_name");
				String description = rs.getString("partner_desc");
				String textURL = rs.getString("text_url");
				String oaiURL = rs.getString("oai_provider_url");

				partner = new Partner(partnerID, name, description, textURL, oaiURL);
				output.add(partner);
			}

		} catch (SQLException e) {
			logger.fatal("Problem retrieving partner info", e);
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
		return output;
	}
}
