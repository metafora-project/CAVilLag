package de.kuei.metafora.server.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.kuei.metafora.server.StartupServlet;

public class MysqlConnector {

	private static String url = "jdbc:mysql://metafora.ku-eichstaett.de/metafora?useUnicode=true&characterEncoding=UTF-8";
	private static String user = "meta";
	private static String password = "didPfM";

	private static MysqlConnector instance = null;

	public static synchronized MysqlConnector getInstance() {
		if (instance == null) {
			instance = new MysqlConnector();
		}
		return instance;
	}

	private Connection connection;

	private MysqlConnector() {
		if (StartupServlet.productive) {
			url = "jdbc:mysql://metaforaserver.ku-eichstaett.de/metafora?useUnicode=true&characterEncoding=UTF-8";
			System.err.println("MysqlConnector: MySQL Server URL changed to "
					+ url);
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");

			System.err.println("class loaded");

			try {
				connection = DriverManager.getConnection(MysqlConnector.url,
						MysqlConnector.user, MysqlConnector.password);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	private Connection getConnection() {
		try {
			if (connection == null || !connection.isValid(5)) {
				connection = DriverManager.getConnection(MysqlConnector.url,
						MysqlConnector.user, MysqlConnector.password);
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

	public synchronized Vector<String[]> getNodeTypesForLanguage(
			String shortname) {

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

				String[] icon = new String[6];

				icon[0] = rs.getString("pictureurl");
				icon[1] = rs.getString("toolurl");
				icon[2] = rs.getString("word");
				icon[3] = rs.getString("langcat");
				icon[4] = rs.getString("scalesize");
				icon[5] = rs.getString("cardid");

				icons.add(icon);
			}

			rs.close();
			stmt.close();

			return icons;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
