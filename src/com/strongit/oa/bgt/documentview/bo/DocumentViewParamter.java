package com.strongit.oa.bgt.documentview.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DocumentViewParamter {
	private String mytype;

	private String state;

	private String orgIds;

	private String startUserName;

	private String excludeWorkflowType;

	private String workflowType;

	private List<String> itemsList;

	private Map<String, Object> paramsMap;

	private Map<String, String> orderMap;

	private StringBuilder customSelectItems;

	private StringBuilder customFromItems;

	private StringBuilder customQuery;

	private String curUserId;

	private String curUserOrgIg;

	private String isSuspended;

	private String businessNmae;

	private String workflowName;

	private Date processStartDateStart;

	private Date processStartDateEnd;
	
	private int doneYear;//办结年份
	public String getCurUserId() {
		return curUserId;
	}

	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}

	public String getCurUserOrgIg() {
		return curUserOrgIg;
	}

	public void setCurUserOrgIg(String curUserOrgIg) {
		this.curUserOrgIg = curUserOrgIg;
	}

	public String getExcludeWorkflowType() {
		return excludeWorkflowType;
	}

	public void setExcludeWorkflowType(String excludeWorkflowType) {
		this.excludeWorkflowType = excludeWorkflowType;
	}

	public String getMytype() {
		return mytype;
	}

	public void setMytype(String mytype) {
		this.mytype = mytype;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public StringBuilder getCustomFromItems() {
		return customFromItems;
	}

	public void setCustomFromItems(StringBuilder customFromItems) {
		this.customFromItems = customFromItems;
	}

	public StringBuilder getCustomQuery() {
		return customQuery;
	}

	public void setCustomQuery(StringBuilder customQuery) {
		this.customQuery = customQuery;
	}

	public StringBuilder getCustomSelectItems() {
		return customSelectItems;
	}

	public void setCustomSelectItems(StringBuilder customSelectItems) {
		this.customSelectItems = customSelectItems;
	}

	public List<String> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<String> itemsList) {
		this.itemsList = itemsList;
	}

	public Map<String, String> getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map<String, String> orderMap) {
		this.orderMap = orderMap;
	}

	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}

	public String getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(String isSuspended) {
		this.isSuspended = isSuspended;
	}

	public String getBusinessNmae() {
		return businessNmae;
	}

	public void setBusinessNmae(String businessNmae) {
		this.businessNmae = businessNmae;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public Date getProcessStartDateEnd() {
		return processStartDateEnd;
	}

	public void setProcessStartDateEnd(Date processStartDateEnd) {
		this.processStartDateEnd = processStartDateEnd;
	}

	public Date getProcessStartDateStart() {
		return processStartDateStart;
	}

	public void setProcessStartDateStart(Date processStartDateStart) {
		this.processStartDateStart = processStartDateStart;
	}


	public int getDoneYear() {
	    return doneYear;
	}

	public void setDoneYear(int doneYear) {
	    this.doneYear = doneYear;
	}
}
