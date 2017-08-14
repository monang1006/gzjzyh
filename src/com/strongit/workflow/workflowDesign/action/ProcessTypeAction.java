package com.strongit.workflow.workflowDesign.action;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfInfoProcessType;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程类型处理Action
 * @author 喻斌
 * @company Strongit Ltd. (C) copyright
 * @date Dec 11, 2008 9:39:21 AM
 * @version 1.0.0.0
 * @classpath com.strongit.workflow.workflowDesign.action.ProcessTypeAction
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "processType.action", type = ServletActionRedirectResult.class),
			@Result(name = "defaultReturn", value = "/WEB-INF/jsp/workflowDesign/defaultpage/defaultPage.jsp", type = ServletDispatcherResult.class)})
public class ProcessTypeAction extends BaseActionSupport<TwfInfoProcessType> {
	private Page<TwfInfoProcessType> page = new Page<TwfInfoProcessType>(FlexTableTag.MAX_ROWS,
			true);

	private String ptId;

	private TwfInfoProcessType model = new TwfInfoProcessType();

	private IProcessDefinitionService manager;

	public String getPtId() {
		return ptId;
	}

	public void setModel(TwfInfoProcessType model) {
		this.model = model;
	}

	public void setPage(Page<TwfInfoProcessType> page) {
		this.page = page;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public ProcessTypeAction() {

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
	 * Sets the value of the infoSetCode property.
	 * 
	 * @param aInfoSetCode
	 *            the new value of the infoSetCode property
	 */
	public void setPtId(java.lang.String ptId) {
		this.ptId = ptId;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public TwfInfoProcessType getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(IProcessDefinitionService aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 */
	@Override
	public String list() {
		page = manager.getProcessTypeList(page);
		return SUCCESS;
	}

	/**
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() {
		if (ptId != null && !"".equals(ptId)) {
			model = manager.getProcessTypeByTypeId(ptId);
		} else {
			model = new TwfInfoProcessType();
		}
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@Override
	public String save() throws Exception {
		if (new Long(0).equals(model.getPtId())) {
			model.setPtId(null);
		}
		manager.saveProcessType(model);
		addActionMessage("信息保存成功");
		return "defaultReturn";
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911402EE
	 */
	@Override
	public String delete() throws Exception {
		try {
			String ids = getRequest().getParameter("ids");
			manager.delProcessType(ids);
			addActionMessage("信息删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage());
		}
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
	 * 判断所选择的流程类型是否可以删除（系统级流程类型不允许删除）
	 * @author  喻斌
	 * @date    Jan 12, 2009  5:03:56 PM
	 * @return
	 */
	public String checkProcessTypeCanDel(){
		String ids = getRequest().getParameter("ids");
		String returnValue = manager.checkProcessTypeCanDel(ids);
		return this.renderText(returnValue);
	}
}
