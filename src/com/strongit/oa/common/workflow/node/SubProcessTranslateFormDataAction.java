package com.strongit.oa.common.workflow.node;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;

import com.strongit.oa.bgt.senddoc.DocManager;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.util.JsonUtil;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;

/**
 * （支持查看原表单功能）
 * 子流程代理类：传递业务数据并发送表单数据.
 * 若是同步子路程,并设置了共享父流程表单数据,则子流程启动后查看的就是父流程的表单数据.
 * 若不共享父流程表单数据,则根据映射关系,生成新的子流程数据.
 * 将父流程的业务数据、表单以及审批意见数据存储到父流程流程实例变量中.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2011年7月26日20:59:36
 * @version  5.0
 * @classpath com.strongit.oa.common.workflow.node.SubProcessTranslateFormDataAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class SubProcessTranslateFormDataAction implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6916393968385250239L;

	public void execute(ExecutionContext executionContext) throws Exception {
		SendDocManager manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
		DocManager docManager = (DocManager)ServiceLocator.getService("docManager");
		IWorkflowService service=(IWorkflowService)ServiceLocator.getService("workflowService");
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		String bussinessId = processInstance.getBusinessId();
		if(bussinessId == null) {
			throw new SystemException("流程实例 "+ processInstance.getId() +" 绑定的业务数据不存在！");
		}
		TwfBaseNodesetting nodeSetting = service.getNodesettingByNodeId(String.valueOf(executionContext.getNode().getId()));
		String formInfo = nodeSetting.getNsSubprocessSetting().split("\\|")[1];
		String type = nodeSetting.getNsSubprocessType();//子流程类型 1：同步,0：异步
		String isSameToParent = nodeSetting.getPlugin("plugins_doReturnSame");
		Long subFormId = nodeSetting.getNsNodeFormId();//父流程的表单id
		String information = JsonUtil.generateApproveToJsonBase64(service.getWorkflowApproveinfo(String.valueOf(processInstance.getId()),bussinessId));
		
		if(processInstance.getContextInstance().getVariable("@{personDemo}") == null) {
			String data,parentId = null;
			parentId = manager.copyData(bussinessId);//生成新的业务数据,避免同时启动同步子流程，异步子流程
			//同时生成新的附件记录（意见征询 dengzc 2012年2月24日17:45:54）
			docManager.copyAttachments(bussinessId.split(";")[2], parentId.split(";")[2]);
			data = parentId + ";" + subFormId + "@begin@" + information;//严建 2012-01-05 17:39 
			processInstance.getContextInstance().setVariable("@{personDemo}", data);			
		}
		if("1".equals(type) && isSameToParent != null && "1".equals(isSameToParent)) {//是同步子流程并且设置了共享父流程数据
			processInstance.getContextInstance().setVariable("@{bid}", bussinessId);
		}  else {
			String formId =formInfo.split(",")[1];
			/**
			 * yanjian 2012-07-25 16:51 处理多级子流程嵌套时，子流程节点表单使用“启动流程表单”，实现子流程表单呈现继承父流程的表单呈现
			 * 
			 * */
			if("流程启动表单".equals(formInfo.split(",")[0])){
				formId = processInstance.getMainFormId();
			}
			String[] strData = manager.getTableNameByFormId(formId);	//得到子流程启动表单挂接的业务表名及其字段	
			if(strData!=null&&strData[0]!=null&&strData[1]!=null){
				String pkey=strData[1].split(",")[0];
				String pk=manager.saveSubProccessData(bussinessId, strData,"");
				if(pk == null) {
					throw new SystemException("无法生成子流程数据，请检查是否设置了映射关系。");
				}
				processInstance.getContextInstance().setVariable("@{bid}", strData[0]+";"+pkey+";"+pk);
			} else {
				throw new SystemException("子流程表单设置错误！");
			}
		}
	}

}
