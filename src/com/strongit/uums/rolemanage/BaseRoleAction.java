package com.strongit.uums.rolemanage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.bo.TUumsBaseRoleUser;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "baseRole.action", type = ServletActionRedirectResult.class) })
public class BaseRoleAction extends BaseActionSupport {

	private Page<TUumsBaseRole> page = new Page<TUumsBaseRole>(FlexTableTag.MAX_ROWS, true);

	private String roleId;
	
	//角色资源复制时保存当前角色Id
	private String searchRoleId;

	private String codeType;

	private String rolePrivil;

	private String privilIds;

	private String roleIds;

	private String userIds;

	private String roleUsers;

	private List<TUumsBaseUser> userList = null;

	private List<TUumsBaseOrg> orgList = null;

	private List<TUumsBaseSystem> systemList;

	private List<TUumsBaseRole> baseRoleList;

	private TUumsBaseRole model = new TUumsBaseRole();

	private List<TUumsBasePrivil> privilList;

	private Map useMap = new HashMap();

	private String orgcode;

	private String message;
	
	/*
	 * 查询相关属性
	 */
	private String selectroleId;//角色编码
	private String selectrolename;//角色名称
	private String roleisact;//是否启用
	private String roledesc;//角色描述
	
	//资源权限编号，用于ajax加载
	private String privilCode;
	//应用系统编号，用于ajax加载
	private String systemCode;
	//组织机构编码，选择委托人延迟加载时使用
	private String orgSyscode;
	
	@Autowired IUserService userService;
	
	@Autowired BaseOptPrivilManager basePrivilManager;


