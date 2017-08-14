package com.strongit.oa.meetingmanage.meetingaudit;

import java.util.Calendar;
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
import com.strongit.oa.util.MessagesConst;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class MeetingtopicAuditManager extends BaseManager{

	 //工作流服务
	private IWorkflowService workflow;
	
//	统一用户服务
	private IUserService user;

	public IUserService getUser() {
		return user;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	@Autowired
	public void setWorkflow(IWorkflowService workflow) {
		this.workflow = workflow;
	}
	/**
	 * 获取当前用户待办来文列表
	 *@author 蒋国斌
	 *@date 2009-4-20 上午11:43:24 
	 * @param page
	 * @param workflowType
	 * @param businessName
	 * @param userName
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public Page getProcessedWorks(Page page,String workflowType,String businessName,String userName,Date startDate,Date endDate) throws SystemException,ServiceException{
    	try{
		    //获取当前用户
	    	User curUser = user.getCurrentUser();
			String searchType = "all";
			workflowType="5";
			if(endDate!=null && !endDate.equals("")){
				Calendar cal=Calendar.getInstance();
				 cal.setTime(endDate);
				 cal.add(Calendar.DAY_OF_MONTH,1); //加一天
				 endDate=cal.getTime();
				}
			try{
				//page = workflow.getTasksTodo(page, curUser.getUserId(), searchType, workflowType,businessName,userName,startDate,endDate);
			}catch(Exception e){
				e.printStackTrace();
			}
			return page;
    	}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取当前用户待办来文列表"});
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
	 * 查找节点的下一步都有哪些流向
	 *@author 蒋国斌
	 *@date 2009-4-20 下午02:21:52 
	 * @param taskId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	 @Transactional(readOnly = true)
		public List getNextTransitions(String taskId) throws SystemException,ServiceException{
	    	try{
	    		return workflow.getNextTransitions(taskId);		
	    	}catch(ServiceException e){
				throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"查找节点的下一步都有哪些流向"});
	    	}
		}
	 
	 /**
	  * 查看流程图
	  *@author 蒋国斌
	  *@date 2009-4-20 下午02:24:01 
	  * @param instanceId
	  * @return
	  * @throws SystemException
	  * @throws ServiceException
	  */
	 public Object[] getWorkflowMonitorData(String instanceId)throws SystemException,ServiceException{
			try{
				return workflow.getWorkflowMonitorData(instanceId);
			}catch(ServiceException e){
				throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"查看流程图数据信息"});
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
	 /**
		public void handleWorkflowNextStep(String taskId, String transitionName,
				String returnNodeId, String isNewForm, String formId,
				String businessId, String suggestion, String[] taskActors,
				String concurrentTrans) throws SystemException,ServiceException{
			try{
				User curUser = user.getCurrentUser();
				workflow.goToNextTransition(taskId, transitionName, returnNodeId,isNewForm, "0", businessId, suggestion, curUser.getUserId(),taskActors);
			}catch(ServiceException e){
				throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"提交工作流转到下一步"});
	    	}
		}	
		**/	
}
