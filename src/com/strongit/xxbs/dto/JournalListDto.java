package com.strongit.xxbs.dto;

import java.util.Date;

public class JournalListDto
{
	private String jourId;
	
	private String jourName;
	
	private Date jourDate;
	
	private int jourCode;

	public String getJourId()
	{
		return jourId;
	}

	public void setJourId(String jourId)
	{
		this.jourId = jourId;
	}

	public String getJourName()
	{
		return jourName;
	}

	public void setJourName(String jourName)
	{
		this.jourName = jourName;
	}
	
	
	
	public Date getJourDate() {
		return jourDate;
	}

	public void setJourDate(Date jourDate) {
		this.jourDate = jourDate;
	}

	public int getJourCode() {
		return jourCode;
	}

	public void setJourCode(int jourCode) {
		this.jourCode = jourCode;
	}

	
	
}
