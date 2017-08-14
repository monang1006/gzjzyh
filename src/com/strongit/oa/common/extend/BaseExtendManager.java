package com.strongit.oa.common.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.extend.oamangager.BackSpaceLastManager;
import com.strongit.oa.common.extend.oamangager.BackSpaceManager;
import com.strongit.oa.common.extend.oamangager.GoToNextTransitionManager;
import com.strongit.oa.common.extend.oamangager.OverRuleManager;
import com.strongit.oa.common.extend.oamangager.StartProcessToFistNodeManager;
import com.strongit.oa.common.extend.oamangager.StartWorkflowManager;
import com.strongit.oa.common.extend.pojo.BackSpaceExtenBean;
import com.strongit.oa.common.extend.pojo.BackSpaceLastExtenBean;
import com.strongit.oa.common.extend.pojo.GoToNextTransitionBean;
import com.strongit.oa.common.extend.pojo.OverRuleExtenBean;
import com.strongit.oa.common.extend.pojo.StartProcessToFistNodeBean;
import com.strongit.oa.common.extend.pojo.StartWorkflowBean;
import com.strongit.oa.util.annotation.OALogger;

/**
 * com.strongit.oa.common.BaseManager的功能扩展管理类
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 29, 2012 11:38:13 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.extend.BaseExtendManager
 */
@Service
@Transactional
@OALogger
public class BaseExtendManager {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BackSpaceManager backSpaceManager;

	@Autowired
	private BackSpaceLastManager backSpaceLastManager;

	@Autowired
	private OverRuleManager overRuleManager;

	@Autowired
	private StartWorkflowManager startWorkflowManager;

	@Autowired
	private GoToNextTransitionManager goToNextTransitionManager;
	@Autowired
	private StartProcessToFistNodeManager startProcessToFistNodeManager;

	/**
	 * 根据oa业务 扩展退回功能
	 * 
	 * @author 严建
	 * @param bean
	 * @createTime Mar 29, 2012 11:54:41 AM
	 */
	public void backSpaceExtend(BackSpaceExtenBean bean) {
		backSpaceManager.backSpaceExtend(bean);
	}

	/**
	 * 根据oa业务 扩展退回上一步功能
	 * 
	 * @author 严建
	 * @param bean
	 * @createTime Mar 29, 2012 12:46:19 PM
	 */
	public void backSpaceLastExtend(BackSpaceLastExtenBean bean) {
		backSpaceLastManager.backSpaceLastExtend(bean);
	}

	/**
	 * 根据oa业务 扩展驳回功能
	 * 
	 * @author 严建
	 * @param bean
	 * @createTime Mar 29, 2012 12:46:19 PM
	 */
	public void overRuleExtend(OverRuleExtenBean bean) {
		overRuleManager.overRuleExtend(bean);
	}

	/**
	 * 根据oa业务 扩展启动新流程的业务
	 * 
	 * @description
	 * @author 严建
	 * @param bean
	 * @createTime Apr 5, 2012 3:26:39 PM
	 */
	public void startWorkflowExtend(StartWorkflowBean bean) {
		startWorkflowManager.startWorkflowExtend(bean);
	}

	/**
	 * 根据oa业务 扩展扩展启动流程并使流程停留在第一个任务节点,通常为拟稿节点业务
	 * 
	 * @description
	 * @author 严建
	 * @param bean
	 * @createTime Apr 5, 2012 3:26:39 PM
	 */
	public void startProcessToFistNodeExtend(StartProcessToFistNodeBean bean){
	    startProcessToFistNodeManager.startProcessToFistNodeExtend(bean);
	}
	/**
	 * 根据oa业务 扩展流程提交的业务
	 * 
	 * @author 严建
	 * @param bean
	 * @createTime Apr 5, 2012 3:53:17 PM
	 */
	public void goToNextTransitionExtend(GoToNextTransitionBean bean) {
		goToNextTransitionManager.goToNextTransitionExtend(bean);
	}
}
