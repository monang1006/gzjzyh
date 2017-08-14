package com.strongit.oa.common.workflow.service.nodeservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.common.workflow.service.nodeservice.pojo.BackBean;
import com.strongit.oa.common.workflow.service.nodeservice.pojo.BackBeanList;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 31, 2012 2:02:56 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.workflow.service.nodeservice.NodeService
 */
@Service
@OALogger
public class NodeService implements
		com.strongit.oa.common.workflow.INodeService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IWorkflowService workflowService;// 工作流服务类

	@Autowired
	IWorkflowConstService workflowConstService;// 工作流常量服务类

	@Autowired
	INodesettingPluginService nodesettingPluginService;// 节点插件服务类

	@Autowired
	IProcessDefinitionService processDefinitionService;// 流程定义调用接口类

	/**
	 * 根据流程名称获取流程第一个节点的id
	 * 
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime Mar 12, 2012 11:11:41 AM
	 */
	@SuppressWarnings("unchecked")
	public Long getFirstNodeId(String workflowName) throws ServiceException,
			DAOException, SystemException {
		try {
			JbpmContext jbpmContext = workflowService.getJbpmContext();
			try {
				if (workflowName != null && workflowName.length() > 0) {
					ProcessDefinition processDefinition = jbpmContext
							.getGraphSession().findLatestProcessDefinition(
									workflowName);
					if (processDefinition != null) {
						List<Transition> transList = processDefinition
								.getStartState().getLeavingTransitions();
						if (transList != null && transList.size() != 0) {
							Transition transition = transList.get(0);
							Node secondNode = transition.getTo();
							return secondNode.getId();
						} else {
							throw new SystemException("该流程开始节点没有任何转移存在!");
						}
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
	 * 根据流程名称得到流程第一个节点的设置信息
	 * 
	 * @description
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime May 31, 2012 1:36:07 PM
	 */
	@SuppressWarnings("unchecked")
	public TwfBaseNodesetting findFirstNodeSettingByWorkflowName(
			String workflowName) throws ServiceException, DAOException,
			SystemException {
		try {
			TwfBaseNodesetting twfbasenodesetting = null;
			if (workflowName != null && workflowName.length() > 0) {
				Long secondNodeId = getFirstNodeId(workflowName);
				String hql = "from TwfBaseNodesetting t where t.nsNodeId = ?";
				List<TwfBaseNodesetting> list = workflowService.getDataByHql(
						hql, new Object[] { Long.valueOf(secondNodeId) });
				if (list != null && !list.isEmpty()) {
					twfbasenodesetting = list.get(0);
				}
			} else {
				throw new SystemException("参数workflowName不能为空！");
			}
			return twfbasenodesetting;
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
	 * 根据任务实例id或流程名称得到节点设置信息,若taskId参数为空，则查询第一个节点上的设置信息。若不为空,则查询当前所在节点设置信息.
	 * 
	 * @param taskId
	 *            任务实例id
	 * @param workflowName
	 *            流程名称
	 * @return 节点对象
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public TwfBaseNodesetting findFirstNodeSetting(String taskId,
			String workflowName) throws ServiceException, DAOException,
			SystemException {
		try {
			TwfBaseNodesetting twfbasenodesetting = null;
			if (taskId != null && taskId.length() > 0) {
				twfbasenodesetting = processDefinitionService
						.getNodesettingByNodeId(processDefinitionService
								.getNodeIdByTaskInstanceId(taskId));
			} else {
				twfbasenodesetting = findFirstNodeSettingByWorkflowName(workflowName);
			}
			if (twfbasenodesetting != null) {
				return twfbasenodesetting;
			} else {
				throw new SystemException("未找到节点设置信息！");
			}
		} catch (ServiceException ex) {
			throw ex;
		} catch (DAOException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * 得到指定的任务之前的节点列表
	 * 
	 * @param taskId
	 *            任务实例id
	 * @return List<TaskBean>
	 * @throws SystemException
	 *             参数错误或数据不存在时会引发此异常
	 * @see {@link #getBackBeanForList(String)}
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<TaskBean> getPreTaskNodeList(String taskId)
			throws ServiceException, DAOException, SystemException {
		try {
			if (taskId == null || taskId.length() == 0) {
				throw new SystemException("参数taskId不可为空！");
			}
			TaskInstance taskInstance = workflowService
					.getTaskInstanceById(taskId);
			long instanceId = taskInstance.getProcessInstance().getId();
			List<TaskBean> tasks = new ArrayList<TaskBean>();
			List<String> toSelectItems = new ArrayList<String>();
			toSelectItems.add("taskNodeId");
			toSelectItems.add("taskNodeName");
			toSelectItems.add("processInstanceId");
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			// paramsMap.put("taskId", taskId);
			paramsMap.put("processInstanceId", instanceId);
			paramsMap.put("taskType", "3");// 取已办结任务
			Map<String, String> orderMap = new HashMap<String, String>();
			orderMap.put("taskEndDate", "1");
			// 得到已办结的任务列表
			List<Object[]> taskList = workflowService
					.getTaskInfosByConditionForList(toSelectItems, paramsMap,
							orderMap, null, null, null, null);
			// 得到当前任务所在节点id
			String currentNodeId = workflowService
					.getNodeIdByTaskInstanceId(taskId);
			if (currentNodeId == null || currentNodeId.length() == 0) {
				throw new SystemException("任务" + taskId + "所在节点不存在或已删除！");
			}
			TwfBaseNodesetting nodeSetting = workflowService
					.getNodesettingByNodeId(currentNodeId);
			if (nodeSetting == null) {
				throw new SystemException("节点" + currentNodeId + "信息不存在或已删除！");
			}
			// 得到当前节点的之前的节点集合,多个节点以逗号隔开
			String preNode = nodeSetting.getPreNode();
			if (preNode != null && preNode.length() > 0) {
				Map<String, Object[]> nodeInfo = new HashMap<String, Object[]>();
				if (taskList != null && !taskList.isEmpty()) {
					for (Object[] taskObj : taskList) {
						nodeInfo.put(taskObj[1].toString(), taskObj);// Map<节点名称,节点id>
					}
				}
				String[] preNodes = preNode.split(",");
				for (String pnode : preNodes) {
					Object[] taskObj = nodeInfo.get(pnode);
					if (taskObj != null) {
						TaskBean taskBean = new TaskBean();
						List<Object[]> list = workflowService
								.getHandleRecordByNode(String
										.valueOf(instanceId), taskObj[0]
										.toString());
						StringBuilder actorName = new StringBuilder();
						// 去掉重复的名字
						Map<String, Object> checkMap = new HashMap<String, Object>();
						if (list != null && !list.isEmpty()) {
							for (Object[] objs : list) {

								String recordId = "";
								if (objs[6] != null) {
									recordId = objs[6].toString();
								}

								// 存在委托人的情况
								List<TwfInfoApproveinfo> wfApproveInfo = workflowService
										.getApproveInfosByPIId(String
												.valueOf(instanceId));
								if (wfApproveInfo != null
										&& !wfApproveInfo.isEmpty()) {
									for (TwfInfoApproveinfo approveInfo : wfApproveInfo) {
										if (recordId
												.equals(String
														.valueOf(approveInfo
																.getAiId()))) {
											if (approveInfo.getAiOpersonname() != null
													&& objs[2] != null) {
												objs[2] = approveInfo
														.getAiOpersonname();
											}

										}
									}
								}

								if (objs[2] != null) {
									if (!checkMap.containsKey(objs[2]
											.toString())) {
										actorName.append(objs[2]).append(",");
										checkMap.put(objs[2].toString(), null);
									}
								}
							}
							if (actorName.length() > 0) {
								actorName.deleteCharAt(actorName.length() - 1);
							}
						}

						taskBean.setNodeId(taskObj[0].toString());
						taskBean.setNodeName(taskObj[1].toString());
						taskBean.setInstanceId(taskObj[2].toString());
						taskBean.setActorName(actorName.toString());
						tasks.add(taskBean);
					}
				}
			}
			return tasks;
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
	 * 驳回时显示父流程可驳回的数据
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @throws SystemException
	 * @createTime Mar 20, 2012 3:57:26 PM
	 */
	@SuppressWarnings("unchecked")
	public List<TaskBean> getSuperTaskNodeList(String taskId)
			throws ServiceException, DAOException, SystemException {
		try {
			List<TaskBean> tasks = new ArrayList<TaskBean>();
			if (taskId == null || taskId.length() == 0) {
				throw new SystemException("参数taskId不可为空！");
			}
			ProcessInstance pi = workflowService.getTaskInstanceById(taskId)
					.getProcessInstance();
			Long nodeId = workflowConstService.getSuperProcessNodeId(pi);
			TwfBaseNodesetting twfbasenodesetting = workflowService
					.getNodesettingByNodeId(nodeId.toString());
			String preNode = twfbasenodesetting.getPreNode();
			if (preNode != null && !"".equals(preNode)) {
				Long superProcessId = workflowConstService
						.getSuperProcessId(pi);// 获取父流程实例id
				String[] sItems = new String[] { "taskNodeId", "taskNodeName", };
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("processInstanceId", superProcessId);
				paramsMap.put("taskType", "3");// 取已办结任务
				Map<String, String> orderMap = new HashMap<String, String>();
				orderMap.put("taskEndDate", "1");
				List<Object[]> taskList = workflowService
						.getTaskInfosByConditionForList(Arrays.asList(sItems),
								paramsMap, orderMap, null, null, null, null);
				Map<String, Object[]> nodeInfo = new HashMap<String, Object[]>();
				for (Object[] objs : taskList) {
					nodeInfo.put(objs[1].toString(), objs);// Map<节点名称,节点信息>
				}
				String[] preNodes = preNode.split(",");
				for (String pnode : preNodes) {
					if (nodeInfo.containsKey(pnode)) {
						Object[] taskBeanobjs = nodeInfo.get(pnode);
						String plugins_notoverrule = nodesettingPluginService
								.getNodesettingPluginValue(
										StringUtil.castString(taskBeanobjs[0]),
										NodesettingPluginConst.PLUGINS_NOTOVERRULE);
						if (!"1".equals(plugins_notoverrule)) {// 控制节点上设置了不可驳回的节点信息不显示在列表中
							TaskBean taskBean = new TaskBean();
							List<Object[]> list = workflowService
									.getHandleRecordByNode(String
											.valueOf(superProcessId),
											taskBeanobjs[0].toString());
							// 去掉重复的名字
							Map<String, Object> checkMap = new HashMap<String, Object>();
							StringBuilder actorName = new StringBuilder();
							if (list != null && !list.isEmpty()) {
								for (Object[] objs : list) {
									String recordId = "";
									if (objs[6] != null) {
										recordId = objs[6].toString();
									}
									// 存在委托人的情况
									List<TwfInfoApproveinfo> wfApproveInfo = workflowService
											.getApproveInfosByPIId(String
													.valueOf(superProcessId));
									if (wfApproveInfo != null
											&& !wfApproveInfo.isEmpty()) {
										for (TwfInfoApproveinfo approveInfo : wfApproveInfo) {
											if (recordId.equals(String
													.valueOf(approveInfo
															.getAiId()))) {
												if (approveInfo
														.getAiOpersonname() != null
														&& objs[2] != null) {
													objs[2] = approveInfo
															.getAiOpersonname();
												}

											}
										}
									}

									if (objs[2] != null) {
										if (!checkMap.containsKey(objs[2]
												.toString())) {
											actorName.append(objs[2]).append(
													",");
											checkMap.put(objs[2].toString(),
													null);
										}
									}
								}
								if (actorName.length() > 0) {
									actorName
											.deleteCharAt(actorName.length() - 1);
								}
							}
							taskBean.setActorName(actorName.toString());
							taskBean.setNodeId(StringUtil
									.castString(taskBeanobjs[0]));
							taskBean.setNodeName(StringUtil
									.castString(taskBeanobjs[1]));
							tasks.add(taskBean);
						}
					}
				}
			}
			return new ArrayList<TaskBean>(tasks);
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
	 * 根据任务id，并按任务名称进行分组,获取退回列表
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 27, 2012 5:12:55 PM
	 */
	public Map<String, List<BackBean>> getBackSpaceForMapGroupByTaskNodeName(
			String taskId) throws ServiceException, DAOException,
			SystemException {
		try {
			return getBackBeanForMapGroupByTaskNodeName(workflowService
					.getProcessInstanceIdByTiId(taskId));
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
	 * 根据任务id，并按任务名称进行分组,获取驳回列表
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 30, 2012 4:12:35 PM
	 */
	public Map<String, List<BackBean>> getOverRuleForMapGroupByTaskNodeName(
			String taskId) throws ServiceException, DAOException,
			SystemException {
		try {
			return getBackBeanForMapGroupByTaskNodeName(workflowConstService
					.getSuperProcessIdByTid(taskId).toString());
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
	 * 根据任务名称进行分组
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 27, 2012 5:12:55 PM
	 */
	protected Map<String, List<BackBean>> getBackBeanForMapGroupByTaskNodeName(
			String instanceId) throws ServiceException, DAOException,
			SystemException {
		try {
			Map<String, List<BackBean>> map = null;
			List<BackBean> backbeanlist = getFilterBackBeanForList(instanceId);
			for (BackBean backbean : backbeanlist) {
				if (map == null) {
					map = new HashMap<String, List<BackBean>>();
				}
				if (map.containsKey(backbean.getTaskNodeName())) {
					map.get(backbean.getTaskNodeName()).add(backbean);
				} else {
					List<BackBean> temp = new LinkedList<BackBean>();
					temp.add(backbean);
					map.put(backbean.getTaskNodeName(), temp);
				}
			}
			return map;
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
	 * 去除同一节点同一个人处理多次的数据
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 27, 2012 5:05:19 PM
	 */
	protected List<BackBean> getFilterBackBeanForList(String instanceId)
			throws ServiceException, DAOException, SystemException {
		try {
			List<BackBean> backbeanlist = getBackBeanForList(instanceId);
			BackBeanList backbeanlistbean = null;
			if (backbeanlist != null && !backbeanlist.isEmpty()) {
				if (backbeanlistbean == null) {
					backbeanlistbean = new BackBeanList();
				}
				for (BackBean backbean : backbeanlist) {
					backbeanlistbean.add(backbean);
				}
			}
			if (backbeanlistbean != null) {
				backbeanlist = backbeanlistbean.getList();
			}
			return backbeanlist;
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
	 * 获取退回信息列表
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 27, 2012 4:39:58 PM
	 */
	protected List<BackBean> getBackBeanForList(String instanceId)
			throws ServiceException, DAOException, SystemException {
		try {
			List<BackBean> backbeanlist = null;
			List<Object[]> taskList = getProcessedTaskInfoByPiId(instanceId
					+ "");
			Map<String, Object[]> processhandlesMap = getProcessHandlesAndNodeSettingByPiId(instanceId
					+ "");
			for (Object[] objs : taskList) {
				String taskid = StringUtil.castString(objs[2]);
				if (processhandlesMap.containsKey(taskid)) {
					if (backbeanlist == null) {
						backbeanlist = new LinkedList<BackBean>();
					}
					Object[] tempobjs = processhandlesMap.get(taskid);
					BackBean backbean = new BackBean();
					backbean.setTaskId(taskid);
					backbean.setTaskNodeId(StringUtil.castString(objs[0]));
					backbean.setTaskNodeName(StringUtil.castString(objs[1]));
					backbean.setAiActorId(StringUtil.castString(tempobjs[5]));
					backbean.setAiActorName(StringUtil.castString(tempobjs[6]));
					backbean
							.setAiOpersonid(StringUtil.castString(tempobjs[13]));
					backbean.setAiOpersonname(StringUtil
							.castString(tempobjs[14]));
					backbeanlist.add(backbean);
				}
			}
			if (backbeanlist != null) {
				backbeanlist = new ArrayList<BackBean>(backbeanlist);
			}
			return backbeanlist;
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
	 * 获取当前任务之前的节点名称
	 * 
	 * @author 严建
	 * @param nodeId
	 * @return
	 * @createTime Mar 27, 2012 4:22:13 PM
	 */
	public String getPreNodeByNodeId(String nodeId) throws ServiceException,
			DAOException, SystemException {
		try {
			TwfBaseNodesetting nodeSetting = workflowService
					.getNodesettingByNodeId(nodeId);
			if (nodeSetting == null) {
				throw new SystemException("节点" + nodeId + "信息不存在或已删除！");
			}
			// 得到当前节点的之前的节点集合,多个节点以逗号隔开
			String preNode = nodeSetting.getPreNode();
			return preNode;
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
	 * 根据流程id 得到已办结的任务列表
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return List -每个任务的信息 Object[]{(0)任务节点Id, (1)任务名称, (2)任务实例Id
	 * @createTime Mar 27, 2012 4:19:10 PM
	 */
	@SuppressWarnings("unchecked")
	protected List<Object[]> getProcessedTaskInfoByPiId(String instanceId)
			throws ServiceException, DAOException, SystemException {
		try {
			if (instanceId == null || "".equals(instanceId)) {
				throw new SystemException("流程id不能为空：" + instanceId);
			}
			List<String> toSelectItems = new ArrayList<String>();
			toSelectItems.add("taskNodeId");
			toSelectItems.add("taskNodeName");
			toSelectItems.add("taskId");
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("processInstanceId", instanceId);
			paramsMap.put("taskType", "3");// 取已办结任务
			Map<String, String> orderMap = new HashMap<String, String>();
			orderMap.put("taskEndDate", "1");
			// 得到已办结的任务列表
			List<Object[]> taskList = workflowService
					.getTaskInfosByConditionForList(toSelectItems, paramsMap,
							orderMap, null, null, null, null);
			return taskList;
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
	 * 获取办理记录
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 27, 2012 4:14:55 PM
	 */
	protected Map<String, Object[]> getProcessHandlesAndNodeSettingByPiId(
			String instanceId) throws ServiceException, DAOException,
			SystemException {
		try {
			Map<String, Object[]> processhandlesMap = null;
			List<Object[]> processhandles = workflowService
					.getProcessHandlesAndNodeSettingByPiId(instanceId + "");
			for (Object[] objs : processhandles) {
				if (processhandlesMap == null) {
					processhandlesMap = new HashMap<String, Object[]>();// 将处理记录转变为map
					// 任务id：对应办理记录信息
				}
				processhandlesMap.put(StringUtil.castString(objs[0]), objs);
			}
			return processhandlesMap;
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
	 * 获取签收节点设置的人员信息
	 * 
	 * @author yanjian
	 * @param nodeId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Dec 1, 2012 10:35:25 PM
	 */
	public String[] getSignNodeActorsByNodeId(String nodeId) throws ServiceException,DAOException, SystemException{
		try {
			if(nodeId == null){
				throw new IllegalArgumentException("nodeId is not null!");
			}
			String[] result = null;
			if(workflowService.isSignNodeTask(nodeId)){
				List list = workflowService.getTaskActorsByTask(nodeId, null,null);
				if(list != null && !list.isEmpty()){
					int listSize = list.size();
					String[] taskActors = new String[listSize];
					for(int i=0;i<listSize;i++){
						taskActors[i] =  ((Object[])list.get(i))[0] + "|" + nodeId;
					}
					result = taskActors;
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
	 * 在提交流程时，获取指定签收节点信息Id
	 * 
	 * @author yanjian
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Dec 1, 2012 10:46:35 PM
	 */
	public long getSignNodeByTaskIdAndTransitionName(String taskId,String transitionName) throws ServiceException,DAOException, SystemException{
		try {
			long nodeId = -1L;
			TaskInstance curTaskInstance = workflowService.getTaskInstanceById(taskId);		//获取当前任务实例
			ProcessDefinition processDefinition = curTaskInstance.getProcessInstance().getProcessDefinition();
			Node  curNode = processDefinition.getNode(curTaskInstance.getNodeName());		//获取当前任务实例所在节点
			Transition transition = curNode.getLeavingTransition(transitionName);
			if(transition != null){
				Node thirdNode = transition.getTo();
				nodeId = thirdNode.getId();
			}
			return nodeId;
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
	 * 在启动流程时，获取指定签收节点信息Id
	 * 
	 * @author yanjian
	 * @param workflowName
	 * @param transitionName
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Dec 1, 2012 10:05:56 PM
	 */
	public long getSignNodeByWorkflowNameAndTransitionName(String workflowName,
			String transitionName) throws ServiceException, DAOException,
			SystemException {
		try {
			long nodeId = -1L;
			if(transitionName==null || workflowName==null){
				throw new IllegalArgumentException("transitionName or workflowName is not null!");
			}
			ProcessDefinition processDefinition = getLatestProcessDefinition(workflowName);
			Node startState= processDefinition.getStartState();		//获取开始节点信息
			Transition transition = (Transition)startState.getLeavingTransitions().get(0);
			Node secondNode = transition.getTo();					//获取公文分发节点信息
			transition = secondNode.getLeavingTransition(transitionName);
			if(transition != null){
				Node thirdNode = transition.getTo();
				nodeId = thirdNode.getId();
			}
			return nodeId;
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
	 * 获取签收节点的后缀名称
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 9, 2012 11:32:35 AM
	 */
	public String getSignNodeNameSuffix() throws ServiceException,
			DAOException, SystemException {
		try {
			StringBuilder namesuffix = new StringBuilder();
			if (LocalizedTextUtil.findDefaultText(
					GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext()
							.getLocale()) != null) {
				namesuffix.append(LocalizedTextUtil.findDefaultText(
						GlobalBaseData.WORKFLOW_SIGNNODE_NAMESUFFIX,
						ActionContext.getContext().getLocale()));
			} else {
				namesuffix.append("签收");
			}
			return namesuffix.toString();
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

}
