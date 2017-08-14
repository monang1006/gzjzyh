package com.strongit.oa.common.workflow.node;

import java.util.Set;

import net.sf.json.JSONObject;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.workflow.helper.AutoGoToNext;
import com.strongit.workflow.util.WorkflowConst;

/**
 * 流程自动提交下去
 * @author 		邓志城
 * @date   		Aug 5, 2011
 * @classpath	com.strongit.oa.common.workflow.node.AutoGoToNextAction
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public class AutoGoToNextAction implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6985477401243985434L;
	
	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext executionContext) throws Exception {
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		TaskInstance ti = executionContext.getTaskInstance();
		String taskId = String.valueOf(ti.getId());
		String userId = ti.getActorId();						//当前任务处理人
		Set<PooledActor> set = ti.getPooledActors();
		if(set != null && !set.isEmpty()) {
			userId = set.iterator().next().getActorId();
		} else {
			userId = "402882282262726001226289c8cb0001";
		}
		String businessName = String.valueOf(processInstance.getContextInstance().getVariable(WorkflowConst.WORKFLOW_BUSINESSNAME));
		StringBuilder sugggestion = new StringBuilder("流程["+businessName+"]");
		sugggestion.append("自动提交到下一环节.");
		JSONObject json = new JSONObject();
		json.put("suggestion", sugggestion.toString());
		json.put("CAInfo", "");
		AutoGoToNext auto = new AutoGoToNext(taskId,userId,json.toString());
		new Thread(auto).start();
	}

}
