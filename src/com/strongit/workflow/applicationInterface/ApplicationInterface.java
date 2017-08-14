package com.strongit.workflow.applicationInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.axis.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.strongit.oa.bo.TProcessUrgency;
import com.strongit.oa.common.remind.RemindManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.common.workflow.applicationInterface.ApplicationInterfaceExtendManager;
import com.strongit.oa.im.IMMessageService;
import com.strongit.oa.message.IMessageService;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.StringUtil;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseTransitionPlugin;
import com.strongit.workflow.po.DueTimerBean;
import com.strongit.workflow.po.ProcessInstanceBean;
import com.strongit.workflow.po.TaskInstanceBean;
import com.strongit.workflow.util.WorkflowConst;

/**
 * 业务应用程序接口调用类
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Feb 4, 2009  8:40:46 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.applicationInterface.UupInterface
 */
@Service
@Transactional
public class ApplicationInterface implements IApplicationInterface{

	Logger logger = LoggerFactory.getLogger(ApplicationInterface.class);
	
	private IMailService mailService;
	
	private IsmsService smsService;
	
	private IMessageService messageService;
	
	@Autowired RemindManager remindManager;
	@Autowired
	ApplicationInterfaceExtendManager applicationInterfaceExtendManager;
	@Autowired IWorkflowService workflowService;//注入工作流服务类.
	@Autowired IUserService userService;//注入工作流服务类.
	
	/**
	 * Rtx提醒服务
	 */
	private IMMessageService imService;
	
	/**
	 * 调用业务系统邮件发送接口
	 * @author  喻斌
	 * @date    Feb 4, 2009  8:45:07 AM
	 * @param userId -用户Id
	 * @param mailContent -邮件内容
	 */
	public void sendMailAction(String sendFlag, String title
			, List<String> userIds, String mailContent){
		String[] cont = mailContent.split("\\*");
		mailContent = mailContent.replace(cont[0]+"*", "");
		mailService.autoSendMail(userIds, title, mailContent, sendFlag);
		//System.out.println("向" + userIds.get(0) + "发送邮件：" + mailContent);
	}

	/**
	 * 调用业务系统rtx消息发送接口
	 * @author 喻斌
	 * @date Jun 19, 2009 10:56:19 AM
	 * @param sendFlag -发送标识
	 * @param userIds -接收人员Ids
	 * @param rtxContent -消息内容
	 */
	public void sendRtxAction(String sendFlag, List<String> userIds, String rtxContent){
		try {
			System.out.println("向" + userIds.get(0) + "发送rtx消息提醒：" + rtxContent);
			String[] cont = rtxContent.split("\\*");
			rtxContent = rtxContent.replace(cont[0]+"*", "");
			imService.sendIMMessage(rtxContent, userIds,null);
		} catch (Exception e) {
			LogPrintStackUtil.logException(e);
		}
	}
	
	/**
	 * 调用业务系统短消息发送接口
	 * @author  喻斌
	 * @date    Feb 4, 2009  8:46:03 AM
	 * @param userId -用户Id
	 * @param mesContent -短消息内容
	 */
	public void sendMessageAction(String sendFlag, List<String> userIds
			, String mesContent){
		String[] cont = mesContent.split("\\*");
		mesContent = mesContent.replace(cont[0]+"*", "");
		smsService.sendSms(sendFlag, userIds, mesContent);
		System.out.println("向" + userIds.get(0) + "发送短消息：" + mesContent);
	}
	
	/**
	 * 调用业务系统通知提醒发送接口
	 * @author  喻斌
	 * @date    Feb 4, 2009  8:46:36 AM
	 * @param userId -用户Id
	 * @param noticeContent -通知提醒内容
	 */
	public void sendNoticeAction(String sendFlag, String title
			, List<String> userIds, String noticeContent){
		if(null!=noticeContent&&!"".equals(noticeContent)){
			
			String[] cont = noticeContent.split("\\*");
			String workFlowType = null;//默认为工作处理
			if(("WorkFlowTypeConst("+WorkFlowTypeConst.INSPECT+")").equals(cont[0])){
				workFlowType = ""+WorkFlowTypeConst.INSPECT;
			}else if(("WorkFlowTypeConst("+WorkFlowTypeConst.SENDDOC+")").equals(cont[0])){
				workFlowType = ""+WorkFlowTypeConst.SENDDOC;
			}else if(("WorkFlowTypeConst("+WorkFlowTypeConst.RECEDOC+")").equals(cont[0])){
				workFlowType = ""+WorkFlowTypeConst.RECEDOC;
			}else if(("WorkFlowTypeConst("+GlobalBaseData.MSG_GZCL+")").equals(cont[0])){
				workFlowType = ""+GlobalBaseData.MSG_GZCL;
			}
			
			noticeContent = noticeContent.replace(cont[0]+"*", "");
			messageService.sendMsg(sendFlag, userIds, title, noticeContent,workFlowType);
		}
	}
	
