package com.strongit.xxbs.dto;

import java.util.Date;

public class MailReceiveDto
{
	private String url;
	
	private int port;
	
	private String username;
	
	private String password;
	
	private Date startdate;
	
	private int interval;
	
	private boolean isdelete;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Date getStartdate()
	{
		return startdate;
	}

	public void setStartdate(Date startdate)
	{
		this.startdate = startdate;
	}

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	public boolean getIsdelete()
	{
		return isdelete;
	}

	public void setIsdelete(boolean isdelete)
	{
		this.isdelete = isdelete;
	}
	
}
