package com.strongit.uums.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.util.Const;
import com.strongit.xxbs.common.contant.Publish;

public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired IUserService userService;

	public String message = "";

//	@UUMSLogger(description = "")
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
//		message = "用户登录";
		TUumsBaseUser user = userService.getUserInfoByLoginName(userName);
		if (user == null) {
			throw new UsernameNotFoundException(userName + "没有找到!");
		} else if (Const.IS_YES.equals(user.getUserIsdel())) {
			throw new UsernameNotFoundException(userName + "已经被删除!");
		} else if (Const.IS_NO.equals(user.getUserIsactive())) {
			throw new UsernameNotFoundException(userName + "已经被禁用!");
		} else if (user.getUserIsSupManager() == null
				|| Const.IS_NO.equals(user.getUserIsSupManager())) {
			/*
			 * if (user.getBaseSysmanagers() == null ||
			 * user.getBaseSysmanagers().size() == 0) { throw new
			 * UsernameNotFoundException(userName + "不具有管理员以上权限,无法登录本系统!"); }
			 */
		}

		List<GrantedAuthority> authsList = new ArrayList<GrantedAuthority>(0);

		/**
		 * 获取当前登录用户的所有权限
		 */
		List<TUumsBasePrivil> privils = userService.getPrivilInfosByUserLoginName(user.getUserLoginname(),
						Const.IS_YES, Const.IS_YES, Const.IS_YES);
		if (privils != null && !privils.isEmpty()) {
			for (TUumsBasePrivil privil : privils) {
				/**
				 * 唯一确定一个权限的编码为：权限所属系统编码-权限编码 未启用权限过滤
				 */
				authsList.add(new GrantedAuthorityImpl(privil
						.getBasePrivilType().getBaseSystem().getSysSyscode()
						+ "-" + privil.getPrivilSyscode()));
			}
			privils.clear();
		}
		StringBuffer abrolePrivilStr = new StringBuffer("");
		// 获取当前登录用户的所有委托权限
		privils = userService.getAbrolePrivilByUserLoginname(user
				.getUserLoginname(), Const.IS_YES);
		if (privils != null && !privils.isEmpty()) {
			for (TUumsBasePrivil privil : privils) {
				/**
				 * 唯一确定一个权限的编码为：权限所属系统编码-权限编码 未启用权限过滤
				 */
				GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(
						privil.getBasePrivilType().getBaseSystem()
								.getSysSyscode()
								+ "-" + privil.getPrivilSyscode());
				if (!authsList.contains(grantedAuthority)) {
					authsList.add(grantedAuthority);
					abrolePrivilStr.append(",").append(
							grantedAuthority.getAuthority());
				}
			}
			abrolePrivilStr.append(",");
		}

		UserInfo userInfo = new UserInfo(user.getUserLoginname(), user
				.getUserPassword(), true, authsList
				.toArray(new GrantedAuthority[authsList.size()]));

		/**
		 * 将用户当前用户信息保存到安全上下文中，以方便系统获取
		 */
		userInfo.setOrgId(user.getOrgId());
		userInfo.setRest1(user.getRest1());
		userInfo.setRest2(user.getRest2());
		userInfo.setRest3(user.getRest3());
		userInfo.setRest4(user.getRest4());
		userInfo.setUserId(user.getUserId());
		userInfo.setUserEmail(user.getUserEmail());
		userInfo.setUserIsactive(user.getUserIsactive());
		userInfo.setUserRealName(user.getUserName());
		userInfo.setUserSyscode(user.getUserSyscode());
		userInfo.setUserAddr(user.getUserAddr());
		userInfo.setUserTel(user.getUserTel());
		userInfo.setUserPubkey(user.getUserPubkey());
		userInfo.setUserUsbkey(user.getUserUsbkey());
		userInfo.setUserIsSupManager(user.getUserIsSupManager());
		userInfo.setAbrolePrivilStr(abrolePrivilStr.toString());
		userInfo.setSupOrgCode(user.getSupOrgCode());
		List<TUumsBaseRole> role = userService.getUumsBaseRoleInfosByUserId(user.getUserId(), "1");
		userInfo.setRole(role);
		if(role!=null){
		for(int i=0;i<role.size();i++){
			if((Publish.ROLE_SUBMITTER.equals(role.get(i).getRoleSyscode()))||(Publish.ROLE_SUBMITTER_NEXT.equals(role.get(i).getRoleSyscode()))){
				userInfo.setIsSubmit(true);
			}
			if(Publish.ROLE_YHGLY_ID.equals(role.get(i).getRoleSyscode())){
				userInfo.setIsAdmin(true);
			}
		}
		}
//		if (Const.LOGIN_SYS_NUM < 0) {
//			Const.LOGIN_SYS_NUM = 0;
//		}
//		++Const.LOGIN_SYS_NUM;// 统计登录的人数
//		System.out.println("当前登录人数为：" + Const.LOGIN_SYS_NUM);
//		
////		String nowDate = TimeKit.formatDate(new Date(), "long");
//		String nowDate = TimeKit.now("long");
//		userInfo.setLoginDate(nowDate);
//		Const.userMap.put(userInfo.getUserId(), userInfo);
//		Map<String,Object> map = Const.userMap;
//		System.out.println("----------------------");
		return userInfo;
	}

}