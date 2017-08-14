package com.strongit.oa.common.workflow.model;

/**
 * 
 * 扩展TaskBean，添加一些业务属性
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Feb 8, 2012 4:17:03 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.model.TaskBusinessBean
 */
public class TaskBusinessBean extends TaskBean {

	private String mainActorName;

	/**
	 * 对应业务数据表中字段为PERSON_CONFIG_FLAG的值
	 */
	private String personConfigFlag;

	/**
	 * 实例对应业务数据表中字段为END_TIME的值,若不存在该字段默认为null
	 */
	private String endTime;

	/**
	 * 自定义上一步处理人信息
	 */
	private String preTaskActorInfo;

	/**
	 * 自定义上一步处理人名称
	 */
	private String preTaskActor;

	/**
	 * 自定义上一步处理人所在部门
	 */
	private String preTaskActorOrgName;

	/**
	 * 自定义当前处理人信息
	 */
	private String curTaskActorInfo;

	private String businessType;// 业务类型，办公厅业务中存在自办文时办理意见征询的业务。这类数据在在办事宜中出现（流程挂起状态），类型：0.

	private boolean isFirstYjzx;

	/**
	 * @field isReceived	任务是否被签收
	 */
	private String isReceived;
	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPersonConfigFlag() {
		return personConfigFlag;
	}

	public void setPersonConfigFlag(String personConfigFlag) {
		this.personConfigFlag = personConfigFlag;
	}

	public String getPreTaskActorInfo() {
		return preTaskActorInfo;
	}

	public void setPreTaskActorInfo(String preTaskActorInfo) {
		this.preTaskActorInfo = preTaskActorInfo;
	}

	public String getCurTaskActorInfo() {
		return curTaskActorInfo;
	}

	public void setCurTaskActorInfo(String curTaskActorInfo) {
		this.curTaskActorInfo = curTaskActorInfo;
	}

	public String getPreTaskActor() {
		return preTaskActor;
	}

	public void setPreTaskActor(String preTaskActor) {
		this.preTaskActor = preTaskActor;
	}

	public String getPreTaskActorOrgName() {
		return preTaskActorOrgName;
	}

	public void setPreTaskActorOrgName(String preTaskActorOrgName) {
		this.preTaskActorOrgName = preTaskActorOrgName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getMainActorName() {
		return mainActorName;
	}

	public void setMainActorName(String mainActorName) {
		this.mainActorName = mainActorName;
	}

	public boolean isFirstYjzx() {
		return isFirstYjzx;
	}

	public void setFirstYjzx(boolean isFirstYjzx) {
		this.isFirstYjzx = isFirstYjzx;
	}

	public String getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(String isReceived) {
		this.isReceived = isReceived;
	}

}
