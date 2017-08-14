package com.strongit.workflow.workflowreport;
/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-6 下午01:36:44
 * Autour: lanlc
 * Version: V1.0
 * Description：报表javabean方式提供数据源
 */
public class WorkFlowTypeDataBean {
	//流程ID
	private String processId = null;
	//流程名称
	private String processName = null;
	//待办流程数量
	private Integer processTodo = null;
	//已办流程数量
	private Integer processDone = null;
	
	public WorkFlowTypeDataBean(){
	}
	
	public WorkFlowTypeDataBean(String wprocessId,String wprocessName,Integer wprocessTodo,Integer wprocessDone){
		processId = wprocessId;
		processName = wprocessName;
		processTodo = wprocessTodo;
		processDone = wprocessDone;
	}
	
	public Integer getProcessDone() {
		return processDone;
	}
	public void setProcessDone(Integer processDone) {
		this.processDone = processDone;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public Integer getProcessTodo() {
		return processTodo;
	}
	public void setProcessTodo(Integer processTodo) {
		this.processTodo = processTodo;
	}
	
	
}
