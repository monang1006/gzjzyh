package com.strongit.xxbs.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javassist.compiler.ast.Stmnt;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.utils.BaseDataExportInfo;
import com.strongit.utils.ProcessXSL;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.IUserService;
import com.strongit.xxbs.dto.StatisticsDto;
import com.strongit.xxbs.service.IColumnService;
import com.strongit.xxbs.service.IInfoReportService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.ISubmitService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class StatisticsAction extends BaseActionSupport<TUumsBaseOrg>
{
	private static final long serialVersionUID = 1L;
	
	private ISubmitService submitService;
	@Autowired private IUserService userService;
	private IOrgService orgService;
	private IColumnService columnService;
	private IInfoReportService infoReportService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TUumsBaseOrg> page;
	private String useStatus = "4";
	private String orgType="";
	private List list2;
	private List list3;
	private List list4;
	private int[] years;
	private String year;
	private List list5;
	private String month;
	private List list6;
	private String flag;
	private List list7;
	private List list8;
	private List list9;
	private List list10;
	private List list11;
	private List list12;
	private List list13;
	
	@Autowired
	public void setInfoReportService(IInfoReportService infoReportService) {
		this.infoReportService = infoReportService;
	}

	@Autowired
	public void setColumnService(IColumnService columnService) {
		this.columnService = columnService;
	}

	@Autowired
	public void setSubmitService(ISubmitService submitService)
	{
		this.submitService = submitService;
	}
	
	@Autowired
	public void setOrgService(IOrgService orgService)
	{
		this.orgService = orgService;
	}

	public TUumsBaseOrg getModel()
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
	
	public String indexList() throws Exception
	{
		JSONObject jsonObj = new JSONObject();

		Map<String, String> orgNames = orgService.getOrgNames();
		
		TreeSet<StatisticsDto> result = submitService.getTotalStatistics();
		
		JSONArray rows = new JSONArray();
		for(StatisticsDto one : result)
		{
			JSONObject obj = new JSONObject();
			obj.put("orgName", orgNames.get(one.getOrgId()));
			
			Long total = 0L;			
			total = one.getTotal();
			
			obj.put("total", total.toString());
			
			rows.add(obj);
		}
		jsonObj.put("rows", rows);

		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String showList() throws Exception
	{
		page = new Page<TUumsBaseOrg>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		List list =new ArrayList();
		String pname[]  = new String[]{"isOrg","0"};
		list.add(pname);
		List list1 = new ArrayList();
		list1.add(orgType);
		//page = orgManage.findByProperty(page, TUumsBaseOrg.class, list, list1.toArray(), null);
		
		//page = orgManage.queryOrgs(page, "0");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		

		/*Map<String, String> mapOrgs =
				new HashMap<String, String>();
		mapOrgs = orgService.getOrgNames();*/
		String a[] = orgService.getOrgIdsByisOrg(orgType);
		List org = new ArrayList(); 
		org = submitService.getStatistics1(a,useStatus,curpage,unitpage);
		//List<TUumsBaseOrg> result = page.getResult();
		JSONArray rows = new JSONArray();
		for(int i=0;i<org.size();i++)
		{
			JSONObject obj = new JSONObject();
			Object[] obj1 = (Object[]) org.get(i);
			if(obj1[1]==null){
				obj1[1]="0";
			}
			if(obj1[2]==null){
				obj1[2]="0";
			}
			if(obj1[3]==null){
				obj1[3]="0";
			}
			if(obj1[4]==null){
				obj1[4]="0";
			}
			String orgName = obj1[0].toString();
			String total = obj1[1].toString();
			String byDay = obj1[2].toString();
			String byYear = obj1[3].toString();
			String byMonth = obj1[4].toString();
			obj.put("orgName", orgName);
			obj.put("total", total.toString());
			obj.put("byYear", byYear.toString());
			obj.put("byMonth", byMonth.toString());
			obj.put("byDay", byDay.toString());
			rows.add(obj);
			/*StatisticsDto stat = mapStats.get(orgId);

			Long total = 0L;
			Long byYear = 0L;
			Long byMonth = 0L;
			Long byDay = 0L;
			
			if(stat != null)
			{
				total = stat.getTotal();
				byYear = stat.getByYear();
				byMonth = stat.getByMonth();
				byDay = stat.getByDay();
			}
			
			obj.put("total", total.toString());
			obj.put("byYear", byYear.toString());
			obj.put("byMonth", byMonth.toString());
			obj.put("byDay", byDay.toString());
			
			rows.add(obj);*/
		}
		jsonObj.put("rows", rows);

		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	/**
	 * 根据年份统计各机构得分
	 * @return
	 */
	public String point(){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		int year2 = Integer.parseInt(year1);
		years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		if(orgType.equals("all")){
			String b[] = orgService.getOrgIdsByisOrg("2");
			list2 = submitService.getStatistics2(b,year);
			list4 = submitService.getStatistics4(b,year);
			list5 = orgService.getOrgIdsByisOrg1("1");
			return "point1";
		}
		if(orgType.equals("1")){
			String a[] = orgService.getOrgIdsByisOrg("1");
			String b[] = orgService.getOrgIdsByisOrg("2");
			list2 = submitService.getStatistics11(a,year,b);
			list3 = submitService.getStatistics12(a,year,b);
			return "point";
		}
		if(orgType.equals("3")){
			String a[] = orgService.getOrgIdsByisOrg(orgType);
			list2 = submitService.getStatistics2(a,year);
			list3 = submitService.getStatistics3(a,year);
			return "point";
		}
		String b[] = orgService.getDeptOrgIds();
		list2 = submitService.getStatistics2(b,year);
		list3 = submitService.getStatistics3(b,year);
		return "point";
	}
	
	
	/**
	 * 根据年份统计各机构上报和采用信息按上报信息排序
	 * @return
	 */
	public String findSubmit(){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		int year2 = Integer.parseInt(year1);
		years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		//县级机构
		if(orgType.equals("all")){
			String b[] = orgService.getOrgIdsByisOrg("2");
			//每月上报数量
			list2 = submitService.getStatisticsByMonthSubmit(b,year);
			//每月采用数量
			list5 = submitService.getStatisticsByMonthUse(b,year);
			//当前年上报数量
			list3 = submitService.getStatisticsByXJYearSubmit(b,year);
			//当前年采用数量
			list4 = submitService.getStatisticsByYearUse(b,year);
			list6 = orgService.getOrgIdsByisOrg1("1");
			return "findSubmit1";
		}
		//市级机构
		if(orgType.equals("1")){
			String a[] = orgService.getOrgIdsByisOrg("1");
			String b[] = orgService.getOrgIdsByisOrg("2");
			//每月上报数量
			list2 = submitService.getStatisticsBySJMonthSubmit(a,year,b);
			//每月采用数量
			list5 = submitService.getStatisticsBySJMonthUse(a,year,b);
			//当前年上报数量
			list3 = submitService.getStatisticsBySJYearSubmit(a,year,b);
			//当前年采用数量
			list4 = submitService.getStatisticsBySJYearUse(a,year,b);
			return "findSubmit";
		}
		//驻外办
		if(orgType.equals("3")){
			String a[] = orgService.getOrgIdsByisOrg(orgType);
			//每月上报数量
			list2 = submitService.getStatisticsByMonthSubmit(a,year);
			//每月采用数量
			list5 = submitService.getStatisticsByMonthUse(a,year);
			//当前年上报数量
			list3 = submitService.getStatisticsByYearSubmit(a,year);
			//当前年采用数量
			list4 = submitService.getStatisticsByYearUse(a,year);
			return "findSubmit";
		}
		//所有
		if(orgType.equals("4")){
			String a[] = orgService.getOrgIdsBySubmit();
			//每月上报数量
			list2 = submitService.getStatisticsByMonthSubmit(a,year);
			//每月采用数量
			list5 = submitService.getStatisticsByMonthUse(a,year);
			//当前年上报数量
			list3 = submitService.getStatisticsByYearSubmit(a,year);
			//当前年采用数量
			list4 = submitService.getStatisticsByYearUse(a,year);
			return "findSubmit";
		}
		String b[] = orgService.getDeptOrgIds();
		//每月上报数量
		list2 = submitService.getStatisticsByMonthSubmit(b,year);
		//每月采用数量
		list5 = submitService.getStatisticsByMonthUse(b,year);
		//当前年上报数量
		list3 = submitService.getStatisticsByYearSubmit(b,year);
		//当前年采用数量
		list4 = submitService.getStatisticsByYearUse(b,year);
		return "findSubmit";
	}
	
	/**
	 * 根据年份统计各机构上报采用信息按采用信息排序
	 * @return
	 */
	public String findUse(){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		int year2 = Integer.parseInt(year1);
		years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		//县级机构
		if(orgType.equals("all")){
			String b[] = orgService.getOrgIdsByisOrg("2");
			//每月上报数量
			list2 = submitService.getStatisticsByMonthSubmit(b,year);
			//每月采用数量
			list5 = submitService.getStatisticsByMonthUse(b,year);
			//当前年采用数量
			list3 = submitService.getStatisticsByXJYearUseOrder(b,year);
			//当前年上报数量
			list4 = submitService.getStatisticsByYearSubmitOrder(b,year);
			list6 = orgService.getOrgIdsByisOrg1("1");
			return "findUse1";
		}
		//市级机构
		if(orgType.equals("1")){
			String a[] = orgService.getOrgIdsByisOrg("1");
			String b[] = orgService.getOrgIdsByisOrg("2");
			//每月上报数量
			list2 = submitService.getStatisticsBySJMonthSubmit(a,year,b);
			//每月采用数量
			list5 = submitService.getStatisticsBySJMonthUse(a,year,b);
			//当前年采用数量
			list3 = submitService.getStatisticsBySJYearUseOrder(a,year,b);
			//当前年上报数量
			list4 = submitService.getStatisticsBySJYearSubmitOrder(a,year,b);
			return "findUse";
		}
		//驻外办
		if(orgType.equals("3")){
			String a[] = orgService.getOrgIdsByisOrg(orgType);
			//每月上报数量
			list2 = submitService.getStatisticsByMonthSubmit(a,year);
			//每月采用数量
			list5 = submitService.getStatisticsByMonthUse(a,year);
			//当前年采用数量
			list3 = submitService.getStatisticsByYearUseOrder(a,year);
			//当前年上报数量
			list4 = submitService.getStatisticsByYearSubmitOrder(a,year);
			return "findUse";
		}
		//所有
		if(orgType.equals("4")){
			String a[] = orgService.getOrgIdsBySubmit();
			//每月上报数量
			list2 = submitService.getStatisticsByMonthSubmit(a,year);
			//每月采用数量
			list5 = submitService.getStatisticsByMonthUse(a,year);
			//当前年采用数量
			list3 = submitService.getStatisticsByYearUseOrder(a,year);
			//当前年上报数量
			list4 = submitService.getStatisticsByYearSubmitOrder(a,year);
			return "findUse";
		}
		String b[] = orgService.getDeptOrgIds();
		//每月上报数量
		list2 = submitService.getStatisticsByMonthSubmit(b,year);
		//每月采用数量
		list5 = submitService.getStatisticsByMonthUse(b,year);
		//当前年采用数量
		list3 = submitService.getStatisticsByYearUseOrder(b,year);
		//当前年上报数量
		list4 = submitService.getStatisticsByYearSubmitOrder(b,year);
		return "findUse";
	}
	
	/**
	 * 根据年月统计各处室在栏目下被采用的文章数
	 * @return
	 */
	public String findOrg(){
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
		
		list5 = orgService.getOrgIdsByisOrg1("4");
		//list2 = columnService.getColumns();
		//查询各处室信息采用情况（领导批示活动及发文）通报
		list3 = submitService.getStatistics5(year,month);
		//查询各处室信息采用情况（其他信息）通报
		list2 = submitService.getStatistics6(year,month);
		
		//查询各处室信息采用情况（其他信息呈国办或呈阅件）通报
		list6 = submitService.getStatistics6s(year,month);
		
		//查询各处室信息采用情况（得分）通报
		list4 = submitService.getStatistics7(year, month);
		return "findOrg";
	}
	
	/**
	 * 南昌市政府根据年份和月份统计采用、得分和排名情况
	 * @return
	 */
	public String findCode(){
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
		
		String b[] = null;
		if(orgType!=null){
			b = orgService.getOrgIdsByisOrg(orgType);
		}
		else{
			b = orgService.getDeptOrgIds();
		}
		list2 = submitService.getStatisticsByYQ(b, year, month);
		list3 = submitService.getStatisticsByZW(b, year, month);
		list4 = submitService.getStatisticsByPS(b, year, month);
		list5 = submitService.getStatisticsBySJYQ(b, year, month);
		list6 = submitService.getStatisticsBySJZW(b, year, month);
		list7 = submitService.getStatisticsBySJPS(b, year, month);
		list8 = submitService.getStatisticsByGBXX(b, year, month);
		list9 = submitService.getStatisticsByGBXXPS(b, year, month);
		list10 = submitService.getStatisticsByJF(b, year, month);
		list11 = submitService.getStatisticsByDF(b, year, month);
		list12 = submitService.getStatisticsBySYLJ(b, year, month);
		list13 = submitService.getStatisticsByLJDF(b, year, month);
		return "findCode";
	}
	
	/**
	 * 各地市导出excel
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String excel() throws UnsupportedEncodingException{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		//int year2 = Integer.parseInt(year1);
		//years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		/*if(orgType.equals("all")){
			String b[] = orgService.getOrgIdsByisOrg("2");
			list2 = submitService.getStatisticcols(b,year);
			list4 = submitService.getStatistics4(b,year);
			list5 = orgService.getOrgIdsByisOrg1("1");
			return "point1";
		}*/
		String orgType = getRequest().getParameter("orgType");
		if("0".equals(orgType))
		{
			String b[] = orgService.getDeptOrgIds();
			list2 = submitService.getStatistics2(b,year);
			list3 = submitService.getStatistics3(b,year);
		}
		else if("1".equals(orgType))
		{
			String a[] = orgService.getOrgIdsByisOrg("1");
			String b[] = orgService.getOrgIdsByisOrg("2");
			list2 = submitService.getStatistics11(a,year,b);
			list3 = submitService.getStatistics12(a,year,b);
		}
		else
		{
			String a[] = orgService.getOrgIdsByisOrg(orgType);
			list2 = submitService.getStatistics2(a,year);
			list3 = submitService.getStatistics3(a,year);
		}
		Map m = new HashMap<String, String>();
		for(int j=0;j<list2.size();j++){
			m.put(((Object[])list2.get(j))[0]+"#"+((Object[])list2.get(j))[1],((Object[])list2.get(j))[2]);
			System.out.println(((Object[])list2.get(j))[0]+"#"+((Object[])list2.get(j))[1]+"="+((Object[])list2.get(j))[2]);
		}
		
		
		
		
		BaseDataExportInfo export = new BaseDataExportInfo();
		if("0".equals(orgType)){
		export.setWorkbookFileName(URLEncoder.encode(year+"年省政府部门及相关单位信息采用情况", "utf-8"));
	    export.setSheetTitle(year +"年省政府部门及相关单位信息采用情况");
	    export.setSheetName(year +"年省政府部门及相关单位信息采用情况");
		}
		else if("1".equals(orgType)){
			export.setWorkbookFileName(URLEncoder.encode(year+"年全省各设区市信息采用情况", "utf-8"));
		    export.setSheetTitle(year +"年全省各设区市信息采用情况");
		    export.setSheetName(year +"年全省各设区市信息采用情况");
		}
		else if("3".equals(orgType)){
			export.setWorkbookFileName(URLEncoder.encode(year+"年省各驻外办信息采用情况", "utf-8"));
		    export.setSheetTitle(year +"年省各驻外办信息采用情况");
		    export.setSheetName(year +"年省各驻外办信息采用情况");
		}
		List s1 = new ArrayList();
		s1.add("机构名称");
		s1.add("1月");
		s1.add("2月");
		s1.add("3月");
		s1.add("4月");
		s1.add("5月");
		s1.add("6月");
		s1.add("7月");
		s1.add("8月");
		s1.add("9月");
		s1.add("10月");
		s1.add("11月");
		s1.add("12月");
		s1.add("得分");
		s1.add("排名");
		export.setTableHead(s1);
		List s2 = new ArrayList();
		for(int i=0;i<list3.size();i++){
			Vector cols = new Vector();
			cols.add(0,String.valueOf(((Object[])list3.get(i))[1]));
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01")!=null){
				cols.add(1,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01")));
			}else{
				cols.add(1,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02")!=null){
				cols.add(2,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02")));
			}else{
				cols.add(2,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03")!=null){
				cols.add(3,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03")));
			}else{
				cols.add(3,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04")!=null){
				cols.add(4,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04")));
			}else{
				cols.add(4,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05")!=null){
				cols.add(5,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05")));
			}else{
				cols.add(5,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06")!=null){
				cols.add(6,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06")));
			}else{
				cols.add(6,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07")!=null){
				cols.add(7,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07")));
			}else{
				cols.add(7,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08")!=null){
				cols.add(8,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08")));
			}else{
				cols.add(8,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09")!=null){
				cols.add(9,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09")));
			}else{
				cols.add(9,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10")!=null){
			    cols.add(10,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10")));
			}else{
				cols.add(10,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11")!=null){
			    cols.add(11,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11")));
			}else{
				cols.add(11,"");
			}
			if(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12")!=null){
			    cols.add(12,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12")));
			    System.out.println(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12"));
			}else{
				cols.add(12,"");
			}
			if("0".equals(String.valueOf(((Object[])list3.get(i))[2]))){
				cols.add(13,"");
			}else{
			cols.add(13,String.valueOf(((Object[])list3.get(i))[2]));
			}
			cols.add(14,String.valueOf(i+1));
			s2.add(cols);
		}
		 export.setRowList(s2);
		 ProcessXSL xsl = new ProcessXSL();
		 xsl.createWorkBookSheet2(export);
		 xsl.writeWorkBook(getResponse());
		return null;
	}
	
	
	/**
	 * 各县政府导出excel
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String excel1() throws UnsupportedEncodingException{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		//int year2 = Integer.parseInt(year1);
		//years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		String b[] = orgService.getOrgIdsByisOrg("2");
		list2 = submitService.getStatistics2(b,year);
		list4 = submitService.getStatistics4(b,year);
		list5 = orgService.getOrgIdsByisOrg1("1");
		Map mm = new HashMap<String , String>();
		for(int m=0;m<list5.size();m++){
			mm.put(((TUumsBaseOrg)list5.get(m)).getOrgSyscode(), ((TUumsBaseOrg)list5.get(m)).getOrgName());
		}
		//通过机构代码获得机构名称
		Map m = new HashMap<String, String>();
		for(int j=0;j<list2.size();j++){
			m.put(((Object[])list2.get(j))[0]+"#"+((Object[])list2.get(j))[1],((Object[])list2.get(j))[2]);
			System.out.println(((Object[])list2.get(j))[0]+"#"+((Object[])list2.get(j))[1]+"="+((Object[])list2.get(j))[2]);
		}
		
		
		
		//构建表头
		BaseDataExportInfo export = new BaseDataExportInfo();
		export.setWorkbookFileName(year+URLEncoder.encode("年1-12月全省各县（市、区）在设区市信息采用情况", "utf-8"));
	    export.setSheetTitle(year +"年1-12月全省各县（市、区）在设区市信息采用情况");
	    export.setSheetName(year +"年1-12月全省各县（市、区）在设区市信息采用情况");
		List s1 = new ArrayList();
		s1.add("机构名称");
		s1.add("");
		s1.add("1月");
		s1.add("2月");
		s1.add("3月");
		s1.add("4月");
		s1.add("5月");
		s1.add("6月");
		s1.add("7月");
		s1.add("8月");
		s1.add("9月");
		s1.add("10月");
		s1.add("11月");
		s1.add("12月");
		s1.add("得分");
		s1.add("排名");
		export.setTableHead(s1);
		List s2 = new ArrayList();
		//循环往表体里面插数据
		for(int i=0;i<list4.size();i++){
			Vector cols = new Vector();
			
			cols.add(0,String.valueOf(mm.get(((Object[])list4.get(i))[3])));
			cols.add(1,String.valueOf(((Object[])list4.get(i))[1]));
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-01")!=null){
				cols.add(2,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-01")));
			}else{
				cols.add(2,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-02")!=null){
				cols.add(3,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-02")));
			}else{
				cols.add(3,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-03")!=null){
				cols.add(4,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-03")));
			}else{
				cols.add(4,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-04")!=null){
				cols.add(5,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-04")));
			}else{
				cols.add(5,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-05")!=null){
				cols.add(6,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-05")));
			}else{
				cols.add(6,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-06")!=null){
				cols.add(7,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-06")));
			}else{
				cols.add(7,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-07")!=null){
				cols.add(8,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-07")));
			}else{
				cols.add(8,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-08")!=null){
				cols.add(9,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-08")));
			}else{
				cols.add(9,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-09")!=null){
				cols.add(10,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-09")));
			}else{
				cols.add(10,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-10")!=null){
			    cols.add(11,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-10")));
			}else{
				cols.add(11,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-11")!=null){
			    cols.add(12,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-11")));
			}else{
				cols.add(12,"");
			}
			if(m.get(((Object[])list4.get(i))[0]+"#"+year+"-12")!=null){
			    cols.add(13,String.valueOf(m.get(((Object[])list4.get(i))[0]+"#"+year+"-12")));
			    System.out.println(m.get(((Object[])list4.get(i))[0]+"#"+year+"-12"));
			}else{
				cols.add(13,"");
			}
			if("0".equals(String.valueOf(((Object[])list4.get(i))[2]))){
			cols.add(14,"");
			}else{
				cols.add(14,String.valueOf(((Object[])list4.get(i))[2]));
			}
			cols.add(15,String.valueOf(i+1));
			
			s2.add(cols);
		}
		 export.setRowList(s2);
		 ProcessXSL xsl = new ProcessXSL();
		 xsl.createWorkBookSheet1(export);
		 xsl.writeWorkBook(getResponse());
		return null;
	}
	
	/**
	 * 各处室导出excel
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String excel2() throws UnsupportedEncodingException{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		int year2 = Integer.parseInt(year1);
		years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		int month1 = date.getMonth()+1;
		String month2 = month1 +"";
		if(month==null){
			month = month2;
		}
		list5 = orgService.getOrgIdsByisOrg1("4");
		//list2 = columnService.getColumns();
		//查询各处室信息采用情况（领导批示活动及发文）通报
		list3 = submitService.getStatistics5(year,month);
		//查询各处室信息采用情况（其他信息）通报
		list2 = submitService.getStatistics6(year,month);
		//查询各处室信息采用情况（得分）通报
		list4 = submitService.getStatistics7(year, month);
		
		//查询各处室信息采用情况（其他信息呈国办或呈阅件）通报
		list6 = submitService.getStatistics6s(year,month);
		
		Map m1 = new HashMap<String, String>();
		if(list3.size()>0){
		for(int j=0;j<list3.size();j++){
			m1.put(((Object[])list3.get(j))[0],((Object[])list3.get(j))[1]);
		}
		}
		Map m2 = new HashMap<String, String>();
		if(list2.size()>0){
		for(int j=0;j<list2.size();j++){
			if(m2.get(((Object[])list2.get(j))[0]) == null){
				m2.put(((Object[])list2.get(j))[0], ((Object[])list2.get(j))[2]+":"+((Object[])list2.get(j))[3]);
			}else{
				m2.put(((Object[])list2.get(j))[0], m2.get(((Object[])list2.get(j))[0])+","+((Object[])list2.get(j))[2]+":"+((Object[])list2.get(j))[3]);
			}
			//st2 = st2 + ((Object[])list2.get(j))[2]+":"+((Object[])list2.get(j))[3]+",";
			//m2.put(((Object[])list2.get(j))[0],((Object[])list2.get(j))[2]+":"+((Object[])list2.get(j))[3]);
		}
		}
		if(list6.size()>0){
		for(int j=0;j<list6.size();j++){
			String ss = "";
			if(((Object[])list6.get(j))[1].equals("1")){
				ss="呈国办";
			}
			else{
				ss= "呈阅件";
			}
			ss=ss+":"+((Object[])list6.get(j))[2];
			
			if(m2.get(((Object[])list6.get(j))[0]) == null){
				m2.put(((Object[])list6.get(j))[0], ss);
			}else{
				m2.put(((Object[])list6.get(j))[0], m2.get(((Object[])list6.get(j))[0])+","+ss);
			}
			/*
			if(((Object[])list6.get(j))[0].equals(((Object[])list6.get(j+1))[0])){
				String ss = "";
				if(((Object[])list6.get(j+1))[1].equals("1")){
					ss="呈国办";
				}
				else{
					ss= "呈阅件";
				}
				st3 = st3 + ss+":"+((Object[])list6.get(j+1))[2]+",";
			}
			else{
				m2.put(((Object[])list6.get(j))[0],st3);
				String ss = "";
				if(((Object[])list6.get(j+1))[1].equals("1")){
					ss="呈国办";
				}
				else{
					ss= "呈阅件";
				}
				st3=ss+":"+((Object[])list6.get(j+1))[2]+",";
			}
			*/
			//st2 = st2 + ((Object[])list2.get(j))[2]+":"+((Object[])list2.get(j))[3]+",";
			//m2.put(((Object[])list2.get(j))[0],((Object[])list2.get(j))[2]+":"+((Object[])list2.get(j))[3]);
		}
		}
		Map m3 = new HashMap<String, String>();
		if(list4.size()>0){
		for(int j=0;j<list4.size();j++){
			m3.put(((Object[])list4.get(j))[0],((Object[])list4.get(j))[1]);
		}
		}
		
		
		BaseDataExportInfo export = new BaseDataExportInfo();
		export.setWorkbookFileName(URLEncoder.encode(year+"年"+month+"月份各处室信息采用情况通报", "utf-8") );
	    export.setSheetTitle(year+"年"+month+"月份各处室信息采用情况通报");
	    export.setSheetName(year+"年"+month+"月份各处室信息采用情况通报");
		List s1 = new ArrayList();
		s1.add("序号");
		s1.add("单位");
		s1.add("领导批示活动及发文");
		s1.add("其他信息");
		s1.add("得分");
		export.setTableHead(s1);
		List s2 = new ArrayList();
		for(int i=0;i<list5.size();i++){
			Vector cols = new Vector();
			cols.add(0,String.valueOf(i+1));
			cols.add(1,((List<TUumsBaseOrg>)list5).get(i).getOrgName());
			String orgId =((List<TUumsBaseOrg>)list5).get(i).getOrgId();
			if(m1.get(orgId)!=null){
			    cols.add(2,String.valueOf(m1.get(orgId)));
			}else{
				cols.add(2,"");
			}
			if(m2.get(orgId)!=null){
			    cols.add(3,String.valueOf(m2.get(orgId)));
			}else{
				cols.add(3,"");
			}
			if("0".equals(String.valueOf(m3.get(orgId)))){
			    cols.add(4,"");
			}else{
				cols.add(4,String.valueOf(m3.get(orgId)));
			}
			s2.add(cols);
		}
		 export.setRowList(s2);
		 ProcessXSL xsl = new ProcessXSL();
		 xsl.createWorkBookSheet(export);
		 xsl.writeWorkBook(getResponse());
		return null;
	}
	
	
	/**
	 * 各地市上报统计导出excel
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String excelSubmit() throws UnsupportedEncodingException{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		//int year2 = Integer.parseInt(year1);
		//years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		/*if(orgType.equals("all")){
			String b[] = orgService.getOrgIdsByisOrg("2");
			list2 = submitService.getStatisticcols(b,year);
			list4 = submitService.getStatistics4(b,year);
			list5 = orgService.getOrgIdsByisOrg1("1");
			return "point1";
		}*/
		
		String orgType = getRequest().getParameter("orgType");
		String flag = getRequest().getParameter("flag");
		//按采用信息排名
		if("Use".equals(flag))
		{
			if("0".equals(orgType))
			{
				String a[] = orgService.getDeptOrgIds();
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(a,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(a,year);
				//当前年采用数量
				list3 = submitService.getStatisticsByYearUseOrder(a,year);
				//当前年上报数量
				list4 = submitService.getStatisticsByYearSubmitOrder(a,year);
			}
			else if("1".equals(orgType))
			{
				String a[] = orgService.getOrgIdsByisOrg("1");
				String b[] = orgService.getOrgIdsByisOrg("2");
				//每月上报数量
				list2 = submitService.getStatisticsBySJMonthSubmit(a,year,b);
				//每月采用数量
				list5 = submitService.getStatisticsBySJMonthUse(a,year,b);
				//当前年采用数量
				list3 = submitService.getStatisticsBySJYearUseOrder(a,year,b);
				//当前年上报数量
				list4 = submitService.getStatisticsBySJYearSubmitOrder(a,year,b);
			}	
			else
			{
				String a[] = orgService.getOrgIdsByisOrg(orgType);
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(a,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(a,year);
				//当前年采用数量
				list3 = submitService.getStatisticsByYearUseOrder(a,year);
				//当前年上报数量
				list4 = submitService.getStatisticsByYearSubmitOrder(a,year);
			}
		}
		//按上报信息排名
		else
		{
			if("0".equals(orgType))
			{
				String a[] = orgService.getDeptOrgIds();
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(a,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(a,year);
				//当前年上报数量
				list3 = submitService.getStatisticsByYearSubmit(a,year);
				//当前年采用数量
				list4 = submitService.getStatisticsByYearUse(a,year);
			}
			else if("1".equals(orgType))
			{
				String a[] = orgService.getOrgIdsByisOrg("1");
				String b[] = orgService.getOrgIdsByisOrg("2");
				//每月上报数量
				list2 = submitService.getStatisticsBySJMonthSubmit(a,year,b);
				//每月采用数量
				list5 = submitService.getStatisticsBySJMonthUse(a,year,b);
				//当前年上报数量
				list3 = submitService.getStatisticsBySJYearSubmit(a,year,b);
				//当前年采用数量
				list4 = submitService.getStatisticsBySJYearUse(a,year,b);
			}	
			else
			{
				String a[] = orgService.getOrgIdsByisOrg(orgType);
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(a,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(a,year);
				//当前年上报数量
				list3 = submitService.getStatisticsByYearSubmit(a,year);
				//当前年采用数量
				list4 = submitService.getStatisticsByYearUse(a,year);
			}
		}
		Map m = new HashMap<String, String>();
		for(int j=0;j<list2.size();j++){
			m.put(((Object[])list2.get(j))[0]+"#"+((Object[])list2.get(j))[1]+"SUBMIT",((Object[])list2.get(j))[2]);
		}
		
		for(int j=0;j<list5.size();j++){
			m.put(((Object[])list5.get(j))[0]+"#"+((Object[])list5.get(j))[1]+"USE",((Object[])list5.get(j))[2]);
		}
		
		for(int j=0;j<list4.size();j++){
			m.put(((Object[])list4.get(j))[0],((Object[])list4.get(j))[1]);
		}
		
		
		
		
		BaseDataExportInfo export = new BaseDataExportInfo();
		if("0".equals(orgType)){
		export.setWorkbookFileName(URLEncoder.encode(year+"年省政府部门及相关单位信息上报情况", "utf-8"));
	    export.setSheetTitle(year +"年省政府部门及相关单位信息上报情况");
	    export.setSheetName(year +"年省政府部门及相关单位信息上报情况");
		}
		else if("1".equals(orgType)){
			export.setWorkbookFileName(URLEncoder.encode(year+"年全省各设区市信息上报情况", "utf-8"));
		    export.setSheetTitle(year +"年全省各设区市信息上报情况");
		    export.setSheetName(year +"年全省各设区市信息上报情况");
		}
		else if("3".equals(orgType)){
			export.setWorkbookFileName(URLEncoder.encode(year+"年省各驻外办信息上报情况", "utf-8"));
		    export.setSheetTitle(year +"年省各驻外办信息上报情况");
		    export.setSheetName(year +"年省各驻外办信息上报情况");
		}
		List s1 = new ArrayList();
		s1.add("机构名称");
		s1.add("1月");
		s1.add("2月");
		s1.add("3月");
		s1.add("4月");
		s1.add("5月");
		s1.add("6月");
		s1.add("7月");
		s1.add("8月");
		s1.add("9月");
		s1.add("10月");
		s1.add("11月");
		s1.add("12月");
		s1.add("上报总数");
		s1.add("采用总数");
		export.setTableHead(s1);
		List s2 = new ArrayList();
		for(int i=0;i<list3.size();i++){
			Vector cols = new Vector();
			cols.add(0,String.valueOf(((Object[])list3.get(i))[1]));
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")!=null)){
				cols.add(1,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")!=null)){
				cols.add(1,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")==null)){
				cols.add(1,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT"))+"/0");
			}
			else{
				cols.add(1,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")!=null)){
				cols.add(2,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")!=null)){
				cols.add(2,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")==null)){
				cols.add(2,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT"))+"/0");
			}
			else{
				cols.add(2,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")!=null)){
				cols.add(3,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")!=null)){
				cols.add(3,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")==null)){
				cols.add(3,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT"))+"/0");
			}
			else{
				cols.add(3,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")!=null)){
				cols.add(4,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")!=null)){
				cols.add(4,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")==null)){
				cols.add(4,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT"))+"/0");
			}
			else{
				cols.add(4,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")!=null)){
				cols.add(5,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")!=null)){
				cols.add(5,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")==null)){
				cols.add(5,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT"))+"/0");
			}
			else{
				cols.add(5,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")!=null)){
				cols.add(6,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")!=null)){
				cols.add(6,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")==null)){
				cols.add(6,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT"))+"/0");
			}
			else{
				cols.add(6,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")!=null)){
				cols.add(7,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")!=null)){
				cols.add(7,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")==null)){
				cols.add(7,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT"))+"/0");
			}
			else{
				cols.add(7,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SE")!=null)){
				cols.add(8,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")!=null)){
				cols.add(8,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")==null)){
				cols.add(8,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT"))+"/0");
			}
			else{
				cols.add(8,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SE")!=null)){
				cols.add(9,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")!=null)){
				cols.add(9,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")==null)){
				cols.add(9,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT"))+"/0");
			}
			else{
				cols.add(9,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SE")!=null)){
				cols.add(10,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")!=null)){
				cols.add(10,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")==null)){
				cols.add(10,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT"))+"/0");
			}
			else{
				cols.add(10,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SE")!=null)){
				cols.add(11,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")!=null)){
				cols.add(11,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")==null)){
				cols.add(11,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT"))+"/0");
			}
			else{
				cols.add(11,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SE")!=null)){
				cols.add(12,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")!=null)){
				cols.add(12,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")==null)){
				cols.add(12,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT"))+"/0");
			}
			else{
				cols.add(12,"");
			}
			if("Use".equals(flag))
			{
				if(m.get(((Object[])list3.get(i))[0])!=null){
					cols.add(13,String.valueOf(m.get(((Object[])list3.get(i))[0])));
				}else{
					cols.add(13,"");
				}
				if("0".equals(String.valueOf(((Object[])list3.get(i))[2]))){
					cols.add(14,"");
				}else{
				cols.add(14,String.valueOf(((Object[])list3.get(i))[2]));
				}
			}
			else
			{
				if("0".equals(String.valueOf(((Object[])list3.get(i))[2]))){
					cols.add(13,"");
				}else{
				cols.add(13,String.valueOf(((Object[])list3.get(i))[2]));
				}
				if(m.get(((Object[])list3.get(i))[0])!=null){
					cols.add(14,String.valueOf(m.get(((Object[])list3.get(i))[0])));
				}else{
					cols.add(14,"");
				}
			}
			s2.add(cols);
		}
		 export.setRowList(s2);
		 ProcessXSL xsl = new ProcessXSL();
		 xsl.createWorkBookSheet2(export);
		 xsl.writeWorkBook(getResponse());
		return null;
	}
	
	
	/**
	 * 各县级上报统计导出excel
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String excelSubmit1() throws UnsupportedEncodingException{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		//int year2 = Integer.parseInt(year1);
		//years = new int[]{year2-5,year2-4,year2-3,year2-2,year2-1,year2,year2+1,year2+2,year2+3,year2+4,year2+5};
		if(year==null){
			year=year1;
		}
		/*if(orgType.equals("all")){
			String b[] = orgService.getOrgIdsByisOrg("2");
			list2 = submitService.getStatisticcols(b,year);
			list4 = submitService.getStatistics4(b,year);
			list5 = orgService.getOrgIdsByisOrg1("1");
			return "point1";
		}*/
		String orgType = getRequest().getParameter("orgType");
		String flag = getRequest().getParameter("flag");
		if("Use".equals(flag))
		{
			if("0".equals(orgType))
			{
				String b[] = orgService.getOrgIdsByisOrg("2");
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(b,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(b,year);
				//当前年采用数量
				list3 = submitService.getStatisticsByXJYearUseOrder(b,year);
				//当前年上报数量
				list4 = submitService.getStatisticsByYearSubmitOrder(b,year);
				list6 = orgService.getOrgIdsByisOrg1("1");
			}
			else
			{
				String b[] = orgService.getOrgIdsByisOrg("2");
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(b,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(b,year);
				//当前年采用数量
				list3 = submitService.getStatisticsByXJYearUseOrder(b,year);
				//当前年上报数量
				list4 = submitService.getStatisticsByYearSubmitOrder(b,year);
				list6 = orgService.getOrgIdsByisOrg1("1");
			}
		}
		else
		{
			if("0".equals(orgType))
			{
				String b[] = orgService.getOrgIdsByisOrg("2");
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(b,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(b,year);
				//当前年上报数量
				list3 = submitService.getStatisticsByXJYearSubmit(b,year);
				//当前年采用数量
				list4 = submitService.getStatisticsByYearUse(b,year);
				list6 = orgService.getOrgIdsByisOrg1("1");
			}
			else
			{
				String b[] = orgService.getOrgIdsByisOrg("2");
				//每月上报数量
				list2 = submitService.getStatisticsByMonthSubmit(b,year);
				//每月采用数量
				list5 = submitService.getStatisticsByMonthUse(b,year);
				//当前年上报数量
				list3 = submitService.getStatisticsByXJYearSubmit(b,year);
				//当前年采用数量
				list4 = submitService.getStatisticsByYearUse(b,year);
				list6 = orgService.getOrgIdsByisOrg1("1");
			}
		}
		Map mm = new HashMap<String , String>();
		for(int m=0;m<list6.size();m++){
			mm.put(((TUumsBaseOrg)list6.get(m)).getOrgSyscode(), ((TUumsBaseOrg)list6.get(m)).getOrgName());
		}
		Map m = new HashMap<String, String>();
		for(int j=0;j<list2.size();j++){
			m.put(((Object[])list2.get(j))[0]+"#"+((Object[])list2.get(j))[1]+"SUBMIT",((Object[])list2.get(j))[2]);
			System.out.println(((Object[])list2.get(j))[0]+"#"+((Object[])list2.get(j))[1]+"="+((Object[])list2.get(j))[2]);
		}
		
		for(int j=0;j<list5.size();j++){
			m.put(((Object[])list5.get(j))[0]+"#"+((Object[])list5.get(j))[1]+"USE",((Object[])list5.get(j))[2]);
		}
		
		for(int j=0;j<list4.size();j++){
			m.put(((Object[])list4.get(j))[0],((Object[])list4.get(j))[1]);
		}
		
		
		
		
		//构建表头
		BaseDataExportInfo export = new BaseDataExportInfo();
		export.setWorkbookFileName(year+URLEncoder.encode("年1-12月全省各县（市、区）在设区市信息上报情况", "utf-8"));
	    export.setSheetTitle(year +"年1-12月全省各县（市、区）在设区市信息上报情况");
	    export.setSheetName(year +"年1-12月全省各县（市、区）在设区市信息上报情况");
		List s1 = new ArrayList();
		s1.add("机构名称");
		s1.add("");
		s1.add("1月");
		s1.add("2月");
		s1.add("3月");
		s1.add("4月");
		s1.add("5月");
		s1.add("6月");
		s1.add("7月");
		s1.add("8月");
		s1.add("9月");
		s1.add("10月");
		s1.add("11月");
		s1.add("12月");
		s1.add("上报总数");
		s1.add("采用总数");
		export.setTableHead(s1);
		List s2 = new ArrayList();
		for(int i=0;i<list3.size();i++){
			Vector cols = new Vector();
			cols.add(0,String.valueOf(mm.get(((Object[])list3.get(i))[3])));
			cols.add(1,String.valueOf(((Object[])list3.get(i))[1]));
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")!=null)){
				cols.add(2,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")!=null)){
				cols.add(2,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01USE")==null)){
				cols.add(2,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-01SUBMIT"))+"/0");
			}
			else{
				cols.add(2,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")!=null)){
				cols.add(3,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")!=null)){
				cols.add(3,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02USE")==null)){
				cols.add(3,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-02SUBMIT"))+"/0");
			}
			else{
				cols.add(3,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")!=null)){
				cols.add(4,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")!=null)){
				cols.add(4,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03USE")==null)){
				cols.add(4,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-03SUBMIT"))+"/0");
			}
			else{
				cols.add(4,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")!=null)){
				cols.add(5,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")!=null)){
				cols.add(5,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04USE")==null)){
				cols.add(5,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-04SUBMIT"))+"/0");
			}
			else{
				cols.add(5,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")!=null)){
				cols.add(6,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")!=null)){
				cols.add(6,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05USE")==null)){
				cols.add(6,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-05SUBMIT"))+"/0");
			}
			else{
				cols.add(6,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")!=null)){
				cols.add(7,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")!=null)){
				cols.add(7,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06USE")==null)){
				cols.add(7,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-06SUBMIT"))+"/0");
			}
			else{
				cols.add(7,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")!=null)){
				cols.add(8,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")!=null)){
				cols.add(8,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07USE")==null)){
				cols.add(8,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-07SUBMIT"))+"/0");
			}
			else{
				cols.add(8,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SE")!=null)){
				cols.add(9,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")!=null)){
				cols.add(9,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08USE")==null)){
				cols.add(9,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-08SUBMIT"))+"/0");
			}
			else{
				cols.add(9,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SE")!=null)){
				cols.add(10,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")!=null)){
				cols.add(10,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09USE")==null)){
				cols.add(10,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-09SUBMIT"))+"/0");
			}
			else{
				cols.add(10,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SE")!=null)){
				cols.add(11,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")!=null)){
				cols.add(11,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10USE")==null)){
				cols.add(11,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-10SUBMIT"))+"/0");
			}
			else{
				cols.add(11,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SE")!=null)){
				cols.add(12,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")!=null)){
				cols.add(12,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11USE")==null)){
				cols.add(12,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-11SUBMIT"))+"/0");
			}
			else{
				cols.add(12,"");
			}
			if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SE")!=null)){
				cols.add(13,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT"))+"/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")));
			}else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT")==null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")!=null)){
				cols.add(13,"0/"+String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")));
			}
			else if((m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT")!=null)&&(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12USE")==null)){
				cols.add(13,String.valueOf(m.get(((Object[])list3.get(i))[0]+"#"+year+"-12SUBMIT"))+"/0");
			}
			else{
				cols.add(13,"");
			}
			if("Use".equals(flag))
			{
				if(m.get(((Object[])list3.get(i))[0])!=null){
					cols.add(14,String.valueOf(m.get(((Object[])list3.get(i))[0])));
				}else{
					cols.add(14,"");
				}
				if("0".equals(String.valueOf(((Object[])list3.get(i))[2]))){
					cols.add(15,"");
				}else{
				cols.add(15,String.valueOf(((Object[])list3.get(i))[2]));
				}
			}
			else
			{
				if("0".equals(String.valueOf(((Object[])list3.get(i))[2]))){
					cols.add(14,"");
				}else{
				cols.add(14,String.valueOf(((Object[])list3.get(i))[2]));
				}
				if(m.get(((Object[])list3.get(i))[0])!=null){
					cols.add(15,String.valueOf(m.get(((Object[])list3.get(i))[0])));
				}else{
					cols.add(15,"");
				}
			}
			s2.add(cols);
		}
		 export.setRowList(s2);
		 ProcessXSL xsl = new ProcessXSL();
		 xsl.createWorkBookSheet1(export);
		 xsl.writeWorkBook(getResponse());
		return null;
	}
	
	

	/**
	 * 月政务信息统计导出excel
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String excelCode() throws UnsupportedEncodingException{
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
		
		String b[] = null;
		if(orgType!=null){
			b = orgService.getOrgIdsByisOrg(orgType);
		}
		else{
			b = orgService.getDeptOrgIds();
		}
		list2 = submitService.getStatisticsByYQ(b, year, month);
		list3 = submitService.getStatisticsByZW(b, year, month);
		list4 = submitService.getStatisticsByPS(b, year, month);
		list5 = submitService.getStatisticsBySJYQ(b, year, month);
		list6 = submitService.getStatisticsBySJZW(b, year, month);
		list7 = submitService.getStatisticsBySJPS(b, year, month);
		list8 = submitService.getStatisticsByGBXX(b, year, month);
		list9 = submitService.getStatisticsByGBXXPS(b, year, month);
		list10 = submitService.getStatisticsByJF(b, year, month);
		list11 = submitService.getStatisticsByDF(b, year, month);
		list12 = submitService.getStatisticsBySYLJ(b, year, month);
		list13 = submitService.getStatisticsByLJDF(b, year, month);
		Map m = new HashMap<String, String>();
		for(int j=0;j<list2.size();j++){
			m.put(((Object[])list2.get(j))[0]+"#YQ",((Object[])list2.get(j))[1]);
		}
		for(int j=0;j<list3.size();j++){
			m.put(((Object[])list3.get(j))[0]+"#ZW",((Object[])list3.get(j))[1]);
		}
		for(int j=0;j<list4.size();j++){
			m.put(((Object[])list4.get(j))[0]+"#PS",((Object[])list4.get(j))[1]);
		}
		for(int j=0;j<list5.size();j++){
			m.put(((Object[])list5.get(j))[0]+"#SJYQ",((Object[])list5.get(j))[1]);
		}
		for(int j=0;j<list6.size();j++){
			m.put(((Object[])list6.get(j))[0]+"#SJZW",((Object[])list6.get(j))[1]);
		}
		for(int j=0;j<list7.size();j++){
			m.put(((Object[])list7.get(j))[0]+"#SJPS",((Object[])list7.get(j))[1]);
		}
		for(int j=0;j<list8.size();j++){
			m.put(((Object[])list8.get(j))[0]+"#GBXX",((Object[])list8.get(j))[1]);
		}
		for(int j=0;j<list9.size();j++){
			m.put(((Object[])list9.get(j))[0]+"#GBXXPS",((Object[])list9.get(j))[1]);
		}
		for(int j=0;j<list10.size();j++){
			m.put(((Object[])list10.get(j))[0]+"#JF",((Object[])list10.get(j))[1]);
		}
		for(int j=0;j<list11.size();j++){
			m.put(((Object[])list11.get(j))[0]+"#DF",((Object[])list11.get(j))[1]);
		}
		for(int j=0;j<list12.size();j++){
			m.put(((Object[])list12.get(j))[0]+"#SYLJ",((Object[])list12.get(j))[1]);
		}
		
		
		
		
		
		BaseDataExportInfo export = new BaseDataExportInfo();
		export.setWorkbookFileName(URLEncoder.encode(year+"年"+month+"月份政务信息采用、得分和排名情况", "utf-8"));
	    export.setSheetTitle(year+"年"+month+"月份政务信息采用、得分和排名情况");
	    export.setSheetName(year+"年"+month+"月份政务信息采用、得分和排名情况");
		List s1 = new ArrayList();
		s1.add("单位");
		s1.add("市级");
		s1.add("");
		s1.add("");
		s1.add("省级");
		s1.add("");
		s1.add("");
		s1.add("国办");
		s1.add("");
		s1.add("本月加分");
		s1.add("本月得分");
		s1.add("上月累计");
		s1.add("累计得分");
		s1.add("排名情况");
		export.setTableHead(s1);
		Vector cols1 = new Vector();
		cols1.add(0,"");
		cols1.add(1,"每日要情");
		cols1.add(2,"南昌政务");
		cols1.add(3,"领导批示");
		cols1.add(4,"每日要情");
		cols1.add(5,"江西政务");
		cols1.add(6,"领导批示");
		cols1.add(7,"信息");
		cols1.add(8,"领导批示");
		cols1.add(9,"");
		cols1.add(10,"");
		cols1.add(11,"");
		cols1.add(12,"");
		cols1.add(13,"");
		List s2 = new ArrayList();
		s2.add(cols1);
		for(int i=0;i<list13.size();i++){
			Vector cols = new Vector();
			cols.add(0,String.valueOf(((Object[])list13.get(i))[1]));
			
			if(m.get(((Object[])list13.get(i))[0]+"#YQ")!=null){
				cols.add(1,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#YQ")));
			}else{
				cols.add(1,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#ZW")!=null){
				cols.add(2,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#ZW")));
			}else{
				cols.add(2,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#PS")!=null){
				cols.add(3,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#PS")));
			}else{
				cols.add(3,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#SJYQ")!=null){
				cols.add(4,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#SJYQ")));
			}else{
				cols.add(4,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#SJZW")!=null){
				cols.add(5,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#SJZW")));
			}else{
				cols.add(5,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#SJPS")!=null){
				cols.add(6,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#SJPS")));
			}else{
				cols.add(6,"");
			}if(m.get(((Object[])list13.get(i))[0]+"#GBXX")!=null){
				cols.add(7,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#GBXX")));
			}else{
				cols.add(7,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#GBXXPS")!=null){
				cols.add(8,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#GBXXPS")));
			}else{
				cols.add(8,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#JF")!=null){
				cols.add(9,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#JF")));
			}else{
				cols.add(9,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#DF")!=null){
				cols.add(10,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#DF")));
			}else{
				cols.add(10,"");
			}
			if(m.get(((Object[])list13.get(i))[0]+"#SYLJ")!=null){
				cols.add(11,String.valueOf(m.get(((Object[])list13.get(i))[0]+"#SYLJ")));
			}else{
				cols.add(11,"");
			}
			cols.add(12,String.valueOf(((Object[])list13.get(i))[2]));
			cols.add(13,String.valueOf(i+1));
			
			s2.add(cols);
		}
		 export.setRowList(s2);
		 ProcessXSL xsl = new ProcessXSL();
		 xsl.createWorkBookSheet5(export);
		 xsl.writeWorkBook(getResponse());
		return null;
	}
	
	
	/*
	 * 点击分数显示具体分数来源
	 * 
	 */
	public String orgcode() throws Exception{
		String month = getRequest().getParameter("month");
		String year = getRequest().getParameter("year");
		String orgId = getRequest().getParameter("orgId");
		List list1 = infoReportService.getOrgReport7(orgId, year, month);
		List list2 =infoReportService.getOrgReport8(orgId, year, month);
		List list3 =submitService.getStatistics10(orgId, year, month);
		String st1 = list1.toString();
		String st2 = list2.toString();
		String st3 = list3.toString();
		String st4 = "要情得分："+st3+"。呈阅件得分："+st2+"。呈国办得分："+st1;
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(st4);
		return null;
	}
	
	/*
	 * 点击分数显示具体分数来源
	 * 
	 */
	public String orgcode1() throws Exception{
		String month = getRequest().getParameter("month");
		String year = getRequest().getParameter("year");
		String orgId = getRequest().getParameter("orgId");
		List list1 = infoReportService.getOrgReport7s(orgId, year, month);
		List list2 =infoReportService.getOrgReport8s(orgId, year, month);
		List list3 =submitService.getStatistics10s(orgId, year, month);
		String st1 = list1.toString();
		String st2 = list2.toString();
		String st3 = list3.toString();
		String st4 = "要情得分："+st3+"。呈阅件得分："+st2+"。呈国办得分："+st1;
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(st4);
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

	public Page<TUumsBaseOrg> getPage()
	{
		return page;
	}

	public void setPage(Page<TUumsBaseOrg> page)
	{
		this.page = page;
	}

	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public List getList2() {
		return list2;
	}

	public void setList2(List list2) {
		this.list2 = list2;
	}

	public List getList3() {
		return list3;
	}

	public void setList3(List list3) {
		this.list3 = list3;
	}

	public int[] getYears() {
		return years;
	}

	public void setYears(int[] years) {
		this.years = years;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List getList4() {
		return list4;
	}

	public void setList4(List list4) {
		this.list4 = list4;
	}

	public List getList5() {
		return list5;
	}

	public void setList5(List list5) {
		this.list5 = list5;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List getList6() {
		return list6;
	}

	public void setList6(List list6) {
		this.list6 = list6;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List getList7() {
		return list7;
	}

	public void setList7(List list7) {
		this.list7 = list7;
	}

	public List getList8() {
		return list8;
	}

	public void setList8(List list8) {
		this.list8 = list8;
	}

	public List getList9() {
		return list9;
	}

	public void setList9(List list9) {
		this.list9 = list9;
	}

	public List getList10() {
		return list10;
	}

	public void setList10(List list10) {
		this.list10 = list10;
	}

	public List getList11() {
		return list11;
	}

	public void setList11(List list11) {
		this.list11 = list11;
	}

	public List getList12() {
		return list12;
	}

	public void setList12(List list12) {
		this.list12 = list12;
	}

	public List getList13() {
		return list13;
	}

	public void setList13(List list13) {
		this.list13 = list13;
	}
	
	
	
}
