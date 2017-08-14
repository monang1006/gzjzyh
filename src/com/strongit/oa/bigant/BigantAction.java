package com.strongit.oa.bigant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.bo.ToaBigAntUser;
import com.strongit.oa.bo.ToaGroup;
import com.strongit.oa.bo.ToaView;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "bigant.action", type = ServletActionRedirectResult.class) })
public class BigantAction extends BaseActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7223924148918443194L;
	
	private AddressOrgManager manager;
	private IBigantService bigantManager;
	private String currentUserId;//获取当前用户ID
	
	private String orgId;
	private String Col_HsItemID;
	private String Col_HsItemType;
	public String getCol_HsItemID() {
		return Col_HsItemID;
	}
	public void setCol_HsItemID(String col_HsItemID) {
		Col_HsItemID = col_HsItemID;
	}

	public String getCol_HsItemType() {
		return Col_HsItemType;
	}

	public void setCol_HsItemType(String col_HsItemType) {
		Col_HsItemType = col_HsItemType;
	}

	@Autowired
	public void setManager(AddressOrgManager manager) {
		this.manager = manager;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
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
		bigantManager.deleteAll();
		List<Organization> lst = manager.getAllDeparments();
		List<ToaView> viewList = new ArrayList<ToaView>();
		List<Organization> newLst = new ArrayList<Organization>();//用于保存根节点的组织机构（如果父编码为0的就是跟节点）
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		String[] hasChild = new String[lst.size()];
		for(int j=0;j<lst.size();j++){
			Organization org = lst.get(j);
			String fatherCode = manager.findFatherCode(org.getOrgSyscode(), config_orgCode, null);
			if("0".equals(fatherCode) || j == 0){
				if(!newLst.contains(org)){
					ToaView model = new ToaView();
					model.setCol_Name(org.getOrgName());
					newLst.add(org);
					viewList.add(model);
					model = null;
				}
			}
		}
		for(int i=0;i<newLst.size();i++){
			Organization org = newLst.get(i);
			List<User> userList = manager.getUsersByOrgID(org.getOrgId());
			int count = userList.size();
			if(count>0 || isHasChild(org.getOrgId(),lst)){ 
				hasChild[i] = "1";//组织机构下有人员,或部门
			}else{
				hasChild[i] = "0";
			}
		}
		for(int i = 0;i < newLst.size() ; i++){
			ToaView model = new ToaView();
			Organization organ = newLst.get(i);
			model.setCol_Name(organ.getOrgName());
			String charge=hasChild[i];
			if("1".equals(charge))
			{//存在子节点
				if(bigantManager.saveView(model))
				{
					try {
						String orgId = newLst.get(i).getOrgId();
						List<User> userList = manager.getUsersByOrgID(orgId);//需要获取此部门下的所有人员
						Organization org = manager.getOrgById(orgId);//需要获取此部门下的所有子部门
						String config_orgCode1 = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
						String orgSysCode = org.getOrgSyscode();
						List<Organization> lst1 = manager.getAllDeparments();
						//将人员挂接到此树下
						for(int j=0;j<userList.size();j++){
							ToaBigAntUser bigantUser = new ToaBigAntUser();
							User user = userList.get(j);
							bigantUser.setCol_LoginName(user.getUserLoginname());
							bigantUser.setCol_Name(user.getUserName());
							bigantUser.setCol_PWord(user.getUserPassword());
							bigantUser.setCol_IsSuper(user.getUserIsSupManager());
							bigantUser.setCol_h_Address(user.getUserAddr());
						    bigantUser.setCol_EMail(user.getUserEmail());
						    bigantUser.setCol_Description(user.getUserDescription());
						    bigantUser.setCol_Mobile(user.getUserTel());
							bigantUser.setCol_DeptInfo(model.getCol_Name());
							bigantManager.saveUser(bigantUser);
							bigantManager.saveViewAndUser(model, bigantUser);
							bigantUser = null;
						}
						for(int i1=0;i1<lst1.size();i1++){
							Organization thisOrg = lst1.get(i1);
							String fatherCode = manager.findFatherCode(thisOrg.getOrgSyscode(), config_orgCode1, null);
							if(orgSysCode.equals(fatherCode)){//如果当前部门是他的子部门
								//如果当前部门下无人员
								List<User> userLst = manager.getUsersByOrgID(thisOrg.getOrgId());//需要获取此部门下的所有人员
								if(userLst.size()==0 && !isHasChild(thisOrg.getOrgId(),lst1)){ //无子节点
									ToaGroup group = new ToaGroup();
									group.setCol_Name(thisOrg.getOrgName());
									if(bigantManager.saveGroup(group)){
										bigantManager.saveViewAndGroup(model, group);
										group = null;
									}
										
								}else{//此部门下还存在人员或子部门
									ToaGroup group = new ToaGroup();
									group.setCol_Name(thisOrg.getOrgName());
									if(bigantManager.saveGroup(group)){
										bigantManager.saveViewAndGroup(model, group);
										saveChildNote(thisOrg.getOrgId(),group);
										group = null;
									}
								}
							}
						}
					} catch (Exception e) {
						LogPrintStackUtil.printErrorStack(logger, e);
						renderText(LogPrintStackUtil.errorMessage);
					}
				}
			}else{
				bigantManager.saveView(model);
			}
		}	
		return this.renderText("true");
	}
	private void saveChildNote(String id,ToaGroup model)throws Exception{
		try {
			List<User> userList = manager.getUsersByOrgID(id);//需要获取此部门下的所有人员
			Organization org = manager.getOrgById(id);//需要获取此部门下的所有子部门
			String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
			String orgSysCode = org.getOrgSyscode();
			List<Organization> lst = manager.getAllDeparments();
			//将人员挂接到此树下
			for(int j=0;j<userList.size();j++){
				ToaBigAntUser bigantUser = new ToaBigAntUser();
				User user = userList.get(j);
				bigantUser.setCol_LoginName(user.getUserLoginname());
				bigantUser.setCol_Name(user.getUserName());
				bigantUser.setCol_PWord(user.getUserPassword());
				bigantUser.setCol_IsSuper(user.getUserIsSupManager());
				bigantUser.setCol_h_Address(user.getUserAddr());
			    bigantUser.setCol_EMail(user.getUserEmail());
			    bigantUser.setCol_Description(user.getUserDescription());
			    bigantUser.setCol_Mobile(user.getUserTel());
				bigantUser.setCol_DeptInfo(model.getCol_Name());
				bigantManager.saveUser(bigantUser);
				bigantManager.saveGroupAndUser(model, bigantUser);
				bigantUser = null;
			}
			
			for(int i=0;i<lst.size();i++){
				Organization thisOrg = lst.get(i);
				String fatherCode = manager.findFatherCode(thisOrg.getOrgSyscode(), config_orgCode, null);
				if(orgSysCode.equals(fatherCode)){//如果当前部门是他的子部门
					//如果当前部门下无人员
					List<User> userLst = manager.getUsersByOrgID(thisOrg.getOrgId());//需要获取此部门下的所有人员
					if(userLst.size()==0 && !isHasChild(thisOrg.getOrgId(),lst)){ //无子节点
						ToaGroup group = new ToaGroup();
						group.setCol_Name(thisOrg.getOrgName());
						if(bigantManager.saveGroup(group)){
							bigantManager.saveGroupAndGroup(model, group);
							group = null;
						}
					}else{//此部门下还存在人员或子部门
						ToaGroup group = new ToaGroup();
						group.setCol_Name(thisOrg.getOrgName());
						if(bigantManager.saveGroup(group)){
							bigantManager.saveGroupAndGroup(model, group);
							saveChildNote(thisOrg.getOrgId(),group);
							group = null;
						}
					}
				}
			}
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
	}
	public String systree()throws Exception{
		try {
			currentUserId = manager.getCurrentUser().getUserId();
			List<Organization> lst = manager.getAllDeparments();
			List<Organization> newLst = new ArrayList<Organization>();//用于保存根节点的组织机构（如果父编码为0的就是跟节点）
			String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
			String[] hasChild = new String[lst.size()];
			for(int j=0;j<lst.size();j++){
				Organization org = lst.get(j);
				String fatherCode = manager.findFatherCode(org.getOrgSyscode(), config_orgCode, null);
				if("0".equals(fatherCode) || j == 0){
					if(!newLst.contains(org)){
						newLst.add(org);					
					}
				}
			}
			for(int i=0;i<newLst.size();i++){
				Organization org = newLst.get(i);
				List<User> userList = manager.getUsersByOrgID(org.getOrgId());
				int count = userList.size();
				if(count>0 || isHasChild(org.getOrgId(),lst)){ 
					hasChild[i] = "1";//组织机构下有人员,或部门
				}else{
					hasChild[i] = "0";
				}
			}
			getRequest().setAttribute("orgList", newLst);
			getRequest().setAttribute("hasChild", hasChild);
			return "systree";
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "systree";
		}
	}
	/**
	 * author:dengzc
	 * description:判断组织机构下是否还有部门
	 * modifyer:
	 * description:
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	private boolean isHasChild(String orgId,List<Organization> lst)throws Exception{
		Organization org = manager.getOrgById(orgId);//需要获取此部门下的所有子部门
		String orgSysCode = org.getOrgSyscode();
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		for(int i=0;i<lst.size();i++){
			Organization thisOrg = lst.get(i);
			String fatherCode = manager.findFatherCode(thisOrg.getOrgSyscode(), config_orgCode, null);
			if(orgSysCode.equals(fatherCode)){//如果当前部门是他的子部门
				return true;
			}
		}
		return false;
	}
	
	/**
	 * author:dengzc
	 * description:异步加载系统通讯录下的人员
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String synajaxtree()throws Exception{
		try {
			StringBuffer str=new StringBuffer("");
			List<User> userList = manager.getUsersByOrgID(orgId);//需要获取此部门下的所有人员
			Organization org = manager.getOrgById(orgId);//需要获取此部门下的所有子部门
			String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
			String orgSysCode = org.getOrgSyscode();
			List<Organization> lst = manager.getAllDeparments();
			//将人员挂接到此树下
			for(int j=0;j<userList.size();j++){
				User user = userList.get(j);
				str.append("<li id="+user.getUserId()+"><span>"+user.getUserName()+"</span><label style='display:none;'>person</label></li>\n");			
			}
			
			for(int i=0;i<lst.size();i++){
				Organization thisOrg = lst.get(i);
				String fatherCode = manager.findFatherCode(thisOrg.getOrgSyscode(), config_orgCode, null);
				if(orgSysCode.equals(fatherCode)){//如果当前部门是他的子部门
					//如果当前部门下无人员
					List<User> userLst = manager.getUsersByOrgID(thisOrg.getOrgId());//需要获取此部门下的所有人员
					if(userLst.size()==0 && !isHasChild(thisOrg.getOrgId(),lst)){ //无子节点
						str.append("<li id="+thisOrg.getOrgId()+" leafChange='folder-org-leaf'><span>"+thisOrg.getOrgName()+"</span><label style='display:none;'>org</label></li>\n");
					}else{//此部门下还存在人员或子部门
						str.append("<li id="+thisOrg.getOrgId()+" ><span>"+thisOrg.getOrgName()+"</span><label style='display:none;'>org</label>\n");
						str.append("<ul class=ajax>\n");
						str.append("<li id="+thisOrg.getOrgId()+i+">{url:"+getRequest().getContextPath()+"/address/addressOrg!synajaxtree.action?orgId="+thisOrg.getOrgId()+"}</li>\n");
						str.append("</ul>\n");
						str.append("</li>\n");					
					}
				}
			}
			renderHtml(str.toString());
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}
	public String bigantTree()throws Exception{
		try {
			List<ToaView> lst = bigantManager.getAllDeparments();
			String[] hasChild = new String[lst.size()];
			for(int i = 0; i < lst.size(); i++)
			{
				ToaView model = lst.get(i);
				if(bigantManager.isHasChild(model.getCol_ID(), 4)){
					hasChild[i] = "1";
				}else{
					hasChild[i] = "0";
				}
			}
			getRequest().setAttribute("orgList", lst);
			getRequest().setAttribute("hasChild", hasChild);
			return "biganttree";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "biganttree";
	}
	public String bigantAjaxTree()throws Exception{
		StringBuffer str = new StringBuffer();
		List<ToaBigAntUser> userList = bigantManager.getUsersByOrgID(Integer.parseInt(Col_HsItemID), Integer.parseInt(Col_HsItemType)); //需要获取此部门下的所有人员
		List<ToaGroup> groupList = bigantManager.getGroupById(Integer.parseInt(Col_HsItemID), Integer.parseInt(Col_HsItemType));			//需要获取此部门下的所有子部门
		//将人员挂接到此树下
		for(int j=0;j<userList.size();j++){
			ToaBigAntUser user = userList.get(j);
			str.append("<li id="+user.getCol_ID()+"><span>"+user.getCol_LoginName()+"</span><label style='display:none;'>person</label></li>\n");			
		}
		for(int i = 0; i <	groupList.size(); i++)
		{
			ToaGroup group = groupList.get(i);
			List<ToaBigAntUser> lst = bigantManager.getUsersByOrgID(group.getCol_ID(), 2); //需要获取此部门下的所有人员
			if(!bigantManager.isHasChild(group.getCol_ID(), 2) && lst.size() == 0)
			{
				str.append("<li id="+group.getCol_ID()+""+new Date().getTime()+" leafChange='folder-org-leaf'><span>"+group.getCol_Name()+"</span><label style='display:none;'>org</label></li>\n");
			}else{
				//此部门下还存在人员或子部门
				str.append("<li id="+group.getCol_ID()+""+new Date().getTime()+" ><span>"+group.getCol_Name()+"</span><label style='display:none;'>org</label>\n");
				str.append("<ul class=ajax>\n");
				str.append("<li id="+group.getCol_ID()+""+new Date().getTime()+">{url:"+getRequest().getContextPath()+"/bigant/bigant!bigantAjaxTree.action?Col_HsItemID="+group.getCol_ID()+"&Col_HsItemType="+2+"}</li>\n");
				str.append("</ul>\n");
				str.append("</li>\n");	
			}
		}
		System.out.println(str);
		renderHtml(str.toString());
		return null;
	}
	public Object getModel() {
		return null;
	}

	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Autowired
	public void setBigantManager(IBigantService bigantManager) {
		this.bigantManager = bigantManager;
	}

}
