package com.strongit.oa.common.workflow.node;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.doc.sends.ToTransDocManager;
import com.strongmvc.service.ServiceLocator;

/**
 * 发文登记自动处理类
 * @author xielei
 *
 */
public class TransDocAction implements ActionHandler{
	
	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 3025626755874014836L;
	
	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext executionContext) throws Exception {
		//获取签收信息
		ToTransDocManager toTransDocManager = (ToTransDocManager)ServiceLocator.getService("toTransDocManager");
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		long instanceId = processInstance.getId();	//流程实例ID
		String bussiness = processInstance.getBusinessId();	//表名;主键;主键值      当前流程
//		List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(String.valueOf(instanceId));
//		if(ret!=null&&ret.size()>0){
//			Object[] obj = ret.get(0);
//			String bussiness = obj[1].toString();		//表名;主键;主键值
//			System.out.println("bussiness ========================== "+bussiness);
//			manager.registModel(bussiness, String.valueOf(instanceId));
//		}		
		toTransDocManager.registModel(bussiness, String.valueOf(instanceId));		
	}
}
