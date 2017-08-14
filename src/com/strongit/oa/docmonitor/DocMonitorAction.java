package com.strongit.oa.docmonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.docmonitor.service.IDocMonitorService;
import com.strongit.oa.docmonitor.util.DocMonitorUtil;
import com.strongit.oa.docmonitor.vo.DocMonitorParamter;
import com.strongit.oa.docmonitor.vo.DocMonitorVo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;


/**
 * 文件监管
 * @author yanjian
 *
 * Sep 25, 2012 11:14:14 AM
 */
@ParentPackage("default")
public class DocMonitorAction extends BaseActionSupport {

	private Page<DocMonitorVo> page = new Page<DocMonitorVo>(
			FlexTableTag.MAX_ROWS, true);

	/**
	 * @field param	接受参数vo
	 */
	private DocMonitorParamter param;

	/**
	 * @field docMonitorManager	文件监管manager
	 */
	@Autowired
	private IDocMonitorService docMonitorManager;

	/**
	 * @field serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

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
		// TODO Auto-generated method stub
		return null;
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

	/**
	 * 文件监管(人保厅)
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 11:11:50 AM
	 */
	public String monitorByRBT() throws Exception {
		StringBuilder result = new StringBuilder();
		int grantCode = 0;
		if (param != null && param.getGrantCode() != 0) {
			grantCode = param.getGrantCode();
		}
		switch (grantCode) {
		case DocMonitorUtil.GRANT_CODE_TLEADER: //厅领导
			break;
		case DocMonitorUtil.GRANT_CODE_DLEADER: //处领导
			break;
		default://个人权限

			break;
		}
		return result.toString();
	}

	/**
	 * 待办
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 11:33:25 AM
	 */
	public String todo() throws Exception {
		StringBuilder result = new StringBuilder();
		int grantCode = 0;
		if (param != null && param.getGrantCode() != 0) {
			grantCode = param.getGrantCode();
		}
		switch (grantCode) {
		case DocMonitorUtil.GRANT_CODE_TLEADER: // 厅领导
			page = docMonitorManager.getTLeaderTodo(page, param);
			result.append("tleadertodo");
			break;
		case DocMonitorUtil.GRANT_CODE_DLEADER: // 处领导
			page = docMonitorManager.getDLeaderTodo(page, param);
			result.append("dleadertodo");
			break;
		default:// 个人权限
			page = docMonitorManager.getPersonalTodo(page, param);
			result.append("personaltodo");
			break;
		}
		return result.toString();
	}

	/**
	 * 已办未办结
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 *             Sep 25, 2012 11:33:33 AM
	 */
	public String processing() throws Exception {
		StringBuilder result = new StringBuilder();
		int grantCode = 0;
		if (param != null && param.getGrantCode() != 0) {
			grantCode = param.getGrantCode();
		}
		switch (grantCode) {
		case DocMonitorUtil.GRANT_CODE_TLEADER: // 厅领导
			page = docMonitorManager.getTLeaderProcessing(page, param);
			result.append("tleaderprocessing");
			break;
		case DocMonitorUtil.GRANT_CODE_DLEADER: // 处领导
			page = docMonitorManager.getDLeaderProcessing(page, param);
			result.append("dleaderprocessing");
			break;
		default:// 个人权限
			page = docMonitorManager.getPersonalProcessing(page, param);
			result.append("personalprocessing");
			break;
		}
		return result.toString();
	}

	/**
	 * 已办办结
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 11:33:45 AM
	 */
	public String processed() throws Exception {
		StringBuilder result = new StringBuilder();
		int grantCode = 0;
		if (param != null && param.getGrantCode() != 0) {
			grantCode = param.getGrantCode();
		}
		switch (grantCode) {
		case DocMonitorUtil.GRANT_CODE_TLEADER: // 厅领导
			page = docMonitorManager.getTLeaderProcessed(page, param);
			result.append("tleaderprocessed");
			break;
		case DocMonitorUtil.GRANT_CODE_DLEADER: // 处领导
			page = docMonitorManager.getDLeaderProcessed(page, param);
			result.append("dleaderprocessed");
			break;
		default:// 个人权限
			page = docMonitorManager.getPersonalProcessed(page, param);
			result.append("personalprocessed");
			break;
		}
		return result.toString();
	}

