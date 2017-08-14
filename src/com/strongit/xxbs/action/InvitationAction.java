package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.InvitationDto;
import com.strongit.xxbs.entity.TInfoBaseAppoint;
import com.strongit.xxbs.entity.TInfoBaseAppointTo;
import com.strongit.xxbs.service.IAppointToService;
import com.strongit.xxbs.service.IInvitationService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.JdbcInvitationService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class InvitationAction extends BaseActionSupport<TInfoBaseAppoint>
{
	private static final long serialVersionUID = 1L;
	
	private IInvitationService invitationService;
	private IOrgService orgService;
	private IAppointToService appointToService;
	private JdbcInvitationService jdbcInvitationService;
	private TInfoBaseAppoint model = new TInfoBaseAppoint();
	
	@Autowired private IUserService userService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseAppoint> page;
	private Page<InvitationDto> pagePl;
	private Page<TUumsBaseOrg> page1;
	private String op;
	private String toId;
	private Map<String, String> orgNames;
	private String[] checkedOrgIds;
	private String[] orgIds;
	private String selectOrg;
	private Boolean isSinceToday = false;
	private String submitStatus = Publish.ALL;
	private String checkId ="";
	private List<TUumsBaseOrg> orgList;
	private String loadTitle = "";
	private String loadId = "";
	
	@Autowired
	public void setJdbcInvitationService(JdbcInvitationService jdbcInvitationService) {
		this.jdbcInvitationService = jdbcInvitationService;
	}

	@Autowired
	public void setInvitationService(IInvitationService invitationService)
	{
		this.invitationService = invitationService;
	}

	@Autowired
	public void setOrgService(IOrgService orgService)
	{
		this.orgService = orgService;
	}

	@Autowired
	public void setAppointToService(IAppointToService appointToService)
	{
		this.appointToService = appointToService;
	}

	public TInfoBaseAppoint getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = invitationService.deleteAppoint(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		String ret = INPUT;
		
		orgNames = orgService.getOrgNames();
		
		if(op.equals("add"))
		{
			UserInfo userInfo = (UserInfo) getUserDetails();
			model.setAptUserid(userInfo.getUserId());
			model.setAptIsAllOrg(Publish.ALL_ORG);
		}
		else if("edit".equals(op) || "view".equals(op))
		{
			model = invitationService.getAppoint(toId);
			String title = model.getAptTitle();
			title = title.replaceAll( "\"", "\\“" );
			model.setAptTitle(title);
			if(Publish.PART_ORG.equals(model.getAptIsAllOrg()))
			{
				//根据约稿信息查询所有约稿用户对象
				List<TInfoBaseAppointTo> rets = appointToService.getATs(model.getAptId());
				if(rets.size()>0)
				{
					Set<String> cOrgIds = new HashSet<String>();
					for(TInfoBaseAppointTo one : rets)
					{
						cOrgIds.add(one.getOrgId());
					}
					checkedOrgIds = cOrgIds.toArray(new String[cOrgIds.size()]);
					orgList = orgService.getOrgsById(checkedOrgIds);
					if(orgList.size()>0){
					for(int j=0;j<orgList.size();j++){
						loadTitle+=orgList.get(j).getOrgName()+",";
						loadId+= orgList.get(j).getOrgId()+",";
					}
					loadTitle=loadTitle.substring(0, loadTitle.length()-1);
					loadId=loadId.substring(0, loadId.length()-1);
					}
					if(checkedOrgIds.length>0){
					for(int i=0;i<checkedOrgIds.length;i++){
						checkId+=checkedOrgIds[i]+",";
						
					}
					checkId=checkId.substring(0, checkId.length()-1);
					}
				}
			}
			
			if("view".equals(op))
			{
				ret = "view";
			}
		}
		return ret;
	}

	@Override
	public String list() throws Exception
	{
		return SUCCESS;
	}


	@Override
	protected void prepareModel() throws Exception
	{
	}

	@Override
	public String save() throws Exception
	{
		if(Publish.ALL_ORG.equals(model.getAptIsAllOrg()))
		{
			invitationService.saveAppoint(model);
		}
		else if(Publish.PART_ORG.equals(model.getAptIsAllOrg()))
		{
			String orgList[] = new String[orgIds[0].split(",").length];
			orgList = orgIds[0].split(",");
			
			invitationService.saveAppoint(model, orgList);
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}
	
	public String showList() throws Exception
	{
		page = new Page<TInfoBaseAppoint>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			Map<String, String> search = new HashMap<String, String>();			
			String aptTitle = getRequest().getParameter("aptTitle");
			aptTitle = aptTitle.replace("%", "\'\'%");
			String aptDate = getRequest().getParameter("aptDate");
			String aptDuedate = getRequest().getParameter("aptDuedate");
			search.put("aptTitle", aptTitle);
			search.put("aptDate", aptDate);
			search.put("aptDuedate", aptDuedate);
			page = invitationService.findAppoints(page, search);
		}
		else if(isSinceToday == true)
		{
			UserInfo userInfo = (UserInfo) getUserDetails();
			String orgId = userInfo.getOrgId();		
			//过滤约稿Page
			page = invitationService.getAppoints(page, orgId, true);
		}
		else
		{
			//未过滤
			page = invitationService.getAppoints(page);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		System.out.println(page.getTotalPages());
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseAppoint> result = page.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(TInfoBaseAppoint one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("aptId", one.getAptId());
				obj.put("aptTitle", one.getAptTitle());
				obj.put("aptDate", one.getAptDate().toString());
				obj.put("aptDuedate", one.getAptDuedate().toString());
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	/**
	 * 查询约稿列表
	 * @return
	 * @throws Exception
	 */
	public String showJdbcList() throws Exception
	{
		pagePl = new Page<InvitationDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		Map<String, String> search = new HashMap<String, String>();
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String aptTitle = getRequest().getParameter("aptTitle");
			aptTitle = aptTitle.replace("%", "\'\'%");
			String aptDate = getRequest().getParameter("aptDate");
			String aptDuedate = getRequest().getParameter("aptDuedate");
			search.put("aptTitle", aptTitle);
			search.put("aptDate", aptDate);
			search.put("aptDuedate", aptDuedate);
			if(isSinceToday == true){
				UserInfo userInfo = (UserInfo) getUserDetails();
				String orgId = userInfo.getOrgId();		
				//过滤约稿Page
				pagePl = jdbcInvitationService.getLaseAppoints(pagePl, orgId, true,search);
			}
			else{
				pagePl = jdbcInvitationService.findAppoints(pagePl, search);
			}
		}
		else if(isSinceToday == true)
		{
			UserInfo userInfo = (UserInfo) getUserDetails();
			String orgId = userInfo.getOrgId();		
			//过滤约稿Page
			pagePl = jdbcInvitationService.getLaseAppoints(pagePl, orgId, true,search);
		}
		else
		{
			//未过滤
			pagePl = jdbcInvitationService.getInvitations(pagePl);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<InvitationDto> result = pagePl.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(InvitationDto one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("aptId", one.getAptId());
				obj.put("aptTitle", one.getAptTitle());
				obj.put("aptDate", sdfDate.format(one.getAptDate()));
				obj.put("aptDuedate", sdfDate.format(one.getAptDuedate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	
	public String findOrg() throws Exception
	{
		page1 = new Page<TUumsBaseOrg>(unitpage, true);
		page1.setPageNo(curpage);
		page1.setPageSize(unitpage);
		page1.setOrder(sord);
		page1.setOrderBy(sidx);
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String orgName = getRequest().getParameter("orgName");
			orgName = orgName.replace("%", "\'\'%");
			page1 = orgService.findOrgByTitle(page1, orgName);
		}
		else
		{
			page1 = orgService.findAllOrg(page1);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page1.getTotalPages()==-1){
			page1.setTotalCount(0);
		}
		jsonObj.put("totalpages", page1.getTotalPages());
		jsonObj.put("totalrecords", page1.getTotalCount());
		List<TUumsBaseOrg> result = page1.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(TUumsBaseOrg one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("orgId", one.getOrgId());
				obj.put("orgName", one.getOrgName());
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String tree() throws Exception
	{
		loadId = ","+loadId+","; 
		getRequest().setAttribute(loadId, loadId);
		return "tree";
	}
	
	public String showTree() throws Exception
	{
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		List<TUumsBaseOrg> allList = userService.getSelfAndChildOrgsByOrgSyscodeByHa("001999", "0", "8a928a703bb11098013bb6756e9a004c");
		int topOrgcodelength = 6;
		TreeNode node;
		String parentCode;
		String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		for(TUumsBaseOrg org : allList)
		{
			node = new TreeNode();
			node.setId(org.getOrgSyscode());
			parentCode = this.getParentCode(org.getOrgSyscode(), codeRule);
			node.setPid(org.getOrgSyscode().length() == topOrgcodelength?"-1":parentCode);
			node.setComplete(true);
			//node.setIsexpand(org.getOrgSyscode().length() == topOrgcodelength?true:false);
			node.setIsexpand(true);
			if(org.getOrgNature()!=null){
			if(!org.getOrgNature().equals("5")){
				if(org.getOrgName().equals("公文传输单位")){
					node.setShowcheck(false);
				}else{
			    node.setShowcheck(true);
				}
			}
			else{
				node.setShowcheck(false);
			}
			}
			else{
				node.setShowcheck(false);
			}
			node.setText(org.getOrgName());
			node.setValue(org.getOrgId());
			nodeLst.add(node);
		}

		String jsonTreeNode = JsonUtil.fromTreeNodes(nodeLst);
		
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonTreeNode);
		return null;
	}
	
	
	/**
	 * 得到父级代码。
	 * 
	 * @param codeRule -
	 *            编码规则
	 * @param code -
	 *            代码
	 * @return String - 父级代码,如果为最顶级则返回""
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	private String getParentCode(String code, String codeRule)
			throws Exception {
		String parentCode = null;
		if (code != null && codeRule != null) {
			// 校验代码编码规则
			// 分析编码规则
			List<Integer> lstCodeRule = new ArrayList<Integer>();
			for (int i = 0; i < codeRule.length(); i++) {
				String str = codeRule.substring(i, i + 1);
				lstCodeRule.add(new Integer(str));
			}
			int codeLength = code.length();
			int count = 0;
			for (Iterator<Integer> iter = lstCodeRule.iterator(); iter
					.hasNext();) {
				Integer len = iter.next();
				count += len.intValue();
				if (count == codeLength) {
					count -= len.intValue();
					parentCode = code.substring(0, count);
				}
			}
		}
		return parentCode;
	}
	
	
	public String select() throws Exception
	{
		return "select";
	}
	
	public String selectOrg() throws Exception
	{
		return "selectOrg";
	}
	
	public String getIds() throws Exception
	{
		return null;
	}
	
	
	/*
	 * 以下是getter/setter
	 */

	public Page<TInfoBaseAppoint> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseAppoint> page)
	{
		this.page = page;
	}

	public String getToId()
	{
		return toId;
	}

	public void setToId(String toId)
	{
		this.toId = toId;
	}

	public String getOp()
	{
		return op;
	}

	public void setOp(String op)
	{
		this.op = op;
	}

	public int getCurpage()
	{
		return curpage;
	}

	public void setCurpage(int curpage)
	{
		this.curpage = curpage;
	}

	public int getUnitpage()
	{
		return unitpage;
	}

	public void setUnitpage(int unitpage)
	{
		this.unitpage = unitpage;
	}

	public String getSidx()
	{
		return sidx;
	}

	public void setSidx(String sidx)
	{
		this.sidx = sidx;
	}

	public String getSord()
	{
		return sord;
	}

	public void setSord(String sord)
	{
		this.sord = sord;
	}

	public String getSelectOrg()
	{
		return selectOrg;
	}

	public void setSelectOrg(String selectOrg)
	{
		this.selectOrg = selectOrg;
	}

	public String[] getOrgIds()
	{
		return orgIds;
	}

	public void setOrgIds(String[] orgIds)
	{
		this.orgIds = orgIds;
	}

	public Map<String, String> getOrgNames()
	{
		return orgNames;
	}

	public void setOrgNames(Map<String, String> orgNames)
	{
		this.orgNames = orgNames;
	}

	public String[] getCheckedOrgIds()
	{
		return checkedOrgIds;
	}

	public void setCheckedOrgIds(String[] checkedOrgIds)
	{
		this.checkedOrgIds = checkedOrgIds;
	}

	public Boolean getIsSinceToday()
	{
		return isSinceToday;
	}

	public void setIsSinceToday(Boolean isSinceToday)
	{
		this.isSinceToday = isSinceToday;
	}

	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public Page<TUumsBaseOrg> getPage1() {
		return page1;
	}

	public void setPage1(Page<TUumsBaseOrg> page1) {
		this.page1 = page1;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public String getLoadTitle() {
		return loadTitle;
	}

	public void setLoadTitle(String loadTitle) {
		this.loadTitle = loadTitle;
	}

	public String getLoadId() {
		return loadId;
	}

	public void setLoadId(String loadId) {
		this.loadId = loadId;
	}

	public Page<InvitationDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<InvitationDto> pagePl) {
		this.pagePl = pagePl;
	}

	
}
