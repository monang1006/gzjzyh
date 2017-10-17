package com.strongit.gzjzyh.statistic;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.appConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.gzjzyh.policeregister.IPoliceRegisterManager;
import com.strongit.gzjzyh.service.IQueryApplyService;
import com.strongit.gzjzyh.service.IQueryAuditService;
import com.strongit.gzjzyh.util.FileKit;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.rolemanage.IRoleManager;
import com.strongit.uums.util.Const;
import com.strongit.workflow.util.TimeKit;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class StatisticAction extends BaseActionSupport {

	public TGzjzyhApplyVo model = new TGzjzyhApplyVo();

	@Autowired
	IStatisticService statisticService;

	private Page<TGzjzyhApplication> page = new Page<TGzjzyhApplication>(10,
			true);
	
	private String bankNamesJson;
	
	private String minTimesJson;
	
	private String maxTimesJson;
	
	private String avgTimesJson;
	
	private String titlesJson;
	
	private String datasJson;

	private Map<String, String> statusMap = new HashMap<String, String>();

	private Map<String, String> typeMap = new HashMap<String, String>();
	
	private Map<String, String> userMap = new HashMap<String, String>();

	private MyLogManager myLogManager;

	private static final String DEFAULT_UPLOAD_IMAGE = "/images/upload/defaultUpload.jpg";

	private String errorMsg = "";
	
	public StatisticAction() {
		statusMap.put(appConstants.STATUS_SUBMIT_NO, "待提交");
		statusMap.put(appConstants.STATUS_SUBMIT_YES, "待审核");
		statusMap.put(appConstants.STATUS_AUDIT_YES, "已审核");
		statusMap.put(appConstants.STATUS_AUDIT_BACK, "已退回");

		statusMap.put(appConstants.STATUS_SIGN_YES, "已签收");
		statusMap.put(appConstants.STATUS_PROCESS_YES, "已处理");
		statusMap.put(appConstants.APP_STATUS_REFUSE, "已拒签");

		typeMap.put("0", "查询申请");
		typeMap.put("1", "冻结申请");
		typeMap.put("2", "续冻申请");
		typeMap.put("3", "解冻申请");
	}

	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

	@Override
	public Object getModel() {
		return this.model;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}

	@Override
	public String save() throws Exception {
		return "close";
	}
	
	public String efficiencyStatistic() throws Exception{
		List<Object[]> lst = this.statisticService.efficiencyStatistic(false, null, null);
		List<String> bankNameLst = new ArrayList<String>(0);
		List<Integer> minTimeLst = new ArrayList<Integer>(0);
		List<Integer> maxTimeLst = new ArrayList<Integer>(0);
		List<Integer> avgTimeLst = new ArrayList<Integer>(0);
		if(lst != null && !lst.isEmpty()){
			for(Object[] item : lst){
				double d = Double.parseDouble(item[0].toString());
				int i = Double.valueOf(item[0].toString()).intValue();
				if(i < d) {
					i = i+1;
				}
				minTimeLst.add(i);
				
				d = Double.parseDouble(item[1].toString());
				i = Double.valueOf(item[1].toString()).intValue();
				if(i < d) {
					i = i+1;
				}
				maxTimeLst.add(i);
				
				d = Double.parseDouble(item[2].toString());
				i = Double.valueOf(item[2].toString()).intValue();
				if(i < d) {
					i = i+1;
				}
				avgTimeLst.add(i);
				
				bankNameLst.add(item[3].toString());
			}
		}
		this.bankNamesJson = JSON.toJSONString(bankNameLst);
		this.minTimesJson = JSON.toJSONString(minTimeLst);
		this.maxTimesJson = JSON.toJSONString(maxTimeLst);
		this.avgTimesJson = JSON.toJSONString(avgTimeLst);
		
		return "efficiency";
	}
	
	public String timeStatistic() throws Exception{
		List<Object[]> lst = this.statisticService.timeStatistic(false, null, null);
		List<String> yearLst = new ArrayList<String>(0);
		Map<String, Map<String, List<Integer>>> datas = new HashMap<String, Map<String, List<Integer>>>(0);
		if(lst != null && !lst.isEmpty()){
			for(Object[] item : lst){
				String year = item[0].toString();
				String month = item[1].toString();
				String appType = item[2].toString();
				Integer amount = Integer.parseInt(item[3].toString());
				if(!yearLst.contains(year)){
					yearLst.add(year);
					Map<String, List<Integer>> map = new HashMap<String, List<Integer>>(0);
					List<Integer> tmpLst = new ArrayList<Integer>(0);
					for(int i=0;i<12;i++){
						tmpLst.add(0);
					}
					map.put("0", tmpLst);
					tmpLst = new ArrayList<Integer>(0);
					for(int i=0;i<12;i++){
						tmpLst.add(0);
					}
					map.put("1", tmpLst);
					tmpLst = new ArrayList<Integer>(0);
					for(int i=0;i<12;i++){
						tmpLst.add(0);
					}
					map.put("2", tmpLst);
					tmpLst = new ArrayList<Integer>(0);
					for(int i=0;i<12;i++){
						tmpLst.add(0);
					}
					map.put("3", tmpLst);
					datas.put(year, map);
				}
				List<Integer> amountLst = datas.get(year).get(appType);
				if("01".equals(month)){
					amountLst.set(0, amountLst.get(0)+amount);
				}else if("02".equals(month)){
					amountLst.set(1, amountLst.get(1)+amount);
				}else if("03".equals(month)){
					amountLst.set(2, amountLst.get(2)+amount);
				}else if("04".equals(month)){
					amountLst.set(3, amountLst.get(3)+amount);
				}else if("05".equals(month)){
					amountLst.set(4, amountLst.get(4)+amount);
				}else if("06".equals(month)){
					amountLst.set(5, amountLst.get(5)+amount);
				}else if("07".equals(month)){
					amountLst.set(6, amountLst.get(6)+amount);
				}else if("08".equals(month)){
					amountLst.set(7, amountLst.get(7)+amount);
				}else if("09".equals(month)){
					amountLst.set(8, amountLst.get(8)+amount);
				}else if("10".equals(month)){
					amountLst.set(9, amountLst.get(9)+amount);
				}else if("11".equals(month)){
					amountLst.set(10, amountLst.get(10)+amount);
				}else if("12".equals(month)){
					amountLst.set(11, amountLst.get(11)+amount);
				}
			}
		}
		
		this.titlesJson = JSON.toJSONString(yearLst);
		this.datasJson = JSON.toJSONString(datas);
		
		return "time";
	}
	
	public String areaStatistic() throws Exception{
		List<Object[]> lst = this.statisticService.areaStatistic(false, null, null);
		List[] result = this.statisticService.getAllOrgNames();
		List<String> areaLst = result[0];
		List<String> orgSysCodes = result[1];
		Map<String, List<Integer>> datas = new HashMap<String, List<Integer>>(0);
		if(lst != null && !lst.isEmpty()){
			List<Integer> tmpLst = new ArrayList<Integer>(0);
			for(int i=0;i<areaLst.size();i++){
				tmpLst.add(0);
			}
			datas.put("0", tmpLst);
			tmpLst = new ArrayList<Integer>(0);
			for(int i=0;i<areaLst.size();i++){
				tmpLst.add(0);
			}
			datas.put("1", tmpLst);
			tmpLst = new ArrayList<Integer>(0);
			for(int i=0;i<areaLst.size();i++){
				tmpLst.add(0);
			}
			datas.put("2", tmpLst);
			tmpLst = new ArrayList<Integer>(0);
			for(int i=0;i<areaLst.size();i++){
				tmpLst.add(0);
			}
			datas.put("3", tmpLst);
			
			for(Object[] item : lst){
				String orgName = item[0].toString();
				String orgSysCode = item[1].toString();
				String appType = item[2].toString();
				Integer amount = Integer.parseInt(item[3].toString());
				List<Integer> amountLst = datas.get(appType);
				int index = 0;
				for(;index<orgSysCodes.size();index++) {
					String tmpOrgSysCode = orgSysCodes.get(index);
					if(orgSysCode.startsWith(tmpOrgSysCode)) {
						break;
					}
				}
				if(index < orgSysCodes.size()){
					amountLst.set(index, amountLst.get(index)+amount);
				}
			}
		}
		
		this.titlesJson = JSON.toJSONString(areaLst);
		this.datasJson = JSON.toJSONString(datas);
		
		return "area";
	}
	
	public String bankStatistic() throws Exception{
		List<Object[]> lst = this.statisticService.bankStatistic(false, null, null);
		List<String> bankLst = this.statisticService.getAllBankNames();
		Map<String, List<Integer>> datas = new HashMap<String, List<Integer>>(0);
		if(lst != null && !lst.isEmpty()){
			List<Integer> tmpLst = new ArrayList<Integer>(0);
			for(int i=0;i<bankLst.size();i++){
				tmpLst.add(0);
			}
			datas.put("2", tmpLst);
			tmpLst = new ArrayList<Integer>(0);
			for(int i=0;i<bankLst.size();i++){
				tmpLst.add(0);
			}
			datas.put("4", tmpLst);
			tmpLst = new ArrayList<Integer>(0);
			for(int i=0;i<bankLst.size();i++){
				tmpLst.add(0);
			}
			datas.put("5", tmpLst);
			
			for(Object[] item : lst){
				String orgName = item[0].toString();
				String appStatus = item[1].toString();
				Integer amount = Integer.parseInt(item[2].toString());
				List<Integer> amountLst = datas.get(appStatus);
				int index = bankLst.indexOf(orgName);
				if(index != -1){
					amountLst.set(index, amountLst.get(index)+amount);
				}
			}
		}
		
		this.titlesJson = JSON.toJSONString(bankLst);
		this.datasJson = JSON.toJSONString(datas);
		
		return "bank";
	}
	
	public void prepareGetApplyView() throws Exception{
		prepareModel();
	}

	public Page<TGzjzyhApplication> getPage() {
		return page;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}

	public Map<String, String> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}


	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	public String getBankNamesJson() {
		return bankNamesJson;
	}

	public void setBankNamesJson(String bankNamesJson) {
		this.bankNamesJson = bankNamesJson;
	}

	public String getMinTimesJson() {
		return minTimesJson;
	}

	public void setMinTimesJson(String minTimesJson) {
		this.minTimesJson = minTimesJson;
	}

	public String getMaxTimesJson() {
		return maxTimesJson;
	}

	public void setMaxTimesJson(String maxTimesJson) {
		this.maxTimesJson = maxTimesJson;
	}

	public String getAvgTimesJson() {
		return avgTimesJson;
	}

	public void setAvgTimesJson(String avgTimesJson) {
		this.avgTimesJson = avgTimesJson;
	}

	public String getTitlesJson() {
		return titlesJson;
	}

	public void setTitlesJson(String titlesJson) {
		this.titlesJson = titlesJson;
	}

	public String getDatasJson() {
		return datasJson;
	}

	public void setDatasJson(String datasJson) {
		this.datasJson = datasJson;
	}

}
