package com.strongit.oa.wap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.ToaWorkForm;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.common.workflow.parameter.BackSpaceParameter;
import com.strongit.oa.senddoc.manager.SendDocAnnalManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.wap.manager.WorkForWapManager;
import com.strongit.oa.wap.util.AttachmentHelper;
import com.strongit.oa.wap.util.Data;
import com.strongit.oa.wap.util.Form;
import com.strongit.oa.wap.util.Item;
import com.strongit.oa.wap.util.Row;
import com.strongit.oa.wap.util.Status;
import com.strongit.oa.work.util.WorkFlowBean;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      工作处理Action
 */
@ParentPackage("default") 
public class WorkAction extends AbstractBaseWorkflowAction<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3413850552885861556L;

	@Autowired WorkForWapManager manager;
	@Autowired SendDocManager baseManager;
	@Autowired protected IEFormService eform;		//电子表单服务
	private Page<EForm> page = new Page<EForm>(20, false);//分页对象,每页20条,支持自动获取总记录数
	private Page<Object[]> flowPage = new Page<Object[]>(10, true);//分页对象,每页10条,支持自动获取总记录数
	private List<EForm> formList;
	private String worktype;
	protected String androidMessage;			//android新增变量
	@Autowired
	private WorkForWapManager workForWapManager;
	/*
	 * 查询工作流相关属性
	 */
    private String flow_id;//流程名称
	private String onlyone;//只有一个选择人员
	private String flow_status;//流程状态
	private String flow_query_type;//流程范围
	private String attchId;
	private String updateSql;		//更新语句
	private Date prcs_date1	;//流程开始时间
	private Date prcs_date2	;//流程结束时间
	private String run_name;//名称文号业务数据
	private String userId;//用户ID
	private String formName;//表单模板列表查询
	private String sql ;//查询表单里业务数据
	private String wapUserName;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IApprovalSuggestionService approvalSuggestion;
	private @Autowired SendDocAnnalManager sendDocAnnalManager; //注入办理记录接口
	@Autowired
	private SystemsetManager systemsetManager;
	@Autowired 
	private  IWorkflowAttachService workflowAttachManager;		//公文附件处理类
	
	private int currentPage;
	private String businessTitle;//
	private String message;//
	private List<String[]> userList=new ArrayList<String[]>();		//提交下一步时得到的人员列表（针对concurrentFlag等于3和4的情况）
	private String isNeedReturn;	//是否需要返回（工作指派）
	private String reAssignActorId;	//指派人员ID
	private String disLogo;			//用于区分是指派还是提交下一步
	private String transHtmls;		//迁移线
	private String usersHtmls;		//用户
	private String selectNodesInfo;	//选择了人员的节点信息，节点ID|节点名称|节点最大参与人数,节点ID|节点名称|节点最大参与人数
	private String tranIds;
	
	private String rtnContent;//当没有正文时候返回提示信息
	private final int TITLELENGTH = 13;//个人桌面右侧栏标题显示长度
	private String deptId;	//处室ID
	/*
	 * 所有流程类型
	 */
	private List<String> typeList=new ArrayList<String>();
	/**
	 *	流程类型下流程信息
	 *	@see {工作处理->高级查询}
	 *  @see {工作处理->高级查询结果列表展现}
	 */
	private List<WorkFlowBean> workFlowLst	;
	
	/**
	 * 流程定义文件主表单信息
	 */
	private List<EFormField> eformFieldLst = new ArrayList<EFormField>();
	
	/**
	 * 初始化工作查询页面
	 *@author 蒋国斌
	 *@date 2009-5-19 上午11:22:34 
	 * @return
	 * @throws Exception
	 */
	public String query()throws Exception{
	if(workflowType.equals("null")){
		//if(workflowType==null){
		  typeList=manager.getAllWorkflowTypeLst();
		}
		else {
			typeList=manager.getAllWorkflowTypeLst(workflowType);
		}
		this.getRequest().setAttribute("workflowType",workflowType);
		return "query";
	}

	/**
	 * 工作查询
	 * @author:邓志城
	 * @date:2009-12-11 下午07:00:41
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	public String querycontent()throws Exception{
    	flowPage=manager.getProcessInfoForPage(flowPage, flow_id, flow_status, flow_query_type, prcs_date1, prcs_date2, userId, run_name);
		return "querycontent";
	}

	/**
	 * 转到新建工作页面
	 */
	@Override
	public String input() throws Exception {
		returnFlag = manager.getOperationHtml(taskId,workflowName).getNsFormPrivInfo();
		User user = userService.getCurrentUser();//得到当前用户
		userName = user.getUserName();//当前用户姓名
		Organization organization = userService.getDepartmentByLoginName(user.getUserLoginname());//得到当前用户所属单位
		orgName = organization.getOrgName();//当前用户所属单位名称
		if(null!=taskId){
			String ret = manager.judgeTaskIsDone(taskId);
			if(!"f4".equals(ret)){//会签挂起
				return renderHtml("<Script language=\"JavaScript\">window.document.location='"+getRequest().getContextPath()+"/fileNameRedirectAction.action?toPage="+"/work/work-todomain.jsp';</Script>");
			}
			//签收
			manager.signForTask(taskId, "0");
			//通过任务ID获取表单ID和业务ID,业务ID用于获取公文对象
			String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
			Object[] objs = manager.getNodeWorkflowPluginInfoByTaskId(taskId);//得到插件信息
			pluginInfo = objs[0].toString();
			bussinessId = info[0];
			formId = info[1];
			if("0".equals(bussinessId)){
				return "form";
			}
			String[] args = bussinessId.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			pkFieldValue = args[2];
		}
		return "form";			
	}
	
	/**
	 * 根据给定任务实例ID获取所在流程实例的所有处理意见
	 * @author:邓志城
	 * @date:2009-12-12 上午10:13:00
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String wapannallist() throws Exception{

		String processInstanceId = "";
		if(taskId != null && !taskId.equals("")){
			processInstanceId = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceIdByTiId(taskId);
		}
		if(instanceId !=null && !instanceId.equals("")){
			processInstanceId = instanceId;
		}
		
		List<Long> PIdList =  sendDocAnnalManager.getWholeProcessIdsByPID(processInstanceId);//流程实例ID列表
		List<List>  listAllAnnal =  sendDocAnnalManager.allAnnal(PIdList,taskId);
		/*ToaSystemset systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		if(systemset.getIsUseCASign()== null || systemset.getIsUseCASign().equals("")){
			systemset.setIsUseCASign("0");
		}*/
		workForWapManager.genAnnalHtmlOneForWap(listAllAnnal,  "0", processInstanceId, getRequest());//request.setAttribute("tableString", tableString);
		return "wapannal";
	}
	
	/*
	 * 
	 * Description:获取某任务的处理记录列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 26, 2010 4:43:24 PM
	 */
	public String androidDoRecordList()throws Exception{
		Data data=new Data();
		HttpServletRequest request = getRequest();
		StringBuffer androidTrans=new StringBuffer();	//手机开发
		try{
			super.annal();
			//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			DateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Row> rows=new ArrayList<Row>();
			for(int i=0;i<listAnnal.size();i++){		//处理记录
				Object[] obj= listAnnal.get(i);
				//Date date=(Date)obj[6];
				String te="";
				if(obj[5]!=null){
					te=obj[5].toString();
				}
				Row row=new Row();
				List<Item> items = new ArrayList<Item>();
				Item item=new Item();
				item.setType("taskName");
				String taskname="";
				if(obj[1]!=null){
					taskname=obj[1].toString();
				}
				item.setValue(taskname);
				items.add(item);
				
				item=new Item();
				item.setType("dealer");
				String dealer="";
				if(obj[4]!=null){
					dealer=obj[4].toString();
				}
				item.setValue(dealer);
				items.add(item);
				
				item=new Item();
				item.setType("dealcon");
				item.setValue(te);
				items.add(item);
				
				String theTime=obj[6].toString();
				if(theTime.indexOf(".")!=-1){
					theTime=theTime.substring(0,theTime.length()-2);
				}
				item=new Item();
				item.setType("dealdate");
				item.setValue(theTime);
				items.add(item);
				
				item=new Item();
				String dealid="";
				if(obj[0]!=null){
					dealid=obj[0].toString();
				}
				item.setType("dealid");
				item.setValue(dealid);
				items.add(item);
				
				row.setItems(items);
				rows.add(row);
				
				//sb.append(obj[1]+">"+obj[4]+">"+te+">"+sdf.format(date)+">"+obj[0]+",");	//任务名称>处理人>处理意见>处理时间>审批记录ID
			}	
			data.setRows(rows);
			Status status=new Status("0","success");
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
			
		}catch(Exception e){
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}
		return "androidform";
	}
	
	public String androidfieldinfo()throws Exception{
		Data data=new Data();
		HttpServletRequest request = getRequest();
		try{
			
			List<EFormComponent> list=new ArrayList<EFormComponent>();
			list=manager.getFieldInfo(taskId, true);
			List<Row> rows=new ArrayList<Row>();
			if(list!=null){
				int i=0;
				for(EFormComponent bo:list){
					//排序号,Lable名,控件名称(数据库字段名),表名，控件类型，控件值，控件字典项，是否显示，是否只读
					if(bo.isVisible()){
						Row row=new Row();
						List<Item> items = new ArrayList<Item>();
						Item item=new Item();
						item.setType("orderno");
						item.setValue(String.valueOf(i));
						items.add(item);
						
						item=new Item();
						item.setType("label");
						item.setValue(bo.getLable());
						items.add(item);
						
						item=new Item();
						item.setType("fieldname");
						item.setValue(bo.getFieldName());
						items.add(item);
						
						item=new Item();
						item.setType("tablename");
						item.setValue(bo.getTableName());
						items.add(item);
						
						item=new Item();
						item.setType("fieldtype");
						item.setValue(bo.getType());
						items.add(item);
						
						item=new Item();
						item.setType("fieldvalue");
						item.setValue(bo.getValue());
						items.add(item);
						
						item=new Item();
						item.setType("fielditems");
						item.setValue(bo.getItems());
						items.add(item);
						
						item=new Item();
						item.setType("isvisible");
						item.setValue(String.valueOf(bo.isVisible()));
						items.add(item);
						
						item=new Item();
						item.setType("isreadonly");
						item.setValue(String.valueOf(bo.isReadonly()));
						items.add(item);
						
						item=new Item();
						item.setType("isrequired");
						item.setValue(String.valueOf(bo.isRequired()));
						items.add(item);
						row.setItems(items);
						rows.add(row);
					}
					i=i+1;
					
					/*if(i==0){
						sb.append("<dataitem>"+i+"#"+bo.getLable()+"#"+bo.getFieldName()+"#"+bo.getTableName()+"#"+bo.getType()+"#"+bo.getValue()+"#"+bo.getItems()+"#"+bo.isVisible()+"#"+bo.isReadonly()+"#"+bo.isRequired()+"</dataitem>");
					}else{
						if(bo.isVisible()){
						sb.append("<dataitem>"+i+"#"+bo.getLable()+"#"+bo.getFieldName()+"#"+bo.getTableName()+"#"+bo.getType()+"#"+bo.getValue()+"#"+bo.getItems()+"#"+bo.isVisible()+"#"+bo.isReadonly()+"#"+bo.isRequired()+"</dataitem>");
						}
					}*/
				}
			}
			data.setRows(rows);
			Status status=new Status("0","success");
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
			
		}catch(Exception e){
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}
		return "androidform";
	}
	
	
	/*
	 * 
	 * Description:获取附件列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 2, 2010 11:05:22 AM
	 */
	public String androidGetAttach() throws Exception{
		if(taskId != null && !"".equals(taskId)){
			HttpServletRequest request=this.getRequest();
			String path=request.getSession().getServletContext().getRealPath("/")+"doc"+File.separator;	//工程所在磁盘路径
			String[] sinfo= getManager().getFormIdAndBussinessIdByTaskId(taskId);
			String buss=sinfo[0];
			String[] infos=buss.split(";");
			String pkFieldValue="";
			if(infos[2]!=null&&!"".equals(infos[2].toString())){
				pkFieldValue=infos[2].toString();
			}
			
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(pkFieldValue); 
			//Properties formProperties =new Properties();
			//formProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("form.properties"));
			//String urlRoot=formProperties.getProperty("attach.service.url");
			
			
			String attachStr="";
			for(WorkflowAttach attach:workflowAttachs){
				String attachPath="work/mobile!viewForm.action?attachId="+attach.getDocattachid()+"&taskId="+taskId;
				attachStr+=","+attach.getDocattachid()+">"+attach.getAttachName()+">"+attachPath;
			}
			if(!"".equals(attachStr)){
				attachStr=attachStr.substring(1);
			}
			/*String isExistOffice=manager.getDocContent(path,sinfo);
			if(!"".equals(isExistOffice)){
				attachStr=infos[2]+">草稿.doc,"+attachStr;
			}*/
			ActionContext.getContext().put("androidsearch",attachStr);	
		}else{
			ActionContext.getContext().put("androidsearch", "");
		}
		return "android";
	}
	
	/*
	 * 
	 * Description:获取所有待办工作列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 26, 2010 10:21:28 AM
	 */
	public String androidWorkList()throws Exception{
		currentPage=currentPage==0?1:currentPage;
		pageWorkflow.setPageSize(7);
		pageWorkflow.setPageNo(currentPage);
	/*//	Page pageTodo=new Page(1000000,true);
		pageWorkflow=manager.getAllTodoWorks(pageWorkflow,null, businessName, userName, startDate, endDate,isBackSpace);
	//	todo();
		String todoWorks="";
		StringBuffer sb = new StringBuffer("");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
		List<Object[]> workList=pageWorkflow.getResult();		//获取待办工作列表
		if(workList==null||workList.size()==0){
			currentPage=pageWorkflow.getTotalPages();
			pageWorkflow.setPageNo(currentPage);
			pageWorkflow=manager.getAllTodoWorks(pageWorkflow,null, businessName, userName, startDate, endDate,isBackSpace);
			workList=pageWorkflow.getResult();		//获取待办工作列表
		}*/
		String todoWorks="";
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer("");
		List<Object[]> workList=new ArrayList<Object[]>();
		Page pageTodo=new Page(1000,true);
		String queryTitle=businessTitle;
		if(queryTitle!=null&&!"".equals(queryTitle)){
			queryTitle="%"+businessTitle+"%";
		}
		if("1".equals(worktype)){
			String workflowtype="2,3";//只查找收发文
			pageTodo=manager.getAllTodoWorks(pageTodo,workflowtype, queryTitle, userName, startDate, endDate,isBackSpace);
			if(pageTodo.getTotalCount()>0){
				workList=pageTodo.getResult();
			}
		}else{
			pageTodo=manager.getAllTodoWorks(pageTodo,null, queryTitle, userName, startDate, endDate,isBackSpace);
			if(pageTodo.getTotalCount()>0){
				List<Object[]> newList=pageTodo.getResult();
				if(newList!=null){
					for(int i=0;i<newList.size();i++){
						Object[] objs=newList.get(i);
						Object obj=objs[8];
						if(obj!=null){
							if(!"2".equals(obj.toString())&&!"3".equals(obj.toString())){
								workList.add(objs);
							}
						}
					}
				}
			}
		}
		if(workList!=null){
			pageWorkflow=ListUtils.splitList2Page(pageWorkflow, workList);
			
		}
		List<Object[]> newList=pageWorkflow.getResult();
		if(newList!=null && !newList.isEmpty()){				//待办工作不为空
			List<Object[]> processTypeList=manager.getAllProcessTypeLists();	//获取所有流程类型
			Map<String,String> typeMap = new HashMap<String, String>();		
			for(Object[] types : processTypeList){							//将流程类型放入Map中
				typeMap.put(types[0].toString(), types[1].toString());
			}
			for(Object[] obj : newList){
				String typeName="";
				if(obj[8]!=null)
				 typeName= typeMap.get(obj[8].toString());			//根据流程类型ID从Map中获取对应的流程名称
				Date date=(Date)obj[1];		
				String taskType="";
				if("1".equals(obj[9])){			//任务类型
					taskType="退回";
	    		}else if("0".equals(obj[10])){
	    			taskType="委托";
	    		}else if("1".equals(obj[10])){
	    			taskType="指派";
	    		}else{
	    			taskType="普通";
	    		}
				String[] info = getManager().getFormIdAndBussinessIdByTaskId(obj[0].toString());
				String dealer="   ";
				if(obj[7]!=null){
					dealer=obj[7].toString();
				}
				//任务ID>标题>接收时间>任务类型>流程类型ID>流程类型名称>流程实例ID>表单ID
				sb.append("<data>"+obj[0]+">"+obj[6]+">"+sdf1.format(date)+">"+taskType+">"+obj[8]+">"+typeName+">"+obj[3]+">"+info[1]+">"+dealer+"</data>"); 
			}
			todoWorks=sb.toString();	//去掉最后一个逗号
		}else{
			
		}
		if(!"".equals(todoWorks)){
			todoWorks+="<totalpages>"+pageWorkflow.getTotalPages()+"</totalpages>";
		}
		//Properties formProperties =new Properties();
		//formProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("form.properties"));
		//String urlRoot=formProperties.getProperty("attach.service.url");
		//if(!"".equals(urlRoot)){
			//todoWorks+="<urlroot>"+urlRoot+"</urlroot>";
		//}
		
		ActionContext.getContext().put("androidsearch", todoWorks);
		return "workandroid";
	}
	
	/*
	 * 
	 * Description:指派
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 26, 2010 11:26:41 PM
	 */
	public String androidReassign()throws Exception{
		String msg="";
		try{
			if(taskId == null || "".equals(taskId)){
				msg="ERROR此任务不存在或已删除，操作失败！";
			}else{
				String ret = getManager().checkCanReturn(taskId);	
				String[] flags = ret.split("\\|");
				if("1".equals(flags[2])){			//允许指派
					msg=getReassignUsers();
				}else if("0".equals(flags[2])){		//不允许指派
					msg="ERROR对不起，此任务不允许指派！";
				}else{
					msg="ERROR对不起，出现未知错误！请与管理员联系！";
				}
			}
		}catch(Exception e){
			msg="ERROR操作失败，请与管理员联系！";
			logger.error("ERROR验证任务是否允许指派的过程中出现异常！异常信息：" + e);
		}
		ActionContext.getContext().put("androidsearch", msg);
		return "android";
	}
	
	/*
	 * 
	 * Description:获取指派人员列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 27, 2010 10:11:24 AM
	 */
	public String getReassignUsers(){
		StringBuffer reAssignUsers=new StringBuffer("");
		List<String[]> 	userList=null;
		if(nodeId != null && !"".equals(nodeId) && !"null".equals(nodeId)){
			userList =getManager().getWorkflowTaskActors(nodeId, taskId,transitionId); 				//获取待指派的人员列表
			if(!"0".equals(nodeId)){
				Object[] setting = getManager().getNodesettingByNodeId(nodeId);
				maxTaskActors = (String)setting[0];
				isSelectOtherActors = (String)setting[1];
			}
		}
		if(userList!=null && !userList.isEmpty()){
			List<Organization> orgList = userService.getAllDeparments();				//获取所有机构列表
			Map<String,Organization> orgMap = new HashMap<String, Organization>();		//将机构信息放入Map中
			for(Organization organization : orgList){
				orgMap.put(organization.getOrgId(), organization);
			}
			int i=0;
			for(String[] info : userList){
				String orgName="";
				Organization org = orgMap.get(info[2]);		//根据机构ID从Map中获取机构名称
				if(org != null){
					orgName=org.getOrgName();
				}
				if("processToNext".equals(disLogo)){
					
				}else{
					reAssignUsers.append("u");	
				}
				i++;
				//if(i<10)
				reAssignUsers.append("<dataitem>"+info[0]+"#"+info[1]+"("+orgName+")"+"#"+info[2]+"</dataitem>");	//人员ID>人员名称>机构ID>机构名称
			}
		}
		String users=reAssignUsers.toString();
		if(!"".equals(users)){
			users+="<maxTaskActors>"+maxTaskActors+"</maxTaskActors><isSelectOtherActors>"+isSelectOtherActors+"</isSelectOtherActors>";
		}System.out.println(users);
		return users;
	}
	
	/*
	 * 
	 * Description:进行指派
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 27, 2010 9:43:01 PM
	 */
	public String reAssignUser()throws Exception{
		String msg="";
		reAssign();
		if("0".equals(androidMessage)||androidMessage==null){
			msg="任务指派成功！";
   		}else if("-1".equals(androidMessage)){
   			msg="任务实例不存在或已删除！";
   		}else if("-2".equals(androidMessage)){
   			msg="指派过程中出现异常！";
   		}else if("-3".equals(androidMessage)){
   			msg="参数传输错误！";
   		}
		ActionContext.getContext().put("androidsearch", msg);
		return "android";
	}
	
	/*
	 * 
	 * Description:获取流程下一步可选迁移线路径
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 29, 2010 8:08:20 PM
	 */
	public String androidGetNextTransitions()throws Exception{
		//this.getNextTransitionsAndroid();
		Data data=new Data();
		HttpServletRequest request = getRequest();
		//StringBuffer androidTrans=new StringBuffer();	//手机开发
		try{
			List<Object[]> list = null;
			if(workflowName !=null && !"undefined".equals(workflowName) && !"".equals(workflowName)){
				//选取开始流程时下一步可选步骤
				list = getManager().getStartWorkflowTransitions(workflowName);
			}
			if(taskId!=null && !"undefined".equals(taskId) && !"".equals(taskId)){
				//选取指定任务下一步可选步骤
				list = getManager().getNextTransitions(taskId);
			}
			if(list!=null&&list.size()>0){
				Collections.sort(list,new Comparator<Object[]>(){
					public int compare(Object[] obj1,Object[] obj2){
						Long lng1=Long.parseLong(obj1[1].toString());
						Long lng2=Long.parseLong(obj2[1].toString());
						return (lng1.intValue()-lng2.intValue());
					}
				});
			}
			List<Row> rows=new ArrayList<Row>();
			if (list != null && !list.isEmpty()) {
				for (Iterator i = list.iterator(); i.hasNext();) {
					Object[] trans = (Object[])i.next();
					Set<String> transInfo = (Set<String>)trans[3];
					String concurrentFlag = (String)trans[2];// 并发标示：“0”：非并发，“1”：并发
					String tranId = (String)ConvertUtils.convert(trans[1], String.class);// 迁移线id
					String tranName = (String)trans[0];// 迁移线名称
					String inputType = "radio";
					String nodeId = null;
					if("0".equals(concurrentFlag)){
						// 如果是子流程并且需要选择子流程第一个节点人员和子流程下一结点（父节点）人员
						if(String.valueOf(transInfo).indexOf("activeSet")!= String.valueOf(transInfo).lastIndexOf("activeSet")){
							concurrentFlag = "2";
						}
						// 非动态选择处理人(包括结束节点)应该隐藏树
						if((String.valueOf(transInfo).indexOf("notActiveSet")!=-1 || 
								String.valueOf(transInfo).indexOf("endNode")!=-1 || 
								String.valueOf(transInfo).indexOf("decideNode")!=-1 || 
								String.valueOf(transInfo).indexOf("subProcessNode")!=-1) && transInfo.size() == 1){
							concurrentFlag = "3";// add by dengzc
													// 2010年3月30日20:33:06
						}
					}else{
						inputType = "checkbox";
					}
					// String[] nodeInfo =
					// String.valueOf(transInfo).split("\\|");
					// String nodeId = (String)nodeInfo[1];//迁移线对应的节点id
					String[] nodeInfo = null;
					String tempNodeId = null;
					for(Iterator<String> it = transInfo.iterator();it.hasNext();){
						nodeInfo = String.valueOf(it.next()).split("\\|");
						String actorFlag = (String)nodeInfo[0];// 是否需要选择人员activeSet:需要重新选择人员
						nodeName = (String)nodeInfo[2];// 迁移线对应的节点名称
						if("activeSet".equals(actorFlag) || "subactiveSet".equals(actorFlag) || "supactiveSet".equals(actorFlag)){// 允许动态设置处理人
							nodeId = (String)nodeInfo[1];// 节点id
						}else{
							tempNodeId = (String)nodeInfo[1];// 节点id
						}
					}
					
					Row row=new Row();
					List<Item> items = new ArrayList<Item>();
					Item item=new Item();
					item.setType("tranId");
					item.setValue(tranId);
					items.add(item);
					
					item=new Item();
					item.setType("concurrentFlag");
					item.setValue(concurrentFlag);
					items.add(item);
					
					item=new Item();
					item.setType("tranName");
					item.setValue(tranName);
					items.add(item);
					
					if(String.valueOf(transInfo).indexOf("endNode")==1){
						if(nodeId == null){
							nodeId = tempNodeId;
						}
						item=new Item();
						item.setType("endNode");
						item.setValue("1");
						items.add(item);
						//androidTrans.append("$"+tranId+">"+concurrentFlag+">"+nodeId+">"+tranName+">1");
					}else{
						item=new Item();
						item.setType("endNode");
						item.setValue("0");
						items.add(item);
						//androidTrans.append("$"+tranId+">"+concurrentFlag+">"+nodeId+">"+tranName+">0");
					}
					
					item=new Item();
					item.setType("nodeId");
					item.setValue(nodeId);
					items.add(item);

					row.setItems(items);
					rows.add(row);
					
				}	
			}
			data.setRows(rows);
			Status status=new Status("0","success");
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
			
		}catch(Exception e){
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}
		return "androidform";
	}
	
	/**
	 * 获取流程下一步可选迁移线路径
	 * 通过$.getJSON方式读取,通过JSON格式传递到前台
	 * JSONObject html = new JSONObject();
	 * 1、正常情况下返回迁移线集合:html.put("transHtml",transHtml);
	 * 2、如不存在可选迁移线,返回“无迁移线的提示”:html.put("transHtml","无下一步可选步骤！");
	 * 3、出现异常情况,返回异常提示信息：html.put("transHtml",e.getMessage);
	 * @author:邓志城
	 * @date:2009-12-13 下午09:36:00
	 * @return html.toString()
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getNextTransitionsAndroid() throws Exception{
		StringBuffer androidTrans=new StringBuffer();	//手机开发
		StringBuffer androidUsers=new StringBuffer();	//手机开发
		JSONObject html = new JSONObject();//保存返回到前台的数据
		String EMPTY_STRING = "";//出现错误时,usersHtml传递空字符串
		try {
			List list = null;
			if(workflowName !=null && !"undefined".equals(workflowName) && !"".equals(workflowName)){
				//选取开始流程时下一步可选步骤
				list = getManager().getStartWorkflowTransitions(workflowName);
			}
			if(taskId!=null && !"undefined".equals(taskId) && !"".equals(taskId)){
				//选取指定任务下一步可选步骤
				list = getManager().getNextTransitions(taskId);
			}
			if (list != null && !list.isEmpty()) {
				String isChecked = "";
				if(list.size() == 1){
					isChecked = "checked";
				}
				StringBuffer transHtml = new StringBuffer();
				StringBuffer usersHtml = new StringBuffer();
				for (Iterator i = list.iterator(); i.hasNext();) {
					Object[] trans = (Object[])i.next();
					Set<String> transInfo = (Set<String>)trans[3];
					String concurrentFlag = (String)trans[2];//并发标示：“0”：非并发，“1”：并发
					String tranId = (String)ConvertUtils.convert(trans[1], String.class);//迁移线id
					String tranName = (String)trans[0];//迁移线名称
					String inputType = "radio";
					String nodeId = null;
					if("0".equals(concurrentFlag)){
						//如果是子流程并且需要选择子流程第一个节点人员和子流程下一结点（父节点）人员
						if(String.valueOf(transInfo).indexOf("activeSet")!= String.valueOf(transInfo).lastIndexOf("activeSet")){
							concurrentFlag = "2";
						}
						//非动态选择处理人(包括结束节点)应该隐藏树
						if((String.valueOf(transInfo).indexOf("notActiveSet")!=-1 || 
								String.valueOf(transInfo).indexOf("endNode")!=-1 || 
								String.valueOf(transInfo).indexOf("decideNode")!=-1 || 
								String.valueOf(transInfo).indexOf("subProcessNode")!=-1) && transInfo.size() == 1){
							concurrentFlag = "3";//add by dengzc 2010年3月30日20:33:06
						}
					}else{
						inputType = "checkbox";
					}
					//String[] nodeInfo = String.valueOf(transInfo).split("\\|");
					//String nodeId = (String)nodeInfo[1];//迁移线对应的节点id
					String[] nodeInfo = null;
					String tempNodeId = null;
					for(Iterator<String> it = transInfo.iterator();it.hasNext();){
						nodeInfo = String.valueOf(it.next()).split("\\|");
						String actorFlag = (String)nodeInfo[0];//是否需要选择人员activeSet:需要重新选择人员
						nodeName = (String)nodeInfo[2];//迁移线对应的节点名称
						if("activeSet".equals(actorFlag) || "subactiveSet".equals(actorFlag) || "supactiveSet".equals(actorFlag)){//允许动态设置处理人
							nodeId = (String)nodeInfo[1];//节点id
						}else{
							tempNodeId = (String)nodeInfo[1];//节点id
						}
					}
					if(String.valueOf(transInfo).indexOf("endNode")==1){
						if(nodeId == null){
							nodeId = tempNodeId;
						}
						transHtml.append("<input ").append(isChecked).append(" concurrentFlag=\""+concurrentFlag+"\" type=\""+inputType+"\" id=\""+tranId+"\" name=\"transition\" nodeid='" + nodeId + "' value=\""+tranName+"\" state='1' onclick='chooseNextStep("+tranId+","+concurrentFlag+","+nodeId+",\""+tranName+"\",1);'>");
						androidTrans.append("$"+tranId+">"+concurrentFlag+">"+nodeId+">"+tranName+">1");
					}else{
						transHtml.append("<input ").append(isChecked).append(" concurrentFlag=\""+concurrentFlag+"\" type=\""+inputType+"\" id=\""+tranId+"\" name=\"transition\" nodeid='" + nodeId + "' value=\""+tranName+"\" state='0' onclick='chooseNextStep("+tranId+","+concurrentFlag+","+nodeId+",\""+tranName+"\",0);'>");
						androidTrans.append("$"+tranId+">"+concurrentFlag+">"+nodeId+">"+tranName+">0");
					}
					
					transHtml.append(tranName);
					transHtml.append("<BR/>");
					usersHtml.append("<div id=\"nodeDiv_" + tranId + "\" style=\"display:none\">");
					for(Iterator<String> it = transInfo.iterator();it.hasNext();){
						nodeInfo = String.valueOf(it.next()).split("\\|");
						String actorFlag = (String)nodeInfo[0];//是否需要选择人员activeSet:需要重新选择人员
						nodeName = (String)nodeInfo[2];//迁移线对应的节点名称
						nodeId = (String)nodeInfo[1];//节点id
						
						if("activeSet".equals(actorFlag) || "subactiveSet".equals(actorFlag) || "supactiveSet".equals(actorFlag)){//允许动态设置处理人
							usersHtml.append(nodeName);
							usersHtml.append("处理人：<span id=\"users_");
							usersHtml.append(nodeId);
							usersHtml.append("\">&nbsp;</span> &nbsp;");
							usersHtml.append("<input type=\"button\" value=\"选择人员\" class=\"input_bg\" onclick=\"chooseActors(" + nodeId + ");\">");
							usersHtml.append("<input type='hidden' id='taskActor_" + nodeId + "'><input transId=\""+tranId+"\" type='hidden' id='strTaskActors_" + nodeId + "'><br><br>");
						}else {//不需要动态设置处理人
							//usersHtml.append("<input type=\"button\" value=\"选择人员\" class=\"input_bg\" disabled><br><br>");
						}
						
					}
					usersHtml.append("</div>");
				}	
				html.put("transHtml", transHtml.toString());
				html.put("usersHtml", usersHtml.toString());
				logger.info("usersHtml:"+usersHtml);
				logger.info("transHtml:"+transHtml.toString());
				androidMessage=androidTrans.toString().substring(1)+"#";
			}else{
				html.put("transHtml", "无下一步可选步骤！");
				html.put("usersHtml", EMPTY_STRING);
				androidMessage="EERRO无下一步可选步骤！"+"#"+EMPTY_STRING;
			}
		} catch (Exception e) {
			logger.error("出现异常，异常信息：",e);
			html.put("transHtml", "出现异常，异常信息：" + e.getMessage());
			html.put("usersHtml", EMPTY_STRING);
			androidMessage= "EERRO出现异常，异常信息：" + e.getMessage()+"#"+EMPTY_STRING;
		}
		return this.renderText(html.toString());
	}
	
	/*
	 * 
	 * Description:提交下步流程选人
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 29, 2010 10:10:15 PM
	 */
	public String androidChooseForWorkflow()throws Exception{
		ActionContext.getContext().put("androidsearch",this.getReassignUsers());
		return "android";
	}
	
	/*
	 * 
	 * Description:判断目前任务是否可被当前用户处理
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 27, 2010 11:04:37 PM
	 */
	public String androidJudgeTaskIsDone()throws Exception{
		String msg="";
		judgeTaskIsDone();
		if("-1".equals(androidMessage)){
			msg="此任务不存在或已删除！";
		}else if("-2".equals(androidMessage)){
			msg="操作异常！";
		}else{
			String[] infos=androidMessage.split("\\|");
			String ch = infos[0];
			if("f1".equals(ch)||"f2".equals(ch)||"f3".equals(ch)){
				msg=infos[1];
			}
		}	
		ActionContext.getContext().put("androidsearch", msg);
		return "android";
	}
	
	/**
	 * 查看表单及流程图
	 *@author 蒋国斌
	 *@date 2009-5-25 上午11:53:41 
	 * @return
	 * @throws Exception
	 */
	public String displayworkview()throws Exception{
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		//根据流程实例获取工作流表单挂接对象
		ToaWorkForm form = manager.getWorkFormByProcessInstanceId(instanceId);
		//获取展现表单
		formId = form==null?"0":String.valueOf(form.getVformId());
		if("0".equals(formId)){
			//throw new ServiceException(MessagesConst.ANY_ERROR,new Object[]{"流程未挂接展现表单！"});
		}
		if(bussinessId.indexOf(";")!=-1){
			String[] strBussinessId = bussinessId.split(";");
			tableName    = strBussinessId[0];
			pkFieldName  = strBussinessId[1];
			pkFieldValue = strBussinessId[2];
			
		}else{//收文模块特殊,未存储表名和主键名，只有主键值。表名和主键字段名是固定的
			tableName="T_OARECVDOC";
			pkFieldName="OARECVDOCID";
			pkFieldValue = bussinessId;
		}
		return "displayworkview";
	}
	
	/**
	 * author:dengzc
	 * description:进入待办工作页面,左侧展示流程类型树
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String workFlowTypeTree()throws Exception{
		List<Object[]> typelist = manager.getAllProcessTypeList();
		List type = new ArrayList();
		Page page = new Page(20,true);
		for(Iterator<Object[]> it=typelist.iterator();it.hasNext();){
			Object[] obj = it.next();
			TempPo po = new TempPo();
			String count = "0";
			po.setId(obj[0].toString());
			//获取某类型下的记录条数
			switch(Integer.valueOf(listMode).intValue()) {
			case HOSTED_BY_LIST:
				page = manager.getHostedByDocs(page, po.getId(),businessName,startDate,endDate,"");			//2010-10-14 郑志斌在getHostedByDocs方法中添加了一个查询状态参数  
				count = String.valueOf(page.getTotalCount());
				break;
			case TODO_LIST:
				page = manager.getTodoWorks(page, po.getId(),businessName,userName,startDate,endDate,isBackSpace);
				count = String.valueOf(page.getTotalCount());
				break;
			case PROCESSED_LIST:
				page = manager.getProcessedWorks(page, po.getId(),businessName,userName,startDate,endDate,processTimeout,"1","","");		//2010-10-14 郑志斌在getProcessedWorks方法中添加了一个查询状态参数 
				count = String.valueOf(page.getTotalCount());
				break;
			case DOING_LIST:
				page = manager.getDoingWorks(page, po.getId(),businessName,userName,startDate,endDate,isBackSpace);
				count = String.valueOf(page.getTotalCount());
				break;
			case ASSIGN_LIST:
				page = manager.getAssignWorks(page, po.getId(),businessName,userName,startDate,endDate);
				count = String.valueOf(page.getTotalCount());
				break;
			case ENTRUST_LIST:
				page = manager.getEntrustWorks(page, po.getId(),businessName,userName,startDate,endDate);
				count = String.valueOf(page.getTotalCount());
				break;
			default:
				page = manager.getTodoWorks(page, po.getId(),businessName,userName,startDate,endDate,isBackSpace);
				count = String.valueOf(page.getTotalCount());
			}
			po.setName(obj[1].toString()+"(<font color=\"blue\">"+count+"</font>)");
			po.setParentId("0");
			po.setType(listMode);//保存列表展示标示。（用于待办、在办在树展示时超链接转向）
			type.add(po);
		}
		getRequest().setAttribute("typeList", type);
		return "tree";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		String ret = "";
		if (null == listMode || "".equals(listMode)) { //进入新建工作页面,展示流程类型树
			
			List<Object[]> typelist = manager.getAllProcessTypeList();
			List type = new ArrayList();
			for(Iterator<Object[]> it=typelist.iterator();it.hasNext();){
				Object[] obj = it.next();
				TempPo po = new TempPo();
				po.setId(obj[0].toString());
				po.setName(obj[1].toString());
				po.setParentId("0");
				po.setType("form");//标示展示表单列表
				type.add(po);
			}
			getRequest().setAttribute("typeList", type);
			return "tree";
			
		}
		switch(Integer.valueOf(listMode).intValue()) {
			case DRAFT_LIST:
				//ret = draft();
				break;
			case HOSTED_BY_LIST:
				ret = hostedby();
				break;
			case 10:
				ret = wapTodo();
				break;
			case TODO_LIST:
				ret = todo();
				break;
			case PROCESSED_LIST:
				ret = processed();
				break;
			case DOING_LIST:
				ret = workinglist();
				break;
			case ASSIGN_LIST:
				ret = assignlist();
				break;
			case ENTRUST_LIST:
				ret = entrustlist();
				break;
			default:
				ret = SUCCESS;
		}
		
		return ret;
		
	}
	
	/***************************以下是wap版手机系统************************/
	
	public String wapTodo()throws Exception {
		InputStream is =null;
		HttpServletRequest request = getRequest();
		try{
			currentPage=currentPage==0?1:currentPage;
			pageWorkflow.setPageNo(currentPage);
			//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
			List<Object[]> workList=new ArrayList<Object[]>();
			Page temppage = new Page(1000, true);//工作流数据列表
			String queryTitle=businessTitle;
			if(wapUserName!=null){
				wapUserName=URLDecoder.decode(wapUserName,"utf-8");
			}
			if(businessTitle!=null&&!"".equals(businessTitle)){
				businessTitle=URLDecoder.decode(businessTitle,"utf-8");
				queryTitle="%"+businessTitle+"%";
			}
			if("1".equals(worktype)){
				String workflowtype="2,3";//只查找收发文
				temppage=manager.getAllTodoWorks(temppage,workflowtype, queryTitle, wapUserName, startDate, endDate,isBackSpace);
				List<Object[]> newList=temppage.getResult();
				if(newList!=null){
					for(int i=0;i<newList.size();i++){
						Object[] objs=newList.get(i);
						SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String strtime1="";
						String strtime2="";
						if(objs[1]!=null){
							Date dt=(Date)objs[1];
							strtime1= sf.format(dt);
							objs[1]=strtime1;
						}
						if(objs[4]!=null){
							Date dt2=(Date)objs[4];
							strtime2=sf.format(dt2);
							objs[4]=strtime2;
						}
						workList.add(objs);
					}
				}
				workList=temppage.getResult();
			}else{
				temppage=manager.getAllTodoWorks(temppage,null, queryTitle, wapUserName, startDate, endDate,isBackSpace);
				List<Object[]> newList=temppage.getResult();
				if(newList!=null){
					for(int i=0;i<newList.size();i++){
						Object[] objs=newList.get(i);
						SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String strtime1="";
						String strtime2="";
						if(objs[1]!=null){
							Date dt=(Date)objs[1];
							strtime1= sf.format(dt);
							objs[1]=strtime1;
						}
						if(objs[4]!=null){
							Date dt2=(Date)objs[4];
							strtime2=sf.format(dt2);
							objs[4]=strtime2;
						}
						Object obj=objs[8];
						if(obj!=null){
							if(!"2".equals(obj.toString())&&!"3".equals(obj.toString())){
								workList.add(objs);
							}
						}
					}
				}
			}
			//获得该待办事宜是否有正文
			if(workList!=null && !workList.isEmpty()){				//待办工作不为空
				for(Object[] obj : workList){
					
					String strNodeInfo = adapterBaseWorkflowManager.getWorkflowService().getNodeInfo(obj[0].toString());
					String[] tmparrNodeInfo = strNodeInfo.split(",");
					String tmpbussinessId = tmparrNodeInfo[2];			//业务数据id
					String tmpformId = tmparrNodeInfo[3];					//表单模板id
					List<EFormField> fieldList = eform.getFormTemplateFieldList(tmpformId);
					EFormField eformField = null;
					for(EFormField field : fieldList) {
						if("Office".equals(field.getType())) {//找到OFFICE控件类型
							eformField = field;
							break;
						}
					}
					if(eformField == null) {
						obj[10]="0";//0表示没有正文 1表示有正文
						
					}else{
						//当有正文时返回正文的路径,直接response.write流也可以，只是需要返回jsp，并且jsp包含wml标示
						String root = request.getContextPath();
						String[] args = tmpbussinessId.split(";");
						String tableName = args[0];
						String pkFieldName = args[1];
						String pkFieldValue = args[2];	
						is = (InputStream)baseManager.getFieldValue(eformField.getFieldname(),tableName, pkFieldName, pkFieldValue);
						InputStream tmpis = (InputStream)baseManager.getFieldValue(eformField.getFieldname(),tableName, pkFieldName, pkFieldValue);
						
						byte[] arrhead=new byte[10];
						tmpis.read(arrhead,0,arrhead.length);
						
						String fileHead=manager.bytesToHexString(arrhead);
					    String filetype= manager.getFileTypeByHeadInfo(fileHead);
					    String fileName="正文.doc";
					    if(filetype!=null&&!"".equals(filetype)){
					    	fileName="正文."+filetype;
					    }
						//String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
						if(is==null){
							obj[10]="";
							continue;
						}
						String path = AttachmentHelper.saveFile(root, is, fileName);
						obj[10]=path;
					}
				}
				pageWorkflow=ListUtils.splitList2Page(pageWorkflow, workList);
			}else{
				pageWorkflow.setTotalCount(0);
			}
		}catch(Exception e){
			logger.error("work读取附件数据发生异常", e);
		}finally{
			try {
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		//pageWorkflow.setPageSize(6);
		/*if(workList!=null && !workList.isEmpty()){				//待办工作不为空
			List<Object[]> processTypeList=manager.getAllProcessTypeLists();	//获取所有流程类型
			Map<String,String> typeMap = new HashMap<String, String>();		
			for(Object[] types : processTypeList){							//将流程类型放入Map中
				typeMap.put(types[0].toString(), types[1].toString());
			}
			for(Object[] obj : workList){
				String typeName="";
				if(obj[8]!=null)
				 typeName= typeMap.get(obj[8].toString());			//根据流程类型ID从Map中获取对应的流程名称
				Date date=(Date)obj[1];		
				String taskType="";
				if("1".equals(obj[9])){			//任务类型
					taskType="退回";
	    		}else if("0".equals(obj[10])){
	    			taskType="委托";
	    		}else if("1".equals(obj[10])){
	    			taskType="指派";
	    		}else{
	    			taskType="普通";
	    		}
				obj[9]=taskType;
				obj[1]=sdf.format(date);
				obj[8]=typeName;
			}
		}*/
		return "todo";
	}
	
	
	/*
	 * 
	 * Description:选择指派人员
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 8, 2010 5:02:10 PM
	 */
	public String wapChooseRP()throws Exception{
		try{
			if(taskId == null || "".equals(taskId)){
				message="此任务不存在或已删除，操作失败！";
			}else{
				String ret = getManager().checkCanReturn(taskId);	
				String[] flags = ret.split("\\|");
				if("1".equals(flags[2])){			//允许指派
					userList=manager.getWapReassignUsers(nodeId, taskId, null,transitionId);	//获取指派人员列表
				}else if("0".equals(flags[2])){		//不允许指派
					message="对不起，此任务不允许指派！";
				}else{
					message="对不起，出现未知错误！请与管理员联系！";
				}
			}
		}catch(Exception e){
			message="操作失败，请与管理员联系！";
			logger.error("验证任务是否允许指派的过程中出现异常！异常信息：" + e);
		}
		return "reassign";
	}
	
	
	/*
	 * 
	 * Description:指派
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 9, 2010 10:54:08 AM
	 */
	public String wapReAssignUser()throws Exception{
		if(isNeedReturn==null||"".equals(isNeedReturn)){
			isNeedReturn="0";
		}
		suggestion="{users:'"+reAssignActorId+","+isNeedReturn+"',remindType:''}";
		reAssign();
		if("0".equals(androidMessage)||androidMessage==null){
			message="任务指派成功！";
   		}else if("-1".equals(androidMessage)){
   			message="任务实例不存在或已删除！";
   		}else if("-2".equals(androidMessage)){
   			message="指派过程中出现异常！";
   		}else if("-3".equals(androidMessage)){
   			message="参数传输错误！";
   		}else{
   			message="任务指派失败！";
   		}
		return "reassign";
	}
	
	/**
	 * 获取流程办理中的常用意见
	 * @author:hecj
	 * @date 2012-4-27 10:20
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public String getSuggestionList(){
		HttpServletRequest request = getRequest();
		Data data=new Data();
		try{
			String curUserId=userService.getCurrentUser().getUserId();
			Page<ToaApprovalSuggestion> suggestionPage=new Page<ToaApprovalSuggestion>(100,true);
			ToaApprovalSuggestion sugBo=new ToaApprovalSuggestion(); 
			suggestionPage=approvalSuggestion.getAppSuggestionPage(suggestionPage, curUserId, null, null, sugBo, new OALogInfo("分页列表搜索"));
			List<ToaApprovalSuggestion> approList=suggestionPage.getResult();
			List<Item> items=new ArrayList<Item>();
			Form form=new Form();
			if(approList!=null){
				int i=0;
				for(ToaApprovalSuggestion bo:approList){
					Item it=new Item();
					it.setType(bo.getSuggestionSeq().toString());
					it.setValue(bo.getSuggestionContent());
					items.add(it);
				}
			}
			form.setItems(items);
			data.setForm(form);
			Status status=new Status("0","success");
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonFormData(data));
			
		}catch(Exception e){
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result",Data.GenerateJsonFormData(data));
		}
		return "androidform";
	}
	
	/*
	 * 
	 * Description:查看表单
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2010 9:34:05 AM
	 */
	public String wapViewForm() throws Exception {
		if(null!=taskId){
			if (!manager.isTaskInCurrentUser(taskId)) {
				message="该任务已不在您的待办事宜列表中";
			}
			String ret = manager.judgeTaskIsDone(taskId);
			String[] infos=ret.split("\\|");
			String ch = infos[0];
			if("f1".equals(ch)||"f2".equals(ch)||"f3".equals(ch)){
				message=infos[1];
			}else{
				if("view".equals(disLogo)){		//查看表单
				}else{							//办理
					returnFlag = this.getWapOperationHtml(taskId);
				}
				String[] info= getManager().getFormIdAndBussinessIdByTaskId(taskId);
				formId=info[1];
				String bussinessId = info[0];
				if ("".equals(formId) || formId == null || "0".equals(formId)) {// 表单模板不存在,即流程未挂接表单,如栏目流程
					message="流程未挂接表单";
				}
				//transHtmls=manager.formInfoToHtml(taskId,bussinessId,formId);			//生成表单控件	
				
				
				//if("view".equals(disLogo)){
					transHtmls=manager.generateFormInfo(taskId,disLogo);			//生成表单控件	
				//}else{
				//	transHtmls=manager.generateFormInfo(taskId);			//生成表单控件	
				//}
				usersHtmls=manager.createAttachHtml(this.getRequest(), info,taskId);		//生成附件
			}
		}
		return "form";			
	}
	
	/**
	 * 获取wapoa表单正文
	 * @return
	 * @throws Exception
	 */
	public String getDocContent(){
		HttpServletRequest request = this.getRequest();
		HttpServletResponse response=this.getResponse();
		OutputStream outputStream=null;
		BufferedInputStream br=null;
		InputStream is =null;
		rtnContent="";
		try{
			if(taskId!=null&&!"".equals(taskId)){
				String root = request.getContextPath();
				String str=manager.getDocContent(taskId,request);
				if("-1".equals(str)){
					wapTodo();
					rtnContent="没有正文!";
					return "todo";
				}else{
					String[] para=str.split(">");
					String bussid=para[1];
					String eformfiled=para[0];
					String[] args = bussid.split(";");
					String tableName = args[0];
					String pkFieldName = args[1];
					String pkFieldValue = args[2];	
					is = (InputStream)baseManager.getFieldValue(eformfiled,tableName, pkFieldName, pkFieldValue);
					
					String fileName="正文.doc";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String now = sdf.format(new Date());// 当前时间
					int index = fileName.lastIndexOf(".");
					String ext = fileName.substring(index, fileName.length());
					Random random = new Random();
					String randomNum = random.nextInt(9) + "" + random.nextInt(9) + ""
							+ random.nextInt(9) + "" + random.nextInt(9) + ""
							+ random.nextInt(9);
					String newFileName = now + randomNum + ext;// 通过当前时间和5位随机数参数新的文件名
					String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
					response.reset();
					response.setCharacterEncoding("utf-8");
					response.setContentType("application/x-msdownload");
					response.setHeader("Content-Type",fileType);
					response.addHeader("Content-Disposition", "attachment;filename=" +
						    new String(newFileName.getBytes("gb2312"),"iso8859-1"));
					outputStream = new BufferedOutputStream(response.getOutputStream());
					br = new BufferedInputStream(is);
					byte[] buf = new byte[1024];
					int len = 0;
					while((len = br.read(buf)) >0){
						outputStream.write(buf,0,len);
					}
					
					/*BASE64Encoder encoder = new BASE64Encoder();
					content = encoder.encode(FileUtil.inputstream2ByteArray(is));*/
					/*byte[] bt= FileUtil.inputstream2ByteArray(is);
			        FileOutputStream write = new FileOutputStream(new File("c:/test2.doc")); 
			        write.write(bt);*/
			        //生成临时文件，并返回临时文件路径
					/*String attName="正文.doc";
					String fileType=attName.substring(attName.indexOf(".")+1,attName.length());
					content = AttachmentHelper.saveFile(root, is, attName);
					request.setAttribute("result",str);*/
				}
			}
		}catch(Exception e){
			
		}finally{
			try {
				if(outputStream!=null){
					outputStream.close();
				}
				if(br!=null){
					br.close();
				}
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取安卓表单正文
	 * @return
	 * @throws Exception
	 */
	public String androidGetDocContent(){
		HttpServletRequest request = this.getRequest();
		InputStream is =null;
		rtnContent="";
		try{
			if(taskId!=null&&!"".equals(taskId)){
				String root = request.getContextPath();
				String str=manager.getDocContent(taskId,request);
				if("-1".equals(str)){
					request.setAttribute("androidsearch","-1");
				}else{
					String[] para=str.split(">");
					String bussid=para[1];
					String eformfiled=para[0];
					String[] args = bussid.split(";");
					String tableName = args[0];
					String pkFieldName = args[1];
					String pkFieldValue = args[2];	
					is = (InputStream)baseManager.getFieldValue(eformfiled,tableName, pkFieldName, pkFieldValue);
					
					String fileName="正文.doc";
					String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
					String path = AttachmentHelper.saveFile(root, is, fileName);
					
					request.setAttribute("androidsearch",path);
					
					/*BASE64Encoder encoder = new BASE64Encoder();
					content = encoder.encode(FileUtil.inputstream2ByteArray(is));*/
					/*byte[] bt= FileUtil.inputstream2ByteArray(is);
			        FileOutputStream write = new FileOutputStream(new File("c:/test2.doc")); 
			        write.write(bt);*/
			        //生成临时文件，并返回临时文件路径
					/*String attName="正文.doc";
					String fileType=attName.substring(attName.indexOf(".")+1,attName.length());
					content = AttachmentHelper.saveFile(root, is, attName);
					request.setAttribute("result",str);*/
				}
			}
		}catch(Exception e){
			
		}finally{
			try {
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "android";
	}
	
	/*
	 * 
	 * Description:下载附件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 18, 2010 10:08:04 PM
	 */
	public String downloadAttach() throws Exception{
		
		manager.downloadAttachment(attchId, this.getRequest(), this.getResponse());
		return null;
	}
	
	/*
	 * 
	 * Description:获取操作按钮
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2010 11:46:08 AM
	 */
	public String getWapOperationHtml(String taskId,OALogInfo...infos){
		StringBuffer html = new StringBuffer();
		try{
			boolean toNext=true;
			boolean toPre=true;
			if(StringUtils.hasLength(taskId)){
				String returnFlag = manager.checkCanReturn(taskId);//回退|驳回|指派|指派返回
				String[] flags = returnFlag.split("\\|");
				String	infomation = getManager().getInstro(taskId,"");
				if(!"".equals(infomation)&&infomation!=null&& !"[".equals(infomation.substring(0,1))){
					infomation="["+infomation+"]";
				}
				JSONArray jsonArray=new JSONArray();
				if(!"".equals(infomation)&&infomation!=null){
					jsonArray= JSONArray.fromObject(infomation);
				}
				for(int i=0;i<jsonArray.size();i++){
					JSONObject fbj = jsonArray.getJSONObject(i);
					if(fbj==null){
						continue;
					}
					JSONArray json=JSONArray.fromObject(fbj.get("otherField"));
					if(!json.toString().equals("[null]")){
					for(int j=0;j<json.size();j++){
						fbj=json.getJSONObject(j);
						if("toNext".equals(fbj.get("fieldName"))){
							if("none".equals(fbj.get("visible"))){
								toNext=false;
								continue;
							}
						}
						if("toPrev".equals(fbj.get("fieldName"))){
							if("none".equals(fbj.get("visible"))){	
								toPre=false;
								continue;
							}
						}
					}}
				}
				if("1".equals(flags[3])){	//允许指派返回
				}else{
					if(toNext){
						html.append("<input type='radio' name='disLogo' value='0' checked='checked'>提交下一办理人");
					}
				}
				if("1".equals(flags[0])&&toPre){ 	//允许退回
					if("".equals(html.toString())){
						html.append("<input type='radio' name='disLogo' value='1' checked='checked'>退回上一办理人");
					}else{
						html.append("<input type='radio' name='disLogo' value='1'>退回上一办理人");
					}
				}
			}
			logger.info("HTML************" + html);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 12, 2010 11:59:25 AM
	 */
	public String wapHandleNextStep()throws Exception{
		try{
			message=this.wapWalidateProcess();
			if("".equals(message)){
				/*if(updateSql!=null&&!"".equals(updateSql)){	//更新表单信息
					manager.updateFormInfo(updateSql);
				}*/
				String[] info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
				bussinessId = info[0];
				/*VoFormDataBean bean = null;
				String[] info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
				bussinessId = info[0];
				if (bussinessId == null || "".equals(bussinessId)) {// 新建表单数据-->提交工作流
					if (formData != null) {
						bean = manager.saveFormData(formData, workflowName, "1",
								businessName);// super.saveForm(formData);
						bussinessId = bean.getBusinessId();
					}

				} else {// 草稿箱中提交工作流
					manager.update(bussinessId, workflowName, "1", businessName);
				}*/
				//manager.update(bussinessId, workflowName, "1", businessName);
				androidHandleNextStep();
				disLogo="success";
				message="提交下一步办理人成功！";
			}
		}catch(Exception e){
			message="提交下一步办理人失败！";
			if(e.getMessage().indexOf("未获取到表单数据")!=-1){
				message="该系统无法处理归档流程！";
			}
			e.printStackTrace();
		}
		return "nextstep";
	}
	
	/*
	 * 
	 * Description:办理操作按钮权限控制
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 26, 2010 11:26:41 PM
	 */
	public String androidOperatePrivil()throws Exception{
		String result="";
		boolean toNext=true;
		boolean toPrev=true;
		String ret=getManager().checkCanReturn(taskId);
		String[] flags = ret.split("\\|");
		if(flags==null||flags.length==0){
			ActionContext.getContext().put("androidsearch",result);
			return "android";
		}
		String	infomation = getManager().getInstro(taskId,"");
		if(infomation!=null&&!"null".equals(infomation)){
			if(!"[".equals(infomation.substring(0,1))){
				infomation="["+infomation+"]";
			}
		}else{
			infomation="";
		}
		JSONArray jsonArray=new JSONArray();
		if(infomation!=null&&!"null".equals(infomation)&&!"".equals(infomation)){
			jsonArray = JSONArray.fromObject(infomation);
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject fbj = jsonArray.getJSONObject(i);
			if(fbj==null){
				continue;
			}
			JSONArray json=JSONArray.fromObject(fbj.get("otherField"));
			if(!json.toString().equals("[null]")){
			for(int j=0;j<json.size();j++){
				fbj=json.getJSONObject(j);
				String visible=null;
				if (json != null) {
					visible = (String) fbj.get("visible");
				} else {
					visible = "block";
				}
				if("toNext".equals(fbj.get("fieldName"))){
					//if("none".equals(fbj.get("visible"))){
					if(visible!=null&&visible.toLowerCase().equals("block")){
					}else{
						toNext=false;
						continue;
					}
				}
				if("toPrev".equals(fbj.get("fieldName"))){
					/*if("none".equals(fbj.get("visible"))){
						toPrev=false;
						continue;
					}*/
					if(visible!=null&&visible.toLowerCase().equals("block")){
					}else{
						toPrev=false;
						continue;
					}
				}
			}}
		}
		if("1".equals(flags[3])){	//允许指派返回
		}else{
			if(!toNext){
				flags[3]="1";
			}
		}
		if("1".equals(flags[0])&&toPrev){
			flags[0]="1";
		}else{
			flags[0]="0";
		}
		
		for(String flag:flags){
			result+="|"+flag;
		}
		result=result.substring(1);
		ActionContext.getContext().put("androidsearch",result);
		return "android";
	}
	
	
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 28, 2010 9:54:14 PM
	 */
	public String androidBacklast()throws Exception{
		String ret = "退回上一办理人成功！";
		try{
			if(StringUtils.hasLength(taskId)){
				//getManager().backSpaceLast(taskId, formId, suggestion, "",formData,new OALogInfo("任务退回上一办理人，taskId="+taskId));
				BackSpaceParameter para=new BackSpaceParameter();
				para.setTaskId(taskId);
				String curUserId=userService.getCurrentUser().getUserId();
				para.setCurUserId(curUserId);
				para.setFormData(formData);
				para.setFormId(formId);
				JSONObject cont=new JSONObject();
				cont.put("suggestion",suggestion);
				cont.put("CAInfo", "");
				para.setSuggestion(cont.toString());
				getManager().backSpaceLast(para,new OALogInfo("任务退回上一办理人，taskId="+taskId));
			}else{
				ret = "任务实例不存在或已删除！";
			}
		}catch(Exception ex){
			logger.error("退回上一办理人时出现异常,异常信息：" + ex.getMessage());
			ret = "任务退回过程中出现异常！";
		}
		ActionContext.getContext().put("androidsearch",ret);
		return "android";
	}
	
	/*
	 * 
	 * Description:提交下一处理人
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Sep 30, 2010 5:43:54 PM
	 */
	public String androidHandleNextStep()throws Exception{
		String isNewForm = "";
		if(taskId == null || "".equals(taskId)){
			isNewForm = "1";
		}else{
			String[] info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
			bussinessId = info[0];
			if ("0".equals(bussinessId)) {
				isNewForm = "1";
			} else {
				isNewForm = "0";
			}						
		}
		
		if(suggestion!=null){
			suggestion = suggestion.replaceAll("\\r\\n", "");//处理审批意见有回车换行的情况	
		}
		JSONObject json = new JSONObject();
		json.put("suggestion", suggestion);
		json.put("CAInfo", "");
		if(businessName == null || "".equals(businessName)){
			businessName = workflowName;
		}
		String curUserId=userService.getCurrentUser().getUserId();
		OALogInfo logInfo = new OALogInfo(getText(GlobalBaseData.WORKFLOW_HANDLERWORKFLOWNEXT, 
				new String[]{userService.getCurrentUser().getUserName(),businessName}));
		 //OtherParameter otherparameter = initOtherParameter();
		OtherParameter otherparameter = null;
		if (strTaskActors==null||"".equals(strTaskActors)||"null".equals(strTaskActors)) {
			getManager().handleWorkflowNextStep(taskId,transitionName,"",isNewForm,formId,bussinessId,json.toString(),curUserId,null,formData,otherparameter,logInfo);
		} else {
			if(strTaskActors.indexOf(" ")!=-1){			//新增代码
				String[] taskActors=strTaskActors.split(",");
				String temp="";
				for(String taskActor:taskActors){
					temp+=","+taskActor.trim();
				}
				if(!"".equals(temp)){
					temp=temp.substring(1);
					strTaskActors=temp;
				}
			}
			
			getManager().handleWorkflowNextStep(taskId,transitionName,"",isNewForm,formId,bussinessId,json.toString(),curUserId,strTaskActors.split(","),formData,otherparameter,logInfo);
			
		}	
		if(sql!=null)
		manager.execuSQL(sql);
		ActionContext.getContext().put("androidsearch","");
		return "android";
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 12, 2010 11:59:46 AM
	 */
	public String wapWalidateProcess(){
		String msg="";
		if(tranIds==null||"".equals(tranIds)){
			msg="请选择下一步骤！";
		}
		if(selectNodesInfo!=null&&!"".equals(selectNodesInfo)){		//动态选人
			if(strTaskActors==null||"".equals(strTaskActors)){		//如果没有选择处理人
				msg="请选择下一步处理人！";
				return msg;
			}
			String[] nodesInfo=selectNodesInfo.split(",");			//下一办理人节点ID|下一办理人节点名称|每个节点最大参与人数
			String[] userAndNodes=strTaskActors.split(",");			//用户|节点，用户|节点
			int[] selectUserNum=new int[nodesInfo.length];			//每个节点选择的办理人数
			for(int i=0;i<nodesInfo.length;i++){					//统计每个节点选择的人员数
				String nodeInfo=nodesInfo[i];
				String[] nextNodeInfo=nodeInfo.split("\\|");
				for(String userAndNode:userAndNodes){
					String[] arr=userAndNode.split("\\|");
					if(nextNodeInfo[0].equals(arr[1])){
						selectUserNum[i]+=1;
					}
				}
			}
			for(int i=0;i<nodesInfo.length;i++){					//循环节点信息，判断是否超出最大参与人数
				String nodeInfo=nodesInfo[i];
				String[] nextNodeInfo=nodeInfo.split("\\|");
				if(selectUserNum[i]==0){
					msg="请选择"+nextNodeInfo[1]+"处理人！";
					return msg;
				}
				String maxActorNum=nextNodeInfo[2];
				if(maxActorNum!=null&&!"".equals(maxActorNum)&&!"null".equals(maxActorNum)
						&&selectUserNum[i]>Integer.parseInt(maxActorNum)){
					msg=nextNodeInfo[1]+"处理人允许最大参与人数为"+maxActorNum+"，您选择了"+selectUserNum[i]+"个人！";
					return msg;
				}
			}
		}
		return msg;
	}
	
	/*
	 * 
	 * Description:退回上一办理人
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2010 5:02:07 PM
	 */
	public String wapBacklast() throws Exception{
		suggestion=suggestion.replace("\n", "").trim();
		suggestion=suggestion.replace("\r", "").trim();
		suggestion="{suggestion:'"+suggestion+"',remindType:''}";
		if(updateSql!=null&&!"".equals(updateSql)){	//更新表单信息
			//manager.updateFormInfo(updateSql);
		}
		backlast();
		//不应该是null
		if("0".equals(androidMessage)||androidMessage==null){
			message="退回上一办理人成功！";
		}else if("-1".equals(androidMessage)){
			message="任务实例不存在或已删除！";
		}else if("-4".equals(androidMessage)){
			message="保存表单数据失败！";
		}else{
			message="任务退回过程中出现异常！";
		}
		return "initback";
	}
	
	/*
	 * 
	 * Description:办理任务
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 17, 2010 2:28:20 PM
	 */
	public String handleTask()throws Exception{
		String temp=manager.walidateFormInfo(this.getRequest(), taskId);	//验证表单信息
		if(temp.startsWith("update")){		//验证成功
			updateSql=temp;
		}else{								//验证失败时展现提示信息
			message=temp;
			return "form";
		}
		if("1".equals(disLogo)){			//退回上一步处理人
			return "initback";
		}else{								//提交下一步办理人
			return getWapNextTransitions();
		}
	}
	
	/*
	 * 
	 * Description:获取迁移线
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 11, 2010 3:11:24 PM
	 */
	public String getWapNextTransitions() throws Exception{
		usersHtmls="";					
		selectNodesInfo="";
		transitionName="";				//迁移线
		StringBuffer androidTrans=new StringBuffer();	//手机开发
		String EMPTY_STRING = "";		//出现错误时,usersHtml传递空字符串
		try {
			List<Object[]> list = null;
			if(workflowName !=null && !"undefined".equals(workflowName) && !"".equals(workflowName)){
				//选取开始流程时下一步可选步骤
				list = getManager().getStartWorkflowTransitions(workflowName);
			}
			if(taskId!=null && !"undefined".equals(taskId) && !"".equals(taskId)){
				//选取指定任务下一步可选步骤
				list = getManager().getNextTransitions(taskId);
			}
			if(list!=null&&!list.isEmpty()){
				Collections.sort(list,new Comparator<Object[]>(){
					public int compare(Object[] obj1,Object[] obj2){
						Long lng1=Long.parseLong(obj1[1].toString());
						Long lng2=Long.parseLong(obj2[1].toString());
						return (lng1.intValue()-lng2.intValue());
					}
				});
			}
			if (list != null && !list.isEmpty()) {
				String endNodeState="";
				StringBuffer transHtml = new StringBuffer();
				for (Iterator i = list.iterator(); i.hasNext();) {
					String isChecked = "";
					Object[] trans = (Object[])i.next();
					Set<String> transInfo = (Set<String>)trans[3];
					String concurrentFlag = (String)trans[2];//并发标示：“0”：非并发，“1”：并发
					String tranId = (String)ConvertUtils.convert(trans[1], String.class);//迁移线id
					String tranName = (String)trans[0];//迁移线名称
					String inputType = "radio";
					String nodeId = null;
					if("0".equals(concurrentFlag)){
						//如果是子流程并且需要选择子流程第一个节点人员和子流程下一结点（父节点）人员
						if(String.valueOf(transInfo).indexOf("activeSet")!= String.valueOf(transInfo).lastIndexOf("activeSet")){
							concurrentFlag = "2";
						}
						//非动态选择处理人(包括结束节点)应该隐藏树
						if((String.valueOf(transInfo).indexOf("notActiveSet")!=-1 || 
								String.valueOf(transInfo).indexOf("endNode")!=-1 || 
								String.valueOf(transInfo).indexOf("decideNode")!=-1 || 
								String.valueOf(transInfo).indexOf("subProcessNode")!=-1) && transInfo.size() == 1){
							concurrentFlag = "3";//add by dengzc 2010年3月30日20:33:06
						}
					}else{
						inputType = "checkbox";
					}
					String[] nodeInfo = null;
					String tempNodeId = null;
					for(Iterator<String> it = transInfo.iterator();it.hasNext();){
						nodeInfo = String.valueOf(it.next()).split("\\|");
						String actorFlag = (String)nodeInfo[0];//是否需要选择人员activeSet:需要重新选择人员
						nodeName = (String)nodeInfo[2];//迁移线对应的节点名称
						if("activeSet".equals(actorFlag) || "subactiveSet".equals(actorFlag) || "supactiveSet".equals(actorFlag)){//允许动态设置处理人
							nodeId = (String)nodeInfo[1];//节点id
						}else{
							tempNodeId = (String)nodeInfo[1];//节点id
						}
					}
					if(list.size() == 1||transitionId==null||transitionId.indexOf(tranId)!=-1){		//wap开发添加代码
						if(transitionId==null){
							transitionId=tranId;
						}
						isChecked = "checked";
					}
					if(String.valueOf(transInfo).indexOf("endNode")==1){
						if(nodeId == null){
							nodeId = tempNodeId;
						}
						endNodeState="1";
					}else{
						endNodeState="0";
					}
					transHtml.append("<input ")
							.append(isChecked)
							.append(" type=\"")
							.append(inputType)
							.append("\" id=\"")
							.append(tranId)
							.append("\" name=\"transitionId\" value=\"")
							.append(tranId)
							.append("\">")
							.append(tranName)
							.append("<BR/>");
					androidTrans.append("$"+tranId+">"+concurrentFlag+">"+nodeId+">"+tranName+">"+endNodeState);
					if(list.size() == 1||transitionId.indexOf(tranId)!=-1){		//wap开发添加代码
						transitionName+=","+tranName;	
						usersHtmls+=this.setNextStepUserList(tranId,tranName,concurrentFlag, taskId, nodeId, nodeName, endNodeState,nodeInfo,transInfo);
					}
				}	
				transHtmls=transHtml.toString();
				androidMessage=androidTrans.toString().substring(1)+"#";
				logger.info("transHtmls:"+transHtmls);
				logger.info("usersHtmls:"+usersHtmls);
				if(!"".equals(selectNodesInfo)){						//需要动态选择人员的节点
					selectNodesInfo=selectNodesInfo.substring(1);
				}	
				if(!"".equals(transitionName)){							//选择的迁移线名称
					transitionName=transitionName.substring(1);	
				}
				
			}else{
				transHtmls="无下一步可选步骤！";
				usersHtmls=EMPTY_STRING;
				androidMessage="EERRO无下一步可选步骤！"+"#"+EMPTY_STRING;
			}
		} catch (Exception e) {
			logger.error("出现异常，异常信息：",e);
			transHtmls="出现异常，异常信息：" + e.getMessage();
			usersHtmls=EMPTY_STRING;
			androidMessage= "EERRO出现异常，异常信息：" + e.getMessage()+"#"+EMPTY_STRING;
		}
		return "nextstep";
	}
	/*
	 * 
	 * Description:获取迁移线
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 11, 2010 3:11:24 PM
	 */
	public String getWapNextTransitionsChosePerson() throws Exception{
		usersHtmls="";					
		selectNodesInfo="";
		transitionName="";				//迁移线
		StringBuffer androidTrans=new StringBuffer();	//手机开发
		String EMPTY_STRING = "";		//出现错误时,usersHtml传递空字符串
		try {
			List<Object[]> list = null;
			if(workflowName !=null && !"undefined".equals(workflowName) && !"".equals(workflowName)){
				//选取开始流程时下一步可选步骤
				list = getManager().getStartWorkflowTransitions(workflowName);
			}
			if(taskId!=null && !"undefined".equals(taskId) && !"".equals(taskId)){
				//选取指定任务下一步可选步骤
				list = getManager().getNextTransitions(taskId);
			}
			if(list!=null&&!list.isEmpty()){
				Collections.sort(list,new Comparator<Object[]>(){
					public int compare(Object[] obj1,Object[] obj2){
						Long lng1=Long.parseLong(obj1[1].toString());
						Long lng2=Long.parseLong(obj2[1].toString());
						return (lng1.intValue()-lng2.intValue());
					}
				});
			}
			if (list != null && !list.isEmpty()) {
				String endNodeState="";
				StringBuffer transHtml = new StringBuffer();
				for (Iterator i = list.iterator(); i.hasNext();) {
					String isChecked = "";
					Object[] trans = (Object[])i.next();
					Set<String> transInfo = (Set<String>)trans[3];
					String concurrentFlag = (String)trans[2];//并发标示：“0”：非并发，“1”：并发
					String tranId = (String)ConvertUtils.convert(trans[1], String.class);//迁移线id
					String tranName = (String)trans[0];//迁移线名称
					String inputType = "radio";
					String nodeId = null;
					if("0".equals(concurrentFlag)){
						//如果是子流程并且需要选择子流程第一个节点人员和子流程下一结点（父节点）人员
						if(String.valueOf(transInfo).indexOf("activeSet")!= String.valueOf(transInfo).lastIndexOf("activeSet")){
							concurrentFlag = "2";
						}
						//非动态选择处理人(包括结束节点)应该隐藏树
						if((String.valueOf(transInfo).indexOf("notActiveSet")!=-1 || 
								String.valueOf(transInfo).indexOf("endNode")!=-1 || 
								String.valueOf(transInfo).indexOf("decideNode")!=-1 || 
								String.valueOf(transInfo).indexOf("subProcessNode")!=-1) && transInfo.size() == 1){
							concurrentFlag = "3";//add by dengzc 2010年3月30日20:33:06
						}
					}else{
						inputType = "checkbox";
					}
					String[] nodeInfo = null;
					String tempNodeId = null;
					for(Iterator<String> it = transInfo.iterator();it.hasNext();){
						nodeInfo = String.valueOf(it.next()).split("\\|");
						String actorFlag = (String)nodeInfo[0];//是否需要选择人员activeSet:需要重新选择人员
						nodeName = (String)nodeInfo[2];//迁移线对应的节点名称
						if("activeSet".equals(actorFlag) || "subactiveSet".equals(actorFlag) || "supactiveSet".equals(actorFlag)){//允许动态设置处理人
							nodeId = (String)nodeInfo[1];//节点id
						}else{
							tempNodeId = (String)nodeInfo[1];//节点id
						}
					}
					if(list.size() == 1||transitionId==null||transitionId.indexOf(tranId)!=-1){		//wap开发添加代码
						if(transitionId==null){
							transitionId=tranId;
						}
						isChecked = "checked";
					}
					if(String.valueOf(transInfo).indexOf("endNode")==1){
						if(nodeId == null){
							nodeId = tempNodeId;
						}
						endNodeState="1";
					}else{
						endNodeState="0";
					}
					transHtml.append("<input ")
							.append(isChecked)
							.append(" disabled=\"disabled\" ")
							.append(" type=\"")
							.append(inputType)
							.append("\" id=\"")
							.append(tranId)
							.append("\" name=\"transitionId\" value=\"")
							.append(tranId)
							.append("\">")
							.append(tranName)
							.append("<BR/>");
					androidTrans.append("$"+tranId+">"+concurrentFlag+">"+nodeId+">"+tranName+">"+endNodeState);
					if(list.size() == 1||transitionId.indexOf(tranId)!=-1){		//wap开发添加代码
						transitionName+=","+tranName;	
						usersHtmls+=this.setNextStepUserList(tranId,tranName,concurrentFlag, taskId, nodeId, nodeName, endNodeState,nodeInfo,transInfo);
					}
				}	
				transHtmls=transHtml.toString();
				androidMessage=androidTrans.toString().substring(1)+"#";
				logger.info("transHtmls:"+transHtmls);
				logger.info("usersHtmls:"+usersHtmls);
				if(!"".equals(selectNodesInfo)){						//需要动态选择人员的节点
					selectNodesInfo=selectNodesInfo.substring(1);
				}	
				if(!"".equals(transitionName)){							//选择的迁移线名称
					transitionName=transitionName.substring(1);	
				}	
			}else{
				transHtmls="无下一步可选步骤！";
				usersHtmls=EMPTY_STRING;
				androidMessage="EERRO无下一步可选步骤！"+"#"+EMPTY_STRING;
			}
		} catch (Exception e) {
			logger.error("出现异常，异常信息：",e);
			transHtmls="出现异常，异常信息：" + e.getMessage();
			usersHtmls=EMPTY_STRING;
			androidMessage= "EERRO出现异常，异常信息：" + e.getMessage()+"#"+EMPTY_STRING;
		}
		return "choseperson";
	}
	
	/**
	 * 根据节点id得到节点信息
	 * 
	 * @author:邓志城
	 * @date:2010-10-9 下午01:46:32
	 */
	public String getNodeSettingInfoForWap() {
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
		//this.renderText(info.toString());
		return info.toString();
		//logger.error("当前节点设置信息：" + info.toString());
	}
	
	/*
	 * 
	 * Description:得到下一步处理人列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 11, 2010 4:29:15 PM
	 */
	public String setNextStepUserList(String transitionId,String tranName,String concurrentFlag,String taskId,String nodeId,
												String nodeName,String endNodeState,String[] nodeInfo,Set<String> transInfo){
		StringBuffer usersHtml = new StringBuffer();
		String selectUser= this.getNodeSettingInfoForWap();
		JSONObject json=JSONObject.fromObject(selectUser);
		String rtnUser=json.getString("returnTaskActor");
		if(!"".equals(selectUser)&&"1".equals(rtnUser)){
			
		}else{
			if("1".equals(concurrentFlag)||"2".equals(concurrentFlag)){
				for(Iterator<String> it = transInfo.iterator();it.hasNext();){
					nodeInfo = String.valueOf(it.next()).split("\\|");
					String actorFlag = (String)nodeInfo[0];			//是否需要选择人员activeSet:需要重新选择人员
					if("activeSet".equals(actorFlag)||"subactiveSet".equals(actorFlag)||"supactiveSet".equals(actorFlag)){	//允许动态设置处理人
						String nextNodeId = (String)nodeInfo[1];	//节点id
						nodeName = (String)nodeInfo[2];				//迁移线对应的节点名称
						Object[] setting = getManager().getNodesettingByNodeId(nextNodeId);
						String maxActors = (String)setting[0];		//最大参与人数
						List<String[]> userList=manager.getWapReassignUsers(nextNodeId, taskId, "processToNext",transitionId);	//获取下一步办理人列表
						usersHtml.append("<div class='sec'>")
							.append(nodeName)
							.append("处理人(")
							.append(maxActors)
							.append(")：")
							.append("</div>");
						for(String[] user:userList){
							usersHtml.append("<div class='sec'>")
								.append("<input type='checkbox'")
								.append(" name='strTaskActors' value='")
								.append(user[0])
								.append("'/>")
								.append(user[1])
								.append("</div>");
						}
						usersHtml.append("<br>");
						selectNodesInfo+=","+nextNodeId+"|"+nodeName+"|"+maxActors;
					}else {	
					}
				}
	        }else if("3".equals(concurrentFlag)){		//非动态选择处理人应该隐藏树
	
	        }else{
	    		if("0".equals(endNodeState)){
					if(taskId==null||"".equals(taskId))
						taskId="0";
					userList=manager.getWapReassignUsers(nodeId, taskId,"processToNext",transitionId);	//获取指派人员列表
					if(userList!=null&&userList.size()>0){
						Object[] setting = getManager().getNodesettingByNodeId(nodeId);
						selectNodesInfo+=","+nodeId+"|"+nodeName+"|"+(String)setting[0];
					}
				}
	        }
			if(userList.size()==1){
				onlyone="1";
			}else{
				onlyone="0";
			}
		}
		return usersHtml.toString();
	}
	
	
	/**
	 * author:dengzc
	 * description:异步加载工作列表
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String ajaxWorkList()throws Exception{
			pageWorkflow.setPageSize(6);//取3条数据
			String workflowType = "-1";//取非系统类型的工作
			String location = "#";//定义“更多”的链接
			
			String title = "";//定义导航标题
			switch(Integer.valueOf(listMode).intValue()) {
			case HOSTED_BY_LIST:
				pageWorkflow = manager.getHostedByDocs(pageWorkflow,workflowType,businessName,startDate,endDate,"");   //2010-10-14 郑志斌在getHostedByDocs方法中添加了一个查询状态参数
				location = "/fileNameRedirectAction.action?toPage=work/work-hostedbymain.jsp";
				title = "主办工作";
				break;
			case TODO_LIST:
				pageWorkflow = manager.getTodoWorks(pageWorkflow,workflowType,businessName,userName,startDate,endDate,isBackSpace);
				location = "/fileNameRedirectAction.action?toPage=work/work-todomain.jsp";
				title = "待办工作";
				break;
			case PROCESSED_LIST:
				pageWorkflow = manager.getProcessedWorks(pageWorkflow,workflowType,businessName,userName,startDate,endDate,processTimeout,"1","","");   //2010-10-14 郑志斌在getProcessedWorks方法中添加了一个查询状态参数
				location = "/fileNameRedirectAction.action?toPage=work/work-processedmain.jsp";
				title = "已办工作";
				break;
			case DOING_LIST:
				pageWorkflow = manager.getDoingWorks(pageWorkflow, workflowType,businessName,userName,startDate,endDate,isBackSpace);
				location = "/fileNameRedirectAction.action?toPage=work/work-doingmain.jsp";
				title = "在办工作";
				break;
			default:
				return null;
			}
			if(logger.isInfoEnabled()){
				logger.info("workInfo:"+getWorkInfo(pageWorkflow,location,listMode,title));
			}
			renderHtml(getWorkInfo(pageWorkflow,location,listMode,title));
			return null;
	}

	/**
	 * author:dengzc
	 * description:获取工作详细信息
	 * modifyer:
	 * description:
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public String getWorkInfo(Page page,String location,String listMode,String jtitle){
		String detailLocation = "/work/work!input.action?listMode="+listMode;//定义每条信息的链接
		StringBuffer sb = new StringBuffer("");
		String frameroot= (String) getRequest().getSession().getAttribute("frameroot"); 
		String path = getRequest().getContextPath();
		String fullPath = "\"javascript:window.parent.refreshWorkByTitle('"+path+location+"', '"+jtitle+"');\"";
		//String detailFullPath = "\"javascript:refreshWorkByTitle('"+path+detailLocation+"', '"+jtitle+"');\"";
		if(frameroot==null||frameroot.equals("")||frameroot.equals("null")){
			frameroot= "/frame/theme_gray";			
		}
		frameroot = path + frameroot ;
		List lst = page.getResult();
		sb.append("<ul>");
		if(lst!=null){
			String sbf = "\"javascript:window.parent.refreshWorkByTitle('"+path+detailLocation;
			String title = "";
			String fullTitle = "";
			String date  = "";
			String instanceId = "";
			String taskId = "";
			for(int i=0;i<lst.size();i++){
				Object[] obj = (Object[])lst.get(i);
				if(Integer.parseInt(listMode) != HOSTED_BY_LIST){
					fullTitle = obj[6] == null?"":(obj[6].toString());
					title = obj[6] == null?"":(obj[6].toString().length()>TITLELENGTH?obj[6].toString().substring(0, TITLELENGTH)+"...":obj[6].toString());
					instanceId = obj[3] == null?"":obj[3].toString();
					taskId = obj[0].toString();
					if(obj[1]!=null && !"".equals(obj[1])){ 
						try{
							date = obj[1].toString().substring(5, 10);
						}catch(Exception e){
							date = obj[1].toString();
						}
					}
				}else{
					fullTitle = obj[4] == null?"":obj[4].toString();
					title = obj[4] == null?"":(obj[4].toString().length()>TITLELENGTH?obj[4].toString().substring(0, TITLELENGTH)+"...":obj[4].toString());
					bussinessId = obj[9].toString();
					instanceId = obj[1].toString();
					formId = obj[10].toString();
					if(obj[2]!=null && !"".equals(obj[2])){ 
						try{
							date = obj[2].toString().substring(5, 10);
						}catch(Exception e){
							date = obj[2].toString();
						}
					}
				}
				sbf += "&taskId="+taskId+"&instanceId="+instanceId+"','"+jtitle+"');\"";		
				if(Integer.parseInt(listMode) == HOSTED_BY_LIST){//主办和已办链接暂时链到列表页，待完善后这里要修改
					//sbf = fullPath;
					sbf = "\"javascript:window.parent.refreshWorkByTitle('"+path+"/work/work!viewHostedBy.action?bussinessId="+bussinessId+
                    		"&instanceId="+instanceId+"&formId="+formId+"', '"+jtitle+"');\"";
				}else if(Integer.parseInt(listMode) == PROCESSED_LIST){
					sbf = fullPath;
				}
				sb.append("<li><img src="+frameroot+"/images/ico.gif /> <a href="+sbf+" title=\""+fullTitle+"\">")
				.append(title).append("</a><span class=\"date\">("+date+")</span></li>");
			}
		}
		sb.append("</ul>");
		sb.append("<p align=\"center\" class=\"more\"><a href="+fullPath+">更多&gt;&gt;</a></p>");
		return sb.toString();
	}
	
	/**
	 * 显示表单模板列表
	 * 	1、如果流程类型为空,加载有模板
	 * 	2、如果流程类型不为空,加载此类型下的所有模板
	 * @author:邓志城
	 * @date:2009-12-17 下午02:03:09
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String formList()throws Exception{
		if(StringUtils.hasLength(workflowType)){  
			formList = manager.getRelativeFormByProcessType(workflowType);
		}else{
			formList = manager.getAllEFormByWorkFlowType();
		}
		if(StringUtils.hasLength(formName)){
			for(Iterator<EForm> iter = formList.iterator();iter.hasNext();){
				EForm ef = iter.next();
				if(ef.getTitle() == null || ef.getTitle().indexOf(formName) == -1){
					iter.remove();
				}
			}
		}
		page = ListUtils.splitList2Page(page, formList);
		return "formlist";
	}

	/**
	 * (展现|查询)表单列表列表
	 * @author:邓志城
	 * @date:2009-5-23 上午09:52:54
	 * @param remindType 展现那种表单列表{query：查询表单列表；view：展现表单列表}
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryFormLst()throws Exception{
		if("query".equals(remindType)){
			formList = manager.getAllQueryForm();			
		}else{
			formList = manager.getAllViewForm();
		}
		ToaWorkForm workForm = manager.getWorkFormByProcessDefinitionId(workflowId);
		if(workForm!=null){
			queryFormId = String.valueOf(workForm.getQformId());
			viewFormId = String.valueOf(workForm.getVformId());
		}
		if(formName!=null){
			int count = formList.size();
			List<EForm> lstEF = new ArrayList<EForm>();
			for(int k=0;k<count;k++){
				EForm ef = formList.get(k);
				if(ef.getTitle() == null || ef.getTitle().indexOf(formName) == -1){
					lstEF.add(ef);
				}
			}
			formList.removeAll(lstEF);
		}
		page = ListUtils.splitList2Page(page, formList);
		return "queryformlist";
	}

	/**
	 * 高级查询结果
	 * @author:邓志城
	 * @date:2009-5-26 上午11:39:22
	 * @return
	 * @throws Exception
	 */
	public String doAdvancedSearch()throws Exception{
		workFlowLst = manager.getAdvancedSearchResult(workflowName, flow_status, flow_query_type, startDate, endDate, userId, businessName, sql);
		return "searchresult";
	}

	/**
	 * 在办工作列表
	 * @author:邓志城
	 * @date:2009-12-17 上午09:42:27
	 * @return 在办工作列表
	 * @throws Exception
	 */
	public String workinglist() throws Exception{
		pageWorkflow = manager.getDoingWorks(pageWorkflow, getWorkflowType(),businessName,userName,startDate,endDate,isBackSpace);			
		return "doing";
	}
	
	/**
	 * 获取收文统计结果
	 * @description
	 * @author 严建
	 * @createTime Nov 29, 2011 1:58:58 PM
	 * @return String 
	 */
	public String showProcessedTotalCount() throws Exception{
		int[] returnInt = getManager().desktopShowTodoWorkTotal("3","2");
		//数据格式：“total:10;timeouttotal:15”
		return this.renderText("total:"+returnInt[0]+";timeouttotal:"+returnInt[1]);
	}
	
/*	public String showProcessedTotalCount() throws Exception{
		int total = 0;
		int timeouttotal = 0;
		String rest1 = userService.getCurrentUser().getRest1();//用户类型
		if(rest1 == null || rest1.equals("")){
			rest1 = "0";
		}
		List<Object[]> returnObjects = new LinkedList<Object[]>();
		if(rest1.equals("1")){
			returnObjects = getManager().getProcessedWorksTotalOfDept("3","2",null);
		}else if(rest1.equals("2")){
			returnObjects = getManager().getProcessedWorksTotalOfPerson(userService.getCurrentUser().getOrgId(),"3","2",null);
		}
		for(int i=0;i<returnObjects.size();i++){
			Object[] objs = returnObjects.get(i);
			total += (new Long(objs[3].toString()));
			if(objs[4].toString().equals("1")){
				timeouttotal += (new Long(objs[3].toString()));
			}
		}
		//数据格式：“total:10;timeouttotal:15”
		return this.renderText("total:"+total+";timeouttotal:"+timeouttotal);
	}*/

	
	
	//处室公文办理情况,F6BD0F
	public String deptColumnChar() throws Exception{
		//正在办理的收文：work/work!processed.action?state=2&workflowType=3
		//逾期未办结的收文：state=2&workflowType=3&processTimeout=1
		//String workflowType,String state,String processTimeout,OALogInfo... infos
		List<Object[]> returnObjects = getManager().getProcessedWorksTotalOfDeptSecond(workflowType,state,processTimeout);
		if(returnObjects.isEmpty()){
			logger.error(userService.getCurrentUser().getUserName()+"["+userService.getCurrentUser().getUserId()+"]没有配置机构分管权限！");
			throw new SystemException("当前用户没有配置机构分管权限！");
		}
		//处室排序
		Collections.sort(returnObjects, new Comparator<Object[]>() {
			public int compare(Object[] arg0, Object[] arg1) {
				Long userSequence1 = new Long(arg0[2].toString());
				Long userSequence2 = new Long(arg1[2].toString());
				Long key1;
				if (userSequence1 != null) {
					key1 = userSequence1;
				} else {
					key1 = Long.MAX_VALUE;
				}
				Long key2;
				if (userSequence2 != null) {
					key2 = userSequence2;
				} else {
					key2 = Long.MAX_VALUE;
				}
				return key1.compareTo(key2);
			}
		});
//		labelDisplay string (WRAP,STAGGER,ROTATE or NONE ) 标签的呈现方式（超长屏蔽、折行、倾斜还是不显示） 
		String charHeader = "<chart palette='2' caption='处室公文办理情况'  baseFontSize='16' shownames='1 '  showvalues='0' decimals='0' numberPrefix='' useRoundEdges='1' legendBorderAlpha='0'>";

			String categoriesHeader = "<categories>";
			String datasetHeader = "<dataset seriesName='收文数量' color='F6BD0F' showValues='0'>";
			
			String categoriesBody = "";
			String datasetBody = "";
//			"&lt;BR&gt;"
			for(int i = 0; i<returnObjects.size();i++){
				Object[] objs = returnObjects.get(i);
				String label = objs[1].toString();
//				String labelTemp = "";
//				for(int ii=0;ii<label.length();ii++){
//					labelTemp += label.charAt(i)+"&lt;BR&gt;";
//				}
//				label = labelTemp;
				categoriesBody +="<category label='"+label+"'/>";
				datasetBody += "<set value='"+objs[3]+"' color='F6BD0F' link=\\\"JavaScript:myJS('"+objs[0]+"','"+workflowType+"','"+state+"','"+processTimeout+"');\\\" />";
			}
			
			String categoriesFooter = "</categories>";
			String datasetFooter = "</dataset>";
			
			String categories = categoriesHeader + categoriesBody + categoriesFooter;
			String dataset = datasetHeader + datasetBody + datasetFooter;
		
		String charBody = categories + dataset;
		String charFooter = "</chart>";
		
		
		String styles = "" ;
//		styles = " <styles>"+
//     " <definition>"+
//        " <style name='myHTMLFont' type='font' isHTML='1'  font='Verdana' italic='1' size='12' color='FF0000' />"+
//     " </definition>"+
//      "<application>"+
//         "<apply toObject='DATALABELS' styles='myHTMLFont' />"+
//      "</application>"+
//	"</styles>";
		
		String chars = charHeader + charBody + styles + charFooter;
		System.out.println(chars);
		logger.info(chars);
		getRequest().setAttribute("charXML",chars);
		return "deptColumnChar";
	}
	
	public String personColumnChar() throws Exception{
		if(deptId == null && deptId.equals("")){
			logger.error("处室id为空！");
			throw new SystemException("处室id为空！");
		}
		List<Object[]> returnObjects = getManager().getProcessedWorksTotalOfPerson(deptId,workflowType,state,processTimeout);
		//人员排序
		Collections.sort(returnObjects, new Comparator<Object[]>() {
			public int compare(Object[] arg0, Object[] arg1) {
				Long userSequence1 = new Long(arg0[2].toString());
				Long userSequence2 = new Long(arg1[2].toString());
				Long key1;
				if (userSequence1 != null) {
					key1 = userSequence1;
				} else {
					key1 = Long.MAX_VALUE;
				}
				Long key2;
				if (userSequence2 != null) {
					key2 = userSequence2;
				} else {
					key2 = Long.MAX_VALUE;
				}
				return key1.compareTo(key2);
			}
		});
		String charHeader = "<chart palette='2' caption='处室内部公文办理情况' baseFontSize='12' shownames='1' showvalues='0' decimals='0' numberPrefix='' useRoundEdges='1' legendBorderAlpha='0'>";

			String categoriesHeader = "<categories>";
			String datasetHeader = "<dataset seriesName='收文数量' color='F6BD0F' showValues='0'>";
			
			String categoriesBody = "";
			String datasetBody = "";
			for(int i = 0; i<returnObjects.size();i++){
				Object[] objs = returnObjects.get(i);
				categoriesBody +="<category label='"+objs[1]+"'/>";
				datasetBody += "<set value='"+objs[3]+"' color='F6BD0F' link=\\\"JavaScript:myJS('"+objs[0]+"','"+workflowType+"','"+state+"','"+processTimeout+"');\\\" />";
			}
			
			String categoriesFooter = "</categories>";
			String datasetFooter = "</dataset>";
			
			String categories = categoriesHeader + categoriesBody + categoriesFooter;
			String dataset = datasetHeader + datasetBody + datasetFooter;
		
		String charBody = categories + dataset;
		String charFooter = "</chart>";
		String chars = charHeader + charBody + charFooter;
		logger.info(chars);
		getRequest().setAttribute("charXML",chars);
		return "personColumnChar";
	}
	
	//预留方法，暂不使用 严建 2011-12-2 15:25
	private String generateDateXML(List<Object[]> returnObjects,String caption,String color,String link){
		return "";
	}
	
	
	/**
	 * 在个人桌面上显示工作处理
	 * @author:邓志城
	 * @date:2009-6-10 上午09:05:12
	 * @return
	 * @throws Exception
	 */
	public String showDesktop() throws Exception{
		//获取blockId
			String blockId = getRequest().getParameter("blockid");
			Map<String, String> map = manager.getDesktopParam(blockId);

			String showNum = map.get("showNum");//显示条数
			String subLength = map.get("subLength");//主题长度
			String showCreator = map.get("showCreator");//是否显示作者
			String showDate = map.get("showDate");//是否显示日期
			int num = 0,length = 0;
			if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
				num = Integer.parseInt(showNum);
			}
			
			if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
				length = Integer.parseInt(subLength);
			}
			//以下取待办、主办桌面显示各一半记录，当设置显示数据为奇数时，待办多取一条显示
			//获取待办任务
			//获取代办工作总记录数
			//工作流类型【-1：非系统类型；0：所有工作流】
			int todoCount = manager.getTodoWorks(new Page(5,true), "-1",businessName,userName,startDate,endDate,isBackSpace).getTotalCount();
			int hostedbyCount = manager.getHostedByDocs(new Page(5,true), "-1", businessName, startDate, endDate,"").getTotalCount();   //2010-10-14 郑志斌在getHostedByDocs方法中添加了一个查询状态参数
			int todonum = 0; //待办任务显示记录条数
			int hostedbynum = 0;
			if(todoCount == 0){//待办数量为0
				hostedbynum = num;
			}else{
				if(hostedbyCount == 0){
					todonum = num;
				}else{
					if(num % 2 !=0){
						todonum = num/2+1;
					}else{
						todonum = num/2;
					}
					if(todoCount<=todonum){
						todonum = todoCount;
					}
					hostedbynum = num-todonum;
					
					if(hostedbyCount<=hostedbynum){
						hostedbynum = hostedbyCount;
						todonum = num - hostedbynum;
					}
					
				}
			}
			
			Page pageTodo = new Page(todonum, true);
			/**
			 * [任务ID,任务创建时间,任务名称,<br>
			 *  流程实例ID,业务创建时间,挂起状态,业务名称,<br>
			 *  发起人,委派人,委派类型（0：委派，1：指派）]
			 */
			pageTodo = manager.getTodoWorks(pageTodo, "-1",businessName,userName,startDate,endDate,isBackSpace);
			String countTodo =String.valueOf(pageTodo.getTotalCount());
			//获取主办任务
			Page pageHostedByDocs = new Page(hostedbynum,true);
			/**
			 * [<p>Object[]{主键Id,流程实例Id,流程创建时间,流程当前状态,</p>
			 * <p>业务名称,发起人Id,发起人名称,流程类型Id,流程实例结束时间,主办业务Id,表单Id}</p>]
			 */
			if(hostedbynum > 0){
				pageHostedByDocs = manager.getHostedByDocs(pageHostedByDocs, "-1", businessName, startDate, endDate,"");			//2010-10-14 郑志斌在getHostedByDocs方法中添加了一个查询状态参数	
			}else {
				pageHostedByDocs.setTotalCount(0);
			}
			String countHostedByDocs =String.valueOf(pageHostedByDocs.getTotalCount());
			String rootPath = getRequest().getContextPath();
			StringBuffer innerHtml = new StringBuffer();
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(pageTodo.getResult()!=null){
				for(Iterator iterator = pageTodo.getResult().iterator(); iterator.hasNext();){
					Object[] object = (Object[])iterator.next();			
					innerHtml.append("<div class=\"linkdiv\" title=\"\">");
					//如果显示的内容长度大于设置的主题长度，则过滤该长度
					String title = object[6] == null?"":object[6].toString();
					if(title.length()>length) {
						title = title.substring(0,length)+"...";
					}
					//图标
					StringBuffer listlink = getWorkFlowLocation(rootPath,
											null,
											object[3].toString(),
											object[8].toString(), 
											title, HOSTED_BY_LIST,
											"待办",null,null,null);
					innerHtml.append("<img src=\"").append(rootPath)
												   .append("/oa/image/desktop/littlegif/zmdb.gif")
												   .append("\"/> ")
												   .append("[<font color=\"blue\">")
												   .append("<a href=\"#\" onclick=\"")
												   .append(listlink)
												   .append("\">")
												   .append("待办")
												   .append("</a>") 
												   .append("</font>] ");
					//获取链接
					StringBuffer link = getWorkFlowLocation(rootPath,
															object[0].toString(),
															object[3].toString(), 
															object[8].toString(),
															title,
															TODO_LIST,
															"待办",null,null,null);
					innerHtml.append("<span title=\"").append(object[6])
													  .append("\">")			
													  .append("<a title=\""+object[6]+"\" href=\"#\" onclick=\"")
													  .append(link.toString())
													  .append("\">")
													  .append(title)
													  .append("</a></span>");
					//显示发起人信息
					if("1".equals(showCreator)) {
						innerHtml.append("<span class =\"linkgray\">").append(object[7])
																	  .append("</span>");
					}
					//显示日期信息
					if("1".equals(showDate)) {
						String date = "";
						if(object[1]!=null && !"".equals(object[1])){ 
							try{
								date = st.format(object[1]);
							}catch(Exception e){
								date = object[1].toString();
							}
						}
						innerHtml.append("<span class =\"linkgray10\">").append(" ("+date+")")
																		.append("</span>");
					}
					innerHtml.append("</div>");
				}
				
			}
			if(pageHostedByDocs.getResult()!=null){
				for(Iterator iterator = pageHostedByDocs.getResult().iterator(); iterator.hasNext();){
					Object[] obj = (Object[])iterator.next();			
					//如果显示的内容长度大于设置的主题长度，则过滤该长度
					String title = obj[4] == null?"":obj[4].toString();
					if(title.length()>length) {
						title = title.substring(0,length)+"...";
					}
					//获取链接
					StringBuffer link = getWorkFlowLocation(rootPath, 
															null, 
															obj[1].toString(),
															obj[7].toString(),
															title, 
															HOSTED_BY_LIST,
															"主办",null,null,null);
					//查看主办链接
					StringBuffer detailLink = getWorkFlowLocation(rootPath,
																  obj[0].toString(), 
																  obj[1].toString(),
																  obj[7].toString(),
																  title,
																  HOSTED_BY_LIST, 
																  "主办",
																  obj[9].toString(), 
																  obj[10].toString(),
																  obj[8]==null?"":obj[8].toString());
					innerHtml.append("<div class=\"linkdiv\" title=\"\">");
					//图标
					innerHtml.append("<img src=\"").append(rootPath).append("/oa/image/desktop/littlegif/zmzb.gif").append("\"/> ");
					innerHtml.append("[<font color=\"green\">")
							 .append("<a href=\"#\" onclick=\"")
							 .append(link.toString()) 
							 .append("\">")
							 .append("主办")
							 .append("</a>") 
							 .append("</font>] ");
					innerHtml.append("<span title=\"").append(title).append("\">")			
							 						  .append("<a title=\""+obj[4]+"\" href=\"#\" onclick=\"")
							 						  .append(detailLink.toString())
							 						  .append("\">")
							 						  .append(title)
							 						  .append("</a></span>");
					//显示发起人信息
					if("1".equals(showCreator)) {
						innerHtml.append("<span class =\"linkgray\">").append(obj[6]).append("</span>");
					}
					//显示日期信息
					if("1".equals(showDate)) {
						String date = "";
						if(obj[2]!=null && !"".equals(obj[2])){ 
							try{
								date = st.format(obj[2]);
							}catch(Exception e){
								date = obj[2].toString();
							}
						}
						innerHtml.append("<span class =\"linkgray10\">").append(" ("+date+")").append("</span>");
					}
					innerHtml.append("</div>");
				}
			}
			innerHtml.append("<div class=\"linkdiv\" align='center'>");
			innerHtml.append("待办工作(<font color=\"red\">").append(countTodo)
					 .append("</font>)/")
					 .append("主办工作(")
					 .append("<font color=\"red\">")
					 .append(countHostedByDocs)
					 .append("</font>)")
					 .append("</div>");
			LogPrintStackUtil.logInfo(innerHtml.toString());
			return this.renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
	
	/*
	 * 
	 * Description:在个人桌面上显示待办工作
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 11, 2010 10:53:28 AM
	 */
	public String displayDesktop() throws Exception{
		//获取blockId
		String blockId = getRequest().getParameter("blockid");
		Map<String, String> map = manager.getDesktopParam(blockId);
		String showNum = map.get("showNum");		//显示条数
		String subLength = map.get("subLength");	//主题长度
		String showCreator = map.get("showCreator");//是否显示作者
		String showDate = map.get("showDate");		//是否显示日期
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		//[任务ID,任务创建时间,任务名称, 流程实例ID,业务创建时间,挂起状态,业务名称,发起人,委派人,委派类型（0：委派，1：指派）]
		
		Page pageTodo=new Page(1000000,true);
		pageTodo=manager.getAllTodoWorks(pageTodo,null, businessName, userName, startDate, endDate,isBackSpace);
		int count=pageTodo!=null?pageTodo.getTotalCount():0;
		List<Object[]> todolist=manager.getProccessTypeIdAndNameList(pageTodo);
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(todolist!=null){
			for(int i=0;i<num&&i<todolist.size();i++){
				Object[] object =todolist.get(i);			
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				//如果显示的内容长度大于设置的主题长度，则过滤该长度
				//String title = object[6] == null?"":object[6].toString();
				//if(title.length()>length) {
				//	title = title.substring(0,length)+"...";
				//}
				//图标
				StringBuffer listlink = getTodoWorkFlowListLocation(rootPath,
										null,
										null,
										object[0].toString(), 
										null, HOSTED_BY_LIST,
										"待办",null,null,null);
				innerHtml.append("<img src=\"").append(rootPath)
									   .append("/oa/image/desktop/littlegif/zmdb.gif")
									   .append("\"/> ")
									   .append("<font color=\"blue\">")
									   .append("<a href=\"#\" onclick=\"")
									   .append(listlink)
									   .append("\">")
									   .append(object[1].toString())
									   .append("</a>") 
									   .append("</font> ");
				//显示发起人信息
				/*if("1".equals(showCreator)) {
					innerHtml.append("<span class =\"linkgray\">")
										.append(object[7])
										.append("</span>");
				}
				//显示日期信息
				if("1".equals(showDate)) {
					String date = "";
					if(object[1]!=null && !"".equals(object[1])){ 
						try{
							date = st.format(object[1]);
						}catch(Exception e){
							date = object[1].toString();
						}
					}
					innerHtml.append("<span class =\"linkgray10\">")
										.append(" ("+date+")")
										.append("</span>");
				}*/
				innerHtml.append("</div>");
			}
		}
		innerHtml.append("<div class=\"linkdiv\" align='center'>");
		innerHtml.append("待办工作(<font color=\"red\">").append(count)
				 .append("</font>)");
		logger.info(innerHtml.toString());
		return this.renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * author:dengzc
	 * description:获取个人桌面中工作处理链接
	 * 个人桌面主要显示待办和主办相关工作处理事项
	 * modifyer:
	 * description:
	 * @param String path Web上下文相对路径 
	 * @param String taskId 任务ID
	 * @param String instanceId 实例ID
	 * @param String workFlowType 当前任务所属流程类型
	 * @param String title 当前任务业务标题
	 * @param int listMode 列表模式
	 * <p>TODO_LIST:待办;HOSTED_BY_LIST:主办</p>
	 * @param String opt “主办”|“待办”
	 * @param String businessId 业务数据ID
	 * @param String formId 主表单ID
	 * @param String overDate 流程结束时间【未结束则为null】
	 * @return
	 * @throws Exception
	 */
	private StringBuffer getWorkFlowLocation(String path,
											 String taskId,
											 String instanceId,
											 String workFlowType,
											 String title,
											 int listMode,
											 String opt,
											 String businessId,
											 String formId,
											 String overDate)throws Exception{
		if(listMode == TODO_LIST){
			StringBuffer href = new StringBuffer("javascript:window.parent.refreshWorkByTitle('"+path);
			switch (Integer.parseInt(workFlowType)) {
				case WorkFlowTypeConst.COLUMN://栏目<%=root%>/infopub/articlesappro/articlesAppro!nextstep.action?taskId="+taskId+"&instanceId=
					/*href.append("/infopub/articlesappro/articlesAppro!nextstep.action?taskId="+taskId+
								"&instanceId="+instanceId+
								"&workflowType="+workFlowType);
					href.append("','"+opt+"栏目');");*/
					href.append("/infopub/articlesappro/articlesAppro!todo.action?workflowType=1");
					href.append("','待审信息');");
					break;
				case WorkFlowTypeConst.SENDDOC://发文处理
					href.append("/senddoc/sendDoc!form.action?taskId="+taskId)
	                    .append("&instanceId="+instanceId+"&isStartWorkflow=0")
						.append("','"+opt+"公文');");
					break;
				case WorkFlowTypeConst.RECEDOC://收文处理
					href.append("/recvdoc/recvDoc!form.action?taskId="+taskId)
						.append("&recvtitle=").append(title)
						.append("&isStartWorkflow=0&instanceId="+instanceId+"&workflowType="+workFlowType)
						.append("','"+opt+"来文');");
					break;
				case WorkFlowTypeConst.INSPECT:
					href.append("/inspect/inspect!input.action?listMode="+listMode)
						.append("&taskId="+taskId+"&instanceId="+instanceId+"','"+opt+"督察');");
					break;
				case WorkFlowTypeConst.MEETING:
					href.append("','');");
					break;
				case WorkFlowTypeConst.TIANJIANYI:
					break;
				case WorkFlowTypeConst.XINFANGGUANLI:
					break;
				case WorkFlowTypeConst.ZHIBANGUANLI:
					break;
				case WorkFlowTypeConst.XINXICHULI:
					break;
				case WorkFlowTypeConst.XINWENGUANLI:
					break;
				case WorkFlowTypeConst.QIANBAOGUANLI:
					break;
				case WorkFlowTypeConst.DANGDANGUANLI:
					break;
				default://默认为工作处理
					href.append("/work/work!input.action?listMode="+listMode)
						.append("&taskId="+taskId+"&businessName="+URLEncoder.encode(URLEncoder.encode(title, "utf-8"),"utf-8")+"&instanceId="+instanceId+"','"+opt+"工作');");
					break;
				}
			return href;
		}else{
			StringBuffer href = new StringBuffer("javascript:window.parent.refreshWorkByTitle('"+path);
			if(taskId == null){//直接转到主办列表页
				switch (Integer.parseInt(workFlowType)) {
					case WorkFlowTypeConst.COLUMN://栏目
						href.append("/infopub/articlesappro/articlesAppro!todo.action?workflowType=1");
						href.append("','待审信息');");
						break;
					case WorkFlowTypeConst.SENDDOC://发文处理
						href.append("/senddoc/sendDoc!hostedby.action?workflowType="+workFlowType)
						.append("','"+opt+"公文');");
						break;
					case WorkFlowTypeConst.RECEDOC://收文处理
						href.append("/recvdoc/recvDoc!hostedby.action?workflowType="+workFlowType)
						.append("','"+opt+"来文');");
						break;
					case WorkFlowTypeConst.INSPECT:
						href.append("/inspect/inspect.action?listMode="+listMode+"&workflowType="+workFlowType)
						.append("','"+opt+"督察');");
						break;
					case WorkFlowTypeConst.MEETING:
						href.append("/meetingmanage/meetingaudit/meetingaudit.action");
						href.append("','');");
						break;
					case WorkFlowTypeConst.TIANJIANYI:
						break;
					case WorkFlowTypeConst.XINFANGGUANLI:
						break;
					case WorkFlowTypeConst.ZHIBANGUANLI:
						break;
					case WorkFlowTypeConst.XINXICHULI:
						break;
					case WorkFlowTypeConst.XINWENGUANLI:
						break;
					case WorkFlowTypeConst.QIANBAOGUANLI:
						break;
					case WorkFlowTypeConst.DANGDANGUANLI:
						break;
					default://默认为工作处理
						href.append("/fileNameRedirectAction.action?toPage=work/work-todomain.jsp")
						.append("','"+opt+"工作');");
					break;
				}
			}else{//转到主办查看页
				switch (Integer.parseInt(workFlowType)) {
					case WorkFlowTypeConst.COLUMN://栏目
						href.append("/infopub/articlesappro/articlesAppro!todo.action?workflowType=1");
						href.append("','待审信息');");
						break;
					case WorkFlowTypeConst.SENDDOC://发文处理
						href.append("/senddoc/sendDoc!viewHostedBy.action?bussinessId="+businessId)
							.append("&instanceId="+instanceId)
							.append("&formId="+formId)
							.append("&overDate="+overDate)
							.append("&workflowType="+workFlowType)
							.append("','"+opt+"公文');");
						break;
					case WorkFlowTypeConst.RECEDOC://收文处理<%=root%>/recvdoc/recvDoc!viewHostedBy.action?recvid="+bussinessId+"&instanceId="+instanceId+"&formId="+formId
						href.append("/recvdoc/recvDoc!viewHostedBy.action?workflowType="+workFlowType)
							.append("&recvid="+businessId)
							.append("&instanceId="+instanceId)
							.append("&formId="+formId)
							.append("&overDate="+overDate)
							.append("','"+opt+"来文');");
						break;
					case WorkFlowTypeConst.INSPECT://<%=root%>/inspect/inspect!viewHostedBy.action?bussinessId="+id+"&instanceId="+instanceId+"&formId="+formId
						href.append("/inspect/inspect!viewHostedBy.action?workflowType="+workFlowType)
							.append("&bussinessId="+businessId)
							.append("&instanceId"+instanceId)
							.append("&formId="+formId)
							.append("&overDate="+overDate) 
							.append("','"+opt+"督察');");
						break;
					case WorkFlowTypeConst.MEETING:
						href.append("','');");
						break;
					case WorkFlowTypeConst.TIANJIANYI:
						break;
					case WorkFlowTypeConst.XINFANGGUANLI:
						break;
					case WorkFlowTypeConst.ZHIBANGUANLI:
						break;
					case WorkFlowTypeConst.XINXICHULI:
						break;
					case WorkFlowTypeConst.XINWENGUANLI:
						break;
					case WorkFlowTypeConst.QIANBAOGUANLI:
						break;
					case WorkFlowTypeConst.DANGDANGUANLI:
						break;
					default://默认为工作处理
						href.append("/work/work!viewHostedBy.action?bussinessId="+businessId)
							.append("&instanceId=").append(instanceId)
							.append("&formId=").append(formId)
							.append("','"+opt+"工作');");
					break;
				}
			}
			return href;
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 15, 2010 11:08:09 AM
	 */
	private StringBuffer getTodoWorkFlowListLocation(String path,
			 String taskId,
			 String instanceId,
			 String workFlowType,
			 String title,
			 int listMode,
			 String opt,
			 String businessId,
			 String formId,
			 String overDate)throws Exception{
		if(listMode == TODO_LIST){
			StringBuffer href = new StringBuffer("javascript:window.parent.refreshWorkByTitle('"+path);
			switch (Integer.parseInt(workFlowType)) {
				case WorkFlowTypeConst.COLUMN://栏目<%=root%>/infopub/articlesappro/articlesAppro!nextstep.action?taskId="+taskId+"&instanceId=
					/*href.append("/infopub/articlesappro/articlesAppro!nextstep.action?taskId="+taskId+
								"&instanceId="+instanceId+
								"&workflowType="+workFlowType);
					href.append("','"+opt+"栏目');");*/
					href.append("/infopub/articlesappro/articlesAppro!todo.action?workflowType=1");
					href.append("','待审信息');");
					break;
				case WorkFlowTypeConst.SENDDOC://发文处理
					href.append("/senddoc/sendDoc!form.action?taskId="+taskId)
	                    .append("&instanceId="+instanceId+"&isStartWorkflow=0")
						.append("','公文审核');");
					break;
				case WorkFlowTypeConst.RECEDOC://收文处理
					href.append("/recvdoc/recvDoc!form.action?taskId="+taskId)
						.append("&recvtitle=").append(title)
						.append("&isStartWorkflow=0&instanceId="+instanceId+"&workflowType="+workFlowType)
						.append("','待办来文');");
					break;
				case WorkFlowTypeConst.INSPECT:
					href.append("/inspect/inspect!input.action?listMode="+listMode)
						.append("&taskId="+taskId+"&instanceId="+instanceId+"','"+opt+"督察');");
					break;
				case WorkFlowTypeConst.MEETING:
					href.append("','');");
					break;
				case WorkFlowTypeConst.TIANJIANYI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.XINFANGGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.ZHIBANGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.XINXICHULI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.XINWENGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.QIANBAOGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.DANGDANGUANLI:
					href = new StringBuffer("");
					break;
				default://默认为工作处理
					href.append("/work/work!input.action?listMode="+listMode)
						.append("&taskId="+taskId+"&instanceId="+instanceId+"','待办工作');");
					break;
				}
			return href;
		}else if(listMode == DOING_LIST){
			StringBuffer href = new StringBuffer("javascript:window.parent.refreshWorkByTitle('"+path);
			href.append("/work/work!input.action?listMode="+listMode)
					.append("&taskId="+taskId+"&instanceId="+instanceId+"','"+opt+"工作');");
			return href;
		}else{
			StringBuffer href = new StringBuffer("javascript:window.parent.refreshWorkByTitle('"+path);
			if(taskId == null){//直接转到主办列表页
				switch (Integer.parseInt(workFlowType)) {
				case WorkFlowTypeConst.COLUMN://栏目
					href.append("/infopub/articlesappro/articlesAppro!todo.action?workflowType=1");
					href.append("','待审信息');");
					break;
				case WorkFlowTypeConst.SENDDOC://发文处理
					href.append("/senddoc/sendDoc!todo.action?workflowType="+workFlowType)
					.append("','公文审核');");
					break;
				case WorkFlowTypeConst.RECEDOC://收文处理
					href.append("/recvdoc/recvDoc!todo.action?workflowType="+workFlowType)
					.append("','待办来文');");
					break;
				case WorkFlowTypeConst.INSPECT:
					href.append("/inspect/inspect.action?listMode="+listMode+"&workflowType="+workFlowType)
					.append("','"+opt+"督察');");
					break;
				case WorkFlowTypeConst.MEETING:
					href.append("/meetingmanage/meetingaudit/meetingaudit.action");
					href.append("','');");
					break;
				case WorkFlowTypeConst.TIANJIANYI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.XINFANGGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.ZHIBANGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.XINXICHULI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.XINWENGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.QIANBAOGUANLI:
					href = new StringBuffer("");
					break;
				case WorkFlowTypeConst.DANGDANGUANLI:
					href = new StringBuffer("");
					break;
				default://默认为工作处理
					href.append("/fileNameRedirectAction.action?toPage=work/work-todomain.jsp")
					.append("','"+opt+"工作');");
				break;
				}
			}else{
				href.append("/fileNameRedirectAction.action?toPage=work/work-doingmain.jsp")
				.append("','"+opt+"工作');");
			}
			return href;
		}
	}

	/**
	 * 转到高级查询初始页面{展现所有}
	 * @author:邓志城
	 * @date:2009-5-20 下午02:09:51
	 * @return
	 * @throws Exception
	 */
	public String advancedSearch()throws Exception{
		if("null".equals(workflowType)){//在工作查询中点击高级查询
			workFlowLst = manager.getAllWorkFlowInstanceWithWorkFlowType();
			return "advancedsearch";			
		}else{//在发文、收文、督察督办模块点击高级查询
			typeList = manager.getAllWorkflowTypeLst(workflowType);
			formId = "0";
			if(typeList!=null && typeList.size()>0){
				String workFlowName = typeList.get(0);
				ToaWorkForm form = manager.getWorkFormByWorkFlowName(workFlowName);
				Long mainFormId = form==null?0:form.getQformId();
				formId = String.valueOf(mainFormId);
				eformFieldLst = manager.getFormTemplateFieldList(formId);
			}
			return "initadvancedsearch";
		}
	}

	/**
	 * 根据流程名称获取查询表单
	 * @author:邓志城
	 * @date:2009-6-9 下午07:53:56
	 * @return
	 * @throws Exception
	 */
	public String getFormByWorkflow()throws Exception{
		try {
			ToaWorkForm form = manager.getWorkFormByWorkFlowName(workflowName);
			Long mainFormId = form==null?0:form.getQformId();
			formId = String.valueOf(mainFormId);
			renderText(formId);
		} catch (Exception e) {
			renderText("error");
		}
		return null;
	}
	
	/**
	 * 转到高级查询页面（携带流程定义文件名）
	 * @author:邓志城
	 * @date:2009-5-21 下午04:27:03
	 * @return
	 * @throws Exception
	 */
	public String toAdvanceSearchPage()throws Exception{
		ToaWorkForm form = manager.getWorkFormByWorkFlowName(workflowName);
		Long mainFormId = form==null?0:form.getQformId();
		formId = String.valueOf(mainFormId);
		eformFieldLst = manager.getFormTemplateFieldList(formId);
		if(workflowId!=null && workflowName!=null){//工作查询链接进来
			typeList.add(workflowName);
		}
		return "initadvancedsearch";
	}

	/**
	 * 高级查询时,根据选择的流程自动切换表单
	 * 需要根据表单动态创建SQL语句
	 * @author:邓志城
	 * @date:2009-6-15 上午10:14:31
	 * @return
	 * @throws Exception
	 * @see work-initadvancedsearch.jsp[$("#selectWorkflow").change(function(){...}]
	 */
	public String getFieldByFormId()throws Exception{
		eformFieldLst = manager.getFormTemplateFieldList(formId);
		JSONArray array = JSONArray.fromObject(eformFieldLst);
		LogPrintStackUtil.logInfo("表单字段域："+array.toString());
		return renderText(array.toString());
	}
	
	/**
	 * 获取流程类别下的所有流程定义
	 * @author:邓志城
	 * @date:2009-5-21 上午11:23:28
	 * @param workFlowTypeId
	 * @return
	 * @throws SystemException
	 */
	public String getWorkFlowInfoByWorkFlowTypeId()throws Exception{
		try{
			String info = manager.getWorkFlowInfoByWorkFlowTypeId(getRequest(),workflowType);
			renderHtml(info);
		}catch(Exception e){
			renderText("error");
		}
		return null;
	}
	
	/**
	 * 通过formId查看表单
	 * @return
	 * @throws Exception
	 */
	public String seeFormSee()throws Exception{
		return "seeform";
	}

	public List<EForm> getFormList() {
		return formList;
	}

	public Page<EForm> getPage() {
		return page;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

    public Page<Object[]> getFlowPage() {
		return flowPage;
	}

	public String getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}

	public String getFlow_query_type() {
		return flow_query_type;
	}

	public void setFlow_query_type(String flow_query_type) {
		this.flow_query_type = flow_query_type;
	}

	public String getFlow_status() {
		return flow_status;
	}

	public void setFlow_status(String flow_status) {
		this.flow_status = flow_status;
	}

	public Date getPrcs_date1() {
		return prcs_date1;
	}

	public void setPrcs_date1(Date prcs_date1) {
		this.prcs_date1 = prcs_date1;
	}

	public Date getPrcs_date2() {
		return prcs_date2;
	}

	@SuppressWarnings("deprecation")
	public void setPrcs_date2(Date prcs_date2) {
		prcs_date2.setHours(23);
		prcs_date2.setMinutes(59);
		this.prcs_date2 = prcs_date2;
	}

	public String getRun_name() {
		return run_name;
	}

	public void setRun_name(String run_name) {
		this.run_name = run_name;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<WorkFlowBean> getWorkFlowLst() {
		return workFlowLst;
	}

	public List<EFormField> getEformFieldLst() {
		return eformFieldLst;
	}

	public List<String> getTypeList() {
		return typeList;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	/**
	 * 提供给父类使用
	 */
	@Override
	protected BaseManager getManager() {
		return this.manager;
	}

	/**
	 * 提供给父类使用
	 * @param workflowType "-1"非系统类型
	 */
	@Override
	public String getWorkflowType() {
		return workflowType;
	}

	/**
	 * 提供给父类使用,返回调用消息模块的类型
	 */
	@Override
	public String getModuleType() {
		return GlobalBaseData.MSG_GZCL;
	}

	@Override
	public String getDictType() {
		return WorkFlowTypeConst.WORKIDEA;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getBusinessTitle() {
		return businessTitle;
	}

	public void setBusinessTitle(String businessTitle) {
		this.businessTitle = businessTitle;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String[]> getUserList() {
		return userList;
	}

	public void setReAssignActorId(String reAssignActorId) {
		this.reAssignActorId = reAssignActorId;
	}

	public String getTransHtmls() {
		return transHtmls;
	}

	public void setTransHtmls(String transHtmls) {
		this.transHtmls = transHtmls;
	}

	public String getUsersHtmls() {
		return usersHtmls;
	}

	public void setUsersHtmls(String usersHtmls) {
		this.usersHtmls = usersHtmls;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public String getAttchId() {
		return attchId;
	}

	public void setAttchId(String attchId) {
		this.attchId = attchId;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getSelectNodesInfo() {
		return selectNodesInfo;
	}

	public void setSelectNodesInfo(String selectNodesInfo) {
		this.selectNodesInfo = selectNodesInfo;
	}

	public String getTranIds() {
		return tranIds;
	}

	public void setTranIds(String tranIds) {
		this.tranIds = tranIds;
	}

	public String getOnlyone() {
		return onlyone;
	}

	public void setOnlyone(String onlyone) {
		this.onlyone = onlyone;
	}

	public String getWorktype() {
		return worktype;
	}

	public void setWorktype(String worktype) {
		this.worktype = worktype;
	}

	public String getRtnContent() {
		return rtnContent;
	}

	public void setRtnContent(String rtnContent) {
		this.rtnContent = rtnContent;
	}

	public String getWapUserName() {
		return wapUserName;
	}

	public void setWapUserName(String wapUserName) {
		this.wapUserName = wapUserName;
	}
	
}
