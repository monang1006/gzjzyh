package com.strongit.gzjzyh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.gzjzyh.appConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
public class QueryAuditService implements IQueryAuditService {

	private GenericDAOHibernate<TGzjzyhApplication, String> queryApplyDao;

	private GenericDAOHibernate<TGzjzyhCase, String> caseDao;

	private GenericDAOHibernate<TGzjzyhUserExtension, String> userExtensionDao;

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
			String appBankuser, Date appStartDate, Date appEndDate)
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
			hql.append(" and t.appFileno = ?");
			values.add(appFileno);
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

		hql.append(" order by t.appId");
		page = this.queryApplyDao.find(page, hql.toString(), values.toArray());
		return page;
	}

	@Override
	public Page<TGzjzyhApplication> findQueryCheckedPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer(
				"from TGzjzyhApplication t where t.appStatus = ?");
		List values = new ArrayList();
		values.add(appConstants.STATUS_AUDIT_YES);
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
			hql.append(" and t.appFileno = ?");
			values.add(appFileno);
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

		hql.append(" order by t.appId");
		page = this.queryApplyDao.find(page, hql.toString(), values.toArray());
		return page;
	}

	@Override
	public void audit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub

		TGzjzyhApplication gzjzyhApplication = this.queryApplyDao
				.get(vo.getGzjzyhApplication().getAppId());

		gzjzyhApplication.setAppAuditUserId(
				vo.getGzjzyhApplication().getAppAuditUserId());
		gzjzyhApplication
				.setAppAuditUser(vo.getGzjzyhApplication().getAppAuditUser());
		//
		gzjzyhApplication
				.setAppAuditUser(vo.getGzjzyhApplication().getAppAuditUser());

		gzjzyhApplication.setAppStatus(appConstants.STATUS_AUDIT_YES);//已审核
		gzjzyhApplication.setAppAuditDate(new Date());

		this.queryApplyDao.update(gzjzyhApplication);

	}

	public void back(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub

		TGzjzyhApplication gzjzyhApplication = this.queryApplyDao
				.get(vo.getGzjzyhApplication().getAppId());

		gzjzyhApplication.setAppAuditUserId(
				vo.getGzjzyhApplication().getAppAuditUserId());
		gzjzyhApplication
				.setAppAuditUser(vo.getGzjzyhApplication().getAppAuditUser());

		gzjzyhApplication
				.setAppNgReason(vo.getGzjzyhApplication().getAppNgReason());//退回原因
		gzjzyhApplication.setAppStatus(appConstants.STATUS_AUDIT_BACK);//驳回
		gzjzyhApplication.setAppAuditDate(new Date());

		this.queryApplyDao.update(gzjzyhApplication);

	}

	@Override
	public TGzjzyhApplyVo getApplyById(String appId)
			throws ServiceException, SystemException, DAOException {
		TGzjzyhApplyVo tGzjzyhApplyVo = new TGzjzyhApplyVo();

		TGzjzyhCase gzjzyhCase = null;

		TGzjzyhApplication tGzjzyhApplication = null;

		TGzjzyhUserExtension tGzjzyhUserExtension = null;

		String hql = "from TGzjzyhUserExtension t where t.tuumsBaseUser.userId = ?";

		if (appId != null && !"".equals(appId)) {
			tGzjzyhApplication = this.queryApplyDao.get(appId);

		}
		if (tGzjzyhApplication != null) {

			gzjzyhCase = this.caseDao.get(tGzjzyhApplication.getCaseId());
			if (gzjzyhCase != null) {
				tGzjzyhApplyVo.setGzjzyhCase(gzjzyhCase);
			}

			//
			List<TGzjzyhUserExtension> tGzjzyhUserExtensionList = (List<TGzjzyhUserExtension>) this.userExtensionDao
					.find(hql,
							new Object[] { tGzjzyhApplication.getAppUserid() });
			if (tGzjzyhUserExtensionList != null
					&& tGzjzyhUserExtensionList.size() > 0) {
				tGzjzyhUserExtension = tGzjzyhUserExtensionList.get(0);
			}

			tGzjzyhApplyVo.setGzjzyhApplication(tGzjzyhApplication);

			if (tGzjzyhUserExtension != null) {
				tGzjzyhApplyVo.setGzjzyhUserExtension(tGzjzyhUserExtension);
			}

		}

		return tGzjzyhApplyVo;

	}

}