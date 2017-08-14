package com.strongit.workflow.workflowDesign.action;

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

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.strongit.oa.bgt.documentview.bo.DocumentViewParamter;
import com.strongit.oa.bgt.documentview.bo.DocumentViewUtil;
import com.strongit.oa.bgt.documentview.bo.UserAndOrgInfo;
import com.strongit.oa.bgt.documentview.manager.DocumentViewManager;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.util.StringUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 公文限时办理Action
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Sep 14, 2011 10:25 AM
 * @version 1.0.0.0
 * @classpath com.strongit.workflow.workflowDesign.action.DocumentHandleAction
 */
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "DocumentHandleAction.action", type = ServletActionRedirectResult.class),
		@Result(name = "limitTime", value = "/WEB-INF/jsp/workflowDesign/action/documentHandle-limitTime.jsp", type = ServletDispatcherResult.class) })
public class DocumentHandleAction extends AbstractBaseWorkflowAction<Object[]> {
	/**
	 */
	private static final long serialVersionUID = 1L;

	private Page<Object[]> page = new Page<Object[]>(FlexTableTag.MAX_ROWS, true);

	private String processId;

	private String proName;

	private Object[] model = new Object[] {};

	private IProcessInstanceService manager;

	@Autowired
	@Qualifier("baseManager")
	BaseManager basemanager;

	@Autowired
	IUserService userService;// 统一用户服务

	@Autowired
	DocumentViewManager documentViewManager;

	// 查询条件
	private String processName; // 流程名

	private String startUserName; // 发起人

	private String orgIds; // 发起人

	private Date searchDate; // 启动时间

	private String searchDateString;

	private String timeout; // 流程是否超期

	private String processDefinitionNames = "";// 流程名称

	private Map<String, String> processDefinitionNameMapIds;

	private List annalList; // 流程处理意见

	private String mytype;

	private String day; // 任务停留天数

	public void setModel(Object[] model) {
		this.model = model;
	}

