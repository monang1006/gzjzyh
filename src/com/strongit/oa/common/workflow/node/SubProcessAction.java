package com.strongit.oa.common.workflow.node;

import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.annotation.AgentClassType;
import com.strongit.oa.common.workflow.annotation.Use;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongmvc.service.ServiceLocator;

/**
 * 子流程代理类（不支持查看原表单功能）
 * 
 * @author 彭小青
 * @company Strongit Ltd. (C) copyright
 * @date 2010-2-25 下午04:30:38
 * @version 2.0.2.3
 * @classpath com.strongit.oa.common.workflow.node.SubProcessAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@SuppressWarnings("serial")
@Use(value = "子流程代理类", type = AgentClassType.NODECLASS)
@Component("subProcessNode")
public class SubProcessAction implements ActionHandler {

	public void execute(ExecutionContext executionContext) throws Exception {
		SendDocManager manager = (SendDocManager) ServiceLocator
				.getService("sendDocManager");
		IWorkflowService service = (IWorkflowService) ServiceLocator
				.getService("workflowService");
		ProcessInstance processInstance = executionContext.getProcessInstance();// 获取流程实例对象(例如：收文转发文，当前流程未收文流程)
		ContextInstance contextInstance = processInstance.getContextInstance();
		ActionContext cxt = ActionContext.getContext();

		if (cxt != null) {
			String transDept = (String) cxt.getSession().get("transDept");
			if (transDept != null && !"".equals(transDept)) {// 协办处室标志
				contextInstance.setVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_TRANSITIONDEPT,
						transDept);// 存储协办处室标志
			}
		}

		TwfBaseNodesetting nodeSetting = service.getNodesettingByNodeId(String
				.valueOf(executionContext.getNode().getId()));
		String formInfo = nodeSetting.getNsSubprocessSetting().split("\\|")[1];
		String type = nodeSetting.getNsSubprocessType();// 子流程类型 1：同步,0：异步
		String formId = formInfo.split(",")[1];
		/**
		 * yanjian 2012-07-25 16:51 处理多级子流程嵌套时，子流程节点表单使用“启动流程表单”，实现子流程表单呈现继承父流程的表单呈现
		 * 
		 * */
		if("流程启动表单".equals(formInfo.split(",")[0])){
			formId = processInstance.getMainFormId();
		}
		String isSameToParent = nodeSetting.getPlugin("plugins_doReturnSame");
		String bussinessId = processInstance.getBusinessId();
		String parentFormId = processInstance.getMainFormId();
		if ("1".equals(type) && isSameToParent != null
				&& "1".equals(isSameToParent)) {// 是同步子流程并且设置了共享父流程数据
			contextInstance.setVariable("@{bid}", bussinessId);// 供子流程设置businessId
			/*
			 * 兼容之前版本com.strongit.oa.common.workflow.node.SubLeaveProcessAction遗留的问题
			 * */
			contextInstance.setVariable("@{parentFormId}", parentFormId);
	        contextInstance.setVariable("@{parentBusinessId}", bussinessId);
	        /* end */
			System.out.println("共享父流程数据.............");
		} else {
			String[] strData = manager.getTableNameByFormId(formId); // 得到子流程启动表单挂接的业务表名及其字段
			if (strData != null && strData[0] != null && strData[1] != null) {
				String pkey = strData[1].split(",")[0];
				String pk = manager.saveSubProccessData(bussinessId, strData,
						"");
				if (pk != null) {
					contextInstance.setVariable("@{bid}", strData[0] + ";"
							+ pkey + ";" + pk);
					/*
					 * start
					 * 兼容之前版本com.strongit.oa.common.workflow.node.SubLeaveProcessAction遗留的问题
					 * */
					contextInstance.setVariable("@{parentFormId}", parentFormId);
			        contextInstance.setVariable("@{parentBusinessId}", bussinessId);
			        /* end */
				}
			}
		}
	}

}
