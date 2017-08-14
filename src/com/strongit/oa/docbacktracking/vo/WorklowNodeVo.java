package com.strongit.oa.docbacktracking.vo;

/**
 * 存放节点信息
 * 
 * @author yanjian
 * 
 * 
 * Oct 10, 2012 9:51:43 AM
 */
public class WorklowNodeVo {
	private Long nodeId = -1L;

	private String nodeName;

	private String workflowName;

	private String taskId;

	private String processId;

	private boolean disable = true;
	
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

}
