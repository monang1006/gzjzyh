/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.common.workflow.service;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.strongit.oa.archive.ITempFileService;
import com.strongit.oa.bo.TefTemplateConfig;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.business.jbpmbusiness.JBPMBusinessManager;
import com.strongit.oa.common.business.mainactorconfig.MainActorConfigManage;
import com.strongit.oa.common.business.mainactorconfig.MainReassignActorConfigManage;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.extend.BaseExtendManager;
import com.strongit.oa.common.extend.pojo.GoToNextTransitionBean;
import com.strongit.oa.common.extend.pojo.StartProcessToFistNodeBean;
import com.strongit.oa.common.extend.pojo.StartWorkflowBean;
import com.strongit.oa.common.service.DataBaseUtil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IDocumentPrivilegeService;
import com.strongit.oa.common.workflow.INodeService;
import com.strongit.oa.common.workflow.IWorkService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.common.workflow.parameter.Parameter;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.formconfig.ConfigManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.prsnfldr.util.Round;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocApproveinfoManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.daohelper.IDao;
import com.strongit.workflow.actorSettingInterface.AbstractActorSetting;
import com.strongit.workflow.bo.TwfBaseActorSetting;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.bo.TwfBaseSubprocess;
import com.strongit.workflow.bo.TwfBaseTransitionPlugin;
import com.strongit.workflow.bo.TwfBaseWorkflow;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.bo.TwfInfoProcessType;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.po.TaskInstanceBean;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.uupInterface.IUupInterface;
import com.strongit.workflow.workflowInterface.IOaSystemService;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongit.workflow.workflowInterface.IWorkflowConfigService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * 工作流接口实现类，适配工作流构件提供的接口
 * 
 * @author dengwenqiang
 * @version 1.0
 */
/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 19, 2012 4:05:44 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.workflow.service.WorkflowService
 */
@Service
@OALogger
public class WorkflowService implements IWorkflowService,IWorkService {
	@Autowired
	BaseExtendManager baseExtendManager;// BaseManager的功能扩展管理类

	@Autowired
	ITaskService workflowRun;

	@Autowired
	IProcessDefinitionService workflowDesign;

	@Autowired
	IProcessInstanceService workflowDelegation;

	@Autowired
	IOaSystemService oaSystemService;

	@Autowired
	SendDocManager sendDocManager;// 发文Manager

	@Autowired
	ITempFileService tempFileManager;// 文件归档Manager

	@Autowired
	ConfigManager configManager; // 表单配置Manager

	@Autowired
	IUserService userService;// 统一用户服务
	@Autowired
	ICustomUserService customUserService;//自定义用户管理服务

	@Autowired
	IUupInterface uupInterface;// UUMS统一用户接口

	@Autowired
	private IWorkflowConfigService workflowConfigService;

	@Autowired
	private DefinitionPluginService definitionPluginService;// 流程定义插件服务类.
	@Autowired
	private SystemsetManager systemsetManager;
	@Autowired
	private JBPMBusinessManager jbpmBusinessManager;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private GenericDAOHibernate<Object, String> serviceDAO;

	@Autowired
	private MyLogManager logService;
	
	@Autowired
	private MainActorConfigManage mainActorConfigManage;
	@Autowired
	private MainReassignActorConfigManage mainReassignActorConfigManage;
	@Autowired
	private SendDocApproveinfoManager sendDocApproveinfoManager;
	@Autowired
	private INodeService nodeService;
	@Autowired
	private IDocumentPrivilegeService documentPrivilegeService;
	
	private IDao baseDAO;
	
	@Resource
	public void setbaseDAO(IDao baseDAO) {
		this.baseDAO = baseDAO;
	}
	
