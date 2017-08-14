/**
 * 资源权限委托action类
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Jul 8, 2009 2:25:41 PM
 * @version 1.0
 * @classpath com.strongit.uums.abrole.AbroleAction
 */
package com.strongit.uums.abrole;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseAbrole;
import com.strongit.uums.bo.TUumsBaseAbrolePrivil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.util.Const;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = ".action", type = ServletActionRedirectResult.class) })
public class AbroleAction extends BaseActionSupport {
	//分页对象
	private Page<TUumsBaseAbrole> page = new Page<TUumsBaseAbrole>(FlexTableTag.MAX_ROWS,
			true);
	
	//资源权限委托规则Id
	private String abroleId;
	
	//业务模型
	private TUumsBaseAbrole model = new TUumsBaseAbrole();
	
	//要委派的资源权限信息,格式为资源Id,资源Id,...
	private String privilIds;
	
	//要委派的资源权限名称信息,格式为资源名称,资源名称,...
	private String privilNames;
	
	//委托规则是否有效对应标识
	private Map<String, String> activeMap = new HashMap<String, String>();
	
	//委托规则中被委托人名称
	private String targetUserName;
	
	//用户管理业务操作层
	@Autowired
	private IUserService userService;
	
	//被委派人信息
	private List<TUumsBaseUser> userLst;
	
	//被委托人姓名查询条件
	private String targetName;
	
	//委托人姓名查询条件
	private String srcName;
	
	//开始时间查询条件
	private Date startDate;
	
	//结束时间查询条件
	private Date endDate;
	
	//是否有效查询条件
	private String status;
	
	//超级管理员标识(“1”：是；“0”：否)
	private String isSuperManager;
	
	//委托人Id（选择被委托人时使用）
	private String srcUserid;
	
	//组织机构集合，选择委托人时使用
	private List<TUumsBaseOrg> orgLst;
	
	//组织机构编码，选择委托人延迟加载时使用
	private String orgSyscode;
	
	//初始化资源权限集合
	private List<TUumsBasePrivil> privilLst;
	
	//初始化应用系统集合
	private List<TUumsBaseSystem> systemLst;
	
	//应用系统Id，用于权限树的延迟加载
	private String systemId;
	
	//资源权限编码，用于权限树的延迟加载
	private String privilCode;
	
	//委托规则已经选择了的资源权限编码，例如资源Id,资源Id,...
	private String abRolePrivil;

	public List<TUumsBaseOrg> getOrgLst() {
		return orgLst;
	}

