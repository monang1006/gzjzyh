package com.strongit.xxbs.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.strongit.xxbs.service.IGradeService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

@Service
public class GradeService implements IGradeService
{
	Properties props = new Properties();
	String path = getClass().getResource("/").getPath();
	String file = path + "xxbs.info.properties";

	public GradeService() throws IOException
	{
		FileInputStream fis = null;
		try
		{			
			fis = new FileInputStream(file);
			props.load(fis);
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

	public String get(String name) throws ServiceException, SystemException,
			DAOException
	{
		return props.getProperty(name);
	}

	public void set(String name, String value) throws ServiceException,
			SystemException, DAOException
	{
		props.setProperty(name, value);
	}

	public void save() throws ServiceException, SystemException, DAOException
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file);
			props.store(fos, null);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
