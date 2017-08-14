package com.strongit.oa.report;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ui.rememberme.AbstractRememberMeServices;

import com.strongit.oa.bo.ToaReportDefinition;
import com.strongit.oa.bo.ToaReportShowitem;
import com.strongit.oa.bo.ToaReportSort;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.report.query.IReportService;
import com.strongit.oa.work.util.WorkFlowBean;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "reportDefine.action", type = ServletActionRedirectResult.class) })
public class ReportDefineAction extends BaseActionSupport {

	private ReportDefineManager manager;

	private WorkflowReportSortManager reportSortManager; // 报表类型manager

	private IWorkflowService workflowService; // 工作流

	private ReportShowitemManager showitemManager; // 显示项manager

	private Page<ToaReportDefinition> page = new Page<ToaReportDefinition>(FlexTableTag.MAX_ROWS,
			true);

	private Page pageReport = new Page(FlexTableTag.MAX_ROWS); // added by dengzc

	private String definitionId;

	private Map<String, String> sortNameMap = new HashMap<String, String>();

	private String sortId; // 报表类型ID

	private String sortName; // 报表类型名

	private String definitionName; // 报表名称

	private String editState; // 判断是新建状态，还是修改状态

	private String definitionFormid; // 表单ID

	private String showitemId; // 报表显示项id

	List<WorkFlowBean> workflowList;

	private List<ToaReportShowitem> showItemList;

	private String showitemData;

	private String workflowCode; // 流程编号 added by dengzc

	private String workflowTitle; // 流程标题 added by dengzc

	private String exportType; // 报表输出形式

	private ToaReportDefinition model = new ToaReportDefinition();

	@Autowired
	IReportService reportService; // 注入报表接口 added by dengzc

	@Autowired
	IEFormService eformService; // 注入电子表单服务 added by dengzc

	@Autowired
	IUserService userService;// 统一用户服务

	private Integer columnWidth = 110; // 报表每列宽度

	public static String JASPERREPORT_PAGESIZE = "jasperreport_pagesize";

	public static String JASPERREPORT_COLUMNWIDTH = "jasperreport_columnwidth";

	public ReportDefineAction() {

	}

	/**
	 * 报表查询框架页面
	 * 
	 * @author:邓志城
	 * @date:2010-12-24 上午10:16:22
	 * @return
	 */
	public String query() {
		return "query";
	}

	/**
	 * 转到报表查询界面
	 * 
	 * @author:邓志城
	 * @date:2010-12-24 上午10:15:49
	 * @return
	 */
	public String queryPage() {
		if (definitionId == null || "".equals(definitionId)
				|| "null".equals(definitionId)) {
			logger.error("报表定义id参数无效！");
			throw new SystemException("报表定义id参数无效！");
		}
		ToaReportDefinition reportDefinition = manager
				.getDefinitionById(definitionId);
		definitionName = reportDefinition.getDefinitionName();
		getRequest().setAttribute("definitionName", definitionName);
		return "operatepage";
	}

