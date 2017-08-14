/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-01-08
 * Autour: pengxq
 * Version: V1.0
 * Description：案卷销毁管理action
 */

package com.strongit.oa.archive.archiveDestr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaArchiveDestroy;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "archiveDestr.action", type = ServletActionRedirectResult.class) })
public class ArchiveDestrAction extends BaseActionSupport<ToaArchiveDestroy> {
	private Page<ToaArchiveDestroy> page = new Page<ToaArchiveDestroy>(FlexTableTag.MAX_ROWS, true);

	private String destroyId; // 销毁案卷id

	private String folderId; // 案卷id

	private String archiveSortId; // 案卷类目id

	private List destroyFileList; // 销毁文件列表

	private ToaArchiveDestroy model = new ToaArchiveDestroy(); // 销毁文件对象

	private ArchiveDestrManager manager;// 销毁案卷manager

	private String status; // 案卷状态

	private String content; // 销毁原因

	// private IUserService userService; //用户接口
	private Date auditTime; // 审核时间

	private String auditDesc; // 审核描述

	private String isPass; // 审核是否通过

	private String destroyFolderNo; // 销毁案卷编号

	private String destroyFolderName;// 销毁案卷名称

	private Date destroyApplyTime; // 申请销毁时间

	private Date destroyFolderDate;// 销毁案卷的创建日期
	
	private String auditingTime;//销毁审核时间
	
	private String folderDate;//案卷的创建日期(格式后的时间显示)
	
	private IUserService userService;// 用户IUserService
	
	private ArchiveFolderManager folderManager;//案卷manager
	
	private HashMap<String, String> map = new HashMap<String, String>();

