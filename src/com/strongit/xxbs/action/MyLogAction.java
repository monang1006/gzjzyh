package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaLog;
import com.strongit.uums.security.UserInfo;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.BulletinListDto;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.service.IBulletinMarkedService;
import com.strongit.xxbs.service.IBulletinService;
import com.strongit.xxbs.service.IMyLogService;
import com.strongit.xxbs.service.JdbcBulletinService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class MyLogAction extends BaseActionSupport<ToaLog>
{
	private static final long serialVersionUID = 1L;
	
	private ToaLog model = new ToaLog();
	
	private IMyLogService myLogService;
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<ToaLog> page;
	private Page<BulletinListDto> pagePl;
	private String op;
	private String toId;
	private String submitStatus = Publish.ALL;

	

	

	@Autowired
	public void setMyLogService(IMyLogService myLogService) {
		this.myLogService = myLogService;
	}

	public ToaLog getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = myLogService.deleteMyLog(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
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

	public String showList() throws Exception
	{
		page = new Page<ToaLog>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String blTitle = getRequest().getParameter("blTitle");
			blTitle = blTitle.replace("%", "\'\'%");
			page = myLogService.findMyLogByTitle(page, blTitle);
		}
		else
		{
			
			try {
				page = myLogService.getMyLogs(page);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<ToaLog> result = page.getResult();
		
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(ToaLog one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("logId", one.getLogId());
				obj.put("logInfo", one.getLogInfo());
				obj.put("openUser", one.getOpeUser());
				obj.put("openIp", one.getOpeIp());
				obj.put("opeTime", sdfTime.format(one.getOpeTime()));
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
		model = myLogService.getMyLog(toId);
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
