package com.strongit.oa.freedomworkflow.service;

import java.util.List;

import com.strongit.oa.bo.TOaFreedomWorkflow;
import com.strongit.oa.bo.TOaFreedomWorkflowTask;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IFreedomWorkflowTaskService
{
	
	/**
	 * 获取自由流程任务
	 * 
	 * @param ftId 任务id
	 * @return
	 */
	public TOaFreedomWorkflowTask getFreedomWorkflowTask(String ftId)
			throws ServiceException,SystemException;
	
	/**
	 * 获取流程中的所有任务
	 * 
	 * @param fwId 流程id
	 * @return
	 */
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowTasks(String fwId)
			throws ServiceException,SystemException;
	
	/**
	 * 获取任务状态为处理中和未开始的任务
	 * 
	 * @param fwId 流程id
	 * @return
	 */
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowNotDoneTasks(String fwId)
			throws ServiceException,SystemException;
	
	/**
	 * 获取未开始的任务
	 * 
	 * @param fwId 流程id
	 * @return
	 */
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowNotStartTasks(String fwId)
			throws ServiceException,SystemException;
	
	/**
	 * 获取已完成的任务
	 * 
	 * @param fwId 流程id
	 * @return
	 */
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowDoneTasks(String fwId)
			throws ServiceException,SystemException;
	
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowTasks(String handler, String ftStatus)
			throws ServiceException,SystemException;

	/**
	 * 获取下一步任务
	 * 
	 * @param curFtId 当前任务id
	 * @return
	 */
	public TOaFreedomWorkflowTask getNextTask(String curFtId)
			throws ServiceException,SystemException;
	
	/**
	 * 新增时，保存流程任务
	 * 
	 * @param fw 流程信息
	 * @param firstTaskMemo 第一个任务的备注
	 * @param jsonHandles 所有任务对象的json串
	 * @return
	 */
	public void saveFreedomWorkflowTasks(TOaFreedomWorkflow fw,
			String firstTaskMemo, String jsonHandles) throws ServiceException,
			SystemException;
	
	/**
	 * 编辑时，保存流程任务
	 * 
	 * @param fwId 流程id
	 * @param jsonHandles 未开始状态的任务对象的json串
	 * @return
	 */
	public void editFreedomWorkflowTasks(String fwId, String jsonHandles)
			throws ServiceException,SystemException;
	
	/**
	 * 删除任务
	 * 
	 * @param fwId 流程id
	 * @return
	 */
	public void deleteFreedomWorkflowTasks(String fwId)
			throws ServiceException,SystemException;
	
	/**
	 * 删除未开始的任务
	 * 
	 * @param fwId 流程id
	 * @return
	 */
	public void deleteFreedomWorkflowNotStartTasks(String fwId)
			throws ServiceException,SystemException;
	
	/**
	 * 删除已处理的任务
	 * 
	 * @param fwId 流程id
	 * @return
	 */
	public void deleteFreedomWorkflowDoneTasks(String fwId)
			throws ServiceException,SystemException;
	
	/**
	 * 已处理以及处理中的任务数目
	 * 
	 * @param fwId 流程id
	 * @return 
	 */
	public int donePendingTaskNum(String fwId)
			throws ServiceException,SystemException;
	
	/**
	 * 设置第一个任务的Memo信息
	 * 
	 * @param fwId 流程id
	 * @param ftMemo 备忘内容
	 * @return
	 */
	public boolean setFirstTaskMemo(String fwId, String ftMemo)
			throws ServiceException,SystemException;
	
	/**
	 * 获取状态为处理中和未开始的任务
	 * 
	 * @param handler 处理人
	 * @return
	 */
	public List<TOaFreedomWorkflowTask> getMyNotDoneTasks(String handler)
			throws ServiceException,SystemException;
	
	/**
	 * 获取处理中的任务
	 * 
	 * @param handler 处理人
	 * @param ftTitle 任务标题
	 * @return
	 */
	public Page<TOaFreedomWorkflowTask> getMyPendingTasks(
			Page<TOaFreedomWorkflowTask> page, String handler, String ftTitle)
			throws ServiceException, SystemException;
	
	/**
	 * 获取已处理的任务
	 * 
	 * @param handler 处理人
	 * @param ftTitle 任务标题
	 * @return
	 */
	public Page<TOaFreedomWorkflowTask> getMyDoneTasks(
			Page<TOaFreedomWorkflowTask> page, String handler, String ftTitle)
			throws ServiceException, SystemException;
	
	public List<TOaFreedomWorkflowTask> getMyNotDoneTasks(String handler, String fwId)
			throws ServiceException,SystemException;

	/**
	 * 提交处理任务
	 * 
	 * @param handler 处理人
	 * @param ftId 任务id
	 * @param ftMemo 备忘内容
	 * @return
	 */
	public void doneTask(String handler, String ftId, String ftMemo)
			throws ServiceException,SystemException;
	
	/**
	 * 发送消息
	 * 
	 * @param remindTypes 发送方式
	 * @param revUsers 接收者列表
	 * @param msg 消息内容
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void sendMsg(String remindTypes, List<String> revUsers, String msg)
			throws ServiceException,SystemException;
	
}
