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
package com.strongit.oa.autocode.flowhandler;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongmvc.service.ServiceLocator;

/**
 * @description: 退文时修改标志位
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 11, 2013 5:12:15 PM
 * @version    : v1.0
 * @since  
 */
public class ReturnAction implements ActionHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 3025626755874014836L;

    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext arg0) throws Exception {
        FlowHandlerManager manager = (FlowHandlerManager)ServiceLocator.getService("flowHandlerManager");
        ProcessInstance processInstance = arg0.getProcessInstance();//获取流程实例对象
        long instanceId = processInstance.getId();  //流程实例ID
        String bussiness = processInstance.getBusinessId(); //表名;主键;主键值
        System.out.println(bussiness);
        manager.updateRturnFlg(bussiness, String.valueOf(instanceId));
    }
}