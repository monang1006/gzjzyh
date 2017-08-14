/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：年内文件管理action 
 */

package com.strongit.oa.archive.tempfile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.OaFormPdfService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.search.SearchManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
import com.sun.star.media.Manager;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "tempFile.action", type = ServletActionRedirectResult.class) })
public class TempFileAction extends
		AbstractBaseWorkflowAction<ToaArchiveTempfile> {
	private Page<ToaArchiveTempfile> page = new Page<ToaArchiveTempfile>(FlexTableTag.MAX_ROWS,
			true); // 年内文件分页列表
	private String tempfileId; // 年内文件主键值
	private String disLogo; // 标识
	private List<ToaArchiveTempfile> tempFileList; // 年内文件列表
	private ToaArchiveTempfile model = new ToaArchiveTempfile(); // 年内文件bo
	private TempFileManager tempmanager; // 年内文件manager
	private AnnexManager annexManager; // 年内文件附件manager
	private Map<String, String> statemap = new HashMap<String, String>(); // 存储销毁状态map
	private String fileIds; // 入卷操作时选择的年内文件id
	private String folderId; // 案卷id
	private String status; // 入卷状态
	private String forwardStr; // 控制跳转字符串
	private File[] file; // 文件对象
	private String file1;
	private String content; // 附件内容
	private String fileFileName; // 附件名称
	//拟办人
	private String tempfileAuthor1;
	private String tempfileNo; // 年内文件编号
	private String tempfileTitle; // 年内文件提名
	private String tempfilePage; // 文件页号
	private String tempfileDepartmentName;
	private TempPrivilManager tempprivilmanager;// 权限
	private IUserService userservice;// 用户接口
	private String treeType;
	private String treeValue;
	private List<String> yearList;
	private String year;// 文件年份
	private List<String> monthList;
	private String month;// 文件发文月份
	private List<TempfileTree> treeList;// 年份显示树
	private String searchType;// 是否是档案搜索标示
	private String tfileAppedId;// 附件ID
	private String delAttachIds;
	private ArchiveFolderManager folderManager;
	private String workflow;// 流程ID
	protected String formData;// 电子表单数据：XML格式
	@Autowired
	private SendDocManager theSendDocManager;
	private String appendFormId;// 电子表单ID
	private String tempfileAwardsOrgLevel;// 查看文件时存放颁奖单位的名字

	private String archiveSortId; // 案卷类目Id，在查看末归案的案卷时，重新编辑案卷中的文件，返回时使用
	private String moduletype;// 案卷模块标识，在查看末归案的案卷时，重新编辑案卷中的文件，返回时使用

	private String archiveSearch;// 档案查询时，传参数为“true" 不要做用户权限控制。

	private String IsEditTempFile;// 判断档案中心文件是否修改，如果修改，则更新当前文件所属附件的全文索引

	private String depLogo; // 标识是否是处领导用户

	private String instanceId; // 该归档文件的流程实例ID

	private String parentInstanceId; // 该归档文件的父流程实例ID
	
	private String personDemo;      //判断查看原表单按钮是否出现
	private String year1; 
	private String month1; 
	private String orgId;
	private String tempfileDeadline;
	private String fileFolder;
	private String fileTitle;
	private String fileNo;
	private String disLogo1;
	private String groupType;
	/** 字典Service接口 */
	private IDictService dictservice;

	/** 保管期限字典列表 */
	private List limitdictList;

	/** 颁奖单位 */
	private List orgdictList;

	private SearchManager searchManager;// 全文检索
	
	
	// 初始化json数组
	private String jsonArr;

	
	public String getJsonArr() {
		return jsonArr;
	}
	
	@Autowired
	private OaFormPdfService srv;

	@Autowired
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

	public String getAppendFormId() {
		return appendFormId;
	}

	public void setAppendFormId(String appendFormId) {
		this.appendFormId = appendFormId;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getDelAttachIds() {
		return delAttachIds;
	}

	public void setDelAttachIds(String delAttachIds) {
		this.delAttachIds = delAttachIds;
	}

	public String getTfileAppedId() {
		return tfileAppedId;
	}

	public void setTfileAppedId(String tfileAppedId) {
		this.tfileAppedId = tfileAppedId;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public List<String> getYearList() {
		return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<TempfileTree> getTreeList() {
		return treeList;
	}

	public void setTreeList(List<TempfileTree> treeList) {
		this.treeList = treeList;
	}

	public IUserService getUserservice() {
		return userservice;
	}

	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	@Autowired
	public void setTempprivilmanager(TempPrivilManager tempprivilmanager) {
		this.tempprivilmanager = tempprivilmanager;
	}

	public Page<ToaArchiveTempfile> getPage() {
		return page;
	}

	/**
	 * @roseuid 493F83C200FA
	 */
	public TempFileAction() {
		statemap.put("0", "待审核");
		statemap.put("1", "审核中");
		statemap.put("2", "已审核");

	}

	/***
	 * 获取当前用户名
	 * 
	 * @return
	 */
	public String getUserid() {
		String userid = userservice.getCurrentUser().getUserId();
		return userid;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param aId
	 *            the new value of the id property
	 */
	public void setTempfileId(String tempfileId) {
		this.tempfileId = tempfileId;
	}

	/**
	 * Access method for the tempFileList property.
	 * 
	 * @return the current value of the tempFileList property
	 */
	public java.util.List getTempFileList() {
		return tempFileList;
	}

	public ToaArchiveTempfile getModel() {
		return model;
	}

	public void tempfileTypeValue() {
		if ("1".equals(treeType)) {
			model.setTempfileDocType(treeValue);
		} else if ("2".equals(treeType)) {
			ToaArchiveFolder folder = new ToaArchiveFolder();
			folder.setFolderId(treeValue);
			model.setToaArchiveFolder(folder);
		} else if ("3".equals(treeType)) {

		} else if ("4".equals(treeType)) {
			String[] str = treeValue.split(",");
			if (str != null && str.length > 0) {
				model.setTempfileYear(str[0]);
				if (str.length > 1) {
					model.setTempfileMonth(str[1]);
				}
			}
		}
	}

	/**
	 * 查询列表
	 */
	@Override
	public String list() throws Exception {
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/archive/tempfile/tempFile.action");
		if (status == null) {// 为空是档案中心文件列表
			status = "0";
		}
		if (disLogo != null && disLogo.equals("search")) {// 是否根据条件搜索文件
			model.setTempfileNo(tempfileNo);
			if (tempfilePage != null && !"".equals(tempfilePage)
					&& !"null".equals(tempfilePage))
				model.setTempfilePage(new Long(tempfilePage));
			model.setTempfileTitle(tempfileTitle);
			model.setTempfileDepartmentName(tempfileDepartmentName);
			this.tempfileTypeValue();
			HttpSession session=getRequest().getSession();
			session.setAttribute("tempfileAuthor1", model.getTempfileAuthor());
			session.setAttribute("tempfilePieceNo", model.getTempfilePieceNo());
			session.setAttribute("tempfileNo", model.getTempfileNo());
			session.setAttribute("tempfileTitle", model.getTempfileTitle());
			session.setAttribute("tempfileDate",model.getTempfileDate());
			session.setAttribute("tempfileDepartmentName", model.getTempfileDepartmentName());
			session.setAttribute("folderNo", model.getToaArchiveFolder().getFolderNo());
			page = tempmanager
					.search(page, model, status, depLogo,"", new OALogInfo(
							"'search'方法是年内文件管理模块，根据查询条件查询年内文件，返回'page',"));
		} else {
		    if(!"True".equals(IsEditTempFile)){
			   page = tempmanager
					.getPageTempFile(
							page,
							status,
							treeType,
							treeValue,
							depLogo,
							new OALogInfo(
									"'getPageTempFile'方法是年内文件管理模块，‘list’初始化获取分页的分页的年内文件列表"));
			}else{
			    ToaArchiveFolder toaArchiveFolder=new  ToaArchiveFolder();
			    model.setTempfileAuthor((String)super.getSession().getAttribute("tempfileAuthor1"));
			    model.setToaArchiveFolder(toaArchiveFolder);
			    model.setTempfileDate((Date)super.getSession().getAttribute("tempfileDate"));
			    model.setTempfileDepartmentName((String)super.getSession().getAttribute("tempfileDepartmentName"));
			    model.setTempfileNo((String)super.getSession().getAttribute("tempfileNo"));
			    model.setTempfilePieceNo((String)super.getSession().getAttribute("tempfilePieceNo"));
			    model.setTempfileTitle((String)super.getSession().getAttribute("tempfileTitle"));
			    toaArchiveFolder.setFolderNo((String)super.getSession().getAttribute("folderNo"));
			    page = tempmanager
                .search(page, model, status, depLogo,"", new OALogInfo(
                        "'search'方法是年内文件管理模块，根据查询条件查询年内文件，返回'page',"));
			}
		}
		List<ToaArchiveTempfile> list = page.getResult();
		if (list != null) {
			// 获取附件个数
			getappendsize(list);
		}
		return SUCCESS;
	}

	/**
	 * 格式化时间
	 */
	public String getDateTime(Date time, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		String date = format.format(time);
		return date;
	}

	/*
	 * 编辑带有电子表单的文件 Description:
	 * 
	 * @author 胡丽丽
	 * 
	 * @date Apr 27, 2010 11:08:17 AM param:
	 */
	public String exitefrom() {
		try {
			ToaArchiveTfileAppend tfileappend = annexManager.getFileAnnex(
					tfileAppedId, new OALogInfo(
							"编辑带有电子表单的年内中心文件，根据附件所属文件的文件ID获取附件对象"));
			tfileappend.setTempappendContent(formData.getBytes("utf-8"));
			annexManager.saveAppend(tfileappend, new OALogInfo(
					"编辑带有电子表单的年内中心文件，对修改的附件保存"));

			return renderHtml("succ");
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 保存文件 (non-Javadoc)
	 * 
	 * @see com.strongit.oa.common.AbstractBaseWorkflowAction#save()
	 */
	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel();");
		String msg = "保存失败！";
		if (model.getTempfileId() != null && "".equals(model.getTempfileId()))// 判断文件ID是否为空字符串
		{

			model.setTempfileId(null);
		} else {
			ToaArchiveTempfile toaArchiveTempfile = model; // 如果是在修改档案文件的时候，在保存前选择删除所有的索引
			Set toaArchiveTfileAppends = toaArchiveTempfile
					.getToaArchiveTfileAppends();
			if (toaArchiveTfileAppends != null
					&& toaArchiveTfileAppends.size() > 0) {
				for (Iterator<ToaArchiveTfileAppend> iterator = toaArchiveTfileAppends
						.iterator(); iterator.hasNext();) {
					ToaArchiveTfileAppend annex = iterator.next();
					searchManager.delIndex(annex.getTempappendId()); // 删除附件索引
				}
			} else {
				searchManager.delIndex(model.getTempfileId()); // 无附件时，删除档案文件索引
			}
		}
		if (forwardStr != null && forwardStr.equals("addAtFolder")) { // 案卷归档和案卷管理模块中的对文件的增改操作
			if (folderId != null && !folderId.equals("")
					&& !folderId.equals("null")) {
				ToaArchiveFolder toaArchiveFolder = tempmanager.getFolderById(
						folderId, new OALogInfo("根据案卷id获取案卷对象"));
				model.setToaArchiveFolder(toaArchiveFolder);
			}
		}
		if (model.getToaArchiveFolder() != null) {// 判断案卷是否为空
			if (model.getToaArchiveFolder().getFolderId() == null
					|| "".equals(model.getToaArchiveFolder().getFolderId())) {
				model.setToaArchiveFolder(null);
			}
		}

		User user = userservice.getCurrentUser();// 获取当前用户
		TUumsBaseOrg org = userservice.getSupOrgByUserIdByHa(user.getUserId()); // 获取当前用户所在机构信息
		if (org != null) { // 判断机构是否为空
			model.setTempfileOrgCode(org.getSupOrgCode()); // 添加机构CODE
			model.setTempfileOrgId(org.getOrgId()); // 添加机构ID
		}

		if (model.getTempfileDate() != null) {
			Date time = model.getTempfileDate();
			String year = getDateTime(time, "yyyy");
			String month = getDateTime(time, "MM");
			model.setTempfileYear(year);
			model.setTempfileMonth(month);
		} else {
			Date time = new Date();
			String year = getDateTime(time, "yyyy");
			String month = getDateTime(time, "MM");
			model.setTempfileYear(year);
			model.setTempfileMonth(month);
		}

		// model.setTempfileDocType("0");//临时文件
		tempmanager.saveTempfile(model, new OALogInfo("保存未归档的年内文件"));// 保存文件
		if (delAttachIds != null && !"".equals(delAttachIds)) {// 在编辑时，对逻辑删除的不显示附件，进行删除
			annexManager.deleteAnnexByID(delAttachIds);
		}
		ToaArchiveTfileAppend tfileAppend = null;
		// if(tempfileId!=null&&!tempfileId.equals("")){
		// tfileAppend=tempmanager.getAnnex(tempfileId, new
		// OALogInfo("根据资料中心文件ID获取附件"));
		// if(tfileAppend.getTempappendId()==null){
		// searchManager.delIndex(tempfileId);
		// }
		// }
		if (file != null && file.length > 0) {
			annexManager.saveAnnex(file, model, fileFileName, new OALogInfo(
					"保存未归档的年内文件所属附件")); // 保存文件附件
		}
		// else
		// if(tempfileId.equals("")||(tfileAppend!=null&&tfileAppend.getTempappendId()==null)){
		// searchManager.saveIndex(model);//如果档案不存在附件，对当前档案文件添加索引
		// }

		// 给自己付权限
		// if(model.getTempfileId()!=null&&!"".equals(model.getTempfileId()))//判断文件是否添加成功
		// {
		// if(tempfileId==null||"".equals(tempfileId))//判断是否是添加文件
		// {
		// ToaArchiveTempfilePrivil privil=new ToaArchiveTempfilePrivil();
		// privil.setTempfileprivilUser(getUserid());
		// privil.setToaArchiveTempfile(model);
		// tempprivilmanager.save(privil,new OALogInfo("给自己付权"));//保存权限
		// }
		// }

		List<ToaArchiveTfileAppend> appendList = annexManager
				.getListAnnex(model.getTempfileId());// 通过文件Id 获取到该文件下的所属附件列表
		if (appendList != null && appendList.size() > 0) { // 判断当前档案是否丰厚附件
			for (int i = 0; i < appendList.size(); i++) {
				ToaArchiveTfileAppend obj = appendList.get(i);
				File file = byteArray2File(obj.getTempappendContent());
				FileInputStream indexfis = new FileInputStream(file);
				annexManager.saveSearchAppend(obj, indexfis, new OALogInfo(
						"修改年内文件时，所属附件更新索引搜索"));
				file.delete();
			}
		} else {
			searchManager.saveIndex(model); // 如果档案不存在附件，对当前档案文件添加索引
		}

		msg = "保存成功!";
		addActionMessage(msg);
		StringBuffer returnhtml = new StringBuffer(
				"<script> var scriptroot = '").append(
				getRequest().getContextPath()).append("'</script>").append(
				"<SCRIPT src='").append(getRequest().getContextPath()).append(
				"/common/js/commontab/workservice.js'>").append("</SCRIPT>")
				.append("<SCRIPT src='").append(getRequest().getContextPath())
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>").append("<script>");

		if (forwardStr != null && forwardStr.equals("addAtFolder"))
			returnhtml
					.append(" location='")
					.append(getRequest().getContextPath())
					.append(
							"/archive/archivefolder/archiveFolder!input.action?depLogo=" + depLogo +"&forward=viewFile&folderId=")
					.append(folderId).append("&archiveSortId=").append(
							archiveSortId).append("&moduletype=").append(
							moduletype).append("';</script>");
		else
		    //returnhtml.append("var tempFileAuth='<%session.getAttribute(tempFileAuthor1)%>';");
			returnhtml.append(" location='").append(
					getRequest().getContextPath()).append(
					"/archive/tempfile/tempFile.action?depLogo=" + depLogo +"&treeType=" + treeType
							+ "&treeValue=" + treeValue+"&IsEditTempFile="+IsEditTempFile)
			// .append("/archive/tempfile/tempFile.action")
					.append("';</script>");
		return renderHtml(returnhtml.toString());
	}
	
	public String topdf() throws Exception
	{
		OutputStream ou = null;
		try{
			byte[]  bt = srv.buildPdf("T_OARECVDOC","PDFIMAGE","OARECVDOCID","ec17f5f30e3c4a1096dc1439592ef46d");
			ou = new FileOutputStream("d:/aa.pdf");
			IOUtils.write(bt, ou);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ou.close();
		}
		return null;
	}

	/**
	 * 将一个字节数组对象转换成一个文件对象
	 * 
	 * @author:邓志城
	 * @date:2009-7-17 下午05:45:38
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public File byteArray2File(byte[] input) throws Exception {
		File file = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			// C:\DOCUME~1\ADMINI~1\LOCALS~1\Temp\test52933.temp
			file = File.createTempFile("test", ".tmp");// 创建临时文件
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			if(input != null){
				bos.write(input);
			}
			// file.deleteOnExit();
		} catch (Exception e) {
			throw new SystemException("字节数组转成文件异常：" + e);
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return file;
	}

	/**
	 * 
	 * @author zhengzb
	 * @desc 全文检索中，通过档案中心文件所属附件ID，来查看档案中心文件。 2010-5-10 下午04:27:37
	 * @param appendId
	 *            附件ID
	 * @return
	 */
	public String searchViewAppend() throws Exception {

		StringBuffer returnhtml = new StringBuffer();

		if (tfileAppedId != null && !tfileAppedId.equals("")) {
			ToaArchiveTfileAppend annex = annexManager.getFileAnnex(
					tfileAppedId, new OALogInfo("根据附件id获取附件"));
			if (annex != null) {
				String tempfileId = annex.getToaArchiveTempfile()
						.getTempfileId();
				String tempfileTitle = annex.getToaArchiveTempfile()
						.getTempfileTitle();
				returnhtml.append(tempfileId + "," + tempfileTitle);
				return renderHtml(returnhtml.toString());
			} else {
				return renderHtml("当前附件不存在");
			}
		} else {
			return renderHtml("当前附件不存在");
		}
	}

	/**
	 * 全文检索中，通过档案中心文件ID，来查看档案中心文件。
	 * 
	 * @author zhengzb
	 * @desc 2010-6-24 下午06:23:25
	 * @return
	 * @throws Exception
	 */
	public String searchViewFile() throws Exception {
		StringBuffer returnhtml = new StringBuffer();
		if (tempfileId != null && !tempfileId.equals("")) {
			int result = tempmanager.getIsTempfileBytempfileId(tempfileId);
			model = tempmanager.getTempFile(tempfileId, new OALogInfo(
					"根据资料中心文件ID获取文件"));
			if (result > 0) {
				returnhtml.append(tempfileId + "," + model.getTempfileTitle());
			} else {
				returnhtml.append("当前附件不存在");
			}

		} else {
			returnhtml.append("当前附件不存在");
		}
		return renderHtml(returnhtml.toString());
	}

	@Override
	public String delete() throws Exception {
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/archive/tempfile/tempFile.action");
		String[] tempfileIds = tempfileId.split(",");
		for (String a : tempfileIds)// 批量删除
		{
			tempmanager.deleteTempfile(a, new OALogInfo("删除未归档文件"));

		}
		StringBuffer returnhtml = new StringBuffer(
				"<script> var scriptroot = '").append(
				getRequest().getContextPath()).append("'</script>").append(
				"<SCRIPT src='").append(getRequest().getContextPath()).append(
				"/common/js/commontab/workservice.js'>").append("</SCRIPT>")
				.append("<SCRIPT src='").append(getRequest().getContextPath())
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>").append("<script>");

		if (forwardStr != null && forwardStr.equals("deleteAtFolder"))// 判断是否是档案管理或档案归档里做的删除操作
			returnhtml
					.append(" location='")
					.append(getRequest().getContextPath())
					.append(
							"/fileNameRedirectAction.action?toPage=/archive/tempfile/tempFile-temps.jsp?depLogo=" + depLogo +"&folderId="
									+ folderId).append("&archiveSortId=")
					.append(archiveSortId).append("&moduletype=").append(
							moduletype).append("';</script>");
		else
			returnhtml.append(" location='").append(
					getRequest().getContextPath()).append(
					"/archive/tempfile/tempFile.action?depLogo=" + depLogo +"&treeType=" + treeType
							+ "&treeValue=" + treeValue).append("';</script>");
		return renderHtml(returnhtml.toString());
	}

	@Override
	protected void prepareModel() throws Exception {
		if (tempfileId != null && !"".equals(tempfileId)
				&& !"null".equals(tempfileId)) {// ID是否为空
			if (model != null) {
				model = tempmanager.getTempFile(tempfileId);
				String orgId = model.getTempfileDepartment();
				if (orgId != null && !"".equals(orgId) && !"null".equals(orgId)) {
					tempfileDepartmentName = tempmanager.getOrgNameById(orgId);
				}
			}
		} else {
			String orgId = userservice.getCurrentUser().getOrgId();
			tempfileDepartmentName = tempmanager.getOrgNameById(orgId);
			model = new ToaArchiveTempfile();
			model.setTempfileDepartment(orgId);
			model.setTempfileDepartmentName(tempfileDepartmentName);
			model.setTempfileDate(new Date());
		}
		if (model != null) {// 如果model不为空，查询附件
			Set annexset = model.getToaArchiveTfileAppends();
			ToaArchiveTfileAppend annex = null;
			if (annexset != null && annexset.size() > 0) {
				for (Iterator ite = annexset.iterator(); ite.hasNext();) {
					annex = (ToaArchiveTfileAppend) ite.next();
					break;
				}
			}
			if (annex != null) {// 附件名称
				fileFileName = annex.getTempappendName();
			}
		}
	}

	@Override
	public String input() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		
		if (model == null && forwardStr != null && forwardStr.equals("view")) {
			return renderHtml("<script>alert('当前所查看的文件不存在，可能已归档 ！');top.perspective_content.actions_container.personal_properties_toolbar.closeWork(); </script>");
		} else if (model != null) {
			if (model.getTempfileId() != null
					&& !"".equals(model.getTempfileId())) {// 判断ID是否为空
				Iterator<ToaArchiveTfileAppend> appendlist = model.getToaArchiveTfileAppends().iterator();
				fileFileName = "[]";
				String type = model.getTempfileDocType();
				if (type != null&&!"0".equals(type)) { // 判断文件类型是否是收发文
					workflow = model.getWorkflow(); // 流程ID

					//add by luosy at 20170709  
					if("gzgthistoryfile".equals(workflow)){//定制赣州历史文件查看内容
						return  gzhistoryfileView();
					}
					
					
					while (appendlist.hasNext()) {
						ToaArchiveTfileAppend app = appendlist.next();
						if(app.getTempappendName()!=null&&!"".equals(app.getTempappendName())){
							String tempname = app.getTempappendName();
							if(tempname.equals(model.getTempfileTitle()+".doc")){
								appendlist.remove();
							}
						}else{
							appendlist.remove();
						}
						if (app.getTempappendContent() != null
								&& !app.getTempappendContent().equals("")) {
							byte[] bufData = app.getTempappendContent();
							//fileFileName = new String(bufData, "utf-8"); // 电子表单信息
							fileFileName += "<div id="
								+ app.getTempappendId()
								+ " style=\"display: \"><a href=\"javascript:delAttach('"
								+ app.getTempappendId() + "')\">[删除]</a>"
								+ "<a href=\"javascript:download('"
								+ app.getTempappendId() + "')\">"       
								+ app.getTempappendName() + "</a>&nbsp;</div>";

						} else {
							fileFileName = "附件内容字段为空";
						}
						tfileAppedId = app.getTempappendId(); // 附件ID
					}
					instanceId = workflow.split(";")[0];
					if(instanceId != null && !instanceId.equals("")){
						ContextInstance cxt = adapterBaseWorkflowManager.getWorkflowService().getContextInstance(instanceId);
						if(cxt!=null&&!cxt.equals("")){
							Object tempObject = cxt.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);//获取父流程实例id
							if(tempObject != null && !tempObject.toString().equals("")){
								parentInstanceId = tempObject.toString();	
								ContextInstance parentCxt = adapterBaseWorkflowManager.getWorkflowService().getContextInstance(parentInstanceId);//获取父流程上下文
								//personDemo = (String)parentCxt.getVariable("@{personDemo}");
							}
						}
						JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
						if(jsonArray != null && !jsonArray.isEmpty()){
							personDemo = jsonArray.toString();
							//parentInstanceId = "exists";
						}else{
							ProcessInstance pi = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId);
							String demo=null;
							if(pi!=null){
							  demo = (String) pi.getContextInstance().getVariable("@{personDemo}");
							}
							String parentinstanceId="";
							if(!"".equals(demo)&&null!=demo){
								String[]  demos = demo.split(";");
								if(demos.length>4){
									parentinstanceId = demos[4];
									parentinstanceId = parentinstanceId.split("@")[0];
									
									jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
									personDemo = jsonArray.toString();
									parentInstanceId = parentinstanceId;
								}					
							}
						}
						
					}
					
					List<Object[]> listOb= theSendDocManager.getFormIdAndBusinessIdByProcessInstanceId(instanceId);
					if(listOb!=null&&listOb.size()>0){
						Object[] objs = theSendDocManager.getFormIdAndBusinessIdByProcessInstanceId(instanceId).get(0); 
						String[] info = new String[]{objs[1].toString(),objs[0].toString()};
						bussinessId = info[0];
						formId = info[1];
						if(!"0".equals(bussinessId) && bussinessId.indexOf(";")!=-1){
							String[] args = bussinessId.split(";");
							tableName = args[0];
							pkFieldName = args[1];
							pkFieldValue = args[2];				
						}
					}
					
					
					appendFormId = model.getTempfileFormId(); // 电子表单ID
					if (forwardStr != null && forwardStr.equals("view")) { // 查看未归档文件
						return "vieweform";
					} else {
						return "exiteform";
					}
				}

				while (appendlist.hasNext()) {
					ToaArchiveTfileAppend app = appendlist.next();
					fileFileName += "<div id="
							+ app.getTempappendId()
							+ " style=\"display: \"><a href=\"javascript:delAttach('"
							+ app.getTempappendId() + "')\">[删除]</a>"
							+ "<a href=\"javascript:download('"
							+ app.getTempappendId() + "')\">"
							+ app.getTempappendName() + "</a>&nbsp;</div>";

				}
			}
			limitdictList = dictservice.getItemsByValue("BGQX");// 保管期限
			orgdictList = dictservice.getItemsByDictValue("BJDW");
			if (model.getTempfileId() == null
					|| "".equals(model.getTempfileId())) {
				return INPUT;
			} else {

				if (forwardStr != null && forwardStr.equals("view")) { // 判断是否查看
					if (model.getTempfileAwardsOrgId() != null
							&& !model.getTempfileAwardsOrgId().equals("")) {
						if (model.getTempfileAwardsOrg() != null
								&& !model.getTempfileAwardsOrg().equals("")) {

							tempfileAwardsOrgLevel = dictservice
									.getDictItemName(model
											.getTempfileAwardsOrgId())
									+ "-" + model.getTempfileAwardsOrg();// 获奖单位和获奖级数
						} else {
							tempfileAwardsOrgLevel = dictservice
									.getDictItemName(model
											.getTempfileAwardsOrgId());// 获奖单位
						}
					} else {
						tempfileAwardsOrgLevel = model.getTempfileAwardsOrg();
					}
					return "view";
				} else
					return INPUT;
			}
		} else {
			return renderHtml("<script>alert('该文件已经不存在！'); top.perspective_content.actions_container.personal_properties_toolbar.closeWork();</script>");
		}
	}

	public String gzhistoryfileView() throws Exception{
		JSONArray array = new JSONArray();
//		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("id", "tttttttttttttttttttttttttttttestID");
//		array.add(jsonObj);
		array = tempmanager.getHistoryDataInfoAndFiles(model.getTempfileId());
		jsonArr = array.toString();
		System.out.println(jsonArr);
		return "historyfileview";
	}
	
	/**
	 * 判断用户是否有权限访问
	 * 
	 * @return
	 * @throws Exception
	 */
	public String isprivil() throws Exception {
		String user = getUserid();// 获取当前用户ID
		model = tempmanager.getTempFile(tempfileId);
		// if(getIsprivlAdmin(user)){//是否是管理员
		// return renderHtml(model.getTempfileTitle());
		// }else
		if (tempprivilmanager.getPrivilByUsreTemp(user, tempfileId).size() > 0)// 判断是否有权限查看
		{
			return renderHtml(model.getTempfileTitle());
		} else {
			return renderHtml("1");
		}
		// return renderHtml(model.getTempfileTitle());
	}

	/**
	 * 判断用户是否有权限访问
	 * 
	 * @return
	 * @throws Exception
	 */
	public String isView() throws Exception {
		model = tempmanager.getTempFile(tempfileId);
		if (model.getTempfileTitle() != null)// 判断是否有权限查看
		{
			return renderHtml(model.getTempfileTitle());
		} else {
			return renderHtml("1");
		}
	}

	/**
	 * @author：hull
	 * @time：2010-01-18
	 * @desc：对选择的文件入卷
	 * @return String
	 */
	public String portalFile() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel();");
		String[] fileId = fileIds.split(","); // 入卷的文件id
		ToaArchiveFolder folder = folderManager.getFolder(folderId);
		String msg = "";
		if ("1".equals(folder.getFolderAuditing())) {
			msg = tempmanager.portalFileAuthing(folderId, fileId);
		} else {
			msg = tempmanager.portalFile(folderId, fileId);// 入卷
		}
		addActionMessage(msg);
		StringBuffer returnhtml = new StringBuffer(
				"<script> var scriptroot = '").append(
				getRequest().getContextPath()).append("'</script>").append(
				"<SCRIPT src='").append(getRequest().getContextPath()).append(
				"/common/js/commontab/workservice.js'>").append("</SCRIPT>")
				.append("<SCRIPT src='").append(getRequest().getContextPath())
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>").append("<script>").append(" location='")
				.append(getRequest().getContextPath()).append(
						"/archive/tempfile/tempFile.action?depLogo=" + depLogo +"&treeType="
								+ treeType + "&treeValue=" + treeValue)
				// .append("/archive/tempfile/tempFile.action")
				.append("';</script>");
		return renderHtml(returnhtml.toString());
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-3下午03:45:46
	 * @desc：获取案卷的状态
	 * @param
	 * @return 案卷状态
	 */
	public String getFolderStatus() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		if (tempfileId != null && !"".equals(tempfileId)
				&& !"null".equals(tempfileId)) {
			folderId = tempmanager.getTempFile(tempfileId)
					.getToaArchiveFolder().getFolderId();
		}
		String data = tempmanager.getFolderStatus(folderId);// 获取该案卷的状态
		renderText(data);
		return null;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午10:44:29
	 * @desc: 下载文件
	 * @param
	 * @return
	 */
	public String download() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		HttpServletResponse response = super.getResponse();

		//String user = getUserid();// 获取当前用户ID

		//if (tempprivilmanager.getPrivilByUsreTemp(user, model.getTempfileId())
		//		.size() > 0)// 判断是否有权限查看
		//{

			ToaArchiveTfileAppend file = null;
			if (tfileAppedId != null && !"".equals(tfileAppedId)) {
				file = tempmanager.getFileAnnex(tfileAppedId);
			} else {
				file = tempmanager.getAnnex(tempfileId);
			}
			response.reset();
			response.setContentType("application/x-download"); // windows
			OutputStream output = null;
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String((file.getTempappendName()+".pdf").getBytes("gb2312"),
							"iso8859-1"));
			output = response.getOutputStream();
			output.write(file.getTempappendContent());
			output.flush();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = null;
			}
			return null;

		//} else {
			//return renderHtml("<script>alert('你无权访问！');window.history.go(-1); </script>");
		//}

	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午01:50:25
	 * @desc: 判断附件类型，并读取附件名称
	 * @param
	 * @return String
	 */
	public String readAnnex() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		String forward = "annex";
		try {
			if (tempfileId != null && !"".equals(tempfileId)
					&& !"null".equals(tempfileId)) {
				model = tempmanager.getTempFile(tempfileId); // 获取年内文件对象
				ToaArchiveTfileAppend obj = null; // 获取附件对象
				if (tfileAppedId != null && !"".equals(tfileAppedId)) {
					obj = tempmanager.getFileAnnex(tfileAppedId);
				} else {
					obj = tempmanager.getAnnex(tempfileId);
				}
				if (obj != null) {
					fileFileName = obj.getTempappendName(); // 获取附件名称
					fileFileName = fileFileName.substring(0, fileFileName
							.lastIndexOf(".")); // 附件名称去掉后缀

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return forward;
	}

	/**
	 * @author: qibh
	 * @created: 2012-8-3 上午09:32:46
	 * @version :5.0
	 */
	public String readAnnexindex() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		String forward = "annex";
		try {
			ToaArchiveTfileAppend obj = null; // 获取附件对象
			if (tfileAppedId != null && !"".equals(tfileAppedId)) {
				obj = tempmanager.getFileAnnex(tfileAppedId);
			} else {
				obj = tempmanager.getAnnex(tempfileId);
			}
			if (obj != null) {
				tempfileId = obj.getToaArchiveTempfile().getTempfileId();
				fileFileName = obj.getTempappendName(); // 获取附件名称
				String ext = fileFileName
				.substring(fileFileName.lastIndexOf(".") + 1);// 获取附件类型
				renderText(tempfileId + "," + ext);
			}
			else{
				renderText("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * @author：pengxq
	 * @time：2009-1-5下午01:53:55
	 * @desc: 读取附件内容
	 * @param
	 * @return String
	 */
	public String openAnnex() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		try {
			HttpServletResponse response = this.getResponse();
			tempmanager.setContentToHttpResponse(response, tempfileId,
					tfileAppedId, new OALogInfo("获取附件对象，并读取附件内容")); // 获取附件对象，并读取附件内容
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-7下午05:28:12
	 * @desc: 文件编号是否有重复的
	 * @param
	 * @return
	 */
	public String isExist() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		boolean flag = true;
		try {
			if(!tempfileNo.equals("")){
				flag = tempmanager.isExist(tempfileNo, new OALogInfo("判断编号是否重复"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag == true)
			renderText("0");
		else
			renderText("1");
		return null;

	}

	/**
	 * @author：pengxq
	 * @time：2009-1-8下午02:02:40
	 * @desc: //获取附件类型
	 * @param
	 * @return
	 */
	public String getTempFileExt() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		if (tempfileId != null && !"".equals(tempfileId)
				&& !"null".equals(tempfileId)) {
			model = tempmanager.getTempFile(tempfileId); // 获取年内文件对象
			ToaArchiveTfileAppend obj = null; // 获取附件对象
			if (tfileAppedId != null && !"".equals(tfileAppedId)) {
				obj = tempmanager.getFileAnnex(tfileAppedId);
			} else {
				obj = tempmanager.getAnnex(tempfileId);
			}
			fileFileName = obj.getTempappendName(); // 获取附件名称
			String ext = fileFileName
					.substring(fileFileName.lastIndexOf(".") + 1);// 获取附件类型
			renderText(ext);
		} else {
		    if(file1!=null&&!"".equals(file1)){
    		    String ext1 = file1
                .substring(file1.lastIndexOf(".") + 1);// 获取附件类型
                renderText(ext1);
            }else{
                renderText("doc");
            }
		}
		return null;
	}

	/**
	 * 显示树
	 * 
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception {
		treeList = new ArrayList<TempfileTree>();
		if ("1".equals(treeType)) { // 是否根据文件类型展示树节点
			List<String> typeList =new ArrayList<String>();
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				typeList = tempmanager.getTempfileTypeDepartment(depLogo);
			}else{
				typeList = tempmanager.getTempfileType();
			}
			for (String type : typeList) {
				if (type != null&&!"".equals(type)) {
					TempfileTree tree = new TempfileTree();
					tree.setTreeid(type);
					if ("LoadData".equals(type)) {
						tree.setTreeName("一般文件");
					}else if ("0".equals(type)) {
						tree.setTreeName("档案中心文件");
					}else{
						List tpNameList=tempmanager.getPtNameByPtId(type);
						if(tpNameList!=null&&tpNameList.size()>0){
							for(int i=0;i<tpNameList.size();i++){
									tree.setTreeName((String) tpNameList.get(i));
							}
						}
					}
					tree.setTreetype("1");
					treeList.add(tree);
				}
			}
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				return "depTypetree";
			} else {

				return "typetree";
			}
		} else if ("2".equals(treeType)) {// 是否根据所属案卷展示树节点
			List<Object> typeList = new ArrayList<Object>();
			int count = 0;
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				typeList = tempmanager.getTempfileFolderDepartment(depLogo);
				count = tempmanager.gettempfileCountByTypeDepartment(treeType,depLogo);
			}else{
				typeList = tempmanager.getTempfileFolder();
				count = tempmanager.gettempfileCountByType(treeType);
			}
			for (Object type : typeList) {
				if (type != null) {
					Object[] obj = (Object[]) type;
					if (obj[1] != null && obj[1] != "" && obj[1] != "null") {
						TempfileTree tree = new TempfileTree();
						tree.setTreeid(obj[0].toString());
						tree.setTreetype("2");
						tree.setTreeName(obj[1].toString());
						treeList.add(tree);
					}
				}
			}
			if (count > 0) {
				TempfileTree tree1 = new TempfileTree();
				tree1.setTreeid("0");
				tree1.setTreetype("2");
				tree1.setTreeName("未入卷文件");
				treeList.add(tree1);
			}
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				return "depFoldertree";
			} else {

				return "foldertree";
			}

		} else { // 根据年份展示树节点
			yearList = new ArrayList<String>();// 年份列表

			User user = userservice.getCurrentUser();
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				yearList = tempmanager.getTempfileYearDepartment(depLogo);
			}else{
				yearList = tempmanager.getTempfileYear();
			}
			Collections.sort(yearList);
			for (String ye : yearList) {
				if (ye != null) {
					TempfileTree tree = new TempfileTree();
					tree.setTreeid(ye);
					tree.setTreeName("年");
					tree.setTreetype("4");
					treeList.add(tree);
					if (depLogo != null && !depLogo.equals("")
							&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
						monthList = tempmanager.getTempfileMonthDepartment(ye,depLogo);
					}else{
						monthList = tempmanager.getTempfileMonth(ye);
					}
					Collections.sort(monthList);
					if (monthList != null) { // 月份列表
						for (String mon : monthList) {
							TempfileTree montree = new TempfileTree();
							montree.setTreeid(mon);
							montree.setParentId(ye);
							montree.setTreeName("月份");
							montree.setTreetype("4");
							treeList.add(montree);
						}
					}
				}

			}
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				return "depTree";
			} else {

				return "tree";
			}
		}
	}

	/**
	 * 年月份显示树
	 * 
	 * @return
	 * @throws Exception
	 */
	public String protecttree() throws Exception {
		yearList = new ArrayList<String>();
		treeList = new ArrayList<TempfileTree>();
		yearList = tempmanager.getTempfileYear();
		for (String ye : yearList) {
			TempfileTree tree = new TempfileTree();
			tree.setTreeid(ye);
			tree.setTreetype("年");
			treeList.add(tree);
			monthList = tempmanager.getTempfileMonth(ye);
			if (monthList != null) {
				for (String mon : monthList) {
					TempfileTree montree = new TempfileTree();
					montree.setTreeid(mon);
					montree.setParentId(ye);
					montree.setTreetype("月份");
					treeList.add(montree);
				}
			}

		}
		return "protecttree";
	}

	/**
	 * 获取文件附件个数
	 * 
	 * @param fileList
	 */
	public void getappendsize(List fileList) {
		if (fileList != null) {
			Map<String, Integer> map = annexManager.getCount();
			for (Object temp : fileList) {
				ToaArchiveTempfile tf = (ToaArchiveTempfile) temp;
				tf.setAppendsize(map.get(tf.getTempfileId()) == null ? 0 : map
						.get(tf.getTempfileId()));
				// tf.setAppendsize(tf.getToaArchiveTfileAppends().size());
			}
		}
	}

	public void setUpload(File[] file) {
		this.file = file;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public String getFileIds() {
		return fileIds;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getForwardStr() {
		return forwardStr;
	}

	public void setForwardStr(String forwardStr) {
		this.forwardStr = forwardStr;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	@Autowired
	public void setAnnexManager(AnnexManager annexManager) {
		this.annexManager = annexManager;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getTempfileId() {
		return tempfileId;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setTempfileNo(String tempfileNo) {
		this.tempfileNo = tempfileNo;
	}

	public Map<String, String> getStatemap() {
		return statemap;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setModel(ToaArchiveTempfile model) {
		this.model = model;
	}

	public String getTempfilePage() {
		return tempfilePage;
	}

	public void setTempfilePage(String tempfilePage) {
		this.tempfilePage = tempfilePage;
	}

	public String getTempfileTitle() {
		return tempfileTitle;
	}

	public void setTempfileTitle(String tempfileTitle) {
		this.tempfileTitle = tempfileTitle;
	}

	public String getTempfileDepartmentName() {
		return tempfileDepartmentName;
	}

	public void setTempfileDepartmentName(String tempfileDepartmentName) {
		this.tempfileDepartmentName = tempfileDepartmentName;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getTreeValue() {
		return treeValue;
	}

	public void setTreeValue(String treeValue) {
		this.treeValue = treeValue;
	}

	@Autowired
	public void setFolderManager(ArchiveFolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	public void setTempmanager(TempFileManager tempmanager) {
		this.tempmanager = tempmanager;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return this.tempmanager;
	}

	@Override
	protected String getDictType() {
		// TODO 自动生成方法存根
		return null;
	}

	@Autowired
	public void setDictservice(IDictService dictservice) {
		this.dictservice = dictservice;
	}

	public List getLimitdictList() {
		return limitdictList;
	}

	public List getOrgdictList() {
		return orgdictList;
	}

	public void setOrgdictList(List orgdictList) {
		this.orgdictList = orgdictList;
	}

	public String getTempfileAwardsOrgLevel() {
		return tempfileAwardsOrgLevel;
	}

	public void setTempfileAwardsOrgLevel(String tempfileAwardsOrgLevel) {
		this.tempfileAwardsOrgLevel = tempfileAwardsOrgLevel;
	}

	public String getArchiveSortId() {
		return archiveSortId;
	}

	public void setArchiveSortId(String archiveSortId) {
		this.archiveSortId = archiveSortId;
	}

	public String getModuletype() {
		return moduletype;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}

	public String getArchiveSearch() {
		return archiveSearch;
	}

	public void setArchiveSearch(String archiveSearch) {
		this.archiveSearch = archiveSearch;
	}

	public String getIsEditTempFile() {
		return IsEditTempFile;
	}

	public void setIsEditTempFile(String isEditTempFile) {
		this.IsEditTempFile = isEditTempFile;
	}

	public String getDepLogo() {
		return depLogo;
	}

	public void setDepLogo(String depLogo) {
		this.depLogo = depLogo;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getParentInstanceId() {
		return parentInstanceId;
	}

	public void setParentInstanceId(String parentInstanceId) {
		this.parentInstanceId = parentInstanceId;
	}
	
	public String getPersonDemo() {
		return personDemo;
	}

	public void setPersonDemo(String personDemo) {
		this.personDemo = personDemo;
	}

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public String getMonth1() {
        return month1;
    }

    public void setMonth1(String month1) {
        this.month1 = month1;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getFileFolder() {
        return fileFolder;
    }

    public void setFileFolder(String fileFolder) {
        this.fileFolder = fileFolder;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getDisLogo1() {
        return disLogo1;
    }

    public void setDisLogo1(String disLogo1) {
        this.disLogo1 = disLogo1;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getTempfileAuthor1() {
        return tempfileAuthor1;
    }

    public void setTempfileAuthor1(String tempfileAuthor1) {
        
          
                this.tempfileAuthor1 = tempfileAuthor1;
        
    }
}
