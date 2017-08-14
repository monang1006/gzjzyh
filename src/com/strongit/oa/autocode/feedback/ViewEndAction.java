/**
 * @copyright  : CopyRright (c) 2012 Jiang Xi Strong Co. Ltd. All right reserved.
 * @description: ViewStartAction.java
 * @jdk        : jdk5.0 
 * @comments   : 描述类功能
 * @create     :徐淑华&xush xush@strongit.com.cn
 * @modify     : new
 * @version    : V1.0 
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 22, 2013 7:58:06 PM  
 */
package com.strongit.oa.autocode.feedback;

import java.util.Date;
import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongmvc.service.ServiceLocator;

/**
 * @description: 反馈意见开始处理类
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 22, 2013 7:58:06 PM
 * @version    : v1.0
 * @since  
 */
public class ViewEndAction implements ActionHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 
     */
    private static final long serialVersionUID = 3025626755874014836L;

    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext arg0) throws Exception {
        IWorkflowService workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
        FeedBackManager manager = (FeedBackManager)ServiceLocator.getService("feedBackManager");
        ProcessInstance processInstance = arg0.getProcessInstance();//获取流程实例对象
        long instanceId = processInstance.getId();  //流程实例ID
//        String bussiness1 = processInstance.getBusinessId(); //表名;主键;主键值
        String firstParentPid = "";
        if (String.valueOf(instanceId) != null && !String.valueOf(instanceId).equals("")) {
        	//获取当前流程的所有父流程实例信息
            List<Object[]>  supProcessInstanceInfos = workflowService.getSupProcessInstanceInfos(instanceId+"");
            if(supProcessInstanceInfos == null || supProcessInstanceInfos.isEmpty()){//如果当前流程不存在父流程，则当前流程是最顶级流程
                firstParentPid = String.valueOf(instanceId);
            }else{//如果当前流程存在父流程，则取最顶级父流程信息
                firstParentPid = supProcessInstanceInfos.get(0)[0].toString();
            }
        }
        ProcessInstance pp= workflowService.getProcessInstanceById(firstParentPid);
        String bussiness = pp.getBusinessId(); //表名;主键;主键值
        //log.info(bussiness);
        //manager.addEndTime(bussiness,bussiness1, firstParentPid);
        //处理意见反馈信息
        //得到所有单位的处理信息
        List<TwfInfoApproveinfo> appinfos = workflowService.getApproveInfosByPIId(instanceId+"");
        //得到规定反馈时限（多少个工作日）
        int wordDays= manager.getYjzxDays(bussiness);
        //得到征询意见开始时间
        Date feedBackTime = manager.getFeedbackTime(bussiness);
        //循环每个处理信息，得到反馈部门、实际反馈用时、是否超期等信息
		for(int i=0;i<appinfos.size();i++){
			TwfInfoApproveinfo info = appinfos.get(i);
			if(info.getAiTaskname().indexOf("转办")>-1){
				Date handledDate = info.getAiDate();	//反馈时间
				String handledDept = info.getAiActor();	//反馈部门
				//计算实际反馈用时（工作日）
				int handleworkDays  = manager.getWorkDaysBetweenStartDateAndEndDate(feedBackTime, handledDate);
				int overDays=0;
				//计算是否超时
				if(handleworkDays>wordDays){
					overDays=handleworkDays-wordDays; 
				}
			 manager.insertData(handledDept,wordDays,handleworkDays,overDays,bussiness);
			}
		}
    }
}