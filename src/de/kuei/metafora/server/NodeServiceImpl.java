package de.kuei.metafora.server;

import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kuei.metafora.client.rpc.NodeService;
import de.kuei.metafora.server.mysql.MysqlConnector;

public class NodeServiceImpl extends RemoteServiceServlet implements NodeService{

	@Override
	public Vector<String[]> getNodes(String language) {
		return MysqlConnector.getInstance().getNodeTypesForLanguage(language);
	}

}
