package com.strongit.oa.common.workflow.autonode;


import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongmvc.service.ServiceLocator;

/**
 * 统一归档接口
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-11-23 下午04:06:46
 * @version  3.0
 * @classpath com.strongit.oa.common.workflow.autonode.ArchiveHandlerAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class ArchiveHandlerAction implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1297722319759723099L;

	/**
	 * 发文管理Manager
	 */
	private SendDocManager manager;
	
	public ArchiveHandlerAction(){
		manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
	}

	/**
	 * 自动节点执行文件归档
	 */
	public void execute(ExecutionContext cxt) throws Exception {
		long instanceId = cxt.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		Object[] obj = manager.getFormIdAndBusiIdByPiIdAndNodeId(String.valueOf(instanceId), String.valueOf(cxt.getNode().getId()));
		if((String)obj[1]==null||"".equals((String)obj[1])){
			manager.doArchive((String)obj[2], obj[0].toString(), processInstanceId);
		}else{
			manager.doArchive((String)obj[1], obj[0].toString(), processInstanceId);
		}
		cxt.getToken().signal();
	}

}
