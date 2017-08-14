package com.strongit.oa.senddoc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.strongit.oa.util.ListUtils;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Repository("notsign")
public class WorkflowServiceNotSignImp extends AbstractWorkflowService {
	
	@SuppressWarnings("unchecked")
	public Page findNotSignTodoForPage(Page page, String workflowType) throws ServiceException, SystemException {
		List<Object[]> result = this.findNotSignTodoForList(workflowType);
		page = ListUtils.splitList2Page(page, result);
		return page;
	}

	@Override
	public List findNotSignTodoForList(List<Object[]> list) throws ServiceException, SystemException {
		List<Object[]> result = new LinkedList<Object[]>();
		if(list != null && !list.isEmpty()) {
			TwfBaseNodesetting nodeSetting;
			for(Object[] objs : list) {
				Object objNode = objs[7];
				String nodeId = null;
				if(objNode != null) {
					nodeId = objs[7].toString();
				}
				if(objs.length >= 10) {
					nodeId = objs[10].toString();
				}
				nodeSetting = workflowService.getNodesettingByNodeId(nodeId);
				String isNotSign = nodeSetting.getPlugin("plugins_chkModifySuggestion");
				if(isNotSign != null && "1".equals(isNotSign)) {//签收节点上的任务
					result.add(objs);
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List findNotSignTodoForList(String workflowType) throws ServiceException, SystemException {
		List<Object[]> list = getTaskInfosByConditionForList(workflowType);
		return this.findNotSignTodoForList(list);
	}
}
