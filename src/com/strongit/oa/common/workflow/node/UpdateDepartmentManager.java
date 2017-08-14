package com.strongit.oa.common.workflow.node;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongmvc.service.ServiceLocator;

/**
 * 
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-9-16 下午05:54:05
 * @version  2.0.7
 * @classpath com.strongit.oa.common.workflow.node.UpdateDepartmentManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class UpdateDepartmentManager implements ActionHandler {

	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -463638336963291831L;

	public void execute(ExecutionContext executionContext) throws Exception {
		IUserService userService = (IUserService)ServiceLocator.getService("userService");
		SendDocManager manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		long instanceId = processInstance.getId();	//流程实例ID
		Object[] obj = manager.getFormIdAndBusiIdByPiIdAndNodeId(String.valueOf(instanceId), String.valueOf(executionContext.getNode().getId()));
		String bussinessId = obj[1].toString();
		String sendDocId = bussinessId.split(";")[2];
		User user = null;
		try {
			user = userService.getCurrentUser();
		} catch (Exception e) {
			LOG.error("获取当前用户信息失败.");
		}
		if(user != null && !"402882282262726001226289c8cb0001".equals(user.getUserId())) { 
			manager.updateDeparentmentManager(sendDocId, user.getUserName());
			LOG.error("更新拟稿单位负责人信息到发文字段'REST4'中成功！");			
		}
	}

}
