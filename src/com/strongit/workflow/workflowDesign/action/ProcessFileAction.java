package com.strongit.workflow.workflowDesign.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程模型处理Action
 * @author 喻斌
 * @company Strongit Ltd. (C) copyright
 * @date Dec 11, 2008 9:39:21 AM
 * @version 1.0.0.0
 * @classpath com.strongit.workflow.workflowDesign.action.ProcessFileAction
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "processFile.action", type = ServletActionRedirectResult.class),
			@Result(name = "defaultReturn", value = "/WEB-INF/jsp/workflowDesign/defaultpage/defaultPage.jsp", type = ServletDispatcherResult.class)})
public class ProcessFileAction extends BaseActionSupport<TwfBaseProcessfile> {
	private Page page = new Page(FlexTableTag.MAX_ROWS,false);

	private String pfId;

	private TwfBaseProcessfile model = new TwfBaseProcessfile();

	private IProcessDefinitionService manager;
	
	private String errorMessage;
	
	@Autowired private JdbcTemplate jdbcTemplate;
	
	@Autowired private IWorkflowService workflowService;
	
	protected List<Object[]> workflowTypeList ;						//流程类型列表

	protected Date startDate	;//开始时间
	
	protected Date endDate	;//结束时间
	
	private String workflowType;
	
	private String issearch; //是否查询
	
	public String getPfId() {
		return pfId;
	}

	public void setModel(TwfBaseProcessfile model) {
		this.model = model;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public ProcessFileAction() {

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
	public void setPfId(java.lang.String pfId) {
		this.pfId = pfId;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public TwfBaseProcessfile getModel() {
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
	public String list() throws Exception{
		String currentPageNo = this.getRequest().getParameter("pageNo");
		String pfName = this.getRequest().getParameter("pfName");
		if(pfName != null && !"".equals(pfName)){			
			model.setPfName(pfName);
		}
		if(currentPageNo != null){
			page.setPageNo(Integer.valueOf(currentPageNo));
		}
		//查询时重置page
		if("1".equals(issearch)){
			page.setPageNo(1);
			page.setPageNoBak(1);
		}
		try {
			workflowTypeList = workflowService.getAllProcessTypeList();
			String fileName = model.getPfName();
			if(fileName != null && !"".equals(fileName)) {
				try {
					fileName = URLDecoder.decode(fileName, "utf-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("流程文件名称查询时转码异常", e);
				}
				
			}
			model.setPfName(fileName);
			String creatorName = model.getPfCreator();
			if(creatorName != null && !"".equals(creatorName)) {
				try {
					creatorName = URLDecoder.decode(creatorName, "utf-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("创建人查询时转码异常", e);
				}
				
			}
			model.setPfCreator(creatorName);
			page = workflowService.getToDesignProcessFilePage(page, model.getPfName(), startDate, endDate, model.getPfCreator(), workflowType, model.getPfIsDeploy());
		} catch (Exception e) {
			logger.error("查询流程列表出错", e);
		}
		return SUCCESS;
	}

	/**
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() {
		if (pfId != null && !"".equals(pfId)) {
			model = manager.getProcessfileByPfId(pfId);
		} else {
			model = new TwfBaseProcessfile();
		}
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@Override
	public String save() throws Exception {
		if (new Long(0).equals(model.getPfId())) {
			model.setPfId(null);
		}
		manager.addOrUpdateProcessFile(model);
		if(model != null && model.getPfId() != null) {//modify 严建  添加流程别名REST2
			String sql = "update T_WF_BASE_PROCESSFILE  " +
						"set REST1 = '" + model.getRest1() + "'," +
						"REST2 = '"+model.getRest2()+"'  where PF_ID = " + model.getPfId();
			jdbcTemplate.update(sql);
		}
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
			manager.delProcessFile(ids);
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
	 * 判断添加或修改的流程文件名是否与存在的冲突(JQuery使用)
	 * @author  喻斌
	 * @date    Dec 15, 2008  9:38:24 AM
	 * @return
	 * @throws Exception
	 */
	public String checkProcessfileName() throws Exception{
		boolean returnvalue = false;
		String id = this.getRequest().getParameter("id");
		String name = this.getRequest().getParameter("name");
		if("".equals(id)){
			returnvalue = manager.checkProcessFileName(name, Long.valueOf(0));
		}else{
			returnvalue = manager.checkProcessFileName(name, Long.valueOf(id));
		}
		if(returnvalue){
			return this.renderText("该名称的流程定义已存在，请更改流程名称！");
		}else{
			return this.renderText("");
		}
	}
	
	/**
	 * 批量部署流程
	 * @author  喻斌
	 * @date    Dec 15, 2008  11:41:40 AM
	 * @return
	 * @throws Exception
	 */
	public String deploy() throws Exception{
		String ids = this.getRequest().getParameter("ids");
		//部署后依然显示为当前页
		String currentPageNo = this.getRequest().getParameter("pageNo");
		try {
			errorMessage = manager.deployProcessFile(ids);
		} catch (Exception e) {
			logger.error("部署出错,ids=" + ids , e);
		}
		page.setPageNo(Integer.valueOf(currentPageNo));
		//addActionMessage(errorInfo);
		return list();
	}
	//tj  验证是否挂载表单
	public String ischeck(){
		String ids = this.getRequest().getParameter("ids");
		String name = "";
		//zeng jia 
		String[] idss = ids.split(",");
		List<TwfBaseProcessfile> t = workflowService.getTwfBaseProcessfileByIds(page,idss);
		if(t!=null&&t.size()>0){
			for(TwfBaseProcessfile temp:t){
				name = name + "," + temp.getPfName();
			}
			return renderText(name.substring(1));
		}else{
			return renderText("");
		}
	}
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Object[]> getWorkflowTypeList() {
		return workflowTypeList;
	}

	public void setWorkflowTypeList(List<Object[]> workflowTypeList) {
		this.workflowTypeList = workflowTypeList;
	}

	public Date getEndDate() {
		return endDate;
	}

	@SuppressWarnings("deprecation")
	public void setEndDate(Date endDate) {
		if(endDate!=null){
			endDate.setHours(23);
			endDate.setMinutes(59);
		}
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getIssearch() {
		return issearch;
	}

	public void setIssearch(String issearch) {
		this.issearch = issearch;
	}

}
