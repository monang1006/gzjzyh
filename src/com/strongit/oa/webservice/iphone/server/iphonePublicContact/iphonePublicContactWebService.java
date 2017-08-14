package com.strongit.oa.webservice.iphone.server.iphonePublicContact;

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
import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
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
public class iphonePublicContactWebService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	
	private PublicContactManage publicContactManage;
	
	/**
	* @构造函数
	* @description 构造方法获取publicContactManage对象
	* @param logger
	* @param articlesManager
	*/
	public iphonePublicContactWebService() {
		publicContactManage = (PublicContactManage)ServiceLocator.getService("publicContactManage");
		logger.info("公共联系人服务类初始化完毕.。。。");
	}
	
	public String sss(String tjxiha){
		String ret = "tttttttttttssssssssssssssssssssssjjjjjjjjjjjjjj";
		System.out.println(ret);
		return ret;
	}
	/**
	 * 获取公共联系人列表	
	 * author  taoji
	 * @param sessionId-用户会话的标识
			  pageSize-每页显示的数量
			  pageNo-页码
	 * @return ret
	 * 返回结果：XML格式字符
	服务器调用成功返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
			<service-response>
					<status>1</status>
					<fail-reason />
					<data totalCount="总记录数" totalPages="总页数">
	                  <row>
						<item type="string" value="联系人id" />
						<item type="string" value="联系人名称" />
						<item type="string" value="联系人手机" />
						<item type="string" value="联系人电话" />
						<item type="string" value="联系人邮件" />
					   <row>
					</data>
			</service-response>
	服务器调用失败返回数据格式如下
		<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>0</status>
			<fail-reason>异常描述</fail-reason>
			<data />
		</service-response>

	 * @date 2014-1-8 上午08:57:21
	 */
	public String getPubContList(String sessionId, String pageSize, String pageNo){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			/**
			 * 判断项目中是否有 【公共联系人】的模块  
			 */
			if(publicContactManage==null){
				throw new SystemException("公共联系人模块不存在！");
			}
			
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			page = publicContactManage.getContactsBycondition(page, "", "", "", "");
			if(page==null||page.getTotalCount()==0){
				throw new SystemException("未查询出数据！");
			}
			List<ToaPubliccontact> list = page.getResult();
			if(list != null && !list.isEmpty()) {
				List<String[]> ls = new ArrayList<String[]>();
				for(ToaPubliccontact t : list){
					//联系人id 
					String[] pcId = new String[2];
					pcId[0] = "String";
					pcId[1] = t.getPcId();
					//名称
					String[] pcName = new String[2];
					pcName[0] = "String";
					pcName[1] = t.getPcName();
					//手机
					String[] pcPhone = new String[2];
					pcPhone[0] = "String";
					pcPhone[1] = t.getPcPhone();
					//电话
					String[] pcTell = new String[2];
					pcTell[0] = "String";
					pcTell[1] = t.getPcTell();
					//email
					String[] pcEmail = new String[2];
					pcEmail[0] = "String";
					pcEmail[1] = t.getPcEmail();
					
					ls.add(pcId);
					ls.add(pcName);
					ls.add(pcPhone);
					ls.add(pcTell);
					ls.add(pcEmail);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ls,5,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 获取公共联系人信息
	 * author  taoji
	 * @param sessionId
	 * @param pubContId
	 * @return
	 * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="联系人图片"/>
		<item type="String" value="联系人Id"/>
		<item type="String" value="联系人名称"/>
		<item type="String" value="联系人手机号"/>
		<item type="String" value="联系人电话"/>
		<item type="String" value="联系人Email"/>
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
	public String getPubContInfo(String sessionId, String pubContId ){
		Dom4jUtil dom = new Dom4jUtil();
		String ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			/**
			 * 判断项目中是否有 【公共联系人】的模块  
			 */
			if(publicContactManage==null){
				throw new SystemException("公共联系人模块不存在！");
			}
			
			ToaPubliccontact t = publicContactManage.get(pubContId);
			List<String[]> ls = new ArrayList<String[]>();
			if(t!=null){
				String[] pcId = new String[2];
				pcId[0] = "String";
				pcId[1] = t.getPcId();
				
				String[] pcName = new String[2];
				pcName[0] = "String";
				pcName[1] = t.getPcName();
				
				String[] pcPhone = new String[2];
				pcPhone[0] = "String";
				pcPhone[1] = t.getPcPhone();
				
				String[] pcTell = new String[2];
				pcTell[0] = "String";
				pcTell[1] = t.getPcTell();
				
				String[] pcEmail = new String[2];
				pcEmail[0] = "String";
				pcEmail[1] = t.getPcEmail();
				
				ls.add(pcId);
				ls.add(pcName);
				ls.add(pcPhone);
				ls.add(pcTell);
				ls.add(pcEmail);
				ret = dom.createXmlUnreads(STATUS_SUC, null,ls);
			}else{
				ret = dom.createItemResponseData(STATUS_SUC, "根据联系人id未查出该联系人！", null, null);
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+ex, null, null);
		}
		return ret;
	}
}
