/**
 * @copyright  : CopyRright (c) 2012 Jiang Xi Strong Co. Ltd. All right reserved.
 * @description: Register.java
 * @project    : 抚州人社局人才市场系统 ——人才市场招聘网站子系统
 * @jdk        : jdk5.0 
 * @comments   : 描述类功能
 * @create     :徐淑华&xush xush@strongit.com.cn
 * @modify     : new
 * @version    : V1.0 
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 11, 2013 11:04:06 AM  
 */
package com.strongit.oa.autocode.flowhandler;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.strongmvc.service.ServiceLocator;

/**
 * @description: 登记时自动处理类
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 11, 2013 11:04:06 AM
 * @version    : v1.0
 * @since  
 */
public class RegisterAction implements ActionHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    
    private static final long serialVersionUID = 3025626755874014836L;

    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext arg0) throws Exception {
    	FlowHandlerManager manager = (FlowHandlerManager)ServiceLocator.getService("flowHandlerManager");
        ProcessInstance processInstance = arg0.getProcessInstance();//获取流程实例对象
        long instanceId = processInstance.getId();  //流程实例ID
        String bussiness = processInstance.getBusinessId(); //表名;主键;主键值
        log.info(bussiness);
        manager.addReport(bussiness, String.valueOf(instanceId));
    }
}