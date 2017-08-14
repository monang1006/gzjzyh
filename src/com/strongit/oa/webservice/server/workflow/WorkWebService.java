package com.strongit.oa.webservice.server.workflow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import net.sf.json.JSONObject;

import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.ipadoa.util.WorkflowForIpadService;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.Office2PDF;
import com.strongit.oa.work.WorkManager;
import com.strongit.workflow.bo.TwfInfoProcessType;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.po.TaskInstanceBean;
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
public class WorkWebService {

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
	 
	 //SendDocUploadManager sendDocUploadManager;   //获取上一步处理人接口
	
	IApprovalSuggestionService approvalSuggestionService; // 审批意见接口
	
	BaseWorkflowManager baseWorkflowManager;

	/**
	 * 构造方法获取manager对象
	 */
	public WorkWebService() {
		workflowName = "IPPOA";
		manager = (WorkManager) ServiceLocator.getService("workManager");
		workflowForIpadService = (WorkflowForIpadService) ServiceLocator.getService("workflowForIpadService");
		approvalSuggestionService = (IApprovalSuggestionService) ServiceLocator.getService("approvalSuggestionService");
		attachmentManager = (IAttachmentService) ServiceLocator.getService("attachmentManager");

		workflowService = (IWorkflowService) ServiceLocator.getService("workflowService");
		baseManager = (SendDocManager) ServiceLocator.getService("sendDocManager");
		attachService = (IWorkflowAttachService) ServiceLocator.getService("workflowAttachManager");
		userService = (IUserService) ServiceLocator.getService("userService");
		
//		sendDocUploadManager = (SendDocUploadManager)ServiceLocator.getService("sendDocUploadManager");
		baseWorkflowManager = (BaseWorkflowManager)ServiceLocator.getService("baseWorkflowManager");
		processDefinitionService = (IProcessDefinitionService)ServiceLocator.getService("processDefinitionService");
		logger.info("工作流服务类初始化完毕.。。。");
	}

	private Date parseDate(String dateStr) throws ParseException, SystemException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

    /**
    @Description: TODO(得到已办公文) 
    @author penghj    
    @date Mar 14, 2012 8:17:20 PM 
    @param userId  用户id
    @param userName  拟稿人姓名,支持模糊查询
    @param businessName  业务数据标题,支持模糊查询
    @param pageNo  页码
    @param pageSize  每页显示待办事宜数量
    @param startDate  任务接收日期开始范围
    @param endDate  任务接收日期结束范围
    @return  XML 格式的数据
    @throws DAOException
    @throws ServiceException
    @throws SystemException
     */
	@SuppressWarnings("unchecked")
	public String getProcessed(String userId, String userName, String businessName, String pageNo, String pageSize,
			String startDate, String endDate) throws DAOException, ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			List<String> items = new ArrayList<String>();  // 查询的属性
			items.add("taskId"); // 任务id
			items.add("taskStartDate"); // 任务开始时间
			items.add("businessName"); // 业务数据标题
			items.add("startUserName"); // 拟稿人
			items.add("processMainFormBusinessId"); // 业务数据 表名;主键名;主键值
			items.add("processTypeName"); // 公文类别名称