	public void setPage(Page<Object[]> page) {
		this.page = page;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public DocumentHandleAction() {

	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public Object[] getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(IProcessInstanceService aManager) {
		manager = aManager;
	}

	/**
	 * @author:luosy
	 * @description:
	 * @date : 2012-4-1
	 * @modifyer:
	 * @description: 待办文件统计
	 * @return
	 * @throws Exception
	 */
	public String todowjtj() throws Exception {
		Long start = System.currentTimeMillis();
		try {
			User curuser = userService.getCurrentUser();
			DocumentViewParamter paramter = new DocumentViewParamter();
			paramter.setMytype(mytype);
			paramter.setState("0");
			paramter.setOrgIds(orgIds);
			paramter.setWorkflowType("3");// 办件登记类型：“370020”,收文处理类型："3"
			paramter.setIsSuspended("0");
			paramter.setCurUserId(curuser.getUserId());
			paramter.setCurUserOrgIg(curuser.getOrgId());
			paramter.setItemsList(documentViewManager.getToSelectItems());
			List<Object[]> processListtemp = documentViewManager
					.getTodowjtjForList(paramter);
			List<TaskBean> processList = new ArrayList<TaskBean>();
			if (processListtemp != null) {
				processList = new ArrayList<TaskBean>(documentViewManager
						.getResult(processListtemp));
			}
			List<UserAndOrgInfo> managerUserAndOrgInfoForList = documentViewManager.getManagerUserAndOrgInfoForList(curuser
				.getUserId());
			String charXML = "0";
			if(managerUserAndOrgInfoForList != null && !managerUserAndOrgInfoForList.isEmpty()){
			    DocumentViewUtil documentviewutil = new DocumentViewUtil(
				    documentViewManager.getManagerUserAndOrgInfoForList(curuser
					    .getUserId()));
			    for (TaskBean taskbean : processList) {
				String deptname = taskbean.getDeptName();
				documentviewutil.addDeptName(deptname);
			    }
			    charXML = genchartodowjtjXML(documentviewutil, "待办文件");
			}
			getRequest().setAttribute("charXML", charXML);
		} catch (SystemException e) {
			logger.error(e.getMessage());
		} catch (Exception ex) {
			logger.error("获取公文限时办理情况出现异常,异常信息：" + ex.getMessage());
		}
		logger.info("\r\n\t\t{@@@@@@} 待办文件柱状图统计显示耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		return "wjtj";
	}

	protected String genchartodowjtjXML(DocumentViewUtil documentviewutil,
			String captionStr) {
		List<String> orgnameList = documentviewutil.getOrgNameListSorted();
		Map<String, Integer> orgNamemapCounter = documentviewutil
				.getOrgNamemapCounter();
		Map<String, String> orgNameMapOrgId = documentviewutil
				.getOrgNameMapOrgId();
		/* 页面显示 */
		String charHeader = "<chart palette='2' caption='"
				+ captionStr
				+ "统计' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1'"
				+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
		String categories = "<categories >";
		String totaldataset = "<dataset seriesName='" + captionStr
				+ "数' color='1328c9'>";
		for (String orgname : orgnameList) {
			categories = categories + "<category label='"
					+ orgname + "' />";
			totaldataset = totaldataset + "<set value='"
					+ orgNamemapCounter.get(orgname)
					+ "' link='javascript:onclickChar(\\\""
					+ orgNameMapOrgId.get(orgname) + "\\\")'/>";
		}
		categories += "</categories >";
		totaldataset += "</dataset>";
		String charXML = charHeader + categories + totaldataset + "</chart>";
		System.out.println("charXML:" + charXML);
		return charXML;
	}

	/**
	 * @author:luosy
	 * @description:
	 * @date : 2012-4-1
	 * @modifyer:
	 * @description: 在办、办结、文件统计
	 * @return
	 * @throws Exception
	 */
	public String wjtj() throws Exception {
		Long start = System.currentTimeMillis();
		String captionStr = "";
		try {
			User curuser = userService.getCurrentUser();
			DocumentViewParamter paramter = new DocumentViewParamter();
			paramter.setMytype(mytype);
			paramter.setState(state);
			paramter.setOrgIds(orgIds);
			paramter.setStartUserName(startUserName);
			if(excludeWorkflowType != null && !"null".equals(excludeWorkflowType)){
				paramter.setExcludeWorkflowType(excludeWorkflowType);
			}
			paramter.setWorkflowType(workflowType);
			paramter.setCurUserId(curuser.getUserId());
			paramter.setCurUserOrgIg(curuser.getOrgId());
			if("1".equals(state)){//办结文默认显示本年度的数据
			    	java.util.Calendar Cal = java.util.Calendar.getInstance();
			    	int doneyear = Cal.get(java.util.Calendar.YEAR);
			    	if(doneYear != null && !"".equals(doneYear) && !"null".equals(doneYear)){
			    	    doneyear =Integer.parseInt(doneYear);
			    	}
			    	paramter.setDoneYear(doneyear);
			}
			List<String> itemsList = new LinkedList<String>();
			itemsList.add("processInstanceId");
			paramter.setItemsList(itemsList);
			List<Object[]> processList = documentViewManager
					.getwjtjForList(paramter);
			if (processList == null) {
				processList = new ArrayList<Object[]>();
			}

			System.out
					.println("DocumentHandleAction wjtj() processList.size():"
							+ processList.size());
			List<UserAndOrgInfo> managerUserAndOrgInfoForList = documentViewManager.getManagerUserAndOrgInfoForList(curuser
				.getUserId());
			String charXML = "0";
			if(managerUserAndOrgInfoForList != null && !managerUserAndOrgInfoForList.isEmpty()){
			    DocumentViewUtil documentviewutil = new DocumentViewUtil(managerUserAndOrgInfoForList);
			    for (Object[] objs : processList) {
				String mainActorId = StringUtil.castString(objs[1]);
				documentviewutil.addUserId(mainActorId);
			    }
			    String state = this.getRequest().getParameter("state");
			    
			    if ("0".equals(state)) {
				captionStr = "在办文件";
			    } else if ("1".equals(state)) {
				captionStr = "办结文件";
			    }
			    charXML = gencharwjtjXML(documentviewutil, captionStr);
			}
			getRequest().setAttribute("charXML", charXML);
		} catch (SystemException e) {
			logger.error(e.getMessage());
		} catch (Exception ex) {
			logger.error("获取公文限时办理情况出现异常,异常信息：" + ex.getMessage());
		}
		logger.info("\r\n\t\t{@@@@@@} "+captionStr+"柱状图统计显示耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		return "wjtj";
	}

	protected String gencharwjtjXML(DocumentViewUtil documentviewutil,
			String captionStr) {
		List<String> orgIdList = documentviewutil.getOrgIdListSorted();
		Map<String, Integer> orgIdMapCounter = documentviewutil
				.getOrgIdMapCounter();
		Map<String, String> orgIdMapOrgName = documentviewutil
				.getOrgIdMapOrgName();

		/* 页面显示 */
		String charHeader = "<chart palette='2' caption='"
				+ captionStr
				+ "统计' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1'"
				+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
		String categories = "<categories >";
		String totaldataset = "<dataset seriesName='" + captionStr
				+ "数' color='1328c9'>";
		for (String orgId : orgIdList) {
			categories = categories + "<category label='"
					+ orgIdMapOrgName.get(orgId) + "' />";
			totaldataset = totaldataset + "<set value='"
					+ orgIdMapCounter.get(orgId)
					+ "' link='javascript:onclickChar(\\\"" + orgId
					+ "\\\")'/>";
		}
		categories += "</categories >";
		totaldataset += "</dataset>";
		String charXML = charHeader + categories + totaldataset + "</chart>";
		System.out.println("charXML:" + charXML);
		return charXML;
	}
	
	protected String gencharlimitTimeXML(DocumentViewUtil documentviewutil) {
	    String captionStr = "公文数限时";
	    List<String> orgIdList = documentviewutil.getOrgIdListSorted();
	    Map<String, Integer> orgIdMapCounter0 = documentviewutil.getOrgIdMapCounter0();
	    Map<String, Integer> orgIdMapCounter1 = documentviewutil.getOrgIdMapCounter1();
	    Map<String, String> orgIdMapOrgName = documentviewutil.getOrgIdMapOrgName();
	    
	    /* 页面显示 */
	    String charHeader = "<chart palette='2' caption='"
		+ captionStr
		+ "统计' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1'"
		+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
	    String categories = "<categories >";
	    String totaldataset0 = "<dataset seriesName='未超期公文数' color='13c930'>";
	    String totaldataset1 = "<dataset seriesName='超期公文数' color='fa2626'>";
	    for (String orgId : orgIdList) {
		categories = categories + "<category label='"
		+ orgIdMapOrgName.get(orgId) + "' />";
		
		totaldataset0 = totaldataset0 + "<set value='"
		+ orgIdMapCounter0.get(orgId)
		+ "'/>";
		
		totaldataset1 = totaldataset1 + "<set value='"
		+ orgIdMapCounter1.get(orgId)
		+ "'/>";
	    }
	    categories += "</categories >";
	    totaldataset0 += "</dataset>";
	    totaldataset1 += "</dataset>";
	   
	    String charXML = charHeader + categories + totaldataset0+ totaldataset1 + "</chart>";
	    System.out.println("charXML:" + charXML);
	    return charXML;
	}
	protected String genlimitTimeTable(DocumentViewUtil documentviewutil){
	    List<String> orgIdList = documentviewutil.getOrgIdListSorted();
	    Map<String, Integer> orgIdMapCounter0 = documentviewutil.getOrgIdMapCounter0();
	    Map<String, Integer> orgIdMapCounter1 = documentviewutil.getOrgIdMapCounter1();
	    Map<String, String> orgIdMapOrgName = documentviewutil.getOrgIdMapOrgName();
	    int heigftUnit = 35;//每一行的行高
	    String height = heigftUnit + "px";
	    int colCount = 1; 
	    
	    StringBuilder tableContent = new StringBuilder();
	    tableContent.append("<tr height=\""+height+"\"  align=\"center\" class=\"scrollColThead\"  >"
		    	+ "<th class=\"scrollRowThead scrollCR\" width=\"5%\" >序号</th>"
			+ "<th width=\"26%\" >处室</th>"
			+ "<th width=\"23%\" >未超期公文数</th>"
			+ "<th width=\"23%\" >超期公文数</th>"
			+ "<th width=\"23%\" >小计</th>" 
			+ "</tr>");//添加行头标题信息
	    
	    int count0 = 0;
	    int count1 = 0;
	    int count = 0;
	    for (String orgId : orgIdList) {
		int count0Temp = orgIdMapCounter0.get(orgId);
		count0 = count0 + count0Temp;
		int count1Temp = orgIdMapCounter1.get(orgId);
		count1 = count1 + count1Temp;
		int countTemp = count0Temp + count1Temp;
		count = count + countTemp;
		StringBuilder tr = new StringBuilder();
		tr.append("<tr  align=\"left\">");
		tr.append("<td  class=\"scrollRowThead\" height=\"" + height
				+ "\"  valign=\"middle\" align=\"center\">"+colCount+"</td>" +
				"<td  height=\"" + height
				+ "\"  valign=\"middle\">"
				+ orgIdMapOrgName.get(orgId) + "</td>"
				+ "<td  valign=\"middle\">"
				+ count0Temp + "</td>"
				+ "<td  valign=\"middle\" "+(count1Temp>0?"style=\"color:red;\"":"")+">"
				+ count1Temp + "</td>"
				+ "<td  valign=\"middle\">"
				+ countTemp + "</td>");
		tr.append("</tr>");
		tableContent.append(tr);
		colCount++;//行数加1
	    }
	    //添加总计信息
	    tableContent.append("<tr  align=\"left\" >" +
		    "<td   class=\"scrollRowThead\" height=\"" + height
			+ "\"  valign=\"middle\" align=\"center\">"+colCount+"</td>" +
		"<td  height=\"" + height
		+ "\"  valign=\"middle\" style=\"color:blue;\"> 总计 </td>"
		+ "<td  valign=\"middle\" style=\"color:blue;\">"
		+ count0 + "</td>"
		+ "<td  valign=\"middle\" style=\"color:blue;\">"
		+ count1 + "</td>"
		+ "<td  valign=\"middle\" style=\"color:blue;\">"
		+ count + "</td>"
		+"</tr>");
	    
	    String tableHeader = "<table border=\"0\" class=\"scrollTable\"  valign=\"middle\" align=\"center\" cellspacing=\"0\" width=\"100%\" height=\""
		+ (colCount * heigftUnit) + "px"
		+ "\""
		+ "bordercolordark=\"#FFFFFF\" bordercolorlight=\"#000000\""
		+ "bordercolor=\"#333300\" cellpadding=\"2\">";
	    String tableFooter = "</table>";
	    String table = tableHeader + tableContent + tableFooter;
	    System.out.println("table:" + table);
	    return table;
	}
	public String limitTime() throws Exception{
	    Long start = System.currentTimeMillis();
		String captionStr = "公文数限时统计";
		try {
			User curuser = userService.getCurrentUser();
			DocumentViewParamter paramter = new DocumentViewParamter();
			paramter.setMytype(mytype);
			paramter.setState("0");
			paramter.setOrgIds(orgIds);
			paramter.setStartUserName(startUserName);
			paramter.setExcludeWorkflowType(excludeWorkflowType);
			paramter.setWorkflowType(workflowType);
			paramter.setCurUserId(curuser.getUserId());
			paramter.setCurUserOrgIg(curuser.getOrgId());
			List<String> itemsList = new LinkedList<String>();
			itemsList.add("processInstanceId");
			itemsList.add("processTimeout");
			paramter.setItemsList(itemsList);
			List<Object[]> processList = documentViewManager
					.getwjtjForList(paramter);
			if (processList == null) {
				processList = new ArrayList<Object[]>();
			}

			System.out
					.println("DocumentHandleAction wjtj() processList.size():"
							+ processList.size());
			List<UserAndOrgInfo> managerUserAndOrgInfoForList = documentViewManager.getManagerUserAndOrgInfoForList(curuser
				.getUserId());
			String charXML = "0";
			String table = "0";
			if(managerUserAndOrgInfoForList != null && !managerUserAndOrgInfoForList.isEmpty()){
			    DocumentViewUtil documentviewutil = new DocumentViewUtil(managerUserAndOrgInfoForList);
			    for (Object[] objs : processList) {
				String processTimeout = StringUtil.castString(objs[1]);
				String mainActorId = StringUtil.castString(objs[2]);
				documentviewutil.addUserId(mainActorId,processTimeout);
			    }
			    charXML = gencharlimitTimeXML(documentviewutil);
			    table = genlimitTimeTable(documentviewutil);
			}
			getRequest().setAttribute("charXML", charXML);
			getRequest().setAttribute("table", table);
		} catch (SystemException e) {
			logger.error(e.getMessage());
		} catch (Exception ex) {
			logger.error("获取公文限时办理情况出现异常,异常信息：" + ex.getMessage());
		}
		logger.info("\r\n\t\t{@@@@@@} "+captionStr+"柱状图统计耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		return "limitTime";
	    
	}
	
	/**
	 * @author 严建
	 * @createTime Sep 14, 2011
	 * @description 公文限时办理情况
	 * @throws Exception
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public String limitTime20120417() throws Exception {
		try {
			List<String> itemsList = new ArrayList<String>();// 要查询的属性
			itemsList.add("startUserName");
			itemsList.add("processStartDate"); // 流程实例开始时间
			itemsList.add("processEndDate"); // 流程实例结束时间
			itemsList.add("startUserId");
			itemsList.add("processInstanceId");
			itemsList.add("processSuspend");

			Map<String, Object> paramsMap = new HashMap<String, Object>();// 查询条件设置
			paramsMap.put("processTypeId", 3);
			Map<String, String> orderMap = new HashMap<String, String>();// 要排序的属性（“0”：升序；“1”：降序）
			List<Object[]> processList = manager
					.getProcessInstanceByConditionForList(itemsList, paramsMap,
							orderMap, "", "", "", null);

			Map orgUnitWithIdAsKey = null;
			System.out.println(processList.size());
			int count = 0;
			orgUnitWithIdAsKey = new HashMap();// 存放界面显示的信息

			if (processList != null && !processList.isEmpty()) {
				for (int i = 0; i < processList.size(); i++) {
					Object[] objs = processList.get(i);
					if (objs[2] == null) {// 流程未结束objs[2]：流程结束时间

						Date processStartDate = (Date) objs[1];
						String startUserId = null;
						String startUserName = null;
						User user = null;
						Organization org = null;
						String orgName = null;
						String orgId = null;
						Long orgSequence = null;
						Map orgUnit = null;
						Map personUnitWithIdAsKey = null;
						Map personUnit = null;
						String timeKey = null;
						if (objs[3] == null) {// 子流程 objs[3]：发起人，子流程不统计
							continue;
							// Long instanceId = (Long)objs[4];
							// List<Object[]> parentList =
							// workflowService.getMonitorParentInstanceIds(instanceId);
							// if(parentList == null || parentList.isEmpty()){
							// continue;
							// }
							// Object[] panent =
							// (Object[])parentList.get(parentList.size()-1);
							// Long panentInstanceId = (Long)panent[0];
							// paramsMap.put("processInstanceId",
							// panentInstanceId);
							// List<Object[]> panentProcessList=
							// manager.getProcessInstanceByConditionForList(itemsList,
							// paramsMap, orderMap,"","","",null);
							// Object[] panentProcess =
							// panentProcessList.get(0);
							// startUserId = (String)panentProcess[3];
							// startUserName = (String)panentProcess[0];

						} else {
							startUserId = (String) objs[3];
							startUserName = (String) objs[0];
						}
						count++;
						org = userService
								.getUserDepartmentByUserId(startUserId);
						user = userService.getUserInfoByUserId(startUserId);

						if (org.getRest1() != null
								&& org.getRest1().equals("1")) {
							continue;
						}

						orgName = org.getOrgName();
						orgId = org.getOrgId();
						orgSequence = org.getOrgSequence();
						long time = new Date().getTime()
								- processStartDate.getTime();
						long hours = time / (1000 * 60 * 60);
						if (hours <= 24) {// 在24小时内
							timeKey = "in24Hours";
						} else if (hours <= 48) {// 在48小时内
							timeKey = "in48Hours";
						} else {// 超过48小时
							timeKey = "out48Hours";
						}

						String orgIdAndSequence = orgId + "@" + orgSequence;
						String userIdAndSequence = user.getUserId() + "@"
								+ user.getUserSequence();

						if (!orgUnitWithIdAsKey.containsKey(orgIdAndSequence)) { // 不存在该部门ID
							orgUnit = new HashMap();
							orgUnit.put("orgName", orgName);
							personUnitWithIdAsKey = new HashMap();
						} else {// 存在该部门ID
							orgUnit = (Map) orgUnitWithIdAsKey
									.get(orgIdAndSequence);
							personUnitWithIdAsKey = (Map) orgUnit
									.get("personInfo");
						}
						if (!personUnitWithIdAsKey
								.containsKey(userIdAndSequence)) {// 不存在该人员ID
							personUnit = new HashMap();
							personUnit.put("startUserName", startUserName);
							personUnit.put("total", 0);
							personUnit.put("in24Hours", 0);
							personUnit.put("in48Hours", 0);
							personUnit.put("out48Hours", 0);
						} else {// 存在该人员ID
							personUnit = (Map) personUnitWithIdAsKey
									.get(userIdAndSequence);
						}
						Integer times = (Integer) personUnit.get(timeKey);
						times++;
						personUnit.put(timeKey, times.intValue());
						Integer total = (Integer) personUnit.get("total");
						total++;
						personUnit.put("total", total.intValue());
						personUnitWithIdAsKey
								.put(userIdAndSequence, personUnit);
						orgUnit.put("personInfo", personUnitWithIdAsKey);
						orgUnitWithIdAsKey.put(orgIdAndSequence, orgUnit);
					}
				}
			}

			String tableHeight = "";
			int colCount = 0;

			int allTotal = 0; // 总计

			/* 页面显示 */

			String charHeader = "<chart palette='2' caption='公文限时办理情况' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1'"
					+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
			String categories = "<categories >";
			String totaldataset = "<dataset seriesName='办理总数' color='1328c9'>";
			String in24Hoursdataset = "<dataset seriesName='24小时内办理' color='13c930'>";
			String in48Hoursdataset = "<dataset seriesName='48小时内办理' color='cff80e'>";
			String out48Hoursdataset = "<dataset seriesName='超过48小时办理' color='fa2626'>";
			// String averagedataset = "<dataset lineThickness='3'
			// seriesName='平均件数' anchorRadius='3' color='da26fa' anchorSides='6'
			// anchorBgColor='da26fa' parentYAxis='S'>";
			StringBuilder tableContent = new StringBuilder().append("");
			int heigftUnit = 35;
			String height = "";
			height = heigftUnit + "px";
			if (orgUnitWithIdAsKey != null && !orgUnitWithIdAsKey.isEmpty()) {
				Object[] ouwiakKeyArray = orgUnitWithIdAsKey.keySet().toArray();
				List<Object> listKey = new LinkedList<Object>();
				for (int fi = 0; fi < ouwiakKeyArray.length; fi++) {
					listKey.add(ouwiakKeyArray[fi]);
				}

				Collections.sort(listKey, new Comparator<Object>() {
					public int compare(Object arg0, Object arg1) {
						Long sequence1 = new Long(arg0.toString().split("@")[1]);
						Long sequence2 = new Long(arg1.toString().split("@")[1]);
						Long key1;
						if (sequence1 != null) {
							key1 = sequence1;
						} else {
							key1 = Long.MAX_VALUE;
						}
						Long key2;
						if (sequence2 != null) {
							key2 = sequence2;
						} else {
							key2 = Long.MAX_VALUE;
						}
						return key1.compareTo(key2);
					}

				});
				ouwiakKeyArray = listKey.toArray();
				for (int fi = 0; fi < ouwiakKeyArray.length; fi++) {
					int totalXJ = 0;
					int in24HoursXJ = 0;
					int in48HoursXJ = 0;
					int out48HoursXJ = 0;
					Map orgUnit = (Map) orgUnitWithIdAsKey
							.get(ouwiakKeyArray[fi]);
					String oraName = (String) orgUnit.get("orgName");
					if (oraName.equals("zzb")) {
						// continue;
					}
					categories = categories + "<category label='" + oraName
							+ "' />";

					StringBuilder tr = new StringBuilder();
					Map personUnitWithIdAsKey = (Map) orgUnit.get("personInfo");
					Object[] puwiakKeyArray = personUnitWithIdAsKey.keySet()
							.toArray();
					colCount = colCount + (puwiakKeyArray.length + 1);
					String col0Height = "";
					col0Height = ((puwiakKeyArray.length + 1) * heigftUnit)
							+ "px";
					tr.append("<tr  align=\"center\">").append(
							"<td height=\"" + col0Height
									+ "\"  valign=\"middle\" rowspan=\""
									+ (puwiakKeyArray.length + 2) + "\">"
									+ oraName + "</td>");
					tr.append("</tr>");

					List<Object> userlistKey = new LinkedList<Object>();
					for (int ufi = 0; ufi < puwiakKeyArray.length; ufi++) {
						userlistKey.add(puwiakKeyArray[ufi]);
					}
					Collections.sort(userlistKey, new Comparator<Object>() {
						public int compare(Object arg0, Object arg1) {
							Long sequence1 = new Long(arg0.toString()
									.split("@")[1]);
							Long sequence2 = new Long(arg1.toString()
									.split("@")[1]);
							Long key1;
							if (sequence1 != null) {
								key1 = sequence1;
							} else {
								key1 = Long.MAX_VALUE;
							}
							Long key2;
							if (sequence2 != null) {
								key2 = sequence2;
							} else {
								key2 = Long.MAX_VALUE;
							}
							return key1.compareTo(key2);
						}

					});
					puwiakKeyArray = userlistKey.toArray();

					for (int si = 0; si < puwiakKeyArray.length; si++) {
						Map personUnit = (Map) personUnitWithIdAsKey
								.get(puwiakKeyArray[si]);
						totalXJ = totalXJ + ((Integer) personUnit.get("total"));
						in24HoursXJ = in24HoursXJ
								+ ((Integer) personUnit.get("in24Hours"));
						in48HoursXJ = in48HoursXJ
								+ ((Integer) personUnit.get("in48Hours"));
						out48HoursXJ = out48HoursXJ
								+ ((Integer) personUnit.get("out48Hours"));

						tr.append("<tr  align=\"center\">");
						tr.append("<td  height=\"" + height
								+ "\"  valign=\"middle\">"
								+ personUnit.get("startUserName") + "</td>"
								+ "<td  valign=\"middle\">"
								+ personUnit.get("total") + "</td>"
								+ "<td  valign=\"middle\">"
								+ personUnit.get("in24Hours") + "</td>"
								+ "<td  valign=\"middle\">"
								+ personUnit.get("in48Hours") + "</td>"
								+ "<td  valign=\"middle\">"
								+ personUnit.get("out48Hours") + "</td>");
						tr.append("</tr>");

						/*
						 * totaldataset = totaldataset + "<set
						 * value='"+personUnit.get("total")+"' />";
						 * in24Hoursdataset = in24Hoursdataset + "<set
						 * value='"+personUnit.get("in24Hours")+"' />";
						 * in48Hoursdataset = in48Hoursdataset + "<set
						 * value='"+personUnit.get("in48Hours")+"' />";
						 * out48Hoursdataset = out48Hoursdataset + "<set
						 * value='"+personUnit.get("out48Hours")+"' />";
						 * averagedataset = averagedataset + "<set
						 * value='"+personUnit.get("total")+"' />";
						 */
					}

					totaldataset = totaldataset + "<set value='" + totalXJ
							+ "' />";
					in24Hoursdataset = in24Hoursdataset + "<set value='"
							+ in24HoursXJ + "' />";
					in48Hoursdataset = in48Hoursdataset + "<set value='"
							+ in48HoursXJ + "' />";
					out48Hoursdataset = out48Hoursdataset + "<set value='"
							+ out48HoursXJ + "' />";
					// averagedataset = averagedataset + "<set
					// value='"+totalXJ+"' />";

					tr.append("<tr  align=\"center\">");
					tr
							.append("<td  height=\""
									+ height
									+ "\" valign=\"middle\"><font color=\"red\">小计</font></td>"
									+ "<td  valign=\"middle\"><font color=\"red\">"
									+ totalXJ
									+ "</font></td>"
									+ "<td  valign=\"middle\"><font color=\"red\">"
									+ in24HoursXJ
									+ "</font></td>"
									+ "<td  valign=\"middle\"><font color=\"red\">"
									+ in48HoursXJ
									+ "</font></td>"
									+ "<td  valign=\"middle\"><font color=\"red\">"
									+ out48HoursXJ + "</font></td>");
					tr.append("</tr>");
					tableContent.append(tr);
					allTotal += totalXJ; // 总计 = 所有的小计之和
				}
			}
			tableHeight = ((colCount) * heigftUnit) + "px";

			String tableHeader = "<table border=\"1\"  valign=\"middle\" align=\"center\" cellspacing=\"0\" width=\"100%\" height=\""
					+ tableHeight
					+ "\""
					+ "bordercolordark=\"#FFFFFF\" bordercolorlight=\"#000000\""
					+ "bordercolor=\"#333300\" cellpadding=\"2\">"
					+ "<tr height=\"5%\"  align=\"center\" >"
					+ "<th width=\"120px\" >处室</th>"
					+ "<th width=\"120px\" >承办人员</th>"
					+ "<th width=\"120px\" >办理总数</th>"
					+ "<th width=\"120px\" >24小时内办理</th>"
					+ "<th width=\"120px\" >48小时内办理</th>"
					+ "<th width=\"120px\" >超过48小时办理</th>" + "</tr>";
			StringBuilder tableFooter = new StringBuilder();
			tableFooter.append("<tr align=\"right\">");
			tableFooter
					.append("<td  height=\""
							+ height
							+ "\"  valign=\"middle\" colspan=\"6\"><font color=\"red\">总计："
							+ allTotal + "</font></td>");
			tableFooter.append("</tr>");
			tableFooter.append("</table>");
			String table = tableHeader + tableContent.toString() + tableFooter;

			categories += "</categories >";
			totaldataset += "</dataset>";
			in24Hoursdataset += "</dataset>";
			in48Hoursdataset += "</dataset>";
			out48Hoursdataset += "</dataset>";
			// averagedataset += "</dataset>";
			String charXML = charHeader + categories + totaldataset
					+ in24Hoursdataset + in48Hoursdataset + out48Hoursdataset
					// + averagedataset
					+ "</chart>";
			System.out.println(table);
			System.out.println(charXML);
			getRequest().setAttribute("charXML", charXML);
			getRequest().setAttribute("table", table);
		} catch (SystemException e) {
			logger.error(e.getMessage());
		} catch (Exception ex) {
			logger.error("获取公文限时办理情况出现异常,异常信息：" + ex.getMessage());
		}
		return "limitTime";
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		if (startUserName != null && !"".equals(startUserName)) {
			try {
				startUserName = URLDecoder.decode(startUserName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.startUserName = startUserName;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return this.basemanager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public List getAnnalList() {
		return annalList;
	}

	public void setAnnalList(List annalList) {
		this.annalList = annalList;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getProcessDefinitionNames() {
		return processDefinitionNames;
	}

	public void setProcessDefinitionNames(String processDefinitionNames) {
		this.processDefinitionNames = processDefinitionNames;
	}

	public String getSearchDateString() {
		return searchDateString;
	}

	public void setSearchDateString(String searchDateString) {
		this.searchDateString = searchDateString;
	}

	public Map<String, String> getProcessDefinitionNameMapIds() {
		return processDefinitionNameMapIds;
	}

	public void setProcessDefinitionNameMapIds(
			Map<String, String> processDefinitionNameMapIds) {
		this.processDefinitionNameMapIds = processDefinitionNameMapIds;
	}

	public String getMytype() {
		return mytype;
	}

	public void setMytype(String mytype) {
		this.mytype = mytype;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

}