package com.strongit.xxbs.dto;

public class JournalDto
{
	private String jourId;
	
	private String jourName;

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
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof JournalDto))
			return false;

		final JournalDto jd = (JournalDto) o;

		if (getJourId().equals(jd.getJourId()))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = 37 * result + getJourId().hashCode();
		return result;
	}
	
}
