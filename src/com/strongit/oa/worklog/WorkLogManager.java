/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理 日志 业务类
 */
package com.strongit.oa.worklog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaWorkLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 处理工作日志类
 * 
 * @Create Date: 2010-5-20
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class WorkLogManager {
	
	private GenericDAOHibernate<ToaWorkLog, java.lang.String> workLogDao;

	@Autowired private WorkLogAttachManager attachManager;

	@Autowired private IAttachmentService attachmentService;

	@Autowired private IUserService userService;
	
	public WorkLogManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory session) {
		workLogDao = new GenericDAOHibernate<ToaWorkLog, java.lang.String>(
				session, ToaWorkLog.class);
	}

	/*
	 * Description:查询列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 21, 2010 4:11:19 PM
	 */
	public Page<ToaWorkLog> searchByWorkLog(Page<ToaWorkLog> page,
			ToaWorkLog workLog,String operateType) throws SystemException,
			ServiceException {
		try {
			User user=userService.getCurrentUser();
			String CurrentUserId= user.getUserId();
			List<Object> list=new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" from ToaWorkLog t where 1=?");
			list.add(1);
			if("all".equals(operateType)){//日志查询列表	新增权限控制，只有为部分分管领导时才能查看所分管部门下人员的日志。 BY 刘皙
				hql.append(" and t.userId in (select user.userId from TUumsBaseOrg org,TUumsBaseUser user where org.orgId = user.orgId and (org.rest2 like ? ");
				list.add("%" + CurrentUserId + "%");
				hql.append("or org.orgManager like ?))");
				list.add("%" + CurrentUserId + "%");
			}else{		//个人日志列表
				hql.append(" and t.userId=?");
				list.add(CurrentUserId);
			}
			//标题
			String title = workLog.getWlogTitle();
			if (!"".equals(title) && title != null) {
				hql.append(" and t.wlogTitle like ?");
				list.add("%"+title+"%");
			}
			// 创建者姓名
			String name=workLog.getWlogUserName();
			if (name!= null&&!"".equals(name)&&!"undefined".equals(name)) {
				hql.append(" and t.wlogUserName like ?");
				list.add("%"+name+"%");
			}
			Date wlogStartTime=workLog.getWlogStartTime();	//工作日志开始时间
			Date wlogEndTime=workLog.getWlogEndTime();		//工作日志结束时间
			if(wlogEndTime!=null){
				wlogEndTime.setHours(23);
				wlogEndTime.setMinutes(59);
				wlogEndTime.setSeconds(59);
			}
			if(wlogStartTime!=null&&wlogEndTime!=null){		//开始时间和结束时间都不为空
				hql.append(" and t.wlogStartTime>=?")
					.append(" and t.wlogStartTime<=?");
				list.add(wlogStartTime);
				list.add(wlogEndTime);
			}else if(wlogStartTime!=null){					//开始时间不为空
				hql.append(" and t.wlogStartTime>=?");
				list.add(wlogStartTime);
			}else if(wlogEndTime!=null){					//结束时间不为空
				hql.append(" and t.wlogStartTime<=?");
				list.add(wlogEndTime);
			}
			Object[] objs = new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				objs[i] = list.get(i);
			}
			hql.append(" order by t.userId,t.wlogStartTime desc");
			page=workLogDao.find(page, hql.toString(),objs);
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "搜索的日志列表" });
		}
	}
	
	/*
	 * 
	 * Description:获取工作日志事务对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 4:13:20 PM
	 */
	public ToaWorkLog getWlogById(String workLogId) throws SystemException,
			ServiceException {
		try {
			ToaWorkLog workLog=workLogDao.get(workLogId);
//			workLogDao.findById(workLogId, false);
			return workLog;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取日志对象" });
		}
	}

	/*
	 * 
	 * Description:保存工作日志+附件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 2:23:20 PM
	 */
	@Transactional
	public String saveInpage(ToaWorkLog workLog, String delAttachIds,File[] file, String[] fileFileName,File wordDoc)
			throws SystemException, ServiceException {
		String msg = "yes";
		try {
			// 保存日志
			this.saveWorkLog(workLog, delAttachIds,wordDoc);
			
			// 保存新增的附件
			if (file != null) {
				Calendar cals = Calendar.getInstance();
				for (int i = 0; i < file.length; i++) {
					if (file[i] != null) {
						FileInputStream fils = null;
						try {
							fils = new FileInputStream(file[i]);
							byte[] buf = new byte[(int) file[i].length()];
							fils.read(buf);
							String ext = fileFileName[i].substring(
									fileFileName[i].lastIndexOf(".") + 1,
									fileFileName[i].length());
							String attachId = attachmentService.saveAttachment(
									fileFileName[i], buf, cals.getTime(), ext,
									"1", "注:工作日志附件", "userId");			// 添加公共附件表数据
							attachManager.saveAttach(attachId, workLog);	// 添加日志附件关联表数据
						} catch (Exception e) {
							throw new ServiceException(
									MessagesConst.save_error,
									new Object[] { "上传失败" });
						} finally {
							try {
								fils.close();
							} catch (IOException e) {
								throw new ServiceException(
										MessagesConst.save_error,
										new Object[] { "附件上传失败" });
							}
						}
					}
				}
			}
		}catch (ServiceException e) {
			msg = "no！";
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "工作日志保存" });
		}catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}

	/*
	 * 
	 * Description:保存工作日志
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 3:31:46 PM
	 */
	@Transactional
	public String saveWorkLog(ToaWorkLog workLog, String delAttachIds,File wordDoc)
			throws SystemException, ServiceException {
		try {
			//用于编辑工作日志时对附件的处理
			if (delAttachIds != null && !"".equals(delAttachIds)) {
				String id[] = delAttachIds.split(",");
				for (int i = 0; i < id.length; i++) {
					this.delAttach(id[i]);
				}
				ToaWorkLog wlog = this.getWlogById(workLog.getWorkLogId());
				Set s = wlog.getToaWorkLogAttaches();
				workLog.setToaWorkLogAttaches(s);
				wlog = null;
				workLogDao.flush();
				workLogDao.clear();
			}
			if (workLog.getWorkLogId() == null| "".equals(workLog.getWorkLogId())) {
				workLog.setWorkLogId(null);
				User user=userService.getCurrentUser();
				workLog.setUserId(user.getUserId());
				workLog.setWlogUserName(user.getUserName());
			}
			//以下是保存工作日志信息
			try{
				if(wordDoc!=null){
					FileInputStream fs = new FileInputStream(wordDoc);
					byte[] fileBuffer = new byte[(int)wordDoc.length()];
					fs.read(fileBuffer);
					workLog.setWlogCon(fileBuffer);
				}else{
					workLog.setWlogCon(new byte[1]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			workLog.setUpdateTime(new Date());
			workLog.setWlogTxtCon(HtmlUtils.htmlEscape(workLog.getWlogTxtCon()));
			workLogDao.save(workLog);
			return workLog.getWorkLogId();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "工作日志保存" });
		}
	}

	/*
	 * 
	 * Description:删除日志
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 4:05:35 PM
	 */
	@Transactional
	public void deleteWorkLog(String ids) throws SystemException,
			ServiceException, AjaxException {
		try {
			if ("".equals(ids) | null == ids) {
			}else {
				String[] id = ids.split(",");
				
				//删除工作日志附件
				String logids="";
				for(int i=0;i<id.length;i++){
					logids+=",'"+id[i]+"'";
				}
				logids=logids.substring(1);
				String hql="select t from ToaAttachment t,ToaWorkLogAttach w,ToaWorkLog l where w.toaWorkLog.workLogId=l.workLogId and t.attachId=w.attachId and l.workLogId in ("+logids+")";
				List<ToaAttachment> list=workLogDao.find(hql);
				attachmentService.deleteAttachment(list);
				
				//删除工作日志及工作日志附件关联记录
				for (int i = 0; i < id.length; i++) {
					workLogDao.delete(id[i]);
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "删除工作日志" });
		} catch (AjaxException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "删除工作日志" });
		}
	}

	/*
	 * 
	 * Description:删除所有附件及附件关联记录
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 23, 2010 9:26:41 PM
	 */
	public void delAttach(String attachId) throws SystemException,
			ServiceException {
		try {
			attachmentService.deleteAttachment(attachId);	// 删除公共附件表
			attachManager.delAttach(attachId);				// 删除附件表
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "删除日志附件" });
		}
	}

	
	/*
	 * 
	 * Description:某段时间的日志安排
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 25, 2010 9:51:06 AM
	 */
	@Transactional(readOnly = true)
	public List getListByTime(Calendar startCal, Calendar endCal) throws SystemException, ServiceException {
		try {
			String userId = userService.getCurrentUser().getUserId();
			Object[] obj = new Object[2];
			obj[0]=userId;
			obj[1]=startCal.getTime();
			StringBuffer hql=new StringBuffer("");
			hql.append("select t.wlogTitle,t.workLogId,t.wlogStartTime,t.wlogEndTime,")
				.append(" t.wlogUserName from ToaWorkLog as t where  t.userId=?")
				.append(" and t.wlogStartTime>=?")
				.append(" order by t.wlogEndTime asc ");
			return workLogDao.find(hql.toString(), obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取桌面日志列表" });
		}
	}

    /*
     * 
     * Description:将工作日志列表转化为jsonArray
     * param: 
     * @author 	    彭小青
     * @date 	    May 28, 2010 4:02:50 PM
     */
	public JSONArray makeWlogListToJSONArray(List calList, boolean canEdit)
			throws SystemException, ServiceException {
		try {
			JSONArray array = new JSONArray();
			if (null == calList) {
				return array;
			}
			for (int i = 0; i < calList.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				JSONObject start = new JSONObject();
				JSONObject finish = new JSONObject();
				Object[] objs = (Object[]) calList.get(i);
				jsonObj.put("edit", canEdit);
				// 设置活动开始结束时间
				Calendar s = Calendar.getInstance(); // calStartTime
				s.setTime((Date) objs[2]);
			/*	Calendar e = Calendar.getInstance(); // calEndTime
				e.setTime((Date) objs[3]);*/
				start.put("year", s.get(Calendar.YEAR));
				start.put("month", s.get(Calendar.MONTH));
				start.put("day", s.get(Calendar.DATE));
				start.put("hour", s.get(Calendar.HOUR_OF_DAY));
				start.put("min", s.get(Calendar.MINUTE));
				finish=start;
				/*finish.put("year", e.get(Calendar.YEAR));
				finish.put("month", e.get(Calendar.MONTH));
				finish.put("day", e.get(Calendar.DATE));
				finish.put("hour", e.get(Calendar.HOUR_OF_DAY));
				finish.put("min", e.get(Calendar.MINUTE));*/

				jsonObj.put("id", objs[1]);					
				jsonObj.put("description",objs[0].toString());		
				jsonObj.put("publicity", true);
				jsonObj.put("start", start);
				jsonObj.put("finish", finish);
				array.add(jsonObj);
			}
			return array;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取桌面日志列表" });
		}
	}
	
	
	/*
	 * 
	 * Description:打开日志说明
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jun 4, 2010 4:01:29 PM
	 */
	public void setContentToHttpResponse(HttpServletResponse response, byte[] content) throws SystemException,ServiceException{
		try{
			response.reset();
			response.setContentType("application/octet-stream");
			OutputStream output = null;
			try {
				output = response.getOutputStream();
				output.write(content);
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"打开日志说明"});
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jun 4, 2010 4:38:15 PM
	 */
	public Map<String,String> getCount()throws SystemException,ServiceException{
		try {
			String sql = "select t.work_log_id,count(t.work_log_attach_id) from t_oa_work_log_attach t group by t.work_log_id";
			ResultSet rs=workLogDao.executeJdbcQuery(sql);
			if(rs!=null){
				Map<String,String> map = new HashMap<String,String>();
				while(rs.next()){
					if(rs.getInt(2)!=0)
						map.put(rs.getString(1), String.valueOf(rs.getInt(2)));
				}
				return map;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
