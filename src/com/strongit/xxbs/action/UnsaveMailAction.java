package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.dto.UnsaveMailDto;
import com.strongit.xxbs.entity.TInfoBaseUnsaveMail;
import com.strongit.xxbs.service.IMemberService;
import com.strongit.xxbs.service.IUnsaveMailService;
import com.strongit.xxbs.service.JdbcUnsaveMailService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class UnsaveMailAction extends BaseActionSupport<TInfoBaseUnsaveMail>
{
	private static final long serialVersionUID = 1L;

	private TInfoBaseUnsaveMail model = new TInfoBaseUnsaveMail();
	
	private IUnsaveMailService unsaveMailService;
	private IMemberService memberService;
	private JdbcUnsaveMailService jdbcUnsaveMailService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseUnsaveMail> page;
	private Page<TUumsBaseUser> pageUser;
	private Page<UnsaveMailDto> pagePl;
	
	private String toId;
	private String email;
	
	
	
	@Autowired
	public void setJdbcUnsaveMailService(JdbcUnsaveMailService jdbcUnsaveMailService) {
		this.jdbcUnsaveMailService = jdbcUnsaveMailService;
	}

	@Autowired
	public void setUnsaveMailService(IUnsaveMailService unsaveMailService)
	{
		this.unsaveMailService = unsaveMailService;
	}

	@Autowired
	public void setMemberService(IMemberService memberService)
	{
		this.memberService = memberService;
	}

	public TInfoBaseUnsaveMail getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		return null;
	}

	@Override
	public String input() throws Exception
	{
		return INPUT;
	}

	@Override
	public String list() throws Exception
	{
		return SUCCESS;
	}
	
	public String view() throws Exception
	{
		model = unsaveMailService.getUnsaveMail(toId);
		return "view";
	}
	
	public String showList() throws Exception
	{
		page = new Page<TInfoBaseUnsaveMail>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		page = unsaveMailService.getUnsaveMails(page);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseUnsaveMail> result = page.getResult();
		
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(TInfoBaseUnsaveMail one : result)
		{
			obj = new JSONObject();
			obj.put("id", one.getId());
			obj.put("mailAddress", one.getMailAddress());
			obj.put("subject", one.getSubject());
			obj.put("sentDate", sdfTime.format(one.getSentDate()));
			rows.add(obj);
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String showJdbcList() throws Exception
	{
		pagePl = new Page<UnsaveMailDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		pagePl = jdbcUnsaveMailService.getUnsaveMails(pagePl);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<UnsaveMailDto> result = pagePl.getResult();
		
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(UnsaveMailDto one : result)
		{
			obj = new JSONObject();
			obj.put("id", one.getId());
			obj.put("mailAddress", one.getMailAddress());
			obj.put("subject", one.getSubject());
			obj.put("sentDate", sdfTime.format(one.getSentDate()));
			rows.add(obj);
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
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
	
	public String selectNotMailUser() throws Exception
	{
		return "selectNotMailUser";
	}
	
	public String showNotMailUser() throws Exception
	{
		pageUser = new Page<TUumsBaseUser>(unitpage, true);
		pageUser.setPageNo(curpage);
		pageUser.setPageSize(unitpage);
		
		pageUser.setOrder(sord);
		pageUser.setOrderBy(sidx);
		
		pageUser = memberService.getNotMailUser(pageUser);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		jsonObj.put("totalpages", pageUser.getTotalPages());
		jsonObj.put("totalrecords", pageUser.getTotalCount());
		
		List<TUumsBaseUser> result = pageUser.getResult();
		
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		if(result != null){
			for(TUumsBaseUser one : result)
			{
				obj = new JSONObject();
				obj.put("userId", one.getUserId());
				obj.put("userName", one.getUserName());
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String saveToPublish() throws Exception
	{
		unsaveMailService.saveToPubilsh();
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}
	
	public String saveMailToUserAndPublish() throws Exception
	{
		//保存异常邮件地址给映射的用户
		memberService.saveMailToUser(toId, email);
		//保存异常邮件
		unsaveMailService.saveToPubilsh();
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}
	
	
	/*
	 * getter/setter
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

	public Page<TInfoBaseUnsaveMail> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseUnsaveMail> page)
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

	public Page<TUumsBaseUser> getPageUser()
	{
		return pageUser;
	}

	public void setPageUser(Page<TUumsBaseUser> pageUser)
	{
		this.pageUser = pageUser;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Page<UnsaveMailDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<UnsaveMailDto> pagePl) {
		this.pagePl = pagePl;
	}
	
	
}
