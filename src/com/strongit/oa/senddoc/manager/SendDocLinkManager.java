package com.strongit.oa.senddoc.manager;

import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.comparator.BaseComparator;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.senddoc.bo.ParamBean;
import com.strongit.oa.senddoc.comparator.SendDocComparator;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 链接管理 Manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Dec 20, 2011 3:47:40 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SendDocLinkManager
 */
@Service
@Transactional
@OALogger
public class SendDocLinkManager {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SendDocManager manager;

	@Autowired
	protected IUserService userService;// 统一用户服务

	@Autowired
	protected IWorkflowService workflow; // 工作流服务
	
	@Autowired
	SessionFactory sessionFactory; // 提供session

	/**
	 * 生成代办流程类型别名链接
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 3:32:47 PM
	 * @return StringBuilder
	 */
	public StringBuilder genTodoWorkflowNameLink(String workflowName,
			String formId, String workflowType, String type, String rootPath,
			Map<String, StringBuilder> workflowNameLinkMap)
			throws DAOException, ServiceException, SystemException {
		try {
			if (workflowName == null || "".equals(workflowName)) {
				logger.error("参数workflowName不能为空或空字符串");
				throw new ServiceException("参数workflowName不能为空或空字符串");
			}
			if (formId == null || "".equals(formId)) {
				logger.error("参数formId不能为空或空字符串");
				throw new ServiceException("参数formId不能为空或空字符串");
			}
			if (workflowType == null || "".equals(workflowType)) {
				logger.error("参数workflowType不能为空或空字符串");
				throw new ServiceException("参数workflowType不能为空或空字符串");
			}
			StringBuilder clickProcessType = new StringBuilder();
			// 流程名称(别名)
			String temp1 = manager.getWorkflowNamesByAliaName(workflowName);
			//temp1出现如下情况，一般是因该流程无别名导致
			if(temp1 == null || "".equals(temp1)){
				temp1 = workflowName;
			}
			// 如果该【流程别名】对应多个【流程名称】
			if(temp1 != null && temp1.indexOf(",")>-1){
				String clickProcessUrl = rootPath
				+ "/senddoc/sendDoc!todoWorkflow.action?type=sign&workflowName="
				+ URLEncoder.encode(URLEncoder.encode(temp1, "utf-8"), "utf-8");
				clickProcessType.append("window.parent.refreshWorkByTitle('"
						+ clickProcessUrl
						+ "','"
						+ (type != null && type.equals("notsign") ? "待签收文件"
								: "待办文件") + "');");
			}else{
				String currworkflowName = URLEncoder.encode(URLEncoder.encode(
						temp1, "utf-8"), "utf-8");
				// 获取表单id
				String[] nodeInfo = formId.split(",");
				formId = nodeInfo[nodeInfo.length - 1];
				// 流程类别
				String currworkflowType = workflowType;
				String clickProcessUrl = rootPath
				+ "/senddoc/sendDoc!todo.action?type=" + type
				+ "&workflowName=" + currworkflowName 
				+ "&formId=" + formId
				+ "&workflowType=" + currworkflowType;
				clickProcessType.append("window.parent.refreshWorkByTitle('"
						+ clickProcessUrl
						+ "','"
						+ (type != null && type.equals("notsign") ? "待签收文件"
								: "待办文件") + "');");				
			}

			return clickProcessType;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 生成已办流程类型别名链接
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 3:32:47 PM
	 * @return StringBuilder
	 */
	public StringBuilder genProcessedWorkflowNameLink(String workflowName,
			String formId, String workflowType, String processStatus,
			String rootPath, String filterSign) throws DAOException,
			ServiceException, SystemException {
		try {
			if (workflowName == null || "".equals(workflowName)) {
				logger.error("参数workflowName不能为空或空字符串");
				throw new ServiceException("参数workflowName不能为空或空字符串");
			}
			if (formId == null || "".equals(formId)) {
				logger.error("参数formId不能为空或空字符串");
				throw new ServiceException("参数formId不能为空或空字符串");
			}
			/*
			 * if (workflowType == null || "".equals(workflowType)) {
			 * logger.error("参数workflowType不能为空或空字符串"); throw new
			 * ServiceException("参数workflowType不能为空或空字符串"); }
			 */StringBuilder clickProcessType = new StringBuilder();
			String currworkflowName = URLEncoder.encode(URLEncoder.encode(
					workflowName, "utf-8"), "utf-8");
			// 获取表单id
			String[] nodeInfo = formId.split(",");
			formId = nodeInfo[nodeInfo.length - 1];
			// 流程类别
			String currworkflowType = workflowType;
			String clickProcessUrl = rootPath
					+ "/senddoc/sendDoc!processed.action" + "?state="
					+ processStatus + "&workflowName=" + currworkflowName
					+ "&filterSign=" + filterSign 
					+ "&formId=" + formId
					+ "&searchType=" + (processStatus.equals("1") ? "1" : "")
					+ "&workflowType=" + currworkflowType;

			clickProcessType
					.append("window.parent.refreshWorkByTitle('"
							+ clickProcessUrl
							+ "','"
							+ (processStatus != null
									&& processStatus.equals("1") ? "办结文件"
									: "在办文件") + "');");

			return clickProcessType;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 生成已办流程类型别名链接
	 * 
	 * @author 严建
	 * @param taskbusinessbean
	 * @param rootPath
	 * @param filterSign
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 16, 2012 6:59:26 PM
	 */
	public StringBuilder genProcessedWorkflowNameLink(
			TaskBusinessBean taskbusinessbean, String rootPath,
			String filterSign) throws DAOException, ServiceException,
			SystemException {
		try {
			return this.genProcessedWorkflowNameLink(taskbusinessbean
					.getWorkflowName(), taskbusinessbean.getFormId(),
					taskbusinessbean.getWorkflowType(), taskbusinessbean
							.getWorkflowStatus(), rootPath, filterSign);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 生成待办流程类型别名链接
	 * 
	 * @author 严建
	 * @param taskbusinessbean
	 * @param rootPath
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Feb 16, 2012 7:32:27 PM
	 */
	public StringBuilder genTodoWorkflowNameLink(
			TaskBusinessBean taskbusinessbean, String rootPath, String type)
			throws DAOException, ServiceException, SystemException {
		try {
			return this.genTodoWorkflowNameLink(taskbusinessbean
					.getWorkflowAliaName(), taskbusinessbean.getFormId(),
					taskbusinessbean.getWorkflowType(), type, rootPath, null);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 生成桌面在办文件排序
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 4:00:04 PM
	 * @return String
	 */
	public String genSortTypeLink(String blockId, String sortType)
			throws DAOException, ServiceException, SystemException {
		try {
			if (blockId == null || "".equals(blockId)) {
				logger.error("参数blockId不能为空或空字符串");
				// throw new ServiceException("参数blockId不能为空或空字符串");
			}
			if (sortType == null) {
				logger.error("参数sortType不能为空或空字符串");
				// throw new ServiceException("参数sortType不能为空或空字符串");
			}
			String sortHtml = "";
			String radioOnClick = "onclick=\""
					+ "document.getElementById('sortTypeHidden').value=this.value;"
					+ "resetDragContent('" + blockId + "');"
					+ "loadDragContent('" + blockId + "');\"";
			sortHtml = "<div style=\"float:left;\">来文时间排序："
					+ "<input name=\"sortType\" type=\"radio\" style=\"vertical-align:middle;\" id=\"processstartdate_desc\" value=\"processstartdate_desc\" "
					+ (sortType.equals("processstartdate_desc")
							? "checked=\"true\"" : "")
					+ " "
					+ radioOnClick
					+ "></input><label for=\"processstartdate_desc\">升序</label>"
					+ "<input name=\"sortType\" type=\"radio\" style=\"vertical-align:middle;\"  id=\"processstartdate_asc\" value=\"processstartdate_asc\" "
					+ (sortType.equals("processstartdate_asc") || sortType.equals("") ? "checked=\"true\""
							: "") + " " + radioOnClick
					+ "></input><label for=\"processstartdate_asc\">降序</label>"
					+ "</div>";
			return sortHtml;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 待办事宜列表 单选排序 (1)来文时间排序HTML (2)意见征询查询HTML
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 29, 2011 5:34:53 PM
	 * @return String
	 */
	public void processedGridViewSort(ParamBean parambean) throws DAOException,
			ServiceException, SystemException {
		try {
			if (parambean.getSortHtml() == null) {
				parambean.setSortHtml(new StringBuilder());
			}
			processedGridVieDatewSort(parambean.getSortType(), parambean
					.getSortHtml());
			if (parambean.getYjzxHtml() == null) {
				parambean.setYjzxHtml(new StringBuilder());
			}
			processedGridVieYJZX(parambean.getFilterYJZX(), parambean
					.getYjzxHtml());
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 来文时间排序HTML
	 * 
	 * @author 严建
	 * @param sortType
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Mar 13, 2012 10:24:58 AM
	 */
	public void processedGridVieDatewSort(String sortType,
			StringBuilder sortHtml) throws DAOException, ServiceException,
			SystemException {
		try {
			if (sortHtml == null) {
				sortHtml = new StringBuilder();
			}
			if (sortType == null) {
				logger.error("参数sortType不能为空或空字符串");
				throw new ServiceException("参数sortType不能为空或空字符串");
			}
			String radioOnClick = "onclick=\""
					+ "document.getElementById('sortTypeHidden').value=this.value;"
					+ "document.getElementById('myTableForm').submit();" + "\"";

			String sortHtmlBAK = "来文时间排序："
					+ "<input name=\"sortType\" type=\"radio\" style=\"vertical-align:middle;\" id=\"processstartdate_desc\" value=\"processstartdate_desc\" "
					+ (sortType.equals("processstartdate_desc")
							 ? "checked=\"true\"" : "")
					+ " "
					+ radioOnClick
					+ "></input><label for=\"processstartdate_desc\">升序</label>"
					+ "<input name=\"sortType\" type=\"radio\" style=\"vertical-align:middle;\"  id=\"processstartdate_asc\" value=\"processstartdate_asc\" "
					+ (sortType.equals("processstartdate_asc") || sortType.equals("")? "checked=\"true\""
							: "") + " " + radioOnClick
					+ "></input><label for=\"processstartdate_asc\">降序</label>"
					+ "";
			sortHtml.append(sortHtmlBAK);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 意见征询查询HTML
	 * 
	 * @author 严建
	 * @param filterYJZX
	 * @param sortHtml
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Mar 13, 2012 10:42:11 AM
	 */
	public void processedGridVieYJZX(String filterYJZX, StringBuilder sortHtml)
			throws DAOException, ServiceException, SystemException {
		try {
			if (sortHtml == null) {
				sortHtml = new StringBuilder();
			}
			if (filterYJZX == null) {
				filterYJZX = "";
			}
			String radioOnClick = "onclick=\""
					+ "document.getElementById('filterYJZXHidden').value=this.value;"
					+ "document.getElementById('myTableForm').submit();" + "\"";

			String sortHtmlBAK = "&nbsp;｜&nbsp;"
					+ "<input name=\"filterYJZX\" type=\"radio\" style=\"vertical-align:middle;\"  id=\"YJZX_ALL\" value=\"2\" "
					+ (filterYJZX.equals("2")  || filterYJZX.equals("")? "checked=\"true\"" : "")
					+ " "
					+ radioOnClick
					+ "></input><label for=\"YJZX_ALL\">全部</label>"
					+ "<input name=\"filterYJZX\" type=\"radio\" style=\"vertical-align:middle;\"  id=\"YJZX_TRUE\" value=\"1\" "
					+ (filterYJZX.equals("1") ? "checked=\"true\"" : "")
					+ " "
					+ radioOnClick
					+ "></input><label for=\"YJZX_TRUE\">意见征询</label>"
					+ "<input name=\"filterYJZX\" type=\"radio\" style=\"vertical-align:middle;\"  id=\"YJZX_FALSE\" value=\"0\" "
					+ (filterYJZX.equals("0") ? "checked=\"true\""
							: "") + " " + radioOnClick
					+ "></input><label for=\"YJZX_FALSE\">非意见征询</label>" + "";
			sortHtml.append(sortHtmlBAK);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 已办事宜排序
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 4:04:31 PM
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public void doneWorkSort(String sortType, List doneList)
			throws DAOException, ServiceException, SystemException {
		try {
			if (sortType == null) {
				logger.error("参数sortType不能为空或空字符串");
				throw new ServiceException("参数sortType不能为空或空字符串");
			}
			if (doneList == null) {
				logger.error("参数doneList不能为空");
				throw new ServiceException("参数doneList不能为空");
			}
			BaseComparator<Object[]> comparator = new SendDocComparator<Object[]>();
			if (sortType.equals(SortConst.SORT_TYPE_TASKENDDATE_ASC)) {
				comparator.setSortType(SortConst.SORT_ASC, 13);
			} else if (sortType
					.equals(SortConst.SORT_TYPE_PROCESSSTARTDATE_ASC)) {
				comparator.setSortType(SortConst.SORT_ASC, 12);
			} else if (sortType.equals(SortConst.SORT_TYPE_TASKENDDATE_DESC)) {
				comparator.setSortType(SortConst.SORT_DESC, 13);
			} else {
				comparator.setSortType(SortConst.SORT_DESC, 12);
			}
			Collections.sort(doneList, comparator);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 生成已办链接点击事件
	 * 
	 * @author 严建
	 * @param rootPath
	 * @param taskbusinessbean
	 * @return
	 * @createTime Feb 16, 2012 7:00:50 PM
	 */
	public StringBuilder genTodoClickTitle(String rootPath,
			TaskBusinessBean taskbusinessbean, String type) throws Exception {
		StringBuilder clickTitle = new StringBuilder();
		clickTitle
				.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
		clickTitle
				.append("var a = window.open('")
				.append(rootPath)
				.append("/senddoc/sendDoc!CASign.action?taskId=")
				.append(taskbusinessbean.getTaskId())
				.append("&type=")
				.append(type);
		
		//公文传输  限制已退回的不能提交   tj
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT DOC_FLAG FROM T_OARECVDOC WHERE OARECVDOCID = '"+taskbusinessbean.getBsinessId().split(";")[2]+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
		if(rs.next()){
			String t = rs.getString("DOC_FLAG");
			if("2".equals(rs.getString("DOC_FLAG"))){
				clickTitle.append("&gwcs=gwcs");
			}
		}
		
		clickTitle
				.append("&instanceId=")
				.append(taskbusinessbean.getInstanceId())
				.append("&workflowName=")
				.append(
						URLEncoder.encode(URLEncoder.encode(taskbusinessbean
								.getWorkflowName().toString(), "utf-8"),
								"utf-8"))
				.append(
						"','todo','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");
		return clickTitle;
	}

	/**
	 * 生成待办链接点击事件
	 * 
	 * @author 严建
	 * @param rootPath
	 * @param taskbusinessbean
	 * @return
	 * @createTime Feb 16, 2012 7:27:26 PM
	 */
	public StringBuilder genProcessedClickTitle(String rootPath,
			TaskBusinessBean taskbusinessbean) {
		StringBuilder clickTitle = new StringBuilder();
		clickTitle
				.append("var width=screen.availWidth-10;var height=screen.availHeight-30;");

		clickTitle
				.append("var a = window.open('")
				.append(rootPath)
				.append("/senddoc/sendDoc!viewProcessed.action?taskId=")
				.append(taskbusinessbean.getTaskId())
				.append("&instanceId=")
				.append(taskbusinessbean.getInstanceId())
				.append("&state=")
				.append(taskbusinessbean.getWorkflowStatus())
				.append("&searchType=1")
				.append("&businessType=")
				.append(taskbusinessbean.getBusinessType())
				.append("&isFirstYjzx=")
				.append(taskbusinessbean.isFirstYjzx())
				// 业务类型参数，0：表示意见征询数据
				.append(
						"','processed','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');")
				.append("if(a && a == 'OK'){window.location.reload();}");
		return clickTitle;
	}

	/**
	 * 已办显示发起人或时间
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param showCreator
	 * @param showDate
	 * @param taskbusinessbean
	 * @createTime Feb 16, 2012 7:04:05 PM
	 */
	public void showProcessedCreatorOrDate(StringBuffer innerHtml,
			String showCreator, String showDate,
			TaskBusinessBean taskbusinessbean) {
		// 显示发起人信息
		/*
		if ("1".equals(showCreator)) {
			innerHtml.append("&nbsp;&nbsp;<span title=\""+taskbusinessbean.getStartUserName()+"\" class =\"linkgray\">").append(
					taskbusinessbean.getStartUserName()).append("</span>");
		}*/

		// 显示日期信息 
		if ("1".equals(showDate)) {
			innerHtml.append("<td width=\"100px\">");
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			innerHtml.append("<span class =\"linkgray10\">")
					.append(
							" ("
									+ st.format(taskbusinessbean
											.getWorkflowStartDate()) + ")")
					.append("</span>");
			innerHtml.append("</td>");
		}
	}

	/**
	 * 待办显示发起人或时间
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param showCreator
	 * @param showDate
	 * @param taskbusinessbean
	 * @createTime Feb 16, 2012 7:20:06 PM
	 */
	public void showTodoCreatorOrDate(StringBuffer innerHtml,
			String showCreator, String showDate,
			TaskBusinessBean taskbusinessbean) {
		// 显示发起人信息
		
		if ("1".equals(showCreator)) {
			innerHtml.append("<td width=\"80px\">");
			if (taskbusinessbean.getBsinessId() == null) {
				taskbusinessbean.setBsinessId(manager
						.getFormIdAndBussinessIdByTaskId(taskbusinessbean
								.getTaskId())[0]);
			}
			String bussinessId = taskbusinessbean.getBsinessId();
			if (!"0".equals(bussinessId)) {
				String departmentName = null;
				String[] args = bussinessId.split(";");

				String tableName = String.valueOf(args[0]);
				String pkFieldName = String.valueOf(args[1]);
				String pkFieldValue = String.valueOf(args[2]);
				Map map = null;
				String userId = null;
				String userName = null;
				try {
					String sql = "select count("+pkFieldName+") as total from "+tableName+" where "+pkFieldName+" = '"+pkFieldValue+"'";
					Map totalMap = manager.queryForMap(sql);
					String total = StringUtil.castString(totalMap.get("total"));
					if (!"0".equals(total)) {
						map = manager.getSystemField(pkFieldName, pkFieldValue,
								tableName);
						userId = (String) map
								.get(BaseWorkflowManager.WORKFLOW_AUTHOR);
					}
					if (userId == null) {// 如果是子流程，拟稿人为第一父流程拟稿人 yanjian
						// 2011-11-03 19:10
						String panentInstanceId = String
								.valueOf(workflow
										.getContextInstance(
												taskbusinessbean
														.getInstanceId())
										.getVariable(
												WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID));
						ProcessInstance panentInstance = workflow
								.getProcessInstanceById(panentInstanceId);
						userId = panentInstance.getStartUserId();
						userName = panentInstance.getStartUserName();
					} else {
						userName = userService.getUserNameByUserId(userId);
					}
				} catch (Exception e) {
					User user = userService.getCurrentUser();
					userId = user.getUserId();
					userName = user.getUserName();
				}

				Organization department = userService
						.getUserDepartmentByUserId(userId);
				departmentName = department.getOrgName();
				//包含部门
				String showName = userName + "[" + departmentName + "]";
				//不包含部门
				String showTitle = userName;
				String titleShowName = showName ;
				if(showName.length()>3){//如果显示的内容长度大于设置的主题长度，则过滤该长度
					showName = showName.substring(0,3)+"...";
				}
				if(showTitle.length()>3){//如果显示的内容长度大于设置的主题长度，则过滤该长度
					showTitle = showTitle.substring(0,3)+"...";
				}
				innerHtml.append("<span class =\"linkgray\" title=\""+titleShowName+"\">").append(showTitle)
				//innerHtml.append("<span class =\"linkgray\" title=\""+userName + "[" + departmentName + "]"+"\">").append(
				//		userName + "[" + departmentName + "]")
						.append("</span>");
			}
			innerHtml.append("</td>");
		}
		// 显示日期信息
		if ("1".equals(showDate)) {
			innerHtml.append("<td width=\"100px\">");
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			innerHtml.append("<span class =\"linkgray10\">")
					.append(
							" ("
									+ st.format(taskbusinessbean
											.getTaskStartDate()) + ")").append(
							"</span>");
			innerHtml.append("</td>");
		}
		
	}

	/**
	 * 显示当前处理人信息
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @createTime Feb 16, 2012 7:06:38 PM
	 */
	public void showCurTaskInfo(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean,ParamBean paramBean) {

		/**
		 * @author yanjian 2012-02-29 9:57 显示当前处理人信息 (1)当前处理人只有一人时，显示如：“陈钢(信息处)”
		 *         (2)当前处理人有多个处室的人，且每个处理只有对应一个处理人时，显示“ 廖裕良(秘书处),陈钢(信息处)”
		 *         (3)当前处理人为一个处室里的多个处理人时，显示“ (秘书处)” (4)其他情况，结合前三点的处理方式进行显示
		 *         (4)办结流程不显示当前处理人
		 */
		StringBuffer tip = null;
		String CurTaskActorInfo = null;
		//只显示名字，不显示部门     申仪玲 2012.05.23
		String TaskActor = "";
		if (taskbusinessbean.getWorkflowEndDate() == null) {
			CurTaskActorInfo = (taskbusinessbean.getCurTaskActorInfo() == null ? ""
					: taskbusinessbean.getCurTaskActorInfo());
			String[] CurTaskActorInfos = CurTaskActorInfo.split(",");
			tip = new StringBuffer();
			Map<String, String> temp = null;
			/**
			 * 桌面上【已办事宜】升序后数据清空
			 * Bug序号： 0000003949 
			 * CurTaskActorInfo为""时，【CurTaskActorInfos.length > 0】，导致出现该问题。
			 * 添加判断条件【!"".equals(CurTaskActorInfo)】
			 * */
			if (!"".equals(CurTaskActorInfo) && CurTaskActorInfos.length > 0) {
				temp = new HashMap<String, String>();
				for (String info : CurTaskActorInfos) {
					if(info.indexOf("(") == -1){
						temp.put(" ", info);
					}else{
						String infoKey = info.substring(info.indexOf("("), info
								.length());// 部门信息
						String infoValue = info.substring(0, info.indexOf("("));
						if (!temp.containsKey(infoKey)) {
							temp.put(infoKey, infoValue);
						} else {
							if(temp.get(infoKey).indexOf(infoValue) == -1){
								temp.put(infoKey, temp.get(infoKey) + "," + infoValue);
							}
						}
					}
				}
				
				for (String infoKey : new ArrayList<String>(temp.keySet())) {
					String[] infoValues  = temp.get(infoKey).split(",");
					tip.append(",").append(infoKey);
					CurTaskActorInfo = "";
					for(String infoValue:infoValues){
						CurTaskActorInfo += ("," + infoValue);
						TaskActor = CurTaskActorInfo;
					}
					tip.append(CurTaskActorInfo.substring(1));
				}
				tip.deleteCharAt(0);
				TaskActor = TaskActor.substring(1);

			}
		} else {
			tip = new StringBuffer("");
			CurTaskActorInfo = "";
		}
		if(TaskActor.length()>3){
			
			TaskActor = TaskActor.subSequence(0, 3)+"...";
		}
		innerHtml.append(
				"<span class =\"linkgray\"  style=\"font-size:"+paramBean.getSectionFontSize()+"px\" title=\"" + tip + "\" >")
				.append(TaskActor).append("</span>");
	}

	/**
	 * 生成更多及更多的链接
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param rootPath
	 * @param processStatus
	 * @param blockId
	 * @param sortType
	 * @createTime Feb 16, 2012 7:13:50 PM
	 */
	public void genProcessedClickMoreLink(StringBuffer innerHtml,
			String rootPath, String processStatus, String blockId,
			String sortType) {
		StringBuffer link = new StringBuffer();
		String sortHtml = "";
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				rootPath);

		// processStatus-->0：未办结流程，1：已办结流程，
		if ("0".equals(processStatus)) {
			link.append("/senddoc/sendDoc!processedWorkflow.action?state=")
					.append(2).append("', '已办事宜'").append(");");
		} else if ("1".equals(processStatus)) {
			link.append("/senddoc/sendDoc!processedWorkflow.action?state=")
					.append(1).append("', '办结事宜'").append(");");
		} else {
			link.append("/senddoc/sendDoc!processedWorkflow.action?state=")
					.append(2).append("', '已办事宜'").append(");");
			if ("2".equals(processStatus)) {
				sortHtml = this.genSortTypeLink(blockId, sortType);
			}
		}
		innerHtml
				.append(
						"<div class=\"select\">"
								+ sortHtml
								+ "</div><div align=\"right\" style=\"padding:2px；font-size:12px;float:right;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ")
				.append("<IMG SRC=\"").append(rootPath).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
	}

	/**
	 * 生成更多及更多的链接
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param rootPath
	 * @param workflowType
	 * @param type
	 * @createTime Feb 16, 2012 7:38:01 PM
	 */
	public void genTodoClickMoreLink(StringBuffer innerHtml, String rootPath,
			String workflowType, String type) {
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				rootPath);
		link.append(
				"/senddoc/sendDoc!todoWorkflow.action?workflowType="
						+ workflowType + "&type=" + type).append("', '待办事宜'")
				.append(");");
		innerHtml.append(
				"<div align=\"right\" style=\"padding:2px；font-size:12px;\">")
				.append("<a href=\"#\" onclick=\"").append(link.toString())
				.append("\"> ")
				.append("<IMG SRC=\"").append(rootPath).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
	}

	/**
	 * 生成桌面待办的标题
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param parambean
	 * @throws Exception
	 * @createTime Feb 16, 2012 8:08:56 PM
	 */
	public void genTodoTitle(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean, ParamBean parambean)
			throws Exception {
		/* 生成流程类型别名链接 */
		StringBuilder clickProcessType = this.genTodoWorkflowNameLink(
				taskbusinessbean, parambean.getRootPath(), parambean.getType());
		StringBuilder clickTitle = this.genTodoClickTitle(parambean
				.getRootPath(), taskbusinessbean, parambean.getType());
		String objTitle = taskbusinessbean.getBusinessName();
		String title = "";

		// 上一步处理人信息
		String PreTaskActor = taskbusinessbean.getPreTaskActorInfo();
		if (objTitle.startsWith("<red>")) {
			objTitle = objTitle.replace("<red>", "");

			// 如果显示的内容长度大于设置的主题长度，则过滤该长度
			title = objTitle == null ? "" : objTitle.toString();
			if (title.length() > parambean.getLength()
					&& parambean.getLength() > 0) {
				title = title.substring(0, parambean.getLength()) + "...";
			}
			title = title.replace("\\r\\n", " ");
			title = title.replace("\\n", " ");

			innerHtml
					.append("<span title=\"")
					.append(objTitle)
					.append(PreTaskActor + "\">").append("")
					.append("<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickProcessType.toString()).append("\">[").append(
							taskbusinessbean.getWorkflowAliaName()).append(
							"]</a>").append("").append(
							"<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickTitle.toString()).append("\">").append(
							"<font color=\"red\" >").append("（急）")
					.append(title).append("</font>").append("</a>").append(
							"</span>");
			innerHtml.append("</td>");
		} else {
			title = objTitle == null ? "" : objTitle.toString();
			if (title.length() > parambean.getLength()
					&& parambean.getLength() > 0) {
				title = title.substring(0, parambean.getLength()) + "...";
			}
			innerHtml
					.append("<span title=\"")
					.append(objTitle)
					.append(PreTaskActor + "\">")
					.append("")
					.append("<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickProcessType.toString()).append("\">[").append(
							taskbusinessbean.getWorkflowAliaName()).append(
							"]</a>").append("").append(
							"<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
							clickTitle.toString()).append("\">").append("")
					.append(title).append("").append("</a></span>");
			innerHtml.append("</td>");
		}
		if ("1".equals(parambean.getShowCreator())) {
			innerHtml.append("<td width=\"80px\">");
			if (taskbusinessbean.getBsinessId() == null) {
				taskbusinessbean.setBsinessId(manager
						.getFormIdAndBussinessIdByTaskId(taskbusinessbean
								.getTaskId())[0]);
			}
			String bussinessId = taskbusinessbean.getBsinessId();
			if (!"0".equals(bussinessId)) {
				String departmentName = null;
				String[] args = bussinessId.split(";");

				String tableName = String.valueOf(args[0]);
				String pkFieldName = String.valueOf(args[1]);
				String pkFieldValue = String.valueOf(args[2]);
				Map map = null;
				String userId = null;
				String userName = null;
				try {
					String sql = "select count("+pkFieldName+") as total from "+tableName+" where "+pkFieldName+" = '"+pkFieldValue+"'";
					Map totalMap = manager.queryForMap(sql);
					String total = StringUtil.castString(totalMap.get("total"));
					if (!"0".equals(total)) {
						map = manager.getSystemField(pkFieldName, pkFieldValue,
								tableName);
						userId = (String) map
								.get(BaseWorkflowManager.WORKFLOW_AUTHOR);
					}
					if (userId == null) {// 如果是子流程，拟稿人为第一父流程拟稿人 yanjian
						// 2011-11-03 19:10
						String panentInstanceId = String
								.valueOf(workflow
										.getContextInstance(
												taskbusinessbean
														.getInstanceId())
										.getVariable(
												WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID));
						if(panentInstanceId != null && !"null".equals(panentInstanceId)){
							ProcessInstance panentInstance = workflow
							.getProcessInstanceById(panentInstanceId);
							userId = panentInstance.getStartUserId();
							userName = panentInstance.getStartUserName();
						}else{
							User user = userService.getCurrentUser();
							userId = user.getUserId();
							userName = user.getUserName();
						}
					} else {
						userName = userService.getUserNameByUserId(userId);
					}
				} catch (Exception e) {
					User user = userService.getCurrentUser();
					userId = user.getUserId();
					userName = user.getUserName();
				}

				Organization department = userService
						.getUserDepartmentByUserId(userId);
				departmentName = department.getOrgName();
				//包含部门
				String showName = userName + "[" + departmentName + "]";
				//不包含部门
				String showTitle = userName;
				String titleShowName = showName ;
				if(showName.length()>3){//如果显示的内容长度大于设置的主题长度，则过滤该长度
					showName = showName.substring(0,3)+"...";
				}
				if(showTitle.length()>3){//如果显示的内容长度大于设置的主题长度，则过滤该长度
					showTitle = showTitle.substring(0,3)+"...";
				}
				innerHtml.append("<span class =\"linkgray\" style=\"font-size:"+parambean.getSectionFontSize()+"px\" title=\""+titleShowName+"\">").append(showTitle)
				//innerHtml.append("<span class =\"linkgray\" title=\""+userName + "[" + departmentName + "]"+"\">").append(
				//		userName + "[" + departmentName + "]")
						.append("</span>");
			}
			innerHtml.append("</td>");
		}
		// 显示日期信息
		if ("1".equals(parambean.getShowDate())) {
			innerHtml.append("<td width=\"150px\">");
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			innerHtml.append("<span style=\"font-size:"+parambean.getSectionFontSize()+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(taskbusinessbean
					.getTaskStartDate())+"\"class =\"linkgray10\">")
					.append(st.format(taskbusinessbean.getTaskStartDate())).append(
							"</span>");
			innerHtml.append("</td>");
		}
	}

	/**
	 * 
	 * 生成桌面已办的标题
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param parambean
	 * @throws Exception
	 * @createTime Feb 17, 2012 6:25:00 PM
	 */
	public void genProcessedTitle(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean, ParamBean parambean)
			throws Exception {
		// 如果显示的内容长度大于设置的主题长度，则过滤该长度
		String title = taskbusinessbean.getBusinessName() == null ? ""
				: taskbusinessbean.getBusinessName();
		title = title.replace("\\r\\n", " ");
		title = title.replace("\\n", " ");
		String showTitle = title;
		if (title.length() > parambean.getLength() && parambean.getLength() > 0) {
			showTitle = title.substring(0, parambean.getLength()) + "...";
		}

		Object titleShowDate = null;
		String docType = null;
		if (parambean.getSortType().startsWith("taskenddate")) {
			docType = "办文时间:";
			titleShowDate = (taskbusinessbean.getTaskEndDate() == null ? new Date()
					: taskbusinessbean.getTaskEndDate());
		} else {
			docType = "来文时间:";
			titleShowDate = (taskbusinessbean.getWorkflowStartDate() == null ? new Date()
					: taskbusinessbean.getWorkflowStartDate());
		}
		StringBuilder clickTitle = this.genProcessedClickTitle(parambean
				.getRootPath(), taskbusinessbean);

		StringBuilder clickProcessType = this.genProcessedWorkflowNameLink(
				taskbusinessbean, parambean.getRootPath(), parambean
						.getFilterSign());
		
		//流程类型的链接
		innerHtml.append("<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
						clickProcessType.toString()).append("\">[").append(
						taskbusinessbean.getWorkflowAliaName()).append("]</a>");
		innerHtml.append("");
		//流程的链接(单个具体流程)
		innerHtml.append("<span title=\"").append(title).append(
				"\r\n"
						+ "("
						+ docType
						+ new SimpleDateFormat("yyyy-MM-dd")
								.format(titleShowDate) + ")").append("\">")
				.append("").append("<a style=\"font-size:"+parambean.getSectionFontSize()+"px\" href=\"#\" onclick=\"").append(
						clickTitle.toString()).append("\">").append(showTitle)
				.append("</a></span>");
		innerHtml.append("</td>");

		if ("1".equals(parambean.getShowCreator())) {
			innerHtml.append("<td style=\"font-size:"+parambean.getSectionFontSize()+"px\" width=\"80px\">");
			if("1".equals(parambean.getProcessStatus())){
				
				StringBuilder tip = new StringBuilder(taskbusinessbean.getStartUserName()+"("+userService.getUserDepartmentByUserId(taskbusinessbean.getStartUserId()).getOrgName()+")");
				StringBuilder TaskActor = new StringBuilder(taskbusinessbean.getStartUserName());
				if(TaskActor.length()>3){
					TaskActor = new StringBuilder(TaskActor.subSequence(0, 3)+"...");
				}
				innerHtml.append(
						"<span class =\"linkgray\"  style=\"font-size:"+parambean.getSectionFontSize()+"px\" title=\"" + tip + "\" >")
						.append(TaskActor).append("</span>");
			}else{
				this.showCurTaskInfo(innerHtml, taskbusinessbean,parambean);
			}
			innerHtml.append("</td>");
		}
		
		// 显示日期信息 
		if ("1".equals(parambean.getShowDate())) {
			innerHtml.append("<td width=\"2%\">");
			innerHtml.append("</td>");
			Date showDate = taskbusinessbean.getWorkflowStartDate();
			String datetitle ="";
			if("1".equals(parambean.getProcessStatus())){
				showDate = taskbusinessbean.getWorkflowEndDate();
				datetitle = "办结时间：";
			}
			innerHtml.append("<td width=\"20%\" align=\"right\">");
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			innerHtml.append("<span style=\"font-size:"+parambean.getSectionFontSize()+"px\" title =\""+datetitle+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(showDate)+"\" class =\"linkgray10\">")
					.append(st.format(showDate)).append("&nbsp;&nbsp;&nbsp;&nbsp;")
					.append("</span>");
			innerHtml.append("</td>");
		}
	}
	/**
	 * 查询来文单位
	 * tj
	 * 
	 */
	public String genTodoClickUnit(String rootPath,
			TaskBusinessBean taskbusinessbean, String type) throws Exception {
		String t = "";
		//公文传输  限制已退回的不能提交   tj
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT ISSUE_DEPART_SIGNED FROM T_OARECVDOC WHERE OARECVDOCID = '"+taskbusinessbean.getBsinessId().split(";")[2]+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
		if(rs.next()){
			t = rs.getString("ISSUE_DEPART_SIGNED");
		}
		
		return t;
	}
}
