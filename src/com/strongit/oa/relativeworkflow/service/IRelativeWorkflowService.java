package com.strongit.oa.relativeworkflow.service;

import java.util.List;
import java.util.Map;

import com.strongit.oa.bo.TOaRelativeWorkflow;
import com.strongit.oa.relativeworkflow.ProcessInstanceDto;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 关联流程信息接口类
 * @author 钟伟
 * @version 1.0
 * @date 2013-12-13
 */
public interface IRelativeWorkflowService
{
	/**
	 * 获取关联流程信息
	 * 
	 * @param rwId 关联流程实体ID
	 * @return
	 */
	public TOaRelativeWorkflow getRelativeWorkflow(String rwId)
			throws ServiceException,SystemException;

	/**
	 * 获取关联流程信息列表
	 * 
	 * @param piId 流程实例ID
	 * @return {@link List}<{@link TOaRelativeWorkflow}> 关联流程信息列表
	 */
	public List<TOaRelativeWorkflow> getRelativeWorkflows(Long piId)
			throws ServiceException,SystemException;

	/**
	 * 获取关联流程信息分页列表
	 * 
	 * @param page 分页参数信息
	 * @param piId 流程实例ID
	 * @return {@link Page}<{@link TOaRelativeWorkflow}> 关联流程信息分页列表
	 */
	public Page<TOaRelativeWorkflow> getRelativeWorkflows(Page<TOaRelativeWorkflow> page, Long piId)
			throws ServiceException,SystemException;
	
	/**
	 * 获取关联流程信息列表bySql
	 * 
	 * @param piId 流程实例ID
	 * @return  关联流程信息列表
	 */
	public List<ProcessInstanceDto> getRelativeWorkflowsBySql(Long piId)
			throws ServiceException, SystemException;

	/**
	 * 保存关联流程信息
	 * 
	 * @param rw 关联流程实体
	 * @return 无
	 */
	public void saveRelativeWorkflow(TOaRelativeWorkflow rw)
			throws ServiceException,SystemException;
	
	/**
	 * 保存关联流程
	 * 
	 * @param piId 流程实例ID
	 * @param piRefIds 关联的流程实例ID
	 * @return
	 */
	public void saveRelativeWorkflow(Long piId, String[] piRefIds)
			throws ServiceException,SystemException;
	
	/**
	 * 删除关联流程信息
	 * 
	 * @param rwId 关联流程实体ID
	 * @return
	 */
	public void deleteRelativeWorkflow(String rwId)
			throws ServiceException,SystemException;
	
	/**
	 * 删除多个关联流程信息
	 * 
	 * @param rwIds 关联流程实体ID数组
	 * @return
	 */
	public void deleteRelativeWorkflows(String[] rwIds)
			throws ServiceException,SystemException;
	
	
	/**
	 * 删除关联流程信息
	 * 
	 * @param piId 流程实例ID
	 * @return
	 */
	public void deleteRelativeWorkflowByPiId(Long piId)
			throws ServiceException,SystemException;
	
	/**
	 * 获取流程实例信息
	 * 
	 * @param curPage 当前页
	 * @param unitPage 每页行数
	 * @param userId 用户ID
	 * @param curPiId 当前的流程实例ID
	 * @return
	 */
	public Page<ProcessInstanceDto> getProcessInstances(int curPage, int unitPage, String userId, Long curPiId)
			throws ServiceException,SystemException;
	
	/**
	 * 获取流程实例的当前任务ID
	 * 
	 * @param userId 用户ID
	 * @param piId 流程实例ID
	 * @return
	 */
	public Long getTaskId(String userId, Long piId)
			throws ServiceException,SystemException;

	/**
	 * 构造流程实例MAP对象
	 * 
	 * @param piIds 流程实例ID列表
	 * @return
	 */
	public Map<Long, ProcessInstanceDto> getProcessInstances(List<Long> piIds)
			throws ServiceException,SystemException;
	
	/**
	 * 判断流程实例是否挂起
	 * @param piId 流程实例ID
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean ProcessInstanceIsSuspend(Long piId)
			throws ServiceException,SystemException;
}
