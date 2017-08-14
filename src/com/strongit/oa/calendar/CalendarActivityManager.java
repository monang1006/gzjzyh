/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理 日程"日程活动" 业务类
 */
package com.strongit.oa.calendar;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarActivity;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 处理日程"日程活动"业务类
 * @Create Date: 2009-2-12
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class CalendarActivityManager {
	private GenericDAOHibernate<ToaCalendarActivity, java.lang.String> calendarActivityDao;

	private IUserService userService;

	private GenericDAOHibernate<ToaCalendar, java.lang.String> calendarDao;
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	/**
	 * @param session
	 * @roseuid 49586BD00232
	 */
	@Autowired
	public void setSessionFactory(SessionFactory session) {
		calendarActivityDao = new GenericDAOHibernate<ToaCalendarActivity, java.lang.String>(
				session, ToaCalendarActivity.class);
		calendarDao = new GenericDAOHibernate<ToaCalendar, java.lang.String>(
				session, ToaCalendar.class);
	}
	
	/**
	 * author:luosy
	 * description: 查找所有的活动分类
	 * modifyer:
	 * description:
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ToaCalendarActivity> getAllActivity(String userId) throws SystemException,ServiceException{
		try{
			if("".equals(userId)|null==userId){
				userId = userService.getCurrentUser().getUserId();
			}
//			return calendarActivityDao.findByProperty("userId", userId);
			return calendarActivityDao.find("from ToaCalendarActivity t where t.userId=? and t.activityIsDel=? order by t.activityName ", userId,ToaCalendarActivity.ACTIVITY_NOTDEL);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找日程活动"});
		}
	}
	/**
	 * author:luosy
	 * description: 获取默认活动分类
	 * modifyer:
	 * description:
	 * @return
	 */
	public ToaCalendarActivity getActivity(String activityName) throws SystemException,ServiceException{
		try{
			ToaCalendarActivity activity = new ToaCalendarActivity();
			String userId = userService.getCurrentUser().getUserId();
			String[] par={userId,activityName,ToaCalendarActivity.ACTIVITY_NOTDEL};
			List  l  = calendarActivityDao.find("from ToaCalendarActivity t where t.userId=? and t.activityName=? and t.activityIsDel=?",par);
			
			if(l.size()>0){
				activity =(ToaCalendarActivity) l.get(0);
			}else{
				activity.setActivityName(ToaCalendarActivity.DEFINE_ACTIVITY);
				activity.setActivityRemark("系统自动添加的活动分类");
				activity.setUserId(userId);
				activity.setActivityIsDel(ToaCalendarActivity.ACTIVITY_NOTDEL);
				calendarActivityDao.save(activity);
			}
			return activity;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取默认活动分类"});
		}
	}
	/**
	 * author:luosy
	 * description: 保存活动分类
	 * modifyer:
	 * description:
	 * @return
	 */
	public ToaCalendarActivity saveActivity(ToaCalendarActivity activity) throws SystemException,ServiceException{
		try{
			if(activity.getActivityId()==null|"".equals(activity.getActivityId())){
				activity.setActivityId(null);
			}
			
			if(activity.getUserId()==null){
				String userId = userService.getCurrentUser().getUserId();
				activity.setUserId(userId);
			}
			
			
			activity.setActivityIsDel(ToaCalendarActivity.ACTIVITY_NOTDEL);
			calendarActivityDao.save(activity);
			return activity;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存日程活动"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description:获取活动分类
	 * modifyer:
	 * description:
	 * @param activityId  活动分类ID
	 * @return
	 */
	public ToaCalendarActivity getActivityById(String activityId) throws SystemException,ServiceException{
		try{
			ToaCalendarActivity activity = calendarActivityDao.findById(activityId, true);
			return activity;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找某个日程活动"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 删除活动分类
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete(String activityId) throws SystemException,ServiceException,AjaxException{
		try{
			ToaCalendarActivity actv = this.getActivityById(activityId);
			if(ToaCalendarActivity.DEFINE_ACTIVITY.equals(actv.getActivityName())){
				return "false";
			}else{
//				calendarActivityDao.delete(activityId);
				actv.setActivityIsDel(ToaCalendarActivity.ACTIVITY_DEL);
				return "删除成功！";
			}
		}catch(ServiceException e){
			throw new AjaxException(MessagesConst.del_error,               
					new Object[] {"删除日程活动"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 删除多个活动分类
	 * modifyer:
	 * description:
	 * @return
	 */
	public String deleteMore(String activityIds) throws SystemException,ServiceException,AjaxException{
		try{
			String ids[] = activityIds.split(",");
			
			for(int i=0;i<ids.length;i++){
				if(!"".equals(ids[i])){
					ToaCalendarActivity actv = this.getActivityById(ids[i]);
						actv.setActivityIsDel(ToaCalendarActivity.ACTIVITY_DEL);
						Set set=actv.getToaCalendars();
						Iterator<ToaCalendar> it = set.iterator();  
						while(it.hasNext()){
							ToaCalendar cal=(ToaCalendar)it.next();
							calendarDao.delete(cal);
						}
					}
				}
			return "删除成功！";
		}catch(ServiceException e){
			throw new AjaxException(MessagesConst.del_error,               
					new Object[] {"删除日程活动"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 活动分类名是否已存在
	 * modifyer:
	 * description:
	 * @param activityName 活动分类名
	 * @return 	true	已存在
	 * 			false	不存在
	 */
	public boolean isActivityExist(String activityName) throws SystemException,ServiceException{
		try{
			String userId = userService.getCurrentUser().getUserId();
			String[] par={userId,activityName,ToaCalendarActivity.ACTIVITY_NOTDEL};
			List<ToaCalendarActivity>  l  = calendarActivityDao.find("from ToaCalendarActivity t where t.userId=? and t.activityName=? and t.activityIsDel=? ",par);
			if(l.size()<1){
				return false;
			}else{
				return true;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"判断日程活动是否已存在"});
		}
	}
	
	/**
	 * author:hecj
	 * description:根据活动名与用户名同时查询
	 *
	 *
	 */
	public String isActivityExist(String activityName,String userId) throws SystemException,ServiceException{
		try{
			String[] par={userId,activityName,ToaCalendarActivity.ACTIVITY_NOTDEL};
			List<ToaCalendarActivity>  l  = calendarActivityDao.find("from ToaCalendarActivity t where t.userId=? and t.activityName=? and t.activityIsDel=? ",par);
			if(l!=null&&l.size()<1){
				return null;
			}else{
				return l.get(0).getActivityId();
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"判断日程活动是否已存在"});
		}
	}
}
