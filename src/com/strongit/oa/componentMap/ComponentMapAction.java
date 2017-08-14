/**
 * 
 */
package com.strongit.oa.componentMap;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaComponentMap;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @description 类描述
 * @className ComponentMapAction
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2012-2-10 下午01:56:37
 * @version 3.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "componentMap.action", type = ServletActionRedirectResult.class) })
public class ComponentMapAction extends BaseActionSupport {
	
	ToaComponentMap model = new ToaComponentMap();    //列表控件model对象
	
	@Autowired ComponentMapManager manager;           //列表控件映射服务类注入
	
	Page<ToaComponentMap> page = new Page<ToaComponentMap>(FlexTableTag.MAX_ROWS,true);  //列表控件映射分页列表
		
	private List privilList;       //资源模块
	
	private List efomList;          //展现的列表控件
	
	private List processList;   //流程
	
	private String mapId;       //列表映射主键
	



	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#delete()
	* @method delete
	* @author 申仪玲
	* @created 2012-2-10 下午01:57:08
	* @description 描述
	* @return
	*/
	@Override
	public String delete() throws Exception {
		try {
			manager.delete(mapId);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("false");
		}
		renderText("true");
		return null;
	}

	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#input()
	* @method input
	* @author 申仪玲
	* @created 2012-2-10 下午01:57:08
	* @description 描述
	* @return
	*/
	@Override
	public String input() throws Exception {
		this.prepareModel();
		privilList = manager.getPrivilList();
	    efomList = manager.getEformTemplateList();
	    processList = manager.getProcessList();
		return INPUT;
	}



	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#list()
	* @method list
	* @author 申仪玲
	* @created 2012-2-10 下午01:57:08
	* @description 描述
	* @return
	*/
	@Override
	public String list() throws Exception {
		page = manager.getComponentMapList(page, model);
		return SUCCESS;
	}



	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#prepareModel()
	* @method prepareModel
	* @author 申仪玲
	* @created 2012-2-10 下午01:57:08
	* @description 描述
	* @return
	*/
	@Override
	protected void prepareModel() throws Exception {
		if(mapId != null && !"".equals(mapId)){			
			model = manager.getMapById(mapId);
			
		}else{			
			model = new ToaComponentMap();
		}
		
	}


	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#save()
	* @method save
	* @author 申仪玲
	* @created 2012-2-10 下午01:57:08
	* @description 描述
	* @return
	*/
	@Override
	public String save() throws Exception {
		
		manager.save(model);
		
		return renderHtml("<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/componentMap/componentMap.action'; window.close();</script>");
	}


	public ToaComponentMap getModel() {

		return model;
	}
	
	public void setModel(ToaComponentMap model) {
		this.model = model;
	}

	
	public Page<ToaComponentMap> getPage() {
		return page;
	}

	public void setPage(Page<ToaComponentMap> page) {
		this.page = page;
	}
	
	public List getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List privilList) {
		this.privilList = privilList;
	}

	public List getEfomList() {
		return efomList;
	}

	public void setEfomList(List efomList) {
		this.efomList = efomList;
	}

	public List getProcessList() {
		return processList;
	}

	public void setProcessList(List processList) {
		this.processList = processList;
	}
	
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

}
