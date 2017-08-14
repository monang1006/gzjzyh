package com.strongit.oa.common.workflow;

import java.util.List;

import com.strongit.oa.common.workflow.service.transitionservice.pojo.TransitionsInfoBean;
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
public interface ITransitionService {
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
			throws SystemException, ServiceException;

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
	public List getStartWorkflowTransitions(String workflowName)
			throws SystemException, ServiceException;

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
			ServiceException;

	/**
	 * 按照节点设置信息中的选择人员的设置方式处理迁移线信息
	 * 
	 * @author 严建
	 * @param list
	 *            迁移线信息
	 * @createTime Mar 7, 2012 11:41:43 PM
	 */
	public TransitionsInfoBean doNextTransitionsBySelectActorSetStyle(List list) throws ServiceException,DAOException, SystemException;
	
}
