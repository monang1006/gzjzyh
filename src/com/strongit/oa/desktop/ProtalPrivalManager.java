package com.strongit.oa.desktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDesktopPortal;
import com.strongit.oa.bo.ToaForamulaRelation;
import com.strongit.oa.bo.ToaPortalPrival;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class ProtalPrivalManager {
	private GenericDAOHibernate<ToaPortalPrival, String> portalpriDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.portalpriDao = new GenericDAOHibernate<ToaPortalPrival, String>(
				sessionFactory, ToaPortalPrival.class);
	}
	
	private IUserService userService;
	
	/**
	 * 根据门户管理ID获取设置权限信息字符串
	 * @author zhengzb
	 * @desc 
	 * 2011-1-20 下午02:24:41 
	 * @param foramulaId
	 * @return 权限类型|权限ID|权限名，权限类型|权限ID|权限名，权限类型|权限ID|权限名，............
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void savePortalPrivalList(List<ToaPortalPrival> list) throws SystemException,ServiceException{
		try {
			portalpriDao.save(list);
		} catch (Exception e) {
			throw new ServiceException("find.error", new Object[] { "保存门户管理权限" });
		}
	}
	
	/**
	 * 
	 * @author zhengzb
	 * @desc 
	 * 2011-1-24 下午04:08:01 
	 * @param portalId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getSelectedDateByPortalId(String portalId) throws SystemException,ServiceException{
		String selectedDate="";
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaPortalPrival  t  where t.portalId= ?");
			List<ToaPortalPrival> list=portalpriDao.find(sql.toString(), portalId);
			if(list!=null&&list.size()>0){
				for(ToaPortalPrival portal:list){
					selectedDate=selectedDate+","+portal.getPrivalType()+"|"+portal.getPrivalId()+"|"+portal.getPrivalName();
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
	 * 把页面组装的门户管理权限设置信息拆分为ToaPortalPrival列表
	 * @author zhengzb
	 * @desc 
	 * 2011-1-19 下午03:46:00 
	 * @param selectData
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaPortalPrival> getSelectDataBySplit(String selectData)  throws SystemException,ServiceException{
		try {
			List<ToaPortalPrival> list=new ArrayList<ToaPortalPrival>();
			JSONArray array = JSONArray.fromObject(selectData);
			int count = array.size();
			for(int i=0;i<count;i++){
				ToaPortalPrival protal=new ToaPortalPrival();

				JSONObject jsonObj=array.getJSONObject(i);
				String protalPrivalId=jsonObj.getString("id");
				if(protalPrivalId!=null&&!protalPrivalId.equals("")){
					protal.setId(protalPrivalId);					
				}else {
					protal.setId(null);	
				}
				protal.setPrivalType(jsonObj.getString("privalType"));
				String privilsortId=jsonObj.getString("privalId");
				if(privilsortId.length()>32){
					privilsortId=privilsortId.substring(1);
				}
				protal.setPrivalId(privilsortId);
				protal.setPortalId(jsonObj.getString("portalId"));
				protal.setPrivalName(jsonObj.getString("privalName"));

				
				list.add(protal);
			}
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"把门户管理权限设置人员数据拆分"});
		}
	}
	
	
	/**
	 * 根据门户管理ID删除门户管理权限信息
	 * @author zhengzb
	 * @desc 
	 * 2011-1-20 下午02:01:58 
	 * @param foramulaId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByPortalId (String portalId) throws SystemException,ServiceException{
		try {
			
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaPortalPrival  t  where t.portalId= ?");
			List<ToaPortalPrival> list=portalpriDao.find(sql.toString(), portalId);
			if(list!=null&&list.size()>0){
				portalpriDao.delete(list);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据门户模块ID删除门户权限信息！"}); 
		}
	}
	
	
	/**
	 * 保存门户管理设置权限List信息
	 * @author zhengzb
	 * @desc 
	 * 2011-1-19 下午03:51:21 
	 * @param list
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveList(List<ToaPortalPrival> list)  throws SystemException,ServiceException{
		try {
			portalpriDao.save(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存门户设置权限信息"}); 
		}
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
	public Boolean isHasSelect(String privalId,String portalId) throws SystemException,ServiceException{
		Boolean boo=true;
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaPortalPrival  t  where t.privalId= ?");
			List<ToaPortalPrival> list=portalpriDao.find(sql.toString(), privalId);
			if(list!=null&&list.size()>0){
				for(ToaPortalPrival protal:list){
					if(!protal.getPortalId().equals(portalId)){
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
	 * 根据用户ID判断当前用户是否设置了门户管理权限
	 * @author zhengzb
	 * @desc 
	 * 2011-1-21 上午09:31:14 
	 * @param userId
	 * @return portalId，portalId,portalId,portalId,.........
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getDesktopProtalByUserId(String userId) throws SystemException,ServiceException{
		String portalId="";
		try {
			User user=userService.getUserInfoByUserId(userId);
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
			List<TUumsBaseRole> userRoleList=userService.getUserRoleInfosByUserIdByHa(userId, "1", org.getOrgId());			//获取当前用户所有角色信息
			String roleIds="";
			if(userRoleList!=null&&userRoleList.size()>0){
				for(TUumsBaseRole role:userRoleList){
					roleIds+=",'"+role.getRoleId()+"'";
				}
			}
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaPortalPrival  t  where 1=1");
			sql.append(" and t.privalId in ('"+userId+"','"+org.getOrgId()+"'");	
			if(!roleIds.equals("")){
				sql.append(roleIds);
			}
			sql.append(")");
			List<ToaPortalPrival> list=portalpriDao.find(sql.toString());
			if(list!=null&&list.size()>0){
				Map<String, String> map=new HashMap<String, String>();
				for(ToaPortalPrival prival:list){
					map.put(prival.getPortalId(), prival.getPortalId());
				}
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					String key = entry.getKey().toString();
					portalId=portalId+",'"+key+"'";
					
				//Object value = entry.getValue();
				}

			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据用户ID判断当前用户是否设置了首页模块权限！"}); 
		}
		if(!portalId.equals("")&&portalId.length()>1){
			portalId=portalId.substring(1);
		}
		return portalId;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	

}
