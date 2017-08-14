package com.strongit.oa.common.workflow;

import java.util.List;
import java.util.Map;

import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.common.workflow.service.nodeservice.pojo.BackBean;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * Service
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 12, 2012 11:26:13 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.nodeservice.INodeService
 */
public interface INodeService {
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
			DAOException, SystemException;

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
			SystemException;

	/**
	 * 得到指定的任务之前的节点列表
	 * 
	 * @param taskId
	 *            任务实例id
	 * @return List<TaskBean>
	 * @throws SystemException
	 *             参数错误或数据不存在时会引发此异常
	 */
	@SuppressWarnings("unchecked")
	public List<TaskBean> getPreTaskNodeList(String taskId)
			throws ServiceException, DAOException, SystemException;

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
			throws ServiceException, DAOException, SystemException;

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
			SystemException;

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
			SystemException;

	/**
	 * 获取当前任务之前的节点名称
	 * 
	 * @author 严建
	 * @param nodeId
	 * @return
	 * @createTime Mar 27, 2012 4:22:13 PM
	 */
	public String getPreNodeByNodeId(String nodeId) throws ServiceException,
			DAOException, SystemException;

	/**
	 * 获取签收节点的后缀名称
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 9, 2012 11:32:35 AM
	 */
	public String getSignNodeNameSuffix() throws ServiceException,
			DAOException, SystemException;

	/**
	 * 根据流程名称得到流程第一个节点的设置信息
	 * 
	 * @description
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime May 31, 2012 1:36:07 PM
	 */
	public TwfBaseNodesetting findFirstNodeSettingByWorkflowName(
			String workflowName) throws ServiceException, DAOException,
			SystemException;
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
			SystemException;
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
	public String[] getSignNodeActorsByNodeId(String nodeId) throws ServiceException,DAOException, SystemException;
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
	public long getSignNodeByTaskIdAndTransitionName(String taskId,String transitionName) throws ServiceException,DAOException, SystemException;
}
