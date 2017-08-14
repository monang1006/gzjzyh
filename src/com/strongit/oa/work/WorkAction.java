package com.strongit.oa.work;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.strongit.oa.bo.ToaWorkForm;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.work.util.WorkFlowBean;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
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

	@Autowired WorkManager manager;
	private Page<EForm> page = new Page<EForm>(FlexTableTag.MAX_ROWS, false);//分页对象,每页20条,支持自动获取总记录数
	private Page<Object[]> flowPage = new Page<Object[]>(FlexTableTag.MAX_ROWS, true);//分页对象,每页10条,支持自动获取总记录数
	private List<EForm> formList;
	
	/*
	 * 查询工作流相关属性
	 */
    private String flow_id;//流程名称
	 
	private String flow_status;//流程状态
	private String flow_query_type;//流程范围
	
	private Date prcs_date1	;//流程开始时间
	private Date prcs_date2	;//流程结束时间
	private String run_name;//名称文号业务数据
	private String userId;//用户ID
	private String formName;//表单模板列表查询
	private String sql ;//查询表单里业务数据
	
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
		User user = adapterBaseWorkflowManager.getUserService().getCurrentUser();//得到当前用户
		userName = user.getUserName();//当前用户姓名
		Organization organization = adapterBaseWorkflowManager.getUserService().getDepartmentByLoginName(user.getUserLoginname());//得到当前用户所属单位
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
		/*int[] returnInt = getManager().desktopShowTodoWorkTotal("3","2");//workflowType='3' state='2'
		//数据格式：“total:10;timeouttotal:15”
		return this.renderText("total:"+returnInt[0]+";timeouttotal:"+returnInt[1]);*/
		return this.renderText("total:20;timeouttotal:20");
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
			logger.error(adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserName()+"["+adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId()+"]没有配置机构分管权限！");
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
	
}
