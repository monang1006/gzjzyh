package com.strongit.oa.docdis.common; 

import org.apache.log4j.Logger;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.docdis.DocDisManager;
import com.strongmvc.service.ServiceLocator;

/**
 * <p>Title: MyWorkAutoRun.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 将完成的公文进行自动分发前将数据内容转存至待分发公文模块
 * @date 	 2009-11-09 16:16:56
 * @version  1.0
 */
public class MyWorkAutoRun implements ActionHandler{

	private static final long serialVersionUID = -680651035173937393L;

	private static final Logger log = Logger.getLogger(MyWorkAutoRun.class);
	
	private DocDisManager manager;
	
	public MyWorkAutoRun(){
		manager = (DocDisManager)ServiceLocator.getService("docDisManager");
	}
	
	public void execute(ExecutionContext myContext) throws Exception {
		// TODO Auto-generated method stub
		long instanceId = myContext.getProcessInstance().getId();
		String processInstanceId = String.valueOf(instanceId);
		manager.copyDocToDocDis(processInstanceId);
		log.info("我是公文分发的自动节点");
		myContext.getToken().signal();
	}

}
