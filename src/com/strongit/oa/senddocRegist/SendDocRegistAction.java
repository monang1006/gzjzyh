package com.strongit.oa.senddocRegist;

import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.util.TempPo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongit.oa.bo.ToaSendDocRegist;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class SendDocRegistAction extends BaseActionSupport<ToaSendDocRegist>{
	
	private SendDocRegistManager sendDocRegistManager;
	private Page<ToaSendDocRegist> page = new Page<ToaSendDocRegist>(FlexTableTag.MAX_ROWS, true);
	private ToaSendDocRegist model = new ToaSendDocRegist();
	private AddressOrgManager addressOrgManager;
	/** 对应模块类型*/
	private String moduletype = null;
	/** 机构列表*/
	private List orgList;
	private String actionType;
	
	/** 报表格式*/
	private String exportType;
	/** 报表统计时间*/
	private String reportDate;
	
	private String reportEndDate;
	/** 当前页*/
	private int currentPage;
	/** 总页数*/
	private int totalNum;
	
	/** 搜索条件*/
	private String type; //搜索标识
	private String searchDocTitle;
	private String searchSend;
	private String searchRoom;
	private String searchDocCode;
	private String searchSecret;
	private Date searchStaTime;
	private Date searchEndTime;
	private Date searchSendStaTime;
	private Date searchSendEndTime;

	@Override
	public String delete() throws Exception {
		HttpServletRequest request = getRequest();
		String idss = request.getParameter("id");
		String[] ids = idss.split(",");
		for(String id : ids){
			sendDocRegistManager.deleteRegistDoc(id);
		}
		return list();
	}

	public Page<ToaSendDocRegist> getPage() {
		return page;
	}

	@Override
	public String input() throws Exception {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		if(id!=null && !"".equals(id)){
			model = sendDocRegistManager.getRegistDocById(id);
		}
		orgList = sendDocRegistManager.getOrgList(moduletype);
		orgList.remove(0);
		showDictOrgTreeWithCheckbox("QF");
		showDictOrgTreeWithCheckbox("FWDJH");
		showDictOrgTreeWithCheckbox("MMDJ");
		return "input";
	}
	
	/**
	 * 得到带复选框的树机构(大集中项目移植)
	 * @author:邓志城
	 * @date:2009-12-21 上午10:52:00
	 * @return
	 * @throws Exception
	 */
	public List<TempPo> showDictOrgTreeWithCheckbox(String param) throws Exception  {
		List<TempPo> list = addressOrgManager.getOrgListFromDict(param);
		list.remove(0);
		getRequest().setAttribute(param, list);
		return list;
	}

	@Override
	public String list() throws Exception {
		if("search".equals(type)){
			String fromLeaderDesktopPage = getRequest().getParameter("fromLeaderDesktopPage");
			if(fromLeaderDesktopPage!=null && "1".equals(fromLeaderDesktopPage)){//判断页面是否来自领导首页(desktopWhole-bgtDesktopForLeader.jsp):0、否 1、是
				Date now = new Date();										     //页面来自领导首页则查询上周数据
				now.setTime(now.getTime()-7*24*60*60*1000);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
					now.setTime(now.getTime()-24*60*60*1000);
					calendar.setTime(now);
				}
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				Date date = calendar.getTime();
				date.setHours(0);
				date.setMinutes(0);
				date.setSeconds(0);
				searchStaTime = date;
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
				date = calendar.getTime();
				date.setHours(23);
				date.setMinutes(59);
				date.setSeconds(59);
				searchEndTime = date;
				String noDocCode = getRequest().getParameter("noDocCode"); //发文登记是否已编号    0：已编号， 1：未编号
				if(noDocCode != null && "1".equals(noDocCode)){
					searchDocCode = "null";
				}
				if(noDocCode != null && "0".equals(noDocCode)){
					searchDocCode = "notNull";
				}
			}
			page = sendDocRegistManager.getRegistDocsBySearch(page, searchDocTitle, searchSend, searchRoom, searchDocCode, searchSecret, searchStaTime, searchEndTime, searchSendStaTime, searchSendEndTime);
			searchDocCode = null;
		}else{
			page = sendDocRegistManager.getRegistDocs(page);
		}				
		showDictOrgTreeWithCheckbox("MMDJ");
		return "list";
	}
	
	//以excel形式导出发文登记列表
