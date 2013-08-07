package de.kuei.metafora.server;

import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kuei.metafora.client.rpc.ChallengeModel;
import de.kuei.metafora.client.rpc.NodeService;
import de.kuei.metafora.server.mysql.MysqlConnector;

/**
 * This class fine is part of the communication structure between the client and the server.
 * @author lingnau
 *
 */
public class NodeServiceImpl extends RemoteServiceServlet implements NodeService{

	@Override
	public Vector<String[]> getNodes(String language) {
		return MysqlConnector.getInstance().getNodeTypesForLanguage(language);
	}

	@Override
	public Vector<ChallengeModel> getChallenges() {
		return MysqlConnector.getInstance().loadChallenges();
	}

	@Override
	public void updateVisualLanguage(int challengeId, String template) {
		MysqlConnector.getInstance().updateVisualLanguage(challengeId, template);
	}

	@Override
	public boolean addNewChallenge(String name, String url) {
		return MysqlConnector.getInstance().createNewChallenge(name, url);
	}

	@Override
	public boolean updateChallengeUrl(int challengeId, String url) {
		return MysqlConnector.getInstance().updateChallengeUrl(challengeId, url);
	}

}
