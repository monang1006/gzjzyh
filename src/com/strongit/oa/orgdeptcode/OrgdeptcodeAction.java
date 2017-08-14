package com.strongit.oa.orgdeptcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.autoencoder.IRuleService;
import com.strongit.oa.bo.ToaOrgRule;
import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.common.user.IUserService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class OrgdeptcodeAction extends BaseActionSupport {
	
	@Autowired OrgdeptcodeManager orgruleManager;
	
	@Autowired IUserService userService;
	
		/** 编码Service层 */
	 @Autowired IRuleService ruleService;
	
	private List<TUumsBaseOrg> orgList;
	
	/** 分页对象 */
	private Page<ToaRule> page = new Page<ToaRule>(FlexTableTag.MAX_ROWS, true);
	
	private String syscode;
	
	private String codeType;
	
//	用户所属组织机构Id
	private String extOrgId;
	
	private String ruleIds;
	
	private String ruleName;
	
	private ToaRule rule = new ToaRule();
	
	private ToaOrgRule model = new ToaOrgRule();
	
	
	public String getExtOrgId() {
		return extOrgId;
	}

	public void setExtOrgId(String extOrgId) {
		this.extOrgId = extOrgId;
	}

	/**
	 * 获取组织机构树
	 * 
	 * @return
	 * @throws IOException
	 */
	public String tree() throws IOException {
	//	orgList = .queryOrgs(Const.IS_NO);
		if(userService.isSystemDataManager(userService.getCurrentUser().getUserId())) {
			orgList = userService.getAllOrgInfos();	
		} else {
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUser().getUserId());
			//syscode=org.getOrgSyscode();
			if(org.getIsOrg().equals("0")){
				syscode=org.getSupOrgCode();
			}
			else{
				syscode=org.getOrgSyscode();
			}
			
			String haid=org.getOrgId();
			orgList=userService.queryOrgsByHa(Const.IS_NO,haid);			
		}

		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "tree";
	}

	@Override
	public String delete() throws Exception {
		try {
			String[] ids=ruleIds.split(",");
			//StringBuffer ru=new StringBuffer("");
			String ru="";
			for (int i=0;i<ids.length;i++) {
				ru += "'" +ids[i] + "',";
			}	
			StringBuffer rus=new StringBuffer(ru);
			if(ru.indexOf(",")>0){
			  rus.deleteCharAt(ru.lastIndexOf(","));
			}
			List<ToaOrgRule>  list=orgruleManager.getListByOrgRuleId(extOrgId, rus.toString());
			orgruleManager.deleteToaOrgRules(list);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("deletefalse");
		
		}
		renderText("deleted");
		return null;
		
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		//if (extOrgId != null && extOrgId.trim().length() > 0) {
			
				//page = userManager.queryOrgUsersByHa(page, extOrgId, selectname, selectloginname, "0", isActive, userManager.getCurrentUserInfo().getOrgId());
			page=orgruleManager.getRuleList(page,ruleName,extOrgId);
				
		//}
		//else
	//	{
	//		page = orgruleManager.getAllRuleList(page,ruleName);
	//	}
		
		return "list";
	}
	
	public String ruleList() throws Exception {
		if (extOrgId != null && extOrgId.trim().length() > 0) {
			page = orgruleManager.getAllRuleList(page,ruleName,extOrgId);
		}		
		return "ruleList";
	}
	
	public String addRule() throws Exception{
		if(ruleIds != null && ruleIds.trim().length() > 0){
			List<ToaOrgRule> orgrule = orgruleManager.getAllListByOrgId(extOrgId);
			String[] ruleids=ruleIds.split(",");
			ArrayList<String> rus=new ArrayList();
			ArrayList<String> arr=new ArrayList();
		
			if (orgrule != null && orgrule.size() > 0) {
				for (Iterator<ToaOrgRule> it = orgrule.iterator(); it.hasNext();) {
					ToaOrgRule role = it.next();
					rus.add(role.getRuleId());
				}
				 
				 }
			  for(int i=0;i<ruleids.length;i++)
			   {
				   arr.add(ruleids[i]);
					
				 }
			  for(int i=0;i<ruleids.length;i++)
			   {
			    if(rus.contains(ruleids[i])){
			    	arr.remove(ruleids[i]);
			    }
			   }
		   for(int i=0;i<arr.size();i++)
		   {
			   model = new ToaOrgRule();
			   model.setTid(null);
			   model.setDeptId(extOrgId);
			  // model.setOrgId(extOrgId);
			   model.setOrgId(userService.getParentOrgByOrgId(extOrgId).getOrgId());
			   model.setRuleId(arr.get(i).toString());
			   orgruleManager.saveToaOrgRule(model);
		   }
		}
	return renderHtml("<script> window.dialogArguments.location='"
						+ getRequest().getContextPath()
						+ "/orgdeptcode/orgdeptcode.action?extOrgId="+extOrgId+"'; window.close();</script>");
		//return "list";
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
		return this.model;
	}
	
	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public Page<ToaRule> getPage() {
		return page;
	}

	public void setPage(Page<ToaRule> page) {
		this.page = page;
	}

	public ToaRule getRule() {
		return rule;
	}

	public void setRule(ToaRule rule) {
		this.rule = rule;
	}


	public void setModel(ToaOrgRule model) {
		this.model = model;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleIds() {
		return ruleIds;
	}

	public void setRuleIds(String ruleIds) {
		this.ruleIds = ruleIds;
	}


}
