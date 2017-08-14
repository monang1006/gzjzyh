package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.uums.security.UserInfo;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.BulletinListDto;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.service.IBulletinMarkedService;
import com.strongit.xxbs.service.IBulletinService;
import com.strongit.xxbs.service.JdbcBulletinService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class BulletinAction extends BaseActionSupport<TInfoBaseBulletin>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBaseBulletin model = new TInfoBaseBulletin();
	
	private IBulletinService bulletinService;
	private IBulletinMarkedService bulletinMarkedService;
	private JdbcBulletinService jdbcBulletinService;
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseBulletin> page;
	private Page<BulletinListDto> pagePl;
	private String op;
	private String toId;
	private String submitStatus = Publish.ALL;

	
	@Autowired
	public void setJdbcBulletinService(JdbcBulletinService jdbcBulletinService) {
		this.jdbcBulletinService = jdbcBulletinService;
	}

	@Autowired
	public void setBulletinService(IBulletinService bulletinService)
	{
		this.bulletinService = bulletinService;
	}

	@Autowired
	public void setBulletinMarkedService(
			IBulletinMarkedService bulletinMarkedService)
	{
		this.bulletinMarkedService = bulletinMarkedService;
	}

	public TInfoBaseBulletin getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = bulletinService.deleteBulletin(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		String ret = "input";
		if("edit".equals(op))
		{
			model = bulletinService.getBulletin(toId);
			String title = model.getBlTitle();
			title = title.replaceAll( "\"", "\\“" );
			model.setBlTitle(title);
		}
		if("view".equals(op))
		{
			model = bulletinService.getBulletin(toId);
			String title = model.getBlTitle();
			title = title.replaceAll( "\"", "\\“" );
			model.setBlTitle(title);
			ret = "view";
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
		model.setBlDate(new Date());
		bulletinService.saveBulletin(model);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String showList() throws Exception
	{
		page = new Page<TInfoBaseBulletin>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String blTitle = getRequest().getParameter("blTitle");
			blTitle = blTitle.replace("%", "\'\'%");
			page = bulletinService.findBulletinsByTitle(page, blTitle);
		}
		else
		{
			page = bulletinService.getBulletins(page);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseBulletin> result = page.getResult();
		
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(TInfoBaseBulletin one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("blId", one.getBlId());
				obj.put("blTitle", one.getBlTitle());
				obj.put("blDate", sdfTime.format(one.getBlDate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	
	public String showJdbcList() throws Exception
	{
		pagePl = new Page<BulletinListDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String blTitle = getRequest().getParameter("blTitle");
			blTitle = blTitle.replace("%", "\'\'%");
			pagePl = jdbcBulletinService.getBulletins(pagePl,blTitle);
		}
		else
		{
			String blTitle ="";
			pagePl = jdbcBulletinService.getBulletins(pagePl,blTitle);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<BulletinListDto> result = pagePl.getResult();
		
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(BulletinListDto one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("blId", one.getBlId());
				obj.put("blTitle", one.getBlTitle());
				obj.put("blDate", sdfTime.format(one.getBlDate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String view() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String userId = userInfo.getUserId();
		model = bulletinService.getBulletin(toId);
		if(!bulletinMarkedService.isRead(model.getBlId(), userId))
		{
			bulletinMarkedService.saveBM(model.getBlId(), userId);
		}
		return "view";
	}
	
	
	
	/*
	 * 以下是setter/getter
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

	public Page<TInfoBaseBulletin> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseBulletin> page)
	{
		this.page = page;
	}

	public String getOp()
	{
		return op;
	}

	public void setOp(String op)
	{
		this.op = op;
	}

	public String getToId()
	{
		return toId;
	}

	public void setToId(String toId)
	{
		this.toId = toId;
	}

	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public Page<BulletinListDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<BulletinListDto> pagePl) {
		this.pagePl = pagePl;
	}

	
}
