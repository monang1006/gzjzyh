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
@Transactional(readOnly = true)
public class QueryApplyService implements IQueryApplyService {

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
	public Page<TGzjzyhApplication> findQueryApplyPage(
			Page<TGzjzyhApplication> page, String searchRequiredType,
			String searchAppFileNo, String searchAppBankuser, Date searchAppStartDate,
			Date searchAppEndDate, String searchCaseId, String searchAppStatus)
			throws ServiceException, SystemException, DAOException {
		StringBuffer hql = new StringBuffer(
				"from TGzjzyhApplication t where 1=1");
		List values = new ArrayList();
		//个人账号
		if ("0".equals(searchRequiredType)) {
			hql.append(" and t.appPersonAccount is not null");
		}
		//单位帐号
		else if ("1".equals(searchRequiredType)) {
			hql.append(" and t.appOrgAccount is not null");
		}
		//个人开户明细
		else if ("2".equals(searchRequiredType)) {
			hql.append(" and t.appPersonDetail is not null");
		}
		//单位开户明细
		else if ("3".equals(searchRequiredType)) {
			hql.append(" and t.appOrgDetail is not null");
		}
		//交易明细
		else if ("4".equals(searchRequiredType)) {
			hql.append(" and t.appChadeDetail is not null");
		}
		//
		if (searchAppFileNo != null && searchAppFileNo.length() > 0) {
			hql.append(" and t.appFileno = ?");
			values.add(searchAppFileNo);
		}
		//
		if (searchCaseId != null && searchCaseId.length() > 0) {
			hql.append(" and t.caseId = ?");
			values.add(searchCaseId);
		}

		if (searchAppBankuser != null && searchAppBankuser.length() > 0) {
			hql.append(" and t.appBankuser = ?");
			values.add(searchAppBankuser);
		}
		// 开始时间
		if (searchAppStartDate != null) {
			hql.append(" and t.appDate >= ? ");
			values.add(searchAppStartDate);
		}
		// 结束时间
		if (searchAppEndDate != null) {
			hql.append(" and t.appDate <= ? ");
			values.add(searchAppEndDate);
		}
		if (searchAppStatus != null && searchAppStatus.length() > 0) {
			hql.append(" and t.appStatus = ?");
			values.add(searchAppStatus);
		}

		hql.append(" order by t.appDate desc");
		page = this.queryApplyDao.find(page, hql.toString(), values.toArray());
		return page;
	}

	@Override
	public Page<TGzjzyhCase> findCasePage(Page<TGzjzyhCase> page,
			String caseCode, String caseName)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer("from TGzjzyhCase t where 1=1");
		List values = new ArrayList();

		if (caseCode != null && caseCode.length() > 0) {
			hql.append(" and t.caseCode = ?");
			values.add(caseCode);
		}
		if (caseName != null && caseName.length() > 0) {
			hql.append(" and t.caseName = ?");
			values.add(caseName);
		}
		hql.append(" order by t.caseId");
		page = this.caseDao.find(page, hql.toString(), values.toArray());
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		TGzjzyhCase caseInfo = vo.getGzjzyhCase();
		this.caseDao.save(caseInfo);
		
