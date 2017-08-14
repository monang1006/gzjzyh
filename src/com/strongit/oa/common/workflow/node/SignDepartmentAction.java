package com.strongit.oa.common.workflow.node;

import java.util.List;
import java.util.Map;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongmvc.service.ServiceLocator;

/**
 * 动态设置签收信息
 * 
 * @author qibh
 * @company Strongit Ltd. (C) copyright
 * @date 2, 4, 2012
 * @classpath com.strongit.oa.common.workflow.node.SignInAction
 * @version 5.0
 */
public class SignDepartmentAction implements ActionHandler {

	Logger LOG = LoggerFactory.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 3025626755874014836L;

	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext executionContext) throws Exception {
		ActionContext cxt = ActionContext.getContext();
		if(cxt == null){
			return ;
		}
		Map<String, Object> session = cxt.getSession();
		//获取签收信息
		String recOrg = (String)session.get("recOrg");
		//获取签收迁移线信息
		SendDocManager manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
		ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
		long instanceId = processInstance.getId();	//流程实例ID
		List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(String.valueOf(instanceId));
		if(ret!=null&&ret.size()>0){
			Object[] obj = ret.get(0);
			String bussinessId = obj[1].toString();		//表名;主键;主键值
			if(bussinessId.indexOf(";")==-1){
				bussinessId="T_OARECVDOC"+";"+"OARECVDOCID"+";"+bussinessId;
			}
			if(recOrg != null) { 
				if(recOrg.equals("签收到主办人员")){
					manager.updateRegdocDateAndBack(bussinessId,"",recOrg,null,"0");
				}else{
					manager.updateRegdocDate(bussinessId,"",recOrg,null,"0");
				}
				LOG.info("登记分发业务表的签收部门字段成功！");			
			}else{
				manager.updateRegdocDateAndBack(bussinessId,"",recOrg,null,"0");//退回时更新签收人员信息
				LOG.info("登记分发业务表的签收人员字段修改成功！");	
			}
		}else{
			String bussinessId = processInstance.getBusinessId();		//表名;主键;主键值
			if(bussinessId.indexOf(";")==-1){
				bussinessId="T_OARECVDOC"+";"+"OARECVDOCID"+";"+bussinessId;
			}
			if(recOrg != null) { 
				manager.updateRegdocDate(bussinessId,"",recOrg,null,"0");
				LOG.info("登记分发业务表的签收部门字段成功！");			
			}
		}
		session.remove("recOrg");
	}

}