	public void setOrgLst(List<TUumsBaseOrg> orgLst) {
		this.orgLst = orgLst;
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#delete()
	 */
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#input()
	 */
	@Override
	public String input() throws Exception {
		//prepareModel();
		try {
			UserInfo user = (UserInfo)this.getUserDetails();
			//非超级管理员设置当前用户为委托人
			if(!Const.IS_YES.equals(user.getUserIsSupManager())){
				model.setAbroleSrcUserid(user.getUserId());
				model.setAbroleSrcUsername(user.getUsername());
			}
			return INPUT;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#list()
	 */
	@Override
	public String list() throws Exception {
		UserInfo user = (UserInfo)this.getUserDetails();
		//超级管理员能看到所有人的委托规则
		if(user != null && Const.IS_YES.equals(user.getUserIsSupManager())){
			page = userService.queryHistoryRecords(page, null,
					srcName, targetName, startDate, endDate, status);
		//非超级管理员只能看到自己的委托规则
		}else if(user != null){
			page = userService.queryHistoryRecords(page, user.getUserId(),
					srcName, targetName, startDate, endDate, status);
		}
		this.isSuperManager = user.getUserIsSupManager();
		activeMap.put("0", "无效");
		activeMap.put("1", "有效");
		activeMap.put("2", "挂起");
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#prepareModel()
	 */
	@Override
	protected void prepareModel() throws Exception {
		//privilIds = "";
		abRolePrivil = "";
		if(abroleId != null && !"".equals(abroleId)){
			model = userService.getAbroleById(abroleId);
			Set<TUumsBaseAbrolePrivil> set = model.getTUumsBaseAbrolePrivils();
			if(set != null && !set.isEmpty()){
				for(TUumsBaseAbrolePrivil abrolePrivil : set){
					//privilIds = "," + abrolePrivil.getBasePrivil().getPrivilId();
					//privilNames = "," + abrolePrivil.getBasePrivil().getPrivilName();
					abRolePrivil = abRolePrivil + "," + abrolePrivil.getBasePrivil().getPrivilSyscode();
				}
				//privilIds = privilIds.substring(1);
				//privilNames = privilNames.substring(1);
				abRolePrivil = abRolePrivil.substring(1);
			}
		}else{
			model = new TUumsBaseAbrole();
		}
	}

	/* (non-Javadoc)
	 * @see com.strongmvc.webapp.action.BaseActionSupport#save()
	 */
	@Override
	public String save() throws Exception {
		//因为传入的为树形延迟加载资源，所以需要先解析传入资源
		String[] privilCodes = privilIds.split("\\|");
		//解析所有子级资源权限
		if(privilCodes.length > 1 && privilCodes[1] != null && !"".equals(privilCodes[1])){
			String[] parseSubPrivilCodes = privilCodes[1].split(",");
			for(String parseSubPrivilCode : parseSubPrivilCodes){
				List<TUumsBasePrivil> subPrivilCodeLst = userService.getPrivilInfosByPrivilCode(parseSubPrivilCode + "%", Const.IS_YES);
				for(int i = 0; i < subPrivilCodeLst.size(); i++){
					privilCodes[0] = privilCodes[0] + "," + subPrivilCodeLst.get(i).getPrivilSyscode();
				}
			}
		}
		//用户没有选择任何资源权限
		if(privilCodes.length == 0){
			privilCodes = new String[]{null};
		}else if(privilCodes[0].startsWith(",")){
			privilCodes[0] = privilCodes[0].substring(1);
		}
		//新增规则
		if(model.getAbroleId() == null || "".equals(model.getAbroleId())){
			model.setAbroleId(null);
			userService.doAppoint(model.getAbroleSrcUserid(), model.getAbroleSrcUsername(), model.getAbroleTargetUserid(),
					model.getAbroleTargetUsername(), model.getAbroleStartTime(), model.getAbroleEndTime(), privilCodes[0],model.getAbroleOrgId());
		//编辑规则
		}else{
			model.setAbroleEnabled("1");
			userService.updateABRole(model, privilCodes[0]);
		}
		return "windowclose";
	}
	
	//初始化选择委托人（取最高级组织机构，其他的延迟加载）
	public String initSelectSrcUser() throws Exception {
		int i = userService.findChildCodeLength(Const.RULE_CODE_ORG, null);
		String random = "%";
		orgLst = userService.getOrgsByOrgSyscode(random, Const.IS_NO);
		for(int j=0; j<orgLst.size();){
			if(orgLst.get(j).getOrgSyscode().length() != i){
				orgLst.remove(j);
			}else{
				j++;
			}
		}
		return "selectsrcuser";
	}
	
	//初始化选择委托人（延迟加载，ajax调用）
	public String lazyLoadUserAndChildOrgs() throws Exception {
		TUumsBaseOrg orgInfo = userService.getOrgInfoBySyscode(orgSyscode);
		userLst = userService.getUserInfoByOrgId(orgInfo.getOrgId(), Const.IS_YES, Const.IS_NO);
		int i = userService.findChildCodeLength(Const.RULE_CODE_ORG, orgInfo.getOrgSyscode());
		if(i == orgInfo.getOrgSyscode().length()){
			orgLst = new ArrayList<TUumsBaseOrg>();
		}else{
			orgLst = userService.getOrgsByOrgSyscode(orgInfo.getOrgSyscode() + "%", Const.IS_NO);
			for(int j=0; j<orgLst.size();){
				if(orgLst.get(j).getOrgSyscode().length() != i){
					orgLst.remove(j);
				}else{
					j++;
				}
			}
		}
		String jasonStr = new String("[");
		if(orgLst != null && !orgLst.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBaseOrg org : orgLst){
				bufStr.append(",{id:'")
						.append(org.getOrgId())
						.append("',name:'")
						.append(org.getOrgName())
						.append("',code:'")
						.append(org.getOrgSyscode())
						.append("',type:'")
						.append("org")
						.append("'}");
			}
			jasonStr = jasonStr + bufStr.substring(1);
		}
		if(userLst != null && !userLst.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBaseUser user : userLst){
				bufStr.append(",{id:'")
						.append(user.getUserId())
						.append("',name:'")
						.append(user.getUserName())
						.append("',code:'")
						.append(user.getUserSyscode())
						.append("',type:'")
						.append("user")
						.append("'}");
			}
			if("[".equals(jasonStr)){
				jasonStr = jasonStr + bufStr.substring(1);
			}else{
				jasonStr = jasonStr + bufStr;
			}
		}
		jasonStr = jasonStr + "]";
		this.renderText(jasonStr);
		return null;
	}
	
	//初始化选择被委派人，必须是相同部门
	public String initSelectTargetUser() throws Exception {
		TUumsBaseUser user = userService.getUserInfoByUserId(srcUserid);
		TUumsBaseOrg orgInfo = userService.getOrgInfoByUserId(user.getUserId());
		userLst = userService.getUserInfoByOrgId(orgInfo.getOrgId(), Const.IS_YES, Const.IS_NO);
		//去除掉委派人
		userLst.remove(user);
		return "selecttargetuser";
	}
	
	//初始化选择委派资源权限(第一级为应用系统)
	public String initSelectPrivil() throws Exception {
		systemLst = userService.getPrivilSystems(srcUserid, Const.IS_YES, Const.IS_YES, Const.IS_YES);
		return "selectprivil";
	}
	
	/**
	 * 得到指定系统下的第一级资源或指定资源code的下一级资源信息，用于ajax延迟加载
	 * @author 喻斌
	 * @date Jul 10, 2009 1:15:47 PM
	 * @return
	 * @throws Exception
	 */
	public String lazyLoadPrivil() throws Exception {
		try {
			StringBuffer code = new StringBuffer("");
			//查找应用系统下的第一级资源权限
			if(this.systemId != null && !"".equals(this.systemId)){
				int codeLength = userService.findChildCodeLength(Const.RULE_CODE_PRIVIL, "");
				for(int i=0; i<codeLength; i++){
					code.append("_");
				}
				privilLst = userService.getInitDelegationPrivilInfoByUserId(srcUserid, Const.IS_YES, Const.IS_YES, Const.IS_YES, code.toString(), systemId, abroleId);
			//查找资源权限下的下一级资源权限
			}else if(this.privilCode != null && !"".equals(this.privilCode)){
				code.append(this.privilCode);
				int codeLength = userService.findChildCodeLength(Const.RULE_CODE_PRIVIL, privilCode);
				int iteratorIndex = codeLength - this.privilCode.length();
				//如果已经达到最大编码
				if(iteratorIndex == 0){
					iteratorIndex = 1;
				}
				for(int i=0; i<iteratorIndex; i++){
					code.append("_");
				}
				privilLst = userService.getInitDelegationPrivilInfoByUserId(srcUserid, Const.IS_YES, Const.IS_YES, Const.IS_YES, code.toString(), "%", abroleId);
			}
			String jasonStr = new String("[");
			if(privilLst != null && !privilLst.isEmpty()){
				StringBuffer bufStr = new StringBuffer("");
				for(TUumsBasePrivil privil : privilLst){
					bufStr.append(",{id:'")
							.append(privil.getPrivilId())
							.append("',name:'")
							.append(privil.getPrivilName())
							.append("',code:'")
							.append(privil.getPrivilSyscode())
							.append("',system:'")
							.append(privil.getBasePrivilType().getBaseSystem().getSysId())
							.append("'}");
				}
				jasonStr = jasonStr + bufStr.substring(1);
			}
			jasonStr = jasonStr + "]";
			this.renderText(jasonStr);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	//取消委托规则
	public String cancel() throws Exception {
		userService.cancelAbrole(abroleId);
		return list();
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	public TUumsBaseAbrole getModel() {
		return model;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsSuperManager() {
		return isSuperManager;
	}

	public void setIsSuperManager(String isSuperManager) {
		this.isSuperManager = isSuperManager;
	}

	public String getSrcUserid() {
		return srcUserid;
	}

	public void setSrcUserid(String srcUserid) {
		this.srcUserid = srcUserid;
	}

	public String getOrgSyscode() {
		return orgSyscode;
	}

	public void setOrgSyscode(String orgSyscode) {
		this.orgSyscode = orgSyscode;
	}

	public List<TUumsBaseUser> getUserLst() {
		return userLst;
	}

	public void setUserLst(List<TUumsBaseUser> userLst) {
		this.userLst = userLst;
	}

	public List<TUumsBasePrivil> getPrivilLst() {
		return privilLst;
	}

	public void setPrivilLst(List<TUumsBasePrivil> privilLst) {
		this.privilLst = privilLst;
	}

	public List<TUumsBaseSystem> getSystemLst() {
		return systemLst;
	}

	public void setSystemLst(List<TUumsBaseSystem> systemLst) {
		this.systemLst = systemLst;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getAbRolePrivil() {
		return abRolePrivil;
	}

	public void setAbRolePrivil(String abRolePrivil) {
		this.abRolePrivil = abRolePrivil;
	}

	public String getPrivilCode() {
		return privilCode;
	}

	public void setPrivilCode(String privilCode) {
		this.privilCode = privilCode;
	}

	public String getPrivilIds() {
		return privilIds;
	}

	public void setPrivilIds(String privilIds) {
		this.privilIds = privilIds;
	}

	public Page<TUumsBaseAbrole> getPage() {
		return page;
	}

	public void setPage(Page<TUumsBaseAbrole> page) {
		this.page = page;
	}

	public String getAbroleId() {
		return abroleId;
	}

	public void setAbroleId(String abroleId) {
		this.abroleId = abroleId;
	}

	public Map<String, String> getActiveMap() {
		return activeMap;
	}

	public void setActiveMap(Map<String, String> activeMap) {
		this.activeMap = activeMap;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

}
