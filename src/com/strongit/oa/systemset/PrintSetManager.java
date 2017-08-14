package com.strongit.oa.systemset;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaPrintSet;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class PrintSetManager {
	
	//统一用户服务
	private IUserService user;

	private GenericDAOHibernate<ToaPrintSet, String> printSetDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.printSetDao = new GenericDAOHibernate<ToaPrintSet, String>(sessionFactory,ToaPrintSet.class);
	}
	
	public void addPrintSet(ToaPrintSet printset) throws SystemException, ServiceException {
		try {
			String userId = user.getCurrentUser().getUserId();
			printset.setUserId(userId);
			this.printSetDao.save(printset);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "读取系统配置" });
		}
	}
	
	public ToaPrintSet getPrintSet(String senddocId)
			throws SystemException, ServiceException {
		try {

			Object[] param = new Object[2];
			StringBuffer hql = new StringBuffer("from ToaPrintSet t where 1=1 ");
			String userId = user.getCurrentUser().getUserId();
			if (userId != null && senddocId != null) {
				hql.append(" and t.userId =? and t.senddocId=?");
				param[0] = userId;
				param[1] = senddocId;
				List list = printSetDao.find(hql.toString(), param);
				if (list.size() != 0) {
					return ((ToaPrintSet) list.get(0));
				} else
					return null;
			} else
				   return null;
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "读取系统配置" });
		}
	}
	
	public void updatePrintSet(ToaPrintSet printset) throws SystemException, ServiceException {
		try {
			this.printSetDao.save(printset);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "读取系统配置" });
		}
	}
	
	@Autowired
	public void setUser(IUserService aUser) {
		user = aUser;
	}
	
}
