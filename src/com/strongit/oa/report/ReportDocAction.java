package com.strongit.oa.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import oracle.sql.DATE;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;


import com.strongit.oa.bo.TgwReportBean;
import com.strongit.oa.bo.ToaFeedback;
import com.strongit.oa.bo.ToaFeedbackBean;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaReportBean1;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.historyquery.DataSource;
import com.strongit.oa.report.query.IReportService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "reportDefine.action", type = ServletActionRedirectResult.class) })
public class ReportDocAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exportType;
	private String reporttongji;
	private int totalNum;
	private String mark;	//跳转标识
	@Autowired
	private IReportService reportService;
	private int currentPage;	//页码
	private String year;//年度
	private String selectTime;//季度
	private String yearGw;//年度
    private String selectDept;//处室
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	public String mark() throws Exception{
		if("1".equals(mark)){
		    if(selectDept!=null&&!"".equals(selectDept)){//办文情况登记（统计）表
		      String selectDept1=URLDecoder.decode(selectDept,"UTF-8");
		       reportSituation(yearGw,selectDept1);
		    }else{
		        reportSituation(yearGw,selectDept);
		    }			
		}else if("2".equals(mark)){
			reportMouthSituation();		//月度各处室办文情况统计表
		}else if("3".equals(mark)){
			reportMouthTime();			//月度办文（发文）办结时间统计表
		}else if("4".equals(mark)){
			reportOpinionSituation(year,selectTime);	//季度征求意见反馈情况统计表
		}else if("5".equals(mark)){
			reportReturnSituation(year,selectTime);	//季度上报公文退文情况统计表
		}
		return "situation";
	}
	public void getDept() throws Exception {
	    String result;
	    result = reportService.getDeptList();
	    this.renderText(result.toString());
	}
	
	public List<ToaReportBean1> getAllDept() throws Exception {
	    List<ToaReportBean1>  list = new ArrayList<ToaReportBean1>();
	    List<ToaReportBean1>  list1 = reportService.getDeptList2();
	    for(ToaReportBean1 t:list1){
	        String department=t.getDepartment();
	        if(department!=null&&!"".equals(department)){
	            list.add(t);
	        }
	    }
	    for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {   
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {   
                  if  (list.get(j).getDepartment().equals(list.get(i).getDepartment()))  {   
                    list.remove(j);   
                   }    
              }    
             }    

        return  list;
    }
	
	
	public void reportSituation(String yearGw,String selectDept) throws Exception {
		
		try {
//			List<ToaReportBean> list=new ArrayList<ToaReportBean>();
//			if(pages!=null&&pages.size()>0){
//				list = manager.pageToToaReportBean(pages);    //对ToaReportBean  填数据
//			}
		    SimpleDateFormat st = new SimpleDateFormat(
            "yyyy-MM");
            if(yearGw==null||"".equals(yearGw)||"null".equals(yearGw)){
             yearGw = st.format(new Date()); 
             this.setYearGw(yearGw);
           }
//			List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),df.parse(reportEndDate),org.getOrgCode());
			List<TgwReportBean> list = new ArrayList<TgwReportBean>();
			list = reportService.getgW(yearGw,selectDept);
			String reportTitle;
			if(selectDept==null||"".equals(selectDept)||"null".equals(selectDept)){
			  reportTitle=yearGw+"处室办文情况登记(统计)表";
			  this.setSelectDept("");
			}else{
			    reportTitle=yearGw+" "+selectDept+"办文情况登记(统计)表";
			}
			
		
			Map map = new HashMap();
			map.put("reportTitle", reportTitle);
			String sr= ServletActionContext.getServletContext().getRealPath("/reportfiles")+"/";//对传参---子报表的目录
	         sr = sr.replace('\\', '/');
	         String sr1= ServletActionContext.getServletContext().getRealPath("/reportfiles")+"/";//对传参---子报表的目录
             sr1 = sr1.replace('\\', '/');
	        map.put("SUBREPORT_DIR", sr);
	        map.put("SUBREPORT_DIR2", sr1);
			totalNum=reportService.getTotalNum(yearGw, selectDept);//总行数
			String jasper="";
			String jasper1="";
			if(selectDept==null||"".equals(selectDept)){
			 jasper = "/reportfiles/bwqkdjtjb.jasper"; // 你的jasper文件地址/ 办公情况登记（统计）表.jasper
			 jasper1 = "/reportfiles/bwqkdjtjb-other.jasper";
			}else{
			    jasper  = "/reportfiles/bwqkdjtjb-two.jasper";
			    jasper1 = "/reportfiles/bwqkdjtjb-two-other.jasper";
			}
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			//读取jasper   
            JasperReport jasperReport = null;
            File exe_rpt=null;
			if(exportType==null||"".equals(exportType)){
				
				exe_rpt = new File(this.getRequest().getRealPath(jasper));
			}else{
			     exe_rpt = new File(this.getRequest().getRealPath(jasper1)); 
			}
			jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
            jasperPrint=JasperFillManager.fillReport(jasperReport, map,new JRBeanCollectionDataSource(list));
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,reportTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,reportTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlSearch(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public void reportMouthSituation() throws Exception {
		
		try {
//			List<ToaReportBean> list=new ArrayList<ToaReportBean>();
//			if(pages!=null&&pages.size()>0){
//				list = manager.pageToToaReportBean(pages);    //对ToaReportBean  填数据
//			}
			
			//List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),df.parse(reportEndDate),org.getOrgCode());
			List<ToaReportBean> list = new ArrayList<ToaReportBean>();
			
			for(int i=0;i<20;i++){
				ToaReportBean t = new ToaReportBean();
				t.setText1("ttt");
				list.add(t);
			}
			
			String reportTitle="月度各处室办文情况统计表";
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd");
			Map map = new HashMap();
			map.put("reportTitle", reportTitle);
			map.put("reportTime", sdf.format(d));
			totalNum=list.size();//总行数
			String jasper = "/reportfiles/ydgcsbgqktjb.jasper"; // 你的jasper文件地址 /月度各处室办文情况统计表.jasper
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,reportTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,reportTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlSearch(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public void reportMouthTime() throws Exception {
	
	try {
//		List<ToaReportBean> list=new ArrayList<ToaReportBean>();
//		if(pages!=null&&pages.size()>0){
//			list = manager.pageToToaReportBean(pages);    //对ToaReportBean  填数据
//		}
		
//		List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),df.parse(reportEndDate),org.getOrgCode());
		List<ToaReportBean> list = new ArrayList<ToaReportBean>();
		
		for(int i=0;i<20;i++){
			ToaReportBean t = new ToaReportBean();
			t.setText1("ttt");
			list.add(t);
		}
		
		String reportTitle="月度办文（发文）办结时间统计表";
		Map map = new HashMap();
		map.put("reportTitle", reportTitle);
		totalNum=list.size();//总行数
		String jasper = "/reportfiles/ydbwfwbjsjtjb.jasper"; // 你的jasper文件地址 /月度办文（发文）办结时间统计表.jasper
		
		HttpSession session = this.getSession();
		HttpServletRequest request=this.getRequest();
		HttpServletResponse response=this.getResponse();
		JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
		if(exportType==null||"".equals(exportType)){
			//读取jasper   
			JasperReport jasperReport = null;
			File exe_rpt = new File(this.getRequest().getRealPath(jasper));
			jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
			jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
			
		}
		session.setAttribute("mysessionkey", jasperPrint);
		if(exportType!=null&&"excel".equals(exportType)){
			this.printToExcel(jasperPrint, request, response,reportTitle);
		}else if(exportType!=null&&"pdf".equals(exportType)){
			this.printToPDF(jasperPrint, request, response,reportTitle);
		}else if(exportType!=null&&"print".equals(exportType)){
			this.printToPrinter(jasperPrint, request, response);
		}else{
			this.generateHtmlSearch(jasperPrint,request,response);
		}
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JRException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void reportOpinionSituation(String year,String selectTime) throws Exception {
	
	try {
//		List<ToaReportBean> list=new ArrayList<ToaReportBean>();
//		if(pages!=null&&pages.size()>0){
//			list = manager.pageToToaReportBean(pages);    //对ToaReportBean  填数据
//		}
		
//		List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),df.parse(reportEndDate),org.getOrgCode());
	    List<ToaFeedbackBean> list = new ArrayList<ToaFeedbackBean>();
        
        list = reportService.getFeedbackData(year,selectTime);
		String reportTitle=year+"年第"+selectTime+"季度"+"全市政府办公系统征求意见反馈情况统计表";
		Map map = new HashMap();
		map.put("reportTitle", reportTitle);
		if(list.size()>0){
		 totalNum=list.size();//总行数
		}
		String jasper = "/reportfiles/jdzqyjfkqktjb.jasper"; // 你的jasper文件地址 /季度征求意见反馈情况统计表.jasper
		String jasper1 = "/reportfiles/jdzqyjfkqktjb-other.jasper";//使另存为不分页
		HttpSession session = this.getSession();
		HttpServletRequest request=this.getRequest();
		HttpServletResponse response=this.getResponse();
		JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
		if(exportType==null||"".equals(exportType)){
			//读取jasper   
			JasperReport jasperReport = null;
			File exe_rpt = new File(this.getRequest().getRealPath(jasper));
			jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
			jasperPrint=JasperFillManager.fillReport(jasperReport, map,new JRBeanCollectionDataSource(list));
			
		}else{
          //读取jasper   
            JasperReport jasperReport = null;
            File exe_rpt = new File(this.getRequest().getRealPath(jasper1));
            jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
            jasperPrint=JasperFillManager.fillReport(jasperReport, map,new JRBeanCollectionDataSource(list));
        }
		session.setAttribute("mysessionkey", jasperPrint);
		if(exportType!=null&&"excel".equals(exportType)){
			this.printToExcel(jasperPrint, request, response,reportTitle);
		}else if(exportType!=null&&"pdf".equals(exportType)){
			this.printToPDF(jasperPrint, request, response,reportTitle);
		}else if(exportType!=null&&"print".equals(exportType)){
			this.printToPrinter(jasperPrint, request, response);
		}else{
			this.generateHtmlSearch(jasperPrint,request,response);
		}
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JRException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public String reportReturnSituation(String year,String selectTime) throws Exception {
	
	try {
//		List<ToaReportBean> list=new ArrayList<ToaReportBean>();
//		if(pages!=null&&pages.size()>0){
//			list = manager.pageToToaReportBean(pages);    //对ToaReportBean  填数据
//		}
		
//		List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),df.parse(reportEndDate),org.getOrgCode());
	    
		List<ToaReportBean1> list = new ArrayList<ToaReportBean1>();
		
		 list = reportService.getReturnData(year,selectTime);
		String re="年第";
		String re1="季度";
		String re2="全市政府办公系统上报公文退文情况统计表";
		String reportTitle=year+re+selectTime+re1+re2;
		Map map = new HashMap();
		map.put("reportTitle", reportTitle);
		String sr= ServletActionContext.getServletContext().getRealPath("/reportfiles")+"/";//对传参---子报表的目录
		 sr = sr.replace('\\', '/');

		map.put("SUBREPORT_DIR", sr);
		//totalNum= reportService.getReturnTotal(year,selectTime);//总行数
		if(list.size()>0){
		totalNum = list.size();}
		String jasper = "/reportfiles/jdsbgwtwqktjb.jasper"; // 你的jasper文件地址 /季度上报公文退文情况统计表.jasper
		String jasper1 = "/reportfiles/jdsbgwtwqktjb-other.jasper";//使另存为不分页
		HttpSession session = this.getSession();
		HttpServletRequest request=this.getRequest();
		HttpServletResponse response=this.getResponse();
		JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
		if(exportType==null||"".equals(exportType)){
			//读取jasper   
			JasperReport jasperReport = null;
			File exe_rpt = new File(this.getRequest().getRealPath(jasper));
			jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
			jasperPrint=JasperFillManager.fillReport(jasperReport, map,new JRBeanCollectionDataSource(list));
			
		}else{
		  //读取jasper   
            JasperReport jasperReport = null;
            File exe_rpt = new File(this.getRequest().getRealPath(jasper1));
            jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
            jasperPrint=JasperFillManager.fillReport(jasperReport, map,new JRBeanCollectionDataSource(list));
		}
		session.setAttribute("mysessionkey", jasperPrint);
		if(exportType!=null&&"excel".equals(exportType)){
			this.printToExcel(jasperPrint, request, response,reportTitle);
		}else if(exportType!=null&&"pdf".equals(exportType)){
			this.printToPDF(jasperPrint, request, response,reportTitle);
		}else if(exportType!=null&&"print".equals(exportType)){
			this.printToPrinter(jasperPrint, request, response);
		}else{
			this.generateHtmlSearch(jasperPrint,request,response);
		   // return "returnReprot";
		    
		}
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JRException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
	public void generateHtmlSearch(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  //.append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html style=\"height:100%;\">")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
			  .append("window.name=\"targetWindow\";\n")
				.append("function onsub(){\n")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body>")
			    .append(" <div id=\"contentborder\" align=\"center\" >");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' scrolling='auto' action='"+contextPath+"/report/reportDoc!mark.action'>\n")
			     .append("<input type='hidden' id='docModel.docTitle' name='docModel.docTitle'  value='"+111+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='mark' name='mark' value='"+mark+"'/>\n")
			     .append("<input type='hidden' id='year1' name='year' value='"+year+"'/>\n")
			     .append("<input type='hidden' id='startTime' name='selectTime' value='"+selectTime+"'/>\n")
			     .append("<input type='hidden' id='reporttongji' name='reporttongji' value='1'/>\n")
			    .append("<input type='hidden' id='yearGw' name='yearGw' value='"+yearGw+"'/>\n")
			     .append("<input type='hidden' id='selectDept' name='selectDept' value='"+selectDept+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center scrolling='auto'>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0' scrolling='auto'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0' >\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center' style=\"overflow:hidden;\">\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("</div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}
	public void printToExcel(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,String struct) throws Exception {
    	ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
		exporter.exportReport();
		byte[] bytes = oStream.toByteArray();
		if (bytes != null && bytes.length > 0) {
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename="+ java.net.URLEncoder.encode(struct+".xls","utf-8"));
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
			outputStream.close();
		}
		oStream.close();

    }
	public void printToPDF(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,String struct){
    	ServletOutputStream ouputStream;
		try{
			ouputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(struct+".pdf", "utf-8"));
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	public void printToPrinter(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response)throws Exception{
		try{
			ServletOutputStream ouputStream = response.getOutputStream();	
		//	JasperPrintManager.printReport(jasperPrint, true);//直接打印,不用预览PDF直接打印  true为弹出打印机选择.false为直接打印
			response.setContentType("application/octet-stream");
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);//将JasperPrint对象写入对象输出流中 
			oos.flush();
			oos.close();  
			ouputStream.flush();
			ouputStream.close();	
		}catch (Exception e) {
			logger.error(e);
			throw e;
		}
    }

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getReporttongji() {
		return reporttongji;
	}

	public void setReporttongji(String reporttongji) {
		this.reporttongji = reporttongji;
	}

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

   

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

    public String getYearGw() {
        return yearGw;
    }

    public void setYearGw(String yearGw) {
        this.yearGw = yearGw;
    }

    public String getSelectDept() {
        return selectDept;
    }

    public void setSelectDept(String selectDept) {
        this.selectDept = selectDept;
    }
}
