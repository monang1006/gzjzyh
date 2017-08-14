package com.strongit.uums.security.usernameandpassword;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.IUserService;

/**
 * 
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Sep 19, 2011
 * @classpath	com.strongit.uums.security.usernameandpassword.UserNameAndPasswordUserDetailServiceImpl
 * @version  	3.0.2
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public class UserNameAndPasswordUserDetailServiceImpl implements
		UserDetailsService {
	@Autowired IUserService userService;

	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		TUumsBaseUser user = this.userService.getUserInfoByLoginName(userName);
		if (user == null)
			throw new UsernameNotFoundException(userName + "没有找到!");
		if ("1".equals(user.getUserIsdel()))
			throw new UsernameNotFoundException(user.getUserName() + "已经被删除!");
		if ("0".equals(user.getUserIsactive()))
			throw new UsernameNotFoundException(user.getUserName() + "已经被禁用!");
		if (user.getUserIsSupManager() != null) {
			"0".equals(user.getUserIsSupManager());
		}

		List authsList = new ArrayList();

		List<TUumsBasePrivil> privils = userService.getPrivilInfosByUserLoginName(user
				.getUserLoginname(), "1", "1", "1");
		if (privils != null) {
			for (TUumsBasePrivil privil : privils) {
				if ("1".equals(privil.getPrivilIsactive())) {
					authsList.add(new GrantedAuthorityImpl(privil
							.getBasePrivilType().getBaseSystem()
							.getSysSyscode()
							+ "-" + privil.getPrivilSyscode()));
				}
			}
			privils.clear();
		}

		StringBuffer abrolePrivilStr = new StringBuffer("");

		privils = userService.getAbrolePrivilByUserLoginname(user
				.getUserLoginname(), "1");
		if ((privils != null) && (!(privils.isEmpty()))) {
			for (TUumsBasePrivil privil : privils) {
				GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(
						privil.getBasePrivilType().getBaseSystem()
								.getSysSyscode()
								+ "-" + privil.getPrivilSyscode());
				if (!(authsList.contains(grantedAuthority))) {
					authsList.add(grantedAuthority);
					abrolePrivilStr.append(",").append(
							grantedAuthority.getAuthority());
				}
			}
			abrolePrivilStr.append(",");
		}

		UserInfo userInfo = new UserInfo(user.getUserLoginname(), user
				.getUserPassword(), true, (GrantedAuthority[]) authsList
				.toArray(new GrantedAuthority[authsList.size()]));

		userInfo.setUserPassword(user.getUserPassword());
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

		return userInfo;
	}
}