		TGzjzyhApplication application = vo.getGzjzyhApplication();
		application.setAppStatus(appConstants.STATUS_SUBMIT_NO);
		application.setCaseId(caseInfo.getCaseId());
		this.queryApplyDao.save(application);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String ids)
			throws ServiceException, SystemException, DAOException {
		if ((ids != null) && (!("".equals(ids)))) {
			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				TGzjzyhApplication application = this.getApplicationById(id[i]);
				if(!appConstants.STATUS_SUBMIT_NO.equals(application.getAppStatus())) {
					throw new SystemException();
				}
				this.queryApplyDao.delete(application);
			}
		}
	}

	@Override
	public TGzjzyhApplyVo getApplyById(String appId)
			throws ServiceException, SystemException, DAOException {
		TGzjzyhApplyVo tGzjzyhApplyVo = new TGzjzyhApplyVo();

		TGzjzyhCase gzjzyhCase = null;

		TGzjzyhApplication tGzjzyhApplication = null;

		TGzjzyhUserExtension tGzjzyhUserExtension = null;

		String hql = "from TGzjzyhUserExtension t where ueUserId = ?";

		if (appId != null && !"".equals(appId)) {
			tGzjzyhApplication = this.queryApplyDao.get(appId);

		}
		if (tGzjzyhApplication != null) {

			gzjzyhCase = this.caseDao.get(tGzjzyhApplication.getCaseId());
			if (gzjzyhCase != null) {
				tGzjzyhApplyVo.setGzjzyhCase(gzjzyhCase);
			}

			//
			tGzjzyhUserExtension = (TGzjzyhUserExtension) this.userExtensionDao
					.find(hql,
							new Object[] { tGzjzyhApplication.getAppUserid() })
					.get(0);
			tGzjzyhApplyVo.setGzjzyhApplication(tGzjzyhApplication);

			if (tGzjzyhUserExtension != null) {
				tGzjzyhApplyVo.setGzjzyhUserExtension(tGzjzyhUserExtension);
			}

		}

		return tGzjzyhApplyVo;

	}

	public TGzjzyhApplyVo getExtensionByUserId(String userId)
			throws ServiceException, SystemException, DAOException {
		TGzjzyhApplyVo tGzjzyhApplyVo = new TGzjzyhApplyVo();

		TGzjzyhUserExtension tGzjzyhUserExtension = null;

		String hql = "from TGzjzyhUserExtension t where t.tuumsBaseUser.userId = ?";

		//
		if (userId != null && !"".equals(userId)) {
			tGzjzyhUserExtension = (TGzjzyhUserExtension) this.userExtensionDao
					.findUnique(hql, new Object[] { userId });
		}

		if (tGzjzyhUserExtension != null) {
			tGzjzyhApplyVo.setGzjzyhUserExtension(tGzjzyhUserExtension);
		}

		return tGzjzyhApplyVo;

	}

	/**批量提交
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	@Transactional(readOnly = false)
	public void goCommits(String ids)
			throws ServiceException, SystemException, DAOException {
		if ((ids != null) && (!("".equals(ids)))) {
			String[] id = ids.split(",");
			TGzjzyhApplication gzjzyhApplication = null;
			for (int i = 0; i < id.length; i++) {
				gzjzyhApplication = this.queryApplyDao.get(id[i]);
				if(!appConstants.STATUS_SUBMIT_NO.equals(gzjzyhApplication.getAppStatus())) {
					throw new SystemException();
				}
				gzjzyhApplication.setAppStatus(appConstants.STATUS_SUBMIT_YES);
				this.queryApplyDao.update(gzjzyhApplication);

			}
		}
	}

	/**
	 * 批量撤销
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	@Transactional(readOnly = false)
	public void goBack(String ids)
			throws ServiceException, SystemException, DAOException {
		if ((ids != null) && (!("".equals(ids)))) {
			String[] id = ids.split(",");
			TGzjzyhApplication gzjzyhApplication = null;
			for (int i = 0; i < id.length; i++) {
				gzjzyhApplication = this.queryApplyDao.get(id[i]);
				if(!appConstants.STATUS_SUBMIT_YES.equals(gzjzyhApplication.getAppStatus())
						&& !appConstants.STATUS_AUDIT_BACK.equals(gzjzyhApplication.getAppStatus())
						&& !appConstants.APP_STATUS_REFUSE.equals(gzjzyhApplication.getAppStatus())) {
					throw new SystemException();
				}
				gzjzyhApplication.setAppStatus(appConstants.STATUS_SUBMIT_NO);
				this.queryApplyDao.update(gzjzyhApplication);
			}
		}
	}

	/**保存并提交
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	@Transactional(readOnly = false)
	public void saveOrCommit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		TGzjzyhCase caseInfo = vo.getGzjzyhCase();
		this.caseDao.save(caseInfo);
		
		TGzjzyhApplication application = vo.getGzjzyhApplication();
		application.setAppStatus(appConstants.STATUS_SUBMIT_YES);
		application.setCaseId(caseInfo.getCaseId());
		this.queryApplyDao.save(application);
	}

	@Override
	public TGzjzyhApplyVo getViewById(String appId)
			throws ServiceException, SystemException, DAOException {
		TGzjzyhApplyVo tGzjzyhApplyVo = new TGzjzyhApplyVo();

		TGzjzyhCase gzjzyhCase = null;

		TGzjzyhApplication tGzjzyhApplication = null;

		TGzjzyhUserExtension tGzjzyhUserExtension = null;

		String hql = "from TGzjzyhUserExtension t where ueUserId = ?";

		if (appId != null && !"".equals(appId)) {
			tGzjzyhApplication = this.queryApplyDao.get(appId);

		}
		if (tGzjzyhApplication != null) {

			gzjzyhCase = this.caseDao.get(tGzjzyhApplication.getCaseId());
			if (gzjzyhCase != null) {
				tGzjzyhApplyVo.setGzjzyhCase(gzjzyhCase);
			}

			//
			tGzjzyhUserExtension = (TGzjzyhUserExtension) this.userExtensionDao
					.find(hql,
							new Object[] { tGzjzyhApplication.getAppUserid() })
					.get(0);
			tGzjzyhApplyVo.setGzjzyhApplication(tGzjzyhApplication);

			if (tGzjzyhUserExtension != null) {
				tGzjzyhApplyVo.setGzjzyhUserExtension(tGzjzyhUserExtension);
			}

		}

		return tGzjzyhApplyVo;

	}
	
	public TGzjzyhApplication getApplicationById(String appId)
			throws ServiceException, SystemException, DAOException{
		TGzjzyhApplication tGzjzyhApplication = this.queryApplyDao.get(appId);
		return tGzjzyhApplication;
	}
	
	public TGzjzyhCase getCaseById(String caseId)
			throws ServiceException, SystemException, DAOException{
		TGzjzyhCase caseInfo = this.caseDao.get(caseId);
		return caseInfo;
	}
}