/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.senddoc.manager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import sun.misc.BASE64Encoder;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.archive.tempfile.AnnexManager;
import com.strongit.oa.bgt.model.ToaYjzx;
import com.strongit.oa.bgt.senddoc.DocManager;
import com.strongit.oa.bo.GWYJAttach;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaInfopublishArticle;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.OaFormPdfService;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.AttachManager;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.customquery.CustomQueryAdpter;
import com.strongit.oa.senddoc.customquery.CustomQueryUtil;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.senddoc.query.bo.NodeNameQuery;
import com.strongit.oa.senddoc.util.GridViewColumUtil;
import com.strongit.oa.senddoc.util.JsonUtil;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.NodeNameConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.viewmodel.util.DateUtil;
import com.strongit.oa.webservice.util.PacketUtil;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.workflowDao.WorkflowDao;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 发送公文业务类
 * 
 * @author dengwenqiang
 * @version 1.0
 * @company Strongit Ltd. (C) copyright
 * @date May 7, 2012 9:32:07 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.manager.SendDocManager
 */
@Service
@Transactional
@OALogger
public class SendDocManager extends BaseManager {
	private GenericDAOHibernate<ToaSenddoc, java.lang.String> sendDocDao;
	private GenericDAOHibernate<ToaInfopublishArticle, java.lang.String> articleDao;
	private WorkflowDao serviceDAO = null;
	public static final String DATE_TYPE = "DATE";

	@Autowired
	SendDocDesktopManager sendDocDesktopManager;
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	@Autowired
	SendDocBaseManager sendDocBaseManager;

	@Autowired
	SendDocUploadManager sendDocUploadManager;

	@Autowired
	private DocManager docManager;

	@Autowired
	private ArticlesManager articlesManager;// 稿件Manager

	// 统一用户服务
	private IUserService user;

	@Autowired
	private AttachManager attachManager;
	
	@Autowired
	private AnnexManager annexManager; // 年内文件附件manager
	
	@Autowired
	private OaFormPdfService formPdfService; // 

	@SuppressWarnings("unchecked")
	public Object[] getInfoItemPage(Page page, String tableName,
			boolean isDraft, String... oatablepks) {
		List<EFormComponent> queryColumnList;
		List<String[]> showColumnList;
		List list;
		try {
			showColumnList = new LinkedList<String[]>();
			queryColumnList = new LinkedList<EFormComponent>();
			List<String> columnNames = new LinkedList<String>();
			/*
			 * final Map<String, String> columnMap = new HashMap<String,
			 * String>();//Map<字段名称,字段对应的Java类型> StringBuilder metaDataQuery =
			 * new StringBuilder("SELECT * ");//.append(strSet);
			 * metaDataQuery.append(" FROM ").append(tableName).append(" WHERE
			 * 1=0 ");//加上过滤条件1=0避免查询出数据,只是为了得到字段信息
			 * jdbcTemplate.query(metaDataQuery.toString(), new
			 * ResultSetExtractor(){ public Object extractData(ResultSet rs)
			 * throws SQLException, DataAccessException { ResultSetMetaData rsmd
			 * = rs.getMetaData(); int count = rsmd.getColumnCount(); for(int
			 * i=1;i<=count ;i++){ columnMap.put(rsmd.getColumnName(i),
			 * String.valueOf(rsmd.getColumnType(i)));//存储数据库字段对应的SQL类型 } return
			 * columnMap; }
			 * 
			 * });
			 */
			tableName = tableName.toUpperCase();
			String pkFieldName = super.getPrimaryKeyName(tableName);
			List<ToaSysmanageProperty> itemList = infoItemManager
					.getItemList(tableName);
			columnNames.add(pkFieldName);
			showColumnList.add(new String[] { pkFieldName, "选择", "0",
					WORKFLOW_TITLE });// 显示字段列表
			if (itemList != null && !itemList.isEmpty()) {
				for (ToaSysmanageProperty property : itemList) {
					if ("1".equals(property.getIsQuery())) {
						String infoItemField = property.getInfoItemField();
						EFormComponent eFormComponent = new EFormComponent();
						eFormComponent.setFieldName(infoItemField);
						eFormComponent.setLable(property
								.getInfoItemSeconddisplay());// 信息项别名
						eFormComponent.setNumber(property.getInfoItemOrderby());// 信息项排序号
						eFormComponent.setType("Strong.Form.Controls.Edit");
						queryColumnList.add(eFormComponent);
					}
					if ("1".equals(property.getIsView())) {
						if (!columnNames.contains(property.getInfoItemField())) {
							columnNames.add(property.getInfoItemField());
							showColumnList.add(new String[] {
									property.getInfoItemField(),
									property.getInfoItemSeconddisplay(),
									property.getInfoItemDatatype(),
									property.getInfoItemField() });// 显示字段列表
						}
					}
				}
			}
			String strColumn = StringUtils.join(columnNames, ',');
			StringBuilder SqlQuery = new StringBuilder("SELECT ");
			SqlQuery.append(strColumn).append(" FROM ").append(tableName)
					.append(" t WHERE 1=1 ");
			if (isDraft) {
				SqlQuery.append(" AND t.").append(WORKFLOW_AUTHOR).append("='")
						.append(userService.getCurrentUser().getUserId())
						.append("' AND t.").append(WORKFLOW_STATE)
						.append("='0' ");
			} else {// 查询非草稿数据
				// if(columnMap.containsKey(WORKFLOW_STATE)) {
				SqlQuery.append(" AND (t.").append(WORKFLOW_STATE)
						.append("='1'");
				SqlQuery.append(" OR t.").append(WORKFLOW_STATE).append("='3'");
				SqlQuery.append(" OR t.").append(WORKFLOW_STATE)
						.append(" is null)");
				// }
				/*
				 * modify yanjian 2012-04-28 17:22
				 * 性能优化考虑：通过添加参数oatablepks缩小信息查询返回
				 */
				StringBuilder pks = null;
				StringBuilder pkname = null;
				if (oatablepks != null && oatablepks.length > 0) {
					for (String bsid : oatablepks) {
						if (pks == null) {
							pks = new StringBuilder();
							pkname = new StringBuilder();
						}
						// 收文流程挂接了意见征询的表单，与收文业务表不符，应该过滤
						if (!(bsid.split(";")[0]).equals(tableName)) {
							continue;
						}
						pks.append(",'" + bsid.split(";")[2] + "'");
						if (pkname.length() == 0) {
							pkname.append(bsid.split(";")[1]);
						}
					}
					if (pks != null && pks.length() > 0) {
						pks.deleteCharAt(0);
						SqlQuery.append(" and (t." + pkname + " in (" + pks
								+ "))");
					}
				}
			}
			// 处理查询
			Map params = ActionContext.getContext().getParameters();
			StringBuilder SqlCondition = new StringBuilder("");
			List<Object> paramValueList = new LinkedList<Object>();
			List<Integer> paramValueSqlTypeList = new LinkedList<Integer>();
			if (params != null && !params.isEmpty()) {
				for (Object objEntry : params.entrySet()) {
					Map.Entry entry = (Map.Entry) objEntry;
					Object key = entry.getKey();
					if ("formId".equals(key)) {
						continue;
					}
					Object value = entry.getValue();
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] objValue = (Object[]) value;
							if (objValue.length > 0) {
								value = objValue[0];
							}
						}
					}
					if (value == null || "".equals(value.toString())) {
						continue;
					}
					for (int i = 0; i < queryColumnList.size(); i++) {
						EFormComponent ec = queryColumnList.get(i);
						String fieldName = ec.getFieldName();// 得到字段名称
						if (key.equals(fieldName)) {// 找到匹配的字段
							SqlCondition.append(" AND t.")
									.append(ec.getFieldName())
									.append(" like ? ");
							paramValueList.add("%" + value + "%");
							paramValueSqlTypeList.add(Types.VARCHAR);
							ec.setValue(value.toString());
						}
					}
				}
				logger.info("自定查询SQL：" + SqlCondition.toString() + ",参数："
						+ paramValueList);
			}
			if (SqlCondition.length() > 0) {
				SqlQuery.append(SqlCondition);
			}
			int[] sqlTypeArray = new int[paramValueSqlTypeList.size()];
			for (int i = 0; i < paramValueSqlTypeList.size(); i++) {
				sqlTypeArray[i] = paramValueSqlTypeList.get(i);
			}
			logger.info(SqlQuery.toString());
			// list =
			// jdbcTemplate.queryForList(SqlQuery.toString(),paramValueList.toArray(),sqlTypeArray);
			if ((!isDraft) && (oatablepks == null || oatablepks.length == 0)) {
				// 当不是查询草稿并且要查询的流程信息中找不到业务id时
				list = new ArrayList();
			} else {
				list = jdbcTemplate.queryForList(SqlQuery.toString(),
						paramValueList.toArray(), sqlTypeArray);
			}
			HashMap busidListMap = new HashMap();
			if (list != null && !list.isEmpty()) {
				for (Object obj : list) {
					Map mapobj = (Map) obj;
					String pkFieldNamevalue = (String) mapobj.get(pkFieldName);
					String busId = tableName + ";" + pkFieldName + ";"
							+ pkFieldNamevalue;
					if (!busidListMap.containsKey(busId)) {
						busidListMap.put(busId, mapobj);
					}
				}
			}
			return new Object[] { showColumnList, list, queryColumnList,
					tableName, busidListMap };
		} catch (DataAccessException e) {
			logger.error("发生Jdbc异常", e);
			throw new SystemException(e);
		} catch (Exception e) {
			logger.error("SystemException..", e);
			throw new SystemException(e);
		}
	}

	/**
	 * 获取当前用户
	 */
	public User getCurrentUser() throws SystemException {
		return user.getCurrentUser();
	}

	/**
	 * 根据用户ID获取其所在的部门
	 * 
	 * @author:邓志城
	 * @date:2009-6-11 上午11:19:41
	 * @param userId
	 * @return
	 * @throws SystemException
	 */
	public Organization getUserDepartmentByUserId(String userId)
			throws SystemException {
		return user.getUserDepartmentByUserId(userId);
	}

	/**
	 * 获取当前用户所属组织机构
	 * 
	 * @author:邓志城
	 * @date:2009-6-11 上午11:21:09
	 * @param userId
	 * @return
	 * @throws SystemException
	 */
	public Organization getCurrentUserDepartmentByUserId()
			throws SystemException {
		String userId = getCurrentUser().getUserId();
		return getUserDepartmentByUserId(userId);
	}

	/**
	 * 重新办理
	 * 
	 * @description
	 * @author 严建
	 * @param formId
	 *            表单id
	 * @param businessName
	 *            业务名称
	 * @param bussinessId
	 *            业务id
	 * @param workflowName
	 *            流程名称
	 * @param otherparameter
	 *            扩展实体信息（用于向后兼容需要添加的参数）
	 * @param infos
	 * @return String[] {流程实例id，任务id}
	 * @createTime Apr 18, 2012 3:42:35 PM
	 */
	@SuppressWarnings("unchecked")
	public String[] reTodo(String formId, String businessName,
			String bussinessId, String workflowName,
			OtherParameter otherparameter, OALogInfo... infos) {
		List transList = workflow.getStartNodeTransitions(workflowName);
		Object[] firsttran = (Object[]) transList.get(0);
		String transitionName = (String) firsttran[0];
		String nodeId = ((Set<String>) firsttran[3]).iterator().next()
				.split("\\|")[1];// 得到节点id
		String[] strTaskActorArray = new String[1];
		User user = userService.getCurrentUser();// 得到当前用户
		String userId = user.getUserId();
		strTaskActorArray[0] = userId + "|" + nodeId;
		System.out.println("asdas");
		VoFormDataBean bean = startProcessToFistNodeWorkflow(formId,
				workflowName, bussinessId, userId, businessName,
				strTaskActorArray, transitionName, "", null, otherparameter,
				new OALogInfo("提交到第一个节点"));

		String instanceId = bean.getInstanceId();
		List<Object[]> list = workflow.getCurrentHandleByNode(instanceId,
				nodeId);
		String taskId = null;
		if (list != null && list.size() > 0) {
			Object[] currentInfo = list.get(0);
			taskId = currentInfo[5].toString(); // 任务实例id
		}
		return new String[] { instanceId, taskId };
	}

	/**
	 * 获取信息项数据
	 * 
	 * @author:邓志城
	 * @date:2010-11-11 下午04:09:31
	 * @param page
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            流程启动表单
	 * @param isDraft
	 *            是否查询草稿数据
	 * @return Object[展现列表,字段数据,查询字段列表,表名称]
	 * @modify yanjian 2011-09-19 12:45 显示红黄警示牌代码： showColumnList.add(new
	 *         String[]{"png","",PNG_TYPE,"png"});
	 * @modify yanjian 2011-10-24 保存草稿列表显示界面 添加流程标题的查询 2012-04-28 17:22
	 *         性能优化考虑：通过添加参数oatablepks缩小信息查询返回
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getInfoItemPage(Page page, String workflowName,
			String formId, boolean isDraft, String... oatablepks)
			throws Exception {
		String tableName;
		List<EFormComponent> queryColumnList;
		List<String[]> showColumnList;
		List list;
		try {

			showColumnList = new LinkedList<String[]>();
			queryColumnList = new LinkedList<EFormComponent>();
			List<String> columnNames = new LinkedList<String>();
			if (formId == null || "".equals(formId) || "0".equals(formId)
					|| formId.startsWith("t")) {
				return this.getInfoItemPage(page, formId, isDraft, oatablepks);// workflowName为表名
			}
			Map<String, EFormComponent> fieldMap = eform.getFieldInfo(formId);
			EFormComponent mainTable = fieldMap
					.get(IEFormService.MAINTABLENAME);
			if (mainTable == null) {
				showColumnList.add(new String[] { "pkFieldName", "选择", "0",
						WORKFLOW_TITLE });// 显示字段列表
				return new Object[] { showColumnList, new ArrayList(),
						queryColumnList, null };
			}
			tableName = mainTable.getTableName();
			StringBuilder metaDataQuery = new StringBuilder("SELECT * ");// .append(strSet);
			metaDataQuery.append(" FROM ").append(tableName)
					.append(" WHERE 1=0  ");// 加上过滤条件1=0避免查询出数据,只是为了得到字段信息
			final Map<String, String> columnMap = new HashMap<String, String>();// Map<字段名称,字段对应的Java类型>
			jdbcTemplate.query(metaDataQuery.toString(),
					new ResultSetExtractor() {
						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							ResultSetMetaData rsmd = rs.getMetaData();
							int count = rsmd.getColumnCount();
							for (int i = 1; i <= count; i++) {
								columnMap.put(rsmd.getColumnName(i),
										String.valueOf(rsmd.getColumnType(i)));// 存储数据库字段对应的SQL类型
							}
							return columnMap;
						}

					});
			String pkFieldName = super.getPrimaryKeyName(tableName);
			List<ToaSysmanageProperty> itemList = infoItemManager
					.getItemList(tableName);
			columnNames.add(pkFieldName);
			showColumnList.add(new String[] { pkFieldName, "选择", "0",
					WORKFLOW_TITLE });// 显示字段列表
			if (itemList != null && !itemList.isEmpty()) {
				for (ToaSysmanageProperty property : itemList) {
					if ("1".equals(property.getIsQuery())) {
						String infoItemField = property.getInfoItemField();
						EFormComponent eFormComponent = fieldMap
								.get(infoItemField);
						if (eFormComponent != null) {
							eFormComponent.setLable(property
									.getInfoItemSeconddisplay());// 信息项别名
							eFormComponent.setNumber(property
									.getInfoItemOrderby());// 信息项排序号
							eFormComponent.setValueType(columnMap
									.get(infoItemField));// 字段对应的Java类型
							// 处理下拉列表情况
							if (eFormComponent.getType().equals(
									"Strong.Form.Controls.ComboxBox")) {
								String items = eFormComponent.getItems();
								if (items.indexOf(";") == -1) {// 下拉列表数据是从数据字典中读取
									if (eFormComponent.getSelTableName() != null
											&& !"".equals(eFormComponent
													.getSelTableName())
											&& eFormComponent.getSelCode() != null
											&& !"".equals(eFormComponent
													.getSelCode())
											&& eFormComponent.getSelName() != null
											&& !"".equals(eFormComponent
													.getSelName())) {
										StringBuilder query = new StringBuilder(
												"select ")
												.append(eFormComponent
														.getSelCode())
												.append(",")
												.append(eFormComponent
														.getSelName())
												.append(" from ")
												.append(eFormComponent
														.getSelTableName())
												.append(" where ")
												.append(eFormComponent
														.getSelFilter());
										List lst = jdbcTemplate
												.queryForList(query.toString());
										StringBuilder builderItems = new StringBuilder();
										if (!lst.isEmpty()) {
											for (int i = 0; i < lst.size(); i++) {
												Map map = (Map) lst.get(i);
												builderItems
														.append(map
																.get(eFormComponent
																		.getSelCode()))
														.append(",")
														.append(map
																.get(eFormComponent
																		.getSelName()))
														.append(",")
														.append(map
																.get(eFormComponent
																		.getSelCode()))
														.append(";");
											}
										}
										eFormComponent.setItems(builderItems
												.toString());
									}
								}
							}
							queryColumnList.add(eFormComponent);
						}
					}
					if ("1".equals(property.getIsView())) {
						if (!columnNames.contains(property.getInfoItemField())) {
							columnNames.add(property.getInfoItemField());
							// if(fieldMap.containsKey(property.getInfoItemField()))
							// {
							showColumnList.add(new String[] {
									property.getInfoItemField(),
									property.getInfoItemSeconddisplay(),
									property.getInfoItemDatatype(),
									property.getInfoItemField() });// 显示字段列表
							// }
						}
					}
				}
			}
			boolean isShowAuthor = false;
			// 是否要显示拟稿人
			if (columnNames.contains(WORKFLOW_AUTHOR)) {
				isShowAuthor = true;
			}
			for (String systemItem : SYSTEM_INFO_ITEM) {
				if (!columnNames.contains(systemItem)) {
					if (columnMap.containsKey(systemItem)) {// 只查询表里存在的字段
						columnNames.add(systemItem);
					}
				}
			}
			String strColumn = StringUtils.join(columnNames, ',');
			StringBuilder SqlQuery = new StringBuilder("SELECT ");
			SqlQuery.append(strColumn).append(" FROM ").append(tableName)
					.append(" t WHERE 1=1   ");
			if (isDraft) {
				SqlQuery.append(" AND t.").append(WORKFLOW_AUTHOR).append("='")
						.append(userService.getCurrentUser().getUserId())
						.append("' AND t.").append(WORKFLOW_NAME).append("='")
						.append(workflowName).append("'").append(" AND t.")
						.append(WORKFLOW_STATE).append("='0' ");
			} else {// 查询非草稿数据
				/*
				 * if(columnMap.containsKey(WORKFLOW_NAME)) { if(workflowName !=
				 * null && workflowName.length() > 0) { SqlQuery.append(" AND
				 * (t.").append(WORKFLOW_NAME).append("=
				 * '").append(workflowName).append("'");
				 * SqlQuery.append(" OR t.").append(WORKFLOW_NAME).append(" is
				 * null)"); } }
				 */
				if (columnMap.containsKey(WORKFLOW_STATE)) {
					SqlQuery.append(" AND (t.").append(WORKFLOW_STATE)
							.append("='1'");
					SqlQuery.append(" OR t.").append(WORKFLOW_STATE)
							.append("='3'");
					SqlQuery.append(" OR t.").append(WORKFLOW_STATE)
							.append(" is null)");
				}
				/*
				 * modify yanjian 2012-04-28 17:22
				 * 性能优化考虑：通过添加参数oatablepks缩小信息查询返回
				 */
				StringBuilder pks = null;
				StringBuilder pkname = null;
				if (oatablepks != null && oatablepks.length > 0) {
					for (String bsid : oatablepks) {
						if (pks == null) {
							pks = new StringBuilder();
							pkname = new StringBuilder();
						}
						// 收文流程挂接了意见征询的表单，与收文业务表不符，应该过滤
						if (!(bsid.split(";")[0]).equals(tableName)) {
							continue;
						}
						pks.append(",'" + bsid.split(";")[2] + "'");
						if (pkname.length() == 0) {
							pkname.append(bsid.split(";")[1]);
						}
					}
					if (pks != null && pks.length() > 0) {
						pks.deleteCharAt(0);
						SqlQuery.append(" and (t." + pkname + " in (" + pks
								+ "))");
					}
				}
			}
			// 处理查询
			Map params = ActionContext.getContext().getParameters();
			StringBuilder SqlCondition = new StringBuilder("");
			List<Object> paramValueList = new LinkedList<Object>();
			List<Integer> paramValueSqlTypeList = new LinkedList<Integer>();
			if (params != null && !params.isEmpty()) {
				for (Object objEntry : params.entrySet()) {
					Map.Entry entry = (Map.Entry) objEntry;
					Object key = entry.getKey();
					Object value = entry.getValue();
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] objValue = (Object[]) value;
							if (objValue.length > 0) {
								value = objValue[0];
							}
						}
					}
					if (value == null || "".equals(value.toString())) {
						continue;
					}
					for (int i = 0; i < queryColumnList.size(); i++) {
						EFormComponent ec = queryColumnList.get(i);
						String fieldName = ec.getFieldName();// 得到字段名称
						if (key.equals(fieldName)) {// 找到匹配的字段
							if ("Strong.Form.Controls.Edit"
									.equals(ec.getType())) {// 单行文本框和多行文本框(支持字符串查询)
								SqlCondition.append(" AND t.")
										.append(ec.getFieldName())
										.append(" like ? escape '<' ");// niwy escape '<'处理%,将/后面的字符当成普通字符处理
								if(value.toString().contains("%")){
									value=value.toString().replaceAll("%","<%");//如果用户输入的是%,对其进行处理
								}
								paramValueList.add("%" + value + "%" );
								paramValueSqlTypeList.add(Types.VARCHAR);
							}
							if ("Strong.Form.Controls.ComboxBox".equals(ec
									.getType())) {
								SqlCondition.append(" AND t.")
										.append(ec.getFieldName())
										.append(" = ? ");
								paramValueList.add(value);
								paramValueSqlTypeList.add(Integer.parseInt(ec
										.getValueType()));
							}
							if ("Strong.Form.Controls.DateTimePicker".equals(ec
									.getType())
									&& value != null
									&& !"".equals(value)) {
								if (Integer.parseInt(ec.getValueType()) == Types.VARCHAR) {// 字符串的日期
									SqlCondition.append(" AND t.")
											.append(ec.getFieldName())
											.append(" <= ?");
									paramValueList.add(value);
									paramValueSqlTypeList.add(Integer
											.parseInt(ec.getValueType()));
								} else {
									SqlCondition
											.append(" AND t.")
											.append(ec.getFieldName())
											.append(" >= to_date('")
											.append(value)
											.append("','YYYY-MM-DD HH24:MI:SS')");
								}
							}
							
							if(value.toString().contains("%")){ // niwy 返回界面时将/换成空字符串
								value=value.toString().replaceAll("<","");
							}
							ec.setValue(value.toString());
						}
					}
				}
				logger.info("自定查询SQL：" + SqlCondition.toString() + ",参数："
						+ paramValueList);
			}
			if (SqlCondition.length() > 0) {
				SqlQuery.append(SqlCondition);
			}
			int[] sqlTypeArray = new int[paramValueSqlTypeList.size()];
			for (int i = 0; i < paramValueSqlTypeList.size(); i++) {
				sqlTypeArray[i] = paramValueSqlTypeList.get(i);
			}
			logger.info(SqlQuery.toString());
			// yanjian 2012-05-14 13:34 基于性能问题进行调整
			if ((!isDraft) && (oatablepks == null || oatablepks.length == 0)) {
				// 当不是查询草稿并且要查询的流程信息中找不到业务id时
				list = new ArrayList();
			} else {
				list = jdbcTemplate.queryForList(SqlQuery.toString(),
						paramValueList.toArray(), sqlTypeArray);
			}
			if (isShowAuthor) {// 需要显示拟稿人字段时才查询
				// 转换ID
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map = (Map<String, Object>) list
								.get(i);
						Object userId = map.get(WORKFLOW_AUTHOR);
						if (userId != null) {
							map.put(WORKFLOW_AUTHOR, userService
									.getUserNameByUserId(userId.toString()));
						}
					}
				}
				logger.info("根据用户id转换用户名称");
			}
			HashMap busidListMap = new HashMap();
			if (list != null && !list.isEmpty()) {
				for (Object obj : list) {
					Map mapobj = (Map) obj;
					String pkFieldNamevalue = (String) mapobj.get(pkFieldName);
					String busId = tableName + ";" + pkFieldName + ";"
							+ pkFieldNamevalue;
					if (!busidListMap.containsKey(busId)) {
						busidListMap.put(busId, mapobj);
					}
				}
			}
			return new Object[] { showColumnList, list, queryColumnList,
					tableName, busidListMap };
		} catch (DataAccessException e) {
			logger.error("发生Jdbc异常", e);
			throw new SystemException(e);
		} catch (Exception e) {
			logger.error("SystemException..", e);
			throw new SystemException(e);
		}
	}
	
	/**
	 * 得到我的在办文件列表
	 * 
	 * @author:xush
	 * @date:1/4/2014 2:58 PM
	 * @param page
	 *            分页对象
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            流程主表单id
	 * @param workflowType
	 *            流程类型
	 * @param state
	 *            流程状态 "2,全部,2;0,在办,0;1,办毕,1;"
	 * @return 
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<
	 *         EFormComponent>,表名称]
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getMyNowFile(Page page, ProcessedParameter parameter) {
		try {
			String workflowName = parameter.getWorkflowName();
			String formId = parameter.getFormId();
			String workflowType = parameter.getWorkflowType();
			String state = parameter.getState();
			Object[] toSelectItems = { "processInstanceId","processMainFormBusinessId","businessName",
					"processStartDate",  "startUserName","processName","processEndDate"};
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("startUserId", userService.getCurrentUser()
					.getUserId());// 当前用户办理任务
			paramsMap.put("processSuspend", "0");
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				paramsMap.put("processTypeId",
						genWorkflowTypeList(workflowType));
			}
			if (workflowName != null && !"".equals(workflowName)) {
				paramsMap.put("processName", workflowName);
			}
			if (state != null && !"".equals(state) && !"2".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			String businessName = ServletActionContext.getRequest()
					.getParameter(WORKFLOW_TITLE);
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("processStartDate", "1");

			page.setAutoCount(true);
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
					.append(Tjbpmbusiness.getShowableBusinessType())
					.append(") ");
			page = workflow.getProcessInstanceByConditionForPage(page, sItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			ProcessedParameter processedParameter = new ProcessedParameter();
			Map[] handlerAndDeptInfo = null;
			Map<String, String> currentuserinfo = new HashMap<String, String>();
			Map<String, String> currentuserDept = new HashMap<String, String>();
			
			// page = ListUtils.splitList2Page(page, listWorkflow);
			// page.setTotalCount(listWorkflow.size());
			List<Object[]> listWorkflow = page.getResult();
			List<TaskBusinessBean> listWorkflow1 = null;
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page.getResult() != null && !page.getResult().isEmpty()) {
				if (listWorkflow1 == null) {
					listWorkflow1 = new LinkedList<TaskBusinessBean>();
				}
				for (Object object : page.getResult()) {
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setStartUserName(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setBsinessId(StringUtil
							.castString(objs[1]));
					
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[5]));
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[2] == null ? "" : objs[2]));
					taskbusinessbean.setWorkflowStartDate(objs[3]==null?null:sf.parse(StringUtil.castString(objs[3])));
					taskbusinessbean.setWorkflowEndDate(objs[6]==null?null:sf.parse(StringUtil.castString(objs[6])));
					
					listWorkflow1.add(taskbusinessbean);
				}
				listWorkflow1 = new ArrayList<TaskBusinessBean>(listWorkflow1);
			}
			
			List<String> oatablepks = null;
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (Object[] objs : listWorkflow) {
					if (oatablepks == null) {
						oatablepks = new LinkedList<String>();
					}
					if (objs[1] != null) {
						oatablepks.add(StringUtil.castString(objs[1]));
					} else {
						logger.info("流程实例id【" + StringUtil.castString(objs[0])
								+ "】的流程业务id为null！");
					}
				}
			}
			String[] oatablepkis = null;
			if (oatablepks != null && !oatablepks.isEmpty()) {
				if (oatablepkis == null) {
					oatablepkis = new String[oatablepks.size()];
				}
				oatablepks.toArray(oatablepkis);
			}
			Object[] obj = getInfoItemPage(page, workflowName, formId, false,
					oatablepkis);
			List showColumnList = (List) obj[0];
			List<Map> showList = new ArrayList<Map>();
			Map tempMap = (Map) obj[4];// 存放业务数据【业务数据ID：业务数据】
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
			// 增加工作流中的显示字段
			if (listWorkflow1 != null && !listWorkflow1.isEmpty()) {
				Map<String, String[]> signInfoMap = new LinkedHashMap<String, String[]>();
				
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				StringBuilder params = new StringBuilder();
				for (TaskBusinessBean taskbusinessbean : listWorkflow1) {
					params.append(taskbusinessbean.getInstanceId()).append(",");
				}
				String isExsitTodo="0";
				Map<String, Map> taskInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder(
							"SELECT TI.PROCINST_,TI.ID_ AS TASKID,TI.START_ AS TASKSTARTDATE,TI.END_ AS TASKENDDATE ");
					sql.append(" FROM JBPM_TASKINSTANCE TI WHERE TI.PROCINST_ IN(");
					sql.append(params.toString());
					sql.append(") AND  TI.ACTORID_='"
							+ paramsMap.get("startUserId")
							+ "' ORDER BY TI.END_ DESC");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						taskInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							if (!taskInfoMap.containsKey(String.valueOf(rsMap
									.get("PROCINST_")))) {
								taskInfoMap.put(
										String.valueOf(rsMap.get("PROCINST_")),
										rsMap);
							}
						}
					}
				}
				for (TaskBusinessBean taskbusinessbean : listWorkflow1) {
					if (taskInfoMap == null) {
						continue;
					}
					Map taskMap = taskInfoMap.get(taskbusinessbean
							.getInstanceId());
					if (taskMap == null) {
						continue;
					}
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
					TwfBaseNodesetting nodesetting = adapterBaseWorkflowManager
					.getWorkflowService().findFirstNodeSetting(StringUtil.castString(taskMap
							.get("TASKID")),workflowName);
					adapterBaseWorkflowManager.getSendDocUploadManager()
					.initProcessedByNodeSetting(nodesetting);
			        String nodeId = StringUtil.castString(nodesetting.getNsNodeId());
			        String nodeName = StringUtil.castString(nodesetting.getNsNodeName());
					taskbusinessbean.setNodeId(nodeId);
					Object[] toSelectItems1 = { "taskId" };
					List sItems1 = Arrays.asList(toSelectItems);
					Map<String, Object> paramsMap1 = new HashMap<String, Object>();
					paramsMap1.put("taskType", "2");// 取非办结任务
					paramsMap1.put("processSuspend", "0");// 取非挂起任务
					paramsMap1.put("processInstanceId", taskbusinessbean
							.getInstanceId());
					paramsMap1.put("handlerId", adapterBaseWorkflowManager
							.getUserService().getCurrentUser().getUserId());// 当前用户办理任务
					Map orderMap1 = new HashMap<Object, Object>();
					orderMap.put("taskStartDate", "1");
					List list = adapterBaseWorkflowManager.getWorkflowService()
							.getTaskInfosByConditionForList(sItems1, paramsMap1,
									orderMap1, null, null, null, null);
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if (list != null && !list.isEmpty()) {
						isExsitTodo = "1";
					} else {
						isExsitTodo = "0";
					}
//					taskbusinessbean.setTaskEndDate((Date) taskMap
//							.get("TASKENDDATE"));
				//	String TaskEndDatestr=taskMap.get("TASKENDDATE");
					if (taskMap.get("TASKENDDATE")==null){
						taskbusinessbean.setTaskEndDate(null);
					}else{
						taskbusinessbean.setTaskEndDate(sdf.parse(StringUtil.castString(taskMap.get("TASKENDDATE"))));
					}
					

//					taskbusinessbean.setTaskStartDate((Date) taskMap
//							.get("TASKSTARTDATE"));
					taskbusinessbean.setTaskStartDate(sdf.parse(StringUtil.castString(taskMap
							.get("TASKSTARTDATE")==null?"":taskMap
									.get("TASKSTARTDATE"))));
					
					Map parmMap = new HashMap();
					parmMap.put("processstartdate",
							taskbusinessbean.getWorkflowStartDate());
					
					
					
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					if (parameter.getWorkflowName() != null
							&& !parameter.getWorkflowName().equals(
									taskbusinessbean.getWorkflowName())) {
						if (parameter.getFormId() != null
								&& (parameter.getFormId().startsWith("t") || parameter
										.getFormId().startsWith("T"))) {
						} else {
							continue;
						}
					}
					Map map = new HashMap();
					// ---------------------End--------------------------
					if (obj[3] != null) {// 存在表名
						if (tempMap
								.containsKey(taskbusinessbean.getBsinessId())) {
							map = new HashMap(
									(Map) tempMap.get(taskbusinessbean
											.getBsinessId()));
						}
					}
					sendDocBaseManager.initProcessedShowMap1(map, pkFieldName,
							taskbusinessbean, parmMap,isExsitTodo);
					showList.add(map);
					
				}
			}
			/*
			 * String businessName =
			 * ServletActionContext.getRequest().getParameter(WORKFLOW_TITLE);
			 * if(businessName != null && !"".equals(businessName)) {
			 * paramsMap.put("businessName", businessName); }
			 */
			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表

				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(ServletActionContext.getRequest()
						.getParameter(WORKFLOW_TITLE));
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			showColumnList.add(new String[] { "processStatus", "状态", "0",
					"processStatus" });// 显示字段列表
			if (parameter.getHandleKind() != null) {
				showColumnList.add(new String[] { "processstartdate", "发起时间",
						"0", "processstartdate" });// 显示字段列表
			}
			showColumnList.add(new String[] { "startUserName", "发起人", "0",
					"startUserName" });// 显示字段列表
			showColumnList.add(new String[] { "processEndDate", "办结时间", "0",
			"processEndDate" });// 显示字段列表
			
