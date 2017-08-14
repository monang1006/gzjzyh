package com.strongit.oa.traceDoc;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaTraceDoc;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * **************************************************************
 * <p>
 * 
 * @CLASS : TraceDocManager
 * @DESCRIPTION :
 * @AUTHOR : formice
 * @VERSION : v1.0
 * @DATE : 2011-11-8 上午10:34:47
 *       </p>
 * 
 * <p>
 * @MODIFY LOG :
 * @AUTHOR : Jianggb
 * @VERSION : v2.0
 * @MODIFY DATE :
 *         </p>
 * @classpath com.strongit.oa.traceDoc.TraceDocManager
 *            ***************************************************************
 */
@Service
@Transactional
public class TraceDocManager {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private GenericDAOHibernate<ToaTraceDoc, java.lang.String> traceDocDao;

	@Autowired
	protected IWorkflowService workflow; // 工作流服务

	@Autowired
	protected IUserService userService;// 统一用户服务

	private String searchDateString;

	@Autowired
	SendDocManager senddocmanager;

	@Autowired
	public void setSessionFactory(SessionFactory session) {
		traceDocDao = new GenericDAOHibernate<ToaTraceDoc, java.lang.String>(
				session, ToaTraceDoc.class);
	}

	/**
	 * 
	 * @description 保存重要文件跟踪bo对象
	 * @author Jianggb
	 * @param cal
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveToaTraceDoc(ToaTraceDoc cal) throws SystemException,
			ServiceException {
		try {
			traceDocDao.save(cal);

		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "跟踪文件" });
		}
	}

	/**
	 * 根据流程实例ID保存重要文件关联
	 * 
	 * @description
	 * @author Jianggb
	 * @param instanceId
	 *            流程实例 ID
	 * @param workFlowName
	 *            流程名称
	 * @param businessName
	 *            文件标题
	 * @param userId
	 *            当前用户ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveTraceDoc(String instanceId, String workFlowName,
			String businessName, String userId) throws SystemException,
			ServiceException {
		try {
			if (instanceId != null && !instanceId.equals("")) {
				List list = this.getToaTraceDocByConId(instanceId, userId);
				if (list.size() == 0 || list == null) {
					ToaTraceDoc cal = new ToaTraceDoc();
					cal.setTraceId(null);
					cal.setTraceIntenceId(instanceId);
					cal.setTraceProcessName(workFlowName);
					cal.setTraceProcessTitle(businessName);
					cal.setTraceUserId(userId);
					cal.setTraceDate(new Date());
					traceDocDao.save(cal);
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "跟踪文件" });
		}
	}

	/**
	 * 
	 * @description 根据流程实例ID与用户ID得到重要文件
	 * @author Jianggb
	 * @param conId
	 * @param userid
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaTraceDoc> getToaTraceDocByConId(String conId, String userid)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaTraceDoc t where t.traceIntenceId =? and t.traceUserId=?";
		return traceDocDao.find(hql, conId, userid);
	}

	/**
	 * 
	 * @description 根据用户id获得重要文件列表
	 * @author Jianggb
	 * @param conId
	 *            用户ID
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaTraceDoc> getToaTraceDocByUserid(String conId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaTraceDoc t where t.traceUserId =?";
		return traceDocDao.find(hql, conId);
	}

	/**
	 * 根据用户ID得到流程实例集合
	 * 
	 * @description
	 * @author Jianggb
	 * @param userName
	 *            用户ID
	 * @return
	 */
	public List<Long> findDefinitionByUserId(String userName) {
		List<Long> result = new ArrayList<Long>();
		List<ToaTraceDoc> list = this.getToaTraceDocByUserid(userName);
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				ToaTraceDoc objs = (ToaTraceDoc) list.get(i);
				String s = objs.getTraceIntenceId();
				result.add(new Long(s));
			}
		}
		return result;
	}

	/**
	 * 
	 * @description 根据流程实例ID和用户ID删除重要文件关联
	 * @author Jianggb
	 * @param insid
	 * @param userid
	 */
	public void deleteToaTraceDocs(String insid, String userid) {
		List<ToaTraceDoc> list = this.getToaTraceDocByConId(insid, userid);
		if (list != null && list.size() > 0) {
			traceDocDao.delete(list);
		}
	}

	/**
	 * 
	 * @description 得到当前用户跟踪的未结束的重要文件PAGE对象
	 * @author Jianggb
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page getTodoTraces(Page page) {
		try {
			Object[] toSelectItems = { "startUserName", "processName",
					"processInstanceId", "businessName", "startUserId",
					"processMainFormBusinessId", "processEndDate",
					"processStartDate", "processTypeId", "processMainFormId" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();

			paramsMap.put("processStatus", "0");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起
			// paramsMap.put("handlerId",
			// userService.getCurrentUser().getUserId());//当前用户办理任务
			List<Long> instanceIds = this.findDefinitionByUserId(userService
					.getCurrentUser().getUserId());
			if (instanceIds != null && instanceIds.size() > 0) {
				paramsMap.put("processInstanceId", instanceIds);
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("processStartDate", "1");
				page.setAutoCount(true);
				page = workflow.getProcessInstanceByConditionForPage(page,
						sItems, paramsMap, orderMap);
				List list = page.getResult();
				if (list != null && !list.isEmpty()) {// 添加非空判断 yanjian
					// 2012-08-16
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);
						if (objs[6] == null) {
							objs[2] = objs[2] + "$" + "0";
						} else {
							objs[2] = objs[2] + "$" + "1";
						}
						list.set(i, objs);
					}
				}
				// page = workflow.getTaskInfosByConditionForPage(page, sItems,
				// paramsMap, orderMap, null, null, null, null);
				// List list = workflow.getTaskInfosByConditionForList( sItems,
				// paramsMap, orderMap, null, null, null, null);
				// if(list!=null){
				// for(int i=0;i<list.size();i++){
				// for(int j=i+1;j<list.size();j++){
				// Object[] obj=(Object[]) list.get(i);
				// Object[] objs=(Object[]) list.get(j);
				// if(obj[4].equals(objs[4])){
				// list.remove(j);
				// j--;
				// }
				// }
				// }
				// }
				// for(int i=0;i<list.size();i++){
				// Object[] objs=(Object[]) list.get(i);
				// if(objs[8] == null){
				// objs[4] = objs[4]+"$"+"0";
				// }else{
				// objs[4] = objs[4]+"$"+"1";
				// }
				// list.set(i, objs);
				// }
				// page = ListUtils.splitList2Page(page, list);
			}
		} catch (Exception e) {
			logger.error("得到重要文件出错", e);
		}
		return page;
	}

	/**
	 * 生成重要文件跟踪流程类型别名链接
	 * 
	 * @description
	 * @author
	 * @createTime 2014年2月14日9:41:33
	 * @return StringBuilder
	 */
	public StringBuilder genProcessedWorkflowNameLink(String workflowName,
			String formId, String workflowType, String rootPath)
			throws DAOException, ServiceException, SystemException {
		try {
			if (workflowName == null || "".equals(workflowName)) {
				logger.error("参数workflowName为空或空字符串");
				// throw new ServiceException("参数workflowName不能为空或空字符串");
			}
			if (formId == null || "".equals(formId)) {
				logger.error("参数formId不能为空或空字符串");
				throw new ServiceException("参数formId不能为空或空字符串");
			}
			/*
			 * if (workflowType == null || "".equals(workflowType)) {
			 * logger.error("参数workflowType不能为空或空字符串"); throw new
			 * ServiceException("参数workflowType不能为空或空字符串"); }
			 */
			StringBuilder clickProcessType = new StringBuilder();
			// 获取表单id
			String[] nodeInfo = formId.split(",");
			formId = nodeInfo[nodeInfo.length - 1];
			// 流程类别
			String currworkflowType = workflowType;
			String clickProcessUrl = rootPath
					+ "/traceDoc/traceDoc!workfolwTypelist.action"
					+ "?workflowType=" + currworkflowType;

			clickProcessType.append("window.parent.refreshWorkByTitle('"
					+ clickProcessUrl + "','" + "办公门户" + "');");
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
	 * 获取流程别名信息
	 * 
	 * @description
	 * @author
	 * @createTime 2014年2月15日10:34:09
	 * @return List<Object[]>
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public String getProcessfileFileByPfName(String pfNames)
			throws SQLException {
		String workflowAliaName = "";
		String sql = "";
		Connection con = senddocmanager.getCurrentConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		if (pfNames != null && !"".equals(pfNames)) {
			sql = "select REST2 from T_WF_BASE_PROCESSFILE where PF_NAME='"
					+ pfNames + "'";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			if (rs.next()) {
				workflowAliaName = rs.getString("REST2");
			}
		}
		return workflowAliaName;
	}

	@SuppressWarnings("unchecked")
	public Page getTodoTraces(Page page, String traceProcessTitle,
			String startUserName, Date searchDate) {
		try {
			Object[] toSelectItems = { "startUserName", "processName",
					"processInstanceId", "businessName", "startUserId",
					"processMainFormBusinessId", "processEndDate",
					"processStartDate" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();

			paramsMap.put("processStatus", "0");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起
			// paramsMap.put("handlerId",
			// userService.getCurrentUser().getUserId());//当前用户办理任务
			List<Long> instanceIds = this.findDefinitionByUserId(userService
					.getCurrentUser().getUserId());
			if (instanceIds != null && instanceIds.size() > 0) {

				paramsMap.put("processInstanceId", instanceIds);
				// 标题
				if (traceProcessTitle != null && !"".equals(traceProcessTitle)) {
					traceProcessTitle = "%" + traceProcessTitle + "%";
					paramsMap.put("businessName", traceProcessTitle);
				}

				// 发起人人员
				if (startUserName != null && !"".equals(startUserName)) {
					paramsMap.put("startUserName", "%" + startUserName + "%");
				}
				// 启动时间
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
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("processStartDate", "1");
				page.setAutoCount(true);
				page = workflow.getProcessInstanceByConditionForPage(page,
						sItems, paramsMap, orderMap);
				if (page == null) {
					return null;
				}
				List list = page.getResult();
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);
						if (objs[6] == null) {
							objs[2] = objs[2] + "$" + "0";
						} else {
							objs[2] = objs[2] + "$" + "1";
						}
						list.set(i, objs);
					}
				}
			}
		} catch (Exception e) {
			logger.error("得到重要文件出错", e);
		}
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page getTodoTracesBy(Page page, String traceProcessTitle,
			String startUserName, Date searchDate, String workflowType) {
		try {
			Object[] toSelectItems = { "startUserName", "processName",
					"processInstanceId", "businessName", "startUserId",
					"processMainFormBusinessId", "processEndDate",
					"processStartDate" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			paramsMap.put("processStatus", "0");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起
			List<Long> instanceIds = this.findDefinitionByUserId(userService
					.getCurrentUser().getUserId());
			if (instanceIds != null && instanceIds.size() > 0) {
				customSelectItems.append("pi.TYPE_ID_,pi.BUSINESS_ID");
				customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
				customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
				if (workflowType != null && workflowType.length() > 0) {
					customQuery.append(" and pi.TYPE_ID_ in(" + workflowType
							+ ")");
				}

				paramsMap.put("processInstanceId", instanceIds);
				// 标题
				if (traceProcessTitle != null && !"".equals(traceProcessTitle)) {
					traceProcessTitle = "%" + traceProcessTitle + "%";
					paramsMap.put("businessName", traceProcessTitle);
				}

				// 发起人人员
				if (startUserName != null && !"".equals(startUserName)) {
					paramsMap.put("startUserName", "%" + startUserName + "%");
				}
				// 启动时间
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
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("processStartDate", "1");
				page.setAutoCount(true);
				// page =
				// workflow.getProcessInstanceByConditionForPage(page,sItems,
				// paramsMap, orderMap);
				page = workflow.getProcessInstanceByConditionForPage(page,
						sItems, paramsMap, orderMap, customSelectItems
								.toString(), customFromItems.toString(),
						customQuery.toString(), null, null);
				if (page == null) {
					return null;
				}
				List list = page.getResult();
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);
						if (objs[6] == null) {
							objs[2] = objs[2] + "$" + "0";
						} else {
							objs[2] = objs[2] + "$" + "1";
						}
						list.set(i, objs);
					}
				}
			}
		} catch (Exception e) {
			logger.error("得到重要文件出错", e);
		}
		return page;
	}

	/**
	 * 
	 * @description 得到当前用户发起的未结束的流程PAGE对象
	 * @author Jianggb
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page getTodoTracesByUserName(Page page) {
		try {
			Object[] toSelectItems = { "taskId", "taskStartDate",
					"startUserName", "processName", "processInstanceId",
					"businessName", "startUserId", "processMainFormBusinessId" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			// paramsMap.put("taskType", "1");//取非办结任务
			// paramsMap.put("handlerId",
			// userService.getCurrentUser().getUserId());//当前用户办理任务
			// List<Long>
			// instanceIds=this.findDefinitionByUserId(userService.getCurrentUser().getUserId());
			String startUserName = userService.getCurrentUser().getUserId();
			if (startUserName != null && !"".equals(startUserName)) {
				startUserName = URLDecoder.decode(startUserName, "utf-8");
				paramsMap.put("startUserId", startUserName);
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("processStartDate", "1");
				page = workflow.getTaskInfosByConditionForPage(page, sItems,
						paramsMap, orderMap, null, null, null, null);
			}
		} catch (Exception e) {
			logger.error("得到在办文件出错", e);
		}
		return page;
	}

	/**
	 * 在办文件跟踪分页
	 * 
	 * @description
	 * @author 严建
	 * @param page
	 * @return
	 * @createTime Jun 7, 2012 11:32:29 AM
	 */
	public Page getDoingTracesByUserName(Page page) {
		Object[] sItems = { "processInstanceId", "processStartDate",
				"startUserName", "processName", "processMainFormBusinessId",
				"businessName" };
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processStatus", "0");// 取非办结任务
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		paramsMap.put("startUserId", userService.getCurrentUser().getUserId());
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processStartDate", "1");
		page = workflow.getProcessInstanceByConditionForPage(page,
				toSelectItems, paramsMap, orderMap, null, null, null, null);
		return page;
	}

	/**
	 * 
	 * @description
	 * @author Jianggb
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getDoingTracesByUserName() {
		List list = new ArrayList();
		try {
			Object[] toSelectItems = { "taskId", "taskStartDate",
					"startUserName", "processName", "processInstanceId",
					"businessName", "startUserId", "processMainFormBusinessId",
					"isReceived" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("processStatus", "0");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			String startUserName = userService.getCurrentUser().getUserId();
			if (startUserName != null && !"".equals(startUserName)) {
				startUserName = URLDecoder.decode(startUserName, "utf-8");
				paramsMap.put("startUserId", startUserName);
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("processStartDate", "1");
				list = workflow.getTaskInfosByConditionForList(sItems,
						paramsMap, orderMap, null, null, null, null);
			}
		} catch (Exception e) {
			logger.error("得到在办文件出错", e);
		}
		return list;
	}

	public List getDoingTracesById(String intanceid) {
		List list = new ArrayList();
		try {
			Object[] toSelectItems = { "taskId", "taskStartDate",
					"startUserName", "taskNodeName", "processInstanceId",
					"businessName", "startUserId", "processMainFormBusinessId",
					"isReceived" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("processStatus", "0");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起
			if (intanceid != null && !intanceid.equals("")) {
				paramsMap.put("processInstanceId", intanceid);
			}
			String startUserName = userService.getCurrentUser().getUserId();
			if (startUserName != null && !"".equals(startUserName)) {
				startUserName = URLDecoder.decode(startUserName, "utf-8");
				paramsMap.put("startUserId", startUserName);
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("processStartDate", "1");
				list = workflow.getTaskInfosByConditionForList(sItems,
						paramsMap, orderMap, null, null, null, null);
			}
		} catch (Exception e) {
			logger.error("得到在办文件出错", e);
		}
		return list;
	}

	public String getSearchDateString() {
		return searchDateString;
	}

	public void setSearchDateString(String searchDateString) {
		this.searchDateString = searchDateString;
	}
}