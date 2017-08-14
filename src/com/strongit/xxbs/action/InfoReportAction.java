package com.strongit.xxbs.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.utils.DateTimes;
import com.strongit.utils.DateToChange;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.security.UserInfo;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.ReportDto;
import com.strongit.xxbs.dto.UsedReportDto;
import com.strongit.xxbs.entity.TInfoBaseInfoReport;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IInfoReportService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.ISubmitService;
import com.strongit.xxbs.service.JdbcInfoReportService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class InfoReportAction extends BaseActionSupport<TInfoBaseInfoReport>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBaseInfoReport model = new TInfoBaseInfoReport();
	private IInfoReportService infoReportService;
	private IOrgService orgService;
	private IPublishService publishService;
	private JdbcInfoReportService jdbcInfoReportService;
	private ISubmitService submitService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBaseInfoReport> page;
	private Page<UsedReportDto> pagePl;
	
	private String toId;
	private String tbTitle;
	private String startDate;
	private String endDate;
	private List<ReportDto> reports = new ArrayList<ReportDto>();
	private File upload;
	private String isPublish;
	private String submitStatus = Publish.ALL;
	
	private List list;
	private List list1 =new ArrayList();
	private int app[];
	private int[] years;
	private String year;
	private String month;
	private String op = "";
	
	private List tongbao1 =new ArrayList();
	private List tongbao2 =new ArrayList();
	private List tongbao3 =new ArrayList();
	private List tongbao4 =new ArrayList();
	private List tongbao5 =new ArrayList();
	
	@Autowired
	public void setSubmitService(ISubmitService submitService) {
		this.submitService = submitService;
	}

	@Autowired
	public void setJdbcInfoReportService(JdbcInfoReportService jdbcInfoReportService) {
		this.jdbcInfoReportService = jdbcInfoReportService;
	}

	@Autowired
	public void setInfoReportService(IInfoReportService infoReportService)
	{
		this.infoReportService = infoReportService;
	}

	@Autowired
	public void setOrgService(IOrgService orgService)
	{
		this.orgService = orgService;
	}

	@Autowired
	public void setPublishService(IPublishService publishService)
	{
		this.publishService = publishService;
	}

	public TInfoBaseInfoReport getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		infoReportService.deleteReport(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		model = infoReportService.getReport(toId);
		String title = model.getRpTitle();
		title = title.replaceAll( "\"", "\\“" );
		model.setRpTitle(title);
		return INPUT;
	}

	@Override
	public String list() throws Exception
	{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		int year2 = Integer.parseInt(year1);
		int month = date.getMonth()+1;
		getRequest().setAttribute("month1", month);
		years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception
	{
	}

	@Override
	public String save() throws Exception
	{
		String title = getRequest().getParameter("rpTitle");
		title = java.net.URLDecoder.decode(title,"UTF-8"); 
		model.setRpTitle(title);
		model.setRpDate(new Date());
		if(!op.equals("edit")){
		model.setRpId(null);
		}
		FileInputStream fIn = null;
		try
		{
			fIn = new FileInputStream(upload);
			byte[] bys = IOUtils.toByteArray(fIn);
			model.setRpWord(bys);
			try {
				infoReportService.saveReport(model);
			} catch (Exception e) {
				e.printStackTrace();
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
			fIn.close();
		}
		return null;
	}
	
	public String officeStream() throws Exception
	{
		TInfoBaseInfoReport report = infoReportService.getReport(toId);
		byte[] bOffice = report.getRpWord();
		ServletOutputStream outStream = null;
		try
		{
			outStream = getResponse().getOutputStream();
			outStream.write(bOffice);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			outStream.close();
		}
		return null;
	}

	public String showList() throws Exception
	{
		page = new Page<TInfoBaseInfoReport>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String rpTitle = getRequest().getParameter("reTitle");
			rpTitle = rpTitle.replace("%", "\'\'%");
			page = infoReportService.getReportsByTitle(page, rpTitle);
		}
		else
		{
		page = infoReportService.getReports(page);
		}
		JSONObject jsonObj = new JSONObject();
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("curpage", curpage);
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBaseInfoReport> result = page.getResult();
		
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(result!=null){
		for(TInfoBaseInfoReport one : result)
		{
			obj = new JSONObject();
			obj.put("rpId", one.getRpId());
			obj.put("rpTitle", one.getRpTitle());
			obj.put("rpDate", sdfTime.format(one.getRpDate()));
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
		pagePl = new Page<UsedReportDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String reTitle = getRequest().getParameter("reTitle");
			reTitle = reTitle.replace("%", "\'\'%");
			pagePl = jdbcInfoReportService.getReports(pagePl,reTitle);
		}
		else
		{
			String reTitle ="";
			pagePl = jdbcInfoReportService.getReports(pagePl,reTitle);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<UsedReportDto> result = pagePl.getResult();
		
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(UsedReportDto one : result)
		{
			obj = new JSONObject();
			obj.put("rpId", one.getRpId());
			obj.put("rpTitle", one.getRpTitle());
			obj.put("rpDate", sdfTime.format(one.getRpDate()));
			rows.add(obj);
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	
	public String word() throws Exception
	{
		Date sDate = DateTimes.ymd2DateTime(startDate);
		Date eDate = DateTimes.ymd2DateTime(endDate, true);
		List<TInfoBasePublish> lists = publishService.findPublishedByDate(sDate, eDate);
		Map<String, BigDecimal> totalScore = publishService.getPlusScore(lists);
		Map<String, BigDecimal> orgScore = publishService.getOrgScore(lists);
		Map<String, String> orgNames = orgService.getOrgNames();
		Set<String> tempOrgId = new HashSet<String>();
		for(TInfoBasePublish one : lists)
		{
			ReportDto rd = new ReportDto();
			String oId = one.getOrgId();
			rd.setOrgScore(orgScore.get(oId).toString());
			if(tempOrgId.contains(oId))
				rd.setIsFirst(false);
			else
				rd.setIsFirst(true);
			tempOrgId.add(oId);

			rd.setUseDate(one.getPubSubmitDate());
			rd.setOrgName(orgNames.get(oId));
			rd.setPubTitle(one.getPubTitle());
			if(one.getPubUseScore()!=null){
			rd.setUseScore(one.getPubUseScore().toString());
			}
			BigDecimal insScore = new BigDecimal(0);
			if(Publish.INSTRUCTION.equals(one.getPubIsInstruction()))
			{
				insScore = one.getPubRemarkScore();
			}
			rd.setRemarkScore(insScore.toString());

			rd.setInfoScore(totalScore.get(one.getPubId()).toString());
			
			BigDecimal jf = totalScore.get(one.getPubId());
			jf = jf.subtract(one.getPubUseScore());
			jf = jf.subtract(insScore);
			rd.setPlusScore(jf.toString());
			reports.add(rd);
		}
		return "word";
	}
	
	public String view() throws Exception
	{
		return "view";
	}
	
	
	public String report() throws Exception{
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		int year2 = Integer.parseInt(year1);
		years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		String month2 = "";
		int month1 = date.getMonth()+1;
		if(month1<10){
			month2 = "0"+month1;
		}
		else{
			month2 = month1 +"";
		}
		if(month==null){
			month = month2;
		}
		//通报开头部分
		app = infoReportService.adoptReports(year, month);
		Map<String, String> orgNames = orgService.getOrgNames();
		
		//循环5个组织机构类型
		for(int m=0;m<4;m++){
		list = infoReportService.getOrgReport(String.valueOf(m), year, month);
		for(int i=0;i<list.size();i++){
			
			String orgName = orgNames.get(list.get(i));
			String orgId = list.get(i).toString();
			
			List list3 = new ArrayList();
			List list7 = new ArrayList();
			List list8 = new ArrayList();
			List list9 = new ArrayList();
			List list10 = new ArrayList();
			List list11 = new ArrayList();
			List list2 = new ArrayList();
			List list12 = new ArrayList();
			int dqsum = 0;
			int sum = 0;
			String[] aid = new String[1];
			aid[0] = orgId;
			
				/*//呈批数量
				list10 = infoReportService.getOrgReport1(orgId, year, month);
				//呈阅数量
				list11 = infoReportService.getOrgReport5(orgId, year, month);
				//批转数量
				list8 = infoReportService.getOrgReport6(orgId, year, month);
				//呈国办数量
				list9 = infoReportService.getOrgReport9(orgId, year, month);
			
				//每日要情数量
				list3 = submitService.getStatisticsByYQ(aid, year, month);
				//南昌政务数量
				list2 = submitService.getStatisticsByZW(aid, year, month);
				//省办数量
				list7 = submitService.getStatisticsBySJ(aid, year, month);
				//本月加分数量
				list12 = submitService.getStatisticsByJF(aid, year, month);
				 //当前月得分
				dqsum = infoReportService.getOrgReportdqsum( orgId,  year, month);*/
				  //累计得分
				sum = infoReportService.getOrgReportsum( orgId,  year, month);
			String flag1="";
			String flag2="";
			String flag6="";
			String flag7="";
			String flag9="";
			int flag3 = 0;
			int flag4 = 0;
			int flag5 = 0;
			int flag8 = 0;
			int flag10 = 0;
			int flag11 = 0;
			int flag12 = 0;
			int flag13 = 0;
			if(list2.size()>0){
			if(((Object[])list2.get(0))[0]!=null)
			flag3 = Integer.valueOf(((Object[])list2.get(0))[1].toString()).intValue();
			}
			if(list3.size()>0){
			if(((Object[])list3.get(0))[0]!=null)
			flag4 = Integer.valueOf(((Object[])list3.get(0))[1].toString()).intValue();
			}
			if(list7.size()>0){
			if(((Object[])list7.get(0))[0]!=null)
			flag5 = Integer.valueOf(((Object[])list7.get(0))[1].toString()).intValue();
			}
			if(list10.size()>0){
				if(((Object[])list10.get(0))[0]!=null)
				flag8 = Integer.valueOf(((Object[])list10.get(0))[1].toString()).intValue();
			}
			if(list11.size()>0){
				if(((Object[])list11.get(0))[0]!=null)
					flag10 = Integer.valueOf(((Object[])list11.get(0))[1].toString()).intValue();
			}
			if(list8.size()>0){
				if(((Object[])list8.get(0))[0]!=null)
					flag11 = Integer.valueOf(((Object[])list8.get(0))[1].toString()).intValue();
			}
			if(list9.size()>0){
				if(((Object[])list9.get(0))[0]!=null)
					flag12 = Integer.valueOf(((Object[])list9.get(0))[1].toString()).intValue();
			}
			if(list12.size()>0){
				if(((Object[])list12.get(0))[0]!=null)
					flag13 = Integer.valueOf(((Object[])list12.get(0))[1].toString()).intValue();
			}
			List list6 = new ArrayList();
			List list4 = new ArrayList();
			List list5 = new ArrayList();
			List list5s = new ArrayList();
			List list4s = new ArrayList();
			List list6s = new ArrayList();
			List list7s = new ArrayList();
				//根据机构ID查出已成刊的文章
				list4 = infoReportService.getOrgReport3(orgId, year, month);
				//根据机构ID查出呈阅件的文章
				list5 = infoReportService.getOrgReport4(orgId, year, month);
				//根据机构ID查出上报国办的文章
				 list5s = infoReportService.getOrgReport4s(orgId, year, month);
				//根据机构ID查出省办的文章
				 list6s = infoReportService.getOrgReportSB(orgId, year, month);
				 //根据机构ID查出已成刊的文章只查询出差出访栏目信息数
				 list4s = infoReportService.getOrgReportTrip(orgId, year, month);
				 //根据机构ID查出已成刊的文章只查询出差出访栏目信息数
				 list7s = infoReportService.getOrgReportJF(orgId, year, month);
			
				for(int j=0;j<list4.size();j++){
					Object[] obj1 = (Object[])list4.get(j);
					String title= obj1[0].toString();
					if(title.substring(title.length()-1, title.length()).equals("。")){
						title = title.substring(0,title.length()-1);
					}
					obj1[0] = title;
					if("1".equals(obj1[1].toString())){
						obj1[1]="要情批示";
					}
					else if("每日要情".equals(obj1[2].toString())){
						obj1[1]="要情";
					}
					else if("南昌政务".equals(obj1[2].toString())){
						obj1[1]="南昌政务";
					}
					/*else{
						obj1[1]="（要情）";
					}*/
					list6.add(obj1);
				}
				for(int j=0;j<list4s.size();j++){
					Object[] obj2 = new Object[2];
						obj2[0]="出差出访 "+list4s.get(0).toString()+"条";
						obj2[1]="要情";
					if(!"0".equals(list4s.get(0).toString())){
						list6.add(obj2);
					}
				}
			
			for(int j=0;j<list5.size();j++){
				Object[] obj1 = (Object[])list5.get(j);
				if(obj1[1]!=null){
					//呈阅件呈批
				if("1".equals(obj1[1].toString()))
				{
					obj1[1]="呈批";
				}
				//呈阅件批转
				else if("2".equals(obj1[1].toString()))
				{
					obj1[1]="批转";
				}
				//送阅
				else if("3".equals(obj1[1].toString()))
				{
					obj1[1]="送阅";
				}
				//专报
				else if("4".equals(obj1[1].toString()))
				{
					obj1[1]="专报";
				}
				//专报指示
				else if("5".equals(obj1[1].toString()))
				{
					obj1[1]="专报批示";
				}
				//呈阅件
				else
				{
					obj1[1]="呈阅";
				}
				if(obj1[2]!=null){
					String oname = orgNames.get(obj1[2].toString());
					/*if(oname.indexOf("政府")>-1){
						oname = oname.replaceAll("政府", "");
					}*/
					String title= obj1[0].toString();
					if(title.substring(title.length()-1, title.length()).equals("。")){
						title = title.substring(0,title.length()-1);
					}
					//obj1[0] = title+"("+oname+")";
					obj1[0] = title;
				}
				list6.add(obj1);
				}
				
			}
			for(int j=0;j<list5s.size();j++){
				Object[] obj1 = (Object[])list5s.get(j);
				
				if(obj1[1]!=null){
					if("0".equals(String.valueOf(obj1[1]))){
						obj1[1]="报国办";
					}
					else if("1".equals(String.valueOf(obj1[1]))){
						obj1[1]="报国办被采用";
					}
					else if("2".equals(String.valueOf(obj1[1]))){
						obj1[1]="报国办被采用并批示";
					}
					else if("3".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿";
					}
					else if("4".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿被采用";
					}
					else if("5".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿被采用并批示";
					}
					else if("6".equals(String.valueOf(obj1[1]))){
						obj1[1]="核国办稿";
					}
					else if("7".equals(String.valueOf(obj1[1]))){
						obj1[1]="核国办稿被采用";
					}
					else if("8".equals(String.valueOf(obj1[1]))){
						obj1[1]="核国办稿被采用并批示";
					}
					else if("9".equals(String.valueOf(obj1[1]))){
						obj1[1]="核国办稿被采用并批示";
					}
					else if("10".equals(String.valueOf(obj1[1]))){
						obj1[1]="核国办稿约稿被采用";
					}
					else if("11".equals(String.valueOf(obj1[1]))){
						obj1[1]="核国办稿约稿被采用并批示";
					}
					else if("12".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办综合采用";
					}
					else if("13".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办要情采用";
					}
					else if("14".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办专报采用";
					}
					else if("15".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办综合采用并批示";
					}
					else if("16".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办要情采用并批示";
					}
					else if("17".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办专报采用并批示";
					}
					else if("18".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿综合采用";
					}
					else if("19".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿要情采用";
					}
					else if("20".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿专报采用";
					}
					else if("21".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿综合采用并批示";
					}
					else if("22".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿要情采用并批示";
					}
					else if("23".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿专报采用并批示";
					}
					
				if(obj1[2]!=null){
					String oname = orgNames.get(obj1[2].toString());
					/*if(oname.indexOf("政府")>-1){
						oname = oname.replaceAll("政府", "");
					}*/
					String title= obj1[0].toString();
					if(title.substring(title.length()-1, title.length()).equals("。")){
						title = title.substring(0,title.length()-1);
					}
					//obj1[0] = title+"("+oname+")";
					obj1[0] = title;
				}
				list6.add(obj1);
				}
			}
			
			for(int j=0;j<list6s.size();j++){
				Object[] obj1 = (Object[])list6s.get(j);
				
				if(obj1[1]!=null){
					obj1[1] = "省办";	
				if(obj1[2]!=null){
					String oname = orgNames.get(obj1[2].toString());
					/*if(oname.indexOf("政府")>-1){
						oname = oname.replaceAll("政府", "");
					}*/
					String title= obj1[0].toString();
					if(title.substring(title.length()-1, title.length()).equals("。")){
						title = title.substring(0,title.length()-1);
					}
					//obj1[0] = title+"("+oname+")";
					obj1[0] = title;
				}
				list6.add(obj1);
				}
			}
			
			for(int j=0;j<list7s.size();j++){
				Object[] obj1 = (Object[])list7s.get(j);
				
				if(obj1[1]!=null){
					if("0".equals(String.valueOf(obj1[1]))){
						obj1[1]="微博";
					}
					else if("1".equals(String.valueOf(obj1[1]))){
						obj1[1]="创先争优简报";
					}
					else if("2".equals(String.valueOf(obj1[1]))){
						obj1[1]="跟班学习";
					}
					else if("3".equals(String.valueOf(obj1[1]))){
						obj1[1]="省外约稿";
					}
					else if("4".equals(String.valueOf(obj1[1]))){
						obj1[1]="国办约稿";
					}
				if(obj1[2]!=null){
					//String oname = orgNames.get(obj1[2].toString());
					/*if(oname.indexOf("政府")>-1){
						oname = oname.replaceAll("政府", "");
					}*/
					String title= obj1[0].toString();
					if(title.substring(title.length()-1, title.length()).equals("。")){
						title = title.substring(0,title.length()-1);
					}
					//obj1[0] = title+"("+oname+")";
					obj1[0] = title;
				}
				list6.add(obj1);
				}
			}
			String content[] = new String[12];
			content[0]= orgId;
			/*if(orgName.indexOf("政府")>-1){
				orgName = orgName.replaceAll("政府", "");
			}*/
			content[1]=orgName;
			content[2]=String.valueOf(dqsum);
			content[3]=String.valueOf(sum);
			/*//南昌政务
			content[4]=String.valueOf(flag3);
			//每日要情
			content[5]=String.valueOf(flag4);
			//省办
			content[6]=String.valueOf(flag5);
			//呈批
			content[7]=String.valueOf(flag8);
			//呈阅
			content[8]=String.valueOf(flag10);
			//批转
			content[9]=String.valueOf(flag11);
			//呈国办
			content[10]=String.valueOf(flag12);
			//本月加分
			content[11]=String.valueOf(flag13);*/
			Object[] obj= new Object[2];
			obj[0] = content;
			obj[1] = list6;
			if(m==0){
			tongbao1.add(i, obj);
			}
			else if(m==1){
				tongbao2.add(i, obj);
			}
			else if(m==2){
				tongbao3.add(i, obj);
			}
			else if(m==3){
				tongbao4.add(i, obj);
			}
			else if(m==4){
				tongbao5.add(i, obj);
			}
		}
		}
		DateToChange dd  = new DateToChange();
		year = dd.toYear(year);
		month = dd.toMonth(month);
		return "report";
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

	public Page<TInfoBaseInfoReport> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBaseInfoReport> page)
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

	public String getTbTitle()
	{
		return tbTitle;
	}

	public void setTbTitle(String tbTitle)
	{
		this.tbTitle = tbTitle;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public List<ReportDto> getReports()
	{
		return reports;
	}

	public void setReports(List<ReportDto> reports)
	{
		this.reports = reports;
	}

	public String getIsPublish()
	{
		return isPublish;
	}

	public void setIsPublish(String isPublish)
	{
		this.isPublish = isPublish;
	}

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public Page<UsedReportDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<UsedReportDto> pagePl) {
		this.pagePl = pagePl;
	}

	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public int[] getApp() {
		return app;
	}

	public void setApp(int[] app) {
		this.app = app;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public List getList1() {
		return list1;
	}

	public void setList1(List list1) {
		this.list1 = list1;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int[] getYears() {
		return years;
	}

	public void setYears(int[] years) {
		this.years = years;
	}

	public List getTongbao1() {
		return tongbao1;
	}

	public void setTongbao1(List tongbao1) {
		this.tongbao1 = tongbao1;
	}

	public List getTongbao2() {
		return tongbao2;
	}

	public void setTongbao2(List tongbao2) {
		this.tongbao2 = tongbao2;
	}

	public List getTongbao3() {
		return tongbao3;
	}

	public void setTongbao3(List tongbao3) {
		this.tongbao3 = tongbao3;
	}

	public List getTongbao4() {
		return tongbao4;
	}

	public void setTongbao4(List tongbao4) {
		this.tongbao4 = tongbao4;
	}

	public List getTongbao5() {
		return tongbao5;
	}

	public void setTongbao5(List tongbao5) {
		this.tongbao5 = tongbao5;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	

	
	
}
