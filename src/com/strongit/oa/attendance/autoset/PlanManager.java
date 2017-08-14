package com.strongit.oa.attendance.autoset;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendPlan;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 考勤执行计划MANAGER
 * @author 胡丽丽
 * @date 2010-03-16
 *
 */
@Service
@Transactional
public class PlanManager {

	private GenericDAOHibernate<ToaAttendPlan, String> planDao;

	public PlanManager() {

	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		planDao = new GenericDAOHibernate<ToaAttendPlan, String>(sessionFactory,ToaAttendPlan.class);
	}
	/**
	 * 保存设置信息
	 * @author hull
	 * @date 2010-03-16
	 * @param model
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void save(ToaAttendPlan model)throws SystemException,ServiceException{
		try {
			planDao.save(model);
		} catch (Exception e) {
			 throw new ServiceException(MessagesConst.save_error,new Object[]{"信息保存出错！"});
		}
	}
	
	/**
	 * 查询设置条件
	 * @author hull
	 * @date 2010-03-16
	 * @return 计划设置条件BO对象
	 * @throws com.strongmvc.exception.SystemException
	 * @throws ServiceException
	 */
	public ToaAttendPlan getPlan()throws SystemException,ServiceException{
		String hql="from ToaAttendPlan ";
		List<ToaAttendPlan> planlist=planDao.find(hql);
		if(planlist!=null&&planlist.size()>0){
			return planlist.get(0);
		}else{
			return new ToaAttendPlan();
		}
	}
}
