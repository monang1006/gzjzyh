/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-22
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板类别action类
 */
package com.strongit.oa.doctemplate.doctempType;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaDoctemplate;
import com.strongit.oa.bo.ToaDoctemplateGroup;
import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "docTempType.action", type = ServletActionRedirectResult.class) })
public class DocTempTypeAction extends BaseActionSupport {
	
	private String docgroupId;
	private List docTempTypeList;
	private String[] hasChild;
	private String doctempTypeName;
	private ToaDoctemplateGroup model = new ToaDoctemplateGroup();
	private DocTempTypeManager manager;
	private DocTempItemManager itemmanager;
	private String docgroupParentId;		//父节点
	private String groupType;     		//模板类型
	
	/**
	 * author:lanlc
	 * description:获取所有公文模板类别及子类虽,树型展现
	 * modifyer:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String tree()throws Exception{
		docTempTypeList=manager.docTempTypeList();
		if(docTempTypeList.isEmpty()){
			model = new ToaDoctemplateGroup();
			model.setDocgroupName("默认模板");
			model.setDocgroupParentId("0");
			manager.saveTemplateType(model);
			docTempTypeList=manager.docTempTypeList();
		}
		ToaDoctemplateGroup group = null;
		List<ToaDoctemplateGroup> subGroupList = null;
		hasChild = new String[docTempTypeList.size()];
		for(int i=0;i<docTempTypeList.size();i++){
			group = (ToaDoctemplateGroup) docTempTypeList.get(i);
			subGroupList = manager.docTempSubTypeList(group.getDocgroupId());
			if(subGroupList.size()>0){
				hasChild[i] = "1";
			}else{
				hasChild[i] = "0";
			}
		}
		return "tree";
	}
	
	/**
	 * author:lanlc
	 * description:office控件中用到的模板类别
	 * modifyer:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String officetree()throws Exception{
		docTempTypeList=manager.docTempWordTypeList();
		ToaDoctemplateGroup group = null;
		List<ToaDoctemplateGroup> subGroupList = null;
		hasChild = new String[docTempTypeList.size()];
		for(int i=0;i<docTempTypeList.size();i++){
			group = (ToaDoctemplateGroup) docTempTypeList.get(i);
			subGroupList = manager.docTempSubTypeList(group.getDocgroupId());
			List itemlist = itemmanager.getTypeByItem(group.getDocgroupId());
			if(subGroupList.size()>0 || itemlist.size()>0){
				hasChild[i] = "1";
			}else{
				hasChild[i] = "0";
			}
		}
		return "officetree";
	}
	
	/**
	 * author:lanlc
	 * description:异步加载树
	 * modifyer:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String syntree()throws Exception{
		StringBuffer str=new StringBuffer();
		List<ToaDoctemplateGroup> folds = manager.docTempSubTypeList(docgroupId);
		for(int i=0; i< folds.size();i++){
			ToaDoctemplateGroup fold= folds.get(i);
			List<ToaDoctemplateGroup> subFoldsList = manager.docTempSubTypeList(fold.getDocgroupId());
			if(!subFoldsList.isEmpty()){
				str.append("<li id="+fold.getDocgroupId() +">");
				str.append("<span>"+fold.getDocgroupName()+"</span>");	
				str.append("<ul class=ajax>");
				str.append("<li id="+fold.getDocgroupId()+i +">{url:"+getRequest().getContextPath()+"/doctemplate/doctempType/docTempType!syntree.action?docgroupId="+fold.getDocgroupId() +"}</li>");	
				str.append("</ul>");	
				str.append("</li>");
			}else{
				str.append("<li id="+fold.getDocgroupId() +">");
				str.append("<span>"+fold.getDocgroupName());
				str.append("</span>");
				str.append("</li>");
			}
		}
		renderHtml(str.toString());
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:office控件选择类别下的子类别及模板异步加载树
	 * modifyer:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String synofficetree()throws Exception{
		StringBuffer str=new StringBuffer();
		List<ToaDoctemplateGroup> folds = manager.docTempSubTypeList(docgroupId);
		List itemlist = itemmanager.getTypeByItem(docgroupId);
		if(folds.size()>0){
			for(int i=0; i< folds.size();i++){
				ToaDoctemplateGroup fold= folds.get(i);
				List<ToaDoctemplateGroup> subFoldsList = manager.docTempSubTypeList(fold.getDocgroupId());
				List itemsublist = itemmanager.getTypeByItem(fold.getDocgroupId());
				if(subFoldsList.size()>0 || itemsublist.size()>0){
					str.append("<li id="+fold.getDocgroupId() +">");
					str.append("<span>"+fold.getDocgroupName()+"</span>");	
					str.append("<ul class=ajax>");
					str.append("<li id="+fold.getDocgroupId()+i +">{url:"+getRequest().getContextPath()+"/doctemplate/doctempType/docTempType!synofficetree.action?docgroupId="+fold.getDocgroupId() +"}</li>");	
					str.append("</ul>");	
					str.append("</li>");					
				}
				else{
				}
				for(int j=0;j<itemlist.size();j++){
					ToaDoctemplate item=null;
					item = (ToaDoctemplate) itemlist.get(j);
					str.append("<li id="+item.getDoctemplateId() +">");
					str.append("<span id=son>"+item.getDoctemplateTitle());
					str.append("</span>");
					str.append("</li>");
				}
			}
		}else{
			for(int j=0;j<itemlist.size();j++){
				ToaDoctemplate item=null;
				item = (ToaDoctemplate) itemlist.get(j);
				str.append("<li id="+item.getDoctemplateId() +">");
				str.append("<span id=son>"+item.getDoctemplateTitle());
				str.append("</span>");
				str.append("</li>");
			}
		}
		renderHtml(str.toString());
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:删除公文模板类别
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String delete() throws Exception {
		try{
			model = manager.getTemplateType(docgroupId);
			List list=manager.getTypeByParentId(docgroupId);
			if(model.getToaDoctemplates().isEmpty()&&list.size()<1){
				manager.delTemplateType(docgroupId);
				addActionMessage("删除模板类别成功");
			}else{
				this.renderText("delfalse");
			}
		}catch(ServiceException e){
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:添加或编辑类别
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		if(!"".equals(docgroupId) && docgroupId!=null){
			doctempTypeName = manager.doctempTypeName(docgroupId);
			ToaDoctemplateGroup groupModel = manager.getTemplateType(docgroupId);
			if(groupModel.getDocgroupParentId()!=null&&!groupModel.getDocgroupParentId().equals("0")){
				
				groupType=groupModel.getDocgroupType();
			}
		}
		return INPUT;
	}
	
	/**
	 * author:lanlc
	 * description:初始化添加公文模板类别
	 * modifyer:
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception{
		if(!"".equals(docgroupId) && docgroupId!=null){
			doctempTypeName = manager.doctempTypeName(docgroupId);
			ToaDoctemplateGroup groupModel = manager.getTemplateType(docgroupId);	
			groupType=groupModel.getDocgroupType();
			if(groupType==null){
				groupType="0";			
			}
		}
		return "init";
	}
	
	/**
	 * author:lanlc
	 * description:公文符合条件的模板类别列表
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	
	
	/**
	 * author:lanlc
	 * description:获取公文模板类别实体
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(docgroupId!=null){
			model = manager.getTemplateType(docgroupId);
		}else{
			model = new ToaDoctemplateGroup();
		}
	}
	
	/**
	 * author:lanlc
	 * description:保存公文模板类别
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String save() throws Exception {
		if("".equals(model.getDocgroupId())){
			model.setDocgroupId(null);
		}
		if("".equals(model.getDocgroupParentId())||model.getDocgroupParentId()==null){
			model.setDocgroupParentId("0");
		}
		manager.saveTemplateType(model);
		addActionMessage("保存模板类别成功");
		return "temp";
	}

	public String getDocgroupId() {
		return docgroupId;
	}

	public void setDocgroupId(String docgroupId) {
		this.docgroupId = docgroupId;
	}

	public void setModel(ToaDoctemplateGroup model) {
		this.model = model;
	}

	public ToaDoctemplateGroup getModel() {
		return model;
	}
	@Autowired
	public void setManager(DocTempTypeManager manager) {
		this.manager = manager;
	}

	public List getDocTempTypeList() {
		return docTempTypeList;
	}

	public String[] getHasChild() {
		return hasChild;
	}

	public void setHasChild(String[] hasChild) {
		this.hasChild = hasChild;
	}

	public String getDoctempTypeName() {
		return doctempTypeName;
	}

	public void setDoctempTypeName(String doctempTypeName) {
		this.doctempTypeName = doctempTypeName;
	}
	@Autowired
	public void setItemmanager(DocTempItemManager itemmanager) {
		this.itemmanager = itemmanager;
	}

	public String getDocgroupParentId() {
		return docgroupParentId;
	}

	public void setDocgroupParentId(String docgroupParentId) {
		this.docgroupParentId = docgroupParentId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

}
