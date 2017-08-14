package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.IssueDto;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.service.IIssueService;
import com.strongit.xxbs.service.IJournalService;
import com.strongit.xxbs.service.JdbcIssueService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class IssueAction extends BaseActionSupport<TInfoBaseIssue>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBaseIssue model = new TInfoBaseIssue();
	private IIssueService issueService;
	private IJournalService journalService;
	private JdbcIssueService jdbcIssueService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseIssue> page;
	private Page<IssueDto> pagePl;
	
	private String op;
	private String toId;
	
	private List<TInfoBaseJournal> journals =
			new ArrayList<TInfoBaseJournal>();

	
	@Autowired
	public void setJdbcIssueService(JdbcIssueService jdbcIssueService) {
		this.jdbcIssueService = jdbcIssueService;
	}

	@Autowired
	public void setIssueService(IIssueService issueService)
	{
		this.issueService = issueService;
	}

	@Autowired
	public void setJournalService(IJournalService journalService)
	{
		this.journalService = journalService;
	}

	public TInfoBaseIssue getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = issueService.deleteIssue(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		journals = journalService.getJourals();
		if("add".equals(op))
		{
			TInfoBaseJournal journal = new TInfoBaseJournal();
			journal.setJourId(toId);
			model.setTInfoBaseJournal(journal);
			model.setIssIsPublish(Publish.NO_PUBLISHED);
		}
		if("edit".equals(op))
		{
			
			model = issueService.getIssue(toId);
			Date date = model.getIssTime();
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
			if(date!=null){
			String date1 = sim.format(date);
			getRequest().setAttribute("date1", date1);
			}
			
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
		model.setIssDate(new Date());
		issueService.saveIssue(model);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}
	
	public String showList() throws Exception
	{
		page = new Page<TInfoBaseIssue>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		page = issueService.getIssues(page);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseIssue> result = page.getResult();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(TInfoBaseIssue one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("issId", one.getIssId());
				obj.put("issNumber", one.getIssNumber());
				obj.put("issTime", sim.format(one.getIssTime()));
				obj.put("jourName", one.getTInfoBaseJournal().getJourName());
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
		pagePl = new Page<IssueDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		pagePl = jdbcIssueService.getIssues(pagePl);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<IssueDto> result = pagePl.getResult();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(IssueDto one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("issId", one.getIssId());
				obj.put("issNumber", one.getIssNumber());
				obj.put("issTime", sim.format(one.getIssTime()));
				obj.put("jourName", one.getJourName());
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

	public Page<TInfoBaseIssue> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseIssue> page)
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

	public List<TInfoBaseJournal> getJournals()
	{
		return journals;
	}

	public void setJournals(List<TInfoBaseJournal> journals)
	{
		this.journals = journals;
	}

	public Page<IssueDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<IssueDto> pagePl) {
		this.pagePl = pagePl;
	}

	
}
