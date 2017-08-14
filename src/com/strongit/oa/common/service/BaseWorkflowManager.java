package com.strongit.oa.common.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.utils.JavaUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.DB2390Dialect;
import org.hibernate.dialect.DB2400Dialect;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.DataDirectOracle9Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQLInnoDBDialect;
import org.hibernate.dialect.MySQLMyISAMDialect;
import org.hibernate.dialect.Oracle9Dialect;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.impl.SessionImpl;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.approveinfo.ApproveinfoManager;
import com.strongit.oa.archive.ITempFileService;
import com.strongit.oa.bgt.senddoc.DocManager;
import com.strongit.oa.bo.ToaDefinitionPlugin;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.ITransitionService;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.comparator.BaseComparator;
import com.strongit.oa.common.workflow.comparator.DateComparator;
import com.strongit.oa.common.workflow.comparator.PersonComparator;
import com.strongit.oa.common.workflow.comparator.groupby.GroupByAfterSorted;
import com.strongit.oa.common.workflow.comparator.groupby.GroupByDepartment;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.parameter.Parameter;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.common.workflow.service.transitionservice.pojo.TransitionsInfoBean;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.PropertiesUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.work.util.Constants;
import com.strongit.oa.work.util.WorkFlowBean;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseNodesettingPlugin;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.uupInterface.IUupInterface;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 用于抽取工作流中一些公用方法 在OA业务系统中涉及工作流的操作太多，这样抽象可以有效消除重复
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2009-12-11 下午04:16:53
 * @version 2.0.2.3
 * @classpath com.strongit.oa.common.service.BaseWorkflowManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
@OALogger
public class BaseWorkflowManager {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected IWorkflowService workflow; // 工作流服务
	@Autowired
	protected IWorkflowConstService workflowConstService; // 工作流自定义常量服务

	@Autowired
	protected IUserService userService;// 统一用户服务

	@Autowired
	protected IUupInterface uupInterface;
	
	@Autowired
	protected ApproveinfoManager approveManager;

	@Autowired
	SessionFactory sessionFactory; // 提供session

	@Autowired
	protected ITempFileService tempFileManager;// 文件归档Manager

	@Autowired
	protected SystemsetManager systemsetManager;// 文件归档Manager

	@Autowired
	protected DefinitionPluginService definitionPluginService;// 流程定义插件服务类.

	@Autowired
	protected ITransitionService transitionService;// 定义迁移线服务
	@Autowired
	protected INodesettingPluginService nodesettingPluginService;// 定义节点设置信息服务
	
	@Autowired
	protected DocManager docManager;// 定义节点设置信息服务

	@Autowired
	protected JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持


	public static final String WORKFLOW_NAME = "WORKFLOWNAME";// 流程名称字段名

	public static final String WORKFLOW_STATE = "WORKFLOWSTATE";// 流程状态

	// (0：草稿，1：非草稿)

	public static final String WORKFLOW_AUTHOR = "WORKFLOWAUTHOR";// 流程拟稿人

	public static final String WORKFLOW_CODE = "WORKFLOWCODE";// 流程编号

	public static final String WORKFLOW_TITLE = "WORKFLOWTITLE";// 流程标题

	public static final String PERSON_CONFIG_FLAG = "PERSON_CONFIG_FLAG";// 紧急程度

	public static final String PERSON_DEMO = "PERSON_DEMO";// 内容

	public static String[] SYSTEM_INFO_ITEM = new String[] { WORKFLOW_NAME,
			WORKFLOW_STATE, WORKFLOW_AUTHOR, WORKFLOW_CODE, WORKFLOW_TITLE };

	private Dialect _database_dialect = null;

	private String _current_schema_name = null;

	private boolean _get_current_schema = false;

	private String _current_catalog_name = null;

	private boolean _get_current_catalog = false;

	/**
	 * 为子类提供session,方便jdbc相关操作
	 * 
	 * @author:邓志城
	 * @date:2009-12-11 下午04:22:43
	 * @return Hibernate session.
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 得到数据库连接
	 * 
	 * @author:邓志城
	 * @date:2009-12-11 下午05:22:34
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected Connection getConnection() {
		return getSession().connection();
	}

	/**
	 * 得到数据库连接
	 * 
	 * @author 严建
	 * @return
	 * @createTime Mar 23, 2012 1:58:12 PM
	 */
	public Connection getCurrentConnection(){
		return getConnection();
	}
	/**
	 * 根据表名称获取表主键名称,不支持复核主键.
	 * 
	 * @author:邓志城
	 * @date:2010-3-1 下午05:30:48
	 * @param tableName
	 *            表名称
	 * @return 主键名称
	 * @throws SystemException
	 *             数据库不支持getMetaData()方法.
	 */
	public String getPrimaryKeyName(String tableName) throws SystemException {
		Connection con = getConnection();
		try {
			DatabaseMetaData meta = con.getMetaData();
			ResultSet rs = meta.getPrimaryKeys(GetCurrentCatalog(),
					GetCurrentSchema(), tableName);
			if (rs.next()) {
				return rs.getString("COLUMN_NAME");// 返回主键名称
			}
			return null;
		} catch (SQLException e) {
			logger.error("数据库不支持获取主键名", e);
			throw new SystemException(e);
		}
	}

