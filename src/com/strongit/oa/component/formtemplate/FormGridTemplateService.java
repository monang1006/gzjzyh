package com.strongit.oa.component.formtemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.component.formtemplate.util.FormGridDataHelper;
import com.strongit.oa.component.jdbc.JdbcSplitService;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
public class FormGridTemplateService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IWorkflowService workflowService; // 工作流服务接口

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SendDocManager manager;
	
	@Autowired
	private JdbcSplitService jdbcSplitService;

	@SuppressWarnings("unchecked")
	public Page<Map<String, String>> loadProcessedData(int pageNo,
			int pageSize, String userId, TaskBean model, String tableName,
			String formColumns, String type) {
		if (formColumns == null || formColumns.length() == 0) {
			throw new SystemException("参数formColumns不可为空！");
		}
		Page page = new Page(pageSize, true);
		page.setPageNo(pageNo);
		StringBuilder businessColumn = new StringBuilder(); // 业务表字段列表
		List<String> workflowColumn = new ArrayList<String>(); // 工作流字段列表
		workflowColumn.add("processInstanceId"); // 默认查询流程实例id
		List<String> systemVarColumn = new ArrayList<String>(); // 系统变量字段列表
		FormGridDataHelper.handlerColumn(workflowColumn, systemVarColumn, businessColumn,formColumns);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		//paramsMap.put("hasHandlerId", userId);
		// 按流程类型查找
		String workflowType = model.getWorkflowType();
		if (workflowType != null && workflowType.length() > 0) {
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for (String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
		// 按流程名称查找
		if (model.getWorkflowName() != null
				&& !"".equals(model.getWorkflowName())) {
			paramsMap.put("processName", model.getWorkflowName());
		}
		// 按流程状态查找
		if (model.getWorkflowStatus() != null
				&& !"".equals(model.getWorkflowStatus())
				&& !"2".equals(model.getWorkflowStatus())) {
			paramsMap.put("processStatus", model.getWorkflowStatus());
		}
		// 按流程启动时间降序显示
		Map orderMap = new HashMap<Object, Object>();
		if (SortConst.SORT_TYPE_TASKENDDATE_ASC.equals(model.getSortType())) {
			orderMap.put("processStartDate", "0");
		} else if (SortConst.SORT_TYPE_PROCESSSTARTDATE_ASC.equals(model.getSortType())) {
			orderMap.put("processStartDate", "0");
		} else if (SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC.equals(model.getSortType())) {
			orderMap.put("processStartDate", "1");
		} else {
			orderMap.put("processStartDate", "1");
		}
		List<Object> customValues = null;
		String customSelectItems = null;
		String customFromItems = null;
		String customQuery = null;
		String queryWithTaskDate = "";//按任务结束时间区间范围查询
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(model.getTaskStartDateStart() != null) {
			queryWithTaskDate = " and ti.START_  >= TO_DATE('"+sdf.format(model.getTaskStartDateStart())+"', 'YYYY-MM-DD HH24:MI')";
		}
		if(model.getTaskStartDateEnd() != null) {
			queryWithTaskDate = " and ti.START_  <= TO_DATE('"+sdf.format(model.getTaskStartDateEnd())+"', 'YYYY-MM-DD HH24:MI')";
		}
		if(model.getTaskEndDateStart() != null) {
			queryWithTaskDate = " and ti.END_  >= TO_DATE('"+sdf.format(model.getTaskEndDateStart())+"', 'YYYY-MM-DD HH24:MI')";
		}
		if(model.getTaskEndDateEnd() != null) {
			queryWithTaskDate = " and ti.END_  <= TO_DATE('"+sdf.format(model.getTaskEndDateEnd())+"', 'YYYY-MM-DD HH24:MI')";
		}
		if("notsign".equals(type)) {
			//已办事宜需要过滤待签收、待办任务(办公厅项目)
			/*customFromItems = "(select ti.procinst_ as instanceId,max(ti.END_) as d from JBPM_TASKINSTANCE ti" +
			" where ti.ACTORID_='"+userId+"' and ti.ISRECEIVE_ = '1'" +
			" and ti.node_id_ not in(select tnplugin.nsp_nodeid from T_WF_BASE_NODESETTINGPLUGIN tnplugin 
			where tnplugin.NSP_PLUGINCLOBVALUE like 1 and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')" +
			" and ti.ISCANCELLED_ = 0 group by ti.procinst_ "+orderBy+") p";
			customQuery = "p.instanceId=@processInstanceId";*/
			//未签收的办结任务对应的流程实例
			customQuery = " exists(select ti.ID_ from JBPM_TASKINSTANCE ti,T_WF_BASE_NODESETTINGPLUGIN tnplugin where " +
					" ti.ISOPEN_ = 0 "+queryWithTaskDate+" and ti.NODE_ID_=tnplugin.nsp_nodeid and ti.END_ is not null and ti.ISCANCELLED_ = 0" +
					" and ti.ACTORID_='"+userId+"'" +
					" and ti.PROCINST_ = @processInstanceId and tnplugin.NSP_PLUGINCLOBVALUE not like 1" +
					" and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')" ;
			//过滤待办任务对应的流程实例
			customQuery += " and not exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISOPEN_ = 1 " +
					"and ti.ACTORID_='"+userId+"' and ti.PROCINST_ = @processInstanceId) " ;
		} else {
			//查询出办结的任务对应的流程实例
			customQuery = " exists(select ti.ID_ from JBPM_TASKINSTANCE ti where " +
				"ti.ISOPEN_ = 0 and ti.END_ is not null "+queryWithTaskDate+" and ti.ISCANCELLED_ = 0 and ti.ACTORID_='"+userId+"'" +
				" and ti.PROCINST_ = @processInstanceId)" ;
		}
		if (businessColumn.length() > 0 && tableName != null
				&& tableName.length() > 0) {// 当参数中传入了业务表的字段时
			String pkFieldName = manager.getPrimaryKeyName(tableName);
			businessColumn.deleteCharAt(businessColumn.length() - 1);
			customSelectItems =  businessColumn.toString();
			customFromItems = tableName + " oatable";
			if(customQuery != null) {
				customQuery += " and " + "@businessId='" + tableName + ";" + pkFieldName
						+ ";' || oatable." + pkFieldName;
			} else {
				customQuery = "@businessId='" + tableName + ";" + pkFieldName
							+ ";' || oatable." + pkFieldName;
			}
		} 
		logger.info(customFromItems + "\n" + customQuery);
		page = workflowService.getProcessInstanceByConditionForPage(page,
				workflowColumn, paramsMap, orderMap, customSelectItems,
				customFromItems, customQuery, customValues, null);
		List<Object[]> result = page.getResult();
		if(customSelectItems == null) {
			customSelectItems = "";
		} else {
			customSelectItems = customSelectItems + ",";
		}
		customSelectItems = customSelectItems + "taskStartDate,taskId";
		if(workflowColumn.contains("taskStartDate")) {
			workflowColumn.remove("taskStartDate");
		}
		if(workflowColumn.contains("taskId")) {
			workflowColumn.remove("taskId");
		}
		if(result != null && !result.isEmpty()) {
			StringBuilder params = new StringBuilder();
			for(Object[] objs : result) {
				//流程实例列表
				params.append(objs[workflowColumn.indexOf("processInstanceId")].toString()).append(",");
			}
			if(params.length() > 0) {
				params.deleteCharAt(params.length() - 1);
				StringBuilder sql = new StringBuilder("select ti.procinst_,ti.id_ as taskId,ti.start_ as taskStartDate ");
				sql.append("from JBPM_TASKINSTANCE ti where ti.procinst_ in(");
				sql.append(params.toString());
				sql.append(") and ti.END_ is not null order by ti.END_ desc");
				List<Map<String, Object>> taskList = jdbcTemplate.queryForList(sql.toString());
				logger.info(sql.toString());
				if(taskList != null && !taskList.isEmpty()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for(Map<String,Object> rsMap : taskList) {
						if(!map.containsKey(rsMap.get("procinst_"))) {
							for(int i=0;i<result.size();i++) {
								Object[] objs = result.get(i);
								if(objs[workflowColumn.indexOf("processInstanceId")].equals(rsMap.get("procinst_"))) {
									objs = ObjectUtils.addObjectToArray(objs, rsMap.get("taskStartDate"));
									objs = ObjectUtils.addObjectToArray(objs, rsMap.get("taskId"));
									result.set(i, objs);
									break;
								}
							}
							map.put(rsMap.get("procinst_").toString(), null);
						}
					}
				}
			}
		}
		page.setOrderBy("processInstanceId"); // 流程实例id作为主键
		FormGridDataHelper.generateFormGridData(page, workflowColumn,customSelectItems,systemVarColumn,workflowService,LoadDataType.LoadDataProcessed);
		return page;
	}

	/**
	 * 得到待办任务分页列表.不支持按业务表字段查询.支持按业务字段显示（在表单中配置字段）
	 * 默认查询数据：业务数据主键（processMainFormBusinessId）、流程实例（processInstanceId）
	 * 、任务实例（taskId）、任务节点id（taskNodeId） 支持按是否是签收任务分类查询.
	 * <p>
	 * 支持按工作流中的字段查询：业务标题、流程类型（多个流程类型逗号隔开）、流程名称、 拟稿人名称、任务接收时间范围。
	 * </p>
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示的记录数
	 * @param userId
	 *            用户id
	 * @param model
	 *            业务对象（查询）
	 * @param tableName
	 *            业务表名称（例如查询收发文、大小写敏感）
	 * @param formColumns
	 *            需要查询的字段（多个字段以逗号分开，在表单中配置）
	 * @param type
	 *            任务类型（notsign：签收节点上的任务，sign：非签收节点上的任务,传空值时查询所有待办任务）
	 * @return Page<Map<String,String>> 内部数据结构：
	 *         <p>
	 *         Map<字段名称,字段值> Page.orderBy:任务实例 Page.totalCount:总记录数
	 *         </p>
	 */
	@SuppressWarnings("unchecked")
	public Page<Map<String, String>> loadTodoDataForPage(int pageNo,
			int pageSize, String userId, TaskBean model, String tableName,
			String formColumns, String type) {
		if (formColumns == null || formColumns.length() == 0) {
			throw new SystemException("参数formColumns不可为空！");
		}
		Page page = new Page(pageSize, true);
		page.setPageNo(pageNo);
		StringBuilder businessColumn = new StringBuilder(); // 业务表字段列表
		List<String> workflowColumn = new ArrayList<String>(); // 工作流字段列表
		List<String> systemVarColumn = new ArrayList<String>(); // 系统变量字段列表
		workflowColumn.add("processInstanceId"); // 默认查询流程实例id
		workflowColumn.add("taskId"); // 默认查询任实例id processName
		FormGridDataHelper.handlerColumn(workflowColumn, systemVarColumn, businessColumn,formColumns);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "2");// 取非办结任务
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		paramsMap.put("handlerId", userId);// 任务处理人
		// 按流程类型查找
		String workflowType = model.getWorkflowType();
		if (workflowType != null && workflowType.length() > 0) {
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for (String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
		// 按流程名称查找
		if (model.getWorkflowName() != null
				&& !"".equals(model.getWorkflowName())) {
			paramsMap.put("processName", model.getWorkflowName());
		}
		// 按拟稿人查询
		if (model.getStartUserName() != null
				&& model.getStartUserName().length() > 0) {
			paramsMap.put("startUserName", model.getStartUserName());
		}
		// 任务开始时间上限
		if (model.getTaskStartDate() != null) {
			paramsMap.put("taskStartDateStart", model.getTaskStartDate());
		}
		// 任务开始时间下限
		if (model.getTaskEndDate() != null) {
			paramsMap.put("taskStartDateEnd", model.getTaskEndDate());
		}
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("taskStartDate", "1");
		List<Object> customValues = null;
		String customQuery = null;
		String customFromItems = null;
		String customSelectItems = null;
		if (businessColumn.length() > 0 && tableName != null
				&& tableName.length() > 0) {// 当参数中传入了业务表的字段时
			String pkFieldName = manager.getPrimaryKeyName(tableName);
			businessColumn.deleteCharAt(businessColumn.length() - 1);
			customSelectItems = businessColumn.toString();
			customFromItems = tableName + " oatable";
			customQuery = "@businessId='" + tableName + ";" + pkFieldName
					+ ";' || oatable." + pkFieldName;
		}
		//type = "notsign";
		if ("notsign".equals(type)) {// 查询未签收的任务
			if(customQuery.indexOf("@businessId")!=-1) {
				customFromItems += ",T_WF_BASE_NODESETTINGPLUGIN tnplugin";
				customQuery += " and tnplugin.NSP_NODEID=ti.NODE_ID_ and tnplugin.NSP_PLUGINCLOBVALUE like 1 and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion'";
			} else {
				customFromItems = "T_WF_BASE_NODESETTINGPLUGIN tnplugin";
				customQuery = "tnplugin.NSP_NODEID=ti.NODE_ID_ and tnplugin.NSP_PLUGINCLOBVALUE like 1 and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion'";
			}
		} else if ("sign".equals(type)) {// 查询已签收的任务
			if(customQuery.indexOf("@businessId")!=-1) {
				customQuery += " and not exists(select tnplugin.NSP_NODEID from T_WF_BASE_NODESETTINGPLUGIN tnplugin where" +
						" tnplugin.NSP_NODEID=ti.NODE_ID_ and tnplugin.NSP_PLUGINCLOBVALUE" +
						" like 1 and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')";
			} else {
				customQuery += " not exists(select tnplugin.NSP_NODEID from T_WF_BASE_NODESETTINGPLUGIN tnplugin where" +
						" tnplugin.NSP_NODEID=ti.NODE_ID_ and tnplugin.NSP_PLUGINCLOBVALUE" +
						" like 1 and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')";			}
		}
		page = workflowService.getTaskInfosByConditionForPage(page,
				workflowColumn, paramsMap, orderMap, customSelectItems,
				customFromItems, customQuery, customValues, null);
		page.setOrderBy("taskId"); // 任务实例id作为主键
		FormGridDataHelper.generateFormGridData(page, workflowColumn,customSelectItems,systemVarColumn,workflowService,LoadDataType.LoadDataTodo);
		return page;
	}


	public Page<Map<String, String>> loadData(int pageNo, int pageSize,
			String userId, TaskBean model, String tableName,
			String formColumns, LoadDataType loadType, String type) {
		if (loadType == LoadDataType.LoadDataHosted) {
			return this.loadHostbyData(pageNo, pageSize, userId, model,
					tableName, formColumns);
		} else if (loadType == LoadDataType.LoadDataTodo) {
			return this.loadTodoDataForPage(pageNo, pageSize, userId, model,
					tableName, formColumns, type);
		} else if (loadType == LoadDataType.LoadDataProcessed) {
			return this.loadProcessedData(pageNo, pageSize, userId, model,
					tableName, formColumns, type);
		} else if(loadType == LoadDataType.LoadDataDraft) {
			return this.loadDraftData(pageNo, pageSize, userId, model, tableName, formColumns);
		}

		return null;
	}

	/**
	 * 查询主办流程,如果不按业务表字段查询,则直接对流程数据分页,之后根据查询到的业务数据集合查询业务表字段.（以流程数据为中心）
	 * 如果需要按业务表字段查询，则需要访问业务所有数据以及工作流所有数据，之后根据找到的业务数据匹配.(以业务表数据为中心)
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示的记录数
	 * @param userId
	 *            用户id,这里一般为当前用户
	 * @param model
	 *            查询实体Bean
	 * @param tableName
	 *            业务表名称
	 * @param formColumns
	 *            表单中绑定的字段
	 * @return 返回Map格式的记录集合
	 */
	@SuppressWarnings("unchecked")
	public Page<Map<String, String>> loadHostbyData(int pageNo, int pageSize,
			String userId, TaskBean model, String tableName, String formColumns) {
		if (formColumns == null || formColumns.length() == 0) {
			throw new SystemException("参数formColumns不可为空！");
		}
		StringBuilder businessColumn = new StringBuilder(); // 业务表字段列表
		List<String> workflowColumn = new ArrayList<String>(); // 工作流字段列表
		List<String> systemVarColumn = new ArrayList<String>(); // 系统变量字段列表
		workflowColumn.add("processInstanceId"); // 默认查询流程实例id
		FormGridDataHelper.handlerColumn(workflowColumn, systemVarColumn, businessColumn,formColumns);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("startUserId", userId);// 主办人员
		Page page = new Page(pageSize, true);
		page.setPageNo(pageNo);
		// 按流程类型查找
		String workflowType = model.getWorkflowType();
		if (workflowType != null && workflowType.length() > 0) {
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for (String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
		// 按流程名称查找
		if (model.getWorkflowName() != null
				&& !"".equals(model.getWorkflowName())) {
			paramsMap.put("processName", model.getWorkflowName());
		}
		// 按流程状态查找
		if (model.getWorkflowStatus() != null
				&& !"".equals(model.getWorkflowStatus())
				&& !"2".equals(model.getWorkflowStatus())) {
			paramsMap.put("processStatus", model.getWorkflowStatus());
		}
		// 按流程启动时间降序显示
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processStartDate", "1");
		List<Object> customValues = null;
		String customQuery = null;
		String customFromItems = null;
		String customSelectItems = null;
		if (businessColumn.length() > 0 && tableName != null
				&& tableName.length() > 0) {// 当参数中传入了业务表的字段时
			String pkFieldName = manager.getPrimaryKeyName(tableName);
			businessColumn.deleteCharAt(businessColumn.length() - 1);
			customSelectItems = businessColumn.toString();
			customFromItems = tableName + " oatable";
			customQuery = "@businessId='" + tableName + ";" + pkFieldName
					+ ";' || oatable." + pkFieldName;
		}
		page = workflowService.getProcessInstanceByConditionForPage(
				page, workflowColumn, paramsMap, orderMap, customSelectItems,
				customFromItems, customQuery, customValues, null);
		
		page.setOrderBy("processInstanceId"); // 流程实例id作为主键
		FormGridDataHelper.generateFormGridData(page, workflowColumn,customSelectItems,systemVarColumn,workflowService,LoadDataType.LoadDataHosted);
		return page;
	}

	/**
	 * 得到草稿业务数据列表
	 * @param pageNo							页码
	 * @param pageSize							每页显示记录数
	 * @param userId							用户id
	 * @param model								业务查询实体
	 * @param tableName							业务表名
	 * @param formColumns						业务字段集合（多个字段以逗号隔开）
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public Page<Map<String, String>> loadDraftData(int pageNo, int pageSize,
			String userId, TaskBean model, String tableName, String formColumns) throws SystemException {
		if (formColumns == null || formColumns.length() == 0) {
			throw new SystemException("参数formColumns不可为空！");
		}
		if(tableName == null || tableName.length() == 0) {
			throw new SystemException("参数tableName不可为空！");
		}
		Page page = new Page(pageSize, true);
		page.setPageNo(pageNo);
		String pkFieldName = manager.getPrimaryKeyName(tableName);
		formColumns = pkFieldName + "," + formColumns;
		StringBuilder sql = new StringBuilder("select ").append(formColumns).append(" from ").append(tableName);
		sql.append(" where ").append(BaseWorkflowManager.WORKFLOW_AUTHOR).append("='").append(userId).append("'");
		sql.append(" and ").append(BaseWorkflowManager.WORKFLOW_STATE).append("=0");
		List<Object> paramsLst = new ArrayList<Object>();
		String orderBy = null;
		page = jdbcSplitService.excuteSqlForPage(page, sql.toString(), paramsLst, orderBy);
		page.setOrderBy(pkFieldName);
		FormGridDataHelper.generateFormGridData(page, null,formColumns,null,workflowService,LoadDataType.LoadDataDraft);
		return page;
	}
}