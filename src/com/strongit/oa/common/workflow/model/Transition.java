package com.strongit.oa.common.workflow.model;

public class Transition {
	//流程流向ID
	private String transitionId;
	//流向名称
	private String transitionName;
	
	Transition() {
		
	}

	public String getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}

	public String getTransitionName() {
		return transitionName;
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}	
}