	@Autowired
	SessionFactory sessionFactory; // 提供session
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		serviceDAO = new GenericDAOHibernate<Object, String>(sessionFactory,
				Object.class);
	}

	public GenericDAOHibernate getServiceDAO() {
		return serviceDAO;
	}

	public WorkflowService() {
	}// 构造函数

	/**
	 * 根据流程实例id得到所有父流程实例id
	 * 
	 * @author:邓志城
	 * @date:2010-11-17 上午11:12:16
	 * @param instanceId
	 *            当前流程实例id
	 * @return 父流程实例id列表
	 * @throws WorkflowException
	 */
	public List getMonitorParentInstanceIds(Long instanceId)
			throws WorkflowException {
		// return workflowDelegation.getMonitorParentInstanceIds(instanceId);
		return this.loopGetParentInstanceIds(instanceId, new ArrayList());
	}

	/**
	 * 根据流程实例id得到父流程实例id
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 父流程实例 List<Object[]>
	 *         流程父流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务名称，(2)流程名称}
	 */
	public List getParentInstanceId(Long instanceId) {
		return workflowDelegation.getMonitorParentInstanceIds(instanceId);
	}

	/**
	 * 得到子流程实例列表
	 * 
	 * @param parentInstanceId
	 * @return
	 */
	public List getChildInstanceId(Long parentInstanceId) {
		return workflowDelegation
				.getMonitorChildrenInstanceIds(parentInstanceId);
	}

	/**
         * 按流程实例ID获取流程实例监控数据
         * 
         * @description
         * @author 严建
         * @param instanceId
         *                流程实例Id
         * @return Object[]{(0)流程监控数据, (1)任务监控数据, (2)变量监控数据, (3)Token监控数据,
         *         (4)父流程Id, (5)子流程Id, (6)日志监控数据}
         * @throws com.strongit.workflow.exception.WorkflowException
         * @createTime Apr 27, 2012 5:28:12 PM
         */
	public java.lang.Object[] getProcessInstanceMonitorData(
	    java.lang.Long instanceId)
	    throws com.strongit.workflow.exception.WorkflowException {
    		return workflowDelegation.getProcessInstanceMonitorData(instanceId);
        }
	
	/**
	 * 递归查找子流程的所有父流程实例
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @param list
	 *            空列表用于存储父流程列表
	 * @return 所有父流程实例
	 */
	@SuppressWarnings("unchecked")
	private List loopGetParentInstanceIds(Long instanceId, List list) {
		List parentInstanceIds = workflowDelegation
				.getMonitorParentInstanceIds(instanceId);
		if (parentInstanceIds != null && !parentInstanceIds.isEmpty()) {
			list.addAll(parentInstanceIds);
			for (int i = 0; i < parentInstanceIds.size(); i++) {
				Object[] objs = (Object[]) parentInstanceIds.get(i);
				Long processInstanceId = Long.parseLong(objs[0].toString());
				loopGetParentInstanceIds(processInstanceId, list);
			}
		}
		return list;
	}

	/**
	 * 根据流程实例id得到所有子流程实例id列表
	 * 
	 * @param instanceId
	 *            当前流程实例id
	 */
	public List getMonitorChildrenInstanceIds(Long instanceId)
			throws WorkflowException {
		return this.loopGetChildrenInstanceIds(instanceId, new ArrayList());
	}

	/**
	 * 递归查找父流程下的所有子流程实例
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @param list
	 *            空列表用于存储子流程列表
	 * @return 所有子流程实例
	 */
	@SuppressWarnings("unchecked")
	private List loopGetChildrenInstanceIds(Long instanceId, List list) {
		List childrenInstanceIds = workflowDelegation
				.getMonitorChildrenInstanceIds(instanceId);
		if (childrenInstanceIds != null && !childrenInstanceIds.isEmpty()) {
			list.addAll(childrenInstanceIds);
			for (int i = 0; i < childrenInstanceIds.size(); i++) {
				Object[] objs = (Object[]) childrenInstanceIds.get(i);
				Long processInstanceId = Long.parseLong(objs[0].toString());
				loopGetChildrenInstanceIds(processInstanceId, list);
			}
		}
		return list;
	}

	/**
	 * 得到待办事宜人员排名表
	 * 
	 * @author:邓志城
	 * @date:2010-11-14 下午06:36:43
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getWorkTodoRanksForList() {
		List<User> userList = userService.getAllUserInfo();
		List<String> userIdList = new ArrayList<String>();
		Object[] toSelectItems = { "taskId", "taskStartDate",
				"processInstanceId" };
		List sItems = Arrays.asList(toSelectItems);
		Map paramsMap = new HashMap<String, Object>();
		for (User user : userList) {
			userIdList.add(user.getUserId());
		}
		paramsMap.put("handlerId", userIdList);
		paramsMap.put("taskType", "2");// 待办任务
		Map orderMap = new HashMap<Object, Object>();
		orderMap.put("taskStartDate", "0");
		List<Object[]> list = workflowRun.getTaskInfosByConditionForList(
				sItems, paramsMap, orderMap, null, null, "", null);

		return list;
	}

	/**
	 * @author 邓志城
	 * @date 2010年11月14日13:40:22 流程效率分析统计 只会统计已经办毕的流程,即已经结束的流程
	 * @param processTypeId
	 *            流程类型id 传入NULL则查询所有流程类型流程,否则查询指定流程类型的流程
	 * @return List<Object[流程类型名称,List<Object[流程名称,流程数量,List<Object[节点名称,节点平均耗时]>
	 *         ]>]>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getProcesNodesByTypeForList(String processTypeId) {
		// 统计应该是针对已经结束的流程
		List<Object[]> returnList = new ArrayList<Object[]>();
		try {
			Object[] toSelectItems = { "taskId", "taskStartDate",
					"taskEndDate", "processInstanceId", "processName",
					"processTypeName", "processTypeId", "taskNodeId",
					"taskNodeName" };
			List sItems = Arrays.asList(toSelectItems);
			Map paramsMap = new HashMap<String, Object>();
			if (null != processTypeId && !"".equals(processTypeId)) {
				String[] workflowTypes = processTypeId.split(",");
				List<String> lType = new ArrayList<String>();
				for (String tp : workflowTypes) {
					lType.add(tp);
				}
				paramsMap.put("processTypeId", lType);
			}
			paramsMap.put("processStatus", "1");// 查询办毕的流程
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "0");
			List<Object[]> list = workflowRun.getTaskInfosByConditionForList(
					sItems, paramsMap, orderMap, null, null, "", null);
			Map<String, String> workflowType = new HashMap<String, String>();// Map<id,名称>
			Map<String, List<String>> workflowMap = new LinkedHashMap<String, List<String>>();// 存储流程
			// Map<流程类型id,List<流程名称>>
			Map<String, List<String>> instanceMap = new HashMap<String, List<String>>();// 存储流程实例
			// Map<流程名称,List<流程实例id>>
			Map<String, List<String>> nodeMap = new HashMap<String, List<String>>();// 存储节点
			// Map<流程名称,List<节点id>>
			Map<String, List<Object[]>> taskMap = new HashMap<String, List<Object[]>>();// 存储任务
			// Map<节点id,List<Object[任务开始时间,任务结束时间]>>
			if (list != null && !list.isEmpty()) {
				for (Object[] objs : list) {
					workflowType.put(objs[6].toString(), objs[5].toString());
					workflowType.put(objs[7].toString(), objs[8].toString());// Map<节点id,节点名称>
					if (!workflowMap.containsKey(objs[6].toString())) {
						List<String> workflowList = new ArrayList<String>(1);
						workflowList.add(objs[4].toString());// 流程名称
						workflowMap.put(objs[6].toString(), workflowList);
					} else {
						List<String> workflowList = workflowMap.get(objs[6]
								.toString());
						if (!workflowList.contains(objs[4].toString())) {
							workflowList.add(objs[4].toString());
						}
					}
					// 处理流程实例
					if (!instanceMap.containsKey(objs[4].toString())) {
						List<String> workflowList = new ArrayList<String>(1);
						workflowList.add(objs[3].toString());
						instanceMap.put(objs[4].toString(), workflowList);
					} else {
						List<String> workflowList = instanceMap.get(objs[4]
								.toString());
						if (!workflowList.contains(objs[3].toString())) {
							workflowList.add(objs[3].toString());
						}
					}
					// 处理节点
					if (!nodeMap.containsKey(objs[3].toString())) {
						List<String> workflowList = new ArrayList<String>(1);
						workflowList.add(objs[7].toString());
						nodeMap.put(objs[3].toString(), workflowList);
					} else {
						List<String> workflowList = nodeMap.get(objs[3]
								.toString());
						if (!workflowList.contains(objs[7].toString())) {
							workflowList.add(objs[7].toString());
						}
					}
					// 处理任务 Map<节点id+"-"+流程实例id> 流程实例id加任务节点id确定一条任务办理记录
					if (!taskMap.containsKey(objs[7].toString() + "-"
							+ objs[3].toString())) {
						List<Object[]> workflowList = new ArrayList<Object[]>(1);
						workflowList.add(new Object[] { objs[1], objs[2] });
						taskMap.put(objs[7].toString() + "-"
								+ objs[3].toString(), workflowList);
					} else {
						List<Object[]> workflowList = taskMap.get(objs[7].toString()
								+ "-" + objs[3].toString());
						workflowList.add(new Object[] { objs[1], objs[2] });
					}
				}
				Set<String> set = workflowType.keySet();
				for (Iterator<String> it = set.iterator(); it.hasNext();) {
					String id = it.next();
					String name = workflowType.get(id);
					List<Object[]> objList = new ArrayList<Object[]>();
					List<String> workflowList = workflowMap.get(id);// 得到所有流程
					if (workflowList == null) {
						continue;
					}
					for (String workflowName : workflowList) {
						List instanceList = instanceMap.get(workflowName);
						Object[] workflowInfo = new Object[2];
						workflowInfo[0] = workflowName;// 流程名称
						int instanceCount = 0;
						if (instanceList != null) {
							instanceCount = instanceList.size();
						}
						workflowInfo[1] = instanceCount;
						Map<String, List<Long>> map = new HashMap<String, List<Long>>();
						List<Object[]> nodeInfoList = new ArrayList<Object[]>();
						for (int k = 0; k < instanceCount; k++) {
							String instanceId = String.valueOf(instanceList.get(k));
							List<String> nodeList = nodeMap.get(instanceId);// 得到流程实例下的节点列表
							if (nodeList == null) {
								continue;
							}
							for (int i = 0; i < nodeList.size(); i++) {
								String nodeId = nodeList.get(i);
								String nodeName = workflowType.get(nodeId);
								List<Object[]> taskTime = taskMap.get(nodeId
										+ "-" + instanceId);
								Long time = null;
								if (taskTime != null) {
									if (taskTime.size() == 1) {// 普通任务节点
										Object[] timeInfo = taskTime.get(0);
										Date startDate = (Date) timeInfo[0];
										if (timeInfo[1] != null) {
											Date endDate = (Date) timeInfo[1];
											long l = endDate.getTime()
													- startDate.getTime();// 相差的毫秒数
											l = l / (1000 * 60);// 得到分钟
											time = l;
										}
									}
									if (taskTime.size() > 1) {// 超过一条,说明是会签
										Object[] firstTimeInfo = taskTime
												.get(0);
										Object[] lastTimeInfo = taskTime
												.get(taskTime.size() - 1);
										Date startDate = (Date) firstTimeInfo[0];
										if (lastTimeInfo[1] != null) {
											Date endDate = (Date) lastTimeInfo[1];
											long l = endDate.getTime()
													- startDate.getTime();// 相差的毫秒数
											l = l / (1000 * 60);// 得到分钟
											time = l;
										}
									}
								}
								// 得到节点名称相同的时间差列表
								if (!map.containsKey(nodeName.toString())) {
									List<Long> timeList = new ArrayList<Long>();
									timeList.add(time);
									map.put(nodeName.toString(), timeList);
								} else {
									map.get(nodeName.toString()).add(time);
								}
							}

						}
						Set keySet = map.keySet();
						for (Iterator iter = keySet.iterator(); iter.hasNext();) {
							String key = (String) iter.next();
							List<Long> timeList = map.get(key);
							Long total = 0L;
							Double d = 0.0;
							for (Long tl : timeList) {
								total += tl;
								d += tl;
							}
							if (total > 0) {
								total = total / timeList.size();// 求平均值
								d = d / timeList.size();
							}
							if (total > 60) {
								d = d / 60;
								d = Round.round(d, 1);
								nodeInfoList
										.add(new Object[] { key, d + "小时" });
							} else {
								nodeInfoList.add(new Object[] { key,
										total + "分钟" });
							}
						}
						workflowInfo = ObjectUtils.addObjectToArray(
								workflowInfo, nodeInfoList);
						objList.add(workflowInfo);
					}
					returnList.add(new Object[] { name, objList });
				}

			}
		} catch (Exception e) {
			logger.error("统计流程时发生异常", e);
		}
		return returnList;
	}

	/**
	 * 流程设计分页列表
	 * 
	 * @author:邓志城
	 * @date:2010-11-26 下午04:47:15
	 * @param page
	 *            分页对象
	 * @param fileName
	 *            流程文件名称：支持模糊查询
	 * @param createDate1
	 *            流程创建开始日期
	 * @param createDate2
	 *            流程创建截止日期
	 * @param creatorName
	 *            创建人：支持模糊查询
	 * @param workflowType
	 *            流程类型
	 * @param isDeploy
	 *            是否部署
	 * @return List<Map<属性名,属性值>>
	 */
	public Page getToDesignProcessFilePage(Page page, String fileName,
			Date createDate1, Date createDate2, String creatorName,
			String workflowType, String isDeploy) {
		User userInfo = userService.getCurrentUser();
		List postorgs = uupInterface.getPostOrgList(userInfo.getUserId());
		List userOrgIdList = uupInterface.getDepartmentByUser(userInfo
				.getUserId());
		List<Object> params = new LinkedList<Object>();
		StringBuilder hql = new StringBuilder("from TwfBaseProcessfile t");
		hql.append(" where t.pfIsDelete='0' ");
		if (fileName != null && !"".equals(fileName)) {
			hql.append(" and t.pfName like ?");
			params.add("%" + fileName + "%");
		}
		if (createDate1 != null) {
			hql.append(" and t.pfCreatedate >= ?");
			params.add(createDate1);
		}
		if (createDate2 != null) {
			hql.append(" and t.pfCreatedate <= ?");
			params.add(createDate2);
		}
		if (creatorName != null && !"".equals(creatorName)) {
			hql.append(" and t.pfCreator like ?");
			params.add("%" + creatorName + "%");
		}
		if (workflowType != null && !"".equals(workflowType)) {
			hql.append(" and t.twfInfoProcessType.ptId = ?");
			params.add(Long.valueOf(workflowType));
		}
		if (isDeploy != null && !"".equals(isDeploy)) {
			if ("0".equals(isDeploy)) {// 未部署
				hql
						.append(" and (t.pfIsDeploy = '0' or (t.pfIsDeploy = '1' and t.pfIsEdit = '1'))");
			} else {
				hql.append(" and t.pfIsDeploy = ?");
				params.add(isDeploy);
			}
		}
		hql.append(" and (t.pfCreatorid='").append(userInfo.getUserId())
				.append("'");
		if (!postorgs.isEmpty()) {
			for (int i = 0; i < postorgs.size(); i++) {
				String args[] = (String[]) postorgs.get(i);
				hql.append(" or t.pfProDesigner like '%p").append(args[1])
						.append("$").append(args[0]).append(",%' ");
			}

		}
		if (!userOrgIdList.isEmpty()) {
			for (int i = 0; i < userOrgIdList.size(); i++) {
				String userOrgId = (String) userOrgIdList.get(i);
				hql.append(" or t.pfProDesigner like '%u").append(
						userInfo.getUserId()).append("$").append(userOrgId)
						.append(",%' or t.pfProDesigner like '%o").append(
								userOrgId).append(",%'");
			}
		}
		hql.append(") order by t.pfId desc");
		List list = workflowDesign.getDataByHql(hql.toString(), params
				.toArray());
		List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
		List<EForm> eformList = sendDocManager.getFormList();
		List<EForm> jspeformList = sendDocManager.getJspFormList();//得到jsp表单列表
		Map<String, String> eformMap = new HashMap<String, String>();
		for (EForm eform : eformList) {
			eformMap.put(String.valueOf(eform.getId()), eform.getTitle());
		}
		for (EForm eform : jspeformList) {
			eformMap.put(String.valueOf(eform.getId()), eform.getTitle());//将jsp表单列表添加到eformMap中
		}
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				TwfBaseProcessfile file = (TwfBaseProcessfile) list.get(i);
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("pfId", file.getPfId());
				map.put("pfName", file.getPfName());
				map.put("pfCreatedate", file.getPfCreatedate());
				map.put("pfCreator", file.getPfCreator());
				TwfInfoProcessType type = file.getTwfInfoProcessType();
				String ptName = null;
				if (type != null) {
					ptName = type.getPtName();
				}
				if (ptName == null) {
					ptName = "未指定";
				}
				map.put("ptName", ptName);
				String formId = String.valueOf(file.getPfMainformId());// 主表单id
				if (!"null".equals(formId) && !"0".equals(formId)) {
					formId = eformMap.get(formId);
					if (formId == null) {
						formId = "<font color='red'>表单已删除</font>";
					}
				} else {
					formId = "未指定表单";
				}
				map.put("formName", formId);
				String pfIsDeploy = (String) file.getPfIsDeploy();
				String pfIsEdit = (String) file.getPfIsEdit();
				if ("0".equals(pfIsDeploy)) {
					pfIsDeploy = "未部署";
				} else if ("1".equals(pfIsDeploy) && "1".equals(pfIsEdit)) {
					pfIsDeploy = "最新版本未部署";
				} else {
					pfIsDeploy = "<font color='red'>已部署</font>";
				}
				map.put("pfIsDeploy", pfIsDeploy);
				result.add(map);
			}
		}
		page = ListUtils.splitList2Page(page, result);
		return page;
	}
	
	//根据TwfBaseProcessfile pfid 查找TwfBaseProcessfile 没有挂在表单的
	public List<TwfBaseProcessfile> getTwfBaseProcessfileByIds(Page page,String[] ids) {
		User userInfo = userService.getCurrentUser();
		List postorgs = uupInterface.getPostOrgList(userInfo.getUserId());
		List userOrgIdList = uupInterface.getDepartmentByUser(userInfo
				.getUserId());
		List<Object> params = new LinkedList<Object>();
		StringBuilder hql = new StringBuilder("from TwfBaseProcessfile t");
		hql.append(" where t.pfIsDelete='0' ");
		hql.append(" and t.pfCreatorid='").append(userInfo.getUserId());
		hql.append("' and t.pfId in(");
		String sss = "";
		for(int i=0;i<ids.length;i++){
			sss = sss +"'"+ ids[i] + "'" + ",";
		}
		sss = sss.substring(0,sss.length()-1);
		hql.append(sss);
		hql.append(") and (t.pfMainformId is null or t.pfMainformId = '0')");
		hql.append(" order by t.pfId desc");
		List<TwfBaseProcessfile> list = workflowDesign.getDataByHql(hql.toString(), params.toArray());
		return list;
	}

	/**
	 * 获取当前用户有权限启动的流程信息 如果传入流程类型参数,则或过滤掉非workflowType下的流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-7 下午05:54:50
	 * @param String
	 *            workflowType 流程类型
	 * @return List<Object[]{流程名称,流程类型id,流程类型名称,流程启动保单id}>
	 * @throws WorkflowException
	 * 
	 * 新建流程时需要过滤掉未挂接表单的流程,例如栏目流程。 过滤条件为：formId=0
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List getStartWorkflow(String workflowType) throws WorkflowException {
		User userInfo = userService.getCurrentUser();
		List postorgs = uupInterface.getPostOrgList(userInfo.getUserId());
		List userOrgIdList = uupInterface.getDepartmentByUser(userInfo
				.getUserId());
		/*
		 * StringBuilder query = new StringBuilder("select
		 * t.pfName,t.twfInfoProcessType.ptId,
		 * t.twfInfoProcessType.ptName,t.pfMainformId from TwfBaseProcessfile t
		 * where "); query.append("t.pfIsDelete='0' and t.pfIsDeploy='1' and
		 * t.pfMainformId !='0'"); if(workflowType != null &&
		 * !"".equals(workflowType)) { query.append(" and
		 * t.twfInfoProcessType.ptId='"+workflowType+"'"); } query.append(" and
		 * (t.pfProStartor is null "); if (!postorgs.isEmpty()){ for (int i = 0;
		 * i < postorgs.size(); i++){ String args[] = (String[])postorgs.get(i);
		 * query.append(" or t.pfProStartor like
		 * '%p").append(args[1]).append("$").append(args[0]).append(",%'"); } }
		 * if (!userOrgIdList.isEmpty()) { for (int i = 0; i <
		 * userOrgIdList.size(); i++) { String userOrgId =
		 * (String)userOrgIdList.get(i); query.append(" or t.pfProStartor like
		 * '%u").append(userInfo.getUserId()).append("$").append(userOrgId).append(",%'
		 * or t.pfProStartor like '%o").append(userOrgId).append(",%'"); } }
		 * query.append(") order by t.twfInfoProcessType.ptId asc ");
		 */
		List result = new LinkedList();
		List list = null;
		List hqlResult = null;
		try {
//		    	StringBuilder querySelectItem = new StringBuilder();
//		    	StringBuilder queryFromItem = new StringBuilder();
//		    	StringBuilder queryWhereItem = new StringBuilder();
//		    	StringBuilder queryOrderByItem = new StringBuilder();
//		    	querySelectItem.append(" t.wfPdid, t.wfName,t.twfInfoProcessType.ptId")
//		    			.append(",t.twfInfoProcessType.ptName,t.twfBaseProcessfile.pfMainformId ");
//		    	queryFromItem.append(" TwfBaseWorkflow t ");
//		    	queryWhereItem.append("   t.twfBaseProcessfile.pfIsDelete='0' and t.twfBaseProcessfile.pfIsDeploy='1' and t.twfBaseProcessfile.pfMainformId !='0'")
//		    			.append(" and (t.twfBaseProcessfile.pfProStartor is null ");
//			if (!postorgs.isEmpty()) {
//				for (int i = 0; i < postorgs.size(); i++) {
//					String args[] = (String[]) postorgs.get(i);
//					queryWhereItem.append(
//							" or t.twfBaseProcessfile.pfProStartor like '%p")
//							.append(args[1]).append("$").append(args[0])
//							.append(",%'");
//				}
//			}
//			if (!userOrgIdList.isEmpty()) {
//				for (int i = 0; i < userOrgIdList.size(); i++) {
//					String userOrgId = (String) userOrgIdList.get(i);
//					queryWhereItem
//							.append(
//									" or t.twfBaseProcessfile.pfProStartor like '%u")
//							.append(userInfo.getUserId())
//							.append("$")
//							.append(userOrgId)
//							.append(
//									",%' or t.twfBaseProcessfile.pfProStartor like '%o")
//							.append(userOrgId).append(",%'");
//				}
//			}
//			queryWhereItem.append(" ) ");
//			queryOrderByItem.append(" t.twfInfoProcessType.ptId asc ");
//			StringBuilder query = new StringBuilder();
//			workflowType = "3";
//			if (workflowType != null && !"".equals(workflowType)) {
//			    	StrBuilder allQuery = new StrBuilder()
//                        			    	.append(" select ").append(querySelectItem)
//                        				.append(" from ").append(queryFromItem)
//                        				.append(" where ").append(queryWhereItem)
//                        				.append(" order by ").append(queryOrderByItem);;
//                        	List allQueryList = workflowDesign.getDataByHql(allQuery.toString(),
//                        					new Object[0]);
//	 			String[] workflowTypes = workflowType.split(",");
//				StringBuilder strType = new StringBuilder();
//				for (String type : workflowTypes) {
//					strType.append(type).append(",");
//				}
//				strType.deleteCharAt(strType.length() - 1);
//				queryWhereItem.append(" and t.twfInfoProcessType.ptId in(" + strType
//						+ ")");
//				StrBuilder workflowTypeQuery = new StrBuilder().append(" select ").append(querySelectItem)
//				.append(" from ").append(queryFromItem)
//				.append(" where ").append(queryWhereItem)
//				.append(" order by ").append(queryOrderByItem);
//				List workflowTypeQueryList = workflowDesign.getDataByHql(workflowTypeQuery.toString(),new Object[0]);
//				list = workflowTypeQueryList;
//			}else{
//			    	query.append(" select ").append(querySelectItem)
//			    		.append(" from ").append(queryFromItem)
//			    		.append(" where ").append(queryWhereItem)
//			    		.append(" order by ").append(queryOrderByItem);
//			    	list = workflowDesign.getDataByHql(query.toString(),new Object[0]);
//			}
			StringBuilder query = new StringBuilder(
					"select t.wfPdid, t.wfName,t.twfInfoProcessType.ptId");
			query.append(",t.twfInfoProcessType.ptName,t.twfBaseProcessfile.pfMainformId from TwfBaseWorkflow t where ");
			query.append("t.twfBaseProcessfile.pfIsDelete='0' and t.twfBaseProcessfile.pfIsDeploy='1' and t.twfBaseProcessfile.pfMainformId !='0'");
			if (workflowType != null && !"".equals(workflowType)) {
				String[] workflowTypes = workflowType.split(",");
				StringBuilder strType = new StringBuilder();
				for (String type : workflowTypes) {
					strType.append(type).append(",");
				}
				strType.deleteCharAt(strType.length() - 1);
				query.append(" and t.twfInfoProcessType.ptId in(" + strType + ")");
			}
			//是否为MicrosoftSQL
			Boolean dateBasetype = DataBaseUtil.isMicrosoftSQL();
			if(dateBasetype){ //sql server
			  query.append(" and (t.twfBaseProcessfile.pfProStartor is null or t.twfBaseProcessfile.pfProStartor='' ");
			}else{
				 query.append(" and (t.twfBaseProcessfile.pfProStartor is null ");
			}
			if (!postorgs.isEmpty()) {
				for (int i = 0; i < postorgs.size(); i++) {
					String args[] = (String[]) postorgs.get(i);
					query.append(
							" or t.twfBaseProcessfile.pfProStartor like '%p")
							.append(args[1]).append("$").append(args[0])
							.append(",%'");
				}
			}
			if (!userOrgIdList.isEmpty()) {
				for (int i = 0; i < userOrgIdList.size(); i++) {
					String userOrgId = (String) userOrgIdList.get(i);
					query.append(
									" or t.twfBaseProcessfile.pfProStartor like '%u")
							.append(userInfo.getUserId())
							.append("$")
							.append(userOrgId)
							.append(
									",%' or t.twfBaseProcessfile.pfProStartor like '%o")
							.append(userOrgId).append(",%'");
				}
			}
			query.append(") order by t.twfInfoProcessType.ptId asc ");
			list = workflowDesign.getDataByHql(query.toString(),
				new Object[0]);
			StringBuilder hqlSelectItem = new StringBuilder();
		    	StringBuilder hqlFromItem = new StringBuilder();
		    	StringBuilder hqlWhereItem = new StringBuilder();
		    	StringBuilder hqlGroupByItem = new StringBuilder();
		    	StringBuilder hqlOrderByItem = new StringBuilder();
		    	hqlSelectItem.append(" tbw.wfName, max(tbw.wfPdid) ");
		    	hqlFromItem.append(" TwfBaseWorkflow tbw ");
		    	hqlWhereItem.append(" tbw.twfBaseProcessfile.pfIsDelete='0' and tbw.twfBaseProcessfile.pfIsDeploy='1' ");
		    	
				if(dateBasetype){ //sql server
		        	hqlWhereItem.append(" and (tbw.twfBaseProcessfile.pfProStartor is null or tbw.twfBaseProcessfile.pfProStartor='' ");
				}else{
					hqlWhereItem.append(" and (tbw.twfBaseProcessfile.pfProStartor is null ");
				}
			if (!postorgs.isEmpty()) {
				for (int i = 0; i < postorgs.size(); i++) {
					String args[] = (String[]) postorgs.get(i);
					hqlWhereItem.append(
							" or tbw.twfBaseProcessfile.pfProStartor like '%p")
							.append(args[1]).append("$").append(args[0])
							.append(",%'");
				}
			}
			if (!userOrgIdList.isEmpty()) {
				for (int i = 0; i < userOrgIdList.size(); i++) {
					String userOrgId = (String) userOrgIdList.get(i);
					hqlWhereItem.append(" or tbw.twfBaseProcessfile.pfProStartor like '%u")
								.append(userInfo.getUserId())
								.append("$")
								.append(userOrgId)
								.append(",%' or tbw.twfBaseProcessfile.pfProStartor like '%o")
								.append(userOrgId).append(",%'");
				}
			}
			hqlWhereItem.append(" ) ");
			hqlGroupByItem.append(" tbw.wfName ");
			hqlOrderByItem.append("  tbw.wfName desc ");
			if (workflowType != null && !"".equals(workflowType)) {
			    	/*
			    	 * modify yanjian 2012-04-25 17:37 
			    	 * 解决：新建流程时，流程C曾经流程类型是A，现在流程类型是B，当查询流程类型为A的数据时，显示出了流程C
			    	 * 
			    	 * 先查询所有流程对应的最新流程类型，得到数据组【A组】，在查找指定流程对应的最新流程类型，得到数据组【B组】，
			    	 * 循环取出【B组】数据 ，逐一和【A组】数据匹配WF_PDID是否相等，如果不相等不显示此数据，否则，显示该数据
			    	 * */
			    	StringBuilder allHQL = new StringBuilder()
                    			    	.append(" select ").append(hqlSelectItem)
	                    				.append(" from ").append(hqlFromItem)
	                    				.append(" where ").append(hqlWhereItem)
	                    				.append("  group by  ").append(hqlGroupByItem)
	                    				.append(" order by ").append(hqlOrderByItem);
			    	List allHQLList =  workflowDesign.getDataByHql(allHQL.toString(),
					new Object[0]);
			    	Map<String,String> mapTemp = new HashMap<String, String>();
			    	for(Object obj : allHQLList){
			    	    Object[] objarr = (Object[])obj;
			    	    mapTemp.put(StringUtil.castString(objarr[0]), StringUtil.castString(objarr[1]));
			    	}
				String[] workflowTypes = workflowType.split(",");
				StringBuilder strType = new StringBuilder();
				for (String type : workflowTypes) {
					strType.append(type).append(",");
				}
				strType.deleteCharAt(strType.length() - 1);
				hqlWhereItem.append(" and tbw.twfInfoProcessType.ptId in(" + strType
						+ ")");
				StringBuilder workflowTypeHQL = new StringBuilder()
						.append(" select ").append(hqlSelectItem)
						.append(" from ").append(hqlFromItem)
						.append(" where ").append(hqlWhereItem)
						.append("  group by  ").append(hqlGroupByItem)
						.append(" order by ").append(hqlOrderByItem);
				List workflowTypeHQLResult = workflowDesign.getDataByHql(workflowTypeHQL.toString(),
					new Object[0]);
				for(Object obj:workflowTypeHQLResult){
				    if(hqlResult == null){
					hqlResult = new LinkedList();
				    }
				    Object[] objarr = (Object[])obj;
				    String pfname = StringUtil.castString(objarr[0]);
				    String wf_pdid = StringUtil.castString(objarr[1]);
				    String tempwf_pdid = mapTemp.get(pfname);
				    if(tempwf_pdid.equals(wf_pdid)){
					hqlResult.add(obj);
				    }
				}
			}else{
				StringBuilder hql = new StringBuilder()
						.append(" select ").append(hqlSelectItem)
						.append(" from ").append(hqlFromItem)
						.append(" where ").append(hqlWhereItem)
						.append("  group by  ").append(hqlGroupByItem)
						.append(" order by ").append(hqlOrderByItem);
				hqlResult = workflowDesign.getDataByHql(hql.toString(),
					new Object[0]);
			}
			if (hqlResult != null && !hqlResult.isEmpty()) {
			    hqlResult = new ArrayList(hqlResult);
			}
//			StringBuilder hql = new StringBuilder(
//					"select tbw.wfName, max(tbw.wfPdid) from TwfBaseWorkflow tbw where ");
//			hql
//					.append("tbw.twfBaseProcessfile.pfIsDelete='0' and tbw.twfBaseProcessfile.pfIsDeploy='1'");
//			if (workflowType != null && !"".equals(workflowType)) {
//				String[] workflowTypes = workflowType.split(",");
//				StringBuilder strType = new StringBuilder();
//				for (String type : workflowTypes) {
//					strType.append(type).append(",");
//				}
//				strType.deleteCharAt(strType.length() - 1);
//				hql.append(" and tbw.twfInfoProcessType.ptId in(" + strType
//						+ ")");
//			}
//			hql.append(" and (tbw.twfBaseProcessfile.pfProStartor is null ");
//			if (!postorgs.isEmpty()) {
//				for (int i = 0; i < postorgs.size(); i++) {
//					String args[] = (String[]) postorgs.get(i);
//					hql.append(
//							" or tbw.twfBaseProcessfile.pfProStartor like '%p")
//							.append(args[1]).append("$").append(args[0])
//							.append(",%'");
//				}
//			}
//			if (!userOrgIdList.isEmpty()) {
//				for (int i = 0; i < userOrgIdList.size(); i++) {
//					String userOrgId = (String) userOrgIdList.get(i);
//					hql
//							.append(
//									" or tbw.twfBaseProcessfile.pfProStartor like '%u")
//							.append(userInfo.getUserId())
//							.append("$")
//							.append(userOrgId)
//							.append(
//									",%' or tbw.twfBaseProcessfile.pfProStartor like '%o")
//							.append(userOrgId).append(",%'");
//				}
//			}
//			hql.append(") group by tbw.wfName order by tbw.wfName desc");
//			List hqlResult = workflowDesign.getDataByHql(hql.toString(),
//					new Object[0]);
			Map<String, Object[]> map = new LinkedHashMap<String, Object[]>();
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					map.put(objs[0].toString(), new Object[] { objs[1],
							objs[2], objs[3], objs[4]});
				}
			}
			if (hqlResult != null) {
				List<EForm> eformList = sendDocManager.getFormList();
				List<EForm> jspeformList = sendDocManager.getJspFormList();//jsp列表
				Map<String, String> eformMap = new HashMap<String, String>();
				for (EForm eform : eformList) {
					eformMap.put(String.valueOf(eform.getId()), null);
				}
				for (EForm eform : jspeformList) {
					eformMap.put(String.valueOf(eform.getId()), null);
				}
				for (int j = 0; j < hqlResult.size(); j++) {
					Object[] objs = (Object[]) hqlResult.get(j);
					Object id = objs[1];
					objs = map.get(id.toString());
					if (objs != null) {
						Object formId = objs[3];
						if (formId != null
								&& eformMap.containsKey(formId.toString())) {
							result.add(objs);
						}
					}
				}
				logger.info("clear:{}...", eformMap.keySet());
				eformMap.clear();
			}
		} catch (Exception e) {
			logger.error("getStartWorkflow()", e);
		}
		return result;
	}

	/**
	* @description 获取当前用户有权限启动的流程信息
	* @method getStartProcess
	* @author 申仪玲(shenyl)
	* @created 2012-7-16 上午11:13:35
	* @param workflowType
	* @return List<Object[]{流程名称,流程类型id,流程类型名称,流程启动保单id,流程别名}>
	* @throws WorkflowException
	* @return List
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	public List getStartProcess() throws WorkflowException {
		User userInfo = userService.getCurrentUser();
		List postorgs = uupInterface.getPostOrgList(userInfo.getUserId());
		List userOrgIdList = uupInterface.getDepartmentByUser(userInfo
				.getUserId());
		List result = new LinkedList();
		List list = null;
		List hqlResult = null;
		try {

			StringBuilder query = new StringBuilder("select t.wfPdid, t.wfName,t.twfInfoProcessType.ptId");
			query.append(",t.twfInfoProcessType.ptName,t.twfBaseProcessfile.pfMainformId,t.twfBaseProcessfile.rest2 from TwfBaseWorkflow t where ");
			query.append("t.twfBaseProcessfile.pfIsDelete='0' and t.twfBaseProcessfile.pfIsDeploy='1' and t.twfBaseProcessfile.pfMainformId !='0'");
			//是否为MicrosoftSQL
			Boolean dateBasetype = DataBaseUtil.isMicrosoftSQL();
			if(dateBasetype){ //sql server
			 query.append(" and (t.twfBaseProcessfile.pfProStartor is null or t.twfBaseProcessfile.pfProStartor='' ");
			}else{
				 query.append(" and (t.twfBaseProcessfile.pfProStartor is null ");
			}
			if (!postorgs.isEmpty()) {
				for (int i = 0; i < postorgs.size(); i++) {
					String args[] = (String[]) postorgs.get(i);
					query.append(
							" or t.twfBaseProcessfile.pfProStartor like '%p")
							.append(args[1]).append("$").append(args[0])
							.append(",%'");
				}
			}
			if (!userOrgIdList.isEmpty()) {
				for (int i = 0; i < userOrgIdList.size(); i++) {
					String userOrgId = (String) userOrgIdList.get(i);
					query.append(" or t.twfBaseProcessfile.pfProStartor like '%u")
						.append(userInfo.getUserId())
						.append("$")
						.append(userOrgId)
						.append(",%' or t.twfBaseProcessfile.pfProStartor like '%o")
						.append(userOrgId).append(",%'");
				}
			}
			query.append(") order by t.twfInfoProcessType.ptId asc ");
			list = workflowDesign.getDataByHql(query.toString(),
				new Object[0]);
			StringBuilder hqlSelectItem = new StringBuilder();
		    	StringBuilder hqlFromItem = new StringBuilder();
		    	StringBuilder hqlWhereItem = new StringBuilder();
		    	StringBuilder hqlGroupByItem = new StringBuilder();
		    	StringBuilder hqlOrderByItem = new StringBuilder();
		    	hqlSelectItem.append(" tbw.wfName, max(tbw.wfPdid) ");
		    	hqlFromItem.append(" TwfBaseWorkflow tbw ");
		    	hqlWhereItem.append(" tbw.twfBaseProcessfile.pfIsDelete='0' and tbw.twfBaseProcessfile.pfIsDeploy='1' ");
		    	
				if(dateBasetype){ //sql server
		    	  hqlWhereItem.append(" and (tbw.twfBaseProcessfile.pfProStartor is null or tbw.twfBaseProcessfile.pfProStartor='' ");
				}else{
					 hqlWhereItem.append(" and (tbw.twfBaseProcessfile.pfProStartor is null");
				}
			
			if (!postorgs.isEmpty()) {
				for (int i = 0; i < postorgs.size(); i++) {
					String args[] = (String[]) postorgs.get(i);
					hqlWhereItem.append(" or tbw.twfBaseProcessfile.pfProStartor like '%p")
								.append(args[1]).append("$").append(args[0])
								.append(",%'");
				}
			}
			if (!userOrgIdList.isEmpty()) {
				for (int i = 0; i < userOrgIdList.size(); i++) {
					String userOrgId = (String) userOrgIdList.get(i);
					hqlWhereItem
							.append(" or tbw.twfBaseProcessfile.pfProStartor like '%u")
							.append(userInfo.getUserId())
							.append("$")
							.append(userOrgId)
							.append(",%' or tbw.twfBaseProcessfile.pfProStartor like '%o")
							.append(userOrgId).append(",%'");
				}
			}
			hqlWhereItem.append(" ) ");
			hqlGroupByItem.append(" tbw.wfName ");
			hqlOrderByItem.append("  tbw.wfName desc ");
			StringBuilder hql = new StringBuilder()
					.append(" select ").append(hqlSelectItem)
					.append(" from ").append(hqlFromItem)
					.append(" where ").append(hqlWhereItem)
					.append("  group by  ").append(hqlGroupByItem)
					.append(" order by ").append(hqlOrderByItem);
			hqlResult = workflowDesign.getDataByHql(hql.toString(), new Object[0]);
			if (hqlResult != null && !hqlResult.isEmpty()) {
			    hqlResult = new ArrayList(hqlResult);
			}
			Map<String, Object[]> map = new LinkedHashMap<String, Object[]>();
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					if(objs[5] == null){
						objs[5] = objs[1];
					}
					map.put(objs[0].toString(), new Object[] { objs[1],
							objs[2], objs[3], objs[4], objs[5] });
				}
			}
			if (hqlResult != null) {
				List<EForm> eformList = sendDocManager.getFormList();
				Map<String, String> eformMap = new HashMap<String, String>();
				for (EForm eform : eformList) {
					eformMap.put(String.valueOf(eform.getId()), null);
				}
				for (int j = 0; j < hqlResult.size(); j++) {
					Object[] objs = (Object[]) hqlResult.get(j);
					Object id = objs[1];
					objs = map.get(id.toString());
					if (objs != null) {
						Object formId = objs[3];
						if (formId != null
								&& eformMap.containsKey(formId.toString())) {
							result.add(objs);
						}
					}
				}
				logger.info("clear:{}...", eformMap.keySet());
				eformMap.clear();
			}
		} catch (Exception e) {
			logger.error("getStartProcess()", e);
		}
		return result;
	}

	/*
	 * @see com.strongit.oa.common.workflow.IWorkflowService#getDraftWorkflow(java
	 *      .lang.String)
	 * 
	 * @method getDraftWorkflow @author 申仪玲 @created 2011-11-7 上午10:26:50
	 * 
	 * @description 描述 @return List<Object[]{流程名称,流程类型id,流程类型名称,流程启动保单id}>
	 */
	@SuppressWarnings("unchecked")
	public List getDraftWorkflow(String workflowType) throws WorkflowException {
		List result = new LinkedList();
		try {
			StringBuilder hql = new StringBuilder("select tbw.wfName, max(tbw.wfPdid) from TwfBaseWorkflow tbw where ");
			hql.append("tbw.twfBaseProcessfile.pfIsDelete='0' and tbw.twfBaseProcessfile.pfIsDeploy='1'");
			if (workflowType != null && !"".equals(workflowType)) {
				String[] workflowTypes = workflowType.split(",");
				StringBuilder strType = new StringBuilder();
				for (String type : workflowTypes) {
					strType.append(type).append(",");
				}
				strType.deleteCharAt(strType.length() - 1);
				hql.append(" and tbw.twfInfoProcessType.ptId in(" + strType
						+ ")");
			}
			hql.append(" and (tbw.twfBaseProcessfile.pfProStartor is null ");
			hql.append(") group by tbw.wfName order by tbw.wfName desc");
			List hqlResult = workflowDesign.getDataByHql(hql.toString(),
					new Object[0]);
			Map<String, Object[]> map = new LinkedHashMap<String, Object[]>();
			if (hqlResult != null) {
				List<EForm> eformList = sendDocManager.getFormList();
				Map<String, String> eformMap = new HashMap<String, String>();
				for (EForm eform : eformList) {
					eformMap.put(String.valueOf(eform.getId()), null);
				}
				for (int j = 0; j < hqlResult.size(); j++) {
					Object[] objs = (Object[]) hqlResult.get(j);
					Object id = objs[1];
					objs = map.get(id.toString());
					if (objs != null) {
						Object formId = objs[3];
						if (formId != null
								&& eformMap.containsKey(formId.toString())) {
							result.add(objs);
						}
					}
				}
				logger.info("clear:{}...", eformMap.keySet());
				eformMap.clear();
			}
		} catch (Exception e) {
			logger.error("getDraftWorkflow()", e);
		}
		return result;
	}

	/**
	 * 获取所有类别下的所有版本的流程
	 * 
	 * @author:邓志城
	 * @date:2009-5-21 上午09:48:21
	 * @return
	 * @throws WorkflowException
	 * @see {工作处理->高级查询，工作流管理->流程监控}
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getPDInstanceList() throws WorkflowException {
		// return workflowDesign.getPDInstanceList();
		return null;
	}

	/**
	 * 查找某流程下的所有流程实例列表
	 * 
	 * @author:邓志城
	 * @date:2009-5-21 上午11:06:28
	 * @param page
	 * @param processId
	 * @return
	 * @throws WorkflowException
	 */
	public Page getInstanceByProcessId(Page page, String processId)
			throws WorkflowException {
		return workflowDesign.getProcessInstancePageByDefinitionId(page,
				processId);
	}

	/**
	 * 获取表单关联的工作流
	 * 
	 * @param formId
	 *            表单ID
	 * @return List<Object[]> 表单列表 List<[流程名称, 流程定义ID]>
	 */
	@SuppressWarnings("deprecation")
	public List<Object[]> getFormRelativeWorkflow(String formId)
			throws WorkflowException {
		List<Object[]> list = null;
		try {
			list = workflowDesign.getFormRelativeProcess(formId);
		} catch (WorkflowException ex) {
			throw new WorkflowException(ex);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> parseToSelectActors(String[] actor, String taskId) {
		Map userMap = new LinkedHashMap(0);
		List lst = this.workflowConfigService.getActorSettings(null, null, null, "1");
		if ((actor != null) && (actor.length != 0) && (lst != null) && (!(lst.isEmpty()))) {
			AbstractActorSetting[] aas = new AbstractActorSetting[lst.size()];
			for (int i = 0; i < lst.size(); ++i) {
				TwfBaseActorSetting as = (TwfBaseActorSetting) lst.get(i);
				aas[i] = ((AbstractActorSetting) ServiceLocator.getService(as
						.getAsHandlerClass()));
			}
			Set[] actorSet = new Set[lst.size()];
			for (int i = 0; i < actorSet.length; ++i) {
				actorSet[i] = new HashSet(0);
			}
			for (String actorInfo : actor) {
				for (int i = 0; i < aas.length; ++i) {
					if (aas[i].match(actorInfo)) {
						actorSet[i].add(actorInfo);
					}
				}
			}
			for (int i = 0; i < aas.length; ++i) {
				userMap.putAll(aas[i].parseActors(actorSet[i], taskId));
			}
		}
		return new ArrayList(userMap.values());
	}

	/**
	 * 通过任务节点ID得到任务节点默认设置人员信息
	 * 
	 * @param nodeId
	 *            任务节点ID(为0表示是任务指派人员，否则是任务委派人员)
	 * @param taskId
	 *            任务ID
	 * @return List
	 * @modify 增加参数 transitionId 用于支持在迁移线上选择人员 [人员ID，人员名称，组织机构ID]
	 */
	@SuppressWarnings("unchecked")
	public List getTaskActorsByTask(String nodeId, String taskId,
			String transitionId) throws WorkflowException {
		// 优先读取迁移线上设置的人员
		List list = null;
		if (transitionId != null && !"".equals(transitionId)) {
			TwfBaseTransitionPlugin plugin = getTransitionPluginByTsId(
					transitionId, "plugins_handleactor");
			if (plugin != null) {
				String actors = plugin.getValue();
				if (actors != null && !"".equals(actors)) {
					String[] actor = ((actors != null) && (!("".equals(actors)))) ? actors.split("\\|") : null;
					list = parseToSelectActors(actor, taskId);
					logger.info("找到迁移线上的人员设置信息 :" + actors);
				}
			}
		}
		if (list == null) {
			list = workflowRun.getToSelectActorsForTasknode(nodeId, taskId);
		}
		if (list != null && list.size() == 1) {// 只有一个人时不排序
			return list;
		}
		final HashMap<String, String> userIdMapSequeue = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			if (objs[0] == null) {// modify yanjian 2011-10-29 11:20
				// objs[0]为空处理
				System.out.println("TTTT");
				continue;
			}
			String userId = objs[0].toString();
			if(!userIdMapSequeue.containsKey(userId)){
				userIdMapSequeue.put(userId, objs[3].toString());
			}
		}
		logger.info("按人员排序号顺序排序...");
		// 按人员排序
		Collections.sort(list, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				String usersequeue1 = userIdMapSequeue.get(o1[0]);
				String usersequeue2 = userIdMapSequeue.get(o2[0]);
				Long key1;
				if (usersequeue1 != null) {
					key1 = Long.valueOf(usersequeue1);
				} else {
					key1 = Long.MAX_VALUE;
				}
				Long key2;
				if (usersequeue2 != null) {
					key2 = Long.valueOf(usersequeue2);
				} else {
					key2 = Long.MAX_VALUE;
				}
				return key1.compareTo(key2);
			}
		});
		// 销毁数据
		userIdMapSequeue.clear();
		return list;
	}

	/**
         * 通过流程名称得到流程开始节点的下一步分支列表
         * 
         * @author 严建
         * @param workflowname -
         *                -流程名称
         * @return List 下一步转移信息 List中数据结构为： Object[]{(0)转移名称, (1)转移Id, (2)并发标识,
         *         (3)节点设置信息} 其中“节点设置信息”为Set集合 并发标识为：0、正常模式；1、并发模式 节点设置信息数据结构
         *         需要动态设置：activeSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
         *         子流程中需要动态设置：subactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
         *         父流程中需要动态设置：supactiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
         *         不需要动态设置：notActiveSet|节点Id|节点名称|是否曾被处理过(0:否、1:是)
         *         下一步骤是条件节点：decideNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
         *         下一步骤是子流程节点：subProcessNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
         *         下一步骤是结束节点：endNode|节点Id|节点名称|是否曾被处理过(0:否、1:是)
         * @throws com.strongit.workflow.exception.WorkflowException
         * @createTime Apr 13, 2012 9:57:13 AM
         */
	public java.util.List getStartNodeTransitions(java.lang.String workflowname)
	    throws com.strongit.workflow.exception.WorkflowException {
	return workflowRun.getStartNodeTransitions(workflowname);
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
    public java.lang.String startProcessToFistNode(java.lang.Long formId,
	    java.lang.String workflowname, java.lang.String businessId,
	    java.lang.String userId, java.lang.String businessname,
	    java.lang.String[] taskActorSet, java.lang.String transitionName,
	    java.lang.String submitOption)
	    throws com.strongit.workflow.exception.WorkflowException {
	String instanceId = workflowDelegation.startProcessToFistNode(formId, workflowname,
		businessId, userId, businessname, taskActorSet, transitionName,
		submitOption);
//	  <!-- oa 业务
	User user = userService.getCurrentUser();
	StartProcessToFistNodeBean bean= new StartProcessToFistNodeBean();
	bean.setProcessInstanceId(instanceId);
	bean.setMainActorId(user.getUserId());
	bean.setMainActorName(user.getUserName());
	baseExtendManager.startProcessToFistNodeExtend(bean);
	//-->
	return instanceId;
    }
	/**
         * 启动流程，添加业务并新建保存流程实例 并返回流程实例 modifyer: lanlc
         * 
         * @param formId
         *                表单ID
         * @param workflowName
         *                工作流名称
         * @param userName
         *                用户名称
         * @param bussinessId
         *                业务ID
         * @param bussinessName
         *                业务名称
         * @param taskActors
         *                流程下一步处理人信息([人员ID|节点ID，人员ID|节点ID……])
         * @param tansitionName
         *                拟稿任务完成后的路径名称,取值可能为模型路径名称、全部并发、选择并发
         * @param concurrentTans
         *                当transitionName为“选择并发”时选择的路径
         * @param suggestion
         *                流程启动时输入的提交意见
         * @return String
         */
	public String startWorkflow(String formId, String workflowName,
			String userName, String bussinessId, String bussinessName,
			String[] taskActors, String tansitionName, String concurrentTans,
			String suggestion) throws WorkflowException {
		if (formId == null || "".equals(formId)) {// 未使用表单的情况.
			formId = "0";
		}
		String instanceId = workflowDelegation.startProcess(Long.valueOf(formId),
				workflowName, bussinessId, userName, bussinessName, taskActors,
				tansitionName, suggestion);
		//<!-- oa 业务
		User user = userService.getCurrentUser();
		StartWorkflowBean bean= new StartWorkflowBean();
		bean.setProcessInstanceId(instanceId);
		bean.setMainActorId(user.getUserId());
		bean.setMainActorName(user.getUserName());
		baseExtendManager.startWorkflowExtend(bean);
		//-->
		return instanceId;
	}

	/**
	 * 得到当前用户相应类别的任务列表, 修改成挂起任务及指派委托状态也查找出来，在展现层展现状态
	 * 增加查询条件参数,添加流程类型参数processType，以区分发文、收文等
	 * 
	 * @param page
	 *            分页对象
	 * @param userId
	 *            用户Id
	 * @param searchType
	 *            搜索类型: 1)doing:在办 2)todo:待办 3) all:不区分待办和在办
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
	 * @return Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起 ,
	 *         (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型}
	 * 
	 * 
	 * Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起 ,
	 * (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)任务是否是回退任务(“0”：否；“1”：是),(10)委托类型(“0
	 * ”：委托；“1”：指派)}
	 */
	@SuppressWarnings("unchecked")
	public Page getTasksTodo(Page page, String userId, String searchType,
			String workflowType, String businessName, String userName,
			Date startDate, Date endDate, String isBackSpace)
			throws WorkflowException {
		// page = workflowRun.getTodoTaskInfoByUserId(page,
		// userId,
		// searchType,
		// workflowType,
		// businessName,
		// userName,
		// startDate,
		// endDate,
		// isBackSpace);
		Object[] toSelectItems = { "taskId", "taskStartDate", "taskName",
				"processInstanceId", "processStartDate", "processSuspend",
				"businessName", "startUserName", "processTypeId",
				"isBackspace", "assignType" };
		List sItems = Arrays.asList(toSelectItems);
		if ("todo".equals(searchType)) {
			searchType = "0";
		} else if ("doing".equals(searchType)) {
			searchType = "1";
		} else {
			searchType = "2";
		}
		Map paramsMap = new HashMap<String, Object>();
		paramsMap.put("processSuspend", "0");// 取非挂起任务
		if (null != userId) {
			paramsMap.put("handlerId", userId);
		}
		paramsMap.put("toAssignHandlerId", userId);
		if (null != searchType) {
			paramsMap.put("taskType", searchType);
		}
		if (null != workflowType && !"".equals(workflowType)) {
			String[] workflowTypes = workflowType.split(",");
			List<String> lType = new ArrayList<String>();
			for (String tp : workflowTypes) {
				lType.add(tp);
			}
			paramsMap.put("processTypeId", lType);
		}
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
			page = workflowRun.getTaskInfosByConditionForPage(page, sItems,
					paramsMap, orderMap, null, null, "", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	/**
	 * 流程查询
	 * 
	 * @author zhengzb
	 * @desc 2010-11-24 下午03:16:06
	 * @param page
	 *            分页对象
	 * @param paramsMap
	 *            查询条件设置 paramsMap的定义 new HashMap<String, Object>();
	 *            数据添加例如：paramsMap.put("processTypeId", workflowType);
	 * 
	 * @return[
	 * <p>
	 * 
	 * Object[]{启动时间,发起人ID，发起人姓名，流程名称，业务名称，任务节点ID，任务节点名，任务开始时间，任务结束时间，结束时间
	 * ，任务ID，
	 * </p>
	 * <p>
	 * 任务节点对应表单Id，流程实例Id，流程对应主表单ID，任务节点对应表单业务数据标识，流程对应主表单业务数据标识,流程类型名称}
	 * </p>
	 * @throws WorkflowException
	 */
	@SuppressWarnings("unchecked")
	public Page getSearchWorkflowByPage(Page page, Map paramsMap)
			throws WorkflowException {
		// toSelectItems - -要查询的属性
		// 启动时间,发起人ID，发起人姓名，流程名称，业务名称，任务节点ID，任务节点名，任务开始时间，任务结束时间，结束时间，任务ID，任务节点对应表单Id，流程实例Id，流程对应主表单ID，任务节点对应表单业务数据标识，流程对应主表单业务数据标识,流程类型名称
		String[] toSelectItems = { "processStartDate", "startUserId",
				"startUserName", "processName", "businessName", "taskNodeId",
				"taskNodeName", "taskStartDate", "taskEndDate",
				"processEndDate", "taskId", "taskFormId", "processInstanceId",
				"processMainFormId", "taskFormBusinessId",
				"processMainFormBusinessId", "processTypeName" };
		List sItems = Arrays.asList(toSelectItems);
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("processStartDate", "1");
		try {
			page = this.workflowRun.getTaskInfosByConditionForPage(page,
					sItems, paramsMap, orderMap, "", "", "", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
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
	 * @param endDate
	 *            结束时间 内嵌数据对象结构：<br> [
	 *            <p>
	 *            Object[]{任务实例Id,任务创建时间,任务名称,流程实例Id,流程创建时间,任务是否被挂起,
	 *            </p>
	 *            <p>
	 *            业务名称,发起人名称,流程类型Id,任务前一处理人名称,任务转移类型}
	 *            </p> ]
	 * 
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getTasksProcessed方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Page getTasksProcessed(Page page, String username,
			String workflowType, String businessName, String userName,
			Date startDate, Date endDate, String state)
			throws WorkflowException {
		Object[] toSelectItems = { "taskId", "taskStartDate", "taskName",
				"processInstanceId", "processStartDate", "processSuspend",
				"businessName", "startUserName", "processTypeId",
				"isBackspace", "assignType", "processEndDate",
				"processMainFormBusinessId" };
		List sItems = Arrays.asList(toSelectItems);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("handlerId", username);
		paramsMap.put("processTypeId", workflowType);
		paramsMap.put("toAssignHandlerId", username);
		paramsMap.put("taskType", "3");
		if (businessName != null && !"".equals(businessName)) {
			paramsMap.put("businessName", businessName);
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
			// page =
			// this.workflowDelegation.getProcessInstanceByConditionForPage(page,
			// sItems, paramsMap, orderMap,"","","",null);
			// page = workflowRun.getTaskInfosByConditionForPage(page, sItems,
			// paramsMap, orderMap, null, null, "", null);
			// page = workflowRun.getTaskInfosByConditionForPage(page, sItems,
			// paramsMap, orderMap,"","","",null);
			// 过滤掉重复的已办记录
			List<Object[]> listWorkflow = workflowRun.getTaskInfosByConditionForList(sItems, paramsMap, orderMap, "", "", "", null);
			System.out.println("before === " + listWorkflow.size());
			List dlist = new ArrayList();// 存储非重复的任务
			List<String> checkList = new ArrayList<String>();// 处理重复的记录
			if (listWorkflow != null && !listWorkflow.isEmpty()) {
				for (int i = 0; i < listWorkflow.size(); i++) {
					Object[] objs = (Object[]) listWorkflow.get(i);
					String businessId = (String) objs[12];
					if (!checkList.contains(businessId)) {
						dlist.add(objs);
						checkList.add(businessId);
					}
				}
			}
			System.out.println("after == " + dlist.size());
			page = ListUtils.splitList2Page(page, dlist);
			// ------------ End -------------------
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
		// Page returnPage = workflowRun.getDoneTaskInfoByUserId(page,
		// username,
		// workflowType,
		// businessName,
		// userName,
		// startDate,
		// endDate);
		// return returnPage;
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
	 *            结束时间 *
	 * @param state
	 *            流程当前状态 [
	 *            <p>
	 *            Object[]{主键Id,流程实例Id,流程创建时间,流程当前状态,
	 *            </p>
	 *            <p>
	 *            业务名称,发起人Id,发起人名称,流程类型Id,流程实例结束时间,主办业务Id,表单Id}
	 *            </p> ]
	 * 
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getTasksHostedBy方法中添加 State 状态 查询参数 为已审公文和已办来文 添加状态栏
	 * 
	 * （*注：返回的流程当前状态，为流程结束时间；当流程结束时间不为空时，流程已经办结；为空时，流程在办）
	 */
	@SuppressWarnings("unchecked")
	public Page getTasksHostedBy(Page page, String username,
			String workflowType, String businessName, Date startDate,
			Date endDate, String state) throws WorkflowException {
		String[] toSelectItems = { "processStartDate", "processInstanceId",
				"processStartDate", "processEndDate", "businessName",
				"startUserId", "startUserName", "processTypeId",
				"processEndDate", "processMainFormBusinessId",
				"processMainFormId" };
		// String[] toSelectItems =
		// {"processMainFormBusinessId","processInstanceId","processStartDate","businessName","startUserId","startUserName","processTypeId","processEndDate","taskId","processMainFormId"};
		List sItems = Arrays.asList(toSelectItems);
		Map paramsMap = new HashMap<String, Object>();
		paramsMap.put("processTypeId", workflowType);
		paramsMap.put("handlerId", username);
		if (null != username && !"".equals(username)) {
			paramsMap.put("startUserId", username);
		}
		if (null != businessName && !"".equals(businessName)) {
			paramsMap.put("businessName", businessName);
		}
		if (null != startDate) {
			paramsMap.put("processStartDateStart", startDate);
		}
		if (null != endDate) {
			paramsMap.put("processStartDateEnd", endDate);
		}
		if (state != null && !"".equals(state)) {
			paramsMap.put("processStatus", state);
		}
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("processStartDate", "1");
		try {
			page = this.workflowDelegation
					.getProcessInstanceByConditionForPage(page, sItems,
							paramsMap, orderMap, "", "", "", null);
			// page = workflowRun.getTaskInfosByConditionForPage(page, sItems,
			// paramsMap, orderMap,"","","",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
		// Page returnPage = oaSystemService.getStartProcessInfoByUserId(page,
		// username,
		// workflowType,
		// businessName,
		// startDate,
		// endDate);
		// return returnPage;
	}

	/**
	 * 根据要查询的属性、查询条件和排序属性得到符合条件的流程实例分页列表信息
	 * 
	 * @author 喻斌
	 * @date Jan 20, 2010 3:33:12 PM
	 * @param page
	 *            -分页列表
	 * @param toSelectItems
	 *            -要查询的属性<br>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 * @param paramsMap
	 *            -查询条件设置<br>
	 *            <p>
	 *            “processName”：流程名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDateStart”：启动时间下限
	 *            </p>
	 *            <p>
	 *            “processStartDateEnd”：启动时间上限
	 *            </p>
	 *            <p>
	 *            “processEndDateStart”：结束时间下限
	 *            </p>
	 *            <p>
	 *            “processEndDateEnd”：结束时间上限
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processStatus”：流程状态（“0”：待办；“1”：办毕）
	 *            </p>
	 * @param orderMap
	 *            -要排序的属性（“0”：升序；“1”：降序）<br>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 * @return Page<Object[]>
	 * @throws WorkflowException
	 */
	@Transactional(readOnly = true)
	public List<Object[]> getProcessInstanceByConditionForList(
			List<String> toSelectItems, Map<String, Object> paramsMap,
			Map<String, String> orderMap) throws WorkflowException {
		return workflowDelegation.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, orderMap, null, null, null, null);
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
	public List getNextTransitions(String taskId) throws WorkflowException {
		return workflowRun.getNextTransitionsByTaskId(Long.valueOf(taskId));
	}

	/**
	 * 根据迁移线id得到迁移线流转到的节点id
	 * 
	 * @param transitionId
	 *            迁移线id
	 * @return 流向的节点id
	 */
	public Long getNodeIdByTransitionId(String transitionId) {
		JbpmContext jbpmContext = getJbpmContext();
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("from org.jbpm.graph.def.Transition t where t.id=?");
			List list = workflowDelegation.getDataByHql(sql.toString(),
					new Object[] { new Long(transitionId) });
			if (list != null && !list.isEmpty()) {
				Transition transition = (Transition) list.get(0);
				return transition.getTo().getId();
			}
			return null;
		} finally {
			jbpmContext.close();
		}
	}

	/**
	 * 根据流程实例id得到流程实例对象
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 流程实例对象
	 */
	public ProcessInstance getProcessInstanceById(String instanceId) {
		JbpmContext jbpmContext = getJbpmContext();
		try {
			return jbpmContext.getProcessInstance(new Long(instanceId));
		} finally {
			jbpmContext.close();
		}
	}

	/**
	 * 根据任务实例id得到任务实例对象
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 28, 2011 1:37:44 PM
	 * @return TaskInstance
	 */
	public TaskInstance getTaskInstanceById(String instanceId) {
		JbpmContext jbpmContext = getJbpmContext();
		try {
			return jbpmContext.getTaskInstance(new Long(instanceId));
		} finally {
			jbpmContext.close();
		}
	}

	/**
	 * 根据流程实例得到流程实例上下文
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 流程实例上下文
	 */
	public ContextInstance getContextInstance(String instanceId) {
		ProcessInstance processInstance = this.getProcessInstanceById(instanceId);
		if(processInstance!=null&&!processInstance.equals("")){
			return processInstance.getContextInstance();
		}
		else{
			return null;
		}
	}
	/**
	 * 获取jbpm_processinstance中的数据，如果流程在回收站被删除
	 * @param tableName
	 * @param pkName
	 * @param pkValue
	 * @return
	 * @throws WorkflowException
	 */
	public List<Long>  getIds(String tableName,String pkName,String pkValue)throws WorkflowException{
		String result=null;
		StringBuilder hql=new StringBuilder(" select t.id from org.jbpm.graph.exe.ProcessInstance t where  " +
				"t.businessId like ? ") ; 
		String param= "%"+pkValue+"%";
		List list = null;
		list = serviceDAO.find(hql.toString(), new Object[]{param});
		return list;
	}
	/**
	 * 判断是不是草稿
	 * @param tableName
	 * @param pkName
	 * @param pkValue
	 * @return
	 * @throws WorkflowException
	 */
	public String   getDraf(String tableName,String pkName,String pkValue)throws WorkflowException{
		StringBuilder hql=new StringBuilder(" select workflowstate from "+ tableName+"  where " +pkName+"='"+pkValue+"'");
		String state=null;
		ResultSet rs = null;
		rs = serviceDAO.executeJdbcQuery(hql.toString());
		try {
			if (rs.next()) {
				state=rs.getString("workflowstate");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(list);
		return state;
	}
	/**
	 * 根据tableName，pkName，pkValue来得到流程表中的taskId 
	 * @date2014年3月3日19:46:16
	 * @param tableName
	 * @param pkName
	 * @param pkValue
	 * @return
	 */
	public List<Long>  getTaskId(String tableName,String pkName,String pkValue)throws WorkflowException{
		String result=null;
		StringBuilder hql=new StringBuilder(" select t.id from org.jbpm.taskmgmt.exe.TaskInstance t where  " +
				"t.processInstance.businessId=?  " +
				" and( t.isCancelled = '1' " +
				" or t.isSuspended = '1')");
		String param= tableName+";"+pkName+";"+pkValue;
		List list = null;
		list = serviceDAO.find(hql.toString(), new Object[]{param});  
		return list;
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
	public List getStartWorkflowTransitions(String workflowName)
			throws WorkflowException {
		return workflowRun.getFirstTaskNodeTransitions(workflowName);
	}

	/**
	 * 根据任务id或者流程名称获取意见域输入方式
	 * 
	 * @description
	 * @author 严建
	 * @param taskId	任务id
	 * @param workflowame	流程名称
	 * @return	
	 * @createTime Apr 27, 2012 11:53:18 AM
	 */
	public String getSuggestionStyle(String workflowame,String taskId){
	    StringBuilder suggestionStyle = new StringBuilder();
	    taskId = null ;workflowame = null;//目前没有对节点信息进行意见输入模式的扩展，此处暂时设置为null
	    if(taskId != null && !"".equals(taskId)){//获取产生该任务的节点信息设置
	    }else{
			if(workflowame != null && !"".equals(workflowame)){//获取流程第一个非开始节点的节点信息设置
			}else{//获取系统设置信息
			    suggestionStyle.append(systemsetManager.getSystemset().getSuggestionStyle());//系统级别设置中的意见输入模式
			}
	    }
	    return suggestionStyle.toString();
	} 
	
	/** 
	 * 是否启用笔形输入意见模式
	 * 
	 * @description
	 * @author 严建
	 * @param workflowame
	 * @param taskId
	 * @return
	 * @createTime Apr 27, 2012 12:25:58 PM
	 */
	public boolean isUserPenInSuggestionStyle(String workflowame,String taskId){
	    String suggestionStyle = getSuggestionStyle( workflowame, taskId);
	    boolean result = false;
	    if("0".equals(suggestionStyle)){
		result = true;
	    }
	    return result;
	}
	/**
	 * 流程向下一步流转
	 * 
	 * @param taskId
	 *            任务ID
	 * @param transitionName
	 *            流向名称
	 * @param returnNodeId
	 *            驳回节点ID
	 * @param businessId
	 *            表单业务数据ID
	 * @param suggestion
	 *            提交意见
	 * @param userId
	 *            人员ID
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String goToNextTransition(String taskId, String transitionName,
			String returnNodeId, String isNewForm, String formId,
			String businessId, String suggestion, String userId,
			String[] taskActors,Parameter... parameters) throws WorkflowException {
	    	boolean isHandleProcess = true;
	    	if(parameters != null){
	    	    for(Parameter parameter:parameters){
		    		if(!parameter.isHandleProcess()){
		    		    isHandleProcess  = false;
		    		    break;
		    		}
	    	    }
	    	}
		String submitType = WorkflowConst.WORKFLOW_TRANSITION_SAVE;
		if (formId == null || "".equals(formId)) {
			formId = "0";
		}
		if (userId != null) {// 流程监控做退回操作时，存在将userId赋值为空的情况 严建 2011-12-19
			if ("".equals(userId)) {
				userId = userService.getCurrentUser().getUserId();
			}
			if(isHandleProcess){//可以保存意见
			    // 13:15
			    if(isUserPenInSuggestionStyle(taskId,null)){//启用了笔形意见输入模式
				List<TwfInfoApproveinfo> lst = this.getDataByHql(
					"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
					new Object[] { new Long(taskId) });
				if (lst.isEmpty()) {// 此任务的意见不存在时，保存意见信息
				    // 保存处理意见,无指派信息
				    workflowRun.handleProcess(taskId, submitType, isNewForm,
					    new Long(formId), businessId, suggestion, userId);
				}
			    }else{
				workflowRun.handleProcess(taskId, submitType, isNewForm,
					new Long(formId), businessId, suggestion, userId);
			    }
			}
//			<!-- oa 业务
			GoToNextTransitionBean bean= new GoToNextTransitionBean();
			bean.setProcessInstanceId(getProcessInstanceIdByTiId(taskId));
			bean.setMainActorId(userId);
			bean.setMainActorName(userService.getUserNameByUserId(userId));
			baseExtendManager.goToNextTransitionExtend(bean);
			//-->
		}
		// 转到下一步
		workflowRun.forwardProcess(taskId, transitionName, returnNodeId,
				taskActors);
		return "";
	}

	/**
	 * 得到指定任务的办理意见
	 * 
	 * @param taskId
	 *            任务实例id
	 * @return 办理意见
	 */
	@SuppressWarnings("unchecked")
	public TwfInfoApproveinfo getApproveinfoByTaskId(String taskId) {
		List<TwfInfoApproveinfo> lst = this.getDataByHql(
				"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
				new Object[] { new Long(taskId) });
		if (lst.isEmpty()) {
			return null;
		}
		return lst.get(0);
	}

	@SuppressWarnings("unchecked")
	public String handleProcess(String taskId, String isNewForm, String formId,
			String businessId, String suggestion, String userId) {
		workflowRun.handleProcess(taskId,
				WorkflowConst.WORKFLOW_TRANSITION_SAVE, isNewForm, new Long(
						formId), businessId, suggestion, userId);
		List<TwfInfoApproveinfo> lst = this.getDataByHql(
				"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
				new Object[] { new Long(taskId) });
		if (lst != null && !lst.isEmpty()) {
			return lst.get(0).getAiId().toString();
		} else {
			throw new SystemException("找不到任务处理记录。");
		}
	}

	/**
	 * 任务指派处理
	 * 
	 * @param taskId
	 *            任务ID
	 * @param isAssignNeedReturn
	 *            重指派是否需要返回
	 * @param reAssignActor
	 *            重指派人员ID
	 * @param userId
	 *            人员ID
	 * @return
	 */
	public String assignTaskActor(String taskId, String reAssignActor,
			String isAssignNeedReturn, String userId) throws WorkflowException {
		workflowRun.handleReAssignTask(taskId, reAssignActor, isAssignNeedReturn, userId);
		return null;
	}

	/**
	 * 任务指派返回
	 * 
	 * @author 喻斌
	 * @date Apr 30, 2009 11:47:02 AM
	 * @param taskId
	 *            -任务实例Id
	 * @param newForm
	 *            -是否是新表单
	 * @param formId
	 *            -表单Id
	 * @param businessId
	 *            -业务数据Id
	 * @param userId
	 *            -当前处理人Id
	 * @throws WorkflowException
	 */
	public void returnReAssignTask(String taskId, String newForm, long formId,
			String businessId, String userId) throws WorkflowException {
		workflowRun.returnReAssignTask(taskId, newForm, formId, businessId,
				userId);
	}

	/**
	 * 获取工作流节点ID
	 * 
	 * @param taskId
	 *            任务ID
	 * @return String <br>
	 *         字符串格式:(调用时只会出现第三种情况) <br>
	 *         (1)"subprocess,表单路径,业务id,表单id"<br>
	 *         (2)"subaction,action设置,表单路径,业务id,表单id"<br>
	 *         (3)"form,表单路径,业务id,表单id" 注：业务ID为0时表示表单是新的表单，与之前节点不同<br>
	 *         (4)"action,action设置,表单路径,业务id,表单id"
	 */
	public String getNodeInfo(String taskId) throws WorkflowException {
		return oaSystemService.getProcessNodeSettingInfo(taskId);
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
	public void signForTask(String userId, String taskId, String flag)
			throws WorkflowException {
		workflowRun.receiveTask(userId, taskId, flag);
	}

	/**
	 * 获取流程监控数据
	 * 
	 * @param instanceId
	 * @return Object[]<br>
	 *         1、实例数据<br>
	 *         2、任务数据<br>
	 *         3、变量数据<br>
	 *         4、Token数据<br>
	 *         5、父流程数据<br>
	 *         6、子流程数据<br>
	 *         7、过程日志
	 */
	public Object[] getWorkflowMonitorData(String instanceId)
			throws WorkflowException {
		return workflowDelegation.getProcessInstanceMonitorData(Long.valueOf(instanceId));
	}

	/**
	 * 获取所有流程类型
	 * 
	 * @return List<Object[]> 流程类型信息集<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 *         Object[]{类型Id, 类型名称}
	 *         </p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getAllProcessTypeList() throws WorkflowException {
		return workflowDesign.getAllProcessTypeList();
	}

	/**
	 * 根据流程类型Id获取当前用户所拥有的所有启动表单
	 * 
	 * @param processTypeId
	 *            -流程类型Id
	 * @return List<Object[]> 启动表单信息集<br>
	 *         <p>
	 *         数据结构：
	 *         </p>
	 *         <p>
	 *         Object[]{表单Id}
	 *         </p>
	 * @throws WorkflowException
	 * 
	 * 修改人： 彭小青 修改时间： 2010-01-21 修改原因： 将表单排序
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	public List<Object[]> getRelativeFormByProcessType(String processTypeId)
			throws WorkflowException {
		List<Object[]> objlist = workflowDesign
				.getRelativeFormByProcessType(processTypeId);
		List newObjList = new ArrayList();
		String tempFormId = "";
		TefTemplateConfig config;
		String userId = configManager.getUser().getUserId();
		List<TefTemplateConfig> list = configManager.getConfigList(userId,
				processTypeId);// 琛ㄥ崟閰嶇疆
		if (list != null && list.size() > 0) { // 濡傛灉娌℃湁閰嶇疆
			for (int i = 0; i < list.size(); i++) {
				config = list.get(i);
				tempFormId = String.valueOf(config.getFormId());
				for (int j = 0; objlist != null && j < objlist.size(); j++) {
					String formId = String.valueOf(objlist.get(j));
					if (formId.equals(tempFormId)) {
						newObjList.add(objlist.get(j));
						objlist.remove(j);
					}
				}
			}
			for (int k = 0; k < objlist.size(); k++) {
				newObjList.add(objlist.get(k));
			}
			return newObjList;
		} else {
			return objlist;
		}
	}

	/**
	 * 根据流程类型按流程名称分类得到流程统计信息
	 * 
	 * @author 喻斌
	 * @date Feb 6, 2009 2:42:52 PM
	 * @param processTypeId
	 *            -流程类型Id
	 * @return List<Object[]> 流程统计信息<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 *         Object[]{流程类型Id, 流程类型名称, 待办流程数量, 已办流程数量}
	 *         </p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getProcessAnalyzeByProcessForList(String processTypeId)
			throws WorkflowException {
		return workflowDelegation
				.getProcessAnalyzeByProcessForList(processTypeId);
	}

	/**
	 * 根据流程类型按流程名称分类得到流程统计信息
	 * 
	 * @author 喻斌
	 * @date Feb 6, 2009 2:41:17 PM
	 * @param page
	 *            -分页对象
	 * @param processTypeId
	 *            -流程类型Id
	 * @return Page<Object[]> 流程统计信息<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 *         Object[]{流程类型Id, 流程类型名称, 待办流程数量, 已办流程数量}
	 *         </p>
	 * @throws WorkflowException
	 */
	@SuppressWarnings("unchecked")
	public Page<Object[]> getProcessAnalyzeByProcessForPage(
			Page<Object[]> page, String processTypeId) throws WorkflowException {
		return workflowDelegation.getProcessAnalyzeByProcessForPage(page,
				processTypeId);
	}

	/**
	 * 人工对单个任务实例进行催办
	 * 
	 * @author 喻斌
	 * @date Feb 3, 2009 4:45:54 PM
	 * @param taskInstanceId
	 *            -任务实例Id
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
	 * @throws WorkflowException
	 */
	public void urgencyTaskInstanceByPerson(String taskInstanceId,
			String noticeTitle, List<String> noticeMethod, String handlerMes)
			throws WorkflowException {
		workflowRun.urgencyTaskInstanceByPerson(taskInstanceId, noticeTitle,
				noticeMethod, handlerMes);
	}

	/**
	 * 人工对整个流程进行催办
	 * 
	 * @author 喻斌
	 * @date Feb 3, 2009 4:45:54 PM
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
	 * @throws WorkflowException
	 */
	public void urgencyProcessByPerson(String processId, String noticeTitle,
			List<String> noticeMethod, String handlerMes)
			throws WorkflowException {
		workflowDelegation.urgencyProcessByPerson(processId, noticeTitle,
				noticeMethod, handlerMes);
	}

	/**
	 * 根据流程类型Id得到该类型下用户有权限的流程信息
	 * 
	 * @author 喻斌
	 * @date Feb 16, 2009 9:13:05 AM
	 * @param processTypeId
	 *            -流程类型Id
	 * @return List<Object[]> 流程类型下有权限的流程信息集<br>
	 *         <p>
	 *         数据结构：
	 *         </p>
	 *         <p>
	 *         Object[]{流程名称, 流程Id}
	 *         </p>
	 * @throws WorkflowException
	 */
	@SuppressWarnings("deprecation")
	public List<Object[]> getProcessOwnedByProcessType(String processTypeId)
			throws WorkflowException {
		return workflowDesign.getProcessOwnedByProcessType(processTypeId);
	}

	/**
	 * author:dengzc description:取回工作任务 modifyer: description:
	 * 
	 * @param taskId
	 * @return
	 * @throws WorkflowException
	 */
	public String fetchTask(String taskId) throws WorkflowException {
		return workflowRun.fetchTask(taskId);
	}

	/**
	 * 判断目前任务是否可被当前用户处理
	 * 
	 * @param taskid
	 *            -任务实例Id
	 * @return String 返回任务实例信息<br>
	 *         <p>
	 *         f1|该任务正在被其他人处理或被挂起，请稍后再试或与管理员联系！现在是否要查看该任务信息？
	 *         </p>
	 *         <p>
	 *         f2|该任务已被取消，请查阅处理记录或与管理员联系！
	 *         </p>
	 *         <p>
	 *         f3|该任务已被其他人处理，请查阅详细处理记录！
	 *         </p>
	 *         <p>
	 *         f4
	 *         </p>
	 * @throws WorkflowException
	 */
	public String judgeTaskIsDone(String taskid) throws WorkflowException {
		return workflowRun.judgeTaskCanBeDone(taskid);
	}

	/**
	 * author:dengzc description:会签处理 modifyer: description:
	 * 
	 * @param taskId
	 * @return
	 * @throws WorkflowException
	 */
	public void resumeConSignTask(String taskId) throws WorkflowException {
		workflowRun.resumeConSignTask(taskId);
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
	public String checkCanReturn(String id) throws WorkflowException {
		return workflowRun.checkTaskCanReturn(id);
	}

	/**
	 * 开始流程时获取文档操作权限
	 * 
	 * @param workflowName
	 * @return
	 *            <p>
	 *            数据结构：
	 *            </p>
	 *            <p>
	 * 
	 * (0)导出PDF,(1)导入模板,(2)打印,(3)保存,(4)保存并关闭,(5)页面设置,(6)保留痕迹,(7)不保留痕迹,(8) 显示痕迹,
	 * </p>
	 * <p>
	 * 
	 * (9)隐藏痕迹,(10)文件套红,(11)插入图片,(12)只读,(13)加盖电子印章,(14)加盖电子印章(从服务器),(15)插入手工绘画 ,
	 * </p>
	 * <p>
	 * (16)插入手写签名,(17)全屏手工绘画,(18)全屏手写签名
	 * </p>
	 * <p>
	 * 数据说明：0、无权限；1、有权限
	 * </p>
	 * @throws WorkflowException
	 */
	public String getStartWorkflowDocumentPrivilege(String workflowName)
			throws WorkflowException {
		return documentPrivilegeService.getStartWorkflowDocumentPrivilege(workflowName);
	}
	
	/**
	 * 获取文档操作权限
	 * 
	 * @param taskId
	 * @return
	 *            <p>
	 *            数据结构：
	 *            </p>
	 *            <p>
	 * 
	 * (0)导出正文,(1)导入模板,(2)打印,(3)保存草稿,(4)保存并关闭,(5)页面设置[改为存储草稿必须保存],(6)保留痕迹
	 * ,(7)不保留痕迹,(8)显示痕迹,
	 * </p>
	 * <p>
	 * 
	 * (9)隐藏痕迹,(10)文件套红,(11)擦除痕迹,(12)只读,(13)加盖电子印章,(14)加盖电子印章(从服务器),(15)生成二维条码 ,
	 * </p>
	 * <p>
	 * (16)插入手写签名,(17)全屏手工绘画,(18)全屏手写签名
	 * </p>
	 * <p>
	 * 数据说明：0、无权限；1、有权限
	 * </p>
	 * @throws WorkflowException
	 * 
	 * 将页面设置存储是否草稿必填.dengzc 2011年7月2日10:48:01
	 */
	public String getDocumentPrivilege(TwfBaseNodesetting nodeSetting)
			throws WorkflowException {
		return documentPrivilegeService.getDocumentPrivilege(nodeSetting);
	}

	/**
	 * 根据任务实例id或流程名称得到节点设置信息,若taskId参数为空，则查询第一个节点上的设置信息。若不为空,则查询当前所在节点设置信息.
	 * 
	 * @param taskId
	 *            任务实例id
	 * @param workflowName
	 *            流程名称
	 * @return 节点对象
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public TwfBaseNodesetting findFirstNodeSetting(String taskId,
			String workflowName) throws SystemException {
		return nodeService.findFirstNodeSetting(taskId, workflowName);
	}
	
	/**
	 * 根据流程名称得到流程第一个节点的设置信息
	 * 
	 * @description
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime May 31, 2012 1:36:07 PM
	 */
	public TwfBaseNodesetting findFirstNodeSettingByWorkflowName(
			String workflowName) throws ServiceException, DAOException,
			SystemException {
		return nodeService.findFirstNodeSettingByWorkflowName(workflowName);
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
			String processInstanceId) throws WorkflowException {
		return workflowDelegation.getFormIdAndBusinessIdByProcessInstanceId(processInstanceId);
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
	 *         (5)任务处理意见, (6)任务处理时间},{7}签名信息
	 *         </p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getProcessHandleByTaskInstanceId(String taskInstanceId)
			throws WorkflowException {
		return workflowRun.getProcessHandleByTaskInstanceId(taskInstanceId);
	}

	/**
	 * 根据查询条件查找相应流程信息（OA专用）
	 * 
	 * @author 喻斌
	 * @date May 15, 2009 11:48:29 AM
	 * @param processType
	 *            -流程类型查询条件<br>
	 *            <p>
	 *            为null表示所有类型
	 *            </p>
	 * @param processStatus
	 *            -流程状态查询条件<br>
	 *            <p>
	 *            0：所有状态
	 *            </p>
	 *            <p>
	 *            1：正在执行
	 *            </p>
	 *            <p>
	 *            2：已经结束
	 *            </p>
	 * @param searchScope
	 *            -查询范围<br>
	 *            <p>
	 *            0：所有范围
	 *            </p>
	 *            <p>
	 *            1：我经办的
	 *            </p>
	 *            <p>
	 *            2：我发起的
	 *            </p>
	 *            <p>
	 *            3：指定发起人
	 *            </p>
	 * @param startDate
	 *            -开始时间查询条件<br>
	 *            <p>
	 *            为null表示不设置
	 *            </p>
	 * @param endDate
	 *            -结束时间查询条件<br>
	 *            <p>
	 *            为null表示不设置
	 *            </p>
	 * @param userId
	 *            -指定发起人Id<br>
	 *            <p>
	 *            searchScope为3时有效
	 *            </p>
	 * @param businessName
	 *            -业务名称查询条件
	 * @return List<Object[]> 流程信息集<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 * 
	 * Object[]{(0)流程实例Id,(1)业务名称,(2)开始时间,(3)结束时间,(4)流程主表单Id,(5)流程主表单业务数据标识 }
	 * </p>
	 * @throws WorkflowExceptin
	 */
	public List<Object[]> getProcessInfoByQueryCondition(String processType,
			String processStatus, String searchScope, Date startDate,
			Date endDate, String userId, String businessName)
			throws WorkflowException {
		List<String> toSelectItems = new ArrayList<String>(6);// 定义需要查询的数据
		toSelectItems.add("processInstanceId");
		toSelectItems.add("businessName");
		toSelectItems.add("processStartDate");
		toSelectItems.add("processEndDate");
		toSelectItems.add("processMainFormId");
		toSelectItems.add("processMainFormBusinessId");
		Map<String, Object> paramsMap = new HashMap<String, Object>();// 定义查询条件
		if (processType != null && !"".equals(processType)) {
			paramsMap.put("processName", processType);
		}
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("processStartDate", "1");
		return workflowDelegation.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, orderMap, null, null, null, null);
	}

	/**
	 * 根据查询条件查找相应流程信息,返回分页对象（OA专用）
	 * 
	 * @author 喻斌
	 * @date May 15, 2009 11:48:29 AM
	 * @param page
	 *            -分页对象
	 * @param processType
	 *            -流程类型查询条件<br>
	 *            <p>
	 *            为null表示所有类型
	 *            </p>
	 * @param processStatus
	 *            -流程状态查询条件<br>
	 *            <p>
	 *            0：所有状态
	 *            </p>
	 *            <p>
	 *            1：正在执行
	 *            </p>
	 *            <p>
	 *            2：已经结束
	 *            </p>
	 * @param searchScope
	 *            -查询范围<br>
	 *            <p>
	 *            0：所有范围
	 *            </p>
	 *            <p>
	 *            1：我经办的
	 *            </p>
	 *            <p>
	 *            2：我发起的
	 *            </p>
	 *            <p>
	 *            3：指定发起人
	 *            </p>
	 * @param startDate
	 *            -开始时间查询条件<br>
	 *            <p>
	 *            为null表示不设置
	 *            </p>
	 * @param endDate
	 *            -结束时间查询条件<br>
	 *            <p>
	 *            为null表示不设置
	 *            </p>
	 * @param userId
	 *            -指定发起人Id<br>
	 *            <p>
	 *            searchScope为3时有效
	 *            </p>
	 * @param businessName
	 *            -业务名称查询条件
	 * @return Page<Object[]> 流程信息集<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 * 
	 * Object[]{(0)流程实例Id,(1)业务名称,(2)开始时间,(3)结束时间,(4)流程主表单Id,(5)流程主表单业务数据标识 }
	 * </p>
	 * @throws WorkflowExceptin
	 */
	public Page getProcessInfoForPageByQueryCondition(Page page,
			String processType, String processStatus, String searchScope,
			Date startDate, Date endDate, String userId, String businessName)
			throws WorkflowException {
		return oaSystemService.getProcessInfoForPageByQueryCondition(page,
				processType, processStatus, searchScope, startDate, endDate,
				userId, businessName);
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
		List list = workflowDesign.getDataByHql(
				"from TwfBaseWorkflow t where t.wfPdid = ?",
				new Object[] { Long.valueOf(pid) });
		if (null != list) {
			TwfBaseWorkflow tbw = (TwfBaseWorkflow) list.get(0);
			return tbw.getTwfBaseProcessfile();
		} else {
			return null;
		}
	}

	/**
	 * 根据流程文件id得到流程文件Bo
	 * 
	 * @author:邓志城
	 * @date:2010-11-9 下午04:01:54
	 * @param id
	 * @return
	 * @throws WorkflowException
	 */
	public TwfBaseProcessfile getTwfBaseProcessFile(String id)
			throws WorkflowException {
		List list = workflowDesign.getDataByHql(
				"from TwfBaseProcessfile t where t.pfId = ?",
				new Object[] { Long.valueOf(id) });
		if (list != null && !list.isEmpty()) {
			return (TwfBaseProcessfile) list.get(0);
		}
		return null;
	}

	/**
	 * 根据流程名称获取流程主表单id
	 * 
	 * @description
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime Jun 4, 2012 4:03:46 PM
	 */
	public Long getMainFormIdByWorkflowName(String workflowName){
		List list = workflowDesign.getDataByHql(
				"select t.pfMainformId from TwfBaseProcessfile t where t.pfName = ?",
				new Object[] { workflowName });
		if (list != null && !list.isEmpty()) {
			return (Long) list.get(0);
		}
		return null;
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
			throws WorkflowException {
		return workflowDelegation.getProcessFileIdByProcessInstanceId(processInstanceId);
	}

	/**
	 * 文件归档接口
	 * 
	 * @author:邓志城
	 * @date:2009-6-10 下午02:32:35
	 * @param String
	 *            fileNo 文件编号
	 * @param String
	 *            fileAuthor 文件作者
	 * @param String
	 *            fileDepartment 文件所属部门
	 * @param String
	 *            fileTitle 文件标题
	 * @param Date
	 *            fileCreateTime 文件创建时间
	 * @param String
	 *            filePageNum 文件页号
	 * @param String
	 *            fileDesc 文件备注
	 * @param byte[]
	 *            fileAppendContent 正文或附件
	 * @param String
	 *            fileType 文件类型【收文或发文】
	 * @param String
	 *            fileId 文档ID
	 * @return 操作结果
	 * @throws SystemException
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	public boolean addTemplateFileInterface(Object... objects)
			throws SystemException {
		throw new SystemException("方法已过期,请使用方法SendDocManager#doArchive代替");
	}

	/**
	 * 查询所有流程文件包括流程类型信息（OA专用）
	 * 
	 * @author 喻斌
	 * @date Jun 19, 2009 8:53:23 AM
	 * @return List<Object[]> 流程定义信息集<br>
	 *         <p>
	 * 
	 * 返回数据类型：Object[]{(0)流程定义文件Id,(1)流程定义文件名称,(2)流程定义文件主表单Id,(3)流程类型Id,(
	 * 4)流程类型名称}
	 * </p>
	 * @throws WorkflowException
	 */
	public List getAllProcessFilesList() throws WorkflowException {
		return oaSystemService.getAllProcessFilesList();
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
	 *         Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(
	 *         4)流程创建时间,(5)任务是否被挂起,
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
		return workflowRun.getAssignTaskInfoByUserId(page, userId, taskType,
				assignType, initiativeType, processType, businessName,
				userName, startDate, endDate);
	}

	/**
	 * 根据任务实例Id获取每个节点的处理意见和对应的业务特殊字段
	 * 
	 * @author 喻斌
	 * @date Dec 19, 2009 2:20:19 PM
	 * @param taskInstanceId
	 *            -任务实例Id
	 * @return List<Object[]> -每个节点的处理意见和对应的业务特殊字段<br>
	 *         <p>
	 *         Object[]{(0)任务实例Id, (1)处理意见内容, (2)处理人Id, (3)处理人名称, (4)处理时间,
	 *         (5)节点对应的业务特殊字段, (6)节点名称,{7}签名信息}
	 *         </p>
	 * @throws WorkflowException
	 */
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = true)
	public List<Object[]> getProcessHandlesAndBusiFlagByTaskId(
			String taskInstanceId) throws WorkflowException {
		return workflowDelegation
				.getProcessHandlesAndBusiFlagByTaskId(taskInstanceId);
	}

	/**
	 * 根据任务实例Id找到对应的流程实例Id
	 * 
	 * @param taskInstanceId
	 * @return
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.lang.String getProcessInstanceIdByTiId(
			java.lang.String taskInstanceId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation.getProcessInstanceIdByTiId(taskInstanceId);
	}

	/**
	 * 根据任务实例Id获取该任务节点对应的业务特殊字段及其它信息
	 * 
	 * @author 喻斌
	 * @date Dec 22, 2009 6:54:20 PM
	 * @param taskInstanceId
	 *            -任务实例Id
	 * @return Object[] -节点对应业务特殊字段及其它信息，节点不存在则返回null
	 *         <p>
	 *         Object[]{(0)任务名称, (1)节点Id, (2)节点名称, (3)节点对应特殊业务字段}
	 *         </p>
	 * @throws WorkflowException
	 */
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = true)
	public Object[] getBusiFlagByTaskId(String taskInstanceId)
			throws WorkflowException {
		Object[] returnObj = new Object[4];
		TwfBaseNodesetting nodeSettring = workflowDesign
				.getNodesettingByNodeId(workflowDesign.getNodeIdByTaskInstanceId(taskInstanceId));
		returnObj[0] = nodeSettring.getNsNodeName();
		returnObj[1] = nodeSettring.getNsNodeId();
		returnObj[2] = nodeSettring.getNsNodeName();
		returnObj[3] = nodeSettring.getPlugin("plugins_businessFlag");
		return returnObj;
		// return workflowDesign.getBusiFlagByTaskId(taskInstanceId);
	}
	
	/**
	 *根据流程名称获取该流程第一个节点对应的业务特殊字段及其它信息 
	 * 
	 * @author 严建
	 * @param workflowName	流程名称
	 * @return Object[] -节点对应业务特殊字段及其它信息，节点不存在则返回null
	 *         <p>
	 *         Object[]{(0)任务名称, (1)节点Id, (2)节点名称, (3)节点对应特殊业务字段}
	 *         </p>
	 * @throws WorkflowException
	 * @createTime May 31, 2012 3:52:31 PM
	 */
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = true)
	public Object[] getFirstNodeBusiFlagByWorkflowName(String workflowName)
			throws WorkflowException {
		Object[] returnObj = new Object[4];
		TwfBaseNodesetting nodeSettring = this.findFirstNodeSettingByWorkflowName(workflowName);
		returnObj[0] = nodeSettring.getNsNodeName();
		returnObj[1] = nodeSettring.getNsNodeId();
		returnObj[2] = nodeSettring.getNsNodeName();
		returnObj[3] = nodeSettring.getPlugin("plugins_businessFlag");
		return returnObj;
	}
	
	/**
	 * 根据要查询的属性、查询条件和排序属性得到符合条件的流程实例分页列表信息
	 * 
	 * @author 喻斌
	 * @date Jan 20, 2010 3:33:12 PM
	 * @param page
	 *            -分页列表
	 * @param toSelectItems
	 *            -要查询的属性<br>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 * @param paramsMap
	 *            -查询条件设置<br>
	 *            <p>
	 *            “processName”：流程名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDateStart”：启动时间下限
	 *            </p>
	 *            <p>
	 *            “processStartDateEnd”：启动时间上限
	 *            </p>
	 *            <p>
	 *            “processEndDateStart”：结束时间下限
	 *            </p>
	 *            <p>
	 *            “processEndDateEnd”：结束时间上限
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processStatus”：流程状态（“0”：待办；“1”：办毕）
	 *            </p>
	 * @param orderMap
	 *            -要排序的属性（“0”：升序；“1”：降序）<br>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 * @return Page<Object[]>
	 * @throws WorkflowException
	 */
	
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = true)
	public Page<Object[]> getProcessInstanceByConditionForPage(
			Page<Object[]> page, List<String> toSelectItems,
			Map<String, Object> paramsMap, Map<String, String> orderMap)
			throws WorkflowException {
		return workflowDelegation.getProcessInstanceByConditionForPage(page,
				toSelectItems, paramsMap, orderMap);
	}

	/**
	 * 根据要查询的属性、查询条件和排序属性得到符合条件的流程实例分页列表信息 2010-03-09：增加集合查询功能
	 * 
	 * @author 喻斌
	 * @date Jan 20, 2010 3:33:12 PM
	 * @param page
	 *            -分页列表
	 * @param toSelectItems
	 *            -要查询的属性<br>
	 *            <p>
	 *            “processInstanceId”：流程实例Id
	 *            </p>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “processSuspend”：流程是否被挂起(false：否；true：是)
	 *            </p>
	 *            <p>
	 *            “processTimeout”：流程是否超时(“0”：否；“1”：是)
	 *            </p>
	 * @param paramsMap
	 *            -查询条件设置<br>
	 *            <p>
	 *            “processInstanceId”：流程实例Id；若要同时查询多个流程实例Id，则使用包含多个Long值的List
	 *            </p>
	 *            <p>
	 *            “processName”：流程名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id；若要同时查询多个流程定义Id，则使用包含多个Long值的List
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id；若要同时查询多个发起人Id，则使用包含多个String值的List
	 *            </p>
	 *            <p>
	 *            “processStartDateStart”：启动时间下限
	 *            </p>
	 *            <p>
	 *            “processStartDateEnd”：启动时间上限
	 *            </p>
	 *            <p>
	 *            “processEndDateStart”：结束时间下限
	 *            </p>
	 *            <p>
	 *            “processEndDateEnd”：结束时间上限
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称（模糊查询）
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id（大于“0”的正整数字符串：流程类型Id；“-1”:非系统级流程类型）；若要同时查询多个流程类型Id，则使用包含多个String值的List
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id；若要同时查询多个表单Id，则使用包含多个String值的List
	 *            </p>
	 *            <p>
	 *            “processStatus”：流程状态（“0”：待办；“1”：办毕）
	 *            </p>
	 *            <p>
	 *            “processSuspend”：流程是否被挂起(false：否；true：是)
	 *            </p>
	 *            <p>
	 *            “processTimeout”：流程是否超时(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “hasHandlerId”：流程实例经办者Id；若要同时查询多个经办者Id，则使用包含多个String值的List
	 *            </p>
	 * @param orderMap
	 *            -要排序的属性（“0”：升序；“1”：降序）<br>
	 *            <p>
	 *            “processInstanceId”：流程实例Id
	 *            </p>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “processSuspend”：流程是否被挂起
	 *            </p>
	 *            <p>
	 *            “processTimeout”：流程是否超时
	 *            </p>
	 * @param customSelectItems
	 *            -用户自定义的要查询的业务字段(Hql中select部分，格式如：表别名1.字段1,表别名2.字段2,...)
	 * @param customFromItems
	 *            -用户自定义的要查询的业务表(Hql中from部分，格式如：表名1 别名1,表名2 别名2,...)
	 * @param customQuery
	 *            -用户自定义的业务表查询语句(Hql中where部分,需要使用命名Query)；其中可以设置如下变量：<br>
	 *            <p>
	 * @businessId：流程主表单对应业务数据标识(如@businessId = ...)
	 *                                        </p>
	 *                                        <p>
	 * @eFormId：流程对应主表单Id(如@eFormId = ...)
	 *                              </p>
	 * @param customValues
	 *            -用户自定义的业务表查询语句参数(Map格式：命名Query参数名, 命名Query值)
	 * @return Page<Object[]>
	 * @throws WorkflowException
	 */
	public Page<Object[]> getProcessInstanceByConditionForPage(
			Page<Object[]> page, List<String> toSelectItems,
			Map<String, Object> paramsMap, Map<String, String> orderMap,
			String customSelectItems, String customFromItems,
			String customQuery, Map<String, Object> customValues)
			throws WorkflowException {
		return workflowDelegation.getProcessInstanceByConditionForPage(page,
				toSelectItems, paramsMap, orderMap, customSelectItems,
				customFromItems, customQuery, customValues);
	}

	/**
	 * 
	 * 注 ：该方法是基于oracle的rownum进行分页处理,在sItems存在多个相同查询参数时，无法正常处理分页
	 * <br/>常见异常java.sql.SQLException: ORA-00918: 未明确定义列
	 * 
	 * @author 严建
	 * @param page
	 * @param toSelectItems
	 * @param paramsMap
	 * @param orderMap
	 * @param customSelectItems
	 * @param customFromItems
	 * @param customQuery
	 * @param customValues
	 * @param customOrderBy
	 * @return
	 * @throws WorkflowException
	 * @createTime Feb 9, 2012 1:31:45 PM
	 */
	public Page<Object[]> getProcessInstanceByConditionForPage(
			Page<Object[]> page, List<String> toSelectItems,
			Map<String, Object> paramsMap, Map<String, String> orderMap,
			String customSelectItems, String customFromItems,
			String customQuery, List<Object> customValues, String customOrderBy)
			throws WorkflowException {
		return workflowDelegation.getProcessInstanceByConditionForPage(page,
				toSelectItems, paramsMap, orderMap, customSelectItems,
				customFromItems, customQuery, customValues, customOrderBy);
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
		return workflowDesign.getFormIdAndBusiIdByPiIdAndNodeId(pid, nodeId);
	}

	/**
	 * author:luosy 根据要查询的属性、查询条件和排序属性得到符合条件的流程实例分页列表信息
	 * 
	 * @date 2010-3-3 3:33:12 PM
	 * @param page
	 *            -分页列表
	 * @param toSelectItems
	 *            -要查询的属性<br>
	 *            <p>
	 *            “taskId”：任务实例Id
	 *            </p>
	 *            <p>
	 *            “taskName”：任务名称
	 *            </p>
	 *            <p>
	 *            “taskNodeId”：任务节点Id
	 *            </p>
	 *            <p>
	 *            “taskNodeName”：任务节点名称
	 *            </p>
	 *            <p>
	 *            “taskStartDate”：任务开始时间
	 *            </p>
	 *            <p>
	 *            “taskEndDate”：任务结束时间
	 *            </p>
	 *            <p>
	 *            “isBackspace”：任务是否是回退任务(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “isReceived”：任务是否被签收(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskFormId”：任务节点对应表单Id
	 *            </p>
	 *            <p>
	 *            “taskFormBusinessId”：任务节点对应表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “taskSuspend”：任务是否被挂起(false：否；true：是)
	 *            </p>
	 *            <p>
	 *            “assignHandlerName”：委托/指派人名称(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerName”：被委托/指派人名称(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “assignHandlerId”：委托/指派人Id(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerId”：被委托/指派人Id(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “assignType”：委托类型(“0”：委托；“1”：指派)
	 *            </p>
	 *            <p>
	 *            “isHandlerNotExist”：任务分配的处理者是否已不存在(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “processInstanceId”：流程实例Id
	 *            </p>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “processSuspend”：流程是否被挂起(false：否；true：是)
	 *            </p>
	 *            <DD>
	 * @param paramsMap
	 *            -查询条件设置<br>
	 *            <p>
	 *            “taskId”：任务实例Id
	 *            </p>
	 *            <p>
	 *            “taskName”：任务名称(模糊查询)
	 *            </p>
	 *            <p>
	 *            “taskNodeId”：任务节点Id
	 *            </p>
	 *            <p>
	 *            “taskNodeName”：任务节点名称(模糊查询)
	 *            </p>
	 *            <p>
	 *            “handlerId”：处理人标识
	 *            </p>
	 *            <p>
	 *            “taskStartDateStart”：任务开始时间下限
	 *            </p>
	 *            <p>
	 *            “taskStartDateEnd”：任务开始时间上限
	 *            </p>
	 *            <p>
	 *            “taskEndDateStart”：任务结束时间下限
	 *            </p>
	 *            <p>
	 *            “taskEndDateEnd”：任务结束时间上限
	 *            </p>
	 *            <p>
	 *            “isBackspace”：任务是否是回退任务(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskFormId”：任务节点对应表单Id
	 *            </p>
	 *            <p>
	 *            “taskSuspend”：任务是否被挂起(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskType”：搜索任务类型(“0”：待办；“1”：在办；“2”：非办结；“3”：办结)
	 *            </p>
	 *            <p>
	 *            “assignType”：委托类型(“0”：委托；“1”：指派；“2”：全部)
	 *            </p>
	 *            <p>
	 *            “assignHandlerName”：委托/指派人名称(需要指定assignType查询条件才有效)
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerName”：被委托/指派人名称(需要指定assignType查询条件才有效)
	 *            </p>
	 *            <p>
	 *            “assignHandlerId”：委托/指派人Id(需要指定assignType查询条件才有效)
	 *            </p>
	 *            <p>
	 * 
	 * “toAssignHandlerId”：被委托/指派人Id(若不指定assignType查询条件，又要查询assignHandlerName 、<br>
	 * 
	 * toAssignHandlerName、assignHandlerId、toAssignHandlerId、assignType的值 ，<br>
	 * 则必须设置toAssignHandlerId查询条件)
	 * </p>
	 * <p>
	 * “isHandlerNotExist”：任务分配的处理者是否已不存在(“0”：否；“1”：是)
	 * </p>
	 * <p>
	 * “processInstanceId”：流程实例Id
	 * </p>
	 * <p>
	 * “processName”：流程名称（模糊查询）
	 * </p>
	 * <p>
	 * “processDefinitionId”：流程定义Id
	 * </p>
	 * <p>
	 * “startUserName”：发起人名称（模糊查询）
	 * </p>
	 * <p>
	 * “startUserId”：发起人Id
	 * </p>
	 * <p>
	 * “processStartDateStart”：启动时间下限
	 * </p>
	 * <p>
	 * “processStartDateEnd”：启动时间上限
	 * </p>
	 * <p>
	 * “processEndDateStart”：结束时间下限
	 * </p>
	 * <p>
	 * “processEndDateEnd”：结束时间上限
	 * </p>
	 * <p>
	 * “businessName”：业务名称（模糊查询）
	 * </p>
	 * <p>
	 * “processTypeName”：流程类型名称（模糊查询）
	 * </p>
	 * <p>
	 * “processTypeId”：流程类型Id（大于“0”的正整数字符串：流程类型Id；“-1”:非系统级流程类型）
	 * </p>
	 * <p>
	 * “processMainFormId”：流程对应主表单Id
	 * </p>
	 * <p>
	 * “processStatus”：流程状态（“0”：待办；“1”：办毕）
	 * </p>
	 * <p>
	 * “processSuspend”：流程是否被挂起(0：否；1：是)
	 * </p>
	 * <DD>
	 * @param orderMap
	 *            -要排序的属性（“0”：升序；“1”：降序）<br>
	 *            <p>
	 *            “taskId”：任务实例Id
	 *            </p>
	 *            <p>
	 *            “taskName”：任务名称
	 *            </p>
	 *            <p>
	 *            “taskNodeId”：任务节点Id
	 *            </p>
	 *            <p>
	 *            “taskNodeName”：任务节点名称
	 *            </p>
	 *            <p>
	 *            “taskStartDate”：任务开始时间
	 *            </p>
	 *            <p>
	 *            “taskEndDate”：任务结束时间
	 *            </p>
	 *            <p>
	 *            “isBackspace”：任务是否是回退任务(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “isReceived”：任务是否被签收(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskFormId”：任务节点对应表单Id
	 *            </p>
	 *            <p>
	 *            “taskFormBusinessId”：任务节点对应表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “taskSuspend”：任务是否被挂起(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “assignHandlerName”：委托/指派人名称
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerName”：被委托/指派人名称
	 *            </p>
	 *            <p>
	 *            “assignHandlerId”：委托/指派人Id
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerId”：被委托/指派人Id
	 *            </p>
	 *            <p>
	 *            “assignType”：委托类型(“0”：委托；“1”：指派)
	 *            </p>
	 *            <p>
	 *            “isHandlerNotExist”：任务分配的处理者是否已不存在(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “processInstanceId”：流程实例Id
	 *            </p>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “processSuspend”：流程是否被挂起(false：否；true：是)
	 *            </p>
	 * @param customSelectItems -
	 *            -用户自定义的要查询的业务字段(Hql中select部分，格式如：表别名1.字段1,表别名2.字段2,...)<br>
	 * @param customFromItems -
	 *            -用户自定义的要查询的业务表(Hql中from部分，格式如：表名1 别名1,表名2 别名2,...)
	 * @param customQuery -
	 *            -用户自定义的业务表查询语句(Hql中where部分)；其中可以设置如下变量：
	 *            <p>
	 * @businessId：流程主表单对应业务数据标识(如@businessId = ...)
	 *                                        </p>
	 *                                        <p>
	 * @eFormId：流程对应主表单Id(如@eFormId = ...)
	 *                              </p>
	 *                              <p>
	 * @handlerId：任务处理者Id(如@handlerId = ...)
	 *                                </p>
	 *                                <p>
	 * @nodeBusinessId：任务表单对应业务数据标识(如@nodeBusinessId = ...)
	 *                                               </p>
	 *                                               <p>
	 * @nodeEFormId：任务对应表单Id(如@nodeEFormId = ...)
	 *                                     </p>
	 * @param customValues -
	 *            -用户自定义的业务表查询语句参数
	 * @return Page<Object[]>
	 * @throws WorkflowException
	 */
	public Page getTaskInfosByConditionForPage(
			com.strongmvc.orm.hibernate.Page page,
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			java.util.Map<java.lang.String, java.lang.Object> customValues)
			throws WorkflowException {
		return workflowRun.getTaskInfosByConditionForPage(page, toSelectItems,
				paramsMap, orderMap, customSelectItems, customFromItems,
				customQuery, customValues);
	}

	@SuppressWarnings("unchecked")
	public Page getTaskInfosByConditionForPage(
			com.strongmvc.orm.hibernate.Page page,
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			List customValues, String customOrderBy) throws WorkflowException {
		return workflowRun.getTaskInfosByConditionForPage(page, toSelectItems,
				paramsMap, orderMap, customSelectItems, customFromItems,
				customQuery, customValues, customOrderBy);
	}

	/**
	 * author:luosy 根据要查询的属性、查询条件和排序属性得到符合条件的流程实例列表信息
	 * 
	 * @date 2010-3-3
	 * @param page
	 *            -分页列表
	 * @param toSelectItems
	 *            -要查询的属性<br>
	 *            <p>
	 *            “taskId”：任务实例Id
	 *            </p>
	 *            <p>
	 *            “taskName”：任务名称
	 *            </p>
	 *            <p>
	 *            “taskNodeId”：任务节点Id
	 *            </p>
	 *            <p>
	 *            “taskNodeName”：任务节点名称
	 *            </p>
	 *            <p>
	 *            “taskStartDate”：任务开始时间
	 *            </p>
	 *            <p>
	 *            “taskEndDate”：任务结束时间
	 *            </p>
	 *            <p>
	 *            “isBackspace”：任务是否是回退任务(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “isReceived”：任务是否被签收(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskFormId”：任务节点对应表单Id
	 *            </p>
	 *            <p>
	 *            “taskFormBusinessId”：任务节点对应表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “taskSuspend”：任务是否被挂起(false：否；true：是)
	 *            </p>
	 *            <p>
	 *            “assignHandlerName”：委托/指派人名称(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerName”：被委托/指派人名称(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “assignHandlerId”：委托/指派人Id(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerId”：被委托/指派人Id(必须同时查询assignType属性)
	 *            </p>
	 *            <p>
	 *            “assignType”：委托类型(“0”：委托；“1”：指派)
	 *            </p>
	 *            <p>
	 *            “isHandlerNotExist”：任务分配的处理者是否已不存在(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “processInstanceId”：流程实例Id
	 *            </p>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “processSuspend”：流程是否被挂起(false：否；true：是)
	 *            </p>
	 *            <DD>
	 * @param paramsMap
	 *            -查询条件设置<br>
	 *            <p>
	 *            “taskId”：任务实例Id
	 *            </p>
	 *            <p>
	 *            “taskName”：任务名称(模糊查询)
	 *            </p>
	 *            <p>
	 *            “taskNodeId”：任务节点Id
	 *            </p>
	 *            <p>
	 *            “taskNodeName”：任务节点名称(模糊查询)
	 *            </p>
	 *            <p>
	 *            “handlerId”：处理人标识
	 *            </p>
	 *            <p>
	 *            “taskStartDateStart”：任务开始时间下限
	 *            </p>
	 *            <p>
	 *            “taskStartDateEnd”：任务开始时间上限
	 *            </p>
	 *            <p>
	 *            “taskEndDateStart”：任务结束时间下限
	 *            </p>
	 *            <p>
	 *            “taskEndDateEnd”：任务结束时间上限
	 *            </p>
	 *            <p>
	 *            “isBackspace”：任务是否是回退任务(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskFormId”：任务节点对应表单Id
	 *            </p>
	 *            <p>
	 *            “taskSuspend”：任务是否被挂起(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskType”：搜索任务类型(“0”：待办；“1”：在办；“2”：非办结；“3”：办结)
	 *            </p>
	 *            <p>
	 *            “assignType”：委托类型(“0”：委托；“1”：指派；“2”：全部)
	 *            </p>
	 *            <p>
	 *            “assignHandlerName”：委托/指派人名称(需要指定assignType查询条件才有效)
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerName”：被委托/指派人名称(需要指定assignType查询条件才有效)
	 *            </p>
	 *            <p>
	 *            “assignHandlerId”：委托/指派人Id(需要指定assignType查询条件才有效)
	 *            </p>
	 *            <p>
	 * 
	 * “toAssignHandlerId”：被委托/指派人Id(若不指定assignType查询条件，又要查询assignHandlerName 、<br>
	 * 
	 * toAssignHandlerName、assignHandlerId、toAssignHandlerId、assignType的值 ，<br>
	 * 则必须设置toAssignHandlerId查询条件)
	 * </p>
	 * <p>
	 * “isHandlerNotExist”：任务分配的处理者是否已不存在(“0”：否；“1”：是)
	 * </p>
	 * <p>
	 * “processInstanceId”：流程实例Id
	 * </p>
	 * <p>
	 * “processName”：流程名称（模糊查询）
	 * </p>
	 * <p>
	 * “processDefinitionId”：流程定义Id
	 * </p>
	 * <p>
	 * “startUserName”：发起人名称（模糊查询）
	 * </p>
	 * <p>
	 * “startUserId”：发起人Id
	 * </p>
	 * <p>
	 * “processStartDateStart”：启动时间下限
	 * </p>
	 * <p>
	 * “processStartDateEnd”：启动时间上限
	 * </p>
	 * <p>
	 * “processEndDateStart”：结束时间下限
	 * </p>
	 * <p>
	 * “processEndDateEnd”：结束时间上限
	 * </p>
	 * <p>
	 * “businessName”：业务名称（模糊查询）
	 * </p>
	 * <p>
	 * “processTypeName”：流程类型名称（模糊查询）
	 * </p>
	 * <p>
	 * “processTypeId”：流程类型Id（大于“0”的正整数字符串：流程类型Id；“-1”:非系统级流程类型）
	 * </p>
	 * <p>
	 * “processMainFormId”：流程对应主表单Id
	 * </p>
	 * <p>
	 * “processStatus”：流程状态（“0”：待办；“1”：办毕）
	 * </p>
	 * <p>
	 * “processSuspend”：流程是否被挂起(0：否；1：是)
	 * </p>
	 * <DD>
	 * @param orderMap
	 *            -要排序的属性（“0”：升序；“1”：降序）<br>
	 *            <p>
	 *            “taskId”：任务实例Id
	 *            </p>
	 *            <p>
	 *            “taskName”：任务名称
	 *            </p>
	 *            <p>
	 *            “taskNodeId”：任务节点Id
	 *            </p>
	 *            <p>
	 *            “taskNodeName”：任务节点名称
	 *            </p>
	 *            <p>
	 *            “taskStartDate”：任务开始时间
	 *            </p>
	 *            <p>
	 *            “taskEndDate”：任务结束时间
	 *            </p>
	 *            <p>
	 *            “isBackspace”：任务是否是回退任务(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “isReceived”：任务是否被签收(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “taskFormId”：任务节点对应表单Id
	 *            </p>
	 *            <p>
	 *            “taskFormBusinessId”：任务节点对应表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “taskSuspend”：任务是否被挂起(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “assignHandlerName”：委托/指派人名称
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerName”：被委托/指派人名称
	 *            </p>
	 *            <p>
	 *            “assignHandlerId”：委托/指派人Id
	 *            </p>
	 *            <p>
	 *            “toAssignHandlerId”：被委托/指派人Id
	 *            </p>
	 *            <p>
	 *            “assignType”：委托类型(“0”：委托；“1”：指派)
	 *            </p>
	 *            <p>
	 *            “isHandlerNotExist”：任务分配的处理者是否已不存在(“0”：否；“1”：是)
	 *            </p>
	 *            <p>
	 *            “processInstanceId”：流程实例Id
	 *            </p>
	 *            <p>
	 *            “processName”：流程名称
	 *            </p>
	 *            <p>
	 *            “processDefinitionId”：流程定义Id
	 *            </p>
	 *            <p>
	 *            “startUserName”：发起人名称
	 *            </p>
	 *            <p>
	 *            “startUserId”：发起人Id
	 *            </p>
	 *            <p>
	 *            “processStartDate”：启动时间
	 *            </p>
	 *            <p>
	 *            “processEndDate”：结束时间
	 *            </p>
	 *            <p>
	 *            “businessName”：业务名称
	 *            </p>
	 *            <p>
	 *            “processTypeName”：流程类型名称
	 *            </p>
	 *            <p>
	 *            “processTypeId”：流程类型Id
	 *            </p>
	 *            <p>
	 *            “processMainFormId”：流程对应主表单Id
	 *            </p>
	 *            <p>
	 *            “processMainFormBusinessId”：流程对应主表单业务数据标识
	 *            </p>
	 *            <p>
	 *            “processSuspend”：流程是否被挂起(false：否；true：是)
	 *            </p>
	 * @param customSelectItems -
	 *            -用户自定义的要查询的业务字段(Hql中select部分，格式如：表别名1.字段1,表别名2.字段2,...)<br>
	 * @param customFromItems -
	 *            -用户自定义的要查询的业务表(Hql中from部分，格式如：表名1 别名1,表名2 别名2,...)
	 * @param customQuery -
	 *            -用户自定义的业务表查询语句(Hql中where部分)；其中可以设置如下变量：
	 *            <p>
	 * @businessId：流程主表单对应业务数据标识(如@businessId = ...)
	 *                                        </p>
	 *                                        <p>
	 * @eFormId：流程对应主表单Id(如@eFormId = ...)
	 *                              </p>
	 *                              <p>
	 * @handlerId：任务处理者Id(如@handlerId = ...)
	 *                                </p>
	 *                                <p>
	 * @nodeBusinessId：任务表单对应业务数据标识(如@nodeBusinessId = ...)
	 *                                               </p>
	 *                                               <p>
	 * @nodeEFormId：任务对应表单Id(如@nodeEFormId = ...)
	 *                                     </p>
	 * @param customValues -
	 *            -用户自定义的业务表查询语句参数
	 * @return List<Object[]>
	 * @throws WorkflowException
	 */
	public List getTaskInfosByConditionForList(
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			java.util.Map<java.lang.String, java.lang.Object> customValues)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowRun.getTaskInfosByConditionForList(toSelectItems,
				paramsMap, orderMap, customSelectItems, customFromItems,
				customQuery, customValues);
	}

	/**
	 * 
	 * 工作流查询任务信息的接口(支持自定义sql语句查询，参数详细信息见工作流API)
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
	 * @return
	 * @throws WorkflowException
	 * @createTime Feb 8, 2012 12:48:51 PM
	 */
	public List<Object[]> getTaskInfosByConditionForList(
			List<String> toSelectItems, Map<String, Object> paramsMap,
			Map<String, String> orderMap, String customSelectItems,
			String customFromItems, String customQuery,
			List<Object> customValues, String customOrderBy)
			throws WorkflowException {
		return workflowRun.getTaskInfosByConditionForList(toSelectItems,
				paramsMap, orderMap, customSelectItems, customFromItems,
				customQuery, customValues, customOrderBy);
	}

	/**
	 * 根据流程实例id获取任务办理意见.
	 * 
	 * @author:邓志城
	 * @date:2010-3-1 下午07:31:29
	 * @param processInstanceId
	 *            流程实例id
	 * @return 办理意见集合.
	 */
	@SuppressWarnings("deprecation")
	public java.util.List<com.strongit.workflow.bo.TwfInfoApproveinfo> getApproveInfosByPIId(
			java.lang.String processInstanceId) {
		return workflowDelegation.getApproveInfosByPIId(processInstanceId);
	}

	/**
	 * 根据流程实例Id获取流程中每个节点的处理意见和对应的节点设置信息
	 * 
	 * @author 严建
	 * @param processInstanceId
	 *            流程实例Id
	 * @return List -每个节点的处理意见和对应的节点设置信息 Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间,
	 *         (3)任务结束时间, (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象,
	 *         (9)审批数字签名, (10)任务委派类型【0：委托；1：指派；“”：非委托】, (11)最后一个委托/指派人Id,
	 *         (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称, (15)主键值}
	 * @throws com.strongit.workflow.exception.WorkflowException
	 * @createTime Mar 27, 2012 3:49:07 PM
	 */
	public java.util.List<java.lang.Object[]> getProcessHandlesAndNodeSettingByPiId(
			java.lang.String processInstanceId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation
				.getProcessHandlesAndNodeSettingByPiId(processInstanceId);
	}
	
	/**
	 * 根据节点ID获取节点设置信息.
	 * 
	 * @author:彭小青
	 * @date:2010-3-5 下午11:31:29
	 * @param
	 * @return
	 */
	public TwfBaseNodesetting getNodesettingByNodeId(String nodeId) {
		return workflowDesign.getNodesettingByNodeId(nodeId);
	}

	
	/**
	 * 根据任务ID获取节点设置信息.
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 21, 2012 9:53:38 AM
	 */
	public TwfBaseNodesetting getNodesettingByTid(String taskId){
		return getNodesettingByNodeId(getTaskInstanceById(taskId).getNodeId().toString());
	}
	/**
	 * 根据一组节点ID获取一组节点设置信息.
	 * 
	 * @description
	 * @author 严建
	 * @param nodeIdList
	 * @return Map【节点id-节点信息】
	 * @throws Exception
	 * @createTime Jan 6, 2012 11:44:44 AM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, TwfBaseNodesetting> getNodesettingMapByNodeIdList(
			List<String> nodeIdList) {
		Map<String, TwfBaseNodesetting> result = new HashMap<String, TwfBaseNodesetting>();
		if (nodeIdList != null && !nodeIdList.isEmpty()) {
			int mod = nodeIdList.size() % 1000;
			int dem = nodeIdList.size() / 1000;
			int length = 0;
			if (mod != 0) {
				length = dem + 1;
			} else {
				length = dem;
			}
			StringBuilder[] paramArray = new StringBuilder[length];
			for (int i = 0; i < dem; i++) {
				int start = dem * 1000;
				int end = (dem + 1) * 1000;
				StringBuilder param = new StringBuilder();
				for (int k = start; k < end; k++) {
					param.append("?,");
				}
				param = param.deleteCharAt(param.length() - 1);
				paramArray[i] = param;
			}
			if (mod != 0) {
				StringBuilder param = new StringBuilder();
				for (int i = 0; i < mod; i++) {
					param.append("?,");
				}
				param = param.deleteCharAt(param.length() - 1);
				paramArray[dem] = param;
			}
			StringBuilder whereString = new StringBuilder();

			for (int i = 0; i < paramArray.length; i++) {
				if (i == 0) {
					whereString.append(" where t.nsNodeId in (" + paramArray[0]
							+ ") ");
				} else {
					whereString.append(" or t.nsNodeId in (" + paramArray[i]
							+ ") ");
				}
			}
			List<Long> nodeIdListTemp = new LinkedList<Long>();
			for (String nodeId : nodeIdList) {
				nodeIdListTemp.add(new Long(nodeId));
			}
			StringBuffer hql = new StringBuffer("").append(
					" from TwfBaseNodesetting t").append(whereString);
			logger.info(hql.toString());
			List<TwfBaseNodesetting> nodesettings = (List<TwfBaseNodesetting>) getServiceDAO()
					.find(hql.toString(), nodeIdListTemp.toArray());
			for (TwfBaseNodesetting nodesetting : nodesettings) {
				result.put(nodesetting.getNsNodeId().toString(), nodesetting);
			}
		}
		return result;
	}

	/**
	 * 根据任务实例Id找到对应的节点Id
	 * 
	 * @author:邓志城
	 * @date:2010-3-22 下午09:07:35
	 * @param taskId
	 *            任务实例Id
	 * @return 任务实例对应的节点Id
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public String getNodeIdByTaskInstanceId(String taskId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDesign.getNodeIdByTaskInstanceId(taskId);
	}

	/**
	 * 根据流程实例id得到办理意见和意见挂接的特殊业务字段
	 * 
	 * @author:邓志城
	 * @date:2010-3-8 下午04:19:14
	 * @param processInstanceId
	 * @return List -每个节点的处理意见和对应的节点设置信息
	 * 
	 * Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)处理意见内容, (5)处理人Id,
	 * (6)处理人名称, (7)处理时间, (8)节点设置对象, (9)审批数字签名, (10)任务委派类型【0：委托；1：指派；2：代办；""：普通】,
	 * (11)最后一个委托/指派人Id, (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称
	 * ,(15)意见记录ID}
	 */
	public List<Object[]> getBusiFlagByProcessInstanceId(
			String processInstanceId) {
		return workflowDelegation.getProcessHandlesAndNodeSettingByPiId(processInstanceId);
	}

	/**
	 * 根据迁移线Id得到迁移线插件属性信息
	 * 
	 * @param transitionId
	 *            迁移线Id
	 * @param pluginName
	 *            插件属性名称
	 * @return 迁移线插件属性信息
	 */
	public TwfBaseTransitionPlugin getTransitionPluginByTsId(
			java.lang.String transitionId, java.lang.String pluginName) {
		return workflowDesign.getTransitionPluginByTsId(transitionId, pluginName);
	}

	/**
	 * 根据迁移线Id得到迁移线插件属性信息列表
	 * 
	 * @param transitionId
	 *            迁移线Id
	 * @return List 迁移线插件属性信息列表
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List<com.strongit.workflow.bo.TwfBaseTransitionPlugin> getTransitionPluginsByTsId(
			java.lang.String transitionId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDesign.getTransitionPluginsByTsId(transitionId);
	}

	/**
	 * 根据流程实例Id得到该流程实例的所有处理意见信息集
	 * 
	 * @param processInstanceId
	 *            流程实例Id
	 * @return List 处理意见信息集
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List<com.strongit.workflow.bo.TwfInfoApproveinfo> getProcessApproveinfoByPiIdForList(
			java.lang.String processInstanceId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation.getProcessApproveinfoByPiIdForList(processInstanceId);
	}

	/**
	 * 得到流程处理意见,含当前流程的父流程、子流程、兄弟流程办理意见
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,第一个委派/指派人名称,意见记录id
	 *         ,处理人机构名称,最后一个委派/指派人名称,任务处理类型]>>
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String[]>> getWorkflowApproveinfo(String instanceId,String taskId)
			throws SystemException {
		Map<String, List<String[]>> appMap = new HashMap<String, List<String[]>>();
		try {
			appMap = sendDocApproveinfoManager.getWorkflowApproveinfo(instanceId,taskId);
		} catch (WorkflowException ex) {
			throw new SystemException(ex);
		}
		return appMap;
	}

	/**
	 * author:luosy 2013-5-18
	 * description: 得到流程处理意见,只含当前流程流程办理意见
	 * modifyer:
	 * description:
	 * @param instanceId
	 *            流程实例id
	 * @return Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,第一个委派/指派人名称,意见记录id
	 *         ,处理人机构名称,最后一个委派/指派人名称,任务处理类型]>>
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String[]>> getThisWorkflowApproveinfo(String instanceId)
			throws SystemException {
		Map<String, List<String[]>> appMap = new HashMap<String, List<String[]>>();
		try {
			appMap = sendDocApproveinfoManager.getThisWorkflowApproveinfo(instanceId);
		} catch (WorkflowException ex) {
			throw new SystemException(ex);
		}
		return appMap;
	}
	
	/**
	 * 根据流程实例Id得到该流程实例的所有处理意见分页信息
	 * 
	 * @param page
	 * @param processInstanceId
	 *            流程实例Id
	 * @return Page 处理意见分页信息
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public com.strongmvc.orm.hibernate.Page<com.strongit.workflow.bo.TwfInfoApproveinfo> getProcessApproveinfoByPiIdForPage(
			com.strongmvc.orm.hibernate.Page page,
			java.lang.String processInstanceId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation.getProcessApproveinfoByPiIdForPage(page, processInstanceId);
	}

	/**
	 * 根据节点Id得到流程节点类型
	 * 
	 * @author 喻斌
	 * @date 2011-7-19 上午07:10:01
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
	public String checkNodeTypeByNodeId(String nodeId) throws WorkflowException {
		return this.workflowDesign.checkNodeTypeByNodeId(nodeId);
	}

	/**
	 * 根据节点Id得到该节点下一步的所有节点信息
	 * 
	 * @author 喻斌
	 * @date 2011-7-26 上午06:24:28
	 * @param nodeId
	 *            -节点Id
	 * @return List<Object[]> 下一步的所有节点信息<br>
	 *         数据格式为：{(0)节点Id，(1)节点名称，(2)节点类型(节点类型值参考
	 *         {@link #checkNodeTypeByNode(Node)})}
	 * @throws WorkflowException
	 */
	public List<Object[]> getNextNodesByNodeId(String nodeId)
			throws WorkflowException {
		return this.workflowDesign.getNextNodesByNodeId(nodeId);
	}

	/**
	 * 根据任务实例Id得到该节点下一步的所有节点信息
	 * 
	 * @author 喻斌
	 * @date 2011-7-26 上午06:24:28
	 * @param nodeId
	 *            -节点Id
	 * @return List<Object[]> 下一步的所有节点信息<br>
	 *         数据格式为：{(0)节点Id，(1)节点名称，(2)节点类型(节点类型值参考
	 *         {@link #checkNodeTypeByNode(Node)})}
	 * @throws WorkflowException
	 */
	public List<Object[]> getNextNodesByTaskInstanceId(String taskInstanceId)
			throws WorkflowException {
		return this.workflowDesign.getNextNodesByTaskInstanceId(taskInstanceId);
	}

	/**
	 * 根据流程实例Id得到流程运行情况
	 * 
	 * @author 喻斌
	 * @date 2011-8-5 上午09:28:27
	 * @param processInstanceId
	 *            -流程实例Id
	 * @return Object[] 流程实例运行情况<br>
	 *         数据格式为：Object[]{(0)流程实例Id，(1)流程名称，(2)流程业务Id，(3)流程业务名称，(4)流程发起人Id，(5
	 *         )流程发起人名称，(6)流程运行情况}<br>
	 *         流程运行情况格式为：Collection<Object[]{(0)任务或子流程标志，(1)节点名称，(2)进入节点时间，(3)任务处理人Id
	 *         ，多个以,分隔}><br>
	 *         其中任务或子流程标志为：“task”：表示未完成的任务；“subProcess”：表示正在执行的子流程；
	 * @throws WorkflowException
	 */
	public Object[] getProcessStatusByPiId(String processInstanceId)
			throws WorkflowException {
		return workflowDelegation.getProcessStatusByPiId(processInstanceId);
	}

	/**
	 * 获取JBPM上下文公共方法
	 * 
	 * @return
	 */
	public org.jbpm.JbpmContext getJbpmContext() {
		return workflowDelegation.getJbpmContext();
	}

	/**
	 * 根据流程实例Id和节点Id得到所在节点所有子流程实例信息
	 * 
	 * @param processInstatnceId
	 *            流程实例Id
	 * @param nodeId
	 *            节点Id
	 * @return List 流程父流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务名称，(2)流程名称}
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List<java.lang.Object[]> getSubProcessesByPiIdAndNodeId(
			java.lang.String processInstatnceId, java.lang.String nodeId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowRun.getSubProcessesByPiIdAndNodeId(processInstatnceId, nodeId);
	}

	/**
	 * 根据流程名称得到最新版本流程中节点信息集
	 * 
	 * @param processName
	 *            流程名称
	 * @return 流程节点信息集
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List<com.strongit.workflow.bo.TwfBaseNodesetting> getNodeInfosByProcessName(
			java.lang.String processName)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDesign.getNodeInfosByProcessName(processName);
	}

	/**
	 * 根据流程实例Id得到指定节点上任务的处理记录
	 * 
	 * @param processinstanceId
	 *            流程实例Id
	 * @param nodeId
	 *            节点Id
	 * @returnist 任务处理记录 数据结构为：
	 *            Object[]{(0)任务开始时间,(1)任务结束时间,(2)任务处理人名称,(3)任务处理意见
	 *            ,(4)任务处理时间,(5)任务是否超时,(6)任务处理记录Id,(7)审批数字签名}
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List getHandleRecordByNode(
			java.lang.String processinstanceId, java.lang.String nodeId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowRun.getHandleRecordByNode(processinstanceId, nodeId);
	}

	/**
	 * 根据任务实例Id得到该任务上一步任务信息
	 * 
	 * @param taskInstanceId
	 *            任务实例Id
	 * @return java.util.List<com.strongit.workflow.po.TaskInstanceBean>
	 *         上一步任务信息集，包括任务信息和处理人信息
	 */
	public java.util.List<com.strongit.workflow.po.TaskInstanceBean> getTruePreTaskInfosByTiId(
			java.lang.String taskInstanceId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowRun.getTruePreTaskInfosByTiId(taskInstanceId);
	}

	/**
	 * 重新设置流程实例定时器信息
	 * 
	 * @param processInstanceId
	 *            流程实例Id
	 * @param reSetInfo
	 *            定时器设置信息；数据格式为{(0)定时器到期日期【时间类型】，(1)重复提醒的时间间隔【字符串类型】}
	 *            “重复提醒的时间间隔”数据格式为：XX day/hour/minute，其中XX为数字，分别表示 天、小时和分钟
	 * @return boolean “true”：成功；“false”：失败
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public boolean reSetProcessTimer(java.lang.String processInstanceId,
			java.lang.Object[] reSetInfo, OALogInfo... infos)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation.reSetProcessTimer(processInstanceId, reSetInfo);
	}

	/**
	 * 动态设置定时器最近一次需要进行处理的时间，这样当定时器结束睡眠运行时，若已到达该时间则执行业务功能，否则继续睡眠 
	 * 
	 * @param idleInterval	最近一次需要进行处理的时间毫秒数 
	 * @throws com.strongit.workflow.exception.WorkflowException
	 * @createTime May 15, 2012 4:46:26 PM
	 */
	public void setJobExecutorTime(long idleInterval)
			throws com.strongit.workflow.exception.WorkflowException {
		workflowDesign.setJobExecutorTime(idleInterval);
	}
	
	/**
	 * 将任务由指定人员委托给指定的被委托人员，若是该被委托人员还有其它委托设置，则会自动进行继续委托
	 * 
	 * @author 喻斌
	 * @date 2011-12-7 下午04:48:20
	 * @param taskInstanceId
	 * @param fromUserId
	 * @param toUserId
	 * @throws WorkflowException
	 */
	@Transactional(readOnly = false)
	public void delegateTaskInstance(String taskInstanceId, String fromUserId,
			String toUserId) throws WorkflowException {
		workflowRun.delegateTaskInstance(taskInstanceId, fromUserId, toUserId);
	}

	/**
	 * 根据流程实例Id得到指定节点上任务的当前处理情况（流程监控使用）
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 9, 2011 10:27:33 PM
	 * @return java.util.List
	 *         Object[]{(0)任务开始时间,(1)任务处理人信息,(2)任务是否已经签收,(3)任务是否超时
	 *         ,(4)任务处理人Id信息,(5)任务实例Id}
	 */
	public java.util.List getCurrentHandleByNode(
			java.lang.String processinstanceId, java.lang.String nodeId)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowRun.getCurrentHandleByNode(processinstanceId, nodeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.strongit.oa.common.workflow.IWorkflowService#
	 * getProcessInstanceByConditionForList(java.util.List, java.util.Map,
	 * java.util.Map, java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Map)
	 */
	public java.util.List<java.lang.Object[]> getProcessInstanceByConditionForList(
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			java.util.Map<java.lang.String, java.lang.Object> customValues)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, orderMap, customSelectItems,
				customFromItems, customQuery, customValues);
	}

	/**
	 * 
	 * 支持自定义sql语句
	 * 
	 * @author 严建
	 * @createTime Dec 10, 2011 4:08:34 PM
	 * @return java.util.List<java.lang.Object[]>
	 */
	public List<Object[]> getProcessInstanceByConditionForList(
			List<String> toSelectItems, Map<String, Object> paramsMap,
			Map<String, String> orderMap, String customSelectItems,
			String customFromItems, String customQuery,
			List<Object> customValues, String customOrderBy)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation.getProcessInstanceByConditionForList(
				toSelectItems, paramsMap, orderMap, customSelectItems,
				customFromItems, customQuery, customValues, customOrderBy);
	}

	public void saveApproveInfo(TwfInfoApproveinfo approveInfo) {
		oaSystemService.saveApproveInfo(approveInfo);
	}

	public TwfInfoApproveinfo getApproveInfoById(String id) {
		return oaSystemService.getApproveInfoById(id);
	}

	public List getDataByHql(String hql, Object[] values) {
		return workflowDelegation.getDataByHql(hql, values);
	}

	/**
	 * @method repealProcess
	 * @author 申仪玲
	 * @created 2011-12-20 下午04:08:29
	 * @description 挂起流程实例
	 * @return boolean 返回类型
	 */
	public boolean repealProcess(String instanceId, String flag,OALogInfo... infos) {
		ContextInstance cxt;
		String subType = null;
		List<Object[]> parList = workflowDelegation
				.getMonitorParentInstanceIds(Long.parseLong(instanceId));
		if (parList != null && !parList.isEmpty()) {// 存在父流程
			for (int i = 0; i < parList.size(); i++) {
				Object[] objs = (Object[]) parList.get(i);
				String parInstanceId = objs[0].toString();
				cxt = this.getContextInstance(instanceId);
				subType = (String) cxt.getVariable("com.strongit.subType");// 是否是同步
				if (subType != null && "1".equals(subType)) {// 挂起同步父流程
					workflowDelegation.changeProcessInstanceStatus(
							parInstanceId, flag);
				}
			}
		}
		/**
		 * 废除--当父流程走到子流程的第一个节点,并且子流程的发起人不指定
		 * 
		 * */
		Object[] obj=workflowDelegation.getProcessInstanceByPiId(instanceId);
		if(obj[5]==null){
			Connection con = this.getConnection();
			try {
				con.setAutoCommit(false);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PreparedStatement psmt = null;
		  User currentUser=userService.getCurrentUser();
		  try {
					String sql = "UPDATE "
						+ "JBPM_PROCESSINSTANCE SET START_USER_ID_ = ?,START_USER_NAME_= ? "
						+ "WHERE ID_ ='"+instanceId+"'";
				psmt = con.prepareStatement(sql);
				psmt.setString(1, currentUser.getUserId());
				psmt.setString(2, currentUser.getUserName());
				psmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return workflowDelegation.changeProcessInstanceStatus(instanceId, flag);
	}
	
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
	 * 改变流程当前状态
	 * 
	 * @param instanceId
	 *            流程实例Id
	 * @param flag
	 *            改变标识 “1”：挂起流程 “2”：恢复流程 “3”：结束流程
	 * @return boolean 改变成功、失败
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public boolean changeProcessInstanceStatus(java.lang.String instanceId,
			java.lang.String flag)
			throws com.strongit.workflow.exception.WorkflowException {
		return workflowDelegation.changeProcessInstanceStatus(instanceId, flag);
	}

	/**
	 * @method returnProcess
	 * @author 申仪玲
	 * @created 2011-12-20 下午04:08:11
	 * @description 恢复流程实例
	 * @return boolean 返回类型
	 */
	public boolean returnProcess(String instanceId, String flag,OALogInfo... infos) {
		String[] intId = instanceId.split(",");
		ContextInstance cxt;
		String subType = null;
		for (int i = 0; i < intId.length; i++) {
			List<Object[]> parList = workflowDelegation
					.getMonitorParentInstanceIds(Long.parseLong(intId[i]));
			if (parList != null && !parList.isEmpty()) {// 存在父流程
				for (int j = 0; j < parList.size(); j++) {
					Object[] objs = (Object[]) parList.get(j);
					String parInstanceId = objs[0].toString();
					cxt = this.getContextInstance(instanceId);
					subType = (String) cxt.getVariable("com.strongit.subType");// 是否是同步
					if (subType != null && "1".equals(subType)) {// 恢复同步父流程
						workflowDelegation.changeProcessInstanceStatus(
								parInstanceId, flag);
					}
				}
			}
		}
		return workflowDelegation.changeProcessInstanceStatus(instanceId, flag);
	}

	/*
	 * @see com.strongit.oa.common.workflow.IWorkflowService#delProcess(java.lang.String)
	 *      @method delProcess @author 申仪玲 @created 2012-1-7 下午01:59:51
	 *      @description 删除挂起的流程，存在同步父流程和子流程，则一起删除 @return
	 */
	public boolean delProcess(String instanceId) {
		String[] intId = instanceId.split(",");
		ContextInstance cxt;
		String subType = null;
		for (int i = 0; i < intId.length; i++) {
/*			List<Object[]> parList = workflowDelegation
					.getMonitorParentInstanceIds(Long.parseLong(intId[i]));*/
			List<Object[]> chiList = workflowDelegation.getMonitorChildrenInstanceIds(Long.parseLong(intId[i]));
			if (chiList != null && !chiList.isEmpty()) {// 存在子流程
				for (int k = 0; k < chiList.size(); k++) {
					Object[] objs = (Object[]) chiList.get(k);
					String chiInstanceId = objs[0].toString();
					cxt = this.getContextInstance(chiInstanceId);
					subType = (String) cxt.getVariable("com.strongit.subType");// 是否是同步
					if (subType != null && "1".equals(subType)) {// 删除同步子流程
						workflowDelegation.delProcessInstances(chiInstanceId);
					}
				}
			}
/*			if (parList != null && !parList.isEmpty()) {// 存在父流程
				for (int j = 0; j < parList.size(); j++) {
					Object[] objs = (Object[]) parList.get(j);
					String parInstanceId = objs[0].toString();
					cxt = this.getContextInstance(instanceId);
					subType = (String) cxt.getVariable("com.strongit.subType");// 是否是同步
					if (subType != null && "1".equals(subType)) {// 删除同步父流程
						workflowDelegation.delProcessInstances(parInstanceId);
					}
				}
			}*/
				//删除本流程实例
				workflowDelegation.delProcessInstances(intId[i]);
		}
		return true;
	}

	/**
	 * 清空当前流程所有数据（含子流程及业务数据），不删除当前流程实例。 返回当前流程的业务表主键数据：表名;主键名;主键值
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @param taskId
	 *            任务实例id
	 * @return 业务数据
	 */
	@SuppressWarnings("unchecked")
	public String deleteProcessInstanceRelateInfo(String instanceId,
			String taskId) {
		JbpmContext jbpmContext = this.getJbpmContext();
		try {
			// 找到所有子流程
			List<Object[]> children = this.getMonitorChildrenInstanceIds(new Long(instanceId));
			// 去除当前任务的“退回”属性
			TaskInstance taskInstance = this.getTaskInstanceById(taskId);
			taskInstance.setStart(new Date());
			taskInstance.setIsBackspace("0");
			if (!children.isEmpty()) {
				for (Object[] objs : children) {
					// 删除业务数据
					getServiceDAO().delete(
									getServiceDAO().find(
													"from TwfInfoBuspdi tb where tb.aiPiId=?",
													new Object[] { new Long(objs[0].toString()) }));
					// 删除子流程办理意见
					getServiceDAO().delete(
							getServiceDAO().find(
													"from TwfInfoApproveinfo ta where ta.aiPiId=?",
													new Object[] { new Long(objs[0].toString()) }));
					// 删除子流程数据
					getServiceDAO().delete(
									getServiceDAO().find("from TwfBaseSubprocess tb where tb.subProcessId=?",
													new Object[] { new Long(objs[0].toString()) }));
					// 删除流程实例
					jbpmContext.getGraphSession().deleteProcessInstance(
							new Long(objs[0].toString()).longValue());
				}
			}
			// 删除业务数据
			/*
			 * getServiceDAO().delete( getServiceDAO().find( "from TwfInfoBuspdi
			 * tb where tb.aiPiId=?", new Object[] { new Long(instanceId) }));
			 */
			// 删除流程办理意见
			getServiceDAO().delete(
					getServiceDAO().find("from TwfInfoApproveinfo ta where ta.aiPiId=?",
											new Object[] { new Long(instanceId) }));
			// 删除子流程数据
			getServiceDAO().delete(
							getServiceDAO().find("from TwfBaseSubprocess tb where tb.subProcessId=?",
											new Object[] { new Long(instanceId) }));
			ProcessInstance pi = jbpmContext.getProcessInstance(Long.valueOf(
					instanceId).longValue());
			/*
			 * Session session = getServiceDAO().getSession();
			 * 
			 * Query query = session.getNamedQuery(
			 * "GraphSession.findTokensForProcessInstance");
			 * query.setEntity("processInstance", pi); List tokens =
			 * query.list();
			 * 
			 * Iterator iter = tokens.iterator();
			 * 
			 * query = session
			 * .getNamedQuery("GraphSession.deleteJobsForProcessInstance");
			 * query.setEntity("processInstance", pi); query.executeUpdate();
			 * 
			 * query = session
			 * .getNamedQuery("GraphSession.findTaskInstanceIdsForProcessInstance" );
			 * query.setEntity("processInstance", pi); List taskInstanceIds =
			 * query.list();
			 * 
			 * query = session
			 * .getNamedQuery("GraphSession.deleteTaskInstancesById");
			 * query.setParameterList("taskInstanceIds", taskInstanceIds);
			 * 
			 * query =
			 * session.getNamedQuery("GraphSession.selectLogsForTokens");
			 * query.setParameterList("tokens", tokens); List logs =
			 * query.list(); iter = logs.iterator(); while (iter.hasNext()) {
			 * session.delete(iter.next()); }
			 */
			User user = userService.getCurrentUser();
			// 添加流程日志时使用
			String businessName = String.valueOf(pi.getContextInstance()
					.getVariable(WorkflowConst.WORKFLOW_BUSINESSNAME));
			StringBuilder logInfo = new StringBuilder("用户[").append(
					user.getUserName()).append("]");
			logInfo.append("重新办理流程[").append(businessName).append("]");
			ToaLog log = new ToaLog();
			String ip = ServletActionContext.getRequest().getRemoteAddr();
			log.setOpeIp(ip); // 操作者IP地址
			log.setOpeUser(user.getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo(logInfo.toString());// 日志信息
			logger.error(logInfo.toString());
			log.setLogModule("重新办理流程");
			logService.saveObj(log);
			return pi.getBusinessId();// 返回当前流程的业务数据
		} finally {
			jbpmContext.close();
		}
	}

	/**
	 * 根据流程实例得到流程当前处理人信息（含子流程和父流程）
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return TaskBean 任务信息bean
	 */
	@SuppressWarnings("unchecked")
	public TaskBean getCurrentTaskHandle(String instanceId) {
		Object[] objs = this.getProcessStatusByPiId(instanceId);
		TaskBean bean = new TaskBean();
		bean.setBsinessId((String) objs[2]);
		bean.setBusinessName((String) objs[3]);
		bean.setStartUserId((String) objs[4]);
		bean.setStartUserName((String) objs[5]);
		bean.setWorkflowName((String) objs[1]);
		bean.setInstanceId((String) objs[0]);
		Collection<Object[]> currentTaskInfo = (Collection<Object[]>) objs[6];
		List<User> users = new ArrayList<User>();
		List<String> usersId = new ArrayList<String>();
		if (currentTaskInfo != null && !currentTaskInfo.isEmpty()) {// 得到当前任务处理情况
			for (Object[] taskInfos : currentTaskInfo) {
				String taskFlag = (String) taskInfos[0];// 得到任务处理标志
				// “task”：表示未完成的任务；“subProcess”：表示正在执行的子流程
				if ("task".equals(taskFlag)) {
					String currentUserId = (String) taskInfos[3];// 任务处理人Id，多个以,分隔
					if (currentUserId != null && currentUserId.length() > 0) {
						String[] actorIds = currentUserId.split(",");
						for (String actorId : actorIds) {
							usersId.add(actorId);
						}
					}
				}
			}
		}
		List<Object[]> childrenInstances = this
		.getMonitorChildrenInstanceIds(new Long(instanceId));// 得到所有子流程
		if (childrenInstances != null && !childrenInstances.isEmpty()) {
			for (Object[] childrenInstance : childrenInstances) {
				String childInstance = childrenInstance[0].toString();// 得到子流程实例id
				objs = this.getProcessStatusByPiId(childInstance);
				currentTaskInfo = (Collection<Object[]>) objs[6];
				if (currentTaskInfo != null && !currentTaskInfo.isEmpty()) {// 得到当前任务处理情况
					for (Object[] taskInfos : currentTaskInfo) {
						String taskFlag = (String) taskInfos[0];// 得到任务处理标志
						// “task”：表示未完成的任务；“subProcess”：表示正在执行的子流程
						if ("task".equals(taskFlag)) {
							String currentUserId = (String) taskInfos[3];// 任务处理人Id，多个以,分隔
							if (currentUserId != null
									&& currentUserId.length() > 0) {
								String[] actorIds = currentUserId.split(",");
								for (String actorId : actorIds) {
									usersId.add(actorId);
								}
							}
						}
					}
				}
			}
		}
		List<Object[]> parentInstances = this
				.getMonitorParentInstanceIds(new Long(instanceId));// 得到所有父流程
		if (parentInstances != null && !parentInstances.isEmpty()) {
			for (Object[] childrenInstance : parentInstances) {
				String childInstance = childrenInstance[0].toString();// 得到子流程实例id
				objs = this.getProcessStatusByPiId(childInstance);
				currentTaskInfo = (Collection<Object[]>) objs[6];
				if (currentTaskInfo != null && !currentTaskInfo.isEmpty()) {// 得到当前任务处理情况
					for (Object[] taskInfos : currentTaskInfo) {
						String taskFlag = (String) taskInfos[0];// 得到任务处理标志
						// “task”：表示未完成的任务；“subProcess”：表示正在执行的子流程
						if ("task".equals(taskFlag)) {
							String currentUserId = (String) taskInfos[3];// 任务处理人Id，多个以,分隔
							if (currentUserId != null
									&& currentUserId.length() > 0) {
								String[] actorIds = currentUserId.split(",");
								for (String actorId : actorIds) {
									usersId.add(actorId);
								}
							}
						}
					}
				}
			}
		}
		if(usersId != null && !usersId.isEmpty()){
			Map<String, Object[]> map = customUserService.getUserInfoMap(usersId);
			boolean mapIsEmptyOrNull = (map == null || map.isEmpty());
			User user = null;
			if(!mapIsEmptyOrNull){
				Iterator it = map.keySet().iterator();
				while(it.hasNext()){
					String dd = it.next().toString();
					for(String userId:usersId){
						if(dd.equals(userId)){
							//用户id，用户名称，所在部门id，所在部门名称
							Object[] objectarr = map.get(userId);
							user = new User();
							user.setUserId(StringUtil.castString(objectarr[0]));
							user.setUserName(StringUtil.castString(objectarr[1]));
							user.setOrgId(StringUtil.castString(objectarr[2]));
							user.setOrgName(StringUtil.castString(objectarr[3]));
							user.setRest1(StringUtil.castString(objectarr[4]));
							user.setOrgSequence(new Long(StringUtil.castString(objectarr[5])));
							user.setUserSequence(new Long(StringUtil.castString(objectarr[6])));
							users.add(user);
						}
					}
				}
			}else{
				for(String userId:usersId){
					user = userService.getUserInfoByUserId(userId);
					if (user != null) {
						String orgId = user.getOrgId();
						Organization organization = userService.getDepartmentByOrgId(orgId);
						user.setOrgName(organization.getOrgName());
					}
				}
			}
		}
		bean.setActors(users);
		return bean;
	}

	/**
	 * 得到上一任务处理人信息
	 * 
	 * @param taskInstanceId
	 *            任务实例id
	 * @return 上一任务信息
	 */
	public TaskBean getPrevTaskHandler(String taskInstanceId) {
		List<TaskInstanceBean> taskList = this.getTruePreTaskInfosByTiId(taskInstanceId);
		TaskBean bean = new TaskBean();
		List<User> users = new ArrayList<User>();
		if (taskList != null && !taskList.isEmpty()) {
			for (TaskInstanceBean taskInstanceBean : taskList) {
				String userId = taskInstanceBean.getTaskActorId();
				User user = userService.getUserInfoByUserId(userId);
				if (user != null) {
					String orgId = user.getOrgId();
					Organization organization = userService.getDepartmentByOrgId(orgId);
					user.setOrgName(organization.getOrgName());
					users.add(user);
				}
			}
		}
		bean.setPrevActors(users);
		return bean;
	}

	/**
	 * @method isBackTask
	 * @author 申仪玲
	 * @created 2011-12-28 上午11:49:19
	 * @description 根据任务ID判断该任务是否是退回任务
	 * @return String "0":不是退回任务；"1":是退回任务
	 */
	public String isBackTask(String taskId) {
		String isBackTask = "0";
		JbpmContext jbpmContext = this.getJbpmContext();
		try {
			TaskInstance taskInstance = jbpmContext.getTaskInstance(new Long(
					taskId));
			if (taskInstance != null) {
				if ("1".equals(taskInstance.getIsBackspace())) {// 是退回任务
					isBackTask = "1";
				}
			}
		} finally {
			jbpmContext.close();
		}
		return isBackTask;
	}
	
	/**
	 * 根据任务id判断任务是否为退回任务
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 21, 2012 12:28:44 PM
	 */
	public boolean isBackTaskByTid(String taskId){
		return isBackTask(getTaskInstanceById(taskId));
	} 
	
	/**
	 * 判断任务是否为退回任务
	 * 
	 * @author 严建
	 * @param taskInstance
	 * @return
	 * @createTime Mar 21, 2012 12:27:21 PM
	 */
	public boolean isBackTask(TaskInstance taskInstance) {
		if (taskInstance != null) {
			if ("1".equals(taskInstance.getIsBackspace())) {// 是退回任务
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 得到指定的任务之前的节点列表
	 * 
	 * @param taskId
	 *            任务实例id
	 * @return List<TaskBean>
	 * @throws SystemException
	 *             参数错误或数据不存在时会引发此异常
	 */
	@SuppressWarnings("unchecked")
	public List<TaskBean> getPreTaskNodeList(String taskId)
			throws SystemException {
		return nodeService.getPreTaskNodeList(taskId);
	}
	
	/**
	 * 驳回时显示父流程可驳回的数据
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @throws SystemException
	 * @createTime Mar 20, 2012 3:57:26 PM
	 */
	@SuppressWarnings("unchecked")
	public List<TaskBean> getSuperTaskNodeList(String taskId)throws SystemException {
		return nodeService.getSuperTaskNodeList(taskId);
	}
	
	/**
	 * 根据任务对应的节点id判断对应节点是否为签收节点
	 * 
	 * @author 严建
	 * @param taskNodeId
	 *            任务对应的节点id
	 * @return result 【true:是签收节点任务|false:不是签收节点任务】
	 * @createTime Jan 5, 2012 3:24:29 PM
	 */
	public boolean isSignNodeTask(String taskNodeId) {
		boolean result = false;
		TwfBaseNodesetting nodeSetting = this
				.getNodesettingByNodeId(taskNodeId);
		result = isSignNodeTask(taskNodeId, nodeSetting);
		return result;
	}

	/**
	 * 根据任务对应的节点id判断对应节点是否为签收节点
	 * 
	 * @author 严建
	 * @param taskNodeId
	 *            任务对应的节点id
	 * @param nodeSetting
	 *            节点信息
	 * @return result 【true:是签收节点任务|false:不是签收节点任务】
	 * @createTime Jan 6, 2012 12:04:08 PM
	 */
	public boolean isSignNodeTask(String taskNodeId, TwfBaseNodesetting nodeSetting) {
		boolean result = false;
		String isNotSign = nodeSetting.getPlugin("plugins_chkModifySuggestion");
		if (isNotSign != null && "1".equals(isNotSign)) {// 签收节点上的任务
			result = true;
		}
		return result;
	}

	/**
	 * 根据流程实例id得到流程实例对象
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 流程实例对象
	 */
	public ProcessInstance getProcessInstanceId(String instanceId) {
		JbpmContext jbpmContext = getJbpmContext();
		try {
			return jbpmContext.getProcessInstance(new Long(instanceId));
		} finally {
			jbpmContext.close();
		}
	}

	/**
	 * 根据流程实例id获取流程主办人员id
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 9, 2012 10:26:23 AM
	 */
	public String getMainActorIdByProcessInstanceId(String instanceId){
		return getMainActorInfoByProcessInstanceId(instanceId).getUserId();
	}
	
	/**
	 * 获取主办人员信息
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:38:17 PM
	 */
	public UserBeanTemp getMainActorInfoByProcessInstanceId(String processInstanceId) {
		return mainActorConfigManage.getMainActorInfoByProcessInstanceId(processInstanceId);
	}
	
	/**
	 * 根据任务实例id获取流程主办人员id
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 9, 2012 10:26:23 AM
	 */
	public String getMainActorIdByTaskInstanceId(String taskInstanceId){
		return getMainActorInfoByTaskInstanceId(taskInstanceId).getUserId();
	}
	
	/**
	 * 根据任务实例id获取主办人员指派人员id
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 24, 2012 5:09:03 PM
	 */
	public String getMainReassignActorIdByTaskInstanceId(String taskInstanceId){
		return getMainReassignActorInfoByTaskInstanceId(taskInstanceId).getUserId();
	}
	
	/**
	 * 根据任务实例id获取主办人员信息
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:24:11 PM
	 */
	public UserBeanTemp getMainActorInfoByTaskInstanceId(String taskInstanceId){
		return mainActorConfigManage.getMainActorInfoByTaskInstanceId(taskInstanceId);
	}
	
	/**
	 * 根据任务实例id获取主办人员指派人员信息
	 * 
	 * @description
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 24, 2012 5:06:09 PM
	 */
	public UserBeanTemp getMainReassignActorInfoByTaskInstanceId(String taskInstanceId){
		return mainReassignActorConfigManage.getMainReassignActorInfoByTaskInstanceId(taskInstanceId);
	}
	
	/**
	 * 根据流程名称获取流程第一个节点的id
	 * 
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime Mar 12, 2012 11:11:41 AM
	 */
	@SuppressWarnings("unchecked")
	public Long getFirstNodeId(String workflowName) {
		return nodeService.getFirstNodeId(workflowName);
	}
	
	public DefinitionPluginService getDefinitionPluginService() {
		return definitionPluginService;
	}
	
	/**
	 * 是否拥有主办权限
	 * 
	 * @author 严建
	 * @param userId
	 * @param taskId
	 * @return
	 * @createTime Mar 24, 2012 5:37:40 PM
	 */
	public boolean hasMainDoing(String userId, String taskId) {
		if (userId.equals(getMainActorIdByTaskInstanceId(taskId))
				|| userId
						.equals(getMainReassignActorIdByTaskInstanceId(taskId))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 挂起意见征询信息
	 * 
	 * @author 严建
	 * @param businessId
	 * @createTime Apr 7, 2012 10:01:08 PM
	 */
	public void suspendYjzx(String businessId) {
		Tjbpmbusiness model = jbpmBusinessManager.findByBusinessId(businessId);
		if(model != null){			
			if ("1".equals(model.getBusinessType())) {
				model.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_YJZX_SUSPEND);
				jbpmBusinessManager.saveModel(model);
			}
		}
	}

	/**
	 * 恢复意见征询信息
	 * 
	 * @author 严建
	 * @param businessId
	 * @createTime Apr 7, 2012 10:05:12 PM
	 */
	public void resumeYjzx(String businessId) {
		Tjbpmbusiness model = jbpmBusinessManager.findByBusinessId(businessId);
		if(model != null){	
			if (Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_YJZX_SUSPEND.equals(model.getBusinessType())) {
				model.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_COMMON);
				jbpmBusinessManager.saveModel(model);
			}
		}
	}
	
	/**
	 * 递归结束流程实例 
	 * 
	 * @description
	 * @author 严建
	 * @param jbpmContext	JBPM上下文
	 * @param instanceId	流程实例Id
	 * @param reason	结束流程实例未完成任务的原因
	 * @throws com.strongit.workflow.exception.WorkflowException
	 * @createTime Apr 27, 2012 2:13:49 PM
	 */
	public void loopEndProcess(org.jbpm.JbpmContext jbpmContext,
                java.lang.String instanceId,
                java.lang.String reason,OALogInfo... infos)
                throws com.strongit.workflow.exception.WorkflowException{
	    workflowDelegation.loopEndProcess(jbpmContext, instanceId, reason);
	}
	
	/**
	 * 根据流程实例Id获取流程中每个节点的处理意见和对应的节点设置信息
	 * @author 喻斌
	 * @date Dec 19, 2009 2:20:19 PM
	 * @param processInstanceIds -流程实例Id集合
	 * @return List<Object[]> -每个节点的处理意见和对应的节点设置信息<br>
	 * 		<p>Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象, (9)审批数字签名, (10)任务委派类型【0：委托；1：指派；“”：非委托】, (11)最后一个委托/指派人Id, (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称, (15)主键值, (16)流程实例Id}</p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getProcessHandlesAndNodeSettingByPiIds(List<Long> processInstanceIds) throws WorkflowException{
		try {
			return workflowDelegation.getProcessHandlesAndNodeSettingByPiIds(processInstanceIds);
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
	 * 获取流程的所有子流程实例信息
	 * @author 喻斌
	 * @date Apr 15, 2009 11:11:32 AM
	 * @param processInstanceId -流程实例Id
	 * @return List<Object[]> 流程子流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务名称，(2)流程名称}
	 * @throws WorkflowException
	 */
	public List<Object[]> getSubProcessInstanceInfos(String processInstanceId)throws WorkflowException{
		try {
			return workflowDelegation.getSubProcessInstanceInfos(processInstanceId);
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
	 * 获取流程的所有父流程实例信息
	 * 
	 * @author 喻斌
	 * @date Apr 15, 2009 11:11:32 AM
	 * @param processInstanceId
	 *            -流程实例Id
	 * @return List<Object[]> 流程父流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务
	 * 
	 * 名称，(2)流程名称}
	 * @throws WorkflowException
	 */
	public List<Object[]> getSupProcessInstanceInfos(String processInstanceId)throws WorkflowException{
		try {
			return workflowDelegation.getSupProcessInstanceInfos(processInstanceId);
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
	 * 根据流程实例Id获取流程中每个节点的处理意见和对应的节点设置信息
	 * @author jiangguobin
	 * @date Dec 19, 2009 2:20:19 PM
	 * @param processInstanceIds -流程实例Id集合
	 * @return List<Object[]> -每个节点的处理意见和对应的节点设置信息<br>
	 * 		<p>Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象, (9)审批数字签名, (10)任务委派类型【0：委托；1：指派；“”：非委托】, (11)最后一个委托/指派人Id, (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称, (15)主键值, (16)流程实例Id}</p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getBusiFlagByProcessInstanceIds(
			List<Long> processInstanceIds) throws WorkflowException {
		return workflowDelegation
		.getProcessHandlesAndNodeSettingByPiIds(processInstanceIds);
	}

    /**
	 * 监控时批量查询流程子流程实例信息
	 * @author	蔡德伍
	 * @date	2014-4-17 19:51:43
	 * @param instanceId -流程实例Id
	 * @return List<Object[]> 流程子流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务名称，(2)流程名称}
	 * @throws WorkflowException
	 */
	public List<Object[]> getMonitorChildrenInstanceIds(List<Long> instanceIds)
			throws WorkflowException {
		List<Object[]> children = null;
		if(instanceIds != null){
			//List<TwfBaseSubprocess> childrenIds = getServiceDAO().find("from TwfBaseSubprocess ts where ts.twfInfoBuspdi.aiPiId=?", new Object[]{instanceId});
			Query query = this.getServiceDAO().getSession().createQuery("from TwfBaseSubprocess ts where ts.twfInfoBuspdi.aiPiId in (:piIds)");
			query.setParameterList("piIds", instanceIds);
			List<TwfBaseSubprocess> childrenIds = query.list();
			if(childrenIds != null && !childrenIds.isEmpty()){
				StringBuffer hql = new StringBuffer("select tb.aiPiId, tb.aiBiName, pi.processDefinition.name from TwfInfoBuspdi tb, org.jbpm.graph.exe.ProcessInstance pi where pi.id=tb.aiPiId and (1<>1");
				for(TwfBaseSubprocess ts : childrenIds){
					hql.append(" or tb.aiPiId = ").append(ts.getSubProcessId());
				}
				hql.append(") order by tb.aiPiId desc");
				children = getServiceDAO().find(hql.toString());
			}
		}
		return children;
	}

	/**
	 * 根据用户id批量获取用户所属部门信息
	 *
	 * @author 蔡德伍
	 * @company		Thinvent Ltd. (C) copyright
	 * @date		2014-4-19 下午2:43:39
	 * @param userIds	用户id
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getOrgInfosByUserIds(List<String> userIds)
			throws DAOException, SystemException, ServiceException {
		if (userIds == null || "".equals(userIds)) {
			throw new ServiceException("参数错误：参数userId不能为空或\"\"!");
		}
		List<TUumsBaseOrg> list = null;
		
		List<TUumsBaseUser> users =this.getUsersByUserIds(userIds);
		List<String> orgIds = new ArrayList<String>();
		for(Iterator<TUumsBaseUser> it = users.iterator();it.hasNext();){
			TUumsBaseUser user= it.next();
			orgIds.add(user.getOrgId());
		}
		
		if(orgIds != null && !orgIds.isEmpty()){
			list = baseDAO.findByQuery("OrgManager.getOrgInfosByOrgIds",
					new Object[] { orgIds });
		}
		return list;
	}

	  /**
     * 根据用户ID集合批量查询用户信息.
     * @param userIds  用户ID
     * @return
     * @throws DAOException
     * @throws SystemException
     * @throws ServiceException
     */
	public List<TUumsBaseUser> getUsersByUserIds(List<String> userIdLst)
			throws DAOException, SystemException, ServiceException {
        List<TUumsBaseUser> userLst = baseDAO.findByQuery("UserManager.getUsersByUserIds",
                new Object[] { userIdLst });
         return userLst;
	}
}