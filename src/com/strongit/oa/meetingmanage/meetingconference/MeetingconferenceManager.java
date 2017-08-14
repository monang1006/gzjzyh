package com.strongit.oa.meetingmanage.meetingconference;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingAttendance;
import com.strongit.oa.bo.ToaMeetingConference;
import com.strongit.oa.bo.ToaMeetingNotice;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.meetingmanage.meetingtopic.MeetingtopicManager;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class MeetingconferenceManager extends BaseManager {

	private GenericDAOHibernate<ToaMeetingConference, String> conferenceDao = null;
	private GenericDAOHibernate<ToaMeetingAttach, String> attachDao = null;
	private GenericDAOHibernate<ToaMeetingTopic, String> topicDao = null;
	private ToaMeetingTopic topic = null; // 议题对象
	private MeetingtopicManager topManager;
	public MeetingtopicManager getTopManager() {
	return topManager;
	}

	@Autowired
	public void setTopManager(MeetingtopicManager topManager) {
		this.topManager = topManager;
	}

	public void MeetingtopicManager() {

	}
	// 统一用户服务
	private IUserService user;

	public IUserService getUser() {
		return user;
	}

	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}

	public MeetingconferenceManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		conferenceDao = new GenericDAOHibernate<ToaMeetingConference, String>(
				sessionFactory, ToaMeetingConference.class);
		attachDao = new GenericDAOHibernate<ToaMeetingAttach, String>(
				sessionFactory, ToaMeetingAttach.class);
		topicDao = new GenericDAOHibernate<ToaMeetingTopic, String>(
				sessionFactory, ToaMeetingTopic.class);
	}
	
	/**
	 * 根据会议ID得到议题对象
	 * 
	 * @author 漆宝华
	 * @date 2011-6-20上午09:44:35
	 * @param noticeId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaMeetingTopic getContopic(String conferenceId) throws DAOException,
			SystemException, ServiceException {
		return topicDao.get(conferenceId);
	}

	// 得到一页会议列表
	public Page<ToaMeetingConference> queryConference(
			Page<ToaMeetingConference> page, String conferenceName,
			String conferenceDemo, Date conferenceStime, String conferenceAddr,
			Date conferenceEndtime, String conferenceStatus)
			throws DAOException, SystemException, ServiceException {

		Calendar cal = Calendar.getInstance();
		Object[] values = new Object[2];
		StringBuffer queryStr = new StringBuffer(
				"from ToaMeetingConference t where 1=1");
		if (conferenceStatus != null && !"".equals(conferenceStatus)) {
			queryStr.append(" and conferenceStatus ='" + conferenceStatus
					+ "'");
		}
		if (conferenceName != null && !"".equals(conferenceName)) {
			queryStr.append(" and conferenceName like '%" + conferenceName
					+ "%'");
		}
		if (conferenceDemo != null && !"".equals(conferenceDemo)) {
			queryStr.append(" and conferenceDemo like '%" + conferenceDemo
					+ "%'");
		}
		if (conferenceAddr != null && !"".equals(conferenceAddr)) {
			queryStr.append(" and conferenceAddr like '%" + conferenceAddr
					+ "%'");
		}

		if (conferenceStime != null && !"".equals(conferenceStime)) {
			queryStr.append(" and conferenceStime>=?");
			values[0] = conferenceStime;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		if (conferenceEndtime != null && !"".equals(conferenceEndtime)) {
			queryStr.append(" and conferenceEndtime<=?");
			cal.setTime(conferenceEndtime);
			cal.add(Calendar.DAY_OF_MONTH, 1); // 加一天
			values[1] = cal.getTime();
		} else {
			queryStr.append(" and 1=?");
			values[1] = 1;
		}
		queryStr.append(" order by t.conferenceStime desc");
		return conferenceDao.find(page, queryStr.toString(), values);

	}		

	public ToaMeetingConference getoneConference(String conferenceId)
			throws DAOException, SystemException, ServiceException {

		return conferenceDao.get(conferenceId);
	}

	@SuppressWarnings("unchecked")
	public List<ToaMeetingAttach> getconferenceAttaches(String conferenceId)
			throws DAOException, SystemException, ServiceException {

		final String hql = "from ToaMeetingAttach as t where t.meetingConference =?";
		return attachDao.find(hql, conferenceId);
	}

	// 删除会议附件
	public void deleteAttaches(String delAttachIds) {
		String[] ids = delAttachIds.split(",");
		attachDao.delete(ids);

	}

	// 保存会议
	public void saveConference(ToaMeetingConference model) throws DAOException,
			SystemException, ServiceException {

		conferenceDao.save(model);

	}
	//保存会议附件
	public void saveConferenceattach(ToaMeetingAttach attach)
			throws DAOException, SystemException, ServiceException {
		attachDao.save(attach);

	}
	//删除会议
	public void deleteConference(String conferenceIds)throws DAOException,
	SystemException, ServiceException {
		String[] ids = conferenceIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			List<ToaMeetingAttach> list = this.getconferenceAttaches(ids[i]);
			if (list.size() != 0 || list != null) {
				attachDao.delete(list);
			}
		
			List<ToaMeetingTopic> listcon=this.getContopicByConId(ids[i]);
			for(Iterator it=listcon.iterator();it.hasNext();){
					ToaMeetingTopic topop = (ToaMeetingTopic)it.next();
					topop.setTopicStatus(MeetingmanageConst.ISAUDIT);//改变议题状态为已审核
					topManager.updateTopic(topop);
					
			}
		}
		conferenceDao.delete(ids);
		
	}
	//根据附件ID得到附件
	public ToaMeetingAttach getconferenceAttache(String id)throws DAOException,
	SystemException, ServiceException  {
		return attachDao.get(id);
	}
	//更新会议
	public void updateCon(ToaMeetingConference conference) {
	
		conferenceDao.update(conference);
	}
	
	//根据会议ID得到议题列表
	public List<ToaMeetingTopic> getContopicByConId(String conferenceId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingTopic as topic where topic.conferenceId =?";
		return topicDao.find(hql, conferenceId);
	}


}
