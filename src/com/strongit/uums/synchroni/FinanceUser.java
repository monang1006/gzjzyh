package com.strongit.uums.synchroni;

public class FinanceUser {
	private String userId;
	private String userName;
	private String userPwd;
	private String userType;
	private String enable;
	private String isCompanyAdmin;
	public String getIsCompanyAdmin() {
		return isCompanyAdmin;
	}
	public void setIsCompanyAdmin(String isCompanyAdmin) {
		this.isCompanyAdmin = isCompanyAdmin;
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
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
}
