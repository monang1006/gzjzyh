package com.strongit.oa.senddocLeader;

import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bgt.model.ToaYjzx;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.common.workflow.oabo.BackInfoBean;
import com.strongit.oa.senddoc.bo.ParamBean;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.customquery.CustomQueryAdpter;
import com.strongit.oa.senddoc.customquery.CustomQueryUtil;
import com.strongit.oa.senddoc.manager.SendDocBaseManager;
import com.strongit.oa.senddoc.manager.SendDocLinkManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.senddoc.manager.service.ISendDocIcoService;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.senddoc.query.bo.NodeNameQuery;
import com.strongit.oa.senddoc.util.GridViewColumUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.NodeNameConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.po.TaskInstanceBean;
import com.strongit.workflow.util.WorkflowConst;

@Service
@Transactional
@OALogger
public class SendDocLeaderManager extends BaseManager {
	
	@Autowired
	SendDocManager manager;
	
	@Autowired
	private IWorkflowService workflow; // 工作流服务
	
	@Autowired
	private IUserService userService;// 统一用户服务
	
	@Autowired
	private ICustomUserService customUserService;// 统一用户服务
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	公共附件接口
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	SendDocUploadManager sendDocUploadManager;
	
	@Autowired
	SendDocBaseManager sendDocBaseManager;
	
	@Autowired
	private ISendDocIcoService sendDocIcoManager;
	
	@Autowired
	private SendDocLinkManager sendDocLinkManager;
	
