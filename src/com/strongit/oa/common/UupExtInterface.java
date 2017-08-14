package com.strongit.oa.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.uumsinterface.IUumsInterface;

@Service
@Transactional
public class UupExtInterface
{
	@Autowired
	private IUumsInterface uumsInterface;

	
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

}
