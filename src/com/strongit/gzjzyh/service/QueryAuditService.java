package com.strongit.gzjzyh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhCommonService;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.appConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.gzjzyh.tosync.IToSyncManager;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class QueryAuditService implements IQueryAuditService {

	private GenericDAOHibernate<TGzjzyhApplication, String> queryApplyDao;

	private GenericDAOHibernate<TGzjzyhCase, String> caseDao;

	private GenericDAOHibernate<TGzjzyhUserExtension, String> userExtensionDao;
	
	@Autowired
	IUserService userService;
	@Autowired
	GzjzyhCommonService commonService;
	@Autowired
	IToSyncManager syncManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {

		queryApplyDao = new GenericDAOHibernate<TGzjzyhApplication, String>(
				sessionFactory, TGzjzyhApplication.class);
		caseDao = new GenericDAOHibernate<TGzjzyhCase, String>(sessionFactory,
				TGzjzyhCase.class);
		userExtensionDao = new GenericDAOHibernate<TGzjzyhUserExtension, String>(
				sessionFactory, TGzjzyhUserExtension.class);
	}

	@Override
	public Page<TGzjzyhApplication> findQueryAuditPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate, String appOrg)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer(
				"from TGzjzyhApplication t where t.appStatus is ?");
		List values = new ArrayList();
		values.add(appConstants.STATUS_SUBMIT_YES);
		//个人账号
		if ("0".equals(accoutType)) {
			hql.append(" and t.appPersonAccount is not null");
		}
		//单位帐号
		else if ("1".equals(accoutType)) {
			hql.append(" and t.appOrgAccount is not null");
		}
		//个人开户明细
		else if ("2".equals(accoutType)) {
			hql.append(" and t.appPersonDetail is not null");
		}
		//单位开户明细
		else if ("3".equals(accoutType)) {
			hql.append(" and t.appOrgDetail is not null");
		}
		//交易明细
		else if ("4".equals(accoutType)) {
			hql.append(" and t.appChadeDetail is not null");
		}
		//
		if (appFileno != null && appFileno.length() > 0) {
			hql.append(" and t.appFileno like ?");
			values.add("%" + appFileno + "%");
		}

		if (appBankuser != null && appBankuser.length() > 0) {
			hql.append(" and t.appBankuser = ?");
			values.add(appBankuser);
		}
		// 开始时间
		if (appStartDate != null) {
			hql.append(" and t.appStartDate >= ? ");
			values.add(appStartDate);
		}
		// 结束时间
		if (appEndDate != null) {
			hql.append(" and t.appEndDate <= ? ");
			values.add(appEndDate);
		}
		if (appOrg != null && !"".equals(appOrg)) {
			hql.append(" and t.appOrg like ?");
			values.add("%" + appOrg + "%");
		}

		hql.append(" order by t.appDate desc");
		page = this.queryApplyDao.find(page, hql.toString(), values.toArray());
		return page;
	}

	@Override
	public Page<TGzjzyhApplication> findQueryCheckedPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate, String appOrg)
			throws ServiceException, SystemException, DAOException {
		User currentUser = this.userService.getCurrentUser();
		
		StringBuffer hql = new StringBuffer(
				"from TGzjzyhApplication t where appAuditUserId = ? ");
		List values = new ArrayList();
		values.add(currentUser.getUserId());
		//个人账号
		if ("0".equals(accoutType)) {
			hql.append(" and t.appPersonAccount is not null");
		}
		//单位帐号
		if ("1".equals(accoutType)) {
			hql.append(" and t.appOrgAccount is not null");
		}
		//个人开户明细
		if ("2".equals(accoutType)) {
			hql.append(" and t.appPersonDetail is not null");
		}
		//单位开户明细
		if ("3".equals(accoutType)) {
			hql.append(" and t.appOrgDetail is not null");
		}
		//交易明细
		if ("4".equals(accoutType)) {
			hql.append(" and t.appChadeDetail is not null");
		}
		//
		if (appFileno != null && appFileno.length() > 0) {
			hql.append(" and t.appFileno like ?");
			values.add("%" + appFileno + "%");
		}

		if (appBankuser != null && appBankuser.length() > 0) {
			hql.append(" and t.appBankuser = ?");
			values.add(appBankuser);
		}
		// 开始时间
		if (appStartDate != null) {
			hql.append(" and t.appStartDate >= ? ");
			values.add(appStartDate);
		}
		// 结束时间
		if (appEndDate != null) {
			hql.append(" and t.appEndDate <= ? ");
			values.add(appEndDate);
		}
		if (appOrg != null && !"".equals(appOrg)) {
			hql.append(" and t.appOrg like ?");
			values.add("%" + appOrg + "%");
		}

		hql.append(" order by t.appDate desc");
		page = this.queryApplyDao.find(page, hql.toString(), values.toArray());
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public void audit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		User user = this.userService.getCurrentUser();
		TGzjzyhApplication gzjzyhApplication = vo.getGzjzyhApplication();
		gzjzyhApplication.setAppAuditUserId(user.getUserId());
		gzjzyhApplication
				.setAppAuditUser(user.getUserName());
		gzjzyhApplication.setAppAuditDate(new Date());
		this.queryApplyDao.update(gzjzyhApplication);
		
		if(appConstants.STATUS_AUDIT_YES.equals(gzjzyhApplication.getAppStatus())) {
			if(GzjzyhApplicationConfig.isDistributedDeployed()) {
				this.syncManager.createToSyncMsg(gzjzyhApplication, GzjzyhConstants.OPERATION_TYPE_ADD_APP);
			}else {
				String smsMsg = "您有一条查询申请需要签收，请登录系统处理。";
				this.commonService.sendSms(gzjzyhApplication.getAppBankuser(), smsMsg);
			}
		}else if(appConstants.STATUS_AUDIT_BACK.equals(gzjzyhApplication.getAppStatus())) {
			String smsMsg = "您有一条查询申请被退回，请登录系统处理。";
			this.commonService.sendSms(gzjzyhApplication.getAppUserid(), smsMsg);
		}
	}
}