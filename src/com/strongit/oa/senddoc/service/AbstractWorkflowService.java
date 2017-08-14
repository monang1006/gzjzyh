package com.strongit.oa.senddoc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

@Repository
public class AbstractWorkflowService implements WorkflowService {

	@Autowired
	protected IWorkflowService workflowService;
	
	@Autowired
	protected IUserService userService;

	public static AbstractWorkflowService getInstance(String beanName) {
		return (AbstractWorkflowService)ServiceLocator.getService(beanName);
	}

	public Page findTodoForPage(Page page,String workflowType,String beanName) throws ServiceException, SystemException {
		if("notsign".equals(beanName)) {
			return findNotSignTodoForPage(page, workflowType);
		} else if("sign".equals(beanName)) {
			return findSignTodoForPage(page, workflowType);
		}
		return null;
	}
	
	public List findTodoForList(String workflowType,String beanName) throws ServiceException, SystemException {
		if("notsign".equals(beanName)) {
			return findNotSignTodoForList(workflowType);
		} else if("sign".equals(beanName)) {
			return findSignTodoForList(workflowType);
		}
		return null;
	}

	public List findTodoForList(List list,String beanName) throws ServiceException, SystemException {
		if("notsign".equals(beanName)) {
			return findNotSignTodoForList(list);
		} else if("sign".equals(beanName)) {
			return findSignTodoForList(list);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List getTaskInfosByConditionForList(String workflowType) {
		Object[] toSelectItems = {"taskId","taskStartDate","startUserName","processName","processInstanceId","businessName","startUserId","processMainFormBusinessId","taskNodeId"};
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "2");//取非办结任务
		paramsMap.put("handlerId", userService.getCurrentUser().getUserId());//当前用户办理任务
		if(workflowType != null && !"".equals(workflowType) && !"null".equals(workflowType)) { 
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for(String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("taskStartDate", "1");
		return workflowService.getTaskInfosByConditionForList(sItems, paramsMap, orderMap, null, null, null, null);
	}
	
	public Page findNotSignTodoForPage(Page page, String workflowType) throws ServiceException, SystemException {
		return null;
	}

	public Page findSignTodoForPage(Page page, String workflowType) throws ServiceException, SystemException {
		return null;
	}

	public List findNotSignTodoForList(String workflowType) throws ServiceException, SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public List findSignTodoForList(String workflowType) throws ServiceException, SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public List findNotSignTodoForList(List<Object[]> list) throws ServiceException, SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public List findSignTodoForList(List<Object[]> list) throws ServiceException, SystemException {
		// TODO Auto-generated method stub
		return null;
	}

}
