package com.strongit.uums.synchroni;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "synchroni!synchroni.action", type = ServletActionRedirectResult.class) })
public class SynchroniAction extends BaseActionSupport<TUumsBaseOrg> {

	private String codeType;

	private List<FinaBaseOrg> orgList;
	
	private List<TUumsBaseOrg> finaList;
	
	private List<FinanceUser> usersList;
	
	private SynchroniManager synchManager;
	
	private String orgId;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 获取组织机构page页
	 */
	@Override
	public String list() throws Exception {
		
		return SUCCESS;
	}

	/**
	 * 保存组织机构
	 */
	@Override
	public String save() throws Exception {
		orgList=this.getOrgList();
		if(this.orgList!=null)
		{
			synchManager.setSynchronizeforOrg(orgList);
			return renderHtml("<script>window.returnValue='yes';window.close();</script>");
		}
		else
		return renderHtml("<script>window.returnValue='no';window.close();</script>");
	}

	/**
	 * 获取财政同步的组织机构树
	 * 
	 * @return
	 * @throws IOException
	 */
	public String tree() throws IOException {
		try{
		orgList=this.getOrgList();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		}catch(Exception ex){
			ex.printStackTrace();
			return "fail";
		}
		return "tree";
	}
	
	public String finanTree()throws IOException{
		finaList=synchManager.getOrgsbyCode();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "finanTree";
	}

	public String userList()throws Exception{
		
		if(orgId!=null && orgId!=""){
			try{
			usersList=this.getUsersList();
	     }catch(Exception ex){
		   ex.printStackTrace();
		    return "fail";
	     }
		}
		HttpServletRequest req=this.getRequest();
		req.setAttribute("orgId",orgId);
		return "userList";
	}
	
	public String saveUser()throws Exception{
		
		if(orgId!=null && orgId!=""){
			usersList=this.getUsersList();
		}
		synchManager.setSynchronizeforUser(usersList,orgId);
		return renderHtml("<script>alert('用户同步成功！');" +
				"parent.organiseInfo.location='"
				+ getRequest().getContextPath()
				+ "/fileNameRedirectAction.action?toPage=/synchroni/blank.jsp'; </script>");
	}

	public List<FinaBaseOrg> getOrgList() {
		HttpSession req=this.getSession();
		String sessionId=req.getId();
		orgList = synchManager.parseOrgXml(sessionId, null);
		for (Iterator it = orgList.iterator(); it.hasNext();) {
			FinaBaseOrg fog = (FinaBaseOrg) it.next();
			String parentCode=fog.getParlorId();
				if(parentCode.equals("202001")||parentCode==null)
				{
					it.remove();
				}
	
			
		}
		return orgList;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}


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
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	public SynchroniManager getSynchManager() {
		return synchManager;
	}
	@Autowired
	public void setSynchManager(SynchroniManager synchManager) {
		this.synchManager = synchManager;
	}

	public TUumsBaseOrg getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOrgList(List<FinaBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public List<FinanceUser> getUsersList() {
		HttpSession req=this.getSession();
		String sessionId=req.getId();
		usersList=synchManager.parseFinaUserXml(sessionId, orgId);
		return usersList;
	}

	public void setUsersList(List<FinanceUser> usersList) {
		this.usersList = usersList;
	}

	public List<TUumsBaseOrg> getFinaList() {
		return finaList;
	}

	public void setFinaList(List<TUumsBaseOrg> finaList) {
		this.finaList = finaList;
	}

}
