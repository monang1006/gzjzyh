/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：类目管理action 
 */

package com.strongit.oa.archive.sort;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
import com.sun.tools.xjc.model.Model;

/**
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
public class ArchiveSortAction extends BaseActionSupport<ToaArchiveSort> {
	private Page<ToaArchiveSort> page = new Page<ToaArchiveSort>(FlexTableTag.MAX_ROWS, true);
	// 类目id
	private String sortId; 
	// 类目列表
	private List archiveSortList; 
	// 父节点
	private String parentId; 
	// 控制跳转字符串
	private String forwardStr; 

	private ToaArchiveSort modle = new ToaArchiveSort();

	private ArchiveSortManager manager;
	private ArchiveFolderManager fodlerManager;
	//统一用户接口
	private IUserService userService;				

	private String objId = null;

	private String objName = null;
	// 类目编号
	private String sortNo; 

	/**
	 * @roseuid 494F04F8030D
	 */
	public ArchiveSortAction() {

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
	 * Sets the value of the sortId property.
	 * 
	 * @param aSortId
	 *            the new value of the sortId property
	 */
	public void setSortId(String aSortId) {
		sortId = aSortId;
	}

	/**
	 * Access method for the archiveSortList property.
	 * 
	 * @return the current value of the archiveSortList property
	 */
	public List getArchiveSortList() {
		return archiveSortList;
	}

	/**
	 * Access method for the parentId property.
	 * 
	 * @return the current value of the parentId property
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets the value of the parentId property.
	 * 
	 * @param aParentId
	 *            the new value of the parentId property
	 */
	public void setParentId(String aParentId) {
		parentId = aParentId;
	}

	/**
	 * Access method for the modle property.
	 * 
	 * @return the current value of the modle property
	 */
	public ToaArchiveSort getModle() {
		return modle;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(ArchiveSortManager aManager) {
		manager = aManager;
	}
	/**
     * Sets the value of the manager property.
     * 
     * @param aManager
     *            the new value of the manager property
     */
    @Autowired
    public void setFolderManager(ArchiveFolderManager aManager) {
        fodlerManager = aManager;
    }
	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:40:07
	 * @desc: 获取类目list，构造树节点
	 * @param
	 * @return String
	 */
	public String tree() throws Exception {
		getRequest()
				.setAttribute(
						"backlocation",
						getRequest().getContextPath()
								+ "/archive/sort/archiveSort!tree.action?forwardStr=tree");
		archiveSortList = manager.getAllArchiveSort();
		
		
		if("tree".equals(forwardStr)){//是否类目管理树
				archiveSortList = manager.getAllArchiveSortByOrg();				
		}else{
			archiveSortList = manager.getAllArchiveSort();
		}
		
		
		return forwardStr;
	}
	/*
	 * @author：xush
     * @time：6/27/2013 2:52 PM
     * @desc: 插入今年相应类目下的案卷
     * @param
     * @return String
	 */
  public String importFolder() throws Exception {
      String msg="0";
      //获取所有的类目对象
      archiveSortList = manager.getAllArchiveSort();
      //传递到jsp页面的提示信息
      msg=fodlerManager.insertFolder(archiveSortList,msg);
    //传递到jsp页面的提示信息
      return renderText(msg);
  }
	
	/**
	 * @return java.lang.String
	 * @roseuid 494F445C0109
	 */
	@Override
	public String list() throws Exception {
		manager.getAllArchiveSort();
		return SUCCESS;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 494F445C0138
	 */
	@Override
	public String save() throws Exception {
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/archive/sort/archiveSort!input.action");
		if ("".equals(modle.getSortId())) {
			modle.setSortId(null);
			modle.setSortParentNo(modle.getSortParentNo());
		}
		 User user=userService.getCurrentUser();
		 TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(user.getUserId());
		 if(org!=null){
			 modle.setSortOrgId(org.getOrgId());
			 modle.setSortOrgCode(org.getSupOrgCode());
		 }
	
		String msg=manager.saveArchiveSort(modle,new OALogInfo("保存类目"));
		addActionMessage(msg);
		 StringBuffer returnhtml = new StringBuffer("<script> var scriptroot = '").append(getRequest().getContextPath()).append("'</script>")
		   .append("<SCRIPT src='")
		   .append(getRequest().getContextPath())
		   .append("/common/js/commontab/workservice.js'>")
		   .append("</SCRIPT>")
		   .append("<SCRIPT src='")
		   .append(getRequest().getContextPath())
		   .append("/common/js/commontab/service.js'>")
		   .append("</SCRIPT>")
		   .append("<script>")
		   
		   .append(" window.parent.location='")
		   .append(getRequest().getContextPath())
		   .append("/fileNameRedirectAction.action?toPage=archive/sort/sortContent.jsp")
		   .append("';</script>");
		   return renderHtml(returnhtml.toString());
	}

	/**
	 * 删除类型
	 * @return java.lang.String
	 * @roseuid 494F445C0167
	 */
	@Override
	public String delete() throws Exception {
		getRequest()
				.setAttribute(
						"backlocation",
						"javascript: window.parent.location="
								+ getRequest().getContextPath()
								+ "/fileNameRedirectAction.action?toPage=archive/sort/sortContent.jsp");
		String foward = INPUT;	
		StringBuffer returnhtml = new StringBuffer(
		"<script> var scriptroot = '")
		.append(getRequest().getContextPath())
		.append("'</script>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/workservice.js'>")
		.append("</SCRIPT>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/service.js'>")
		.append("</SCRIPT>")
		.append("<script>");
		if (modle.getSortId() != null && !"".equals(modle.getSortId())
				&& !"null".equals(modle.getSortId())) {
			String msg = manager.delArchiveSort(modle.getSortId(),new OALogInfo("删除类目")); // 删除类目
			//addActionMessage(msg);	
			if (msg != null && msg.equals("该类目中存在案卷，请先删除相应的案卷在做此操作")) {	
				returnhtml.append("alert('")
					.append("该类目中存在案卷，请先删除相应的案卷")
					.append("');window.parent.location.reload();window.close();</script>");		
			}else if(msg!=null&&"该类目下存在子类目，请先删除子类目".equals(msg)){
				returnhtml.append("alert('")
				.append("该类目下存在子类目，请先删除子类目")
				.append("');window.parent.location.reload();window.close();</script>");		
		
			}else {
				returnhtml
					.append("  window.parent.location='")
					.append(getRequest().getContextPath())
					.append("/fileNameRedirectAction.action?toPage=archive/sort/sortContent.jsp")
					.append("';</script>");
			}
		} else {
			
			returnhtml.append("alert('")
			.append("没有要删除的信息")
			.append("');</script>");
		}
		return renderHtml(returnhtml.toString());
	}

	/**
	 * @roseuid 494F445C0196
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (sortId != null && !"".equals(sortId) && !"null".equals(sortId)) {
			modle = manager.getArchiveSort(sortId);
		} else {
			modle = new ToaArchiveSort();
			if (parentId != null && !"".equals(parentId)
					&& !"null".equals(parentId))
				modle.setSortParentNo(parentId);
			else
				modle.setSortParentNo("0");
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-8上午09:28:12
	 * @desc: 类目编号是否有重复的
	 * @param
	 * @return
	 */
	public String isExist() throws Exception {
		//getRequest().setAttribute("backlocation","javascript:history.back();");	
		String flag = "1";
		try {
			flag = manager.isExist(sortNo,sortId,new OALogInfo("判断类目编号是否重复"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return renderText(flag);
		//return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		getRequest().setAttribute("backlocation","javascript:history.back();");	
		prepareModel();
		return INPUT;
	}

	public ToaArchiveSort getModel() {
		// TODO Auto-generated method stub
		return modle;
	}

	public void setForwardStr(String forwardStr) {
		this.forwardStr = forwardStr;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
