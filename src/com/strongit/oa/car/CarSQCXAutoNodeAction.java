package com.strongit.oa.car;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.bo.ToaCarApplicant;
import com.strongit.oa.car.CarApplyManager;
import com.strongmvc.service.ServiceLocator;

/*
 * Description：车辆申请自动节点设置车辆申请为已撤销状态
 */
public class CarSQCXAutoNodeAction implements ActionHandler {
	private CarApplyManager service = null;
	private CarApplyManager getService(){
		if(service == null){
			service = (CarApplyManager)ServiceLocator.getService("carApplyManager");
		}
		return service;
	}
	
	public void execute(ExecutionContext arg0) throws Exception {
		long instanceId = arg0.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		//郑志斌，service改成getService()
		List<Object[]> ret = getService().getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String applicantId = bussinessId.split(";")[2];
		ToaCarApplicant model = this.getService().getCarApplyInfo(applicantId);
		model.setApplystatus("4"); //将状态置为已撤销
		service.saveCarApplyInfo(model);
		arg0.getToken().unlock("token[" + arg0.getToken().getId() + "]");
		arg0.getToken().signal();
	}
}