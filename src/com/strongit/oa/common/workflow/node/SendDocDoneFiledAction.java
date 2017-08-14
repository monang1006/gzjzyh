package com.strongit.oa.common.workflow.node;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongmvc.service.ServiceLocator;

/**
 * 发文办结归档自动处理类
 * @author  ganyao
 *com.strongit.oa.common.workflow.node.SendDocDoneFiledAction
 */
public class SendDocDoneFiledAction implements ActionHandler {

	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 3025626755874014836L;
	
	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext executionContext) throws Exception {
		//获取发文信息
		SendDocManager manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		long instanceId = processInstance.getId();	//流程实例ID
		String bussiness = processInstance.getBusinessId();	//表名;主键;主键值      当前流程
		manager.getSendDocContent(bussiness, String.valueOf(instanceId));		

	}

}