	@Autowired
	SessionFactory sessionFactory; // 提供session
	
	
	/**
	 * 得到已办流程列表 过滤掉重复的已办任务
	 * 领导联系人查看领导的已办事宜
	 * @author:
	 * @date:2013-04-24
	 * 
	 * @modifyer:
	 * @description: 添加返回参数
	 * 
	 * 
	 * @param workflowType
	 *            流程类型
	 * @param processStatus
	 *            流程状态 0：未办结流程，1：已办结流程，2：所有流程
	 * @param sortType
	 *            排序方式 例如：SortConst.SORT_TYPE_DATE_ASC
	 * @param filterSign
	 *            是否过滤签收数据 0：否，1：是
	 * @return List<TaskBusinessBean]>
	 * @throws SQLException 
	 * @modify yanjian 2011-08-08 19:00 return List<TaskBusinessBean>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<TaskBusinessBean> getLdProcessedWorkflowForDesktop(
			String processStatus, int num, String sortType, String filterSign) throws SQLException {
		if (filterSign == null || "".equals(filterSign)) {
			filterSign = "0";
		}
		Object[] sItems = { "processName", "processMainFormId",
				"startUserName", "processInstanceId", "businessName",
				"processStartDate", "processEndDate","startUserId" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();

		if (SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC.equals(sortType)) {
			orderMap.put("processStartDate", "0");
		} else {
			orderMap.put("processStartDate", "1");
		}
		String currentid=userService.getCurrentUser().getUserId();
		String userId = userService.getLDId(currentid);
		if(userId==null || userId==""){
			return null;
		}
		StringBuilder customSelectItems = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customQuery = new StringBuilder();
		customSelectItems
				.append("jbpmbusiness.END_TIME,jbpmbusiness.BUSINESS_TYPE,pi.TYPE_ID_,pi.BUSINESS_ID");
		customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
//		 增加按流程类型检索的方式
		HttpServletRequest request = ServletActionContext.getRequest();
		String workflowType = request.getParameter("workflowType");
		if (workflowType != null && workflowType.length() > 0) {
			customQuery.append(" and pi.TYPE_ID_ in(" + workflowType + ")");
		}
		// 增加按流程类型反选检索方式
		String excludeWorkflowType = request
				.getParameter("excludeWorkflowType");
		if (excludeWorkflowType != null && excludeWorkflowType.length() > 0) {
			customQuery.append(" and pi.TYPE_ID_ not in(" + excludeWorkflowType
					+ ")");
		}
		// 增加按流程类型名称反选检索方式,过滤掉该流程类型名称的数据
		String excludeWorkflowTypeName = request
				.getParameter("excludeWorkflowTypeName");
		if (excludeWorkflowTypeName != null && excludeWorkflowTypeName.length() > 0) {
			customQuery.append(" and pi.NAME_ not like '%"
					+ excludeWorkflowTypeName +"%' ");
		}
		
		// paramsMap.put("processSuspend", "0");// 取非挂起任务
		customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
		customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
		ProcessedParameter processedParameter = new ProcessedParameter();
		processedParameter.setFilterSign(filterSign);
		processedParameter.setUserId(userId);
		processedParameter.setCustomFromItems(customFromItems);
		processedParameter.setCustomQuery(customQuery);
		processedParameter.setQueryWithTaskDate(null);
		manager.initProcessedFilterSign(processedParameter);
		logger.info(customFromItems + "\n" + customQuery);
		Page page = new Page(num, true);
		page = workflow.getProcessInstanceByConditionForPage(page,
				toSelectItems, paramsMap, orderMap, customSelectItems
						.toString(), customFromItems.toString(), customQuery
						.toString(), null, null);
		manager.destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
				customSelectItems, customFromItems, customQuery, null, null);
		List dlist = (page.getResult() == null ? new ArrayList() : page
				.getResult());
		List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
		for (int i = 0; i < dlist.size(); i++) {
			Object[] obj = (Object[]) dlist.get(i);
			TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
			taskbusinessbean.setWorkflowName((String) obj[0]);
			taskbusinessbean.setFormId((String) obj[1]);
			taskbusinessbean.setStartUserName((String) obj[2]);
			taskbusinessbean.setInstanceId(obj[3].toString());
			taskbusinessbean.setBusinessName((String) (obj[4]==null?"":obj[4]));
			taskbusinessbean.setWorkflowStartDate((Date) obj[5]);
			taskbusinessbean.setWorkflowEndDate((Date) obj[6]);
			taskbusinessbean.setStartUserId(StringUtil.castString(obj[7]));
			taskbusinessbean.setEndTime(StringUtil.castString(obj[8]));
			taskbusinessbean.setBusinessType(StringUtil.castString(obj[9]));
			taskbusinessbean.setWorkflowType(StringUtil.castString(obj[10]));
			taskbusinessbean.setBsinessId(StringUtil.castString(obj[11]));
			beans.add(taskbusinessbean);
		}

		String[] pfNames = new String[dlist.size()];
		StringBuilder params = new StringBuilder();
		for (int i = 0; i < beans.size(); i++) {
			TaskBusinessBean taskbusinessbean = beans.get(i);
			pfNames[i] = taskbusinessbean.getWorkflowName();
			params.append(taskbusinessbean.getInstanceId()).append(",");
		}
		Map<String, Map> map = null;

		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
			StringBuilder sql = new StringBuilder(
					"select ti.procinst_,ti.id_ as taskId,ti.start_ as taskStartDate ,ti.name_ as taskName ");
			sql.append(" from JBPM_TASKINSTANCE ti where ti.procinst_ in(");
			sql.append(params.toString());
			sql.append(")  AND  TI.ACTORID_='"+processedParameter.getUserId()+"' AND TI.END_ IS NOT NULL  order by ti.END_ desc");
			List<Map<String, Object>> taskList = jdbcTemplate.queryForList(sql
					.toString());
			logger.info(sql.toString());
			if (taskList != null && !taskList.isEmpty()) {
				map = new HashMap<String, Map>();
				for (Map<String, Object> rsMap : taskList) {
					if (!map
							.containsKey(String.valueOf(rsMap.get("PROCINST_")))) {
						map.put(String.valueOf(rsMap.get("PROCINST_")), rsMap);
					}
				}
			}
		}

		Map<String, String> pfNameMapRest2 = sendDocUploadManager.getPfNameMapRest2(pfNames);
		/* 所有的用户信息 */
		Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
		for (int i = 0; i < beans.size(); i++) {
			TaskBusinessBean taskbusinessbean = beans.get(i);
			if(taskbusinessbean.getWorkflowEndDate() != null){
				
			}else{
				String nodeName = StringUtil.castString(map.get(
						taskbusinessbean.getInstanceId()).get("TASKNAME"));
				if("意见征询".equals(nodeName)){
					taskbusinessbean.setCurTaskActorInfo("意见征询");
				}else{
					List[] listTemp = sendDocUploadManager.getUserBeanTempArrayOfProcessStatusByPiId(
							taskbusinessbean.getInstanceId(),
							AllUserIdMapUserBeanTem);
					StringBuilder cruuentUser = new StringBuilder();
					for (int index = 0; index < listTemp[0].size(); index++) {
						if(" ".equals(listTemp[1].get(index).toString())){
							cruuentUser.append(","
									+ listTemp[0].get(index).toString());
						}else{
							cruuentUser.append(","
									+ listTemp[0].get(index).toString() + "("
									+ listTemp[1].get(index).toString() + ")");
						}
					}
					if(cruuentUser.length()>0){
						cruuentUser.deleteCharAt(0);
					}
					taskbusinessbean.setCurTaskActorInfo(cruuentUser.toString());
				}
			}
			String pfName = taskbusinessbean.getWorkflowName();
			String processFileRest2 = pfNameMapRest2.get(pfName);
			if (processFileRest2 == null || "".equals(processFileRest2)) {// 不存在别名是，别名即为流程名
				taskbusinessbean.setWorkflowAliaName(pfName);
			} else {
				taskbusinessbean.setWorkflowAliaName(processFileRest2);
			}
			if (map != null && !map.isEmpty()) {
				taskbusinessbean.setTaskId(map.get(
						taskbusinessbean.getInstanceId()).get("taskId")
						+ "");
			}
		}
		page.setResult(beans);
		return page;
	}
	
	
	/**
	 * 查询用户已办理任务
	 * 
	 * @param page
	 *            分页对象
	 * @param username
	 *            人员名称
	 * @param workflowType
	 *            流程类型
	 * @return com.strongmvc.orm.hibernate.Page
	 * @param businessName
	 *            业务名称（标题）
	 * @param userName
	 *            主办人
	 * @param startDate
	 *            开始时间
	 * @param processTimeout
	 *            是否超时
	 * @param noTotal
	 *            是否统计
	 * @param endDate
	 *            结束时间 内嵌数据对象结构：<br>
	 *            [任务ID,任务创建时间,任务名称,<br>
	 *            流程实例ID,业务创建时间,挂起状态,业务名称,<br>
	 *            发起人,委派人,委派类型（0：委派，1：指派）] *
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getProcessedWorks方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getProcessedWorks(Page page, ProcessedParameter parameter,
			OALogInfo... infos) throws SystemException, ServiceException, SQLException {
		try {

			String workflowType = parameter.getWorkflowType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			String excludeWorkflowTypeName=parameter.getExcludeWorkflowTypeName();
			String businessName = parameter.getBusinessName();
			// String userName = parameter.getUserName();
			Date startDate = parameter.getStartDate();
			Date endDate = parameter.getEndDate();
			String state = parameter.getState();
			String isSuspended = parameter.getIsSuspended();
			Object[] toSelectItems = { "processInstanceId", "businessName",
					"processStartDate", "processName", "processTypeId",
					"processEndDate" };
			List sItems = Arrays.asList(toSelectItems);

			Map<String, Object> paramsMap = new HashMap<String, Object>();
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customSelectItems.append("JBPMBUSINESS.BUSINESS_TYPE");
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ in(" + workflowType
							+ ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ in(" + workflowType
							+ ") ");
				}
			}
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ not in("
							+ excludeWorkflowType + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ not in("
							+ excludeWorkflowType + ") ");
				}
			}
			
			// 过滤掉该流程类型名称的数据		
			if (excludeWorkflowTypeName != null && excludeWorkflowTypeName.length() > 0) {
				customQuery.append(" and pi.NAME_ not like '%"
						+ excludeWorkflowTypeName +"%' ");
			}
			User curUser = userService.getCurrentUser();// 获取当前用户
			String LdId=userService.getLDId(curUser.getUserId());//获取当前用户的领导id
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			if (endDate != null && !"".equals(endDate)) {
				paramsMap.put("processStartDateEnd", endDate);
			}
			if (startDate != null && !"".equals(startDate)) {
				paramsMap.put("processStartDateStart", startDate);
			}
			if (state != null && !"".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			if (isSuspended != null && !"".equals(isSuspended)) {
				paramsMap.put("processSuspend", isSuspended);
			}
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(parameter.getFilterSign());
			processedParameter.setUserId(LdId);
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setQueryWithTaskDate(null);
			processedParameter.setUserIds(parameter.getUserIds());
			initProcessedFilterSign(processedParameter);
			Map orderMap = new LinkedHashMap<Object, Object>();
			orderMap.put("processStartDate", "1");
			page = workflow.getProcessInstanceByConditionForPage(page, sItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			// page = workflow
			// .getProcessInstanceByConditionForPage(page,sItems, paramsMap,
			// orderMap, "", "", "", null);
			List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
			if (page != null && page.getResult() != null) {
				List<String> processInstanceIds = new LinkedList<String>();
				StringBuilder params = new StringBuilder();
				for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[1]));
					taskbusinessbean.setWorkflowStartDate((Date) objs[2]);
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setWorkflowType(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setWorkflowEndDate((Date) objs[5]);
					taskbusinessbean.setBusinessType(StringUtil
							.castString(objs[6]));
					beans.add(taskbusinessbean);
					processInstanceIds.add(taskbusinessbean.getInstanceId());
					params.append(taskbusinessbean.getInstanceId()).append(",");
				}
				Map<String, Map> taskInfoMap = null;
				StringBuilder actorid_SQL = new StringBuilder();
				if(processedParameter.getUserIds() != null){
					List<String> userIds = processedParameter.getUserIds() ; 
					actorid_SQL.append(" TI.ACTORID_ in (");
					for (String userId : userIds) {
						actorid_SQL.append("'").append(userId).append("',");
					}
					actorid_SQL.deleteCharAt(actorid_SQL.length()-1);
					actorid_SQL.append(") ");
				}else{
					actorid_SQL.append(" TI.ACTORID_='").append(processedParameter.getUserId()).append("' ");
				}
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder(
							"SELECT TI.PROCINST_,TI.ID_ AS TASKID,TI.START_ AS TASKSTARTDATE,TI.END_ AS TASKENDDATE,TI.NAME_ AS TASKNAME");
					sql
							.append(" FROM JBPM_TASKINSTANCE TI WHERE TI.PROCINST_ IN(");
					sql.append(params.toString());
					sql
							.append(") AND  ").append(actorid_SQL).append(" AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						taskInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							if (!taskInfoMap.containsKey(String.valueOf(rsMap
									.get("PROCINST_")))) {
								taskInfoMap.put(String.valueOf(rsMap
										.get("PROCINST_")), rsMap);
							}
						}
					}
				}
				/* 所有的用户信息 */
				Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
				for (TaskBusinessBean taskbusinessbean : beans) {
					Map taskMap = taskInfoMap.get(taskbusinessbean
							.getInstanceId());
					if (taskMap == null) {
						continue;
					}
					StringBuilder cruuentUser = new StringBuilder();
					taskbusinessbean.setNodeName(StringUtil.castString(taskMap
							.get("TASKNAME")));
					if("意见征询".equals(taskbusinessbean.getNodeName())){
						cruuentUser.append("意见征询");
					}else{
						if (taskbusinessbean.getWorkflowEndDate() == null) {// 流程未结束
							List[] listTemp = sendDocUploadManager
							.getUserBeanTempArrayOfProcessStatusByPiId(
									taskbusinessbean.getInstanceId(),
									AllUserIdMapUserBeanTem);
							for (int index = 0; index < listTemp[0].size(); index++) {
									if(" ".equals(listTemp[1].get(index).toString())){
										cruuentUser.append(","
												+ listTemp[0].get(index).toString());
									}else{
										cruuentUser.append(","
												+ listTemp[0].get(index).toString() + "["
												+ listTemp[1].get(index).toString() + "]");
									}
							}
							cruuentUser.deleteCharAt(0);
						} else {
							cruuentUser.append("已办结");
						}
					}
					taskbusinessbean.setActorName(cruuentUser.toString());
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
					taskbusinessbean.setTaskEndDate((Date) taskMap
							.get("TASKENDDATE"));
					taskbusinessbean.setTaskStartDate((Date) taskMap
							.get("TASKSTARTDATE"));
					if ("1".equals(parameter.getState())) {
						taskbusinessbean.setMainActorName(workflow
								.getMainActorInfoByProcessInstanceId(
										taskbusinessbean.getInstanceId())
								.getUserName());
					}
				}
				page.setResult(new ArrayList<TaskBusinessBean>(beans));
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取已办工作" });
		}
		return page;
	}
	
	
	
	/**
	 * 得到已办流程列表 过滤掉重复的已办任务
	 * 
	 * @description
	 * @author 严建
	 * @param parameter
	 * @return
	 * @createTime Mar 27, 2012 10:54:07 AM
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getProcessedWorkflow(ProcessedParameter parameter) {
		String workflowType = parameter.getWorkflowType();
		String excludeWorkflowType = parameter.getExcludeWorkflowType();
		String processStatus = parameter.getProcessStatus();
		String filterSign = parameter.getFilterSign();
		if (filterSign == null || "".equals(filterSign)) {
			filterSign = "0";
		}
		Object[] sItems = { "processName", "processTypeId", "processTypeName",
		"processMainFormId" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processSuspend", "0");// 取未挂起任务
//		if (workflowType != null && !"".equals(workflowType)) {
//			paramsMap.put("processTypeId", genWorkflowTypeList(workflowType));
//		}
		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();
		
		String userId = userService.getCurrentUser().getUserId();
		String LdId="";
		try {
			LdId = userService.getLDId(userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder customSelectItems = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customQuery = new StringBuilder();
		customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
		customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
		if (workflowType != null
				&& !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			if (customQuery.length() == 0) {
				customQuery.append("  pi.TYPE_ID_ in(" + workflowType + ") ");
			} else {
				customQuery.append(" and pi.TYPE_ID_ in(" + workflowType + ") ");
			}
		}
		if (excludeWorkflowType != null
				&& !"".equals(excludeWorkflowType)
				&& !"null".equals(excludeWorkflowType)) {
			if (customQuery.length() == 0) {
				customQuery.append("  pi.TYPE_ID_ not in(" + excludeWorkflowType + ") ");
			} else {
				customQuery.append(" and pi.TYPE_ID_ not in(" + excludeWorkflowType + ") ");
			}
		}
		
		ProcessedParameter processedParameter = new ProcessedParameter();
		processedParameter.setFilterSign(filterSign);
		processedParameter.setUserId(LdId);
		processedParameter.setCustomFromItems(customFromItems);
		processedParameter.setCustomQuery(customQuery);
		processedParameter.setQueryWithTaskDate(null);
		initProcessedFilterSign(processedParameter);
		logger.info(customFromItems + "\n" + customQuery);
		List list = workflow.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, orderMap, customSelectItems
				.toString(), customFromItems.toString(), customQuery
				.toString(), null, null);
		destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
				customSelectItems, customFromItems, customQuery, null, null);
		
		Map<String, List<Object[]>> map = null;// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
		List<Object[]> workflowList = new ArrayList<Object[]>();
		if (list != null && !list.isEmpty()) {
			for (Object object : list) {
				if (map == null) {
					map = new HashMap<String, List<Object[]>>();
				}
				Object[] objs = (Object[]) object;
				if (!map.containsKey(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
					workflowSet.add(objs);
					map.put(objs[0].toString() + "$" + objs[1].toString(),
							workflowSet);
				} else {
					map.get(objs[0].toString() + "$" + objs[1].toString()).add(
							objs);
				}
			}
			List<String> checkList = null;// 处理重复的记录
			for (Object object : list) {
				if (checkList == null) {
					checkList = new LinkedList<String>();
				}
				Object[] objs = (Object[]) object;
				if (!checkList.contains(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List l = map.get(objs[0].toString() + "$"
							+ objs[1].toString());
					if (l != null) {
						workflowList.add(new Object[] { objs[0], objs[1],
								objs[2], objs[3], l.size() });
						checkList.add(objs[0].toString() + "$"
								+ objs[1].toString());
					}
				}
			}
			checkList = null;
			map = null;
			list = null;
		}
		return workflowList;
	}

	
	/**
	 * 生成已办流程类型别名链接
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 3:32:47 PM
	 * @return StringBuilder
	 */
	public StringBuilder genProcessedWorkflowNameLink(String workflowName,
			String formId, String workflowType, String processStatus,
			String rootPath, String filterSign) throws DAOException,
			ServiceException, SystemException {
		try {
			if (workflowName == null || "".equals(workflowName)) {
				logger.error("参数workflowName不能为空或空字符串");
				throw new ServiceException("参数workflowName不能为空或空字符串");
			}
			if (formId == null || "".equals(formId)) {
				logger.error("参数formId不能为空或空字符串");
				throw new ServiceException("参数formId不能为空或空字符串");
			}
			/*
			 * if (workflowType == null || "".equals(workflowType)) {
			 * logger.error("参数workflowType不能为空或空字符串"); throw new
			 * ServiceException("参数workflowType不能为空或空字符串"); }
			 */StringBuilder clickProcessType = new StringBuilder();
			String currworkflowName = URLEncoder.encode(URLEncoder.encode(
					workflowName, "utf-8"), "utf-8");
			// 获取表单id
			String[] nodeInfo = formId.split(",");
			formId = nodeInfo[nodeInfo.length - 1];
			// 流程类别
			String currworkflowType = workflowType;
			String clickProcessUrl = rootPath
					+ "/senddocLeader/sendDocLeader!processed.action" + "?state="
					+ processStatus + "&workflowName=" + currworkflowName
					+ "&filterSign=" + filterSign 
					+ "&formId=" + formId
					+ "&searchType=" + (processStatus.equals("1") ? "1" : "")
					+ "&workflowType=" + currworkflowType;

			clickProcessType
					.append("window.parent.refreshWorkByTitle('"
							+ clickProcessUrl
							+ "','"
							+ (processStatus != null
									&& processStatus.equals("1") ? "办结文件"
									: "在办文件") + "');");

			return clickProcessType;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 查询已办流程 通过流程状态 区分已办结流程和未办结流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-18 上午09:42:53
	 * @param page
	 *            分页对象
	 * @param parameter		封装参数
	 * @return Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<EFormComponent>,表名称]
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<EFormComponent>,表名称]
	 * @throws Exception
	 * @modify yanjian 2011-09-28 添加来文或办文排序处理
	 * @modify shenyl  2012-03-09 该方法修改，则getRepeal也须修改，两方法不同之处在于pi.ISSUSPENDED_值不同
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getProcessed(Page page, ProcessedParameter parameter)
			throws Exception {
		try {

			if (parameter.getFilterSign() == null
					|| "".equals(parameter.getFilterSign())) {
				parameter.setFilterSign("0");
			}
			
			// 增加工作流中的显示字段
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customSelectItems.append("jbpmbusiness.END_TIME,jbpmbusiness.BUSINESS_TYPE");
//			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS,T_MAINA_CORCONFING MAINACTOR");
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			Object[] sItems = { "startUserName", "processMainFormBusinessId",
					"processName", "processInstanceId", "businessName",
					"processStartDate", "processEndDate" ,"startUserId"};
			if(!"1".equals(parameter.getIsSuspended()) && parameter.getProcessStatus() !=null && !"1".equals(parameter.getProcessStatus())){
				NodeNameQuery nodenamequery = new NodeNameQuery();
				if(NodeNameConst.BAN_JIE.equals(parameter.getNodeName())){
				}else if(NodeNameConst.YI_JIAN_ZHENG_XUN.equals(parameter.getNodeName())){
					parameter.setNodeName("");
					parameter.setFilterYJZX("1");
				}
				if(parameter.getNodeName() != null && !"".equals(parameter.getNodeName())
						&& !"null".equals(parameter.getWorkflowType())){
					nodenamequery.setNodeName(parameter.getNodeName());
				}
				if(parameter.getWorkflowType() != null
						&& !"".equals(parameter.getWorkflowType())
						&& !"null".equals(parameter.getWorkflowType())){
					nodenamequery.setProcessTypeId(parameter.getWorkflowType());
				}
				customFromItems.append(","+nodenamequery.getCustomFromItem());
				customSelectItems.append(","+nodenamequery.getCustomSelectItem());
				customQuery.append(" and "+nodenamequery.getCustomQuery());
			}
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if("1".equals(parameter.getIsSuspended())){//是否挂起
				if (customQuery.length() == 0) {
					customQuery.append("  pi.ISSUSPENDED_ = 1 ");
				} else {
					customQuery.append(" and pi.ISSUSPENDED_ = 1 ");
				}
			}else{
				if (customQuery.length() == 0) {
					customQuery.append("  pi.ISSUSPENDED_ = 0 ");
				} else {
					customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
				}
			}
			if("1".equals(parameter.getFilterYJZX())){
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.BUSINESS_TYPE = '").append(Tjbpmbusiness.BUSINESS_TYPE_YJZX).append("' ");
				} else {
					customQuery.append(" and jbpmbusiness.BUSINESS_TYPE = '").append(Tjbpmbusiness.BUSINESS_TYPE_YJZX).append("' ");
				}
			}else {
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
				} else {
					customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
				}
			}
			
			
			if (parameter.getProcessStatus() != null
					&& !"".equals(parameter.getProcessStatus())
					&& !"2".equals(parameter.getProcessStatus())) {
				paramsMap.put("processStatus", parameter.getProcessStatus());
			}
			if (parameter.getWorkflowType() != null
					&& !"".equals(parameter.getWorkflowType())
					&& !"null".equals(parameter.getWorkflowType())) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ in(" + parameter.getWorkflowType() + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ in(" + parameter.getWorkflowType() + ") ");
				}
			}
			if (parameter.getExcludeWorkflowType() != null
					&& !"".equals(parameter.getExcludeWorkflowType())
					&& !"null".equals(parameter.getExcludeWorkflowType())) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ not in(" + parameter.getWorkflowType() + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ not in(" + parameter.getWorkflowType() + ") ");
				}
			}
			if (parameter.getWorkflowName() != null
					&& !"".equals(parameter.getWorkflowName())) {
				if (customQuery.length() == 0) {
					customQuery.append(" pi.NAME_ like '"
							+ parameter.getWorkflowName() + "' ");
				} else {
					customQuery.append("and pi.NAME_ like '"
							+ parameter.getWorkflowName() + "' ");
				}
			}
			if (null != parameter.getUserName()
					&& !"".equals(parameter.getUserName())) {//流程发起人名称
				paramsMap.put("startUserName", parameter.getUserName());
			}
			
