package com.strongit.xxbs.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.IUserService;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.service.IMemberService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class MemberService implements IMemberService
{
	private GenericDAOHibernate<TUumsBaseUser, String> userDao;
	private GenericDAOHibernate<TUumsBaseRole, String> roleDao;
	@Autowired private IUserService userService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		userDao = new GenericDAOHibernate<TUumsBaseUser, String>(
				sessionFactory, TUumsBaseUser.class);
		roleDao = new GenericDAOHibernate<TUumsBaseRole, String>(
				sessionFactory, TUumsBaseRole.class);
	}

	public String getOrgIdByMail(String mail) throws ServiceException,
			SystemException, DAOException
	{
		String orgId = "";
		List<TUumsBaseUser> lists = userDao.findByProperty("userEmail", mail);
		if(lists != null && lists.size() > 0)
		{
			TUumsBaseUser one = lists.get(0);
			orgId = one.getOrgId();
		}
		return orgId;
	}

	public String getUserIdByMail(String mail) throws ServiceException,
			SystemException, DAOException
	{
		String userId = "";
		List<TUumsBaseUser> lists = userDao.findByProperty("userEmail", mail);
		if(lists != null && lists.size() > 0)
		{
			
			/*for(int i=0;i<lists.size();i++){
			TUumsBaseUser one = lists.get(i);
			TUumsBaseOrg org = userManager.getSupOrgByUserIdByHa(one.getUserId());
			String isDel = org.getOrgIsdel();
			if("0".equals(isDel)){
				userId = one.getUserId();
				break;
			}
			}*/
			TUumsBaseUser one = lists.get(0);
			userId = one.getUserId();
		}
		return userId;
	}

	@Transactional(readOnly = false)
	public void save(TUumsBaseUser user) throws ServiceException,
			SystemException, DAOException
	{
		//syncDao.insertUser(user);
		//addRoleToBys(user);
	}

	@Transactional(readOnly = false)
	public void update(TUumsBaseUser user) throws ServiceException,
			SystemException, DAOException
	{
		//userDao.update(user);
		//addRoleToBys(user);
	}
	
	@Transactional(readOnly = false)
	private void addRoleToBys(TUumsBaseUser user)
	{
		List<TUumsBaseRole> rets = roleDao.findByProperty("roleSyscode", Publish.ROLE_SUBMITTER);
		if(rets.size() > 0)
		{		
			TUumsBaseRole role = rets.get(0);
			userService.saveUserRoles(user.getUserId(), role.getRoleId());
		}
	}

	public Page<TUumsBaseUser> getNotMailUser(Page<TUumsBaseUser> page)
			throws ServiceException, SystemException, DAOException
	{
		Criterion cr1 = Restrictions.isNull("userEmail");
		Criterion cr2 = Restrictions.eq("userEmail", "");
		Criterion cr = Restrictions.or(cr1, cr2);
		return userDao.findByCriteria(page, cr);
	}
	/**
	 * 保存异常邮件地址给映射的用户
	 */
	@Transactional(readOnly = false)
	public void saveMailToUser(String userId, String mail)
			throws ServiceException, SystemException, DAOException
	{
		TUumsBaseUser user = userDao.get(userId);
		user.setUserEmail(mail);
		userDao.save(user);
	}

}
