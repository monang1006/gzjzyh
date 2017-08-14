package com.strongit.workflow.workflowDesign.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.common.business.jbpmbusiness.JBPMBusinessManager;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseSubprocess;
import com.strongit.workflow.bo.TwfInfoBuspdi;
import com.strongit.workflow.bo.TwfInfoProbusrelation;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.po.UserInfo;
import com.strongit.workflow.util.WorkflowConst;
import com.strongit.workflow.uupInterface.UupUtil;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.service.ServiceLocator;

public class SubProcessStart implements ActionHandler {

	private IProcessDefinitionService processService= null;
	
	private IProcessDefinitionService getProcessService(){
		if(processService == null){
			processService = (IProcessDefinitionService)ServiceLocator.getService("processDefinitionService");
		}
		return processService;
	}

	private UupUtil uupInterface = null;

	private UupUtil getUupInterface() {
		if (uupInterface == null) {
			uupInterface = (UupUtil) ServiceLocator
					.getService("uupUtil");
		}
		return uupInterface;
	}

	private void addSubProcessInfo(Token token, ProcessInstance superprocess,
			ProcessInstance subprocess, String subControl, String formInfo,
			String nodeName, String businessName, String startorId,
			String startorName) throws WorkflowException {
		String hql = new String("from TwfInfoBuspdi t where t.aiPiId=?");
		List list = this.getProcessService().getDataByHql(hql,
				new Object[] { new Long(superprocess.getId()) });
		boolean isContainSubForm = false;//判断父流程的业务数据中是否包含子流程主表单数据
		if (!list.isEmpty()) {
			Object customBusinessId = superprocess.getContextInstance().getVariable(
					WorkflowConst.WORKFLOW_SUB_CUSTOM_BUSIID);//用户自定义业务标识
			
			List relations = new ArrayList();
			System.out.println(superprocess.getMainFormId());
			System.out.println(subprocess.getMainFormId());
			TwfInfoBuspdi superinfo = (TwfInfoBuspdi) list.get(0);
			superinfo.setBiState(nodeName);
			relations.add(superinfo);

			TwfBaseSubprocess tbs = new TwfBaseSubprocess();
			tbs.setSubProcessId(new Long(subprocess.getId()));
			tbs.setTwfInfoBuspdi(superinfo);
			relations.add(tbs);

			TwfInfoBuspdi subinfo = new TwfInfoBuspdi();
			subinfo.setAiPiId(new Long(subprocess.getId()));
			subinfo.setAiBiName(businessName);
			subinfo.setBiCreatedate(new Date());
			subinfo.setBiStartUser(startorId == null?"":startorId);
			subinfo.setBiStartUserName(startorName == null?"":startorName);
			subinfo.setBiMainform(formInfo.split(",")[1]);
			subinfo.setBiIsDraft("0");
			subinfo.setBiIsSub("0");
			subinfo.setBiState(((Transition) subprocess.getProcessDefinition()
					.getStartState().getLeavingTransitions().get(0)).getName());
			//设置父子流程结构编码
			if(superinfo.getBiProcessCode() == null || "".equals(superinfo.getBiProcessCode())){//父流程是最顶级流程
				subinfo.setBiProcessCode(","+superinfo.getBiId()+",");
			}else{//父流程也是子流程
				subinfo.setBiProcessCode(superinfo.getBiProcessCode()+superinfo.getBiId()+",");
			}
			relations.add(subinfo);

			TwfInfoProbusrelation tip;
			TwfInfoProbusrelation tip2;

			hql = new String(
					"from TwfInfoProbusrelation t where t.twfInfoBuspdi.aiPiId=?");
			List tempList = this.processService.getDataByHql(
					hql, new Object[] { superinfo.getAiPiId() });
			if ("1".equals(subControl.split("\\|")[0])) {//父流程业务数据需要拷贝到子流程中
				for (int i = 0; i < tempList.size(); i++) {
					tip = (TwfInfoProbusrelation) tempList.get(i);// it.next();
					//if (tip.getWrFormId().equals(formInfo.split(",")[1])) {
					if (formInfo.split(",")[1].equals(String.valueOf(tip.getWrFormId()))) {
						isContainSubForm = true;//父流程的业务数据中包含子流程主表单数据
						/** 添加冗余数据 */
						subprocess.setBusinessId(tip.getWrBusinessId());
					}
					tip2 = new TwfInfoProbusrelation();
					tip2.setWrFormId(tip.getWrFormId());
					tip2.setWrBusinessId(tip.getWrBusinessId());
					tip2.setTwfInfoBuspdi(subinfo);
					/** 添加冗余数据 */
					tip2.setWrPiId(subprocess.getId());//设置流程实例Id
					relations.add(tip2);
				}
			} else {//父流程业务数据不需要拷贝到子流程中
				for (int i = 0; i < tempList.size(); i++) {
					tip = (TwfInfoProbusrelation) tempList.get(i);// it.next();
					if (formInfo.split(",")[1].equals(String.valueOf(tip.getWrFormId()))) {
						isContainSubForm = true;//父流程的业务数据中包含子流程主表单数据
						tip2 = new TwfInfoProbusrelation();
						tip2.setWrFormId(tip.getWrFormId());
						tip2.setWrBusinessId(tip.getWrBusinessId());
						tip2.setTwfInfoBuspdi(subinfo);
						/** 添加冗余数据 */
						subprocess.setBusinessId(tip.getWrBusinessId());
						tip2.setWrPiId(subprocess.getId());//设置流程实例Id
						relations.add(tip2);
					}
				}
			}
			if(!isContainSubForm && customBusinessId != null){//父流程的业务数据中没有包含子流程主表单数据并且用户自定义了子流程业务数据Id
				tip2 = new TwfInfoProbusrelation();
				tip2.setWrFormId(Long.valueOf(formInfo.split(",")[1]));
				tip2.setWrBusinessId(customBusinessId.toString());
				tip2.setTwfInfoBuspdi(subinfo);
				/** 添加冗余数据 */
				tip2.setWrPiId(subprocess.getId());//设置流程实例Id
				subprocess.setBusinessId(customBusinessId.toString());//设置业务标识到流程实例属性中
				relations.add(tip2);
			}
			if(customBusinessId != null){
			    //<--
			    /**保存将业务数据业务字段  yanjian 2012-02-06 16:07*/
			    JBPMBusinessManager JBPMBusinessManager = (JBPMBusinessManager)ServiceLocator.getService("JBPMBusinessManager");
				Tjbpmbusiness model = JBPMBusinessManager.findByBusinessId(customBusinessId.toString());
				if(model == null){
					model = new Tjbpmbusiness(customBusinessId.toString());
				}
				JBPMBusinessManager.saveModel(model);
			    //-->
			}
			for (int k = 0; k < relations.size(); k++) {
				this.getProcessService().saveObject(relations.get(k));
			}
			if(!isContainSubForm){//父流程的业务数据中不包含子流程主表单数据
				superprocess.getContextInstance().setVariable("jbpm_subinfoId",
						subinfo.getBiId().toString(), token);//保存子流程业务信息表Id给业务操作,为了兼容目前OA需要在父子流程之间导数据
			}
		}
	}

