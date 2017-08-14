package com.strongit.uums.security;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;

import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PrivilHelper;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 统一用户系统登录初始化action处理类
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Jan 4, 2009  11:04:27 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.uums.security.InitModuleTreeAction
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value ="baseRole.action", type = ServletActionRedirectResult.class),
			@Result(name = BaseActionSupport.SUCCESS, value = "/WEB-INF/jsp/frame/perspective_content/navigator_container/navigator_content.jsp", type = ServletDispatcherResult.class),
			@Result(name = "navigator", value = "/WEB-INF/jsp/frame/perspective_content/navigator_container/navigator_toolbar.jsp", type = ServletDispatcherResult.class),
			@Result(name = "perspective", value = "/WEB-INF/jsp/frame/perspective_toolbar.jsp", type = ServletDispatcherResult.class)})
public class InitModuleTreeAction extends BaseActionSupport {

	/**
	 * 系统模块权限集
	 */
	private List<TUumsBasePrivil> privilList;
	
	//委托资源权限集
	private List<TUumsBasePrivil> abrolePrivilList;
	
	/**
	 * 权限编码规则
	 */
	private String privilRuleCode;
	
	/**
	 * 当前外挂系统的系统编码
	 */
	private String sysSysCode;
	
	/**
	 * 当前用户登录名
	 */
	private String userLoginName;
	
	@Autowired IUserService userService;
	
	/**
	 * 当前用户中文名
	 */
	private String userName;
	
	// 当前登录用户选择的机构ID huzw
	private String currentOrgId = "";
	
	public String getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(String currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	public String getPrivilRuleCode() {
		return privilRuleCode;
	}

	public void setPrivilRuleCode(String privilRuleCode) {
		this.privilRuleCode = privilRuleCode;
	}

	public List<TUumsBasePrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<TUumsBasePrivil> privilList) {
		this.privilList = privilList;
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

	/**
	 * 系统模块树初始化
	 */
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		
		if(sysSysCode == null || "".equals(sysSysCode)){
			throw new Exception("请设置要展现模块信息的系统编码!");
		}
		
		UserInfo user = (UserInfo)this.getUserDetails();
		Date currentDate = new Date();
		userService.cancelAbroleBeforLogin(user.getUserId(), currentDate);
		userService.pauseAbroleBeforLogin(user.getUserId(), currentDate);
		
		privilRuleCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);

		GrantedAuthority[] au = user.getAuthorities();
		privilList = new ArrayList<TUumsBasePrivil>();
		abrolePrivilList = new ArrayList<TUumsBasePrivil>();
		TUumsBasePrivil privil;
		for(int i=0; i<au.length; i++){
			if(au[i].getAuthority().startsWith(sysSysCode)
					&& PrivilHelper.checkHas(au[i].getAuthority())){
				privil = PrivilHelper.getPrivil(au[i].getAuthority());
				/**
				 * 只取管理资源权限
				 */
				if(Const.PRIVIL_TYPE_MANAGE.equalsIgnoreCase(privil.getBasePrivilType().getTypeCategory())){
					if(user.getAbrolePrivilStr().indexOf(au[i].getAuthority()) != -1){
						privil.setRest4("1");
					}
					privilList.add(privil);
				}
			}
		}
		if(user.getAbrolePrivilStr() != null && !"".equals(user.getAbrolePrivilStr())){
			TUumsBasePrivil abrolePrivil = new TUumsBasePrivil();
			abrolePrivil.setPrivilName("委托权限");
			abrolePrivil.setPrivilSequence(Long.MAX_VALUE);
			String abroleCode = "";
			int i = Integer.valueOf(privilRuleCode.substring(0,1));
			for(int j=0; j<i; j++){
				abroleCode = abroleCode + "9";
			}
			abrolePrivil.setPrivilSyscode(abroleCode);
			privilList.add(abrolePrivil);
		}
		
