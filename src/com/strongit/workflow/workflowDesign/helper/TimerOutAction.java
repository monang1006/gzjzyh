package com.strongit.workflow.workflowDesign.helper;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.job.Timer;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.strongit.oa.common.workflow.helper.AutoBackLast;
import com.strongit.oa.common.workflow.helper.AutoMustFetchTask;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.po.DueTimerBean;
import com.strongit.workflow.po.ProcessInstanceBean;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.service.ServiceLocator;

/**
 * 
 * @company      Strongit Ltd. (C) copyright
 * @date         May 31, 2012 10:01:16 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.workflowDesign.helper.TimerOutAction
 */
public class TimerOutAction implements ActionHandler {
	
	/**
	 * @field  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private ITaskService taskService= null;
	
	private ITaskService getTaskService(){
		if(taskService == null){
			taskService = (ITaskService)ServiceLocator.getService("taskService");
		}
		return taskService;
	}
	
	private IProcessInstanceService processService= null;
	
	private IProcessInstanceService getProcessService(){
		if(processService == null){
			processService = (IProcessInstanceService)ServiceLocator.getService("processInstanceService");
		}
		return processService;
	}
	
	/**
	 * 获取流程节点上的定时器设置信息
	 * @author  喻斌
	 * @date    Feb 4, 2009  2:16:28 PM
	 * @param nodeId -流程节点Id
	 * @return String 定时器设置信息
	 */
	private TwfBaseNodesetting getTimerSet(Long nodeId){
	    	/*String sql = "select t.nsTaskTimerSet from TwfBaseNodesetting t where t.nsNodeId = ?";
	    	List list = getProcessService().getDataByHql(sql, new Object[]{nodeId});
	    	if(!list.isEmpty()){
	    		return String.valueOf(list.get(0));
	    	}else{
	    		return null;
	    	}*/
		String sql = "from TwfBaseNodesetting t where t.nsNodeId = ?";
    	List list = getProcessService().getDataByHql(sql, new Object[]{nodeId});
    	if(!list.isEmpty()) {
    		return (TwfBaseNodesetting)list.get(0);
    	}
    	return null;
	}

	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext arg0) throws WorkflowException {
		Timer timer = arg0.getTimer();
		TaskInstance ti = timer.getTaskInstance();
		ProcessInstance pi = timer.getProcessInstance();
		String businessName = String.valueOf(pi.getContextInstance()
				.getVariable(WorkflowConst.WORKFLOW_BUSINESSNAME));
		String timerSet = null;
		String isBackLast = null;
		if(ti != null){//启用任务定时器时，才在ti
			TwfBaseNodesetting setting = this.getTimerSet(new Long(ti.getTask().getTaskNode().getId()));
			timerSet = setting.getNsTaskTimerSet();
			isBackLast = setting.getPlugin("plugins_backlast");
		}else{//启用流程定时器时，不存在ti
			TwfBaseNodesetting setting = this.getTimerSet(new Long(timer.getGraphElement().getId()));
			timerSet = setting.getNsTaskTimerSet();
		}
		
		List<String> noticeMethod = new ArrayList<String>();
		List<String> noticeGroup = new ArrayList<String>();
		
        String[] timerSets = timerSet.split(",");
        if("1".equals(timerSets[12])){
        	noticeGroup.add(WorkflowConst.WORKFLOW_URGENCY_GROUP_OWNER);
        }
        if("1".equals(timerSets[11])){
        	noticeGroup.add(WorkflowConst.WORKFLOW_URGENCY_GROUP_STARTOR);
        }
        if("1".equals(timerSets[10])){
        	noticeGroup.add(WorkflowConst.WORKFLOW_URGENCY_GROUP_HANDLER);
        }
        if("1".equals(timerSets[9])){
        	// send rtx
        	noticeMethod.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_RTX);
        }
        if("1".equals(timerSets[8])){
        	// send message
        	noticeMethod.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_MESSAGE);
        }
        if("1".equals(timerSets[7])){
        	// send notice
        	noticeMethod.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE);
        }
        if("1".equals(timerSets[6])){
        	// send mail
        	noticeMethod.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL);
        }
        
        ProcessInstanceBean processInstanceBean = new ProcessInstanceBean();
        processInstanceBean.setProcessInstanceId(pi.getId());
        processInstanceBean.setProcessName(pi.getProcessDefinition().getName());
        if(isBackLast != null && "1".equals(isBackLast)) {
        	processInstanceBean.setBusinessName("《"+businessName+"》超期,系统自动退回给上一环节处理人员。");    
        } else {
        	if(ti != null) {//启用任务定时器时，才在ti
        		if("0".equals(ti.getIsTimeout())){
        			processInstanceBean.setBusinessName("系统提醒：您的待办事项《"+businessName+"》任务即将超期，请尽快办理。");        	
        		} else {
        			processInstanceBean.setBusinessName("系统提醒：您的待办事项《"+businessName+"》任务已超期，请处理。");   
        		}
        	} else {//启用流程定时器时，不存在ti
        		if("0".equals(pi.getIsTimeout())){
        			processInstanceBean.setBusinessName("系统提醒：您有一个待办事项《"+businessName+"》流程即将超期。");    
        		}else{
        			processInstanceBean.setBusinessName("系统提醒：您有一个待办事项《"+businessName+"》流程已超期。");   
        		}
        	}
        }
        
        if(ti != null){
        	if("0".equals(ti.getIsTimeout())){//设置任务实例为超时状态
        		ti.setIsTimeout("1");
        	}
        	if("0".equals(pi.getIsTimeout())){//设置流程实例为超时状态
        		pi.setIsTimeout("1");
        	}
        	
        	getTaskService().urgencyTaskInstanceByTimer(WorkflowConst.WORKFLOW_NOTICE_SYSTEM
        			, processInstanceBean, ti, noticeGroup, noticeMethod, new DueTimerBean());
        	if(isBackLast != null && "1".equals(isBackLast)) {//需要退回上一办理人员
        		//任务超时后自动退回上一办理人 严建 2012-05-26 14:44
        		AutoBackLast bean = new AutoBackLast(ti);
        		new Thread(bean).start();
        	}
        }else{
        	if("0".equals(pi.getIsTimeout())){//设置流程实例为超时状态
        		pi.setIsTimeout("1");
        	}
        	//任务超期制动取回
        	AutoMustFetchTask bean = new AutoMustFetchTask(
        			pi.getId()+"");
			new Thread(bean).start();
        	getProcessService().urgencyProcessByTimer(WorkflowConst.WORKFLOW_NOTICE_SYSTEM
        			, processInstanceBean, pi, noticeGroup, noticeMethod, new DueTimerBean());
        }
	}
}