	/**
	 * 所有已办
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 11:33:59 AM
	 */
	public String processAll() throws Exception {
		StringBuilder result = new StringBuilder();
		int grantCode = 0;
		if (param != null && param.getGrantCode() != 0) {
			grantCode = param.getGrantCode();
		}
		switch (grantCode) {
		case DocMonitorUtil.GRANT_CODE_TLEADER: // 厅领导
			page = docMonitorManager.getTLeaderProcessAll(page, param);
			result.append("tleaderprocessall");
			break;
		case DocMonitorUtil.GRANT_CODE_DLEADER: // 处领导
			page = docMonitorManager.getDLeaderProcessAll(page, param);
			result.append("dleaderprocessall");
			break;
		default:// 个人权限
			page = docMonitorManager.getPersonalProcessAll(page, param);
			result.append("personalprocessall");
			break;
		}
		return result.toString();
	}

	/**
	 * 待办柱状图
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 12:27:52 PM
	 */
	public String todoChar() throws Exception {
		 Map<String, String[]>  map = docMonitorManager.getManagerDeptTodoDocs(param);
		getRequest().setAttribute("charXML", genCharXML("待办文件", map));
		return "todoChar";
	}
	
	
	/**
	 * 查看单个处室待办文件情况
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 4:46:32 PM
	 */
	public String todoDeptLocaltion() throws Exception  {
		page = docMonitorManager.getTodoDeptLocaltion(page, param);
		return "tododeptlocaltion";
	}
	
	
	
	/**
	 * 已办未办结柱状图
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 12:28:05 PM
	 */
	public String processingChar() throws Exception {
		if(param == null){
			param = new DocMonitorParamter();
		}
		param.setState("0");
		Map<String, String[]>  map = docMonitorManager.getManagerDeptProcessDocs(param);
		getRequest().setAttribute("charXML", genCharXML("已办文件", map));
		return "processingChar";
	}

	/**
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 4:46:32 PM
	 */
	public String processingDeptLocaltion() throws Exception  {
		if(param == null){
			param = new DocMonitorParamter();
		}
		param.setState("0");
		page = docMonitorManager.getProcessDeptLocaltion(page, param);
		return "processingdeptlocaltion";
	}
	
	
	/**
	 * 已办办结柱状图
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 12:28:22 PM
	 */
	public String processedChar() throws Exception {
		if(param == null){
			param = new DocMonitorParamter();
		}
		param.setState("1");
		Map<String, String[]>  map = docMonitorManager.getManagerDeptProcessDocs(param);
		getRequest().setAttribute("charXML", genCharXML("办结文件", map));
		return "processedChar";
	}
	
	/**
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 4:46:32 PM
	 */
	public String processedDeptLocaltion() throws Exception  {
		if(param == null){
			param = new DocMonitorParamter();
		}
		param.setState("1");
		page = docMonitorManager.getProcessDeptLocaltion(page, param);
		return "processeddeptlocaltion";
	}
	
	
	/**
	 * 所有已办柱状图
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 12:28:33 PM
	 */
	public String processAllChar() throws Exception {
		if(param == null){
			param = new DocMonitorParamter();
		}
		Map<String, String[]>  map = docMonitorManager.getManagerDeptProcessDocs(param);
		getRequest().setAttribute("charXML", genCharXML("已办文件", map));
		return "processallChar";
	}

	
	/**
	 * 
	 * @author yanjian
	 * @return
	 * @throws Exception
	 * Sep 25, 2012 4:46:32 PM
	 */
	public String processAllDeptLocaltion() throws Exception  {
		if(param == null){
			param = new DocMonitorParamter();
		}
		page = docMonitorManager.getProcessDeptLocaltion(page, param);
		return "processalldeptlocaltion";
	}
	
	
	public Page<DocMonitorVo> getPage() {
		return page;
	}

	public void setPage(Page<DocMonitorVo> page) {
		this.page = page;
	}

	public DocMonitorParamter getParam() {
		return param;
	}

	public void setParam(DocMonitorParamter param) {
		this.param = param;
	}
	private String genCharXML(String captionStr,Map<String, String[]>  map){
		if(map != null && !map.isEmpty()){
			List<String> orgnameList = new ArrayList<String>(map.keySet());
			/* 页面显示 */
			String charHeader = "<chart palette='2' caption='"
				+ captionStr
				+ "统计' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1'"
				+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
			String categories = "<categories >";
			String totaldataset = "<dataset seriesName='" + captionStr
			+ "数' color='1328c9'>";
			for (String orgname : orgnameList) {
				String[] strs = map.get(orgname);
				categories = categories + "<category label='"
				+ orgname + "' />";
				totaldataset = totaldataset + "<set value='"
				+ strs[2]
				       + "' link='javascript:onclickChar(\\\""
				       + strs[1] + "\\\")'/>";
			}
			categories += "</categories >";
			totaldataset += "</dataset>";
			String charXML = charHeader + categories + totaldataset + "</chart>";
			System.out.println("charXML:" + charXML);
			return charXML;
		}
		return "";
	}
}
