package com.strongit.xxbs.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.strongit.xxbs.dto.MailReceiveDto;
import com.strongit.xxbs.service.IMailReceiveService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

@Service
public class MailReceiveService implements IMailReceiveService
{
	Properties mail = new Properties();
	String path = getClass().getResource("/").getPath();
	String file = path + "xxbs.mail.properties";
	

	public MailReceiveService() throws IOException
	{
		FileInputStream fis = null;
		try
		{
			
			fis = new FileInputStream(file);
			mail.load(fis);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			fis.close();
		}
	}

	public MailReceiveDto get() throws ServiceException, SystemException, DAOException
	{
		MailReceiveDto mr = new MailReceiveDto();
		mr.setUrl(get("pop3.url"));
		Integer port = new Integer(get("pop3.port"));
		mr.setPort(port);
		mr.setUsername(get("mail.username"));
		mr.setPassword(get("mail.password"));
		
		Date startdate = null;
		String date = get("mail.receive.startdate");
		if(!("".equals(date)))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				startdate = sdf.parse(date);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		mr.setStartdate(startdate);
		
		Integer interval = new Integer(get("mail.receive.interval"));
		mr.setInterval(interval);
		Boolean isdelete = new Boolean(get("mail.isdelete"));
		mr.setIsdelete(isdelete);
		return mr;
	}

	public String get(String name) throws ServiceException, SystemException,
			DAOException
	{
		return mail.getProperty(name);
	}

	public void set(String name, String value) throws ServiceException,
			SystemException, DAOException
	{
		mail.setProperty(name, value);
	}

	public void save() throws ServiceException,
			SystemException, DAOException, IOException
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file);
			mail.store(fos, null);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			fos.close();
		}
	}

	public void save(MailReceiveDto mr) throws ServiceException,
			SystemException, DAOException, IOException
	{
		set("pop3.url", mr.getUrl());
		Integer port = mr.getPort();
		set("pop3.port", port.toString());
		set("mail.username", mr.getUsername());
		set("mail.password", mr.getPassword());
		
		String startdate = "";
		Date date = mr.getStartdate();
		if(date != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startdate = sdf.format(date);
		}
		set("mail.receive.startdate", startdate);
		
		Integer interval = mr.getInterval();
		set("mail.receive.interval", interval.toString());
		Boolean isdelete = mr.getIsdelete();
		set("mail.isdelete", isdelete.toString());
		save();
	}

}
