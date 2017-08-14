package com.strongit.xxbs.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.xxbs.common.PathUtil;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.dto.WordTemplateDto;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.entity.TInfoBaseWordTemplate;
import com.strongit.xxbs.service.IWordTemplateService;
import com.strongit.xxbs.service.JdbcWordTemplateService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@Results( { @Result(name = "WTLIST", value = "wordTemplate.action", type = ServletActionRedirectResult.class)})
public class WordTemplateAction extends BaseActionSupport<TInfoBaseWordTemplate>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBaseWordTemplate model = new TInfoBaseWordTemplate();
	
	private IWordTemplateService wordTemplateService;
	private JdbcWordTemplateService jdbcWordTemplateService;
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseWordTemplate> page;
	private Page<WordTemplateDto> pagePl;
	
	private File upload;
	private File upload2;

	private String op;
	private String toId;

	
	@Autowired
	public void setJdbcWordTemplateService(
			JdbcWordTemplateService jdbcWordTemplateService) {
		this.jdbcWordTemplateService = jdbcWordTemplateService;
	}

	@Autowired
	public void setWordTemplate(IWordTemplateService wordTemplateService)
	{
		this.wordTemplateService = wordTemplateService;
	}

	public TInfoBaseWordTemplate getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = wordTemplateService.deleteTemplate(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		if("edit".equals(op))
		{
			model = wordTemplateService.getTemplate(toId);
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
		model.setWtDate(new Date());
		model.setWtTitle(URLDecoder.decode(model.getWtTitle(), "UTF-8"));
		wordTemplateService.saveTemplate(model);
		String id = model.getWtId();
		
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		FileInputStream fIn2 = null;
		FileOutputStream fOut2 = null;
		try
		{
			if(upload != null)
			{
				fIn = new FileInputStream(upload);
				byte[] bys = IOUtils.toByteArray(fIn);
				fOut = new FileOutputStream(PathUtil.wordTemplatePath() + id + "-header.doc");
				System.out.println(PathUtil.wordTemplatePath() + id + "-header.doc");
				IOUtils.write(bys, fOut);
			}
			
			if(upload2 != null)
			{
				fIn2 = new FileInputStream(upload2);
				byte[] bys2 = IOUtils.toByteArray(fIn2);
				fOut2 = new FileOutputStream(PathUtil.wordTemplatePath() + id + "-body.doc");
				IOUtils.write(bys2, fOut2);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(upload != null)
			{
				fIn.close();
				fOut.close();
			}
			if(upload2 != null)
			{
				fIn2.close();
				fOut2.close();
			}
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}
	
	public String showList() throws Exception
	{
		page = new Page<TInfoBaseWordTemplate>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			page.setPageNo(1);
			curpage = 1;
			String wtTitle = getRequest().getParameter("wtTitle");
			wtTitle = wtTitle.replace("%", "\'\'%");
			page = wordTemplateService.findTemplatesByTitle(page, wtTitle);
		}
		else
		{
			page = wordTemplateService.getTemplates(page);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseWordTemplate> result = page.getResult();
		
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(TInfoBaseWordTemplate one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("wtId", one.getWtId());
				obj.put("wtTitle", one.getWtTitle());
				obj.put("wtDate", sdfTime.format(one.getWtDate()));
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
		pagePl = new Page<WordTemplateDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			pagePl.setPageNo(1);
			curpage = 1;
			String wtTitle = getRequest().getParameter("wtTitle");
			wtTitle = wtTitle.replace("%", "\'\'%");
			pagePl = jdbcWordTemplateService.getTemplates(pagePl,wtTitle);
		}
		else
		{
			pagePl = jdbcWordTemplateService.getTemplates(pagePl,"");
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<WordTemplateDto> result = pagePl.getResult();
		
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(WordTemplateDto one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("wtId", one.getWtId());
				obj.put("wtTitle", one.getWtTitle());
				obj.put("wtDate", sdfTime.format(one.getWtDate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String officeStream() throws Exception
	{
		FileInputStream fIn = null;
		ServletOutputStream outStream = null;
		try
		{
			fIn = new FileInputStream(PathUtil.wordTemplatePath() + toId + ".doc");
			byte[] bOffice = IOUtils.toByteArray(fIn);
			outStream = getResponse().getOutputStream();
			outStream.write(bOffice);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			fIn.close();
			outStream.close();
		}
		return null;
	}

	
	
	/*
	 * setter/getter
	 */

	public String getToId()
	{
		return toId;
	}

	public void setToId(String toId)
	{
		this.toId = toId;
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

	public Page<TInfoBaseWordTemplate> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseWordTemplate> page)
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

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public File getUpload2()
	{
		return upload2;
	}

	public void setUpload2(File upload2)
	{
		this.upload2 = upload2;
	}

	public Page<WordTemplateDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<WordTemplateDto> pagePl) {
		this.pagePl = pagePl;
	}
	
	

}
