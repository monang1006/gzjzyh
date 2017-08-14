package com.strongit.oa.common.service.adapter.entrust;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.service.adapter.bo.EntrustWorkflowParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;

/**
 * 
 * 委派|指派功能相关manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 17, 2012 3:44:44 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.service.adapter.entrust.EntrustWorkflowManager
 */
@Service
@Transactional
@OALogger
public class EntrustWorkflowManager {
    @Autowired
    private IUserService userService;// 统一用户服务

    @Autowired
    private IWorkflowService workflowService;// 统一用户服务
    @Autowired
    private IEFormService eform;// 统一用户服务
    
    /**
     * 得到委派|指派的流程
     * 
     * @author:邓志城
     * @date:2010-11-21 下午06:11:00
     * @param workflowType
     *                流程类别
     * @param assignType
     *                委托类型(“0”：委托；“1”：指派；“2”：全部)
     * @return List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Object[]> getEntrustWorkflow(EntrustWorkflowParameter parameter) {
	String workflowType = parameter.getWorkflowType();
	String assignType = parameter.getAssignType();
	String excludeWorkflowType = parameter.getExcludeWorkflowType();
	Object[] toSelectItems = { "processName", "processTypeId",
		"processTypeName", "processMainFormId","processInstanceId" };
	List sItems = Arrays.asList(toSelectItems);
	Map<String, Object> paramsMap = new HashMap<String, Object>();
	paramsMap.put("assignHandlerId", userService.getCurrentUser()
		.getUserId());// 委托/指派人Id(必须同时查询assignType属性)
	StringBuilder customQuery = new StringBuilder(" 1=1 ");
	if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)) {
	    customQuery.append(" and pi.typeId not in (" + excludeWorkflowType
		    + ") ");
	}
	if (workflowType != null && !"".equals(workflowType)
		&& !"null".equals(workflowType)) {
	    customQuery.append(" and pi.typeId in (" + workflowType + ") ");
	}
	if (assignType != null && !"".equals(assignType)) {
	    paramsMap.put("assignType", assignType);// 委托类型(“0”：委托；“1”：指派；“2”：全部)
	} else {
	    paramsMap.put("assignType", "2");
	}
	Map orderMap = new HashMap<Object, Object>();
	orderMap.put("processTypeId", "0");
	List list = workflowService.getTaskInfosByConditionForList(sItems,
		paramsMap, orderMap, null, null, customQuery.toString(), null);
	Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
//	List<String> pids = new LinkedList<String>();
	List<Object[]> workflowList = new ArrayList<Object[]>();
	if (list != null && !list.isEmpty()) {
	    for (int i = 0; i < list.size(); i++) {
		Object[] objs = (Object[]) list.get(i);
//		String processInstanceId = objs[4].toString();
//		if(pids.indexOf(processInstanceId) != -1){//同一个流程的数据不重复计算
//		    continue;
//		}else{
//		    pids.add(processInstanceId);
//		}
		if (!map.containsKey(objs[0].toString() + "$"
			+ objs[1].toString())) {
		    List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
		    workflowSet.add(objs);
		    map.put(objs[0].toString() + "$" + objs[1].toString(),
			    workflowSet);
		} else {
		    map.get(objs[0].toString() + "$" + objs[1].toString()).add(
			    objs);
		}
	    }
	    List<String> checkList = new ArrayList<String>();// 处理重复的记录
	    for (int j = 0; j < list.size(); j++) {
		Object[] objs = (Object[]) list.get(j);
		objs = StringUtil.arraydeleteAt(objs, 4);//保持数组长度，删除流程实例id信息
		if (!checkList.contains(objs[0].toString() + "$"
			+ objs[1].toString())) {
		    List l = map.get(objs[0].toString() + "$"
			    + objs[1].toString());
		    if (l != null) {
			objs = ObjectUtils.addObjectToArray(objs, l.size());
			workflowList.add(objs);
			checkList.add(objs[0].toString() + "$"
				+ objs[1].toString());
		    }
		}
	    }
	}
	return workflowList;
    }
}
