package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.JournalListDto;
import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoBaseWordTemplate;
import com.strongit.xxbs.service.IColumnService;
import com.strongit.xxbs.service.IJournalService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.ISubmitService;
import com.strongit.xxbs.service.IWordTemplateService;
import com.strongit.xxbs.service.JdbcJournalService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class JournalAction extends BaseActionSupport<TInfoBaseJournal>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBaseJournal model = new TInfoBaseJournal();
	private IJournalService journalService;
	private ISubmitService submitService;
	private IColumnService columnService;
	private IOrgService orgService;
	@Autowired
	private IWordTemplateService wordTemplateService;
	private JdbcJournalService jdbcJournalService;
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseJournal> page;
	private Page<TInfoBasePublish> pagePublish;
	private Page<JournalListDto> pagePl;
	
	private String op;
	private String toId;
	private List<TInfoBaseWordTemplate> wordTemplates;
	
	
	
	@Autowired
	public void setJdbcJournalService(JdbcJournalService jdbcJournalService) {
		this.jdbcJournalService = jdbcJournalService;
	}

	@Autowired
	public void setJournalService(IJournalService journalService)
	{
		this.journalService = journalService;
	}

	@Autowired
	public void setSubmitService(ISubmitService submitService)
	{
		this.submitService = submitService;
	}

	@Autowired
	public void setColumnService(IColumnService columnService)
	{
		this.columnService = columnService;
	}

	@Autowired
	public void setOrgService(IOrgService orgService)
	{
		this.orgService = orgService;
	}

	public TInfoBaseJournal getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = journalService.deleteJoural(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		wordTemplates = wordTemplateService.getTemplates();
		if("add".equals(op))
		{
			model.setJourIsDefault(Publish.DEFAULT);
		}
		if("edit".equals(op))
		{
			model = journalService.getJoural(toId);
			String title = model.getJourName();
			title = title.replaceAll( "\"", "\\“" );
			model.setJourName(title);
		}
		//categories = journalCategoryService.getJouralCategories();
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
		model.setJourDate(new Date());
		journalService.saveJoural(model);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}
	
	public String showList() throws Exception
	{
		page = new Page<TInfoBaseJournal>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			Map<String, String> search = new HashMap<String, String>();			
			String jourName = getRequest().getParameter("jourName");
			jourName = jourName.replace("%", "\'\'%");
			String addDate = getRequest().getParameter("addDate");
			search.put("jourName", jourName);
			search.put("addDate", addDate);
			page = journalService.findJourals(page, search);
		}
		else
		{
			page = journalService.getJourals(page);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
				
		List<TInfoBaseJournal> result = page.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(TInfoBaseJournal one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("jourId", one.getJourId());
				obj.put("jourName", one.getJourName());
				obj.put("jourDate", one.getJourDate().toString());
				obj.put("jourCode", one.getJourCode());
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
		pagePl = new Page<JournalListDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			Map<String, String> search = new HashMap<String, String>();			
			String jourName = getRequest().getParameter("jourName");
			jourName = jourName.replace("%", "\'\'%");
			String addDate = getRequest().getParameter("addDate");
			search.put("jourName", jourName);
			search.put("addDate", addDate);
			pagePl = jdbcJournalService.getJourals(pagePl,jourName);
		}
		else
		{
			String title = "";
			pagePl = jdbcJournalService.getJourals(pagePl,title);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
				
		List<JournalListDto> result = pagePl.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(JournalListDto one : result)
			{
				
				JSONObject obj = new JSONObject();
				obj.put("jourId", one.getJourId());
				obj.put("jourName", one.getJourName());
				obj.put("jourCode", one.getJourCode());
				obj.put("jourDate", sdfTime.format(one.getJourDate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String tree() throws Exception
	{
		return "tree";
	}
	
/*	public String showTree() throws Exception
	{
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		List<TInfoBaseJournalCategory> allList = 
				journalCategoryService.getJouralCategories();
		List<TInfoBaseJournal> jourList = 
				journalService.getJourals();
		
		for(TInfoBaseJournalCategory row : allList)
		{
			String jcId = row.getJcId();

			TreeNode node = new TreeNode();
			node.setPid("-1");		
			node.setId(jcId);
			node.setText(row.getJcTitle());
			node.setValue(jcId);	
			node.setComplete(true);
			node.setShowcheck(false);
			node.setIsexpand(true);
			nodeList.add(node);
			
			
			TreeNode node2 = null;
			for(TInfoBaseJournal one : jourList)
			{
				if(jcId.equals(one.getTInfoBaseJournalCategory().getJcId()))
				{
					node2 = new TreeNode();
					node2.setPid(jcId);		
					node2.setId(one.getJourId());
					node2.setText(one.getJourName());
					node2.setValue(one.getJourId());	
					node2.setComplete(true);
					node2.setShowcheck(false);
					node2.setIsexpand(true);
					nodeList.add(node2);
				}
			}
		}

		String jsonTreeNode = JsonUtil.fromTreeNodes(nodeList);
		
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonTreeNode);
		return null;
	}
	*/
	public String release() throws Exception
	{
		return "release";
	}
	
	public String releaseContent() throws Exception
	{
		return "releaseContent";
	}
	
//	public String releaseList() throws Exception
//	{
//		pagePublish = new Page<TInfoBasePublish>(unitpage, true);
//		pagePublish.setPageNo(curpage);
//		pagePublish.setPageSize(unitpage);
//		
//		pagePublish.setOrder(sord);
//		pagePublish.setOrderBy(sidx);
//		
//		if(getRequest().getParameter("isSearch") != null 
//				&& getRequest().getParameter("isSearch").equals("true"))
//		{
//			Map<String, String> search = new HashMap<String, String>();			
//			String jourName = getRequest().getParameter("jourName");
//			String addDate = getRequest().getParameter("addDate");
//			search.put("jourName", jourName);
//			search.put("addDate", addDate);
//			//pagePublish = journalService.findJourals(page, search);
//		}
//		else
//		{
//			pagePublish = submitService.getSubmits(pagePublish);
//		}
//		
//		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("curpage", curpage);
//		jsonObj.put("totalpages", pagePublish.getTotalPages());
//		jsonObj.put("totalrecords", pagePublish.getTotalCount());
//				
//		Map<String, String> orgNames = orgService.getOrgNames();
//		List<?> lists = submitService.fullSubmitted("");
//		JSONArray rows = new JSONArray();
//		if(lists != null)
//		{
//			for(int i=0;i<lists.size();i++)
//			{
//				Object[] one = (Object[]) lists.get(i);
//				JSONObject obj = new JSONObject();
//				obj.put("pubId", one[0]);
//				obj.put("pubTitle", one[1]);
//				obj.put("orgId", one[2]);
//				obj.put("orgName", orgNames.get(one[2]));
//				obj.put("adoUseScore", one[3].toString());
//				obj.put("adoDate", one[4].toString());
//				obj.put("colName", one[5]);
//				rows.add(obj);
//			}
//		}
//		jsonObj.put("rows", rows);
//		getResponse().setContentType("application/json");
//		getResponse().getWriter().write(jsonObj.toString());
//		return null;
//	}
	
	

	

	
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

	public Page<TInfoBaseJournal> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseJournal> page)
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

	public Page<TInfoBasePublish> getPagePublish()
	{
		return pagePublish;
	}

	public void setPagePublish(Page<TInfoBasePublish> pagePublish)
	{
		this.pagePublish = pagePublish;
	}

	public List<TInfoBaseWordTemplate> getWordTemplates()
	{
		return wordTemplates;
	}

	public void setWordTemplates(List<TInfoBaseWordTemplate> wordTemplates)
	{
		this.wordTemplates = wordTemplates;
	}

	public Page<JournalListDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<JournalListDto> pagePl) {
		this.pagePl = pagePl;
	}
	
}
