package com.strongit.oa.common.service.adapter.bo;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 17, 2012 4:34:16 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.service.adapter.bo.EntrustWorkflowParameter
 */
public class EntrustWorkflowParameter {
    private String workflowType;

    private String assignType;

    private String excludeWorkflowType;

    public String getAssignType() {
	return assignType;
    }

    public void setAssignType(String assignType) {
	this.assignType = assignType;
    }

    public String getExcludeWorkflowType() {
	return excludeWorkflowType;
    }

    public void setExcludeWorkflowType(String excludeWorkflowType) {
	this.excludeWorkflowType = excludeWorkflowType;
    }

    public String getWorkflowType() {
	return workflowType;
    }

    public void setWorkflowType(String workflowType) {
	this.workflowType = workflowType;
    }
}
