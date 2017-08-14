package com.strongit.oa.webservice.server.ipadLoginBg;

/**
 * 
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.Dom4jUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * @author Administrator
 *
 */
public class ipadLoginWebService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	
 	private SystemsetManager systemsetManager;
 	private IWorkflowService workflowService;      //工作流接口
 	private MessageFolderManager msgForlderManager;//邮件文件夹接口
	 
	public ipadLoginWebService() {
		systemsetManager = (SystemsetManager)ServiceLocator.getService("systemsetManager");
		workflowService = (IWorkflowService) ServiceLocator.getService("workflowService");
		msgForlderManager = (MessageFolderManager) ServiceLocator.getService("messageFolderManager");
 	}
	 
	private Date parseDate(String dateStr) throws ParseException,SystemException {
		   try {
			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      return dateFormat.parse(dateStr);
		   } catch (ParseException e) {
		     throw e;
		   } catch (Exception e) {
			   throw new SystemException(e);
		   }
		}

    /**
    @Description: TODO(Base64数据格式的ipad登陆界面图片) 
    @author penghj    
    @date Mar 14, 2012 8:37:20 PM 
    @return
    @throws DAOException
    @throws ServiceException
    @throws SystemException
     */
	@SuppressWarnings("unchecked")
	public String getIpadBgImage() throws DAOException, ServiceException,
			SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
   
		//获取全局系统设置对象
		ToaSystemset systemSet = systemsetManager.getSystemset();
		byte[] buf = systemSet.getIpadbg();
		try {
			if (buf != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
				String content = encoder.encode(buf);
				ret = dom.createItemResponseData(STATUS_SUC, null, "string",
						content);
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret = dom.createItemResponseData(STATUS_FAIL, "获取ipad界面图片数据级异常:"+e.getMessage(), null, null);
		}
		return ret;
	}
	
	 /**
	 @Description: TODO(	ipad登陆界面背景图片更新时间) 
	 @author penghj    
	 @date Mar 14, 2012 8:36:54 PM 
	 @return
	 @throws DAOException
	 @throws ServiceException
	 @throws SystemException
	  */
	public String getIpadBgUpdate() throws DAOException, ServiceException,
			SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		String strTime = null;

		try {
			//获取全局系统设置对象
			ToaSystemset systemSet = systemsetManager.getSystemset();
			Date date = systemSet.getIpadBgUpdate();
			if (date != null) {
				strTime = date.toString();
				ret = dom.createItemResponseData(STATUS_SUC, null, "date",
						strTime);
			} else {
				ret = dom.createItemResponseData(STATUS_FAIL,
						"获取ipad界面图片数据级异常:数据库中无历史界面更新时间", null, null);
			}

		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取ipad界面图片数据级异常:"
					+ e.getMessage(), null, null);
		}
		return ret;
	}
	
	
	/**
	* @method getUserUnreads
	* @author 申仪玲(shenyl)
	* @created 2012-3-30 下午06:40:31
	* @description 获取待办文件的个数和未读收件箱邮件的条数
	* @return String 返回类型
	*/
	@SuppressWarnings("unchecked")
	public String getUserUnreads(String userId) throws DAOException, ServiceException,
			SystemException{
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try{
			List<String> items = new ArrayList<String>();
			items.add("taskId"); // 任务id
			items.add("taskStartDate"); // 任务开始时间
			items.add("businessName"); // 业务数据标题
			items.add("startUserName"); // 拟稿人
			items.add("processTypeId"); // 公文类别 2：发文；3：收文
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}
			paramsMap.put("handlerId", userId); // 处理人
			paramsMap.put("taskType", "2"); // 非办结任务
			
			Map orderMap = new HashMap();
			//获取待办文件列表
			List<Object[]> listWorkflow = workflowService.getTaskInfosByConditionForList(items, paramsMap,
					orderMap, null, null, null, null);
			//待办文件个数
			String unReadTask = String.valueOf(listWorkflow.size());
			//获取收件箱文件夹ID
			String folderId = msgForlderManager.getMsgFolderByName("收件箱", userId).getMsgFolderId();
			//未读收件箱邮件条数
			String unReadMessage = msgForlderManager.getUnreadCount(folderId, userId);
			
			List<String[]> result = new ArrayList<String[]>();
			String[] unReadTasks = new String[2];
			String[] unReadMessages = new String[2];
			unReadTasks[0] = "string";
			unReadTasks[1] = unReadTask;
			
			unReadMessages[0] = "string";
			unReadMessages[1] = unReadMessage;
			
			result.add(unReadTasks);
			result.add(unReadMessages);
			ret = dom.createXmlUnreads(STATUS_SUC, null, result);
 			
		}catch(Exception e){
			
			
		}
		return ret;
		
	}
	
	
}
