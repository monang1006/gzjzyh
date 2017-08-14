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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.autocode.feedback.FeedBackManager;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongmvc.service.ServiceLocator;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @description: 进入领导发文流程类
 * @author :徐淑华&xush xush@strongit.com.cn
 * @date : 5/8/2013 7:59 PM
 * @version : v1.0
 * @since
 */
public class LeaderSendAction
        implements ActionHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 3025626755874014836L;

    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext arg0) throws Exception {
        ToDoManager manager = (ToDoManager)ServiceLocator.getService("toDoManager");
        AdapterBaseWorkflowManager adapterBaseWorkflowManager = (AdapterBaseWorkflowManager)ServiceLocator.getService("adapterBaseWorkflowManager");
        IWorkflowService workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
        ProcessInstance processInstance = arg0.getProcessInstance();//获取流程实例对象
        FeedBackManager feedBackmanager = (FeedBackManager)ServiceLocator.getService("feedBackManager");
        long instanceId = processInstance.getId();  //流程实例ID
       
        //得到所有领导审批信息
        List<Object[]> task = workflowService.getProcessHandlesAndNodeSettingByPiId(instanceId+"");
        List<Object[]> task2 =new ArrayList<Object[]>();
        Date startTime=null;
        Date endTime=null;
        for(Object[] task1:task){
            if(task1[1].toString().indexOf("审签")>-1||task1[1].toString().indexOf("签发")>-1||task1[1].toString().indexOf("审核")>-1
                    ||task1[1].toString().indexOf("林书记核稿")>-1||task1[1].toString().indexOf("复审")>-1){
               task2.add(task1);
               }
        }
        if(!task2.isEmpty()&&task2.size()>0){
            Collections.sort(task2, new Comparator<Object[]>() {
                    public int compare(Object[] arg0, Object[] arg1) {
                        Long l1 = Long.MAX_VALUE;
                        Long l2 = Long.MAX_VALUE;
                        if (arg0[3] != null) {
                            l1 = new Long(((Date) arg0[3]).getTime());
                        }
                        if (arg1[3] != null) {
                            l2 = new Long(((Date) arg1[3]).getTime());
                        }
                        return l1.compareTo(l2);
                    }
                });
           }
        startTime=(Date)task2.get(0)[2];
        endTime=(Date)task2.get(task2.size()-1)[3];
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
        SimpleDateFormat  fmt =new SimpleDateFormat("MM.dd");
        int leaderworkDays=0;
        String fwLeaderStartTime="0天";
        Date receiveTime=manager.getReceive(bussiness1);
        int finishPrior=0;
        if(startTime!=null&&endTime!=null){
      //计算处室用时（工作日）
         leaderworkDays  = feedBackmanager.getWorkDaysBetweenStartDateAndEndDate(startTime, endTime);
         fwLeaderStartTime=fmt.format(startTime)+"-"+fmt.format(endTime)+","+leaderworkDays+"天";
        }
        if(endTime!=null){
            finishPrior = feedBackmanager.getWorkDaysBetweenStartDateAndEndDate(receiveTime, endTime);
        }
         manager.insertTime(endTime,bussiness);
         manager.insertLeaderSend(fwLeaderStartTime,finishPrior,bussiness,bussiness1);
      }
}