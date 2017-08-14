package com.strongit.xxbs.mail;

import java.util.Date;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import com.strongit.xxbs.common.DeployType;
import com.strongit.xxbs.common.contant.Publish;
import com.strongmvc.util.SpringContextUtil;

public class MailReceiveTask extends TimerTask
{
	private ServletContext context;
	
	public MailReceiveTask(ServletContext context)
	{
		this.context = context;
	}
	
	@Override
	public void run()
	{
		System.out.println("Mail receive has been started.");				
		String str = (new Date()).toString();
		System.out.println(str);

		MailReceive.receive();
			
	}

}
