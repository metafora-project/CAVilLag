package de.kuei.metafora.client.rpc;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface NodeServiceAsync {

	void getNodes(String language, AsyncCallback<Vector<String[]>> callback);

	void getChallenges(AsyncCallback<Vector<ChallengeModel>> callback);

	void updateVisualLanguage(int challengeId, String template, AsyncCallback<Void> callback);

	void addNewChallenge(String name, String url, AsyncCallback<Boolean> asyncCallback);

	void updateChallengeUrl(int challengeId, String url, AsyncCallback<Boolean> callback);

	void updateChallengeName(int challengeID, String name, AsyncCallback<Boolean> asyncCallback);

}
