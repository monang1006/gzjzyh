package com.strongit.oa.work.util;

/**
 * 工作流Bean
 * @author 邓志城
 *
 */
public class WorkFlowBean {

	/**
	 * 工作流流程类型ID
	 */
	private String workFlowTypeId;
	/**
	 * 工作流流程类型名称
	 */
	private String workFlowTypeName	;
	/**
	 * 流程启动表单ID
	 */
	private String formId	;
	/**
	 * 工作流流程名称
	 */
	private String workFlowName;
	/**
	 * 工作流流程ID
	 */
	private String workFlowId;
	/**
	 * 已结束流程数量
	 */
	private String overWorkflowCount	;
	/**
	 * 办理中流程数量
	 */
	private String dingWorkflowCount	;
	
	/**
	 * 流水号
	 */
	private String flowNum;
	/**
	 * 工作名称
	 */
	private String bussinessName;
	/**
	 * 流程开始时间
	 */
	private String flowStartDate;
	/**
	 * 流程结束时间
	 */
	private String flowEndDate;
	/**
	 * 流程状态
	 */
	private String flowStatus;
	
	/**
	 * 业务数据
	 */
	private String businessId;
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getWorkFlowTypeId() {
		return workFlowTypeId;
	}
	public void setWorkFlowTypeId(String workFlowTypeId) {
		this.workFlowTypeId = workFlowTypeId;
	}
	public String getWorkFlowTypeName() {
		return workFlowTypeName;
	}
	public void setWorkFlowTypeName(String workFlowTypeName) {
		this.workFlowTypeName = workFlowTypeName;
	}
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public String getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(String workFlowId) {
		this.workFlowId = workFlowId;
	}
	public String getDingWorkflowCount() {
		return dingWorkflowCount;
	}
	public void setDingWorkflowCount(String dingWorkflowCount) {
		this.dingWorkflowCount = dingWorkflowCount;
	}
	public String getOverWorkflowCount() {
		return overWorkflowCount;
	}
	public void setOverWorkflowCount(String overWorkflowCount) {
		this.overWorkflowCount = overWorkflowCount;
	}
	public String getBussinessName() {
		return bussinessName;
	}
	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}
	public String getFlowEndDate() {
		return flowEndDate;
	}
	public void setFlowEndDate(String flowEndDate) {
		this.flowEndDate = flowEndDate;
	}
	public String getFlowNum() {
		return flowNum;
	}
	public void setFlowNum(String flowNum) {
		this.flowNum = flowNum;
	}
	public String getFlowStartDate() {
		return flowStartDate;
	}
	public void setFlowStartDate(String flowStartDate) {
		this.flowStartDate = flowStartDate;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
}
