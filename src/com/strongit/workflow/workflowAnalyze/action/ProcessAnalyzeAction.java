package com.strongit.workflow.workflowAnalyze.action;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;

import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程分析处理Action
 * @author       wuws
 * @company      Strongit Ltd. (C) copyright
 * @date         
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.workflowDelegation.action.ProcessAnalyzeAction
 */
  @ParentPackage("default")
  @Results({ 
	 @Result(name = BaseActionSupport.RELOAD,value = "processAnalyze.action", type = ServletActionRedirectResult.class),
	 @Result(name = "pd",value = "/WEB-INF/jsp/workflowAnalyze/action/processAnalyze-pd.jsp", type = ServletDispatcherResult.class),
	 @Result(name = "node",value = "/WEB-INF/jsp/workflowAnalyze/action/processAnalyze-node.jsp", type = ServletDispatcherResult.class),
	 @Result(name = "ryjx",value = "/WEB-INF/jsp/workflowAnalyze/action/processAnalyze-ryjx.jsp", type = ServletDispatcherResult.class)
  })
   
  public class ProcessAnalyzeAction extends BaseActionSupport 
  {
	private static final long serialVersionUID = 1L;
	private Page page = new Page(FlexTableTag.MAX_ROWS,true);
	private IProcessInstanceService manager;
	private String pdName;
	private String pdId;
	private String startDate;
	private String endDate;
	private String taskOperator;
	  

    public ProcessAnalyzeAction(){
	    
   }

   public String pd() throws Exception {
	   page=manager.getStatisticProcessInstance(pdName,startDate,endDate,page);
	   return "pd";
   }
   
   public String node() throws Exception {
	   page=manager.getStatisticTaskNode(pdId,startDate,endDate,page);
	   return "node";
   }
   
   public String ryjx() throws Exception {
	   page=manager.getPersonalStatisticPerformance(taskOperator,startDate,endDate,page);
	   return "ryjx";
   }
   
   @Override
   public String list() throws Exception {
	   // TODO Auto-generated method stub
	   return null;
   }

   @Override
   public String save() throws Exception {
	   // TODO Auto-generated method stub
	   return null;
   }

   @Override
   public String delete() throws Exception {
	    // try {
		//		addActionMessage("删除成功");
		//} catch (ServiceException e) {
		//	logger.error(e.getMessage(), e);
		//	addActionMessage(e.getMessage());
	    //	}
	    //	return RELOAD;
	  return null;
   }

   @Override
   protected void prepareModel() throws Exception {
	
   }

   @Override
   public String input() 
   {
	   return INPUT;
   }
   
   public Object getModel() {
 	   // TODO Auto-generated method stub
	   return null;
   }

  public String getEndDate() {
	  return endDate;
  }

  public void setEndDate(String endDate) {
	  this.endDate = endDate;
  }

  public IProcessInstanceService getManager() {
	  return manager;
  }

  @Autowired
  public void setManager(IProcessInstanceService manager) {
	 this.manager = manager;
  }

  public String getPdId() {
	 return pdId;
  }

  public void setPdId(String pdId) {
	 this.pdId = pdId;
  }

  public String getPdName() {
	 return pdName;
  }

  public void setPdName(String pdName) {
	 this.pdName = pdName;
  }

  public String getStartDate() {
	 return startDate;
  }

  public void setStartDate(String startDate) {
	 this.startDate = startDate;
  }

  public String getTaskOperator() {
	 return taskOperator;
  }

  public void setTaskOperator(String taskOperator) {
	this.taskOperator = taskOperator;
  }

  public Page getPage() {
	return page;
  }

  public void setPage(Page page) {
	this.page = page;
  }
	  	
 }
