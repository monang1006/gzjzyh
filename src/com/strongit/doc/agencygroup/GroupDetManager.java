package com.strongit.doc.agencygroup;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.ToaGroupDet;
import com.strongit.doc.sends.util.DocType;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongmvc.orm.hibernate.Page;

/**
 * 机构分组服务类.
 * 
 * @author
 * @company Strongit Ltd. (C) copyright
 * @date
 * @version 2.0.2.3
 * @classpath
 * @comment
 * @email
 */
@Service
@Transactional
@OALogger
public class GroupDetManager {
	Logger logger = LoggerFactory.getLogger(this.getClass());
 
	IGenericDAO<ToaGroupDet, java.lang.String> groupDetsDao = null; // 定义DAO操作类.
	
	@Autowired IUserService userService; // 统一用户服务类.

	/**
	 * 注入SESSION工厂
	 * 
	 * @author:
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) { 
		groupDetsDao = new GenericDAOHibernate<ToaGroupDet, java.lang.String>(
				sessionFactory, ToaGroupDet.class);
	}

	/**
	 * 
	 * @param  
	 * @param groupId
	 * @return List
	 * @roseuid  
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaGroupDet> getAgencyList(Page<ToaGroupDet> page,String groupId,String orgCode,String orgName) throws SystemException,ServiceException {
		try {
			StringBuilder hql = new StringBuilder("select t.groupDetsId,t.orgId,tm.orgSyscode,tm.orgName,tm.orgTel,tm.orgAddr from ToaGroupDet t, ");
			hql.append(" TUumsBaseOrg tm where t.orgId=tm.orgId "); // 表关联T_UUMS_BASE_ORG
			hql.append(" and t.toaGroupAgency.userId='"+userService.getCurrentUser().getUserId() +"'");				
					
//			List<String> params = new ArrayList<String>(1);
//			params.add(userService.getCurrentUser().getUserId());
			if(groupId != null && !"".equals(groupId)){
				hql.append(" and t.toaGroupAgency.groupAgencyId='"+groupId+"'");
//				params.add(groupId);
			}
			if(null!=orgCode && !"".equals(orgCode)){   
				hql.append("and tm.orgSyscode like '%"+FiltrateContent.getNewContent(orgCode).trim()+"%' ");
			}
			if(null!=orgName && !"".equals(orgName)){
				hql.append("and tm.orgName like '%"+FiltrateContent.getNewContent(orgName).trim()+"%' ");
			}

			hql.append(" order by tm.orgSyscode ");
			page = groupDetsDao.find(page, hql.toString());//, params.toArray());
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"机构"});
		}
		return page;
	}
	
	/**
	 * author:dengzc
	 * description:获取组下的人员列表
	 * modifyer:
	 * description:
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaGroupDet> getDetByGroupId(String groupId)throws SystemException,ServiceException{
		try{
			String hql = "from ToaGroupDet as t where t.toaGroupAgency.groupAgencyId=?"; 
			return groupDetsDao.find(hql, groupId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"机构"});
		}
	}  
	
	/**
	 * @param group
	 * @roseuid 495041520242
	 */
	public void delete(String group)  throws SystemException,ServiceException{
		try{
			groupDetsDao.delete(group);			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,new Object[] {"机构"});
		}
	}
	/**
	 * 根据公文id得到公文信息
	 * @author:qt
	 * @date:2010-8-27
	 * @param id				主键id
	 * @return					信息
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	ToaGroupDet getGroupDetById(String id) throws ServiceException {
		return groupDetsDao.get(id);
	}
	/**
	 *  
	 * @author: 
	 * @date:2010-8-18 上午10:16:20
	 * @param ids
	 */
	void delete(String id,OALogInfo...infos) {
		if(id!= null){
//			StringBuilder sql = new StringBuilder("update T_TRANS_DOC t set t.ISDELETE = '"+DocType.DELETE+"' where t.DOC_ID = '");
//			sql.append(id).append("'"); 
//			logger.info("更新草稿状态SQL：{}",sql);
//			groupDetsDao.executeJdbcUpdate(sql.toString());
			groupDetsDao.delete(id);
		}
	}
	/**
	 * 保存
	 * 
	 * @author:
	 * @date:
	 * @param model
	 */
	void save(ToaGroupDet model) { 

		groupDetsDao.save(model);
	}
	
	/**
	 * @author:luosy
	 * @description:  根据用户ID获取分组机构
	 * @date : 2010-8-18
	 * @modifyer:
	 * @description:
	 * @param userId
	 * @return
	 * 		Object[]:分组id，分组名，机构编码，机构名
	 * 			
	 * 			groupAgencyId,groupAgencyName,orgSyscode,orgName 
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List getAgencyListByUserId(String userId) throws SystemException,ServiceException {
		try {
			StringBuilder hql = new StringBuilder("select t.toaGroupAgency.groupAgencyId,t.toaGroupAgency.groupAgencyName,org.orgSyscode,org.orgName ");
			hql.append(" from ToaGroupDet t, TUumsBaseOrg org "); // 表关联T_UUMS_BASE_ORG
			hql.append(" where t.orgId=org.orgId  and t.toaGroupAgency.userId='"+userId+"' ")
				.append(" and org.orgIsdel='0'");		

			hql.append(" order by org.orgSyscode ");
			List list = groupDetsDao.find(hql.toString());
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"机构"});
		}
	}

	public IUserService getUserService() {
		return userService;
	}

}
