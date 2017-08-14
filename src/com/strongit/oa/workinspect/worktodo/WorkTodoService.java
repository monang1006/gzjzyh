package com.strongit.oa.workinspect.worktodo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.TOsManagementSummary;
import com.strongit.oa.bo.TOsTaskAttach;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.PathUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class WorkTodoService implements IWorkTodoService {
	private GenericDAOHibernate<TOsWorktaskSend, java.lang.String> tOsWorktaskSendDao;
	private GenericDAOHibernate<TOsTaskAttach, java.lang.String> taskAttachDao;
	private GenericDAOHibernate<TOsManagementSummary, java.lang.String> summaryDao;
	
	private GenericDAOHibernate<ToaAttachment, String> attachDao;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IUserService userService;

//	公共附件接口
	@Autowired
	private IAttachmentService attachmentService;
	
	/**
	 * 注册DAO
	 * 
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		tOsWorktaskSendDao = new GenericDAOHibernate<TOsWorktaskSend, java.lang.String>(
				sessionFactory, TOsWorktaskSend.class);
		taskAttachDao = new GenericDAOHibernate<TOsTaskAttach, java.lang.String>(
				sessionFactory, TOsTaskAttach.class);
		summaryDao = new GenericDAOHibernate<TOsManagementSummary, java.lang.String>(
				sessionFactory, TOsManagementSummary.class);
		attachDao = new GenericDAOHibernate<ToaAttachment, java.lang.String>(
				sessionFactory, ToaAttachment.class);
	}
	
	
	public Page<TOsWorktaskSend> getAllTOsWork(
			Page<TOsWorktaskSend> page, TOsWorktaskSend tOsWorktaskSend)
			throws DAOException, SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId");// 查询个人未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 工作标题
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskTitle() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskTitle())) {
				hql.append(" and tw.TOsWorktask.worktaskTitle like ?");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskTitle()));
			}
			// 工作来源(根据接收单位ID个人ID)
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskUserName() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskUserName())) {
				hql.append(" and u.userName like ? ");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskUserName()));
			}

			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getUserId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间TaskRecvTime
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskSendTime desc ");// 发送时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();
				String imgState1 = "<img src='../../frame/theme_red_12/images/red.gif'>";
				String imgState2 = "<img src='../../frame/theme_red_12/images/blue.gif'>";
				String imgState3 = "<img src='../../frame/theme_red_12/images/green.gif'>";
				String imgState4 = "<img src='../../frame/theme_red_12/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					int num = (page.getPageNo()-1)*page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend work = result.get(i);
						work.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(work
								.getTOsWorktask().getWorktaskUser());
						TUumsBaseOrg org = userService.getOrgInfoByOrgId(u
								.getOrgId());
						work.getTOsWorktask()
								.setWorktaskUserName(
										org.getOrgName() + "/("
												+ u.getUserName() + ")");
						String imgState = "";
						String sendStateF = "";
						String sendState = work.getTaskState();
						Date endTime = work.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);
						StringBuffer recvName = new StringBuffer("");
						Set<TOsWorktaskSend> sends = work.getTOsWorktask()
								.getTOsWorktaskSends();
						int n = 0;

						if ("0".equals(sendState)) {
							sendStateF = "(<font color='red'>待接收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						//work.setRest1(imgState);
						work.setRestImg(imgState);
						work.getTOsWorktask().setWorktaskSender(
								recvName.toString());
						result.set(i, work);
					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}
	

	/**
	 * Description：得到个人待办工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	
	public Page<TOsWorktaskSend> getAllTOsWorktaskSend(
			Page<TOsWorktaskSend> page, TOsWorktaskSend tOsWorktaskSend)
			throws DAOException, SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='1' and (tw.TOsWorktask.rest1 !='0' or tw.TOsWorktask.rest1 is null) ");// 查询个人未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 工作标题
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskTitle() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskTitle())) {
				hql.append(" and tw.TOsWorktask.worktaskTitle like ?");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskTitle()));
			}
			// 工作来源(根据接收单位ID个人ID)
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskUserName() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskUserName())) {
				hql.append(" and u.userName like ? ");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskUserName()));
			}

			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getUserId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}else{
				hql.append(" and tw.taskState !='2'  ");
			}
			// 结束时间TaskRecvTime
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskSendTime desc ");// 发送时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();
				String imgState1 = "<img src='../../frame/theme_red_12/images/red.gif'>";
				String imgState2 = "<img src='../../frame/theme_red_12/images/blue.gif'>";
				String imgState3 = "<img src='../../frame/theme_red_12/images/green.gif'>";
				String imgState4 = "<img src='../../frame/theme_red_12/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					int num = (page.getPageNo()-1)*page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend work = result.get(i);
						work.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(work
								.getTOsWorktask().getWorktaskUser());
						TUumsBaseOrg org = userService.getOrgInfoByOrgId(u
								.getOrgId());
						work.getTOsWorktask()
								.setWorktaskUserName(
										org.getOrgName() + "/("
												+ u.getUserName() + ")");
						String imgState = "";
						String sendStateF = "";
						String sendState = work.getTaskState();
						Date endTime = work.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);
						StringBuffer recvName = new StringBuffer("");
						Set<TOsWorktaskSend> sends = work.getTOsWorktask()
								.getTOsWorktaskSends();
						int n = 0;

						if ("0".equals(sendState)) {
							sendStateF = "(<font color='red'>待接收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						//work.setRest1(imgState);
						work.setRestImg(imgState);
						work.getTOsWorktask().setWorktaskSender(
								recvName.toString());
						result.set(i, work);
					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	/**
	 * Description：得到个人 未签收工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getSend(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='1' and tw.taskState='0'");// 查询个人未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getUserId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskSendTime desc ");// 发送时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		return page;
	}
	

	/**
	 * Description：得到部门待办工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getUnitWork(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='0' ");// 查询单位未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 工作标题
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskTitle() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskTitle())) {
				hql.append(" and tw.TOsWorktask.worktaskTitle like ?");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskTitle()));
			}
			// 工作来源(根据接收单位ID个人ID)
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskUserName() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskUserName())) {
				hql.append(" and u.userName like ? ");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskUserName()));
			}

			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getOrgId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskSendTime desc ");// 接发送时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();
				String imgState1 = "<img src='../../frame/theme_red_12/images/red.gif'>";
				String imgState2 = "<img src='../../frame/theme_red_12/images/blue.gif'>";
				String imgState3 = "<img src='../../frame/theme_red_12/images/green.gif'>";
				String imgState4 = "<img src='../../frame/theme_red_12/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					int num = (page.getPageNo()-1)*page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend work = result.get(i);
						work.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(work
								.getTOsWorktask().getWorktaskUser());
						TUumsBaseOrg org = userService.getOrgInfoByOrgId(u
								.getOrgId());
						work.getTOsWorktask()
								.setWorktaskUserName(
										org.getOrgName() + "/("
												+ u.getUserName() + ")");
						String imgState = "";
						String sendStateF = "";
						String sendState = work.getTaskState();
						Date endTime = work.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);
						StringBuffer recvName = new StringBuffer("");
						Set<TOsWorktaskSend> sends = work.getTOsWorktask()
								.getTOsWorktaskSends();
						int n = 0;
						for (TOsWorktaskSend sender : sends) {
							if (++n > 1) {
								recvName
										.append("<span style='padding-left:10px;'>");
							} else {
								recvName.append("<span>");
							}
							String recvId = sender.getTaskRecvId();
							String recvType = sender.getTaskType();
						}
						if ("0".equals(sendState)) {
							sendStateF = "(<font color='red'>待接收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						//work.setRest1(imgState);
						work.setRestImg(imgState);
						work.getTOsWorktask().setWorktaskSender(
								recvName.toString());
						result.set(i, work);
					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	public TOsWorktaskSend getById(String id) {
		return this.tOsWorktaskSendDao.get(id);
	}

	/**
	 * 保存附件
	 * 
	 * @param task
	 * @param attachName
	 * @param attachCon
	 * @return
	 */
	public void saveAttachment(TOsManagementSummary summary,
			String[] fileNames, File[] files, Boolean isTaskSummary) {
		if (files != null) {
			int arr = files.length;
			for (int i = 0; i < arr; i++) {

				String attachName = fileNames[i];
				TOsTaskAttach attach = new TOsTaskAttach();
				String attachType = attachName.substring(attachName
						.lastIndexOf(".") + 1, attachName.length());

				attach.setAttachSummaryId(summary.getSummaryId());// 办理纪要id
				attach.setAttachFileName(attachName);// 文件名
				attach.setAttachFileType(attachType);// 文件类型
				attach.setAttachFileDate(new Date());// 上传时间
				String rootPath = PathUtil.getRootPath();// 得到工程根路径
				String dir = rootPath + "uploadfile";
				attach.setFileServer(ServletActionContext.getRequest()
						.getServerName());// 文件服务器
				attach.setAttachFilePath(dir);// 文件路径
				byte[] buff = new byte[(int)files[i].length()];
				String ext = fileNames[i].substring(fileNames[i].lastIndexOf(".") + 1,
						fileNames[i].length());
				//添加公共附件表数据
				Calendar cals = Calendar.getInstance();
				String attachId = attachmentService.saveAttachment(fileNames[i], buff, cals.getTime(), ext, "1", "注:任务工单附件", userService.getCurrentUser().getUserId());
				attach.setAttachRest2(attachId);
				taskAttachDao.save(attach);

				OutputStream os = null;
				InputStream is = null;

				try {
					FileInputStream fis = null;
					fis = new FileInputStream(files[i]);
					byte[] buf = new byte[(int) files[i].length()];
					fis.read(buf);
					File file = new File(dir);
					if (!file.exists()) {
						file.mkdir();
					}
					os = new FileOutputStream(dir + File.separator
							+ attach.getAttachId() + "."
							+ attach.getAttachFileType());
					os.write(buf, 0, buf.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
						}
					}
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
						}
					}
				}

			}
		}
	}

	public void save(TOsWorktaskSend tOsWorktaskSend, File[] file,
			String[] fileFileName) throws ServiceException, SystemException {
		this.tOsWorktaskSendDao.save(tOsWorktaskSend);
	}

	/**
	 * 删除办理纪要附件
	 * 
	 * @param attId
	 * @param selectedData
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void delAttach(String attId)
			throws ServiceException, SystemException{
		TOsTaskAttach attach = taskAttachDao.get(attId);
		String rest2 = attach.getAttachRest2();//删除附件的同时删除公告附件表的附件
		attachmentService.deleteAttachment(rest2);
		taskAttachDao.delete(attId);
	}
	
	/**
	 * 保存办理纪要
	 * @param summary
	 * @param file
	 * @param fileFileName
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void save(TOsManagementSummary summary, File[] file,
			String[] fileFileName) throws ServiceException, SystemException {
		// 更新对应的任务下发
		TOsWorktaskSend send = summary.getTOsWorktaskSend();
		send = this.tOsWorktaskSendDao.get(send.getSendtaskId());
		if ("1".equals(summary.getManagetState())) {
			// 如果工作状态为完成
			send.setTaskState("2");
			send.setTaskcompleteTime(new Date());
//			send.setTaskRecvTime(new Date());
		} else {
			send.setTaskState("1");
			send.setTaskRecvTime(new Date());
		}
		summary.setManagetTime(new Date());
		User u = this.userService.getCurrentUser();
		send.setOperateUserid(u.getUserId());
		this.tOsWorktaskSendDao.update(send);

		if(summary.getSummaryId()!=null && !"".equals(summary.getSummaryId())){
			this.summaryDao.update(summary);
		}else{
			summary.setSummaryId(null);
			this.summaryDao.save(summary);
		}
		this.saveAttachment(summary, fileFileName, file, Boolean.FALSE);
	}

	/*
	 * 签收工作
	 */
	public void siGn(TOsWorktaskSend summary) throws ServiceException,
			SystemException {
		summary = this.tOsWorktaskSendDao.get(summary.getSendtaskId());
		summary.setTaskState("1");
		summary.setTaskRecvTime(new Date());
		this.tOsWorktaskSendDao.update(summary);
	}

	/**
	 * Description：得到个人待办工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getAllTOsWorktaskTodo(
			Page<TOsWorktaskSend> page, TOsWorktaskSend tOsWorktaskSend)
			throws DAOException, SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='1' and tw.taskState='1'");// 查询个人未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {

			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getUserId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskRecvTime desc ");// 接收时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		return page;
	}

	/**
	 * Description：得到部门待签收工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getWorkTobe(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='0' and tw.taskState='0'");// 查询单位未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 当前登录用户待接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getOrgId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskSendTime desc");// 接发送时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();
				String imgState1 = "<img src='../../frame/theme_red_12/images/red.gif'>";
				String imgState2 = "<img src='../../frame/theme_red_12/images/blue.gif'>";
				String imgState3 = "<img src='../../frame/theme_red_12/images/green.gif'>";
				String imgState4 = "<img src='../../frame/theme_red_12/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					int num = (page.getPageNo()-1)*page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend work = result.get(i);
						work.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(work
								.getTOsWorktask().getWorktaskUser());
						TUumsBaseOrg org = userService.getOrgInfoByOrgId(u
								.getOrgId());
						work.getTOsWorktask()
								.setWorktaskUserName(
										org.getOrgName() + "/("
												+ u.getUserName() + ")");
						String imgState = "";
						String sendStateF = "";
						String sendState = work.getTaskState();
						Date endTime = work.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);
						StringBuffer recvName = new StringBuffer("");
						Set<TOsWorktaskSend> sends = work.getTOsWorktask()
								.getTOsWorktaskSends();
						int n = 0;
						for (TOsWorktaskSend sender : sends) {
							if (++n > 1) {
								recvName
										.append("<span style='padding-left:10px;'>");
							} else {
								recvName.append("<span>");
							}
							String recvId = sender.getTaskRecvId();
							String recvType = sender.getTaskType();
						}
						if ("0".equals(sendState)) {
							sendStateF = "(<font color='red'>待接收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						//work.setRest1(imgState);
						work.setRestImg(imgState);
						work.getTOsWorktask().setWorktaskSender(
								recvName.toString());
						result.set(i, work);
					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	/**
	 * Description：得到部门待办理工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getWorkTodo(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='0' and tw.taskState='1'");// 查询单位待办理
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getOrgId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskSendTime desc ");// 按发送时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();
				String imgState1 = "<img src='../../frame/theme_red_12/images/red.gif'>";
				String imgState2 = "<img src='../../frame/theme_red_12/images/blue.gif'>";
				String imgState3 = "<img src='../../frame/theme_red_12/images/green.gif'>";
				String imgState4 = "<img src='../../frame/theme_red_12/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					int num = (page.getPageNo()-1)*page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend work = result.get(i);
						work.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(work
								.getTOsWorktask().getWorktaskUser());
						TUumsBaseOrg org = userService.getOrgInfoByOrgId(u
								.getOrgId());
						work.getTOsWorktask()
								.setWorktaskUserName(
										org.getOrgName() + "/("
												+ u.getUserName() + ")");
						String imgState = "";
						String sendStateF = "";
						String sendState = work.getTaskState();
						Date endTime = work.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);
						StringBuffer recvName = new StringBuffer("");
						Set<TOsWorktaskSend> sends = work.getTOsWorktask()
								.getTOsWorktaskSends();
						int n = 0;
						for (TOsWorktaskSend sender : sends) {
							if (++n > 1) {
								recvName
										.append("<span style='padding-left:10px;'>");
							} else {
								recvName.append("<span>");
							}
							String recvId = sender.getTaskRecvId();
							String recvType = sender.getTaskType();
						}
						if ("0".equals(sendState)) {
							sendStateF = "(<font color='red'>待接收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						//work.setRest1(imgState);
						work.setRestImg(imgState);
						work.getTOsWorktask().setWorktaskSender(
								recvName.toString());
						result.set(i, work);
					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	/**
	 * Description：得到个人所有未签收工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getAllnotsign(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='1' and tw.taskState='0'");// 查询个人未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 工作标题
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskTitle() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskTitle())) {
				hql.append(" and tw.TOsWorktask.worktaskTitle like ?");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskTitle()));
			}
			// 工作来源(根据接收单位ID个人ID)
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskUserName() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskUserName())) {
				hql.append(" and u.userName like ? ");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskUserName()));
			}

			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getUserId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskSendTime desc ");// 接发送时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();
				String imgState1 = "<img src='../../frame/theme_red_12/images/red.gif'>";
				String imgState2 = "<img src='../../frame/theme_red_12/images/blue.gif'>";
				String imgState3 = "<img src='../../frame/theme_red_12/images/green.gif'>";
				String imgState4 = "<img src='../../frame/theme_red_12/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					int num = (page.getPageNo()-1)*page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend work = result.get(i);
						work.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(work
								.getTOsWorktask().getWorktaskUser());
						work.getTOsWorktask().setWorktaskUserName(
								u.getUserName());
						String imgState = "";
						String sendStateF = "";
						String sendState = work.getTaskState();
						Date endTime = work.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);
						StringBuffer recvName = new StringBuffer("");
						Set<TOsWorktaskSend> sends = work.getTOsWorktask()
								.getTOsWorktaskSends();
						int n = 0;
						for (TOsWorktaskSend sender : sends) {
							if (++n > 1) {
								recvName
										.append("<span style='padding-left:10px;'>");
							} else {
								recvName.append("<span>");
							}
							String recvId = sender.getTaskRecvId();
							String recvType = sender.getTaskType();
							if ("1".equals(recvType)) {
								// 个人承办
								User recvU = userService
										.getUserInfoByUserId(recvId);
								TUumsBaseOrg org = userService
										.getOrgInfoByOrgId(recvU.getOrgId());
								recvName.append(org.getOrgName()).append("/")
										.append(recvU.getUserName()).append(
												"</span><br/>");
							} else {
								// 单位承办
								TUumsBaseOrg org = userService
										.getOrgInfoByOrgId(recvId);
								recvName.append(org.getOrgName()).append("/")
										.append("</span><br/>");
							}
						}
						if ("0".equals(sendState)) {
							sendStateF = "(<font color='red'>待接收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						//work.setRest1(imgState);
						work.setRestImg(imgState);
						work.getTOsWorktask().setWorktaskSender(
								recvName.toString());
						result.set(i, work);
					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	/**
	 * Description：得到个人所有办理中工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getAllnotTodo(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException {
		StringBuilder hql = new StringBuilder(
				"select tw from TOsWorktaskSend tw,TUumsBaseUser u where tw.TOsWorktask.worktaskUser=u.userId and tw.taskType='1' and tw.taskState='1'");// 查询个人未签收
		List<Object> parmList = new ArrayList<Object>();
		if (tOsWorktaskSend != null && !"".equals(tOsWorktaskSend)) {
			// 工作标题
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskTitle() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskTitle())) {
				hql.append(" and tw.TOsWorktask.worktaskTitle like ?");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskTitle()));
			}
			// 工作来源(根据接收单位ID个人ID)
			if (tOsWorktaskSend.getTOsWorktask().getWorktaskUserName() != null
					&& !"".equals(tOsWorktaskSend.getTOsWorktask()
							.getWorktaskUserName())) {
				hql.append(" and u.userName like ? ");
				parmList.add(CommonMethod.addPercentSign(tOsWorktaskSend
						.getTOsWorktask().getWorktaskUserName()));
			}

			// 当前登录用户待接收、接收的
			User u = this.userService.getCurrentUser();
			hql.append(" and tw.taskRecvId = ? ");
			parmList.add(u.getUserId());

			// 查询状态
			if (tOsWorktaskSend.getTaskState() != null
					&& !"".equals(tOsWorktaskSend.getTaskState())) {
				hql.append(" and tw.taskState = ? ");
				parmList.add(tOsWorktaskSend.getTaskState());
			}
			// 结束时间
			if (tOsWorktaskSend.getTaskSendTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskSendTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskSendTime = sdf.format(tOsWorktaskSend
						.getTaskSendTime());// 转换日期至字符串
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')<=? ");
				parmList.add(taskSendTime);
			}
			// 开始时间
			if (tOsWorktaskSend.getTaskRecvTime() != null
					&& !"".equals(tOsWorktaskSend.getTaskRecvTime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String taskcompleteTime = sdf.format(tOsWorktaskSend
						.getTaskRecvTime());
				hql.append(" and to_char(tw.taskSendTime,'yyyy-MM-dd')>=?");
				parmList.add(taskcompleteTime);
			}
		}
		hql.append(" order by tw.taskRecvTime desc ");// 接收时间排序
		page = this.tOsWorktaskSendDao.find(page, hql.toString(), parmList
				.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();
				String imgState1 = "<img src='../../frame/theme_red_12/images/red.gif'>";
				String imgState2 = "<img src='../../frame/theme_red_12/images/blue.gif'>";
				String imgState3 = "<img src='../../frame/theme_red_12/images/green.gif'>";
				String imgState4 = "<img src='../../frame/theme_red_12/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					int num = (page.getPageNo()-1)*page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend work = result.get(i);
						work.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(work
								.getTOsWorktask().getWorktaskUser());
						work.getTOsWorktask().setWorktaskUserName(
								u.getUserName());
						String imgState = "";
						String sendStateF = "";
						String sendState = work.getTaskState();
						Date endTime = work.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);
						StringBuffer recvName = new StringBuffer("");
						Set<TOsWorktaskSend> sends = work.getTOsWorktask()
								.getTOsWorktaskSends();
						int n = 0;
						for (TOsWorktaskSend sender : sends) {
							if (++n > 1) {
								recvName
										.append("<span style='padding-left:10px;'>");
							} else {
								recvName.append("<span>");
							}
							String recvId = sender.getTaskRecvId();
							String recvType = sender.getTaskType();
							if ("1".equals(recvType)) {
								// 个人承办
								User recvU = userService
										.getUserInfoByUserId(recvId);
								TUumsBaseOrg org = userService
										.getOrgInfoByOrgId(recvU.getOrgId());
								recvName.append(org.getOrgName()).append("/")
										.append(recvU.getUserName()).append(
												"</span><br/>");
							} else {
								// 单位承办
								TUumsBaseOrg org = userService
										.getOrgInfoByOrgId(recvId);
								recvName.append(org.getOrgName()).append("/")
										.append("</span><br/>");
							}
						}
						if ("0".equals(sendState)) {
							sendStateF = "(<font color='red'>待接收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						//work.setRest1(imgState);
						work.setRestImg(imgState);
						work.getTOsWorktask().setWorktaskSender(
								recvName.toString());
						result.set(i, work);
					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	/**
	 * 获取附件信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsTaskAttach> getAttachListByTaskId(String taskId)
			throws ServiceException, SystemException {
		return this.taskAttachDao.findByProperty("attachTaskId", taskId);
	}

	/**
	 * 获取工作的附件
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsTaskAttach> getTaskSummaryAttachListByTaskId(String taskId)
			throws ServiceException, SystemException {
		return this.taskAttachDao
				.find(
						"from TOsTaskAttach a where a.attachTaskId=?",
						new Object[] { taskId });
	}

	/**
	 * 根据附件id获取附件信息
	 * 
	 * @param attachId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TOsTaskAttach getAttachById(String attachId)
			throws ServiceException, SystemException {
		return this.taskAttachDao.get(attachId);
	}

	/**
	 * 获取附件
	 * @param attachId 附件ID
	 * @return ToaAttachment 附件对象
	 */
	@Transactional(readOnly = true)
	public ToaAttachment getToaAttachmentById(String attachId,String rest2) {
		ToaAttachment toaAttachment = attachDao.get(rest2);
		if(toaAttachment.getAttachCon() != null) {//兼容早期存数据库的数据
			return toaAttachment ;
		}
		ToaAttachment entity = null;
		try {
			entity = toaAttachment.clone();
		} catch (CloneNotSupportedException e1) {
			entity = toaAttachment;
			logger.error("克隆附件对象失败。",e1);
		}
	    String rootPath = PathUtil.getRootPath();
	    File attchFile = null;
	    if(entity.getAttachType() != null && !"".equals(entity.getAttachType())) {
	    	attchFile = new File(rootPath + "uploadfile" + File.separator + attachId + "." + entity.getAttachType());
	    } else {
	    	attchFile = new File(rootPath + "uploadfile" + File.separator + attachId);
	    }
	    InputStream is = null;
	    ByteArrayOutputStream bos = null;
	    try {
			if(attchFile.exists()){
				is = new FileInputStream(attchFile);
				byte[] buf = null;
				byte[] b = new byte[8192];
				int read = 0;
				bos = new ByteArrayOutputStream();
				while((read=is.read(b))!=-1){
					bos.write(b, 0, read);
				}
				buf = bos.toByteArray();
				entity.setAttachCon(buf);
			}
		} catch (FileNotFoundException e) {
			logger.error("未找到文件" + entity.getAttachName(), e);
		} catch (IOException e) {
			logger.error("发生IO异常。",e);
		} finally {
			try {
				if(bos != null){
					bos.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				logger.error("关闭流发生异常。",e);
			}
		}
		return entity ;
	}

	
	public List<TOsManagementSummary> getSummaryList(String taskId)
			throws ServiceException, SystemException {
		StringBuffer hql = new StringBuffer(
				"select m from TOsManagementSummary m,TOsWorktask t where m.TOsWorktaskSend.TOsWorktask.worktaskId=t.worktaskId and t.worktaskId=?");
		List<TOsManagementSummary> summaryList = this.tOsWorktaskSendDao.find(
				hql.toString(), taskId);
		for (TOsManagementSummary summary : summaryList) {
			List<TOsTaskAttach> attachList = this.taskAttachDao.findByProperty(
					"attachSummaryId", summary.getSummaryId());
			summary.setAttachList(attachList);
		}
		return summaryList;
	}
	

	public List<TOsTaskAttach> getSummaryListBySummaryId(String summaryId)
			throws ServiceException, SystemException {
			List<TOsTaskAttach> attachList = this.taskAttachDao.findByProperty(
					"attachSummaryId", summaryId);
		return attachList;
	}
	
	/**
	 * 获取接收单位名称路径
	 * 
	 * @param orgId
	 * @return
	 */
	public String getRecvOrgNamePath(String orgId) {
		String result = "";
		TUumsBaseOrg org = this.userService.getOrgInfoByOrgId(orgId);
		if (org != null) {
			if (org.getOrgParentId() != null) {
				result += getRecvOrgNamePath(org.getOrgParentId())
						+ org.getOrgName() + "/";
			} else {
				result += org.getOrgName() + "/";
			}
		}
		return result;
	}
}
