/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-27
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板套红类别action
 */
package com.strongit.oa.docredtemplate.docredtype;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaDocred;
import com.strongit.oa.bo.ToaDocredGroup;
import com.strongit.oa.docredtemplate.docreditem.DocRedItemManager;
import com.strongit.oa.util.TempPo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.webapp.action.BaseActionSupport;
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "docRedType.action", type = ServletActionRedirectResult.class) })
public class DocRedTypeAction extends BaseActionSupport {
	
	private String redtempGroupId; //公文套红ID
	private List docRedTypeList; //公文类型集
	private String[] hasChild; //是否有子节点
	private String docredGroupName; //套红名称
	private ToaDocredGroup model = new ToaDocredGroup();
	private DocRedTypeManager manager;
	private DocRedItemManager itemmanager;

	/**
	 * author:lanlc
	 * description:获取所有公文模板类别及子类虽,树型展现
	 * modifyer:
	 * @return
	 */
	public String tree()throws Exception{
		docRedTypeList=manager.docDocredTypeList();
		if(docRedTypeList.isEmpty()){
			model = new ToaDocredGroup();
			model.setRedtempGroupName("默认套红");
			model.setRedtempGroupParentId("0");
			manager.saveDocredType(model);
			docRedTypeList=manager.docDocredTypeList();
		}
		ToaDocredGroup group = null;
		List<ToaDocredGroup> subGroupList = null;
		hasChild = new String[docRedTypeList.size()];
		for(int i=0;i<docRedTypeList.size();i++){
			group = (ToaDocredGroup) docRedTypeList.get(i);
			subGroupList = manager.docredSubTypeList(group.getRedtempGroupId());
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
	 * description:office控件中用到的套红类别
	 * modifyer:
	 * @return
	 */
	public String officetree()throws Exception{
		/*ToaDocredGroup group = null;
		List<ToaDocredGroup> subGroupList = null;
		hasChild = new String[docRedTypeList.size()];
		for(int i=0;i<docRedTypeList.size();i++){
			group = (ToaDocredGroup) docRedTypeList.get(i);
			subGroupList = manager.docredSubTypeList(group.getRedtempGroupId());
			List itemlist = itemmanager.getTypeByItem(group.getRedtempGroupId());
			if(subGroupList.size()>0 || itemlist.size()>0){
				hasChild[i] = "1";
			}else{
				hasChild[i] = "0";
			}
		}*/
		getRequest().setAttribute("typeList", manager.docRedTree());
		return "officetree";
	}
	
	/**
	 * author:lanlc
	 * description:异步加载树
	 * modifyer:
	 * @return
	 */
	public String syntree()throws Exception{
		StringBuffer str=new StringBuffer();
		List<ToaDocredGroup> folds = manager.docredSubTypeList(redtempGroupId);
		for(int i=0; i< folds.size();i++){
			ToaDocredGroup fold= folds.get(i);
			List<ToaDocredGroup> subFoldsList = manager.docredSubTypeList(fold.getRedtempGroupId());
			if(!subFoldsList.isEmpty()){
				str.append("<li id="+fold.getRedtempGroupId() +">");
				str.append("<span>"+fold.getRedtempGroupName()+"</span>");	
				str.append("<ul class=ajax>");
				str.append("<li id="+fold.getRedtempGroupId()+i +">{url:"+getRequest().getContextPath()+"/docredtemplate/docredtype/docRedType!syntree.action?redtempGroupId="+fold.getRedtempGroupId() +"}</li>");	
				str.append("</ul>");	
				str.append("</li>");
			}else{
				str.append("<li id="+fold.getRedtempGroupId() +">");
				str.append("<span>"+fold.getRedtempGroupName());
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
	public String synofficetree()throws Exception{
		StringBuffer str=new StringBuffer();
		List<ToaDocredGroup> folds = manager.docredSubTypeList(redtempGroupId);
		List itemlist = itemmanager.getTypeByItem(redtempGroupId);
		if(folds.size()>0){
			for(int i=0; i< folds.size();i++){
				ToaDocredGroup fold= folds.get(i);
				List<ToaDocredGroup> subFoldsList = manager.docredSubTypeList(fold.getRedtempGroupId());
				List itemsublist = itemmanager.getTypeByItem(fold.getRedtempGroupId());
				if(!subFoldsList.isEmpty() || itemsublist.size()>0){
					str.append("<li id="+fold.getRedtempGroupId() +">");
					str.append("<span>"+fold.getRedtempGroupName()+"</span>");	
					str.append("<ul class=ajax>");
					str.append("<li id="+fold.getRedtempGroupId()+i +">{url:"+getRequest().getContextPath()+"/docredtemplate/docredtype/docRedType!synofficetree.action?redtempGroupId="+fold.getRedtempGroupId() +"}</li>");	
					str.append("</ul>");	
					str.append("</li>");
				}else{
				}
				for(int j=0;j<itemlist.size();j++){
					ToaDocred item = null;
					item = (ToaDocred) itemlist.get(j);
					str.append("<li id="+item.getRedstempId() +">");
					str.append("<span id=son>"+item.getRedstempTitle());
					str.append("</span>");
					str.append("</li>");
				}
			}
		}else{
			for(int j=0;j<itemlist.size();j++){
				ToaDocred item = null;
				item = (ToaDocred) itemlist.get(j);
				str.append("<li id="+item.getRedstempId() +">");
				str.append("<span id=son>"+item.getRedstempTitle());
				str.append("</span>");
				str.append("</li>");
			}
		}
		renderHtml(str.toString());
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:删除公文套红类别
	 * modifyer:
	 */
	@Override
	public String delete() throws Exception {
		try{
			model = manager.getDocredType(redtempGroupId);
			if(model.getToaDocred().isEmpty()){
				manager.delDocredType(redtempGroupId);
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
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		if(!"".equals(redtempGroupId) && redtempGroupId!=null){
			docredGroupName = manager.redtempGroupName(redtempGroupId);
		}
		return INPUT;
		
	}
	
	/**
	 * author:lanlc
	 * description:初始化添加公文套红类别
	 * modifyer:
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception{
		if(!"".equals(redtempGroupId) && redtempGroupId!=null){
			docredGroupName = manager.redtempGroupName(redtempGroupId);
		}
		return "init";
	}
	
	/**
	 * author:lanlc
	 * description:公文符合条件的套红类别列表
	 * modifyer:
	 */
	@Override
	public String list() throws Exception {
		return SUCCESS;
		
	}
	
	/**
	 * author:lanlc
	 * description:获取公文套红类别实体
	 * modifyer:
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(redtempGroupId!=null){
			model = manager.getDocredType(redtempGroupId);
		}else{
			model = new ToaDocredGroup();
		}
	}
	
	/**
	 * author:lanlc
	 * description:保存公文套红类别
	 * modifyer:
	 */
	@Override
	public String save() throws Exception {
		if("".equals(model.getRedtempGroupId())){
			model.setRedtempGroupId(null);
		}
		if("".equals(model.getRedtempGroupParentId())||model.getRedtempGroupParentId()==null){
			model.setRedtempGroupParentId("0");
		}
		manager.saveDocredType(model);
		addActionMessage("保存模板类别成功");
		return "temp";
	}
	
	public List getDocRedTypeList() {
		return docRedTypeList;
	}

	public void setDocRedTypeList(List docRedTypeList) {
		this.docRedTypeList = docRedTypeList;
	}



	public String[] getHasChild() {
		return hasChild;
	}

	public void setHasChild(String[] hasChild) {
		this.hasChild = hasChild;
	}

	public ToaDocredGroup getModel() {
		return model;
	}

	public void setModel(ToaDocredGroup model) {
		this.model = model;
	}

	public String getRedtempGroupId() {
		return redtempGroupId;
	}

	public void setRedtempGroupId(String redtempGroupId) {
		this.redtempGroupId = redtempGroupId;
	}

	
	@Autowired
	public void setManager(DocRedTypeManager manager) {
		this.manager = manager;
	}

	public String getDocredGroupName() {
		return docredGroupName;
	}

	public void setDocredGroupName(String docredGroupName) {
		this.docredGroupName = docredGroupName;
	}
	
	@Autowired
	public void setItemmanager(DocRedItemManager itemmanager) {
		this.itemmanager = itemmanager;
	}

}
