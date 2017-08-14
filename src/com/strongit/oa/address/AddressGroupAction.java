//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\address\\AddressGroupAction.java

package com.strongit.oa.address;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.address.util.GroupConst;
import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.oa.bo.ToaGroup;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.UserGroup;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2009年2月12日10:31:52
 * @version 1.0.0.0
 * @comment 个人通讯录分组Action
 */
@ParentPackage("default")
public class AddressGroupAction extends BaseActionSupport<ToaAddressGroup> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1572652531068993670L;
	private String groupId;
	public AddressGroupManager manager;
	private ToaAddressGroup model = new ToaAddressGroup();
	private List<ToaAddressGroup> groupList = new ArrayList<ToaAddressGroup>();;
	private String id;
	// 判断是否为委派页面
	private String typewei;

	public String getTypewei() {
		return typewei;
	}

	public void setTypewei(String typewei) {
		this.typewei = typewei;
	}

	private String groupName;

	// 邮件发送 选择接收人
	private String accepter; // 收件人
	private String csAccepter;// 抄送
	private String msAaccepter;// 密送

	private String users;// 导入系统人员时选择的人员。格式：【user1,user2,...】

	@Autowired
	IUserService userService;// 注入同一用户服务对象

	private String isShowAllUser; // 1:显示，其他：不显示；是否显示全部人员节点,目前在内部消息模块需提供此功能

	/**
	 * @roseuid 495041D2008C
	 */
	public AddressGroupAction() {
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaAddressGroup getModel() {
		return model;
	}

	@Override
	public String delete() throws Exception {
		try {
			manager.deleteGroup(groupId);
			renderText("删除成功！");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * 转到分组编辑页面
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		return "add";
	}

	/**
	 * 
	 * author:dengzc description:获取通讯录分组列表 modifyer: description:
	 * 
	 * @return
	 */
	@Override
	public String list() throws Exception {
		List<ToaAddressGroup> list = manager.getGroupList();
		// 系统默认提供一些组
		String userId = manager.getCurrentUser().getUserId();
		String[] sysGroup = GroupConst.systemGroup;
		for (int i = 0; i < sysGroup.length; i++) {
			String groupName = sysGroup[i];
			if (!isExistGroup(list, groupName, userId)) {
				ToaAddressGroup group = new ToaAddressGroup();
				group.setAddrGroupName(groupName);
				group.setUserId(userId);
				manager.addGroup(group);
			}
		}
		String strGroup = manager.getStrGroupLst();
		getRequest().setAttribute("group", strGroup);
		/*
		 * List<ToaAddressGroup> newLst = manager.getGroupList();
		 * for(Iterator<ToaAddressGroup> it=newLst.iterator();it.hasNext();){
		 * ToaAddressGroup groupSrc = it.next(); ToaAddressGroup group = new
		 * ToaAddressGroup(); int personCount =
		 * groupSrc.getToaAddresses()==null?0:groupSrc.getToaAddresses().size();
		 * group.setAddrGroupId(groupSrc.getAddrGroupId());
		 * group.setAddrGroupName
		 * (groupSrc.getAddrGroupName()+"("+personCount+")");
		 * groupList.add(group); }
		 */
		return SUCCESS;
	}

	/**
	 * 获取组列表,通过树标签在前台展示
	 * 
	 * @author:郑志斌
	 * @date:2010-6-1 下午15:16:03
	 * @return
	 * @throws Exception
	 */
	public String orgtree() throws Exception {
		List<TempPo> nodeList = new ArrayList<TempPo>();
		TempPo personalRoot = new TempPo();// 构造个人通讯录根节点
		personalRoot.setId(GlobalBaseData.ADDRESS_PERSONAL_ROOT_NAME);
		personalRoot
				.setName(getText(GlobalBaseData.ADDRESS_PERSONAL_ROOT_NAME));// 节点名,从资源文件中读取
		personalRoot.setParentId("0");
		personalRoot.setTypewei(typewei);
		personalRoot.setType("docAddress");
		nodeList.add(personalRoot);

		TempPo publicRoot = new TempPo();// 构造系统通讯录根节点
		publicRoot.setId(GlobalBaseData.ADDRESS_PUBLIC_ROOT_NAME);
		publicRoot.setParentId("0");
		publicRoot.setTypewei(typewei);
		publicRoot.setName(getText(GlobalBaseData.ADDRESS_PUBLIC_ROOT_NAME));// 节点名,从资源文件中读取
		publicRoot.setType("orgAddress");
		if (publicRoot.getName() == null
				|| "".equals(publicRoot.getName())
				|| GlobalBaseData.ADDRESS_PUBLIC_ROOT_NAME.equals(publicRoot
						.getName())) {
			publicRoot.setId("0");
		} else {
			nodeList.add(publicRoot);
		}

		// 个人通讯录节点挂接设置
		List<ToaAddressGroup> groupList = manager.getGroupList();
		for (ToaAddressGroup group : groupList) {
			TempPo groupPo = new TempPo();
			groupPo.setId(group.getAddrGroupId());
			groupPo.setName(group.getAddrGroupName());
			groupPo.setParentId(personalRoot.getId());
			groupPo.setTypewei(typewei);
			groupPo.setType("docAddress");// 个人通讯录节点
			nodeList.add(groupPo);
		}
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);// 读取codeRule.properties文件中组织机构编码规则
		// 增加全部人员节点
		if ("1".equals(isShowAllUser)) {
			TempPo allRoot = new TempPo();
			allRoot.setId(GlobalBaseData.ADDRESS_ALL_ROOT_NAME);
			allRoot.setName("全部人员");
			allRoot.setParentId("0");
			allRoot.setTypewei(typewei);
			allRoot.setType("orgAddress");
			nodeList.add(allRoot);
			List<TUumsBaseOrg> orgList = userService.getAllOrgInfo();
			if (orgList != null && !orgList.isEmpty()) {
				for (TUumsBaseOrg organization : orgList) {
					TempPo orgPo = new TempPo();
					orgPo.setCodeId(organization.getOrgSyscode());
					orgPo.setId(organization.getOrgSyscode());
					orgPo.setName(organization.getOrgName());
					String parentId = userService.findFatherCode(
							organization.getOrgSyscode(), config_orgCode, null);
					if ("0".equals(parentId)) {
						parentId = allRoot.getId();
					}
					orgPo.setTypewei(typewei);
					orgPo.setParentId(parentId);
					orgPo.setType("orgAddress");// 系统通讯录节点
					nodeList.add(orgPo);
				}
			}
		}
		// 增加"用户组"节点
		TempPo userGroupRoot = new TempPo();
		userGroupRoot.setId(GlobalBaseData.ADDRESS_USERGROUP_ROOT_NAME);
		userGroupRoot.setName("用户组");
		userGroupRoot.setParentId("0");
		userGroupRoot.setType("userGroup");
		userGroupRoot.setTypewei(typewei);
		// userGroupRoot.setCodeId(null);
		nodeList.add(userGroupRoot);
		List<UserGroup> grpList = userService.getAllGroupInfo();
		String config_groupCode = PropertiesUtil
				.getCodeRule(Const.RULE_CODE_GROUP);// 读取codeRule.properties文件中组织机构编码规则
		if (grpList != null && !grpList.isEmpty()) {
			for (UserGroup organization : grpList) {
				TempPo orgPo = new TempPo();
				orgPo.setCodeId(organization.getGroupId());
				// orgPo.setId(organization.getGroupId());
				orgPo.setId("g" + organization.getGroupSyscode());
				orgPo.setName(organization.getGroupName());
				// String parentId =
				// userService.findFatherCode(organization.getGroupSyscode(),
				// config_orgCode, null);
				String parentId = userService.findFatherCode(
						organization.getGroupSyscode(), config_groupCode, null);
				if ("0".equals(parentId)) {
					parentId = userGroupRoot.getId();
				} else {
					parentId = "g" + parentId;
				}
				orgPo.setTypewei(typewei);
				orgPo.setParentId(parentId);
				orgPo.setType("userGroup");// 用户组节点
				nodeList.add(orgPo);
			}
		}

		// ----------- End -----------------
		List<Organization> orgList = userService.getAllDeparments();
		// 找到机构根节点,编码长度最短的是根节点
		List<Organization> newList = new ArrayList<Organization>(orgList);
		Collections.sort(newList, new Comparator<Organization>() {

			public int compare(Organization o1, Organization o2) {
				String code1 = o1.getOrgSyscode();
				String code2 = o2.getOrgSyscode();
				if (code1 != null && code2 != null) {
					return code1.length() - code2.length();
				}
				return 0;
			}

		});
		String root = null;
		if (!newList.isEmpty()) {
			root = newList.get(0).getOrgSyscode();
		}

		for (int i = 0; i < orgList.size(); i++) {
			Organization organization = orgList.get(i);
			TempPo orgPo = new TempPo();
			orgPo.setCodeId(organization.getOrgSyscode());
			orgPo.setId(organization.getOrgSyscode());
			orgPo.setName(organization.getOrgName());
			String parentId = userService.findFatherCode(
					organization.getOrgSyscode(), config_orgCode, null);
			/*
			 * if(i == 0){//机构列表根据编码排序，第一个一定是根节点 if(!"0".equals(parentId)){
			 * parentId = publicRoot.getId(); } }
			 */
			if (organization.getOrgSyscode().equals(root)) {
				if (!"0".equals(parentId)) {
					parentId = publicRoot.getId();
				}
			}
			/*
			 * if(organization.getOrgParentId() == null ||
			 * organization.getOrgId().equals(organization.getOrgParentId())){
			 * orgPo.setParentId(publicRoot.getId()); }else{
			 * orgPo.setParentId(organization.getOrgParentId()); }
			 */
			orgPo.setTypewei(typewei);
			orgPo.setParentId(parentId);
			orgPo.setType("orgAddress");// 系统通讯录节点
			nodeList.add(orgPo);
		}
		getRequest().setAttribute("groupLst", nodeList);
		return "orgtree";
	}

	/**
	 * 获取组列表,通过树标签在前台展示
	 * 
	 * @author:邓志城
	 * @date:2009-7-20 上午09:46:03
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception {
		List<ToaAddressGroup> list = manager.getGroupList();
		getRequest().setAttribute("groupLst", list);
		return "tree";
	}

	/**
	 * 将系统通讯录导入到个人通讯录时,允许用户选择一个组时,将所选择的组 设为默认的存放系统人员的组,以方便以后导入时直接导入到此组中。
	 * 这里采用的策略是将默认的组放入session中。因此下次登录后,再次 导入时需要重新选择。
	 * 
	 * @author:邓志城
	 * @date:2009-7-20 上午11:16:02
	 * @return
	 * @throws Exception
	 */
	public String setGroupDefault() throws Exception {
		try {
			getRequest().getSession().setAttribute("defaultGroup", groupId);
			renderText(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			renderText(ERROR);
		}
		return null;
	}

	/**
	 * 获取默认的系统人员存放组,若未找到返回“null” 若找到返回组ID
	 * 
	 * @author:邓志城
	 * @date:2009-7-20 上午11:22:52
	 * @return
	 * @throws Exception
	 */
	public String getGroupDefault() throws Exception {
		String ret = "";
		try {
			Object obj = getRequest().getSession().getAttribute("defaultGroup");
			if (obj == null) {
				ret = "null";
			} else {
				ret = obj.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = ERROR;
		}
		return renderText(ret);
	}

	/**
	 * author:luosy description: 查找重复的用户名 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkUserName() throws Exception {
		try {
			String ret = manager.checkUserName(groupId, users);
			return renderText(ret);
		} catch (Exception e) {
			e.printStackTrace();
			return renderText(ERROR);
		}
	}

	/**
	 * 将选择的系统人员导入到个人通讯录组中
	 * 
	 * @author:邓志城
	 * @date:2009-7-20 上午11:32:57
	 * @return
	 * @throws Exception
	 */
	public String doImport() throws Exception {
		try {
			manager.importPublic2PersonalAddress(groupId, users);
			return renderText(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return renderText(ERROR);
		}
	}

	/**
	 * author:dengzc description:验证用户的系统组是否已经初始化了 modifyer: description:
	 * 
	 * @param lst
	 * @param groupName
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private boolean isExistGroup(List<ToaAddressGroup> lst, String groupName,
			String userId) throws Exception {
		if (lst == null || lst.size() == 0) {
			return false;
		}
		for (ToaAddressGroup group : lst) {
			if (group.getAddrGroupName().equals(groupName)
					&& userId.equals(group.getUserId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证要删除的联系组是否是系统提供的
	 */
	public String checkIsSysGroup() throws Exception {
		try {
			groupName = URLDecoder.decode(groupName, "utf-8");
			if (Arrays.asList(GroupConst.systemGroup).contains(groupName)) {
				renderText("yes");// 是系统类型
			} else {
				renderText("no");
			}
		} catch (Exception e) {
			renderText("error");
		}
		return null;
	}

	/**
	 * 发送邮件选择人员
	 * 
	 * @author:邓志城
	 * @date:2009-12-9 上午10:35:37
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String chooseperson() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");

		accepter = manager.formatMailAddress(accepter);
		csAccepter = manager.formatMailAddress(csAccepter);
		msAaccepter = manager.formatMailAddress(msAaccepter);

		List<ToaAddressGroup> list = manager.getGroupList();
		TempPo po = new TempPo();
		TempPo publicPo = new TempPo();
		List newList = new ArrayList();
		po.setId("personalgroupId");
		po.setName("个人通讯录");
		po.setParentId("0");

		publicPo.setId("publicgroupId");
		publicPo.setName("系统通讯录");
		publicPo.setParentId("0");

		newList.add(po);
		newList.add(publicPo);
		for (Iterator<ToaAddressGroup> it = list.iterator(); it.hasNext();) {
			ToaAddressGroup group = it.next();
			TempPo temp = new TempPo();
			temp.setId(group.getAddrGroupId());
			temp.setName(group.getAddrGroupName());
			temp.setParentId(po.getId());
			newList.add(temp);
		}
		// 系统通讯录
		List<Organization> lst = manager.getAllDeparments();
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);// 读取codeRule.properties文件中组织机构编码规则
		for (Iterator<Organization> it = lst.iterator(); it.hasNext();) {
			TempPo ppo = new TempPo();
			Organization org = it.next();
			String orgSysCode = org.getOrgSyscode();
			String parentId = manager.findFatherCode(orgSysCode,
					config_orgCode, null);
			ppo.setId(orgSysCode);
			ppo.setCodeId(org.getOrgId());
			if ("0".equals(parentId)) {
				ppo.setParentId(publicPo.getId());
			} else {
				ppo.setParentId(parentId);
			}
			ppo.setType("public");
			ppo.setName(org.getOrgName());
			newList.add(ppo);

		}

		getRequest().setAttribute("groupList", newList);
		return "chooseperson";
	}

	/**
	 * author:dengzc description:显示对应组下的人员列表 modifyer: description:
	 * 
	 * @return
	 */
	public String choosePersonByGroupId() throws Exception {
		try {
			getRequest().setAttribute("backlocation", "javascript:cancel()");
			ToaAddressGroup group = null;
			StringBuffer sb = new StringBuffer("");
			if (groupId == null) {
				List<ToaAddressGroup> list = manager.getGroupList();
				// 获取个人通讯录下第一组的联系人列表
				if (list.size() > 0) {
					group = list.get(0);
				} else {
					return null;
				}
			} else {
				group = manager.getGroupById(groupId);
			}
			Set<ToaAddress> set = group.getToaAddresses();
			for (Iterator<ToaAddress> it = set.iterator(); it.hasNext();) {
				ToaAddress address = it.next();
				sb.append("<tr class=\"tr1\">");
				sb.append("<td class=\"td1\" style=\"text-indent: 0px;\" align=\"center\">");
				sb.append(address.getName());
				sb.append("</td>");
				sb.append("<td class=\"td1\" style=\"text-indent: 0px;\" align=\"center\">");
				String em = address.getDefaultEmail();
				if (null == em || "".equals(em)) {
					String userId = address.getUserId();
					em = userService.getUserInfoByUserId(userId).getUserEmail();
				}
				if (null == em) {
					em = "";
				}
				sb.append(em);
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

	@Override
	protected void prepareModel() throws Exception {
		if (id != null) {
			model = manager.getGroupById(id);
		}
	}

	/**
	 * 个人桌面侧栏通讯录关联
	 * 
	 * @author:邓志城
	 * @date:2009-12-9 上午10:45:12
	 * @return
	 * @throws Exception
	 */
	public String systree() throws Exception {
		List<ToaAddressGroup> lst = manager.getGroupList();// 获取所有组
		// 系统默认提供一些组
		String userId = manager.getCurrentUser().getUserId();// 当前用户
		String[] sysGroup = GroupConst.systemGroup;// 默认组
		for (int i = 0; i < sysGroup.length; i++) {
			String groupName = sysGroup[i];
			if (!isExistGroup(lst, groupName, userId)) {
				ToaAddressGroup group = new ToaAddressGroup();
				group.setAddrGroupName(groupName);
				group.setUserId(userId);
				manager.addGroup(group);
			}
		}

		List<ToaAddressGroup> newLst = manager.getGroupList();// 再次获取所有组

		String[] hasChild = new String[newLst.size()];
		for (int i = 0; i < newLst.size(); i++) {
			ToaAddressGroup group = newLst.get(i);
			int count = manager.getUserCountByGroupId(group.getAddrGroupId());// 获取组下的人员数
			if (count > 0) {
				hasChild[i] = "1";// 表示存在字节点
			} else {
				hasChild[i] = "0";// 表示不存在子结点
			}
		}
		getRequest().setAttribute("groupList", newLst);
		getRequest().setAttribute("hasChild", hasChild);
		return "systree";
	}

	/**
	 * author:dengzc description:异步加载树 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String synajaxtree() throws Exception {
		try {
			StringBuffer str = new StringBuffer("");
			List<ToaAddress> addressLst = manager.getAddressByGroupId(groupId);
			for (int i = 0; i < addressLst.size(); i++) {
				ToaAddress address = addressLst.get(i);
				str.append("<li id="
						+ address.getAddrId()
						+ "><span>"
						+ address.getName()
						+ "</span><label style='display:none;'>person</label></li>\n");
			}
			renderHtml(str.toString());
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * 保存组信息
	 */
	@Override
	public String save() throws Exception {
		try {
			getRequest().setAttribute("backlocation", "javascript:cancel()");
			model.setUserId(manager.getCurrentUser().getUserId());
			String goupName = model.getAddrGroupName();
			if (!"".equals(goupName) && null != goupName) {
				goupName = java.net.URLDecoder.decode(goupName, "utf-8");
			}
			if(manager.isExistGroup(goupName)){
				renderText("exist");
			}else{
				manager.addGroup(model);
				renderText("添加成功!");
			}
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	@Autowired
	public void setTheAddressGroupManager(
			AddressGroupManager theAddressGroupManager) {
		this.manager = theAddressGroupManager;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<ToaAddressGroup> getGroupList() {
		return groupList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAccepter() {
		return accepter;
	}

	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}

	public String getCsAccepter() {
		return csAccepter;
	}

	public void setCsAccepter(String csAccepter) {
		this.csAccepter = csAccepter;
	}

	public String getMsAaccepter() {
		return msAaccepter;
	}

	public void setMsAaccepter(String msAaccepter) {
		this.msAaccepter = msAaccepter;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getIsShowAllUser() {
		return isShowAllUser;
	}

	public void setIsShowAllUser(String isShowAllUser) {
		this.isShowAllUser = isShowAllUser;
	}
}
