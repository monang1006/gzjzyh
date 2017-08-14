package com.strongit.oa.common.workflow.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.StringUtil;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.service.ServiceLocator;

/**
 * 任务超时,通过线程的异步方式将任务退回到上一办理人员.
 * 
 * @author 邓志城
 * @date Jul 21, 2011
 * @classpath com.strongit.oa.common.workflow.helperAutoBackLast.AutoBackLast.java
 * @email dengzc@strongit.com.cn
 * @tel 0791-8186916
 */
public class AutoBackLast implements Runnable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String transitionName = WorkflowConst.WORKFLOW_TRANSITION_LASTPRE; // 任务退回上一步标志

	private IWorkflowService workflowService; // 工作流服务接口

	private IUserService userService; // 统一用户接口

	private String taskId; // 任务id

	private String[] taskActors = null;

	private String userId;

	private String suggestion;

	private String businessId;

	private String formId;

	private String isNewForm = "0";

	private String returnNodeId = "";

	@SuppressWarnings("unchecked")
	public AutoBackLast(TaskInstance ti) {
		workflowService = (IWorkflowService) ServiceLocator
				.getService("workflowService");
		userService = (IUserService) ServiceLocator.getService("userService");
		String taskId = String.valueOf(ti.getId());
		String userId = ti.getActorId(); // 当前任务处理人
		Set<PooledActor> set = ti.getPooledActors();
		if (set != null && !set.isEmpty()) {
			userId = set.iterator().next().getActorId();
		} else {
			userId = "402882282262726001226289c8cb0001";
		}
		StringBuilder sugggestion = new StringBuilder("流程["
				+ ti.getProcessInstance().getBusinessName() + "],[").append(
				ti.getName()).append("]环节超时,");
		sugggestion.append("自动将任务退回到上一环节.");
		JSONObject json = new JSONObject();
		json.put("suggestion", sugggestion.toString()+ "     ");
		json.put("CAInfo", "");
		this.taskId = taskId;
		this.userId = userId;
		this.suggestion = json.toString();
		String strNodeInfo = workflowService.getNodeInfo(taskId);
		String[] arrNodeInfo = strNodeInfo.split(",");
		if ("form".equals(arrNodeInfo[0])) {
			this.businessId = arrNodeInfo[2]; // 业务id
			this.formId = arrNodeInfo[3]; // 表单id
		}
	}
	
	public AutoBackLast(String taskId, String userId, String suggestion) {
		workflowService = (IWorkflowService) ServiceLocator
				.getService("workflowService");
		userService = (IUserService) ServiceLocator.getService("userService");
		this.taskId = taskId;
		this.userId = userId;
		this.suggestion = suggestion;
		String strNodeInfo = workflowService.getNodeInfo(taskId);
		String[] arrNodeInfo = strNodeInfo.split(",");
		if ("form".equals(arrNodeInfo[0])) {
			this.businessId = arrNodeInfo[2]; // 业务id
			this.formId = arrNodeInfo[3]; // 表单id
		}
	}

	public void run() {
		try {
			boolean isExecute = false;// 是否可以执行，默认不执行
			boolean flag = true;
			long maxtime = System.currentTimeMillis() + (60 * 30*1000);// 当前线程生命周期的结束时间
			while (flag) {
				Thread.sleep(6000);// 线程睡眠6秒钟
				if (isTimeOut()) {
					flag = false;
					isExecute = true;// 可以执行操作
				}
				if (System.currentTimeMillis() - maxtime > 0) {// 超过了该流程的生命周期的最长时间,结束循环
					break;
				}
			}
			if (isExecute) {
				userService.setUserId(IUserService.SYSTEM_ACCOUNT);
				workflowService.goToNextTransition(taskId, transitionName,
						returnNodeId, isNewForm, formId, businessId,
						suggestion, userId, taskActors);
				logger.info("线程自动将任务退回上一办理人");
			}
		} catch (Exception e) {
			logger.error("线程自动将任务退回上一办理人时发生异常。", e);
		} finally {
			userService.setUserId(null);
		}
	}

	/**
	 * 任务是否超期
	 * 
	 * @author 严建
	 * @return
	 * @createTime Jun 26, 2012 1:16:00 PM
	 */
	@SuppressWarnings("unchecked")
	public boolean isTimeOut() {
		boolean result = false;
		ArrayList toSelectItems = new ArrayList();
		toSelectItems.add("taskTimeout");
		HashMap paramsMap = new HashMap();
		paramsMap.put("taskId", taskId);
		List list = workflowService.getTaskInfosByConditionForList(
				toSelectItems, paramsMap, null, null, null, null, null);
		if (list != null && !list.isEmpty()) {
			if ("1".equals(StringUtil.castString(list.get(0)))) {
				result = true;
			}
		}
		return result;
	}
}
