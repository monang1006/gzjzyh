/**
 * @copyright  : CopyRright (c) 2012 Jiang Xi Strong Co. Ltd. All right reserved.
 * @description: ReturnAction.java
 * @project    : 抚州人社局人才市场系统 ——人才市场招聘网站子系统
 * @jdk        : jdk5.0 
 * @comments   : 描述类功能
 * @create     :徐淑华&xush xush@strongit.com.cn
 * @modify     : new
 * @version    : V1.0 
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 11, 2013 5:12:15 PM  
 */
package com.strongit.oa.autocode.gongwen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.autocode.feedback.FeedBackManager;
import com.strongit.oa.autocode.flowhandler.FlowHandlerManager;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongmvc.service.ServiceLocator;

/**
 * @description: 进入处室流程类
 * @author :徐淑华&xush xush@strongit.com.cn
 * @date : 5/8/2013 7:59 PM
 * @version : v1.0
 * @since
 */
public class DeptSignAction
        implements ActionHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 
     */
    private static final long serialVersionUID = 3025626755874014836L;

    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext arg0) throws Exception {
        ToDoManager manager = (ToDoManager)ServiceLocator.getService("toDoManager");
        AdapterBaseWorkflowManager adapterBaseWorkflowManager = (AdapterBaseWorkflowManager)ServiceLocator.getService("adapterBaseWorkflowManager");
        IWorkflowService workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
        ProcessInstance processInstance = arg0.getProcessInstance();//获取流程实例对象
        FeedBackManager feedBackmanager = (FeedBackManager)ServiceLocator.getService("feedBackManager");
        long instanceId = processInstance.getId();  //流程实例ID
        String bussiness = processInstance.getBusinessId(); //表名;主键;主键值
        System.out.println(bussiness);
        String firstParentPid = "";
        if (String.valueOf(instanceId) != null && !String.valueOf(instanceId).equals("")) {
            
//          获取当前流程的所有父流程实例信息
            List<Object[]>  supProcessInstanceInfos = workflowService.getSupProcessInstanceInfos(instanceId+"");
//          List<Object[]> parentPList = (List<Object[]>) workflowService.getMonitorParentInstanceIds(new Long(instanceId));
            
            if(supProcessInstanceInfos == null || supProcessInstanceInfos.isEmpty()){//如果当前流程不存在父流程，则当前流程是最顶级流程
                firstParentPid = String.valueOf(instanceId);
            }else{//如果当前流程存在父流程，则取最顶级父流程信息
                firstParentPid = supProcessInstanceInfos.get(0)[0].toString();
            }
        }
        ProcessInstance pp= workflowService.getProcessInstanceById(firstParentPid);
        String bussiness1 = pp.getBusinessId(); //表名;主键;主键值
        Date departStartTime = manager.getDeptStartTime(bussiness1);//获取处室开始时间
        Date now = new Date();
        SimpleDateFormat  fmt =new SimpleDateFormat("MM.dd");
       //计算处室用时（工作日）
        if(departStartTime!=null){
        int deptworkDays  = feedBackmanager.getWorkDaysBetweenStartDateAndEndDate(departStartTime, now);
        String gwDeptStartTime=fmt.format(departStartTime)+"-"+fmt.format(now)+","+deptworkDays+"天";
        manager.insertDeptSign(gwDeptStartTime,bussiness1);}
     }

}
