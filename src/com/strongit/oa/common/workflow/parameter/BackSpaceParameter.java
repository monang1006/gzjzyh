package com.strongit.oa.common.workflow.parameter;

import java.io.File;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 15, 2012 12:27:31 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.workflow.parameter.BackSpaceParameter
 */
public class BackSpaceParameter extends Parameter {
	/**
	 * 任务id
	 */
	private String taskId;

	/**
	 * 需要退回到的目标节点
	 */
	private String returnNodeId;

	/**
	 * 表单id
	 */
	private String formId;


	/**
	 * 人员ID
	 */
	private String curUserId;

	private String instaceId;

	/**
	 * 表单数据
	 */
	private File formData;

	private String[] taskActors;

	public String getCurUserId() {
		return curUserId;
	}

	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}

	public File getFormData() {
		return formData;
	}

	public void setFormData(File formData) {
		this.formData = formData;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getReturnNodeId() {
		return returnNodeId;
	}

	public void setReturnNodeId(String returnNodeId) {
		this.returnNodeId = returnNodeId;
	}


	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String[] getTaskActors() {
		if (null != taskActors && taskActors.length != 0) {
			return taskActors;
		} else {
			return null;
		}
	}

	public void setTaskActors(String[] taskActors) {
		this.taskActors = taskActors;
	}

	public String getInstaceId() {
		return instaceId;
	}

	public void setInstaceId(String instaceId) {
		this.instaceId = instaceId;
	}
}
