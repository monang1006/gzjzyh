package com.strongit.oa.common.workflow.applicationInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.jbpm.JbpmContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.common.workflow.parameter.BackSpaceParameter;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseNodesettingPlugin;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 
 * 自动强制取回Manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 18, 2012 8:53:29 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.applicationInterface.AutoFetchTaskManager
 */
@Service
@Transactional
@OALogger
public class AutoFetchTaskManager {
	@Autowired
	private IWorkflowService workflowService;// 注入工作流服务类

	@Autowired
	private SendDocManager sendDocManager;// 注入工作流服务类
	@Autowired
	private IUserService userService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MyLogManager logService;
	/**
	 * 启动强制取回
	 * 
	 * @description
	 * @author 严建
	 * @param pid
	 * @createTime May 15, 2012 11:31:59 AM
	 */
	public void mustFetchTaskStart(String pid) {
		TaskBean taskbean = getAutoBackNodeIdAndActorId(pid);
		if (taskbean != null) {// 存在包含自动强制取回的节点的任务
			endSubProcess(pid);// 结束其所有的子流程
			ProcessInstance processinstance = workflowService
					.getProcessInstanceById(pid);// 获取流程实例
			BackSpaceParameter parameter = new BackSpaceParameter();
			parameter.setInstaceId(pid);
			parameter.setFormId(processinstance.getMainFormId());// 设置表单id
			parameter.setHandleProcess(true);// 产生办理记录
			JSONObject json = new JSONObject();
			json.put("suggestion",  "系统自动强制取回"+ "     ");
			json.put("CAInfo", "");
			parameter.setSuggestion(json.toString());
			parameter.setReturnNodeId(taskbean.getNodeId());// 设置回到节点
			String[] taskActors = new String[] { taskbean.getActorId() + "|"
					+ taskbean.getNodeId() };
			parameter.setTaskActors(taskActors);// 设置回到指定节点的处理人
			parameter.setFormData(null);// /不处理formData
			parameter.setCurUserId(IUserService.SYSTEM_ACCOUNT);// 设置系统用户为当前处理人
			mustFetchTask(parameter);// 根据流程实例id将流程强制取回到指定节点
		}
	}

	/**
	 * 根据流程实例id获取该流程自动强制取回的节点及任务信息
	 * 
	 * @description
	 * @author 严建
	 * @param pid
	 * @return
	 * @createTime May 15, 2012 11:40:14 AM
	 */
	public TaskBean getAutoBackNodeIdAndActorId(String pid) {
		Map<String, TaskBean> map = getProcessedTaskInfoByPid(pid);// 获取当前流程已经处理过的任务信息
		if (map != null && !map.isEmpty()) {// 当前流程存在已经办结的任务时，再获取这些任务对应的节点是否存在自动强制取回的节点
			return getAutoBackNodeIdAndActorId(map);
		}
		return null;
	}

