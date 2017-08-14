package com.strongit.oa.exchange;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongmvc.service.ServiceLocator;

/**
 * 处理 公文归档
 * @Create Date: 2009-3-11
 * @author luosy
 * @version 1.0
 */
public class SaveTempFileAction implements ActionHandler {

	
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

//		columnArticleId:-->T_OA_SENDDOC;SENDDOC_ID;4028822d1fd99dc9011fd9eb09b10001
		//this.getService().saveFileToArch(columnArticleId);
		
		
		arg0.getToken().unlock("token[" + arg0.getToken().getId() + "]");
		arg0.getToken().signal();
	}
}
