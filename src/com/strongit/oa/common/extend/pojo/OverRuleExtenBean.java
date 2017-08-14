package com.strongit.oa.common.extend.pojo;

import com.strongit.oa.common.workflow.parameter.BackSpaceParameter;


public class OverRuleExtenBean {
	private String taskId;

	private String backType;

	private String suggestion;

	private String curUserId;

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public String getCurUserId() {
		return curUserId;
	}

	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public OverRuleExtenBean() {
	}
	public OverRuleExtenBean(BackSpaceParameter parameter){
		 this.taskId = parameter.getTaskId();
		 this.suggestion = parameter.getSuggestion();
		 this.curUserId = parameter.getCurUserId();
	}
}
