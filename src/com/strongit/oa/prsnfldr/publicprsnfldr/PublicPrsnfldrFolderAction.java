//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\prsnfldr\\publicprsnfldr\\PublicPrsnfldrFolderAction.java

package com.strongit.oa.prsnfldr.publicprsnfldr;

import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaPublicPrsnfldrFolder;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      公共文件夹管理Action
 */
@ParentPackage("default")
@Results({@Result(name=BaseActionSupport.RELOAD,value="/prsnfldr/publicPrsnfldrFolder.action")})
public class PublicPrsnfldrFolderAction extends BaseActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6864447501316052L;

	private ToaPublicPrsnfldrFolder model = new ToaPublicPrsnfldrFolder();

	private PublicPrsnfldrFolderManager manager;
	
	private List<ToaPublicPrsnfldrFolder> folderList ;
	
	private String folderId;
	
	private String folderName;
	
	private String[] hasChild;

	/**
	 * @roseuid 493DD6E1031C
	 */
	public PublicPrsnfldrFolderAction() {

	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaPublicPrsnfldrFolder getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(PublicPrsnfldrFolderManager aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 493DD6E1033C
	 */
	public String list() {
		return null;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 493DD6E1034B
	 */
	public String save()throws Exception {
		model.setFolderCreateDatetime(new Date());
		model.setUserId(manager.getCurrentUser().getUserId());
		model.setFolderCreatePerson(manager.getCurrentUser().getUserName());//这里等同一用户接口完成要换成当前用户
		//在根节点下添加
		if(model.getFolderParentId() == null || "".equals(model.getFolderParentId())){
			model.setFolderParentId("0");
		}
		String folderSort = model.getFolderSort();
		if("last".equals(folderSort)){//此文件夹位置是最后
			model.setFolderSort(manager.maxPublicFolderSort((model.getFolderParentId())));
		}else{//否则放在最前
			model.setFolderSort(manager.minPublicFolderSort(model.getFolderParentId()));
		}
		manager.addFolder(model);
		//addActionMessage("ok");
		getRequest().setAttribute("isTrue", 1);
		return init();
	}

	/**
	 * 修改文件夹
	 */
	public String edit()throws Exception{
		ToaPublicPrsnfldrFolder folder = manager.getFolderById(folderId);
		String folderSort = model.getFolderSort();
		folder.setFolderName(model.getFolderName());
		if("last".equals(folderSort)){//此文件夹位置是最后
			folder.setFolderSort(manager.maxPublicFolderSort(folder.getFolderParentId()));
		}else if("first".equals(folderSort)){//否则放在最前 
			folder.setFolderSort(manager.minPublicFolderSort(folder.getFolderParentId()));
		}
		
		manager.updateFolder(folder);
		addActionMessage("ok");	
		return "initeditfolder";
	}
	
	/**
	 * 进入文件夹编辑页面
	 * @return
	 */
	public String initEdit()throws Exception{
		prepareModel();
		return "initeditfolder";
	}
	
	/**
	 * @return java.lang.String
	 * @roseuid 493DD6E1035B
	 */
	public String delete() {
		return null;
	}

	/**
	 * 转到添加或编辑文件夹页面
	 * 取决于prepareModel
	 * @return
	 */
	public String init()throws Exception{
		prepareModel();
		return "init";
	}
	/**
	 * @roseuid 493DD6E1036B
	 */
	protected void prepareModel()throws Exception {
		if(folderId!=null){
			model = manager.getFolderById(folderId);
		}
	}

	/**
	 * 
	 * author:dengzc
	 * description:
	 * modifyer:
	 * description:进入公共文件柜主页
	 * @return
	 */
	public String publicContent(){
		return "content";
	}
	
	/**
	 * 展示公共文件夹树
	 * @return
	 */
	public String tree()throws Exception{
		folderList = manager.getAllFolders();
		ToaPublicPrsnfldrFolder folder=null; 
		List<ToaPublicPrsnfldrFolder> subFoldersList = null;
		hasChild = new String[folderList.size()];
		for(int i=0;i<folderList.size();i++){
			folder=folderList.get(i);
			subFoldersList = manager.getAllSubFolders(folder.getFolderId());
			if(subFoldersList.size()>0){//此文件夹下有子文件夹
				hasChild[i] = "1";
			}else{
				hasChild[i] = "0";
			}
		}
		return "tree";
	}

	/**
	 * 异步加载树
	 */
	public String syntree()throws Exception{
		try {
			StringBuffer str=new StringBuffer();
			List<ToaPublicPrsnfldrFolder> folders = manager.getAllSubFolders(folderId);
			for(int i=0;i<folders.size();i++){
				ToaPublicPrsnfldrFolder folder = folders.get(i);
				List<ToaPublicPrsnfldrFolder> subFoldersList = manager.getAllSubFolders(folder.getFolderId());
				if(subFoldersList.size()!=0){
					str.append("<li id="+folder.getFolderId()+"><span title=\""+folder.getFolderCreatePerson()+"\">"+folder.getFolderName()+"</span>\n");
					str.append("<ul class=ajax>\n");
					str.append("<li id="+folder.getFolderId()+i+">{url:"+getRequest().getContextPath()+"/prsnfldr/publicprsnfldr/publicPrsnfldrFolder!syntree.action?folderId="+folder.getFolderId()+"}</li>\n");
					str.append("</ul>\n");
					str.append("</li>\n");
				}else{
					str.append("<li id="+folder.getFolderId()+"><span title=\""+folder.getFolderCreatePerson()+"\">"+folder.getFolderName()+"</span></li>\n");
				}
				
			}
			renderHtml(str.toString());
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * 校验文件夹名称是否已经使用.
	 * @author:邓志城
	 * @date:2009-10-14 下午04:54:40
	 * @return
	 * @throws Exception
	 */
	public String checkFolderName() throws Exception {
		String ret = "";
		try{
			boolean isUsed = manager.isFolderNameUsed(model.getFolderParentId(), model.getFolderName());
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
	
	@Override
	public String input() throws Exception {
		return null;
	}

	public List<ToaPublicPrsnfldrFolder> getFolderList() {
		return folderList;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName)throws Exception {
		if(folderId != null && folderName.startsWith("%")){
			folderName = java.net.URLDecoder.decode(folderName, "utf-8");
		}
		this.folderName = folderName;
	}

	public String[] getHasChild() {
		return hasChild;
	}

}
