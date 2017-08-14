package com.strongit.xxbs.dto;

public class StatisticsDto
{
	private String orgId;
	
	private long byDay;
	
	private long byMonth;
	
	private long bySeason;
	
	private long byYear;
	
	private long total;

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public long getByDay()
	{
		return byDay;
	}

	public void setByDay(long byDay)
	{
		this.byDay = byDay;
	}

	public long getByMonth()
	{
		return byMonth;
	}

	public void setByMonth(long byMonth)
	{
		this.byMonth = byMonth;
	}

	public long getBySeason()
	{
		return bySeason;
	}

	public void setBySeason(long bySeason)
	{
		this.bySeason = bySeason;
	}

	public long getByYear()
	{
		return byYear;
	}

	public void setByYear(long byYear)
	{
		this.byYear = byYear;
	}

	public long getTotal()
	{
		return total;
	}

	public void setTotal(long total)
	{
		this.total = total;
	}

}
