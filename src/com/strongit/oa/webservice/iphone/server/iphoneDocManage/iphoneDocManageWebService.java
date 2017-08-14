package com.strongit.oa.webservice.iphone.server.iphoneDocManage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import net.sf.json.JSONObject;

import org.apache.axis.utils.JavaUtils;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Encoder;



import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.ToaAgencyPrsnfldrFolder;
import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.ToaDepartmentPrsnfldrFolder;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaPrivatePrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.bo.ToaPrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrShare;
import com.strongit.oa.bo.ToaPublicPrsnfldrFolder;
import com.strongit.oa.bo.ToaPubliccontact;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.ipadoa.util.WorkflowForIpadService;
import com.strongit.oa.prsnfldr.agencyprsnfldr.AgencyPrsnfldrFolderManager;
import com.strongit.oa.prsnfldr.departmentprsnfldr.DepartmentPrsnfldrFolderManager;
import com.strongit.oa.prsnfldr.privateprsnfldr.PrsnfldrFolderManager;
import com.strongit.oa.prsnfldr.publicprsnfldr.PublicPrsnfldrFolderManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.publiccontact.PublicContactManage;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.Office2PDF;
import com.strongit.oa.work.WorkManager;
import com.strongit.uums.webservice.AuthenticationHandler;
import com.strongit.workflow.bo.TwfInfoProcessType;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.po.TaskInstanceBean;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * 供外部调用的webservice接口
 * 
 * @author Administrator
 * 
 */