//			if(null != parameter.getUserId() && !"".equals(parameter.getUserId())){//主办人员id
//				if (customQuery.length() == 0) {
//					customQuery.append(" MAINACTOR.MAIN_ACTORID = '"
//							+ parameter.getUserId() + "' ");
//				} else {
//					customQuery.append("and MAINACTOR.MAIN_ACTORID = '"
//							+ parameter.getUserId() + "' ");
//				}
//			}
//			if (customQuery.length() == 0) {
//				customQuery.append("  MAINACTOR.PROCESSINSTANCE_ID = "+DataBaseUtil.SqlNumberToChar("@processInstanceId")+" ");
//			} else {
//				customQuery.append(" and MAINACTOR.PROCESSINSTANCE_ID = "+DataBaseUtil.SqlNumberToChar("@processInstanceId")+" ");
//			}
			Map orderMap = new HashMap<Object, Object>();
			// “在办事宜”、“收文处理”和“发文处理”中的列表按本人签批时间排序。（严建）
			if (SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC.equals(parameter
					.getSortType())) {
				orderMap.put("processStartDate", "0");
			}else if(SortConst.SORT_TYPE_PROCESSSTARTDATE_ASC.equals(parameter
					.getSortType())){
				orderMap.put("processStartDate", "1");
			} else {
				orderMap.put("processStartDate", "1");
			}
			String userId = userService.getCurrentUser().getUserId();
			String LdId=userService.getLDId(userId);
			String queryWithTaskDate = "";//按任务结束时间区间范围查询
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if (parameter.getStartDate() != null) {
				queryWithTaskDate = " and ti.END_  >= TO_DATE('"
						+ sdf.format(parameter.getStartDate())
						+ "', 'YYYY-MM-DD HH24:MI')";
			}
			if (parameter.getEndDate() != null) {
				queryWithTaskDate = " and ti.END_  <= TO_DATE('"
						+ sdf.format(parameter.getEndDate())
						+ "', 'YYYY-MM-DD HH24:MI')";
			}
			
			String businessName = ServletActionContext.getRequest()
			.getParameter(WORKFLOW_TITLE);
			if (businessName != null && !"".equals(businessName)) {
				if (customQuery.length() == 0) {
					customQuery
					.append(" pi.BUSINESS_NAME_ like '%" + businessName + "%' ");
				} else {
					customQuery.append("and pi.BUSINESS_NAME_ like '%" + businessName
							+ "%' ");
				}
			}
			//收文编号
			String RECV_NUM = ServletActionContext.getRequest().getParameter("RECV_NUM");
			if(RECV_NUM != null && !"".equals(RECV_NUM)){
				if (customQuery.length() == 0) {
					customQuery
					.append(" jbpmbusiness.RECV_NUM like '%" + RECV_NUM + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.RECV_NUM like '%" + RECV_NUM
							+ "%' ");
				}
			}
			//来文字号
			String DOC_NUMBER = ServletActionContext.getRequest().getParameter("DOC_NUMBER");
			if(DOC_NUMBER != null && !"".equals(DOC_NUMBER)){
				if (customQuery.length() == 0) {
					customQuery
					.append(" jbpmbusiness.DOC_NUMBER like '%" + DOC_NUMBER + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.DOC_NUMBER like '%" + DOC_NUMBER
							+ "%' ");
				}
			}
			//来文单位
			String ISSUE_DEPART_SIGNED  = ServletActionContext.getRequest().getParameter("ISSUE_DEPART_SIGNED");
			if(ISSUE_DEPART_SIGNED != null && !"".equals(ISSUE_DEPART_SIGNED)){
				if (customQuery.length() == 0) {
					customQuery
					.append(" jbpmbusiness.ISSUE_DEPART_SIGNED  like '%" + ISSUE_DEPART_SIGNED  + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.ISSUE_DEPART_SIGNED  like '%" + ISSUE_DEPART_SIGNED 
							+ "%' ");
				}
			}
			
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(parameter.getFilterSign());
			processedParameter.setUserId(LdId);
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setCustomSelectItems(customSelectItems);
			processedParameter.setQueryWithTaskDate(queryWithTaskDate);
			processedParameter.setFilterYJZX(parameter.getFilterYJZX());
			processedParameter.setShowSignUserInfo(parameter.getShowSignUserInfo());
			initProcessedFilterSign(processedParameter);
			customSelectItems = processedParameter.getCustomSelectItems();
			customFromItems = processedParameter.getCustomFromItems();
			customQuery = processedParameter.getCustomQuery();
			logger.info(customFromItems + "\n" + customQuery);
			page = workflow.getProcessInstanceByConditionForPage(page,
					toSelectItems, paramsMap, orderMap, customSelectItems
							.toString(), customFromItems.toString(),
					customQuery.toString(), null, null);
			destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);
			List<TaskBusinessBean> listWorkflow = null;
			List<String> oatablepks = null;
			if (page.getResult() != null && !page.getResult().isEmpty()) {
				if (listWorkflow == null) {
					listWorkflow = new LinkedList<TaskBusinessBean>();
				}
				for (Object object : page.getResult()) {
				    	if(oatablepks == null){
				    	    oatablepks = new LinkedList<String>();
				    	}
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setStartUserName(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBsinessId(StringUtil
							.castString(objs[1]));
					if(objs[1] != null){
					    oatablepks.add(taskbusinessbean.getBsinessId());
					}
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[2]));
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[4]==null?"":objs[4]));
					taskbusinessbean.setWorkflowStartDate((Date) objs[5]);
					taskbusinessbean.setWorkflowEndDate((Date) objs[6]);
					taskbusinessbean.setStartUserId(StringUtil.castString(objs[7]));
					taskbusinessbean.setEndTime(StringUtil.castString(objs[8]));
					taskbusinessbean.setBusinessType(StringUtil.castString(objs[9]));// 业务类型，是否是自办文中启动意见征询数据
					taskbusinessbean.setNodeName(objs.length>10?StringUtil.castString(objs[10]):"");
					listWorkflow.add(taskbusinessbean);
				}
				listWorkflow = new ArrayList<TaskBusinessBean>(listWorkflow);
			}
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			checkList.clear();
			String[] oatablepkis = null;
			if(oatablepks != null && !oatablepks.isEmpty()){
			    if(oatablepkis == null){
				oatablepkis = new String[oatablepks.size()];
			    }
			    oatablepks.toArray(oatablepkis);
			}
			
			Page newpage = new Page(page.getPageSize(),page.isAutoCount());
			Object[] obj = getInfoItemPage(newpage, parameter.getWorkflowName(),
			parameter.getFormId(), false,oatablepkis);
    		Map tempMap = (Map)obj[4];//存放业务数据【业务数据ID：业务数据】
    		List showColumnList = (List) obj[0];
    		List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
			

			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表

				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(ServletActionContext.getRequest()
						.getParameter(WORKFLOW_TITLE));
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			//添加图片列
			if(!"1".equals(parameter.getProcessStatus())){
				GridViewColumUtil.addPngColum(showColumnList);
			}
//			showColumnList.add(new String[] {GridViewColumUtil.getPNGColumName(), "", GridViewColumUtil.getPNGColumType(), GridViewColumUtil.getPNGColumName() });// 显示红黄警示牌
			if (parameter.getProcessStatus() != null
					&& !"".equals(parameter.getProcessStatus())
					&& !"1".equals(parameter.getProcessStatus())) {
				if (parameter.getShowSignUserInfo() != null
						&& parameter.getShowSignUserInfo().equals("1")) {// 显示签收人信息
					showColumnList.add(new String[] { "showSignUserDept",
							"主办处室", "0", "showSignUserDept" });// 显示字段列表 12
					showColumnList.add(new String[] { "showSignUserName",
							"处室签收", "0", "showSignUserName" });// 显示字段列表 12
				} else {
					showColumnList.add(new String[] { "cruuentUserName",
							"当前办理人", "0", "cruuentUserName" });// 显示字段列表 12
					showColumnList.add(new String[] { "cruuentUserDept",
							"流程状态", "0", "cruuentUserDept" });// 显示字段列表 12
					if(parameter.getHandleKind() != null){
						showColumnList.add(new String[] { "processstartdate", "发起时间", "0",
						"processstartdate" });// 显示字段列表
					}
				}
			} else {
				if(parameter.getHandleKind() != null){
					showColumnList.add(new String[] { "processstartdate", "发起时间", "0",
					"processstartdate" });// 显示字段列表
				}
				showColumnList.add(new String[] { "processEndDate", "办结时间",
						SendDocManager.DATE_TYPE, "processEndDate" });
				showColumnList.add(new String[] { "startUserName", "主办人", "0",
						"startUserName" });
			}

			List<Map> showList = new ArrayList<Map>();

			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				Map<String,String[]> signInfoMap = new LinkedHashMap<String, String[]>();
				if("1".equals(processedParameter.getShowSignUserInfo())){
					 signInfoMap = sendDocBaseManager.getSignInfo(oatablepkis);
				}
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				StringBuilder params = new StringBuilder();
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					params.append(taskbusinessbean.getInstanceId()).append(",");
				}
				Map<String, Map> taskInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder(
							"SELECT TI.PROCINST_,TI.ID_ AS TASKID,TI.START_ AS TASKSTARTDATE,TI.END_ AS TASKENDDATE ");
					sql
							.append(" FROM JBPM_TASKINSTANCE TI WHERE TI.PROCINST_ IN(");
					sql.append(params.toString());
					sql
							.append(") AND  TI.ACTORID_='"+processedParameter.getUserId()+"' AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						taskInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							if (!taskInfoMap.containsKey(String.valueOf(rsMap
									.get("PROCINST_")))) {
								taskInfoMap.put(String.valueOf(rsMap
										.get("PROCINST_")), rsMap);
							}
						}
					}
				}

				/* 所有的用户信息 */
