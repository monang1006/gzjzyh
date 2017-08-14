package com.strongit.oa.senddoc.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * Manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Dec 20, 2011 3:47:40 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SendDocBaseManager
 */
@Service
@Transactional
@OALogger
public class SendDocBaseManager {
	@Autowired
	protected IUserService userService;// 统一用户服务

	@Autowired
	protected IWorkflowService workflow; // 工作流服务
	@Autowired
	protected JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持
	@Autowired
	SendDocUploadManager sendDocUploadManager;

	private static final String WORKFLOW_NAME = "WORKFLOWNAME";// 流程名称字段名

	private static final String WORKFLOW_TITLE = "WORKFLOWTITLE";// 流程标题

	private GenericDAOHibernate<Object, String> serviceDAO;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		serviceDAO = new GenericDAOHibernate<Object, String>(sessionFactory,
				Object.class);
	}

	public GenericDAOHibernate getServiceDAO() {
		return serviceDAO;
	}

	/**
	 * 去除所有的在签收节点产生的任务
	 * 
	 * @author 严建
	 * @param dlist
	 *            任务列表
	 * @param taskNodeIdIndex
	 *            节点id在数组中的索引位置
	 * @createTime Jan 11, 2012 3:57:04 PM
	 */
	public void removeSignNodeTask(List dlist, int taskNodeIdIndex) {
		List<String> nodeIdList = new LinkedList<String>();
		for (int i = 0; i < dlist.size(); i++) {
			String taskNodeId = ((Object[]) dlist.get(i))[taskNodeIdIndex]
					.toString();
			nodeIdList.add(taskNodeId);
		}
		Map<String, TwfBaseNodesetting> nodesettingMap = workflow
				.getNodesettingMapByNodeIdList(nodeIdList);
		for (int i = 0; i < dlist.size(); i++) {
			String taskNodeId = ((Object[]) dlist.get(i))[taskNodeIdIndex]
					.toString();
			TwfBaseNodesetting nodeSetting = nodesettingMap.get(taskNodeId);
			if (workflow.isSignNodeTask(taskNodeId, nodeSetting)) {
				dlist.remove(i);
				i--;
			}
		}
	}

	/**
	 * 去除所有的在签收节点产生的任务(优化)
	 * 
	 * @author 严建
	 * @param dlist
	 * @param taskNodeIdIndex
	 * @createTime Jan 12, 2012 6:04:34 PM
	 */
	public void removeSignNodeTaskByMap(List dlist, int taskNodeIdIndex) {
		List<String> nodeIdList = new LinkedList<String>();
		for (int i = 0; i < dlist.size(); i++) {
			String taskNodeId = ((Object[]) dlist.get(i))[taskNodeIdIndex]
					.toString();
			nodeIdList.add(taskNodeId);
		}
		Map<String, String> map = this
				.getNodesettingPluginValueMapByNodeIdList(
						"plugins_chkModifySuggestion", nodeIdList);
		for (int i = 0; i < dlist.size(); i++) {
			String taskNodeId = ((Object[]) dlist.get(i))[taskNodeIdIndex]
					.toString();
			if ("1".equals(map.get(taskNodeId))) {
				dlist.remove(i);
				i--;
			}
		}
	}

	/**
	 * 去除所有的不在签收节点产生的任务
	 * 
	 * @author 严建
	 * @param dlist
	 *            任务列表
	 * @param taskNodeIdIndex
	 *            节点id在数组中的索引位置
	 * @createTime Jan 11, 2012 3:57:04 PM
	 */
	public void removeNotSignNodeTask(List dlist, int taskNodeIdIndex) {
		List<String> nodeIdList = new LinkedList<String>();
		for (int i = 0; i < dlist.size(); i++) {
			String taskNodeId = ((Object[]) dlist.get(i))[taskNodeIdIndex]
					.toString();
			nodeIdList.add(taskNodeId);
		}
		Map<String, TwfBaseNodesetting> nodesettingMap = workflow
				.getNodesettingMapByNodeIdList(nodeIdList);
		for (int i = 0; i < dlist.size(); i++) {
			String taskNodeId = ((Object[]) dlist.get(i))[taskNodeIdIndex]
					.toString();
			TwfBaseNodesetting nodeSetting = nodesettingMap.get(taskNodeId);
			if (workflow.isSignNodeTask(taskNodeId, nodeSetting)) {

			} else {
				dlist.remove(i);
				i--;
			}
		}
	}

	/**
	 * 处理同步子流程，并返回经过处理之后的索引位置
	 * 
	 * @author 严建
	 * @param index
	 *            当前所在索引位置
	 * @param listWorkflow
	 *            集合
	 * @param processInstanceId
	 *            当前流程实例id
	 * @return 经过处理之后的索引位置
	 * @createTime Jan 11, 2012 4:12:17 PM
	 */
	@SuppressWarnings("unchecked")
	public int removeSubTypeSubProcess(int index, List<Object[]> listWorkflow,
			String processInstanceId) {
		ContextInstance cxt = workflow.getContextInstance(processInstanceId);
		if (cxt != null) {
			String isSubProcess = (String) cxt
					.getVariable("com.strongit.isSubProcess");// 是否是子流程
			if (isSubProcess != null && "1".equals(isSubProcess)) {// 是子流程
				String subType = (String) cxt
						.getVariable("com.strongit.subType");// 是否是同步
				if (subType != null && "1".equals(subType)) {// 是同步子流程
					List<Object[]> parentInstances = workflow
							.getMonitorParentInstanceIds(new Long(
									processInstanceId));
					if (parentInstances != null && !parentInstances.isEmpty()) {
						Object[] parentInstance = parentInstances
								.get(parentInstances.size() - 1);
						List<String> itemsList2 = new LinkedList<String>();
						itemsList2.add("processEndDate");
						itemsList2.add("processInstanceId");
						Map<String, Object> paramsMap2 = new HashMap<String, Object>();
						paramsMap2.put("processInstanceId", parentInstance[0]);// 取已办结任务
						System.out.println(parentInstance[0]);
						List<Object[]> plist = workflow
								.getProcessInstanceByConditionForList(
										itemsList2, paramsMap2, null, "", "",
										"", null);
						Object[] p = plist.get(0);
						if (p[0] == null) {
							listWorkflow.remove(index);
							index--;
						}
					}

				}
			}

		}
		return index;
	}

	/**
	 * 初始化已办列表要显示的数据
	 * 
	 * @author 严建
	 * @param map
	 *            要显示的数据结构
	 * @param pkFieldName
	 *            主键名称
	 * @param info
	 *            单条数据信息
	 * @param parmMap
	 *            自定义要显示的数据结构
	 * @createTime Jan 11, 2012 4:28:43 PM
	 */
	@SuppressWarnings("unchecked")
	public void initProcessedShowMap(Map map, String pkFieldName,
			TaskBusinessBean taskbusinessbean, Map parmMap) {
		if (taskbusinessbean.getWorkflowEndDate() != null) {
			if (parmMap.containsKey("cruuentUserName")
					&& parmMap.get("cruuentUserName") == null) {
				parmMap.put("cruuentUserName", "");
			}
			if (parmMap.containsKey("cruuentUserDept")
					&& parmMap.get("cruuentUserDept") == null) {
				parmMap.put("cruuentUserDept", "结束");
			}
		}
		// map.put("startUserName", taskbusinessbean.getStartUserName());//
		// 拟稿人名称
		map.put("startUserName", workflow.getMainActorInfoByProcessInstanceId(
				taskbusinessbean.getInstanceId()).getUserName());// 拟稿人名称
		map.put("taskEndDate", taskbusinessbean.getTaskEndDate());// 接收日期
		map.put("processStartDate", taskbusinessbean.getWorkflowStartDate());// 流程启动时间
		map.put("processEndDate", taskbusinessbean.getWorkflowEndDate());// 流程启动时间
		map.put("WORKFLOWNAME", taskbusinessbean.getWorkflowName());
		map.put("businessType", taskbusinessbean.getBusinessType());
		map.put("businessId", taskbusinessbean.getBsinessId());
		map.putAll(parmMap);
		map.put("taskStartDate", taskbusinessbean.getTaskStartDate());
		String workFlowTitle = (String) map.get("WORKFLOWTITLE");
		if(workFlowTitle == null){
			map.put("WORKFLOWTITLE", 	taskbusinessbean.getBusinessName());
		}
		map.put(pkFieldName, taskbusinessbean.getTaskId() + "$"
				+ taskbusinessbean.getInstanceId() + "$"
				+ (taskbusinessbean.getWorkflowEndDate() == null ? "0" : "1")
				+"$"+taskbusinessbean.getBusinessType());// 替换主键,即选择框的Value值,返回：任务id$流程实例id
	}
	/**
	 * 初始化已办列表要显示的数据
	 * 
	 * @author 严建
	 * @param map
	 *            要显示的数据结构
	 * @param pkFieldName
	 *            主键名称
	 * @param info
	 *            单条数据信息
	 * @param parmMap
	 *            自定义要显示的数据结构
	 * @createTime Jan 11, 2012 4:28:43 PM
	 */
	@SuppressWarnings("unchecked")
	public void initProcessedShowMap1(Map map, String pkFieldName,
			TaskBusinessBean taskbusinessbean, Map parmMap,String isExsitTodo) {
		if (taskbusinessbean.getWorkflowEndDate() != null) {
			if (parmMap.containsKey("cruuentUserName")
					&& parmMap.get("cruuentUserName") == null) {
				parmMap.put("cruuentUserName", "");
			}
			if (parmMap.containsKey("cruuentUserDept")
					&& parmMap.get("cruuentUserDept") == null) {
				parmMap.put("cruuentUserDept", "结束");
			}
		}
		// map.put("startUserName", taskbusinessbean.getStartUserName());//
		// 拟稿人名称
		map.put("startUserName", workflow.getMainActorInfoByProcessInstanceId(
				taskbusinessbean.getInstanceId()).getUserName());// 拟稿人名称
		map.put("taskEndDate", taskbusinessbean.getTaskEndDate());// 接收日期
		map.put("processStartDate", taskbusinessbean.getWorkflowStartDate());// 流程启动时间
		map.put("processEndDate", taskbusinessbean.getWorkflowEndDate());// 流程启动时间
		map.put("WORKFLOWNAME", taskbusinessbean.getWorkflowName());
		map.put("businessType", taskbusinessbean.getBusinessType());
		map.put("businessId", taskbusinessbean.getBsinessId());
		map.putAll(parmMap);
		map.put("taskStartDate", taskbusinessbean.getTaskStartDate());
		String workFlowTitle = (String) map.get("WORKFLOWTITLE");
		if(workFlowTitle == null){
			map.put("WORKFLOWTITLE", 	taskbusinessbean.getBusinessName());
		}
		map.put(pkFieldName, taskbusinessbean.getTaskId() + "$"
				+ taskbusinessbean.getInstanceId() + "$"
				+ (taskbusinessbean.getWorkflowEndDate() == null ? "0" : "1")
				+"$"+taskbusinessbean.getBusinessType()+"$"+taskbusinessbean.getNodeId()+"$"+isExsitTodo);// 替换主键,即选择框的Value值,返回：任务id$流程实例id
	}

	/**
	 * 初始化待办列表要显示的数据
	 * 
	 * @author 严建
	 * @param map
	 *            要显示的数据结构
	 * @param pkFieldName
	 *            主键名称
	 * @param info
	 *            单条数据信息
	 * @param parmMap
	 *            自定义要显示的数据结构
	 * @createTime Jan 11, 2012 4:28:43 PM
	 */
	@SuppressWarnings("unchecked")
	public void initTodoShowMap(Map map, String pkFieldName,
			TaskBusinessBean taskbusinessbean, Map parmMap) {
		if (map == null) {
			map = new HashMap();
		}
		map.put("startUserName", taskbusinessbean.getStartUserName());// 拟稿人名称
		map.put("taskStartDate", taskbusinessbean.getTaskStartDate());// 接收日期
		map.put("taskNodeName", taskbusinessbean.getNodeName());
		map.put("processStartDate", taskbusinessbean.getWorkflowStartDate());
		map.putAll(parmMap);
		map.put(WORKFLOW_TITLE, taskbusinessbean.getBusinessName());
		map.put(WORKFLOW_NAME, taskbusinessbean.getWorkflowName());
		map.put("assignType", taskbusinessbean.getAssignType());
		map.put("isBackspace", taskbusinessbean.getIsBackspace());
		map.put("isReceived", taskbusinessbean.getIsReceived());
		map.put(pkFieldName, taskbusinessbean.getTaskId() + "$"
				+ taskbusinessbean.getInstanceId());// 替换主键,即选择框的Value值,返回：任务id$流程实例id
		map.put("taskId", taskbusinessbean.getTaskId());
	}

	/**
	 * 根据一组流程实例id获取签收节点产生的任务信息
	 * 
	 * @author 严建
	 * @param processInstanceIds
	 * @return
	 * @createTime Jan 11, 2012 5:19:03 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, TaskInstance> getSignTaskInfoForMap(
			List<String> processInstanceIds) {
		List<Long> processInstanceIdsL = new LinkedList<Long>();
		for (String processInstanceId : processInstanceIds) {
			processInstanceIdsL.add(new Long(processInstanceId));
		}
		Map<String, TaskInstance> TaskInstances = new HashMap<String, TaskInstance>();
		Object[] toSelectItems = { "taskId", "processInstanceId", "taskNodeId" };
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processInstanceId", processInstanceIdsL);// 取办结任务
		paramsMap.put("taskType", "3");// 取办结任务
		Map orderMap = new HashMap<Object, Object>();
		List<Object[]> listWorkflow = workflow.getTaskInfosByConditionForList(
				sItems, paramsMap, orderMap, null, null, null, null);

		/* tasknodeid 在数组中的索引位置为2 */
		this.removeNotSignNodeTask(listWorkflow, 2);
		for (int i = 0; i < listWorkflow.size(); i++) {
			Object[] objs = listWorkflow.get(i);
			String processInstanceId = objs[1].toString();
			String taskId = objs[0].toString();
			TaskInstance taskinstance = workflow.getTaskInstanceById(taskId);
			TaskInstances.put(processInstanceId, taskinstance);
		}
		return TaskInstances;
	}

	/**
	 * 根据一组流程实例id获取流程签收人的信息
	 * 
	 * @author 严建
	 * @param processInstanceIds
	 * @param usermap
	 * @return
	 * @createTime Jan 12, 2012 3:14:37 PM
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public Map<String, UserBeanTemp> getSignTaskInfoUserInfoForMap(
			List<String> processInstanceIds, Map<String, UserBeanTemp> usermap) {
		Map<String, UserBeanTemp> map = new HashMap<String, UserBeanTemp>();
		Map<String, TaskInstance> taskinstancesMap = this
				.getSignTaskInfoForMap(processInstanceIds);
		List<String> keySet = new ArrayList(taskinstancesMap.keySet());// keySet
		// 存在签收节点的流程实例的id
		for (int i = 0; i < keySet.size(); i++) {
			String processInstancId = keySet.get(i);
			TaskInstance taskinstance = taskinstancesMap.get(processInstancId);
			String signUserId = taskinstance.getActorId();
			UserBeanTemp signUserInfo = usermap.get(signUserId);
			map.put(processInstancId, signUserInfo);
		}
		return map;
	}

	/**
	 * 获取签收处室和主办人员
	 * 
	 * @author 严建
	 * @param businessId
	 * @return	【主办人员名称，处室名称】
	 * @createTime Mar 24, 2012 3:44:38 PM
	 */
	@Deprecated
	public String[] getSignInfo(String businessId){
		String[] objs = new String[2];
		String tableName = businessId.split(";")[0];
		String pkName = businessId.split(";")[1];
		String pkValue = businessId.split(";")[2];
		Map map = jdbcTemplate.queryForMap("SELECT CSQS,ZBCS FROM "+tableName+"   WHERE "+pkName+"='"+pkValue+"' ");
		objs[0] = StringUtil.castString( map.get("CSQS")==null?"":map.get("CSQS"));
		objs[1] = StringUtil.castString( map.get("ZBCS")==null?"":map.get("ZBCS"));
		return objs;
	}
	/**
	 * 获取签收处室和主办人员
	 * 
	 * @author 严建
	 * @param businessId
	 * @return	【主办人员名称，处室名称】
	 * @createTime Mar 24, 2012 3:44:38 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String[]> getSignInfo(String[] businessIds) throws ServiceException,DAOException, SystemException{
		try {
			Map<String,String[]> result = null;
			if(businessIds != null || businessIds.length > 0 ){
				List<String> paramList = new ArrayList<String>();
				String tableName = null;
				String pkName = null;
				StringBuilder paramString = new StringBuilder();
				for(String businessId:businessIds){
					if(businessId != null){
						String[] busIds = businessId.split(";");
						if(tableName == null){
							tableName = busIds[0];
						}
						if(pkName == null){
							pkName = busIds[1];
						}
						paramList.add(busIds[2]);
						paramString.append(",?");
					}
				}
				String[] oatablepkis = null;
				if(paramList != null && !paramList.isEmpty()){
					if(oatablepkis == null){
						oatablepkis = new String[paramList.size()];
					}
					paramList.toArray(oatablepkis);
					paramString.deleteCharAt(0);
					StringBuilder SQL = new StringBuilder("SELECT CSQS,ZBCS,").append(pkName).append(" FROM ").append(tableName)
					.append("  WHERE ").append(pkName).append(" in (").append(paramString).append(")");
					List<Map> mapList = jdbcTemplate.queryForList(SQL.toString(),oatablepkis);
					if(mapList != null && !mapList.isEmpty()){
						result = new LinkedHashMap<String, String[]>(mapList.size());
						for (Map map : mapList) {
							String businessId = new StringBuilder()
							.append(tableName).append(";").append(pkName).append(";").append(StringUtil.castString(map.get(pkName))).toString();
							String[] objs = new String[2];
							objs[0] = StringUtil.castString( map.get("CSQS")==null?"":map.get("CSQS"));
							objs[1] = StringUtil.castString( map.get("ZBCS")==null?"":map.get("ZBCS"));
							result.put(businessId, objs);
						}
					}
				}
				
			}
			return result;
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
	 * 根据流程实例id过滤流程实例中相同的任务
	 * 
	 * @author 严建
	 * @param listWorkflow
	 * @param processInstanceIdIndex
	 *            流程实例在数组中的索引位置
	 * @return
	 * @createTime Jan 12, 2012 4:48:17 PM
	 */
	@SuppressWarnings("unchecked")
	public List filterTaskInfoSameProcessInstanceById(List listWorkflow,
			int processInstanceIdIndex) {
		List<String> filterList = new LinkedList<String>();
		List<Object> result = new LinkedList<Object>();
		for (int i = 0; i < listWorkflow.size(); i++) {
			Object[] objs = (Object[]) listWorkflow.get(i);
			String processInstanceId = objs[processInstanceIdIndex].toString();
			if (filterList.contains(processInstanceId)) {
			} else {
				filterList.add(processInstanceId);
				result.add(objs);
			}
		}
		return new ArrayList(result);
	}

	/**
	 * 获取指定节点插件的值
	 * 
	 * @author 严建
	 * @param pluginName
	 *            插件名称
	 * @param nodeIdList
	 *            节点id列表
	 * @return
	 * @createTime Jan 12, 2012 5:53:28 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getNodesettingPluginValueMapByNodeIdList(
			String pluginName, List<String> nodeIdList) {
		Map<String, String> result = new HashMap<String, String>();
		if (nodeIdList != null && !nodeIdList.isEmpty()) {
			int mod = nodeIdList.size() % 1000;
			int dem = nodeIdList.size() / 1000;
			int length = 0;
			if (mod != 0) {
				length = dem + 1;
			} else {
				length = dem;
			}
			StringBuilder[] paramArray = new StringBuilder[length];
			for (int i = 0; i < dem; i++) {
				int start = dem * 1000;
				int end = (dem + 1) * 1000;
				StringBuilder param = new StringBuilder();
				for (int k = start; k < end; k++) {
					param.append("?,");
				}
				param = param.deleteCharAt(param.length() - 1);
				paramArray[i] = param;
			}
			if (mod != 0) {
				StringBuilder param = new StringBuilder();
				for (int i = 0; i < mod; i++) {
					param.append("?,");
				}
				param = param.deleteCharAt(param.length() - 1);
				paramArray[dem] = param;
			}
			StringBuilder whereString = new StringBuilder();

			for (int i = 0; i < paramArray.length; i++) {
				whereString.append(" where (");
				if (i == 0) {
					whereString.append(" t.nsNodeId in (" + paramArray[0]
							+ ") ");
				} else {
					whereString.append(" or t.nsNodeId in (" + paramArray[i]
							+ ") ");
				}
				whereString.append(" )");
			}
			whereString.append(" and plugin.nspPluginName = '" + pluginName
					+ "'");
			whereString.append(" and plugin.nspNodeId = t.nsNodeId");

			List<Long> nodeIdListTemp = new LinkedList<Long>();
			for (String nodeId : nodeIdList) {
				nodeIdListTemp.add(new Long(nodeId));
			}

			StringBuffer hql = new StringBuffer("")
					.append(
							"select t.nsNodeId,plugin.value from TwfBaseNodesetting t,TwfBaseNodesettingPlugin plugin")
					.append(whereString);
			logger.info(hql.toString());
			List<Object[]> list = getServiceDAO().find(hql.toString(),
					nodeIdListTemp.toArray());
			for (Object[] object : list) {
				result.put(object[0].toString(), object[1].toString());
			}
		}
		return result;
	}

	/**
	 * 将一组业务id按照指定规则进行分组转换,返回Map格式的数据
	 * 
	 * @author 严建
	 * @param bidInfos
	 * @return 数据类型Map; 数据格式--Key(String):【表名;主键字段名】;Value(List<String>):【主键值1,主键值2...】
	 * @createTime Feb 1, 2012 3:49:10 PM
	 */
	public Map<String, List<String>> getTableNameAndpkFieldNameMapPkFieldValueArray(
			List<String> bidInfos) {
		String tableName = null, pkFieldName = null, pkFieldValue = null;
		Map<String, List<String>> tableNameAndpkFieldNameMapPkFieldValueArray = new HashMap<String, List<String>>();
		for (int i = 0; i < bidInfos.size(); i++) {
			String bidInfo = bidInfos.get(i);
			String[] args = bidInfo.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			pkFieldValue = args[2];
			String tableNameAndpkFieldName = tableName + ";" + pkFieldName;
			if (!tableNameAndpkFieldNameMapPkFieldValueArray
					.containsKey(tableNameAndpkFieldName)) {
				List<String> pkFieldValueList = new LinkedList<String>();
				pkFieldValueList.add(pkFieldValue);
				tableNameAndpkFieldNameMapPkFieldValueArray.put(
						tableNameAndpkFieldName, pkFieldValueList);
			} else {
				List<String> pkFieldValueList = tableNameAndpkFieldNameMapPkFieldValueArray
						.get(tableNameAndpkFieldName);
				pkFieldValueList.add(pkFieldValue);
				tableNameAndpkFieldNameMapPkFieldValueArray.put(
						tableNameAndpkFieldName, pkFieldValueList);
			}
		}
		return tableNameAndpkFieldNameMapPkFieldValueArray;
	}

	/**
	 * 将Map格式的业务数据中value值有List类型转换为String类型
	 * 
	 * @author 严建
	 * @param tableNameAndpkFieldNameMapPkFieldValueArray
	 * @return 数据类型Map; 数据格式--Key(String):【表名;主键字段名】;Value(List<String>):【'主键值1','主键值2'...SQL参数格式】
	 * @createTime Feb 1, 2012 5:03:52 PM
	 */
	public Map getTableNameAndpkFieldNameMapPkFieldValueArrayToString(
			Map<String, List<String>> tableNameAndpkFieldNameMapPkFieldValueArray) {
		if (tableNameAndpkFieldNameMapPkFieldValueArray == null
				|| tableNameAndpkFieldNameMapPkFieldValueArray.isEmpty()) {
			return null;
		}
		Map<String, String> tableNameAndpkFieldNameMapPkFieldValueString = new HashMap<String, String>();
		List<String> tableNameAndpkFieldNameList = new ArrayList<String>(
				tableNameAndpkFieldNameMapPkFieldValueArray.keySet());
		for (int j = 0; j < tableNameAndpkFieldNameList.size(); j++) {
			String tableNameAndpkFieldName = tableNameAndpkFieldNameList.get(j);
			List<String> pkFieldValueList = tableNameAndpkFieldNameMapPkFieldValueArray
					.get(tableNameAndpkFieldName);
			StringBuilder params = new StringBuilder();
			for (int k = 0; k < pkFieldValueList.size(); k++) {
				params.append("'" + pkFieldValueList.get(k) + "',");
			}
			tableNameAndpkFieldNameMapPkFieldValueString.put(
					tableNameAndpkFieldName, params.deleteCharAt(
							params.length() - 1).toString());
		}
		return tableNameAndpkFieldNameMapPkFieldValueString;
	}

}
