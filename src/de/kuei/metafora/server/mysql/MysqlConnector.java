package de.kuei.metafora.server.mysql;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Vector;

import de.kuei.metafora.client.rpc.ChallengeModel;

public class MysqlConnector {
	
	// the resource file is not part of this distribution, so you either need to create your own or directly type in your information
	// where RESOURCE_BUNDLE.getString is used
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("de.kuei.metafora.cavillag");

	public static String url;// =
														// "jdbc:mysql://localhost/metafora?useUnicode=true&characterEncoding=UTF-8";
	public static String user = RESOURCE_BUNDLE.getString("username"); // put in your mysql database username here
	public static String password = RESOURCE_BUNDLE.getString("password"); // put in your mysql database password here

	private static MysqlConnector instance = null;

	public static synchronized MysqlConnector getInstance() {
		if (instance == null) {
			instance = new MysqlConnector();
		}
		return instance;
	}

	private Connection connection;

	private MysqlConnector() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.err.println("class loaded");		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() {
		try {
			if (connection == null || !connection.isValid(5)) {
				connection = DriverManager.getConnection(url, user, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	public synchronized Vector<String[]> getNodeLanguages() {

		Statement stmt;
		try {
			stmt = getConnection().createStatement();

			Vector<String[]> languages = new Vector<String[]>();

			String sql = "SELECT * FROM language";

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String[] lang = new String[3];

				lang[0] = rs.getString("id");
				lang[1] = rs.getString("name");
				lang[2] = rs.getString("short");

				languages.add(lang);
			}

			rs.close();
			stmt.close();

			return languages;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public synchronized Vector<String[]> getNodeTypesForLanguage(String shortname) {

		Statement stmt;
		try {
			stmt = getConnection().createStatement();

			String sql = "SELECT * FROM language JOIN (SELECT TEMP2.*, languagecategory.word as langcat, languagecategory.language as langlang,  languagecategory.scalesize, languagecategory.`order` FROM (SELECT languagecategory.category, languagecategory.language, languagecategory.word, category.scalesize, category.`order` FROM languagecategory JOIN category ON category.id = languagecategory.category) AS languagecategory JOIN (SELECT temp.*, language.short, language.name as langlong FROM language JOIN (SELECT icon.id as cardid, icon.pictureurl, icon.toolurl, icon.name, icon.category, languagename.language, languagename.word FROM icon JOIN languagename ON icon.name = languagename.name) AS temp ON language.id = temp.language WHERE language.short LIKE '"
					+ shortname
					+ "') AS TEMP2 ON TEMP2.category = languagecategory.category) as TEMP3 on TEMP3.langlang = language.id WHERE language.short LIKE '"
					+ shortname + "' ORDER BY `order` ASC";

			ResultSet rs = stmt.executeQuery(sql);

			Vector<String[]> icons = new Vector<String[]>();

			while (rs.next()) {

				String[] card = new String[7];

				card[0] = rs.getString("pictureurl");
				card[1] = rs.getString("toolurl");
				card[2] = rs.getString("word");
				card[3] = rs.getString("langcat");
				card[4] = rs.getString("scalesize");
				card[5] = rs.getString("cardid");
				card[6] = rs.getString("category");

				icons.add(card);
			}

			rs.close();
			stmt.close();

			return icons;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public synchronized Vector<ChallengeModel> loadChallenges() {
		String sql = "SELECT * FROM challenge";

		Vector<ChallengeModel> challenges = new Vector<ChallengeModel>();

		Statement stmt;
		try {
			stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.first()) {
				do {
					ChallengeModel challenge = new ChallengeModel(rs.getInt("challengeId"),
							rs.getString("challengeName"), rs.getString("challengeUrl"), rs.getString("template"),
							rs.getString("lastUsed"));
					challenges.add(challenge);
				} while (rs.next());
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return challenges;
	}

	public synchronized void updateVisualLanguage(int challengeId, String template) {

		if (challengeId == -1)
			return;

		String sql = "UPDATE challenge SET template='" + template + "'  WHERE challengeId=" + challengeId;

		Statement stmt;
		try {
			stmt = getConnection().createStatement();

			stmt.executeUpdate(sql);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean updateChallengeUrl(int challengeId, String challengeUrl) {
		if (!isValidURL(challengeUrl)) {
			return false;
		}

		if (challengeId == -1)
			return false;

		String sql = "UPDATE challenge SET challengeUrl='" + challengeUrl + "'  WHERE challengeId=" + challengeId;

		Statement stmt;
		try {
			stmt = getConnection().createStatement();

			stmt.executeUpdate(sql);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public synchronized boolean createNewChallenge(String challengeName, String challengeUrl) {

		// if (challengeId == -1)
		// return;
		//
		if (!isValidURL(challengeUrl)) {
			return false;
		}

		String sql = "INSERT INTO challenge(challengeName, challengeUrl) VALUES ('" + challengeName + "', '"
				+ challengeUrl + "')";

		Statement stmt;
		try {
			stmt = getConnection().createStatement();

			stmt.execute(sql);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private static boolean isValidURL(String url) {
		try {
			URL u = new URL(url);
			u.toURI();
		} catch (MalformedURLException e) {
			return false;
		} catch (URISyntaxException e) {
			return false;
		}
		return true;
	}

}