		/**
		 * 修改日期：2009.02.27 修改人： 喻斌 修改内容：增加对权限结果集的排序
		 */
		java.util.Collections.sort(privilList,
				new Comparator<TUumsBasePrivil>() {
					public int compare(TUumsBasePrivil arg0,
							TUumsBasePrivil arg1) {
						Long key1, key2;
						if (arg0.getPrivilSequence() != null) {
							key1 = Long.valueOf(arg0
									.getPrivilSequence());
						} else {
							key1 = Long.MAX_VALUE;
						}
						if (arg1.getPrivilSequence() != null) {
							key2 = Long.valueOf(arg1
									.getPrivilSequence());
						} else {
							key2 = Long.MAX_VALUE;
						}
						return key1.compareTo(key2);
					}
				});
		return SUCCESS;
	}
	
	/**
	 * 得到当前登录用户的中文姓名和登录名
	 * @author  喻斌
	 * @date    Jan 9, 2009  11:30:46 AM
	 * @return
	 */
	public String currentinfo(){
		UserInfo user = (UserInfo)this.getUserDetails();
		userLoginName = user.getUsername();
		userName = user.getUserRealName();
		/**
		 * pageType 返回页面类型标识参数
		 */
		String pageType = this.getRequest().getParameter("pageType");
		/**
		 * 返回页面为perspective_toolbar.jsp
		 */
		if("perspective".equals(pageType)){
			return "perspective";
		/**
		 * 返回页面为navigator_toolbar.jsp
		 */
		}else if("navigator".equals(pageType)){
			return "navigator";
		/**
		 * 返回页面为perspective_toolbar.jsp
		 */
		}else{
			return "perspective";
		}
	}

	/**
	 * 切换机构 huzw
	 * <br>
	 * creator: 胡志文 <br>
	 * create date: Apr 18, 2012
	 * @return
	 * @throws Exception
	 */
	public String changeOrg() throws Exception{
		try {
			currentOrgId = getRequest().getParameter("currentOrgId");
			UserInfo userInfo = (UserInfo) this.getUserDetails();
			userInfo.setOrgId(currentOrgId);
			userInfo.setSupOrgCode(userService.getOrgInfoByOrgId(currentOrgId).getSupOrgCode());
			TUumsBaseUser user = userService.getUserInfoByUserId(userInfo.getUserId());
			
			List<String> authsList = new ArrayList<String>(0);

			/**
			 * 获取当前登录用户的所有分级授权权限
			 */
			List<TUumsBasePrivil> privils = userService.getPrivilInfosByUserLoginNameByHa(user.getUserLoginname(),
							Const.IS_YES, Const.IS_YES, Const.IS_YES,currentOrgId);
			if (privils != null && !privils.isEmpty()) {
				for (TUumsBasePrivil privil : privils) {
					/**
					 * 唯一确定一个权限的编码为：权限所属系统编码-权限编码 未启用权限过滤
					 */
					authsList.add(privil
							.getBasePrivilType().getBaseSystem().getSysSyscode()
							+ "-" + privil.getPrivilSyscode());
				}
				privils.clear();
			}
			// 获取当前登录用户的所有委托权限
			privils = userService.getAbrolePrivilByUserLoginnameByHa(user
					.getUserLoginname(), Const.IS_YES,currentOrgId);
			if (privils != null && !privils.isEmpty()) {
				for (TUumsBasePrivil privil : privils) {
					/**
					 * 唯一确定一个权限的编码为：权限所属系统编码-权限编码 未启用权限过滤
					 */
					String pl = privil.getBasePrivilType().getBaseSystem()
									.getSysSyscode()
									+ "-" + privil.getPrivilSyscode();
					if (!authsList.contains(pl)) {
						authsList.add(pl);
					}
				}
			}
			userService.modifyCurrentUserPrivil(authsList);
			renderText("");
		}
		catch (Exception e) {
			e.printStackTrace();
			renderText(e.getMessage());
		}
		return null;
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
		return null;
	}

	public String getSysSysCode() {
		return sysSysCode;
	}

	public void setSysSysCode(String sysSysCode) {
		this.sysSysCode = sysSysCode;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<TUumsBasePrivil> getAbrolePrivilList() {
		return abrolePrivilList;
	}

	public void setAbrolePrivilList(List<TUumsBasePrivil> abrolePrivilList) {
		this.abrolePrivilList = abrolePrivilList;
	}
}
