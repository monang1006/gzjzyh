package com.strongit.oa.common.extend.oamangager;

import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TMainActorConfing;
import com.strongit.oa.common.business.mainactorconfig.MainActorConfigManage;
import com.strongit.oa.common.extend.pojo.GoToNextTransitionBean;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;

/**
 * 
 * 扩展提交下一步业务
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 5, 2012 3:23:50 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.extend.oamangager.GoToNextTransitionManager
 */
@Service
@Transactional
@OALogger
public class GoToNextTransitionManager {
	@Autowired
	private MainActorConfigManage mainactorconfigmanage;

	@Autowired
	private IWorkflowService workflowService;

	/**
	 * 根据oa业务 扩展启动新流程的业务
	 * 
	 * @description
	 * @author 严建
	 * @param bean
	 * @createTime Apr 5, 2012 3:26:39 PM
	 */
	public void goToNextTransitionExtend(GoToNextTransitionBean bean) {
		saveProcessInstanceMainActor(bean); // 保存主办人员
	}

	/**
	 * 提交下一步时保存主办人员
	 * 
	 * @author 严建
	 * @param bean
	 * @createTime Apr 5, 2012 3:34:49 PM
	 */
	private void saveProcessInstanceMainActor(GoToNextTransitionBean bean) {
		if (mainactorconfigmanage.getModelByProcessInstanceId(bean
				.getProcessInstanceId()) == null) {// 主办人员变更信息表中不存在该流程的相关信息
			ProcessInstance processinstance = workflowService
					.getProcessInstanceById(bean.getProcessInstanceId());
			if (processinstance.getStartUserId() != null
					&& !"".equals(processinstance.getStartUserId())) {// 该流程存在发起人时，将主办人员设置为主办人员，否则主办人员为指定的处理人
				bean.setMainActorId(processinstance.getStartUserId());
				bean.setMainActorName(processinstance.getStartUserName());
			}
			mainactorconfigmanage.updateModel(new TMainActorConfing(bean
					.getProcessInstanceId(), bean.getMainActorId()),
					new OALogInfo("启动提交时，保存流程主办人员信息：" + bean.getMainActorName()
							+ "【" + bean.getMainActorId() + "】"));
		}
	}
}
