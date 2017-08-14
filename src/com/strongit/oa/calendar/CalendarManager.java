/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理 日程 业务类
 */
package com.strongit.oa.calendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarActivity;
import com.strongit.oa.bo.ToaCalendarAttach;
import com.strongit.oa.bo.ToaCalendarRemind;
import com.strongit.oa.bo.ToaCalendarShare;
import com.strongit.oa.bo.ToaCalendarAssign;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.paramconfig.ConfigModule;
import com.strongit.oa.paramconfig.ParamConfigService;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 处理日程类
 * @Create Date: 2009-2-12
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class CalendarManager {
	private GenericDAOHibernate<ToaCalendar, java.lang.String> calendarDao;
	private GenericDAOHibernate<ToaCalendarAssign, java.lang.String> assignDao;
	private GenericDAOHibernate<ToaCalendarRemind, java.lang.String> calendarRemindDao;
	private CalendarRemindManager remindManager;
	
	private CalendarActivityManager activityManager;
	
	private CalendarAttachManager attachManager ;
	
	private CalendarShareManager shareManager ;
	
	private IAttachmentService attachmentService;
	
	private IUserService userService;

	private ParamConfigService pcService;
	
	@Autowired
	public void setPcService(ParamConfigService pcService) {
		this.pcService = pcService;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	/**
	 * author:luosy
	 * description: 日程 活动分类
	 * modifyer:
	 * description:
	 * @param activityManager
	 */
	@Autowired
	public void setActivityManager(CalendarActivityManager activityManager) {
		this.activityManager = activityManager;
	}
	/**
	 * author:luosy
	 * description: 日程 提醒
	 * modifyer:
	 * description:
	 * @param remindManager
	 */
	@Autowired
	public void setRemindManager(CalendarRemindManager remindManager) {
		this.remindManager = remindManager;
	}
	/**
	 * author:luosy
	 * description: 日程 附件
	 * modifyer:
	 * description:
	 * @param remindManager
	 */
	@Autowired
	public void setAttachManager(CalendarAttachManager attachManager) {
		this.attachManager = attachManager;
	}
	/**
	 * author:luosy
	 * description: 日程共享
	 * modifyer:
	 * description:
	 * @param iattachmentService
	 */
	@Autowired
	public void setShareManager(CalendarShareManager shareManager) {
		this.shareManager = shareManager;
	}
	/**
	 * author:luosy
	 * description: 公共附件接口
	 * modifyer:
	 * description:
	 * @param iattachmentService
	 */
	@Autowired
	public void setAttachmentService(IAttachmentService iattachmentService) {
		this.attachmentService = iattachmentService;
	}

	/**
	 * @roseuid 49586EDC01B5
	 */
	public CalendarManager() {

	}

	
	/**
	 * @param session
	 * @roseuid 49586BD00232
	 */
	@Autowired
	public void setSessionFactory(SessionFactory session) {
		calendarDao = new GenericDAOHibernate<ToaCalendar, java.lang.String>(
				session, ToaCalendar.class);
		assignDao=new GenericDAOHibernate<ToaCalendarAssign, java.lang.String>(
				session, ToaCalendarAssign.class);
		calendarRemindDao=new GenericDAOHibernate<ToaCalendarRemind, java.lang.String>(
				session, ToaCalendarRemind.class);
	}
	@Transactional
	public void saveRemind(ToaCalendarRemind remind) throws SystemException,ServiceException{
		try{
			if("".equals(remind.getRemindId())|null==remind.getRemindId()){
				calendarRemindDao.flush();
				calendarRemindDao.clear();
				calendarRemindDao.save(remind);
				//remindManager.addTimeTask(remind);
			}else{
				calendarRemindDao.flush();
				calendarRemindDao.clear();
				calendarRemindDao.save(remind);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"添加日程提醒"});
		}
		
	}
	/**
	 * author:luosy
	 * description:  保存日程事务+附件+提醒+共享
	 * modifyer:
	 * description:
	 * @param calendar 日程对象
	 * @param input  由"page"页面中操作
	 * @return
	 */
	@Transactional
	public void saveInpage(String activityId,ToaCalendar calendar,String shareToUserIds, String remindData,File[] file, String[] fileFileName) throws SystemException,ServiceException{
		try{
			//保存日程
			saveCalendar(activityId,calendar, "page");
			
			//保存附件
			if(file!=null){
				Calendar cals = Calendar.getInstance();
				for(int i=0;i<file.length;i++){
					FileInputStream fils = null;
					try{
						fils = new FileInputStream(file[i]);
						byte[] buf = new byte[(int)file[i].length()];
						fils.read(buf);	
						String ext = fileFileName[i].substring(fileFileName[i].lastIndexOf(".") + 1,
								fileFileName[i].length());
						//添加公共附件表数据
						String attachId = attachmentService.saveAttachment(fileFileName[i], buf, cals.getTime(), ext, "1", "注:日程附件", "userId");
						//添加日程附件表数据
						attachManager.saveAttach(attachId, calendar);
					}catch (Exception e) {
						throw new ServiceException(MessagesConst.save_error,               
								new Object[] {"上传失败"});
					}finally{
						try {
							fils.close();
						} catch (IOException e) {
							throw new ServiceException(MessagesConst.save_error,               
									new Object[] {"附件上传失败"});
						}
					}
				}
			}
			
			//保存 日程提醒
			if(remindData!=null&&!"".equals(remindData)){
				if("noRemind".equals(remindData)){
					ToaCalendar cal = getCalById(calendar.getCalendarId());
					this.delRemindsByCal(cal);
					calendar.setCalHasRemind(ToaCalendar.HAS_NO_REMIND);
				}else{
					remindManager.setRemindData(calendar,remindData);
					calendar.setCalHasRemind(ToaCalendar.HAS_REMIND);
				}
			}else{
				calendar.setCalHasRemind(ToaCalendar.HAS_NO_REMIND);
			}
			
			//保存共享对象
			shareManager.saveShare(shareToUserIds,calendar);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"日程保存"});
		}
		
	}
	
	/**
	 * author:luosy
	 * description:保存基本日程事务
	 * modifyer:
	 * description:
	 * @param calendar
	 * @param input  由"page"页面(单独的编辑页面)或"view"视图中操作
	 * @return
	 */
	@Transactional
	public String saveCalendar(String activityId, ToaCalendar calendar, String input)  throws SystemException,ServiceException{
		try{
			String userId = userService.getCurrentUser().getUserId();
			String userName = userService.getCurrentUser().getUserName();
			
			calendar.setCalCon(HtmlUtils.htmlEscape(calendar.getCalCon()));
			
			if(calendar.getCalendarId()==null|"".equals(calendar.getCalendarId())){
				calendar.setCalendarId(null);
				
				if(calendar.getUserId()==null){
					calendar.setUserId(userId);
				}
				if(calendar.getCalUserName()==null){
					calendar.setCalUserName(userName);
				}
				if(!ToaCalendar.IS_LEADER.equals(calendar.getIsLeader())){
					calendar.setIsLeader(ToaCalendar.IS_NOT_LEADER);
				}
				//活动分类
				if("".equals(activityId)|null==activityId){
					calendar.setToaCalendarActivity(activityManager.getActivity(ToaCalendarActivity.DEFINE_ACTIVITY));
				}else{
					ToaCalendarActivity act = activityManager.getActivityById(activityId);
					if(null!=act){
						calendar.setToaCalendarActivity(act);
					}else{
						calendar.setToaCalendarActivity(activityManager.getActivity(ToaCalendarActivity.DEFINE_ACTIVITY));
					}
				}
				
				if(calendar.getCalTitle()==null|"".equals(calendar.getCalTitle())){
					calendar.setCalTitle("无主题");
				}
				
				if("view".equals(input)){
					ToaCalendar obj = new ToaCalendar();
					if(calendar.getCalCon()!=null|!"".equals(calendar.getCalCon())){
						obj.setCalCon(calendar.getCalCon());
					}
					obj.setCalStartTime(calendar.getCalStartTime());
					obj.setCalEndTime(calendar.getCalEndTime());
					obj.setRepeatType(calendar.getRepeatType());
					obj.setCalRepeatStime(calendar.getCalRepeatStime());
					obj.setCalRepeatEtime(calendar.getCalRepeatEtime());
					obj.setCalTitle(calendar.getCalTitle());
					obj.setToaCalendarActivity(calendar.getToaCalendarActivity());
					obj.setUserId(userId);
					obj.setCalUserName(userName);
					obj.setRepeatType(ToaCalendar.REPEAT_NONE);
					obj.setCalHasRemind(ToaCalendar.HAS_NO_REMIND);
					obj.setIsLeader(calendar.getIsLeader());
					obj.setAssignUserId(calendar.getAssignUserId());
					calendarDao.save(obj);
					return obj.getCalendarId();
				}else{
					calendarDao.save(calendar);
					return calendar.getCalendarId();
				}
			}else{
				if("view".equals(input)){
					ToaCalendar obj = getCalById(calendar.getCalendarId());
					obj.setCalStartTime(calendar.getCalStartTime());
					obj.setCalEndTime(calendar.getCalEndTime());
					if(obj.getUserId()==null){
						obj.setUserId(userId);
					}
					if(obj.getCalUserName()==null){
						obj.setCalUserName(userName);
					}
					calendarDao.save(obj);
					return obj.getCalendarId();
				}else{
					if(calendar.getUserId()==null){
						calendar.setUserId(userId);
					}
					if(calendar.getCalUserName()==null){
						calendar.setCalUserName(userName);
					}
					
					String calActId = calendar.getToaCalendarActivity().getActivityId();
					if("".equals(calActId)|null==calActId){
						calendar.setToaCalendarActivity(activityManager.getActivity(ToaCalendarActivity.DEFINE_ACTIVITY));
					}
					calendarDao.flush();
					calendarDao.clear();
					calendarDao.update(calendar);
					return calendar.getCalendarId();
				}
			}
			

		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"日程保存"});
		}
	}

	/**
	 * author:hecj
	 * description:删除指定的授权用户
	 */
	public void deleteUser(String ids,String treeUserId)throws SystemException,ServiceException{
		try{
			Object[] ob=ids.split(",");
			for(int i=0;i<ob.length;i++){
				String hql="from ToaCalendarAssign t where t.assignUserId='" + treeUserId +"' and t.assignToUserId=?";
				List tmplist= assignDao.find(hql,ob[i]);
				ToaCalendarAssign po=(ToaCalendarAssign)tmplist.get(0);
				assignDao.delete(po);
			}
		}catch(ServiceException e)
		{
			throw new ServiceException(MessagesConst.find_error,new Object[] {"删除授权用户"});
		}
	}
	
	/**
	 * author:hecj
	 * description:根据传递过来的用户id，获取被授权的用户名
	 */
	public Page<ToaCalendarAssign> searchAssignUser(Page<ToaCalendarAssign> page, String assignUserId) throws SystemException,ServiceException
	{
		try{
			StringBuilder hql=new StringBuilder();
			if(assignUserId==null){
				assignUserId=userService.getCurrentUser().getUserId();
			}
			
			hql.append("select o.userName,t.assignToUserId from ToaCalendarAssign as t,TUumsBaseUser as o where t.assignUserId=?");
			hql.append(" and t.assignToUserId=o.userId");
			if (assignUserId==null){
				hql.append(" and 1=1");
			}
			return  assignDao.find(page, hql.toString(), assignUserId);
		}catch(ServiceException e)
		{
			throw new ServiceException(MessagesConst.find_error,new Object[] {"搜索授权用户列表"});
		}
	}
	
	/**
	 * author:hecj
	 * description:查询当前用户被授权的用户列表
	 */
	public List searchAssignByUserId(String UserId) throws SystemException,ServiceException
	{
		try{
			StringBuilder hql=new StringBuilder();
			if(UserId==null){
				UserId=userService.getCurrentUser().getUserId();
			}
			hql.append("select o.userName,t.assignToUserId from ToaCalendarAssign as t,TUumsBaseUser as o where t.assignUserId=?");
			hql.append(" and t.assignToUserId=o.userId");
			return  assignDao.find(hql.toString(), UserId);
		}catch(ServiceException e)
		{
			throw new ServiceException(MessagesConst.find_error,new Object[] {"搜索授权用户列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:删除日程事务
	 * modifyer:
	 * description:
	 * @param ids
	 */
	@Transactional
	public void deleteCalendar(String ids)  throws SystemException,ServiceException,AjaxException{
		try{
			if ("".equals(ids) | null == ids) {
			} else {
				String[] id = ids.split(",");
				for (int i = 0; i < id.length; i++) {
					//删除公共附件表相关记录
					Set att = getCalById(id[i]).getToaCalendarAttaches();
					if(att!=null){
						Iterator it=att.iterator();
						while (it.hasNext()) {
							ToaCalendarAttach objs = (ToaCalendarAttach) it.next();
							attachmentService.deleteAttachment(objs.getAttachId());
						}
					}
					//删除记录
					calendarDao.delete(id[i]);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除日程"});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除日程"});
		}
	}

	/**
	 * author:luosy
	 * description:获取公共附件表的附件
	 * modifyer:
	 * description:
	 * @param attachId 公共附件id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAttachment getToaAttachmentById(String attachId) throws SystemException,ServiceException{
		try{
			return attachmentService.getAttachmentById(attachId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取附件"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 查找所有领导公布的领导日程
	 * modifyer:
	 * description:
	 * @return
	 */
	@Transactional(readOnly=true)
	public List getLeaderCal() throws SystemException,ServiceException{
		try{
			List leaderCalList = calendarDao.find("select distinct t.userId,t.calUserName from ToaCalendar as t where t.isLeader = '"+ToaCalendar.IS_LEADER+"'");
			return leaderCalList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找所有领导公布的日程"});
		}
	}

	/**
	 * author:luosy
	 * description:获取日程事务对象
	 * modifyer:
	 * description:
	 * @param calendarId 日程事务id
	 * @return
	 */
	public ToaCalendar getCalById(String calendarId) throws SystemException,ServiceException{
		try{
			return calendarDao.findById(calendarId, false);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取日程对象"});
		}
	}
	
	/**
	 * author:hecj
	 * description:保存指定授权人员
	 */
	public void saveAssignPerson(String treeUserId,String assigntouserid)throws SystemException,ServiceException
	{
		try{
			ToaCalendarAssign bo;
			String hql="from ToaCalendarAssign where assignUserId='" + treeUserId + "'";
			assignDao.delete(assignDao.find(hql));
			String id[]=assigntouserid.split(",");
			for(int i=0;i<id.length;i++)
			{
				bo =new ToaCalendarAssign();
				bo.setAssignUserId(treeUserId);
				bo.setAssignToUserId(id[i]);
				assignDao.save(bo);
			}
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存指定授权人员"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 按条件搜索的日程活动数据记录
	 * modifyer:
	 * description:
	 * @param inputType  查看类型(查看共享日程列表/个人列表)
	 * @param cal 日程对象
	 * @return page 列表分页对象
	 */
	@Transactional(readOnly=true)
	public Page<ToaCalendar> searchByCal(Page<ToaCalendar> page, ToaCalendar cal,String inputType) throws SystemException,ServiceException{
		try{
			String CurrentUserId = userService.getCurrentUser().getUserId();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" select t.calendarId,t.calTitle,t.calCon,t.calStartTime,t.calEndTime,t.calUserName,t.toaCalendarActivity.activityName, t.calPlace, t.calHasRemind,t.isLeader from ToaCalendar as t ");
			
			if("share".equals(inputType)){
				hql.append(" ,ToaCalendarShare as ts ");
			}
			
			hql.append(" where 1=1 ");
//		创建者姓名
			String userName = cal.getCalUserName();
			if (!"".equals(userName) && userName != null) {
				hql.append(" and t.calUserName like '%" + FiltrateContent.getNewContent(userName.trim()) + "%' ");
			}
			
//		标题
			String title = cal.getCalTitle();
			if (!"".equals(title) && title != null) {
				hql.append(" and t.calTitle like '%"+FiltrateContent.getNewContent(title.trim())+"%' ");
			}
			
//		内容
			String calCon = cal.getCalCon();
			if (!"".equals(calCon) && calCon != null) {
				hql.append(" and t.calCon like '%"+FiltrateContent.getNewContent(calCon.trim()) +"%' ");
			}
			
//			活动地点
			String calplace = cal.getCalPlace();
			if (!"".equals(calplace) && calplace != null) {
				hql.append(" and t.calPlace like '%"+FiltrateContent.getNewContent(calplace.trim()) +"%' ");
			}
//			有无提醒
			String hasRemind = cal.getCalHasRemind();
			if(!"".equals(hasRemind) && hasRemind != null) {
				hql.append(" and t.calHasRemind like '%"+FiltrateContent.getNewContent(hasRemind.trim()) +"%' ");
			}
			
			
//			活动分类
			ToaCalendarActivity act = cal.getToaCalendarActivity();
			if(act!=null){
				String activityName = act.getActivityName();
				if(!"".equals(activityName)&&activityName!=null) {
					hql.append(" and t.toaCalendarActivity.activityName like '%"+FiltrateContent.getNewContent(activityName.trim()) +"%' ");
				}
			}
			
			
//		是否为"查看领导日程"
			if("leader".equals(inputType)){
				hql.append(" and t.isLeader like '%"+ToaCalendar.IS_LEADER +"%' ");
			}else if("share".equals(inputType)){//共享日程
				hql.append(" and t.calendarId=ts.toaCalendar.calendarId and ts.userId = '" + CurrentUserId +"' ");
			}else{//个人日程
				hql.append(" and t.userId= '" + CurrentUserId +"' ");
			}
			
			String isLeader = cal.getIsLeader();
			if (!"".equals(isLeader) && calCon != null) {
				if(ToaCalendar.IS_NOT_LEADER.equals(isLeader)){
					hql.append(" and t.isLeader like '%"+ToaCalendar.IS_NOT_LEADER+"%' ");
				}
			}
			
//		在某个时间段内出现的日程
			if(cal.getCalStartTime()!=null&&cal.getCalEndTime()!=null){
				Calendar ts = Calendar.getInstance();
				ts.setTime(cal.getCalStartTime());
				ts.set(Calendar.HOUR_OF_DAY, 0);
				ts.set(Calendar.MINUTE, 0);
				Calendar te = Calendar.getInstance();
				te.setTime(cal.getCalEndTime());
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				
				Object[] obj = new Object[6];
				obj[0] = ts.getTime();
				obj[1] = te.getTime();
				obj[2] = ts.getTime();
				obj[3] = ts.getTime();
				obj[4] = te.getTime();
				obj[5] = te.getTime();
				hql.append("and (( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>?) " +
				" or ( t.calStartTime<? and t.calEndTime>?) ) ");
				return calendarDao.find(page,hql.toString()+" order by t.calEndTime asc ",obj);
			}else if(cal.getCalStartTime()!=null&&cal.getCalEndTime()==null){
				Calendar ts = Calendar.getInstance();
				ts.setTime(cal.getCalStartTime());
				ts.set(Calendar.HOUR_OF_DAY, 0);
				ts.set(Calendar.MINUTE, 0);
				
				hql.append("and  t.calEndTime>? ");
				return calendarDao.find(page,hql.toString()+" order by t.calStartTime desc ",ts.getTime());
			}else if(cal.getCalStartTime()==null&&cal.getCalEndTime()!=null){
				Calendar te = Calendar.getInstance();
				te.setTime(cal.getCalEndTime());
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				
				hql.append("and  t.calStartTime<?  ");
				return calendarDao.find(page,hql.toString()+" order by t.calEndTime desc ",te.getTime());
			}else{
				
				Calendar s = Calendar.getInstance();
				s.set(Calendar.HOUR_OF_DAY, 00);
				s.set(Calendar.MINUTE, 00);
				Calendar e = Calendar.getInstance();
				e.set(Calendar.HOUR_OF_DAY, 23);
				e.set(Calendar.MINUTE, 59);
				Object[] obj = new Object[6];
				obj[0] = s.getTime();
				obj[1] = e.getTime();
				obj[2] = s.getTime();
				obj[3] = s.getTime();
				obj[4] = e.getTime();
				obj[5] = e.getTime();
				
				hql.append(" order by case when ")
					.append("(( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>? )  or ( t.calStartTime<? and t.calEndTime>?) ) ")
					.append(" then t.calRepeatStime  else t.calStartTime end desc  ");
				page.setAutoCount(false);
				page.setTotalCount(calendarDao.find(hql.toString(), obj).size());
				return calendarDao.find(page,hql.toString(),obj);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"搜索的日程列表"});
		}
	}
	
	/**
	 * author:hecj
	 * description:根据授权人assign_user_id来获得日程记录数
	 */
	public Page<ToaCalendar> searchByOtherPage(Page<ToaCalendar> page, ToaCalendar cal,String inputType) throws SystemException,ServiceException{
		try{
			String CurrentUserId = userService.getCurrentUser().getUserId();
			StringBuilder hql = new StringBuilder();
			hql.append(" select t.calendarId,t.calTitle,t.calCon,t.calStartTime,t.calEndTime,t.calUserName,t.toaCalendarActivity.activityName, t.calPlace, t.calHasRemind,t.assignUserId from ToaCalendar as t ");
			hql.append(" where 1=1 ");
			
//		创建者姓名
			String userName = cal.getCalUserName();
			if (!"".equals(userName) && userName != null) {
				hql.append(" and t.calUserName like '%" + FiltrateContent.getNewContent(userName.trim()) + "%' ");
			}
			
//		标题
			String title = cal.getCalTitle();
			if (!"".equals(title) && title != null) {
				hql.append(" and t.calTitle like '%"+FiltrateContent.getNewContent(title.trim())+"%' ");
			}
			
//		内容
			String calCon = cal.getCalCon();
			if (!"".equals(calCon) && calCon != null) {
				hql.append(" and t.calCon like '%"+FiltrateContent.getNewContent(calCon.trim()) +"%' ");
			}
			
//			活动地点
			String calplace = cal.getCalPlace();
			if (!"".equals(calplace) && calplace != null) {
				hql.append(" and t.calPlace like '%"+FiltrateContent.getNewContent(calplace.trim()) +"%' ");
			}
//			有无提醒
			String hasRemind = cal.getCalHasRemind();
			if(!"".equals(hasRemind) && hasRemind != null) {
				hql.append(" and t.calHasRemind like '%"+FiltrateContent.getNewContent(hasRemind.trim()) +"%' ");
			}
			
			
//			活动分类
			ToaCalendarActivity act = cal.getToaCalendarActivity();
			if(act!=null){
				String activityName = act.getActivityName();
				if(!"".equals(activityName)&&activityName!=null) {
					hql.append(" and t.toaCalendarActivity.activityName like '%"+FiltrateContent.getNewContent(activityName.trim()) +"%' ");
				}
			}
			
			
//		是否为"查看领导日程"
			if("leader".equals(inputType)){
				hql.append(" and t.isLeader like '%"+ToaCalendar.IS_LEADER +"%' ");
			}else if("share".equals(inputType)){//共享日程
				hql.append(" and t.calendarId=ts.toaCalendar.calendarId and ts.userId = '" + CurrentUserId +"' ");
			}else if("assign".equals(inputType)){
				hql.append(" and t.assignUserId= '"+CurrentUserId+"' ");
			}
			else{//个人日程
				//hql.append(" and t.userId= '" + CurrentUserId +"' ");
			}
			
			String isLeader = cal.getIsLeader();
			if (!"".equals(isLeader) && calCon != null) {
				if(ToaCalendar.IS_NOT_LEADER.equals(isLeader)){
					hql.append(" and t.isLeader like '%"+ToaCalendar.IS_NOT_LEADER+"%' ");
				}
			}
			
//		在某个时间段内出现的日程
			if(cal.getCalStartTime()!=null&&cal.getCalEndTime()!=null){
				Calendar ts = Calendar.getInstance();
				ts.setTime(cal.getCalStartTime());
				ts.set(Calendar.HOUR_OF_DAY, 0);
				ts.set(Calendar.MINUTE, 0);
				Calendar te = Calendar.getInstance();
				te.setTime(cal.getCalEndTime());
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				
				Object[] obj = new Object[6];
				obj[0] = ts.getTime();
				obj[1] = te.getTime();
				obj[2] = ts.getTime();
				obj[3] = ts.getTime();
				obj[4] = te.getTime();
				obj[5] = te.getTime();
				hql.append("and (( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>?) " +
				" or ( t.calStartTime<? and t.calEndTime>?) ) ");
				return calendarDao.find(page,hql.toString()+" order by t.calEndTime asc ",obj);
			}else if(cal.getCalStartTime()!=null&&cal.getCalEndTime()==null){
				Calendar ts = Calendar.getInstance();
				ts.setTime(cal.getCalStartTime());
				ts.set(Calendar.HOUR_OF_DAY, 0);
				ts.set(Calendar.MINUTE, 0);
				
				hql.append("and  t.calEndTime>? ");
				return calendarDao.find(page,hql.toString()+" order by t.calStartTime desc ",ts.getTime());
			}else if(cal.getCalStartTime()==null&&cal.getCalEndTime()!=null){
				Calendar te = Calendar.getInstance();
				te.setTime(cal.getCalEndTime());
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				
				hql.append("and  t.calStartTime<?  ");
				return calendarDao.find(page,hql.toString()+" order by t.calEndTime desc ",te.getTime());
			}else{
				
				Calendar s = Calendar.getInstance();
				s.set(Calendar.HOUR_OF_DAY, 00);
				s.set(Calendar.MINUTE, 00);
				Calendar e = Calendar.getInstance();
				e.set(Calendar.HOUR_OF_DAY, 23);
				e.set(Calendar.MINUTE, 59);
				Object[] obj = new Object[6];
				obj[0] = s.getTime();
				obj[1] = e.getTime();
				obj[2] = s.getTime();
				obj[3] = s.getTime();
				obj[4] = e.getTime();
				obj[5] = e.getTime();
				
				hql.append(" order by case when ")
					.append("(( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>? )  or ( t.calStartTime<? and t.calEndTime>?) ) ")
					.append(" then t.calRepeatStime  else t.calStartTime end desc  ");
				page.setAutoCount(false);
				page.setTotalCount(calendarDao.find(hql.toString(), obj).size());
				return calendarDao.find(page,hql.toString(),obj);
				}
			}
			catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"他人的日程列表"});
			}
	}
	/**
	 * author:luosy
	 * description:删除所有相关附件
	 * modifyer:
	 * description:
	 * @param attachId 公共附件表主键
	 */
	public void delAttach(String attachId)throws SystemException,ServiceException{
		try{
			//删除公共附件表
			attachmentService.deleteAttachment(attachId);
			//删除公告附件表
			attachManager.delAttach(attachId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除日程附件"});
		}
	}

	/**
	 * author:luosy
	 * description:删除某个提醒
	 * modifyer:
	 * description:
	 * @param remindId 提醒对象ID
	 */
	public void delRemind(String remindId) throws SystemException,ServiceException{
		try{
			remindManager.delRemind(remindId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除日程提醒"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 删除某个日程事务的所有提醒
	 * modifyer:
	 * description:
	 * @param cal
	 */
	public void delRemindsByCal(ToaCalendar cal)throws SystemException,ServiceException{
		try{
			remindManager.delRemindsByCal(cal);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除日程提醒"});
		}
	}
	
	/**
	 * author:luosy
	 * description : 按条件 获取所有日程 事务活动信息
	 * modifyer:
	 * description:
	 * @param cal
	 * @param activityId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List getMyCalList(ToaCalendar cal,String activityId) throws SystemException,ServiceException{
		try{
			StringBuffer hql = new StringBuffer();
			hql.append("select t.calTitle,t.calendarId,t.calStartTime,t.calEndTime,t.isLeader,t.repeatType,t.calRepeatStime,t.calRepeatEtime from ToaCalendar as t where 1=1");
			
//		用户()
			String userId = userService.getCurrentUser().getUserId();
			if (!"".equals(userId) && userId != null) {
				hql.append(" and t.userId like '%" + userId + "%' ");
			}
//		活动分类
			if (!"".equals(activityId) && activityId != null) {
				hql.append(" and t.toaCalendarActivity.activityId like '%" + FiltrateContent.getNewContent(activityId) + "%' ");
			}
			
//		标题
			String title = cal.getCalTitle();
			if (!"".equals(title) && title != null) {
				hql.append(" and t.calTitle like '%"+FiltrateContent.getNewContent(title)+"%' ");
			}
			
//		内容
			String calCon = cal.getCalCon();
			if (!"".equals(calCon) && calCon != null) {
				hql.append(" and t.calCon like '%"+FiltrateContent.getNewContent(calCon) +"%' ");
			}
			
//		地点
			String place = cal.getCalPlace();
			if (!"".equals(place) && place != null) {
				hql.append(" and t.calPlace like '%" + FiltrateContent.getNewContent(place) + "%' ");
			}
			List l = null;
			String str = hql.toString()+" order by t.calStartTime desc ";
			try {
				l = calendarDao.find(str);
				
			} catch (Exception e) {
				throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"获取的日程信息列表"});
			}
			return l;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取的日程信息"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取领导发布的日程列表
	 * modifyer:
	 * description:
	 * @param userId
	 * @param inputTpye
	 * @return
	 */
	@Transactional(readOnly=true)
	public List getleaderCalList(Calendar startCal, Calendar endCal,String userId,String inputType)throws SystemException,ServiceException{
		try{
			StringBuffer addhql = new StringBuffer();
			addhql.append("1=1 ");
			if("leader".equals(inputType)){
				addhql.append(" and t.isLeader = '"+ToaCalendar.IS_LEADER+"' ");
			}
			
			if (!"".equals(userId) && userId != null) {
				addhql.append(" and t.userId = '" + userId + "' ");
			}
			
			Object[] obj = new Object[6];
			obj[0] = startCal.getTime();
			obj[1] = endCal.getTime();
			obj[2] = startCal.getTime();
			obj[3] = startCal.getTime();
			obj[4] = endCal.getTime();
			obj[5] = endCal.getTime();
			
			return calendarDao.find("select t.calTitle,t.calendarId,t.calStartTime,t.calEndTime,t.isLeader,t.repeatType,t.calRepeatStime,t.calRepeatEtime, t.calUserName " +
					"from ToaCalendar as t where " + addhql.toString()+
					" and ( ( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>?) " +
					" or ( t.calStartTime<? and t.calEndTime>?) ) " +
					"order by t.calEndTime asc ",obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取领导发布的日程列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取用户共享的日程列表
	 * modifyer:
	 * description:
	 * @param userId
	 * @param inpytTpye
	 * @return
	 */
	@Transactional(readOnly=true)
	public List getshareCalList(Calendar startCal, Calendar endCal,String userId,String inputType) throws SystemException,ServiceException{
		try{
			StringBuffer hql = new StringBuffer();
			String currentUserId = userService.getCurrentUser().getUserId();
			
			Object[] obj = new Object[6];
			obj[0] = startCal.getTime();
			obj[1] = endCal.getTime();
			obj[2] = startCal.getTime();
			obj[3] = startCal.getTime();
			obj[4] = endCal.getTime();
			obj[5] = endCal.getTime();
			
			hql.append("select t.toaCalendar.calTitle,t.toaCalendar.calendarId,t.toaCalendar.calStartTime,t.toaCalendar.calEndTime,t.toaCalendar.isLeader,t.toaCalendar.repeatType,t.toaCalendar.calRepeatStime,t.toaCalendar.calRepeatEtime, t.toaCalendar.calUserName " +
					"from ToaCalendarShare as t where t.userId like '%" + currentUserId + "%' ");
			if (!"".equals(userId) && userId != null) {
				hql.append(" and t.toaCalendar.userId = '" + userId + "' ");
			}
			hql.append(" and ( ( t.toaCalendar.calStartTime>? and t.toaCalendar.calEndTime<? ) or ( t.toaCalendar.calStartTime<? and t.toaCalendar.calEndTime>?) " +
					" or ( t.toaCalendar.calStartTime<? and t.toaCalendar.calEndTime>?) ) ");
			return calendarDao.find(hql.toString(),obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取用户共享的日程列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 查看某个日程活动是否是否共享
	 * modifyer:
	 * description:
	 * @param CalId 日程ID
	 * @return 该日程有共享记录则返回 true 否则返回 false
	 */
	public boolean isShareCal(String CalId) throws SystemException,ServiceException{
		try{
			return shareManager.isShareCal(CalId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查看日程是否被共享"});
		}
	}
	
	/**
	 * author:luosy
	 * description:查找共享到当前用户的所有共享记录
	 * modifyer:
	 * description:
	 * @return List<ToaCalendarShare>
	 */
	@Transactional(readOnly=true)
	public List<Object[]> getAllShare() throws SystemException,ServiceException{
		try{
			String userId = this.userService.getCurrentUser().getUserId();
			return shareManager.getAllShare(userId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找共享记录"});
		}
	}
	
	
	public List<ToaCalendarAssign> getAssignList(String treeUserId) throws SystemException,ServiceException{
		try{
			List<ToaCalendarAssign> assignList=assignDao.find("from ToaCalendarAssign as t where t.assignUserId = '"+treeUserId+"'");
			return assignList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"分配日程权限"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description:获取当前用户的活动分类
	 * modifyer:
	 * description:
	 * @param userID
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ToaCalendarActivity> getAllActivityByUserID(String userId) throws SystemException,ServiceException{
		try{
			if(userId==null|"".equals(userId)){
				userId = userService.getCurrentUser().getUserId();
			}
			return activityManager.getAllActivity(userId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取用户活动分类"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取用户名
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 */
	public String getUserNameByUserId(String userId) throws SystemException,ServiceException{
		try{
			return userService.getUserNameByUserId(userId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取用户名"});
		}
	}

	/**
	 * author:luosy
	 * description:获取个人桌面列表  当天的日程列表
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<ToaCalendar> getListForTable(String userId,String ifLeader,Page<ToaCalendar> page)throws SystemException,ServiceException{
		try{
			Calendar s = Calendar.getInstance();
			s.set(Calendar.HOUR_OF_DAY, 00);
			s.set(Calendar.MINUTE, 00);
			Calendar e = Calendar.getInstance();
			e.set(Calendar.HOUR_OF_DAY, 23);
			e.set(Calendar.MINUTE, 59);

			Object[] obj = new Object[6];
			obj[0] = s.getTime();
			obj[1] = e.getTime();
			obj[2] = s.getTime();
			obj[3] = s.getTime();
			obj[4] = e.getTime();
			obj[5] = e.getTime();
			
			if(ToaCalendar.IS_LEADER.equals(ifLeader)){
				return calendarDao.find(page,"from ToaCalendar as t where t.isLeader='" +ToaCalendar.IS_LEADER+"' "+
						"and (( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>?) " +
						" or ( t.calStartTime<? and t.calEndTime>?) ) " +
						"order by t.calEndTime asc ",obj);
			}else{
				//今天的日程安排
				/*return calendarDao.find(page,"from ToaCalendar as t where t.userId like '%" + userId + "%' " + "and (t.repeatType= null or t.repeatType=0 ) "+
						" and ( ( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>?) " +
						" or ( t.calStartTime<? and t.calEndTime>?) ) " +
						"order by t.calEndTime asc ",obj);*/
				return calendarDao.find(page,"from ToaCalendar as t where t.userId like '%" + userId + "%' " + "and (t.repeatType= null or t.repeatType=0 ) "+
						" and (t.calEndTime>?) "+
						"order by t.calEndTime asc ",s.getTime());
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取桌面日程列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:查看共享日程桌面模块
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List<ToaCalendarShare> getShareCalForTable()throws SystemException,ServiceException{
			try{
				String userId = userService.getCurrentUser().getUserId();
				
				Calendar s = Calendar.getInstance();
				s.set(Calendar.HOUR_OF_DAY, 00);
				s.set(Calendar.MINUTE, 00);
				Calendar e = Calendar.getInstance();
				e.set(Calendar.HOUR_OF_DAY, 23);
				e.set(Calendar.MINUTE, 59);

				Object[] obj = new Object[6];
				obj[0] = s.getTime();
				obj[1] = e.getTime();
				obj[2] = s.getTime();
				obj[3] = s.getTime();
				obj[4] = e.getTime();
				obj[5] = e.getTime();
			List list = calendarDao.find("from ToaCalendarShare as t where t.userId like '%" + userId + "%' "+
					"and (t.toaCalendar.calEndTime>?) "+
					"order by t.toaCalendar.calEndTime asc ",s.getTime());
			/*List list = calendarDao.find("from ToaCalendarShare as t where t.userId like '%" + userId + "%' "+
					" and ( ( t.toaCalendar.calStartTime>? and t.toaCalendar.calEndTime<? ) or ( t.toaCalendar.calStartTime<? and t.toaCalendar.calEndTime>?) " +
					" or ( t.toaCalendar.calStartTime<? and t.toaCalendar.calEndTime>?) ) " +
					"order by t.toaCalendar.calEndTime asc ",obj);*/
				return list;
			}catch(ServiceException e){
				throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"获取桌面日程列表"});
			}
	}
	/**
	 * author:luosy
	 * description:获取个人桌面列表  某天的日程列表(桌面万年历 悬停显示详情)
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ToaCalendar> getListForTable(Calendar s,String ifLeader)throws SystemException,ServiceException{
		try{
			String userId = userService.getCurrentUser().getUserId();

			Calendar e = Calendar.getInstance();
			e.setTime(s.getTime());
			e.set(Calendar.HOUR_OF_DAY, 23);
			e.set(Calendar.MINUTE, 59);
			
			Object[] obj = new Object[6];
			obj[0] = s.getTime();
			obj[1] = e.getTime();
			obj[2] = s.getTime();
			obj[3] = s.getTime();
			obj[4] = e.getTime();
			obj[5] = e.getTime();

//			今天的日程安排
			return calendarDao.find("from ToaCalendar as t where t.userId like '%" + userId + "%' " + " "+
					" and ( ( t.calStartTime>=? and t.calEndTime<=? ) or ( t.calStartTime<=? and t.calEndTime>=?) " +
					" or ( t.calStartTime<=? and t.calEndTime>=?) ) " +
					"order by t.calEndTime asc ",obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取桌面日程列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 取出与指定时间段相关的日程
	 * modifyer:
	 * description:
	 * @param startCal 开始时间
	 * @param endCal   结束时间
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List getListByTime(Calendar startCal, Calendar endCal, String activityId)throws SystemException,ServiceException{
		try{
			String userId = userService.getCurrentUser().getUserId();
			
			StringBuffer addhql = new StringBuffer();
			addhql.append(" ");
			if(!"".equals(activityId)&&null!=activityId){
				addhql.append(" and t.toaCalendarActivity.activityId like '%" + activityId + "%' ");
			}
			
			Object[] obj = new Object[6];
			obj[0] = startCal.getTime();
			obj[1] = endCal.getTime();
			obj[2] = startCal.getTime();
			obj[3] = startCal.getTime();
			obj[4] = endCal.getTime();
			obj[5] = endCal.getTime();
			
//			今天的日程安排
			return calendarDao.find("select t.calTitle,t.calendarId,t.calStartTime,t.calEndTime,t.isLeader,t.repeatType,t.calRepeatStime,t.calRepeatEtime,t.calUserName " +
					"from ToaCalendar as t where t.userId like '%" + userId + "%' " + addhql.toString()+
					" and ( ( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>?) " +
					" or ( t.calStartTime<? and t.calEndTime>?) ) " +
					"order by t.calEndTime asc ",obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取桌面日程列表"});
		}
	}
	
	/**
	 * author:hecj
	 * description: 取出与指定时间段相关的日程
	 */
	@Transactional(readOnly=true)
	public List getAssignListByTime(Calendar startCal, Calendar endCal, String activityId)throws SystemException,ServiceException{
		try{
			String userId = userService.getCurrentUser().getUserId();
			
			StringBuffer addhql = new StringBuffer();
			addhql.append(" ");
			if(!"".equals(activityId)&&null!=activityId){
				addhql.append(" and t.toaCalendarActivity.activityId like '%" + activityId + "%' ");
			}
			
			Object[] obj = new Object[6];
			obj[0] = startCal.getTime();
			obj[1] = endCal.getTime();
			obj[2] = startCal.getTime();
			obj[3] = startCal.getTime();
			obj[4] = endCal.getTime();
			obj[5] = endCal.getTime();
			
//			今天的日程安排
			return calendarDao.find("select t.calTitle,t.calendarId,t.calStartTime,t.calEndTime,t.isLeader,t.repeatType,t.calRepeatStime,t.calRepeatEtime,t.calUserName " +
					"from ToaCalendar as t where t.assignUserId like '%" + userId + "%' " + addhql.toString()+
					" and ( ( t.calStartTime>? and t.calEndTime<? ) or ( t.calStartTime<? and t.calEndTime>?) " +
					" or ( t.calStartTime<? and t.calEndTime>?) ) " +
					"order by t.calEndTime asc ",obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取桌面日程列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:将日程事务列表转化为jsonArray
	 * modifyer:
	 * description:
	 * @param calList<Object[]> 日程列表 id,calStartTime,calEndTime,isLeader,repeatType,calRepeatStime,calRepeatEtime, calUserName
	 * @return 	日程组件的jsonArray
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public JSONArray makeCalListToJSONArray(List calList, boolean canEdit)throws SystemException,ServiceException{
		try{
			java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			JSONArray array = new JSONArray();
			if(null==calList){
				return array;
			}
			String title="" ;
			for (int i = 0; i < calList.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				JSONObject start = new JSONObject();
				JSONObject finish = new JSONObject();
				
				Object[] objs = (Object[])calList.get(i);

				jsonObj.put("edit",canEdit);
				
				if(!canEdit){
					title = objs[8]+"："+objs[0]+"("+format.format(objs[2])+")";
				}else{
					title = objs[0].toString();
				}
				
				String calId = "repeatBy"+objs[1].toString();
				
				boolean publicity = false;
				if("1".equals(objs[4])|this.isShareCal(objs[1].toString())){
					publicity = true;
				}
				
				String repType = ""; //循环类型
				if(objs[5]!=null){
					repType = objs[5].toString();
				}
				
				if(ToaCalendar.REPEAT_BY_DAY.equals(repType)){		//按日循环
					Calendar s = Calendar.getInstance(); //calStartTime
					s.setTime((Date)objs[2]);
					Calendar e = Calendar.getInstance(); //calEndTime
					e.setTime((Date)objs[3]);
					Calendar repS = Calendar.getInstance(); //calRepeatStime
					repS.setTime((Date)objs[6]);
					Calendar repE = Calendar.getInstance(); //calRepeatEtime
					repE.setTime((Date)objs[7]);
					
					while(s.getTime().getTime()<e.getTime().getTime()){
						//设置活动开始结束时间
						start.put("year", s.get(Calendar.YEAR));
						start.put("month", s.get(Calendar.MONTH));
						start.put("day", s.get(Calendar.DATE));
						start.put("hour", repS.get(Calendar.HOUR_OF_DAY));
						start.put("min", repS.get(Calendar.MINUTE));
						finish.put("year", s.get(Calendar.YEAR));
						finish.put("month", s.get(Calendar.MONTH));
						finish.put("day", s.get(Calendar.DATE));
						finish.put("hour", repE.get(Calendar.HOUR_OF_DAY));
						finish.put("min", repE.get(Calendar.MINUTE));
						
						jsonObj.put("id", calId);//calendarId
						jsonObj.put("description", title);//calTitle
						jsonObj.put("publicity", publicity);
						jsonObj.put("start", start);
						jsonObj.put("finish", finish);
						array.add(jsonObj);
						
						s.set(Calendar.DATE,s.get(Calendar.DATE)+1);
					}
				}else if(ToaCalendar.REPEAT_BY_WEEK.equals(repType)){	//按周循环
					Calendar s = Calendar.getInstance(); //calStartTime
					s.setTime((Date)objs[2]);
					Calendar e = Calendar.getInstance(); //calEndTime
					e.setTime((Date)objs[3]);
					Calendar repS = Calendar.getInstance(); //calRepeatStime
					repS.setTime((Date)objs[6]);
					Calendar repE = Calendar.getInstance(); //calRepeatEtime
					repE.setTime((Date)objs[7]);
					
					Calendar c = Calendar.getInstance();
					c.setTime(e.getTime());
					c.set(Calendar.DAY_OF_YEAR, (e.get(Calendar.DAY_OF_YEAR)+7));
					while(s.getTime().getTime()<c.getTime().getTime()){
						int dayS = s.get(Calendar.DATE) + repS.get(Calendar.DAY_OF_WEEK)-s.get(Calendar.DAY_OF_WEEK);
						int dayE = s.get(Calendar.DATE) + repE.get(Calendar.DAY_OF_WEEK)-s.get(Calendar.DAY_OF_WEEK);

						Calendar ds = Calendar.getInstance();
						ds.set(s.get(Calendar.YEAR), s.get(Calendar.MONTH), dayS,repS.get(Calendar.HOUR_OF_DAY), repS.get(Calendar.MINUTE));
						Calendar de = Calendar.getInstance();
						de.set(s.get(Calendar.YEAR), s.get(Calendar.MONTH), dayE,repE.get(Calendar.HOUR_OF_DAY), repE.get(Calendar.MINUTE));
						
						if(((Date)objs[2]).getTime()>de.getTime().getTime()||((Date)objs[3]).getTime()<ds.getTime().getTime()){
							//循环时段在活动时段之外
						}else if(((Date)objs[2]).getTime()>ds.getTime().getTime()&&((Date)objs[2]).getTime()<de.getTime().getTime()){
							// 活动开始时间在循环时段中
							ds.setTime((Date)objs[2]);
							
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", s.get(Calendar.MONTH));
							start.put("day", ds.get(Calendar.DATE));
							start.put("hour", ds.get(Calendar.HOUR_OF_DAY));
							start.put("min", ds.get(Calendar.MINUTE));
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", s.get(Calendar.MONTH));
							finish.put("day", de.get(Calendar.DATE));
							finish.put("hour", repE.get(Calendar.HOUR_OF_DAY));
							finish.put("min", repE.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
							
						}else if(((Date)objs[3]).getTime()>ds.getTime().getTime()&&((Date)objs[3]).getTime()<de.getTime().getTime()){
							// 活动结束时间在循环时段中
							de.setTime((Date)objs[3]);

							start.put("year", s.get(Calendar.YEAR));
							start.put("month", s.get(Calendar.MONTH));
							start.put("day", ds.get(Calendar.DATE));
							start.put("hour", repS.get(Calendar.HOUR_OF_DAY));
							start.put("min", repS.get(Calendar.MINUTE));
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", ds.get(Calendar.MONTH));
							finish.put("day", de.get(Calendar.DATE));
							finish.put("hour", de.get(Calendar.HOUR_OF_DAY));
							finish.put("min", de.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
						}else{
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", ds.get(Calendar.MONTH));
							start.put("day", ds.get(Calendar.DAY_OF_MONTH));
							start.put("hour", repS.get(Calendar.HOUR_OF_DAY));
							start.put("min", repS.get(Calendar.MINUTE));
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", de.get(Calendar.MONTH));
							finish.put("day", de.get(Calendar.DAY_OF_MONTH));
							finish.put("hour", repE.get(Calendar.HOUR_OF_DAY));
							finish.put("min", repE.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
						}
						
						s.set(Calendar.DATE, s.get(Calendar.DATE)+7);
					}
				}else if(ToaCalendar.REPEAT_BY_MONTH.equals(repType)){	//按月循环
					Calendar s = Calendar.getInstance(); //calStartTime
					s.setTime((Date)objs[2]);
					Calendar e = Calendar.getInstance(); //calEndTime
					e.setTime((Date)objs[3]);
					Calendar repS = Calendar.getInstance(); //calRepeatStime
					repS.setTime((Date)objs[6]);
					Calendar repE = Calendar.getInstance(); //calRepeatEtime
					repE.setTime((Date)objs[7]);
					
					while(s.getTime().getTime()<e.getTime().getTime()){
					
						//每次月循环的开始时间ds和结束时间de
						Calendar ds = Calendar.getInstance();
						ds.set(s.get(Calendar.YEAR), s.get(Calendar.MONTH), repS.get(Calendar.DATE),repS.get(Calendar.HOUR_OF_DAY), repS.get(Calendar.MINUTE));
						Calendar de = Calendar.getInstance();
						de.set(s.get(Calendar.YEAR), s.get(Calendar.MONTH), repE.get(Calendar.DATE),repE.get(Calendar.HOUR_OF_DAY), repE.get(Calendar.MINUTE));
						
						if(((Date)objs[2]).getTime()>de.getTime().getTime()|((Date)objs[3]).getTime()<ds.getTime().getTime()){
							//循环时段在活动时段之外
						}else if(((Date)objs[2]).getTime()>ds.getTime().getTime()&&((Date)objs[2]).getTime()<de.getTime().getTime()){
							//活动开始时间在循环时段中
							ds.setTime((Date)objs[2]);
							
							//设置活动开始结束时间
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", s.get(Calendar.MONTH));
							start.put("day", ds.get(Calendar.DATE));
							start.put("hour", ds.get(Calendar.HOUR_OF_DAY));
							start.put("min", ds.get(Calendar.MINUTE));
							
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", s.get(Calendar.MONTH));
							finish.put("day", repE.get(Calendar.DATE));
							finish.put("hour", repE.get(Calendar.HOUR_OF_DAY));
							finish.put("min", repE.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
							
						}else if(((Date)objs[3]).getTime()>ds.getTime().getTime()&&((Date)objs[3]).getTime()<de.getTime().getTime()){
							//活动结束时间在循环时段中
							de.setTime((Date)objs[3]);
							//设置活动开始结束时间
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", s.get(Calendar.MONTH));
							start.put("day", repS.get(Calendar.DATE));
							start.put("hour", repS.get(Calendar.HOUR_OF_DAY));
							start.put("min", repS.get(Calendar.MINUTE));
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", s.get(Calendar.MONTH));
							finish.put("day", de.get(Calendar.DATE));
							finish.put("hour", de.get(Calendar.HOUR_OF_DAY));
							finish.put("min", de.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
							
						}else{
							//设置活动开始结束时间
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", s.get(Calendar.MONTH));
							start.put("day", repS.get(Calendar.DATE));
							start.put("hour", repS.get(Calendar.HOUR_OF_DAY));
							start.put("min", repS.get(Calendar.MINUTE));
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", s.get(Calendar.MONTH));
							finish.put("day", repE.get(Calendar.DATE));
							finish.put("hour", repE.get(Calendar.HOUR_OF_DAY));
							finish.put("min", repE.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
						}
						s.set(s.get(Calendar.YEAR),s.get(Calendar.MONTH), repS.get(Calendar.DATE), repS.get(Calendar.HOUR_OF_DAY), repS.get(Calendar.MINUTE));
						s.set(Calendar.MONTH,s.get(Calendar.MONTH)+1);
//						s.setMonth(s.getMonth()+1);
					}
				}else if(ToaCalendar.REPEAT_BY_YEAR.equals(repType)){	//按年循环
					Calendar s = Calendar.getInstance(); //calStartTime
					s.setTime((Date)objs[2]);
					Calendar e = Calendar.getInstance(); //calEndTime
					e.setTime((Date)objs[3]);
					Calendar repS = Calendar.getInstance(); //calRepeatStime
					repS.setTime((Date)objs[6]);
					Calendar repE = Calendar.getInstance(); //calRepeatEtime
					repE.setTime((Date)objs[7]);
					
					while(s.getTime().getTime()<e.getTime().getTime()){
						//设置活动开始结束时间
						
//						每次月循环的开始时间ds和结束时间de
						Calendar ds = Calendar.getInstance();
						ds.set(s.get(Calendar.YEAR), repS.get(Calendar.MONTH), repS.get(Calendar.DATE),repS.get(Calendar.HOUR_OF_DAY), repS.get(Calendar.MINUTE));
						Calendar de = Calendar.getInstance();
						de.set(s.get(Calendar.YEAR), repE.get(Calendar.MONTH), repE.get(Calendar.DATE),repE.get(Calendar.HOUR_OF_DAY), repE.get(Calendar.MINUTE));
						
						if(((Date)objs[2]).getTime()>de.getTime().getTime()|((Date)objs[3]).getTime()<ds.getTime().getTime()){
							//循环时段在活动时段之外
						}else if(((Date)objs[2]).getTime()>ds.getTime().getTime()&&((Date)objs[2]).getTime()<de.getTime().getTime()){
							//活动开始时间在循环时段中
							ds.setTime((Date)objs[2]);
							
							//设置活动开始结束时间
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", ds.get(Calendar.MONTH));
							start.put("day", ds.get(Calendar.DATE));
							start.put("hour", ds.get(Calendar.HOUR_OF_DAY));
							start.put("min", ds.get(Calendar.MINUTE));
							
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", repE.get(Calendar.MONTH));
							finish.put("day", repE.get(Calendar.DATE));
							finish.put("hour", repE.get(Calendar.HOUR_OF_DAY));
							finish.put("min", repE.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
							
						}else if(((Date)objs[3]).getTime()>ds.getTime().getTime()&&((Date)objs[3]).getTime()<de.getTime().getTime()){
							//活动结束时间在循环时段中
							de.setTime((Date)objs[3]);
							//设置活动开始结束时间
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", repS.get(Calendar.MONTH));
							start.put("day", repS.get(Calendar.DATE));
							start.put("hour", repS.get(Calendar.HOUR_OF_DAY));
							start.put("min", repS.get(Calendar.MINUTE));
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", de.get(Calendar.MONTH));
							finish.put("day", de.get(Calendar.DATE));
							finish.put("hour", de.get(Calendar.HOUR_OF_DAY));
							finish.put("min", de.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
							
						}else{
							start.put("year", s.get(Calendar.YEAR));
							start.put("month", repS.get(Calendar.MONTH));
							start.put("day", repS.get(Calendar.DATE));
							start.put("hour", repS.get(Calendar.HOUR_OF_DAY));
							start.put("min", repS.get(Calendar.MINUTE));
							finish.put("year", s.get(Calendar.YEAR));
							finish.put("month", repE.get(Calendar.MONTH));
							finish.put("day", repE.get(Calendar.DATE));
							finish.put("hour", repE.get(Calendar.HOUR_OF_DAY));
							finish.put("min", repE.get(Calendar.MINUTE));
							
							jsonObj.put("id", calId);//calendarId
							jsonObj.put("description", title);//calTitle
							jsonObj.put("publicity", publicity);
							jsonObj.put("start", start);
							jsonObj.put("finish", finish);
							array.add(jsonObj);
						}
						
						s.set(s.get(Calendar.YEAR),repS.get(Calendar.MONTH), repS.get(Calendar.DATE), repS.get(Calendar.HOUR_OF_DAY), repS.get(Calendar.MINUTE));
						s.set(Calendar.YEAR, s.get(Calendar.YEAR)+1);
					}
				}else{							//不循环
					//设置活动开始结束时间
					Calendar s = Calendar.getInstance(); //calStartTime
					s.setTime((Date)objs[2]);
					Calendar e = Calendar.getInstance(); //calEndTime
					e.setTime((Date)objs[3]);
					
					start.put("year", s.get(Calendar.YEAR));
					start.put("month", s.get(Calendar.MONTH));
					start.put("day", s.get(Calendar.DATE));
					start.put("hour", s.get(Calendar.HOUR_OF_DAY));
					start.put("min", s.get(Calendar.MINUTE));
					finish.put("year", e.get(Calendar.YEAR));
					finish.put("month", e.get(Calendar.MONTH));
					finish.put("day", e.get(Calendar.DATE));
					finish.put("hour", e.get(Calendar.HOUR_OF_DAY));
					finish.put("min", e.get(Calendar.MINUTE));
					
					jsonObj.put("id", objs[1]);//calendarId
					jsonObj.put("description",title);//calTitle
					jsonObj.put("publicity", publicity);
					jsonObj.put("start", start);
					jsonObj.put("finish", finish);
					array.add(jsonObj);
				}
			}
			
			return array;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取桌面日程列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:提醒是否被删除
	 * modifyer:
	 * description:
	 * @param remindId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean hasRemindById(String remindId)throws SystemException,ServiceException{
		try{ 
			return remindManager.hasRemindById(remindId);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"日程提醒"});
		}
	}
	
	/**
	 * author:luosy
	 * description:获取日程的共享人list
	 * modifyer:
	 * description:
	 * @param remindId
	 * @return
	 */
	public List<String> getRemindSharePerson(String remindId)throws SystemException,ServiceException{
		try{
			return remindManager.getRemindSharePerson(remindId);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"日程提醒"});
		}
	}
	

	/**
	 * @author:luosy
	 * @description:获取日程默认附件大小限制
	 * @date : 2011-1-20
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getDefAttSize()throws SystemException,ServiceException{
		try{
			String baseSize=pcService.getConfigSize(ConfigModule.CALENDAR);
			return (new Long(baseSize).longValue())*1024*1024;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取新闻公告附件默认大小限制"});
		}
	}
	/**
	 * author:luosy
	 * description: 取出与指定时间段相关的日程
	 * modifyer:
	 * description:
	 * @param startCal 开始时间
	 * @param endCal   结束时间
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public Page<ToaCalendar> getListByDate(Page<ToaCalendar> page,Calendar startCal, Calendar endCal, String userId)throws SystemException,ServiceException{
		try{
			StringBuffer addhql = new StringBuffer();
			addhql.append(" from ToaCalendar as t where t.userId like '%"+ userId + "%' " );
			addhql.append(" and ( ( t.calStartTime>=? and t.calEndTime<=? ) or ( t.calStartTime<? and t.calEndTime>?) " +
					" or ( t.calStartTime<? and t.calEndTime>?) ) " +
					"order by t.calEndTime asc ");
			Object[] obj = new Object[6];
			obj[0] = startCal.getTime();
			obj[1] = endCal.getTime();
			obj[2] = startCal.getTime();
			obj[3] = startCal.getTime();
			obj[4] = endCal.getTime();
			obj[5] = endCal.getTime();
			
//			今天的日程安排
			return calendarDao.find(page,addhql.toString(),obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取桌面日程列表"});
		}
	}
	/**
	 * 获取领导日程数量
	 * author  taoji
	 * @param userId
	 * @return
	 * @date 2014-1-20 下午08:06:50
	 */
	public String getNum(String userId){
		List<String> m = calendarDao.find("select count(*) from ToaCalendar as t where t.userId='"+userId+"' and t.isLeader = '"+ToaCalendar.IS_LEADER+"'");
		if(m!=null&&m.size()>0){
			return String.valueOf(m.get(0));
		}
		return "0";
	}
	/**
	 * 获取领导日程数量
	 * author  taoji
	 * @param userId
	 * @return
	 * @date 2014-1-20 下午08:06:50
	 */
	public String getCalendarNum(String userId){
		
		//List<String> m = calendarDao.find("select count(*) from ToaCalendar as t where t.userId='"+userId+"'");
		
		StringBuffer addhql = new StringBuffer();
		addhql.append(" select count(*) from ToaCalendar as t where t.userId like '%"+ userId + "%' " );
		addhql.append(" and (  t.calStartTime>=?  ) " +
				"order by t.calEndTime asc ");
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(new Date());
		int day1=startCal.get(Calendar.DATE);  
		startCal.set(Calendar.DATE,day1-1); 
		
		Object[] obj = new Object[1];
		obj[0] = startCal.getTime();
		
		List<String> m = calendarDao.find(addhql.toString(),obj);
		
		if(m!=null&&m.size()>0){
			return String.valueOf(m.get(0));
		}
		return "0";
	}
}
