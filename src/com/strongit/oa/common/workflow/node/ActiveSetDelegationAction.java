package com.strongit.oa.common.workflow.node;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.service.ServiceLocator;

/**
 * 动态设置流程委托,条件取决于用户在个人信息中设置了“主办人员代办”
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date Dec 7, 2011
 * @classpath com.strongit.oa.common.workflow.node.ActiveSetDelegationAction
 * @version 5.0
 * @email dengzc@strongit.com.cn
 * @tel 0791-8186916
 */
public class ActiveSetDelegationAction implements ActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3025626755874014836L;

	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext cxt) throws Exception {
		Object object = cxt.getProcessInstance().getContextInstance()
				.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);
		Collection taskinstances = cxt.getTaskMgmtInstance().getTaskInstances();
		if (object == null && taskinstances.size() == 1) {
			//return;
		}
		TaskInstance taskInstance = cxt.getTaskInstance();
		if (taskInstance == null) {
			return;
		}
		Set<PooledActor> actorSet = taskInstance.getPooledActors();
		if (!actorSet.isEmpty() && actorSet.size() == 1) {//只有一个人处理任务
			MyInfoManager manager = (MyInfoManager) ServiceLocator
					.getService("myInfoManager");// 得到个人信息服务类
			IUserService userService = (IUserService)ServiceLocator.getService("userService");
			try {
				User user = userService.getCurrentUser();
				IWorkflowService workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
				String taskInstanceId = String.valueOf(taskInstance.getId());//得到任务实例id
				for(Iterator<PooledActor> it=actorSet.iterator();it.hasNext();){
					String userId = it.next().getActorId();
					ToaPersonalInfo entry = manager.getInfoByUserid(userId);
					if(entry != null) {
						String flag = entry.getMailnum();//得到委托设置
						if("3".equals(flag)) {
							//主办人员代办
							workflowService.delegateTaskInstance(taskInstanceId, userId ,user.getUserId());
						}						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
