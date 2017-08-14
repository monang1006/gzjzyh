//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\prsnfldr\\privateprsnfldr\\PrsnfldrFolderAction.java

package com.strongit.oa.prsnfldr.privateprsnfldr;

import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.di.util.UUIDGenerator;
import com.strongit.oa.bo.ToaPrivatePrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.bo.ToaPrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrShare;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      个人文件夹管理Action
 */
@ParentPackage("default")
@Results({@Result(name=BaseActionSupport.RELOAD,value="prsnfldrFolder!init.action")})
public class PrsnfldrFolderAction extends BaseActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3736428597503472750L;
	private List<ToaPrivatePrsnfldrFolder> folderList;
	private String folderId;
	private ToaPrivatePrsnfldrFolder model = new ToaPrivatePrsnfldrFolder();
	private String userId;//共享接收人
	private String userName;
	private String[] folderProp;//共享属性
	private PrsnfldrFolderManager manager;
	private String[] hasChild;
	/**
	 * @roseuid 493DDBAC002E
	 */
	public PrsnfldrFolderAction() {}

	/**
	 * Sets the value of the folderId property.
	 * 
	 * @param aFolderId
	 *            the new value of the folderId property
	 */
	public void setFolderId(java.lang.String aFolderId) {
		folderId = aFolderId;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaPrivatePrsnfldrFolder getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(PrsnfldrFolderManager aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4939F086004E
	 */
	public String save()throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
			model.setFolderCreateDatetime(new Date());
			model.setUserId(manager.getCurrentUser().getUserId());
			model.setFolderCreatePerson(manager.getUserName());
			//在根节点下添加
			if(model.getFolderParentId() == null || "".equals(model.getFolderParentId())){
				model.setFolderParentId("0");
			}
			String folderSort = model.getFolderSort();
			if("last".equals(folderSort)){//此文件夹位置是最后
				model.setFolderSort(manager.maxFolderSort(model.getFolderParentId()));
			}else{//否则放在最前
				model.setFolderSort(manager.minFolderSort(model.getFolderParentId()));
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
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		ToaPrivatePrsnfldrFolder folder = manager.getFolderById(folderId);
		String folderSort = model.getFolderSort();
		if("last".equals(folderSort)){//此文件夹位置是最后
			folder.setFolderSort(manager.maxFolderSort(folder.getFolderParentId()));
		}else if("first".equals(folderSort)){//否则放在最前 
			folder.setFolderSort(manager.minFolderSort(folder.getFolderParentId()));
		}
		
		folder.setFolderName(model.getFolderName());
		manager.updateFolder(folder,"NO",null,null);
		addActionMessage("ok");	
		return "initeditfolder";
	}

	/**
	 * 转到文件夹共享页面
	 */
	public String initShare()throws Exception{
		prepareModel();
		return "share";
	}
	
	/**
	 * 共享或取消共享文件夹
	 *
	 * 取决于model.isShare
	 */
	public String share()throws Exception{
		try {
			getRequest().setAttribute("backlocation", "javascript:cancel()");
			String isShare = getRequest().getParameter("chkIsShare");
			ToaPrivatePrsnfldrFolder folder = manager.getFolderById(folderId);
			String folderIsShare = folder.getIsShare();
			String code = "";
			if("YES".equals(folderIsShare)){//已经是共享状态
				if("isShare".equals(isShare)){
					//donothing,设定共享范围			
					manager.updateFolder(folder,"U",userId,folderProp);
					code = "0";
				}else{//去掉共享			
					folder.setIsShare("NO");
					manager.updateFolder(folder,"D",null,null);//删除共享信息
					code = "1";
				}
			}else{//不是共享状态
				if("isShare".equals(isShare)){//勾选共享
					folder.setIsShare("YES");
					userName = URLDecoder.decode(userName,"utf-8");
					if("所有人".equals(userName)){
						userId = "allPeople";
					}
					manager.updateFolder(folder,"I",userId,folderProp);
					code = "2";
				}else{
					//donothing
					code = "3";
				}
			}
			renderText(code);//通知页面更新文件夹状态
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}
	
	/**
	 * 删除文件夹，并删除子文件夹。若有文件将删除文件夹下的所有文件
	 * @return java.lang.String
	 * @roseuid 4939F086005D
	 */
	public String delete()throws Exception {
		try {
			ToaPrsnfldrFolder folder = manager.getWholeFolderById(folderId);//获取要删除的文件夹
			List<ToaPrsnfldrFolder> subFoldersList = manager.getAllSubFolder(folderId);//获取此文件夹下的子文件夹集合
			for(Iterator<ToaPrsnfldrFolder> it=subFoldersList.iterator();it.hasNext();){
				manager.deleteFolder(it.next());
			}
			String message = "";
			boolean isSuc = manager.deleteFolder(folder);
			if(isSuc){		
				message = "删除成功!";
			}else{
				message = "对不起,操作失败!请重试或与管理员联系!";
			}
			renderText(message);
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}
	
	/**
	 * 检验要删除的文件夹下是否有文件
	 */
	public String initDelete()throws Exception{
		try {
			ToaPrsnfldrFolder folder = manager.getWholeFolderById(folderId);
			Set<ToaPrsnfldrFile> files = folder.getToaPrsnfldrFiles();
			String message = "";
			if(!files.isEmpty()){//此文件夹下存在文件
				message = "1";
			}else{
				message = "0";
			}
			renderText(message);
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}
	
	/**
	 * @roseuid 4939F086006D
	 */
	protected void prepareModel()throws Exception {
		if(folderId != null){//非添加操作
			model = manager.getFolderById(folderId);
			//获取此文件夹的共享信息
			Set<ToaPrsnfldrShare> set = model.getToaPrsnfldrShares();
			StringBuffer userIdInfo = new StringBuffer("");
			StringBuffer userNameInfo = new StringBuffer("");
			String[] propInfo = new String[set.size()];
			int i=0;
			for(Iterator<ToaPrsnfldrShare> it=set.iterator();it.hasNext();i++){
				ToaPrsnfldrShare share = it.next();
				if(null!=share.getUserId() && !"".equals(share.getUserId())){
					userIdInfo.append(share.getUserId()).append(",");
					userNameInfo.append(manager.getUserName(share.getUserId())).append(",");					
				}
				propInfo[i] = share.getSharePrivilege();
			}
			setFolderProp(propInfo);
			if(userIdInfo.length()>0){
				userId = userIdInfo.substring(0, userIdInfo.length()-1);
				ActionContext.getContext().put("userId", userId);
				if(userId.equals("allPeople")){
					userName = "所有人";
				}else{
					userName = userNameInfo.substring(0, userNameInfo.length()-1);
				}
			}else{
				userName = "所有人";
			}
		}
	}

	/**
	 * @return java.lang.String
	 * @roseuid 493C705B005D
	 */
	public String list() {
		return null;
	}

	/**
	 * 转到个人文件柜主页
	 * @return
	 */
	public String content(){
		return "content";
	}

	/**
	 * 转到共享文件柜主页
	 */
	public String shareContent(){
		return "sharecontent";
	}
	
	/**
	 * 进入个人文件柜主页时展示文件夹树结构
	 * @return
	 */
	public String tree()throws Exception{
		folderList = manager.getAllFolders();
		ToaPrivatePrsnfldrFolder folder=null; 
		List<ToaPrivatePrsnfldrFolder> subFoldersList = null;
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
		String forward = getRequest().getParameter("forward");
		if("move".equals(forward)){//转到移动文件时选择文件夹页面
			return "move";
		}
		return "tree";
	}

	/**
	 * 异步加载树
	 */
	public String syntree()throws Exception{
		try {
			StringBuffer str=new StringBuffer();
			List<ToaPrivatePrsnfldrFolder> folders = manager.getAllSubFolders(folderId);
			for(int i=0;i<folders.size();i++){
				ToaPrivatePrsnfldrFolder folder = folders.get(i);
				List<ToaPrivatePrsnfldrFolder> subFoldersList = manager.getAllSubFolders(folder.getFolderId());
				if(subFoldersList.size()!=0){
					if("YES".equals(folder.getIsShare())){//共享状态
						str.append("<li id="+folder.getFolderId()+" picChange='folder-share'>");					
					}else{
						str.append("<li id="+folder.getFolderId() +">");	
					}
					str.append("<span>"+folder.getFolderName()+"</span>");	
					str.append("<ul class=ajax>");
					str.append("<li id="+folder.getFolderId()+i +">{url:"+getRequest().getContextPath()+"/prsnfldr/privateprsnfldr/prsnfldrFolder!syntree.action?folderId="+folder.getFolderId() +"}</li>");	
					str.append("</ul>");	
					str.append("</li>");
				}else{
					if("YES".equals(folder.getIsShare())){
						str.append("<li id="+folder.getFolderId()+" leafChange='folder-share-leaf'>");		
					}else{
						str.append("<li id="+folder.getFolderId()+" >");		
					}
					str.append("<span>"+folder.getFolderName());
					str.append("</span>");
					str.append("</li>");
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
	 * 进入共享文件柜主页展示共享文件夹树
	 */
	public String shareFolderTree()throws Exception{
		getRequest().setAttribute("backlocation", "javascript:history.back()");
		folderList = manager.getAllShareFolders();	
		ToaPrivatePrsnfldrFolder folder=null; 
		List<ToaPrivatePrsnfldrFolder> subFoldersList = null;
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
		return "sharefoldertree";
	}

	/**
	 * 异步加载树(共享文件柜)
	 */
	public String synShareTree()throws Exception{
		try {
/*			StringBuffer str=new StringBuffer();
			List<ToaPrivatePrsnfldrFolder> folders = manager.getAllShareSubFolders(folderId);
			for(int i=0;i<folders.size();i++){
				ToaPrivatePrsnfldrFolder folder = folders.get(i);
				List<ToaPrivatePrsnfldrFolder> subFoldersList = manager.getAllSubFolders(folder.getFolderId());
				if(subFoldersList.size()!=0){
					str.append("<li id="+folder.getFolderId()+"><span>"+folder.getFolderName()+"</span>\n");
					str.append("<ul class=ajax>\n");
					str.append("<li id="+folder.getFolderId()+i+">{url:"+getRequest().getContextPath()+"/prsnfldr/privateprsnfldr/prsnfldrFolder!syntree.action?folderId="+folder.getFolderId()+"}</li>\n");
					str.append("</ul>\n");
					str.append("</li>\n");
				}else{
					str.append("<li id="+folder.getFolderId()+"><span>"+folder.getFolderName()+"</span></li>\n");
				}
				
			}
			renderHtml(str.toString());*/
			StringBuffer str=new StringBuffer();
			List<ToaPrivatePrsnfldrFolder> folders = manager.getAllSubFolders(folderId);
			UUIDGenerator generator = new UUIDGenerator();
			for(int i=0;i<folders.size();i++){
				ToaPrivatePrsnfldrFolder folder = folders.get(i);
				List<ToaPrivatePrsnfldrFolder> subFoldersList = manager.getAllSubFolders(folder.getFolderId());
				if(subFoldersList.size()!=0){
					if("YES".equals(folder.getIsShare())){//共享状态
						str.append("<li folderId="+folder.getFolderId()+" id="+folder.getFolderId()+generator.generate() +" picChange='folder-share'>");					
					}else{
						str.append("<li folderId="+folder.getFolderId()+" id="+folder.getFolderId() +">");	
					}
					str.append("<span>"+folder.getFolderName()+"</span>");	
					str.append("<ul class=ajax>");
					str.append("<li folderId="+folder.getFolderId()+" id="+folder.getFolderId()+i +">{url:"+getRequest().getContextPath()+"/prsnfldr/privateprsnfldr/prsnfldrFolder!synShareTree.action?folderId="+folder.getFolderId() +"}</li>");	
					str.append("</ul>");	
					str.append("</li>");
				}else{
					if("YES".equals(folder.getIsShare())){
						str.append("<li folderId="+folder.getFolderId()+" id="+folder.getFolderId()+generator.generate()+" leafChange='folder-share-leaf'>");		
					}else{
						str.append("<li folderId="+folder.getFolderId()+" id="+folder.getFolderId()+generator.generate()+" >");		
					}
					str.append("<span>"+folder.getFolderName());
					str.append("</span>");
					str.append("</li>");
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
	 * 转到添加文件夹页面
	 * 取决于prepareModel
	 * @return
	 */
	public String init(){
		return "init";
	}
	
	/**
	 * 转到修改文件夹页面
	 * @return
	 */
	public String initEdit()throws Exception{
		prepareModel();
		return "initeditfolder";
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
	
	public String getFolderId() {
		return folderId;
	}

	public List<ToaPrivatePrsnfldrFolder> getFolderList() {
		return folderList;
	}

	@Override
	public String input() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	public String[] getHasChild() {
		return hasChild;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public String[] getFolderProp() {
		return folderProp;
	}

	public void setFolderProp(String[] folderProp) {
		this.folderProp = folderProp;
	}

}
