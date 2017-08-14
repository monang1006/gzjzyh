/**
 * 工作流配置Action
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Jun 16, 2010 2:23:36 AM
 * @version 1.0
 * @classpath com.strongit.workflow.config.action.WorkflowConfigAction
 */
package com.strongit.workflow.config.action;

import java.util.List;
import java.util.UUID;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfBaseActorSetting;
import com.strongit.workflow.workflowInterface.IWorkflowConfigService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( {
		@Result(name = "windowClose", value = "/WEB-INF/jsp/config/action/default.jsp", type = ServletDispatcherResult.class) })
public class WorkflowConfigAction extends BaseActionSupport<TwfBaseActorSetting> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TwfBaseActorSetting model = new TwfBaseActorSetting();
	
	private Page<TwfBaseActorSetting> page = new Page<TwfBaseActorSetting>(FlexTableTag.MAX_ROWS, true);
	
	private IWorkflowConfigService manager;
	
	private String asName;
	
	private String asAlias;
	
	private String asPrefix;
	
	private String searchAsName;//配置名称查询条件
	
	private String searchAsAlias;//配置别名查询条件
	
	private String searchAsPrefix;//配置前缀查询条件
	
	private String searchAsIsActive;//配置是否启用查询条件
	
	private String asId;//配置主键值
	
	private String ids;//配置主键集，格式为id1,id2,...,idN
	
	private String assignerSet;
	
	private String reAssignerSet;
	
	private String managerSet;
	
	private List<TwfBaseActorSetting> asLst;//人员选择配置信息集合
	
	@Autowired
	public void setManager(IWorkflowConfigService aManager) {
		manager = aManager;
	}
	
	/**
	 * 判断是否有相同的配置别名存在
	 * @author 喻斌
	 * @date Jun 16, 2010 4:14:14 AM
	 * @return
	 * @throws Exception
	 */
	public String checkSameAlias() throws Exception{
		boolean sameAlias = manager.checkSameAlias(this.asAlias);
		this.renderText(String.valueOf(sameAlias));
		return null;
	}
	
	/**
	 * 判断是否有相同的配置前缀存在
	 * @author 喻斌
	 * @date Jun 16, 2010 11:56:47 AM
	 * @return
	 * @throws Exception
	 */
	public String checkSamePrefix() throws Exception{
		boolean samePrefix = manager.checkSamePrefix(this.asPrefix);
		this.renderText(String.valueOf(samePrefix));
		return null;
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#delete()
	 */
	@Override
	public String delete() throws Exception {
		if(this.ids != null && !"".equals(this.ids)){
			manager.delActorSettingByIds(this.ids);
		}
		return list();
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#input()
	 */
	@Override
	public String input() throws Exception {
		return INPUT;
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#list()
	 */
	@Override
	public String list() throws Exception {
		this.page = manager.queryActorSettings(page, searchAsName, searchAsAlias, searchAsPrefix, searchAsIsActive);
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#prepareModel()
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(this.asId != null && !"".equals(this.asId)){
			this.model = manager.getActorSettingById(this.asId);
		}else{
			this.model = new TwfBaseActorSetting();
		}
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#save()
	 */
	@Override
	public String save() throws Exception {
		
		if(this.asId != null && !"".equals(this.asId)){
			this.model.setAsId(Long.valueOf(this.asId));
		}else{
			String s = UUID.randomUUID().toString();
	        s = s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);//去掉“-”符号
			this.model.setAsFlag(s);
		}
		manager.saveActorSetting(this.model);
		return "windowClose";
	}
	
	/**
	 * 判断  配置解析类 是否存在
	 * 
	 * */
	public String isExistHandlerClass(){
		String sHandlerClass=model.getAsHandlerClass();
		if(sHandlerClass!=null && !"".equals(sHandlerClass)){
		  sHandlerClass=sHandlerClass.substring(0, 1).toUpperCase()+sHandlerClass.subSequence(1, sHandlerClass.length());
			try{
				Class.forName("com.strongit.workflow.actorSettingInterface."+sHandlerClass);	
				return renderText("true");
			}catch(Exception e){
				return renderText("false");
			}
		}
		return renderText("true");
		
	}
	
	/**
	 * 初始化工作流系统配置
	 * @author 喻斌
	 * @date Jun 16, 2010 7:15:53 AM
	 * @return
	 * @throws Exception
	 */
	public String initWorkflowConfig() throws Exception{
		List<TwfBaseActorSetting> lst = manager.getActorSettingsByConfigClass("ag", "1");
		this.assignerSet = buildActorSettringJson(lst);
		
		lst = manager.getActorSettingsByConfigClass("ra", "1");
		this.reAssignerSet = buildActorSettringJson(lst);
		
		lst = manager.getActorSettingsByConfigClass("mg", "1");
		this.managerSet = buildActorSettringJson(lst);
		
		return "config";
	}
	
	/**
	 * 构造人员选择配置Json格式返回内容
	 * @author 喻斌
	 * @date Jun 16, 2010 8:10:45 AM
	 * @param lst -人员选择配置信息集
	 * @return Json格式返回内容
	 */
	private String buildActorSettringJson(List<TwfBaseActorSetting> lst){
		StringBuffer json = new StringBuffer("");
		if(!lst.isEmpty()){
			for(TwfBaseActorSetting as : lst){
				json.append(",").append("{")
					.append("name:").append("'").append(as.getAsName()).append("'").append(",")
					.append("alias:").append("'").append(as.getAsFlag()).append("'")
					.append("}");
			}
		}
		return "[" + ("".equals(json.toString()) ? "" : json.toString().substring(1)) + "]";
	}
	
	/**
	 * 得到人员选择配置树
	 * @author 喻斌
	 * @date Jun 16, 2010 8:19:50 AM
	 * @return
	 * @throws Exception
	 */
	public String getActorSettingTree() throws Exception{
		this.asLst = manager.getActorSettings(null, null, null, "1");
		return "asTree";
	}
	
	/**
	 * 保存工作流系统设置信息
	 * @author 喻斌
	 * @date Jun 16, 2010 9:06:42 AM
	 * @return
	 * @throws Exception
	 */
	public String saveConfig() throws Exception{
		manager.saveActorSettingConfig(this.assignerSet, this.reAssignerSet, null);
		return initWorkflowConfig();
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	public TwfBaseActorSetting getModel() {
		return model;
	}

	/**
	 * @return the page
	 */
	public Page<TwfBaseActorSetting> getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Page<TwfBaseActorSetting> page) {
		this.page = page;
	}

	/**
	 * @return the asAlias
	 */
	public String getAsAlias() {
		return asAlias;
	}

	/**
	 * @param asAlias the asAlias to set
	 */
	public void setAsAlias(String asAlias) {
		this.asAlias = asAlias;
	}

	/**
	 * @return the asName
	 */
	public String getAsName() {
		return asName;
	}

	/**
	 * @param asName the asName to set
	 */
	public void setAsName(String asName) {
		this.asName = asName;
	}

	/**
	 * @return the asId
	 */
	public String getAsId() {
		return asId;
	}

	/**
	 * @param asId the asId to set
	 */
	public void setAsId(String asId) {
		this.asId = asId;
	}

	/**
	 * @return the searchAsAlias
	 */
	public String getSearchAsAlias() {
		return searchAsAlias;
	}

	/**
	 * @param searchAsAlias the searchAsAlias to set
	 */
	public void setSearchAsAlias(String searchAsAlias) {
		this.searchAsAlias = searchAsAlias;
	}

	/**
	 * @return the searchAsIsActive
	 */
	public String getSearchAsIsActive() {
		return searchAsIsActive;
	}

	/**
	 * @param searchAsIsActive the searchAsIsActive to set
	 */
	public void setSearchAsIsActive(String searchAsIsActive) {
		this.searchAsIsActive = searchAsIsActive;
	}

	/**
	 * @return the searchAsName
	 */
	public String getSearchAsName() {
		return searchAsName;
	}

	/**
	 * @param searchAsName the searchAsName to set
	 */
	public void setSearchAsName(String searchAsName) {
		this.searchAsName = searchAsName;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the assignerSet
	 */
	public String getAssignerSet() {
		return assignerSet;
	}

	/**
	 * @param assignerSet the assignerSet to set
	 */
	public void setAssignerSet(String assignerSet) {
		this.assignerSet = assignerSet;
	}

	/**
	 * @return the managerSet
	 */
	public String getManagerSet() {
		return managerSet;
	}

	/**
	 * @param managerSet the managerSet to set
	 */
	public void setManagerSet(String managerSet) {
		this.managerSet = managerSet;
	}

	/**
	 * @return the reAssignerSet
	 */
	public String getReAssignerSet() {
		return reAssignerSet;
	}

	/**
	 * @param reAssignerSet the reAssignerSet to set
	 */
	public void setReAssignerSet(String reAssignerSet) {
		this.reAssignerSet = reAssignerSet;
	}

	/**
	 * @return the asLst
	 */
	public List<TwfBaseActorSetting> getAsLst() {
		return asLst;
	}

	/**
	 * @param asLst the asLst to set
	 */
	public void setAsLst(List<TwfBaseActorSetting> asLst) {
		this.asLst = asLst;
	}

	/**
	 * @return the searchAsPrefix
	 */
	public String getSearchAsPrefix() {
		return searchAsPrefix;
	}

	/**
	 * @param searchAsPrefix the searchAsPrefix to set
	 */
	public void setSearchAsPrefix(String searchAsPrefix) {
		this.searchAsPrefix = searchAsPrefix;
	}

	/**
	 * @return the asPrefix
	 */
	public String getAsPrefix() {
		return asPrefix;
	}

	/**
	 * @param asPrefix the asPrefix to set
	 */
	public void setAsPrefix(String asPrefix) {
		this.asPrefix = asPrefix;
	}

}
