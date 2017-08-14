/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-13
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典类管理ACTION
 */
package com.strongit.oa.dict.dictType;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.dict.dictItem.DictItemManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "dictType.action", type = ServletActionRedirectResult.class) })
public class DictTypeAction extends BaseActionSupport {
	/** 分页对象*/
	private Page<ToaSysmanageDict> page = new Page<ToaSysmanageDict>(FlexTableTag.MAX_ROWS, true);

	/** 字典类编号*/
	private String dictCode;
	/** 字典类编号*/
    private String dictCode1;
    /** 字典类值*/
    private String dictValue1;

	/** 字典类列表*/
	private List dictTypeList;

	/** 字典类类型*/
	private String type;

	/** 字典类名称*/
	private String name;

	/** 字典类系统状态情况*/
	private HashMap<String, String> sysmap = new HashMap<String, String>();

	/** 字典类类型情况*/
	private HashMap<String, String> typemap = new HashMap<String, String>();

	/** 字典类对象*/
	private ToaSysmanageDict model = new ToaSysmanageDict();

	/** 字典类Manager*/
	private DictTypeManager manager;
	
	private DictItemManager dictItemManager;
	
	/** 是*/
	private static final String YES = "1";

	/** 否*/
	private static final String NO = "0";

	/**
	 * @roseuid 493D192A0393
	 */
	public DictTypeAction() {
		sysmap.put("1", "是");
		sysmap.put("0", "否");
		sysmap.put("", "否");
		sysmap.put(null, "否");

		typemap.put("A", "国标码");
		typemap.put("B", "地方码");
		typemap.put("C", "自定义码");
		typemap.put("D", "其他");
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
	 * Sets the value of the dictCode property.
	 * 
	 * @param aDictCode
	 *            the new value of the dictCode property
	 */
	public void setDictCode(java.lang.String aDictCode) {
		dictCode = aDictCode;
	}

	/**
	 * Access method for the dictTypeList property.
	 * 
	 * @return the current value of the dictTypeList property
	 */
	public java.util.List getDictTypeList() {
		return dictTypeList;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaSysmanageDict getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(DictTypeManager aManager) {
		manager = aManager;
	}

	/**
	 * 获取字典类分页列表
	 * 
	 * @return java.lang.String
	 * @roseuid 493D1A1D0199
	 */
	@Override
	public String list() throws Exception{
		//getRequest().setAttribute("backlocation", getRequest().getContextPath()+"/dict/dictType/dictType.action");
		getRequest().setAttribute("backlocation","javascript:history.back();");
		page = manager.getAllDictTypes(page, model);
		return SUCCESS;
	}

	/**
	 * 保存字典类
	 * 
	 * @return java.lang.String
	 * @roseuid 493D1A1D01B8
	 */
	@Override
	public String save() throws Exception{
		getRequest().setAttribute("backlocation", getRequest().getContextPath()
				+"/dict/dictType/dictType!input.action?dictCode="+dictCode);
		//if ("".equals(model.getDictCode()))
			//model.setDictCode(null);
		//addActionMessage(manager.saveDictType(model));
		manager.saveDictType1(model);
		addActionMessage("岗位信息保存成功。");
		return "temp";
	}
	/**
     * 字典类新增或修改验证
     * 
     * @return java.lang.String
     * @roseuid 493D1A1D01D7
     */
	public String yanzheng()throws Exception{
	    String msg="保存成功";
	    msg=manager.tryDictType(dictCode1, dictValue1);
	    return renderText(msg);
	}
	/**
	 * 删除字典类
	 * 
	 * @return java.lang.String
	 * @roseuid 493D1A1D01D7
	 */
	@Override
	public String delete() throws Exception{
		getRequest().setAttribute("backlocation", getRequest().getContextPath()
				+"/dict/dictType/dictType.action");
		addActionMessage(manager.deleteDictType(dictCode));
		return "reloads";
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-6-8下午05:37:25
	  * @desc: 
	  * @param
	  * @return
	 */
	public String isExistDictItem()throws Exception{
		List list=dictItemManager.getAllDictItems(dictCode);
		if(list!=null&&list.size()>0){
			renderText("该字典类下存在字典项，是否继续删除？");
		}else{
			renderText("");
		}
		return null;
	}

	/**
	 * 初始化字典类对象
	 * 
	 * @roseuid 493D1A1D01F6
	 */
	@Override
	protected void prepareModel() throws Exception{
		if (dictCode1 != null) {
			model = manager.getDictType(dictCode1);
		} else if(dictCode!= null){
		    model = manager.getDictType(dictCode);
		}else if(dictCode1 == null&&dictCode== null){
			model = new ToaSysmanageDict();
		}
	}

	/**
	 * 初始化字典类输入框
	 * 
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	@Override
	public String input() throws Exception{
		getRequest().setAttribute("backlocation", getRequest().getContextPath()
				+"/dict/dictType/dictType.action");	
		prepareModel();
		if (YES.equals(model.getDictIsSystem())) {
			addActionMessage(model.getDictName() + "是系统字典类，不可修改！");
		}
		return INPUT;
	}

	/**
	 * 构造字典类树
	 * 
	 * @return java.lang.String
	 * @roseuid 49368F740196
	 */
	public String tree() throws Exception{
		getRequest().setAttribute("backlocation", getRequest().getContextPath()
				+"/dict/dictType/dictType!tree.action");
		dictTypeList = manager.getDictTypesByType(type, name);
		return "tree";
	}

	public HashMap getSysmap() {
		return sysmap;
	}

	public HashMap getTypemap() {
		return typemap;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Autowired
	public void setDictItemManager(DictItemManager dictItemManager) {
		this.dictItemManager = dictItemManager;
	}

    public String getDictCode1() {
        return dictCode1;
    }

    public void setDictCode1(String dictCode1) {
        this.dictCode1 = dictCode1;
    }

    public String getDictValue1() {
        return dictValue1;
    }

    public void setDictValue1(String dictValue1) {
        this.dictValue1 = dictValue1;
    }

}
