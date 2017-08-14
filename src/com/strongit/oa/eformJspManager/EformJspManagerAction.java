/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息附件 manager类
 */
package com.strongit.oa.eformJspManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TEJsptemplate;
import com.strongit.oa.common.user.IUserService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理Jsp列表
 * @Create Date: 2013年12月31日16:55:34
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
public class EformJspManagerAction extends BaseActionSupport {

	private Page<TEJsptemplate> page = new Page<TEJsptemplate>(FlexTableTag.MAX_ROWS, true);
	@Autowired IUserService userService;

	public Page<TEJsptemplate> getPage() {
		return page;
	}

	public void setPage(Page<TEJsptemplate> page) {
		this.page = page;
	}




	private String id;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}




	private TEJsptemplate model = new TEJsptemplate();

	private EformJspManagerManager manager;
	private String  name;//jsp表单名称
	

	@Autowired
	public void setManager(EformJspManagerManager manager) {
		this.manager = manager;
	}

	@Override
	public String delete() throws Exception {
		String message = "";
		try {
			prepareModel();
			List list=manager.findRef(model.getId());
			if(list.isEmpty() || list.size()==0){
				manager.delete(model);
				message="ok";
			}else{
				message="exit";
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("deletefalse");
		}
		return renderText(message);
	}




	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		this.prepareModel();
		return INPUT;
	}




	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		page = manager.getAllJSPForm(page, model);
		return SUCCESS;
	}




	@Override
	public String save() throws Exception {
		SimpleDateFormat  sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(model.getId() == null || "".equals(model.getId())){
			model.setCreatetime(new Date());// new Date()为获取当前系统时间
			model.setModifyTime(new Date());// new Date()为获取当前系统时间
		}else{
			model.setModifyTime(new Date());// new Date()为获取当前系统时间
		}
		if("".equals(model.getId())){
			model.setId(null);
		}
		String username = userService.getCurrentUser().getUserName();
		model.setType("0");
		model.setCreator(username);
		manager.save(model);
		return renderHtml("<script>window.close();</script>");
	}
	/**
	 * 检查表单名称是否重复
	 * @达特 2014年1月2日10:34:56
	 * @return
	 * @throws Exception
	 */
	public String checkTitle() throws Exception {
		
		if(null!=id&&!"".equals(id)){
			model = manager.get(id);
			if(name.equals(model.getName())){
				return renderHtml("true"); //说明是编辑表单,表单名没有变
			}else{				
				model = manager.getByName(name);
				if(model == null){
					return renderHtml("true");
				}else{
					return renderHtml("false");
				}				
			}
		}else{
			model = manager.getByName(name);
			if(model == null){
				return renderHtml("true");
			}else{
				return renderHtml("false");
			}		
			
		}
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}




	public TEJsptemplate getModel() {
		return model;
	}




	public void setModel(TEJsptemplate model) {
		this.model = model;
	}




	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id !=null && !"".equals(id)){
			model = manager.get(id);
		}else {
			model = new TEJsptemplate();
		}
		
	}




}
