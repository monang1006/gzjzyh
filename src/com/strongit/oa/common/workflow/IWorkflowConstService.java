package com.strongit.oa.common.workflow;

import org.jbpm.graph.exe.ProcessInstance;

import com.strongit.oa.common.workflow.oabo.BackInfoBean;

/**
 * Service 工作流接口实现类，适配工作流常量提供的接口
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 20, 2012 3:21:15 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.service.WorkflowConstService
 */
public interface IWorkflowConstService {
	/**
	 * 
	 * 根据流程实例id获取流程在父流程中对应的子流程节点id
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 7, 2012 8:29:11 PM
	 */
	public Long getSuperProcessNodeIdByPid(String instanceId);

	/**
	 * 根据任务实例id获取父流程节点id
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 20, 2012 2:55:01 PM
	 */
	public Long getSuperProcessNodeIdByTid(String taskInstanceId);

	/**
	 * 获取流程在父流程实例中所对应的子流程节点id
	 * 
	 * @author 严建
	 * @param pi
	 * @return
	 * @createTime Mar 20, 2012 3:12:44 PM
	 */
	public Long getSuperProcessNodeId(ProcessInstance pi);

	/**
	 * 获取父流程实例id
	 * 
	 * @author 严建
	 * @param pi
	 * @return
	 * @createTime Mar 20, 2012 3:18:10 PM
	 */
	public Long getSuperProcessId(ProcessInstance pi);

	/**
	 * 根据任务实例id获取父流程实例的id
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 20, 2012 2:24:56 PM
	 */
	public Long getSuperProcessIdByTid(String taskInstanceId);

	/**
	 * 
	 * 根据流程实例id获取父流程实例的id
	 * 
	 * @author 严建
	 * @param pid
	 * @return
	 * @createTime Mar 20, 2012 3:34:48 PM
	 */
	public Long getSuperProcessIdByPid(String pid);

	/**
	 * 退回文提交时，根据任务实例id删除之前存在流程中的退回信息
	 * 
	 * @author 严建
	 * @param taskId
	 * @createTime Mar 22, 2012 12:56:57 PM
	 */
	public void deleteBackInfo(String taskId);

	/**
	 * 根据任务id获取当前退文的上一步退回信息
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 22, 2012 1:09:39 PM
	 */
	public String getBackInfo(String taskId);
	/**
	 * 根据任务id获取当前退文的上一步退回信息
	 * 
	 * @description
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Apr 10, 2012 10:28:12 PM
	 */
	public BackInfoBean getBackInfoBean(String taskId);
	/**
	 * 退文时，将退回信息保存在流程实例中<br/> 驳回时将退回信息保存在父流程实例中，退回和退回上一步保存在当前流程实例中
	 * 
	 * @author 严建
	 * @param taskId		当前任务id
	 * @param backType		退回方式
	 * @param suggestion	意见信息
	 * @param backUserId	退回人员id
	 * @createTime Mar 23, 2012 11:24:15 AM
	 */
	public void setBackInfo(String taskId,String backType,String suggestion,String backUserId);
}
