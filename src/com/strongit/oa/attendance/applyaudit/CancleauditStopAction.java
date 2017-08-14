package com.strongit.oa.attendance.applyaudit;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.attendance.apply.ApplyManager;
import com.strongit.oa.bo.ToaAttendCancle;
import com.strongmvc.service.ServiceLocator;
/**
 * 销假申请单自动节点  销假流程走完后调用该方法改变申请单状态
 * @author Administrator
 *
 */
public class CancleauditStopAction implements ActionHandler{
	private ApplyManager service = null;
	
	public CancleauditStopAction(){
		service = (ApplyManager)ServiceLocator.getService("applyManager");
	}
	
	public void execute(ExecutionContext arg0) throws Exception {
		long instanceId = arg0.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		List<Object[]> ret = service.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String cancleid = bussinessId.split(";")[2];
		ToaAttendCancle model=service.getToaAttendCancle(cancleid);
		model.setCancleState("2");//将销假状态置为已审核
		service.updateAttendCancle(model);
		arg0.getToken().unlock("token[" + arg0.getToken().getId() + "]");
		arg0.getToken().signal();
	}

}