	/**
	 * 获取当前流程已经处理过的任务信息
	 * 
	 * @description
	 * @author 严建
	 * @param pid
	 * @return Map<String,TaskBean> String nodeId 任务节点id TaskBean taskbean
	 *         {任务id，任务节点id,任务结束时间}
	 * @createTime May 15, 2012 9:18:39 AM
	 */
	@SuppressWarnings("unchecked")
	private java.util.Map<String, TaskBean> getProcessedTaskInfoByPid(String pid) {
		if (pid == null || "".equals(pid)) {
			throw new NullPointerException("pid is not null or empty String!");
		}
		java.util.List<java.lang.String> toSelectItems = new java.util.ArrayList<String>();
		toSelectItems.add("taskId");// 任务id
		toSelectItems.add("taskNodeId");// 任务节点id
		toSelectItems.add("taskEndDate");// 任务结束时间
		java.util.Map<java.lang.String, java.lang.Object> paramsMap = new java.util.HashMap<String, Object>();
		paramsMap.put("processInstanceId", pid);
		java.util.List<Object[]> list = workflowService
				.getTaskInfosByConditionForList(toSelectItems, paramsMap, null,
						null, null, null, null);
		Map<String, TaskBean> nodeidMapTaskBean = new HashMap<String, TaskBean>();
		if (list != null && !list.isEmpty()) {
			for (Object[] objs : list) {
				if (objs[2] != null) {
					String nodeId = StringUtil.castString(objs[1]);
					if (nodeidMapTaskBean.containsKey(nodeId)) {// 存在重复的nodeId,取任务结束时间离现在最近的任务信息
						Date now = (Date) objs[2];
						TaskBean tempBean = nodeidMapTaskBean.get(nodeId);
						if (now.after(tempBean.getTaskEndDate())) {
							tempBean.setTaskId(StringUtil.castString(objs[0]));
							tempBean.setTaskEndDate((Date) objs[2]);
						} else {
							continue;
						}
					} else {
						TaskBean tempBean = new TaskBean();
						tempBean.setTaskId(StringUtil.castString(objs[0]));
						tempBean.setNodeId(StringUtil.castString(objs[1]));
						tempBean.setTaskEndDate((Date) objs[2]);
						nodeidMapTaskBean.put(tempBean.getNodeId(), tempBean);
					}
				}
			}
		}
		return nodeidMapTaskBean;
	}

	/**
	 * 获取自动退回的节点id和要退回节点产生任务的处理人id
	 * 
	 * @description
	 * @author 严建
	 * @param nodeidMapTaskBean
	 * @return TaskBean taskbean{任务id，任务节点id，节点名称，任务结束时间，任务处理人}
	 * @createTime May 15, 2012 9:44:53 AM
	 */
	private TaskBean getAutoBackNodeIdAndActorId(
			Map<String, TaskBean> nodeidMapTaskBean) {
		List<TaskBean> result = null;
		TaskBean resultTaskBean = null;
		if (nodeidMapTaskBean != null && !nodeidMapTaskBean.isEmpty()) {
			List<String> nodeIdList = new LinkedList<String>();
			List<TaskBean> taskBeanList = new ArrayList<TaskBean>(
					nodeidMapTaskBean.values());
			for (TaskBean taskbean : taskBeanList) {
				nodeIdList.add(taskbean.getNodeId());
			}
			Map<String, TwfBaseNodesetting> map = workflowService
					.getNodesettingMapByNodeIdList(nodeIdList);
			List<TwfBaseNodesetting> twfbasenodesettingList = new ArrayList<TwfBaseNodesetting>(
					map.values());

			for (TwfBaseNodesetting twfbasenodesetting : twfbasenodesettingList) {
				Map plugins = twfbasenodesetting.getPlugins();// 得到节点上的插件信息集合
				TwfBaseNodesettingPlugin plugin = (TwfBaseNodesettingPlugin) plugins
						.get(NodesettingPluginConst.PLUGINS_MUSTFETCHTASKTIMEOUT);// 获取节点超期自动退回标识
				if (plugin != null && plugin.getValue() != null) {
					String pluginValue = plugin.getValue();
					if ("1".equals(pluginValue)) {
						if (result == null) {
							result = new LinkedList<TaskBean>();
						}
						String nodeid = StringUtil
								.castString(twfbasenodesetting.getNsNodeId());
						TaskBean taskbean = nodeidMapTaskBean.get(nodeid);
						String actorId = workflowService.getTaskInstanceById(
								taskbean.getTaskId()).getActorId();
						taskbean.setActorId(actorId);
						taskbean
								.setNodeName(twfbasenodesetting.getNsNodeName());
						result.add(taskbean);
					}
				}
			}
		}
		if (result != null && !result.isEmpty()) {
			if (result.size() > 1) {
				StringBuilder exceptionMessage = new StringBuilder();
				for (TaskBean taskbean : result) {
					exceptionMessage.append(",").append(taskbean.getNodeName());
				}
				exceptionMessage.deleteCharAt(0);
				exceptionMessage.append("设置了自动强制退回。").append(
						"流程设计中最多只能设置一个节点自动强制退回。");
				logger.error(exceptionMessage.toString());
				
				ToaLog log = new ToaLog();
				try {
					InetAddress inet = InetAddress.getLocalHost();
					log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				log.setOpeUser(userService.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT)); // 操作姓名
				log.setLogState("1"); // 日志状态
				log.setOpeTime(new Date()); // 操作时间
				log.setLogInfo(exceptionMessage.toString());// 日志信息
				logService.saveObj(log);
//				throw new ServiceException(exceptionMessage.toString());
			} else {
				resultTaskBean = result.get(0);
			}
		}
		return resultTaskBean;
	}

