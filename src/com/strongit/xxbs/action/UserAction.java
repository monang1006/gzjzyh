package com.strongit.xxbs.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class UserAction extends BaseActionSupport<TUumsBaseUser>
{
	private static final long serialVersionUID = 1L;

	@Autowired private IUserService userService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TUumsBaseUser> page;
	
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
		return null;
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
		page = new Page<TUumsBaseUser>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);

		page = userService.queryUsers(page, "0", "1");

		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("currpage", curpage);
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TUumsBaseUser> result = page.getResult();
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		for(TUumsBaseUser one : result)
		{
			obj = new JSONObject();
			obj.put("userId", one.getUserId());
			obj.put("userName", one.getUserName());
			obj.put("userLoginname", one.getUserLoginname());
			rows.add(obj);
		}
		jsonObj.put("rows", rows);
		getResponse().getWriter().write(jsonObj.toString());
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

}