//			// 按任务类型搜索
//			EFormComponent taskTypeSelect = new EFormComponent();
//			taskTypeSelect.setType("Strong.Form.Controls.ComboxBox");
//			taskTypeSelect.setLable("状态");
//			taskTypeSelect.setFieldName("state");
//			String items = "0,在办,0;1,办毕,1;";
//			taskTypeSelect.setItems(items);
//			taskTypeSelect.setValue(state);
//			queryColumnList.add(taskTypeSelect);
			
			System.out.println();
			
			page.setResult(showList);
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (Exception ex) {
			logger.error("SystemException", ex);
			throw new SystemException(ex);
		}
	}
	
	public String getisExsitTodo(String instanceId){
		String isExsitTodo="0";
		Object[] toSelectItems1 = { "taskId" };
		List sItems1 = Arrays.asList(toSelectItems1);
		Map<String, Object> paramsMap1 = new HashMap<String, Object>();
		paramsMap1.put("taskType", "2");// 取非办结任务
		paramsMap1.put("processSuspend", "0");// 取非挂起任务
		paramsMap1.put("processInstanceId",instanceId);
		paramsMap1.put("handlerId", adapterBaseWorkflowManager
				.getUserService().getCurrentUser().getUserId());// 当前用户办理任务
		Map orderMap1 = new HashMap<Object, Object>();
		orderMap1.put("taskStartDate", "1");
		List list = adapterBaseWorkflowManager.getWorkflowService()
				.getTaskInfosByConditionForList(sItems1, paramsMap1,
						orderMap1, null, null, null, null);
		if (list != null && !list.isEmpty()) {
			isExsitTodo = "1";
		} else {
			isExsitTodo = "0";
		}
		return isExsitTodo;
	}

	/**
	 * 得到主办流程列表
	 * 
	 * @author:邓志城
	 * @date:2010-11-17 下午07:00:14
	 * @param page
	 *            分页对象
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            流程主表单id
	 * @param workflowType
	 *            流程类型
	 * @param state
	 *            流程状态 "2,全部,2;0,在办,0;1,办毕,1;"
	 * @return 
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<
	 *         EFormComponent>,表名称]
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getHosted(Page page, ProcessedParameter parameter) {
		try {
			String workflowName = parameter.getWorkflowName();
			String formId = parameter.getFormId();
			String workflowType = parameter.getWorkflowType();
			String state = parameter.getState();
			Object[] toSelectItems = { "processInstanceId",
					"processMainFormBusinessId", "processEndDate",
					"processTimeout", "businessName", "processName",
					"processStartDate" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("startUserId", userService.getCurrentUser()
					.getUserId());// 当前用户办理任务
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				paramsMap.put("processTypeId",
						genWorkflowTypeList(workflowType));
			}
			if (workflowName != null && !"".equals(workflowName)) {
				paramsMap.put("processName", workflowName);
			}
			if (state != null && !"".equals(state) && !"2".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			String businessName = ServletActionContext.getRequest()
					.getParameter(WORKFLOW_TITLE);
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("processStartDate", "1");
			page.setAutoCount(true);
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
					.append(Tjbpmbusiness.getShowableBusinessType())
					.append(") ");
			page = workflow.getProcessInstanceByConditionForPage(page, sItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			Map[] handlerAndDeptInfo = null;
			Map<String, String> currentuserinfo = new HashMap<String, String>();
			Map<String, String> currentuserDept = new HashMap<String, String>();
			// page = ListUtils.splitList2Page(page, listWorkflow);
			// page.setTotalCount(listWorkflow.size());
			List<Object[]> listWorkflow = page.getResult();
			List<String> oatablepks = null;
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (Object[] objs : listWorkflow) {
					if (oatablepks == null) {
						oatablepks = new LinkedList<String>();
					}
					if (objs[1] != null) {
						oatablepks.add(StringUtil.castString(objs[1]));
					} else {
						logger.info("流程实例id【" + StringUtil.castString(objs[0])
								+ "】的流程业务id为null！");
					}
				}
			}
			String[] oatablepkis = null;
			if (oatablepks != null && !oatablepks.isEmpty()) {
				if (oatablepkis == null) {
					oatablepkis = new String[oatablepks.size()];
				}
				oatablepks.toArray(oatablepkis);
			}
			Object[] obj = getInfoItemPage(page, workflowName, formId, false,
					oatablepkis);
			List showColumnList = (List) obj[0];
			Map tempMap = (Map) obj[4];// 存放业务数据【业务数据ID：业务数据】
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
			// 增加工作流中的显示字段
			/*
			 * String businessName =
			 * ServletActionContext.getRequest().getParameter(WORKFLOW_TITLE);
			 * if(businessName != null && !"".equals(businessName)) {
			 * paramsMap.put("businessName", businessName); }
			 */
			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表
				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(ServletActionContext.getRequest()
						.getParameter(WORKFLOW_TITLE));
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			showColumnList.add(new String[] { "processStatus", "状态", "0",
					"processStatus" });// 显示字段列表
			showColumnList.add(new String[] { "cruuentUserName", "当前处理人", "0",
					"cruuentUserName" });// 显示字段列表
			showColumnList.add(new String[] { "cruuentUserDept", "当前部门", "0",
					"cruuentUserDept" });// 显示字段列表
			if (parameter.getHandleKind() != null) {
				showColumnList.add(new String[] { "processstartdate", "发起时间",
						"0", "processstartdate" });// 显示字段列表
			}
			// 按任务类型搜索
			EFormComponent taskTypeSelect = new EFormComponent();
			taskTypeSelect.setType("Strong.Form.Controls.ComboxBox");
			taskTypeSelect.setLable("状态");
			taskTypeSelect.setFieldName("state");
			String items = "0,在办,0;1,办毕,1;";
			taskTypeSelect.setItems(items);
			taskTypeSelect.setValue(state);
			queryColumnList.add(taskTypeSelect);
			List<Map> showList = new ArrayList<Map>();
			System.out.println();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				for (int i = 0; i < listWorkflow.size(); i++) {
					Object[] info = (Object[]) listWorkflow.get(i);
					String processInstanceId = info[0].toString();
					handlerAndDeptInfo = sendDocDesktopManager
							.getHandlerAndDeptInfo(processInstanceId,
									processInstanceId, currentuserinfo,
									currentuserDept);
					currentuserinfo = handlerAndDeptInfo[0];
					currentuserDept = handlerAndDeptInfo[1];

					String businessId = (String) info[1];// 得到业务数据主键信息
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					String processName = (String) info[5];// 流程名称
					if (workflowName != null
							&& !workflowName.equals(processName)) {
						if (formId != null
								&& (formId.startsWith("t") || formId
										.startsWith("T"))) {
						} else {
							continue;
						}
					}
					// ---------------------End--------------------------
					Map map = new HashMap();
					if (obj[3] != null) {// 存在表名
						if (tempMap.containsKey(businessId)) {
							map = new HashMap((Map) tempMap.get(businessId));
						}
					}
					Object processEndDate = info[2];// 流程结束时间
					String valueType = "在办";
					String status = "0";
					if (processEndDate != null) {
						valueType = "办毕";
						status = "1";
					}
					if ((!map.containsKey(WORKFLOW_TITLE))
							|| (map.containsKey(WORKFLOW_TITLE) && (map
									.get(WORKFLOW_TITLE) == null))) {
						map.put(WORKFLOW_TITLE, info[4]);
					}
					map.put("processStatus", valueType);
					map.put("cruuentUserName",
							currentuserinfo.get(info[0].toString()));
					map.put("cruuentUserDept",
							currentuserDept.get(info[0].toString()));
					map.put("processstartdate", info[6]);
					map.put(pkFieldName, info[0] + "$" + status);// 替换主键,即选择框的Value值,返回：流程实例id$流程状态
					showList.add(map);
				}
			}
			page.setResult(showList);
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (Exception ex) {
			logger.error("SystemException", ex);
			throw new SystemException(ex);
		}
	}

	/**
	 * @公文回收站列表(包括待办和已办的公文)
	 * @method gerRepeal
	 * @author 申仪玲(shenyl)
	 * @created 2012-6-18 下午10:41:13
	 * @param page
	 * @param parameter
	 * @return
	 * @throws Exception
	 * @return Object[]
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object[] getRepeal(Page page, ProcessedParameter parameter)
			throws Exception {
		try {
			Object[] sItems = { "startUserName", "processMainFormBusinessId",
					"processName", "processInstanceId", "businessName",
					"processStartDate", "processEndDate", "startUserId" };
			List toSelectItems = Arrays.asList(sItems);
			Map paramsMap = new HashMap();
			paramsMap.put("startUserId", userService.getCurrentUser()
					.getUserId());
			// paramsMap.put("processName", parameter.getWorkflowName());
			paramsMap.put("processSuspend", "1");
			String processStatus=parameter.getProcessStatus();
			if (processStatus != null && !"".equals(processStatus)
					&& !"2".equals(processStatus)) {
				paramsMap.put("processStatus", processStatus);
			}
			String workflowType=parameter.getWorkflowType();
			if (workflowType != null && !"".equals(workflowType)) {
				paramsMap.put("processTypeId", genWorkflowTypeList(workflowType));
			}
			//流程标题为过滤条件时
			String businessName = ServletActionContext.getRequest()
            .getParameter(WORKFLOW_TITLE);
           if (businessName != null && !"".equals(businessName)) {
             paramsMap.put("businessName", "%" + businessName + "%");
           }
			Map orderMap = new HashMap();
			orderMap.put("processStartDate", "1");
			page.setAutoCount(true);
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();

			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			if (parameter.getWorkflowName() != null
					&& !"".equals(parameter.getWorkflowName())) {
				if (customQuery.length() == 0) {
					customQuery.append(" pi.NAME_ like '"
							+ parameter.getWorkflowName() + "' ");
				} else {
					customQuery.append("and pi.NAME_ like '"
							+ parameter.getWorkflowName() + "' ");
				}
			}
//			page = workflow.getProcessInstanceByConditionForPage(page,
//					toSelectItems, paramsMap, orderMap, null, null, null, null);
			page = workflow.getProcessInstanceByConditionForPage(page,
					toSelectItems, paramsMap, orderMap,
					null, customFromItems.toString(),
					customQuery.toString(), null, null);
			
			List<TaskBusinessBean> listWorkflow = null;
			List<String> oatablepks = null;
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page.getResult() != null && !page.getResult().isEmpty()) {
				if (listWorkflow == null) {
					listWorkflow = new LinkedList<TaskBusinessBean>();
				}
				for (Object object : page.getResult()) {
					if (oatablepks == null) {
						oatablepks = new LinkedList<String>();
					}
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setStartUserName(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBsinessId(StringUtil
							.castString(objs[1]));
					if (objs[1] != null) {
						oatablepks.add(taskbusinessbean.getBsinessId());
					}
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[2]));
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[4] == null ? "" : objs[4]));
					//taskbusinessbean.setWorkflowStartDate((Date) objs[5]);
					taskbusinessbean.setWorkflowStartDate(objs[5]==null?null:sf.parse(StringUtil
							.castString(objs[5])));
					//taskbusinessbean.setWorkflowEndDate((Date) objs[6]);
					taskbusinessbean.setWorkflowEndDate(objs[6]==null?null:sf.parse(StringUtil
							.castString(objs[6])));
					taskbusinessbean.setStartUserId(StringUtil
							.castString(objs[7]));
					listWorkflow.add(taskbusinessbean);
				}
				listWorkflow = new ArrayList<TaskBusinessBean>(listWorkflow);
			}
			String[] oatablepkis = null;
			if (oatablepks != null && !oatablepks.isEmpty()) {
				if (oatablepkis == null) {
					oatablepkis = new String[oatablepks.size()];
				}
				oatablepks.toArray(oatablepkis);
			}
			Object[] obj = getInfoItemPage(page, parameter.getWorkflowName(),
					parameter.getFormId(), false, oatablepkis);
			Map tempMap = (Map) obj[4];// 存放业务数据【业务数据ID：业务数据】
			List showColumnList = (List) obj[0];
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];

			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表

				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(ServletActionContext.getRequest()
						.getParameter(WORKFLOW_TITLE));
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			// 添加图片列
			GridViewColumUtil.addPngColum(showColumnList);
			// showColumnList.add(new String[] {
			// GridViewColumUtil.getPNGColumName(), "",
			// GridViewColumUtil.getPNGColumType(),
			// GridViewColumUtil.getPNGColumName() });// 显示红黄警示牌
			List<Map> showList = new ArrayList<Map>();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					Map<String, String> parmMap = new HashMap<String, String>();
					// ---------------------End--------------------------
					if (obj[3] != null) {// 存在表名
						if (tempMap
								.containsKey(taskbusinessbean.getBsinessId())) {
							Map map = new HashMap(
									(Map) tempMap.get(taskbusinessbean
											.getBsinessId()));
							sendDocBaseManager.initProcessedShowMap(map,
									pkFieldName, taskbusinessbean, parmMap);
							showList.add(map);
						}else{
							Map map = new HashMap();
							sendDocBaseManager.initProcessedShowMap(map,
									pkFieldName, taskbusinessbean, parmMap);
							showList.add(map);
						}
					} else {
						Map map = new HashMap();
						sendDocBaseManager.initProcessedShowMap(map,
								pkFieldName, taskbusinessbean, parmMap);
						showList.add(map);
					}
				}
			}
			page.setResult(showList);
			// 设置过滤之后的总条数 shenyl 20120629
			//page.setTotalCount(showList.size());
			if (tempMap.isEmpty()) {
				page.setTotalCount(0);
			}
			listWorkflow = null;
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (SystemException ex) {
			logger.error("SystemException", ex);
			throw ex;
		}
	}

	/**
	 * 查询已办流程 通过流程状态 区分已办结流程和未办结流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-18 上午09:42:53
	 * @param page
	 *            分页对象
	 * @param parameter
	 *            封装参数
	 * @return 
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<
	 *         EFormComponent>,表名称]
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List
	 *         <Map<字段名称,字段值>>,List<EFormComponent>,表名称]
	 * @throws Exception
	 * @modify yanjian 2011-09-28 添加来文或办文排序处理
	 * @modify shenyl 2012-03-09
	 *         该方法修改，则getRepeal也须修改，两方法不同之处在于pi.ISSUSPENDED_值不同
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getProcessed(Page page, ProcessedParameter parameter)
			throws Exception {
		try {
			if (parameter.getFilterSign() == null
					|| "".equals(parameter.getFilterSign())) {
				parameter.setFilterSign("0");
			}
			// 增加工作流中的显示字段
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customSelectItems
					.append("jbpmbusiness.END_TIME,jbpmbusiness.BUSINESS_TYPE");
			// customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS,T_MAINA_CORCONFING MAINACTOR");
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			Object[] sItems = { "startUserName", "processMainFormBusinessId",
					"processName", "processInstanceId", "businessName",
					"processStartDate", "processEndDate", "startUserId" };
			if (!"1".equals(parameter.getIsSuspended())
					&& parameter.getProcessStatus() != null
					&& !"1".equals(parameter.getProcessStatus())) {
				NodeNameQuery nodenamequery = new NodeNameQuery();
				if (NodeNameConst.BAN_JIE.equals(parameter.getNodeName())) {
				} else if (NodeNameConst.YI_JIAN_ZHENG_XUN.equals(parameter
						.getNodeName())) {
					parameter.setNodeName("");
					parameter.setFilterYJZX("1");
				}
				if (parameter.getNodeName() != null
						&& !"".equals(parameter.getNodeName())
						&& !"null".equals(parameter.getWorkflowType())) {
					nodenamequery.setNodeName(parameter.getNodeName());
				}
				if (parameter.getWorkflowType() != null
						&& !"".equals(parameter.getWorkflowType())
						&& !"null".equals(parameter.getWorkflowType())) {
					nodenamequery.setProcessTypeId(parameter.getWorkflowType());
				}
				customFromItems.append("," + nodenamequery.getCustomFromItem());
				customSelectItems.append(","
						+ nodenamequery.getCustomSelectItem());
				customQuery.append(" and " + nodenamequery.getCustomQuery());
			}
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if ("1".equals(parameter.getIsSuspended())) {// 是否挂起
				if (customQuery.length() == 0) {
					customQuery.append("  pi.ISSUSPENDED_ = 1 ");
				} else {
					customQuery.append(" and pi.ISSUSPENDED_ = 1 ");
				}
			} else {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.ISSUSPENDED_ = 0 ");
				} else {
					customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
				}
			}
			if ("1".equals(parameter.getFilterYJZX())) {
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.BUSINESS_TYPE = '")
							.append(Tjbpmbusiness.BUSINESS_TYPE_YJZX)
							.append("' ");
				} else {
					customQuery.append(" and jbpmbusiness.BUSINESS_TYPE = '")
							.append(Tjbpmbusiness.BUSINESS_TYPE_YJZX)
							.append("' ");
				}
			} else {
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (")
							.append(Tjbpmbusiness.getShowableBusinessType())
							.append(") ");
				} else {
					customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
							.append(Tjbpmbusiness.getShowableBusinessType())
							.append(") ");
				}
			}
			if (parameter.getProcessStatus() != null
					&& !"".equals(parameter.getProcessStatus())
					&& !"2".equals(parameter.getProcessStatus())) {
				paramsMap.put("processStatus", parameter.getProcessStatus());
			}
			if (parameter.getWorkflowType() != null
					&& !"".equals(parameter.getWorkflowType())
					&& !"null".equals(parameter.getWorkflowType())) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ in("
							+ parameter.getWorkflowType() + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ in("
							+ parameter.getWorkflowType() + ") ");
				}
			}
			if (parameter.getExcludeWorkflowType() != null
					&& !"".equals(parameter.getExcludeWorkflowType())
					&& !"null".equals(parameter.getExcludeWorkflowType())) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ not in("
							+ parameter.getWorkflowType() + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ not in("
							+ parameter.getWorkflowType() + ") ");
				}
			}
			if (parameter.getWorkflowName() != null
					&& !"".equals(parameter.getWorkflowName())) {
				if (customQuery.length() == 0) {
					customQuery.append(" pi.NAME_ like '"
							+ parameter.getWorkflowName() + "' ");
				} else {
					customQuery.append("and pi.NAME_ like '"
							+ parameter.getWorkflowName() + "' ");
				}
			}
			if (null != parameter.getUserName()
					&& !"".equals(parameter.getUserName())) {// 流程发起人名称
				paramsMap.put("startUserName", parameter.getUserName());
			}
			// if(null != parameter.getUserId() &&
			// !"".equals(parameter.getUserId())){//主办人员id
			// if (customQuery.length() == 0) {
			// customQuery.append(" MAINACTOR.MAIN_ACTORID = '"
			// + parameter.getUserId() + "' ");
			// } else {
			// customQuery.append("and MAINACTOR.MAIN_ACTORID = '"
			// + parameter.getUserId() + "' ");
			// }
			// }
			// if (customQuery.length() == 0) {
			// customQuery.append("  MAINACTOR.PROCESSINSTANCE_ID = "+DataBaseUtil.SqlNumberToChar("@processInstanceId")+" ");
			// } else {
			// customQuery.append(" and MAINACTOR.PROCESSINSTANCE_ID = "+DataBaseUtil.SqlNumberToChar("@processInstanceId")+" ");
			// }
			Map orderMap = new HashMap<Object, Object>();
			// “在办事宜”、“收文处理”和“发文处理”中的列表按本人签批时间排序。（严建）
			if (SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC.equals(parameter
					.getSortType())) {
				orderMap.put("processStartDate", "0");
			} else if (SortConst.SORT_TYPE_PROCESSSTARTDATE_ASC
					.equals(parameter.getSortType())) {
				orderMap.put("processStartDate", "1");
			} else {
				orderMap.put("processStartDate", "1");
			}
			String userId = userService.getCurrentUser().getUserId();
			String queryWithTaskDate = "";// 按任务结束时间区间范围查询
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if (parameter.getStartDate() != null) {
				queryWithTaskDate = " and ti.END_  >= TO_DATE('"
						+ sdf.format(parameter.getStartDate())
						+ "', 'YYYY-MM-DD HH24:MI')";
			}
			if (parameter.getEndDate() != null) {
				queryWithTaskDate = " and ti.END_  <= TO_DATE('"
						+ sdf.format(parameter.getEndDate())
						+ "', 'YYYY-MM-DD HH24:MI')";
			}
			String businessName = ServletActionContext.getRequest()
					.getParameter(WORKFLOW_TITLE);
			if (businessName != null && !"".equals(businessName)) {
				if (customQuery.length() == 0) {
					customQuery.append(" pi.BUSINESS_NAME_ like '%"
							+ businessName + "%' ");
				} else {
					customQuery.append("and pi.BUSINESS_NAME_ like '%"
							+ businessName + "%' ");
				}
			}
			// 收文编号
			String RECV_NUM = ServletActionContext.getRequest().getParameter(
					"RECV_NUM");
			if (RECV_NUM != null && !"".equals(RECV_NUM)) {
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.RECV_NUM like '%"
							+ RECV_NUM + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.RECV_NUM like '%"
							+ RECV_NUM + "%' ");
				}
			}
			// 来文字号
			String DOC_NUMBER = ServletActionContext.getRequest().getParameter(
					"DOC_NUMBER");
			if (DOC_NUMBER != null && !"".equals(DOC_NUMBER)) {
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.DOC_NUMBER like '%"
							+ DOC_NUMBER + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.DOC_NUMBER like '%"
							+ DOC_NUMBER + "%' ");
				}
			}
			// 来文单位
			String ISSUE_DEPART_SIGNED = ServletActionContext.getRequest()
					.getParameter("ISSUE_DEPART_SIGNED");
			if (ISSUE_DEPART_SIGNED != null && !"".equals(ISSUE_DEPART_SIGNED)) {
				if (customQuery.length() == 0) {
					customQuery
							.append(" jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
									+ ISSUE_DEPART_SIGNED + "%' ");
				} else {
					customQuery
							.append("and jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
									+ ISSUE_DEPART_SIGNED + "%' ");
				}
			}
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(parameter.getFilterSign());
			processedParameter.setUserId(userId);
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setCustomSelectItems(customSelectItems);
			processedParameter.setQueryWithTaskDate(queryWithTaskDate);
			processedParameter.setFilterYJZX(parameter.getFilterYJZX());
			processedParameter.setShowSignUserInfo(parameter
					.getShowSignUserInfo());
			initProcessedFilterSign(processedParameter);
			customSelectItems = processedParameter.getCustomSelectItems();
			customFromItems = processedParameter.getCustomFromItems();
			customQuery = processedParameter.getCustomQuery();
			logger.info(customFromItems + "\n" + customQuery);
			page = workflow.getProcessInstanceByConditionForPage(page,
					toSelectItems, paramsMap, orderMap,
					customSelectItems.toString(), customFromItems.toString(),
					customQuery.toString(), null, null);
			destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);
			List<TaskBusinessBean> listWorkflow = null;
			List<String> oatablepks = null;
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page.getResult() != null && !page.getResult().isEmpty()) {
				if (listWorkflow == null) {
					listWorkflow = new LinkedList<TaskBusinessBean>();
				}
				for (Object object : page.getResult()) {
					if (oatablepks == null) {
						oatablepks = new LinkedList<String>();
					}
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setStartUserName(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBsinessId(StringUtil
							.castString(objs[1]));
					if (objs[1] != null) {
						oatablepks.add(taskbusinessbean.getBsinessId());
					}
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[2]));
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[4] == null ? "" : objs[4]));
					//taskbusinessbean.setWorkflowStartDate((Date) objs[5]);
					taskbusinessbean.setWorkflowStartDate(objs[5]==null?null:sf.parse(StringUtil.castString(objs[5])));
					//taskbusinessbean.setWorkflowEndDate((Date) objs[6]);
					taskbusinessbean.setWorkflowEndDate(objs[6]==null?null:sf.parse(StringUtil.castString(objs[6])));
					taskbusinessbean.setStartUserId(StringUtil
							.castString(objs[7]));
					taskbusinessbean.setEndTime(StringUtil.castString(objs[8]));
					taskbusinessbean.setBusinessType(StringUtil
							.castString(objs[9]));// 业务类型，是否是自办文中启动意见征询数据
					taskbusinessbean.setNodeName(objs.length > 10 ? StringUtil
							.castString(objs[10]) : "");
					listWorkflow.add(taskbusinessbean);
				}
				listWorkflow = new ArrayList<TaskBusinessBean>(listWorkflow);
			}
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			checkList.clear();
			String[] oatablepkis = null;
			if (oatablepks != null && !oatablepks.isEmpty()) {
				if (oatablepkis == null) {
					oatablepkis = new String[oatablepks.size()];
				}
				oatablepks.toArray(oatablepkis);
			}
			Page newpage = new Page(page.getPageSize(), page.isAutoCount());
			Object[] obj = getInfoItemPage(newpage,
					parameter.getWorkflowName(), parameter.getFormId(), false,
					oatablepkis);
			Map tempMap = (Map) obj[4];// 存放业务数据【业务数据ID：业务数据】
			List showColumnList = (List) obj[0];
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];

			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表
				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(ServletActionContext.getRequest()
						.getParameter(WORKFLOW_TITLE));
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			// 添加图片列
			if (!"1".equals(parameter.getProcessStatus())) {
				GridViewColumUtil.addPngColum(showColumnList);
			}
			// showColumnList.add(new String[]
			// {GridViewColumUtil.getPNGColumName(), "",
			// GridViewColumUtil.getPNGColumType(),
			// GridViewColumUtil.getPNGColumName() });// 显示红黄警示牌
			if (parameter.getProcessStatus() != null
					&& !"".equals(parameter.getProcessStatus())
					&& !"1".equals(parameter.getProcessStatus())) {
				if (parameter.getShowSignUserInfo() != null
						&& parameter.getShowSignUserInfo().equals("1")) {// 显示签收人信息
					showColumnList.add(new String[] { "showSignUserDept",
							"主办处室", "0", "showSignUserDept" });// 显示字段列表 12
					showColumnList.add(new String[] { "showSignUserName",
							"处室签收", "0", "showSignUserName" });// 显示字段列表 12
				} else {
					showColumnList.add(new String[] { "cruuentUserName",
							"当前办理人", "0", "cruuentUserName" });// 显示字段列表 12
					showColumnList.add(new String[] { "cruuentUserDept",
							"流程状态", "0", "cruuentUserDept" });// 显示字段列表 12
					if (parameter.getHandleKind() != null) {
						showColumnList.add(new String[] { "processstartdate",
								"发起时间", "0", "processstartdate" });// 显示字段列表
					}
				}
			} else {
				if (parameter.getHandleKind() != null) {
					showColumnList.add(new String[] { "processstartdate",
							"发起时间", "0", "processstartdate" });// 显示字段列表
				}
				showColumnList.add(new String[] { "processEndDate", "办结时间",
						SendDocManager.DATE_TYPE, "processEndDate" });
				showColumnList.add(new String[] { "startUserName", "主办人", "0",
						"startUserName" });
			}
			List<Map> showList = new ArrayList<Map>();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				Map<String, String[]> signInfoMap = new LinkedHashMap<String, String[]>();
				if ("1".equals(processedParameter.getShowSignUserInfo())) {
					signInfoMap = sendDocBaseManager.getSignInfo(oatablepkis);
				}
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				StringBuilder params = new StringBuilder();
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					params.append(taskbusinessbean.getInstanceId()).append(",");
				}
				Map<String, Map> taskInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder(
							"SELECT TI.PROCINST_,TI.ID_ AS TASKID,TI.START_ AS TASKSTARTDATE,TI.END_ AS TASKENDDATE ");
					sql.append(" FROM JBPM_TASKINSTANCE TI WHERE TI.PROCINST_ IN(");
					sql.append(params.toString());
					sql.append(") AND  TI.ACTORID_='"
							+ processedParameter.getUserId()
							+ "' AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						taskInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							if (!taskInfoMap.containsKey(String.valueOf(rsMap
									.get("PROCINST_")))) {
								taskInfoMap.put(
										String.valueOf(rsMap.get("PROCINST_")),
										rsMap);
							}
						}
					}
				}
				/* 所有的用户信息 */
				// Map<String, UserBeanTemp> AllUserIdMapUserBeanTem =
				// sendDocUploadManager
				// .getAllUserIdMapUserBeanTemp();
				Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					if (taskInfoMap == null) {
						continue;
					}
					Map taskMap = taskInfoMap.get(taskbusinessbean
							.getInstanceId());
					if (taskMap == null) {
						continue;
					}
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
//					taskbusinessbean.setTaskEndDate((Date) taskMap
//							.get("TASKENDDATE"));
					taskbusinessbean.setTaskEndDate(sdf.parse(StringUtil.castString(taskMap
							.get("TASKENDDATE")==null?"":taskMap
									.get("TASKENDDATE"))));
