package com.strongit.oa.workinspect.workquery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
@OALogger
public class WorkQueryManager extends BaseManager {
	@Autowired
	private JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持

	private GenericDAOHibernate<TOsWorktaskSend, java.lang.String> tOsWorktaskSendDao;

	private GenericDAOHibernate<TUumsBaseUser, java.lang.String> userDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		tOsWorktaskSendDao = new GenericDAOHibernate<TOsWorktaskSend, java.lang.String>(
				sessionFactory, TOsWorktaskSend.class);
		userDao = new GenericDAOHibernate<TUumsBaseUser, java.lang.String>(
				sessionFactory, TUumsBaseUser.class);
	}

	/**
	 * 发送件查询
	 * 
	 * @author niwy 2013年5月8日11:32:252
	 */
	@SuppressWarnings("unchecked")
	public List<ToaReportBean> getSend(String worktaskTitle,
			String worktaskUnitName, String worktaskNo, String managetState,
			String worktaskUser, String worktaskEmerlevel, Date worktaskEtime,
			Date startTime, Date endTime) throws SystemException,
			ServiceException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<ToaReportBean> reportList = new ArrayList<ToaReportBean>();// 报表列表
		ToaReportBean bean = null;// 报表对象
		StringBuilder hql = new StringBuilder(
		" select tw from TOsWorktaskSend tw  where 1=1  ");// 用来存放根据查询条件的sql
		// 存放需要查询的参数值
		List<Object> paramList = new ArrayList<Object>();

		User u = this.userService.getCurrentUser();
		hql.append(" and tw.TOsWorktask.worktaskUser = ? ");
		paramList.add(u.getUserId());
		if (worktaskTitle != null && !"".equals(worktaskTitle)) {
			hql.append(" and ").append(" tw.TOsWorktask.worktaskTitle like ? "); // 名称
			paramList.add("%" + worktaskTitle + "%");
		}
		if (worktaskNo != null && !"".equals(worktaskNo)) { // 工作编号
			hql.append(" and ").append(" tw.TOsWorktask.worktaskNo like ? ");
			paramList.add("%" + worktaskNo + "%");
		}
		if (managetState == null || "".equals(managetState)) { // 工作状态
		} else {
			hql.append(" and ").append(" tw.taskState = ? ");
			paramList.add(managetState);
		}
		if (worktaskEmerlevel == null || "".equals(worktaskEmerlevel)) { // 紧急程度
		} else {
			hql.append(" and ")
					.append(" tw.TOsWorktask.worktaskEmerlevel = ? ");
			paramList.add(worktaskEmerlevel);
		}
		if (worktaskEtime != null && !"".equals(worktaskEtime)) { // 办理时限
			hql.append(" and ").append(" tw.TOsWorktask.worktaskEtime <= ? ");
			paramList.add(worktaskEtime);
		}
		if (startTime != null && !"".equals(startTime)) {
			hql.append(" and ").append(" tw.taskSendTime >= ?"); // 发送开始时间
			paramList.add(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			hql.append(" and ").append(" tw.taskSendTime <= ? "); // 发送结束时间
			paramList.add(endTime);
		}
		hql.append(" order by tw.taskSendTime desc ");// 发送时间排序
		List<TOsWorktaskSend> list = this.tOsWorktaskSendDao.find(hql
				.toString(), paramList.toArray());
	//	 System.out.println(hql.toString());
		if (list != null && !list.isEmpty()) {
			for (TOsWorktaskSend task : list) {
				bean = new ToaReportBean();
				if (task.getTOsWorktask().getWorktaskTitle() != null
						&& !task.getTOsWorktask().getWorktaskTitle().equals("")) {
					bean.setText7(task.getTOsWorktask().getWorktaskTitle());// 设置任务名称
				} else {
					bean.setText7(" ");
				}
				if (task.getTOsWorktask().getWorktaskNo() != null
						&& !task.getTOsWorktask().getWorktaskNo().equals("")) {
					bean.setText12(task.getTOsWorktask().getWorktaskNo()
							.toString());// 设置任务编号
				} else {
					bean.setText12(" ");
				}
				if (task.getTaskRecvId() != null
						&& !task.getTaskRecvId().equals("")) {
					String userNa = this.userService.getUserInfoByUserId(task.getTaskRecvId()).getUserName();
					bean.setText11(userNa
							.toString());// 设置承办人
				} else {
					bean.setText11(" ");
				}
				if (task.getTaskState() != null // 工作状态
						&& !task.getTaskState().equals("")) {
					if (task.getTaskState().equals("0")) {
						bean.setText10("待签收");
					} else if (task.getTaskState().equals("1")) {
						bean.setText10("办理中");
					} else if (task.getTaskState().equals("2")) {
						bean.setText10("已办结");
					}
				} else {
					bean.setText10(" ");
				}
				String stime = "";
				String eTime = "";
				stime = getDateToString(startTime, "yyyy/MM/dd");
				eTime = getDateToString(endTime, "yyyy/MM/dd");
				String time = "";
				time = stime + "--" + eTime;
				if (time != null && !time.equals("")) { // 设置时间段
					bean.setText5(time);
				} else {
					bean.setText5(" ");
				}
				reportList.add(bean);
			}
		}

		return reportList;
	}

	/**
	 * 我的办理件查询
	 * 
	 * @author niwy 2013年5月8日9:30:20
	 */
	@SuppressWarnings("unchecked")
	public List<ToaReportBean> getHandle(String worktaskTitle,
			String worktaskUnitName, String worktaskNo, String managetState,
			String worktaskUser, String worktaskEmerlevel, Date worktaskEtime,
			Date startTime, Date endTime) throws SystemException,
			ServiceException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<ToaReportBean> reportList = new ArrayList<ToaReportBean>();// 报表列表
		ToaReportBean bean = null;// 报表对象
		StringBuilder hql = new StringBuilder(
				" select tw from TOsWorktaskSend tw,TUumsBaseUser u,TUumsBaseUser u2,TUumsBaseOrg o where tw.taskRecvId=u.userId and tw.taskRecvOrgid=o.orgId  ");// 用来存放根据查询条件的sql
		hql.append(" and tw.TOsWorktask.worktaskUser = u2.userId ");
		// 存放需要查询的参数值
		List<Object> paramList = new ArrayList<Object>();

		User u = this.userService.getCurrentUser();
		hql.append(" and tw.taskRecvId = ? ");
		paramList.add(u.getUserId());
		if (worktaskTitle != null && !"".equals(worktaskTitle)) {
			hql.append(" and ").append(" tw.TOsWorktask.worktaskTitle like ? "); // 名称
			paramList.add("%" + worktaskTitle + "%");
		}
		if (worktaskUnitName != null && !"".equals(worktaskUnitName)) { // 发起单位
			hql.append(" and ").append(" o.orgName = ? ");
			paramList.add(worktaskUnitName);
		}
		if (worktaskNo != null && !"".equals(worktaskNo)) { // 工作编号
			hql.append(" and ").append(" tw.TOsWorktask.worktaskNo like ? ");
			paramList.add("%" + worktaskNo + "%");
		}
		if (managetState == null || "".equals(managetState)) { // 工作状态
		} else {
			hql.append(" and ").append(" tw.taskState = ? ");
			paramList.add(managetState);
		}
		if (worktaskUser != null && !"".equals(worktaskUser)) { // 发起人
			hql.append(" and ").append("u2.userName like ? ");
			paramList.add("%"+worktaskUser+"%");
		}
		if (worktaskEmerlevel == null || "".equals(worktaskEmerlevel)) { // 紧急程度
		} else {
			hql.append(" and ")
					.append(" tw.TOsWorktask.worktaskEmerlevel = ? ");
			paramList.add(worktaskEmerlevel);
		}
		if (worktaskEtime != null && !"".equals(worktaskEtime)) { // 办理时限
			hql.append(" and ").append(" tw.TOsWorktask.worktaskEtime <= ? ");
			paramList.add(worktaskEtime);
		}
		if (startTime != null && !"".equals(startTime)) {
			hql.append(" and ").append(" tw.taskSendTime >= ?"); // 发送开始时间
			paramList.add(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			hql.append(" and ").append(" tw.taskSendTime <= ? "); // 发送结束时间
			paramList.add(endTime);
		}
		hql.append(" order by tw.taskSendTime desc ");// 发送时间排序
		List<TOsWorktaskSend> list = this.tOsWorktaskSendDao.find(hql
				.toString(), paramList.toArray());
		System.out.println(hql.toString());
		if (list != null && !list.isEmpty()) {
			for (TOsWorktaskSend task : list) {
				bean = new ToaReportBean();
				if (task.getTOsWorktask().getWorktaskTitle() != null
						&& !task.getTOsWorktask().getWorktaskTitle().equals("")) {
					bean.setText7(task.getTOsWorktask().getWorktaskTitle());// 设置任务名称
				} else {
					bean.setText7(" ");
				}
				if (task.getTOsWorktask().getWorktaskNo() != null
						&& !task.getTOsWorktask().getWorktaskNo().equals("")) {
					bean.setText12(task.getTOsWorktask().getWorktaskNo()
							.toString());// 设置任务编号
				} else {
					bean.setText12(" ");
				}
				if (task.getTOsWorktask().getWorktaskUser() != null
						&& !task.getTOsWorktask().getWorktaskUser().equals("")) {
					User user = userService.getUserInfoByUserId(task
							.getTOsWorktask().getWorktaskUser());
					bean.setText11(user.getUserName());// 发送人
				} else {
					bean.setText11(" ");
				}
				if (task.getTaskState() != null // 工作状态
						&& !task.getTaskState().equals("")) {
					if (task.getTaskState().equals("0")) {
						bean.setText10("待签收");
					} else if (task.getTaskState().equals("1")) {
						bean.setText10("办理中");
					} else if (task.getTaskState().equals("2")) {
						bean.setText10("已办结");
					}
				} else {
					bean.setText10(" ");
				}
				String stime = "";
				String eTime = "";
				stime = getDateToString(startTime, "yyyy/MM/dd");
				eTime = getDateToString(endTime, "yyyy/MM/dd");
				String time = "";
				time = stime + "--" + eTime;
				if (time != null && !time.equals("")) { // 设置时间段
					bean.setText5(time);
				} else {
					bean.setText5(" ");
				}
				reportList.add(bean);
			}
		}

		return reportList;
	}

	/**
	 * 任务办理情况统计
	 * 
	 * @author niwy 2013年5月8日11:30:29
	 */
	@SuppressWarnings("unchecked")
	public List<ToaReportBean> getTaskHandle(String org) throws SystemException,
			ServiceException, ParseException {
		List<ToaReportBean> reportList = new ArrayList<ToaReportBean>();// 报表列表
		ToaReportBean bean = null;// 报表对象
		List<Object[]> list = null;
		if (org  != null && !"".equals(org)) {
			StringBuilder hql = new StringBuilder(
			" select t.userId ,t.orgId,t.userName,o.orgName from TUumsBaseUser t , TUumsBaseOrg o where t.orgId =o.orgId and o.orgName ='"+org+"' ");// 用来存放根据查询条件的sql
			list = userDao.find(hql
					.toString());
		}else{
			StringBuilder hql = new StringBuilder(
					" select t.userId ,t.orgId,t.userName,o.orgName from TUumsBaseUser t , TUumsBaseOrg o where t.orgId =o.orgId ");// 用来存放根据查询条件的sql
					list = userDao.find(hql
							.toString());
		}
		if (list != null && !list.isEmpty()) {
			for (Object[] userIds : list) {
				int dotask = this.getDoTask(userIds[0].toString());//办理任务总数
				int unfinished = this.getUnfinishedTask(userIds[0].toString());//未完成总数
				int timespecific = this.getTimespecificTask(userIds[0].toString());//按时完成总数
				int untimespecific = this.getUnTimespecificTask(userIds[0].toString());//超期完成总数
				if(dotask==0){
				}else{
					bean = new ToaReportBean();
					if (userIds[3].toString()!=null&&!"".equals(userIds[3].toString())) {
						bean.setText1(userIds[3].toString());// 分组
						bean.setText2(userIds[3].toString());// 设置机构名称
					} else {
						bean.setText1(" ");
						bean.setText2(" ");
					}
					if(userIds[2].toString()!=null){
						bean.setText3(userIds[2].toString());
					}else{
						bean.setText3("");
					}
					bean.setText4(""+dotask+"");
					bean.setText5(""+unfinished+"");
					bean.setText6(""+timespecific+"");
					bean.setText7(""+untimespecific+"");
					reportList.add(bean);
				}
			}
		}

		return reportList;
	}

	/**
	 * 获取用户办理的任务数量
	 * @author: qibh
	 *@param userId
	 *@return
	 * @created: 2013-6-6 上午11:54:32
	 * @version :5.0
	 */
	@SuppressWarnings("unchecked")
	public int getDoTask(String userId){
		List<TOsWorktaskSend> list = null;
		StringBuilder unfinishedhql = new StringBuilder(
				" from TOsWorktaskSend tw  where  tw.taskRecvId ='"+userId+"' ");// 获取未办结的数量
		list = this.tOsWorktaskSendDao.find(unfinishedhql
						.toString());
		if(list!=null&&list.size()>0){
			return list.size();
		}else{
			return 0;
		}
	}

	/**
	 * 获取用户未办结的任务数量
	 * @author: qibh
	 *@param userId
	 *@return
	 * @created: 2013-6-6 上午11:54:32
	 * @version :5.0
	 */
	@SuppressWarnings("unchecked")
	public int getUnfinishedTask(String userId){
		List<TOsWorktaskSend> list = null;
		StringBuilder unfinishedhql = new StringBuilder(
				" from TOsWorktaskSend tw  where  tw.taskRecvId ='"+userId+"' and tw.taskState <>'2' ");// 获取未办结的数量
		list = this.tOsWorktaskSendDao.find(unfinishedhql
						.toString());
		if(list!=null&&list.size()>0){
			return list.size();
		}else{
			return 0;
		}
	}
	/**
	 * 获取用户按时办结的任务数量
	 * @author: qibh
	 *@param userId
	 *@return
	 * @created: 2013-6-6 上午11:54:32
	 * @version :5.0
	 */
	@SuppressWarnings("unchecked")
	public int getTimespecificTask(String userId){
		List<TOsWorktaskSend> list = null;
		StringBuilder unfinishedhql = new StringBuilder(
		" from TOsWorktaskSend o ,TOsWorkReviews t where t.sendtaskId=o.sendtaskId  and o.taskRecvId ='"+userId+"' and t.commpleteLevel ='0' ");// 获取按时办结的数量
		list = this.tOsWorktaskSendDao.find(unfinishedhql
				.toString());
		if(list!=null&&list.size()>0){
			return list.size();
		}else{
			return 0;
		}
	}
	/**
	 * 获取用户超期办结的任务数量
	 * @author: qibh
	 *@param userId
	 *@return
	 * @created: 2013-6-6 上午11:54:32
	 * @version :5.0
	 */
	@SuppressWarnings("unchecked")
	public int getUnTimespecificTask(String userId){
		List<TOsWorktaskSend> list = null;
		StringBuilder unfinishedhql = new StringBuilder(
		" from TOsWorktaskSend o ,TOsWorkReviews t where t.sendtaskId=o.sendtaskId  and o.taskRecvId ='"+userId+"' and t.commpleteLevel ='1' ");// 获取超期办结的数量
		list = this.tOsWorktaskSendDao.find(unfinishedhql
				.toString());
		if(list!=null&&list.size()>0){
			return list.size();
		}else{
			return 0;
		}
	}
	/**
	 * 日期转换成字符串
	 * 
	 * @date 2013年5月8日9:28:06
	 * @param date
	 * @param temp
	 * @return
	 */
	public String getDateToString(Date date, String temp) {
		SimpleDateFormat df = new SimpleDateFormat(temp);
		return df.format(date);
	}
}
