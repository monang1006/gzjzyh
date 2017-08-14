package com.strongit.oa.common.workflow.node;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;

import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongmvc.service.ServiceLocator;

/**
 * 
 * @author 		邓志城
 * @date   		Jul 29, 2011
 * @classpath	com.strongit.oa.common.workflow.node.SubLeaveProcessAction
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Deprecated
public class SubLeaveProcessAction implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SendDocManager manager;
	
	public SubLeaveProcessAction(){
		manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
	}

	public void execute(ExecutionContext executionContext) throws Exception {
		//将提醒(方式|内容)存储在session中
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		/*ActionContext cxt = ActionContext.getContext();
		
		 String fullFormData=(String) cxt.getSession().get("formData");//获取电子表单数据，用于归档至档案中心.
*/	//	 System.out.print(fullFormData);
		 String bid=(String) processInstance.getContextInstance().getVariable("@{bid}");
		 String parentId = (String)processInstance.getContextInstance().getVariable("@{parentBusinessId}");
		 String formId = (String)processInstance.getContextInstance().getVariable("@{parentFormId}");
		 //生成新的业务数据
		 parentId = manager.copyData(parentId);
//		 SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmssms");
//	     String newFileName = sdf.format(new Date());
//		 String src = "/personPhoto/" + newFileName + ".txt";
//		 String fileName=PathUtil.getRootPath()+ src;
//		 *********************** 得到意见数据 -----------------
		 String information = manager.getBusiFlagByProcessInstanceId(String.valueOf(processInstance.getId()));
		 String data = parentId + ";" + formId + "@begin@" + information;
		 processInstance.getContextInstance().setVariable("@{personDemo}", data);	
//		 manager.saveParentFormdata(data, bid,fileName);
		 System.out.println("********************"+bid);
		}

}

