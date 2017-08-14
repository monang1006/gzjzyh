package com.strongit.oa.common.extend.oamangager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.extend.pojo.BackSpaceLastExtenBean;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;

/**
 * 扩展退回上一步功能manager
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Mar 29, 2012 11:56:45 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.extend.oamangager.BackSpaceManager
 */
@Service
@Transactional
@OALogger
public class BackSpaceLastManager {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IWorkflowConstService workflowConstService;
	
	/**
	 * 根据oa业务 扩展退回功能
	 * (1)保存退回上一步意见
	 * 
	 * @author 严建
	 * @param bean
	 * @createTime Mar 29, 2012 11:54:41 AM
	 */
	public void backSpaceLastExtend(BackSpaceLastExtenBean bean){
		setBackSpaceLastInfo(bean);
	}
	
	/**
	 * oa业务  保存退回上一步意见
	 * 
	 * @author 严建
	 * @param bean
	 * @createTime Mar 29, 2012 11:52:56 AM
	 */
	private void setBackSpaceLastInfo(BackSpaceLastExtenBean parameter){
		String taskId = parameter.getTaskId();
		String suggestion = parameter.getSuggestion();
		String curUserId = parameter.getCurUserId();
		workflowConstService.setBackInfo(taskId, 
				CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACEPREV, suggestion, curUserId);
	}
}
