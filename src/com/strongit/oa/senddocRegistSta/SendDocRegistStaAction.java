package com.strongit.oa.senddocRegistSta;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.bgt.documentview.bo.DocumentViewUtil;
import com.strongit.oa.bo.ToaSendDocRegist;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class SendDocRegistStaAction extends BaseActionSupport{
	
	private SendDocRegistStaManager sendDocRegistStaManager;
	private AddressOrgManager addressOrgManager;
	private String numTotal = "";
	/** 对应模块类型*/
	private String moduletype = null;
	/** 机构列表*/
	private List<TempPo> orgList;	
	private String selectType = "";	//查询粒度
	private Map<String, Object> map = null;	//存放返回结果
	@Autowired 
	protected IUserService userService;//统一用户服务
	private Page<ToaSendDocRegist> page = new Page<ToaSendDocRegist>(FlexTableTag.MAX_ROWS, true);
	
	private String roomId = null;	//处室id
	
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
		numTotal = sendDocRegistStaManager.getRegistDocsNum();
		return "list";
	}
	
	/**
	 * 供ajax方式调用(无权限控制)
	 * @return
	 */
	public String getRegistDocsNum(){
		return this.renderText(sendDocRegistStaManager.getRegistDocsNumWithOutRight());
	}
	
	public String wjtjList() throws Exception{
		if("search".equals(type)){
			page = sendDocRegistStaManager.getRegistDocsBySearch(page, searchDocTitle, searchSend, searchRoom, searchDocCode, searchSecret, searchStaTime, searchEndTime, searchSendStaTime, searchSendEndTime);
		}else{
			if(roomId!=null && !"".equals(roomId)){
				page = sendDocRegistStaManager.getRegistDocs(page, selectType, roomId);
				searchRoom = sendDocRegistStaManager.getRoomNameByRoomId(roomId);
			}else{
				page = sendDocRegistStaManager.getRegistDocs(page, selectType, null);
			}
		}
		showDictOrgTreeWithCheckbox("MMDJ");		
		return "wjtjlist";
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
	
	/**
	 * 供ajax调用
	 * @return
	 */
	public String wjtj4ajax(){
		moduletype = "wjtj4ajax";
		orgList = sendDocRegistStaManager.getOrgList(moduletype);
		String charXMLs = "";
		for(int i=0; i<4; i++){
			selectType = i+"";
			map = sendDocRegistStaManager.getRegistDocsBySelectType(orgList, selectType);
			String captionStr = "";
			if("0".equals(selectType)){
				captionStr = "本日发文";
			}else if("1".equals(selectType)){
				captionStr = "本周发文";
			}else if("2".equals(selectType)){
				captionStr = "本月发文";
			}else if("3".equals(selectType)){
				captionStr = "本年发文";
			}
			String charXML = gencharwjtjXML4ajax(map, captionStr);
			charXMLs = charXMLs + "-" + charXML;
		}
		return this.renderText(charXMLs.substring(1));
	}
	
	protected String gencharwjtjXML4ajax(Map<String, Object> map,
			String captionStr) {		
		/* 页面显示 */
		String charHeader = "<chart palette='2' subCaption='' showValues='0' baseFontSize='10' divLineDecimalPrecision='1' yAxisMaxValue='5'" 
				+ " limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>";
		String categories = "<categories >";
		String totaldataset = "<dataset seriesName='" + captionStr
				+ "数' color='1328c9'>";
		for (String orgId : map.keySet()) {
			categories = categories + "<category label='"
					+ userService.getOrgInfoByOrgId(orgId).getOrgName() + "' />";
			totaldataset = totaldataset + "<set value='"
					+ map.get(orgId)
					+ "'/>";
		}
		categories += "</categories >";
		totaldataset += "</dataset>";
		String charXML = charHeader + categories + totaldataset + "</chart>";
		System.out.println("charXML:" + charXML);
		return charXML;
	}
	
	public String wjtj(){
		moduletype = "wjtj";
		orgList = sendDocRegistStaManager.getOrgList(moduletype);
		map = sendDocRegistStaManager.getRegistDocsBySelectType(orgList, selectType);
		String captionStr = "";
		if("0".equals(selectType)){
			captionStr = "本日发文";
		}else if("1".equals(selectType)){
			captionStr = "本周发文";
		}else if("2".equals(selectType)){
			captionStr = "本月发文";
		}else if("3".equals(selectType)){
			captionStr = "本年发文";
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
				+ "统计' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1' yAxisMaxValue='5'" 
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
		System.out.println("charXML:" + charXML);
		return charXML;
	}
	
	public String orgtree() throws Exception{
		orgList = sendDocRegistStaManager.orgtree(moduletype);
		return "orgtree";
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

	public SendDocRegistStaManager getSendDocRegistStaManager() {
		return sendDocRegistStaManager;
	}
	
	@Autowired
	public void setSendDocRegistStaManager(
			SendDocRegistStaManager sendDocRegistStaManager) {
		this.sendDocRegistStaManager = sendDocRegistStaManager;
	}

	public String getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(String numTotal) {
		this.numTotal = numTotal;
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

	public Page<ToaSendDocRegist> getPage() {
		return page;
	}

	public void setPage(Page<ToaSendDocRegist> page) {
		this.page = page;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	@Autowired
	public void setManager(AddressOrgManager manager) {
		this.addressOrgManager = manager;
	}
}
