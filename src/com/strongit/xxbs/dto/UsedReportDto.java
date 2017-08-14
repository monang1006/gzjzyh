package com.strongit.xxbs.dto;

import java.util.Date;

public class UsedReportDto
{
	private String rpId;
	
	private String rpTitle;
	
	private Date rpDate;

	public String getRpId()
	{
		return rpId;
	}

	public void setRpId(String rpId)
	{
		this.rpId = rpId;
	}

	public String getRpTitle()
	{
		return rpTitle;
	}

	public void setRpTitle(String rpTitle)
	{
		this.rpTitle = rpTitle;
	}

	public Date getRpDate() {
		return rpDate;
	}

	public void setRpDate(Date rpDate) {
		this.rpDate = rpDate;
	}

	
}
