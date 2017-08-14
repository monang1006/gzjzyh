package com.strongit.oa.common.workflow.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.applicationInterface.ApplicationInterfaceExtendManager;
import com.strongit.oa.util.StringUtil;
import com.strongmvc.service.ServiceLocator;

/**
 * 流程超时自动强制取回到指定节点
 * 
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 15, 2012 11:52:04 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.helper.AutoMustFetchTask
 */
public class AutoMustFetchTask implements Runnable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String instanceId;

	private ApplicationInterfaceExtendManager applicationInterfaceExtendManager;

	private IUserService userService; // 统一用户接口

	private IWorkflowService workflowService;

	public AutoMustFetchTask(String instanceId) {
		applicationInterfaceExtendManager = (ApplicationInterfaceExtendManager) ServiceLocator
				.getService("applicationInterfaceExtendManager");
		userService = (IUserService) ServiceLocator.getService("userService");
		workflowService = (IWorkflowService) ServiceLocator
				.getService("workflowService");
		this.instanceId = instanceId;
	}

	public void run() {
		try {
			logger.info("#######	线程开始自动将流程强制取回！		#######");
			boolean isExecute = false;// 是否可以执行，默认不执行
			boolean flag = true;
			long maxtime = System.currentTimeMillis() + (60 * 30*1000);// 当前线程生命周期的结束时间，默认是30分钟
			while (flag) {
				Thread.sleep(6000);// 线程睡眠6秒钟
				logger.info("#######	检查流程实例id=" + instanceId + "是否超期		#######");
				if (isTimeOut()) {// 确认流程已经超期时，不再等待
					flag = false;
					isExecute = true;// 可以执行操作
				}
				if (System.currentTimeMillis() - maxtime > 0) {// 超过了该流程的生命周期的最长时间,结束循环
					logger.info("#######	监听线程结束，原因：在30分钟内，监听线程未发现流程实例id="
							+ instanceId + "超期		#######");
					break;
				}
			}
			if (isExecute) {
				logger.info("#######	流程实例id=" + instanceId
						+ "超期，进行流程强制取回操作		#######");
				userService.setUserId(IUserService.SYSTEM_ACCOUNT);
				applicationInterfaceExtendManager
						.mustFetchTaskStart(instanceId);// 结束该流程所有的子流程
				logger.info("线程自动进行流程强制取回操作");
			}
			logger.info("#######	线程自动将流程强制取回完毕！		#######");
		} catch (Exception e) {
			logger.error("线程自动将流程强制取回时发生异常。", e);
		} finally {
			userService.setUserId(null);
		}
	}

	/**
	 * 流程是否超期
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Jun 26, 2012 12:40:48 PM
	 */
	@SuppressWarnings("unchecked")
	public boolean isTimeOut() {
		boolean result = false;
		ArrayList toSelectItems = new ArrayList();
		toSelectItems.add("processTimeout");
		HashMap paramsMap = new HashMap();
		paramsMap.put("processInstanceId", instanceId);
		List list = workflowService.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, null, null, null, null, null);
		if (list != null && !list.isEmpty()) {
			if ("1".equals(StringUtil.castString(list.get(0)))) {
				result = true;
			}
		}
		return result;
	}
}
