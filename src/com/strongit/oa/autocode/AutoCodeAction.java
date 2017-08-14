package com.strongit.oa.autocode;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.autoencoder.CodemanageManager;
import com.strongit.oa.autoencoder.IRuleService;
import com.strongit.oa.autoencoder.util.NumberAnalysis;
import com.strongit.oa.bo.ToaCodemanage;
import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.common.user.IUserService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "autoCode.action", type = ServletActionRedirectResult.class) })
public class AutoCodeAction extends BaseActionSupport{

	private static final long serialVersionUID = 4758878952330216378L;
	
	private static Logger log  = Logger.getLogger(AutoCodeAction.class);
	
	@Autowired private IRuleService ruleService;
	
	@Autowired private CodemanageManager codemanageManager;
	
	@Autowired private IUserService userService;
	
	private Page<ToaCodemanage> page = new Page<ToaCodemanage>(FlexTableTag.MAX_ROWS, true);
	
	private String id;
	
	private String selIds;
	
	private String prevCode;
	
	private String type;
	
	private String codeName;
	
	private String orgId;
	
	private Date starttime;
	
	private Date endtime;
	
	private String selCode;
	
	private ToaRule rule;

	private List<ToaRule> list;

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getXmlInfo() throws Exception {
		ToaRule rule = ruleService.getRuleById(id);
		return this.renderText(rule.getRule());
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return "input";
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @author:
	 * @description:
	 * @date : 2011-5-27
	 * @modifyer:
	 * @description: 生成编号
	 * @return
	 * @throws Exception
	 */
	public String getCode() throws Exception {
		
		if(id!=null&&!"".equals(id)){						//显示某个或多个指定的规则
			list = ruleService.getRuleListByRuleIds(id);
		}else{																									//根据当前用户查找规则
			String userId = userService.getCurrentUser().getUserId();
			list = ruleService.getRuleListByUserId(userId);
			//list = ruleService.getAllList();
		}
//		list = ruleService.getAllList();
		ToaRule rule = new ToaRule();
		rule.setId("0");
		rule.setRuleName("请您选择编号规则");
		list.add(0, rule);
		return "getcode";
	}
	
	public String useReservedCode() throws Exception{
		ToaCodemanage obj = codemanageManager.getObjById(id);
		if(obj==null){
			return this.renderText("false");
		}else{
			obj.setCodeStatus("0");
			obj.setCodeCreatetime(new Date());
			if(codemanageManager.saveObj(obj)){
				return this.renderText("true");
			}else{
				return this.renderText("false");
			}
			
		}
	}
	
	public String getReservedCode() throws Exception{
		//String tmpCode=prevCode;
		page = codemanageManager.getReservedCodePageList(page, codeName, starttime, endtime,"1",selCode);
		return "reserved";
	}
	
	public String getUsedCode() throws Exception{
		page = codemanageManager.getReservedCodePageList(page, codeName, starttime, endtime, "0",selCode);
		return "used";
	}
	
	public String getRecCode() throws Exception{
		page = codemanageManager.getPageList(page, codeName, starttime, endtime, "2",selCode);
		return "rec";
	}
	
	public String createRecCode() throws Exception{
		ToaCodemanage obj = codemanageManager.getObjByName(codeName);
		if(obj==null){
			return this.renderText("no");
		}else{
			if("1".equals(obj.getCodeStatus())){
				return this.renderText("res");
			}else if("2".equals(obj.getCodeStatus())){
				return this.renderText("rec");
			}
			obj.setCodeStatus("2");
			if(codemanageManager.saveObj(obj)){
				return this.renderText("true");
			}else{
				return this.renderText("false");
			}
		}
	}
	
	public String useRecCode() throws Exception{
		ToaCodemanage obj = codemanageManager.getObjById(id);
		if(obj==null){
			return this.renderText("false");
		}else{
			obj.setCodeStatus("0");
			obj.setCodeCreatetime(new Date());
			if(codemanageManager.saveObj(obj)){
				return this.renderText("true");
			}else{
				return this.renderText("false");
			}
		}
	}
	
	public String recCode() throws Exception{
		String[] ids = id.split(",");
		for(int i=0;i<ids.length;i++){
			ToaCodemanage obj = codemanageManager.getObjById(ids[i]);
			if(obj==null){
				return this.renderText("false");
			}else{
				obj.setCodeStatus("2");
				obj.setCodeCreatetime(new Date());
				if(codemanageManager.saveObj(obj)){
					
				}else{
					return this.renderText("false");
				}
			}
		}
		return this.renderText("true");
	}
	
	public String getPreview() throws Exception{
		ToaRule rule = ruleService.getRuleById(id);
		//获取当前用户所在部门ID
		String userId = userService.getCurrentUser().getUserId();
		TUumsBaseOrg org=userService.getUserDepartmentByUserId(userId);
		String orgId = org.getOrgId();
		//System.out.println(orgId);
		String xml = rule.getRule();
		String rVal = "error";
		if(xml==null||"".equals(xml)){
			
		}else{
			NumberAnalysis analysis = new NumberAnalysis(xml);
			if("".equals(selIds)){
				rVal = analysis.getMyNumber(orgId);
			}else{
				rVal = analysis.getMyNumber(orgId,selIds.split(","));
			}
		}
		return this.renderText(rVal);
	}
	
	public String createCode() throws Exception{
		ToaRule rule = ruleService.getRuleById(id);
		//获取当前用户所在部门ID
		String userId = userService.getCurrentUser().getUserId();
		TUumsBaseOrg org=userService.getUserDepartmentByUserId(userId);
		String orgId = org.getOrgId();
		//System.out.println(orgId);
		String xml = rule.getRule();
		if(xml==null||"".equals(xml)){
			return this.renderText("error");
		}else{
			NumberAnalysis analysis = new NumberAnalysis(xml);
			String val="";
			if("".equals(selIds)){
				val = analysis.getMyNumber(orgId);
			}else{
				val = analysis.getMyNumber(orgId,selIds.split(","));
			}
			if(prevCode.equals(val)){
				String updatedXml = "";
				if("".equals(selIds)){
					updatedXml = analysis.updateXmlByInfo(orgId);
				}else{
					updatedXml = analysis.updateXmlByInfo(orgId,selIds.split(",")[0]);
				}
				if(updatedXml==null){
					return this.renderText("error2");
				}else{
					rule.setRule(updatedXml);
					this.ruleService.save(rule);
					ToaCodemanage obj = new ToaCodemanage();
					obj.setCodeInfo(val);
					obj.setCoderuleId(id);
					if("reserved".equals(type)){
						obj.setCodeStatus("1");
					}else{
						obj.setCodeStatus("0");
					}
					obj.setCodeUsername(userService.getCurrentUser().getUserName());
					obj.setCodeCreatetime(new Date());
					codemanageManager.saveObj(obj);
					return this.renderText("true");
				}
			}else{
				return this.renderText("noeq");
			}
		}
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

	public List<ToaRule> getList() {
		return list;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSelIds(String selIds) {
		this.selIds = selIds;
	}

	public void setPrevCode(String prevCode) {
		this.prevCode = prevCode;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		if(endtime==null){
			
		}else{
			endtime.setHours(23);
			endtime.setMinutes(59);
			endtime.setSeconds(59);
		}
		this.endtime = endtime;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Page<ToaCodemanage> getPage() {
		return page;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getSelCode() {
		return selCode;
	}

	public void setSelCode(String selCode) {
		this.selCode = selCode;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}
	
	public ToaRule getRule() {
		return rule;
	}

	public void setRule(ToaRule rule) {
		this.rule = rule;
	}

}
