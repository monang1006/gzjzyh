package com.strongit.uums.usergroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseGroup;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "baseGroup!groupTree.action", type = ServletActionRedirectResult.class),
		@Result(name = "insert", value = "/WEB-INF/jsp/usergroup/baseGroup-insert.jsp", type = ServletDispatcherResult.class) })
public class BaseGroupAction extends BaseActionSupport {
	// private Page<TUumsBaseGroup> grouppage = new Page<TUumsBaseGroup>(10,
	// true);

	private String groupId;

	private String codeType;

	private String code;

	private String privilIds;

	private String userIds;

	private String groupPrivil;

	private String groupCodes;

	private String groupUsers;

	private List<TUumsBaseGroup> groupList;

	private List<TUumsBasePrivil> privilList;

	private List<TUumsBaseUser> userList = null;

	private List<TUumsBaseOrg> orgList = null;

	private TUumsBaseGroup model = new TUumsBaseGroup();

	private List<TUumsBaseSystem> systemList;

	private Page<TUumsBaseUser> page = new Page<TUumsBaseUser>(FlexTableTag.MAX_ROWS, true);

	private String orgcode;

	private String message;

	// 是否将下级组织机构设置为启用状态标识
	private String childTogether;

	// 组织机构系统编码
	private String groupSyscode;
	
	//资源权限编号，用于ajax加载
	private String privilCode;
	//应用系统编号，用于ajax加载
	private String systemCode;
	//组织机构编码，选择委托人延迟加载时使用
	private String orgSyscode;
	
	@Autowired IUserService userService;
	
	@Autowired BaseOptPrivilManager basePrivilManager;

	public String getGroupSyscode() {
		return groupSyscode;
	}

	public void setGroupSyscode(String groupSyscode) {
		this.groupSyscode = groupSyscode;
	}

	public Page<TUumsBaseUser> getPage() {
		return page;
	}

	public BaseGroupAction() {
	}

	@Override
	public String delete() throws Exception {
		try {
			userService.deleteGroups(groupId);
			addActionMessage("删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return renderHtml("<script>window.location='"
				+ getRequest().getContextPath()
				+ "/usergroup/baseGroup!groupTree.action';" +
						"parent.propertiesList.location='"+getRequest().getContextPath()+"/usergroup/baseGroup!userList.action';</script>");
		// return RELOAD;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	/**
	 * 检验编码合法性
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-25 下午02:17:49
	 * @return String
	 * @throws Exception
	 */
	public String compareCode() throws Exception {
		message = "111";
		boolean iserr = userService.checkGroupCode(orgcode);
		if (iserr) {
			boolean flag = userService.compareGroupCode(orgcode);
			if (flag) {
				boolean isParentExist = userService.checkGroupParentIsExist(orgcode,
						groupId);
				if (!isParentExist) {
					message = "222";// 该编码的父级不存在
				}
			} else {
				message = "000";// 编码不唯一
			}
		} else {
			message = "333";// 编码不符合规则
		}
		return this.renderText(message);
	}

	/**
	 * 添加新用户组
	 * 
	 * @author 蒋国斌
	 * @date 下午05:15:54
	 * @return
	 * @throws Exception
	 */
	public String insert() throws Exception {
		prepareModel();
		String nextCode = userService.getNextGroupCode(code);
		if (nextCode.equals(code)) {
			return "windowclose";
		}
		model.setGroupSyscode(nextCode);
		return "insert";
	}

	/**
	 * 得到用户组树
	 * 
	 * @author 蒋国斌
	 * @date 下午05:16:43
	 * @return
	 * @throws Exception
	 */
	public String groupTree() throws Exception {
		if(userService.isSystemDataManager(userService.getCurrentUserInfo().getUserId())) {
			groupList = userService.getAllGroupInfos();
		} else {
			groupList=userService.getAllGroupInfosByHa(userService.getCurrentUserInfo().getOrgId());			
		}
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_GROUP);
		return "groupTree";
	}

	/**
	 * 给用户组添加组成员
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-11下午05:18:05
	 * @return String
	 * @throws Exception
	 */
	public String adduser() throws Exception {

		// List lst = manager.getGroupUsers(groupId);
		List lst = userService.getGroupUsers(groupId, "1", "0");
		if (lst.size() != 0 && lst != null) {
			StringBuffer sbr = new StringBuffer("");
			for (Iterator<TUumsBaseUser> iter = lst.iterator(); iter.hasNext();) {
				TUumsBaseUser user = (TUumsBaseUser) iter.next();
				String id = user.getUserId();
				sbr.append(",").append(id);
			}
			groupUsers = sbr.substring(1);
		}
		String random = "%";
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUserInfo().getUserId());
		String codes=org.getOrgSyscode();
		//orgList = orgManager.getOrgsByOrgSyscode(random, Const.IS_NO);
		orgList = userService.getOrgsByOrgSyscodeByHa(random, Const.IS_NO, userService.getCurrentUserInfo().getOrgId());
		for(int j=0; j<orgList.size();){
			if(!orgList.get(j).getOrgSyscode().equals(codes)){
				orgList.remove(j);
			}else{
				j++;
			}
		}
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "adduser";
	}
	

