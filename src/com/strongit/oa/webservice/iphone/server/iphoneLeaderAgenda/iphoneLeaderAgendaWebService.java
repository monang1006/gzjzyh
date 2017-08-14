package com.strongit.oa.webservice.iphone.server.iphoneLeaderAgenda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.hamcrest.core.IsSame;
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
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarAttach;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaPubliccontact;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.calendar.CalendarAttachManager;
import com.strongit.oa.calendar.CalendarManager;
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
public class iphoneLeaderAgendaWebService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	
	private CalendarManager calendarManager;
	private CalendarAttachManager calendarAttachManager;
	
	/**
	* @构造函数
	* @description 构造方法获取publicContactManage对象
	* @param logger
	* @param articlesManager
	*/
	public iphoneLeaderAgendaWebService() {
		calendarManager = (CalendarManager)ServiceLocator.getService("calendarManager");
		calendarAttachManager = (CalendarAttachManager)ServiceLocator.getService("calendarAttachManager");
		logger.info("日程服务类初始化完毕.。。。");
	}
	
	public String sss(String tjxiha){
		String ret = "tttttttttttssssssssssssssssssssssjjjjjjjjjjjjjj";
		System.out.println(ret);
		return ret;
	}
	/**
	 * 获取当天日程列表	
	 * author  taoji
	 * @param sessionId-用户会话的标识
			  userId-用户Id
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
						<item type="string" value="日程id" />
						<item type="string" value="活动主题" />
						<item type="string" value="活动开始时间" />
						<item type="string" value="活动结束时间" />
						<item type="string" value="活动地点" />
						<item type="string" value="附件id" />
						<item type="string" value="附件标题" />
						<item type="string" value="活动说明" />
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
	public String getCalendarToday(String sessionId, String userId, String pageSize, String pageNo){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			/**
			 * 判断项目中是否有 【日程】的模块  
			 */
			if(calendarManager==null){
				throw new SystemException("日程模块不存在！");
			}
			
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			/************获取今天时间区间***************/
			Date dt = new Date();
			dt.setMinutes(0);
			dt.setSeconds(0);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(dt);
			int day1=startCal.get(Calendar.DATE);  
			startCal.set(Calendar.DATE,day1-1); 
			
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(dt);
			int day2=endCal.get(Calendar.DATE);  
			endCal.set(Calendar.DATE,day2); 
			
			page = calendarManager.getListByDate(page, startCal, endCal, userId);
			if(page==null||page.getTotalCount()==0){
				throw new SystemException("未查询到数据！");
			}
			List<ToaCalendar> list = page.getResult();
			if(list != null && !list.isEmpty()) {
				List<String[]> ls = new ArrayList<String[]>();
				for(ToaCalendar t : list){
					//日程id
					String[] cId = new String[2];
					cId[0] = "String";
					cId[1] = t.getCalendarId();
					//标题
					String[] cTitle = new String[2];
					cTitle[0] = "String";
					cTitle[1] = t.getCalTitle();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					//活动开始时间
					String[] cDateB = new String[2];
					cDateB[0] = "String";
					cDateB[1] = sdf.format(t.getCalStartTime());
					//活动结束时间
					String[] cDateE = new String[2];
					cDateE[0] = "String";
					cDateE[1] = sdf.format(t.getCalEndTime());
					//活动地点
					String[] cPlace = new String[2];
					cPlace[0] = "String";
					cPlace[1] = t.getCalPlace();
					
					//附件
					String cAttachId = "";
					String cAttachName = "";
					Set att = t.getToaCalendarAttaches();
					if(att!=null){
						Iterator it=att.iterator();
						while (it.hasNext()) {
							ToaCalendarAttach objs = (ToaCalendarAttach) it.next();
							ToaAttachment attachment = calendarManager.getToaAttachmentById(objs.getAttachId());
							cAttachId = cAttachId + objs.getAttachId()+",";
							cAttachName = cAttachName + attachment.getAttachName()+",";
						}
					}
					if(!"".equals(cAttachId)){
						cAttachId = cAttachId.substring(0, cAttachId.length()-1);
						cAttachName = cAttachName.substring(0, cAttachName.length()-1);
					}
					//附件id
					String[] cAId = new String[2];
					cAId[0] = "String";
					cAId[1] = cAttachId;
					//附件名称
					String[] cAName= new String[2];
					cAName[0] = "String";
					cAName[1] = cAttachName;
					
					//说明
					String[] cCalCon = new String[2];
					cCalCon[0] = "String";
					cCalCon[1] = t.getCalCon();
					
					ls.add(cId);
					ls.add(cTitle);
					ls.add(cDateB);
					ls.add(cDateE);
					ls.add(cPlace);
					ls.add(cAId);
					ls.add(cAName);
					ls.add(cCalCon);
				}
				ret = dom.createItemsResponseData(STATUS_SUC, null, ls,8,
						String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 获取日程指定附件信息
	 * author  taoji
	 * @param sessionId
	 * @param cId 日程附件id
	 * @return
	 * 返回结果：XML格式字符
		服务器调用成功时返回数据格式如下：
		<?xml version="1.0" encoding="UTF-8"?>
					<service-response>
			          <status>1</status>
		<fail-reason />
		<data>
		<user>
		<item type="String" value="附件id "/>
		<item type="String" value="附件标题 "/>
		<item type="String" value="附件内容 "/>
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
	public String getCalendarAttachById(String sessionId, String cId){
		Dom4jUtil dom = new Dom4jUtil();
		String ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			/**
			 * 判断项目中是否有 【日程】的模块  
			 */
			if(calendarManager==null){
				throw new SystemException("日程模块不存在！");
			}
			if(calendarAttachManager==null){
				throw new SystemException("日程附件模块不存在！");
			}
			
			List<String[]> ls = new ArrayList<String[]>();
			ToaAttachment attachment = calendarManager.getToaAttachmentById(cId);
			if(attachment!=null){
				long length_byte = attachment.getAttachCon().length;
			    if (length_byte >= 1024) {
					double length_k = ((double) length_byte) / 1024;
					if (length_k >= 6144) {
						ret = dom.createItemResponseData(STATUS_FAIL, "日程附件大小超过6M,请在PC端查看附件！", null, null);
						logger.info(ret);
						return ret;
					} 
				}
				//附件id 
				String[] attachId = new String[2];
				attachId[0] = "String";
				attachId[1] = attachment.getAttachId();
				//附件标题
				String[] attachName = new String[2];
				attachName[0] = "String";
				attachName[1] = attachment.getAttachName();
				//附件内容
				String[] attachCon = new String[2];
				attachCon[0] = "String";
				BASE64Encoder base64 = new BASE64Encoder();
				attachCon[1] = base64.encode(attachment.getAttachCon());
				
				ls.add(attachId);
				ls.add(attachName);
				ls.add(attachCon);
				ret = dom.createXmlUnreads(STATUS_SUC, null,ls);
			}else{
				ret = dom.createXmlUnreads(STATUS_SUC, "日程附件为空或不存在!",ls);
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			if(ex.getMessage()==null){
				ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生未知异常:附件不存在", null, null);
			}else{
				ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生未知异常:"+ex, null, null);
			}
		}
		return ret;
	}
	/**
	 * 获取指定日期日程列表	
	 * author  taoji
	 * @param sessionId-用户会话的标识
			  userId-用户Id
			  startTime-开始时间 	格式2012-10-08 09:50
			  endTime-结束时间  	格式2012-10-08 14:50
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
						<item type="string" value="日程id" />
						<item type="string" value="活动主题" />
						<item type="string" value="活动开始时间" />
						<item type="string" value="活动结束时间" />
						<item type="string" value="活动地点" />
						<item type="string" value="附件id" />
						<item type="string" value="附件标题" />
						<item type="string" value="活动说明" />
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
	public String getCalendarByDate(String sessionId, String userId,String startTime,
			String endTime, String pageSize, String pageNo){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			/**
			 * 判断项目中是否有 【日程】的模块  
			 */
			if(calendarManager==null){
				throw new SystemException("日程模块不存在！");
			}
			
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			/************获取开始时间***************/
			SimpleDateFormat sdfff = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			SimpleDateFormat notime=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat onlytime=new SimpleDateFormat("HH:mm");
			SimpleDateFormat comparetime=new SimpleDateFormat("HHmm");
			Calendar startCal = Calendar.getInstance();
			if(startTime!=null&&!"".equals(startTime)){
				Date startDate = sdfff.parse(startTime);
				startCal.setTime(startDate);
			}
			/************获取结束时间***************/
			Calendar endCal = Calendar.getInstance();
			if(endTime!=null&&!"".equals(endTime)){
				Date endDate = sdfff.parse(endTime);
				endCal.setTime(endDate);
			}
			
			page = calendarManager.getListByDate(page, startCal, endCal, userId);
			if(page==null||page.getTotalCount()==0){
				/*ret = dom.createItemResponseData(STATUS_FAIL, "没有查找到相关日程记录", null, null);
				return ret;*/
				ret = dom.createItemsResponseData(STATUS_SUC, null, new ArrayList<String[]>(),11,
						String.valueOf(0),String.valueOf(0));
			}
			List<ToaCalendar> list = page.getResult();
			if(list != null && !list.isEmpty()) {
				List<String[]> ls = new ArrayList<String[]>();
				for(ToaCalendar t : list){
					
					//日程id
					String[] cId = new String[2];
					cId[0] = "String";
					cId[1] = t.getCalendarId();
					//标题
					String[] cTitle = new String[2];
					cTitle[0] = "String";
					cTitle[1] = t.getCalTitle();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					//活动开始时间
					String[] cDateB = new String[2];
					cDateB[0] = "String";
					cDateB[1] = sdf.format(t.getCalStartTime());
					//活动结束时间
					String[] cDateE = new String[2];
					cDateE[0] = "String";
					cDateE[1] = sdf.format(t.getCalEndTime());
					//活动地点
					String[] cPlace = new String[2];
					cPlace[0] = "String";
					cPlace[1] = t.getCalPlace()==null?"":t.getCalPlace();
					
					//附件
					String cAttachId = "";
					String cAttachName = "";
					String cAttachSize="";
					Set att = t.getToaCalendarAttaches();
					if(att!=null&&att.size()>0){
						Iterator it=att.iterator();
						while (it.hasNext()) {
							ToaCalendarAttach objs = (ToaCalendarAttach) it.next();
							ToaAttachment attachment = calendarManager.getToaAttachmentById(objs.getAttachId());
							cAttachId = cAttachId + objs.getAttachId()+",";
							cAttachName = cAttachName + attachment.getAttachName()+",";
							cAttachSize=cAttachSize+(attachment.getAttachCon()==null?"0":String.valueOf(attachment.getAttachCon().length))+",";
						}
					}
					if(!"".equals(cAttachId)){
						cAttachId = cAttachId.substring(0, cAttachId.length()-1);
						cAttachName = cAttachName.substring(0, cAttachName.length()-1);
						cAttachSize = cAttachSize.substring(0, cAttachSize.length()-1);
					}
					//附件id
					String[] cAId = new String[2];
					cAId[0] = "String";
					cAId[1] = cAttachId;
					//附件名称
					String[] cAName= new String[2];
					cAName[0] = "String";
					cAName[1] = cAttachName;
					
					//说明
					String[] cCalCon = new String[2];
					cCalCon[0] = "String";
					cCalCon[1] = t.getCalCon()==null?"":t.getCalCon();
					
					//附件大小
					String[] calSize = new String[2];
					calSize[0] = "String";
					calSize[1] =cAttachSize;
					
					/**
					 * 1、先判断是否在有效日期内
					 */
					//日期的有效时间
					Calendar beginDate=Calendar.getInstance();
					beginDate.setTime(t.getCalStartTime());
					
					//日程的实效时间
					Calendar endDate=Calendar.getInstance();
					endDate.setTime(t.getCalEndTime());
					/**
					 * 开始的时间 时:分
					 */
					int startMin=Integer.valueOf(comparetime.format(t.getCalStartTime()));
					int endMin=Integer.valueOf(comparetime.format(t.getCalEndTime()));
					/**
					 * 1、首先判断今天(传入的日期)是否在有效的日期范围内
					 */
					//活动开始时间
					String[] tmpDateB = new String[2];
					//活动结束时间
					String[] tmpDateE = new String[2];
					
					if(endCal.before(beginDate)||startCal.after(endDate)){
						//说明传入的日期与开始-结束日期没有交集
						continue;
					}else{
						if(t.getCalRepeatStime()!=null){
							/**
							 * 1、判断是否在循环内
							 * 2、剩余的就是关注时间
							 */
							//循环的起始时间
							Calendar repeatWeekStartDay=Calendar.getInstance();
							repeatWeekStartDay.setTime(t.getCalRepeatStime());
							
							//循环的超时时间
							Calendar repeatWeekEndDay=Calendar.getInstance();
							repeatWeekEndDay.setTime(t.getCalRepeatEtime());
							
							/**
							 * 有效日期的分钟
							 */
							int validStartMin=Integer.valueOf(comparetime.format(beginDate.getTime()));
							int validEndMin=Integer.valueOf(comparetime.format(endDate.getTime()));
							/**
							 * 循环日期的分钟
							 */
							int repeatStartMin=Integer.valueOf(comparetime.format(t.getCalRepeatStime()));
							int repeatEndMin=Integer.valueOf(comparetime.format(t.getCalRepeatEtime()));
							
							/**
							 * 循环的时间 时:分
							 */
							/**
							 * 在这个有效期内,获取所有的循环日期
							 * 1、如果循环一部分在有效期内(循环开始早于循环开始)
							 * 2、循环完全在有效期内
							 * 3、循环部分在有效期内(循环的结束大于失效时间)
							 */
							int validBegin=0;
							int validEnd=0;
							int repeatBegin=0;
							int repeatEnd=0;
							if(t.getRepeatType()!=null&&("3".equals(t.getRepeatType())||"4".equals(t.getRepeatType()))){
								if("3".equals(t.getRepeatType())){
									repeatBegin=repeatWeekStartDay.get(Calendar.DAY_OF_WEEK);
									repeatEnd=repeatWeekEndDay.get(Calendar.DAY_OF_WEEK);
								}else{
									repeatBegin=repeatWeekStartDay.get(Calendar.DAY_OF_MONTH);
									repeatEnd=repeatWeekEndDay.get(Calendar.DAY_OF_MONTH);
								}
								
								boolean firstday=true;
								while(!beginDate.after(endDate)||isSameDay(beginDate, endDate)){
									if("3".equals(t.getRepeatType())){
										validBegin=beginDate.get(Calendar.DAY_OF_WEEK);
									}else{
										validBegin=beginDate.get(Calendar.DAY_OF_MONTH);
									}
									//活动开始时间
									cDateB = new String[2];
									cDateB[0] = "String";
									
									//活动结束时间
									cDateE = new String[2];
									cDateE[0] = "String";
									/**
									 * 当前日期属于有效日期中间的一天
									 * 1、找出所有在循环周期中间的
									 * 2、找出所有等于循环开始的
									 * 3、找出所有等于循环结束的
									 * 
									 * 当前日期属于有效日期的开始
									 * 
									 * 当前日期属于有效日期的结束
									 */
									if(validBegin>repeatBegin&&validBegin<repeatEnd){
										if(firstday){
											cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(beginDate.getTime());
											cDateE[1] = notime.format(beginDate.getTime())+" 23:59";
										}else if(isSameDay(beginDate, endDate)){
											cDateB[1] = notime.format(beginDate.getTime())+" 00:01";
											cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(endDate.getTime());
										}else{
											cDateB[1] = notime.format(beginDate.getTime())+" 00:01";
											cDateE[1] = notime.format(beginDate.getTime())+" 23:59";
										}
									}else if(validBegin==repeatBegin&&validBegin==repeatEnd){
										if(firstday){
											if(validStartMin>repeatStartMin&&validStartMin<repeatEndMin){
												cDateB[1] = sdf.format(beginDate.getTime());
												cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
											}else if(validStartMin<=repeatStartMin){
												cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
												cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
											}else{
												//这个日程就无效
												firstday=false;
												beginDate.add(Calendar.DATE, 1);
												continue;
											}
										}else if(isSameDay(beginDate, endDate)){
											if(validEndMin>=repeatEndMin){
												cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
												cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
											}else if(validEndMin<repeatEndMin&&validEndMin>repeatStartMin){
												cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
												cDateE[1] = sdf.format(endDate.getTime());
											}else{
												//这个日程就无效
												firstday=false;
												beginDate.add(Calendar.DATE, 1);
												continue;
											}
										}else{
											cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
											cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
										}
									}else if(validBegin==repeatBegin){
										if(firstday){
											if(validStartMin<repeatStartMin){
												cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
												cDateE[1] = notime.format(beginDate.getTime())+" 23:59";
											}else{
												cDateB[1] = sdf.format(beginDate.getTime());
												cDateE[1] = notime.format(beginDate.getTime())+" 23:59";
											}
										}else if(isSameDay(beginDate, endDate)){
											if(validEndMin<repeatStartMin){
												//这个日程就无效
												firstday=false;
												beginDate.add(Calendar.DATE, 1);
												continue;
											}else if(validEndMin>repeatStartMin){
												cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
												cDateE[1] = sdf.format(endDate.getTime());
											}
										}else{
											cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
											cDateE[1] = notime.format(beginDate.getTime())+" 23:59";
										}
									}else if(validBegin==repeatEnd){
										if(firstday){
											if(validStartMin>repeatEndMin){
												//这个日程就无效
												firstday=false;
												beginDate.add(Calendar.DATE, 1);
												continue;
											}else if(validStartMin<repeatEndMin){
												cDateB[1] = sdf.format(beginDate.getTime());
												cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
											}
										}else if(isSameDay(beginDate, endDate)){
											if(validEndMin<repeatEndMin){
												cDateB[1] = notime.format(beginDate.getTime())+" 00:01";
												cDateE[1] = sdf.format(endDate.getTime());
											}else{
												cDateB[1] = notime.format(beginDate.getTime())+" 00:01";
												cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
											}
										}else{
											cDateB[1] = notime.format(beginDate.getTime())+" 00:01";
											cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
										}
									}
									if(cDateB[1]!=null&&!"".equals(cDateB[1])){
										Calendar cal=Calendar.getInstance();
										cal.setTime(sdf.parse(cDateB[1]));
										if(isSameDay(startCal, cal)){
											ls.add(cDateB);
											ls.add(cDateE);
										}else{
											firstday=false;
											beginDate.add(beginDate.DATE, 1);
											continue;
										}
									}
									firstday=false;
									beginDate.add(beginDate.DATE, 1);
								}
								if(cDateB[1]!=null||"".equals(cDateB[1])){
									//continue;
								}
								
							}else if(t.getRepeatType()!=null&&"2".equals(t.getRepeatType())){
								/**
								 * 按日循环
								 */
								/*if(startMin>repeatStartMin&&startMin>repeatEndMin){
									//如果是同一天
									continue;
								}
								*//**
								 * 按日循环,就只需要关注时间就可以
								 * 1、如果是开始日期
								 * 2、如果是结束日期
								 * 3、中间日期
								 *//*
								if(isSameDay(startCal,beginDate)){
									tmpDateB[0] = "String";
									tmpDateB[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalRepeatStime());
									if(startMin>startRepeatMin){
										tmpDateB[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalStartTime());
									}
									tmpDateE[0] = "String";
									tmpDateE[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalRepeatEtime());
								}else if(isSameDay(startCal,endDate)){
									tmpDateB[0] = "String";
									tmpDateB[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalRepeatStime());
									
									tmpDateE[0] = "String";
									tmpDateE[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalRepeatEtime());
									if(endMin<endRepeatMin){
										tmpDateE[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalEndTime());
									}
								}else{
									tmpDateB[0] = "String";
									tmpDateB[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalRepeatStime());
									
									tmpDateE[0] = "String";
									tmpDateE[1]=notime.format(startCal.getTime())+" "+onlytime.format(t.getCalRepeatEtime());
								}
								ls.add(tmpDateB);
								ls.add(tmpDateE);*/
								/**
								 * 循环日期的分钟
								 */
								repeatStartMin=Integer.valueOf(comparetime.format(t.getCalRepeatStime()));
								repeatEndMin=Integer.valueOf(comparetime.format(t.getCalRepeatEtime()));
								boolean isfirst=true;
								while(!beginDate.after(endDate)||isSameDay(beginDate, endDate)){
									if(repeatEndMin<=validStartMin&&isfirst){
										isfirst=false;
										//不做处理+1day
									}else if(validEndMin<=repeatStartMin&&isSameDay(beginDate, endDate)){
										//不做处理+1day
									}else{
										//活动开始时间
										cDateB = new String[2];
										cDateB[0] = "String";
										cDateB[1] = notime.format(beginDate.getTime())+" "+onlytime.format(t.getCalRepeatStime());
										
										//活动结束时间
										cDateE = new String[2];
										cDateE[0] = "String";
										cDateE[1] = notime.format(beginDate.getTime())+" "+onlytime.format(t.getCalRepeatEtime());
										
										//+7天进入下一个星期
										if(cDateB[1]!=null&&!"".equals(cDateB[1])){
											Calendar cal=Calendar.getInstance();
											cal.setTime(sdf.parse(cDateB[1]));
											if(isSameDay(startCal, cal)){
												ls.add(cDateB);
												ls.add(cDateE);
											}else{
												isfirst=false;
												beginDate.add(beginDate.DATE, 1);
												continue;
											}
										}
										/*ls.add(cDateB);
										ls.add(cDateE);*/
									}
									beginDate.add(beginDate.DATE, 1);
								}
							}else{
								continue;
							}
						}else{
							/**
							 * 非循环,就只需要关注时间就可以
							 * 1、如果是开始日期
							 * 2、如果是结束日期
							 * 3、中间日期
							 */
							if(isSameDay(startCal,beginDate)&&isSameDay(startCal,endDate)){
								tmpDateB[0] = "String";
								tmpDateB[1]=notime.format(startCal.getTime())+" "+onlytime.format(beginDate.getTime());
								
								tmpDateE[0] = "String";
								tmpDateE[1]=notime.format(startCal.getTime())+" "+onlytime.format(endDate.getTime());
							}else if(isSameDay(startCal,beginDate)){
								tmpDateB[0] = "String";
								tmpDateB[1]=notime.format(startCal.getTime())+" "+onlytime.format(beginDate.getTime());
								
								tmpDateE[0] = "String";
								tmpDateE[1]=notime.format(startCal.getTime())+" 23:59";
							}else if(isSameDay(startCal,endDate)){
								tmpDateB[0] = "String";
								tmpDateB[1]=notime.format(startCal.getTime())+" 00:01";
								
								tmpDateE[0] = "String";
								tmpDateE[1]=notime.format(startCal.getTime())+" "+onlytime.format(endDate.getTime());
							}else{
								tmpDateB[0] = "String";
								tmpDateB[1]=notime.format(startCal.getTime())+" 00:01";
								
								tmpDateE[0] = "String";
								tmpDateE[1]=notime.format(startCal.getTime())+" 23:59";
							}
							ls.add(tmpDateB);
							ls.add(tmpDateE);
						}
					}
					if(ls!=null&&(ls.size()%11)==2){
						ls.add(cId);
						ls.add(cTitle);
						ls.add(cDateB);
						ls.add(cDateE);
						ls.add(cPlace);
						ls.add(cAId);
						ls.add(cAName);
						ls.add(cCalCon);
						ls.add(calSize);
					}
				}
				
				if(ls==null||ls.size()==0){
					ret = dom.createItemsResponseData(STATUS_SUC, null, ls,11,
							String.valueOf(0),String.valueOf(0));
				}else{
					ret = dom.createItemsResponseData(STATUS_SUC, null, ls,11,
							String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
				}
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生未知异常:"+ex, null, null);
		}
		return ret;
	}
	
	/**
	 * 比较是否为同一天
	 * @description
	 *
	 * @author  hecj
	 * @date    May 5, 2014 12:10:42 PM
	 * @param   
	 * @return  boolean
	 * @throws
	 */
	private boolean isSameDay(Calendar calA,Calendar calB){
		if((calB.get(Calendar.DAY_OF_MONTH)==calA.get(Calendar.DAY_OF_MONTH))&&Math.abs(calB.getTime().getTime()-calA.getTime().getTime())<24*60*60*1000){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取指定月份中  拥有日程安排的日期list	
	 * author  taoji
	 * @param sessionId-用户会话的标识
			  userId-用户Id
			  dateTime-时间 	格式2012-10
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
						<item type="string" value="日程的创建日期" />
						<item type="string" value="日程的结束日期" />
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
	public String getCalendarDateByDate(String sessionId, String userId,String dateTime, String pageSize, String pageNo){
		Dom4jUtil dom = new Dom4jUtil();
		String  ret = "";
		try {
			//验证用户
			/*if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}*/
			/**
			 * 判断项目中是否有 【日程】的模块  
			 */
			if(calendarManager==null){
				throw new SystemException("日程模块不存在！");
			}
			
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			/************获取开始时间***************/
			int[] monDays = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			if ( ( (new Date().getYear()) % 4 == 0 && (new Date().getYear()) % 100 != 0) ||(new Date().getYear()) % 400 == 0) 
			        monDays[1]++;
			String[] ss =  dateTime.split("-");
			int monDay = monDays[Integer.parseInt(ss[1])-1];
			String starTime = dateTime + "-01 00:00";
			String endTime = dateTime + "-" + monDay+" 00:00";
			SimpleDateFormat sdfff = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat notime=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat onlytime=new SimpleDateFormat("HH:mm");
			Calendar startCal = Calendar.getInstance();
			Date startDate = sdfff.parse(starTime);
			startCal.setTime(startDate);
			/************获取结束时间***************/
			Calendar endCal = Calendar.getInstance();
			Date endDate = sdfff.parse(endTime);
			endDate.setDate(endDate.getDate()+1);
			endCal.setTime(endDate);
			
			page = calendarManager.getListByDate(page, startCal, endCal, userId);
			List<ToaCalendar> list = page.getResult();
			if(page==null||page.getTotalCount()==0){
				ret = dom.createItemsResponseData(STATUS_SUC, null, new ArrayList<String[]>(),2,
						String.valueOf(0),String.valueOf(0));
				return ret;
			}
			if(list != null && !list.isEmpty()) {
				List<String[]> ls = new ArrayList<String[]>();
				/**
				 * 日程循环存在3种模式
				 * 日程表内保存的字段是repeatType
				 * 						2:按日、3:按周、4:按月
				 * 这里作为ios显示日程的接口，只需要处理按周以及按月的情况
				 * 
				 */
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SimpleDateFormat comparetime=new SimpleDateFormat("HHmm");
				/**
				 *如果传入的是5月份，那么这个结束日期是6月1日的00：00
				 */
				for(ToaCalendar t : list){
					/**
					 * 因为传入的是月份,比如5月
					 * 那么就只要将有效范围确定
					 */
					
					//日期的有效时间
					Calendar calBeginDate=Calendar.getInstance();
					calBeginDate.setTime(t.getCalStartTime());
					
					//日程的实效时间
					Calendar calEndDate=Calendar.getInstance();
					calEndDate.setTime(t.getCalEndTime());
					
					/**
					 * 日程的有效起始以及结束时间
					 */
					Calendar validStartCal=Calendar.getInstance();
					Calendar validEndCal=Calendar.getInstance();
					
					/**
					 * 1、如果当前月份的结束时间在日程起效时间之前
					 * 2、如果当前月份的开始时间在日程失效时间之后
					 */
					if(!endCal.after(calBeginDate)){
						continue;
					}else if(startCal.after(calEndDate)){
						continue;
					}else{
						/**
						 * 有效的
						 * 1、当前月份的开始时间不在，结束时间在，部分在有效日期内(包括月份开始时间等于有效开始时间)
						 * 2、当前月份的开始时间在，结束时间不在，部分在有效日期内(包括月份结束时间等于失效时间)
						 * 3、当前月份完全在有效日期内
						 * 4、当前月份完全包含了有效日期
						 */
						if(!startCal.after(calBeginDate)&&endCal.before(calEndDate)){
							validStartCal.setTime(t.getCalStartTime());
							validEndCal.setTime(endCal.getTime());
						}else if(startCal.after(calBeginDate)&&!endCal.before(calEndDate)){
							validStartCal.setTime(startCal.getTime());
							validEndCal.setTime(calEndDate.getTime());
						}else if(startCal.after(calBeginDate)&&endCal.before(calEndDate)){
							validStartCal.setTime(startCal.getTime());
							validEndCal.setTime(endCal.getTime());
						}else{
							validStartCal.setTime(calBeginDate.getTime());
							validEndCal.setTime(calEndDate.getTime());
						}
					}
					/**
					 * 在这个有效期内,获取所有的循环日期
					 * 1、如果循环一部分在有效期内(循环开始早于循环开始)
					 * 2、循环完全在有效期内
					 * 3、循环部分在有效期内(循环的结束大于失效时间)
					 */
					int validBegin=0;
					int validEnd=0;
					int repeatBegin=0;
					int repeatEnd=0;
					
					/**
					 * 有效日期的分钟
					 */
					int validStartMin=Integer.valueOf(comparetime.format(validStartCal.getTime()));
					int validEndMin=Integer.valueOf(comparetime.format(validEndCal.getTime()));
					
					//活动开始时间
					String[] cDateB = null;
					
					//活动结束时间
					String[] cDateE = null;
					
					if(t.getRepeatType()!=null&&("3".equals(t.getRepeatType())||"4".equals(t.getRepeatType()))){
						//循环的起始时间
						Calendar repeatWeekStartDay=Calendar.getInstance();
						repeatWeekStartDay.setTime(t.getCalRepeatStime());
						
						//循环的超时时间
						Calendar repeatWeekEndDay=Calendar.getInstance();
						repeatWeekEndDay.setTime(t.getCalRepeatEtime());
						
						/**
						 * 循环日期的分钟
						 */
						int repeatStartMin=Integer.valueOf(comparetime.format(t.getCalRepeatStime()));
						int repeatEndMin=Integer.valueOf(comparetime.format(t.getCalRepeatEtime()));
						
						if("3".equals(t.getRepeatType())){
							repeatBegin=repeatWeekStartDay.get(Calendar.DAY_OF_WEEK);
							repeatEnd=repeatWeekEndDay.get(Calendar.DAY_OF_WEEK);
						}else{
							repeatBegin=repeatWeekStartDay.get(Calendar.DAY_OF_MONTH);
							repeatEnd=repeatWeekEndDay.get(Calendar.DAY_OF_MONTH);
						}
						
						boolean firstday=true;
						while(!validStartCal.after(validEndCal)||isSameDay(validStartCal, validEndCal)){
							if("3".equals(t.getRepeatType())){
								validBegin=validStartCal.get(Calendar.DAY_OF_WEEK);
							}else{
								validBegin=validStartCal.get(Calendar.DAY_OF_MONTH);
							}
							//活动开始时间
							cDateB = new String[2];
							cDateB[0] = "String";
							
							//活动结束时间
							cDateE = new String[2];
							cDateE[0] = "String";
							/**
							 * 当前日期属于有效日期中间的一天
							 * 1、找出所有在循环周期中间的
							 * 2、找出所有等于循环开始的
							 * 3、找出所有等于循环结束的
							 * 
							 * 当前日期属于有效日期的开始
							 * 
							 * 当前日期属于有效日期的结束
							 */
							if(validBegin>repeatBegin&&validBegin<repeatEnd){
								if(firstday){
									cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(validStartCal.getTime());
									cDateE[1] = notime.format(validStartCal.getTime())+" 23:59";
								}else if(isSameDay(validStartCal, validEndCal)){
									cDateB[1] = notime.format(validStartCal.getTime())+" 00:01";
									cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(validEndCal.getTime());
								}else{
									cDateB[1] = notime.format(validStartCal.getTime())+" 00:01";
									cDateE[1] = notime.format(validStartCal.getTime())+" 23:59";
								}
							}else if(validBegin==repeatBegin&&validBegin==repeatEnd){
								if(firstday){
									if(validStartMin>repeatStartMin&&validStartMin<repeatEndMin){
										cDateB[1] = sdf.format(validStartCal.getTime());
										cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
									}else if(validStartMin<=repeatStartMin){
										cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
										cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
									}else{
										//这个日程就无效
										firstday=false;
										validStartCal.add(Calendar.DATE, 1);
										continue;
									}
								}else if(isSameDay(validStartCal, validEndCal)){
									if(validEndMin>=repeatEndMin){
										cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
										cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
									}else if(validEndMin<repeatEndMin&&validEndMin>repeatStartMin){
										cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
										cDateE[1] = sdf.format(validEndCal.getTime());
									}else{
										//这个日程就无效
										firstday=false;
										validStartCal.add(Calendar.DATE, 1);
										continue;
									}
								}else{
									cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
									cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
								}
							}else if(validBegin==repeatBegin){
								if(firstday){
									if(validStartMin<repeatStartMin){
										cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
										cDateE[1] = notime.format(validStartCal.getTime())+" 23:59";
									}else{
										cDateB[1] = sdf.format(validStartCal.getTime());
										cDateE[1] = notime.format(validStartCal.getTime())+" 23:59";
									}
								}else if(isSameDay(validStartCal, validEndCal)){
									if(validEndMin<repeatStartMin){
										//这个日程就无效
										firstday=false;
										validStartCal.add(Calendar.DATE, 1);
										continue;
									}else if(validEndMin>repeatStartMin){
										cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
										cDateE[1] = sdf.format(validEndCal.getTime());
									}
								}else{
									cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
									cDateE[1] = notime.format(validStartCal.getTime())+" 23:59";
								}
							}else if(validBegin==repeatEnd){
								if(firstday){
									if(validStartMin>repeatEndMin){
										//这个日程就无效
										firstday=false;
										validStartCal.add(Calendar.DATE, 1);
										continue;
									}else if(validStartMin<repeatEndMin){
										cDateB[1] = sdf.format(validStartCal.getTime());
										cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
									}
								}else if(isSameDay(validStartCal, validEndCal)){
									if(validEndMin<repeatEndMin){
										cDateB[1] = notime.format(validStartCal.getTime())+" 00:01";
										cDateE[1] = sdf.format(validEndCal.getTime());
									}else{
										cDateB[1] = notime.format(validStartCal.getTime())+" 00:01";
										cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
									}
								}else{
									cDateB[1] = notime.format(validStartCal.getTime())+" 00:01";
									cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
								}
							}
							
							ls.add(cDateB);
							ls.add(cDateE);
							firstday=false;
							validStartCal.add(validStartCal.DATE, 1);
						}
					}/*else if(t.getRepeatType()!=null&&"4".equals(t.getRepeatType())){
						int begin=calBeginDate.get(Calendar.DAY_OF_MONTH);
						
						int end=calEndDate.get(Calendar.DAY_OF_MONTH);
						
						int startMonthDay=repeatWeekStartDay.get(Calendar.DAY_OF_MONTH);
						int endMonthDay=repeatWeekEndDay.get(Calendar.DAY_OF_MONTH);
						
						while(!calBeginDate.after(calEndDate)){
							if(!calBeginDate.before(startCal)&&!calBeginDate.after(endCal)){
								begin=(calBeginDate.get(Calendar.DAY_OF_MONTH));
								//如果在是循环月份内
								if(begin>=startMonthDay&&begin<=endMonthDay){
									//活动开始时间
									cDateB[0] = "String";
									
									cDateB[1] = notime.format(calBeginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
									
									//循环周期的结束那天
									int addDay=endMonthDay-begin;
									calBeginDate.add(Calendar.DATE, addDay);
									
									//活动结束时间
									cDateE[0] = "String";
									cDateE[1] = notime.format(calBeginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
									
									//+7天进入下一个星期
									ls.add(cDateB);
									ls.add(cDateE);
								}
							}
							calBeginDate.add(Calendar.DATE, 1);	
						}
					}*/else if(t.getRepeatType()!=null&&"2".equals(t.getRepeatType())){
						/**
						 * 按日循环
						 */
						/**
						 * 循环日期的分钟
						 */
						int repeatStartMin=Integer.valueOf(comparetime.format(t.getCalRepeatStime()));
						int repeatEndMin=Integer.valueOf(comparetime.format(t.getCalRepeatEtime()));
						boolean isfirst=true;
						while(!validStartCal.after(validEndCal)||isSameDay(validStartCal, validEndCal)){
							if(repeatEndMin<=validStartMin&&isfirst){
								isfirst=false;
								//不做处理+1day
							}else if(validEndMin<=repeatStartMin&&isSameDay(validStartCal, validEndCal)){
								//不做处理+1day
							}else{
								//活动开始时间
								cDateB = new String[2];
								cDateB[0] = "String";
								cDateB[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(t.getCalRepeatStime());
								
								//活动结束时间
								cDateE = new String[2];
								cDateE[0] = "String";
								cDateE[1] = notime.format(validStartCal.getTime())+" "+onlytime.format(t.getCalRepeatEtime());
								
								//+7天进入下一个星期
								ls.add(cDateB);
								ls.add(cDateE);
							}
							validStartCal.add(validStartCal.DATE, 1);
						}
						/*while(!calBeginDate.after(calEndDate)){
							if(!calBeginDate.before(startCal)&&!calBeginDate.after(endCal)){
								*//**
								 * 如果相同，则判断是否是同一天
								 *//*
								//活动开始时间
								cDateB[0] = "String";
								
								cDateB[1] = notime.format(calBeginDate.getTime())+" "+onlytime.format(repeatWeekStartDay.getTime());
								
								
								//活动结束时间
								cDateE[0] = "String";
								cDateE[1] = notime.format(calBeginDate.getTime())+" "+onlytime.format(repeatWeekEndDay.getTime());
								
								//+7天进入下一个星期
								ls.add(cDateB);
								ls.add(cDateE);
							}
							calBeginDate.add(Calendar.DATE, 1);	
						}*/
					}else{
						//活动开始时间
						cDateB = new String[2];
						cDateB[0] = "String";
						
						if(startDate.after(t.getCalStartTime())){
							cDateB[1] = sdf.format(startDate);
						}else{
							cDateB[1] = sdf.format(t.getCalStartTime());
						}
						//活动结束时间
						cDateE = new String[2];
						cDateE[0] = "String";
						
						if(t.getCalEndTime().after(endDate)){
							cDateE[1] = sdf.format(endDate);
						}else{
							cDateE[1] = sdf.format(t.getCalEndTime());
						}
						
						ls.add(cDateB);
						ls.add(cDateE);
					}
				}
				if(ls==null||ls.size()==0){
					ret = dom.createItemsResponseData(STATUS_SUC, null, ls,2,
							String.valueOf(0),String.valueOf(0));
				}else{
					ret = dom.createItemsResponseData(STATUS_SUC, null, ls,2,
							String.valueOf(page.getTotalCount()),String.valueOf(page.getTotalPages()));
				}
			}
		} catch (DAOException ex) {
			//ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生数据级异常:"+ex, null, null);
		} catch (ServiceException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生服务级异常:"+ex, null, null);
		} catch (SystemException ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生系统级异常:"+ex, null, null);
		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "读取公共联系人发生未知异常:"+JavaUtils.stackToString(ex), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "读取日程信息发生未知异常:"+ex, null, null);
		}
		return ret;
	}
}