	public List<String> getTables() throws DAOException, SystemException {
		List<String> lst = new ArrayList<String>();
		ResultSet rs = null;
		try {
			Connection conn = getConnection();
			rs = conn.getMetaData().getTables(GetCurrentCatalog(),
					GetCurrentSchema(), null, new String[] { "TABLE" });
			while (rs.next()) {
				String tn = rs.getString("TABLE_NAME");
				lst.add(tn);
			}
		} catch (DAOException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					logger.error(JavaUtils.stackToString(ex));
				}
			}
		}
		return lst;
	}

	protected Dialect getDatabaseDialect() throws DAOException, SystemException {
		if (this._database_dialect == null) {
			this._database_dialect = ((SessionImpl) getSession()).getFactory()
					.getDialect();
		}
		return this._database_dialect;
	}

	@SuppressWarnings("deprecation")
	protected boolean isOracle() throws DAOException, SystemException {
		return ((getDatabaseDialect() instanceof OracleDialect)
				|| (getDatabaseDialect() instanceof Oracle9Dialect) || (getDatabaseDialect() instanceof DataDirectOracle9Dialect));
	}

	protected boolean isSQLServer() throws DAOException, SystemException {
		return (getDatabaseDialect() instanceof SQLServerDialect);
	}

	protected boolean isDB2() throws DAOException, SystemException {
		return ((getDatabaseDialect() instanceof DB2Dialect)
				|| (getDatabaseDialect() instanceof DB2390Dialect) || (getDatabaseDialect() instanceof DB2400Dialect));
	}

	protected boolean isMySQL() throws DAOException, SystemException {
		return ((getDatabaseDialect() instanceof MySQLDialect)
				|| (getDatabaseDialect() instanceof MySQL5Dialect)
				|| (getDatabaseDialect() instanceof MySQL5InnoDBDialect)
				|| (getDatabaseDialect() instanceof MySQLInnoDBDialect) || (getDatabaseDialect() instanceof MySQLMyISAMDialect));
	}

	protected String getDatabaseProductName() throws DAOException,
			SystemException {
		try {
			Connection conn = getConnection();
			return conn.getMetaData().getDatabaseProductName();
		} catch (SQLException ex) {
			throw new DAOException(ex);
		}
	}

	protected String GetCurrentSchema() throws DAOException, SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (!(this._get_current_schema)) {
				if (isOracle()) {
					@SuppressWarnings("unused")
					String sql = "select SYS_CONTEXT('USERENV','CURRENT_SCHEMA') as cs from dual";
					ps = getConnection()
							.prepareStatement(
									"select SYS_CONTEXT('USERENV','CURRENT_SCHEMA') as cs from dual");
					rs = ps.executeQuery();
					if (rs.next())
						this._current_schema_name = rs.getString(1);
				} else if (isSQLServer()) {
					this._current_schema_name = null;
				}
				this._get_current_schema = true;
			}
		} catch (SQLException ex) {
		} catch (SystemException ex) {
		} catch (DAOException ex) {
		} catch (Exception ex) {
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					logger.warn(JavaUtils.stackToString(ex));
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					logger.warn(JavaUtils.stackToString(ex));
				}
			}
		}
		return this._current_schema_name;
	}

	protected String GetCurrentCatalog() throws DAOException, SystemException {
		try {
			if (!(this._get_current_catalog)) {
				this._current_catalog_name = getConnection().getCatalog();
				this._get_current_catalog = true;
			}
		} catch (SQLException ex) {
			throw new DAOException(ex);
		} catch (SystemException ex) {
			throw ex;
		} catch (DAOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
		return this._current_catalog_name;
	}

	/**
	 * 根据表名得到字段信息：类型完整名称、字段长度、字段SQL类型、字段类型名称
	 * 
	 * @author:邓志城
	 * @date:2010-3-2 下午02:29:25
	 * @param tableName
	 *            表名称
	 * @return 字段信息Map<字段名称,字段信息BO>
	 * @throws SystemException
	 *             执行查询时或者关闭连接时抛出异常.
	 */
	protected Map<String, EFormField> getColumnInfo(String tableName)
			throws SystemException {
		Connection con = getConnection();
		try {
			String sql = "select * from " + tableName + " where 1=0";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			Map<String, EFormField> columnInfo = new HashMap<String, EFormField>();
			for (int i = 1; i <= count; i++) {
				EFormField field = new EFormField();
				field.setFieldClassName(rsmd.getColumnClassName(i));
				field.setFieldDiaplaySize(rsmd.getColumnDisplaySize(i));
				field.setFieldType(rsmd.getColumnType(i));
				field.setFieldTypeName(rsmd.getColumnTypeName(i));
				columnInfo.put(rsmd.getColumnName(i), field);
			}
			return columnInfo;
		} catch (SQLException ex) {
			logger.error("获取列信息时发生异常", ex);
			throw new SystemException(ex);
		}
	}

	/**
	 * 执行jdbc操作
	 * 
	 * @author:邓志城
	 * @date:2009-12-11 下午05:23:46
	 * @param sql
	 * @throws DAOException
	 */
	protected void executeJdbcUpdate(String sql) throws DAOException {
		try {
			getConnection().prepareStatement(sql).execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * 执行JDBC查询
	 * 
	 * @author:邓志城
	 * @date:2009-12-11 下午05:25:33
	 * @param sql
	 * @return
	 * @throws DAOException
	 */
	protected ResultSet executeJdbcQuery(String sql) throws DAOException {
		try {
			return getConnection().prepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public List queryForList(String sql) throws DAOException, SystemException {
		try {
			logger.info(sql);
			return jdbcTemplate.queryForList(sql);
		} catch (DataAccessException ex) {
			throw new DAOException(ex);
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	public Map queryForMap(String sql) throws DAOException, SystemException {
		try {
			logger.info(sql);
			return jdbcTemplate.queryForMap(sql);
		} catch (DataAccessException ex) {
			throw new DAOException(ex);
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * 根据给定任务实例ID获取所在流程实例的所有处理意见
	 * 
	 * @author 喻斌
	 * @date May 14, 2009 3:02:07 PM
	 * @param taskInstanceId
	 *            -任务实例Id
	 * @return List<Object[]> 流程实例处理意见集<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 *         Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)任务处理人,
	 *         (5)任务处理意见, (6)任务处理时间}
	 *         </p>
	 * @throws WorkflowException
	 */
	@Transactional(readOnly = true)
	public List<Object[]> getProcessHandleByTaskInstanceId(String taskInstanceId)
			throws WorkflowException {
		return workflow.getProcessHandleByTaskInstanceId(taskInstanceId);
	}

	/**
	 * 根据流程实例Id获取流程对应的表单Id和业务数据Id
	 * 
	 * @author 喻斌
	 * @date Apr 7, 2009 10:26:29 AM
	 * @param processInstanceId
	 *            -流程实例Id
	 * @return List<Object[]> 表单Id和业务数据Id信息集<br>
	 *         <p>
	 *         数据结构：
	 *         </p>
	 *         <p>
	 *         Object[]{(0)表单Id,(1)业务数据Id}
	 *         </p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getFormIdAndBusinessIdByProcessInstanceId(
			String processInstanceId) throws SystemException {
		return workflow
				.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
	}

	/**
	 * 根据流程实例id获取任务处理意见.
	 * 
	 * @author:邓志城
	 * @date:2010-3-1 下午07:33:58
	 * @param processInstanceId
	 * @return
	 */
	public java.util.List<com.strongit.workflow.bo.TwfInfoApproveinfo> getApproveInfosByPIId(
			java.lang.String processInstanceId) {
		return workflow.getApproveInfosByPIId(processInstanceId);
	}

	/**
	 * 获取所偶有非系统流程类型
	 * 
	 * @author:邓志城
	 * @date:2009-12-11 下午04:27:04
	 * @return List<Object[]> 流程类型信息集<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 *         Object[]{类型Id, 类型名称,是否系统类型:1：是；}
	 *         </p>
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public List<Object[]> getAllProcessTypeList() throws SystemException,
			ServiceException {
		// 需要过滤掉收发文类型
		try {
			List<Object[]> typeList = workflow.getAllProcessTypeList();
			/*
			 * List<Object[]> newTypeList = new ArrayList<Object[]>(); for(int
			 * i=0;i<typeList.size();i++){ Object[] obj = typeList.get(i);
			 * String leavel = obj[2]==null?"":obj[2].toString();
			 * if(!"1".equals(leavel)){ newTypeList.add(obj); } }
			 */
			return typeList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "流程类型" });
		}
	}

	/**
	 * author:lanlc description:个人任务催办 modifyer:
	 * 
	 * @param processId
	 *            -流程实例Id
	 * @param noticeTitle
	 *            -催办标题<br>
	 *            <p>
	 *            若为null或“”则取系统默认催办内容
	 *            </p>
	 * @param noticeMethod
	 *            -催办方式<br>
	 *            <p>
	 *            message：短消息方式
	 *            </p>
	 *            <p>
	 *            (see:WorkflowConst.WORKFLOW_NOTICE_TYPE_MESSAGE)
	 *            </p>
	 *            <p>
	 *            mail：邮件方式
	 *            </p>
	 *            <p>
	 *            (see:WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL)
	 *            </p>
	 *            <p>
	 *            notice：通知方式
	 *            </p>
	 *            <p>
	 *            (see:WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE)
	 *            </p>
	 * @param handlerMes
	 *            -对任务处理人的催办内容<br>
	 *            <p>
	 *            若为null或“”则取系统默认催办内容
	 *            </p>
	 */
	public void urgencyProcessByPerson(String processId, String noticeTitle,
			List<String> noticeMethod, String handlerMes, OALogInfo... infos)
			throws SystemException, ServiceException {
		try {
			workflow.urgencyProcessByPerson(processId, noticeTitle,
					noticeMethod, handlerMes);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "催办" });
		}
	}

	/*
	 * 
	 * Description:人工对单个任务实例进行催办 param: taskInstanceId 任务示例ID @author 彭小青 @date
	 * Feb 25, 2010 3:16:03 PM
	 */
	public void urgencyTaskInstanceByPerson(String taskInstanceId,
			String noticeTitle, List<String> noticeMethod, String handlerMes,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {
			workflow.urgencyTaskInstanceByPerson(taskInstanceId, noticeTitle,
					noticeMethod, handlerMes);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "催办" });
		}
	}

	/*
	 * 
	 * Description:根据流程实例ID和节点ID获取任务实例ID param: @author 彭小青 @date Feb 25, 2010
	 * 3:49:18 PM
	 */
	public List getTaskInfosByConditionForList(
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			java.util.Map<java.lang.String, java.lang.Object> customValues)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflow.getTaskInfosByConditionForList(toSelectItems,
				paramsMap, orderMap, customSelectItems, customFromItems,
				customQuery, customValues);
	}

	/**
	 * 验证指定的任务ID是否在当前用户列表里
	 * 
	 * @author:邓志城
	 * @date:2010-11-24 上午10:52:11
	 * @param taskId
	 * @return
	 */
	public boolean isTaskInCurrentUser(String taskId) {
		Object[] toSelectItems = { "taskId" };
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "2");// 取非办结任务
		paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
		List list = this.getTaskInfosByConditionForList(sItems, paramsMap,
				null, null, null, null, null);
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object objs = (Object) list.get(i);
				if (taskId.equals(objs.toString())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取表单ID和业务ID
	 * 
	 * @param taskId
	 * @return String[] 数据格式:[业务ID,表单ID]
	 */
	@Transactional(readOnly = true)
	public String[] getFormIdAndBussinessIdByTaskId(String taskId)
			throws SystemException, ServiceException {
		try {
			String[] ret = new String[2];
			String strNodeInfo = workflow.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			if ("form".equals(arrNodeInfo[0])) {
				ret[0] = arrNodeInfo[2];
				ret[1] = arrNodeInfo[3];
			} else {
				// 异常情况，抛出异常
			}

			return ret;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取表单信息" });
		}
	}

	/**
	 * 根据流程实例Id和节点Id得到该节点对应的表单Id和业务数据标识
	 * 
	 * @author 喻斌
	 * @date Jan 20, 2010 9:33:05 AM
	 * @param processInstanceId
	 *            -流程实例Id
	 * @param nodeId
	 *            -节点Id
	 * @return Object[] 节点对应的表单Id和业务数据标识，为空则返回null<br>
	 *         <p>
	 *         Object[]{(0)表单Id,(1)表单对应业务数据标识,(2)主表单对应业务数据标识}
	 *         </p>
	 * @throws WorkflowException
	 */
	public Object[] getFormIdAndBusiIdByPiIdAndNodeId(String pid, String nodeId)
			throws WorkflowException {
		return workflow.getFormIdAndBusiIdByPiIdAndNodeId(pid, nodeId);
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
	 * @param concurrentTrans
	 *            并发流向
	 */
	@SuppressWarnings("unchecked")
	public void handleWorkflowNextStep(String taskId, String transitionName,
			String returnNodeId, String isNewForm, String formId,
			String businessId, String suggestion, String curActorId, String[] taskActors,Parameter... parameters)
			throws SystemException, ServiceException {
		try {
			if(parameters != null && parameters.length >0){
				if(parameters[0].isDaiBan()){
					resumeConSignTask(taskId);
					workflow.signForTask(curActorId,taskId, "0");
				}
			}
			// 校验任务是否已签收
			Object[] toSelectItems = { "isReceived" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskId", taskId);
			List listWorkflow = workflow.getTaskInfosByConditionForList(sItems,
					paramsMap, null, null, null, null, null);
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				Object isReceived = listWorkflow.get(0);
				if (!"1".equals(isReceived)) {// 未签收状态
					this.signForTask(taskId, "0", new OALogInfo("签收任务"));
				}
			}
			
			workflow.goToNextTransition(taskId, transitionName, returnNodeId,
					isNewForm, formId, businessId, suggestion, curActorId, taskActors,parameters);
			
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "流程跳转" });
		}
	}

	/**
	 * 提交工作流处理
	 * 
	 * @param formId
	 *            表单ID
	 * @param workflowName
	 *            流程名称
	 * @param docId
	 *            公文ID
	 * @param docTitle
	 *            公文标题
	 * @param taskActors
	 *            下一步处理人([人员ID|节点ID，人员ID|节点ID……])
	 */
	public String handleWorkflow(String formId, String workflowName,
			String docId, String docTitle, String[] taskActors,
			String tansitionName, String concurrentTrans, String sugguestion)
			throws SystemException, ServiceException {
		try {
			User curUser = userService.getCurrentUser();
			// 当业务数据标题为空时，默认内容取工作流流程名称
			if (docTitle == null || "".equals(docTitle)) {
				docTitle = workflowName;
			}
			return workflow.startWorkflow(formId, workflowName, curUser
					.getUserId(), docId, docTitle, taskActors, tansitionName,
					concurrentTrans, sugguestion);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "工作流处理" });
		}
	}

	/**
         * 启动流程并使流程停留在第一个任务节点,通常为拟稿节点
         * 
         * @author 严建
         * @param formId -
         *                -表单Id
         * @param workflowname -
         *                -流程名称
         * @param businessId -
         *                -业务数据标识
         * @param userId -
         *                -流程发起人Id，如果为空，则流程在第一个任务的处理人默认为流程发起人
         * @param businessname -
         *                -业务名称
         * @param taskActorSet -
         *                -流程任务选择的处理人信息，此处应为第一个任务的处理人信息， 为空则取流程发起人为任务处理人 数据结构为：
         *                String[]{人员Id|节点Id, 人员Id|节点Id, ..., 人员Id|节点Id}
         * @param transitionName -
         *                -开始节点后的路径名称，为空则取默认路径
         * @param submitOption -
         *                -流程启动时的输入的提交意见，若第一个任务的处理人不是流程发起人（userId），则意见不能输入，必须为空
         * @return 流程实例Id
         * @throws com.strongit.workflow.exception.WorkflowException
         * @createTime Apr 13, 2012 10:16:02 AM
         */
	public java.lang.String startProcessToFistNode(java.lang.String formId,
	    java.lang.String workflowname, java.lang.String businessId,
	    java.lang.String userId, java.lang.String businessname,
	    java.lang.String[] taskActorSet, java.lang.String transitionName,
	    java.lang.String submitOption) throws SystemException,
	    ServiceException {
	try {
	    if (formId == null || "".equals(formId)) {// 未使用表单的情况.
		formId = "0";
	    }
	    // 当业务数据标题为空时，默认内容取工作流流程名称
	    if (businessname == null || "".equals(businessname)) {
		businessname = workflowname;
	    }
	    return workflow.startProcessToFistNode(Long.valueOf(formId),
		    workflowname, businessId, userId,
		    businessname, taskActorSet, transitionName, submitOption);
	} catch (ServiceException e) {
	    throw new ServiceException(MessagesConst.operation_error,
		    new Object[] { "工作流处理" });
	}
    }
	/**
	 * 获取表单对应的流程
	 * 
	 * @param formId
	 * @return List
	 */
	@Transactional(readOnly = true)
	public List<Object[]> getWorkflowsByFormId(String formId)
			throws SystemException, ServiceException {
		try {
			return workflow.getFormRelativeWorkflow(formId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取流程" });
		}
	}

	/**
	 * 获取指定任务实例的下一步可选流向
	 * 
	 * @param taskId
	 *            任务ID
	 * @return List中数据结构为： Object[]{(0)转移名称, (1)转移Id, (2)并发标识, (3)节点设置信息}
	 *         其中“节点设置信息”为Set集合 并发标识为：0、正常模式；1、并发模式 节点设置信息数据结构
	 *         需要动态设置：activeSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         子流程中需要动态设置：subactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         父流程中需要动态设置：supactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         不需要动态设置：notActiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是条件节点：decideNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是子流程节点：subProcessNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是结束节点：endNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 */
	@Transactional(readOnly = true)
	public List getNextTransitions(String taskId) throws SystemException,
			ServiceException {
		try {
			return transitionService.getNextTransitions(taskId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取下一步可选步骤" });
		}
	}

	/**
	 * @author 严建 获取流程流转的迁移线 (1)获取指定任务实例的下一步可选流向 (2)流程开始时获取下一步骤
	 * 
	 * @param taskId
	 *            任务ID
	 * @param workflowName
	 * @return List List 下一步转移信息 List中数据结构为： Object[]{(0)转移名称, (1)转移Id, (2)并发标识,
	 *         (3)节点设置信息} 其中“节点设置信息”为Set集合 并发标识为：0、正常模式；1、并发模式 节点设置信息数据结构
	 *         需要动态设置：activeSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         子流程中需要动态设置：subactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         父流程中需要动态设置：supactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         不需要动态设置：notActiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是条件节点：decideNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是子流程节点：subProcessNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是结束节点：endNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 * @throws SystemException
	 * @throws ServiceException
	 * @createTime Mar 7, 2012 10:25:34 PM
	 */
	@Transactional(readOnly = true)
	public List getNextTransitions(String taskId, String workflowName)
			throws SystemException, ServiceException {
		try {
			return transitionService.getNextTransitions(taskId, workflowName);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取下一步可选步骤" });
		}
	}

	/**
	 * 通过任务节点ID得到任务节点默认设置人员信息
	 * 
	 * @param nodeId
	 * @param taskId
	 * @modify 增加参数 transitionId 用于支持在迁移线上选择人员
	 * @return List [人员ID，人员名称，组织机构ID]
	 */
	@Transactional(readOnly = true)
	public List getWorkflowTaskActors(String nodeId, String taskId,
			String transitionId) throws SystemException, ServiceException {
		try {
			return workflow.getTaskActorsByTask(nodeId, taskId, transitionId);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取人员信息" });
		}
	}

	/**
	 * 流程开始时获取下一步骤
	 * 
	 * @param workflowName
	 * @return List List 下一步转移信息 List中数据结构为： Object[]{(0)转移名称, (1)转移Id, (2)并发标识,
	 *         (3)节点设置信息} 其中“节点设置信息”为Set集合 并发标识为：0、正常模式；1、并发模式 节点设置信息数据结构
	 *         需要动态设置：activeSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         子流程中需要动态设置：subactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         父流程中需要动态设置：supactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         不需要动态设置：notActiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是条件节点：decideNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是子流程节点：subProcessNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 *         下一步骤是结束节点：endNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
	 */
	@Transactional(readOnly = true)
	public List getStartWorkflowTransitions(String workflowName)
			throws SystemException, ServiceException {
		try {
			return transitionService.getStartWorkflowTransitions(workflowName);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取下一步可选步骤" });
		}
	}

	/**
	 * author:dengzc description:恢复被挂起的与给定任务同一批的其他会签任务 modifyer: description:
	 * 
	 * @param taskId
	 * @return
	 * @throws WorkflowException
	 */
	public void resumeConSignTask(String taskId) throws SystemException {
		workflow.resumeConSignTask(taskId);
	}

	/**
	 * 得到当前用户相应类别的任务列表, 修改成挂起任务及指派委托状态也查找出来，在展现层展现状态
	 * 增加查询条件参数,添加流程类型参数processType，以区分发文、收文等
	 * 
	 * @param page
	 *            分页对象
	 * @param workflowType
	 *            流程类型参数 1)大于0的正整数字符串：流程类型Id 2)0:不需指定流程类型 3)-1:非系统级流程类型
	 * @param businessName
	 *            业务名称查询条件
	 * @param userName
	 *            主办人名称
	 * @param startDate
	 *            任务开始时间
	 * @param endDate
	 *            任务结束时间
	 * @param isBackspace
	 *            是否是回退任务 “0”：非回退任务；“1”：回退任务；“2”：全部
	 * @return Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起,
	 *         (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型}
	 */
	@Transactional(readOnly = true)
	public Page getTodoWorks(Page page, String workflowType,
			String businessName, String userName, Date startDate, Date endDate,
			String isBackSpace, OALogInfo... infos) throws SystemException,
			ServiceException {
		try {
			User curUser = userService.getCurrentUser();// 获取当前用户
			// String searchType = "todo";
			String searchType = "all"; // dengzc 2010年6月28日9:42:34 待办和在办功能合并
			return workflow.getTasksTodo(page, curUser.getUserId(), searchType,
					workflowType, businessName, userName, startDate, endDate,
					isBackSpace);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取待办工作" });
		}
	}

	
	@Transactional(readOnly = true)
	public Page getTodoWorksOfAppointDept(Page page, String workflowType,
			String businessName, String userName, Date startDate, Date endDate,
			String isBackSpace,String state,String processTimeout,String deptId, OALogInfo... infos) throws SystemException,
			ServiceException {
		try {
			
			Object[] toSelectItems = { "taskId", "taskStartDate", "taskName","processInstanceId", "processStartDate", 
									   "processSuspend","businessName", "startUserName", "processTypeId","isBackspace",
									   "assignType","processMainFormBusinessId"};
			List sItems = Arrays.asList(toSelectItems);

			Map paramsMap = new HashMap<String, Object>();
			
			if (workflowType != null && !"".equals(workflowType)) {//流程类型
				//paramsMap.put("processTypeId", workflowType);
				String[] workflowTypes = workflowType.split(",");
				List<String> lType = new ArrayList<String>();
				for(String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			if (state != null && !"".equals(state)) {	//流程状态[未办结]
				paramsMap.put("processStatus", state);
			}
			if (processTimeout != null && !"".equals(processTimeout)) {	//流程状态[未办结]
				paramsMap.put("processTimeout", processTimeout);
			}
			
			List<User> users = userService.getUsersByOrgID(deptId);
			List<String> userStrs = new LinkedList<String>();
			for(User user:users){
				userStrs.add(user.getUserId());
			}
			if (null != userStrs) {
				paramsMap.put("handlerId", userStrs);
			}
			paramsMap.put("toAssignHandlerId", userService.getCurrentUser().getUserId());
			
			paramsMap.put("taskType", "2");
			if (null != businessName && !"".equals(businessName)) {
				paramsMap.put("businessName", businessName);
			}
			if (null != userName && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (null != startDate) {
				paramsMap.put("taskStartDateStart", startDate);
			}
			if (null != endDate) {
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
			} else {

			}

			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			try {
//				page = workflow.getTaskInfosByConditionForPage(page, sItems,
//						paramsMap, orderMap, null, null, "", null);
				
				// 过滤掉重复的已办记录
				List<Object[]> listWorkflow = workflow
						.getTaskInfosByConditionForList(sItems, paramsMap,
								orderMap, "", "", "", null);
				System.out.println("before === " + listWorkflow.size());
				List dlist = new ArrayList();// 存储非重复的任务
				List<String> checkList = new ArrayList<String>();// 处理重复的记录
				if (listWorkflow != null && !listWorkflow.isEmpty()) {
					for (int i = 0; i < listWorkflow.size(); i++) {
						Object[] objs = (Object[]) listWorkflow.get(i);
						String businessId = (String) objs[11];
						if (businessId == null) {
							businessId = this
									.getFormIdAndBussinessIdByTaskId(objs[0]
											.toString())[0];
						}

						if (!checkList.contains(businessId)) {
							dlist.add(objs);
							checkList.add(businessId);
						}
					}
				}
				System.out.println("after == " + dlist.size());
				page = ListUtils.splitList2Page(page, dlist);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取待办工作" });
		}
	}
	
	@Transactional(readOnly = true)
	public Page getTodoWorksOfAppointUser(Page page, String workflowType,
			String businessName, String userName, Date startDate, Date endDate,
			String isBackSpace, String state, String processTimeout,
			String userId, OALogInfo... infos)
			throws SystemException, ServiceException {
		try {

			Object[] toSelectItems = { "taskId", "taskStartDate", "taskName",
					"processInstanceId", "processStartDate", "processSuspend",
					"businessName", "startUserName", "processTypeId",
					"isBackspace", "assignType" ,"processMainFormBusinessId"};
			List sItems = Arrays.asList(toSelectItems);

			Map paramsMap = new HashMap<String, Object>();

			if (workflowType != null && !"".equals(workflowType)) {// 流程类型
				String[] workflowTypes = workflowType.split(",");
				List<String> lType = new ArrayList<String>();
				for(String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			if (state != null && !"".equals(state)) { // 流程状态[未办结]
				paramsMap.put("processStatus", state);
			}
			if (processTimeout != null && !"".equals(processTimeout)) { // 流程状态[未办结]
				paramsMap.put("processTimeout", processTimeout);
			}

			if (null != userId) {
				paramsMap.put("handlerId", userId);
			}
			paramsMap.put("toAssignHandlerId", userId);

			paramsMap.put("taskType", "2");
			if (null != businessName && !"".equals(businessName)) {
				paramsMap.put("businessName", businessName);
			}
			if (null != userName && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (null != startDate) {
				paramsMap.put("taskStartDateStart", startDate);
			}
			if (null != endDate) {
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
			} else {

			}

			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			try {
//				page = workflow.getTaskInfosByConditionForPage(page, sItems,
//						paramsMap, orderMap, null, null, "", null);
//				 过滤掉重复的已办记录
				List<Object[]> listWorkflow = workflow
						.getTaskInfosByConditionForList(sItems, paramsMap,
								orderMap, "", "", "", null);
				System.out.println("before === " + listWorkflow.size());
				List dlist = new ArrayList();// 存储非重复的任务
				List<String> checkList = new ArrayList<String>();// 处理重复的记录
				if (listWorkflow != null && !listWorkflow.isEmpty()) {
					for (int i = 0; i < listWorkflow.size(); i++) {
						Object[] objs = (Object[]) listWorkflow.get(i);
						String businessId = (String) objs[11];
						if (businessId == null) {
							businessId = this
									.getFormIdAndBussinessIdByTaskId(objs[0]
											.toString())[0];
						}

						if (!checkList.contains(businessId)) {
							dlist.add(objs);
							checkList.add(businessId);
						}
					}
				}
				System.out.println("after == " + dlist.size());
				page = ListUtils.splitList2Page(page, dlist);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取待办工作" });
		}
	}

	/**
	 * 得到当前用户相应类别的任务列表, 修改成挂起任务及指派委托状态也查找出来，在展现层展现状态
	 * 增加查询条件参数,添加流程类型参数processType，以区分发文、收文等
	 * 
	 * @param page
	 *            分页对象
	 * @param workflowType
	 *            流程类型参数 1)大于0的正整数字符串：流程类型Id 2)0:不需指定流程类型 3)-1:非系统级流程类型
	 * @param businessName
	 *            业务名称查询条件
	 * @param userName
	 *            主办人名称
	 * @param startDate
	 *            任务开始时间
	 * @param endDate
	 *            任务结束时间
	 * @param isBackspace
	 *            是否是回退任务 “0”：非回退任务；“1”：回退任务；“2”：全部
	 * @return Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起,
	 *         (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型}
	 */
	@Transactional(readOnly = true)
	public Page getDoingWorks(Page page, String workflowType,
			String businessName, String userName, Date startDate, Date endDate,
			String isBackSpace) throws SystemException, ServiceException {
		try {
			User curUser = userService.getCurrentUser();// 获取当前用户
			// String searchType = "doing";
			String searchType = "all"; // dengzc 2010年6月28日9:42:34 待办和在办功能合并
			return workflow.getTasksTodo(page, curUser.getUserId(), searchType,
					workflowType, businessName, userName, startDate, endDate,
					isBackSpace);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取在办工作" });
		}
	}

	/**
	 * 任务重新指派
	 * 
	 * @author 喻斌
	 * @date Apr 30, 2009 9:18:21 AM
	 * @param taskId
	 *            -任务实例Id
	 * @param reAssignActorId
	 *            -重新指派人员Id
	 * @param isNeedReturn
	 *            -指派是否需要返回
	 * @throws SystemException
	 */
	public void reAssignTask(String taskId, String reAssignActorId,
			String isNeedReturn) throws SystemException {
		workflow.assignTaskActor(taskId, reAssignActorId, isNeedReturn,
				userService.getCurrentUser().getUserId());
	}

	/**
	 * 任务指派返回
	 * 
	 * @author 喻斌
	 * @date Apr 30, 2009 11:47:02 AM
	 * @param taskId
	 *            -任务实例Id
	 * @param formId
	 *            -表单Id
	 * @param businessId
	 *            -业务数据Id
	 * @throws WorkflowException
	 */
	public void returnReAssignTask(String taskId, String newForm, long formId,
			String businessId) throws WorkflowException {
		workflow.returnReAssignTask(taskId, newForm, formId, businessId,
				userService.getCurrentUser().getUserId());
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
	public Page getProcessedWorks(Page page, String workflowType,
			String businessName, String userName, Date startDate, Date endDate,
			String state, String processTimeout, String noTotal,
			String appointUserId, OALogInfo... infos) throws SystemException,
			ServiceException {
		try {

			// User curUser = userService.getCurrentUser();// 获取当前用户
			/*
			 * List<String> toSelectItems = new ArrayList<String>();
			 * toSelectItems.add("taskId"); // 查询任务id
			 * toSelectItems.add("taskStartDate");
			 * toSelectItems.add("taskName");
			 * toSelectItems.add("processInstanceId");
			 * toSelectItems.add("processStartDate");
			 * toSelectItems.add("taskSuspend");
			 * toSelectItems.add("businessName");
			 * toSelectItems.add("startUserName");
			 * toSelectItems.add("assignHandlerName");
			 * toSelectItems.add("assignType");
			 * toSelectItems.add("processEndDate");
			 * 
			 * Map<String, Object> paramsMap = new HashMap<String, Object>();
			 * paramsMap.put("handlerId", curUser.getUserId());
			 * paramsMap.put("processTypeId", workflowType);
			 * paramsMap.put("toAssignHandlerId", curUser.getUserId()); if
			 * (businessName != null && !"".equals(businessName)) {
			 * paramsMap.put("businessName", businessName); } if (userName !=
			 * null && !"".equals(userName)) { paramsMap.put("startUserName",
			 * userName); } if (startDate != null) {
			 * paramsMap.put("taskStartDateStart", startDate); } if (endDate !=
			 * null) { paramsMap.put("taskStartDateEnd", endDate); }
			 */
			// paramsMap.put("assignType", "3");
			// page = workflow.getTaskInfosByConditionForPage(page,
			// toSelectItems, paramsMap, null, null, null, null, null);
			// page.setAutoCount(false);
			/*
			 * page = workflow.getTasksProcessed(page, curUser.getUserId(),
			 * workflowType, businessName, userName, startDate, endDate, state);
			 */

			Object[] toSelectItems = { "taskId", "taskStartDate", "taskName",
					"processInstanceId", "processStartDate", "processSuspend",
					"businessName", "startUserName", "processTypeId",
					"isBackspace", "assignType", "processEndDate",
					"processMainFormBusinessId" };
			List sItems = Arrays.asList(toSelectItems);

			Map<String, Object> paramsMap = new HashMap<String, Object>();

			User curUser = userService.getCurrentUser();// 获取当前用户

			Organization org = userService.getDepartmentByOrgId(curUser
					.getOrgId());

			String isOrg = org.getIsOrg();
			if (isOrg == null || isOrg.equals("")) {
				isOrg = "0";
			}

			String rest1 = curUser.getRest1();// 用户类型
			if (rest1 == null || rest1.equals("")) {
				rest1 = "0";
			}

			if (appointUserId == null || appointUserId.equals("")) {
				List<User> userlist = new LinkedList<User>();
				if (noTotal == null || "".equals(noTotal)) {// 如果noTotal为空或空字符串时
					List<String> handlerIds = new LinkedList<String>();
					if (rest1.equals("1") || isOrg.equals("1")) {// 该用户类型为厅领导或者该用户所处的部门是机构
						userlist = userService.getAllUserInfoByHa();// 得到当前用户所在机构下的用户列表
					} else if (rest1.equals("2")) { // 该用户类型为处领导
						userlist = userService.getUsersByOrgID(curUser
								.getOrgId());// 根据部门ID获取用户列表
					} else {// 该用户类型为个人
						userlist.add(curUser);
					}
					for (int i = 0; i < userlist.size(); i++) {
						handlerIds.add(userlist.get(i).getUserId());
					}
					paramsMap.put("handlerId", handlerIds);
				} else {
					paramsMap.put("handlerId", curUser.getUserId());
				}
			} else {
				paramsMap.put("handlerId", appointUserId);
			}

			if (workflowType != null && !"".equals(workflowType)) {
				String[] workflowTypes = workflowType.split(",");
				List<String> lType = new ArrayList<String>();
				for(String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			if (processTimeout != null && !"".equals(processTimeout)) {
				paramsMap.put("processTimeout", processTimeout);
			}
			paramsMap.put("toAssignHandlerId", curUser.getUserId());
			paramsMap.put("taskType", "3");
			if (null != businessName && !"".equals(businessName)) {
				paramsMap.put("businessName", "%"+businessName+"%");
			}
			if (userName != null && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (startDate != null) {
				paramsMap.put("taskStartDateStart", startDate);
			}
			if (endDate != null) {
				paramsMap.put("taskStartDateEnd", endDate);
			}

			if (state != null && !"".equals(state)) {
				paramsMap.put("processStatus", state);
			}
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			try {
				// 过滤掉重复的已办记录
				List<Object[]> listWorkflow = workflow
						.getTaskInfosByConditionForList(sItems, paramsMap,
								orderMap, "", "", "", null);
				System.out.println("before === " + listWorkflow.size());
				List dlist = new ArrayList();// 存储非重复的任务
				List<String> checkList = new ArrayList<String>();// 处理重复的记录
				if (listWorkflow != null && !listWorkflow.isEmpty()) {
					for (int i = 0; i < listWorkflow.size(); i++) {
						Object[] objs = (Object[]) listWorkflow.get(i);
						String businessId = (String) objs[12];
						if (businessId == null) {
							businessId = this
									.getFormIdAndBussinessIdByTaskId(objs[0]
											.toString())[0];
						}

						if (!checkList.contains(businessId)) {
							dlist.add(objs);
							checkList.add(businessId);
						}
					}
				}
				System.out.println("after == " + dlist.size());
				page = ListUtils.splitList2Page(page, dlist);

			} catch (Exception e) {
				e.printStackTrace();

			}
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取已办工作" });
		}

	}

	/**
	 * @description 获取指定处室的每个人的办文总数
	 * @author 严建
	 * @createTime Dec 2, 2011 1:11:10 PM
	 * @return List<Object[]> Object[]{人员id,人员名称，人员排序号，人员的办文总数，是否超期}
	 */
	public List<Object[]> getProcessedWorksTotalOfPerson(String deptId,
			String workflowType, String state, String processTimeout,
			OALogInfo... infos) throws SystemException, ServiceException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		if (workflowType != null && !"".equals(workflowType)) {// 流程类型
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for(String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
		if (state != null && !"".equals(state)) { // 流程状态[未办结]
			paramsMap.put("processStatus", state);
		}
		if (processTimeout != null && !"".equals(processTimeout)) { // 流程状态[未办结]
			paramsMap.put("processTimeout", processTimeout);
		}

		Object[] toSelectItems = { "processInstanceId", "processTimeout" };
		List sItems = Arrays.asList(toSelectItems);

		// 查询出未办结的流程
		List<Object[]> listWorkflow = workflow
				.getProcessInstanceByConditionForList(sItems, paramsMap, null);

		// 流程信息 processInstanceInfos<人员id######人员名称######人员排序号######是否超期>
		List<String> processInstanceInfos = new LinkedList<String>();
		for (int i = 0; i < listWorkflow.size(); i++) {
			Object[] processInstanceinfo = listWorkflow.get(i);
			if (processInstanceinfo[1] == null
					|| processInstanceinfo[1].toString().equals("")) {
				processInstanceinfo[1] = "0";
			}
			Object[] returnObjs = workflow
					.getProcessStatusByPiId(processInstanceinfo[0] + "");// 得到此流程实例下的运行情况
			Collection col = (Collection) returnObjs[6];// 处理任务信息
			if (col != null && !col.isEmpty()) {
				for (Iterator it = col.iterator(); it.hasNext();) {
					Object[] itObjs = (Object[]) it.next();
					String userId = (String) itObjs[3];
					if (userId != null && !"".equals(userId)) {
						String[] userIds = userId.split(",");
						Organization org = userService
								.getUserDepartmentByUserId(userIds[0]);
						User user = userService.getUserInfoByUserId(userIds[0]);
						if (org.getRest1() != null
								&& org.getRest1().equals("1")) {// 当机构为虚部门,不做处理
							continue;
						}
						if (org.getOrgId() != null
								&& deptId.equals(org.getOrgId())) {
							processInstanceInfos.add(userIds[0] + "######"
									+ user.getUserName() + "######"
									+ user.getUserSequence() + "######"
									+ processInstanceinfo[1]);
						} else {
							continue;
						}
					}
				}
			}
		}

		// 流程统计信息 processInstanceMap<流程信息，数量>
		Map<String, Long> processInstanceMap = new HashMap<String, Long>();
		String processInstanceInfo = null;
		for (int i = 0; i < processInstanceInfos.size(); i++) {
			processInstanceInfo = processInstanceInfos.get(i);
			if (!processInstanceMap.containsKey(processInstanceInfo)) {
				processInstanceMap.put(processInstanceInfo, new Long(1));
			} else {
				processInstanceMap.put(processInstanceInfo, processInstanceMap
						.get(processInstanceInfo) + 1);
			}
		}

		// returnObjects返回结果 List<Object[]>
		// Object[]{处室id,处室名称，处室排序号，处室的的办文总数，是否超期}
		List<Object[]> returnObjects = new LinkedList<Object[]>();

		Iterator<String> processInstanceInfoIterator = processInstanceMap
				.keySet().iterator();
		List<String> userIds = new LinkedList<String>();
		while (processInstanceInfoIterator.hasNext()) {
			processInstanceInfo = processInstanceInfoIterator.next();
			Long counter = processInstanceMap.get(processInstanceInfo);
			String[] processInstanceInfoUnits = processInstanceInfo
					.split("######");
			Object[] objs = new Object[5];
			objs[0] = processInstanceInfoUnits[0];
			objs[1] = processInstanceInfoUnits[1];
			objs[2] = processInstanceInfoUnits[2];
			objs[3] = counter;
			objs[4] = processInstanceInfoUnits[3];
			userIds.add(objs[0].toString());
			returnObjects.add(objs);
		}

		List<User> users = userService.getUsersByOrgID(deptId);
		for (User user : users) {
			if (!userIds.contains(user.getUserId())) {
				Object[] objs = new Object[5];
				objs[0] = user.getUserId();
				objs[1] = user.getUserName();
				objs[2] = user.getUserSequence();
				objs[3] = 0;
				objs[4] = "";
				returnObjects.add(objs);
			}
		}

		return returnObjects;
	}

	/**
	 * @description	返回未办结文的情况
	 * @author 严建
	 * @createTime Dec 6, 2011 1:15:46 PM
	 * @return int[] {(0)全部的文 ，(1)逾期的文}
	 */
	public int[] desktopShowTodoWorkTotal(String workflowType,String state, OALogInfo... infos) throws SystemException,
			ServiceException {
		
		int[] returnInt =  new int[2];
		int total = 0;
		int timeouttotal = 0;
		
		// 获取分管领导和部门之间的映射关系以及部门ID与部门信息的映射关系
		Map[] manageROrg = this.getManageMapOrgAndOrgMapManage();
		//manageMapOrg{分管领导ID：部门Id（多个部门id用","隔开）}
		Map manageMapOrg = manageROrg[0];
		String currentUserId = userService.getCurrentUser().getUserId();
		//当前用户所分管的机构的id
		String orgStrs = null;
		if(manageMapOrg.containsKey(currentUserId)){
			orgStrs = (String)manageMapOrg.get(currentUserId);
		}
		
		if(orgStrs != null){
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			
			if (workflowType != null && !"".equals(workflowType)) {//流程类型
				String[] workflowTypes = workflowType.split(",");
				List<String> lType = new ArrayList<String>();
				for(String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			if (state != null && !"".equals(state)) {	//流程状态[未办结]
				paramsMap.put("processStatus", state);
			}
			Object[] toSelectItems = null;
			toSelectItems = new Object[]{"processInstanceId","processTimeout"};
			List sItems = Arrays.asList(toSelectItems);
			
			//查询出未办结的流程
			List<Object[]> listWorkflow = workflow.getProcessInstanceByConditionForList(sItems, paramsMap, null);
			
			for(int i=0;i<listWorkflow.size();i++){
				Object[] processInstanceinfo = listWorkflow.get(i);
				if(processInstanceinfo[1]==null || processInstanceinfo[1].toString().equals("")){
					processInstanceinfo[1] = "0";
				}
				Object[] returnObjs = workflow.getProcessStatusByPiId(processInstanceinfo[0]+"");//得到此流程实例下的运行情况
				Collection col = (Collection)returnObjs[6];//处理任务信息
				if(col != null && !col.isEmpty()) {
					for(Iterator it = col.iterator();it.hasNext();) {
						Object[] itObjs = (Object[])it.next();
						String userId = (String)itObjs[3];
						if(userId != null && !"".equals(userId)) {
							String[] userIds = userId.split(",");
							Organization org = userService.getUserDepartmentByUserId(userIds[0]);
							if(org.getRest1()!= null && org.getRest1().equals("1")){//当机构为虚部门,不做处理
								continue;
							}
							if(orgStrs.indexOf(org.getOrgId()) == -1){//不是当前用户所分管的文
								continue;
							}
							total++;
							if(processInstanceinfo[1].equals("1")){
								timeouttotal++;
							}
						}
					}
				}
			}
		}
		returnInt[0] = total;
		returnInt[1] = timeouttotal;
		return returnInt;
		
	}
	
	
	/**
	 * @description 获取当前未办结的办文总数
	 * @author 严建
	 * @createTime Dec 2, 2011 11:34:36 AM
	 * @return List<Object[]>  Object[]{处室id,处室名称，处室排序号，处室的的办文总数}
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getProcessedWorksTotalOfDeptSecond(String workflowType,
			String state, String processTimeout, OALogInfo... infos) throws SystemException,
			ServiceException {
//		returnObjects返回结果 List<Object[]>  Object[]{处室id,处室名称，处室排序号，处室的的办文总数}
		List<Object[]> returnObjects = new LinkedList<Object[]>();
		
		
		// 获取分管领导和部门之间的映射关系以及部门ID与部门信息的映射关系
		Map[] manageROrg = this.getManageMapOrgAndOrgMapManage();
		//manageMapOrg{分管领导ID：部门Id（多个部门id用","隔开）}
		Map manageMapOrg = manageROrg[0];
		Map orgMapOrgBean = manageROrg[1];
		String currentUserId = userService.getCurrentUser().getUserId();
		//当前用户所分管的机构的id
		String orgStrs = null;
		//key：部门id，value：部门信息,初始化柱状图结构
		Map<String,Object[]> orgidMapOrgInfo = new HashMap<String, Object[]>();
		if(manageMapOrg.containsKey(currentUserId)){
			orgStrs = (String)manageMapOrg.get(currentUserId);
			String[] orgids = orgStrs.split(",");
			for(int i=0;i<orgids.length;i++){
				String orgid= orgids[i];
				Organization org = (Organization)orgMapOrgBean.get(orgid);
				Object[] objs = new Object[4];
				objs[0] = org.getOrgId();
				objs[1] = org.getOrgName();
				objs[2] = org.getOrgSequence();
				objs[3] = 0;
				orgidMapOrgInfo.put(orgid, objs);
			}
		}
		
		
		if(orgidMapOrgInfo != null && !orgidMapOrgInfo.isEmpty()){
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			
			if (workflowType != null && !"".equals(workflowType)) {//流程类型
				String[] workflowTypes = workflowType.split(",");
				List<String> lType = new ArrayList<String>();
				for(String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			if (state != null && !"".equals(state)) {	//流程状态[未办结]
				paramsMap.put("processStatus", state);
			}
			if (processTimeout != null && !"".equals(processTimeout)) {	//流程状态[未办结]
				paramsMap.put("processTimeout", processTimeout);
			}
			
			Object[] toSelectItems = new Object[]{"processInstanceId"};
			List sItems = Arrays.asList(toSelectItems);
			
			//查询出未办结的流程
			List<Object[]> listWorkflow = workflow.getProcessInstanceByConditionForList(sItems, paramsMap, null);
			
			for(int i=0;i<listWorkflow.size();i++){
				Object processInstanceId =listWorkflow.get(i);
				Object[] returnObjs = workflow.getProcessStatusByPiId(processInstanceId+"");//得到此流程实例下的运行情况
				Collection col = (Collection)returnObjs[6];//处理任务信息
				if(col != null && !col.isEmpty()) {
					for(Iterator it = col.iterator();it.hasNext();) {
						Object[] itObjs = (Object[])it.next();
						String userId = (String)itObjs[3];
						if(userId != null && !"".equals(userId)) {
							String[] userIds = userId.split(",");
							Organization org = userService.getUserDepartmentByUserId(userIds[0]);
							if(org.getRest1()!= null && org.getRest1().equals("1")){//当机构为虚部门,不做处理
								continue;
							}
							if(orgidMapOrgInfo.containsKey(org.getOrgId())){
								Object[] objs = orgidMapOrgInfo.get(org.getOrgId());
								objs[3] = Integer.parseInt(objs[3].toString())+1;
								orgidMapOrgInfo.put(org.getOrgId(), objs);
							}
						}
					}
				}
			}
			
			Iterator<String> orgids = orgidMapOrgInfo.keySet().iterator();
			String orgid = "";
			while(orgids.hasNext()){
				orgid = orgids.next();
				Object[] objs = orgidMapOrgInfo.get(orgid);
				returnObjects.add(objs);
			}
		}
		
		return returnObjects;
		
	}
	
	/**
	 * @description 获取分管领导和部门之间的映射关系以及部门ID与部门信息的映射关系
	 * @author 严建
	 * @createTime Dec 6, 2011 12:06:45 PM
	 * @return Map[] {(0)manageMapOrg  分管领导ID：部门Id[多个部门id用","隔开],
	 * 				  (1)orgMapOrgBean 部门ID：部门实体信息
	 * 				  }
	 */
	public Map[] getManageMapOrgAndOrgMapManage(){
		Map[] returnMap =  new Map[2];
		//manageMapOrg{分管领导ID：部门Id（多个部门id用","隔开）}
		Map<String,String> manageMapOrg =  new HashMap<String, String>();
		
		//orgMapOrgInfo{部门ID：部门信息}
		Map<String,Organization> orgMapOrgBean =  new HashMap<String, Organization>();

		List<Organization> orgs = userService.getCurrentUserOrgAndDept();
		Organization org = null;
		for(int i=0; i< orgs.size(); i++){
			org = orgs.get(i);
			if(org.getRest2() == null || org.getRest2().equals("")){//没设置分管领导的机构不处理
				
			}else{
				orgMapOrgBean.put(org.getOrgId(), org);
				String rest2 = "";
				for(int ii = 0; ii < org.getRest2().split(",").length; ii++){
					rest2 = org.getRest2().split(",")[ii];
					rest2 = rest2.substring(1);//rest2第一位是标识符“u”
					if(manageMapOrg.containsKey(rest2)){
						manageMapOrg.put(rest2, manageMapOrg.get(rest2)+","+org.getOrgId());
					}else{
						manageMapOrg.put(rest2, org.getOrgId());
					}
				}
			}
		}
		
		returnMap[0] = manageMapOrg;
		returnMap[1] = orgMapOrgBean;
		return returnMap;
	}
	
	/**
	 * @description 获取各处室的办文总数(未办结)
	 * @author 严建
	 * @createTime Dec 2, 2011 11:34:36 AM
	 * @return List<Object[]> Object[]{处室id,处室名称，处室排序号，是否超期，处室的的办文总数}
	 */
	public List<Object[]> getProcessedWorksTotalOfDept(String workflowType,
			String state, String processTimeout, OALogInfo... infos)
			throws SystemException, ServiceException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		if (workflowType != null && !"".equals(workflowType)) {// 流程类型
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for(String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
		if (state != null && !"".equals(state)) { // 流程状态[未办结]
			paramsMap.put("processStatus", state);
		}
		if (processTimeout != null && !"".equals(processTimeout)) { // 流程状态[未办结]
			paramsMap.put("processTimeout", processTimeout);
		}

		Object[] toSelectItems = { "processInstanceId", "processTimeout" };
		List sItems = Arrays.asList(toSelectItems);

		// 查询出未办结的流程
		List<Object[]> listWorkflow = workflow
				.getProcessInstanceByConditionForList(sItems, paramsMap, null);

		// 流程信息 processInstanceInfos<部门id######部门名称######部门排序号######是否超期>
		List<String> processInstanceInfos = new LinkedList<String>();
		for (int i = 0; i < listWorkflow.size(); i++) {
			Object[] processInstanceinfo = listWorkflow.get(i);
			if (processInstanceinfo[1] == null
					|| processInstanceinfo[1].toString().equals("")) {
				processInstanceinfo[1] = "0";
			}
			Object[] returnObjs = workflow
					.getProcessStatusByPiId(processInstanceinfo[0] + "");// 得到此流程实例下的运行情况
			Collection col = (Collection) returnObjs[6];// 处理任务信息
			if (col != null && !col.isEmpty()) {
				for (Iterator it = col.iterator(); it.hasNext();) {
					Object[] itObjs = (Object[]) it.next();
					String userId = (String) itObjs[3];
					if (userId != null && !"".equals(userId)) {
						String[] userIds = userId.split(",");
						Organization org = userService
								.getUserDepartmentByUserId(userIds[0]);
						if (org.getRest1() != null
								&& org.getRest1().equals("1")) {// 当机构为虚部门,不做处理
							continue;
						}
						processInstanceInfos.add(org.getOrgId() + "######"
								+ org.getOrgName() + "######"
								+ org.getOrgSequence() + "######"
								+ processInstanceinfo[1]);
					}
				}
			}
		}

		// 流程统计信息 processInstanceMap<流程信息，数量>
		Map<String, Long> processInstanceMap = new HashMap<String, Long>();
		String processInstanceInfo = null;
		for (int i = 0; i < processInstanceInfos.size(); i++) {
			processInstanceInfo = processInstanceInfos.get(i);
			if (!processInstanceMap.containsKey(processInstanceInfo)) {
				processInstanceMap.put(processInstanceInfo, new Long(1));
			} else {
				processInstanceMap.put(processInstanceInfo, processInstanceMap
						.get(processInstanceInfo) + 1);
			}
		}

		// returnObjects返回结果 List<Object[]> Object[]{处室id,处室名称，处室排序号，处室的的办文总数}
		List<Object[]> returnObjects = new LinkedList<Object[]>();
		List<String> orgIds = new LinkedList<String>();

		Iterator<String> processInstanceInfoIterator = processInstanceMap
				.keySet().iterator();
		while (processInstanceInfoIterator.hasNext()) {
			processInstanceInfo = processInstanceInfoIterator.next();
			Long counter = processInstanceMap.get(processInstanceInfo);
			String[] processInstanceInfoUnits = processInstanceInfo
					.split("######");
			Object[] objs = new Object[5];
			objs[0] = processInstanceInfoUnits[0];
			objs[1] = processInstanceInfoUnits[1];
			objs[2] = processInstanceInfoUnits[2];
			objs[3] = counter;
			objs[4] = processInstanceInfoUnits[3];
			orgIds.add(objs[0].toString());
			returnObjects.add(objs);
		}

		List<Organization> orgs = userService.getCurrentUserOrgAndDept();
		for (Organization org : orgs) {
			if (org.getRest1() != null && org.getRest1().equals("1")) {// 当机构为虚部门,不做处理
				continue;
			}
			if (!orgIds.contains(org.getOrgId())) {
				Object[] objs = new Object[5];
				objs[0] = org.getOrgId();
				objs[1] = org.getOrgName();
				objs[2] = org.getOrgSequence();
				objs[3] = 0;
				objs[4] = "";
				returnObjects.add(objs);
			}
		}

		return returnObjects;

	}

	/**
	 * @description 获取当前未办结的办文总数
	 * @author 严建
	 * @createTime Dec 2, 2011 11:34:36 AM
	 * @return List<Object[]> Object[]{处室id,处室名称，处室排序号，是否超期，处室的的办文总数}
	 */
	public List<Object[]> getProcessedWorksTotalOfDeptInfo(String workflowType,
			String state, String processTimeout, OALogInfo... infos)
			throws SystemException, ServiceException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		if (workflowType != null && !"".equals(workflowType)) {// 流程类型
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for(String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
		if (state != null && !"".equals(state)) { // 流程状态[未办结]
			paramsMap.put("processStatus", state);
		}
		if (processTimeout != null && !"".equals(processTimeout)) { // 流程状态[未办结]
			paramsMap.put("processTimeout", processTimeout);
		}
		paramsMap.put("processSuspend", false);

		Object[] toSelectItems = { "processInstanceId", "processTimeout" };
		List sItems = Arrays.asList(toSelectItems);

		// 查询出未办结的流程
		List<Object[]> listWorkflow = workflow
				.getProcessInstanceByConditionForList(sItems, paramsMap, null);

		// 流程信息 processInstanceInfos<部门id######部门名称######部门排序号######是否超期>
		List<String> processInstanceInfos = new LinkedList<String>();
		for (int i = 0; i < listWorkflow.size(); i++) {
			Object[] processInstanceinfo = listWorkflow.get(i);
			if (processInstanceinfo[1] == null
					|| processInstanceinfo[1].toString().equals("")) {
				processInstanceinfo[1] = "0";
			}
			Object[] returnObjs = workflow
					.getProcessStatusByPiId(processInstanceinfo[0] + "");// 得到此流程实例下的运行情况
			Collection col = (Collection) returnObjs[6];// 处理任务信息
			if (col != null && !col.isEmpty()) {
				for (Iterator it = col.iterator(); it.hasNext();) {
					Object[] itObjs = (Object[]) it.next();
					String userId = (String) itObjs[3];
					if (userId != null && !"".equals(userId)) {
						String[] userIds = userId.split(",");
						Organization org = userService
								.getUserDepartmentByUserId(userIds[0]);
						if (org.getRest1() != null
								&& org.getRest1().equals("1")) {// 当机构为虚部门,不做处理
							continue;
						}
						processInstanceInfos.add(org.getOrgId() + "######"
								+ org.getOrgName() + "######"
								+ org.getOrgSequence() + "######"
								+ processInstanceinfo[1]);
					}
				}
			}
		}

		// 流程统计信息 processInstanceMap<流程信息，数量>
		Map<String, Long> processInstanceMap = new HashMap<String, Long>();
		String processInstanceInfo = null;
		for (int i = 0; i < processInstanceInfos.size(); i++) {
			processInstanceInfo = processInstanceInfos.get(i);
			if (!processInstanceMap.containsKey(processInstanceInfo)) {
				processInstanceMap.put(processInstanceInfo, new Long(1));
			} else {
				processInstanceMap.put(processInstanceInfo, processInstanceMap
						.get(processInstanceInfo) + 1);
			}
		}

		// returnObjects返回结果 List<Object[]> Object[]{处室id,处室名称，处室排序号，处室的的办文总数}
		List<Object[]> returnObjects = new LinkedList<Object[]>();

		Iterator<String> processInstanceInfoIterator = processInstanceMap
				.keySet().iterator();
		while (processInstanceInfoIterator.hasNext()) {
			processInstanceInfo = processInstanceInfoIterator.next();
			Long counter = processInstanceMap.get(processInstanceInfo);
			String[] processInstanceInfoUnits = processInstanceInfo
					.split("######");
			Object[] objs = new Object[5];
			objs[0] = processInstanceInfoUnits[0];
			objs[1] = processInstanceInfoUnits[1];
			objs[2] = processInstanceInfoUnits[2];
			objs[3] = counter;
			objs[4] = processInstanceInfoUnits[3];
			returnObjects.add(objs);
		}

		return returnObjects;

	}

	/**
	 * 查询用户已办理任务总数
	 * 
	 * @param workflowType
	 *            流程类型
	 * @param state
	 *            流程状态
	 * @param processTimeout
	 *            流程是否超时(“0”：否；“1”：是)
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getProcessedWorks方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
	 */
	public int getProcessedWorksTotalCount(String workflowType, String state,
			String processTimeout, OALogInfo... infos) throws SystemException,
			ServiceException {
		try {

			// Object[] toSelectItems = { "taskId","processMainFormBusinessId"
			// };
			// List sItems = Arrays.asList(toSelectItems);
			//
			// Map<String, Object> paramsMap = new HashMap<String, Object>();
			//			
			// User curUser = userService.getCurrentUser();// 获取当前用户
			// List<String> handlerIds = new LinkedList<String>();
			// Organization org =
			// userService.getDepartmentByOrgId(curUser.getOrgId());
			//			
			//			
			// String isOrg = org.getIsOrg();
			// if(isOrg == null || isOrg.equals("")){
			// isOrg = "0";
			// }
			// String rest1 = curUser.getRest1();//用户类型
			// if(rest1 == null || rest1.equals("")){
			// rest1 = "0";
			// }
			// List<User> userlist = new LinkedList<User>();
			// if(rest1.equals("1") ||
			// isOrg.equals("1")){//该用户类型为厅领导或者该用户所处的部门是机构
			// List<String> orgidList = new LinkedList<String>();
			//				
			// List<Organization> orglist =
			// userService.getCurrentUserOrgAndDept();
			// for(int i=0; i<orglist.size();i++){
			// Organization organization = orglist.get(i);
			// if(organization.getRest1()!= null &&
			// organization.getRest1().equals("1")){//当机构为虚部门,不做处理
			// continue;
			// }else{
			// orgidList.add(organization.getOrgId());
			// }
			// }
			//				
			//				
			// userlist = userService.getAllUserInfoByHa();//得到当前用户所在机构下的用户列表
			// for(int i=0;i<userlist.size();i++){
			// User user = userlist.get(i);
			// if(!orgidList.contains(user.getOrgId())){
			// userlist.remove(user);
			// i--;
			// continue;
			// }
			// }
			//				
			// }else if(rest1.equals("2")){ //该用户类型为处领导
			// userlist =
			// userService.getUsersByOrgID(curUser.getOrgId());//根据部门ID获取用户列表
			// }else{//该用户类型为个人
			// userlist.add(curUser);
			// }
			// for(int i = 0; i<userlist.size(); i++){
			// handlerIds.add(userlist.get(i).getUserId());
			// }
			//			
			// paramsMap.put("startUserId",handlerIds );
			// if (workflowType != null && !"".equals(workflowType)) {
			// paramsMap.put("processTypeId", workflowType);
			// }
			// if (processTimeout != null && !"".equals(processTimeout)) {
			// paramsMap.put("processTimeout", processTimeout);
			// }
			// paramsMap.put("taskType", "3");
			// if (state != null && !"".equals(state)) {
			// paramsMap.put("processStatus", state);
			// }
			List dlist = new ArrayList();// 存储非重复的任务
			// try {
			//
			// // 过滤掉重复的已办记录
			// List<Object[]> listWorkflow = workflow
			// .getTaskInfosByConditionForList(sItems, paramsMap,
			// null, "", "", "", null);
			// List<String> checkList = new ArrayList<String>();// 处理重复的记录
			// if (listWorkflow != null && !listWorkflow.isEmpty()) {
			// for (int i = 0; i < listWorkflow.size(); i++) {
			// Object[] objs = (Object[]) listWorkflow.get(i);
			// String businessId = (String) objs[1];
			// if(businessId == null){
			// businessId =
			// this.getFormIdAndBussinessIdByTaskId(objs[0].toString())[0];
			// }
			// if (!checkList.contains(businessId)) {
			// dlist.add(objs);
			// checkList.add(businessId);
			// }
			// }
			// }
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			//
			// }

			return dlist.size();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取已办工作" });
		}

	}

	/**
	 * 获取当前用户主办的任务列表
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
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间 内嵌数据对象结构：com.strongit.workflow.bo.TwfInfoBuspdi
	 * 
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getHostedByDocs方法中添加 State 状态 查询参数 为已审公文和已办来文 添加状态栏
	 */
	@Transactional(readOnly = true)
	public Page getHostedByDocs(Page page, String workflowType,
			String businessName, Date startDate, Date endDate, String state,
			OALogInfo... infos) throws SystemException, ServiceException {
		try {
			User curUser = userService.getCurrentUser();// 获取当前用户
			page = workflow.getTasksHostedBy(page, curUser.getUserId(),
					workflowType, businessName, startDate, endDate, state);
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取主办工作" });
		}
	}

	/**
	 * 获取当前用户指派的任务列表
	 * 
	 * @param page
	 *            分页对象 [
	 *            <p>
	 *            Object[]{主键Id,流程实例Id,流程创建时间,流程当前状态,
	 *            </p>
	 *            <p>
	 *            业务名称,发起人Id,发起人名称,流程类型Id,流程实例结束时间,主办业务Id,表单Id}
	 *            </p>]
	 * @param username
	 *            人员名称
	 * @param workflowType
	 *            流程类型
	 * @return com.strongmvc.orm.hibernate.Page
	 * @param businessName
	 *            业务名称（标题）
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getAssignWorks(Page page, String workflowType,
			String businessName, String userName, Date startDate, Date endDate)
			throws SystemException, ServiceException {
		try {
			User curUser = userService.getCurrentUser();// 获取当前用户
			// page = workflow.getTasksHostedBy(page, curUser.getUserId(),
			// workflowType,businessName,startDate,endDate);

			Object[] toSelectItems = { "taskId", "processInstanceId",
					"processStartDate", "processSuspend", "businessName",
					"startUserId", "startUserName", "startUserName",
					"processEndDate", "processMainFormBusinessId",
					"taskFormId", "toAssignHandlerName" };
			List sItems = Arrays.asList(toSelectItems);

			Map paramsMap = new HashMap<String, Object>();
			paramsMap.put("assignHandlerId", curUser.getUserId());// assignHandlerId”：委托/指派人Id
			paramsMap.put("assignType", "1");// 委托类型(“0”：委托；“1”：指派；“2”：全部)

			if (null != workflowType && !"".equals(workflowType)) {
				String[] workflowTypes = workflowType.split(",");
				List<String> lType = new ArrayList<String>();
				for(String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			if (null != businessName && !"".equals(businessName)) {
				paramsMap.put("businessName", businessName);
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

			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			try {
				page = workflow.getTaskInfosByConditionForPage(page, sItems,
						paramsMap, orderMap, null, null, "", null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取当前用户指派的任务列表" });
		}
	}

	/**
	 * 获取当前用户委派的任务列表
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
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间 内嵌数据对象结构：com.strongit.workflow.bo.TwfInfoBuspdi
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page getEntrustWorks(Page page, String workflowType,
			String businessName, String userName, Date startDate, Date endDate)
			throws SystemException, ServiceException {
		try {
			User curUser = userService.getCurrentUser();// 获取当前用户
			// page = workflow.getTasksHostedBy(page, curUser.getUserId(),
			// workflowType,businessName,startDate,endDate);

			Object[] toSelectItems = { "taskId", "processInstanceId",
					"processStartDate", "processSuspend", "businessName",
					"startUserId", "startUserName", "startUserName",
					"processEndDate", "processMainFormBusinessId",
					"taskFormId", "toAssignHandlerName" };
			List sItems = Arrays.asList(toSelectItems);

			Map paramsMap = new HashMap<String, Object>();
			paramsMap.put("assignHandlerId", curUser.getUserId());// assignHandlerId”：委托/指派人Id
			paramsMap.put("assignType", "0");// 委托类型(“0”：委托；“1”：指派；“2”：全部)

			if (null != workflowType && !"".equals(workflowType)) {
				String[] workflowTypes = workflowType.split(",");
				List<String> lType = new ArrayList<String>();
				for(String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			if (null != businessName && !"".equals(businessName)) {
				paramsMap.put("businessName", businessName);
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

			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");

			try {
				page = workflow.getTaskInfosByConditionForPage(page, sItems,
						paramsMap, orderMap, null, null, "", null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取当前用户委派的任务列表" });
		}
	}

	/**
	 * 签收任务 增加签收时设置前置任务实例为不可取回（并行会签时只有最后一个任务实例可改变取回状态）
	 * 
	 * @param userId
	 * @param taskId
	 * @param flag
	 *            签收标志符，flag="0":签收并处理，需要挂起会签实例；flag="1":仅签收，不需挂起会签实例
	 * @throws WorkflowException
	 */
	public void signForTask(String taskId, String flag, OALogInfo... infos)
			throws SystemException, ServiceException {
		try {
			String userId = userService.getCurrentUser().getUserId();
			if(userId == null){
				userId = IUserService.SYSTEM_ACCOUNT;
			}
			workflow.signForTask(userId,taskId, flag);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "签收工作" });
		}
	}

	/**
	 * author:dengzc description:取回工作任务 modifyer: description:
	 * 
	 * @param taskId
	 * @return 1、流程实例已结束 0； 2、任务已被签收处理1； 3、任务取回成功2； 4、任务实例不存在返回-1 5、抛出异常返回-2
	 * @throws WorkflowException
	 */
	public String fetchTask(String taskId, OALogInfo... infos)
			throws WorkflowException {
		try {
//			任务取回前，判断一下当前流程环节是不是处于意见征询节点，是，返回一个包含数据的数组，否则返回null。
			Object[] info = docManager.fetchTaskbeforeIsAtYjzxNode(taskId);
			String ret = workflow.fetchTask(taskId);
			if("2".equals(ret)){
//				流程取回，判断是否取回意见征询节点任务，如果是则删除意见征询信息，否则不删除
				docManager.fetchTaskAndDeleteYjzxInfo(info,taskId);	
			}
			return ret;
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
	 * 获取节点信息，主要用于办公厅分发公文取回时更改分发单位信息
	 * @author: qibh
	 *@param taskId
	 *@param infos
	 *@return
	 *@throws WorkflowException
	 * @created: 2012-12-18 上午08:40:17
	 * @version :5.0
	 */
	public String gettaskInf(String taskId, OALogInfo... infos)
		throws WorkflowException {
		TaskInstance taskInstance = workflow.getTaskInstanceById(taskId);
		//ProcessInstance processInstance = workflow.getTaskInstanceById(taskId).getProcessInstance();
		return taskInstance.getNodeName();
	}
	/**
	 * 判断目前任务是否可被当前用户处理
	 * 
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	public String judgeTaskIsDone(String taskid) throws WorkflowException {
		return workflow.judgeTaskIsDone(taskid);
	}

	public Object[] getWorkflowMonitorData(String instanceId) {
		return workflow.getWorkflowMonitorData(instanceId);
	}

	/**
	 * added by yubin on 2008.09.05<br>
	 * <p>
	 * 判断该任务是否允许回退和驳回,指派和指派返回
	 * 
	 * @author 喻斌
	 * @date Feb 2, 2009 2:05:45 PM
	 * @param id
	 *            -任务实例Id
	 * @return String 是否允许回退和驳回,指派和指派返回<br>
	 *         <p>
	 *         回退|驳回|指派|指派返回
	 *         </p>
	 *         <p>
	 *         0：不允许；1：允许
	 *         </p>
	 * @throws WorkflowException
	 */
	public String checkCanReturn(String id) throws SystemException {
		return workflow.checkCanReturn(id);
	}

	/**
	 * 获取所有流程类型
	 * 
	 * @author:邓志城
	 * @date:2009-5-20 下午04:05:41
	 * @return
	 * @throws SystemException
	 * @see {工作处理->高级查询}
	 */
	@SuppressWarnings("unchecked")
	public List<WorkFlowBean> getAllWorkFlowInstanceWithWorkFlowType()
			throws SystemException {
		List<WorkFlowBean> beanLst = new ArrayList<WorkFlowBean>();
		List<Object[]> workflowLst = workflow.getAllProcessFilesList();
		for (int i = 0; i < workflowLst.size(); i++) {
			WorkFlowBean bean = new WorkFlowBean();
			Object[] obj = workflowLst.get(i);
			String workflowTypeId = obj[3].toString();// 流程类型ID
			String workflowTypeName = obj[4].toString();// 流程类型名称
			bean.setWorkFlowTypeId(workflowTypeId);
			bean.setWorkFlowTypeName(workflowTypeName);
			if (!isExistWorkflowType(beanLst, workflowTypeId)) {
				beanLst.add(bean);
			}
		}

		return beanLst;
	}

	/**
	 * 工作流高级查询实现
	 * 
	 * @author:邓志城
	 * @date:2009-5-26 上午10:35:21
	 * @param processName
	 *            流程名称
	 * @param processStatus
	 *            流程状态
	 * @param searchScope
	 *            查询范围
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param userId
	 *            发起人
	 * @param businessName
	 *            业务名称
	 * @param sql
	 *            通过表单组合的查询SQL语句
	 * @return 工作流查询列表
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public List<WorkFlowBean> getAdvancedSearchResult(String processName,
			String processStatus, String searchScope, Date startDate,
			Date endDate, String userId, String businessName, String sql)
			throws SystemException, SQLException {

		/**
		 * Object[]{(0)流程实例Id,(1)业务名称,(2)开始时间,(3)结束时间,(4)流程主表单Id,(5)流程主表单业务数据标识}
		 */
		List<Object[]> objLst = workflow.getProcessInfoByQueryCondition(
				processName, processStatus, searchScope, startDate, endDate, ""
						.equals(userId) ? null : userId, businessName);
		List<TempPo> lst = null;
		ResultSet rs = null;
		List<WorkFlowBean> workFlowBean = new ArrayList<WorkFlowBean>();
		if (sql != null && !"".equals(sql)) {
			Object[] objArgs = getSql(sql);
			String queryStr = objArgs[0].toString();
			LogPrintStackUtil.logInfo("SQL:" + queryStr);
			rs = executeJdbcQuery(queryStr);
			lst = ListUtils.result2List(rs);
			String businessField = null;
			String pkFieldValue = null;
			if (lst != null && lst.size() > 0) {
				for (TempPo po : lst) {
					String pk = po.getId();
					for (Object[] obj : objLst) {
						businessField = obj[5].toString();
						if (businessField.indexOf(";") != -1) {
							pkFieldValue = businessField.split(";")[2];
						} else {
							pkFieldValue = businessField;
						}
						if (pk.equals(pkFieldValue)) {
							WorkFlowBean bean = new WorkFlowBean();
							bean.setFlowNum(obj[0].toString());
							bean.setBussinessName(obj[1] == null ? "" : obj[1]
									.toString());
							String sdate = obj[2].toString();
							try {
								sdate = sdate.substring(0, 16);
							} catch (Exception e) {
								e.printStackTrace();
							}
							bean.setFlowStartDate(sdate);
							bean.setBusinessId(businessField);
							if (obj[3] == null) {
								bean
										.setFlowStatus("<font color='#90036'>执行中</font>&nbsp&nbsp");
							} else {
								bean
										.setFlowStatus("<font color='#63ad00'>已结束</font>&nbsp&nbsp");
							}
							workFlowBean.add(bean);
						}

					}
				}
			}
		} else {
			for (Object[] obj : objLst) {
				WorkFlowBean bean = new WorkFlowBean();
				bean.setFlowNum(obj[0].toString());
				bean.setBussinessName(obj[1] == null ? "" : obj[1].toString());
				String sdate = obj[2].toString();
				bean.setBusinessId(obj[5].toString());
				try {
					sdate = sdate.substring(0, 16);
				} catch (Exception e) {
					e.printStackTrace();
				}
				bean.setFlowStartDate(sdate);
				if (obj[3] == null) {
					bean
							.setFlowStatus("<font color='#90036'>执行中</font>&nbsp&nbsp");
				} else {
					bean
							.setFlowStatus("<font color='#63ad00'>已结束</font>&nbsp&nbsp");
				}
				workFlowBean.add(bean);
			}
		}
		return workFlowBean;

	}

	/**
	 * 获取解析后的SQL语句
	 * 
	 * @author:邓志城
	 * @date:2009-6-1 下午04:49:22
	 * @param queryStr
	 *            查询语句
	 * @return {解析后的SQL语句,参数}
	 * @throws SystemException
	 */
	private Object[] getSql(String queryStr) throws SystemException {
		StringBuffer sql = new StringBuffer(queryStr);
		String dataBaseType = "oracle";
		try {
			String jdbcUrl = PropertiesUtil.getProperty("jdbc.url",
					"appconfig.properties");
			if (jdbcUrl.indexOf("oracle") != -1) {
				dataBaseType = "oracle";
			} else if (jdbcUrl.indexOf("mysql") != -1) {
				dataBaseType = "mysql";
			} else if (jdbcUrl.indexOf("microsoft") != -1) {
				dataBaseType = "microsoft";
			} else {
				throw new SystemException("不支持的数据库类型！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println("读取配置文件“appconfig.properties”文件失败！");
		}
		Object[] obj = new Object[2];
		List<Object> params = new ArrayList<Object>();// 保存所有字段值
		while (sql.indexOf("{") != -1) {
			// 获取SQL语句里的字段{格式:【操作符|字段值|字段绑定控件所属类型】}
			String str = sql.substring(sql.indexOf("{") + 1, sql.indexOf("}"));
			String[] strs = str.split("\\|");
			// 操作符
			String opt = strs[0];
			// 字段值
			String value = strs[1];
			// 字段所属控件类型
			String type = strs[2];
			if (Constants.BAOHAN.equals(opt)) {
				str = "like '%" + value + "%'";
			} else if (Constants.BUDENGYU.equals(opt)) {
				if (Constants.DATEPICKER.equals(type)) {
					if ("oracle".equals(dataBaseType)) {
						str = "<>'" + value + "'";
					} else if ("microsoft".equals(dataBaseType)) {
						str = "<>'" + value + "'";
					} else {// mysql
						str = "<>'" + value + "'";
					}
				} else {
					str = "<>'" + value + "'";
				}
			} else if (Constants.DAYU.equals(opt)) {
				if (Constants.DATEPICKER.equals(type)) {
					// Oracle语法
					if ("oracle".equals(dataBaseType)) {
						// str = ">to_date('"+value+"','yyyy-MM-dd
						// HH24:mi:ss')";
						str = ">'" + value + "'";
					} else if ("microsoft".equals(dataBaseType)) {
						str = ">'" + value + "'";
					} else {// mysql
						str = ">'" + value + "'";
					}
				} else {
					LogPrintStackUtil.logInfo("非日期域不支持“大于”查询！");
				}
			} else if (Constants.DAYUDENGYU.equals(opt)) {
				if (Constants.DATEPICKER.equals(type)) {
					// Oracle语法
					if ("oracle".equals(dataBaseType)) {
						str = ">='" + value + "'";
					} else if ("microsoft".equals(dataBaseType)) {
						str = ">='" + value + "'";
					} else {
						str = ">='" + value + "'";
					}
				} else {
					LogPrintStackUtil.logInfo("非日期域不支持“大于等于”查询！");
				}
			} else if (Constants.DENGYU.equals(opt)) {
				if (Constants.DATEPICKER.equals(type)) {
					// Oracle语法
					if ("oracle".equals(dataBaseType)) {
						str = "='" + value + "'";
					} else if ("microsoft".equals(dataBaseType)) {
						str = "='" + value + "'";
					} else {
						str = "='" + value + "'";
					}

				} else {
					str = "='" + value + "'";
				}
			} else if (Constants.JIESHU.equals(opt)) {
				str = "like '%" + value + "'";
			} else if (Constants.KAISHI.equals(opt)) {
				str = "like '" + value + "%'";
			} else if (Constants.XIAOYU.equals(opt)) {
				if (Constants.DATEPICKER.equals(type)) {
					if ("oracle".equals(dataBaseType)) {
						str = "<'" + value + "'";
					} else if ("microsoft".equals(dataBaseType)) {
						str = "<'" + value + "'";
					} else {
						str = "<'" + value + "'";
					}
				} else {
					LogPrintStackUtil.logInfo("非日期域不支持“小于”查询！");
				}
			} else if (Constants.XIAOYUDENGYU.equals(opt)) {
				if (Constants.DATEPICKER.equals(type)) {
					if ("oracle".equals(dataBaseType)) {
						str = "<='" + value + "'";
					} else if ("microsoft".equals(dataBaseType)) {
						str = "<='" + value + "'";
					} else {
						str = "<='" + value + "'";
					}
				} else {
					LogPrintStackUtil.logInfo("非日期域不支持“小于等于”查询！");
				}
			}
			sql.replace(sql.indexOf("{"), sql.indexOf("}") + 1, str);
		}
		obj[0] = sql.toString();
		obj[1] = params;
		return obj;
	}

	/**
	 * 获取所有流程文件（不区分版本号）
	 * 
	 * @author:邓志城
	 * @date:2009-5-21 下午03:46:00
	 * @return
	 * @throws SystemException
	 * @see 【工作查询-显示所有流程定义】
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllWorkflowTypeLst() throws SystemException {
		List<String> beanLst = new ArrayList<String>();
		List<WorkFlowBean> workflowTypeLst = getAllWorkFlowInstanceWithWorkFlowType();
		List<Object[]> workflowLst = workflow.getAllProcessFilesList();
		for (WorkFlowBean type : workflowTypeLst) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < workflowLst.size(); i++) {
				Object[] obj = workflowLst.get(i);
				String workflowName = obj[1].toString();// 流程名称
				String workflowTypeId = obj[3].toString();// 流程类型ID
				if (type.getWorkFlowTypeId().equals(workflowTypeId)) {
					if (!map.containsValue(workflowName)) {// 一个流程定义下可能会有多个版本的流程,这里需要过滤掉不同版本，流程名一样的流程
						beanLst.add(workflowName);
						map.put(workflowTypeId + i, workflowName);
					}
				}
			}
		}
		return beanLst;
	}

	/**
	 * 获取某类型下的所有流程文件（不区分版本号）
	 * 
	 * @author:邓志城
	 * @date:2009年5月26日14:59:46
	 * @return
	 * @throws SystemException
	 * @see 【蒋国斌-->工作查询-显示某类型下的所有流程定义】
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllWorkflowTypeLst(String flowTypeId)
			throws SystemException {
		List<String> beanLst = new ArrayList<String>();
		List<WorkFlowBean> workflowTypeLst = getAllWorkFlowInstanceWithWorkFlowType();
		List<Object[]> workflowLst = workflow.getAllProcessFilesList();
		for (WorkFlowBean type : workflowTypeLst) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < workflowLst.size(); i++) {
				Object[] obj = workflowLst.get(i);
				String workflowName = obj[1].toString();// 流程名称
				String workflowTypeId = obj[3].toString();// 流程类型ID
				if (type.getWorkFlowTypeId().equals(workflowTypeId)) {
					if (!map.containsValue(workflowName)) {// 一个流程定义下可能会有多个版本的流程,这里需要过滤掉不同版本，流程名一样的流程
						if (workflowTypeId.equals(flowTypeId)) {
							beanLst.add(workflowName);
							map.put(workflowTypeId + i, workflowName);
						}
					}
				}
			}
		}
		return beanLst;
	}

	/**
	 * 获取流程类别下的所有流程定义
	 * 
	 * @author:邓志城
	 * @date:2009-5-21 上午11:23:28
	 * @param workFlowTypeId
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String getWorkFlowInfoByWorkFlowTypeId(HttpServletRequest reqeust,
			String workFlowTypeId) throws SystemException,
			UnsupportedEncodingException {
		List<Object[]> workflowLst = workflow.getAllProcessFilesList();
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer info = new StringBuffer("");
		for (int i = 0; i < workflowLst.size(); i++) {
			Object[] obj = workflowLst.get(i);
			String workflowName = obj[1].toString();// 流程名称
			String workflowTypeId = obj[3].toString();// 流程类型ID
			if (workFlowTypeId.equals(workflowTypeId)) {
				if (!map.containsValue(workflowName)) {// 一个流程定义下可能会有多个版本的流程,这里需要过滤掉不同版本，流程名一样的流程
					info
							.append("<ul><li>")
							.append(
									"<a href=\""
											+ reqeust.getContextPath()
											+ "/work/work!toAdvanceSearchPage.action?workflowId="
											+ obj[0].toString()
											+ "&workflowName=").append(
									URLEncoder.encode(URLEncoder.encode(
											workflowName, "utf-8"), "utf-8"))
							.append("\">").append("<span>")
							.append(workflowName).append("</span>").append(
									"</a>");
					info.append("</ul></li>");
					map.put(workFlowTypeId + i, workflowName);
				}
			}
		}

		return info.toString();
	}

	@Transactional(readOnly = true)
	public Page getProcessInfoForPage(Page page, String processType,
			String processStatus, String searchScope, Date startDate,
			Date endDate, String userId, String businessName)
			throws SystemException, ServiceException {
		try {
			// User curUser = user.getCurrentUser();//获取当前用户
			if ("".equals(processType)) {
				processType = null;
			}
			if ("".equals(userId)) {
				userId = null;
			}
			if ("".equals(businessName)) {
				businessName = null;
			}

			if ("".equals(startDate)) {
				startDate = null;
			}
			if ("".equals(endDate)) {
				endDate = null;
			}
			if ("".equals(processStatus)) {
				processStatus = null;
			}
			if ("".equals(searchScope)) {
				searchScope = null;
			}

			page = workflow.getProcessInfoForPageByQueryCondition(page,
					processType, processStatus, searchScope, startDate,
					endDate, userId, businessName);
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取工作流信息" });
		}

	}

	/**
	 * 判断是否已经在列表中添加了流程类别
	 * 
	 * @author:邓志城
	 * @date:2009-5-21 上午10:46:22
	 * @param lst
	 * @param workFlowTypeId
	 * @return
	 * @throws Exception
	 */
	private boolean isExistWorkflowType(List<WorkFlowBean> lst,
			String workFlowTypeId) throws SystemException {
		for (WorkFlowBean bean : lst) {
			if (bean.getWorkFlowTypeId().equals(workFlowTypeId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据流程定义ID获取流程定义文件信息
	 * 
	 * @author:邓志城
	 * @date:2009-5-22 上午10:30:01
	 * @param pid
	 * @return
	 * @throws WorkflowException
	 */
	public TwfBaseProcessfile getProcessfileById(String pid)
			throws WorkflowException {
		return workflow.getProcessfileById(pid);
	}

	/**
	 * 得到给定Id用户的符合查询条件的委托（指派）任务信息或被委托（指派）任务信息分页列表
	 * 
	 * @author:邓志城
	 * @date:2010-1-27 下午03:52:58
	 * @param page
	 *            分页列表
	 * @param userId
	 *            用户Id
	 * @param taskType
	 *            搜索任务类型 “0”：待办；“1”：在办；“2”：非办结；“3”：办结；“4”：全部
	 * @param assignType
	 *            委托类型 “0”：委托；“1”：指派；“2”：全部
	 * @param initiativeType
	 *            委托或被委托类型 “0”：查询委托给其他人的任务列表；“1”：查询其他人委托给该用户的任务列表
	 * @param processType
	 *            流程类型参数 大于“0”的正整数字符串：流程类型Id；“0”:不需指定流程类型；“-1”:非系统级流程类型
	 * @param businessName
	 *            业务名称查询条件 为null则不查询
	 * @param userName
	 *            主办人名称查询条件 为null则不查询
	 * @param startDate
	 *            任务开始时间上限查询条件 为null则不查询
	 * @param endDate
	 *            任务开始时间下限查询条件 为null则不查询
	 * @return Page 任务分页列表 数据结构：
	 *         Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起,
	 *         (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)任务委托人或被委托人名称,(10)任务委托类型}
	 *         其中：任务委托类型为“0”表示委托，为“1”表示指派
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public com.strongmvc.orm.hibernate.Page getAssignTaskInfoByUserId(
			com.strongmvc.orm.hibernate.Page page, java.lang.String userId,
			java.lang.String taskType, java.lang.String assignType,
			java.lang.String initiativeType, java.lang.String processType,
			java.lang.String businessName, java.lang.String userName,
			java.util.Date startDate, java.util.Date endDate)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflow.getAssignTaskInfoByUserId(page, userId, taskType,
				assignType, initiativeType, processType, businessName,
				userName, startDate, endDate);
	}

	/**
	 * 得到任务绑定的控件对应的输入意见
	 * 
	 * @author:邓志城
	 * @date:2009-12-19 下午04:53:01
	 * @param taskInstanceId
	 *            任务实例id
	 * @param instanceId
	 *            流程实例id
	 * @return
	 */
	public String getInstro(String taskInstanceId, String instanceId)
			throws Exception {
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
				flag.put("fieldName", fbj.getString("fieldName"));
			}
			flag.put("otherField", jsonArray.toString());// 其他字段的绑定信息
			return flag.toString();
		}
		return null;
	}

	/**
	 * 根据流程实例得到JSON格式审批意见
	 * 
	 * @author:邓志城
	 * @date:2010-11-17 上午11:35:10
	 * @param processInstanceId
	 * @return
	 */
	private JSONArray getJSONArray(String processInstanceId,
			ToaSystemset toaSystemSet) throws Exception {
		List<Object[]> list = workflow
				.getBusiFlagByProcessInstanceId(processInstanceId);
		JSONArray jsons = new JSONArray();// 存储所有返回到前台页面的信息:JSON格式
		if (list == null) {
			return jsons;
		}
		String temp = " ";
		// String isShow = toaSystemSet.getSuggestionShowName();
		String showDateFormat = toaSystemSet.getShowDateFormat();
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = list.get(i);
			String time = (String) ConvertUtils.convert(objs[7], String.class);
			if (time != null) {
				if (showDateFormat != null && !showDateFormat.equals("")) {

					time = time.substring(0, Integer.parseInt(showDateFormat));
				} else {
					time = time.substring(0, 10);
				}

			}
			JSONObject json = new JSONObject();
			if (objs[8] == null) {
				continue;
			}
			
			TwfBaseNodesetting tn = (TwfBaseNodesetting) objs[8];
			
			String info = nodesettingPluginService.getNodesettingPluginValue(tn, "plugins_businessFlag");// 得到特殊业务字段

			// 设置隐藏退回意见，hecj 2011-9-1 11:53:00
			String showsuggest = nodesettingPluginService.getNodesettingPluginValue(tn, "plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
			if ("1".equals(showsuggest)) {
			} else {
				String backSuggestion = "";
				String lastchar = "";
				if (objs[4] != null) {
					backSuggestion = objs[4].toString();
					if (backSuggestion.length() >= 5) {
						lastchar = backSuggestion.substring(backSuggestion
								.length() - 5);
						if ("     ".equals(lastchar)) {
							continue;
						}
					}
				}
			}

			if (info == null || "".equals(info)) {
				continue;
			}
			info = URLDecoder.decode(info, "utf-8");
			info = info.replaceAll("#", ",");
			JSONArray array = JSONArray.fromObject(info);
			JSONObject firstObj = array.getJSONObject(0);
			if (firstObj.containsKey("type")) {// 输入意见字段
				json.put("fieldName", firstObj.get("fieldName"));

				if (objs[4] == null || "null".equals(objs[4])
						|| "".equals(objs[4])) {
					temp = " ";
				} else {
					temp = objs[4].toString();
				}

				// 处理意见
				String infomation = "";

				// modify yanjian 2011-10-25 统一不显示节点名 bug-2516
				infomation = " " + temp + "\n " + objs[6] + "  " + time
						+ "\n\n";

				// if ("0".equals(isShow)) {
				// // 不显示节点名
				// infomation = " " + temp + "\n " + objs[6] + " " + time
				// + "\n\n";
				// } else {
				// // 显示节点名
				// infomation = " [" + tn.getNsNodeName() + "]:" + temp
				// + "\n " + objs[6] + " " + time + "\n\n";
				// }
				json.put("infomation", infomation);

				// 增加保存处理人id 用于人员排序功能 2010年11月17日16:15:05
				json.put("userId", (String) objs[5]);
				json.put("taskStartDate", objs[2]);// 任务开始时间,排序用到
				json.put("taskTime", objs[7]);// 任务处理时间,排序用到
				jsons.add(json);
			}
		}
		return jsons;
	}

	/**
	 * 根据流程实例id得到挂接在节点上的表单意见域设置信息
	 * 
	 * @author:邓志城
	 * @date:2010-3-8 下午04:24:11
	 * @param processInstanceId
	 *            流程实例id
	 * @return 节点设置信息 JSON格式。 Object[]{(0)任务实例Id,(1)任务名称 (2)开始时间 (3)结束时间
	 *         (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象}
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getBusiFlagByProcessInstanceId(String processInstanceId)
			throws Exception {
		ToaSystemset toaSystemSet = systemsetManager.getSystemset();
		JSONArray jsons = this.getJSONArray(processInstanceId, toaSystemSet);
		// 这里需要增加父、子流程意见
		List childrenInstanceIds = workflow
				.getMonitorChildrenInstanceIds(new Long(processInstanceId));
		if (childrenInstanceIds != null && !childrenInstanceIds.isEmpty()) {// 找到子流程
			for (int i = 0; i < childrenInstanceIds.size(); i++) {
				Object[] objs = (Object[]) childrenInstanceIds.get(i);
				String id = String.valueOf(objs[0]);
				JSONArray parentJsonArray = this.getJSONArray(id, toaSystemSet);
				for (int j = 0; j < parentJsonArray.size(); j++) {
					jsons.add(parentJsonArray.getJSONObject(j));
				}
			}
		}

		List parentInstanceIds = workflow.getMonitorParentInstanceIds(new Long(
				processInstanceId));
		if (parentInstanceIds != null && !parentInstanceIds.isEmpty()) {
			for (int i = 0; i < parentInstanceIds.size(); i++) {
				Object[] objs = (Object[]) parentInstanceIds.get(i);
				String id = String.valueOf(objs[0]);
				JSONArray parentJsonArray = this.getJSONArray(id, toaSystemSet);
				for (int j = 0; j < parentJsonArray.size(); j++) {
					jsons.add(parentJsonArray.getJSONObject(j));
				}
			}
		}
		jsons = this.getJSONObject(jsons, processInstanceId);
		return jsons.toString();
	}

	/**
	 * 根据workflowName得到挂接在节点上的表单设置信息
	 * 
	 * @description
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @throws Exception
	 * @createTime May 31, 2012 4:07:09 PM
	 */
	public String getBusiFlagByWorkflowName(String workflowName)
			throws Exception {
		JSONObject flag = new JSONObject();// 存储当前节点对应的表单域设置信息
		Object[] cunrrentNodeInfo = workflow
				.getFirstNodeBusiFlagByWorkflowName(workflowName);
		String strFlag = (String) cunrrentNodeInfo[3];// 得到当前节点设置的域信息
		if (strFlag != null) {
			strFlag = URLDecoder.decode(strFlag, "utf-8");
			strFlag = strFlag.replaceAll("#", ",");
			JSONArray jsonArray = JSONArray.fromObject(strFlag);
			JSONObject fbj = jsonArray.getJSONObject(0);
			if (fbj.containsKey("type")) {// 输入意见字段
				jsonArray.remove(fbj);
				flag.put("fieldName", fbj.getString("fieldName"));
			}
			flag.put("otherField", jsonArray.toString());// 其他字段的绑定信息
			return flag.toString();
		}
		return null;
	}
	/**
	 * 对于同一节点意见累加
	 * 
	 * @author 严建
	 * @createTime Aug 13, 2011
	 * @description
	 */
	private JSONArray getJSONObject(JSONArray array, String instanceId) throws Exception{
		// 根据流程实例查找流程的处理意见的排序方式 added by dengzc 2011年2月16日15:16:48
		try {
            String definitionId = workflow
                    .getProcessFileIdByProcessInstanceId(instanceId);
            String sortValue = null;
            if (definitionId != null) {
                List<ToaDefinitionPlugin> plugins = definitionPluginService
                        .find(definitionId);
                if (plugins != null && !plugins.isEmpty()) {
                    for (ToaDefinitionPlugin plugin : plugins) {
                        if ("plugins_suggestion".equals(plugin
                                .getDefinitionPluginName())) {
                            sortValue = plugin.getDefinitionPluginValue();// 得到排序方式
                            break;
                        }
                    }
                }
            } else {
                logger.error("未找到流程实例为'" + instanceId + "'的流程。");
            }
            logger.info("流程处理意见排序方式：" + sortValue);
            Map<String, List<String[]>> info = new HashMap<String, List<String[]>>();
            final Map<String, Long> userMap = new HashMap<String, Long>();
            JSONArray newArray = new JSONArray();
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String key = obj.getString("fieldName");
                if (key != null) {
                    String value = obj.getString("infomation");
                    String userId = obj.getString("userId");// 得到人员id
                    String orgId = null;
                    Long orgSequence = null;
                    String orgName = null;
                    if (userId != null) {
                        User user = userService.getUserInfoByUserId(userId);
                        orgId = user.getOrgId();
                        if (!userMap.containsKey(userId)) {
                            if (user != null) {
                                userMap.put(userId, user.getUserSequence());
                            }
                        }
                        if (sortValue!=null && !"".equals(sortValue)&& !"null".equals(sortValue)&& sortValue.startsWith("department")) {// -start如果要按部门排序时，执行
                            Organization organization = userService
                                    .getUserDepartmentByUserId(userId);
                            orgSequence = organization.getOrgSequence();
                            orgName = organization.getOrgName();
                        }// -end
                    }
                    Object taskTimeJSONObj = obj.get("taskTime");
                    String time = Long.toString(System.currentTimeMillis());
                    if (taskTimeJSONObj instanceof JSONObject) {
                        JSONObject jsonTaskTime = (JSONObject) taskTimeJSONObj;
                        time = jsonTaskTime.getString("time");

                    }
                    String[] valueStringArray = null;
                    Object taskStartDateJson = obj.get("taskStartDate");
                    String taskStartDatetime = Long.toString(System.currentTimeMillis());
                    if (taskStartDateJson instanceof JSONObject) {
                        JSONObject jsonTaskTime = (JSONObject) taskStartDateJson;
                        taskStartDatetime = jsonTaskTime.getString("time");

                    }
                    if (sortValue!=null && !"".equals(sortValue)&& !"null".equals(sortValue)&& sortValue.startsWith("department")) {
                        valueStringArray = new String[] { value, userId, time,
                                orgId, orgSequence.toString(), orgName,taskStartDatetime };
                    } else {
                        valueStringArray = new String[] { value, userId, time,taskStartDatetime };
                    }
                    if (info.containsKey(key)) {
                        info.get(key).add(valueStringArray);
                    } else {
                        List<String[]> valueList = new ArrayList<String[]>();
                        valueList.add(valueStringArray);
                        info.put(key, valueList);
                    }
                }
            }
            Set<String> keySet = info.keySet();
            for (String key : keySet) {
                List<String[]> valueList = info.get(key);
                if (valueList == null) {
                    continue;
                }
                if (sortValue == null) {// 默认按人员排序号顺序排序-按人员排序号进行排序
                    Collections.sort(valueList, new PersonComparator<String[]>(
                            SortConst.SORT_ASC, 1, userMap));
                } else {
                    if (sortValue.startsWith("departmentnumber")) {
                        List<List<String[]>> groupByList = null;
                        List<String[]> results = null;
                        BaseComparator<String[]> comparator = null;
                        int sortType = -1;
                        if (sortValue.startsWith("departmentnumber_asc_and")) {
                            sortType = SortConst.SORT_ASC;
                        } else {
                            sortType = SortConst.SORT_DESC;
                        }
                        groupByList = new GroupByDepartment(valueList, sortType)
                                .groupBy();
                        if (sortValue.endsWith("asc")) {
                            sortType = SortConst.SORT_ASC;
                        } else {
                            sortType = SortConst.SORT_DESC;
                        }
                        if (sortValue.indexOf("personnumber") != -1) {
                            comparator = new PersonComparator<String[]>(
                                    SortConst.SORT_ASC, 1, userMap);
                        } else if (sortValue.indexOf("date") != -1) {
                            comparator = new DateComparator<String[]>(sortType,
                                    2);
                        } else if (sortValue.indexOf("_taskstart_") != -1) {// 按任务开始顺序排序
                            comparator = new DateComparator<String[]>(sortType,
                                    6);
                        }
                        results = new GroupByAfterSorted(groupByList,
                                comparator).getSortedResult();
                        valueList = results;
                    } else {
                        if (sortValue.equals(SortConst.SORT_TYPE_DATE_DESC)) {// 按日期降序
                            Collections.sort(valueList,
                                    new DateComparator<String[]>(
                                            SortConst.SORT_DESC, 2));
                        } else if (sortValue
                                .equals(SortConst.SORT_TYPE_DATE_ASC)) {// 按日期升序
                            Collections.sort(valueList,
                                    new DateComparator<String[]>(
                                            SortConst.SORT_ASC, 2));
                        } else if (sortValue
                                .equals(SortConst.SORT_TYPE_PERSON_ASC)) {// 按人员排序号升序
                            Collections.sort(valueList,
                                    new PersonComparator<String[]>(
                                            SortConst.SORT_ASC, 1, userMap));
                        } else if (sortValue
                                .equals(SortConst.SORT_TYPE_PERSON_DESC)) {// 按人员排序号降序
                            Collections.sort(valueList,
                                    new PersonComparator<String[]>(
                                            SortConst.SORT_DESC, 1, userMap));
                        }
                    }

                }
                StringBuilder valueStr = new StringBuilder();
                /**
                 * 意见域显示所在部门 yanjian 20110906 11:30
                 */
                List<String> orgNameList = new ArrayList<String>();
                String orgNameTemp = null;
                for (int j = 0; j < valueList.size(); j++) {
                    if (sortValue!=null && !"".equals(sortValue)&& !"null".equals(sortValue)&& sortValue.startsWith("departmentnumber")) {
                        orgNameTemp = valueList.get(j)[5];
                        if (!orgNameList.contains(orgNameTemp)) {
                            valueStr.append("[" + orgNameTemp + "]\r\n");
                            orgNameList.add(orgNameTemp);
                        }
                        valueStr.append(valueList.get(j)[0].toString());
                    } else {
                        valueStr.append(valueList.get(j)[0].toString());
                    }
                }
                orgNameTemp = null;
                orgNameList.clear();
                // for (int j = 0; j < valueList.size(); j++) {
                // valueStr.append(valueList.get(j)[0].toString());
                // }
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("fieldName", key);
                jsonObj.put("infomation", valueStr.toString());
                newArray.add(jsonObj);
            }
            info.clear();
            userMap.clear();
            return newArray;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(e);
        }
	}

	/**
	 * 根据任务实例id得到节点设置的流程和表单设置信息。
	 * 
	 * @author:邓志城
	 * @date:2010-3-22 下午09:33:31
	 * @param taskId
	 *            任务实例id
	 * @return 流程和表单插件信息。 Object[] 内部数据结构 {0}流程表单插件信息[Json格式返回] {1}节点设置对象
	 * @throws SystemException
	 * @throws WorkflowException
	 */
	@SuppressWarnings("unchecked")
	public Object[] getNodeWorkflowPluginInfoByTaskId(String taskId)
			throws SystemException, WorkflowException {
		try {
			JSONArray array = new JSONArray();
			String nodeId = workflow.getNodeIdByTaskInstanceId(taskId);
			if (nodeId == null) {
				throw new SystemException("任务实例不存在或已被删除！");
			}
			TwfBaseNodesetting nodeSet = workflow
					.getNodesettingByNodeId(nodeId);
			Map<String, TwfBaseNodesettingPlugin> info = nodeSet.getPlugins();
			Set keySet = info.keySet();
			int size = keySet.size();
			for (int i = 1; i <= size; i++) {
				TwfBaseNodesettingPlugin pluginFlowNamei = info
						.get(GlobalBaseData.WORKFLOWPREFIX + i);
				TwfBaseNodesettingPlugin pluginFormNamei = info
						.get(GlobalBaseData.FORMPREFIX + i);
				TwfBaseNodesettingPlugin pluginFormIdi = info
						.get(GlobalBaseData.FORMIDPRIFIX + i);
				TwfBaseNodesettingPlugin pluginFlowIdi = info
						.get(GlobalBaseData.WORKFLOWIDPREFIX + i);
				String flowNamei = pluginFlowNamei == null ? null
						: pluginFlowNamei.getValue();
				String flowIdi = pluginFlowIdi == null ? null : pluginFlowIdi
						.getValue();
				String formNamei = pluginFormNamei == null ? null
						: pluginFormNamei.getValue();
				String formIdi = pluginFormIdi == null ? null : pluginFormIdi
						.getValue();
				if (flowNamei != null&& !"".equals(flowNamei) && formNamei != null && !"".equals(formNamei) && formIdi != null && !"".equals(formIdi)) {
					JSONObject obj = new JSONObject();
					obj.put("flowName", flowNamei);// 流程名称
					obj.put("flowId", flowIdi);// 流程id
					obj.put("formName", formNamei);// 表单名称
					obj.put("formId", formIdi);// 表单id
					array.add(obj);
				}
			}
			logger.info("流程和表单插件设置信息{}", array.toString());
			return new Object[] { array.toString(), nodeSet };
		} catch (WorkflowException e) {
			logger.error("获取节点上工作流设置信息时出现异常。", e);
			throw e;
		}
	}
	
	/**
	 * 根据节点id得到节点设置的流程和表单设置信息。
	 * 
	 *
	 * @date:2013-12-12 15:11:08
	 * @param nodeId
	 *            节点d
	 * @return 流程和表单插件信息。 Object[] 内部数据结构 {0}流程表单插件信息[Json格式返回] {1}节点设置对象
	 * @throws SystemException
	 * @throws WorkflowException
	 */
	@SuppressWarnings("unchecked")
	public Object[] getNodeWorkflowPluginInfoByNodeId(String nodeId)
			throws SystemException, WorkflowException {
		try {
			JSONArray array = new JSONArray();
			if (nodeId == null) {
				throw new SystemException("任务实例不存在或已被删除！");
			}
			TwfBaseNodesetting nodeSet = workflow
					.getNodesettingByNodeId(nodeId);
			Map<String, TwfBaseNodesettingPlugin> info = nodeSet.getPlugins();
			Set keySet = info.keySet();
			int size = keySet.size();
			for (int i = 1; i <= size; i++) {
				TwfBaseNodesettingPlugin pluginFlowNamei = info
						.get(GlobalBaseData.WORKFLOWPREFIX + i);
				TwfBaseNodesettingPlugin pluginFormNamei = info
						.get(GlobalBaseData.FORMPREFIX + i);
				TwfBaseNodesettingPlugin pluginFormIdi = info
						.get(GlobalBaseData.FORMIDPRIFIX + i);
				TwfBaseNodesettingPlugin pluginFlowIdi = info
						.get(GlobalBaseData.WORKFLOWIDPREFIX + i);
				String flowNamei = pluginFlowNamei == null ? null
						: pluginFlowNamei.getValue();
				String flowIdi = pluginFlowIdi == null ? null : pluginFlowIdi
						.getValue();
				String formNamei = pluginFormNamei == null ? null
						: pluginFormNamei.getValue();
				String formIdi = pluginFormIdi == null ? null : pluginFormIdi
						.getValue();
				if (flowNamei != null&& !"".equals(flowNamei) && formNamei != null && !"".equals(formNamei) && formIdi != null && !"".equals(formIdi)) {
					JSONObject obj = new JSONObject();
					obj.put("flowName", flowNamei);// 流程名称
					obj.put("flowId", flowIdi);// 流程id
					obj.put("formName", formNamei);// 表单名称
					obj.put("formId", formIdi);// 表单id
					array.add(obj);
				}
			}
			logger.info("流程和表单插件设置信息{}", array.toString());
			return new Object[] { array.toString(), nodeSet };
		} catch (WorkflowException e) {
			logger.error("获取节点上工作流设置信息时出现异常。", e);
			throw e;
		}
	}

	/**
	 * 根据节点id得到节点设置信息.
	 * 
	 * @author:邓志城
	 * @date:2010-8-2 上午10:44:12
	 * @param nodeId
	 *            节点id
	 * @return 节点设置信息 new Object[]{ 最大处理人数,是否允许选择其他人,是否返回经办人 }
	 */
	public Object[] getNodesettingByNodeId(String nodeId) {
		return nodesettingPluginService.getNodesettingByNodeId(nodeId);
	}

	/**
	 * 按照节点设置信息中的选择人员的设置方式处理迁移线信息
	 * 
	 * @author 严建
	 * @param list
	 *            迁移线信息
	 * @createTime Mar 7, 2012 11:41:43 PM
	 */
	public TransitionsInfoBean doNextTransitionsBySelectActorSetStyle(List list) throws ServiceException,DAOException, SystemException{
		try {
			return transitionService.doNextTransitionsBySelectActorSetStyle(list);
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

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}
