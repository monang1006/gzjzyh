/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理 日程"日程提醒" 业务类
 */
package com.strongit.oa.calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarRemind;
import com.strongit.oa.bo.ToaCalendarShare;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.im.IMManager;
import com.strongit.oa.im.IMMessageService;
import com.strongit.oa.message.IMessageService;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
/**
 * 处理 日程"日程提醒" 类
 * @Create Date: 2009-2-12
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class CalendarRemindManager {
	private Log logger = LogFactory.getLog(IMManager.class);

	private GenericDAOHibernate<ToaCalendarRemind, java.lang.String> calendarRemindDao;
	
	private IMessageService msgService;
	
	private IMailService mailService;
	
	private IsmsService smsService;
	
	private IMMessageService rtxService;
	
	@Autowired	private IWorkflowService workflowService;
	
	private CalendarManager calendarManager;
	
	@Autowired
	public void setSessionFactory(SessionFactory session) {
		calendarRemindDao = new GenericDAOHibernate<ToaCalendarRemind, java.lang.String>(
				session, ToaCalendarRemind.class);
	}
	
	@Autowired
	public void setMsgService(IMessageService msgService) {
		this.msgService = msgService;
	}
	
	@Autowired
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
	
	@Autowired
	public void setSmsService(IsmsService smsService) {
		this.smsService = smsService;
	}

	@Autowired
	public void setCalendarManager(CalendarManager calendarManager) {
		this.calendarManager = calendarManager;
	}

	/**
	 * author:luosy
	 * description:添加一个提醒
	 * modifyer:
	 * description:
	 * @param remind
	 */
	@Transactional
	public void saveRemind(ToaCalendarRemind remind) throws SystemException,ServiceException{
		try{
			if("".equals(remind.getRemindId())|null==remind.getRemindId()){
				calendarRemindDao.save(remind);
				//addTimeTask(remind);
			}else{
				calendarRemindDao.save(remind);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"添加日程提醒"});
		}
		
	}
	/**
	 * author:luosy
	 * description: 删除单个的提醒
	 * modifyer:
	 * description:
	 * @param remindId 提醒记录id
	 */
	public void delRemind(String remindId)throws SystemException,ServiceException{
		try{
			calendarRemindDao.delete(remindId);
			calendarRemindDao.flush();
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"日程提醒"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"日程提醒"});
		}
	}
	/**
	 * author:luosy
	 * description: 删除一个日程事务的所有提醒
	 * modifyer:
	 * description:
	 * @param remind
	 */
	@Transactional
	public void delRemindsByCal(ToaCalendar toaCalendar) throws SystemException,ServiceException{
		try{
			List<ToaCalendarRemind> l = calendarRemindDao.findByProperty("toaCalendar", toaCalendar);
			for(int i=0;i<l.size();i++){
				ToaCalendarRemind remind = (ToaCalendarRemind) l.get(i);
				calendarRemindDao.delete(remind);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"日程提醒"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 设置某个日程下的提醒
	 * modifyer:
	 * description:
	 */
	public void setRemindData(ToaCalendar toaCalendar,String remindData) throws SystemException,ServiceException{
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String arr[] = remindData.split(";");
			for(int i=0;i<arr.length;i++){
				ToaCalendarRemind calRemind = new ToaCalendarRemind();
				String remind[] = arr[i].split(",");
				
				remind[0] = remind[0].replace("id", "");
				if(remind[0].length()==32){
					calRemind.setRemindId(remind[0]);
				}
				try {
					calRemind.setRemindTime(format.parse(remind[1]));
				} catch (ParseException e) {
					e.printStackTrace();
					throw new ServiceException(MessagesConst.save_error,               
							new Object[] {"日程提醒"});
				}
				
				//提醒方式  MSG：0/MAIL：1
				calRemind.setRemindMethod(remind[2]);
				calRemind.setRemindCon(remind[3].toString());
//				calRemind.setRemindShare(remind[4].toString());//是否提醒共享人
				calRemind.setToaCalendar(toaCalendar);
				calRemind.setRemindStatus("1");
				
				saveRemind(calRemind);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"日程提醒"});
		}
		
	}
	

	/**
	 * @author:luosy
	 * @description:	根据流程实例ID 获取其当前处理人 （包括子流程）
	 * @date : 2011-9-9
	 * @modifyer:
	 * @description:
	 * @param taskId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List getChildrenHandler(Collection col,String taskId)throws SystemException,ServiceException{
		List handlerList = new ArrayList<String>();
		
		Object[] cols = col.toArray();
		Object[] objs = (Object[]) cols[0];
		if("task".equals(objs[0].toString())){//"任务";
			String userIds = objs[3].toString();
			String[] users = userIds.split(",");
			for(int i=0;i<users.length;i++){
				handlerList.add(users[i]);
			}
		}else{									//"子流程"；
			List list = workflowService.getMonitorChildrenInstanceIds(new Long(taskId));
			
			for(int i=0;i<list.size();i++){
				Object[] listObj = (Object[]) list.get(i);
				
				Collection<Object> ChildCol = (Collection<Object>)workflowService.getProcessStatusByPiId(listObj[0].toString())[6];
				Object[] ChildCols = ChildCol.toArray();
				Object[] ChildObjs = (Object[]) ChildCols[0];
				if(ChildObjs[0].toString().equals("task")){         //"任务";
					String ChildUserIds = ChildObjs[3].toString();
					String[] Childusers = ChildUserIds.split(",");
					for(int j=0;j<Childusers.length;j++){
						handlerList.add(Childusers[j]);
					}
				}
			}
			
		}
		
		return handlerList;
	}
	
	/**
	 * author:luosy
	 * description: 发送提醒
	 * modifyer:
	 * description:
	 * @param remind  提醒记录
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public  void sendTimeTask(ToaCalendarRemind remind)throws SystemException,ServiceException,Exception{

		String remindId = remind.getRemindId();							
		String mth = remind.getRemindMethod();							//提醒方式
		String remindShare = remind.getRemindShare();					//提醒范围
		String con = remind.getRemindCon();								//提醒内容
		String calUserName = remind.getToaCalendar().getCalUserName();	//日程创建人用户名
		String userid = remind.getToaCalendar().getUserId();			//日程创建人ID
		String taskId = remind.getRemindTaskId();						//日程提醒流程实例ID
		 
		List<String> list = new ArrayList<String>();					//日程创建人ID
		list.add(remind.getToaCalendar().getUserId());
		
		if(!"".equals(mth)){
	//流程添加的提醒
			if(!"".equals(taskId)&&null!=taskId){
				
				/**
				 * @return Object[] 流程实例运行情况<br>
				 * 数据格式为：Object[]{(0)流程实例Id，(1)流程名称，(2)流程业务Id，(3)流程业务名称，(4)流程发起人Id，(5)流程发起人名称，(6)流程运行情况}<br>
				 * 流程运行情况格式为：Collection<Object[]{(0)任务或子流程标志，(1)节点名称，(2)进入节点时间，(3)任务处理人Id，多个以,分隔}><br>
				 * 其中任务或子流程标志为：“task”：表示未完成的任务；“subProcess”：表示正在执行的子流程；
				 */
				
				Object[] objWork=workflowService.getProcessStatusByPiId(taskId);
				Collection<Object> col =null;
				if (objWork!=null){
					if(objWork[6]!=null){
						col = (Collection<Object>)objWork[6];
					}	
				}
				if(null!=col&&col.size()>0){//未结束的流程
					
					List<String> handlers =getChildrenHandler(col,taskId);
					
					if(handlers==null){
						System.out.println("\ncalendarRemindManager--->sendTimeTask--->handlers==null taskId="+taskId+";\n");
						throw new ServiceException(MessagesConst.save_error,               
								new Object[] {"handlers==null taskId="+taskId+";"});
					}
					
					String title = remind.getToaCalendar().getCalTitle();
					List<String> sharelist =calendarManager.getRemindSharePerson(remindId);
					if(mth.indexOf(ToaCalendarRemind.REMIND_BY_MSG)>-1){//内部消息
						if("0".equals(remindShare)){
							msgService.sendMsg("system", list, "流程提醒：【"+title+"】", con,GlobalBaseData.MSG_CALENDAR);
						}else if("1".equals(remindShare)){
							msgService.sendMsg("system", list, "流程提醒：【"+title+"】", con,GlobalBaseData.MSG_CALENDAR);
							msgService.sendMsg("system", handlers, "流程提醒：【"+title+"】（"+calUserName+"）", con,GlobalBaseData.MSG_CALENDAR);
						}else if("2".equals(remindShare)){
							msgService.sendMsg("system", handlers, "流程提醒：【"+title+"】（"+calUserName+"）", con,GlobalBaseData.MSG_CALENDAR);
						}
						
						if(null!=sharelist&&sharelist.size()>0){
							msgService.sendMsg("system", sharelist, "流程提醒：【"+title+"】（"+calUserName+"）", con,GlobalBaseData.MSG_CALENDAR);
						}
					}
					if(mth.indexOf(ToaCalendarRemind.REMIND_BY_MAIL)>-1){//电子邮件
						
						if("0".equals(remindShare)){
							mailService.autoSendMail(list, "流程提醒：【"+title+"】", con, "system");
						}else if("1".equals(remindShare)){
							mailService.autoSendMail(list, "流程提醒：【"+title+"】", con, "system");
							mailService.autoSendMail(handlers, "流程提醒：【"+title+"】", con, "system");
						}else if("2".equals(remindShare)){
							mailService.autoSendMail(handlers, "流程提醒：【"+title+"】", con, "system");
						}
						
						if(null!=sharelist&&sharelist.size()>0){
							mailService.autoSendMail(sharelist, "流程提醒：【"+title+"】（"+calUserName+"）", con, "system");
						}
						
					}
					if(mth.indexOf(ToaCalendarRemind.REMIND_BY_SMS)>-1){//手机短信
						if("0".equals(remindShare)){
							smsService.sendSms("system", list, con,GlobalBaseData.SMSCODE_CALENDAR);
						}else if("1".equals(remindShare)){
							smsService.sendSms("system", list, con,GlobalBaseData.SMSCODE_CALENDAR);
							smsService.sendSms("system", handlers, con,GlobalBaseData.SMSCODE_CALENDAR);
						}else if("2".equals(remindShare)){
							smsService.sendSms("system", handlers, con,GlobalBaseData.SMSCODE_CALENDAR);
						}
						
						if(null!=sharelist&&sharelist.size()>0){
							smsService.sendSms("system", sharelist, con,GlobalBaseData.SMSCODE_CALENDAR);
						}
						
					}
					if(mth.indexOf(ToaCalendarRemind.REMIND_BY_RTX)>-1){//即时通讯
						try {
							List<String> recv = new ArrayList<String>();
							recv.add(userid);
							
							Task task = new Task();
							task.setTaskId(remindId);//任务id
							task.setTaskName("");			//任务名称
							task.setJjcd("");				 			//紧急程度
							task.setCreater(calUserName);		//发起人名称
							task.setCreateTime(remind.getRemindTime());			//启动时间
							task.setBusinessName("");	//业务标题
							task.setWorkflowInstanceId("");		//流程实例id
							task.setContent(remind.getRemindCon());
							task.setHref("");
							
							if("0".equals(remindShare)){
								rtxService.sendIMMessageBySender(con, recv,task,userid);
							}else if("1".equals(remindShare)){
								rtxService.sendIMMessageBySender(con, recv,task,userid);
								rtxService.sendIMMessageBySender(con, handlers,task,userid);
							}else if("2".equals(remindShare)){
								rtxService.sendIMMessageBySender(con, handlers,task,userid);
							}
							
							if(null!=sharelist&&sharelist.size()>0){
								rtxService.sendIMMessageBySender(con, sharelist,task,userid);
							}
							
						} catch (Exception e) {
							LogPrintStackUtil.printInfo(logger, "发送rtx消息出错:\n"+e.getMessage());
							throw new ServiceException(MessagesConst.save_error,               
									new Object[] {"日程调用RTX发送提醒"});
						}
					}
					
				}
			
	//日程提醒
			}else{
				String title = remind.getToaCalendar().getCalTitle();
				List<String> sharelist =calendarManager.getRemindSharePerson(remindId);
				if(mth.indexOf(ToaCalendarRemind.REMIND_BY_MSG)>-1){//内部消息
					msgService.sendMsg("system", list, "日程提醒：【"+title+"】", con,GlobalBaseData.MSG_CALENDAR);
					if("1".equals(remindShare)){
						msgService.sendMsg("system", sharelist, "共享日程提醒：（"+calUserName+"）共享了日程【"+title+"】", con,GlobalBaseData.MSG_CALENDAR);
					}
				}
				if(mth.indexOf(ToaCalendarRemind.REMIND_BY_MAIL)>-1){//电子邮件
					mailService.autoSendMail(list, "日程提醒：【"+title+"】", con, "system");
					if("1".equals(remindShare)){
						mailService.autoSendMail(sharelist, "共享日程提醒：（"+calUserName+"）共享了日程【"+title+"】", con, "system");
					}
				}
				if(mth.indexOf(ToaCalendarRemind.REMIND_BY_SMS)>-1){//手机短信
					smsService.sendSms("system", list, "日程提醒："+con,GlobalBaseData.SMSCODE_CALENDAR);
					if("1".equals(remindShare)){
						smsService.sendSms("system", sharelist, "共享日程提醒："+con,GlobalBaseData.SMSCODE_CALENDAR);
					}
				}
				if(mth.indexOf(ToaCalendarRemind.REMIND_BY_RTX)>-1){//即时通讯
					try {
						List<String> recv = new ArrayList<String>();
						recv.add(userid);
						
						Task task = new Task();
						task.setTaskId(remindId);//任务id
						task.setTaskName("");			//任务名称
						task.setJjcd("");				 			//紧急程度
						task.setCreater(calUserName);		//发起人名称
						task.setCreateTime(remind.getRemindTime());			//启动时间
						task.setBusinessName("");	//业务标题
						task.setWorkflowInstanceId("");		//流程实例id
						task.setContent(remind.getRemindCon());
						task.setHref("");
						
						rtxService.sendIMMessageBySender("日程提醒：\n"+con, recv,task,userid);
						if("1".equals(remindShare)){
							rtxService.sendIMMessageBySender("日程提醒：\n"+con, sharelist,task,userid);
						}
					} catch (Exception e) {
						LogPrintStackUtil.printInfo(logger, "发送rtx消息出错:\n"+e.getMessage());
						throw new ServiceException(MessagesConst.save_error,               
								new Object[] {"日程调用RTX发送提醒"});
					}
				}
			}
		}

		remind.setRemindStatus("0");
		calendarManager.saveRemind(remind);
	}
	
	
	/**
	 * author:luosy
	 * description: 添加进程 定时调用接口发送提醒
	 * modifyer:
	 * description:
	 * @param remind  提醒记录
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public  void addTimeTask(ToaCalendarRemind remind)throws SystemException,ServiceException{
		final Timer timer = new Timer();
		try{
			final Date remindDate = remind.getRemindTime();
			final String con = remind.getRemindCon();
			final String title = remind.getToaCalendar().getCalTitle();
			final String mth = remind.getRemindMethod();
			final String remindId = remind.getRemindId();
			final String userid = remind.getToaCalendar().getUserId();
			final String calUserName = remind.getToaCalendar().getCalUserName();
			final String remindShare = remind.getRemindShare();
			
			final List<String> list = new ArrayList<String>();
			list.add(remind.getToaCalendar().getUserId());
			final ToaCalendarRemind calremind = calendarRemindDao.findById(remindId, false);
			/*Date remindDate = remind.getRemindTime();
			String con = remind.getRemindCon();
			String title = remind.getToaCalendar().getCalTitle();
			String mth = remind.getRemindMethod();
			String remindId = remind.getRemindId();
			String userid = remind.getToaCalendar().getUserId();
			String calUserName = remind.getToaCalendar().getCalUserName();
			String remindShare = remind.getRemindShare();
			
			List<String> list = new ArrayList<String>();
			list.add(remind.getToaCalendar().getUserId());
			ToaCalendarRemind calremind = calendarRemindDao.findById(remindId, false);*/
			timer.schedule(new TimerTask() {
				public void run() {
					
					boolean notDel = calendarManager.hasRemindById(remindId);
					if(!"".equals(mth)&&notDel){
						List<String> sharelist =calendarManager.getRemindSharePerson(remindId);
						if(mth.indexOf(ToaCalendarRemind.REMIND_BY_MSG)>-1){//内部消息
							msgService.sendMsg("system", list, "日程提醒：【"+title+"】", con,GlobalBaseData.MSG_CALENDAR);
							//if("1".equals(remindShare)){
								//msgService.sendMsg("system", sharelist, "共享日程提醒：（"+calUserName+"）共享了日程【"+title+"】", con,GlobalBaseData.MSG_CALENDAR);
							//}
							if (sharelist!=null){
								msgService.sendMsg("system", sharelist, "日程提醒：【"+title+"】", con,GlobalBaseData.MSG_CALENDAR);
							}	
						}
						if(mth.indexOf(ToaCalendarRemind.REMIND_BY_MAIL)>-1){//电子邮件
							mailService.autoSendMail(list, "日程提醒：【"+title+"】", con, "system");
							//if("1".equals(remindShare)){
								//mailService.autoSendMail(sharelist, "共享日程提醒：（"+calUserName+"）共享了日程【"+title+"】", con, "system");
							//}
							if (sharelist!=null){
								mailService.autoSendMail(sharelist, "日程提醒：【"+title+"】", con, "system");
							}
						}
						if(mth.indexOf(ToaCalendarRemind.REMIND_BY_SMS)>-1){//手机短信
							smsService.sendSms("system", list, con,GlobalBaseData.SMSCODE_CALENDAR);
							//if("1".equals(remindShare)){
								//smsService.sendSms("system", sharelist, "共享日程提醒：（"+calUserName+"）共享了日程【"+title+"】",GlobalBaseData.SMSCODE_CALENDAR);
							//}
							if (sharelist!=null){
								smsService.sendSms("system", sharelist, con,GlobalBaseData.SMSCODE_CALENDAR);
							}
						}
						if(mth.indexOf(ToaCalendarRemind.REMIND_BY_RTX)>-1){//即时通讯
							try {
								List<String> recv = new ArrayList<String>();
								recv.add(userid);
								rtxService.sendIMMessage("日程提醒："+title, recv,null);
								if("1".equals(remindShare)){
									rtxService.sendIMMessage("日程提醒："+title, sharelist,null);
//									for(String user:sharelist){
//										rtxService.sendIM("共享日程提醒：（"+calUserName+"）共享了日程【"+title+"】", user);
//									}
								}
							} catch (Exception e) {
								LogPrintStackUtil.printInfo(logger, "发送rtx消息出错:\n"+e.getMessage());
								throw new ServiceException(MessagesConst.save_error,               
										new Object[] {"日程调用RTX发送提醒"});
							}
						}
						calremind.setRemindStatus("0");
						calendarManager.saveRemind(calremind);
					}
					
				}
			},remindDate);
			
		}catch(ServiceException e){
			timer.cancel();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"日程提醒"});
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
			ToaCalendarRemind remind = calendarRemindDao.findById(remindId, false);
			if(null!=remind){
				if(null!=remind.getRemindId().toString()&&!"".equals(remind.getRemindId().toString())){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(ObjectNotFoundException e){
			return false;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"日程提醒"});
		}
	}
	
	/**
	 * author:hecj
	 * description:查找所有需要提醒的日程
	 * 
	 */
	public List<ToaCalendarRemind> findValidRemind()throws SystemException,ServiceException{
		try{
			//return calendarRemindDao.find("from ToaCalendarRemind t where t.remindStatus='1' and ROUND(TO_NUMBER(t.remindTime-sysdate)*24*60)<0");
			return calendarRemindDao.find("from ToaCalendarRemind t where t.remindStatus='1' and t.remindTime < ? ", new Date());
		}catch(ObjectNotFoundException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取提醒日程"});
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
	@SuppressWarnings("unchecked")
	public List<String> getRemindSharePerson(String remindId)throws SystemException,ServiceException{
		try{
			List<String> list = new ArrayList<String>();
			ToaCalendarRemind remind = calendarRemindDao.findById(remindId, false);
			Set<ToaCalendarShare> set = remind.getToaCalendar().getToaCalendarShares();
			if(null!=set&&set.size()>0){
				for(ToaCalendarShare share: set){
					list.add(share.getUserId());
				}
			}
			return list;
		}catch(ObjectNotFoundException e){
			return new ArrayList<String>();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"日程提醒"});
		}
	}

	@Autowired
	public void setRtxService(IMMessageService rtxService) {
		this.rtxService = rtxService;
	}

	
	
}
