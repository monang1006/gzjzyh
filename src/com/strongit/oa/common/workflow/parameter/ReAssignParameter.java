package com.strongit.oa.common.workflow.parameter;

import java.io.File;

public class ReAssignParameter  extends Parameter{

	/**
	 * 任务实例Id
	 */
	private String taskId;

	/**
	 * 重新指派人员Id
	 */
	private String reAssignActorId;

	/**
	 * 指派是否需要返回
	 */
	private String isNeedReturn;

	/**
	 * 电子表单数据,因为指派时需要先保存电子表单数据
	 */
	private File formData;

	/**
	 * 是否允许变更主办人员
	 */
	private String allowChangeMainActor;

	public String getAllowChangeMainActor() {
		return allowChangeMainActor;
	}

	public void setAllowChangeMainActor(String allowChangeMainActor) {
		this.allowChangeMainActor = allowChangeMainActor;
	}

	public File getFormData() {
		return formData;
	}

	public void setFormData(File formData) {
		this.formData = formData;
	}

	public String getIsNeedReturn() {
		return isNeedReturn;
	}

	public void setIsNeedReturn(String isNeedReturn) {
		this.isNeedReturn = isNeedReturn;
	}

	public String getReAssignActorId() {
		return reAssignActorId;
	}

	public void setReAssignActorId(String reAssignActorId) {
		this.reAssignActorId = reAssignActorId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
