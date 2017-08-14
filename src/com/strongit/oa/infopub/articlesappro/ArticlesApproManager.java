package com.strongit.oa.infopub.articlesappro;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-20 下午03:18:50
 * Autour: lanlc
 * Version: V1.0
 * Description：处理审核信息manager
 */
@Service
@Transactional
public class ArticlesApproManager extends BaseManager {

	/**
	 * 得到当前用户相应类别的任务列表,
	   修改成挂起任务及指派委托状态也查找出来，在展现层展现状态 
	   增加查询条件参数,添加流程类型参数processType，以区分发文、收文等
	   @param page 分页对象
	   @param workflowType 流程类型参数
	   		1)大于0的正整数字符串：流程类型Id 
	   		2)0:不需指定流程类型
	   		3)-1:非系统级流程类型
	   @param businessName 业务名称查询条件
	   @param userName 主办人名称
	   @param startDate 任务开始时间
	   @param endDate 任务结束时间
	   @param isBackspace  是否是回退任务
	   		“0”：非回退任务；“1”：回退任务；“2”：全部
	   @return
	   		Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起, 
				 (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型} 
	 */
	@Transactional(readOnly = true)
	@Override
	public Page getTodoWorks(Page page,String workflowType,String businessName, 
			 				 String userName,Date startDate,
			 				 Date endDate,String isBackSpace,OALogInfo...infos) throws SystemException,ServiceException{
		try {
			User curUser = userService.getCurrentUser();//		获取当前用户
			String searchType = "all";
			return workflow.getTasksTodo(page, curUser.getUserId(), searchType, workflowType,businessName,userName,startDate,endDate,isBackSpace);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取待办工作"});
		}
	}	
	 /**
	 * 提交工作流转到下一步
	 * @param taskId 任务ID
	 * @param transitionName 流向名称
	 * @param returnNodeId 驳回节点ID
	 * @param isNewForm 当前节点是否是新的表单
	 * @param formId 表单ID
	 * @param businessId 业务ID
	 * @param suggestion 提交意见
	 * @param taskActors 下一步任务处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @param concurrentTrans 并发流向
	 */
	public void handleWorkflowNextStep(String taskId, String transitionName,
			String returnNodeId, String isNewForm, String formId,
			String businessId, String suggestion, String[] taskActors,
			String concurrentTrans) throws SystemException,ServiceException{
		try{
			User curUser = userService.getCurrentUser();
			workflow.goToNextTransition(taskId, transitionName, returnNodeId,isNewForm, "0", businessId, suggestion, curUser.getUserId(),taskActors);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"提交工作流转到下一步"});
    	}
	}
	/**
	 * 获取表单ID和业务ID
	 * @param taskId
	 * @return String[] 数据格式:[业务ID,表单ID]
	 */
	@Transactional(readOnly = true)
	public String[] getFormIdAndBussinessIdByTaskId(String taskId) throws SystemException,ServiceException{
		try{
			String[] ret = new String[2];
			String strNodeInfo="";
			try{
				strNodeInfo = workflow.getNodeInfo(taskId);
			}catch(Exception e){
				e.printStackTrace();
			}
			String[] arrNodeInfo = strNodeInfo.split(",");
			if ("form".equals(arrNodeInfo[0])) {
				ret[0] = arrNodeInfo[2];
				ret[1] = arrNodeInfo[3];
			} else {
				//异常情况，抛出异常
			}
			
			return ret;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取表单ID和业务ID"});
    	}
	}	
	/**
	 * 任务退回
	 * 退回操作之前需要先保存电子表单数据
	 * @author:胡丽丽
	 * @date:2009-12-15 下午08:22:14
	 * @param taskId 任务ID
	 * @param formId 表单ID
	 * @param suggestion 退回意见
	 * @param formData 电子表单数据
	 * @param returnNodeId 需要退回到的目标节点
	 * @throws SystemException
	 */
	public void backSpace(String taskId,String returnNodeId,String formId,String suggestion,String businessId) throws SystemException {
		String curUserId = userService.getCurrentUser().getUserId();
		super.handleWorkflowNextStep(taskId,WorkflowConst.WORKFLOW_TRANSITION_HUITUI,returnNodeId,"0",formId,businessId,suggestion,curUserId,null);
	}

}
