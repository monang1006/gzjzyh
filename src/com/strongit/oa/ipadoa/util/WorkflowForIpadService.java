package com.strongit.oa.ipadoa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.SessionFactory;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.ITransitionService;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.common.workflow.oabo.BackInfoBean;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.common.workflow.plugin.util.TransitionPluginConst;
import com.strongit.oa.common.workflow.plugin.util.po.OATransitionPlugin;
import com.strongit.oa.common.workflow.service.transitionservice.pojo.TransitionsInfoBean;
import com.strongit.oa.ipadoa.Attachment;
import com.strongit.oa.ipadoa.Attachments;
import com.strongit.oa.ipadoa.Form;
import com.strongit.oa.ipadoa.FormData;
import com.strongit.oa.ipadoa.Item;
import com.strongit.oa.ipadoa.OperateButton;
import com.strongit.oa.ipadoa.Status;
import com.strongit.oa.ipadoa.model.Suggestion;
import com.strongit.oa.ipadoa.model.Suggestions;
import com.strongit.oa.senddoc.bo.TaskBeanTemp;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.util.PropertiesUtil;
import com.strongit.oa.webservice.iphone.server.iphoneWork.iphoneWorkWebService;
import com.strongit.oa.webservice.util.WebServiceAddressUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.usermanage.IUserManager;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseNodesettingPlugin;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.po.TaskInstanceBean;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongmvc.orm.hibernate.Page;

