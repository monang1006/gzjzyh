package com.strongit.oa.common.workflow.finance;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.webservice.client.finance.guideline.Client;
import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;

/**
 * 自动节点执行来文流转到指标系统中
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-7-15 上午09:11:25
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.workflow.finance.RecvDocHandler
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class RecvDocHandler implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3526171614215886539L;
	/**
	 * 来文管理Manager
	 */
	private SendDocManager manager;
	
	public RecvDocHandler(){
		manager = (SendDocManager)ServiceLocator.getService("recvDocManager");
	}

	/**
	 * 有工作流引擎自动执行的方法【执行来文转到指标系统】
	 */
	public void execute(ExecutionContext cxt) throws Exception {
		long instanceId = cxt.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		LogPrintStackUtil.logInfo(processInstanceId);
		List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String data = "";//manager.getPacketByDocId(bussinessId);
		Client.archiveGuideLine(ServletActionContext.getRequest().getSession().getId(), data);
		cxt.getToken().signal();
		throw new SystemException("不支持的方法");
	}
}
