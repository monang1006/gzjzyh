package com.strongit.oa.meetingmanage.meetingnotice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.meetingmanage.meetingconference.MeetingconferenceManager;
import com.strongit.oa.meetingmanage.meetingtopic.MeetingtopicManager;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class MeetingnoticeManager {
	private GenericDAOHibernate<ToaMeetingNotice, String> noticeDao = null;
	private GenericDAOHibernate<ToaMeetingAttach, String> attachDao = null;
	private GenericDAOHibernate<ToaMeetingAttendance, String> danceDao = null;
	private GenericDAOHibernate<ToaMeetingTopic, String> topicDao = null;
	private IUserService userService;
	private MeetingconferenceManager conManager;

	public MeetingconferenceManager getConManager() {
		return conManager;
	}

	@Autowired
	public void setConManager(MeetingconferenceManager conManager) {
		this.conManager = conManager;
	}

	public MeetingnoticeManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		noticeDao = new GenericDAOHibernate<ToaMeetingNotice, String>(
				sessionFactory, ToaMeetingNotice.class);
		attachDao = new GenericDAOHibernate<ToaMeetingAttach, String>(
				sessionFactory, ToaMeetingAttach.class);
		danceDao = new GenericDAOHibernate<ToaMeetingAttendance, String>(
				sessionFactory, ToaMeetingAttendance.class);
		topicDao = new GenericDAOHibernate<ToaMeetingTopic, String>(
				sessionFactory, ToaMeetingTopic.class);
	}

	/**
	 * 根据用户ID得到用户姓名
	 * 
	 * @author 蒋国斌
	 *@date 2009-4-3 上午11:26:08
	 * @param userId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public String getUserName(String userId) throws SystemException,
			ServiceException {
		try {
			return userService.getUserNameByUserId(userId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "当前用户信息" });
		}
	}

	/**
	 * 根据通知ID得到一个通知对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:44:35
	 * @param noticeId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaMeetingNotice getConnotice(String noticeId) throws DAOException,
			SystemException, ServiceException {
		return noticeDao.get(noticeId);
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
	public ToaMeetingTopic getContopic(String conId) throws DAOException,
			SystemException, ServiceException {
		return topicDao.get(conId);
	}

	/**
	 * 保存会议通知对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:45:40
	 * @param toNotice
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveConnotice(ToaMeetingNotice toNotice) throws DAOException,
			SystemException, ServiceException {
		noticeDao.save(toNotice);
	}

	@Transactional(readOnly = false)
	public void updateConnotice(ToaMeetingNotice toNotice) throws DAOException,
			SystemException, ServiceException {
		noticeDao.update(toNotice);
	}

	/**
	 * 支持批量删除会议通知
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:46:11
	 * @param sortIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteNotices(String notId) throws DAOException,
			SystemException, ServiceException {
		String[] ids = notId.split(",");
		for (int i = 0; i < ids.length; i++) {
			List<ToaMeetingAttach> list = this.getNoticeAttaches(ids[i]);
			if (list.size() != 0 || list != null) {
				attachDao.delete(list);
			}
			ToaMeetingNotice notice = noticeDao.get(ids[i]);
			String conId = notice.getConferenceId();
			if (conId != null) {
				ToaMeetingConference conference = conManager
						.getoneConference(conId);
				conference.setConferenceStatus("0");
				conManager.updateCon(conference);
			}
		}

		noticeDao.delete(ids);

	}

	@Transactional(readOnly = false)
	public void deleteNotice(ToaMeetingNotice notice) throws DAOException,
			SystemException, ServiceException {
		noticeDao.delete(notice);
	}

	/**
	 * 保存会议通知附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:03:59
	 * @param attach
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveNoticeattach(ToaMeetingAttach attach) throws DAOException,
			SystemException, ServiceException {
		attachDao.save(attach);
	}

	/**
	 * 删除通知附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:04:22
	 * @param sortIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteAttaches(String sortIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = sortIds.split(",");
		attachDao.delete(ids);
	}

	/**
	 * 删除指定会议通知下的所有附件
	 * 
	 * @author 蒋国斌
	 *@date 2009-3-31 上午10:12:34
	 * @param noticeId
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteNoticeAttaches(String noticeId) throws DAOException,
			SystemException, ServiceException {
		List<ToaMeetingAttach> list = this.getNoticeAttaches(noticeId);
		attachDao.delete(list);
	}

	/**
	 * 根据会议通知ID得到其下面的附件列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:04:34
	 * @param topicId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaMeetingAttach> getNoticeAttaches(String noticeId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingAttach as attach where attach.meetingNotice=?";
		return attachDao.find(hql, noticeId);
	}

	/**
	 * 根据附件ID得到附件
	 * 
	 */
	public ToaMeetingAttach getNoticeAttache(String id) throws DAOException,
			SystemException, ServiceException {

		return attachDao.get(id);
	}
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void savemeetingNotice(ToaMeetingNotice model, String delAttachIds,
			String meetingId, String userId, File[] file, String[] fileName) {
		if ("".equals(model.getNoticeId())) {
			model.setNoticeId(null);
		}
		if (delAttachIds != null && !"".equals(delAttachIds)) {
			conManager.deleteAttaches(delAttachIds);
		}
		model.setNoticeIs("0");
		model.setConferenceId(meetingId);
		model.setNoticeUsers(userId);
		this.saveConnotice(model);

		if (file != null) {
			for (int i = 0; i < file.length; i++) {

				ToaMeetingAttach attach = new ToaMeetingAttach();
				attach.setMeetingNotice(model.getNoticeId());
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file[i]);
					byte[] buf = new byte[(int) file[i].length()];
					fis.read(buf);
					attach.setAttachCon(buf);
					attach.setAttachName(fileName[i]);
					// String ext =
					// fileName[i].substring(fileName[i].lastIndexOf(".")+1);

				} catch (Exception e) {
					System.out.print("上传失败！" + e);
				} finally {
					try {
						fis.close();
					} catch (IOException e) {

						System.out.print("文件关闭失败！" + e);
					}
				}

			}
		}
	}

	// 一页会议通知列表
	public Page<ToaMeetingNotice> queryConnotice(Page<ToaMeetingNotice> page,
			String noticeTitle, String noticeAddr, Date noticeStime,
			Date noticeEndTime, String noticeIs) {
		Calendar cal = Calendar.getInstance();
		Object[] values = new Object[2];
		StringBuffer queryStr = new StringBuffer(
				"from ToaMeetingNotice t where 1=1");
		if (noticeTitle != null && !"".equals(noticeTitle)) {
			queryStr.append(" and noticeTitle like '%" + noticeTitle + "%'");
		}
		if (noticeAddr != null && !"".equals(noticeAddr)) {
			queryStr.append(" and noticeAddr like '%" + noticeAddr + "%'");
		}

		if (noticeStime != null && !"".equals(noticeStime)) {
			queryStr.append(" and noticeStime>=?");
			values[0] = noticeStime;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		if (noticeEndTime != null && !"".equals(noticeEndTime)) {
			queryStr.append(" and noticeEndTime<=?");
			cal.setTime(noticeEndTime);
			cal.add(Calendar.DAY_OF_MONTH, 1); // 加一天
			values[1] = cal.getTime();
		} else {
			queryStr.append(" and 1=?");
			values[1] = 1;
		}
		if (noticeIs != null && !"".equals(noticeIs)) {
			queryStr.append(" and noticeIs like '%" + noticeIs + "%'");
		}

		queryStr.append(" order by noticeStime desc");
		return noticeDao.find(page, queryStr.toString(), values);
	}

	/**
	 * 保存会议考勤情况记录
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:03:59
	 * @param attach
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveMeetingAttendance(ToaMeetingAttendance dance)
			throws DAOException, SystemException, ServiceException {
		danceDao.save(dance);
	}

	/**
	 * 根据会议通知ID得到指定的考勤情况列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:47:51
	 * @param topicId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaMeetingAttendance> getConAttendances(String notId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingAttendance as dance where dance.noticeId =?";
		return danceDao.find(hql, notId);
	}

	@Transactional(readOnly = false)
	public void deleteSummarytens(String notId) throws DAOException,
			SystemException, ServiceException {
		List<ToaMeetingAttendance> list = getConAttendances(notId);
		danceDao.delete(list);
	}

	public void deleteAttendance(String noticeId) {
		List<ToaMeetingAttendance> list = this.getConAttendances(noticeId);
		danceDao.delete(list);

	}
	//根据会议ID得到会议通知列表
	public List<ToaMeetingNotice> getConnoticeByConId(String conId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingNotice as notice where notice.conferenceId =?";
		return danceDao.find(hql, conId);
	}

	//根据会议ID得到议题列表
	public List<ToaMeetingTopic> getContopicByConId(String conId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingTopic as topic where topic.conferenceId =?";
		return topicDao.find(hql, conId);
	}


}
