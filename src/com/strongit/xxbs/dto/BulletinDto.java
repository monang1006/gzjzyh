package com.strongit.xxbs.dto;

import java.util.Date;

public class BulletinDto
{
	private String blId;
	
	private String blTitle;
	
	private Date blDate;
	
	private Boolean isRead;

	public String getBlId()
	{
		return blId;
	}

	public void setBlId(String blId)
	{
		this.blId = blId;
	}

	public String getBlTitle()
	{
		return blTitle;
	}

	public void setBlTitle(String blTitle)
	{
		this.blTitle = blTitle;
	}

	public Date getBlDate()
	{
		return blDate;
	}

	public void setBlDate(Date blDate)
	{
		this.blDate = blDate;
	}

	public Boolean getIsRead()
	{
		return isRead;
	}

	public void setIsRead(Boolean isRead)
	{
		this.isRead = isRead;
	}
	
}

