package com.strongit.oa.component.formtemplate.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.component.formtemplate.LoadDataType;
import com.strongit.oa.component.formtemplate.model.FormGridBean;
import com.strongit.oa.util.PropertiesUtil;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public class FormGridDataHelper {

	private static Logger logger = LoggerFactory.getLogger(FormGridDataHelper.class);
	
	public static String FORM_GRID_COUNT = "count";
	
	public static String FORM_GRID_TABLENAME = "tableName";
	
	public static String FORM_GRID_PKFIELDNAME = "pkFieldName";
	
	public static String FORM_GRID_PREFIX = "_tip";													//提醒列字段后缀约定,名称为:字段名_tip
	
	public static String FORM_GRID_HREF_PREFIX = "_link";											//超链接列名后缀

	public static List<String> columns = new ArrayList<String>();
	
	public static String FORM_GRID_BUSINESS_COLUMN_RECV_TIMEOUT = "recvtimeout";					//业务表限时办理字段
	
	public static String FORM_GRID_SYSTEM_VAR_CURRENT_TASK_HANDLER = "currenttaskhandler";			//系统变量：当前任务处理人
	
	public static String FORM_GRID_SYSTEM_VAR_PREV_TASK_HANDLER = "prevtaskhandler";				//系统变量：上一任务处理人
	
	public static String FORM_GRID_SYSTEM_VAR_PROCESS_STATE = "processstate";						//系统变量：流程是否办毕
	
	public static String FORM_GRID_SYSTEM_VAR_TASK_STATE = "taskstate";								//系统变量：任务接收时间距离现在多长时间
	
	public static String FORM_GRID_SYSTEM_VAR_IS_BACKSPACE = "isbackspace";							//系统变量：是否是退文（图标）
	
	public static List<String> systemVar = new ArrayList<String>();									//系统变量列表
	
	private static Properties prop = new Properties();
	
	private static String basePath = null;
	
	public static Properties getColorSetProperties(){
		return prop;
	}
	
	static {
		try {
			prop = PropertiesUtil.getPropertiesWithFileName("colorSet.properties");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		basePath = getHttpBasePath();
		
		systemVar.add(FORM_GRID_SYSTEM_VAR_CURRENT_TASK_HANDLER);
		systemVar.add(FORM_GRID_SYSTEM_VAR_PREV_TASK_HANDLER);
		systemVar.add(FORM_GRID_SYSTEM_VAR_PROCESS_STATE);
		systemVar.add(FORM_GRID_SYSTEM_VAR_TASK_STATE);
		systemVar.add(FORM_GRID_SYSTEM_VAR_IS_BACKSPACE);
		
		columns.add("processInstanceId");						//流程实例id
		columns.add("processName");								//流程名称
		columns.add("processDefinitionId");						//流程定义id
		columns.add("startUserName");							//发起人名称
		columns.add("startUserId");								//发起人Id
		columns.add("processStartDate");						//流程启动时间
		columns.add("processEndDate");							//流程结束时间
		columns.add("businessName");							//业务名称
		columns.add("processTypeName");							//流程类型名称
		columns.add("processTypeId");							//流程类型id
		columns.add("processMainFormId");						//流程对应主表单Id
		columns.add("processMainFormBusinessId");				//流程对应主表单业务数据标识
		columns.add("processSuspend");							//流程是否被挂起
		columns.add("processTimeout");							//流程是否超时
		columns.add("taskId");									//任务实例Id
		columns.add("taskName");								//任务名称
		columns.add("taskNodeId");								//任务节点Id
		columns.add("taskNodeName");							//任务节点名称
		columns.add("taskStartDate");							//任务开始时间
		columns.add("taskEndDate");								//任务结束时间
		columns.add("isBackspace");								//任务是否是回退任务(“0”：否；“1”：是)
		columns.add("isReceived");								//任务是否被签收(“0”：否；“1”：是)
		columns.add("taskFormId");								//任务节点对应表单Id
		columns.add("taskFormBusinessId");						//任务节点对应表单业务数据标识
		columns.add("taskSuspend");								//任务是否被挂起(false：否；true：是)
		columns.add("assignHandlerName");						//委托/指派人名称(必须同时查询assignType属性)
		columns.add("toAssignHandlerName");						//被委托/指派人名称(必须同时查询assignType属性)
		columns.add("assignHandlerId");							//委托/指派人Id(必须同时查询assignType属性)
		columns.add("toAssignHandlerId");						//被委托/指派人Id(必须同时查询assignType属性)
		columns.add("assignType");								//委托类型(“0”：委托；“1”：指派)
		columns.add("isHandlerNotExist");						//任务分配的处理者是否已不存在(“0”：否；“1”：是)
	}

	/**
	 * 生成列表组件需要的格式文件
	 * @param page				分页对象
	 * page.order				存储业务表名称
	 * @return
	 */
	public static FormGridBean generateFormGridData(Page<Map<String, String>> page) {
		FormGridBean bean = new FormGridBean();
		List<Map<String, String>> rsList = new ArrayList<Map<String,String>>();
		Map<String, String> rsAttrib = new HashMap<String, String>();
		if(page != null) {
			//定义返回记录总数
			rsAttrib.put(FORM_GRID_COUNT, String.valueOf(page.getTotalCount()));
			String info = page.getOrderBy();
			String name = "";
			String id = "";
			if(info != null && info.length() > 0) {
				if(info.indexOf(",")!=-1) {
					String[] infos = info.split(",");
					name = infos[0];
					id = infos[1];
				} else {
					id = info;
				}
			}
			rsAttrib.put(FORM_GRID_TABLENAME, name);
			rsAttrib.put(FORM_GRID_PKFIELDNAME, id);
			rsList = page.getResult();
		}
		bean.setRsAttrib(rsAttrib);
		bean.setRsList(rsList);
		return bean;
	}

	private static String generateHttpLink(LoadDataType module,Object[] objs,List<String> columnList,Page page) {
		String href = null;
		if (module == LoadDataType.LoadDataHosted) {
			///senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+overDate
			String instanceId = null;
			String state = null;
			if(columnList.indexOf("processInstanceId")!=-1) {
				instanceId = objs[columnList.indexOf("processInstanceId")].toString();
			}
			if(columnList.indexOf("processEndDate") !=-1) {
				state = objs[columnList.indexOf("processEndDate")] == null ? "" : "1";
			}
			href = "/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+state;
		} else if (module == LoadDataType.LoadDataTodo) {
			///senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId
			String instanceId = null;
			String taskId = null;
			if(columnList.indexOf("processInstanceId")!=-1) {
				instanceId = objs[columnList.indexOf("processInstanceId")].toString();
			}
			if(columnList.indexOf("taskId") !=-1) {
				taskId = objs[columnList.indexOf("taskId")].toString();
			}
			href = "/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId;
		} else if (module == LoadDataType.LoadDataProcessed) {
			///senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId
			String instanceId = null;
			String taskId = null;
			if(columnList.indexOf("processInstanceId")!=-1) {
				instanceId = objs[columnList.indexOf("processInstanceId")].toString();
			}
			if(columnList.indexOf("taskId") !=-1) {
				taskId = objs[columnList.indexOf("taskId")].toString();
			}
			href = "/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId;
		} else if(module == LoadDataType.LoadDataDraft) {
			HttpServletRequest request = ServletActionContext.getRequest();
			String workflowName = null;
			String tableName = null;
			String formId = null;
			if(request != null) {
				workflowName = request.getParameter("workflowName");
				tableName = request.getParameter("tableName");
				formId = request.getParameter("formId");
				if(workflowName != null && workflowName.length() > 0) {
					try {
						workflowName = URLEncoder.encode(workflowName, "utf-8");
					} catch (UnsupportedEncodingException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}	
			href = "/senddoc/sendDoc!input.action?formId="+formId+"&workflowName="+workflowName
						+"&pkFieldValue"+objs[columnList.indexOf(page.getOrderBy())]
						                      +"&tableName"+tableName;
		}
		return href;
	}

	/**
	 * 处理Page对象内部数据结构,返回电子表单数据能解析的格式数据.
	 * @param page						分页对象
	 * @param columnList				查询的字段名称列表,与Page内部返回的字段值一直.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Page generateFormGridData(Page page,List<String> workflowColumns,String customSelectItems,
										    List<String> systemVarColumn,IWorkflowService workflowService,LoadDataType module) {
		List<String> columnList = new ArrayList<String>();
		if(workflowColumns != null && !workflowColumns.isEmpty()) {
			for(String column : workflowColumns) {
				columnList.add(column);
			}
		}
		if(customSelectItems != null && customSelectItems.length() > 0) {
			String[] items = customSelectItems.split(",");
			for(String item : items) {
				columnList.add(item);
			}
		}
		List result = page.getResult();
		SimpleDateFormat sdf = null;
		List<Map<String,String>> rows = new ArrayList<Map<String,String>>();
		if(result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = (Object[]) result.get(i);
				Map<String, String> row = new HashMap<String, String>();
				for (int j = 0; j < columnList.size(); j++) {
					String column = columnList.get(j);
					Object value = objs[columnList.indexOf(column)];
					if (value != null && value instanceof Date) {
						if (sdf == null) {
							sdf = new SimpleDateFormat("yyyy-MM-dd");
						}
						String strValue = sdf.format((Date) value);
						row.put(column, strValue);
					} else {
						row.put(column, value == null ? "" : value.toString());
					}
					String url = generateHttpLink(module, objs, columnList,page);
					handlerMapRow(row, column, value,objs, columnList,url);
				}
				//处理系统变量字段
				if(systemVarColumn != null && !systemVarColumn.isEmpty()) {
					for (String field : systemVarColumn) {
						if (FORM_GRID_SYSTEM_VAR_CURRENT_TASK_HANDLER.equalsIgnoreCase(field)) {
							// 当前任务处理人
							if(columnList.contains("processInstanceId")) {
								String instanceId = objs[columnList.indexOf("processInstanceId")].toString();
								TaskBean taskBean = workflowService.getCurrentTaskHandle(instanceId);
								row.put(field, taskBean.getActor());
							}
						}
						if(FORM_GRID_SYSTEM_VAR_PREV_TASK_HANDLER.equalsIgnoreCase(field)) {
							if(columnList.contains("taskId")) {
								//上一任务处理人信息
								String taskInstanceId = objs[columnList.indexOf("taskId")].toString();
								TaskBean taskBean = workflowService.getPrevTaskHandler(taskInstanceId);
								row.put(field, taskBean.getPrevActor());
							}
						}
					}
				}
				rows.add(row);
			}
		}
		page.setResult(rows);
		return page;
	}

	private static String getHttpBasePath() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String basePath = "";
		if(request != null) {
			String path = request.getContextPath();
			if(path.endsWith("/")){
				path="";
			}
			basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
		}
		return basePath;
	}
	
	private static void handlerMapRow(Map<String, String> row,String column,Object value,
			Object[] objs,List<String> columnList,String url) throws SystemException {
		if(column.equals("processEndDate")) {
			//处理流程办理状态
			if(value != null) {
				row.put(FORM_GRID_SYSTEM_VAR_PROCESS_STATE, "办毕");
			} else {
				row.put(FORM_GRID_SYSTEM_VAR_PROCESS_STATE, "在办");
			}
		} else if("taskStartDate".equals(column)) {
			if(value != null) {
				Date startDate = (Date)value;
				long time = new Date().getTime() - startDate.getTime();
				long hours = time / (1000 * 60 * 60);
				String picPath = null;
				String titleShow = null;
				if (hours <= 72) {// 在3天内
					picPath = prop.getProperty("green");
					titleShow = "3天内办理";
				} else if (hours <= 144) {// 6天内办理
					picPath = prop.getProperty("yellow");
					titleShow = "6天内办理";
				} else {// 超过7天办理
					picPath = prop.getProperty("red");
					titleShow = "超过7天办理";
				}
				row.put(FORM_GRID_SYSTEM_VAR_TASK_STATE, basePath + picPath);
				handlerMapRowTip(row, FORM_GRID_SYSTEM_VAR_TASK_STATE, titleShow);
			}
		} else if("isBackspace".equals(column)) {
			if("1".equals(value)) {
				row.put(FORM_GRID_SYSTEM_VAR_IS_BACKSPACE, basePath + prop.getProperty("back"));				
			}
		}
		/*else if(FORM_GRID_BUSINESS_COLUMN_RECV_TIMEOUT.equals(column)) {
			row.put(FORM_GRID_SYSTEM_VAR_IS_BACKSPACE, basePath + prop.getProperty("clock"));	
			Object processStartDate = objs[columnList.indexOf("processStartDate")];//得到流程启动时间
			String titleShow = null;
			handlerMapRowTip(row, FORM_GRID_SYSTEM_VAR_TASK_STATE, titleShow);
		}*/
		//增加列上的提示信息
		//仅增加标题列上的提示
		if(BaseWorkflowManager.WORKFLOW_TITLE.equalsIgnoreCase(column) || "businessName".equals(column)) {
			handlerMapRowTip(row, column, value);
			
		}
		//在标题列上增加链接
		if(BaseWorkflowManager.WORKFLOW_TITLE.equalsIgnoreCase(column)) {
			handlerMapRowLink(row, column, url);
		}
		if("businessName".equalsIgnoreCase(column)) {
			handlerMapRowLink(row, column, url);
		}
	}
	
	private static void handlerMapRowTip(Map<String, String> row,String column,Object value) {
		row.put(column + FORM_GRID_PREFIX, value == null ? "" : value.toString());
	}

	private static void handlerMapRowLink(Map<String,String> row,String column,String url) {
		String strUrl = null;
		if(url != null) {
			BASE64Encoder base64 = new BASE64Encoder();
			strUrl = base64.encode(url.getBytes());
			logger.info(url);
		}
		row.put(column + FORM_GRID_HREF_PREFIX, getHttpBasePath() + "/fileNameRedirectAction.action?toPage=component/formdata/formGridData-target.jsp&url=" + strUrl);
	}
	
	/**
	 * 处理业务查询字段,对工作流字段、业务字段、系统变量进行归类.
	 * @param workflowColumn						工作流字段列表
	 * @param systemVarColumn						系统变量字段列表
	 * @param businessColumn						业务字段列表（多个字段以逗号分隔）
	 * @param formColumns							前台传递近来的字段集合（多个字段以逗号分隔）
	 */
	public static void handlerColumn(List<String> workflowColumn,List<String> systemVarColumn,StringBuilder businessColumn,String formColumns) {
		List<String> formWorkflowColumns = FormGridDataHelper.columns;// 表单中绑定的工作流字段列表
		String[] columns = formColumns.split(",");
		for (String column : columns) {
			if (formWorkflowColumns.contains(column)) {
				workflowColumn.add(column);
			} else if(FormGridDataHelper.systemVar.contains(column)) {
				systemVarColumn.add(column);
				if(FormGridDataHelper.FORM_GRID_SYSTEM_VAR_PROCESS_STATE.equalsIgnoreCase(column)) {
					if(!workflowColumn.contains("processEndDate")) {
						workflowColumn.add("processEndDate");
					}
				}
				if(FormGridDataHelper.FORM_GRID_SYSTEM_VAR_TASK_STATE.equalsIgnoreCase(column)) {
					if(!workflowColumn.contains("taskStartDate")) {
						workflowColumn.add("taskStartDate");
					}
				}
				if(FormGridDataHelper.FORM_GRID_SYSTEM_VAR_IS_BACKSPACE.equalsIgnoreCase(column)) {
					if(!workflowColumn.contains("isBackspace")) {
						workflowColumn.add("isBackspace");
					}
				}
			} else {
				/*if(FORM_GRID_BUSINESS_COLUMN_RECV_TIMEOUT.equals(column)) {
					if(!workflowColumn.contains("processStartDate")) {
						workflowColumn.add("processStartDate");
					}
				}*/
				businessColumn.append(column).append(",");
			}
		}
	}
	
}