	public List<TUumsBasePrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<TUumsBasePrivil> privilList) {
		this.privilList = privilList;
	}

	public Page<TUumsBaseRole> getPage() {
		return page;
	}

	public BaseRoleAction() {
		useMap.put("1", "是");
		useMap.put("0", "否");
	}

	@Override
	public String delete() throws Exception {
		try {
			userService.deleteRoles(roleId);
			addActionMessage("删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return renderHtml("<script>window.location='"
				+ getRequest().getContextPath()
				+ "/rolemanage/baseRole.action';</script>");
		// return RELOAD;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		if(userService.isSystemDataManager(userService.getCurrentUserInfo().getUserId())) {
			page = userService.queryRoles(page, selectroleId, selectrolename, roleisact, roledesc);			
		} else {
			page = userService.queryRolesByHa(page, selectroleId, selectrolename, roleisact, roledesc,userService.getCurrentUserInfo().getOrgId());			
		}
		List<TUumsBaseRole> list = page.getResult();
		if(list!=null){
			for(TUumsBaseRole role:list){
				Set<TUumsBaseRoleUser> set = role.getBaseRoleUsers();
				StringBuilder userName = new StringBuilder("");
				for(Iterator<TUumsBaseRoleUser> it=set.iterator();it.hasNext();){
					TUumsBaseRoleUser ru = it.next();
					TUumsBaseUser tUser=userService.getUserInfoByUserId(ru.getUserId());
					if(Const.IS_YES.equals(tUser.getUserIsdel())){   //判断用户是否删除，删除了的用户不显示在列表中
						continue;
					}
					userName.append(userService.getUserNameByUserId(ru.getUserId()));
					if(it.hasNext()){
						userName.append("、");
					}
				}
				role.setRest3(userName.toString());
			}
		}
		model.getBaseRoleUsers();
		return SUCCESS;
	}

	/**
	 * 此方法过期，建议使用方法setRolePrivil
	 * @author:邓志城
	 * @date:2011-1-7 下午02:46:12
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String addprivil() throws Exception {
		List lst = userService.getRolePrivilInfosByRoleId(roleId, Const.IS_YES);
		if (lst.size() != 0 && lst != null) {
			StringBuffer sbr = new StringBuffer();
			for (Iterator<TUumsBasePrivil> iter = lst.iterator(); iter
					.hasNext();) {
				TUumsBasePrivil privil = (TUumsBasePrivil) iter.next();
				//String id = privil.getPrivilId();
				String id = privil.getPrivilSyscode();
				sbr.append(",");
				sbr.append(id);
			}
			if(sbr.toString().startsWith(",")){
				rolePrivil = sbr.substring(1);
			}else{
				rolePrivil = sbr.toString();
			}
		}
		//privilList = privilManager.getPrivils();
		//systemList = systemManager.getCurrentUserSystems();
		//privilList = privilManager.getPrivils("1", "1", "1");
		systemList=userService.getCurrentUserSystems("1");
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);

		return "addprivil";
	}

	/**
	 * 设置角色权限
	 * @author:邓志城
	 * @date:2011-1-7 下午03:22:42
	 * @return
	 * @throws Exception
	 */
	public String setRolePrivil() throws Exception {
		try{
			List<TUumsBasePrivil> lst = userService.getRolePrivilInfosByRoleId(roleId, Const.IS_YES);//得到角色已有的资源权限
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
			//得到当前用户资源权限
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
					//po.setId(privil.getPrivilId());
					po.setId(privil.getPrivilSyscode());
					po.setName(privil.getPrivilName());
					po.setType(privil.getPrivilType());
					String code = privil.getPrivilSyscode();
					String parentCode = userService.findFatherCode(code, ruleCode, "0");
					if("0".equals(parentCode)) {
						parentId = "1";
					} else {
						/*ToaUumsBaseOperationPrivil op = basePrivilManager.getPrivilInfoByPrivilSyscode(parentCode);
						if(op.getPrivilId() != null) {
							parentId = op.getPrivilId();
						}*/
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
			logger.error("设置角色资源时发生异常", e);
			throw e;
		}
		return "tree";
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
	 * 方法过期 replaced by saveRolePrivil
	 * @author:邓志城
	 * @date:2011-1-7 下午03:43:18
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String savePrivil() throws Exception {
		//manager.saveRolePrivils(roleId, privilIds);
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
				userService.saveRolePrivilsByPrivilCodes(roleId, privilCodes[0]);
			}

			// return "addprivil";
			//return renderHtml("<script>window.dialogArguments.location='"
			//		+ getRequest().getContextPath()
				//	+ "/rolemanage/baseRole.action'; window.close();</script>");
			 return "temp";
	}

	/**
	 * 保存角色资源
	 * @author:邓志城
	 * @date:2011-1-7 下午03:43:49
	 * @return
	 * @throws Exception
	 */
	public String saveRolePrivil() throws Exception {
		try{
			userService.saveRolePrivilsByPrivilCodes(roleId, privilCode);
			return "temp";
		}catch(Exception e) {
			logger.error("保存角色资源发生异常", e);
			throw e;
		}
	}
	
	public String copyprivil() throws Exception {
		if(userService.isSystemDataManager(userService.getCurrentUserInfo().getUserId())) {
			page = userService.queryRoles(page, selectroleId, selectrolename, roleisact, roledesc);			
		} else {
			page = userService.queryRolesByHa(page, selectroleId, selectrolename, roleisact, roledesc,userService.getCurrentUserInfo().getOrgId());			
		}
		return "copyprivil";
	}

	public String savecopyprivils() throws Exception {
		userService.copyRolePrivils(searchRoleId, roleIds);

	//	return renderHtml("<script>window.dialogArguments.location='"
		//		+ getRequest().getContextPath()
			//	+ "/rolemanage/baseRole.action'; window.close();</script>");
		// return "copyprivil";
		  return "temp";
	}

//	初始化选择角色人员（取最高级组织机构，其他的延迟加载）
	public String adduser() throws Exception {
		List lst = userService.getRoleUsersByRoleId(roleId, Const.IS_YES, Const.IS_NO);
		if (lst.size() != 0 && lst != null) {
			StringBuffer sbr = new StringBuffer("");
			for (Iterator<TUumsBaseUser> iter = lst.iterator(); iter.hasNext();) {
				TUumsBaseUser user = (TUumsBaseUser) iter.next();
				String id = user.getUserId();
				sbr.append(",").append(id);
			}
			roleUsers = sbr.substring(1);
		}
		String random = "%";
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUserInfo().getUserId());
		String codes=org.getOrgSyscode();
		orgList=userService.getOrgsByOrgSyscodeByHa(random, Const.IS_NO, userService.getCurrentUserInfo().getOrgId());
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
	
//	初始化选择委托人（延迟加载，ajax调用）
	public String lazyLoadUserAndChildOrgs() throws Exception {
		TUumsBaseOrg orgInfo = userService.getOrgInfoBySyscode(orgSyscode);
		userList = userService.getUserInfoByOrgId(orgInfo.getOrgId(), Const.IS_YES, Const.IS_NO);
		int i = userService.findChildCodeLength(Const.RULE_CODE_ORG, orgInfo.getOrgSyscode());
		if(i == orgInfo.getOrgSyscode().length()){
			orgList = new ArrayList<TUumsBaseOrg>();
		}else{
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
//						.append(user.getOrgId())
//						.append("_")
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
     * 角色选人，不使用延迟加载
     * @author: qibh
     * @return
     * @throws Exception
     * @created: 2013-11-19 上午11:46:13
     * @version :
     */
    public String adduerNew() throws Exception{
    	List lst = userService.getRoleUsersByRoleId(roleId, Const.IS_YES,
                Const.IS_NO);
        if (lst.size() != 0 && lst != null) {
            StringBuffer sbr = new StringBuffer("");
            for (Iterator<TUumsBaseUser> iter = lst.iterator(); iter.hasNext();) {
                TUumsBaseUser user = (TUumsBaseUser) iter.next();
                String id = user.getUserId();
                sbr.append(",").append("u").append(id);
            }
            roleUsers = sbr.substring(1);
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
        getRequest().setAttribute("id", roleUsers);
        return "bigtree";
    }

//	改用延迟加载方式增加角色与人员关联
	public String saveUser() throws Exception {
		try {
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
				//若用户没有选择任何人员
				if(orgCodes.length == 0){
					orgCodes = new String[]{null};
				}else if(orgCodes[0].startsWith(",")){
					orgCodes[0] = orgCodes[0].substring(1);
				}
				userService.saveRoleUsers(roleId, orgCodes[0]);
			}else{
				userService.saveRoleUsers(roleId, "");
            }
		//	return renderHtml("<script>window.dialogArguments.location='"
			//		+ getRequest().getContextPath()
				//	+ "/rolemanage/baseRole.action'; window.close();</script>");
			return "temp";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	protected void prepareModel() throws Exception {
		if (roleId != null && !"".equals(roleId)) {
			model = userService.getRoleInfoByRoleId(roleId);
		} else {
			model = new TUumsBaseRole();
			Long sequence = userService.getNextRoleSequence();
			model.setRoleSequence(sequence);
		}
	}

	@Override
	public String save() throws Exception {
	String roleId=getRequest().getParameter("roleId1");
		if ("".equals(roleId)) {
			model.setRoleId(null);
		}else{
			model.setRoleId(roleId);
		}
		//manager.saveRole(model);
		userService.saveRoleByHa(model, userService.getCurrentUserInfo().getOrgId());
		addActionMessage("保存成功");
		//return renderHtml("<script>window.dialogArguments.location='"
		//		+ getRequest().getContextPath()
			//	+ "/rolemanage/baseRole.action'; window.close();</script>");
		// return RELOAD;
		return "temp";
	}

	/**
	 * 比较编码唯一性
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-25 下午02:17:49
	 * @return String
	 * @throws Exception
	 */
	public String compareCode() throws Exception {
		boolean flag = userService.compareRoleCode(orgcode);
		if (flag == true) {
			message = "111";
		} else
			message = "000";
		return this.renderText(message);
	}

	public TUumsBaseRole getModel() {

		return model;
	}

	public List<TUumsBaseRole> getBaseRoleList() {
		return baseRoleList;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getRolePrivil() {
		return rolePrivil;
	}

	public void setRolePrivil(String rolePrivil) {
		this.rolePrivil = rolePrivil;
	}

	public String getPrivilIds() {
		return privilIds;
	}

	public void setPrivilIds(String privilIds) {
		this.privilIds = privilIds;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public String getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(String roleUsers) {
		this.roleUsers = roleUsers;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public void setUserList(List<TUumsBaseUser> userList) {
		this.userList = userList;
	}

	public List<TUumsBaseSystem> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<TUumsBaseSystem> systemList) {
		this.systemList = systemList;
	}

	public Map getUseMap() {
		return useMap;
	}

	public void setUseMap(Map useMap) {
		this.useMap = useMap;
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

	public String getSelectroleId() {
		return selectroleId;
	}

	public void setSelectroleId(String selectroleId) {
		this.selectroleId = selectroleId;
	}

	public String getSelectrolename() {
		return selectrolename;
	}

	public void setSelectrolename(String selectrolename) {
		this.selectrolename = selectrolename;
	}

	public String getRoleisact() {
		return roleisact;
	}

	public void setRoleisact(String roleisact) {
		this.roleisact = roleisact;
	}

	public String getRoledesc() {
		return roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	public String getSearchRoleId() {
		return searchRoleId;
	}

	public void setSearchRoleId(String searchRoleId) {
		this.searchRoleId = searchRoleId;
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
