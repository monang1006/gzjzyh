/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理日程共享manager业务类
 */
package com.strongit.oa.calendar;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarShare;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;


/**
 * 处理日程共享manager业务类
 * @Create Date: 2009-2-12
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class CalendarShareManager {
	private GenericDAOHibernate<ToaCalendarShare, java.lang.String> calendarShareDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory session) {
		calendarShareDao = new GenericDAOHibernate<ToaCalendarShare, java.lang.String>(
				session, ToaCalendarShare.class);
	}
	
	/**
	 * author:luosy
	 * description: 保存
	 * modifyer:
	 * description:
	 * @param shareToUserIds
	 * @param cal
	 */
	public void saveShare(String shareToUserIds,ToaCalendar cal) throws SystemException,ServiceException{
		try{
			delExistShareCals(cal.getCalendarId());
			if(!"".equals(shareToUserIds)&&shareToUserIds!=null){
				String id[] = shareToUserIds.split(",");
				for(int i=0;i<id.length;i++){
					id[i] = id[i].replace("id", "");
					ToaCalendarShare calShare = new ToaCalendarShare();
					calShare.setToaCalendar(cal);
					calShare.setUserId(id[i]);
					calendarShareDao.save(calShare);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"日程共享"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 日程是否为共享日程
	 * modifyer:
	 * description:
	 * @param CalId 日程ID
	 * @return
	 */
	public boolean isShareCal(String CalId)throws SystemException,ServiceException{
		try{
			List l = calendarShareDao.findByProperty("toaCalendar.calendarId", CalId);
			if(l.size()>0){
				return true;
			}else{
				return false;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"日程共享"});
		}
	}
	
	/**
	 * author:luosy
	 * description:删除已存在的共享记录
	 * modifyer:
	 * description:
	 * @param CalId 日程id
	 */
	public void delExistShareCals(String CalId) throws SystemException,ServiceException{
		try{
			List l = calendarShareDao.findByProperty("toaCalendar.calendarId", CalId);
			if(l.size()>0){
				calendarShareDao.delete(l);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除共享记录"});
		}
	}
	
	/**
	 * author:luosy
	 * description:查找某用户的所有共享记录
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Object[]> getAllShare(String userId) throws SystemException,ServiceException{
		try{
			List leaderCalList = calendarShareDao.find("select distinct t.toaCalendar.userId,t.toaCalendar.calUserName from ToaCalendarShare as t where t.userId = '"+userId+"'");
			return leaderCalList;
//			return calendarShareDao.findByProperty("userId", userId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"查找共享记录"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"查找共享记录"});
		}
	}
}
