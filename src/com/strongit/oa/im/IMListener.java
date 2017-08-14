package com.strongit.oa.im;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.im.service.AbstractBaseService;
import com.strongit.oa.util.PathUtil;

/**
 * 即时通讯轮询器.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-9-17 下午06:08:06
 * @version  2.0.7
 * @classpath com.strongit.oa.im.IMListener
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class IMListener {

	Logger LOG = LoggerFactory.getLogger(this.getClass());

	private ReplyTimer rt = null;

	int rate = 20 * 60; //默认定义20分钟轮询一次
	
	AbstractBaseService service = null;
	
	private boolean isStart = false;//是否启动了即时通讯轮询
	
	/**
	 * 初始化方法,读取配置文件以及启动轮询器.
	 * @author:邓志城
	 * @date:2010-9-17 下午06:08:28
	 */
	public void initial() {
		PathUtil.setIMListener(this);
		service = Cache.getService();
		this.start();
	}

	/**
	 * 启动轮询器
	 * @author:邓志城
	 * @date:2010-10-13 下午02:59:25
	 */
	public void start(){
		if(!service.isEnabled()){
			LOG.error("即时通讯软件未启用，方法执行结束。");
			this.destory();
			return ;
		}
		if(!service.isLoopEnabled()){
			LOG.error("即时通讯轮询器未开启，方法执行结束。");
			return ;
		}
		String strRate = Cache.get() == null ? null : Cache.get().getRest3();
		if(strRate != null && !"".equals(strRate)) {
			rate = Integer.parseInt(strRate) * 60;
		}
		if(isStart){
			LOG.error("轮询已经是开启状态。轮询时间为：{}秒",rate);
			//return ;
			this.destory();
		}
		rt = new ReplyTimer();
		rt.start();
		isStart = true;
		LOG.error("即时通讯轮询启动。轮询时间为：{}秒",rate);
	}
	
	/**
	 * 关闭轮询器
	 * @author:邓志城
	 * @date:2010-9-17 下午06:08:49
	 */
	public void destory() {
		if(isStart){
			rt.stop();
			LOG.error("即时通讯轮询关闭");
			isStart = false;
		}
	}
	
	public class ReplyTimer {
		
		private final Timer timer = new Timer();

		public void start() {
			Date date = new Date();
			ReplyTask r = new ReplyTask();
			timer.schedule(r, date, rate * 1000);
		}

		public void stop() {
			timer.cancel();
		}
	}
	
	/**
	 * 执行任务调度
	 * @author 邓志城
	 * @company  Strongit Ltd. (C) copyright
	 * @date 2010-9-17 下午06:13:30
	 * @version  2.0.7
	 * @classpath com.strongit.oa.im.ReplyTask
	 * @comment
	 * @email dengzc@strongit.com.cn
	 */
	public class ReplyTask extends TimerTask {
		@Override
		public void run() {
			try {
				Long l1 = System.currentTimeMillis();
				LOG.error("执行轮询开始...");
				service.synchronizedUserInfo();
				Long l2 = System.currentTimeMillis();
				LOG.error("同步数据完毕,耗时{}毫秒",(l2-l1));
			} catch (Exception e) {
				LOG.error("执行同步时发生错误", e);
			}
		}
		
	}

	public static void main(String...strings){
		IMListener im = new IMListener();
		im.initial();
	}
}