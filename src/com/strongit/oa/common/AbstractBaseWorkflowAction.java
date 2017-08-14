package com.strongit.oa.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.form.action.BaseAction;
import com.strongit.form.action.FormDataAware;
import com.strongit.oa.archive.tempfile.TempfileTree;
import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarRemind;
import com.strongit.oa.bo.ToaDoctemplate;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.remind.Constants;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.common.service.adapter.bo.EntrustWorkflowParameter;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.common.workflow.parameter.BackSpaceParameter;
import com.strongit.oa.common.workflow.parameter.ReAssignParameter;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.common.workflow.plugin.util.TransitionPluginConst;
import com.strongit.oa.common.workflow.plugin.util.po.OATransitionPlugin;
import com.strongit.oa.common.workflow.service.transitionservice.pojo.TransitionsInfoBean;
import com.strongit.oa.component.formtemplate.util.FormGridDataHelper;
import com.strongit.oa.im.IMManager;
import com.strongit.oa.im.IMMessageService;
import com.strongit.oa.message.IMessageService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.CAAuth;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.viewmodel.util.DateUtil;
import com.strongit.oa.webservice.iphone.server.pushNotify.PushNotifyManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBasePost;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.bo.TwfBaseTransitionPlugin;
import com.strongit.workflow.bo.TwfBaseWorkflow;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.po.ProcessInstanceBean;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 抽取OA业务系统中一些共性操作,以消除重复
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2009-12-12 上午09:22:33
 * @version 2.0.2.3
 * @classpath com.strongit.oa.common.AbstractBaseWorkflowAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Results( {
		@Result(name = "validation", value = "/WEB-INF/jsp/workflow/validation.jsp", type = ServletDispatcherResult.class),
		@Result(name = "pdimageview", value = "/WEB-INF/jsp/workflow/pdimageview.jsp", type = ServletDispatcherResult.class),
		@Result(name = "annal", value = "/WEB-INF/jsp/workflow/annal.jsp", type = ServletDispatcherResult.class),
		@Result(name = "newform", value = "/WEB-INF/jsp/workflow/newworkflowform.jsp", type = ServletDispatcherResult.class),
		@Result(name = "newwizard", value = "/WEB-INF/jsp/workflow/wizard.jsp", type = ServletDispatcherResult.class),
		@Result(name = "annallist", value = "/WEB-INF/jsp/workflow/annallist.jsp", type = ServletDispatcherResult.class),
		@Result(name = "viewHostedByForm", value = "/WEB-INF/jsp/workflow/viewHostedByForm.jsp", type = ServletDispatcherResult.class),
		@Result(name = "viewform", value = "/WEB-INF/jsp/workflow/viewform.jsp", type = ServletDispatcherResult.class),
		@Result(name = "workflow", value = "/WEB-INF/jsp/workflow/choosetree.jsp", type = ServletDispatcherResult.class),
		@Result(name = "singlechoosetree", value = "/WEB-INF/jsp/workflow/singlechoosetree.jsp", type = ServletDispatcherResult.class),
		@Result(name = "towizard", value = "/WEB-INF/jsp/workflow/workflow-wizard.jsp", type = ServletDispatcherResult.class),
		@Result(name = "sessionError", value = "/common/error/406.jsp", type = ServletDispatcherResult.class),
		@Result(name = "tonextstep", value = "/WEB-INF/jsp/workflow/workflow-nextstep.jsp", type = ServletDispatcherResult.class) })
