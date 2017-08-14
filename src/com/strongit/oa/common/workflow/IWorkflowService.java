/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.common.workflow;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.common.workflow.parameter.Parameter;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.util.OALogInfo;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.bo.TwfBaseTransitionPlugin;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 工作流接口
 * 
 * @author dengwenqiang
 * @version 1.0
 * @company      Strongit Ltd. (C) copyright
 * @date         May 15, 2012 4:48:03 PM
 * @classpath    com.strongit.oa.common.workflow.IWorkflowService
 */
public interface IWorkflowService {

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
			String workflowType, String isDeploy);
	//根据TwfBaseProcessfile pfid 查找TwfBaseProcessfile 没有挂在表单的
	public List<TwfBaseProcessfile> getTwfBaseProcessfileByIds(Page page,String[] ids);
	/**
	 * 根据流程名称获取流程主表单id
	 * 
	 * @description
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime Jun 4, 2012 4:03:46 PM
	 */
	public Long getMainFormIdByWorkflowName(String workflowName);

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
			throws WorkflowException;

	/**
	 * 得到子流程实例列表
	 * 
	 * @param parentInstanceId
	 * @return
	 */
	public List getChildInstanceId(Long parentInstanceId);

	/**
	 * 根据流程实例id得到所有子流程实例id列表
	 * 
	 * @param instanceId
	 *            当前流程实例id
	 */
	public List getMonitorChildrenInstanceIds(Long instanceId)
			throws WorkflowException;

	public List<Object[]> getWorkTodoRanksForList();

	/**
	 * @author 邓志城
	 * @date 2010年11月14日13:40:22 流程效率分析统计 只会统计已经办毕的流程,即已经结束的流程
	 * @param processTypeId
	 *            流程类型id 传入NULL则查询所有流程类型流程,否则查询指定流程类型的流程
	 * @return List<Object[流程类型名称,List<Object[流程名称,流程数量,List<Object[节点名称,节点平均耗时]>]>]>
	 */
	public List<Object[]> getProcesNodesByTypeForList(String processTypeId);

	/**
	 * 获取当前用户有权限启动的流程信息
	 * 
	 * @author:邓志城
	 * @date:2010-11-7 下午05:54:50
	 * @return List<Object[]{流程名称,流程类型id,流程类型名称,流程启动保单id}>
	 * @throws WorkflowException
	 */
	public List getStartWorkflow(String workflowType) throws WorkflowException;
	
	/**
	* @description 获取当前用户有权限启动的流程信息
	* @method getStartProcess
	* @author 申仪玲(shenyl)
	* @created 2012-7-16 上午11:13:35
	* @return List<Object[]{流程名称,流程类型id,流程类型名称,流程启动保单id,流程别名}>
	* @throws WorkflowException
	* @return List
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	public List getStartProcess() throws WorkflowException;

	/**
	 * 获取所有的流程信息
	 * 
	 * @author:申仪玲
	 * @date:2011-11-7 上午09:54:50
	 * @return List<Object[]{流程名称,流程类型id,流程类型名称,流程启动保单id}>
	 * @throws WorkflowException
	 */
	public List getDraftWorkflow(String workflowType) throws WorkflowException;

	/**
	 * 获取表单关联的工作流
	 * 
	 * @param formId
	 *            表单ID
	 * @return List<Object[]> 表单列表 List<[流程名称, 流程定义ID]>
	 */
	public List<Object[]> getFormRelativeWorkflow(String formId)
			throws WorkflowException;

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
			throws WorkflowException;

	/**
	 * 通过任务节点ID得到任务节点默认设置人员信息
	 * 
	 * @param nodeId
	 *            任务节点ID(为0表示是任务指派人员，否则是任务委派人员)
	 * @param taskId
	 *            任务ID
	 * @return List [人员ID，人员名称，组织机构ID]
	 * 
	 * @modify 增加参数 transitionId 用于支持在迁移线上选择人员
	 */
	public List getTaskActorsByTask(String nodeId, String taskId,
			String transitionId) throws WorkflowException;

	/**
	 * 启动流程，添加业务并新建保存流程实例
	 * 
	 * @param formId
	 *            表单ID
	 * @param workflowName
	 *            工作流名称
	 * @param userName
	 *            用户名称
	 * @param bussinessId
	 *            业务ID
	 * @param bussinessName
	 *            业务名称
	 * @param taskActors
	 *            流程下一步处理人信息([人员ID|节点ID，人员ID|节点ID……])
	 * @param tansitionName
	 *            拟稿任务完成后的路径名称,取值可能为模型路径名称、全部并发、选择并发
	 * @param concurrentTans
	 *            当transitionName为“选择并发”时选择的路径
	 * @param suggestion
	 *            流程启动时输入的提交意见
	 * @return void
	 */
	public String startWorkflow(String formId, String workflowName,
			String userName, String bussinessId, String bussinessName,
			String[] taskActors, String tansitionName, String concurrentTans,
			String suggestion) throws WorkflowException;
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
	    throws com.strongit.workflow.exception.WorkflowException;
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
	    throws com.strongit.workflow.exception.WorkflowException ;
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
	 * @return Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起,
	 *         (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型}
	 */
	public Page getTasksTodo(Page page, String userId, String searchType,
			String workflowType, String businessName, String userName,
			Date startDate, Date endDate, String isBackSpace)
			throws WorkflowException;

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
	 *            结束时间 内嵌数据对象结构：<br>
	 *            [任务ID,任务创建时间,任务名称,<br>
	 *            流程实例ID,业务创建时间,挂起状态,业务名称,<br>
	 *            发起人,委派人,委派类型（0：委派，1：指派）]
	 * 
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getTasksProcessed方法中添加 State 状态 查询参数 为主办来文和已拟公文 添加状态栏
	 */
	public Page getTasksProcessed(Page page, String username,
			String workflowType, String businessName, String userName,
			Date startDate, Date endDate, String state)
			throws WorkflowException;

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
	 *            结束时间
	 * @param state
	 *            流程当前状态 内嵌数据对象结构：com.strongit.workflow.bo.TwfInfoBuspdi
	 * 
	 * @Date:"2010-10-13 上午09:46:23
	 * @author:郑 志斌 getTasksHostedBy方法中添加 State 状态 查询参数 为已审公文和已办来文 添加状态栏
	 */
	public Page getTasksHostedBy(Page page, String username,
			String workflowType, String businessName, Date startDate,
			Date endDate, String state) throws WorkflowException;

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
	 * @return[
	 *            <p>
	 *            Object[]{启动时间,发起人ID，发起人姓名，流程名称，业务名称，任务节点ID，任务节点名，任务开始时间，任务结束时间，结束时间，任务ID，
	 *            </p>
	 *            <p>
	 *            任务节点对应表单Id，流程实例Id，流程对应主表单ID，任务节点对应表单业务数据标识，流程对应主表单业务数据标识,流程类型名称}
	 *            </p>
	 * @throws WorkflowException
	 */
	public Page getSearchWorkflowByPage(Page page, Map paramsMap)
			throws WorkflowException;

	/**
	 * author:dengzc description:取回工作任务 modifyer: description:
	 * 
	 * @param taskId
	 * @return
	 * @throws WorkflowException
	 */
	public String fetchTask(String taskId) throws WorkflowException;

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
	public List getNextTransitions(String taskId) throws WorkflowException;

	/**
	 * 根据迁移线id得到迁移线流转到的节点id
	 * 
	 * @param transitionId
	 *            迁移线id
	 * @return 流向的节点id
	 */
	public Long getNodeIdByTransitionId(String transitionId);

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
			throws WorkflowException;

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
	public String goToNextTransition(String taskId, String transitionName,
			String returnNodeId, String isNewForm, String formId,
			String businessId, String suggestion, String userId,
			String[] taskActors,Parameter... parameters) throws WorkflowException;

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
			String isAssignNeedReturn, String userId) throws WorkflowException;

	/**
	 * 获取工作流节点ID
	 * 
	 * @param taskId
	 *            任务ID
	 * @return String <br>
	 *         字符串格式:(调用时只会出现第三种情况) <br>
	 *         (1)"subprocess,表单路径,业务id,表单id"<br>
	 *         (2)"subaction,action设置,表单路径,业务id,表单id"<br>
	 *         (3)"form,表单路径,业务id,表单id"<br>
	 *         (4)"action,action设置,表单路径,业务id,表单id"
	 */
	public String getNodeInfo(String taskId) throws WorkflowException;

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
			throws WorkflowException;

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
			throws WorkflowException;

	/**
	 * 获取所有流程类型
	 * 
	 * @return List<Object[]> 流程类型信息集<br>
	 *         <p>
	 *         数据结构为：
	 *         </p>
	 *         <p>
	 *         Object[]{类型Id, 类型名称,是否系统类型}
	 *         </p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getAllProcessTypeList() throws WorkflowException;

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
	 */
	public List<Object[]> getRelativeFormByProcessType(String processTypeId)
			throws WorkflowException;

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
			throws WorkflowException;

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
	public Page<Object[]> getProcessAnalyzeByProcessForPage(
			Page<Object[]> page, String processTypeId) throws WorkflowException;

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
			throws WorkflowException;

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
			throws WorkflowException;

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
	public List<Object[]> getProcessOwnedByProcessType(String processTypeId)
			throws WorkflowException;

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
	public String judgeTaskIsDone(String taskid) throws WorkflowException;

	/**
	 * author:dengzc description:恢复被挂起的与给定任务同一批的其他会签任务 modifyer: description:
	 * 
	 * @param taskId
	 * @return
	 * @throws WorkflowException
	 */
	public void resumeConSignTask(String taskId) throws WorkflowException;

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
	public String checkCanReturn(String id) throws WorkflowException;

	/**
	 * 开始流程时获取文档操作权限
	 * 
	 * @param workflowName
	 * @return
	 *            <p>
	 *            数据结构：
	 *            </p>
	 *            <p>
	 *            (0)导出PDF,(1)导入模板,(2)打印,(3)保存,(4)保存并关闭,(5)页面设置,(6)保留痕迹,(7)不保留痕迹,(8)显示痕迹,
	 *            </p>
	 *            <p>
	 *            (9)隐藏痕迹,(10)文件套红,(11)插入图片,(12)只读,(13)加盖电子印章,(14)加盖电子印章(从服务器),(15)生成二维条码,
	 *            </p>
	 *            <p>
	 *            (16)插入手写签名,(17)全屏手工绘画,(18)全屏手写签名
	 *            </p>
	 *            <p>
	 *            数据说明：0、无权限；1、有权限
	 *            </p>
	 * @throws WorkflowException
	 */
	public String getStartWorkflowDocumentPrivilege(String workflowName);

	/**
	 * 获取文档操作权限
	 * 
	 * @param taskId
	 * @return
	 *            <p>
	 *            数据结构：
	 *            </p>
	 *            <p>
	 *            (0)导出正文,(1)导入模板,(2)打印,(3)保存,(4)保存并关闭,(5)页面设置,(6)保留痕迹,(7)不保留痕迹,(8)显示痕迹,
	 *            </p>
	 *            <p>
	 *            (9)隐藏痕迹,(10)文件套红,(11)插入图片,(12)只读,(13)加盖电子印章,(14)加盖电子印章(从服务器),(15)生成二维条码,
	 *            </p>
	 *            <p>
	 *            (16)插入手写签名,(17)全屏手工绘画,(18)全屏手写签名
	 *            </p>
	 *            <p>
	 *            数据说明：0、无权限；1、有权限
	 *            </p>
	 * @throws WorkflowException
	 */
	public String getDocumentPrivilege(TwfBaseNodesetting nodeSetting)
			throws WorkflowException;

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
			String workflowName) throws SystemException;
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
			SystemException;
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
	    throws com.strongit.workflow.exception.WorkflowException;
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
			String processInstanceId) throws WorkflowException;

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
	 * @param userId
	 *            -当前处理人Id
	 * @throws WorkflowException
	 */
	public void returnReAssignTask(String taskId, String newForm, long formId,
			String businessId, String userId) throws WorkflowException;

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
	public List<Object[]> getProcessHandleByTaskInstanceId(String taskInstanceId)
			throws WorkflowException;

	/**
	 * 根据查询条件查找相应流程信息（OA专用）
	 * 
	 * @author 喻斌
	 * @date May 15, 2009 11:48:29 AM
	 * @param processType
	 *            -流程类型查询条件<br>
	 *            <p>
	 *            为null所有流程
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
	 *         Object[]{(0)流程实例Id,(1)业务名称,(2)开始时间,(3)结束时间,(4)流程主表单Id,(5)流程主表单业务数据标识}
	 *         </p>
	 * @throws WorkflowExceptin
	 */
	public List<Object[]> getProcessInfoByQueryCondition(String processType,
			String processStatus, String searchScope, Date startDate,
			Date endDate, String userId, String businessName)
			throws WorkflowException;

	/**
	 * 根据查询条件查找相应流程信息,返回分页对象（OA专用）
	 * 
	 * @author 喻斌
	 * @date May 15, 2009 11:48:29 AM
	 * @param page
	 *            -分页对象
	 * @param processName
	 *            -流程名称查询条件<br>
	 *            <p>
	 *            null：所有流程
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
	 *         Object[]{(0)流程实例Id,(1)业务名称,(2)开始时间,(3)结束时间,(4)流程主表单Id,(5)流程主表单业务数据标识}
	 *         </p>
	 * @throws WorkflowExceptin
	 */
	public Page getProcessInfoForPageByQueryCondition(Page page,
			String processType, String processStatus, String searchScope,
			Date startDate, Date endDate, String userId, String businessName)
			throws WorkflowException;

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
			throws WorkflowException;

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
			throws WorkflowException;

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
			throws WorkflowException;

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
	 */
	public boolean addTemplateFileInterface(Object... objects)
			throws SystemException;

	/**
	 * 查询所有流程文件包括流程类型信息（OA专用）
	 * 
	 * @author 喻斌
	 * @date Jun 19, 2009 8:53:23 AM
	 * @return List<Object[]> 流程定义信息集<br>
	 *         <p>
	 *         返回数据类型：Object[]{(0)流程定义文件Id,(1)流程定义文件名称,(2)流程定义文件主表单Id,(3)流程类型Id,(4)流程类型名称}
	 *         </p>
	 * @throws WorkflowException
	 */
	public List getAllProcessFilesList() throws WorkflowException;

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
			throws com.strongit.workflow.exception.WorkflowException;

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
	 *         (5)节点对应的业务特殊字段, (6)节点名称}
	 *         </p>
	 * @throws WorkflowException
	 */
	@Transactional(readOnly = true)
	public List<Object[]> getProcessHandlesAndBusiFlagByTaskId(
			String taskInstanceId) throws WorkflowException;

	/**
	 * 根据任务实例Id找到对应的流程实例Id
	 * 
	 * @param taskInstanceId
	 * @return
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.lang.String getProcessInstanceIdByTiId(
			java.lang.String taskInstanceId)
			throws com.strongit.workflow.exception.WorkflowException;

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
	@Transactional(readOnly = true)
	public Object[] getBusiFlagByTaskId(String taskInstanceId)
			throws WorkflowException;
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
	@Transactional(readOnly = true)
	public Object[] getFirstNodeBusiFlagByWorkflowName(String workflowName)
			throws WorkflowException;
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
	public Page<Object[]> getProcessInstanceByConditionForPage(
			Page<Object[]> page, List<String> toSelectItems,
			Map<String, Object> paramsMap, Map<String, String> orderMap)
			throws WorkflowException;

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
			throws WorkflowException;

	/**
	 * 
	 * 注 ：该方法是基于oracle的rownum进行分页处理,在toSelectItems不能存在多个相同查询参数时，否则无法正常处理分页
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
			throws WorkflowException;

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
			Map<String, String> orderMap) throws WorkflowException;

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
			throws WorkflowException;

	public Page getTaskInfosByConditionForPage(
			com.strongmvc.orm.hibernate.Page page,
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			java.util.Map<java.lang.String, java.lang.Object> customValues)
			throws WorkflowException;

	public Page getTaskInfosByConditionForPage(
			com.strongmvc.orm.hibernate.Page page,
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			List customValues, String customOrderBy) throws WorkflowException;

	public List getTaskInfosByConditionForList(
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			java.util.Map<java.lang.String, java.lang.Object> customValues)
			throws com.strongit.workflow.exception.WorkflowException;

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
			throws WorkflowException;

	public List<Object[]> parseToSelectActors(String[] actor, String taskId);

	/**
	 * 根据流程实例id获取任务办理意见.
	 * 
	 * @author:邓志城
	 * @date:2010-3-1 下午07:31:29
	 * @param processInstanceId
	 *            流程实例id
	 * @return 办理意见集合.
	 */
	public java.util.List<com.strongit.workflow.bo.TwfInfoApproveinfo> getApproveInfosByPIId(
			java.lang.String processInstanceId);
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
			throws com.strongit.workflow.exception.WorkflowException;
	/**
	 * 根据节点ID获取节点设置信息.
	 * 
	 * @author:彭小青
	 * @date:2010-3-5 下午11:31:29
	 * @param
	 * @return
	 */
	public TwfBaseNodesetting getNodesettingByNodeId(String nodeId);

	/**
	 * 根据任务ID获取节点设置信息.
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 21, 2012 9:53:38 AM
	 */
	public TwfBaseNodesetting getNodesettingByTid(String taskId);

	/**
	 * 根据一组节点ID获取一组节点设置信息.
	 * 
	 * @author 严建
	 * @param nodeIdList
	 * @return Map【节点id-节点信息】
	 * @throws Exception
	 * @createTime Jan 6, 2012 11:44:44 AM
	 */
	public Map<String, TwfBaseNodesetting> getNodesettingMapByNodeIdList(
			List<String> nodeIdList);

	/**
	 * 根据流程实例id得到办理意见和意见挂接的特殊业务字段
	 * 
	 * @author:邓志城
	 * @date:2010-3-8 下午04:19:14
	 * @param processInstanceId
	 * @return List -每个节点的处理意见和对应的节点设置信息 Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间,
	 *         (3)任务结束时间, (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象,
	 *         (9)审批数字签名, (10)任务委派类型【0：委托；1：指派；2：代办；""：普通】, (11)最后一个委托/指派人Id,
	 *         (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称}
	 */
	public List<Object[]> getBusiFlagByProcessInstanceId(
			String processInstanceId);

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
			throws com.strongit.workflow.exception.WorkflowException;

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
			java.lang.String transitionId, java.lang.String pluginName);

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
			throws com.strongit.workflow.exception.WorkflowException;

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
			throws com.strongit.workflow.exception.WorkflowException;

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
			throws com.strongit.workflow.exception.WorkflowException;

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
	public abstract String checkNodeTypeByNodeId(String nodeId)
			throws WorkflowException;

	/**
	 * 根据节点Id得到该节点下一步的所有节点信息
	 * 
	 * @author 喻斌
	 * @date 2011-7-26 上午06:24:28
	 * @param nodeId
	 *            -节点Id
	 * @return List<Object[]> 下一步的所有节点信息<br>
	 *         数据格式为：{(0)节点Id，(1)节点名称，(2)节点类型(节点类型值参考{@link #checkNodeTypeByNode(Node)})}
	 * @throws WorkflowException
	 */
	public abstract List<Object[]> getNextNodesByNodeId(String nodeId)
			throws WorkflowException;

	/**
	 * 根据任务实例Id得到该节点下一步的所有节点信息
	 * 
	 * @author 喻斌
	 * @date 2011-7-26 上午06:24:28
	 * @param nodeId
	 *            -节点Id
	 * @return List<Object[]> 下一步的所有节点信息<br>
	 *         数据格式为：{(0)节点Id，(1)节点名称，(2)节点类型(节点类型值参考{@link #checkNodeTypeByNode(Node)})}
	 * @throws WorkflowException
	 */
	public abstract List<Object[]> getNextNodesByTaskInstanceId(
			String taskInstanceId) throws WorkflowException;

	/**
	 * 根据流程实例Id得到流程运行情况
	 * 
	 * @author 喻斌
	 * @date 2011-8-5 上午09:28:27
	 * @param processInstanceId
	 *            -流程实例Id
	 * @return Object[] 流程实例运行情况<br>
	 *         数据格式为：Object[]{(0)流程实例Id，(1)流程名称，(2)流程业务Id，(3)流程业务名称，(4)流程发起人Id，(5)流程发起人名称，(6)流程运行情况}<br>
	 *         流程运行情况格式为：Collection<Object[]{(0)任务或子流程标志，(1)节点名称，(2)进入节点时间，(3)任务处理人Id，多个以,分隔}><br>
	 *         其中任务或子流程标志为：“task”：表示未完成的任务；“subProcess”：表示正在执行的子流程；
	 * @throws WorkflowException
	 */
	public Object[] getProcessStatusByPiId(String processInstanceId)
			throws WorkflowException;

	/**
	 * 根据流程实例id得到父流程实例id
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 父流程实例 List<Object[]>
	 *         流程父流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务名称，(2)流程名称}
	 */
	public List getParentInstanceId(Long instanceId);

	/**
	 * 获取JBPM上下文公共方法
	 * 
	 * @return
	 */
	public org.jbpm.JbpmContext getJbpmContext();

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
			throws com.strongit.workflow.exception.WorkflowException;

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
			throws com.strongit.workflow.exception.WorkflowException;

	/**
	 * 根据流程实例Id得到指定节点上任务的处理记录
	 * 
	 * @param processinstanceId
	 *            流程实例Id
	 * @param nodeId
	 *            节点Id
	 * @return List 任务处理记录 数据结构为：
	 *         Object[]{(0)任务开始时间,(1)任务结束时间,(2)任务处理人名称,(3)任务处理意见,(4)任务处理时间,(5)任务是否超时,(6)任务处理记录Id,(7)审批数字签名}
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List getHandleRecordByNode(
			java.lang.String processinstanceId, java.lang.String nodeId)
			throws com.strongit.workflow.exception.WorkflowException;

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
			throws com.strongit.workflow.exception.WorkflowException;

	/**
	 * 得到流程处理意见,含当前流程的父流程、子流程、兄弟流程办理意见
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 其他排序：Map<控件名称,List<String[意见内容,用户id,处理时间,处理人名称,处理时间]>> 部门排序：Map<控件名称,List<String[意见内容,用户id,处理时间,机构id,机构排序号,机构名称,处理人名称,被代办人处理时间,任务处理类型]>>
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String[]>> getWorkflowApproveinfo(String instanceId,String taskId)
			throws SystemException;
	
	/**
	 * 得到流程处理意见,本流程办理意见
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 其他排序：Map<控件名称,List<String[意见内容,用户id,处理时间,处理人名称,处理时间]>> 部门排序：Map<控件名称,List<String[意见内容,用户id,处理时间,机构id,机构排序号,机构名称,处理人名称,被代办人处理时间,任务处理类型]>>
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String[]>> getThisWorkflowApproveinfo(String instanceId)
			throws SystemException;
	
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
			throws com.strongit.workflow.exception.WorkflowException;
	/**
	 * 动态设置定时器最近一次需要进行处理的时间，这样当定时器结束睡眠运行时，若已到达该时间则执行业务功能，否则继续睡眠 
	 * 
	 * @param idleInterval	最近一次需要进行处理的时间毫秒数 
	 * @throws com.strongit.workflow.exception.WorkflowException
	 * @createTime May 15, 2012 4:46:26 PM
	 */
	public void setJobExecutorTime(long idleInterval)
			throws com.strongit.workflow.exception.WorkflowException ;

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
			String toUserId) throws WorkflowException;

	/**
	 * 根据流程实例Id得到指定节点上任务的当前处理情况（流程监控使用）
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 9, 2011 10:27:33 PM
	 * @return java.util.List
	 *         Object[]{(0)任务开始时间,(1)任务处理人信息,(2)任务是否已经签收,(3)任务是否超时,(4)任务处理人Id信息,(5)任务实例Id}
	 */
	public java.util.List getCurrentHandleByNode(
			java.lang.String processinstanceId, java.lang.String nodeId)
			throws com.strongit.workflow.exception.WorkflowException;

	/**
	 * @description
	 * @author 严建
	 * @createTime Dec 10, 2011 4:08:34 PM
	 * @return java.util.List<java.lang.Object[]>
	 */
	public java.util.List<java.lang.Object[]> getProcessInstanceByConditionForList(
			java.util.List<java.lang.String> toSelectItems,
			java.util.Map<java.lang.String, java.lang.Object> paramsMap,
			java.util.Map<java.lang.String, java.lang.String> orderMap,
			java.lang.String customSelectItems,
			java.lang.String customFromItems, java.lang.String customQuery,
			java.util.Map<java.lang.String, java.lang.Object> customValues)
			throws com.strongit.workflow.exception.WorkflowException;

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
			throws com.strongit.workflow.exception.WorkflowException;

	public void saveApproveInfo(TwfInfoApproveinfo approveInfo);

	public TwfInfoApproveinfo getApproveInfoById(String id);

	public String handleProcess(String taskId, String isNewForm, String formId,
			String businessId, String suggestion, String userId);

	public List getDataByHql(String hql, Object[] values);

	/**
	 * 根据流程实例id得到流程实例对象
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 流程实例对象
	 */
	public ProcessInstance getProcessInstanceById(String instanceId);

	/**
	 * 根据任务实例id得到任务实例对象
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 28, 2011 1:37:44 PM
	 * @return TaskInstance
	 */
	public TaskInstance getTaskInstanceById(String instanceId);

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
			throws com.strongit.workflow.exception.WorkflowException;

	/**
	 * 根据流程实例得到流程实例上下文
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 流程实例上下文
	 */
	public ContextInstance getContextInstance(String instanceId);

	// 挂起流程实例
	public boolean repealProcess(String instanceId, String flag,OALogInfo... infos);

	// 恢复流程实例
	public boolean returnProcess(String instanceId, String flag,OALogInfo... infos);

	// 删除流程回收站的流程
	public boolean delProcess(String instanceId);

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
			String taskId);

	/**
	 * 得到指定任务的办理意见
	 * 
	 * @param taskId
	 *            任务实例id
	 * @return 办理意见
	 */
	public TwfInfoApproveinfo getApproveinfoByTaskId(String taskId);

	/**
	 * 根据流程实例得到流程当前处理人信息（含子流程）
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return TaskBean 任务信息bean
	 */
	@SuppressWarnings("unchecked")
	public TaskBean getCurrentTaskHandle(String instanceId);

	/**
	 * @method isBackTask
	 * @author 申仪玲
	 * @created 2011-12-28 上午11:49:19
	 * @description 根据任务ID判断该任务是否是退回任务
	 * @return String "0":不是退回任务；"1":是退回任务
	 */
	public String isBackTask(String taskId);

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
			throws SystemException;

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
	public List<TaskBean> getSuperTaskNodeList(String taskId)
			throws SystemException;

	/**
	 * @description 根据任务对应的节点id判断对应节点是否为签收节点
	 * @author 严建
	 * @param taskNodeId
	 *            任务对应的节点id
	 * @return result 【true:是签收节点任务|false:不是签收节点任务】
	 * @createTime Jan 5, 2012 3:24:29 PM
	 */
	public boolean isSignNodeTask(String taskNodeId);

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
	public boolean isSignNodeTask(String taskNodeId,
			TwfBaseNodesetting nodeSetting);

	/**
	 * 根据流程实例id得到流程实例对象
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return 流程实例对象
	 */
	public ProcessInstance getProcessInstanceId(String instanceId);

	/**
	 * 得到上一任务处理人信息
	 * 
	 * @param taskInstanceId
	 *            任务实例id
	 * @return 上一任务信息
	 */
	public TaskBean getPrevTaskHandler(String taskInstanceId);

	/**
	 * 根据流程实例id获取流程主办人员id
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 9, 2012 10:26:23 AM
	 */
	public String getMainActorIdByProcessInstanceId(String instanceId);

	/**
	 * 获取主办人员信息
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:38:17 PM
	 */
	public UserBeanTemp getMainActorInfoByProcessInstanceId(
			String processInstanceId);

	/**
	 * 根据任务实例id获取主办人员信息
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:24:11 PM
	 */
	public UserBeanTemp getMainActorInfoByTaskInstanceId(String taskInstanceId);

	/**
	 * 根据任务实例id获取流程主办人员id
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 9, 2012 10:26:23 AM
	 */
	public String getMainActorIdByTaskInstanceId(String taskInstanceId);

	/**
	 * 根据任务实例id获取主办人员指派人员id
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 24, 2012 5:09:03 PM
	 */
	public String getMainReassignActorIdByTaskInstanceId(String taskInstanceId);

	/**
	 * 根据任务实例id获取主办人员指派人员信息
	 * 
	 * @description
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 24, 2012 5:06:09 PM
	 */
	public UserBeanTemp getMainReassignActorInfoByTaskInstanceId(
			String taskInstanceId);

	/**
	 * 根据流程名称获取流程第一个节点的id
	 * 
	 * @author 严建
	 * @param workflowName
	 * @return
	 * @createTime Mar 12, 2012 11:11:41 AM
	 */
	@SuppressWarnings("unchecked")
	public Long getFirstNodeId(String workflowName);

	/**
	 * 判断任务是否为退回任务
	 * 
	 * @author 严建
	 * @param taskInstance
	 * @return
	 * @createTime Mar 21, 2012 12:27:21 PM
	 */
	public boolean isBackTask(TaskInstance taskInstance);

	/**
	 * 根据任务id判断任务是否为退回任务
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 21, 2012 12:28:44 PM
	 */
	public boolean isBackTaskByTid(String taskId);

	/**
	 * 是否拥有主办权限
	 * 
	 * @author 严建
	 * @param userId
	 * @param taskId
	 * @return
	 * @createTime Mar 24, 2012 5:37:40 PM
	 */
	public boolean hasMainDoing(String userId, String taskId);

	/**
	 * 挂起意见征询信息
	 * 
	 * @author 严建
	 * @param businessId
	 * @createTime Apr 7, 2012 10:01:08 PM
	 */
	
	public void suspendYjzx(String bussinessId);
	
	/**
	 * 恢复意见征询信息
	 * 
	 * @author 严建
	 * @param businessId
	 * @createTime Apr 7, 2012 10:05:12 PM
	 */
	public void resumeYjzx(String businessId);
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
                throws com.strongit.workflow.exception.WorkflowException;
	/**
	 * 根据流程实例Id获取流程中每个节点的处理意见和对应的节点设置信息
	 * @author 喻斌
	 * @date Dec 19, 2009 2:20:19 PM
	 * @param processInstanceIds -流程实例Id集合
	 * @return List<Object[]> -每个节点的处理意见和对应的节点设置信息<br>
	 * 		<p>Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)处理意见内容, (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象, (9)审批数字签名, (10)任务委派类型【0：委托；1：指派；“”：非委托】, (11)最后一个委托/指派人Id, (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称, (15)主键值, (16)流程实例Id}</p>
	 * @throws WorkflowException
	 */
	public List<Object[]> getProcessHandlesAndNodeSettingByPiIds(List<Long> processInstanceIds) throws WorkflowException;
	/**
	 * 获取流程的所有子流程实例信息
	 * @author 喻斌
	 * @date Apr 15, 2009 11:11:32 AM
	 * @param processInstanceId -流程实例Id
	 * @return List<Object[]> 流程子流程实例信息,信息格式为{(0)流程实例Id，(1)流程业务名称，(2)流程名称}
	 * @throws WorkflowException
	 */
	public List<Object[]> getSubProcessInstanceInfos(String processInstanceId)throws WorkflowException ;
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
	public List<Object[]> getSupProcessInstanceInfos(String processInstanceId)throws WorkflowException;
	/**
	 * 根据tableName，pkName，pkValue来得到流程表中的taskId
	 * @date2014年3月3日19:46:16
	 * @param tableName
	 * @param pkName
	 * @param pkValue
	 * @return
	 */
	public List<Long>   getTaskId(String tableName,String pkName,String pkValue)throws WorkflowException;
	
	public List<Long>   getIds(String tableName,String pkName,String pkValue)throws WorkflowException;
	public String  getDraf(String tableName,String pkName,String pkValue)throws WorkflowException;
}
