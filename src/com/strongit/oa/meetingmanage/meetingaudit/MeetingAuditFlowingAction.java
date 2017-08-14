package com.strongit.oa.meetingmanage.meetingaudit;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.meetingmanage.meetingtopic.MeetingtopicManager;
import com.strongmvc.service.ServiceLocator;
/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-24 下午04:09:26
 * Autour: JGB
 * Version: V1.0 
 * Description：会议审核自动节点设置议题为审核中状态
 */
public class MeetingAuditFlowingAction implements ActionHandler {
	
	private MeetingtopicManager service = null;
	private MeetingtopicManager getService(){
		if(service == null){
			service = (MeetingtopicManager)ServiceLocator.getService("meetingtopicManager");
		}
		return service;
	}
	
	public void execute(ExecutionContext arg0) throws Exception {
		long instanceId = arg0.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		//String meetingId = service.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		List<Object[]> ret = getService().getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
		Object[] obj = ret.get(0);
		String meetingId = obj[1].toString();
		ToaMeetingTopic topic=this.getService().getoneTopic(meetingId);
	       topic.setTopicStatus("1");//将状态置为审核中状态
		service.updateTopic(topic);
		//arg0.getToken().unlock("token[" + arg0.getToken().getId() + "]");
		arg0.getToken().signal();
	}

}