//					taskbusinessbean.setTaskStartDate((Date) taskMap
//							.get("TASKSTARTDATE"));
					taskbusinessbean.setTaskStartDate(sdf.parse(StringUtil.castString(taskMap
							.get("TASKSTARTDATE")==null?"":taskMap
									.get("TASKSTARTDATE"))));
					Map parmMap = new HashMap();
					parmMap.put("processstartdate",
							taskbusinessbean.getWorkflowStartDate());
					// 已分办 不查询当前处理人
					if (parameter.getProcessStatus() != null
							&& !"".equals(parameter.getProcessStatus())
							&& !"1".equals(parameter.getProcessStatus())
							&& !"1".equals(parameter.getShowSignUserInfo())) {
						// parmMap.put("cruuentUserName", new
						// StringBuilder(listTemp[0].toString()).substring(1,listTemp[0].toString().length()-1)+"["+new
						// StringBuilder(listTemp[1].toString()).substring(1,listTemp[1].toString().length()-1)+"]");
						StringBuilder cruuentUser = new StringBuilder();
						StringBuilder cruuentUserDept = new StringBuilder();
						if (taskbusinessbean.getWorkflowEndDate() != null) {
							cruuentUserDept.append("办结");
						} else {
							List[] listTemp = sendDocUploadManager
									.getUserBeanTempArrayOfProcessStatusByPiId(
											taskbusinessbean.getInstanceId(),
											AllUserIdMapUserBeanTem);
							for (int index = 0; index < listTemp[0].size(); index++) {
								if (" ".equals(listTemp[1].get(index)
										.toString())) {
									cruuentUser
											.append(","
													+ listTemp[0].get(index)
															.toString());
								} else {
									cruuentUser.append(","
											+ listTemp[0].get(index).toString()
											+ "["
											+ listTemp[1].get(index).toString()
											+ "]");
								}
							}
							cruuentUser.deleteCharAt(0);
							// cruuentUser.append(new
							// StringBuilder(listTemp[0].toString()).substring(1,listTemp[0].toString().length()-1)+"["+new
							// StringBuilder(listTemp[1].toString()).substring(1,listTemp[1].toString().length()-1)+"]");
							cruuentUserDept.append(taskbusinessbean
									.getNodeName());
						}
						parmMap.put(
								"cruuentUserName",
								"意见征询".equals(taskbusinessbean.getNodeName()) ? "意见征询"
										: cruuentUser);
						parmMap.put("cruuentUserDept", cruuentUserDept);
					}
					if (parameter.getProcessStatus() != null
							&& !"1".equals(parameter.getProcessStatus())) {
						parmMap.put("timeOut", taskbusinessbean.getEndTime());// 公文限时时间
					}
					if (parameter.getShowSignUserInfo() != null
							&& parameter.getShowSignUserInfo().equals("1")) {// 显示签收人信息
						if (signInfoMap.containsKey(taskbusinessbean
								.getBsinessId())) {
							String[] signInfo = signInfoMap
									.get(taskbusinessbean.getBsinessId());
							parmMap.put("showSignUserName", signInfo[0]);
							parmMap.put("showSignUserDept", signInfo[1]);
						} else {
							parmMap.put("showSignUserName", "");
							parmMap.put("showSignUserDept", "");
						}
					}
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					if (parameter.getWorkflowName() != null
							&& !parameter.getWorkflowName().equals(
									taskbusinessbean.getWorkflowName())) {
						if (parameter.getFormId() != null
								&& (parameter.getFormId().startsWith("t") || parameter
										.getFormId().startsWith("T"))) {
						} else {
							continue;
						}
					}
					Map map = new HashMap();
					// ---------------------End--------------------------
					if (obj[3] != null) {// 存在表名
						if (tempMap
								.containsKey(taskbusinessbean.getBsinessId())) {
							map = new HashMap(
									(Map) tempMap.get(taskbusinessbean
											.getBsinessId()));
						}
					}
					map.put("processStatus", parameter.getProcessStatus());// 流程状态
					sendDocBaseManager.initProcessedShowMap(map, pkFieldName,
							taskbusinessbean, parmMap);
					showList.add(map);
					if (map.containsKey("SENDDOC_ISSUE_DEPART_SIGNED")
							&& !"1".equals(processedParameter
									.getShowSignUserInfo())) {
						map.put("SENDDOC_ISSUE_DEPART_SIGNED",
								userService.getUserDepartmentByUserId(
										taskbusinessbean.getStartUserId())
										.getOrgName());
					}
				}
			}
			page.setResult(showList);
			if (page.getResult().isEmpty()) {
				page.setTotalCount(0);
			}
			listWorkflow = null;
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (SystemException ex) {
			logger.error("SystemException", ex);
			throw ex;
		}
	}

	/**
	 * @method getRepeal
	 * @author 申仪玲
	 * @created 2012-3-9 上午11:25:41
	 * @description 取公文回收站中废除的流程列表，该方法直接参考getProcessed方法
	 *              与getProcessed方法不同之处在于pi.ISSUSPENDED_的取值不同(0/1分别代表非挂起/挂起)
	 * @return Object[] 返回类型
	 * @see {@link #getProcessed(Page, ProcessedParameter)}
	 */
	@Deprecated
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Transactional(readOnly = true)
	public Object[] getRepeal(Page page, String workflowName, String formId,
			String workflowType, String userName, String isBackSpace,
			Date startDate, Date endDate, String processStatus,
			String sortType, List taskIdList, String showSignUserInfo,
			String filterSign) throws Exception {
		try {
			if (filterSign == null || "".equals(filterSign)) {
				filterSign = "0";
			}
			Object[] obj = getInfoItemPage(page, workflowName, formId, false);
			List list = (List) obj[1];
			Map tempMap = new HashMap();// 存放业务数据【业务数据ID：业务数据】
			StringBuilder ATbusinessId = null;
			if (obj[3] != null) {// 存在表名
				StringBuilder ATbusinessIdTemp = null;
				String businessId = null;
				for (int j = 0; j < list.size(); j++) {// 查询到的业务数据
					if (ATbusinessIdTemp == null) {
						ATbusinessIdTemp = new StringBuilder();
					}
					Map map = (Map) list.get(j);
					String tablename = (String) obj[3];
					String pkname = new ArrayList<String>(map.keySet()).get(0);
					String pkvalue = (String) map.get(pkname);
					// businessId format eg:
					// 'T_OARECVDOC;OARECVDOCID;2d58579d05064783ab086d3bf0ee19d3'
					businessId = " '" + tablename + ";" + pkname + ";"
							+ pkvalue + "' ";
					tempMap.put(tablename + ";" + pkname + ";" + pkvalue, map);
					if (j == 0) {
						ATbusinessIdTemp.append(" @businessId = " + businessId);
					} else {
						ATbusinessIdTemp.append(" or @businessId = "
								+ businessId);
					}
				}
				if (ATbusinessIdTemp != null) {
					ATbusinessId = new StringBuilder().append(" (")
							.append(ATbusinessIdTemp).append(") ");
				}
			}
			List showColumnList = (List) obj[0];
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
			// 增加工作流中的显示字段

			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表
				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(ServletActionContext.getRequest()
						.getParameter(WORKFLOW_TITLE));
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customSelectItems.append("jbpmbusiness.END_TIME,jbpmbusiness.BUSINESS_TYPE");
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			Object[] sItems = { "startUserName", "processMainFormBusinessId",
					"processName", "processInstanceId", "businessName",
					"processStartDate", "processEndDate" };
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			// paramsMap.put("processSuspend", "0");// 取挂起任务
			if (customQuery.length() == 0) {
				customQuery.append("  pi.ISSUSPENDED_ = 1 ");
			} else {
				customQuery.append(" and pi.ISSUSPENDED_ = 1 ");
			}
			if (ATbusinessId != null) {
				if (customQuery.length() == 0) {
					customQuery.append(ATbusinessId);
				} else {
					customQuery.append(" and ").append(ATbusinessId);
				}
			}
			if (processStatus != null && !"".equals(processStatus)
					&& !"2".equals(processStatus)) {
				paramsMap.put("processStatus", processStatus);
			}
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				paramsMap.put("processTypeId",
						genWorkflowTypeList(workflowType));
			}
			if (workflowName != null && !"".equals(workflowName)) {
				if (customQuery.length() == 0) {
					customQuery
							.append(" pi.NAME_ like '" + workflowName + "' ");
				} else {
					customQuery.append("and pi.NAME_ like '" + workflowName
							+ "' ");
				}
			}
			if (null != userName && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			Map orderMap = new HashMap<Object, Object>();
			// “在办事宜”、“收文处理”和“发文处理”中的列表按本人签批时间排序。（严建）
			if (SortConst.SORT_TYPE_PROCESSSTARTDATE_ASC.equals(sortType)) {
				orderMap.put("processStartDate", "0");
			} else {
				orderMap.put("processStartDate", "1");
			}
			String userId = userService.getCurrentUser().getUserId();
			String queryWithTaskDate = "";// 按任务结束时间区间范围查询
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if (startDate != null) {
				queryWithTaskDate = " and ti.END_  >= TO_DATE('"
						+ sdf.format(startDate) + "', 'YYYY-MM-DD HH24:MI')";
			}
			if (endDate != null) {
				queryWithTaskDate = " and ti.END_  <= TO_DATE('"
						+ sdf.format(endDate) + "', 'YYYY-MM-DD HH24:MI')";
			}
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(filterSign);
			processedParameter.setUserId(userId);
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setQueryWithTaskDate(queryWithTaskDate);
			initProcessedFilterSign(processedParameter);
			logger.info(customFromItems + "\n" + customQuery);
			page = workflow.getProcessInstanceByConditionForPage(page,
					toSelectItems, paramsMap, orderMap,
					customSelectItems.toString(), customFromItems.toString(),
					customQuery.toString(), null, null);
			destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);
			List<TaskBusinessBean> listWorkflow = null;
			if (page.getResult() != null && !page.getResult().isEmpty()) {
				if (listWorkflow == null) {
					listWorkflow = new LinkedList<TaskBusinessBean>();
				}
				for (Object object : page.getResult()) {
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					// objs[8]为bussinessType,当为"0"时，代表意见征询，过滤掉 申仪玲 20120324
					String bussnessType = "";
					if (objs[8] != null) {
						bussnessType = StringUtil.castString(objs[8]);
					}
					if (!bussnessType.equals("0")) {
						taskbusinessbean.setStartUserName(StringUtil
								.castString(objs[0]));
						taskbusinessbean.setBsinessId(StringUtil
								.castString(objs[1]));
						taskbusinessbean.setWorkflowName(StringUtil
								.castString(objs[2]));
						taskbusinessbean.setInstanceId(StringUtil
								.castString(objs[3]));
						taskbusinessbean.setBusinessName(StringUtil
								.castString(objs[4]));
						taskbusinessbean.setWorkflowStartDate((Date) objs[5]);
						taskbusinessbean.setWorkflowEndDate((Date) objs[6]);
						taskbusinessbean.setEndTime(StringUtil
								.castString(objs[7]));
						listWorkflow.add(taskbusinessbean);
					}
				}
				listWorkflow = new ArrayList<TaskBusinessBean>(listWorkflow);
			}
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			checkList.clear();
			// 添加图片列
			GridViewColumUtil.addPngColum(showColumnList);
			// showColumnList.add(new String[] {
			// GridViewColumUtil.getPNGColumName(), "",
			// GridViewColumUtil.getPNGColumType(),
			// GridViewColumUtil.getPNGColumName() });// 显示红黄警示牌
			if (processStatus != null && !"".equals(processStatus)
					&& !"1".equals(processStatus)) {
				if (showSignUserInfo != null && showSignUserInfo.equals("1")) {// 显示签收人信息

				} else {
					showColumnList.add(new String[] { "cruuentUserName",
							"当前办理人", "0", "cruuentUserName" });// 显示字段列表 12
					showColumnList.add(new String[] { "cruuentUserDept",
							"当前部门", "0", "cruuentUserDept" });// 显示字段列表 12
				}
			} else {
				showColumnList.add(new String[] { "processEndDate", "办结时间",
						SendDocManager.DATE_TYPE, "processEndDate" });
				showColumnList.add(new String[] { "startUserName", "主办人", "0",
						"startUserName" });
			}
			List<Map> showList = new ArrayList<Map>();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				// List<String> processInstanceIds = new LinkedList<String>();
				StringBuilder params = new StringBuilder();
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					// processInstanceIds.add(taskbusinessbean.getInstanceId());
					params.append(taskbusinessbean.getInstanceId()).append(",");
				}
				Map<String, Map> taskInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder(
							"SELECT TI.PROCINST_,TI.ID_ AS TASKID,TI.START_ AS TASKSTARTDATE,TI.END_ AS TASKENDDATE ");
					sql.append(" FROM JBPM_TASKINSTANCE TI WHERE TI.PROCINST_ IN(");
					sql.append(params.toString());
					sql.append(")  AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						taskInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							if (!taskInfoMap.containsKey(String.valueOf(rsMap
									.get("PROCINST_")))) {
								taskInfoMap.put(
										String.valueOf(rsMap.get("PROCINST_")),
										rsMap);
							}
						}
					}
				}
				/* 所有的用户信息 */
				Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
				// Map<String, UserBeanTemp> signtaskinfouserinfomap = null;
				if (showSignUserInfo != null && showSignUserInfo.equals("1")) {// 显示签收人信息
					showColumnList.add(new String[] { "showSignUserDept",
							"主办处室", "0", "showSignUserDept" });// 显示字段列表 12
					showColumnList.add(new String[] { "showSignUserName",
							"处室签收", "0", "showSignUserName" });// 显示字段列表 12
					/* 根据一组流程实例id获取流程签收人的信息 key:流程实例id，value:签收人信息 */
					// signtaskinfouserinfomap = sendDocBaseManager
					// .getSignTaskInfoUserInfoForMap(processInstanceIds,
					// AllUserIdMapUserBeanTem);
				}
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					Map taskMap = taskInfoMap.get(taskbusinessbean
							.getInstanceId());
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
					taskbusinessbean.setTaskEndDate((Date) taskMap
							.get("TASKENDDATE"));
					taskbusinessbean.setTaskStartDate((Date) taskMap
							.get("TASKSTARTDATE"));
					Map<String, String> parmMap = new HashMap<String, String>();
					if (processStatus != null && !"".equals(processStatus)
							&& !"1".equals(processStatus)) {
						List[] listTemp = sendDocUploadManager
								.getUserBeanTempArrayOfProcessStatusByPiId(
										taskbusinessbean.getInstanceId(),
										AllUserIdMapUserBeanTem);
						String currentuser = listTemp[0].get(0).toString();
						String currentorg = listTemp[1].get(0).toString();
						parmMap.put("cruuentUserName", currentuser);
						parmMap.put("cruuentUserDept", currentorg);
					}
					if (processStatus != null && !"1".equals(processStatus)) {
						parmMap.put("timeOut", taskbusinessbean.getEndTime());// 公文限时时间
					}
					if (showSignUserInfo != null
							&& showSignUserInfo.equals("1")) {// 显示签收人信息
						// String showSignUserName = "";
						// String showSignUserDept = "";
						// if (signtaskinfouserinfomap
						// .containsKey(taskbusinessbean.getInstanceId())) {
						// UserBeanTemp userbeantemp = signtaskinfouserinfomap
						// .get(taskbusinessbean.getInstanceId());
						// showSignUserName = userbeantemp.getUserName();
						// showSignUserDept = userbeantemp.getOrgName();
						// }
						// parmMap.put("showSignUserName", showSignUserName);
						// parmMap.put("showSignUserDept", showSignUserDept);
						String[] signInfo = sendDocBaseManager
								.getSignInfo(taskbusinessbean.getBsinessId());
						parmMap.put("showSignUserName", signInfo[0]);
						parmMap.put("showSignUserDept", signInfo[1]);
					}
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					if (workflowName != null
							&& !workflowName.equals(taskbusinessbean
									.getWorkflowName())) {
						if (formId != null
								&& (formId.startsWith("t") || formId
										.startsWith("T"))) {

						} else {
							continue;
						}
					}
					// ---------------------End--------------------------
					if (obj[3] != null) {// 存在表名
						if (tempMap
								.containsKey(taskbusinessbean.getBsinessId())) {
							Map map = new HashMap(
									(Map) tempMap.get(taskbusinessbean
											.getBsinessId()));
							sendDocBaseManager.initProcessedShowMap(map,
									pkFieldName, taskbusinessbean, parmMap);
							showList.add(map);
						}
					} else {
						Map map = new HashMap();
						sendDocBaseManager.initProcessedShowMap(map,
								pkFieldName, taskbusinessbean, parmMap);
						showList.add(map);
					}
				}
			}
			page.setResult(showList);
			if (tempMap.isEmpty()) {
				page.setTotalCount(0);
			}
			listWorkflow = null;
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (SystemException ex) {
			logger.error("SystemException", ex);
			throw ex;
		}
	}

	/**
	 * 对于同一节点意见累加
	 * 
	 * @author:邓志城
	 * @date:2009-12-22 上午11:52:17
	 * @param array
	 * @return
	 */
	private JSONArray getJSONObject(JSONArray array) {
		/*
		 * JSONArray newArray = new JSONArray(); for(int
		 * i=0;i<array.size();i++){ JSONObject obji = array.getJSONObject(i);
		 * 
		 * for(int j=0;j<array.size();j++){ JSONObject objj =
		 * array.getJSONObject(j); if(obji != objj){ if(obji.get("fieldName") ==
		 * null || objj.get("fieldName") ==null){ continue; }
		 * if(obji.get("fieldName").equals(objj.get("fieldName"))){ StringBuffer
		 * sb = new StringBuffer();
		 * sb.append(obji.get("infomation")).append(objj.get("infomation"));
		 * obji.put("infomation", sb.toString()); } } }
		 * if(!this.isExist(newArray, obji)){ newArray.add(obji); } }
		 */
		Map<String, List<String>> info = new HashMap<String, List<String>>();
		JSONArray newArray = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String key = obj.getString("fieldName");
			if (key != null) {
				String value = obj.getString("infomation");
				if (info.containsKey(key)) {
					info.get(key).add(value);
				} else {
					List<String> valueList = new ArrayList<String>();
					valueList.add(value);
					info.put(key, valueList);
				}
			}
		}
		Set<String> keySet = info.keySet();
		for (String key : keySet) {
			List<String> valueList = info.get(key);
			StringBuilder valueStr = new StringBuilder();
			for (int j = 0; j < valueList.size(); j++) {
				valueStr.append(valueList.get(j));
			}
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("fieldName", key);
			jsonObj.put("infomation", valueStr.toString());
			newArray.add(jsonObj);
		}
		info.clear();
		return newArray;
	}

	/*	*//**
	 * 根据任务实例Id获取每个节点的处理意见和对应的业务特殊字段
	 * 
	 * @author 喻斌
	 * @date Dec 19, 2009 2:20:19 PM
	 * @param taskInstanceId
	 *            -任务实例Id
	 * @return List<Object[]> -每个节点的处理意见和对应的业务特殊字段<br>
	 *         <p>
	 *         Object[]{(0)任务实例Id, (1)处理意见内容, (2)处理人Id, (3)处理人名称, (4)处理时间,
	 *         (5)节点对应的业务特殊字段, (6)节点名称,(7)节点id}
	 *         </p>
	 * @throws WorkflowException
	 */
	/*
	 * @Transactional(readOnly = true) public List<Object[]>
	 * getProcessHandlesAndBusiFlagByTaskId(String taskInstanceId) throws
	 * WorkflowException{ return
	 * workflow.getProcessHandlesAndBusiFlagByTaskId(taskInstanceId); }
	 */

	/**
	 * 得到任务绑定的控件对应的输入意见
	 * 
	 * @author:邓志城
	 * @date:2009-12-19 下午04:53:01
	 * @param taskInstanceId
	 * @return
	 */
	public String getInstro(String taskInstanceId) throws Exception {
		JSONArray jsons = new JSONArray();// 存储所有返回到前台页面的信息:JSON格式
		try {
			List<Object[]> list = workflow
					.getProcessHandlesAndBusiFlagByTaskId(taskInstanceId);// 得到所有处理意见
			JSONObject flag = new JSONObject();// 存储当前节点对应的表单域设置信息
			Object[] cunrrentNodeInfo = workflow
					.getBusiFlagByTaskId(taskInstanceId);
			String strFlag = (String) cunrrentNodeInfo[3];// 得到当前节点设置的域信息
			if (strFlag != null) {
				strFlag = URLDecoder.decode(strFlag, "utf-8");
				strFlag = strFlag.replaceAll("#", ",");
				JSONArray jsonArray = JSONArray.fromObject(strFlag);
				JSONObject fbj = jsonArray.getJSONObject(0);
				if (fbj.containsKey("type")) {// 输入意见字段
					jsonArray.remove(fbj);
				}
				flag.put("otherField", jsonArray.toString());// 其他字段的绑定信息
			}
			String temp = " ";
			for (int i = 0; i < list.size() - 1; i++) {
				Object[] objs = list.get(i);
				String time = (String) ConvertUtils.convert(objs[4],
						String.class);
				if (time != null) {
					time = time.substring(0, 10);
				}
				JSONObject json = new JSONObject();
				if (objs[5] == null) {
					continue;
				}
				String info = objs[5].toString();
				info = URLDecoder.decode(info, "utf-8");
				info = info.replaceAll("#", ",");
				JSONArray array = JSONArray.fromObject(info);
				JSONObject firstObj = array.getJSONObject(0);
				if (firstObj.containsKey("type")) {// 输入意见字段
					json.put("fieldName", firstObj.get("fieldName"));

					if (objs[1] == null || "null".equals(objs[1])
							|| "".equals(objs[1])) {
						temp = " ";
					} else {
						temp = objs[1].toString();
					}
					json.put("infomation", "   [" + objs[6] + "]:" + temp
							+ "\n    " + objs[3] + "    " + time + "\n");// 意见信息
					jsons.add(json);
				}
			}
			jsons = this.getJSONObject(jsons);
			if (flag.size() > 0) {
				jsons.add(flag);
			}
		} catch (Exception e) {
			logger.error("读取表单域设置信息时出现异常", e);
		}
		return jsons.toString();

	}

	/**
	 * 得到委派|指派的流程列表
	 * 
	 * @author:邓志城
	 * @date:2010-11-21 下午06:26:46
	 * @param page
	 *            分页对象
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            流程启动表单id
	 * @param workflowType
	 *            流程类型
	 * @param assignType
	 *            委托类型(“0”：委托；“1”：指派；“2”：全部)
	 * @param userName
	 *            被委托人
	 * @param startDate
	 *            流程启动开始日期
	 * @param endDate
	 *            流程启动截止日期
	 * @return 
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<
	 *         EFormComponent>,表名称]
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getEntrust(Page page, String workflowName, String formId,
			String workflowType, String assignType, String userName,
			Date startDate, Date endDate) {
		try {
			Object[] toSelectItems = { "taskId", "processStartDate",
					"startUserName", "processMainFormBusinessId",
					"processName", "processTypeId", "toAssignHandlerName",
					"assignType", "processInstanceId", "businessName" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("assignHandlerId", userService.getCurrentUser()
					.getUserId());// 委托/指派人Id(必须同时查询assignType属性)
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				paramsMap.put("processTypeId",
						genWorkflowTypeList(workflowType));
			}
			if (assignType != null && !"".equals(assignType)) {
				paramsMap.put("assignType", assignType);// 委托类型(“0”：委托；“1”：指派；“2”：全部)
			} else {
				paramsMap.put("assignType", "2");
			}
			if (workflowName != null && !"".equals(workflowName)) {
				paramsMap.put("processName", workflowName);
			}
			if (null != userName && !"".equals(userName)) {
				paramsMap.put("toAssignHandlerName", userName);
			}
			if (null != startDate) {
				paramsMap.put("processStartDateStart", startDate);
			}
			if (null != endDate) {
				paramsMap.put("processStartDateEnd", endDate);
			}
			String businessName = ServletActionContext.getRequest()
					.getParameter(WORKFLOW_TITLE);
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", businessName);
			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			page.setAutoCount(true);
			page = workflow.getTaskInfosByConditionForPage(page, sItems,
					paramsMap, orderMap, null, null, null, null);
			List<Object[]> listWorkflow = page.getResult();
			List<String> oatablepks = null;
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (Object[] objs : listWorkflow) {
					if (oatablepks == null) {
						oatablepks = new LinkedList<String>();
					}
					if (objs[3] != null) {
						oatablepks.add(StringUtil.castString(objs[3]));
					}
				}
			}
			String[] oatablepkis = null;
			if (oatablepks != null && !oatablepks.isEmpty()) {
				if (oatablepkis == null) {
					oatablepkis = new String[oatablepks.size()];
				}
				oatablepks.toArray(oatablepkis);
			}

			Object[] obj = getInfoItemPage(page, workflowName, formId, false,
					oatablepkis);
			List showColumnList = (List) obj[0];
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
			Map busidListMap = (Map) obj[4];
			// 增加工作流中的显示字段
			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表
				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(businessName);
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}

			showColumnList.add(new String[] { "toAssignHandlerName", "被委派人",
					"0", "toAssignHandlerName" });// 显示字段列表
			showColumnList.add(new String[] { "processStartDate", "流程启动日期",
					DATE_TYPE, "processStartDate" });// 显示字段列表

			// 按拟稿人搜索
			EFormComponent startUserName = new EFormComponent();
			startUserName.setType("Strong.Form.Controls.Edit");
			startUserName.setFieldName("userName");
			startUserName.setValue(userName);
			startUserName.setLable("被委派人");
			queryColumnList.add(startUserName);

			List<Map> showList = new ArrayList<Map>();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				for (int i = 0; i < listWorkflow.size(); i++) {
					Object[] info = (Object[]) listWorkflow.get(i);
					String businessId = (String) info[3];// 得到业务数据主键信息
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					String processName = (String) info[4];// 流程名称
					if (workflowName != null
							&& !workflowName.equals(processName)) {
						continue;
					}
					// ---------------------End--------------------------
					Map map = new HashMap();
					if (obj[3] != null) {// 存在表名
						if (businessId == null) {
							// 子流程的业务数据会返回NULL,因此这里需要将businessId==null加入逻辑判断中
							// 邓志城 2010年11月29日15:19:07
							String[] bussinessId = this
									.getFormIdAndBussinessIdByTaskId(info[0]
											.toString());
							businessId = bussinessId[0];
							if (!"0".equals(businessId)) {
								// 子流程数据有可能共享父流程的数据,这里就单独处理查询
								// 2010年12月20日11:14:41
								String[] args = businessId.split(";");
								String tableName = args[0];
								String pkName = args[1];
								String pkFieldValue = args[2];
								map = jdbcTemplate
										.queryForMap(new StringBuilder(
												"select * from ")
												.append(tableName)
												.append(" where ")
												.append(pkName).append(" = '")
												.append(pkFieldValue)
												.append("'").toString());
							}
						} else {
							if (busidListMap.containsKey(businessId)) {
								map = (Map) busidListMap.get(businessId);
							}
						}
					}
					map.put("toAssignHandlerName", info[6]);// 被委派人名称
					map.put("processStartDate",
							DateUtil.format((Date) info[1], "yyyy-MM-dd"));// 流程启动日期
					map.put(pkFieldName, info[0] + "$" + info[8]);// 替换主键,即选择框的Value值,返回：任务id$流程实例id
					showList.add(map);
				}

			}
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (Exception ex) {
			logger.error("SystemException", ex);
			throw new SystemException(ex);
		}

	}

	/**
	 * 得到当前用户待办任务
	 * 
	 * @author:邓志城
	 * @date:2010-11-24 下午04:26:00
	 * @param page
	 *            分页对象
	 * @return List<Object[任务实例id,接收日期,发起人,流程名称,流程实例Id,流程标题]>
	 * @modify author yanjian
	 *         List<Object[任务实例id,接收日期,发起人,流程名称,流程实例Id,流程标题,发起人ID,业务ID]>
	 * @see #getTodo(Page, ProcessedParameter)
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public Page getTodo(Page page, String workflowType) {
		try {
			Object[] toSelectItems = { "taskId", "taskStartDate",
					"startUserName", "processName", "processInstanceId",
					"businessName", "startUserId", "processMainFormBusinessId" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap
					.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				paramsMap.put("processTypeId",
						genWorkflowTypeList(workflowType));
			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			page = workflow.getTaskInfosByConditionForPage(page, sItems,
					paramsMap, orderMap, null, null, null, null);
		} catch (Exception e) {
			logger.error("得到待办事宜出错", e);
		}
		return page;
	}

	/**
	 * @author:luosy
	 * @description: “待办事宜”中显示，且急件需要“标红、置顶”；
	 * @date : 2011-7-3
	 * @modifyer:
	 * @description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<TaskBusinessBean> getTodoByRedType(String workflowType,
			String type, int num) {
		Page page = new Page(num, false);
		try {
			List<TaskBusinessBean> retList = null;
			Object[] sItems = { "taskId", "taskStartDate", "taskNodeId",
					"isReceived", "assignType" };
			List toSelectItems = Arrays.asList(sItems);
			sItems = null;
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap
					.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
			paramsMap.put("toAssignHandlerId", userService.getCurrentUser()
					.getUserId());
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customSelectItems
					.append("JBPMBUSINESS.END_TIME,WFBASEPROCESSFILE.REST2,JBPMBUSINESS.BUSINESS_TYPE");
			customFromItems
					.append("T_JBPM_BUSINESS JBPMBUSINESS,T_WF_BASE_PROCESSFILE WFBASEPROCESSFILE");
			initTodoSign(type, customSelectItems, customFromItems, customQuery);
			type = null;

			StringBuilder jjCustomQuery = new StringBuilder();
			StringBuilder njjCustomQuery = new StringBuilder();
			if (customQuery.length() == 0) {
				customQuery.append(" WFBASEPROCESSFILE.PF_NAME =  PI.NAME_ ");
			} else {
				customQuery
						.append(" AND  WFBASEPROCESSFILE.PF_NAME =  PI.NAME_ ");
			}
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (workflowType.startsWith("-")) {
					customQuery.append(" and pi.TYPE_ID_ not in("
							+ workflowType.substring(1) + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_  in(" + workflowType
							+ ") ");
				}
			}
			HttpServletRequest requset = ServletActionContext.getRequest();
			String excludeWorkflowType = requset
					.getParameter("excludeWorkflowType");
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
				customQuery.append(" and pi.TYPE_ID_ not in("
						+ excludeWorkflowType + ") ");
			}
			if (customQuery.length() == 0) {
				customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (")
						.append(Tjbpmbusiness.getShowableBusinessType())
						.append(") ");
			} else {
				customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
						.append(Tjbpmbusiness.getShowableBusinessType())
						.append(") ");
			}
			if (customQuery.length() == 0) {
				jjCustomQuery
						.append(" @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG > 0");
				njjCustomQuery
						.append(" @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG = 0");
			} else {
				jjCustomQuery
						.append(" AND  @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG > 0");
				njjCustomQuery
						.append(" AND  @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG = 0");
			}
			int pageTotalCount = 0;
			int jjpageTotalCount = 0; // 紧急状态总数
			int njjpageTotalCount = 0; // 非紧急状态总数

			// ------- 紧急 ---------
			Page jjpage = null;
			if (num > 0) {
				jjpage = new Page(num, true);
				jjpage = workflow.getTaskInfosByConditionForPage(jjpage,
						toSelectItems, paramsMap, orderMap,
						customSelectItems.toString(),
						customFromItems.toString(), customQuery.toString()
								+ jjCustomQuery.toString(), null, null);
				jjpageTotalCount = (jjpage.getTotalCount() < 0 ? 0 : jjpage
						.getTotalCount());
			}

			// // ------- 非紧急 ---------
			int mod = num - jjpageTotalCount;
			Page njjpage = null;
			if (mod > 0) {
				njjpage = new Page(mod, true);
				njjpage = workflow.getTaskInfosByConditionForPage(njjpage,
						toSelectItems, paramsMap, orderMap,
						customSelectItems.toString(),
						customFromItems.toString(), customQuery.toString()
								+ njjCustomQuery.toString(), null, null);
				njjpageTotalCount = (njjpage == null
						|| njjpage.getTotalCount() < 0 ? 0 : njjpage
						.getTotalCount());
			} else {
				njjpage = new Page(1, true);
				njjpage = workflow.getTaskInfosByConditionForPage(njjpage,
						toSelectItems, paramsMap, orderMap,
						customSelectItems.toString(),
						customFromItems.toString(), customQuery.toString()
								+ njjCustomQuery.toString(), null, null);
				njjpageTotalCount = (njjpage == null
						|| njjpage.getTotalCount() < 0 ? 0 : njjpage
						.getTotalCount());
				njjpage = null;
			}
			pageTotalCount = jjpageTotalCount + njjpageTotalCount;
			destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);

			StringBuilder params = new StringBuilder();
			if (jjpage != null && jjpage.getResult() != null
					&& !jjpage.getResult().isEmpty()) {
				retList = new LinkedList();
				for (Object object : jjpage.getResult()) {
					if (retList == null) {
						retList = new LinkedList<TaskBusinessBean>();
					}
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
					taskbusinessbean
							.setTaskStartDate((Date) (objs[1] == null ? new Date()
									: objs[1]));
					taskbusinessbean.setNodeId(StringUtil.castString(objs[2]));
					taskbusinessbean.setIsReceived(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setAssignType(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setEndTime(StringUtil.castString(objs[5]));
					taskbusinessbean.setWorkflowAliaName(StringUtil
							.castString(objs[6]));
					taskbusinessbean.setBusinessType(StringUtil
							.castString(objs[7]));
					taskbusinessbean.setBusinessName("<red>");
					retList.add(taskbusinessbean);
					params.append(StringUtil.castString(objs[0])).append(",");
				}
			}
			jjpage = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (njjpage != null && njjpage.getResult() != null
					&& !njjpage.getResult().isEmpty()) {
				for (Object object : njjpage.getResult()) {
					if (retList == null) {
						retList = new LinkedList<TaskBusinessBean>();
					}
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
					String stadate = StringUtil.castString(objs[1]);
					taskbusinessbean.setTaskStartDate(sdf.parse(stadate));
					taskbusinessbean.setNodeId(StringUtil.castString(objs[2]));
					taskbusinessbean.setIsReceived(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setAssignType(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setEndTime(StringUtil.castString(objs[5]));
					taskbusinessbean.setWorkflowAliaName(StringUtil
							.castString(objs[6]));
					taskbusinessbean.setBusinessType(StringUtil
							.castString(objs[7]));
					taskbusinessbean.setBusinessName("");
					retList.add(taskbusinessbean);
					params.append(StringUtil.castString(objs[0])).append(",");
				}
			}
			njjpage = null;

			if (retList != null) {
				Map<String, Map> processInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder();
					sql.append(" SELECT ")
							.append(" TI.TASKID AS  TASKID, ")
							.append(" TI.PROCESSID AS PROCESSINSTANCEID, ")
							.append(" PI.START_USER_NAME_ AS STARTUSERNAME, ")
							.append(" PI.BUSINESS_ID    AS PROCESSMAINFORMBUSINESSID, ")
							.append(" PI.NAME_          AS PROCESSNAME, ")
							.append(" PI.MAINFORM_ID_   AS PROCESSMAINFORMID, ")
							.append(" PI.TYPE_ID_       AS PROCESSTYPEID, ")
							.append(" PI.BUSINESS_NAME_ AS BUSINESSNAME, ")
							.append(" PI.START_		    AS PROCESSSTARTDATE, ")
							.append(" TI.ISBACKSPACE AS ISBACKSPACE ")
							// .append(" TI.ISBACKSPACE AS ISBACKSPACE, ")
							// .append(" TI.ISREASSIGN AS ISREASSIGN ")
							.append("  FROM  ")
							.append(" JBPM_PROCESSINSTANCE PI, ")
							.append(" (SELECT ")
							.append("T.ID_ AS TASKID,T.PROCINST_ AS PROCESSID,T.ISBACKSPACE_ AS ISBACKSPACE,T.ISREASSIGN_ AS ISREASSIGN  ")
							.append(" FROM ").append("JBPM_TASKINSTANCE T ")
							.append("WHERE ").append("T.ID_ IN (")
							.append(params.toString()).append("))TI ")
							.append(" WHERE ")
							.append(" PI.ID_ = TI.PROCESSID ");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						processInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							processInfoMap.put(
									String.valueOf(rsMap.get("TASKID")), rsMap);
						}
					}
				}
				retList = new ArrayList<TaskBusinessBean>(retList);
				for (TaskBusinessBean taskbusinessbean : retList) {
					Map map = processInfoMap.get(taskbusinessbean.getTaskId());
					taskbusinessbean.setInstanceId(StringUtil.castString(map
							.get("PROCESSINSTANCEID")));
					taskbusinessbean.setStartUserName(StringUtil.castString(map
							.get("STARTUSERNAME")));
					taskbusinessbean.setBsinessId(StringUtil.castString(map
							.get("PROCESSMAINFORMBUSINESSID")));
					taskbusinessbean.setWorkflowName(StringUtil.castString(map
							.get("PROCESSNAME")));
					if (taskbusinessbean.getWorkflowAliaName() == null
							|| "".equals(taskbusinessbean.getWorkflowAliaName())) {
						taskbusinessbean.setWorkflowAliaName(taskbusinessbean
								.getWorkflowName());
					}
					taskbusinessbean.setFormId(StringUtil.castString(map
							.get("PROCESSMAINFORMID")));
					taskbusinessbean.setWorkflowType(StringUtil.castString(map
							.get("PROCESSTYPEID")));
					taskbusinessbean
							.setBusinessName(taskbusinessbean.getBusinessName()
									+ StringUtil.castString(map
											.get("BUSINESSNAME") == null ? ""
											: map.get("BUSINESSNAME")));
					taskbusinessbean.setIsBackspace(StringUtil.castString(map
							.get("ISBACKSPACE")));
					// taskbusinessbean.setAssignType(StringUtil.castString(map
					// .get("ISREASSIGN")));
					taskbusinessbean.setWorkflowStartDate(sdf.parse(StringUtil.castString(map
							.get("PROCESSSTARTDATE")==null?"":map
									.get("PROCESSSTARTDATE"))));
				}

				page.setResult(retList);
				page.setTotalCount(pageTotalCount);
				retList = null;
			}
		} catch (Exception e) {
			logger.error("得到待办事宜出错", e);
		}
		return page;
	}

	/**
	 * @author:luosy
	 * @description: 待办中心列表(省财政厅专用)
	 * @date : 2011-5-16
	 * @modifyer:
	 * @description:
	 * @param page
	 * @param workflowType
	 * @param listType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getTodoCenter(String listType, int num) {
		List<Object[]> r = new ArrayList<Object[]>();
		// HttpServletRequest req = ServletActionContext.getRequest();
		try {
			Object[] sItems = { "taskId", "taskStartDate", "startUserName",
					"processName", "processInstanceId", "businessName",
					"processMainFormBusinessId" };
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap
					.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务

			// 判断待办类型
			if ("sp_div".equals(listType)) {// 审批件
				paramsMap.put("processTypeId", WorkFlowTypeConst.SENDDOC);
			} else if ("cy_div".equals(listType)) {// 呈阅件
				paramsMap.put("processTypeId", WorkFlowTypeConst.RECEDOC);
			}// else不指定类型

			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			java.lang.String customSelectItems = null;
			java.lang.String customFromItems = "";
			java.lang.String customQuery = "";
			/**/// Tjbpmbusiness jbpmbusiness,
			customSelectItems = "jbpmbusiness.END_TIME";
			customFromItems = "T_JBPM_BUSINESS jbpmbusiness";

			List retList = null;
			Page page = new Page(num, true);
			String CustomQueryExtension = "";
			if (!customQuery.equals("")) {
				CustomQueryExtension = " and ";
			}
			if (listType != null && "jj_div".equals(listType)) {// 急件
				// ------- 紧急 ---------
				CustomQueryExtension += " @businessId = jbpmbusiness.BUSINESS_ID and jbpmbusiness.PERSON_CONFIG_FLAG not like 0";
			} else {
				// // ------- 非紧急 ---------
				CustomQueryExtension += " @businessId = jbpmbusiness.BUSINESS_ID and jbpmbusiness.PERSON_CONFIG_FLAG like 0";
			}
			page = workflow.getTaskInfosByConditionForPage(page, toSelectItems,
					paramsMap, orderMap, customSelectItems, customFromItems,
					customQuery + CustomQueryExtension, null, null);
			retList = page.getResult();
			if (retList != null && !retList.isEmpty()) {
				r = retList;
			}

		} catch (Exception e) {
			logger.error("得到待办事宜出错", e);
		}
		return r;
	}
	
	/**
	 * 得到我的退文文件列表 支持自定义查询以及自定义显示列表.
	 * 
	 * @author:xush
	 * @date:1/8/2014 8:54 AM
	 * @param page
	 *            分页列表对象（暂无用途）
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            表单id
	 * @param workflowType
	 *            流程类型id -1表示所有非系统流程类型
	 * @param userName
	 *            拟稿人名称
	 * @param isBackSpace
	 *            是否是退回任务
	 * @param startDate
	 *            任务接收开始日期:查询用
	 * @param endDate
	 *            任务接收截止日期：查询用
	 * @return 
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<
	 *         EFormComponent>,表名称]
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getMyReturnList(Page page, ProcessedParameter parameter) {
		try {
				String workflowName = parameter.getWorkflowName();
				String formId = parameter.getFormId();
				String workflowType = parameter.getWorkflowType();
				String userName = parameter.getUserName();
				Date startDate = parameter.getStartDate();
				Date endDate = parameter.getEndDate();
				String isBackSpace = parameter.getIsBackSpace();
				String type = parameter.getType();
				String excludeWorkflowType = parameter.getExcludeWorkflowType();
				String orgId = parameter.getOrgId();

				// 增加工作流中的显示字段

				String businessName = ServletActionContext.getRequest()
						.getParameter(WORKFLOW_TITLE);

				StringBuilder customQuery = new StringBuilder();
				StringBuilder customFromItems = new StringBuilder();
				StringBuilder customSelectItems = new StringBuilder();
				customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
				customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
				Object[] sItems = { "taskId", "taskStartDate", "isBackspace",
						"assignType", "taskNodeId", "taskNodeName", "isReceived" };
				List toSelectItems = Arrays.asList(sItems);
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("taskType", "2");// 取非办结任务
				paramsMap.put("isBackspace", "1");// 取非办结任务
				if (customQuery.length() == 0) {
					customQuery.append("  pi.ISSUSPENDED_ = 0 ");
				} else {
					customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
				}
				User curuser = userService.getCurrentUser();
				paramsMap.put("startUserId", curuser.getUserId());// 当前用户办理任务
//				// 根据处室Id 取得该处室全部(待签收文件)
//				if (orgId != null) {
//					List<User> userList = userService.getUsersByOrgID(orgId);
//					if (!userList.isEmpty()) {
//						User firstUser = userList.get(0);
//						paramsMap.put("handlerId", firstUser.getUserId());// 当前用户办理任务
//					}
//				} else {
//					paramsMap.put("handlerId", curuser.getUserId());// 当前用户办理任务
//				}

				paramsMap.put("toAssignHandlerId", userService.getCurrentUser()
						.getUserId());
				String[] adpterWorkflowType = CustomQueryAdpter
						.adpterWorkflowType(new String[] { workflowType,
								excludeWorkflowType });
				workflowType = adpterWorkflowType[0];
				excludeWorkflowType = adpterWorkflowType[1];
				if (workflowType != null) {
					CustomQueryUtil.genWorkflowTypeStringForSql(workflowType,
							customQuery);
				}
				if (excludeWorkflowType != null) {
					CustomQueryUtil.genExcludeWorkflowTypeStringForSql(
							excludeWorkflowType, customQuery);
				}
				if (workflowName != null && !"".equals(workflowName)) {
					if (customQuery.length() == 0) {
						customQuery
								.append(" pi.NAME_ like '" + workflowName + "' ");
					} else {
						customQuery.append("and pi.NAME_ like '" + workflowName
								+ "' ");
					}
				}
				if (businessName != null && !"".equals(businessName)) {
					if (customQuery.length() == 0) {
						customQuery.append(" pi.BUSINESS_NAME_ like '%"
								+ businessName + "%' ");
					} else {
						customQuery.append("and pi.BUSINESS_NAME_ like '%"
								+ businessName + "%' ");
					}
				}
				if (null != userName && !"".equals(userName)) {
					paramsMap.put("startUserName", userName);
				}
				if (startDate != null) {
					paramsMap.put("taskStartDateStart", startDate);
				}
				if (endDate != null) {
					paramsMap.put("taskStartDateEnd", endDate);
				}

				if ("0".equals(isBackSpace)) {// 退回
					paramsMap.put("isBackspace", "1");
				} else if ("1".equals(isBackSpace)) {// 委托
					paramsMap.put("assignType", "0");
				} else if ("2".equals(isBackSpace)) {// 指派
					paramsMap.put("assignType", "1");
				} else if ("3".equals(isBackSpace)) {// 一般流转
					paramsMap.put("assignType", "3");
					paramsMap.put("isBackspace", "0");
				}
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("taskStartDate", "1");

				// 收文编号
				String RECV_NUM = ServletActionContext.getRequest().getParameter(
						"RECV_NUM");
				if (RECV_NUM != null && !"".equals(RECV_NUM)) {
					if (customQuery.length() == 0) {
						customQuery.append(" jbpmbusiness.RECV_NUM like '%"
								+ RECV_NUM + "%' ");
					} else {
						customQuery.append("and jbpmbusiness.RECV_NUM like '%"
								+ RECV_NUM + "%' ");
					}
				}
				// 来文字号
				String DOC_NUMBER = ServletActionContext.getRequest().getParameter(
						"DOC_NUMBER");
				if (DOC_NUMBER != null && !"".equals(DOC_NUMBER)) {
					if (customQuery.length() == 0) {
						customQuery.append(" jbpmbusiness.DOC_NUMBER like '%"
								+ DOC_NUMBER + "%' ");
					} else {
						customQuery.append("and jbpmbusiness.DOC_NUMBER like '%"
								+ DOC_NUMBER + "%' ");
					}
				}
				// 来文单位
				String ISSUE_DEPART_SIGNED = ServletActionContext.getRequest()
						.getParameter("ISSUE_DEPART_SIGNED");
				if (ISSUE_DEPART_SIGNED != null && !"".equals(ISSUE_DEPART_SIGNED)) {
					if (customQuery.length() == 0) {
						customQuery
								.append(" jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
										+ ISSUE_DEPART_SIGNED + "%' ");
					} else {
						customQuery
								.append("and jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
										+ ISSUE_DEPART_SIGNED + "%' ");
					}
				}

				// 是否签收
				String isReceived = ServletActionContext.getRequest().getParameter(
						"isReceived");
				if (isReceived != null && !"".equals(isReceived)
						&& !"null".equals(isReceived)) {
					if ("1".equals(isReceived)) {
						customQuery.append(" and ti.ISRECEIVE_  =  1");
					} else {
						customQuery.append(" and ti.ISRECEIVE_  is null ");
					}
				}

				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (")
							.append(Tjbpmbusiness.getShowableBusinessType())
							.append(") ");
				} else {
					customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
							.append(Tjbpmbusiness.getShowableBusinessType())
							.append(") ");
				}

				initTodoSign(type, customSelectItems, customFromItems, customQuery);

				type = null;
				page.setAutoCount(true);
				page = workflow.getTaskInfosByConditionForPage(page, toSelectItems,
						paramsMap, orderMap, customSelectItems.toString(),
						customFromItems.toString(), customQuery.toString(), null,
						null);

				destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
						customSelectItems, customFromItems, customQuery, null, null);

				List<TaskBusinessBean> listWorkflow = null;
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				List<String> oatablepks = null;
				if (page.getResult() != null && !page.getResult().isEmpty()) {
					listWorkflow = new LinkedList<TaskBusinessBean>();
					StringBuilder params = new StringBuilder();
					for (Object object : page.getResult()) {
						Object[] objs = (Object[]) object;
						TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
						taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
						taskbusinessbean.setTaskStartDate(objs[1]==null?null:sf.parse(StringUtil.castString(objs[1])));
						taskbusinessbean.setIsBackspace(StringUtil
								.castString(objs[2]));
						taskbusinessbean.setAssignType(StringUtil
								.castString(objs[3]));
						taskbusinessbean.setNodeId(StringUtil.castString(objs[4]));
						taskbusinessbean
								.setNodeName(StringUtil.castString(objs[5]));
						taskbusinessbean.setIsReceived(StringUtil
								.castString(objs[6]));
						listWorkflow.add(taskbusinessbean);
						params.append(StringUtil.castString(objs[0])).append(",");
					}
					Map<String, Map> processInfoMap = null;
					if (params.length() > 0) {
						params.deleteCharAt(params.length() - 1);
						StringBuilder sql = new StringBuilder();
						sql.append(" SELECT ")
								.append(" TI.TASKID           AS TASKID, ")
								.append(" TI.PROCESSID        AS PROCESSINSTANCEID, ")
								.append(" PI.START_USER_NAME_ AS STARTUSERNAME, ")
								.append(" PI.BUSINESS_ID      AS PROCESSMAINFORMBUSINESSID, ")
								.append(" PI.NAME_            AS PROCESSNAME, ")
								.append(" PI.BUSINESS_NAME_   AS BUSINESSNAME, ")
								.append(" BI.END_TIME         AS END_TIME, ")
								.append(" BI.BUSINESS_TYPE    AS BUSINESS_TYPE, ")
								.append(" PI.START_   		  AS STARTDATE, ")
								.append(" PI.START_USER_ID_   AS START_USER_ID ")
								.append(" FROM ")
								.append(" JBPM_PROCESSINSTANCE PI, ")
								.append(" T_JBPM_BUSINESS BI, ").append(" (")
								.append("SELECT ")
								.append("T.ID_       AS TASKID, ")
								.append("T.PROCINST_ AS PROCESSID ")
								.append("FROM ").append("JBPM_TASKINSTANCE T ")
								.append("WHERE ").append("T.ID_ IN (")
								.append(params.toString()).append(")")
								.append(")TI ").append(" WHERE ")
								.append(" PI.ID_ = TI.PROCESSID ")
								.append(" AND PI.BUSINESS_ID = BI.BUSINESS_ID");
						List<Map<String, Object>> taskList = jdbcTemplate
								.queryForList(sql.toString());
						logger.info(sql.toString());
						if (taskList != null && !taskList.isEmpty()) {
							processInfoMap = new HashMap<String, Map>();
							for (Map<String, Object> rsMap : taskList) {
								processInfoMap.put(
										String.valueOf(rsMap.get("TASKID")), rsMap);
							}
						}
					}

					for (TaskBusinessBean taskbusinessbean : listWorkflow) {
						Map map = processInfoMap.get(taskbusinessbean.getTaskId());
						if (map == null) {
							continue;
						}
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						if (oatablepks == null) {
							oatablepks = new LinkedList<String>();
						}
						taskbusinessbean.setInstanceId(StringUtil.castString(map
								.get("PROCESSINSTANCEID")));
						taskbusinessbean.setStartUserName(StringUtil.castString(map
								.get("STARTUSERNAME")));
						taskbusinessbean.setBsinessId(StringUtil.castString(map
								.get("PROCESSMAINFORMBUSINESSID")));
						if (taskbusinessbean.getBsinessId() != null) {
							oatablepks.add(taskbusinessbean.getBsinessId());
						}
						taskbusinessbean.setWorkflowName(StringUtil.castString(map
								.get("PROCESSNAME")));
						taskbusinessbean.setBusinessName(StringUtil.castString(map
								.get("BUSINESSNAME")));
						taskbusinessbean.setEndTime(StringUtil.castString(map
								.get("END_TIME")));
						taskbusinessbean.setBusinessType(StringUtil.castString(map
								.get("BUSINESS_TYPE")));
//						taskbusinessbean.setWorkflowStartDate((Date) map
//								.get("STARTDATE"));
						taskbusinessbean.setWorkflowStartDate(sdf.parse(StringUtil.castString(map
								.get("STARTDATE")==null?"":map
										.get("STARTDATE"))));
						
						// 处理子流程获取不到发起人的情况 Bug序号： 0000004533 yanjian 2012-07-02
						// 13:06
						taskbusinessbean
								.setStartUserId(map.get("START_USER_ID") == null ? curuser
										.getUserId() : StringUtil.castString(map
										.get("START_USER_ID")));
					}

					listWorkflow = new ArrayList<TaskBusinessBean>(listWorkflow);
				}
				String[] oatablepkis = null;
				if (oatablepks != null && !oatablepks.isEmpty()) {
					if (oatablepkis == null) {
						oatablepkis = new String[oatablepks.size()];
					}
					oatablepks.toArray(oatablepkis);
				}
				Page newpage = new Page(page.getPageSize(), page.isAutoCount());
				Object[] obj = getInfoItemPage(newpage, workflowName, formId,
						false, oatablepkis);
				Map tempMap = (Map) obj[4];// 存放业务数据【业务数据ID：业务数据】
				List showColumnList = (List) obj[0];
				// 添加图片列
				GridViewColumUtil.addPngColum(showColumnList);
				// showColumnList.add(new String[] {
				// GridViewColumUtil.getPNGColumName(), "",
				// GridViewColumUtil.getPNGColumType(),
				// GridViewColumUtil.getPNGColumName()});// 显示红黄警示牌
				List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
				if (obj[3] == null) {// 不存在表名,流程未挂接表单
					showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
							WORKFLOW_TITLE });// 显示字段列表

					EFormComponent startUserName = new EFormComponent();
					startUserName.setType("Strong.Form.Controls.Edit");
					startUserName.setFieldName(WORKFLOW_TITLE);
					startUserName.setValue(businessName);
					startUserName.setLable("流程标题");
					queryColumnList.add(startUserName);
				}
				if (parameter.getHandleKind() != null) {
					showColumnList.add(new String[] { "processstartdate", "发起时间",
							"5", "processstartdate" });//
				}
				showColumnList.add(new String[] { "currentusername", "上步办理人", "0",
						"currentusername" });//
				showColumnList.add(new String[] { "currentuserdept", "所在部门", "0",
						"currentuserdept" });//

				List<Map> showList = new ArrayList<Map>();
				if (listWorkflow != null && !listWorkflow.isEmpty()) {
					String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
					String pkFieldName = firstItem[0];
					List<Long> taskIdListTemp = new LinkedList<Long>();
					for (TaskBusinessBean taskbusinessbean : listWorkflow) {
						taskIdListTemp.add(new Long(taskbusinessbean.getTaskId()));
					}
					Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = sendDocUploadManager
							.getTaskIdMapPreTaskBeanTemper(taskIdListTemp);

					for (TaskBusinessBean taskbusinessbean : listWorkflow) {
						Map parmMap = new HashMap<String, String>();
						TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp
								.get(taskbusinessbean.getTaskId());
						if ("意见征询".equals(taskBeanTemp.getNodeName())) {// 处理意见征询的文
							ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
									.getBsinessId().split(";")[2]);
							parmMap.put("currentusername", "已征求完意见");
							parmMap.put("currentuserdept",
									toayjzx == null ? ""
											: toayjzx.getUnit() == null ? ""
													: toayjzx.getUnit());
						} else {
							String PreTaskActor = taskBeanTemp.getTaskActorName();
							String PreTaskActorOrgName = taskBeanTemp.getOrgName();
							parmMap.put("currentusername", PreTaskActor);
							parmMap.put("currentuserdept", PreTaskActorOrgName);
						}
						parmMap.put("processstartdate",
								taskbusinessbean.getWorkflowStartDate());
						parmMap.put("timeOut", taskbusinessbean.getEndTime());
						// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
						// 2010年12月10日9:11:23
						if (workflowName != null
								&& !workflowName.equals(taskbusinessbean
										.getWorkflowName())) {
							if (formId != null
									&& (formId.startsWith("t") || formId
											.startsWith("T"))) {

							} else {
								continue;
							}
						}
						// ---------------------End--------------------------
						Map map = new HashMap();
						if (obj[3] != null) {// 存在表名
							if (tempMap
									.containsKey(taskbusinessbean.getBsinessId())) {
								map = new HashMap(
										(Map) tempMap.get(taskbusinessbean
												.getBsinessId()));
							}
						}
						sendDocBaseManager.initTodoShowMap(map, pkFieldName,
								taskbusinessbean, parmMap);
						showList.add(map);
						if (map.containsKey("SENDDOC_ISSUE_DEPART_SIGNED")) {
							map.put("SENDDOC_ISSUE_DEPART_SIGNED",
									userService.getUserDepartmentByUserId(
											taskbusinessbean.getStartUserId())
											.getOrgName());
						}
					}
					page.setResult(showList);
					if (page.getResult().isEmpty()) {
						page.setTotalCount(0);
					}
				}
				return new Object[] { showColumnList, showList, queryColumnList,
						obj[3] };
			} catch (WorkflowException e) {
				logger.error("工作流查询异常", e);
				throw new SystemException(e);
			} catch (Exception ex) {
				logger.error("SystemException", ex);
				return null;
			}

		}


	/**
	 * 得到当前用户待办任务列表 支持自定义查询以及自定义显示列表.
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 下午09:03:45
	 * @param page
	 *            分页列表对象（暂无用途）
	 * @param workflowName
	 *            流程名称
	 * @param formId
	 *            表单id
	 * @param workflowType
	 *            流程类型id -1表示所有非系统流程类型
	 * @param userName
	 *            拟稿人名称
	 * @param isBackSpace
	 *            是否是退回任务
	 * @param startDate
	 *            任务接收开始日期:查询用
	 * @param endDate
	 *            任务接收截止日期：查询用
	 * @return 
	 *         Object[List<String[字段名称,字段别名,字段类型,显示字段]>,List<Map<字段名称,字段值>>,List<
	 *         EFormComponent>,表名称]
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getTodo(Page page, ProcessedParameter parameter) {
		try {
			String workflowName = parameter.getWorkflowName();
			String formId = parameter.getFormId();
			String workflowType = parameter.getWorkflowType();
			String userName = parameter.getUserName();
			Date startDate = parameter.getStartDate();
			Date endDate = parameter.getEndDate();
			String isBackSpace = parameter.getIsBackSpace();
			String type = parameter.getType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			String orgId = parameter.getOrgId();

			// 增加工作流中的显示字段

			String businessName = ServletActionContext.getRequest()
					.getParameter(WORKFLOW_TITLE);

			StringBuilder customQuery = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customSelectItems = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			Object[] sItems = { "taskId", "taskStartDate", "isBackspace",
					"assignType", "taskNodeId", "taskNodeName", "isReceived" };
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			if (customQuery.length() == 0) {
				customQuery.append("  pi.ISSUSPENDED_ = 0 ");
			} else {
				customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
			}
			User curuser = userService.getCurrentUser();

			// 根据处室Id 取得该处室全部(待签收文件)
			if (orgId != null) {
				List<User> userList = userService.getUsersByOrgID(orgId);
				if (!userList.isEmpty()) {
					User firstUser = userList.get(0);
					paramsMap.put("handlerId", firstUser.getUserId());// 当前用户办理任务
				}
			} else {
				paramsMap.put("handlerId", curuser.getUserId());// 当前用户办理任务
			}

			paramsMap.put("toAssignHandlerId", userService.getCurrentUser()
					.getUserId());
			String[] adpterWorkflowType = CustomQueryAdpter
					.adpterWorkflowType(new String[] { workflowType,
							excludeWorkflowType });
			workflowType = adpterWorkflowType[0];
			excludeWorkflowType = adpterWorkflowType[1];
			if (workflowType != null) {
				CustomQueryUtil.genWorkflowTypeStringForSql(workflowType,
						customQuery);
			}
			if (excludeWorkflowType != null) {
				CustomQueryUtil.genExcludeWorkflowTypeStringForSql(
						excludeWorkflowType, customQuery);
			}
			if (workflowName != null && !"".equals(workflowName)) {
				if (customQuery.length() == 0) {
					customQuery
							.append(" pi.NAME_ like '" + workflowName + "' ");
				} else {
					customQuery.append("and pi.NAME_ like '" + workflowName
							+ "' ");
				}
			}
			if (businessName != null && !"".equals(businessName)) {
				if (customQuery.length() == 0) {
					customQuery.append(" pi.BUSINESS_NAME_ like '%"
							+ businessName + "%' ");
				} else {
					customQuery.append("and pi.BUSINESS_NAME_ like '%"
							+ businessName + "%' ");
				}
			}
			if (null != userName && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (startDate != null) {
				paramsMap.put("taskStartDateStart", startDate);
			}
			if (endDate != null) {
				paramsMap.put("taskStartDateEnd", endDate);
			}

			if ("0".equals(isBackSpace)) {// 退回
				paramsMap.put("isBackspace", "1");
			} else if ("1".equals(isBackSpace)) {// 委托
				paramsMap.put("assignType", "0");
			} else if ("2".equals(isBackSpace)) {// 指派
				paramsMap.put("assignType", "1");
			} else if ("3".equals(isBackSpace)) {// 一般流转
				paramsMap.put("assignType", "3");
				paramsMap.put("isBackspace", "0");
			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			// 收文编号
			String RECV_NUM = ServletActionContext.getRequest().getParameter(
					"RECV_NUM");
			if (RECV_NUM != null && !"".equals(RECV_NUM)) {
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.RECV_NUM like '%"
							+ RECV_NUM + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.RECV_NUM like '%"
							+ RECV_NUM + "%' ");
				}
			}
			// 来文字号
			String DOC_NUMBER = ServletActionContext.getRequest().getParameter(
					"DOC_NUMBER");
			if (DOC_NUMBER != null && !"".equals(DOC_NUMBER)) {
				if (customQuery.length() == 0) {
					customQuery.append(" jbpmbusiness.DOC_NUMBER like '%"
							+ DOC_NUMBER + "%' ");
				} else {
					customQuery.append("and jbpmbusiness.DOC_NUMBER like '%"
							+ DOC_NUMBER + "%' ");
				}
			}
			// 来文单位
			String ISSUE_DEPART_SIGNED = ServletActionContext.getRequest()
					.getParameter("ISSUE_DEPART_SIGNED");
			if (ISSUE_DEPART_SIGNED != null && !"".equals(ISSUE_DEPART_SIGNED)) {
				if (customQuery.length() == 0) {
					customQuery
							.append(" jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
									+ ISSUE_DEPART_SIGNED + "%' ");
				} else {
					customQuery
							.append("and jbpmbusiness.ISSUE_DEPART_SIGNED  like '%"
									+ ISSUE_DEPART_SIGNED + "%' ");
				}
			}

			// 是否签收
			String isReceived = ServletActionContext.getRequest().getParameter(
					"isReceived");
			if (isReceived != null && !"".equals(isReceived)
					&& !"null".equals(isReceived)) {
				if ("1".equals(isReceived)) {
					customQuery.append(" and ti.ISRECEIVE_  =  1");
				} else {
					customQuery.append(" and ti.ISRECEIVE_  is null ");
				}
			}

			if (customQuery.length() == 0) {
				customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (")
						.append(Tjbpmbusiness.getShowableBusinessType())
						.append(") ");
			} else {
				customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
						.append(Tjbpmbusiness.getShowableBusinessType())
						.append(") ");
			}

			initTodoSign(type, customSelectItems, customFromItems, customQuery);

			type = null;
			page.setAutoCount(true);
			page = workflow.getTaskInfosByConditionForPage(page, toSelectItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);

			destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);

			List<TaskBusinessBean> listWorkflow = null;

			List<String> oatablepks = null;
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page.getResult() != null && !page.getResult().isEmpty()) {
				listWorkflow = new LinkedList<TaskBusinessBean>();
				StringBuilder params = new StringBuilder();
				for (Object object : page.getResult()) {
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
					taskbusinessbean.setTaskStartDate(objs[1]==null?null:sf.parse(StringUtil.castString(objs[1])));
					taskbusinessbean.setIsBackspace(StringUtil
							.castString(objs[2]));
					taskbusinessbean.setAssignType(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setNodeId(StringUtil.castString(objs[4]));
					taskbusinessbean
							.setNodeName(StringUtil.castString(objs[5]));
					taskbusinessbean.setIsReceived(StringUtil
							.castString(objs[6]));
					listWorkflow.add(taskbusinessbean);
					params.append(StringUtil.castString(objs[0])).append(",");
				}
				Map<String, Map> processInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder();
					sql.append(" SELECT ")
							.append(" TI.TASKID           AS TASKID, ")
							.append(" TI.PROCESSID        AS PROCESSINSTANCEID, ")
							.append(" PI.START_USER_NAME_ AS STARTUSERNAME, ")
							.append(" PI.BUSINESS_ID      AS PROCESSMAINFORMBUSINESSID, ")
							.append(" PI.NAME_            AS PROCESSNAME, ")
							.append(" PI.BUSINESS_NAME_   AS BUSINESSNAME, ")
							.append(" BI.END_TIME         AS END_TIME, ")
							.append(" BI.BUSINESS_TYPE    AS BUSINESS_TYPE, ")
							.append(" PI.START_   		  AS STARTDATE, ")
							.append(" PI.START_USER_ID_   AS START_USER_ID ")
							.append(" FROM ")
							.append(" JBPM_PROCESSINSTANCE PI, ")
							.append(" T_JBPM_BUSINESS BI, ").append(" (")
							.append("SELECT ")
							.append("T.ID_       AS TASKID, ")
							.append("T.PROCINST_ AS PROCESSID ")
							.append("FROM ").append("JBPM_TASKINSTANCE T ")
							.append("WHERE ").append("T.ID_ IN (")
							.append(params.toString()).append(")")
							.append(")TI ").append(" WHERE ")
							.append(" PI.ID_ = TI.PROCESSID ")
							.append(" AND PI.BUSINESS_ID = BI.BUSINESS_ID");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						processInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							processInfoMap.put(
									String.valueOf(rsMap.get("TASKID")), rsMap);
						}
					}
				}

				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					Map map = processInfoMap.get(taskbusinessbean.getTaskId());
					if (map == null) {
						continue;
					}
					if (oatablepks == null) {
						oatablepks = new LinkedList<String>();
					}
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					taskbusinessbean.setInstanceId(StringUtil.castString(map
							.get("PROCESSINSTANCEID")));
					taskbusinessbean.setStartUserName(StringUtil.castString(map
							.get("STARTUSERNAME")));
					taskbusinessbean.setBsinessId(StringUtil.castString(map
							.get("PROCESSMAINFORMBUSINESSID")));
					if (taskbusinessbean.getBsinessId() != null) {
						oatablepks.add(taskbusinessbean.getBsinessId());
					}
					taskbusinessbean.setWorkflowName(StringUtil.castString(map
							.get("PROCESSNAME")));
					taskbusinessbean.setBusinessName(StringUtil.castString(map
							.get("BUSINESSNAME")));
					taskbusinessbean.setEndTime(StringUtil.castString(map
							.get("END_TIME")));
					taskbusinessbean.setBusinessType(StringUtil.castString(map
							.get("BUSINESS_TYPE")));
//					taskbusinessbean.setWorkflowStartDate((Date) map
//							.get("STARTDATE"));
					taskbusinessbean.setWorkflowStartDate(sdf.parse(StringUtil.castString(map
												.get("STARTDATE")==null?"":map
														.get("STARTDATE"))));
					
					// 处理子流程获取不到发起人的情况 Bug序号： 0000004533 yanjian 2012-07-02
					// 13:06
					taskbusinessbean
							.setStartUserId(map.get("START_USER_ID") == null ? curuser
									.getUserId() : StringUtil.castString(map
									.get("START_USER_ID")));
				}

				listWorkflow = new ArrayList<TaskBusinessBean>(listWorkflow);
			}
			String[] oatablepkis = null;
			if (oatablepks != null && !oatablepks.isEmpty()) {
				if (oatablepkis == null) {
					oatablepkis = new String[oatablepks.size()];
				}
				oatablepks.toArray(oatablepkis);
			}
			Page newpage = new Page(page.getPageSize(), page.isAutoCount());
			Object[] obj = getInfoItemPage(newpage, workflowName, formId,
					false, oatablepkis);
			Map tempMap = (Map) obj[4];// 存放业务数据【业务数据ID：业务数据】
			List showColumnList = (List) obj[0];
			// 添加图片列
			GridViewColumUtil.addPngColum(showColumnList);
			// showColumnList.add(new String[] {
			// GridViewColumUtil.getPNGColumName(), "",
			// GridViewColumUtil.getPNGColumType(),
			// GridViewColumUtil.getPNGColumName()});// 显示红黄警示牌
			List<EFormComponent> queryColumnList = (List<EFormComponent>) obj[2];
			if (obj[3] == null) {// 不存在表名,流程未挂接表单
				showColumnList.add(new String[] { WORKFLOW_TITLE, "流程标题", "0",
						WORKFLOW_TITLE });// 显示字段列表

				EFormComponent startUserName = new EFormComponent();
				startUserName.setType("Strong.Form.Controls.Edit");
				startUserName.setFieldName(WORKFLOW_TITLE);
				startUserName.setValue(businessName);
				startUserName.setLable("流程标题");
				queryColumnList.add(startUserName);
			}
			if (parameter.getHandleKind() != null) {
				showColumnList.add(new String[] { "processstartdate", "发起时间",
						"5", "processstartdate" });//
			}
			showColumnList.add(new String[] { "currentusername", "上步办理人", "0",
					"currentusername" });//
			showColumnList.add(new String[] { "currentuserdept", "所在部门", "0",
					"currentuserdept" });//

			List<Map> showList = new ArrayList<Map>();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				String[] firstItem = (String[]) showColumnList.get(0);// 得到第一项
				String pkFieldName = firstItem[0];
				List<Long> taskIdListTemp = new LinkedList<Long>();
				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					taskIdListTemp.add(new Long(taskbusinessbean.getTaskId()));
				}
				Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = sendDocUploadManager
						.getTaskIdMapPreTaskBeanTemper(taskIdListTemp);

				for (TaskBusinessBean taskbusinessbean : listWorkflow) {
					Map parmMap = new HashMap<String, String>();
					TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp
							.get(taskbusinessbean.getTaskId());
					if ("意见征询".equals(taskBeanTemp.getNodeName())) {// 处理意见征询的文
						ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
								.getBsinessId().split(";")[2]);
						parmMap.put("currentusername", "已征求完意见");
						parmMap.put("currentuserdept",
								toayjzx == null ? ""
										: toayjzx.getUnit() == null ? ""
												: toayjzx.getUnit());
					} else {
						String PreTaskActor = taskBeanTemp.getTaskActorName();
						String PreTaskActorOrgName = taskBeanTemp.getOrgName();
						parmMap.put("currentusername", PreTaskActor);
						parmMap.put("currentuserdept", PreTaskActorOrgName);
					}
					parmMap.put("processstartdate",
							taskbusinessbean.getWorkflowStartDate());
					parmMap.put("timeOut", taskbusinessbean.getEndTime());
					// 因为接口查询，流程名称是模糊查询,这里在这里加一道控制 added by dengzc
					// 2010年12月10日9:11:23
					if (workflowName != null
							&& !workflowName.equals(taskbusinessbean
									.getWorkflowName())) {
						if (formId != null
								&& (formId.startsWith("t") || formId
										.startsWith("T"))) {

						} else {
							continue;
						}
					}
					// ---------------------End--------------------------
					Map map = new HashMap();
					if (obj[3] != null) {// 存在表名
						if (tempMap
								.containsKey(taskbusinessbean.getBsinessId())) {
							map = new HashMap(
									(Map) tempMap.get(taskbusinessbean
											.getBsinessId()));
						}
					}
					sendDocBaseManager.initTodoShowMap(map, pkFieldName,
							taskbusinessbean, parmMap);
					showList.add(map);
					if (map.containsKey("SENDDOC_ISSUE_DEPART_SIGNED")) {
						map.put("SENDDOC_ISSUE_DEPART_SIGNED",
								userService.getUserDepartmentByUserId(
										taskbusinessbean.getStartUserId())
										.getOrgName());
					}
				}
				page.setResult(showList);
				if (page.getResult().isEmpty()) {
					page.setTotalCount(0);
				}
			}
			return new Object[] { showColumnList, showList, queryColumnList,
					obj[3] };
		} catch (WorkflowException e) {
			logger.error("工作流查询异常", e);
			throw new SystemException(e);
		} catch (Exception ex) {
			logger.error("SystemException", ex);
			return null;
		}

	}

	/**
	 * 注入sessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		sendDocDao = new GenericDAOHibernate<ToaSenddoc, String>(
				sessionFactory, ToaSenddoc.class);
		
		articleDao = new GenericDAOHibernate<ToaInfopublishArticle, String>(
				sessionFactory, ToaInfopublishArticle.class);
		serviceDAO = new WorkflowDao(sessionFactory, Object.class);

	}

	/**
	 * 保存公文
	 * 
	 * @param docBean
	 */
	public void saveDoc(ToaSenddoc docBean, OALogInfo... infos) {
		sendDocDao.save(docBean);
	}

	/**
	 * 删除记录 - 删除公文时保证先删除公文对应的附件记录.防止出现垃圾数据.
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 下午07:35:09
	 * @param ids
	 *            主键id,多个记录以逗号隔开
	 */
	public void deleteMultiDocs(String[] ids, OALogInfo... infos) {
		if (!ObjectUtils.isEmpty(ids)) {
			super.deleteAttach(ids);
			sendDocDao.delete(ids);
		}
	}

	/**
	 * 删除公文草稿，同时删除附件记录
	 * 
	 * @author:邓志城
	 * @date:2010-7-22 下午02:12:52
	 * @param bo
	 * @param infos
	 */
	public void delete(String id, String tableName, OALogInfo... infos) {
		if (id == null || "".equals(id)) {
			logger.error("参数id为空。");
			return;
		}
		String[] ids = id.split(",");
		super.deleteAttach(ids);
		String pkFieldName = super.getPrimaryKeyName(tableName);
		List<String> sql = new ArrayList<String>(ids.length);
		for (String pk : ids) {
			StringBuilder SqlDelete = new StringBuilder("DELETE FROM ");
			SqlDelete.append(tableName).append(" WHERE ").append(pkFieldName);
			SqlDelete.append(" ='").append(pk).append("'");
			logger.info("Delete SQL:" + SqlDelete.toString());
			sql.add(SqlDelete.toString());
		}
		jdbcTemplate.batchUpdate(sql.toArray(new String[sql.size()]));
	}

	/**
	 * 提交工作流转到下一步
	 * 
	 * @param taskId
	 *            任务ID
	 * @param transitionName
	 *            流向名称
	 * @param returnNodeId
	 *            驳回节点ID
	 * @param isNewForm
	 *            当前节点是否是新的表单
	 * @param formId
	 *            表单ID
	 * @param businessId
	 *            业务ID
	 * @param suggestion
	 *            提交意见
	 * @param taskActors
	 *            下一步任务处理人([人员ID|节点ID，人员ID|节点ID……])
	 */
	public VoFormDataBean handleWorkflowNextStep(String taskId,
			String transitionName, String returnNodeId, String isNewForm,
			String formId, String businessId, String suggestion,
			String[] taskActors, File formData, String workflowName,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {
			VoFormDataBean bean = null;
			if (formData != null) {
				bean = this.saveFormData(formData, workflowName, "1", null);// super.saveForm(formData);
				businessId = bean.getBusinessId();
			}
			String curUserId = userService.getCurrentUser().getUserId();
			super.handleWorkflowNextStep(taskId, transitionName, returnNodeId,
					isNewForm, formId, businessId, suggestion, curUserId,
					taskActors);
			return bean;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "流程跳转" });
		}
	}

	/**
	 * 根据公文ID获取公文
	 * 
	 * @param docId
	 * @return com.strongit.oa.bo.ToaSenddoc
	 */
	@Transactional(readOnly = true)
	public ToaSenddoc getDocById(String docId) {
		return sendDocDao.findUniqueByProperty("senddocId", docId);
	}

	/**
	 * 根据公文ID生成XML包装字符
	 * 
	 * @author:邓志城
	 * @date:2009-7-14 下午03:37:01
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public String getPacketBySendocId(String docId) throws Exception {
		ToaSenddoc sendDoc = getDocById(docId);
		// 指标文号
		String docName = sendDoc.getSenddocCode();// "指标文号1";
		// 文号性质
		String docType = "2";
		// 签发人
		String docSignatory = sendDoc.getSenddocIssuer();
		// 正文内容【采用BASE64编码】
		BASE64Encoder baseEncoder = new BASE64Encoder();
		byte[] bcontent = sendDoc.getSenddocContent();
		String docContent = baseEncoder.encode(bcontent);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 签发时间
		Date dDocSigntime = sendDoc.getSenddocOfficialTime();
		String docSigntime = "";
		if (dDocSigntime != null) {
			docSigntime = sdf.format(dDocSigntime);
		}
		// 备注
		String docRemark = sendDoc.getSenddocRemark();
		// 业务处室编号
		String branchId = sendDoc.getSenddocBussinessCode();
		List<Map<String, Object>> attachLst = getAttachBySenddocId(docId);
		// 附件名
		String attaName = null;
		// 附件内容
		String attaContent = null;
		if (attachLst.size() > 0) {// 如果有附件只取第一个附件
			Map<String, Object> attachMap = attachLst.get(0);
			attaName = (String) attachMap.get("ATTACH_NAME");
			byte[] battachMent = (byte[]) attachMap.get("ATTACH_CONTENT");
			attaContent = baseEncoder.encode(battachMent);
		}
		String createIp = ServletActionContext.getRequest().getLocalAddr();
		String xml = PacketUtil.getPacket(docName, docType, docContent,
				docSignatory, docSigntime, docRemark, branchId, attaName,
				attaContent, createIp);
		return xml;
	}

	/**
	 * 文档流直接写入HttpServletResponse请求
	 * 
	 * @param response
	 * @param docId
	 */
	public void setContentToHttpResponse(HttpServletResponse response,
			String docId) {
		ToaSenddoc doc = getDocById(docId);

		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + doc.getSenddocTitle());
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			output.write(doc.getSenddocContent());
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新拟稿单位负责人信息到REST4字段中.
	 * 
	 * @author:邓志城
	 * @date:2010-9-16 下午06:08:40
	 * @param id
	 *            公文主键
	 * @param name
	 *            拟稿单位负责人姓名
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateDeparentmentManager(String id, String name)
			throws SystemException, ServiceException {
		if (id != null) {
			ToaSenddoc senddoc = sendDocDao.get(id);
			senddoc.setRest4(name);
			sendDocDao.update(senddoc);
		}
	}

	/**
	 * 更新审核人信息到REST8字段中.
	 * 
	 * @author:刘皙
	 * @date:2012年5月14日16:46:56
	 * @param id
	 *            公文主键
	 * @param name
	 *            审核人姓名
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateShenHeToRest8(String id, String name)
			throws SystemException, ServiceException {
		if (id != null) {
			ToaSenddoc senddoc = sendDocDao.get(id);
			if (senddoc.getRest8() == null) {
				senddoc.setRest8(name);
			} else {
				senddoc.setRest8(senddoc.getRest8() + "," + name);
			}
			sendDocDao.update(senddoc);
		}
	}

	/**
	 * 更新审批人信息到REST7字段中.
	 * 
	 * @author:刘皙
	 * @date:2012年5月14日16:46:56
	 * @param id
	 *            公文主键
	 * @param name
	 *            审批人姓名
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateShenPiToRest7(String id, String name)
			throws SystemException, ServiceException {
		if (id != null) {
			ToaSenddoc senddoc = sendDocDao.get(id);
			if (senddoc.getRest8() == null) {
				senddoc.setRest8(name);
			} else {
				senddoc.setRest8(senddoc.getRest8() + "," + name);
			}
			sendDocDao.update(senddoc);
		}
	}

	/**
	 * 公文归档
	 * 
	 * @author:邓志城
	 * @date:2009-6-10 下午03:30:06
	 * @param fileType
	 *            文件类型
	 * @param fileId
	 *            文件ID
	 * @return
	 * @throws SystemException
	 */
	@Deprecated
	public boolean addTempFileInterface(Object... objects)
			throws SystemException {
		return workflow.addTemplateFileInterface(objects);
	}

	/**
	 * 生成新的业务数据
	 * 
	 * @param businessId
	 *            表名;主键名;主键值
	 * @return
	 */
	public String copyData(String businessId) {
		UUIDGenerator generator = new UUIDGenerator();
		String pk = generator.generate().toString(); // 生成主键值
		String[] args = businessId.split(";");
		String tableName = args[0];
		String pkFieldName = args[1];
		String pkFieldValue = args[2];
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(tableName).append(" where ").append(pkFieldName);
		sql.append(" = '").append(pkFieldValue).append("'");
		ResultSet rs = super.executeJdbcQuery(sql.toString());
		PreparedStatement psmt = null;
		Connection con = getConnection();
		ResultSet rss = null;
		try {
			if (rs.next()) {
				StringBuilder fields = new StringBuilder(pkFieldName)
						.append(",");
				StringBuilder quto = new StringBuilder("?,");
				StringBuilder newfields = new StringBuilder("");
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				Map<Integer, Object> map = new HashMap<Integer, Object>();
				for (int i = 1; i <= count; i++) {
					String columnName = rsmd.getColumnName(i);
					if (!pkFieldName.equals(columnName)) {// 非主键字段
						newfields.append(columnName).append(",");
						fields.append(columnName).append(",");
						quto.append("?").append(",");
						/* 数据备份 设置WORKFLOWSTATE为 “-1” yanjian 2012-10-16 14:12 */
						if ("WORKFLOWSTATE".equals(columnName)) {
							map.put(i, "-1");
						} else {
							map.put(i, rs.getObject(i)==null?"":rs.getObject(i));
						}
					}
				}
				newfields.deleteCharAt(newfields.length() - 1);// 得到表中所有字段,并且主键排在第一位
				fields.deleteCharAt(fields.length() - 1);// 得到表中所有字段,并且主键排在第一位
				quto.deleteCharAt(quto.length() - 1);
				StringBuilder newInsertSql = new StringBuilder("insert into ").append(tableName);
				newInsertSql.append(" (").append(fields).append(") ");
				newInsertSql.append("  select  '").append(pk).append("' ,").append(newfields).append(" from ").append(tableName);
				newInsertSql.append(" where ").append(pkFieldName);
				newInsertSql.append(" = '").append(pkFieldValue).append("'");
//				StringBuilder insertSql = new StringBuilder("insert into ")
//						.append(tableName);
//				insertSql.append(" (").append(fields).append(") values (")
//						.append(quto.toString()).append(")");
//				psmt = con.prepareStatement(insertSql.toString());
//				psmt.setString(1, pk);
//				for (int i = 2; i <= count; i++) {
//					psmt.setObject(i, map.get(i));
//				}
				psmt = con.prepareStatement(newInsertSql.toString());

				psmt.executeUpdate();
				// ----------- 生成新的业务数据完成 ----------------------
				// 拷贝附件数据
				/*
				 * StringBuilder queryAttach = new StringBuilder("select
				 * ATTACH_NAME,ATTACH_CONTENT from T_DOCATTACH t where
				 * t.DOC_ID='"); queryAttach.append(pkFieldValue).append("'");
				 * rss = super.executeJdbcQuery(queryAttach.toString());
				 * while(rss.next()) { StringBuilder insertAttachSql = new
				 * StringBuilder("insert into T_DOCATTACH
				 * (DOCATTACHID,ATTACH_CONTENT,ATTACH_NAME,DOC_ID) values");
				 * insertAttachSql.append("(?,?,?,?)"); psmt =
				 * con.prepareStatement(insertAttachSql.toString());
				 * UUIDGenerator generate = new UUIDGenerator(); String id =
				 * generate.generate().toString(); //生成主键值 psmt.setString(1,
				 * id); psmt.setObject(2, rss.getObject(2)); psmt.setString(3,
				 * rss.getString(1)); psmt.setString(4, pk);//外键 -- 公文ID
				 * psmt.executeUpdate(); }
				 */
				List<WorkflowAttach> workflowAttachs = workflowAttachManager
						.getWorkflowAttachsByDocId(pkFieldValue);
				if (workflowAttachs != null && !workflowAttachs.isEmpty()) {
					for (WorkflowAttach attach : workflowAttachs) {
						WorkflowAttach model = new WorkflowAttach();
						model.setAttachContent(attach.getAttachContent());
						model.setAttachName(attach.getAttachName());
						model.setDocId(pk);
						workflowAttachManager.saveWorkflowAttach(model);
					}
				}
				// *****************附件数据拷贝完毕******************************

			}
			return tableName + ";" + pkFieldName + ";" + pk;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeResultSet(rss);
			JdbcUtils.closeStatement(psmt);
			JdbcUtils.closeConnection(con);
		}
	}

	public void doArchiveWithBusinessData(String businessId, String formId,
			String instanceId) {
		if (businessId != null && !"".equals(businessId)) {
			UUIDGenerator generator = new UUIDGenerator();
			String pk = generator.generate().toString(); // 生成主键值
			String[] args = businessId.split(";");
			String tableName = args[0];
			String pkFieldName = args[1];
			String pkFieldValue = args[2];
			StringBuilder sql = new StringBuilder("select * from ");
			sql.append(tableName).append(" where ").append(pkFieldName);
			sql.append(" = '").append(pkFieldValue).append("'");
			ResultSet rs = super.executeJdbcQuery(sql.toString());
			PreparedStatement psmt = null;
			Connection con = getConnection();
			ResultSet rss = null;
			try {
				if (rs.next()) {
					StringBuilder fields = new StringBuilder(pkFieldName)
							.append(",");
					StringBuilder newfields = new StringBuilder("");
					StringBuilder quto = new StringBuilder("?,");
					ResultSetMetaData rsmd = rs.getMetaData();
					int count = rsmd.getColumnCount();
					Map<Integer, Object> map = new HashMap<Integer, Object>();
					for (int i = 1; i <= count; i++) {
						String columnName = rsmd.getColumnName(i);
						if (!pkFieldName.equals(columnName)) {// 非主键字段
							newfields.append(columnName).append(",");
							fields.append(columnName).append(",");
							quto.append("?").append(",");
							if (columnName.equals(WORKFLOW_STATE)) {// 归档之后将业务表中的WORKFLOWSTATE改为'2'，标识归档
								map.put(i, "2");
							} else {
								map.put(i, rs.getObject(i)==null?"":rs.getObject(i));
							}
						}
					}				
					newfields.deleteCharAt(newfields.length() - 1);// 得到表中所有字段,并且主键排在第一位
					if(newfields.lastIndexOf(",ROWID")!=-1){
						newfields.delete(newfields.length()-6, newfields.length());
					}
					fields.deleteCharAt(fields.length() - 1);// 得到表中所有字段,并且主键排在第一位
					if(fields.lastIndexOf(",ROWID")!=-1){
						fields.delete(fields.length()-6, fields.length());
					}
					quto.deleteCharAt(quto.length() - 1);
					StringBuilder newInsertSql = new StringBuilder("insert into ").append(tableName);
					newInsertSql.append(" (").append(fields).append(") ");
					newInsertSql.append("  select  '").append(pk).append("' ,").append(newfields).append(" from ").append(tableName);
					newInsertSql.append(" where ").append(pkFieldName);
					newInsertSql.append(" = '").append(pkFieldValue).append("'");

//					StringBuilder insertSql = new StringBuilder("insert into ")
//							.append(tableName);
//					insertSql.append(" (").append(fields).append(") values (")
//							.append(quto.toString()).append(")");
//					psmt = con.prepareStatement(insertSql.toString());
//					psmt.setString(1, pk);
//					for (int i = 2; i <= count; i++) {
//						psmt.setObject(i, map.get(i));
//					}
					psmt = con.prepareStatement(newInsertSql.toString());
					psmt.executeUpdate();
					// ----------- 生成新的业务数据完成 ----------------------
					// 拷贝附件数据
					/*
					 * StringBuilder queryAttach = new StringBuilder("select
					 * ATTACH_NAME,ATTACH_CONTENT from T_DOCATTACH t where
					 * t.DOC_ID='");
					 * queryAttach.append(pkFieldValue).append("'"); rss =
					 * super.executeJdbcQuery(queryAttach.toString());
					 * while(rss.next()) { StringBuilder insertAttachSql = new
					 * StringBuilder("insert into T_DOCATTACH
					 * (DOCATTACHID,ATTACH_CONTENT,ATTACH_NAME,DOC_ID) values");
					 * insertAttachSql.append("(?,?,?,?)"); psmt =
					 * con.prepareStatement(insertAttachSql.toString());
					 * UUIDGenerator generate = new UUIDGenerator(); String id =
					 * generate.generate().toString(); //生成主键值 psmt.setString(1,
					 * id); psmt.setObject(2, rss.getObject(2));
					 * psmt.setString(3, rss.getString(1)); psmt.setString(4,
					 * pk);//外键 -- 公文ID psmt.executeUpdate(); }
					 */

					List<GWYJAttach> gwyjAttachs = attachManager
							.getGwyjAttachsByDocId(pkFieldValue);
					if (gwyjAttachs != null && !gwyjAttachs.isEmpty()) {
						for (GWYJAttach attach : gwyjAttachs) {
							GWYJAttach model = new GWYJAttach();
							model.setAttachContent(attach.getAttachContent());
							model.setAccachName(attach.getAccachName());
							model.setDocid(pk);
							attachManager.saveGwyjAttach(model);
						}
					}

					List<WorkflowAttach> workflowAttachs = workflowAttachManager
							.getWorkflowAttachsByDocId(pkFieldValue);
					if (workflowAttachs != null && !workflowAttachs.isEmpty()) {
						for (WorkflowAttach attach : workflowAttachs) {
							WorkflowAttach model = new WorkflowAttach();
							model.setAttachContent(attach.getAttachContent());
							model.setAttachName(attach.getAttachName());
							model.setDocId(pk);
							workflowAttachManager.saveWorkflowAttach(model);
						}
					}
					/**
					 * 存取流程类型，来判断归档是否要显示表单信息
					 * */
					ProcessInstance pi = workflow
							.getProcessInstanceById(instanceId);
					String ptId = pi.getTypeId().toString();// 用来获取流程类型ID
					pi = null;
					// *****************附件数据拷贝完毕******************************
					ToaArchiveTempfile tempFile = new ToaArchiveTempfile();
					// tempFile.setWorkflow(instanceId);//流程实例id
					tempFile.setTempfileFormId(formId);// 表单模板id
					// tempFile.setTempfileDocType(TempFileType.SENDDOC);
					tempFile.setTempfileDocType(ptId);// 以后走的数据都是通过这种方式
					String userId = rs.getString(WORKFLOW_AUTHOR);// 拟稿人
					tempFile.setTempfileDesc((String) rs
							.getObject(WORKFLOW_TITLE));
					// 发文字号
					if("T_OARECVDOC".equals(tableName)){
					    tempFile.setTempfileNo((String) rs.getObject("RECV_NUM"));
					}else if("T_OA_SENDDOC".equals(tableName)){
						tempFile.setTempfileNo((String) rs.getObject("SENDDOC_CODE"));
					}else{
						tempFile.setTempfileNo((String) rs.getObject(WORKFLOW_CODE));
					}
					tempFile.setTempfileTitle((String) rs
							.getObject(WORKFLOW_TITLE));// 标题
					tempFile.setTempfileDate(new Date());
					/*
					 * if(userId == null || "".equals(userId)) {
					 * //拟稿人为空,一般为子流程。这里通过流程实例得到流程发起人 List<String> toSelectItems
					 * = new ArrayList<String>(1);//定义需要查询的数据
					 * toSelectItems.add("startUserId"); Map<String, Object>
					 * paramsMap = new HashMap<String, Object>();//定义查询条件
					 * paramsMap.put("processInstanceId", instanceId);
					 * List<Object[]> result =
					 * workflow.getProcessInstanceByConditionForList
					 * (toSelectItems, paramsMap, null); if(result != null &&
					 * !result.isEmpty()) { Object userIdObj = result.get(0);
					 * userId = userIdObj.toString(); } }
					 */

					// 拟稿人为空,一般为子流程。这里通过流程实例得到流程发起人
					List<String> toSelectItems = new ArrayList<String>(1);// 定义需要查询的数据
					toSelectItems.add("startUserId");
					toSelectItems.add("processTypeId");
					toSelectItems.add("processTypeName");
					Map<String, Object> paramsMap = new HashMap<String, Object>();// 定义查询条件
					paramsMap.put("processInstanceId", instanceId);
					List<Object[]> result = workflow
							.getProcessInstanceByConditionForList(
									toSelectItems, paramsMap, null);
					Object type = null;
					Object typeName = null;
					if (result != null && !result.isEmpty()) {
						Object[] userIdObj = result.get(0);
						if (userId == null || "".equals(userId)) {
							userId = userIdObj[0].toString();
						}
						type = userIdObj[1];
						typeName = userIdObj[2];
					}

					/*
					 * if (userId != null && !"".equals(userId)) { User user =
					 * userService.getUserInfoByUserId(userId .toString());
					 * tempFile.setTempfileAuthor(user.getUserName());
					 * tempFile.setTempfileDepartment(user.getOrgId()); }
					 */
					User user = userService.getCurrentUser();
					tempFile.setTempfileAuthor(user.getUserName());
					tempFile.setTempfileDepartment(user.getOrgId());
					tempFile.setTempfileDocId(tableName + ";" + pkFieldName
							+ ";" + pk);
					tempFile.setWorkflow(instanceId + ";" + type + ";"
							+ typeName);// 流程实例id;流程类型id;流程类型名称
					// *********************** 得到意见数据 -----------------
					ToaArchiveTfileAppend tappend = null;
					Set<ToaArchiveTfileAppend> tempFileAppend = new HashSet<ToaArchiveTfileAppend>();
					try {
					String information = getBusiFlagByProcessInstanceId(instanceId);
					tappend = new ToaArchiveTfileAppend();
						tappend.setTempappendContent(information
								.getBytes("utf-8"));
						tappend.setTempappendName(tempFile.getTempfileTitle());
					} catch (UnsupportedEncodingException e) {
						LogPrintStackUtil.error("字符串转字节数组异常！");
						throw new SystemException(e);
					}
					tempFileAppend.add(tappend);
					
					ToaArchiveTfileAppend annex=new ToaArchiveTfileAppend();
					if(newfields.indexOf("PDFIMAGE")!=-1){
						byte[] pdfcontentnew  = (byte[]) rs.getBytes("PDFIMAGE");
						if(pdfcontentnew!=null&&!"".equals(pdfcontentnew)){
							//增加归档保存电子表单快照为档案附件 by qibh 2014-01-22
							byte[] pdfcontent=formPdfService.buildPdf(tableName, "PDFIMAGE", pkFieldName, pkFieldValue);
							if(pdfcontent!=null && !"".equals(pdfcontent)){
								annex.setTempappendContent(formPdfService.buildPdf(tableName, "PDFIMAGE", pkFieldName, pkFieldValue));
								annex.setTempappendName(tempFile.getTempfileTitle()+"表单PDF快照");
								annex.setTempappendType("2");
								tempFileAppend.add(annex);
							}
						}
					}
					
					tempFile.setToaArchiveTfileAppends(tempFileAppend);
					tempFileManager.saveTempfile(tempFile);
					if(newfields.indexOf("PDFIMAGE")!=-1){
						annex.setToaArchiveTempfile(tempFile);
						annexManager.saveAppend(annex);
					}
					logger.error("归档成功,业务数据为:" + tempFile.getTempfileDocId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			    throw new SystemException(e);
			} finally {
				JdbcUtils.closeResultSet(rs);
				JdbcUtils.closeResultSet(rss);
				JdbcUtils.closeStatement(psmt);
				JdbcUtils.closeConnection(con);
			}
		}
	}

	/**
	 * 工作流归档接口
	 * 
	 * @author:邓志城
	 * @date:2010-11-23 下午04:11:34
	 * @param businessId
	 *            业务数据主键标示 tableName;pkFieldName;pkFieldValue
	 * @param formId
	 *            表单模板id
	 * @param instanceId
	 *            流程实例
	 */
	public void doArchive(String businessId, String formId, String instanceId) {/*
																				 * ToaArchiveTempfile
																				 * tempFile
																				 * =
																				 * new
																				 * ToaArchiveTempfile
																				 * (
																				 * )
																				 * ;
																				 * tempFile
																				 * .
																				 * setWorkflow
																				 * (
																				 * instanceId
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 流程实例id
																				 * tempFile
																				 * .
																				 * setTempfileFormId
																				 * (
																				 * formId
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 表单模板id
																				 * tempFile
																				 * .
																				 * setTempfileDocType
																				 * (
																				 * TempFileType
																				 * .
																				 * SENDDOC
																				 * )
																				 * ;
																				 * if
																				 * (
																				 * businessId
																				 * !=
																				 * null
																				 * &&
																				 * businessId
																				 * .
																				 * indexOf
																				 * (
																				 * ";"
																				 * )
																				 * !=
																				 * -
																				 * 1
																				 * )
																				 * {
																				 * String
																				 * [
																				 * ]
																				 * args
																				 * =
																				 * businessId
																				 * .
																				 * split
																				 * (
																				 * ";"
																				 * )
																				 * ;
																				 * String
																				 * tableName
																				 * =
																				 * args
																				 * [
																				 * 0
																				 * ]
																				 * ;
																				 * String
																				 * pkFieldName
																				 * =
																				 * args
																				 * [
																				 * 1
																				 * ]
																				 * ;
																				 * String
																				 * pkFieldValue
																				 * =
																				 * args
																				 * [
																				 * 2
																				 * ]
																				 * ;
																				 * StringBuilder
																				 * sql
																				 * =
																				 * new
																				 * StringBuilder
																				 * (
																				 * "SELECT
																				 * ");
																				 * List
																				 * systemField
																				 * =
																				 * infoItemManager
																				 * .
																				 * getSystemField
																				 * (
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 得到默认的工作流字段
																				 * String
																				 * strSystemField
																				 * =
																				 * org
																				 * .
																				 * apache
																				 * .
																				 * commons
																				 * .
																				 * lang
																				 * .
																				 * StringUtils
																				 * .
																				 * join
																				 * (
																				 * systemField
																				 * ,
																				 * ','
																				 * )
																				 * ;
																				 * sql
																				 * .
																				 * append
																				 * (
																				 * strSystemField
																				 * )
																				 * ;
																				 * sql
																				 * .
																				 * append
																				 * (
																				 * "
																				 * FROM
																				 * ").append(tableName).append("
																				 * WHERE
																				 * ").append(pkFieldName);
																				 * sql
																				 * .
																				 * append
																				 * (
																				 * "
																				 * =
																				 * '"+pkFieldValue+"'");
																				 * ResultSet
																				 * rs
																				 * =
																				 * super
																				 * .
																				 * executeJdbcQuery
																				 * (
																				 * sql
																				 * .
																				 * toString
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * String
																				 * userId
																				 * =
																				 * null
																				 * ;
																				 * try
																				 * {
																				 * if
																				 * (
																				 * rs
																				 * .
																				 * next
																				 * (
																				 * )
																				 * )
																				 * {
																				 * userId
																				 * =
																				 * rs
																				 * .
																				 * getString
																				 * (
																				 * WORKFLOW_AUTHOR
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 拟稿人
																				 * tempFile
																				 * .
																				 * setTempfileDesc
																				 * (
																				 * (
																				 * String
																				 * )
																				 * rs
																				 * .
																				 * getObject
																				 * (
																				 * WORKFLOW_TITLE
																				 * )
																				 * )
																				 * ;
																				 * tempFile
																				 * .
																				 * setTempfileNo
																				 * (
																				 * (
																				 * String
																				 * )
																				 * rs
																				 * .
																				 * getObject
																				 * (
																				 * WORKFLOW_CODE
																				 * )
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 发文字号
																				 * tempFile
																				 * .
																				 * setTempfileTitle
																				 * (
																				 * (
																				 * String
																				 * )
																				 * rs
																				 * .
																				 * getObject
																				 * (
																				 * WORKFLOW_TITLE
																				 * )
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 标题
																				 * }
																				 * }
																				 * catch
																				 * (
																				 * SQLException
																				 * e
																				 * )
																				 * {
																				 * e
																				 * .
																				 * printStackTrace
																				 * (
																				 * )
																				 * ;
																				 * throw
																				 * new
																				 * SystemException
																				 * (
																				 * e
																				 * )
																				 * ;
																				 * }
																				 * finally
																				 * {
																				 * JdbcUtils
																				 * .
																				 * closeResultSet
																				 * (
																				 * rs
																				 * )
																				 * ;
																				 * }
																				 * /
																				 * /
																				 * Map
																				 * map
																				 * =
																				 * super
																				 * .
																				 * getSystemField
																				 * (
																				 * pkFieldName
																				 * ,
																				 * pkFieldValue
																				 * ,
																				 * tableName
																				 * )
																				 * ;
																				 * if
																				 * (
																				 * userId
																				 * !=
																				 * null
																				 * )
																				 * {
																				 * User
																				 * user
																				 * =
																				 * userService
																				 * .
																				 * getUserInfoByUserId
																				 * (
																				 * userId
																				 * )
																				 * ;
																				 * if
																				 * (
																				 * user
																				 * .
																				 * getUserId
																				 * (
																				 * )
																				 * !=
																				 * null
																				 * )
																				 * {
																				 * tempFile
																				 * .
																				 * setTempfileAuthor
																				 * (
																				 * user
																				 * .
																				 * getUserName
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * tempFile
																				 * .
																				 * setTempfileDate
																				 * (
																				 * new
																				 * Date
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * tempFile
																				 * .
																				 * setTempfileDepartment
																				 * (
																				 * user
																				 * .
																				 * getOrgId
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * }
																				 * else
																				 * {
																				 * logger
																				 * .
																				 * error
																				 * (
																				 * "用户"
																				 * +
																				 * userId
																				 * +
																				 * "已删除。"
																				 * )
																				 * ;
																				 * }
																				 * }
																				 * else
																				 * {
																				 * /
																				 * /
																				 * 拟稿人为空
																				 * ,
																				 * 一般为子流程
																				 * 。
																				 * 这里通过流程实例得到流程发起人
																				 * List
																				 * <
																				 * String
																				 * >
																				 * toSelectItems
																				 * =
																				 * new
																				 * ArrayList
																				 * <
																				 * String
																				 * >
																				 * (
																				 * 1
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 定义需要查询的数据
																				 * toSelectItems
																				 * .
																				 * add
																				 * (
																				 * "startUserId"
																				 * )
																				 * ;
																				 * Map
																				 * <
																				 * String
																				 * ,
																				 * Object
																				 * >
																				 * paramsMap
																				 * =
																				 * new
																				 * HashMap
																				 * <
																				 * String
																				 * ,
																				 * Object
																				 * >
																				 * (
																				 * )
																				 * ;
																				 * /
																				 * /
																				 * 定义查询条件
																				 * paramsMap
																				 * .
																				 * put
																				 * (
																				 * "processInstanceId"
																				 * ,
																				 * instanceId
																				 * )
																				 * ;
																				 * List
																				 * <
																				 * Object
																				 * [
																				 * ]
																				 * >
																				 * result
																				 * =
																				 * workflow
																				 * .
																				 * getProcessInstanceByConditionForList
																				 * (
																				 * toSelectItems
																				 * ,
																				 * paramsMap
																				 * ,
																				 * null
																				 * )
																				 * ;
																				 * if
																				 * (
																				 * result
																				 * !=
																				 * null
																				 * &&
																				 * !
																				 * result
																				 * .
																				 * isEmpty
																				 * (
																				 * )
																				 * )
																				 * {
																				 * Object
																				 * userIdObj
																				 * =
																				 * result
																				 * .
																				 * get
																				 * (
																				 * 0
																				 * )
																				 * ;
																				 * User
																				 * user
																				 * =
																				 * userService
																				 * .
																				 * getUserInfoByUserId
																				 * (
																				 * userIdObj
																				 * .
																				 * toString
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * if
																				 * (
																				 * user
																				 * .
																				 * getUserId
																				 * (
																				 * )
																				 * !=
																				 * null
																				 * )
																				 * {
																				 * tempFile
																				 * .
																				 * setTempfileAuthor
																				 * (
																				 * user
																				 * .
																				 * getUserName
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * tempFile
																				 * .
																				 * setTempfileDate
																				 * (
																				 * new
																				 * Date
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * tempFile
																				 * .
																				 * setTempfileDepartment
																				 * (
																				 * user
																				 * .
																				 * getOrgId
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * }
																				 * else
																				 * {
																				 * logger
																				 * .
																				 * error
																				 * (
																				 * "用户"
																				 * +
																				 * userId
																				 * +
																				 * "已删除。"
																				 * )
																				 * ;
																				 * }
																				 * }
																				 * tempFile
																				 * .
																				 * setTempfileDate
																				 * (
																				 * new
																				 * Date
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * }
																				 * tempFile
																				 * .
																				 * setTempfileDocId
																				 * (
																				 * businessId
																				 * )
																				 * ;
																				 * }
																				 * else
																				 * {
																				 * logger
																				 * .
																				 * error
																				 * (
																				 * "业务数据不存在或已删除！"
																				 * )
																				 * ;
																				 * }
																				 * Set
																				 * <
																				 * ToaArchiveTfileAppend
																				 * >
																				 * tempFileAppend
																				 * =
																				 * new
																				 * HashSet
																				 * <
																				 * ToaArchiveTfileAppend
																				 * >
																				 * (
																				 * )
																				 * ;
																				 * ToaArchiveTfileAppend
																				 * tappend
																				 * =
																				 * new
																				 * ToaArchiveTfileAppend
																				 * (
																				 * )
																				 * ;
																				 * ActionContext
																				 * cxt
																				 * =
																				 * ActionContext
																				 * .
																				 * getContext
																				 * (
																				 * )
																				 * ;
																				 * String
																				 * fullFormData
																				 * =
																				 * (
																				 * String
																				 * )
																				 * cxt
																				 * .
																				 * getSession
																				 * (
																				 * )
																				 * .
																				 * get
																				 * (
																				 * "formData"
																				 * )
																				 * ;
																				 * if
																				 * (
																				 * fullFormData
																				 * ==
																				 * null
																				 * ||
																				 * ""
																				 * .
																				 * equals
																				 * (
																				 * fullFormData
																				 * )
																				 * )
																				 * {
																				 * LogPrintStackUtil
																				 * .
																				 * error
																				 * (
																				 * "未获取到表单数据！"
																				 * )
																				 * ;
																				 * throw
																				 * new
																				 * SystemException
																				 * (
																				 * "未获取到表单数据！"
																				 * )
																				 * ;
																				 * }
																				 * try
																				 * {
																				 * tappend
																				 * .
																				 * setTempappendContent
																				 * (
																				 * fullFormData
																				 * .
																				 * getBytes
																				 * (
																				 * "utf-8"
																				 * )
																				 * )
																				 * ;
																				 * tappend
																				 * .
																				 * setTempappendName
																				 * (
																				 * tempFile
																				 * .
																				 * getTempfileTitle
																				 * (
																				 * )
																				 * )
																				 * ;
																				 * }
																				 * catch
																				 * (
																				 * UnsupportedEncodingException
																				 * e
																				 * )
																				 * {
																				 * LogPrintStackUtil
																				 * .
																				 * error
																				 * (
																				 * "字符串转字节数组异常！"
																				 * )
																				 * ;
																				 * throw
																				 * new
																				 * SystemException
																				 * (
																				 * e
																				 * )
																				 * ;
																				 * }
																				 * tempFileAppend
																				 * .
																				 * add
																				 * (
																				 * tappend
																				 * )
																				 * ;
																				 * tempFile
																				 * .
																				 * setToaArchiveTfileAppends
																				 * (
																				 * tempFileAppend
																				 * )
																				 * ;
																				 * tempFileManager
																				 * .
																				 * saveTempfile
																				 * (
																				 * tempFile
																				 * )
																				 * ;
																				 * logger
																				 * .
																				 * error
																				 * (
																				 * "归档成功"
																				 * )
																				 * ;
																				 */
		this.doArchiveWithBusinessData(businessId, formId, instanceId);
	}

	/**
	 * 保存登记分发业务表的签收人和签收部门数据
	 * 
	 * @param bussinessId
	 * @param userName
	 * @param orgName
	 * @param date
	 * @param flag
	 */
	public void updateRegdocDate(String bussinessId, String userName,
			String orgName, Date date, String flag) throws Exception {
		if (bussinessId != null && !"".equals(bussinessId)) {
			String[] bussinessIds = bussinessId.split(";");
			Connection con = this.getConnection();
			con.setAutoCommit(false);
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {
				String sql = "";
				sql = "select CSQS,ZBCS,QSSJ,QSZT from " + bussinessIds[0]
						+ " where  " + bussinessIds[1] + "='" + bussinessIds[2]
						+ "'";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();
				if (rs.next()) {
					sql = "update " + bussinessIds[0]
							+ " set CSQS=? , ZBCS=? , QSSJ=? , QSZT=? where  "
							+ bussinessIds[1] + "='" + bussinessIds[2] + "'";
					psmt = con.prepareStatement(sql.toString());
					psmt.setString(1, userName);
					psmt.setString(2, orgName);
					if (date == null) {
						psmt.setDate(3, null);
					} else {
						psmt.setDate(3, new java.sql.Date(date.getTime()));
					}
					psmt.setString(4, flag);
					psmt.executeUpdate();
				}
				con.commit();

			} catch (Exception e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw new SystemException("保存签收信息异常！", e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException("关闭记录集异常！", e);
				}
			}
		}
	}

	/**
	 * 退回更改签收人信息
	 * 
	 * @author: qibh
	 * @param bussinessId
	 * @param userName
	 * @param orgName
	 * @param date
	 * @param flag
	 * @throws Exception
	 * @created: 2012-11-27 上午06:21:24
	 * @version :5.0
	 */
	public void updateRegdocDateAndBack(String bussinessId, String userName,
			String orgName, Date date, String flag) throws Exception {
		if (bussinessId != null && !"".equals(bussinessId)) {
			String[] bussinessIds = bussinessId.split(";");
			Connection con = this.getConnection();
			con.setAutoCommit(false);
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {
				String sql = "";
				sql = "select CSQS,ZBCS,QSSJ,QSZT from " + bussinessIds[0]
						+ " where  " + bussinessIds[1] + "='" + bussinessIds[2]
						+ "' for update";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();
				if (rs.next()) {
					String depart = rs.getString("ZBCS");
					if (depart == null) {
						sql = "update "
								+ bussinessIds[0]
								+ " set CSQS=? , ZBCS=? , QSSJ=? , QSZT=? where  "
								+ bussinessIds[1] + "='" + bussinessIds[2]
								+ "'";
						psmt = con.prepareStatement(sql.toString());
						psmt.setString(1, userName);
						psmt.setString(2, orgName);
						if (date == null) {
							psmt.setDate(3, null);
						} else {
							psmt.setDate(3, new java.sql.Date(date.getTime()));
						}
						psmt.setString(4, flag);
						psmt.executeUpdate();
					} else {
						sql = "update " + bussinessIds[0]
								+ " set CSQS=? , QSSJ=? , QSZT=? where  "
								+ bussinessIds[1] + "='" + bussinessIds[2]
								+ "'";
						psmt = con.prepareStatement(sql.toString());
						psmt.setString(1, userName);
						if (date == null) {
							psmt.setDate(2, null);
						} else {
							psmt.setDate(2, new java.sql.Date(date.getTime()));
						}
						psmt.setString(3, flag);
						psmt.executeUpdate();
					}
				}
				con.commit();

			} catch (Exception e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw new SystemException("保存签收信息异常！", e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException("关闭记录集异常！", e);
				}
			}
		}
	}

	/**
	 * 更改废除业务数据
	 * 
	 * @author: qibh
	 * @param bussinessId
	 * @param userName
	 * @param orgName
	 * @param date
	 * @param flag
	 * @throws Exception
	 * @created: 2012-11-26 下午12:26:20
	 * @version :5.0
	 */
	public void updateFeichuDate(String bussinessId, String flag)
			throws Exception {
		if (bussinessId != null && !"".equals(bussinessId)) {
			String[] bussinessIds = bussinessId.split(";");
			Connection con = this.getConnection();
			con.setAutoCommit(false);
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {
				String sql = "";
				sql = "select FCZT from " + bussinessIds[0] + " where  "
						+ bussinessIds[1] + "='" + bussinessIds[2]
						//+ "' for update";
						  +"'";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();
				if (rs.next()) {
					sql = "update " + bussinessIds[0] + " set FCZT=? where  "
							+ bussinessIds[1] + "='" + bussinessIds[2] + "'";
					psmt = con.prepareStatement(sql.toString());
					psmt.setString(1, flag);
					psmt.executeUpdate();
				}
				con.commit();

			} catch (Exception e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw new SystemException("保存废除信息异常！", e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException("关闭记录集异常！", e);
				}
			}
		}
	}

	/**
	 * 保存父级流程表单数据
	 * 
	 * @param fullData
	 * @param bid
	 */
	public void saveParentFormdata(String fullData, String bid, String fileName)
			throws Exception {

		String[] bids = bid.split(";");
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile(); // 如果文件不存在，则创建
		}

		FileUtil.writeFile(fileName, fullData, false);

		Connection con = this.getConnection();
		con.setAutoCommit(false);
		PreparedStatement psmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			// 产生UUID
			if (file != null && file.length() > 0) {
				String sql = "";
				sql = "select PERSON_DEMO from " + bids[0] + " where  "
						+ bids[1] + "='" + bids[2] + "' for update";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();
				if (rs.next()) {
					sql = "update " + bids[0] + " set PERSON_DEMO=? where "
							+ bids[1] + "='" + bids[2] + "'";
					psmt = con.prepareStatement(sql);
					File binaryFile = new File(fileName);
					is = new FileInputStream(binaryFile); // 读入二进制文件
					psmt.setBinaryStream(1, is, is.available());
					psmt.executeUpdate();
				}
				con.commit();
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SystemException("保存附件异常！", e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SystemException("关闭记录集异常！", e);
			}
		}
		if (file.isFile() && file.exists()) {
			file.delete();
		}

		// Connection conn = this.getConnection();
		// conn.setAutoCommit(false);
		// //PreparedStatement pstmt=conn.prepareStatement("update "+bids[0]+"
		// set PERSON_DEMO=? "+"where "+bids[1]+"='"+bids[2]+"'");
		// Statement st = null;
		// try{
		// st = conn.createStatement();
		// st.execute("update "+bids[0]+" set PERSON_DEMO=empty_blob() "+"where
		// "+bids[1]+"='"+bids[2]+"'");
		// }catch(Exception e){
		// e.printStackTrace();
		// }finally{
		// try{
		// if(st!=null){
		// st.close();
		// st=null;
		// }
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		//
		//
		// String selectSql = "select PERSON_DEMO from "+bids[0]+" where
		// "+bids[1]+"='"+bids[2]+"' FOR UPDATE";
		// Statement pstmt= conn.createStatement();
		// ResultSet rs = pstmt.executeQuery(selectSql);
		// BLOB blob=null;
		// while(rs.next()){
		// blob = ( (OracleResultSet) rs).getBLOB(1);
		// System.out.println(rs.getBlob(1).toString());
		//
		// }
		//
		// File binaryFile = new File(fileName);
		// FileInputStream instream = new FileInputStream(binaryFile); //读入二进制文件
		// OutputStream outstream = blob.getBinaryOutputStream();
		// byte[] buffer = new byte[32];
		//
		// int length = -1;
		// while ( (length = instream.read(buffer)) != -1)
		// outstream.write(buffer, 0, length);
		// instream.close();
		// outstream.close();
		//
		// conn.commit();
		// conn.setAutoCommit(true);
		// pstmt.close();
		// conn.close();
		//
		// if(binaryFile.isFile() && binaryFile.exists()){
		// binaryFile.delete();
		// }

	}

	// pstmt.setCharacterStream(1, bais, bytes_zyjs.length);
	// pstmt.executeUpdate();

	/*
	 * Description:获取数据库连接 param: @author 蒋国斌 @date Oct 26, 2011 4:01:27 PM
	 */

	/*
	 * public Connection getConnection() { Properties properties =
	 * this.getProperties(); Connection conn = null; try { conn =
	 * sendDocDao.getConnection();
	 * Class.forName(properties.getProperty("jdbc.driverClassName")); conn =
	 * (OracleConnection) DriverManager.getConnection(properties
	 * .getProperty("jdbc.url"), properties .getProperty("jdbc.username"),
	 * properties .getProperty("jdbc.password")); conn = (OracleConnection)
	 * conn; } catch (Exception e) { e.printStackTrace(); } return conn; }
	 */

	/*
	 * 
	 * Description:获取资源对象 param: 蒋国斌 @date Oct 15, 2011 3:04:46 PM
	 * 
	 * public Properties getProperties() throws SystemException,
	 * ServiceException { try { Properties properties = new Properties(); URL in
	 * = this.getClass().getClassLoader().getResource( "appconfig.properties");
	 * properties.load(new FileInputStream(in.getFile())); return properties; }
	 * catch (ServiceException e) { throw new
	 * ServiceException(MessagesConst.create_error, new Object[] { "获取资源对象" });
	 * } catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * throw new ServiceException(MessagesConst.create_error, new Object[] {
	 * "获取资源对象" }); } catch (IOException e) { // TODO Auto-generated catch block
	 * throw new ServiceException(MessagesConst.create_error, new Object[] {
	 * "获取资源对象" }); } }
	 */

	public Map<String, String> getFormInfo(String tableName,
			String pkFieldName, String pkFieldValue) {
		Map<String, String> FormInfos = new HashMap<String, String>();
		StringBuilder sqlQuery = new StringBuilder("select * from ");
		sqlQuery.append(tableName);
		sqlQuery.append(" where ").append(pkFieldName).append("='")
				.append(pkFieldValue).append("'");
		logger.info("getFormInfo():" + sqlQuery.toString());
		try {
			ResultSet rs = super.executeJdbcQuery(sqlQuery.toString());
			if (rs.next()) {
				ResultSetMetaData r = rs.getMetaData();
				int count = r.getColumnCount();
				for (int i = 1; i <= count; i++) {
					String columnName = r.getColumnName(i);
					String value = rs.getString(columnName);
					FormInfos.put(columnName, value);
				}
			}
			return FormInfos;
		} catch (Exception e) {
			logger.error("getFormInfo()", e);
		}
		return null;
	}

	/**
	 * 查询构建表中字段PERSON_DEMO字段的值
	 * 
	 * @author:邓志城
	 * @date:2011-5-11 下午04:41:57
	 * @param tableName
	 *            表名称
	 * @param pkFieldName
	 *            主键名称
	 * @param pkFieldValue
	 *            主键值
	 * @return 字段PERSON_DEMO值
	 */
	public String getFormData(String tableName, String pkFieldName,
			String pkFieldValue) {
		if ("".equals(pkFieldValue)) {
			return "";
		}
		StringBuilder sqlQuery = new StringBuilder("select PERSON_DEMO from ");
		sqlQuery.append(tableName);
		sqlQuery.append(" where ").append(pkFieldName).append("='")
				.append(pkFieldValue).append("'");
		logger.info("getFormData():" + sqlQuery.toString());
		try {
			ResultSet rs = super.executeJdbcQuery(sqlQuery.toString());
			while (rs.next()) {
				InputStream is = rs.getBinaryStream("PERSON_DEMO");
				if (is != null) {
					byte[] buf = FileUtil.inputstream2ByteArray(is);
					String personDemo = new String(buf);
					return personDemo;
				}
			}
		} catch (Exception e) {
			logger.error("getFormData()", e);
		}
		return null;
	}

	/**
	 * 查询构建表中某字段的值是否存在
	 * 
	 * @author:张磊
	 * @param tableName
	 *            表名称
	 * @param fieldName
	 *            字段名称
	 * @param keyName
	 *            主键名称
	 * @param fieldValue
	 *            字段值
	 * @param keyValue
	 *            主键值
	 * @date:2012-02-14 下午04:41:57
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	public int getCheckValue(String tableName, String fieldName,
			String fieldValue, String keyName, String keyValue) {
		String sql = "";
		if (keyValue.equals("1")) {
			sql = "select * from " + tableName + " where " + fieldName + "='"
					+ fieldValue + "'";
		} else {
			sql = "select * from " + tableName + " where " + fieldName + "='"
					+ fieldValue + "' and " + keyName + "<>'" + keyValue
					+ "' and workflowstate in ('0','1','2')";
		}
		List count = jdbcTemplate.queryForList(sql);
		return count.size();
	}

	/**
	 * 通过流程名称得到流程类型ID
	 * 
	 * @param workflowname
	 *            流程名称
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getPtIdByWorkflowname(String workflowname) {
		StringBuffer sql = new StringBuffer("select pf.twfInfoProcessType")
				.append(" from TwfBaseProcessfile pf").append(
						" where pf.pfName = ?");
		return getServiceDAO().find(sql.toString(), workflowname);
	}

	public WorkflowDao getServiceDAO() {
		return this.serviceDAO;
	}

	/**
	 * 获取主动公开的发文信息的内容和标题(自动处理类调用) 将获取的信息存入文章表归属政务公开栏目，自动发送到政务公开栏目下
	 * 
	 * @param bussiness
	 *            表名;主键;主键值
	 * @param instanceId
	 *            流程实例ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getSendDocContent(String bussiness, String instanceId)
			throws Exception {
		if (bussiness != null && !"".equals(bussiness)) {
			String[] bussinessIds = bussiness.split(";");
			Connection con = this.getConnection();
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {
				String sql = "select WORKFLOWTITLE,SENDDOC_CONTENT from "
						+ bussinessIds[0] + " where  " + bussinessIds[1] + "='"
						+ bussinessIds[2] + "'" + "  and  " + bussinessIds[0]
						+ ".Rest5='1'";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();
				if (rs.next()) {
					ToaInfopublishArticle model = new ToaInfopublishArticle();
					model.setArticlesTitle(rs.getString("WORKFLOWTITLE"));
					// 将blob类型转换成byte[]类型
					byte[] content = null;
					try {
						if (rs.getBlob("SENDDOC_CONTENT") != null) {
							long in = 1L;
							content = rs.getBlob("SENDDOC_CONTENT").getBytes(
									in,
									(int) (rs.getBlob("SENDDOC_CONTENT")
											.length()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					String path = PathUtil.getRootPath() + "common"
								+ File.separatorChar + "ewebeditor"
								+ File.separatorChar + "uploadfile";
				    String newPath = path + File.separatorChar
								+ bussinessIds[2];
					String newPath1 = "common" + File.separatorChar
								+ "ewebeditor" + File.separatorChar + "uploadfile"
								+ File.separatorChar + bussinessIds[2];
					if(content!=null && !"".equals(content)&& !"null".equals(content)){
						InputStream is = FileUtil.ByteArray2InputStream(content);
							File file = new File(newPath);
							FileOutputStream fos = null;
							try {
								fos = new FileOutputStream(file);
								int BUFFER_SIZE = 8192;
								byte[] buffer = new byte[BUFFER_SIZE];
								while (true) {
									int len = is.read(buffer);
									if (len > 0) {
										fos.write(buffer, 0, len);
									} else if (len < 0) {
										break;
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								if (fos != null) {
									try {
										fos.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								if (is != null) {
									try {
										is.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
					}
					StringBuffer htmlString = new StringBuffer();
					HttpServletRequest request = ServletActionContext
							.getRequest();
					String contextPath = request.getContextPath();
					htmlString
							.append("<FONT size=4><FONT size=4>")
							.append("<INPUT style='WIDTH: 708px; HEIGHT: 20px' id=newPath name=newPath/ value='"
									+ newPath1 + "' size=3 type=hidden //> \n")
							.append("<TABLE width='100%' height='100%'>\n")
							.append("<TBODY height='100%' width='100%'>\n")
							.append("<TR height='100%' width='100%'>\n")
							.append("<TD id=offcieTd height='100%' align='center' vAlign=top colSpan=4>\n")
							.append("</TD></TR></TBODY></TABLE></FONT></FONT>");

					model.setArticlesArticlecontent(htmlString.toString());
					model.setArticlesTitlecolor("0");
					model.setArticlesTitlefont("0");
					model.setArticlesHits(0L);
					model.setArticlesAticlestate("2");
					model.setArticlesCreatedate(new Date());
					model.setArticlesAutopublishtime(new Date());
					model.setArticlesLatestchangtime(new Date());
					// 000000003d393fa2013d4424094b0931为政务公开栏目的ID
					articlesManager.saveZWGKArticle(model,
							"000000003d393fa2013d4424094b0931", null);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	/**
	 * 查询用户已办理任务
	 * 
	 * @param page
	 *            分页对象
	 * @param username
	 *            人员名称
	 * @param workflowType
	 *            流程类型
	 * @return com.strongmvc.orm.hibernate.Page
	 * @param businessName
	 *            业务名称（标题）
	 * @param userName
	 *            主办人
	 * @param startDate
	 *            开始时间
	 * @param processTimeout
	 *            是否超时
	 * @param noTotal
	 *            是否统计
	 * @param endDate
	 *            结束时间 内嵌数据对象结构：<br>
	 *            [任务ID,任务创建时间,任务名称,<br>
	 *            流程实例ID,业务创建时间,挂起状态,业务名称,<br>
	 *            发起人,委派人,委派类型（0：委派，1：指派）] *
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getProcessedWorks方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getProcessedWorks(Page page, ProcessedParameter parameter,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {

			String workflowType = parameter.getWorkflowType();
			String excludeWorkflowType = parameter.getExcludeWorkflowType();
			String businessName = parameter.getBusinessName();
			// String userName = parameter.getUserName();
			Date startDate = parameter.getStartDate();
			Date endDate = parameter.getEndDate();
			String state = parameter.getState();
			String isSuspended = parameter.getIsSuspended();
			Object[] toSelectItems = { "processInstanceId", "businessName",
					"processStartDate", "processName", "processTypeId",
					"processEndDate" };
			List sItems = Arrays.asList(toSelectItems);

			Map<String, Object> paramsMap = new HashMap<String, Object>();
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customSelectItems.append("JBPMBUSINESS.BUSINESS_TYPE");
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
					.append(Tjbpmbusiness.getShowableBusinessType())
					.append(") ");

			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ in(" + workflowType
							+ ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ in(" + workflowType
							+ ") ");
				}
			}
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
				if (customQuery.length() == 0) {
					customQuery.append("  pi.TYPE_ID_ not in("
							+ excludeWorkflowType + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ not in("
							+ excludeWorkflowType + ") ");
				}
			}
			User curUser = userService.getCurrentUser();// 获取当前用户
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			if (endDate != null && !"".equals(endDate)) {
				paramsMap.put("processStartDateEnd", endDate);
			}
			if (startDate != null && !"".equals(startDate)) {
				paramsMap.put("processStartDateStart", startDate);
			}
			if (state != null && !"".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			if (isSuspended != null && !"".equals(isSuspended)) {
				paramsMap.put("processSuspend", isSuspended);
			}
			ProcessedParameter processedParameter = new ProcessedParameter();
			processedParameter.setFilterSign(parameter.getFilterSign());
			processedParameter.setUserId(curUser.getUserId());
			processedParameter.setCustomFromItems(customFromItems);
			processedParameter.setCustomQuery(customQuery);
			processedParameter.setQueryWithTaskDate(null);
			processedParameter.setUserIds(parameter.getUserIds());
			initProcessedFilterSign(processedParameter);
			Map orderMap = new LinkedHashMap<Object, Object>();
			orderMap.put("processStartDate", "1");
			page = workflow.getProcessInstanceByConditionForPage(page, sItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			// page = workflow
			// .getProcessInstanceByConditionForPage(page,sItems, paramsMap,
			// orderMap, "", "", "", null);
			List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (page != null && page.getResult() != null) {
				List<String> processInstanceIds = new LinkedList<String>();
				StringBuilder params = new StringBuilder();
				for (Object[] objs : new ArrayList<Object[]>(page.getResult())) {
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setInstanceId(StringUtil
							.castString(objs[0]));
					taskbusinessbean.setBusinessName(StringUtil
							.castString(objs[1]));
					try {
						taskbusinessbean.setWorkflowStartDate(objs[2]==null?null:sdf.parse(StringUtil
								.castString(objs[2])));
						taskbusinessbean.setWorkflowEndDate(objs[5]==null?null:sdf.parse(StringUtil
								.castString(objs[5])));
					}catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					taskbusinessbean.setWorkflowName(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setWorkflowType(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setBusinessType(StringUtil
							.castString(objs[6]));
					beans.add(taskbusinessbean);
					processInstanceIds.add(taskbusinessbean.getInstanceId());
					params.append(taskbusinessbean.getInstanceId()).append(",");
				}
				Map<String, Map> taskInfoMap = null;
				StringBuilder actorid_SQL = new StringBuilder();
				if (processedParameter.getUserIds() != null) {
					List<String> userIds = processedParameter.getUserIds();
					actorid_SQL.append(" TI.ACTORID_ in (");
					for (String userId : userIds) {
						actorid_SQL.append("'").append(userId).append("',");
					}
					actorid_SQL.deleteCharAt(actorid_SQL.length() - 1);
					actorid_SQL.append(") ");
				} else {
					actorid_SQL.append(" TI.ACTORID_='")
							.append(processedParameter.getUserId())
							.append("' ");
				}
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder(
							"SELECT TI.PROCINST_,TI.ID_ AS TASKID,TI.START_ AS TASKSTARTDATE,TI.END_ AS TASKENDDATE,TI.NAME_ AS TASKNAME");
					sql.append(" FROM JBPM_TASKINSTANCE TI WHERE TI.PROCINST_ IN(");
					sql.append(params.toString());
					sql.append(") AND  ")
							.append(actorid_SQL)
							.append(" AND TI.END_ IS NOT NULL  ORDER BY TI.END_ DESC");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						taskInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							if (!taskInfoMap.containsKey(String.valueOf(rsMap
									.get("PROCINST_")))) {
								taskInfoMap.put(
										String.valueOf(rsMap.get("PROCINST_")),
										rsMap);
							}
						}
					}
				}
				/* 所有的用户信息 */
				Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
				for (TaskBusinessBean taskbusinessbean : beans) {
					Map taskMap = taskInfoMap.get(taskbusinessbean
							.getInstanceId());
					if (taskMap == null) {
						continue;
					}
					StringBuilder cruuentUser = new StringBuilder();
					taskbusinessbean.setNodeName(StringUtil.castString(taskMap
							.get("TASKNAME")));
					if ("意见征询".equals(taskbusinessbean.getNodeName())) {
						cruuentUser.append("意见征询");
					} else {
						if (taskbusinessbean.getWorkflowEndDate() == null) {// 流程未结束
							List[] listTemp = sendDocUploadManager
									.getUserBeanTempArrayOfProcessStatusByPiId(
											taskbusinessbean.getInstanceId(),
											AllUserIdMapUserBeanTem);
							for (int index = 0; index < listTemp[0].size(); index++) {
								if (" ".equals(listTemp[1].get(index)
										.toString())) {
									cruuentUser
											.append(","
													+ listTemp[0].get(index)
															.toString());
								} else {
									cruuentUser.append(","
											+ listTemp[0].get(index).toString()
											+ "["
											+ listTemp[1].get(index).toString()
											+ "]");
								}
							}
							cruuentUser.deleteCharAt(0);
						} else {
							cruuentUser.append("已办结");
						}
					}
					taskbusinessbean.setActorName(cruuentUser.toString());
					taskbusinessbean.setTaskId(StringUtil.castString(taskMap
							.get("TASKID")));
					try {
						taskbusinessbean.setTaskEndDate(taskMap.get("TASKENDDATE")==null?null:sdf.parse(StringUtil.castString(taskMap.get("TASKENDDATE"))));
						taskbusinessbean.setTaskStartDate(taskMap
								.get("TASKSTARTDATE")==null?null:sdf.parse(StringUtil.castString(taskMap.get("TASKSTARTDATE"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if ("1".equals(parameter.getState())) {
						taskbusinessbean.setMainActorName(workflow
								.getMainActorInfoByProcessInstanceId(
										taskbusinessbean.getInstanceId())
								.getUserName());
					}
				}
				page.setResult(new ArrayList<TaskBusinessBean>(beans));
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取已办工作" });
		}
		return page;
	}

	/**
	 * @author:luosy
	 * @description: “待办事宜”中显示，且急件需要“标红、置顶”；
	 * @date : 领导联系人 查看领导的待办事宜
	 * @modifyer:
	 * @description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<TaskBusinessBean> getLdTodoByRedType(String workflowType,
			String type, int num) {
		Page page = new Page(num, false);
		try {
			List<TaskBusinessBean> retList = null;
			Object[] sItems = { "taskId", "taskStartDate", "taskNodeId",
					"isReceived", "assignType" };
			List toSelectItems = Arrays.asList(sItems);
			sItems = null;
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			String currentid = userService.getCurrentUser().getUserId();
			String LdId = userService.getLDId(currentid);
			if (LdId == "" || LdId == null) {
				return null;
			}
			paramsMap.put("handlerId", LdId);// 当前用户办理任务
			paramsMap.put("toAssignHandlerId", LdId);
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customSelectItems
					.append("JBPMBUSINESS.END_TIME,WFBASEPROCESSFILE.REST2,JBPMBUSINESS.BUSINESS_TYPE");
			customFromItems
					.append("T_JBPM_BUSINESS JBPMBUSINESS,T_WF_BASE_PROCESSFILE WFBASEPROCESSFILE");
			initTodoSign(type, customSelectItems, customFromItems, customQuery);
			type = null;

			StringBuilder jjCustomQuery = new StringBuilder();
			StringBuilder njjCustomQuery = new StringBuilder();
			if (customQuery.length() == 0) {
				customQuery.append(" WFBASEPROCESSFILE.PF_NAME =  PI.NAME_ ");
			} else {
				customQuery
						.append(" AND  WFBASEPROCESSFILE.PF_NAME =  PI.NAME_ ");
			}
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (workflowType.startsWith("-")) {
					customQuery.append(" and pi.TYPE_ID_ not in("
							+ workflowType.substring(1) + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_  in(" + workflowType
							+ ") ");
				}
			}
			HttpServletRequest requset = ServletActionContext.getRequest();
			String excludeWorkflowType = requset
					.getParameter("excludeWorkflowType");
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
				customQuery.append(" and pi.TYPE_ID_ not in("
						+ excludeWorkflowType + ") ");
			}
			if (customQuery.length() == 0) {
				customQuery.append(" jbpmbusiness.BUSINESS_TYPE in (")
						.append(Tjbpmbusiness.getShowableBusinessType())
						.append(") ");
			} else {
				customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (")
						.append(Tjbpmbusiness.getShowableBusinessType())
						.append(") ");
			}
			if (customQuery.length() == 0) {
				jjCustomQuery
						.append(" @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG > 0");
				njjCustomQuery
						.append(" @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG = 0");
			} else {
				jjCustomQuery
						.append(" AND  @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG > 0");
				njjCustomQuery
						.append(" AND  @businessId = JBPMBUSINESS.BUSINESS_ID AND JBPMBUSINESS.PERSON_CONFIG_FLAG = 0");
			}
			int pageTotalCount = 0;
			int jjpageTotalCount = 0; // 紧急状态总数
			int njjpageTotalCount = 0; // 非紧急状态总数

			// ------- 紧急 ---------
			Page jjpage = null;
			if (num > 0) {
				jjpage = new Page(num, true);
				jjpage = workflow.getTaskInfosByConditionForPage(jjpage,
						toSelectItems, paramsMap, orderMap,
						customSelectItems.toString(),
						customFromItems.toString(), customQuery.toString()
								+ jjCustomQuery.toString(), null, null);
				jjpageTotalCount = (jjpage.getTotalCount() < 0 ? 0 : jjpage
						.getTotalCount());
			}

			// // ------- 非紧急 ---------
			int mod = num - jjpageTotalCount;
			Page njjpage = null;
			if (mod > 0) {
				njjpage = new Page(mod, true);
				njjpage = workflow.getTaskInfosByConditionForPage(njjpage,
						toSelectItems, paramsMap, orderMap,
						customSelectItems.toString(),
						customFromItems.toString(), customQuery.toString()
								+ njjCustomQuery.toString(), null, null);
				njjpageTotalCount = (njjpage == null
						|| njjpage.getTotalCount() < 0 ? 0 : njjpage
						.getTotalCount());
			} else {
				njjpage = new Page(1, true);
				njjpage = workflow.getTaskInfosByConditionForPage(njjpage,
						toSelectItems, paramsMap, orderMap,
						customSelectItems.toString(),
						customFromItems.toString(), customQuery.toString()
								+ njjCustomQuery.toString(), null, null);
				njjpageTotalCount = (njjpage == null
						|| njjpage.getTotalCount() < 0 ? 0 : njjpage
						.getTotalCount());
				njjpage = null;
			}
			pageTotalCount = jjpageTotalCount + njjpageTotalCount;
			destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);

			StringBuilder params = new StringBuilder();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (jjpage != null && jjpage.getResult() != null
					&& !jjpage.getResult().isEmpty()) {
				retList = new LinkedList();
				for (Object object : jjpage.getResult()) {
					if (retList == null) {
						retList = new LinkedList<TaskBusinessBean>();
					}
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
					taskbusinessbean
							.setTaskStartDate((Date) (objs[1] == null ? new Date()
									: objs[1]));
					taskbusinessbean.setNodeId(StringUtil.castString(objs[2]));
					taskbusinessbean.setIsReceived(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setAssignType(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setEndTime(StringUtil.castString(objs[5]));
					taskbusinessbean.setWorkflowAliaName(StringUtil
							.castString(objs[6]));
					taskbusinessbean.setBusinessType(StringUtil
							.castString(objs[7]));
					taskbusinessbean.setBusinessName("<red>");
					retList.add(taskbusinessbean);
					params.append(StringUtil.castString(objs[0])).append(",");
				}
			}
			jjpage = null;

			if (njjpage != null && njjpage.getResult() != null
					&& !njjpage.getResult().isEmpty()) {
				for (Object object : njjpage.getResult()) {
					if (retList == null) {
						retList = new LinkedList<TaskBusinessBean>();
					}
					Object[] objs = (Object[]) object;
					TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
					taskbusinessbean.setTaskId(StringUtil.castString(objs[0]));
					taskbusinessbean.setTaskStartDate((Date) objs[1]);
					taskbusinessbean.setNodeId(StringUtil.castString(objs[2]));
					taskbusinessbean.setIsReceived(StringUtil
							.castString(objs[3]));
					taskbusinessbean.setAssignType(StringUtil
							.castString(objs[4]));
					taskbusinessbean.setEndTime(StringUtil.castString(objs[5]));
					taskbusinessbean.setWorkflowAliaName(StringUtil
							.castString(objs[6]));
					taskbusinessbean.setBusinessType(StringUtil
							.castString(objs[7]));
					taskbusinessbean.setBusinessName("");
					retList.add(taskbusinessbean);
					params.append(StringUtil.castString(objs[0])).append(",");
				}
			}
			njjpage = null;

			if (retList != null) {
				Map<String, Map> processInfoMap = null;
				if (params.length() > 0) {
					params.deleteCharAt(params.length() - 1);
					StringBuilder sql = new StringBuilder();
					sql.append(" SELECT ")
							.append(" TI.TASKID AS  TASKID, ")
							.append(" TI.PROCESSID AS PROCESSINSTANCEID, ")
							.append(" PI.START_USER_NAME_ AS STARTUSERNAME, ")
							.append(" PI.BUSINESS_ID    AS PROCESSMAINFORMBUSINESSID, ")
							.append(" PI.NAME_          AS PROCESSNAME, ")
							.append(" PI.MAINFORM_ID_   AS PROCESSMAINFORMID, ")
							.append(" PI.TYPE_ID_       AS PROCESSTYPEID, ")
							.append(" PI.BUSINESS_NAME_ AS BUSINESSNAME, ")
							.append(" PI.START_		    AS PROCESSSTARTDATE, ")
							.append(" TI.ISBACKSPACE AS ISBACKSPACE ")
							// .append(" TI.ISBACKSPACE AS ISBACKSPACE, ")
							// .append(" TI.ISREASSIGN AS ISREASSIGN ")
							.append("  FROM  ")
							.append(" JBPM_PROCESSINSTANCE PI, ")
							.append(" (SELECT ")
							.append("T.ID_ AS TASKID,T.PROCINST_ AS PROCESSID,T.ISBACKSPACE_ AS ISBACKSPACE,T.ISREASSIGN_ AS ISREASSIGN  ")
							.append(" FROM ").append("JBPM_TASKINSTANCE T ")
							.append("WHERE ").append("T.ID_ IN (")
							.append(params.toString()).append("))TI ")
							.append(" WHERE ")
							.append(" PI.ID_ = TI.PROCESSID ");
					List<Map<String, Object>> taskList = jdbcTemplate
							.queryForList(sql.toString());
					logger.info(sql.toString());
					if (taskList != null && !taskList.isEmpty()) {
						processInfoMap = new HashMap<String, Map>();
						for (Map<String, Object> rsMap : taskList) {
							processInfoMap.put(
									String.valueOf(rsMap.get("TASKID")), rsMap);
						}
					}
				}
				retList = new ArrayList<TaskBusinessBean>(retList);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (TaskBusinessBean taskbusinessbean : retList) {
					Map map = processInfoMap.get(taskbusinessbean.getTaskId());
					taskbusinessbean.setInstanceId(StringUtil.castString(map
							.get("PROCESSINSTANCEID")));
					taskbusinessbean.setStartUserName(StringUtil.castString(map
							.get("STARTUSERNAME")));
					taskbusinessbean.setBsinessId(StringUtil.castString(map
							.get("PROCESSMAINFORMBUSINESSID")));
					taskbusinessbean.setWorkflowName(StringUtil.castString(map
							.get("PROCESSNAME")));
					if (taskbusinessbean.getWorkflowAliaName() == null
							|| "".equals(taskbusinessbean.getWorkflowAliaName())) {
						taskbusinessbean.setWorkflowAliaName(taskbusinessbean
								.getWorkflowName());
					}
					taskbusinessbean.setFormId(StringUtil.castString(map
							.get("PROCESSMAINFORMID")));
					taskbusinessbean.setWorkflowType(StringUtil.castString(map
							.get("PROCESSTYPEID")));
					taskbusinessbean
							.setBusinessName(taskbusinessbean.getBusinessName()
									+ StringUtil.castString(map
											.get("BUSINESSNAME") == null ? ""
											: map.get("BUSINESSNAME")));
					taskbusinessbean.setIsBackspace(StringUtil.castString(map
							.get("ISBACKSPACE")));
					// taskbusinessbean.setAssignType(StringUtil.castString(map
					// .get("ISREASSIGN")));
//					taskbusinessbean.setWorkflowStartDate((Date) map
//							.get("PROCESSSTARTDATE"));
					taskbusinessbean.setWorkflowStartDate(sdf.parse(StringUtil.castString(map
												.get("PROCESSSTARTDATE")==null?"":map
														.get("PROCESSSTARTDATE"))));
				}

				page.setResult(retList);
				page.setTotalCount(pageTotalCount);
				retList = null;
			}
		} catch (Exception e) {
			logger.error("得到待办事宜出错", e);
		}
		return page;
	}

	/**
	 * 信息审签 结束节点 (自动处理类调用) 将获取的信息存入文章表归属每日要请，自动发送到每日要请栏目下
	 * 
	 * @param bussiness
	 *            表名;主键;主键值
	 * @param instanceId
	 *            流程实例ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getYaoQingInfo(String bussiness, String instanceId)
			throws Exception {
		if (bussiness != null && !"".equals(bussiness)) {
			String[] bussinessIds = bussiness.split(";");
			Connection con = this.getConnection();
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {
				String sql = "select WORKFLOWTITLE,PERSON_DEMO from "
						+ bussinessIds[0] + " where  " + bussinessIds[1] + "='"
						+ bussinessIds[2] + "'";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();
				if (rs.next()) {
					ToaInfopublishArticle model = new ToaInfopublishArticle();
					model.setArticlesTitle(rs.getString("WORKFLOWTITLE"));
					// 将blob类型转换成byte[]类型
					byte[] content = null;
					try {
						if (rs.getBlob("PERSON_DEMO") != null) {
							long in = 1L;
							content = rs.getBlob("PERSON_DEMO").getBytes(in,
									(int) (rs.getBlob("PERSON_DEMO").length()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					InputStream is = FileUtil.ByteArray2InputStream(content);
					String path = PathUtil.getRootPath() + "common"
							+ File.separatorChar + "ewebeditor"
							+ File.separatorChar + "uploadfile";
					String newPath = path + File.separatorChar
							+ bussinessIds[2];
					String newPath1 = "common" + File.separatorChar
							+ "ewebeditor" + File.separatorChar + "uploadfile"
							+ File.separatorChar + bussinessIds[2];
					File file = new File(newPath);

					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(file);
						int BUFFER_SIZE = 8192;
						byte[] buffer = new byte[BUFFER_SIZE];
						while (true) {
							int len = is.read(buffer);
							if (len > 0) {
								fos.write(buffer, 0, len);
							} else if (len < 0) {
								break;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					StringBuffer htmlString = new StringBuffer();
					htmlString
							.append("<FONT size=4><FONT size=4>")
							.append("<INPUT style='WIDTH: 708px; HEIGHT: 20px' id=newPath name=newPath/ value='"
									+ newPath1 + "' size=3 type=hidden //> \n")
							.append("<TABLE width='100%' height='100%'>\n")
							.append("<TBODY height='100%' width='100%'>\n")
							.append("<TR height='100%' width='100%'>\n")
							.append("<TD id=offcieTd height='100%' align='center' vAlign=top colSpan=4>\n")
							.append("</TD></TR></TBODY></TABLE></FONT></FONT>");

					model.setArticlesArticlecontent(htmlString.toString());
					model.setArticlesTitlecolor("0");
					model.setArticlesTitlefont("0");
					model.setArticlesAticlestate("2");
					model.setArticlesCreatedate(new Date());
					model.setArticlesAutopublishtime(new Date());
					model.setArticlesLatestchangtime(new Date());
					// 8a928a703bca8513013bd17bbf9700e6为每日要请栏目的ID
					articlesManager.saveZWGKArticle(model,
							"8a928a703bca8513013bd17bbf9700e6", null);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	/**
	 * 将获取的信息存入文章表归属每日要请，自动发送到每日要请栏目下。从信息采编系统里面取值
	 * 
	 * @param bussiness
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveYaoQingInfo(String issId) throws Exception {
		String sql = "select j.jour_name,i.iss_number,to_char(i.iss_time,'mm-dd') from t_info_base_issue i,t_info_base_journal j"
				+ "  where i.jour_id=j.jour_id and i.iss_id=? ";
		Query query = serviceDAO.getSession().createSQLQuery(sql)
				.setString(0, issId);
		List list = query.list();
		String jourName = ((Object[]) list.get(0))[0].toString();
		String issNumber = ((Object[]) list.get(0))[1].toString();
		issNumber = "第" + issNumber + "期";
		String issTime = ((Object[]) list.get(0))[2].toString();
		issTime = issTime.replaceAll("-", "月");
		String path = PathUtil.getRootPath() + "common" + File.separatorChar
				+ "ewebeditor" + File.separatorChar + "uploadfile";
		String newPath = path + File.separatorChar + issId;
		String newPath1 = "common" + File.separatorChar + "ewebeditor"
				+ File.separatorChar + "uploadfile" + File.separatorChar
				+ issId;
		//String path = PathUtil.wordPath() + issId + ".doc";
		ToaInfopublishArticle model = new ToaInfopublishArticle();
		model.setArticlesTitle(jourName + issNumber + issTime);
		StringBuffer htmlString = new StringBuffer();
		htmlString
				.append("<FONT size=4><FONT size=4>")
				.append("<INPUT style='WIDTH: 708px; HEIGHT: 20px' id=newPath name=newPath/ value='"
						+ newPath1 + "' size=3 type=hidden //> \n")
				.append("<TABLE width='100%' height='100%'>\n")
				.append("<TBODY height='100%' width='100%'>\n")
				.append("<TR height='100%' width='100%'>\n")
				.append("<TD id=offcieTd height='100%' align='center' vAlign=top colSpan=4>\n")
				.append("</TD></TR></TBODY></TABLE></FONT></FONT>");
		model.setArticlesIssId(issId);
		model.setArticlesArticlecontent(htmlString.toString());
		model.setArticlesTitlecolor("0");
		model.setArticlesTitlefont("0");
		model.setArticlesAticlestate("2");
		model.setArticlesCreatedate(new Date());
		model.setArticlesAutopublishtime(new Date());
		model.setArticlesLatestchangtime(new Date());
		// 8a928a703bca8513013bd17bbf9700e6为每日要请栏目的ID
		articlesManager.saveZWGKArticle(model,
				"8a928a703bca8513013bd17bbf9700e6", null);
	}
	
	
	/**
	 * 获取每日要请栏目下Page信息
	 * 
	 * @param bussiness
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ToaInfopublishColumnArticl> showListByYQ() throws Exception {
		
			Page<ToaInfopublishColumnArticl> page = new Page<ToaInfopublishColumnArticl>();
			page = articlesManager.getAllColumnArticleList(page, "'8a928a703bca8513013bd17bbf9700e6'", null);
		
		
		
		List<ToaInfopublishColumnArticl> result = page.getResult();
		List<ToaInfopublishColumnArticl> res = new ArrayList<ToaInfopublishColumnArticl>();
		if(result != null)
		{
			if(result.size()>=5){
				for(int i=0;i<result.size();i++)
				{
					if(i<5){
						res.add(result.get(i));
					}
					else{
						break;
					}
				}
			}
			else{
				return result;
			}
			
		}
		return res;
	}
	
	/**
	 * 获取每日要请栏目下的Page信息
	 * 
	 * @param bussiness
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page<ToaInfopublishColumnArticl> showListYQ(Page<ToaInfopublishColumnArticl> page) throws Exception {
		
			page = articlesManager.getYQColumnArticleList(page, "'8a928a703bca8513013bd17bbf9700e6'", null);
		
		return page;
	}
	
	/**
	 * 删除要情信息
	 * 
	 * @param bussiness
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void deleteYaoQingInfo(String issId) throws Exception {
		String hql =  " from ToaInfopublishArticle t  where t.articlesIssId=?";
		Query query = serviceDAO.getSession().createQuery(hql).setString(0, issId);
		List<ToaInfopublishArticle> list = query.list();
		articleDao.delete(list.get(0));
	}
	
	/**
	 * 根据期号ID获取要情信息
	 * 
	 * @param bussiness
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ToaInfopublishArticle getYaoQingByIssue(String issId) throws Exception {
		String hql =  " from ToaInfopublishArticle t  where t.articlesIssId=?";
		Query query = serviceDAO.getSession().createQuery(hql).setString(0, issId);
		List<ToaInfopublishArticle> list = query.list();
		if(list.size()>0){
			return(list.get(0));
		}
		else{
			return null;
		}
	}
	
	/**
	 * 保存要情信息
	 * 
	 * @param bussiness
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveYaoQing(ToaInfopublishArticle pub) throws Exception {
		serviceDAO.save(pub);
	}

	/**
	 * 截取字符串长度，判断是数字还是字符串。
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public String getStr(String str, int len) throws Exception {

		int a = 0;// 预期计数：中文2字节，英文1字节
		int i = 0;// 循环计数
		String temp = "";// 临时字串
		for (i = 0; i < str.length(); i++) {
			if (str.codePointAt(i) > 255) {
				a += 2;// 按照预期计数增加2
			} else {
				a++;
			}
			if ((a - (i * 0)) > len) {// 如果增加计数后长度大于限定长度，就直接返回临时字符串
				return temp + "..";
			}
			temp += str.charAt(i);// 将当前内容加到临时字符串
		}
		return str; // 如果全部是单字节字符，就直接返回源字符串
	}
	
	/**
	 * 根据流程id获取转办记录
	 * @param instanceId
	 * @return
	 */
	public List<Object[]> getTransmittingRecord(String instanceId){
		List<Object[]> transmittingList = new ArrayList<Object[]>();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "select t.NODE_NAME_, t.END_,t.ISRECEIVE_ " +
					"from JBPM_TASKINSTANCE t " +
					"where t.PROCINST_ = '" + instanceId + "' " +
					"and t.NAME_ like '转办%' ";
		try {
			psmt = getCurrentConnection().prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()){
				//任务节点名称
				String nodeName = rs.getString("NODE_NAME_");
				//办结时间(可能为null)
				Date endDate = rs.getTimestamp("END_");
				//是否已签收("1"为已签收)
				String isReceive = rs.getString("ISRECEIVE_");
				Object[] objs = new Object[]{nodeName,endDate,isReceive};
				transmittingList.add(objs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(psmt != null){
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return transmittingList;
	}
	
	/**
	 * 转办记录生成html
	 * @param instanceId
	 * @param request
	 */
	public void transmitting2html(String instanceId, HttpServletRequest request){
		List<Object[]> transmittingList = getTransmittingRecord(instanceId);
		//根据流程实例id获取流程信息
		Object[] processInfo = processinstanceservice.getProcessInstanceByPiId(instanceId);
		//获取流程名称
		String workflowName = (String)processInfo[1];
		String td1="30%";
		String td4 = "40%";
	    String td5 = "30%";
		String tableString = "<table cellSpacing=1 cellPadding=1 width=\"100%\" border=\"0\" class=\"table1\">";
		String trString = "<tr class=\"biao_bg2\">"+
		"<td class=\"td1\" align=\"center\" width=\""+td1+"\" >"+
			"环节名称"+
		"</td>"+
		"<td class=\"td3\" align=\"center\" width=\""+ td4 +"\" >"+
			"处理时间"+
		"</td>"+
		"<td class=\"td4\" align=\"center\" width=\""+td5+"\" >"+
			"处理状态"+
		"</td>";
		/*if(isUseCASign.equals("1")){
			td4 = 120;
			trString = trString + "<td class=\"td5\"  align=\"center\" width=\""+td5+"\" >"+
			"<strong>CA签名</strong>"+
			"</td>";
		}*/
		trString = trString +"</tr>";
		String title = "";
		if(!transmittingList.isEmpty()){
			for(int j=0;j<transmittingList.size();j++){
				Object[] objs = transmittingList.get(j);
				if(j == 0){
					title = title + "<font style=\"font: bold;color:blue;\">"+workflowName+"</font>";
					int colspan = 5;
					tableString  = tableString +"<tr class=\"biao_bg1\" style=\"BACKGROUND-COLOR:#FFFFFF;\">"+
					"<td align=\"left\"  colspan=\""+colspan+"\">"+title+"</td>"+
					"</tr>";
					tableString  = tableString +trString;
				}
				if(objs[0] != null){
					String state = "未签收";
					if(objs[1] != null && objs[2] != null){
						state = "已反馈";
					}else if(objs[1] == null && objs[2] != null){
						state = "已签收";
					}
					tableString  = tableString +"<tr class=\"biao_bg1\">"+
					"<td align=\"left\">"+
					objs[0]+
					"</td>"+
					"<td align=\"left\">"+
					(objs[1] == null ?"":new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)objs[1]))+
					"</td>"+
					"<td align=\"left\">"+state+
					"</td>";
					tableString  = tableString + "</tr>";
				}				
			}				
		}
		tableString = tableString+"</table><br/>";
		request.setAttribute("tableString", tableString);
		//用于返回页面标识(区分办理记录和转办记录)
		request.setAttribute("isTransmitting", "1");
	}
	/**
	 * author:luosy 2013-7-18
	 * description: 启动新流程是设置父表单数据
	 * modifyer:
	 * description:
	 * @param instanceId
	 * @param taskId
	 * @param workflowName
	 * @param bussinessId
	 */
	public String startNewinstanceSetPersonDemo(String FormId,String instanceId,String parentinstanceId,String bussId){
		Long subFormId = Long.valueOf(FormId);//父流程的表单id
		String information = JsonUtil.generateApproveToJsonBase64(workflow.getWorkflowApproveinfo(String.valueOf(instanceId),bussId));
		String data = bussId + ";" + subFormId + ";" + parentinstanceId + ";" + instanceId+ "@begin@" + information;
		ProcessInstance processInstance = workflow.getProcessInstanceById(instanceId);
//		processInstance.getContextInstance().setVariable("@{parentInstanceId}", parentinstanceId);
		processInstance.getContextInstance().setVariable("@{personDemo}", data);
		return null;
	}
	
	/**
	 * 根据流程别名获取流程名称
	 * ps.流程名称可能为多个，流程名称间用","号隔开
	 * @param AliaName 流程别名
	 * @return
	 */
	public String getWorkflowNamesByAliaName(String AliaName){
		String workflowNames = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT PF_NAME FROM T_WF_BASE_PROCESSFILE WHERE REST2='" + AliaName + "'";
		try {
			psmt = getCurrentConnection().prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()){
				workflowNames = workflowNames + "," + rs.getString("PF_NAME");					
			}
			if(workflowNames.length()!=0){
				workflowNames = workflowNames.substring(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(psmt != null){
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return workflowNames;
	}
}
