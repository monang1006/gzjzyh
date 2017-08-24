package com.strongit.gzjzyh.schedule;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.strongit.gzjzyh.GzjzyhApplicationConfig;

public class StartScheduleListener implements ServletContextListener {

	public static SyncThread syncThread = null;

	public void contextDestroyed(ServletContextEvent sce) {
		if (syncThread != null) {
			syncThread.Stop();
			// 等待线程结束
			while (!syncThread.isStoped()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			syncThread = null;
		}
	}

	public void contextInitialized(ServletContextEvent sce) {
		if(GzjzyhApplicationConfig.isDistributedDeployed()) {
			syncThread = new SyncThread();
			syncThread.start();
		}
	}

}
