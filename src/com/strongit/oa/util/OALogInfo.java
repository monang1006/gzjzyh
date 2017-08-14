package com.strongit.oa.util;

import java.util.Date;

/**
 * <p>Title: OALogInfo.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-11-23 16:29:21
 * @version  1.0
 */
public class OALogInfo {
	
	private String logId;						//日志ID
	
	private String opeUser;						//系统操作人
	
	private Date opeTime;						//操作时间
	
	private String opeIp;						//操作IP
	
	private String logInfo;						//日志信息
	
	private String logState;					//日志状态 0:日志无效状态  1：有效日志
	
	private String logModule;					//产生日志模块标识
	
	public OALogInfo(String opeIp,String logInfo){
		this.opeUser = null;
		this.opeTime = new Date();
		this.opeIp = opeIp;
		this.logInfo = logInfo;
		this.logState = "1";
		this.logModule = null;
	}
	
	public OALogInfo(String logInfo){
		this.opeUser = null;
		this.opeTime = new Date();
		this.opeIp = null;
		this.logInfo = logInfo;
		this.logState = "1";
		this.logModule = null;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getLogModule() {
		return logModule;
	}

	public void setLogModule(String logModule) {
		this.logModule = logModule;
	}

	public String getLogState() {
		return logState;
	}

	public void setLogState(String logState) {
		this.logState = logState;
	}

	public String getOpeIp() {
		return opeIp;
	}

	public void setOpeIp(String opeIp) {
		this.opeIp = opeIp;
	}

	public Date getOpeTime() {
		return opeTime;
	}

	public void setOpeTime(Date opeTime) {
		this.opeTime = opeTime;
	}

	public String getOpeUser() {
		return opeUser;
	}

	public void setOpeUser(String opeUser) {
		this.opeUser = opeUser;
	}

}
