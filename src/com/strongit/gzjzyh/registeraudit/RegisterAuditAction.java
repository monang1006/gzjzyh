package com.strongit.gzjzyh.registeraudit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.strongit.gzjzyh.policeregister.IPoliceRegisterManager;
import com.strongit.oa.bo.ToaAffiche;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.util.ListUtils;
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
@Results({ @Result(name = BaseActionSupport.RELOAD, value = "registerAudit.action", type = ServletActionRedirectResult.class) })
public class RegisterAuditAction extends BaseActionSupport<TGzjzyhUserExtension> {

	private Page<TGzjzyhUserExtension> page = new Page<TGzjzyhUserExtension>(
			FlexTableTag.MIDDLE_ROWS, true);
	
	private static final String DEFAULT_UPLOAD_IMAGE = "/images/upload/defaultUpload.jpg";

	private String ueId;
	private String userOrgName;
	private String searchLoginName;
	private String searchName;
	private Date appStartDate;
	private Date appEndDate;
	
	private String auditStatus;
	
	private String ueMainNo1Tmp;
	private String ueMainNo2Tmp;
	private String ueMainId1Tmp;
	private String ueMainId2Tmp;
	private String ueHelpNo1Tmp;
	private String ueHelpNo2Tmp;
	private String ueHelpId1Tmp;
	private String ueHelpId2Tmp;
	
	private String isDesktop;
	private String blockId;

	private Map statusMap = new HashMap();

	private TGzjzyhUserExtension model = new TGzjzyhUserExtension();

	@Autowired
	private MyLogManager myLogManager;
	
	@Autowired
	private MyInfoManager myInfoManager;

	@Autowired
	IUserService userService;
	
	@Autowired
	DesktopSectionManager sectionManager;
	
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
	
	public RegisterAuditAction() {
		statusMap.put(GzjzyhConstants.STATUS_AUDIT_BACK, "已退回");
		statusMap.put(GzjzyhConstants.STATUS_AUDIT_PASS, "已审核");
		statusMap.put(GzjzyhConstants.STATUS_WAIT_AUDIT, "待审核");
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
		
		if(GzjzyhConstants.STATUS_WAIT_AUDIT.equals(this.model.getUeStatus())){
			return INPUT;
		}else{
			return "view";
		}
	}

	@Override
	public String list() throws Exception {
		page = this.registerManager.queryApplyPage(page, searchLoginName, searchName, GzjzyhConstants.STATUS_WAIT_AUDIT, appStartDate, appEndDate);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		try {
			if(this.ueId != null && !"".equals(this.ueId)){
				model = this.registerManager.getUserExtensionById(this.ueId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String save() throws Exception {
		User currentUser = this.userService.getCurrentUser();
		model.setUeStatus(this.auditStatus);
		model.setUeAuditUser(currentUser.getUserName());
		model.setUeAuditUserId(currentUser.getUserId());
		this.registerManager.audit(model);
		
		addActionMessage("保存成功");

		// 添加日志信息
		String ip = getRequest().getRemoteAddr();
		String logInfo = "审核了账号" + model.getTuumsBaseUser().getUserName();

		this.myLogManager.addLog(logInfo, ip);

		return "winclose";
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
	
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		if(sectionFontSize == null || "".equals("sectionFontSize") || "null".equals("sectionFontSize") ){
			sectionFontSize = "14";
		}
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		//链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/registeraudit/registerAudit.action")
			.append("', '账号审核'")
			.append(");");
		
		List<TGzjzyhUserExtension> list = null;
		Page<TGzjzyhUserExtension> desktopPage = new Page<TGzjzyhUserExtension>(num, false);
		desktopPage = this.registerManager.queryApplyPage(desktopPage, null, null, GzjzyhConstants.STATUS_WAIT_AUDIT, null, null);
		list = desktopPage.getResult();
		
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				TGzjzyhUserExtension notify = list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:window.showModalDialog('")
					.append(getRequest().getContextPath())
					.append("/registeraudit/registerAudit!input.action?ueId=").append(notify.getUeId())
					.append("&blockId=").append(blockid).append("&isDesktop=1").append("'")
					.append(",window,'help:no;status:no;scroll:no;dialogWidth:1500px; dialogHeight:800px'")
					.append(")");
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = notify.getTuumsBaseUser().getUserLoginname() + "（" + notify.getTuumsBaseUser().getUserName() + "）";
				if(title==null){
					title="";
				}
				
				StringBuilder tip = new StringBuilder();
				tip.append(title);
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
							.append("<span style=\"font-size: "+sectionFontSize+"px;\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				if("1".equals(showCreator)){//如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"115px\">");
					if(notify.getName() == null){
						innerHtml.append("");
					}else{
						innerHtml.append("<span class =\"linkgray\">").append(notify.getTuumsBaseUser().getUserName()).append("</span>&nbsp;&nbsp;&nbsp;");
					}
					innerHtml.append("</td>");
				}
				if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span class =\"linkgray10\">").append(st.format(notify.getUeDate())).append("</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		if(list==null){
			for (int i = 0; i < num; i++) { // 获取在条数范围内的列表
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml.append("	&nbsp;");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("	</table>");
				}
		}
		if(list!=null&&list.size()<num){
			for (int i = 0; i < num-list.size() ; i++) { // 获取在条数范围内的列表
			innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
			innerHtml.append("<tr>");
			innerHtml.append("<td>");
			innerHtml.append("	&nbsp;");
			innerHtml.append("</td>");
			innerHtml.append("</tr>");
			innerHtml.append("	</table>");
			}
		}
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
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

	public Map getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map statusMap) {
		this.statusMap = statusMap;
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

	public String getSearchLoginName() {
		return searchLoginName;
	}

	public void setSearchLoginName(String searchLoginName) {
		this.searchLoginName = searchLoginName;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public Date getAppStartDate() {
		return appStartDate;
	}

	public void setAppStartDate(Date appStartDate) {
		this.appStartDate = appStartDate;
	}

	public Date getAppEndDate() {
		return appEndDate;
	}

	public void setAppEndDate(Date appEndDate) {
		this.appEndDate = appEndDate;
	}

	public String getUeId() {
		return ueId;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}

	public String getIsDesktop() {
		return isDesktop;
	}

	public void setIsDesktop(String isDesktop) {
		this.isDesktop = isDesktop;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}
}