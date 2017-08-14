package com.strongit.oa.viewmodel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDesktopPortal;
import com.strongit.oa.bo.ToaForamula;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Role;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.DesktopPortalManager;
import com.strongit.oa.desktop.ProtalPrivalManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.viewmodel.IModelPrivalService;
import com.strongit.oa.viewmodel.ModelPrivalManager;
import com.strongit.oa.viewmodel.ViewModelManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;

/**
 * 实现门户权限接口
 *@author zhengzb
 *@desc  
 *@date 2011-1-17 下午03:03:18
 */

@Service
@Transactional
@OALogger
public class ModelPrivalService implements IModelPrivalService {
	
	private IUserService userService;
	
	private ModelPrivalManager manager;						//权限模块MANAGER
	
	private ViewModelManager viewModelManager;					//首页模块MANAGER
	
	private ProtalPrivalManager protalPrivalManager;			//门户管理权限MANAGER

	private DesktopPortalManager desktopPortalManager;			//门户管理MANAGER
	
	public List<Object[]>getAllOrgList(){	
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		try {
			List<TUumsBaseOrg> orgList;
			String superCode;
			String userId = userService.getCurrentUser().getUserId();
			TUumsBaseOrg org1 = userService.getSupOrgByUserIdByHa(userId);
			if(org1.getIsOrg().equals("0")){
				superCode=org1.getSupOrgCode();
			}else{
				superCode=org1.getOrgSyscode();
			}
			String haid=org1.getOrgId();
			orgList=userService.getAllOrgInfoByHa(haid);
			if(orgList != null && !orgList.isEmpty()){
				for(TUumsBaseOrg org : orgList){
					if(org.getIsOrg()!=null&&org.getIsOrg().equals("1")){
						if(org.getOrgParentId()==null||org.getOrgParentId().equals("null")){
							parentOrgId = "0";
						}else{			
							parentOrgId=org.getOrgParentId();
						}
						returnList.add(new Object[]{org.getOrgId(), parentOrgId
								, org.getOrgName()});
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"得到所有组织机构信息列表"});
		}	
		return returnList;
	}
	/**
	 * 获取所有组织机构部门
	 */
	public List<Object[]> getAllOrgAndDepaList(){
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		try{
			List<TUumsBaseOrg> orgList;
			String superCode;
			String userId = userService.getCurrentUser().getUserId();
			TUumsBaseOrg org1 = userService.getSupOrgByUserIdByHa(userId);
			if(org1.getIsOrg().equals("0")){
				superCode=org1.getSupOrgCode();
			}else{
				superCode=org1.getOrgSyscode();
			}
			String haid=org1.getOrgId();
			orgList=userService.getAllOrgInfoByHa(haid);

			
			if(orgList != null && !orgList.isEmpty()){
				for(TUumsBaseOrg org : orgList){
//					parentOrgId = this.getParentDepart( org.getOrgId(),org.getOrgSyscode());
					if(org.getOrgParentId()==null||org.getOrgParentId().equals("null")){
						parentOrgId = "0";
					}else{			
						parentOrgId=org.getOrgParentId();
					}
					returnList.add(new Object[]{org.getOrgId(), parentOrgId
							, org.getOrgName()});
				}
			}
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"得到所有组织机构部门信息列表"});
		}	
		return returnList;
	}



	
	/**
	 * 获取所有机构下岗位信息列表
	 */
	public List<Object[]> getAllOrgPostList(){
		List<Object[]> returnList = new ArrayList<Object[]>();
		try {
			 Map<String,List<String[]>> pastMap=userService.getPostMap();
			 List<Object[]> orgList=getAllOrgAndDepaList();
			 if(pastMap!=null&&pastMap.isEmpty()&&orgList!=null&&orgList.size()>0){
				 for(Object[] obj:orgList){
					 List<String[]> postList=pastMap.get(obj[0].toString());
					
						 returnList.add(new Object[]{obj[0].toString(), obj[1].toString(), obj[2].toString(),postList});
					     
					 
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"得到所有组织机构下岗位信息列表"});
		}
		return returnList;
	}
	
	/**
	 * 获取所有角色信息
	 */
	public List<Object[]> getAllOrgRoleList(){
		List<Object[]> returnList = new ArrayList<Object[]>();
		try {
			 List<Role> roleList=userService.getAllUsedRoleInfos("1");
			 if(roleList!=null&&roleList.size()>0){
				 for (int i = 0; i < roleList.size(); i++) {
					Role role=roleList.get(i);
					returnList.add(new Object[]{role.getRoleId(), "0", role.getRoleName()});					
				}
			 }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"得到所有角色信息列表"});
		}
		return returnList;
	}
	
	/**
	 * 获取所有选择人员列表
	 */
	public List<Object[]> getAllOrgUserList(){
		List<Object[]> returnList = new ArrayList<Object[]>();
		try {
			 List<Object[]> orgList=getAllOrgAndDepaList();
			 List<User> userList=userService.getAllUserInfo();
			 List<String[]> orgUserList;
			 for(int i=0;i<orgList.size();i++){
				 Object[] obj=orgList.get(i);
				 orgUserList=new ArrayList<String[]>();
				 for (int j = 0; j < userList.size(); j++) {
					User user=userList.get(j);					
					if(user!=null&&user.getOrgId().equals(obj[0])){
						orgUserList.add(new String[]{user.getUserId(), user.getUserName()});
						continue;
					}					
				}
				 returnList.add(new Object[]{obj[0].toString(), obj[1].toString(), obj[2].toString(),orgUserList});
				 
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"得到所有人员选择列表"});
		}
		return returnList;
	}
	
	/**
	 * 根据用户ID判断当前用户是否设置了首页模块权限
	 */
	public ToaForamula getForamulaIdByUserId(String userId){
		ToaForamula foramula;
		try {
			String foramulaId=manager.getForamulaIdByUserId(userId);					//根据用户ID判断当前用户是否设置了首页模块权限
			if("".equals(foramulaId)||"null".equals(foramulaId)||null==foramulaId){											//当前用户没有设置首页权限，将默认首页ID
				foramula =viewModelManager.getDefaultToaForamula();				
				
			}else{
				foramula=viewModelManager.getObjById(foramulaId);
				if(foramula.getIsCreatePage()==null||foramula.getIsCreatePage().equals("0")||foramula.getIsCreatePage().equals("")){
					foramula=viewModelManager.getDefaultToaForamula();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据用户ID判断当前用户是否设置了首页模块权限！"}); 
		}
		return foramula;
	}
	
	/**
	 * 根据用户ID判断当前用户是否设置了门户管理权限
	 */
	public List<ToaDesktopPortal> getDesktopProtaList (String userId){
		List<ToaDesktopPortal> list=new ArrayList<ToaDesktopPortal>();
		try {
			String protalId=protalPrivalManager.getDesktopProtalByUserId(userId);
			if(!protalId.equals("")&&protalId.length()>1){
				list=desktopPortalManager.getDesktopPortalListByIds(protalId);				
			}
			else {
				list=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据用户ID判断当前用户是否设置了首页模块权限！"}); 
		}
		return list;
	}
	
	public void deleteByPortalId (String portalId){
		
		protalPrivalManager.deleteByPortalId(portalId);
	}
	
	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public ModelPrivalManager getManager() {
		return manager;
	}

	@Autowired
	public void setManager(ModelPrivalManager manager) {
		this.manager = manager;
	}
	public ViewModelManager getViewModelManager() {
		return viewModelManager;
	}



	@Autowired
	public void setViewModelManager(ViewModelManager viewModelManager) {
		this.viewModelManager = viewModelManager;
	}
	public ProtalPrivalManager getProtalPrivalManager() {
		return protalPrivalManager;
	}
	@Autowired
	public void setProtalPrivalManager(ProtalPrivalManager protalPrivalManager) {
		this.protalPrivalManager = protalPrivalManager;
	}
	public DesktopPortalManager getDesktopPortalManager() {
		return desktopPortalManager;
	}
	@Autowired
	public void setDesktopPortalManager(DesktopPortalManager desktopPortalManager) {
		this.desktopPortalManager = desktopPortalManager;
	}

	
}
