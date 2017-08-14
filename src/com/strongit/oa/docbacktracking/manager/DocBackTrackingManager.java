package com.strongit.oa.docbacktracking.manager;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.docbacktracking.service.IDocBackTrackingService;
import com.strongit.oa.docbacktracking.vo.WorkflowInfoVo;
import com.strongit.oa.docbacktracking.vo.WorklowNodeVo;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.workflowDao.WorkflowDao;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class DocBackTrackingManager extends BaseManager implements IDocBackTrackingService {
	@Autowired
	IWorkflowService workflowService;

	@Autowired
	IUserService userService;

	@Autowired
	MyLogManager logService;
	@Autowired
	IProcessDefinitionService processDefinitionService;
	@Autowired
	IProcessInstanceService processInstanceService;

	WorkflowDao workflowDao;
	/**
	 * 注入SessionFactory
	 * 
	 * @param session
	 *            会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		workflowDao = new WorkflowDao(session,Object.class);
	}

	/**
	 * 获取可以补录的公文类型
	 * 
	 * @author yanjian
	 * @param workflowType
	 *            流程类型
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 9, 2012 1:00:14 PM
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowInfoVo> getWorkflowInfoVoList(String workflowType)
			throws ServiceException, DAOException, SystemException {
		try {
			List<WorkflowInfoVo> result = null;
			List<Object[]> startWorkflow = workflowService
					.getStartWorkflow(workflowType);
			if (startWorkflow != null && !startWorkflow.isEmpty()) {
				result = new ArrayList<WorkflowInfoVo>(startWorkflow.size());
				/** objsj结构组成：流程名称,流程类型id,流程类型名称,流程启动保单id */
				for (Object[] objs : startWorkflow) {
					WorkflowInfoVo vo = new WorkflowInfoVo();
					vo.setPfname(StringUtil.castString(objs[0]));
					vo.setTypeId(new Integer(StringUtil.castString(objs[1])));
					vo.setTypeName(StringUtil.castString(objs[2]));
					vo.setMainFormId(new Integer(StringUtil
									.castString(objs[3])));
					result.add(vo);
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
	 * 获取最新的流程定义
	 * 
	 * @author yanjian
	 * @param workflowName
	 *            流程名称
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 9, 2012 1:54:28 PM
	 */
	public ProcessDefinition getLatestProcessDefinition(String workflowName)
			throws ServiceException, DAOException, SystemException {
		try {
			JbpmContext jbpmContext = workflowService.getJbpmContext();
			try {
				if (workflowName != null && workflowName.length() > 0) {
					ProcessDefinition processDefinition = jbpmContext
							.getGraphSession().findLatestProcessDefinition(
									workflowName);
					if (processDefinition != null) {
						return processDefinition;
					} else {
						throw new SystemException("该名称的流程定义不存在!");
					}
				} else {
					throw new SystemException("参数workflowName不能为空！");
				}
			} finally {
				jbpmContext.close();
			}
		} catch (ServiceException ex) {
			// TODO: handle ServiceException
			throw ex;
		} catch (DAOException ex) {
			// TODO: handle DAOException
			throw ex;
		} catch (SystemException ex) {
			// TODO: handle SystemException
			throw ex;
		} catch (Exception ex) {
			// TODO: handle Exception
			throw new SystemException(ex);
		}
	}
	
	
	
	/**
	 * 是否被使用
	 * 
	 * @author yanjian
	 * @param businessId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 16, 2012 11:43:07 AM
	 */
	public boolean isEnd(String businessId) throws ServiceException,
			DAOException, SystemException {
		try {
			java.util.List<java.lang.String> toSelectItems = new ArrayList<String>(1);
			toSelectItems.add("processEndDate");
			String customQuery = "@businessId = '"+businessId+"'";
			List list = workflowService.getProcessInstanceByConditionForList(toSelectItems, null, null, null, null, customQuery, null);
			if(list != null && !list.isEmpty()){
				if(list.get(0) != null){
					return true;
				}
			}
			return false;
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
	 * 获取流程实例信息
	 * 
	 * @author yanjian
	 * @param businessId
	 * @param startUserId
	 * @param workflowName
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 11, 2012 7:12:37 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getPIdByBusid(String businessId,String startUserId,String workflowName) throws ServiceException,
			DAOException, SystemException {
		Map<String,String> map = null;
		try {
			java.util.List<java.lang.String> toSelectItems = new ArrayList<String>(
					2);
			toSelectItems.add("processInstanceId");
			toSelectItems.add("businessName");
			String customQuery = "@businessId = '" + businessId + "'";
			Map paramsMap = new HashMap(2);
			paramsMap.put("startUserId", startUserId);
			paramsMap.put("processName", workflowName);
			List list = workflowService.getProcessInstanceByConditionForList(
					toSelectItems, paramsMap, null, null, null, customQuery, null);
			if (list != null && !list.isEmpty()) {
				map = new LinkedHashMap<String, String>(3);
				Object[] objs = (Object[])list.get(0);
				map.put("processInstanceId", StringUtil.castString(objs[0]));
				map.put("businessName",StringUtil.castString(objs[1]));
				map.put("businessId", businessId);
			}
			return map;
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

	
	/*
	 * 获取任务实例表中的任务节点名称
	 * */
	@SuppressWarnings("unchecked")
	public List<Object[]> getProcessedNodeNameForList(String businessId,
			String workflowName, String startUserId) throws ServiceException,
			DAOException, SystemException {
		try {
			java.util.List<java.lang.String> toSelectItems = new ArrayList<String>(
					1);
			toSelectItems.add("taskNodeName");
			toSelectItems.add("taskId");
			toSelectItems.add("processInstanceId");
			toSelectItems.add("processEndDate");
			toSelectItems.add("taskEndDate");
			Map paramsMap = new HashMap(2);
			paramsMap.put("startUserId", startUserId);
			paramsMap.put("processName", workflowName);
			Map orderMap = new HashMap(1);
			orderMap.put("taskStartDate", "1");
			String customQuery = "@businessId = '" + businessId + "' ";
			List<Object[]> list = workflowService.getTaskInfosByConditionForList(
					toSelectItems, paramsMap, orderMap, null, null,
					customQuery, null);
			/*
			 * modify yanjian 2012-1126 16:20
			 * 
			 * 解决：并行会签同时显示多个环节信息，但是办理界面始终显示任务开始时间最后的任务办理界面，
			 * 多次提交任务系统提示“任务已结束信息”
			 * */
			int listSize = (list == null?0:list.size());
			List checkList = new ArrayList(0);
			for(int i=0;i<listSize;i++){
				Object[] obs = (Object[])list.get(i);
				if(obs[4] == null){
					checkList.add(obs);
				}
			}
			if(checkList.size()>1){
				checkList.remove(checkList.size()-1);
				list.removeAll(checkList);
			}
			return list;
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
	 * 获取节点列表
	 * 
	 * @author yanjian
	 * @param workflowName
	 * @param bussinessId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 15, 2012 2:13:09 PM
	 */
	public List<WorklowNodeVo> getWorklowNodeVoList(String workflowName,String businessId) throws ServiceException,DAOException, SystemException{
		try {
			List<WorklowNodeVo>  list = null;
			String startUserId = userService.getCurrentUser().getUserId();
			List<Object[]> nodelist = getProcessedNodeNameForList(businessId, workflowName, startUserId);
			if(nodelist == null || nodelist.isEmpty()){
				list = new ArrayList<WorklowNodeVo>(1);
				ProcessDefinition processDefinition = getLatestProcessDefinition(workflowName);
				Node startState= processDefinition.getStartState();
				Transition transition = (Transition)startState.getLeavingTransitions().get(0);
				Node secondNode = transition.getTo();
				WorklowNodeVo secondVo = new WorklowNodeVo();
				secondVo.setNodeName(secondNode.getName());
				secondVo.setTaskId("-1");
				secondVo.setProcessId("-1");
				secondVo.setDisable(false);
				list.add(secondVo);
			}else{
				list = new ArrayList<WorklowNodeVo>(nodelist.size());
				int size = nodelist.size();
				for (int i=0;i<size;i++) {
					Object[] objs = nodelist.get(i);
					WorklowNodeVo vo = new WorklowNodeVo();
					vo.setNodeName(objs[0].toString());
					vo.setTaskId(objs[1].toString());
					vo.setProcessId(objs[2].toString());
					if(i == 0){
						if(objs[3] == null){//流程未结束
							vo.setDisable(false);
						}
					}
					list.add(vo);
				}
			}
			return list;
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
	 * 获取补录公文列表
	 * 
	 * @author yanjian
	 * @param page
	 * @param workflowName
	 * @param formId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 9, 2012 6:53:18 PM
	 */
	@SuppressWarnings("unchecked")
	public Object[] getDocList(Page page, String workflowName, String formId)
			throws ServiceException, DAOException, SystemException {
			String tableName;
			List<EFormComponent> queryColumnList;
			List<String[]> showColumnList;
			List list;
			try {
				
				showColumnList = new LinkedList<String[]>();
				queryColumnList = new LinkedList<EFormComponent>();
				List<String> columnNames = new LinkedList<String>();
				Map<String, EFormComponent> fieldMap = eform.getFieldInfo(formId);
				EFormComponent mainTable = fieldMap
						.get(IEFormService.MAINTABLENAME);
				if (mainTable == null) {
					showColumnList.add(new String[] { "pkFieldName", "选择", "0",
							"WORKFLOWTITLE" });// 显示字段列表
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
						.append(" t WHERE 1=1 ").append(" AND t.")
						.append(WORKFLOW_AUTHOR).append("='")
						.append(userService.getCurrentUser().getUserId())
						.append("' AND t.").append(WORKFLOW_NAME).append("='")
						.append(workflowName).append("'").append(" AND t.")
						.append(WORKFLOW_STATE).append("='3' ")
						.append(" and not exists (")
						.append("select pi.id_  from jbpm_processinstance pi ")
						.append(" where 1=1 ")
						.append(" and pi.end_ is not null  ")
						.append(" and pi.business_id = '").append(tableName).append(";").append(pkFieldName).append(";' || t.").append(pkFieldName)
						.append(" )");
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
//					list = jdbcTemplate.queryForList(SqlQuery.toString(),
//							paramValueList.toArray(), sqlTypeArray);
				page = workflowDao.excuteSqlForPage(page, SqlQuery.toString(), paramValueList);
				List result = page.getResult();
				list = null;
				if (result != null && !result.isEmpty()) {
					list = new ArrayList(result.size());
					for(Object obj:result){
						Map map = new LinkedHashMap();
						if(obj instanceof Object[]){
							int size = columnNames.size();
							Object[] objs = (Object[])obj;
							for(int i = 0;i<size;i++){
								map.put(columnNames.get(i), objs[i]);
							}
						}else{
							map.put(columnNames.get(0), obj);
						}
						list.add(map);
					}
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
					list = new ArrayList(list);
					showColumnList = new ArrayList<String[]>(showColumnList);
					page.setResult(list);
				}
				return new Object[] { showColumnList, list, queryColumnList,
						tableName,busidListMap };
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
	 * 获取下一环节节点信息
	 * 
	 * @author yanjian
	 * @param nodeName
	 * @param transitionName
	 * @param workflowName
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 10, 2012 1:07:26 PM
	 */
	protected Node getNextNode(String nodeName, String transitionName,
			String workflowName) throws ServiceException, DAOException,
			SystemException {
		try {
			ProcessDefinition processDefinition = getLatestProcessDefinition(workflowName);
			Node currentNode = processDefinition.getNode(nodeName);
			Transition transition = currentNode
					.getLeavingTransition(transitionName);
			Node nextNode = transition.getTo();
			return nextNode;
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
	 * 获取符合条件的迁移线
	 * 
	 * @author yanjian
	 * @param nodeName
	 * @param workflowName
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 10, 2012 3:59:41 PM
	 */
	@SuppressWarnings("unchecked")
	protected List<Transition> getNextStepTransitions(String nodeName,
			String workflowName) throws ServiceException, DAOException,
			SystemException {
		try {
			ProcessDefinition processDefinition = getLatestProcessDefinition(workflowName);
			Node currentNode = processDefinition.getNode(nodeName);
			return currentNode.getLeavingTransitionsList();
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
	 * 删除公文信息
	 * 
	 * @author yanjian
	 * @param bussinessId		业务id
	 * @param userId			用户id
	 * @param workflowName		流程名称
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 26, 2012 4:25:55 PM
	 */
	public void deleteDoc(String bussinessId,String userId,String workflowName)throws ServiceException,DAOException, SystemException{
		try {
			if(bussinessId == null || "".equals(bussinessId)){
				throw new IllegalArgumentException(" bussinessId is not null or Empty String");
			}
			if(userId == null || "".equals(userId)){
				throw new IllegalArgumentException(" userId is not null or Empty String");
			}
			if(workflowName == null || "".equals(workflowName)){
				throw new IllegalArgumentException(" workflowName is not null or Empty String");
			}
			Map<String,String> map = getPIdByBusid(bussinessId, userId, workflowName);
			if(map != null){
				//删除流程数据
				String instanceId = map.get("processInstanceId");
				processInstanceService.delProcessInstances(instanceId);
			}
			//删除业务数据
			String[] busid = bussinessId.split(";");
			String tableName = busid[0];
			String pkField = busid[1];
			String pkValue = busid[2];
			StringBuilder SqlDelete = new StringBuilder("DELETE FROM ");
			SqlDelete.append(tableName).append(" WHERE ").append(pkField);
			SqlDelete.append(" ='").append(pkValue).append("'");
			logger.info("Delete SQL:" + SqlDelete.toString());
			jdbcTemplate.execute(SqlDelete.toString());
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
	 * 处理日志保存
	 * 
	 * @author yanjian
	 * @param message
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 10, 2012 11:17:02 AM
	 */
	protected void saveLogInfo(String message) throws ServiceException,
			DAOException, SystemException {
		try {
			ToaLog log = new ToaLog();
			InetAddress inet = InetAddress.getLocalHost();
			log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo(message);// 日志信息
			logService.saveObj(log);
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
}
