package com.strongit.gzjzyh.policeregister;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.ApplicationConfig;
import com.strongit.uums.security.UserInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results({ @Result(name = BaseActionSupport.RELOAD, value = "policeRegister.action", type = ServletActionRedirectResult.class) })
public class PoliceRegisterAction extends BaseActionSupport<TGzjzyhUserExtension> {

	private Page<TGzjzyhUserExtension> page = new Page<TGzjzyhUserExtension>(
			FlexTableTag.MIDDLE_ROWS, true);
	
	private static final String DEFAULT_UPLOAD_IMAGE = "/images/upload/defaultUpload.jpg";

	private String ueId;
	private String userOrgName;
	
	private String ueMainNo1Tmp;
	private String ueMainNo2Tmp;
	private String ueMainId1Tmp;
	private String ueMainId2Tmp;
	private String ueHelpNo1Tmp;
	private String ueHelpNo2Tmp;
	private String ueHelpId1Tmp;
	private String ueHelpId2Tmp;

	// 用户是否启用
	private Map userActiveTypeMap = new HashMap();

	private TGzjzyhUserExtension model = new TGzjzyhUserExtension();

	@Autowired
	private MyLogManager myLogManager;
	
	@Autowired
	private MyInfoManager myInfoManager;

	@Autowired
	IUserService userService;
	
	@Autowired
	IPoliceRegisterManager registerManager;

	// 全局设置信息
	@Autowired
	ApplicationConfig applicationConfig;

	// 密码是否要md5加密
	private String md5Enable;

	public String getMd5Enable() {
		return md5Enable;
	}

	public void setMd5Enable(String md5Enable) {
		this.md5Enable = md5Enable;
	}

