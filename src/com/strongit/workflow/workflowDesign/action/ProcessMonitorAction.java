package com.strongit.workflow.workflowDesign.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.DataBaseUtil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.component.formtemplate.util.FormGridDataHelper;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.DateCountUtil;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.oa.util.StringUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.bo.TwfInfoProcessLog;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.workflowDesign.action.util.ProcessInstanceDataUtil;
import com.strongit.workflow.workflowDesign.action.util.ProcessUtil;
import com.strongit.workflow.workflowInterface.IOaSystemService;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 流程监控处理Action
 * 
 * @author 喻斌
 * @company Strongit Ltd. (C) copyright
 * @date Dec 15, 2008 1:28:35 PM
 * @version 1.0.0.0
 * @classpath com.strongit.workflow.workflowDesign.action.ProcessMonitorAction
 */
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "processMonitor.action", type = ServletActionRedirectResult.class),
		@Result(name = "defaultReturn", value = "/WEB-INF/jsp/workflowDesign/defaultpage/defaultPage.jsp", type = ServletDispatcherResult.class),
		@Result(name = "tree", value = "/WEB-INF/jsp/workflowDesign/action/processMonitor-tree.jsp", type = ServletDispatcherResult.class),
		@Result(name = "processView", value = "/WEB-INF/jsp/workflowDesign/action/processMonitor-processView.jsp", type = ServletDispatcherResult.class),
		@Result(name = "mainFrame", value = "/WEB-INF/jsp/workflowDesign/action/processMonitor-mainFrame.jsp", type = ServletDispatcherResult.class) })
public class ProcessMonitorAction extends AbstractBaseWorkflowAction<Object[]> {
	/**
	 */
	private static final long serialVersionUID = 1L;

	private Page page = new Page(FlexTableTag.MAX_ROWS, true);

	private List treeList;

	private String processId;

	private String proName;

	private String personDemo = "";

	private Object[] model = new Object[] {};

	private IProcessInstanceService manager;

	private IProcessDefinitionService definitionService;

	private String processWorkflowNames;
	@Autowired
	@Qualifier("baseManager")
	BaseManager basemanager;

	@Autowired
	ITaskService taskService; // 任务处理服务类

	@Autowired
	IOaSystemService oaSystemService; // 处理审批意见
	@Autowired
	MyLogManager logService;// 日志服务类

	@Autowired
	IUserService userService;// 统一用户服务
	@Autowired
	ICustomUserService customUserService;// 

	@Autowired
	protected IWorkflowService workflow; // 工作流服务

	// 查询条件
	private String processName; // 流程名

	private String startUserName; // 发起人

	private Date searchDate; // 启动时间

	private String searchDateString;

	private String timeout; // 流程是否超期

	private String processDefinitionNames = "";// 流程名称

	private Map<String, String> processDefinitionNameMapIds;

	@SuppressWarnings("unused")
	private Map<String, String> OrgNameMapIds;
	
	private String orgIds;

	private List annalList; // 流程处理意见

	private String day; // 任务停留天数

	private String mytype;

	private String isFind = "0"; // "1":表示正在执行查询操作,"0"或其他:表示当前不是进行查询操作
	
	private String fromLeaderDesktopPage = "0";
	
	private String processTitle;
	
	@Autowired
	private IRelativeWorkflowService rwSrv;
	
	
	public String getProcessTitle() {
		return processTitle;
	}

	public void setProcessTitle(String processTitle) {
		this.processTitle = processTitle;
	}

