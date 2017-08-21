package com.strongit.gzjzyh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
public class QueryApplyService implements IQueryApplyService {
	private GenericDAOHibernate<TGzjzyhApplication, String> queryApplyDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {

		queryApplyDao = new GenericDAOHibernate<TGzjzyhApplication, String>(
				sessionFactory, TGzjzyhApplication.class);
	}

	@Override
	public Page<TGzjzyhApplication> findQueryApplyPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate)
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

		if (appBankuser != null && appBankuser.length() > 0) {
			hql.append(" and t.appBankuser = ?");
			values.add(appBankuser);
		}
		hql.append(" order by t.appId");
		page = this.queryApplyDao.find(page, hql.toString(), values.toArray());
		return page;
	}

}