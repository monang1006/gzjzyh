package com.strongit.xxbs.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class AddressAction extends BaseActionSupport<TUumsBaseUser>
{
	private static final long serialVersionUID = 1L;

	private String extOrgId;
	private String selectname;
	private String selectloginname;// 用户登陆名

	private String selectorg;// 所属机构

	private String isdel;// 是否删除
	
	@Autowired
	private IUserService userService;
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TUumsBaseUser> page = new Page<TUumsBaseUser>(10, true);
	private String isActive;//是否启用
	
	private String toId;
	
	
	public TUumsBaseUser getModel()
	{
		return null;
	}

	@Override
	public String delete() throws Exception
	{
		return null;
	}

	@Override
	public String input() throws Exception
	{
		return null;
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
		return null;
	}
	
	public String select() throws Exception
	{
		return "select";
	}
	
	public String showList() throws Exception
	{
		UserInfo userInfo = userService.getCurrentUserInfo(); 
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			Map<String, String> search = new HashMap<String, String>();			
			String selectname = getRequest().getParameter("selectname");
			String selectloginname = getRequest().getParameter("selectloginname");
			String isdel = getRequest().getParameter("isdel");
			String isActive = getRequest().getParameter("isActive");
			search.put("selectname", selectname);
			search.put("selectloginname", selectloginname);
			search.put("isdel", isdel);
			search.put("isActive", isActive);
		}
		if(userInfo.getIsAdmin()||userInfo.getIsSubmit()){
			List<TUumsBaseOrg>orgList = userService.getOrgsByOrgSyscode("001006013", "0");
			String orgId = orgList.get(0).getOrgId();
			page = userService.queryOrgUsersByHa(page, orgId, selectname, selectloginname, isdel, isActive, userInfo.getOrgId());
		}	
		else if (extOrgId != null && extOrgId.trim().length() > 0) {
			page = userService.queryOrgUsersByHa(page, extOrgId, selectname, selectloginname, isdel, isActive, userInfo.getOrgId());
		} else {
			//page = userManager.queryUsersByHa(page, selectname, selectloginname, selectorg, "0", "1", userInfo.getOrgId());
		}
		
		int totalRecord =page.getTotalCount();
		int totalPage =totalRecord % unitpage == 0 ? totalRecord   
				              / unitpage : totalRecord / unitpage  
				             + 1; // 计算总页数  
				              
	    int index = (curpage - 1) * unitpage;
	    int pageSize = unitpage; 	              
	    // 定义返回的数据类型：json，使用了json-lib
	    JSONObject jsonObj = new JSONObject();
	    // 根据jqGrid对JSON的数据格式要求给jsonObj赋值
	    jsonObj.put("curpage", curpage);// 当前页
	    jsonObj.put("totalpages", totalPage);// 总页数
	    jsonObj.put("totalrecords", totalRecord);// 总记录数
	    
		
		//List<TInfoBasePublish> result = page.getResult();
		JSONArray rows = new JSONArray();
		if(page.getResult()!=null && page.getResult().size()>0){
		  List<TUumsBaseUser> uslist=page.getResult();
		
		
		JSONObject obj = null;
		for(TUumsBaseUser one : uslist)
		{
			obj = new JSONObject();
			obj.put("uid", one.getUserId());
			obj.put("username", one.getUserName());
			obj.put("usertel", one.getUserTel());
			obj.put("useraddr", one.getUserAddr());
			obj.put("useremail", one.getUserEmail());
			obj.put("userepost", one.getRest1());
			obj.put("userposition", one.getRest2());
			rows.add(obj);
		}
		}	
		jsonObj.put("rows", rows);
		
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		//System.out.print(jsonObj.toString());
		return null;
	}

	
	
	public String ogrlist() {
		 UserInfo userInfo = userService.getCurrentUserInfo();
			if (extOrgId != null && extOrgId.trim().length() > 0) {
				page = userService.queryOrgUsersByHa(page, extOrgId, userInfo.getOrgId());
			} else {
				page = userService.queryUsersByHa(page, "%", "%", userInfo.getOrgId());
			}
			return "usermanage";
		}
	
	public String isNature() throws Exception {
		 String flag = "";
		 TUumsBaseOrg org = userService.getOrgInfoByOrgId(toId);
		 if(("5".equals(org.getOrgNature()))||(org.getOrgName().equals("公文传输单位"))){
			  flag = "nosuccess";
		 }
			getResponse().setContentType("application/json");
			getResponse().getWriter().write(flag);
			return null;
		}
	
	/*
	 * 以下是getter/setter
	 */

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

	public Page<TUumsBaseUser> getPage()
	{
		return page;
	}

	public void setPage(Page<TUumsBaseUser> page)
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

	public String getExtOrgId() {
		return extOrgId;
	}

	public void setExtOrgId(String extOrgId) {
		this.extOrgId = extOrgId;
	}

	public String getSelectname() {
		return selectname;
	}

	public void setSelectname(String selectname) {
		this.selectname = selectname;
	}

	public String getSelectloginname() {
		return selectloginname;
	}

	public void setSelectloginname(String selectloginname) {
		this.selectloginname = selectloginname;
	}

	public String getSelectorg() {
		return selectorg;
	}

	public void setSelectorg(String selectorg) {
		this.selectorg = selectorg;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
