package com.strongit.xxbs.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.util.ListUtils;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.dto.ColumnDto;
import com.strongit.xxbs.entity.TInfoBaseColumn;
import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.service.IColumnService;
import com.strongit.xxbs.service.IJournalService;
import com.strongit.xxbs.service.JdbcColumnService;
import com.strongit.xxbs.service.impl.JournalService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class ColumnAction extends BaseActionSupport<TInfoBaseColumn>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBaseColumn model = new TInfoBaseColumn();
	
	private IColumnService columnService;
	private IJournalService journalService;
	private JdbcColumnService jdbcColumnService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseColumn> page;
	private Page<ColumnDto> pagePl;
	
	private String op;
	private String toId;
	private String colName;
	private String jourName;
	private Map<String, String> jourMap;
	
	private List<TInfoBaseJournal> journals =
			new ArrayList<TInfoBaseJournal>();

	
	
	@Autowired
	public void setJdbcColumnService(JdbcColumnService jdbcColumnService) {
		this.jdbcColumnService = jdbcColumnService;
	}

	@Autowired
	public void setColumnService(IColumnService columnService)
	{
		this.columnService = columnService;
	}

	@Autowired
	public void setJournalService(IJournalService journalService)
	{
		this.journalService = journalService;
	}

	public TInfoBaseColumn getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = columnService.deleteColumn(toId);
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
			String sort = columnService.getColumnSort();
			model.setColSort(sort);
			model.setTInfoBaseJournal(journal);
		}
		if("edit".equals(op))
		{
			model = columnService.getColumn(toId);
		}
		return INPUT;
	}

	@Override
	public String list() throws Exception
	{
		jourMap = journalService.getJourNames();
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception
	{
	}

	@Override
	public String save() throws Exception
	{
		String colName = getRequest().getParameter("colName");
		String colDetail = getRequest().getParameter("colDetail");
		String toId = getRequest().getParameter("TInfoBaseJournal.jourId");
		String colSort = getRequest().getParameter("colSort");
		model.setColName(colName);
		model.setColDetail(colDetail);
		model.setColSort(colSort);
		TInfoBaseJournal journal = new TInfoBaseJournal();
		journal.setJourId(toId);
		model.setTInfoBaseJournal(journal);
		columnService.saveColumn(model);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String showList() throws Exception
	{
		page = new Page<TInfoBaseColumn>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String colName = getRequest().getParameter("colName");
			colName = colName.replace("%", "\'\'%");
			String jourName = getRequest().getParameter("jourName");
			
			page = columnService.findColumnsByNameAndtoId(page, colName,jourName);
			/*List<TInfoBaseColumn> list = page.getResult();
			List<TInfoBaseColumn> list1 = new ArrayList<TInfoBaseColumn>();
			for(TInfoBaseColumn t :list){
				String jName = t.getTInfoBaseJournal().getJourName();
				if(jName!=null&&jName.equals(jourName)){
					list1.add(t);
				}
			}
			if(list1!=null&&list1.size()>0){
			page = ListUtils.splitList2Page(page, list1);
			}*/
		}
		else
		{
			page = columnService.getColumns(page);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseColumn> result = page.getResult();
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		if(result != null)
		{
		for(TInfoBaseColumn one : result)
		{
			obj = new JSONObject();
			obj.put("colId", one.getColId());
			obj.put("colName", one.getColName());
			obj.put("jourName", one.getTInfoBaseJournal().getJourName());
			obj.put("colCode", one.getColCode());
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
		pagePl = new Page<ColumnDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String colName = getRequest().getParameter("colName");
			colName = colName.replace("%", "\'\'%");
			String jourName = getRequest().getParameter("jourName");
			
			pagePl = jdbcColumnService.getColumns(pagePl,colName,jourName);
		}
		else
		{
			pagePl = jdbcColumnService.getColumns(pagePl,"","");
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<ColumnDto> result = pagePl.getResult();
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		if(result != null)
		{
		for(ColumnDto one : result)
		{
			obj = new JSONObject();
			obj.put("colId", one.getColId());
			obj.put("colName", one.getColName());
			obj.put("jourName", one.getJourName());
			obj.put("colCode", one.getColCode());
			rows.add(obj);
		}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	
	public String isExistSore() throws Exception
	{
		String sore = getRequest().getParameter("sore");
		String t = columnService.isExistSore(sore).toString();
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(t);
		return null;
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

	public Page<TInfoBaseColumn> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseColumn> page)
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

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getJourName() {
		return jourName;
	}

	public void setJourName(String jourName) {
		this.jourName = jourName;
	}

	public Map<String, String> getJourMap() {
		return jourMap;
	}

	public void setJourMap(Map<String, String> jourMap) {
		this.jourMap = jourMap;
	}

	public Page<ColumnDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<ColumnDto> pagePl) {
		this.pagePl = pagePl;
	}

	
	
}
