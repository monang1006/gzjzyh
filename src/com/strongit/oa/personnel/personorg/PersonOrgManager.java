package com.strongit.oa.personnel.personorg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.personnel.structure.PersonStructureManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 机构manager
 * @author hull
 *
 */
@Service
@Transactional
public class PersonOrgManager extends BaseOrgManager{
  private GenericDAOHibernate<ToaBaseOrg, String> orgDAO;
  private PersonManager personManager;
  private PersonStructureManager strctManager;
  @Autowired private IUserService userManagers;
  
  @Autowired
  public void setStrctManager(PersonStructureManager strctManager) {
	this.strctManager = strctManager;
  }



@Autowired
  public void setSessionFactory(SessionFactory sessionFactory) {
	  orgDAO = new GenericDAOHibernate<ToaBaseOrg, String>(
				sessionFactory, ToaBaseOrg.class);
  }
  
 

/**
   * 保存机构
   * @param model
   * @throws SystemException
   * @throws ServiceException
   */
  @Transactional(readOnly = false)
  public void save(ToaBaseOrg model)throws SystemException,ServiceException{
	  try {
		 
		orgDAO.save(model);
	} catch (Exception e) {
		throw new ServiceException(MessagesConst.save_error,
				new Object[] { "保存机构出错！" });
	}
  }
  
