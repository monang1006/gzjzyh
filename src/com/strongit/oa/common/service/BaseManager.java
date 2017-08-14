package com.strongit.oa.common.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.strongit.oa.approveinfo.ApproveinfoManager;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.autoencoder.CodemanageManager;
import com.strongit.oa.autoencoder.IRuleService;
import com.strongit.oa.autoencoder.util.NumberAnalysis;
import com.strongit.oa.bo.TJbpmTaskExtend;
import com.strongit.oa.bo.TMainActorConfing;
import com.strongit.oa.bo.TMainReassignActorConfing;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaCodemanage;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaWorkForm;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.business.jbpmbusiness.JBPMBusinessManager;
import com.strongit.oa.common.business.jbpmtaskextend.IJbpmTaskExtendService;
import com.strongit.oa.common.business.mainactorconfig.MainActorConfigManage;
import com.strongit.oa.common.business.mainactorconfig.MainReassignActorConfigManage;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.extend.BaseExtendManager;
import com.strongit.oa.common.extend.pojo.BackSpaceExtenBean;
import com.strongit.oa.common.extend.pojo.BackSpaceLastExtenBean;
import com.strongit.oa.common.extend.pojo.OverRuleExtenBean;
import com.strongit.oa.common.service.adapter.AdapterWorkflowManager;
import com.strongit.oa.common.service.adapter.bo.EntrustWorkflowParameter;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.parameter.BackSpaceParameter;
import com.strongit.oa.common.workflow.parameter.ReAssignParameter;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.dict.dictType.DictTypeManager;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.pdf.PdfTempFileHelper;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.relat.WorkFormManager;
import com.strongit.oa.senddoc.docservice.IDocService;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.DateCountUtil;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseNodesettingPlugin;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 此类提供了有关工作流和电子表单和统一用户相关接口。
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2009-12-11 下午04:36:34
 * @version 2.0.2.3
 * @classpath com.strongit.oa.common.service.BaseManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
@OALogger
public class BaseManager extends BaseEFormManager {

	@Autowired
	private MyLogManager logService;
	@Autowired
	DesktopSectionManager desktop;// 个人桌面服务

	@Autowired
	WorkFormManager workFormManager;// 工作流-表单挂接manager
	@Autowired
	BaseExtendManager baseExtendManager;// BaseManager的功能扩展管理类

	@Autowired
	protected IDictService dictService;// 字典管理Manager

	@Autowired
	protected IDocService docService;

	@Autowired
	protected DictTypeManager typeManager;

	@Autowired
	protected SystemsetManager manager;
	@Autowired
	protected AdapterWorkflowManager adapterworkflowmanager;

	@Autowired
	protected JBPMBusinessManager jbpmbusinessManager;
	@Autowired
	protected InfoItemManager infoItemManager;

	@Autowired
	protected IWorkflowAttachService workflowAttachManager; // 工作流附件管理

	@Autowired
	protected ApproveinfoManager approveManager;

	@Autowired
	protected MainActorConfigManage mainactorconfigmanage;
	@Autowired
	protected MainReassignActorConfigManage mainReassignActorConfigManage;
	@Autowired
	protected IProcessInstanceService processinstanceservice;
	@Autowired
	protected IJbpmTaskExtendService jbpmTaskExtendService;
	@Autowired private IRuleService ruleService;
	@Autowired private CodemanageManager codemanageManager;
	/**
	 * 根据任务节点id得到字段信息
	 * 
	 * @author:邓志城
	 * @date:2010-11-10 下午05:05:16
	 * @param String
	 *            taskId 任务实例id
	 * @param boolean
	 *            isContainsVisibleField 是否返回不显示的字段
	 * @return 返回List<EFormComponent>
	 */
	public List<EFormComponent> getFieldInfo(String taskId,
			boolean isContainsHiddenField) throws Exception {
		long l1 = System.currentTimeMillis();
		List<EFormComponent> eformComponentList = new ArrayList<EFormComponent>();
		// 通过任务ID获取表单ID和业务ID
		try {
			String[] info = super.getFormIdAndBussinessIdByTaskId(taskId);
			Object[] cunrrentNodeInfo = workflow.getBusiFlagByTaskId(taskId);
			String strFlag = (String) cunrrentNodeInfo[3];// 得到当前节点设置的域信息
			JSONArray jsonArray = null;
			if (strFlag != null) {
				strFlag = URLDecoder.decode(strFlag, "utf-8");
				strFlag = strFlag.replaceAll("#", ",");
				jsonArray = JSONArray.fromObject(strFlag);
				JSONObject fbj = jsonArray.getJSONObject(0);
				if (fbj.containsKey("type")) {// 输入意见字段
					jsonArray.remove(fbj);
				}
			}
			String businessId = info[0];
			if (!"0".equals(businessId)) {
				String[] args = businessId.split(";");
				String tableName = args[0];
				String pkFieldName = args[1];
				String pkFieldValue = args[2];
				// 增加返回字段类型的处理
				StringBuilder SqlColumnQuery = new StringBuilder(
						"select * from ").append(tableName).append(
						" t where 1 = 0");
				final Map<String, Object[]> columnInfoMap = new LinkedHashMap<String, Object[]>();
				jdbcTemplate.query(SqlColumnQuery.toString(),
						new ResultSetExtractor() {
							public Object extractData(ResultSet rs)
									throws SQLException, DataAccessException {
								ResultSetMetaData rsmd = rs.getMetaData();
								int count = rsmd.getColumnCount();
								for (int i = 1; i <= count; i++) {
									// 过滤掉BLOB类型字段
									if (Types.BLOB != rsmd.getColumnType(i)) {
										columnInfoMap.put(
														rsmd.getColumnName(i),
														new Object[] {
																rsmd.getColumnType(i),
																rsmd.getColumnTypeName(i),
																rsmd.getColumnClassName(i) });
									}
								}
								return columnInfoMap;
							}
						});
				// ----------End------------
				// String SqlQuery = "SELECT * FROM " + tableName + " t WHERE
				// t." + pkFieldName + "='" + pkFieldValue + "'";
				StringBuilder SqlQuery = new StringBuilder("select ").append(
						org.apache.commons.lang.StringUtils.join(columnInfoMap
								.keySet(), ',')).append(" from ").append(
						tableName).append(" t where t.").append(pkFieldName)
						.append("='").append(pkFieldValue).append("'");
				Map resultMap = jdbcTemplate.queryForMap(SqlQuery.toString());
				String formId = info[1];
				Map<String, EFormComponent> fieldMap = eform.getFieldInfo(formId);
				List<ToaSysmanageProperty> itemList = infoItemManager.getItemList(tableName);
				if (jsonArray != null) {// 存在挂接域信息
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Object fieldName = jsonObject.get("fieldName");
						if (fieldName != null) {
							EFormComponent property = fieldMap.get(fieldName.toString());
							if (property != null) {
								Object ReadOnly = jsonObject.get("ReadOnly");// 是否只读
								if (ReadOnly != null) {
									property.setReadonly(Boolean
											.valueOf(ReadOnly.toString()));
								}
								Object Required = jsonObject.get("Required");
								if (Required != null) {
									property.setRequired(Required.toString());
								}
								Object Visible = jsonObject.get("Visible");
								if (Visible != null) {
									property.setVisible(Boolean.valueOf(Visible.toString()));
								}
								if (isContainsHiddenField) {// 需要返回不显示的字段
									fieldMap.put(fieldName.toString(), property);
								} else {
									if (property.isVisible()) {
										fieldMap.put(fieldName.toString(),
												property);
									}
								}
							}
						}
					}
				}
				Collection<EFormComponent> values = fieldMap.values();
				List<EFormComponent> valueList = new ArrayList<EFormComponent>(
						values);
				if (values != null && !values.isEmpty()) {
					for (ToaSysmanageProperty fieldProperty : itemList) {
						for (int k = 0; k < valueList.size(); k++) {
							EFormComponent property = valueList.get(k);
							String fieldName = property.getFieldName();// 得到控件绑定的字段名称
							if(fieldName==null){
								continue;//当表单域名为空时则直接进行下一次循环,modify by hecj 2012-04-9 09:53
							}
							if (fieldName.equals(fieldProperty
									.getInfoItemField())) {
								Long fieldNo = fieldProperty
										.getInfoItemOrderby();
								String fieldCaption = fieldProperty
										.getInfoItemSeconddisplay();
								if (fieldNo != null) {
									property.setNumber(fieldNo);// 排序号
								}
								property.setLable(fieldCaption);// 信息项别名
								property.setValueType(fieldProperty
										.getInfoItemDatatype());// 信息项类型
								Object[] columnInfoArray = columnInfoMap
										.get(fieldName);
								if (columnInfoArray != null) {
									property
											.setColumnType((Integer) columnInfoArray[0]);// SQL类型
									property
											.setColumnTypeName((String) columnInfoArray[1]);
									property
											.setColumnClassName((String) columnInfoArray[2]);
								}
								if (resultMap != null) {
									Object fieldValue = resultMap
											.get(fieldName);
									if (fieldValue != null) {
										property.setValue(ConvertUtils.convert(
												fieldValue, String.class)
												.toString());// 得到字段值
									}
								}
								// 处理下拉列表情况
								if (property.getType().equals("TEFComboBox")) {
									String items = property.getItems();
									if (items.indexOf(";") == -1) {// 下拉列表数据是从数据字典中读取
										if (property.getSelTableName() != null
												&& !"".equals(property
														.getSelTableName())
												&& property.getSelCode() != null
												&& !"".equals(property
														.getSelCode())
												&& property.getSelName() != null
												&& !"".equals(property
														.getSelName())) {
											StringBuilder query = new StringBuilder(
													"select ")
													.append(property.getSelCode())
													.append(",")
													.append(property.getSelName())
													.append(" from ")
													.append(property.getSelTableName())
													.append(" where ")
													.append(property.getSelFilter());
											List list = jdbcTemplate
													.queryForList(query.toString());
											StringBuilder builderItems = new StringBuilder();
											if (!list.isEmpty()) {
												for (int i = 0; i < list.size(); i++) {
													Map map = (Map) list.get(i);
													builderItems
															.append(
																	map.get(property
																					.getSelCode()))
															.append(",")
															.append(
																	map.get(property
																					.getSelName()))
															.append(",")
															.append(
																	map.get(property
																					.getSelCode()))
															.append(";");
												}
											}
											property.setItems(builderItems.toString());
										}
									}
								}
								eformComponentList.add(property);
								valueList.remove(k);
								k++;
								break;
							}
						}
					}
				}
				if (values != null) {
					values.clear();
				}
				if (itemList != null) {
					itemList.clear();
				}
				fieldMap.clear();
				if (resultMap != null) {
					resultMap.clear();
				}
				if (columnInfoMap != null) {
					columnInfoMap.clear();
				}
			}
			long l2 = System.currentTimeMillis();
			logger.info("***************返回数据：" + eformComponentList.toString());
			logger.info("共耗时为：" + (l2 - l1) + "ms");
		} catch (Exception e) {
			logger.error("读取字段信息时发生异常", e);
			throw new SystemException(e);
		}
		return eformComponentList;
	}

	/**
	 * author:lanlc description:个人桌面 modifyer:
	 * 
	 * @param blockId
	 * @return
	 */
	public Map<String, String> getDesktopParam(String blockId)
			throws SystemException, ServiceException {
		try {
			return desktop.getParam(blockId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "个人桌面" });
		}
	}

	/**
	 * 通过字典类值，得到该字典类值下的字典项对象
	 * 
	 * @author:邓志城
	 * @date:2009-12-11 下午05:11:50
	 * @param dictValue
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	protected List getItemsByDictValue(String dictValue)
			throws SystemException, ServiceException {
		try {
			return dictService.getItemsByDictValue(dictValue);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "个人桌面" });
		}
	}

	/**
	 * 根据流程ID获取工作流表单挂接对象
	 * <p>
	 * 一个流程定义下有多个版本流程
	 * </p>
	 * 
	 * @author:邓志城
	 * @date:2009-5-27 上午10:25:24
	 * @param id
	 *            流程ID
	 * @return
	 * @throws SystemException
	 */
	public ToaWorkForm getWorkForm(String id) throws SystemException {
		TwfBaseProcessfile processFile = getProcessfileById(id);
		String pfId = String.valueOf(processFile.getPfId());
		return workFormManager.getWorkForm(pfId);
	}

	/**
	 * 根据流程定义ID获取工作流表单挂接对象
	 * 
	 * @author:邓志城
	 * @date:2009-5-27 上午10:25:24
	 * @param id
	 *            流程定义ID
	 * @return
	 * @throws SystemException
	 */
	public ToaWorkForm getWorkFormByProcessDefinitionId(String pid)
			throws SystemException {
		return workFormManager.getWorkForm(pid);
	}

	/**
	 * 根据流程定义名称获取流程表单挂接对象
	 * 
	 * @author:邓志城
	 * @date:2009-6-9 下午07:06:31
	 * @param workFlowName
	 *            流程定义名称
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public ToaWorkForm getWorkFormByWorkFlowName(String workFlowName)
			throws SystemException, ServiceException {
		return workFormManager.getWorkFormByWorkFlowName(workFlowName);
	}

	/**
	 * 根据流程实例ID获取工作流表单挂接对象
	 * 
	 * @author:邓志城
	 * @date:2009-6-1 上午11:10:58
	 * @param processInstanceId -
	 *            流程实例ID
	 * @return 工作流表单挂接对象
	 * @throws SystemException
	 */
	public ToaWorkForm getWorkFormByProcessInstanceId(String processInstanceId)
			throws SystemException {
		String processDefinitionId = getProcessFileIdByProcessInstanceId(processInstanceId);
		return getWorkFormByProcessDefinitionId(processDefinitionId);
	}

	/**
	 * 根据流程实例Id得到流程定义文件Id
	 * 
	 * @author 邓志城
	 * @date Jun 1, 2009 9:51:16 AM
	 * @param processInstanceId
	 *            -流程实例Id
	 * @return String 流程定义文件Id
	 * @throws WorkflowException
	 */
	public String getProcessFileIdByProcessInstanceId(String processInstanceId)
			throws SystemException {
		return workflow.getProcessFileIdByProcessInstanceId(processInstanceId);
	}

	/**
	 *
	 * 启动流程并使流程停留在第一个任务节点,通常为拟稿节点，将保存电子表单数据和提交工作流处理一并处理
	 * 
	 * @author 严建
	 * @param formId
	 *            表单ID
	 * @param workflowName
	 *            流程名称
	 * @param businessId
	 *            业务数据id
	 * @param businessName
	 *            标题
	 * @param taskActors
	 *            下一步处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @param transitionName
	 *            迁移线名称
	 * @param otherparameter
	 *            扩展实体信息（用于向后兼容需要添加的参数）  
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @createTime Apr 13, 2012 10:51:19 AM
	 */
	public  VoFormDataBean  startProcessToFistNodeWorkflow(String formId, String workflowName,
		String businessId, String userId,String businessName, String[] taskActors,
		String tansitionName,  String sugguestion,
		File formData,OtherParameter otherparameter,OALogInfo... infos) throws SystemException,
		ServiceException {
	    VoFormDataBean bean = null;
		try {
			if (businessId == null || "".equals(businessId)) {// 新建表单数据-->提交工作流
				if (formData != null) {
					bean = this.saveFormData(formData, workflowName, "1",
							businessName);// super.saveForm(formData);
					businessId = bean.getBusinessId();
				}
			} else{//重新办理时，将数据保存到中间表中
				Tjbpmbusiness model = jbpmbusinessManager.findByBusinessId(businessId);
				if(model == null){
					model = new Tjbpmbusiness(businessId);
				}
				jbpmbusinessManager.saveModel(model);
			}
			// 读取标题
			if (businessName == null || "".equals(businessName)) {
				if (this instanceof SendDocManager) {
					String[] args = businessId.split(";");
					String tableName = args[0];
					String pkFieldName = args[1];
					String pkFieldValue = args[2];
					Map map = getSystemField(pkFieldName, pkFieldValue, tableName);
					businessName = (String) map
							.get(BaseWorkflowManager.WORKFLOW_TITLE);// 得到标题
				}
			}
			if (businessName == null || "".equals(businessName)) {
				businessName = workflowName;
			} else {
				businessName = businessName.replaceAll("\\r\\n", "");// 处理有回车换行的情况
			}
			String instanceId = super.startProcessToFistNode(formId,workflowName,businessId,userId, businessName,
				taskActors,  tansitionName,
				sugguestion);
			if (bean == null) {
				bean = new VoFormDataBean();
			}
			bean.setInstanceId(instanceId);
			// 更新流程办理意见及日期
			approveManager.synchronizedApproveInfo(businessId, instanceId, sugguestion,otherparameter);
			return bean;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "工作流处理" });
		}
	}
	
	/**
	 * 提交工作流处理 这里覆盖父类的方法，将保存电子表单数据和提交工作流处理一并处理
	 * 
	 * @param formId
	 *            表单ID
	 * @param workflowName
	 *            流程名称
	 * @param businessId
	 *            业务数据id
	 * @param businessName
	 *            标题
	 * @param taskActors
	 *            下一步处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @param transitionName
	 *            迁移线名称
	 * @param otherparameter
	 *            扩展实体信息（用于向后兼容需要添加的参数）           
	 */
	public VoFormDataBean handleWorkflow(String formId, String workflowName,
			String businessId, String businessName, String[] taskActors,
			String tansitionName, String concurrentTrans, String sugguestion,
			File formData,OtherParameter otherparameter, OALogInfo... infos) throws SystemException,
			ServiceException {
		VoFormDataBean bean = null;
		try {
			if (businessId == null || "".equals(businessId)) {// 新建表单数据-->提交工作流
				if (formData != null) {
					bean = this.saveFormData(formData, workflowName, "1",
							businessName);// super.saveForm(formData);
					businessId = bean.getBusinessId();
				}
			} else {// 草稿箱中提交工作流
				this.update(businessId, workflowName, "1", businessName);
			}
			// 读取标题
			if (businessName == null || "".equals(businessName)) {
				if (this instanceof SendDocManager) {
					String[] args = businessId.split(";");
					String tableName = args[0];
					String pkFieldName = args[1];
					String pkFieldValue = args[2];
					Map map = getSystemField(pkFieldName, pkFieldValue,
							tableName);
					businessName = (String) map
							.get(BaseWorkflowManager.WORKFLOW_TITLE);// 得到标题
				}
			}
			if (businessName == null || "".equals(businessName)) {
				businessName = workflowName;
			} else {
				businessName = businessName.replaceAll("\\r\\n", "");// 处理有回车换行的情况
			}
			String instanceId = super.handleWorkflow(formId, workflowName,
					businessId, businessName, taskActors, tansitionName,
					concurrentTrans, sugguestion);
			if (bean == null) {
				bean = new VoFormDataBean();
			}
			bean.setInstanceId(instanceId);
			// 更新流程办理意见及日期
			approveManager.synchronizedApproveInfo(businessId, instanceId, sugguestion,otherparameter);
			return bean;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "工作流处理" });
		}
	}

	/**
	 * 重新设置流程期限时间
	 * 
	 * @description
	 * @author 严建
	 * @param processInstanceId
	 * @param ProcessTimer
	 * @createTime Apr 18, 2012 9:02:47 AM
	 */
	public boolean reSetProcessTimer(String processInstanceId, Date ProcessTimer) {
		boolean flag = false;
		try {
			if (ProcessTimer != null && processInstanceId != null
					&& !"".equals(processInstanceId)) {
				ProcessInstance processinstance = workflow
						.getProcessInstanceById(processInstanceId);
				Object[] reSetInfo = new Object[] { ProcessTimer };
				flag = workflow.reSetProcessTimer(processInstanceId, reSetInfo,
						new OALogInfo("流程【" + processinstance.getName() + "】《"
								+ processinstance.getBusinessName()
								+ "》设置了流程期限"));
				if (!flag) {
					ToaLog log = new ToaLog();
					String ip = ServletActionContext.getRequest()
							.getRemoteHost();
					log.setOpeIp(ip); // 操作者IP地址
					log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
					log.setLogState("1"); // 日志状态
					log.setOpeTime(new Date()); // 操作时间
					log.setLogInfo("设置流程期限失败，可能没有启用催办！");// 日志信息
					logger.error("设置流程期限失败，可能没有启用催办！");
					logService.saveObj(log);
				} 
			}
		} catch (Exception e) {
			ToaLog log = new ToaLog();
			String ip = ServletActionContext.getRequest().getRemoteAddr();
			log.setOpeIp(ip); // 操作者IP地址
			log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("设置流程期限失败，可能没有启用催办！");// 日志信息
			logger.error("设置流程期限失败，可能没有启用催办！");
			logService.saveObj(log);
		} finally {
		}
		return flag;
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
	 * @param otherparameter
	 *            扩展实体信息（用于向后兼容需要添加的参数）
	 * @param taskActors
	 *            下一步任务处理人([人员ID|节点ID，人员ID|节点ID……])
	 */
	@SuppressWarnings("unchecked")
	public VoFormDataBean handleWorkflowNextStep(String taskId,
			String transitionName, String returnNodeId, String isNewForm,
			String formId, String businessId, String suggestion,
			String curActorId, String[] taskActors, File formData,
			OtherParameter otherparameter,OALogInfo... infos) throws SystemException, ServiceException {
		try {
			VoFormDataBean bean = null;
			if (formData != null) {
				bean = eform.saveFormData(formData);
				businessId = bean.getBusinessId();
			}
			this.update(businessId, "", "1", "");
			super.handleWorkflowNextStep(taskId, transitionName, returnNodeId,
					isNewForm, formId, businessId, suggestion, curActorId,
					taskActors,otherparameter);
			// 更新流程办理意见及日期
			approveManager.synchronizedApproveInfo(taskId,otherparameter);
			return bean;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "流程跳转" });
		}
	}

	/**
	 * 抚州高新区定制签发功能：提交工作流转到下一步
	 * from 林业厅
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
	 * @param otherparameter
	 *            扩展实体信息（用于向后兼容需要添加的参数）
	 * @param taskActors
	 *            下一步任务处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @throws javax.transaction.SystemException 
	 */
	@SuppressWarnings("unchecked")
	public VoFormDataBean qianfaWorkflowNextStep(String taskId,
			String transitionName, String returnNodeId, String isNewForm,
			String formId, String businessId, String suggestion,
			String curActorId, String[] taskActors, File formData,
			OtherParameter otherparameter,OALogInfo... infos) throws Exception {
		try {
			VoFormDataBean bean = null;
			if (formData != null) {
				bean = eform.saveFormData(formData);
				businessId = bean.getBusinessId();
			}
			this.update(businessId, "", "1", "");
			super.handleWorkflowNextStep(taskId, transitionName, returnNodeId,
					isNewForm, formId, businessId, suggestion, curActorId,
					taskActors);
            // 签发时自动生成编号
			if (!"".equals(businessId)) {
				String[] args = businessId.split(";");
				String tableName = args[0];
				String pkFieldName = args[1];
				String pkFieldValue = args[2];
				if (pkFieldValue != null && !pkFieldValue.equals("")) {
					StringBuilder sql = new StringBuilder("");
					sql.append("SELECT SENDDOC_RULE_INFO FROM ").append(tableName).append(" WHERE ")
						.append(pkFieldName).append("='").append(pkFieldValue).append("'");
					String ruleInfo =  (String) jdbcTemplate.queryForObject(sql.toString(), java.lang.String.class);
					String val="";
					String id = "";
					String selIds = "0";
					if(ruleInfo != null && !"".equals(ruleInfo)){
						id = ruleInfo.split("%")[0];
						selIds = ruleInfo.split("%")[1];
						ToaRule rule = ruleService.getRuleById(id);
						//获取当前用户所在部门ID
						String userId = userService.getCurrentUser().getUserId();
						TUumsBaseOrg org=userService.getUserDepartmentByUserId(userId);
						String orgId = org.getOrgId();
						//System.out.println(orgId);
						String xml = rule.getRule();
						if(xml==null||"".equals(xml)){
						}else{
							NumberAnalysis analysis = new NumberAnalysis(xml);
							String updatedXml = "";
							val = analysis.getMyNumber(orgId,selIds.split(","));
							updatedXml = analysis.updateXmlByInfo(orgId,selIds.split(",")[0]);
							if(updatedXml==null){
							}else{
								rule.setRule(updatedXml);
								this.ruleService.save(rule);
								ToaCodemanage obj = new ToaCodemanage();
								obj.setCodeInfo(val);
								obj.setCoderuleId(id);
								obj.setCodeStatus("0");							
								obj.setCodeUsername(userService.getCurrentUser().getUserName());
								obj.setCodeCreatetime(new Date());
								codemanageManager.saveObj(obj);
							}
						}
						
						//更新流程编号
						StringBuilder hql = new StringBuilder("");
						hql.append("Update ").append(tableName).append(" t Set t.").append(WORKFLOW_CODE).append("='").append(val).append("'")
							.append(" Where t.").append(pkFieldName).append("='").append(pkFieldValue).append("'");
						jdbcTemplate.update(hql.toString());
					}
				}
			}
			// 更新流程办理意见及日期
			approveManager.qianfaWorkflowNextStepAfterUpdate(taskId);
			return bean;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "流程跳转" });
		}
	}
	
	/**
	 * 任务退回给上一办理人 退回操作之前需要先保存电子表单数据
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午08:22:14
	 * @param taskId
	 *            任务ID
	 * @param formId
	 *            表单ID
	 * @param suggestion
	 *            退回意见
	 * @param formData
	 *            电子表单数据
	 * @throws SystemException
	 * @see  {@link #backSpaceLast(BackSpaceParameter, OALogInfo[])}
	 * 返回要删除的附件
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public VoFormDataBean backSpaceLast(String taskId, String formId,
			String suggestion, String curUserId, File formData,
			OALogInfo... infos) throws SystemException {
		String businessId = null;
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
			businessId = bean.getBusinessId();
		}
		super.handleWorkflowNextStep(taskId,
				WorkflowConst.WORKFLOW_TRANSITION_LASTPRE, curUserId, "0",
				formId, businessId, suggestion, curUserId, null);
//		<--oa业务  保存退回意见
		workflowConstService.setBackInfo(taskId, 
				CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACEPREV, suggestion, curUserId);
		//-->
		return bean;
	}
	/**
	 *  任务退回给上一办理人 退回操作之前需要先保存电子表单数据
	 * 
	 * @description
	 * @author 严建
	 * @param parameter	参数实体
	 * @param infos
	 * @return
	 * @throws SystemException
	 * @createTime Mar 29, 2012 12:32:38 PM
	 */
	@SuppressWarnings("unchecked")
	public VoFormDataBean backSpaceLast(BackSpaceParameter parameter,
			OALogInfo... infos) throws SystemException {
		String taskId = parameter.getTaskId();
		String formId = parameter.getFormId();
		String suggestion = parameter.getSuggestion();
		String curUserId = parameter.getCurUserId();
		String[] taskActors = parameter.getTaskActors();
		File formData = parameter.getFormData();
		String businessId = null;
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
			businessId = bean.getBusinessId();
		}
		super.handleWorkflowNextStep(taskId,
				WorkflowConst.WORKFLOW_TRANSITION_LASTPRE, curUserId, "0",
				formId, businessId, suggestion, curUserId, taskActors,parameter);
		//调用同步临时意见表和流程意见表的数据,但是退回时，不同步临时意见表的中数据的意见时间等
		parameter.setBack(true);	//属于退回操作
		approveManager.synchronizedApproveInfo(taskId, parameter);
