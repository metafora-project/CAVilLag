package de.kuei.metafora.server;

import javax.servlet.http.HttpServlet;

import de.kuei.metafora.server.mysql.MysqlInitConnector;
import de.kuei.metafora.server.mysql.MysqlConnector;
import de.kuei.metafora.server.mysql.ServerDescription;

public class StartupServlet extends HttpServlet {

	public void init() {
		// init mysql
		MysqlInitConnector.getInstance().loadData("cavillag");
		ServerDescription mysqlServer = MysqlInitConnector.getInstance().getAServer("mysql");

		MysqlConnector.url = "jdbc:mysql://" + mysqlServer.getServer() + "/metafora?useUnicode=true&characterEncoding=UTF-8";
		MysqlConnector.user = mysqlServer.getUser();
		MysqlConnector.password = mysqlServer.getPassword();

	}

}
