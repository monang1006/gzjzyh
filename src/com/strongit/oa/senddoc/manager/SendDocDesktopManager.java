package com.strongit.oa.senddoc.manager;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfBaseNodesetting;

@Service
@Transactional
@OALogger
public class SendDocDesktopManager{

	@Autowired
	SendDocManager SendDocManager;
	@Autowired
	protected IUserService userService;// 统一用户服务

	@Autowired
	protected IWorkflowService workflow; // 工作流服务
	/**
	 * @description 获取当前用户的主办任务
	 * @author 严建
	 * @createTime Dec 13, 2011 4:25:52 PM
	 * @return Page
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getHostedWorkflowForDesktop(String processStatus) {
		Object[] toSelectItems = { "processInstanceId",
				"processMainFormBusinessId", "processEndDate",
				"processTimeout", "businessName", "processName",
				"processStartDate" };
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("startUserId", userService.getCurrentUser().getUserId());// 当前用户办理任务
		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processStartDate", "1");
		return workflow.getProcessInstanceByConditionForList(sItems, paramsMap,
				orderMap);
	}

	
	
	/**
	 * @description 根据流程实例ID和任务id查询出当前处理人和当前处理人所在的部门信息
	 * @param processInstanceId 	流程实例id
	 * @param signId 				标识id
	 * @param currentuserinfo 		当前处理人的信息
	 * @param currentuserDept 		当前处理人的部门信息
	 * @author 严建
	 * @createTime Dec 15, 2011 3:57:56 PM
	 * @return Map[]  数据格式map[0]【taskId,当前处理人】 map[1]【taskId,当前处理人所在部门】
	 */
	@SuppressWarnings("unchecked")
	public Map[] getHandlerAndDeptInfo(String processInstanceId, String signId,Map currentuserinfo,Map currentuserDept) {
		Map[] map = new HashMap[2];
		map[0] = currentuserinfo;
		map[1] = currentuserDept;
		List<Object[]> ss = workflow.getMonitorChildrenInstanceIds(new Long(
				processInstanceId));
		if (!ss.isEmpty()) {// 存在子流程
			StringBuilder userNames = new StringBuilder();
			StringBuilder userNamesNoDept = new StringBuilder();
			StringBuilder userDept = new StringBuilder();
			StringBuilder strUserName = new StringBuilder();// 人员姓名
			StringBuilder strUserDept = new StringBuilder();// 用户部门
			for (Object[] s : ss) {
				Object[] ds = workflow.getProcessStatusByPiId(s[0].toString());
				Collection col2 = (Collection) ds[6];// 处理任务信息
				if (col2 != null && !col2.isEmpty()) {

					for (Iterator it = col2.iterator(); it.hasNext();) {
						Object[] itObjs = (Object[]) it.next();
						String userId = (String) itObjs[3];
						if (userId != null && !"".equals(userId)) {
							String[] userIds = userId.split(",");
							for (String id : userIds) {
								userNamesNoDept.append(
										userService.getUserNameByUserId(id))
										.append(",");
								userDept.append(
										userService.getUserDepartmentByUserId(
												id).getOrgName()).append(",");
								userNames
										.append(
												userService
														.getUserNameByUserId(id))
										.append(
												"["
														+ userService
																.getUserDepartmentByUserId(
																		id)
																.getOrgName()
														+ "]").append(",");
							}
						}
					}
				}
			}
			if (userNames.length() > 0) {
				userNames.deleteCharAt(userNames.length() - 1);
				userNamesNoDept.deleteCharAt(userNamesNoDept.length() - 1);
				userDept.deleteCharAt(userDept.length() - 1);
				strUserName.append(userNamesNoDept);
				strUserDept.append(userDept);
			}else{
				userNamesNoDept.append(userService.getCurrentUser().getUserName());
				userDept.append(userService.getOrgInfoByOrgId(userService.getCurrentUser().getOrgId()).getOrgName());
				strUserName.append(userNamesNoDept);
				strUserDept.append(userDept);
			}
			currentuserinfo.put(signId, strUserName.toString());
			currentuserDept.put(signId, strUserDept.toString());
		} else {
			Object[] returnObjs = workflow
					.getProcessStatusByPiId(processInstanceId);// 得到此流程实例下的运行情况
			Collection col = (Collection) returnObjs[6];// 处理任务信息
			if (col != null && !col.isEmpty()) {
				StringBuilder strUserName = new StringBuilder();// 人员姓名
				StringBuilder strUserDept = new StringBuilder();// 用户部门

				for (Iterator it = col.iterator(); it.hasNext();) {
					Object[] itObjs = (Object[]) it.next();
					String userId = (String) itObjs[3];

					StringBuilder userNames = new StringBuilder();
					StringBuilder userNamesNoDept = new StringBuilder();
					StringBuilder userDept = new StringBuilder();
					if (userId != null && !"".equals(userId)) {
						String[] userIds = userId.split(",");
						for (String id : userIds) {
							userNamesNoDept.append(
									userService.getUserNameByUserId(id))
									.append(",");
							userDept.append(
									userService.getUserDepartmentByUserId(id)
											.getOrgName()).append(",");
							userNames
									.append(userService.getUserNameByUserId(id))
									.append(
											"["
													+ userService
															.getUserDepartmentByUserId(
																	id)
															.getOrgName() + "]")
									.append(",");
						}
						userDept.deleteCharAt(userDept.length() - 1);
						userNamesNoDept
								.deleteCharAt(userNamesNoDept.length() - 1);
						userNames.deleteCharAt(userNames.length() - 1);
					}
					if (userNames.length() > 0) {
						strUserName.append(userNamesNoDept);
						strUserDept.append(userDept);
					}else{
						userNamesNoDept.append(userService.getCurrentUser().getUserName());
						userDept.append(userService.getOrgInfoByOrgId(userService.getCurrentUser().getOrgId()).getOrgName());
						strUserName.append(userNamesNoDept);
						strUserDept.append(userDept);
					}
				}
				currentuserinfo.put(signId, strUserName.toString());
				currentuserDept.put(signId, strUserDept.toString());
			}
		}
		return map;
	}
	
