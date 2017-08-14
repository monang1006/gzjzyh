package com.strongit.oa.attendance.autoset;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class PlanListener  implements ServletContextListener {

	private ServletContext context = null;
	
	private PlanTimer rt = null;
	
	int runTimes = 1;
	
	public void contextDestroyed(ServletContextEvent sce) {
		this.context.log("考勤自动计算监听关闭！");
		rt.stop();
	}

	public void contextInitialized(ServletContextEvent sce) {
		context = sce.getServletContext();
		context.log("考勤自动计算监听启动！考勤自动计算操作间隔时间为:1天");
		rt = new PlanTimer();
		rt.start();
		
	}
	/**
	 * 定义定时器
	 * @Create Date: 2009-2-21
	 * @author hull
	 * @version 1.0
	 */
	public class PlanTimer {
		
		private final Timer timer = new Timer();

		public void start() {
			try {
				PlanTask r = new PlanTask();
				Date date = r.getdate();
				timer.schedule(r, date, 1* 1000*60*60*24);	//每隔  1天执行一次
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
	public class PlanTask extends TimerTask {
		
		private PlanService planservice;
		
		public void run() {
			try {
				if(planservice==null){	
					planservice = (PlanService)WebApplicationContextUtils.getWebApplicationContext(context).getBean("planService");	//注入gatherManager 接口
				}
				planservice.runSave();	//执行计划
				runTimes++;
				System.out.println("考勤自动执行计划~~~~~~~~~:"+runTimes);
			} catch (Exception   e) {
				e.printStackTrace();
				context.log("ReplyTask()出错  at:"+new Date());
			}
		}
		
		/**
		 * 获取执行时间点
		 * @author hull
		 * @return
		 */
		public Date getdate(){
			planservice = (PlanService)WebApplicationContextUtils.getWebApplicationContext(context).getBean("planService");	//注入gatherManager 接口
			return planservice.getRunDate();
		}
	}
	
}


