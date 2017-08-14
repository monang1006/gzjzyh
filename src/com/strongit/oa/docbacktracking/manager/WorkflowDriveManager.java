package com.strongit.oa.docbacktracking.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.docbacktracking.service.IDocBackTrackingService;
import com.strongit.oa.docbacktracking.service.IWorkflowDriveService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 工作流驱动管理类
 * 
 * @author yanjian
 * 
 * Oct 12, 2012 4:18:01 PM
 */
@Service
@OALogger
public class WorkflowDriveManager implements IWorkflowDriveService {
	@Autowired
	IWorkflowService workflowService;
	
	@Autowired
	IProcessInstanceService processInstanceService;

	@Autowired
	IProcessDefinitionService processDefinitionService;

	@Autowired
	ITaskService taskService;

	@Autowired
	IUserService userService;

	@Autowired
	IDocBackTrackingService docBackTrackingService;

	@Autowired
	JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持
	
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
	@SuppressWarnings("unchecked")
	public String handleWorkflow(String formId, String workflowName,
			String bussinessId, String[] strTaskActorArray,
			String transitionName, String concurrentTrans, String submitOption,Date handleDate)
			throws ServiceException, DAOException, SystemException {
		try {

			// 读取标题
			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkFieldName = args[1];
			String pkFieldValue = args[2];
			Map map = docBackTrackingService.getSystemField(pkFieldName,
					pkFieldValue, tableName);
			String businessName = (String) map.get(BaseWorkflowManager.WORKFLOW_TITLE);
			if (businessName == null || "".equals(businessName)) {
				businessName = workflowName;
			} else {
				businessName = businessName.replaceAll("\\r\\n", "");// 处理有回车换行的情况
			}
			User curUser = userService.getCurrentUser();
			String instanceId = workflowService.startWorkflow(formId, workflowName, curUser
					.getUserId(), bussinessId, businessName, strTaskActorArray,
					transitionName, concurrentTrans, submitOption);
			List toSelectItems = new ArrayList(1);
			toSelectItems.add("taskId");
			Map paramsMap = new HashMap(1);
			paramsMap.put("processInstanceId", instanceId);
			if (handleDate != null) {
				List list = workflowService.getTaskInfosByConditionForList(
						toSelectItems, paramsMap, null, null, null, null, null);
				if (list != null && !list.isEmpty()) {
					String taskId = list.get(0).toString();
					List<TwfInfoApproveinfo> wfApproveInfo = workflowService
							.getDataByHql(
									"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
									new Object[] { new Long(taskId) });
					if (!wfApproveInfo.isEmpty()) {
						TwfInfoApproveinfo wfAInfo = wfApproveInfo.get(0);
						wfAInfo.setAiDate(handleDate);
						workflowService.saveApproveInfo(wfAInfo);// 更新意见和内容
					}
				}
			}
			return instanceId;
			
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
			String curActorId, String[] taskActors,Date handleDate) throws SystemException, ServiceException {
		try {
//			 校验任务是否已签收
			Object[] toSelectItems = { "isReceived" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskId", taskId);
			System.out.println();
			List listWorkflow = workflowService.getTaskInfosByConditionForList(sItems,
					paramsMap, null, null, null, null, null);
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				Object isReceived = listWorkflow.get(0);
				if (!"1".equals(isReceived)) {// 未签收状态
					this.signForTask(taskId,curActorId, "0", new OALogInfo("签收任务"));
				}
			}
			workflowService.goToNextTransition(taskId, transitionName, returnNodeId,
					isNewForm, formId, businessId, suggestion, curActorId, taskActors);
			if(handleDate != null){
				TwfInfoApproveinfo wfAInfo = getApproveInfoByTaskId(taskId);
				if(wfAInfo != null){
					wfAInfo.setAiDate(handleDate);
					workflowService.saveApproveInfo(wfAInfo);// 更新意见和内容
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "流程跳转" });
		}
	}
	
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
	public TwfInfoApproveinfo getApproveInfoByAiId(String aiId)throws ServiceException,DAOException, SystemException{
		try {
			TwfInfoApproveinfo result = null;
			List<TwfInfoApproveinfo> list = workflowService.getDataByHql(
					"from TwfInfoApproveinfo ta where ta.aiId = ?",
					new Object[] { new Long(aiId) });
			if(list != null && !list.isEmpty()){
				result = list.get(0);
			}
			return result;
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
	public List<TwfInfoApproveinfo> getApproveInfoForListByPiId(String aiPiId) throws ServiceException,DAOException, SystemException{
		try {
			List<TwfInfoApproveinfo> result = workflowService.getDataByHql(
					"from TwfInfoApproveinfo ta where ta.aiPiId = ? order by ta.aiDate ",
					new Object[] { new Long(aiPiId) });
			return result;
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
	public TwfInfoApproveinfo getApproveInfoByTaskId(String taskId)throws ServiceException,DAOException, SystemException{
		try {
			TwfInfoApproveinfo result = null;
			List<TwfInfoApproveinfo> list = workflowService.getDataByHql(
					"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
					new Object[] { new Long(taskId) });
			if(list != null && !list.isEmpty()){
				result = list.get(0);
			}
			return result;
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
	public void saveApproveInfo(TwfInfoApproveinfo wfAInfo)throws ServiceException,DAOException, SystemException{
		try {
			workflowService.saveApproveInfo(wfAInfo);
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
	
	/**
	 * 签收任务 增加签收时设置前置任务实例为不可取回（并行会签时只有最后一个任务实例可改变取回状态）
	 * 
	 * @param userId
	 *            用户id
	 * @param taskId
	 *            任务id
	 * @param flag
	 *            签收标志符，flag="0":签收并处理，需要挂起会签实例；flag="1":仅签收，不需挂起会签实例
	 * @throws WorkflowException
	 */
	public void signForTask(String taskId, String userId, String flag,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {
			taskService.receiveTask(userId, taskId, flag);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "签收工作" });
		}
	}
}
