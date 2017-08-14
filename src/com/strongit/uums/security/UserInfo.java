package com.strongit.uums.security;

import java.util.List;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.User;

import com.strongit.uums.bo.TUumsBaseRole;

/**
 * 当前登录用户信息类
 * 
 * @author 喻斌
 * @company Strongit (C) copyright
 * @time Jan 5, 2009
 * @version 1.0.0.0
 * @comment
 */
public class UserInfo extends User {

	private String orgId;

	private String userId;

	private String userSyscode;

	private String userRealName;

	private String userPassword;

	private String userUsbkey;

	private String userIsactive;

	private String userIsSupManager;

	private String userPubkey;

	private String userTel;

	private String userAddr;

	private String userEmail;

	private String userDescription;

	private String rest1;

	private String rest2;

	private String rest3;

	private String rest4;
	// 委托资源权限编码标识
	private String abrolePrivilStr;

	private String supOrgCode;// 用户所属机构编码

	private GrantedAuthority[] supAuthorities;
	
	private String loginDate;
	
	private String loginIp;
	//用户拥有访问权限的应用系统编码和权限等级信息
	private String systemCodes;

	private List<TUumsBaseRole> role;
	private boolean isSubmit;
	private boolean isAdmin;
	public boolean getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(boolean isSubmit) {
		this.isSubmit = isSubmit;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<TUumsBaseRole> getRole() {
		return role;
	}

	public void setRole(List<TUumsBaseRole> role) {
		this.role = role;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	public String getRest3() {
		return rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	public String getRest4() {
		return rest4;
	}

	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}

	public String getUserAddr() {
		return userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserIsactive() {
		return userIsactive;
	}

	public void setUserIsactive(String userIsactive) {
		this.userIsactive = userIsactive;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userName) {
		this.userRealName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPubkey() {
		return userPubkey;
	}

	public void setUserPubkey(String userPubkey) {
		this.userPubkey = userPubkey;
	}

	public String getUserSyscode() {
		return userSyscode;
	}

	public void setUserSyscode(String userSyscode) {
		this.userSyscode = userSyscode;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getUserUsbkey() {
		return userUsbkey;
	}

	public void setUserUsbkey(String userUsbkey) {
		this.userUsbkey = userUsbkey;
	}

	public UserInfo(String username, String password, boolean enabled,
			GrantedAuthority[] authorities) throws IllegalArgumentException {
		super(username, password, enabled, authorities);
	}

	public String getUserIsSupManager() {
		return userIsSupManager;
	}

	public void setUserIsSupManager(String userIsSupManager) {
		this.userIsSupManager = userIsSupManager;
	}

	public String getAbrolePrivilStr() {
		return abrolePrivilStr;
	}

	public void setAbrolePrivilStr(String abrolePrivilStr) {
		this.abrolePrivilStr = abrolePrivilStr;
	}

	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId
	 *            the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the supOrgCode
	 */
	public String getSupOrgCode() {
		return supOrgCode;
	}

	/**
	 * @param supOrgCode
	 *            the supOrgCode to set
	 */
	public void setSupOrgCode(String supOrgCode) {
		this.supOrgCode = supOrgCode;
	}

	public GrantedAuthority[] getSupAuthorities() {
		return super.getAuthorities();
	}

	public void setSupAuthorities(GrantedAuthority[] supAuthorities) {
		super.setAuthorities(supAuthorities);
	}

	public String getSystemCodes() {
		return systemCodes;
	}

	public void setSystemCodes(String systemCodes) {
		this.systemCodes = systemCodes;
	}

}
