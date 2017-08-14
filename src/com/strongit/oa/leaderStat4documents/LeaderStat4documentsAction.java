package com.strongit.oa.leaderStat4documents;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaDraftRecvdoc;
import com.strongit.oa.bo.ToaSendDocRegist;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.docmonitor.vo.DocMonitorParamter;
import com.strongit.oa.docmonitor.vo.DocMonitorVo;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class LeaderStat4documentsAction extends BaseActionSupport{
	
	private LeaderStat4documentsManage manage ;
	private String numTotal = "";	//已登记数
	private String sendNumTotal = "";	//已分发数
	private String selectType = "";	//查询粒度
	private Map<String, Object> map = null;	//存放返回结果
	private String registOrSend = ""; //判断已登记还是已分发
	private Page<DocMonitorVo> page = new Page<DocMonitorVo>(FlexTableTag.MAX_ROWS, true);
	private String workflowName = ""; // 要查询的流程名称
	private String orgId = ""; // 要查询的机构id
	private List<String> documentsStat = null;//存放领导统计结果
	@Autowired 
	protected IUserService userService;//统一用户服务
	/**
	 * @field param	接受参数vo
	 */
	private DocMonitorParamter param;
	/** 机构列表*/
	private List<TempPo> orgList;
	/** 对应模块类型*/
	private String moduletype = null;

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
		numTotal = manage.getReceiveRegistNum("收文办件登记");
		sendNumTotal = manage.getReceiveRegistNum("处室收文办理");
		
		return "homePage";
	}
	
	/**
	 * 已登记
	 * @return
	 */
	public String wjtj(){
		orgList = manage.getOrgList(moduletype);
		map = manage.getReceiveRegistBySelectType(orgList, selectType, param, "收文办件登记");
		String captionStr = "";
		if("0".equals(selectType)){
			captionStr = "本日收文登记";
		}else if("1".equals(selectType)){
			captionStr = "本周收文登记";
		}else if("2".equals(selectType)){
			captionStr = "本月收文登记";
		}else if("3".equals(selectType)){
			captionStr = "本年收文登记";
		}
		String charXML = gencharwjtjXML(map, captionStr);
		getRequest().setAttribute("charXML", charXML);
		return "wjtj";
	}
	
	/**
	 * 已分发
	 * @return
	 */
	public String wjtjSend(){
		orgList = manage.getOrgList(moduletype);
		map = manage.getReceiveRegistBySelectType(orgList, selectType, param, "处室收文办理");
		String captionStr = "";
		if("0".equals(selectType)){
			captionStr = "本日收文分发";
		}else if("1".equals(selectType)){
			captionStr = "本周收文分发";
		}else if("2".equals(selectType)){
			captionStr = "本月收文分发";
		}else if("3".equals(selectType)){
			captionStr = "本年收文分发";
		}
		String charXML = gencharwjtjXML(map, captionStr);
		getRequest().setAttribute("charXML", charXML);
		return "wjtj";
	}
	
	protected String gencharwjtjXML(Map<String, Object> map,
			String captionStr) {		
		/* 页面显示 */
		String charHeader = "<chart palette='2' caption='"
				+ captionStr
				+ "统计' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1'"
				+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
		String categories = "<categories >";
		String totaldataset = "<dataset seriesName='" + captionStr
				+ "数' color='1328c9'>";
		for (String orgId : map.keySet()) {
			categories = categories + "<category label='"
					+ userService.getOrgInfoByOrgId(orgId).getOrgName() + "' />";
			totaldataset = totaldataset + "<set value='"
					+ map.get(orgId)
					+ "' link='javascript:onclickChar(\\\"" + orgId
					+ "\\\")'/>";
		}
		categories += "</categories >";
		totaldataset += "</dataset>";
		String charXML = charHeader + categories + totaldataset + "</chart>";
		return charXML;
	}
	
	/**
	 * 分管领导本周文件统计(收文 办结 在办)
	 * @return String 格式:"x-x,x-x,x-x;x-x,x-x,x-x" 以";"分割为该领导所分管的各处室本周统计情况  以","分割为收文/办结/在办  再以"-"分割为已登记/已分发数 或 文种
	 */
	public String getLeaderManageOrgs(){
		return this.renderText(manage.getLeaderManageOrgs());
	}
	
	/**
	 * 办结统计柱状图
	 * @return
	 */
	public String getProcessedCountByOrg(){
		List<Map<String, Integer>> totalList = manage.getProcessedCountByOrg();
		int i = 0;
		String captionStr = "";
		String charXMLs = "";
		for(Map<String, Integer> map : totalList){
			if(i==0){
				captionStr = "本日办结文件";
			}else if(i==1){
				captionStr = "本周办结文件";
			}else if(i==2){
				captionStr = "本月办结文件";
			}else if(i==3){
				captionStr = "本年办结文件";
			}
			String charXML = gencharwjtjXML4ajax(map, captionStr);
			charXMLs = charXMLs + "-" + charXML;
			i++;
		}
		return this.renderText(charXMLs.substring(1));
	}
	
	/**
	 * 在办统计柱状图
	 * @return
	 */
	public String getProcessingCountByOrg(){
		Map<String, Integer> map = manage.getProcessingCountByOrg();
		String charXML = gencharwjtjXML4ajax(map, "在办文件");
		return this.renderText(charXML);
	}
	
	/**
	 * for ajax
	 * @param map2
	 * @param captionStr
	 * @return
	 */
	protected String gencharwjtjXML4ajax(Map<String, Integer> map2,
			String captionStr) {		
		/* 页面显示 */
		String charHeader = "<chart palette='2' subCaption='' showValues='0' baseFontSize='10' divLineDecimalPrecision='1' yAxisMaxValue='5'" 
				+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
		String categories = "<categories >";
		String totaldataset = "<dataset seriesName='" + captionStr
				+ "数' color='1328c9'>";
		for (String orgId : map2.keySet()) {
			categories = categories + "<category label='"
					+ userService.getOrgInfoByOrgId(orgId).getOrgName() + "' />";
			totaldataset = totaldataset + "<set value='"
					+ map2.get(orgId)
					+ "'/>";
		}
		categories += "</categories >";
		totaldataset += "</dataset>";
		String charXML = charHeader + categories + totaldataset + "</chart>";
		return charXML;
	}
	
	public String wjtjList(){
		if(registOrSend!=null && "regist".equals(registOrSend)){
			workflowName = "收文办件登记";
		}else{
			workflowName = "处室收文办理";
		}
		page = manage.getReceiveRegistByOrg(orgId, workflowName, selectType);
		return "wjtjlist";
	}
	
	/**
	 * 收文登记统计
	 * @return String 格式:"x-x,x-x,x-x,x-x" 以","分割为日/周/月/年  再以"-"分割为已登记/已分发数
	 */
	public String getReceiveCount(){
		return this.renderText(manage.getReceiveCount());
	}
	
	/**
	 * 在办文件统计(自办文 收文 发文)
	 * @return	String 格式:"x-x-x" 以"-"分割为自办文/收文/发文数
	 */
	public String getProcessingCount(){
		return this.renderText(manage.getProcessingCount());
	}
	
	/**
	 * 办结文件统计(自办文 收文 发文)
	 * @return	String	格式:"x-x-x,x-x-x,x-x-x,x-x-x" 以","分割为日/周/月/年  再以"-"分割为自办文/收文/发文数
	 */
	public String getProcessedCount(){
		return this.renderText(manage.getProcessedCount());
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

	public LeaderStat4documentsManage getReceiveRegistManage() {
		return manage;
	}

	@Autowired
	public void setReceiveRegistManage(LeaderStat4documentsManage receiveRegistManage) {
		this.manage = receiveRegistManage;
	}

	public String getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(String numTotal) {
		this.numTotal = numTotal;
	}
	
	public List getOrgList() {
		return orgList;
	}

	public String getModuletype() {
		return moduletype;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}

	public String getSendNumTotal() {
		return sendNumTotal;
	}

	public void setSendNumTotal(String sendNumTotal) {
		this.sendNumTotal = sendNumTotal;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public DocMonitorParamter getParam() {
		return param;
	}

	public void setParam(DocMonitorParamter param) {
		this.param = param;
	}

	public String getRegistOrSend() {
		return registOrSend;
	}

	public void setRegistOrSend(String registOrSend) {
		this.registOrSend = registOrSend;
	}

	public Page<DocMonitorVo> getPage() {
		return page;
	}

	public void setPage(Page<DocMonitorVo> page) {
		this.page = page;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<String> getDocumentsStat() {
		return documentsStat;
	}

	public void setDocumentsStat(List<String> documentsStat) {
		this.documentsStat = documentsStat;
	}
	
}