	/**
     * 角色选人，不使用延迟加载
     * @author: qibh
     * @return
     * @throws Exception
     * @created: 2013-11-19 上午11:46:13
     * @version :
     */
    public String adduerNew() throws Exception{
    	List lst = userService.getGroupUsers(groupId, Const.IS_YES,
                Const.IS_NO);
        if (lst.size() != 0 && lst != null) {
            StringBuffer sbr = new StringBuffer("");
            for (Iterator<TUumsBaseUser> iter = lst.iterator(); iter.hasNext();) {
                TUumsBaseUser user = (TUumsBaseUser) iter.next();
                String id = user.getUserId();
                sbr.append(",").append("u").append(id);
            }
            groupUsers = sbr.substring(1);
        }
    	List<TempPo> nodes = new LinkedList<TempPo>();
        try {
            List<Organization> orgList = userService.getAllDeparments();// 得到机构列表
            Map<String, List<User>> userMap = userService.getUserMap();
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
                TempPo po = new TempPo();
                po.setId("o" + organization.getOrgId());
                po.setName(organization.getOrgName());
                String parentId = organization.getOrgParentId();
                po.setType("o");
                /*
                 * if(i == 0){//机构列表根据编码排序，第一个一定是根节点 parentId = "0"; } else {
                 * parentId = "o" + parentId; }
                 */
                if (organization.getOrgSyscode().equals(root)) {
                    parentId = "0";
                } else {
                    parentId = parentId == null ? "0" : "o" + parentId;
                }
                po.setParentId(parentId);
                nodes.add(po);
                // 添加人员
                List<User> userList = userMap.get(organization.getOrgId());
                if (userList != null) {
                    for (User u : userList) {
                        TempPo upo = new TempPo();
                        upo.setId("u" + u.getUserId());
                        upo.setName(u.getUserName());
                        upo.setParentId(po.getId());
                        upo.setType("u");
                        nodes.add(upo);
                    }
                }
            }
            userMap.clear();
            orgList.clear();
        } catch (Exception e) {
            logger.error("选择机构负责人发生异常", e);
        }
        getRequest().setAttribute("data", nodes);
        getRequest().setAttribute("id", groupUsers);
        return "bigtree";
    }

	/**
	 * 保存用户组成员
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-11 下午05:19:55
	 * @return
	 * @throws Exception
	 */
	public String saveUser() throws Exception {
		//修改为延迟加载的方式，userIds为人员Id,人员Id,...|组织机构code,组织机构code,...
		if(userIds != null && !"".equals(userIds)){
			String[] orgCodes = userIds.split("\\|");
			//解析所有人员Id
			if(orgCodes.length > 1 && orgCodes[1] != null && !"".equals(orgCodes[1])){
				String[] parseUserCodes = orgCodes[1].split(",");
				for(String parseUserCode : parseUserCodes){
					List<TUumsBaseUser> userIdLst = userService.getUsersOfOrgAndChildByOrgcodeByHa(parseUserCode, Const.IS_YES, Const.IS_NO,userService.getCurrentUserInfo().getOrgId());
					for(int i = 0; i < userIdLst.size(); i++){
						orgCodes[0] = orgCodes[0] + "," + userIdLst.get(i).getUserId();
					}
				}
			}
			//所有人员Id信息，有可能会有重复，所以service端的处理需要使用到map
			//用户选择的人员为空,即没有选择任何人
			if(orgCodes.length == 0){
				orgCodes = new String[]{null};
			}else if(orgCodes[0].startsWith(",")){
				orgCodes[0] = orgCodes[0].substring(1);
			}
			userService.saveGroupUsers(groupId, orgCodes[0]);
		}
		return renderHtml("<script>window.returnValue='succss';window.close();</script>");
	}
	
	//初始化选择委托人（延迟加载，ajax调用）
	public String lazyLoadUserAndChildOrgs() throws Exception {
		TUumsBaseOrg orgInfo = userService.getOrgInfoBySyscode(orgSyscode);
		userList = userService.getUserInfoByOrgId(orgInfo.getOrgId(), Const.IS_YES, Const.IS_NO);
		int i = userService.findChildCodeLength(Const.RULE_CODE_ORG, orgInfo.getOrgSyscode());
		if(i == orgInfo.getOrgSyscode().length()){
			orgList = new ArrayList<TUumsBaseOrg>();
		}else{
			//orgList = orgManager.getOrgsByOrgSyscode(orgInfo.getOrgSyscode() + "%", Const.IS_NO);
			orgList = userService.getOrgsByOrgSyscodeByHa(orgInfo.getOrgSyscode() + "%",Const.IS_NO, userService.getCurrentUserInfo().getOrgId());
			for(int j=0; j<orgList.size();){
				if(orgList.get(j).getOrgSyscode().length() != i){
					orgList.remove(j);
				}else{
					j++;
				}
			}
		}
		String jasonStr = new String("[");
		if(orgList != null && !orgList.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBaseOrg org : orgList){
				bufStr.append(",{id:'")
						.append(org.getOrgId())
						.append("',name:'")
						.append(org.getOrgName())
						.append("',code:'")
						.append(org.getOrgSyscode())
						.append("',type:'")
						.append("org")
						.append("'}");
			}
			jasonStr = jasonStr + bufStr.substring(1);
		}
		if(userList != null && !userList.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBaseUser user : userList){
				bufStr.append(",{id:'")
						.append(user.getUserId())
						.append("',name:'")
						.append(user.getUserName())
						.append("',code:'")
						.append(user.getUserSyscode())
						.append("',type:'")
						.append("user")
						.append("'}");
			}
			if("[".equals(jasonStr)){
				jasonStr = jasonStr + bufStr.substring(1);
			}else{
				jasonStr = jasonStr + bufStr;
			}
		}
		jasonStr = jasonStr + "]";
		this.renderText(jasonStr);
		return null;
	}

	/**
	 * 给用户组添加权限
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-11 下午05:20:18
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String addprivil() throws Exception {

		List lst = userService.getGroupPrivilInfos(groupId, Const.IS_YES);
		if (lst.size() != 0 && lst != null) {
			StringBuffer sbr = new StringBuffer();
			for (Iterator<TUumsBasePrivil> iter = lst.iterator(); iter
					.hasNext();) {
				TUumsBasePrivil privil = (TUumsBasePrivil) iter.next();
				String id = privil.getPrivilSyscode();
				sbr.append(",");
				sbr.append(id);
			}
			groupPrivil = sbr.substring(1);
		}
		//privilList = privilManager.getPrivils();
		//systemList = systemManager.getCurrentUserSystems();
		//privilList = privilManager.getPrivils("1", "1", "1");
		systemList=userService.getCurrentUserSystems("1");
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);

		return "addprivil";
	}

	/**
	 * 设置资源权限
	 * @author:邓志城
	 * @date:2011-1-19 下午02:46:07
	 * @return
	 */
	public String setGroupPrivil() {
		try{
			List<TUumsBasePrivil> lst = userService.getGroupPrivilInfos(groupId, Const.IS_YES);//得到用户组已有权限
			StringBuilder ids = new StringBuilder();
			if(lst != null && !lst.isEmpty()) {
				for(TUumsBasePrivil privil : lst) {
					ids.append(privil.getPrivilSyscode()).append(",");
				}
			}
			if(ids.length() > 0) {
				ids.deleteCharAt(ids.length() - 1);
			}
			getRequest().setAttribute("id", ids.toString());
			getRequest().setAttribute("title", "设置资源权限");
			List<TempPo> data = new LinkedList<TempPo>();
			String ruleCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
			systemList = userService.getCurrentUserSystems("1");
			if(!systemList.isEmpty()) {
				TUumsBaseSystem system = systemList.get(0);
				TempPo root = new TempPo();
				root.setId("1");
				root.setName(system.getSysName());
				root.setType("system");
				root.setParentId("0");
				data.add(root);
				String parentId = "1";
				List<ToaUumsBaseOperationPrivil> privilList = basePrivilManager.getCurrentUserPrivilLst(true);
				for(ToaUumsBaseOperationPrivil privil : privilList) {
					TempPo po = new TempPo();
					po.setId(privil.getPrivilSyscode());
					po.setName(privil.getPrivilName());
					po.setType(privil.getPrivilType());
					String code = privil.getPrivilSyscode();
					String parentCode = userService.findFatherCode(code, ruleCode, "0");
					if("0".equals(parentCode)) {
						parentId = "1";
					} else {
						parentId = parentCode;
					}
					po.setParentId(parentId);
					data.add(po);
				}
			} else {
				getRequest().setAttribute("title", "设置权限操作仅限于管理员身份");
			}
			getRequest().setAttribute("data", data);
		}catch(Exception e) {
			logger.error("设置用户组资源权限出错", e);
		}
		return "tree";
	}
	
	/**
	 * 保存用户组权限
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-11 下午05:20:38
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String savePrivil() throws Exception {
		//manager.saveGroupPrivils(groupId, privilIds);
		//修改为通过资源权限编码来添加角色权限关联信息
		if(privilIds != null && !"".equals(privilIds)){
			String[] privilCodes = privilIds.split("\\|");
			//解析所有子级资源权限
			if(privilCodes.length > 1 && privilCodes[1] != null && !"".equals(privilCodes[1])){
				String[] parseSubPrivilCodes = privilCodes[1].split(",");
				for(String parseSubPrivilCode : parseSubPrivilCodes){
					List<TUumsBasePrivil> subPrivilCodeLst = userService.getPrivilInfosByPrivilCode(parseSubPrivilCode + "%", Const.IS_YES);
					for(int i = 0; i < subPrivilCodeLst.size(); i++){
						privilCodes[0] = privilCodes[0] + "," + subPrivilCodeLst.get(i).getPrivilSyscode();
					}
				}
			}
			//若用户没有选择任何资源权限
			if(privilCodes.length == 0){
				privilCodes = new String[]{null};
			}else if(privilCodes[0].startsWith(",")){
				privilCodes[0] = privilCodes[0].substring(1);
			}
			userService.saveGroupPrivilsByPrivilCodes(groupId, privilCodes[0]);
		}

		return renderHtml("<script>window.dialogArguments.parent.propertiesTree.location='"
				+ getRequest().getContextPath()
				+ "/usergroup/baseGroup!groupTree.action'; window.close();</script>");
	}

	/**
	 * 保存用户组资源
	 * @author:邓志城
	 * @date:2011-1-7 下午03:43:49
	 * @return
	 * @throws Exception
	 */
	public String saveGroupPrivil() throws Exception {
		try{
			userService.saveGroupPrivilsByPrivilCodes(groupId, privilCode);
			return renderHtml("<script>window.returnValue=\"OK\";window.close();</script>");
		}catch(Exception e) {
			logger.error("保存用户组资源发生异常", e);
			return renderHtml("<script>window.returnValue=\"NO\";window.close();</script>");
		}
	}
	
	/**
	 * 复制用户组权限
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-11 下午05:20:53
	 * @return
	 * @throws Exception
	 */
	public String copyprivil() throws Exception {

		//groupList = manager.getAllGroupInfos();
		 //TUumsBaseOrg org=userManager.getSupOrgByUserIdByHa(userManager.getCurrentUserInfo().getUserId());
		
		//	String haid=org.getOrgId();
		groupList=userService.getAllGroupInfosByHa(userService.getCurrentUserInfo().getOrgId());
	
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_GROUP);
		return "copyprivil";
	}

	/**
	 * 保存需复制的用户组权限
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-11 下午05:21:16
	 * @return
	 * @throws Exception
	 */
	public String saveCopyPrivil() throws Exception {
		userService.copyGroupPrivils(groupId, groupCodes);

		return renderHtml("<script>window.dialogArguments.parent.propertiesTree.location='"
				+ getRequest().getContextPath()
				+ "/usergroup/baseGroup!groupTree.action'; window.close();</script>");
	}

	/**
	 * 得到指定用户组下面的成员
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-11 下午05:21:44
	 * @return
	 * @throws Exception
	 */
	public String userList() throws Exception {
		try {
			if (groupId == null || groupId.equals("")) {
				//page = manager.getAllGroupUsers(page, "1", "0");
				// TUumsBaseOrg org=userManager.getSupOrgByUserIdByHa(userManager.getCurrentUserInfo().getUserId());
				//String haid=org.getOrgId();
				page = userService.getAllGroupUsersByHa(page, "1", "0",userService.getCurrentUserInfo().getOrgId());
			}else{
				page = userService.getGroupUsers(page, groupId, "1", "0");
			}
			//model = manager.getGroup(groupId);
			model=userService.getGroupInfoByGroupId(groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "userList";
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (groupId != null && !"".equals(groupId)) { 
			model = userService.getGroupInfoByGroupId(groupId);
		} else {
			model = new TUumsBaseGroup();
			Long nextSequence = userService.getNextGroupSequenceCode();
			model.setGroupSequence(nextSequence);
		}
	}

	@Override
	public String save() throws Exception {
		if ("".equals(model.getGroupId())) {
			model.setGroupId(null);
		}
		//model.setTUumsBaseSupOrg(orgManager.getOrgInfoByOrgId(userManager.getCurrentUserInfo().getOrgId()));
		//manager.saveGroup(model, childTogether);
		userService.saveGroupByHa(model, childTogether, userService.getCurrentUserInfo().getOrgId());

		return renderHtml("<script>window.dialogArguments.parent.propertiesTree.location='"
				+ getRequest().getContextPath()
				+ "/usergroup/baseGroup!groupTree.action'; window.close();</script>");
	}


	/**
	 * 用户组上移
	 * 
	 * @author 蒋国斌
	 * @date Feb 11, 2009 11:09:47 AM
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		prepareModel();
		String mes = userService.moveGroupInTree(model, Const.MOVE_UP_IN_TREE);
		if (!"".equals(mes)) {
			this.addActionMessage(mes);
			this.message = mes;
		}
		return "move";
	}

	/**
	 * 用户组下移
	 * 
	 * @author 蒋国斌
	 * @date Feb 11, 2009 11:50:00 AM
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		prepareModel();
		String mes = userService.moveGroupInTree(model, Const.MOVE_DOWN_IN_TREE);
		if (!"".equals(mes)) {
			this.addActionMessage(mes);
			this.message = mes;
		}
		return "move";
	}

	/**
	 * 判断父级元素是否被设置未启用状态
	 * 
	 * @author 喻斌
	 * @date Apr 3, 2009 2:18:20 PM
	 * @return
	 * @throws Exception
	 */
	public String checkFatherIsdel() throws Exception {
		TUumsBaseGroup groupInfo = userService
				.getParentGroupByGroupSyscode(this.groupSyscode);
		if (groupInfo != null) {
			if (Const.IS_NO.equals(groupInfo.getGroupIsactive())) {
				return this.renderText("true");
			}
		}
		return this.renderText("false");
	}
	
	/**
	 * 得到指定系统下的第一级资源或指定资源code的下一级资源信息，用于ajax延迟加载
	 * @author 喻斌
	 * @date Jul 10, 2009 1:15:47 PM
	 * @return
	 * @throws Exception
	 */
	public String lazyLoadPrivil() throws Exception {
		StringBuffer code = new StringBuffer("");
		//查找应用系统下的第一级资源权限
		if(this.systemCode != null && !"".equals(this.systemCode)){
			int codeLength = userService.findChildCodeLength(Const.RULE_CODE_PRIVIL, "");
			for(int i=0; i<codeLength; i++){
				code.append("_");
			}
			privilList = userService.getPrivilInfosByPrivilCodeAndSystem(code.toString(), systemCode, Const.IS_YES);
		//查找资源权限下的下一级资源权限
		}else if(this.privilCode != null && !"".equals(this.privilCode)){
			code.append(this.privilCode);
			int codeLength = userService.findChildCodeLength(Const.RULE_CODE_PRIVIL, privilCode);
			int iteratorIndex = codeLength - this.privilCode.length();
			//如果已经达到最大编码
			if(iteratorIndex == 0){
				iteratorIndex = 1;
			}
			for(int i=0; i<iteratorIndex; i++){
				code.append("_");
			}
			privilList = userService.getPrivilInfosByPrivilCode(code.toString(), Const.IS_YES);
		}
		String jasonStr = new String("[");
		if(privilList != null && !privilList.isEmpty()){
			StringBuffer bufStr = new StringBuffer("");
			for(TUumsBasePrivil privil : privilList){
				bufStr.append(",{id:'")
						.append(privil.getPrivilId())
						.append("',name:'")
						.append(privil.getPrivilName())
						.append("',code:'")
						.append(privil.getPrivilSyscode())
						.append("',system:'")
						.append(privil.getBasePrivilType().getBaseSystem().getSysSyscode())
						.append("'}");
			}
			jasonStr = jasonStr + bufStr.substring(1);
		}
		jasonStr = jasonStr + "]";
		this.renderText(jasonStr);
		return null;
	}

	/**
	 * @roseuid 根据用户组ID得到用户列表
	 */

	public TUumsBaseGroup getModel() {

		return model;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<TUumsBaseGroup> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<TUumsBaseGroup> groupList) {
		this.groupList = groupList;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<TUumsBasePrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<TUumsBasePrivil> privilList) {
		this.privilList = privilList;
	}

	public String getPrivilIds() {
		return privilIds;
	}

	public void setPrivilIds(String privilIds) {
		this.privilIds = privilIds;
	}

	public String getGroupPrivil() {
		return groupPrivil;
	}

	public void setGroupPrivil(String groupPrivil) {
		this.groupPrivil = groupPrivil;
	}

	public String getGroupCodes() {
		return groupCodes;
	}

	public void setGroupCodes(String groupCodes) {
		this.groupCodes = groupCodes;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public void setUserList(List<TUumsBaseUser> userList) {
		this.userList = userList;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(String groupUsers) {
		this.groupUsers = groupUsers;
	}

	public List<TUumsBaseSystem> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<TUumsBaseSystem> systemList) {
		this.systemList = systemList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getChildTogether() {
		return childTogether;
	}

	public void setChildTogether(String childTogether) {
		this.childTogether = childTogether;
	}

	public String getPrivilCode() {
		return privilCode;
	}

	public void setPrivilCode(String privilCode) {
		this.privilCode = privilCode;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getOrgSyscode() {
		return orgSyscode;
	}

	public void setOrgSyscode(String orgSyscode) {
		this.orgSyscode = orgSyscode;
	}

}
