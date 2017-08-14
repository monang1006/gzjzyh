package com.strongit.oa.common.workflow.service.nodeservice.pojo;

public class BackBean {

	private String taskNodeId;

	private String taskNodeName;

	private String processInstanceId;

	private String taskId;

	private String aiActorId;

	private String aiActorName;

	private String aiOpersonname;

	private String aiOpersonid;

	/**
	 * 获取处理用户id
	 * 
	 * @author 严建
	 * @return
	 * @createTime Apr 2, 2012 9:12:29 PM
	 */
	public String getUserId() {
		return (getAiOpersonid() != null && !"".equals(getAiOpersonid()) ? getAiOpersonid()
				: getAiActorId());
	}

	/**
	 * 获取处理用户名称
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 2, 2012 9:12:47 PM
	 */
	public String getUserName() {
		return (getAiOpersonname() != null && !"".equals(getAiOpersonname()) ? getAiOpersonname()
				: getAiActorName());
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public String getAiActorId() {
		return aiActorId;
	}

	public void setAiActorId(String aiActorId) {
		this.aiActorId = aiActorId;
	}

	public String getAiActorName() {
		return aiActorName;
	}

	public void setAiActorName(String aiActorName) {
		this.aiActorName = aiActorName;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskNodeId() {
		return taskNodeId;
	}

	public void setTaskNodeId(String taskNodeId) {
		this.taskNodeId = taskNodeId;
	}

	public String getTaskNodeName() {
		return taskNodeName;
	}

	public void setTaskNodeName(String taskNodeName) {
		this.taskNodeName = taskNodeName;
	}

	public String getAiOpersonid() {
		return aiOpersonid;
	}

	public void setAiOpersonid(String aiOpersonid) {
		this.aiOpersonid = aiOpersonid;
	}

	public String getAiOpersonname() {
		return aiOpersonname;
	}

	public void setAiOpersonname(String aiOpersonname) {
		this.aiOpersonname = aiOpersonname;
	}
}
