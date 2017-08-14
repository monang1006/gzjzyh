package com.strongit.oa.bgt.documentview.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bgt.documentview.bo.DocumentViewParamter;
import com.strongit.oa.bgt.documentview.bo.UserAndOrgInfo;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.service.DataBaseUtil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.INodeService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.util.StringUtil;
import com.strongit.workflow.workflowDesign.action.util.ProcessInstanceDataUtil;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 文件监管（文件办理情况）功能
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 6, 2012 1:25:34 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.bgt.documentview.manager.DocumentViewManager
 */
@Service
@Transactional
public class DocumentViewManager {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ICustomUserService customUserService;
	@Autowired
	IUserService userService;
	@Autowired
	private IProcessInstanceService processInstanceService;
	@Autowired
	private ITaskService taskservice;
	@Autowired
	private INodeService nodeService;

	/**
	 * 获取分管用户信息
	 * 
	 * @description
	 * @author 严建
	 * @param userId
	 * @return
	 * @createTime Apr 5, 2012 2:12:36 PM
	 */
	@SuppressWarnings("unchecked")
	public List<UserAndOrgInfo> getManagerUserAndOrgInfoForList(String userId) {
		List<Object[]> list = customUserService.getManagerUserInfo(userId);
		List<UserAndOrgInfo> returnList = null;
		for (Object[] objs : list) {
			if (returnList == null) {
				returnList = new LinkedList<UserAndOrgInfo>();
			}
			UserAndOrgInfo userAndOrgInfo = new UserAndOrgInfo();
			userAndOrgInfo.setUserId(StringUtil.castString(objs[0]));
			userAndOrgInfo.setUserName(StringUtil.castString(objs[1]));
			userAndOrgInfo.setOrgId(StringUtil.castString(objs[2]));
			userAndOrgInfo.setOrgName(StringUtil.castString(objs[3]));
			userAndOrgInfo.setOrgSequence(new Long(StringUtil
					.castString(objs[4])));
			returnList.add(userAndOrgInfo);
		}
		if (returnList != null) {
			returnList = new ArrayList<UserAndOrgInfo>(returnList);
		}
		logger.info("获取分管用户信息");
		return returnList;
	}

	/**
	 * 获取（在办、办结）统计数
	 * 
	 * @description
	 * @author 严建
	 * @param paramter
	 * @return
	 * @createTime Apr 6, 2012 12:59:25 PM
	 */
	public int getwjtjTotal(DocumentViewParamter paramter) {
		initParamter(paramter);
		Page page = new Page(1, true);
		int total = getProcessInstanceByConditionForPage(page, paramter)
				.getTotalCount();
		return (total < 0 ? 0 : total);
	}

	/**
	 * 获取待办统计数
	 * 
	 * @description
	 * @author 严建
	 * @param paramter
	 * @return
	 * @createTime Apr 9, 2012 2:48:26 PM
	 */
	public int getTodowjtjTotal(DocumentViewParamter paramter){
		initTaskInfosParamter(paramter);
		Page page = new Page(1, true);
		int total = getTaskInfosByConditionForPage(page, paramter)
				.getTotalCount();
		return (total < 0 ? 0 : total);
	}
	
	/**
	 * 获取文件统计列表
	 * 
	 * @description
	 * @author 严建
	 * @param paramter
	 * @return
	 * @createTime Apr 6, 2012 1:10:45 PM
	 */
	public List<Object[]> getwjtjForList(DocumentViewParamter paramter) {
		initParamter(paramter);
		return getProcessInstanceByConditionForList(paramter);
	}
	public Page getwjtjForPage(Page page,DocumentViewParamter paramter) {
		initParamter(paramter);
		return getProcessInstanceByConditionForPage(page,paramter);
	}

