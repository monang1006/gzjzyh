/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-13
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息集管理ACTION
 */
package com.strongit.oa.infotable.infoset;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "infoSet.action", type = ServletActionRedirectResult.class) })
public class InfoSetAction extends BaseActionSupport<ToaSysmanageStructure> {
	private Page<ToaSysmanageStructure> page = new Page<ToaSysmanageStructure>(
			FlexTableTag.MAX_ROWS, true);

	private String infoSetCode;

	private String infoSetParentid;

	private String infoParentName;
	
	private String infoSetValue;		//信息集值 

	private List<ToaSysmanageStructure> infosetlist;

	private ToaSysmanageStructure model = new ToaSysmanageStructure();

	private InfoSetManager manager;
	
	private InfoItemManager itemManager;			//信息项Manager

	private HashMap<String, String> statemap = new HashMap<String, String>();

	private HashMap<String, String> sytypemap = new HashMap<String, String>();

	private static final String YES = "1";

	private static final String NO = "0";
	
	private String iscreate;  															//是否构建

	/**
	 * @roseuid 493692F5002E
	 */
	public InfoSetAction() {
		statemap.put(NO, "未构建");
		statemap.put(YES, "已构建");

		sytypemap.put(null, "普通信息集");
		sytypemap.put("", "普通信息集");
		sytypemap.put(NO, "普通信息集");
		sytypemap.put(YES, "系统信息集");
	}

	public HashMap getStatemap() {
		return statemap;
	}

