package com.strongit.oa.common.remind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.service.ServiceLocator;

/**
 * 工作流流转过程中的消息提醒类
 * 此类在流程流转过程中，其execute方法会被工作流引擎自动调用。
 * 在前台，通过将提醒方式存储在session中。在后台，在RemindManager
 * 中实现逻辑处理。
 * @author 邓志城
 *
 */
public class WorkFlowRemindAction implements ActionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2088255544199273660L;
	
	/**
	 * 提醒服务类
	 */
	private RemindManager manager;
	
	private SendDocManager baseManager;

	/**
	 * 构造方法注入提醒服务
	 */
	public WorkFlowRemindAction(){
		manager = (RemindManager)ServiceLocator.getService("remindManager");
		baseManager = (SendDocManager)ServiceLocator.getService("sendDocManager");
	}

	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext cxt) throws Exception {
		Object object = cxt.getProcessInstance().getContextInstance()
				.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);
		Collection taskinstances = cxt.getTaskMgmtInstance().getTaskInstances();
		if(object == null && taskinstances.size() == 1){
			return;
		}
		TaskInstance taskInstance = cxt.getTaskInstance();
		if(taskInstance == null){
			return ;
		}
		Set<PooledActor> actorSet = taskInstance.getPooledActors();
		if(!actorSet.isEmpty()){ 
			List<String> receivers = new ArrayList<String>();
			for(Iterator<PooledActor> it=actorSet.iterator();it.hasNext();){
				receivers.add(it.next().getActorId());
			}
			ProcessInstance instance = cxt.getContextInstance().getProcessInstance();
			long instanceId = instance.getId();
			String processInstanceId = String.valueOf(instanceId);
			Object[] obj = baseManager.getFormIdAndBusiIdByPiIdAndNodeId(String.valueOf(instanceId), String.valueOf(cxt.getNode().getId()));
			String bussinessId = obj[2].toString();//instance.getBusinessId();//
			String[] businessIds = bussinessId.split(";");
			Object flag = baseManager.getFieldValue(BaseWorkflowManager.PERSON_CONFIG_FLAG, businessIds[0], businessIds[1], businessIds[2]);
			Task task = new Task();
			task.setTaskId(String.valueOf(taskInstance.getId()));//任务id
			task.setTaskName(taskInstance.getName());			//任务名称
			task.setJjcd((String)flag);				 			//紧急程度
			task.setCreater(instance.getStartUserName());		//发起人名称
			task.setCreateTime(instance.getStart());			//启动时间
			task.setBusinessName(instance.getBusinessName());	//业务标题
			task.setWorkflowInstanceId(processInstanceId);		//流程实例id
			ActionContext context = ActionContext.getContext();
			
			StringBuilder sugggestion = new StringBuilder("流程["+instance.getBusinessName()+"]").append("超时，由系统自动退回。");
			String handlerMes = sugggestion.toString();
			if(context != null) {
				Map<String, Object> session = context.getSession();
				handlerMes = (String)session.get("handlerMes");
				
				HttpServletRequest request = ServletActionContext.getRequest();
				String path = request.getContextPath();
				if(path.endsWith("/")) path="";
				String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
				task.setHref(basePath + "senddoc/sendDoc!CASign.action");
			} else {
				task.setHref("/senddoc/sendDoc!CASign.action");
			}
			task.setContent(handlerMes);
			
			
			manager.sendRemind(receivers,task);
		}
	}
	
}