	public void setModel(Object[] model) {
		this.model = model;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public ProcessMonitorAction() {
	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page getPage() {
//		if(page.getPageSizeBak() != -1 && page.getPageSizeBak() != page.getPageSize()){
//			page.setPageNo(1);
//		}
//		if(page.getPageSizeBak() != -1 && page.getPageNoBak() == page.getPageNo()){
//			page.setPageNo(1);
//		}
		return page;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public Object[] getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(IProcessInstanceService aManager) {
		manager = aManager;
	}

	@Autowired
	public void setDefinitionService(IProcessDefinitionService definitionService) {
		this.definitionService = definitionService;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 * @modify yanjian 2011-09-28 添加红黄预警功能
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public String list() throws Exception {
		if (processDefinitionNames != null & !processDefinitionNames.equals("")) {
			try {
				processDefinitionNames = java.net.URLDecoder.decode(
						processDefinitionNames, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// page = definitionService.getProcessInstancePageByDefinitionId(page,
		// processId);
		// return SUCCESS;
		try {
			if (!"".equals(proName) && null != proName) {
				proName = java.net.URLDecoder.decode(proName, "utf-8");
				getRequest().setAttribute("proName", proName);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(proName);
		List<String> itemsList = new ArrayList<String>();
		itemsList.add("businessName");
		itemsList.add("startUserName");
		itemsList.add("processInstanceId");
		itemsList.add("processStartDate");
		itemsList.add("processEndDate");
		itemsList.add("processTimeout");
		itemsList.add("processName");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if (processId != null && !"".equals(processId)) {
			// 设置流程定义Id
			String[] processIds = processId.split(",");
			List<Long> pdIds = new ArrayList<Long>();
			for (int i = 0; i < processIds.length; i++) {
				pdIds.add(new Long(processIds[i]));
			}
			paramsMap.put("processDefinitionId", pdIds);
		} else {// 默认显示所有流程定义下的流程实例
			/*
			 * User user = userService.getCurrentUser();
			 * if(user.getUserIsSupManager() != null &&
			 * "1".equals(user.getUserIsSupManager())) { //超级管理员可以看到所有监控流程 }
			 * else {
			 */
			if (processDefinitionNames != null
					& !processDefinitionNames.equals("")) {
				List<Long> pdIds = new ArrayList<Long>();
				List<Long> pidList = new LinkedList<Long>();
				String[] pidArray = processDefinitionNames.split(",");
				for (String str : pidArray) {
					pidList.add(Long.valueOf(str));
				}
				pdIds = pidList;
				paramsMap.put("processDefinitionId", pdIds);
			}else{
				Map<String,String> map = getProcessDefinitionNameMapIds();
				if(map != null){
					List<String> ss= new ArrayList(map.values());
					List<Long> pdIds = new ArrayList<Long>();
					for(String str:ss){
						String[] temp = str.split(",");
						for(String strtemp:temp){
							pdIds.add(Long.valueOf(strtemp));
						}
					}
					paramsMap.put("processDefinitionId", pdIds);
				}else{
					paramsMap.put("processDefinitionId", Long.MAX_VALUE);
				}
			}


			/* } */
		}
		if ((workflowName != null && !"".equals(workflowName))
				|| (day != null && !"".equals(day))) {
			List<Long> instanceIds = basemanager.findDefinitionByHandler(
					workflowName, day);
			if (instanceIds == null || instanceIds.isEmpty()) {
				page.setTotalCount(0);
				return SUCCESS;
			}
			paramsMap.put("processInstanceId", instanceIds);
		}
		try {
			if (processName != null && !"".equals(processName)) {
				paramsMap.put("businessName", "%"+processName+"%");
			}
			if (startUserName != null && !"".equals(startUserName)) {
				startUserName = URLDecoder.decode(startUserName, "utf-8");
				paramsMap.put("startUserName", "%"+startUserName+"%");
			}
			if (searchDate != null) {
				searchDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format(searchDate);
			} else {
				searchDateString = "";
			}
			if (searchDate != null && !"".equals(searchDate)) {
				Date endtime = new Date();
				endtime.setTime(searchDate.getTime());
				endtime.setHours(23);
				endtime.setMinutes(59);
				// paramsMap.put("processStartDateEnd", searchDate);
				paramsMap.put("processStartDateStart", searchDate);
				paramsMap.put("processStartDateEnd", endtime);
			}
			if (state != null && !"".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			if (timeout != null && !"".equals(timeout)) {
				paramsMap.put("processTimeout", timeout);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// paramsMap.put("processStartDateEnd", searchDate);
		// paramsMap.put("processStartDateStart", processName);

		paramsMap.put("processSuspend", "0");
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("processStartDate", "1");
		if (isFind != null && isFind.equals("1")) { // 执行查询操作，页面自动跳转到第一页
			page.setPageNo(1);
		}
		String customQuery = "";
		String customFromItems = "";
		String customSelectItems  = "";
		if(customFromItems.length()>0){
			customFromItems += ",";
		}
		customFromItems += "Tjbpmbusiness JBPMBUSINESS";
		if(customQuery.length()>0){
			customQuery += " and ";
		}
		customQuery += " @businessId = JBPMBUSINESS.businessId ";
		customQuery += " and JBPMBUSINESS.businessType in ("+Tjbpmbusiness.getShowableBusinessType()+") ";
		page = manager.getProcessInstanceByConditionForPage(page, itemsList,
				paramsMap, orderMap, customSelectItems, customFromItems, customQuery, null);
		if (page.getResult() != null) {
			List<Object[]> result = page.getResult();
			Properties properties = FormGridDataHelper.getColorSetProperties();
			if (result != null && !result.isEmpty()) {
				for (int i = 0; i < result.size(); i++) {

					Object[] objs = result.get(i);
					if (objs[0] != null) {
						objs[0] = ((String) objs[0]).replace("\\r\\n", " ");
						objs[0] = ((String) objs[0]).replace("\\n", " ");
					}
					Object endDate = objs[4];// 流程实例介绍时间
					objs = ObjectUtils.addObjectToArray(objs, "");
					objs = ObjectUtils.addObjectToArray(objs, "");
					objs = ObjectUtils.addObjectToArray(objs, "");
					StringBuilder picImage = new StringBuilder();
					String rootPath = getRequest().getContextPath();
					String picPath = null;
					String title = null;
					if (endDate == null) {// 流程未结束
						Long instanceId = (Long) objs[2];
						Object[] returnObjs = adapterBaseWorkflowManager.getWorkflowService()
								.getProcessStatusByPiId(instanceId.toString());// 得到此流程实例下的运行情况
						Collection col = (Collection) returnObjs[6];// 处理任务信息
						if (col != null && !col.isEmpty()) {
							StringBuilder strUserName = new StringBuilder();// 人员姓名
							StringBuilder strNodeName = new StringBuilder();// 节点名称
							StringBuilder nodeEnterTime = new StringBuilder();// 节点进入时间
							for (Iterator it = col.iterator(); it.hasNext();) {
								Object[] itObjs = (Object[]) it.next();
								String userId = (String) itObjs[3];
								String taskFlag = (String) itObjs[0];
								if (itObjs[2] == null) {
									result.remove(i);
									i--;
									break;
								}
								Date nodeEnter = (Date) itObjs[2];
								Date now = new Date();
								long day = DateCountUtil.getDistDates(
										nodeEnter, now);

								long time = new Date().getTime()
										- nodeEnter.getTime();
								long hours = time / (1000 * 60 * 60);
								if (hours <= 72) {// 在24小时内
									picPath = properties.getProperty("green");
									title = "3天内办理";
								} else if (hours <= 144) {// 在48小时内
									picPath = properties.getProperty("yellow");
									title = "6天内办理";
								} else {// 超过48小时
									picPath = properties.getProperty("red");
									title = "超过7天办理";
								}

								StringBuilder userName = new StringBuilder();
								if (userId != null && !"".equals(userId)) {
									String[] userIds = userId.split(",");
									for (String id : userIds) {
										userName
												.append(
														userService
																.getUserNameByUserId(id))
												.append(",");
									}
									userName
											.deleteCharAt(userName.length() - 1);
								}
								if (userName.length() > 0) {
									strUserName.append("[").append(userName)
											.append("]");
								}
								if ("subProcess".equals(taskFlag)) {// 子流程节点
									strNodeName.append("[").append(
											(String) itObjs[1]).append("]");
								} else {// 任务节点
									strNodeName.append("[").append(
											(String) itObjs[1]).append("]");
								}
								if (nodeEnterTime.length() == 0) {
									if (day == 0) {
//										nodeEnterTime.append("正在办理中");
										nodeEnterTime.append("小于1天未办理");
									} else {
										nodeEnterTime.append(day)
												.append("天未办理");
									}
								}
							}
							objs[4] = strNodeName.toString();
							objs[7] = strUserName.toString();
							objs[8] = nodeEnterTime.toString();

						} else {
							picPath = properties.getProperty("blue");
							title = "办毕";
							objs[4] = "办毕";
						}
					} else {
						picPath = properties.getProperty("blue");
						title = "办毕";
						objs[4] = "办毕";
					}
					if(picPath != null && !"".equals(picPath)){
						picImage
						.append("<img src=\"")
						.append(rootPath)
						.append(picPath)
						.append(
								"\"  title=\""
								+ title
								+ "\" style=\"text-align:center;vertical-align:middle;width:15px;height:15px;\"/> ");
						objs[9] = picImage.toString();
					}

					result.set(i, objs);
				}
			}
		}

		return SUCCESS;
	}

	@SuppressWarnings( { "unchecked", "deprecation" })
	public String monitorList() throws Exception {
		ProcessInstanceDataUtil processInstanceDataUtil = new ProcessInstanceDataUtil(
				mytype);
		if (processDefinitionNames != null & !processDefinitionNames.equals("")) {
			processDefinitionNames = java.net.URLDecoder.decode(
					processDefinitionNames, "UTF-8");
		}
		List<String> itemsList = processInstanceDataUtil.getToSelectItems();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		List<Long> instanceIds = new ArrayList<Long>();
		if (processDefinitionNames != null && !processDefinitionNames.equals("") && !"null".equals(processDefinitionNames)) {
			List<Long> pidList = new LinkedList<Long>();
			String[] pidArray = processDefinitionNames.split(",");
			for (String str : pidArray) {
				 pidList.add(Long.valueOf(str));
			}
			paramsMap.put("processDefinitionId", pidList);
		}else{
			Map<String,String> map = getProcessDefinitionNameMapIds();
			if(map != null){
				List<String> ss= new ArrayList(map.values());
				List<Long> pdIds = new ArrayList<Long>();
				for(String str:ss){
					String[] temp = str.split(",");
					for(String strtemp:temp){
						pdIds.add(Long.valueOf(strtemp));
					}
				}
				paramsMap.put("processDefinitionId", pdIds);
			}else{
				if(mytype != null && !"".equals(mytype)){
									
				}else{
					paramsMap.put("processDefinitionId", Long.MAX_VALUE);
				}
			}
		}
		if ((workflowName != null && !"".equals(workflowName))
				|| (day != null && !"".equals(day))) {
			instanceIds = basemanager
					.findDefinitionByHandler(workflowName, day);
			System.out.println();
			if (instanceIds == null || instanceIds.isEmpty()) {
				page.setTotalCount(0);
				if ("dept".equals(mytype)) {
					// return "monitordeptList";
					return "monitororgList";
				}
				if ("org".equals(mytype)) {
					return "monitororgList";
				}
				if ("user".equals(mytype)) {
					return "monitoruserList";
				}
				return "monitorList";
			} else {
					paramsMap.put("processInstanceId", instanceIds);
			}
		}
		
		
		if (processWorkflowNames != null && !"".equals(processWorkflowNames)) {
			paramsMap.put("processName", "%"+processWorkflowNames+"%");
		}
		if (processName != null && !"".equals(processName)) {
			//processName = URLDecoder.decode(processName, "utf-8");
			paramsMap.put("businessName", "%"+processName+"%");
		}
		if (startUserName != null && !"".equals(startUserName)) {
			//startUserName = URLDecoder.decode(startUserName, "utf-8");
			if(mytype == null || "".equals(mytype) || "user".equals(mytype)){
				paramsMap.put("startUserName", "%"+startUserName+"%");
			}
		}
		if (searchDate != null) {
			searchDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.format(searchDate);
		} else {
			searchDateString = "";
		}
		if (searchDate != null && !"".equals(searchDate)) {
			Date endtime = new Date();
			endtime.setTime(searchDate.getTime());
			endtime.setHours(23);
			endtime.setMinutes(59);
			// paramsMap.put("processStartDateEnd", searchDate);
			paramsMap.put("processStartDateStart", searchDate);
			paramsMap.put("processStartDateEnd", endtime);
		}
		if (state != null && !"".equals(state)) {
			paramsMap.put("processStatus", state);
		}
		if (timeout != null && !"".equals(timeout)) {
			paramsMap.put("processTimeout", timeout);
		}

		Map<String, String> orderMap = new LinkedHashMap<String, String>();
		orderMap.put("processStartDate", "1");
		orderMap.put("processInstanceId", "1");
		if (isFind != null && isFind.equals("1")) { // 执行查询操作，页面自动跳转到第一页
			page.setPageNo(1);
		}
		User user = userService.getCurrentUser();
		String sql = null;
		if("org".equals(mytype) ){
			if((orgIds != null && !"".equals(orgIds))){
				sql = processInstanceDataUtil.getSql(user.getUserId(), orgIds,startUserName);
			}else{
				sql = processInstanceDataUtil.getSql(user.getUserId(), null,startUserName);
			}
		}else{
			sql = processInstanceDataUtil.getSql(user.getUserId(), user.getOrgId(),startUserName);
		}
		String customQuery = "";
		String customFromItems = "";
		String customSelectItems  = "";
		if(mytype != null && !"".equals(mytype)){
			customSelectItems  = " mainActor.mainActorId, wfbaseprocessfile.rest2 as workflowAliaName ";
			customFromItems  += " com.strongit.oa.bo.TMainActorConfing mainActor, com.strongit.workflow.bo.TwfBaseProcessfile wfbaseprocessfile ";
			
			//如果页面来自领导首页(desktopWhole-bgtDesktopForLeader.jsp)  则查询相应处室
			if(fromLeaderDesktopPage!=null && "1".equals(fromLeaderDesktopPage)){
				String roomId = getRequest().getParameter("roomId");
				if(roomId!=null && !"".equals(roomId)){
					sql += " and org.orgId = '" + roomId + "'";
				}				
			}
			
			customQuery = "mainActor.processInstanceId = "+DataBaseUtil.SqlNumberToChar("@processInstanceId")+" and pi.startUserId in ("+sql+")  AND  wfbaseprocessfile.pfName =  pi.name ";
			if("1".equals(state)){//办结文取本年度的
			    	int doneyear = Integer.MIN_VALUE;
			    if(doneYear == null || "null".equals(doneYear)){
			    	//doneYear = "-1";
			    	Calendar a=Calendar.getInstance();  
					 doneYear = a.get(Calendar.YEAR)+"";
			    }
				if (doneYear != null && !"".equals(doneYear)
						&& !"null".equals(doneYear)) {
					if ("-1".equals(doneYear)) {
						java.util.Calendar Cal = java.util.Calendar
								.getInstance();
						doneyear = Cal.get(java.util.Calendar.YEAR);
					} else {
						doneyear = Integer.parseInt(doneYear);
					}
					customQuery += " and "
							+ DataBaseUtil.SqlYearOfDate("pi.end") + "="
							+ doneyear + " ";
				}
			}
		}else{
			customQuery +=" exists(select ti.id from org.jbpm.taskmgmt.exe.TaskInstance ti join ti.pooledActors pa where "
				+ "pa.actorId in("
				+ sql
				+ ")"
				+ " and ti.processInstance.id = @processInstanceId)";
		}
		customQuery +=" and pi.name not like '自办文' ";
		
		
				//如果页面来自领导首页(desktopWhole-bgtDesktopForLeader.jsp)  办结文件则查询本周
				if(fromLeaderDesktopPage!=null && "1".equals(fromLeaderDesktopPage)){
					String isFenban = getRequest().getParameter("isFenban");
					//计算上周起止日期
					Date now = new Date();
					now.setTime(now.getTime()-7*24*60*60*1000);//上周
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(now);
					if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
						now.setTime(now.getTime()-24*60*60*1000);
						calendar.setTime(now);
					}
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					Date currentWeekStartDate = calendar.getTime();
					currentWeekStartDate.setHours(0);
					currentWeekStartDate.setMinutes(0);
					currentWeekStartDate.setSeconds(0);
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
					Date currentWeekEndDate = calendar.getTime();
					currentWeekEndDate.setHours(23);
					currentWeekEndDate.setMinutes(59);
					currentWeekEndDate.setSeconds(59);
					if(state!=null && "1".equals(state)){				//办结则查询本周   在办则无时间限制							
						paramsMap.put("processEndDateStart", currentWeekStartDate);
						paramsMap.put("processEndDateEnd", currentWeekEndDate);						
					}					
					if(isFenban != null && "1".equals(isFenban)){	//isFenban: 1 表示为"处室收文办理"
						paramsMap.put("processStatus",null);	// 统计已分办 实则统计"处室收文办理"数 忽略"处室收文办理"流程实例的状态
						paramsMap.put("processStartDateStart", currentWeekStartDate);
						paramsMap.put("processStartDateEnd", currentWeekEndDate);	
						customQuery += " and pi.name in ('处室收文办理') ";
					}else{
						customQuery += " and pi.name in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文') ";
					}					
				}
				
		
		if(customFromItems.length()>0){
			customFromItems += ",";
		}
		customFromItems += "Tjbpmbusiness JBPMBUSINESS";
		if(customQuery.length()>0){
			customQuery += " and ";
		}
		customQuery += " @businessId = JBPMBUSINESS.businessId ";
		customQuery += " and JBPMBUSINESS.businessType in ("+Tjbpmbusiness.getShowableBusinessType()+") ";
		if(excludeWorkflowType != null && !"".equals(excludeWorkflowType) 
				&& !"null".equals(excludeWorkflowType)){
			customQuery += " and pi.typeId not in ("+excludeWorkflowType+") ";
		}
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			customQuery += " and pi.typeId in ("+workflowType+") ";
		}
		paramsMap.put("processSuspend", "0");
		page = manager.getProcessInstanceByConditionForPage(page, itemsList,
				paramsMap, orderMap, customSelectItems, customFromItems, customQuery, null);
		processInstanceDataUtil.getResult(page, adapterBaseWorkflowManager);
		if ("dept".equals(mytype)) {
			// return "monitordeptList";
//			return "monitororgList";
			return "deptmonitorList";
		}
		if ("org".equals(mytype)) {
			return "orgmonitorList";
		}
		if ("user".equals(mytype)) {
			return "usermonitorList";
			// return "monitoruserList";
		}
		return "monitorList";
	}
	
	
	/**
	 * @author 
	 * @createTime 
	 * @description 厅内文件检索
	 */
	@SuppressWarnings( { "unchecked", "deprecation" })
	public String orgFileSearchList() throws Exception {
		ProcessInstanceDataUtil processInstanceDataUtil = new ProcessInstanceDataUtil(
				mytype);
		if (processDefinitionNames != null & !processDefinitionNames.equals("")) {
			processDefinitionNames = java.net.URLDecoder.decode(
					processDefinitionNames, "UTF-8");
		}
		List<String> itemsList = processInstanceDataUtil.getToSelectItems();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		List<Long> instanceIds = new ArrayList<Long>();
		if (processDefinitionNames != null & !processDefinitionNames.equals("")) {
			List<Long> pidList = new LinkedList<Long>();
			String[] pidArray = processDefinitionNames.split(",");
			for (String str : pidArray) {
				pidList.add(Long.valueOf(str));
			}
			paramsMap.put("processDefinitionId", pidList);
		}else{
			Map<String,String> map = getProcessDefinitionNameMapIds();
			if(map != null){
				List<String> ss= new ArrayList(map.values());
				List<Long> pdIds = new ArrayList<Long>();
				for(String str:ss){
					String[] temp = str.split(",");
					for(String strtemp:temp){
						pdIds.add(Long.valueOf(strtemp));
					}
				}
				paramsMap.put("processDefinitionId", pdIds);
			}else{
				if(mytype != null && !"".equals(mytype)){
									
				}else{
					paramsMap.put("processDefinitionId", Long.MAX_VALUE);
				}
			}
		}
		if ((workflowName != null && !"".equals(workflowName))
				|| (day != null && !"".equals(day))) {
			instanceIds = basemanager
					.findDefinitionByHandler(workflowName, day);
			System.out.println();
			if (instanceIds == null || instanceIds.isEmpty()) {
				page.setTotalCount(0);
				if ("org".equals(mytype)) {
					return "orgFileSearchList";
				}
				return "orgFileSearchList";
			} else {
					paramsMap.put("processInstanceId", instanceIds);
			}
		}
		
		//流程名称
		if (processWorkflowNames != null && !"".equals(processWorkflowNames)) {
			paramsMap.put("processName", "%"+processWorkflowNames+"%");
		}
		//标题,业务名称
		if (processName != null && !"".equals(processName)) {
			//processName = URLDecoder.decode(processName, "utf-8");
			paramsMap.put("businessName", "%"+processName+"%");
		}
		//主办人员
		if (startUserName != null && !"".equals(startUserName)) {
			//startUserName = URLDecoder.decode(startUserName, "utf-8");
			if(mytype == null || "".equals(mytype) || "user".equals(mytype)){
				paramsMap.put("startUserName", "%"+startUserName+"%");
			}
		}
		//启动时间
		if (searchDate != null) {
			searchDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.format(searchDate);
		} else {
			searchDateString = "";
		}
		if (searchDate != null && !"".equals(searchDate)) {
			Date endtime = new Date();
			endtime.setTime(searchDate.getTime());
			endtime.setHours(23);
			endtime.setMinutes(59);
			// paramsMap.put("processStartDateEnd", searchDate);
			paramsMap.put("processStartDateStart", searchDate);
			paramsMap.put("processStartDateEnd", endtime);
		}
		//流程状态
		if (state != null && !"".equals(state)) {
			paramsMap.put("processStatus", state);
		}
		//流程是否超时
		if (timeout != null && !"".equals(timeout)) {
			paramsMap.put("processTimeout", timeout);
		}

		Map<String, String> orderMap = new LinkedHashMap<String, String>();
		orderMap.put("processStartDate", "1");
		orderMap.put("processInstanceId", "1");
		if (isFind != null && isFind.equals("1")) { // 执行查询操作，页面自动跳转到第一页
			page.setPageNo(1);
		}
		User user = userService.getCurrentUser();
		String sql = null;
		if("org".equals(mytype) ){
			if((orgIds != null && !"".equals(orgIds))){
				sql = processInstanceDataUtil.getSql(user.getUserId(), orgIds,startUserName);
			}else{
				sql = processInstanceDataUtil.getSql(user.getUserId(), null,startUserName);
			}
		}else{
			sql = processInstanceDataUtil.getSql(user.getUserId(), user.getOrgId(),startUserName);
		}
		String customQuery = "";
		String customFromItems = "";
		String customSelectItems  = "";
		if(mytype != null && !"".equals(mytype)){
			customSelectItems  = " mainActor.mainActorId, wfbaseprocessfile.rest2 as workflowAliaName ";
			customFromItems  += " com.strongit.oa.bo.TMainActorConfing mainActor, com.strongit.workflow.bo.TwfBaseProcessfile wfbaseprocessfile ";
			
			//如果页面来自领导首页(desktopWhole-bgtDesktopForLeader.jsp)  则查询相应处室
			if(fromLeaderDesktopPage!=null && "1".equals(fromLeaderDesktopPage)){
				String roomId = getRequest().getParameter("roomId");
				if(roomId!=null && !"".equals(roomId)){
					sql += " and org.orgId = '" + roomId + "'";
				}				
			}
			
			customQuery = "mainActor.processInstanceId = "+DataBaseUtil.SqlNumberToChar("@processInstanceId")+" and pi.startUserId in ("+sql+")  AND  wfbaseprocessfile.pfName =  pi.name ";
			if("1".equals(state)){//办结文取本年度的
			    	int doneyear = Integer.MIN_VALUE;
			    if(doneYear == null || "null".equals(doneYear)){
			    	doneYear = "-1";
			    }
				if (doneYear != null && !"".equals(doneYear)
						&& !"null".equals(doneYear)) {
					if ("-1".equals(doneYear)) {
						java.util.Calendar Cal = java.util.Calendar
								.getInstance();
						doneyear = Cal.get(java.util.Calendar.YEAR);
					} else {
						doneyear = Integer.parseInt(doneYear);
					}
					customQuery += " and "
							+ DataBaseUtil.SqlYearOfDate("pi.end") + "="
							+ doneyear + " ";
				}
			}
		}else{
			customQuery +=" exists(select ti.id from org.jbpm.taskmgmt.exe.TaskInstance ti join ti.pooledActors pa where "
				+ "pa.actorId in("
				+ sql
				+ ")"
				+ " and ti.processInstance.id = @processInstanceId)";
		}
		customQuery +=" and pi.name not like '自办文' ";
		
		
				//如果页面来自领导首页(desktopWhole-bgtDesktopForLeader.jsp)  办结文件则查询本周
				if(fromLeaderDesktopPage!=null && "1".equals(fromLeaderDesktopPage)){
					String isFenban = getRequest().getParameter("isFenban");
					//计算上周起止日期
					Date now = new Date();
					now.setTime(now.getTime()-7*24*60*60*1000);//上周
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(now);
					if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
						now.setTime(now.getTime()-24*60*60*1000);
						calendar.setTime(now);
					}
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					Date currentWeekStartDate = calendar.getTime();
					currentWeekStartDate.setHours(0);
					currentWeekStartDate.setMinutes(0);
					currentWeekStartDate.setSeconds(0);
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
					Date currentWeekEndDate = calendar.getTime();
					currentWeekEndDate.setHours(23);
					currentWeekEndDate.setMinutes(59);
					currentWeekEndDate.setSeconds(59);
					if(state!=null && "1".equals(state)){				//办结则查询本周   在办则无时间限制							
						paramsMap.put("processEndDateStart", currentWeekStartDate);
						paramsMap.put("processEndDateEnd", currentWeekEndDate);						
					}					
					if(isFenban != null && "1".equals(isFenban)){	//isFenban: 1 表示为"处室收文办理"
						paramsMap.put("processStatus",null);	// 统计已分办 实则统计"处室收文办理"数 忽略"处室收文办理"流程实例的状态
						paramsMap.put("processStartDateStart", currentWeekStartDate);
						paramsMap.put("processStartDateEnd", currentWeekEndDate);	
						customQuery += " and pi.name in ('处室收文办理') ";
					}else{
						customQuery += " and pi.name in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文') ";
					}					
				}
				
		
		if(customFromItems.length()>0){
			customFromItems += ",";
		}
		customFromItems += "Tjbpmbusiness JBPMBUSINESS";
		if(customQuery.length()>0){
			customQuery += " and ";
		}
		customQuery += " @businessId = JBPMBUSINESS.businessId ";
		customQuery += " and JBPMBUSINESS.businessType in ("+Tjbpmbusiness.getShowableBusinessType()+") ";
		//需要过滤掉的流程类型ID
		if(excludeWorkflowType != null && !"".equals(excludeWorkflowType) 
				&& !"null".equals(excludeWorkflowType)){
			customQuery += " and pi.typeId not in ("+excludeWorkflowType+") ";
		}
		//流程类型ID 
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			customQuery += " and pi.typeId in ("+workflowType+") ";
		}
		//流程是否被挂起
		paramsMap.put("processSuspend", "0");
		page = manager.getProcessInstanceByConditionForPage(page, itemsList,
				paramsMap, orderMap, customSelectItems, customFromItems, customQuery, null);
		processInstanceDataUtil.getResult(page, adapterBaseWorkflowManager);
		if ("org".equals(mytype)) {
			return "orgFileSearchList";
		}
		
		return "monitorList";
	}
	
	
	/**
	 * @author 严建
	 * @createTime Aug 17, 2011
	 * @description 导出流程监控记录
	 */
	public String exportfils() throws Exception {
		List<Object[]> processMonitorlist = getProcessMonitorlistInfo();
		try {

			HttpServletResponse response = getResponse();

			// 创建EXCEL对象
			BaseDataExportInfo export = new BaseDataExportInfo();
			String str = ProcessUtil.toUtf8String("流程监控记录");
			export.setWorkbookFileName(str);
			export.setSheetTitle("流程监控记录");
			export.setSheetName("流程监控记录");
			// 描述行信息
			List<String> tableHead = new ArrayList<String>();
			tableHead.add("标题");
			tableHead.add("当前处理人");
			tableHead.add("发起人");
			tableHead.add("启动时间");
			tableHead.add("状态");
			tableHead.add("时间");
			tableHead.add("超期");
			tableHead.add("流程名称");
			export.setTableHead(tableHead);

			// 获取导出信息
			List<Vector<String>> rowList = new ArrayList<Vector<String>>();
			Map rowhigh = new HashMap();
			int rownum = 0;
			for (Object[] obj : processMonitorlist) {
				Vector<String> cols = new Vector<String>();
				cols.add(obj[0] == null ? "" : obj[0].toString());// 标题
				cols.add(obj[7] == null ? "" : obj[7].toString());// 当前处理人
				cols.add(obj[1] == null ? "" : obj[1].toString());// 发起人
				cols.add(obj[3] == null ? "" : obj[3].toString());// 启动时间
				cols.add(obj[4] == null ? "" : obj[4].toString());// 状态
				cols.add(obj[8] == null ? "" : obj[8].toString());// 时间
				cols.add(obj[5] == null ? ""
						: obj[5].toString().equals("0") ? "否" : "是");// 超期
				cols.add(obj[6] == null ? "" : obj[6].toString());// 超期
				rowList.add(cols);
				rownum++;
			}
			export.setRowList(rowList);
			export.setRowHigh(rowhigh);
			ProcessXSL xsl = new ProcessXSL();
			xsl.createWorkBookSheet(export);
			xsl.writeWorkBook(response);

		} catch (Exception e) {

			e.printStackTrace();

		}
		return null;
	}

	/**
	 * @author 严建
	 * @createTime Aug 17, 2011
	 * @description 获取流程监控记录
	 */
	@SuppressWarnings("deprecation")
	private List<Object[]> getProcessMonitorlistInfo() {
		if (processDefinitionNames != null) {
			try {
				processDefinitionNames = java.net.URLDecoder.decode(
						processDefinitionNames, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		List<Object[]> list = null;
		try {
			if (!"".equals(proName) && null != proName) {
				proName = java.net.URLDecoder.decode(proName, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<String> itemsList = new ArrayList<String>();
		itemsList.add("businessName");
		itemsList.add("startUserName");
		itemsList.add("processInstanceId");
		itemsList.add("processStartDate");
		itemsList.add("processEndDate");
		itemsList.add("processTimeout");
		itemsList.add("processName");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if (processId != null && !"".equals(processId)) {
			// 设置流程定义Id
			String[] processIds = processId.split(",");
			List<Long> pdIds = new ArrayList<Long>();
			for (int i = 0; i < processIds.length; i++) {
				pdIds.add(new Long(processIds[i]));
			}
			paramsMap.put("processDefinitionId", pdIds);
		} else {// 默认显示所有流程定义下的流程实例
			List<Object[]> newtree = definitionService
					.getMonitorProcessDefinitionList();
			if (newtree != null && !newtree.isEmpty()) {
				List<Long> pdIds = new ArrayList<Long>();
				for (Object[] objs : newtree) {
					Long processDefinitionId = (Long) objs[0];
					pdIds.add(processDefinitionId);
					String processDefinitionName = objs[1].toString();
					if (processDefinitionNameMapIds == null) {
						processDefinitionNameMapIds = new HashMap<String, String>();
					}
					if (!processDefinitionNameMapIds
							.containsKey(processDefinitionName)) {
						processDefinitionNameMapIds.put(processDefinitionName,
								processDefinitionId.toString());
					} else {
						StringBuilder pid = new StringBuilder();
						if (processDefinitionNameMapIds
								.get(processDefinitionName) != null) {
							pid.append(processDefinitionNameMapIds
									.get(processDefinitionName));
						}
						pid.append(",");
						pid.append(processDefinitionId.toString());
						processDefinitionNameMapIds.put(processDefinitionName,
								pid.toString());
					}
				}
				if (processDefinitionNames != null
						& processDefinitionNames != "") {
					List<Long> pidList = new LinkedList<Long>();
					String[] pidArray = processDefinitionNames.split(",");
					for (String str : pidArray) {
						pidList.add(Long.valueOf(str));
					}
					pdIds = pidList;
				}
				paramsMap.put("processDefinitionId", pdIds);
			}
		}
		if ((workflowName != null && !"".equals(workflowName))
				|| (day != null && !"".equals(day))) {
			List<Long> instanceIds = basemanager.findDefinitionByHandler(
					workflowName, day);
			paramsMap.put("processInstanceId", instanceIds);
		}
		try {
			if (processName != null && !"".equals(processName)) {
				paramsMap.put("businessName", "%"+processName+"%");
			}
			if (startUserName != null && !"".equals(startUserName)) {
				startUserName = URLDecoder.decode(startUserName, "utf-8");
				paramsMap.put("startUserName", startUserName);
			}
			if (searchDate != null) {
				searchDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format(searchDate);
			} else {
				searchDateString = "";
			}
			if (searchDate != null && !"".equals(searchDate)) {
				Date endtime = new Date();
				endtime.setTime(searchDate.getTime());
				endtime.setHours(23);
				endtime.setMinutes(59);
				paramsMap.put("processStartDateStart", searchDate);
				paramsMap.put("processStartDateEnd", endtime);
			}
			if (state != null && !"".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			if (timeout != null && !"".equals(timeout)) {
				paramsMap.put("processTimeout", timeout);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("processStartDate", "1");

		list = manager.getProcessInstanceByConditionForList(itemsList,
				paramsMap, orderMap, "", "", "", null);
		if (list != null) {
			List<Object[]> result = list;
			if (result != null && !result.isEmpty()) {
				for (int i = 0; i < result.size(); i++) {
					Object[] objs = result.get(i);
					Object endDate = objs[4];// 流程实例介绍时间
					objs = ObjectUtils.addObjectToArray(objs, "");
					objs = ObjectUtils.addObjectToArray(objs, "");
					if (endDate == null) {// 流程未结束
						Long instanceId = (Long) objs[2];
						Object[] returnObjs = adapterBaseWorkflowManager.getWorkflowService()
								.getProcessStatusByPiId(instanceId.toString());// 得到此流程实例下的运行情况
						Collection col = (Collection) returnObjs[6];// 处理任务信息
						if (col != null && !col.isEmpty()) {
							StringBuilder strUserName = new StringBuilder();// 人员姓名
							StringBuilder strNodeName = new StringBuilder();// 节点名称
							StringBuilder nodeEnterTime = new StringBuilder();// 节点进入时间
							for (Iterator it = col.iterator(); it.hasNext();) {
								Object[] itObjs = (Object[]) it.next();
								String userId = (String) itObjs[3];
								String taskFlag = (String) itObjs[0];
								Date nodeEnter = (Date) itObjs[2];
								Date now = new Date();
								long day = DateCountUtil.getDistDates(
										nodeEnter, now);
								StringBuilder userName = new StringBuilder();
								if (userId != null && !"".equals(userId)) {
									String[] userIds = userId.split(",");
									for (String id : userIds) {
										userName
												.append(
														userService
																.getUserNameByUserId(id))
												.append(",");
									}
									userName
											.deleteCharAt(userName.length() - 1);
								}
								if (userName.length() > 0) {
									strUserName.append("[").append(userName)
											.append("]");
								}
								if ("subProcess".equals(taskFlag)) {// 子流程节点
									strNodeName.append("[").append(
											(String) itObjs[1]).append("]");
								} else {// 任务节点
									strNodeName.append("[").append(
											(String) itObjs[1]).append("]");
								}
								if (nodeEnterTime.length() == 0) {
									nodeEnterTime.append(day).append("天未办理");
								}
							}
							objs[4] = strNodeName.toString();
							objs[7] = strUserName.toString();
							objs[8] = nodeEnterTime.toString();
						} else {
							objs[4] = "办毕";
						}
					} else {
						objs[4] = "办毕";
					}
					result.set(i, objs);
				}
			}
		}
		return list;
	}

	/**
	 * 退回任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午07:09:51
	 * @param taskId
	 *            -任务实例Id
	 * @param formId
	 *            -表单Id
	 * @param formData
	 *            -电子表单数据
	 * @param returnNodeId
	 *            需要退回到的目标节点
	 * @return ret 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现异常返回-2；
	 *         4、出现系统异常返回-3,一般是电子表单数据未获取到引起
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String back() throws Exception {
		String ret = "0";
		try {
			if (StringUtils.hasLength(taskId)) {
				ActionContext cxt = ActionContext.getContext();
				String content = "";
				if (suggestion != null && !"".equals(suggestion)) {
					suggestion = java.net.URLDecoder
							.decode(suggestion, "UTF-8");
					JSONObject jsonObj = JSONObject.fromObject(suggestion);
					content = jsonObj.getString("suggestion");
					remindType = jsonObj.getString("remindType");
				}
				cxt.getSession().put("remindType", remindType);
				cxt.getSession().put("handlerMes", content);
				cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
				String returnNodeId = getRequest().getParameter("returnNodeId");
				JSONObject json = new JSONObject();
				json.put("suggestion", content + "     ");// 增加5个空格，以标示回退意见
				json.put("CAInfo", "");
				List list = taskService.getCurrentHandleByNode(instanceId,
						nodeId);// 得到节点处理人信息
				String userId = userService.getCurrentUser().getUserId();
				if (list != null && !list.isEmpty()) {
					Object[] info = (Object[]) list.get(0);
					userId = info[4].toString();

					/**
					 * 流程监控中退回，如果节点上存在多个处理人信息时，将userid置空
					 * 解决admin在流程监控时，从签收节点退回时，报异常 modify 严建 2011-12-19 13:15
					 */
					if (userId.length() > 32) {
						userId = null;
					}
					logger.error("处理人信息：" + info[1] + ";处理人id为："
							+ info[4].toString());
				}
				// getManager().backSpace(taskId, returnNodeId, formId,
				// json.toString(), formData,new
				// OALogInfo("任务退回，taskId="+taskId));
				adapterBaseWorkflowManager.getWorkflowService().goToNextTransition(taskId,
						WorkflowConst.WORKFLOW_TRANSITION_HUITUI, returnNodeId,
						"0", formId, null, json.toString(), userId, null);
			} else {
				ret = "-1";
			}
		} catch (SystemException e) {
			logger.error(e.getMessage());
			ret = "-3";
		} catch (Exception ex) {
			logger.error("退回任务时出现异常,异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}

	/**
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() {

	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@Override
	public String save() throws Exception {

		return RELOAD;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911402EE
	 */
	@Override
	public String delete() throws Exception {
		try {
			String ids = getRequest().getParameter("ids");
			manager.delProcessInstances(ids);
			addActionMessage("信息删除成功");
			
			/*
			 * 删除流程实例时，把关联流程的流程实例删除
			 */
			String[] piIds = ids.split(",");
			for(String piId : piIds)
			{
				Long id = Long.valueOf(piId);
				rwSrv.deleteRelativeWorkflowByPiId(id);
			}
			
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list();
	}

	public String processUtil() throws Exception {
		try {
			transitionName = URLDecoder.decode(transitionName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// manager.processUtil(Long.valueOf(instanceId), transitionName);
		logger.info("sucess.......");
		return list();
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	@Override
	public String input() {
		model = manager.getProcessInstanceMonitorData(Long.valueOf(instanceId));
		this.getRequest().setAttribute("token", String.valueOf(model[3]));

		if (!"".equals(proName) && null != proName) {
			try {
				proName = java.net.URLDecoder.decode(proName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				proName = "";
			}
		}

		return INPUT;
		// try {
		// model = manager.getPDMonitorData(Long.valueOf(instanceId));
		// Object obj = model[1];
		// if(obj instanceof List){
		// List<TaskInstanceBean> lst = (List<TaskInstanceBean>)obj;
		// for(TaskInstanceBean bean:lst){
		// String taskActor = bean.getTaskActor();
		// if(taskActor!=null && !"".equals(taskActor)){
		// bean.setTaskActor(uupInterface.getUsernameById(taskActor));
		// }
		// lst.set(lst.indexOf(bean), bean);
		// }
		// }
		// this.getRequest().setAttribute("token", String.valueOf(model[3]));
		//			
		// if(!"".equals(proName)&&null!=proName){
		// proName = java.net.URLDecoder.decode(proName, "utf-8");
		// }
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// return INPUT;
	}

	/**
	 * 异步得到当前流程所处环节任务ID 多个任务id以逗号组成的字符串返回，若无任务id则直接返回空.
	 * 
	 * @author:邓志城
	 * @date:2010-10-20 下午03:11:42
	 * @return
	 */
	public String findCurrentTaskWithSubProcess() {
		StringBuilder taskIdStr = new StringBuilder();
		try {
			List<Object[]> subProcessList = taskService
					.getSubProcessesByPiIdAndNodeId(instanceId, nodeId);// 根据节点id以及实例ID获得所有子流程
			if (subProcessList != null && !subProcessList.isEmpty()) {
				Object[] objs = subProcessList.get(0);
				instanceId = objs[0].toString();
				Object[] objInfo = adapterBaseWorkflowManager.getWorkflowService()
						.getProcessStatusByPiId(objs[0].toString());// 根据子流程实例id获得流程运行情况
				Collection col = (Collection) objInfo[6];
				if (col != null && !col.isEmpty()) {// 遍历运行情况，获得是否运行的子流程是否还有子流程
					for (Iterator it = col.iterator(); it.hasNext();) {
						Object[] subTask = (Object[]) col.iterator().next();
						String flag = subTask[0].toString();
						if ("task".equals(flag)) {
							nodeName = subTask[1].toString();
							break;
						}
					}

				}
			}
			if (nodeName == null) {// 子流程还有子流程
				logger.info("当前子流程正运行于子流程节点，不允许驳回！");
				return this.renderText(taskIdStr.toString());
			}
			List<String> toSelectTtems = new ArrayList<String>(1);
			toSelectTtems.add("taskId");
			Map<String, Object> params = new HashMap<String, Object>(3);
			params.put("processInstanceId", instanceId);
			params.put("taskNodeName", nodeName);
			params.put("taskType", "2");// 处理方法与退回的findCurrentTask类似
			List list = getManager().getTaskInfosByConditionForList(
					toSelectTtems, params, null, null, null, null, null);
			if (list != null && !list.isEmpty()) {
				/*
				 * String taskStr =
				 * org.apache.commons.lang.StringUtils.join(list, ",");
				 * System.out.println("taskStr:" + taskStr);
				 */
				for (int i = 0; i < list.size(); i++) {
					Object objTaskId = list.get(i);
					if (objTaskId != null) {
						taskIdStr.append(objTaskId).append(",");
					}
				}
				if (taskIdStr.length() > 0) {
					taskIdStr = taskIdStr.deleteCharAt(taskIdStr.length() - 1);
				}
			}
		} catch (Exception e) {
			logger.error("得到当前所处节点的任务时发生异常", e);
			taskIdStr.append(e.toString());
		}
		logger.info("当前任务节点id为：" + taskIdStr.toString());
		return this.renderText(taskIdStr.toString());
	}

	/**
	 * 根据节点ID得到当前节点类型
	 * 
	 * @author:hecj
	 * @date:2011-8-11 下午05:22:42
	 * @return
	 */
	public String getTypeByNodeId() {
		String nodeType = "";
		try {
			nodeType = getManager().findTypeByNodeId(nodeId);
		} catch (Exception e) {
			logger.error("得到当前所处节点类型时发生异常", e);
		}
		return this.renderText(nodeType);
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	@SuppressWarnings("unchecked")
	public String processView() {
		if (!"".equals(proName) && null != proName) {
			try {
				proName = java.net.URLDecoder.decode(proName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				proName = "";
			}
		}
		model = adapterBaseWorkflowManager.getWorkflowService().getProcessStatusByPiId(instanceId);
		Collection<Object> col = (Collection<Object>) model[6];
		Object[] objs = col.toArray();
		// System.out.println(objs.length);
		for (int i = 0; i < objs.length; i++) {
			Object[] o = (Object[]) objs[i];

			if (o[0].toString().equals("task")) {
				o[0] = "任务";
			} else {
				o[0] = "子流程";
			}
			java.text.DateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			o[2] = format.format(o[2]);

			String ids = o[3].toString();
			String[] id = ids.split(",");
			String name = "";
			if (!"".equals(ids)) {
				for (int j = 0; j < id.length; j++) {
					if (j == id.length - 1) {
						name = name + userService.getUserNameByUserId(id[j]);
					} else {
						name = name + ","
								+ userService.getUserNameByUserId(id[j]);
					}
				}
			} else {
				if ("子流程".equals(o[0])) {
					List<TwfInfoProcessLog> lst = manager
							.getMonitorProcessLogs(Long.valueOf(instanceId));
					if (lst != null && lst.size() > 0) {
						TwfInfoProcessLog info = lst.get(lst.size() - 1);
						name = info.getPlActorname();
					}
				}
			}
			List list = Arrays.asList(o);
			List arrayList = new ArrayList(list);

			arrayList.add(name);

			objs[i] = arrayList.toArray();
			// System.out.println(arrayList.toArray().length);
		}
		// Collection<Object> dd = (Collection<Object>)model[6];
		model[6] = Arrays.asList(objs);
		return "processView";
	}

	/**
	 * 流程监控中流程定义树型列表
	 * 
	 * @author 喻斌
	 * @date Dec 15, 2008 1:34:09 PM
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public String tree() throws Exception {

		// Object[]{(0)流程定义Id, (1)流程名称, (2)流程定义版本, (3)流程类型Id, (4)流程类型名称}
		// treeList = definitionService.getMonitorProcessDefinitionList();
		// return "tree";

		Map<String, String> tempMap = new HashMap<String, String>();
		Map<String, Object[]> treeMap = new HashMap<String, Object[]>();
		List<Object[]> newtree = definitionService
				.getMonitorProcessDefinitionList();
		Object[] defin, tempDefin;
		String defName = "";
		for (int i = 0; i < newtree.size(); i++) {
			defin = newtree.get(i);
			defName = defin[1].toString();
			if (tempMap.get(defName) == null) {
				tempMap.put(defName, defin[2].toString());// tempmap-->{(1)流程名称:(2)流程定义版本}
				treeMap.put(defName, defin); // treeMap-->{(1)流程名称: 流程定义信息对象}
			} else {
				if (Integer.parseInt(tempMap.get(defName)) > Integer
						.parseInt(defin[2].toString())) {
					tempDefin = treeMap.get(defName);
					tempDefin[0] = tempDefin[0] + "," + defin[0];
					treeMap.put(defName, tempDefin);
				} else {
					tempDefin = treeMap.get(defName);
					defin[0] = tempDefin[0] + "," + defin[0];
					tempMap.put(defName, defin[2].toString());
					treeMap.put(defName, defin);
				}

			}
		}
		treeList = new ArrayList();
		treeList.addAll(treeMap.values());
		return "tree";
	}

	/**
	 * 转移到流程监控主界面
	 * 
	 * @author 喻斌
	 * @date Dec 15, 2008 2:06:25 PM
	 * @return
	 * @throws Exception
	 */
	public String mainFrame() throws Exception {
		return "mainFrame";
	}

	public String mainFrameOrg() throws Exception {
		return "mainFrameOrg";
	}

	/**
	 * 流程监控中改变流程状态（jquery使用）
	 * 
	 * @author 喻斌
	 * @date Dec 15, 2008 3:55:01 PM
	 * @return
	 * @throws Exception
	 */
	public String status() throws Exception {
		String processId = this.getRequest().getParameter("processId");
		String flag = this.getRequest().getParameter("flag");
		manager.changeProcessInstanceStatus(processId, flag);
		return this.renderText("状态修改成功！");
	}

	/**
	 * @author:luosy
	 * @description:
	 * @date : 2010-8-2
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String hasForm() throws Exception {
		String[] info = null;
		Object[] objs = definitionService.getFormIdAndBusiIdByPiIdAndNodeId(
				instanceId, nodeId);
		//如果节点挂接表单与流程挂接表单不同
		if(objs[1] != null){
			info = new String[] { (String) objs[1], (String) objs[0] };			
		}else{
			info = new String[] { (String) objs[2], (String) objs[0] };
		}
		bussinessId = info[0] + "";
		formId = info[1];
		if (!"0".equals(bussinessId) && !"null".equals(bussinessId)
				&& !"0".equals(formId)) {
			return this.renderText("yes");
		} else {
			return this.renderText("no");
		}
	}

	/**
	 * 根据任务ID得到展现表单
	 * 
	 * @author luosy StrongOA2.0_DEV 2010-7-13 下午03:24:02
	 * @return
	 * @throws Exception
	 */
	public String viewform() throws Exception {
		// String userName = user.getCurrentUser().getUserName();//当前用户姓名
		String[] info = null;
		Object[] objs = definitionService.getFormIdAndBusiIdByPiIdAndNodeId(
				instanceId, nodeId);
		//如果节点挂接表单与流程挂接表单不同
		if(objs[1] != null){
			info = new String[] { (String) objs[1], (String) objs[0] };			
		}else{
			info = new String[] { (String) objs[2], (String) objs[0] };
		}
		bussinessId = info[0] + "";
		formId = info[1];
		if (!"0".equals(bussinessId) && !"null".equals(bussinessId)
				&& !"0".equals(formId)) {
			if (instanceId != null && !instanceId.equals("")) {
				ContextInstance cxt = adapterBaseWorkflowManager.getWorkflowService()
						.getContextInstance(instanceId);
				businessName = cxt.getProcessInstance().getBusinessName();
				JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
				if(jsonArray != null && !jsonArray.isEmpty()){
					personDemo = jsonArray.toString();
					parentInstanceId = "exists";
				}else{
					String demo = (String) cxt.getProcessInstance().getContextInstance().getVariable("@{personDemo}");
					String parentinstanceId="";
					if(!"".equals(demo)&&null!=demo){
						String[]  demos = demo.split(";");
						if(demos.length>4){
							parentinstanceId = demos[4];
							parentinstanceId = parentinstanceId.split("@")[0];
							
							jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
							personDemo = jsonArray.toString();
							parentInstanceId = "exists";
						}					
					}
				}
			}
			String[] args = bussinessId.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			pkFieldValue = args[2];
		}
		return "formView";
	}
	/**
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Mar 31, 2012 10:47:57 AM
	 */
	public String viewformSelect() throws Exception {
		// String userName = user.getCurrentUser().getUserName();//当前用户姓名
		String[] info = null;
		ProcessInstance processinstance = workflow.getProcessInstanceById(instanceId);
		info = new String[] { processinstance.getBusinessId(), processinstance.getMainFormId()};
		bussinessId = info[0] + "";
		formId = info[1];
		if (!"0".equals(bussinessId) && !"null".equals(bussinessId)
				&& !"0".equals(formId)) {
			if (instanceId != null && !instanceId.equals("")) {
				ContextInstance cxt = adapterBaseWorkflowManager.getWorkflowService()
						.getContextInstance(instanceId);
				businessName = cxt.getProcessInstance().getBusinessName();
				JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
				if(jsonArray != null && !jsonArray.isEmpty()){
					personDemo = jsonArray.toString();
					parentInstanceId = "exists";
				}else{
					String demo = (String) processinstance.getContextInstance().getVariable("@{personDemo}");
					String parentinstanceId="";
					if(!"".equals(demo)&&null!=demo){
						String[]  demos = demo.split(";");
						if(demos.length>4){
							parentinstanceId = demos[4];
							parentinstanceId = parentinstanceId.split("@")[0];
							
							jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
							personDemo = jsonArray.toString();
							parentInstanceId = "exists";
						}					
					}
				}
			}
			String[] args = bussinessId.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			pkFieldValue = args[2];
		}
		return "formView";
	}

	/**
	 * 查看节点上的审批意见.
	 * 
	 * @author:邓志城
	 * @date:2011-2-15 下午02:00:43
	 * @param annalList
	 *            内部数据结构：
	 *            Object[]{(0)任务开始时间,(1)任务结束时间,(2)任务处理人名称,(3)任务处理意见,(4)任务处理时间,(5)任务是否超时,(6)任务处理记录Id}
	 * @return 节点上审批意见列表
	 */
	public String annalList() {
		try {
			annalList = taskService.getHandleRecordByNode(processId, nodeId);
		} catch (Exception e) {
			logger.error("流程监控中查看节点审批意见发生异常.", e);
		}
		return "viewannal";
	}

	/**
	 * 更新处理意见
	 * 
	 * @author:邓志城
	 * @date:2011-2-15 下午03:07:27
	 * @return ret 0:操作成功；-1：发生异常；-2：指定的审批记录不存在。
	 */
	public void saveApproveInfo() {
		String ret = "0";// 操作成功
		try {
			String approveId = getRequest().getParameter("approveId");// 审批记录ID
			String oldApproveContent = getRequest().getParameter("oldContent");// 旧的审批意见

			String approveContent = getRequest().getParameter("content");// 审批记录新内容

			approveContent = approveContent.replaceAll("\\r", "");// 处理审批意见有回车的情况
			approveContent = approveContent.replaceAll("\\n", " ");// 处理审批意见有换行的情况
			String[] FF_String = new String[] { "\'", "\"" };// 特殊字符
			String[] NFF_String = new String[] { "’", "”" };// 替换字符
			for (int i = 0; i < FF_String.length; i++) {
				if (approveContent.indexOf(FF_String[i]) != -1) {
					approveContent = approveContent.replaceAll(FF_String[i],
							NFF_String[i]);
				}

			}

			TwfInfoApproveinfo approveInfo = oaSystemService
					.getApproveInfoById(approveId);
			String userName = userService.getCurrentUser().getUserName();
			StringBuilder logInfo = new StringBuilder("用户[").append(userName)
					.append("]");
			logInfo.append("将审批意见[").append(oldApproveContent).append("]修改成[");
			logInfo.append(approveContent).append("]");
			if (approveInfo != null) {
				approveInfo.setAiContent(approveContent);
				oaSystemService.saveApproveInfo(approveInfo);
				// 记录到日志中
				ToaLog log = new ToaLog();
				String ip = getRequest().getRemoteAddr();
				log.setOpeIp(ip); // 操作者IP地址
				log.setOpeUser(userName); // 操作姓名
				log.setLogState("1"); // 日志状态
				log.setOpeTime(new Date()); // 操作时间
				log.setLogInfo(logInfo.toString());// 日志信息
				logger.error(logInfo.toString());
				log.setLogModule("流程监控");
				logService.saveObj(log);
			} else {
				ret = "-2";// 记录不存在
			}
		} catch (Exception e) {
			logger.error("保存审批意见发生异常", e);
			ret = "-1";
		}
		renderText(ret);
	}

	/**
	 * 根据任务实例Id取消任务信息
	 * 
	 * @author 喻斌
	 * @date 2011-10-9 下午04:08:45
	 * @return
	 * @throws Exception
	 */
	public String cancelTask() throws Exception {
		String taskId = this.getRequest().getParameter("taskId");
		String userName = userService.getCurrentUser().getUserName();
		if (taskId != null && !"".equals(taskId)) {
			this.taskService.cancelTaskByTaskInstanceId(taskId);
			Object[] objs = taskService.getTaskInstanceByTiId(taskId);
			StringBuilder logInfo = new StringBuilder("用户[").append(userName)
					.append("]");
			logInfo.append("取消任务[").append(objs[0]).append("]");
			// 记录到日志中
			ToaLog log = new ToaLog();
			String ip = getRequest().getRemoteAddr();
			log.setOpeIp(ip); // 操作者IP地址
			log.setOpeUser(userName); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo(logInfo.toString());// 日志信息
			logger.error(logInfo.toString());
			log.setLogModule("流程监控");
			logService.saveObj(log);

		}
		return this.renderText("success");
	}

	/**
	 * 根据任务实例Id取消该任务实例所产生的部分并发分支
	 * 
	 * @author 喻斌
	 * @date 2011-10-11 上午11:28:16
	 * @return
	 * @throws Exception
	 */
	public String getConcurrencyTask() throws Exception {
		String taskId = this.getRequest().getParameter("taskId");
		if (taskId != null && !"".equals(taskId)) {
			List<Object[]> lst = this.taskService
					.getCancelConcurrencyTaskByTiId(taskId);
			this.getRequest().setAttribute("lst", lst);

			List<TwfBaseNodesetting> nodeLst = this.oaSystemService
					.getJoinNodesByTaskInstanceId(taskId);
			if (nodeLst != null && !nodeLst.isEmpty()) {
				for (TwfBaseNodesetting node : new ArrayList<TwfBaseNodesetting>(
						nodeLst)) {
					String pluginsConcurrencySet = node
							.getPlugin("plugins_ConcurrencySet");
					if (pluginsConcurrencySet != null
							&& "1".equals(pluginsConcurrencySet)) {// 不显示此会聚节点
						nodeLst.remove(node);
					}
				}
			}
			this.getRequest().setAttribute("nodeLst", nodeLst);
			this.getRequest().setAttribute("taskId", taskId);
		}
		return "cancel";
	}

	/**
	 * 取消并发分支
	 * 
	 * @author 喻斌
	 * @date 2011-10-11 下午03:06:37
	 * @return
	 * @throws Exception
	 */
	public String cancelConcurrencyTask() throws Exception {
		String tokenIdstr = this.getRequest().getParameter("tokenIds");
		String nodeId = this.getRequest().getParameter("nodeId");
		userName = userService.getCurrentUser().getUserName();
		if (tokenIdstr != null && !"".equals(tokenIdstr) && nodeId != null
				&& !"".equals(nodeId)) {
			String[] tokenIds = tokenIdstr.split(",");
			for (String tokenId : tokenIds) {
				this.taskService.moveTokenToNodeAndEnd(tokenId, nodeId);
			}
			TwfBaseNodesetting setting = adapterBaseWorkflowManager.getWorkflowService()
					.getNodesettingByNodeId(nodeId);
			StringBuilder logInfo = new StringBuilder("用户[").append(userName)
					.append("]");
			logInfo.append("取消并发节点[").append(setting.getNsNodeName()).append(
					"]");
			// 记录到日志中
			ToaLog log = new ToaLog();
			String ip = getRequest().getRemoteAddr();
			log.setOpeIp(ip); // 操作者IP地址
			log.setOpeUser(userName); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo(logInfo.toString());// 日志信息
			logger.error(logInfo.toString());
			log.setLogModule("流程监控");
			logService.saveObj(log);
		}
		return this.renderText("success");
	}

	/**
	 * 取消任务实例后刷新页面
	 * 
	 * @author 喻斌
	 * @date 2011-10-9 下午04:40:01
	 * @return
	 * @throws Exception
	 */
	public String refreshMonitorPage() throws Exception {
		String taskId = this.getRequest().getParameter("taskId");
		if (taskId != null && !"".equals(taskId)) {
			Object[] obj = this.manager.getProcessInstanceByTiId(taskId);
			if (obj != null) {
				this.instanceId = String.valueOf(obj[0]);
				return this.input();
			}
		}
		return null;
	}

	public List getTreeList() {
		return treeList;
	}

	public void setTreeList(List treeList) {
		this.treeList = treeList;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		if (processName != null && processName.length() > 0) {
			try {
				processName = URLDecoder.decode(processName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		this.processName = processName;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		if (startUserName != null && startUserName.length() > 0) {
			try {
				startUserName = URLDecoder.decode(startUserName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		this.startUserName = startUserName;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return this.basemanager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return this.workflowType;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public List getAnnalList() {
		return annalList;
	}

	public void setAnnalList(List annalList) {
		this.annalList = annalList;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getProcessDefinitionNames() {
		return processDefinitionNames;
	}

	public void setProcessDefinitionNames(String processDefinitionNames) {
		this.processDefinitionNames = processDefinitionNames;
	}

	public String getSearchDateString() {
		return searchDateString;
	}

	public void setSearchDateString(String searchDateString) {
		this.searchDateString = searchDateString;
	}

	@SuppressWarnings("deprecation")
	public Map<String, String> getProcessDefinitionNameMapIds() {
		List<Object[]> newtree = definitionService
				.getMonitorProcessDefinitionList();
		if (newtree != null && !newtree.isEmpty()) {
			List<Long> pdIds = new ArrayList<Long>();
			for (Object[] objs : newtree) {
				Long processDefinitionId = (Long) objs[0];
				pdIds.add(processDefinitionId);
				String processDefinitionName = objs[1].toString();
				if (processDefinitionNameMapIds == null) {
					processDefinitionNameMapIds = new HashMap<String, String>();
				}
				if (!processDefinitionNameMapIds
						.containsKey(processDefinitionName)) {
					processDefinitionNameMapIds.put(processDefinitionName,
							processDefinitionId.toString());
				} else {
					StringBuilder pid = new StringBuilder();
					if (processDefinitionNameMapIds.get(processDefinitionName) != null) {
						pid.append(processDefinitionNameMapIds
								.get(processDefinitionName));
					}
					pid.append(",");
					pid.append(processDefinitionId.toString());
					processDefinitionNameMapIds.put(processDefinitionName, pid
							.toString());
				}
			}
		}
		return processDefinitionNameMapIds;
	}

	public void setProcessDefinitionNameMapIds(
			Map<String, String> processDefinitionNameMapIds) {
		this.processDefinitionNameMapIds = processDefinitionNameMapIds;
	}

	public String getIsFind() {
		return isFind;
	}

	public void setIsFind(String isFind) {
		this.isFind = isFind;
	}

	public String getFromLeaderDesktopPage() {
		return fromLeaderDesktopPage;
	}

	public void setFromLeaderDesktopPage(String fromLeaderDesktopPage) {
		this.fromLeaderDesktopPage = fromLeaderDesktopPage;
	}

	public String getMytype() {
		return mytype;
	}

	public void setMytype(String mytype) {
		this.mytype = mytype;
	}

	public String getPersonDemo() {
		return personDemo;
	}

	public void setPersonDemo(String personDemo) {
		this.personDemo = personDemo;
	}

	public Map<String, String> getOrgNameMapIds() {
		Map<String,String> OrgNameMapIds = new HashMap<String, String>();
		if ("org".equals(mytype)) {
			List orgInfo = customUserService
					.getManageOrgInfo(userService.getCurrentUser().getUserId());
			try {
				for (Object org : orgInfo) {
					Object[] orgs = (Object[]) org;
					OrgNameMapIds.put(StringUtil.castString(orgs[1]), StringUtil.castString(orgs[0]));
				}
				this.OrgNameMapIds = OrgNameMapIds;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return OrgNameMapIds;
	}

	public void setOrgNameMapIds(Map<String, String> orgNameMapIds) {
		OrgNameMapIds = orgNameMapIds;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		if (orgIds != null & !orgIds.equals("")) {
			try {
				orgIds = java.net.URLDecoder.decode(
						orgIds, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		this.orgIds = orgIds;
	}

	public String getProcessWorkflowNames() {
		return processWorkflowNames;
	}

	public void setProcessWorkflowNames(String processWorkflowNames) {
		if (processWorkflowNames != null & !processWorkflowNames.equals("")) {
			try {
				processWorkflowNames = java.net.URLDecoder.decode(
						processWorkflowNames, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		this.processWorkflowNames = processWorkflowNames;
	}

}