	/**
	 * 把字符串转成utf8编码，保证中文文件名不会乱码
	 * 
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		// prepareModel();
		// 获取md5加密设置
		if (applicationConfig.isMd5Enable()) {
			this.md5Enable = "1";
		} else {
			this.md5Enable = "0";
		}

		String isSupman = "0";
		this.getRequest().setAttribute("isSupman", isSupman);
		
		if(this.model.getTuumsBaseUser().getOrgId() != null && !"".equals(this.model.getTuumsBaseUser().getOrgId())) {
			TUumsBaseOrg org = this.userService.getOrgInfoByOrgId(this.model.getTuumsBaseUser().getOrgId());
			this.userOrgName = org.getOrgName();
		}
		
		if(this.model.getUeMainId1() == null || "".equals(this.model.getUeMainId1())) {
			this.ueMainId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueMainId1Tmp = this.model.getUeMainId1();
		}
		if(this.model.getUeMainId2() == null || "".equals(this.model.getUeMainId2())) {
			this.ueMainId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueMainId2Tmp = this.model.getUeMainId2();
		}
		if(this.model.getUeMainNo1() == null || "".equals(this.model.getUeMainNo1())) {
			this.ueMainNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueMainNo1Tmp = this.model.getUeMainNo1();
		}
		if(this.model.getUeMainNo2() == null || "".equals(this.model.getUeMainNo2())) {
			this.ueMainNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueMainNo2Tmp = this.model.getUeMainNo2();
		}
		
		if(this.model.getUeHelpId1() == null || "".equals(this.model.getUeHelpId1())) {
			this.ueHelpId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueHelpId1Tmp = this.model.getUeHelpId1();
		}
		if(this.model.getUeHelpId2() == null || "".equals(this.model.getUeHelpId2())) {
			this.ueHelpId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueHelpId2Tmp = this.model.getUeHelpId2();
		}
		if(this.model.getUeHelpNo1() == null || "".equals(this.model.getUeHelpNo1())) {
			this.ueHelpNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueHelpNo1Tmp = this.model.getUeHelpNo1();
		}
		if(this.model.getUeHelpNo2() == null || "".equals(this.model.getUeHelpNo2())) {
			this.ueHelpNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		}else {
			this.ueHelpNo2Tmp = this.model.getUeHelpNo2();
		}
		
		//已审核的信息不允许再修改
		if(GzjzyhConstants.STATUS_AUDIT_PASS.equals(this.model.getUeStatus())) {
			return "view";
		}else {
			return INPUT;
		}
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		try {
			User currentUser = this.userService.getCurrentUser();
			model = this.registerManager.getUserExtensionByUserId(currentUser.getUserId());
			if(model == null){
				model = new TGzjzyhUserExtension();
				TUumsBaseUser user = new TUumsBaseUser();
				model.setTuumsBaseUser(user);
				user.setUserSequence(userService.getNextUserSequenceCode());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String save() throws Exception {
		// 添加用户还是编辑用户的标志
		String flag = "edit";
		if ("".equals(model.getUeId()) || model.getUeId() == null) {
			flag = "add";
			model.setUeId(null);
		}
		this.registerManager.save(model);
		
		addActionMessage("保存成功");

		// 添加日志信息
		String ip = getRequest().getRemoteAddr();
		String logInfo = "";
		if (flag.equals("add")) {
			logInfo = "添加了账号" + model.getTuumsBaseUser().getUserName();
		} else {
			logInfo = "编辑了账号" + model.getTuumsBaseUser().getUserName();
		}

		this.myLogManager.addLog(logInfo, ip);
		
		this.renderHtml("<script>alert('保存成功。')</script>");

		return null;
	}
	
	public String orgMoreTree() throws Exception {
		List<TUumsBaseOrg> orgList = userService.queryOrgs(Const.IS_NO);
		
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		for(int i=0;i<orgList.size();i++){
			String parentId = orgList.get(i).getOrgParentId();
			TreeNode node = new TreeNode();
			if(parentId==null || "".equals(parentId)){
				node.setId(orgList.get(i).getOrgId());
				node.setPid("-1");
				node.setIsexpand(true);
				node.setShowcheck(true);
				node.setComplete(true);
				node.setText(orgList.get(i).getOrgName());
				node.setValue("system");
				nodeLst.add(node);
			}else{
				node.setId(orgList.get(i).getOrgId());
				node.setPid(parentId);
				node.setIsexpand(true);
				node.setShowcheck(true);
				node.setComplete(true);
				node.setText(orgList.get(i).getOrgName());
				node.setValue("privil");
				nodeLst.add(node);
			}
		}
		String treeNodes = "";
		//构造树的json数据
		treeNodes = JsonUtil.fromTreeNodes(nodeLst);//JsonMapper.buildNormalBinder().toJson(nodeLst);
//		this.getResponse().setContentType("application/json");
//		this.getResponse().getWriter().write(treeNodes);
		getRequest().setAttribute("treeNodes", treeNodes);
		
		return "checkmore";
	}
	
	public String imageUpload() throws Exception {
		return "upload";
	}

	public TGzjzyhUserExtension getModel() {
		return model;
	}

	public void setUeId(String ueId) {
		this.ueId = ueId;
	}

	public MyLogManager getMyLogManager() {
		return myLogManager;
	}

	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

	public Page<TGzjzyhUserExtension> getPage() {
		return page;
	}

	public Map getUserActiveTypeMap() {
		return userActiveTypeMap;
	}

	public void setUserActiveTypeMap(Map userActiveTypeMap) {
		this.userActiveTypeMap = userActiveTypeMap;
	}

	public String getUeMainNo1Tmp() {
		return ueMainNo1Tmp;
	}

	public void setUeMainNo1Tmp(String ueMainNo1Tmp) {
		this.ueMainNo1Tmp = ueMainNo1Tmp;
	}

	public String getUeMainNo2Tmp() {
		return ueMainNo2Tmp;
	}

	public void setUeMainNo2Tmp(String ueMainNo2Tmp) {
		this.ueMainNo2Tmp = ueMainNo2Tmp;
	}

	public String getUeMainId1Tmp() {
		return ueMainId1Tmp;
	}

	public void setUeMainId1Tmp(String ueMainId1Tmp) {
		this.ueMainId1Tmp = ueMainId1Tmp;
	}

	public String getUeMainId2Tmp() {
		return ueMainId2Tmp;
	}

	public void setUeMainId2Tmp(String ueMainId2Tmp) {
		this.ueMainId2Tmp = ueMainId2Tmp;
	}

	public String getUeHelpNo1Tmp() {
		return ueHelpNo1Tmp;
	}

	public void setUeHelpNo1Tmp(String ueHelpNo1Tmp) {
		this.ueHelpNo1Tmp = ueHelpNo1Tmp;
	}

	public String getUeHelpNo2Tmp() {
		return ueHelpNo2Tmp;
	}

	public void setUeHelpNo2Tmp(String ueHelpNo2Tmp) {
		this.ueHelpNo2Tmp = ueHelpNo2Tmp;
	}

	public String getUeHelpId1Tmp() {
		return ueHelpId1Tmp;
	}

	public void setUeHelpId1Tmp(String ueHelpId1Tmp) {
		this.ueHelpId1Tmp = ueHelpId1Tmp;
	}

	public String getUeHelpId2Tmp() {
		return ueHelpId2Tmp;
	}

	public void setUeHelpId2Tmp(String ueHelpId2Tmp) {
		this.ueHelpId2Tmp = ueHelpId2Tmp;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}

	public String getUeId() {
		return ueId;
	}
}