public class iphoneDocManageWebService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	
	//个人文件柜
	private PrsnfldrFolderManager pffManager;
	//公共文件柜
	private PublicPrsnfldrFolderManager ppfManager;
	//部门文件柜
	private DepartmentPrsnfldrFolderManager dpfManager;
	//机构文件柜
	private AgencyPrsnfldrFolderManager apfManager;
	
	private IUserService userService;
	
	/**
	* @构造函数
	* @description 构造方法获取publicContactManage对象
	* @param logger
	* @param articlesManager
	*/
	public iphoneDocManageWebService() {
		pffManager = (PrsnfldrFolderManager)ServiceLocator.getService("prsnfldrFolderManager");
		ppfManager = (PublicPrsnfldrFolderManager)ServiceLocator.getService("publicPrsnfldrFolderManager");
		dpfManager = (DepartmentPrsnfldrFolderManager)ServiceLocator.getService("departmentPrsnfldrFolderManager");
		apfManager = (AgencyPrsnfldrFolderManager)ServiceLocator.getService("agencyPrsnfldrFolderManager");
		userService = (IUserService)ServiceLocator.getService("userService");
		logger.info("文件柜服务类初始化完毕.。。。");
	}
	
	public String sss(String tjxiha){
		String ret = "tttttttttttssssssssssssssssssssssjjjjjjjjjjjjjj";
		System.out.println(ret);
		return ret;
	}
	/**
	 * * 获取个人文件柜
	 * author  taoji
	 * @param sessionId
	 * @param userId
	 * @param folderName	支持查询
	 * @param pageSize	一页显示多少数据
	 * @param pageNo	显示第几页
	 * @param order		排序方式  desc  asc
	 * @param orderBy   1 根据创建时间排序  0 默认排序
	 * @return
	 * * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件夹id"/>
		<item type="String" value="文件夹名称"/>
		<item type="String" value="文件夹下文件夹和文件数量"/>
		<item type="String" value="文件夹创建时间"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-9 下午02:10:18
	 */
	public String getPersonFolderList(String sessionId, String userId,String folderName,
			String pageSize, String pageNo,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【个人文件柜】的模块  
			 */
			if(pffManager==null){
				throw new SystemException("个人文件柜模块不存在！");
			}
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			if("1".equals(orderBy)){
				page.setOrderBy("1");
				if(order == null || "".equals(order)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrder(order);
				}
			}
			page.setPageNo(Integer.parseInt(pageNo));
			page = pffManager.getDocFolderList(page,userId,folderName);
			if(page!=null&&page.getTotalCount()>0){
				List<ToaPrivatePrsnfldrFolder> list = page.getResult();
				for(ToaPrivatePrsnfldrFolder t : list){
					
					//文件夹id
					String[] foId = new String[2];
					foId[0] = "String";
					foId[1] = t.getFolderId();
					//文件夹name
					String[] fName = new String[2];
					fName[0] = "String";
					fName[1] = t.getFolderName();
					//文件夹下文件夹和文件数量
					Page pagess = new Page(Integer.parseInt(pageSize),true);
					String[] folderFileNum = new String[2];
					folderFileNum[0] = "String";
					folderFileNum[1] = String.valueOf(pffManager.getFolderFileNum(t.getFolderId())+
							pffManager.getCommonFolderListById(pagess,t.getFolderId(),"").getTotalCount());
					//创建文件夹时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String[] folderCreateDate = new String[2];
					folderCreateDate[0] = "String";
					folderCreateDate[1] = sdf.format(t.getFolderCreateDatetime());
					
					//是否共享
					String[] isShare = new String[2];
					isShare[0] = "String";
					String  tempString =  pffManager.getFolderById(t.getFolderId()).getIsShare();
					if("YES".equals(tempString)){
						isShare[1] = "YES";
					}else{
						isShare[1] = "NO";
					}
					
					ss.add(foId);
					ss.add(fName);
					ss.add(folderFileNum);
					ss.add(folderCreateDate);
					ss.add(isShare);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,5,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,5,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "日程发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取个人文件柜发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "日程发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取个人文件柜发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "日程发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取个人文件柜发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "日程发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取个人文件柜发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * * 获取共享文件柜
	 * author  taoji
	 * @param sessionId
	 * @param userId
	 * @param folderName	支持查询
	 * @param pageSize	一页显示多少数据
	 * @param pageNo	显示第几页
	 * @param order		排序方式  desc  asc
	 * @param orderBy   1 根据创建时间排序  0 默认排序
	 * @return
	 * * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件夹id"/>
		<item type="String" value="文件夹名称"/>
		<item type="String" value="文件夹下文件夹和文件数量"/>
		<item type="String" value="文件夹创建时间"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-9 下午02:10:18
	 */
	public String getShareFolderList(String sessionId, String userId,String folderName,
			String pageSize, String pageNo,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【共享文件柜】的模块  
			 */
			if(pffManager==null){
				throw new SystemException("共享文件柜模块不存在！");
			}
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			if("1".equals(orderBy)){
				page.setOrderBy("1");
				if(order == null || "".equals(order)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrder(order);
				}
			}
			page.setPageNo(Integer.parseInt(pageNo));
			page = pffManager.getShareFolders(page,userId,folderName);
			if(page!=null&&page.getResult()!=null){
				List<ToaPrsnfldrShare> list = page.getResult();
				for(ToaPrsnfldrShare m : list){
					ToaPrivatePrsnfldrFolder t = m.getToaPrsnfldrFolder();
					//文件夹id
					String[] foId = new String[2];
					foId[0] = "String";
					foId[1] = t.getFolderId();
					//文件夹name
					String[] fName = new String[2];
					fName[0] = "String";
					fName[1] = t.getFolderName();
					//文件夹下文件夹和文件数量
					Page pagess = new Page(Integer.parseInt(pageSize),true);
					String[] folderFileNum = new String[2];
					folderFileNum[0] = "String";
					folderFileNum[1] = String.valueOf(pffManager.getFolderFileNum(t.getFolderId())+
							pffManager.getCommonFolderListById(pagess,t.getFolderId(),"").getTotalCount());
					//创建文件夹时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String[] folderCreateDate = new String[2];
					folderCreateDate[0] = "String";
					folderCreateDate[1] = sdf.format(t.getFolderCreateDatetime());
					
					ss.add(foId);
					ss.add(fName);
					ss.add(folderFileNum);
					ss.add(folderCreateDate);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
//				ret = dom.createItemResponseData(STATUS_FAIL, "未查询到数据！", null, null);
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取共享文件柜发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取共享文件柜发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取共享文件柜发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取共享文件柜发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 获取公共文件柜
	 * author  taoji
	 * @param sessionId
	 * @param userId
	 * @param folderName	支持查询
	 * @param pageSize	一页显示多少数据
	 * @param pageNo	显示第几页
	 * @param order		排序方式  desc  asc
	 * @param orderBy   1 根据创建时间排序  0 默认排序
	 * @return
	 * * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件夹id"/>
		<item type="String" value="文件夹名称"/>
		<item type="String" value="文件夹下文件夹和文件数量"/>
		<item type="String" value="文件夹创建时间"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-9 下午02:39:46
	 */
	public String getPublicFolderList(String sessionId, String userId,String folderName,
			String pageSize, String pageNo,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【公共文件柜】的模块  
			 */
			if(ppfManager==null){
				throw new SystemException("公共文件柜模块不存在！");
			}
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			if("1".equals(orderBy)){
				page.setOrderBy("1");
				if(order == null || "".equals(order)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrder(order);
				}
			}
			page.setPageNo(Integer.parseInt(pageNo));
			page = ppfManager.getPublicFolders(page,userId,folderName);
			if(page!=null&&page.getTotalCount()>0){
				List<ToaPublicPrsnfldrFolder> list = page.getResult();
				for(ToaPublicPrsnfldrFolder t : list){
					//文件夹id
					String[] foId = new String[2];
					foId[0] = "String";
					foId[1] = t.getFolderId();
					//文件夹名称
					String[] fName = new String[2];
					fName[0] = "String";
					fName[1] = t.getFolderName();
					//文件夹下文件夹和文件数量
					Page pagess = new Page(Integer.parseInt(pageSize),true);
					String[] folderFileNum = new String[2];
					folderFileNum[0] = "String";
					folderFileNum[1] = String.valueOf(pffManager.getFolderFileNum(t.getFolderId())+
							pffManager.getCommonFolderListById(pagess,t.getFolderId(),"").getTotalCount());
					//文件夹创建时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String[] folderCreateDate = new String[2];
					folderCreateDate[0] = "String";
					folderCreateDate[1] = sdf.format(t.getFolderCreateDatetime());
					
					ss.add(foId);
					ss.add(fName);
					ss.add(folderFileNum);
					ss.add(folderCreateDate);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
//				ret = dom.createItemResponseData(STATUS_FAIL, "未查询到数据！", null, null);
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共文件柜发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共文件柜发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共文件柜发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共文件柜发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 获取部门文件柜
	 * author  taoji
	 * @param sessionId
	 * @param userId		用户id
	 * @param folderName	支持查询
	 * @param pageSize	一页显示多少数据
	 * @param pageNo	显示第几页
	 * @param order		排序方式  desc  asc
	 * @param orderBy   1 根据创建时间排序  0 默认排序
	 * @return
	 * * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件夹id"/>
		<item type="String" value="文件夹名称"/>
		<item type="String" value="文件夹下文件夹和文件数量"/>
		<item type="String" value="文件夹创建时间"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-9 下午02:40:08
	 */
	public String getDepartmentFolderList(String sessionId, String userId,String folderName,
			 String pageSize, String pageNo,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【部门文件柜】的模块  
			 */
			if(dpfManager==null){
				throw new SystemException("部门文件柜模块不存在！");
			}
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			if("1".equals(orderBy)){
				page.setOrderBy("1");
				if(order == null || "".equals(order)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrder(order);
				}
			}
			page.setPageNo(Integer.parseInt(pageNo));
			page = dpfManager.getDepartmentFolders(page,userService.getUserInfoByUserId(userId).getOrgId(),folderName);
			if(page!=null&&page.getTotalCount()>0){
				List<ToaDepartmentPrsnfldrFolder> list = page.getResult();
				for(ToaDepartmentPrsnfldrFolder t : list){
					//文件夹id 
					String[] foId = new String[2];
					foId[0] = "String";
					foId[1] = t.getFolderId();
					//文件夹名称
					String[] fName = new String[2];
					fName[0] = "String";
					fName[1] = t.getFolderName();
					//文件夹下文件夹和文件数量
					Page pagess = new Page(Integer.parseInt(pageSize),true);
					String[] folderFileNum = new String[2];
					folderFileNum[0] = "String";
					folderFileNum[1] = String.valueOf(pffManager.getFolderFileNum(t.getFolderId())+
							pffManager.getCommonFolderListById(pagess,t.getFolderId(),"").getTotalCount());
					//文件夹创建时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String[] folderCreateDate = new String[2];
					folderCreateDate[0] = "String";
					folderCreateDate[1] = sdf.format(t.getFolderCreateDatetime());
					
					ss.add(foId);
					ss.add(fName);
					ss.add(folderFileNum);
					ss.add(folderCreateDate);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
//				ret = dom.createItemResponseData(STATUS_FAIL, "未查询到数据！", null, null);
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取部门文件柜发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取部门文件柜发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取部门文件柜发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取部门文件柜发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 获取机构文件柜
	 * author  taoji
	 * @param sessionId
	 * @param userId    	用户id
	 * @param folderName	支持查询
	 * @param pageSize	一页显示多少数据
	 * @param pageNo	显示第几页
	 * @param order		排序方式  desc  asc
	 * @param orderBy   1 根据创建时间排序  0 默认排序
	 * @return
	 * * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件夹id"/>
		<item type="String" value="文件夹名称"/>
		<item type="String" value="文件夹下文件夹和文件数量"/>
		<item type="String" value="文件夹创建时间"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-9 下午02:40:23
	 */
	public String getAgencyFolderList(String sessionId, String userId,String folderName,
			String pageSize, String pageNo,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【机构文件柜】的模块  
			 */
			if(apfManager==null){
				throw new SystemException("机构文件柜模块不存在！");
			}
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			if("1".equals(orderBy)){
				page.setOrderBy("1");
				if(order == null || "".equals(order)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrder(order);
				}
			}
			page.setPageNo(Integer.parseInt(pageNo));
			page = apfManager.getAgencyFolders(page, userService.
					getUserInfoByUserId("6006800BBDF034DFE040007F01001D9F").
					getSupOrgCode().replaceAll(",", ""),folderName);
			if(page!=null&&page.getTotalCount()>0){
				List<ToaAgencyPrsnfldrFolder> list = page.getResult();
				for(ToaAgencyPrsnfldrFolder t : list){
					//文件夹id
					String[] foId = new String[2];
					foId[0] = "String";
					foId[1] = t.getFolderId();
					//文件夹名称
					String[] fName = new String[2];
					fName[0] = "String";
					fName[1] = t.getFolderName();
					//文件夹下文件夹和文件数量
					Page pagess = new Page(Integer.parseInt(pageSize),true);
					String[] folderFileNum = new String[2];
					folderFileNum[0] = "String";
					folderFileNum[1] = String.valueOf(pffManager.getFolderFileNum(t.getFolderId())+
							pffManager.getCommonFolderListById(pagess,t.getFolderId(),"").getTotalCount());
					//文件夹创建时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String[] folderCreateDate = new String[2];
					folderCreateDate[0] = "String";
					folderCreateDate[1] = sdf.format(t.getFolderCreateDatetime());
					
					ss.add(foId);
					ss.add(fName);
					ss.add(folderFileNum);
					ss.add(folderCreateDate);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
//				ret = dom.createItemResponseData(STATUS_FAIL, "未查询到数据！", null, null);
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取机构文件柜发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取机构文件柜发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取机构文件柜发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取机构文件柜发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * * 根据文件夹id获取子文件夹
	 * author  taoji
	 * @param sessionId
	 * @param folderId
	 * @param folderName	支持查询
	 * @param order		排序方式  desc  asc
	 * @param orderBy   1 根据创建时间排序  0 默认排序
	 * @return
	 * * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件夹id"/>
		<item type="String" value="文件夹名称"/>
		<item type="String" value="文件夹下文件数量"/>
		<item type="String" value="文件夹创建时间"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-9 下午02:10:18
	 */
	public String getFolderListById(String sessionId, String folderId,String folderName,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【文件柜】的模块  
			 */
			if(pffManager==null){
				throw new SystemException("文件柜模块不存在！");
			}
			
			Page page = new Page(1000,true);
			if("1".equals(orderBy)){
				page.setOrderBy("1");
				if(order == null || "".equals(order)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrder(order);
				}
			}
			page.setPageNo(1);
			page = pffManager.getCommonFolderListById(page,folderId,folderName);
			if(page!=null&&page.getTotalCount()>0){
				List<ToaPrsnfldrFolder> list = page.getResult();
				for(ToaPrsnfldrFolder t : list){
					
					//文件夹id
					String[] foId = new String[2];
					foId[0] = "String";
					foId[1] = t.getFolderId();
					//文件夹name
					String[] fName = new String[2];
					fName[0] = "String";
					fName[1] = t.getFolderName();
					//文件夹下文件数量
					String[] folderFileNum = new String[2];
					folderFileNum[0] = "String";
					//文件夹下文件夹和文件数量
					Page pagess = new Page(10,true);
					folderFileNum[1] = String.valueOf(pffManager.getFolderFileNum(t.getFolderId())+
							pffManager.getCommonFolderListById(pagess,t.getFolderId(),"").getTotalCount());
					//创建文件夹时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String[] folderCreateDate = new String[2];
					folderCreateDate[0] = "String";
					folderCreateDate[1] = sdf.format(t.getFolderCreateDatetime());
					
					ss.add(foId);
					ss.add(fName);
					ss.add(folderFileNum);
					ss.add(folderCreateDate);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
//				ret = dom.createItemResponseData(STATUS_FAIL, "未查询到数据！", null, null);
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "日程发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "文件柜发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "日程发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "文件柜发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "日程发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "文件柜发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "日程发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "文件柜发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 共享或删除  文件夹
	 * author  taoji
	 * @param sessionId  
	 * @param folderId ***** 文件夹id
	 * @param userId ***** 共享给哪些人 allPeople 所有人
	 * @param folderProp ***** readonly 只读 readwrite 读写
	 * @param mark *****	1 为共享   0 为删除
	 * @return
	 * @date 2014-1-9 下午06:43:29
	 */
	public String shareOrDel(String sessionId,String folderId,String userId,String folderProp,String mark){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【机构文件柜】的模块  
			 */
			if(apfManager==null){
				throw new SystemException("机构文件柜模块不存在！");
			}
			if(mark == null || "".equals(mark)) {
				throw new SystemException("参数mark不可为空！");
			}
			if(folderId == null || "".equals(folderId)) {
				throw new SystemException("参数folderId不可为空！");
			}
			if("1".equals(mark)){
				if(folderProp == null || "".equals(folderProp)) {
					throw new SystemException("参数folderProp不可为空！");
				}
				String[] sss = folderId.split(",");
				for(int i=0;i<sss.length;i++){
					if(sss[i]!=null&&!"".equals(sss[i])){
						ToaPrivatePrsnfldrFolder folder = pffManager.getFolderById(sss[i]);
						folder.setIsShare("YES");
						
						String[] fdpp = new String[1];
						fdpp[0]=folderProp;
						pffManager.updateFolder(folder,"U",userId,fdpp);
					}
				}
				ret = dom.createItemResponseData(STATUS_SUC, "共享文件夹成功", null, null);
			}else{
				String[] sss;
				if(folderId.indexOf(",")!=-1){
					sss = folderId.split(",");
				}else{
					sss = new String[1];
					sss[0]=folderId;
				}
				for(String id : sss){
					if(id!=null&&!"".equals(id)){
						ToaPrsnfldrFolder folder = pffManager.getWholeFolderById(id);//获取要删除的文件夹
						List<ToaPrsnfldrFolder> subFoldersList = pffManager.getAllSubFolder(id);//获取此文件夹下的子文件夹集合
						for(Iterator<ToaPrsnfldrFolder> it=subFoldersList.iterator();it.hasNext();){
							pffManager.deleteFolder(it.next());
						}
						pffManager.deleteFolder(folder);
					}
				}
				ret = dom.createItemResponseData(STATUS_SUC, "删除文件夹成功", null, null);
			}
			
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 根据文件夹id  获取该文件夹下的所有文件
	 * author  taoji
	 * @param sessionId
	 * @param folderId	文件夹id
	 * @param fileTitle	文件名  支持查询
	 * @param order		desc or  asc
	 * @param orderBy	按什么排序   参数 为   date 按创建时间 ，   size 按文件大小 为null或其他  默认排序
	 * @return
	 * * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件id"/>
		<item type="String" value="文件标题"/>
		<item type="String" value="文件大小"/>
		<item type="String" value="创建时间"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-9 下午07:46:37
	 */
	public String getFileList(String sessionId,String folderId,String fileTitle,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【机构文件柜】的模块  
			 */
			if(apfManager==null){
				throw new SystemException("机构文件柜模块不存在！");
			}
			
			Page page = new Page(1000,true);
			if(!"".equals(order)&&order!=null){
				if(order == null || "".equals(order)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrder(order);
				}
			}
			if(!"".equals(orderBy)&&orderBy!=null){
				if(orderBy == null || "".equals(orderBy)) {
					throw new SystemException("参数order不可为空！");
				}else{
					page.setOrderBy(orderBy);
				}
			}
//			if("1".equals(orderBy)){
//				page.setOrderBy("date");
//			}else if("0".equals(orderBy)){
//				page.setOrderBy("size");
//			}
			page.setPageNo(1);
			page = pffManager.getFolderFileList(page, folderId,fileTitle);
			if(page!=null&&page.getTotalCount()>0){
				List<ToaPrsnfldrFile> list = page.getResult();
				for(ToaPrsnfldrFile t : list){
					//文件id
					String[] fId = new String[2];
					fId[0] = "String";
					fId[1] = t.getFileId();
					//文件标题
					String[] fTitle = new String[2];
					fTitle[0] = "String";
					fTitle[1] = t.getFileTitle();
					//文件大小
					String[] folderFileNum = new String[2];
					folderFileNum[0] = "String";
					if(t.getFileSize().contains("字节")){
						String fileSize = t.getFileSize().replace("字节", "");
						fileSize = fileSize + "B";
						folderFileNum[1] = fileSize;
					}
					else if(t.getFileSize().contains("k")){
						String fileSize = t.getFileSize().replace("k", "");
						fileSize = fileSize + "KB";
						folderFileNum[1] = fileSize;
					}else{
					folderFileNum[1] = t.getFileSize();
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					//创建时间
					String[] folderCreateDate = new String[2];
					folderCreateDate[0] = "String";
					folderCreateDate[1] = sdf.format(t.getFileCreateTime());
					
					ss.add(fId);
					ss.add(fTitle);
					ss.add(folderFileNum);
					ss.add(folderCreateDate);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
				ret = dom.createItemsResponseData(STATUS_SUC, null, ss,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 根据文件id  删除文件   支持批量
	 * author  taoji
	 * @param sessionId
	 * @param fileId 文件id
	 * @return
	 * @date 2014-1-9 下午08:13:00
	 */
	public String delFile(String sessionId,String fileId){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			List<String[]>  ss = new ArrayList<String[]>();
			/**
			 * 判断项目中是否有 【文件柜】的模块  
			 */
			if(pffManager==null){
				throw new SystemException("文件柜模块不存在！");
			}
			if(fileId == null || "".equals(fileId)) {
				throw new SystemException("参数fileId不可为空！");
			}
			pffManager.delFile(fileId);
			ret = dom.createItemResponseData(STATUS_SUC, "删除文件成功", null, null);
			
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 根据文件id 获取文件信息
	 * author  taoji
	 * @param sessionId
	 * @param fileId	//文件id
	 * @param folderId	//文件夹id
	 * @param order	//按什么排序   desc  or  asc  
	 * @param orderBy	//按什么排序     date  or  size
	 * 所有参数都不能为空
	 * @return
	 * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件id"/>
		<item type="String" value="文件标题"/>
		<item type="String" value="(正文标题)附件名称+后缀"/>
		<item type="String" value="备注"/>
		<item type="String" value="创建人"/>
		<item type="String" value="创建时间"/>
		<item type="String" value="该文件前一个文件id"/>
		<item type="String" value="该文件后一个文件id"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-8 下午01:41:53
	 */
	public String getFileInfo(String sessionId,String folderId , String fileId ,String order,String orderBy){
		Dom4jUtil dom = new Dom4jUtil();
		String ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			/**
			 * 判断项目中是否有 【文件柜】的模块  
			 */
			if(pffManager==null){
				throw new SystemException("文件柜模块不存在！");
			}
			if(order == null || "".equals(order)) {
				throw new SystemException("参数order不可为空！");
			}
			if(orderBy == null || "".equals(orderBy)) {
				throw new SystemException("参数orderBy不可为空！");
			}
			if(fileId == null || "".equals(fileId)) {
				throw new SystemException("参数fileId不可为空！");
			}
			if(folderId == null || "".equals(folderId)) {
				throw new SystemException("参数folderId不可为空！");
			}
			ToaPrsnfldrFile t = pffManager.getFile(fileId);
			List<String[]> ls = new ArrayList<String[]>();
			if(t!=null){
				//文件id
				String[] fId = new String[2];
				fId[0] = "String";
				fId[1] = t.getFileId();
				//文件标题
				String[] fTitle = new String[2];
				fTitle[0] = "String";
				fTitle[1] = t.getFileTitle();
				//附件名称
				String[] fName = new String[2];
				fName[0] = "String";
				fName[1] = t.getFileName();
				//备注
				String[] fBeiZhu = new String[2];
				fBeiZhu[0] = "String";
				if(t.getFileBeiZhu()!=null&&!"".equals(t.getFileBeiZhu())){
					fBeiZhu[1] = t.getFileBeiZhu().replaceAll("<p>", "").replaceAll("</p>", "");
				}else{
					fBeiZhu[1] = "";
				}
				//创建人
				String[] fCreatPerson = new String[2];
				fCreatPerson[0] = "String";
				fCreatPerson[1] = t.getFileCreatePerson();
				//创建时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String[] fCreattime = new String[2];
				fCreattime[0] = "String";
				fCreattime[1] = sdf.format(t.getFileCreateTime());
				//该文件前一个id
				String[] agoFileId = new String[2];
				agoFileId[0] = "String";
				agoFileId[1] = pffManager.getFileId(folderId,fileId,order,orderBy,"ago");
				//该文件后一个id
				String[] afterFileId = new String[2];
				afterFileId[0] = "String";
				afterFileId[1] = pffManager.getFileId(folderId,fileId,order,orderBy,"after");
				
				
				ls.add(fId);
				ls.add(fTitle);
				ls.add(fName);
				ls.add(fBeiZhu);
				ls.add(fCreatPerson);
				ls.add(fCreattime);
				ls.add(agoFileId);
				ls.add(afterFileId);
				ret = dom.createXmlUnreads(STATUS_SUC, null,ls);
			}else{
//				ret = dom.createItemResponseData(STATUS_FAIL, "根据文件id未查出数据！", null, null);
				ret = dom.createItemsResponseData(STATUS_SUC, null, ls,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			String s=ex.getMessage();
			if(ex.getMessage()==null){
				ret = dom.createItemResponseData(STATUS_FAIL, "发生未知异常:附件不存在!", null, null);
			}else{
				ret = dom.createItemResponseData(STATUS_FAIL, "发生未知异常:"+ex, null, null);
			}
		}
		return ret;
	}
	/**
	 * 根据文件id 获取正文（附件）文件信息
	 * author  taoji
	 * @param sessionId
	 * @param fileId  文件id
	 * @return
	 * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="文件id"/>
		<item type="String" value="(正文标题)附件名称+后缀"/>
		<item type="String" value="附件内容"/>
		</user>
		</data>			
		</service-response>
			服务器调用失败时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>
	 * @date 2014-1-8 下午01:41:53
	 */
	public String getAttachInfo(String sessionId, String fileId){
		Dom4jUtil dom = new Dom4jUtil();
		String ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			/**
			 * 判断项目中是否有 【文件柜】的模块  
			 */
			if(pffManager==null){
				throw new SystemException("文件柜模块不存在！");
			}
			
			ToaPrsnfldrFile t = pffManager.getFile(fileId);
			if(t==null){
				throw new SystemException("文件不存在！");
			}
			List<String[]> ls = new ArrayList<String[]>();
			if(t!=null){
				long length_byte = t.getFileContent().length;
			    if (length_byte >= 1024) {
					double length_k = ((double) length_byte) / 1024;
					if (length_k >= 6144) {
						ret = dom.createItemResponseData(STATUS_FAIL, "文件柜附件大小超过6M,请在PC端查看附件！", null, null);
						logger.info(ret);
						return ret;
					} 
				}
				//文件id
				String[] fId = new String[2];
				fId[0] = "String";
				fId[1] = t.getFileId();
				//附件名称
				String[] fName = new String[2];
				fName[0] = "String";
				fName[1] = t.getFileName();
				//附件内容
				String[] fContent = new String[2];
				fContent[0] = "String";
				//base64 编码成string 类型
				BASE64Encoder base64 = new BASE64Encoder();
				fContent[1] = base64.encode(t.getFileContent());
				
				ls.add(fId);
				ls.add(fName);
				ls.add(fContent);
				ret = dom.createXmlUnreads(STATUS_SUC, null,ls);
			}else{
//				ret = dom.createItemResponseData(STATUS_FAIL, "根据文件id未查出数据！", null, null);
				ret = dom.createItemsResponseData(STATUS_SUC, null, ls,4,
						String.valueOf(0),String.valueOf(0));
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
			String rtn=ex.getMessage();
			if(rtn==null||"null".equals(rtn)){
				ret = dom.createItemResponseData(STATUS_FAIL, "发生未知异常:文件不存在!", null, null);
			}else{
				ret = dom.createItemResponseData(STATUS_FAIL, "发生未知异常:"+ex, null, null);
			}
		}
		return ret;
	}
	
}
