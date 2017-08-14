package com.strongit.oa.senddoc.pojo;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 29, 2012 9:03:21 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.senddoc.pojo.ProcessedParameter
 */
public class ProcessedParameter extends WorkflowParameter {
	private String filterSign;

	private String userId;

	private String queryWithTaskDate;

	private String filterYJZX;// 是否要过滤意见征询数据 0：不显示意见征询信息，1：显示意见征询信息

	private String workflowName;

	private String formId;

	private String workflowType = "";// 流程类型

	private String excludeWorkflowType;// 不包含的流程类型
	
	private String excludeWorkflowTypeName;// 不包含的流程类型名称

	private String userName;

	private String isBackSpace;

	private Date startDate;

	private Date endDate;
	
	private Date workflowEndDatestartDate;//办结时间的开始时间

	private Date workflowEndDateendDate;//办结时间的结束时间

	private String processStatus;

	private String sortType;

	private List taskIdList;

	private String showSignUserInfo;

	private String handleKind;

	private String type;

	private String state;

	private boolean isDraft;

	private String businessName;

	private String isSuspended;// 流程是否挂起

	private String nodeName;

	private String mainActorId;

	private boolean isHost;

	private List<String> userIds;
	
	private String orgId;//指定处室
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getIsBackSpace() {
		return isBackSpace;
	}

	public void setIsBackSpace(String isBackSpace) {
		this.isBackSpace = isBackSpace;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getShowSignUserInfo() {
		return showSignUserInfo;
	}

	public void setShowSignUserInfo(String showSignUserInfo) {
		this.showSignUserInfo = showSignUserInfo;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List getTaskIdList() {
		return taskIdList;
	}

	public void setTaskIdList(List taskIdList) {
		this.taskIdList = taskIdList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getFilterYJZX() {
		return filterYJZX;
	}

	public void setFilterYJZX(String filterYJZX) {
		this.filterYJZX = filterYJZX;
	}

	public String getFilterSign() {
		return filterSign;
	}

	public void setFilterSign(String filterSign) {
		this.filterSign = filterSign;
	}

	public String getQueryWithTaskDate() {
		return queryWithTaskDate;
	}

	public void setQueryWithTaskDate(String queryWithTaskDate) {
		this.queryWithTaskDate = queryWithTaskDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHandleKind() {
		return handleKind;
	}

	public void setHandleKind(String handleKind) {
		this.handleKind = handleKind;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getExcludeWorkflowType() {
		return excludeWorkflowType;
	}

	public void setExcludeWorkflowType(String excludeWorkflowType) {
		this.excludeWorkflowType = excludeWorkflowType;
	}

	public String getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(String isSuspended) {
		this.isSuspended = isSuspended;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getMainActorId() {
		return mainActorId;
	}

	public void setMainActorId(String mainActorId) {
		this.mainActorId = mainActorId;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getWorkflowEndDatestartDate() {
		return workflowEndDatestartDate;
	}

	public void setWorkflowEndDatestartDate(Date workflowEndDatestartDate) {
		this.workflowEndDatestartDate = workflowEndDatestartDate;
	}

	public Date getWorkflowEndDateendDate() {
		return workflowEndDateendDate;
	}

	public void setWorkflowEndDateendDate(Date workflowEndDateendDate) {
		this.workflowEndDateendDate = workflowEndDateendDate;
	}

	public String getExcludeWorkflowTypeName() {
		return excludeWorkflowTypeName;
	}

	public void setExcludeWorkflowTypeName(String excludeWorkflowTypeName) {
		this.excludeWorkflowTypeName = excludeWorkflowTypeName;
	}
	
}
