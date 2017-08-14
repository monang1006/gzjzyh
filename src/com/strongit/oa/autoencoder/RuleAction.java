package com.strongit.oa.autoencoder;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.common.user.IUserService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.util.Const;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "rule.action", type = ServletActionRedirectResult.class) })
public class RuleAction extends BaseActionSupport<ToaRule> {
	/** 序列码*/
	private static final long serialVersionUID = 1L;
	/** 分页对象 */
	private Page<ToaRule> page = new Page<ToaRule>(FlexTableTag.MAX_ROWS, true);
	/** 编码ID */
	private String id;
	/** 编码名称 */
	private String code;
	/** 编码内容 */
	private String type;
	
	/** 编码Model层 */
	private ToaRule model = new ToaRule();
	
	/** 编码Service层 */
	private IRuleService ruleService;
	
	@Autowired private IUserService userService;

	private List<TUumsBaseOrg> orgList;
	
	private String orgId;            //部门ID
	private String orgName;			//部门名称	
	private String nowYear;			//当前年月
	private String ruleNum;             //规则属性（开始值，步长等）


	/**
	 * 删除编码
	 */
	@Override
	public String delete() throws Exception {
		ruleService.deleteRuleById(id);
		return this.renderText("deleted");
	}

	/**
	 * 查询编码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public String list() throws Exception {

		page = ruleService.getRuleList(page, model);
		return "list";
	}

	/**
	 * 添加编码
	 */
	@Override
	public String input() throws Exception {
		if (id != null && id != "") {
			ToaRule rule = ruleService.getRuleById(id);
			code = rule.getRuleName();
		}
		Date date = new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date); 
		String year = String.valueOf(cal.get(cal.YEAR));
		this.getRequest().setAttribute("nowYear", year);
		//获取部门列表
		if(userService.isSystemDataManager(userService.getCurrentUser().getUserId())) {
			orgList = userService.getAllOrgInfos();	
		} else {
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUser().getUserId());
			
			String haid=org.getOrgId();
			orgList=userService.queryOrgsByHa(Const.IS_NO,haid);			
		}
		//orgList排序
		if(orgList != null && !orgList.isEmpty()){
			Collections.sort(orgList, new Comparator<TUumsBaseOrg>(){	
				public int compare(TUumsBaseOrg org1, TUumsBaseOrg org2) {
					String orgSysCode1 = org1.getOrgSyscode(); 
					String orgSysCode2 = org2.getOrgSyscode(); 
					return orgSysCode1.compareTo(orgSysCode2); 						
				}				
			});
		}
		
		return INPUT;
	}
	
	public String setNum() throws Exception{
		TUumsBaseOrg org = userService.getOrgInfoByOrgId(orgId);
		orgName = org.getOrgName();	
		String[] nums = ruleNum.split(",");
		if(nums.length == 5){
			String rstart = nums[0];
			String rstep = nums[1];
			String rend = nums[2];
			String rnow = nums[3];
			String nowYear = nums[4];
			this.getRequest().setAttribute("rstart", rstart);
			this.getRequest().setAttribute("rstep", rstep);
			this.getRequest().setAttribute("rend", rend);
			this.getRequest().setAttribute("rnow", rnow);
			this.getRequest().setAttribute("nowYear", nowYear);
		}
		return "setNum";
	}
	

	/**
	 * 保存XML
	 * 
	 * @return String
	 */
	@Override
	public String save() throws Exception {
		ToaRule rule = new ToaRule();
		//分级授权操作
		String userId = userService.getCurrentUser().getUserId();
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
		rule.setOrgId(org.getOrgId());
		rule.setOrgCode(org.getSupOrgCode());
		rule.setRuleName(code);
		rule.setRule(type);
		ruleService.save(rule);
		return this.renderText("seved");
	}

	/**
	 * 查找一条规则
	 * 
	 * @return
	 * @throws Exception
	 */
	public String find() throws Exception {
		ToaRule rule = ruleService.getRuleById(id);
		return this.renderText(rule.getRule());
	}

	/**
	 * 更新一条规则
	 * 
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
		ToaRule rule = new ToaRule();
		rule.setId(id);
		rule.setRuleName(code);
		rule.setRule(type);
		ruleService.update(rule);
		return this.renderText("updated");
	}

	@Override
	protected void prepareModel() throws Exception {

	}

	public ToaRule getModel() {
		return this.model;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Autowired
	public void setRuleService(IRuleService ruleService) {
		this.ruleService = ruleService;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Page<ToaRule> getPage() {
		return page;
	}

	public void setPage(Page<ToaRule> page) {
		this.page = page;
	}
	
	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getNowYear() {
		Date date = new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date); 
		String nowYear = String.valueOf(cal.get(cal.YEAR));
		return nowYear;
	}

	public void setNowYear(String nowYear) {
		this.nowYear = nowYear;
	}
	
	public String getRuleNum() {
		return ruleNum;
	}

	public void setRuleNum(String ruleNum) {
		this.ruleNum = ruleNum;
	}
	
}