	/**
	 * @roseuid 4959D19A01A5
	 */
	public ArchiveDestrAction() {
		map.put(null, "全部");
		map.put("0", "待审核");
		map.put("1", "审核中");
		map.put("2", "已销毁");
		map.put("3", "不通过");
	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page<ToaArchiveDestroy> getPage() {
		return page;
	}

	/**
	 * Access method for the destroyId property.
	 * 
	 * @return the current value of the destroyId property
	 */
	public java.lang.String getDestroyId() {
		return destroyId;
	}

	/**
	 * Sets the value of the destroyId property.
	 * 
	 * @param aDestroyId
	 *            the new value of the destroyId property
	 */
	public void setDestroyId(java.lang.String aDestroyId) {
		destroyId = aDestroyId;
	}

	/**
	 * Access method for the folderId property.
	 * 
	 * @return the current value of the folderId property
	 */
	public java.lang.String getFolderId() {
		return folderId;
	}

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
	 * Access method for the archiveSortId property.
	 * 
	 * @return the current value of the archiveSortId property
	 */
	public java.lang.String getArchiveSortId() {
		return archiveSortId;
	}

	/**
	 * Sets the value of the archiveSortId property.
	 * 
	 * @param aArchiveSortId
	 *            the new value of the archiveSortId property
	 */
	public void setArchiveSortId(java.lang.String aArchiveSortId) {
		archiveSortId = aArchiveSortId;
	}

	/**
	 * Access method for the destroyFileList property.
	 * 
	 * @return the current value of the destroyFileList property
	 */
	public java.util.List getDestroyFileList() {
		return destroyFileList;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaArchiveDestroy getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(ArchiveDestrManager aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4959D19A0213
	 */
	@Override
	public String list() throws Exception {
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/archive/archiveDestr/archiveDestr.action");
		status = model.getDestroyAuditingType();
		model.setDestroyApplyName(destroyFolderName);
		model.setDestroyFolderNo(destroyFolderNo);
		model.setDestroyApplyTime(destroyApplyTime);
		model.setDestroyFolderDate(destroyFolderDate);
		page=manager.searchDestrFolder(page, model, status);
		return SUCCESS;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4959D19A0261
	 */
	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel();");
		ToaArchiveFolder obj = manager.getFolder(folderId);
		String msg="销毁案卷失败！";
		if (obj != null) {
			model.setDestroyApplyDesc(content);
			model.setDestroyFolderName(obj.getFolderName()); // 销毁案卷题名
			model.setDestroyFolderNo(obj.getFolderNo()); // 销毁案卷编号
			model.setDestroyFolderDate(obj.getFolderDate()); // 销毁案卷日期
			model.setDestroyFolderOrgname(obj.getFolderOrgName());// 销毁所属全宗名称
			model.setDestroyFolderFromtime(obj.getFolderFromDate()); // 销毁档案年度开始时间
			model.setDestroyFolderEndtime(obj.getFolderToDate()); // 销毁档案年度结束时间
			model.setDestroyFolderArchiveNo(obj.getFolderArchiveNo()); // 销毁归档号
			model.setDestroyFolderDepartment(obj.getFolderDepartment()); // 销毁案卷所属处事
			model.setDestroyFolderCreaterName(obj.getFolderCreaterName()); // 销毁案卷创建者名称
			// model.setDestroyFolderCreaterTime(obj.get) //销毁案卷创建者时间
			model.setDestroyFolderDesc(obj.getFolderDesc()); // 销毁案卷情况说明
			model.setDestroyAuditingType("0");
			model.setDestroyApplyTime(new Date());
			String isSuccess=null;
			isSuccess = manager.saveDestrFolder(model, folderId,new OALogInfo("保存案卷销毁记录")); // 保存销毁申请单和销毁文件
			if (isSuccess != null && isSuccess.equals("yes"))
				msg=manager.updateStatus(obj, "4"); // 修改案卷状态为"销毁待审核"
		}	
		 //  addActionMessage(msg);	
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
			.append("<script>")
			.append("window.dialogArguments.submitForm();window.close();")
			.append("</script>");
		   	return renderHtml(returnhtml.toString());
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4959D19A029F
	 */
	@Override
	public String delete() throws Exception {
		return null;
	}

	/**
	 * @roseuid 4959D19A02EE
	 */
	@Override
	protected void prepareModel() {
		if (destroyId != null && !"".equals(destroyId)
				&& !"null".equals(destroyId)) {
			model = manager.getDestrFolder(destroyId); // 获取销毁案卷对象
		} else {
			model = new ToaArchiveDestroy();
			model.setDestroyApplyName(userService.getCurrentUser().getUserName());
		}

	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		getRequest().setAttribute("backlocation", "javascript:cancel();");
		String forward = "";
		String msg="";
		ToaArchiveFolder obj = manager.getFolder(folderId);
		String status = obj.getFolderAuditing();
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
		.append("</SCRIPT>");
		if (status != null && status.equals("1")) { // 已归档的案卷才可以进行销毁操作
			prepareModel();
			model.setDestroyFolderNo(obj.getFolderNo());
			model.setDestroyFolderName(obj.getFolderName());
			forward=INPUT;
		} else { // 未归档的给予提示
			
			if (status != null && status.equals("0")) {
				//addActionMessage("该案卷未归档，只有归档的案卷才能执行销毁操作！");
				msg="该案卷未归档，只有归档的案卷才可以申请销毁！";
			} else if (status != null && status.equals("2")) {
			//	addActionMessage("该案在归档审核中，只有归档的案卷才能执行销毁操作！");
				msg="该案在归档审核中，只有归档的案卷才可以申请销毁！";
			} else if (status != null && status.equals("3")) {
			//	addActionMessage("该案卷被驳回，只有归档的案卷才能执行销毁操作！");
				msg="该案卷归档申请被驳回，只有归档的案卷才可以申请销毁！";
			} else if (status != null && status.equals("4")) {
				//addActionMessage("该案卷销毁待审核中，只有归档的案卷才能执行销毁操作！");
				msg="该案卷已提交了销毁申请，请耐心等待！";
			}else if(status!=null&&status.equals("5")){
				msg="该案卷销毁申请被驳回，不可以销毁！";
			}
			StringBuffer returnhtmls = new StringBuffer("<script>").append(
							"alert('").append(
							getActionMessages().toString()).append(
							"');window.close();</script>");	
			//forward = renderHtml(returnhtmls.toString());
			return renderHtml(returnhtmls.toString());
		}
		
		return forward;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-8下午04:07:02
	 * @desc: 销毁审核
	 * @param
	 * @return
	 */
	public String auditRecord() throws Exception {
		List<ToaArchiveDestroy> destroyList = new ArrayList<ToaArchiveDestroy>();
		// User user=userService.getCurrentUser();
		// String userName=user.getUserName();
		String msg="案卷销毁审核失败！";
		if (isPass != null && !isPass.equals("0")) {
			if (destroyId.length() > 0) {
				String[] destroyIds = destroyId.split(",");
				for (int i = 0; i < destroyIds.length; i++) {
					model = manager.getDestrFolder(destroyIds[i]); // 获取销毁案卷对象
					model.setDestroyAuditingDesc(auditDesc);
					model.setDestroyAuditingTime(auditTime);
					model.setDestroyAuditingName(userService.getCurrentUser().getUserName());
					model.setDestroyAuditingType("2");
					destroyList.add(model);
					manager.update(model,new OALogInfo("修改销毁审核中的案卷"));
				}
			}
			manager.save(destroyList,new OALogInfo("保存销毁审核情况"));
		}else
		{
			if (destroyId.length() > 0) {
				String[] destroyIds = destroyId.split(",");
				for (int i = 0; i < destroyIds.length; i++) {
					model = manager.getDestrFolder(destroyIds[i]); // 获取销毁案卷对象
					model.setDestroyAuditingDesc(auditDesc);
					model.setDestroyAuditingTime(auditTime);
					model.setDestroyAuditingName(userService.getCurrentUser().getUserName());
					model.setDestroyAuditingType("3");
					destroyList.add(model);
					manager.update(model,new OALogInfo("修改销毁审核中的案卷"));
					ToaArchiveFolder folder=(ToaArchiveFolder)folderManager.getFolderByNo(model.getDestroyFolderNo()).get(0);
					msg=manager.updateStatus(folder, "5",new OALogInfo("修改案卷状态为销毁驳回")); // 修改案卷状态为"销毁驳回"
				}
			}
		}
		msg="操作成功！";
		 addActionMessage(msg);	
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
			.append("<script>")
			.append("window.dialogArguments.submitForm();window.close();")
			.append("</script>");
		   	return renderHtml(returnhtml.toString());
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-8下午04:07:44
	 * @desc: 查看销毁文件
	 * @param
	 * @return
	 */
	public String view() throws Exception {
		try {
			model = manager.getDestrFolder(destroyId);
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
			if (model.getDestroyAuditingTime() != null
					&& !"".equals(model.getDestroyAuditingTime()))// 判断销毁时间是否为空
			{
				auditingTime = dateFm.format(model.getDestroyAuditingTime());
			}
			if(model.getDestroyFolderDate()!=null&&!"".equals(model.getDestroyFolderDate()))
			{
				folderDate=dateFm.format(model.getDestroyFolderDate());
			}
			destroyFileList = manager.getDestrFile(destroyId);
			String departmentName = userService.getDepartmentByOrgId(
					model.getDestroyFolderDepartment()).getOrgName();// 获取部门名称
			model.setDepartmentName(departmentName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "view";
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-8下午04:08:48
	 * @desc: 查看销毁案卷
	 * @param
	 * @return
	 */
	public String viewDestrFolder() throws Exception {
		try {
			model = manager.getDestrFolder(destroyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "viewDestrFolder";
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-9上午10:56:13
	 * @desc: 获取销毁状态
	 * @param
	 * @return
	 */
	public String getDestrStatus() throws Exception {
		String[] destroyIds = destroyId.split(",");
		int k = 0;
		if (destroyIds.length > 0) {
			ToaArchiveDestroy obj = new ToaArchiveDestroy();
			for (int i = 0; i < destroyIds.length; i++) {
				obj = manager.getDestrFolder(destroyId); // 获取销毁案卷对象
				if (obj != null) {
					if (obj.getDestroyAuditingType() != null
							&& obj.getDestroyAuditingType().equals("1")) {
						renderText("1");
						break;
					} else if (obj.getDestroyAuditingType() != null
							&& obj.getDestroyAuditingType().equals("2")) {
						renderText("2");
						break;
					}
					k++;
				} else {
					renderText("");
					break;
				}
			}
		}
		if (k == destroyIds.length) {
			renderText("0");
		}
		return null;
	}

	public void setModel(ToaArchiveDestroy model) {
		this.model = model;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// @Autowired
	// public void setUserService(IUserService userService) {
	// this.userService = userService;
	// }

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

	public Date getDestroyApplyTime() {
		return destroyApplyTime;
	}

	public void setDestroyApplyTime(Date destroyApplyTime) {
		this.destroyApplyTime = destroyApplyTime;
	}

	public Date getDestroyFolderDate() {
		return destroyFolderDate;
	}

	public void setDestroyFolderDate(Date destroyFolderDate) {
		this.destroyFolderDate = destroyFolderDate;
	}

	public String getDestroyFolderName() {
		return destroyFolderName;
	}

	public void setDestroyFolderName(String destroyFolderName) {
		this.destroyFolderName = destroyFolderName;
	}

	public String getDestroyFolderNo() {
		return destroyFolderNo;
	}

	public void setDestroyFolderNo(String destroyFolderNo) {
		this.destroyFolderNo = destroyFolderNo;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public String getIsPass() {
		return isPass;
	}

	public String getAuditingTime() {
		return auditingTime;
	}

	public void setAuditingTime(String auditingTime) {
		this.auditingTime = auditingTime;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setFolderManager(ArchiveFolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public String getFolderDate() {
		return folderDate;
	}

	public void setFolderDate(String folderDate) {
		this.folderDate = folderDate;
	}
}
