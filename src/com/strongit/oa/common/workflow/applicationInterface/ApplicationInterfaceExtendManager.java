package com.strongit.oa.common.workflow.applicationInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TProcessUrgency;
import com.strongit.oa.common.business.processurgency.ProcessUrgencyManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.util.WorkflowConst;

/**
 * 扩展com.strongit.workflow.applicationInterface.ApplicationInterface
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 15, 2012 9:16:00 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.applicationInterface.ApplicationInterfaceExtendManager
 */
@Service
@Transactional
@OALogger
public class ApplicationInterfaceExtendManager {
	@Autowired
	private AutoFetchTaskManager autoFetchTaskManager;// 注入工作流服务类

	@Autowired
	private ProcessUrgencyManager processUrgencyManager;// 注入工作流服务类
	@Autowired
	private IUserService userService;

	/**
	 * 启动强制取回
	 * 
	 * @description
	 * @author 严建
	 * @param pid
	 * @createTime May 15, 2012 11:31:59 AM
	 */
	public void mustFetchTaskStart(String pid) {
		autoFetchTaskManager.mustFetchTaskStart(pid);
	}

	/**
	 * 根据流程实例id获取该流程自动强制取回的节点的最新任务处理人id
	 * 
	 * @description
	 * @author 严建
	 * @param pid
	 * @return
	 * @createTime May 15, 2012 11:40:14 AM
	 */
	public String getAutoBackNodeIdAndActorId(String pid) {
		TaskBean taskbean = autoFetchTaskManager
				.getAutoBackNodeIdAndActorId(pid);
		if (taskbean != null && taskbean.getActorId() != null) {
			return taskbean.getActorId();
		}
		return null;
	}

	/**
	 * 保存流程催办记录
	 * 
	 * @description
	 * @author 严建
	 * @param model
	 * @param sendFlag - -催办发送标识 
	 * 			system：系统级发送 (see:WorkflowConst.WORKFLOW_NOTICE_SYSTEM) 
	 * 			person：手工级发送	 (see:WorkflowConst.WORKFLOW_NOTICE_PERSON)
	 * @createTime May 18, 2012 1:40:05 PM
	 */
	public void saveProcessUrgency(TProcessUrgency model,String sendFlag) {
		OALogInfo oaLogInfo = new OALogInfo(
				"保存流程催办记录,流程id=" + model.getProcessInstanceId());
		InetAddress inet;
		try {
			inet = InetAddress.getLocalHost();
			oaLogInfo.setOpeIp(inet.getHostAddress());
			if(sendFlag.equals(WorkflowConst.WORKFLOW_NOTICE_SYSTEM)){//系统催办
				oaLogInfo.setOpeUser(userService.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT));
			}else{
				oaLogInfo.setOpeUser(userService.getCurrentUser().getUserName());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		processUrgencyManager.updateOrSaveModel(model, oaLogInfo);
	}
}
