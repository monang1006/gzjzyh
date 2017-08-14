package com.strongit.oa.workinspect.workquery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 任务工单报表
 * @company      Strongit Ltd. (C) copyright
 * @author qibh
 * @version      1.0.0.0
 * @comment    任务工单报表Action
 */
@ParentPackage("default")
public class WorkQueryAction extends BaseActionSupport<TOsWorktask> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private WorkQueryManager workQueryManager;
	/** 报表格式 */
	private String exportType;
	
	/** 当前页 */
	private int currentPage;
	/** 总页数 */
	private int totalNum;

	/** 搜索条件 */
	private String reportType;
	private String worktaskTitle;//任务标题
	private String worktaskUnitName;//任务发送机构
	private String worktaskNo;//任务编号
	private String managetState;//任务状态
	private String worktaskUser;//任务发起人
	private String worktaskEmerlevel;//任务缓急
	private String worktaskEtime;//任务结束时间
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String org;//机构名

	
	private List<TUumsBaseOrg> orgList;//机构列表，用于选择机构进行查询
	
	@Autowired
	private IUserService userService;
	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	@Override
	public String delete() throws Exception {
		return list();
	}

	@Override
	public String input() throws Exception {
		return "input";
	}

	/**
	 * 用于我的办理件和发送件查询
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-4 上午05:57:30
	 * @version :5.0
	 */
	@Override
	public String list() throws Exception {
		orgList = userService.getAllOrgInfo();
		this.reportType = this.getRequest().getParameter("reportType");
		return SUCCESS;
	}

	/**
	 * 用于任务办理情况统计
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-4 上午05:57:30
	 * @version :5.0
	 */
	public String tasklist() throws Exception {
		orgList = userService.getAllOrgInfo();
		this.reportType = this.getRequest().getParameter("reportType");
		return "task";
	}
	
	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {

		return null;
	}

	/**
	 * 查询报表数据
	 * @author: qibh
	 *@return
	 * @created: 2013-6-4 上午05:57:53
	 * @version :5.0
	 */
	public String getBorrowReport() {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<ToaReportBean> list = new ArrayList<ToaReportBean>();
			String jasper = "";
			String borrowTitle = "";
			if (reportType != null && reportType.equals("0")) {
				list = workQueryManager.getHandle(worktaskTitle,
						worktaskUnitName, worktaskNo, managetState,
						worktaskUser, worktaskEmerlevel, "".equals(worktaskEtime )|| worktaskEtime == null?null:df.parse(worktaskEtime),
						df.parse(startTime), df.parse(endTime));
				jasper = "/reportfiles/Handle_record.jasper"; // 我的办理件查询
				borrowTitle = "我的办理件查询";
			}
			if (reportType.equals("1")) {
				list = workQueryManager.getSend(worktaskTitle,
						worktaskUnitName, worktaskNo, managetState,
						worktaskUser, worktaskEmerlevel, "".equals(worktaskEtime )|| worktaskEtime == null?null:df.parse(worktaskEtime),
						df.parse(startTime), df.parse(endTime));
				int siexe=list.size();
				jasper = "/reportfiles/Send_record.jasper"; // 我的发送件查询
				borrowTitle = "我的发送件查询";
			}
			Map map = new HashMap();
			totalNum = list.size();// 总行数
			HttpSession session = this.getSession();
			HttpServletRequest request = this.getRequest();
			HttpServletResponse response = this.getResponse();
			JasperPrint jasperPrint = (JasperPrint) session
					.getAttribute("mysessionkey");
			if (exportType == null || "".equals(exportType)) {
				// 读取jasper
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				// System.out.println(exe_rpt.getPath());
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt
						.getPath());
				// System.out.println(jasperReport);
				jasperPrint = JasperFillManager.fillReport(jasperReport, map,
						new DataSource(list));
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if (exportType != null && "excel".equals(exportType)) {
				 this.exportmyfils(list, reportType);
			} else if (exportType != null && "pdf".equals(exportType)) {
				 this.printToPDF(jasperPrint, request, response, borrowTitle);
			} else if (exportType != null && "print".equals(exportType)) {
				 this.printToPrinter(jasperPrint, request, response);
			} else {
				this.generateHtml(jasperPrint, request, response);
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
	 * 用于办理件情况统计
	 * 
	 * @author niwy
	 * @date 2013年5月8日11:44:48
	 * @return
	 */
	public String getTaskHadle() {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<ToaReportBean> list = new ArrayList<ToaReportBean>();
			String jasper = "";
			String borrowTitle = "";
			if (reportType != null && reportType.equals("2")) {
				list = workQueryManager.getTaskHandle(org);
				jasper = "/reportfiles/Task_record.jasper"; // 办理件情况查询
				borrowTitle = "办理件情况查询";
			}
			Map map = new HashMap();
			totalNum = list.size();// 总行数
			HttpSession session = this.getSession();
			HttpServletRequest request = this.getRequest();
			HttpServletResponse response = this.getResponse();
			JasperPrint jasperPrint = (JasperPrint) session
					.getAttribute("mysessionkey");
			if (exportType == null || "".equals(exportType)) {
				// 读取jasper
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt
						.getPath());
				jasperPrint = JasperFillManager.fillReport(jasperReport, map,
						new DataSource(list));
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if (exportType != null && "excel".equals(exportType)) {
				 this.exportmyfils(list, reportType);
			} else if (exportType != null && "pdf".equals(exportType)) {
				 this.printToPDF(jasperPrint, request, response, borrowTitle);
			} else if (exportType != null && "print".equals(exportType)) {
				 this.printToPrinter(jasperPrint, request, response);
			} else {
				this.generateHtml1(jasperPrint, request, response);
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
	 * 导出报表
	 * @author  niwy
	 * @date  2013年7月24日17:20:58
	 */
	private void exportmyfils(List<ToaReportBean> orglist, String rtype) {
		try {
			HttpServletResponse response = getResponse();
			if (rtype.equals("0")) {
				// 创建EXCEL对象
				BaseDataExportInfo export = new BaseDataExportInfo();
				StringBuilder str = new StringBuilder();
				str=str.append(toUtf8String("我的办理件查询"));
				export.setWorkbookFileName(str.toString());
				StringBuilder str2=new StringBuilder();
				str2=str2.append("我的办理件查询");
				export.setSheetTitle(str2.toString());
				export.setSheetName("我的办理件查询");
				export.setColWidth(6600);
				// 描述行信息
				List<String> tableHead = new ArrayList<String>();
				tableHead.add("序号");
				tableHead.add("名称");
				tableHead.add("编号");
				tableHead.add("发送人");
				tableHead.add("工作状态");

				export.setTableHead(tableHead);

				// 获取导出信息
				List rowList = new ArrayList();
				Map rowhigh = new HashMap();
				int rownum = 0;
				for (int i = 0; i < orglist.size(); i++) {
					Vector cols = new Vector();
					ToaReportBean org = orglist.get(i);
					cols.add(String.valueOf(i + 1));// 序号
					cols.add(org.getText7());//
					cols.add(org.getText12());
					cols.add(org.getText11());
					cols.add(org.getText10());
					rowList.add(cols);
					rownum++;
				}
				export.setRowList(rowList);
				export.setRowHigh(rowhigh);
				ProcessXSL xsl = new ProcessXSL();
				xsl.createWorkBookSheet(export);
				xsl.writeWorkBook(response);
			} else if (rtype.equals("1")) {
				// 创建EXCEL对象
				BaseDataExportInfo export = new BaseDataExportInfo();
				StringBuilder str = new StringBuilder();
				str=str.append(startTime+toUtf8String("至")+endTime).append(toUtf8String("我的发送件情况统计"));
				export.setWorkbookFileName(str.toString());
				StringBuilder str2=new StringBuilder();
				str2=str2.append(startTime+"至"+endTime).append("我的发送件情况统计");
				export.setSheetTitle(str2.toString());
				export.setSheetName("我的发送件情况统计");
				export.setColWidth(5900);
				// 描述行信息
				List<String> tableHead = new ArrayList<String>();
				tableHead.add("序号");
				tableHead.add("名称");
				tableHead.add("编号");
				tableHead.add("承办者");
				tableHead.add("工作状态");

				export.setTableHead(tableHead);

				// 获取导出信息
				List rowList = new ArrayList();
				Map rowhigh = new HashMap();
				int rownum = 0;
				for (int i = 0; i < orglist.size(); i++) {
					Vector cols = new Vector();
					ToaReportBean org = orglist.get(i);
					cols.add(String.valueOf(i + 1));// 序号
					cols.add(org.getText7());//
					cols.add(org.getText12());
					cols.add(org.getText11());
					cols.add(org.getText10());
					rowList.add(cols);
					rownum++;
				}
				export.setRowList(rowList);
				export.setRowHigh(rowhigh);
				ProcessXSL xsl = new ProcessXSL();
				xsl.createWorkBookSheet(export);
				xsl.writeWorkBook(response);
			} else if (rtype.equals("2")) {
				// 创建EXCEL对象
				BaseDataExportInfo export = new BaseDataExportInfo();
				StringBuilder str = new StringBuilder();
				str=str.append(toUtf8String("任务办理情况统计"));
				export.setWorkbookFileName(str.toString());
				StringBuilder str2=new StringBuilder();
				str2=str2.append("任务办理情况统计");
				export.setSheetTitle(str2.toString());
				export.setSheetName("任务办理情况统计");
				export.setColWidth(5900);
				// 描述行信息
				List<String> tableHead = new ArrayList<String>();
				tableHead.add("序号");
				tableHead.add("部门");
				tableHead.add("人员");
				tableHead.add("办理任务总数");
				tableHead.add("未完成任务总数");
				tableHead.add("按时完成任务总数");
				tableHead.add("超期完成任务总数");

				export.setTableHead(tableHead);

				// 获取导出信息
				List rowList = new ArrayList();
				Map rowhigh = new HashMap();
				int rownum = 0;
				for (int i = 0; i < orglist.size(); i++) {
					Vector cols = new Vector();
					ToaReportBean org = orglist.get(i);
					cols.add(String.valueOf(i + 1));// 序号
					cols.add(org.getText2());//
					cols.add(org.getText3());
					cols.add(org.getText4());
					cols.add(org.getText5());
					cols.add(org.getText6());
					cols.add(org.getText7());
					rowList.add(cols);
					rownum++;
				}
				export.setRowList(rowList);
				export.setRowHigh(rowhigh);
				ProcessXSL xsl = new ProcessXSL();
				xsl.createWorkBookSheet(export);
				xsl.writeWorkBook(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取报表
	 * @author: qibh
	 *@param jasperPrint
	 *@param request
	 *@param response
	 *@throws Exception
	 * @created: 2013-6-4 上午05:58:20
	 * @version :5.0
	 */
	public void generateHtml(JasperPrint jasperPrint,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pages = 0;// 总页数
		StringBuffer htmlString = new StringBuffer();
		StringBuffer sbuffer = new StringBuffer();
		String contextPath = request.getContextPath();
		if (jasperPrint.getPages().size() > 0) {// 报表是否有数据
			Object jaspersize = jasperPrint.getPages().get(
					jasperPrint.getPages().size() - 1);// 获取最后一页对象
			int laftsize = ((JRBasePrintPage) jaspersize).getElements().size();// 获取最后一页数据量
			if (laftsize > 0) {
				pages = jasperPrint.getPages().size();
			} else {
				pages = jasperPrint.getPages().size() - 1;
			}
		}
		StringBuffer url = new StringBuffer(contextPath
				+ "/workinspect/workquery/workQuery!getBorrowReport.action");
		try {
			PrintWriter out = response.getWriter();
			JRHtmlExporter exporter = new JRHtmlExporter();
			int pageIndex = currentPage;
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			int lastPageIndex = 0;
			if (pages != 0) {
				lastPageIndex = pages - 1;
			}
			if (pageIndex > lastPageIndex) {
				pageIndex = lastPageIndex;
			}
			if (pages <= 0) {
				sbuffer.append("报表内容为空！");
				sbuffer.append("<script>if(window.parent){$('#reporttongji',window.parent.document).val(0);}</script>");
			} else {
				exporter.setParameter(
						JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
						Boolean.FALSE);
				exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,
						new HashMap());
				exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,
						sbuffer);
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
						"utf-8");
				exporter.setParameter(JRExporterParameter.PAGE_INDEX,
						new Integer(pageIndex));
				exporter.exportReport();
			}
			htmlString
					.append(
							"<%@ page contentType=\"text/html; charset=UTF-8\" %>")
					.append("<html>")
					.append(
							"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
					.append("<head>")
					.append("<style type=\"text/css\">")
					.append("table{ border-collapse:collapse;}")
					.append("</style>")
					.append(
							"<script src='"
									+ contextPath
									+ "/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
					.append("<script language='javascript'>\n")
					.append(
							"function onsub(exportType,reportType,worktaskTitle,worktaskUnitName,worktaskNo,managetState,worktaskUser,worktaskEmerlevel,worktaskEtime,startTime,endTime){\n")
					.append("$('#reportType').val(reportType);\n")
					.append("$('#exportType').val(exportType);\n")
					.append("$('#worktaskTitle').val(worktaskTitle);\n")
					.append("$('#worktaskUnitName').val(worktaskUnitName);\n")
					.append("$('#worktaskNo').val(worktaskNo);\n")
					.append("$('#managetState').val(managetState);\n")
					.append("$('#worktaskUser').val(worktaskUser);\n")
					.append("$('#worktaskEmerlevel').val(worktaskEmerlevel);\n")
					.append("$('#worktaskEtime').val(worktaskEtime);\n")
					.append("$('#startTime').val(startTime);\n")
					.append("$('#endTime').val(endTime);\n")
					.append("$('#myTableForm').submit();")
					.append(" }\n")
					.append("function goPages(page){\n")
					.append("window.parent.goPages(page);")
					.append(" }\n")
					.append("</script>")
					.append("</head>")
					.append(
							"<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
					.append(" <div id=\"contentborder\" align=\"center\">");
			htmlString
					.append(
							"<form id='myTableForm'  method='post'  target=\"targetWindow\" action='"
									+ contextPath
									+ "/workinspect/workquery/workQuery!getBorrowReport.action';")
					.append(
							"<input type='hidden' id='exportType' name='exportType'  value='"
									+ exportType + "'/>\n").append(
							"<input type='hidden' id='reportType' name='reportType' value='"
									+ reportType + "'/>\n").append(
							"<input type='hidden' id='totalNum' name='totalNum' value='"
									+ totalNum + "'/>\n").append(
							"<input type='hidden' id='worktaskTitle' name='worktaskTitle' value='"
									+ worktaskTitle + "'/>\n").append(
							"<input type='hidden' id='worktaskUnitName' name='worktaskUnitName' value='"
									+ worktaskUnitName + "'/>\n").append(
							"<input type='hidden' id='worktaskNo' name='worktaskNo' value='"
									+ worktaskNo + "'/>\n").append(
							"<input type='hidden' id='managetState' name='managetState' value='"
									+ managetState + "'/>\n").append(
							"<input type='hidden' id='worktaskUser' name='worktaskUser' value='"
									+ worktaskUser + "'/>\n").append(
							"<input type='hidden' id='worktaskEmerlevel' name='worktaskEmerlevel' value='"
									+ worktaskEmerlevel + "'/>\n").append(
							"<input type='hidden' id='worktaskEtime' name='worktaskEtime' value='"
									+ worktaskEtime + "'/>\n").append(
							"<input type='hidden' id='startTime' name='startTime' value='"
									+ startTime + "'/>\n").append(
							"<input type='hidden' id='endTime' name='endTime' value='"
									+ endTime + "'/>\n").append(
							"<input type='hidden' id='currentPage' name='currentPage' value='"
									+ currentPage + "' />\n");
			htmlString
					.append(
							" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
					.append("<tr>\n")
					.append("<td align='left' width=33%>\n")
					.append(
							"<table width='100%' cellpadding='0' cellspacing='0' border='0' style=\"BORDER-COLLAPSE:collapse;\"><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
					.append("<tr>\n");

			if (pageIndex > 0) {
				htmlString
						.append(" <td><a href='")
						.append("javascript:goPages(0)")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/first.GIF' border='0'></a></td>\n")
						.append(" <td><a href='")
						.append("javascript:goPages(" + (pageIndex - 1) + ")")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/previous.GIF' border='0'></a></td>\n");
			} else {
				htmlString
						.append(
								" <td><img src='"
										+ contextPath
										+ "/oa/image/query/first_grey.GIF' border='0'></td>\n")
						.append(
								"<td><img src='"
										+ contextPath
										+ "/oa/image/query/previous_grey.GIF' border='0'></td>\n");
			}
			if (pageIndex < lastPageIndex) {
				htmlString
						.append(" <td><a href='")
						.append("javascript:goPages(" + (pageIndex + 1) + ")")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/next.GIF' border='0'></a></td>\n")
						.append(" <td><a href='")
						.append("javascript:goPages(" + lastPageIndex + ")")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/last.GIF' border='0'></a></td>\n");
			} else {
				htmlString
						.append(
								" <td><img src='"
										+ contextPath
										+ "/oa/image/query/next_grey.GIF' border='0'></td>\n")
						.append(
								" <td><img src='"
										+ contextPath
										+ "/oa/image/query/last_grey.GIF' border='0'></td>\n");
			}

			htmlString.append(
					" <td width='100%'>&nbsp;&nbsp;" + (pageIndex + 1) + "/"
							+ pages + "&nbsp;&nbsp;</td>\n").append(" </tr>\n")
					.append(" </table></td>\n").append("</tr></table>").append(
							"</td>\n").append("</tr>\n").append("<tr>\n")
					.append(" <td align='center'>\n").append(sbuffer).append(
							" </td>\n").append("</tr>\n").append("</table>\n")
					.append("</form>").append("<div></body></html>");
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
	 * 获取报表图形，用于办理件情况统计
	 * @author: qibh
	 *@param jasperPrint
	 *@param request
	 *@param response
	 *@throws Exception
	 * @created: 2013-6-4 上午06:01:58
	 * @version :5.0
	 */
	public void generateHtml1(JasperPrint jasperPrint,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pages = 0;// 总页数
		StringBuffer htmlString = new StringBuffer();
		StringBuffer sbuffer = new StringBuffer();
		String contextPath = request.getContextPath();
		if (jasperPrint.getPages().size() > 0) {// 报表是否有数据
			Object jaspersize = jasperPrint.getPages().get(
					jasperPrint.getPages().size() - 1);// 获取最后一页对象
			int laftsize = ((JRBasePrintPage) jaspersize).getElements().size();// 获取最后一页数据量
			if (laftsize > 0) {
				pages = jasperPrint.getPages().size();
			} else {
				pages = jasperPrint.getPages().size() - 1;
			}
		}
		StringBuffer url = new StringBuffer(contextPath
				+ "/workinspect/workquery/workQuery!getTaskHadle.action");
		try {
			PrintWriter out = response.getWriter();
			JRHtmlExporter exporter = new JRHtmlExporter();
			int pageIndex = currentPage;
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			int lastPageIndex = 0;
			if (pages != 0) {
				lastPageIndex = pages - 1;
			}
			if (pageIndex > lastPageIndex) {
				pageIndex = lastPageIndex;
			}
			if (pages <= 0) {
				sbuffer.append("报表内容为空！");
				sbuffer.append("<script>if(window.parent){$('#reporttongji',window.parent.document).val(0);}</script>");
			} else {
				exporter.setParameter(
						JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
						Boolean.FALSE);
				exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,
						new HashMap());
				exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,
						sbuffer);
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
						"utf-8");
				exporter.setParameter(JRExporterParameter.PAGE_INDEX,
						new Integer(pageIndex));
				exporter.exportReport();
			}
			htmlString
					.append(
							"<%@ page contentType=\"text/html; charset=UTF-8\" %>")
					.append("<html>")
					.append(
							"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
					.append("<head>")
					.append("<style type=\"text/css\">")
					.append("table{ border-collapse:collapse;}")
					.append("</style>")
					.append(
							"<script src='"
									+ contextPath
									+ "/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
					.append("<script language='javascript'>\n")
					.append(
							"function onsub(exportType,reportType,org){\n")
					.append("$('#reportType').val(reportType);\n")
					.append("$('#exportType').val(exportType);\n")
					.append("$('#org').val(org);\n")
					.append("$('#myTableForm').submit();")
					.append(" }\n")
					.append("function goPages(page){\n")
					.append("window.parent.goPages(page);")
					.append(" }\n")
					.append("</script>")
					.append("</head>")
					.append(
							"<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
					.append(" <div id=\"contentborder\" align=\"center\">");
			htmlString
					.append(
							"<form id='myTableForm'  method='post'  target=\"targetWindow\" action='"
									+ contextPath
									+ "/workinspect/workquery/workQuery!getTaskHadle.action';")
					.append(
							"<input type='hidden' id='exportType' name='exportType'  value='"
									+ exportType + "'/>\n").append(
							"<input type='hidden' id='reportType' name='reportType' value='"
									+ reportType + "'/>\n").append(
							"<input type='hidden' id='totalNum' name='totalNum' value='"
									+ totalNum + "'/>\n").append(
							"<input type='hidden' id='org' name='org' value='"
									+ org + "'/>\n").append(
							"<input type='hidden' id='currentPage' name='currentPage' value='"
									+ currentPage + "' />\n");
			htmlString
					.append(
							" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
					.append("<tr>\n")
					.append("<td align='left' width=33%>\n")
					.append(
							"<table width='100%' cellpadding='0' cellspacing='0' border='0' style=\"BORDER-COLLAPSE:collapse;\"><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
					.append("<tr>\n");

			if (pageIndex > 0) {
				htmlString
						.append(" <td><a href='")
						.append("javascript:goPages(0)")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/first.GIF' border='0'></a></td>\n")
						.append(" <td><a href='")
						.append("javascript:goPages(" + (pageIndex - 1) + ")")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/previous.GIF' border='0'></a></td>\n");
			} else {
				htmlString
						.append(
								" <td><img src='"
										+ contextPath
										+ "/oa/image/query/first_grey.GIF' border='0'></td>\n")
						.append(
								"<td><img src='"
										+ contextPath
										+ "/oa/image/query/previous_grey.GIF' border='0'></td>\n");
			}
			if (pageIndex < lastPageIndex) {
				htmlString
						.append(" <td><a href='")
						.append("javascript:goPages(" + (pageIndex + 1) + ")")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/next.GIF' border='0'></a></td>\n")
						.append(" <td><a href='")
						.append("javascript:goPages(" + lastPageIndex + ")")
						.append(
								"'><img src='"
										+ contextPath
										+ "/oa/image/query/last.GIF' border='0'></a></td>\n");
			} else {
				htmlString
						.append(
								" <td><img src='"
										+ contextPath
										+ "/oa/image/query/next_grey.GIF' border='0'></td>\n")
						.append(
								" <td><img src='"
										+ contextPath
										+ "/oa/image/query/last_grey.GIF' border='0'></td>\n");
			}

			htmlString.append(
					" <td width='100%'>&nbsp;&nbsp;" + (pageIndex + 1) + "/"
							+ pages + "&nbsp;&nbsp;</td>\n").append(" </tr>\n")
					.append(" </table></td>\n").append("</tr></table>").append(
							"</td>\n").append("</tr>\n").append("<tr>\n")
					.append(" <td align='center'>\n").append(sbuffer).append(
							" </td>\n").append("</tr>\n").append("</table>\n")
					.append("</form>").append("<div></body></html>");
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
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-4 上午05:57:30
	 * @version :5.0
	 */
	public void printToPrinter(JasperPrint jasperPrint,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			// JasperPrintManager.printReport(jasperPrint,
			// true);//直接打印,不用预览PDF直接打印 true为弹出打印机选择.false为直接打印
			response.setContentType("application/octet-stream");
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);// 将JasperPrint对象写入对象输出流中
			oos.flush();
			oos.close();
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	/**
	 * 打印成EXCLE
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-4 上午05:57:30
	 * @version :5.0
	 */
	public void printToExcel(JasperPrint jasperPrint,
			HttpServletRequest request, HttpServletResponse response,
			String struct) throws Exception {
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
				Boolean.TRUE);
		exporter.exportReport();
		byte[] bytes = oStream.toByteArray();
		if (bytes != null && bytes.length > 0) {
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(struct + ".xls", "utf-8"));
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
			outputStream.close();
		}
		oStream.close();
	}

	/**
	 * 打印成PDF
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-4 上午05:57:30
	 * @version :5.0
	 */
	public void printToPDF(JasperPrint jasperPrint, HttpServletRequest request,
			HttpServletResponse response, String struct) {
		ServletOutputStream ouputStream;
		try {
			ouputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(struct + ".pdf", "utf-8"));
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public TOsWorktask getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWorktaskTitle() {
		return worktaskTitle;
	}

	public void setWorktaskTitle(String worktaskTitle) {
		this.worktaskTitle = worktaskTitle;
	}

	public String getWorktaskUnitName() {
		return worktaskUnitName;
	}

	public void setWorktaskUnitName(String worktaskUnitName) {
		this.worktaskUnitName = worktaskUnitName;
	}

	public String getWorktaskNo() {
		return worktaskNo;
	}

	public void setWorktaskNo(String worktaskNo) {
		this.worktaskNo = worktaskNo;
	}

	public String getManagetState() {
		return managetState;
	}

	public void setManagetState(String managetState) {
		this.managetState = managetState;
	}

	public String getWorktaskUser() {
		return worktaskUser;
	}

	public void setWorktaskUser(String worktaskUser) {
		this.worktaskUser = worktaskUser;
	}

	public String getWorktaskEmerlevel() {
		return worktaskEmerlevel;
	}

	public void setWorktaskEmerlevel(String worktaskEmerlevel) {
		this.worktaskEmerlevel = worktaskEmerlevel;
	}


	/**
	 * 把字符串转成utf8编码，保证中文文件名不会乱码
	 * 
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public String getWorktaskEtime() {
		return worktaskEtime;
	}

	public void setWorktaskEtime(String worktaskEtime) {
		this.worktaskEtime = worktaskEtime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
}