//		<--oa业务扩展
		baseExtendManager.backSpaceLastExtend(new BackSpaceLastExtenBean(parameter));
		//-->
		return bean;
	}

	/**
	 * 任务退回 退回操作之前需要先保存电子表单数据
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午08:22:14
	 * @param taskId
	 *            任务ID
	 * @param formId
	 *            表单ID
	 * @param suggestion
	 *            退回意见
	 * @param formData
	 *            电子表单数据
	 * @param returnNodeId
	 *            需要退回到的目标节点
	 * @throws SystemException
	 * @see {@link #backSpace(BackSpaceParameter, OALogInfo[])}}
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public VoFormDataBean backSpace(String taskId, String returnNodeId,
			String formId, String suggestion, String curUserId, File formData,
			OALogInfo... infos) throws SystemException {
		String businessId = null;
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
			businessId = bean.getBusinessId();
		}
		super.handleWorkflowNextStep(taskId,
				WorkflowConst.WORKFLOW_TRANSITION_HUITUI, returnNodeId, "0",
				formId, businessId, suggestion, curUserId, null);
		
//		<--oa业务  保存退回意见
		workflowConstService.setBackInfo(taskId, 
				CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACE, suggestion, curUserId);
		//-->
		return bean;
	}
	
	/**
	 *  任务退回 退回操作之前需要先保存电子表单数据
	 * 
	 * @author 严建
	 * @param parameter	退回参数实体
	 * @param infos
	 * @return
	 * @throws SystemException
	 * @createTime Mar 28, 2012 8:42:34 AM
	 */
	@SuppressWarnings("unchecked")
	public VoFormDataBean backSpace(BackSpaceParameter parameter,
			OALogInfo... infos) throws SystemException {
		String taskId = parameter.getTaskId();
		String returnNodeId = parameter.getReturnNodeId();
		String formId = parameter.getFormId();
		String suggestion = parameter.getSuggestion();
		String curUserId = parameter.getCurUserId();
		String[] taskActors = parameter.getTaskActors();
		File formData = parameter.getFormData();
		String businessId = null;
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
			businessId = bean.getBusinessId();
		}
		super.handleWorkflowNextStep(taskId,
				WorkflowConst.WORKFLOW_TRANSITION_HUITUI, returnNodeId, "0",
				formId, businessId, suggestion, curUserId, taskActors,parameter);
//		调用同步临时意见表和流程意见表的数据,但是退回时，不同步临时意见表的中数据的意见时间等
		parameter.setBack(true);	//属于退回操作
		approveManager.synchronizedApproveInfo(taskId, parameter);
//		<--oa业务扩展
		baseExtendManager.backSpaceExtend(new BackSpaceExtenBean(parameter));
		//-->
		return bean;
	}
	
	/**
	 * 任务驳回 退回操作之前需要先保存电子表单数据
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午08:22:14
	 * @param taskId
	 *            任务ID
	 * @param formId
	 *            表单ID
	 * @param suggestion
	 *            退回意见
	 * @param formData
	 *            电子表单数据
	 * @param returnNodeId
	 *            需要退回到的目标节点
	 * @throws SystemException
	 * @see {@link #overRule(BackSpaceParameter, OALogInfo[])}
	 */
	@Deprecated 
	public VoFormDataBean overRule(String taskId, String returnNodeId,
			String formId, String suggestion, File formData, OALogInfo... infos)
			throws SystemException {
		String businessId = null;
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
			businessId = bean.getBusinessId();
		}
		super.handleWorkflowNextStep(taskId,
				WorkflowConst.WORKFLOW_TRANSITION_BOHUI, returnNodeId, "0",
				formId, businessId, suggestion, "", null);
//		<--oa业务  保存退回意见
		workflowConstService.setBackInfo(taskId, 
				CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_OVERRULE, suggestion, "");
		//-->
		return bean;
	}
	
	/**
	 * 任务驳回 退回操作之前需要先保存电子表单数据
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午08:22:14
	 * @param taskId
	 *            任务ID
	 * @param formId
	 *            表单ID
	 * @param suggestion
	 *            退回意见
	 * @param formData
	 *            电子表单数据
	 * @param returnNodeId
	 *            需要退回到的目标节点
	 * @throws SystemException
	 */
	/**
	 * 任务驳回 退回操作之前需要先保存电子表单数据
	 * 
	 * @author 严建
	 * @param parameter	参数实体
	 * @param infos
	 * @return
	 * @throws SystemException
	 * @createTime Mar 29, 2012 1:04:49 PM
	 */
	public VoFormDataBean overRule(BackSpaceParameter parameter,
			OALogInfo... infos) throws SystemException {
		String taskId = parameter.getTaskId();
		String returnNodeId = parameter.getReturnNodeId();
		String formId = parameter.getFormId();
		String suggestion = parameter.getSuggestion();
		File formData = parameter.getFormData();
		String[] taskActors = parameter.getTaskActors();
		String curUserId = parameter.getCurUserId();
		String businessId = null;
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
			businessId = bean.getBusinessId();
		}
		super.handleWorkflowNextStep(taskId,
				WorkflowConst.WORKFLOW_TRANSITION_BOHUI, returnNodeId, "0",
				formId, businessId, suggestion, curUserId, taskActors,parameter);
		// <--oa业务扩展
