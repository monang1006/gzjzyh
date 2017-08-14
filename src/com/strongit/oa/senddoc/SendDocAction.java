/*
s * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.senddoc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;

import com.strongit.doc.sends.DocSendManager;
import com.strongit.oa.autoencoder.CodemanageManager;
import com.strongit.oa.bo.TEJsptemplate;
import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.ToaDoctemplateGroup;
import com.strongit.oa.bo.ToaInfopublishArticle;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaPrintSet;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.service.OaFormPdfService;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongit.oa.doctemplate.doctempType.DocTempTypeManager;
import com.strongit.oa.eformJspManager.EformJspManagerManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService;
import com.strongit.oa.senddoc.bo.ParamBean;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocDesktopManager;
import com.strongit.oa.senddoc.manager.SendDocHtmlManager;
import com.strongit.oa.senddoc.manager.SendDocLinkManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.senddoc.manager.service.ISendDocIcoService;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.senddoc.util.HandleKindConst;
import com.strongit.oa.senddoc.util.JsonUtil;
import com.strongit.oa.systemset.ISystemsetService;
import com.strongit.oa.systemset.PrintSetManager;
import com.strongit.oa.traceDoc.TraceDocManager;
import com.strongit.oa.updatebadwords.phrasefilter.IPhraseFilterManage;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseNodesettingPlugin;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 发文处理Action 约定：Ajax调用，“0”表示调用成功；“1”表示调用失败。
 * 
 * 
 * @author dengwenqiang
 * @version 1.0
 */
