package com.strongit.uums.optprivilmanage.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.im.IMListener;
import com.strongit.oa.util.PathUtil;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;

/**
 * 加载所有操作权限
 * @author Administrator
 *
 */
public class BaseOptPrivilListener implements ServletContextListener{

	private Log logger = LogFactory.getLog(BaseOptPrivilListener.class);
	
	BaseOptPrivilManager manager = null;
	
	IMListener imListener = null;
	/**
	 * 释放内存中的权限数据
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		imListener.destory();
	}

	public BaseOptPrivilListener() {
		imListener = new IMListener();
	}
	
	/**
	 * 加载权限列表到内存中
	 */
	public void contextInitialized(ServletContextEvent event) {
		/*ApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		manager = (BaseOptPrivilManager)cxt.getBean("baseOptPrivilManager");
		List<ToaUumsBaseOperationPrivil> lst = manager.getAllOptPrivilst();
		LogPrintStackUtil.printInfo(logger, "加载操作权限数据开始!");
		for(ToaUumsBaseOperationPrivil privil:lst){
			PrivilHelper.addPrivil(privil.getPrivilSyscode(), privil);
			PrivilHelper.addPrivil(privil.getPrivilId(), privil);//增加KEY为权限ID的MAP
		}
		//初始化用户权限
		initUserPrivil();
		//初始化管理员权限
		initAdminPrivil();
		//初始化岗位权限
		initPostPrivil();
		LogPrintStackUtil.printInfo(logger, "加载操作权限数据完成!");*/
		
//		设置当前工程跟路径
		PathUtil.setRootPath(event.getServletContext().getRealPath("/")); 
		logger.info("设置工程跟路径为：" + PathUtil.getRootPath());
		
		//加载资源文件
		String resourceName = event.getServletContext().getInitParameter("messageResource");
		LocalizedTextUtil.addDefaultResourceBundle(resourceName);
		logger.info("load resource file " + resourceName);
		//启动即时通讯的轮询器
		imListener.initial();
		
//		这里处理历史数据 --- 附件数据全部转移到硬盘上
		ApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		final IWorkflowAttachService attachService = (IWorkflowAttachService)cxt.getBean("workflowAttachManager");
		
		while(true) {
			boolean rb = attachService.doneHistoryData();
			if(!rb) {//当出现异常或者同步完成之后跳出循环
				logger.info("##################################################################");
				logger.info("历史数据传输完毕....");
				logger.info("##################################################################");
				break;
			}
		}
		
		/*Thread thread = new Thread(new Runnable(){

			public void run() {
				while(true) {
					boolean rb = attachService.doneHistoryData();
					if(!rb) {//当出现异常或者同步完成之后跳出循环
						logger.info("##################################################################");
						logger.info("data translate success finish....");
						logger.info("##################################################################");
						break;
					}
				}
			}
			
		});
		thread.start();*/
	}
}
