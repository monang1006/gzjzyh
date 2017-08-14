package com.strongit.xxbs.dto;

import java.util.Date;

public class MailInfo
{
	private String subject;
	
	private Date date;
	
	private String mailUrl;
	
	private String content;
	
	private String orgId;
	
	private String userId;
	
	private String mailUid;

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getMailUrl()
	{
		return mailUrl;
	}

	public void setMailUrl(String mailUrl)
	{
		this.mailUrl = mailUrl;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getMailUid()
	{
		return mailUid;
	}

	public void setMailUid(String mailUid)
	{
		this.mailUid = mailUid;
	}
	
	
}
