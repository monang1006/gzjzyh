package com.strongit.oa.common.workflow;


import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.service.ServiceLocator;

/**
 * 收文归档自动节点
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-3-25 上午10:01:21
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.workflow.RecvDocHandlerAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class RecvDocHandlerAction implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1297722319759723099L;

	/**
	 * 来文管理Manager
	 */
	private SendDocManager manager;
	
	public RecvDocHandlerAction(){
		manager = (SendDocManager)ServiceLocator.getService("recvDocManager");
	}

	/**
	 * 自动节点执行文件归档
	 */
	public void execute(ExecutionContext cxt) throws Exception {
		long instanceId = cxt.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		/*List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);*/
		Object[] obj = manager.getFormIdAndBusiIdByPiIdAndNodeId(String.valueOf(instanceId), String.valueOf(cxt.getNode().getId()));
		String bussinessId = obj[1].toString();
		String recvDocId = bussinessId.split(";")[2];
		manager.addTempFileInterface(String.valueOf(WorkFlowTypeConst.RECEDOC), recvDocId,obj[0].toString(),processInstanceId,new OALogInfo("归档来文，id="+recvDocId));
		cxt.getToken().signal();
	}

}
