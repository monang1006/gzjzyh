package com.strongit.gzjzyh.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.strongit.gzjzyh.tosync.IToSyncManager;
import com.strongmvc.service.ServiceLocator;

public class SyncThread extends Thread {

	private Log log = LogFactory.getLog(SyncThread.class);
	private String threadName = "数据同步线程";
	private IToSyncManager syncManager;
	private boolean bAllowSleep = true;
	// 默认5分钟
	private long nIntervalTime = 300000;
	// 默认半小时
	private long nLogIntervalTime = 1800000;
	private boolean bStop = false;
    private boolean bStopped = true;

	public void Stop() {
        this.bStop = true;
        bAllowSleep = false;
    }

    public boolean isStoped() {
        return this.bStopped;
    }
    
    public void setAllowSleep(boolean bAllow) {
		bAllowSleep = bAllow;
	}
	
	public void run() {
		this.bStop = false;
		log.info(this.threadName + "启动。");
		try {
			syncManager = (IToSyncManager)ServiceLocator.getService("toSyncManager");
			
			long logSt = System.currentTimeMillis();
			while (!this.bStop) {
				bAllowSleep = false;
				try {
					int counter = syncManager.syncMsg();
					if(counter == 0) {
						// 允许休眠
						bAllowSleep = true;
					}
				} catch (Exception ex) {
					log.error(this.threadName + "：" + ex.getMessage(), ex);
				}
				// 休眠
				long st = System.currentTimeMillis();
				long ct = 0;
				do {
					sleep(1000);
					ct = System.currentTimeMillis();
					// 处理系统时间在休眠期间被改成比st(start time)更前
					if (ct < st) {
						st = ct;
					}
					// 在休眠期间唤醒
					if (!bAllowSleep) {
						if (log.isInfoEnabled()) {
							log.info("唤醒" + this.threadName + "。");
						}
						break;
					}
					// 休眠了一个周期
					if ((ct - st) >= nIntervalTime) {
						if (log.isInfoEnabled()) {
							log.info("到期唤醒" + this.threadName + "。");
						}
						break;
					}

				} while (true);
				
				long logCt = System.currentTimeMillis();
				if((logCt - logSt) >= nLogIntervalTime){
					if (log.isInfoEnabled()) {
						log.info(this.threadName + "正常运行。");
					}
					logSt = logCt;
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

		log.info(this.threadName + "停止。");
		this.bStopped = true;
	}

}
