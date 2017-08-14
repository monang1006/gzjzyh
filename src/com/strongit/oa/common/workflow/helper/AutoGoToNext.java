package com.strongit.oa.common.workflow.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.service.ServiceLocator;

/**
 * 通过线程的异步方式将任务提交到下一办理人员.
 * @author 		邓志城
 * @date   		Jul 21, 2011
 * @classpath	com.strongit.oa.common.workflow.helper.AutoGoToNext
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public class AutoGoToNext implements Runnable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String transitionName = WorkflowConst.WORKFLOW_TRANSITION_LASTPRE;	//任务退回上一步标志
	
	private IWorkflowService workflowService;									//工作流服务接口
	
	private String taskId;														//任务id
	
	private String[] taskActors = null;								
	
	private String userId;
	
	private String suggestion;
	
	private String businessId;
	
	private String formId;
	
	private String isNewForm = "0";
	
	private String returnNodeId = "";
	
	public AutoGoToNext(String taskId,String userId,String suggestion) {
		workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
		this.taskId = taskId;
		this.userId = userId;
		this.suggestion = suggestion;
		String strNodeInfo = workflowService.getNodeInfo(taskId);
		String[] arrNodeInfo = strNodeInfo.split(",");
		if ("form".equals(arrNodeInfo[0])) {
			this.businessId = arrNodeInfo[2]; // 业务id
			this.formId  = arrNodeInfo[3]; // 表单id
		}
		List list = workflowService.getNextTransitions(taskId);
		if(list != null && !list.isEmpty()) {
			transitionName = ((Object[])list.get(0))[0].toString();
		}
	}
	
	public void run() {
		try{
			Thread.sleep(30000);//线程睡眠30秒钟
			workflowService.goToNextTransition(taskId, transitionName, returnNodeId, isNewForm, formId, businessId, suggestion, userId, taskActors);
			logger.info("线程自动将任务提交到下一环节");
		} catch (Exception e) {
			logger.error("线程自动将任务提交到下一环节时发生异常。", e);
		}
	}

}
