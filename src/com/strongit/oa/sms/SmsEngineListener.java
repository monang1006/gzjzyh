/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理短信监听类
 */
package com.strongit.oa.sms;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.strongit.oa.bo.ToaCalendarRemind;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.calendar.CalendarRemindManager;

/**
 * 短信发送监听类
 * @Create Date: 2009-2-21
 * @author luosy
 * @version 1.0
 */
public class SmsEngineListener implements ServletContextListener {
	
	private ServletContext context = null;
	
	private ReplyTimer rt = null;
	
	private static int rate = 60;//任务触发间隔时间(秒)
	
	boolean isSending = false;//是否正在短信发送''
	
	int runTimes = 1;
	
	List<ToaSms> toSendList = new ArrayList<ToaSms>();
	private CalendarRemindManager calendarRemindManager;
	static {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource("im.properties");
			InputStream is = url.openStream();
			Properties prop = System.getProperties();
			prop.load(is);
			
			String second = prop.getProperty("sms.intermission.bySecond");
			
			if (is != null) {
				is.close();
			}
			rate = new Integer(second);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		context.log("短信监听启动！短信猫操作间隔时间为:"+rate +"秒");
		
		rt = new ReplyTimer();
		rt.start();
	}

	public void contextDestroyed(ServletContextEvent event) {
		this.context.log("短信监听关闭！");
		rt.stop();
	}
	
	
	/**
	 * 定义定时器
	 * @Create Date: 2009-2-21
	 * @author luosy
	 * @version 1.0
	 */
	public class ReplyTimer {
		
		private final Timer timer = new Timer();

		public void start() {
			Date date = new Date();
			try {
				ReplyTask r = new ReplyTask();
				//每隔  rate秒执行一次
				timer.schedule(r, date, rate * 1000);
				
				//测试☞---生日提醒
//				long l = 86400 ;//86400即二十四小时后执行
				long l = 60;
				birthReminder b = new birthReminder();
				timer.schedule(b, date, l * 1000);	
				
			} catch (Exception   e) {
				context.log("timer.schedule(new ReplyTask(), date, min * 60 * 1000);出错  at:"+new Date());
			}
		}

		public void stop() {
			timer.cancel();
		}

	}
	
	/**
	 * 调用定时触发方法的类 
	 * @Create Date: 2009-2-21
	 * @author luosy
	 * @version 1.0
	 */
	public class ReplyTask extends TimerTask {

		private SmsService smsService;
		
		public void run() {
			try {
				context.log("\n\n###############################################\n第"+runTimes+"次调用 开始 at: "+ new Date());
				
				
				try {
					
//					定时提醒
					if (calendarRemindManager==null){
						calendarRemindManager = (CalendarRemindManager)WebApplicationContextUtils.getWebApplicationContext(context).getBean("calendarRemindManager");
					}else
					{
						List<ToaCalendarRemind> lst= calendarRemindManager.findValidRemind();
						if(lst!=null&&lst.size()>0){
							for(int i=0;i<lst.size();i++){
								calendarRemindManager.sendTimeTask(lst.get(i));
							}
						}
					}
				} catch (Exception   e) {
					e.printStackTrace();
					context.log("定时提醒(calendarRemindManager.sendTimeTask)出错  at:"+new Date());
				}
				
				
				
				
		 		if(smsService == null){
					//注入SmsService 接口
					smsService = (SmsService)WebApplicationContextUtils.getWebApplicationContext(context).getBean("smsService");
				}else{
					
//1、发送短信
					context.log("\n--------------------------------->1、发送短信 开始 at: "+ new Date());
			    	long sTime = System.currentTimeMillis();
					List<ToaSms> unSendlist = smsService.getUnSendSmsList();
					if(unSendlist!=null&&unSendlist.size()>0){
						System.out.println(" unSendList.size = "+unSendlist.size());
						smsService.sendSmsByEngine(unSendlist);
					}
			        long eTime = System.currentTimeMillis();
					context.log("\n--------------------------------->1、发送短信 开始 at: "+ new Date()+"  ==========》 共用时:"+((eTime-sTime)/1000)+"s");
//2、读短信
					context.log("\n--------------------------------->2、读短信 开始 at: "+ new Date());
					smsService.readSmsInSim();
//3、删除SIM卡上的短信
					context.log("\n--------------------------------->3、删除SIM卡上的短信 开始 at: "+ new Date());
					smsService.delSmsInSim();
				}
		 		context.log("\n第"+runTimes+"次调用 结束 at: "+ new Date()+"\n###############################################");
				runTimes++;
			} catch (Exception   e) {
				e.printStackTrace();
				context.log("ReplyTask()出错  at:"+new Date());
			}
		}
	}
	
	/**
	 * @author 张磊
	 * @create 2012-03-08
	 * @description 生日提醒功能
	 * @throws Exception
	 * */
	public class birthReminder extends TimerTask {
		private SmsService smsService;
		public void run() {
			try {
				smsService = (SmsService)WebApplicationContextUtils.getWebApplicationContext(context).getBean("smsService");
				smsService.birthReminder();
			} catch (Exception ex) {
				ex.printStackTrace();
				context.log("birthReminder()出错  at:" + new Date());
			}
		}
	}
	
}