/**
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 11, 2012 10:13:05 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SendDocAction
 */
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "sendDoc.action", type = ServletActionRedirectResult.class),
		@Result(name = "personaldraft", value = "/WEB-INF/jsp/senddoc/handle/personal/sendDoc-draft.jsp", type = ServletDispatcherResult.class),
		@Result(name = "personaltodo", value = "/WEB-INF/jsp/senddoc/handle/personal/sendDoc-todo.jsp", type = ServletDispatcherResult.class),
		@Result(name = "personalmyTodo", value = "/WEB-INF/jsp/senddoc/handle/personal/sendDoc-myTodo.jsp", type = ServletDispatcherResult.class),
		@Result(name = "personalmyReturn", value = "/WEB-INF/jsp/senddoc/handle/personal/sendDoc-myReturn.jsp", type = ServletDispatcherResult.class),
		@Result(name = "personalprocessed", value = "/WEB-INF/jsp/senddoc/handle/personal/sendDoc-processed.jsp", type = ServletDispatcherResult.class),
		@Result(name = "personalrepeal", value = "/WEB-INF/jsp/senddoc/handle/personal/sendDoc-repeal.jsp", type = ServletDispatcherResult.class),
		@Result(name = "personalhostedby", value = "/WEB-INF/jsp/senddoc/handle/personal/sendDoc-hostedby.jsp", type = ServletDispatcherResult.class),
		@Result(name = "senddocdraft", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-draft.jsp", type = ServletDispatcherResult.class),
		@Result(name = "senddoctodo", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-todo.jsp", type = ServletDispatcherResult.class),
		@Result(name = "senddocmyTodo", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-myTodo.jsp", type = ServletDispatcherResult.class),
		@Result(name = "senddocmyReturn", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-myReturn.jsp", type = ServletDispatcherResult.class),
		@Result(name = "senddocprocessed", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-processed.jsp", type = ServletDispatcherResult.class),
		@Result(name = "senddocrepeal", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-repeal.jsp", type = ServletDispatcherResult.class),
		@Result(name = "senddochostedby", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-hostedby.jsp", type = ServletDispatcherResult.class),
		@Result(name = "recvdocdraft", value = "/WEB-INF/jsp/senddoc/handle/recvdoc/sendDoc-draft.jsp", type = ServletDispatcherResult.class),
		@Result(name = "recvdoctodo", value = "/WEB-INF/jsp/senddoc/handle/recvdoc/sendDoc-todo.jsp", type = ServletDispatcherResult.class),
		@Result(name = "recvdocmyTodo", value = "/WEB-INF/jsp/senddoc/handle/recvdoc/sendDoc-myTodo.jsp", type = ServletDispatcherResult.class),
		@Result(name = "recvdocmyReturn", value = "/WEB-INF/jsp/senddoc/handle/recvdoc/sendDoc-myReturn.jsp", type = ServletDispatcherResult.class),
		@Result(name = "recvdocprocessed", value = "/WEB-INF/jsp/senddoc/handle/recvdoc/sendDoc-processed.jsp", type = ServletDispatcherResult.class),
		@Result(name = "recvdocrepeal", value = "/WEB-INF/jsp/senddoc/handle/recvdoc/sendDoc-repeal.jsp", type = ServletDispatcherResult.class),
		@Result(name = "yijian", value = "/WEB-INF/jsp/senddoc/handle/senddoc/sendDoc-yijian.jsp", type = ServletDispatcherResult.class),
		@Result(name = "recvdochostedby", value = "/WEB-INF/jsp/senddoc/handle/recvdoc/sendDoc-hostedby.jsp", type = ServletDispatcherResult.class),
		@Result(name = "annal", value = "/WEB-INF/jsp/workflow/annal.jsp", type = ServletDispatcherResult.class) })
public class SendDocAction extends AbstractBaseWorkflowAction<ToaSenddoc> {

	private static final long serialVersionUID = 4103263551309775221L;
	private HttpServletRequest m_request;
	@Autowired
	SendDocDesktopManager sendDocDesktopManager;
	@Autowired
	AttachManager attachManager;
	@Autowired
	private IPhraseFilterManage filterManager;
	@Autowired
	private IUserService user;

	@Autowired
	SendDocManager manager;
	@Autowired
	private MessageManager Mmanager;
	@Autowired
	SendDocUploadManager sendDocUploadManager;

	@Autowired
	ISendDocIcoService sendDocIcoManager;

	@Autowired
	SendDocLinkManager sendDocLinkManager;
	@Autowired
	EformJspManagerManager jspManager;
	@Autowired
	IUserService userService;

	@Autowired
	private IWorkflowService workflowService;

	protected Page page = new Page(FlexTableTag.MAX_ROWS, false);

	private Page<Object[]> SendDocpage = new Page(15, true);

	protected List showColumnList;// 显示字段列表

	protected List<EFormComponent> queryColumnList;// 查询字段列表

	protected Page workflowPage = new Page(FlexTableTag.MAX_ROWS, true);

	private String userId; // 用户ID
	private String orguserid; // 收邮件人ids

	private String msgReceiverNames;// 收邮件人名字

	@Autowired
	private IRelativeWorkflowService rwSrv;

	public String getMsgReceiverNames() {
		return msgReceiverNames;
	}

	public void setMsgReceiverNames(String msgReceiverNames) {
		this.msgReceiverNames = msgReceiverNames;
	}

	public String getOrguserid() {
		return orguserid;
	}

	public void setOrguserid(String orguserid) {
		this.orguserid = orguserid;
	}

	// 是否为邮件
	private String sendMail;

	public String getSendMail() {
		return sendMail;
	}

	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	private String printed;
	// 邮件主题
	private String mailMain;

	public String getMailMain() {
		return mailMain;
	}

	public void setMailMain(String mailMain) {
		this.mailMain = mailMain;
	}

	// 邮件发送人
	private String sendPerson;

	public String getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public String getWorkflowtitle() {
		return workflowtitle;
	}

	public void setWorkflowtitle(String workflowtitle) {
		this.workflowtitle = workflowtitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	// 邮件主题
	private String workflowtitle;
	// 邮件内容
	private String mailContent;
	private String total;

	private String docId;

	// private File wordDoc; // Word文档

	private SendDocManager theSendDocManager;

	private String insId;

	// 查询流程中所有查询字段定义

	private String processName; // 流程名称

	private String startUserId; // 流程发起人ID

	private String startUserName; // 流程发起人名称

	private String taskType; // 搜索任务类型(“0”：待办；“1”：在办；“2”：非办结；“3”：办结）

	protected String processStatus; // 流程状态（“0”：待办；“1”：办毕)

	private Date taskStartDateStart; // 任务开始时间上限

	private Date taskStartDateEnd; // 任务开始时间下限

	private Date taskEndDateStart; // 任务开始时间上限

	private Date taskEndDateEnd; // 任务开始时间下限

	private Date processStartDate; // 流程创建开始时间

	private Date processEndDate; // 流程红结束开始时间

	private String taskNodeName; // 任务节点名

	private String privilName; // 资源属性名称

	private String exitSuggestion; // 取回流程时，之前的意见

	protected String roomId; // 处室id

	protected String isReceived; // 是否签收

	private String docid;// 反馈意见，工作流ID

	private String path1;

	private String flag1;

	public String getPath1() {
		return path1;
	}

	public void setPath1(String path1) {
		this.path1 = path1;
	}

	public String getFlag1() {
		return flag1;
	}

	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}

	private List<ToaInfopublishColumnArticl> article;

	private ToaInfopublishArticle pub;

	private Page<ToaInfopublishColumnArticl> articles = new Page<ToaInfopublishColumnArticl>(
			FlexTableTag.MAX_ROWS, true);

	public List<ToaInfopublishColumnArticl> getArticle() {
		return article;
	}

	public void setArticle(List<ToaInfopublishColumnArticl> article) {
		this.article = article;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public ToaInfopublishArticle getPub() {
		return pub;
	}

	public void setPub(ToaInfopublishArticle pub) {
		this.pub = pub;
	}

	public Page<ToaInfopublishColumnArticl> getArticles() {
		return articles;
	}

	public void setArticles(Page<ToaInfopublishColumnArticl> articles) {
		this.articles = articles;
	}

	// 机构列表
	private List orgList;
	// 机构分组
	private List orgGroupList;

	@Autowired
	DocTempTypeManager tempTypeManager; // 模板类别服务类.

	@Autowired
	DocTempItemManager tempItemManager; // 模板服务类

	@Autowired
	IProcessDefinitionService processDefinitionService;

	@Autowired
	IProcessInstanceService processInstanceService;

	@Autowired
	ITaskService TaskService;

	@Autowired
	CodemanageManager codeService;// 编号生成器服务类

	@Autowired
	private DocSendManager docManager;

	@Autowired
	DefinitionPluginService definitionPluginService;// 流程定义插件服务类

	@Autowired
	private OaFormPdfService formPdfService; // PDF快照生成类

	private ToaSenddoc model = new ToaSenddoc(); // 公文对象

	protected String workflowCode; // 生成的编码
	protected String userJob; // 用户职务

	protected String isNeedSign; // 是否需要签名认证 1：需要签名认证

	private String sortType = ""; // 已办事宜排序方式

	private String isFind = "0"; // "1":当前正在执行查询操作，否则，不是在执行查询操作

	protected String firstNodeControlName;// 一个节点上绑定的意见控件名称

	protected int timeout = 0; // session生命周期

	protected String suggestionStyle; // 意见输入模式
	/** 词语过滤模块--消息模块对应的ID */
	private static final String FILTER_PHRASE_ID = "402882271e1f6980011e1f6ce51a0001";
	@Autowired
	SendDocHtmlManager sendDocHtmlManager;
	/**
	 * 待办公文文档权限控制
	 */
	protected String privilegeInfo;

	/**
	 * 系统设置接口
	 */
	private ISystemsetService isystemsetService;

	private PrintSetManager printSetManager;

	protected @Autowired
	TraceDocManager traceDocManager; // 注入跟踪重要文件接口

	@Autowired
	public void setPrintSetManager(PrintSetManager aprintSetManager) {
		printSetManager = aprintSetManager;
	}

	public Page getPage() {
		if (page.getPageSizeBak() != -1
				&& page.getPageSizeBak() != page.getPageSize()) {
			page.setPageNo(1);
		}
		if (page.getPageSizeBak() != -1
				&& page.getPageNoBak() == page.getPageNo()) {
			page.setPageNo(1);
		}
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String sendTrace() throws Exception {
		List list = traceDocManager.getToaTraceDocByConId(insId,
				adapterBaseWorkflowManager.getUserService().getCurrentUser()
						.getUserId());
		if (list != null && list.size() > 0) {
			renderText("1");
		} else {
			renderText("0");
		}
		return null;
	}

	// 转发邮件
	public String sendMail() {
		if (!"".equals(tableName) && tableName != null
				&& tableName.trim() != "undefined") {
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = manager.getPrimaryKeyName(tableName);// 得到主键名称
			}
			Object o = manager.getFieldValue("workflowtitle", tableName,
					pkFieldName, pkFieldValue);
			if (o != null) {
				workflowtitle = o.toString();
			}

		} else {
			TaskInstance taskInstance = adapterBaseWorkflowManager
					.getWorkflowService().getTaskInstanceById(pkFieldValue);
			ProcessInstance processInstance = taskInstance.getProcessInstance();
			workflowtitle = processInstance.getBusinessName();
			orguserid = "";
			msgReceiverNames = "";
			List<Object[]> task = adapterBaseWorkflowManager
					.getWorkflowService()
					.getProcessHandlesAndNodeSettingByPiId(instanceId + "");
			/** 去除重复的 任务处理人id ---start */
			List<Object> personlist = new ArrayList<Object>();
			for (Object[] person : task) {
				personlist.add(person[5]);
			}
			HashSet<Object> h = new HashSet<Object>(personlist);
			personlist.clear();
			personlist.addAll(h);
			/** 去除重复的任务处理人 id ---end */
			for (int i = 0; i < personlist.size(); i++) {
				String userid = (String) personlist.get(i);
				if (!userid.equals(adapterBaseWorkflowManager.getUserService()
						.getCurrentUserInfo().getUserId())) {
					String userName = adapterBaseWorkflowManager
							.getUserService().getUserNameByUserId(userid);
					if (userName != null && !"null".equals(userName)) {
						orguserid += userid + ",";
						msgReceiverNames += userName + ",";
					}
				}
			}
			if (orguserid.length() > 1) {
				orguserid = orguserid.substring(0, orguserid.length() - 1);
			}
			if (msgReceiverNames.length() > 1) {
				msgReceiverNames = msgReceiverNames.substring(0,
						msgReceiverNames.length() - 1);
			}
		}
		// return HandleKindConst.getReturnString(handleKind, "sendMail");
		return "sendMail";
	}

	public String saveMail() {
		if (!"".equals(tableName) && tableName != null
				&& tableName.trim() != "undefined") {
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = manager.getPrimaryKeyName(tableName);// 得到主键名称
			}
		} else {
			taskId = pkFieldValue;
			TaskInstance taskInstance = adapterBaseWorkflowManager
					.getWorkflowService().getTaskInstanceById(pkFieldValue);
			ProcessInstance processInstance = taskInstance.getProcessInstance();
			instanceId = processInstance.getId() + "";

		}
		String path = getRequest().getContextPath();
		ToaMessage msg = new ToaMessage();
		mailContent = "<br>关联文件：<a target='_blank' href='" + path
				+ "/senddoc/sendDoc!viewProcessed.action?formId=" + formId
				+ "&workflowName=" + workflowName + "&pkFieldName="
				+ pkFieldName + "&pkFieldValue=" + pkFieldValue + "&tableName="
				+ tableName + "&taskId=" + taskId + "&instanceId=" + instanceId
				+ "&sendMail=true&state=0'>" + workflowtitle
				+ "</a><br><br>文件内容：" + mailContent;
		String content = filterManager.filterPhrase(mailContent,
				FILTER_PHRASE_ID);
		msg.setMsgTitle(mailMain);
		msg.setMsgContent(HtmlUtils.htmlEscape(content));
		// 处理用户id格式
		String sender = Mmanager
				.formatUserId(user.getCurrentUser().getUserId());
		String recvUser = Mmanager.formatUserId(orguserid);

		msg.setMsgSender(sender);
		msg.setMsgReceiver(recvUser);
		ToaMessage msg1 = Mmanager.sendMessage(null, msg, null, null, null,
				new OALogInfo("我的消息-消息记录-『发送』：" + mailMain));
		return "reloads";
	}

	/**
	 * RTX侧边板列出待办公文 add yangwg
	 * 
	 * @return
	 */
	public String listTodoDoc() {
		System.out.println("列出待办公文");
		// 获取待办公文
		try {
			Page pageTodo = new Page(FlexTableTag.MAX_ROWS, false);
			pageTodo = manager.getTodo(pageTodo, "");
			// <Object[任务实例id,接收日期,发起人,流程名称,流程实例Id,流程标题,发起人ID,业务ID]
			List todoDocList = pageTodo.getResult();
			this.getRequest().setAttribute("todoDocList", todoDocList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listTodoDoc";
	}

	/**
	 * 查询当前用户所处理的流程
	 * 
	 * @author zhengzb
	 * @desc 2011-3-21 下午02:42:20
	 * @return
	 * @throws Exception
	 * @modify yanjian 过滤流程标题中的换行符 2011-09-25 9:13
	 */
	@SuppressWarnings("unchecked")
	public String searchWorkflow() throws Exception {
		workflowTypeList = adapterBaseWorkflowManager.getWorkflowService()
				.getAllProcessTypeList();
		User user = adapterBaseWorkflowManager.getUserService()
				.getCurrentUser();
		Map paramsMap = new HashMap<String, Object>();
		// 增加对流程类型过滤搜索 added by dengzc 2010年12月7日17:19:43
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType) && !"0".equals(workflowType)) {
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			if (workflowType.startsWith("-")) {// 不想显示的流程类型
				List<String> FulType = new ArrayList<String>();
				for (String tp : workflowTypes) {
					FulType.add(tp.substring(1));
				}
				List<Object[]> allProcessTypeObjs = adapterBaseWorkflowManager
						.getWorkflowService().getAllProcessTypeList();
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
			paramsMap.put("processTypeId", lType);
		}
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		paramsMap.put("handlerId", user.getUserId()); // 流程处理人标识
		if (processName != null && !processName.equals("")) { // 流程名
			paramsMap.put("processName", processName);
		}
		if (businessName != null && !businessName.equals("")) { // 业务名称
			paramsMap.put("businessName", "%" + businessName + "%");
		}
		if (startUserId != null && !startUserId.equals("")) { // 流程创建人
			String[] arr = startUserId.split(",");
			List<String> userIdList = new ArrayList<String>();
			for (int i = 0; i < arr.length; i++) {
				userIdList.add(arr[i]);
			}
			if (userIdList != null && userIdList.size() > 0) {

				paramsMap.put("startUserId", userIdList);
			}
		}
		if (taskType != null && !taskType.equals("")) { // 搜索任务类型
			paramsMap.put("taskType", taskType);
		}
		if (processStatus != null && !processStatus.equals("")) { // 流程状态
			paramsMap.put("processStatus", processStatus);
		}
		if (taskStartDateStart != null && !taskStartDateStart.equals("")) { // 任务接收时间上限
			paramsMap.put("taskStartDateStart", taskStartDateStart);
		}
		if (taskStartDateEnd != null && !taskStartDateEnd.equals("")) { // 任务接收时间下限
			paramsMap.put("taskStartDateEnd", taskStartDateEnd);
		}
		if (taskEndDateStart != null && !taskEndDateStart.equals("")) { // 任务结束时间下限
			paramsMap.put("taskEndDateStart", taskEndDateStart);
		}
		if (taskEndDateEnd != null && !taskEndDateEnd.equals("")) { // 任务结束时间上限
			paramsMap.put("taskEndDateEnd", taskEndDateEnd);
		}
		if (processStartDate != null && !processStartDate.equals("")) { // 流程开始时间
			paramsMap.put("processStartDateStart", processStartDate);
		}
		if (processEndDate != null && !processEndDate.equals("")) { // 流程结束时间
			paramsMap.put("processStartDateEnd", processEndDate);
		}
		if (taskNodeName != null && !taskNodeName.equals("")) { // 任务节点名称
			paramsMap.put("taskNodeName", taskNodeName);
		}
		if (isFind != null && isFind.equals("1")) { // 执行查询操作，页面自动跳转到第一页
			workflowPage.setPageNo(1);
		}
		workflowPage = adapterBaseWorkflowManager.getWorkflowService()
				.getSearchWorkflowByPage(workflowPage, paramsMap);
		List<Object[]> objList1 = new ArrayList<Object[]>();
		List<Object[]> objList2 = new ArrayList<Object[]>();
		User curUser = adapterBaseWorkflowManager.getUserService()
				.getCurrentUser();
		if (workflowPage != null && workflowPage.getResult() != null
				&& workflowPage.getResult().size() > 0) {
			objList1 = workflowPage.getResult();
			Properties properties = new Properties();
			properties.load(new FileInputStream(new ClassPathResource(
					"colorSet.properties").getFile()));
			for (Object[] obj : objList1) { // 把表单的“业务数据”和“流程实例ID”组装，在查看流程查看详情时使用
				String processInstanceId = obj[12].toString();
				Object[] returnObjs = adapterBaseWorkflowManager
						.getWorkflowService().getProcessStatusByPiId(
								processInstanceId);// 得到此流程实例下的运行情况
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
												adapterBaseWorkflowManager
														.getUserService()
														.getUserNameByUserId(id))
										.append(
												"["
														+ adapterBaseWorkflowManager
																.getUserService()
																.getUserDepartmentByUserId(
																		id)
																.getOrgName()
														+ "]").append(",");
							}
							userNames.deleteCharAt(userNames.length() - 1);
						}
						if (userNames.length() > 0) {
							strUserName.append(userNames);
						}
					}
					if (strUserName.toString().equals("")) {
						strUserName.append(curUser.getUserName()).append(
								"["
										+ adapterBaseWorkflowManager
												.getUserService()
												.getUserDepartmentByUserId(
														curUser.getUserId())
												.getOrgName() + "]");
					}
					// obj[6] = obj[6].toString()+strUserName;
					obj[6] = strUserName;
				} else {
					Object processEndDate = obj[9];
					if (processEndDate != null) {
						obj[6] = "办理完毕";
					}
				}
				Object PendDate = obj[9];
				Object PstartDate = obj[7];
				obj = ObjectUtils.addObjectToArray(obj, "");
				StringBuilder picImage = new StringBuilder();
				String rootPath = getRequest().getContextPath();
				String picPath = null;
				String titleShow = null;
				if (PendDate == null) {
					long time = new Date().getTime()
							- ((Date) PstartDate).getTime();
					long hours = time / (1000 * 60 * 60);
					if (hours <= 72) {// 在24小时内
						picPath = properties.getProperty("green");
						titleShow = "3天内办理";
					} else if (hours <= 144) {// 在48小时内
						picPath = properties.getProperty("yellow");
						titleShow = "6天内办理";
					} else {// 超过48小时
						picPath = properties.getProperty("red");
						titleShow = "超过7天办理";
					}
				} else {
					picPath = properties.getProperty("blue");
					titleShow = "办毕";
				}
				picImage
						.append("<img src=\"")
						.append(rootPath)
						.append(picPath)
						.append(
								"\" title=\""
										+ titleShow
										+ "\" style=\"text-align:center;vertical-align:middle;width:15px;height:15px;\"/> ");
				obj[17] = picImage.toString();
				String viewInfoStr = obj[10].toString() + "$"
						+ obj[12].toString();
				obj[10] = viewInfoStr;
				if (obj[4] != null) {// 过滤标题中的换行符 yanjian
					String workflowTitle = (String) obj[4];
					workflowTitle = workflowTitle.replace("\\r\\n", " ");
					workflowTitle = workflowTitle.replace("\\n ", " ");
					obj[4] = workflowTitle;
				}
				objList2.add(obj);
			}
			workflowPage.setResult(objList2);
		}
		return "searchWorkflow";
	}

	public String getBar() {
		Map<String, String> formInfo = manager.getFormInfo(tableName,
				pkFieldName, pkFieldValue);
		StringBuffer sb = new StringBuffer(
				"条码版本标识符:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GB0626-2005<br>");
		if (formInfo != null && !formInfo.isEmpty()) {
			if (formInfo.get("SENDDOC_ID") == null) {
				formInfo.put("SENDDOC_ID", "无");
			}
			sb.append("条码编号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_ID")).append("<br>");
			if (formInfo.get("SENDDOC_SENDER_DEPART_CODE") == null) {
				formInfo.put("SENDDOC_SENDER_DEPART_CODE", "无");
			}
			sb
					.append(
							"发文单位：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_SENDER_DEPART_CODE"))
					.append("<br>")
					.append(
							"公文种类：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;公文")
					.append("<br>");
			if (formInfo.get("SENDDOC_CODE") == null) {
				formInfo.put("SENDDOC_CODE", "无");
			}
			sb.append(
					"发文字号或期号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_CODE")).append("<br>");
			if (formInfo.get("SENDDOC_SUBMITTO_DEPART") == null
					|| "".equals(formInfo.get("SENDDOC_SUBMITTO_DEPART"))) {
				formInfo.put("SENDDOC_SUBMITTO_DEPART", "无");
			}
			sb.append("主送单位:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_SUBMITTO_DEPART")).append(
							"<br>");
			if (formInfo.get("SENDDOC_TITLE") == null) {
				formInfo.put("SENDDOC_TITLE", "无");
			}
			sb
					.append(
							"标题:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_TITLE")).append("<br>");
			if (formInfo.get("SENDDOC_SECRET_LVL") == null) {
				formInfo.put("SENDDOC_SECRET_LVL", "无");
			}
			sb.append("秘密等级:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_SECRET_LVL")).append("<br>");
			if (formInfo.get("SENDDOC_EMERGENCY") == null) {
				formInfo.put("SENDDOC_EMERGENCY", "无");
			}
			sb.append("紧急程度:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_EMERGENCY")).append("<br>");
			if (formInfo.get("SENDDOC_OFFICIAL_TIME") == null) {
				formInfo.put("SENDDOC_OFFICIAL_TIME", "无");
			} else {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				try {
					formInfo.put("SENDDOC_OFFICIAL_TIME", formatter
							.format(formatter.parse(formInfo
									.get("SENDDOC_OFFICIAL_TIME"))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			sb
					.append(
							"成文日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_OFFICIAL_TIME"))
					.append("<br>")
					.append(
							"发布层次:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append("无<br>");
			if (formInfo.get("SENDDOC_ISSUE_DEPART_SIGNED") == null
					|| "".equals(formInfo.get("SENDDOC_ISSUE_DEPART_SIGNED"))) {
				formInfo.put("SENDDOC_ISSUE_DEPART_SIGNED", "无");
			}
			sb
					.append(
							"条码制作单位:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(formInfo.get("SENDDOC_ISSUE_DEPART_SIGNED"))
					.append("<br>");
			Date barcreatedate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String createdate = formatter.format(barcreatedate);
			sb
					.append(
							"条码制作日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append(createdate).append("<br>");
			sb.append("自定义字段:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
					.append("无");
		} else {
			sb.append("-1");
		}
		return this.renderText(sb.toString());
	}

	/**
	 * 查寻所有条形码信息
	 * 
	 * @author:肖利建
	 * @date;2010-8-23 下午14:54:23
	 * @return 所有条码信息
	 * @throw Exception
	 */
	public String getBarParam() throws Exception {
		Map<String, String> formInfo = manager.getFormInfo(tableName,
				pkFieldName, pkFieldValue);
		StringBuffer sb = new StringBuffer("GB0626-2005^");
		if (formInfo != null && !formInfo.isEmpty()) {
			if (formInfo.get("SENDDOC_ID") == null) {
				formInfo.put("SENDDOC_ID", "无");
			}
			sb.append(formInfo.get("SENDDOC_ID")).append("^");

			if (formInfo.get("SENDDOC_SENDER_DEPART_CODE") == null) {
				formInfo.put("SENDDOC_SENDER_DEPART_CODE", "无");
			}
			sb.append(formInfo.get("SENDDOC_SENDER_DEPART_CODE")).append("^")
					.append("公文").append("^");

			if (formInfo.get("SENDDOC_CODE") == null) {
				formInfo.put("SENDDOC_CODE", "无");
			}
			sb.append(formInfo.get("SENDDOC_CODE")).append("^");

			if (formInfo.get("SENDDOC_SUBMITTO_DEPART") == null
					|| "".equals(formInfo.get("SENDDOC_SUBMITTO_DEPART"))) {
				formInfo.put("SENDDOC_SUBMITTO_DEPART", "无");
			}
			sb.append(formInfo.get("SENDDOC_SUBMITTO_DEPART")).append("^");

			if (formInfo.get("SENDDOC_TITLE") == null) {
				formInfo.put("SENDDOC_TITLE", "无");
			}
			sb.append(formInfo.get("SENDDOC_TITLE")).append("^");

			if (formInfo.get("SENDDOC_SECRET_LVL") == null) {
				formInfo.put("SENDDOC_SECRET_LVL", "无");
			}
			sb.append(formInfo.get("SENDDOC_SECRET_LVL")).append("^");

			if (formInfo.get("SENDDOC_EMERGENCY") == null) {
				formInfo.put("SENDDOC_EMERGENCY", "无");
			}
			sb.append(formInfo.get("SENDDOC_EMERGENCY")).append("^");

			if (formInfo.get("SENDDOC_OFFICIAL_TIME") == null) {
				formInfo.put("SENDDOC_OFFICIAL_TIME", "无");
			} else {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				formInfo.put("SENDDOC_OFFICIAL_TIME", formatter
						.format(formatter.parse(formInfo
								.get("SENDDOC_OFFICIAL_TIME"))));
			}
			sb.append(formInfo.get("SENDDOC_OFFICIAL_TIME")).append("^")
					.append("无^");

			if (formInfo.get("SENDDOC_ISSUE_DEPART_SIGNED") == null
					|| "".equals(formInfo.get("SENDDOC_ISSUE_DEPART_SIGNED"))) {
				formInfo.put("SENDDOC_ISSUE_DEPART_SIGNED", "无");
			}
			sb.append(formInfo.get("SENDDOC_ISSUE_DEPART_SIGNED")).append("^");
			Date barcreatedate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String createdate = formatter.format(barcreatedate);
			sb.append(createdate).append("^");
		}
		String cc = sb.toString();
		return this.renderText(cc);
	}

	/**
	 * 插入模板时显示树形结构.
	 * 
	 * @author:邓志城
	 * @date:2010-7-9 下午07:54:47
	 * @return WORD模板类别树
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String templateTree() throws Exception {
		List<ToaDoctemplateGroup> typeLst = tempTypeManager
				.getAllTypeTemplate();// 得到模板所有类别
		Map<Object, List<Object[]>> map = tempItemManager.getTemplateMap();
		List<TempPo> tempPoList = new ArrayList<TempPo>();// 树节点列表
		if (typeLst != null && !typeLst.isEmpty()) {
			for (ToaDoctemplateGroup type : typeLst) {
				if (!"1".equals(type.getDocgroupType())) {
					TempPo po = new TempPo();
					po.setType("type");// 类别节点
					po.setId(type.getDocgroupId());
					po.setName(type.getDocgroupName());
					po.setParentId(type.getDocgroupParentId());
					tempPoList.add(po);
					List<Object[]> tempLst = map.get(po.getId());
					if (tempLst != null && !tempLst.isEmpty()) {
						for (Object[] obj : tempLst) {
							TempPo templatePo = new TempPo();
							templatePo.setId(obj[0].toString());
							templatePo.setName((String) obj[1]);
							templatePo.setParentId(po.getId());
							templatePo.setType("item");// 模板节点
							tempPoList.add(templatePo);
						}
					}
				}
			}
		}
		getRequest().setAttribute("typeList", tempPoList);
		return "templatetree";
	}

	/**
	 * 办理任务时进行CA认证,认证成功后跳转到input方法
	 * 
	 * @author:邓志城
	 * @date:2011-4-2 下午03:47:55
	 * @modyfy：严建 2011-11024 11:50 添加系统全局设置是否启用的ca认证的处理
	 * @return
	 */
	public String CASign() throws Exception {
		if (taskId != null && !"".equals(taskId)) {
			Object[] objs = manager.getNodeWorkflowPluginInfoByTaskId(taskId);// 得到插件信息
			pluginInfo = objs[0].toString();

			ToaSystemset systemset = adapterBaseWorkflowManager
					.getSystemsetManager().getSystemset(); // 获取系统全局配置信息

			if (systemset.getIsUseCASign() == null
					|| systemset.getIsUseCASign().equals("")) {
				systemset.setIsUseCASign("0");
			}
			if (systemset.getIsUseCASign().equals("0")) {// 如果系统全局设置没有启用ca认证
				// ，系统直接调用input方法
				return input();
			}
			TwfBaseNodesetting setting = (TwfBaseNodesetting) objs[1];
			if (setting != null) {
				isNeedSign = setting.getPlugin("plugins_chkNeedSign");// 返回节点上设置的是否需要签名认证设置信息
				if ("1".equals(isNeedSign)) {
					String num = "1234567890abcdefghijklmnopqrstopqrstuvwxyz";
					int size = 6;
					char[] charArray = num.toCharArray();
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < size; i++) {
						sb
								.append(charArray[((int) (Math.random() * 10000) % charArray.length)]);
					}
					getRequest().getSession().setAttribute("original_data",
							sb.toString());
				} else {
					getRequest().getSession().removeAttribute("original_data");
					return input();
				}
			}
		}
		return "checksign";// 页面跳转到CA认证页面
	}

	/**
	 * 执行CA认证
	 * 
	 * @author:邓志城
	 * @date:2011-4-2 下午03:57:21
	 * @return
	 */
	public String doCASign() {
		String ret = "0";
		try {
			boolean isSuccess = new CAAuth().auth(getRequest().getSession(),
					getRequest());
			if (!isSuccess) {
				ret = "1";
			}
		} catch (Exception e) {
			logger.error("CA认证发生错误", e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

	/**
	 * 签收待签收文件
	 * 
	 * @author:肖利建
	 * @date:2012-12-30 下午12:45:01
	 * @return
	 */
	public String singdoc() throws Exception {
		manager.signForTask(taskId, "0", new OALogInfo("签收任务"));
		return this.renderText("1");
	}

	/**
	 * 电子表单处理界面 支持新建流程和办理流程
	 */
	@Override
	public String input() throws Exception {
		if (formId != null && !"".equals(formId)) {
			TEJsptemplate jspForm = jspManager.getFormTemplateInfo(formId);// 通过formId来查找jsp表单
			if (jspForm != null) { // 如果是jsp表单的话，得到url,直接跳转到配置的url
				jspInput(formId);
			}

		}

		HttpSession session = getSession();
		timeout = session.getMaxInactiveInterval();
		ToaSystemset systemset = adapterBaseWorkflowManager
				.getSystemsetManager().getSystemset(); // 获取系统全局配置信息
		suggestionStyle = systemset.getSuggestionStyle(); // 得到意见输入模式
		if (workflowName == null || workflowName.equals("")) {
			if (pkFieldValue != null && !pkFieldValue.equals("")) {
				Map map = manager.getSystemField(pkFieldValue, tableName);
				workflowName = (String) map
						.get(BaseWorkflowManager.WORKFLOW_NAME);// 得到流程名
			}
		}
		TwfBaseNodesetting nodeSetting = manager.getOperationHtml(taskId,
				workflowName);
		nodeName = nodeSetting.getNsNodeName();
		returnFlag = nodeSetting.getNsFormPrivInfo();
		privilegeInfo = adapterBaseWorkflowManager.getWorkflowService()
				.getDocumentPrivilege(nodeSetting);
		User user = adapterBaseWorkflowManager.getUserService()
				.getCurrentUser();// 得到当前用户
		userId = user.getUserId();
		getRequest().setAttribute("currentId", userId);// 给表单页面加一个参数 userId.
		userName = user.getUserName();// 当前用户姓名
		Organization organization = adapterBaseWorkflowManager.getUserService()
				.getDepartmentByLoginName(user.getUserLoginname());// 得到当前用户所属单位
		orgName = organization.getOrgName();// 当前用户所属单位名称
		if (taskId == null || "".equals(taskId)) {// 草稿
			if (nodeSetting != null) {// 流程启动表单取第一个节点上的表单
				formId = nodeSetting.getNsNodeFormId().toString();
				String strFlag = (String) nodeSetting
						.getPlugin("plugins_businessFlag");// 得到当前节点设置的域信息
				if (strFlag != null && !"".equals(strFlag)) {
					strFlag = URLDecoder.decode(strFlag, "utf-8");
					strFlag = strFlag.replaceAll("#", ",");
					JSONArray jsonArray = JSONArray.fromObject(strFlag);
					JSONObject fbj = jsonArray.getJSONObject(0);
					if (fbj.containsKey("type")) {// 输入意见字段
						jsonArray.remove(fbj);
						firstNodeControlName = fbj.getString("fieldName");
					}
				}
				/** 第一个任务节点 根据节点id获取插件信息 */
				String nodeId = nodeSetting.getNsNodeId() + "";
				Object[] objs = manager
						.getNodeWorkflowPluginInfoByNodeId(nodeId);// 得到插件信息
				pluginInfo = objs[0].toString();
			}
			// taoji 新建征求意见不显示意见征询反馈栏
			getRequest().setAttribute("fankui", "1");
			if (pkFieldValue == null || "".equals(pkFieldValue)) {// 新建
				user = adapterBaseWorkflowManager.getUserService()
						.getUserInfoByUserId(user.getUserId());
				userJob = user.getUserDescription();// 得到用户职务
				if (userJob == null) {
					userJob = "";
				}
				tableName = adapterBaseWorkflowManager.getEformService()
						.getTable(formId);
				String codeId = definitionPluginService
						.getRuleCodeIdByWorkflowName(workflowName);
				if (codeId != null) {
					workflowCode = codeService.getCodeToFlow(codeId);
					logger
							.info("流程[" + workflowName + "]编码规则为："
									+ workflowCode);
				}
			} else {// 修改
				if (pkFieldName == null || "".equals(pkFieldName)) {
					pkFieldName = manager.getPrimaryKeyName(tableName);// 得到主键名称
				}
			}
		} else {
			// 判断流程主办人员是否是当前用户
			flag = adapterBaseWorkflowManager.getWorkflowService()
					.hasMainDoing(userId, taskId)
					+ "";
			String taskFlag = getRequest().getParameter("taskFlag");// 任务是否需要校验
			if (taskFlag != null && "notNeedCheck".equals(taskFlag)) {

			} else {
				if (!manager.isTaskInCurrentUser(taskId)) {
					return super.renderJavaScript("该任务已不在您的待办事宜列表中。");
				}
			}
			// 任务是否允许办理
			String ret = manager.judgeTaskIsDone(taskId);
			String[] rets = ret.split("\\|");
			String retCode = rets[0];
			if (retCode.equals("f1")) {
				return super.renderJavaScript("该任务正在被其他人处理或被挂起，请稍后再试或与管理员联系。");
			} else if (retCode.equals("f2")) {
				return super.renderJavaScript("该任务已被取消，请查阅处理记录或与管理员联系。");
			} else if (retCode.equals("f3")) {
				return super.renderJavaScript("该任务已被其他人处理，请查阅详细处理记录。");
			} else {
				isReceived = adapterBaseWorkflowManager.getWorkflowService()
						.getTaskInstanceById(taskId).getIsReceive();
				TaskInstance taskInstance = adapterBaseWorkflowManager
						.getWorkflowService().getTaskInstanceById(taskId);
				getRequest().setAttribute("isBackspace",
						taskInstance.getIsBackspace());
				ProcessInstance processInstance = taskInstance
						.getProcessInstance();
				workflowName = processInstance.getName();
				instanceId = processInstance.getId() + "";
				businessName = processInstance.getBusinessName();
				JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
				if (jsonArray != null && !jsonArray.isEmpty()) {
					personDemo = jsonArray.toString();
					parentInstanceId = "exists";
				} else {
					String demo = (String) processInstance.getContextInstance()
							.getVariable("@{personDemo}");
					String parentinstanceId = "";
					if (!"".equals(demo) && null != demo) {
						String[] demos = demo.split(";");
						if (demos.length > 4) {
							parentinstanceId = demos[4];
							parentinstanceId = parentinstanceId.split("@")[0];
							jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
							personDemo = jsonArray.toString();
							parentInstanceId = "exists";
						}
					}
				}
				String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
				formId = info[1];
				bussinessId = info[0];
				if ("".equals(formId) || formId == null || "0".equals(formId)) {// 表单模板不存在,即流程未挂接表单,如栏目流程
					return this.nextstep();
				}
				if (!adapterBaseWorkflowManager.getWorkflowService()
						.isSignNodeTask(null, nodeSetting)) {// 签收节点查看时不签收任务
					if ("1".equals(taskInstance.getIsReceive())) {

					} else {// 肖利建--签收的在这里加if判断
						if (!"公文转办流程".equals(workflowName)
								&& !"征求意见流程".equals(workflowName)) {
							manager.signForTask(taskId, "0", new OALogInfo(
									"签收任务"));
						}
					}
				}
				if (!"0".equals(bussinessId)) {
					String[] args = bussinessId.split(";");
					tableName = args[0];
					pkFieldName = args[1];
					pkFieldValue = args[2];
				}
			}
		}
		sendDocUploadManager.initViewByNodeSetting(nodeSetting);
		/*
		 * 控制公文传输转来的流程限制 tj String t = getRequest().getParameter("gwcs");
		 * if("gwcs".equals(t)){ return "gwcs"; }
		 */
		return INPUT;
	}

	/**
	 * jsp表单的input
	 */
	public String jspInput(String formId) throws Exception {
		HttpSession session = getSession();
		timeout = session.getMaxInactiveInterval();
		ToaSystemset systemset = adapterBaseWorkflowManager
				.getSystemsetManager().getSystemset(); // 获取系统全局配置信息
		suggestionStyle = systemset.getSuggestionStyle(); // 得到意见输入模式
		if (workflowName == null || workflowName.equals("")) {
			if (pkFieldValue != null && !pkFieldValue.equals("")) {
				Map map = manager.getSystemField(pkFieldValue, tableName);
				workflowName = (String) map
						.get(BaseWorkflowManager.WORKFLOW_NAME);// 得到流程名
			}
		}
		TwfBaseNodesetting nodeSetting = manager.getOperationHtml(taskId,
				workflowName);
		nodeName = nodeSetting.getNsNodeName();
		returnFlag = nodeSetting.getNsFormPrivInfo();
		privilegeInfo = adapterBaseWorkflowManager.getWorkflowService()
				.getDocumentPrivilege(nodeSetting);
		User user = adapterBaseWorkflowManager.getUserService()
				.getCurrentUser();// 得到当前用户
		userId = user.getUserId();
		userName = user.getUserName();// 当前用户姓名
		Organization organization = adapterBaseWorkflowManager.getUserService()
				.getDepartmentByLoginName(user.getUserLoginname());// 得到当前用户所属单位
		orgName = organization.getOrgName();// 当前用户所属单位名称
		if (taskId == null || "".equals(taskId)) {// 草稿
			if (nodeSetting != null) {// 流程启动表单取第一个节点上的表单
				formId = nodeSetting.getNsNodeFormId().toString();
				String strFlag = (String) nodeSetting
						.getPlugin("plugins_businessFlag");// 得到当前节点设置的域信息
				if (strFlag != null && !"".equals(strFlag)) {
					strFlag = URLDecoder.decode(strFlag, "utf-8");
					strFlag = strFlag.replaceAll("#", ",");
					JSONArray jsonArray = JSONArray.fromObject(strFlag);
					JSONObject fbj = jsonArray.getJSONObject(0);
					if (fbj.containsKey("type")) {// 输入意见字段
						jsonArray.remove(fbj);
						firstNodeControlName = fbj.getString("fieldName");
					}
				}
				/** 第一个任务节点 根据节点id获取插件信息 */
				String nodeId = nodeSetting.getNsNodeId() + "";
				Object[] objs = manager
						.getNodeWorkflowPluginInfoByNodeId(nodeId);// 得到插件信息
				pluginInfo = objs[0].toString();
			}
			// taoji 新建征求意见不显示意见征询反馈栏
			getRequest().setAttribute("fankui", "1");
			if (pkFieldValue == null || "".equals(pkFieldValue)) {// 新建
				user = adapterBaseWorkflowManager.getUserService()
						.getUserInfoByUserId(user.getUserId());
				userJob = user.getUserDescription();// 得到用户职务
				if (userJob == null) {
					userJob = "";
				}
				tableName = adapterBaseWorkflowManager.getEformService()
						.getTable(formId);
				String codeId = definitionPluginService
						.getRuleCodeIdByWorkflowName(workflowName);
				if (codeId != null) {
					workflowCode = codeService.getCodeToFlow(codeId);
					logger
							.info("流程[" + workflowName + "]编码规则为："
									+ workflowCode);
				}
			} else {// 修改
				if (pkFieldName == null || "".equals(pkFieldName)) {
					pkFieldName = manager.getPrimaryKeyName(tableName);// 得到主键名称
				}
			}
		} else {
			// 判断流程主办人员是否是当前用户
			flag = adapterBaseWorkflowManager.getWorkflowService()
					.hasMainDoing(userId, taskId)
					+ "";
			String taskFlag = getRequest().getParameter("taskFlag");// 任务是否需要校验
			if (taskFlag != null && "notNeedCheck".equals(taskFlag)) {

			} else {
				if (!manager.isTaskInCurrentUser(taskId)) {
					return super.renderJavaScript("该任务已不在您的待办事宜列表中。");
				}
			}
			// 任务是否允许办理
			String ret = manager.judgeTaskIsDone(taskId);
			String[] rets = ret.split("\\|");
			String retCode = rets[0];
			if (retCode.equals("f1")) {
				return super.renderJavaScript("该任务正在被其他人处理或被挂起，请稍后再试或与管理员联系。");
			} else if (retCode.equals("f2")) {
				return super.renderJavaScript("该任务已被取消，请查阅处理记录或与管理员联系。");
			} else if (retCode.equals("f3")) {
				return super.renderJavaScript("该任务已被其他人处理，请查阅详细处理记录。");
			} else {
				isReceived = adapterBaseWorkflowManager.getWorkflowService()
						.getTaskInstanceById(taskId).getIsReceive();
				TaskInstance taskInstance = adapterBaseWorkflowManager
						.getWorkflowService().getTaskInstanceById(taskId);
				getRequest().setAttribute("isBackspace",
						taskInstance.getIsBackspace());
				ProcessInstance processInstance = taskInstance
						.getProcessInstance();
				workflowName = processInstance.getName();
				instanceId = processInstance.getId() + "";
				businessName = processInstance.getBusinessName();
				JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
				if (jsonArray != null && !jsonArray.isEmpty()) {
					personDemo = jsonArray.toString();
					parentInstanceId = "exists";
				} else {
					String demo = (String) processInstance.getContextInstance()
							.getVariable("@{personDemo}");
					String parentinstanceId = "";
					if (!"".equals(demo) && null != demo) {
						String[] demos = demo.split(";");
						if (demos.length > 4) {
							parentinstanceId = demos[4];
							parentinstanceId = parentinstanceId.split("@")[0];
							jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
							personDemo = jsonArray.toString();
							parentInstanceId = "exists";
						}
					}
				}
				String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
				formId = info[1];
				bussinessId = info[0];
				if ("".equals(formId) || formId == null || "0".equals(formId)) {// 表单模板不存在,即流程未挂接表单,如栏目流程
					return this.nextstep();
				}
				if (!adapterBaseWorkflowManager.getWorkflowService()
						.isSignNodeTask(null, nodeSetting)) {// 签收节点查看时不签收任务
					if ("1".equals(taskInstance.getIsReceive())) {

					} else {// 肖利建--签收的在这里加if判断
						if (!"公文转办流程".equals(workflowName)
								&& !"征求意见流程".equals(workflowName)) {
							manager.signForTask(taskId, "0", new OALogInfo(
									"签收任务"));
						}
					}
				}
				if (!"0".equals(bussinessId)) {
					String[] args = bussinessId.split(";");
					tableName = args[0];
					pkFieldName = args[1];
					pkFieldValue = args[2];
				}
			}
		}
		sendDocUploadManager.initViewByNodeSetting(nodeSetting);
		/*
		 * 控制公文传输转来的流程限制 tj String t = getRequest().getParameter("gwcs");
		 * if("gwcs".equals(t)){ return "gwcs"; }
		 */
		return INPUT;
	}

	/**
	 * @description
	 * @method reTodo
	 * @author 申仪玲(shenyl)
	 * @created 2012-4-17 下午03:50:07
	 * @return
	 * @throws Exception
	 * @return String
	 * @throws Exception
	 */
	public String reTodo() throws Exception {
		OtherParameter otherparameter = initOtherParameter();
		String busId = manager.copyData(bussinessId);
		String[] result = manager.reTodo(formId, businessName, busId,
				workflowName, otherparameter, new OALogInfo("重新办理["
						+ businessName + "]"));
		instanceId = result[0];
		taskId = result[1];
		return this.renderText(instanceId + "@" + taskId);
	}

	/**
	 * @description 是否设置了显示流程简图
	 * @author 严建
	 * @createTime Nov 20, 2011 4:20:25 PM
	 * @return String
	 * @modify yanjian 2011-12-16 办理状态是否不显示流程图
	 */
	@SuppressWarnings("unchecked")
	public String isSetSimplePDImageView() throws Exception {
		String instanceid = getRequest().getParameter("instanceId");// 实例id
		String result = sendDocUploadManager
				.getWorkStatePDImageViewInfo(instanceid);
		return this.renderText(result);
	}

	/**
	 * 
	 * @Title:SimplePDImageView
	 * @Description:查看代办事宜流程简图
	 * @Param
	 * @Author:hecj
	 * @Return
	 * @Throws
	 */
	public String simplePDImageView() throws Exception {
		String instanceid = getRequest().getParameter("instanceId");// 实例id
		Object[] obj = adapterBaseWorkflowManager.getWorkflowService()
				.getProcessStatusByPiId(instanceid);
		Collection col = (Collection) obj[6];
		String findnode = "";
		if (col != null && !col.isEmpty()) {// 遍历运行情况，获得运行节点名称
			for (Iterator it = col.iterator(); it.hasNext();) {
				Object[] subTask = (Object[]) col.iterator().next();
				String flag = subTask[0].toString();
				if ("task".equals(flag)) {
					findnode = subTask[1].toString();
					break;
				}
			}
		}
		List<com.strongit.workflow.bo.TwfBaseNodesetting> lst = adapterBaseWorkflowManager
				.getWorkflowService().getNodeInfosByProcessName(
						obj[1].toString());
		String tmpdesp = "";
		String tmporderid = "";
		String curdesp = "";// 当前运行节点的描述信息
		String curid = "";// 当前运行节点的排序号
		List<Object[]> nodes = new ArrayList<Object[]>();
		List<String> checkLst = new ArrayList<String>();
		if (lst != null && !lst.isEmpty()) {
			for (TwfBaseNodesetting node : lst) {
				Map map = node.getPlugins();
				TwfBaseNodesettingPlugin plugin = (TwfBaseNodesettingPlugin) map
						.get("plugins_description");
				TwfBaseNodesettingPlugin plugin2 = (TwfBaseNodesettingPlugin) map
						.get("plugins_orderid");
				if (plugin != null & plugin2 != null) {
					tmpdesp = (String) plugin.getValue();
					tmporderid = (String) plugin2.getValue();
					if (findnode.equals(node.getNsNodeName())) {
						curdesp = tmpdesp;
						curid = tmporderid;
					}
					if (tmpdesp != null && tmporderid != null) {
						String str = tmpdesp + tmporderid;
						if (!checkLst.contains(str)) {
							String strtmp = node.getNsTaskActors();
							String actor = "";
							if (strtmp == null) {
								actor = "";
							} else {
								if (strtmp.indexOf("|") > 0) {
									String[] arr = strtmp.split("\\|");
									for (int i = 0; i < arr.length; i++) {
										actor = actor
												+ arr[i].substring(arr[i]
														.indexOf(",") + 1)
												+ ",";
									}
									if (",".equals(actor.substring(actor
											.length() - 1, actor.length()))) {
										actor = actor.substring(0, actor
												.length() - 1);
									}
								} else {
									actor = strtmp.substring(strtmp
											.indexOf(",") + 1);
								}
							}
							// 判断是否有处理时间，如果有，则获取已办理的处理人，替换之前的actor
							String ender = "";
							if (findnode.equals(node.getNsNodeName())) {
								List nodelist = TaskService
										.getCurrentHandleByNode(instanceid,
												node.getNsNodeId().toString());
								if (nodelist != null) {
									for (int n = 0; n < nodelist.size(); n++) {
										Object[] tmpobj = (Object[]) nodelist
												.get(n);
										if (tmpobj[1] != null
												&& !"".equals(tmpobj[1]
														.toString())) {
											ender = ender
													+ tmpobj[1].toString()
													+ ",";// 如果已处理则将之前的处理人改成实际处理人
										}
									}
								}
								if (!"".equals(ender)) {
									if (",".equals(ender.substring(ender
											.length() - 1, ender.length()))) {
										ender = ender.substring(0, ender
												.length() - 1);
									}
								}
							} else {
								List nodelist = TaskService
										.getHandleRecordByNode(instanceid, node
												.getNsNodeId().toString());
								if (nodelist != null) {
									for (int n = 0; n < nodelist.size(); n++) {
										Object[] tmpobj = (Object[]) nodelist
												.get(n);
										if (tmpobj[4] != null) {
											ender = ender
													+ tmpobj[2].toString()
													+ ",";// 如果已处理则将之前的处理人改成实际处理人
										}
									}
								}
								if (!"".equals(ender)) {
									if (",".equals(ender.substring(ender
											.length() - 1, ender.length()))) {
										ender = ender.substring(0, ender
												.length() - 1);
									}
								}
							}
							if (!"".equals(ender)) {
								nodes.add(new Object[] { tmpdesp, tmporderid,
										node.getNsNodeName(), ender, 1 });// 已处理过的节点
							} else {
								nodes.add(new Object[] { tmpdesp, tmporderid,
										node.getNsNodeName(), actor, 0 });// 未处理的节点
							}
							checkLst.add(str);
						}
					}
				}
			}
		}
		// 对于排序号相同描述信息不同的放到同一个list中
		List<List<Object[]>> newlist = new ArrayList<List<Object[]>>();
		Map<String, List<Object[]>> tempMap = new HashMap<String, List<Object[]>>();
		Object[] defin;
		String orderId;
		// List<Object[]> list = new ArrayList<Object[]>();
		for (int t = 0; t < nodes.size(); t++) {
			// list.clear();
			defin = nodes.get(t);
			orderId = defin[1].toString();
			if (tempMap.get(orderId) == null) {
				List<Object[]> list = new ArrayList<Object[]>();
				list.add(defin);
				tempMap.put(orderId, list);
			} else {
				List<Object[]> list = new ArrayList<Object[]>();
				list = tempMap.get(orderId);
				list.add(defin);
			}
		}
		newlist.addAll(tempMap.values());
		// 按排序号进行排序
		Collections.sort(newlist, new Comparator<List<Object[]>>() {
			public int compare(List<Object[]> node1, List<Object[]> node2) {
				String n1 = (String) node1.get(0)[1];
				String n2 = (String) node2.get(0)[1];
				Long l1 = Long.MAX_VALUE;
				Long l2 = Long.MAX_VALUE;
				if (n1 != null && !"".equals(n1)) {
					l1 = Long.parseLong(n1);
				}
				if (n2 != null && !"".equals(n2)) {
					l2 = Long.parseLong(n2);
				}
				return l1.compareTo(l2);
			}
		});
		getRequest().setAttribute("nodes", newlist);
		getRequest().setAttribute("curdesp", curdesp);
		getRequest().setAttribute("curid", curid);
		return "simple";
	}

	/**
	 * 查阅公文
	 * 
	 * @author 邓志城
	 * @date 2009年5月6日8:56:34
	 */
	public String viewDoc() throws Exception {
		splitBussinessId();
		return "viewdoc";
	}

	/**
	 * 删除草稿 多条记录以逗号隔开
	 * 
	 * @return ret: 0:操作成功 -1：操作异常
	 */
	public String delete() throws Exception {
		String ret = "";
		try {
			String[] docIds = pkFieldValue.split(",");
			StringBuilder title = new StringBuilder();
			for (String id : docIds) {
				Map map = manager.getSystemField(id, tableName);
				title.append(map.get(BaseWorkflowManager.WORKFLOW_TITLE))
						.append(",");
			}
			if (title.length() > 0) {
				title.deleteCharAt(title.length() - 1);
			}
			manager.delete(pkFieldValue, tableName, new OALogInfo(getText(
					GlobalBaseData.SENDDOC_DELETE_DOC, new String[] {
							adapterBaseWorkflowManager.getUserService()
									.getCurrentUser().getUserName(),
							title.toString() })));
			ret = "0";
		} catch (Exception e) {
			logger.error("删除草稿时发生异常", e);
			ret = "-1";
		}
		return this.renderText(ret);

	}

	/**
	 * 流程草稿列表
	 * 
	 * @author:邓志城
	 * @date:2009-12-18 上午10:49:31
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String draft() throws Exception {
		try {
			if (!"1".equals(showSignUserInfo)) {// 不是已分办 yanijan
				String currworkflowType = workflowType;
				handleKind = "0";
				if ("3".equals(currworkflowType)) {
					handleKind = "2";
				} else if ("2".equals(currworkflowType)) {
					handleKind = "1";
				}
			}
			Object[] obj = manager.getInfoItemPage(page, workflowName, formId,
					true);
			List list = new ArrayList((List) obj[1]);
			showColumnList = (List) obj[0];
			queryColumnList = (List<EFormComponent>) obj[2];
			tableName = (String) obj[3];
			showColumnList
					.add(new String[] { "DOCDATE", "日期", "5", "DOCDATE" });// 日期
			for (Object objs : list) {
				Map objstemp = (Map) objs;
				objstemp.put("DOCDATE", new Date());
			}
			page = ListUtils.splitList2Page(page, list);
		} catch (Exception e) {
			logger.error("获取草稿列表时发生异常", e);
		}
		return HandleKindConst.getReturnString(handleKind, "draft");
	}

	/**
	 * @method repealProcess
	 * @author 申仪玲
	 * @created 2011-12-12 下午05:28:14
	 * @description 废除公文，挂起流程
	 * @return String 返回类型
	 */
	public String repealProcess() throws Exception {
		boolean res = adapterBaseWorkflowManager.getWorkflowService()
				.repealProcess(instanceId, "1",
						new OALogInfo("废除公文[" + businessName + "]"));
		List<Object[]> ret = manager
				.getFormIdAndBusinessIdByProcessInstanceId(String
						.valueOf(instanceId));// 2012-12-19添加，用来更改业务表数据FCZT，供报表查询使用
		if (ret != null && ret.size() > 0) {
			Object[] obj = ret.get(0);
			String bussinessIdfc = obj[1].toString(); // 表名;主键;主键值
			if (bussinessIdfc != null && !bussinessIdfc.equals("")) {
				manager.updateFeichuDate(bussinessIdfc, "1");
			}
		}
		if (res == true) {
			// 修改意见征询状态
			if (bussinessId != null && !"".equals(bussinessId)) {
				adapterBaseWorkflowManager.getWorkflowService().suspendYjzx(
						bussinessId);
			}
			return renderHtml("true");
		} else {
			return renderHtml("false");
		}
	}

	/**
	 * @method returnProcess
	 * @author 申仪玲
	 * @created 2011-12-12 下午10:05:10
	 * @description 还原挂起的流程
	 * @return boolean 返回类型
	 */
	public String returnProcess() throws Exception {
		ContextInstance cxt;
		String[] intId = instanceId.split(",");
		String ret = "true";
		try {
			for (int i = 0; i < intId.length; i++) {
				Object[] objs = this.getManager()
						.getFormIdAndBusinessIdByProcessInstanceId(intId[i])
						.get(0);
				cxt = adapterBaseWorkflowManager.getWorkflowService()
						.getContextInstance(intId[i]);
				businessName = String.valueOf(cxt
						.getVariable(WorkflowConst.WORKFLOW_BUSINESSNAME));
				if (objs != null) {
					bussinessId = objs[1].toString();
					if (bussinessId != null && !bussinessId.equals("")) {
						manager.updateFeichuDate(bussinessId, "0");// 2012-12-19添加，用来更改业务表数据FCZT，供报表查询使用
					}
				}
				boolean res = adapterBaseWorkflowManager.getWorkflowService()
						.returnProcess(intId[i], "2",
								new OALogInfo("还原公文[" + businessName + "]"));
				if (res == true) {
					if (bussinessId != null && !"".equals(bussinessId)) {
						adapterBaseWorkflowManager.getWorkflowService()
								.resumeYjzx(bussinessId);
					}
					ret = "true";
				} else {
					ret = "false";
					return renderHtml(ret);
				}
			}
		} catch (Exception e) {
			logger.error("还原流程时发生异常", e);
			ret = "false";
		}
		return renderHtml(ret);
	}

	/**
	 * @method delProcess
	 * @author 申仪玲
	 * @created 2012-1-7 上午11:54:19
	 * @description 描述
	 * @return String 删除挂起的流程
	 */
	public String delProcess() throws Exception {
		boolean res = adapterBaseWorkflowManager.getWorkflowService()
				.delProcess(instanceId);

		/*
		 * 删除流程实例时，把关联流程的流程实例删除
		 */
		String[] piIds = instanceId.split(",");
		for (String piId : piIds) {
			Long id = Long.valueOf(piId);
			rwSrv.deleteRelativeWorkflowByPiId(id);
		}

		if (res == true) {
			return renderHtml("true");
		} else {
			return renderHtml("false");
		}
	}

	/**
	 * 得到待办流程列表 支持自定义查询以及自定义显示.
	 * 
	 * 郑志斌 20110816 根据流程标题最大长度，动态显示：流程标题、拟稿人、办理日期、任务类型区域太长
	 */
	@SuppressWarnings("unchecked")
	public String todo() throws Exception {
		String workflowflag = ServletActionContext.getRequest().getParameter(
				"workflowflag");
		isReceived = ServletActionContext.getRequest().getParameter(
				"isReceived");
		if ("1".equals(workflowflag)) {
			workflowName = "公文转办流程";
		} else if ("0".equals(workflowflag)) {
			workflowName = "征求意见流程";
		} else {
			// workflowName = "";
			// 前台桌面显示问题修改 taoji
			if (workflowName == null) {
				workflowName = "";
			}
		}
		logger
				.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.todo() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理待办流程列表显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		try {
			ProcessedParameter parameter = new ProcessedParameter();
			String currworkflowType = workflowType;
			handleKind = "0";
			if ("3".equals(currworkflowType)) {
				handleKind = "2";
			} else if ("2".equals(currworkflowType)) {
				handleKind = "1";
			}
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setWorkflowType(workflowType);
			parameter.setUserName(userName);
			parameter.setIsBackSpace(isBackSpace);
			String fromLeaderDesktopPage = getRequest().getParameter(
					"fromLeaderDesktopPage");// "1"代表页面来自领导首页，则取本周时间
			if (fromLeaderDesktopPage != null
					&& "1".equals(fromLeaderDesktopPage)) {
				Date now = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				if (calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0) { // 如果是星期日
					// 则向前退一天
					now.setTime(now.getTime() - 24 * 60 * 60 * 1000);
					calendar.setTime(now);
				}
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				Date date = calendar.getTime();
				date.setHours(0);
				date.setMinutes(0);
				date.setSeconds(0);
				parameter.setStartDate(date);
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
				date = calendar.getTime();
				date.setHours(23);
				date.setMinutes(59);
				date.setSeconds(59);
				parameter.setEndDate(date);
			} else {
				parameter.setStartDate(startDate);
				parameter.setEndDate(endDate);
			}
			parameter.setType(type);
			parameter.setTaskIdList(null);
			parameter.setHandleKind(handleKind);
			parameter.setOrgId(roomId);// 根据处室id取该处室全部(待签收文件)
			Object[] obj = manager.getTodo(page, parameter);
			if (obj != null) {
				List list = (List) obj[1];
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
							workFlowTitle = workFlowTitle
									.replace("\\r\\n", " ");
							workFlowTitle = workFlowTitle.replace("\\n", " ");
						}
						// 列表加载图标
						sendDocIcoManager.todoGridViewIco(map);
						// StringBuilder picImage = new StringBuilder();
						// String rootPath = getRequest().getContextPath();
						// sendDocIcoManager.gridViewRedYellowGreenIco(picImage,
						// (Date) map.get("processstartdate"), rootPath);
						// // 显示公文期限图标
						// sendDocIcoManager.gridViewTimeOutIco(picImage, map
						// .get("taskStartDate"), map.get("timeOut"),
						// rootPath);
						//
						// // 通过标识区分正常办理文、代办文、退文
						// sendDocIcoManager.gridViewBackspaceIco(picImage,
						// (String) map.get("isBackspace"), (String)
						// map.get("taskId"), rootPath);
						// sendDocIcoManager.gridViewAssignTypeIco(picImage,
						// (String) map.get("assignType"), rootPath);
						// if (!map.containsKey("png")) {
						// map.put("png", picImage.toString());
						// }
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 20 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 20 * (columnSize - 1);
							otherLength = 20;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取待办事宜时发生异常", e);
		}
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"
				+ (System.currentTimeMillis() - start) + "ms{@@@@@@}");
		return HandleKindConst.getReturnString(handleKind, "todo");
	}

	/**
	 * 得到我的退回文件列表 支持自定义查询以及自定义显示.
	 * 
	 * 郑志斌 20110816 根据流程标题最大长度，动态显示：流程标题、拟稿人、办理日期、任务类型区域太长
	 */
	@SuppressWarnings("unchecked")
	public String myReturn() throws Exception {
		String workflowflag = ServletActionContext.getRequest().getParameter(
				"workflowflag");
		isReceived = ServletActionContext.getRequest().getParameter(
				"isReceived");
		if ("1".equals(workflowflag)) {
			workflowName = "公文转办流程";
		} else if ("0".equals(workflowflag)) {
			workflowName = "征求意见流程";
		} else {
			// workflowName = "";
			// 前台桌面显示问题修改 taoji
			if (workflowName == null) {
				workflowName = "";
			}
		}
		logger
				.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.todo() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理待办流程列表显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		try {
			ProcessedParameter parameter = new ProcessedParameter();
			String currworkflowType = workflowType;
			handleKind = "0";
			if ("3".equals(currworkflowType)) {
				handleKind = "2";
			} else if ("2".equals(currworkflowType)) {
				handleKind = "1";
			}
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setWorkflowType(workflowType);
			parameter.setUserName(userName);
			parameter.setType(type);
			parameter.setTaskIdList(null);
			parameter.setHandleKind(handleKind);
			Object[] obj = manager.getMyReturnList(page, parameter);
			if (obj != null) {
				List list = (List) obj[1];
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
							workFlowTitle = workFlowTitle
									.replace("\\r\\n", " ");
							workFlowTitle = workFlowTitle.replace("\\n", " ");
						}
						// 列表加载图标
						sendDocIcoManager.todoGridViewIco(map);
						// StringBuilder picImage = new StringBuilder();
						// String rootPath = getRequest().getContextPath();
						// sendDocIcoManager.gridViewRedYellowGreenIco(picImage,
						// (Date) map.get("processstartdate"), rootPath);
						// // 显示公文期限图标
						// sendDocIcoManager.gridViewTimeOutIco(picImage, map
						// .get("taskStartDate"), map.get("timeOut"),
						// rootPath);
						//
						// // 通过标识区分正常办理文、代办文、退文
						// sendDocIcoManager.gridViewBackspaceIco(picImage,
						// (String) map.get("isBackspace"), (String)
						// map.get("taskId"), rootPath);
						// sendDocIcoManager.gridViewAssignTypeIco(picImage,
						// (String) map.get("assignType"), rootPath);
						// if (!map.containsKey("png")) {
						// map.put("png", picImage.toString());
						// }
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 20 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 20 * (columnSize - 1);
							otherLength = 20;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取待办事宜时发生异常", e);
		}
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"
				+ (System.currentTimeMillis() - start) + "ms{@@@@@@}");
		return HandleKindConst.getReturnString(handleKind, "myReturn");
	}

	/**
	 * 查看废除公文
	 * 
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Jul 27, 2012 7:02:42 PM
	 */
	public String viewrepeal() throws Exception {
		try {
			// 判断流程主办人员是否是当前用户
			if ("null".equals(taskId)) {
				taskId = null;
			}
			String curUserId = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser().getUserId();
			flag = adapterBaseWorkflowManager.getWorkflowService()
					.getMainActorIdByProcessInstanceId(instanceId).equals(
							curUserId)
					+ "";
			ProcessInstance pi = adapterBaseWorkflowManager
					.getWorkflowService().getProcessInstanceById(instanceId);
			if (taskId == null) {
				formId = pi.getMainFormId();
				bussinessId = pi.getBusinessId();
				workflowName = pi.getName();
			} else {
				String[] strBusinessId = getManager()
						.getFormIdAndBussinessIdByTaskId(taskId);
				formId = strBusinessId[1];
				bussinessId = strBusinessId[0];
			}
			TwfBaseNodesetting nodesetting = adapterBaseWorkflowManager
					.getWorkflowService().findFirstNodeSetting(taskId,
							workflowName);
			adapterBaseWorkflowManager.getSendDocUploadManager()
					.initProcessedByNodeSetting(nodesetting);
			nodeId = StringUtil.castString(nodesetting.getNsNodeId());
			if (!"0".equals(formId)) {
				String[] strBussinessId = bussinessId.split(";");
				tableName = strBussinessId[0];
				pkFieldName = strBussinessId[1];
				pkFieldValue = strBussinessId[2];
			}

			if (pi.getEnd() != null) {
				state = "1";
			} else {
				state = "0";
			}
			bussinessId = tableName + ";" + pkFieldName + ";" + pkFieldValue;
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if (jsonArray != null && !jsonArray.isEmpty()) {
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			} else {
				String demo = (String) pi.getContextInstance().getVariable(
						"@{personDemo}");
				String parentinstanceId = "";
				if (!"".equals(demo) && null != demo) {
					String[] demos = demo.split(";");
					if (demos.length > 4) {
						parentinstanceId = demos[4];
						parentinstanceId = parentinstanceId.split("@")[0];

						jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
						personDemo = jsonArray.toString();
						parentInstanceId = "exists";
					}
				}
			}
			// ContextInstance cxt = adapterBaseWorkflowManager
			// .getWorkflowService().getContextInstance(instanceId);
			// Object tempObject = cxt
			// .getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);//
			// 获取父流程实例id
			// if (tempObject != null && !tempObject.toString().equals("")) {
			// parentInstanceId = tempObject.toString();
			// ContextInstance parentCxt = adapterBaseWorkflowManager
			// .getWorkflowService().getContextInstance(
			// parentInstanceId);// 获取父流程上下文
			// personDemo = (String) parentCxt.getVariable("@{personDemo}");
			// }
			Object[] toSelectItems = { "taskId" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("processInstanceId", instanceId);
			paramsMap.put("handlerId", adapterBaseWorkflowManager
					.getUserService().getCurrentUser().getUserId());// 当前用户办理任务
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			List list = adapterBaseWorkflowManager.getWorkflowService()
					.getTaskInfosByConditionForList(sItems, paramsMap,
							orderMap, null, null, null, null);
			if (list != null && !list.isEmpty()) {
				getRequest().setAttribute("isExsitTodo", "1");
			} else {
				getRequest().setAttribute("isExsitTodo", "0");
			}
		} catch (Exception e) {
			logger.error("查看主办流程时发生错误", e);
		}
		return "viewrepeal";
	}

	/**
	 * @method repeal
	 * @author 申仪玲
	 * @created 2011-12-12 下午09:27:27
	 * @description 得到公文回收站流程列表
	 * @return String 返回类型
	 */
	@SuppressWarnings("unchecked")
	public String repeal() throws Exception {
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		ParamBean parambean = new ParamBean();
		try {
			// 取主办人员是当前用户的挂起流程 申仪玲 20120323
			User user = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser();// 得到当前用户
			ProcessedParameter parameter = new ProcessedParameter();
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setExcludeWorkflowType(excludeWorkflowType);
			parameter.setWorkflowType(workflowType);
			if (!"1".equals(showSignUserInfo)) {// 不是已分办 yanijan
				String currworkflowType = workflowType;
				handleKind = "0";
				if ("3".equals(currworkflowType)) {
					handleKind = "2";
				} else if ("2".equals(currworkflowType)) {
					handleKind = "1";
				}
			}
			// parameter.setUserId(user.getUserId());//设置主办人员id
			// parameter.setUserName(userName);
			// // parameter.setIsBackSpace(isBackSpace);
			// parameter.setStartDate(startDate);
			// parameter.setEndDate(endDate);
			// // parameter.setProcessStatus(state);
			// parameter.setSortType(sortType);
			// // parameter.setTaskIdList(null);
			// // parameter.setShowSignUserInfo(showSignUserInfo);
			// // parameter.setFilterSign(filterSign);
			// // parameter.setFilterYJZX("0");
			// parameter.setHandleKind(handleKind);
			// parameter.setIsSuspended("1");
			parameter.setWorkflowName(workflowName);
			parameter.setWorkflowType(workflowType);
			Object[] obj = manager.getRepeal(page, parameter);
			System.out.println(page.getTotalCount());
			parambean.setFilterYJZX(filterYJZX);
			parambean.setSortType(sortType);
			sendDocLinkManager.processedGridViewSort(parambean);
			if (obj != null) {
				List list = (List) obj[1];
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
							workFlowTitle = workFlowTitle
									.replace("\\r\\n", " ");
							workFlowTitle = workFlowTitle.replace("\\n", " ");
						}
						sendDocIcoManager.repealGridViewIco(map);
						// StringBuilder picImage = new StringBuilder();
						// String rootPath = getRequest().getContextPath();
						// sendDocIcoManager.gridProcessedViewRedYellowGreenIco(
						// picImage, (Date) map.get("processStartDate"),
						// (Date) map.get("processEndDate"), rootPath);
						// sendDocIcoManager.gridViewTimeOutIco(picImage, map
						// .get("processStartDate"), map.get("timeOut"),
						// rootPath);
						// if (!map.containsKey("png")) {
						// map.put("png", picImage.toString());
						// }
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 20 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 20 * (columnSize - 1);
							otherLength = 20;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取已办流程时发生异常", e);
		}
		getRequest().setAttribute("pageResultSize",
				(page.getResult() == null ? 0 : page.getResult().size()));
		getRequest()
				.setAttribute(
						"sortHtml",
						(parambean.getSortHtml() == null ? "" : parambean
								.getSortHtml()).toString());
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		return HandleKindConst.getReturnString(handleKind, "repeal");
	}

	/**
	 * 我委派|指派的流程列表
	 * 
	 * @author:邓志城
	 * @date:2010-11-21 下午06:21:16
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String entrust() {
		try {
			Object[] obj = manager.getEntrust(page, workflowName, formId,
					workflowType, assignType, userName, startDate, endDate);
			if (obj != null) {
				List list = (List) obj[1];
				page = ListUtils.splitList2Page(page, list);
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
			}
		} catch (Exception e) {
			logger.error("获取我委派|指派时发生异常", e);
		}
		return "entrust";
	}

	/**
	 * 得到已办流程列表 支持自定义查询及自定义显示
	 * 
	 * 郑志斌 20110816 根据流程标题最大长度，动态显示：流程标题、拟稿人、办理日期、任务类型区域太长
	 * 
	 * @modify yanjian 2011-09-28 添加来文办文排序
	 */
	@SuppressWarnings("unchecked")
	public String processed() throws Exception {
		// 公文传输中的意见征询 标识
		String sends = getRequest().getParameter("sends");
		if ("1".equals(sends)) {
			getRequest().setAttribute("sends", sends);
		}
		String suporgcode = user.getCurrentUser().getSupOrgCode();
		getRequest().setAttribute("suporgcode", suporgcode);
		// System.out.println("orgcode:" + suporgcode);
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		ParamBean parambean = new ParamBean();
		try {
			ProcessedParameter parameter = new ProcessedParameter();
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setExcludeWorkflowType(excludeWorkflowType);
			parameter.setWorkflowType(workflowType);
			if (!"1".equals(showSignUserInfo)) {// 不是已分办 yanijan
				String currworkflowType = workflowType;
				handleKind = "0";
				if ("3".equals(currworkflowType)) {
					handleKind = "2";
				} else if ("2".equals(currworkflowType)) {
					handleKind = "1";
				}
			}
			parameter.setUserName(userName);
			parameter.setIsBackSpace(isBackSpace);
			parameter.setStartDate(startDate);
			parameter.setEndDate(endDate);
			parameter.setProcessStatus(state);
			parameter.setTaskIdList(null);
			parameter.setShowSignUserInfo(showSignUserInfo);
			if (showSignUserInfo != null && showSignUserInfo.equals("1")) {
				if (sortType == null || sortType.equals("")) {
					sortType = SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC;
				}
			}
			parameter.setSortType(sortType);
			parameter.setFilterSign(filterSign);
			parameter.setFilterYJZX(filterYJZX);
			parameter.setHandleKind(handleKind);
			parameter.setIsSuspended("0");
			parameter.setNodeName(nodeName);
			Object[] obj = manager.getProcessed(page, parameter);
			parambean.setFilterYJZX(filterYJZX);
			parambean.setSortType(sortType);
			sendDocLinkManager.processedGridViewSort(parambean);
			if (obj != null) {
				List list = (List) obj[1];
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
							workFlowTitle = workFlowTitle
									.replace("\\r\\n", " ");
							workFlowTitle = workFlowTitle.replace("\\n", " ");
						}
						sendDocIcoManager.processedGridViewIco(map);
						// sendDocIcoManager.processedGridViewIco(
						// picImage, (Date) map.get("processStartDate"),
						// (Date) map.get("processEndDate"), rootPath);
						// sendDocIcoManager.gridViewTimeOutIco(picImage, map
						// .get("taskStartDate"), map.get("timeOut"),
						// rootPath);
						// sendDocIcoManager.gridViewYjzxIco(picImage,
						// (String)map
						// .get("businessType"), (String)map.get("businessId"),
						// rootPath);
						// if (!map.containsKey("png")) {
						// map.put("png", picImage.toString());
						// }
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 20 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 20 * (columnSize - 1);
							otherLength = 20;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取已办流程时发生异常", e);
		}
		getRequest().setAttribute("pageResultSize",
				(page.getResult() == null ? 0 : page.getResult().size()));
		getRequest()
				.setAttribute(
						"sortHtml",
						(parambean.getSortHtml() == null ? "" : parambean
								.getSortHtml()).toString());
		getRequest()
				.setAttribute(
						"yjzxHtml",
						(parambean.getYjzxHtml() == null ? "" : parambean
								.getYjzxHtml()).toString());
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		return HandleKindConst.getReturnString(handleKind, "processed");
	}

	/**
	 * 得到主办流程列表 支持自定义查询及自定义显示
	 * 
	 * 郑志斌 20110816 根据流程标题最大长度，动态显示：流程标题、拟稿人、办理日期、任务类型区域太长
	 */
	@SuppressWarnings("unchecked")
	public String hostedby() throws Exception {
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		try {
			String currworkflowType = workflowType;
			handleKind = "0";
			if ("3".equals(currworkflowType)) {
				handleKind = "2";
			} else if ("2".equals(currworkflowType)) {
				handleKind = "1";
			}
			ProcessedParameter parameter = new ProcessedParameter();
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setWorkflowType(workflowType);
			parameter.setState(state);
			parameter.setHandleKind(handleKind);
			Object[] obj = manager.getHosted(page, parameter);
			if (obj != null) {
				List list = (List) obj[1];
				// page = ListUtils.splitList2Page(page, list);
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (Object object : list) {
						Map map = (Map) object;
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
						}
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 15 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 15 * (columnSize - 1);
							otherLength = 15;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取主办流程时发生异常", e);
		}
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		return HandleKindConst.getReturnString(handleKind, "hostedby");
	}

	/**
	 * 得到我的在办文件列表 支持自定义查询及自定义显示
	 * 
	 * 郑志斌 20110816 根据流程标题最大长度，动态显示：流程标题、发起日期、发起人
	 */
	@SuppressWarnings("unchecked")
	public String myTodo() throws Exception {
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		try {
			String currworkflowType = workflowType;
			handleKind = "0";
			if ("3".equals(currworkflowType)) {
				handleKind = "2";
			} else if ("2".equals(currworkflowType)) {
				handleKind = "1";
			}
			ProcessedParameter parameter = new ProcessedParameter();
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setWorkflowType(workflowType);
			parameter.setState(state);
			parameter.setHandleKind(handleKind);
			Object[] obj = manager.getMyNowFile(page, parameter);
			if (obj != null) {
				List list = (List) obj[1];
				// page = ListUtils.splitList2Page(page, list);
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (Object object : list) {
						Map map = (Map) object;
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
						}
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 15 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 15 * (columnSize - 1);
							otherLength = 15;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取主办流程时发生异常", e);
		}
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		return HandleKindConst.getReturnString(handleKind, "myTodo");
	}

	/**
	 * 发文统计
	 */
	public String statistics() {
		return "statistics";
	}

	/**
	 * 打开WORD文档，将文档流直接写入HttpServletResponse
	 * 
	 * @return
	 */
	public String openDoc() {
		HttpServletResponse response = this.getResponse();
		manager.setContentToHttpResponse(response, bussinessId);
		return null;
	}

	/**
	 * 公文显示个人桌面。
	 * 
	 * @author:邓志城
	 * @date:2009-11-25 上午11:46:31
	 * @return
	 */
	public String showDesktop() throws Exception {
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否显示字体大小
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		// 获取待办公文
		//Page pageTodo = new Page(num, false);
		//pageTodo = manager.getTodo(pageTodo, workflowType);
		//换一种查询待办公文接口，获取上步处理人信息
		Page<TaskBusinessBean> page = sendDocUploadManager.getTodoByRedType(
				workflowType, type, num);
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if ( page.getResult()!= null) {
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator =  page.getResult().iterator(); iterator
					.hasNext();) {
				//Object[] object = (Object[]) iterator.next();
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
				.next();
				innerHtml
						.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td >");
				// 图标
				innerHtml.append("<img src=\"").append(rootPath).append(
						"/oa/image/desktop/littlegif/news_bullet.gif").append(
						"\"/> ");
				// 如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = taskbusinessbean.getBusinessName() == null ? "" :  taskbusinessbean.getBusinessName();
				if (title.length() > length && length > 0) {
					title = title.substring(0, length) + "...";
				}
				StringBuilder clickTitle = new StringBuilder();
				clickTitle
						.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
				clickTitle
						.append("var a = window.open('")
						.append(getRequest().getContextPath())
						.append("/senddoc/sendDoc!CASign.action?taskId=")
						.append(taskbusinessbean.getTaskId())
						.append("&instanceId=")
						.append(taskbusinessbean.getInstanceId())
						.append("&workflowName=")
						.append(
								URLEncoder.encode(URLEncoder.encode(taskbusinessbean
										.getWorkflowName().toString(), "utf-8"), "utf-8"))
						.append(
								"','senddoc','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");
				innerHtml.append("<span title=\"").append(taskbusinessbean.getBusinessName()).append(
						"\n 发起时间："
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm")
										.format(taskbusinessbean.getWorkflowStartDate()) + ""+taskbusinessbean.getPreTaskActorInfo()+"\">").append(
						"<a style=\"font-size:" + sectionFontSize
								+ "px\" href=\"#\" onclick=\"").append(
						clickTitle.toString()).append("\">").append(title)
						.append("</a></span>");
				innerHtml.append("</td>");
				// 显示上步处理人信息
				if ("1".equals(showCreator)) {
					innerHtml.append("<td width=\"80px\">");
					
					/**  这段注释的代码  是 获取发起人的信息  现要改成 显示上步处理人信息 故注释掉
					  if (object[7] == null) {
						object[7] = manager
								.getFormIdAndBussinessIdByTaskId(object[0]
										.toString())[0];
					}
					bussinessId = String.valueOf(object[7]);
					if (!"0".equals(bussinessId)) {
						String departmentName = null;
						String[] args = bussinessId.split(";");
						tableName = String.valueOf(args[0]);
						pkFieldName = String.valueOf(args[1]);
						pkFieldValue = String.valueOf(args[2]);
						Map map = manager.getSystemField(pkFieldName,
								pkFieldValue, tableName);
						userId = (String) map
								.get(BaseWorkflowManager.WORKFLOW_AUTHOR);
						if (userId != null) {
							userName = adapterBaseWorkflowManager
									.getUserService().getUserNameByUserId(
											userId);
							Organization department = adapterBaseWorkflowManager
									.getUserService()
									.getUserDepartmentByUserId(userId);
							departmentName = department.getOrgName();
							String showName = userName + "[" + departmentName
									+ "]";
							String realShowName = showName;
							if (showName.length() > 3) {
								showName = showName.substring(0, 3) + "...";
							}
							innerHtml.append(
									"<span title =\"" + realShowName
											+ "\" class =\"linkgray\">")
									.append(showName).append("</span>");
						}
					}*/
					
					//获取上一步处理人
					List<Long> taskIdList = new LinkedList<Long>();
					taskIdList.add(new Long(taskbusinessbean.getTaskId()));
					Map<String, TaskBeanTemp> TaskIdMapPreTaskBeanTemp = sendDocUploadManager.getTaskIdMapPreTaskBeanTemper(taskIdList);
					TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp.get(taskbusinessbean.getTaskId());
					String showName =taskBeanTemp.getTaskActorName();
			        String realShowName = showName;
			        if (showName.length() > 4) {
						showName = showName.substring(0, 4) + "...";
					}
					innerHtml.append(
							"<span title =\"" + realShowName
									+ "\" class =\"linkgray\">")
							.append(showName).append("</span>");
					innerHtml.append("</td>");
					
				}
				// 显示日期信息
				if ("1".equals(showDate)) {
					innerHtml.append("<td width=\"120px\">");
					SimpleDateFormat st = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					innerHtml.append("<span class =\"linkgray\">").append(
							" (" + st.format(taskbusinessbean.getTaskStartDate()) + ")")
							.append("</span>");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
				innerHtml.append("</div>");
			}
		}
		// 跳转连接
		StringBuffer link = new StringBuffer();
		if (workflowType != null && "2".equals(workflowType)) {
			link.append("javascript:window.parent.refreshWorkByTitle('")
					.append(getRequest().getContextPath());
			link.append(
					"/senddoc/sendDoc!todoWorkflow.action?workflowType="
							+ workflowType + "").append("', '公文审核'").append(
					");");
		} else if (workflowType != null && "3".equals(workflowType)) {
			link.append("javascript:window.parent.refreshWorkByTitle('")
					.append(getRequest().getContextPath());
			link.append(
					"/senddoc/sendDoc!todoWorkflow.action?workflowType="
							+ workflowType + "").append("', '收文审核'").append(
					");");
		} else {
			link.append("javascript:window.parent.refreshWorkByTitle('")
					.append(getRequest().getContextPath());
			link.append(
					"/senddoc/sendDoc!todoWorkflow.action?workflowType="
							+ workflowType + "").append("', '待办事宜'").append(
					");");

		}
		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ").append("<IMG SRC=\"")
				.append(getRequest().getContextPath()).append(
						"/oa/image/more.gif\" BORDER=\"0\" /></a></div>");

		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 
	 * @author：yuhz
	 * @time：May 6, 20093:30:00 PM
	 * @desc:
	 * @return List<EForm>
	 */
	public String getNowDig() {
		getResponse().setHeader("Cache-Control", "no-store");
		getResponse().setHeader("Pragrma", "no-cache");
		getResponse().setDateHeader("Expires", 0);
		ToaSenddoc nowDoc = manager.getDocById(pkFieldValue);
		String total = "0";
		String printed = "0";
		if ("0".equals(isystemsetService.gwControl()))// 读取系统设置显示系打印设置还是个人
		{
			total = nowDoc.getSenddocPrintNum() == null ? "0" : nowDoc
					.getSenddocPrintNum();
			printed = nowDoc.getSenddocHavePrintNum() == null ? "0" : nowDoc
					.getSenddocHavePrintNum();
		} else {
			ToaPrintSet print = printSetManager.getPrintSet(pkFieldValue);
			if (null == print) {
				print = new ToaPrintSet();
				print.setSenddocId(pkFieldValue);
				print.setPrintCount(0);
				printSetManager.addPrintSet(print);
			}
			printed = Integer.toString(print.getPrintCount());
			total = nowDoc.getSenddocPrintNum() == null ? "0" : nowDoc
					.getSenddocPrintNum();
		}
		JSONArray jsArray = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("total", total);
		obj.put("printed", printed);
		jsArray.add(obj);
		return this.renderText(jsArray.toString());
	}

	/**
	 * 
	 * @author：yuhz
	 * @time：May 6, 20094:12:07 PM
	 * @desc:
	 * @return String
	 */
	public String changePrintedNum() {
		getResponse().setHeader("Cache-Control", "no-store");
		getResponse().setHeader("Pragrma", "no-cache");
		getResponse().setDateHeader("Expires", 0);
		if ("0".equals(isystemsetService.gwControl()))// 读取系统设置显示系打印设置还是个人
		{
			ToaSenddoc nowDoc = manager.getDocById(pkFieldValue);
			nowDoc.setSenddocHavePrintNum(printed);
			try {
				manager.saveDoc(nowDoc, new OALogInfo("更新打印份数"));
			} catch (Exception e) {
				System.out.println(e);
				return renderText("false");
			}
			return renderText("true");
		} else {
			ToaPrintSet print = printSetManager.getPrintSet(pkFieldValue);
			print.setPrintCount(Integer.parseInt(printed));
			try {
				printSetManager.updatePrintSet(print);
				return renderText("true");
			} catch (Exception e) {
				System.out.println(e);
				return renderText("false");
			}
		}
	}

	/**
	 * @author:luosy
	 * @description: 桌面显示 已办 或 办结
	 * @date : 2011-5-17
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 * @modify yanjian 已办事宜增加排序功能 2011-09-13 11:27
	 * @modify yanjian 已办事宜记录title显示内容根据不同的排序方式显示不同的信息 2011-09-17 11:27
	 * @modify yanjian 解决已办事宜中无法正常显示出带有换行符的流程名
	 */
	public String showDesktopDoneWork() throws Exception {
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 字体大小
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		Page<TaskBusinessBean> page = sendDocUploadManager
				.getProcessedWorkflowForDesktop(processStatus, num, sortType,
						filterSign);
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if (page.getResult() != null) {
			ParamBean parambean = new ParamBean();
			parambean.setNum(num);
			parambean.setProcessStatus(processStatus);
			parambean.setLength(length);
			parambean.setRootPath(rootPath);
			parambean.setShowCreator(showCreator);
			parambean.setShowDate(showDate);
			parambean.setSectionFontSize(sectionFontSize);
			parambean.setFilterSign(filterSign);
			parambean.setSortType(sortType);
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = page.getResult().iterator(); iterator
					.hasNext();) {
				innerHtml
						.append("<table class=\"linkdiv\" width=\"100%\" style=\"vertical-align: top;width: 100%;table-layout: fixed;\" title=\"\">");
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				taskbusinessbean.setWorkflowStatus(processStatus);
				innerHtml.append("<tr>");
				// 图标
				innerHtml.append("<td>");
				if (taskbusinessbean.getWorkflowStatus().equals("1")) {// 办结文件
					// object[14] = "";
					innerHtml.append("<img src=\"").append(rootPath).append(
							"/oa/image/desktop/littlegif/news_bullet.gif")
							.append("\"/> ");
				} else {// 已办文件
					sendDocIcoManager.loadProcessedIco(innerHtml,
							taskbusinessbean, rootPath);
				}
				sendDocLinkManager.genProcessedTitle(innerHtml,
						taskbusinessbean, parambean);
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		// 跳转连接
		sendDocLinkManager.genProcessedClickMoreLink(innerHtml, rootPath,
				processStatus, blockId, sortType);
		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(page == null ? 0 : (page.getTotalCount())).append(
						"\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 桌面显示 已办 或 办结 表格形式
	 * 
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Feb 16, 2012 5:09:12 PM
	 */
	public String showTableDesktopDoneWork() throws Exception {
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 字体大小
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		Page<TaskBusinessBean> page = sendDocUploadManager
				.getProcessedWorkflowForDesktop(processStatus, num, sortType,
						filterSign);
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer(
				"	<table width=\"100%\" style=\"font-size:" + sectionFontSize
						+ "px\" cellpadding=\"0\" cellspacing=\"0\">");
		if (page.getResult() != null) {
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = page.getResult().iterator(); iterator
					.hasNext();) {
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				innerHtml.append(" <tr height=\"25px\">");
				// 图标
				innerHtml.append("<td width=\"65%\">");
				sendDocIcoManager.loadProcessedIco(innerHtml, taskbusinessbean,
						rootPath);
				// if (processStatus.equals("1")) {// 办结文件
				// // object[14] = "";
				// innerHtml.append("<img src=\"").append(rootPath).append(
				// "/oa/image/desktop/littlegif/news_bullet.gif")
				// .append("\"/> ");
				// } else {// 已办文件
				// /* 显示红黄蓝图标 */
				// sendDocIcoManager.loadRedYellowGreenIco(innerHtml,
				// taskbusinessbean.getWorkflowStartDate(), rootPath);
				//					
				// /* 显示公文期限图标 */
				// /**
				// sendDocIcoManager.loadTimeOutIco(innerHtml,
				// taskbusinessbean.getWorkflowStartDate(),
				// taskbusinessbean.getEndTime(), rootPath);
				// */
				//
				// }
				// 如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = taskbusinessbean.getBusinessName() == null ? ""
						: taskbusinessbean.getBusinessName();
				title = title.replace("\\r\\n", " ");
				title = title.replace("\\n", " ");
				String showTitle = title;
				// 截取字符串，判断是中文还是数字。
				showTitle = manager.getStr(showTitle, length);
				if (title.length() > length && length > 0) {
					// showTitle = showTitle.substring(0, length) + "...";
					showTitle = showTitle + "...";
				}
				Object titleShowDate = null;
				String docType = null;
				if (sortType.startsWith("taskenddate")) {
					docType = "办文时间：";
					titleShowDate = taskbusinessbean.getTaskEndDate();
				} else {
					docType = "来文时间：";
					titleShowDate = taskbusinessbean.getWorkflowStartDate();
				}
				StringBuilder clickTitle = new StringBuilder();
				clickTitle
						.append("var width=screen.availWidth-10;var height=screen.availHeight-30;");
				clickTitle
						.append("var a = window.open('")
						.append(getRequest().getContextPath())
						.append("/senddoc/sendDoc!viewProcessed.action?taskId=")
						.append(taskbusinessbean.getTaskId())
						.append("&instanceId=")
						.append(taskbusinessbean.getInstanceId())
						.append("&state=")
						.append(processStatus)
						.append("&searchType=1")
						.append(
								"','processed','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');")
						.append("if(a && a == 'OK'){window.location.reload();}");
				StringBuilder clickProcessType = new StringBuilder();
				clickProcessType.append(sendDocLinkManager
						.genProcessedWorkflowNameLink(taskbusinessbean
								.getWorkflowName(), taskbusinessbean
								.getFormId(), taskbusinessbean
								.getWorkflowType(), processStatus, rootPath,
								filterSign));
				innerHtml.append("<span>").append("").append(
						"<a href=\"#\" onclick=\"").append(
						clickProcessType.toString()).append("\">[").append(
						taskbusinessbean.getWorkflowAliaName()).append("]</a>")
						.append("").append("<a href=\"#\" onclick=\"").append(
								clickTitle.toString()).append("\"").append(
								"title=\"").append(title).append("\n").append(
								docType
										+ new SimpleDateFormat(
												"yyyy-MM-dd HH:mm")
												.format(titleShowDate)).append(
								"\">").append(showTitle).append("</a></span>");
				/**
				 * // 显示发起人信息 if ("1".equals(showCreator)) {
				 * innerHtml.append("&nbsp;&nbsp;<span class =\"linkgray\">")
				 * .append(taskbusinessbean.getStartUserName()) .append("</span>"); } //
				 * 显示日期信息 if ("1".equals(showDate)) { SimpleDateFormat st = new
				 * SimpleDateFormat( "yyyy-MM-dd HH:mm"); innerHtml
				 * .append("&nbsp;&nbsp;<span class =\"linkgray10\">") .append( " (" +
				 * st.format(taskbusinessbean .getWorkflowStartDate()) +
				 * ")").append("</span>"); }
				 */
				innerHtml.append("</td>");
				// 显示时间
				innerHtml.append("<td width=\"20%\">");
				innerHtml.append(new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format(taskbusinessbean.getWorkflowStartDate()));
				innerHtml.append("</td width=\"*%\">");
				// 显示人名
				innerHtml.append("<td>");
				// sendDocLinkManager.showCurTaskInfo(innerHtml,
				// taskbusinessbean);
				StringBuffer tip = null;
				String CurTaskActorInfo = null;
				// 只显示名字，不显示部门 申仪玲 2012.05.23
				String TaskActor = "";
				if (taskbusinessbean.getWorkflowEndDate() == null) {
					CurTaskActorInfo = (taskbusinessbean.getCurTaskActorInfo() == null ? ""
							: taskbusinessbean.getCurTaskActorInfo());
					String[] CurTaskActorInfos = CurTaskActorInfo.split(",");
					tip = new StringBuffer();
					if (CurTaskActorInfos.length > 0) {
						for (String info : CurTaskActorInfos) {
							String username = null;
							if (info.indexOf("(") == -1) {
								username = info;
							} else {
								username = info.substring(0, info.indexOf("("));
							}
							if (TaskActor.length() > 0) {
								TaskActor += ",";
							}
							TaskActor += username;
						}
						tip.append(CurTaskActorInfo);
					} else {
						tip = new StringBuffer("");
						CurTaskActorInfo = "";
					}
				} else {
					tip = new StringBuffer("");
					CurTaskActorInfo = "";
				}
				if (CurTaskActorInfo.length() > 5) {
					CurTaskActorInfo = CurTaskActorInfo.subSequence(0, 5)
							+ "...";
				}
				// 清除空格
				TaskActor = TaskActor.replaceAll(" ", "");
				if (TaskActor.length() > 3) {
					TaskActor = TaskActor.subSequence(0, 3) + "...";
				}
				List<User> users = adapterBaseWorkflowManager
						.getWorkflowService().getCurrentTaskHandle(
								taskbusinessbean.getInstanceId()).getActors();
				String userNames = "";
				if (users != null && users.size() > 0) {
					for (int i = 0; i < users.size(); i++) {
						User user = adapterBaseWorkflowManager.getUserService()
								.getUserInfoByUserId(users.get(i).getUserId());
						if ("".equals(userNames)) {
							userNames = user.getUserName() + "_"
									+ user.getUserLoginname();
						} else {
							userNames += "," + user.getUserName() + "_"
									+ user.getUserLoginname();
						}
					}
				}
				// 已办事宜桌面列表不发送即时大蚂蚁消息
				/**
				 * innerHtml.append( "<span class =\"linkgray\" title=\"" + tip +
				 * "\" >").append("<a href=\"#\" onclick=\"sendMsg('" +
				 * userNames + "');\">") .append(TaskActor).append("</a></span>");
				 */
				innerHtml.append(
						"<span class =\"linkgray\" title=\"" + tip + "\" >")
						.append(TaskActor).append("</span>");
				innerHtml.append("</td>");
				innerHtml.append(" </tr>");
			}
		}
		innerHtml.append("</table>");
		// 跳转连接
		StringBuffer link = new StringBuffer();
		String sortHtml = "";
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				getRequest().getContextPath());
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
				sortHtml = sendDocLinkManager.genSortTypeLink(blockId,
						this.sortType);
			}
		}
		innerHtml
				.append(
						"<div class=\"select\">"
								+ sortHtml
								+ "</div><div align=\"right\" style=\"padding:2px；font-size:12px;float:right;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ").append("<IMG SRC=\"")
				.append(rootPath).append(
						"/oa/image/more.gif\" BORDER=\"0\" /></a></div>");

		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(page == null ? 0 : (page.getTotalCount())).append(
						"\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 桌面显示 每日要情
	 * 
	 * @author 杨智超
	 * @return
	 * @throws Exception
	 * @createTime
	 */
	public String showTableYQ() throws Exception {
		SendDocManager manager = (SendDocManager) ServiceLocator
				.getService("sendDocManager");
		article = manager.showListByYQ();
		// getRequest().setAttribute("article", article);
		return "mryq";
	}

	/**
	 * 点击每日要情，进入每日要情列表
	 * 
	 * @author 杨智超
	 * @return
	 * @throws Exception
	 * @createTime
	 */
	public String showListYQ() throws Exception {
		SendDocManager manager = (SendDocManager) ServiceLocator
				.getService("sendDocManager");
		articles = manager.showListYQ(articles);
		// getRequest().setAttribute("article", article);
		return "content";
	}

	/**
	 * 弹出 每日要情及批示
	 * 
	 * @author 杨智超
	 * @return
	 * @throws Exception
	 * @createTime
	 */
	public String getYQ() throws Exception {
		String issId = getRequest().getParameter("issId");
		pub = manager.getYaoQingByIssue(issId);
		path1 = getRequest().getContextPath()
				+ "/common/ewebeditor/uploadfile/" + issId;
		getRequest().setAttribute("issId", issId);
		return "yq";
	}

	/**
	 * 保存每日要情及批示
	 * 
	 * @author 杨智超
	 * @return
	 * @throws Exception
	 * @createTime
	 */
	public String saveYQ() throws Exception {
		String issId = getRequest().getParameter("issId");
		String remark = getRequest().getParameter("remark");
		SendDocManager manager = (SendDocManager) ServiceLocator
				.getService("sendDocManager");
		pub = manager.getYaoQingByIssue(issId);
		pub.setArticlesInstructionContent(remark);
		manager.saveYaoQing(pub);
		return "yq";
	}

	/**
	 * @author:luosy
	 * @description: 桌面 待办中心
	 * @date : 2011-5-11
	 * @modifyer:
	 * @description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String todoCenter() {
		StringBuffer innerHtml = new StringBuffer();
		String simpletype = getRequest().getParameter("simpletype");// 获取是否简单
		String ifreamUrl = "/fileNameRedirectAction.action?toPage=/work/work-desktopTodoCenter.jsp";
		if ("simple".equals(simpletype)) {
			ifreamUrl = "/fileNameRedirectAction.action?toPage=/work/work-desktopTodoCenterSimple.jsp";
		}
		String blockId = getRequest().getParameter("blockid");// 获取blockid
		System.out.println("\n\n\nblockId:" + blockId);
		String showNum = getRequest().getParameter("showNum");
		String subLength = getRequest().getParameter("subLength");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否显示字体大小
		}
		int num = 0, length = 0, iframeHeight = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
			iframeHeight = 26 + num * 24;
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		System.out.println(length);
		List jjList = manager.getTodoCenter("jj_div", num);// 急件
		List cpList = manager.getTodoCenter("sp_div", num);// 审批件
		List cyList = manager.getTodoCenter("cy_div", num); // 呈阅件
		List dbList = manager.getTodoCenter("db_div", num); // 待办事宜
		innerHtml
				.append(
						"<div id=\"calframe\" align=\"center\" style=\"width:100%;\">")
				.append("<input type=\"hidden\" id=\"calDeskTopID\" value=\"")
				.append(blockId)
				.append("\">")
				.append("<iframe id=\"daycontent\" src='")
				.append(getRequest().getContextPath())
				.append(ifreamUrl)
				.append("?showNum=")
				.append(showNum)
				.append("&subLength=")
				.append(subLength)
				.append("&sectionFontSize=")
				.append(sectionFontSize)
				.append("&showCreator=")
				.append(showCreator)
				.append("&showDate=")
				.append(showDate)
				.append("&jjList=")
				.append(jjList.size())
				.append("&cpList=")
				.append(cpList.size())
				.append("&cyList=")
				.append(cyList.size())
				.append("&dbList=")
				.append(dbList.size())
				.append("&blockId=")
				.append(blockId)
				.append("'")
				.append(
						"frameborder=\"0\" scrolling=\"no\" height=\""
								+ iframeHeight
								+ "px\" width=\"100%\" align=\"top\" ></iframe></div>");
		// 跳转连接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				getRequest().getContextPath());
		link.append("/senddoc/sendDoc!todoWorkflow.action").append("', '待办事宜'")
				.append(");");

		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ").append("<IMG SRC=\"")
				.append(getRequest().getContextPath()).append(
						"/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * @author:luosy
	 * @description: ajax获取待办中心的数据
	 * @date : 2011-5-16
	 * @modifyer:
	 * @description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getTodoListByType() {
		String listType = (String) this.getRequest().getParameter("listType");
		String showNum = (String) this.getRequest().getParameter("showNum");
		String subLength = (String) this.getRequest().getParameter("subLength");
		String showCreator = (String) this.getRequest().getParameter(
				"showCreator");// 1：显示
		String showDate = (String) this.getRequest().getParameter("showDate");// 1：显示
		String blockId = (String) this.getRequest().getParameter("blockId");
		// if("jj_div".equals(listType)){//急件
		// }else if("sp_div".equals(listType)){//审批件
		// }else if("cy_div".equals(listType)){//呈阅件
		// }else if("db_div".equals(listType)){//待办事宜
		// }
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		List todoList = manager.getTodoCenter(listType, num);
		// 获取待办公文
		Page pageTodo = new Page(num, false);
		pageTodo.setResult(todoList);
		pageTodo.setPageNo(1);
		pageTodo.setTotalCount(todoList.size());
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if (pageTodo.getResult() != null) {
			int countNum = 0;
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {parent.document.getElementById('drag_refresh_img_"
							+ blockId + "').onclick();}</script>");
			for (Iterator iterator = pageTodo.getResult().iterator(); iterator
					.hasNext();) {
				if (countNum == num) {
					break;
				}
				countNum += 1;
				Object[] object = (Object[]) iterator.next();
				innerHtml
						.append("<div class=\"linkdiv\" style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\" title=\"\">");
				// 图标
				innerHtml.append("<img src=\"").append(rootPath).append(
						"/oa/image/desktop/littlegif/news_bullet.gif").append(
						"\"/> ");
				// 如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = object[5] == null ? "" : object[5].toString();
				if (title.length() > length && length > 0) {
					title = title.substring(0, length) + "...";
				}
				if ("jj_div".equals(listType)) {// 急件
					title = "<b>（急）&nbsp;</b>" + title;
				}
				StringBuilder clickTitle = new StringBuilder();
				String workflowName = (String) object[3];
				if (workflowName != null) {
					try {
						workflowName = URLEncoder.encode(URLEncoder.encode(
								workflowName, "utf-8"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						logger.error("Parse workflowName to utf-8 error", e);
					}
				}
				clickTitle
						.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
				clickTitle
						.append("var a = window.open('")
						.append(getRequest().getContextPath())
						.append("/senddoc/sendDoc!CASign.action?taskId=")
						.append(object[0])
						.append("&instanceId=")
						.append(object[4])
						.append("&workflowName=")
						.append(workflowName)
						.append(
								"','todo','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");

				try {
					innerHtml
							.append("<span title=\"")
							.append(object[5])
							.append(
									"("
											+ new SimpleDateFormat(
													"yyyy-MM-dd ")
													.parse(StringUtil
															.castString(object[1]))
											+ ")\">").append(
									"<a href=\"#\" onclick=\"").append(
									clickTitle.toString()).append("\">")
							.append(title).append("</a></span>");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 显示发起人信息
				if ("1".equals(showCreator)) {
					if (null == object[2] || "null".equals(object[2])) {
						innerHtml
								.append(
										"&nbsp;&nbsp;<span class =\"linkgray\">")
								.append(
										adapterBaseWorkflowManager
												.getUserService()
												.getCurrentUser().getUserName())
								.append("</span>");
					} else {
						innerHtml.append(
								"&nbsp;&nbsp;<span class =\"linkgray\">")
								.append(object[2]).append("</span>");
					}
				}
				// 显示日期信息
				if ("1".equals(showDate)) {
					SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
					innerHtml
							.append("&nbsp;&nbsp;<span class =\"linkgray10\">")
							.append(" (" + st.format(object[1]) + ")").append(
									"</span>");
				}
				innerHtml.append("</div>");
			}
		}
		if (pageTodo.getResult().size() < num) {
			for (int i = 0; i < num - pageTodo.getResult().size(); i++) { // 获取在条数范围内的列表
				innerHtml
						.append("<table width=\"100%\" class=\"linkdiv\" style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml.append("	&nbsp;");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("	</table>");
			}
		}

		return renderHtml(innerHtml.toString());
	}

	/**
	 * @author:luosy
	 * @description: “待办事宜”中显示，且急件需要“标红、置顶”；
	 * @date : 2011-7-3
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 * @modify yanjian 2011-09-14 19:55 “待办事宜”增加红黄牌预警提示功能
	 * @modify yanjian 2011-09-25 9:15 过滤标题中的换行符
	 */
	@SuppressWarnings("unchecked")
	public String getTodoByRedType() throws Exception {
		logger
				.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.getTodoByRedType() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理桌面待办事宜显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否字体大小
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		ParamBean parambean = new ParamBean();
		parambean.setLength(length);
		parambean.setRootPath(getRequest().getContextPath());
		parambean.setNum(num);
		parambean.setType(type);
		parambean.setShowCreator(showCreator);
		parambean.setShowDate(showDate);
		parambean.setSectionFontSize(sectionFontSize);
		parambean.setWorkflowType(workflowType);
		Page<TaskBusinessBean> page = sendDocUploadManager.getTodoByRedType(
				workflowType, type, num);
		StringBuffer innerHtml = new StringBuffer();
		sendDocHtmlManager.loadDesktopTodoHtml(page, innerHtml, parambean);
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"
				+ (System.currentTimeMillis() - start) + "ms{@@@@@@}");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * @description 显示列表格式的待办事宜
	 * @method showTableTodo
	 * @author 申仪玲(shenyl)
	 * @created 2012-5-31 下午02:32:50
	 * @return
	 * @throws Exception
	 * @return String
	 * @throws Exception
	 */
	public String showTableTodo() throws Exception {
		logger
				.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.getTodoByRedType() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理桌面待办事宜显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		this.workflowType = getRequest().getParameter("workflowType");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否字体大小
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		ParamBean parambean = new ParamBean();
		parambean.setLength(length);
		parambean.setRootPath(getRequest().getContextPath());
		parambean.setNum(num);
		parambean.setType(type);
		parambean.setShowCreator(showCreator);
		parambean.setShowDate(showDate);
		parambean.setSectionFontSize(sectionFontSize);
		parambean.setWorkflowType(workflowType);
		Page<TaskBusinessBean> page = sendDocUploadManager.getTodoByRedType(
				workflowType, type, num);
		StringBuffer innerHtml = new StringBuffer();
		if (innerHtml == null) {
			throw new NullPointerException("参数innerHtml不能为null");
		}
		List resList = page.getResult();
		if (resList != null) {
			innerHtml.append("<table width=\"100%\" style=\"font-size:"
					+ sectionFontSize
					+ "px\" cellpadding=\"0\" cellspacing=\"0\">");
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = resList.iterator(); iterator.hasNext();) {
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				innerHtml.append("<tr height=\"25px\">");
				innerHtml.append("<td width=\"60%\">");
				/* 显示红黄蓝图标 */
				// sendDocIcoManager.loadProcessedRedYellowGreenIco(innerHtml,
				// taskbusinessbean,
				// parambean.getRootPath());
				// sendDocIcoManager.loadToDoIco(innerHtml, taskbusinessbean,
				// parambean.getRootPath());
				/* 显示流程类型和标题 */
				// 流程类型
				StringBuilder clickProcessType = sendDocLinkManager
						.genTodoWorkflowNameLink(taskbusinessbean, parambean
								.getRootPath(), parambean.getType());
				// 标题
				StringBuilder clickTitle = sendDocLinkManager
						.genTodoClickTitle(parambean.getRootPath(),
								taskbusinessbean, parambean.getType());
				String objTitle = taskbusinessbean.getBusinessName();
				String title = "";
				// 上一步处理人信息
				String PreTaskActor = taskbusinessbean.getPreTaskActorInfo();
				// 来文单位
				String unit = sendDocLinkManager.genTodoClickUnit(parambean
						.getRootPath(), taskbusinessbean, parambean.getType());
				if (unit != null && unit.length() > 7) {
					unit = unit.substring(0, 6) + "...";
				}
				String workflowTitle = taskbusinessbean.getWorkflowAliaName();
				if (workflowTitle.length() > 4) {
					workflowTitle = workflowTitle.substring(0, 4);
				}
				if (objTitle.startsWith("<red>")) {
					objTitle = objTitle.replace("<red>", "");
					// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = objTitle == null ? "" : objTitle.toString();
					if (title.length() > parambean.getLength()
							&& parambean.getLength() > 0) {
						title = title.substring(0, parambean.getLength())
								+ "...";
					}
					title = title.replace("\\r\\n", " ");
					title = title.replace("\\n", " ");
					innerHtml
							.append("<span>")
							.append("<a href=\"#\" onclick=\"")
							.append(clickProcessType.toString())
							.append("\"")
							.append("title=\"")
							.append(taskbusinessbean.getWorkflowAliaName())
							.append("\">[")
							.append(workflowTitle)
							.append("]</a>")
							.append("")
							.append(
									"<a href=\"#\" style=\"color: red \" onclick=\"")
							.append(clickTitle.toString()).append("\"").append(
									"title=\"").append(objTitle + PreTaskActor)
							.append("\">").append("(急)").append(title).append(
									"").append("</a></span>");
					innerHtml.append("</td>");
					innerHtml.append("<td width=\"15%\">").append(unit).append(
							"</td>");
				} else {
					title = objTitle == null ? "" : objTitle.toString();
					if (title.length() > parambean.getLength()
							&& parambean.getLength() > 0) {
						title = title.substring(0, parambean.getLength())
								+ "...";
					}
					innerHtml.append("<span>").append(
							"<a href=\"#\" onclick=\"").append(
							clickProcessType.toString()).append("\"").append(
							"title=\"").append(
							taskbusinessbean.getWorkflowAliaName()).append(
							"\">[").append(workflowTitle).append("]</a>")
							.append("").append("<a href=\"#\" onclick=\"")
							.append(clickTitle.toString()).append("\"").append(
									"title=\"").append(objTitle + PreTaskActor)
							.append("\">").append("").append(title).append("")
							.append("</a></span>");
					innerHtml.append("</td>");

					innerHtml.append("<td width=\"15%\">").append(
							(unit == null) ? "" : unit).append("</td>");
				}
				/* 显示图标 */
				innerHtml.append("<td align=\"left\" width=\"15%\">");
				sendDocIcoManager.loadToDoIco(innerHtml, taskbusinessbean,
						parambean.getRootPath());
				// //限时办理图标
				// Properties properties =
				// FormGridDataHelper.getColorSetProperties();
				// String picPath = null;
				// if (taskbusinessbean.getEndTime() != null &&
				// !"".equals(taskbusinessbean.getEndTime())) {
				// StringBuilder titleShow = new StringBuilder();
				// picPath = properties.getProperty("clock");
				// String recTime = new SimpleDateFormat("yyyy-MM-dd")
				// .format(taskbusinessbean.getWorkflowStartDate());
				// titleShow.append(properties.getProperty("clock_titleShow").replace(
				// "{1}", recTime).replace("{2}",
				// taskbusinessbean.getEndTime().toString()));
				// innerHtml
				// .append("<img src=")
				// .append(parambean.getRootPath())
				// .append(picPath)
				// .append(
				// " title="
				// + titleShow
				// + " style=vertical-align:middle;/> ");
				// }else{
				// picPath = properties.getProperty("noclock");
				// innerHtml
				// .append("<img src=")
				// .append(parambean.getRootPath())
				// .append(picPath)
				// .append(
				// " title=\"非限时办理\" style=vertical-align:middle;/> ");
				// }
				// //退文图标
				// if (taskbusinessbean.getIsBackspace() != null &&
				// taskbusinessbean.getIsBackspace().equals("1")) {
				// picPath = properties.getProperty("back");
				// innerHtml
				// .append("<img src=")
				// .append(parambean.getRootPath())
				// .append(picPath)
				// .append(" ")
				// .append(" title='")
				// .append(sendDocIcoManager.showBackInfo(taskbusinessbean.getTaskId()))
				// // 退回信息
				// .append("' ")
				// .append("style=vertical-align:middle;/> ");
				// }else{
				// picPath = properties.getProperty("noback");
				// innerHtml
				// .append("<img src=")
				// .append(parambean.getRootPath())
				// .append(picPath)
				// .append(
				// " title=\"非退文\" style=vertical-align:middle;/> ");
				// }
				// //代办图标
				// if (taskbusinessbean.getAssignType() != null &&
				// taskbusinessbean.getAssignType().equals("0")) {
				// picPath = properties.getProperty("instead");
				// innerHtml
				// .append("<img src=")
				// .append(parambean.getRootPath())
				// .append(picPath)
				// .append(
				// " style=vertical-align:middle;/> ");
				// }else{
				// picPath = properties.getProperty("noinstead");
				// innerHtml
				// .append("<img src=")
				// .append(parambean.getRootPath())
				// .append(picPath)
				// .append(
				// " title=\"非代办\" style=vertical-align:middle;/> ");
				// }
				innerHtml.append("</td>");
				/* 显示拟稿人 */
				String name = taskbusinessbean.getStartUserName();
				if (taskbusinessbean.getStartUserName() == null) {
					name = adapterBaseWorkflowManager.getUserService()
							.getCurrentUser().getUserName();
				}
				innerHtml.append("<td align=\"left\">");
				innerHtml.append("<span>").append(name).append("</span>");
				innerHtml.append("</td>");
				// sendDocLinkManager.genTodoTitle(innerHtml, taskbusinessbean,
				// parambean);
				innerHtml.append("</tr>");
			}
			innerHtml.append("</table>");
		}
		// 【更多】跳转连接
		sendDocLinkManager.genTodoClickMoreLink(innerHtml, parambean
				.getRootPath(), parambean.getWorkflowType(), parambean
				.getType());
		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(
						page == null ? 0 : (page.getTotalCount() == -1 ? 0
								: page.getTotalCount())).append("\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"
				+ (System.currentTimeMillis() - start) + "ms{@@@@@@}");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * @description 显示列表格式的 征求意见流程待签收文件
	 * @method showTableTodoZQSign
	 * @author 肖利建(xiaolj)
	 * @created 2012-12-30 下午18:02:05
	 * @return
	 * @throws Exception
	 * @return String
	 * @throws Exception
	 */
	public String showTableTodoZQSign() throws Exception {
		logger
				.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.getTodoByRedType() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理桌面待办事宜显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		this.workflowType = getRequest().getParameter("workflowType");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否字体大小
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}

		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		ParamBean parambean = new ParamBean();
		parambean.setLength(length);
		parambean.setRootPath(getRequest().getContextPath());
		parambean.setNum(num);
		parambean.setType(type);
		parambean.setShowCreator(showCreator);
		parambean.setShowDate(showDate);
		parambean.setSectionFontSize(sectionFontSize);
		parambean.setWorkflowType(workflowType);
		Page<TaskBusinessBean> page = sendDocUploadManager.getTodoByRedType(
				workflowType, type, num);
		StringBuffer innerHtml = new StringBuffer();
		if (innerHtml == null) {
			throw new NullPointerException("参数innerHtml不能为null");
		}
		List<TaskBusinessBean> resList = page.getResult();
		// 过滤征求意见流程流程，且状态为未签收
		for (int i = 0; i < resList.size(); i++) {
			String workflowNameSign = resList.get(i).getWorkflowName();
			String isRev = resList.get(i).getIsReceived();
			if ("征求意见流程".equals(workflowNameSign)
					&& ("".equals(isRev) || isRev == null)) {
			} else {
				resList.remove(i);
				i--;
			}
		}
		if (resList != null) {
			innerHtml.append("<table width=\"100%\" style=\"font-size:"
					+ sectionFontSize
					+ "px\" cellpadding=\"0\" cellspacing=\"0\">");
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = resList.iterator(); iterator.hasNext();) {
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				innerHtml.append("<tr height=\"25px\">");
				innerHtml.append("<td width=\"65%\">");
				/* 显示流程类型和标题 */
				// 流程类型
				/*
				 * StringBuilder clickProcessType =
				 * sendDocLinkManager.genTodoWorkflowNameLink( taskbusinessbean,
				 * parambean.getRootPath(), parambean.getType());
				 */
				String clickProcessType = "window.parent.refreshWorkByTitle('"
						+ getRequest().getContextPath()
						+ "/senddoc/sendDoc!todo.action?workflowflag=0&formId=t_oarecvdoc&isReceived=0','待签收文件');";
				// 标题
				StringBuilder clickTitle = sendDocLinkManager
						.genTodoClickTitle(parambean.getRootPath(),
								taskbusinessbean, parambean.getType());
				String objTitle = taskbusinessbean.getBusinessName();
				String title = "";
				// 上一步处理人信息
				String PreTaskActor = taskbusinessbean.getPreTaskActorInfo();

				String workflowTitle = taskbusinessbean.getWorkflowAliaName();
				if (workflowTitle.length() > 4) {
					workflowTitle = workflowTitle.substring(0, 4);
				}
				if (objTitle.startsWith("<red>")) {
					objTitle = objTitle.replace("<red>", "");
					// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = objTitle == null ? "" : objTitle.toString();
					if (title.length() > parambean.getLength()
							&& parambean.getLength() > 0) {
						title = title.substring(0, parambean.getLength())
								+ "...";
					}
					title = title.replace("\\r\\n", " ");
					title = title.replace("\\n", " ");
					innerHtml
							.append("<span>")
							.append("<a href=\"#\" onclick=\"")
							.append(clickProcessType)
							.append("\"")
							.append("title=\"")
							.append(taskbusinessbean.getWorkflowAliaName())
							.append("\">[")
							.append(workflowTitle)
							.append("]</a>")
							.append("")
							.append(
									"<a href=\"#\" style=\"color: red \" onclick=\"")
							.append(clickTitle.toString()).append("\"").append(
									"title=\"").append(objTitle + PreTaskActor)
							.append("\">").append("(急)").append(title).append(
									"").append("</a></span>");
					innerHtml.append("</td>");
				} else {
					title = objTitle == null ? "" : objTitle.toString();
					if (title.length() > parambean.getLength()
							&& parambean.getLength() > 0) {
						title = title.substring(0, parambean.getLength())
								+ "...";
					}
					innerHtml.append("<span>").append(
							"<a href=\"#\" onclick=\"")
							.append(clickProcessType).append("\"").append(
									"title=\"").append(
									taskbusinessbean.getWorkflowAliaName())
							.append("\">[").append(workflowTitle).append(
									"]</a>").append("").append(
									"<a href=\"#\" onclick=\"").append(
									clickTitle.toString()).append("\"").append(
									"title=\"").append(objTitle + PreTaskActor)
							.append("\">").append("").append(title).append("")
							.append("</a></span>");
					innerHtml.append("</td>");
				}
				/* 显示图标 */
				innerHtml.append("<td align=\"left\" width=\"20%\">");
				sendDocIcoManager.loadToDoIco(innerHtml, taskbusinessbean,
						parambean.getRootPath());
				innerHtml.append("</td>");
				/* 显示拟稿人 */
				String name = taskbusinessbean.getStartUserName();
				if (taskbusinessbean.getStartUserName() == null) {
					name = adapterBaseWorkflowManager.getUserService()
							.getCurrentUser().getUserName();
				}
				innerHtml.append("<td align=\"left\">");
				innerHtml.append("<span>").append(name).append("</span>");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
			}
			innerHtml.append("</table>");

		}
		// 【更多】跳转连接
		sendDocLinkManager.genTodoClickMoreLink(innerHtml, parambean
				.getRootPath(), parambean.getWorkflowType(), parambean
				.getType());
		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(
						page == null ? 0 : (page.getTotalCount() == -1 ? 0
								: page.getTotalCount())).append("\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"
				+ (System.currentTimeMillis() - start) + "ms{@@@@@@}");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * @description 显示列表格式的 公文转办流程待签收文件
	 * @method showTableTodoZBSign
	 * @author 肖利建(xiao)
	 * @created 2012-12-30 下午17:36:53
	 * @return
	 * @throws Exception
	 * @return String
	 * @throws Exception
	 */
	public String showTableTodoZBSign() throws Exception {
		logger
				.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.getTodoByRedType() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理桌面待办事宜显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		this.workflowType = getRequest().getParameter("workflowType");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否字体大小
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		ParamBean parambean = new ParamBean();
		parambean.setLength(length);
		parambean.setRootPath(getRequest().getContextPath());
		parambean.setNum(num);
		parambean.setType(type);
		parambean.setShowCreator(showCreator);
		parambean.setShowDate(showDate);
		parambean.setSectionFontSize(sectionFontSize);
		parambean.setWorkflowType(workflowType);
		Page<TaskBusinessBean> page = sendDocUploadManager.getTodoByRedType(
				workflowType, type, num);
		StringBuffer innerHtml = new StringBuffer();

		if (innerHtml == null) {
			throw new NullPointerException("参数innerHtml不能为null");
		}
		List<TaskBusinessBean> resList = page.getResult();
		// 过滤出公文转办流程，且状态为未签收
		for (int i = 0; i < resList.size(); i++) {
			String workflowNameSign = resList.get(i).getWorkflowName();
			String isRev = resList.get(i).getIsReceived();
			if ("公文转办流程".equals(workflowNameSign)
					&& ("".equals(isRev) || isRev == null)) {
			} else {
				resList.remove(i);
				i--;
			}
		}
		if (resList != null) {
			innerHtml.append("<table width=\"100%\" style=\"font-size:"
					+ sectionFontSize
					+ "px\" cellpadding=\"0\" cellspacing=\"0\">");
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = resList.iterator(); iterator.hasNext();) {
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				innerHtml.append("<tr height=\"25px\">");
				innerHtml.append("<td width=\"65%\">");
				/* 显示流程类型和标题 */
				// 流程类型
				/*
				 * StringBuilder clickProcessType =
				 * sendDocLinkManager.genTodoWorkflowNameLink( taskbusinessbean,
				 * parambean.getRootPath(), parambean.getType());
				 */
				String clickProcessType = "window.parent.refreshWorkByTitle('"
						+ getRequest().getContextPath()
						+ "/senddoc/sendDoc!todo.action?workflowflag=1&formId=t_oarecvdoc&isReceived=0','待签收文件');";
				// 标题
				StringBuilder clickTitle = sendDocLinkManager
						.genTodoClickTitle(parambean.getRootPath(),
								taskbusinessbean, parambean.getType());
				String objTitle = taskbusinessbean.getBusinessName();
				String title = "";
				// 上一步处理人信息
				String PreTaskActor = taskbusinessbean.getPreTaskActorInfo();
				String workflowTitle = taskbusinessbean.getWorkflowAliaName();
				if (workflowTitle.length() > 4) {
					workflowTitle = workflowTitle.substring(0, 4);
				}
				if (objTitle.startsWith("<red>")) {
					objTitle = objTitle.replace("<red>", "");

					// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = objTitle == null ? "" : objTitle.toString();
					if (title.length() > parambean.getLength()
							&& parambean.getLength() > 0) {
						title = title.substring(0, parambean.getLength())
								+ "...";
					}
					title = title.replace("\\r\\n", " ");
					title = title.replace("\\n", " ");
					innerHtml
							.append("<span>")
							.append("<a href=\"#\" onclick=\"")
							.append(clickProcessType)
							.append("\"")
							.append("title=\"")
							.append(taskbusinessbean.getWorkflowAliaName())
							.append("\">[")
							.append(workflowTitle)
							.append("]</a>")
							.append("")
							.append(
									"<a href=\"#\" style=\"color: red \" onclick=\"")
							.append(clickTitle.toString()).append("\"").append(
									"title=\"").append(objTitle + PreTaskActor)
							.append("\">").append("(急)").append(title).append(
									"").append("</a></span>");
					innerHtml.append("</td>");
				} else {
					title = objTitle == null ? "" : objTitle.toString();
					if (title.length() > parambean.getLength()
							&& parambean.getLength() > 0) {
						title = title.substring(0, parambean.getLength())
								+ "...";
					}
					innerHtml.append("<span>").append(
							"<a href=\"#\" onclick=\"")
							.append(clickProcessType).append("\"").append(
									"title=\"").append(
									taskbusinessbean.getWorkflowAliaName())
							.append("\">[").append(workflowTitle).append(
									"]</a>").append("").append(
									"<a href=\"#\" onclick=\"").append(
									clickTitle.toString()).append("\"").append(
									"title=\"").append(objTitle + PreTaskActor)
							.append("\">").append("").append(title).append("")
							.append("</a></span>");
					innerHtml.append("</td>");
				}
				/* 显示图标 */
				innerHtml.append("<td align=\"left\" width=\"20%\">");
				sendDocIcoManager.loadToDoIco(innerHtml, taskbusinessbean,
						parambean.getRootPath());
				innerHtml.append("</td>");
				/* 显示拟稿人 */
				String name = taskbusinessbean.getStartUserName();
				if (taskbusinessbean.getStartUserName() == null) {
					name = adapterBaseWorkflowManager.getUserService()
							.getCurrentUser().getUserName();
				}
				innerHtml.append("<td align=\"left\">");
				innerHtml.append("<span>").append(name).append("</span>");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
			}
			innerHtml.append("</table>");
		}
		// 【更多】跳转连接
		sendDocLinkManager.genTodoClickMoreLink(innerHtml, parambean
				.getRootPath(), parambean.getWorkflowType(), parambean
				.getType());
		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(
						page == null ? 0 : (page.getTotalCount() == -1 ? 0
								: page.getTotalCount())).append("\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"
				+ (System.currentTimeMillis() - start) + "ms{@@@@@@}");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 根据表单模板展现表单数据
	 * 
	 * @author:邓志城
	 * @date:2011-5-11 下午04:46:55
	 * @return
	 */
	public String viewFormData() {
		if (instanceId == null) {
			logger.error("流程实例id为空！");
			throw new SystemException("流程实例id为空！");
		}
		String indexParentInstanceId = getRequest().getParameter("index");
		List parentInstance = adapterBaseWorkflowManager.getWorkflowService()
				.getMonitorParentInstanceIds(new Long(instanceId));
		if (parentInstance.isEmpty()) {
			throw new SystemException("对不起，父流程不存在。");
		}
		if (indexParentInstanceId == null
				|| indexParentInstanceId.length() == 0
				|| "1".equals(indexParentInstanceId)
				|| "null".equals(indexParentInstanceId)) {
			Object[] processInstanceInfo = (Object[]) parentInstance.get(0);
			String parentInstanceId = processInstanceInfo[0].toString();
			ProcessInstance processInstance = adapterBaseWorkflowManager
					.getWorkflowService()
					.getProcessInstanceId(parentInstanceId);
			businessName = (String) processInstance.getContextInstance()
					.getVariable(
							"@{personDemo}_" + instanceId + "_"
									+ parentInstanceId);
		} else {
			int index = Integer.parseInt(indexParentInstanceId);
			if (index > parentInstance.size()) {
				throw new SystemException("当前流程父流程个数为:" + parentInstance.size()
						+ ";表单中设置的关联父流程数为：" + index + ".超出最大数！");
			}
			if (index >= 2) {
				String topProcessInstanceId = ((Object[]) parentInstance
						.get(index - 1))[0].toString();
				ProcessInstance processInstance = adapterBaseWorkflowManager
						.getWorkflowService().getProcessInstanceId(
								topProcessInstanceId);
				businessName = (String) processInstance.getContextInstance()
						.getVariable(
								"@{personDemo}_"
										+ ((Object[]) parentInstance
												.get(index - 2))[0].toString()
										+ "_" + topProcessInstanceId);
			}
		}
		logger.info(businessName);
		if (businessName == null || businessName.length() == 0) {
			throw new SystemException("对不起，父流程表单数据不存在，请检查流程设置。");
		}
		return "viewformdata";
	}

	/**
	 * @description 查看原表单
	 * @author 严建
	 * @createTime Dec 1, 2011 10:07:07 PM
	 * @return String
	 */
	public String viewParentFormData() {
		if (instanceId == null) {
			logger.error("流程实例id为空！");
			throw new SystemException("流程实例id为空！");
		}
		if (instanceId != null && !instanceId.equals("")) {
			ContextInstance cxt = adapterBaseWorkflowManager
					.getWorkflowService().getContextInstance(instanceId);
			ProcessInstance pi = cxt.getProcessInstance();
			String information = JsonUtil
					.generateApproveToJsonBase64(adapterBaseWorkflowManager
							.getWorkflowService().getWorkflowApproveinfo(
									String.valueOf(pi.getId()), instanceId));
			businessName = pi.getBusinessId() + ";" + pi.getMainFormId()
					+ "@begin@" + information;
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if (jsonArray != null && !jsonArray.isEmpty()) {
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			} else {
				String demo = (String) pi.getContextInstance().getVariable(
						"@{personDemo}");
				String parentinstanceId = "";
				if (!"".equals(demo) && null != demo) {
					String[] demos = demo.split(";");
					if (demos.length > 4) {
						parentinstanceId = demos[4];
						parentinstanceId = parentinstanceId.split("@")[0];

						jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
						personDemo = jsonArray.toString();
						parentInstanceId = "exists";
					}
				}
			}
			workflowName = pi.getName();
			String[] tableInfo = businessName.split(";");
			bussinessId = tableInfo[0] + ";" + tableInfo[1] + ";"
					+ tableInfo[2] + "";
			tableName = tableInfo[0];
			pkFieldName = tableInfo[1];
			pkFieldValue = tableInfo[2];
		}
		return "viewformdata";
	}

	public String viewPersonDemo() {
		personDemo = manager.getFormData(tableName, pkFieldName, pkFieldValue);
		if (personDemo == null) {
			personDemo = "";
		}
		return renderText(personDemo);
	}

	/**
	 * 在Senddoc-input.jsp中嵌入了工作流选择界面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String workflow() throws Exception {
		User user = adapterBaseWorkflowManager.getUserService()
				.getCurrentUser();
		ideaLst = adapterBaseWorkflowManager.getApprovalSuggestionService()
				.getAppSuggestionListByUserId(user.getUserId());
		// yanjian 2011-11-23 15:58 处理原有数据库中数据
		for (int j = 0; j < ideaLst.size(); j++) {
			ToaApprovalSuggestion suggestionBean = (ToaApprovalSuggestion) ideaLst
					.get(j);
			String suggestion = suggestionBean.getSuggestionContent();
			suggestion = suggestion.replaceAll("\r", "");// 处理审批意见有回车的情况
			suggestion = suggestion.replaceAll("\n", " ");// 处理审批意见有换行的情况
			String[] FF_String = new String[] { "\'", "\"", "<", ">", "\\\\" };// 特殊字符
			String[] NFF_String = new String[] { "’", "”", "＜", "＞", "＼" };// 替换字符
			for (int i = 0; i < FF_String.length; i++) {
				if (suggestion.indexOf(FF_String[i]) != -1) {
					suggestion = suggestion.replaceAll(FF_String[i],
							NFF_String[i]);
				}
			}
			suggestionBean.setSuggestionContent(suggestion);
		}
		if (pkFieldValue != null && pkFieldValue.length() > 0) {
			Map map = manager.getSystemField(pkFieldName, pkFieldValue,
					tableName);
			businessName = (String) map.get(BaseWorkflowManager.WORKFLOW_TITLE);// 得到标题
		}
		// 处理取回流程时，得到原来的意见显示在意见输入框中
		if (taskId != null && !"".equals(taskId) && "null".equals(taskId)) {
			List<TwfInfoApproveinfo> wfApproveInfo = adapterBaseWorkflowManager
					.getWorkflowService().getDataByHql(
							"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
							new Object[] { new Long(taskId) });
			if (!wfApproveInfo.isEmpty()) {
				exitSuggestion = wfApproveInfo.get(0).getAiContent();
			}
		}
		return "toworkflow";
	}

	public void saveSuggestion() throws Exception {
		String ret = "0";
		try {
			String[] info = getManager()
					.getFormIdAndBussinessIdByTaskId(taskId);
			bussinessId = info[0];
			String isNewForm = "0";
			if ("0".equals(bussinessId)) {
				isNewForm = "1";
			}
			JSONObject json = new JSONObject();
			json.put("suggestion", suggestion);
			json.put("CAInfo", getCASignInfo() == null ? "" : getCASignInfo());
			String recordId = adapterBaseWorkflowManager.getWorkflowService()
					.handleProcess(
							taskId,
							isNewForm,
							formId,
							bussinessId,
							json.toString(),
							adapterBaseWorkflowManager.getUserService()
									.getCurrentUser().getUserId());
			ret = recordId;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ret = "-1";
		}
		this.renderText(ret);
	}

	/**
	 * 保存公文草稿:公文分发，新建后保存
	 */
	public String save() {
		if ("".equals(model.getSenddocId())) {
			model.setSenddocId(null);
		}
		try {
			FileInputStream fs = new FileInputStream(this.getWordDoc());
			byte[] fileBuffer = new byte[(int) this.getWordDoc().length()];
			fs.read(fileBuffer);

			model.setSenddocContent(fileBuffer);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		model.setSenddocOfficialTime(new Date());
		model.setSenddocState(WorkFlowTypeConst.SENDDOCDRAFT);
		theSendDocManager.saveDoc(model);
		StringBuffer message = new StringBuffer("");
		message.append("{").append("id: '").append(model.getSenddocId())
				.append("',").append("title: '")
				.append(model.getSenddocTitle()).append("',").append(
						"success: '").append("yes'").append("}");
		this.renderText(message.toString());
		return null;
	}

	public void updateSuggestion() {
		String ret = "0";
		try {
			TwfInfoApproveinfo info = adapterBaseWorkflowManager
					.getWorkflowService().getApproveInfoById(bussinessId);
			if (info == null) {
				throw new SystemException("意见记录未找到。");
			}
			info.setAiContent(suggestion);
			info.setAiDate(new Date());
			adapterBaseWorkflowManager.getWorkflowService().saveApproveInfo(
					info);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ret = "-1";
		}
		this.renderText(ret);
	}

	public String toSuggestion() throws Exception {
		User user = adapterBaseWorkflowManager.getUserService()
				.getCurrentUser();
		ideaLst = adapterBaseWorkflowManager.getApprovalSuggestionService()
				.getAppSuggestionListByUserId(user.getUserId());
		// yanjian 2011-11-23 15:58 处理原有数据库中数据
		for (int j = 0; j < ideaLst.size(); j++) {
			ToaApprovalSuggestion suggestionBean = (ToaApprovalSuggestion) ideaLst
					.get(j);
			String suggestion = suggestionBean.getSuggestionContent();
			suggestion = suggestion.replaceAll("\r", "");// 处理审批意见有回车的情况
			suggestion = suggestion.replaceAll("\n", " ");// 处理审批意见有换行的情况
			String[] FF_String = new String[] { "\'", "\"", "<", ">", "\\\\" };// 特殊字符
			String[] NFF_String = new String[] { "’", "”", "＜", "＞", "＼" };// 替换字符
			boolean isFlag = false;
			for (int i = 0; i < FF_String.length; i++) {
				if (suggestion.indexOf(FF_String[i]) != -1) {
					suggestion = suggestion.replaceAll(FF_String[i],
							NFF_String[i]);
					isFlag = true;
				}
			}
			if (isFlag) {
				suggestionBean.setSuggestionContent(suggestion);
			}
		}
		return "tosuggestion";
	}

	/**
	 * @author 张磊
	 * @created 2012-02-14 上午11:00:14
	 * @description 验证唯一性
	 * @return String 返回类型
	 */
	public String checkDocNumber() throws Exception {
		PrintWriter out = getResponse().getWriter();
		String tName = getRequest().getParameter("tableName");// 表名
		String fieldName = getRequest().getParameter("fieldName");// 所验证字段名
		String fieldValue = getRequest().getParameter("fieldValue");// 验证的值
		String keyName = getRequest().getParameter("keyName");// 主键名
		String keyValue = getRequest().getParameter("keyValue");// 主键值
		int count = manager.getCheckValue(tName, fieldName, fieldValue,
				keyName, keyValue);
		if (count > 0) {
			out.print(0);// 表示重复
		} else {
			out.print(1);// 表此值不存在
		}
		return null;
	}

	/**
	 * 收文办件登记退到公文公文分面结点
	 * 
	 * @author 肖利建
	 * @return
	 * @throws Exception
	 * @createTime 2013-01-30 19:11:53
	 */
	public String tuiwen() {
		return "retrecv";
	}

	/**
	 * @author 肖利建
	 * @created 2012-12-24 上午20:35:14
	 * @description 展示机构树
	 * @return String 返回类型
	 */
	public String orgTree() throws Exception {
		orgList = user.getOrgAgency("0", "1");
		orgGroupList = docManager.getAgencyListByUserId();
		return "orgtree";
	}

	/**
	 * @author 李骏
	 * @created 2012-12-24 上午20:35:14
	 * @description 展示意见WORD输入框
	 * @return String 返回类型
	 */
	public String yijian() throws Exception {
		return "yijian";
	}

	/**
	 * @author 李骏
	 * @created 2012-12-24 上午20:35:14
	 * @description 提交保存意见
	 * @return String 返回类型
	 */
	public String doyijian() throws Exception {
		attachManager.saveConAttach(this.getWordDoc(), docid);
		return this.renderText("0");
	}

	/**
	 * author:luosy description: modifyer: description:
	 * 
	 * @return
	 */
	public String endProcessInstance() {
		/**
		 * Parameters: instanceId 流程实例Id flag 改变标识 “1”：挂起流程 “2”：恢复流程 “3”：结束流程
		 * Returns: boolean 改变成功、失败
		 */
		System.out.println(instanceId + "\n");
		boolean ret = adapterBaseWorkflowManager.getWorkflowService()
				.changeProcessInstanceStatus(instanceId, "3");
		return this.renderText(ret + "");
	}

	/**
	 * 
	 * @author：yuhz
	 * @time：May 7, 200911:44:44 AM
	 * @desc:
	 * @return String
	 */
	public String gotoPrintConfig() {
		return "printConfig";
	}

	public String getPrivilegeInfo() {
		return privilegeInfo;
	}

	public void setPrivilegeInfo(String privilegeInfo) {
		this.privilegeInfo = privilegeInfo;
	}

	public String getPrinted() {
		return printed;
	}

	public void setPrinted(String printed) {
		this.printed = printed;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Autowired
	public void setIsystemsetService(ISystemsetService isystemsetService) {
		this.isystemsetService = isystemsetService;
	}

	@Override
	protected BaseManager getManager() {
		return this.manager;
	}

	@Override
	protected String getModuleType() {
		return String.valueOf(WorkFlowTypeConst.SENDDOC);
	}

	@Override
	public String getWorkflowType() {
		return this.workflowType;
	}

	@Override
	public List<Object[]> getWorkflowTypeList() {
		return workflowTypeList;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	@Override
	public String getDictType() {
		return WorkFlowTypeConst.SENDDOCIDEA;
	}

	public List getShowColumnList() {
		return showColumnList;
	}

	public void setShowColumnList(List showColumnList) {
		this.showColumnList = showColumnList;
	}

	public List<EFormComponent> getQueryColumnList() {
		return queryColumnList;
	}

	public Page getWorkflowPage() {
		return workflowPage;
	}

	public void setWorkflowPage(Page workflowPage) {
		this.workflowPage = workflowPage;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Date getTaskStartDateEnd() {
		return taskStartDateEnd;
	}

	public void setTaskStartDateEnd(Date taskStartDateEnd) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(taskStartDateEnd);
		Cal.set(java.util.Calendar.HOUR, 23);
		Cal.set(java.util.Calendar.MINUTE, 59);
		this.taskStartDateEnd = Cal.getTime();
	}

	public Date getTaskStartDateStart() {
		return taskStartDateStart;
	}

	public void setTaskStartDateStart(Date taskStartDateStart) {
		this.taskStartDateStart = taskStartDateStart;
	}

	public Date getProcessStartDate() {
		return processStartDate;
	}

	public void setProcessStartDate(Date processStartDate) {
		this.processStartDate = processStartDate;
	}

	public Date getProcessEndDate() {
		return processEndDate;
	}

	public void setProcessEndDate(Date processEndDate) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(processEndDate);
		Cal.set(java.util.Calendar.HOUR, 23);
		Cal.set(java.util.Calendar.MINUTE, 59);
		this.processEndDate = Cal.getTime();
	}

	public String getTaskNodeName() {
		return taskNodeName;
	}

	public void setTaskNodeName(String taskNodeName) {
		this.taskNodeName = taskNodeName;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	public String getWorkflowCode() {
		return workflowCode;
	}

	public void setWorkflowCode(String workflowCode) {
		this.workflowCode = workflowCode;
	}

	public String getUserJob() {
		return userJob;
	}

	public void setUserJob(String userJob) {
		this.userJob = userJob;
	}

	public String getIsNeedSign() {
		return isNeedSign;
	}

	public void setIsNeedSign(String isNeedSign) {
		this.isNeedSign = isNeedSign;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getIsFind() {
		return isFind;
	}

	public void setIsFind(String isFind) {
		this.isFind = isFind;
	}

	public String getInsId() {
		return insId;
	}

	public void setInsId(String insId) {
		this.insId = insId;
	}

	public String getFirstNodeControlName() {
		return firstNodeControlName;
	}

	public void setFirstNodeControlName(String firstNodeControlName) {
		this.firstNodeControlName = firstNodeControlName;
	}

	public String getPrivilName() {
		try {
			if (privilName != null)
				privilName = URLDecoder.decode(privilName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("流程名称转码异常！");
		}
		return privilName;
	}

	public void setPrivilName(String privilName) {
		this.privilName = privilName;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getSuggestionStyle() {
		return suggestionStyle;
	}

	public void setSuggestionStyle(String suggestionStyle) {
		this.suggestionStyle = suggestionStyle;
	}

	public Date getTaskEndDateEnd() {
		return taskEndDateEnd;
	}

	@SuppressWarnings("deprecation")
	public void setTaskEndDateEnd(Date taskEndDateEnd) {
		if (taskEndDateEnd != null || "".equals(taskEndDateEnd)) {
			taskEndDateEnd.setHours(23);
			taskEndDateEnd.setMinutes(59);
		}
		this.taskEndDateEnd = taskEndDateEnd;
	}

	public Date getTaskEndDateStart() {
		return taskEndDateStart;
	}

	public void setTaskEndDateStart(Date taskEndDateStart) {
		this.taskEndDateStart = taskEndDateStart;
	}

	public String getExitSuggestion() {
		return exitSuggestion;
	}

	public void setExitSuggestion(String exitSuggestion) {
		this.exitSuggestion = exitSuggestion;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public List getOrgList() {
		return orgList;
	}

	public void setOrgList(List orgList) {
		this.orgList = orgList;
	}

	public List getOrgGroupList() {
		return orgGroupList;
	}

	public void setOrgGroupList(List orgGroupList) {
		this.orgGroupList = orgGroupList;
	}

	public String getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(String isReceived) {
		this.isReceived = isReceived;
	}

	/**
	 * 桌面显示 发文办结
	 * 
	 * @author ganyao
	 * @return
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String showDesktopSendDocDoneWork() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		String blockid = getRequest().getParameter("blockid");// 获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockid != null) {
			Map<String, String> map = manager.getDesktopParam(blockid);// 通过blockid获取映射对象
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否显示字体大小
		}
		if (sectionFontSize == null || "".equals("sectionFontSize")
				|| "null".equals("sectionFontSize")) {
			sectionFontSize = "12";
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		// 链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				getRequest().getContextPath()).append(
				"/work/work!listprocessed.action?state=1&workflowType=2")
				.append("', '发文办结'").append(");");

		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType("2");
		parameter.setBusinessName(businessName);
		parameter.setIsSuspended("0");
		parameter.setState("1");
		Page<TaskBusinessBean> page = manager.getProcessedWorks(pageWorkflow,
				parameter, new OALogInfo("得到待办任务列表"));
		String rootPath = getRequest().getContextPath();
		innerHtml = new StringBuffer(
				"	<table width=\"100%\" style=\"font-size:" + sectionFontSize
						+ "px\" cellpadding=\"0\" cellspacing=\"0\">");
		if (page.getResult() != null) {
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = page.getResult().iterator(); iterator
					.hasNext();) {
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				innerHtml.append(" <tr height=\"25px\">");

				// 如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = taskbusinessbean.getBusinessName() == null ? ""
						: taskbusinessbean.getBusinessName();
				title = title.replace("\\r\\n", " ");
				title = title.replace("\\n", " ");
				if (title.length() > length && length > 0) {
					title = title.substring(0, length) + "...";
				}
				if ("".equals(title)) {
					title = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				StringBuilder titleLink = new StringBuilder();
				titleLink.append("var width=screen.availWidth-10;var height=screen.availHeight-30;");
				titleLink
						.append("var a = window.open('")
						.append(getRequest().getContextPath())
						.append("/senddoc/sendDoc!viewProcessed.action?taskId=")
						.append(taskbusinessbean.getTaskId())
						.append("&instanceId=")
						.append(taskbusinessbean.getInstanceId())
						.append("&state=")
						.append(processStatus)
						.append("&searchType=1")
						.append(
								"','processed','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');")
						.append("if(a && a == 'OK'){window.location.reload();}");
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				StringBuilder tip = new StringBuilder();
				tip
						.append(
								taskbusinessbean.getBusinessName() == null ? ""
										: taskbusinessbean.getBusinessName()
												.toString()).append("\n")
						.append(
								"办结时间："
										+ st.format(taskbusinessbean
												.getWorkflowEndDate())).append(
								"\n").append(
								"主办人：" + taskbusinessbean.getMainActorName());
				innerHtml.append("<td >");
				innerHtml.append("	<a href=\"#\" onclick=\"").append(
						titleLink.toString()).append("\">").append(
						"<span style=\"font-size: " + sectionFontSize
								+ "px;color:blue\" title=\"").append(tip)
						.append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				// 显示时间
				innerHtml.append("<td width=\"150px\">");
				innerHtml.append(new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format(taskbusinessbean.getWorkflowStartDate()));
				innerHtml.append("</td>");
				// 清除空格
				String TaskActor = "";
				TaskActor = TaskActor.replaceAll(" ", "");
				if (TaskActor.length() > 3) {
					TaskActor = TaskActor.subSequence(0, 3) + "...";
				}
				List<User> users = adapterBaseWorkflowManager
						.getWorkflowService().getCurrentTaskHandle(
								taskbusinessbean.getInstanceId()).getActors();
				String userNames = "";
				if (users != null && users.size() > 0) {
					for (int i = 0; i < users.size(); i++) {
						User user = adapterBaseWorkflowManager.getUserService()
								.getUserInfoByUserId(users.get(i).getUserId());
						if ("".equals(userNames)) {
							userNames = user.getUserName() + "_"
									+ user.getUserLoginname();
						} else {
							userNames += "," + user.getUserName() + "_"
									+ user.getUserLoginname();
						}
					}
				}
			}
		}
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 查看转办记录
	 * 
	 * @return
	 */
	public String transmitting() {
		manager.transmitting2html(instanceId, getRequest());
		return "annal";
	}

	/**
	 * author:luosy 2013-7-18 description: 启动新流程是设置父表单数据 modifyer: description:
	 * 
	 * @return
	 */
	public String startNewinstanceSetPersonDemo() {
		String subFormId = getRequest().getParameter("subFormId");
		String thisinstanceId = getRequest().getParameter("instanceId");
		String bussId = getRequest().getParameter("bussId");
		String parentinstanceId = getRequest().getParameter("parentinstanceId");
		manager.startNewinstanceSetPersonDemo(subFormId, thisinstanceId,
				parentinstanceId, bussId);
		return renderText("ok");
	}

	/**
	 * 读取PDF快照，并且保存为PDF在临时文件夹，供下载使用
	 * 
	 * @author: qibh
	 * @return
	 * @created: Jan 24, 2014 2:38:25 AM
	 * @version :
	 */
	public String topdf() {
		try {
			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkFieldName = args[1];
			String pkFieldValue = args[2];
			getRequest().setAttribute("backlocation",
					"javascript:history.back();");
			HttpServletResponse response = super.getResponse();
			response.reset();
			response.setContentType("application/x-download"); // windows
			OutputStream output = null;
			response.addHeader("Content-Disposition", "attachment;filename="
					+ businessName + ".pdf");
			output = response.getOutputStream();
			output.write(formPdfService.buildPdf(tableName, "PDFIMAGE",
					pkFieldName, pkFieldValue));
			output.flush();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 獲取用戶Id
	 * 
	 * @return
	 */
	public String getCurrentId() {
		String userId = null;
		User user = new User();
		try {
			user = userService.getCurrentUser();
			userId = user.getUserId();
			return renderText(userId);
		} catch (Exception e) {
			return renderText("-1");// -1表示当前系统获取不到“当前处理人”
		}
	}

	/**
	 * 由 当前任务--taskId 退回上一步 退回上一步，判断上一步是否是不可退回的节点，如果是，不允许退文。
	 * 
	 * @date 2014年2月26日10:12:37 return 是 不可退回节点 1 加上节点名称 nodeName
	 */
	public String checkIsBackspace() {
		String taskId = getRequest().getParameter("taskId");
		// 通过当前任务id 获取上一步任务id
		Long preTaskId = TaskService.getTaskInstanceInfoByTiId(taskId)
				.getPreTaskInstance();
		// 根据任务id 获取节点设置信息
		TwfBaseNodesetting nodeSetting = workflowService
				.getNodesettingByTid(preTaskId.toString());
		nodeName = nodeSetting.getNsNodeName();
		// 不可退回到此节点，1是不可退回，0是可退回
		String chkShowBlankSuggestion = nodeSetting
				.getPlugin("plugins_notbackspace");
		String ret = chkShowBlankSuggestion + ";" + nodeName;
		return this.renderText(ret);
	}

	/**
	 * @author ganyao
	 * @created 2014年3月26日20:04:06
	 * @description 通过任务id(taskId) 得到流程实例id (instanceId)
	 * @return String
	 */
	public String getInstanceIdBytask() {
		String taskIds = getRequest().getParameter("taskId");
		String instanceIds = "";
		String[] task = taskIds.split(",");
		for (int i = 0; i < task.length; i++) {
			// 通过任务id(taskId) 得到流程实例id (instanceId)
			instanceIds = adapterBaseWorkflowManager.getWorkflowService()
					.getProcessInstanceIdByTiId(task[i])
					+ "," + instanceIds;
		}
		if (instanceIds.length() > 1) {
			instanceIds = instanceIds.substring(0, instanceIds.length() - 1);
		}
		return renderText(instanceIds);
	}

	/**
	 * @author ganyao
	 * @created 2014年3月31日11:11:56
	 * @description 通过流程实例id(instanceId) 得到当前任务是否在待办事宜
	 * @return String
	 */
	public String getisExsitTodo() {
		String instanceId = getRequest().getParameter("instanceId");
		String isExsitTodo = "";
		isExsitTodo = manager.getisExsitTodo(instanceId);
		return renderText(isExsitTodo);
	}
}