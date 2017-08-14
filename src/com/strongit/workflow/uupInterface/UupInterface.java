package com.strongit.workflow.uupInterface;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.uums.uumsinterface.IUumsInterface;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePost;
import com.strongit.uums.bo.TUumsBasePostUser;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.workflow.dto.uup.UupPost;
import com.strongit.workflow.po.UserInfo;
import com.strongmvc.exception.SystemException;

/**
 * 统一用户构件调用接口代理
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Dec 9, 2008  3:11:25 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.uupInterface.UupInterface
 */
@Service
@Transactional
public class UupInterface implements IUupInterface{
	
	private IUumsInterface uumsInterface;
	
	@Autowired IUserService userService;

	@Autowired
	public void setUumsInterface(IUumsInterface uumsInterface){
		this.uumsInterface = uumsInterface;
	}
	
	/**
	 * added by yubin on 2008.10.17<br>
	 * 	<p>得到指定Id用户的岗位列表
	 * @param userId 用户Id
	 * @return List<String[]> 岗位信息<br>
	 * 		<p>岗位信息数据结构:<br>
	 * 		<p>String[]{岗位所属组织机构Id, 岗位Id, 岗位名称}
	 */
	public List<String[]> getPostOrgList(String userId){
		List<String[]> returnList = new ArrayList<String[]>();
		//List<TUumsBasePost> list = uumsInterface.getPostInfoByUserId(userId);
		//根据用户所有的机构查询出所有的岗位信息 by qibh 2014-03-04
		TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(userId);
		String[] orgidsss = uumsuer.getOrgIds().split(",");
		List<String> orgIds = new ArrayList<String>();
		for(int k=0;k<orgidsss.length;k++){
			String orgId = orgidsss[k];
			if (orgId != null&&!"".equals(orgId)) {
				orgIds.add(orgId);
			}
		}
		List<TUumsBasePostUser> list = uumsInterface.getPostUsersdByUserAndOrgIds(userId, orgIds);
		//TUumsBaseOrg orgInfo = uumsInterface.getOrgInfoByUserId(userId);
		for(TUumsBasePostUser post : list){
			returnList.add(new String[]{String.valueOf(post
					.getOrgId()), String.valueOf(post.getBasePost().getPostId())
					, post.getBasePost().getPostName()});
		}
		return returnList;
	}

	
	/**
	 * added by yubin on 2008.10.17<br>
	 * 	<p>得到当前用户信息
	 * @return com.strongit.workflow.po.UserInfo;
	 */
	public com.strongit.workflow.po.UserInfo getCurrentUserInfo(){
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			com.strongit.uums.security.UserInfo userInfo = (com.strongit.uums.security.UserInfo) authentication
					.getPrincipal();

			com.strongit.workflow.po.UserInfo retUser = new com.strongit.workflow.po.UserInfo();
			retUser.setUserId(userInfo.getUserId());
			retUser.setUserLonginName(userInfo.getUsername());
			retUser.setUserName(userInfo.getUserRealName());

			return retUser;
		} catch (Exception e) {
			String userId = userService.getUserId();
			if(userId != null) {
				UserInfo user = new UserInfo();
				TUumsBaseUser userInfo = uumsInterface.getUserInfoByUserId(userId);
				user.setUserId(userId);
				user.setUserLonginName(userInfo.getUserLoginname());
				user.setUserName(userInfo.getUserName());
				user.setOrganizationId(userInfo.getOrgId());
				return user;
			}
			throw new SystemException("很抱歉，会话过程已结束，请您重新登录。");
		}
	}
	
	/**
	 * added by yubin on 2008.10.17<br>
	 * 	<p>得到指定Id用户的组织机构信息
	 * @param userId 用户Id
	 * @return List<String[]> 组织机构信息<br>
	 * 		<p>组织机构数据结构：<br>
	 * 		<p>String[]{组织机构Id, 组织机构名称}
	 */
	public List<String[]> getUserOrgs(String userId){
		TUumsBaseOrg baseOrg = uumsInterface.getOrgInfoByUserId(userId);
		String[] org = new String[]{baseOrg.getOrgId(), baseOrg.getOrgName()};
		List<String[]> list = new ArrayList<String[]>();
		list.add(org);
		return list;
	}
	
	/**
	 * added by yubin on 2008.10.19<br>
	 * 	<p>由指定Id用户得到组织机构Id
	 * @param userId 用户Id
	 * @return List<String> 组织机构Id信息<br>
	 * 		<p>数据结构：String
	 */
	public List<String> getDepartmentByUser(String userId){
		List<String> orgId = new ArrayList<String>();
		TUumsBaseOrg baseOrg = uumsInterface.getOrgInfoByUserId(userId);
		if(baseOrg != null){
			orgId.add(baseOrg.getOrgId());			
		}
		return orgId;
	}
	
	/**
	 * added by yubin on 2008.10.19<br>
	 * 	<p>由组织机构Id得到组织机构负责人Id
	 * @param orgId 组织机构Id
	 * @return String 组织机构负责人Id
	 */
	public String getDepartmentManager(String orgId){
		/*TUumsBaseUser user = uumsInterface.getOrgManagerByOrgId(orgId);
		return user == null?null:user.getUserId();*/
		TUumsBaseOrg org = uumsInterface.getOrgInfoByOrgId(orgId);
		if(org != null) {
			return org.getOrgManager();
		}
		return null;
	}
	
	/**
	 * added by yubin on 2008.10.19<br>
	 * 	<p>由组织机构Id得到上级组织机构Id
	 * @param orgId 组织机构Id
	 * @return String 上级组织机构Id<br>
	 * 		<p>最顶级组织机构的父级Id为该组织机构本身Id
	 */
	public String getParentDepart(String orgId){
		TUumsBaseOrg org = uumsInterface.getParentOrgByOrgId(orgId);
		if(org == null){
			return orgId;
		}else{
			return org.getOrgId();
		}
	}
	
	/**
	 * added by yubin on 2008.10.18<br>
	 * 	<p>得到指定Id用户所属组织机构负责人Id
	 * @param userId 用户Id
	 * @return String 组织机构负责人Id
	 */
	public String getDepartManagerByUser(String userId){
		String orgId = this.getDepartmentByUser(userId).get(0);
		if(orgId != null){
			return this.getDepartmentManager(orgId);
		}else{
			return null;
		}
	}
	
	/**
	 * added by yubin on 2008.10.18<br>
	 * 	<p>得到指定Id用户的上级组织负责人Id
	 * @param userId 用户Id
	 * @return String 上级组织负责人Id
	 */
	public String getParentDepartByUser(String userId){
		String parentUserId = null;
		String orgId = this.getDepartmentByUser(userId).get(0);
		if(orgId != null){
			String parentOrgId = this.getParentDepart(orgId);
			if(parentOrgId != null){
				parentUserId = this.getDepartmentManager(parentOrgId);
			}
		}
		return parentUserId;
	}
	
	/**
	 * added by yubin on 2008.10.18<br>
	 * 	<p>由用户Id得到用户名称
	 * @param userId 用户Id
	 * @return String 用户名称
	 */
	public String getUsernameById(String userId){
		TUumsBaseUser user = uumsInterface.getUserInfoByUserId(userId);
		if(user != null){
			return user.getUserName();
		}
		return null;
	}
	
	/**
	 * 获取所有组织机构信息
	 * @author  喻斌
	 * @date    Jan 13, 2009  2:35:11 PM
	 * @return List<String[]> 组织机构信息<br>
	 * 		<p>组织机构信息数据机构：<br>
	 * 		<p>String[]{组织机构Id, 父级组织机构Id, 组织机构名称}<br>
	 * 		<p>最顶级组织机构父级Id为”0“
	 */
	public List<String[]> getAllOrgList(){
		String parentOrgId;
		List<String[]> returnList = new ArrayList<String[]>();
		List<TUumsBaseOrg> orgList = uumsInterface.getAllOrgInfoByHa(uumsInterface.getCurrentUserInfo().getOrgId());
		for(TUumsBaseOrg org : orgList){
			/**
			 * 如果此组织机构已是最顶级，则其父级Id为”0“
			 */
			parentOrgId = org.getOrgParentId();
			if(parentOrgId == null || "".equals(parentOrgId)) {
				parentOrgId = "0";
			}
			/*parentOrgId = this.getParentDepart(org.getOrgId());
			if(org.getOrgId().equals(parentOrgId)){
				parentOrgId = "0";
			}*/
			returnList.add(new String[]{org.getOrgId(), parentOrgId, org.getOrgName()});
		}
		return returnList;
	}
	
	/**
	 * 根据组织机构Id得到该组织机构下的人员信息<br>
	 * 	<p></p>
	 * @author  喻斌
	 * @date    Feb 3, 2009  3:48:51 PM
	 * @param orgId -组织机构Id
	 * @return List<String[]> 人员信息集<br>
	 * 		<p>数据结构为：</p>
	 * 		<p>String[]{人员Id, 人员名称}</p>
	 */
	@SuppressWarnings("unchecked")
	public List<String[]> getUserInfoByOrgId(String orgId){
		List<TUumsBaseUser> userList = uumsInterface.getUserInfoByOrgId(orgId);
		/*List<Object[]> orgList = this.getAllOrgUserList();
		for(Object[] orgInfo : orgList){
			if(String.valueOf(orgInfo[0]).equals(orgId)){
				return (List<String[]>)orgInfo[3];
			}
		}
		return new ArrayList<String[]>();*/
		List<String[]> result = new ArrayList<String[]>();
		if(userList != null && !userList.isEmpty()) {
			for(TUumsBaseUser baseUser : userList) {
				result.add(new String[]{baseUser.getUserId(),baseUser.getUserName()});
			}
		}
		return result;
	}
	
	/**
	 * 根据人员Id得到人员相应的信息
	 * @author 喻斌
	 * @date Jun 29, 2009 9:17:51 AM
	 * @param userId -人员Id
	 * @return List<String[]> 人员信息<br>
	 * 		<p>String[]{人员Id,人员名称,人员所属组织机构Id}</p>
	 */
	public List<String[]> getUserInfoById(String userId){
		List<String[]> userLst = new ArrayList<String[]>();
		TUumsBaseUser user = uumsInterface.getUserInfoByUserId(userId);
		if(user != null){
			userLst.add(new String[]{user.getUserId(),
					user.getUserName(), user.getOrgId()});
		}
		return userLst;
	}
	
	
	/**
	 * 得到所有组织机构及其下人员信息列表
	 * @author  喻斌
	 * @date    Jan 13, 2009  2:11:29 PM
	 * @return List<Object[]> 信息列表<br>
	 * 		<p>信息列表数据结构：<br>
	 * 		<p>Object[]{组织机构Id, 父级组织机构Id, 组织机构名称, 组织机构下人员信息}<br>
	 * 		<p>最顶级组织机构父级Id为”0“<br>
	 * 		<p>人员信息数据机构：<br>
	 * 		<p>userList<String[]{人员Id, 人员名称}>
	 */
	public List<Object[]> getAllOrgUserList(){
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		List<String[]> orgUserList;
		String orgId = uumsInterface.getCurrentUserInfo().getOrgId();
		List<TUumsBaseOrg> orgList = uumsInterface.getAllOrgInfoByHa(orgId);
		List<TUumsBaseUser> userList = null;
		try {
			userList = uumsInterface.getAllUserInfoByHa(orgId);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		if(orgList != null && !orgList.isEmpty()){
			for(TUumsBaseOrg org : orgList){
				orgUserList = new ArrayList<String[]>();
				for(int i=0; i<userList.size();){
					TUumsBaseUser user = userList.get(i);
					if(org.getOrgId().equals(user.getOrgId())){
						orgUserList.add(new String[]{user.getUserId(), user.getUserName()});
						userList.remove(i);
						continue;
					}
					i++;
				}
				/**
				 * 如果此组织机构已是最顶级，则其父级Id为”0“
				 */
				/*parentOrgId = this.getParentDepart(org.getOrgId());
				if(org.getOrgId().equals(parentOrgId)){
					parentOrgId = "0";
				}*/		
				parentOrgId = org.getOrgParentId();
				if(parentOrgId == null || "".equals(parentOrgId)) {
					parentOrgId = "0";
				}
				returnList.add(new Object[]{org.getOrgId(), parentOrgId
						, org.getOrgName(), orgUserList});
				
			}
		}
		return returnList;
	}
	
	/**
	 * 得到所有组织机构及其下人员信息列表
	 * @author  喻斌
	 * @date    Jan 13, 2009  2:11:29 PM
	 * @return List<Object[]> 信息列表<br>
	 * 		<p>信息列表数据结构：<br>
	 * 		<p>Object[]{组织机构Id, 父级组织机构Id, 组织机构名称, 组织机构下人员信息}<br>
	 * 		<p>最顶级组织机构父级Id为”0“<br>
	 * 		<p>人员信息数据机构：<br>
	 * 		<p>userList<String[]{人员Id, 人员名称}>
	 */
	public List<Object[]> getAllOrgUserListForGlobalWorkflow(){
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		List<String[]> orgUserList;
		List<TUumsBaseOrg> orgList = uumsInterface.getAllOrgInfo();
		List<TUumsBaseUser> userList = null;
		try {
			userList = uumsInterface.getAllUserInfo();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		if(orgList != null && !orgList.isEmpty()){
			for(TUumsBaseOrg org : orgList){
				orgUserList = new ArrayList<String[]>();
				for(int i=0; i<userList.size();){
					TUumsBaseUser user = userList.get(i);
					if(org.getOrgId().equals(user.getOrgId())){
						orgUserList.add(new String[]{user.getUserId(), user.getUserName()});
						userList.remove(i);
						continue;
					}
					i++;
				}
				/**
				 * 如果此组织机构已是最顶级，则其父级Id为”0“
				 */
				/*parentOrgId = this.getParentDepart(org.getOrgId());
				if(org.getOrgId().equals(parentOrgId)){
					parentOrgId = "0";
				}*/		
				parentOrgId = org.getOrgParentId();
				if(parentOrgId == null || "".equals(parentOrgId)) {
					parentOrgId = "0";
				}
				returnList.add(new Object[]{org.getOrgId(), parentOrgId
						, org.getOrgName(), orgUserList});
			}
		}
		return returnList;
	}
	
	/**
	 * 得到所有组织机构及其下岗位信息列表
	 * @author  喻斌
	 * @date    Jan 13, 2009  2:11:29 PM
	 * @return List<Object[]> 信息列表<br>
	 * 		<p>信息列表数据结构：<br>
	 * 		<p>Object[]{组织机构Id, 父级组织机构Id, 组织机构名称, 组织机构下岗位信息}<br>
	 * 		<p>最顶级组织机构父级Id为”0“<br>
	 * 		<p>岗位信息数据机构：<br>
	 * 		<p>postList<String[]{岗位Id, 岗位名称}>
	 */
	public List<Object[]> getAllOrgPostList(){
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		List<String[]> orgPostList;
		List<TUumsBaseOrg> orgList = uumsInterface.getAllOrgInfoByHa(uumsInterface.getCurrentUserInfo().getOrgId());
		List<Object[]> postList = uumsInterface.getAllOrgPostInfoByHa(uumsInterface.getCurrentUserInfo().getOrgId());
		if(orgList != null && !orgList.isEmpty()){
			for(TUumsBaseOrg org : orgList){
				orgPostList = new ArrayList<String[]>();
				for(int i=0; i<postList.size();){
					Object[] postInfo = postList.get(i);
					if(org.getOrgId().equals(postInfo[2])){
						orgPostList.add(new String[]{String.valueOf(postInfo[0])
								, String.valueOf(postInfo[1])});
						postList.remove(i);
						continue;
					}
					i++;
				}
				/**
				 * 如果此组织机构已是最顶级，则其父级Id为”0“
				 */
				/*parentOrgId = this.getParentDepart(org.getOrgId());
				if(org.getOrgId().equals(parentOrgId)){
					parentOrgId = "0";
				}*/	
				parentOrgId = org.getOrgParentId();
				if(parentOrgId == null || "".equals(parentOrgId)) {
					parentOrgId = "0";
				}
				returnList.add(new Object[]{org.getOrgId(), parentOrgId
						, org.getOrgName(), orgPostList});
			}
		}
		return returnList;
	}
	
	/**
	 * 得到所有组织机构及其下岗位信息列表
	 * @author  喻斌
	 * @date    Jan 13, 2009  2:11:29 PM
	 * @return List<Object[]> 信息列表<br>
	 * 		<p>信息列表数据结构：<br>
	 * 		<p>Object[]{组织机构Id, 父级组织机构Id, 组织机构名称, 组织机构下岗位信息}<br>
	 * 		<p>最顶级组织机构父级Id为”0“<br>
	 * 		<p>岗位信息数据机构：<br>
	 * 		<p>postList<String[]{岗位Id, 岗位名称}>
	 */
	public List<Object[]> getAllOrgPostListForGlobalWorkflow(){
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		List<String[]> orgPostList;
		List<TUumsBaseOrg> orgList = uumsInterface.getAllOrgInfo();
		List<Object[]> postList = uumsInterface.getAllOrgPostInfo();
		if(orgList != null && !orgList.isEmpty()){
			for(TUumsBaseOrg org : orgList){
				orgPostList = new ArrayList<String[]>();
				for(int i=0; i<postList.size();){
					Object[] postInfo = postList.get(i);
					if(org.getOrgId().equals(postInfo[2])){
						orgPostList.add(new String[]{String.valueOf(postInfo[0])
								, String.valueOf(postInfo[1])});
						postList.remove(i);
						continue;
					}
					i++;
				}
				/**
				 * 如果此组织机构已是最顶级，则其父级Id为”0“
				 */
				/*parentOrgId = this.getParentDepart(org.getOrgId());
				if(org.getOrgId().equals(parentOrgId)){
					parentOrgId = "0";
				}*/	
				parentOrgId = org.getOrgParentId();
				if(parentOrgId == null || "".equals(parentOrgId)) {
					parentOrgId = "0";
				}
				returnList.add(new Object[]{org.getOrgId(), parentOrgId
						, org.getOrgName(), orgPostList});
			}
		}
		return returnList;
	}
	
	/**
	 * added by yubin on 2008.10.18<br>
	 * 	<p>由岗位Id和组织机构Id得到用户信息
	 * @param userId 用户Id
	 * @return List<String[]> 用户信息<br>
	 * 		<p>用户信息数据结构：<br>
	 * 		<p>String[]{用户Id, 用户姓名}
	 */
	public List<String[]> getUserByPostAndOrg(String postId, String orgId){
		List<String[]> returnList = new ArrayList<String[]>();
		List<TUumsBaseUser> userList = uumsInterface.getUserInfoByOrgAndPostId(orgId, postId);
		if(userList != null && !userList.isEmpty()){
			for(TUumsBaseUser user : userList){
				returnList.add(new String[]{
					user.getUserId(),
					user.getUserName()
				});
			}
		}
		return returnList;
	}

	public List<UupPost> getPostByUser(String userId)
	{
		Assert.hasLength(userId);
		
		List<UupPost> rets = new ArrayList<UupPost>();
		
		List<TUumsBasePost> list = uumsInterface.getPostInfoByUserId(userId);
		TUumsBaseOrg orgInfo = uumsInterface.getOrgInfoByUserId(userId);
		for(TUumsBasePost post : list){
			rets.add(new UupPost(
					String.valueOf(orgInfo.getOrgId()),
					String.valueOf(post.getPostId()),
					post.getPostName()));
		}

		return rets;
	}

	public void setUserInfoByLogin(String arg0)
	{
	}

}