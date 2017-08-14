package com.strongit.oa.senddoc.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bgt.model.ToaYjzx;
import com.strongit.oa.bgt.senddoc.DocManager;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaDefinitionPlugin;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.common.workflow.oabo.BackInfoBean;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.docservice.IDocService;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseNodesettingPlugin;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.po.TaskInstanceBean;
import com.strongit.workflow.workflowDesign.action.util.ProcessUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 保存领导批示意见 Manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Dec 20, 2011 3:47:40 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SendDocUploadManager
 */
@Service
@Transactional
@OALogger
public class SendDocUploadManager {

	@Autowired
	SendDocManager manager;

	@Autowired
	SendDocBaseManager sendDocBaseManager;

	@Autowired
	protected IDocService docService;

	@Autowired
	private DocManager docManager;

	private @Autowired
	IWorkflowService workflowService;// 工作流服务类
	@Autowired
	private IWorkflowConstService workflowConstService;
	@Autowired
	private IUserService userService;// 统一用户服务
	@Autowired
	private ICustomUserService customUserService;// 统一用户服务

	@Autowired
	private IWorkflowService workflow; // 工作流服务

	@Autowired
	private JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DefinitionPluginService definitionPluginService;// 流程定义插件服务类.

//	公共附件接口
	@Autowired
	private IAttachmentService attachmentService;

	/**
	 * @description (该方法未完成)
	 * @param bussinessId
	 *            业务数据id
	 * @param map
	 *            要更新的数据 map格式： key:Object[]【字段名称:字段信息】 Object[]格式：
	 *            {dataType,dataValue}【字段类型，字段值】
	 * @author 严建
	 * @createTime Dec 20, 2011 3:47:59 PM
	 * @return void
	 */
	public void saveUploadInfo(String bussinessId, Map map)
			throws SystemException {

	}

	/**
	 * 查询表中的字段信息
	 * 
	 * @description
	 * @param bussinessId
	 *            业务数据id
	 * @param fliedNames
	 *            要查询的字段(多个字段用“,”隔开)
	 * @author 严建
	 * @createTime Dec 20, 2011 6:43:57 PM
	 * @return Map
	 */
	public Map loadAttachInfo(String bussinessId, String fliedNames) {
		String[] args = bussinessId.split(";");
		String tableName = args[0];
		String pkName = args[1];
		String pkFieldValue = args[2];

		String sql = "select " + fliedNames + " from " + tableName + " where "
				+ pkName + "='" + pkFieldValue + "'";
		Map map = jdbcTemplate.queryForMap(sql);
		return map;
	}

	/**
	 * 查询表中的PDF内容字段信息
	 * 
	 * @description
	 * @param pkFieldValue
	 *            条件判断字段值
	 * @param fliedNames
	 *            要查询的字段(多个字段用“,”隔开)
	 * @param tableName
	 *            表名
	 * @param pkName
	 *            条件判断字段
	 * @author 严建
	 * @createTime Dec 20, 2011 6:43:57 PM
	 * @return Map
	 */
	public Map<?, ?> loadPDFInfo(String pkFieldValue, String fliedNames,
			String tableName, String pkName) {
		String sql = "select " + fliedNames + " from " + tableName + " where "
				+ pkName + "='" + pkFieldValue + "'";
		Map<?, ?> map = jdbcTemplate.queryForMap(sql);
		return map;
	}

	/**
	 * 查询表中的所有字段信息
	 * 
	 * @description
	 * @param tableName
	 *            表名
	 * @param fliedNames
	 *            要查询的字段
	 * @author 刘皙
	 * @createTime 2012年3月13日16:33:31
	 * @return List
	 */
	public List<?> getAllBussinessId(String tableName, String fliedNames) {
		String sql = "select " + fliedNames + " from " + tableName;
		List<?> list = jdbcTemplate.queryForList(sql);
		sql = "update t_oarecvdoc a set adobe_pdf_name =(select b.oarecvdocid || '.pdf' from t_oarecvdoc b where a.oarecvdocid=b.oarecvdocid) where adobe_pdf_name is null and a.pdfcontent is not null";
		jdbcTemplate.execute(sql);
		return list;
	}

