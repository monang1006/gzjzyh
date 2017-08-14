package com.strongit.oa.common.workflow.autonode;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.stereotype.Component;

import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.annotation.AgentClassType;
import com.strongit.oa.common.workflow.annotation.Use;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.service.ServiceLocator;

/**
 * 子流程代理类
 * @author 彭小青
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-2-25 下午04:30:38
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.workflow.autonode.SubProcessAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@SuppressWarnings("serial")
@Use(value = "子流程代理类",type = AgentClassType.AUTONODECLASS)
@Component("subProcessAutoNode")
@Deprecated
public class SubProcessAction implements ActionHandler {

	public void execute(ExecutionContext executionContext) throws Exception {
		SendDocManager manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
		IWorkflowService service=(IWorkflowService)ServiceLocator.getService("workflowService");
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		long instanceId = processInstance.getId();	//流程实例ID
		List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(String.valueOf(instanceId));
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();		//表名;主键;主键值
		if(bussinessId.indexOf(";")==-1){
			bussinessId="T_OARECVDOC"+";"+"OARECVDOCID"+";"+bussinessId;
		}
		//String contentFiled= manager.getContentFiled(obj[0].toString());
		TwfBaseNodesetting nodeSetting=service.getNodesettingByNodeId(String.valueOf(executionContext.getNode().getId()));
		String formInfo = nodeSetting.getNsSubprocessSetting().split("\\|")[1];
		String formId =formInfo.split(",")[1];
		String[] strData = manager.getTableNameByFormId(formId);	//得到子流程启动表单挂接的业务表名及其字段	
		if(strData!=null&&strData[0]!=null&&strData[1]!=null){
			String pkey=strData[1].split(",")[0];
			String pk=manager.saveSubProccessData(bussinessId, strData,"");
			if(pk!=null){
				processInstance.getContextInstance().setVariable(WorkflowConst.WORKFLOW_SUB_CUSTOM_BUSIID, strData[0]+";"+pkey+";"+pk);
				processInstance.getContextInstance().setVariable("@{bname}", "");
			}
		}
	}
}
