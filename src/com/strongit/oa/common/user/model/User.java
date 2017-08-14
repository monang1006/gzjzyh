/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.common.user.model;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.strongit.uums.bo.TUumsBaseUser;

/**
 * 内部用户对象
 * @author dengwenqiang
 * @version 1.0
 *
 */
public class User extends TUumsBaseUser {

	private static final long serialVersionUID = -3201576356766431694L;

	private String orgName;
	
	private String expresslyPassword;
	
	private Long orgSequence;//用户所在机构的排序号 jianggb 2014-03-02
	
	public Long getOrgSequence() {
		return orgSequence;
	}

	public void setOrgSequence(Long orgSequence) {
		this.orgSequence = orgSequence;
	}

	/**
	 * 得到当前用户明文密码,通过登录时写入SESSION中的密码获取.
	 * @author:邓志城
	 * @date:2010-6-24 上午11:59:26
	 * @return 密码明文
	 */
	public String getUserExpresslyPassword(){
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			return (String)session.getAttribute("ExpresslyPassword");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return expresslyPassword;
	}
	
	public void setUserExpresslyPassword(String expresslyPassword) {
		this.expresslyPassword = expresslyPassword;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
