package com.strongit.oa.common.workflow.ipp;

import static com.strongit.oa.common.workflow.WorkFlowTypeConst.BSYSHBTG;
import static com.strongit.oa.common.workflow.WorkFlowTypeConst.BSYSHTG;
import static com.strongit.oa.common.workflow.WorkFlowTypeConst.KSLDSHBTG;
import static com.strongit.oa.common.workflow.WorkFlowTypeConst.KSLDSHTG;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;

import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.webservice.client.ipp.Client;
import com.strongit.oa.work.WorkManager;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongmvc.service.ServiceLocator;

/**
 * 自动节点返回数据给IPP
 * <P>修改IPP表单业务数据状态以及反馈审批意见</P>
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-7-15 上午11:25:10
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.workflow.ipp.WorkHandler
 * @comment
 * @email dengzc@strongit.com.cn
 */
@SuppressWarnings("serial")
public class WorkHandler implements ActionHandler {

	private WorkManager manager;//工作处理管理对象
	private IsmsService smsManager;//手机短信接口
	

	public WorkHandler(){
		manager = (WorkManager)ServiceLocator.getService("workManager");
		smsManager = (IsmsService)ServiceLocator.getService("smsService");
	}

	/**
	 * OA反馈IPP的自动节点调用
	 */
	public void execute(ExecutionContext executionContext) throws Exception {
		long processInstanceId = executionContext.getContextInstance().getProcessInstance().getId();
		
		String instanceId = String.valueOf(processInstanceId);
		List<Object[]> ret = manager.getFormIdAndBusinessIdByProcessInstanceId(instanceId);
		Object[] obj = ret.get(0);
		String bussinessId = obj[1].toString();
		String id = bussinessId.split(";")[2];
		String[] data = manager.getFeedBackCode(id);
		String feedBackCode = data[0];//咨询反馈号
		String mobile = data[1];//手机号码
		//获取所有处理意见
		List lst = manager.getApproveInfosByPIId(String.valueOf(processInstanceId)); 
		LogPrintStackUtil.logInfo("审批意见："+lst);
		String suggestion = "";
		//如果有审批意见,则获取最后一个
		if(lst != null && lst.size()>0){
			TwfInfoApproveinfo info = (TwfInfoApproveinfo)lst.get(lst.size()-1);
			String objSuggestion = (String)info.getAiContent();
			if(objSuggestion!=null && !"".equals(objSuggestion)){
				suggestion = objSuggestion;
			}
		}
		Token token = executionContext.getToken();
		Node node = token.getNode();
		if(node != null){
			String nodeName = node.getName();
			if(nodeName != null){
				nodeName = nodeName.trim();
			}
			if(BSYSHTG.equals(nodeName)){//办事员审核通过
				if("".equals(suggestion)){
					suggestion = "办事员审核通过";
				}
				Client.archiveIPP(feedBackCode,"1", suggestion);
				smsManager.sendSmsByNum(mobile, suggestion, "OA",GlobalBaseData.SMSCODE_WORKFLOW);
				token.signal();
			}else if(BSYSHBTG.equals(nodeName)){//办事员审核不通过
				Client.archiveIPP(feedBackCode,"2", suggestion);
				smsManager.sendSmsByNum(mobile, suggestion, "OA",GlobalBaseData.SMSCODE_WORKFLOW);
				token.signal();
			}else if(KSLDSHTG.equals(nodeName)){//科室领导审批通过
				if("".equals(suggestion)){
					suggestion = "科室领导审批通过";
				}
				Client.archiveIPP(feedBackCode,"2", suggestion);
				smsManager.sendSmsByNum(mobile, suggestion, "OA",GlobalBaseData.SMSCODE_WORKFLOW);
				token.signal();
			}else if(KSLDSHBTG.equals(nodeName)){//科室领导审批不通过
				Client.archiveIPP(feedBackCode,"2", suggestion);
				smsManager.sendSmsByNum(mobile, "科室领导审批不通过", "OA",GlobalBaseData.SMSCODE_WORKFLOW);
				token.signal();
			}else{//自动节点设置错误
				
			}
			
		}

		/*try{
			smsManager.sendSmsByNum(mobile, suggestion, "OA",GlobalBaseData.SMSCODE_WORKFLOW);
			LogPrintStackUtil.logInfo("向IPP“"+mobile+"”发送短信成功，短信内容：" + suggestion);
		}catch(Exception e){
			LogPrintStackUtil.logExceptionInfo(e, "向IPP发送短信异常！");
		}*/
		
	}

}
