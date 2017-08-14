/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: zhangli
 * Version: V1.0
 * Description： 借阅档案管理ACTION
 */
package com.strongit.oa.archive.archiveborrow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.archive.archivefile.ArchiveFileManager;
import com.strongit.oa.archive.tempfile.TempFileType;
import com.strongit.oa.attendance.report.AttendDataSource;
import com.strongit.oa.bo.ToaArchiveBorrow;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.historyquery.DataSource;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "archiveBorrow.action", type = ServletActionRedirectResult.class) })
public class ArchiveBorrowAction extends BaseActionSupport {
	/** 分页对象*/
	private Page<ToaArchiveBorrow> page = new Page<ToaArchiveBorrow>(FlexTableTag.MAX_ROWS, true);

	/** 档案借阅编号*/
	private String borrowId;

	/** 档案文件编号*/
	private String fileId;

	/** 档案案卷编号*/
	private String folderId;

	/** 档案借阅记录列表*/
	private List borrowlist;

	/** 档案借阅记录对象*/
	private ToaArchiveBorrow model = new ToaArchiveBorrow();

	/** 档案借阅manager*/
	private ArchiveBorrowManager manager;

	/** 档案文件manager*/
	private ArchiveFileManager filemanager;
	
	/** 文件创建时间*/
	private String createDateFile;

	/** 待审*/
	private static final String NO = "0";

	/**已审*/
	private static final String YES = "1";

	/**驳回*/
	private static final String BACK = "3";
	
	/**全部*/
	private static final String ALL = "4";

	/** 档案借阅状态字典*/
	private HashMap<String, String> statemap = new HashMap<String, String>();
	
	/** 所属机构名称*/
	private String fileDepartmentName;
	
	/** 要查询的文件附件**/
	private ToaArchiveFileAppend append;
	
	private String borrowAuditing;
	private String appendNames;//附件名称

	/** 报表格式*/
	private String exportType;
	/** 报表统计时间*/
	private String reportDate;
	/** 当前页*/
	private int currentPage;
	/** 总页数*/
	private int totalNum;
	
	private String eformContent;		//附件名

	/** 流程ID*/
	private String workflow ;
	
	private String  tfileAppedId;		//附件ID
	
	private String instanceId; // 该归档文件的流程实例ID
	
	private String appendFormId;		//电子表单ID
	
	private ToaArchiveFile filemodel=new ToaArchiveFile();		//档案室文件
	
	private  Iterator<ToaArchiveFileAppend> appendlist;
	public Iterator<ToaArchiveFileAppend> getAppendlist() {
		return appendlist;
	}

	public void setAppendlist(Iterator<ToaArchiveFileAppend> appendlist) {
		this.appendlist = appendlist;
	}

	public String getBorrowAuditing() {
		return borrowAuditing;
	}

	public void setBorrowAuditing(String borrowAuditing) {
		this.borrowAuditing = borrowAuditing;
	}

	public String getFileDepartmentName() {
		return fileDepartmentName;
	}

	public void setFileDepartmentName(String fileDepartmentName) {
		this.fileDepartmentName = fileDepartmentName;
	}

