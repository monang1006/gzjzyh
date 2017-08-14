/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理 日程"日程附件" 业务类
 */
package com.strongit.oa.calendar;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAfficheAttach;
import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarAttach;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 处理 日程"日程附件" 业务类
 * @Create Date: 2009-2-12
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class CalendarAttachManager {
	
	private GenericDAOHibernate<ToaCalendarAttach, java.lang.String> calendarAttachDao;

	@Autowired
	public void setSessionFactory(SessionFactory session) {
		calendarAttachDao = new GenericDAOHibernate<ToaCalendarAttach, java.lang.String>(
				session, ToaCalendarAttach.class);
	}
	
	/**
	 * author:luosy
	 * description:  保存附件
	 * modifyer:
	 * description:
	 * @param remind
	 */
	@Transactional
	public String saveAttach(String attachId,ToaCalendar toaCalendar) throws SystemException,ServiceException{
		try{
			ToaCalendarAttach attach = new ToaCalendarAttach();
			attach.setAttachId(attachId);
			attach.setToaCalendar(toaCalendar);
			calendarAttachDao.save(attach);
			return attach.getCalAttachId();
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存日程附件"});
		}
		
	}
	/**
	 * author:luosy
	 * description: 删除公告附件
	 * modifyer:
	 * description:
	 * @param attachId 公共附件表对象的ID
	 */
	public void delAttach(String attachId) throws SystemException,ServiceException{
		try{
			List l= calendarAttachDao.findByProperty("attachId", attachId);
			if(l.size()>0){
				ToaCalendarAttach attach = (ToaCalendarAttach)l.get(0);
				calendarAttachDao.delete(attach);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除日程附件"});
		}
	}
	/**
	 * 根据日程id  获取附件中间表的所有记录
	 * author  taoji
	 * @param page
	 * @param CalendarId
	 * @return
	 * @date 2014-1-10 上午11:12:14
	 */
	public Page<ToaCalendarAttach> getAllToaCalendarAttachByCalendarId(Page<ToaCalendarAttach> page,String CalendarId){
		StringBuffer sb = new StringBuffer();
		sb.append(" from ToaCalendarAttach t where t.toaCalendar.calendarId = '"+CalendarId+"'");
		return calendarAttachDao.find(page, sb.toString());
	}
}
