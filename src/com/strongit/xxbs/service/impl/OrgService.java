package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.runqian.report.engine.function.Query;
import com.strongit.uums.bo.TUumsBaseGroup;
import com.strongit.uums.bo.TUumsBaseGroupPrivil;
import com.strongit.uums.bo.TUumsBaseGroupUser;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.bo.TUumsBaseRolePrivil;
import com.strongit.uums.bo.TUumsBaseSupOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.IUserService;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.service.IOrgService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class OrgService implements IOrgService {
	private GenericDAOHibernate<TUumsBaseOrg, String> orgDao;

	private GenericDAOHibernate<TUumsBaseGroupUser, String> groupUserDao;
	
	private GenericDAOHibernate<TUumsBaseUser, String> userDao;

	@Autowired
	private IUserService userService;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		orgDao = new GenericDAOHibernate<TUumsBaseOrg, String>(sessionFactory,
				TUumsBaseOrg.class);
		groupUserDao = new GenericDAOHibernate<TUumsBaseGroupUser, String>(
				sessionFactory, TUumsBaseGroupUser.class);
		userDao = new GenericDAOHibernate<TUumsBaseUser, String>(
				sessionFactory, TUumsBaseUser.class);
	}

	public Map<String, String> getOrgNames() throws DAOException,
			SystemException, ServiceException {
		Map<String, String> ret = new HashMap<String, String>();
		List<TUumsBaseOrg> lists = orgDao.findAll();
		if (lists != null) {
			for (TUumsBaseOrg org : lists) {
				ret.put(org.getOrgId(), org.getOrgName());
			}
		}
		return ret;
	}

	public Map<String, TUumsBaseOrg> getOrgs(String[] orgIds)
			throws DAOException, SystemException, ServiceException {
		Map<String, TUumsBaseOrg> ret = new HashMap<String, TUumsBaseOrg>();
		Criterion[] criterion = new Criterion[] { Restrictions.in("orgId",
				orgIds) };
		List<TUumsBaseOrg> lists = orgDao.findByCriteria(criterion);
		if (lists != null) {
			for (TUumsBaseOrg org : lists) {
				ret.put(org.getOrgId(), org);
			}
		}
		return ret;
	}

	public Page<TUumsBaseOrg> getOrgs(Page<TUumsBaseOrg> page, String[] orgIds)
			throws DAOException, SystemException, ServiceException {
		Criterion[] criterion = new Criterion[] { Restrictions.in("orgId",
				orgIds) };
		return orgDao.findByCriteria(page, criterion);
	}

	public String getOrgGroup(String userId) throws DAOException,
			SystemException, ServiceException {
		String ret = "";
		List<TUumsBaseGroupUser> lists = groupUserDao.findByProperty("userId",
				userId);
		if (lists != null) {
			TUumsBaseGroupUser gu = lists.get(0);
			ret = gu.getBaseGroup().getGroupId();
		}
		return ret;
	}

	public String getOrgIdByMail(String mail) throws DAOException,
			SystemException, ServiceException {
		String orgId = "";
		List<TUumsBaseOrg> lists = orgDao.findByProperty("rest2", mail);
		if (lists != null && lists.size() > 0) {
			TUumsBaseOrg one = lists.get(0);
			orgId = one.getOrgId();
		}
		return orgId;
	}

	public String[] getOrgIds() throws DAOException, SystemException,
			ServiceException {
		List<TUumsBaseOrg> lists = orgDao.findAll();
		String[] ids = new String[lists.size()];
		if (lists != null) {
			int i = 0;
			for (TUumsBaseOrg org : lists) {
				ids[i] = org.getOrgId();
				i++;
			}
		}
		return ids;
	}

	public Page<TUumsBaseOrg> findAllOrg(Page<TUumsBaseOrg> page)
			throws DAOException, SystemException, ServiceException {
		return orgDao.findAll(page);
	}

	public List<TUumsBaseOrg> getOrgsById(String[] orgIds) throws DAOException,
			SystemException, ServiceException {
		Criterion[] criterion = new Criterion[] { Restrictions.in("orgId",
				orgIds) };
		List<TUumsBaseOrg> lists = orgDao.findByCriteria(criterion);
		return lists;
	}

	public void updateOrg(TUumsBaseOrg org) throws ServiceException,
			SystemException, DAOException {
		orgDao.update(org);
	}

	public Page<TUumsBaseOrg> findOrgByTitle(Page<TUumsBaseOrg> page,
			String orgName) throws DAOException, SystemException,
			ServiceException {
		Criterion[] criterion = new Criterion[] { Restrictions.like("orgName",
				"%" + orgName + "%") };
		return orgDao.findByCriteria(page, criterion);
	}
	public String[] getOrgIdsByisOrg(String isOrg)
	throws DAOException, SystemException, ServiceException
	{
		Criterion[] criterion = new Criterion[]{
				Restrictions.eq("orgNature", isOrg),Restrictions.eq("orgIsdel", "0") };
		List<TUumsBaseOrg> lists = new ArrayList<TUumsBaseOrg>();
		if (isOrg.equals("")) {
			Criterion[] cs1 = new Criterion[] { Restrictions.eq("orgNature",
					"0"),Restrictions.eq("orgIsdel", "0") };
			lists = orgDao.findByCriteria(cs1);
		} else {
			lists = orgDao.findByCriteria(criterion);
		}
		String[] ids = new String[lists.size()];
		if (lists != null) {
			int i = 0;
			for (TUumsBaseOrg org : lists) {
				ids[i] = org.getOrgId();
				i++;
			}
		}
		return ids;
	}
	
	public String[] getDeptOrgIds()
	throws DAOException, SystemException, ServiceException
	{
		Criterion[] criterion = new Criterion[]{
				Restrictions.in("orgNature", new Object[]{"0","3"}),Restrictions.eq("orgIsdel", "0") };
		List<TUumsBaseOrg> lists = new ArrayList<TUumsBaseOrg>();
		
		lists = orgDao.findByCriteria(criterion);
		
		String[] ids = new String[lists.size()];
		if (lists != null) {
			int i = 0;
			for (TUumsBaseOrg org : lists) {
				ids[i] = org.getOrgId();
				i++;
			}
		}
		return ids;
	}

	public List<TUumsBaseOrg> getOrgNames1() throws DAOException,
			SystemException, ServiceException {
		List<TUumsBaseOrg> lists = orgDao.getSession()
				.createCriteria(TUumsBaseOrg.class)
				.addOrder(Order.asc("orgNature")).list();
		return lists;
	}

	public List<TUumsBaseOrg> getOrgIdsByisOrg1(String isOrg)
	throws DAOException, SystemException, ServiceException
	{
		Criterion criterion1 = Restrictions.eq("orgNature", isOrg);
		Criterion criterion2 = Restrictions.eq("orgIsdel", "0");
		List<TUumsBaseOrg> lists = orgDao.getSession()
		.createCriteria(TUumsBaseOrg.class).add(criterion1).add(criterion2)
		.addOrder(Order.asc("orgSequence")).list();
		return lists;
	}

	@Transactional(readOnly = false)
	public void saveOrgAndChildren(TUumsBaseOrg boOrg, String iscode)
			throws DAOException, SystemException, ServiceException {
		String conditionFlag = "0";

		TUumsBaseOrg parentOrg = this.userService.getParentOrgByOrgSyscode(boOrg
				.getOrgSyscode());
		String supOrgCode = null;

		if ((parentOrg != null)
				&& (!(parentOrg.getOrgSyscode().equals(boOrg.getOrgSyscode())))) {
			boOrg.setOrgParentId(parentOrg.getOrgId());
			supOrgCode = parentOrg.getSupOrgCode();
		} else {
			boOrg.setOrgParentId(null);
		}

		if ((supOrgCode == null)
				|| (("1".equals(boOrg.getIsOrg())) && (iscode
						.startsWith(supOrgCode)))) {
			boOrg.setSupOrgCode(boOrg.getOrgSyscode());
			conditionFlag = "1";
		} else if ((supOrgCode != null)
				&& (supOrgCode.equals(boOrg.getSupOrgCode()))) {
			conditionFlag = "0";
		} else if ("1".equals(boOrg.getIsOrg())) {
			boOrg.setSupOrgCode(boOrg.getOrgSyscode());
			conditionFlag = "2";
		} else {
			boOrg.setSupOrgCode(supOrgCode);
			conditionFlag = "3";
		}

		this.orgDao.getSession().update(boOrg);
		List userGroupLst;
		if (!("0".equals(conditionFlag))) {
			List<TUumsBaseUser> userLst = this.orgDao
					.find("from TUumsBaseUser as baseUser where baseUser.orgId = ? order by baseUser.userSequence, baseUser.userSyscode",
							new Object[] { boOrg.getOrgId() });
			if ((userLst != null) && (!(userLst.isEmpty()))) {
				for (TUumsBaseUser user : userLst) {
					List userPrivilLst;
					if ("2".equals(conditionFlag)) {
						userPrivilLst = this.orgDao
								.find("from TUumsBaseUserPrivil userPrivil where userPrivil.userId=?",
										new Object[] { user.getUserId() });
						if (userPrivilLst != null && !userPrivilLst.isEmpty()) {
							for (Object userPrivil : userPrivilLst) {
								this.orgDao.getSession().delete(userPrivil);
							}
						}
					} else if ("3".equals(conditionFlag)) {
						userPrivilLst = this.orgDao
								.find("from TUumsBaseUserPrivil userPrivil where userPrivil.userId=?",
										new Object[] { user.getUserId() });
						if (userPrivilLst != null && !userPrivilLst.isEmpty()) {
							for (Object userPrivil : userPrivilLst) {
								this.orgDao.getSession().delete(userPrivil);
							}
						}

						List userRoleLst = this.orgDao
								.find("from TUumsBaseRoleUser roleUser where roleUser.userId=?",
										new Object[] { user.getUserId() });
						if (userRoleLst != null && !userRoleLst.isEmpty()) {
							for (Object userRole : userRoleLst) {
								this.orgDao.getSession().delete(userRole);
							}
						}

						userGroupLst = this.orgDao
								.find("from TUumsBaseGroupUser groupUser where groupUser.userId=?",
										new Object[] { user.getUserId() });
						if (userGroupLst != null && !userGroupLst.isEmpty()) {
							for (Object userGroup : userGroupLst) {
								this.orgDao.getSession().delete(userGroup);
							}
						}
					}
					user.setSupOrgCode(boOrg.getSupOrgCode());
					this.orgDao.getSession().saveOrUpdate(user);
				}
			}

		}

		List<TUumsBaseOrg> lst = this.orgDao
				.find("from TUumsBaseOrg as baseOrg where baseOrg.orgSyscode like ? and baseOrg.orgIsdel like ? order by baseOrg.orgSequence, baseOrg.orgSyscode",
						new Object[] { iscode + "%", "%" });
		List<TUumsBaseUser> userLst;
		List userPrivilLst;
		List userRoleLst;
		if ((lst != null) && (!(lst.isEmpty())))
			for (TUumsBaseOrg org : lst) {
				org.setOrgSyscode(org.getOrgSyscode().replaceFirst(iscode,
						boOrg.getOrgSyscode()));
				if ("3".equals(conditionFlag))
					org.setSupOrgCode(supOrgCode);
				else {
					org.setSupOrgCode(org.getSupOrgCode().replaceFirst(iscode,
							boOrg.getOrgSyscode()));
				}
				this.orgDao.getSession().saveOrUpdate(org);

				if (!("0".equals(conditionFlag))) {
					userLst = this.orgDao
							.find("from TUumsBaseUser as baseUser where baseUser.orgId = ? order by baseUser.userSequence, baseUser.userSyscode",
									new Object[] { org.getOrgId() });
					if ((userLst != null) && (!(userLst.isEmpty())))
						for (TUumsBaseUser user : userLst) {
							if ("2".equals(conditionFlag)) {
								userPrivilLst = this.orgDao
										.find("from TUumsBaseUserPrivil userPrivil where userPrivil.userId=?",
												new Object[] {
														user.getUserId()});
								if (userPrivilLst != null
										&& !userPrivilLst.isEmpty()) {
									for (Object userPrivil : userPrivilLst) {
										this.orgDao.getSession().delete(
												userPrivil);
									}
								}
							} else if ("3".equals(conditionFlag)) {
								userPrivilLst = this.orgDao
										.find("from TUumsBaseUserPrivil userPrivil where userPrivil.userId=?",
												new Object[] { user.getUserId() });
								if (userPrivilLst != null
										&& !userPrivilLst.isEmpty()) {
									for (Object userPrivil : userPrivilLst) {
										this.orgDao.getSession().delete(
												userPrivil);
									}
								}

								userRoleLst = this.orgDao
										.find("from TUumsBaseRoleUser roleUser where roleUser.userId=?",
												new Object[] { user.getUserId() });
								if (userRoleLst != null
										&& !userRoleLst.isEmpty()) {
									for (Object userRole : userRoleLst) {
										this.orgDao.getSession().delete(
												userRole);
									}
								}

								userGroupLst = this.orgDao
										.find("from TUumsBaseGroupUser groupUser where groupUser.userId=?",
												new Object[] { user.getUserId() });
								if (userGroupLst != null
										&& !userGroupLst.isEmpty()) {
									for (Object userGroup : userGroupLst) {
										this.orgDao.getSession().delete(
												userGroup);
									}
								}
							}
							user.setSupOrgCode(org.getSupOrgCode());
							this.orgDao.getSession().saveOrUpdate(user);
						}
				}
			}
		List supOrgLst;
		TUumsBaseSupOrg supOrg;
		Iterator tmpIterator;
		if ("1".equals(conditionFlag)) {
			supOrgLst = this.orgDao
					.find("from TUumsBaseSupOrg as supOrg where supOrg.orgSyscode like ?",
							new Object[] { iscode + "%" });
			if ((supOrgLst != null) && (!(supOrgLst.isEmpty())))
				for (tmpIterator = supOrgLst.iterator(); tmpIterator.hasNext();) {
					supOrg = (TUumsBaseSupOrg) tmpIterator.next();
					supOrg.setOrgSyscode(supOrg.getOrgSyscode().replaceFirst(
							iscode, boOrg.getOrgSyscode()));
					this.orgDao.getSession().saveOrUpdate(supOrg);
				}
		} else if ("2".equals(conditionFlag)) {
			supOrgLst = this.orgDao
					.find("from TUumsBaseSupOrg as supOrg where supOrg.orgSyscode like ?",
							new Object[] { iscode + "%" });
			if ((supOrgLst != null) && (!(supOrgLst.isEmpty())))
				for (tmpIterator = supOrgLst.iterator(); tmpIterator.hasNext();) {
					supOrg = (TUumsBaseSupOrg) tmpIterator.next();
					supOrg.setOrgSyscode(supOrg.getOrgSyscode().replaceFirst(
							iscode, boOrg.getOrgSyscode()));
					this.orgDao.getSession().saveOrUpdate(supOrg);

					Set<TUumsBaseRole> roleSet = supOrg.getTUumsBaseRoles();
					if ((roleSet != null) && (!(roleSet.isEmpty()))) {
						for (TUumsBaseRole role : roleSet) {
							if (role.getBaseRolePrivils() != null
									&& role.getBaseRolePrivils().size() > 0) {
								for (TUumsBaseRolePrivil trp : role
										.getBaseRolePrivils()) {
									this.orgDao.getSession().delete(trp);
								}
							}
						}
					}

					Set<TUumsBaseGroup> groupSet = supOrg.getTUumsBaseGroups();
					if ((groupSet != null) && (!(groupSet.isEmpty())))
						for (TUumsBaseGroup group : groupSet)
							if (group.getBaseGroupPrivils() != null
									&& group.getBaseGroupPrivils().size() > 0)
								for (TUumsBaseGroupPrivil tgp : group
										.getBaseGroupPrivils()) {
									this.orgDao.getSession().delete(tgp);
								}
				}
		}
	}
	
	
	public List<?> getOrgtree(String flag)
	throws DAOException, SystemException, ServiceException{
		String sql = "SELECT O.ORG_SYSCODE,O.ORG_NAME,O.ORG_ID ,O.ORG_ISDEL FROM T_UUMS_BASE_ORG O WHERE O.ORG_SYSCODE LIKE ? ORDER BY O.ORG_SEQUENCE ASC";
		SQLQuery query = orgDao.getSession().createSQLQuery(sql);
		query.setString(0, flag+"%");
		List dayLists = query
		.list();
		return dayLists;
	}
	
	public List<?> getOrgtree1(String flag)
	throws DAOException, SystemException, ServiceException{
		String sql = "SELECT O.ORG_SYSCODE,O.ORG_NAME,O.ORG_ID ,O.ORG_ISDEL FROM T_UUMS_BASE_ORG O WHERE O.ORG_SYSCODE LIKE ? AND O.ORG_ISDEL='0' ORDER BY O.ORG_SEQUENCE ASC";
		SQLQuery query = orgDao.getSession().createSQLQuery(sql);
		query.setString(0, flag+"%");
		List dayLists = query
		.list();
		return dayLists;
	}
	
	/*
	 * 获得比当前用户排序号大的所选机构所有用户按降序排列
	 * 
	 */
	public List<?> getMaxUserup(Long seq,String orgId)
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT U.USER_ID FROM STRONGINFO.T_UUMS_BASE_USER U WHERE U.ORG_ID = '"+orgId+"' AND U.USER_SEQUENCE<"+seq+" ORDER BY U.USER_SEQUENCE DESC";
		SQLQuery query = userDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		return dayLists;
	}
	/*
	 * 获得比当前用户排序号小的所选机构所有用户按升序排列
	 * 
	 */
	public List<?> getMaxUserdown(Long seq,String orgId)
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT U.USER_ID FROM STRONGINFO.T_UUMS_BASE_USER U WHERE U.ORG_ID = '"+orgId+"' AND U.USER_SEQUENCE>"+seq+" ORDER BY U.USER_SEQUENCE ASC";
		SQLQuery query = userDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		return dayLists;
	}
	
	/*
	 * 更新当前用户信息
	 * 
	 */
	@Transactional(readOnly = false)
	public void updateUser(TUumsBaseUser user)
	throws ServiceException,SystemException, DAOException{
		userDao.update(user);
	}
	
	public TUumsBaseOrg getOrgById(String orgId)
	throws DAOException, SystemException, ServiceException
	{
		TUumsBaseOrg org = orgDao.get(orgId);
		return org;
	}
	
	public String[] getOrgIdsBySubmit()
	throws DAOException, SystemException, ServiceException
	{
		Criterion[] criterion = new Criterion[]{
				Restrictions.ne("orgNature", "4" ),Restrictions.eq("orgIsdel", "0") };
		List<TUumsBaseOrg> lists = new ArrayList<TUumsBaseOrg>();
		
		lists = orgDao.findByCriteria(criterion);
		
		String[] ids = new String[lists.size()];
		if (lists != null) {
			int i = 0;
			for (TUumsBaseOrg org : lists) {
				ids[i] = org.getOrgId();
				i++;
			}
		}
		return ids;
	}
	
}
