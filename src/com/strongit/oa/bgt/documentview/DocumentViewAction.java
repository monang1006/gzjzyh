package com.strongit.oa.bgt.documentview;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bgt.documentview.bo.DocumentViewParamter;
import com.strongit.oa.bgt.documentview.manager.DocumentViewManager;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.util.StringUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.workflowDesign.action.util.ProcessInstanceDataUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 6, 2012 1:24:09 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.bgt.documentview.DocumentViewAction
 */
public class DocumentViewAction extends BaseActionSupport {
	private Page documentViewPage = new Page(FlexTableTag.MAX_ROWS, true);

	private String isFind;

	private String mytype;

	private String orgIds;

	private String startUserName;

	private String state;

	private String excludeWorkflowType;

	private String workflowType;

	private String processName;

	private String processWorkflowNames;
	private String searchDateString;
	private Date searchDate; // 启动时间
	@SuppressWarnings("unused")
	private Map<String, String> OrgNameMapIds;
	@Autowired
	IUserService userService;

	@Autowired
	DocumentViewManager documentViewManager;

	@Autowired
	ICustomUserService customUserService;

	@Autowired
	IWorkflowService workflowService;
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	private String doneYear;//办结年份
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 获取待办文件数
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 6, 2012 1:15:28 PM
	 */
	public String getTodoDocCount() {
		logger.info("\r\n\t\t{@@@@@@} 开始处理待办文件数统计(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		User curuser = userService.getCurrentUser();
		DocumentViewParamter paramter = new DocumentViewParamter();
		paramter.setMytype(mytype);
		paramter.setState("0");
		paramter.setOrgIds(orgIds);
		paramter.setWorkflowType("3");// 办件登记类型：“370020”  //收文处理类型：3
		paramter.setIsSuspended("0");
		paramter.setCurUserId(curuser.getUserId());
		paramter.setCurUserOrgIg(curuser.getOrgId());
		paramter.setItemsList(documentViewManager.getToSelectItems());
//		ProcessInstanceDataUtil processInstanceDataUtil = new ProcessInstanceDataUtil(
//				mytype);
//		paramter.setItemsList(processInstanceDataUtil.getToSelectItems());
//		// 显示令牌不挂起的流程
//		StringBuilder customFromItems = new StringBuilder()
//				.append(" org.jbpm.graph.exe.Token tokencustom ");
//		paramter.setCustomFromItems(customFromItems);
//		StringBuilder customQuery = new StringBuilder()
//				.append(" @processInstanceId = tokencustom.processInstance.id and  tokencustom.isSuspended = '0'");
//		paramter.setCustomQuery(customQuery);
		int count = documentViewManager.getTodowjtjTotal(paramter);
		logger.info("\r\n\t\t{@@@@@@} 待办文件数："+count+",统计耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		return this.renderText(count + "");
	}

	/**
	 * 获取在办文件数
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 6, 2012 1:16:33 PM
	 */
	public String getDoingDocCout() {
		logger.info("\r\n\t\t{@@@@@@} 开始处理在办文件数统计(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		User curuser = userService.getCurrentUser();
		DocumentViewParamter paramter = new DocumentViewParamter();
		paramter.setMytype(mytype);
		paramter.setState("0");
		paramter.setOrgIds(null);
		paramter.setExcludeWorkflowType(excludeWorkflowType);
		paramter.setWorkflowType(workflowType);
		paramter.setCurUserId(curuser.getUserId());
		paramter.setCurUserOrgIg(curuser.getOrgId());
		ProcessInstanceDataUtil processInstanceDataUtil = new ProcessInstanceDataUtil(
				mytype);
		paramter.setItemsList(processInstanceDataUtil.getToSelectItems());
		int count = documentViewManager.getwjtjTotal(paramter);
		logger.info("\r\n\t\t{@@@@@@} 在办文件数："+count+",统计耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		return this.renderText(count + "");
	}

	/**
	 * 获取办结文件数
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 6, 2012 1:17:03 PM
	 */
	public String getDoneDocCount() {
		logger.info("\r\n\t\t{@@@@@@} 开始处理办结文件数统计(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		User curuser = userService.getCurrentUser();
		DocumentViewParamter paramter = new DocumentViewParamter();
		if (doneYear != null && !"".equals(doneYear)
				&& !"undefined".equals(doneYear)) {//-1:表示去当前年份的数据
			paramter.setDoneYear(Integer.parseInt(doneYear));
		}
		paramter.setMytype(mytype);
		paramter.setState("1");
		paramter.setOrgIds(orgIds);
		paramter.setExcludeWorkflowType(excludeWorkflowType);
		paramter.setWorkflowType(workflowType);
		paramter.setCurUserId(curuser.getUserId());
		paramter.setCurUserOrgIg(curuser.getOrgId());
		ProcessInstanceDataUtil processInstanceDataUtil = new ProcessInstanceDataUtil(
				mytype);
		paramter.setItemsList(processInstanceDataUtil.getToSelectItems());
		int count = documentViewManager.getwjtjTotal(paramter);
		logger.info("\r\n\t\t{@@@@@@} 办结文件数："+count+",统计耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		return this.renderText(count + "");
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public String mainFrameOrg() throws Exception {
		return "mainFrameOrg";
	}
	
	@SuppressWarnings( { "unchecked", "deprecation" })
	public String monitorList() throws Exception {
		logger.info("\r\n\t\t{@@@@@@} 开始处理代办文件列表显示统计(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		if (isFind != null && isFind.equals("1")) { // 执行查询操作，页面自动跳转到第一页
			documentViewPage.setPageNo(1);
		}
		User curuser = userService.getCurrentUser();
		DocumentViewParamter paramter = new DocumentViewParamter();
		paramter.setMytype(mytype);
		paramter.setState("0");
		paramter.setOrgIds(orgIds);
		paramter.setWorkflowType("3");// 办件登记类型：“370020” //收文处理类型：3
		paramter.setIsSuspended("0");
		paramter.setCurUserId(curuser.getUserId());
		paramter.setCurUserOrgIg(curuser.getOrgId());
		paramter.setBusinessNmae(processName);
		
		if (searchDate != null && !"".equals(searchDate)) {
			Date endtime = new Date();
			endtime.setTime(searchDate.getTime());
			endtime.setHours(23);
			endtime.setMinutes(59);
			// paramsMap.put("processStartDateEnd", searchDate);
			paramter.setProcessStartDateStart(searchDate);
			paramter.setProcessStartDateEnd(endtime);
		}
		paramter.setItemsList(documentViewManager.getToSelectItems());
		
		documentViewPage = documentViewManager.getTodowjtjForPage(documentViewPage, paramter);
		documentViewManager.getResult(documentViewPage);
		logger.info("\r\n\t\t{@@@@@@} 代办文件列表显示统计耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		if ("dept".equals(mytype)) {
			return "deptmonitorList";
		}
		if ("org".equals(mytype)) {
			return "orgmonitorList";
		}
		if ("user".equals(mytype)) {
			return "usermonitorList";
		}
		return "monitorList";
	}
	/**
	 * 获取待办列表
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 6, 2012 1:17:03 PM
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		if (isFind != null && isFind.equals("1")) { // 执行查询操作，页面自动跳转到第一页
			documentViewPage.setPageNo(1);
		}
		// TODO Auto-generated method stub
		User curuser = userService.getCurrentUser();
		DocumentViewParamter paramter = new DocumentViewParamter();
		paramter.setMytype(mytype);
		paramter.setState("0");
		state = "0";
		paramter.setOrgIds(orgIds);
		paramter.setStartUserName(startUserName);
		paramter.setWorkflowType("370020");// 办件登记类型：“370020”
		paramter.setCurUserId(curuser.getUserId());
		paramter.setCurUserOrgIg(curuser.getOrgId());
		paramter.setBusinessNmae(processName);
		paramter.setWorkflowName(processWorkflowNames);
		// 显示令牌不挂起的流程
		StringBuilder customFromItems = new StringBuilder()
				.append(" org.jbpm.graph.exe.Token tokencustom ");
		paramter.setCustomFromItems(customFromItems);
		StringBuilder customQuery = new StringBuilder()
				.append(" @processInstanceId = tokencustom.processInstance.id and  tokencustom.isSuspended = '0'");
		paramter.setCustomQuery(customQuery);
		ProcessInstanceDataUtil processInstanceDataUtil = new ProcessInstanceDataUtil(
				mytype);
		paramter.setItemsList(processInstanceDataUtil.getToSelectItems());
		documentViewPage = documentViewManager.getwjtjForPage(documentViewPage, paramter);
		processInstanceDataUtil.getResult(documentViewPage,adapterBaseWorkflowManager);
		return SUCCESS;
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

	public String getExcludeWorkflowType() {
		return excludeWorkflowType;
	}

	public void setExcludeWorkflowType(String excludeWorkflowType) {
		this.excludeWorkflowType = excludeWorkflowType;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}


	public String getIsFind() {
		return isFind;
	}

	public void setIsFind(String isFind) {
		this.isFind = isFind;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		if (!"".equals(processName) && null != processName) {
			try {
				processName = java.net.URLDecoder.decode(processName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.processName = processName;
	}

	public String getProcessWorkflowNames() {
		return processWorkflowNames;
	}

	public void setProcessWorkflowNames(String processWorkflowNames) {
		if (processWorkflowNames != null & !processWorkflowNames.equals("")) {
			try {
				processWorkflowNames = java.net.URLDecoder.decode(
						processWorkflowNames, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		this.processWorkflowNames = processWorkflowNames;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getSearchDateString() {
		return searchDateString;
	}

	public void setSearchDateString(String searchDateString) {
		this.searchDateString = searchDateString;
	}

	public Map<String, String> getOrgNameMapIds() {
		Map<String,String> OrgNameMapIds = new HashMap<String, String>();
		if ("org".equals(mytype)) {
			List orgInfo = customUserService
					.getManageOrgInfo(userService.getCurrentUser().getUserId());
			try {
				for (Object org : orgInfo) {
					Object[] orgs = (Object[]) org;
					OrgNameMapIds.put(StringUtil.castString(orgs[1]), StringUtil.castString(orgs[0]));
				}
				this.OrgNameMapIds = OrgNameMapIds;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return OrgNameMapIds;
	}

	public void setOrgNameMapIds(Map<String, String> orgNameMapIds) {
		OrgNameMapIds = orgNameMapIds;
	}

	public Page getDocumentViewPage() {
		return documentViewPage;
	}

	public void setDocumentViewPage(Page documentViewPage) {
		this.documentViewPage = documentViewPage;
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

}
