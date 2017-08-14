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
 * @author 刘皙
 * @company Strongit Ltd. (C) copyright
 * @date 2012年5月14日15:40:29
 * @version StrongOA5.0
 * @classpath com.strongit.oa.common.workflow.node.GetShenPiToRest7
 * @comment 得到审批人员姓名，并放入对应Rest7中。
 * @email liuxi@strongit.com.cn
 */
public class GetShenPiToRest7 implements ActionHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1471613179069753697L;
	Logger LOG = LoggerFactory.getLogger(this.getClass());

	public void execute(ExecutionContext executionContext) throws Exception {
		IUserService userService = (IUserService) ServiceLocator.getService("userService");
		SendDocManager manager = (SendDocManager) ServiceLocator.getService("sendDocManager");
		ProcessInstance processInstance = executionContext.getProcessInstance();// 获取流程实例对象
		long instanceId = processInstance.getId(); // 流程实例ID
		Object[] obj = manager.getFormIdAndBusiIdByPiIdAndNodeId(String.valueOf(instanceId),
			String.valueOf(executionContext.getNode().getId()));
		String bussinessId = obj[1].toString();
		String sendDocId = bussinessId.split(";")[2];
		User user = null;
		try {
			user = userService.getCurrentUser();
		}
		catch (Exception e) {
			LOG.error("获取当前用户信息失败.");
		}
		if (user != null) {
			manager.updateShenPiToRest7(sendDocId, user.getUserName());
			LOG.error("更新审批人信息到发文字段'REST7'中成功！");
		}
	}
}
