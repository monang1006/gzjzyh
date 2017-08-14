package com.strongit.oa.docbacktracking;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.autoencoder.CodemanageManager;
import com.strongit.oa.bgt.model.ToaYjzx;
import com.strongit.oa.bgt.senddoc.DocManager;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.common.business.jbpmbusiness.JBPMBusinessManager;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.service.util.IActorAdpter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.docbacktracking.service.IDocBackTrackingService;
import com.strongit.oa.docbacktracking.service.IWorkflowDriveService;
import com.strongit.oa.docbacktracking.vo.WorkflowInfoVo;
import com.strongit.oa.docbacktracking.vo.WorklowNodeVo;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 公文补录功能Action
 * 
 * @author yanjian
 * 
 * Oct 9, 2012 11:05:53 AM
 */
@ParentPackage("default")
@Results({@Result(name = "yjzx", value = "/WEB-INF/jsp/senddoc/sendDoc-writeyjzx.jsp", type = ServletDispatcherResult.class)})
public class DocBackTrackingAction extends BaseActionSupport {

	/**
	 * @field serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	protected IDocBackTrackingService docBackTrackingService;

	@Autowired
	IWorkflowDriveService workflowDriveService;

	@Autowired
	ITaskService taskService;
	
	@Autowired
	IWorkflowService workflowService;
	
	
	@Autowired
	IUserService userService;

	@Autowired
	SendDocUploadManager sendDocUploadManager;
	@Autowired
	JBPMBusinessManager jbpmBusinessManager;

	@Autowired
	IEFormService eformService; // 电子表单服务类

	@Autowired
	DefinitionPluginService definitionPluginService;// 流程定义插件服务类

	@Autowired
	CodemanageManager codeService;// 编号生成器服务类
	@Autowired
    IActorAdpter actorAdpter;
	@Autowired
	DocManager docManager;
	/**
	 * @field workflowType 流程类型
	 */
	private String workflowType;

	private Page page = new Page(FlexTableTag.MAX_ROWS, false);

	private List showColumnList;// 显示字段列表

	private String workflowName;

	private String transitionName;

	private String transitionId;

	private String formId;

	private String tableName;

	private String pkFieldName;

	private String pkFieldValue;

	private String bussinessId;

	private String userJob;

	private String workflowCode;

	private String userId;

	private String userName;

	private String orgName;

	private String workflowSateId; // 流程状态标识id

	private String nodeName;
	private String businessType;

	private List<EFormComponent> queryColumnList;// 查询字段列表
	private String suggestion;
	private String returnFlag;
	private String strTaskActors;
	private String taskId;
	private String instanceId;
	private String concurrentTrans;
	private String CASignInfo; // 签名信息
	private String curActorId;
	private Date handleDate;//处理时间
	private String businessName;
	private String docId;	//意见征询公文标识
	/**
	 * @field approveinfo	办理记录bo
	 */
	private  TwfInfoApproveinfo approveinfo;
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<EFormComponent> getQueryColumnList() {
		return queryColumnList;
	}

	public void setQueryColumnList(List<EFormComponent> queryColumnList) {
		this.queryColumnList = queryColumnList;
	}

	public List getShowColumnList() {
		return showColumnList;
	}

