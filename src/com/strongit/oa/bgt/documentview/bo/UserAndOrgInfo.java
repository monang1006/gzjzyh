package com.strongit.oa.bgt.documentview.bo;

public class UserAndOrgInfo {
	private String userId;

	private String userName;

	private String orgId;

	private String orgName;

	private String currentActorName;

	private String currentActorId;

	private Long orgSequence;

	public String getCurrentActorId() {
		return currentActorId;
	}

	public void setCurrentActorId(String currentActorId) {
		this.currentActorId = currentActorId;
	}

	public String getCurrentActorName() {
		return currentActorName;
	}

	public void setCurrentActorName(String currentActorName) {
		this.currentActorName = currentActorName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getOrgSequence() {
		return orgSequence;
	}

	public void setOrgSequence(Long orgSequence) {
		this.orgSequence = orgSequence;
	}

}
