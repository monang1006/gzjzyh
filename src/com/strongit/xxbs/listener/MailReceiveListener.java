package com.strongit.xxbs.listener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;

import com.strongit.xxbs.common.DeployType;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.mail.MailReceiveTask;
import com.strongmvc.util.SpringContextUtil;

public class MailReceiveListener implements ServletContextListener
{

	private Timer timer;
	
	public void contextDestroyed(ServletContextEvent event)
	{
		timer.cancel();
		System.out.println("Mail receive listener has been stop.");
	}

	public void contextInitialized(ServletContextEvent event)
	{
		String path = getClass().getResource("/").getPath();
		String file = path + "xxbs.mail.properties";
		Properties mail = new Properties();
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(file);
			mail.load(fis);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		String interval = mail.getProperty("mail.receive.interval");
		Long val = new Long(interval);
		
		timer = new Timer(true);
		
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		DeployType depoly = (DeployType) ctx.getBean("deployType");
		if(Publish.TERMINAL_CAI.equals(depoly.getType()))
		{
			timer.schedule(new MailReceiveTask(
					event.getServletContext()), 5*60*1000, val.longValue() *60*1000);
		}
	}

}
