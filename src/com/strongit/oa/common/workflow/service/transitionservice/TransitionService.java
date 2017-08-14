package com.strongit.oa.common.workflow.service.transitionservice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.ITransitionService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.common.workflow.service.transitionservice.pojo.TransitionsInfoBean;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * Service 定义迁移线服务
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 7, 2012 11:01:50 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.transition.TransitionService
 */
@Service
@OALogger
public class TransitionService implements ITransitionService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IWorkflowService workflowService;// 工作流服务类
	@Autowired
	IUserService userService;// 统一用户服务

	@Autowired
	INodesettingPluginService nodesettingPluginService;// 工作流服务类

	/**
	 * @author 严建 获取流程流转的迁移线 (1)获取指定任务实例的下一步可选流向 (2)流程开始时获取下一步骤
	 * 
	 * @param taskId
	 *            任务ID
	 * @param workflowName
	 * @return List List 下一步转移信息 List中数据结构为： Object[]{(0)转移名称, (1)转移Id, (2)并发标识,
	 *         (3)节点设置信息} 其中“节点设置信息”为Set集合 并发标识为：0、正常模式；1、并发模式 节点设置信息数据结构
	 *         需要动态设置：activeSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         子流程中需要动态设置：subactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         父流程中需要动态设置：supactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         不需要动态设置：notActiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是条件节点：decideNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是子流程节点：subProcessNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是结束节点：endNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 * @throws SystemException
	 * @throws ServiceException
	 * @createTime Mar 7, 2012 10:25:34 PM
	 */
	public List getNextTransitions(String taskId, String workflowName)
			throws SystemException, ServiceException {
		try {
			List list = null;
			if (workflowName != null && !"undefined".equals(workflowName)
					&& !"".equals(workflowName)
					&& (taskId == null || "".equals(taskId))) {
				// 选取开始流程时下一步可选步骤
				list = this.getStartWorkflowTransitions(workflowName);
			}
			if (taskId != null && !"undefined".equals(taskId)
					&& !"".equals(taskId)) {
				// 选取指定任务下一步可选步骤
				list = this.getNextTransitions(taskId);
			}
			//filterTransInfoByPNDN(list);//根据子流程节点选择的部门是否是当前用户所属的部门来控制迁移线的显示
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取下一步可选步骤" });
		}
	}

	
	/**
	 * 根据迁移线信息获取该迁移线对应的流程节点id
	 * 
	 * @author 严建
	 * @param transInfo
	 * @return
	 * @createTime Mar 28, 2012 3:56:54 PM
	 */
	 protected String getNodeIdByTransInfoSet(Set<String> transInfo){
		String[] transInfoarr =  new String[transInfo.size()];
		transInfo.toArray(transInfoarr);
		return transInfoarr[0].split("\\|")[1];
	}
	
	
	/**
	 * 流程开始时获取下一步骤
	 * 
	 * @param workflowName
	 * @return List List 下一步转移信息 List中数据结构为： Object[]{(0)转移名称, (1)转移Id, (2)并发标识,
	 *         (3)节点设置信息} 其中“节点设置信息”为Set集合 并发标识为：0、正常模式；1、并发模式 节点设置信息数据结构
	 *         需要动态设置：activeSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         子流程中需要动态设置：subactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         父流程中需要动态设置：supactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         不需要动态设置：notActiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是条件节点：decideNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是子流程节点：subProcessNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是结束节点：endNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 */
	@Transactional(readOnly = true)
	public List getStartWorkflowTransitions(String workflowName)
			throws SystemException, ServiceException {
		try {
			return workflowService.getStartWorkflowTransitions(workflowName);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取下一步可选步骤" });
		}
	}

	/**
	 * 获取指定任务实例的下一步可选流向
	 * 
	 * @param taskId
	 *            任务ID
	 * @return List中数据结构为： Object[]{(0)转移名称, (1)转移Id, (2)并发标识, (3)节点设置信息}
	 *         其中“节点设置信息”为Set集合 并发标识为：0、正常模式；1、并发模式 节点设置信息数据结构
	 *         需要动态设置：activeSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         子流程中需要动态设置：subactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         父流程中需要动态设置：supactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         不需要动态设置：notActiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是条件节点：decideNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是子流程节点：subProcessNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是结束节点：endNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 */
	public List getNextTransitions(String taskId) throws SystemException,
			ServiceException {
		try {
			return workflowService.getNextTransitions(taskId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取下一步可选步骤" });
		}
	}

	/**
	 * 按照节点设置信息中的选择人员的设置方式处理迁移线信息
	 * 
	 * @author 严建
	 * @param list
	 *            迁移线信息
	 * @createTime Mar 7, 2012 11:41:43 PM
	 */
	@SuppressWarnings("unchecked")
	public TransitionsInfoBean doNextTransitionsBySelectActorSetStyle(List list) throws ServiceException,DAOException, SystemException{
		try {
			TransitionsInfoBean transitionsInfoBean = null;
			Set<Long> transitionIds = null;
			
			if (list != null && !list.isEmpty()) {
				
				Set<Long> nsNodeids = new HashSet<Long>(list.size());
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					Set<String> setTrans = (Set<String>) objs[3];
					for (String str : setTrans) {
						String[] strs = str.split("\\|");
						System.out.println("nodeId:"+strs[1]);
						nsNodeids.add(Long.valueOf(strs[1]));
					}
				}
				Set<String> pluginNames = new HashSet<String>(1);
				/*
				 * 根据判断节点是否为待签收节点，如果是的话将动态选择处理人改为非动态指定处理人（流程提交时，在获取签收节点上所有的人员信息）
				 * */
				pluginNames.add("plugins_chkModifySuggestion");
				/*
				 * 根据子流程节点选择的部门是否是当前用户所属的部门来控制迁移线的显示【true：不显示|false：显示】
				 * */
				pluginNames.add(NodesettingPluginConst.PLUGINS_NODEORGID);	
				Map<Long, Map<String, String>> nodeSettingPluginMap = nodesettingPluginService.getNodesettingPluginValueForMapByNodeIdsAndpluginNames(nsNodeids, pluginNames);
				if(nodeSettingPluginMap == null){
					nodeSettingPluginMap = new HashMap<Long, Map<String,String>>();
				}
				
				/*
				 * 当前处理人所在结构id
				 * */
				String curUserOrgId = (userService.getCurrentUser().getOrgId() == null ?"":userService.getCurrentUser().getOrgId());
				for(int i=0;i<list.size();i++){
					Object[] trans = (Object[]) list.get(i);
					Set<String> transInfo = (Set<String>) trans[3];
					Long nodeId =Long.valueOf(getNodeIdByTransInfoSet(transInfo));
					Map<String, String> map = (nodeSettingPluginMap.get(nodeId)==null?new HashMap<String, String>():nodeSettingPluginMap.get(nodeId));
					//根据子流程节点选择的部门是否是当前用户所属的部门来控制迁移线的显示(不显示当前处理人所在部门的迁移线信息)
					if(map.containsKey(NodesettingPluginConst.PLUGINS_NODEORGID) && curUserOrgId.equals(map.get(NodesettingPluginConst.PLUGINS_NODEORGID))){
						list.remove(i);
						i--;
					}
				}
				int listSize = (list==null?0:list.size());
				if(listSize>0){
					transitionsInfoBean = new TransitionsInfoBean();
					transitionIds = new HashSet<Long>(listSize);
					transitionsInfoBean.setTransitionIds(transitionIds);
					transitionsInfoBean.setTransitionsInfo(list);
				}
				for (int i = 0; i < listSize; i++) {
					Object[] objs = (Object[]) list.get(i);
					Set<String> setTrans = (Set<String>) objs[3];
					//添加迁移线id
					String transitionId = objs[1].toString();
					transitionIds.add(Long.valueOf(transitionId));
					
					String transInfo = setTrans.toString();
					boolean flag = false;
					if (transInfo.indexOf("subProcessNode") != -1
							&& setTrans.size() > 1) {// 子流程节点,并且要指定返回父流程时的人员
						for (String str : setTrans) {
							String[] strs = str.split("\\|");
							String actorFlag = strs[0];// 是否需要选择人员activeSet:需要重新选择人员
							if (actorFlag.equals("subProcessNode")) {
								String subNodeId = strs[1];
								String plugin = nodesettingPluginService
										.getNodesettingPluginValue(subNodeId,NodesettingPluginConst.PLUGINS_DORETURN);
								if (plugin != null && "1".equals(plugin)) {
									flag = true;
									break;
								}
							}
						}
					}
					if (flag) {
						Set<String> newSet = new HashSet<String>();
						for (String str : setTrans) {
							String[] strs = str.split("\\|");
							String actorFlag = strs[0];// 是否需要选择人员activeSet:需要重新选择人员
							if ("activeSet".equals(actorFlag)) {
								/*
								 * nodeId =
								 * workflowService.getNodeIdByTaskInstanceId(taskId);
								 * if(nodeId.equals(strs[1]))
								 * {//不相等为子流程选择下一步处理人,如果相等的话是为子流程结束后返回父流程选择人员 str =
								 * "notActiveSet|" + strs[1] + "|" + strs[2] + "|" +
								 * strs[3]; }
								 */
								str = "notActiveSet|" + strs[1] + "|" + strs[2]
										+ "|" + strs[3];
							}
							newSet.add(str);
						}
						objs[3] = newSet;
						list.set(i, objs);
					}
					//处理流程提交到待签收节点，不选择处理人（节点设置动态选择处理人）
					if(!flag && transInfo.indexOf("activeSet") != -1){	//如果设置成了不能动态指定处理人，则不需要再判断是否为签收节点
						Set<String> chkModifySuggestionSet = new HashSet<String>();
						for (String str : setTrans) {
							String[] strs = str.split("\\|");
							Long nodeId = Long.valueOf(strs[1]);
							String actorFlag = strs[0];// 是否需要选择人员activeSet:需要重新选择人员
							if ("activeSet".equals(actorFlag)) {
								Map<String, String> map = (nodeSettingPluginMap.get(nodeId)==null?new HashMap<String, String>():nodeSettingPluginMap.get(nodeId));
								if(map.containsKey("plugins_chkModifySuggestion") && "1".equals(map.get("plugins_chkModifySuggestion"))){
									str = "notActiveSet|" + strs[1] + "|" + strs[2]+ "|" + strs[3];
								}
							}
							chkModifySuggestionSet.add(str);
						}
						objs[3] = chkModifySuggestionSet;
						list.set(i, objs);
					}
				}
			}
			return transitionsInfoBean;
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