	@Autowired
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
	
	@Autowired
	public void setSmsService(IsmsService smsService) {
		this.smsService = smsService;
	}
	
	@Autowired
	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	@Autowired
	public void setImService(IMMessageService imService) {
		this.imService = imService;
	}

	/**
	 *  流程信息发送接口
	 *  sendFlag - -催办发送标识
		system：系统级发送 
		(see:WorkflowConst.WORKFLOW_NOTICE_SYSTEM) 
		person：手工级发送 
		(see:WorkflowConst.WORKFLOW_NOTICE_PERSON) 
		processInstanceBean - -流程信息
		noticeGroup - -催办人群类型
		内容说明： 
		owner：流程管理者 
		(see:WorkflowConst.WORKFLOW_URGENCY_GROUP_OWNER) 
		startor：流程发起者 
		(see:WorkflowConst.WORKFLOW_URGENCY_GROUP_STARTOR) 
		handler：流程任务处理人 
		(see:WorkflowConst.WORKFLOW_URGENCY_GROUP_HANDLER) 
		noticeMethod - -催办方式
		message：短消息方式 
		(see:WorkflowConst.WORKFLOW_NOTICE_TYPE_MESSAGE) 
		mail：邮件方式 
		(see:WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL) 
		notice：通知方式 
		(see:WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE) 
		rtx：rtx方式 
		(see:WorkflowConst.WORKFLOW_NOTICE_TYPE_RTX) 
		isProcessUrgency - -是否是流程催办
		“1”：流程催办；其他：非流程催办
		dueTimerBean - -定时器信息，为以后预留
	 */
	@SuppressWarnings("unchecked")
	public void sendProcessMessage(String sendFlag, ProcessInstanceBean processInstanceBean, List<String> noticeGroup, List<String> noticeMethod, String isProcessUrgency, DueTimerBean dueTimerBean) {
		boolean isSaveProcessUrgencyInfo = false;// 标识变量。是否保存催办记录，默认不保存
		StringBuilder remindType = null;// 催办方式
		StringBuilder handlerMes = null;// 催办内容
		StringBuilder urgencyerId = null;// 催办者id
		StringBuilder urgencyederId = null;// 被催办者id
		Date urgencyDate = new Date(); // 催办时间
		if(WorkflowConst.WORKFLOW_NOTICE_PERSON.equals(sendFlag)){//人工催办
			List<TaskInstanceBean> taskList = processInstanceBean.getTaskLst();
			List<String> userIds = new ArrayList<String>();
			if(taskList!=null && !taskList.isEmpty()){
				for(TaskInstanceBean taskBean : taskList){
					List<String> actorIds = taskBean.getActorIdLst();//得到人员
					userIds.addAll(actorIds);
				}
				remindManager.sendRemind(userIds,null);
			}
			if ("1".equals(isProcessUrgency)) {// 流程催办
				ActionContext cxt = ActionContext.getContext();
				if (cxt != null) {
					Map<String, Object> session = cxt.getSession();
					// 获取提醒方式
					if (session.get("remindType") != null) {
						if (remindType == null) {
							remindType = new StringBuilder();
						}
						remindType.append((String)session.get("remindType"));
					}
					// 获取提醒内容
					if (session.get("handlerMes") != null) {
						if (handlerMes == null) {
							handlerMes = new StringBuilder();
						}
						handlerMes.append((String) session.get("handlerMes"));
					}
					// 获取被催办人信息
					if (userIds != null && !userIds.isEmpty()) {
						urgencyederId = new StringBuilder().append(StringUtil
								.stringListToString(userIds));
					}
					urgencyerId = new StringBuilder(userService
							.getCurrentUser().getUserId());
					if (!userIds.contains(urgencyerId.toString())) {// 提醒人中不包含催办人时，添加催办人作为提醒人
						userIds.add(urgencyerId.toString());// 消息提醒人添加催办人
					}
					isSaveProcessUrgencyInfo = true;// 设置标识符。标识需要保存催办记录
				}
			}
		}else{//系统发送
			if("1".equals(isProcessUrgency)){//催办
				if(processInstanceBean.getIsTimeout() != null){
					return ;
				}
				List<String> userIds = new ArrayList<String>();
				if(noticeGroup != null && !noticeGroup.isEmpty()){
					for(int i=0;i<noticeGroup.size();i++){
						String handler = noticeGroup.get(i);
						if(handler.equals(WorkflowConst.WORKFLOW_URGENCY_GROUP_HANDLER)){
							List<TaskInstanceBean> taskList = processInstanceBean.getTaskLst();
							if(taskList!=null && !taskList.isEmpty()){
								for(TaskInstanceBean taskBean : taskList){
									List<String> actorIds = taskBean.getActorIdLst();//得到人员
									if(actorIds != null && !actorIds.isEmpty()){
										userIds.addAll(actorIds);								
									}
								}
							}
						}
						if(handler.equals(WorkflowConst.WORKFLOW_URGENCY_GROUP_OWNER)){
							List<String> ownerList = processInstanceBean.getProcessOwnerIdLst();
							if(ownerList != null && !ownerList.isEmpty()){
								userIds.addAll(ownerList);
							}
						}
						if(handler.equals(WorkflowConst.WORKFLOW_URGENCY_GROUP_STARTOR)) {
							List<String> startorList = processInstanceBean.getProcessStartorIdLst();
							if(startorList != null && !startorList.isEmpty()){
								userIds.addAll(startorList);
							}
						}
					}
					Set<String> set = new HashSet<String>();
					set.addAll(userIds);
					List<String> duleList = new ArrayList<String>(set);
					/*ActionContext cxt = ActionContext.getContext();
					Locale locale = Locale.getDefault();
					if(cxt != null){
						locale = cxt.getLocale();
					}*/
					//String subject = LocalizedTextUtil.findDefaultText(GlobalBaseData.WORKFLOW_SYSTEM_REMIND, locale,new Object[]{processInstanceBean.getBusinessName()});
					String subject = processInstanceBean.getBusinessName();//"《"+processInstanceBean.getBusinessName()+"》超期,系统自动退回给上一环节处理人员。";
					String content = subject ;
					logger.info(subject);

					for(String notice : noticeMethod){
						if(notice.equals(WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL)) {//邮件提醒
							if(mailService.isMailUseable()){//邮件功能启用
								mailService.autoSendMail(duleList, subject, content, "system");
								logger.info("系统发送邮件提醒。");
							}
						}
						if(notice.equals(WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE)) {//内部消息
							messageService.sendMsg("system", duleList, subject, content, "");
							logger.info("系统发送内部消息提醒");
						}
						if(notice.equals(WorkflowConst.WORKFLOW_NOTICE_TYPE_RTX)) { //Rtx
							try {
								if(imService.isEnabled()) {
									imService.sendIMMessage(subject, duleList,null, IUserService.SYSTEM_ACCOUNT);
//									imService.sendIMMessage(subject, duleList,null);
									logger.info("系统发送即时消息提醒");
								}
							} catch (Exception e) {
								logger.error(e.toString());
							}
						}
						if(notice.equals(WorkflowConst.WORKFLOW_NOTICE_TYPE_MESSAGE)) {
							//if(smsService.isSMSOpen(GlobalBaseData.SMSCODE_WORKFLOW)){
								smsService.sendSms("system", duleList, subject);
								logger.info("系统发送手机短信提醒");
							//}
						}
					}
					logger.info("发送系统催办。" + subject);
//					 设置被催办人
					if (userIds != null && !userIds.isEmpty()) {
						urgencyederId = new StringBuilder().append(StringUtil
								.stringListToString(userIds));
					}
//					 设置催办内容
					handlerMes = new StringBuilder("系统催办");
//					 设置提醒方式 严建
					if (noticeMethod != null && !noticeMethod.isEmpty()) {
						remindType = new StringBuilder().append(StringUtil.stringListToString(noticeMethod));
					}
					urgencyerId = new StringBuilder(IUserService.SYSTEM_ACCOUNT);// 系统催办设置系统用户为催办人
					isSaveProcessUrgencyInfo = true;
				}
			}else{//工作流流转过程中的提醒
				if(noticeGroup != null && !noticeGroup.isEmpty()){
					for(int i=0;i<noticeGroup.size();i++){
						String handler = noticeGroup.get(i);
						if(handler.equals(WorkflowConst.WORKFLOW_URGENCY_GROUP_HANDLER)){
							if(noticeMethod != null && !noticeMethod.isEmpty()){//提醒方式不为空
								List<TaskInstanceBean> taskList = processInstanceBean.getTaskLst();
								List<String> userIds = new ArrayList<String>();
								if(taskList!=null && !taskList.isEmpty()){
									for(TaskInstanceBean taskBean : taskList){
										List<String> actorIds = taskBean.getActorIdLst();//得到人员
										if(actorIds != null){
											userIds.addAll(actorIds);								
										}
									}
								}
								String subject = LocalizedTextUtil.findDefaultText(GlobalBaseData.WORKFLOW_SYSTEM_REMIND, Locale.getDefault(),new Object[]{processInstanceBean.getBusinessName()});
								String content = subject ;
								for(String notice : noticeMethod){
									if(notice.equals(WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL)) {//邮件提醒
										if(mailService.isMailUseable()){//邮件功能启用
											mailService.autoSendMail(userIds, subject, content, "system");
											logger.info("系统发送邮件提醒。");
										}
									}
									if(notice.equals(WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE)) {//内部消息
										messageService.sendMsg("system", userIds, subject, content, "");
										logger.info("系统发送内部消息提醒");
									}
								}
								System.out.println("系统提醒。");
							}
							break;
						}
					}
				}
			}
		}
		// 保存催办记录 严建 2012-05-18 10:25
		if (isSaveProcessUrgencyInfo) {
			applicationInterfaceExtendManager.saveProcessUrgency(
					new TProcessUrgency(processInstanceBean
							.getProcessInstanceId(), (remindType == null ? null
							: remindType.toString()),
							(handlerMes == null ? "系统催办" : handlerMes
									.toString()), urgencyerId.toString(),
							(urgencyederId == null ? urgencyerId
									: urgencyederId).toString(), urgencyDate),
					sendFlag);
		}
	}