//	public void exportExcel() throws Exception{
//		sendDocRegistManager.exportExcel(sendDocRegistId, getResponse());
//	}
	
	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {
		if("".equals(model.getRegistId())){
			model.setRegistId(null);
		}
		sendDocRegistManager.saveRegistDoc(model);
		if("2".equals(actionType)){
			String docCode = model.getDocCode();
			model = new ToaSendDocRegist();
			model.setDocCode(docCode);
			orgList = sendDocRegistManager.getOrgList(moduletype);
			orgList.remove(0);
			showDictOrgTreeWithCheckbox("QF");
			showDictOrgTreeWithCheckbox("FWDJH");
			showDictOrgTreeWithCheckbox("MMDJ");
			return "input";
		}else{
			renderHtml("<script>returnValue='reload';window.close();</script>");
		}		
		return null;
	}

	public ToaSendDocRegist getModel() {
		return model;
	}
	
	/**
	 * 统计发文登记查询
	 * @return
	 */
	public String getBorrowReport(){
		try {			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		//	Date rdate=df.parse(reportEndDate);
			Date  rdate=new Date(df.parse(reportEndDate).getTime()+24*3600*1000   );   //加一天 
			
			List<ToaReportBean> list=sendDocRegistManager.getBorrowReport(df.parse(reportDate),rdate);
			String borrow=sendDocRegistManager.getDateToString(df.parse(reportDate), "yyyy-MM-dd");//报表参数
			String endDate=sendDocRegistManager.getDateToString(df.parse(reportEndDate), "yyyy-MM-dd");//报表参数
			
			String borrowTitle="发文登记查询表";
			Map map = new HashMap();
			map.put("borrowdate", borrow);
			map.put("endDate", endDate);
		
			totalNum=list.size();//总行数
			String jasper = "/reportfiles/sendDoc.jasper"; // 你的jasper文件地址 
			
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
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtml(jasperPrint,request,response);
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
	
	public void generateHtml(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
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
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
				.append("function onsub(exportType,date,endDate){\n")
				.append("$('#exportType').val(exportType);\n")
				.append("$('#reportDate').val(date);\n")
				.append("$('#reportEndDate').val(endDate);\n")
				.append("document.forms[0].action='"+contextPath+"/senddocRegist/sendDocRegist!getBorrowReport.action';")
			    .append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='get'  target='targetWindow' action='"+contextPath+"/senddocRegist/sendDocRegist!getBorrowReport.action'>\n")
			     .append("<input type='hidden' id='exportType' name='exportType'  value='"+exportType+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='reportDate' name='reportDate' value='"+reportDate+"'/>\n")
			       .append("<input type='hidden' id='reportEndDate' name='reportEndDate' value='"+reportEndDate+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
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
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
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

	   public void printToExcel(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,String struct) throws Exception {
	    	ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,Boolean.TRUE);
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

	/**
	 * 获取机构树
	 * 
	 * @return java.lang.String
	 * @roseuid 494F58830109
	 */
	public String orgtree() throws Exception{
		orgList = sendDocRegistManager.getOrgList(moduletype);
		return "orgtree";
	}
	
	public String getModuletype() {
		return moduletype;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}
	
	public List getOrgList() {
		return orgList;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(String reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	public SendDocRegistManager getSendDocRegistManager() {
		return sendDocRegistManager;
	}

	@Autowired
	public void setSendDocRegistManager(SendDocRegistManager sendDocRegistManager) {
		this.sendDocRegistManager = sendDocRegistManager;
	}

	public String getSearchDocTitle() {
		return searchDocTitle;
	}

	public void setSearchDocTitle(String searchDocTitle) {
		try {
			this.searchDocTitle = URLDecoder.decode(searchDocTitle, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getSearchSend() {
		return searchSend;
	}

	public void setSearchSend(String searchSend) {
		try {
			this.searchSend = URLDecoder.decode(searchSend, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getSearchRoom() {
		return searchRoom;
	}

	public void setSearchRoom(String searchRoom) {
		try {
			this.searchRoom = URLDecoder.decode(searchRoom, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getSearchDocCode() {
		return searchDocCode;
	}

	public void setSearchDocCode(String searchDocCode) {
		try {
			this.searchDocCode = URLDecoder.decode(searchDocCode, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getSearchSecret() {
		return searchSecret;
	}

	public void setSearchSecret(String searchSecret) {
		try {
			this.searchSecret = URLDecoder.decode(searchSecret, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Date getSearchStaTime() {
		return searchStaTime;
	}

	public void setSearchStaTime(Date searchStaTime) {
		this.searchStaTime = searchStaTime;
	}

	public Date getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(Date searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getSearchSendStaTime() {
		return searchSendStaTime;
	}

	public void setSearchSendStaTime(Date searchSendStaTime) {
		this.searchSendStaTime = searchSendStaTime;
	}

	public Date getSearchSendEndTime() {
		return searchSendEndTime;
	}

	public void setSearchSendEndTime(Date searchSendEndTime) {
		this.searchSendEndTime = searchSendEndTime;
	}
	
	@Autowired
	public void setManager(AddressOrgManager manager) {
		this.addressOrgManager = manager;
	}
}
