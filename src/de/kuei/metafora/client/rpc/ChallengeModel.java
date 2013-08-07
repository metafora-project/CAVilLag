package de.kuei.metafora.client.rpc;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;


public class ChallengeModel implements Serializable {
	private int challengeId;
	private String challengeName;
	private String challengeUrl;
	private String template;
	private String lastUsed;

	public ChallengeModel() {
		super();
	}
	
	public ChallengeModel(int challengeId, String challengeName, String challengeUrl, String template, String lastUsed) {
		super();
		this.challengeId = challengeId;
		this.challengeName = challengeName;
		this.challengeUrl = challengeUrl;
		this.template = template;
		this.lastUsed = lastUsed;
		
	}
	
	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public String getChallengeUrl() {
		return challengeUrl;
	}

	public void setChallengeUrl(String challengeUrl) {
		this.challengeUrl = challengeUrl;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

		public String getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(String lastUsed) {
		this.lastUsed = lastUsed;
	}

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<ChallengeModel> KEY_PROVIDER = new ProvidesKey<ChallengeModel>() {
		@Override
		public Object getKey(ChallengeModel item) {
			System.out.println("ChallengeInfo.ProvidesKey.getKey called");
			return item == null ? null : item.getChallengeId();
		}
	};

}
