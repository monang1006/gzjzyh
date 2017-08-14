package com.strongit.oa.docbacktracking.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.docbacktracking.vo.WorkflowInfoVo;
import com.strongit.oa.docbacktracking.vo.WorklowNodeVo;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IDocBackTrackingService {
	/**
	 * 根据主键id,表名称得到默认的信息项字段
	 * 
	 * @author:邓志城
	 * @date:2010-11-13 上午09:32:26
	 * @param id
	 *            主键值
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map getSystemField(String id, String tableName);
	
	public Map getSystemField(String pkFieldName,String pkFieldValue,String tableName);
	
	/**
	 * 获取表单ID和业务ID
	 * 
	 * @param taskId
	 * @return String[] 数据格式:[业务ID,表单ID]
	 */
	@Transactional(readOnly = true)
	public String[] getFormIdAndBussinessIdByTaskId(String taskId)
			throws SystemException, ServiceException;
	/**
	 * 展现操作按钮 1、新建流程时仅显示 a、提交下一步 b、打印处理单 2、审批流程时显示 a、退回 允许退回的情况下 b、驳回 允许驳回的情况下
	 * c、指派 允许指派的情况下 d、指派返回 允许指派返回的情况下,注意指派返回后不能提交下一步 e、提交下一步 不允许提交下一步的情况下
	 * f、查看办理状态 g、打印处理单
	 * 
	 * @author:邓志城
	 * @date:2009-12-14 下午10:25:51
	 * @param taskId
	 *            任务实例id
	 * @return 满足条件的html代码 子类可根据需要重载此方法
	 */
	public TwfBaseNodesetting getOperationHtml(String taskId,
			String workflowName) ;
	/**
	 * 根据表名称获取表主键名称,不支持复核主键.
	 * 
	 * @author:邓志城
	 * @date:2010-3-1 下午05:30:48
	 * @param tableName
	 *            表名称
	 * @return 主键名称
	 * @throws SystemException
	 *             数据库不支持getMetaData()方法.
	 */
	public String getPrimaryKeyName(String tableName) throws SystemException;
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
			throws ServiceException, DAOException, SystemException ;
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
	 *             Oct 15, 2012 2:13:09 PM
	 */
	public List<WorklowNodeVo> getWorklowNodeVoList(String workflowName,
			String businessId) throws ServiceException, DAOException,
			SystemException;
	/**
	 * 获取流程实例id
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
	public  Map<String,String> getPIdByBusid(String businessId,String startUserId,String workflowName) throws ServiceException,
			DAOException, SystemException ;
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
			throws ServiceException, DAOException, SystemException;
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
			DAOException, SystemException;
	/**
	 * 删除公文信息
	 * 
	 * @author yanjian
	 * @param bussinessId
	 *            业务id
	 * @param userId
	 *            用户id
	 * @param workflowName
	 *            流程名称
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Nov 26, 2012 4:25:55 PM
	 */
	public void deleteDoc(String bussinessId, String userId, String workflowName)
			throws ServiceException, DAOException, SystemException;
}