	public HashMap getSytypemap() {
		return sytypemap;
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
	public void setInfoSetCode(java.lang.String aInfoSetCode) {
		infoSetCode = aInfoSetCode;
	}

	/**
	 * Access method for the infosetlist property.
	 * 
	 * @return the current value of the infosetlist property
	 */
	public java.util.List<ToaSysmanageStructure> getInfosetlist() {
		return infosetlist;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaSysmanageStructure getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(InfoSetManager aManager) {
		manager = aManager;
	}

	/**
	 * 获取信息集树结构（用于信息集模块）
	 * 
	 * @return java.lang.String
	 * @roseuid 49368F740196
	 */
	public String tree() throws Exception{
		infosetlist = manager.getAllSets();
		return "tree";
	}

	/**
	 * 获取信息集树结构（用于信息项模块）
	 * 
	 * @return java.lang.String
	 * @roseuid 49368F740196
	 */
	public String itemtree() throws Exception{
		infosetlist = manager.getAllSets();
		return "itemtree";
	}

	/**
	 * 获取信息集树结构（用于信息项类型模块）
	 * 
	 * @return java.lang.String
	 * @roseuid 49368F740196
	 */
	public String typetree() throws Exception{
		infosetlist = manager.getAllSets();
		return "typetree";
	}

	/**
	 * 获取信息集分页列表
	 * 
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 */
	@Override
	public String list() throws Exception{
		page = manager.getAllSets(infoSetParentid, page, model);
		return SUCCESS;
	}
	
	/**
	 * 同步已构建信息集的工作流初始化字段
	 * @author zhengzb
	 * @desc 
	 * 2010-11-22 上午11:14:11 
	 * @return
	 * @throws Exception
	 */
	public String dataInfoSet() throws Exception{
		try {		
			infosetlist=manager.getAllListByInfoset();
			if(infosetlist!=null&&infosetlist.size()>0){
				for(ToaSysmanageStructure structure:infosetlist){
					if(structure!=null&&structure.getInfoSetValue()!=null&&!structure.equals("")){
						manager.CreatedInfoSetByWorkflow(structure);
					}
				}
			}
			renderText("ok");
		} catch (Exception e) {
			e.printStackTrace();
			renderText("error");
		}
		return null;
	}
	

	/**
	 * 初始化信息集对象
	 * 
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() throws Exception{
		if (infoSetCode != null) {
			model = manager.getInfoSet(infoSetCode);
		} else {
			model = new ToaSysmanageStructure();
		}
	}

	/**
	 * 保存信息集
	 * 
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@Override
	public String save() throws Exception {
		if ("".equals(model.getInfoSetCode()))
			model.setInfoSetCode(null);

		String newInfosetValue = model.getInfoSetValue();
		String oldInfosetValue = newInfosetValue;
		List list = manager.getToaStructListByValue(newInfosetValue);
		String infosetcode = model.getInfoSetCode();
		if(infosetcode != null && !"".equals(infosetcode)){
			oldInfosetValue = manager.getInfoSet(infosetcode).getInfoSetValue();
			
		}
		if (infosetcode == null && list != null && list.size() > 0) {
			addActionMessage("该信息集已经存在！");
		}else if(infosetcode != null && list != null && list.size() > 1) {
			addActionMessage("该信息集已经存在！");
		}else if(infosetcode != null && list != null && list.size() > 0 && !newInfosetValue.equals(oldInfosetValue)){
			addActionMessage("该信息集已经存在！");
		}else{
			addActionMessage(manager.saveInfoSet(model));
		}
		return "reloads";
	}
	
	/**
	 * 判断信息集是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-11-15 下午01:53:24 
	 * @return
	 * @throws Exception
	 */
	public String isHasInfoSet()throws Exception{
		ToaSysmanageStructure structure=manager.getInfosetModel(infoSetValue);
		if(structure!=null){
			return "0";
		}else {
			return structure.getInfoSetCode();
		}
	}
	

	/**
	 * 删除信息集
	 * 
	 * @return java.lang.String
	 * @roseuid 4936911402EE
	 */
	@Override
	public String delete() throws Exception {
		addActionMessage(manager.delInfoSet(infoSetCode));
		return "reloads";

	}

	/**
	 * 初始化信息集输入框
	 * 
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	@Override
	public String input() throws Exception{
		prepareModel();
		long maxSort=0;
		if (YES.equals(model.getInfoSetIsSystem())) {		//是系统信息集
			addActionMessage(model.getInfoSetName() + "("
					+ model.getInfoSetValue() + ")" + "是系统信息集，不可修改！");
			return "reloads";
		} 
		//添加序号自动生成的功能
		if(model.getInfoSetCode()==null||"".equals(model.getInfoSetCode())){
			//获取最大排序值
			maxSort=manager.maxNo();
		}
//		else if (YES.equals(model.getInfoSetState())) {	//信息集已构建
////			addActionMessage(model.getInfoSetName() + "("+ model.getInfoSetValue() + ")" + "已构建，请先撤销信息集构建！");
//			
//			return "reloads";
//		}
		if(maxSort!=0){
			model.setInfoSetOrderno(maxSort);
		}
		infoParentName = manager.getNameByCode(infoSetParentid);
		return INPUT;
	}

	/**
	 * 构建信息集
	 * 
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	public String create() throws Exception{
		List<String> list=itemManager.getSystemField();
		addActionMessage(manager.CreatedInfoSet(infoSetCode));
		return "reloads";
	}

	/**
	 * 撤销构建信息集
	 * 
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	public String destroy() throws Exception{
		addActionMessage(manager.DestoryInfoSet(infoSetCode));
		return "reloads";
	}

	public String getInfoParentName() {
		return infoParentName;
	}

	public String getInfoSetParentid() {
		return infoSetParentid;
	}

	public void setInfoSetParentid(String infoSetParentid) {
		this.infoSetParentid = infoSetParentid;
	}

	@Autowired
	public void setItemManager(InfoItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public String getInfoSetValue() {
		return infoSetValue;
	}

	public void setInfoSetValue(String infoSetValue) {
		this.infoSetValue = infoSetValue;
	}

	public String getIscreate() {
		return iscreate;
	}

	public void setIscreate(String iscreate) {
		this.iscreate = iscreate;
	}
}
