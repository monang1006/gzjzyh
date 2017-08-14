package com.strongit.oa.viewmodel;

import java.util.List;

import com.strongit.oa.bo.ToaDesktopPortal;
import com.strongit.oa.bo.ToaForamula;

public interface IModelPrivalService {

	/**
	 * 获取所有组织机构信息
	 * @author zhengzb
	 * @desc 
	 * 2011-1-18 下午03:04:49 
	 * @return
	 */
	public List<Object[]> getAllOrgList();
	
	/**
	 * 获取所有岗位
	 * @author zhengzb
	 * @desc 
	 * 2011-1-18 下午03:05:55 
	 * @return
	 */
	public List<Object[]> getAllOrgPostList();
	
	/**
	 * 获取所有已启动的角色
	 * @author zhengzb
	 * @desc 
	 * 2011-1-18 下午03:43:21 
	 * @return
	 */
	public List<Object[]> getAllOrgRoleList();
	
	/**
	 * 获取所有人员列表
	 * @author zhengzb
	 * @desc 
	 * 2011-1-18 下午04:05:06 
	 * @return
	 */
	public List<Object[]> getAllOrgUserList();
	
	/**
	 * 根据用户ID获取首页权限ID
	 * @author zhengzb
	 * @desc 
	 * 2011-1-21 上午09:15:13 
	 * @param userId
	 * @return
	 */
	public ToaForamula getForamulaIdByUserId(String userId);
	
	/**
	 * 根据用户ID获取门户权限
	 * @author zhengzb
	 * @desc 
	 * 2011-1-25 下午04:44:37 
	 * @param userId
	 * @return
	 */
	public List<ToaDesktopPortal> getDesktopProtaList (String userId);
	
	
	public void deleteByPortalId (String portalId);
	
	

}