	public void execute(ExecutionContext arg0) throws WorkflowException {
		JbpmContext jbpmcontext = arg0.getJbpmContext();
		Token token = arg0.getToken();
		Node node = arg0.getNode();
		TwfBaseNodesetting tbn = this.getProcessService().getNodesettingByNodeId(String.valueOf((node
				.getId())));
		if (!"".equals(tbn.getNsSubprocessSetting())) {
			// StringTokenizer st = new
			// StringTokenizer(tbn.getNsSubprocessSetting(),"|");
			String processName = tbn.getNsSubprocessSetting().split("\\|")[0];// st.nextToken();
			String formInfo = tbn.getNsSubprocessSetting().split("\\|")[1];// st.nextToken();
			ProcessInstance supPi = arg0.getProcessInstance();
			String oriSupProcess = String.valueOf(supPi.getContextInstance()
					.getVariable(WorkflowConst.WORKFLOW_ORISUPPROCESS,
							supPi.getRootToken()));
			String businessName = null;//业务名称
			Object customBusinessName = token.getProcessInstance()
												.getContextInstance().getVariable(
														WorkflowConst.WORKFLOW_SUB_CUSTOM_BUSINAME);//用户自定义子流程业务名称
			if(customBusinessName != null && !"".equals(customBusinessName)){//用户指定了子流程业务名称
				businessName = customBusinessName.toString();
			}else{//用户未指定子流程业务名称
				Object parentBusinessName = token.getProcessInstance()
													.getContextInstance().getVariable(
															WorkflowConst.WORKFLOW_BUSINESSNAME);//父流程业务名称
				if(parentBusinessName == null){
					parentBusinessName = "";
				}
				String businessNameModel = tbn.getPlugin(WorkflowConst.WORKFLOW_PF_SUB_BUSINAME_MODEL);//子流程业务名称模式
				if(businessNameModel != null && !"".equals(businessNameModel)){//指定了子流程业务名称模式
					String replaceStr = WorkflowConst.WORKFLOW_PF_SUB_REPLACEBUSINAME;//父流程业务名称占位符
					businessName = businessNameModel.replaceAll(replaceStr,
							parentBusinessName.toString());//替换占位符为父流程业务名称
				}else{//未指定子流程业务名称模式则直接使子流程业务名称等于父流程业务名称
					businessName = parentBusinessName.toString();
				}
			}
			
			String startorId = null;//发起人Id
			String startorName = null;//发起人名称
			String businessNameModel = tbn.getPlugin(WorkflowConst.WORKFLOW_PF_SUB_USERSUPSTARTOR);//子流程发起人模式
			if(businessNameModel != null && "1".equals(businessNameModel)){//父流程发起人
				Object parentStartor = token.getProcessInstance()
												.getContextInstance().getVariable(
														WorkflowConst.WORKFLOW_PROCESSSTARTER);//父流程发起人
				if(parentStartor != null){
					startorId = parentStartor.toString();
					startorName = this.getUupInterface().getUsernameById(startorId);
				}
			}else if(businessNameModel != null && "2".equals(businessNameModel)){//前步处理人
				UserInfo currentUser = this.getUupInterface().getCurrentUserInfo();
				if(currentUser != null){
					startorId = currentUser.getUserId();
					startorName = currentUser.getUserName();
				}
			}
			
			ProcessInstance pi = new ProcessInstance(jbpmcontext
					.getGraphSession().findLatestProcessDefinition(processName));
			
			// 保存节点上的子流程实例ID，为了实现回退的时候结束节点上的子流程
			if ("1".equals(tbn.getNsSubprocessType())) {
				String nodeSub = String.valueOf(supPi.getContextInstance()
						.getVariable(
								WorkflowConst.WORKFLOW_NODE_SUB + "_"
										+ node.getId()));
				supPi.getContextInstance().setVariable(
						WorkflowConst.WORKFLOW_NODE_SUB + "_" + node.getId(),
						"null".equals(nodeSub) ? pi.getId() : nodeSub
								 + "," + pi.getId());
			}
			supPi.getContextInstance().setVariable("async_" + node.getId(), pi.getId());//存储当前子流程实例Id，为了兼容目前OA需要在父子流程之间导数据
			/**
			 * yanjian 2012-07-25 16:51 处理多级子流程嵌套时，子流程节点表单使用“启动流程表单”，实现子流程表单呈现继承父流程的表单呈现
			 * 
			 * */
			if("流程启动表单".equals(formInfo.split(",")[0])){//如果选择的是启动表单，将根据父流程实例的主表单id重新初始化formInfo，以保证子流程的业务数据正常呈现
				StringBuilder formInfoTemp = new StringBuilder();
				String[] formInfoArray = formInfo.split(",");
				formInfoTemp.append(formInfoArray[0]).append(",").append(token.getProcessInstance().getMainFormId());//获取父流程实例的主表单id
				formInfo = formInfoTemp.toString();
			}
			
			this.addSubProcessInfo(token, token.getProcessInstance(), pi, tbn
					.getNsSubControll(), formInfo, token.getNode().getName(), businessName,
					startorId, startorName);
			pi.getContextInstance().setVariableLocally(
					WorkflowConst.WORKFLOW_ORISUPPROCESS, oriSupProcess,
					pi.getRootToken());
			pi.getContextInstance().setVariable(
					WorkflowConst.WORKFLOW_BUSINESSNAME, businessName);
			pi.getContextInstance().setVariableLocally(
					WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID,
					new Long(token.getProcessInstance().getId()),
					pi.getRootToken());
			pi.getContextInstance().setVariableLocally(
					WorkflowConst.WORKFLOW_SUB_ISSUBPROCESS, "1",
					pi.getRootToken());
			pi.getContextInstance().setVariableLocally(
					WorkflowConst.WORKFLOW_SUB_MAINFORMID,
					formInfo.split(",")[1], pi.getRootToken());
			pi.getContextInstance().setVariableLocally(
					WorkflowConst.WORKFLOW_SUB_PARENTTOKEN,
					new Long(token.getId()), pi.getRootToken());

			pi.getContextInstance().setVariableLocally(
					WorkflowConst.WORKFLOW_SUB_TYPE, tbn.getNsSubprocessType(),
					pi.getRootToken());
			pi.getContextInstance().setVariableLocally(
					WorkflowConst.WORKFLOW_SUB_NEEDRETURN,
					tbn.getNsSubControll().split("\\|")[1], pi.getRootToken());
//			增加是否需要协办的标志：人保厅二级单位需求，科室单位需要控制到第四级 dengzc 2011年10月31日21:11:28 
			String isAllowContinueToDept = tbn.getPlugin(NodesettingPluginConst.PLUGINS_ISNOTALLOWCONTINUETODEPT);
			pi.getContextInstance().setVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_ISALLOWCONTINUETODEPT, isAllowContinueToDept);
			//存储节点id,用于获取下一步迁移线步骤时验证是选择了多个处室还是选择了多个科室
			pi.getContextInstance().setVariable(CustomWorkflowConst.WORKFLOW_SUPERPROCESS_NODEID, node.getId());
			//存储父流程表单数据 
			//严建 2012-11-05 11:15 不需要使用@{personDemo}变量来处理查看员表单的功能
			//------------------------------------------------------------------------------------
			// 2010-03-03：增加子流程启动时保存子流程节点名称和子流程结束时是否可以选择父流程下一步处理人信息
			pi.getContextInstance().setVariable(WorkflowConst.WORKFLOW_SUB_SUPNODE, node.getName());
			if(tbn.getPlugins() != null){
				String setSupActor = tbn.getPlugin(WorkflowConst.WORKFLOW_PF_SUB_SETSUPACTOR);
				if(setSupActor != null && "1".equals(setSupActor.toString())){
					pi.getContextInstance().setVariable(WorkflowConst.WORKFLOW_SUB_SETSUPACTOR, "1");
				}
			}
			
			/** 添加冗余数据 */
			pi.setBusinessName(businessName);//设置业务名称
			pi.setMainFormId(formInfo.split(",")[1]);//设置主表单Id
			pi.setName(processName);//设置流程名称
			if(startorId != null){//指定了子流程启动者
				pi
						.getContextInstance()
						.setVariable(
								WorkflowConst.WORKFLOW_PROCESSSTARTER,
								startorId);
				pi.setStartUserId(startorId);//设置发起人Id
				pi.setStartUserName(startorName);//设置发起人名称
			}
			pi.setTypeId(pi.getProcessDefinition().getTypeId());//设置流程类型Id
			
			pi.signal();

			// 添加流程日志时使用
			UserInfo userInfo = getUupInterface().getCurrentUserInfo();
			String parentPiId = String.valueOf(supPi.getContextInstance()
					.getVariableLocally(
							WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID,
							supPi.getRootToken()));
			// 添加流程日志
			/**
			 * 将businessId设置成String
			 */
			getProcessService().addProcessLog(
					new Long(0),
					new Long(supPi.getId()),
					businessName,
					node.getName(),
					userInfo.getUserId(),
					userInfo.getUserName(),
					"",
					"启动子流程[" + processName + "]",
					"null".equalsIgnoreCase(parentPiId) ? new Long(supPi.getId()) 
												: new Long(parentPiId), "1");

			/**
			 * 设定子流程的开始任务的前置任务token
			 */
			Collection subTaskCollection = pi.getTaskMgmtInstance()
					.getTaskInstances();
			if (!subTaskCollection.isEmpty()) {
				
				// 去掉任务节点的取回任务列表后的增加功能
				String fetchTask = String.valueOf(supPi
						.getContextInstance().getVariableLocally(
								WorkflowConst.WORKFLOW_TASK_FETCHINFO,
								token));
				
				// 增加真实上一步任务的信息
				String truePretask = String.valueOf(supPi
						.getContextInstance().getVariableLocally(
								WorkflowConst.WORKFLOW_TASK_TRUEPRETASK,
								token));
				
				Iterator it = subTaskCollection.iterator();
				while (it.hasNext()) {
					TaskInstance ti = (TaskInstance) it.next();
					ti.setSupToken(new Long(token.getId()));

					if (!"null".equals(fetchTask)) {
						ti.setFetchTasks(fetchTask);
					}
					ti.setPreTaskInstance(null);
					
					// 增加真实上一步任务的信息
					if(!"null".equalsIgnoreCase(truePretask)){
						ti.setTruePreTaskinstance(truePretask);
					}
				}
			}
			if ("1".equals(tbn.getNsSubprocessType())) {
				token.suspend();
			} else {
				token.unlock("token[" + token.getId() + "]");
				token.signal();
			}
		} else {
			token.unlock("token[" + token.getId() + "]");
			token.signal();
		}
	}
}
