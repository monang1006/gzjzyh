package com.strongit.oa.senddoc.bo;

public class UserBeanTemp {
	private String userId;

	private String userName;

	private String orgId;

	private String orgName;

	private String rest1;

	private Long userSequence;
	
	private Long orgSequence;

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

	public UserBeanTemp(String userId, String userName, String orgId,
			String orgName) {
		this.userId = userId;
		this.userName = userName;
		this.orgId = orgId;
		this.orgName = orgName;
	}

	public UserBeanTemp() {
	}

	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	public Long getUserSequence() {
		return userSequence;
	}

	public void setUserSequence(Long userSequence) {
		this.userSequence = userSequence;
	}

	public Long getOrgSequence() {
		return orgSequence;
	}

	public void setOrgSequence(Long orgSequence) {
		this.orgSequence = orgSequence;
	}

}
