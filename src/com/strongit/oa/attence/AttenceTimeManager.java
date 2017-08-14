package com.strongit.oa.attence;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.formula.functions.False;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendanceTime;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @author       汤世风
 * @company      Strongit Ltd. (C) copyright
 * @date         2012年9月17日
 * @comment      考勤时间设定Manager
 */
@Service
@Transactional(readOnly=true)
public class AttenceTimeManager extends BaseManager{

	private GenericDAOHibernate<ToaAttendanceTime, java.lang.String> attenceTimeDao = null;

//	统一用户接口
	@Autowired private IUserService userService;
	
	public AttenceTimeManager() {}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attenceTimeDao = new GenericDAOHibernate<ToaAttendanceTime, java.lang.String>(
				sessionFactory, ToaAttendanceTime.class);
	}
	
	/**
	 * 取出时间最新的那条记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ToaAttendanceTime getLastRecord(){
		List<ToaAttendanceTime> attendanceTimes = new ArrayList<ToaAttendanceTime>();
		
		attendanceTimes=attenceTimeDao.getSession()
		.createCriteria(ToaAttendanceTime.class)
		.addOrder(Order.desc("time"))
		.setMaxResults(1).list();
		ToaAttendanceTime attendanceTime=new ToaAttendanceTime();
		if(attendanceTimes.size()>0){
			attendanceTime=attendanceTimes.get(0);
		}
		
		return attendanceTime;
	}
	
	
	/**
	 * 保存参数设置信息
	 * @param attendanceTime
	 */
	@Transactional(readOnly=false)
	public void save(ToaAttendanceTime attendanceTime){
		attenceTimeDao.save(attendanceTime);
	}
	
}
