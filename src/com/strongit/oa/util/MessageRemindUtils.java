package com.strongit.oa.util;

import java.util.Set;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.opensymphony.xwork2.ActionContext;

/**
 * 消息提醒辅助类
 * @author 邓志城
 *
 */
public class MessageRemindUtils implements ActionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1968319656541986954L;

	public void execute(ExecutionContext context) throws Exception {
		TaskInstance taskInstance = context.getTaskInstance();
		Set actorSet = taskInstance.getPooledActors();
		ActionContext cxt = ActionContext.getContext();
		LogPrintStackUtil.logInfo(cxt.getSession().get("msg").toString());
	}

}
