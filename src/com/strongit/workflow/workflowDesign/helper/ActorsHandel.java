/**
 * ActorsHandel 流程任务分配人员代理
 */
package com.strongit.workflow.workflowDesign.helper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.uupInterface.UupUtil;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.service.ServiceLocator;


public class ActorsHandel implements AssignmentHandler {

	private ITaskService processService= null;
	
	private ITaskService getProcessService(){
		if(processService == null){
			processService = (ITaskService)ServiceLocator.getService("taskService");
		}
		return processService;
	}
	
	private UupUtil uupBroker= null;
	
	private UupUtil getUupInterface(){
		if(uupBroker == null){
			uupBroker = (UupUtil)ServiceLocator.getService("uupUtil");
		}
		return uupBroker;
	}	
	
	private Set<String> preTaskHandler = null;
	
	private Set<String> getPreTaskHandler(ExecutionContext context){
		if(preTaskHandler == null){
			String preTaskInstance = String.valueOf(context.getProcessInstance().getContextInstance()
					.getVariableLocally(WorkflowConst.WORKFLOW_TASK_CURRENTINFO, context.getToken()));
			TaskInstance ti = context.getJbpmContext().getTaskInstance(Long.parseLong(preTaskInstance));
			if(ti != null){
				//该任务为会签任务
				if (ti.getIsCountSign() != null) {
					String consignTask = String.valueOf(context
							.getProcessInstance().getContextInstance()
							.getVariable(
									WorkflowConst.WORKFLOW_TASK_CONSIGNTASKS
											+ "_"
											+ ti.getTask().getTaskNode()
													.getId()));
					if (!"null".equals(consignTask) && !"".equals(consignTask)) {
						String[] consignTasks = consignTask.split(",");
						TaskInstance taskInstance;
						for (int i = 0; i < consignTasks.length; i++) {
							taskInstance = context.getJbpmContext()
									.getTaskInstance(
											Long.parseLong(consignTasks[i]));
							if(taskInstance != null && !"".equals(taskInstance.getActorId())){
								preTaskHandler.add(taskInstance.getActorId());
							}
						}
					}
				//普通任务
				}else{
					preTaskHandler = new HashSet<String>();
					preTaskHandler.add(ti.getActorId());
				}
			}
		}
		return preTaskHandler;
	}
	
	/**
	 * 取得节点具体信息
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	private Object[] getNodeInfo(Long nodeId) throws WorkflowException{
	    	String sql = new String("select t.nsTaskActors, t.isActiveactor, t.nsReassign, t.nsReassignmore, t.nsTaskMaxactors, t.nsNodeFormId, t.nsIsMainform from TwfBaseNodesetting t where t.nsNodeId=?");
	    	List typeList = getProcessService().getDataByHql(sql, new Object[]{nodeId});
	    	if(!typeList.isEmpty()){
	    		return (Object[])typeList.get(0);
	    	}else{
	    		throw new WorkflowException("不存在该节点记录！");
	    	}
	}
	
	/**
	 * 得到曾经处理过该任务的人员Id
	 * @author 喻斌
	 * @date Apr 29, 2009 4:21:14 PM
	 * @param taskId -任务Id
	 * @param context -JBPM运行上下文
	 * @return 曾经处理过该任务的人员Id
	 * @throws WorkflowException
	 */
	private String getTaskPreActor(String taskId, ExecutionContext context) throws WorkflowException{
		StringBuffer sql=new StringBuffer("select ti.id,ti.actorId from org.jbpm.taskmgmt.exe.TaskInstance ti")
											.append(" where ti.task.id=")
											.append(taskId)
											.append(" and ti.end is not null and ti.processInstance.id=")
											.append(context.getProcessInstance().getId())
											.append(" and ti.actorId is not null ")            			
											.append(" order by ti.id desc");
		List list = context.getJbpmContext().getSession().createQuery(sql.toString()).list();
		if(!list.isEmpty()){ // 若前一处理人存在则按其最初人员分配
			Object[] objs = (Object[])list.get(0);
			//String taskInstanceId = String.valueOf(objs[0]);
			String actorId = String.valueOf(objs[1]);
			//return getProcessService().getDelegationIdAndOriUser(
			//				new Long(taskInstanceId),actorId,"assigned")[1];
			return actorId;
		}
		return null;
	}
	
