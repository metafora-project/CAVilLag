package de.kuei.metafora.client.rpc;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("nodeservice")
public interface NodeService extends RemoteService{
	
	public Vector<String[]> getNodes(String language);
	
	public Vector<ChallengeModel> getChallenges();
	
	public void updateVisualLanguage(int challengeId, String template);

	boolean addNewChallenge(String name, String url);

	boolean updateChallengeUrl(int challengeId, String url);

	boolean updateChallengeName(int challengeID, String name);

	boolean addNewChallenge(String name, String url, String template); 

}
