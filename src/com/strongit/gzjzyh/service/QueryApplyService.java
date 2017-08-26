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
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate,
			String caseCode)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer(
				"from TGzjzyhApplication t where 1=1");
		List values = new ArrayList();
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
		//
		if (caseCode != null && caseCode.length() > 0) {
			hql.append(" and t.caseCode = ?");
			values.add(caseCode);
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
		// TODO Auto-generated method stub
		this.caseDao.save(vo.getGzjzyhCase());
		//
		vo.getGzjzyhApplication().setAppStatus(appConstants.STATUS_SUBMIT_NO);
		vo.getGzjzyhApplication().setCaseId(vo.getGzjzyhCase().getCaseId());
		this.queryApplyDao.save(vo.getGzjzyhApplication());

	}

	@Override
	public void update(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub

		this.caseDao.update(vo.getGzjzyhCase());

		vo.getGzjzyhApplication().setAppStatus(appConstants.STATUS_SUBMIT_NO);
		this.queryApplyDao.update(vo.getGzjzyhApplication());

	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String ids)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub
		if ((ids != null) && (!("".equals(ids)))) {
			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				this.queryApplyDao.delete(id[i]);
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

		String hql = "from TGzjzyhUserExtension t where ueUserId = ?";

		//
		if (userId != null && "".equals(userId)) {

			tGzjzyhUserExtension = (TGzjzyhUserExtension) this.userExtensionDao
					.find(hql, new Object[] { userId }).get(0);
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
	public void goCommits(String ids)
			throws ServiceException, SystemException, DAOException {
		List<TGzjzyhApplication> queryList = new ArrayList<TGzjzyhApplication>();
		if ((ids != null) && (!("".equals(ids)))) {
			String[] id = ids.split(",");
			TGzjzyhApplication gzjzyhApplication = null;
			for (int i = 0; i < id.length; i++) {
				gzjzyhApplication = this.queryApplyDao.get(id[i]);

				gzjzyhApplication.setAppStatus(appConstants.STATUS_SUBMIT_YES);
				queryList.add(gzjzyhApplication);

			}
			this.queryApplyDao.update(queryList);
		}
	}

	/**批量提交
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void goBack(String ids)
			throws ServiceException, SystemException, DAOException {
		List<TGzjzyhApplication> queryList = new ArrayList<TGzjzyhApplication>();
		if ((ids != null) && (!("".equals(ids)))) {
			String[] id = ids.split(",");
			TGzjzyhApplication gzjzyhApplication = null;
			for (int i = 0; i < id.length; i++) {
				gzjzyhApplication = this.queryApplyDao.get(id[i]);
				gzjzyhApplication.setAppStatus(appConstants.STATUS_SUBMIT_NO);
				queryList.add(gzjzyhApplication);

			}
			this.queryApplyDao.update(queryList);
		}
	}

	/**保存并提交
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void saveOrCommit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		this.caseDao.save(vo.getGzjzyhCase());
		//
		vo.getGzjzyhApplication().setCaseId(vo.getGzjzyhCase().getCaseId());
		vo.getGzjzyhApplication().setAppStatus(appConstants.STATUS_SUBMIT_YES);
		this.queryApplyDao.save(vo.getGzjzyhApplication());

	}

	@Override
	public void updateOrCommit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException {
		// TODO Auto-generated method stub

		this.caseDao.update(vo.getGzjzyhCase());
		vo.getGzjzyhApplication().setAppStatus(appConstants.STATUS_SUBMIT_YES);
		this.queryApplyDao.update(vo.getGzjzyhApplication());

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
}