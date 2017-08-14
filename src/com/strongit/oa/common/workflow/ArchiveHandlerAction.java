package com.strongit.oa.common.workflow;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.service.ServiceLocator;

/**
 * 公文归档.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-2-24 下午04:38:22
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.workflow.ArchiveHandlerAction
 * @comment
 * @email dengzc@strongit.com.cn
 * 
 * 替代类：com.strongit.oa.common.workflow.autonode.ArchiveHandlerAction
 */
@Deprecated
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
		//List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		//Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String sendDocId = bussinessId.split(";")[2];
		manager.addTempFileInterface(String.valueOf(WorkFlowTypeConst.SENDDOC), sendDocId,obj[0].toString(),processInstanceId,new OALogInfo("归档公文，id="+sendDocId));
		cxt.getToken().signal();
	}

}
