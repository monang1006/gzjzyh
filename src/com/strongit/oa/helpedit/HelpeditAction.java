package com.strongit.oa.helpedit;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaHelp;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 帮助编辑action跳转类
 * 
 * @author liuxi
 * @version 5.0
 */

@SuppressWarnings({ "serial", "rawtypes" })
@ParentPackage("default")
@Results({ @Result(name = BaseActionSupport.RELOAD, value = "helpedit.action", type = ServletActionRedirectResult.class) })
public class HelpeditAction extends BaseActionSupport {

	private String helpId;

	private String helpDesc;

	private String helpTreeId;

	private ToaHelp model = new ToaHelp();

	private List<TUumsBasePrivil> helpList;

	@Autowired
	private HelpeditManager imanager;

	private List<TUumsBaseSystem> systemList;

	private String codeType;

	private String lID;

	private String url;

	private String ihelpTreeId;

	private String helpTreeName;

	public String getHelpTreeName() {
		return helpTreeName;
	}

	public void setHelpTreeName(String helpTreeName) {
		this.helpTreeName = helpTreeName;
	}

	public String getIhelpTreeId() {
		return ihelpTreeId;
	}

	public void setIhelpTreeId(String ihelpTreeId) {
		this.ihelpTreeId = ihelpTreeId;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if (helpTreeId != null && !("").equals(helpTreeId)) {
			helpId = imanager.getHelpIdByHelpTreeId(helpTreeId);
			if (("402882101eba1e2f011eba6482540001").equals(helpTreeId)){
				 List<TUumsBaseSystem> list=null;
				 list=imanager.getCurrentUserSystems(Const.IS_YES);
				helpTreeName=list.get(0).getSysName();
				
			}else{
			 helpTreeName = imanager.getPrivilInfoByPrivilId(helpTreeId).getPrivilName();
			}
		}
		if (helpId != null && !("").equals(helpId)) {
			model = imanager.getToaHelp(helpId);
			helpDesc = model.getHelpDesc();
		}
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		if (("402882101eba1e2f011eba6482540001").equals(helpTreeId)) {
			helpDesc = "<P><FONT style=\"FONT-FAMILY: 黑体; FONT-SIZE: 22pt\">OA帮助</FONT></P>";
			return "input";
		}
		if (helpDesc == null || ("").equals(helpDesc)) {
			if (helpTreeName != null && !("").equals(helpTreeName)) {
				helpDesc = "<P><FONT style=\"FONT-FAMILY: 黑体; FONT-SIZE: 22pt\">" + helpTreeName + "</FONT></P>"
						+ "<FONT color=#808080>当前页面还没有帮助信息，等待您的输入。</FONT>";
			}
		}
		return "input";
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return "list";
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub

		model.setHelpDesc(helpDesc);
		model.setHelptreeId(helpTreeId);
		// model.setHelpId(helpId);
		if (helpId != null && !"".equals(helpId)) {
			imanager.update(model);
		} else {
			imanager.save(model);
		}
		return "input";
	}

	@SuppressWarnings("unchecked")
	public String helptree() throws Exception {
		try {
			helpList = imanager.getPrivilInfosByUserLoginName();
			systemList = imanager.getCurrentUserSystems(Const.IS_YES);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
		return "helptree";
	}

	// 通过传过来的url获得当前页面的HelpTreeId并跳转到helpedit-info.jsp
	public String gethelpTreeId() throws Exception {
		if (ihelpTreeId != null) {
			helpTreeId = ihelpTreeId;
		} else {
			helpTreeId = imanager.findbyPrivilsUrl(url);
			// System.out.println("helpTreeId:" + helpTreeId);
			if (helpTreeId != null && !("").equals(helpTreeId)) {
				helpId = imanager.getHelpIdByHelpTreeId(helpTreeId);
			}
			if (helpId != null && !("").equals(helpId)) {
				model = imanager.getToaHelp(helpId);
			}
		}
		return "info";
	}

	@SuppressWarnings("unchecked")
	public String vhelptree() throws Exception {
		try {
			List privilInfosByUserLoginName = imanager.getPrivilInfosByUserLoginName();
			helpList = privilInfosByUserLoginName;
			List currentUserSystems = imanager.getCurrentUserSystems(Const.IS_YES);
			systemList = currentUserSystems;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
		return "vhelptree";
	}

	public String view() throws Exception {
		// TODO Auto-generated method stub
		if (helpTreeId != null && !("").equals(helpTreeId)) {
			helpId = imanager.getHelpIdByHelpTreeId(helpTreeId);
			if (!("402882101eba1e2f011eba6482540001").equals(helpTreeId)) {
				if(imanager.getPrivilInfoByPrivilId(helpTreeId) != null){
					helpTreeName = imanager.getPrivilInfoByPrivilId(helpTreeId).getPrivilName();
				}
			} else {
				helpTreeName = "";
				helpDesc = "<P><FONT style=\"FONT-FAMILY: 黑体; FONT-SIZE: 22pt\">欢迎访问帮助信息页面！</FONT></P>";
			}
		}else{
			helpTreeName = "";
		}
		if (helpId != null && !("").equals(helpId)) {
			model = imanager.getToaHelp(helpId);
			helpDesc = model.getHelpDesc();
		}
		if (helpDesc == null) {
			if(helpTreeName == null){
				helpDesc = "<font color=\"#808080\">对不起，</font></font><font color=\"#808080\">当前页面暂时没有对应的帮助信息。</font>";
			}else{
				helpDesc = "<font color=\"#808080\">对不起，</font><FONT style=\"FONT-FAMILY: 黑体; FONT-SIZE: 22pt\">"
					+ helpTreeName + "</font><font color=\"#808080\">页面暂时没有对应的帮助信息。</font>";
			}
		}
		// System.out.println("model.helpDesc:" +
		// helpDesc+"  model.helpId:"+model.getHelpId()
		// +"  model.helpTreeId:"+model.getHelptreeId());
		return "view";
	}

	public String add() throws Exception {
		// TODO Auto-generated method stub
		return "add";
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TUumsBaseSystem> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<TUumsBaseSystem> systemList) {
		this.systemList = systemList;
	}

	public void setPrivilList(List<TUumsBasePrivil> helpList) {
		this.helpList = helpList;
	}

	public List<TUumsBasePrivil> getPrivilList() {
		return helpList;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getLID() {
		return lID;
	}

	public void setLID(String lid) {
		lID = lid;
	}

	public String getHelpId() {
		return helpId;
	}

	public void setHelpId(String helpId) {
		this.helpId = helpId;
	}

	public String getHelpTreeId() {
		return helpTreeId;
	}

	public void setHelpTreeId(String helpTreeId) {
		this.helpTreeId = helpTreeId;
	}

	public String getHelpDesc() {
		return helpDesc;
	}

	public void setHelpDesc(String helpDesc) {
		this.helpDesc = helpDesc;
	}

	public void setModel(ToaHelp model) {
		this.model = model;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
