package com.strongit.oa.common.extend.pojo;

public class GoToNextTransitionBean {
	private String processInstanceId;

	private String mainActorId;

	private String mainActorName;

	public String getMainActorName() {
		return mainActorName;
	}

	public void setMainActorName(String mainActorName) {
		this.mainActorName = mainActorName;
	}

	public String getMainActorId() {
		return mainActorId;
	}

	public void setMainActorId(String mainActorId) {
		this.mainActorId = mainActorId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