//				Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = sendDocUploadManager
//						.getAllUserIdMapUserBeanTemp();
				Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();

				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					if(taskInfoMap == null){
						continue;
					}
					Map taskMap = taskInfoMap.get(taskbusinessbean
							.getInstanceId());
					if(taskMap == null){
						continue;
					}
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
					taskbusinessbean.setTaskEndDate((Date) taskMap
							.get("TASKENDDATE"));
					taskbusinessbean.setTaskStartDate((Date) taskMap
							.get("TASKSTARTDATE"));
					Map parmMap = new HashMap();
					parmMap.put("processstartdate", taskbusinessbean.getWorkflowStartDate());
					//已分办 不查询当前处理人
					if (parameter.getProcessStatus() != null
							&& !"".equals(parameter.getProcessStatus())
							&& !"1".equals(parameter.getProcessStatus()) 
							&& !"1".equals(parameter.getShowSignUserInfo())) {
//						parmMap.put("cruuentUserName", new StringBuilder(listTemp[0].toString()).substring(1,listTemp[0].toString().length()-1)+"["+new StringBuilder(listTemp[1].toString()).substring(1,listTemp[1].toString().length()-1)+"]");
						StringBuilder cruuentUser = new StringBuilder();
						StringBuilder cruuentUserDept = new StringBuilder();
						if(taskbusinessbean.getWorkflowEndDate() != null){
						    cruuentUserDept.append("办结");
						}else{
						    List[] listTemp = sendDocUploadManager
							.getUserBeanTempArrayOfProcessStatusByPiId(
									taskbusinessbean.getInstanceId(),
									AllUserIdMapUserBeanTem);
						    for(int index = 0;index<listTemp[0].size(); index++){
							   if(" ".equals(listTemp[1].get(index).toString())){
								   cruuentUser.append(","+listTemp[0].get(index).toString());
							   }else{
								   cruuentUser.append(","+listTemp[0].get(index).toString()+"["+listTemp[1].get(index).toString()+"]");
							   }
						    }
						    cruuentUser.deleteCharAt(0);
//						    cruuentUser.append(new StringBuilder(listTemp[0].toString()).substring(1,listTemp[0].toString().length()-1)+"["+new StringBuilder(listTemp[1].toString()).substring(1,listTemp[1].toString().length()-1)+"]");
						    cruuentUserDept.append(taskbusinessbean.getNodeName());
						}
						parmMap.put("cruuentUserName","意见征询".equals(taskbusinessbean.getNodeName())?"意见征询":cruuentUser);
						parmMap.put("cruuentUserDept",  cruuentUserDept);
					}
					if (parameter.getProcessStatus() != null
							&& !"1".equals(parameter.getProcessStatus())) {
						parmMap.put("timeOut", taskbusinessbean.getEndTime());// 公文限时时间
					}
					if (parameter.getShowSignUserInfo() != null
							&& parameter.getShowSignUserInfo().equals("1")) {// 显示签收人信息
						if(signInfoMap.containsKey(taskbusinessbean.getBsinessId())){
							String[] signInfo = signInfoMap.get(taskbusinessbean.getBsinessId());
							parmMap.put("showSignUserName", signInfo[0]);
							parmMap.put("showSignUserDept", signInfo[1]);
						}else{
							parmMap.put("showSignUserName", "");
							parmMap.put("showSignUserDept", "");
						}
					}
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					if (parameter.getWorkflowName() != null
							&& !parameter.getWorkflowName().equals(
									taskbusinessbean.getWorkflowName())) {
						if (parameter.getFormId() != null
								&& (parameter.getFormId().startsWith("t") || parameter
										.getFormId().startsWith("T"))) {

						} else {
							continue;
						}
					}
					Map map = new HashMap();
					// ---------------------End--------------------------
					if (obj[3] != null) {// 存在表名
						if (tempMap
								.containsKey(taskbusinessbean.getBsinessId())) {
							 map = new HashMap((Map) tempMap
									.get(taskbusinessbean.getBsinessId()));
						}
					}
					map.put("processStatus", parameter.getProcessStatus());//流程状态
					sendDocBaseManager.initProcessedShowMap(map,
							pkFieldName, taskbusinessbean, parmMap);
					showList.add(map);
					if(map.containsKey("SENDDOC_ISSUE_DEPART_SIGNED") && !"1".equals(processedParameter.getShowSignUserInfo())){
						map.put("SENDDOC_ISSUE_DEPART_SIGNED", userService.getUserDepartmentByUserId(taskbusinessbean.getStartUserId()).getOrgName());
					}
				}
			}
			page.setResult(showList);
			if (page.getResult().isEmpty()) {
				page.setTotalCount(0);
			}
			listWorkflow = null;
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (SystemException ex) {
			logger.error("SystemException", ex);
			throw ex;
		}
	}

	/**
	 * 获取信息项数据
	 * 
	 * @author:邓志城
	 * @date:2010-11-11 下午04:09:31
	 * @param page
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            流程启动表单
	 * @param isDraft
	 *            是否查询草稿数据
	 * @return Object[展现列表,字段数据,查询字段列表,表名称]
	 * @modify yanjian 2011-09-19 12:45 显示红黄警示牌代码： showColumnList.add(new
	 *         String[]{"png","",PNG_TYPE,"png"});
	 * @modify yanjian 2011-10-24 保存草稿列表显示界面 添加流程标题的查询
	 * 		   2012-04-28 17:22 性能优化考虑：通过添加参数oatablepks缩小信息查询返回
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getInfoItemPage(Page page, String workflowName,
			String formId, boolean isDraft,String... oatablepks ) throws Exception{
		String tableName;
		List<EFormComponent> queryColumnList;
		List<String[]> showColumnList;
		List list;
		try {
			
			showColumnList = new LinkedList<String[]>();
			queryColumnList = new LinkedList<EFormComponent>();
			List<String> columnNames = new LinkedList<String>();
			if (formId == null || "".equals(formId) || "0".equals(formId)
				|| formId.startsWith("t")) {
				return this.getInfoItemPage(page,formId, isDraft,oatablepks);// workflowName为表名
			}
			Map<String, EFormComponent> fieldMap = eform.getFieldInfo(formId);
			EFormComponent mainTable = fieldMap
					.get(IEFormService.MAINTABLENAME);
			if (mainTable == null) {
				showColumnList.add(new String[] { "pkFieldName", "选择", "0",
						WORKFLOW_TITLE });// 显示字段列表
				return new Object[] { showColumnList, new ArrayList(),
						queryColumnList, null };
			}
			tableName = mainTable.getTableName();
			StringBuilder metaDataQuery = new StringBuilder("SELECT * ");// .append(strSet);
			metaDataQuery.append(" FROM ").append(tableName).append(
					" WHERE 1=0 ");// 加上过滤条件1=0避免查询出数据,只是为了得到字段信息
			final Map<String, String> columnMap = new HashMap<String, String>();// Map<字段名称,字段对应的Java类型>
			jdbcTemplate.query(metaDataQuery.toString(),
					new ResultSetExtractor() {
						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							ResultSetMetaData rsmd = rs.getMetaData();
							int count = rsmd.getColumnCount();
							for (int i = 1; i <= count; i++) {
								columnMap.put(rsmd.getColumnName(i), String
										.valueOf(rsmd.getColumnType(i)));// 存储数据库字段对应的SQL类型
							}
							return columnMap;
						}

					});

			String pkFieldName = super.getPrimaryKeyName(tableName);
			List<ToaSysmanageProperty> itemList = infoItemManager
					.getItemList(tableName);
			columnNames.add(pkFieldName);
			showColumnList.add(new String[] { pkFieldName, "选择", "0",
					WORKFLOW_TITLE });// 显示字段列表

			if (itemList != null && !itemList.isEmpty()) {
				for (ToaSysmanageProperty property : itemList) {
					if ("1".equals(property.getIsQuery())) {
						String infoItemField = property.getInfoItemField();
						EFormComponent eFormComponent = fieldMap
								.get(infoItemField);
						if (eFormComponent != null) {
							eFormComponent.setLable(property
									.getInfoItemSeconddisplay());// 信息项别名
							eFormComponent.setNumber(property
									.getInfoItemOrderby());// 信息项排序号
							eFormComponent.setValueType(columnMap
									.get(infoItemField));// 字段对应的Java类型
							// 处理下拉列表情况
							if (eFormComponent.getType().equals(
									"Strong.Form.Controls.ComboxBox")) {
								String items = eFormComponent.getItems();
								if (items.indexOf(";") == -1) {// 下拉列表数据是从数据字典中读取
									if (eFormComponent.getSelTableName() != null
											&& !"".equals(eFormComponent
													.getSelTableName())
											&& eFormComponent.getSelCode() != null
											&& !"".equals(eFormComponent
													.getSelCode())
											&& eFormComponent.getSelName() != null
											&& !"".equals(eFormComponent
													.getSelName())) {
										StringBuilder query = new StringBuilder(
												"select ")
												.append(
														eFormComponent
																.getSelCode())
												.append(",")
												.append(
														eFormComponent
																.getSelName())
												.append(" from ")
												.append(
														eFormComponent
																.getSelTableName())
												.append(" where ")
												.append(
														eFormComponent
																.getSelFilter());
										List lst = jdbcTemplate
												.queryForList(query.toString());
										StringBuilder builderItems = new StringBuilder();
										if (!lst.isEmpty()) {
											for (int i = 0; i < lst.size(); i++) {
												Map map = (Map) lst.get(i);
												builderItems
														.append(
																map
																		.get(eFormComponent
																				.getSelCode()))
														.append(",")
														.append(
																map
																		.get(eFormComponent
																				.getSelName()))
														.append(",")
														.append(
																map
																		.get(eFormComponent
																				.getSelCode()))
														.append(";");
											}
										}
										eFormComponent.setItems(builderItems
												.toString());
									}
								}
							}
							queryColumnList.add(eFormComponent);
						}
					}
					if ("1".equals(property.getIsView())) {
						if (!columnNames.contains(property.getInfoItemField())) {
							columnNames.add(property.getInfoItemField());
							// if(fieldMap.containsKey(property.getInfoItemField()))
							// {
							showColumnList.add(new String[] {
									property.getInfoItemField(),
									property.getInfoItemSeconddisplay(),
									property.getInfoItemDatatype(),
									property.getInfoItemField() });// 显示字段列表
							// }
						}
					}
				}
			}
			boolean isShowAuthor = false;
			// 是否要显示拟稿人
			if (columnNames.contains(WORKFLOW_AUTHOR)) {
				isShowAuthor = true;
			}
			for (String systemItem : SYSTEM_INFO_ITEM) {
				if (!columnNames.contains(systemItem)) {
					if (columnMap.containsKey(systemItem)) {// 只查询表里存在的字段
						columnNames.add(systemItem);
					}
				}
			}
			String strColumn = StringUtils.join(columnNames, ',');
			StringBuilder SqlQuery = new StringBuilder("SELECT ");
			SqlQuery.append(strColumn).append(" FROM ").append(tableName)
					.append(" t WHERE 1=1 ");
			if (isDraft) {
				SqlQuery.append(" AND t.").append(WORKFLOW_AUTHOR).append("='")
						.append(userService.getCurrentUser().getUserId())
						.append("' AND t.").append(WORKFLOW_NAME).append("='")
						.append(workflowName).append("'").append(" AND t.")
						.append(WORKFLOW_STATE).append("='0' ");
			} else {// 查询非草稿数据
				/*
				 * if(columnMap.containsKey(WORKFLOW_NAME)) { if(workflowName !=
				 * null && workflowName.length() > 0) { SqlQuery.append(" AND
				 * (t.").append(WORKFLOW_NAME).append("='").append(workflowName).append("'");
				 * SqlQuery.append(" OR t.").append(WORKFLOW_NAME).append(" is
				 * null)"); } }
				 */
				if (columnMap.containsKey(WORKFLOW_STATE)) {
					SqlQuery.append(" AND (t.").append(WORKFLOW_STATE).append(
							"='1'");
					SqlQuery.append(" OR t.").append(WORKFLOW_STATE).append(
					"='3'");
					SqlQuery.append(" OR t.").append(WORKFLOW_STATE).append(
							" is null)");
				}
				
				/*
				 * modify  yanjian 2012-04-28 17:22 性能优化考虑：通过添加参数oatablepks缩小信息查询返回
				 * 
				 * */
				StringBuilder pks = null;
				StringBuilder pkname = null;
				if(oatablepks != null && oatablepks.length>0){
				    for(String bsid:oatablepks){
					if(pks == null){
					    pks = new StringBuilder(); 
					    pkname = new StringBuilder();
					}
//					收文流程挂接了意见征询的表单，与收文业务表不符，应该过滤
					if(!(bsid.split(";")[0]).equals(tableName)){
						continue;
					}
					pks.append(",'"+bsid.split(";")[2]+"'");
					if(pkname.length() == 0){
					    pkname.append(bsid.split(";")[1]);
					}
				    }
				    if(pks != null && pks.length()>0){
						pks.deleteCharAt(0);
						SqlQuery.append(" and (t."+pkname+" in ("+pks+"))");
				    }
				}
			}
			// 处理查询
			Map params = ActionContext.getContext().getParameters();
			StringBuilder SqlCondition = new StringBuilder("");
			List<Object> paramValueList = new LinkedList<Object>();
			List<Integer> paramValueSqlTypeList = new LinkedList<Integer>();
			if (params != null && !params.isEmpty()) {
				for (Object objEntry : params.entrySet()) {
					Map.Entry entry = (Map.Entry) objEntry;
					Object key = entry.getKey();
					Object value = entry.getValue();
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] objValue = (Object[]) value;
							if (objValue.length > 0) {
								value = objValue[0];
							}
						}
					}
					if (value == null || "".equals(value.toString())) {
						continue;
					}
					for (int i = 0; i < queryColumnList.size(); i++) {
						EFormComponent ec = queryColumnList.get(i);
						String fieldName = ec.getFieldName();// 得到字段名称
						if (key.equals(fieldName)) {// 找到匹配的字段
							if ("Strong.Form.Controls.Edit"
									.equals(ec.getType())) {// 单行文本框和多行文本框(支持字符串查询)
								SqlCondition.append(" AND t.").append(
										ec.getFieldName()).append(" like ? ");
								paramValueList.add("%" + value + "%");
								paramValueSqlTypeList.add(Types.VARCHAR);
							}
							if ("Strong.Form.Controls.ComboxBox".equals(ec
									.getType())) {
								SqlCondition.append(" AND t.").append(
										ec.getFieldName()).append(" = ? ");
								paramValueList.add(value);
								paramValueSqlTypeList.add(Integer.parseInt(ec
										.getValueType()));
							}
							if ("Strong.Form.Controls.DateTimePicker".equals(ec
									.getType())
									&& value != null && !"".equals(value)) {
								if (Integer.parseInt(ec.getValueType()) == Types.VARCHAR) {// 字符串的日期
									SqlCondition.append(" AND t.").append(
											ec.getFieldName()).append(" <= ?");
									paramValueList.add(value);
									paramValueSqlTypeList.add(Integer
											.parseInt(ec.getValueType()));
								} else {
									SqlCondition
											.append(" AND t.")
											.append(ec.getFieldName())
											.append(" >= to_date('")
											.append(value)
											.append(
													"','YYYY-MM-DD HH24:MI:SS')");
								}
							}
							ec.setValue(value.toString());
						}
					}
				}
				logger.info("自定查询SQL：" + SqlCondition.toString() + ",参数："
						+ paramValueList);
			}

			if (SqlCondition.length() > 0) {
				SqlQuery.append(SqlCondition);
			}
			int[] sqlTypeArray = new int[paramValueSqlTypeList.size()];
			for (int i = 0; i < paramValueSqlTypeList.size(); i++) {
				sqlTypeArray[i] = paramValueSqlTypeList.get(i);
			}
			logger.info(SqlQuery.toString());
			//yanjian 2012-05-14 13:34 基于性能问题进行调整
			if((!isDraft)&&(oatablepks == null || oatablepks.length == 0)){
				//当不是查询草稿并且要查询的流程信息中找不到业务id时
				list = new ArrayList();
			}else{
				list = jdbcTemplate.queryForList(SqlQuery.toString(),
						paramValueList.toArray(), sqlTypeArray);
			}
			if (isShowAuthor) {// 需要显示拟稿人字段时才查询
				// 转换ID
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map = (Map<String, Object>) list
								.get(i);
						Object userId = map.get(WORKFLOW_AUTHOR);
						if (userId != null) {
							map.put(WORKFLOW_AUTHOR, userService
									.getUserNameByUserId(userId.toString()));
						}
					}
				}
				logger.info("根据用户id转换用户名称");
			}
			HashMap busidListMap = new HashMap();
			if (list != null && !list.isEmpty()) {
				for (Object obj : list) {
					Map mapobj = (Map) obj;
					String pkFieldNamevalue = (String) mapobj.get(pkFieldName);
					String busId = tableName + ";" + pkFieldName + ";"
							+ pkFieldNamevalue;
					if (!busidListMap.containsKey(busId)) {
						busidListMap.put(busId, mapobj);
					}
				}
			}
			return new Object[] { showColumnList, list, queryColumnList,
					tableName,busidListMap };
		} catch (DataAccessException e) {
			logger.error("发生Jdbc异常", e);
			throw new SystemException(e);
		} catch (Exception e) {
			logger.error("SystemException..", e);
			throw new SystemException(e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public Object[] getInfoItemPage(Page page, String tableName, boolean isDraft,String... oatablepks) {
		List<EFormComponent> queryColumnList;
		List<String[]> showColumnList;
		List list;
		try {
			showColumnList = new LinkedList<String[]>();
			queryColumnList = new LinkedList<EFormComponent>();
			List<String> columnNames = new LinkedList<String>();
			/*
			 * final Map<String, String> columnMap = new HashMap<String,
			 * String>();//Map<字段名称,字段对应的Java类型> StringBuilder metaDataQuery =
			 * new StringBuilder("SELECT * ");//.append(strSet);
			 * metaDataQuery.append(" FROM ").append(tableName).append(" WHERE
			 * 1=0 ");//加上过滤条件1=0避免查询出数据,只是为了得到字段信息
			 * jdbcTemplate.query(metaDataQuery.toString(), new
			 * ResultSetExtractor(){ public Object extractData(ResultSet rs)
			 * throws SQLException, DataAccessException { ResultSetMetaData rsmd =
			 * rs.getMetaData(); int count = rsmd.getColumnCount(); for(int
			 * i=1;i<=count ;i++){ columnMap.put(rsmd.getColumnName(i),
			 * String.valueOf(rsmd.getColumnType(i)));//存储数据库字段对应的SQL类型 } return
			 * columnMap; }
			 * 
			 * });
			 */
			tableName = tableName.toUpperCase();
			String pkFieldName = super.getPrimaryKeyName(tableName);
			List<ToaSysmanageProperty> itemList = infoItemManager
					.getItemList(tableName);
			columnNames.add(pkFieldName);
			showColumnList.add(new String[] { pkFieldName, "选择", "0",
					WORKFLOW_TITLE });// 显示字段列表
			if (itemList != null && !itemList.isEmpty()) {
				for (ToaSysmanageProperty property : itemList) {
					if ("1".equals(property.getIsQuery())) {
						String infoItemField = property.getInfoItemField();
						EFormComponent eFormComponent = new EFormComponent();
						eFormComponent.setFieldName(infoItemField);
						eFormComponent.setLable(property
								.getInfoItemSeconddisplay());// 信息项别名
						eFormComponent.setNumber(property.getInfoItemOrderby());// 信息项排序号
						eFormComponent.setType("Strong.Form.Controls.Edit");
						queryColumnList.add(eFormComponent);
					}
					if ("1".equals(property.getIsView())) {
						if (!columnNames.contains(property.getInfoItemField())) {
							columnNames.add(property.getInfoItemField());
							showColumnList.add(new String[] {
									property.getInfoItemField(),
									property.getInfoItemSeconddisplay(),
									property.getInfoItemDatatype(),
									property.getInfoItemField() });// 显示字段列表
						}
					}
				}
			}
			String strColumn = StringUtils.join(columnNames, ',');
			StringBuilder SqlQuery = new StringBuilder("SELECT ");
			SqlQuery.append(strColumn).append(" FROM ").append(tableName)
					.append(" t WHERE 1=1 ");
			if (isDraft) {
				SqlQuery.append(" AND t.").append(WORKFLOW_AUTHOR).append("='")
						.append(userService.getCurrentUser().getUserId())
						.append("' AND t.").append(WORKFLOW_STATE).append(
								"='0' ");
			} else {// 查询非草稿数据
				// if(columnMap.containsKey(WORKFLOW_STATE)) {
				SqlQuery.append(" AND (t.").append(WORKFLOW_STATE).append(
						"='1'");
				SqlQuery.append(" OR t.").append(WORKFLOW_STATE).append(
				"='3'");
				SqlQuery.append(" OR t.").append(WORKFLOW_STATE).append(
						" is null)");
				// }
				/*
				 * modify  yanjian 2012-04-28 17:22 性能优化考虑：通过添加参数oatablepks缩小信息查询返回
				 * 
				 * */
				StringBuilder pks = null;
				StringBuilder pkname = null;
				if(oatablepks != null && oatablepks.length>0){
				    for(String bsid:oatablepks){
					if(pks == null){
					    pks = new StringBuilder(); 
					    pkname = new StringBuilder();
					}
//					收文流程挂接了意见征询的表单，与收文业务表不符，应该过滤
					if(!(bsid.split(";")[0]).equals(tableName)){
						continue;
					}
					pks.append(",'"+bsid.split(";")[2]+"'");
					if(pkname.length() == 0){
					    pkname.append(bsid.split(";")[1]);
					}
				    }
				    if(pks != null && pks.length()>0){
						pks.deleteCharAt(0);
						SqlQuery.append(" and (t."+pkname+" in ("+pks+"))");
				    }
				}
			}
			// 处理查询
			Map params = ActionContext.getContext().getParameters();
			StringBuilder SqlCondition = new StringBuilder("");
			List<Object> paramValueList = new LinkedList<Object>();
			List<Integer> paramValueSqlTypeList = new LinkedList<Integer>();
			if (params != null && !params.isEmpty()) {
				for (Object objEntry : params.entrySet()) {
					Map.Entry entry = (Map.Entry) objEntry;
					Object key = entry.getKey();
					if ("formId".equals(key)) {
						continue;
					}
					Object value = entry.getValue();
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] objValue = (Object[]) value;
							if (objValue.length > 0) {
								value = objValue[0];
							}
						}
					}
					if (value == null || "".equals(value.toString())) {
						continue;
					}
					for (int i = 0; i < queryColumnList.size(); i++) {
						EFormComponent ec = queryColumnList.get(i);
						String fieldName = ec.getFieldName();// 得到字段名称
						if (key.equals(fieldName)) {// 找到匹配的字段
							SqlCondition.append(" AND t.").append(
									ec.getFieldName()).append(
									" like ? ");
							paramValueList.add("%" + value + "%");
							paramValueSqlTypeList.add(Types.VARCHAR);
							ec.setValue(value.toString());
						}
					}
				}
				logger.info("自定查询SQL：" + SqlCondition.toString() + ",参数："
						+ paramValueList);
			}

			if (SqlCondition.length() > 0) {
				SqlQuery.append(SqlCondition);
			}
			int[] sqlTypeArray = new int[paramValueSqlTypeList.size()];
			for (int i = 0; i < paramValueSqlTypeList.size(); i++) {
				sqlTypeArray[i] = paramValueSqlTypeList.get(i);
			}
			logger.info(SqlQuery.toString());
			// list =
			// jdbcTemplate.queryForList(SqlQuery.toString(),paramValueList.toArray(),sqlTypeArray);
			if((!isDraft)&&(oatablepks == null || oatablepks.length == 0)){
				//当不是查询草稿并且要查询的流程信息中找不到业务id时
				list = new ArrayList();
			}else{
				list = jdbcTemplate.queryForList(SqlQuery.toString(),
						paramValueList.toArray(), sqlTypeArray);
			}
			HashMap busidListMap = new HashMap();
			if (list != null && !list.isEmpty()) {
				for (Object obj : list) {
					Map mapobj = (Map) obj;
					String pkFieldNamevalue = (String) mapobj.get(pkFieldName);
					String busId = tableName + ";" + pkFieldName + ";"
							+ pkFieldNamevalue;
					if (!busidListMap.containsKey(busId)) {
						busidListMap.put(busId, mapobj);
					}
				}
			}
			return new Object[] { showColumnList, list, queryColumnList,
					tableName,busidListMap };
		} catch (DataAccessException e) {
			logger.error("发生Jdbc异常", e);
			throw new SystemException(e);
		} catch (Exception e) {
			logger.error("SystemException..", e);
			throw new SystemException(e);
		}
	}
	
	
	/**
	 * 桌面显示代办事宜
	 * 
	 * @author 严建
	 * @param page
	 * @param innerHtml
	 * @param parambean
	 * @throws Exception
	 * @createTime Mar 23, 2012 1:39:10 PM
	 */
	public void loadDesktopTodoHtml(Page<TaskBusinessBean> page,
			StringBuffer innerHtml, ParamBean parambean) throws Exception {
		if (innerHtml == null) {
			throw new NullPointerException("参数innerHtml不能为null");
		}
		String sectionFontSize = parambean.getSectionFontSize();
		List resList = page.getResult();
		if (resList != null) {
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = resList.iterator(); iterator.hasNext();) {

				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				innerHtml.append("<table class=\"linkdiv\" style=\"font-size:"+sectionFontSize+"px\" width=\"100%\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				sendDocIcoManager.loadToDoIco(innerHtml, taskbusinessbean,
						parambean.getRootPath());
				this.genTodoTitle(innerHtml, taskbusinessbean,
						parambean);
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
				innerHtml.append("</div>");
			}

		}
		// 【更多】跳转连接
		this.genTodoClickMoreLink(innerHtml, parambean
				.getRootPath(), parambean.getWorkflowType(), parambean
				.getType());
		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(
						page == null ? 0 : (page.getTotalCount() == -1 ? 0
								: page.getTotalCount())).append("\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
	}
	
	
	/**
	 * 生成桌面待办的标题
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param parambean
	 * @throws Exception
	 * @createTime Feb 16, 2012 8:08:56 PM
	 */
	public void genTodoTitle(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean, ParamBean parambean)
			throws Exception {
		/* 生成流程类型别名链接 */
		StringBuilder clickProcessType = this.genTodoWorkflowNameLink(
				taskbusinessbean, parambean.getRootPath(), parambean.getType());
		StringBuilder clickTitle = this.genTodoClickTitle(parambean
				.getRootPath(), taskbusinessbean, parambean.getType());
		String objTitle = taskbusinessbean.getBusinessName();
		String title = "";

		// 上一步处理人信息
		String PreTaskActor = taskbusinessbean.getPreTaskActorInfo();
		if (objTitle.startsWith("<red>")) {
			objTitle = objTitle.replace("<red>", "");

			// 如果显示的内容长度大于设置的主题长度，则过滤该长度
			title = objTitle == null ? "" : objTitle.toString();
			if (title.length() > parambean.getLength()
					&& parambean.getLength() > 0) {
				title = title.substring(0, parambean.getLength()) + "...";
			}
			title = title.replace("\\r\\n", " ");
			title = title.replace("\\n", " ");

			innerHtml
					.append("<span title=\"")
					.append(objTitle)
					.append(PreTaskActor + "\">").append("")
					.append("<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickProcessType.toString()).append("\">[").append(
							taskbusinessbean.getWorkflowAliaName()).append(
							"]</a>").append("").append(
							"<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickTitle.toString()).append("\">").append(
							"<font color=\"red\" >").append("（急）")
					.append(title).append("</font>").append("</a>").append(
							"</span>");
			innerHtml.append("</td>");
		} else {
			title = objTitle == null ? "" : objTitle.toString();
			if (title.length() > parambean.getLength()
					&& parambean.getLength() > 0) {
				title = title.substring(0, parambean.getLength()) + "...";
			}
			innerHtml
					.append("<span title=\"")
					.append(objTitle)
					.append(PreTaskActor + "\">")
					.append("")
					.append("<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickProcessType.toString()).append("\">[").append(
							taskbusinessbean.getWorkflowAliaName()).append(
							"]</a>").append("").append(
							"<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickTitle.toString()).append("\">").append("")
					.append(title).append("").append("</a></span>");
			innerHtml.append("</td>");
		}
		if ("1".equals(parambean.getShowCreator())) {
			innerHtml.append("<td width=\"80px\">");
			if (taskbusinessbean.getBsinessId() == null) {
				taskbusinessbean.setBsinessId(manager
						.getFormIdAndBussinessIdByTaskId(taskbusinessbean
								.getTaskId())[0]);
			}
			String bussinessId = taskbusinessbean.getBsinessId();
			if (!"0".equals(bussinessId)) {
				String departmentName = null;
				String[] args = bussinessId.split(";");

				String tableName = String.valueOf(args[0]);
				String pkFieldName = String.valueOf(args[1]);
				String pkFieldValue = String.valueOf(args[2]);
				Map map = null;
				String userId = null;
				String userName = null;
				try {
					String sql = "select count("+pkFieldName+") as total from "+tableName+" where "+pkFieldName+" = '"+pkFieldValue+"'";
					Map totalMap = manager.queryForMap(sql);
					String total = StringUtil.castString(totalMap.get("total"));
					if (!"0".equals(total)) {
						map = manager.getSystemField(pkFieldName, pkFieldValue,
								tableName);
						userId = (String) map
								.get(BaseWorkflowManager.WORKFLOW_AUTHOR);
					}
					if (userId == null) {// 如果是子流程，拟稿人为第一父流程拟稿人 yanjian
						// 2011-11-03 19:10
						String panentInstanceId = String
								.valueOf(workflow
										.getContextInstance(
												taskbusinessbean
														.getInstanceId())
										.getVariable(
												WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID));
						if(panentInstanceId != null && !"null".equals(panentInstanceId)){
							ProcessInstance panentInstance = workflow
							.getProcessInstanceById(panentInstanceId);
							userId = panentInstance.getStartUserId();
							userName = panentInstance.getStartUserName();
						}else{
							User user = userService.getCurrentUserLeader();
							userId = user.getUserId();
							userName = user.getUserName();
						}
					} else {
						userName = userService.getUserNameByUserId(userId);
					}
				} catch (Exception e) {
					User user = userService.getCurrentUser();
					userId = user.getUserId();
					userName = user.getUserName();
				}

				Organization department = userService
						.getUserDepartmentByUserId(userId);
				departmentName = department.getOrgName();
				//包含部门
				String showName = userName + "[" + departmentName + "]";
				//不包含部门
				String showTitle = userName;
				String titleShowName = showName ;
				if(showName.length()>3){//如果显示的内容长度大于设置的主题长度，则过滤该长度
					showName = showName.substring(0,3)+"...";
				}
				if(showTitle.length()>3){//如果显示的内容长度大于设置的主题长度，则过滤该长度
					showTitle = showTitle.substring(0,3)+"...";
				}
				innerHtml.append("<span class =\"linkgray\" style=\"font-size:"+parambean.getSectionFontSize()+"px\" title=\""+titleShowName+"\">").append(showTitle)
				//innerHtml.append("<span class =\"linkgray\" title=\""+userName + "[" + departmentName + "]"+"\">").append(
				//		userName + "[" + departmentName + "]")
						.append("</span>");
			}
			innerHtml.append("</td>");
		}
		// 显示日期信息
		if ("1".equals(parambean.getShowDate())) {
			innerHtml.append("<td width=\"100px\">");
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
			innerHtml.append("<span style=\"font-size:"+parambean.getSectionFontSize()+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd hh:mm").format(taskbusinessbean
					.getTaskStartDate())+"\"class =\"linkgray10\">")
					.append(st.format(taskbusinessbean.getTaskStartDate())).append(
							"</span>");
			innerHtml.append("</td>");
		}
	}
	
	
	/**
	 * 生成待办流程类型别名链接
	 * 
	 * @author 严建
	 * @param taskbusinessbean
	 * @param rootPath
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 16, 2012 7:32:27 PM
	 */
	public StringBuilder genTodoWorkflowNameLink(
			TaskBusinessBean taskbusinessbean, String rootPath, String type)
			throws DAOException, ServiceException, SystemException {
		try {
			return this.genTodoWorkflowNameLink(taskbusinessbean
					.getWorkflowName(), taskbusinessbean.getFormId(),
					taskbusinessbean.getWorkflowType(), type, rootPath, null);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 生成代办流程类型别名链接
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 3:32:47 PM
	 * @return StringBuilder
	 */
	public StringBuilder genTodoWorkflowNameLink(String workflowName,
			String formId, String workflowType, String type, String rootPath,
			Map<String, StringBuilder> workflowNameLinkMap)
			throws DAOException, ServiceException, SystemException {
		try {
			if (workflowName == null || "".equals(workflowName)) {
				logger.error("参数workflowName不能为空或空字符串");
				throw new ServiceException("参数workflowName不能为空或空字符串");
			}
			if (formId == null || "".equals(formId)) {
				logger.error("参数formId不能为空或空字符串");
				throw new ServiceException("参数formId不能为空或空字符串");
			}
			if (workflowType == null || "".equals(workflowType)) {
				logger.error("参数workflowType不能为空或空字符串");
				throw new ServiceException("参数workflowType不能为空或空字符串");
			}
			StringBuilder clickProcessType = new StringBuilder();
			// 流程名称
			String currworkflowName = URLEncoder.encode(URLEncoder.encode(
					workflowName, "utf-8"), "utf-8");
			// 获取表单id
			String[] nodeInfo = formId.split(",");
			formId = nodeInfo[nodeInfo.length - 1];
			// 流程类别
			String currworkflowType = workflowType;
			String clickProcessUrl = rootPath
					+ "/senddocLeader/sendDocLeader!todo.action?type=" + type
					+ "&workflowName=" + currworkflowName 
					+ "&formId=" + formId
					+ "&workflowType=" + currworkflowType;
			clickProcessType.append("window.parent.refreshWorkByTitle('"
					+ clickProcessUrl
					+ "','"
					+ (type != null && type.equals("notsign") ? "待签收文件"
							: "待办文件") + "');");

			return clickProcessType;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	/**
	 * 生成已办链接点击事件
	 * 
	 * @author 严建
	 * @param rootPath
	 * @param taskbusinessbean
	 * @return
	 * @createTime Feb 16, 2012 7:00:50 PM
	 */
	public StringBuilder genTodoClickTitle(String rootPath,
			TaskBusinessBean taskbusinessbean, String type) throws Exception {
		StringBuilder clickTitle = new StringBuilder();
		clickTitle
				.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
		clickTitle
				.append("var a = window.open('")
				.append(rootPath)
				.append("/senddocLeader/sendDocLeader!viewProcessed.action?taskId=")
				.append(taskbusinessbean.getTaskId())
				.append("&type=")
				.append(type);
		
		//公文传输  限制已退回的不能提交   tj
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT DOC_FLAG FROM T_OARECVDOC WHERE OARECVDOCID = '"+taskbusinessbean.getBsinessId().split(";")[2]+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
		if(rs.next()){
			String t = rs.getString("DOC_FLAG");
			if("2".equals(rs.getString("DOC_FLAG"))){
				clickTitle.append("&gwcs=gwcs");
			}
		}
		
		clickTitle
				.append("&instanceId=")
				.append(taskbusinessbean.getInstanceId())
				.append("&workflowName=")
				.append(
						URLEncoder.encode(URLEncoder.encode(taskbusinessbean
								.getWorkflowName().toString(), "utf-8"),
								"utf-8"))
				.append(
						"','todo','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");
		return clickTitle;
	}
	
	
	/**
	 * 生成更多及更多的链接
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param rootPath
	 * @param workflowType
	 * @param type
	 * @createTime Feb 16, 2012 7:38:01 PM
	 */
	public void genTodoClickMoreLink(StringBuffer innerHtml, String rootPath,
			String workflowType, String type) {
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				rootPath);
		link.append(
				"/senddocLeader/sendDocLeader!todoWorkflow.action?workflowType="
						+ workflowType + "&type=" + type).append("', '领导待办事宜'")
				.append(");");
		innerHtml.append(
				"<div align=\"right\" style=\"padding:2px；font-size:12px;\">")
				.append("<a href=\"#\" onclick=\"").append(link.toString())
				.append("\"> ")
				.append("<IMG SRC=\"").append(rootPath).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
	}

	
	/**
	 * 得到当前用户待办任务列表 支持自定义查询以及自定义显示列表.
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 下午09:03:45
	 * @param page
	 *            分页列表对象（暂无用途）
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            表单id
	 * @param workflowType
	 *            流程类型id -1表示所有非系统流程类型
	 * @param userName
	 *            拟稿人名称
	 * @param isBackSpace
	 *            是否是退回任务
	 * @param startDate
	 *            任务接收开始日期:查询用
	 * @param endDate
	 *            任务接收截止日期：查询用
	 * @return Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<EFormComponent>,表名称]
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getTodo(Page page, ProcessedParameter parameter) {
		try {
			String workflowName = parameter.getWorkflowName();
			String formId = parameter.getFormId();
			String workflowType = parameter.getWorkflowType();
			String userName = parameter.getUserName();
			Date startDate = parameter.getStartDate();
			Date endDate =	parameter.getEndDate();
			String isBackSpace = parameter.getIsBackSpace();
			String type = parameter.getType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			String orgId = parameter.getOrgId();
			
			// 增加工作流中的显示字段

		
			String businessName = ServletActionContext.getRequest()
					.getParameter(WORKFLOW_TITLE);
			

			StringBuilder customQuery = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customSelectItems = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			Object[] sItems = { "taskId", "taskStartDate", "isBackspace",
					"assignType", "taskNodeId", "taskNodeName","isReceived" };
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			if (customQuery.length() == 0) {
				customQuery.append("  pi.ISSUSPENDED_ = 0 ");
			} else {
				customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
			}
	        //User curuser = userService.getCurrentUser();
			User curuser = userService.getCurrentUserLeader();
            String LdId=userService.getLDId(userService.getCurrentUser()
					.getUserId());
			//根据处室Id 取得该处室全部(待签收文件)
			if(orgId != null){
				List<User> userList = userService.getUsersByOrgID(orgId);
				if(!userList.isEmpty()){
					User firstUser = userList.get(0);
					paramsMap.put("handlerId", firstUser.getUserId());// 当前用户的领导办理任务
				}				
			}else{
				paramsMap.put("handlerId", curuser.getUserId());// 当前用户的领导办理任务
			}
			
			paramsMap.put("toAssignHandlerId",LdId);
			String[] adpterWorkflowType = CustomQueryAdpter.adpterWorkflowType(new String[]{workflowType,excludeWorkflowType});
			workflowType = adpterWorkflowType[0];
			excludeWorkflowType =  adpterWorkflowType[1];
			if (workflowType != null) {
				CustomQueryUtil.genWorkflowTypeStringForSql(workflowType,customQuery);
			}
			if (excludeWorkflowType != null) {
				CustomQueryUtil.genExcludeWorkflowTypeStringForSql(excludeWorkflowType,customQuery);
			}
			if (workflowName != null && !"".equals(workflowName)) {
				if (customQuery.length() == 0) {
					customQuery
							.append(" pi.NAME_ like '" + workflowName + "' ");
				} else {
					customQuery.append("and pi.NAME_ like '" + workflowName
							+ "' ");
				}
			}
			if (businessName != null && !"".equals(businessName)) {
				if (customQuery.length() == 0) {
					customQuery
					.append(" pi.BUSINESS_NAME_ like '%" + businessName + "%' ");
				} else {
					customQuery.append("and pi.BUSINESS_NAME_ like '%" + businessName
							+ "%' ");
				}
			}
			if (null != userName && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (startDate != null) {
				paramsMap.put("taskStartDateStart", startDate);
			}
			if (endDate != null) {
				paramsMap.put("taskStartDateEnd", endDate);
			}

			if ("0".equals(isBackSpace)) {// 退回
				paramsMap.put("isBackspace", "1");
			} else if ("1".equals(isBackSpace)) {// 委托
				paramsMap.put("assignType", "0");
			} else if ("2".equals(isBackSpace)) {// 指派
				paramsMap.put("assignType", "1");
			} else if ("3".equals(isBackSpace)) {// 一般流转
				paramsMap.put("assignType", "3");
				paramsMap.put("isBackspace", "0");
			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			
//			收文编号
			String RECV_NUM = ServletActionContext.getRequest().getParameter("RECV_NUM");
			if(RECV_NUM != null && !"".equals(RECV_NUM)){
				if (customQuery.length() == 0) {
					customQuery
					.append(" jbpmbusiness.RECV_NUM like '%" + RECV_NUM + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.RECV_NUM like '%" + RECV_NUM
							+ "%' ");
				}
			}
			//来文字号
			String DOC_NUMBER = ServletActionContext.getRequest().getParameter("DOC_NUMBER");
			if(DOC_NUMBER != null && !"".equals(DOC_NUMBER)){
				if (customQuery.length() == 0) {
					customQuery
					.append(" jbpmbusiness.DOC_NUMBER like '%" + DOC_NUMBER + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.DOC_NUMBER like '%" + DOC_NUMBER
							+ "%' ");
				}
			}
			//来文单位
			String ISSUE_DEPART_SIGNED  = ServletActionContext.getRequest().getParameter("ISSUE_DEPART_SIGNED");
			if(ISSUE_DEPART_SIGNED != null && !"".equals(ISSUE_DEPART_SIGNED)){
				if (customQuery.length() == 0) {
					customQuery
					.append(" jbpmbusiness.ISSUE_DEPART_SIGNED  like '%" + ISSUE_DEPART_SIGNED  + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.ISSUE_DEPART_SIGNED  like '%" + ISSUE_DEPART_SIGNED 
							+ "%' ");
				}
			}
			
			//是否签收
			String isReceived  = ServletActionContext.getRequest().getParameter("isReceived");
			if(isReceived != null && !"".equals(isReceived) && !"null".equals(isReceived)){
				if("1".equals(isReceived)){
					customQuery.append(" and ti.ISRECEIVE_  =  1");
				}else{
					customQuery.append(" and ti.ISRECEIVE_  is null ");
				}
			}
			
			if (customQuery.length() == 0) {
				customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			} else {
				customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			}
			
			initTodoSign(type, customSelectItems, customFromItems, customQuery);

			type = null;
			page.setAutoCount(true);
			page = workflow.getTaskInfosByConditionForPage(page, toSelectItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);

			destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);

			List<TaskBusinessBean> listWorkflow = null;
			
			List<String> oatablepks = null;
			if (page.getResult() != null && !page.getResult().isEmpty()) {
				listWorkflow = new LinkedList<TaskBusinessBean>();
				StringBuilder params = new StringBuilder();
				for (Object object : page.getResult()) {
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
					taskbusinessbean.setTaskStartDate((Date) objs[1]);
					taskbusinessbean.setIsBackspace(StringUtil
							.castString(objs[2]));
					taskbusinessbean.setAssignType(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setNodeId(StringUtil.castString(objs[4]));
					taskbusinessbean
							.setNodeName(StringUtil.castString(objs[5]));
					taskbusinessbean.setIsReceived(StringUtil.castString(objs[6]));
					listWorkflow.add(taskbusinessbean);
					params.append(StringUtil.castString(objs[0])).append(",");
				}
				Map<String, Map> processInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder();
					sql
							.append(" SELECT ")
							.append(" TI.TASKID           AS TASKID, ")
							.append(
									" TI.PROCESSID        AS PROCESSINSTANCEID, ")
							.append(" PI.START_USER_NAME_ AS STARTUSERNAME, ")
							.append(
									" PI.BUSINESS_ID      AS PROCESSMAINFORMBUSINESSID, ")
							.append(" PI.NAME_            AS PROCESSNAME, ")
							.append(" PI.BUSINESS_NAME_   AS BUSINESSNAME, ")
							.append(" BI.END_TIME         AS END_TIME, ")
							.append(" BI.BUSINESS_TYPE    AS BUSINESS_TYPE, ")
							.append(" PI.START_   		  AS STARTDATE, ")
							.append(" PI.START_USER_ID_   AS START_USER_ID ")
							.append(" FROM ").append(
									" JBPM_PROCESSINSTANCE PI, ").append(
									" T_JBPM_BUSINESS BI, ").append(" (")
							.append("SELECT ")
							.append("T.ID_       AS TASKID, ").append(
									"T.PROCINST_ AS PROCESSID ")
							.append("FROM ").append("JBPM_TASKINSTANCE T ")
							.append("WHERE ").append("T.ID_ IN (").append(
									params.toString()).append(")").append(
									")TI ").append(" WHERE ").append(
									" PI.ID_ = TI.PROCESSID ")
									.append(" AND PI.BUSINESS_ID = BI.BUSINESS_ID" )
									;
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						processInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							processInfoMap.put(String.valueOf(rsMap
									.get("TASKID")), rsMap);
						}
					}
				}
				
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					Map map = processInfoMap.get(taskbusinessbean.getTaskId());
					if(map == null){
						continue;
					}
					if(oatablepks == null){
				    	    oatablepks = new LinkedList<String>();
				    	}
					taskbusinessbean.setInstanceId(StringUtil.castString(map
							.get("PROCESSINSTANCEID")));
					taskbusinessbean.setStartUserName(StringUtil.castString(map
							.get("STARTUSERNAME")));
					taskbusinessbean.setBsinessId(StringUtil.castString(map
							.get("PROCESSMAINFORMBUSINESSID")));
					if(taskbusinessbean.getBsinessId() != null){
					    oatablepks.add(taskbusinessbean.getBsinessId());
					}
					taskbusinessbean.setWorkflowName(StringUtil.castString(map
							.get("PROCESSNAME")));
					taskbusinessbean.setBusinessName(StringUtil.castString(map
							.get("BUSINESSNAME")));
					taskbusinessbean.setEndTime(StringUtil.castString(map
							.get("END_TIME")));
					taskbusinessbean.setBusinessType(StringUtil.castString(map
							.get("BUSINESS_TYPE")));
					taskbusinessbean.setWorkflowStartDate((Date)map
							.get("STARTDATE"));
					//处理子流程获取不到发起人的情况	Bug序号： 0000004533  yanjian 2012-07-02 13:06
					taskbusinessbean.setStartUserId(map
							.get("START_USER_ID")==null?curuser.getUserId():StringUtil.castString(map
							.get("START_USER_ID")));
				}

				listWorkflow = new ArrayList<TaskBusinessBean>(listWorkflow);
			}
			String[] oatablepkis = null;
			if(oatablepks != null && !oatablepks.isEmpty()){
			    if(oatablepkis == null){
				oatablepkis = new String[oatablepks.size()];
			    }
			    oatablepks.toArray(oatablepkis);
			}
			Page newpage = new Page(page.getPageSize(),page.isAutoCount());
			Object[] obj = getInfoItemPage(newpage, workflowName, formId, false,oatablepkis);
			Map tempMap = (Map)obj[4];//存放业务数据【业务数据ID：业务数据】
			List showColumnList = (List) obj[0];
//			添加图片列
			GridViewColumUtil.addPngColum(showColumnList);
//			showColumnList.add(new String[] { GridViewColumUtil.getPNGColumName(), "", GridViewColumUtil.getPNGColumType(), GridViewColumUtil.getPNGColumName()});// 显示红黄警示牌
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表

				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(businessName);
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			if(parameter.getHandleKind() != null){
				showColumnList.add(new String[] { "processstartdate", "发起时间", "5",
				"processstartdate" });//
			}
			showColumnList.add(new String[] { "currentusername", "上步办理人", "0",
					"currentusername" });//
			showColumnList.add(new String[] { "currentuserdept", "所在部门", "0",
					"currentuserdept" });//


			List<Map> showList = new ArrayList<Map>();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				List<Long> taskIdListTemp = new LinkedList<Long>();
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					taskIdListTemp.add(new Long(taskbusinessbean.getTaskId()));
				}
				Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = this
						.getTaskIdMapPreTaskBeanTemper(taskIdListTemp);

				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					Map parmMap = new HashMap<String, String>();
					TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp
					.get(taskbusinessbean.getTaskId());
					if ("意见征询".equals(taskBeanTemp.getNodeName())) {//处理意见征询的文
						ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
								.getBsinessId().split(";")[2]);
						parmMap.put("currentusername", "已征求完意见");
						parmMap.put("currentuserdept", toayjzx == null ? ""
								: toayjzx.getUnit() == null ? "" : toayjzx
										.getUnit());
					} else {
						String PreTaskActor = taskBeanTemp.getTaskActorName();
						String PreTaskActorOrgName = taskBeanTemp.getOrgName();
						parmMap.put("currentusername", PreTaskActor);
						parmMap.put("currentuserdept", PreTaskActorOrgName);
					}
					parmMap.put("processstartdate", taskbusinessbean.getWorkflowStartDate());
					parmMap.put("timeOut", taskbusinessbean.getEndTime());
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					if (workflowName != null
							&& !workflowName.equals(taskbusinessbean
									.getWorkflowName())) {
						if (formId != null
								&& (formId.startsWith("t") || formId
										.startsWith("T"))) {

						} else {
							continue;
						}
					}
					// ---------------------End--------------------------
					Map map = new HashMap();
					if (obj[3] != null) {// 存在表名
						if (tempMap
								.containsKey(taskbusinessbean.getBsinessId())) {
							 map = new HashMap((Map) tempMap
									.get(taskbusinessbean.getBsinessId()));
						}
					} 
					sendDocBaseManager.initTodoShowMap(map, pkFieldName,
							taskbusinessbean, parmMap);
					showList.add(map);
					if(map.containsKey("SENDDOC_ISSUE_DEPART_SIGNED")){
						map.put("SENDDOC_ISSUE_DEPART_SIGNED", userService.getUserDepartmentByUserId(taskbusinessbean.getStartUserId()).getOrgName());
					}
				}
				page.setResult(showList);
				if (page.getResult().isEmpty()) {
					page.setTotalCount(0);
				}
			}
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (Exception ex) {
			logger.error("SystemException", ex);
			return null;
		}

	}
	
	
	/**
	 * 
	 * 获取前一步处理人信息（兼容退回时无法准确显示上一步处理人）
	 * 
	 * @description
	 * @author 严建
	 * @param taskIdList
	 * @return
	 * @createTime Apr 10, 2012 10:06:41 PM
	 */
	public Map<String, TaskBeanTemp> getTaskIdMapPreTaskBeanTemper(List<Long> taskIdList){
		Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = null;
		if (taskIdList != null && !taskIdList.isEmpty()) {
			List<String> userIds = null;
			for (int i = 0; i < taskIdList.size(); i++) {

				if (TaskIdMapPreTaskBeanTemp == null) {
					TaskIdMapPreTaskBeanTemp = new HashMap<String, TaskBeanTemp>();
				}
				if (userIds == null) {
					userIds = new ArrayList<String>();
				}
				String taskId = taskIdList.get(i).toString().toString();
				TaskBeanTemp taskBeanTemp = new TaskBeanTemp();
				boolean flag = false;
				BackInfoBean backInfoBean = null;
				if(workflow.isBackTaskByTid(taskId)){
				    backInfoBean = workflowConstService.getBackInfoBean(taskId);
				    if(backInfoBean != null){
					flag = true;
				    }
				}
				if(flag){//如果是退回任务且存在退回信息
					    taskBeanTemp.setStart(backInfoBean.getBackTime());
					    taskBeanTemp.setTaskActorId(backInfoBean.getUserId());
					    taskBeanTemp.setTaskActorName(backInfoBean.getUserName());
					    taskBeanTemp.setOrgId(backInfoBean.getOrgId());
					    taskBeanTemp.setOrgName(backInfoBean.getOrgName());
				}else{//不是退回任务
					List<TaskInstanceBean> lists = workflow.getTruePreTaskInfosByTiId(taskId);
					if(lists == null || lists.isEmpty()){
						//User user = userService.getCurrentUser();
						User user = userService.getCurrentUserLeader();//获取当前用户的领导信息
						TaskInstance taskInstanceTemp = workflow.getTaskInstanceById(taskId);
						Long preTaskInstanceId= taskInstanceTemp.getPreTaskInstance();
						if(preTaskInstanceId != null){//存在前一步任务id
							TaskInstance preTaskInstance = workflow.getTaskInstanceById(preTaskInstanceId+"");
							taskBeanTemp.setStart(preTaskInstance.getEnd());
							taskBeanTemp.setTaskActorId(preTaskInstance.getActorId());
							taskBeanTemp.setNodeName(preTaskInstance.getNodeName());
						}else{//不存在前一步任务id
							taskBeanTemp.setStart(taskInstanceTemp.getStart());
							taskBeanTemp.setTaskActorId(user.getUserId());
							taskBeanTemp.setTaskActorName(user.getUserName());
						}
					}else{
						TaskInstanceBean lastest = null;
						for(TaskInstanceBean taskinstancebean:lists){
							if(lastest == null){
								lastest = taskinstancebean;
							}else{
								Date endDate = workflow
								.getTaskInstanceById(taskinstancebean.getTaskId()+"").getEnd();
								Date lastestendDate = workflow
								.getTaskInstanceById(lastest.getTaskId()+"").getEnd();
								if(endDate.after(lastestendDate)){
									lastest = taskinstancebean;
								}
							}
						}
						TaskInstanceBean taskinstancebean = lastest;
						taskBeanTemp.setTaskActorId(taskinstancebean.getTaskActorId());
						taskBeanTemp.setTaskActorName(taskinstancebean.getTaskActorName());
						taskBeanTemp.setNodeName(taskinstancebean.getTaskName());
						taskBeanTemp.setStart(workflow
								.getTaskInstanceById(taskinstancebean.getTaskId()+"").getEnd());
					}
					userIds.add(taskBeanTemp.getTaskActorId());//无法直接查询出处室信息，需要通过另外的方式进行数据查询
				}
				TaskIdMapPreTaskBeanTemp.put(taskId, taskBeanTemp);
			}
			if (userIds != null && !userIds.isEmpty()) {
				Map<String, UserBeanTemp> map = getUserIdMapUserBeanTemp(userIds);//获取用户信息
				if(map == null){
					for(String oriUserId:userIds){
						if(map == null){
							map =  new HashMap<String, UserBeanTemp>();
						}
						if(!map.containsKey(oriUserId)){
							Organization org = userService.getUserDepartmentByUserId(oriUserId);
							User user = userService.getUserInfoByUserId(oriUserId);
							UserBeanTemp userbeantemp = new UserBeanTemp();
							userbeantemp.setUserId(oriUserId);
							userbeantemp.setUserName(user.getUserName());
							userbeantemp.setOrgId(org.getOrgId());
							userbeantemp.setOrgName(org.getOrgName());
							userbeantemp.setRest1(user.getRest1());
							map.put(oriUserId, userbeantemp);
						}
					}
				}
				if (map != null && !map.isEmpty()) {
					List<String> keys = new ArrayList<String>(
							TaskIdMapPreTaskBeanTemp.keySet());//获取所有的taskid
					for (int i = 0; i < keys.size(); i++) {
						String key = keys.get(i);
						TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp
								.get(key);
						String userId = taskBeanTemp.getTaskActorId();
						String LdId="";
						if(map.containsKey(userId)){//只对那么添加到列表userIds中的数据进行匹配
							UserBeanTemp userBean = map.get(userId);
							if (userBean == null) {
								userId = userService.getCurrentUser().getUserId();
								try {
									LdId=userService.getLDId(userId);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								userBean = map.get(LdId);
							}
							/*设置处室信息*/
							taskBeanTemp.setTaskActorName(userBean.getUserName());
							taskBeanTemp.setOrgId(userBean.getOrgId());
							if("1".equals(userBean.getRest1())){
								taskBeanTemp.setOrgName(" ");
							}else{
								taskBeanTemp.setOrgName(userBean.getOrgName());
							}
							taskBeanTemp.setRest1(userBean.getRest1());
							TaskIdMapPreTaskBeanTemp.put(key, taskBeanTemp);
						}
					}
				}
			}
		}
		return TaskIdMapPreTaskBeanTemp;
	}
	
	
	/**
	 * 获取的用户信息
	 * 
	 * @author 严建
	 * @param userIds
	 *            一组用户id
	 * @return return Map<String,UserBeanTemp> 数据结构：userid【Map】用户信息
	 * @createTime Feb 7, 2012 1:15:45 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, UserBeanTemp> getUserIdMapUserBeanTemp(
			List<String> userIds) {
		Map<String, UserBeanTemp> map = null;
		List<Object[]> list = customUserService.getUsersInfo(userIds);
		if (list != null && !list.isEmpty()) {
			map = new HashMap<String, UserBeanTemp>();
			for (int i = 0; i < list.size(); i++) {
				UserBeanTemp userBeanTemp = new UserBeanTemp();
				Object[] obj = list.get(i);
				userBeanTemp.setUserId(obj[0].toString());
				userBeanTemp.setUserName(obj[1].toString());
				userBeanTemp.setOrgId(obj[2].toString());
				userBeanTemp.setOrgName(obj[3].toString());
				userBeanTemp.setRest1(StringUtil.castString(obj[4]));
				map.put(userBeanTemp.getUserId(), userBeanTemp);
			}
		}
		return map;
	}
	
	
	/**
	 * 得到待办流程列表
	 * 
	 * @author yanjian
	 * @param parameter
	 * @return	List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 17, 2012 4:09:14 PM
	 * 
	 * 
	 */
	public List<Object[]> getTodoWorkflow(ProcessedParameter parameter)
			throws ServiceException, DAOException, SystemException {
		try {
			String workflowType = parameter.getWorkflowType();	//流程类型
			String type = parameter.getType();		//type:是否是代签收公文 notsign;sign
			String excludeWorkflowType = parameter.getExcludeWorkflowType();		//
			List<Object[]> list = null;
			Object[] sItems = { "processName", "processTypeId",
					"processTypeName", "processMainFormId" };
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			String LdId=userService.getLDId(userService.getCurrentUser().getUserId());
			paramsMap
					.put("handlerId", LdId);// 当前用户的领导办理任务

			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			StringBuilder customQuery = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customSelectItems = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS jbpmbusiness");
			customQuery.append(" @businessId = jbpmbusiness.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (workflowType.startsWith("-")) {
					customQuery.append(" and  pi.TYPE_ID_ not in("
							+ workflowType.substring(1) + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ in(" + workflowType
							+ ") ");
				}

			}
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
					customQuery.append(" and  pi.TYPE_ID_ not in("+ excludeWorkflowType + ") ");
			}

			this.initTodoSign(type, customSelectItems, customFromItems,
					customQuery);
			type = null;
			list = workflow.getTaskInfosByConditionForList(toSelectItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			this
					.destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
							customSelectItems, customFromItems, customQuery,
							null, null);

			Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
			List<Object[]> workflowList = new ArrayList<Object[]>();
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					if (!map.containsKey(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
						workflowSet.add(objs);
						map.put(objs[0].toString() + "$" + objs[1].toString(),
								workflowSet);
					} else {
						map.get(objs[0].toString() + "$" + objs[1].toString())
								.add(objs);
					}
				}
				List<String> checkList = null;// 处理重复的记录
				for (int j = 0; j < list.size(); j++) {
					if (checkList == null) {
						checkList = new LinkedList<String>();
					}
					Object[] objs = (Object[]) list.get(j);
					Object[] newObjs = new Object[5];
					if (!checkList.contains(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List l = map.get(objs[0].toString() + "$"
								+ objs[1].toString());
						if (l != null) {
							newObjs[0] = objs[0];
							newObjs[1] = objs[1];
							newObjs[2] = objs[2];
							newObjs[3] = objs[3];
							newObjs[4] = l.size();
							workflowList.add(newObjs);
							checkList.add(objs[0].toString() + "$"
									+ objs[1].toString());
						}
					}
				}
				list = null;
				checkList = null;
				map = null;
			}
			return workflowList;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	
	/**
	 * 得到当前用户相应类别的任务列表, 修改成挂起任务及指派委托状态也查找出来，在展现层展现状态
	 * 增加查询条件参数,添加流程类型参数processType，以区分发文、收文等
	 * 
	 * @param page
	 *            分页对象
	 * @param parameter
	 *            参数对象
	 * @return Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起,
	 *         (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型}
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getTodoWorks(Page page, ProcessedParameter parameter,
			OALogInfo... infos) throws SystemException, ServiceException, SQLException {
		try {
			String workflowType = parameter.getWorkflowType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			if(workflowType.startsWith("-")){
				if(excludeWorkflowType != null && excludeWorkflowType.length()>0){
					excludeWorkflowType = excludeWorkflowType+","+workflowType.substring(1);
				}else{
					excludeWorkflowType = workflowType.substring(1);
				}
				workflowType = null;
			}
			String businessName = parameter.getBusinessName();
			String userName = parameter.getUserName();
			Date startDate = parameter.getStartDate();
			Date endDate = parameter.getEndDate();
			String isBackSpace = parameter.getIsBackSpace();
			String userId = userService.getCurrentUser().getUserId();// 获取当前用户	
		    String LdId=userService.getLDId(userId);
			
			String searchType = "all"; // dengzc 2010年6月28日9:42:34 待办和在办功能合并
			Object[] toSelectItems = { "taskId", "businessName",
					"processStartDate", "processInstanceId", "processName",
					"processTypeId","processMainFormBusinessId"};
			List sItems = Arrays.asList(toSelectItems);
			long stamp = System.currentTimeMillis();
			StringBuilder customFromItems = new StringBuilder();
			customFromItems.append("Tjbpmbusiness JBPMBUSINESS");
			StringBuilder customQuery = new StringBuilder().append(stamp).append(" = ").append(stamp);
			customQuery.append(" and @businessId = JBPMBUSINESS.businessId ");
			customQuery.append(" and JBPMBUSINESS.businessType in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			customQuery.append(" and ti.name not like '%签收' ");
			if ("todo".equals(searchType)) {
				searchType = "0";
			} else if ("doing".equals(searchType)) {
				searchType = "1";
			} else {
				searchType = "2";
			}
			Map paramsMap = new HashMap<String, Object>();
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			if(parameter.getUserIds() != null){
				paramsMap.put("handlerId", parameter.getUserIds());
			}else{
				if (null != LdId) {
					paramsMap.put("handlerId", LdId);
				}
			}
//			paramsMap.put("toAssignHandlerId", userId);
			if (null != searchType) {
				paramsMap.put("taskType", searchType);
			}
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.typeId in(" + workflowType + ") ");
				} else {
					customQuery.append(" and pi.typeId in(" + workflowType + ") ");
				}
//				paramsMap.put("processTypeId",
//						genWorkflowTypeList(workflowType));
			}
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.typeId not in(" + excludeWorkflowType + ") ");
				} else {
					customQuery.append(" and pi.typeId not in(" + excludeWorkflowType + ") ");
				}
			}
			
			if (null != businessName && !"".equals(businessName)) {
				paramsMap.put("businessName", "%"+businessName+"%");
			}
			if (null != userName && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (null != startDate) {
				paramsMap.put("taskStartDateStart", startDate);
			}
			if (null != endDate) {
				paramsMap.put("taskStartDateEnd", endDate);
			}
			if ("0".equals(isBackSpace)) {// 退回
				paramsMap.put("isBackspace", "1");
			} else if ("1".equals(isBackSpace)) {// 委托
				paramsMap.put("assignType", "0");
			} else if ("2".equals(isBackSpace)) {// 指派
				paramsMap.put("assignType", "1");
			} else if ("3".equals(isBackSpace)) {// 一般流转
				paramsMap.put("assignType", "3");
				paramsMap.put("isBackspace", "0");
			} else {

			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("processStartDate", "1");
			page = workflow.getTaskInfosByConditionForPage(page, sItems,
					paramsMap, orderMap, null, customFromItems.toString(), customQuery.toString(), null);
			List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
			List<Long> taskIdListTemp = new LinkedList<Long>();
			if (page != null && page.getResult() != null) {
				for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[1]));
					taskbusinessbean.setWorkflowStartDate((Date) objs[2]);
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setWorkflowType(StringUtil
							.castString(objs[5]));
					taskbusinessbean.setBsinessId(StringUtil
							.castString(objs[6]));
					beans.add(taskbusinessbean);
					taskIdListTemp.add(new Long(taskbusinessbean.getTaskId()));
				}
				Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = sendDocUploadManager
						.getTaskIdMapPreTaskBeanTemper(taskIdListTemp);
				for (TaskBusinessBean taskbusinessbean : beans) {
					TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp.get(taskbusinessbean.getTaskId());
					if ("意见征询".equals(taskBeanTemp.getNodeName())) {//处理意见征询的文
						ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
								.getBsinessId().split(";")[2]);
						taskbusinessbean.setPreTaskActor("已征求完意见");
						taskbusinessbean.setPreTaskActorOrgName(toayjzx == null ? ""
								: toayjzx.getUnit() == null ? "" : toayjzx
										.getUnit());
					}else{
						taskbusinessbean.setPreTaskActor(taskBeanTemp
								.getTaskActorName());
						taskbusinessbean.setPreTaskActorOrgName(taskBeanTemp
								.getOrgName());
					}
				}
				page.setResult(new ArrayList<TaskBusinessBean>(beans));
			}
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取待办工作" });
		}
	}
	
	
	/**
	 * 验证指定的任务ID是否在当前用户列表里
	 * 
	 * @author:邓志城
	 * @date:2010-11-24 上午10:52:11
	 * @param taskId
	 * @return
	 */
	public boolean isTaskInCurrentUser(String taskId) {
		Object[] toSelectItems = { "taskId" };
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "2");// 取非办结任务
		String LdId="";
		try {
			LdId = userService.getLDId(userService.getCurrentUser().getUserId());
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		paramsMap.put("handlerId",LdId);// 当前用户办理任务
		List list = this.getTaskInfosByConditionForList(sItems, paramsMap,
				null, null, null, null, null);
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object objs = (Object) list.get(i);
				if (taskId.equals(objs.toString())) {
					return true;
				}
			}
		}
		return false;
	}
	
}
