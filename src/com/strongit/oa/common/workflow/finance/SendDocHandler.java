package com.strongit.oa.common.workflow.finance;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.webservice.client.finance.guideline.Client;
import com.strongmvc.service.ServiceLocator;

/**
 * 发文归档后自动流转到指标系统中
 * @author 邓志城
 *
 */
public class SendDocHandler implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8323776441595545551L;

	private SendDocManager manager;//发文管理manager
	
	/**
	 * 构造方法获取服务对象
	 *
	 */
	public SendDocHandler(){
		manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
	}

	/**
	 * 自动执行的方法
	 */
	public void execute(ExecutionContext executionContext) throws Exception {
		long instanceId = executionContext.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String sendDocId = bussinessId.split(";")[2];
		String data = manager.getPacketBySendocId(sendDocId);
		Client.archiveGuideLine(ServletActionContext.getRequest().getSession().getId(), data);
		executionContext.getToken().signal();
	}

}
