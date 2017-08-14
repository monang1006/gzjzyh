package com.strongit.oa.wap.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.ListUtils;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
public class WorkflowForWapService {

	
	@Autowired
	private IWorkflowService workflowService;

	
	private Date parseDate(String dateStr) throws ParseException,SystemException {
		   try {
			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      return dateFormat.parse(dateStr);
		   } catch (ParseException e) {
		     throw e;
		   } catch (Exception e) {
			   throw new SystemException(e);
		   }
		}
	
	/**
	 * 得到待办流程数量
	 * @param userId			用户id
	 * @param workflowType		流程类型 ,多个流程类型以逗号隔开
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer getTodoCount(String userId,String workflowType) {
		List<String> items = new ArrayList<String>();
		items.add("taskId");					//任务id
		items.add("processName");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(userId == null || "".equals(userId)||"null".equals(userId)) {
			throw new SystemException("参数userId不能为空！");
		}
		paramsMap.put("handlerId", userId);		//处理人
		paramsMap.put("taskType", "2");			//非办结任务
		List<String> type = new ArrayList<String>(2);//流程类型，仅查询收发文
		if(workflowType != null && workflowType.length() > 0) {
			String[] workflowTypes = workflowType.split(",");
			for(String t : workflowTypes) {
				type.add(t);
			}
			paramsMap.put("processTypeId", type);
		}
		List lst=workflowService.getTaskInfosByConditionForList(items, paramsMap, null,null, null, null, null);
		/*List newLst=new ArrayList();
		for(int i=0;i<lst.size();i++){
			Object[] obj=(Object[])lst.get(i);
			if(!obj[1].toString().equals(GlobalBaseData.NXSWORKFLOW)){
				//lst.remove(i);
				newLst.add(obj);
			}
		}*/
		return lst.size();
	}

	/**
	 * 得到待办流程列表
	 * @param userId						用户id
	 * @param userName						拟稿人名称
	 * @param workflowType					流程类型
	 * @param businessName					业务标题
	 * @param pageNo						页码
	 * @param pageSize						每页显示的数量
	 * @param startDate						任务接收日期开始范围
	 * @param endDate						任务接收日期结束范围
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page getTodo(String userId,String userName,String workflowType,
						 String businessName,String pageNo,
						 String pageSize,String startDate,String endDate) throws Exception {
		List<String> items = new ArrayList<String>();
		items.add("taskId");					//任务id
		items.add("taskStartDate");				//任务开始时间
		items.add("businessName");				//业务数据标题
		items.add("startUserName");				//拟稿人
		items.add("processTypeId"); 			//公文类别 2：发文；3：收文
		items.add("processName");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(userId == null || "".equals(userId)) {
			throw new SystemException("参数userId不能为空！");
		}
		paramsMap.put("handlerId", userId);		//处理人
		paramsMap.put("taskType", "2");			//非办结任务
		List<String> type = new ArrayList<String>(2);//流程类型，仅查询收发文
		if(workflowType != null && workflowType.length() > 0) {
			String[] workflowTypes = workflowType.split(",");
			for(String t : workflowTypes) {
				type.add(t);
			}
			paramsMap.put("processTypeId", type);
		}
//		以下为查询条件
		if(userName != null && !"".equals(userName)) {
			paramsMap.put("startUserName", userName);
		}
		if(businessName != null && !"".equals(businessName)) {
			paramsMap.put("businessName", businessName);
		}
		if(startDate != null && !"".equals(startDate)) {
			Date sd = this.parseDate(startDate);
			paramsMap.put("taskStartDateStart", sd);
		}
		if(endDate != null && !"".equals(endDate)) {
			Date ed = this.parseDate(endDate);
			paramsMap.put("taskStartDateEnd", ed);
		}
		// --------- ------  //
		Map orderMap = new HashMap();
		orderMap.put("taskStartDate", "1");
		if(pageSize == null || "".equals(pageSize)) {
			throw new SystemException("参数pageSize不可为空！");
		}
		if(pageNo == null || "".equals(pageNo)) {
			throw new SystemException("参数pageNo不可为空！");
		}
		Page page = new Page(Integer.parseInt(pageSize),true);
		page.setPageNo(Integer.parseInt(pageNo));
		//Page<Object[]> pageWorkflow = workflowService.getTaskInfosByConditionForPage(page,items, paramsMap, orderMap,
		//						
		List lst = workflowService.getTaskInfosByConditionForList(items, paramsMap, orderMap,null, null, null, null);
		List newLst=new ArrayList();
		for(int i=0;i<lst.size();i++){
			/*Object[] obj=(Object[])lst.get(i);
			if(!obj[5].toString().equals(GlobalBaseData.NXSWORKFLOW)){
				//lst.remove(i);
				newLst.add(obj);
			}*/
		}
		page=ListUtils.splitList2Page(page, newLst);
		return page;
	}
}
