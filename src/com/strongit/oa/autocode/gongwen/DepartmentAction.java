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

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongmvc.service.ServiceLocator;

/**
 * @description: 进入处室流程类
 * @author :徐淑华&xush xush@strongit.com.cn
 * @date : 5/8/2013 7:59 PM
 * @version : v1.0
 * @since
 */
public class DepartmentAction implements ActionHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 3025626755874014836L;

    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext arg0) throws Exception {
        ToDoManager manager = (ToDoManager)ServiceLocator.getService("toDoManager");
        AdapterBaseWorkflowManager adapterBaseWorkflowManager = (AdapterBaseWorkflowManager)ServiceLocator.getService("adapterBaseWorkflowManager");
        IWorkflowService workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
        ProcessInstance processInstance = arg0.getProcessInstance();//获取流程实例对象
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
        log.info(bussiness1);
        /*List<Object[]> 流程父流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务*/
       // List<Object[]> supList = workflowService.getSupProcessInstanceInfos(instanceId+"");
        User user = adapterBaseWorkflowManager.getUserService().getCurrentUser();// 得到当前用户
        String orgId = user.getOrgId();
        Organization org = adapterBaseWorkflowManager.getUserService().getDepartmentByOrgId(orgId);
        String department = org.getOrgName();
        manager.insertDept(department,bussiness1);
    }
}