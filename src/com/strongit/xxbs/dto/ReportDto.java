package com.strongit.xxbs.dto;

import java.util.Date;

public class ReportDto
{
	private String pubTitle;
	
	private String orgName;
	
	private String useScore;
	
	private String remarkScore;
	
	private Boolean isFirst;
	
	private Date useDate;
	
	private String plusScore;
	
	private String orgScore;
	
	private String infoScore;

	public String getPubTitle()
	{
		return pubTitle;
	}

	public void setPubTitle(String pubTitle)
	{
		this.pubTitle = pubTitle;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public Boolean getIsFirst()
	{
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst)
	{
		this.isFirst = isFirst;
	}

	public String getUseScore()
	{
		return useScore;
	}

	public void setUseScore(String useScore)
	{
		this.useScore = useScore;
	}

	public String getRemarkScore()
	{
		return remarkScore;
	}

	public void setRemarkScore(String remarkScore)
	{
		this.remarkScore = remarkScore;
	}

	public Date getUseDate()
	{
		return useDate;
	}

	public void setUseDate(Date useDate)
	{
		this.useDate = useDate;
	}

	public String getPlusScore()
	{
		return plusScore;
	}

	public void setPlusScore(String plusScore)
	{
		this.plusScore = plusScore;
	}

	public String getOrgScore()
	{
		return orgScore;
	}

	public void setOrgScore(String orgScore)
	{
		this.orgScore = orgScore;
	}

	public String getInfoScore()
	{
		return infoScore;
	}

	public void setInfoScore(String infoScore)
	{
		this.infoScore = infoScore;
	}
	
}
