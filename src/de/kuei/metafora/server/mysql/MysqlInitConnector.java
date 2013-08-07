package de.kuei.metafora.server.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

public class MysqlInitConnector {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("de.kuei.metafora.cavillag");
	private static String url = "jdbc:mysql://localhost/metaforainit?useUnicode=true&characterEncoding=UTF-8";
	private static String user = RESOURCE_BUNDLE.getString("username"); // put in your mysql database username here
	private static String password = RESOURCE_BUNDLE.getString("password"); // put in your mysql database password here

	private static MysqlInitConnector instance = null;

	public static synchronized MysqlInitConnector getInstance() {
		if (instance == null) {
			instance = new MysqlInitConnector();
		}
		return instance;
	}

	private Connection connection;

	private HashMap<String, Vector<ServerDescription>> server;
//	private Vector<ChannelDescription> xmppChannels;
//	private HashMap<String, String> general;

	private MysqlInitConnector() {
		server = new HashMap<String, Vector<ServerDescription>>();
//		xmppChannels = new Vector<ChannelDescription>();
//		general = new HashMap<String, String>();

		System.err.println("Starting MysqlInitConnector...");

		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				System.err.println("Loading init data from "
						+ MysqlInitConnector.url);
				connection = DriverManager.getConnection(
						MysqlInitConnector.url, MysqlInitConnector.user,
						MysqlInitConnector.password);
			} catch (SQLException e) {
				System.err.println("Loading init failed because "
						+ e.getMessage());
				try {
					// Use metafora as backup if there is no local database
					url = "jdbc:mysql://metafora.ku-eichstaett.de/metaforafallback?useUnicode=true&characterEncoding=UTF-8";
					System.err
							.println("Loading init data from fallback server "
									+ url);
					connection = DriverManager.getConnection(
							MysqlInitConnector.url, MysqlInitConnector.user,
							MysqlInitConnector.password);
				} catch (SQLException ex) {
					connection = null;
					ex.printStackTrace();
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadData(String modul) {
		if (connection == null) {
			System.err.println("Connection to MySQL server failed!");
			return;
		}

		System.err.println("Loading data from metaforainit...");

		PreparedStatement pst;
		try {
			pst = connection.prepareStatement("SELECT connectionname, channel, alias, user, modul FROM channeldata WHERE modul LIKE ?");
			pst.setString(1, modul);
			ResultSet rs = pst.executeQuery();

			if (rs.first()) {
				do {
					String channel = rs.getString("channel");
					String alias = rs.getString("alias");
					String user = rs.getString("user");
					String mod = rs.getString("modul");
					String connectionname = rs.getString("connectionname");
//					xmppChannels.add(new ChannelDescription(channel, alias,	user, mod, connectionname));
				} while (rs.next());
			}

			rs.close();
			pst.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			pst = connection.prepareStatement("SELECT connectionname, servername, serverurl, user, password, device, modul FROM serverdata WHERE modul LIKE ? OR modul IS NULL");
			pst.setString(1, modul);
			ResultSet rs = pst.executeQuery();

			if (rs.first()) {
				do {
					String servername = rs.getString("servername");
					String serverUrl = rs.getString("serverurl");
					String user = rs.getString("user");
					String password = rs.getString("password");
					String device = rs.getString("device");
					String mod = rs.getString("modul");
					String connectionname = rs.getString("connectionname");

					ServerDescription description = new ServerDescription(
							serverUrl, user, password, device, mod,
							connectionname);
					Vector<ServerDescription> descs = null;
					if (server.containsKey(servername)) {
						descs = server.get(servername);
					} else {
						descs = new Vector<ServerDescription>();
						server.put(servername, descs);
					}
					descs.add(description);

				} while (rs.next());
			}

			rs.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			pst = connection.prepareStatement("SELECT `key`, `value` FROM generalModul  WHERE modul LIKE ? OR modul IS NULL");
			pst.setString(1, modul);
			ResultSet rs = pst.executeQuery();

			System.err.println("General:");

			if (rs.first()) {
				do {
					String key = rs.getString("key");
					String value = rs.getString("value");

					System.err.println("Key: " + key + ", Value: " + value);

//					general.put(key, value);

				} while (rs.next());
			}

			rs.close();
			pst.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	public Vector<ChannelDescription> getXMPPChannels() {
//		return xmppChannels;
//	}

	public Vector<ServerDescription> getServer(String name) {
		return server.get(name);
	}

	public ServerDescription getAServer(String name) {
		Vector<ServerDescription> servers = server.get(name);
		if (servers != null && servers.size() > 0) {
			return servers.firstElement();
		} else {
			return null;
		}
	}

//	public String getParameter(String key) {
//		return general.get(key);
//	}
}