	//给流程任务设置任务处理用户
	//2010-03-03：增加当该节点是流程第一个任务节点并且该流程是子流程时，到父流程的流程变量中查询其动态处理人功能
	public void assign(Assignable assignable, ExecutionContext context) throws WorkflowException {
			String[] activeActor = null;
			TaskInstance taskinstance = context.getTaskInstance();
			Task task=taskinstance.getTask();
			TaskNode tasknode = task.getTaskNode();
			Token token = context.getToken();
			
			Object[] nodeInfo = this.getNodeInfo(new Long(tasknode.getId()));
			/** 添加冗余数据 */
			taskinstance.setNodeId(tasknode.getId());//设置节点Id
			taskinstance.setNodeName(tasknode.getName());//设置节点名称
			if("0".equals(nodeInfo[6])){//节点挂接的不是主表单
				taskinstance.setFormId(String.valueOf(nodeInfo[5]));
			}else if("1".equals(nodeInfo[6])){//节点挂接的是主表单
				taskinstance.setFormId(token.getProcessInstance().getMainFormId());
			}
			
			String isActiveactor = String.valueOf(nodeInfo[1]);
			int maxTaskActors = 1;
			if(nodeInfo[4] != null){
				maxTaskActors = Integer.parseInt(String.valueOf(nodeInfo[4]));
			}
			
			if("1".equals(isActiveactor)){
				String taskActors = String.valueOf(token.getProcessInstance().getContextInstance()
						.getVariable(WorkflowConst.WORKFLOW_TASK_ACTIVEACTOR + "_" + tasknode.getId()));
				if("null".equals(taskActors)){
					if(context.getProcessDefinition().getStartState().getDefaultLeavingTransition().getTo()
							.getId() == context.getNode().getId()){//在第一个任务节点上
						Object supProcessInstanceId = context.getProcessInstance().getContextInstance()
									.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);
						if(supProcessInstanceId != null){//该流程是子流程
							ProcessInstance supProcessInstance = context.getJbpmContext().getProcessInstance(
									Long.valueOf(supProcessInstanceId.toString()));
							
							// 获取父流程中的子流程节点Id
							String subNodeName = (String)context.getProcessInstance().getContextInstance().getVariable(WorkflowConst.WORKFLOW_SUB_SUPNODE);
							Node subNode = supProcessInstance.getProcessDefinition().getNode(subNodeName);
							Long subNodeId = subNode.getId();
							
							taskActors = String.valueOf(supProcessInstance.getContextInstance()
									.getVariable(WorkflowConst.WORKFLOW_TASK_ACTIVEACTOR + "_" + subNodeId));
							supProcessInstance.getContextInstance().deleteVariable(
									WorkflowConst.WORKFLOW_TASK_ACTIVEACTOR + "_" + subNodeId);//删除该流程变量，以防再次生成子流程时混杂
						}
					}
					if("null".equals(taskActors)){
						isActiveactor = "0";
					}
				} 
				if(WorkflowConst.WORKFLOW_TASK_PREACTOR.equals(taskActors)){
					String taskPreActor = getTaskPreActor(String.valueOf(task.getId()), context);
					if(taskPreActor != null){
						activeActor = new String[]{taskPreActor};
						//parseActors.add(taskPreActor);
					}else{ // 若前一处理人不存在则按默认设置执行分配
						isActiveactor = "0";
					}
				}else{
					activeActor = taskActors.split(",");
				}
			}
			if("0".equals(isActiveactor)){
				String taskPreActor = getTaskPreActor(String.valueOf(task.getId()), context);
				if(taskPreActor != null){
					//parseActors.add(taskPreActor);
					activeActor = new String[]{taskPreActor};
				}else{ // 若前一处理人不存在则按默认设置执行分配
					//Set<String> parseActors = new HashSet<String>();
					List<Object[]> lst = getProcessService().getToSelectActorsForTasknode(String.valueOf(context.getNode().getId()), String.valueOf(context.getTaskInstance().getId()));
					
					if(maxTaskActors >= lst.size()){//默认选择人数小于规定的最大人数
						maxTaskActors = lst.size();
					}
					activeActor = new String[maxTaskActors];
					for(int i = 0; i < maxTaskActors; i++){
						activeActor[i] = String.valueOf(lst.get(i)[0]);
					}
				}
			}
			
			for(int i = 0; i < activeActor.length; i++){
				
				activeActor[i] = getProcessService().getTaskDelegationUser(new Long(context.getTaskInstance()
							.getId()), activeActor[i], activeActor[i], context.getProcessInstance()
							.getProcessDefinition().getName(), context.getJbpmContext());					
			}
			
			assignable.setPooledActors(activeActor);
			taskinstance.setReAssign(String.valueOf(nodeInfo[2]));
			taskinstance.setReAssignmore(String.valueOf(nodeInfo[3]));
			
			// 增加真实上一步任务的信息
			String truePreTask = String.valueOf(token.getProcessInstance().getContextInstance()
					.getVariableLocally(WorkflowConst.WORKFLOW_TASK_TRUEPRETASK, token));
			if(!"null".equalsIgnoreCase(truePreTask)){
				taskinstance.setTruePreTaskinstance(truePreTask);
			}
			// 去掉任务节点的取回任务列表后的增加功能
			String preTask = String.valueOf(token.getProcessInstance().getContextInstance()
					.getVariableLocally(WorkflowConst.WORKFLOW_TASK_CURRENTINFO, token));
			if(!"null".equals(preTask)){
				taskinstance.setPreTaskInstance(Long.valueOf(preTask));
			}
			String fetchTask = String.valueOf(token.getProcessInstance().getContextInstance()
					.getVariableLocally(WorkflowConst.WORKFLOW_TASK_FETCHINFO, token));
			if(!"null".equals(fetchTask)){
				taskinstance.setFetchTasks(fetchTask);
			}
			
			taskinstance.start();
			//modify end
	}	
}