@Service
public class WorkflowForIpadService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IWorkflowService workflowService;
	
	@Autowired
	private IWorkflowConstService workflowConstService;

	@Autowired
	private SendDocManager manager;
	
	@Autowired
	private SendDocUploadManager sendDocUploadManager;

	@Autowired
	private IEFormService eformService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IWorkflowAttachService workflowAttachManager; // 工作流附件管理
	@Autowired
	IUserService userService;
	@Autowired
	private IUserManager userManager;

	protected @Autowired
	IApprovalSuggestionService approvalSuggestionService; // 审批意见接口
	
	protected @Autowired
	AddressOrgManager addressOrgManager;
	@Autowired
	private ITransitionService transitionService;
	@Autowired
	private AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	
	
 
	protected List ideaLst;// 保存在字典中的意见
	
	private IGenericDAO<TUumsBaseOrg, String> orgDao = null ;	 //通用DAO操作类
	
	/**
	 * 注入SESSION工厂
	 * @author:申仪玲
	 * @date:2012-10-10 上午09:48:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		orgDao = new GenericDAOHibernate<TUumsBaseOrg, String>(sessionFactory,TUumsBaseOrg.class);
	}


	public String getForm(String loginUserId, String taskId) throws SystemException {
		Dom4jUtil dom = new Dom4jUtil();

		userService.setUserId(loginUserId);
//		if (!manager.isTaskInCurrentUser(taskId)) {
//			throw new SystemException("该任务已不在您的待办事宜列表中。");
//		}
		FormData data = new FormData();
		// 任务是否允许办理
		String ret = manager.judgeTaskIsDone(taskId);
		String[] rets = ret.split("\\|");
		String retCode = rets[0];
		if (retCode.equals("f1")) {
			throw new SystemException("该任务正在被其他人处理或被挂起，请稍后再试或与管理员联系！");
		} else if (retCode.equals("f2")) {
			throw new SystemException("该任务已被取消，请查阅处理记录或与管理员联系！");
		} else if (retCode.equals("f3")) {
			throw new SystemException("该任务已被其他人处理，请查阅详细处理记录！");
		} else {
			String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
			String formId = info[1]; 
			String bussinessId = info[0];
			if ("".equals(formId) || formId == null || "0".equals(formId)) {// 表单模板不存在,即流程未挂接表单,如栏目流程
				throw new SystemException("流程未挂接表单.");
			}
			manager.signForTask(taskId, "0"); 
			if (!"0".equals(bussinessId)) {
				//TaskInstance taskInstance = workflowService.getTaskInstanceById(taskId);
				TwfBaseNodesetting nodeSetting = workflowService.findFirstNodeSetting(taskId,null);
				String isback="0";
				//是否显示退回按钮标示(这个退回按钮设置在表单域里对应的不是退回到上一步，而是退回到指定的节点) 1:显示 0:不显示
				if (taskId!=null&&!"".equals(taskId)) {
					// 显示意见征询按钮（办公厅需求） 不显示 严建
					//this.loadYjzx(taskId, html, nodeSetting, frameRoot);
					String returnFlag = workflowService.checkCanReturn(taskId);// 回退|驳回|指派|指派返回
					String[] flags = returnFlag.split("\\|");
					if ("1".equals(flags[3])) {// 允许指派返回
					} else {
						// String nodeId = workflow.getNodeIdByTaskInstanceId(taskId);
						// TwfBaseNodesetting nodeSet =
						// workflow.getNodesettingByNodeId(nodeId);
						//this.loadSubmitToNextPersonHtml(frameRoot, html, nodeSetting);// 显示提交下一办理人按钮
					}
					if ("1".equals(flags[1])) {// 允许驳回
					}
					
					User suser = userService.getUserInfoByUserId(userService.getCurrentUser().getUserId());
					String isDeal = suser.getRest1();// 判断当前用户是否为领导人
					String dealFlag = "";
					if(isDeal==null||isDeal.equals("0")){
						 dealFlag = "1";
					}else{
						 dealFlag = "0";
					}
					if ("1".equals(flags[0])&&dealFlag.equals("1")) {// 允许退回
						//1是退回到拟稿 0是退回到上一步
						String isbacfirst="0";
						try {
							isbacfirst = WebServiceAddressUtil.getBackModule();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//this.loadBackSpacePrevHtml(frameRoot, html, nodeSetting);// 显示退回上一办理人按钮
						//this.loadBackSpaceHtml(frameRoot, html, nodeSetting);// 显示退回按钮
						if("1".equals(isbacfirst)){
							/**
							 * 显示退回按钮的
							 */
							Map plugins = nodeSetting.getPlugins();// 得到节点上的插件信息集合
							String visible = null;
							JSONObject json = manager.getPluginsBusinessFlagFromPlugins(plugins,"toBack");
							if(json != null){
								visible = (String) json.get("visible");
							}else{
								visible = "block";
							}
							if (visible != null && visible.toLowerCase().equals("block")) {
								//显示退回按钮
								isback="1";
							}
						}else{
							//显示退回上一步
							Map plugins = nodeSetting.getPlugins();// 得到节点上的插件信息集合
							String buttonName = null;
							String visible = null;
							JSONObject json = manager.getPluginsBusinessFlagFromPlugins(plugins, "toPrev");
							if (json != null) {
								visible = (String) json.get("visible");
							} else {
								visible = "block";
							}
							if (visible != null && visible.toLowerCase().equals("block")) {
								//显示退回上一步按钮
								isback="1";
							}
						}
						
					}
					if ("1".equals(flags[2])) {// 允许指派
						/*boolean enable = this.loadChangeMainActorHtml(frameRoot, html, nodeSetting, taskId);// 加载【主办变更】按钮
						if(!enable){
							this.loadAssignHtml(frameRoot, html);// 显示指派按钮
						}*/
					}
				}
				
				//初始化是否显示签发按钮
				String qianFa = "0";
				Map plugins = nodeSetting.getPlugins();
				TwfBaseNodesettingPlugin plugin = (TwfBaseNodesettingPlugin) plugins.get("plugins_chkModifySend");
				if (plugin != null && plugin.getValue() != null) {
					qianFa = plugin.getValue();
				}
				
				String[] args = bussinessId.split(";");
				String tableName = args[0];
				String pkFieldName = args[1];
				String pkFieldValue = args[2];
				try {
					Map<String, EFormComponent> mapComponent = eformService.getFieldInfo(formId);
					if (mapComponent.isEmpty()) {
						throw new SystemException("表单[" + formId + "]未绑定任何数据库字段！");
					}
					mapComponent.remove(IEFormService.MAINTABLENAME);
					Set<Map.Entry<String, EFormComponent>> entrys = mapComponent.entrySet();
					//
					List<String[]> columns = new ArrayList<String[]>(entrys.size());
					String contentFieldName = null;// 正文字段 这里只考虑只有一个正文的情况
					for (Map.Entry<String, EFormComponent> entry : entrys) {// 获取电子表单上的每一项字段，在此进行循环遍历
						EFormComponent eformField = entry.getValue();
						if ("Blank".equals(eformField.getType())) {//
							continue;
						}
						/**
						 * 如果表单上关联的控件不是tableName表的字段，那么就要去除，否则查询的时候会报错
						 * update by hecj 2014-09-10 15:30 南昌市人社信息收文稿纸表单的附件
						 */
						if(eformField.getTableName()==null||!tableName.equals(eformField.getTableName())){
							continue;
						}
						if ("Iframe".equals(eformField.getType())) {//
							continue;
						}

						if ("Strong.Form.Controls.AccessoryControl".equals(eformField.getType())) {//
							continue;
						}
						//过滤主键数据  申仪玲 2012.03.20
						if("主键".equals(eformField.getCaption())){
							
							continue;
						}

						if ("Office".equals(eformField.getType())) {// 获取正文域字段
							contentFieldName = eformField.getFieldName();
							continue;
 						} else if ("Strong.Form.Controls.ComboxBox".equals(eformField.getType())) {// 下拉控件
							String items = eformField.getItems();
							if (items.indexOf(";") == -1) {// 下拉列表数据是从数据字典中读取
								if (eformField.getSelTableName() != null && !"".equals(eformField.getSelTableName())
										&& eformField.getSelCode() != null && !"".equals(eformField.getSelCode())
										&& eformField.getSelName() != null && !"".equals(eformField.getSelName())) {
									StringBuilder query = new StringBuilder("select ").append(eformField.getSelCode())
											.append(",").append(eformField.getSelName()).append(" from ").append(
													eformField.getSelTableName()).append(" where ");
									if(eformField.getSelFilter()==""||eformField.getSelFilter()==null){
										query.append("1=1");
									}else{
										query.append(eformField.getSelFilter());
									}
									List lst = jdbcTemplate.queryForList(query.toString());
									StringBuilder builderItems = new StringBuilder();
									if (!lst.isEmpty()) {
										for (int i = 0; i < lst.size(); i++) {
											Map map = (Map) lst.get(i);
											builderItems.append(map.get(eformField.getSelCode())).append(",").append(
													map.get(eformField.getSelName())).append(",").append(
													map.get(eformField.getSelCode())).append(";");
										}
									}
									eformField.setItems(builderItems.toString());
								}
							}
						}
						String fieldName = entry.getKey(); 
						if (fieldName == null || fieldName.length() == 0) {
							continue;
						}
 						String [] column = new String [2];
 						column[0] = fieldName;
 						column[1] = eformField.getTabOrder();
 						
 						columns.add(column);
					}
					
					// 按控件TabOrder属性进行排序
					Collections.sort(columns, new Comparator<String[]>() {
						public int compare(String[] strs1, String[] strs2) {
							String s1 = strs1[1];
							String s2 = strs2[1];
							Long l1 = Long.MAX_VALUE;
							Long l2 = Long.MAX_VALUE;
							if (s1 != null && !"".equals(s1)) {
								l1 = Long.parseLong(s1);
							}
							if (s2 != null && !"".equals(s2)) {
								l2 = Long.parseLong(s2);
							}
							return l1.compareTo(l2);
						}
					});
					
					if (columns.size() > 0) {
						String strColumns = "";
						for (String[] strings : columns) {
							if(!strings[0].equals("ADOBE_PDF_NAME")&&!strings[0].equals("OARECVDOCID")){
								strColumns += strings[0] +",";
							}
						}
						//
						strColumns = strColumns.substring(0, strColumns.length()-1);
						Status status = new Status("1", "success");
						data.setStatus(status);
						Form form = new Form();
						List<Item> items = new ArrayList<Item>();
						Attachments attachments = new Attachments();					
						//String strColumns = StringUtils.join(columns, ",");
						StringBuilder sqlQuery = new StringBuilder("select ");
						sqlQuery.append(strColumns).append(" from ").append(tableName);
						sqlQuery.append(" where ").append(pkFieldName).append(" ");
						sqlQuery.append("=").append("'").append(pkFieldValue).append("'");
						Map result = jdbcTemplate.queryForMap(sqlQuery.toString());
						if (result != null && !result.isEmpty()) {
							Set keySet = result.keySet();
							for (Iterator it = keySet.iterator(); it.hasNext();) {
								String key = (String) it.next();
								Object value = result.get(key);
								//去掉无用字段：修改备注   Bug序号： 0000066374,0000066397
								if(key.equals("SENDDOC_REMARK")){
									value = null;
									key = "11111111";
								}
								if(key.equals("SENDDOC_RECV_ENTERPRISE")){
									value = null;
									key = "11111111";
								}
								if (key.equals(contentFieldName)) {// 得到正文
									//
								} else {// 非正文，电子表单上的控件
									EFormComponent component = mapComponent.get(key);
									if (component != null) {
										String caption = component.getCaption();
										if(caption.equals("附件数")){
											caption = "附件";
										}
										if(caption.equals("附件1")){
											caption = "附件";
										}
										if(caption.equals("起止时间")){
											caption = "起始时间";
										}
										if(caption.equals("校核")){
											caption = "校对";
										}
										if (caption != null && caption.length() > 0 && !"".equals(caption)) {
											if (component.getItems() == null || "".equals(component.getItems())) {
												String strValue = value == null ? "" : value.toString();
												if(this.isDate(strValue)){
													strValue = strValue.substring(0, 10);
												}
												if(strValue.contains("00:00:00.0")){
													strValue = strValue.replace("00:00:00.0", "");
												}
												Item item = new Item("string", caption, strValue);
												items.add(item);
											} else {
												String[] itemss = component.getItems().split(";");
												for (String itt : itemss) {
													if (itt.split(",")[0].equals(value)) {
														if ("".equals(itt.split(",")[1])) {
															value = itt.split(",")[2];
															break;
														} if (value.equals(itt.split(",")[2])) {
															value = itt.split(",")[1];
															break;
														} else {
															value = itt.split(",")[2];
															break;
														}
													}
												}
												if(caption.equals("紧急程度")&&("".equals(value) ||value==null)){
													value="普通";
												}
												String strValue = value == null ? "" : value.toString();
												Item item = new Item("string", caption, strValue);
												items.add(item);

											}
										}
									}
								}
							}
						}

						form.setItems(items);
						data.setForm(form);

						List<Attachment> atts = new ArrayList<Attachment>();

						// 附件的
						List<WorkflowAttach> workflowAttachs = workflowAttachManager
								.getWorkflowAttachsByDocId(pkFieldValue);
						if (workflowAttachs != null && !workflowAttachs.isEmpty()) {
							for (WorkflowAttach attach : workflowAttachs) {
								Attachment attachment = new Attachment();

								List<Item> aitems = new ArrayList<Item>();// 附件显示项

								Item aItem = new Item("string", attach.getDocattachid());// 附件ID
								aitems.add(aItem);
								aItem = new Item("string", attach.getAttachName());// 附件名称
								aitems.add(aItem);
								if(attach.getAttachContent() != null){
								    aItem = new Item("string", String.valueOf(attach.getAttachContent().length));// 附件大小
								    aitems.add(aItem);
								}
								
								attachment.setItems(aitems);
								atts.add(attachment);
							}
						}

						attachments.setAtachments(atts);
						data.setAttachments(attachments);

						// 处理意见
						Suggestions suggestions = new Suggestions();
						String instanceId = workflowService.getProcessInstanceIdByTiId(taskId);
						List<Object[]> list = workflowService.getBusiFlagByProcessInstanceId(instanceId);
						if (list != null && !list.isEmpty()) {
							String orgName = "";
							List<Suggestion> suggestionList = new ArrayList<Suggestion>();
							for (int i = 0; i < list.size(); i++) {
								Object[] objs = list.get(i);
								String userId = objs[5].toString();
								Organization org = userService.getUserDepartmentByUserId(userId);

								User user = userService.getUserInfoByUserId(userId);

								if (org != null) {
									orgName = org.getOrgName();
								}
								Suggestion suggestion = new Suggestion();
								List<Item> sItems = new ArrayList<Item>();
								Item sItem = new Item("date", objs[7].toString());// 处理时间
								sItems.add(sItem);
								sItem = new Item("string", orgName == null ? "" : orgName.toString());// 处理人部门名称
								sItems.add(sItem);

								sItem = new Item("string", user.getUserName() == null ? "" : user.getUserName());// 处理人名称
								sItems.add(sItem);
								if(objs[4]!= null){
									objs[4] = objs[4].toString().trim();
								}
								sItem = new Item("string", objs[4] == null ? "" : objs[4].toString());// 意见内容
								sItems.add(sItem);
								
								//任务名称
								sItem=new Item("string",objs[1]==null?"":objs[1].toString());
								sItems.add(sItem);
								
								suggestion.setItems(sItems);
								suggestionList.add(suggestion);
							}
							suggestions.setSuggestions(suggestionList);

						}
						data.setSuggestions(suggestions);
					
						
					 //是否有正文
						List<EFormField> fieldList = manager.getFormTemplateFieldList(formId);
						EFormField eformField = null;
						for (EFormField field : fieldList) {
							if ("Office".equals(field.getType())) {// 找到OFFICE控件类型
								eformField = field;
								data.setHasContent(true);
								break;
							}
						}
						if (eformField == null) {
							data.setHasContent(false);
 						}
						
						/**
						 * 显示操作按钮
						 */
						OperateButton op=new OperateButton();
						List<Item> opList=new ArrayList<Item>();
						Item backItem=new Item();
						backItem.setType("backspace");
						backItem.setValue(isback);
						opList.add(backItem);
						op.setItems(opList);
						data.setOperateButton(op);
						
						//流程线和处理人
					    iphoneWorkWebService workWebService = new iphoneWorkWebService();//获取数据接口
//						List<String[]> transition = workWebService.getOneTransitions(taskId, instanceId);
					    List<String[]> transition = null;//修改此功能，所有迁移线都显示出来，即使只有一个人一条迁移线
						//显示牵引线和办理人标志  1：显示  0：不显示
						String showFlag = "";
						String nodeId = "";
						String transitionId = "";
						List<String[]> taskActors = new ArrayList<String[]>();
						String actorRet = "";
						
						if(transition!=null&&transition.size()==1){
							nodeId = transition.get(0)[3];
							transitionId = transition.get(0)[0];
							if(nodeId!=null&&transitionId!=null){
							taskActors = workWebService.getActor(nodeId, taskId, transitionId);
							}
						    if(taskActors.size()==1){
								 showFlag = "0";
							}else{
								showFlag = "1";
							}
						}else{
							showFlag = "1";
						}
						if(showFlag.equals("1")){
							OperateButton transOp = new OperateButton();
							data.setTransition(transOp);
						}else{
							OperateButton transOp = new OperateButton();
							List<Item> transList = new ArrayList<Item>();
							/*Item sItem = new Item("string", showFlag == null ? "" : showFlag);// 是否显示
							transList.add(sItem);*/
							Item sItem  = new Item("string", transition.get(0)[0] == null ? "" : transition.get(0)[0].toString());//迁移线Id
							transList.add(sItem);
							sItem = new Item("string",transition.get(0)[1] == null ? "" : transition.get(0)[1].toString());// 迁移线名称
							transList.add(sItem);
							sItem = new Item("string",transition.get(0)[3] == null ? "" : transition.get(0)[3].toString());// 节点Id
							transList.add(sItem);
							sItem = new Item("string",transition.get(0)[4] == null ? "" : transition.get(0)[4].toString());// 节点名称
							transList.add(sItem);
							sItem = new Item("string",taskActors.get(0)[0] == null ? "" : taskActors.get(0)[0].toString());// 处理人Id
							transList.add(sItem);
							sItem = new Item("string",taskActors.get(0)[1] == null ? "" : taskActors.get(0)[1].toString());// 处理人名称
							transList.add(sItem);
							sItem = new Item("string",taskActors.get(0)[2] == null ? "" : taskActors.get(0)[2].toString());// 处理人组织机构Id
							transList.add(sItem);
							transOp.setItems(transList);
							data.setTransition(transOp);
						}
						
						/**
						 * 显示取回操作按钮
						 */
						String istake = "";
						User suser = userService.getUserInfoByUserId(userService.getCurrentUser().getUserId());
						String isDeal = suser.getRest1();// 判断当前用户是否为领导人
//						if(isDeal==null||isDeal.equals("0")){
							istake = "1";
//						}else{
//							istake = "0";
//						}
						OperateButton qh=new OperateButton();
						List<Item> qhList=new ArrayList<Item>();
						Item taskItem=new Item();
						taskItem.setType("taskspace");
						taskItem.setValue(istake);
						qhList.add(taskItem);
						qh.setItems(qhList);
						data.setTasktion(qh);
						
						/**
						 * 显示签发操作按钮
						 */
						OperateButton qh2=new OperateButton();
						List<Item> qhList2=new ArrayList<Item>();
						Item taskItem2=new Item();
						taskItem2.setType("modifySend");
						taskItem2.setValue(qianFa);
						qhList2.add(taskItem2);
						qh2.setItems(qhList2);
						data.setTasktion(qh2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException(e);
				}
			} else {
				throw new SystemException("流程数据不存在或已删除。");
			}
		}

		// 解析成XML格式的数据
		String strData = dom.GenerateXmlFormData(data);
		logger.info("返回流程表单数据：" + strData);
		return strData;
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
					Properties prop = PropertiesUtil.getPropertiesWithFileName("colorSet.properties");
					for (String key : transGroupList) {
						//过滤迁移线分组信息【过滤依据是判断该迁移线所属分组是否为意见征询】	严建 2012-06-23 15:27
						if (key.equals(prop.getProperty("yjzxGroupId"))) {// 该分组属于意见征询分组
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
										listNode[5] = "0";
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
			 throw new SystemException("获取下一步骤数据级异常:"+ e.getMessage());
		}
		return transitions;
	}
	
	public boolean isDate(String date){ 
        try{ 
            return java.text.DateFormat.getDateInstance().parse(date)!=null; 
        }catch(java.text.ParseException   e){ 
            return false; 
        } 
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
	 * @param userId
	 * @param parentSysCode
	 * @return 返回机构
	 */
	public List<TUumsBaseOrg> getOrgInfo(String userId, String parentSysCode) {
		List<TUumsBaseOrg> relList = new ArrayList<TUumsBaseOrg>();

		Organization rootOrg = null;
		;
		userService.setUserId(userId);
		List<Organization> orgList = userService.getAllDeparments();
		// 找到机构根节点,编码长度最短的是根节点
		List<Organization> newList = new ArrayList<Organization>(orgList);
		Collections.sort(newList, new Comparator<Organization>() {

			public int compare(Organization o1, Organization o2) {
				String code1 = o1.getOrgSyscode();
				String code2 = o2.getOrgSyscode();
				if (code1 != null && code2 != null) {
					return code1.length() - code2.length();
				}
				return 0;
			}

		});
		String root = null;
		if (!newList.isEmpty()) {
			if ("".equals(parentSysCode) || parentSysCode == null) {// 返回根节点组织
				rootOrg = newList.get(0);
				relList.add(rootOrg);
			} else {// 子节点组织
				List<TUumsBaseOrg> list = userService.getSubOrgInfoByOrgsyscode(parentSysCode, "0");
				Collections.sort(list, new Comparator<TUumsBaseOrg>() {
					public int compare(TUumsBaseOrg o1, TUumsBaseOrg o2) {
						String code1 = o1.getOrgSyscode();
						String code2 = o2.getOrgSyscode();
						if (code1 != null && code2 != null) {
							return code1.length() - code2.length();
						}
						return 0;
					}

				});
				if (!list.isEmpty()) {
					list.remove(0);
				}

				return list;
			}
		}
		return relList;
	}
	
	/**
	 * 根据传入的机构编码获得其子机构（只返回下一级机构， 不含子机构的子机构）
	 * @author 申仪玲
	 * @date 2012-10-10 上午09:50:08
	 * @param userId
	 * @param parentSysCode
	 * @return 组织机构列表
	 */
	public List<TUumsBaseOrg> getSubOrgInfo(String userId, String parentSysCode) {
		String orgId = "";
		if (!"".equals(parentSysCode)&& parentSysCode != null) {// 没传入机构编码，则返回机构根节点			
			TUumsBaseOrg org = userService.getOrgInfoBySyscode(parentSysCode);
			orgId = org.getOrgId();
		}
		
		StringBuilder hql = new StringBuilder("from TUumsBaseOrg t where t.orgIsdel = 0 ");
		if(!"".equals(orgId)){		
			hql.append("and t.orgParentId = '").append(orgId).append("'");
		}else{
			hql.append("and t.orgParentId is null");
		}
		
		List<TUumsBaseOrg> list =this.orgDao.find(hql.toString());
		if(!list.isEmpty()){			
			Collections.sort(list, new Comparator<TUumsBaseOrg>() {
				public int compare(TUumsBaseOrg o1, TUumsBaseOrg o2) {
					Long seq1 = o1.getOrgSequence();
					Long seq2 = o2.getOrgSequence();
					if (seq1 != null && seq2 != null) {
						return seq1.compareTo(seq2);
					}
					return 0;
				}
				
			});
		}
		return list; 
	}

	/**
	 * @param userId
	 * @param parentSysCode
	 * @return 返回机构
	 */
	public List<TUumsBaseUser> getSystemAddressUsers(String userId, String orgSyscode) {
		userService.setUserId(userId);
		TUumsBaseOrg org = new TUumsBaseOrg();
		List<TUumsBaseUser> userList = new ArrayList<TUumsBaseUser>();

		if ("".equals(orgSyscode) || orgSyscode == null) {// 返回根节点组织
		// String rootOrgId = null;
		// List<Organization> orgList = userService.getAllDeparments();
		// // 找到机构根节点,编码长度最短的是根节点
		// List<Organization> newList = new ArrayList<Organization>(orgList);
		// Collections.sort(newList, new Comparator<Organization>() {
		// public int compare(Organization o1, Organization o2) {
		// String code1 = o1.getOrgSyscode();
		// String code2 = o2.getOrgSyscode();
		// if (code1 != null && code2 != null) {
		// return code1.length() - code2.length();
		// }
		// return 0;
		// }
		//
		// });
		// if (!newList.isEmpty()) {
		// rootOrgId = newList.get(0).getOrgId();
		// }
		// userList = userService.getAllUserInfoByHa(rootOrgId);
		} else {
			org = userService.getOrgInfoBySyscode(orgSyscode);
			// List<TUumsBaseUser>
			userList = userService.getUserInfoByOrgId(org.getOrgId(), "1", "0");
			// userList = userService.getAllUserInfoByHa(org.getOrgId());
		}
		return userList;

	}
	
	/**
	 * @param userId
	 * @param parentSysCode
	 * @param page
	 * @return 返回机构
	 * @throws Exception 
	 */
	public Page getSystemAddressUsers(String userId, String orgSyscode, Page page ) throws Exception {
		userService.setUserId(userId);
		TUumsBaseOrg org = new TUumsBaseOrg();
		List<TUumsBaseUser> userList = new ArrayList<TUumsBaseUser>();
		String orgId = "";

		if (!"".equals(orgSyscode) && orgSyscode != null) {
			orgId = userService.getOrgInfoBySyscode(orgSyscode).getOrgId();
			
		}
		
//		org = userService.getUserDepartmentByUserId(userId);
//		if("".equals(orgId)){
//			orgId = org.getOrgId();		
//		}
		
		page = addressOrgManager.getUsersByOrgId(page, orgId, "", "", "");

		return page;

	}
	

	/**获取上一步办理人
	* @description 
	* @method getTaskIdMapPreTaskBeanTemper
	* @author 申仪玲(shenyl)
	* @created 2012-4-18 上午10:47:17
	* @param taskIdList
	* @param userId
	* @return
	* @return Map<String,TaskBeanTemp>
	* @throws Exception
	*/
	public Map<String, TaskBeanTemp> getTaskIdMapPreTaskBeanTemper(List<Long> taskIdList,String userId){
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
				if(workflowService.isBackTaskByTid(taskId)){
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
					List<TaskInstanceBean> lists = workflowService.getTruePreTaskInfosByTiId(taskId);
					if(lists == null || lists.isEmpty()){
						User user = userService.getUserInfoByUserId(userId);
						TaskInstance taskInstanceTemp = workflowService.getTaskInstanceById(taskId);
						Long preTaskInstanceId= taskInstanceTemp.getPreTaskInstance();
						if(preTaskInstanceId != null){//存在前一步任务id
							TaskInstance preTaskInstance = workflowService.getTaskInstanceById(preTaskInstanceId+"");
							taskBeanTemp.setStart(preTaskInstance.getEnd());
							taskBeanTemp.setTaskActorId(preTaskInstance.getActorId());
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
								Date endDate = workflowService
								.getTaskInstanceById(taskinstancebean.getTaskId()+"").getEnd();
								Date lastestendDate = workflowService
								.getTaskInstanceById(lastest.getTaskId()+"").getEnd();
								if(endDate.after(lastestendDate)){
									lastest = taskinstancebean;
								}
							}
						}
						TaskInstanceBean taskinstancebean = lastest;
						taskBeanTemp.setTaskActorId(taskinstancebean.getTaskActorId());
						taskBeanTemp.setTaskActorName(taskinstancebean.getTaskActorName());
						taskBeanTemp.setStart(workflowService
								.getTaskInstanceById(taskinstancebean.getTaskId()+"").getEnd());
					}
					userIds.add(taskBeanTemp.getTaskActorId());//无法直接查询出处室信息，需要通过另外的方式进行数据查询
				}
				TaskIdMapPreTaskBeanTemp.put(taskId, taskBeanTemp);
			}
			if (userIds != null && !userIds.isEmpty()) {
				Map<String, UserBeanTemp> map = sendDocUploadManager.getUserIdMapUserBeanTemp(userIds);//获取用户信息
				if (map != null && !map.isEmpty()) {
					List<String> keys = new ArrayList<String>(
							TaskIdMapPreTaskBeanTemp.keySet());//获取所有的taskid
					for (int i = 0; i < keys.size(); i++) {
						String key = keys.get(i);
						TaskBeanTemp taskBeanTemp = TaskIdMapPreTaskBeanTemp
								.get(key);
						String actorId = taskBeanTemp.getTaskActorId();
						if(map.containsKey(actorId)){//只对那么添加到列表userIds中的数据进行匹配
							UserBeanTemp userBean = map.get(actorId);
							if (userBean == null) {
								actorId = userId;
								userBean = map.get(actorId);
							}
							/*设置处室信息*/
							taskBeanTemp.setTaskActorName(userBean.getUserName());
							taskBeanTemp.setOrgId(userBean.getOrgId());
							taskBeanTemp.setOrgName(userBean.getOrgName());
							TaskIdMapPreTaskBeanTemp.put(key, taskBeanTemp);
						}
					}
				}
			}
		}
		return TaskIdMapPreTaskBeanTemp;
	}
	/**移动webservice 调用接口
	 * @param userId
	 * @param parentSysCode
	 * @param page
	 * @return 返回机构
	 * @throws Exception 
	 */
	public Page getSystemAddressUsers(String userId, String userName, String orgSyscode, Page page ) throws Exception {
		userService.setUserId(userId);
		TUumsBaseOrg org = new TUumsBaseOrg();
		List<TUumsBaseUser> userList = new ArrayList<TUumsBaseUser>();
		String orgId = "";

		if (!"".equals(orgSyscode) && orgSyscode != null&&orgSyscode.length()!=3) {
			orgId = userService.getOrgInfoBySyscode(orgSyscode).getOrgId();
			if(userService.isSystemDataManager(userService.getCurrentUser().getUserId())) {
//				page = userService.queryOrgUsers(page, orgId, userName,
//						null, "0", null);
			    	page=userManager.getUsersOfOrgAndChildByOrgcodeForPageByHa(page,orgSyscode,"","0",userService.getCurrentUser().getOrgId());
//				page = addressOrgManager.getUsersByOrgId(page, orgId, userName);//替换接口，通讯录iphone和pc端相统一
			}else{
				if(userName != null && !"".equals(userName)){
//					page = userService.queryOrgUsers(page, orgId, userName,
//							null, "0", "0");
				    	page=userManager.getUsersOfOrgAndChildByOrgcodeForPageByHa(page,orgSyscode,"","0",userService.getCurrentUser().getOrgId());
				}else{
				    	//pc和ios端相统一  谭飞  2014-05-28
					page=userManager.getUsersOfOrgAndChildByOrgcodeForPageByHa(page,orgSyscode,"","0",userService.getCurrentUser().getOrgId());
					//String hasOrgId=userService.getCurrentUser().getOrgId();
					//page = userService.queryOrgUsersByHa(page, orgId, null, null, "0", null, hasOrgId);	
					
				}
			}
		}else{
			//addressOrgManager.getUsersByOrgId(page, orgId, userName, "", "");
			//if(userService.isSystemDataManager(userService.getCurrentUser().getUserId())) {
//				page = userService.queryUsers(page, userName,null, null, "0", null);
			//}else{
				//page=userService.queryUsersByHa(page, userName, null, null, "0", null, userService.getCurrentUserInfo().getOrgId());				
			//}
			page = addressOrgManager.getUsersByOrgId(page, orgId, userName,"");
		}
		

		/*if (!"".equals(orgSyscode) && orgSyscode != null&&orgSyscode.length()!=3) {
			orgId = userService.getOrgInfoBySyscode(orgSyscode).getOrgId();
			
		}
		
		page = addressOrgManager.getUsersByOrgId(page, orgId, userName, "", "");*/

		return page;

	}
}
