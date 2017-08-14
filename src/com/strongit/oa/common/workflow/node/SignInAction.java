package com.strongit.oa.common.workflow.node;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
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
public class SignInAction implements ActionHandler {

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
		String rcv= (String)session.get("recUser");
		if(rcv != null && !"".equals(rcv)){//流程退回到该节点时，rcv为空 yanjian 2012-02-12 16:39
			int ifind=rcv.indexOf("|");
			if(ifind!=-1){
				rcv=rcv.substring(0, ifind);
			}
			String userId =rcv;
			//获取签收单位
			SendDocManager manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
			ProcessInstance processInstance = executionContext.getProcessInstance();//获取流程实例对象
			long instanceId = processInstance.getId();	//流程实例ID
			System.out.println();
			Date date = new Date();
			IUserService userService = (IUserService)ServiceLocator.getService("userService");
			User user = userService.getUserInfoByUserId(userId);
			String orgName = "";
			try {
				orgName = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
			} catch (Exception e) {
				LOG.error("获取当前用户信息失败.");
			}
			List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(String.valueOf(instanceId));
			if(ret!=null&&ret.size()>0){
				
				Object[] obj = ret.get(0);
				String bussinessId = obj[1].toString();		//表名;主键;主键值
				if(bussinessId.indexOf(";")==-1){
					bussinessId="T_OARECVDOC"+";"+"OARECVDOCID"+";"+bussinessId;
				}
				if(user != null) { 
					manager.updateRegdocDate(bussinessId,user.getUserName(),orgName,date,"1");
					LOG.info("登记分发业务表的签收人、签收部门、签收时间、签收状态字段成功！");			
				}
			}else{
				String bussinessId = processInstance.getBusinessId();		//表名;主键;主键值
				if(bussinessId.indexOf(";")==-1){
					bussinessId="T_OARECVDOC"+";"+"OARECVDOCID"+";"+bussinessId;
				}
				if(user != null) { 
					manager.updateRegdocDate(bussinessId,user.getUserName(),orgName,date,"1");
					LOG.info("登记分发业务表的签收人、签收部门、签收时间、签收状态字段成功！");			
				}
			}
			session.remove("recUser");
		}
	}

}
