package com.strongit.oa.meetingmanage.meetingsummary;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.archive.ITempFileService;
import com.strongit.oa.archive.tempfile.TempFileType;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingSummary;
import com.strongit.oa.bo.ToaMeetingTodo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class MeetingsummaryManager {

	private GenericDAOHibernate<ToaMeetingSummary, String> summaryDao = null;

	private GenericDAOHibernate<ToaMeetingAttach, String> attachDao = null;

	private GenericDAOHibernate<ToaMeetingTodo, String> todoDao = null;
	@Autowired
	IUserService userService;// 统一用户服务
	@Autowired
	ITempFileService tempFileManager;// 文件归档Manager

	public MeetingsummaryManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		summaryDao = new GenericDAOHibernate<ToaMeetingSummary, String>(
				sessionFactory, ToaMeetingSummary.class);
		attachDao = new GenericDAOHibernate<ToaMeetingAttach, String>(
				sessionFactory, ToaMeetingAttach.class);

		todoDao = new GenericDAOHibernate<ToaMeetingTodo, String>(
				sessionFactory, ToaMeetingTodo.class);

	}

	/**
	 * 得到一页会议记要列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-30 上午11:58:55
	 * @param page
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaMeetingSummary> queryConsummary(
			Page<ToaMeetingSummary> sumpage, String summtitle,String summaddr, Date summtime, String isguidang)
			throws DAOException, SystemException, ServiceException {
		// Calendar cal=Calendar.getInstance();
		Object[] values = new Object[1];
		StringBuffer queryStr = new StringBuffer(
				"from ToaMeetingSummary t where 1=1");

		if (summtitle != null && !"".equals(summtitle)) {
			queryStr.append(" and t.summaryTitle like '%" + summtitle + "%'");

		}
		
		if (summaddr != null && !"".equals(summaddr)) {
			queryStr.append(" and t.summaryAddr like '%" + summaddr + "%'");

		}
		
		
		if (isguidang != null && !"".equals(isguidang)) {
			queryStr.append(" and t.isGuidang like '%" + isguidang + "%'");

		}
		if (summtime != null && !"".equals(summtime)) {
			queryStr.append(" and t.summaryTime<=?");
			// cal.setTime(summDate);
			// cal.add(Calendar.DAY_OF_MONTH,1); //加一天
			// values[0] = cal.getTime();
			values[0] = summtime;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}

		queryStr.append(" order by t.summaryTime desc");
		return summaryDao.find(sumpage, queryStr.toString(), values);
	}

	/**
	 * 根据会议记要ID得到指定会议记要对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-30 下午01:47:57
	 * @param sortId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaMeetingSummary getConsummary(String summaryId)
			throws DAOException, SystemException, ServiceException {
		return summaryDao.get(summaryId);
	}

	/**
	 * 保存会议记要
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:29:12
	 * @param toSort
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveConsummary(ToaMeetingSummary summary)
			throws DAOException, SystemException, ServiceException {
		summaryDao.save(summary);
	}

	@Transactional(readOnly = false)
	public void updateConsummary(ToaMeetingSummary summary)
			throws DAOException, SystemException, ServiceException {
		summaryDao.update(summary);
	}

	/**
	 * 支持批量删除会议记要
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:29:41
	 * @param sortIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteSummarys(String sumIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = sumIds.split(",");
		summaryDao.delete(ids);
	}

	/**
	 * 保存会议记要附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:03:59
	 * @param attach
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveSummaryattach(ToaMeetingAttach attach)
			throws DAOException, SystemException, ServiceException {
		attachDao.save(attach);
	}

	/**
	 * 删除会议纪要附件
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
	 * 删除指定会议纪要下的所有附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:12:34
	 * @param noticeId
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteSummaryAttaches(String summaryId) throws DAOException,
			SystemException, ServiceException {
		List<ToaMeetingAttach> list = this.getSummaryAttaches(summaryId);
		attachDao.delete(list);
	}

	/**
	 * 根据会议纪要ID得到其下面的附件列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:04:34
	 * @param topicId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<ToaMeetingAttach> getSummaryAttaches(String summaryId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingAttach as attach where attach.meetingSummary =?";
		return attachDao.find(hql, summaryId);
	}

	/**
	 * 保存会议待办事项
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:41:53
	 * @param todo
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveMeetingTodo(ToaMeetingTodo todo) throws DAOException,
			SystemException, ServiceException {
		todoDao.save(todo);
	}

	/**
	 * 删除会议纪要下的所有待办事项
	 * 
	 * @author 蒋国斌
	 *@date 2009-4-8 上午10:03:02
	 * @param summaryId
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteSummaryTodos(String summaryId) throws DAOException,
			SystemException, ServiceException {
		List<ToaMeetingTodo> list = this.getSummaryTodos(summaryId);
		todoDao.delete(list);
	}

	public List<ToaMeetingTodo> getSummaryTodos(String summaryId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingTodo as todo where todo.meetingSummary.summaryId =?";
		return todoDao.find(hql, summaryId);
	}

	/**
	 * 归档会议纪要
	 * 
	 * @author Administrator蒋国斌 StrongOA2.0_DEV 2010-4-7 上午10:02:20
	 * @param meetid
	 * @return
	 * @throws SystemException
	 */
	public boolean addTemplateFile(String meetid) throws SystemException {

//		List<ToaMeetingSummary> list = this.getTopicsummarys(meetid);
	 ToaMeetingSummary mod =this.getConsummary(meetid);
//		if (list.size() != 0 && list != null)
	//		mod = (ToaMeetingSummary) list.get(0);
//		String id = mod.getSummaryId();

		ToaArchiveTempfile tempFile = new ToaArchiveTempfile();
	if (meetid == null || meetid.equals("")) {
			return true;
		}
		User user = userService.getCurrentUser();
		tempFile.setTempfileAuthor(user.getUserName());
		tempFile.setTempfileDate(new Date());// 成文日期

		tempFile.setTempfileDepartment(userService.getUserDepartmentByUserId(
				user.getUserId()).getOrgId());
		tempFile.setTempfileDesc(mod.getSummaryContent());// 附注	
		tempFile.setTempfileDocId(mod.getSummaryId());
		tempFile.setTempfileDocType(TempFileType.MEETING);
		 tempFile.setTempfileNo(mod.getSummaryTitle());//会议字号
		tempFile.setTempfileTitle(mod.getSummaryTitle());// 标题
		Set<ToaArchiveTfileAppend> tempFileAppend = new HashSet<ToaArchiveTfileAppend>();

		List<ToaMeetingAttach> attachList = this.getSummaryAttaches(meetid);
		if (attachList != null) {
			Iterator it = attachList.iterator();
			while (it.hasNext()) {
				ToaMeetingAttach att = (ToaMeetingAttach) it.next();
				ToaArchiveTfileAppend tappend = new ToaArchiveTfileAppend();
				tappend.setTempappendContent(att.getAttachCon());
				// tappend.setTempappendName(tempFile.getTempfileTitle());
				tappend.setTempappendName(att.getAttachName());
				tempFileAppend.add(tappend);
			}

		}
		tempFile.setToaArchiveTfileAppends(tempFileAppend);

		tempFileManager.saveTempfile(tempFile);
		mod.setIsGuidang("1");
		this.updateConsummary(mod);
		return true;
	}
	/**
	 * 根据会议通知ID得到会议记要LIST对象
	 * 
	 * @author 申仪玲
	 * @date 
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaMeetingSummary> getConsummarys(String notId)
		throws DAOException, SystemException, ServiceException {
			final String hql = "from ToaMeetingSummary as summary where summary.noticeId =?";
			return summaryDao.find(hql, notId);
	}

}
