package com.strongit.oa.attendance.gather;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.attendance.apply.ApplyManager;
import com.strongit.oa.bo.ToaAttendCancle;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.service.ServiceLocator;
/**
 * 考勤汇总自动节点代理类
 * @author 胡丽丽
 * @date 2010-03-23
 */
public class GatherStopAction implements ActionHandler {

	private GatherManager manager=null;
	public GatherStopAction(){
		manager = (GatherManager)ServiceLocator.getService("gatherManager");
	}
	public void execute(ExecutionContext arg0) throws Exception {
		long instanceId = arg0.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		manager.updateGatherAutistate(bussinessId, "2", new OALogInfo("审核考勤汇总记录完毕"));
		arg0.getToken().unlock("token[" + arg0.getToken().getId() + "]");
		arg0.getToken().signal();
	}

}
