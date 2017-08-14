package com.strongit.oa.work.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bgt.model.ToaYjzx;
import com.strongit.oa.bgt.senddoc.DocManager;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.customquery.CustomQueryAdpter;
import com.strongit.oa.senddoc.customquery.CustomQueryUtil;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.StringUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 7, 2012 9:32:46 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.work.manager.WorkExtendManager
 */
@Service
@Transactional
public class WorkExtendManager extends BaseManager {
	@Autowired
	SendDocUploadManager sendDocUploadManager;
	@Autowired
	private DocManager docManager;
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
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getTodoWorks(Page page, ProcessedParameter parameter,
			OALogInfo... infos) throws SystemException, ServiceException {
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
			User user1=new User();
			try{
				 user1 = userService.getCurrentUser();
			}catch (Exception e) {
				/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
				if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
					throw new SystemException("很抱歉，会话过程已结束，请您重新登录。");
					
				}

			}
			String userId = user1.getUserId();// 获取当前用户
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
				if (null != userId) {
					paramsMap.put("handlerId", userId);
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
				paramsMap.put("processStartDateStart", startDate);
			}
			if (null != endDate) {
				paramsMap.put("processStartDateEnd", endDate);
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
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getProcessedWorks(Page page, ProcessedParameter parameter,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {

			String workflowType = parameter.getWorkflowType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			String excludeWorkflowTypeName=parameter.getExcludeWorkflowTypeName();
			String businessName = parameter.getBusinessName();
			// String userName = parameter.getUserName();
			Date startDate = parameter.getStartDate();
			Date endDate = parameter.getEndDate();
			Date workflowEndDatestartDate = parameter.getWorkflowEndDatestartDate();
			Date workflowEndDateendDate = parameter.getWorkflowEndDateendDate();
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
					customQuery.append(" pi.TYPE_ID_ not in("
							+ excludeWorkflowType + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ not in("
							+ excludeWorkflowType + ") ");
				}
			}
			//流程类型名称，过滤掉该流程类型名称的数据
			if (excludeWorkflowTypeName != null && !"".equals(excludeWorkflowTypeName)
					&& !"null".equals(excludeWorkflowTypeName)) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.NAME_ not like '%"
							+ excludeWorkflowTypeName +"%' ");
				} else {
					customQuery.append(" and pi.NAME_ not like '%"
							+ excludeWorkflowTypeName +"%' ");
				}
			}//customQuery.append(" and ti.NAME_ not like '%签收' ");
			//and ti.NAME_ like '"+orgName+"签收') ");
			User curUser=new User();
			try{
				curUser = userService.getCurrentUser();
			}catch (Exception e) {
				/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
				if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
					throw new SystemException("很抱歉，会话过程已结束，请您重新登录。");
				}

			}
		//	User curUser = userService.getCurrentUser();// 获取当前用户
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			if (endDate != null && !"".equals(endDate)) {
				paramsMap.put("processStartDateEnd", endDate);
			}
			if (startDate != null && !"".equals(startDate)) {
				paramsMap.put("processStartDateStart", startDate);
			}
			if(workflowEndDatestartDate !=null && !"".equals(workflowEndDatestartDate)){
				paramsMap.put("processEndDateStart", workflowEndDatestartDate);
					
			}
			if(workflowEndDateendDate !=null && !"".equals(workflowEndDateendDate)){
				paramsMap.put("processEndDateEnd", workflowEndDateendDate);
					
			}
			if (state != null && !"".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			if (isSuspended != null && !"".equals(isSuspended)) {
				paramsMap.put("processSuspend", isSuspended);
			}
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(parameter.getFilterSign());
			processedParameter.setUserId(curUser.getUserId());
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setQueryWithTaskDate(null);
			processedParameter.setUserIds(parameter.getUserIds());
			initProcessedFilterSign(processedParameter);
			Map orderMap = new LinkedHashMap<Object, Object>();
			orderMap.put("processStartDate", "1");
			orderMap.put("processedStartDate", "1");			
			page = workflow.getProcessInstanceByConditionForPage(page, sItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			// page = workflow
			// .getProcessInstanceByConditionForPage(page,sItems, paramsMap,
			// orderMap, "", "", "", null);
			List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page != null && page.getResult() != null) {
				List<String> processInstanceIds = new LinkedList<String>();
				StringBuilder params = new StringBuilder();
				for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[1]));
					try {
						taskbusinessbean.setWorkflowStartDate(objs[2]==null?null:sdf.parse(StringUtil
								.castString(objs[2])));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setWorkflowType(StringUtil
							.castString(objs[4]));
					//taskbusinessbean.setWorkflowEndDate((Date) objs[5]);
					try {
						taskbusinessbean.setWorkflowEndDate(objs[5]==null?null:sdf.parse(StringUtil
								.castString(objs[5])));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
//					taskbusinessbean.setTaskEndDate((Date) taskMap
//							.get("TASKENDDATE"));

					try {
						taskbusinessbean.setTaskEndDate(sdf.parse(StringUtil.castString(taskMap
													.get("TASKENDDATE")==null?"":taskMap
															.get("TASKENDDATE"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					taskbusinessbean.setTaskStartDate((Date) taskMap
//							.get("TASKSTARTDATE"));
					try {
						taskbusinessbean.setTaskStartDate(sdf.parse(StringUtil.castString(taskMap
								.get("TASKSTARTDATE")==null?"":taskMap
										.get("TASKSTARTDATE"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
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
	 * 查询我的请求
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
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getHostedWorks(Page page, ProcessedParameter parameter,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {
			
			String workflowType = parameter.getWorkflowType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			String businessName = parameter.getBusinessName();
			Date startDate = parameter.getStartDate();
			Date endDate = parameter.getEndDate();
			Object[] toSelectItems = {"processInstanceId","businessName",
					"processStartDate",  "processName",
					"processTypeId","processEndDate" };
			List sItems = Arrays.asList(toSelectItems);
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
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
			User curUser = new User();
			try{
				curUser = userService.getCurrentUser();// 获取当前用户
			}catch (Exception e) {
				/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
				if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
					throw new SystemException("很抱歉，会话过程已结束，请您重新登录。");  //特别声明：异常抛出信息文字，不能修改。多处有捕获此处异常信息
				}

			}
			if(businessName != null && !"".equals(businessName)){
				paramsMap.put("businessName", "%"+businessName+"%");
			}
			if(endDate != null && !"".equals(endDate)){
				paramsMap.put("processStartDateEnd", endDate);
			}
			if(startDate != null && !"".equals(startDate)){
				paramsMap.put("processStartDateStart",startDate);
			}
			paramsMap.put("processSuspend","0");
			paramsMap.put("startUserId", userService.getCurrentUser()
					.getUserId());// 当前用户办理任务
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(null);
			processedParameter.setUserId(curUser.getUserId());
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setQueryWithTaskDate(null);
			processedParameter.setHost(true);
			initHosted(processedParameter);
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("processStartDate", "1");
			page = workflow.getProcessInstanceByConditionForPage(page,
					sItems, paramsMap, orderMap, customSelectItems
					.toString(), customFromItems.toString(), customQuery
					.toString(), null, null);
//			page = workflow
//			.getProcessInstanceByConditionForPage(page,sItems, paramsMap,
//					orderMap, "", "", "", null);
			List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page != null && page.getResult() != null) {
				List<String> processInstanceIds = new LinkedList<String>();
				StringBuilder params = new StringBuilder();
				for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[1]));
					taskbusinessbean.setWorkflowStartDate(objs[2]==null?null:sf.parse(StringUtil.castString(objs[2])));
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setWorkflowType(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setWorkflowEndDate(objs[5]==null?null:sf.parse(StringUtil.castString(objs[5])));
					beans.add(taskbusinessbean);
					processInstanceIds.add(taskbusinessbean.getInstanceId());
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
					.append(")  AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
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
					if(taskMap == null){
						continue;
					}
					StringBuilder cruuentUser = new StringBuilder();
					if(taskbusinessbean.getWorkflowEndDate() == null){
						List[] listTemp = sendDocUploadManager
						.getUserBeanTempArrayOfProcessStatusByPiId(
								taskbusinessbean.getInstanceId(),
								AllUserIdMapUserBeanTem);
						for(int index = 0;index<listTemp[0].size(); index++){
							cruuentUser.append(","+listTemp[0].get(index).toString()+"["+listTemp[1].get(index).toString()+"]");
						}
						if(cruuentUser.length()>0){
							cruuentUser.deleteCharAt(0);
						}
					}else{
						cruuentUser.append("已办结");
					}
					taskbusinessbean.setActorName(cruuentUser.toString());
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
//					taskbusinessbean.setTaskEndDate((Date) taskMap
//							.get("TASKENDDATE"));
					taskbusinessbean.setTaskEndDate(sf.parse(StringUtil.castString(taskMap
							.get("TASKENDDATE")==null?"":taskMap
									.get("TASKENDDATE"))));
//					taskbusinessbean.setTaskStartDate((Date) taskMap
//							.get("TASKSTARTDATE"));
					taskbusinessbean.setTaskStartDate(sf.parse(StringUtil.castString(taskMap
							.get("TASKSTARTDATE")==null?"":taskMap
									.get("TASKSTARTDATE"))));
					if("1".equals(parameter.getState())){
						taskbusinessbean.setMainActorName(workflow.getMainActorInfoByProcessInstanceId(taskbusinessbean.getInstanceId()).getUserName());
					}
				}
				page.setResult(new ArrayList<TaskBusinessBean>(beans));
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取已办工作" });
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}
	
	/**
	 * 查询我的在办，已办文件
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
	 * @author:xush getMytodoWorks方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getMytodoWorks(Page page, ProcessedParameter parameter,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {
			
			String workflowType = parameter.getWorkflowType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			String businessName = parameter.getBusinessName();
			Date startDate = parameter.getStartDate();
			Date endDate = parameter.getEndDate();
			Object[] toSelectItems = {"processInstanceId","businessName",
					"processStartDate",  "processName",
					"processTypeId","processEndDate" };
			List sItems = Arrays.asList(toSelectItems);
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
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
			User curUser = userService.getCurrentUser();// 获取当前用户
			if(businessName != null && !"".equals(businessName)){
				paramsMap.put("businessName", "%"+businessName+"%");
			}
			if(endDate != null && !"".equals(endDate)){
				paramsMap.put("processStartDateEnd", endDate);
			}
			if(startDate != null && !"".equals(startDate)){
				paramsMap.put("processStartDateStart",startDate);
			}
			paramsMap.put("processSuspend","0");
			paramsMap.put("processStatus",parameter.getState());
			paramsMap.put("startUserId", userService.getCurrentUser()
					.getUserId());// 当前用户办理任务
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(null);
			processedParameter.setUserId(curUser.getUserId());
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setQueryWithTaskDate(null);
			processedParameter.setHost(true);
			initHosted(processedParameter);
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("processStartDate", "1");
			page = workflow.getProcessInstanceByConditionForPage(page,
					sItems, paramsMap, orderMap, customSelectItems
					.toString(), customFromItems.toString(), customQuery
					.toString(), null, null);
//			page = workflow
//			.getProcessInstanceByConditionForPage(page,sItems, paramsMap,
//					orderMap, "", "", "", null);
			List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page != null && page.getResult() != null) {
				List<String> processInstanceIds = new LinkedList<String>();
				StringBuilder params = new StringBuilder();
				for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[1]));
//					taskbusinessbean.setWorkflowStartDate((Date) objs[2]);
					try {
						taskbusinessbean.setWorkflowStartDate(objs[2]==null?null:sdf.parse(StringUtil
								.castString(objs[2])));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setWorkflowType(StringUtil
							.castString(objs[4]));
//					taskbusinessbean.setWorkflowEndDate((Date) objs[5]);
					try {
						taskbusinessbean.setWorkflowEndDate(objs[5]==null?null:sdf.parse(StringUtil
								.castString(objs[5])));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					beans.add(taskbusinessbean);
					processInstanceIds.add(taskbusinessbean.getInstanceId());
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
					.append(")  AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
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
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (TaskBusinessBean taskbusinessbean : beans) {
					Map taskMap = taskInfoMap.get(taskbusinessbean
							.getInstanceId());
					if(taskMap == null){
						continue;
					}
					StringBuilder cruuentUser = new StringBuilder();
					if(taskbusinessbean.getWorkflowEndDate() == null){
						List[] listTemp = sendDocUploadManager
						.getUserBeanTempArrayOfProcessStatusByPiId(
								taskbusinessbean.getInstanceId(),
								AllUserIdMapUserBeanTem);
						for(int index = 0;index<listTemp[0].size(); index++){
							cruuentUser.append(","+listTemp[0].get(index).toString()+"["+listTemp[1].get(index).toString()+"]");
						}
						if(cruuentUser.length()>0){
							cruuentUser.deleteCharAt(0);
						}
					}else{
						cruuentUser.append("已办结");
					}
					taskbusinessbean.setActorName(cruuentUser.toString());
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
//					taskbusinessbean.setTaskEndDate((Date) taskMap
//							.get("TASKENDDATE"));
					try {
						taskbusinessbean.setTaskEndDate(sf.parse(StringUtil.castString(taskMap
								.get("TASKENDDATE")==null?"":taskMap
										.get("TASKENDDATE"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					taskbusinessbean.setTaskStartDate((Date) taskMap
//							.get("TASKSTARTDATE"));
					try {
						taskbusinessbean.setTaskStartDate(sf.parse(StringUtil.castString(taskMap
								.get("TASKSTARTDATE")==null?"":taskMap
										.get("TASKSTARTDATE"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if("1".equals(parameter.getState())){
						taskbusinessbean.setMainActorName(workflow.getMainActorInfoByProcessInstanceId(taskbusinessbean.getInstanceId()).getUserName());
					}
				}
				page.setResult(new ArrayList<TaskBusinessBean>(beans));
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取我的在办工作" });
		}
		return page;
	}
	

/**
 * 查询我的在办，已办文件
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
 * @author:xush getMytodoWorks方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
 */
@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public Page getRepealWorks(Page page, ProcessedParameter parameter,
		OALogInfo... infos) throws SystemException, ServiceException {
	try {
		
		String workflowType = parameter.getWorkflowType();
		String excludeWorkflowType = parameter.getExcludeWorkflowType();
		String businessName = parameter.getBusinessName();
		Date startDate = parameter.getStartDate();
		Date endDate = parameter.getEndDate();
		Object[] toSelectItems = {"processInstanceId","businessName",
				"processStartDate",  "processName",
				"processTypeId","processEndDate"};
		List sItems = Arrays.asList(toSelectItems);
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		StringBuilder customSelectItems = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customQuery = new StringBuilder();
		customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
		//customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
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
		User curUser = userService.getCurrentUser();// 获取当前用户
		String deptName=curUser.getOrgName();
		if(businessName != null && !"".equals(businessName)){
			paramsMap.put("businessName", "%"+businessName+"%");
		}
		if(endDate != null && !"".equals(endDate)){
			paramsMap.put("processStartDateEnd", endDate);
		}
		if(startDate != null && !"".equals(startDate)){
			paramsMap.put("processStartDateStart",startDate);
		}
		paramsMap.put("processSuspend","1");
		//paramsMap.put("processStatus",parameter.getState());
		paramsMap.put("startUserId", userService.getCurrentUser()
				.getUserId());// 当前用户办理任务
		ProcessedParameter processedParameter = new ProcessedParameter();
		processedParameter.setFilterSign(null);
		processedParameter.setUserId(curUser.getUserId());
		processedParameter.setCustomFromItems(customFromItems);
		processedParameter.setCustomQuery(customQuery);
		processedParameter.setQueryWithTaskDate(null);
		processedParameter.setHost(true);
		initHosted(processedParameter);
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processStartDate", "1");
		page = workflow.getProcessInstanceByConditionForPage(page,
				sItems, paramsMap, orderMap, customSelectItems
				.toString(), customFromItems.toString(), customQuery
				.toString(), null, null);
//		page = workflow
//		.getProcessInstanceByConditionForPage(page,sItems, paramsMap,
//				orderMap, "", "", "", null);
		List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (page != null && page.getResult() != null) {
			List<String> processInstanceIds = new LinkedList<String>();
			StringBuilder params = new StringBuilder();
			for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
				TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
				taskbusinessbean.setInstanceId(StringUtil
						.castString(objs[0]));
				taskbusinessbean.setBusinessName(StringUtil
						.castString(objs[1]));
				//taskbusinessbean.setWorkflowStartDate((Date) objs[2]);

				try {
					taskbusinessbean.setWorkflowStartDate(objs[2]==null?null:sdf.parse(StringUtil
													.castString(objs[2])));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				taskbusinessbean.setWorkflowName(StringUtil
						.castString(objs[3]));
				taskbusinessbean.setWorkflowType(StringUtil
						.castString(objs[4]));
				//taskbusinessbean.setWorkflowEndDate((Date) objs[5]);
				try {
					taskbusinessbean.setWorkflowEndDate(objs[5]==null?null:sdf.parse(StringUtil
							.castString(objs[5])));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beans.add(taskbusinessbean);
				processInstanceIds.add(taskbusinessbean.getInstanceId());
				params.append(taskbusinessbean.getInstanceId()).append(",");
			}
			Map<String, Map> taskInfoMap = null;
			if (params.length() > 0) {
				params.deleteCharAt(params.length() - 1);
				StringBuilder sql = new StringBuilder(
				"SELECT TI.PROCINST_,TI.ACTORID_ AS ACTORID,TI.ID_ AS TASKID,TI.START_ AS TASKSTARTDATE,TI.END_ AS TASKENDDATE ");
				sql
				.append(" FROM JBPM_TASKINSTANCE TI WHERE TI.PROCINST_ IN(");
				sql.append(params.toString());
				sql
				.append(")  AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
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
				if(taskMap == null){
					continue;
				}
				List<Long> taskIdListTemp = new LinkedList<Long>();
				taskIdListTemp.add( Long.parseLong(StringUtil.castString(taskMap.get("TASKID")))); 


				Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = sendDocUploadManager
						.getTaskIdMapPreTaskBeanTemper(taskIdListTemp);
				
					TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp.get(StringUtil.castString(taskMap
							.get("TASKID")));
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
				StringBuilder cruuentUser = new StringBuilder();
				if(taskbusinessbean.getWorkflowEndDate() == null){
					List[] listTemp = sendDocUploadManager
					.getUserBeanTempArrayOfProcessStatusByPiId(
							taskbusinessbean.getInstanceId(),
							AllUserIdMapUserBeanTem);
					taskbusinessbean.setDeptName(listTemp[1].toString());
					for(int index = 0;index<listTemp[0].size(); index++){
						cruuentUser.append(","+listTemp[0].get(index).toString()+"["+listTemp[1].get(index).toString()+"]");
					}
					if(cruuentUser.length()>0){
						cruuentUser.deleteCharAt(0);
					}
				}else{
					cruuentUser.append("已办结");
				}
				taskbusinessbean.setActorName(cruuentUser.toString());
				
				taskbusinessbean.setTaskId(StringUtil.castString(taskMap
						.get("TASKID")));
//				taskbusinessbean.setTaskEndDate((Date) taskMap
//						.get("TASKENDDATE"));
				
				try {
					taskbusinessbean.setTaskEndDate(sdf.parse(StringUtil.castString(taskMap
							.get("TASKENDDATE")==null?"":taskMap
									.get("TASKENDDATE"))));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				taskbusinessbean.setTaskStartDate((Date) taskMap
//						.get("TASKSTARTDATE"));
				
				try {
					taskbusinessbean.setTaskStartDate(sdf.parse(StringUtil.castString(taskMap
							.get("TASKSTARTDATE")==null?"":taskMap
									.get("TASKSTARTDATE"))));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if("1".equals(parameter.getState())){
					taskbusinessbean.setMainActorName(workflow.getMainActorInfoByProcessInstanceId(taskbusinessbean.getInstanceId()).getUserName());
				}
			}
			page.setResult(new ArrayList<TaskBusinessBean>(beans));
		}
	} catch (ServiceException e) {
		throw new ServiceException(MessagesConst.operation_error,
				new Object[] { "获取我的在办工作" });
	}
	return page;
}

/**
 * 查询我的退回文件
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
 * @author:xush getMytodoWorks方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
 */
@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public Page getReturnWorks(Page page, ProcessedParameter parameter,
		OALogInfo... infos) throws SystemException, ServiceException {
	try {
		
		String workflowName = parameter.getWorkflowName();
		String formId = parameter.getFormId();
		String workflowType = parameter.getWorkflowType();
		String userName = parameter.getUserName();
		Date startDate = parameter.getStartDate();
		Date endDate = parameter.getEndDate();
		String isBackSpace = parameter.getIsBackSpace();
		String type = parameter.getType();
		String excludeWorkflowType = parameter.getExcludeWorkflowType();
		String orgId = parameter.getOrgId();

		// 增加工作流中的显示字段

//		String businessName = ServletActionContext.getRequest()
//				.getParameter(WORKFLOW_TITLE);
		//隐藏上面  解决bug0000049435
		String businessName = parameter.getBusinessName();

		StringBuilder customQuery = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customSelectItems = new StringBuilder();
		customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		customSelectItems.append("pi.BUSINESS_NAME_,pi.START_,pi.ID_,pi.NAME_,pi.END_,pi.TYPE_ID_");
		customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
		Object[] sItems = { "taskId", "taskStartDate", "isBackspace",
				"assignType", "taskNodeId", "taskNodeName", "isReceived"};
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "2");// 取非办结任务
		paramsMap.put("isBackspace", "1");// 取非办结任务
		if (customQuery.length() == 0) {
			customQuery.append("  pi.ISSUSPENDED_ = 0 ");
		} else {
			customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
		}
		User curuser = userService.getCurrentUser();
		paramsMap.put("startUserId", curuser.getUserId());// 当前用户办理任务
//		// 根据处室Id 取得该处室全部(待签收文件)
//		if (orgId != null) {
//			List<User> userList = userService.getUsersByOrgID(orgId);
//			if (!userList.isEmpty()) {
//				User firstUser = userList.get(0);
//				paramsMap.put("handlerId", firstUser.getUserId());// 当前用户办理任务
//			}
//		} else {
//			paramsMap.put("handlerId", curuser.getUserId());// 当前用户办理任务
//		}

		String[] adpterWorkflowType = CustomQueryAdpter
				.adpterWorkflowType(new String[] { workflowType,
						excludeWorkflowType });
		workflowType = adpterWorkflowType[0];
		excludeWorkflowType = adpterWorkflowType[1];
		if (workflowType != null) {
			CustomQueryUtil.genWorkflowTypeStringForSql(workflowType,
					customQuery);
		}
		if (excludeWorkflowType != null) {
			CustomQueryUtil.genExcludeWorkflowTypeStringForSql(
					excludeWorkflowType, customQuery);
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
		if(businessName != null && !"".equals(businessName)){
			paramsMap.put("businessName", "%"+businessName+"%");
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
		paramsMap.put("toAssignHandlerId", userService.getCurrentUser()
				.getUserId());
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processStartDate", "1");

		// 收文编号
		String RECV_NUM = ServletActionContext.getRequest().getParameter(
				"RECV_NUM");
		if (RECV_NUM != null && !"".equals(RECV_NUM)) {
			if (customQuery.length() == 0) {
				customQuery.append(" jbpmbusiness.RECV_NUM like '%"
						+ RECV_NUM + "%' ");
			} else {
				customQuery.append("and jbpmbusiness.RECV_NUM like '%"
						+ RECV_NUM + "%' ");
			}
		}
		// 来文字号
		String DOC_NUMBER = ServletActionContext.getRequest().getParameter(
				"DOC_NUMBER");
		if (DOC_NUMBER != null && !"".equals(DOC_NUMBER)) {
			if (customQuery.length() == 0) {
				customQuery.append(" jbpmbusiness.DOC_NUMBER like '%"
						+ DOC_NUMBER + "%' ");
			} else {
				customQuery.append("and jbpmbusiness.DOC_NUMBER like '%"
						+ DOC_NUMBER + "%' ");
			}
		}
		// 来文单位
		String ISSUE_DEPART_SIGNED = ServletActionContext.getRequest()
				.getParameter("ISSUE_DEPART_SIGNED");
		if (ISSUE_DEPART_SIGNED != null && !"".equals(ISSUE_DEPART_SIGNED)) {
			if (customQuery.length() == 0) {
				customQuery
						.append(" jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
								+ ISSUE_DEPART_SIGNED + "%' ");
			} else {
				customQuery
						.append("and jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
								+ ISSUE_DEPART_SIGNED + "%' ");
			}
		}

		// 是否签收
		String isReceived = ServletActionContext.getRequest().getParameter(
				"isReceived");
		if (isReceived != null && !"".equals(isReceived)
				&& !"null".equals(isReceived)) {
			if ("1".equals(isReceived)) {
				customQuery.append(" and ti.ISRECEIVE_  =  1");
			} else {
				customQuery.append(" and ti.ISRECEIVE_  is null ");
			}
		}

		if (customQuery.length() == 0) {
			customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (")
					.append(Tjbpmbusiness.getShowableBusinessType())
					.append(") ");
		} else {
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
					.append(Tjbpmbusiness.getShowableBusinessType())
					.append(") ");
		}

		initTodoSign(type, customSelectItems, customFromItems, customQuery);

		type = null;
		page.setAutoCount(true);
		page = workflow.getTaskInfosByConditionForPage(page, toSelectItems,
				paramsMap, orderMap, customSelectItems.toString(),
				customFromItems.toString(), customQuery.toString(), null,
				null);

		List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (page != null && page.getResult() != null) {
			List<String> processInstanceIds = new LinkedList<String>();
			StringBuilder params = new StringBuilder();
			for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
				TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
				taskbusinessbean.setInstanceId(StringUtil
						.castString(objs[9]));
				taskbusinessbean.setBusinessName(StringUtil
						.castString(objs[7]));
				//taskbusinessbean.setWorkflowStartDate((Date) objs[8]);
				try {
					taskbusinessbean.setWorkflowStartDate(objs[8]==null?null:sf.parse(StringUtil
							.castString(objs[8])));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				taskbusinessbean.setWorkflowName(StringUtil
						.castString(objs[10]));
				taskbusinessbean.setWorkflowType(StringUtil
						.castString(objs[12]));
				//taskbusinessbean.setWorkflowEndDate((Date) objs[11]);
				try {
					taskbusinessbean.setWorkflowEndDate(objs[11]==null?null:sf.parse(StringUtil
							.castString(objs[11])));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beans.add(taskbusinessbean);
				processInstanceIds.add(taskbusinessbean.getInstanceId());
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
				.append(")  AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
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
				if(taskMap == null){
					continue;
				}
				StringBuilder cruuentUser = new StringBuilder();
				if(taskbusinessbean.getWorkflowEndDate() == null){
					List[] listTemp = sendDocUploadManager
					.getUserBeanTempArrayOfProcessStatusByPiId(
							taskbusinessbean.getInstanceId(),
							AllUserIdMapUserBeanTem);
					for(int index = 0;index<listTemp[0].size(); index++){
						cruuentUser.append(","+listTemp[0].get(index).toString()+"["+listTemp[1].get(index).toString()+"]");
					}
					if(cruuentUser.length()>0){
						cruuentUser.deleteCharAt(0);
					}
				}else{
					cruuentUser.append("已办结");
				}
				taskbusinessbean.setActorName(cruuentUser.toString());
				taskbusinessbean.setTaskId(StringUtil.castString(taskMap
						.get("TASKID")));
//				taskbusinessbean.setTaskEndDate((Date) taskMap
//						.get("TASKENDDATE"));
			
					try {
						taskbusinessbean.setTaskEndDate(sf.parse(StringUtil.castString(taskMap
								.get("TASKENDDATE")==null?"":taskMap
										.get("TASKENDDATE"))));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
//				taskbusinessbean.setTaskStartDate((Date) taskMap
//						.get("TASKSTARTDATE"));
				
				try {
					taskbusinessbean.setTaskStartDate(sf.parse(StringUtil.castString(taskMap
							.get("TASKSTARTDATE")==null?"":taskMap
									.get("TASKSTARTDATE"))));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if("1".equals(parameter.getState())){
					taskbusinessbean.setMainActorName(workflow.getMainActorInfoByProcessInstanceId(taskbusinessbean.getInstanceId()).getUserName());
				}
			}
			page.setResult(new ArrayList<TaskBusinessBean>(beans));
		}
	} catch (ServiceException e) {
		throw new ServiceException(MessagesConst.operation_error,
				new Object[] { "获取我的在办工作" });
	}
	return page;
}
}