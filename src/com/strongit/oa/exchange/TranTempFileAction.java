package com.strongit.oa.exchange;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongmvc.service.ServiceLocator;

public class TranTempFileAction implements ActionHandler {

	
	private ExchangeFileManager service = null;
	
	private ExchangeFileManager getService(){
		if(service == null){
			service = (ExchangeFileManager)ServiceLocator.getService("exchangeFileManager");
		}
		return service;
	}
	
	public void execute(ExecutionContext arg0) throws Exception {
		
		long instanceId = arg0.getContextInstance().getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		//String columnArticleId = this.getProcessService().getBusinessIdByProcessInstanceId(processInstanceId);

		//this.getService().tranFile(columnArticleId);
		
		arg0.getToken().unlock("token[" + arg0.getToken().getId() + "]");
		arg0.getToken().signal();
	}
}