	/**
	 * 获取待办文的数据信息（包括待签收和待办的文）
	 * @author 严建
	 * @return
	 * @createTime Jan 6, 2012 3:50:38 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Integer> getDoingWorkKindsGroupInfo(){
		Map<String,List<Object[]>> map = new HashMap<String, List<Object[]>>();
		Map<String,Integer> result = new HashMap<String, Integer>();
		List retList = null;
		Object[] toSelectItems = {"taskId","taskStartDate","startUserName","processName","processInstanceId","businessName","processMainFormBusinessId","taskNodeId","processTypeId"};
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "2");//取非办结任务
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		paramsMap.put("handlerId", userService.getCurrentUser().getUserId());//当前用户办理任务
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("taskStartDate", "1");
		retList = workflow.getTaskInfosByConditionForList(sItems, paramsMap, orderMap, null, null, null, null);
		List<String> nodeIdList = new LinkedList<String>();
		for(int i=0;i<retList.size();i++){
			String taskNodeId = ((Object[])retList.get(i))[7].toString();
			nodeIdList.add(taskNodeId);
		}
		Map<String,TwfBaseNodesetting> nodesettingMap = workflow.getNodesettingMapByNodeIdList(nodeIdList);
		List<Object[]> signList = new LinkedList<Object[]>();
		map.put("sign", signList);
		List<Object[]> notsignList = new LinkedList<Object[]>();
		map.put("notsign", notsignList);
		for(int i=0;i<retList.size();i++){
			Object[] objs = (Object[])retList.get(i);
			String taskNodeId = objs[7].toString();
			TwfBaseNodesetting nodeSetting = nodesettingMap.get(taskNodeId);
			if(!workflow.isSignNodeTask(taskNodeId, nodeSetting)){
				map.get("sign").add(objs);
			}else{
				map.get("notsign").add(objs);
			}
		}
		result.put("sign",map.get("sign").size());
		result.put("notsign", map.get("notsign").size());
		return result;
	}
	
	
	/**
	 * 获取已办文的数据信息（包括在办和办结的文）
	 * @author 严建
	 * @return
	 * @createTime Jan 6, 2012 4:05:56 PM
	 */
	public Map<String,Integer> getDoneWorkKindsGroupInfo(){
		List<Object[]> noendInfoList = SendDocManager.getProcessedWorkflow("", "0","0");
		List<Object[]> endInfoList = SendDocManager.getProcessedWorkflow("", "1","0");
		Map<String,Integer> map = new HashMap<String, Integer>();
		int noendInfoSize = 0;
		for(Object[] noendInfo:noendInfoList){
			noendInfoSize += Integer.parseInt(noendInfo[4].toString());
		}
		int endInfoSize = 0;
		for(Object[] endInfo:endInfoList){
			endInfoSize += Integer.parseInt(endInfo[4].toString());
		}
		map.put("end", endInfoSize);
		map.put("noend", noendInfoSize);
		return map;
	}
}
