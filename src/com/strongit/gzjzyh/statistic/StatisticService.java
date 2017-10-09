package com.strongit.gzjzyh.statistic;

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
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.organisemanage.IOrgManager;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class StatisticService implements IStatisticService {

	private GenericDAOHibernate<TGzjzyhApplication, String> queryApplyDao;

	@Autowired
	IUserService userService;
	@Autowired
	GzjzyhCommonService commonService;
	@Autowired
	IToSyncManager syncManager;
	@Autowired
	IOrgManager orgManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		queryApplyDao = new GenericDAOHibernate<TGzjzyhApplication, String>(
				sessionFactory, TGzjzyhApplication.class);
	}

	@Override
	public List<Object[]> efficiencyStatistic(boolean isOrder, String orderColumn, String orderType)
			throws ServiceException, SystemException, DAOException {
		StringBuilder sqlBuilder = new StringBuilder("");
		sqlBuilder.append("select min(duringTime) as minTime, max(duringTime) as maxTime, avg(duringTime) as avgTime, userName from ")
					.append("(select TO_NUMBER((APP_RESPONSE_DATE - APP_RECEIVE_DATE)*24*60*60) as duringTime, USER_NAME as userName from T_GZJZYH_APPLICATION ")
					.append("left join T_UUMS_BASE_USER on APP_BANKUSER=USER_ID where APP_RESPONSE_DATE is not null) ")
					.append("group by userName ");
		if(isOrder) {
			if("maxTime".equalsIgnoreCase(orderColumn)) {
				sqlBuilder.append("order by maxTime ");
			}else if("minTime".equalsIgnoreCase(orderColumn)) {
				sqlBuilder.append("order by minTime ");
			}else if("avgTime".equalsIgnoreCase(orderColumn)) {
				sqlBuilder.append("order by avgTime ");
			}
			if("desc".equalsIgnoreCase(orderType)) {
				sqlBuilder.append("desc");
			}
		}
		
		List<Object[]> lst = this.queryApplyDao.getSession().createSQLQuery(sqlBuilder.toString()).list();
		
		return lst;
	}

	@Override
	public List<Object[]> timeStatistic(boolean isOrder, String orderColumn, String orderType)
			throws ServiceException, SystemException, DAOException {
		StringBuilder sqlBuilder = new StringBuilder("");
		sqlBuilder.append("select yearNum, monthNum, APP_TYPE, count(1) from ")
					.append("(select TO_CHAR(APP_DATE, 'yyyy') as yearNum, TO_CHAR(APP_DATE, 'mm') as monthNum, APP_TYPE from T_GZJZYH_APPLICATION) ")
					.append("group by yearNum, monthNum, APP_TYPE order by yearNum, monthNum, APP_TYPE ");
		if(isOrder) {
			//待处理
		}
		
		List<Object[]> lst = this.queryApplyDao.getSession().createSQLQuery(sqlBuilder.toString()).list();
		
		return lst;
	}

	@Override
	public List<Object[]> areaStatistic(boolean isOrder, String orderColumn, String orderType)
			throws ServiceException, SystemException, DAOException {
		StringBuilder sqlBuilder = new StringBuilder("");
		sqlBuilder.append("select ORG_NAME, APP_TYPE, count(1) from ")
					.append("(select ORG_NAME, APP_TYPE from T_GZJZYH_APPLICATION left join T_UUMS_BASE_ORG on APP_ORGID=ORG_ID) ")
					.append("group by ORG_NAME, APP_TYPE order by ORG_NAME, APP_TYPE");
		if(isOrder) {
			//待处理
		}
		
		List<Object[]> lst = this.queryApplyDao.getSession().createSQLQuery(sqlBuilder.toString()).list();
		
		return lst;
	}

	@Override
	public List<Object[]> bankStatistic(boolean isOrder, String orderColumn, String orderType)
			throws ServiceException, SystemException, DAOException {
		StringBuilder sqlBuilder = new StringBuilder("");
		sqlBuilder.append("select USER_NAME, APP_STATUS, count(1) from ")
					.append("(select USER_NAME, APP_STATUS from T_GZJZYH_APPLICATION ")
					.append("left join T_UUMS_BASE_USER on APP_BANKUSER=USER_ID where APP_STATUS='2' or APP_STATUS='4' or APP_STATUS='5') ")
					.append("group by USER_NAME, APP_STATUS order by USER_NAME, APP_STATUS");
		if(isOrder) {
			//待处理
		}
		
		List<Object[]> lst = this.queryApplyDao.getSession().createSQLQuery(sqlBuilder.toString()).list();
		
		return lst;
	}

	@Override
	public List<String> getAllOrgNames() throws ServiceException,
			SystemException, DAOException {
		List<String> result = new ArrayList<String>(0);
		String hql = "from TUumsBaseOrg where orgSyscode like '001___' and orgSyscode != '" + GzjzyhConstants.DEFAULT_BANKORG_SYSCODE + "' and orgIsdel = '0' order by orgSyscode";
		List<TUumsBaseOrg> lst = this.queryApplyDao.find(hql, null);
		if(lst != null && !lst.isEmpty()){
			for(TUumsBaseOrg org : lst){
				result.add(org.getOrgName());
			}
		}
		return result;
	}

	@Override
	public List<String> getAllBankNames() throws ServiceException,
			SystemException, DAOException {
		List<String> result = new ArrayList<String>(0);
		TUumsBaseOrg bankOrg = this.orgManager.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
		String hql = "from TUumsBaseUser where orgId = '" + bankOrg.getOrgId() + "' and userIsdel = '0' and userIsactive = '1' order by userSequence";
		List<TUumsBaseUser> lst = this.queryApplyDao.find(hql, null);
		if(lst != null && !lst.isEmpty()){
			for(TUumsBaseUser user : lst){
				result.add(user.getUserName());
			}
		}
		return result;
	}
	
	

	
}