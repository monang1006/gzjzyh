package com.strongit.oa.infopub.articlesappro;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-20 下午03:15:16
 * Autour: lanlc
 * Version: V1.0
 * Description：处理审核信息action
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "articlesAppro.action", type = ServletActionRedirectResult.class) })
public class ArticlesApproAction extends AbstractBaseWorkflowAction {
	
	
	private String columnArticleId;//上栏稿件ID
	private ToaInfopublishColumnArticl ca = null;//已上栏稿件BO
	private String articlesName;  //文章标题
	private ArticlesApproManager manager;
	private ArticlesManager articlesManager;//稿件Manager
	private String businessId;
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	/**
	 * 退回任务
	 * @author:胡丽丽
	 * @date:2010-03-22
	 * @param taskId -任务实例Id
	 * @param formId -表单Id
	 * @param formData -电子表单数据
	 * @param returnNodeId 需要退回到的目标节点
	 * @return ret
	 * 		1、操作成功返回0；
	 * 		2、任务实例不存在返回-1；
	 * 		3、出现异常返回-2；
	 * 		4、出现系统异常返回-3,一般是电子表单数据未获取到引起
	 * @throws Exception
	 */
	/*public String back() throws Exception {
		String ret = "0";
		try{
			if(StringUtils.hasLength(taskId)){
				String returnNodeId = getRequest().getParameter("returnNodeId");
				getManager().backSpace(taskId, returnNodeId, "0", suggestion,businessId);
			}else{
				ret = "-1";
			}
		}catch(SystemException e){
			logger.error(e.getMessage());
			ret = "-3";
		}catch(Exception ex){
			logger.error("退回任务时出现异常,异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}*/
	/**
	 * 查询业务数据ID
	 * 
	 * @author 胡丽丽
	 * @date 2010-03-22
	 * @return
	 * @throws Exception
	 */
	public String displayview() throws Exception {
		String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
		String cid = info[0];
		return renderHtml(cid);
	}
	public Object getModel() {
		return null;
	}
	
	@Autowired
	public void setManager(ArticlesApproManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setArticlesManager(ArticlesManager articlesManager) {
		this.articlesManager = articlesManager;
	}

	public String getArticlesName() {
		return articlesName;
	}

	public void setArticlesName(String articlesName) {
		this.articlesName = articlesName;
	}

	public ToaInfopublishColumnArticl getCa() {
		return ca;
	}

	public void setCa(ToaInfopublishColumnArticl ca) {
		this.ca = ca;
	}

	public String getColumnArticleId() {
		return columnArticleId;
	}

	public void setColumnArticleId(String columnArticleId) {
		this.columnArticleId = columnArticleId;
	}

	@Override
	protected BaseManager getManager() {
		// TODO 自动生成方法存根
		return this.manager;
	}

	@Override
	protected String getModuleType() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO 自动生成方法存根
		return String.valueOf(WorkFlowTypeConst.COLUMN);
	}

	@Override
	protected String getDictType() {
		// TODO 自动生成方法存根
		return null;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

}