	/**
	 * 显示报表数据
	 * 
	 * @author:邓志城
	 * @date:2010-12-24 上午10:16:08
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String workflow() {
		if (definitionId == null || "".equals(definitionId)
				|| "null".equals(definitionId)) {
			logger.error("报表定义id参数无效！");
			throw new SystemException("报表定义id参数无效！");
		}
		if (exportType == null || "".equals(exportType)) {
			exportType = "html";
		}
		// 非HTML是，需要查询所有数据
		if (!"html".equals(exportType)) {
			pageReport.setAutoCount(true);
		}
		// 从cookie中读取每页设置页数和每列宽度
		Cookie[] cookies = getRequest().getCookies();
		if ((cookies != null) && (cookies.length != 0)) {
			for (int i = 0; i < cookies.length; i++) {
				if (JASPERREPORT_PAGESIZE.equals(cookies[i].getName())) {
					String value = cookies[i].getValue();
					try {
						int pageSize = Integer.parseInt(value);
						pageReport.setPageSize(pageSize);
					} catch (Exception e) {
						logger.error("每页设置页数不是整数。");
					}
					logger.info("从cookie中读取每页设置页数为：" + value);
				} else if (JASPERREPORT_COLUMNWIDTH
						.equals(cookies[i].getName())) {
					String value = cookies[i].getValue();
					try {
						int pageSize = Integer.parseInt(value);
						this.columnWidth = pageSize;
					} catch (Exception e) {
						logger.error("列宽不是整数。");
					}
					logger.info("从cookie中读取列宽为：" + value);
				}
			}
		}

		try {
			ToaReportDefinition reportDefinition = manager
					.getDefinitionById(definitionId);
			long l1 = System.currentTimeMillis();
			Map parameters = new HashMap();
			parameters.put("ReportTitle", reportDefinition.getDefinitionName());
			parameters.put("PAGE_NUMBER", String
					.valueOf(pageReport.getPageNo()));
			System.out.println(parameters);
			List showItems = showitemManager
					.getListByDefinitionId(definitionId);
			String tableName = eformService.getTable(reportDefinition
					.getDefinitionFormid());
			// 查询工作流业务数据
			String definitionId = reportDefinition.getDefinitionWorkflowid();
			List<String> items = new LinkedList<String>();
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			items.add("processMainFormBusinessId");// 业务数据
			paramsMap.put("processDefinitionId", definitionId);
			List<Object[]> ret = workflowService
					.getProcessInstanceByConditionForList(items, paramsMap,
							null);
			items.clear();
			String pkName = null;// 主键名称
			StringBuilder pkFieldValue = new StringBuilder();
			if (ret != null) {
				for (Object obj : ret) {
					if (obj != null) {
						String businessId = obj.toString();
						String[] ids = businessId.split(";");
						String table = ids[0];
						if (pkName == null && tableName.equals(table)) {
							pkName = ids[1];
						}
						pkFieldValue.append("'").append(ids[2]).append("'")
								.append(",");
					}
				}
			}
			if (pkFieldValue.length() > 0) {
				pkFieldValue.deleteCharAt(pkFieldValue.length() - 1);
			}
			logger.info(pkFieldValue);
			// End----------------
			Map<String, Integer> columnMap = reportService
					.getColumnField(tableName);
			List columnList = new LinkedList();
			List<String> fieldList = new LinkedList<String>();// 保存所有要显示的字段名
			List<String> fieldOrderBy = new LinkedList<String>();// 排序字段
			for (Iterator it = showItems.iterator(); it.hasNext();) {
				Map map = new HashMap();
				ToaReportShowitem reportShowItem = (ToaReportShowitem) it
						.next();
				Integer columnType = columnMap.get(reportShowItem
						.getShowitemName());
				// 去掉BLOB类型字段
				if (columnType != null && Types.BLOB != columnType) {
					map.put("COLUMNTEXT", reportShowItem.getShowitemText());
					map.put("COLUMNVALUE", reportShowItem.getShowitemName());
					columnList.add(map);
					fieldList.add(reportShowItem.getShowitemName());
					String orderby = reportShowItem.getShowitemOrderby();
					if ("1".equals(orderby)) {
						orderby = "asc";
						fieldOrderBy.add(new StringBuilder(reportShowItem
								.getShowitemName()).append(" ").append(orderby)
								.append(" ").toString());
					}
					if ("-1".equals(reportShowItem.getShowitemOrderby())) {
						orderby = "desc";
						fieldOrderBy.add(new StringBuilder(reportShowItem
								.getShowitemName()).append(" ").append(orderby)
								.append(" ").toString());
					}
				} else {
					logger.info("找到BLOB字段：" + reportShowItem.getShowitemName()
							+ "[" + reportShowItem.getShowitemText() + "]");
				}
			}
			String strColumn = StringUtils.join(fieldList, ',');
			StringBuilder sql = new StringBuilder("select ").append(strColumn)
					.append(" from ").append(tableName);
			sql.append(" where 1=1 ");

			// 加入主键信息过滤
			if (pkFieldValue.length() > 0) {
				sql.append(" and ").append(pkName).append(" in(").append(
						pkFieldValue).append(") ");
			}
			// ----End----
			if (workflowCode != null && !"".equals(workflowCode)) {
				sql.append(" and ").append(BaseWorkflowManager.WORKFLOW_CODE)
						.append(" like '%").append(workflowCode).append("%'");
			}
			if (workflowTitle != null && !"".equals(workflowTitle)) {
				sql.append(" and ").append(BaseWorkflowManager.WORKFLOW_TITLE)
						.append(" like '%").append(workflowTitle).append("%'");
			}
			if (!fieldOrderBy.isEmpty()) {
				sql.append(" order by ").append(
						StringUtils.join(fieldOrderBy, ','));
			}
			logger.info(sql.toString());
			JasperPrint jasperPrint = reportService.generateJasperPrint(
					parameters, columnList, sql.toString(), pageReport,
					definitionId, columnWidth);
			if ("html".equals(exportType)) {
				String html = reportService.exportHtml(jasperPrint, pageReport
						.getPageNo());
				getRequest().setAttribute("html", html);
				logger.info("报表查询耗时：" + (System.currentTimeMillis() - l1)
						+ " ms");
				return "workflow";
			} else if ("pdf".equals(exportType)) {
				reportService.exportPdf(jasperPrint, reportDefinition
						.getDefinitionName());
			} else if ("print".equals(exportType)) {
				reportService.exportPrinter(jasperPrint);
			} else if ("excel".equals(exportType)) {
				reportService.exportExcel(jasperPrint, reportDefinition
						.getDefinitionName());
			}
		} catch (Exception e) {
			logger.error("展现报表出错", e);
		}
		return null;
	}

	@Override
	public String delete() throws Exception {
		String[] definitionIds = definitionId.split(",");
		for (int i = 0; i < definitionIds.length; i++) {
			manager.delete(definitionIds[i]);
		}
		return RELOAD;
	}

	@Override
	public String input() throws Exception {
		if (definitionId != null && !definitionId.equals("")) {
			// showItemList=manager.getShowitemListByDefinitionId(definitionId);
			showItemList = showitemManager.getListByDefinitionId(definitionId);
		}
		return "input";
	}

	@Override
	public String list() throws Exception {
		// User userInfo = userService.getCurrentUser();
		// model.setDefinitionCreateUserId(userInfo.getUserId());
		page = manager.getPage(page, model);
		List<ToaReportSort> rsList = reportSortManager.getAllList();
		if (rsList != null && rsList.size() > 0) {
			for (ToaReportSort sortModel : rsList) {
				sortNameMap.put(sortModel.getSortId(), sortModel.getSortName());
			}
		}

		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (definitionId != null && !definitionId.equals("")) {
			model = manager.getDefinitionById(definitionId);
		} else {
			model = new ToaReportDefinition();
			model.setDefinitionId("");
		}

	}

	@Override
	public String save() throws Exception {
		User userInfo = userService.getCurrentUser();
		String msg = "";
		if (model.getDefinitionId() != null
				&& model.getDefinitionId().equals("")) {
			model.setDefinitionId(null);
		} else {
			showItemList = showitemManager.getListByDefinitionId(model
					.getDefinitionId());
			if (showItemList != null && showItemList.size() > 0) {

				showitemManager.deleteList(showItemList);
				showItemList = null;
			}
		}
		User user = userService.getCurrentUser();
		TUumsBaseOrg org = userService.getSupOrgByUserIdByHa(user.getUserId());
		if (org != null) {
			model.setDefinitionOrgId(org.getOrgId());
			model.setDefinitionOrgCode(org.getSupOrgCode());
		}

		if (sortId != null && !sortId.equals("") && showitemData != null
				&& !showitemData.equals("")) {
			ToaReportSort reportSort = reportSortManager
					.getReportSortById(sortId);
			model.setToaReportSort(reportSort);
			model.setDefinitionCreateUserId(userInfo.getUserId()); // 添加创建者用户ID
			manager.save(model); // 保存定义报表
			showItemList = manager.getShowitemListBySplit(showitemData, model); // 获取定义报表的显示项列表信息
			showitemManager.saveList(showItemList); // 保存定义报表显示项
			msg = "保存成功!";

		}
		addActionMessage(msg);
		  StringBuffer returnhtml=new StringBuffer("");
			returnhtml.append(
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
			.append("<script>");
			
			returnhtml.append("window.dialogArguments.window.location = window.dialogArguments.window.location;window.close();")
			.append("</script>");
		return renderHtml(returnhtml.toString());
	}

	// 判断定义报表名称是否存在
	public String isHasDefinitionName() throws Exception {
		boolean boo = manager.isHasDefinitionName(definitionName, definitionId);
		if (boo) {
			return renderText("1");
		} else {
			return renderText("0");
		}
	}

	// 选择工作流
	public String checkWorkflow() throws Exception {

		if (definitionFormid != null && !definitionFormid.equals("")) {
			workflowList = manager.getWorkflowNameByformId(definitionFormid);

		}
		return "checkWorkflow";

	}

	// 对选择的表单，展现它的显示项
	public String getShowitemList() throws Exception {
		String showitemInfo = "";
		try {
			showItemList = manager.showitemListByFormId(definitionFormid);
			if (showItemList != null && !showItemList.isEmpty()) {
				JSONArray showitemArray = new JSONArray(); // 组状 显示项名，和显示名名称
				if (showItemList != null && showItemList.size() > 0) {
					for (ToaReportShowitem showitem : showItemList) {
						String text = showitem.getShowitemText();
						if (text != null || !"".equals(text)
								|| !"undefined".equals(text)) {
							JSONObject objShowitem = new JSONObject();
							objShowitem.put("showitemName", showitem
									.getShowitemName());
							objShowitem.put("showitemText", showitem
									.getShowitemText());
							showitemArray.add(objShowitem);
						}
					}
				}
				showitemInfo = showitemArray.toString();
			} else {
				showitemInfo = "error";
				JSONObject objShowitem = new JSONObject();
				objShowitem.put("showitemName", showitemInfo);
				showitemInfo = objShowitem.toString();
			}

		} catch (Exception e) {
			showitemInfo = "-1";
		}
		System.out.println("showitemInfo === " + showitemInfo);
		return this.renderText(showitemInfo);
	}

	// 删除报表显示项
	public String deleteByShowitem() throws Exception {
		try {
			showitemManager.deleteByShowitemId(showitemId);
			return renderText("1");
		} catch (Exception e) {
			e.printStackTrace();
			return renderText("error");
		}
	}

	public ToaReportDefinition getModel() {
		// TODO 自动生成方法存根
		return model;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public ReportDefineManager getManager() {
		return manager;
	}

	@Autowired
	public void setManager(ReportDefineManager manager) {
		this.manager = manager;
	}

	public Page<ToaReportDefinition> getPage() {
		return page;
	}

	public void setPage(Page<ToaReportDefinition> page) {
		this.page = page;
	}

	public WorkflowReportSortManager getReportSortManager() {
		return reportSortManager;
	}

	@Autowired
	public void setReportSortManager(WorkflowReportSortManager reportSortManager) {
		this.reportSortManager = reportSortManager;
	}

	public Map<String, String> getSortNameMap() {
		return sortNameMap;
	}

	public void setSortNameMap(Map<String, String> sortNameMap) {
		this.sortNameMap = sortNameMap;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getEditState() {
		return editState;
	}

	public void setEditState(String editState) {
		this.editState = editState;
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public IWorkflowService getWorkflowService() {
		return workflowService;
	}

	@Autowired
	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	public String getDefinitionFormid() {
		return definitionFormid;
	}

	public void setDefinitionFormid(String definitionFormid) {
		this.definitionFormid = definitionFormid;
	}

	public List<WorkFlowBean> getWorkflowList() {
		return workflowList;
	}

	public void setWorkflowList(List<WorkFlowBean> workflowList) {
		this.workflowList = workflowList;
	}

	public List<ToaReportShowitem> getShowItemList() {
		return showItemList;
	}

	public void setShowItemList(List<ToaReportShowitem> showItemList) {
		this.showItemList = showItemList;
	}

	public String getShowitemData() {
		return showitemData;
	}

	public void setShowitemData(String showitemData) {
		this.showitemData = showitemData;
	}

	public ReportShowitemManager getShowitemManager() {
		return showitemManager;
	}

	@Autowired
	public void setShowitemManager(ReportShowitemManager showitemManager) {
		this.showitemManager = showitemManager;
	}

	public String getShowitemId() {
		return showitemId;
	}

	public void setShowitemId(String showitemId) {
		this.showitemId = showitemId;
	}

	public Page getPageReport() {
		return pageReport;
	}

	public void setPageReport(Page pageReport) {
		this.pageReport = pageReport;
	}

	public String getWorkflowCode() {
		return workflowCode;
	}

	public void setWorkflowCode(String workflowCode) {
		try {
			workflowCode = URLDecoder.decode(workflowCode, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		this.workflowCode = workflowCode;
	}

	public String getWorkflowTitle() {
		return workflowTitle;
	}

	public void setWorkflowTitle(String workflowTitle) {
		try {
			workflowTitle = URLDecoder.decode(workflowTitle, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		this.workflowTitle = workflowTitle;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public Integer getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(Integer columnWidth) {
		this.columnWidth = columnWidth;
	}

}
