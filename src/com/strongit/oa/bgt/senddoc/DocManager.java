package com.strongit.oa.bgt.senddoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bgt.model.ToaYjzx;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.business.jbpmbusiness.JBPMBusinessManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.component.jdbc.JdbcSplitService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.util.StringUtil;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseNodesettingPlugin;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 7, 2012 10:05:59 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.bgt.senddoc.DocManager
 */
@Repository
@Transactional
public class DocManager {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IUserService userService;

	@Autowired
	private IWorkflowService workflowService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private JdbcSplitService jdbcSplitService;

	@Autowired
	private SendDocUploadManager manager;
	@Autowired
	private MyLogManager logService;

	@Autowired
	private JBPMBusinessManager jbpmBusinessManager;

	@Autowired
	private IAttachmentService attachService;

	private GenericDAOHibernate<Object, String> DAO;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		DAO = new GenericDAOHibernate<Object, String>(sessionFactory,
				Object.class);
	}

	/**
	 * 保存意见征询回执至意见征询附件表中同时恢复流程（之前是挂起状态）
	 * 
	 * @param model
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public void saveYjzxHz(ToaYjzx model, File[] files, String[] fileFileName,
			String[] delAttachId) throws SystemException {
		FileInputStream fis = null;
		try {
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					String ext = fileFileName[i].substring(fileFileName[i]
							.lastIndexOf(".") + 1, fileFileName[i].length());
					fis = new FileInputStream(file);
					attachService.saveAttachment(fileFileName[i], FileUtil
							.inputstream2ByteArray(fis), new Date(), ext, "1",
							"注:意见征询反馈文件", model.getId());// userId存储意见征询记录id，作为外键使用

				}
			}
			// 删除带有删除标记的附件(附件id以逗号结尾)
			if (delAttachId != null && delAttachId.length > 0) {
				for (String id : delAttachId) {
					if (id.endsWith(";")) {
						// 需要删除的附件
						attachService.deleteAttachment(id.substring(0, id
								.length() - 1));
					}
				}
			}
			/*
			 * 删除一下代码 意见征询不做流程状态的强制控制 严建 // 恢复流程时,验证流程是否已经是恢复状态 ProcessInstance
			 * processInstance = workflowService
			 * .getProcessInstanceById(model.getInstanceId()); // 挂起状态才做恢复操作 if
			 * (processInstance.isSuspended()) { boolean isSuccss =
			 * workflowService.changeProcessInstanceStatus(
			 * model.getInstanceId(), "2"); if (!isSuccss) { throw new
			 * SystemException("恢复流程时发生异常。"); } }
			 */
			Object[] toSelectItems = { "taskId", "taskNodeId",
					"processMainFormId","processMainFormBusinessId","isReceived"};
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("processInstanceId", model.getInstanceId());
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			List list = workflowService.getTaskInfosByConditionForList(sItems,
					paramsMap, orderMap, null, null, null, null);
			if (list != null) {

				boolean flag = false;// 是否驱动流程提交，默认不启动
				Object[] objs = (Object[]) list.get(0);
				String taskId = StringUtil.castString(objs[0]);
				String taskNodeId = StringUtil.castString(objs[1]);
				String isReceived = StringUtil.castString(objs[4]);

				// 严建 2012-06-19 16:16 以下代码是用户判断节点是否为意见征询节点
				System.out.println("判断当前节点是否为意见征询节点。。。");
				TwfBaseNodesetting nodeseting = workflowService
						.getNodesettingByNodeId(taskNodeId);
				Map plugins = nodeseting.getPlugins();
				if (plugins != null) {
					TwfBaseNodesettingPlugin plugin = (TwfBaseNodesettingPlugin) plugins
							.get("plugins_chkYjzxSuggestion");
					if (plugin != null) {
						if ("1".equals(plugin.getValue())) {
							flag = true;
						}
					}
				}
				if (flag) {
					System.out.println("当前节点时意见征询节点。。。。");
					User curuser = userService.getCurrentUser();
					String sugguestion = "【" + curuser.getUserName()
							+ "】进行意见反馈操作";
					String taskFormId = StringUtil.castString(objs[2]);
					String processMainFormBusinessId = StringUtil
							.castString(objs[3]);
					List transList = workflowService.getNextTransitions(taskId);
					Object[] firsttran = (Object[]) transList.get(0);
					Set<String> set = (Set<String>) firsttran[3];
					String nodeId = set.iterator().next().split("\\|")[1];// 得到节点id
					String[] strTaskActorArray = new String[] { "p0|"
							+ nodeId };
					String transitionName = (String) firsttran[0];
					String isNewForm = "1";
					if ("0".equals(processMainFormBusinessId)) {
						isNewForm = "1";
					} else {
						isNewForm = "0";
					}
					if(!"1".equals(isReceived)){
						workflowService.signForTask(IUserService.SYSTEM_ACCOUNT,taskId,  "0");
					}
					JSONObject json = new JSONObject();
	        		json.put("suggestion", sugguestion.toString());
	        		json.put("CAInfo", "");
					 workflowService.goToNextTransition(taskId, transitionName, "", isNewForm, taskFormId, processMainFormBusinessId, json.toString(), IUserService.SYSTEM_ACCOUNT, strTaskActorArray);
					
					/*
					 * 记录日志
					 * */ 
					ToaLog log = new ToaLog();
					try {
						InetAddress inet = InetAddress.getLocalHost();
						log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					log.setOpeUser(userService
							.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT)); // 操作姓名
					log.setLogState("1"); // 日志状态
					log.setOpeTime(new Date()); // 操作时间
					log.setLogInfo(sugguestion);// 日志信息
					logService.saveObj(log);
				}
			}
		} catch (FileNotFoundException e) {
			throw new SystemException(e);
		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

	}

	/**
	 * 得到意见征询附件列表
	 * 
	 * @param id
	 *            意见征询记录id
	 * @return 意见征询附件列表（意见反馈）
	 */
	public List<ToaAttachment> getAttachmentsWithoutContent(String id)
			throws DAOException {
		String sql = "select t.ATTACH_ID,t.ATTACH_NAME from T_OA_ATTACHMENT t where t.USER_ID = '"
				+ id + "'";
		ResultSet rs = DAO.executeJdbcQuery(sql);
		List<ToaAttachment> result = new ArrayList<ToaAttachment>();
		try {
			while (rs.next()) {
				ToaAttachment entry = new ToaAttachment();
				entry.setAttachId(rs.getString("ATTACH_ID"));
				entry.setAttachName(rs.getString("ATTACH_NAME"));
				result.add(entry);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
		return result;
	}

	/**
	 * 复制附件.
	 * 
	 * @param src
	 *            源附件记录对应外键
	 * @param dest
	 *            目标附件记录对应外键
	 */
	public void copyAttachments(String src, String dest) {
		List<ToaAttachment> attachments = this.getAttachments(src);
		if (attachments != null && !attachments.isEmpty()) {
			for (ToaAttachment attachment : attachments) {
				attachService.saveAttachment(attachment.getAttachName(),
						attachment.getAttachCon(), new Date(), attachment
								.getAttachType(), "1", "注:意见征询反馈文件", dest);// userId存储意见征询记录id，作为外键使用
			}
		}
	}

	/**
	 * 得到意见征询附件列表
	 * 
	 * @param id
	 *            意见征询记录id
	 * @return 意见征询附件列表（意见反馈）
	 */
	public List<ToaAttachment> getAttachments(String id) {

		String sql = "select t.ATTACH_ID,t.ATTACH_NAME from T_OA_ATTACHMENT t where t.USER_ID = '"
				+ id + "'";
		ResultSet rs = DAO.executeJdbcQuery(sql);
		List<ToaAttachment> result = new ArrayList<ToaAttachment>();
		try {
			while (rs.next()) {
				ToaAttachment entry = attachService.getAttachmentById(rs
						.getString("ATTACH_ID"));
				result.add(entry);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	public ToaYjzx getYjzx(String id) {
		String sql = "select * from t_oa_yjzx t where t.YJZXID = '" + id + "'";
		ResultSet rs = DAO.executeJdbcQuery(sql);
		ToaYjzx model = null;
		try {
			if (rs.next()) {
				model = new ToaYjzx();
				model.setContent(StringUtil.nullOfString(rs.getString("ZXNR")));
				model.setDate(rs.getTimestamp("PERSON_OPERATE_DATE"));//修改获取时间格式  BY：刘皙
				model.setId(id);
				model.setTitle(StringUtil.nullOfString(rs.getString("WORKFLOWTITLE")));
				model.setUnit(StringUtil.nullOfString(rs.getString("SEND_UNIT")));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DAOException(e);
		}
		return model;
	}

	/**
	 * 更新意见征询登记内容
	 * 
	 * @param model
	 */
	public void updateToYjzx(final ToaYjzx model) throws SystemException {
		if (model.getId() == null || model.getId().length() == 0) {
			throw new SystemException("参数id不可为空！");
		}
		StringBuilder sql = new StringBuilder("update t_oa_yjzx t set ");
		sql
				.append(" t.WORKFLOWTITLE = ?,t.SEND_UNIT=?,t.PERSON_OPERATE_DATE=?,t.ZXNR=? ");
		sql.append(" where t.YJZXID = ? ");
		jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

			public void setValues(PreparedStatement ps) throws SQLException {
				Date time = new Date();
				if (model.getStrDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					try {
						time = sdf.parse(model.getStrDate());
					} catch (ParseException e) {
						logger.error(e.getMessage(), e);
					}
				}
				String id = model.getId();
				if(id.indexOf(";")!=-1) {
					id = id.split(";")[2];
				}
				ps.setString(1, model.getTitle());
				ps.setString(2, model.getUnit());
				ps.setTimestamp(3, new Timestamp(time.getTime()));
				ps.setString(4, model.getContent());
				ps.setString(5, id);
			}

		});
	}

	/**
	 * 保存意见征询数据,并挂起流程实例
	 * 
	 * @param model
	 */
	public void saveToYjzx(final ToaYjzx model) throws SystemException {
		StringBuilder sql = new StringBuilder(
				"insert into t_oa_yjzx(YJZXID,WORKFLOWTITLE,SEND_UNIT,PERSON_OPERATE_DATE,ZXNR)");
		sql.append("values(?,?,?,?,?)");
		ToaYjzx entry = this.getYjzx(model.getId().split(";")[2]);
		if(entry != null) {
			this.updateToYjzx(model);
			Tjbpmbusiness bModel = jbpmBusinessManager.findByBusinessId(model.getId());
			if(model == null){
				bModel = new Tjbpmbusiness(model.getId());
			}
			if(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_COMMON.equals(bModel.getBusinessType())){//公文补录状态
				bModel.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_YJZX);//设置意见征询
			}else{
				bModel.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_YJZX);
			}
			jbpmBusinessManager.saveModel(bModel);
//			Tjbpmbusiness bModel = new Tjbpmbusiness(model.getId());
//			bModel.setBusinessType("0");
//			jbpmBusinessManager.saveModel(bModel);
			/*严建 删除一下代码 意见征询不做一下操作
			//非挂起状态才做挂起操作
			ProcessInstance processInstance = workflowService
					.getProcessInstanceById(model.getInstanceId());
			if (!processInstance.isSuspended()) {
				// 挂起流程实例
				boolean isSuccss = workflowService.changeProcessInstanceStatus(
						model.getInstanceId(), "1");
				if (!isSuccss) {
					throw new SystemException("挂起流程时发生异常。");
				}
			}
			*/
			return	;
		}
		jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {

			public void setValues(PreparedStatement ps) throws SQLException {
				Date time = new Date();
				if (model.getStrDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					try {
						time = sdf.parse(model.getStrDate());
					} catch (ParseException e) {
						logger.error(e.getMessage(), e);
					}
				}
				ps.setString(1, model.getId().split(";")[2]);// model.getId()
				// 格式：tableName;pkFieldName;pkFieldValue
				ps.setString(2, model.getTitle());
				ps.setString(3, model.getUnit());
				ps.setTimestamp(4, new Timestamp(time.getTime()));
				ps.setString(5, model.getContent());
			}

		});
		// 保存到工作流业务模型中
		Tjbpmbusiness bModel = jbpmBusinessManager.findByBusinessId(model.getId());
		if(bModel == null){
			 bModel = new Tjbpmbusiness(model.getId());
		}
		if(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_COMMON.equals(bModel.getBusinessType())){//公文补录正常标识
			bModel.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_YJZX);
		}else{
			bModel.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_YJZX);
		}
		jbpmBusinessManager.saveModel(bModel);
		/*严建 删除一下代码 意见征询不做一下操作
		// 非挂起状态才做挂起操作
		ProcessInstance processInstance = workflowService
				.getProcessInstanceById(model.getInstanceId());
		if (!processInstance.isSuspended()) {
			// 挂起流程实例
			boolean isSuccss = workflowService.changeProcessInstanceStatus(
					model.getInstanceId(), "1");
			if (!isSuccss) {
				throw new SystemException("挂起流程时发生异常。");
			}
		}
		*/
	}

	/**
	 * 得到已办流程列表 过滤掉重复的已办任务
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 上午11:54:19
	 * 
	 * @modifyer:luosy
	 * @description: 添加返回参数
	 * 
	 * 
	 * @param workflowType
	 *            流程类型
	 * @param processStatus
	 *            流程状态 0：未办结流程，1：已办结流程，2：所有流程
	 * @param sortType
	 *            排序方式 例如：SortConst.SORT_TYPE_DATE_ASC
	 * @param filterSign
	 *            是否过滤签收数据 0：否，1：是
	 * @return List<TaskBusinessBean]>
	 * @modify yanjian 2011-08-08 19:00 return List<TaskBusinessBean>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<TaskBusinessBean> getProcessedWorkflowForDesktop(
			String processStatus, int num, String sortType, String filterSign) {
		String userId = userService.getCurrentUser().getUserId();
		StringBuilder sqlQuery = new StringBuilder(
				"select pi.NAME_,pi.MAINFORM_ID_,pi.START_USER_NAME_,pi.ID_");
		sqlQuery
				.append(",pi.BUSINESS_NAME_,pi.START_,pi.END_,jbpmbusiness.END_TIME,jbpmbusiness.BUSINESS_TYPE,pi.TYPE_ID_,pi.BUSINESS_ID");
		sqlQuery
				.append(" from JBPM_PROCESSINSTANCE pi,T_JBPM_BUSINESS JBPMBUSINESS");
		sqlQuery.append(" where pi.BUSINESS_ID = JBPMBUSINESS.BUSINESS_ID ");
		sqlQuery.append(" and pi.business_id is not null");
		if ("0".equals(processStatus)) {
			sqlQuery.append(" and pi.END_ is null");
		} else if ("1".equals(processStatus)) {
			sqlQuery.append(" and pi.END_ is not null");
		}
		// 增加按流程类型检索的方式
		HttpServletRequest request = ServletActionContext.getRequest();
		String workflowType = request.getParameter("workflowType");
		if (workflowType != null && workflowType.length() > 0) {
			sqlQuery.append(" and pi.TYPE_ID_ in(" + workflowType + ")");
		}
		// 增加按流程类型反选检索方式
		String excludeWorkflowType = request
				.getParameter("excludeWorkflowType");
		if (excludeWorkflowType != null && excludeWorkflowType.length() > 0) {
			sqlQuery.append(" and pi.TYPE_ID_ not in(" + excludeWorkflowType
					+ ")");
		}
		StringBuilder signSql = new StringBuilder();
		signSql
				.append(" and pi.ISSUSPENDED_ = 0 and exists(select ti.ID_ from JBPM_TASKINSTANCE ti,T_WF_BASE_NODESETTINGPLUGIN tnplugin where "
						+ " ti.ISOPEN_ = 0 and ti.NODE_ID_=tnplugin.nsp_nodeid and ti.END_ is not null and ti.ISCANCELLED_ = 0"
						+ " and ti.ACTORID_='"
						+ userId
						+ "'"
						+ " and ti.PROCINST_ = pi.ID_ and tnplugin.NSP_PLUGINCLOBVALUE not like 1"
						+ " and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')");
//		signSql
//				.append(
//						" and not exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISOPEN_ = 1 ")
//				.append("and ti.ACTORID_='").append(userId).append(
//						"' and ti.PROCINST_ = pi.ID_) ");
		StringBuilder notsignSql = new StringBuilder();
		/*
		 * notsignSql.append(" and exists(select ti.ID_ from JBPM_TASKINSTANCE
		 * ti,T_WF_BASE_NODESETTINGPLUGIN tnplugin where " + " ti.ISOPEN_ = 0
		 * and ti.NODE_ID_=tnplugin.nsp_nodeid and ti.ISCANCELLED_ = 0" + " and
		 * ti.ACTORID_='"+userId+"'" + " and ti.PROCINST_ = pi.ID_ and
		 * tnplugin.NSP_PLUGINCLOBVALUE not like 1" + " and
		 * tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')") ;
		 */
		notsignSql
				.append(
						" and exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISCANCELLED_ = 0 ")
				.append("and ti.ACTORID_='").append(userId).append(
						"' and ti.PROCINST_ = pi.ID_) ");

		StringBuilder yjzxSql = new StringBuilder(
				" and jbpmbusiness.BUSINESS_TYPE=0");

		StringBuilder querySign = new StringBuilder().append(sqlQuery).append(
				signSql);
		StringBuilder queryYjzx = new StringBuilder().append(sqlQuery).append(
				yjzxSql).append(notsignSql);
		querySign.append(" union ").append(queryYjzx);

		String countSql = "select count(*) from (" + querySign.toString() + ")";
		logger.info(countSql);
		int count = jdbcTemplate.queryForInt(countSql);

		/*
		 * Object[] sItems = { "processName", "processMainFormId",
		 * "startUserName", "processInstanceId", "businessName",
		 * "processStartDate", "processEndDate" };//
		 * 查询processMainFormBusinessId用于过滤重复的已办任务 List toSelectItems =
		 * Arrays.asList(sItems);
		 */
		// Map<String, Object> paramsMap = new HashMap<String, Object>();
		// Map orderMap = new HashMap<Object, Object>();
		if (SortConst.SORT_TYPE_TASKENDDATE_ASC.equals(sortType)) {
			// orderMap.put("processStartDate", "0");
			querySign.append(" order by START_ asc");
		}else if(SortConst.SORT_TYPE_TASKENDDATE_DESC.equals(sortType)){
			querySign.append(" order by START_ desc");
		} else if (SortConst.SORT_TYPE_PROCESSSTARTDATE_ASC.equals(sortType)) {
			// orderMap.put("processStartDate", "0");
			querySign.append(" order by START_ asc");
		} else if (SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC.equals(sortType)) {
			// orderMap.put("processStartDate", "1");
			querySign.append(" order by START_ desc");
		} else {
			querySign.append(" order by START_ asc");
		}

		/*
		 * StringBuilder customSelectItems = new StringBuilder(); StringBuilder
		 * customFromItems = new StringBuilder(); StringBuilder customQuery =
		 * new StringBuilder();
		 * customSelectItems.append("jbpmbusiness.END_TIME,jbpmbusiness.BUSINESS_TYPE");//查询办结时间和业务类型。BUSINESS_TYPE=0表示是
		 * customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		 * //自办文时办理意见征询。,这类数据是流程挂起状态的。 customQuery.append(" @businessId =
		 * JBPMBUSINESS.BUSINESS_ID "); // 取非挂起流程和意见征询流程 //customQuery.append("
		 * and pi.ISSUSPENDED_ = 0 ");//or jbpmbusiness.BUSINESS_TYPE=0)
		 * initProcessedFilterSign(filterSign, userId, customSelectItems,
		 * customFromItems, customQuery);
		 */
		Page page = new Page(num, false);
		/*
		 * page = workflowService.getProcessInstanceByConditionForPage(page,
		 * toSelectItems, paramsMap, orderMap, customSelectItems .toString(),
		 * customFromItems.toString(), customQuery .toString(), null, null);
		 */
		page = jdbcSplitService.excuteSqlForPage(page, querySign.toString(),
				null, null);
		page.setTotalCount(count);
		List dlist = (page.getResult() == null ?new ArrayList():page.getResult());
		List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
		for (int i = 0; i < dlist.size(); i++) {
			Object[] obj = (Object[]) dlist.get(i);
			TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
			taskbusinessbean.setWorkflowName((String) obj[0]);
			taskbusinessbean.setFormId((String) obj[1]);
			taskbusinessbean.setStartUserName((String) obj[2]);
			taskbusinessbean.setInstanceId(obj[3].toString());
			taskbusinessbean.setBusinessName((String) obj[4]);
			taskbusinessbean.setWorkflowStartDate((Date) obj[5]);
			taskbusinessbean.setWorkflowEndDate((Date) obj[6]);
			taskbusinessbean.setEndTime(StringUtil.castString(obj[7]));
			taskbusinessbean.setBusinessType(StringUtil.castString(obj[8]));// 业务类型，是否是自办文中启动意见征询数据
			taskbusinessbean.setWorkflowType(StringUtil.castString(obj[9]));
			taskbusinessbean.setBsinessId(StringUtil.castString(obj[10]));
			beans.add(taskbusinessbean);
		}

		String[] pfNames = new String[dlist.size()];
		StringBuilder params = new StringBuilder();
		for (int i = 0; i < beans.size(); i++) {
			TaskBusinessBean taskbusinessbean = beans.get(i);
			pfNames[i] = taskbusinessbean.getWorkflowName();
			params.append(taskbusinessbean.getInstanceId()).append(",");
		}
		Map<String, Map> map = null;
		Map<String,Map> yjzxMap = null;
		Map<String,Boolean> isfirstyjzxMap = new HashMap<String, Boolean>();
		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
			StringBuilder sql = new StringBuilder(
					"select " +
					"ti.procinst_," +
					"ti.id_ as taskId," +
					"ti.start_ as taskStartDate ");
			sql.append("from JBPM_TASKINSTANCE ti where ti.procinst_ in(");
			sql.append(params.toString());
			sql.append(") AND  TI.ACTORID_='"+userId+"'  AND TI.END_ IS NOT NULL order by ti.END_ desc");
			List<Map<String, Object>> taskList = jdbcTemplate.queryForList(sql
					.toString());
			logger.info(sql.toString());
			if (taskList != null && !taskList.isEmpty()) {
				map = new HashMap<String, Map>();
				for (Map<String, Object> rsMap : taskList) {
					if (!map.containsKey(String.valueOf(rsMap.get("PROCINST_")))) {
						map.put(String.valueOf(rsMap.get("PROCINST_")), rsMap);
					}
				}
			}
			
			StringBuilder yjzxTSql = new StringBuilder(
					"select " +
					"ti.procinst_," +
					"ti.id_ as taskId," +
					"ti.start_ as taskStartDate ");
			yjzxTSql.append("from JBPM_TASKINSTANCE ti where ti.procinst_ in(");
			yjzxTSql.append(params.toString());
			yjzxTSql.append(") AND  TI.ISCANCELLED_ = 0 AND  TI.ACTORID_='"+userId+"'  AND EXISTS "+
					"(SELECT PI.ID_ "+
						" FROM JBPM_PROCESSINSTANCE PI, T_JBPM_BUSINESS JBPMBUSINESS "+
						" WHERE PI.BUSINESS_ID = JBPMBUSINESS.BUSINESS_ID "+
						" AND PI.BUSINESS_ID IS NOT NULL "+
						" AND PI.END_ IS NULL "+
						" AND JBPMBUSINESS.BUSINESS_TYPE = 0 "+
						" AND TI.PROCINST_ = PI.ID_)");
			List<Map<String, Object>> yzxTtaskList = jdbcTemplate.queryForList(yjzxTSql
					.toString());
			logger.info(yjzxTSql.toString());
			if (yzxTtaskList != null && !yzxTtaskList.isEmpty()) {
				yjzxMap = new HashMap<String, Map>();
				for (Map<String, Object> rsMap : yzxTtaskList) {
					if(map.containsKey(String.valueOf(rsMap.get("PROCINST_")))){//不是第一次意见征询,将map中的rsMap赋值
						yjzxMap.put(String.valueOf(rsMap.get("PROCINST_")),map.get(String.valueOf(rsMap.get("PROCINST_"))));
						isfirstyjzxMap.put(String.valueOf(rsMap.get("PROCINST_")), false);
					}else{//第一次意见征询
						if (!yjzxMap.containsKey(String.valueOf(rsMap.get("PROCINST_")))) {
							yjzxMap.put(String.valueOf(rsMap.get("PROCINST_")), rsMap);
							isfirstyjzxMap.put(String.valueOf(rsMap.get("PROCINST_")), true);
						}
					}
				}
			}
		}

		Map<String, String> pfNameMapRest2 = manager.getPfNameMapRest2(pfNames);
		for (int i = 0; i < beans.size(); i++) {
			TaskBusinessBean taskbusinessbean = beans.get(i);
			taskbusinessbean.setCurTaskActorInfo(workflowService
					.getCurrentTaskHandle(taskbusinessbean.getInstanceId())
					.getActor());
			String pfName = taskbusinessbean.getWorkflowName();
			String processFileRest2 = pfNameMapRest2.get(pfName);
			if (processFileRest2 == null || "".equals(processFileRest2)) {// 不存在别名是，别名即为流程名
				taskbusinessbean.setWorkflowAliaName(pfName);
			} else {
				taskbusinessbean.setWorkflowAliaName(processFileRest2);
			}
			if("0".equals(taskbusinessbean.getBusinessType())){
				if (yjzxMap != null && !yjzxMap.isEmpty()) {
					if(yjzxMap.get(
							taskbusinessbean.getInstanceId()) != null){
						taskbusinessbean.setTaskId(yjzxMap.get(
								taskbusinessbean.getInstanceId()).get("taskId")
								+ "");
						taskbusinessbean.setFirstYjzx(isfirstyjzxMap.get(taskbusinessbean.getInstanceId()));
					}
				}
			}else{
				if (map != null && !map.isEmpty()) {
					if(map.get(
							taskbusinessbean.getInstanceId()) != null){
						taskbusinessbean.setTaskId(map.get(
								taskbusinessbean.getInstanceId()).get("taskId")
								+ "");
					}
				}
			}
		}
		page.setResult(beans);
		return page;
	}
	/**
	 * 任务取回前，判断一下当前流程环节是不是处于意见征询节点，是，返回一个包含数据的数组，否则返回null。
	 * 
	 * 数据格式{任务实例id，任务节点id，流程主表单id，业务id}
	 * 
	 * @author yanjian
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 9:49:33 AM
	 */
	@SuppressWarnings("unchecked")
	public Object[] fetchTaskbeforeIsAtYjzxNode(String taskId)
			throws ServiceException, DAOException, SystemException {
		try {
			Object[] result = null;
			String instanceId = workflowService.getProcessInstanceIdByTiId(taskId);
			Object[] toSelectItems = { "taskId", "taskNodeId",
					"processMainFormId", "processMainFormBusinessId" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("processInstanceId", instanceId);
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			List list = workflowService.getTaskInfosByConditionForList(sItems, paramsMap,
							orderMap, null, null, null, null);
			if (list != null && !list.isEmpty()) {
				boolean flag = false;// 是否删除意见征询相关信息
				Object[] objs = (Object[]) list.get(0);
				String taskNodeId = StringUtil.castString(objs[1]);

				// 严建 2012-06-19 16:16 以下代码是用户判断节点是否为意见征询节点
				System.out.println("判断当前节点是否为意见征询节点。。。");
				TwfBaseNodesetting nodeseting = workflowService.getNodesettingByNodeId(taskNodeId);
				Map plugins = nodeseting.getPlugins();
				if (plugins != null) {
					TwfBaseNodesettingPlugin plugin = (TwfBaseNodesettingPlugin) plugins
							.get("plugins_chkYjzxSuggestion");
					if (plugin != null) {
						if ("1".equals(plugin.getValue())) {
							flag = true;
						}
					}
				}
				if (flag) {
					result = objs;
				}
			}
			return result;
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
	 * 
	 * 
	 * @author yanjian
	 * @param taskId
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 9:33:25 AM
	 */
	/**
	 * 流程取回，判断是否取回意见征询节点任务，如果是则删除意见征询信息和清空处理意见，否则不做处理
	 * 
	 * @author yanjian
	 * @param info		意见征询节点任务信息
	 * @param taskId	当前任务实例id
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Dec 4, 2012 2:50:24 PM
	 */
	@SuppressWarnings("unchecked")
	public void fetchTaskAndDeleteYjzxInfo(Object[] info,String taskId ) throws ServiceException,DAOException, SystemException{
		try {
			if(info != null){
				logger.info("@:当前节点时意见征询节点，开始删除意见征询及反馈信息");
				String bussinessId = StringUtil.castString(info[3]);
				Tjbpmbusiness bModel = jbpmBusinessManager.findByBusinessId(bussinessId);
				if(bModel != null){
					//修改中间表中的数据
					bModel.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_COMMON);
					jbpmBusinessManager.saveModel(bModel);
					if(bussinessId != null){
						String docId = bussinessId.split(";")[2];
						//删除意见征询附件
						String attach_sql = "select t.ATTACH_ID from T_OA_ATTACHMENT t where t.USER_ID = '"+ docId + "'";
						ResultSet rs = DAO.executeJdbcQuery(attach_sql);
						try {
							while (rs.next()) {
								String attach_id = rs.getString("ATTACH_ID");
								attachService.deleteAttachment(attach_id);
							}
						} catch (SQLException e) {
							logger.error(e.getMessage(), e);
							throw new DAOException(e);
						} finally {
							if (rs != null) {
								try {
									rs.close();
								} catch (SQLException e) {
									logger.warn(e.getMessage(), e);
								}
							}
						}
						
						//删除意见征询数据
						StringBuilder deleteString = new StringBuilder("DELETE FROM T_OA_YJZX T WHERE T.YJZXID = '"+docId+"'");
						DAO.executeJdbcUpdate(deleteString.toString());
					}
				}
				
				//清空处理意见
				List<TwfInfoApproveinfo> wfApproveInfo = workflowService.getDataByHql(
		        		"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
		        		new Object[] { new Long(taskId) });
				if(wfApproveInfo != null && !wfApproveInfo.isEmpty()){
					TwfInfoApproveinfo approveInfo = wfApproveInfo.get(0);
					approveInfo.setAiContent("");
					workflowService.saveApproveInfo(approveInfo);// 更新意见和内容
					logger.info("@:取回意见征询文，清空处理意见");
				}
				
				
				/*
				 * 记录日志
				 * */ 
				ToaLog log = new ToaLog();
				try {
					InetAddress inet = InetAddress.getLocalHost();
					log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
				log.setLogState("1"); // 日志状态
				log.setOpeTime(new Date()); // 操作时间
				log.setLogInfo("取回处于意见征询的文，系统删除意见征询及反馈信息。");// 日志信息
				logService.saveObj(log);
			}
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
}
