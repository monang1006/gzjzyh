package com.strongit.oa.docbacktracking.service;

import java.util.Date;
import java.util.List;

import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IWorkflowDriveService {
	/**
	 * 提交工作流处理 
	 * 
	 * @author yanjian
	 * @param formId	表单id
	 * @param workflowName	流程名称
	 * @param bussinessId	业务id
	 * @param strTaskActorArray	下一步处理人
	 * @param transitionName	迁移线名称
	 * @param concurrentTrans	
	 * @param submitOption		意见信息
	 * @param handleDate		处理时间
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 16, 2012 2:40:40 PM
	 */
	public String handleWorkflow(String formId, String workflowName,
			String bussinessId, String[] strTaskActorArray,
			String transitionName, String concurrentTrans, String submitOption,Date handleDate)
			throws ServiceException, DAOException, SystemException ;
	/**
	 * 提交工作流转到下一步
	 * 
	 * @author yanjian
	 * @param taskId
	 *            任务id
	 * @param transitionName
	 *            迁移线名称
	 * @param returnNodeId
	 *            返回的节点id
	 * @param isNewForm
	 *            是否新表单
	 * @param formId
	 *            表单id
	 * @param businessId
	 *            业务id
	 * @param suggestion
	 *            意见信息
	 * @param curActorId
	 *            提交人
	 * @param taskActors
	 *            下一步处理人
	 * @param handleDate
	 *            处理时间
	 * @throws SystemException
	 * @throws ServiceException
	 *             Oct 16, 2012 2:27:59 PM
	 */
	@SuppressWarnings("unchecked")
	public void handleWorkflowNextStep(String taskId,
			String transitionName, String returnNodeId, String isNewForm,
			String formId, String businessId, String suggestion,
			String curActorId, String[] taskActors,Date handleDate);
	/**
	 * 保存意见信息
	 * 
	 * @author yanjian
	 * @param wfAInfo
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 10:12:46 AM
	 */
	public void saveApproveInfo(TwfInfoApproveinfo wfAInfo)throws ServiceException,DAOException, SystemException;
	/**
	 * 根据任务id获取办理记录信息
	 * 
	 * @author yanjian
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 10:10:43 AM
	 */
	public TwfInfoApproveinfo getApproveInfoByTaskId(String taskId)throws ServiceException,DAOException, SystemException;
	/**
	 * 根据流程实例id获取办理记录信息
	 * 
	 * @author yanjian
	 * @param piId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 10:24:42 AM
	 */
	@SuppressWarnings("unchecked")
	public List<TwfInfoApproveinfo> getApproveInfoForListByPiId(String aiPiId) throws ServiceException,DAOException, SystemException;
	/**
	 * 根据主键id获取办理记录信息
	 * 
	 * @author yanjian
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 10:10:43 AM
	 */
	public TwfInfoApproveinfo getApproveInfoByAiId(String aiId)throws ServiceException,DAOException, SystemException;
}
