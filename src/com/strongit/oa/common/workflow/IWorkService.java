package com.strongit.oa.common.workflow;

import java.util.List;

import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IWorkService {

	/**
	 * 根据流程实例Id获取流程中每个节点的处理意见和对应的节点设置信息
	 * @author 喻斌
	 * @date Dec 19, 2009 2:20:19 PM
	 * @param processInstanceIds -流程实例Id集合
	 * @return List<Object[]> -每个节点的处理意见和对应的节点设置信息<br>
	 * 		<p>Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象, (9)审批数字签名, (10)任务委派类型【0：委托；1：指派；“”：非委托】, (11)最后一个委托/指派人Id, (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称, (15)主键值, (16)流程实例Id}</p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getBusiFlagByProcessInstanceIds(List<Long> processInstanceIds) throws WorkflowException;
	
	/**
	 * 监控时批量查询流程子流程实例信息
	 * @author	蔡德伍
	 * @date	2014-4-17 19:51:43
	 * @param instanceId -流程实例Id
	 * @return List<Object[]> 流程子流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务名称，(2)流程名称}
	 * @throws WorkflowException
	 */
	public List<Object[]> getMonitorChildrenInstanceIds(List<Long> instanceIds)throws WorkflowException ;
	
	 /**
     * 根据用户ID集合批量查询用户信息.
     * @param userIds  用户ID
     * @return
     * @throws DAOException
     * @throws SystemException
     * @throws ServiceException
     */
    public List<TUumsBaseUser> getUsersByUserIds(List<String> userIds)throws DAOException,
            SystemException, ServiceException;
    
	/**
	 * 根据用户id批量获取用户所属部门信息
	 *
	 * @author 蔡德伍
	 * @company		Thinvent Ltd. (C) copyright
	 * @date		2014-4-19 下午2:43:39
	 * @param userIds	用户id
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
public List<TUumsBaseOrg> getOrgInfosByUserIds(List<String> userIds) throws DAOException,
	SystemException, ServiceException;

}