	/**
	 * @description 根据业务id判断表中是否存在指定字段 param bussinessId 业务数据id param fliedName
	 *              字段名称
	 * @author 严建
	 * @createTime Dec 21, 2011 5:07:25 PM
	 * @return boolean
	 */
	public boolean fliedIsExisttable(String bussinessId, String fliedName) {
		String[] args = bussinessId.split(";");
		String tableName = args[0];
		String pkName = args[1];
		String pkFieldValue = args[2];

		String sql = "select * from " + tableName + " where " + pkName + "='"
				+ pkFieldValue + "'";
		Map map = jdbcTemplate.queryForMap(sql);
		if (map.containsKey(fliedName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存领导批示意见及其他信息
	 * 
	 * @description
	 * @param bussinessId
	 *            业务数据id
	 * @param attachName
	 *            扫描附件名称(图片)
	 * @param doneSuggestion
	 *            办结意见
	 * @param upload
	 *            上传的文件
	 * @author 严建
	 * @createTime Dec 20, 2011 4:27:20 PM
	 * @return void
	 */
	public void saveAttachInfo(String bussinessId, String attachName,
			String doneSuggestion, File upload) throws SystemException {
		Connection con = manager.getCurrentConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			con.setAutoCommit(false);

			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkName = args[1];
			String pkFieldValue = args[2];

			String sql = "select * from " + tableName + " where " + pkName
					+ "='" + pkFieldValue + "' for update";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();

			try {
				is = new FileInputStream(upload);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SystemException("保存文件" + attachName + "时，出现异常！");
			}

			if (rs.next()) {
				sql = "update "
						+ tableName
						+ " set BANJIEYIJIAN=?,SHAOMIAOFUJIAN_NAME=?,SHAOMIAOFUJIAN=? where "
						+ pkName + "=?";
				psmt = con.prepareStatement(sql);
				psmt.setString(1, doneSuggestion);
				psmt.setString(2, attachName);
				psmt.setBinaryStream(3, is, is.available());
				psmt.setString(4, pkFieldValue);
				psmt.executeUpdate();
			}
			con.commit();
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
	}

	/**
	 * @author:luosy
	 * @description: 保存领导批示意见及其他信息 (多附件保存到附件表)
	 * @date : 2012-4-6
	 * @modifyer:
	 * @description
	 * @param bussinessId
	 *            业务数据id
	 * @param attachName
	 *            扫描附件名称(图片)
	 * @param doneSuggestion
	 *            办结意见
	 * @param upload
	 *            上传的文件
	 * @throws SystemException
	 */
	public void saveAttachsInfo(String bussinessId, String[] fileFileName,
			String doneSuggestion, File[] file) throws SystemException {
		Connection con = manager.getCurrentConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			con.setAutoCommit(false);

			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkName = args[1];
			String pkFieldValue = args[2];

			String sql = "select * from " + tableName + " where " + pkName
					+ "='" + pkFieldValue + "' for update";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();

			StringBuilder attachName = new StringBuilder("");
			try {
				//		保存附件
				if(file!=null){
					attachName.append("attachId");
					Calendar cals = Calendar.getInstance();
					for(int i=0;i<file.length;i++){
						FileInputStream fils = null;
						try{
							fils = new FileInputStream(file[i]);
							byte[] buf = new byte[(int)file[i].length()];
							fils.read(buf);	
							String ext = fileFileName[i].substring(fileFileName[i].lastIndexOf(".") + 1,
									fileFileName[i].length());
							//添加公共附件表数据
							String attachId = attachmentService.saveAttachment(fileFileName[i], buf, cals.getTime(), ext, "1", "注:办结扫描件", userService.getCurrentUser().getUserId());
							//添加业务表 附件ID
							attachName.append(",").append(attachId);
							
						}catch (Exception e) {
							e.printStackTrace();
							throw new ServiceException(MessagesConst.save_error,               
									new Object[] {"附件上传失败"});
						}finally{
							try {
								fils.close();
							} catch (IOException e) {
								throw new ServiceException(MessagesConst.save_error,               
										new Object[] {"附件保存失败"});
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SystemException("保存文件" + attachName + "时，出现异常！");
			}

			if (rs.next()) {
				sql = "update "
						+ tableName
						+ " set BANJIEYIJIAN=?,SHAOMIAOFUJIAN_NAME=? where " //SHAOMIAOFUJIAN_NAME=?,SHAOMIAOFUJIAN=? where "
						+ pkName + "=?";
				psmt = con.prepareStatement(sql);
				psmt.setString(1, doneSuggestion);
				psmt.setString(2, attachName.toString());
				psmt.setString(3, pkFieldValue);
				psmt.executeUpdate();
			}
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SystemException("保存附件异常！", e);
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
	
	/**
	 * 初始化扫描附件信息
	 * 
	 * @description
	 * @param bussinessId
	 *            业务数据id
	 * @param request
	 *            reques请求
	 * @author 严建
	 * @createTime Dec 22, 2011 3:20:19 PM
	 * @return void
	 */
	public void initAttachInfo(HttpServletRequest request, String bussinessId) {
		if (this.fliedIsExisttable(bussinessId, "BANJIEYIJIAN")
				&& this.fliedIsExisttable(bussinessId, "SHAOMIAOFUJIAN_NAME")) {
			String fliedNames = "BANJIEYIJIAN,SHAOMIAOFUJIAN_NAME";
			Map map = this.loadAttachInfo(bussinessId, fliedNames);
			if (map.get("BANJIEYIJIAN") != null) {
				request.setAttribute("doneSuggestion", "1");
			}
			if (map.get("SHAOMIAOFUJIAN_NAME") != null) {
				request.setAttribute("attachName", "1");
			}
		}

	}

	/**
	 * 根据节点信息初始化查看已办文界面<br/>
	 * (1)初始化流程控制功能(已办)<br/>
	 * (2)初始化当前环节是否启用了废除功能<br/>
	 * 
	 * @description
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Apr 27, 2012 2:21:45 PM
	 */
	public void initProcessedByNodeSetting(TwfBaseNodesetting nodeSetting){
	    initProcessedWorkflowFunctionByNodeSetting(nodeSetting);
	    initShowRepeal(nodeSetting);
	}
	
	
	/**
	 * 初始化流程控制功能(已办)<br/>
	 * (1)对已办文是否可以进行强制取回(默认值"0"：表示不能进行强制取回，"1"：表示可以强制取回)<br/>
	 * @description
	 * @author 严建
	 * @createTime Apr 27, 2012 2:24:00 PM
	 */
	public void  initProcessedWorkflowFunctionByNodeSetting(TwfBaseNodesetting nodeSetting){
	    initMustFetchTask(nodeSetting);
	}
	
	/**
	 * 对已办文是否可以进行强制取回(默认值"0"：表示不能进行强制取回，"1"：表示可以强制取回)
	 * 
	 * @description
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Apr 27, 2012 2:26:34 PM
	 */
	public void initMustFetchTask(TwfBaseNodesetting nodeSetting){
	    setRequestAttribute(nodeSetting, "plugins_mustFetchTask",
			"mustFetchTask", "0");
	}
	/**
	 * 根据节点设置信息初始化办理界面<br/> 
	 * (1)初始化流程控制的功能<br/> 
	 * (2)初始化办理界面表单的功能
	 * 
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Feb 28, 2012 10:55:01 AM
	 */
	public void initViewByNodeSetting(TwfBaseNodesetting nodeSetting) {
		initWorkflowFunctionByNodeSetting(nodeSetting);
		initEFormFuctionByNodeSetting(nodeSetting);
	}

	/**
	 * 根据节点设置信息初始化办理界面流程控制的功能<br/> 
	 * (1)初始化流程控制审批意见必填(退回时，不做控制)<br/>
	 * (2)初始化判断当前流程所在节点是否为流程中的第一个节点的信息<br/>
	 * (3)初始化是否显示流程期限设置
	 * (4)初始化当前环节是否启用了废除功能<br/>
	 * (5)初始化当前环节是否启用了签发功能
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Feb 28, 2012 11:01:40 AM
	 */
	private void initWorkflowFunctionByNodeSetting(
			TwfBaseNodesetting nodeSetting) {
		initSuggestionRequired(nodeSetting);
		initAddIsFirstNode(nodeSetting);
		initShowWorkflowTimeOutSet(nodeSetting);
		initShowRepeal(nodeSetting);
		initModifySend(nodeSetting);
	}

	/**
	 * 根据节点设置信息初始化办理界面表单的功能<br/> 
	 * (1)初始化office的功能<br/>
	 *  (2)初始化表单【收文正文】Tab页的功能<br/>
	 * 
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Mar 15, 2012 10:03:18 AM
	 */
	private void initEFormFuctionByNodeSetting(TwfBaseNodesetting nodeSetting) {
		initOfficeFunctionByNodeSetting(nodeSetting);
		initPDFFunctionByNodeSetting(nodeSetting);
	}

	/**
	 * 根据节点设置信息初始化办理界面office的功能<br/> (1)初始化office痕迹保留情况<br/>
	 * (2)初始化office痕迹显示情况<br/>
	 * 
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Feb 28, 2012 10:55:01 AM
	 */
	private void initOfficeFunctionByNodeSetting(TwfBaseNodesetting nodeSetting) {
		initMarkModify(nodeSetting);
		initShowRevisions(nodeSetting);
	}

	/**
	 * 根据节点设置信息初始化表单【收文正文】Tab页<br/> (1)初始化PDF上传权限情况<br/>(2)初始化SMJ(扫描件)上传权限情况<br/>
	 * 
	 * @author 刘皙
	 * @param nodeSetting
	 * @createTime 2012-3-1 15:10:24
	 */
	private void initPDFFunctionByNodeSetting(TwfBaseNodesetting nodeSetting) {
		initPermitUploadPDF(nodeSetting);
		initPermitUploadSMJ(nodeSetting);
	}

	/**
	 * 初始化office痕迹保留情况<br/> plugins_doMarkModify【0：默认不留痕迹；1：留痕迹】
	 * 
	 * @description
	 * @param nodeSetting
	 *            节点情况
	 * @author 严建
	 * @createTime Dec 22, 2011 4:28:07 PM
	 * @return void
	 */
	public void initMarkModify(TwfBaseNodesetting nodeSetting) {
		setRequestAttribute(nodeSetting, "plugins_doMarkModify",
				"doMarkModify", "0");
	}

	/**
	 * 初始化流程控制审批意见必填(退回时，不做控制)<br/> plugins_suggestionrequired【0：不控制必填；1：控制必填】
	 * 
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Feb 28, 2012 11:03:13 AM
	 */
	public void initSuggestionRequired(TwfBaseNodesetting nodeSetting) {
		setRequestAttribute(nodeSetting, "plugins_suggestionrequired",
				"suggestionrequired", "0");
	}

	// plugins_notifybackinfo
	/**
	 * 显示上一步退回意见
	 * 
	 * @author 严建
	 * @param request
	 * @param nodeSetting
	 * @param instanceId
	 * @param takId
	 * @createTime Feb 20, 2012 2:07:30 PM 数据格式： 不显示退回信息，返回"0"
	 *             显示退回信息，返回"1,"+退回意见 注：退回意见末尾存在5个空格符，用以和普通意见进行区分
	 */
	public void initNotifyBackInfo(HttpServletRequest request,
			TwfBaseNodesetting nodeSetting, String instanceId, String takId) {
		if (takId == null || "".equals(takId)) {
			throw new ServiceException("参数takId不能为空或空字符串");
		}
		if (instanceId == null || "".equals(instanceId)) {
			throw new ServiceException("参数instanceId不能为空或空字符串");
		}
		StringBuilder data = new StringBuilder();
		String notifyBackInfo = "0";
		String content = null;
		Map plugins = nodeSetting.getPlugins();// 得到节点上的插件信息集合
		TwfBaseNodesettingPlugin pluginsNotifyBackInfo = (TwfBaseNodesettingPlugin) plugins
				.get("plugins_notifybackinfo");
		if (pluginsNotifyBackInfo != null
				&& pluginsNotifyBackInfo.getValue() != null) {
			notifyBackInfo = pluginsNotifyBackInfo.getValue();
			if ("1".equals(notifyBackInfo)) {
				TaskInstance taskinstance = workflow.getTaskInstanceById(takId);
				if ("1".equals(taskinstance.getIsBackspace())) {
					// workflow.getProcessHandlesAndNodeSettingByPiId(instanceId);
				}
			}
		}
		// if (backSuggestion.length() >= 5) {
		// lastchar = backSuggestion.substring(backSuggestion
		// .length() - 5);
		// if (" ".equals(lastchar)) {
		// continue;
		// }
		// }
		if ("1".equals(notifyBackInfo)) {
			request.setAttribute("notifyBackInfo", data.append(notifyBackInfo)
					.append(",").append((content == null ? "" : content)));
		} else {
			request.setAttribute("notifyBackInfo", notifyBackInfo);
		}
	}

	/**
	 * 初始化office痕迹显示情况<br/>
	 *  plugins_doShowRevisions【0：默认显示痕迹；1：不显示痕迹】
	 * 
	 * @description
	 * @param nodeSetting
	 *            节点情况
	 * @author 严建
	 * @createTime Dec 22, 2011 4:28:07 PM
	 * @return void
	 */
	public void initShowRevisions(TwfBaseNodesetting nodeSetting) {
		setRequestAttribute(nodeSetting, "plugins_doShowRevisions",
				"doShowRevisions", "0");
	}

	/**
	 * 初始化表单【收文正文】Tab页判断是否允许上传PDF<br/> 
	 * plugins_permitUploadPDF【0：不允许；1：允许】
	 * 
	 * @description
	 * @param nodeSetting
	 *            节点情况
	 * @author 刘皙
	 * @createTime 2012-3-1 15:16:25
	 * @return void
	 */
	public void initPermitUploadPDF(TwfBaseNodesetting nodeSetting) {
		setRequestAttribute(nodeSetting, "plugins_permitUploadPDF",
				"doPermitUploadPDF", "0");
	}

	/**
	 * 初始化表单【收文正文】Tab页判断是否允许上传SMJ（扫描件）<br/> 
	 * plugins_permitUploadSMJ【0：不允许；1：允许】
	 * 
	 * @description
	 * @param nodeSetting
	 *            节点情况
	 * @author 刘皙
	 * @createTime 2013-5-9 11:22:13
	 * @return void
	 */
	public void initPermitUploadSMJ(TwfBaseNodesetting nodeSetting) {
		setRequestAttribute(nodeSetting, "plugins_permitUploadSMJ",
				"doPermitUploadSMJ", "0");
	}

	/**
	 * 初始化判断当前流程所在节点是否为流程中的第一个节点的信息<br/>
	 *  isFirstNode【0：不是；1：是】
	 * 
	 * @description
	 * @param nodeSetting
	 *            节点情况
	 * @author 刘皙
	 * @createTime 2012-3-12 14:25:18
	 * @return void modify yanjian 解决workflowName为空或空字符串引起空指针的问题
	 */
	public void initAddIsFirstNode(TwfBaseNodesetting nodeSetting) {
		Long nodeId = nodeSetting.getNsNodeId();
		String workflowName = (String) ServletActionContext.getRequest()
				.getAttribute("workflowName");
		if (workflowName == null || "".equals(workflowName)) {
			String instanceId = (String) ServletActionContext.getRequest()
					.getAttribute("instanceId");
			workflowName = workflowService.getProcessInstanceById(instanceId)
					.getName();
		}
		Long firstNodeId = workflow.getFirstNodeId(workflowName);
		if ((nodeId).equals(firstNodeId)) {
			setRequestAttribute(nodeSetting, "", "isFirstNode", "1");
		} else {
			setRequestAttribute(nodeSetting, "", "isFirstNode", "0");
		}
	}

	/**
	 * 初始化是否显示流程期限设置
	 * 
	 * @description
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Apr 16, 2012 3:33:30 PM
	 */
	public  void initShowWorkflowTimeOutSet(TwfBaseNodesetting nodeSetting) {
	    setRequestAttribute(nodeSetting, "plugins_lcqx","isShowlcqx", "0");
	}
	/**
	 * 初始化当前环节是否启用了废除功能
	 * 
	 * @author 严建
	 * @param nodeSetting
	 * @createTime Jul 2, 2012 9:14:08 AM
	 */
	public  void initShowRepeal(TwfBaseNodesetting nodeSetting) {
		setRequestAttribute(nodeSetting, "plugins_showrepeal","showrepeal", "0");
	}
	
    /**
     * 初始化是否为签发环节
     * 
     * @author yanjian
     * @param nodeSetting
     *            Sep 18, 2012 11:06:46 AM
     */
    public void initModifySend(TwfBaseNodesetting nodeSetting) {
        setRequestAttribute(nodeSetting, "plugins_chkModifySend", "modifySend",
                "0");
    }
	
	/**
	 * 根据流程实例id获取处理状态tab页的展现设置
	 * 
	 * @description
	 * @author 严建
	 * @param instanceid
	 *            流程实例id
	 * @createTime Dec 22, 2011 4:36:18 PM
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getWorkStatePDImageViewInfo(String instanceid) {
		String result = "";
		ProcessInstance processInstance = workflow
				.getProcessInstanceById(instanceid);
		String pfName = processInstance.getName();
		List<TwfBaseProcessfile> twfBaseProcessfileList = workflow
				.getDataByHql("from TwfBaseProcessfile t where t.pfName = ?",
						new Object[] { pfName });
		TwfBaseProcessfile processFile = twfBaseProcessfileList.get(0);

		// 是否设置了显示流程简图
		String definitionId = processFile.getPfId().toString();
		boolean isShowSimplePDImage = false;// 默认不显示流程简图tab
		boolean isShowPDImage = false;// 默认显示流程图tab
		if (definitionId != null) {
			String plugins_isShowPDImage = "";
			String plugins_isShowSimplePDImage = "";
			List<ToaDefinitionPlugin> plugins = definitionPluginService
					.find(definitionId);
			if (plugins != null && !plugins.isEmpty()) {
				for (ToaDefinitionPlugin plugin : plugins) {
					if ("plugins_isShowPDImage".equals(plugin
							.getDefinitionPluginName())) {
						plugins_isShowPDImage = plugin
								.getDefinitionPluginValue();
						break;
					}
				}

				for (ToaDefinitionPlugin plugin : plugins) {
					if ("plugins_isShowSimplePDImage".equals(plugin
							.getDefinitionPluginName())) {
						plugins_isShowSimplePDImage = plugin
								.getDefinitionPluginValue();
						break;
					}
				}
			}
			if (plugins_isShowPDImage != null
					&& plugins_isShowPDImage.equals("1")) {// 不显示流程图tab
				isShowPDImage = true;
			}
			if (plugins_isShowSimplePDImage != null
					&& plugins_isShowSimplePDImage.equals("1")) {// 显示流程简图tab
				isShowSimplePDImage = true;
			}
		}
		result += isShowPDImage + "," + isShowSimplePDImage;
		return result;
	}

	/**
	 * 获取所有部门以及部门下的部门的MAP集合，格式[部门id，部门信息]
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 1:57:55 PM
	 * @return Map<String,Organization>
	 */
	public Map<String, Organization> getAllDeparmentMaps() {
		Map<String, Organization> orgsmap = new HashMap<String, Organization>();
		/* 将org信息存放在orgsmap中，便于查询 */
		List<Organization> orgs = userService.getAllDeparments();
		String orgId = null;
		for (Organization org : orgs) {
			orgId = org.getOrgId();
			if (!orgsmap.containsKey(orgId)) {
				orgsmap.put(orgId, org);
			}
		}
		return orgsmap;
	}

	/**
	 * 获取的用户信息(用户数量过多情况下，不建议使用该方法)
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 29, 2011 10:49:57 AM
	 * @return Map<String,UserBeanTemp> 数据结构：userid【Map】用户信息
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public Map<String, UserBeanTemp> getAllUserIdMapUserBeanTemp() {
		Map<String, UserBeanTemp> map = null;
		List<Object[]> list = customUserService.getAllUserInfo();
		if (list != null && !list.isEmpty()) {
			map = new HashMap<String, UserBeanTemp>();
			for (int i = 0; i < list.size(); i++) {
				UserBeanTemp userBeanTemp = new UserBeanTemp();
				Object[] obj = list.get(i);
				userBeanTemp.setUserId(obj[0].toString());
				userBeanTemp.setUserName(obj[1].toString());
				userBeanTemp.setOrgId(obj[2].toString());
				userBeanTemp.setOrgName(obj[3].toString());
				map.put(userBeanTemp.getUserId(), userBeanTemp);
			}
		}
		return map;
	}

	/**
	 * 获取的用户信息
	 * 
	 * @author 严建
	 * @param userIds
	 *            一组用户id
	 * @return return Map<String,UserBeanTemp> 数据结构：userid【Map】用户信息
	 * @createTime Feb 7, 2012 1:15:45 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, UserBeanTemp> getUserIdMapUserBeanTemp(
			List<String> userIds) {
		Map<String, UserBeanTemp> map = null;
		List<Object[]> list = customUserService.getUsersInfo(userIds);
		if (list != null && !list.isEmpty()) {
			map = new HashMap<String, UserBeanTemp>();
			for (int i = 0; i < list.size(); i++) {
				UserBeanTemp userBeanTemp = new UserBeanTemp();
				Object[] obj = list.get(i);
				userBeanTemp.setUserId(obj[0].toString());
				userBeanTemp.setUserName(obj[1].toString());
				userBeanTemp.setOrgId(obj[2].toString());
				userBeanTemp.setOrgName(obj[3].toString());
				userBeanTemp.setRest1(StringUtil.castString(obj[4]));
				map.put(userBeanTemp.getUserId(), userBeanTemp);
			}
		}
		return map;
	}

	/**
	 * 得到当前用户所在机构下的用户列表的MAP集合，格式[用户id，用户信息]
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 2:03:53 PM
	 * @return Map<String,User>
	 */
	public Map<String, User> getAllUserInfoByHaMap() {
		Map<String, User> usersmap = new HashMap<String, User>();
		/* 将org信息存放在orgsmap中，便于查询 */
		List<User> users = userService.getAllUserInfoByHa();
		String userId = null;
		for (User user : users) {
			userId = user.getUserId();
			if (!usersmap.containsKey(userId)) {
				usersmap.put(userId, user);
			}
		}
		return usersmap;
	}

	/**
	 * 获取上一步处理人信息（不建议使用）
	 * 
	 * @description
	 * @param taskId
	 *            任务实例id
	 * @author 严建
	 * @createTime Dec 24, 2011 2:38:34 PM
	 * @return String
	 */
	public String getPreTaskActorInfo(String taskId) {
		String[] taskActorAndDeptInfo = getPreTaskActorAndDeptInfo(taskId,
				null, null);
		String[] PreTaskActor = taskActorAndDeptInfo[0].split(",");
		String[] PreTaskActorOrgName = taskActorAndDeptInfo[1].split(",");
		String PreTaskActorInfo = "";
		for (int i = 0; i < PreTaskActor.length; i++) {
			PreTaskActorInfo = PreTaskActor[i] + "[" + PreTaskActorOrgName[i]
					+ "],";
		}

		if (!PreTaskActorInfo.equals("")) {
			PreTaskActorInfo = PreTaskActorInfo.substring(0, PreTaskActorInfo
					.length() - 1);
			PreTaskActorInfo = "  \n上步办理人:" + PreTaskActorInfo;
		}
		return PreTaskActorInfo;
	}

	/**
	 * 获取上一步处理人信息(桌面显示)
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 28, 2011 3:43:46 PM
	 * @return String
	 */
	public String getPreTaskActorInfo(TaskBeanTemp taskBeanTemp) {
		String PreTaskActor = taskBeanTemp.getTaskActorName();
		String PreTaskActorOrgName = taskBeanTemp.getOrgName();
		String PreTaskActorInfo = "";

		if (PreTaskActor != null && !PreTaskActor.equals("")) {
			PreTaskActorInfo = "  \n上步办理人员: "
				+ PreTaskActor;
			if(" ".equals(PreTaskActorOrgName)){
				
			}else{
				PreTaskActorInfo = PreTaskActorInfo+ "["
				+ PreTaskActorOrgName
				+ "] ";
			}
			PreTaskActorInfo = PreTaskActorInfo+"  \n上步办理时间: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.format(taskBeanTemp.getStart() == null ? new Date()
									: taskBeanTemp.getStart());
		}
		return PreTaskActorInfo;
	}

	/**
	 * 根据一组任务id获取一组上一任务的办理信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 28, 2011 3:44:48 PM
	 * @return Map<String,TaskBeanTemp>
	 */
	public Map<String, TaskBeanTemp> getPreTaskBeanTempsMap(
			List<String> taskList) {
		Map<String, TaskBeanTemp> map = new HashMap<String, TaskBeanTemp>();
		Map<String, User> usersMap = new HashMap<String, User>();
		Map<String, Organization> orgsMap = new HashMap<String, Organization>();
		for (int i = 0; i < taskList.size(); i++) {
			String taskId = taskList.get(i);
			TaskBeanTemp taskBeanTemp = genPreTaskBeanTemp(taskId, usersMap,
					orgsMap);
			map.put(taskId, taskBeanTemp);
		}
		return map;

	}

	/**
	 * 根据任务id获取上一任务的办理信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 28, 2011 3:39:57 PM
	 * @return TaskBeanTemp
	 */
	public TaskBeanTemp genPreTaskBeanTemp(String taskId,
			Map<String, User> usersMap, Map<String, Organization> orgsMap) {
		TaskBeanTemp taskBeanTemp = new TaskBeanTemp();
		TaskInstance taskInstanceTemp = new TaskInstance();
		TaskInstance CurTaskBean = workflow.getTaskInstanceById(taskId);
		if (CurTaskBean.getTruePreTaskinstance() == null) {
			/**
			 * 当前任务不存在上一个任务时， 上一步办理人为当前任务的办理人， 上一步办理时间为当前任务的开始时间
			 */
			if (CurTaskBean.getFetchTasks() == null
					|| "".equals(CurTaskBean.getFetchTasks())) {
				taskInstanceTemp = workflow.getTaskInstanceById(taskId);
				taskBeanTemp.setStart(taskInstanceTemp.getStart());//
			} else {
				/**
				 * author yanjian 2012-02-15 11:31 Bug序号： 0000001078
				 * 查看待签收列表中的上步处理人(鼠标移动到标题上) 显示的处理人为当前登录的用户，而不是上步处理人(上一步一般是父流程)
				 */
				String[] fetchTasks = CurTaskBean.getFetchTasks().split(",");
				String pertaskid = fetchTasks[fetchTasks.length - 1];
				taskInstanceTemp = workflow.getTaskInstanceById(pertaskid);
				taskBeanTemp.setStart(taskInstanceTemp.getEnd());
			}
		} else {
			/**
			 * 当前任务存在上一个任务时， 上一步办理人为上一个任务的办理人， 上一步办理时间为上一个任务的结束时间
			 * 如果存在多个TruePreTaskinstance，取最后一个
			 * 
			 */

			String[] truePreTaskinstance = CurTaskBean.getTruePreTaskinstance()
					.split(",");
			taskInstanceTemp = workflow
					.getTaskInstanceById(truePreTaskinstance[truePreTaskinstance.length - 1]
							.toString());
			taskBeanTemp.setStart(taskInstanceTemp.getEnd());
		}
		String userId = taskInstanceTemp.getActorId();
		if (userId == null) {
			userId = userService.getCurrentUser().getUserId();
		}
		User user = null;
		if (!usersMap.containsKey(userId)) {
			user = userService.getUserInfoByUserId(userId);
			usersMap.put(userId, user);
		} else {
			user = usersMap.get(userId);
		}
		if (user == null) {
			userId = userService.getCurrentUser().getUserId();
			user = usersMap.get(userId);
		}
		taskBeanTemp.setTaskActorId(user.getUserId());
		taskBeanTemp.setTaskActorName(user.getUserName());
		taskBeanTemp.setOrgId(user.getOrgId());
		String orgId = user.getOrgId();
		Organization org = null;
		if (!orgsMap.containsKey(orgId)) {
			org = userService.getDepartmentByOrgId(orgId);
			orgsMap.put(orgId, org);
		} else {
			org = orgsMap.get(orgId);
		}
		taskBeanTemp.setOrgName(org.getOrgName());
		return taskBeanTemp;
	}

	/**
	 * 获取任务对应的上一步处理人信息
	 * 
	 * @author 严建
	 * @param taskIdList
	 * @param AllUserIdMapUserBeanTemp
	 * @return
	 * @createTime Feb 7, 2012 1:13:30 PM
	 */
	public Map<String, TaskBeanTemp> getTaskIdMapPreTaskBeanTemp(
			List<Long> taskIdList,
			Map<String, UserBeanTemp> AllUserIdMapUserBeanTemp) {
		Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = new HashMap<String, TaskBeanTemp>();
		for (int i = 0; i < taskIdList.size(); i++) {
			String taskId = taskIdList.get(i).toString().toString();
			TaskBeanTemp taskBeanTemp = new TaskBeanTemp();
			TaskInstance taskInstanceTemp = new TaskInstance();
			TaskInstance CurTaskBean = workflow.getTaskInstanceById(taskId);
			if (CurTaskBean.getTruePreTaskinstance() == null) {
				/**
				 * 当前任务不存在上一个任务时， 上一步办理人为当前任务的办理人， 上一步办理时间为当前任务的开始时间
				 */
				if (CurTaskBean.getFetchTasks() == null
						|| "".equals(CurTaskBean.getFetchTasks())) {
					taskInstanceTemp = workflow.getTaskInstanceById(taskId);
					taskBeanTemp.setStart(taskInstanceTemp.getStart());//
				} else {
					/**
					 * author yanjian 2012-02-15 11:31 Bug序号： 0000001078
					 * 查看待签收列表中的上步处理人(鼠标移动到标题上)
					 * 显示的处理人为当前登录的用户，而不是上步处理人(上一步一般是父流程)
					 */
					String[] fetchTasks = CurTaskBean.getFetchTasks()
							.split(",");
					String pertaskid = fetchTasks[fetchTasks.length - 1];
					taskInstanceTemp = workflow.getTaskInstanceById(pertaskid);
					taskBeanTemp.setStart(taskInstanceTemp.getEnd());
				}
			} else {
				/**
				 * 当前任务存在上一个任务时， 上一步办理人为上一个任务的办理人， 上一步办理时间为上一个任务的结束时间
				 * 如果存在多个TruePreTaskinstance，取最后一个
				 * 
				 */

				String[] truePreTaskinstance = CurTaskBean
						.getTruePreTaskinstance().split(",");
				taskInstanceTemp = workflow
						.getTaskInstanceById(truePreTaskinstance[truePreTaskinstance.length - 1]
								.toString());
				taskBeanTemp.setStart(taskInstanceTemp.getEnd());
			}
			String userId = taskInstanceTemp.getActorId();
			if (userId == null) {
				userId = userService.getCurrentUser().getUserId();
			}
			UserBeanTemp UserBeanTemp = AllUserIdMapUserBeanTemp.get(userId);
			if (UserBeanTemp == null) {
				userId = userService.getCurrentUser().getUserId();
				UserBeanTemp = AllUserIdMapUserBeanTemp.get(userId);
			}
			taskBeanTemp.setTaskActorId(UserBeanTemp.getUserId());
			taskBeanTemp.setTaskActorName(UserBeanTemp.getUserName());
			taskBeanTemp.setOrgId(UserBeanTemp.getOrgId());
			taskBeanTemp.setOrgName(UserBeanTemp.getOrgName());
			TaskIdMapPreTaskBeanTemp.put(taskId, taskBeanTemp);
		}
		return TaskIdMapPreTaskBeanTemp;
	}

	
	/**
	 * 
	 * 获取前一步处理人信息（兼容退回时无法准确显示上一步处理人）
	 * 
	 * @description
	 * @author 严建
	 * @param taskIdList
	 * @return
	 * @createTime Apr 10, 2012 10:06:41 PM
	 */
	public Map<String, TaskBeanTemp> getTaskIdMapPreTaskBeanTemper(List<Long> taskIdList){
		Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = null;
		if (taskIdList != null && !taskIdList.isEmpty()) {
			List<String> userIds = null;
			for (int i = 0; i < taskIdList.size(); i++) {

				if (TaskIdMapPreTaskBeanTemp == null) {
					TaskIdMapPreTaskBeanTemp = new HashMap<String, TaskBeanTemp>();
				}
				if (userIds == null) {
					userIds = new ArrayList<String>();
				}
				String taskId = taskIdList.get(i).toString().toString();
				TaskBeanTemp taskBeanTemp = new TaskBeanTemp();
				boolean flag = false;
				BackInfoBean backInfoBean = null;
				if(workflow.isBackTaskByTid(taskId)){
				    backInfoBean = workflowConstService.getBackInfoBean(taskId);
				    if(backInfoBean != null){
					flag = true;
				    }
				}
				if(flag){//如果是退回任务且存在退回信息
					    taskBeanTemp.setStart(backInfoBean.getBackTime());
					    taskBeanTemp.setTaskActorId(backInfoBean.getUserId());
					    taskBeanTemp.setTaskActorName(backInfoBean.getUserName());
					    taskBeanTemp.setOrgId(backInfoBean.getOrgId());
					    taskBeanTemp.setOrgName(backInfoBean.getOrgName());
				}else{//不是退回任务
					List<TaskInstanceBean> lists = workflow.getTruePreTaskInfosByTiId(taskId);
					if(lists == null || lists.isEmpty()){
						User user = userService.getCurrentUser();
						TaskInstance taskInstanceTemp = workflow.getTaskInstanceById(taskId);
						Long preTaskInstanceId= taskInstanceTemp.getPreTaskInstance();
						if(preTaskInstanceId != null){//存在前一步任务id
							TaskInstance preTaskInstance = workflow.getTaskInstanceById(preTaskInstanceId+"");
							taskBeanTemp.setStart(preTaskInstance.getEnd());
							taskBeanTemp.setTaskActorId(preTaskInstance.getActorId());
							taskBeanTemp.setNodeName(preTaskInstance.getNodeName());
						}else{//不存在前一步任务id
							taskBeanTemp.setStart(taskInstanceTemp.getStart());
							taskBeanTemp.setTaskActorId(user.getUserId());
							taskBeanTemp.setTaskActorName(user.getUserName());
						}
					}else{
						TaskInstanceBean lastest = null;
						for(TaskInstanceBean taskinstancebean:lists){
							if(lastest == null){
								lastest = taskinstancebean;
							}else{
								Date endDate = workflow
								.getTaskInstanceById(taskinstancebean.getTaskId()+"").getEnd();
								Date lastestendDate = workflow
								.getTaskInstanceById(lastest.getTaskId()+"").getEnd();
								if(endDate.after(lastestendDate)){
									lastest = taskinstancebean;
								}
							}
						}
						TaskInstanceBean taskinstancebean = lastest;
						taskBeanTemp.setTaskActorId(taskinstancebean.getTaskActorId());
						taskBeanTemp.setTaskActorName(taskinstancebean.getTaskActorName());
						taskBeanTemp.setNodeName(taskinstancebean.getTaskName());
						taskBeanTemp.setStart(workflow
								.getTaskInstanceById(taskinstancebean.getTaskId()+"").getEnd());
					}
					userIds.add(taskBeanTemp.getTaskActorId());//无法直接查询出处室信息，需要通过另外的方式进行数据查询
				}
				TaskIdMapPreTaskBeanTemp.put(taskId, taskBeanTemp);
			}
			if (userIds != null && !userIds.isEmpty()) {
				Map<String, UserBeanTemp> map = getUserIdMapUserBeanTemp(userIds);//获取用户信息
				if(map == null){
					for(String oriUserId:userIds){
						if(map == null){
							map =  new HashMap<String, UserBeanTemp>();
						}
						if(!map.containsKey(oriUserId)){
							Organization org = userService.getUserDepartmentByUserId(oriUserId);
							User user = userService.getUserInfoByUserId(oriUserId);
							UserBeanTemp userbeantemp = new UserBeanTemp();
							userbeantemp.setUserId(oriUserId);
							userbeantemp.setUserName(user.getUserName());
							userbeantemp.setOrgId(org.getOrgId());
							userbeantemp.setOrgName(org.getOrgName());
							userbeantemp.setRest1(user.getRest1());
							map.put(oriUserId, userbeantemp);
						}
					}
				}
				if (map != null && !map.isEmpty()) {
					List<String> keys = new ArrayList<String>(
							TaskIdMapPreTaskBeanTemp.keySet());//获取所有的taskid
					for (int i = 0; i < keys.size(); i++) {
						String key = keys.get(i);
						TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp
								.get(key);
						String userId = taskBeanTemp.getTaskActorId();
						if(map.containsKey(userId)){//只对那么添加到列表userIds中的数据进行匹配
							UserBeanTemp userBean = map.get(userId);
							if (userBean == null) {
								userId = userService.getCurrentUser().getUserId();
								userBean = map.get(userId);
							}
							/*设置处室信息*/
							taskBeanTemp.setTaskActorName(userBean.getUserName());
							taskBeanTemp.setOrgId(userBean.getOrgId());
							if("1".equals(userBean.getRest1())){
								taskBeanTemp.setOrgName(" ");
							}else{
								taskBeanTemp.setOrgName(userBean.getOrgName());
							}
							taskBeanTemp.setRest1(userBean.getRest1());
							TaskIdMapPreTaskBeanTemp.put(key, taskBeanTemp);
						}
					}
				}
			}
		}
		return TaskIdMapPreTaskBeanTemp;
	}
	
	/**
	 * 获取任务对应的上一步处理人信息
	 * 
	 * @author 严建
	 * @param taskIdList
	 * @param AllUserIdMapUserBeanTemp
	 * @return
	 * @createTime Feb 7, 2012 1:13:30 PM
	 * @see {@link #getTaskIdMapPreTaskBeanTemp(List)}
	 */
	@Deprecated
	public Map<String, TaskBeanTemp> getTaskIdMapPreTaskBeanTemp(
			List<Long> taskIdList) {
		Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = null;
		if (taskIdList != null && !taskIdList.isEmpty()) {
			List<String> userIds = null;
			for (int i = 0; i < taskIdList.size(); i++) {

				if (TaskIdMapPreTaskBeanTemp == null) {
					TaskIdMapPreTaskBeanTemp = new HashMap<String, TaskBeanTemp>();
				}
				if (userIds == null) {
					userIds = new ArrayList<String>();
				}
				String taskId = taskIdList.get(i).toString().toString();
				TaskBeanTemp taskBeanTemp = new TaskBeanTemp();
				TaskInstance taskInstanceTemp = new TaskInstance();
				TaskInstance CurTaskBean = workflow.getTaskInstanceById(taskId);
				if (CurTaskBean.getTruePreTaskinstance() == null) {
					/**
					 * 当前任务不存在上一个任务时， 上一步办理人为当前任务的办理人， 上一步办理时间为当前任务的开始时间
					 */
					if (CurTaskBean.getFetchTasks() == null
							|| "".equals(CurTaskBean.getFetchTasks())) {
						taskInstanceTemp = workflow.getTaskInstanceById(taskId);
						taskBeanTemp.setStart(taskInstanceTemp.getStart());//
					} else {
						/**
						 * author yanjian 2012-02-15 11:31 Bug序号： 0000001078
						 * 查看待签收列表中的上步处理人(鼠标移动到标题上)
						 * 显示的处理人为当前登录的用户，而不是上步处理人(上一步一般是父流程)
						 */
						String[] fetchTasks = CurTaskBean.getFetchTasks()
								.split(",");
						String pertaskid = fetchTasks[fetchTasks.length - 1];
						taskInstanceTemp = workflow
								.getTaskInstanceById(pertaskid);
						taskBeanTemp.setStart(taskInstanceTemp.getEnd());
					}
				} else {
					/**
					 * 当前任务存在上一个任务时， 上一步办理人为上一个任务的办理人， 上一步办理时间为上一个任务的结束时间
					 * 如果存在多个TruePreTaskinstance，取最后一个
					 * 
					 */

					String[] truePreTaskinstance = CurTaskBean
							.getTruePreTaskinstance().split(",");
					taskInstanceTemp = workflow
							.getTaskInstanceById(truePreTaskinstance[truePreTaskinstance.length - 1]
									.toString());
					taskBeanTemp.setStart(taskInstanceTemp.getEnd());
				}
				String userId = taskInstanceTemp.getActorId();
				if (userId == null) {
					userId = userService.getCurrentUser().getUserId();
				}
				userIds.add(userId);
				taskBeanTemp.setTaskActorId(userId);
				TaskIdMapPreTaskBeanTemp.put(taskId, taskBeanTemp);
			}
			if (userIds != null && !userIds.isEmpty()) {
				Map<String, UserBeanTemp> map = getUserIdMapUserBeanTemp(userIds);
				if (map != null && !map.isEmpty()) {
					List<String> keys = new ArrayList<String>(
							TaskIdMapPreTaskBeanTemp.keySet());
					for (int i = 0; i < keys.size(); i++) {
						String key = keys.get(i);
						TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp
								.get(key);
						String userId = taskBeanTemp.getTaskActorId();
						UserBeanTemp userBean = map.get(userId);
						if (userBean == null) {
							userId = userService.getCurrentUser().getUserId();
							userBean = map.get(userId);
						}
						taskBeanTemp.setTaskActorName(userBean.getUserName());
						System.out.println();
						taskBeanTemp.setOrgId(userBean.getOrgId());
						taskBeanTemp.setOrgName(userBean.getOrgName());
						TaskIdMapPreTaskBeanTemp.put(key, taskBeanTemp);
					}
				}
			}
		}
		return TaskIdMapPreTaskBeanTemp;
	}

	/**
	 * 获取上一步处理人名称及所在部门名称
	 * 
	 * @description
	 * @author 严建
	 * @param taskId
	 *            任务实例id
	 * @createTime Dec 25, 2011 10:47:17 AM
	 * @return String[]
	 */
	@SuppressWarnings("unchecked")
	public String[] getPreTaskActorAndDeptInfo(String taskId,
			Map<String, Organization> orgsmap, Map<String, User> usersmap) {
		String[] taskActorAndDeptInfo = new String[2];
		if (orgsmap == null) {
			orgsmap = this.getAllDeparmentMaps();
		}
		if (usersmap == null) {
			usersmap = this.getAllUserInfoByHaMap();
		}
		String PreTaskActor = "";
		String PreTaskActorOrgName = "";
		List<TaskInstanceBean> list = workflow
				.getTruePreTaskInfosByTiId(taskId);
		for (int ii = 0; ii < list.size(); ii++) {
			TaskInstanceBean taskInstanceBean = list.get(ii);
			PreTaskActor = taskInstanceBean.getTaskActorName();
			String PreTaskActorid = taskInstanceBean.getTaskActorId();
			String deptid = usersmap.get(PreTaskActorid).getOrgId();
			PreTaskActorOrgName = orgsmap.get(deptid).getOrgName();
			PreTaskActor = PreTaskActor + ",";
			PreTaskActorOrgName = PreTaskActorOrgName + ",";
		}
		if (!PreTaskActor.equals("")) {
			PreTaskActor = PreTaskActor.substring(0, PreTaskActor.length() - 1);
			PreTaskActorOrgName = PreTaskActorOrgName.substring(0,
					PreTaskActorOrgName.length() - 1);
		} else {
			PreTaskActor = userService.getCurrentUser().getUserName();
			PreTaskActorOrgName = orgsmap.get(
					userService.getCurrentUser().getOrgId()).getOrgName();
		}
		taskActorAndDeptInfo[0] = PreTaskActor;
		taskActorAndDeptInfo[1] = PreTaskActorOrgName;
		return taskActorAndDeptInfo;
	}

	/**
	 * 获取上一步处理人信息
	 * 
	 * @description
	 * @param taskId
	 *            任务实例id
	 * @param orgsmap
	 *            用户信息map
	 * @param usersmap
	 *            部门信息map
	 * @author 严建
	 * @createTime Dec 24, 2011 2:40:26 PM
	 * @return String
	 */
	public String getPreTaskActorInfo(String taskId,
			Map<String, Organization> orgsmap, Map<String, User> usersmap) {
		String[] taskActorAndDeptInfo = getPreTaskActorAndDeptInfo(taskId,
				orgsmap, usersmap);
		String[] PreTaskActor = taskActorAndDeptInfo[0].split(",");
		String[] PreTaskActorOrgName = taskActorAndDeptInfo[1].split(",");
		String PreTaskActorInfo = "";
		for (int i = 0; i < PreTaskActor.length; i++) {
			PreTaskActorInfo = PreTaskActor[i] + "[" + PreTaskActorOrgName[i]
					+ "],";
		}

		if (!PreTaskActorInfo.equals("")) {
			PreTaskActorInfo = PreTaskActorInfo.substring(0, PreTaskActorInfo
					.length() - 1);
			PreTaskActorInfo = "  上步办理人:" + PreTaskActorInfo;
		}
		return PreTaskActorInfo;
	}

	/**
	 * 获取所有的流程定义文件
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 4:38:08 PM
	 * @return List<TwfBaseProcessfile>
	 */
	@SuppressWarnings("unchecked")
	public List<TwfBaseProcessfile> getAllProcessFilesInfo() {
		List<TwfBaseProcessfile> twfBaseProcessfileList = workflow
				.getDataByHql("from TwfBaseProcessfile t", new Object[] {});
		return twfBaseProcessfileList;
	}

	/**
	 * 根据流程定义名称获取流程定义信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 25, 2011 6:09:38 PM
	 * @return TwfBaseProcessfile
	 */
	@SuppressWarnings("unchecked")
	public TwfBaseProcessfile getProcessfileFileByPfName(String pfName) {
		TwfBaseProcessfile processFile = null;
		List<TwfBaseProcessfile> twfBaseProcessfileList = workflow
				.getDataByHql(" from TwfBaseProcessfile t where t.pfName = ?",
						new Object[] { pfName });
		processFile = twfBaseProcessfileList.get(0);
		return processFile;
	}

	/**
	 * 获取流程别名信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 28, 2011 6:53:28 PM
	 * @return List<Object[]>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getProcessfileFileByPfName(Object[] pfNames) {
		List<Object[]> twfBaseProcessfileList = new ArrayList<Object[]>();
		if (pfNames != null && pfNames.length != 0) {
			StringBuilder param = new StringBuilder();
			for (int i = 0; i < pfNames.length; i++) {
				param.append("?,");
			}
			twfBaseProcessfileList = workflow.getDataByHql(
					"select t.pfName,t.rest2 from TwfBaseProcessfile t where t.pfName in ("
							+ param.deleteCharAt(param.length() - 1) + ")",
					pfNames);
		}
		return twfBaseProcessfileList;
	}

	/**
	 * 获取流程别名信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 28, 2011 7:00:01 PM
	 * @return Map<String,String>
	 */
	public Map<String, String> getPfNameMapRest2(Object[] pfNames) {
		Map<String, String> map = new HashMap<String, String>();
		List<Object[]> twfBaseProcessfileList = getProcessfileFileByPfName(pfNames);
		for (int i = 0; i < twfBaseProcessfileList.size(); i++) {
			Object[] info = twfBaseProcessfileList.get(i);
			map.put(info[0].toString(), info[1] == null ? "" : info[1]
					.toString());
		}
		return map;
	}

	/**
	 * 获取所有的流程定义文件，名称作为主键
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 4:28:16 PM
	 * @return Map<String,Object[]>
	 */
	public Map<String, TwfBaseProcessfile> getAllProcessFilesNameAsKeyMap() {
		Map<String, TwfBaseProcessfile> map = new HashMap<String, TwfBaseProcessfile>();
		List<TwfBaseProcessfile> list = this.getAllProcessFilesInfo();
		for (TwfBaseProcessfile objs : list) {
			if (!map.containsKey(objs.getPfName())) {
				map.put(objs.getPfName(), objs);
			}
		}
		return map;
	}

	/**
	 * 获取所有的流程定义文件，id作为主键
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 4:28:16 PM
	 * @return Map<String,Object[]>
	 */
	public Map<String, TwfBaseProcessfile> getAllProcessFilesNameAsIdMap() {
		Map<String, TwfBaseProcessfile> map = new HashMap<String, TwfBaseProcessfile>();
		List<TwfBaseProcessfile> list = this.getAllProcessFilesInfo();
		for (TwfBaseProcessfile objs : list) {
			if (!map.containsKey(objs.getPfId())) {
				map.put(objs.getPfId().toString(), objs);
			}
		}
		return map;
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
		if (filterSign == null || "".equals(filterSign)) {
			filterSign = "0";
		}
		Object[] sItems = { "processName", "processMainFormId",
				"startUserName", "processInstanceId", "businessName",
				"processStartDate", "processEndDate","startUserId" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();

		if (SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC.equals(sortType)) {
			orderMap.put("processStartDate", "0");
		} else {
			orderMap.put("processStartDate", "1");
		}
		String userId = userService.getCurrentUser().getUserId();
		StringBuilder customSelectItems = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customQuery = new StringBuilder();
		customSelectItems
				.append("jbpmbusiness.END_TIME,jbpmbusiness.BUSINESS_TYPE,pi.TYPE_ID_,pi.BUSINESS_ID");
		customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
//		 增加按流程类型检索的方式
		HttpServletRequest request = ServletActionContext.getRequest();
		String workflowType = request.getParameter("workflowType");
		if (workflowType != null && workflowType.length() > 0) {
			customQuery.append(" and pi.TYPE_ID_ in(" + workflowType + ")");
		}
		// 增加按流程类型反选检索方式
		String excludeWorkflowType = request
				.getParameter("excludeWorkflowType");
		if (excludeWorkflowType != null && excludeWorkflowType.length() > 0) {
			customQuery.append(" and pi.TYPE_ID_ not in(" + excludeWorkflowType
					+ ")");
		}
		// 增加按流程类型名称反选检索方式,过滤掉该流程类型名称的数据
		String excludeWorkflowTypeName = request
				.getParameter("excludeWorkflowTypeName");
		if (excludeWorkflowTypeName != null && excludeWorkflowTypeName.length() > 0) {
			customQuery.append(" and pi.NAME_ not like '%"
					+ excludeWorkflowTypeName +"%' ");
		}
		// paramsMap.put("processSuspend", "0");// 取非挂起任务
		customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
		customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
		ProcessedParameter processedParameter = new ProcessedParameter();
		processedParameter.setFilterSign(filterSign);
		processedParameter.setUserId(userId);
		processedParameter.setCustomFromItems(customFromItems);
		processedParameter.setCustomQuery(customQuery);
		processedParameter.setQueryWithTaskDate(null);
		manager.initProcessedFilterSign(processedParameter);
		logger.info(customFromItems + "\n" + customQuery);
		Page page = new Page(num, true);
		page = workflow.getProcessInstanceByConditionForPage(page,
				toSelectItems, paramsMap, orderMap, customSelectItems
						.toString(), customFromItems.toString(), customQuery
						.toString(), null, null);
		manager.destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
				customSelectItems, customFromItems, customQuery, null, null);
		List dlist = (page.getResult() == null ? new ArrayList() : page
				.getResult());
		List<TaskBusinessBean> beans = new LinkedList<TaskBusinessBean>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < dlist.size(); i++) {
			Object[] obj = (Object[]) dlist.get(i);
			TaskBusinessBean taskbusinessbean = new TaskBusinessBean();
			taskbusinessbean.setWorkflowName((String) obj[0]);
			taskbusinessbean.setFormId((String) obj[1]);
			taskbusinessbean.setStartUserName((String) obj[2]);
			taskbusinessbean.setInstanceId(obj[3].toString());
			taskbusinessbean.setBusinessName((String) (obj[4]==null?"":obj[4]));
//			taskbusinessbean.setWorkflowStartDate((Date) obj[5]);
//			taskbusinessbean.setWorkflowEndDate((Date) obj[6]);
			try {
				taskbusinessbean.setWorkflowStartDate(obj[5]==null?null:sdf.parse(StringUtil
						.castString(obj[5])));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				try {
					taskbusinessbean.setWorkflowEndDate(obj[6]==null?null:sdf.parse(StringUtil
										.castString(obj[6])));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			taskbusinessbean.setStartUserId(StringUtil.castString(obj[7]));
			taskbusinessbean.setEndTime(StringUtil.castString(obj[8]));
			taskbusinessbean.setBusinessType(StringUtil.castString(obj[9]));
			taskbusinessbean.setWorkflowType(StringUtil.castString(obj[10]));
			taskbusinessbean.setBsinessId(StringUtil.castString(obj[11]));
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

		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
			StringBuilder sql = new StringBuilder(
					"select ti.procinst_,ti.id_ as taskId,ti.start_ as taskStartDate ,ti.name_ as taskName ");
			sql.append(" from JBPM_TASKINSTANCE ti where ti.procinst_ in(");
			sql.append(params.toString());
			sql.append(")  AND  TI.ACTORID_='"+processedParameter.getUserId()+"' AND TI.END_ IS NOT NULL  order by ti.END_ desc");
			List<Map<String, Object>> taskList = jdbcTemplate.queryForList(sql
					.toString());
			logger.info(sql.toString());
			if (taskList != null && !taskList.isEmpty()) {
				map = new HashMap<String, Map>();
				for (Map<String, Object> rsMap : taskList) {
					if (!map
							.containsKey(String.valueOf(rsMap.get("PROCINST_")))) {
						map.put(String.valueOf(rsMap.get("PROCINST_")), rsMap);
					}
				}
			}
		}

		Map<String, String> pfNameMapRest2 = getPfNameMapRest2(pfNames);
		/* 所有的用户信息 */
		Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
		for (int i = 0; i < beans.size(); i++) {
			TaskBusinessBean taskbusinessbean = beans.get(i);
			if(taskbusinessbean.getWorkflowEndDate() != null){
				
			}else{
				String nodeName = StringUtil.castString(map.get(
						taskbusinessbean.getInstanceId()).get("TASKNAME"));
				if("意见征询".equals(nodeName)){
					taskbusinessbean.setCurTaskActorInfo("意见征询");
				}else{
					List[] listTemp = this
					.getUserBeanTempArrayOfProcessStatusByPiId(
							taskbusinessbean.getInstanceId(),
							AllUserIdMapUserBeanTem);
					StringBuilder cruuentUser = new StringBuilder();
					for (int index = 0; index < listTemp[0].size(); index++) {
						if(" ".equals(listTemp[1].get(index).toString())){
							cruuentUser.append(","
									+ listTemp[0].get(index).toString());
						}else{
							cruuentUser.append(","
									+ listTemp[0].get(index).toString() + "("
									+ listTemp[1].get(index).toString() + ")");
						}
					}
					if(cruuentUser.length()>0){
						cruuentUser.deleteCharAt(0);
					}
					taskbusinessbean.setCurTaskActorInfo(cruuentUser.toString());
				}
			}
			String pfName = taskbusinessbean.getWorkflowName();
			String processFileRest2 = pfNameMapRest2.get(pfName);
			if (processFileRest2 == null || "".equals(processFileRest2)) {// 不存在别名是，别名即为流程名
				taskbusinessbean.setWorkflowAliaName(pfName);
			} else {
				taskbusinessbean.setWorkflowAliaName(processFileRest2);
			}
			if (map != null && !map.isEmpty()) {
				taskbusinessbean.setTaskId(map.get(
						taskbusinessbean.getInstanceId()).get("taskId")
						+ "");
			}
		}
		page.setResult(beans);
		return page;
	}

	/**
	 * 获取流程实例监控的用户信息（桌面显示）
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 29, 2011 3:13:46 PM
	 * @return StringBuilder
	 */
	@SuppressWarnings("unchecked")
	public StringBuilder getUserBeanTempInfoOfProcessStatusByPiId(
			String processInstanceId,
			Map<String, UserBeanTemp> AllUserIdMapUserBeanTem) {
		StringBuilder info = new StringBuilder();
		StringBuilder returninfo = new StringBuilder();
		List[] list = getUserBeanTempArrayOfProcessStatusByPiId(
				processInstanceId, AllUserIdMapUserBeanTem);
		List<String> userList = list[0];
		List<String> orgList = list[1];
		String orgName = "";
		String userName = "";
		for (int i = 0; i < userList.size(); i++) {
			userName = userList.get(i);
			orgName = orgList.get(i);
			if (userName.equals("")) {
				info.append(orgList.get(i) + ",");
			} else {
				info.append(userName + "(" + orgName + "),");
			}
		}
		info = info.deleteCharAt(info.length() - 1);
		returninfo.append("<span title=" + info + ">" + info + "</span>");
		return returninfo;
	}

	/**
	 * 获取流程实例监控的用户信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 29, 2011 4:38:38 PM
	 * @return List[]
	 */
	@SuppressWarnings("unchecked")
	public List[] getUserBeanTempArrayOfProcessStatusByPiId(
			String processInstanceId,
			Map<String, UserBeanTemp> AllUserIdMapUserBeanTem) {
		List[] listArray = new List[2];
		List<String> userNameList = new LinkedList<String>();
		List<String> orgNameList = new LinkedList<String>();
		List<UserBeanTemp> userBeanTempList = getUserBeanTempOfProcessStatusByPiId(
				processInstanceId, AllUserIdMapUserBeanTem);
		if (!userBeanTempList.isEmpty()) {
			for(UserBeanTemp userbeantemp:userBeanTempList){
				if(userbeantemp == null){
					
				}else{
					userNameList.add(userbeantemp.getUserName());
//					if(!orgNameList.contains(userbeantemp.getOrgName())){//不添加重复处室名称
					if("1".equals(userbeantemp.getRest1())){
						orgNameList.add(" ");
					}else{
						orgNameList.add(userbeantemp.getOrgName());
					}
//					}
				}
			}
		} else {
			List<Object[]> ss = workflow
					.getMonitorChildrenInstanceIds(new Long(processInstanceId));
			List<List<UserBeanTemp>> CListList = new LinkedList<List<UserBeanTemp>>();
			for (Object[] s : ss) {
				String prcessInstanceCId = s[0].toString();
				String workflowName = s[2].toString();
				if("省政府发文流程".equals(workflowName) || "办公厅发文流程".equals(workflowName)){//发文流程作为子流程时，在父流程查看时不显示发文当前处理人信息
					continue;
				}
				List<UserBeanTemp> userBeanTempCList = getUserBeanTempOfProcessStatusByPiId(
						prcessInstanceCId, AllUserIdMapUserBeanTem);
				CListList.add(userBeanTempCList);
			}
			String orgName = "";
			String userName = "";
			for (List<UserBeanTemp> CList : CListList) {
				if (!CList.isEmpty()) {
					for(UserBeanTemp userbeantemp:CList){
						userNameList.add(userbeantemp.getUserName());
//						if(!orgNameList.contains(userbeantemp.getOrgName())){//不添加重复处室名称
						if("1".equals(userbeantemp.getRest1())){
							orgNameList.add(" ");
						}else{
							orgNameList.add(userbeantemp.getOrgName());
						}
//						}
					}
				}
			}
			if (userNameList.isEmpty() && orgNameList.isEmpty()) {
				String userId = userService.getCurrentUser().getUserId();
				if(!AllUserIdMapUserBeanTem.containsKey(userId)){
					UserBeanTemp userBeanTemp = new UserBeanTemp();
					User user = userService.getUserInfoByUserId(userId);
					userBeanTemp.setUserId(userId);
					userBeanTemp.setUserName(user.getUserName());
					userBeanTemp.setOrgId(user.getOrgId());
					userBeanTemp.setOrgName(userService.getDepartmentByOrgId(user.getOrgId()).getOrgName());
					AllUserIdMapUserBeanTem.put(userId, userBeanTemp);
				}
				UserBeanTemp userBeanTemp = AllUserIdMapUserBeanTem.get(userId);
				userName = userBeanTemp.getUserName();
				orgName = userBeanTemp.getOrgName();
				if("1".equals(userBeanTemp.getRest1())){
					orgNameList.add(" ");
				}else{
					orgNameList.add(userBeanTemp.getOrgName());
				}
				userNameList.add(userName);
				orgNameList.add(orgName);
			}
		}
		listArray[0] = new ArrayList<String>(userNameList);
		listArray[1] = new ArrayList<String>(orgNameList);
		return listArray;
	}

	/**
	 * 获取流程实例监控的用户信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 29, 2011 2:24:57 PM
	 * @return List<UserBeanTemp>
	 */
	public List<UserBeanTemp> getUserBeanTempOfProcessStatusByPiId(
			String processInstanceId,
			Map<String, UserBeanTemp> AllUserIdMapUserBeanTem) {
		List<UserBeanTemp> list = new LinkedList<UserBeanTemp>();
		Object[] ds = workflow.getProcessStatusByPiId(processInstanceId);
		Collection col2 = (Collection) ds[6];// 处理任务信息
		if (col2 != null && !col2.isEmpty()) {
			for (Iterator it = col2.iterator(); it.hasNext();) {
				Object[] itObjs = (Object[]) it.next();
				String userId = (String) itObjs[3];
				if (userId != null && !"".equals(userId)) {
					String[] userIds = userId.split(",");
					for (String id : userIds) {
						if(!AllUserIdMapUserBeanTem.containsKey(id)){
							UserBeanTemp userBeanTemp = new UserBeanTemp();
							User user = userService.getUserInfoByUserId(id);
							userBeanTemp.setUserSequence(user.getUserSequence());
							userBeanTemp.setRest1(user.getRest1());
							userBeanTemp.setUserId(id);
							userBeanTemp.setUserName(user.getUserName());
							userBeanTemp.setOrgId(user.getOrgId());
							userBeanTemp.setOrgName(userService.getDepartmentByOrgId(user.getOrgId()).getOrgName());
							userBeanTemp.setOrgSequence(userService.getDepartmentByOrgId(user.getOrgId()).getOrgSequence());
							AllUserIdMapUserBeanTem.put(id, userBeanTemp);
						}
						list.add(AllUserIdMapUserBeanTem.get(id));
					}
				}
			}
		}
		
		if(list != null && !list.isEmpty() ){
		Collections.sort(list, new Comparator<UserBeanTemp>(){       
		    public int compare(UserBeanTemp f1, UserBeanTemp f2){    
		        if(f1.getOrgSequence()-f2.getOrgSequence()>0){    
		            return 1;    
		        }else if (f1.getOrgSequence()-f2.getOrgSequence()<0){    
		            return -1;    
		        }else{//Foo.a equal, then check Foo.b    
		            return (int) (f1.getUserSequence()-f2.getUserSequence());    
		        }    
		}});  
		}
		/*if(list != null && !list.isEmpty() ){
			Collections.sort(list, new Comparator<UserBeanTemp>() {
				public int compare(UserBeanTemp arg0, UserBeanTemp arg1) {
					Long key0 = arg0.getOrgSequence() == null ? Long.MAX_VALUE
							: arg0.getOrgSequence();
					Long key1 = arg1.getOrgSequence() == null ? Long.MAX_VALUE
							: arg1.getOrgSequence();
					return key0.compareTo(key1);
				}
			});
		}*/
		/*if(list != null && !list.isEmpty() ){
			Collections.sort(list, new Comparator<UserBeanTemp>() {
				public int compare(UserBeanTemp arg0, UserBeanTemp arg1) {
					Long key0 = arg0.getUserSequence() == null ? Long.MAX_VALUE
							: arg0.getUserSequence();
					Long key1 = arg1.getUserSequence() == null ? Long.MAX_VALUE
							: arg1.getUserSequence();
					return key0.compareTo(key1);
				}
			});
		}	*/
		return new ArrayList<UserBeanTemp>(list);
	}

	/**
	 * @description “待办事宜”中显示，且急件需要“标红、置顶”；
	 * @author 严建
	 * @createTime Dec 15, 2011 2:59:00 PM
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public Page<TaskBusinessBean> getTodoByRedType(String workflowType,
			String type, int num) {
		Page<TaskBusinessBean> page = manager.getTodoByRedType(workflowType,
				type, num);
		List<TaskBusinessBean> r = (page.getResult() == null ? new ArrayList<TaskBusinessBean>()
				: page.getResult());
		List<Long> taskIdList = new LinkedList<Long>();
		for (int i = 0; i < r.size(); i++) {
			taskIdList.add(new Long(r.get(i).getTaskId()));
		}
		Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = getTaskIdMapPreTaskBeanTemper(taskIdList);
		for (int i = 0; i < r.size(); i++) {
			TaskBusinessBean taskbusinessbean = r.get(i);
			TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp.get(taskbusinessbean.getTaskId());
			if("意见征询".equals(taskBeanTemp.getNodeName())){	// 处理意见征询的文
				ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
						.getBsinessId().split(";")[2]);
				taskbusinessbean
				.setPreTaskActorInfo("\n已征求完意见\n征询单位:" + (toayjzx == null ? ""
						: toayjzx.getUnit() == null ? "" : toayjzx
								.getUnit()));
			}else{
				 taskbusinessbean
					.setPreTaskActorInfo(getPreTaskActorInfo(taskBeanTemp));
			}
//			if ("0".equals(taskbusinessbean.getBusinessType())) {// 处理意见征询的文
//				ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
//						.getBsinessId().split(";")[2]);
//				taskbusinessbean
//						.setPreTaskActorInfo("\n已征求完意见\n征询单位:" + (toayjzx == null ? ""
//								: toayjzx.getUnit() == null ? "" : toayjzx
//										.getUnit()));
//			} else {
//				 taskBeanTemp = TaskIdMapPreTaskBeanTemp
//						.get(taskbusinessbean.getTaskId());
//				taskbusinessbean
//						.setPreTaskActorInfo(getPreTaskActorInfo(taskBeanTemp));
//			}
			r.set(i, taskbusinessbean);
		}
		page.setResult(r);
		return page;

	}

	/**
	 * 根据工作流返回信息构造生成excel的信息
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 27, 2011 11:08:38 AM
	 * @return List[]
	 */
	public List[] exportWorkExcel(Object[] objs) {
		List list = (List) objs[1];
		List showColumnList = (List) objs[0];
		List<String> titleList = new LinkedList<String>();
		List<String> columnNameList = new LinkedList<String>();
		List<String> typeList = new LinkedList<String>();

		Object[] recv_num = null;
		for (int i = 0; i < showColumnList.size(); i++) {
			Object[] column = (Object[]) showColumnList.get(i);
			String columnName = (String) column[0];
			if (columnName.toUpperCase().equals("RECV_NUM".toUpperCase())) {
				recv_num = column;
				showColumnList.remove(i);
				break;
			}
		}

		for (int i = 0; i < showColumnList.size(); i++) {
			if (i == 0) {
				if (recv_num != null) {
					String columnName = (String) recv_num[0];
					String caption = (String) recv_num[1];
					String type = (String) recv_num[2];// 字段类型
					titleList.add(caption);
					columnNameList.add(columnName);
					typeList.add(type);
				}
				continue;
			}
			Object[] column = (Object[]) showColumnList.get(i);
			String columnName = (String) column[0];
			String caption = (String) column[1];
			String type = (String) column[2];// 字段类型
			if (caption.equals("")) {
				continue;
			}
			titleList.add(caption);
			columnNameList.add(columnName);
			typeList.add(type);

		}
		List<Object[]> listobjs = new LinkedList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			Object[] objects = new Object[columnNameList.size()];
			for (int j = 0; j < columnNameList.size(); j++) {
				objects[j] = map.get(columnNameList.get(j));
			}
			listobjs.add(objects);
		}
		return new List[] { titleList, listobjs };
	}

	/**
	 * 生成Excel文件
	 * 
	 * @description
	 * @author 严建
	 * @param response
	 * @param titleList
	 * @param listobjs
	 * @param title
	 * @createTime Dec 27, 2011 10:59:49 AM
	 * @return void
	 */
	public void genExcelFile(HttpServletResponse response, List titleList,
			List listobjs, String title) {
		try {
			// 创建EXCEL对象
			BaseDataExportInfo export = new BaseDataExportInfo();
			String str = ProcessUtil.toUtf8String(title);
			export.setWorkbookFileName(str);
			export.setSheetTitle(title);
			export.setSheetName(title);
			// 描述行信息
			List<String> tableHead = new ArrayList<String>();
			for (int i = 0; i < titleList.size(); i++) {
				tableHead.add(titleList.get(i).toString());
			}
			export.setTableHead(tableHead);
			int columns = 0;
			// 获取导出信息
			List<Vector<String>> rowList = new ArrayList<Vector<String>>();
			Map rowhigh = new HashMap();
			for (int i = 0; i < listobjs.size(); i++) {
				Object[] objects = (Object[]) listobjs.get(i);
				Vector<String> cols = new Vector<String>();
				if (columns == 0) {
					columns = objects.length;
				}
				for (int j = 0; j < objects.length; j++) {
					if (objects[j] instanceof Date) {
						objects[j] = new SimpleDateFormat("yyyy-MM-dd")
								.format(objects[j]);
					}
					cols.add(objects[j] == null ? "" : objects[j].toString());
				}
				rowList.add(cols);
			}
			export.setRowList(rowList);
			export.setRowHigh(rowhigh);
			ProcessXSL xsl = new ProcessXSL();
			xsl.createWorkBookSheet(export);
			xsl.writeWorkBook(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据插件name获取插件的value
	 * 
	 * @author 严建
	 * @param plugin
	 * @param pluginName
	 * @return
	 * @createTime Feb 28, 2012 11:07:08 AM
	 */
	private String getNodesettingPluginValue(TwfBaseNodesetting nodeSetting,
			String pluginName) {
		if (pluginName != null && !"".equals(pluginName)) {
			Map plugins = nodeSetting.getPlugins();// 得到节点上的插件信息集合
			TwfBaseNodesettingPlugin plugin = (TwfBaseNodesettingPlugin) plugins
					.get(pluginName);
			if (plugin != null && plugin.getValue() != null) {
				return plugin.getValue();
			}
		}
		return null;
	}

	/**
	 * 设置request的attribute
	 * 
	 * @author 严建
	 * @param nodeSetting
	 * @param pluginName
	 *            插件名称
	 * @param attributeName
	 *            request的attribute的name
	 * @param defalut
	 *            对应属性名称的默认值（应用场合：当指定插件名称对应的值为空）
	 * @createTime Feb 28, 2012 11:23:09 AM
	 */
	private void setRequestAttribute(TwfBaseNodesetting nodeSetting,
			String pluginName, String attributeName, Object defalut) {
		ServletActionContext
				.getRequest()
				.setAttribute(
						attributeName,
						getNodesettingPluginValue(nodeSetting, pluginName) == null ? defalut
								: getNodesettingPluginValue(nodeSetting,
										pluginName));
	}
	
	/**
	 * @description “待办事宜”中显示，且急件需要“标红、置顶”；
	 * @author  领导联系人查看领导的待办事宜
	 * @createTime 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public Page<TaskBusinessBean> getLdTodoByRedType(String workflowType,
			String type, int num) {
		Page<TaskBusinessBean> page = manager.getLdTodoByRedType(workflowType,
				type, num);
		if(page==null){
			return null;
		}
		List<TaskBusinessBean> r = (page.getResult() == null ? new ArrayList<TaskBusinessBean>()
				: page.getResult());
		List<Long> taskIdList = new LinkedList<Long>();
		for (int i = 0; i < r.size(); i++) {
			taskIdList.add(new Long(r.get(i).getTaskId()));
		}
		Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = getTaskIdMapPreTaskBeanTemper(taskIdList);
		for (int i = 0; i < r.size(); i++) {
			TaskBusinessBean taskbusinessbean = r.get(i);
			TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp.get(taskbusinessbean.getTaskId());
			if("意见征询".equals(taskBeanTemp.getNodeName())){	// 处理意见征询的文
				ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
						.getBsinessId().split(";")[2]);
				taskbusinessbean
				.setPreTaskActorInfo("\n已征求完意见\n征询单位:" + (toayjzx == null ? ""
						: toayjzx.getUnit() == null ? "" : toayjzx
								.getUnit()));
			}else{
				 taskbusinessbean
					.setPreTaskActorInfo(getPreTaskActorInfo(taskBeanTemp));
			}
//			if ("0".equals(taskbusinessbean.getBusinessType())) {// 处理意见征询的文
//				ToaYjzx toayjzx = docManager.getYjzx(taskbusinessbean
//						.getBsinessId().split(";")[2]);
//				taskbusinessbean
//						.setPreTaskActorInfo("\n已征求完意见\n征询单位:" + (toayjzx == null ? ""
//								: toayjzx.getUnit() == null ? "" : toayjzx
//										.getUnit()));
//			} else {
//				 taskBeanTemp = TaskIdMapPreTaskBeanTemp
//						.get(taskbusinessbean.getTaskId());
//				taskbusinessbean
//						.setPreTaskActorInfo(getPreTaskActorInfo(taskBeanTemp));
//			}
			r.set(i, taskbusinessbean);
		}
		page.setResult(r);
		return page;

	}
	
	
}