	/**
	 * 指定流程强制取回，结束其所有的子流程
	 * 
	 * @description
	 * @author 严建
	 * @param pid
	 * @createTime May 15, 2012 10:46:57 AM
	 */
	private void endSubProcess(String pid) {
		Object[] monitorData = workflowService
				.getProcessInstanceMonitorData(new Long(pid));
		try {
			List subinfo = (List) monitorData[5];// 获取当前流程产生的子流程Id
			if (subinfo != null) {
				JbpmContext jbpmcontext = null;

				for (Object obj : subinfo) {// 结束子流程
					if (jbpmcontext == null) {
						jbpmcontext = workflowService.getJbpmContext();
					}
					Object[] objs = (Object[]) obj;
					String subPid = StringUtil.castString(objs[0]);
					workflowService.loopEndProcess(jbpmcontext, subPid,
							jbpmcontext.getProcessInstance(new Long(pid))
									.getName()
									+ "【"
									+ jbpmcontext.getProcessInstance(
											new Long(pid)).getBusinessName()
									+ "】设置了强制自动取回，系统强制结束"
									+ jbpmcontext.getProcessInstance(
											new Long(pid)).getName()
									+ "【"
									+ jbpmcontext.getProcessInstance(
											new Long(subPid)).getBusinessName()
									+ "】");
				}
				if (jbpmcontext != null) {
					jbpmcontext.close();
					jbpmcontext = null;
				}
			}
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("退回流程时出现异常,异常信息：" + ex.getMessage());
		}
	}

	/**
	 * 根据流程实例id将流程强制取回到指定节点
	 * 
	 * @description
	 * @author 严建
	 * @param parameter
	 * @createTime May 15, 2012 10:58:03 AM
	 */
	private void mustFetchTask(BackSpaceParameter parameter) {
		try {
			if (parameter.getInstaceId() == null
					|| "".equals(parameter.getInstaceId())) {
				throw new NullPointerException(
						"parameter.instanceId is not null or is not empty String");
			}
			List<Object[]> todoTaskIds = sendDocManager
					.getTodoTaskIdsByPid(parameter.getInstaceId());
			if (todoTaskIds != null && !todoTaskIds.isEmpty()) {
				for (Object[] objs : todoTaskIds) {
					String ataskId = StringUtil.castString(objs[0]);
					String ataskNodeId = StringUtil.castString(objs[1]);
					if (!ataskNodeId.equals(parameter.getReturnNodeId())) {// 当前任务节点id和退回节点id不相等时，进行强制取回操作
						parameter.setTaskId(ataskId);
						OALogInfo oaloginfo = new OALogInfo("系统将任务强制取回，taskId="
								+ parameter.getTaskId());
						InetAddress inet = InetAddress.getLocalHost();
						oaloginfo.setOpeIp(inet.getHostAddress());
						oaloginfo.setOpeUser(userService.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT));
						VoFormDataBean bean = sendDocManager.backSpace(
								parameter, oaloginfo);
						if (bean != null) {
							bean.deleteFile();
						}
					}
				}
			}
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SystemException("退回流程时出现异常,异常信息：" + ex.getMessage());
		}
	}
}
