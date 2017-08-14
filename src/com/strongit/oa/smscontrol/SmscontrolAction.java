/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.smscontrol;

import java.util.HashMap;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSmscontrol;
import com.strongit.oa.common.user.IUserService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;



@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "smscontrol.action", type = ServletActionRedirectResult.class) })
public class SmscontrolAction extends BaseActionSupport<ToaSmscontrol> {
	
	private Page<ToaSmscontrol> page = new Page<ToaSmscontrol>(FlexTableTag.MAX_ROWS, true);
	
	private SmscontrolManager manager;
	
	private String controlId;
	
	private IUserService user;
	
	private ToaSmscontrol model = new ToaSmscontrol();
	
	private String userIds;
	
	private HashMap<String, String> statemap = new HashMap<String, String>();

	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	@Autowired
	public void setManager(SmscontrolManager manager) {
		this.manager = manager;
	}

	public Page<ToaSmscontrol> getPage() {
		return page;
	}
	
	public void setModel(ToaSmscontrol model) {
		this.model = model;
	}
	
	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}
	
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	
	public HashMap getStatemap() {
		return statemap;
	}
	
	public ToaSmscontrol getModel() {
		return model;
	}
	
	public SmscontrolAction() {
		//		发送状态
		statemap.put("0", "禁用");
		statemap.put("1", "开启");
	}
	
	/**   以下为跳转方法 */
	
	public String delete() throws Exception {
		return null;
	}

	public String input() throws Exception {
		return "input";
	}

	public String list() throws Exception {
		page = manager.getControlList(page);
		return SUCCESS;
	}

	protected void prepareModel() throws Exception {
		
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#save()
	 */
	public String save() throws Exception {
		manager.saveRight(userIds);
		return null;
	}
	
	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		page = manager.searchList(page, model);
		return "list";
	}
	
	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String openRight()throws Exception {
		manager.openRight(controlId);
		renderText("success");
		return null;
	}

	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String closeRight()throws Exception {
		manager.closeRight(controlId);
		renderText("success");
		return null;
	}
	



}
