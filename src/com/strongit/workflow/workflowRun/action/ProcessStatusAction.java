package com.strongit.workflow.workflowRun.action;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.workflowInterface.ITaskService;

import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程处理状态Action
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Dec 15, 2008  3:11:53 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.workflowRun.action.ProcessMonitorAction
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "processStatus.action", type = ServletActionRedirectResult.class),
			@Result(name = "handle", value = "/WEB-INF/jsp/workflowRun/action/processStatus-handle.jsp", type = ServletDispatcherResult.class),
			@Result(name = "current", value = "/WEB-INF/jsp/workflowRun/action/processStatus-current.jsp", type = ServletDispatcherResult.class)})
public class ProcessStatusAction extends BaseActionSupport<Object[]> {
	private Page<Object[]> page = new Page<Object[]>(FlexTableTag.MAX_ROWS,
			true);

	private List handlerecords;
	
	private String total;
	
	private String processId;
	
	private String nodeId;
	
	private List currentActor;

	private Object[] model = new Object[]{};

	private ITaskService manager;

	public void setModel(Object[] model) {
		this.model = model;
	}

	public void setPage(Page<Object[]> page) {
		this.page = page;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public ProcessStatusAction() {

	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public Object[] getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(ITaskService aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 */
	@Override
	public String list() {
		return SUCCESS;
	}

	/**
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() {

	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@Override
	public String save() throws Exception {
		return RELOAD;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911402EE
	 */
	@Override
	public String delete() throws Exception {
		return RELOAD;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	@Override
	public String input() {
		return INPUT;
	}
	
	/**
	 * 查看流程节点已处理情况
	 * @author  喻斌
	 * @date    Dec 15, 2008  3:17:50 PM
	 * @return
	 * @throws Exception
	 */
	public String handle() throws Exception{
		handlerecords = manager.getHandleRecordByNode(processId, nodeId);
		total = String.valueOf(handlerecords.size());
		return "handle";
	}
	
	/**
	 * 查看当前处理节点的处理情况
	 * @author  喻斌
	 * @date    Dec 15, 2008  3:23:25 PM
	 * @return
	 * @throws Exception
	 */
	public String current() throws Exception{
		handlerecords = manager.getHandleRecordByNode(processId, nodeId);
		total = String.valueOf(handlerecords.size());
		currentActor = manager.getCurrentHandleByNode(processId, nodeId);
		return "current";		
	}
	
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public List getHandlerecords() {
		return handlerecords;
	}

	public void setHandlerecords(List handlerecords) {
		this.handlerecords = handlerecords;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public void setCurrentActor(List currentActor) {
		this.currentActor = currentActor;
	}

	public List getCurrentActor() {
		return currentActor;
	}

}