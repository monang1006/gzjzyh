/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-18
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息项分类管理ACTION
 */
package com.strongit.oa.infotable.infotype;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanagePropertype;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "infoType.action", type = ServletActionRedirectResult.class) })
public class InfoTypeAction extends BaseActionSupport {
	private Page<ToaSysmanagePropertype> page = new Page<ToaSysmanagePropertype>(
			15, true);

	private String propertypeId;

	private List typelist;

	private String infoSetCode;

	private String infoSetName;

	private ToaSysmanagePropertype model = new ToaSysmanagePropertype();

	private InfoTypeManager manager;

	/**
	 * @roseuid 49479C9400DA
	 */
	public InfoTypeAction() {

	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page<ToaSysmanagePropertype> getPage() {
		return page;
	}

	/**
	 * Sets the value of the propertypeId property.
	 * 
	 * @param aPropertypeId
	 *            the new value of the propertypeId property
	 */
	public void setPropertypeId(java.lang.String aPropertypeId) {
		propertypeId = aPropertypeId;
	}

	/**
	 * Access method for the typelist property.
	 * 
	 * @return the current value of the typelist property
	 */
	public java.util.List getTypelist() {
		return typelist;
	}

	/**
	 * Access method for the infoSetCode property.
	 * 
	 * @return the current value of the infoSetCode property
	 */
	public java.lang.String getInfoSetCode() {
		return infoSetCode;
	}

	/**
	 * Sets the value of the infoSetCode property.
	 * 
	 * @param aInfoSetCode
	 *            the new value of the infoSetCode property
	 */
	public void setInfoSetCode(java.lang.String aInfoSetCode) {
		infoSetCode = aInfoSetCode;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaSysmanagePropertype getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(InfoTypeManager aManager) {
		manager = aManager;
	}

	/**
	 * 初始化信息项分类输入框
	 * 
	 * @return java.lang.String
	 * @roseuid 49471E3F015C
	 */
	@Override
	public String input() throws Exception{
		prepareModel();
		infoSetName = manager.getInfoSetName(infoSetCode);
		return INPUT;
	}

	/**
	 * 校验信息项分类名称是否已经使用.
	 * @author:
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String checkInfoTypeName() throws Exception {
		String ret = "";
		try{
			boolean isUsed = manager.isInfoTypeUsed(model.getToaSysmanageStructure().getInfoSetCode(), model.getPropertypeName());
			if(isUsed){
				ret = "1";//存在
			}else{
				ret = "0";//不存在
			}
		}catch(Exception e){
			ret = e.getMessage();
		}
		renderText(ret);
		return null;
	}
	/**
	 * 获取信息项分类分页列表
	 * 
	 * @return java.lang.String
	 * @roseuid 4947B2960138
	 */
	@Override
	public String list() throws Exception{
		page = manager.getAllTypes(infoSetCode, page, model);
		return SUCCESS;
	}

	/**
	 * 保存信息项分类对象
	 * 
	 * @return java.lang.String
	 * @roseuid 4947B2960167
	 */
	@Override
	public String save() throws Exception{
		if ("".equals(model.getPropertypeId()))
			model.setPropertypeId(null);

		manager.saveInfoType(model);
		addActionMessage("保存信息项分类成功");
		return "temp";
	}

	/**
	 * 删除信息项分类
	 * 
	 * @return java.lang.String
	 * @roseuid 4947B2960186
	 */
	@Override
	public String delete() throws Exception{
		manager.delInfoType(propertypeId);
		addActionMessage("删除信息项分类成功");
		return "temp";
	}

	/**
	 * 初始化信息项分类对象
	 * 
	 * @roseuid 4947B29601A5
	 */
	@Override
	protected void prepareModel() throws Exception{
		if (propertypeId != null) {
			model = manager.getInfoType(propertypeId);
		} else {
			model = new ToaSysmanagePropertype();
		}
	}

	public String getInfoSetName() {
		return infoSetName;
	}
}