	private void initParamter(DocumentViewParamter paramter) {
		if (paramter.getCustomFromItems() == null) {
			paramter.setCustomFromItems(new StringBuilder());
		}
		if (paramter.getCustomQuery() == null) {
			paramter.setCustomQuery(new StringBuilder(" 1=1 "));
		}
		if (paramter.getCustomSelectItems() == null) {
			paramter.setCustomSelectItems(new StringBuilder());
		}
		if (paramter.getItemsList() == null) {
			paramter.setItemsList(new ArrayList<String>());
		}
		if (paramter.getParamsMap() == null) {
			paramter.setParamsMap(new HashMap<String, Object>());
		}
		if (paramter.getOrderMap() == null) {
			paramter.setOrderMap(new HashMap<String, String>());
		}
		String mytype = paramter.getMytype();
		String state = paramter.getState();
		String orgIds = paramter.getOrgIds();
		String startUserName = paramter.getStartUserName();
		String excludeWorkflowType = paramter.getExcludeWorkflowType();
		String workflowType = paramter.getWorkflowType();
		String isSuspended = paramter.getIsSuspended();
		String businessName = paramter.getBusinessNmae();
		String processName = paramter.getWorkflowName();
		ProcessInstanceDataUtil processInstanceDataUtil = new ProcessInstanceDataUtil(
				mytype);

		Map<String, Object> paramsMap = paramter.getParamsMap();// 查询条件设置
		if(isSuspended != null && !"".equals(isSuspended)){
			paramsMap.put("processSuspend", isSuspended);
		}else{
			paramsMap.put("processSuspend", "0");
		}
		if (state != null && !"".equals(state)) {
			paramsMap.put("processStatus", state);
			if("1".equals(state)){//办结文取本年度的
		    	int doneyear = Integer.MIN_VALUE;
				if (paramter.getDoneYear() != 0) {
					if (paramter.getDoneYear() == -1) {// -1取当前年份的数据
						java.util.Calendar Cal = java.util.Calendar
								.getInstance();
						doneyear = Cal.get(java.util.Calendar.YEAR);
					} else {
						doneyear = paramter.getDoneYear();
					}
					paramter.getCustomQuery().append(
							" and " + DataBaseUtil.SqlYearOfDate("pi.end")
									+ "=" + doneyear + " ");
				}
			}
		}
		
		if (businessName != null && !"".equals(businessName)) {
			paramsMap.put("businessName", "%"+businessName+"%");
		}
		if (processName != null && !"".equals(processName)) {
			paramsMap.put("processName", "%"+processName+"%");
		}
		Map<String, String> orderMap = paramter.getOrderMap();
		orderMap.put("processStartDate", "1");

		String sql = null;
		if ("org".equals(mytype)) {
			if ((orgIds != null && !"".equals(orgIds))) {
				sql = processInstanceDataUtil.getSql(paramter.getCurUserId(),
						orgIds, startUserName);
			} else {
				sql = processInstanceDataUtil.getSql(paramter.getCurUserId(),
						null, startUserName);
			}
		} else {
			sql = processInstanceDataUtil.getSql(paramter.getCurUserId(),
					paramter.getCurUserOrgIg(), startUserName);
		}
		if(paramter.getCustomSelectItems().length()>0){
			paramter.getCustomSelectItems().append(" ,mainActor.mainActorId, wfbaseprocessfile.rest2 as workflowAliaName  ");
		}else{
			paramter.getCustomSelectItems().append(" mainActor.mainActorId, wfbaseprocessfile.rest2 as workflowAliaName  ");
		}
		if(paramter.getCustomFromItems().length()>0){
			paramter.getCustomFromItems().append(
			" ,com.strongit.oa.bo.TMainActorConfing mainActor,com.strongit.workflow.bo.TwfBaseProcessfile wfbaseprocessfile, com.strongit.oa.bo.Tjbpmbusiness extendtjbpmbusiness  ");
		}else{
			paramter.getCustomFromItems().append(
			" com.strongit.oa.bo.TMainActorConfing mainActor,com.strongit.workflow.bo.TwfBaseProcessfile wfbaseprocessfile, com.strongit.oa.bo.Tjbpmbusiness extendtjbpmbusiness  ");
		}
		if(paramter
				.getCustomQuery().length()>0){
			paramter
			.getCustomQuery()
			.append(
					" and mainActor.processInstanceId = to_char(@processInstanceId) and pi.startUserId in ("
					+ sql + ")  AND  wfbaseprocessfile.pfName =  pi.name  ");
		}else{
			paramter
			.getCustomQuery()
			.append(
					"mainActor.processInstanceId = to_char(@processInstanceId) and pi.startUserId in ("
					+ sql + ")  AND  wfbaseprocessfile.pfName =  pi.name  ");
		}
		paramter.getCustomQuery().append(" and pi.name not like '自办文' ");
		paramter.getCustomQuery().append(" and @businessId = extendtjbpmbusiness.businessId ");
		paramter.getCustomQuery().append( " and extendtjbpmbusiness.businessType in ("+Tjbpmbusiness.getShowableBusinessType()+") ");
		if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)) {
			paramter.getCustomQuery().append(
					" and pi.typeId not in (" + excludeWorkflowType + ") ");
		}
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			paramter.getCustomQuery().append(
					" and pi.typeId in (" + workflowType + ") ");
		}
		if ("1".equals(state)) {// 办结文取本年度的
			java.util.Calendar Cal = java.util.Calendar.getInstance();
			int doneYear = paramter.getDoneYear();
			doneYear = (doneYear<1?0 :doneYear);
			if (doneYear != 0) {
			    Cal.set(java.util.Calendar.YEAR, doneYear);
			    paramter.getCustomQuery().append(
				    " and to_char(pi.end,  'yyyy')="
				    + Cal.get(java.util.Calendar.YEAR) + " ");
			}
		}
	}

	private List<Object[]> getProcessInstanceByConditionForList(
			DocumentViewParamter paramter) {
		List<Object[]> processList = processInstanceService
				.getProcessInstanceByConditionForList(paramter.getItemsList(),
						paramter.getParamsMap(), paramter.getOrderMap(),
						paramter.getCustomSelectItems().toString(), paramter
								.getCustomFromItems().toString(), paramter
								.getCustomQuery().toString(), null);
		return processList;
	}

	private Page getProcessInstanceByConditionForPage(Page page,
			DocumentViewParamter paramter) {
		return processInstanceService.getProcessInstanceByConditionForPage(
				page, paramter.getItemsList(), paramter.getParamsMap(),
				paramter.getOrderMap(), paramter.getCustomSelectItems()
						.toString(), paramter.getCustomFromItems().toString(),
				paramter.getCustomQuery().toString(), null);
	}
	
	/**
	 * 获取待办文件
	 * 
	 * @description
	 * @author 严建
	 * @param paramter
	 * @return
	 * @createTime Apr 9, 2012 2:26:35 PM
	 */
	public List<Object[]> getTodowjtjForList(DocumentViewParamter paramter) {
		initTaskInfosParamter(paramter);
		return getTaskInfosByConditionForList(paramter);
	}
	public Page getTodowjtjForPage(Page page,DocumentViewParamter paramter) {
		initTaskInfosParamter(paramter);
		return getTaskInfosByConditionForPage( page,paramter);
	}
	
	private void initTaskInfosParamter(DocumentViewParamter paramter) {
		if (paramter.getCustomFromItems() == null) {
			paramter.setCustomFromItems(new StringBuilder());
		}
		long stmp = System.currentTimeMillis();
		if (paramter.getCustomQuery() == null) {
			paramter.setCustomQuery(new StringBuilder(" 1=1 "));
		}
		paramter.getCustomQuery().append(" and ").append(stmp).append(" = ").append(stmp);
		if (paramter.getCustomSelectItems() == null) {
			paramter.setCustomSelectItems(new StringBuilder());
		}
		if (paramter.getItemsList() == null) {
			paramter.setItemsList(new ArrayList<String>());
		}
		if (paramter.getParamsMap() == null) {
			paramter.setParamsMap(new HashMap<String, Object>());
		}
		if (paramter.getOrderMap() == null) {
			paramter.setOrderMap(new LinkedHashMap<String, String>());
		}
		
		String mytype = paramter.getMytype();
		String orgIds = paramter.getOrgIds();
		String excludeWorkflowType = paramter.getExcludeWorkflowType();
		String workflowType = paramter.getWorkflowType();
		String businessName = paramter.getBusinessNmae();
		String processName = paramter.getWorkflowName();
		Date processStartDateStart = paramter.getProcessStartDateStart() ;
		Date processStartDateEnd = paramter.getProcessStartDateEnd() ;
		Map<String, Object> paramsMap = paramter.getParamsMap();// 查询条件设置
		Map<String, String> orderMap = paramter.getOrderMap();
		orderMap.put("processStartDate", "1");
		orderMap.put("processInstanceId", "1");
		if (businessName != null && !"".equals(businessName)) {
			paramsMap.put("businessName", "%"+businessName+"%");
		}
		if (processName != null && !"".equals(processName)) {
			paramsMap.put("processName", "%"+processName+"%");
		}
		if(processStartDateStart != null){
			paramsMap.put("processStartDateStart", processStartDateStart);
		}
		if(processStartDateEnd != null){
			paramsMap.put("processStartDateEnd", processStartDateEnd);
		}
		paramsMap.put("taskType", "2");// 取非办结任务
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		String sql = null;
		if ("org".equals(mytype)) {
			if ((orgIds != null && !"".equals(orgIds))) {
				sql = getSignDeptNodeNameSql(paramter.getCurUserId(),
						orgIds, mytype);
			} else {
				sql = getSignDeptNodeNameSql(paramter.getCurUserId(),
						null, mytype);
			}
		} else {
			sql = getSignDeptNodeNameSql(paramter.getCurUserId(),
					paramter.getCurUserOrgIg(), mytype);
		}
		if(paramter.getCustomFromItems().length()>0){
			paramter.getCustomFromItems().append(", com.strongit.oa.bo.Tjbpmbusiness extendtjbpmbusiness");
		}else{
			paramter.getCustomFromItems().append("com.strongit.oa.bo.Tjbpmbusiness extendtjbpmbusiness");
		}
		if(paramter.getCustomQuery().length()>0){
			paramter.getCustomQuery().append(" and exists (select extendti.id from org.jbpm.taskmgmt.exe.TaskInstance extendti where  extendti.nodeName in ("+
					sql+")" +
			" and extendti.nodeName = ti.nodeName) ");
		}else{
			paramter.getCustomQuery().append("  exists (select extendti.id from org.jbpm.taskmgmt.exe.TaskInstance extendti where  extendti.nodeName in ("+
					sql+")" +
			" and extendti.nodeName = ti.nodeName) ");
		}
		paramter.getCustomQuery().append(" and pi.name not like '自办文' ");
		paramter.getCustomQuery().append(" and @businessId = extendtjbpmbusiness.businessId ");
		paramter.getCustomQuery().append( " and extendtjbpmbusiness.businessType in ("+Tjbpmbusiness.getShowableBusinessType()+") ");
		if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)) {
			paramter.getCustomQuery().append(
					" and pi.typeId not in (" + excludeWorkflowType + ") ");
		}
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			paramter.getCustomQuery().append(
					" and pi.typeId in (" + workflowType + ") ");
		}
				
	}
	
	private List<Object[]> getTaskInfosByConditionForList(DocumentViewParamter paramter){
		return taskservice.getTaskInfosByConditionForList( paramter.getItemsList(), paramter.getParamsMap(),
				paramter.getOrderMap(), paramter.getCustomSelectItems()
						.toString(), paramter.getCustomFromItems().toString(),
				paramter.getCustomQuery().toString(), null);
	}
	private Page getTaskInfosByConditionForPage(Page page,DocumentViewParamter paramter){
		return taskservice.getTaskInfosByConditionForPage(page, paramter.getItemsList(), paramter.getParamsMap(),
				paramter.getOrderMap(), paramter.getCustomSelectItems()
				.toString(), paramter.getCustomFromItems().toString(),
		paramter.getCustomQuery().toString(), null);
	}
	
	public String getSignDeptNodeNameSql(String userId, String orgId,
			String type) {
		if (type == null || "".equals(type)) {
			throw new ServiceException("ERROR:type is null or type is \"\"");
		}
		String sql = null;
		if ("org".equals(type)) {
			sql = "select " + adapterSignDeptNodeName("org.orgName")
					+ " from TUumsBaseOrg org where org.rest2 like '%" + userId
					+ "%'";
			if (orgId != null && !"".equals(orgId)) {// 查询具体处室的数据
				String[] orgIdarr = orgId.split(",");
				sql += " and (";
				for (String org : orgIdarr) {
					sql += " org.orgId ='" + org + "' or";
				}
				sql = sql.substring(0, sql.length() - 2);
				sql += " ) ";
			}
		} else if ("dept".equals(type)) {
			sql = "select " + adapterSignDeptNodeName("org.orgName")
					+ " from TUumsBaseOrg org where org.rest2 like '%" + userId
					+ "%'";
		}
		return sql;
	}
	
	
	public List<String> getToSelectItems() {
			List<String> itemsList = new LinkedList<String>();
			itemsList.add("processInstanceId");
			itemsList.add("businessName");
			itemsList.add("startUserName");
			itemsList.add("processStartDate");
			itemsList.add("taskNodeName");
		return new ArrayList<String>(itemsList);
	}
	@SuppressWarnings("unchecked")
	public Page<TaskBean> getResult(Page page)
			throws SystemException {
		List result = page.getResult();
		List<TaskBean> list = getResult(result);
		if(list == null || list.isEmpty()){
			page.setTotalCount(0);
		}
		page.setResult(list);
		return page;
	}
	@SuppressWarnings("unchecked")
	public List<TaskBean> getResult(List objlist) throws SystemException {
		List<TaskBean> list = null;
		if (objlist != null && !objlist.isEmpty()) {
			Map<String, Object[]> map = customUserService
					.getManagerUserInfoForMap(userService.getCurrentUser()
							.getUserId());
			if (map != null) {
				String signNodeNameSuffix = nodeService.getSignNodeNameSuffix();
				List<String> itemsList = getToSelectItems();
				for(Object obj:objlist){
					if(list == null){
						list = new LinkedList<TaskBean>();
					}
					Object[] objs = (Object[])obj;
					TaskBean bean = new TaskBean();
					if (itemsList.indexOf("processInstanceId") != -1) {
						bean.setInstanceId(objs[itemsList
								.indexOf("processInstanceId")].toString()); // 流程实例id
					} else {
						throw new SystemException("必须查询processInstanceId参数!");
					}
					if (itemsList.indexOf("businessName") != -1) {
						bean.setBusinessName(objs[itemsList
								.indexOf("businessName")].toString()); // 业务标题
					}
					if (itemsList.indexOf("startUserName") != -1) {
						bean.setStartUserName(objs[itemsList
						                          .indexOf("startUserName")].toString()); // 业务标题
					}
					if (itemsList.indexOf("taskNodeName") != -1) {
						bean.setNodeName(objs[itemsList
						                          .indexOf("taskNodeName")].toString()); // 业务标题
						bean.setDeptName(FadapterSignDeptNodeName(bean.getNodeName(),signNodeNameSuffix));
					}
					if (itemsList.indexOf("processStartDate") != -1) {
						bean.setWorkflowStartDate((Date) objs[itemsList
								.indexOf("processStartDate")]); // 流程启动日期
					}
					list.add(bean);
				}
			} else {
				logger.info("/r/n/t/tERROR{@Y}:该用户没有配置权限！");
			}
		}
		if(list == null){//预防NullException
			list = new ArrayList<TaskBean>();
		}else{
			list = new ArrayList<TaskBean>(list);
		}
		return list;
	}
	/**
	 * sql查询字符串连接
	 * 
	 * @description
	 * @author 严建
	 * @param orgName
	 * @return
	 * @createTime Apr 9, 2012 2:16:22 PM
	 */
	private String adapterSignDeptNodeName(String orgName) {
		return DataBaseUtil.SqlConcat(orgName, "'"
				+ nodeService.getSignNodeNameSuffix() + "'");
	}
	private String FadapterSignDeptNodeName(String orgName,String signNodeNameSuffix){
		return orgName.substring(0, orgName.length()-signNodeNameSuffix.length());
	}
}
