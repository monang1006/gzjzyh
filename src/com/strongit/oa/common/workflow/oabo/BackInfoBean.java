package com.strongit.oa.common.workflow.oabo;

import java.util.Date;

import com.strongit.workflow.businesscustom.CustomWorkflowConst;

public class BackInfoBean {
	/**
	 * 当前任务id(不能为空)
	 */
	private String taskId;

	private Date backTime;

	/**
	 * 退文信息
	 */
	private String backInfo;

	/**
	 * 退文方式(不能为空)<br/> CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACE
	 * 退文方式：退回<br/> CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACEPREV
	 * 退文方式：退回上一步<br/> CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_OVERRULE
	 * 退文方式：驳回<br/>
	 */
	private String backType;

	private String userId;

	private String userName;

	private String orgId;

	private String orgName;

	private String suggestion;

	private String rest1;// 用户类型

	public String getBackInfo() {
		return backInfo;
	}

	public void setBackInfo(String backInfo) {
		this.backInfo = backInfo;
	}

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String backType() {
		if (backType == null || "".equals(backType)) {
			throw new NullPointerException("参数backType不能为null或空字符串");
		}
		if (backType
				.equals(CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACE)) {
			return "退回";
		}
		if (backType
				.equals(CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACEPREV)) {
			return "退回上一步";
		}
		return "驳回";
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}
}
