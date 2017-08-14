package com.strongit.oa.relativeworkflow;

import java.util.Date;

public class ProcessInstanceDto
{
	private Long processInstanceId;
	
	private String processName;
	
	private String businessName;
	
	private String processTypeName;
	
	private String startUserId;
	
	private String startUserName;
	
	private Date processStartDate;

	public Long getProcessInstanceId()
	{
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId)
	{
		this.processInstanceId = processInstanceId;
	}

	public String getProcessName()
	{
		return processName;
	}

	public void setProcessName(String processName)
	{
		this.processName = processName;
	}

	public String getBusinessName()
	{
		return businessName;
	}

	public void setBusinessName(String businessName)
	{
		this.businessName = businessName;
	}

	public String getProcessTypeName()
	{
		return processTypeName;
	}

	public void setProcessTypeName(String processTypeName)
	{
		this.processTypeName = processTypeName;
	}

	public String getStartUserId()
	{
		return startUserId;
	}

	public void setStartUserId(String startUserId)
	{
		this.startUserId = startUserId;
	}

	public String getStartUserName()
	{
		return startUserName;
	}

	public void setStartUserName(String startUserName)
	{
		this.startUserName = startUserName;
	}

	public Date getProcessStartDate()
	{
		return processStartDate;
	}

	public void setProcessStartDate(Date processStartDate)
	{
		this.processStartDate = processStartDate;
	}
	
}