			Map<String, Object> paramsMap = new HashMap<String, Object>();//查询条件设置
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}

			List<String> type = new ArrayList<String>(2);// 流程类型，查询所有类别的公文
			//获取流程类型
			List<Object[]>  typeList = processDefinitionService.getAllProcessTypeList();			
			if (typeList != null && !typeList.isEmpty()){
				for (Object[] processType : typeList){
					type.add(String.valueOf(processType[0]));
				}
			}
			// 以下为查询条件
			paramsMap.put("handlerId", userId); // 处理人
			paramsMap.put("taskType", "3"); // 非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("processTypeId", type);
			if (userName != null && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", businessName);
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
			orderMap.put("taskStartDate", "1");//要排序的属性
			if (pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if (pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));
			//获取公文List
			List<Object[]> listWorkflow = workflowService.getTaskInfosByConditionForList(items, paramsMap, orderMap,
					null, null, null, null);
			List<String[]> result = new ArrayList<String[]>();

			List<Object[]> dlist = new ArrayList<Object[]>();// 存储非重复的任务
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (int i = 0; i < listWorkflow.size(); i++) {
					Object[] objs = (Object[]) listWorkflow.get(i);
					String businessId = (String) objs[4];
					if (businessId == null) {// 子流程数据
						String strNodeInfo = workflowService.getNodeInfo(objs[0].toString());
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
					String[] taskId = new String[2];
					taskId[0] = "string";
					taskId[1] = objs[0] == null?"": objs[0].toString();

					String[] taskStartDate = new String[2];
					taskStartDate[0] = "date";
					taskStartDate[1] = objs[1] == null?"": objs[1].toString();

					String[] businessNameArray = new String[2];
					businessNameArray[0] = "string";
					businessNameArray[1] = objs[2] == null?"": objs[2].toString();

					String[] startUserNameArray = new String[2];
					startUserNameArray[0] = "string";
					startUserNameArray[1] = objs[3] == null?"": objs[3].toString();
					
					String[] id = new String[2];
					String processTypeName = "";
					id[0] = "string";
					if(objs[5] != null){
						processTypeName = objs[5].toString();
						if(processTypeName.length() > 4){
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
					String.valueOf(pageWorkflow.getTotalCount()), String.valueOf(pageWorkflow.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取已办事宜发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取已办事宜发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取已办事宜发生系统级异常:" + ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取已办事宜发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * 提供获取待办事宜接口,支持分页、查询.
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
	 *         <fail-reason />
	 *         <data totalCount="总记录数" totalPages="总页数"> 
	 *         <row>
	 *         		<item type="string" value="任务id" />
	 *         		<item type="date" value="任务接收时间" /> 
	 *         		<item type="string" value="业务数据标题" /> 
	 *         		<item type="string" value="拟稿人" /> 
	 *         		<item type="string" value="公文类别名称" /> 
	 *         	    <item type="string" value="上一步处理人" /> 
	 *         </row> 
	 *         </data> 
	 *         </service-response> 
	 *         服务调用失败时返回数据格式如下： <?xml version="1.0" encoding="UTF-8"?>
	 *         <service-response> <status>0</status> <fail-reason>异常描述</fail-reason>
	 *         <data /> </service-response>
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String getTodo(String userId, String userName, String businessName, String pageNo, String pageSize,
			String startDate, String endDate) throws DAOException, ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			List<String> items = new ArrayList<String>();
			items.add("taskId"); // 任务id
			items.add("taskStartDate"); // 任务开始时间
			items.add("businessName"); // 业务数据标题
			items.add("startUserName"); // 拟稿人
			items.add("processTypeName"); // 公文类别名称
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if (userId == null || "".equals(userId)) {
				throw new SystemException("参数userId不能为空！");
			}

			List<String> type = new ArrayList<String>(2);// 流程类型，查询所有类别的公文
			//获取流程类型
			List<Object[]>  typeList = processDefinitionService.getAllProcessTypeList();			
			if (typeList != null && !typeList.isEmpty()){
				for (Object[] processType : typeList){
					type.add(String.valueOf(processType[0]));
				}
			}
			// 以下为查询条件
			paramsMap.put("handlerId",userId); // 处理人
			paramsMap.put("taskType","2"); // 非办结任务
			paramsMap.put("processSuspend","0");// 取非挂起任务
			//paramsMap.put("processTypeId",type);
			if (userName != null && !"".equals(userName)) {
				paramsMap.put("startUserName", userName);
			}
			if (businessName != null && !"".equals(businessName)) {
				paramsMap.put("businessName", businessName);
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
			Page<Object[]> pageWorkflow = workflowService.getTaskInfosByConditionForPage(page, items, paramsMap,
					orderMap, null, null, null, null);
			List<String[]> result = new ArrayList<String[]>();
			List<Object[]> listWorkflow = pageWorkflow.getResult();
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (Object[] objs : listWorkflow) {
					String[] taskId = new String[2];
					taskId[0] = "string";
					taskId[1] = objs[0] == null ? "" :objs[0].toString();
					
					List<Long> list = new ArrayList<Long>();
					list.add(Long.parseLong(objs[0].toString()));
					Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = null;
					TaskIdMapPreTaskBeanTemp = workflowForIpadService.getTaskIdMapPreTaskBeanTemper(list,userId);
					
					//得到上一步处理人
					String preTaskActor = null;
					preTaskActor = TaskIdMapPreTaskBeanTemp.get(objs[0].toString()).getTaskActorName();
					String[] preActor = new String[2];
					preActor[0] = "string";
					preActor[1] = preTaskActor;

					String[] taskStartDate = new String[2];
					taskStartDate[0] = "date";
					taskStartDate[1] = objs[1] == null ? "" :objs[1].toString();

					String[] businessNameArray = new String[2];
					businessNameArray[0] = "string";
					businessNameArray[1] = objs[2] == null ? "" :objs[2].toString();

					String[] startUserNameArray = new String[2];
					startUserNameArray[0] = "string";
					startUserNameArray[1] = objs[3] == null ? "" : objs[3].toString();

					String[] id = new String[2];
					String processTypeName = "";
					id[0] = "string";
					if(objs[4] != null){
						processTypeName = objs[4].toString();
						if(processTypeName.length() > 4){
							processTypeName = processTypeName.substring(0, 3);
						}
					}
					id[1] = processTypeName;

					result.add(taskId);
					result.add(taskStartDate);
					result.add(businessNameArray);
					result.add(startUserNameArray);
					result.add(id);
					result.add(preActor);
				}
			}

			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 6,
					String.valueOf(pageWorkflow.getTotalCount()), String.valueOf(pageWorkflow.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取待办事宜发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取待办事宜发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取待办事宜发生系统级异常:" + ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取待办事宜发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
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
	public String loadContent(String taskId) throws DAOException, ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			String strNodeInfo = workflowService.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			String bussinessId = arrNodeInfo[2]; // 业务数据id
			String formId = arrNodeInfo[3]; // 表单模板id
			List<EFormField> fieldList = manager.getFormTemplateFieldList(formId);
			EFormField eformField = null;
			for (EFormField field : fieldList) {
				if ("Office".equals(field.getType())) {// 找到OFFICE控件类型
					eformField = field;
					break;
				}
			}
			if (eformField == null) {
				throw new SystemException("表单中不存在正文。");
			}
			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkFieldName = args[1];
			String pkFieldValue = args[2];
			
			InputStream is = (InputStream) baseManager.getFieldValue(eformField.getFieldname(), tableName, pkFieldName,
					pkFieldValue);
			
			//转换成PDF流
			ByteArrayOutputStream os = (ByteArrayOutputStream) Office2PDF.office2PDF(is);
			
			if(os != null){
				is =  (InputStream)new ByteArrayInputStream(os.toByteArray());
			}
			
			/**			
			FileOutputStream f = new FileOutputStream("E://临时文件.pdf");			 
			int i = 0;
			while(i != -1) {
				i = is.read();
				f.write(i);
			}
			//注意流的关闭(★必须的)
			is.close();
			f.close();
             */
			
			BASE64Encoder encoder = new BASE64Encoder();
			String content = encoder.encode(FileUtil.inputstream2ByteArray(is));
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", content);
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生系统级异常:" + ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * 得到收发文表单数据,附件数据,办理记录
	 * 
	 * @param taskId
	 *            任务实例id
	 * @return 服务调用成功时发文数据格式如下： <?xml version="1.0" encoding="UTF-8"?>
<service-response>
	<status>1</status>
	<fail-reason />
	<data>
		<form>
			<item type="string" value="编号" />
			<item type="string" value="紧急程度" />
			<item type="string" value="拟稿单位" />
			<item type="string" value="拟稿人" />
			<item type="string" value="标题" />
			<item type="string" value="主送" />
			<item type="string" value="抄送" />
			<item type="string" value="主题词" />
			<item type="string" value="信息公开" />
			<item type="string" value="发文日期" />
			<item type="string" value="录入" />
			<item type="string" value="校对" />
		</form>
		<attachments>
			<attachment>
				<item type="string" value="附件id" />
				<item type="string" value="附件名称" />
			</attachment>
		</attachments>
		<suggestions>
			<suggestion>
				<item type="date" value="任务处理时间" />
				<item type="string" value="处理人部门名称" />
				<item type="string" value="处理人名称" />
				<item type="string" value="意见内容" />
			</suggestion>
		</suggestions>
	</data>
</service-response>
	 * 
	 * 服务调用成功时收文返回数据格式如下：<?xml version="1.0" encoding="UTF-8"?>
<service-response>
	<status>1</status>
	<fail-reason />
	<data>
		<form>
			<item type="string" value="来文单位" />
			<item type="string" value="来文号" />
			<item type="string" value="来文时间" />
			<item type="string" value="事由" />
			<item type="string" value="编号" />
			<item type="string" value="紧急程度" />
		</form>
		<attachments>
			<attachment>
				<item type="string" value="附件记录id" />
				<item type="string" value="附件名称" />
			</attachment>
		</attachments>
		<suggestions>
			<suggestion>
				<item type="date" value="任务处理时间" />
				<item type="string" value="处理人部门名称" />
				<item type="string" value="处理人名称" />
				<item type="string" value="意见内容" />
			</suggestion>
		</suggestions>
	</data>
</service-response> 
 服务调用失败时返回数据格式如下： <?xml version="1.0"
	 * encoding="UTF-8"?> <service-response> <status>0</status>
	 * <fail-reason>异常描述</fail-reason> <data /> </service-response>
	 * @throws Exception
	 */
	public String loadFormData(String loginUserId, String taskId) throws Exception {
//		String ret = null;
//		Dom4jUtil dom = new Dom4jUtil();
//		try {
//			String strNodeInfo = workflowService.getNodeInfo(taskId);
//			String[] arrNodeInfo = strNodeInfo.split(",");
//			String bussinessId = arrNodeInfo[2]; // 业务数据id
//			String[] args = bussinessId.split(";");
//			String tableName = args[0];
//			String pkFieldName = args[1];
//			String pkFieldValue = args[2];
//			List<String[]> result = new ArrayList<String[]>();
//			StringBuilder sql = new StringBuilder("select ");
//			if (tableName.equalsIgnoreCase("t_oa_senddoc")) {// 发文
//				// 编号,紧急程度,拟稿单位
//				sql.append("WORKFLOWCODE,PERSON_CONFIG_FLAG,SENDDOC_ISSUE_DEPART_SIGNED,");
//				// 拟稿人,标题,主送,抄送
//				sql.append("SENDDOC_AUTHOR,WORKFLOWTITLE,SENDDOC_SUBMITTO_DEPART,SENDDOC_CC_DEPART,");
//				// 主题词,信息公开,发文日期,录入,校对
//
//				// 运管字段不同
//				sql.append("SENDDOC_GKXZ,SENDDOC_NGDW,SENDDOC_EMERGENCY,");
//
//				sql.append("SENDDOC_KEYWORDS,REST5,SENDDOC_OFFICIAL_TIME,SENDDOC_ENTRY_PEOPLE,SENDDOC_PROOF_READER");
//				sql.append(" from ").append(tableName);
//				sql.append(" where ").append(pkFieldName).append(" ='").append(pkFieldValue).append("'");
//				Map map = baseManager.queryForMap(sql.toString());
//				if (map != null && !map.isEmpty()) {
//					result.add(new String[] { "string", (String) map.get("WORKFLOWCODE") });
//					String flag = (String) map.get("SENDDOC_EMERGENCY");
//
//					// String flag = (String)map.get("PERSON_CONFIG_FLAG");
//					// if("1".equals(flag)){
//					// flag = "平急";
//					// } else if("2".equals(flag)){
//					// flag = "加急";
//					// } else if("3".equals(flag)){
//					// flag = "特急";
//					// } else if("4".equals(flag)){
//					// flag = "特提";
//					// } else {
//					// flag = "无";
//					// }
//					result.add(new String[] { "string", flag });
//					result.add(new String[] { "string", (String) map.get("SENDDOC_ISSUE_DEPART_SIGNED") }); //拟稿单位
//					result.add(new String[] { "string", (String) map.get("SENDDOC_NGDW") });//拟稿单位
//
//					result.add(new String[] { "string", (String) map.get("SENDDOC_AUTHOR") });//拟稿人
//					result.add(new String[] { "string", (String) map.get("WORKFLOWTITLE") });//标题
//					result.add(new String[] { "string", (String) map.get("SENDDOC_SUBMITTO_DEPART") });//主送
//					result.add(new String[] { "string", (String) map.get("SENDDOC_CC_DEPART") });//抄送
//					result.add(new String[] { "string", (String) map.get("SENDDOC_KEYWORDS") });//主题词
//
//					String xxgk = (String) map.get("SENDDOC_GKXZ");//信息公开
//					// String xxgk = (String)map.get("REST5");
//					// if("01".equals(xxgk)) {
//					// xxgk = "主动公开";
//					// } else if("02".equals(xxgk)) {
//					// xxgk = "依申请公开";
//					// } else if("03".equals(xxgk)) {
//					// xxgk = "免于公开";
//					// } else {
//					// xxgk = "";
//					// }
//					result.add(new String[] { "string", xxgk });
//					Object time = map.get("SENDDOC_OFFICIAL_TIME");//发文日期
//					String strTime = null;
//					if (time != null) {
//						strTime = time.toString();
//					}
//					result.add(new String[] { "date", strTime });
//					result.add(new String[] { "string", (String) map.get("SENDDOC_ENTRY_PEOPLE") });//录入
//					result.add(new String[] { "string", (String) map.get("SENDDOC_PROOF_READER") });//校对
//				}
//			} else {
//				// 来文单位,来文号,来文时间,事由,编号,紧急程度
//				sql.append("ISSUE_DEPART_SIGNED,DOC_NUMBER,RECV_TIME,WORKFLOWTITLE,WORKFLOWCODE,PERSON_CONFIG_FLAG");
//				sql.append(" from ").append(tableName);
//				sql.append(" where ").append(pkFieldName).append(" ='").append(pkFieldValue).append("'");
//				Map map = baseManager.queryForMap(sql.toString());
//				if (map != null && !map.isEmpty()) {
//					result.add(new String[] { "string", (String) map.get("ISSUE_DEPART_SIGNED") });
//					result.add(new String[] { "string", (String) map.get("DOC_NUMBER") });
//					result.add(new String[] { "date",
//							map.get("RECV_TIME") == null ? null : map.get("RECV_TIME").toString() });
//					result.add(new String[] { "string", (String) map.get("WORKFLOWTITLE") });
//					result.add(new String[] { "string", (String) map.get("WORKFLOWCODE") });
//					String flag = (String) map.get("PERSON_CONFIG_FLAG");
//					if ("1".equals(flag)) {
//						flag = "平急";
//					} else if ("2".equals(flag)) {
//						flag = "加急";
//					} else if ("3".equals(flag)) {
//						flag = "特急";
//					} else if ("4".equals(flag)) {
//						flag = "特提";
//					} else {
//						flag = "无";
//					}
//					result.add(new String[] { "string", flag });
//				}
//			}
//			List<WorkflowAttach> attachs = attachService.getWorkflowAttachsByDocId(pkFieldValue, false);
//			List<String[]> attachments = new ArrayList<String[]>();
//			if (attachs != null && !attachs.isEmpty()) {
//				for (WorkflowAttach attach : attachs) {
//					attachments
//							.add(new String[] { "string", attach.getDocattachid(), "string", attach.getAttachName() });
//				}
//			}
//			// 办理记录
//			String instanceId = workflowService.getProcessInstanceIdByTiId(taskId);
//			List<Object[]> list = workflowService.getBusiFlagByProcessInstanceId(instanceId);
//			if (list != null && !list.isEmpty()) {
//				String orgName = "";
//				for (int i = 0; i < list.size(); i++) {
//					Object[] objs = list.get(i);
//					String userId = objs[5].toString();
//					Organization org = userService.getUserDepartmentByUserId(userId);
//					if (org != null) {
//						orgName = org.getOrgName();
//					}
//					objs = ObjectUtils.addObjectToArray(objs, orgName);
//					list.set(i, objs);
//				}
//			}
//			ret = dom.createItemsResponseData(STATUS_SUC, null, result, attachments, list);
//		} catch (Exception ex) {
//			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生异常:" + JavaUtils.stackToString(ex), null, null);
//		}
//		return ret;
		 String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			//获取公文的表单详细信息
			ret = workflowForIpadService.getForm(loginUserId, taskId);
			//恢复会签挂起的任务
			baseManager.resumeConSignTask(taskId);
		} catch (Exception ex) {
			ex.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文发生未知异常:" + ex.getMessage(), null, null);
		}
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
			//获取附件对象
			WorkflowAttach attach = attachService.get(id);
			if (attach == null || attach.getAttachContent() == null) {
				throw new SystemException("附件'" + id + "'不存在！");
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

			//将数据转换为BASE64格式
			BASE64Encoder encoder = new BASE64Encoder();
			String content = encoder.encode(attach.getAttachContent());
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", content);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取公文附件发生异常:" + ex.getMessage(), null, null);
		}
		return ret;
	}
	/**
	 * 取回任务
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
			ret = dom.createItemResponseData(STATUS_FAIL, "取回任务时发生异常:" + ex.getMessage(), null, null);
		} finally {
			userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
		}
		return ret;
	}

	/**
	  此方法废除
	 */
	@SuppressWarnings("unchecked")
	public String submitWorkflow(String taskId, String suggestion, String userId) {
		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();
		try {
			List<Object[]> trans = workflowService.getNextTransitions(taskId);
			if (trans != null && trans.size() > 1) {
				throw new SystemException("下一步骤存在多个分支，系统暂不支持多分支操作。");
			}
			String strNodeInfo = workflowService.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			String formId = "0";
			String businessId = null;
			if ("form".equals(arrNodeInfo[0])) {
				businessId = arrNodeInfo[2]; // 业务id
				formId = arrNodeInfo[3]; // 表单id
			}
			JSONObject json = new JSONObject();
			json.put("suggestion", suggestion);
			json.put("CAInfo", "");
			Object[] objs = trans.get(0);
			String transitionName = objs[0].toString();
			String returnNodeId = "";
			userService.setUserId(userId);
			workflowService.goToNextTransition(taskId, transitionName, returnNodeId, "0", formId, businessId, json
					.toString(), userId, null);
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
		} catch (Exception e) {
			e.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "提交工作流异常，异常详细信息：" +e.getMessage(), null, null);
		} finally {
			userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
		}
		return ret;
	}

	/**
	 * 
	 * @author:邓志城
	 * @date:2009-11-20 下午04:29:44
	 * @param info
	 * @param attachments
	 * @param fileNames
	 * @return
	 */
	public String saveFormData(String info, DataHandler[] attachments, String[] fileNames) {
		String ret = "";
		LogPrintStackUtil.logInfo("IPP调用开始：" + new Date() + info.toString());
		Dom4jUtil dom = new Dom4jUtil();
		try {
			String businessId = manager.saveBusinessData(info, attachments, fileNames);
			// String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new
			// Date());
			JSONObject objInfo = JSONObject.fromObject(info);
			String title = objInfo.getString("title");// 标题
			// 获取此流程名称下对应的流程
			List lst = manager.getStartWorkflowTransitions(workflowName);
			// 如果有多个则默认取第一个
			if (lst != null && lst.size() > 0) {
				Object[] objs = (Object[]) lst.get(0);
				manager.handleWorkflowWithUser(workflowName, businessId, title, objs[0].toString(), "", "请审批");
				ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "提交工作流异常，异常详细信息：" + e.getMessage(), null, null);
		}
		LogPrintStackUtil.logInfo("IPP调用结束：" + new Date());
		LogPrintStackUtil.logInfo("OA返回IPP:" + ret);
		return ret;
	}
	
   /**
   @Description: TODO(获取流程办理常用意见) 
   @author penghj    
   @date Mar 14, 2012 8:28:10 PM 
   @param userId
   @return
    */
	public String getUsedSuggestion(String userId) {
		String ret = "";
		List<String> suggestions = new ArrayList<String>();
		Dom4jUtil dom = new Dom4jUtil();
		try {
			//获取床用意见的List
			ideaLst = approvalSuggestionService.getAppSuggestionListByUserId(userId);
			String os = "100";
			for (int j = 0; j < ideaLst.size(); j++) {
				ToaApprovalSuggestion suggestionBean = (ToaApprovalSuggestion) ideaLst.get(j);
				String suggestion = suggestionBean.getSuggestionContent();
				suggestion = suggestion.replaceAll("\r", "");// 处理审批意见有回车的情况
				suggestion = suggestion.replaceAll("\n", " ");// 处理审批意见有换行的情况
				os = suggestion;

				String[] FF_String = new String[] { "\'", "\"", "<", ">", "\\\\" };// 特殊字符
				String[] NFF_String = new String[] { "’", "”", "＜", "＞", "＼" };// 替换字符
				boolean isFlag = false;
				for (int i = 0; i < FF_String.length; i++) {// 如果存在特殊字符
					if (suggestion.indexOf(FF_String[i]) != -1) {
						suggestion = suggestion.replaceAll(FF_String[i], NFF_String[i]);
						isFlag = true;
					}
				}
				if (isFlag) {
					suggestionBean.setSuggestionContent(suggestion);// 如果存在特殊字符，就替换
				}
				suggestions.add(suggestion);
			}
			// 组装常用意见List
			ret = dom.createItemsUsedSuggestionData(STATUS_SUC, null, suggestions);
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取常用办理意见数据级异常:" +e.getMessage(), null, null);
		}
		return ret;
	}
	
     /**
     @Description: TODO(	获得当前公文办理时，下一步流程的列表) 
     @author penghj    
     @date Mar 14, 2012 8:29:20 PM 
     @param taskId 任务id
     @return
      */
	public String getNextTransitions(String taskId) {

		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();

		List<String[]> transitions = new ArrayList<String[]>();
		try {
			if (taskId != null && !"undefined".equals(taskId) && !"".equals(taskId)) {
				// 选取指定任务下一步可选步骤
				String transitionId = "";
				String transitionName = "";
				String taskActorNo = "0";
			
				String os = "";
				
				//获取指定任务实例的下一步可选流向
				List<Object[]> trans = workflowService.getNextTransitions(taskId);
											
				for (Object[] obj : trans) {
					String nodeId = "";
					String nodeName = "";
					transitionId = obj[1].toString();
					transitionName = (String) obj[0];
					//transitionNo = (String) obj[2];
					Set setTrans = (Set) obj[3];
					String[] listNode = new String[5];
					for (Iterator it = setTrans.iterator(); it.hasNext();) {
						taskActorNo = "0";
						os = (String) it.next();
						String[] str = os.split("\\|");
						
						String typeId = str[0];
						if("subProcessNode".equals(typeId)){
							break;
						}
						nodeId = str[1];
						nodeName = str[2];
						Object[] objs = baseWorkflowManager.getNodesettingByNodeId(nodeId);
						if(objs[0] != null && !"".equals(String.valueOf(objs[0]))){
							int no = Integer.parseInt(objs[0].toString());
							if(no > 1){
								taskActorNo = "1";
							}
						}
						
					}
					
					listNode[0] = transitionId;
					listNode[1] = transitionName;
					listNode[2] = taskActorNo;
					listNode[3] = nodeId;
					listNode[4] = nodeName;
					transitions.add(listNode);
				}

			}
			//下一步流程列表排序	
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
			
			ret = dom.createItemsTransitionsData(STATUS_SUC, null, transitions);
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取下一步骤数据级异常:" + e.getMessage(), null, null);
		}
		return ret;
	}

	/**
	@Description: TODO(获取下一步骤处理人) 
	@author penghj    
	@date Mar 14, 2012 8:30:53 PM 
	@param nodeId   节点ID
	@param taskId     任务ID
	@param transitionId  迁移线ID
	@return  处理人 
	 */
	public String getWorkflowTaskActors(String nodeId, String taskId, String transitionId) {
		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();
		List taskActorsList = new ArrayList();
		try {
			if(nodeId != null && !"".equals(nodeId) && !"null".equals(nodeId)){
				//通过任务节点ID得到任务节点默认设置人员信息
				//判断是否返回经办人的情况
				//String ns = workflowService.getNodeIdByTaskInstanceId(taskId) ;
				Object[] objs = baseWorkflowManager.getNodesettingByNodeId(workflowService.getNodeIdByTaskInstanceId(taskId));
				if(objs[2] != null && "1".equals(String.valueOf(objs[2]))){
					//返回经办人
					JbpmContext jbpmContext = workflowService.getJbpmContext();
					TaskInstance taskInstance = jbpmContext.getTaskInstance(new Long(
							taskId));
					List<TaskInstanceBean> taskList = workflowService.getTruePreTaskInfosByTiId(taskInstance.getId() + "");
					if(!taskList.isEmpty()){
						for (TaskInstanceBean taskInstanceBean : taskList) {
							String userId = taskInstanceBean.getTaskActorId();
							User user = userService.getUserInfoByUserId(userId);
							String userName = user.getUserName();
							String orgId = user.getOrgId();
							Organization organization = userService
							.getDepartmentByOrgId(orgId);
							String orgName = organization.getOrgName();
							String[] str = new String[3];
							str[0] = userId;
							str[1] = userName;
							str[2] = orgName;
							taskActorsList.add(str);
						}
					}
				}else{
					taskActorsList = workflowService.getTaskActorsByTask(nodeId, taskId, transitionId);
				}
				
			}
			ret = dom.createItemsTaskActorsData(STATUS_SUC, null, taskActorsList);
		} catch (ServiceException e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取下一步骤处理人数据级异常:" +e.getMessage(), null, null);
			throw new ServiceException(MessagesConst.operation_error, new Object[] { "获取人员信息" });
		}

		return ret;
	}
	 
	/**
	@Description: TODO(	提交流程  ) 
	@author penghj    
	@date Mar 14, 2012 8:32:26 PM 
	@param taskId  任务ID
	@param userId 登陆用户ID
	@param suggestion  提交意见
	@param transitionName  流向名称
	@param taskActors  处理人id
	@param nodeId  节点Id
	@return
	 */
	public String submitWorkflowNext(String taskId, String userId, String suggestion, String transitionName,
			String taskActors, String nodeId) {
		String ret = "";
		Dom4jUtil dom = new Dom4jUtil();

		String returnNodeId = "";
		
		String flag = manager.judgeTaskIsDone(taskId);
		String[] flags = flag.split("\\|");
		String retCode = flags[0];
		if (retCode.equals("f1")) {
			ret = dom.createItemResponseData(STATUS_FAIL, "该任务正在被其他人处理或被挂起，请稍后再试或与管理员联系！", null, null);
			
		} else if (retCode.equals("f2")) {
			ret = dom.createItemResponseData(STATUS_FAIL, "该任务已被取消，请查阅处理记录或与管理员联系", null, null);
			throw new SystemException("该任务已被取消，请查阅处理记录或与管理员联系！");
		} else if (retCode.equals("f3")) {
			ret = dom.createItemResponseData(STATUS_FAIL, "该任务已被其他人处理，请查阅详细处理记录！", null, null);

		} else{
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

				// 处理人
			
				if (taskActors != null && !"".equals(taskActors) && !"null".equals(taskActors)) {
			        //拼装处理人的数据格式： actor|nodId
					String[] actors = taskActors.split(",");
					String[] realActors=new String[actors.length];
					for (int i = 0; i < actors.length; i++) {
						String realActor = actors[i] + "|" + nodeId;
						realActors[i] = realActor;
					}
					userService.setUserId(userId);
				   //提交流程
					workflowService.goToNextTransition(taskId, transitionName, returnNodeId, "1", formId, businessId, json
							.toString(), userId, realActors);
				}else{
					userService.setUserId(userId);
					//获取上下文实例
					ContextInstance contextInstance = workflowService.getTaskInstanceById(taskId).getProcessInstance().getContextInstance();
					contextInstance.setVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_TRANSITIONDEPT, "0");//存储协办处室标志
					workflowService.goToNextTransition(taskId, transitionName, returnNodeId, "1", formId, businessId, json
							.toString(), userId, null);
				}
				
				ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
			} catch (Exception e) {
				e.printStackTrace();
				ret = dom.createItemResponseData(STATUS_FAIL, "提交流程数据级异常:" +e.getMessage(), null, null);
			}
		}


		return ret;
	}

}