	/**
	 * @roseuid 4958CFDE0099
	 */
	public ArchiveBorrowAction() {
		statemap.put(NO, "待审");
		statemap.put(YES, "已审");
		statemap.put(BACK, "驳回");
	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Access method for the borrowId property.
	 * 
	 * @return the current value of the borrowId property
	 */
	public java.lang.String getBorrowId() {
		return borrowId;
	}

	/**
	 * Sets the value of the borrowId property.
	 * 
	 * @param aBorrowId
	 *            the new value of the borrowId property
	 */
	public void setBorrowId(java.lang.String aBorrowId) {
		borrowId = aBorrowId;
	}

	/**
	 * Access method for the fileId property.
	 * 
	 * @return the current value of the fileId property
	 */
	public java.lang.String getFileId() {
		return fileId;
	}

	/**
	 * Sets the value of the fileId property.
	 * 
	 * @param aFileId
	 *            the new value of the fileId property
	 */
	public void setFileId(java.lang.String aFileId) {
		fileId = aFileId;
	}

	/**
	 * Access method for the borrowlist property.
	 * 
	 * @return the current value of the borrowlist property
	 */
	public java.util.List getBorrowlist() {
		return borrowlist;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaArchiveBorrow getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(ArchiveBorrowManager aManager) {
		manager = aManager;
	}

	/**
	 * 初始化输入框
	 * 
	 * @return java.lang.String
	 * @roseuid 4958C9980101
	 */
	@Override
	public String input() throws Exception{
		getRequest().setAttribute("backlocation","javascript:history.back();"); 
		prepareModel();

		return INPUT;
	}

	/**
	 * 显示借阅申请信息
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception{
		prepareModel();
		
		return "show";
	}
	/**
	 * 审核借阅请求
	 * 
	 * @return java.lang.String
	 * @roseuid 4958CCF200CA
	 */
	public String audit() throws Exception{
		getRequest().setAttribute("backlocation","javascript:cancel();"); 
		addActionMessage(manager.auditBorrow(borrowId, model
				.getBorrowAuditing(), model.getBorrowAuditingDesc(),new OALogInfo("审核借阅申请")));
		return "temp";
	}

	/**
	 * author:zhangli 
	 * description:查看借阅文件 
	 * modifyer: 
	 * description:
	 * 
	 * @return
	 */
	public String viewFile() throws Exception{
		getRequest().setAttribute("backlocation","javascript:history.back();"); 
		prepareModel();
		StringBuffer appstr=new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(model.getToaArchiveFile().getFileDate()!=null){//文件创建日期是否为空
			createDateFile=format.format(model.getToaArchiveFile().getFileDate());
		}
		Date stime = model.getBorrowFromtime();/** 借阅起始时间*/
		Date etime = model.getBorrowEndtime();/** 借阅截止时间*/
		Date nowtime = new Date();
		String msg="";
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);//时间向前推一天
		/** 审核记录为已审且在借阅期内，则进入查看页面并修改查看状态为已查看*/
		if (YES.equals(model.getBorrowAuditing())&&nowtime.after(stime)&&(c.getTime().before(etime))) {
			model.setBorrowViewState(YES);
			manager.saveBorrow(model,new OALogInfo("修改查看状态"));
			String orgId=model.getToaArchiveFile().getFileDepartment();
			   if(orgId!=null&&!"".equals(orgId)){
				   fileDepartmentName=manager.getOrgNameById(orgId);
			   }
			   String type=model.getToaArchiveFile().getFileDocType();
			   appendlist=model.getToaArchiveFile().getToaArchiveFileAppends().iterator();
			   if(type!=null&&!type.equals("")&&!TempFileType.TEMPFILE.equals(type)&&!TempFileType.MEETING.equals(type)){		//查看收发文件
				   filemodel=model.getToaArchiveFile();
				   if(appendlist.hasNext()){
					   while(appendlist.hasNext()){
						   append=appendlist.next();
						   if(append.getAppendContent()!=null&&!append.getAppendContent().equals("")){
							   byte[] bufData = append.getAppendContent();
							   eformContent = new String(bufData,"utf-8");;	//电子表单信息
							   
						   }else {
							   eformContent="附件内容字段为空";
						   }
						   workflow=filemodel.getWorkflow();				//流程ID
						   instanceId = workflow.split(";")[0];
						   tfileAppedId=append.getAppendId();			//附件ID
						   appendFormId=filemodel.getFileFormId();		//电子表单ID
					   }
				   }
				   return "vieweform";
			   }else {				
				   if(appendlist.hasNext()){
					   while(appendlist.hasNext()){
						   append=appendlist.next();
						   
						   appstr.append("<a  id=\"fujian\" href=\"#\" onclick=\"view('"+append.getAppendId()+"');\"  style='cursor: hand;'><font color=\"blue\">"+append.getAppendName()+"</font></a>")
						   .append("<a href=\"#\" onclick=\"viewAnnex('"+append.getAppendId()+"');\"  style='cursor: hand;'>下载</a><br>");
					   }
				   }
				   appendNames=appstr.toString();
				   getRequest().setAttribute("toaarchivefile",
						   model.getToaArchiveFile());
				   return "viewArchivefile";
			   }
		} else {
			if(NO.equals(model.getBorrowAuditing()))
				//addActionMessage("该申请还未审批或未通过，不能查看！");
			msg="该申请还未审批或未通过，不能查看！";
			else if(BACK.equals(model.getBorrowAuditing()))
				msg="该申请审核未通过，不可以查看！";
			else if(nowtime.before(stime))
				//addActionMessage("该文件还未到可查看时间！");
			    msg="该文件还未到可查看时间！";
			else
				//addActionMessage("该文件已经超过借阅截止时间了！");
			    msg="该文件已经超过借阅截止时间了！";
			
			StringBuffer returnhtml = new StringBuffer(
					"<script> var scriptroot = '")
					.append(getRequest().getContextPath())
					.append("'</script>")
					.append("<SCRIPT src='")
					.append(getRequest().getContextPath())
					.append(
							"/common/js/commontab/workservice.js'>")
					.append("</SCRIPT>")
					.append("<SCRIPT src='")
					.append(getRequest().getContextPath())
					.append(
							"/common/js/commontab/service.js'>")
					.append("</SCRIPT>")
					.append("<script>")
					.append("alert('")
					.append(msg)
					.append("');window.location='")
					.append(getRequest().getContextPath())
					.append(
							"/archive/archiveborrow/archiveBorrow.action';</script>");
			return renderHtml(returnhtml.toString());
		}
	}

	/**
	 * 查看借阅信息列表
	 * 
	 * @return java.lang.String
	 * @roseuid 4958CFDE00C8
	 */
	@Override
	public String list() throws Exception{
		getRequest().setAttribute("backlocation","javascript:history.back();"); 
		User userinfo = manager.getCurrentUser();
		if (userinfo != null)
			model.setBorrowPersonid(userinfo.getUserId());
		if (model.getBorrowAuditing() == null)
			model.setBorrowAuditing(ALL);
		page = manager.getAllBorrow(page, model);
		return SUCCESS;
	}

	/**
	 * author:zhangli 
	 * description:初始化审核借阅请求 
	 * modifyer: 
	 * description:
	 * 
	 * @return
	 */
	public String auditing() throws Exception{
		getRequest().setAttribute("backlocation","javascript:cancel();"); 
		if (model.getBorrowAuditing() == null)
			model.setBorrowAuditing(NO);
		page = manager.getAllBorrow(page, model);
		return "auditing";
	}

	public String state() throws Exception{
		getRequest().setAttribute("backlocation","javascript:history.back();"); 
		prepareModel();
		return "state";
	}
	/**
	 * 保存借阅申请信息
	 * 
	 * @return java.lang.String
	 * @roseuid 4958CFDE00E8
	 */
	@Override
	public String save() throws Exception{
		getRequest().setAttribute("backlocation","javascript:history.back();"); 
				//getRequest().getContextPath()+"/archive/archiveborrow/archiveBorrow.action");
		if ("".equals(model.getBorrowId()))
			model.setBorrowId(null);
		ToaArchiveFile file = filemanager.getFile(fileId);
		model.setToaArchiveFile(file);
		addActionMessage(manager.saveBorrow(model,new OALogInfo("保存借阅申请")));
		return "reloads";
	}

	/**
	 * 删除借阅申请信息
	 * 
	 * @return java.lang.String
	 * @roseuid 4958CFDE0107
	 */
	@Override
	public String delete() throws Exception{
		getRequest().setAttribute("backlocation","javascript:history.back();"); 
		addActionMessage(manager.delBorrow(borrowId,new OALogInfo("删除借阅申请记录")));
		return "reloads";
	}

	/**
	 * 初始化借阅信息对象
	 * 
	 * @roseuid 4958CFDE0126
	 */
	@Override
	protected void prepareModel() throws Exception{
		if (borrowId != null) {
			model = manager.getBorrow(borrowId);
		} else {
			model = new ToaArchiveBorrow();
			model.setBorrowFromtime(new Date());
			model.setBorrowTime(model.getBorrowFromtime());
			User user = manager.getCurrentUser();
			model.setBorrowPersonid(user.getUserId());
			model.setBorrowPersonname(user.getUserName());
			ToaArchiveFile file = filemanager.getFile(fileId);
			model.setToaArchiveFile(file);
		}
		if(model.getBorrowAuditing()!=null)
			borrowAuditing=statemap.get(model.getBorrowAuditing());
	}
	/**
	 * 统计文件解决登记情况
	 * @return
	 */
	public String getBorrowReport(){
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM");
			List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate));
			String borrow=manager.getDateToString(df.parse(reportDate), "yyyy 年 MM 月");//报表参数
			String borrowTitle="档案材料调阅借出登记表";
			Map map = new HashMap();
			map.put("borrowdate", borrow);
			totalNum=list.size();//总行数
			String jasper = "/WEB-INF/jsp/archive/archiveborrow/report/fileborrow.jasper"; // 你的jasper文件地址 
			
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
	/**
	 * 
	 * Description:生成HTML报表
	 * param: 
	 * @author 	    胡丽丽
	 * @date 	    Jan 28, 2010 4:25:18 PM
	 */
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
				.append("function onsub(exportType,date){\n")
				.append("$('#exportType').val(exportType);\n")
				.append("$('#reportDate').val(date);\n")
				.append("document.forms[0].action='"+contextPath+"/archive/archiveborrow/archiveBorrow!getBorrowReport.action';")
			    .append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("<style type=\"text/css\">")
				.append("#contentborder { BORDER-RIGHT: #DBDBDB  1px solid; PADDING-RIGHT: 1px; PADDING-LEFT: 1px; BACKGROUND: white; PADDING-BOTTOM: 10px; OVERFLOW: auto; BORDER-LEFT: #DBDBDB 1px solid; WIDTH: 100%; BORDER-BOTTOM: #DBDBDB 1px solid; POSITION: absolute; HEIGHT: 85%; margin-left: 1px; }")
				.append("</style>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='get'  target='targetWindow' action='"+contextPath+"/archive/archiveborrow/archiveBorrow!getBorrowReport.action'>\n")
			     .append("<input type='hidden' id='exportType' name='exportType'  value='"+exportType+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='reportDate' name='reportDate' value='"+reportDate+"'/>\n")
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
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
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
	/**
	 * 打印报表
	 * @author 胡丽丽
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @throws Exception
	 */
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

	/**
	 * 导出excel
	 * @author 胡丽丽
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @param struct
	 * @throws Exception
	 */
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
    
   /**
    * 
    * Description:导出PDF
    * param: 
    * @author 胡丽丽
    * @date  Apr 13, 2010 9:42:44 AM
    */
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
	public HashMap<String, String> getStatemap() {
		return statemap;
	}

	@Autowired
	public void setFilemanager(ArchiveFileManager filemanager) {
		this.filemanager = filemanager;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public ToaArchiveFileAppend getAppend() {
		return append;
	}

	public void setAppend(ToaArchiveFileAppend append) {
		this.append = append;
	}

	public String getCreateDateFile() {
		return createDateFile;
	}

	public void setCreateDateFile(String createDateFile) {
		this.createDateFile = createDateFile;
	}

	public String getAppendNames() {
		return appendNames;
	}

	public void setAppendNames(String appendNames) {
		this.appendNames = appendNames;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}



	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getAppendFormId() {
		return appendFormId;
	}

	public void setAppendFormId(String appendFormId) {
		this.appendFormId = appendFormId;
	}

	public String getTfileAppedId() {
		return tfileAppedId;
	}

	public void setTfileAppedId(String tfileAppedId) {
		this.tfileAppedId = tfileAppedId;
	}

	public ToaArchiveFile getFilemodel() {
		return filemodel;
	}

	public void setFilemodel(ToaArchiveFile filemodel) {
		this.filemodel = filemodel;
	}

	public String getEformContent() {
		return eformContent;
	}

	public void setEformContent(String eformContent) {
		this.eformContent = eformContent;
	}
	
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
