package com.strongit.oa.webservice.iphone.server.iphoneWork;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.axis.MessageContext;
import org.apache.commons.beanutils.ConvertUtils;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import sun.misc.BASE64Encoder;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.ITransitionService;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.common.workflow.plugin.util.TransitionPluginConst;
import com.strongit.oa.common.workflow.plugin.util.po.OATransitionPlugin;
import com.strongit.oa.common.workflow.service.transitionservice.pojo.TransitionsInfoBean;
import com.strongit.oa.ipadoa.util.WorkflowForIpadService;
import com.strongit.oa.mylog.MyLogManager;

import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.Office2PDF;
import com.strongit.oa.webservice.util.WebServiceAddressUtil;
import com.strongit.oa.work.WorkManager;
import com.strongit.uums.bo.TUumsBasePost;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * 供外部调用的webservice接口
 * 
 * @author Administrator
 * 
 */
@Service
public class iphoneWorkWebService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private WorkManager manager;// 工作处理manager

	private static String STATUS_SUC = "1";// 返回成功状态L
	private static String STATUS_FAIL = "0";// 返回失败状态
	private String workflowName;// 流程名称

	protected List ideaLst;// 保存在字典中的意见

	IWorkflowService workflowService;

	WorkflowForIpadService workflowForIpadService;

	IProcessDefinitionService processDefinitionService;

	SendDocManager baseManager;

	IWorkflowAttachService attachService;

	IUserService userService;

	IAttachmentService attachmentManager;

	// SendDocUploadManager sendDocUploadManager; //获取上一步处理人接口

	IApprovalSuggestionService approvalSuggestionService; // 审批意见接口

	BaseWorkflowManager baseWorkflowManager;

	ITransitionService transitionService;

	IWorkflowConstService workflowConstService;// 工作流服务类

	INodesettingPluginService nodesettingPluginService;

	AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	
	MyLogManager myLogManager;
	
	
	JdbcTemplate jdbcTemplate;

	/**
	 * 构造方法获取manager对象
	 */
	public iphoneWorkWebService() {
		workflowName = "IPPOA";
		manager = (WorkManager) ServiceLocator.getService("workManager");
		workflowForIpadService = (WorkflowForIpadService) ServiceLocator
				.getService("workflowForIpadService");
		approvalSuggestionService = (IApprovalSuggestionService) ServiceLocator
				.getService("approvalSuggestionService");
		attachmentManager = (IAttachmentService) ServiceLocator
				.getService("attachmentManager");

		workflowService = (IWorkflowService) ServiceLocator
				.getService("workflowService");
		baseManager = (SendDocManager) ServiceLocator
				.getService("sendDocManager");
		attachService = (IWorkflowAttachService) ServiceLocator
				.getService("workflowAttachManager");
		userService = (IUserService) ServiceLocator.getService("userService");

		// sendDocUploadManager =
		// (SendDocUploadManager)ServiceLocator.getService("sendDocUploadManager");
		baseWorkflowManager = (BaseWorkflowManager) ServiceLocator
				.getService("baseWorkflowManager");
		processDefinitionService = (IProcessDefinitionService) ServiceLocator
				.getService("processDefinitionService");

		transitionService = (ITransitionService) ServiceLocator
				.getService("transitionService");
		workflowConstService = (IWorkflowConstService) ServiceLocator
				.getService("workflowConstService");

		nodesettingPluginService = (INodesettingPluginService) ServiceLocator
				.getService("nodesettingPluginService");

		adapterBaseWorkflowManager = (AdapterBaseWorkflowManager) ServiceLocator
				.getService("adapterBaseWorkflowManager");
		myLogManager = (MyLogManager)ServiceLocator.getService("myLogManager");
		
		jdbcTemplate = (JdbcTemplate)ServiceLocator.getService("jdbcTemplate");
		logger.info("工作流服务类初始化完毕.。。。");
	}

	private Date parseDate(String dateStr) throws ParseException,
			SystemException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 提供获取其他类型待办事宜接口,支持分页、查询(待办事宜模块).
	 * 
	 * @param userId
	 *            用户id
	 * @param userName
	 *            拟稿人姓名,支持模糊查询
	 * @param businessName
	 *            业务数据标题,支持模糊查询
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示待办事宜数量
	 * @param startDate
	 *            任务接收日期开始范围
	 * @param endDate
	 *            任务接收日期结束范围
	 * @return XML格式字符的待办事宜数据  服务调用成功时返回数据格式如下： <?xml version="1.0"
	 *         encoding="UTF-8"?> <service-response> <status>1</status>
	 *         <fail-reason /> <data totalCount="总记录数" totalPages="总页数"> <row>
	 *         <item type="string" value="任务id" /> <item type="date"
	 *         value="任务接收时间" /> <item type="string" value="业务数据标题" /> <item
	 *         type="string" value="拟稿人" /> <item type="string" value="公文类别名称" />
	 *         <item type="string" value="上一步处理人" /> </row> </data>
	 *         </service-response>  服务调用失败时返回数据格式如下： <?xml version="1.0"
	 *         encoding="UTF-8"?> <service-response> <status>0</status>
	 *         <fail-reason>异常描述</fail-reason> <data /> </service-response>
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String getShoufawenTodo(String userId, String userName,
			String businessName, String pageNo, String pageSize,
			String startDate, String endDate) throws DAOException,
			ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			List<String> items = new ArrayList<String>();
			items.add("taskId"); // 任务id
			items.add("taskStartDate"); // 任务开始时间
			items.add("businessName"); // 业务数据标题
			items.add("startUserName"); // 拟稿人
			items.add("processTypeName"); // 公文类别名称
			items.add("processInstanceId");//流程实例id
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}

			List<String> type = new ArrayList<String>(2);// 流程类型，查询除收发文的其他流程类型
			// 获取流程类型
			List<Object[]> typeList = processDefinitionService
					.getAllProcessTypeList();
			if (typeList != null && !typeList.isEmpty()) {
				for (Object[] processType : typeList) {
					// 过滤掉收发文流程类型
					if (!"2".equals(String.valueOf(processType[0]))
							&& !"3".equals(String.valueOf(processType[0]))) {
						type.add(String.valueOf(processType[0]));
					}
				}
			}
			// 以下为查询条件
			paramsMap.put("handlerId", userId); // 处理人
			paramsMap.put("taskType", "2"); // 待办
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("processTypeId", type);
			if (userName != null && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			if (startDate != null && !"".equals(startDate)) {
				Date sd = this.parseDate(startDate);
				paramsMap.put("taskStartDateStart", sd);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date ed = this.parseDate(endDate);
				paramsMap.put("taskStartDateEnd", ed);
			}
			// --------- ------ //
			Map orderMap = new HashMap();
			orderMap.put("taskStartDate", "1");
			if (pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if (pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));
			Page<Object[]> pageWorkflow = workflowService
					.getTaskInfosByConditionForPage(page, items, paramsMap,
							orderMap, null, null, null, null);
			List<String[]> result = new ArrayList<String[]>();
			List<Object[]> listWorkflow = pageWorkflow.getResult();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (Object[] objs : listWorkflow) {
					String[] taskId = new String[2];
					taskId[0] = "string";
					taskId[1] = objs[0] == null ? "" : objs[0].toString();

					List<Long> list = new ArrayList<Long>();
					list.add(Long.parseLong(objs[0].toString()));
					Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = null;
					TaskIdMapPreTaskBeanTemp = workflowForIpadService
							.getTaskIdMapPreTaskBeanTemper(list, userId);

					// 得到上一步处理人
					String preTaskActor = null;
					preTaskActor = TaskIdMapPreTaskBeanTemp.get(
							objs[0].toString()).getTaskActorName();
					String[] preActor = new String[2];
					preActor[0] = "string";
					preActor[1] = preTaskActor;

					String[] taskStartDate = new String[2];
					taskStartDate[0] = "date";
					taskStartDate[1] = objs[1] == null ? "" : objs[1]
							.toString();

					String[] businessNameArray = new String[2];
					businessNameArray[0] = "string";
					businessNameArray[1] = objs[2] == null ? "" : objs[2]
							.toString();

					String[] startUserNameArray = new String[2];
					startUserNameArray[0] = "string";
					startUserNameArray[1] = objs[3] == null ? "" : objs[3]
							.toString();

					String[] id = new String[2];
					String processTypeName = "";
					id[0] = "string";
					if (objs[4] != null) {
						processTypeName = objs[4].toString();
						if (processTypeName.length() > 4) {
							processTypeName = processTypeName.substring(0, 3);
						}
					}
					id[1] = processTypeName;

					/**
					 * 流程实例id
					 */
					String[] instanceId = new String[2];
					instanceId[0] = "string";
					instanceId[1] = objs[5] == null ? "" : objs[5].toString();
					
					result.add(taskId);
					result.add(taskStartDate);
					result.add(businessNameArray);
					result.add(startUserNameArray);
					result.add(id);
					result.add(preActor);
					result.add(instanceId);
				}
			}

			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 7,
					String.valueOf(pageWorkflow.getTotalCount()), String
							.valueOf(pageWorkflow.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型待办事宜发生数据级异常:"
					+ ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型待待办事宜发生服务级异常:"
					+ ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型待待办事宜发生系统级异常:"
					+ ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型待待办事宜发生未知异常:"
					+ ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * @Description: 提供获取其他类型已办事宜接口,支持分页、查询(待办事宜模块).
	 * @author penghj
	 * @date Mar 14, 2012 8:17:20 PM
	 * @param userId
	 *            用户id
	 * @param userName
	 *            拟稿人姓名,支持模糊查询
	 * @param businessName
	 *            业务数据标题,支持模糊查询
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示待办事宜数量
	 * @param startDate
	 *            任务接收日期开始范围
	 * @param endDate
	 *            任务接收日期结束范围
	 * @return XML 格式的数据
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String getShoufawenProcessed(String userId, String userName,
			String businessName, String pageNo, String pageSize,
			String startDate, String endDate) throws DAOException,
			ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			List<String> items = new ArrayList<String>(); // 查询的属性
			items.add("taskId"); // 任务id
			items.add("taskStartDate"); // 任务开始时间
			items.add("businessName"); // 业务数据标题
			items.add("startUserName"); // 拟稿人
			items.add("processMainFormBusinessId"); // 业务数据 表名;主键名;主键值
			items.add("processTypeName"); // 公文类别名称
			items.add("processInstanceId");

			Map<String, Object> paramsMap = new HashMap<String, Object>();// 查询条件设置
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}

			List<String> type = new ArrayList<String>(2);// 流程类型，查询除收发文的其他流程类型
			// 获取流程类型
			List<Object[]> typeList = processDefinitionService
					.getAllProcessTypeList();
			if (typeList != null && !typeList.isEmpty()) {
				for (Object[] processType : typeList) {
					// 过滤掉收发文流程类型
					if (!"2".equals(String.valueOf(processType[0]))
							&& !"3".equals(String.valueOf(processType[0]))) {
						type.add(String.valueOf(processType[0]));
					}
				}
			}
			// 以下为查询条件
			paramsMap.put("handlerId", userId); // 处理人
			paramsMap.put("taskType", "3"); // 已办
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("processTypeId", type);
			if (userName != null && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			if (startDate != null && !"".equals(startDate)) {
				Date sd = this.parseDate(startDate);
				paramsMap.put("taskStartDateStart", sd);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date ed = this.parseDate(endDate);
				paramsMap.put("taskStartDateEnd", ed);
			}
			// --------- ------ //
			Map orderMap = new HashMap();
			orderMap.put("taskStartDate", "1");// 要排序的属性
			if (pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if (pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));
			// 获取公文List
			List<Object[]> listWorkflow = workflowService
					.getTaskInfosByConditionForList(items, paramsMap, orderMap,
							null, null, null, null);
			List<String[]> result = new ArrayList<String[]>();

			List<Object[]> dlist = new ArrayList<Object[]>();// 存储非重复的任务
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (int i = 0; i < listWorkflow.size(); i++) {
					Object[] objs = (Object[]) listWorkflow.get(i);
					String businessId = (String) objs[4];
					if (businessId == null) {// 子流程数据
						String strNodeInfo = workflowService
								.getNodeInfo(objs[0].toString());
						String[] arrNodeInfo = strNodeInfo.split(",");
						businessId = arrNodeInfo[2];
					}
					if (!checkList.contains(businessId)) {
						dlist.add(objs);
						checkList.add(businessId);
					}
				}
				checkList.clear();
			}
			Page pageWorkflow = ListUtils.splitList2Page(page, dlist);
			dlist = pageWorkflow.getResult();
			if (dlist != null && !dlist.isEmpty()) {
				for (Object[] objs : dlist) {
					/**
					 * 根据流程实例id获取当前处理人
					 */
					Object[] ds = workflowService
							.getProcessStatusByPiId(objs[6].toString());
					Collection col2 = (Collection) ds[6];// 处理任务信息
					String curDealUserName = "";
					if (col2 != null && !col2.isEmpty()) {
						for (Iterator it = col2.iterator(); it.hasNext();) {
							Object[] itObjs = (Object[]) it.next();
							String tempUserId = (String) itObjs[3];
							if (tempUserId != null && !"".equals(tempUserId)) {
								String[] userIds = tempUserId.split(",");
								for (String id : userIds) {
									User user = userService
											.getUserInfoByUserId(id);
									curDealUserName += user.getUserName() + ",";
								}
							}
						}
					}

					if ("".equals(curDealUserName)) {
						List<Object[]> ss = workflowService
								.getMonitorChildrenInstanceIds(new Long(objs[6]
										.toString()));
						for (Object[] s : ss) {
							String prcessInstanceCId = s[0].toString();
							Object[] tmpDeal = workflowService
									.getProcessStatusByPiId(prcessInstanceCId);
							Collection col3 = (Collection) tmpDeal[6];// 处理任务信息
							if (col3 != null && !col3.isEmpty()) {
								for (Iterator it = col3.iterator(); it
										.hasNext();) {
									Object[] itObjs = (Object[]) it.next();
									String tempUserId = (String) itObjs[3];
									if (tempUserId != null
											&& !"".equals(tempUserId)) {
										String[] userIds = tempUserId
												.split(",");
										for (String id : userIds) {
											User user = userService
													.getUserInfoByUserId(id);
											curDealUserName += user
													.getUserName()
													+ ",";
										}
									}
								}
							}
						}
					}

					String[] taskId = new String[2];
					taskId[0] = "string";
					taskId[1] = objs[0] == null ? "" : objs[0].toString();

					String[] taskStartDate = new String[2];
					taskStartDate[0] = "date";
					taskStartDate[1] = objs[1] == null ? "" : objs[1]
							.toString();

					String[] businessNameArray = new String[2];
					businessNameArray[0] = "string";
					businessNameArray[1] = objs[2] == null ? "" : objs[2]
							.toString();

					if (!"".equals(curDealUserName)) {
						curDealUserName = curDealUserName.substring(0,
								curDealUserName.length() - 1);
					}
					String[] startUserNameArray = new String[2];
					startUserNameArray[0] = "string";
					startUserNameArray[1] = curDealUserName;

					String[] id = new String[2];
					String processTypeName = "";
					id[0] = "string";
					if (objs[5] != null) {
						processTypeName = objs[5].toString();
						if (processTypeName.length() > 4) {
							processTypeName = processTypeName.substring(0, 3);
						}
					}
					id[1] = processTypeName;

					result.add(taskId);
					result.add(taskStartDate);
					result.add(businessNameArray);
					result.add(startUserNameArray);
					result.add(id);
				}
			}

			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 5,
					String.valueOf(pageWorkflow.getTotalCount()), String
							.valueOf(pageWorkflow.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型已办事宜发生数据级异常:"
					+ ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型已办事宜发生服务级异常:"
					+ ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型已办事宜发生系统级异常:"
					+ ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取其他类型已办事宜发生未知异常:"
					+ ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * 提供获取收发文待办事宜接口,支持分页、查询(公文处理模块).
	 * 
	 * @param userId
	 *            用户id
	 * @param userName
	 *            拟稿人姓名,支持模糊查询
	 * @param businessName
	 *            业务数据标题,支持模糊查询
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示待办事宜数量
	 * @param startDate
	 *            任务接收日期开始范围
	 * @param endDate
	 *            任务接收日期结束范围
	 * @return XML格式字符的待办事宜数据  服务调用成功时返回数据格式如下： <?xml version="1.0"
	 *         encoding="UTF-8"?> <service-response> <status>1</status>
	 *         <fail-reason /> <data totalCount="总记录数" totalPages="总页数"> <row>
	 *         <item type="string" value="任务id" /> <item type="date"
	 *         value="任务接收时间" /> <item type="string" value="业务数据标题" /> <item
	 *         type="string" value="拟稿人" /> <item type="string" value="公文类别名称" />
	 *         <item type="string" value="上一步处理人" /> </row> </data>
	 *         </service-response>  服务调用失败时返回数据格式如下： <?xml version="1.0"
	 *         encoding="UTF-8"?> <service-response> <status>0</status>
	 *         <fail-reason>异常描述</fail-reason> <data /> </service-response>
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String getOtherTodo(String userId, String userName,
			String businessName, String pageNo, String pageSize,
			String startDate, String endDate) throws DAOException,
			ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			List<String> items = new ArrayList<String>();
			items.add("taskId"); // 任务id
			items.add("taskStartDate"); // 任务开始时间
			items.add("businessName"); // 业务数据标题
			items.add("startUserName"); // 拟稿人
			items.add("processTypeName"); // 公文类别名称
			items.add("processInstanceId");//流程实例id
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}
			paramsMap.put("handlerId", userId); // 处理人
			paramsMap.put("taskType", "2"); // 待办
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			List<String> type = new ArrayList<String>(2);// 流程类型，仅查询收发文

			type.add(String.valueOf(WorkFlowTypeConst.SENDDOC));// 发文
			type.add(String.valueOf(WorkFlowTypeConst.RECEDOC));// 收文

			paramsMap.put("processTypeId", type);
			// 以下为查询条件
			if (userName != null && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			if (startDate != null && !"".equals(startDate)) {
				Date sd = this.parseDate(startDate);
				paramsMap.put("taskStartDateStart", sd);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date ed = this.parseDate(endDate);
				paramsMap.put("taskStartDateEnd", ed);
			}
			// --------- ------ //
			Map orderMap = new HashMap();
			orderMap.put("taskStartDate", "1");
			if (pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if (pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));
			String customSelectItems="BI.PERSON_CONFIG_FLAG";
			String customFromItems="T_JBPM_BUSINESS BI";
			String customQuery="BI.BUSINESS_ID=@businessId";
			String customOrderBy=" BI.PERSON_CONFIG_FLAG  desc,ti.START_ desc";
			Page<Object[]> pageWorkflow = workflowService
					.getTaskInfosByConditionForPage(page, items, paramsMap,
							orderMap, customSelectItems, customFromItems, customQuery, null,customOrderBy);
			/*Page<Object[]> pageWorkflow = workflowService
					.getTaskInfosByConditionForPage(page, items, paramsMap,
							orderMap, null, null, null, null);*/
			int intRet = Integer.parseInt(pageNo);
			if(pageWorkflow.getTotalPages()<intRet){
				pageWorkflow = new Page<Object[]>();
			}
			List<String[]> result = new ArrayList<String[]>();
			List<Object[]> listWorkflow = pageWorkflow.getResult();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (Object[] objs : listWorkflow) {
					String[] taskId = new String[2];
					taskId[0] = "string";
					taskId[1] = objs[0] == null ? "" : objs[0].toString();

					List<Long> list = new ArrayList<Long>();
					list.add(Long.parseLong(objs[0].toString()));
					Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = null;
					TaskIdMapPreTaskBeanTemp = workflowForIpadService
							.getTaskIdMapPreTaskBeanTemper(list, userId);

					// 得到上一步处理人
					String preTaskActor = null;
					preTaskActor = TaskIdMapPreTaskBeanTemp.get(
							objs[0].toString()).getTaskActorName();
					String[] preActor = new String[2];
					preActor[0] = "string";
					preActor[1] = preTaskActor;

					String[] taskStartDate = new String[2];
					taskStartDate[0] = "date";
					taskStartDate[1] = objs[1] == null ? "" : objs[1]
							.toString();

					String[] businessNameArray = new String[2];
					businessNameArray[0] = "string";
					businessNameArray[1] = objs[2] == null ? "" : objs[2]
							.toString();

					String[] startUserNameArray = new String[2];
					startUserNameArray[0] = "string";
					startUserNameArray[1] = objs[3] == null ? "" : objs[3]
							.toString();

					String[] id = new String[2];
					String processTypeName = "";
					id[0] = "string";
					if (objs[4] != null) {
						processTypeName = objs[4].toString();
						if (processTypeName.length() > 4) {
							processTypeName = processTypeName.substring(0, 3);
						}
					}
					/**
					 * 流程实例id
					 */
					String[] instanceId = new String[2];
					instanceId[0] = "string";
					instanceId[1] = objs[5] == null ? "" : objs[5].toString();
					
					/**
					 * 加急程度
					 */
					String[] personConfigFlag = new String[2];
					personConfigFlag[0] = "string";
					personConfigFlag[1] = objs[6] == null ? "" : objs[6].toString();
					
					id[1] = processTypeName;

					result.add(taskId);
					result.add(taskStartDate);
					result.add(businessNameArray);
					result.add(startUserNameArray);
					result.add(id);
					result.add(preActor);
					result.add(instanceId);
					result.add(personConfigFlag);
				}
			}

			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 8,
					String.valueOf(pageWorkflow.getTotalCount()), String
							.valueOf(pageWorkflow.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文待办事宜发生数据级异常:"
					+ ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文待办事宜发生服务级异常:"
					+ ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文待办事宜发生系统级异常:"
					+ ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文待办事宜发生未知异常:"
					+ ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * @Description: 提供获取收发文已办事宜接口,支持分页、查询(公文处理模块).
	 * @author penghj
	 * @date Mar 14, 2012 8:17:20 PM
	 * @param userId
	 *            用户id
	 * @param userName
	 *            拟稿人姓名,支持模糊查询
	 * @param businessName
	 *            业务数据标题,支持模糊查询
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示待办事宜数量
	 * @param startDate
	 *            任务接收日期开始范围
	 * @param endDate
	 *            任务接收日期结束范围
	 * @return XML 格式的数据
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String getOtherProcessed(String userId, String userName,
			String businessName, String pageNo, String pageSize,
			String startDate, String endDate) throws DAOException,
			ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			List<String> items = new ArrayList<String>(); // 查询的属性
			items.add("taskId"); // 任务id
			items.add("taskEndDate"); // 上一步任务结束时间
			items.add("businessName"); // 业务数据标题
			items.add("startUserName"); // 拟稿人
			items.add("processMainFormBusinessId"); // 业务数据 表名;主键名;主键值
			items.add("processTypeName"); // 公文类别名称
			items.add("processInstanceId");

			Map<String, Object> paramsMap = new HashMap<String, Object>();// 查询条件设置
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}
			paramsMap.put("handlerId", userId); // 处理人
			paramsMap.put("taskType", "3"); // 已办
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			List<String> type = new ArrayList<String>(2);// 流程类型，仅查询收发文

			type.add(String.valueOf(WorkFlowTypeConst.SENDDOC));// 发文
			type.add(String.valueOf(WorkFlowTypeConst.RECEDOC));// 收文

			paramsMap.put("processTypeId", type);
			// 以下为查询条件
			if (userName != null && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", "%" + businessName + "%");
			}
			if (startDate != null && !"".equals(startDate)) {
				Date sd = this.parseDate(startDate);
				paramsMap.put("taskStartDateStart", sd);
			}
			if (endDate != null && !"".equals(endDate)) {
				Date ed = this.parseDate(endDate);
				paramsMap.put("taskStartDateEnd", ed);
			}
			// --------- ------ //
			Map orderMap = new HashMap();
			orderMap.put("taskEndDate", "1");// 要排序的属性
			if (pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if (pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));
			// 获取公文List
			List<Object[]> listWorkflow = workflowService
					.getTaskInfosByConditionForList(items, paramsMap, orderMap,
							null, null, null, null);
			List<String[]> result = new ArrayList<String[]>();

			List<Object[]> dlist = new ArrayList<Object[]>();// 存储非重复的任务
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (int i = 0; i < listWorkflow.size(); i++) {
					Object[] objs = (Object[]) listWorkflow.get(i);
					String businessId = (String) objs[4];
					if (businessId == null) {// 子流程数据
						String strNodeInfo = workflowService
								.getNodeInfo(objs[0].toString());
						String[] arrNodeInfo = strNodeInfo.split(",");
						businessId = arrNodeInfo[2];
					}
					if (!checkList.contains(businessId)) {
						dlist.add(objs);
						checkList.add(businessId);
					}
				}
				checkList.clear();
			}
			Page pageWorkflow = ListUtils.splitList2Page(page, dlist);
			dlist = pageWorkflow.getResult();
			if (dlist != null && !dlist.isEmpty()) {
				for (Object[] objs : dlist) {
					/**
					 * 根据流程实例id获取当前处理人
					 */
					Object[] ds = workflowService
							.getProcessStatusByPiId(objs[6].toString());
					Collection col2 = (Collection) ds[6];// 处理任务信息
					// 处理人
					String curDealUserName = "";
					if (col2 != null && !col2.isEmpty()) {
						for (Iterator it = col2.iterator(); it.hasNext();) {
							Object[] itObjs = (Object[]) it.next();
							String tempUserId = (String) itObjs[3];
							if (tempUserId != null && !"".equals(tempUserId)) {
								String[] userIds = tempUserId.split(",");
								for (String id : userIds) {
									User user = userService
											.getUserInfoByUserId(id);
									curDealUserName += user.getUserName() + ",";
								}
							}
						}
					}
					if ("".equals(curDealUserName)) {
						List<Object[]> ss = workflowService
								.getMonitorChildrenInstanceIds(new Long(objs[6]
										.toString()));
						for (Object[] s : ss) {
							String prcessInstanceCId = s[0].toString();
							Object[] tmpDeal = workflowService
									.getProcessStatusByPiId(prcessInstanceCId);
							Collection col3 = (Collection) tmpDeal[6];// 处理任务信息
							if (col3 != null && !col3.isEmpty()) {
								for (Iterator it = col3.iterator(); it
										.hasNext();) {
									Object[] itObjs = (Object[]) it.next();
									String tempUserId = (String) itObjs[3];
									if (tempUserId != null
											&& !"".equals(tempUserId)) {
										String[] userIds = tempUserId
												.split(",");
										for (String id : userIds) {
											User user = userService
													.getUserInfoByUserId(id);
											curDealUserName += user
													.getUserName()
													+ ",";
										}
									}
								}
							}
						}
					}

					String[] taskId = new String[2];
					taskId[0] = "string";
					taskId[1] = objs[0] == null ? "" : objs[0].toString();

					String[] taskStartDate = new String[2];
					taskStartDate[0] = "date";
					taskStartDate[1] = objs[1] == null ? "" : objs[1]
							.toString();

					String[] businessNameArray = new String[2];
					businessNameArray[0] = "string";
					businessNameArray[1] = objs[2] == null ? "" : objs[2]
							.toString();

					if (!"".equals(curDealUserName)) {
						curDealUserName = curDealUserName.substring(0,
								curDealUserName.length() - 1);
					}

					String[] startUserNameArray = new String[2];
					startUserNameArray[0] = "string";
					startUserNameArray[1] = curDealUserName;

					String[] id = new String[2];
					String processTypeName = "";
					id[0] = "string";
					if (objs[5] != null) {
						processTypeName = objs[5].toString();
						if (processTypeName.length() > 4) {
							processTypeName = processTypeName.substring(0, 3);
						}
					}
					id[1] = processTypeName;

					result.add(taskId);
					result.add(taskStartDate);
					result.add(businessNameArray);
					result.add(startUserNameArray);
					result.add(id);
				}
			}

			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 5,
					String.valueOf(pageWorkflow.getTotalCount()), String
							.valueOf(pageWorkflow.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文已办事宜发生数据级异常:"
					+ ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文已办事宜发生服务级异常:"
					+ ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文已办事宜发生系统级异常:"
					+ ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发文已办事宜发生未知异常:"
					+ ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * 得到表单数据,附件数据,办理记录
	 * 
	 * @param taskId
	 *            任务实例id
	 * @return 服务调用成功时发文数据格式如下： <?xml version="1.0" encoding="UTF-8"?>
	 *         <service-response> <status>1</status> <fail-reason /> <data>
	 *         <form> <item type="string" value="编号" /> <item type="string"
	 *         value="紧急程度" /> <item type="string" value="拟稿单位" /> <item
	 *         type="string" value="拟稿人" /> <item type="string" value="标题" />
	 *         <item type="string" value="主送" /> <item type="string" value="抄送" />
	 *         <item type="string" value="主题词" /> <item type="string"
	 *         value="信息公开" /> <item type="string" value="发文日期" /> <item
	 *         type="string" value="录入" /> <item type="string" value="校对" />
	 *         </form> <attachments> <attachment> <item type="string"
	 *         value="附件id" /> <item type="string" value="附件名称" /> </attachment>
	 *         </attachments> <suggestions> <suggestion> <item type="date"
	 *         value="任务处理时间" /> <item type="string" value="处理人部门名称" /> <item
	 *         type="string" value="处理人名称" /> <item type="string" value="意见内容" />
	 *         </suggestion> </suggestions> </data> </service-response>
	 * 
	 * 服务调用成功时收文返回数据格式如下：<?xml version="1.0" encoding="UTF-8"?>
	 * <service-response> <status>1</status> <fail-reason /> <data> <form>
	 * <item type="string" value="来文单位" /> <item type="string" value="来文号" />
	 * <item type="string" value="来文时间" /> <item type="string" value="事由" />
	 * <item type="string" value="编号" /> <item type="string" value="紧急程度" />
	 * </form> <attachments> <attachment> <item type="string" value="附件记录id" />
	 * <item type="string" value="附件名称" /> </attachment> </attachments>
	 * <suggestions> <suggestion> <item type="date" value="任务处理时间" /> <item
	 * type="string" value="处理人部门名称" /> <item type="string" value="处理人名称" />
	 * <item type="string" value="意见内容" /> </suggestion> </suggestions> </data>
	 * </service-response>  服务调用失败时返回数据格式如下： <?xml version="1.0"
	 * encoding="UTF-8"?> <service-response> <status>0</status>
	 * <fail-reason>异常描述</fail-reason> <data /> </service-response>
	 * @throws Exception
	 */
	public String loadFormData(String loginUserId, String taskId)
			throws Exception {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			// 获取公文的表单详细信息
			ret = workflowForIpadService.getForm(loginUserId, taskId);
			// 恢复会签挂起的任务
			baseManager.resumeConSignTask(taskId);
		} catch (Exception ex) {
			ex.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生未知异常:"
					+ ex.getMessage(), null, null);
		}
		return ret;
	}

	/**
	 * 得到公文内容
	 * 
	 * @param taskId
	 *            任务id
	 * @return 服务调用成功时返回数据格式如下： <?xml version="1.0" encoding="UTF-8"?>
	 *         <service-response> <status>1</status> <fail-reason /> <data>
	 *         <item type="string" value="Base64格式的正文内容" /> </data>
	 *         </service-response>  服务调用失败时返回数据格式如下： <?xml version="1.0"
	 *         encoding="UTF-8"?> <service-response> <status>0</status>
	 *         <fail-reason>异常描述</fail-reason> <data /> </service-response>
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public String loadContent(String taskId) throws DAOException,
			ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			String strNodeInfo = workflowService.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			String bussinessId = arrNodeInfo[2]; // 业务数据id
			String formId = arrNodeInfo[3]; // 表单模板id
			List<EFormField> fieldList = manager
					.getFormTemplateFieldList(formId);
			EFormField eformField = null;
			for (EFormField field : fieldList) {
				if ("Office".equals(field.getType())) {// 找到OFFICE控件类型
					eformField = field;
					break;
				}
			}
			String pdf = null;
			for (EFormField field : fieldList) {
				if ("ADOBE_PDF_NAME".equals(field.getFieldname())) {// 找到OFFICE控件类型
				    	pdf = "ADOBE_PDF_NAME";
					break;
				}
			}
			if (eformField == null && pdf == null) {
				throw new SystemException("表单中不存在正文。");
			}
			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkFieldName = args[1];
			String pkFieldValue = args[2];
			String  pdffileName = null;
			if(pdf != null && !"".equals(pdf)){
			      Object obj = baseManager.getFieldValue(pdf, tableName, pkFieldName, pkFieldValue);
			      if(obj !=null){
			    	  pdffileName = obj.toString();
			      }
			}
			InputStream is = null;
			if(pdf != null && pdffileName != null && !"".equals(pdf) && !"".equals(pdffileName)) {
			    String path = System.getProperty("user.dir");
			    path = path.replaceAll("bin", "webapps");
			    logger.info("path:"+path);
			    String s = File.separator;
			    path = path + s +"oa"+s+"pdfFile"+s;
			    System.out.println("path:"+path+"\n"+"pdffileName:"+pdffileName);
			    logger.info("Finalpath:"+path+pdffileName);
			    File file = new File(path +pdffileName);
			    long length_byte = file.length();
			    if (length_byte >= 1024) {
					double length_k = ((double) length_byte) / 1024;
					if (length_k >= 6144) {
						ret = dom.createItemResponseData(STATUS_FAIL, "正文大小超过6M,请在PC端查看正文！", null, null);
						logger.info(ret);
						return ret;
					} 
				}

			    is  = new FileInputStream(file);
			    BASE64Encoder encoder = new BASE64Encoder();
			    String content = "";
				if (is != null) {
					content = encoder.encode(FileUtil.inputstream2ByteArray(is));
				}
				ret = dom.createItemResponseData(STATUS_SUC, null, "string",
						content);

			}else{
				is = (InputStream) baseManager.getFieldValue(eformField
						.getFieldname(), tableName, pkFieldName, pkFieldValue);
	
				// 转换成PDF流
				ByteArrayOutputStream os = (ByteArrayOutputStream) Office2PDF
						.office2PDF(is);
				if (os != null) {
					is = (InputStream) new ByteArrayInputStream(os.toByteArray());
				} else {
					if ("0".equals(Office2PDF.serviceRunState)) {
						ret = dom.createItemResponseData(STATUS_FAIL,
								"openOffice服务没有开启!", null, null);
						return ret;
					}
				}
	
				BASE64Encoder encoder = new BASE64Encoder();
				String content = "";
				if (is != null) {
					content = encoder.encode(FileUtil.inputstream2ByteArray(is));
				}
				ret = dom.createItemResponseData(STATUS_SUC, null, "string",
						content);
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生数据级异常:"
					+ ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生服务级异常:"
					+ ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生系统级异常:"
					+ ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生未知异常:"
					+ ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * 得到公文附件
	 * 
	 * @param id
	 *            附件id
	 * @return 服务调用成功时返回数据格式如下： <?xml version="1.0" encoding="UTF-8"?>
	 *         <service-response> <status>1</status> <fail-reason /> <data>
	 *         <item type="string" value="Base64格式的附件内容" /> </data>
	 *         </service-response>  服务调用失败时返回数据格式如下： <?xml version="1.0"
	 *         encoding="UTF-8"?> <service-response> <status>0</status>
	 *         <fail-reason>异常描述</fail-reason> <data /> </service-response>
	 */
	public String loadAttachment(String id) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			// 获取附件对象
			WorkflowAttach attach = attachService.get(id);
			if (attach == null || attach.getAttachContent() == null) {
				//throw new SystemException("附件'" + id + "'不存在！");
				throw new SystemException("附件不存在！");
			}else{
				long length_byte = attach.getAttachContent().length;
			    if (length_byte >= 1024) {
					double length_k = ((double) length_byte) / 1024;
					if (length_k >= 6144) {
						ret = dom.createItemResponseData(STATUS_FAIL, "附件大小超过6M,请在PC端查看附件！", null, null);
						logger.info(ret);
						return ret;
					} 
				}
			}

			// 将数据转换为BASE64格式
			BASE64Encoder encoder = new BASE64Encoder();
			String content = encoder.encode(attach.getAttachContent());
			ret = dom.createItemResponseData(STATUS_SUC, null, "string",
					content);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文附件发生异常:"
					+ ex.getMessage(), null, null);
		}
		return ret;
	}

	/**
	 * @Description: TODO( 获得当前公文办理时，下一步流程迁移线列表)
	 * @author penghj
	 * @date Mar 14, 2012 8:29:20 PM
	 * @param taskId
	 *            任务id
	 * @return
	 */
	public String getNextTransitions(String taskId, String instanceId,String userId) {
	    
		userService.setUserId(userId);
		String transitionIds = null;
		//List ss = workflowService.getNextTransitions(taskId);
//	    	List<Object[]> obj =new ArrayList();
//	    	obj=workflowService.getNextTransitions(taskId);
			//getTransitions(taskId);
//		for(Object[] transition:obj){
//		    //Object ob=transition[0];
//		    if("厅林改办".equals(transition[0].toString())){
//			transitionIds = transition[1].toString();
//		    }
//		}

		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();
		String nodeName=""; // 任务节点名称
		List<String[]> transitions = new ArrayList<String[]>();
		try {
			if (taskId != null && !"undefined".equals(taskId)
					&& !"".equals(taskId)) {
				// 获取指定任务实例的下一步可选流向
				List list = workflowService.getNextTransitions(taskId);
				// 处理迁移线信息，并且返回迁移线信息存储对象（包含提交下一步迁移线信息，迁移线id信息，节点id信息）
				TransitionsInfoBean transitionsInfoBean = transitionService
						.doNextTransitionsBySelectActorSetStyle(list);
				if (transitionsInfoBean != null) {
					list = transitionsInfoBean.getTransitionsInfo();
				}
				if (list != null && !list.isEmpty()) {
					 // 按照节点设置信息中的选择人员的设置方式处理迁移线信息
					ContextInstance cxt = null;
					Boolean boolTransDept = false;// 协办处室标志
					Boolean isMoreTransDept = false;// 是否是多个科室
					if (instanceId != null && !"".equals(instanceId)) {
						cxt = adapterBaseWorkflowManager.getWorkflowService()
								.getContextInstance(instanceId);
						if (cxt != null) {
							String isSubProcess = (String) cxt
									.getVariable(WorkflowConst.WORKFLOW_SUB_ISSUBPROCESS);// 是否是子流程
							if (isSubProcess != null && "1".equals(isSubProcess)) {// 是子流程
								Long parentProcessID = (Long) cxt
										.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);
								ProcessInstance parentProcessInstance = adapterBaseWorkflowManager
										.getWorkflowService()
										.getProcessInstanceById(
												parentProcessID.toString());
								cxt = adapterBaseWorkflowManager
										.getWorkflowService().getContextInstance(
												parentProcessID.toString());
								String transDeptFlag = (String) cxt
										.getVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_TRANSITIONDEPT);
								// 验证是否选择了多个协办处室为优先级1
								if (transDeptFlag != null
										&& !"".equals(transDeptFlag) && // 协办处室
										transDeptFlag.indexOf(",") != -1) {// 选择多于一个协办处室
									// 过滤掉类型为协办处室的迁移线
									boolTransDept = true;
									// 判断启动此子流程时是选择了多个处室还是多个科室
									List childInstanceList = adapterBaseWorkflowManager
											.getWorkflowService()
											.getSubProcessInstanceInfos(parentProcessID+"");
									int count = 0;
									for (int i = 0; i < childInstanceList.size(); i++) {
										Object[] instances = (Object[]) childInstanceList
												.get(i);
										Long supNodeId = adapterBaseWorkflowManager
												.getWorkflowConstService()
												.getSuperProcessNodeIdByPid(
														instances[0].toString());
										if (supNodeId != null) {
											String isAllowContinueToDept = adapterBaseWorkflowManager
													.getNodesettingPluginService()
													.getNodesettingPluginValue(
															supNodeId.toString(),
															NodesettingPluginConst.PLUGINS_ISNOTALLOWCONTINUETODEPT);
											if (isAllowContinueToDept != null
													&& "1"
															.equals(isAllowContinueToDept)) {
												count++;
											}
										}
									}
									if (count > 1) {
										isMoreTransDept = true;// 选择了多个科室
									}
								} else {
									if (parentProcessID != null) {// 得到父流程实例id
										List parentInstance = adapterBaseWorkflowManager
												.getWorkflowService()
												.getParentInstanceId(
														parentProcessInstance
																.getId());
										if (parentInstance != null
												&& !parentInstance.isEmpty()) {// 属于第三级子流程
											boolTransDept = true;// 属于第三级子流程时默认不允许继续向下协办,如果设置了允许则覆盖默认的设置
										}
									}
								}
							}
						}
					}
					Map<String, List<Object[]>> transGroupMap = new HashMap<String, List<Object[]>>();// 分组信息
					String noGroupKey = String.valueOf(Long.MAX_VALUE);// 没有设置分组的默认显示在最后
					List<ToaSysmanageDictitem> items = adapterBaseWorkflowManager
							.getDictItemManager().getAllDictItems(
									"40288f2931842af601318451d879002b");// 得到迁移线分组
					final Map<String, Object[]> dictItemMap = new HashMap<String, Object[]>();// 排序号信息
					if (items != null && !items.isEmpty()) {
						for (ToaSysmanageDictitem item : items) {
							Long seq = Long.MAX_VALUE;
							if (item.getDictItemDescspell() != null
									&& !"".equals(item.getDictItemDescspell())) {
								try {
									seq = Long.parseLong(item
											.getDictItemDescspell());
								} catch (Exception e) {
								}
							}
							dictItemMap.put(item.getDictItemCode(), new Object[] {
									seq, item.getDictItemName() });
						}
					}
					String defaultGroupName = "其他";
					final HashMap<String,OATransitionPlugin> transitionPluginMap = new HashMap<String, OATransitionPlugin>();
					Set<Long> transIdList = transitionsInfoBean.getTransitionIds();
					Set<String> pluginNames = new HashSet<String>();
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANSITIONGROUP);		// 迁移线分组
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANSITIONPRI);		// 排序号
					pluginNames.add(TransitionPluginConst.PLUGINS_DEPT);				// 协办处室
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANLINETYPE);		// 迁移线类型 "defaultType":默认|"checkboxType":复选|"radioType":单选
					pluginNames.add(TransitionPluginConst.PLUGINS_SELECTPERSONTYPE);	// 人员选择模式 "treeType":树型|"buttonType":按钮型
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANLINEVALIDATE);	// 办理必填验证
					Map<Long, Map<String, String>> transitionPluginValueMap = adapterBaseWorkflowManager.getTransitionPluginService().getTransitionPluginValueForMapByTransitionIdsAndpluginNames(transIdList, pluginNames);
					if(transitionPluginValueMap == null){
						transitionPluginValueMap = new LinkedHashMap<Long, Map<String,String>>();
					}
					boolean isSetGroup = false;// 是否有迁移线设置了分组
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);
						String transitionId = objs[1].toString();
						Long tsId = Long.valueOf(transitionId);
						Map<String, String> pluginValueMap = transitionPluginValueMap.get(tsId);
						if(pluginValueMap == null){
							pluginValueMap = new HashMap<String, String>(); 
						}
						// "1":协办处室
						String transGroupName = pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANSITIONGROUP);// 所属分组
						String transSeq = pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANSITIONPRI);// 排序号
						String transDept = (pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE) == null
								?"0" :pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE));
						String tranlineType = (pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE) == null
								?"defaultType":pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE));// 迁移线类型
						String selectPersonType = (pluginValueMap.get(TransitionPluginConst.PLUGINS_SELECTPERSONTYPE) == null
								?"treeType":pluginValueMap.get(TransitionPluginConst.PLUGINS_SELECTPERSONTYPE));// 人员选择方式
						String tranlineValidate = (pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINEVALIDATE) == null
								?"":pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINEVALIDATE));// 迁移线类型
						if(!transitionPluginMap.containsKey(transitionId)){
							OATransitionPlugin oaTransitionPlugin = new OATransitionPlugin();
							oaTransitionPlugin.setDept(transDept);// 设置协办处室
							oaTransitionPlugin.setTransitionPri(transSeq);// 设置排序号
							oaTransitionPlugin.setTransitiongroup(transGroupName);// 设置所属分组
							oaTransitionPlugin
									.setSelectPersonType(selectPersonType);// 设置人员选择模式
							oaTransitionPlugin.setTranlineType(tranlineType);// 设置迁移线类型
							oaTransitionPlugin
									.setTranlineValidate(tranlineValidate);// 设置是否办结必填验证
							transitionPluginMap.put(transitionId,
									oaTransitionPlugin);
						}
						// 判断父流程是否选择了多个协办处室,如果选择了多个协办处室,那么在子流程中协办处室的线不能显示
						if (boolTransDept) {
							// 过滤掉类型为协办处室的迁移线
							if ("1".equals(transDept)) {// 属于协办处室的迁移线
								if (isMoreTransDept) {// 选择了多个科室不允许继续协办
									continue;
								}
								// 若是科室子流程,则可以继续出现
								Set<String> transSet = (Set<String>) objs[3];
								boolean isContinue = false;
								;
								for (String tran : transSet) {
									String[] info = tran.split("\\|");
									if (info[0].equals("subProcessNode")) {
										String nodeId = info[1];
										TwfBaseNodesetting setting = adapterBaseWorkflowManager
												.getWorkflowService()
												.getNodesettingByNodeId(nodeId);
										String isAllowContinueToDept = setting
												.getPlugin(NodesettingPluginConst.PLUGINS_ISNOTALLOWCONTINUETODEPT);
										if (isAllowContinueToDept != null
												&& "1"
														.equals(isAllowContinueToDept)) {
											// 科室子流程
											isContinue = true;
											if (cxt != null) {
												String flag = (String) cxt
														.getVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_ISALLOWCONTINUETODEPT);
												if (flag != null
														&& "1".equals(flag)) {// 说明到了第二次科室子流程
													isContinue = false;
												}
											}
										}
										break;
									}
								}
								if (!isContinue) {
									continue;
								}
							}
						}
						// --------- End -------
						if (transGroupName != null
								&& !"".equals(transGroupName)) {// 设置了分组
							isSetGroup = true;
							if (!transGroupMap.containsKey(transGroupName)) {
								List<Object[]> temp = new ArrayList<Object[]>();
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号 --- 4
								objs = ObjectUtils.addObjectToArray(objs,
										dictItemMap.get(transGroupName));// 分组名称
								// ---
								// 5
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志 -- 6
								temp.add(objs);
								transGroupMap.put(transGroupName, temp);
							} else {
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号
								objs = ObjectUtils.addObjectToArray(objs,
										dictItemMap.get(transGroupName));// [分组号,分组名称]
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志
								transGroupMap.get(transGroupName).add(objs);
							}
						} else {// 未设置分组
							if (!transGroupMap.containsKey(noGroupKey)) {
								List<Object[]> temp = new ArrayList<Object[]>();
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号
								objs = ObjectUtils.addObjectToArray(objs,
										defaultGroupName);// 分组名称
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志
								temp.add(objs);
								transGroupMap.put(noGroupKey, temp);
							} else {
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号
								objs = ObjectUtils.addObjectToArray(objs,
										defaultGroupName);// 分组名称
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志
								transGroupMap.get(noGroupKey).add(objs);
							}
						}
					}

					List<String> transGroupList = new ArrayList<String>(
							transGroupMap.keySet());// 转化成List排序
					Collections.sort(transGroupList, new Comparator<String>() {
						public int compare(String o1, String o2) {
							Object[] objs1 = dictItemMap.get(o1);
							Object[] objs2 = dictItemMap.get(o2);
							Long s1, s2;
							if (objs1 == null) {
								s1 = Long.MAX_VALUE;
							} else {
								s1 = (Long) objs1[0];
							}
							if (objs2 == null) {
								s2 = Long.MAX_VALUE;
							} else {
								s2 = (Long) objs2[0];
							}
							return s1.compareTo(s2);
						}

					});

					String isChecked = "";
					if (list.size() == 1) {
						isChecked = "checked";
					}

					// transGroupIdAndName:存放分组信息，格式：分组(0)ID1|Name1 (1)ID2|Name2...
					List<String> transGroupIdAndNameList = new LinkedList<String>();
					for (String key : transGroupList) {
						//过滤迁移线分组信息【过滤依据是判断该迁移线所属分组是否为意见征询】	严建 2012-06-23 15:27
						/*if (key.equals(FormGridDataHelper.getColorSetProperties()
								.getProperty("yjzxGroupId"))) {// 该分组属于意见征询分组
							// 是否允许显示该意见征询分组【已经征询过的文不能再进行意见征询】
							String bid = (adapterBaseWorkflowManager
									.getWorkflowService().getProcessInstanceById(
											instanceId).getBusinessId());// 业务id
							if (bid != null) {
								Tjbpmbusiness tjbpmbusiness = adapterBaseWorkflowManager
										.getJbpmbusinessmanager().findByBusinessId(
												bid);
								if (tjbpmbusiness != null
										&& "0".equals(tjbpmbusiness
												.getBusinessType())) {// 已经进行征询的文不再进行征询
									continue;
								}
							}
						}*/
						list = transGroupMap.get(key);
						// 增加排序功能 按迁移线上定义的扩展属性排序 扩展属性名称：plugins_transitionpri 邓志城
						// 2011年7月20日16:01:33
						final Map<String, Object> map = new HashMap<String, Object>();
						List<Object[]> listWithPlugin = new ArrayList<Object[]>();
						List<Object[]> listWithNotPlugin = new ArrayList<Object[]>();
						Object groupName = null;
						for (int i = 0; i < list.size(); i++) {
							Object[] objs = (Object[]) list.get(i);
							String transitionId = objs[1].toString();
							Object value = objs[4];// 排序号
							if (groupName == null) {
								if (objs[5] instanceof Object[]) {
									groupName = ((Object[]) objs[5])[1];// 分组名称
								} else {
									if (isSetGroup) {
										groupName = defaultGroupName;
									}
								}
							}
							if (value != null && !"".equals(value)) {
								map.put(transitionId, value);
								listWithPlugin.add(objs);
							} else {
								listWithNotPlugin.add(objs);
							}
						}
						Collections.sort(listWithPlugin,
								new Comparator<Object[]>() {
									public int compare(Object[] o1, Object[] o2) {
										String trans1 = String.valueOf(o1[1]);
										String trans2 = String.valueOf(o2[1]);
										Object obj1 = map.get(trans1);
										Object obj2 = map.get(trans2);
										try {
											Long l1 = Long.parseLong(obj1
													.toString());
											Long l2 = Long.parseLong(obj2
													.toString());
											return l1.compareTo(l2);
										} catch (Exception e) {
											logger.error("迁移线排序解析出错", e);
											return 0;
										}
									}

								});
						map.clear();
						Collections.sort(listWithNotPlugin,
								new Comparator<Object[]>() {
									public int compare(Object[] o1, Object[] o2) {
										String transName1 = (String) o1[0];
										String transName2 = (String) o2[0];
										return transName1.compareTo(transName2);
									}

								});
						list.clear();
						list.addAll(listWithPlugin);
						list.addAll(listWithNotPlugin);
						// ----------------------- End ------------------------

						for (Iterator i = list.iterator(); i.hasNext();) {
							Object[] trans = (Object[]) i.next();
							Set<String> transInfo = (Set<String>) trans[3];
							String concurrentFlag = (String) trans[2];// 并发标示：“0”：非并发，“1”：并发
							String tranId = (String) ConvertUtils.convert(trans[1],
									String.class);// 迁移线id
							String tranName = (String) trans[0];// 迁移线名称
							String inputType = "radio";
							String nodeId = null;
							OATransitionPlugin oaTransitionPlugin = transitionPluginMap.get(tranId);	// 
							String tranlineType = oaTransitionPlugin.getTranlineType();					//迁移线类型
							String selectPersonType = oaTransitionPlugin.getSelectPersonType();			//人员选择模式
							String tranlineValidate = oaTransitionPlugin.getTranlineValidate();			//办理必填验证
							if ("0".equals(concurrentFlag)) {
								// 如果是子流程并且需要选择子流程第一个节点人员和子流程下一结点（父节点）人员
								if (String.valueOf(transInfo).indexOf("activeSet") != String
										.valueOf(transInfo)
										.lastIndexOf("activeSet")) {
									concurrentFlag = "2";
								}
								// 非动态选择处理人(包括结束节点)应该隐藏树
								if ((String.valueOf(transInfo).indexOf(
										"notActiveSet") != -1
										|| String.valueOf(transInfo).indexOf(
												"endNode") != -1
										|| String.valueOf(transInfo).indexOf(
												"decideNode") != -1 || String
										.valueOf(transInfo).indexOf(
												"subProcessNode") != -1)
										&& transInfo.size() == 1) {
									concurrentFlag = "3";// add by dengzc
									// 2010年3月30日20:33:06
								}
							} else {
								inputType = "checkbox";
								if (tranlineType.equals("radioType")) {// 迁移线设置为单选方式
									inputType = "radio";
								}
							}
							if (list.size() == 1) {// 只存在一根迁移线的时候,迁移线为单选
								inputType = "radio";
							}
							String[] nodeInfo = null;
							String tempNodeId = null;
							for (Iterator<String> it = transInfo.iterator(); it
									.hasNext();) {
								nodeInfo = String.valueOf(it.next()).split("\\|");
								String actorFlag = (String) nodeInfo[0];// 是否需要选择人员activeSet:需要重新选择人员
								nodeName = (String) nodeInfo[2];// 迁移线对应的节点名称
								if ("activeSet".equals(actorFlag)
										|| "subactiveSet".equals(actorFlag)
										|| "supactiveSet".equals(actorFlag)) {// 允许动态设置处理人
									nodeId = (String) nodeInfo[1];// 节点id
								} else {
									if (actorFlag.equals("notActiveSet")) {
										tempNodeId = (String) nodeInfo[1];// 节点id
									}
								}
							}
							String newnodeId = null;
							// 结束节点并且不需要选择人员
							if (String.valueOf(transInfo).indexOf("endNode") == 1
									&& (String.valueOf(transInfo).indexOf(
											"activeSet") == -1
											&& String.valueOf(transInfo).indexOf(
													"subactiveSet") == -1 && String
											.valueOf(transInfo).indexOf(
													"supactiveSet") == -1)) {
								if (nodeId == null) {
									nodeId = tempNodeId;
								}
								
								String[] listNode = new String[6];
								listNode[0] = tranId;// transtionId转移步骤id
								listNode[1] = tranName;// 转移步骤名称
								if(concurrentFlag.equals("1")){
									concurrentFlag = "0";
								}
								/**
								 * concurrentFlag 是否是并发模式：
								 * “0”：非并发；“1”：并发;"2"：子流程并且父节点需要选择人员;"3"：非动态选择处理人(包括结束节点)应该隐藏树
								 */
								listNode[2] = concurrentFlag;
								listNode[3] = nodeId;// 节点id
								listNode[4] = nodeName;// 节点名称
								/**
								 * 如果是这种情况就不显示选择人员界面，选择默认人员 0:示默认人员 1：默认人员
								 */
								if("buttonType".equals(selectPersonType)){
									//按钮选择人员
									listNode[5] = "0";
								}else{
									if ("1".equals(concurrentFlag)) {
										if ("radio".equals(inputType)) {
											listNode[5] = "1";
										} else {
											listNode[5] = "1";
										}
									} else if ("2".equals(concurrentFlag)) {
										//子流程并且父节点需要选择人员
										listNode[5] = "0";
									} else if ("3".equals(concurrentFlag)) {// //add by
																			// dengzc
										// 2010年3月30日20:33:06//非动态选择处理人应该隐藏
										listNode[5] = "1";
									} else {
										//结束节点,不显示
										listNode[5] = "1";
									}
								}
								transitions.add(listNode);
							} else {
								if (nodeId == null) {
									nodeId = tempNodeId;
								}
								/*if(String.valueOf(transInfo).indexOf("subactiveSet") != -1){
									for (Iterator<String> itt = transInfo.iterator(); itt
									.hasNext();) {
										String[] newnodeInfo = String.valueOf(itt.next()).split("\\|");
										if(("subProcessNode").equals(newnodeInfo[0])){
											nodeId = newnodeInfo[1];
											newnodeId = newnodeInfo[1];
										}
									}
								}*/
								String[] listNode = new String[6];
								listNode[0] = tranId;// transtionId转移步骤id
								listNode[1] = tranName;// 转移步骤名称
								if(concurrentFlag.equals("1")){
									if((String.valueOf(transInfo).indexOf(
									"notActiveSet") != -1&&String
									.valueOf(transInfo).indexOf(
									"subProcessNode") != -1)){
										concurrentFlag = "4";
									}else{
										concurrentFlag = "0";
									}
								}
								/**
								 * concurrentFlag 是否是并发模式：
								 * “0”：非并发；“1”：并发;"2"：子流程并且父节点需要选择人员;"3"：非动态选择处理人(包括结束节点)应该隐藏树
								 */
								listNode[2] = concurrentFlag;
								listNode[3] = nodeId;// 节点id
								listNode[4] = nodeName;// 节点名称
								/**
								 * 如果是这种情况就不显示选择人员界面，选择默认人员 0:不显示默认人员 1：默认人员
								 */
								if("buttonType".equals(selectPersonType)){
									//按钮选择人员
									listNode[5] = "0";
								}else{
									if ("1".equals(concurrentFlag)) {
										if ("radio".equals(inputType)) {
											listNode[5] = "0";
										} else {
											listNode[5] = "1";
										}
									} else if ("2".equals(concurrentFlag)) {
										//子流程并且父节点需要选择人员
										listNode[5] = "1";
									} else if ("3".equals(concurrentFlag)) {// //add by
																			// dengzc
										// 2010年3月30日20:33:06//非动态选择处理人应该隐藏
										listNode[5] = "1";
									} else if ("4".equals(concurrentFlag)) {// //add by
																			// dengzc
										// 2010年3月30日20:33:06//非动态选择处理人应该隐藏
										listNode[5] = "1";
									}else {
											//节点设置为返回经办人员时不需要选择人员
											Object[] objs = baseWorkflowManager.getNodesettingByNodeId(workflowService.getNodeIdByTaskInstanceId(taskId));
											if(objs[2] != null && "1".equals(String.valueOf(objs[2]))){
												listNode[5] = "1";
											}else{
												listNode[5] = "0";
											}
									    	//listNode[5] = "1";
										
									}
								}
								transitions.add(listNode);
							}
						}
					}
				}
				
			}

			// }
			// 下一步流程列表排序
			Collections.sort(transitions, new Comparator<String[]>() {
				public int compare(String[] s1, String[] s2) {
					String str1 = String.valueOf(s1[0]);
					String str2 = String.valueOf(s2[0]);
					Long key1;
					if (str1 != null && !"".equals(str1)) {
						key1 = Long.valueOf(str1);
					} else {
						key1 = Long.MAX_VALUE;
					}
					Long key2;
					if (str2 != null && !"".equals(str1)) {
						key2 = Long.valueOf(str2);
					} else {
						key2 = Long.MAX_VALUE;
					}
					return key1.compareTo(key2);
				}
			});
           
			String viewflag = "0";
			for(int i=0;i<transitions.size();i++) {
				String[] objs = transitions.get(i);
				if(objs[5].equals("1")){
					viewflag = "1";
					break;
				}
			}
			if(viewflag.equals("1")){
				ret = dom.createItemsTransitionsAndActorData(STATUS_SUC, null, transitions, taskId);
			}else{
				ret = dom.createItemsTransitionsData(STATUS_SUC, null, transitions);
			}
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取下一步骤数据级异常:"
					+ e.getMessage(), null, null);
		}
		return ret;
	}
	public List<String[]> getActor(String nodeId, String taskId,String transitionId) {
		return baseWorkflowManager.getWorkflowTaskActors(nodeId, taskId,
						transitionId);
		
	}
	/**
	 * @Description: TODO( 查看公文时，获取下一步流程迁移线以及相应处理人)
	 * @author penghj
	 * @date Mar 14, 2012 8:29:20 PM
	 * @param taskId
	 *            任务id
	 * @return
	 */
	public List<String[]> getOneTransitions(String taskId, String instanceId) {

		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();
		String nodeName=""; // 任务节点名称
		List<String[]> transitions = new ArrayList<String[]>();
		try {
			if (taskId != null && !"undefined".equals(taskId)
					&& !"".equals(taskId)) {
				// 获取指定任务实例的下一步可选流向
				List list = workflowService.getNextTransitions(taskId);
				// 处理迁移线信息，并且返回迁移线信息存储对象（包含提交下一步迁移线信息，迁移线id信息，节点id信息）
				TransitionsInfoBean transitionsInfoBean = transitionService
						.doNextTransitionsBySelectActorSetStyle(list);
				if (transitionsInfoBean != null) {
					list = transitionsInfoBean.getTransitionsInfo();
				}
				if (list != null && !list.isEmpty()) {
					 // 按照节点设置信息中的选择人员的设置方式处理迁移线信息
					ContextInstance cxt = null;
					Boolean boolTransDept = false;// 协办处室标志
					Boolean isMoreTransDept = false;// 是否是多个科室
					if (instanceId != null && !"".equals(instanceId)) {
						cxt = adapterBaseWorkflowManager.getWorkflowService()
								.getContextInstance(instanceId);
						if (cxt != null) {
							String isSubProcess = (String) cxt
									.getVariable(WorkflowConst.WORKFLOW_SUB_ISSUBPROCESS);// 是否是子流程
							if (isSubProcess != null && "1".equals(isSubProcess)) {// 是子流程
								Long parentProcessID = (Long) cxt
										.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);
								ProcessInstance parentProcessInstance = adapterBaseWorkflowManager
										.getWorkflowService()
										.getProcessInstanceById(
												parentProcessID.toString());
								cxt = adapterBaseWorkflowManager
										.getWorkflowService().getContextInstance(
												parentProcessID.toString());
								String transDeptFlag = (String) cxt
										.getVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_TRANSITIONDEPT);
								// 验证是否选择了多个协办处室为优先级1
								if (transDeptFlag != null
										&& !"".equals(transDeptFlag) && // 协办处室
										transDeptFlag.indexOf(",") != -1) {// 选择多于一个协办处室
									// 过滤掉类型为协办处室的迁移线
									boolTransDept = true;
									// 判断启动此子流程时是选择了多个处室还是多个科室
									List childInstanceList = adapterBaseWorkflowManager
											.getWorkflowService()
											.getSubProcessInstanceInfos(parentProcessID+"");
									int count = 0;
									for (int i = 0; i < childInstanceList.size(); i++) {
										Object[] instances = (Object[]) childInstanceList
												.get(i);
										Long supNodeId = adapterBaseWorkflowManager
												.getWorkflowConstService()
												.getSuperProcessNodeIdByPid(
														instances[0].toString());
										if (supNodeId != null) {
											String isAllowContinueToDept = adapterBaseWorkflowManager
													.getNodesettingPluginService()
													.getNodesettingPluginValue(
															supNodeId.toString(),
															NodesettingPluginConst.PLUGINS_ISNOTALLOWCONTINUETODEPT);
											if (isAllowContinueToDept != null
													&& "1"
															.equals(isAllowContinueToDept)) {
												count++;
											}
										}
									}
									if (count > 1) {
										isMoreTransDept = true;// 选择了多个科室
									}
								} else {
									if (parentProcessID != null) {// 得到父流程实例id
										List parentInstance = adapterBaseWorkflowManager
												.getWorkflowService()
												.getParentInstanceId(
														parentProcessInstance
																.getId());
										if (parentInstance != null
												&& !parentInstance.isEmpty()) {// 属于第三级子流程
											boolTransDept = true;// 属于第三级子流程时默认不允许继续向下协办,如果设置了允许则覆盖默认的设置
										}
									}
								}
							}
						}
					}
					Map<String, List<Object[]>> transGroupMap = new HashMap<String, List<Object[]>>();// 分组信息
					String noGroupKey = String.valueOf(Long.MAX_VALUE);// 没有设置分组的默认显示在最后
					List<ToaSysmanageDictitem> items = adapterBaseWorkflowManager
							.getDictItemManager().getAllDictItems(
									"40288f2931842af601318451d879002b");// 得到迁移线分组
					final Map<String, Object[]> dictItemMap = new HashMap<String, Object[]>();// 排序号信息
					if (items != null && !items.isEmpty()) {
						for (ToaSysmanageDictitem item : items) {
							Long seq = Long.MAX_VALUE;
							if (item.getDictItemDescspell() != null
									&& !"".equals(item.getDictItemDescspell())) {
								try {
									seq = Long.parseLong(item
											.getDictItemDescspell());
								} catch (Exception e) {
								}
							}
							dictItemMap.put(item.getDictItemCode(), new Object[] {
									seq, item.getDictItemName() });
						}
					}
					String defaultGroupName = "其他";
					final HashMap<String,OATransitionPlugin> transitionPluginMap = new HashMap<String, OATransitionPlugin>();
					Set<Long> transIdList = transitionsInfoBean.getTransitionIds();
					Set<String> pluginNames = new HashSet<String>();
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANSITIONGROUP);		// 迁移线分组
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANSITIONPRI);		// 排序号
					pluginNames.add(TransitionPluginConst.PLUGINS_DEPT);				// 协办处室
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANLINETYPE);		// 迁移线类型 "defaultType":默认|"checkboxType":复选|"radioType":单选
					pluginNames.add(TransitionPluginConst.PLUGINS_SELECTPERSONTYPE);	// 人员选择模式 "treeType":树型|"buttonType":按钮型
					pluginNames.add(TransitionPluginConst.PLUGINS_TRANLINEVALIDATE);	// 办理必填验证
					Map<Long, Map<String, String>> transitionPluginValueMap = adapterBaseWorkflowManager.getTransitionPluginService().getTransitionPluginValueForMapByTransitionIdsAndpluginNames(transIdList, pluginNames);
					if(transitionPluginValueMap == null){
						transitionPluginValueMap = new LinkedHashMap<Long, Map<String,String>>();
					}
					boolean isSetGroup = false;// 是否有迁移线设置了分组
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);
						String transitionId = objs[1].toString();
						Long tsId = Long.valueOf(transitionId);
						Map<String, String> pluginValueMap = transitionPluginValueMap.get(tsId);
						if(pluginValueMap == null){
							pluginValueMap = new HashMap<String, String>(); 
						}
						// "1":协办处室
						String transGroupName = pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANSITIONGROUP);// 所属分组
						String transSeq = pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANSITIONPRI);// 排序号
						String transDept = (pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE) == null
								?"0" :pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE));
						String tranlineType = (pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE) == null
								?"defaultType":pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINETYPE));// 迁移线类型
						String selectPersonType = (pluginValueMap.get(TransitionPluginConst.PLUGINS_SELECTPERSONTYPE) == null
								?"treeType":pluginValueMap.get(TransitionPluginConst.PLUGINS_SELECTPERSONTYPE));// 人员选择方式
						String tranlineValidate = (pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINEVALIDATE) == null
								?"":pluginValueMap.get(TransitionPluginConst.PLUGINS_TRANLINEVALIDATE));// 迁移线类型
						if(!transitionPluginMap.containsKey(transitionId)){
							OATransitionPlugin oaTransitionPlugin = new OATransitionPlugin();
							oaTransitionPlugin.setDept(transDept);// 设置协办处室
							oaTransitionPlugin.setTransitionPri(transSeq);// 设置排序号
							oaTransitionPlugin.setTransitiongroup(transGroupName);// 设置所属分组
							oaTransitionPlugin
									.setSelectPersonType(selectPersonType);// 设置人员选择模式
							oaTransitionPlugin.setTranlineType(tranlineType);// 设置迁移线类型
							oaTransitionPlugin
									.setTranlineValidate(tranlineValidate);// 设置是否办结必填验证
							transitionPluginMap.put(transitionId,
									oaTransitionPlugin);
						}
						// 判断父流程是否选择了多个协办处室,如果选择了多个协办处室,那么在子流程中协办处室的线不能显示
						if (boolTransDept) {
							// 过滤掉类型为协办处室的迁移线
							if ("1".equals(transDept)) {// 属于协办处室的迁移线
								if (isMoreTransDept) {// 选择了多个科室不允许继续协办
									continue;
								}
								// 若是科室子流程,则可以继续出现
								Set<String> transSet = (Set<String>) objs[3];
								boolean isContinue = false;
								;
								for (String tran : transSet) {
									String[] info = tran.split("\\|");
									if (info[0].equals("subProcessNode")) {
										String nodeId = info[1];
										TwfBaseNodesetting setting = adapterBaseWorkflowManager
												.getWorkflowService()
												.getNodesettingByNodeId(nodeId);
										String isAllowContinueToDept = setting
												.getPlugin(NodesettingPluginConst.PLUGINS_ISNOTALLOWCONTINUETODEPT);
										if (isAllowContinueToDept != null
												&& "1"
														.equals(isAllowContinueToDept)) {
											// 科室子流程
											isContinue = true;
											if (cxt != null) {
												String flag = (String) cxt
														.getVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_ISALLOWCONTINUETODEPT);
												if (flag != null
														&& "1".equals(flag)) {// 说明到了第二次科室子流程
													isContinue = false;
												}
											}
										}
										break;
									}
								}
								if (!isContinue) {
									continue;
								}
							}
						}
						// --------- End -------
						if (transGroupName != null
								&& !"".equals(transGroupName)) {// 设置了分组
							isSetGroup = true;
							if (!transGroupMap.containsKey(transGroupName)) {
								List<Object[]> temp = new ArrayList<Object[]>();
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号 --- 4
								objs = ObjectUtils.addObjectToArray(objs,
										dictItemMap.get(transGroupName));// 分组名称
								// ---
								// 5
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志 -- 6
								temp.add(objs);
								transGroupMap.put(transGroupName, temp);
							} else {
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号
								objs = ObjectUtils.addObjectToArray(objs,
										dictItemMap.get(transGroupName));// [分组号,分组名称]
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志
								transGroupMap.get(transGroupName).add(objs);
							}
						} else {// 未设置分组
							if (!transGroupMap.containsKey(noGroupKey)) {
								List<Object[]> temp = new ArrayList<Object[]>();
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号
								objs = ObjectUtils.addObjectToArray(objs,
										defaultGroupName);// 分组名称
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志
								temp.add(objs);
								transGroupMap.put(noGroupKey, temp);
							} else {
								objs = ObjectUtils.addObjectToArray(objs,
										transSeq);// 数组中增加排序号
								objs = ObjectUtils.addObjectToArray(objs,
										defaultGroupName);// 分组名称
								objs = ObjectUtils.addObjectToArray(objs,
										transDept);// 协办处室标志
								transGroupMap.get(noGroupKey).add(objs);
							}
						}
					}

					List<String> transGroupList = new ArrayList<String>(
							transGroupMap.keySet());// 转化成List排序
					Collections.sort(transGroupList, new Comparator<String>() {
						public int compare(String o1, String o2) {
							Object[] objs1 = dictItemMap.get(o1);
							Object[] objs2 = dictItemMap.get(o2);
							Long s1, s2;
							if (objs1 == null) {
								s1 = Long.MAX_VALUE;
							} else {
								s1 = (Long) objs1[0];
							}
							if (objs2 == null) {
								s2 = Long.MAX_VALUE;
							} else {
								s2 = (Long) objs2[0];
							}
							return s1.compareTo(s2);
						}

					});

					String isChecked = "";
					if (list.size() == 1) {
						isChecked = "checked";
					}

					// transGroupIdAndName:存放分组信息，格式：分组(0)ID1|Name1 (1)ID2|Name2...
					List<String> transGroupIdAndNameList = new LinkedList<String>();
					for (String key : transGroupList) {
						//过滤迁移线分组信息【过滤依据是判断该迁移线所属分组是否为意见征询】	严建 2012-06-23 15:27
						/*if (key.equals(FormGridDataHelper.getColorSetProperties()
								.getProperty("yjzxGroupId"))) {// 该分组属于意见征询分组
							// 是否允许显示该意见征询分组【已经征询过的文不能再进行意见征询】
							String bid = (adapterBaseWorkflowManager
									.getWorkflowService().getProcessInstanceById(
											instanceId).getBusinessId());// 业务id
							if (bid != null) {
								Tjbpmbusiness tjbpmbusiness = adapterBaseWorkflowManager
										.getJbpmbusinessmanager().findByBusinessId(
												bid);
								if (tjbpmbusiness != null
										&& "0".equals(tjbpmbusiness
												.getBusinessType())) {// 已经进行征询的文不再进行征询
									continue;
								}
							}
						}*/
						list = transGroupMap.get(key);
						// 增加排序功能 按迁移线上定义的扩展属性排序 扩展属性名称：plugins_transitionpri 邓志城
						// 2011年7月20日16:01:33
						final Map<String, Object> map = new HashMap<String, Object>();
						List<Object[]> listWithPlugin = new ArrayList<Object[]>();
						List<Object[]> listWithNotPlugin = new ArrayList<Object[]>();
						Object groupName = null;
						for (int i = 0; i < list.size(); i++) {
							Object[] objs = (Object[]) list.get(i);
							String transitionId = objs[1].toString();
							Object value = objs[4];// 排序号
							if (groupName == null) {
								if (objs[5] instanceof Object[]) {
									groupName = ((Object[]) objs[5])[1];// 分组名称
								} else {
									if (isSetGroup) {
										groupName = defaultGroupName;
									}
								}
							}
							if (value != null && !"".equals(value)) {
								map.put(transitionId, value);
								listWithPlugin.add(objs);
							} else {
								listWithNotPlugin.add(objs);
							}
						}
						Collections.sort(listWithPlugin,
								new Comparator<Object[]>() {
									public int compare(Object[] o1, Object[] o2) {
										String trans1 = String.valueOf(o1[1]);
										String trans2 = String.valueOf(o2[1]);
										Object obj1 = map.get(trans1);
										Object obj2 = map.get(trans2);
										try {
											Long l1 = Long.parseLong(obj1
													.toString());
											Long l2 = Long.parseLong(obj2
													.toString());
											return l1.compareTo(l2);
										} catch (Exception e) {
											logger.error("迁移线排序解析出错", e);
											return 0;
										}
									}

								});
						map.clear();
						Collections.sort(listWithNotPlugin,
								new Comparator<Object[]>() {
									public int compare(Object[] o1, Object[] o2) {
										String transName1 = (String) o1[0];
										String transName2 = (String) o2[0];
										return transName1.compareTo(transName2);
									}

								});
						list.clear();
						list.addAll(listWithPlugin);
						list.addAll(listWithNotPlugin);
						// ----------------------- End ------------------------

						for (Iterator i = list.iterator(); i.hasNext();) {
							Object[] trans = (Object[]) i.next();
							Set<String> transInfo = (Set<String>) trans[3];
							String concurrentFlag = (String) trans[2];// 并发标示：“0”：非并发，“1”：并发
							String tranId = (String) ConvertUtils.convert(trans[1],
									String.class);// 迁移线id
							String tranName = (String) trans[0];// 迁移线名称
							String inputType = "radio";
							String nodeId = null;
							OATransitionPlugin oaTransitionPlugin = transitionPluginMap.get(tranId);	// 
							String tranlineType = oaTransitionPlugin.getTranlineType();					//迁移线类型
							String selectPersonType = oaTransitionPlugin.getSelectPersonType();			//人员选择模式
							String tranlineValidate = oaTransitionPlugin.getTranlineValidate();			//办理必填验证
							if ("0".equals(concurrentFlag)) {
								// 如果是子流程并且需要选择子流程第一个节点人员和子流程下一结点（父节点）人员
								if (String.valueOf(transInfo).indexOf("activeSet") != String
										.valueOf(transInfo)
										.lastIndexOf("activeSet")) {
									concurrentFlag = "2";
								}
								// 非动态选择处理人(包括结束节点)应该隐藏树
								if ((String.valueOf(transInfo).indexOf(
										"notActiveSet") != -1
										|| String.valueOf(transInfo).indexOf(
												"endNode") != -1
										|| String.valueOf(transInfo).indexOf(
												"decideNode") != -1 || String
										.valueOf(transInfo).indexOf(
												"subProcessNode") != -1)
										&& transInfo.size() == 1) {
									concurrentFlag = "3";// add by dengzc
									// 2010年3月30日20:33:06
								}
							} else {
								inputType = "checkbox";
								if (tranlineType.equals("radioType")) {// 迁移线设置为单选方式
									inputType = "radio";
								}
							}
							if (list.size() == 1) {// 只存在一根迁移线的时候,迁移线为单选
								inputType = "radio";
							}
							String[] nodeInfo = null;
							String tempNodeId = null;
							for (Iterator<String> it = transInfo.iterator(); it
									.hasNext();) {
								nodeInfo = String.valueOf(it.next()).split("\\|");
								String actorFlag = (String) nodeInfo[0];// 是否需要选择人员activeSet:需要重新选择人员
								nodeName = (String) nodeInfo[2];// 迁移线对应的节点名称
								if ("activeSet".equals(actorFlag)
										|| "subactiveSet".equals(actorFlag)
										|| "supactiveSet".equals(actorFlag)) {// 允许动态设置处理人
									nodeId = (String) nodeInfo[1];// 节点id
								} else {
									if (actorFlag.equals("notActiveSet")) {
										tempNodeId = (String) nodeInfo[1];// 节点id
									}
								}
							}
							String newnodeId = null;
							// 结束节点并且不需要选择人员
							if (String.valueOf(transInfo).indexOf("endNode") == 1
									&& (String.valueOf(transInfo).indexOf(
											"activeSet") == -1
											&& String.valueOf(transInfo).indexOf(
													"subactiveSet") == -1 && String
											.valueOf(transInfo).indexOf(
													"supactiveSet") == -1)) {
								if (nodeId == null) {
									nodeId = tempNodeId;
								}
								
								String[] listNode = new String[6];
								listNode[0] = tranId;// transtionId转移步骤id
								listNode[1] = tranName;// 转移步骤名称
								/**
								 * concurrentFlag 是否是并发模式：
								 * “0”：非并发；“1”：并发;"2"：子流程并且父节点需要选择人员;"3"：非动态选择处理人(包括结束节点)应该隐藏树
								 */
								listNode[2] = concurrentFlag;
								listNode[3] = nodeId;// 节点id
								listNode[4] = nodeName;// 节点名称
								/**
								 * 如果是这种情况就不显示选择人员界面，选择默认人员 0:不显示默认人员 1：默认人员
								 */
								if("buttonType".equals(selectPersonType)){
									//按钮选择人员
									listNode[5] = "0";
								}else{
									if ("1".equals(concurrentFlag)) {
										if ("radio".equals(inputType)) {
											listNode[5] = "1";
										} else {
											listNode[5] = "1";
										}
									} else if ("2".equals(concurrentFlag)) {
										//子流程并且父节点需要选择人员
										listNode[5] = "0";
									} else if ("3".equals(concurrentFlag)) {// //add by
																			// dengzc
										// 2010年3月30日20:33:06//非动态选择处理人应该隐藏
										listNode[5] = "1";
									} else {
										//结束节点,不显示
										listNode[5] = "1";
									}
								}
								transitions.add(listNode);
							} else {
								if (nodeId == null) {
									nodeId = tempNodeId;
								}
								//时间：2014年9月4日16:58:53  BUG：0000066377
							/*	if(String.valueOf(transInfo).indexOf("subactiveSet") != -1){
									for (Iterator<String> itt = transInfo.iterator(); itt
									.hasNext();) {
										String[] newnodeInfo = String.valueOf(itt.next()).split("\\|");
										if(("subProcessNode").equals(newnodeInfo[0])){
											nodeId = newnodeInfo[1];
											newnodeId = newnodeInfo[1];
										}
									}
								}*/
								String[] listNode = new String[6];
								listNode[0] = tranId;// transtionId转移步骤id
								listNode[1] = tranName;// 转移步骤名称
								/**
								 * concurrentFlag 是否是并发模式：
								 * “0”：非并发；“1”：并发;"2"：子流程并且父节点需要选择人员;"3"：非动态选择处理人(包括结束节点)应该隐藏树
								 */
								listNode[2] = concurrentFlag;
								listNode[3] = nodeId;// 节点id
								listNode[4] = nodeName;// 节点名称
								/**
								 * 如果是这种情况就不显示选择人员界面，选择默认人员 0:不显示默认人员 1：默认人员
								 */
								if("buttonType".equals(selectPersonType)){
									//按钮选择人员
									listNode[5] = "0";
								}else{
									if ("1".equals(concurrentFlag)) {
										if ("radio".equals(inputType)) {
											for (Iterator<String> it = transInfo.iterator(); it
											.hasNext();) {
											nodeInfo = String.valueOf(it.next()).split("\\|");
											String actorFlag = (String) nodeInfo[0];// 是否需要选择人员activeSet:需要重新选择人员
												if ("activeSet".equals(actorFlag)
														|| "subactiveSet".equals(actorFlag)
														|| "supactiveSet".equals(actorFlag)) {// 允许动态设置处理人
													listNode[5] = "0";
												}else{
													listNode[5] = "0";
												}
											}
										} else {
											listNode[5] = "1";
										}
									} else if ("2".equals(concurrentFlag)) {
										//子流程并且父节点需要选择人员
										listNode[5] = "1";
									} else if ("3".equals(concurrentFlag)) {// //add by
																			// dengzc
										// 2010年3月30日20:33:06//非动态选择处理人应该隐藏
										listNode[5] = "1";
									} else {
										//非结束节点,显示人员
								    	//下一步骤为“返回”节点为人员不需选择， tanfei 2014-5-21 9:30
								    	if(tranName.contains("返回")){
								    	    listNode[5] = "1";
								    	}else{
								    	    listNode[5] = "0";
								    	}
								    	//listNode[5] = "1";
									
								}
								}
								transitions.add(listNode);
							}
						}
					}
				}
				
			}

			// }
			// 下一步流程列表排序
			Collections.sort(transitions, new Comparator<String[]>() {
				public int compare(String[] s1, String[] s2) {
					String str1 = String.valueOf(s1[0]);
					String str2 = String.valueOf(s2[0]);
					Long key1;
					if (str1 != null && !"".equals(str1)) {
						key1 = Long.valueOf(str1);
					} else {
						key1 = Long.MAX_VALUE;
					}
					Long key2;
					if (str2 != null && !"".equals(str1)) {
						key2 = Long.valueOf(str2);
					} else {
						key2 = Long.MAX_VALUE;
					}
					return key1.compareTo(key2);
				}
			});
            
		 } catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取下一步骤数据级异常:"
					+ e.getMessage(), null, null);
		}
		return transitions;
	}
	/**
	 * @Description: TODO(获取指定迁移线的下一步骤处理人)
	 * @author penghj
	 * @date Mar 14, 2012 8:30:53 PM
	 * @param nodeId
	 *            节点ID
	 * @param taskId
	 *            任务ID
	 * @param transitionId
	 *            迁移线ID
	 * @return 处理人
	 */
	public String getWorkflowTaskActors(String nodeId, String taskId,
			String transitionId,String userId) {
		System.out.println(nodeId +"," + taskId +"," + transitionId);
		userService.setUserId(userId);
		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();
		List<String[]> taskActors = new ArrayList<String[]>();
		List<String[]> newActors=new ArrayList<String[]>();
		try {
			String multSelect="1";
			if (nodeId != null && !"".equals(nodeId) && !"null".equals(nodeId)) {
				// 通过任务节点ID得到任务节点默认设置人员信息
				// 判断是否返回经办人的情况
				// String ns = workflowService.getNodeIdByTaskInstanceId(taskId)
				// ;
				/*
				BaseManager bsManager = new BaseManager();
				taskActors = bsManager.getWorkflowTaskActors(nodeId, taskId,
				transitionId);*/
				//nodeId = "6876436";
				taskActors = baseWorkflowManager.getWorkflowTaskActors(nodeId, taskId,
						transitionId);
				System.out.println(taskActors.size());
				if (taskActors != null && !taskActors.isEmpty()) {
					for (String[] info : taskActors) {
						String actorUserId = info[0];
						if(actorUserId!=null&&!"".equals(actorUserId)){
							User user = userService.getUserInfoByUserId(actorUserId);
							info[2] = user.getOrgId();
						}
					}
				}
				
				Object[] setting = baseWorkflowManager.getNodesettingByNodeId(nodeId);
				String maxTaskActors = (String) setting[0];
				
				String inputType="checkbox";
				if (maxTaskActors != null && !maxTaskActors.equals("")
						&& maxTaskActors.equals("1")&&!"null".equals(maxTaskActors)) {
					inputType = "radio";
				}
				List<Object[]> orgLst = new ArrayList<Object[]>();
				List<Object[]> users = new ArrayList<Object[]>();
				final Map<String, Organization> orgMap = new HashMap<String, Organization>();
				String isOrganizationSelect = "0";// 是否是选择机构
				Map<String, List<Object[]>> orgidusersmap = new HashMap<String, List<Object[]>>();
				HashMap<String,String> userIdMappostName = new HashMap<String, String>();
				if (taskActors != null && !taskActors.isEmpty()) {
					Map<String, List<Object[]>> userList = new HashMap<String, List<Object[]>>();
					for (String[] info : taskActors) {
						String actorOrgId = info[2];
						if (!orgMap.containsKey(actorOrgId)) {
							orgMap.put(actorOrgId, null);
						}
					}
					ArrayList<String> orgIds = new ArrayList<String>(orgMap.keySet());
					/*List<TUumsBaseOrg> list_org = adapterBaseWorkflowManager.getOrgManager().getAllOrgInfos();*/
					for (String orgId : orgIds) {
						Organization org = adapterBaseWorkflowManager
								.getUserService().getDepartmentByOrgId(orgId);
						orgMap.put(orgId, org);
					}
					
					List<String> userIdLst = new ArrayList<String>(taskActors.size());
					for (String[] info : taskActors) {
						userIdLst.add(info[0]);
					}
					Map<String, List<TUumsBasePost>>  postUsersMap =  userService.getPostUsersByUserIds(userIdLst);
					
					for (String[] info : taskActors) {
						String actorOrgId = info[2];
						String postName = "";
						if (!userList.containsKey(actorOrgId) && (info[0] != null)) {
							List<Object[]> uList = new ArrayList<Object[]>();

//							List<TUumsBasePost> userPostSet = userService
//									.getPostInfoByUserId(info[0].toString());//找出该人员岗位信息
							List<TUumsBasePost> userPostSet = postUsersMap
									.get(info[0].toString());//找出该人员岗位信息
							StringBuffer pn = new StringBuffer("( ");
							if (userPostSet != null && userPostSet.size() > 0) {
								for (TUumsBasePost up : userPostSet) {
									if (up != null) {
										pn.append(up.getPostName()).append(" ");
									}
								}
								pn.append(")");
								postName = pn.toString();
							}
							uList.add(new Object[] { info[0], info[1], postName });
							userList.put(actorOrgId, uList);
						} else {
							if (info[0] != null) {
//								List<TUumsBasePost> userPostSet = userService
//										.getPostInfoByUserId(info[0].toString());//找出该人员岗位信息
								List<TUumsBasePost> userPostSet = postUsersMap
										.get(info[0].toString());//找出该人员岗位信息
								StringBuffer pn = new StringBuffer("( ");
								if (userPostSet != null && userPostSet.size() > 0) {
									for (TUumsBasePost up : userPostSet) {
										if (up != null) {
											pn.append(up.getPostName()).append(" ");
										}
									}
									pn.append(")");
									postName = pn.toString();
								}
								if (info[1] != null && !info[1].equals("")) {
									userList.get(actorOrgId)
											.add(new Object[] { info[0], info[1],
															postName });
								} else {
									userList.get(actorOrgId).add(
											new Object[] { info[0], info[1], "" });
								}
							}
						}
						if(info[0] != null){
							if(!userIdMappostName.containsKey(info[0].toString())){
								userIdMappostName.put(info[0].toString(), postName);
							}
						}
						if (info[0] != null) {
							if (info[0].startsWith("@")) {
								isOrganizationSelect = "1";
							}
						}
					}
					Collection<Organization> orgs = orgMap.values();
					for (Organization org : orgs) {
						if (org != null) {
							String parentId = org.getOrgParentId();
							if (parentId == null || "".equals(parentId)
									|| !userList.containsKey(parentId)) {
								parentId = "0";
							}
							orgLst.add(new Object[] { org.getOrgId(), parentId,
									org.getOrgName(), userList.get(org.getOrgId()) });
						}
					}
					for (String[] info : taskActors) {
						Organization org = orgMap.get(info[2]);
						if (org != null && info[0] != null) {
							List<Object[]> userLists = null;
							String postName = userIdMappostName.get(info[0].toString());
							if (!orgidusersmap.containsKey(org.getOrgId())) {
								userLists = new LinkedList<Object[]>();
								userLists.add(new Object[] { info[0], info[1],
										org.getOrgName(), postName });
								orgidusersmap.put(org.getOrgId(), userLists);
							} else {
								userLists = orgidusersmap.get(org.getOrgId());
								userLists.add(new Object[] { info[0], info[1],
										org.getOrgName(), postName });
								orgidusersmap.put(org.getOrgId(), userLists);
							}
							// users.add(new
							// Object[]{info[0],info[1],org.getOrgName()});
						}
					}
				}
				// 对机构数据按排序号排序
				Collections.sort(orgLst, new Comparator<Object[]>() {
					public int compare(Object[] o1, Object[] o2) {
						Organization org1 = orgMap.get(o1[0]);
						Organization org2 = orgMap.get(o2[0]);
						Long key1;
						if (org1 != null && org1.getOrgSequence() != null) {
							key1 = Long.valueOf(org1.getOrgSequence());
						} else {
							key1 = Long.MAX_VALUE;
						}
						Long key2;
						if (org2 != null && org2.getOrgSequence() != null) {
							key2 = Long.valueOf(org2.getOrgSequence());
						} else {
							key2 = Long.MAX_VALUE;
						}
						return key1.compareTo(key2);
					}
				});
				for (int i = 0; i < orgLst.size(); i++) {
					List<Object[]> tmpobjs = orgidusersmap.get(orgLst.get(i)[0]);
					if(tmpobjs != null){
						users.addAll(orgidusersmap.get(orgLst.get(i)[0]));
					}
				}
				
				if("radio".equals(inputType)){
					multSelect="0";
				}
				if(users!=null&&users.size()>0){
					Iterator it=users.iterator();
					while(it.hasNext()){
						Object obj=it.next();
						Object[] arr=(Object[])obj;
						String[] str = new String[3];
						str[0]=arr[0].toString();
						str[1]=arr[1].toString();
						str[2]=arr[2].toString();
						newActors.add(str);
					}
				}					
			}

			//tanfei 2014-05-21
			ret = dom.createItemsTaskActorsData(STATUS_SUC, null,
					newActors,multSelect);
		} catch (ServiceException e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取下一步骤处理人数据级异常:"
					+ e.getMessage(), null, null);
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取人员信息" });
		}
		System.out.println(ret);
		return ret;
	}

	/**
	 * @Description: TODO(获取流程办理常用意见)
	 * @author penghj
	 * @date Mar 14, 2012 8:28:10 PM
	 * @param userId
	 * @return
	 */
	public String getUsedSuggestion(String userId) {
		String ret = "";
		List<String> suggestions = new ArrayList<String>();
		Dom4jUtil dom = new Dom4jUtil();
		try {
			// 获取床用意见的List
			ideaLst = approvalSuggestionService
					.getAppSuggestionListByUserId(userId);
			String os = "100";
			for (int j = 0; j < ideaLst.size(); j++) {
				ToaApprovalSuggestion suggestionBean = (ToaApprovalSuggestion) ideaLst
						.get(j);
				String suggestion = suggestionBean.getSuggestionContent();
				suggestion = suggestion.replaceAll("\r", "");// 处理审批意见有回车的情况
				suggestion = suggestion.replaceAll("\n", " ");// 处理审批意见有换行的情况
				os = suggestion;

				String[] FF_String = new String[] { "\'", "\"", "<", ">",
						"\\\\" };// 特殊字符
				String[] NFF_String = new String[] { "’", "”", "＜", "＞", "＼" };// 替换字符
				boolean isFlag = false;
				for (int i = 0; i < FF_String.length; i++) {// 如果存在特殊字符
					if (suggestion.indexOf(FF_String[i]) != -1) {
						suggestion = suggestion.replaceAll(FF_String[i],
								NFF_String[i]);
						isFlag = true;
					}
				}
				if (isFlag) {
					suggestionBean.setSuggestionContent(suggestion);// 如果存在特殊字符，就替换
				}
				suggestions.add(suggestion);
			}
			// 组装常用意见List
			ret = dom.createItemsUsedSuggestionData(STATUS_SUC, null,
					suggestions);
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取常用办理意见数据级异常:"
					+ e.getMessage(), null, null);
		}
		return ret;
	}

	/**
	 * 通过签发 提交流程
	 * 同submitWorkflowNext方法，只是意见内容增加了标识
	 * @param sessionId
	 * @param taskId
	 * @param userId
	 * @param suggestion
	 * @param transitionName
	 * @param taskActors
	 * @param nodeId
	 * @return
	 */
	public String submitWorkflowNextFromQianfa(String sessionId, String taskId,
			String userId, String suggestion, String transitionName,
			String taskActors, String nodeId) {
		return submitWorkflowNext(sessionId,taskId,userId,"<签发>"+suggestion,transitionName,taskActors,nodeId);
	}	
	
	/**
	 * @Description: TODO( 提交流程 )
	 * @author penghj
	 * @date Mar 14, 2012 8:32:26 PM
	 * @param sessionId
	 * @param taskId
	 *            任务ID
	 * @param userId
	 *            登陆用户ID
	 * @param suggestion
	 *            提交意见
	 * @param transitionName
	 *            流向名称
	 * @param taskActors
	 *            处理人id
	 * @param nodeId
	 *            节点Id
	 * @return
	 */
	public String submitWorkflowNext(String sessionId, String taskId,
			String userId, String suggestion, String transitionName,
			String taskActors, String nodeId) {
		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();

		// 验证用户
		// if(AuthenticationHandler.getUserInfo(sessionId)==null){
		// return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null,
		// null);
		// }

		String returnNodeId = "";

		try {
			String strNodeInfo = workflowService.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			String formId = "0";
			String businessId = null;
			if ("form".equals(arrNodeInfo[0])) {
				businessId = arrNodeInfo[2]; // 业务id
				formId = arrNodeInfo[3]; // 表单id
			}

			// 签批意见
			JSONObject json = new JSONObject();
			json.put("suggestion", suggestion);
			json.put("CAInfo", "");

			
			String transitionId = null;
			//List ss = workflowService.getNextTransitions(taskId);
	    	List<Object[]> obj =new ArrayList();
	    	obj=workflowService.getNextTransitions(taskId);
				//getTransitions(taskId);
			for(Object[] transition:obj){
			    //Object ob=transition[0];
			    if(transitionName.equals(transition[0].toString())){
			    	transitionId = String.valueOf(transition[1]).toString();
			    }
			}
			//用于移动端传迁移线id（1、传入子流程人员获取不正确的情况）
			saveTransitionInfoSession(transitionId);
			// 处理人
			if (taskActors != null && !"".equals(taskActors)
					&& !"null".equals(taskActors)) {
				// 拼装处理人的数据格式： actor|nodId
				String[] actors = taskActors.split(",");
				String[] realActors = new String[actors.length];
				for (int i = 0; i < actors.length; i++) {
					String realActor = actors[i] + "|" + nodeId;
					realActors[i] = realActor;
				}
				userService.setUserId(userId);
				// 提交流程
				workflowService.goToNextTransition(taskId, transitionName,
						returnNodeId, "1", formId, businessId, json.toString(),
						userId, realActors);
			} else {
				userService.setUserId(userId);
				// 获取上下文实例
				ContextInstance contextInstance = workflowService
						.getTaskInstanceById(taskId).getProcessInstance()
						.getContextInstance();
				contextInstance
						.setVariable(
								CustomWorkflowConst.WORKFLOW_SUPERPROCESS_TRANSITIONDEPT,
								"0");// 存储协办处室标志
				saveTransitionInfoSession(transitionId);
				workflowService.goToNextTransition(taskId, transitionName,
						returnNodeId, "1", formId, businessId, json.toString(),
						userId, null);
			}
			/**
			 * 针对手机办理，某一个节点取回，又提交，一个节点产生多次办理意见的情况，就只保留最新的办理记录
			 */
			// 更新流程办理意见及日期
			List<TwfInfoApproveinfo> wfApproveInfo = workflowService
					.getDataByHql(
							"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
							new Object[] { new Long(taskId) });

			if (wfApproveInfo != null && wfApproveInfo.size() > 0) {
				TwfInfoApproveinfo approve = wfApproveInfo.get(0);
				approve.setAiDate(new Date());
				approve.setAiContent(suggestion);
				workflowService.saveApproveInfo(approve);// 更新意见和内容
			}

			ret = dom.createItemResponseData(STATUS_SUC, null, "string",
					"success");
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().indexOf("task instance")!=-1&&e.getMessage().indexOf("is already ended")!=-1){
				ret = dom.createItemResponseData(STATUS_FAIL, "此任务已被处理!"
						, null, null);
			}else if(e.getMessage().indexOf("task instance")!=-1&&e.getMessage().indexOf("is suspended")!=-1){
				ret = dom.createItemResponseData(STATUS_FAIL, "此任务已被废除或挂起!"
						, null, null);
			}else{
				ret = dom.createItemResponseData(STATUS_FAIL, "提交流程数据级异常:"
						+ e.getMessage(), null, null);
			}
		}
		return ret;
	}

	/**
	 * 将迁移线信息保存在session中
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 2:35:24 PM
	 */
	protected void saveTransitionInfoSession(String transitionId) {
		MessageContext mc = MessageContext.getCurrentContext();
		if (transitionId != null && transitionId.length() > 0) {
			String[] transitionIds = transitionId.split(",");
			List<String[]> stackTransitionId = new ArrayList<String[]>();
			for (String tid : transitionIds) {
				Long subNodeId = adapterBaseWorkflowManager
						.getWorkflowService().getNodeIdByTransitionId(tid);
				TwfBaseNodesetting subNodeSetting = adapterBaseWorkflowManager
						.getWorkflowService().getNodesettingByNodeId(
								subNodeId.toString());
				if (subNodeSetting.getNsSubprocessSetting() != null
						&& !subNodeSetting.getNsSubprocessSetting().equals("")) {
					String processName = subNodeSetting
							.getNsSubprocessSetting().split("\\|")[0];
					stackTransitionId.add(new String[] { processName, tid });
				}
			}
			//mc.getCurrentContext().getSession().set("transitionId", stackTransitionId);
		
			mc.setProperty("transitionId", stackTransitionId);
			//mc.getCurrentContext().getSession().set("transitionId", stackTransitionId);
			//cxt.put("transitionId", stackTransitionId);
		}
	}
	/**
	 * 取回任务
	 * 
	 * @param taskId
	 *            任务实例id
	 * @param userId
	 *            需要取回任务的用户id
	 * @return 服务调用成功时返回数据格式如下： <?xml version="1.0" encoding="UTF-8"?>
	 *         <service-response> <status>1</status> <fail-reason /> <data>
	 *         <item type="string" value="操作返回编码/><!--编码含义：0:该流程实例已经结束，无法取回，1:该任务的后继任务已被签收处理，不允许取回,2:取回成功 >
	 *         </data> </service-response>  服务调用失败时返回数据格式如下： <?xml
	 *         version="1.0" encoding="UTF-8"?> <service-response> <status>0</status>
	 *         <fail-reason>异常描述</fail-reason> <data /> </service-response>
	 */
	public String fetchTask(String taskId, String userId) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			userService.setUserId(userId);
			String str = workflowService.fetchTask(taskId);
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", str);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "取回任务时发生异常:"
					+ ex.getMessage(), null, null);
		} finally {
			userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
		}
		return ret;
	}

	public String backLast(String taskId, String userId, String suggestion) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			String isbacfirst=WebServiceAddressUtil.getBackModule();
			if("1".equals(isbacfirst)){
				return backToFirst(taskId, userId, suggestion);
			}else{
				try {

					userService.setUserId(userId);
					// 退文意见
					JSONObject json = new JSONObject();
					json.put("suggestion", suggestion + "     ");
					json.put("CAInfo", "");

					baseWorkflowManager.handleWorkflowNextStep(taskId,
							WorkflowConst.WORKFLOW_TRANSITION_LASTPRE, userId, "0", "",
							"", json.toString(), "", null);
					ret = dom.createItemResponseData(STATUS_SUC, null, "string",
							"success");
				} catch (Exception ex) {
					ret = dom.createItemResponseData(STATUS_FAIL, "退回任务时发生异常:"
							+ ex.getMessage(), null, null);
				} finally {
					userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	* @Title: backToFirst
	* @Description: 退回到第一个流程节点
	* @detailed description：
	* @param：@param taskId
	* @param：@param userId
	* @param：@param suggestion
	* @param：@return    
	* @return：String
	* @author：申仪玲  
	* @time：2013-3-7 上午10:51:05   
	* @throws
	*/
	public String backToFirst(String taskId, String userId, String suggestion){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			
			userService.setUserId(userId);
			
			/**
			 * 获取第一步节点Id
			 * @author 钟伟
			 */
			String returnNodeId = "";			
			TaskInstance ti = workflowService.getTaskInstanceById(taskId);
			if(ti == null)
			{
				Exception e = new Exception("任务有误，请检查任务id。");
				throw e;
			}
			ProcessInstance pi = ti.getProcessInstance();
			ProcessDefinition pd = pi.getProcessDefinition();
			if (pd != null)
			{
				List transList = pd.getStartState().getLeavingTransitions();
				if ((transList != null) && (transList.size() != 0))
				{
					Transition transition = (Transition) transList.get(0);
					Node secondNode = transition.getTo();
					returnNodeId = Long.valueOf(secondNode.getId()).toString();
				}
			}
			
//			String workflowName = workflowService.getTaskInstanceById(taskId).getProcessInstance().getName();
//			returnNodeId = workflowService.getFirstNodeId(workflowName).toString();

			// 退文意见
			JSONObject json = new JSONObject();
			json.put("suggestion", suggestion+ "     ");
			json.put("CAInfo", "");
			
			
			
			//处理并行提交问题  2012-11-20
			boolean isContinue = false;
			while(!isContinue){
				try {
					baseWorkflowManager.handleWorkflowNextStep(taskId,WorkflowConst.WORKFLOW_TRANSITION_HUITUI, 
							returnNodeId, "0", "", "", json.toString(), "", null);     
					isContinue = true;
				} catch (Exception e) {
					if(e.getMessage().indexOf("Row was updated or deleted by another transaction ") != -1){
						logger.warn("并行提交，系统出现异常，需要将本次数据提交重新执行一遍！");
					}else{
						isContinue = true;
						throw e;
					}
					// TODO: handle exception
				}
			}
			//添加日志信息
			//添加日志信息
			String logInfo = userService.getCurrentUser().getUserName() + "退回《" + 
				workflowService.getTaskInstanceById(taskId).getProcessInstance().getBusinessName() + "》,任务ID为：" + taskId + "(移动端处理)";
			String ip = ""; 
			myLogManager.addLog(logInfo, ip);
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "退回任务时发生异常:" +ex.getMessage(), null, null);
		} finally {
			userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
		}
		return ret;
		
	}

}