  @Transactional(readOnly = false)
  public void updateOrg(ToaBaseOrg model)throws SystemException,ServiceException{
	  try {
		 
		orgDAO.update(model);
	} catch (Exception e) {
		throw new ServiceException(MessagesConst.save_error,
				new Object[] { "更新机构出错！" });
	}
  }
  public void save(List<ToaBaseOrg> list)throws SystemException,ServiceException{
	  try {
		orgDAO.save(list);
	} catch (DAOException e) {
		throw new ServiceException(MessagesConst.save_error,
				new Object[] { "保存机构出错！" });
	}
  }
  /**
	 * 得到下一个组织机构排序号
	 * 
	 * @return String 下一个排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getNextSequenceCode() throws DAOException, SystemException,
			ServiceException {
		return this.getNextSequence(orgDAO, "TUumsBaseOrg", "orgSequence");
	}

	/**
	 * 得到下一个组织结构代码。
	 * 
	 * @param code -
	 *            当前组织结构代码
	 * @return String - 下一个组织结构代码
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getNextOrgCode(String code) throws DAOException,
			SystemException, ServiceException {
		return this.getNextCode(orgDAO, "ToaBaseOrg", "orgSyscode",
				Const.RULE_TYPE_NEXT, Const.RULE_CODE_ORG, code);
	}

	/**
	 * 判断编码唯一性
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-25 下午02:13:31
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean compareCode(String code) throws DAOException,
			SystemException, ServiceException {

		return this.getObjBycode(orgDAO, "ToaBaseOrg", "orgSyscode", code);
	}
	
	/**
	 * 根据组织机构系统编码得到组织机构信息
	 * @author Hull
	 * @param orgSyscode
	 * @return ToaBaseOrg 组织机构信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaBaseOrg getOrgInfoBySyscode(String orgSyscode) throws DAOException, SystemException,
																				ServiceException {
		String hql = "from ToaBaseOrg as t where t.orgSyscode like ?";
		return (ToaBaseOrg)orgDAO.findUnique(hql, orgSyscode);
	}
	
  /**
   * 逻辑删除机构
   * @param id
   * @throws SystemException
   * @throws ServiceException
   */
  public void deleteByID(String id) throws SystemException, ServiceException {
		try {
			ToaBaseOrg org= this.getOrgByID(id);
			List<Object> list=new ArrayList<Object>();
			String hql = "update ToaBaseOrg t set t.orgIsdel='1' where t.orgid=?";
			list.add(id);
			if(org!=null){
				String code=org.getOrgSyscode();
				hql=hql+" or t.orgSyscode like ?";
				list.add(org.getOrgSyscode()+"%");
			}
			Object[] obj=new Object[list.size()];
			for(int i=0;i<obj.length;i++){
				obj[i]=list.get(i);
			}
			Query query = orgDAO.createQuery(hql, obj);
			query.executeUpdate();	
			personManager.deleteByCode(id);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "逻辑删除机构出错！" });
		}
	}
  /**
   * 还原已删除机构
   * @param id
   * @throws SystemException
   * @throws ServiceException
   */
  public void restoreById(String id) throws SystemException, ServiceException {
		try {
			ToaBaseOrg org= this.getOrgByID(id);
			List<Object> list=new ArrayList<Object>();
			String hql = "update ToaBaseOrg t set t.orgIsdel='0' where t.orgid=?";
			list.add(id);
			if(org!=null){
				String code=org.getOrgSyscode();
				List orlist=this.getAllOrg();
				for(int i=0;i<orlist.size();i++){
				String parentcode=((ToaBaseOrg) orlist.get(i)).getOrgSyscode();
				   int x=code.indexOf(parentcode);
					if(x!=-1){
					  hql=hql+" or t.orgSyscode like ?";
					  list.add(parentcode);
					}
	  		}
						
			}
			Object[] obj=new Object[list.size()];
			for(int i=0;i<obj.length;i++){
				obj[i]=list.get(i);
			}
			Query query = orgDAO.createQuery(hql, obj);
			query.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "逻辑删除机构出错！" });
		}
	}
  /**
   * 根据ID删除机构，物理删除
   * @author 胡丽丽
   * @param id
   * @throws SystemException
   * @throws ServiceException
   */
  public void delete(String id) throws SystemException,ServiceException{
	  try {
		  strctManager.deleteByOrg(id);
		  String hql="delete ToaBaseOrg t where t.orgid=?";
		  Query query= orgDAO.createQuery(hql, id);
		  query.executeUpdate();
		
	} catch (Exception e) {
		e.printStackTrace();
		throw new ServiceException(MessagesConst.del_error,
				new Object[]{"根据ID做物理删除机构出错!"});
	}
  }
  /**
   * 根据机构ID查询
   * @author hull
   * @param id
   * @return
   * @throws SystemException
   * @throws ServiceException
   */
  public ToaBaseOrg getOrgByID(String id)throws SystemException,ServiceException{
	  try {
		return orgDAO.get(id);
	} catch (Exception e) {
		throw new ServiceException(MessagesConst.find_error,
				    new Object[]{"根据机构ID查询出错！"});
	}
  }
  
  /**
   * 根据机构名称查询机构
   * @author HULL
   * @param orgname
   * @return
   * @throws SystemException
   * @throws ServiceException
   */
  public List<ToaBaseOrg> getOrgByName(String orgname)throws SystemException,ServiceException{
	  try {
		  String hql="from ToaBaseOrg t where t.orgName =? order by t.orgSyscode";
		  return orgDAO.find(hql,orgname);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					    new Object[]{"根据机构ID查询出错！"});
		}
  }
  
  /**
   * 查询所有机构
   * @author hull
   * @return
   * @throws SystemException
   * @throws ServiceException
   */
  public List<ToaBaseOrg> getAllOrg()throws SystemException,ServiceException{
	  try {
		  String hql="from ToaBaseOrg t ";
		  //pengxq于20110105添加
		  boolean flag=userManagers.isViewChildOrganizationEnabeld();
		  User user=userManagers.getCurrentUser();
		  TUumsBaseOrg org=userManagers.getSupOrgByUserIdByHa(user.getUserId());
		  if(org!=null){
			  if(flag){
				  hql+=" where t.userOrgcode like '"+org.getOrgSyscode()+"%'";
			  }else{
				  hql+=" where t.userOrgid ='"+org.getOrgId()+"'";
			  }
		  }else{
			  hql+=" where t.userOrgcode='0'";
		  }
		  hql+=" order by t.orgSyscode";
		return orgDAO.find(hql);
	} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
			    new Object[]{"查询所有机构出错！"});
	}
  }
  
  /**
   * 根据删除情况和机构编码查询
   * @author hull
   * @param isdel
   * @return
   */
  public List<ToaBaseOrg> getOrgsByIsdel(String isdel,String syscode)throws SystemException,ServiceException{
     try {
		String hql="from ToaBaseOrg t where t.orgIsdel=? and t.orgid not in (select t1.orgid from ToaBaseOrg t1 where t1.orgSyscode like ? ) order by t.orgSyscode";
		 return orgDAO.find(hql,isdel,syscode+"%");
	} catch (DAOException e) {
		throw new ServiceException(MessagesConst.find_error,
			    new Object[]{" 根据删除清楚查询出错！"});
	}
  }
  
  public List<ToaBaseOrg> getOrgsByIsdelnoum(String isdel,String syscode)throws SystemException,ServiceException{
	  try {
		  String hql="from ToaBaseOrg t where t.orgIsdel=? and t.rest=null and t.orgid not in (select t1.orgid from ToaBaseOrg t1 where t1.orgSyscode like ? ) ";
		 //pengxq于20110105添加
		  boolean flag=userManagers.isViewChildOrganizationEnabeld();
		  User user=userManagers.getCurrentUser();
		  TUumsBaseOrg org=userManagers.getSupOrgByUserIdByHa(user.getUserId());
		  if(org!=null){
			  if(flag){
				  hql+=" and t.userOrgcode like '"+org.getOrgSyscode()+"%'";
			  }else{
				  hql+=" and t.userOrgid ='"+org.getOrgId()+"'";
			  }
		  }else{
			  hql+=" and t.userOrgcode='0'";
		  }
		  hql+=" order by t.orgSyscode";
		  return orgDAO.find(hql,isdel,syscode+"%");
	  } catch (DAOException e) {
		  throw new ServiceException(MessagesConst.find_error,
				  new Object[]{" 根据删除清楚查询出错！"});
	  }
  }
  /**
   * 根据删除情况查询
   * @author hull
   * @param isdel
   * @return
   */
  public List<ToaBaseOrg> getOrgsByIsdel(String isdel)throws SystemException,ServiceException{
	  try {
		  String hql="from ToaBaseOrg t where t.orgIsdel=? ";
		  //pengxq于20110105添加
		  boolean flag=userManagers.isViewChildOrganizationEnabeld();
		  User user=userManagers.getCurrentUser();
		  TUumsBaseOrg org=userManagers.getSupOrgByUserIdByHa(user.getUserId());
		  if(org!=null){
			  if(flag){
				  hql+=" and t.userOrgcode like '"+org.getOrgSyscode()+"%'";
			  }else{
				  hql+=" and t.userOrgid ='"+org.getOrgId()+"'";
			  }
		  }else{
			  hql+=" and t.userOrgcode='0'";
		  }
		  hql+=" order by t.orgSyscode" ;
		  return orgDAO.find(hql,isdel);
	  } catch (DAOException e) {
		  throw new ServiceException(MessagesConst.find_error,
				  new Object[]{" 根据删除清楚查询出错！"});
	  }
  }
  
  /**
   * 获取父机构编码
   * @param orgsyscode
   * @return
   * @throws DAOException
   * @throws SystemException
   * @throws ServiceException
   */
  public String getParentOrgSyscode(String orgsyscode)throws DAOException, SystemException, ServiceException {
	  try {
		    String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
			String parentOrgSyscode = this.getParentCode(codeRule, orgsyscode);
			return parentOrgSyscode;
	} catch (IOException e) {
		throw new ServiceException(MessagesConst.find_error,
			    new Object[]{" 获取父机构编码出错！"});
	}
  }
    /**
	 * 根据组织机构系统编码获取上级组织机构信息，如果没有上级则返回本级。
	 * 
	 * @param orgSyscode -
	 *            组织机构系统编码
	 * @return ToaBaseOrg - 上级组织机构信息BO
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaBaseOrg getParentOrgByOrgSyscode(String orgSyscode)
			throws DAOException, SystemException, ServiceException {
		ToaBaseOrg boBaseOrg = null;
		try {
			// 得到组织机构编码规则
			String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
			String parentOrgSyscode = this.getParentCode(codeRule, orgSyscode);
			if (!(parentOrgSyscode != null && parentOrgSyscode.length() > 0)) {
				parentOrgSyscode = orgSyscode;
			}
			final String hql = "from ToaBaseOrg as t where t.orgSyscode=?";
			boBaseOrg = (ToaBaseOrg) orgDAO.findUnique(hql, parentOrgSyscode);
		} catch (DAOException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
		return boBaseOrg;
	}


  /**
   * 查询一个机构下的子机构
   * @param syscode 父机构编码
   * @return
   * @throws SystemException
   * @throws ServiceException
   */
  public List<ToaBaseOrg> getOrgByParent(String syscode)throws SystemException,ServiceException{
	  try {
		String hql="from ToaBaseOrg t where t.orgSyscode like ? and t.orgSyscode!=? order by t.orgSyscode";
		  return orgDAO.find(hql,syscode+"%",syscode);
	} catch (Exception e) {
		throw new ServiceException(MessagesConst.save_error,
			    new Object[]{"查询一个机构下的子机构出错！"});
	}
  }
  
  /**
   * 得到统一用户同步过来的机构
   *@author 蒋国斌
   *@date 2009-10-15 下午03:34:51 
   * @param syscode
   * @return
   * @throws SystemException
   * @throws ServiceException
   */
  public List<ToaBaseOrg> getOrgsByUums()throws SystemException,ServiceException{
	  try {
		String hql="from ToaBaseOrg t where t.rest!=null";
		  return orgDAO.find(hql);
	} catch (Exception e) {
		throw new ServiceException(MessagesConst.save_error,
			    new Object[]{"查询一个机构出错！"});
	}
  }
  /**
   * 物理删除统一用户机构
   *@author 蒋国斌
   *@date 2009-10-15 下午03:36:54 
   * @param List uumsOrg
   * @throws SystemException
   * @throws ServiceException
   */
  public void deleteByList(List uumsOrg) throws SystemException, ServiceException {
		try {
			//for (Iterator it = uumsOrg.iterator(); it.hasNext();) {
			//	ToaBaseOrg fog = (ToaBaseOrg) it.next();
				//personManager.deleteByOrg(fog.getOrgid());
			//}
			orgDAO.delete(uumsOrg);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "物理删除机构出错！" });
		}
	}

  /*
   * 
   * @author: 彭小青
   * @date: 	Dec 3, 2009 9:35:34 AM
   * Desc:
   * param:
   */
  	public List<ToaBaseOrg> getOrgListByID(String id)throws SystemException,ServiceException{
	  	try {
	  		String hql="from ToaBaseOrg  t where t.orgid=?";
	  		return orgDAO.find(hql, id);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					    new Object[]{"根据机构ID查询出错！"});
		}
  	}
  	
  	/*
  	 * 
  	 * Description:
  	 * param: 
  	 * @author 	    彭小青
  	 * @date 	    Apr 9, 2010 10:28:21 PM
  	 */
  	public String getPersonOrgIds()throws SystemException,ServiceException{
  		try {
  			String orgIds="";
	  		String hql=" select distinct(t.orgid) from ToaBaseOrg t group by t.orgid";
	  		List<String> orgIdList=orgDAO.find(hql);
	  		if(orgIds!=null&&orgIdList.size()>0){
	  			for(int i=0;i<orgIdList.size();i++){
	  				orgIds+=","+orgIdList.get(i);
	  			}
	  		}
	  		return orgIds;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					    new Object[]{"根据机构ID查询出错！"});
		}
  	}

public PersonManager getPersonManager() {
	return personManager;
}


@Autowired
public void setPersonManager(PersonManager personManager) {
	this.personManager = personManager;
}

  
  
}
