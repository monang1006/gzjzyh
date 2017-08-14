package com.strongit.oa.senddoc.bo;

import java.util.Date;

public class TaskBeanTemp {
	private String taskActorId = null;

	private String taskActorName = null;

	private String orgId = null;

	private String orgName = null;

	private Date start = null;

	private Date end = null;

	private String rest1;

	private String nodeName;

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public String getTaskActorId() {
		return taskActorId;
	}

	public void setTaskActorId(String taskActorId) {
		this.taskActorId = taskActorId;
	}

	public String getTaskActorName() {
		return taskActorName;
	}

	public void setTaskActorName(String taskActorName) {
		this.taskActorName = taskActorName;
	}

	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