	/**
	 * 根据业务系统规则对解析出的任务处理人进行重排序
	 * @param selectUsers 解析出的任务处理人
	 * 	Collection{(0)人员Id, (1)人员名称, (2)组织机构Id}
	 * @param nodeId 任务节点Id;“0”表示为重指派
	 * @param taskId 任务实例Id;“0”表示流程尚未启动
	 * @return
	 * 	Collection 重排序后的任务处理人信息；
	 * 	数据结构为：Object[]{(0)人员Id, (1)人员名称, (2)组织机构Id}
	 */
	@SuppressWarnings("unchecked")
	public Collection<Object[]> reSortUser(Collection<Object[]> selectUsers, String nodeId , String taskId) {
		List<Object[]> userList = new ArrayList<Object[]>(selectUsers);
		if(nodeId != null) {
			TwfBaseNodesetting nodeSetting = workflowService.getNodesettingByNodeId(nodeId);
			String isCountersign = nodeSetting.getNsIsCountersign();//“1”：是会签节点
			String countersignType = nodeSetting.getCounterType();//会签类型，“0”：串行；“1”：并行
			String isActiveActor = nodeSetting.getIsActiveactor();//是否动态选择处理人；“0”：否；“1”：需要动态选择处理人
			/**
			 * 处理签收节点设置历史数据（统一设置为动态选择处理人） yanjian 2012-12-03 10:51
			 * */
			if(workflowService.isSignNodeTask(nodeId)){
				isActiveActor = "1";
			}
			if("0".equals(isActiveActor)) {//非动态选择处理人
				/*
				 * 处理子流程迁移线选择人员时，提交给错误的人员
				 * */
				//是否为子流程第一个节点
				boolean isSubFirstNode  = false;
				if(workflowService.getTaskInstanceById(taskId) != null){
					String workflowName = workflowService.getTaskInstanceById(taskId).getProcessInstance().getName();
					String firstNodeId = workflowService.getFirstNodeId(workflowName).toString();
					if(firstNodeId.equals(nodeId)){
						isSubFirstNode = true;
					}
					if(isSubFirstNode) {//子流程第一个节点
//					if("开始".equals(nodeSetting.getPreNode())) {//子流程第一个节点
						ActionContext cxt = ActionContext.getContext();
						List<String[]> stackTransitionId = null;
						if(cxt != null) {
							stackTransitionId = (List<String[]>)cxt.get("transitionId");
						}else{
							MessageContext mc = MessageContext.getCurrentContext();
							if(mc!=null){
								//stackTransitionId = (List<String[]>)mc.getCurrentContext().getSession().get("transitionId");;
								if(mc.getProperty("transitionId")!=null){
									stackTransitionId = (List<String[]>)mc.getProperty("transitionId");
									mc.removeProperty("transitionId");
								}
							}
						}
						if(stackTransitionId != null && !stackTransitionId.isEmpty()) {
							Object[] toSelectItems = {"processName"};
							List sItems = Arrays.asList(toSelectItems);
							Map<String, Object> paramsMap = new HashMap<String, Object>();
							paramsMap.put("taskId", taskId);
							paramsMap.put("taskNodeId", nodeId);
							List result = workflowService.getTaskInfosByConditionForList(sItems, paramsMap, null, null, null, null, null);
							if(result != null && !result.isEmpty()) {
								Object objs = (Object)result.get(0);
								String currentProcessName = objs.toString();
								for(String[] stack : stackTransitionId) {
									if(stack[0].equals(currentProcessName)) {
										String transitionId = stack[1];//得到迁移线id
										TwfBaseTransitionPlugin plugin = workflowService.getTransitionPluginByTsId(
												transitionId, "plugins_handleactor");
										if (plugin != null) {
											String actors = plugin.getValue();
											if(actors != null && !"".equals(actors)) {
												String[] actor = ((actors != null) && (!("".equals(actors)))) ? actors
														.split("\\|")
														: null;
														userList = workflowService.parseToSelectActors(actor, taskId);
														logger.info("找到迁移线上的人员设置信息 :" + actors);					
											}
										}
										stackTransitionId.remove(stack);
										break;
									}
								}
							}
						}
					}
				}
			} else {//动态选择处理人也可能进入此方法
				ActionContext cxt = ActionContext.getContext();
				if(cxt != null) {
					String strTaskActor = (String)cxt.get("strTaskActors");
					if(strTaskActor != null && strTaskActor.length() > 0) {
						String[] strTaskActors = strTaskActor.split(",");
						List<Object[]> result = new ArrayList<Object[]>();
						for(String actor : strTaskActors) {
							String[] actors = actor.split("\\|");
							String userId = actors[0];
							String cnodeId = actors[1];
							if(nodeId.equals(cnodeId)) {
								result.add(new Object[]{userId,null,null});							
							}
						}
						if(!result.isEmpty()) {
							userList = result;
						}
					}
				}
			}
			
			//如果是串行会签,则按排序号从大到小分配任务
			if("1".equals(isCountersign) && "0".equals(countersignType) && "0".equals(isActiveActor)) {//串行会签 
				System.out.println("对人员排序********************"); 
				final Map<String,TUumsBaseUser> userMap = new HashMap<String, TUumsBaseUser>();
				for(Object[] objs : userList) {
					String userId = (String)objs[0];
					TUumsBaseUser user = userService.getUserInfoByUserId(userId);
					if(user != null) {
						userMap.put(user.getUserId(), user);
					}
				}
				Collections.sort(userList, new Comparator<Object[]>(){

					public int compare(Object[] o1, Object[] o2) {
						TUumsBaseUser user1 = userMap.get(o1[0]);
						TUumsBaseUser user2 = userMap.get(o2[0]);
						Long key1;
						if(user1 != null && user1.getUserSequence()!=null){
							key1 = Long.valueOf(user1.getUserSequence());
						}else{
							key1 = Long.MAX_VALUE;
						}
						Long key2;
						if(user2 != null && user2.getUserSequence() != null){
							key2 = Long.valueOf(user2.getUserSequence());
						}else{
							key2 = Long.MAX_VALUE;
						}
						return key2.compareTo(key1); 
					}
					
				});
				userMap.clear();
			}
		}
		return userList;
	}

	/**
	 * 根据传入的标志、流程定义Id或节点Id得到动态设置的定时器信息，以实现动态设置定时器的功能
	 * 
	 * @param 	flag - -处理标志，“process”：获取流程定时器信息，processInstanceId参数有效；“node”：获取任务定时器信息，nodeId参数有效
	 * @param	processDefinitionId - -流程定义Id，flag=“process”时有效
	 * @param	nodeId - -节点Id，flag=“node”时有效
	 * @return	
	 * 			Object[] 定时器设置信息；数据格式为{(0)定时器到期日期【时间类型】，(1)重复提醒的时间间隔【字符串类型】}
						“重复提醒的时间间隔”数据格式为：XX day/hour/minute，其中XX为数字，分别表示 天、小时和分钟
	 */
	public Object[] activeSetTime(String flag, String processDefinitionId, String nodeId) {
		if("process".equals(flag)) {//动态设置流程实例超时
			
		}
		System.out.println("定时器执行...... ");
		return null;
	}
}