	public void setShowColumnList(List showColumnList) {
		this.showColumnList = showColumnList;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub

		if (workflowName == null || workflowName.equals("")) {
			if (pkFieldValue != null && !pkFieldValue.equals("")) {
				Map map = docBackTrackingService.getSystemField(pkFieldValue,
						tableName);
				workflowName = (String) map
						.get(BaseWorkflowManager.WORKFLOW_NAME);// 得到流程名
			}
		}
		TwfBaseNodesetting nodeSetting = docBackTrackingService
				.getOperationHtml(null, workflowName);
		User user = userService.getCurrentUser();// 得到当前用户
		userId = user.getUserId();
		userName = user.getUserName();// 当前用户姓名
		Organization organization = userService.getDepartmentByLoginName(user
				.getUserLoginname());// 得到当前用户所属单位
		orgName = organization.getOrgName();// 当前用户所属单位名称
		if (nodeSetting != null) {// 流程启动表单取第一个节点上的表单
			formId = nodeSetting.getNsNodeFormId().toString();
		}
		if (pkFieldValue == null || "".equals(pkFieldValue)) {// 新建
			userJob = user.getUserDescription();// 得到用户职务
			if (userJob == null) {
				userJob = "";
			}
			tableName = eformService.getTable(formId);
			workflowCode = "";
//			String codeId = definitionPluginService
//					.getRuleCodeIdByWorkflowName(workflowName);
//			if (codeId != null) {
//				workflowCode = codeService.getCodeToFlow(codeId);
//				logger.info("流程[" + workflowName + "]编码规则为：" + workflowCode);
//
//			}

		} else {// 修改
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = docBackTrackingService
						.getPrimaryKeyName(tableName);// 得到主键名称
			}
			bussinessId = new StringBuilder().append(tableName).append(";")
			.append(pkFieldName).append(";").append(pkFieldValue)
			.toString();
			Map<String,String> map = docBackTrackingService.getPIdByBusid(bussinessId, userId, workflowName);
			if(map != null){
				instanceId = map.get("processInstanceId");
				ToaYjzx toayjzx = docManager.getYjzx(pkFieldValue);
				if (toayjzx != null) {// 已经进行征询的文不再进行征询
					businessType = "0";
				}
				if(docBackTrackingService.isEnd(bussinessId)){
					return this.renderHtml("<script>alert(\"该公文已经办结\");top.close();</script>");
				}
			}
		}
		sendDocUploadManager.initViewByNodeSetting(nodeSetting);
		return INPUT;
	}

	/**
	 * 公文是否办结
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Oct 16, 2012 12:31:51 PM
	 */
	public String isEnd() throws ServiceException,DAOException, SystemException{
		try {
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = docBackTrackingService
						.getPrimaryKeyName(tableName);// 得到主键名称
			}
			bussinessId = new StringBuilder().append(tableName).append(";")
			.append(pkFieldName).append(";").append(pkFieldValue)
			.toString();
			if(docBackTrackingService.isEnd(bussinessId)){
				return this.renderText("1");
			}else{
				return this.renderText("0");
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
	
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 跳转到主操作界面
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 9, 2012 11:07:51 AM
	 */
	public String container() throws ServiceException, DAOException,
			SystemException {
		try {

		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return "container";
	}

	/**
	 * 显示流程树结构
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 9, 2012 11:13:16 AM
	 */
	public String tree() throws ServiceException, DAOException, SystemException {
		try {
			List<WorkflowInfoVo> workflowInfoVolist = docBackTrackingService
					.getWorkflowInfoVoList(workflowType);
			getRequest().setAttribute("workflowInfoVolist", workflowInfoVolist);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return "tree";
	}

	/**
	 * 补录公文列表
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 9, 2012 11:15:57 AM
	 */
	@SuppressWarnings("unchecked")
	public String gridview() throws ServiceException, DAOException,
			SystemException {
		try {
			try {
				Object[] obj = docBackTrackingService.getDocList(page,
						workflowName, formId);
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
			} catch (Exception e) {
				logger.error("获取草稿列表时发生异常", e);
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
		return "gridview";
	}

	/**
	 * 补录公文列表
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 9, 2012 6:50:55 PM
	 */
	public String doclist() throws ServiceException, DAOException,
			SystemException {
		try {
			return "doclist";
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
	 * 添加审批意见
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 10, 2012 9:56:26 AM
	 */
	public String approvalinput() throws ServiceException, DAOException,
			SystemException {
		try {
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = docBackTrackingService
						.getPrimaryKeyName(tableName);// 得到主键名称
			}
			bussinessId = new StringBuilder().append(tableName).append(";")
					.append(pkFieldName).append(";").append(pkFieldValue)
					.toString();
//			if (!docBackTrackingService.existsWorkflowState(workflowName,
//					bussinessId)) {// 数据表中不存在对应的流程信息
//				docBackTrackingService.saveStartNodeState(workflowName,
//						bussinessId); // 保存开始节点的信息
//			}
			if(docBackTrackingService.isEnd(bussinessId)){
				return this.renderHtml("<script>alert(\"该公文已经办结，不能添加意见\");top.close();</script>");
			}
			return "approvalinput";
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
	 * 删除公文
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 26, 2012 3:46:12 PM
	 */
	public String deleteDoc() throws ServiceException,DAOException, SystemException{
		try {
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = docBackTrackingService
						.getPrimaryKeyName(tableName);// 得到主键名称
			}
			bussinessId = new StringBuilder().append(tableName).append(";")
			.append(pkFieldName).append(";").append(pkFieldValue)
			.toString();
			userId = userService.getCurrentUser().getUserId();
			docBackTrackingService.deleteDoc(bussinessId, userId, workflowName);
			return this.renderText("1");
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
	 * 编辑办理记录
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 10:17:34 AM
	 */
	public String editapproveinfo() throws ServiceException,DAOException, SystemException{
		try {
			if(approveinfo.getAiId() == null){
				throw new IllegalAccessException("参数【approveinfo.aiId】的值只能为数字");
			}else{
				approveinfo = workflowDriveService.getApproveInfoByAiId(approveinfo.getAiId().toString());
			}
			return "editapproveinfo";
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
	 * 保存办理记录
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 10:19:07 AM
	 */
	public String saveapproveinfo() throws ServiceException,DAOException, SystemException{
		try {
			if(approveinfo.getAiId() == null){
				throw new IllegalAccessException("参数【approveinfo.aiId】的值只能为数字");
			}else{
				TwfInfoApproveinfo oldApproveinfo =  workflowDriveService.getApproveInfoByAiId(approveinfo.getAiId().toString());
//				 处理意见信息
				suggestion = approveinfo.getAiContent();
				if (suggestion != null) {
					/* modify yanjian 2011-11-06 bug-2635 */
					suggestion = java.net.URLDecoder.decode(suggestion, "UTF-8");
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
				oldApproveinfo.setAiContent(suggestion);//设置内容
				oldApproveinfo.setAiDate(approveinfo.getAiDate());	//设置时间
				workflowDriveService.saveApproveInfo(oldApproveinfo);//保存数据
			}
			return this.renderHtml("<script>top.returnValue=\"OK\";top.close();</script>");
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
	 * 办理记录列表
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 22, 2012 10:28:50 AM
	 */
	public String approveinfolist() throws ServiceException,DAOException, SystemException{
		try {
			if(instanceId == null || "".equals(instanceId) || "null".equals(instanceId)){
				throw new IllegalAccessException("参数【instanceId】的值只能为数字");
			}else{
				List<TwfInfoApproveinfo> list = workflowDriveService.getApproveInfoForListByPiId(instanceId);
				 int td2 = 150;
				    int td4 = 160;
				    int td5 = 80;
					String tableString = "<table cellSpacing=1 cellPadding=1  border=\"0\" class=\"table1\" style=\"vertical-align: top;width: 100%;table-layout: fixed;\" >";
					String trString = "<tr class=\"biao_bg2\">"+
					"<td class=\"td1\" align=\"center\" width=\"10%\" >"+
						"<strong>环节名称</strong>"+
					"</td>"+
					"<td class=\"td2\" align=\"center\" width=\""+td2+"\" >"+
						"<strong>处理人</strong>"+
					"</td>"+
					"<td class=\"td3\" align=\"center\">"+
						"<strong>审批意见</strong>"+
					"</td>"+
					"<td class=\"td4\" align=\"center\" width=\""+td4+"\" >"+
						"<strong>处理时间</strong>"+
					"</td>"+
					"<td class=\"td5\" align=\"center\" width=\""+td5+"\" >"+
					"<strong>操作</strong>"+
					"</td>";
					trString = trString +"</tr>";
					tableString  = tableString +trString;
					for(int i=0;i<list.size();i++){
						TwfInfoApproveinfo twfinfoapproveinfo = list.get(i);
						tableString  = tableString +"<tr class=\"biao_bg1\">"+
						"<td align=\"left\">"+
						twfinfoapproveinfo.getAiTaskname()+
						"</td>"+
						"<td align=\"left\">"+
						twfinfoapproveinfo.getAiActor()+
						"</td>"+
						"<td align=\"left\" title='"+(twfinfoapproveinfo.getAiContent() == null?"":twfinfoapproveinfo.getAiContent())+"'>"+
						(twfinfoapproveinfo.getAiContent() == null?"":twfinfoapproveinfo.getAiContent())+
						"</td>"+
						"<td align=\"left\">"+
							(twfinfoapproveinfo.getAiDate() == null ?"":new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(twfinfoapproveinfo.getAiDate()))+
						"</td>"
						+"<td align=\"left\"><input type=\"button\" value=\"修改\"   class=\"input_bg\" onclick=\"edit("+twfinfoapproveinfo.getAiId()+")\"></td>";
						
						tableString  = tableString + "</tr>";
							
						}
					tableString = tableString+"</table><br/>";
					getRequest().setAttribute("tableString", tableString);
			}
			
			return "approveinfolist";
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
	 * 审批意见列表
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 10, 2012 2:27:15 PM
	 */
	public String approvallist() throws ServiceException, DAOException,
			SystemException {
		try {
//			page = docBackTrackingService.getApprovalForPage(page,
//					workflowSateId);
//			if (page.getTotalCount() < 0) {
//				page.setTotalCount(0);
//			}
			return "approvallist";
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

	public String yjzx() throws ServiceException,DAOException, SystemException{
		try {
			bussinessId = new StringBuilder().append(tableName).append(";")
			.append(pkFieldName).append(";").append(pkFieldValue)
			.toString();
			String startUserId = userService.getCurrentUser().getUserId();
			Map<String,String> map = docBackTrackingService.getPIdByBusid(bussinessId, startUserId, workflowName);
			instanceId = map.get("processInstanceId");
			businessName = map.get("businessName");
			docId = bussinessId;
			return "yjzx";
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
	 * 流程节点树
	 * 
	 * @author yanjian
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Oct 10, 2012 10:22:19 AM
	 */
	public String nodetree() throws ServiceException, DAOException,
			SystemException {
		try {
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = docBackTrackingService
						.getPrimaryKeyName(tableName);// 得到主键名称
			}
			bussinessId = new StringBuilder().append(tableName).append(";")
					.append(pkFieldName).append(";").append(pkFieldValue)
					.toString();
			
			List<WorklowNodeVo> workflowNodeVolist = docBackTrackingService
					.getWorklowNodeVoList(workflowName, bussinessId);
			getRequest().setAttribute("workflowNodeVolist", workflowNodeVolist);
			getRequest().setAttribute("lastestworkflowNodeVo", workflowNodeVolist.get(0));
			
			return "nodetree";
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



	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {

		this.formId = formId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		try {
			workflowName = URLDecoder.decode(workflowName, "utf-8");
			workflowName = URLDecoder.decode(workflowName, "utf-8");// 解决在weblogic中workflowName字段乱码问题
		} catch (UnsupportedEncodingException e) {
			logger.error("流程名称转码异常！");
		}
		this.workflowName = workflowName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserJob() {
		return userJob;
	}

	public void setUserJob(String userJob) {
		this.userJob = userJob;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkflowCode() {
		return workflowCode;
	}

	public void setWorkflowCode(String workflowCode) {
		this.workflowCode = workflowCode;
	}

	public String getWorkflowSateId() {
		return workflowSateId;
	}

	public void setWorkflowSateId(String workflowSateId) {
		this.workflowSateId = workflowSateId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		try {
			nodeName = URLDecoder.decode(nodeName, "utf-8");
			nodeName = URLDecoder.decode(nodeName, "utf-8");// 解决在weblogic中workflowName字段乱码问题
		} catch (UnsupportedEncodingException e) {
			logger.error("流程名称转码异常！");
		}
		this.nodeName = nodeName;
	}

	public String getTransitionName() {
		return transitionName;
	}

	public void setTransitionName(String transitionName) {
		try {
			transitionName = URLDecoder.decode(transitionName, "utf-8");
			transitionName = URLDecoder.decode(transitionName, "utf-8");// 解决在weblogic中workflowName字段乱码问题
		} catch (UnsupportedEncodingException e) {
			logger.error("流程名称转码异常！");
		}
		this.transitionName = transitionName;
	}



	
	
	public String nextstep() throws ServiceException,DAOException, SystemException{
		try {
			if(taskId!=null && taskId.startsWith("-")){
				taskId = null;
			}
			if(instanceId != null && !instanceId.startsWith("-")){
				Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
				 List[] listTemp = sendDocUploadManager.getUserBeanTempArrayOfProcessStatusByPiId(instanceId,AllUserIdMapUserBeanTem);
				 if(listTemp != null && listTemp.length != 0){
					 curActorId = listTemp[0].get(0).toString();
				 }
			}
			if(curActorId == null){
				curActorId = userService.getCurrentUser().getUserName();
			}
			if (pkFieldName == null || "".equals(pkFieldName)) {
				pkFieldName = docBackTrackingService
						.getPrimaryKeyName(tableName);// 得到主键名称
			}
			bussinessId = new StringBuilder().append(tableName).append(";")
					.append(pkFieldName).append(";").append(pkFieldValue)
					.toString();
			TwfBaseNodesetting nodeSetting = docBackTrackingService.getOperationHtml(
					taskId, workflowName);
			if (nodeSetting != null) {// 流程启动表单取第一个节点上的表单
				formId = nodeSetting.getNsNodeFormId().toString();
			}
			return "nextstep";
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
	
		@SuppressWarnings("unchecked")
		public String handleNextStep() throws SystemException, DAOException,
				ServiceException {
			try {
				if (formId != null && !formId.equals("")) {
					formId = formId.toUpperCase();
				}
				ActionContext cxt = ActionContext.getContext();
				cxt.getSession().put("transDept", returnFlag);// 存储协办处室信息,用于子流程是否显示协办处室
				cxt.getSession().put("recOrg", transitionName);// 存储签收部门信息
				cxt.getSession().put("recUser", strTaskActors);// 存储签收人员信息
				// 将迁移线信息保存在session中
				if (transitionId != null && transitionId.length() > 0) {
					String[] transitionIds = transitionId.split(",");
					List<String[]> stackTransitionId = new ArrayList<String[]>();
					for (String tid : transitionIds) {
						Long subNodeId = workflowService.getNodeIdByTransitionId(tid);
						TwfBaseNodesetting subNodeSetting = workflowService.getNodesettingByNodeId(subNodeId.toString());
						if (subNodeSetting.getNsSubprocessSetting() != null
								&& !subNodeSetting.getNsSubprocessSetting().equals("")) {
							String processName = subNodeSetting
									.getNsSubprocessSetting().split("\\|")[0];
							stackTransitionId.add(new String[] { processName, tid });
						}
					}
					cxt.put("transitionId", stackTransitionId);
				}
				// 处理意见信息
				if (suggestion != null) {
					/* modify yanjian 2011-11-06 bug-2635 */
					suggestion = java.net.URLDecoder.decode(suggestion, "UTF-8");
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
				// 处理ca信息
				JSONObject json = new JSONObject();
				json.put("suggestion", suggestion);
				json.put("CAInfo", "");
				CASignInfo = json.toString();
				// 生成日志信息

				// 适配选择人员
				strTaskActors = actorAdpter.getActors(strTaskActors);

				// 将用户信息保存在session中
				cxt.put("strTaskActors", strTaskActors);
				if (taskId == null || "".equals(taskId)) {// 送审流程
					startWorkflow();
				} else {// 提交流程
					gotoNextWorkflow();
					ProcessInstance processInstance = workflowService.getTaskInstanceById(taskId).getProcessInstance();
					if(processInstance.hasEnded()){
						//流程结束时，设置为正常状态
						bussinessId = processInstance.getBusinessId();
						Tjbpmbusiness bModel = jbpmBusinessManager.findByBusinessId(bussinessId);
						if(bModel == null){
							bModel = new Tjbpmbusiness(bussinessId);
						}
						if(Tjbpmbusiness.BUSINESS_TYPE_DOCBACKTRACK_YJZX.equals(bModel.getBusinessType())){//补录状态下的意见征询
							bModel.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_YJZX);
						}else{
							bModel.setBusinessType(Tjbpmbusiness.BUSINESS_TYPE_COMMON);
						}
						jbpmBusinessManager.saveModel(bModel);
					}
				}
				// 执行完成之后清空SESSION中数据,added by dengzc 2010年12月16日15:13:00
//				 执行完成之后清空SESSION中数据,added by dengzc 2010年12月16日15:13:00
				Map<String, Object> session = cxt.getSession();
				String remindType = (String) session.get("remindType");
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
				// ----------END--------------------
				return renderHtml("<script>top.backCall();</script>");// 提交至流程后关闭窗口
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
		private void startWorkflow()
				throws SystemException, DAOException, ServiceException {
			try {
				if (taskId == null || "".equals(taskId)) {// 送审流程
					String[] strTaskActorArray = null;
					if ("".equals(strTaskActors)) {
					} else {
						// 加入对串行会签排序支持,按排序号倒序排列.added by dengzc 2011年2月16日11:34:07
						strTaskActorArray = strTaskActors.split(",");
						doFilterTaskActors(strTaskActorArray);
						// ------------End--------------------
					}
					instanceId = workflowDriveService.handleWorkflow(formId, workflowName, bussinessId, strTaskActorArray, transitionName, concurrentTrans, CASignInfo,handleDate);
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
		@SuppressWarnings("unchecked")
		private void gotoNextWorkflow()
				throws SystemException, DAOException, ServiceException {
			try {
				if (taskId != null && !"".equals(taskId)) {//  提交下一步
					String[] info = docBackTrackingService.getFormIdAndBussinessIdByTaskId(taskId);
					bussinessId = info[0];
					
					String curUserId = userService.getCurrentUser().getUserId();
					TaskInstance taskInstance = workflowService.getTaskInstanceById(taskId);
					Set set =taskInstance.getPooledActors();
					if(set != null && !set.isEmpty()){
						List list = new ArrayList(set);
						PooledActor poolActor = (PooledActor)list.get(0);
						curUserId = poolActor.getActorId();
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
					workflowDriveService.handleWorkflowNextStep(taskId, transitionName, "", isNewForm, formId, bussinessId, CASignInfo, curUserId, strTaskActorArray,handleDate);
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
		 * 同步工作流中的businessName
		 * 
		 * @author 严建
		 * @createTime Mar 21, 2012 2:52:35 PM
		 */
		protected void synchronizedPiBusName() {
			// 同步工作流中的businessName yanjian 2012-01-05 15:57
			if (instanceId != null && !"".equals(instanceId)) {
				ProcessInstance processInstance = workflowService.getProcessInstanceById(instanceId);
				if(pkFieldValue == null || "".equals(pkFieldValue)){
					tableName = bussinessId.split(";")[0];
					pkFieldName = bussinessId.split(";")[1];
					pkFieldValue = bussinessId.split(";")[2];
				}
				Map map = docBackTrackingService.getSystemField(pkFieldName,
						pkFieldValue, tableName);
				String businessName = (String) map.get(BaseWorkflowManager.WORKFLOW_TITLE);
				processInstance.setBusinessName(businessName);
			}
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
				TwfBaseNodesetting nodeSetting = workflowService.getNodesettingByNodeId(nodeId);
				String isCountersign = nodeSetting.getNsIsCountersign();// “1”：是会签节点
				String countersignType = nodeSetting.getCounterType();// 会签类型，“0”：串行；“1”：并行
				if ("1".equals(isCountersign) && "0".equals(countersignType)) {// 串行会签
					logger.info("串行会签,按排序号倒序排列...");
					final Map<String, User> userMap = new HashMap<String, User>();
					List<User> userLst = userService.getAllUserInfoByHa();
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

	public String getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}

	public String getReturnFlag() {
		return returnFlag;
	}

	public void setReturnFlag(String returnFlag) {
		this.returnFlag = returnFlag;
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

	public String getConcurrentTrans() {
		return concurrentTrans;
	}

	public void setConcurrentTrans(String concurrentTrans) {
		this.concurrentTrans = concurrentTrans;
	}

	public String getCASignInfo() {
		return CASignInfo;
	}

	public void setCASignInfo(String signInfo) {
		CASignInfo = signInfo;
	}

	public String getCurActorId() {
		return curActorId;
	}

	public void setCurActorId(String curActorId) {
		this.curActorId = curActorId;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public TwfInfoApproveinfo getApproveinfo() {
		return approveinfo;
	}

	public void setApproveinfo(TwfInfoApproveinfo approveinfo) {
		this.approveinfo = approveinfo;
	}

}
