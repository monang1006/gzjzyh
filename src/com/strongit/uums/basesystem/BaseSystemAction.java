package com.strongit.uums.basesystem;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseSysmanager;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "baseSystem.action", type = ServletActionRedirectResult.class) })
public class BaseSystemAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Page<TUumsBaseSystem> page = new Page<TUumsBaseSystem>(FlexTableTag.MAX_ROWS,
			true);

	private String sysId;

	private String codeType;

	private String userId;

	private String userPrivil = "";

	private List<TUumsBaseSystem> listSystem = null;

	private List<TUumsBaseUser> userList = null;

	private List<TUumsBaseOrg> orgList = null;

	private TUumsBaseSystem boBaseSystem = new TUumsBaseSystem();

	@Autowired
	private IUserService userService;

	private String orgcode;
	
	private String message;
	
	//是否系统下的权限设置为启用状态
	private String childTogether;
	
	private Map systemMap = new HashMap(); 
	
	/*
	 * 查询相关属性
	 */
	private String selectsystemid;//系统ID
	private String selectsystemname;//系统名称
	private String systemisact;//是否启用（0，1）
	private Date startDate	;//开始时间
	private Date endDate	;//结束时间
	
	public BaseSystemAction(){
		
		systemMap.put("0", "否");
		systemMap.put("1", "是");
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public TUumsBaseSystem getBoBaseSystem() {
		return boBaseSystem;
	}

	public List<TUumsBaseSystem> getListSystem() {
		return listSystem;
	}

	public Page<TUumsBaseSystem> getPage() {
		return page;
	}

	@Override
	public String delete() throws Exception {
		try {
			userService.deleteSystems(sysId);
			addActionMessage("全局岗位信息删除成功。");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		}
		return renderHtml("<script>window.location='"+getRequest().getContextPath()+"/basesystem/baseSystem.action';</script>");

//		return RELOAD;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		if(endDate != null){
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_MONTH, 1); // 加一天
			d1 = cal.getTime();
		}else{
			d1 = null;
		}
		page = userService.querySystems(page, selectsystemid, selectsystemname, systemisact, startDate, d1);
		return SUCCESS;
	}

	@Override
	public String save() throws Exception {
        String sysId= getRequest().getParameter("sysId1");
		if (sysId!= null && !"".equals(sysId)){
			boBaseSystem.setSysId(sysId);
		}else{
			boBaseSystem.setSysId(null);
		}
		try {
			userService.saveSystem(boBaseSystem, childTogether);
			addActionMessage("外挂系统信息保存成功。");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
//		return RELOAD;
		return renderHtml("<script>window.dialogArguments.location='"+getRequest().getContextPath()+"/basesystem/baseSystem.action'; window.close();</script>");

	}

	@Override
	protected void prepareModel() throws Exception {
		if (sysId != null) {
			boBaseSystem = userService.getSystemBySystemId(sysId);
		} else {
			boBaseSystem = new TUumsBaseSystem();
			Long sequence = userService.getNextSystemSequenceCode();
			boBaseSystem.setSysSequence(sequence);
		}
	}

	public TUumsBaseSystem getModel() {

		return this.boBaseSystem;
	}

	public String tree() throws IOException {
		boBaseSystem = userService.getSystemBySystemId(sysId);
		Set<TUumsBaseSysmanager> systemManager = boBaseSystem.getBaseSysmanagers();
		if(systemManager.size()>0){
			for(Iterator<TUumsBaseSysmanager> it = systemManager.iterator() ; it.hasNext() ;){
				TUumsBaseSysmanager bo = it.next(); 
				userPrivil += "," + bo.getUserId();
			}
		}
		if(userPrivil.length() > 0){
			userPrivil.substring(1);
		}
		orgList = userService.getAllOrgInfos();
		userList = userService.getAllUserInfos();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "tree";
	}

	public String saveUsers() {
		try {

			//manager.saveSystemUsers(sysId, userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RELOAD;
	}
	/**
	 * 比较编码唯一性
	 *@author 
	 *@date 
	 * @return String 
	 * @throws Exception
	 */
	public String compareCode() throws Exception{
		boolean flag = userService.compareSystemCode(orgcode);
		if(flag==true){
			message="0";
		}
		else 
			message="1";
		return this.renderText(message);
	}
	
	public String getCodeType() {
		return codeType;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public String getUserPrivil() {
		return userPrivil;
	}

	public void setUserPrivil(String userPrivil) {
		this.userPrivil = userPrivil;
	}
	public Map getSystemMap() {
		return systemMap;
	}
	public void setSystemMap(Map systemMap) {
		this.systemMap = systemMap;
	}
	public String getOrgcode() {
		return orgcode;
	}
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	public String getSelectsystemid() {
		return selectsystemid;
	}
	public void setSelectsystemid(String selectsystemid) {
		this.selectsystemid = selectsystemid;
	}
	public String getSelectsystemname() {
		return selectsystemname;
	}
	public void setSelectsystemname(String selectsystemname) {
		this.selectsystemname = selectsystemname;
	}
	public String getSystemisact() {
		return systemisact;
	}
	public void setSystemisact(String systemisact) {
		this.systemisact = systemisact;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getChildTogether() {
		return childTogether;
	}
	public void setChildTogether(String childTogether) {
		this.childTogether = childTogether;
	}
}