public abstract class AbstractBaseWorkflowAction<T> extends
		BaseActionSupport<T> implements FormDataAware {

	public final static String DEFAULT_DRAFT_ATTACH_NAME = "草稿.doc";

	// 工作流相关属性
	protected String taskId; // 任务ID

	protected String instanceId; // 流程实例ID

	protected String parentInstanceId = ""; // 流程实例ID

	protected String workflowId; // 流程定义ID

	protected String queryFormId;// 流程挂接的查询表单ID
    private String orguserid; // 反馈人ids
	


	protected String viewFormId;// 流程挂接的展现表单ID

	protected String workflowName; // 流程名称

	protected String nodeId; // 任务节点ID

	protected String nodeName; // 任务节点名称
	protected String viewReturn; // 任务节点名称

	protected String transitionId; // 流程流向ID

	protected String transitionName; // 流程流向名称

	protected String concurrentTrans; // 并发流向("流向ID1,流向ID2,...")

	protected String strTaskActors; // 字符格式的处理人信息("人员ID|节点ID,人员ID|节点ID...")

	protected String suggestion; // 审批意见

	protected List taskActors = new ArrayList();// 可选任务处理人员

	protected String isSelectOtherActors; // 是否可以选择其他处理人

	protected String maxTaskActors; // 任务接点可选择最大处理人数目

	protected String workflowType = ""; // 流程类型

	protected String excludeWorkflowType = ""; // 流程类型
	
	protected String excludeWorkflowTypeName = ""; // 流程类型名称

	protected String workflowTypeName;// 流程类型名称

	protected String showSignUserInfo;// 是否显示流程签收人信息

	protected List<Object[]> listAnnal = new ArrayList<Object[]>();// 某流程实例的处理意见列表

	protected String returnFlag;// 允许操作的按钮html代码片段,【同用于协办处室标志】

	protected List<Object[]> workflows = new ArrayList<Object[]>();// 获取所有的可选启动流程列表

	protected String filterSign; // 是否要过滤签收数据 0：否，1：是

	protected String filterYJZX; // 是否要过滤意见征询数据 0：不显示意见征询信息，1：显示意见征询信息

	protected String handleKind;// 处理类型

	protected String personDemo = "";

	private String isGenzong; // 是否跟踪重要文件

	protected List<Object[]> workflowTypeList; // 流程类型列表

	protected List<Object[]> leftWorkflowTypeList; // 右侧流程类型

	protected List<Object[]> rightWorkflowTypeList; // 右侧流程类型

	protected Map<String, List<Object[]>> workflowMap; // 启动流程集合

	protected Map<String, List<Object[]>> leftWorkflowMap;// 左列流程集合

	protected Map<String, List<Object[]>> rightWorkflowMap;// 左列流程集合

	public static final String PLUGIN_SUBMIT_FORMDATA_SET = "plugins_submitformdataset";// 点击迁移线时是否调用OCX#GetData；通过此开关

	// 避免每次提交都调用这个方法,解决附件大时的性能问题.

	// 电子表单相关属性
	protected String formId;// 表单模板ID

	protected String newFormId;// 新表单模板ID

	protected File formData;// 电子表单数据：XML格式

	protected String fullFormData;// 电子表单内容：通过OCX#GetData获取.

	protected List<EForm> formListbyWFType = new ArrayList<EForm>(); // 流程类型下的所有表单

	// 列表显示相关设置
	protected String listMode; // 列表模式，使用四个常量标识

	protected static final int DRAFT_LIST = 0; // 草稿列表

	protected static final int TODO_LIST = 1; // 待办列表

	protected static final int HOSTED_BY_LIST = 2; // 主办列表

	protected static final int PROCESSED_LIST = 3; // 已办列表

	protected static final int DOING_LIST = 4; // 在办列表

	protected static final int ASSIGN_LIST = 5; // 我指派的工作列表

	protected static final int ENTRUST_LIST = 6; // 我委托的工作列表

	protected Page pageWorkflow = new Page(FlexTableTag.MAX_ROWS, true);// 工作流数据列表

	// 提醒相关属性
	protected String urgencytype; // 催办方式

	protected String handlerMes;// 自定义提醒内容

	protected String remindType;// 提醒方式：一般在前台页面用户勾选提醒方式，通过自动节点实现。

	// 查询相关属性
	protected String businessName;// 业务名称(标题)

	protected String userName;// 主办人

	protected Date startDate;// 开始时间

	protected Date endDate;// 结束时间
	
	protected Date workflowEndDatestartDate;//办结 开始时间

	protected Date workflowEndDateendDate;// 办结结束时间
	
	protected String orgName;// 组织机构名称

	protected String processTimeout = "";// 流程是否超时(“0”：否；“1”：是)

	protected String state = ""; // 郑志斌添加字段 为流程查询状态

	protected String noTotal = ""; // 是否进行统计

	protected String type;// 类型；搜索任务类型“0”：待办；“1”：在办；“2”：非办结；“3”：办结；“4”：全部

	protected String assignType;// 委托类型 “0”：委托；“1”：指派；“2”：全部

	protected String initiativeType;// 委托或被委托类型

	// “0”：查询委托给其他人的任务列表；“1”：查询其他人委托给该用户的任务列表

	protected static final String defaultIsBackSpace = "4";

	protected String isBackSpace = defaultIsBackSpace;// 是否是回退的任务“0”：非回退任务；“1”：回退任务；“4”：全部

	// 业务数据相关属性
	protected String tableName;// 业务表名

	protected String pkFieldName;// 业务表主键名

	protected String pkFieldValue;// 业务表主键值

	protected String bussinessId;// 业务数据id：作为参数传递用。格式:tableName;PK;PKValue

	protected List ideaLst;// 保存在字典中的意见

	protected String dictId;// 字典类主键

	protected String doneYear;//办结年份

	protected String pluginInfo;// 节点上挂接的插件信息

	private File wordDoc;// 正文

	protected String searchType = "";// 任务查询时，传递的参数

	protected abstract BaseManager getManager();// 抛出让子类实现

	protected abstract String getWorkflowType();// 抛出让子类实现

	protected abstract String getModuleType();// 抛出让子类实现:判断调用消息的模块

	protected abstract String getDictType();// 抛出让子类实现：得到数据字典中工作流处理意见。WorkFlowTypeConst中定义意见类型

	private String message;// 提示信息内容

	private String userIds;// 要提醒的用户ID

	private String remindSet;// 定时提醒设置

	private String docType;// 打开的OFFICE类型，参见千航控件说明：0: 没有文档； 100 =其他文档类型；

	// 1=word；2=Excel.Sheet或者 Excel.Chart ；
	// 3=PowerPoint.Show； 4= Visio.Drawing；
	// 5=MSProject.Project； 6= WPS Doc；
	// 7:Kingsoft Sheet

	private String CASignInfo; // 签名信息

	private String formAction;

	private String appointUserId; // 指定的用户ID

	private String appointDeptId; // 指定的部门ID

	protected String flag = "false"; // 判断流程主办人员是否当前用户

	private String processSuspend = ""; // 判断是否是挂起任务

	private String rUserId; // 委托人ID;

	private String allowChangeMainActor; // "0":不允许；"1":允许
	
	private String daiBan; //代办| "0":不允许；"1":允许

	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	@Autowired
	private IAttachmentService attachService;
	@Autowired
	private SendDocManager sendDocManager;
	@Autowired
	private MyLogManager logService;
	//推送给ios的manager类
	@Autowired
	private PushNotifyManager pushManager;
	protected OALogInfo logInfo;
	
	private String taskIds;

	private String processOutTime;//add yanjian 添加属性processOutTime保存用户界面提交的流程期限时间
	
	private String tableNames;   //附件表名
	private String idName; 		//表主键 名
	private String attName;     //附件文件名称
	private  Integer count;//新建流程的数量显示
	//弹出窗口模式下  审批意见是否必填 1必填  0不必填
	private String suggestionrequired;
	/**
	 * 邮件提醒服务
	 */
	@Autowired
	private IMailService mailService;
	/**
	 * 手机短信提醒服务
	 */
	@Autowired
	private IsmsService smsService;
	/**
	 * 内部消息提醒服务
	 */
	@Autowired
	private IMessageService messageService;
	/**
	 * Rtx提醒服务
	 */
	@Autowired
	private IMMessageService imService;
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Autowired
	private IMManager iMManager;
	
	@Autowired
	IProcessInstanceService workflowDelegation;
	
	@Autowired IUserService userService ;										//统一用户服务类
	/**
	 * 得到调用模块的名称 - 需要记录各子模块的日志信息。
	 * 
	 * @author:邓志城
	 * @date:2010-5-6 下午05:40:58
	 * @return 模块名称
	 */
	protected String getModuleName() throws Exception {
		return "";
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public String validation() {
		return "validation";
	}

	/**
	 * 新建流程 显示有启动权限的流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-7 下午06:44:58
	 * @return
	 * @throws Exception
	 */
	public String createWorkflow() throws Exception {
		List workflowInfoList = adapterBaseWorkflowManager.getWorkflowService()
				.getStartWorkflow(workflowType);
		count =workflowInfoList.size();
		if (workflowInfoList != null && !workflowInfoList.isEmpty()) {
			workflowMap = new HashMap<String, List<Object[]>>(workflowInfoList
					.size());
			workflowTypeList = new ArrayList<Object[]>(workflowInfoList.size());
			List<String> workflowTpeIdList = new LinkedList<String>();
			for (int i = 0; i < workflowInfoList.size(); i++) {
				Object[] workflowInfo = (Object[]) workflowInfoList.get(i);
				if (!workflowTpeIdList.contains(workflowInfo[1].toString())) {
					workflowTpeIdList.add(workflowInfo[1].toString());
					workflowTypeList.add(new Object[] { workflowInfo[1],
							workflowInfo[2] });
				}
				if (!workflowMap.containsKey(workflowInfo[1].toString())) {
					List<Object[]> workflowList = new ArrayList<Object[]>(1);
					workflowList.add(new Object[] { workflowInfo[0],
							workflowInfo[3] });
					workflowMap.put(workflowInfo[1].toString(), workflowList);
				} else {
					workflowMap.get(workflowInfo[1].toString()).add(
							new Object[] { workflowInfo[0], workflowInfo[3] });
				}
			}
		}
		System.out.println(count);
		return "createworkflow";
	}
	
	/**
	 * 创建自由流程
	 * 
	 * @param
	 * @return
	 */
	public String createFwWorkflow() throws Exception
	{
		List workflowInfoList = adapterBaseWorkflowManager.getWorkflowService()
				.getStartWorkflow(workflowType);
		count =workflowInfoList.size();
		if (workflowInfoList != null && !workflowInfoList.isEmpty()) {
			workflowMap = new HashMap<String, List<Object[]>>(workflowInfoList
					.size());
			workflowTypeList = new ArrayList<Object[]>(workflowInfoList.size());
			List<String> workflowTpeIdList = new LinkedList<String>();
			for (int i = 0; i < workflowInfoList.size(); i++) {
				Object[] workflowInfo = (Object[]) workflowInfoList.get(i);
				if (!workflowTpeIdList.contains(workflowInfo[1].toString())) {
					workflowTpeIdList.add(workflowInfo[1].toString());
					workflowTypeList.add(new Object[] { workflowInfo[1],
							workflowInfo[2] });
				}
				if (!workflowMap.containsKey(workflowInfo[1].toString())) {
					List<Object[]> workflowList = new ArrayList<Object[]>(1);
					workflowList.add(new Object[] { workflowInfo[0],
							workflowInfo[3] });
					workflowMap.put(workflowInfo[1].toString(), workflowList);
				} else {
					workflowMap.get(workflowInfo[1].toString()).add(
							new Object[] { workflowInfo[0], workflowInfo[3] });
				}
			}
		}
		System.out.println(count);
		return "createFwWorkflow";
	}

	/**
	 * 显示流程草稿
	 * 
	 * @author:邓志城
	 * @date:2010-11-9 下午02:33:36
	 * @return
	 * @throws Exception
	 */
	public String workflowDraft() throws Exception {
		List workflowInfoList = getManager()
				.getStartWorkflowDraft(workflowType);
		doneWorkflowList(workflowInfoList);
		return "workflowdraft";
	}

	/**
	 * 处理流程信息
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 上午11:56:22
	 * @param workflowInfoList
	 */
	private Object[] doneWorkflowList(List workflowInfoList) {
		if (workflowInfoList != null && !workflowInfoList.isEmpty()) {
			workflowMap = new HashMap<String, List<Object[]>>(workflowInfoList
					.size());
			List<Object[]> tempWorkflowTypeList = new LinkedList<Object[]>();
			workflowTypeList = new ArrayList<Object[]>(workflowInfoList.size());
			List<String> workflowTpeIdList = new LinkedList<String>();
			for (int i = 0; i < workflowInfoList.size(); i++) {
				Object[] workflowInfo = (Object[]) workflowInfoList.get(i);
				if (!workflowTpeIdList.contains(workflowInfo[1].toString())) {
					workflowTpeIdList.add(workflowInfo[1].toString());
					tempWorkflowTypeList.add(new Object[] { workflowInfo[1],
							workflowInfo[2] });
				}
				if (!workflowMap.containsKey(workflowInfo[1].toString())) {
					List<Object[]> workflowList = new ArrayList<Object[]>(1);
					workflowList.add(new Object[] { workflowInfo[0],
							workflowInfo[3], workflowInfo[4] });
					workflowMap.put(workflowInfo[1].toString(), workflowList);
				} else {
					workflowMap.get(workflowInfo[1].toString()).add(
							new Object[] { workflowInfo[0], workflowInfo[3],
									workflowInfo[4] });
				}
			}
			for (int i = 0; i < tempWorkflowTypeList.size(); i++) {
				Object[] objType = tempWorkflowTypeList.get(i);
				List<Object[]> workflowList = workflowMap.get(objType[0]
						.toString());
				if (workflowList != null) {
					int total = 0;
					for (int j = 0; j < workflowList.size(); j++) {
						Object[] workflowInfo = (Object[]) workflowList.get(j);
						int count = (Integer) workflowInfo[2];
						total = total + count;
					}
					objType = ObjectUtils.addObjectToArray(objType, total);
					workflowTypeList.add(objType);
				}
			}
			return new Object[] { workflowMap, workflowTypeList };
		}
		return null;
	}

	/**
	 * 待办流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 上午11:58:20
	 * @return
	 * @throws Exception
	 */
	public String todoWorkflow() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(workflowType);
		parameter.setType(type);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setWorkflowName(workflowName);
		List workflowInfoList = getManager().getTodoWorkflow(parameter);
		doneWorkflowList(workflowInfoList);
		//公文传输  辅助参数
		String t = getRequest().getParameter("gwcs");
		getRequest().setAttribute("gwcs", t);
		
		return "todoworkflow";
	}
	//我的退回文件列表
	public String returnWorkflow() throws Exception{
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(workflowType);
		parameter.setType(type);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setWorkflowName(workflowName);
		List workflowInfoList = getManager().getMyReturnWorkflow(parameter);
		doneWorkflowList(workflowInfoList);
		return "returnworkflow";
	}
	/**
	 * 我的在办文件列表视图
	 * 
	 * @description
	 * @author xush
	 * @return
	 * @throws Exception
	 * @createTime 1/9/2014 9:44 AM
	 */
	public String listReturn() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setState(state);
		parameter.setIsSuspended("0");
		pageWorkflow = adapterBaseWorkflowManager.getWorkExtendManager()
				.getReturnWorks(pageWorkflow, parameter,
						new OALogInfo("得到待办任务列表"));
		return "listReturn";
	}
	/**
	 * @method getRepealWorkflow
	 * @author 申仪玲
	 * @created 2011-12-12 下午08:56:45
	 * @description 公文回收站流程（挂起流程）
	 * @return String 返回类型
	 */
	public String repealWorkflow() throws Exception {
		List workflowInfoList = getManager().getRepealWorkflow(workflowType,
				state);
		doneWorkflowList(workflowInfoList);
		return "repealworkflow";
	}

	/**
	 * 指派|委派的流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-21 下午06:14:06
	 * @return
	 */
	public String entrustWorkflow() {
		try {
			EntrustWorkflowParameter parameter = new EntrustWorkflowParameter();
			parameter.setWorkflowType(workflowType);
			parameter.setExcludeWorkflowType(excludeWorkflowType);
			parameter.setAssignType(assignType);
			List workflowInfoList = getManager().getEntrustWorkflow(parameter);
			doneWorkflowList(workflowInfoList);
		} catch (Exception e) {
			logger.error("得到委派流程列表发生异常", e);
		}
		return "entrustworkflow";
	}

	/**
	 * 已办流程 通过参数state控制已办结流程和未办结流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-18 上午09:09:49
	 * @return
	 * @throws Exception
	 */
	public String processedWorkflow() throws Exception {
		//List workflowInfoList = getManager().getProcessedWorkflow(workflowType, state, filterSign);
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setProcessStatus(state);
		parameter.setFilterSign(filterSign);
		parameter.setExcludeWorkflowTypeName(excludeWorkflowTypeName);
		List workflowInfoList = getManager().getProcessedWorkflow(parameter);
		doneWorkflowList(workflowInfoList);
		return "processedworkflow";
	}
	/**
	 * 我的在办流程 通过参数state控制已办结流程和未办结流程
	 * 
	 * @author:xush
	 * @date:2010-11-18 上午09:09:49
	 * @return
	 * @throws Exception
	 */
	public String myTodoWorkflow() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setProcessStatus(state);
		parameter.setFilterSign(filterSign);
		parameter.setExcludeWorkflowTypeName(excludeWorkflowTypeName);
		//List workflowInfoList = getManager().getProcessedWorkflow(workflowType, state, filterSign);
		List workflowInfoList = getManager()
		.getMyNowFileWorkflow(parameter);
        doneWorkflowList(workflowInfoList);
		return "mytodoworkflow";
	}

	/**
	 * 主办流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-16 下午03:45:14
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String hostedWorkflow() throws Exception {
		Object[] workflowInfoList = getManager()
				.getHostedWorkflow(workflowType);
		List leftList = (List) workflowInfoList[0];
		List rightList = (List) workflowInfoList[1];
		Object[] retObject = doneWorkflowList(leftList);
		if (retObject != null) {
			leftWorkflowMap = (Map) retObject[0];
			leftWorkflowTypeList = (List) retObject[1];
		}
		Object[] rightObject = doneWorkflowList(rightList);
		if (rightObject != null) {
			rightWorkflowMap = (Map) rightObject[0];
			rightWorkflowTypeList = (List) rightObject[1];
		}
		return "hostedworkflow";
	}

	/**
	 * 提交流程
	 * 
	 * @author:邓志城
	 * @date:2009-12-18 上午10:46:21
	 * @return
	 */
	@Deprecated
	public String wizard() throws Exception {
		// 获取表单对应的工作流
		if (formId == null || "".equals(formId)) {
			logger.error("表单模板id不存在！");
			throw new SystemException("表单模板id不存在！");
		}
		workflows = getManager().getWorkflowsByFormId(formId);
		// 获取审批意见列表
		ideaLst = getManager().getCurrentUserDictItem(getDictType());
		// 得到发文字典类主键
		ToaSysmanageDict dict = getManager().getDict(getDictType());
		if (dict != null) {
			dictId = dict.getDictCode();
		}
		return "towizard";
	}

	/**
	 * 转交下一步处理
	 * 
	 * @return 郑志斌修改 2010-11-18 获取当前用户的审批意见列表
	 * @modify yanjian 2011-09-28 过滤换行
	 */
	public String nextstep() throws Exception {
		try {
			User user = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser();
			ideaLst = adapterBaseWorkflowManager.getApprovalSuggestionService()
					.getAppSuggestionListByUserId(user.getUserId(),
							new OALogInfo("获取当前用户审批意见列表"));
			userIds = user.getUserId();
			if (tableName != null && !"".equals(tableName)) {
				pkFieldName = getManager().getPrimaryKeyName(tableName);// 得到主键名称
				if (taskId != null && !taskId.equals("")) {// 待签收文件 获取业务ID
					String formIdAndBussinessId = adapterBaseWorkflowManager
							.getWorkflowService().getNodeInfo(taskId);
					bussinessId = formIdAndBussinessId.split(",")[2];
				} else {
					bussinessId = tableName + ";" + pkFieldName + ";"
							+ pkFieldValue;
				}
				BaseManager baseManager = getManager();
				if (baseManager instanceof SendDocManager) {
					SendDocManager docManager = (SendDocManager) baseManager;
					if (null != pkFieldValue && !"".equals(pkFieldValue)) {
						Map map = docManager.getSystemField(pkFieldValue,
								tableName);
						businessName = (String) map
								.get(BaseWorkflowManager.WORKFLOW_TITLE);// 得到标题
						workflowName = (String) map
								.get(BaseWorkflowManager.WORKFLOW_NAME);// 得到流程名
					}
					TwfBaseNodesetting nodeSetting = docManager
							.getOperationHtml(taskId, workflowName);
					if (nodeSetting != null) {// 流程启动表单取第一个节点上的表单
						formId = nodeSetting.getNsNodeFormId().toString();
					}
				}
			}
			if (businessName != null) {
				businessName = businessName.replace("\\r\\n", " ");
			}
			if (businessName != null) {
				businessName = businessName.replace("\\n", " ");
				businessName = URLDecoder.decode(businessName, "utf-8");
				businessName = URLDecoder.decode(businessName, "utf-8");//解决在weblogic中businessName字段乱码问题
			}
			
			userName = user.getUserName();
		} catch (Exception e) {
			logger.error("转到下一步时发生异常", e);
		}
		return "tonextstep";
	}

	/**
	 * 代办任务列表
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 上午09:39:41
	 * @return
	 * @throws Exception
	 */
	public String todo() throws Exception {
		pageWorkflow = getManager().getTodoWorks(pageWorkflow,
				getWorkflowType(), businessName, userName, startDate, endDate,
				isBackSpace, new OALogInfo("得到待办任务列表"));
		return "todo";
	}

	/**
	 * 待办列表视图列表
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Apr 17, 2012 3:30:17 PM
	 */
	public String listtodo() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setIsBackSpace(isBackSpace);
		try{
			pageWorkflow = adapterBaseWorkflowManager.getWorkExtendManager()
					.getTodoWorks(pageWorkflow, parameter,
							new OALogInfo("得到待办任务列表"));
			}catch (Exception e) {
				/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
				if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
					return "sessionError";
				}

			}
		return "listtodo";
	}

	/**
	 * 在办、办结列表视图列表
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Apr 17, 2012 3:30:33 PM
	 */
	public String listprocessed() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setExcludeWorkflowTypeName(excludeWorkflowTypeName);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setFilterSign(filterSign);
		parameter.setEndDate(endDate);
		parameter.setState(state);
		parameter.setWorkflowEndDatestartDate(workflowEndDatestartDate);//设置办结时间的开始时间
		parameter.setWorkflowEndDateendDate(workflowEndDateendDate);//设置办结时间的结束时间
		parameter.setIsSuspended("0");
		try{
			pageWorkflow = adapterBaseWorkflowManager.getWorkExtendManager()
				.getProcessedWorks(pageWorkflow, parameter,
						new OALogInfo("得到待办任务列表"));
			}catch (Exception e) {
				/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
				if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
					return "sessionError";
				}

			}
		return "listprocessed";
	}

	/**
	 * 我的请求列表视图
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime May 23, 2012 12:56:08 PM
	 */
	public String listHosted() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setState(state);
		parameter.setIsSuspended("0");
		try{
		pageWorkflow = adapterBaseWorkflowManager.getWorkExtendManager()
				.getHostedWorks(pageWorkflow, parameter,
						new OALogInfo("得到待办任务列表"));
		}catch (Exception e) {
			/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
			if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
				return "sessionError";
			}

		}
		return "listHosted";
	}
	/**
	 * 我的在办文件列表视图
	 * 
	 * @description
	 * @author xush
	 * @return
	 * @throws Exception
	 * @createTime 1/9/2014 9:44 AM
	 */
	public String listMytodo() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setState(state);
		parameter.setIsSuspended("0");
		pageWorkflow = adapterBaseWorkflowManager.getWorkExtendManager()
				.getMytodoWorks(pageWorkflow, parameter,
						new OALogInfo("得到待办任务列表"));
		return "listMytodo";
	}
	/**
	 * 我的在办文件列表视图
	 * 
	 * @description
	 * @author xush
	 * @return
	 * @throws Exception
	 * @createTime 1/9/2014 9:44 AM
	 */
	public String listRepeal() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setState(state);
		parameter.setIsSuspended("0");
		pageWorkflow = adapterBaseWorkflowManager.getWorkExtendManager()
				.getRepealWorks(pageWorkflow, parameter,
						new OALogInfo("得到待办任务列表"));
		return "listRepeal";
	}
	/**
	 * 指定用户的代办任务列表
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 上午09:39:41
	 * @return
	 * @throws Exception
	 */
	public String todoOfAppointUser() throws Exception {
		pageWorkflow = getManager().getTodoWorksOfAppointUser(pageWorkflow,
				getWorkflowType(), businessName, userName, startDate, endDate,
				isBackSpace, state, processTimeout, appointUserId,
				new OALogInfo("得到待办任务列表"));
		return "todoOfAppointUser";
	}

	/**
	 * 指定部门的代办任务列表
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 上午09:39:41
	 * @return
	 * @throws Exception
	 */
	public String todoOfAppointDept() throws Exception {
		pageWorkflow = getManager().getTodoWorksOfAppointDept(pageWorkflow,
				getWorkflowType(), businessName, userName, startDate, endDate,
				isBackSpace, state, processTimeout, appointDeptId,
				new OALogInfo("得到待办任务列表"));
		return "todoOfAppointDept";
	}

	/**
	 * 已办任务列表
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 上午09:46:23
	 * @return
	 * @throws Exception
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getProcessedWorks方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
	 */
	public String processed() throws Exception {
		pageWorkflow = getManager().getProcessedWorks(pageWorkflow,
				getWorkflowType(), businessName, userName, startDate, endDate,
				state, processTimeout, noTotal, appointUserId,
				new OALogInfo("得到已办任务列表"));
		return "processed";
	}

	/**
	 * 主办任务列表
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 上午10:33:06
	 * @return
	 * @throws Exception
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getHostedByDocs方法中添加 State 状态 查询参数 为已审公文和已办来文 添加状态栏
	 */
	public String hostedby() throws Exception {
		pageWorkflow = getManager().getHostedByDocs(pageWorkflow,
				getWorkflowType(), businessName, startDate, endDate, state,
				new OALogInfo("得到主办任务列表"));
		return "hostedby";
	}

	/**
	 * author:luosy description: 我指派的任务列表 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String assignlist() throws Exception {
		pageWorkflow = getManager().getAssignWorks(pageWorkflow,
				getWorkflowType(), businessName, userName, startDate, endDate);
		return "assign";
	}

	/**
	 * author:luosy description:我委派的任务列表 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String entrustlist() throws Exception {
		pageWorkflow = getManager().getEntrustWorks(pageWorkflow,
				getWorkflowType(), businessName, userName, startDate, endDate);
		return "entrust";
	}

	/**
	 * 根据给定任务实例ID获取所在流程实例的所有处理意见
	 * 
	 * @author:邓志城
	 * @date:2009-12-12 上午10:13:00
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String annallist() throws Exception {
		String processInstanceId = "";
		if (taskId != null && !taskId.equals("") && !taskId.equals("null")) {
			processInstanceId = adapterBaseWorkflowManager.getWorkflowService()
					.getProcessInstanceIdByTiId(taskId);
		}else{
			taskId = "";
		}
		if (instanceId != null && !instanceId.equals("")) {
			processInstanceId = instanceId;
		}
		List<List> listAllAnnal = adapterBaseWorkflowManager
				.getSendDocAnnalManager().allAnnal(processInstanceId, taskId);
		ToaSystemset systemset = adapterBaseWorkflowManager
				.getSystemsetManager().getSystemset(); // 获取系统全局配置信息
		if (systemset.getIsUseCASign() == null
				|| systemset.getIsUseCASign().equals("")) {
			systemset.setIsUseCASign("0");
		}
		adapterBaseWorkflowManager.getSendDocAnnalManager().genAnnalHtmlOne(
				listAllAnnal, systemset.getIsUseCASign(), processInstanceId,
				getRequest());
		return "annal";
	}

	/**
	 * 在（待办|在办）指派任务 如果taskId为空则返回-1； 若抛出异常则返回-2； 正常情况下返回回退标示。
	 * 
	 * @author:邓志城
	 * @date:2009-5-18 上午09:19:39
	 * @return
	 * @throws Exception
	 */
	public String checkCanReturn() throws Exception {
		String ret = "";
		try {
			if (taskId == null || "".equals(taskId)) {
				ret = "-1";
			} else {
				ret = getManager().checkCanReturn(taskId);
			}
		} catch (Exception e) {
			ret = "-2";
			logger.error("验证任务是否允许指派的过程中出现异常！异常信息：" + e);
		}
		return this.renderText(ret);
	}

	/**
	 * 覆盖父类的方法 如果存在formAction,调用表单的Action,否则调用弗雷方法。
	 */
	public String execute() throws Exception {
		if (formAction != null && !"".equals(formAction)) {
			return BaseAction.execute(this, formAction, type);
		}
		return super.execute();
	}

	/**
	 * 同步工作流中的businessName
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 2:52:35 PM
	 */
	protected void synchronizedPiBusName() {
		// 同步工作流中的businessName yanjian 2012-01-05 15:57
		if (instanceId != null && !"".equals(instanceId)) {
			ProcessInstance processInstance = adapterBaseWorkflowManager
					.getWorkflowService().getProcessInstanceById(instanceId);
			processInstance.setBusinessName(businessName);
			//父流程业务名称
			processInstance.getContextInstance().setVariable(WorkflowConst.WORKFLOW_BUSINESSNAME, businessName);
		}
	}

	/**
	 * 保存 定时提醒信息
	 * 
	 * @author 严建
	 * @param thisTaskId
	 *                流程实例id
	 * @createTime Mar 21, 2012 2:48:19 PM
	 */
	protected void saveRemindInfo(String thisTaskId) {
		try {
			// 定时提醒是否有内容
			if (!"undefined".equals(remindSet) && !"".equals(remindSet)
					&& !"".equals(remindType)) {
				String arrType[] = remindType.split(",");
				for (int j = 0; j < arrType.length; j++) {
					ToaCalendar calmodel = new ToaCalendar();
					// 添加默认日程时间
					java.util.Date dt = new java.util.Date();
					calmodel.setCalStartTime(dt);// 开始时间
					calmodel.setCalEndTime(dt);// 结束时间
					calmodel.setCalTitle("文件办理定时提醒：" + businessName);
					calmodel.setCalCon("设置关于文件的定时提醒:" + businessName
							+ "<br>\n\r<br>\n\r<br>\n\r<b>删除该日程将取消相关的定时提醒</b>");
					calmodel.setUserId("workflowRemind");
					calmodel.setCalUserName("文件办理定时提醒");
					String calId = adapterBaseWorkflowManager.getCalManager()
							.saveCalendar("", calmodel, "view");
					calmodel.setCalHasRemind(ToaCalendar.HAS_REMIND);
					ToaCalendar cal = adapterBaseWorkflowManager
							.getCalManager().getCalById(calId);
					Set s = cal.getToaCalendarReminds();
					calmodel.setToaCalendarReminds(s);
					cal = null;

					ToaCalendar calModel = adapterBaseWorkflowManager
							.getCalManager().getCalById(calId);
					// 保存 日程提醒
					DateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String arr[] = remindSet.split("##");
					for (int i = 0; i < arr.length; i++) {
						ToaCalendarRemind calRemind = new ToaCalendarRemind();
						String remind[] = arr[i].split(";");

						remind[0] = remind[0].replace("id", "");
						if (remind[0].length() == 32) {
							calRemind.setRemindId(remind[0]);
						}
						try {
							if (remind[1] == null || "".equals(remind[1])
									|| "null".equals(remind[1])) {
								calRemind.setRemindTime(new Date());
							} else {
								calRemind
										.setRemindTime(format.parse(remind[1]));
							}
						} catch (ParseException e) {
							e.printStackTrace();
							throw new ServiceException(
									MessagesConst.save_error,
									new Object[] { "日程提醒" });
						}
						// 提醒方式 MSG：0/MAIL：1
						calRemind.setRemindMethod(arrType[j].toLowerCase());
						calRemind.setRemindCon(remind[5]);
						calRemind.setToaCalendar(calModel);
						calRemind.setRemindStatus("1");
						if (taskId == null || "".equals(taskId)) {
							calRemind.setRemindTaskId(thisTaskId);// 流程实例ID
						} else {
							calRemind.setRemindTaskId(instanceId);// 流程实例ID
						}
						// if("null".equals(remind[4])||null==remind[4]){//只选择了指定提醒的人员
						// 不能选择
						// }else if("0".equals(remind[4])){//只提醒自己
						// }else if("1".equals(remind[4])){//提醒自己和相关处理人
						// }else if("2".equals(remind[4])){//只提醒相关处理人
						// }
						calRemind.setRemindShare(remind[4]);// 是否提醒共享人
						adapterBaseWorkflowManager.getCalRemindManager()
								.saveRemind(calRemind);
						if (!"".equals(remind[3]) && null != remind[3]) {
							adapterBaseWorkflowManager.getCalShareManager()
									.saveShare(remind[3], calModel);// 保存共享对象
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时提醒异常", e);
		}
	}

	protected void doCASignInfo() {
		JSONObject json = new JSONObject();
		json.put("suggestion", suggestion);
		json.put("CAInfo", CASignInfo == null ? "" : CASignInfo);
		//考虑快捷办理，代办意见的情况，增加委托人的ID 申仪玲  20120409
		json.put("rUserId", rUserId == null ? "" : rUserId);
		CASignInfo = json.toString();
		logger.info("CASignInfo ----- " + CASignInfo);
	}

	/**
	 * 处理意见信息
	 * 
	 * @author 严建
	 * @throws Exception
	 * @createTime Mar 21, 2012 2:40:01 PM
	 */
	protected void doFilterSuggestion() throws Exception {
		if (suggestion != null) {
			/* modify yanjian 2011-11-06 bug-2635 */
			/* modify xielei 2014-02-20 转义百分号*/
			suggestion = java.net.URLDecoder.decode(suggestion.replaceAll("%", "%25"), "UTF-8");
			String[] FF_String = new String[] { "\'", "\"", "<", ">", "\\\\" };// 特殊字符
			String[] NFF_String = new String[] { "’", "”", "＜", "＞", "＼" };// 替换字符
			for (int i = 0; i < FF_String.length; i++) {
				if (suggestion.indexOf(FF_String[i]) != -1) {
					suggestion = suggestion.replaceAll(FF_String[i],
							NFF_String[i]);
				}
			}
		} else {
			suggestion = "";
		}
	}

	/**
	 * 将message的信息保存在session中
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 2:31:17 PM
	 */
	@SuppressWarnings("unchecked")
	protected void saveMessageSession() {
		ActionContext cxt = ActionContext.getContext();
		cxt.getSession().put("remindType", remindType);
		cxt.getSession().put("handlerMes", handlerMes);
		cxt.getSession().put("moduleType", GlobalBaseData.MSG_GZCL);// 调用消息模块的类型
		cxt.getSession().put("transDept", returnFlag);// 存储协办处室信息,用于子流程是否显示协办处室
	}

	protected void saveStrTaskActorsSession() {
		ActionContext cxt = ActionContext.getContext();
		cxt.put("strTaskActors", strTaskActors);
	}

	/**
	 * 将迁移线信息保存在session中
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 2:35:24 PM
	 */
	protected void saveTransitionInfoSession() {
		ActionContext cxt = ActionContext.getContext();
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
			cxt.put("transitionId", stackTransitionId);
		}
	}

	/**
	 * 将签收信息保存在session中
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 2:34:00 PM
	 */
	@SuppressWarnings("unchecked")
	protected void saveSignInfoSession() {
		ActionContext cxt = ActionContext.getContext();
		cxt.getSession().put("recOrg", transitionName);// 存储签收部门信息
		cxt.getSession().put("recUser", strTaskActors);// 存储签收人员信息
	}

	/**
	 * 清空SESSION中message的数据
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 2:29:35 PM
	 */
	@SuppressWarnings("unchecked")
	protected void clearMessageSession() {
		ActionContext cxt = ActionContext.getContext();
		// 执行完成之后清空SESSION中数据,added by dengzc 2010年12月16日15:13:00
		Map<String, Object> session = cxt.getSession();
		String remindType = (String)session.get("remindType");
		if (remindType != null) {
			session.remove("remindType");
			logger.info("清空SESSION中提醒方式");
		}
		String handlerMes = (String)session.get("handlerMes");
		if (handlerMes != null) {
			session.remove("handlerMes");
			logger.info("清空SESSION中提醒内容");
		}
		String moduleType = (String)session.get("moduleType");
		if (moduleType != null) {
			session.remove("moduleType");
			logger.info("清空SESSION中提醒模块");
		}
	}

	/**
	 * 处理文件跟踪
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 2:27:30 PM
	 */
	protected void doTraceDoc() {
		try {
			// 跟踪文件
			isGenzong = this.getRequest().getParameter("isGenzong");
			if (this.isGenzong.equals("1")) {
				adapterBaseWorkflowManager.getTraceDocManager().saveTraceDoc(
						instanceId,
						workflowName,
						businessName,
						adapterBaseWorkflowManager.getUserService()
								.getCurrentUser().getUserId());

			} else
				adapterBaseWorkflowManager.getTraceDocManager()
						.deleteToaTraceDocs(
								instanceId,
								adapterBaseWorkflowManager.getUserService()
										.getCurrentUser().getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("跟踪文件异常", e);
		}
	}

	/**
	 * 生成日志信息
	 * 
	 * @author 严建
	 * @createTime Mar 21, 2012 3:04:04 PM
	 */
	protected void genOALogInfo() {
		logInfo = new OALogInfo(
				getText(GlobalBaseData.WORKFLOW_HANDLERWORKFLOWNEXT,
						new String[] {
								adapterBaseWorkflowManager.getUserService()
										.getCurrentUser().getUserName(),
								businessName }));
	}

	/**
	 * 启动流程并使流程停留在第一个任务节点,通常为拟稿节点
	 * 
	 * @author 严建
	 * @return
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 * @createTime Apr 13, 2012 10:40:07 AM
	 */
	@SuppressWarnings("unchecked")
	public String startProcessToFistNode() throws SystemException,
			DAOException, ServiceException {
		try {
			if (formId != null && !formId.equals("")) {
				formId = formId.toUpperCase();
			}
			String thisTaskId = "";
			// 将提醒(方式|内容)存储在session中
			saveMessageSession();
			// 将签收信息存储在session中
			saveSignInfoSession();
			// 将迁移线信息保存在session中
			saveTransitionInfoSession();
			// 处理意见信息
			doFilterSuggestion();
			// 处理ca信息
			doCASignInfo();
			// 生成日志信息
			genOALogInfo();
			// 适配选择人员
			strTaskActors = adapterBaseWorkflowManager.getActorAdpter()
					.getActors(strTaskActors);
			String[] strTaskActorArray = null;
			// 将用户信息保存在session中
			saveStrTaskActorsSession();
			if (!"".equals(strTaskActors)) {
				strTaskActorArray = strTaskActors.split(",");
			}
			List transList = adapterBaseWorkflowManager.getWorkflowService()
					.getStartNodeTransitions(workflowName);
			Object[] firsttran = (Object[]) transList.get(0);
			transitionName = (String) firsttran[0];
			Set<String> set = (Set<String>) firsttran[3];
			String nodeId = set.iterator().next().split("\\|")[1];// 得到节点id
			String userId = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser().getUserId();
			if (strTaskActorArray == null || strTaskActorArray.length == 0) {
				if (strTaskActorArray == null) {
					strTaskActorArray = new String[1];
				}
				strTaskActorArray[0] = userId + "|" + nodeId;
			}
			OtherParameter otherparameter = initOtherParameter();
			VoFormDataBean bean = getManager().startProcessToFistNodeWorkflow(
					formId, workflowName, bussinessId, userId, businessName,
					strTaskActorArray, transitionName, CASignInfo, formData,
					otherparameter, logInfo);

			if (bean != null) {
				bean.deleteFile();
			}
			instanceId = bean.getInstanceId();
			//重新设置流程期限时间
			if (otherparameter != null
					&& otherparameter.getProcessTimer() != null) {
				getManager().reSetProcessTimer(instanceId,
						otherparameter.getProcessTimer());
				adapterBaseWorkflowManager.getWorkflowService()
						.setJobExecutorTime(
								otherparameter.getProcessTimer().getTime());
				/*
				 * 
				 * 设置流程实例变量“processTimer_Enable”，便于WorkFlowReminAndReSetProcessTimerdAction中进行数据验证
				 * 
				 * */
				adapterBaseWorkflowManager.getWorkflowService().getContextInstance(instanceId)
										.setVariable("processTimer_Enable", "true");
			}else{//没有设置限时时间时，默认设置失效期是100年
				long dueDateTime = System.currentTimeMillis()+ 100* 365 * 24 * 60;
				Date dueDate = new Date();
				dueDate.setTime(dueDateTime);
				getManager().reSetProcessTimer(instanceId,dueDate);
				adapterBaseWorkflowManager.getWorkflowService().setJobExecutorTime(dueDateTime);
				/*
				 * 
				 * 设置流程实例变量“processTimer_Enable”，便于WorkFlowReminAndReSetProcessTimerdAction中进行数据验证
				 * 
				 * */
				adapterBaseWorkflowManager.getWorkflowService().getContextInstance(instanceId)
										.setVariable("processTimer_Enable", "false");
			}
			thisTaskId = instanceId;
			// 处理文件跟踪
			doTraceDoc();
			// 执行完成之后清空SESSION中数据,added by dengzc 2010年12月16日15:13:00
			clearMessageSession();
			// 保存 定时提醒信息
			saveRemindInfo(thisTaskId);
			// ----------END--------------------
			if (formData == null) {// 不是在表单中提交流程,可能是在草稿中提交流程
				return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");// 提交至流程后关闭窗口
			}
			return null;
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
	 * 启动流程
	 * 
	 * @description
	 * @author 严建
	 * @param thisTaskId
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 * @createTime Apr 17, 2012 11:42:42 AM
	 */
	private void startWorkflow(StringBuilder thisTaskId)
			throws SystemException, DAOException, ServiceException {
		try {
			if (taskId == null || "".equals(taskId)) {// 送审流程
				OtherParameter otherparameter = initOtherParameter();
				String[] strTaskActorArray = null;
				if ("".equals(strTaskActors)) {
				} else {
					// 加入对串行会签排序支持,按排序号倒序排列.added by dengzc 2011年2月16日11:34:07
					strTaskActorArray = strTaskActors.split(",");
					/**
					 * 将新增的一些待办放入推送消息表
					 * -------begin----------
					 */
					String uid=null;
					String todoType=null;
					if(PushNotifyManager.WORKFLOW_TYPE_DOC.indexOf(workflowType)!=-1){
						todoType=PushNotifyManager.PUSH_MODULE_NO_DOCTODO;
					}else{
						todoType=PushNotifyManager.PUSH_MODULE_NO_TODO;
					}
					if(pushManager.getPushState(uid,todoType)){
						for(String str:strTaskActorArray){
							/**
							 * 新增操作时往推送设置里面新增记录或者修改推送数
							 */
							uid=str.split("\\|")[0];
							pushManager.saveNotity(uid,todoType, 1);
						}
					}
					/**
					 * -----------end----------------
					 */
					doFilterTaskActors(strTaskActorArray);
					// ------------End--------------------
				}
				//开始，节点1，节点2（获取节点2的节点Id）
				if(transitionName !=null ){
					if(transitionName.indexOf(",") != -1){
						//并行流程
					}else{
						long nodeId = adapterBaseWorkflowManager.getNodeService().getSignNodeByWorkflowNameAndTransitionName(workflowName, transitionName);
						String[] taskActors = adapterBaseWorkflowManager.getNodeService().getSignNodeActorsByNodeId(nodeId+"");
						if(taskActors != null){	//是签收节点，且签收节点设置了可选择的人员
							strTaskActorArray = taskActors;
						}
					}
				}
				VoFormDataBean bean = getManager().handleWorkflow(formId,
						workflowName, bussinessId, businessName,
						strTaskActorArray, transitionName, concurrentTrans,
						CASignInfo, formData, otherparameter, logInfo);
				if (bean != null) {
					bean.deleteFile();
				}
				instanceId = bean.getInstanceId();
				//重新设置流程期限时间
				if (otherparameter != null
						&& otherparameter.getProcessTimer() != null) {
					getManager().reSetProcessTimer(instanceId,
							otherparameter.getProcessTimer());
					adapterBaseWorkflowManager.getWorkflowService()
							.setJobExecutorTime(
									otherparameter.getProcessTimer().getTime());
				}
				thisTaskId.append(instanceId);
			} else {
				throw new ServiceException("当前上下文变量taskId不为null且不为空字符串，不能使用该接口");
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
	
	/**
	 * 提交下一步
	 * 
	 * @description
	 * @author 严建
	 * @param thisTaskId
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 * @createTime Apr 17, 2012 11:45:33 AM
	 */
	private void gotoNextWorkflow(StringBuilder thisTaskId)
			throws SystemException, DAOException, ServiceException {
		try {
			if (taskId != null && !"".equals(taskId)) {//  提交下一步
				OtherParameter otherparameter = initOtherParameter();
				String[] info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
				bussinessId = info[0];
				String curUserId = adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId();
				if (rUserId != null && !"".equals(curUserId)) {
					curUserId = rUserId;
				}
				String isNewForm = "1";
				if ("0".equals(bussinessId)) {
					isNewForm = "1";
				} else {
					isNewForm = "0";
				}
				// 同步工作流中的businessName yanjian 2012-01-05 15:57
				synchronizedPiBusName();
				String[] strTaskActorArray = null;
				if ("".equals(strTaskActors)) {
				} else {
					// 加入对串行会签排序支持,按排序号倒序排列.added by dengzc 2011年2月16日11:34:07
					strTaskActorArray = strTaskActors.split(",");
					doFilterTaskActors(strTaskActorArray);
					// ----------- End --------------
				}
				if(transitionName!=null && transitionName.indexOf(",")==-1){
					long nodeId = adapterBaseWorkflowManager.getNodeService().getSignNodeByTaskIdAndTransitionName(taskId, transitionName);
					String[] taskActors = adapterBaseWorkflowManager.getNodeService().getSignNodeActorsByNodeId(nodeId+"");
					if(taskActors != null){	//是签收节点，且签收节点设置了可选择的人员
						strTaskActorArray = taskActors;
					}
				}
				if("1".equals(daiBan)){
					otherparameter.setDaiBan(true);
				}
				otherparameter.setSuggestion(suggestion);
				//去除strTaskActorArray中重复数据
					if(strTaskActorArray != null && !"".equals(strTaskActorArray)){
						Set<String> taskActors2Set = new HashSet<String>();
						CollectionUtils.addAll(taskActors2Set, strTaskActorArray);
						strTaskActorArray = (String[])taskActors2Set.toArray(new String[taskActors2Set.size()]);
					}
				VoFormDataBean bean = getManager().handleWorkflowNextStep(
						taskId, transitionName, "", isNewForm, formId,
						bussinessId, CASignInfo, curUserId, strTaskActorArray,
						formData, otherparameter, logInfo);
				if (bean != null) {
					bean.deleteFile();
				}
				//重新设置流程期限时间
				if (otherparameter != null
						&& otherparameter.getProcessTimer() != null) {
					getManager().reSetProcessTimer(instanceId,
							otherparameter.getProcessTimer());
					adapterBaseWorkflowManager.getWorkflowService()
							.setJobExecutorTime(
									otherparameter.getProcessTimer().getTime());
				}
				thisTaskId.append(instanceId);
			} else {
				throw new ServiceException("当前上下文变量taskId为null或为空字符串，不能使用该接口");
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

	/**
	 * 初始化参数
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 17, 2012 3:18:51 PM
	 */
	protected OtherParameter initOtherParameter() {
		OtherParameter otherparameter = new OtherParameter();
		if(processOutTime != null && !"undefined".equals(processOutTime) && !"".equals(processOutTime)){
			otherparameter.setProcessTimer(DateUtil.parse(processOutTime, "yyyy-MM-dd HH:mm:ss"));//设置流程期限值
		}
		return otherparameter;
	}

	/**
	 * 加入对串行会签排序支持,按排序号倒序排列
	 * 
	 * @description
	 * @author 严建
	 * @param strTaskActorArray
	 * @createTime Apr 17, 2012 11:57:06 AM
	 */
	protected void doFilterTaskActors(String[] strTaskActorArray) {
		if (strTaskActorArray.length > 0) {
			String nodeId = strTaskActorArray[0].split("\\|")[1];// 得到节点id
			TwfBaseNodesetting nodeSetting = adapterBaseWorkflowManager
					.getWorkflowService().getNodesettingByNodeId(nodeId);
			String isCountersign = nodeSetting.getNsIsCountersign();// “1”：是会签节点
			String countersignType = nodeSetting.getCounterType();// 会签类型，“0”：串行；“1”：并行
			if ("1".equals(isCountersign) && "0".equals(countersignType)) {// 串行会签
				logger.info("串行会签,按排序号倒序排列...");
				final Map<String, User> userMap = new HashMap<String, User>();
				List<User> userLst = adapterBaseWorkflowManager
						.getUserService().getAllUserInfoByHa();
				for (User user : userLst) {
					userMap.put(user.getUserId(), user);
				}
				List<String> sortList = new LinkedList<String>();
				for (String str : strTaskActorArray) {
					sortList.add(str);
				}
				Collections.sort(sortList, new Comparator<String>() {
					public int compare(String o1, String o2) {
						User user1 = userMap.get(o1.split("\\|")[0]);
						User user2 = userMap.get(o2.split("\\|")[0]);
						Long key1;
						if (user1 != null && user1.getUserSequence() != null) {
							key1 = Long.valueOf(user1.getUserSequence());
						} else {
							key1 = Long.MAX_VALUE;
						}
						Long key2;
						if (user2 != null && user2.getUserSequence() != null) {
							key2 = Long.valueOf(user2.getUserSequence());
						} else {
							key2 = Long.MAX_VALUE;
						}
						return key2.compareTo(key1);
					}
				});
				userMap.clear();
				userLst.clear();
				strTaskActorArray = sortList
						.toArray(new String[sortList.size()]);
			}
		}
		List<String> sortList = new ArrayList<String>();
		// 如果是相同人员,则只分配一个任务
		for (String str : strTaskActorArray) {
			if (!sortList.contains(str)) {
				sortList.add(str);
			}
		}
		strTaskActorArray = sortList.toArray(new String[sortList.size()]);
	}

	/**
	 * 提交流程 提交成功返回“OK”，提交失败返回“NO”
	 * 
	 * @author:邓志城
	 * @date:2009-12-12 上午11:10:11
	 * @return
	 * @throws Exception
	 * 
	 * 2010-11-18 郑志斌修改 保存审批 意见接口
	 */
	@SuppressWarnings("unchecked")
	public String handleNextStep() throws SystemException, DAOException,
			ServiceException {
		try {
			if (formId != null && !formId.equals("")) {
				formId = formId.toUpperCase();
			}
			if(userName!=null&&!"".equals(userName)){
				userName = URLDecoder.decode(userName, "utf-8");//解决在weblogic中userName字段乱码问题
			}
			if(orgName!=null&&!"".equals(orgName)){
				orgName = URLDecoder.decode(orgName, "utf-8");//解决在weblogic中orgName字段乱码问题
			}
			// 将提醒(方式|内容)存储在session中
			saveMessageSession();
			// 将签收信息存储在session中
			saveSignInfoSession();
			// 将迁移线信息保存在session中
			saveTransitionInfoSession();
			// 处理意见信息
			doFilterSuggestion();
			// 处理ca信息
			doCASignInfo();
			// 生成日志信息
			genOALogInfo();
			// 适配选择人员
			strTaskActors = adapterBaseWorkflowManager.getActorAdpter().getActors(strTaskActors);
			// 将用户信息保存在session中
			saveStrTaskActorsSession();
			StringBuilder thisTaskId = new StringBuilder();
			if (taskId == null || "".equals(taskId)) {// 送审流程
				startWorkflow(thisTaskId);
			} else {// 提交流程
				boolean isContinue = false;
				while(!isContinue){
					try {
						gotoNextWorkflow(thisTaskId);
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
			}
			// 处理文件跟踪
			doTraceDoc();
			// 执行完成之后清空SESSION中数据,added by dengzc 2010年12月16日15:13:00
			clearMessageSession();
			// 保存 定时提醒信息
			saveRemindInfo(thisTaskId.toString());
			// ----------END--------------------
			if (formData == null) {// 不是在表单中提交流程,可能是在草稿中提交流程
				return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");// 提交至流程后关闭窗口
			}else{
				return renderText(thisTaskId.toString());// 提交至流程后关闭窗口
			}
//			return null;
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
	 *抚州高新区定制签发功能
	 * from 林业厅
	 * @author:申仪玲
	 * @date:2012-09-20 上午10:50:11
	 * @return
	 * @throws Exception
	 */
	public String qianfa() throws SystemException, DAOException,
			ServiceException {
		try {
			if (formId != null && !formId.equals("")) {
				formId = formId.toUpperCase();
			}
			// 将提醒(方式|内容)存储在session中
			saveMessageSession();
			// 将签收信息存储在session中
			saveSignInfoSession();
			// 将迁移线信息保存在session中
			saveTransitionInfoSession();
			// 处理意见信息
			doFilterSuggestion();
			// 处理ca信息
			doCASignInfo();
			// 生成日志信息
			genOALogInfo();
			
			// 适配选择人员
			strTaskActors = adapterBaseWorkflowManager.getActorAdpter()
					.getActors(strTaskActors);

			// 将用户信息保存在session中
			saveStrTaskActorsSession();
			StringBuilder thisTaskId = new StringBuilder();

			//gotoNextWorkflow(thisTaskId);
			qianfaNextWorkflow(thisTaskId);

			// 处理文件跟踪
			doTraceDoc();
			// 执行完成之后清空SESSION中数据,added by dengzc 2010年12月16日15:13:00
			clearMessageSession();
			// 保存 定时提醒信息
			saveRemindInfo(thisTaskId.toString());
			// ----------END--------------------
			if (formData == null) {// 不是在表单中提交流程,可能是在草稿中提交流程
				return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");// 提交至流程后关闭窗口
			}
			return null;
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
	 * 抚州高新区定制签发功能，提交签发操作 
	 * from 林业厅
	 * @author:
	 * @date:2012-09-20 上午10:55:11
	 * @return
	 * @throws Exception
	 */
	private void qianfaNextWorkflow(StringBuilder thisTaskId)
			throws SystemException, DAOException, ServiceException {
		try {
			if (taskId != null && !"".equals(taskId)) {//  提交下一步
				OtherParameter otherparameter = initOtherParameter();
				String[] info = getManager().getFormIdAndBussinessIdByTaskId(
						taskId);
				bussinessId = info[0];
				String curUserId = adapterBaseWorkflowManager.getUserService()
						.getCurrentUser().getUserId();

				if (rUserId != null && !"".equals(curUserId)) {
					curUserId = rUserId;
				}
				String isNewForm = "1";
				if ("0".equals(bussinessId)) {
					isNewForm = "1";
				} else {
					isNewForm = "0";
				}
				// 同步工作流中的businessName yanjian 2012-01-05 15:57
				synchronizedPiBusName();
				String[] strTaskActorArray = null;
				if ("".equals(strTaskActors)) {
				} else {
					// 加入对串行会签排序支持,按排序号倒序排列.added by dengzc 2011年2月16日11:34:07
					strTaskActorArray = strTaskActors.split(",");
					doFilterTaskActors(strTaskActorArray);
					// ----------- End --------------
				}
				VoFormDataBean bean = getManager().qianfaWorkflowNextStep(
						taskId, transitionName, "", isNewForm, formId,
						bussinessId, CASignInfo, curUserId, strTaskActorArray,
						formData, otherparameter, logInfo);
				if (bean != null) {
					bean.deleteFile();
				}
				//重新设置流程期限时间
				if (otherparameter != null
						&& otherparameter.getProcessTimer() != null) {
					getManager().reSetProcessTimer(instanceId,
							otherparameter.getProcessTimer());
					adapterBaseWorkflowManager.getWorkflowService()
							.setJobExecutorTime(
									otherparameter.getProcessTimer().getTime());
				}
				thisTaskId.append(instanceId);
			} else {
				throw new ServiceException("当前上下文变量taskId为null或为空字符串，不能使用该接口");
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
	
	/**
	 * 获取流程下一步可选迁移线路径 通过$.getJSON方式读取,通过JSON格式传递到前台 JSONObject html = new
	 * JSONObject(); 1、正常情况下返回迁移线集合:html.put("transHtml",transHtml);
	 * 2、如不存在可选迁移线,返回“无迁移线的提示”:html.put("transHtml","无下一步可选步骤！");
	 * 3、出现异常情况,返回异常提示信息：html.put("transHtml",e.getMessage);
	 * 
	 * @author:邓志城
	 * @date:2009-12-13 下午09:36:00
	 * @return html.toString()
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getNextTransitions() throws Exception {
		JSONObject html = new JSONObject();// 保存返回到前台的数据
		String EMPTY_STRING = "";// 出现错误时,usersHtml传递空字符串
		try {
			// 获取流程流转的迁移线
			List list = getManager().getNextTransitions(taskId, workflowName);
			//处理迁移线信息，并且返回迁移线信息存储对象（包含提交下一步迁移线信息，迁移线id信息，节点id信息）
			TransitionsInfoBean transitionsInfoBean = getManager().doNextTransitionsBySelectActorSetStyle(list);
			if(transitionsInfoBean != null){
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
								logger.error(e);
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
				//标识是否为菜单模式
				String isSetGroupflag = getRequest().getParameter("isMenuWin");
				if(isSetGroupflag != null && "1".equals(isSetGroupflag)){
					//菜单模式需分组使用
					isSetGroup = true;
				}
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
				StringBuffer transHtml = new StringBuffer();
				StringBuffer usersHtml = new StringBuffer();
				int nc = 0;
				String path = getRequest().getContextPath();
				if (path.endsWith("/"))
					path = "";

				// transGroupIdAndName:存放分组信息，格式：分组(0)ID1|Name1 (1)ID2|Name2...
				List<String> transGroupIdAndNameList = new LinkedList<String>();
				for (String key : transGroupList) {
					//过滤迁移线分组信息【过滤依据是判断该迁移线所属分组是否为意见征询】	严建 2012-06-23 15:27
					if (key.equals(FormGridDataHelper.getColorSetProperties()
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
					}
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

					transHtml.append("<div id=div_" + key
							+ " class=\"column\">");
					if (groupName != null) {
						String transGroupIdAndName = key + "@" + groupName;
						if (!transGroupIdAndNameList
								.contains(transGroupIdAndName)) {
							transGroupIdAndNameList.add(transGroupIdAndName);
						}
						transHtml.append("<h3  onclick=\"doSelectUI('" + key
								+ "')\" id=h3_" + key + ">");
						// .append("<input type=\"checkbox\">全选")
						if (nc == 0) {
							transHtml.append("<img title=\"折叠\" id=img_" + key
									+ " src=" + path
									+ "/oa/image/work/expand_arrow.png />");// 展开的节点为展开图标
						} else {
							transHtml.append("<img title=\"展开\" id=img_" + key
									+ " src=" + path
									+ "/oa/image/work/collapse_arrow.png />");
						}
						transHtml.append(groupName.toString()).append("</h3>");
						// yanjian 2011-12-10 23:48 菜单模式的办理方式
						// 需要添加一个添加隐藏域click来调用doMenuSelectUI方法
						transHtml
								.append("<input type=\"hidden\" onclick=\"doMenuSelectUI('"
										+ key + "')\" id=input_" + key + ">");
					}
					if (nc == 0) {
						transHtml.append("<ul id=ul_" + key + ">");
					} else {
						transHtml.append("<ul style=\"display: none;\" id=ul_"
								+ key + ">");
					}
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
						// String[] nodeInfo =
						// String.valueOf(transInfo).split("\\|");
						// String nodeId = (String)nodeInfo[1];//迁移线对应的节点id
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
							transHtml
									.append("<li><input ")
									.append(isChecked)
									.append(
											" tranlineValidate=\""
													+ tranlineValidate
													+ "\" selectPersonType=\""
													+ selectPersonType
													+ "\" concurrentFlag=\""
													+ concurrentFlag
													+ "\" type=\""
													+ inputType
													+ "\" group=\""
													+ key
													+ "\" transDept="
													+ trans[6]
													+ " id=\""
													+ tranId
													+ "\" name=\"transition\" nodeid='"
													+ nodeId
													+ "' value=\""
													+ tranName
													+ "\" state='1' onclick='chooseNextStep("
													+ tranId + ","
													+ concurrentFlag + ","
													+ nodeId + ",\"" + tranName
													+ "\",1);'>");
						} else {
							if (nodeId == null) {
								nodeId = tempNodeId;
							}
							if(String.valueOf(transInfo).indexOf("subactiveSet") != -1){
								for (Iterator<String> itt = transInfo.iterator(); itt
								.hasNext();) {
									String[] newnodeInfo = String.valueOf(itt.next()).split("\\|");
									if(("subProcessNode").equals(newnodeInfo[0])){
										nodeId = newnodeInfo[1];
										newnodeId = newnodeInfo[1];
									}
								}
							}
							transHtml
									.append("<li><input ")
									.append(isChecked)
									.append(
											" tranlineValidate=\""
													+ tranlineValidate
													+ "\" selectPersonType=\""
													+ selectPersonType
													+ "\" concurrentFlag=\""
													+ concurrentFlag
													+ "\" type=\""
													+ inputType
													+ "\" group=\""
													+ key
													+ "\" transDept="
													+ trans[6]
													+ " id=\""
													+ tranId
													+ "\" name=\"transition\" nodeid='"
													+ nodeId
													+ "' value=\""
													+ tranName
													+ "\" state='0' onclick='chooseNextStep("
													+ tranId + ","
													+ concurrentFlag + ","
													+ nodeId + ",\"" + tranName
													+ "\",0);'>");
						}
						transHtml.append("<label for=" + tranId + ">");
						transHtml.append(tranName);
						transHtml.append("</label></li>");
						usersHtml.append("<div id=\"nodeDiv_" + tranId
								+ "\" style=\"display:none\">");
						if("buttonType".equals(selectPersonType)){//按钮型选择人员方式
							usersHtml.append(nodeName);
							usersHtml.append("处理人：<span transId=\""
									+ tranId + "\" id=\"users_");
							usersHtml.append(nodeId);
							usersHtml.append("\">&nbsp;</span> &nbsp;");
							usersHtml
									.append("<input type=\"button\" value=\"选择人员\" class=\"input_bg\" onclick=\"chooseButtonActors();\">");
							usersHtml
									.append("<input type='hidden' id='taskActor_"
											+ nodeId
											+ "' transId=\""
											+ tranId
											+ "\"><input transId=\""
											+ tranId
											+ "\" type='hidden' id='strTaskActors_"
											+ nodeId + "'><br><br>");
						}else{
							for (Iterator<String> it = transInfo.iterator(); it
								.hasNext();) {
								nodeInfo = String.valueOf(it.next()).split("\\|");
								String actorFlag = (String) nodeInfo[0];// 是否需要选择人员activeSet:需要重新选择人员
								nodeName = (String) nodeInfo[2];// 迁移线对应的节点名称
								nodeId = (String) nodeInfo[1];// 节点id
								if ("activeSet".equals(actorFlag)
										|| "subactiveSet".equals(actorFlag)
										|| "supactiveSet".equals(actorFlag)) {// 允许动态设置处理人
									int paramTransId = 0;
									if ("activeSet".equals(actorFlag)) {// 迁移线上选择人员只针对当前流程
										paramTransId = Integer.parseInt(tranId);
									}
									if (transInfo.size() > 1
											&& String.valueOf(transInfo).indexOf(
													"subProcessNode") != -1) {// 启动子流程时，需要选择返回
										// 父流程时的人员和子流程第一步处理人
										if ("subactiveSet".equals(actorFlag)) {
											paramTransId = Integer.parseInt(tranId);// 选择迁移线上的人员为子流程上的人员
										} else {
											paramTransId = 0;// 返回父流程选择人员以节点上为准
										}
									}
									if(newnodeId!=null&&!"".equals(newnodeId)){
										nodeId = newnodeId;
									}
									usersHtml.append(nodeName);
									usersHtml.append("处理人：<span transId=\""
											+ tranId + "\" id=\"users_");
									usersHtml.append(nodeId);
									usersHtml.append("\">&nbsp;</span> &nbsp;");
									// usersHtml.append("<input type=\"button\"
									// value=\"选择人员\" class=\"input_bg\"
									// onclick=\"chooseActors(" + nodeId + "," +
									// paramTransId + ");\">");
									// usersHtml.append("<input type='hidden'
									// id='taskActor_" + nodeId + "'><input
									// transId=\""+tranId+"\" type='hidden'
									// id='strTaskActors_" + nodeId + "'><br><br>");
									usersHtml
											.append("<input type=\"button\" value=\"选择人员\" class=\"input_bg\" onclick=\"chooseActors("
													+ nodeId
													+ ","
													+ paramTransId
													+ "," + tranId + ");\">");
									usersHtml
											.append("<input type='hidden' id='taskActor_"
													+ nodeId
													+ "' transId=\""
													+ tranId
													+ "\"><input transId=\""
													+ tranId
													+ "\" type='hidden' id='strTaskActors_"
													+ nodeId + "'><br><br>");
								} else {// 不需要动态设置处理人
									// usersHtml.append("<input type=\"button\"
									// value=\"选择人员\" class=\"input_bg\"
									// disabled><br><br>");
								}
		
							}
						}
						usersHtml.append("</div>");
					}
					transHtml.append("</ul>");
					transHtml.append("</div>");
					nc++;
					/*
					 * if(nc !=0 && nc % 3 == 0) { transHtml.append("<br style=\"clear: left\" />
					 * <!--Break after 3rd column. Move this if desired-->"); }
					 */
				}

				// 构建menu模式下的 按钮 yanjian 2011-12-11 0:17
				StringBuilder menuButtonTableHtml = new StringBuilder();
				int index = 0;
				for (String transGroupIdAndName : transGroupIdAndNameList) {
					index = index + 1;
					String transGroupId = transGroupIdAndName.split("@")[0];
					String transGroupName = transGroupIdAndName.split("@")[1];
					menuButtonTableHtml.append(
							"<td id=\"groupButton" + transGroupId + "\">")
							.append(
									//"<a class=\"Operation\" onclick=\"selecttransGroupById('"
									"<a class=\"button\" onclick=\"selecttransGroupById('"
											+ transGroupId
											+ "')\" href=\"#\">&nbsp;"
											+ transGroupName + "&nbsp;</a>")
							.append("</td>");
					menuButtonTableHtml.append("<td width=\"5\"></td>");
				}

				html.put("transHtml", transHtml.toString());
				html.put("usersHtml", usersHtml.toString());
				html.put("menuButtonTableHtml", menuButtonTableHtml.toString());

				logger.info("usersHtml:" + usersHtml);
				logger.info("transHtml:" + transHtml.toString());
				logger.info("menuButtonTableHtml:"
						+ menuButtonTableHtml.toString());
			} else {
				html.put("transHtml", "无下一步可选步骤！");
				html.put("usersHtml", EMPTY_STRING);
			}
		} catch (Exception e) {
			logger.error("出现异常，异常信息：", e);
			html.put("transHtml", "出现异常，异常信息：" + e.getMessage());
			html.put("usersHtml", EMPTY_STRING);
		}
		return this.renderText(html.toString());
	}

	/**
	 * 查看流程图
	 * 
	 * @author:邓志城
	 * @date:2009-12-14 下午08:35:56
	 * @see /WEB-INF/jsp/workflow/pdimageview.jsp
	 * @return
	 * @throws Exception
	 */
	public String PDImageView() throws Exception {
		if (getRequest().getParameter("instanceId") != null
				&& !getRequest().getParameter("instanceId").toString().equals(
						"")) {
			instanceId = getRequest().getParameter("instanceId").toString();
		}
		Object[] obj = getManager().getWorkflowMonitorData(instanceId);
		getRequest().setAttribute("model", obj);
		getRequest().setAttribute("token", String.valueOf(obj[3]));
		return "pdimageview";
	}

	/**
	 * 保存电子表单数据 如果操作成功，返回:业务数据到前台 如果操作失败，返回-2 如果未读取到电子表单数据返回-1
	 * 
	 * @author:邓志城
	 * @date:2009-12-14 下午09:44:14
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public InputStream saveFormData() throws Exception {
		InputStream responseData = null;
		String ret = "0";
		try {
			if (formData != null) {
				VoFormDataBean bean = getManager().saveFormData(formData,
						workflowName, state, businessName);// getManager().saveForm(formData);
				ret = bean.getBusinessId();
				bean.deleteFile();
			} else {
				ret = "-1";
			}
		} catch (Exception ex) {
			logger.error("保存电子表单出现异常，异常信息：", ex);
			ret = "-2";
		}
		responseData = new ByteArrayInputStream(ret.toString()
				.getBytes("utf-8"));
		return responseData;
	}

	/**
	 * 任务指派
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午07:09:51
	 * @param taskId
	 *            -任务实例Id
	 * @param reAssignActorId
	 *            -重新指派人员Id
	 * @param isNeedReturn
	 *            -指派是否需要返回
	 * @param formData
	 *            -电子表单数据
	 * @return ret 1、操作成功返回0； 2、任务实例不存在返回-1； 3、业务数据传输错误返回-3； 4、出现异常返回-2；
	 * @throws Exception
	 * 
	 * 修改时间2010-02-05 author:胡丽丽
	 * @param remidType
	 *            -提醒类型
	 */
	@SuppressWarnings("unchecked")
	public String reAssign() throws Exception {
		try {
			List<String> userlist = new ArrayList<String>();
			String reAssignActorId = "";
			String isNeedReturn = "";
			// 将提醒(方式|内容)存储在session中
			JSONObject jsonObj = JSONObject.fromObject(suggestion);
			String users = jsonObj.getString("users");
			// String[] userstr=users.split("|");
			if (!"".equals(users)) {// 用户是否为空
				String[] userstr1 = users.split(",");
				reAssignActorId = userstr1[0].substring(1);// 用户ID
				isNeedReturn = userstr1[2];
			}
			userlist.add(reAssignActorId);
			remindType = jsonObj.getString("remindType");
			ActionContext cxt = ActionContext.getContext();
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put(
					"handlerMes",
					getText(GlobalBaseData.WORKFLOW_RESSIGN_REMIND,
							new String[] {
									adapterBaseWorkflowManager.getUserService()
											.getCurrentUser().getUserName(),
									businessName }));
			cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
			ReAssignParameter parameter = new ReAssignParameter();
			parameter.setTaskId(taskId);
			parameter.setReAssignActorId(reAssignActorId);
			parameter.setIsNeedReturn(isNeedReturn);
			parameter.setFormData(formData);
			parameter.setAllowChangeMainActor(allowChangeMainActor);
			VoFormDataBean bean = getManager().reAssignTask(
					parameter,
					new OALogInfo(getText(GlobalBaseData.WORKFLOW_RESSIGN_LOG,
							new String[] {
									adapterBaseWorkflowManager.getUserService()
											.getCurrentUser().getUserName(),
									businessName,
									adapterBaseWorkflowManager.getUserService()
											.getUserNameByUserId(
													reAssignActorId) })));
			if (bean != null) {
				bean.deleteFile();
			}
			List<String> list = new ArrayList<String>();
			list.add(reAssignActorId);
			adapterBaseWorkflowManager.getRemindService()
					.sendRemind(list, null);// 发送提醒
			if (formData == null) {
				return this.renderText("0");// 兼容在页面上指派任务
			}
			return null;
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("指派流程时出现异常,异常信息：" + ex.getMessage());
		}
	}

	/**
	 * 任务指派返回
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午07:09:51
	 * @param taskId
	 *            -任务实例Id
	 * @param formId
	 *            -表单Id
	 * @param formData
	 *            -电子表单数据
	 * @return ret 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现异常返回-2；
	 * @throws Exception
	 */
	public String returnReAssign() throws Exception {
		try {
			VoFormDataBean bean = getManager().returnReAssignTask(taskId, "0",
					Long.parseLong(formId), formData,
					new OALogInfo("任务指派返回,taskId=" + taskId));
			bean.deleteFile();
			return null;
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("任务指派返回时出现异常,异常信息：" + ex.getMessage());
		}
	}

	/**
	 * 退回上一办理人
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午07:09:51
	 * @param taskId
	 *            -任务实例Id
	 * @param formId
	 *            -表单Id
	 * @param formData
	 *            -电子表单数据
	 * @return ret 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现异常返回-2；
	 *         4、出现系统异常返回-3,一般是电子表单数据未获取到引起
	 * @throws Exception
	 * 
	 * 修改时间2010-02-05 胡丽丽
	 * @param jsonObj-提醒信息数据
	 * @param content
	 *            -提醒内容
	 * @param remidType
	 *            -提醒类型
	 */
	@SuppressWarnings("unchecked")
	public String backlast() throws Exception {
		try {
			String content = "";
			if (suggestion != null && !"".equals(suggestion)) {
				/* modify yanjian 2011-11-06 bug-2635 */
				suggestion = java.net.URLDecoder.decode(suggestion, "UTF-8");
				JSONObject jsonObj = JSONObject.fromObject(suggestion);
				content = jsonObj.getString("suggestion");
				String[] contents = content.split("＜BR＞");
				String content_bak = null;
				for (int i = 0; i < contents.length; i++) {
					if (content_bak == null) {
						content_bak = "";
					}
					if (i < contents.length - 1) {
						content_bak += contents[i] + "\r\n";
					} else {
						content_bak += contents[i];
					}
				}
				if (content_bak != null) {
					content = content_bak;
				}
				remindType = jsonObj.getString("remindType");
				// 将提醒(方式|内容)存储在session中
				ActionContext cxt = ActionContext.getContext();
				cxt.getSession().put("remindType", remindType);
				cxt.getSession().put("handlerMes", content);
				cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
				//发送消息 by tj
				User user = userService.getCurrentUser();
				Organization organization = userService.getUserDepartmentByUserId(user.getUserId());
				if(organization!=null){
					if(organization.getOrgManager()!=null){
						String[] userids = organization.getOrgManager().split(",");
						List<String> receiver = new ArrayList<String>();
						receiver.add(user.getUserId());
						if(userids!=null&&userids.length>0){
							for(int i = 0;i<userids.length;i++){
								receiver.add(userids[i]);
							}
						}
						iMManager.sendIMMessage(content, receiver, null);
					}
				}
			}
			JSONObject json = new JSONObject();
			json.put("suggestion", content + "     ");
			json.put("CAInfo", "");
			String curUserId = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser().getUserId();

			if (rUserId != null && !"".equals(curUserId)) {
				curUserId = rUserId;
			}
			BackSpaceParameter parameter = new BackSpaceParameter();
			parameter.setTaskId(taskId);
			parameter.setFormId(formId);
			parameter.setSuggestion(json.toString());
			parameter.setFormData(formData);
			parameter.setCurUserId(curUserId);
			if("1".equals(daiBan)){
				parameter.setDaiBan(true);
			}
			if("1".equals(daiBan)){
				parameter.setDaiBan(true);
			}
			VoFormDataBean bean = getManager().backSpaceLast(parameter,
					new OALogInfo("任务退回上一办理人，taskId=" + taskId));
			if (bean != null) {
				bean.deleteFile();//modify by hecj 2012-4-7 15:55因为退回获取到bean为null，执行时报异常，所以加上判断条件
			}
			return null;
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("退回上一办理人时出现异常,异常信息：" + ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void loopAddUserId(List<String> userids, String instanceId) {
		List<Object[]> childrenPList = null;
		Object[] returnObjs = adapterBaseWorkflowManager.getWorkflowService()
				.getProcessStatusByPiId(instanceId.toString());// 得到此流程实例下的运行情况
		Collection col = (Collection) returnObjs[6];// 处理任务信息
		if (col != null && !col.isEmpty()) {//获取当前处理人的id
			List list = new ArrayList(col);
			for (Object obj : list) {
				Object[] itObjs = (Object[]) obj;
				String flag = (String) itObjs[0];
				if (flag.equals("task")) {//任务
					String userId = (String) itObjs[3];
					if (userId != null && !"".equals(userId)) {
						String[] userIdArr = userId.split(",");
						for (String userIdTemp : userIdArr) {
							if (userids.indexOf(userIdTemp) == -1) {
								userids.add(userIdTemp);
							}
						}
					}
				} else {//子流程
					childrenPList = adapterBaseWorkflowManager
							.getWorkflowService()
							.getMonitorChildrenInstanceIds(
									new Long(instanceId.toString()));
					for (Object[] objs : childrenPList) {
						loopAddUserId(userids, objs[0].toString());
					}
				}
			}
		}
	}

	/**
	 * 获取当前用户信息
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime May 8, 2012 4:21:45 PM
	 */
	public String getCurrentUserInfo() throws Exception {
		try {
			if (instanceId == null || "".equals(instanceId)
					|| "null".equals(instanceId)) {
				throw new SystemException(
						"instanceId is not null or empty String !");
			}
			List<String> userids = new LinkedList<String>();
			loopAddUserId(userids, instanceId);
			//		List<Object[]> childrenPList =(List<Object[]>) adapterBaseWorkflowManager.getWorkflowService().getMonitorChildrenInstanceIds(new Long(instanceId.toString()));
			//		
			//		Object[] returnObjs = adapterBaseWorkflowManager.getWorkflowService()
			//		.getProcessStatusByPiId(instanceId.toString());// 得到此流程实例下的运行情况
			//                Collection col = (Collection) returnObjs[6];// 处理任务信息
			//                if (col != null && !col.isEmpty()) {//获取当前处理人的id
			//                    List list = new ArrayList(col);
			//                    for (Object obj:list) {
			//        		Object[] itObjs = (Object[]) obj;
			//        		String userId = (String) itObjs[3];
			//        		if(userId != null && !"".equals(userId)){
			//        		    if(userids == null) {
			//        			userids = new LinkedList<String>();
			//        		    }
			//        		    String[] userIdArr = userId.split(",");
			//        		    for(String userIdTemp:userIdArr){
			//        			if(userids.indexOf(userIdTemp) == -1){
			//        			    userids.add(userIdTemp);
			//        			}
			//        		    }
			//        		}
			//                    } 
			//                }
			StringBuilder curInfo = null;
			if (!userids.isEmpty()) {
				Map<String, UserBeanTemp> map = adapterBaseWorkflowManager
						.getSendDocUploadManager().getUserIdMapUserBeanTemp(
								userids);
				for (String userIdTemp : map.keySet()) {
					if (curInfo == null) {
						curInfo = new StringBuilder();
					}
					UserBeanTemp userbeantemp = map.get(userIdTemp);
					curInfo.append(",").append(userbeantemp.getUserName())
							.append("[" + userbeantemp.getOrgName() + "]");
				}
				if (curInfo != null && curInfo.length() > 0) {
					curInfo.deleteCharAt(0);
				}
			}
			if (curInfo != null) {
				return this.renderText(curInfo.toString());
			} else {
				return this.renderText("");
			}
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("获取当前处理人信息异常：" + ex.getMessage());
		}
	}

	/**
	 * 强制取回
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Apr 28, 2012 8:11:47 PM
	 */
	@SuppressWarnings("unchecked")
	public String mustFetchTask1() throws Exception {
		try {
			ActionContext cxt = ActionContext.getContext();
			String content = "";
			System.out.println(remindType);
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", content);
			cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
			JSONObject json = new JSONObject();
			json.put("suggestion", content + "     ");
			json.put("CAInfo", "");
			User user = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser();
			String curUserId = user.getUserId();

			if (rUserId != null && !"".equals(curUserId)) {
				curUserId = rUserId;
			}
			BackSpaceParameter parameter = new BackSpaceParameter();
			parameter.setFormId(formId);
			parameter.setSuggestion(json.toString());
			parameter.setFormData(null);///不处理formData
			parameter.setCurUserId(curUserId);
			parameter.setHandleProcess(false);//不产生办理记录
			if (bussinessId != null && !"".equals(bussinessId)) {
				String[] temp = bussinessId.split(",");// 退回的节点id,指定退回用户id
				parameter.setReturnNodeId(temp[0]);
				if (temp.length > 1) {
					String[] taskActors = new String[] { temp[1] + "|"
							+ temp[0] };
					parameter.setTaskActors(taskActors);
				}
			}
			Object[] monitorData = adapterBaseWorkflowManager
					.getWorkflowService().getProcessInstanceMonitorData(
							new Long(instanceId));
			List subinfo = (List) monitorData[5];//获取当前流程产生的子流程Id
			if (subinfo != null && !subinfo.isEmpty()) {
				ProcessInstanceBean processInstanceBean = (ProcessInstanceBean)monitorData[0];
				JbpmContext jbpmcontext = null;
				for (Object obj : subinfo) {//结束子流程
					if (jbpmcontext == null) {
						jbpmcontext = adapterBaseWorkflowManager
								.getWorkflowService().getJbpmContext();
					}
					Object[] objs = (Object[]) obj;
					String subPid = StringUtil.castString(objs[0]);
					adapterBaseWorkflowManager.getWorkflowService()
							.loopEndProcess(
									jbpmcontext,
									subPid,
									user.getUserName() + "【" + user.getUserId()
											+ "】强制结束",new OALogInfo(processInstanceBean.getBusinessName()+"被"+user.getUserName()+"【" + user.getUserId()+ "】强制结束"));
				}
				if (jbpmcontext != null) {
					jbpmcontext.close();
					jbpmcontext = null;
				}
			}
			mustFetchTask(instanceId, parameter);//强制取回到指定节点
//			saveRemindInfo(instanceId.toString());
			//手机短信提醒
			if(remindType.indexOf("SMS")>-1){
				List<String> userids = new LinkedList<String>();
				loopAddUserId(userids, instanceId);
				if(remindSet.indexOf(";")>-1){
					smsService.sendSms("person", userids, remindSet.substring(remindSet.lastIndexOf(";")+1));
				}else{
					smsService.sendSms("person", userids, remindSet);
				}
			}
			return renderText("true");
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("退回流程时出现异常,异常信息：" + ex.getMessage());
		}
	}
	/**
	 * 强制取回
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Apr 28, 2012 8:11:47 PM
	 */
	@SuppressWarnings("unchecked")
	public String mustFetchTask() throws Exception {
		try {
			ActionContext cxt = ActionContext.getContext();
			String content = "";
			System.out.println(remindType);
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", content);
			cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
			JSONObject json = new JSONObject();
			/**
			 * 为解决
			 * bug:0000048059
			 * 强制取回时将输入的提醒内容作为待办事宜列表中退回图标的标题内容
			 * 
			 */
			if(remindSet.indexOf(";")>-1){
				String[] splitArr=remindSet.split(";");
				content=splitArr[splitArr.length-1];
			}
			
			json.put("suggestion", content + "     ");
			json.put("CAInfo", "");
			User user = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser();
			String curUserId = user.getUserId();

			if (rUserId != null && !"".equals(curUserId)) {
				curUserId = rUserId;
			}
			BackSpaceParameter parameter = new BackSpaceParameter();
			parameter.setFormId(formId);
			parameter.setSuggestion(json.toString());
			parameter.setFormData(null);///不处理formData
			parameter.setCurUserId(curUserId);
			parameter.setHandleProcess(false);//不产生办理记录
			if (bussinessId != null && !"".equals(bussinessId)) {
				String[] temp = bussinessId.split(",");// 退回的节点id,指定退回用户id
				parameter.setReturnNodeId(temp[0]);
				if (temp.length > 1) {
					String[] taskActors = new String[] { temp[1] + "|"
							+ temp[0] };
					parameter.setTaskActors(taskActors);
				}
			}
			Object[] monitorData = adapterBaseWorkflowManager
					.getWorkflowService().getProcessInstanceMonitorData(
							new Long(instanceId));
			List subinfo = (List) monitorData[5];//获取当前流程产生的子流程Id
			if (subinfo != null && !subinfo.isEmpty()) {
				ProcessInstanceBean processInstanceBean = (ProcessInstanceBean)monitorData[0];
				JbpmContext jbpmcontext = null;
				for (Object obj : subinfo) {//结束子流程
					if (jbpmcontext == null) {
						jbpmcontext = adapterBaseWorkflowManager
								.getWorkflowService().getJbpmContext();
					}
					Object[] objs = (Object[]) obj;
					String subPid = StringUtil.castString(objs[0]);
					adapterBaseWorkflowManager.getWorkflowService()
							.loopEndProcess(
									jbpmcontext,
									subPid,
									user.getUserName() + "【" + user.getUserId()
											+ "】强制结束",new OALogInfo(processInstanceBean.getBusinessName()+"被"+user.getUserName()+"【" + user.getUserId()+ "】强制结束"));
				}
				if (jbpmcontext != null) {
					jbpmcontext.close();
					jbpmcontext = null;
				}
			}
			mustFetchTask(instanceId, parameter);//强制取回到指定节点
//			saveRemindInfo(instanceId.toString());
			//手机短信提醒
			if(remindType.indexOf("SMS")>-1){
				List<String> userids = new LinkedList<String>();
				loopAddUserId(userids, instanceId);
				if(remindSet.indexOf(";")>-1){
					smsService.sendSms("person", userids, remindSet.substring(remindSet.lastIndexOf(";")+1));
				}else{
					smsService.sendSms("person", userids, remindSet);
				}
			}
			return null;
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("退回流程时出现异常,异常信息：" + ex.getMessage());
		}
	}
	/**
	 * 根据流程实例id将流程强制取回到指定节点
	 * 
	 * @description
	 * @author 严建
	 * @param instanceId
	 * @param parameter
	 * @createTime Apr 28, 2012 10:44:10 AM
	 */
	private void mustFetchTask(String instanceId, BackSpaceParameter parameter) {
		List<Object[]> todoTaskIds = getManager().getTodoTaskIdsByPid(
				instanceId);
		for (Object[] objs : todoTaskIds) {
			String ataskId = StringUtil.castString(objs[0]);
			if (adapterBaseWorkflowManager.getWorkflowService()
					.getTaskInstanceById(ataskId).getEnd() == null) {//任务未结束
				String ataskNodeId = StringUtil.castString(objs[1]);
				if (!ataskNodeId.equals(parameter.getReturnNodeId())) {// 当前任务节点id和退回节点id不相等时，进行强制取回操作
					parameter.setTaskId(ataskId);
					VoFormDataBean bean = getManager().backSpace(parameter,
							new OALogInfo("任务退回，taskId=" + taskId));
					if (bean != null) {
						bean.deleteFile();
					}
				}
			}
		}
	}

	/**
	 * 退回任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午07:09:51
	 * @param taskId
	 *            -任务实例Id
	 * @param formId
	 *            -表单Id
	 * @param formData
	 *            -电子表单数据
	 * @param returnNodeId
	 *            需要退回到的目标节点
	 * @return ret 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现异常返回-2；
	 *         4、出现系统异常返回-3,一般是电子表单数据未获取到引起
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String back() throws Exception {
		try {
			ActionContext cxt = ActionContext.getContext();
			String content = "";
			if (suggestion != null && !"".equals(suggestion)) {
				/* modify yanjian 2011-11-06 bug-2635 */
				suggestion = java.net.URLDecoder.decode(suggestion, "UTF-8");
				// suggestion = suggestion.replaceAll("'", "\\\\'");
				JSONObject jsonObj = JSONObject.fromObject(suggestion);
				content = jsonObj.getString("suggestion");
				String[] contents = content.split("＜BR＞");
				String content_bak = null;
				for (int i = 0; i < contents.length; i++) {
					if (content_bak == null) {
						content_bak = "";
					}
					if (i < contents.length - 1) {
						content_bak += contents[i] + "\r\n";
					} else {
						content_bak += contents[i];
					}
				}
				if (content_bak != null) {
					content = content_bak;
				}
				remindType = jsonObj.getString("remindType");
			}
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", content);
			cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
			JSONObject json = new JSONObject();
			json.put("suggestion", content + "     ");
			json.put("CAInfo", "");

			String curUserId = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser().getUserId();

			if (rUserId != null && !"".equals(curUserId)) {
				curUserId = rUserId;
			}
			BackSpaceParameter parameter = new BackSpaceParameter();
			parameter.setTaskId(taskId);
			parameter.setFormId(formId);
			parameter.setSuggestion(json.toString());
			parameter.setFormData(formData);
			parameter.setCurUserId(curUserId);
			if("1".equals(daiBan)){
				parameter.setDaiBan(true);
			}
			if (bussinessId != null && !"".equals(bussinessId)) {
				String[] temp = bussinessId.split(",");// 退回的节点id,指定退回用户id
				parameter.setReturnNodeId(temp[0]);
				if (temp.length > 1) {
					String[] taskActors = new String[] { temp[1] + "|"
							+ temp[0] };
					parameter.setTaskActors(taskActors);
				}
			}
			VoFormDataBean bean = getManager().backSpace(parameter,
					new OALogInfo("任务退回，taskId=" + taskId));
			bean.deleteFile();
			return null;
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException("退回流程时出现异常,异常信息：" + ex.getMessage());
		}
	}

	/**
	 * 驳回任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午07:09:51
	 * @param taskId
	 *            -任务实例Id
	 * @param formId
	 *            -表单Id
	 * @param formData
	 *            -电子表单数据
	 * @param returnNodeId
	 *            需要退回到的目标节点
	 * @return ret 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现异常返回-2；
	 *         4、出现系统异常返回-3,一般是电子表单数据未获取到引起
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String bohui() throws Exception {
		String ret = "0";
		try {
			if (StringUtils.hasLength(taskId)) {

				/* modify yanjian 2011-11-06 bug-2635 */
				String content = "";
				if (suggestion != null && !"".equals(suggestion)) {

					suggestion = java.net.URLDecoder
							.decode(suggestion, "UTF-8");
					// suggestion = suggestion.replaceAll("'", "\\\\'");
					JSONObject jsonObj = JSONObject.fromObject(suggestion);
					content = jsonObj.getString("suggestion");
					String[] contents = content.split("＜BR＞");
					String content_bak = null;
					for (int i = 0; i < contents.length; i++) {
						if (content_bak == null) {
							content_bak = "";
						}
						if (i < contents.length - 1) {
							content_bak += contents[i] + "\r\n";
						} else {
							content_bak += contents[i];
						}
					}
					if (content_bak != null) {
						content = content_bak;
					}
					remindType = jsonObj.getString("remindType");
				}
				/*
				 * JSONObject jsonObj = JSONObject.fromObject(suggestion);
				 * String content = jsonObj.getString("suggestion"); remindType =
				 * jsonObj.getString("remindType");
				 */
				ActionContext cxt = ActionContext.getContext();
				cxt.getSession().put("remindType", remindType);
				cxt.getSession().put("handlerMes", content);
				cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
				if (bussinessId == null) {
					bussinessId = getRequest().getParameter("returnNodeId");
				}
				JSONObject json = new JSONObject();
				json.put("suggestion", content + "     ");
				json.put("CAInfo", "");
				if (formId == null || "".equals(formId)) {
					Object[] objs = getManager()
							.getFormIdAndBussinessIdByTaskId(taskId);
					formId = objs[1].toString();
				}
				String curUserId = adapterBaseWorkflowManager.getUserService()
						.getCurrentUser().getUserId();

				if (rUserId != null && !"".equals(curUserId)) {
					curUserId = rUserId;
				}
				BackSpaceParameter parameter = new BackSpaceParameter();
				parameter.setTaskId(taskId);
				parameter.setFormId(formId);
				parameter.setSuggestion(json.toString());
				parameter.setFormData(formData);
				parameter.setCurUserId(curUserId);
				if("1".equals(daiBan)){
					parameter.setDaiBan(true);
				}
				if (bussinessId != null && !"".equals(bussinessId)) {
					String[] temp = bussinessId.split(",");// 退回的节点id,指定退回用户id
					parameter.setReturnNodeId(temp[0]);
					//处理退文到签收节点，让处室所有人都能看到公文信息(签收节点设置动态选择处理人)	yanjian 2012-12-01 20:04
					String nodeId = parameter.getReturnNodeId();
					String[] taskActors = adapterBaseWorkflowManager.getNodeService().getSignNodeActorsByNodeId(nodeId);
					if(taskActors != null){	//是签收节点，且签收节点设置了可选择的人员
						parameter.setTaskActors(taskActors);
					}else{
						if (temp.length > 1) {
							taskActors = new String[] { temp[1] + "|"
									+ temp[0] };
							parameter.setTaskActors(taskActors);
						}
					}
				}
				VoFormDataBean bean = getManager().overRule(parameter,
						new OALogInfo("任务驳回，taskId=" + taskId));
				if (bean != null) {
					bean.deleteFile();
				}
			} else {
				ret = "-1";
			}
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			ret = "-3";
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			ret = "-2";
			throw new SystemException("驳回流程时出现异常,异常信息：" + ex.getMessage());
		}
		return this.renderText(ret);
	}

	/**
	 * 恢复会签挂起的任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-15 下午09:28:14
	 * @throws Exception
	 */
	public void resumeConSignTask() throws Exception {
		try {
			if (StringUtils.hasLength(taskId)) {
				getManager().resumeConSignTask(taskId);
			}
		} catch (Exception ex) {
			logger.error("恢复会签挂起的任务失败,异常信息：" + ex.getMessage());
		}
	}

	/**
	 * 人工对任务催办
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 上午11:12:07
	 * @return 催办结果 1、催办成功返回0； 2、流程实例不存在返回-1； 3、抛出异常返回-2；
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String urgencyProcessByPerson() throws Exception {
		List<String> remindType = new ArrayList<String>();
		String ret = "0";
		try {
			if (StringUtils.hasLength(urgencytype)) {
				
				  String[] urgency = urgencytype.split(",");
				  if(ObjectUtils.containsElement(urgency, "MSG")){//消息
				  remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE); }
				  if(ObjectUtils.containsElement(urgency,"SMS")){ //短信
				  remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_MESSAGE); }
				  if(ObjectUtils.containsElement(urgency,"EMAIL")){ //邮件方式
				  remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL); }
				  if(ObjectUtils.containsElement(urgency, "RTX")){//RTX
				  remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_RTX); }
				
				if (StringUtils.hasLength(instanceId)) {
					if (!StringUtils.hasLength(handlerMes)) {
						handlerMes = "请尽快处理你的待处理事务!";
					}
					// 将提醒(方式|内容)存储在session中
					ActionContext cxt = ActionContext.getContext();
					cxt.getSession().put("remindType", urgencytype);
					cxt.getSession().put("handlerMes", handlerMes);
					cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
					getManager().urgencyProcessByPerson(
							instanceId,
							null,
							remindType,
							handlerMes,
							new OALogInfo("催办任务,催办方式：" + urgencytype + ";催办模块:"
									+ getModuleType() + ";催办内容:" + handlerMes));
					ret = "0";
				} else {// 流程实例不存在
					ret = "-1";
				}
			} else {
				throw new SystemException("催办类型参数传输错误！");
			}
		} catch (Exception ex) {
			ret = "-2";
			logger.error("催办失败,异常信息：" + ex.getMessage());
		}
		return this.renderText(ret);
	}
	/**
	 * 反馈
	 * 
	 * @author:xush
	 * @date:1/6/2014 8:09 PM1/6/2014 8:09 PM
	 * @return 反馈结果 1、反馈成功返回0； 2、流程实例不存在返回-1； 3、抛出异常返回-2；
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
 public String feedBackProcessByPerson() throws Exception {
	//得到所有领导审批信息
   // List<Object[]> task = adapterBaseWorkflowManager
	//.getWorkflowService().getProcessHandlesAndNodeSettingByPiId(instanceId+"");
    List<String> handlerManId=new ArrayList<String>();
    orguserid= getRequest().getParameter("orguserid");
    String[] sendIds=orguserid.split(",");
//    for(Object[] o:task){
//    	String userId=(String)o[5];
//    	if(!userId.equals(userService.getCurrentUser().getUserId())){
//    	handlerManId.add((String)o[5]);}
//    }
    for(String msgReceiverId:sendIds){
    	if(!msgReceiverId.equals(userService.getCurrentUser().getUserId())){
    	  handlerManId.add(msgReceiverId);
    	  }
    }
    List<String> remindType = new ArrayList<String>();
	String ret = "0";
	if (StringUtils.hasLength(urgencytype)) {
			
			  String[] urgency = urgencytype.split(",");
			  if(ObjectUtils.containsElement(urgency, "MSG")){//消息
			  remindType.add(Constants.MSG); }
			  if(ObjectUtils.containsElement(urgency,"SMS")){ //短信
			  remindType.add(Constants.SMS); }
			  if(ObjectUtils.containsElement(urgency,"EMAIL")){ //邮件方式
			  remindType.add(Constants.EMAIL); }
			  if(ObjectUtils.containsElement(urgency, "RTX")){//RTX
			  remindType.add(Constants.RTX); }
	}
		if (StringUtils.hasLength(instanceId)) {
				if (!StringUtils.hasLength(handlerMes)) {
					handlerMes = "我已经处理完了!";
				}
				// 将提醒(方式|内容)存储在session中
				ActionContext cxt = ActionContext.getContext();
				cxt.getSession().put("remindType", urgencytype);
				cxt.getSession().put("handlerMes", handlerMes);
				cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
				//至少选择了一种提醒方式
				if(null!=remindType && !"".equals(remindType) && handlerManId!=null && !handlerManId.isEmpty()){
					for(String type:remindType){
						if(Constants.EMAIL.equals(type)){//邮件提醒
							try {
								mailService.autoSendMail(handlerManId,handlerMes, handlerMes, "person");
								logger.info("邮件提醒发送成功。");
								ret = "0";
							} catch (Exception e) {
								logger.info("发送邮件提醒发生异常。",e);
								ret = "-2";
							}
						}else if(Constants.MSG.equals(type)){//内部消息提醒
							messageService.sendMsg("person", handlerManId, getRequest().getParameter("xiha1"), handlerMes,null);
							logger.info("发送内部消息提醒成功！"+new Date());
							ret = "0";
						}else if(Constants.RTX.equals(type)){//Rtx提醒
							try {
								imService.sendIMMessage(handlerMes, handlerManId,null);
								logger.info("发送即时消息提醒成功！"+new Date());
								ret = "0";
							} catch (Exception e) {
								logger.error("发送即时消息提醒出错！"+new Date(),e);
								ret = "-2";
							}
						}else if(Constants.SMS.equals(type)){//手机短信提醒
							smsService.sendSms("person", handlerManId, handlerMes);
							logger.info("发送手机短信提醒成功！"+new Date());
							ret = "0";
						}
					}
				
			}
				}else {// 流程实例不存在
				ret = "-1";
			}
		
	   return this.renderText(ret);
	}
	
	/**
	 * 反馈
	 * 获取 文件相关的所有处理人员（已流转过的环节处理人） 
	 * @author:
	 * @date:2014年1月20日15:23:42
	 * @return 需要反馈的人员  id和用户名
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
 public String feedBackProcessPerson() throws Exception {

    List<Object[]> task = adapterBaseWorkflowManager
	.getWorkflowService().getProcessHandlesAndNodeSettingByPiId(instanceId+"");
	Object[] obj=workflowDelegation.getProcessInstanceByPiId(instanceId);
	 String  bussinessName=(String)obj[4];
 	String feedBackPersonId="";
	String feedBackPersonNames="";
	 /** 去除重复的 任务处理人id     ---start    */
	 List<Object> personlist=new ArrayList<Object>();
	    for(Object[] o:task){
	     	personlist.add(o[5]);
	    }
	    HashSet<Object> h = new HashSet<Object>(personlist);
	   	personlist.clear();
	   	personlist.addAll(h);
	  /** 去除重复的任务处理人 id  ---end    */
	    for(int i=0;i<personlist.size();i++){
			   String userid=(String)personlist.get(i);
				if(!userid.equals(userService.getCurrentUser().getUserId())){
					String userName =userService.getUserNameByUserId(userid);
					if(userName!=null && !"null".equals(userName)){
						feedBackPersonId += userid + ",";
						feedBackPersonNames += userName+ ",";
				      }
			   }
	}
	if (feedBackPersonId.length() > 1) {
		feedBackPersonId = feedBackPersonId.substring(0, feedBackPersonId
				.length() - 1);
	}
	if (feedBackPersonNames.length() > 1) {
		feedBackPersonNames = feedBackPersonNames.substring(0,
				feedBackPersonNames.length() - 1);
	}
	
	   return this.renderText(feedBackPersonId+":"+feedBackPersonNames+":"+bussinessName);
	}

	/*
	 * 
	 * Description:人工对单个任务实例进行催办 param: @author 彭小青 @date Feb 25, 2010 3:13:50
	 * PM
	 */
	@SuppressWarnings("unchecked")
	public String urgencyTaskInstanceByPerson() throws Exception {
		List<String> remindType = new ArrayList<String>();
		String ret = "0";
		try {
			if (StringUtils.hasLength(urgencytype)) {
				/*
				 * String[] urgency = urgencytype.split(",");
				 * if(ObjectUtils.containsElement(urgency, MSG)){//消息
				 * remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE); }
				 * if(ObjectUtils.containsElement(urgency,SMS)){ //短信
				 * remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_MESSAGE); }
				 * if(ObjectUtils.containsElement(urgency,EMAIL)){ //邮件方式
				 * remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL); }
				 * if(ObjectUtils.containsElement(urgency, RTX)){//RTX
				 * remindType.add(WorkflowConst.WORKFLOW_NOTICE_TYPE_RTX); }
				 */
				if (StringUtils.hasLength(instanceId)) {
					/*
					 * Object[] toSelectItems = {"taskId"}; List sItems =
					 * Arrays.asList(toSelectItems); Map paramsMap = new HashMap<String,
					 * Object>(); paramsMap.put("processInstanceId",
					 * instanceId); paramsMap.put("taskNodeId", nodeId);
					 * paramsMap.put("taskType","2"); Map orderMap = new HashMap<Object,
					 * Object>(); orderMap.put("taskStartDate", "1"); List
					 * list=getManager().getTaskInfosByConditionForList(sItems,
					 * paramsMap, orderMap, null, null, "", null);
					 * if(list!=null&&list.size()>0){
					 */
					if (!StringUtils.hasLength(handlerMes)) {
						handlerMes = "请尽快处理你的待处理事务!";
					}
					// 将提醒(方式|内容)存储在session中
					ActionContext cxt = ActionContext.getContext();
					cxt.getSession().put("remindType", urgencytype);
					cxt.getSession().put("handlerMes", handlerMes);
					cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
					getManager().urgencyTaskInstanceByPerson(
							taskId,
							null,
							remindType,
							handlerMes,
							new OALogInfo("催办任务,催办方式：" + urgencytype + ";催办模块:"
									+ getModuleType() + ";催办内容:" + handlerMes));
					ret = "0";
					/*
					 * }else{ ret = "-3"; }
					 */
				} else {// 流程实例不存在
					ret = "-1";
				}
			} else {
				throw new SystemException("催办类型参数传输错误！");
			}
		} catch (Exception ex) {
			ret = "-2";
			ex.printStackTrace();
			logger.error("催办失败,异常信息：" + ex.getMessage());
		}
		return this.renderText(ret);
	}

	/*
	 * 
	 * Description:流程监控模块的催办功能，判断是否为当前任务 param: @author 彭小青 @date Mar 22, 2010
	 * 10:38:42 AM
	 */
	public String isCurrentTask() throws Exception {
		String[] toSelectItems = { "taskId" };
		List<String> sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processInstanceId", instanceId);
		paramsMap.put("taskNodeId", nodeId);
		paramsMap.put("taskType", "2");
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("taskStartDate", "1");
		List list = getManager().getTaskInfosByConditionForList(sItems,
				paramsMap, orderMap, null, null, null, null);
		if (list != null && list.size() > 0) {
			renderText(list.get(0).toString());
		} else {
			renderText("");
		}
		return null;
	}

	/**
	 * 异步得到当前流程所处环节任务ID 多个任务id以逗号组成的字符串返回，若无任务id则直接返回空.
	 * 
	 * @author:邓志城
	 * @date:2010-10-20 下午03:11:42
	 * @return
	 */
	public String findCurrentTask() {
		StringBuilder taskIdStr = new StringBuilder();
		try {
			List<String> toSelectTtems = new ArrayList<String>(1);
			toSelectTtems.add("taskId");
			Map<String, Object> params = new HashMap<String, Object>(3);
			params.put("processInstanceId", instanceId);
			params.put("taskNodeId", nodeId);
			params.put("taskType", "2");
			List list = getManager().getTaskInfosByConditionForList(
					toSelectTtems, params, null, null, null, null, null);
			if (list != null && !list.isEmpty()) {
				/*
				 * String taskStr =
				 * org.apache.commons.lang.StringUtils.join(list, ",");
				 * System.out.println("taskStr:" + taskStr);
				 */
				for (int i = 0; i < list.size(); i++) {
					Object objTaskId = list.get(i);
					if (objTaskId != null) {
						taskIdStr.append(objTaskId).append(",");
					}
				}
				if (taskIdStr.length() > 0) {
					taskIdStr = taskIdStr.deleteCharAt(taskIdStr.length() - 1);
				}
			}
		} catch (Exception e) {
			logger.error("得到当前所处节点的任务时发生异常", e);
			taskIdStr.append(e.toString());
		}
		logger.info("当前任务节点id为：" + taskIdStr.toString());
		return this.renderText(taskIdStr.toString());
	}

	/**
	 * 查看主办任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 下午01:15:11
	 * @return 主办任务页面
	 * @throws Exception
	 * @modify yanjian 2011-10-24 解决委派显示异常问题
	 */
	public String viewHostedBy() throws Exception {
		try {
			/*
			 * 导致 我的请求、公文处理模块的已拟公文等多个模块无法正常工作 String[] strBusinessId =
			 * getManager().getFormIdAndBussinessIdByTaskId(taskId); formId =
			 * strBusinessId[1]; if(!"0".equals(formId)){ String[]
			 * strBussinessId = strBusinessId[0].split(";"); tableName =
			 * strBussinessId[0]; pkFieldName = strBussinessId[1]; pkFieldValue =
			 * strBussinessId[2]; }
			 */
			if (taskId != null && !taskId.equals("")) {
				String[] strBusinessId = getManager()
						.getFormIdAndBussinessIdByTaskId(taskId);
				formId = strBusinessId[1];
				if (!"0".equals(formId)) {
					String[] strBussinessId = strBusinessId[0].split(";");
					tableName = strBussinessId[0];
					pkFieldName = strBussinessId[1];
					pkFieldValue = strBussinessId[2];
				}
			} else {
				List<Object[]> list = getManager()
						.getFormIdAndBusinessIdByProcessInstanceId(instanceId);
				if (list != null) {
					Object[] objs = null;
//					if (list.size() == 1) {
//						objs = list.get(list.size()-1);
//					}
//					if (list.size() == 2) {
//						objs = list.get(1);
//					}
					if(list.size() > 0){
						objs = list.get(list.size()-1);
					}
					if (objs != null) {
						Object objFormId = objs[0].toString();
						if (objFormId != null) {
							formId = String.valueOf(objFormId);
							if (formId.indexOf(",") != -1) {
								formId = formId.split(",")[1];
							}
						}
						if (!"0".equals(formId)) {
							String[] strBussinessId = objs[1].toString().split(
									";");
							tableName = strBussinessId[0];
							pkFieldName = strBussinessId[1];
							pkFieldValue = strBussinessId[2];
						}
					}
				}
			}
			bussinessId = tableName + ";" + pkFieldName + ";" + pkFieldValue;
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if(jsonArray != null && !jsonArray.isEmpty()){
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			}else{
				ProcessInstance pi = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId);
				String demo = (String) pi.getContextInstance().getVariable("@{personDemo}");
				String parentinstanceId="";
				if(!"".equals(demo)&&null!=demo){
					String[]  demos = demo.split(";");
					if(demos.length>4){
						parentinstanceId = demos[4];
						parentinstanceId = parentinstanceId.split("@")[0];
						jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
						personDemo = jsonArray.toString();
						parentInstanceId = "exists";
					}					
				}
			}
//			ContextInstance cxt = adapterBaseWorkflowManager
//					.getWorkflowService().getContextInstance(instanceId);
//			Object tempObject = cxt
//					.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);// 获取父流程实例id
//			if (tempObject != null && !tempObject.toString().equals("")) {
//				parentInstanceId = tempObject.toString();
//				ContextInstance parentCxt = adapterBaseWorkflowManager
//						.getWorkflowService().getContextInstance(
//								parentInstanceId);// 获取父流程上下文
//				personDemo = (String) parentCxt.getVariable("@{personDemo}");
//			}
		} catch (Exception e) {
			logger.error("查看主办流程时发生错误", e);
		}
		return "hostedbyview";
	}

	/**
	 * 查看已办任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-18 下午01:56:13
	 * @return
	 * @throws Exception
	 * @modify niwy
	 * @date 2014年3月8日10:35:43
	 */
	@SuppressWarnings("unchecked")
	public String viewProcessed() throws Exception {
		try {
			//获取是否是转发邮件而来
			String sendMail=(String)getRequest().getAttribute("sendMail");
			Boolean isSuspended=false;
			Boolean isCancelled=false;
			if("true".equals(sendMail)){
				getRequest().setAttribute("sendMailfalg", "sendMail");//判断是否是转发邮件
				if(!"null".equals(instanceId)&& !"".equals(instanceId)&&instanceId!=null){
					ProcessInstance processInstance = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId);
					if(processInstance != null){
						isSuspended = processInstance.isSuspended();
					}else{
						isCancelled = true;
					}
				}else{
					//一开始是流程草稿时发送的邮件
					//判断这个流程是不是删除了
					//根据tableName, pkFieldName, pkFieldValue获取业务表的数据的workflowstate字段的值，如果值为0或者null标识该数据还是草稿，否则，就是已经走流程了
					String  workflowstates=null;
					workflowstates=adapterBaseWorkflowManager.getWorkflowService().getDraf(tableName, pkFieldName, pkFieldValue);
					if(workflowstates!=null){//如果是草稿
						if("0".equals(workflowstates)){//还是流程草稿
						}else{
							//已经提交了流程
							List<Long>  Ids=adapterBaseWorkflowManager.getWorkflowService().getIds(tableName, pkFieldName, pkFieldValue);
							if(Ids == null || Ids.isEmpty()){//如果ids为空，说明在回收站被删除了
								isCancelled = true;
							}else{
								List<Long>  taskIds=adapterBaseWorkflowManager.getWorkflowService().getTaskId(tableName, pkFieldName, pkFieldValue);
								if(taskIds != null && !taskIds.isEmpty()){
									isSuspended = true;
								}
							}
						}
						
					}else{
						isSuspended = true;
					}
				}
			}
			if(isSuspended || isCancelled){
				String text = "流程已取消或废除，无法查看。";
				StringBuilder javascript = new StringBuilder();
				javascript.append("<html><head>");
				javascript.append("<Script language=\"JavaScript\">");
				javascript.append("alert('" + text + "')");
				javascript.append(";window.opener=null;window.open('','_self' ,'');window.close();</Script>");
				javascript.append("</head></html>");
				return renderHtml(javascript.toString());
			}
		 if(taskId!=null&&!"null".equals(taskId)){	
			String suporgcode = userService.getCurrentUser().getSupOrgCode();
			getRequest().setAttribute("suporgcode", suporgcode);
			
			// 判断流程主办人员是否是当前用户
			String curUserId = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser().getUserId();
			flag = adapterBaseWorkflowManager.getWorkflowService()
					.hasMainDoing(curUserId, taskId)
					+ "";

			String[] strBusinessId = getManager()
					.getFormIdAndBussinessIdByTaskId(taskId);
			TwfBaseNodesetting nodesetting = adapterBaseWorkflowManager
					.getWorkflowService().findFirstNodeSetting(taskId,
							workflowName);
			adapterBaseWorkflowManager.getSendDocUploadManager()
					.initProcessedByNodeSetting(nodesetting);
			nodeId = StringUtil.castString(nodesetting.getNsNodeId());
			formId = strBusinessId[1];
			if (!"0".equals(formId)) {
				String[] strBussinessId = strBusinessId[0].split(";");
				tableName = strBussinessId[0];
				pkFieldName = strBussinessId[1];
				pkFieldValue = strBussinessId[2];
			}
			ProcessInstance processinstance = adapterBaseWorkflowManager
					.getWorkflowService().getProcessInstanceById(instanceId);			
			businessName = processinstance.getBusinessName();
			workflowName = processinstance.getName();
			if (processinstance.getEnd() != null) {
				state = "1";
			} else {
				state = "0";
			}
			bussinessId = tableName + ";" + pkFieldName + ";" + pkFieldValue;
			String businessType = adapterBaseWorkflowManager.getJbpmbusinessmanager().findByBusinessId(bussinessId).getBusinessType();
			getRequest().setAttribute("businessType", businessType);
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if(jsonArray != null && !jsonArray.isEmpty()){
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			}else{
				ProcessInstance pi = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId);
				String demo = (String) pi.getContextInstance().getVariable("@{personDemo}");
				String parentinstanceId="";
				if(!"".equals(demo)&&null!=demo){
					String[]  demos = demo.split(";");
					if(demos.length>4){
						parentinstanceId = demos[4];
						parentinstanceId = parentinstanceId.split("@")[0];
						
						jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
						personDemo = jsonArray.toString();
						parentInstanceId = "exists";
					}					
				}
			}
//			ContextInstance cxt = adapterBaseWorkflowManager
//					.getWorkflowService().getContextInstance(instanceId);
//			Object tempObject = cxt
//					.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);// 获取父流程实例id
//			if (tempObject != null && !tempObject.toString().equals("")) {
//				parentInstanceId = tempObject.toString();
//				ContextInstance parentCxt = adapterBaseWorkflowManager
//						.getWorkflowService().getContextInstance(
//								parentInstanceId);// 获取父流程上下文
//				personDemo = (String) parentCxt.getVariable("@{personDemo}");
//			}
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
		 }else{
			 String suporgcode = userService.getCurrentUser().getSupOrgCode();
				getRequest().setAttribute("suporgcode", suporgcode);
			 bussinessId = tableName + ";" + pkFieldName + ";" + pkFieldValue;
			 String businessType = adapterBaseWorkflowManager.getJbpmbusinessmanager().findByBusinessId(bussinessId).getBusinessType();
				getRequest().setAttribute("businessType", businessType);
				getRequest().setAttribute("isExsitTodo", "0");
		 }

		} catch (Exception e) {
			logger.error("查看主办流程时发生错误", e);
		}
		//关联表单查看  没有按钮操作  tj
		String tempShow = getRequest().getParameter("tempShow");
		if("1".equals(tempShow)){
			return "processedviewTempShow";
		}
		return "processedview";
	}
	
	/**
	 * 根据流程实例id获取父流程数据信息，适用于查看原表单的操作
	 * 
	 * @author 严建
	 * @param instanceId	流程实例id
	 * @return
	 * @createTime Jul 20, 2012 2:16:23 PM
	 */
	protected JSONArray getParentInstanceIdLevelInfo(String instanceId) {
		JSONArray jsonArray = new JSONArray();
		List parentInstanceList = adapterBaseWorkflowManager
				.getWorkflowService().getMonitorParentInstanceIds(
						new Long(instanceId));
		if (parentInstanceList != null && !parentInstanceList.isEmpty()) {
			int parentInstanceListSize = parentInstanceList.size();
			for (int i = 0; i < parentInstanceListSize; i++) {
				String tempPID = ((Object[]) parentInstanceList.get(i))[0]
						.toString();
				String tempParentPID = "-1";// 父流程实例id
				if (i < parentInstanceListSize - 1) {
					tempParentPID = ((Object[]) parentInstanceList.get(i + 1))[0]
							.toString();
				}
				ProcessInstance temppi = adapterBaseWorkflowManager
						.getWorkflowService().getProcessInstanceId(tempPID);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("instanceId", temppi.getId()); // 流程实例id
				jsonObject.put("parentInstanceId", tempParentPID); // 父流程实例id，“-1”表示不存在
				jsonObject.put("businessId", temppi.getBusinessId()); // 流程业务id
				jsonObject.put("mainFormId", temppi.getMainFormId()); // 流程实例业务表单id
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}
	
	/**
	 * author:luosy description: 查看指派任务 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewAssign() throws Exception {
		String[] strBussinessId = bussinessId.split(";");
		tableName = strBussinessId[0];
		pkFieldName = strBussinessId[1];
		pkFieldValue = strBussinessId[2];
		return "assignview";
	}

	/**
	 * author:luosy description: 查看委派任务 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewEntrust() throws Exception {
		String[] strBussinessId = bussinessId.split(";");
		tableName = strBussinessId[0];
		pkFieldName = strBussinessId[1];
		pkFieldValue = strBussinessId[2];
		return "entrustview";
	}

	/**
	 * 取回已办任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 下午02:22:37
	 * @return 1、流程实例已结束 0； 2、任务已被签收处理1； 3、任务取回成功2； 4、任务实例不存在返回-1 5、抛出异常返回-2
	 * @throws Exception
	 */
	public String fetchTask() throws Exception {
		String ret = "0";
		try {
			if (StringUtils.hasLength(taskId)) {
				ret = getManager().fetchTask(taskId,
						new OALogInfo("取回任务，taskId=" + taskId));
			} else {
				ret = "-1";
			}
		} catch (Exception ex) {
			logger.error("任务取回失败,异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}

	/**
	 * 获取节点信息，主要用于办公厅分发公文取回时更改分发单位信息
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2012-12-18 上午08:41:50
	 * @version :5.0
	 */
	public String isdjfb() throws Exception{
		String ret = "0";
		if (StringUtils.hasLength(taskId)) {
			ret = getManager().gettaskInf(taskId,
					new OALogInfo("取回任务，taskId=" + taskId));
			if(ret!=null && ret.equals("公文分发")){
				if(bussinessId==null||bussinessId.equals("undefind")||bussinessId=="undefind"){
					List<Object[]> rets = sendDocManager.getFormIdAndBusinessIdByProcessInstanceId(String.valueOf(instanceId));
					if(rets!=null&&rets.size()>0){
						Object[] obj = rets.get(0);
						bussinessId = obj[1].toString();		//表名;主键;主键值
						if(bussinessId.indexOf(";")==-1){
							bussinessId="T_OARECVDOC"+";"+"OARECVDOCID"+";"+bussinessId;
						}
					}
				}
				sendDocManager.updateRegdocDate(bussinessId,"","",null,"0");
			}
		}
		return this.renderText(ret);
	}
	
	/**
	 * 判断目前任务是否可被当前用户处理
	 * 
	 * @author:邓志城
	 * @date:2009-12-17 下午04:00:25
	 * @return 1、f1|该任务正在被其他人处理或被挂起 2、f2|该任务已被取消 3、f3|该任务已被其他人处理 4、f4|可被处理
	 *         5、任务实例不存在返回-1 6、抛出异常返回-2
	 * @throws Exception
	 */
	public String judgeTaskIsDone() throws Exception {
		String ret = "0";
		try {
			if (StringUtils.hasLength(taskId)) {
				ret = getManager().judgeTaskIsDone(taskId);
			} else {
				ret = "-1";
			}
		} catch (Exception ex) {
			logger.error("任务取回失败,异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}

	/**
	 * 展现可选人员
	 * 
	 * @return
	 */
	@Deprecated
	public String taskActors() throws Exception {
		if (StringUtils.hasLength(taskId)) {
			taskActors = getManager().getWorkflowTaskActors(nodeId, taskId,
					transitionId);
		} else {
			taskActors = getManager().getWorkflowTaskActors(nodeId, "0",
					transitionId);
		}
		return "taskActors";
	}

	/**
	 * 得到当前用户的符合查询条件的委托（指派）任务信息或被委托（指派）任务信息分页列表
	 * 
	 * @author:邓志城
	 * @date:2010-1-27 下午04:14:32
	 * @return
	 * @throws Exception
	 */
	public String getAssignTaskInfoByUserId() throws Exception {
		pageWorkflow = getManager().getAssignTaskInfoByUserId(
				pageWorkflow,
				adapterBaseWorkflowManager.getUserService().getCurrentUser()
						.getUserId(), type, assignType, initiativeType,
				workflowType, businessName, userName, startDate, endDate);
		return null;
	}

	/**
	 * 得到JSON格式的签发意见
	 * 
	 * @author:邓志城
	 * @date:2009-12-21 下午07:03:57
	 * @return
	 */
	public String getInstro() throws Exception {
		String infomation = "";
		if (taskId != null && !"".equals(taskId) && !"undefined".equals(taskId)) {// 根据任务获取处理意见
			infomation = getManager().getInstro(taskId, instanceId);
		} else {// 在主办任务中查看办理意见
			if (instanceId != null && !instanceId.equals("")
					&& !"undefined".equals(instanceId)) {
				infomation = getManager().getBusiFlagByProcessInstanceId(
						instanceId);
			} else {
				if (workflowName != null && !"".equals(workflowName)
						&& !"undefined".equals(workflowName)) {
					workflowName = URLDecoder.decode(workflowName, "UTF-8");
					infomation = getManager().getBusiFlagByWorkflowName(
							workflowName);
				}
			}
		}
		logger.info(infomation);
		if (infomation == null) {
			infomation = "";
		}
		return this.renderText(infomation);
	}

	/**
	 * 只得到JSON格式签发意见，不返回其他域设置信息。
	 * 
	 * @author:邓志城
	 * @date:2010-1-21 下午07:59:01
	 * @return
	 * @throws Exception
	 */
	public String onlyGetInstro() throws Exception {
		try {
			String infomation = getManager().getBusiFlagByProcessInstanceId(
					instanceId);
			logger.info(infomation);
			return this.renderText(infomation);
		} catch (Exception e) {
			logger.error("根据流程实例id得到审批意见时出错。", e);
			throw e;
		}
	}

	/**
	 * 得到指定模板的所有组件（组件名称，组件类型）
	 * 
	 * @author:邓志城
	 * @date:2010-1-26 下午04:39:20
	 * @return
	 * @throws Exception
	 */
	public String getFormComponent() throws Exception {
		String info = getManager().getFormComponent(formId, null);
		logger.info(info);
		return this.renderText(info);
	}

	/**
	 * 数据复制
	 * 
	 * @author:邓志城
	 * @date:2010-3-19 下午02:17:56
	 * @throws Exception
	 */
	public InputStream copyData() throws Exception {
		InputStream responseData = null;
		try {
			VoFormDataBean bean = adapterBaseWorkflowManager.getEformService()
					.saveFormData(formData);
			bussinessId = bean.getBusinessId();
			if(this.getRequest().getParameter("newId") != null){
				newFormId = this.getRequest().getParameter("newId");				
			}
			instanceId = this.getRequest().getParameter("instanceId");
			String result = getManager().copyData(bussinessId, newFormId,
					new OALogInfo("发起新流程，数据复制。"));
			result = result+";"+instanceId;
			bean.deleteFile();
			responseData = new ByteArrayInputStream(result.toString().getBytes(
					"utf-8"));
		} catch (Exception e) {
			logger.error("发起新流程，数据复制出现异常。", e);
			responseData = new ByteArrayInputStream("-1".toString().getBytes(
					"utf-8"));
		}
		return responseData;
	}

	/**
	 * 发起新流程转到表单页面
	 * 
	 * @author:邓志城
	 * @date:2010-3-19 下午02:41:08
	 * @return
	 * @throws Exception
	 */
	public String newForm() throws Exception {
		String[] args = bussinessId.split(";");
		logger.info("新流程业务数据：" + bussinessId);
		tableName = args[0];
		pkFieldName = args[1];
		pkFieldValue = args[2];
		return "newform";
	}

	/**
	 * 转到发起新流程向导页面
	 * 
	 * @author:邓志城
	 * @date:2010-3-23 下午04:36:28
	 * @return
	 * @throws Exception
	 */
	public String newWizard() throws Exception {
		// 获取审批意见列表
		ideaLst = getManager().getCurrentUserDictItem(getDictType());
		// 得到发文字典类主键
		ToaSysmanageDict dict = getManager().getDict(getDictType());
		if (dict != null) {
			dictId = dict.getDictCode();
		}
		return "newwizard";
	}

	/**
	 * 在列表中通过任务ID得到处理意见列表
	 * 
	 * @author Administrator蒋国斌 StrongOA2.0_DEV 2010-3-24 下午03:23:11
	 * @return
	 * @throws Exception
	 */
	public String annal() throws Exception {
		if (taskId != null && !"".equals(taskId)) {
			listAnnal = getManager().getProcessHandleByTaskInstanceId(taskId);
			if (listAnnal != null && !listAnnal.isEmpty()) {
				CAAuth ca = new CAAuth();
				for (int i = 0; i < listAnnal.size(); i++) {
					Object[] objs = listAnnal.get(i);
					Object signInfo = objs[7];
					Object[] objs1 = { objs[0], objs[1], objs[2], objs[3],
							objs[4], objs[5], objs[6], objs[7], "" };
					String allAiContent = "";
					if (objs[5] == null) {
						allAiContent = "";
					} else {
						allAiContent = objs[5].toString();
					}
					if ("".equals(allAiContent)) {
						objs1[5] = "";
						objs1[8] = allAiContent;
					} else {
						/*
						 * if(allAiContent.length() > 30){ objs1[5] =
						 * allAiContent.substring(0,30) + "..."; objs1[8] =
						 * allAiContent; }else{ objs1[5] = allAiContent;
						 * objs1[8] = allAiContent; }
						 */
						objs1[5] = allAiContent;
						objs1[8] = allAiContent;
					}
					if (signInfo != null) {
						if (!"".equals(signInfo.toString())) {
							signInfo = ca.getSignInfo(signInfo.toString());
							String[] strSignInfo = signInfo.toString().split(
									",");
							signInfo = strSignInfo[0];
							objs[7] = signInfo;
							listAnnal.set(i, objs1);
						}
					}
					listAnnal.set(i, objs1);
				}
			}
		}
		if (listAnnal == null || listAnnal.isEmpty()) {
			if (instanceId != null && !"".equals(instanceId)) {
				List<TwfInfoApproveinfo> infoLst = getManager()
						.getApproveInfosByPIId(instanceId);
				if (infoLst != null && !infoLst.isEmpty()) {
					CAAuth ca = new CAAuth();
					for (TwfInfoApproveinfo info : infoLst) {
						String signInfo = ca.getSignInfo(info.getAiDigital());
						String[] strSignInfo = signInfo.toString().split(",");
						signInfo = strSignInfo[0];

						// 开始
						String allAiContent = "";
						String subAiContent = "";
						allAiContent = info.getAiContent();
						if (allAiContent == null) {
							subAiContent = "";
						} else {
							/*
							 * if(allAiContent.length() > 30){ subAiContent =
							 * allAiContent.substring(0, 30) + "..."; }else{
							 * subAiContent = allAiContent; }
							 */
							subAiContent = allAiContent;
						}
						// 结束
						listAnnal.add(new Object[] { info.getAiTaskId(),
								info.getAiTaskname(), null, null,
								info.getAiActor(), subAiContent,
								info.getAiDate(), signInfo, allAiContent });
					}
				}
			}
		}
		return "annallist";
	}

	public void getParentId() throws Exception {
		try {
			List parentInstanceIds = adapterBaseWorkflowManager
					.getWorkflowService().getMonitorParentInstanceIds(
							new Long(instanceId));
			String parentId = "";
			if (parentInstanceIds != null && parentInstanceIds.size() > 0) {
				parentId = parentInstanceIds.get(0).toString();
			} else
				parentId = "0";
			this.renderText(parentId);// 返回业务数据
		} catch (Exception e) {
			logger.error("数据异常，找不到父级工作流程实例。", e);
			this.renderText("-1");
		}
	}

	/**
	 * 根据任务ID得到展现表单
	 * 
	 * @author Administrator蒋国斌 StrongOA2.0_DEV 2010-3-24 下午03:24:02
	 * @return
	 * @throws Exception
	 */
	public String viewform() throws Exception {
		String[] info = null;
		if (taskId != null && !"".equals(taskId)) {
			// 通过任务ID获取表单ID和业务ID,业务ID用于获取公文对象
			info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
		}
		if (info == null) {
			if (instanceId != null && !"".equals(instanceId)) {
				Object[] objs = null;
				if (nodeId != null && !"".equals(nodeId)) {
					objs = getManager().getFormIdAndBusiIdByPiIdAndNodeId(
							instanceId, nodeId);
					info = new String[] { (String) objs[2], objs[0].toString() };
				} else {
					objs = getManager()
							.getFormIdAndBusinessIdByProcessInstanceId(
									instanceId).get(0);
					info = new String[] { objs[1].toString(),
							objs[0].toString() };
				}
				JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
				if(jsonArray != null && !jsonArray.isEmpty()){
					personDemo = jsonArray.toString();
					parentInstanceId = "exists";
				}else{
					ProcessInstance pi = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId);
					String demo = (String) pi.getContextInstance().getVariable("@{personDemo}");
					String parentinstanceId="";
					if(!"".equals(demo)&&null!=demo){
						String[]  demos = demo.split(";");
						if(demos.length>4){
							parentinstanceId = demos[4];
							parentinstanceId = parentinstanceId.split("@")[0];
							
							jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
							personDemo = jsonArray.toString();
							parentInstanceId = "exists";
						}					
					}
				}
			}
		}
		if (info == null) {// 直接传递值过来
			info = new String[] { bussinessId, formId };
		}
		bussinessId = info[0];
		formId = info[1];
		if (!"0".equals(bussinessId) && bussinessId.indexOf(";") != -1) {
			String[] args = bussinessId.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			pkFieldValue = args[2];
		}
		return "viewform";
	}

	/**
	 * 根据实例ID得到展现表单
	 * 
	 * @author Administrator蒋国斌 StrongOA2.0_DEV 2010-3-24 下午03:24:45
	 * @return
	 * @throws Exception
	 */
	public String viewHostedByForm() throws Exception {
		splitBussinessId();
		return "viewHostedByForm";
	}

	/**
	 * 处理分割bussinessId并且给tableName、pkFieldName、pkFieldValue赋值
	 * 
	 * @description
	 * @author 严建
	 * @createTime May 8, 2012 1:42:52 PM
	 */
	protected void splitBussinessId() {
		String[] strBussinessId = bussinessId.split(";");
		tableName = strBussinessId[0];
		pkFieldName = strBussinessId[1];
		pkFieldValue = strBussinessId[2];
	}

	/**
	 * 根据节点id得到节点信息
	 * 
	 * @author:邓志城
	 * @date:2010-10-9 下午01:46:32
	 */
	public void getNodeSettingInfo() {
		JSONObject info = new JSONObject();
		try {
			if (taskId != null && !"".equals(taskId)
					&& !"undefined".equals(taskId)) {
				String nodeId = adapterBaseWorkflowManager.getWorkflowService()
						.getNodeIdByTaskInstanceId(taskId);
				Object[] setting = getManager().getNodesettingByNodeId(nodeId);
				info.put("maxTaskActors", setting[0]);
				info.put("isSelectOtherActors", setting[1]);
				info.put("returnTaskActor", setting[2]);
			} else {
				info.put("returnTaskActor", "0");
			}
		} catch (Exception e) {
			logger.error("读取节点设置信息发生异常。", e);
			info.put("returnTaskActor", "0");
		}
		this.renderText(info.toString());
		logger.error("当前节点设置信息：" + info.toString());
	}

	/**
	 * 工作流选择人员
	 * 
	 * @author:邓志城
	 * @date:2010-3-25 下午09:22:02
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String chooseForWorkflow() throws Exception {
		long sart = System.currentTimeMillis();
		List<String[]> taskActors = null;
		String inputType = "checkbox";
		if (nodeId != null && !"".equals(nodeId) && !"null".equals(nodeId)) {
			taskActors = getManager().getWorkflowTaskActors(nodeId, taskId,
					transitionId);
			if (taskActors != null && !taskActors.isEmpty()) {
				for (String[] info : taskActors) {
					String actorUserId = info[0];
					if(actorUserId!=null&&!"".equals(actorUserId)){
						User user = userService.getUserInfoByUserId(actorUserId);
						info[2] = user.getOrgId();
					}
				}
			}
			if (maxTaskActors == null || "".equals(maxTaskActors)
					|| "null".equals(maxTaskActors)) {
				Object[] setting = getManager().getNodesettingByNodeId(nodeId);
				this.maxTaskActors = (String) setting[0];
				this.isSelectOtherActors = (String) setting[1];
			}
			if (maxTaskActors != null && !maxTaskActors.equals("")
					&& maxTaskActors.equals("1")) {
				inputType = "radio";
			}
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

//					List<TUumsBasePost> userPostSet = userService
//							.getPostInfoByUserId(info[0].toString());//找出该人员岗位信息
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
//						List<TUumsBasePost> userPostSet = userService
//								.getPostInfoByUserId(info[0].toString());//找出该人员岗位信息
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
			List<Object[]> objs = orgidusersmap.get(orgLst.get(i)[0]);
			if(objs != null){
				users.addAll(orgidusersmap.get(orgLst.get(i)[0]));
			}
		}
		getRequest().setAttribute("orgList", orgLst);
		getRequest().setAttribute("userList", users);
		getRequest().setAttribute("inputType", inputType);
		getRequest().setAttribute("isOrganizationSelect", isOrganizationSelect);
		if (inputType.equals("radio")) {
			List<TempfileTree> treeList = new ArrayList<TempfileTree>();
			List<TempfileTree> treeOrgList = new ArrayList<TempfileTree>();
			if (users != null && users.size() > 0) {
				TempfileTree tree = new TempfileTree();
				tree.setTreeid("0");
				tree.setTreeName(nodeName);
				tree.setTreetype("0");
				tree.setParentId("-1");
				treeList.add(tree);
				for (Object[] obj : users) {
					TempfileTree tree1 = new TempfileTree();
					tree1.setTreeid(obj[0].toString());
					tree1.setTreeName(obj[1].toString() + "[" + obj[2].toString() + "]");
					tree1.setTreetype("1");
					tree1.setParentId("0");
					treeList.add(tree1);
				}
			}
			if (orgLst != null && orgLst.size() > 0) {
				for (Object[] obj : orgLst) {
					TempfileTree tree = new TempfileTree();
					tree.setTreeid(obj[0].toString());
					tree.setTreeName(obj[2].toString());
					tree.setTreetype("0");
					tree.setParentId(obj[1].toString());
					treeOrgList.add(tree);
					List<Object[]> orgUsrsList = (List<Object[]>) obj[3];
					if (orgUsrsList != null && orgUsrsList.size() > 0) {
						for (Object[] obj1 : orgUsrsList) {
							String postName = userIdMappostName.get(obj1[0].toString());
							TempfileTree tree1 = new TempfileTree();
							tree1.setTreeid(obj1[0].toString());
							tree1.setTreeName(obj1[1].toString() + ""
									+ postName);
							tree1.setTreetype("1");
							tree1.setParentId(obj[0].toString());
							treeOrgList.add(tree1);
						}
					}
				}
			}
			long end =  System.currentTimeMillis() - sart;
			getRequest().setAttribute("treeList", treeList);
			getRequest().setAttribute("treeOrgList", treeOrgList);
			return "singlechoosetree";
		} else {

			return "workflow";
		}
	}

	public void findTemplateByFormId() {
		String templateId = adapterBaseWorkflowManager
				.getTemplateEFormManager().findLatestTemplate(formId);
		if (templateId != null) {
			this.renderText(templateId);
		} else {
			this.renderText("notconfig");
		}
	}

	/**
	 * 兼容2003 - 2007 打开空文档。
	 * 
	 * @author:邓志城
	 * @date:2010-4-13 上午11:49:00
	 */
	public void openEmptyDocFromUrl() {
		HttpServletResponse response = this.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		OutputStream output = null;
		String type = ".doc";
		logger.info("文档类型：" + docType);
		try {
			String templateId = null;// ;templateEFormManager.findLatestTemplate(formId);
			output = response.getOutputStream();
			if (templateId == null) {
				if ("1".equals(docType)) {
					type = ".doc";
				} else if ("2".equals(docType)) {
					type = ".xls";
				} else if ("3".equals(docType)) {
					type = ".ppt";
				} else if ("4".equals(docType)) {
					type = ".vsd";
				} else if ("5".equals(docType)) {
					type = ".mpp";
				} else if ("6".equals(docType)) {
					type = ".wps";
				}
				String rootPath = getRequest().getSession().getServletContext()
						.getRealPath("/empty" + type);
				File file = new File(rootPath);
				if (!file.exists()) {
					rootPath = getRequest().getSession().getServletContext()
							.getRealPath("/empty.doc");
					file = new File(rootPath);
				}
				logger.info("打开文档:" + rootPath);
				byte[] buf = FileUtil
						.inputstream2ByteArray(new FileInputStream(file));
				output.write(buf);
			} else {
				ToaDoctemplate template = adapterBaseWorkflowManager
						.getDocTempItemManager().getDoctemplate(templateId);
				if (template != null) {
					byte[] buf = template.getDoctemplateContent();
					output.write(buf);
				} else {
					if ("1".equals(docType)) {
						type = ".doc";
					} else if ("2".equals(docType)) {
						type = ".xls";
					} else if ("3".equals(docType)) {
						type = ".ppt";
					} else if ("4".equals(docType)) {
						type = ".vsd";
					} else if ("5".equals(docType)) {
						type = ".mpp";
					} else if ("6".equals(docType)) {
						type = ".wps";
					}
					String rootPath = getRequest().getSession()
							.getServletContext().getRealPath("/empty" + type);
					output = response.getOutputStream();
					File file = new File(rootPath);
					if (!file.exists()) {
						rootPath = getRequest().getSession()
								.getServletContext().getRealPath("/empty.doc");
						file = new File(rootPath);
					}
					logger.info("打开文档:" + rootPath);
					byte[] buf = FileUtil
							.inputstream2ByteArray(new FileInputStream(file));
					output.write(buf);
				}
			}
		} catch (Exception e) {
			logger.error("打开文档出错了。", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	/**
	 * 读取标签与表单模板的映射关系.
	 * 
	 * @author:邓志城
	 * @date:2010-7-11 下午10:49:12
	 * @return
	 */
	public String readBookMarkInfo() {
		try {
			Assert.notNull(formId, "表单id不能为空。");
			List list = adapterBaseWorkflowManager.getBookMarkService()
					.getBookMarkList(formId);
			JSONArray array = new JSONArray();
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					JSONObject obj = new JSONObject();
					obj.put("bookMarkName", objs[1]); // 标签名称
					obj.put("componentName", objs[2]); // 控件名称
					obj.put("fieldName", objs[3]); // 控件绑定的字段名称
					obj.put("tableName", objs[4]); // 控件绑定的表名称
					array.add(obj);
				}
			}
			return this.renderText(array.toString());
		} catch (Exception e) {
			logger.error("读取标签与电子表单映射时异常", e);
			return this.renderText("-1");
		}
	}

	/**
	 * 保存公文草稿.
	 * 
	 * @author:邓志城
	 * @date:2010-9-10 下午06:03:20
	 * @return 操作结果.
	 */
	public InputStream saveDraft() throws DAOException, ServiceException,
			SystemException {
		InputStream is = null;
		InputStream responseData = null;
		try {
			VoFormDataBean bean = adapterBaseWorkflowManager.getEformService()
					.saveFormData(new FileInputStream(formData));
			bussinessId = bean.getBusinessId();
			String[] args = bussinessId.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			pkFieldValue = args[2];
			BaseManager manager = getManager();
			List<EFormField> fieldList = manager
					.getFormTemplateFieldList(formId);
			String fieldName = null;
			for (EFormField field : fieldList) {
				if ("Office".equals(field.getType())) {// 找到OFFICE控件类型
					fieldName = field.getFieldname();
					break;
				}
			}
			if (fieldName == null) {
				throw new SystemException("表单中不存在OFFICE控件。");
			}
			if (manager instanceof SendDocManager) {
				SendDocManager docManager = (SendDocManager) manager;
				is = (InputStream) docManager.getFieldValue(fieldName,
						tableName, pkFieldName, pkFieldValue);
			}
			if (is != null) {
				/* yanjian 保存公文附件 */
				WorkflowAttach model = null;
				if (workflowType != null && !"0".equals(workflowType)) {// 草稿id不为空
					model = adapterBaseWorkflowManager
							.getWorkflowAttachManager().get(
									workflowType.split(",")[1]);
				} else {
					model = new WorkflowAttach();
				}
				if(model == null){
					model = new WorkflowAttach();
				}
				model.setAttachName(DEFAULT_DRAFT_ATTACH_NAME);
				model.setDocId(pkFieldValue);
				model.setAttachContent(FileUtil.inputstream2ByteArray(is));
				adapterBaseWorkflowManager.getWorkflowAttachManager()
						.saveWorkflowAttach(model);
			} else {
				throw new SystemException("不是收发文类型！");
			}
			responseData = new ByteArrayInputStream(bussinessId.toString()
					.getBytes("utf-8"));
			bean.deleteFile();
			return responseData;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException("保存草稿出错：" + e.getMessage());
		}
	}

	/**
	 * 校验附件是否存在
	 * 
	 * @author:邓志城
	 * @date:2010-9-10 下午07:41:10
	 * @return 0:不存在 1：存在 -1：发生异常
	 */
	public String checkDraft() {
		String ret = "0";
		try {
			String id = getRequest().getParameter("id"); // 公文id
			String retCode = getManager().isAttachExist(id,
					DEFAULT_DRAFT_ATTACH_NAME);
			if (!"".equals(retCode)) {
				ret = retCode;
				logger.error("草稿'" + DEFAULT_DRAFT_ATTACH_NAME + "'已存在！");
			}
		} catch (Exception e) {
			logger.error("校验附件是否存在发生异常", e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

	/**
	 * 更新附件内容.
	 * 
	 * @author:邓志城
	 * @date:2010-9-10 下午07:23:19
	 * @return
	 */
	/*
	 * public String updateDraft() { String ret = ""; InputStream is = null;
	 * try{ String id = getRequest().getParameter("id");//附件id bussinessId =
	 * eformService.saveFormData(formData);//主表数据 String[] args =
	 * bussinessId.split(";"); tableName = args[0]; pkFieldName = args[1];
	 * pkFieldValue = args[2]; BaseManager manager = getManager(); List<EFormField>
	 * fieldList = manager.getFormTemplateFieldList(formId); String fieldName =
	 * null; for(EFormField field : fieldList) {
	 * if("Office".equals(field.getType())) {//找到OFFICE控件类型 fieldName =
	 * field.getFieldname(); break; } } if(fieldName == null) { throw new
	 * SystemException("表单中不存在OFFICE控件。"); } if(manager instanceof
	 * SendDocManager){ SendDocManager docManager = (SendDocManager)manager; is =
	 * (InputStream)docManager.getFieldValue(fieldName, tableName, pkFieldName,
	 * pkFieldValue); } if(is != null){ getManager().updateAttach(id, is); ret =
	 * "0"; }else{ throw new SystemException("不是收发文类型！"); } }catch(Exception e){
	 * logger.error("保存公文草稿时异常", e); ret = "-1"; } return this.renderText(ret); }
	 */

	/**
	 * 从URL打开WORD
	 * 
	 * @author:邓志城
	 * @date:2010-10-11 下午02:41:23
	 * @throws Exception
	 */
	public void openWordDocFromUrl() throws Exception {
		if (bussinessId == null || "".equals(bussinessId)) {
			logger.error("参数bussinessId为空");
			throw new SystemException("参数bussinessId为空");
		}
		InputStream is = getManager().getAttachById(bussinessId);
		if (is == null) {
			throw new SystemException("附件不存在或已删除！");
		}
		if (is != null) {
			getManager().openInputStreamToHttpResponse(
					FileUtil.inputstream2ByteArray(is));
		}
	}
	
	/**
	 * 从URL打开WORD
	 * 
	 * @author:邓志城
	 * @date:2010-10-11 下午02:41:23
	 * @throws Exception
	 */
	public void openWordDocFromUrls() throws Exception {
		if (bussinessId == null || "".equals(bussinessId)) {
			logger.error("参数bussinessId为空");
			throw new SystemException("参数bussinessId为空");
		}
		InputStream is = getManager().getAttachByIds(bussinessId,tableNames,idName);
		if (is == null) {
			throw new SystemException("附件不存在或已删除！");
		}
		if (is != null) {
			getManager().openInputStreamToHttpResponse(
					FileUtil.inputstream2ByteArray(is));
		}
	}

	/**
	 * 更新附件信息.
	 * 
	 * @author:邓志城
	 * @date:2010-10-12 上午09:45:57
	 * @return 操作结果 0：操作成功；-1：发生异常
	 */
	public String saveAttach() {
		String ret = "";
		try {
			if (bussinessId == null || "".equals(bussinessId)) {
				logger.error("参数bussinessId为空");
				throw new SystemException("参数bussinessId为空");
			}
			if (wordDoc == null) {
				throw new SystemException("读取WORD内容失败。");
			}
			getManager()
					.updateAttach(bussinessId, new FileInputStream(wordDoc));
			ret = "0";
		} catch (Exception e) {
			logger.error("保存附件信息发生异常", e);
			ret = "-1";
		}
		return this.renderText(ret);
	}
	
	/**
	 * 更新附件信息.
	 */
	public String saveAttachsT() {
		String ret = "";
		try {
			if (bussinessId == null || "".equals(bussinessId)) {
				logger.error("参数bussinessId为空");
				throw new SystemException("参数bussinessId为空");
			}
			if (wordDoc == null) {
				throw new SystemException("读取WORD内容失败。");
			}
			getManager()
					.updateAttachs(new FileInputStream(wordDoc));
			ret = "0";
		} catch (Exception e) {
			logger.error("保存附件信息发生异常", e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

	protected String renderJavaScript(String text) {
		StringBuilder javascript = new StringBuilder();
		javascript.append("<Script language=\"JavaScript\">");
		javascript.append("alert('" + text + "')");
		javascript.append(";window.close();</Script>");
		javascript.append("</head></html>");
		return renderHtml(javascript.toString());
	}

	/**
	 * 判断流程是否应用，办毕。
	 * 
	 * @author zhengzb
	 * @desc 2010-12-13 上午11:23:01
	 * @return 返回：“0”： 当前流程还没有被引用 “-1”： 系统异常， “【流程名】”：当前流程已被引用
	 */
	@SuppressWarnings("unchecked")
	public String checkWorkflowIsDel() {
		String ret = "";
		try {
			Object[] toSelectItems = { "processName", "processTypeId",
					"processTypeName", "processMainFormId", "processEndDate" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if (workflowId != null && !"".equals(workflowId)) {
				String[] arr = workflowId.split(",");
				List<Long> listId = new ArrayList<Long>();
				for (int i = 0; i < arr.length; i++) {
					TwfBaseProcessfile processfile = adapterBaseWorkflowManager
							.getWorkflowService().getTwfBaseProcessFile(arr[i]);
					if (processfile != null
							&& processfile.getPfIsDeploy() != null
							&& processfile.getPfIsDeploy().equals("1")) {
						TwfBaseWorkflow workflow = null;
						Set<TwfBaseWorkflow> set = processfile
								.getTwfBaseWorkflows();
						Iterator<TwfBaseWorkflow> iterator = set.iterator();
						if (iterator.hasNext()) {
							while (iterator.hasNext()) {
								workflow = iterator.next();
								listId.add(workflow.getWfPdid()); // 获取流程定义ID，组装成List<Long>
							}
						}
					}
				}
				if (listId != null && listId.size() > 0) {
					paramsMap.put("processDefinitionId", listId); // 流程定义ID
					// :若要同时查询多个流程定义Id，则使用包含多个Long值的List
					// paramsMap.put("processStatus", "0"); //查找待办流程
					// paramsMap.put("startUserId",
					// userService.getCurrentUser().getUserId()); //发起人是当前用户
					Map orderMap = new HashMap<Object, Object>();
					orderMap.put("processDefinitionId", "0");
					List<Object[]> list = adapterBaseWorkflowManager
							.getWorkflowService()
							.getProcessInstanceByConditionForList(sItems,
									paramsMap, orderMap);
					if (list != null && list.size() > 0) {
						for (Object[] obj : list) {
							ret = ret + "," + obj[0];
						}
						ret = ret.substring(1);
					} else {
						ret = "0";
					}
				} else {
					ret = "0";
				}
			} else {
				ret = "-1";
			}
		} catch (Exception e) {
			logger.error("判断流程是否应用办毕发生异常", e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

	/**
	 * 得到当前节点上设置的操作按钮名称
	 * 
	 * @author:邓志城
	 * @date:2011-5-20 下午04:16:24
	 */
	public void GetNextStepOperationName() {
		nodeId = adapterBaseWorkflowManager.getWorkflowService()
				.getNodeIdByTaskInstanceId(taskId);
		TwfBaseNodesetting nodeSet = adapterBaseWorkflowManager
				.getWorkflowService().getNodesettingByNodeId(nodeId);
		transitionName = nodeSet.getPlugin("plugins_buttonname");
		logger.info("当前节点上设置的操作按钮名称为：" + transitionName);
		this.renderText(transitionName);
	}

	/**
	 * 得到是否能修改审批意见
	 * 
	 * @author luosy
	 * @desc 2011-06-21
	 * @return 返回：“0”： 不能修改 “1”： 可以修改
	 */
	public void chkModifySuggestion() {
		if (taskId != null && !"".equals(taskId)) {
			nodeId = adapterBaseWorkflowManager.getWorkflowService()
					.getNodeIdByTaskInstanceId(taskId);
			TwfBaseNodesetting nodeSet = adapterBaseWorkflowManager
					.getWorkflowService().getNodesettingByNodeId(nodeId);
			String chkModifySuggestion = nodeSet
					.getPlugin("plugins_chkModifySuggestion");
			logger.info("当前节点上设置是否可以修改审批意见：" + chkModifySuggestion);
			this.renderText(chkModifySuggestion);
		} else {
			this.renderText("0");
		}
	}

	/**
	 * 在迁移线上设置是否需要读取表单数据 返回迁移线上设置的值.
	 */
	public void chkSubmitFormData() {
		try {
			TwfBaseTransitionPlugin plugin = adapterBaseWorkflowManager
					.getWorkflowService().getTransitionPluginByTsId(
							transitionId, PLUGIN_SUBMIT_FORMDATA_SET);
			if (plugin != null) {
				String value = plugin.getValue();
				if (value != null) {
					renderText(value);
				}
			}
		} catch (Exception e) {
			logger.error("校验下一节点是否需要读取表单数据时发生异常.", e);
			renderText("0");
		}
		renderText("0");
	}

	/**
	 * 下载工作流流转过程中的附件
	 * 
	 * @throws Exception
	 */
	public void downLoadAttachment() throws Exception {
		HttpServletResponse response = super.getResponse();
		response.reset();
		response.setContentType("application/x-download"); // windows
		OutputStream output = null;
		HttpServletRequest req = getRequest();
		String id = req.getParameter("id"); // 附件id
		if (id == null) {
			throw new SystemException("参数不可为空！");
		}
		WorkflowAttach attachment = adapterBaseWorkflowManager
				.getWorkflowAttachManager().get(id);
		if (attachment == null) {
			throw new SystemException("附件不存在！");
		}
		byte[] content = attachment.getAttachContent();
		if (content == null) {
			throw new SystemException("附件'" + attachment.getAttachName() + "'不存在或已删除！");
		}
		try {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(attachment.getAttachName().getBytes("gb2312"), "iso8859-1"));
			output = response.getOutputStream();
			output.write(content);
			output.flush();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
				output = null;
			}
		}
	}
	
	//下载附件
	public void downLoad() throws Exception {
		HttpServletResponse response = super.getResponse();
		response.reset();
		response.setContentType("application/x-download"); // windows
		OutputStream output = null;
		HttpServletRequest req = getRequest();
		String id = req.getParameter("id"); // 附件id
		if (id == null) {
			throw new SystemException("参数不可为空！");
		}
		//取得附件
		InputStream is = getManager().getAttachByIds(id,tableNames,idName);
		if (is == null) {
			throw new SystemException("附件不存在！");
		}
		byte[] content = FileUtil.inputstream2ByteArray(is);
		if (content == null) {
			throw new SystemException("附件'" + attName
					+ "'不存在或已删除！");
		}
		try {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(attName.getBytes("gb2312"), "iso8859-1"));
			output = response.getOutputStream();
			output.write(content);
			output.flush();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
				output = null;
			}
		}
	}
	
	/**
	 * 双击查看PDF
	 * @author: qibh
	 *@throws Exception
	 * @created: 2012-11-22 上午02:31:26
	 * @version :5.0
	 */
	public void openLoadAttachment() throws Exception {
		try {
			HttpServletResponse response = super.getResponse();
			response.reset();
			response.setContentType("application/x-download"); // windows
			HttpServletRequest req = getRequest();
			String id = req.getParameter("id"); // 附件id
			if (id == null) {
				throw new SystemException("参数不可为空！");
			}
			WorkflowAttach attachment = adapterBaseWorkflowManager
					.getWorkflowAttachManager().get(id);
			String[] types = attachment.getAttachName().split("\\.");
			String type = types[1];
			if (attachment == null) {
				throw new SystemException("附件不存在！");
			}
			byte[] content = attachment.getAttachContent();
			if (content == null) {
				throw new SystemException("附件'" + attachment.getAttachName() + "'不存在或已删除！");
			}
			String path="";
			if(content!=null){
				InputStream is = FileUtil.ByteArray2InputStream(content);
				path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, id+"."+type);
				logger.info("生成附件：" + path);
			}
//			String ip = getRequest().getLocalAddr();
//			int port = getRequest().getServerPort();
//			this.renderText("http://"+ip+":"+port+path);
			this.renderText(path);
		} catch (SystemException ex) {
		      throw ex;
		} catch (Exception ex) {
		      throw new SystemException(ex);
		}
	}
	
	public T getModel() {
		return null;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getQueryFormId() {
		return queryFormId;
	}

	public void setQueryFormId(String queryFormId) {
		this.queryFormId = queryFormId;
	}

	public String getViewFormId() {
		return viewFormId;
	}

	public void setViewFormId(String viewFormId) {
		this.viewFormId = viewFormId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		try {
			workflowName = URLDecoder.decode(workflowName, "utf-8");
			workflowName = URLDecoder.decode(workflowName, "utf-8");//解决在weblogic中workflowName字段乱码问题
		} catch (UnsupportedEncodingException e) {
			logger.error("流程名称转码异常！");
		}
		this.workflowName = workflowName;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		try {
			nodeName = URLDecoder.decode(nodeName, "utf-8");
		} catch (Exception e) {
			logger.error("节点名称转码异常。", e);
		}
		this.nodeName = nodeName;
	}

	public String getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}

	public String getTransitionName() {
		return transitionName;
	}

	public void setTransitionName(String transitionName) {
		/*添加迁移线名称转码处理 严建 2012-08-31 09:31*/
		try {
			transitionName = URLDecoder.decode(transitionName, "utf-8");
			transitionName = URLDecoder.decode(transitionName, "utf-8");//解决在weblogic中transitionName字段乱码问题
		} catch (Exception e) {
			logger.error("迁移线名称转码异常。", e);
		}
		this.transitionName = transitionName;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		if (formId != null) {
			logger.error("处理表单中的空格！");
			formId = formId.trim();// 表单中赋值时可能出现空格
		}
		this.formId = formId;
	}

	public String getListMode() {
		return listMode;
	}

	public void setListMode(String listMode) {
		this.listMode = listMode;
	}

	public String getUrgencytype() {
		return urgencytype;
	}

	public void setUrgencytype(String urgencytype) {
		this.urgencytype = urgencytype;
	}

	public void setFormData(File formData) {
		this.formData = formData;
	}

	public String getHandlerMes() {
		return handlerMes;
	}

	public void setHandlerMes(String handlerMes) {
		try {
			handlerMes = URLDecoder.decode(handlerMes, "utf-8");
			handlerMes = URLDecoder.decode(handlerMes, "utf-8");//解决在weblogic中handlerMes字段乱码问题
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.handlerMes = handlerMes;
	}

	public String getConcurrentTrans() {
		return concurrentTrans;
	}

	public void setConcurrentTrans(String concurrentTrans) {
		this.concurrentTrans = concurrentTrans;
	}

	public String getIsSelectOtherActors() {
		return isSelectOtherActors;
	}

	public void setIsSelectOtherActors(String isSelectOtherActors) {
		this.isSelectOtherActors = isSelectOtherActors;
	}

	public String getMaxTaskActors() {
		return maxTaskActors;
	}

	public void setMaxTaskActors(String maxTaskActors) {
		this.maxTaskActors = maxTaskActors;
	}

	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public String getStrTaskActors() {
		return strTaskActors;
	}

	public void setStrTaskActors(String strTaskActors) {
		this.strTaskActors = strTaskActors;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		try {
			suggestion = URLDecoder.decode(suggestion, "utf-8");
			suggestion = URLDecoder.decode(suggestion, "utf-8");//解决在weblogic中suggestion字段乱码问题
			if (suggestion != null) {
				suggestion = suggestion.replace(";", "；");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("转码异常！");
		}
		this.suggestion = suggestion;
	}

	public List getTaskActors() {
		return taskActors;
	}

	public void setTaskActors(List taskActors) {
		this.taskActors = taskActors;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		try {
			businessName = URLDecoder.decode(businessName, "utf-8");
			businessName = URLDecoder.decode(businessName, "utf-8");//解决在weblogic中businessName字段乱码问题
		} catch (UnsupportedEncodingException e) {
			logger.error("转码异常！");
		}
		if (businessName != null) {
			if (businessName.indexOf("%2B") != -1) {
				businessName = businessName.replaceAll("%2B", "+");
			}
		}
		this.businessName = businessName;
	}

	public Date getEndDate() {
		return endDate;
	}

	@SuppressWarnings("deprecation")
	public void setEndDate(Date endDate) {
		if(endDate !=null || "".equals(endDate)){
		endDate.setHours(23);
		endDate.setMinutes(59);
		}
		this.endDate = endDate;
	}

	public String getIsBackSpace() {
		return isBackSpace;
	}

	public void setIsBackSpace(String isBackSpace) {
		this.isBackSpace = isBackSpace;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkflowTypeName() {
		return workflowTypeName;
	}

	public void setWorkflowTypeName(String workflowTypeName) {
		try {
			workflowTypeName = URLDecoder.decode(workflowTypeName, "utf-8");
			workflowTypeName = URLDecoder.decode(workflowTypeName, "utf-8");//解决在weblogic中workflowTypeName字段乱码问题
		} catch (UnsupportedEncodingException e) {
			logger.error("流程类型转码出现异常。", e);
		}
		this.workflowTypeName = workflowTypeName;
	}

	public String getPkFieldName() {
		return pkFieldName;
	}

	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	public String getPkFieldValue() {
		return pkFieldValue;
	}

	public void setPkFieldValue(String pkFieldValue) {
		this.pkFieldValue = pkFieldValue;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Object[]> getListAnnal() {
		return listAnnal;
	}

	public void setListAnnal(List<Object[]> listAnnal) {
		this.listAnnal = listAnnal;
	}

	public String getReturnFlag() {
		return returnFlag;
	}

	public void setReturnFlag(String returnFlag) {
		this.returnFlag = returnFlag;
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public Page getPageWorkflow() {
		if (pageWorkflow.getPageSizeBak() != -1
				&& pageWorkflow.getPageSizeBak() != pageWorkflow.getPageSize()) {
			pageWorkflow.setPageNo(1);
		}
		if (pageWorkflow.getPageSizeBak() != -1
				&& pageWorkflow.getPageNoBak() == pageWorkflow.getPageNo()) {
			pageWorkflow.setPageNo(1);
		}
		return pageWorkflow;
	}

	public void setPageWorkflow(Page pageWorkflow) {
		this.pageWorkflow = pageWorkflow;
	}

	public List<Object[]> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(List<Object[]> workflows) {
		this.workflows = workflows;
	}

	public List<EForm> getFormListbyWFType() {
		return formListbyWFType;
	}

	public void setFormListbyWFType(List<EForm> formListbyWFType) {
		this.formListbyWFType = formListbyWFType;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAssignType() {
		return assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getInitiativeType() {
		return initiativeType;
	}

	public void setInitiativeType(String initiativeType) {
		this.initiativeType = initiativeType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getFullFormData() {
		return fullFormData;
	}

	public void setFullFormData(String fullFormData) {
		this.fullFormData = fullFormData;
	}

	public List getIdeaLst() {
		return ideaLst;
	}

	public void setIdeaLst(List ideaLst) {
		this.ideaLst = ideaLst;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getPluginInfo() {
		return pluginInfo;
	}

	public void setPluginInfo(String pluginInfo) {
		this.pluginInfo = pluginInfo;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public File getWordDoc() {
		return wordDoc;
	}

	public void setWordDoc(File wordDoc) {
		this.wordDoc = wordDoc;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Map<String, List<Object[]>> getWorkflowMap() {
		return workflowMap;
	}

	public List<Object[]> getWorkflowTypeList() {
		return workflowTypeList;
	}

	public Map<String, List<Object[]>> getLeftWorkflowMap() {
		return leftWorkflowMap;
	}

	public void setLeftWorkflowMap(Map<String, List<Object[]>> leftWorkflowMap) {
		this.leftWorkflowMap = leftWorkflowMap;
	}

	public List<Object[]> getLeftWorkflowTypeList() {
		return leftWorkflowTypeList;
	}

	public void setLeftWorkflowTypeList(List<Object[]> leftWorkflowTypeList) {
		this.leftWorkflowTypeList = leftWorkflowTypeList;
	}

	public Map<String, List<Object[]>> getRightWorkflowMap() {
		return rightWorkflowMap;
	}

	public void setRightWorkflowMap(Map<String, List<Object[]>> rightWorkflowMap) {
		this.rightWorkflowMap = rightWorkflowMap;
	}

	public List<Object[]> getRightWorkflowTypeList() {
		return rightWorkflowTypeList;
	}

	public void setRightWorkflowTypeList(List<Object[]> rightWorkflowTypeList) {
		this.rightWorkflowTypeList = rightWorkflowTypeList;
	}

	public String getCASignInfo() {
		return CASignInfo;
	}

	public void setCASignInfo(String signInfo) {
		CASignInfo = signInfo;
	}

	public String getRemindSet() {
		return remindSet;
	}

	public void setRemindSet(String remindSet) {
		this.remindSet = remindSet;
	}

	public String getIsGenzong() {
		return isGenzong;
	}

	public void setIsGenzong(String isGenzong) {
		this.isGenzong = isGenzong;
	}

	public String getProcessTimeout() {
		return processTimeout;
	}

	public void setProcessTimeout(String processTimeout) {
		this.processTimeout = processTimeout;
	}

	public String getNoTotal() {
		return noTotal;
	}

	public void setNoTotal(String noTotal) {
		this.noTotal = noTotal;
	}

	public String getParentInstanceId() {
		return parentInstanceId;
	}

	public void setParentInstanceId(String parentInstanceId) {
		this.parentInstanceId = parentInstanceId;
	}

	public String getAppointUserId() {
		return appointUserId;
	}

	public void setAppointUserId(String appointUserId) {
		this.appointUserId = appointUserId;
	}

	public String getAppointDeptId() {
		return appointDeptId;
	}

	public void setAppointDeptId(String appointDeptId) {
		this.appointDeptId = appointDeptId;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPersonDemo() {
		return personDemo;
	}

	public void setPersonDemo(String personDemo) {
		this.personDemo = personDemo;
	}

	public String getNewFormId() {
		return newFormId;
	}

	public void setNewFormId(String newFormId) {
		this.newFormId = newFormId;
	}

	public String getProcessSuspend() {
		return processSuspend;
	}

	public void setProcessSuspend(String processSuspend) {
		this.processSuspend = processSuspend;
	}

	public String getShowSignUserInfo() {
		return showSignUserInfo;
	}

	public void setShowSignUserInfo(String showSignUserInfo) {
		this.showSignUserInfo = showSignUserInfo;
	}

	public String getFilterSign() {
		return filterSign;
	}

	public void setFilterSign(String filterSign) {
		this.filterSign = filterSign;
	}

	public String getrUserId() {
		return rUserId;
	}

	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}

	public String getAllowChangeMainActor() {
		return allowChangeMainActor;
	}

	public void setAllowChangeMainActor(String allowChangeMainActor) {
		this.allowChangeMainActor = allowChangeMainActor;
	}

	public String getFilterYJZX() {
		return filterYJZX;
	}

	public void setFilterYJZX(String filterYJZX) {
		this.filterYJZX = filterYJZX;
	}

	public String getHandleKind() {
		return handleKind;
	}

	public void setHandleKind(String handleKind) {
		this.handleKind = handleKind;
	}

	public String getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(String taskIds) {
		this.taskIds = taskIds;
	}

	public String getExcludeWorkflowType() {
		return excludeWorkflowType;
	}

	public void setExcludeWorkflowType(String excludeWorkflowType) {
		this.excludeWorkflowType = excludeWorkflowType;
	}

	public String getDoneYear() {
		return doneYear;
	}

	public void setDoneYear(String doneYear) {
		if(doneYear == null){
			doneYear = "";
		}
		this.doneYear = doneYear;
	}

	public String getProcessOutTime() {
		return processOutTime;
	}

	public void setProcessOutTime(String processOutTime) {
		this.processOutTime = processOutTime;
	}

	public String getDaiBan() {
		return daiBan;
	}

	public void setDaiBan(String daiBan) {
		this.daiBan = daiBan;
	}

	public String getTableNames() {
		return tableNames;
	}

	public void setTableNames(String tableNames) {
		this.tableNames = tableNames;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public Date getWorkflowEndDatestartDate() {
		return workflowEndDatestartDate;
	}

	public void setWorkflowEndDatestartDate(Date workflowEndDatestartDate) {
		this.workflowEndDatestartDate = workflowEndDatestartDate;
	}

	public Date getWorkflowEndDateendDate() {
		return workflowEndDateendDate;
	}
	@SuppressWarnings("deprecation")
	public void setWorkflowEndDateendDate(Date workflowEndDateendDate) {
		if(workflowEndDateendDate !=null || "".equals(workflowEndDateendDate)){
			workflowEndDateendDate.setHours(23);
			workflowEndDateendDate.setMinutes(59);
			}
		this.workflowEndDateendDate = workflowEndDateendDate;
	}

	public String getExcludeWorkflowTypeName() {
		return excludeWorkflowTypeName;
	}

	public void setExcludeWorkflowTypeName(String excludeWorkflowTypeName) {
		this.excludeWorkflowTypeName = excludeWorkflowTypeName;
	}
	public String getViewReturn() {
		return viewReturn;
	}

	public void setViewReturn(String viewReturn) {
		this.viewReturn = viewReturn;
	}

	public String getOrguserid() {
		return orguserid;
	}

	public void setOrguserid(String orguserid) {
		this.orguserid = orguserid;
	}

	public String getSuggestionrequired() {
		return suggestionrequired;
	}

	public void setSuggestionrequired(String suggestionrequired) {
		this.suggestionrequired = suggestionrequired;
	}
	
	

}