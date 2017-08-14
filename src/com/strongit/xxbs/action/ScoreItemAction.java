package com.strongit.xxbs.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.ScoreDto;
import com.strongit.xxbs.entity.TInfoBaseScore;
import com.strongit.xxbs.service.IScoreItemService;
import com.strongit.xxbs.service.JdbcScoreItemService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class ScoreItemAction extends BaseActionSupport<TInfoBaseScore>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBaseScore model = new TInfoBaseScore();
	
	private IScoreItemService scoreItemService;
	private JdbcScoreItemService jdbcScoreItemService;
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseScore> page;
	private Page<ScoreDto> pagePl;
	
	private String op;
	private String toId;
	
	
	@Autowired
	public void setJdbcScoreItemService(JdbcScoreItemService jdbcScoreItemService) {
		this.jdbcScoreItemService = jdbcScoreItemService;
	}

	@Autowired
	public void setScoreItemService(IScoreItemService scoreItemService)
	{
		this.scoreItemService = scoreItemService;
	}

	public TInfoBaseScore getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = scoreItemService.deleteScoreItem(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		model.setScState(Publish.SC_YS);
		if("edit".equals(op))
		{
			model = scoreItemService.getScoreItem(toId);
		}
		return INPUT;
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
		model.setScDate(new Date());
		scoreItemService.saveScoreItem(model);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}
	
	public String showList() throws Exception
	{
		page = new Page<TInfoBaseScore>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String scoreName = getRequest().getParameter("colName");
			scoreName = scoreName.replace("%", "\'\'%");
			page = scoreItemService.findScoreItem(page,scoreName);
		}
		else
		{
			page = scoreItemService.getScoreItems(page);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseScore> result = page.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(TInfoBaseScore one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("scId", one.getScId());
				obj.put("scName", one.getScName());
				obj.put("scScore", one.getScScore());
				String state = one.getScState();
				if(state.equals(Publish.SC_YS)){
					state = "已启用";
				}
				else if(state.equals(Publish.SC_NO)){
					state = "未启用";
				}
				obj.put("scState", state);
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
		pagePl = new Page<ScoreDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String scoreName = getRequest().getParameter("colName");
			scoreName = scoreName.replace("%", "\'\'%");
			pagePl = jdbcScoreItemService.getScoreItems(pagePl,scoreName);
		}
		else
		{
			pagePl = jdbcScoreItemService.getScoreItems(pagePl,"");
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<ScoreDto> result = pagePl.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(ScoreDto one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("scId", one.getScId());
				obj.put("scName", one.getScName());
				obj.put("scScore", one.getScScore());
				String state = one.getScState();
				if(state.equals(Publish.SC_YS)){
					state = "已启用";
				}
				else if(state.equals(Publish.SC_NO)){
					state = "未启用";
				}
				obj.put("scState", state);
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	
	
	/*
	 * setter/getter
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

	public Page<TInfoBaseScore> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseScore> page)
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

	public Page<ScoreDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<ScoreDto> pagePl) {
		this.pagePl = pagePl;
	}

	
}