//		调用同步临时意见表和流程意见表的数据,但是退回时，不同步临时意见表的中数据的意见时间等
		parameter.setBack(true);	//属于退回操作
		approveManager.synchronizedApproveInfo(taskId, parameter);
		baseExtendManager.overRuleExtend(new OverRuleExtenBean(parameter));
		// -->
		return bean;
	}

	/**
	 * 展现操作按钮 1、新建流程时仅显示 a、提交下一步 b、打印处理单 2、审批流程时显示 a、退回 允许退回的情况下 b、驳回 允许驳回的情况下
	 * c、指派 允许指派的情况下 d、指派返回 允许指派返回的情况下,注意指派返回后不能提交下一步 e、提交下一步 不允许提交下一步的情况下
	 * f、查看办理状态 g、打印处理单
	 * 
	 * @author:邓志城
	 * @date:2009-12-14 下午10:25:51
	 * @param taskId
	 *            任务实例id
	 * @return 满足条件的html代码 子类可根据需要重载此方法
	 */
	public TwfBaseNodesetting getOperationHtml(String taskId,
			String workflowName) {
		String frameRoot = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("frameroot");
		if (frameRoot == null || frameRoot.equals("")
				|| frameRoot.equals("null"))
			frameRoot = "/frame/theme_gray";
		frameRoot = ServletActionContext.getRequest().getContextPath()
				+ frameRoot;
		StringBuffer html = new StringBuffer();
		TwfBaseNodesetting nodeSetting = workflow.findFirstNodeSetting(taskId,
				workflowName);
		if (StringUtils.hasLength(taskId)) {
			// 显示意见征询按钮（办公厅需求） 不显示 严建
			//this.loadYjzx(taskId, html, nodeSetting, frameRoot);
			String returnFlag = super.checkCanReturn(taskId);// 回退|驳回|指派|指派返回
			String[] flags = returnFlag.split("\\|");
			if ("1".equals(flags[3])) {// 允许指派返回
				// this.loadAssignAndReturnHtml(frameRoot, html ,
				// nodeSetting);//显示指派返回按钮
				this.loadAssignAndReturnHtml(frameRoot, html, nodeSetting);// 显示指派返回按钮
				// modify
				// yanjian
				// 2011-10-21
			} else {
				// String nodeId = workflow.getNodeIdByTaskInstanceId(taskId);
				// TwfBaseNodesetting nodeSet =
				// workflow.getNodesettingByNodeId(nodeId);
				this.loadSubmitToNextPersonHtml(frameRoot, html, nodeSetting);// 显示提交下一办理人按钮
			}
			if ("1".equals(flags[1])) {// 允许驳回
				this.loadOverRuleHtml(frameRoot, html, nodeSetting);// 显示驳回按钮
			}
			if ("1".equals(flags[0])) {// 允许退回
				this.loadBackSpacePrevHtml(frameRoot, html, nodeSetting);// 显示退回上一办理人按钮
				this.loadBackSpaceHtml(frameRoot, html, nodeSetting);// 显示退回按钮
			}
			if ("1".equals(flags[2])) {// 允许指派
				boolean enable = this.loadChangeMainActorHtml(frameRoot, html, nodeSetting, taskId);// 加载【主办变更】按钮
				if(!enable){
					this.loadAssignHtml(frameRoot, html);// 显示指派按钮
				}
			}
			this.loadTransactStateHtml(frameRoot, html, nodeSetting);// 存在任务节点时显示办理状态按钮
		} else {
			this.loadSubmitToNextPersonHtml(frameRoot, html, nodeSetting);// 显示提交下一办理人按钮
		}
		this.loadSaveOrToNextHtml(frameRoot, html, nodeSetting, taskId);
		this.loadSaveOrClosedHtml(frameRoot, html, nodeSetting);// 显示保存并关闭按钮
		this.loadPrintFormHtml(frameRoot, html, nodeSetting);// 显示打印处理单按钮
		super.getSession().evict(nodeSetting);
		nodeSetting.setNsFormPrivInfo(html.toString());
		logger.info("HTML************" + html);
		return nodeSetting;
	}

	/**
	 * 加载意见征询按钮
	 * 
	 * @param taskId
	 *            任务实例id
	 * @param html
	 */
	private void loadYjzx(String taskId, StringBuffer html,
			TwfBaseNodesetting nodeSetting, String frameRoot) {
		if (taskId != null && taskId.length() > 0) {
			TaskInstance taskInstance = workflow.getTaskInstanceById(taskId);
			if (taskInstance != null) {
				ProcessInstance processInstance = taskInstance.getProcessInstance();
				if (processInstance != null) {
					if(workflow.hasMainDoing(userService.getCurrentUser().getUserId(), taskId)){//当前用户拥有主办权限
						String pluginYjzx = nodeSetting
						.getPlugin("plugins_yjzx");
						// 当前任务节点上设置了显示意见征询按钮
						if ("1".equals(pluginYjzx)) {
							String businessId = processInstance.getBusinessId();// 得到业务数据id
							if (businessId != null
									&& businessId.indexOf(";") != -1) {
								String id = businessId.split(";")[2];// 得到业务数据主键值,用于验证是否已经做过意见征询的操作
								String sql = "select count(*) from T_OA_ATTACHMENT t where t.USER_ID = '"
										+ id + "'";
								int count = jdbcTemplate.queryForInt(sql);
								if (count > 0) {// 附件表中存在记录,说明已经做过意见征询反馈操作
									// 不显示意见征询按钮
									return;
								}
							}
							String yjzxName = nodeSetting
									.getPlugin("plugins_yjzxname");// 意见征询按钮名称
							if (yjzxName == null | yjzxName.length() == 0) {
								yjzxName = "意见征询";
							}
							html.append("<td id=\"toYjzx\">");
							html.append("	<a class=\"Operation\" href=\"#\" onclick=\"javascript:doYjzx();\"><img src=\""
											+ frameRoot
											+ "/images/search16.gif\" width=\"15\" height=\"15\" alt=\"\" class=\"img_s\">&nbsp;"
											+ yjzxName + "&nbsp;</a>");
							html.append("</td>");
							html.append("<td width=\"5\"></td>");
						}
					}
				}
			}
		}
	}

	/**
	 * 获取流程选择域中的业务字段信息
	 * 
	 * @author 严建
	 * @param plugins
	 *            插件集合
	 * @param key
	 *            键值
	 * @return
	 * @createTime Jan 16, 2012 11:15:48 AM
	 */
	public JSONObject getPluginsBusinessFlagFromPlugins(Map plugins, String key) {
		JSONObject result = null;
		String plugins_businessFlag_value = null;
		TwfBaseNodesettingPlugin pluginsBusinessFlag = (TwfBaseNodesettingPlugin) plugins
				.get("plugins_businessFlag");
		/**
		 * yanjiian 2012-02-08 13:40 Bug序号： 0000000914
		 * (BugRoad|BugFree,URL:http://192.168.2.215/index.php)
		 * 缺陷描述【1.新建了一个流程，用了收文表单，2.部署后，admin新建报错】 解决方法： if(pluginsBusinessFlag !=
		 * null)--> if(pluginsBusinessFlag != null &&
		 * pluginsBusinessFlag.getValue() != null)
		 */
		if (pluginsBusinessFlag != null
				&& pluginsBusinessFlag.getValue() != null&& !"".equals(pluginsBusinessFlag.getValue())) {
			plugins_businessFlag_value = (String) pluginsBusinessFlag
					.getValue();
			try {
				plugins_businessFlag_value = URLDecoder.decode(
						plugins_businessFlag_value, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			plugins_businessFlag_value = plugins_businessFlag_value.replaceAll(
					"#", ",");
			JSONArray array = JSONArray.fromObject(plugins_businessFlag_value);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (obj.containsKey("fieldName")
						&& obj.get("fieldName") != null
						&& obj.get("fieldName").toString().toLowerCase()
								.equals(key.toLowerCase())) {
					result = obj;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 得到提交下一步处理人的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadSubmitToNextPersonHtml(String frameRoot,
			StringBuffer html, TwfBaseNodesetting nodeSet) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String isSuggestionButton = null;
		String isMenuButton = null;
		String isPop = null;
		String isOpen = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins,
				"toNext");
		if (json != null) {
			visible = (String) json.get("visible");
		} else {
			visible = "block";
		}
		if (plugins != null) {
			TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins
					.get("plugins_buttonname");
			TwfBaseNodesettingPlugin pluginIsPop = (TwfBaseNodesettingPlugin) plugins
					.get("plugins_isPop");
			TwfBaseNodesettingPlugin pluginIsSuggestionButton = (TwfBaseNodesettingPlugin) plugins
					.get("plugins_isSuggestionButton");
			TwfBaseNodesettingPlugin pluginIsSuggestionMenu = (TwfBaseNodesettingPlugin) plugins
					.get("plugins_isMenu");
			TwfBaseNodesettingPlugin pluginIsSuggestionOpen = (TwfBaseNodesettingPlugin) plugins
					.get("plugins_isOpen");
			if (pluginButton != null) {
				buttonName = pluginButton.getValue();// 办理按钮名称
			}
			if (pluginIsPop != null) {
				isPop = pluginIsPop.getValue();// 点击办理按钮时是否是弹出方式
			}
			if (pluginIsSuggestionButton != null) {
				isSuggestionButton = pluginIsSuggestionButton.getValue();// 是否是快捷办理按钮
			}
			if (pluginIsSuggestionMenu != null) {
				isMenuButton = pluginIsSuggestionMenu.getValue();// 是否是菜单模式
			}
			if (pluginIsSuggestionOpen != null) {
				isOpen = pluginIsSuggestionOpen.getValue();// 是否是菜单模式
			}
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
			if (buttonName == null || "".equals(buttonName)
					|| "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(
						GlobalBaseData.REMIND_MESSAGE, ActionContext
								.getContext().getLocale()) != null) {
					buttonName = LocalizedTextUtil.findDefaultText(
							GlobalBaseData.WORKFLOW_BUTTONNAME_BUTTONNAME,
							ActionContext.getContext().getLocale());
				} else {
					buttonName = "办理";
				}
			}
			String[] buttonNames = buttonName.split(",");
			for (String name : buttonNames) {
				if (!"".equals(name)) {
					html.append("<td id=\"toNext\" nodeName='"
							+ nodeSet.getNsNodeName() + "' isPop = '" + isPop
							+ "' buttonName='" + name
							+ "' isSuggestionButton = '" + isSuggestionButton
							+ "' isMenuButton = '" + isMenuButton
							+ "' ' isOpen = '" + isOpen + "'  >");
					html.append("	<a buttonName='"
							+ name
							+ "' href=\"#\" class=\"button\" onclick=\"javascript:doNext(this);\" id=\"hrf_tjxyb\">"
							+ name + "</a>");
					html.append("</td>");
					html.append("<td width=\"5\"></td>");
				}
			}
		} else {
			html.append("<td id=\"toNext\" nodeName='"
					+ nodeSet.getNsNodeName() + "' isPop = '" + isPop
					+ "' isSuggestionButton = '" + isSuggestionButton
					+ "' buttonName='办理" + "' isMenuButton = '" + isMenuButton
					+ "' ' isOpen = '" + isOpen + "'>");
			html.append("	<a buttonName='办理' href=\"#\" class=\"button\" onclick=\"javascript:doNext('办理');\" id=\"hrf_tjxyb\">办理</a>");
			html.append("</td>");
			html.append("<td width=\"5\"></td>");
		}
	}

	/**
	 * 得到打印处理单的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadPrintFormHtml(String frameRoot, StringBuffer html, TwfBaseNodesetting nodeSet) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins, "toPrint");
		if (json != null) {
			visible = (String) json.get("visible");
		} else {
			visible = "block";
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
			if (plugins != null) {
				TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins
						.get("plugins_toPrint");
				if (pluginButton != null) {
					buttonName = pluginButton.getValue();// 办理按钮名称
				}
			}
			if (buttonName == null || "".equals(buttonName)
					|| "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(
						GlobalBaseData.REMIND_MESSAGE, ActionContext
								.getContext().getLocale()) != null) {
					buttonName = LocalizedTextUtil.findDefaultText(
							GlobalBaseData.WORKFLOW_BUTTONNAME_TOPRINT,
							ActionContext.getContext().getLocale());
				} else {
					buttonName = "打印处理单";
				}
			}
			html.append("<td id=\"toPrint\">");
			html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:doPrintForm();\">"
							+ buttonName + "</a>");
			html.append("</td>");
			html.append("<td width=\"5\"></td>");
		}
	}

	/**
	 * 得到主办变更的按钮Html 前提：当前用户是主办人员
	 * 
	 * @author 严建
	 * @param frameRoot
	 * @param html
	 * @param nodeSet
	 * @createTime Mar 9, 2012 11:59:41 AM
	 */
	protected boolean loadChangeMainActorHtml(String frameRoot, StringBuffer html,
			TwfBaseNodesetting nodeSet, String taskId) {
		//前提：当前用户是主办人员
		boolean result = false;
		if (userService.getCurrentUser().getUserId().equals(
				workflow.getMainActorIdByTaskInstanceId(taskId))) {
			Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
			String buttonName = null;
			String visible = null;
			JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins,
					"toChangeMainActor");
			if (json != null) {
				visible = (String) json.get("visible");
			} else {
				visible = "none";
			}
			if (visible != null && visible.toLowerCase().equals("block")) {
				if (plugins != null) {
					TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins
							.get("plugins_toChangeMainActor");
					if (pluginButton != null) {
						buttonName = pluginButton.getValue();// 办理按钮名称
					}
				}
				if (buttonName == null || "".equals(buttonName)
						|| "null".equals(buttonName)) {
					if (LocalizedTextUtil.findDefaultText(
							GlobalBaseData.REMIND_MESSAGE, ActionContext
									.getContext().getLocale()) != null) {
						buttonName = LocalizedTextUtil
								.findDefaultText(
										GlobalBaseData.WORKFLOW_BUTTONNAME_TOCHANGEMAINACTOR,
										ActionContext.getContext().getLocale());
					} else {
						buttonName = "主办变更";
					}
				}
				html.append("<td id=\"toChangeMainActor\">");
				html
						.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:changeMainActor();\">"
								+ buttonName + "</a>");
				html.append("</td>");
				html.append("<td width=\"5\"></td>");
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 加载保存或提交按钮(默认不显示该按钮，建议不要和保存并关闭按钮同时显示)
	 * 
	 * @description
	 * @author 严建
	 * @param frameRoot
	 * @param html
	 * @param nodeSet
	 * @param taskId		任务id(预留参数)
	 * @createTime Apr 13, 2012 8:59:46 AM
	 */
	protected void loadSaveOrToNextHtml(String frameRoot, StringBuffer html,
		TwfBaseNodesetting nodeSet, String taskId) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins,
			"toSaveOrToNext");
		if (json != null) {
		    visible = (String) json.get("visible");
		} else {
		    visible = "none";
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
		    if(plugins != null){
				TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins.get("plugins_toSaveOrToNext");
				if(pluginButton != null){
				    buttonName = pluginButton.getValue();// 办理按钮名称
				}
		    }
		    if (buttonName == null || "".equals(buttonName) || "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale()) != null) {
				    buttonName = LocalizedTextUtil.findDefaultText(GlobalBaseData.WORKFLOW_BUTTONNAME_TOSAVEORTONEXT,ActionContext.getContext().getLocale());
				} else {
				    buttonName = "保存或提交";
				}
		    }
		    html.append("<td id=\"toSaveOrToNext\">");
		    html.append("	<a class=\"button\" href=\"#\"  onclick=\"javascript:toSaveOrToNext(true);\">" + buttonName + "</a>");
		    html.append("</td>");
		    html.append("<td width=\"5\"></td>");
		}
	}

	/**
	 * 得到保存并关闭的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadSaveOrClosedHtml(String frameRoot, StringBuffer html,TwfBaseNodesetting nodeSet) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins,"toSave");
		if (json != null) {
			visible = (String)json.get("visible");
		} else {
			visible = "block";
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
			if(plugins != null){
				TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins.get("plugins_toSave");
				if(pluginButton != null){
					buttonName = pluginButton.getValue();// 办理按钮名称
				}
			}
			if (buttonName == null || "".equals(buttonName)
					|| "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale()) != null) {
					buttonName = LocalizedTextUtil.findDefaultText(GlobalBaseData.WORKFLOW_BUTTONNAME_TOSAVE,ActionContext.getContext().getLocale());
				} else {
					buttonName = "保存并关闭";
				}
			}
			html.append("<td id=\"toSave\">");
			html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:saveFormData(true);\" >" + buttonName + "</a>");
			html.append("</td>");
			html.append("<td width=\"5\"></td>");
		}
	}

	/**
	 * 根据节点Id得到流程节点类型
	 * 
	 * @author hecj
	 * @date 2011-8-11
	 * @param nodeId
	 *            -节点Id
	 * @return String 节点类型，具体值含义如下：<br>
	 *         “startNode”：开始节点<br>
	 *         “endNode”：结束节点<br>
	 *         “decideNode”：条件节点<br>
	 *         “statNode”：状态节点<br>
	 *         “forkNode”：并发节点<br>
	 *         “joinNode”：聚合节点<br>
	 *         “subProcessNode”：子流程节点<br>
	 *         “taskNode”：任务节点<br>
	 *         “node”：自动节点<br>
	 *         null：未定义节点
	 * @throws WorkflowException
	 */
	public String findTypeByNodeId(String nodeId) throws com.strongit.workflow.exception.WorkflowException {
		return workflow.checkNodeTypeByNodeId(nodeId);
	}

	/**
	 * 得到退回的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadBackSpaceHtml(String frameRoot, StringBuffer html,TwfBaseNodesetting nodeSet) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins,"toBack");
		if(json != null){
			visible = (String) json.get("visible");
		}else{
			visible = "block";
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
			if (plugins != null) {
				TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin)plugins.get("plugins_toBack");
				if(pluginButton != null){
					buttonName = pluginButton.getValue();// 办理按钮名称
				}
			}
			if (buttonName == null || "".equals(buttonName) || "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale()) != null) {
					buttonName = LocalizedTextUtil.findDefaultText(GlobalBaseData.WORKFLOW_BUTTONNAME_TOBACK,ActionContext.getContext().getLocale());
				} else {
					buttonName = "退回";
				}
			}
			html.append("<td id=\"toBack\" style=\"display: none ;\">");
			html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:doBackSpace('" + buttonName + "');\">" + buttonName + "</a>");
			html.append("</td>");
			html.append("<td width=\"5\"></td>");
		}
	}

	/**
	 * 得到驳回的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadOverRuleHtml(String frameRoot, StringBuffer html, TwfBaseNodesetting nodeSet) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins, "toReturnBack");
		if (json != null) {
			visible = (String) json.get("visible");
		} else {
			visible = "block";
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
			if (plugins != null) {
				TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins.get("plugins_toReturnBack");
				if (pluginButton != null) {
					buttonName = pluginButton.getValue();// 办理按钮名称
				}
			}
			if (buttonName == null || "".equals(buttonName) || "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale()) != null) {
					buttonName = LocalizedTextUtil.findDefaultText(GlobalBaseData.WORKFLOW_BUTTONNAME_TORETURNBACK, ActionContext.getContext().getLocale());
				} else {
					buttonName = "驳回";
				}
			}
			html.append("<td id=\"toReturnBack\" style=\"display: none ;\">");
			html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:doOverRule('" + buttonName + "');\">" + buttonName + "</a>");
			html.append("</td>");
			html.append("<td width=\"5\"></td>");
		}
	}

	/**
	 * 得到办理状态的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadTransactStateHtml(String frameRoot, StringBuffer html, TwfBaseNodesetting nodeSet) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins, "toViewState");
		if (json != null) {
			visible = (String) json.get("visible");
		} else {
			visible = "block";
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
			if (plugins != null) {
				TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins.get("plugins_toViewState");
				if (pluginButton != null) {
					buttonName = pluginButton.getValue();// 办理按钮名称
				}
			}
			if (buttonName == null || "".equals(buttonName)
					|| "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(
						GlobalBaseData.REMIND_MESSAGE, ActionContext
								.getContext().getLocale()) != null) {
					buttonName = LocalizedTextUtil.findDefaultText(
							GlobalBaseData.WORKFLOW_BUTTONNAME_TOVIEWSTATE,
							ActionContext.getContext().getLocale());
				} else {
					buttonName = "办理状态";
				}
			}
			html.append("<td id=\"toViewState\">");
			html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:workflowView('00001');\">" + buttonName + "</a>");
			html.append("</td>");
			html.append("<td width=\"5\"></td>");
		}
	}

	/**
	 * 得到退回上一办理人的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadBackSpacePrevHtml(String frameRoot, StringBuffer html, TwfBaseNodesetting nodeSet) {
		Map plugins = nodeSet.getPlugins();// 得到节点上的插件信息集合
		String buttonName = null;
		String visible = null;
		JSONObject json = this.getPluginsBusinessFlagFromPlugins(plugins, "toPrev");
		if (json != null) {
			visible = (String) json.get("visible");
		} else {
			visible = "block";
		}
		if (visible != null && visible.toLowerCase().equals("block")) {
			if (plugins != null) {
				TwfBaseNodesettingPlugin pluginButton = (TwfBaseNodesettingPlugin) plugins.get("plugins_toPrev");
				if (pluginButton != null) {
					buttonName = pluginButton.getValue();// 办理按钮名称
				}
			}
			if (buttonName == null || "".equals(buttonName)
					|| "null".equals(buttonName)) {
				if (LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale()) != null) {
					buttonName = LocalizedTextUtil.findDefaultText(GlobalBaseData.WORKFLOW_BUTTONNAME_TOPREV, ActionContext.getContext().getLocale());
				} else {
					buttonName = "退回上一办理人";
				}
			}
			if(buttonName.length()>4){
				html.append("<td id=\"toPrev\" style=\"display: none ;\">");
				html.append("	<a class=\"button8\" href=\"#\" onclick=\"javascript:doBackSpacePrev('" + buttonName + "');\">" + buttonName + "</a>");
				html.append("</td>");
				html.append("<td width=\"5\"></td>");
			}else{
				html.append("<td id=\"toPrev\" style=\"display: none ;\">");
				html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:doBackSpacePrev('" + buttonName + "');\">" + buttonName + "</a>");
				html.append("</td>");
				html.append("<td width=\"5\"></td>");
			}
			
		}
	}

	/**
	 * 得到指派的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 */
	protected void loadAssignHtml(String frameRoot, StringBuffer html) {
		html.append("<td>");
		html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:assignTask();\">指派</a>");
		html.append("</td>");
		html.append("<td width=\"5\"></td>");
	}

	/**
	 * 得到指派返回的按钮Html
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 上午09:15:07
	 * @param frameRoot
	 *            图片的上下文路径
	 * @param html
	 *            已经加载的html信息
	 * @modfy yanjian 2011-10-21 问题：当查看指派公文，当前环节：undefined
	 *        解决方案：添加参数TwfBaseNodesetting nodeSet
	 *        2012-04-23 修正【联合发文流程】，在wanfeng指派给huangping后，黄平登陆处理按钮成了underfind。(财政厅OA)
	 *        	解决方法：td标签添加buttonName='指派返回'
	 */
	protected void loadAssignAndReturnHtml(String frameRoot, StringBuffer html, TwfBaseNodesetting nodeSet) {
		html.append("<td id=\"toNext\" nodeName='" + nodeSet.getNsNodeName() + "' buttonName='指派返回'  >");
		html.append("	<a class=\"button\" href=\"#\" onclick=\"javascript:doAssignAndReturn();\">指派返回;</a>");
		html.append("</td>");
		html.append("<td width=\"5\"></td>");
	}

	// protected void loadAssignAndReturnHtml(String frameRoot ,StringBuffer
	// html) {
	// html.append("<td>");
	// html.append(" <a class=\"Operation\"
	// href=\"javascript:doAssignAndReturn();\"><img src=\"" + frameRoot +
	// "/images/songshen.gif\" width=\"15\" height=\"15\" alt=\"\"
	// class=\"img_s\">指派返回&nbsp;</a>");
	// html.append("</td>");
	// html.append("<td width=\"5\"></td>");
	// }

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
	 * @param formData
	 *            电子表单数据,因为指派时需要先保存电子表单数据
	 * @throws SystemException
	 */
	@Deprecated
	public VoFormDataBean reAssignTask(String taskId, String reAssignActorId, String isNeedReturn, File formData, OALogInfo... infos) throws SystemException {
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
		}
		super.reAssignTask(taskId, reAssignActorId, isNeedReturn);
		return bean;
	}
	
	/**
	 * 任务重新指派
	 * 
	 * @author 严建
	 * @param parameter		指派信息
	 * @param infos
	 * @return
	 * @throws SystemException
	 * @createTime Mar 11, 2012 2:51:30 AM
	 */
	public VoFormDataBean reAssignTask(ReAssignParameter parameter, OALogInfo... infos) throws SystemException {
		VoFormDataBean bean = null;
		if (parameter.getFormData() != null) {
			bean = eform.saveFormData(parameter.getFormData());
		}
		super.reAssignTask(parameter.getTaskId(), parameter.getReAssignActorId(),parameter.getIsNeedReturn());
		StringBuilder action = new StringBuilder();
		if("1".equals(parameter.getAllowChangeMainActor())){//允许变更主办人员
			action.append(TJbpmTaskExtend.ACTION_ZHUBANBIANGENG);//变更主办人员
			mainactorconfigmanage.updateModel(new TMainActorConfing(workflow
					.getProcessInstanceIdByTiId(parameter.getTaskId()), parameter.getReAssignActorId())
			,new OALogInfo("主办人员变更为："+userService.getUserNameByUserId(parameter.getReAssignActorId())
					+"【"+parameter.getReAssignActorId()+"】"));
		}else{//不允许变更主办人员
			action.append(TJbpmTaskExtend.ACTION_ZHIPAI);//指派
			User curuser = userService.getCurrentUser();
			if(workflow.hasMainDoing(curuser.getUserId(), parameter.getTaskId())){//拥有主办权限
				mainReassignActorConfigManage.updateModel(new TMainReassignActorConfing(workflow
						.getProcessInstanceIdByTiId(parameter.getTaskId()), parameter.getReAssignActorId()), 
						new OALogInfo("拥有主办权限人员"+curuser.getUserName()+"【"+curuser.getUserId()+"】指派，主办权限转交给"
								+userService.getUserNameByUserId(parameter.getReAssignActorId())
								+"【"+parameter.getReAssignActorId()+"】"));
			}
		}
		//保存任务指派信息
		TJbpmTaskExtend model = new TJbpmTaskExtend();
		model.setTaskInstanceId(Long.valueOf(parameter.getTaskId()));	//保存任务实例id
		model.setAction(action.toString());								//保存Action类型
		User fromUser = userService.getCurrentUser();	
		model.setFromUserId(fromUser.getUserId());						//
		model.setFromUserName(fromUser.getUserName());
		User toUser = userService.getUserInfoByUserId(parameter.getReAssignActorId());
		model.setToUserId(toUser.getUserId());
		model.setToUserName(toUser.getUserName());
		jbpmTaskExtendService.saveModel(model);
		return bean;
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
	 * @param formData
	 *            电子表单数据,因为指派时需要先保存电子表单数据
	 * @throws WorkflowException
	 */
	public VoFormDataBean returnReAssignTask(String taskId, String newForm,
			long formId, File formData, OALogInfo... infos)
			throws WorkflowException {
		String businessId = null;
		VoFormDataBean bean = null;
		if (formData != null) {
			bean = eform.saveFormData(formData);
			businessId = bean.getBusinessId();
		}
		super.returnReAssignTask(taskId, newForm, formId, businessId);
//		保存任务指派信息
		TJbpmTaskExtend model = new TJbpmTaskExtend();
		model.setTaskInstanceId(Long.valueOf(taskId));					//保存任务实例id
		model.setAction(TJbpmTaskExtend.ACTION_ZHIPAIDFANHUI);			//保存Action类型
		User fromUser = userService.getCurrentUser();	
		model.setFromUserId(fromUser.getUserId());						//
		model.setFromUserName(fromUser.getUserName());
		jbpmTaskExtendService.saveModel(model);
		return bean;
	}

	/**
	 * 得到当前用户输入意见
	 * 
	 * @author:邓志城
	 * @date:2009-12-26 上午11:25:35
	 * @return
	 * @throws SystemException
	 */
	public List<ToaSysmanageDictitem> getCurrentUserDictItem(String dictType)
			throws SystemException {
		List<ToaSysmanageDictitem> list = dictService.getItemsByDictValue(dictType);
		String userId = userService.getCurrentUser().getUserId();
		List<ToaSysmanageDictitem> newList = new ArrayList<ToaSysmanageDictitem>();
		if (list != null && !list.isEmpty()) {
			for (ToaSysmanageDictitem item : list) {
				if (StringUtils.hasLength(item.getDictItemParentvalue())
						&& userId.equals(item.getDictItemParentvalue())) {// 查找当前用户的输入意见
					newList.add(item);
				}
			}
		}
		return newList;
	}

	/**
	 * 得到字典类
	 * 
	 * @author:邓志城
	 * @date:2009-12-26 下午12:50:37
	 * @param value
	 * @return
	 */
	public ToaSysmanageDict getDict(String value) {
		return typeManager.getDictByValue(value);
	}

	/**
	 * 保存当前用户的审批意见
	 * 
	 * @param dictId
	 *            字典类主键
	 * @param content
	 *            审批意见
	 * @author:邓志城
	 * @date:2009-12-26 上午10:55:26
	 * @throws SystemException
	 * @throws SQLException
	 */
	public void saveIdea(String dictId, String content) throws SystemException {
		if (!StringUtils.hasText(content) || !StringUtils.hasText(dictId)) {
			logger.info("字典类‘{}’或审批意见‘{}’为空,跳过保存！", dictId, content);
			return;
		}
		content = content.trim();
		UUIDGenerator gen = new UUIDGenerator();
		String id = gen.generate().toString();
		String userId = userService.getCurrentUser().getUserId();
		// DICT_ITEM_PARENTVALUE字段保存当前用户
		String sql = "insert into t_oa_sysmanage_dictitem"
				+ "(DICT_ITEM_CODE,DICT_CODE,DICT_ITEM_VALUE,DICT_ITEM_SHORTDESC,"
				+ "DICT_ITEM_PARENTVALUE,"
				+ "DICT_ITEM_NAME,DICT_ITEM_IS_SELECT) values('" + id + "','"
				+ dictId + "','" + content + "','" + content + "','" + userId
				+ "','" + content + "','0')";
		// 执行保存之前先查询是否已经存在输入意见
		String querySql = "select DICT_ITEM_NAME from t_oa_sysmanage_dictitem where DICT_ITEM_NAME='"
				+ content + "' and DICT_ITEM_PARENTVALUE = '" + userId + "'";
		ResultSet rs = super.executeJdbcQuery(querySql);
		try {
			if (!rs.next()) {
				logger.info("不存在输入意见“{}”", content);
				super.executeJdbcUpdate(sql);
			} else {
				logger.info("输入意见“{}”已存在。", content);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/*
	 * 
	 * Description:获取正文内容 param: @author 彭小青 @date Mar 10, 2010 10:07:59 AM
	 */
	public byte[] getContent(String tableName, String contentFiled,
			String pkey, String value, Connection con) throws SystemException {
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer("select ").append(contentFiled)
					.append(" from ").append(tableName).append(" where ")
					.append(pkey).append("='").append(value).append("'");
			psmt = con.prepareStatement(sql.toString()); // 查询父流程业务表数据
			rs = psmt.executeQuery();
			if (rs != null && rs.next()) {
				return rs.getBytes(1);
			}
		} catch (Exception e) {
			try {
				con.rollback();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SystemException("获取正文内容异常", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SystemException("获取正文内容异常", e);
			}
		}
		return null;
	}

	/*
	 * 
	 * Description: param: @author 彭小青 @date Mar 10, 2010 2:41:36 PM
	 */
	@SuppressWarnings("deprecation")
	public void saveAttachmentsForProcess(Connection con, String fkId,
			InputStream[] attachments, String[] fileNames)
			throws SystemException {
		PreparedStatement psmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			// 产生UUID
			UUIDGenerator gen = new UUIDGenerator();
			if (attachments != null && attachments.length > 0) {
				con.setAutoCommit(false);
				for (int i = 0; i < attachments.length; i++) {
					String sql = "insert into T_DOCATTACH(DOCATTACHID,DOC_ID,ATTACH_NAME,ATTACH_CONTENT)values(?,?,?,empty_blob())";
					String id = gen.generate().toString();
					psmt = con.prepareStatement(sql);
					psmt.setString(1, id);
					psmt.setString(2, fkId);
					psmt.setString(3, fileNames[i]);
					psmt.executeUpdate();
					sql = "select * from T_DOCATTACH where DOCATTACHID='" + id + "' for update";
					psmt = con.prepareStatement(sql);
					rs = psmt.executeQuery();
					if (rs.next()) {
						sql = "update T_DOCATTACH set ATTACH_CONTENT=? where DOCATTACHID=?";
						psmt = con.prepareStatement(sql);
						is = attachments[i];
						psmt.setBinaryStream(1, is, is.available());
						psmt.setString(2, id);
						psmt.executeUpdate();
					}
				}
				con.commit();
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SystemException("保存附件异常。", e);
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
				throw new SystemException("关闭记录集异常。", e);
			}
		}
	}

	/*
	 * Description:根据查询条件查询 param: @author 彭小青 @date Feb 26, 2010 12:03:25 PM
	 */
	public String saveSubProccessData(String bussinessId, String[] strData,
			String contentFileds) throws SystemException {
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			con = super.getConnection();
			con.setAutoCommit(false);
			String tableName = bussinessId.split(";")[0];
			String pkey = bussinessId.split(";")[1];
			String sendDocId = bussinessId.split(";")[2];
			UUIDGenerator generator = new UUIDGenerator();
			String pk = generator.generate().toString(); // 生成主键值
			String parentProccessFiledStr = manager.getParentProccessFiledStr(
					tableName, strData[0], strData[1]);// 查找子流程业务表字段对应的父流程业务表字段
			if (parentProccessFiledStr == null) {
				return null;
			}
			StringBuffer queryStr = new StringBuffer("select ").append(
					parentProccessFiledStr).append(" from ").append(tableName)
					.append(" where ").append(pkey).append("='").append(
							sendDocId).append("'");
			StringBuffer newqueryStr = new StringBuffer("select '").append(
					pk+"',"+parentProccessFiledStr).append(" from ").append(tableName)
					.append(" where ").append(pkey).append("='").append(
							sendDocId).append("'");

			psmt = con.prepareStatement(queryStr.toString()); // 查询父流程业务表数据
			rs = psmt.executeQuery();
			if (rs != null && rs.next()) {
				int size = strData[1].split(",").length;
				String params = "";
				for (int i = 0; i < size; i++) {
					params += ",?";
				}
//				String sql = "insert into " + strData[0] + "(" + strData[1]
//						+ ") values(" + params.substring(1) + ")"; // 组装插入sql语句
				String sql = "insert into " + strData[0] + "(" + strData[1]+ ")  "+newqueryStr.toString() ; // 组装插入sql语句

				psmt = con.prepareStatement(sql);
//				psmt.setString(1, pk);
//				for (int i = 1; i < size; i++) {
//					psmt.setObject(i + 1, rs.getObject(i));
//				}
				psmt.executeUpdate();
			}
			/** ***************以下是保存附件，且将正文保存为附件****************** */
			/*
			 * List<String> fileNameList = new ArrayList<String>(); //附件名字列表
			 * List<InputStream> attachMents = new ArrayList<InputStream>();
			 * //附件内容列表 List<Map<String, Object>> list =
			 * this.getAttachBySenddocId(sendDocId); //查找附件列表
			 * 
			 * for(Map attachBean : list){ String name =
			 * (String)attachBean.get("ATTACH_NAME"); byte[] attachContent =
			 * (byte[])attachBean.get("ATTACH_CONTENT"); fileNameList.add(name);
			 * attachMents.add(FileUtil.ByteArray2InputStream(attachContent)); }
			 * this.saveAttachmentsForProcess(con,pk, attachMents.toArray(new
			 * InputStream[attachMents.size()]), fileNameList.toArray(new
			 * String[fileNameList.size()]));
			 */
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(sendDocId);
			if (workflowAttachs != null && !workflowAttachs.isEmpty()) {
				for (WorkflowAttach attach : workflowAttachs) {
					WorkflowAttach model = new WorkflowAttach();
					model.setAttachContent(attach.getAttachContent());
					model.setAttachName(attach.getAttachName());
					model.setDocId(pk);
					workflowAttachManager.saveWorkflowAttach(model);
				}
			}
			con.commit();
			return pk;
		} catch (Exception e) {
			try {
				con.rollback();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SystemException("插入记录失败。", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SystemException("关闭记录集异常。", e);
			}
		}
	}

	/**
	 * 根据公文ID获取附件
	 * 
	 * @author:邓志城
	 * @date:2009-7-14 下午03:15:23
	 * @param senddocId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<Map<String, Object>> getAttachBySenddocId(String senddocId)
			throws ServiceException, SystemException {
		ByteArrayOutputStream bos = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			List<Map<String, Object>> attachLst = new ArrayList<Map<String, Object>>();
			String sql = "select ATTACH_NAME,ATTACH_CONTENT from T_DOCATTACH t where t.DOC_ID='" + senddocId + "'";
			rs = super.executeJdbcQuery(sql);
			while (rs.next()) {
				Map<String, Object> attachMap = new HashMap<String, Object>();
				String attachName = rs.getString("ATTACH_NAME");
				attachMap.put("ATTACH_NAME", attachName);
				is = rs.getBinaryStream("ATTACH_CONTENT");
				bos = new ByteArrayOutputStream();
				int input = 0;
				byte[] buf = new byte[8192];
				while ((input = is.read(buf)) != -1) {
					bos.write(buf, 0, input);
				}
				byte[] attachContent = bos.toByteArray();
				attachMap.put("ATTACH_CONTENT", attachContent);
				attachLst.add(attachMap);
			}
			return attachLst;
		} catch (Exception e) {
			throw new SystemException("查询公文附件出错了。");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (is != null) {
					is.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据附件表中的外键删除附件。 通过JDBC方式删除。
	 * 
	 * @author:邓志城
	 * @date:2010-4-28 下午04:27:44
	 * @param id
	 *            外键id
	 */
	public void deleteAttach(String[] id) {
		if (id != null && id.length > 0) {
			for (String docId : id) {
				List<WorkflowAttach> attachs = workflowAttachManager.getWorkflowAttachsByDocId(docId);
				if (attachs != null && !attachs.isEmpty()) {
					for (WorkflowAttach attach : attachs) {
						workflowAttachManager.delete(attach.getDocattachid());
					}
				}
			}
		}
	}

	/*
	 * 
	 * Description:根据表单模板id得到表名称 param: @author 彭小青 @date Feb 25, 2010 1:42:27
	 * PM
	 */
	public String getTNByFormId(String formId) {
		List<EFormField> list = eform.getFormTemplateFieldList(formId);// 根据表单获取表单域信息列表
		String tableName = "";
		EFormField eformField;
		for (int i = 0; list != null && i < list.size(); i++) {
			eformField = list.get(i);
			if (eformField != null) {
				tableName = eformField.getTablename();
				break;
			}
		}
		return tableName;
	}

	/*
	 * 
	 * Description:根据表单获取正文字段 param: @author 彭小青 @date Mar 10, 2010 10:56:19 AM
	 */
	public String getContentFiled(String formId) {
		List<EFormField> list = eform.getFormTemplateFieldList(formId);// 根据表单获取表单域信息列表
		EFormField eformField;
		String contentFiled = "";
		for (int i = 0; list != null && i < list.size(); i++) {
			eformField = list.get(i);
			if (eformField != null && "TEFOffice".equals(eformField.getType())) {
				contentFiled += eformField.getFieldname() + ",";
			}
		}
		return contentFiled;
	}

	/*
	 * 
	 * Description:根据表单模板id得到表名称 param: @author 彭小青 @date Feb 26, 2010 10:35:31
	 * AM
	 */
	@Transactional(readOnly = true)
	public String[] getTableNameByFormId(String formId) {
		String[] tableAndFileds = new String[2];
		String table = "";
		StringBuffer column = new StringBuffer();
		EFormField eformField;
		List<EFormField> list = super.getFormTemplateFieldOrderByPrimaryKey(formId); // 根据表单ID获取表单域信息列表（主键排第一）
		for (int i = 0; list != null && i < list.size(); i++) {
			eformField = list.get(i);
			if (eformField != null) {
				table = eformField.getTablename();
				break;
			}
		}
		if ("".equals(table)) {
			return null;
		}
		for (int k = 0; k < list.size(); k++) {
			eformField = list.get(k);
			if (eformField.getTablename() == null) {// yanjian 2011-12-17 20:42
				continue;
			}
			if (eformField != null && eformField.getTablename().equals(table)) {
				column.append(eformField.getFieldname()).append(",");
			}
		}
		if (column.length() > 0) {
			column = column.deleteCharAt(column.length() - 1);
			tableAndFileds[0] = table;
			tableAndFileds[1] = column.toString();
		}
		return tableAndFileds;
	}

	/**
	 * 数据复制,发起新流程将父流程数据传递到新流程中
	 * 
	 * @author:邓志城
	 * @date:2010-3-19 下午01:57:57
	 * @param bussinessId
	 * @param formId
	 *            返回发起新流程对应的业务数据和表单id
	 */
	public String copyData(String bussinessId, String formId,
			OALogInfo... infos) {
		String ret = null;
		if (bussinessId.indexOf(";") == -1) {
			bussinessId = "T_OARECVDOC" + ";" + "OARECVDOCID" + ";" + bussinessId;
		}
		String[] strData = this.getTableNameByFormId(formId); // 得到发起新流程的表单id
		if (strData != null && strData[0] != null && strData[1] != null) {
			String pkey = strData[1].split(",")[0];
			String pk = this.saveSubProccessData(bussinessId, strData, "");
			ret = strData[0] + ";" + pkey + ";" + pk;
		}
		return ret;
	}

	/**
	 * 保存附件
	 * 
	 * @author:邓志城
	 * @date:2009-7-9 下午04:05:20
	 * @param fkId
	 * @param attachments
	 * @param fileNames
	 */
	@SuppressWarnings("deprecation")
	public void saveAttachments(String fkId, InputStream[] attachments,
			String[] fileNames) throws SystemException {
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			// transfer(attachments, fileNames);
			con = getConnection();
			// 产生UUID
			UUIDGenerator gen = new UUIDGenerator();
			if (attachments != null && attachments.length > 0) {
				con.setAutoCommit(false);
				for (int i = 0; i < attachments.length; i++) {
					String sql = "insert into T_DOCATTACH(DOCATTACHID,DOC_ID,ATTACH_NAME,ATTACH_CONTENT)values(?,?,?,empty_blob())";
					String id = gen.generate().toString();
					psmt = con.prepareStatement(sql);
					psmt.setString(1, id);
					psmt.setString(2, fkId);
					psmt.setString(3, fileNames[i]);
					psmt.executeUpdate();
					sql = "select * from T_DOCATTACH where DOCATTACHID='" + id + "' for update";
					psmt = con.prepareStatement(sql);
					rs = psmt.executeQuery();
					if (rs.next()) {
						sql = "update T_DOCATTACH set ATTACH_CONTENT=? where DOCATTACHID=?";
						psmt = con.prepareStatement(sql);
						is = attachments[i];
						psmt.setBinaryStream(1, is, is.available());
						psmt.setString(2, id);
						psmt.executeUpdate();
					}
				}
				con.commit();
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SystemException("保存附件异常。", e);
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
				throw new SystemException("关闭记录集异常。", e);
			}
		}
	}

	/**
	 * 校验指定名称并且外键为id的附件是否存在.
	 * 
	 * @author:邓志城
	 * @date:2010-9-10 下午06:15:56
	 * @param id
	 *            外键id
	 * @param fileName
	 *            附件名称
	 * @return 查询结果
	 * @throws Exception
	 */
	public String isAttachExist(String id, String fileName) throws Exception {
		/*
		 * String sql = "select count(*) from T_DOCATTACH t where
		 * t.DOC_ID='"+id+"' and t.ATTACH_NAME = '" + fileName + "'"; ResultSet
		 * rs = super.executeJdbcQuery(sql); String ret = ""; if(rs.next()){
		 * Object result = rs.getObject(1); if(result != null){
		 * if(!"".equals(result) && Integer.parseInt(result.toString()) > 0){
		 * String queryResult = "select t.DOCATTACHID from T_DOCATTACH t where
		 * t.DOC_ID='"+id+"' and t.ATTACH_NAME = '" + fileName + "'"; ResultSet
		 * queryRet = super.executeJdbcQuery(queryResult); if(queryRet.next()){
		 * ret = "1," + queryRet.getString(1); } } } } return ret;
		 */
		String sql = "select t.DOCATTACHID from T_DOCATTACH t where t.DOC_ID='"
				+ id + "' and t.ATTACH_NAME = '" + fileName + "'";
		ResultSet rs = super.executeJdbcQuery(sql);
		String ret = "";
		if (rs.next()) {
			ret = "1," + rs.getString(1);
		}
		return ret;
	}

	/**
	 * 更新附件信息.
	 * 
	 * @author:邓志城
	 * @date:2010-3-9 上午11:19:17
	 * @param id
	 * @param wordDoc
	 * @throws Exception
	 */
	public void updateAttach(String id, InputStream is) throws Exception {
		WorkflowAttach model = workflowAttachManager.getWithoutContent(id);
		byte[] attachContent = FileUtil.inputstream2ByteArray(is);
		model.setAttachContent(attachContent);
		workflowAttachManager.saveWorkflowAttach(model);
	}
	public void updateAttachs(InputStream is) throws Exception {
		workflowAttachManager.savetoAttachs(FileUtil.inputstream2ByteArray(is));
	}

	/**
	 * 根据附件id得到附件内容
	 * 
	 * @author:邓志城
	 * @date:2010-10-11 下午03:20:53
	 * @param id
	 *            附件id
	 * @return
	 */
	public InputStream getAttachById(String id) {
		InputStream is = null;
		WorkflowAttach attach = workflowAttachManager.get(id);
		byte[] attachContent = attach.getAttachContent();
		if (attachContent == null) {
			return null;
		}
		try {
			is = FileUtil.ByteArray2InputStream(attachContent);
		} catch (Exception e) {
			throw new SystemException("保存附件异常。", e);
		}
		return is;
	}
	/**
	 * 根据附件id得到附件内容
	 * 
	 * @author:邓志城
	 * @date:2010-10-11 下午03:20:53
	 * @param id
	 *            附件id
	 * @return
	 * @throws SQLException 
	 * @throws HibernateException 
	 */
	public InputStream getAttachByIds(String id,String tableNames,String idName) throws HibernateException, SQLException {
		InputStream is = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "SELECT * from "+tableNames+" t where "+idName+" = '"+id+"'";
		psmt = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
		rs = psmt.executeQuery();
		String ATTACH_PATH = "";
		while(rs.next()){
			ATTACH_PATH = rs.getString("ATTACH_PATH");
		}
		byte[] attachContent = workflowAttachManager.gets(ATTACH_PATH);
		if (attachContent == null) {
			return null;
		}
		try {
			is = FileUtil.ByteArray2InputStream(attachContent);
		} catch (Exception e) {
			throw new SystemException("保存附件异常。", e);
		}
		return is;
	}

	/**
	 * 将字节写入到Http输出流中.
	 * 
	 * @author:邓志城
	 * @date:2010-10-11 下午02:40:20
	 * @param buf
	 *            字节数组
	 */
	public void openInputStreamToHttpResponse(byte[] buf) {
		if (buf != null) {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			response.setContentType("application/octet-stream");
			OutputStream output = null;
			try {
				output = response.getOutputStream();
				output.write(buf);
			} catch (Exception e) {
				logger.error("写入到HttpResponse中发生异常。", e);
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						logger.error("关闭输出流时发生异常", e);
					}
				}
			}
		}
	}

	/**
	 * 保存表单数据,并更新数据,初始化流程名称,拟稿人,流程状态,流程标题
	 * 
	 * @author:邓志城
	 * @date:2010-11-9 上午11:32:44
	 * @param data
	 * @param workflowName
	 */
	public VoFormDataBean saveFormData(File data, String workflowName,
			String workflowState, String businessName) {
		String businessId = null;
		VoFormDataBean bean = null;
		if (data != null) {
			bean = eform.saveFormData(data);
			businessId = bean.getBusinessId();
		}
		this.update(businessId, workflowName, workflowState, businessName);
		return bean;
	}

	/**
	 * 保存表单模板数据,并返回生成的流信息
	 * 
	 * @param data
	 * @param workflowName
	 * @param workflowState
	 * @param businessName
	 * @return
	 */
	public VoFormDataBean saveFormData(InputStream data, String workflowName,
			String workflowState, String businessName, String instanceId,
			String taskId, String userId) {
		VoFormDataBean bean = null;
		if (data != null) {
			bean = eform.saveFormData(data);
			if (businessName != null && businessName.length() > 0) {
				if ("0".equals(workflowState)) {// 记录草稿状态的意见
					approveManager.save(businessName, bean.getBusinessId().split(";")[2], userId, null);
				}
				logger.info("保存意见成功。");
			}
		}
		this.update(bean.getBusinessId(), workflowName, workflowState, null);
		// 更新流程实例中的BusinessName
		if (instanceId != null && !instanceId.equals("")) {
			String businessId = bean.getBusinessId();
			String[] datas = businessId.split(";");
			String tableName = datas[0];
			String pkFieldName = datas[1];
			String pkFieldValue = datas[2];
			String sql = "select WORKFLOWTITLE from " + tableName + " where " + pkFieldName + "='" + pkFieldValue + "'";
			Map map = queryForMap(sql);
			String workflowtitle = map.get("WORKFLOWTITLE").toString();
			ProcessInstance processInstance = workflow.getProcessInstanceById(instanceId);
			processInstance.setBusinessName(workflowtitle);
		}
		// String pdfpath =
		// ServletActionContext.getRequest().getSession().getServletContext().getRealPath("");
		// String pdfinfo =
		// ServletActionContext.getRequest().getParameter("pdfContentInfo");
		// //
		// System.out.println("\n\n\n\n\n\n\npdfinfo:"+pdfinfo+"\n\\n\\n\\n\\n\n\n");
		// PdfTempFileHelper pdfhelp = new PdfTempFileHelper();
		// if(pdfhelp.updatePdfFile(pdfpath,pdfinfo)){
		// throw new SystemException("pdf更新失败");
		// }
		return bean;
	}

	/**
	 * 更新业务表数据 在更新时应该先查询是否需要更新系统字段
	 * 
	 * @author:邓志城
	 * @date:2010-11-16 上午10:15:14
	 * @param businessId
	 *            业务表数据标识 tableName;pkFieldName;pkFieldValue
	 * @param workflowName
	 *            流程名称
	 * @param workflowState
	 *            流程状态 0：草稿；1：非草稿
	 */
	public void update(String businessId, String workflowName, String workflowState, String businessName) {
		// if (workflowName != null && !"".equals(workflowName)
		// && !"undefined".equals(workflowName) && !"".equals(businessId)) {
		if (businessId.indexOf(";") != -1) {
			String[] tableInfo = businessId.split(";");
			// if (workflowState == null || "".equals(workflowState)) {
			// return;
			// }
			// Map map = getSystemField(tableInfo[1], tableInfo[2],
			// tableInfo[0]);
			// if (map != null) {
			// StringBuilder Sql = new StringBuilder("Update ")
			// .append(tableInfo[0]);
			// Sql.append(" t Set t.").append(WORKFLOW_STATE).append("='")
			// .append(workflowState).append("'");
			// if (map.get(WORKFLOW_AUTHOR) == null
			// || "".equals(map.get(WORKFLOW_AUTHOR))) {// 拟稿人不存在
			// Sql.append(",t.").append(WORKFLOW_AUTHOR).append("='")
			// .append(
			// userService.getCurrentUser()
			// .getUserId()).append("'");
			// }
			// if (map.get(WORKFLOW_TITLE) == null
			// || "".equals(map.get(WORKFLOW_TITLE))) {
			// if (businessName != null && !"".equals(businessName)) {
			// Sql.append(",t.").append(WORKFLOW_TITLE).append(
			// "='").append(businessName).append("'");
			// }
			// }
			// if (map.get(WORKFLOW_NAME) == null
			// || "".equals(map.get(WORKFLOW_NAME))) {
			// Sql.append(",t.").append(WORKFLOW_NAME).append("='")
			// .append(workflowName).append("'");
			// }
			// Sql.append(" Where t.").append(tableInfo[1]).append("='")
			// .append(tableInfo[2]).append("'");
			//					
			// jdbcTemplate.update(Sql.toString());
			// logger.info("Update:" + Sql.toString());
			Map map = getSystemField(tableInfo[1], tableInfo[2], tableInfo[0]);
			StringBuilder Sql = new StringBuilder("");
			if (map != null) {
				if (map.get(WORKFLOW_STATE) != null
						&& !"".equals(map.get(WORKFLOW_STATE))) {// 如果存在不为null或者空字符串的流程状态
					String workflow_state = map.get(WORKFLOW_STATE).toString();
					if("0".equals(workflow_state)){//草稿
						
					}else if("1".equals(workflow_state)){//走进入流程
						
					}else if("2".equals(workflow_state)){//已归档
						
					}else if("3".equals(workflow_state)){//公文补录
						/**
						 * 如果是公文补录的公文，不管是通过补录模块进行流转还是
						 * 通过公文办理模块流转，都将workflowState设置为“3”
						 * */
						workflowState = "3";
					}
				}
				Sql.append("Update ").append(tableInfo[0]);
				Sql.append("  Set ").append(WORKFLOW_STATE).append("='")
						.append(workflowState).append("'");
				if (map.get(WORKFLOW_AUTHOR) == null
						|| "".equals(map.get(WORKFLOW_AUTHOR))) {// 拟稿人不存在
					Sql.append(",").append(WORKFLOW_AUTHOR).append("='")
							.append(userService.getCurrentUser().getUserId())
							.append("'");
				}
				if (map.get(WORKFLOW_TITLE) == null
						|| "".equals(map.get(WORKFLOW_TITLE))) {
					if (businessName != null && !"".equals(businessName)) {
						Sql.append(",").append(WORKFLOW_TITLE).append("='")
								.append(businessName).append("'");
					}
				}
				if (map.get(WORKFLOW_NAME) == null
						|| "".equals(map.get(WORKFLOW_NAME))) {
					if (workflowName != null && !"".equals(workflowName)) {
						Sql.append(",").append(WORKFLOW_NAME).append("='")
								.append(workflowName).append("'");
					}
				}
			}
			String pdfpath = ServletActionContext.getRequest().getSession().getServletContext().getRealPath("");
			String pdfinfo = ServletActionContext.getRequest().getParameter("pdfContentInfo");
			// System.out.println("\n\n\n\n\n\n\npdfinfo:"+pdfinfo+"\n\\n\\n\\n\\n\n\n");
			String PDFCONTENT = "";
			String PDFCONTENT_SIZE = "";
			int fileSize = 0;
			InputStream is = null;
			if (!"".equals(pdfinfo) && pdfinfo != "" && pdfinfo != null) {
				String[] data = pdfinfo.split(";");
				if (data.length == 4) {
					PDFCONTENT = data[1];
					PDFCONTENT_SIZE = data[2];
					pdfinfo = data[0] + ";" + data[1] + ";" + data[2];
					PdfTempFileHelper pdfhelp = new PdfTempFileHelper();
					fileSize = pdfhelp.getPdfFileSize(pdfpath, pdfinfo);
					is = pdfhelp.getPdfFile(pdfpath, pdfinfo);
					if (fileSize != 0 && is != null) {
						Sql.append(",").append(PDFCONTENT_SIZE).append("=?");
//						Sql.append(",").append(PDFCONTENT).append("=?");
					}
				} else if(data.length ==1){
					String fileName  = data[0];
					PdfTempFileHelper pdfhelp = new PdfTempFileHelper();
					is = pdfhelp.getPdfFileL(pdfpath, fileName);
					fileSize = pdfhelp.getPdfSize(pdfpath, pdfinfo);
					if (fileSize != 0 && is != null) {
						Sql.append(",").append("PDFCONTENT_SIZE").append("=?");
//						Sql.append(",").append("PDFCONTENT").append("=?");
					}
				}else {
					if (data.length < 3) {
						pdfinfo = businessId + ";" + pdfinfo;
						data = pdfinfo.split(";");
					}
					PDFCONTENT = data[3];
					PDFCONTENT_SIZE = data[4];
					PdfTempFileHelper pdfhelp = new PdfTempFileHelper();
					fileSize = pdfhelp.getPdfFileSize(pdfpath, pdfinfo);
					is = pdfhelp.getPdfFile(pdfpath, pdfinfo);
					if (fileSize != 0 && is != null) {
						Sql.append(",").append(PDFCONTENT_SIZE).append("=?");
//						Sql.append(",").append(PDFCONTENT).append("=?");
					}
				}
			}
			Sql.append(" Where ").append(tableInfo[1]).append("='").append(
					tableInfo[2]).append("'");
			if (!"".equals(pdfinfo) && pdfinfo != "" && fileSize != 0 && is != null) {
				Connection con = this.getConnection();
				PreparedStatement psmt = null;
				ResultSet rs = null;
				try {
					con.setAutoCommit(false);
//					String sql = "";
//					sql = "select PDFCONTENT_SIZE,PDFCONTENT from "
//							+ tableInfo[0] + " where  " + tableInfo[1] + "='"
//							+ tableInfo[2] + "' for update";
//					psmt = con.prepareStatement(sql);
//					rs = psmt.executeQuery();
//					if (rs.next()) {
						// sql = "update "+tableInfo[0]+" set PERSON_DEMO=?
						// where "+tableInfo[1]+"='"+tableInfo[2]+"'";
						// psmt = con.prepareStatement(sql);
						psmt = con.prepareStatement(Sql.toString());
						if (!"".equals(pdfinfo) && pdfinfo != ""
								&& fileSize != 0 && is != null) {
							psmt.setInt(1, fileSize);
//							psmt.setBinaryStream(2, is, is.available());
						}
						psmt.executeUpdate();
//					}
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
			} else {
				jdbcTemplate.update(Sql.toString());
			}
			Tjbpmbusiness tjbpmbusiness = jbpmbusinessManager.findByBusinessId(businessId);
			if(tjbpmbusiness == null){
				tjbpmbusiness = new Tjbpmbusiness(businessId);
			}
			/*add yanjian 2012-06-12 20:55*/
			if("3".equals(workflowState)){	//公文补录的公文,设置BusinessType为3
				if(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_COMMON.equals(tjbpmbusiness.getBusinessType())){//公文补录状态
					
				}else if(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_YJZX.equals(tjbpmbusiness.getBusinessType())){
					
				}else if(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_YJZX_SUSPEND.equals(tjbpmbusiness.getBusinessType())){
					
				}else{
					tjbpmbusiness.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_COMMON);
				}
			}
			jbpmbusinessManager.saveModel(tjbpmbusiness);
		}
	}
	// }
	
	/**
	 * 根据表名称，主键名，主键值查询指定的字段值
	 * 
	 * @author:邓志城
	 * @date:2010-11-19 上午09:07:04
	 * @param fieldName
	 * @param tableName
	 * @param pkFieldName
	 * @param pkFieldValue
	 * @return
	 */
	@Transactional(readOnly = true)
	public Object getPdfNameByBuss(String fieldName, String tableName,
			String pkFieldName, String pkFieldValue) {
		if (pkFieldValue == null || "".equals(pkFieldValue)) {
			logger.error("参数pkFieldValue为空。");
			return null;
		}
		if (tableName == null || "".equals(tableName)) {
			logger.error("参数tableName为空。");
			return null;
		}
		if (pkFieldName == null || "".equals(pkFieldName)) {
			pkFieldName = super.getPrimaryKeyName(tableName);
		}
		StringBuilder Sql = new StringBuilder("select ");
		Sql.append(fieldName).append(" from ").append(tableName).append(
				" where ").append(pkFieldName).append(" ='").append(
				pkFieldValue).append("'");
		String fileName="";
		try {
			System.out.println(jdbcTemplate);
			List list = super.jdbcTemplate.queryForList(Sql.toString());
			System.out.println(jdbcTemplate);
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					fileName = ((String) map.get(fieldName));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return fileName;
	}
	/**
	 * 根据主键id,表名称得到默认的信息项字段
	 * 
	 * @author:邓志城
	 * @date:2010-11-13 上午09:32:26
	 * @param id
	 *            主键值
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map getSystemField(String id, String tableName) {
		return getSystemField(null, id, tableName);
	}

	/**
	 * 根据主键名,主键值,表名获取默认的字段信息
	 * 
	 * @author:邓志城
	 * @date:2010-11-16 下午01:48:36
	 * @param pkFieldName
	 *            主键名称 为空时将根据物理表名称自动得到主键名称
	 * @param pkFieldValue
	 *            主键值
	 * @param tableName
	 *            表名称
	 * @return Map<字段名称,字段值>
	 */
	@Transactional(readOnly = true)
	public Map getSystemField(String pkFieldName, String pkFieldValue,
			String tableName) {
		if (pkFieldValue == null || "".equals(pkFieldValue)) {
			logger.error("参数pkFieldValue为空。");
			return null;
		}
		if (tableName == null || "".equals(tableName)) {
			logger.error("参数tableName为空。");
			return null;
		}
		if (pkFieldName == null || "".equals(pkFieldName)) {
			pkFieldName = super.getPrimaryKeyName(tableName);
		}
		StringBuilder sql = new StringBuilder("SELECT ");
		List systemField = infoItemManager.getSystemField();// 得到默认的工作流字段
		String strSystemField = org.apache.commons.lang.StringUtils.join(
				systemField, ',');
		sql.append(strSystemField);
		sql.append(" FROM ").append(tableName).append(" WHERE ").append(
				pkFieldName);
		sql.append(" = ?");
		return jdbcTemplate.queryForMap(sql.toString(),
				new Object[] { pkFieldValue });
	}

	/**
	 * 根据表名称，主键名，主键值查询指定的字段值
	 * 
	 * @author:邓志城
	 * @date:2010-11-19 上午09:07:04
	 * @param fieldName
	 * @param tableName
	 * @param pkFieldName
	 * @param pkFieldValue
	 * @return
	 */
	@Transactional(readOnly = true)
	public Object getFieldValue(String fieldName, String tableName,
			String pkFieldName, String pkFieldValue) {
		if (pkFieldValue == null || "".equals(pkFieldValue)) {
			logger.error("参数pkFieldValue为空。");
			return null;
		}
		if (tableName == null || "".equals(tableName)) {
			logger.error("参数tableName为空。");
			return null;
		}
		if (pkFieldName == null || "".equals(pkFieldName)) {
			pkFieldName = super.getPrimaryKeyName(tableName);
		}
		StringBuilder Sql = new StringBuilder("select ");
		Sql.append(fieldName).append(" from ").append(tableName).append(
				" where ").append(pkFieldName).append(" ='").append(
				pkFieldValue).append("'");
		ResultSet rs = executeJdbcQuery(Sql.toString());
		try {
			if (rs.next()) {
				Object obj = rs.getObject(fieldName);
				if (obj instanceof byte[]) {
					return new ByteArrayInputStream((byte[]) obj);
				} else if (obj instanceof Blob) {// edit by luosy at 20110311
					return new ByteArrayInputStream(FileUtil
							.blobToBytes((Blob) obj));
					// return ((Blob) obj).getBinaryStream();
					// return (InputStream)obj;
				} else if (obj instanceof InputStream) {
					return (InputStream) obj;
				} else {
					return obj;
				}
			}
		} catch (SQLException e) {
			logger.error("jdbc query", e);
			throw new SystemException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
		}
		/*
		 * Map map = jdbcTemplate.queryForMap(Sql.toString(),new
		 * Object[]{pkFieldValue}); if(map != null) { Object obj =
		 * map.get(fieldName); if(obj instanceof byte[]) { return new
		 * ByteArrayInputStream((byte[])obj); }else if(obj instanceof
		 * InputStream) { return (InputStream)obj; }else{ return obj; } }
		 */
		return null;
	}

	/**
	 * 得到主办流程列表
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 上午11:54:19
	 * @param workflowType
	 *            流程类型
	 * @return Object[为完成的流程List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量],已完成的流程>
	 * @modify  yanjian 2012-04-28 18:58 	添加处理：找不到流程表单的流程不显示 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getHostedWorkflow(String workflowType) {
		List<Object[]> leftList = new ArrayList<Object[]>();
		List<Object[]> rightList = new ArrayList<Object[]>();
		try {
			Object[] toSelectItems = { "processName", "processTypeId",
					"processTypeName", "processMainFormId", "processEndDate" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if (workflowType != null && !"".equals(workflowType)) {
				paramsMap.put("processTypeId",
						genWorkflowTypeList(workflowType));
			}
			paramsMap.put("startUserId", userService.getCurrentUser()
					.getUserId());// 发起人是当前用户
			paramsMap.put("processSuspend","0");//非挂起
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("processTypeId", "0");
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			List list= workflow.getProcessInstanceByConditionForList(sItems, paramsMap, orderMap, customSelectItems.toString(), customFromItems.toString(), customQuery.toString(), null, null);
			Map<String, List<Object[]>> endedMap = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
			Map<String, List<Object[]>> doingMap = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
			List<Object[]> endedProcessList = new LinkedList<Object[]>();// 已经结束的流程列表
			List<Object[]> doingProcessList = new LinkedList<Object[]>();// 正在执行的流程列表
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					Object endDate = objs[4];
					if (endDate == null) {// 未结束的流程
						doingProcessList.add(objs);
					} else {
						endedProcessList.add(objs);
					}
				}
				for (int j = 0; j < endedProcessList.size(); j++) {
					Object[] endedObjs = endedProcessList.get(j);
					if (!endedMap.containsKey(endedObjs[0].toString() + "$"
							+ endedObjs[1].toString())) {
						List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
						workflowSet.add(endedObjs);
						endedMap.put(endedObjs[0].toString() + "$"
								+ endedObjs[1].toString(), workflowSet);
					} else {
						endedMap.get(
								endedObjs[0].toString() + "$"
										+ endedObjs[1].toString()).add(
								endedObjs);
					}
				}
				for (int j = 0; j < doingProcessList.size(); j++) {
					Object[] doingObjs = doingProcessList.get(j);
					if (!doingMap.containsKey(doingObjs[0].toString() + "$"
							+ doingObjs[1].toString())) {
						List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
						workflowSet.add(doingObjs);
						doingMap.put(doingObjs[0].toString() + "$"
								+ doingObjs[1].toString(), workflowSet);
					} else {
						doingMap.get(
								doingObjs[0].toString() + "$" + doingObjs[1].toString()).add(doingObjs);
					}
				}
				List<String> checkLeftList = new ArrayList<String>();// 处理重复的记录
				List<String> checkRightList = new ArrayList<String>();// 处理重复的记录
				for (int j = 0; j < doingProcessList.size(); j++) {
					Object[] objs = (Object[]) doingProcessList.get(j);
					if (!checkLeftList.contains(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List l = doingMap.get(objs[0].toString() + "$"
								+ objs[1].toString());
						if (l != null) {
							leftList.add(new Object[] { objs[0], objs[1],
									objs[2], objs[3], l.size() });
							checkLeftList.add(objs[0].toString() + "$"
									+ objs[1].toString());
						}
					}
				}
				for (int j = 0; j < endedProcessList.size(); j++) {
					Object[] objs = (Object[]) endedProcessList.get(j);
					if (!checkRightList.contains(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List l = endedMap.get(objs[0].toString() + "$"
								+ objs[1].toString());
						if (l != null) {
							rightList.add(new Object[] { objs[0], objs[1],
									objs[2], objs[3], l.size() });
							checkRightList.add(objs[0].toString() + "$"
									+ objs[1].toString());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("查询主办流程时发生异常", e);
			throw new SystemException(e);
		}
		return new Object[] { leftList, rightList };
	}

	/**
	 * 得到已办流程列表 过滤掉重复的已办任务
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 上午11:54:19
	 * @param workflowType
	 *            流程类型
	 * @param processStatus
	 *            流程状态 0：未办结流程，1：已办结流程，2：所有流程
	 * @param processStatus
	 *            是否要过滤签收数据 0：否，1：是
	 * @return List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getProcessedWorkflow(String workflowType,
			String processStatus, String filterSign) {
		if (filterSign == null || "".equals(filterSign)) {
			filterSign = "0";
		}
		Object[] sItems = { "processName", "processTypeId", "processTypeName",
				"processMainFormId" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processSuspend", "0");// 取未挂起任务
		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();
		String userId = userService.getCurrentUser().getUserId();
		StringBuilder customSelectItems = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customQuery = new StringBuilder();
		customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			if(workflowType.startsWith("-")){
				System.out.println(workflowType.substring(1));
				customQuery.append(" and  pi.TYPE_ID_ not in(" + workflowType.substring(1)+ ") ");
			}else{
				customQuery.append(" and pi.TYPE_ID_ in(" + workflowType + ") ");
			}
			
		}
		ProcessedParameter processedParameter = new ProcessedParameter();
		processedParameter.setFilterSign(filterSign);
		processedParameter.setUserId(userId);
		processedParameter.setCustomFromItems(customFromItems);
		processedParameter.setCustomQuery(customQuery);
		processedParameter.setQueryWithTaskDate(null);
		initProcessedFilterSign(processedParameter);
		logger.info(customFromItems + "\n" + customQuery);
		List list = workflow.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, orderMap, customSelectItems
						.toString(), customFromItems.toString(), customQuery
						.toString(), null, null);
		destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
				customSelectItems, customFromItems, customQuery, null, null);

		Map<String, List<Object[]>> map = null;// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
		List<Object[]> workflowList = new ArrayList<Object[]>();
		List<Long> allTEFormTemplateId = eform.getAllTEFormTemplateId();
		if (list != null && !list.isEmpty()) {
			for (Object object : list) {
				if (map == null) {
					map = new HashMap<String, List<Object[]>>();
				}
				Object[] objs = (Object[]) object;
				String[] formidinfo = objs[3].toString().split(",");
				Long mainFromId = new Long(formidinfo[formidinfo.length-1]);
				if(allTEFormTemplateId.indexOf(mainFromId) == -1){
				    continue;
				}
				if (!map.containsKey(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
					workflowSet.add(objs);
					map.put(objs[0].toString() + "$" + objs[1].toString(),
							workflowSet);
				} else {
					map.get(objs[0].toString() + "$" + objs[1].toString()).add(
							objs);
				}
			}
			List<String> checkList = null;// 处理重复的记录
			for (Object object : list) {
				if (checkList == null) {
					checkList = new LinkedList<String>();
				}
				Object[] objs = (Object[]) object;
				if (!checkList.contains(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List l = map.get(objs[0].toString() + "$"
							+ objs[1].toString());
					if (l != null) {
						workflowList.add(new Object[] { objs[0], objs[1],
								objs[2], objs[3], l.size() });
						checkList.add(objs[0].toString() + "$"
								+ objs[1].toString());
					}
				}
			}
			checkList = null;
			map = null;
			list = null;
		}
		return workflowList;
	}
	/**
	 * 得到已办流程列表 过滤掉重复的已办任务
	 * 
	 * @description
	 * @author 严建
	 * @param parameter
	 * @return
	 * @createTime Mar 27, 2012 10:54:07 AM
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getProcessedWorkflow(ProcessedParameter parameter) {
		String workflowType = parameter.getWorkflowType();
		String excludeWorkflowType = parameter.getExcludeWorkflowType();
		String processStatus = parameter.getProcessStatus();
		String filterSign = parameter.getFilterSign();
		String excludeWorkflowTypeName=parameter.getExcludeWorkflowTypeName();
		if (filterSign == null || "".equals(filterSign)) {
			filterSign = "0";
		}
		Object[] sItems = { "processName", "processTypeId", "processTypeName",
		"processMainFormId" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processSuspend", "0");// 取未挂起任务
//		if (workflowType != null && !"".equals(workflowType)) {
//			paramsMap.put("processTypeId", genWorkflowTypeList(workflowType));
//		}
		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();
		
		String userId = userService.getCurrentUser().getUserId();
		StringBuilder customSelectItems = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customQuery = new StringBuilder();
		customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
		customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
		customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
		if (workflowType != null
				&& !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			if (customQuery.length() == 0) {
				customQuery.append("  pi.TYPE_ID_ in(" + workflowType + ") ");
			} else {
				customQuery.append(" and pi.TYPE_ID_ in(" + workflowType + ") ");
			}
		}
		//流程类型名称，过滤掉该流程类型名称的数据
		if (excludeWorkflowTypeName != null && !"".equals(excludeWorkflowTypeName)
				&& !"null".equals(excludeWorkflowTypeName)) {
			if (customQuery.length() == 0) {
				customQuery.append("  pi.NAME_ not like '%"
						+ excludeWorkflowTypeName +"%' ");
			} else {
				customQuery.append(" and pi.NAME_ not like '%"
						+ excludeWorkflowTypeName +"%' ");
			}
		}
		if (excludeWorkflowType != null
				&& !"".equals(excludeWorkflowType)
				&& !"null".equals(excludeWorkflowType)) {
			if (customQuery.length() == 0) {
				customQuery.append("  pi.TYPE_ID_ not in(" + excludeWorkflowType + ") ");
			} else {
				customQuery.append(" and pi.TYPE_ID_ not in(" + excludeWorkflowType + ") ");
			}
		}
		ProcessedParameter processedParameter = new ProcessedParameter();
		processedParameter.setFilterSign(filterSign);
		processedParameter.setUserId(userId);
		processedParameter.setCustomFromItems(customFromItems);
		processedParameter.setCustomQuery(customQuery);
		processedParameter.setQueryWithTaskDate(null);
		initProcessedFilterSign(processedParameter);
		logger.info(customFromItems + "\n" + customQuery);
		List list = workflow.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, orderMap, customSelectItems
				.toString(), customFromItems.toString(), customQuery
				.toString(), null, null);
		destroyProcessInfosData(toSelectItems, paramsMap, orderMap,
				customSelectItems, customFromItems, customQuery, null, null);
		Map<String, List<Object[]>> map = null;// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
		List<Object[]> workflowList = new ArrayList<Object[]>();
		if (list != null && !list.isEmpty()) {
			for (Object object : list) {
				if (map == null) {
					map = new HashMap<String, List<Object[]>>();
				}
				Object[] objs = (Object[]) object;
				if (!map.containsKey(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
					workflowSet.add(objs);
					map.put(objs[0].toString() + "$" + objs[1].toString(),
							workflowSet);
				} else {
					map.get(objs[0].toString() + "$" + objs[1].toString()).add(
							objs);
				}
			}
			List<String> checkList = null;// 处理重复的记录
			for (Object object : list) {
				if (checkList == null) {
					checkList = new LinkedList<String>();
				}
				Object[] objs = (Object[]) object;
				if (!checkList.contains(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List l = map.get(objs[0].toString() + "$"
							+ objs[1].toString());
					if (l != null) {
						workflowList.add(new Object[] { objs[0], objs[1],
								objs[2], objs[3], l.size() });
						checkList.add(objs[0].toString() + "$"
								+ objs[1].toString());
					}
				}
			}
			checkList = null;
			map = null;
			list = null;
		}
		return workflowList;
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
	 * @return List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 * @modify yanjian 2011-08-08 19:00 return List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量,来文时间,办文时间]>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getProcessedWorkflowForDesktop(String processStatus) {
		Object[] toSelectItems = { "processName", "processTypeId",
				"processTypeName", "processMainFormId",
				"processMainFormBusinessId", "taskId", "taskStartDate",
				"startUserName", "processName", "processInstanceId",
				"businessName", "processMainFormBusinessId",
				"processStartDate", "taskEndDate", "processEndDate" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "3");// 取已办结任务
		paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("taskStartDate", "1");
		List list = workflow.getTaskInfosByConditionForList(sItems, paramsMap,
				orderMap, null, null, null, null);
		List dlist = new ArrayList();// 存储非重复的任务
		if (list != null && !list.isEmpty()) {
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				// modify yanjian 2011-11-12-15 15:04
				String pfName = objs[0].toString();
				List<TwfBaseProcessfile> twfBaseProcessfileList = workflow
						.getDataByHql(
								"from TwfBaseProcessfile t where t.pfName = ?",
								new Object[] { pfName });
				TwfBaseProcessfile processFile = twfBaseProcessfileList.get(0);
				if (processFile.getRest2() != null
						&& !"".equals(processFile.getRest2())) {// 如果存在别名，显示别名
					objs[0] = processFile.getRest2();
				}
				objs = ObjectUtils.addObjectToArray(objs, "");
				String processInstanceId = objs[9].toString();
				// --start
				// 办结中的流程，排除主流程未结束的同步子流程子流程任务
				if (processStatus.equals("1")) {
					ContextInstance cxt = workflow
							.getContextInstance(processInstanceId);
					if (cxt != null) {
						String isSubProcess = (String) cxt
								.getVariable("com.strongit.isSubProcess");// 是否是子流程
						if (isSubProcess != null && "1".equals(isSubProcess)) {// 是子流程
							String subType = (String) cxt
									.getVariable("com.strongit.subType");// 是否是同步
							if (subType != null && "1".equals(subType)) {// 是同步子流程
								List<Object[]> parentInstances = workflow
										.getMonitorParentInstanceIds(new Long(
												processInstanceId));
								if (parentInstances != null
										&& !parentInstances.isEmpty()) {
									Object[] parentInstance = parentInstances
											.get(parentInstances.size() - 1);

									List<String> itemsList2 = new LinkedList<String>();
									itemsList2.add("processEndDate");
									itemsList2.add("processInstanceId");
									Map<String, Object> paramsMap2 = new HashMap<String, Object>();
									paramsMap2.put("processInstanceId",
											parentInstance[0]);// 取已办结任务
									List<Object[]> plist = workflow
											.getProcessInstanceByConditionForList(
													itemsList2, paramsMap2,
													null, "", "", "", null);
									Object[] p = plist.get(0);
									if (p[0] == null) {
										continue;
									}
								}
							}
						}
					}
				} else {
					if (objs[14] != null) {
						continue;
					}
				}
				// --end
				String businessId = (String) objs[4];
				if (businessId == null) {// 子流程数据
					String[] bussinessId = this.getFormIdAndBussinessIdByTaskId(objs[5].toString());
					businessId = bussinessId[0];
					objs[4] = businessId;
				}
				if (!checkList.contains(businessId)) {
					List<Object[]> ss = workflow
							.getMonitorChildrenInstanceIds(new Long(
									processInstanceId));
					if (!ss.isEmpty()) {// 存在子流程
						StringBuilder userNames = new StringBuilder();
						StringBuilder strUserName = new StringBuilder();// 人员姓名
						for (Object[] s : ss) {
							Object[] ds = workflow.getProcessStatusByPiId(s[0]
									.toString());
							Collection col2 = (Collection) ds[6];// 处理任务信息
							if (col2 != null && !col2.isEmpty()) {

								for (Iterator it = col2.iterator(); it
										.hasNext();) {
									Object[] itObjs = (Object[]) it.next();
									String userId = (String) itObjs[3];
									if (userId != null && !"".equals(userId)) {
										String[] userIds = userId.split(",");
										for (String id : userIds) {
											userNames
													.append(
															userService.getUserNameByUserId(id))
													.append(
															"("
																+ userService.getUserDepartmentByUserId(id).getOrgName()
																+ ")")
													.append(",");
										}
									}
								}
							}
						}
						if (userNames.length() > 0) {
							userNames.deleteCharAt(userNames.length() - 1);
							strUserName.append("").append(userNames);
						}
						objs[14] = strUserName;
					} else {
						Object[] returnObjs = workflow
								.getProcessStatusByPiId(processInstanceId);// 得到此流程实例下的运行情况
						Collection col = (Collection) returnObjs[6];// 处理任务信息
						if (col != null && !col.isEmpty()) {
							StringBuilder strUserName = new StringBuilder();// 人员姓名
							for (Iterator it = col.iterator(); it.hasNext();) {
								Object[] itObjs = (Object[]) it.next();
								String userId = (String) itObjs[3];
								StringBuilder userNames = new StringBuilder();
								if (userId != null && !"".equals(userId)) {
									String[] userIds = userId.split(",");
									for (String id : userIds) {
										userNames
												.append(
														userService
																.getUserNameByUserId(id))
												.append(
														"("
																+ userService
																		.getUserDepartmentByUserId(
																				id)
																		.getOrgName()
																+ ")").append(
														",");
									}
									userNames
											.deleteCharAt(userNames.length() - 1);
								}
								if (userNames.length() > 0) {
									strUserName.append("").append(userNames);
								}
							}
							// objs[14] = objs[14].toString()+strUserName;
							objs[14] = strUserName;
						}
					}
					//
					objs = ObjectUtils.addObjectToArray(objs, "");
					objs = ObjectUtils.addObjectToArray(objs, "");
					objs[objs.length - 2] = pfName;
					objs[objs.length - 1] = docService
							.getDocTimeOutDate(objs[5].toString());
					dlist.add(objs);
					checkList.add(businessId);
				}
			}
			/*
			 * for(int i=0;i<dlist.size();i++) { Object[] objs =
			 * (Object[])dlist.get(i);
			 * if(!map.containsKey(objs[0].toString()+"$"+objs[1].toString())) {
			 * List<Object[]> workflowSet = new LinkedList<Object[]>();//统计流程数量
			 * workflowSet.add(objs);
			 * map.put(objs[0].toString()+"$"+objs[1].toString(), workflowSet); }
			 * else {
			 * map.get(objs[0].toString()+"$"+objs[1].toString()).add(objs); } }
			 */
			checkList.clear();
			/*
			 * for(int j=0;j<dlist.size();j++) { Object[] objs =
			 * (Object[])dlist.get(j);
			 * if(!checkList.contains(objs[0].toString()+"$"+objs[1].toString())) {
			 * List l = map.get(objs[0].toString()+"$"+objs[1].toString()); if(l !=
			 * null) { workflowList.add(new
			 * Object[]{objs[0],objs[1],objs[2],objs[3],objs[4],objs[5],objs[6],objs[7],objs[8],objs[9],objs[10],objs[11]});
			 * checkList.add(objs[0].toString()+"$"+objs[1].toString()); } } }
			 */
		}
		return dlist;
	}

	/**
	 * 得到委派|指派的流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-21 下午06:11:00
	 * @param workflowType
	 *            流程类别
	 * @param assignType
	 *            委托类型(“0”：委托；“1”：指派；“2”：全部)
	 * @return List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getEntrustWorkflow(EntrustWorkflowParameter parameter) {
	    	return adapterworkflowmanager.getEntrustWorkflow(parameter);
	}

	/**
	 * 获取指定流程实例所有待办的任务
	 * 
	 * @description
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Apr 27, 2012 4:04:21 PM
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getTodoTaskIdsByPid(String instanceId) {
        	List<Object[]> list = null;
        	Object[] sItems = { "taskId","taskNodeId" };
        	List toSelectItems = Arrays.asList(sItems);
        	Map<String, Object> paramsMap = new HashMap<String, Object>();
        	paramsMap.put("taskType", "2");// 取非办结任务
        	paramsMap.put("processSuspend", "0");// 取非挂起任务
        	//paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
        	paramsMap.put("processInstanceId", instanceId);
        	Map orderMap = new LinkedHashMap<Object, Object>();
        	orderMap.put("taskStartDate", "1");
        	orderMap.put("taskId", "1");
        	StringBuilder customQuery = new StringBuilder();
        	StringBuilder customFromItems = new StringBuilder();
        	StringBuilder customSelectItems = new StringBuilder();
        	list = workflow.getTaskInfosByConditionForList(toSelectItems,
        		paramsMap, orderMap, customSelectItems.toString(),
        		customFromItems.toString(), customQuery.toString(), null, null);
        	if(list != null && !list.isEmpty()){
        	    list = new ArrayList<Object[]>(list);
        	}
        	return list;
        }
	
	/**
	 * 得到待办流程列表
	 * 
	 * @author yanjian
	 * @param parameter
	 * @return	List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 17, 2012 4:09:14 PM
	 * 
	 * 
	 */
	public List<Object[]> getTodoWorkflow(ProcessedParameter parameter)
			throws ServiceException, DAOException, SystemException {
		try {
			String workflowType = parameter.getWorkflowType();	//流程类型
			String type = parameter.getType();		//type:是否是代签收公文 notsign;sign
			String excludeWorkflowType = parameter.getExcludeWorkflowType();		//
			String workflowName = parameter.getWorkflowName();	//流程名称
			List<Object[]> list = null;
			Object[] sItems = { "processName", "processTypeId",
					"processTypeName", "processMainFormId" };
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			StringBuilder customQuery = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customSelectItems = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS jbpmbusiness");
			customQuery.append(" @businessId = jbpmbusiness.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (workflowType.startsWith("-")) {
					customQuery.append(" and  pi.TYPE_ID_ not in("
							+ workflowType.substring(1) + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ in(" + workflowType
							+ ") ");
				}

			}
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
					customQuery.append(" and  pi.TYPE_ID_ not in("+ excludeWorkflowType + ") ");
			}
			//*****为了处理按【流程别名】的查询******************************
			//workflowName中的这些【流程名称】(之间用逗号分隔)拥有同一个【流程别名】
			if (workflowName != null && workflowName.indexOf(",")>-1){
				StringBuffer wkName = new StringBuffer();
				String[] workflowNames = workflowName.split(",");
				for(int n=0; n<workflowNames.length; n++){
					wkName.append(",'").append(workflowNames[n]).append("'");
				}
				customQuery.append(" and pi.NAME_ in(" + wkName.substring(1).toString() + ") ");
			}
			//**********************************************************
			this.initTodoSign(type, customSelectItems, customFromItems,
					customQuery);
			type = null;
			list = workflow.getTaskInfosByConditionForList(toSelectItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			this.destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
							customSelectItems, customFromItems, customQuery,
							null, null);
			Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
			List<Object[]> workflowList = new ArrayList<Object[]>();
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					if (!map.containsKey(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
						workflowSet.add(objs);
						map.put(objs[0].toString() + "$" + objs[1].toString(),
								workflowSet);
					} else {
						map.get(objs[0].toString() + "$" + objs[1].toString())
								.add(objs);
					}
				}
				List<String> checkList = null;// 处理重复的记录
				for (int j = 0; j < list.size(); j++) {
					if (checkList == null) {
						checkList = new LinkedList<String>();
					}
					Object[] objs = (Object[]) list.get(j);
					Object[] newObjs = new Object[5];
					if (!checkList.contains(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List l = map.get(objs[0].toString() + "$"
								+ objs[1].toString());
						if (l != null) {
							newObjs[0] = objs[0];
							newObjs[1] = objs[1];
							newObjs[2] = objs[2];
							newObjs[3] = objs[3];
							newObjs[4] = l.size();
							workflowList.add(newObjs);
							checkList.add(objs[0].toString() + "$"
									+ objs[1].toString());
						}
					}
				}
				list = null;
				checkList = null;
				map = null;
			}
			return workflowList;
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
	 * 得到我的退文流程列表
	 * 
	 * @author yanjian
	 * @param parameter
	 * @return	List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 17, 2012 4:09:14 PM
	 * 
	 * 
	 */
	public List<Object[]> getMyReturnWorkflow(ProcessedParameter parameter)
			throws ServiceException, DAOException, SystemException {
		try {
			String workflowType = parameter.getWorkflowType();	//流程类型
			String type = parameter.getType();		//type:是否是代签收公文 notsign;sign
			String excludeWorkflowType = parameter.getExcludeWorkflowType();		//
			String workflowName = parameter.getWorkflowName();	//流程名称
			List<Object[]> list = null;
			Object[] sItems = { "processName", "processTypeId",
					"processTypeName", "processMainFormId" };
			List toSelectItems = Arrays.asList(sItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("isBackspace", "1");
			paramsMap.put("startUserId", userService.getCurrentUser().getUserId());// 当前用户办理任务
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			StringBuilder customQuery = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customSelectItems = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			if (workflowType != null && !"".equals(workflowType)
					&& !"null".equals(workflowType)) {
				if (workflowType.startsWith("-")) {
					customQuery.append(" and  pi.TYPE_ID_ not in("
							+ workflowType.substring(1) + ") ");
				} else {
					customQuery.append(" and pi.TYPE_ID_ in(" + workflowType
							+ ") ");
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
			if (customQuery.length() == 0) {
				customQuery.append("  pi.ISSUSPENDED_ = 0 ");
			} else {
				customQuery.append(" and pi.ISSUSPENDED_ = 0 ");
			}
			if (excludeWorkflowType != null && !"".equals(excludeWorkflowType)
					&& !"null".equals(excludeWorkflowType)) {
					customQuery.append(" and  pi.TYPE_ID_ not in("+ excludeWorkflowType + ") ");
			}
			//*****为了处理按【流程别名】的查询******************************
			//workflowName中的这些【流程名称】(之间用逗号分隔)拥有同一个【流程别名】
			if (workflowName != null && workflowName.indexOf(",")>-1){
				StringBuffer wkName = new StringBuffer();
				String[] workflowNames = workflowName.split(",");
				for(int n=0; n<workflowNames.length; n++){
					wkName.append(",'").append(workflowNames[n]).append("'");
				}
				customQuery.append(" and pi.NAME_ in(" + wkName.substring(1).toString() + ") ");
			}
			//**********************************************************
			this.initTodoSign(type, customSelectItems, customFromItems,
					customQuery);
			type = null;
			list = workflow.getTaskInfosByConditionForList(toSelectItems,
					paramsMap, orderMap, customSelectItems.toString(),
					customFromItems.toString(), customQuery.toString(), null,
					null);
			this.destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
							customSelectItems, customFromItems, customQuery,
							null, null);
			Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
			List<Object[]> workflowList = new ArrayList<Object[]>();
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					if (!map.containsKey(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
						workflowSet.add(objs);
						map.put(objs[0].toString() + "$" + objs[1].toString(),
								workflowSet);
					} else {
						map.get(objs[0].toString() + "$" + objs[1].toString())
								.add(objs);
					}
				}
				List<String> checkList = null;// 处理重复的记录
				for (int j = 0; j < list.size(); j++) {
					if (checkList == null) {
						checkList = new LinkedList<String>();
					}
					Object[] objs = (Object[]) list.get(j);
					Object[] newObjs = new Object[5];
					if (!checkList.contains(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List l = map.get(objs[0].toString() + "$"
								+ objs[1].toString());
						if (l != null) {
							newObjs[0] = objs[0];
							newObjs[1] = objs[1];
							newObjs[2] = objs[2];
							newObjs[3] = objs[3];
							newObjs[4] = l.size();
							workflowList.add(newObjs);
							checkList.add(objs[0].toString() + "$"
									+ objs[1].toString());
						}
					}
				}
				list = null;
				checkList = null;
				map = null;
			}
			return workflowList;
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
         * 得到待办流程列表
         * 
         * @author:邓志城
         * @date:2010-11-15 上午11:54:19
         * @param workflowType
         *                流程类型
         * 
         * type:是否是代签收公文 notsign;sign
         * @return List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
         */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Deprecated
	public List<Object[]> getTodoWorkflow(String workflowType, String type) {
		List<Object[]> list = null;
		Object[] sItems = { "processName", "processTypeId", "processTypeName",
				"processMainFormId" };
		List toSelectItems = Arrays.asList(sItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "2");// 取非办结任务
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("taskStartDate", "1");
		StringBuilder customQuery = new StringBuilder();
		StringBuilder customFromItems = new StringBuilder();
		StringBuilder customSelectItems = new StringBuilder();
		customFromItems.append("T_JBPM_BUSINESS jbpmbusiness");
		customQuery.append(" @businessId = jbpmbusiness.BUSINESS_ID ");
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			if(workflowType.startsWith("-")){
				System.out.println(workflowType.substring(1));
				customQuery.append(" and  pi.TYPE_ID_ not in(" + workflowType.substring(1)+ ") ");
			}else{
				customQuery.append(" and pi.TYPE_ID_ in(" + workflowType + ") ");
			}
			
		}
		this.initTodoSign(type, customSelectItems, customFromItems,
						customQuery);
		type = null;
		list = workflow.getTaskInfosByConditionForList(toSelectItems,
				paramsMap, orderMap, customSelectItems.toString(),
				customFromItems.toString(), customQuery.toString(), null, null);
		this.destroyTaskInfosData(toSelectItems, paramsMap, orderMap,
				customSelectItems, customFromItems, customQuery, null, null);
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
		List<Object[]> workflowList = new ArrayList<Object[]>();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				if (!map.containsKey(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
					workflowSet.add(objs);
					map.put(objs[0].toString() + "$" + objs[1].toString(),
							workflowSet);
				} else {
					map.get(objs[0].toString() + "$" + objs[1].toString()).add(
							objs);
				}
			}
			List<String> checkList = null;// 处理重复的记录
			for (int j = 0; j < list.size(); j++) {
				if (checkList == null) {
					checkList = new LinkedList<String>();
				}
				Object[] objs = (Object[]) list.get(j);
				Object[] newObjs = new Object[5];
				if (!checkList.contains(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List l = map.get(objs[0].toString() + "$"
							+ objs[1].toString());
					if (l != null) {
						newObjs[0] = objs[0];
						newObjs[1] = objs[1];
						newObjs[2] = objs[2];
						newObjs[3] = objs[3];
						newObjs[4] = l.size();
						workflowList.add(newObjs);
						checkList.add(objs[0].toString() + "$"
								+ objs[1].toString());
					}
				}
			}
			list = null;
			checkList = null;
			map = null;
		}
		return workflowList;
	}

	/**
	 * 用于初始化查询待签收或待办事宜的参数
	 * 
	 * @author 严建
	 * @param type
	 * @param customSelectItems
	 * @param customFromItems
	 * @param customQuery
	 * @createTime Feb 15, 2012 5:08:25 PM
	 */
	public void initTodoSign(String type, StringBuilder customSelectItems,
			StringBuilder customFromItems, StringBuilder customQuery) {
		String orgId = null;
		String orgName = null;
		if(type != null){
			String[] temps = type.split(",");
			if(temps.length>1){
				type  = temps[0];
				orgId = temps[1];
				orgName = temps[2];
			}
		}
		if(orgId == null){
			orgId = userService.getCurrentUser().getOrgId();
			orgName = userService.getOrgInfoByOrgId(orgId).getOrgName();
		}
		if (customQuery.length() == 0) {
			customQuery.append(" pi.business_id is not null ");
		} else {
			customQuery.append("and pi.business_id is not null ");
		}
		if ("notsign".equals(type)) {// 查询未签收的任务
//			customQuery.append(" and ti.NAME_ like '%签收' ");
			customQuery.append("and ti.id_ in (select distinct ti.id_" +
					" from JBPM_PROCESSINSTANCE pi, jbpm_taskinstance ti" +
					" join jbpm_taskactorpool ta on (ti.id_ = ta.taskinstance_)" +
					"  join jbpm_pooledactor pa on (ta.pooledactor_ = pa.id_), T_JBPM_BUSINESS" +
					" JBPMBUSINESS where 1 = 1" +
					" and ti.ISOPEN_ = 1" +
					" and pa.ACTORID_ in (select u.user_id from t_uums_base_user u,t_uums_base_org o where u.org_id=o.org_id and o.org_id='"+orgId+"')" +
					" and ti.PROCINST_ = pi.ID_" +
					" and pi.BUSINESS_ID = JBPMBUSINESS.BUSINESS_ID" +
					" and pi.ISSUSPENDED_ = 0" +
					"  and exists (select processinstance.id_" +
					"  from jbpm_processinstance processinstance" +
					"  where processinstance.type_id_ in (3, 370020, 413460)" +
					"  and processinstance.id_ = pi.ID_)" +
					"   and jbpmbusiness.BUSINESS_TYPE in ('0', '1')" +
					"  and pi.business_id is not null" +
					" and ti.NAME_ like '"+orgName+"签收') ");
//			if (customQuery.indexOf("@businessId") != -1) {
//				if (customFromItems.length() == 0) {
//					customFromItems
//							.append("T_WF_BASE_NODESETTINGPLUGIN tnplugin");
//				} else {
//					customFromItems
//							.append(",T_WF_BASE_NODESETTINGPLUGIN tnplugin");
//				}
//				customQuery
//						.append(" and tnplugin.NSP_NODEID=ti.NODE_ID_ and tnplugin.NSP_PLUGINCLOBVALUE like 1 and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion'");
//			} else {
//				if (customFromItems.length() == 0) {
//					customFromItems
//							.append("T_WF_BASE_NODESETTINGPLUGIN tnplugin");
//				} else {
//					customFromItems
//							.append(",T_WF_BASE_NODESETTINGPLUGIN tnplugin");
//				}
//				if (customQuery.length() > 0) {
//					customQuery.append(" and ");
//				}
//				customQuery
//						.append("tnplugin.NSP_NODEID=ti.NODE_ID_ and tnplugin.NSP_PLUGINCLOBVALUE like 1 and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion'");
//			}
		} else if ("sign".equals(type)) {// 查询已签收的任务
			customQuery.append(" and ti.NAME_ not like '%签收' ");
//			if (customFromItems.length() == 0) {
//				customFromItems
//						.append(" (select tnplugin.NSP_NODEID as NSP_NODEID")
//						.append(" from T_WF_BASE_NODESETTINGPLUGIN tnplugin ")
//						.append("  where ")
//
//						.append("  tnplugin.NSP_PLUGINCLOBVALUE not like 1")
//
//						.append(
//								" and tnplugin.NSP_PLUGINNAME = 'plugins_chkModifySuggestion') tab2");
//			} else {
//				customFromItems
//						.append(", (select tnplugin.NSP_NODEID as NSP_NODEID")
//						.append(" from T_WF_BASE_NODESETTINGPLUGIN tnplugin ")
//						.append("  where ")
//
//						.append("  tnplugin.NSP_PLUGINCLOBVALUE not like 1")
//
//						.append(
//								" and tnplugin.NSP_PLUGINNAME = 'plugins_chkModifySuggestion') tab2");
//			}
//
//			if (customQuery.indexOf("@businessId") != -1) {
//				customQuery.append(" and ti.node_id_ = tab2.nsp_nodeid");
//			} else {
//				if (customQuery.length() > 0) {
//					customQuery.append(" and ");
//				}
//				customQuery.append("  ti.node_id_ = tab2.nsp_nodeid");
//			}
		}
		type = null;
	}

	/**
	 * 用于初始化查询在办事宜的参数
	 * 
	 * @author 严建
	 * @param filterSign
	 * @param userId
	 * @param customSelectItems
	 * @param customFromItems
	 * @param customQuery
	 * @createTime Feb 16, 2012 12:38:14 PM
	 */
	@Deprecated
	public void initProcessedFilterSign(String filterSign, String userId,
			StringBuilder customSelectItems, StringBuilder customFromItems,
			StringBuilder customQuery, String queryWithTaskDate) {
		if (queryWithTaskDate == null) {
			queryWithTaskDate = "";
		}
		if (customQuery.length() == 0) {
			customQuery.append(" pi.business_id is not null ");
		} else {
			customQuery.append("and pi.business_id is not null ");
		}
		if ("1".equals(filterSign)) {
			if (customQuery.length() > 0) {
				customQuery.append(" and ");
			}
			if (customFromItems.length() > 0) {
				customFromItems.append(",");
			}
			customFromItems
					.append("(")
					.append(" select distinct ti.procinst_ as procinst ")
					.append(" from ")
					.append(" JBPM_TASKINSTANCE            ti ")
					.append(" ,T_WF_BASE_NODESETTINGPLUGIN tnplugin")
					.append(" where ")
					.append(" ti.ISOPEN_ = 0 ")
					.append(queryWithTaskDate)
					.append(
							" and ti.NODE_ID_=tnplugin.nsp_nodeid and ti.END_ is not null and ti.ISCANCELLED_ = 0 ")
					.append("  and ti.ACTORID_='")
					.append(userId)
					.append("'")
					.append(" and tnplugin.NSP_PLUGINCLOBVALUE not like 1 ")
					.append(
							" and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')tii");
			customQuery.append(" @processInstanceId = tii.procinst ");
			// 未签收的办结任务对应的流程实例
			/*
			 * customQuery .append( " exists(select ti.ID_ from
			 * JBPM_TASKINSTANCE ti,T_WF_BASE_NODESETTINGPLUGIN tnplugin where ")
			 * .append( " ti.ISOPEN_ = 0 and ti.NODE_ID_=tnplugin.nsp_nodeid and
			 * ti.END_ is not null and ti.ISCANCELLED_ = 0") .append(" and
			 * ti.ACTORID_='") .append(userId) .append("'") .append( " and
			 * ti.PROCINST_ = @processInstanceId and
			 * tnplugin.NSP_PLUGINCLOBVALUE not like 1") .append( " and
			 * tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')");
			 */
			// 过滤待办任务对应的流程实例
			customQuery
					.append(
							" and not exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISOPEN_ = 1 ")
					.append("and ti.ACTORID_='").append(userId).append(
							"' and ti.PROCINST_ = @processInstanceId) ");
		} else {
			// 查询出办结的任务对应的流程实例
			if (customFromItems.length() > 0) {
				customFromItems.append(",");
			}
			customFromItems
					.append("(select distinct ti.PROCINST_ from JBPM_TASKINSTANCE ti "
							+ " where ti.ISOPEN_ = 0 and ti.END_ is not null "
							+ queryWithTaskDate
							+ " and ti.ISCANCELLED_ = 0 "
							+ " and ti.ACTORID_ ='" + userId + "') tii");
			if (customQuery.length() > 0) {
				customQuery.append(" and  ");
			}
			customQuery.append("  tii.procinst_ = @processInstanceId ");

			/*
			 * customQuery .append( " exists(select ti.ID_ from
			 * JBPM_TASKINSTANCE ti where ") .append( "ti.ISOPEN_ = 0 and
			 * ti.END_ is not null and ti.ISCANCELLED_ = 0 and ti.ACTORID_='")
			 * .append(userId).append("'").append( " and ti.PROCINST_ =
			 * @processInstanceId)");
			 */
		}
		filterSign = null;
		userId = null;
	}
	
	public void initHosted(ProcessedParameter processedParameter){
		if (processedParameter.getQueryWithTaskDate() == null) {
			processedParameter.setQueryWithTaskDate("");
		}
		if (processedParameter.getCustomQuery().length() == 0) {
			processedParameter.getCustomQuery().append(" pi.business_id is not null ");
		} else {
			processedParameter.getCustomQuery().append("and pi.business_id is not null ");
		}
	}
	
	/**
	 * 用于初始化查询在办事宜的参数
	 * 
	 * @author 严建
	 * @param processedParameter
	 * @createTime Mar 12, 2012 5:41:39 PM
	 */
	public void initProcessedFilterSign(ProcessedParameter processedParameter) {
		if (processedParameter.getQueryWithTaskDate() == null) {
			processedParameter.setQueryWithTaskDate("");
		}
		if (processedParameter.getCustomQuery().length() == 0) {
			processedParameter.getCustomQuery().append(" pi.business_id is not null ");
		} else {
			processedParameter.getCustomQuery().append("and pi.business_id is not null ");
		}
		StringBuilder actorid_SQL = new StringBuilder();
		if(processedParameter.getUserIds() != null){
			List<String> userIds = processedParameter.getUserIds() ; 
			actorid_SQL.append(" ti.ACTORID_ in (");
			for (String userId : userIds) {
				actorid_SQL.append("'").append(userId).append("',");
			}
			actorid_SQL.deleteCharAt(actorid_SQL.length()-1);
			actorid_SQL.append(") ");
		}else{
			actorid_SQL.append(" ti.ACTORID_='").append(processedParameter.getUserId()).append("' ");
		}
		if ("1".equals(processedParameter.getFilterSign())) {
//			if("1".equals(processedParameter.getFilterYJZX())){
//				
//				StringBuilder notsignSql = new StringBuilder().append(
//						" and exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISCANCELLED_ = 0 ")
//				.append("and ").append(actorid_SQL).append(
//						" and ti.PROCINST_ = pi.ID_) ");
//				StringBuilder yjzxSql = new StringBuilder(
//				" and jbpmbusiness.BUSINESS_TYPE=0");
//				processedParameter.getCustomQuery().append(yjzxSql).append(notsignSql);
//			}else{
				if (processedParameter.getCustomQuery().length() > 0) {
					processedParameter.getCustomQuery().append(" and ");
				}
				if (processedParameter.getCustomFromItems().length() > 0) {
					processedParameter.getCustomFromItems().append(",");
				}
				processedParameter.getCustomFromItems()
				.append("(")
				.append(" select distinct ti.procinst_ as procinst ")
				.append(" from ")
				.append(" JBPM_TASKINSTANCE            ti ")
				.append(" ,T_WF_BASE_NODESETTINGPLUGIN tnplugin")
				.append(" where ")
				.append(" ti.ISOPEN_ = 0 ")
				.append(processedParameter.getQueryWithTaskDate())
				.append(
						" and ti.NODE_ID_=tnplugin.nsp_nodeid and ti.END_ is not null and ti.ISCANCELLED_ = 0 ")
						.append("  and ")
						.append(actorid_SQL)
						.append("")
						.append(" and tnplugin.NSP_PLUGINCLOBVALUE not like 1 ")
						.append(
						" and tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')tii");
				processedParameter.getCustomQuery().append(" @processInstanceId = tii.procinst ");
				// 未签收的办结任务对应的流程实例
				/*
				 * customQuery .append( " exists(select ti.ID_ from
				 * JBPM_TASKINSTANCE ti,T_WF_BASE_NODESETTINGPLUGIN tnplugin where ")
				 * .append( " ti.ISOPEN_ = 0 and ti.NODE_ID_=tnplugin.nsp_nodeid and
				 * ti.END_ is not null and ti.ISCANCELLED_ = 0") .append(" and
				 * ti.ACTORID_='") .append(userId) .append("'") .append( " and
				 * ti.PROCINST_ = @processInstanceId and
				 * tnplugin.NSP_PLUGINCLOBVALUE not like 1") .append( " and
				 * tnplugin.NSP_PLUGINNAME='plugins_chkModifySuggestion')");
				 */
				// 过滤待办任务对应的流程实例
//				processedParameter.getCustomQuery()
//				.append(
//				" and not exists(select ti.ID_ from JBPM_TASKINSTANCE ti where ti.ISOPEN_ = 1 ")
//				.append("and ti.ACTORID_='").append( processedParameter.getUserId()).append(
//				"' and ti.PROCINST_ = @processInstanceId) ");
//			}
			
//			if(yjzxCustomQuery.length())
		} else {
			// 查询出办结的任务对应的流程实例
			if (processedParameter.getCustomFromItems().length() > 0) {
				processedParameter.getCustomFromItems().append(",");
			}
			long currentTime = System.currentTimeMillis();
			processedParameter.getCustomFromItems()
			.append("(select distinct ti.PROCINST_ from JBPM_TASKINSTANCE ti "
					+ " where ti.ISOPEN_ = 0 and ti.END_ is not null "
					+ processedParameter.getQueryWithTaskDate()
					+ " and ti.ISCANCELLED_ = 0 "
					+ " and ").append(actorid_SQL).append(" and ").append(currentTime).append(" = ").append(currentTime).append(") tii");
			if (processedParameter.getCustomQuery().length() > 0) {
				processedParameter.getCustomQuery().append(" and  ");
			}
			processedParameter.getCustomQuery().append("  tii.procinst_ = @processInstanceId ");
			
			/*
			 * customQuery .append( " exists(select ti.ID_ from
			 * JBPM_TASKINSTANCE ti where ") .append( "ti.ISOPEN_ = 0 and
			 * ti.END_ is not null and ti.ISCANCELLED_ = 0 and ti.ACTORID_='")
			 * .append(userId).append("'").append( " and ti.PROCINST_ =
			 * @processInstanceId)");
			 */
		}
		//已分办 不显示重新分发中已退回的数据
		if("1".equals(processedParameter.getShowSignUserInfo())){
			StringBuilder paactorid_SQL = new StringBuilder();
			if(processedParameter.getUserIds() != null){
				List<String> userIds = processedParameter.getUserIds() ; 
				paactorid_SQL.append(" pa.ACTORID_ in (");
				for (String userId : userIds) {
					paactorid_SQL.append("'").append(userId).append("',");
				}
				paactorid_SQL.deleteCharAt(actorid_SQL.length()-1);
				paactorid_SQL.append(") ");
			}else{
				paactorid_SQL.append(" pa.ACTORID_='").append(processedParameter.getUserId()).append("' ");
			}
			if(processedParameter.getCustomQuery().length() >0 ){
				processedParameter.getCustomQuery().append(" and ");
			}
//			processedParameter.getCustomQuery().append("  not exists (select ti.procinst_ ")
			processedParameter.getCustomQuery().append("   @processInstanceId not in (select ti.procinst_ ")
					.append(" from jbpm_taskinstance ti ")
					.append(" join jbpm_taskactorpool ta on (ti.id_ = ta.taskinstance_) ")
					.append(" join jbpm_pooledactor pa on (ta.pooledactor_ = pa.id_) ")
					.append(" where ").append(paactorid_SQL)
					.append(" and ti.isbackspace_ = 1 ")
					.append(" and ti.procinst_ = @processInstanceId ")
					.append(" and ti.ISOPEN_ = 1) ");
		}
	}

	/**
	 * 清除调用getProcessInstanceByConditionForPage或getProcessInstanceByConditionForList
	 * For SQL 接口的参数
	 * 
	 * @author 严建
	 * @param toSelectItems
	 * @param paramsMap
	 * @param orderMap
	 * @param customSelectItems
	 * @param customFromItems
	 * @param customQuery
	 * @param customValues
	 * @param customOrderBy
	 * @createTime Feb 16, 2012 12:38:14 PM
	 */
	public void destroyProcessInfosData(List<String> toSelectItems,
			Map<String, Object> paramsMap, Map<String, String> orderMap,
			StringBuilder customSelectItems, StringBuilder customFromItems,
			StringBuilder customQuery, List<Object> customValues,
			StringBuilder customOrderBy) {
		if (toSelectItems != null) {
			toSelectItems = null;
		}
		if (paramsMap != null) {
			paramsMap = null;
		}
		if (orderMap != null) {
			orderMap = null;
		}
		if (customSelectItems != null) {
			customSelectItems = null;
		}
		if (customFromItems != null) {
			customFromItems = null;
		}
		if (customQuery != null) {
			customQuery = null;
		}
		if (customValues != null) {
			customValues = null;
		}
		if (customOrderBy != null) {
			customOrderBy = null;
		}
	}

	/**
	 * 清除调用getTaskInfosByConditionForList或getTaskInfosByConditionForPage For SQL
	 * 接口的参数
	 * 
	 * @author 严建
	 * @param toSelectItems
	 * @param paramsMap
	 * @param orderMap
	 * @param customSelectItems
	 * @param customFromItems
	 * @param customQuery
	 * @param customValues
	 * @param customOrderBy
	 * @createTime Feb 15, 2012 5:15:33 PM
	 */
	public void destroyTaskInfosData(List<String> toSelectItems,
			Map<String, Object> paramsMap, Map<String, String> orderMap,
			StringBuilder customSelectItems, StringBuilder customFromItems,
			StringBuilder customQuery, List<Object> customValues,
			StringBuilder customOrderBy) {
		if (toSelectItems != null) {
			toSelectItems = null;
		}
		if (paramsMap != null) {
			paramsMap = null;
		}
		if (orderMap != null) {
			orderMap = null;
		}
		if (customSelectItems != null) {
			customSelectItems = null;
		}
		if (customFromItems != null) {
			customFromItems = null;
		}
		if (customQuery != null) {
			customQuery = null;
		}
		if (customValues != null) {
			customValues = null;
		}
		if (customOrderBy != null) {
			customOrderBy = null;
		}
	}
	
	/**
	 * @method getRepealWorkflow
	 * @author 申仪玲
	 * @created 2011-12-12 下午09:17:01
	 * @description 得到公文回收站流程列表
	 * @return List<Object[]> [流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getRepealWorkflow(String workflowType,
			String processStatus) {
		
		Object[] toSelectItems = { "processName", "processTypeId",
				"processTypeName", "processMainFormId",
				"processMainFormBusinessId", "processInstanceId",
				"processEndDate" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		//paramsMap.put("taskType", "3");// 取已办结任务
		paramsMap.put("processSuspend", "1");// 取挂起任务
		paramsMap.put("startUserId", userService.getCurrentUser().getUserId());
		//paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
		if (workflowType != null && !"".equals(workflowType)) {
			paramsMap.put("processTypeId", genWorkflowTypeList(workflowType));
		}
		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processTypeId", "0");
		List list = workflow.getProcessInstanceByConditionForList(sItems, paramsMap,
				orderMap, null, null, null, null);
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
		List<Object[]> workflowList = new ArrayList<Object[]>();

		if (list != null && !list.isEmpty()) {
			List dlist = new ArrayList();// 存储非重复的任务
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);

				String processInstanceId = objs[5].toString();
				// --start
				// 办结中的流程，排除主流程未结束的同步子流程子流程任务
				if (processStatus.equals("1")) {
					ContextInstance cxt = workflow
							.getContextInstance(processInstanceId);
					if (cxt != null) {
						String isSubProcess = (String) cxt
								.getVariable("com.strongit.isSubProcess");// 是否是子流程
						if (isSubProcess != null && "1".equals(isSubProcess)) {// 是子流程
							String subType = (String) cxt
									.getVariable("com.strongit.subType");// 是否是同步
							if (subType != null && "1".equals(subType)) {// 是同步子流程
								List<Object[]> parentInstances = workflow
										.getMonitorParentInstanceIds(new Long(
												processInstanceId));
								if (parentInstances != null
										&& !parentInstances.isEmpty()) {
									Object[] parentInstance = parentInstances
											.get(parentInstances.size() - 1);

									List<String> itemsList2 = new LinkedList<String>();
									itemsList2.add("processEndDate");
									itemsList2.add("processInstanceId");
									Map<String, Object> paramsMap2 = new HashMap<String, Object>();
									paramsMap2.put("processInstanceId",
											parentInstance[0]);// 取已办结任务
									List<Object[]> plist = workflow
											.getProcessInstanceByConditionForList(
													itemsList2, paramsMap2,
													null, "", "", "", null);
									Object[] p = plist.get(0);
									if (p[0] == null) {
										continue;
									}
								}

							}
						}

					}

				} else {
					if (objs[6] != null) {
						continue;
					}
				}
				// --end

				String businessId = (String) objs[4];
				if (businessId == null) {// 子流程数据
					String[] bussinessId = this
							.getFormIdAndBussinessIdByTaskId(objs[5].toString());
					businessId = bussinessId[0];
					objs[4] = businessId;
				}
				/**
				 * 当通过一个父流程启动一个子流程实例时,业务id会相同,如果过滤的话会少统计数据,所以判断业务id相同并且实例id也相同,就过滤：bug:0000051170
				 */
				if (!checkList.contains(businessId+objs[5].toString())) {
					dlist.add(objs);
					checkList.add(businessId+objs[5].toString());
				}
			}

			for (int i = 0; i < dlist.size(); i++) {
				Object[] objs = (Object[]) dlist.get(i);
				if (!map.containsKey(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
					workflowSet.add(objs);
					map.put(objs[0].toString() + "$" + objs[1].toString(),
							workflowSet);
				} else {
					map.get(objs[0].toString() + "$" + objs[1].toString()).add(
							objs);
				}
			}
			checkList.clear();
			for (int j = 0; j < dlist.size(); j++) {
				Object[] objs = (Object[]) dlist.get(j);
				if (!checkList.contains(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List l = map.get(objs[0].toString() + "$"
							+ objs[1].toString());
					if (l != null) {
						workflowList.add(new Object[] { objs[0], objs[1],
								objs[2], objs[3], l.size() });
						checkList.add(objs[0].toString() + "$"
								+ objs[1].toString());
					}
				}
			}
		}
		return workflowList;
	}
	/**
	 * @method getRepealWorkflow
	 * @author 申仪玲
	 * @created 2011-12-12 下午09:17:01
	 * @description 我的退回文件
	 * @return List<Object[]> [流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getReturnWorkflow(String workflowType,
			String processStatus) {
		
		Object[] toSelectItems = { "processName", "processTypeId",
				"processTypeName", "processMainFormId",
				"processMainFormBusinessId", "processInstanceId",
				"processEndDate" };// 查询processMainFormBusinessId用于过滤重复的已办任务
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		//paramsMap.put("taskType", "3");// 取已办结任务
		paramsMap.put("processSuspend", "1");// 取挂起任务
		paramsMap.put("startUserId", userService.getCurrentUser().getUserId());
		//paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
		if (workflowType != null && !"".equals(workflowType)) {
			paramsMap.put("processTypeId", genWorkflowTypeList(workflowType));
		}
		if (processStatus != null && !"".equals(processStatus)
				&& !"2".equals(processStatus)) {
			paramsMap.put("processStatus", processStatus);
		}
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processTypeId", "0");
		List list = workflow.getProcessInstanceByConditionForList(sItems, paramsMap,
				orderMap, null, null, null, null);
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
		List<Object[]> workflowList = new ArrayList<Object[]>();

		if (list != null && !list.isEmpty()) {
			List dlist = new ArrayList();// 存储非重复的任务
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);

				String processInstanceId = objs[5].toString();
				// --start
				// 办结中的流程，排除主流程未结束的同步子流程子流程任务
				if (processStatus.equals("1")) {
					ContextInstance cxt = workflow
							.getContextInstance(processInstanceId);
					if (cxt != null) {
						String isSubProcess = (String) cxt
								.getVariable("com.strongit.isSubProcess");// 是否是子流程
						if (isSubProcess != null && "1".equals(isSubProcess)) {// 是子流程
							String subType = (String) cxt
									.getVariable("com.strongit.subType");// 是否是同步
							if (subType != null && "1".equals(subType)) {// 是同步子流程
								List<Object[]> parentInstances = workflow
										.getMonitorParentInstanceIds(new Long(
												processInstanceId));
								if (parentInstances != null
										&& !parentInstances.isEmpty()) {
									Object[] parentInstance = parentInstances
											.get(parentInstances.size() - 1);

									List<String> itemsList2 = new LinkedList<String>();
									itemsList2.add("processEndDate");
									itemsList2.add("processInstanceId");
									Map<String, Object> paramsMap2 = new HashMap<String, Object>();
									paramsMap2.put("processInstanceId",
											parentInstance[0]);// 取已办结任务
									List<Object[]> plist = workflow
											.getProcessInstanceByConditionForList(
													itemsList2, paramsMap2,
													null, "", "", "", null);
									Object[] p = plist.get(0);
									if (p[0] == null) {
										continue;
									}
								}

							}
						}

					}

				} else {
					if (objs[6] != null) {
						continue;
					}
				}
				// --end

				String businessId = (String) objs[4];
				if (businessId == null) {// 子流程数据
					String[] bussinessId = this
							.getFormIdAndBussinessIdByTaskId(objs[5].toString());
					businessId = bussinessId[0];
					objs[4] = businessId;
				}
				if (!checkList.contains(businessId)) {
					dlist.add(objs);
					checkList.add(businessId);
				}
			}

			for (int i = 0; i < dlist.size(); i++) {
				Object[] objs = (Object[]) dlist.get(i);
				if (!map.containsKey(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
					workflowSet.add(objs);
					map.put(objs[0].toString() + "$" + objs[1].toString(),
							workflowSet);
				} else {
					map.get(objs[0].toString() + "$" + objs[1].toString()).add(
							objs);
				}
			}
			checkList.clear();
			for (int j = 0; j < dlist.size(); j++) {
				Object[] objs = (Object[]) dlist.get(j);
				if (!checkList.contains(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List l = map.get(objs[0].toString() + "$"
							+ objs[1].toString());
					if (l != null) {
						workflowList.add(new Object[] { objs[0], objs[1],
								objs[2], objs[3], l.size() });
						checkList.add(objs[0].toString() + "$"
								+ objs[1].toString());
					}
				}
			}
		}
		return workflowList;
	}

	/**
	 * 获取并统计流程草稿
	 * 
	 * @author:邓志城
	 * @date:2010-11-9 下午02:12:11
	 * @param workflowType
	 *            流程类型id
	 * @return List<Object[流程名称,流程类型id,流程类型名称,流程启动保单id,流程草稿数量]>
	 */
	@Transactional(readOnly = true)
	public List getStartWorkflowDraft(String workflowType) {
		List<Object[]> list = new LinkedList<Object[]>();
		Map<String, String> formAndTable = new HashMap<String, String>();// 记录查询过的表单对应的主表,避免重复查询
		try {
			List workflowInfoList = workflow.getStartWorkflow(workflowType);
			String tableName = null;
			if (workflowInfoList != null && !workflowInfoList.isEmpty()) {
				String userId = userService.getCurrentUser().getUserId();
				for (int i = 0; i < workflowInfoList.size(); i++) {
					Object[] workflowInfo = (Object[]) workflowInfoList.get(i);
					String formId = workflowInfo[3].toString();
					String workflowName = workflowInfo[0].toString();
					if (formId != null && !"0".equals(formId)) {
						if (formAndTable.get(formId) != null) {
							tableName = formAndTable.get(formId);
						} else {
							tableName = eform.getTable(formId);
							formAndTable.put(formId, tableName);
						}
						if (tableName == null) {
							continue;
						}
						StringBuilder SqlQuery = new StringBuilder(
								"Select count(*) from ");
						SqlQuery.append(tableName).append(" t Where t.")
								.append(WORKFLOW_NAME).append("='").append(
										workflowName).append("' And t.")
								.append(WORKFLOW_AUTHOR).append("='").append(
										userId).append("' And t.").append(
										WORKFLOW_STATE).append("='0' ");
						int count = jdbcTemplate.queryForInt(SqlQuery
								.toString());
						logger.info(SqlQuery.toString());
						if (count > 0) {
							workflowInfo = ObjectUtils.addObjectToArray(
									workflowInfo, count);
							list.add(workflowInfo);
						}
					}
				}
			}
		} catch (WorkflowException e) {
			logger.error("工作流获取数据异常", e);
			throw e;
		} catch (SystemException e) {
			logger.error("发生系统异常", e);
			throw e;
		} catch (DataAccessException e) {
			logger.error("Jdbc查询出错", e);
			throw e;
		}
		System.out.println(formAndTable + "..清空。。。。");
		formAndTable.clear();
		return list;
	}
	/**
	 * 获取我的在办文件
	 * 
	 * @author:xush
	 * @date:1/4/2014 10:55 AM
	 * @param workflowType
	 *            流程类型id
	 * @return List<Object[流程名称,流程类型id,流程类型名称,流程启动保单id,流程数量]>
	 */
	@Transactional(readOnly = true)
	public List getMyNowFileWorkflow(ProcessedParameter parameter) {
		Object[] toSelectItems = { "processName", "processTypeId",
					"processTypeName", "processMainFormId", "processEndDate" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if (parameter.getWorkflowType() != null && !"".equals(parameter.getWorkflowType())) {
				paramsMap.put("processTypeId",
						genWorkflowTypeList(parameter.getWorkflowType()));
			}
			paramsMap.put("startUserId", userService.getCurrentUser()
					.getUserId());// 发起人是当前用户
			paramsMap.put("processSuspend", "0");
			paramsMap.put("processStatus", parameter.getProcessStatus());
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("processTypeId", "0");
			StringBuilder customSelectItems = new StringBuilder();
			StringBuilder customFromItems = new StringBuilder();
			StringBuilder customQuery = new StringBuilder();
			customFromItems.append("T_JBPM_BUSINESS JBPMBUSINESS");
			customQuery.append(" @businessId = JBPMBUSINESS.BUSINESS_ID ");
			customQuery.append(" and jbpmbusiness.BUSINESS_TYPE in (").append(Tjbpmbusiness.getShowableBusinessType()).append(") ");
			List list= workflow.getProcessInstanceByConditionForList(sItems, paramsMap, orderMap, customSelectItems.toString(), customFromItems.toString(), customQuery.toString(), null, null);
			destroyProcessInfosData(sItems, paramsMap, orderMap,
					customSelectItems, customFromItems, customQuery, null, null);

			Map<String, List<Object[]>> map = null;// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
			List<Object[]> workflowList = new ArrayList<Object[]>();
			List<Long> allTEFormTemplateId = eform.getAllTEFormTemplateId();
			if (list != null && !list.isEmpty()) {
				for (Object object : list) {
					if (map == null) {
						map = new HashMap<String, List<Object[]>>();
					}
					Object[] objs = (Object[]) object;
					String[] formidinfo = objs[3].toString().split(",");
					Long mainFromId = new Long(formidinfo[formidinfo.length-1]);
					if(allTEFormTemplateId.indexOf(mainFromId) == -1){
					    continue;
					}
					if (!map.containsKey(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
						workflowSet.add(objs);
						map.put(objs[0].toString() + "$" + objs[1].toString(),
								workflowSet);
					} else {
						map.get(objs[0].toString() + "$" + objs[1].toString()).add(
								objs);
					}
				}
				List<String> checkList = null;// 处理重复的记录
				for (Object object : list) {
					if (checkList == null) {
						checkList = new LinkedList<String>();
					}
					Object[] objs = (Object[]) object;
					if (!checkList.contains(objs[0].toString() + "$"
							+ objs[1].toString())) {
						List l = map.get(objs[0].toString() + "$"
								+ objs[1].toString());
						if (l != null) {
							workflowList.add(new Object[] { objs[0], objs[1],
									objs[2], objs[3], l.size() });
							checkList.add(objs[0].toString() + "$"
									+ objs[1].toString());
						}
					}
				}
				checkList = null;
				map = null;
				list = null;
			}
			return workflowList;
	}

	/**
	 * 根据用户名称，查找满足条件的正处于这些用户的流程定义id列表
	 * 
	 * @param userName
	 *            用户姓名
	 * @param day
	 *            任务停留天数
	 * @return 流程定义列表
	 */
	public List<Long> findDefinitionByHandler(String userName, String day) {
		if ((userName == null || "".equals(userName))
				&& (day == null || "".equals(day))) {
			return new ArrayList<Long>();
		}
		String sql = "select t.USER_ID from T_UUMS_BASE_USER t where t.USER_NAME like '%"
				+ FiltrateContent.getNewContent(userName)
				+ "%' and t.USER_ISDEL='0' and t.USER_ISACTIVE='1'";
		List list = jdbcTemplate.queryForList(sql);
		List<String> userIds = new ArrayList<String>();
		List<Long> result = new ArrayList<Long>();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				userIds.add((String) map.get("USER_ID"));
			}
		}
		List<String> toSelectTtems = new ArrayList<String>(2);
		toSelectTtems.add("processInstanceId");// 流程实例
		toSelectTtems.add("taskStartDate");// 任务开始时间
		Map<String, Object> params = new HashMap<String, Object>(3);
		if (!userIds.isEmpty()) {
			params.put("handlerId", userIds);
		} else {
			return null;
		}
		params.put("taskType", "2");
		List list2 = this.getTaskInfosByConditionForList(toSelectTtems, params,
				null, null, null, null, null);
		if (list2 != null && !list2.isEmpty()) {
			for (int j = 0; j < list2.size(); j++) {
				Object[] objs = (Object[]) list2.get(j);
				Date taskStartDate = (Date) objs[1];
				if (day != null && !"".equals(day)) {
					long dayL = DateCountUtil.getDistDates(taskStartDate,
							new Date());// 任务开始时间和当前时间的时间差
					logger.info("***************" + dayL
							+ "天*******************");
					//流程监控中，通过任务停留天数搜索有问题 0000049923
//					if (dayL > Long.parseLong(day)) {
					if (dayL >= Long.parseLong(day)) {
						result.add((Long) objs[0]);
					}
				} else {
					result.add((Long) objs[0]);
				}
			}
		}
		return result;
	}

	/**
	 * @method getProcessedBySeclected
	 * @author 申仪玲
	 * @created 2011-12-13 下午06:55:21
	 * @description 查找当前用户指定条件的流程
	 * @return List 返回类型
	 */

	@SuppressWarnings("unchecked")
	public List getProcessedBySeclected(String taskId, String instanceId) {

		List<Object[]> list = null;
		Object[] toSelectItems = { "taskId", "taskEndDate", "startUserName",
				"processMainFormBusinessId", "processName", "processTypeId",
				"taskId", "taskId", "processInstanceId", "businessName",
				"taskName", "processStartDate", "taskNodeName",
				"processEndDate" };
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskType", "3");// 取非办结任务
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		paramsMap.put("handlerId", userService.getCurrentUser().getUserId());// 当前用户办理任务
		paramsMap.put("startUserId", userService.getCurrentUser().getUserId());// 当前用户发起任务
		paramsMap.put("taskId", taskId);
		paramsMap.put("processInstanceId", instanceId);

		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("processTypeId", "0");
		list = workflow.getTaskInfosByConditionForList(sItems, paramsMap,
				orderMap, null, null, null, null);

		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();// Map<流程名称,List<流程名称,流程类型id,流程类型名称,主表单id>>
		List<Object[]> workflowList = new ArrayList<Object[]>();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				if (!map.containsKey(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List<Object[]> workflowSet = new LinkedList<Object[]>();// 统计流程数量
					workflowSet.add(objs);
					map.put(objs[0].toString() + "$" + objs[1].toString(),
							workflowSet);
				} else {
					map.get(objs[0].toString() + "$" + objs[1].toString()).add(
							objs);
				}
			}
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			for (int j = 0; j < list.size(); j++) {
				Object[] objs = (Object[]) list.get(j);
				Object[] newObjs = new Object[5];
				if (!checkList.contains(objs[0].toString() + "$"
						+ objs[1].toString())) {
					List l = map.get(objs[0].toString() + "$"
							+ objs[1].toString());
					if (l != null) {
						// objs = ObjectUtils.addObjectToArray(objs, l.size());
						// workflowList.add(objs);
						newObjs[0] = objs[0];
						newObjs[1] = objs[1];
						newObjs[2] = objs[2];
						newObjs[3] = objs[3];
						newObjs[4] = l.size();
						workflowList.add(newObjs);
						checkList.add(objs[0].toString() + "$"
								+ objs[1].toString());
					}
				}
			}
		}
		return workflowList;
	}

	/**
	 * 数据库中是否存在该表
	 * 
	 * @author 严建
	 * @param tableName
	 *            表名称（参数tableName不能为空或空字符串）
	 * @param columName
	 *            列名称（参数columName不能为空或空字符串）
	 * @return
	 * @createTime Feb 4, 2012 3:50:44 PM
	 */
	public boolean existColumInTable(String tableName, String columName) {

		if (columName == null || columName.equals("")) {
			throw new DAOException("参数columName不能为空或空字符串");
		} else {
			columName = columName.toUpperCase();
		}
		List<String> columns = getAllColumNameInTable(tableName);
		return columns.contains(columName);
	}

	/**
	 * 
	 * 传入一组列名，判断指定表中是否存在相应的列
	 * 
	 * @author 严建
	 * @param tableName
	 *            表名称
	 * @param columNames
	 *            List<String> 一组列名称
	 * @return
	 * @createTime Feb 5, 2012 4:23:00 PM
	 */
	public Map<String, Boolean> existColumsInTable(String tableName,
			List<String> columNames) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		List<String> columns = getAllColumNameInTable(tableName);
		for (int i = 0; i < columNames.size(); i++) {
			String columName = columNames.get(i).toUpperCase();
			map.put(columName, columns.contains(columName));
		}
		return map;
	}

	/**
	 * 
	 * 返回表中所有的字段
	 * 
	 * @author 严建
	 * @param tableName
	 *            表名
	 * @return
	 * @createTime Feb 5, 2012 4:15:55 PM
	 */
	private List<String> getAllColumNameInTable(String tableName) {
		if (tableName == null || tableName.equals("")) {
			throw new DAOException("参数tableName不能为空或空字符串");
		} else {
			tableName = tableName.toUpperCase();
		}
		Connection con = getConnection();
		try {
			String sql = "select * from " + tableName + " where 1=0";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			final List<String> columns = new LinkedList<String>();// List<字段名称>
			for (int i = 1; i <= count; i++) {
				if (!columns.contains(rsmd.getColumnName(i))) {
					columns.add(rsmd.getColumnName(i));
				}
			}
			return columns;
		} catch (SQLException ex) {
			logger.error("获取列信息时发生异常", ex);
			throw new SystemException(ex);
		}
	}

	/**
	 * 
	 * 处理工作流参数workflowType（对应工作流标准参数processTypeId）
	 * 
	 * @author 严建
	 * @param workflowType
	 * @return
	 * @createTime Feb 9, 2012 9:30:42 AM
	 */
	public List<String> genWorkflowTypeList(String workflowType) {
		if (workflowType == null || "".equals(workflowType)) {
			throw new ServiceException("参数workflowType不能空或者空字符串");
		}
		String[] workflowTypes = workflowType.split(",");
		List<String> lType = new ArrayList<String>();
		if (workflowType.startsWith("-")) {// 不想显示的流程类型
			List<String> FulType = new ArrayList<String>();
			for (String tp : workflowTypes) {
				FulType.add(tp.substring(1));
			}

			List<Object[]> allProcessTypeObjs = workflow
					.getAllProcessTypeList();
			for (Object[] ProcessTypeObj : allProcessTypeObjs) {
				String tp = ProcessTypeObj[0].toString();
				if (!FulType.contains(tp)) {
					lType.add(tp);
				}
			}

		} else {
			for (String tp : workflowTypes) {
				lType.add(tp);
			}
		}
		return lType;
	}

}
