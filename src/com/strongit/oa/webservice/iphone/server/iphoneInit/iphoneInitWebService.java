package com.strongit.oa.webservice.iphone.server.iphoneInit;

/**
 * 
 */
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Encoder;

import com.strongit.oa.bo.TUumsUserandip;
import com.strongit.oa.bo.ToaIphoneSet;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.calendar.CalendarManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.iphoneset.IphonesetManager;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.notify.NotifyManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.webservice.verificationService.MobileLogService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseLog;
import com.strongit.uums.webservice.AuthenticationHandler;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * @author Administrator
 *
 */
public class iphoneInitWebService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	
 	private IWorkflowService workflowService;      //工作流接口
 	private MessageFolderManager msgForlderManager;//邮件文件夹接口
 	private IphonesetManager iphonesetManager;     //iphone系统设置服务接口
 	private NotifyManager notifyManager;     		//新闻公告服务接口
 	private CalendarManager calendarManager;     	//日程服务接口
	private IUserService userService;
 	
	private IProcessDefinitionService processDefinitionService;
	private SystemsetManager systemsetManager = null;
	private MobileLogService mobileLogService;
	
	public iphoneInitWebService() {
		workflowService = (IWorkflowService) ServiceLocator.getService("workflowService");
		msgForlderManager = (MessageFolderManager) ServiceLocator.getService("messageFolderManager");
		iphonesetManager = (IphonesetManager) ServiceLocator.getService("iphonesetManager");
		processDefinitionService = (IProcessDefinitionService) ServiceLocator.getService("processDefinitionService");
		notifyManager = (NotifyManager) ServiceLocator.getService("notifyManager");
		calendarManager = (CalendarManager) ServiceLocator.getService("calendarManager");
		systemsetManager = (SystemsetManager) ServiceLocator.getService("systemsetManager");
		userService = (IUserService) ServiceLocator.getService("userService");
		mobileLogService= (MobileLogService) ServiceLocator.getService("mobileLogService");
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
	 * @author：shenyl
	 * @time：2012-9-19上午9:33:29
	 * @param mark-获取iphone图片or android图片  传参数   iphone  android  默认 iphone图片
	 * @return String
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public String getIphoneBgUpdate(String mark) throws DAOException, ServiceException,
			SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		

		try {
			//获取全局系统设置对象
			ToaIphoneSet model = iphonesetManager.getIphoneset();
			Date bgUpdate;
			Date modularUpdate;
			if("android".equals(mark)){
				//后期处理
				bgUpdate = new Date();
				modularUpdate = new Date();
			}else{
				bgUpdate = model.getIphoneBgUpdate();
				modularUpdate = model.getIphoneModularUpdate();
			}
			String strTime1 = "";
			String strTime2 = "";
			if(bgUpdate != null){
				strTime1 = 	bgUpdate.toString();
			}
			
			if(modularUpdate != null){
				strTime2 = 	modularUpdate.toString();
			} 
			String [] dates = new String[2]; 
			dates[0] = strTime1;
			dates[1] = strTime2;
			
			ret = dom.createXmldata(STATUS_SUC, null , dates);
		} catch (Exception e) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "获取iphone界面图片数据级异常:"
//					+ e.getMessage(), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "获取界面图片数据级异常:"
					+ e.getMessage(), null, null);
		}
		return ret;
	}
	
    /**
    @Description: (Base64数据格式的iphone登陆界面图片) 
    @author shenyl    
    @date 2012 8月17日下午15:06:42
    @param mark-获取iphone图片or android图片  传参数   iphone  android  默认 iphone图片
    @return
    @throws DAOException
    @throws ServiceException
    @throws SystemException
     */
	public String getIphonebgPic(String mark) throws DAOException, ServiceException,
			SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		String Path = "";
		if("android".equals(mark)){
			Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidBgPic.png";
		}else{
			Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneBgPic.png";
		}
		String picPath = PathUtil.getRootPath()+ Path;	
		try {
			FileInputStream fis = new FileInputStream(picPath);

			if (fis != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
 				String content = encoder.encode(FileUtil.inputstream2ByteArray(fis));
				ret = dom.createItemResponseData(STATUS_SUC, null, "string",
						content);
			}
		} catch (Exception e) {
			// TODO: handle exception
//			ret = dom.createItemResponseData(STATUS_FAIL, "获取iphone界面图片数据级异常:"+e.getMessage(), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "获取界面图片数据级异常:"+e.getMessage(), null, null);
		}
		return ret;
	}
	
	
    /**
    @Description: (Base64数据格式的iphone系统模块图标) 
    @author shenyl    
    @date 2012 8月17日下午15:06:42
    @param mark-获取iphone图片or android图片  传参数   iphone  android  默认 iphone图片
    @return
    Base64格式的登录界面图片
	服务调用成功时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>1</status>
			<fail-reason />
			<data>
				<item type="String" value="待办事宜Base64格式图标 " />
				<item type="String" value="公文管理Base64格式图标 " />
				<item type="String" value="通讯录Base64格式图标 " />
				<item type="String" value="内部邮件Base64格式图标 " />
				<item type="String" value="通知公告Base64格式图标 " />
				<item type="String" value="文档管理Base64格式图标 " />
				<item type="String" value="公共联系人Base64格式图标 " />
				<item type="String" value="领导日程Base64格式图标 " />
				<item type="String" value="系统设置Base64格式图标 " />
			</data>
		</service-response>
		服务调用失败时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>0</status>
		<fail-reason>异常描述</fail-reason>
		<data />
	</service-response>

    @throws DAOException
    @throws ServiceException
    @throws SystemException
     */
	public String getIphoneModularPics(String mark) throws DAOException, ServiceException,
			SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
        
		String [] iphoneModularPics = new String [9];
		String Path = "";
		String picPath = "";
		FileInputStream fis = null;
		try {
			//待办事宜图标
			Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneDbsyPic.png";
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
 				iphoneModularPics[0] = encoder.encode(FileUtil.inputstream2ByteArray(fis));

			}
			//公文管理图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidGwglPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneGwglPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
				BASE64Encoder encoder = new BASE64Encoder();
				iphoneModularPics[1] = encoder.encode(FileUtil.inputstream2ByteArray(fis));
				
			}
			//通讯录图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidTxlPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneTxlPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
				BASE64Encoder encoder = new BASE64Encoder();
				iphoneModularPics[2] = encoder.encode(FileUtil.inputstream2ByteArray(fis));
				
			}
			//内部邮件图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidNbyjPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneNbyjPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
 				iphoneModularPics[3] = encoder.encode(FileUtil.inputstream2ByteArray(fis));

			}
			//通知公告图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidTzggPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneTzggPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
 				iphoneModularPics[4] = encoder.encode(FileUtil.inputstream2ByteArray(fis));

			}
			//文档管理图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidWdglPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneWdglPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
 				iphoneModularPics[5] = encoder.encode(FileUtil.inputstream2ByteArray(fis));

			}
			//公共联系人图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidGglxrPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneGglxrPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
 				iphoneModularPics[6] = encoder.encode(FileUtil.inputstream2ByteArray(fis));

			}
			//领导日程图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidLdrcPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneLdrcPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
				BASE64Encoder encoder = new BASE64Encoder();
				iphoneModularPics[7] = encoder.encode(FileUtil.inputstream2ByteArray(fis));
				
			}
			//系统设置图标
			if("android".equals(mark)){
				Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"androidXtszPic.png";
			}else{
				Path = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"iphoneXtszPic.png";
			}
			picPath = PathUtil.getRootPath()+ Path;	
			fis = new FileInputStream(picPath);
			if (fis != null) {
 				BASE64Encoder encoder = new BASE64Encoder();
 				iphoneModularPics[8] = encoder.encode(FileUtil.inputstream2ByteArray(fis));

			}
			
			ret = dom.createXmldata(STATUS_SUC, null, iphoneModularPics);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			ret = dom.createItemResponseData(STATUS_FAIL, "获取iphone系统图标数据级异常:"+e.getMessage(), null, null);
		}
		return ret;
	}
	
	
	/**
	* @method getUserUnreads
	* @author 申仪玲(shenyl)
	* @created 2012-3-30 下午06:40:31
	* @description 获取待办文件的个数和未读收件箱邮件的条数
	* @return String 返回类型
	* 待办事宜条数、待办公文条数、未读收件箱邮件条数和未查看的领导日程条数。
	服务调用成功时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>1</status>
			<fail-reason />
			<data>
				<item type="String" value="待办事宜条数" />
				<item type="String" value="待办公文条数" />
				<item type="String" value="未读收件箱邮件条数" />
				<item type="String" value="未读通知公告条数" />
				< item type="String" value="未查看的领导日程条数"/>
			</data>
		</service-response>
		服务调用失败时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>0</status>
		<fail-reason>异常描述</fail-reason>
		<data />
	</service-response>

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
			items.add("processTypeName"); // 公文类别名称
			//待办事宜查询条件
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			//待办公文查询条件
			Map<String, Object> paramsMap1 = new HashMap<String, Object>();
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}
			paramsMap.put("handlerId", userId); // 处理人
			paramsMap.put("taskType", "2"); // 非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			
			paramsMap1.put("handlerId", userId); // 处理人
			paramsMap1.put("taskType", "2"); // 非办结任务
			paramsMap1.put("processSuspend", "0");// 取非挂起任务
			
			List<String> type = new ArrayList<String>(2);// 流程类型，查询除收发文的其他流程类型			
			//获取流程类型
			List<Object[]>  typeList = processDefinitionService.getAllProcessTypeList();			
			if (typeList != null && !typeList.isEmpty()){
				for (Object[] processType : typeList){
					//过滤掉收发文流程类型
					if(!"2".equals(String.valueOf(processType[0])) && !"3".equals(String.valueOf(processType[0]))){					
						type.add(String.valueOf(processType[0]));
					}
				}
			}
			
			List<String> type1 = new ArrayList<String>(2);// 流程类型，仅查询收发文			
			type1.add(String.valueOf(WorkFlowTypeConst.SENDDOC));// 发文
			type1.add(String.valueOf(WorkFlowTypeConst.RECEDOC));// 收文
			
			paramsMap.put("processTypeId", type);
			paramsMap1.put("processTypeId", type1);
			

			
			Map orderMap = new HashMap();
			//获取待办事宜列表
			List<Object[]> listWorkflow = workflowService.getTaskInfosByConditionForList(items, paramsMap,
					orderMap, null, null, null, null);
			
			List<Object[]> listShoufa = workflowService.getTaskInfosByConditionForList(items, paramsMap1,
					orderMap, null, null, null, null);
			//待办事宜个数
			String unReadTask = String.valueOf(listWorkflow.size());			
			//待办公文个数
			String unReadShoufa = String.valueOf(listShoufa.size());
			
			//获取收件箱文件夹ID
			String folderId = msgForlderManager.getMsgFolderByName("收件箱", userId).getMsgFolderId();
			//未读收件箱邮件条数
			String unReadMessage = msgForlderManager.getUnreadCount(folderId, userId);
			//未读通知公告条数
			String AfficheNum = notifyManager.getAfficheNum(userId);
			//未查看的领导日程条数
			String calendarNum = calendarManager.getCalendarNum(userId);
			
			List<String[]> result = new ArrayList<String[]>();
			String[] unReadTasks = new String[2];
			String[] unReadShoufas = new String[2];
			String[] unReadMessages = new String[2];
			String[] AfficheNums = new String[2];
			String[] calendarNums = new String[2];
			unReadTasks[0] = "string";
			unReadTasks[1] = unReadTask;
			
			unReadShoufas[0] = "string";
			unReadShoufas[1] = unReadShoufa;
			
			unReadMessages[0] = "string";
			unReadMessages[1] = unReadMessage;
			
			AfficheNums[0] = "string";
			AfficheNums[1] = AfficheNum;
			
			calendarNums[0] = "string";
			calendarNums[1] = calendarNum;
			
			result.add(unReadTasks);
			result.add(unReadShoufas);
			result.add(unReadMessages);
			result.add(AfficheNums);
			result.add(calendarNums);
			ret = dom.createXmlUnreads(STATUS_SUC, null, result);
 			
		}catch(Exception e){
			
			// TODO: handle exception
			ret = dom.createItemResponseData(STATUS_FAIL, "获取数据发生异常:"+e.getMessage(), null, null);
		}
		return ret;
		
	}
	/**
	 * 得到上次登录IP以及本次登录IP
	 * author  taoji
	 * @param sessionId
	 * @param userId 用户id
	 * @param pageSize 当页显示多少条记录
	 * @param pageNo 显示第几页
	 * @return
	 * @date 2014-1-18 下午03:43:47
	 * <?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>1</status>
			<fail-reason />
			<data>
				<item type="String" value="用户名称" />
				<item type="String" value="日志操作开始时间" />
				<item type="String" value="日志操作结束时间" />
				<item type="String" value="操作结果成功or失败" />
				<item type="String" value="上次登入ip " />
				<item type="String" value="本次登入ip" />
			</data>
		</service-response>
		服务调用失败时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>0</status>
		<fail-reason>异常描述</fail-reason>
		<data />
	</service-response>
	 */
	public String getLastIpAndIpandLog(String sessionId,String userId,String pageSize,String pageNo,String loginState){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		//验证用户
		if(AuthenticationHandler.getUserInfo(sessionId)==null){
			return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
		}
		if(pageSize == null || "".equals(pageSize)) {
			throw new SystemException("参数pageSize不可为空！");
		}
		if(pageNo == null || "".equals(pageNo)) {
			throw new SystemException("参数pageNo不可为空！");
		}
		if(loginState == null || "".equals(loginState)) {
			throw new SystemException("参数loginState不可为空！");
		}
		try{
			List<String[]> result = new ArrayList<String[]>();
//			String ip = systemsetManager.getIpByUserId(userId);
			Page<TUumsUserandip> page = new Page<TUumsUserandip>(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));
			String userName = userService.getUserInfoByUserId(userId).getUserName();
			//String logUserName = userService.getUserInfoByUserId(userId).getUserLoginname();
			//page = userService.queryBaseLog(page, logUserName, "", "");
			page=mobileLogService.queryMobileLog(page, userId,loginState);
			if(page!=null&&page.getTotalCount()>0){
				for(TUumsUserandip t : page.getResult()){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					//用户名称
					String[] userN = new String[2];
					userN[0] = "string";
					userN[1] = userName;
					
					//日志操作开始时间
					String[] sdate = new String[2];
					sdate[0] = "string";
					if(t.getLogintime()!=null){
						sdate[1] = sdf.format(t.getLogintime());
					}else{
						sdate[1] = "";
					}
					//设备类型
					String[] eType = new String[2];
					eType[0] = "string";
					eType[1]=t.getDeviceType();
					
					//操作结果
					String[] logResult = new String[2];
					logResult[0] = "string";
					logResult[1]=t.getLoginState();
					
					//操作内容
					String[] logCont = new String[2];
					logCont[0] = "string";
					logCont[1]=t.getLoginDesc();
					
					//ip
					String[] ips = new String[2];
					ips[0] = "string";
					ips[1] = t.getIp();
					
					/*//上次登入ip 
					String[] sps = new String[2];
					//本次登入ip 
					String[] bps = new String[2];
					if(ip!=null&&!"".equals(ip)){
						String[] ips = ip.split(",");
						if(ips.length>1){
							sps[0] = "string";
							sps[1] = ips[0];
							
							bps[0] = "string";
							bps[1] = ips[1];
						}else{
							sps[0] = "string";
							sps[1] = ips[0];
							
							bps[0] = "string";
							bps[1] = "";
						}
					}else{
						sps[0] = "string";
						sps[1] = "";
						
						bps[0] = "string";
						bps[1] = "";
					}*/
						
					result.add(userN);
					result.add(sdate);
					result.add(eType);
					result.add(logResult);
					result.add(logCont);
					result.add(ips);
//					result.add(sps);
//					result.add(bps);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, result,6,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}else{
				ret = dom.createXmlUnreads(STATUS_FAIL, "获取数据为空", null);
			}
		}catch (Exception e) {
			// TODO: handle exception
			ret = dom.createItemResponseData(STATUS_FAIL, "获取数据发生异常:"+e.getMessage(), null, null);
		}
		return ret;
	}
	
}
