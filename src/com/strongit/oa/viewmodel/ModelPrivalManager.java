package com.strongit.oa.viewmodel;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaForamulaRelation;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;


/**
 * 门户权限设置MANAGER
 *@author zhengzb
 *@desc  
 *@date 2011-1-19 下午03:37:04
 */
@Service
@Transactional
@OALogger
public class ModelPrivalManager {

	private GenericDAOHibernate<ToaForamulaRelation, String>relationDao;
	
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		relationDao=new GenericDAOHibernate<ToaForamulaRelation, String>(sessionFactory,ToaForamulaRelation.class);
	}
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	private IUserService userService;
	
	/**
	 * 把页面组装的门户权限设置信息拆分为ToaForamulaRelation列表
	 * @author zhengzb
	 * @desc 
	 * 2011-1-19 下午03:46:00 
	 * @param selectData
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaForamulaRelation> getSelectDataBySplit(String selectData)  throws SystemException,ServiceException{
		try {
			List<ToaForamulaRelation> list=new ArrayList<ToaForamulaRelation>();
			JSONArray array = JSONArray.fromObject(selectData);
			int count = array.size();
			for(int i=0;i<count;i++){
				ToaForamulaRelation relation=new ToaForamulaRelation();

				JSONObject jsonObj=array.getJSONObject(i);
				String relationId=jsonObj.getString("relationId");
				if(relationId!=null&&!relationId.equals("")){
					relation.setRelationId(relationId);					
				}else {
					relation.setRelationId(null);	
				}
				relation.setPrivilsort(jsonObj.getString("privilsort"));
				String privilsortId=jsonObj.getString("privilsortId");
				if(privilsortId.length()>32){
					privilsortId=privilsortId.substring(1);
				}
				relation.setPrivilsortId(privilsortId);
				relation.setForamulaId(jsonObj.getString("foramulaId"));
				relation.setPrivilsortName(jsonObj.getString("privilsortName"));

				
				list.add(relation);
			}
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"把门户权限设置人员数据拆分"});
		}
	}
	
	/**
	 * 保存门户设置权限List信息
	 * @author zhengzb
	 * @desc 
	 * 2011-1-19 下午03:51:21 
	 * @param list
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveList(List<ToaForamulaRelation> list)  throws SystemException,ServiceException{
		try {
			relationDao.save(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存门户设置权限信息"}); 
		}
	}
	
	/**
	 * 根据门户模块ID删除门户权限信息
	 * @author zhengzb
	 * @desc 
	 * 2011-1-20 下午02:01:58 
	 * @param foramulaId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByForamulaId (String foramulaId) throws SystemException,ServiceException{
		try {
			
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaForamulaRelation  t  where t.foramulaId= ?");
			List<ToaForamulaRelation> list=relationDao.find(sql.toString(), foramulaId);
			if(list!=null&&list.size()>0){
				relationDao.delete(list);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据门户模块ID删除门户权限信息！"}); 
		}
	}
	
	/**
	 * 根据门户模块ID获取设置权限信息字符串
	 * @author zhengzb
	 * @desc 
	 * 2011-1-20 下午02:24:41 
	 * @param foramulaId
	 * @return 权限类型|权限ID|权限名，权限类型|权限ID|权限名，权限类型|权限ID|权限名，............
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getSelectedDateByForamulaId(String foramulaId) throws SystemException,ServiceException{
		String selectedDate="";
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaForamulaRelation  t  where t.foramulaId= ?");
			List<ToaForamulaRelation> list=relationDao.find(sql.toString(), foramulaId);
			if(list!=null&&list.size()>0){
				for(ToaForamulaRelation relation:list){
					selectedDate=selectedDate+","+relation.getPrivilsort()+"|"+relation.getPrivilsortId()+"|"+relation.getPrivilsortName();
				}
				if(selectedDate.length()>0){
					selectedDate=selectedDate.substring(1);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedDate;
	}
	
	/**
	 * 判断当前选择人员是否可以添加权限
	 * @author zhengzb
	 * @desc 
	 * 2011-1-20 下午05:13:46 
	 * @param privilsortId
	 * @param foramulaId
	 * @return   返回：true 可以添加权限，  返回： false 不可以添加
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Boolean isHasSelect(String privilsortId,String foramulaId) throws SystemException,ServiceException{
		Boolean boo=true;
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaForamulaRelation  t  where t.privilsortId= ?");
			List<ToaForamulaRelation> list=relationDao.find(sql.toString(), privilsortId);
			if(list!=null&&list.size()>0){
				for(ToaForamulaRelation relation:list){
					if(!relation.getForamulaId().equals(foramulaId)){
						boo=false;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"判断当前选择人员是否可以添加权限！"}); 
		}
		return boo;
	}
	/**
	 * 查询视图设置
	 * @author xush
	 * @desc 
	 * 12/11/2013 3:36 PM
	 * @param privilsortId
	 * @param foramulaId
	 * @return   返回：true 可以添加权限，  返回： false 不可以添加
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateView(String privilsortId,String foramulaId) throws SystemException,ServiceException{
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaForamulaRelation  t  where t.privilsortId= ?");
			List<ToaForamulaRelation> list=relationDao.find(sql.toString(), privilsortId);
			if(list!=null&&list.size()>0){
				for(ToaForamulaRelation relation:list){
					  relation.setForamulaId(foramulaId);
					  relationDao.save(relation);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"替换视图！"}); 
		}
		
	}
	/**
	 * 根据用户ID判断当前用户是否设置了首页模块权限
	 * @author zhengzb
	 * @desc 
	 * 2011-1-21 上午09:31:14 
	 * @param userId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getForamulaIdByUserId(String userId) throws SystemException,ServiceException{
		String foramulaId="";
		try {
			User user=userService.getUserInfoByUserId(userId);
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
			List<TUumsBaseRole> userRoleList=userService.getUserRoleInfosByUserIdByHa(userId, "1", org.getOrgId());			//获取当前用户所有角色信息
			String roleIds="";
			if(userRoleList!=null&&userRoleList.size()>0){
				Long sequ=userRoleList.get(0).getRoleSequence();
				String roleId="";
				if(userRoleList!=null&&userRoleList.size()>0){
					for(TUumsBaseRole role:userRoleList){
						roleIds+=",'"+role.getRoleId()+"'";
					}
				}
			}
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaForamulaRelation  t  where 1=1");
			sql.append(" and t.privilsortId in ('"+userId+"','"+org.getOrgId()+"'");	
			if(!roleIds.equals("")){
				sql.append(roleIds);
			}
			sql.append(")");
			List<ToaForamulaRelation> list=relationDao.find(sql.toString());
			
			List<ToaForamulaRelation> orgList=new ArrayList<ToaForamulaRelation>();
			List<ToaForamulaRelation> roleList=new ArrayList<ToaForamulaRelation>();
			List<ToaForamulaRelation> perList=new ArrayList<ToaForamulaRelation>();
			if(list!=null&&list.size()>0){
				for(ToaForamulaRelation relation:list){
					//人员类型：4   角色类型：3    机构类型：1
					if(relation.getPrivilsort().equals("4")){			//人员
						perList.add(relation);
					}else if (relation.getPrivilsort().equals("3")) {	//角色
						roleList.add(relation);
					}else if (relation.getPrivilsort().equals("1")) {	//机构
						orgList.add(relation);						
					}					
				}				
			}
			if(perList!=null&&perList.size()>0){						//选择判断人员
				 foramulaId=perList.get(0).getForamulaId();
			}else {
				if(roleList!=null&&roleList.size()>0){	//在判断角色
					int k=0;//初始值，最后根据k来取首页权限ID
					//得到第一条数据的角色的排序
					Long squence=userService.getRoleInfoByRoleId(roleList.get(0).getPrivilsortId()).getRoleSequence();
					for(int i=1;i<roleList.size();i++){//循环角色，得到角色排序字段最小的
						Long squence2=userService.getRoleInfoByRoleId(roleList.get(i).getPrivilsortId()).getRoleSequence();
						if(squence2<squence){
							k=i;
						}
					}
					foramulaId=roleList.get(k).getForamulaId();
				}else {
					if(orgList!=null&&orgList.size()>0){				//最后判断机构
						foramulaId=orgList.get(0).getForamulaId();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据用户ID判断当前用户是否设置了首页模块权限！"}); 
		}
		return foramulaId;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
}
