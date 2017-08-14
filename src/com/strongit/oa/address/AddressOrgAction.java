package com.strongit.oa.address;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.address.util.ComparatorOrgUser;
import com.strongit.oa.address.util.UserBean;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.model.UserGroup;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseGroup;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      组织机构Action
 */
@ParentPackage("default")
public class AddressOrgAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2269536603864795631L;
	private AddressOrgManager manager;
	private String orgId;
	private String groupId;//组Id
	

	private String orgName;
	private String userName;//根据用户姓名搜索
	private String userId;	//根据用户ID查看用户详细信息
	private UserBean model = new UserBean();
	private  String typewei;
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getTypewei() {
		return typewei;
	}

	public void setTypewei(String typewei) {
		this.typewei = typewei;
	}

	private Page<TUumsBaseUser> page = new Page<TUumsBaseUser>(FlexTableTag.MAX_ROWS,true);
	
	private String chooseType;//按部门、岗位、用户组类型展示人员
	
	private final static String DEPARTMENT = "bm";//部门
	private final static String POST = "gw";//岗位
	private final static String GROUP = "yhz";//用户组
	
	
	private String currentUserId;//获取当前用户ID
	
	private String type;//标示组织机构树的展示性质【导入到个人通讯录...】
	
	@Autowired IUserService userService;
	
	///////////////////////////////////
	private int usershow;     //显示所有页面标题为用户列表
	

	public int getUsershow() {
		return usershow;
	}

	public void setUsershow(int usershow) {
		this.usershow = usershow;
	}

	/////////////////////////////////
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	/**
	 * 获取组织机构列表,以树形式展现
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		List<Organization> lst = userService.getAllDeparments();
		List newList = new ArrayList();
		List<Organization> newList2 = new ArrayList<Organization>(lst);
		Collections.sort(newList2, new Comparator<Organization>(){

			public int compare(Organization o1, Organization o2) {
				String code1 = o1.getOrgSyscode();
				String code2 = o2.getOrgSyscode();
				if(code1 != null && code2 != null) {
					return code1.length() - code2.length();
				}
				return 0;
			}
			
		});
		String root = null;
		if(!newList2.isEmpty()) {
			root = newList2.get(0).getOrgSyscode();
		}
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		for(int i=0;i<lst.size();i++){
			TempPo po = new TempPo();
			Organization org = lst.get(i); 
			String orgSysCode = org.getOrgSyscode();
			po.setId(orgSysCode);
			po.setCodeId(org.getOrgId());
			String parentId = userService.findFatherCode(orgSysCode, config_orgCode, null);
			/*if(i == 0){//机构列表根据编码排序，第一个一定是根节点
				if(!"0".equals(parentId)){
					parentId = "0";
				}
			}*/
			if(org.getOrgSyscode().equals(root)) {
				if(!"0".equals(parentId)){
					parentId = "0";
				}
			}
			po.setParentId(parentId);
			po.setName(org.getOrgName());
			po.setType(type);//这里暂时用于表示树用于导入系统人员
			newList.add(po);
			
		}
		getRequest().setAttribute("orgList", newList);
		
		return "tree";
	}

	/**
	 * author:dengzc
	 * description:选择接收人
	 * modifyer:邓志城
	 * description:转到选择人员页面，增加了按岗位、用户组选择人员功能。
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String tree()throws Exception{
//		return "chooseperson";
		return "selectperson";
	}

	/**
	 * author:dengzc
	 * description:按类型展示人员
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String choosetree()throws Exception{
		List<TempPo> lst = new ArrayList<TempPo>();
		if(DEPARTMENT.equals(chooseType)){//按部门搜索
			lst = manager.getUserTreeByDept();
		}else if(POST.equals(chooseType)){//按岗位搜索
			lst = manager.getUserTreeByPost();
		}else if(GROUP.equals(chooseType)){//按组搜索
			lst = manager.getUserTreeByUserGroup();
		}
		getRequest().setAttribute("orgList",lst);
		return "choosetree";
	}

	/**
	 * 人员选择树形机构显示
	 * @author:邓志城
	 * @date:2011-3-10 上午11:23:30
	 * @return
	 */
	public String chooseForType() {
		List<TempPo> lst = new ArrayList<TempPo>();
		try{
			if("0".equals(chooseType)) {//本部门人员
				lst = manager.CurrentUserDeptTree();
			}else if("1".equals(chooseType)) {//本机构人员
				lst = manager.CurrentUserOrgTree();
			}else if("2".equals(chooseType)) {//全部人员
				lst = manager.getUserTreeByDept();
			}else if("3".equals(chooseType)) {//岗位人员
				lst = manager.getUserTreeByPost();
			}else if("4".equals(chooseType)) {//用户组人员
				lst = manager.getUserTreeByUserGroup();
			}else if("5".equals(chooseType)) {//选择机构
				List<Organization> orgList = userService.getAllDeparments();
				if(orgList != null && !orgList.isEmpty()) {
					for(Organization organization : orgList) {
						TempPo po = new TempPo();
						po.setType("true");
						po.setId(organization.getOrgId());
						po.setName(organization.getOrgName());
						if(organization.getOrgParentId() == null || "".equals(organization.getOrgParentId())) {
							po.setParentId("0");
						}else{
							po.setParentId(organization.getOrgParentId());
						}
						lst.add(po);
					}
				}
				getRequest().setAttribute("root", "组织机构");
				getRequest().setAttribute("orgList",lst);
				return "choosedictorg";
			}else{
				lst = manager.getUserTreeByDept();
			}
		}catch(Exception e) {
			logger.error("查找人员出现错误", e);
		}
		getRequest().setAttribute("orgList",lst);
		return "choosetype";
	}
	
	/**
	 * author:dengzc
	 * description:获取系统用户，用同步用户到IM软件
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String systemUserTree()throws Exception{
		return "synuser";
	}
	
	/**
	 * author:dengzc
	 * description:获取组织机构树
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String systree()throws Exception{
		currentUserId = manager.getCurrentUser().getUserId();
		List<Organization> lst = manager.getAllDeparments();
		List<Organization> newLst = new ArrayList<Organization>();//用于保存根节点的组织机构（如果父编码为0的就是跟节点）
		//String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		String[] hasChild = new String[lst.size()];
		for(int j=0;j<lst.size();j++){
			Organization org = lst.get(j);
			if(org.getOrgParentId() == null || org.getOrgParentId().length() == 0) {
				newLst.add(org);
			}
		}
		for(int i=0;i<newLst.size();i++){
			Organization org = newLst.get(i);
			List<User> userList = manager.getUsersByOrgID(org.getOrgId());
			int count = userList.size();
			if(count>0 || isHasChild(org.getOrgId())){ 
				hasChild[i] = "1";//组织机构下有人员,或部门
			}else{
				hasChild[i] = "0";
			}
		}
		getRequest().setAttribute("orgList", newLst);
		getRequest().setAttribute("hasChild", hasChild);
		return "systree";
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
	private boolean isHasChild(String orgId)throws Exception{
		Organization org = manager.getOrgById(orgId);//需要获取此部门下的所有子部门
		String orgSysCode = org.getOrgSyscode();
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		List<Organization> lst = manager.getAllDeparments();
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
					if(userLst.size()==0 && !isHasChild(thisOrg.getOrgId())){ //无子节点
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
	
	/**
	 * author:dengzc
	 * description:获取组织机构下的人员列表
	 * modifyer:liuxi
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String orguserlist()throws Exception{
		currentUserId = manager.getCurrentUser().getUserId();
		if(orgId==null || "".equals(orgId)){
		  page = manager.getUsersByOrgId(page, orgId, userName,"");
		}else if(userService.getOrgInfoByOrgId(orgId).getOrgCode()!=null&&"001".equals(userService.getOrgInfoByOrgId(orgId).getOrgCode())){
			Organization org = manager.getOrgById(orgId);//需要获取此部门下的所有子部门
			String parentId = org.getOrgParentId();
			page = manager.getUsersByOrgId(page,"",userName,parentId);
//			page = userService.queryUsersByHa(page, userName, selectloginname, selectorg, isdel, isActive, haOrgId)
		}else{
//			String sysCode=userService.getOrgInfoByOrgId(orgId).getOrgCode();
//			page=userService.getUsersOfOrgAndChildByOrgcodeForPageByHa(page,sysCode,"","0",orgId);
			page = userService.queryOrgUsers(page, orgId, userName, null, Const.IS_NO, Const.IS_YES);
//			page = userService.queryOrgUsersByHa(page, orgId, userService.getCurrentUserInfo().getOrgId());
		}
		if(page.getTotalCount()==-1){
			page.setTotalCount(0);
		}
		return "userlist";
	}

	/**
	 * author:dengzc
	 * description:显示对应组织机构下的人员列表
	 * modifyer:
	 * description:
	 * @return
	 */
	public String choosePersonByOrgId()throws Exception{
		try {
			StringBuffer sb = new StringBuffer("");
			if(orgId == null){
				List<Organization> list = manager.getAllDeparments();
				if(list.size()>0){
					orgId = list.get(0).getOrgId();
				}else{
					return null;
				}
			}
			List<User> lstUser = manager.getUsersByOrgID(orgId);
			for(Iterator<User> it=lstUser.iterator();it.hasNext();){
				User user = it.next();
				sb.append("<tr class=\"tr1\">");
				sb.append("<td class=\"td1\" style=\"text-indent: 0px;border-left:2px  solid;border-top:2px solid;border-bottom:2px  solid;\" align=\"center\">");
				sb.append(user.getUserName());
				sb.append("</td>");
				sb.append("<td class=\"td1\" style=\"text-indent: 0px;border-right:2px  solid;border-top:2px solid;border-bottom:2px  solid;\" align=\"center\">");
				sb.append(user.getUserEmail()==null?"":user.getUserEmail());
				sb.append("</td>");
				sb.append("</tr>");
			}
			renderHtml(sb.toString());
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * author:dengzc
	 * description:搜索个人通讯录和系统通讯录
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String searchUser()throws Exception{
		String searchType = getRequest().getParameter("searchType");
		String userName = URLDecoder.decode(getRequest().getParameter("userName"), "utf-8"); 
		List<User> lstUser = manager.getAllUsers(userName,searchType);
		StringBuffer sb = new StringBuffer("<table class=\"table1\" width=\"190\" border=\"0\" cellspacing=\"0\" cellpadding=\"00\">");
		for(int i=0;i<lstUser.size();i++){
			User user = lstUser.get(i);
			String name = user.getUserName();
			if(null!=name && !"".equals(name)){
				sb.append("<tr class=\"biao_bg1\"><td>")
					.append(name);
				String path = getRequest().getContextPath();
				String frameroot= (String) getRequest().getSession().getAttribute("frameroot"); 
				if(frameroot==null||frameroot.equals("")||frameroot.equals("null")){
					frameroot= "/frame/theme_gray";
				}
				frameroot=path+frameroot;
				sb.append("</td>");
				if("public".equals(searchType)){//公共通讯录
					sb.append("<td width=\"135\" align=\"right\">");
					sb.append("<img src=\""+frameroot+"/images/tijiao.gif\" title=\"发送邮件\" width=\"15\" style=\"cursor: hand;\" onclick=\"javascript:sendmail('"+user.getUserId()+"','public'"+")\" height=\"15\"></img>")
							.append("&nbsp;&nbsp;")
							.append("<img style=\"cursor: hand;\" src=\""+path+"/oa/image/address/sms.gif\" title=\"发送消息\" onclick=\"javascript:sendmsg('"+user.getUserId()+"'"+")\" width=\"15\" height=\"15\" />")
							.append("&nbsp;&nbsp;")
							.append("<img style=\"cursor: hand;\" src=\""+path+"/oa/image/address/mobile_sms.gif\" title=\"发送手机短信\" onclick=\"javascript:sendphone('"+user.getUserId()+"'"+")\" width=\"15\" height=\"17\" />")
							.append("&nbsp;&nbsp;")
							.append("<img style=\"cursor: hand;\" src=\""+path+"/oa/image/address/rtx.bmp\" title=\"发送即时消息\" onclick=\"javascript:sendIM('"+user.getUserId()+"','"+manager.getCurrentUser().getUserId()+"'"+")\" width=\"15\" height=\"15\" />");
				}else{
					sb.append("<td width=\"55\" align=\"right\">");
					sb.append("<img style=\"cursor: hand;\" src=\""+frameroot+"/images/tijiao.gif\" title=\"发送邮件\" onclick=\"javascript:sendmail('"+user.getUserId()+"','personal'"+")\" width=\"15\" height=\"15\" />")
					.append("&nbsp;&nbsp;")
					.append("<img style=\"cursor: hand;\" src=\""+frameroot+"/images/chakan.gif\" width=\"15\" title=\"查看属性\" onclick=\"javascript:prop('"+user.getUserId()+"'"+")\" height=\"15\" />");
				}
				
				sb.append("</td></tr>");
			}
			if(null!=user.getUserTel() && !"null".equals(user.getUserTel()) && !"".equals(user.getUserTel())){
				sb.append("<tr class=\"td1\"><td  colspan=\"2\"><span class=\"wz\">");
				sb.append("电话："+user.getUserTel()).append("</span></td></tr>"); 
			}
			if(null!=user.getUserEmail() && !"null".equals(user.getUserEmail()) && !"".equals(user.getUserEmail())){
				sb.append("<tr class=\"td1\"><td  colspan=\"2\"><span class=\"wz\">");
				sb.append("邮件："+user.getUserEmail()).append("</span></td></tr>"); 
			}
			sb.append("<tr class=\"td1\"><td  colspan=\"2\"></td></tr>");
		}
		if(lstUser.size()==0){
			sb.append("<tr class=\"biao_bg1\"><td><span class=\"wz\">没有找到相关记录</span></td></tr>");
		}
		sb.append("</table>");
		if(logger.isInfoEnabled()){
			logger.info(sb.toString());
		}
		ActionContext.getContext().put("searchResult", sb.toString());
		return "searchresult";
	}

	/**
	 * author:dengzc
	 * description:系统通讯录中查看用户详细信息
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String userDetail() throws Exception{
		if(userId == null || "".equals(userId)){
			throw new Exception("用户ID不存在！");
		}
		model = manager.getUserInfo(userId);
		return "userdetail";
	}
	public String userImport() throws Exception{
		return "import";
	}
	

	/**
	 * 显示个人通讯录要导入的公共通讯录列表
	 * @author:邓志城
	 * @date:2009-7-20 上午08:43:48
	 * @return
	 * @throws Exception
	 */
	public String importOrgUserList()throws Exception{
		String parentId=null;
		if("1".equals(orgId)){//取当前用户所在机构人员
			TUumsBaseOrg baseOrg =  manager.getUserDepartmentByUserId();//获取当前用户所在的组织机构
			orgId = baseOrg.getOrgId();
			orgName = baseOrg.getOrgName();
		}
		if(orgId == null || "".equals(orgId)){
			orgName = "用户列表";
		}
		if(orgId != null && !"".equals(orgId)){
			TUumsBaseOrg baseOrg1 =manager.getOrgInfoByOrgId(orgId);
			parentId=baseOrg1.getOrgParentId();
		}
		page = manager.getUsersByOrgId(page, orgId, userName,parentId);
		return "importlist";
	}
	
	
	/**
	 * 得到带复选框的树机构(大集中项目移植)
	 * @author:邓志城
	 * @date:2009-12-21 上午10:52:00
	 * @return
	 * @throws Exception
	 */
	public String showDictOrgTreeWithCheckbox() throws Exception  {
		String param = getRequest().getParameter("type");
		String ischeckbox = getRequest().getParameter("ischeckbox");
		List<TempPo> list = manager.getOrgListFromDict(param);
		if(!list.isEmpty()){
			TempPo firstPo = list.get(0);
			getRequest().setAttribute("root", firstPo.getName());
		}else{
			getRequest().setAttribute("root", "未找到记录");
		}
		getRequest().setAttribute("orgList",list);
		getRequest().setAttribute("ischeckbox",ischeckbox);
		return "choosedictorg";
	}
	
	public String showTree() throws Exception
	{
		String param = getRequest().getParameter("type");
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		List<TempPo> allList = manager.getOrgListFromDict(param);
		if(!allList.isEmpty()){
			TempPo firstPo = allList.get(0);
			getRequest().setAttribute("root1", firstPo.getName());
		}else{
			getRequest().setAttribute("root1", "未找到记录");
		}
		int topOrgcodelength = 6;
		TreeNode node;
		String parentCode;
		String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		for(TempPo Po : allList)
		{
			if(!"发文红头".equals(Po.getName())){
			node = new TreeNode();
			node.setId(Po.getId());
			parentCode = this.getParentCode(Po.getId(), codeRule);
			node.setPid("-1");
			node.setShowcheck(true);
			
			//node.setPid(Po.getId().length() == topOrgcodelength?"-1":parentCode);
			node.setComplete(true);
			node.setIsexpand(false);
			node.setText(Po.getName());
			node.setValue(Po.getId());
			nodeLst.add(node);
			}
		}

		String jsonTreeNode = JsonUtil.fromTreeNodes(nodeLst);
		
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonTreeNode);
		getRequest().setAttribute("orgList",jsonTreeNode);
		return "choosedictorgTree";
	}
	
	/**
	 * 得到父级代码。
	 * 
	 * @param codeRule -
	 *            编码规则
	 * @param code -
	 *            代码
	 * @return String - 父级代码,如果为最顶级则返回""
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	private String getParentCode(String code, String codeRule)
			throws Exception {
		String parentCode = null;
		if (code != null && codeRule != null) {
			// 校验代码编码规则
			// 分析编码规则
			List<Integer> lstCodeRule = new ArrayList<Integer>();
			for (int i = 0; i < codeRule.length(); i++) {
				String str = codeRule.substring(i, i + 1);
				lstCodeRule.add(new Integer(str));
			}
			int codeLength = code.length();
			int count = 0;
			for (Iterator<Integer> iter = lstCodeRule.iterator(); iter
					.hasNext();) {
				Integer len = iter.next();
				count += len.intValue();
				if (count == codeLength) {
					count -= len.intValue();
					parentCode = code.substring(0, count);
				}
			}
		}
		return parentCode;
	}
	
	
	/**
	 * 选择人员时的全选功能。
	 * @author:郑志斌
	 * @date:2010-6-2 下午16:47:53
	 * @param type "1":公文通讯录；其他：系统通讯录
	 * @return
	 * @throws Exception
	 */
	public String getAllUsersForWorkflow() throws Exception {
		String ret = "";
		try{
			orgId = getRequest().getParameter("orgId");
			type = getRequest().getParameter("type");
			ret = manager.getAllUserInfoForWorkflow(type,orgId);
		}catch(Exception e){
			logger.error("工作流选择人员全选时出现错误，异常信息：" + e);
			ret = "-1";
		}
		return this.renderText(ret);
	}
	
	/**
	 * 工作流选择人员,按用户组显示
	 * @author:邓志城
	 * @date:2011-1-13 下午04:05:34
	 * @return
	 */
	public String groupTree() {
		List<TempPo> tree = new LinkedList<TempPo>();
		try {
			List groupList;
			if("1".equals(type)) {
				groupList = userService.getAllGroupInfo();
			} else {
				groupList = userService.getAllGroupInfoByHa(userService.getCurrentUser().getOrgId());//得到分级授权机构下的用户组列表
			}
			String ruleCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_GROUP);//读取codeRule.properties文件中用户组编码规则
			if(groupList != null && !groupList.isEmpty()) {
				Map<String, TUumsBaseGroup> map = new LinkedHashMap<String, TUumsBaseGroup>();
				for(int i=0;i<groupList.size();i++) {
					TUumsBaseGroup group = (TUumsBaseGroup)groupList.get(i);
					map.put(group.getGroupSyscode(), group);
				}
				
				for(int i=0;i<groupList.size();i++) {
					TUumsBaseGroup group = (TUumsBaseGroup)groupList.get(i);
					TempPo node = new TempPo();
					node.setId("h"+group.getGroupId());
					StringBuilder parentId = new StringBuilder(userService.findFatherCode(group.getGroupSyscode(), ruleCode, null));
					String parent = parentId.toString();
					if("0".equals(parent)) {//根节点
						
					} else {
						TUumsBaseGroup cp = map.get(parent);
						if(cp != null) {
							parent = "h"+cp.getGroupId();
						}
					}
					node.setParentId(parent);
					node.setName(group.getGroupName());
					node.setCodeId("h"+group.getGroupId());
					tree.add(node);
				}
			}
		} catch (Exception e) {
			logger.error("查询分级授权机构下的用户组列表出错", e);
		}
		getRequest().setAttribute("data", tree);
		return "grouptree";
	}
	
	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public Object getModel() {
		return model;
	}

	@Autowired
	public void setManager(AddressOrgManager manager) {
		this.manager = manager;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Page<TUumsBaseUser> getPage() {
		return page;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName)throws Exception {
		orgName = URLDecoder.decode(orgName, "utf-8");
		this.orgName = orgName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChooseType() {
		return chooseType;
	}

	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setType(String type) {
		this.type = type;
	}

}
