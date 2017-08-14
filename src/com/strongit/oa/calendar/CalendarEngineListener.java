package com.strongit.oa.calendar;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.strongit.oa.bo.ToaCalendarRemind;





/**
 * 日程提醒监听类
 * @Create Date: 2009-2-21
 * @author luosy
 * @version 1.0
 */
public class CalendarEngineListener implements ServletContextListener{
	
	private ServletContext context = null;
	
	//private ReplyTimer rt = null;
	private CalendarRemindManager calendarRemindManager;
	private static int rate = 60;//任务触发间隔时间(秒)
	
	int runTimes = 1;
	
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		context.log("日程提醒启动！操作间隔时间为:"+rate +"秒");
		
		/*if (calendarRemindManager==null){
			calendarRemindManager = (CalendarRemindManager)WebApplicationContextUtils.getWebApplicationContext(context).getBean("calendarRemindManager");
		}else
		{
			List<ToaCalendarRemind> lst= calendarRemindManager.findValidRemind();
			if(lst!=null&&lst.size()>0){
				for(int i=0;i<lst.size();i++){
					calendarRemindManager.addTimeTask(lst.get(i));
				}
			}
		}*/
		
		//rt = new ReplyTimer();
		//rt.start();
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		this.context.log("日程提醒监听关闭！");
		//rt.stop();
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
				//ReplyTask r = new ReplyTask();
				//每隔  rate秒执行一次
				//timer.schedule(r, date, rate * 1000);
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
		//private CalendarRemindManager calendarRemindManager;
		public void run() {
			try {
				context.log("\n\n###############################################\n第"+runTimes+"次日程提醒调用 开始 at: "+ new Date());
				/*if (calendarRemindManager==null){
					calendarRemindManager = (CalendarRemindManager)WebApplicationContextUtils.getWebApplicationContext(context).getBean("calendarRemindManager");
				}else
				{
 					List<ToaCalendarRemind> lst= calendarRemindManager.findValidRemind();
					if(lst!=null&&lst.size()>0){
						for(int i=0;i<lst.size();i++){
							calendarRemindManager.addTimeTask(lst.get(i));
						}
					}
				}*/
		 		context.log("\n第"+runTimes+"次日程提醒调用 结束 at: "+ new Date()+"\n###############################################");
				runTimes++;
			} catch (Exception   e) {
				e.printStackTrace();
				context.log("ReplyTask()出错  at:"+new Date());
			}
		}
	}
}
